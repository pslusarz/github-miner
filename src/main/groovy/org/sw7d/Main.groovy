package org.sw7d
class Main {
  static void main(args) {
      GithubSearchRetriever search = new GithubSearchRetriever(query:'import+in:file+extension:java', loadFromFile: true)
      search.results.items.each {
          println it.repository.full_name+": "+it.name
          it.text_matches.each {
              println "   "+it.fragment.replaceAll(/\s/, "~")
          }
      }
  }


}
