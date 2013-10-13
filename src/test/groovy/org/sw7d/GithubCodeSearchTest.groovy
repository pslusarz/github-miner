package org.sw7d

import org.junit.Test

class GithubCodeSearchTest {
    @Test
    void testRetrieveViaUrl() {
      GithubSearchRetriever search = new GithubSearchRetriever(query:'gnubridge+in:file+extension:java')
      assert search.results.total_count > 0
    }

    @Test
    void testRetrieveViaFile() {
        GithubSearchRetriever searchViaUrl = new GithubSearchRetriever(query:'while+in:file+extension:java', loadFromFile: false)
        assert searchViaUrl.results.total_count > 0, "precondition"
        GithubSearchRetriever searchFromFile = new GithubSearchRetriever(query:'while+in:file+extension:java', loadFromFile: true)

        assert searchFromFile.results == searchViaUrl.results
    }

    @Test
    void testParseNextUrlNextExists() {
       assert "https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=2" == GithubSearchRetriever.parseNextUrl(["Link":['<https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=2>; rel="next", <https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=10>; rel="last"']])
    }

    @Test
    void testParseNextUrlLast() {
        assert null == GithubSearchRetriever.parseNextUrl(["Link":['<https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=1>; rel="first", <https://api.github.com/search/code?q=import+in%3Afile+extension%3Ajava&per_page=100&page=9>; rel="prev"']])
    }

    @Test
    void testGetsEverything() {
        GithubSearchRetriever search = new GithubSearchRetriever(query:'gnubridge+in:file+extension:java', perPage: 20, getAll: true)
        assert 120 < search.results.items.size()
    }
}
