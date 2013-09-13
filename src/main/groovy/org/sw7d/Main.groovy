package org.sw7d
class Main {
  static void main(args) {
      GithubCodeSearch search = new GithubCodeSearch(query:'import+in:file+extension:java', loadFromFile: true)
      search.results.items.each {
          println it.repository.full_name+": "+it.name
          it.text_matches.each {
              println "   "+it.fragment.replaceAll(/\s/, "~")
          }
      }
  }


}
