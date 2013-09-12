package org.sw7d

import org.junit.Test

class GithubCodeSearchTest {
    @Test
    void testRetrieveViaUrl() {
      GithubCodeSearch search = new GithubCodeSearch(query:'import+in:file+extension:java')
      assert search.results.total_count > 0
    }

    @Test
    void testRetrieveViaFile() {
        GithubCodeSearch search = new GithubCodeSearch(query:'import+in:file+extension:java', loadFromFile: true)
        assert search.results.total_count > 0
    }

    @Test
    void testDisplayResults() {
        GithubCodeSearch search = new GithubCodeSearch(query:'import+in:file+extension:java', loadFromFile: true)
        search.results.items.each {
            println it.name + " " + it.repository.full_name
            it.text_matches.each {
                println "  "+it.fragment
            }
        }
    }
}
