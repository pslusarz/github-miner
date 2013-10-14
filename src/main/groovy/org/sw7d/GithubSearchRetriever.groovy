package org.sw7d

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class GithubSearchRetriever {
    String type = 'code'
    String query
    int perPage=100
    boolean getAll = false
    boolean loadFromFile
    String _nextUrl
    File f
    Map _result
    Map getResults() {
       load()
       if (!_result) {
           _result = getOnePage("https://api.github.com/search/${type}?q=${query}&per_page=${perPage}")
           while (getAll && _nextUrl) {
             _result.items.addAll (getOnePage(_nextUrl).items)
           }
           store(mapToJsonString(_result))

       }
        _result
    }

    void load() {
        f = new File("src/main/resources/queries/query"+query.replaceAll("[^a-zA-Z0-9]", "_")+".json")
        if (!loadFromFile) {
            return
        }
        if (!f.exists()) {
            throw new RuntimeException("no file to load from: "+f.getAbsolutePath())
        }
        _result =  new JsonSlurper().parseText(f.text)

    }

    void store(String resultText) {
        if (f.exists()) {
            f.delete()
        }
        f.parentFile.mkdirs()
        f << resultText
    }

    Map getOnePage(String uri) {
        println "  now retrieving: "+uri
        URL url = new URL(uri)
        URLConnection connection = url.openConnection()
        connection.addRequestProperty('Accept', 'application/vnd.github.preview.text-match+json')

        _nextUrl = parseNextUrl(connection.headerFields)
        if (_nextUrl && connection.headerFields['X-RateLimit-Remaining'][0] == '0') {
            int waitSeconds = Integer.parseInt(connection.headerFields['X-RateLimit-Reset'][0]) - System.currentTimeMillis()/1000  +1
            println "---> need to wait seconds: "+waitSeconds
            sleep (waitSeconds *1000)
        }
        new JsonSlurper().parseText(connection.content.text)
    }

    String mapToJsonString(Map map) {
        JsonBuilder builder = new JsonBuilder()
        builder _result
        return builder.toPrettyString()
    }

    // if there are more:
    // Link=[<https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=2>; rel="next", <https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=10>; rel="last"]
    // if this is the last one
    // Link=[<https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=1>; rel="first", <https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=9>; rel="prev"]

    static String parseNextUrl(Map headers) {
       if (!headers.Link || !headers.Link[0]) {
           return null
       }
       String linkContent = headers.Link[0]
       String firstLinkChunk = linkContent.split(',')[0]
       if (!firstLinkChunk.contains(/rel="next"/)) {
           return null
       }
       String result = firstLinkChunk.split(';')[0][1..-2]
       return result
    }

}
