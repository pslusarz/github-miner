package org.sw7d

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class GithubCodeSearch {
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


           _result = getOnePage("https://api.github.com/search/code?q=${query}&per_page=${perPage}")
           if (getAll && _nextUrl) {

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
        URL url = new URL(uri)
        URLConnection connection = url.openConnection()
        connection.addRequestProperty('Accept', 'application/vnd.github.preview.text-match+json')
        println "----"+connection.headerFields.Link[0]+"----" +connection.headerFields.Link[0].getClass()
        new JsonSlurper().parseText(connection.content.text)
    }

    String mapToJsonString(Map map) {
        JsonBuilder builder = new JsonBuilder()
        builder _result
        return builder.toPrettyString()
    }

}
