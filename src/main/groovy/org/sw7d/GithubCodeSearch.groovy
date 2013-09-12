package org.sw7d

import groovy.json.JsonSlurper

class GithubCodeSearch {
    String query
    boolean loadFromFile
    File f
    Map _result
    Map getResults() {
       load()
       if (!_result) {
           URL url = new URL("https://api.github.com/search/code?q=${query}")
           String resultText = url.getText(requestProperties: ['Accept':'application/vnd.github.preview.text-match+json'])
           store(resultText)
           _result =  new JsonSlurper().parseText(resultText)
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

}
