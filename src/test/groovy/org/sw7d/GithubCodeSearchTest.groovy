package org.sw7d

import org.junit.Ignore
import org.junit.Test

class GithubCodeSearchTest {
    @Test
    void testRetrieveViaUrl() {
      GithubCodeSearch search = new GithubCodeSearch(query:'gnubridge+in:file+extension:java')
      assert search.results.total_count > 0
    }

    @Test
    void testRetrieveViaFile() {
        GithubCodeSearch search = new GithubCodeSearch(query:'import+in:file+extension:java', loadFromFile: true)
        search.results.items.each {
            println it.repository.full_name+": "+it.name
            it.text_matches.each {
                println "   "+it.fragment.replaceAll(/\s/, "~")
            }
        }
        assert search.results.total_count > 900
    }

    @Test
    void testParseNextUrlNextExists() {
       assert "https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=2" == GithubCodeSearch.parseNextUrl(["Link":['<https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=2>; rel="next", <https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=10>; rel="last"']])
    }

    @Test
    void testParseNextUrlLast() {
        assert null == GithubCodeSearch.parseNextUrl(["Link":['<https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=1>; rel="first", <https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=9>; rel="prev"']])
    }

    @Test
    void testGetsEverything() {
        GithubCodeSearch search = new GithubCodeSearch(query:'gnubridge+in:file+extension:java', perPage: 20, getAll: true)
        assert 120 < search.results.items.size()
    }
}
