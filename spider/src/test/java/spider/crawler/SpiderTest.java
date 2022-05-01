package spider.crawler;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spider.crawler.internal.PageParser;
import spider.crawler.internal.ParsedPage;
import spider.testhelpers.CrawlRecorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SpiderTest {
  private static final List<String> NO_LINKS = new ArrayList<>();

  @Mock PageParser parser;
  Spider spider;
  CrawlRecorder crawlRecorder = new CrawlRecorder();

  @BeforeEach
  void init() {
    spider = new Spider(parser);
  }

  @Test
  void doNotFollowPageWithNoLinks() throws IOException, InterruptedException {
    var URL = "https://www.spidertest.com/start.html";
    var EXPECTED_LINKS = NO_LINKS;
    givenStartPage(URL)
        .andPageWithLinks(URL, EXPECTED_LINKS)
        .whenCrawled()
        .then()
        .pageCountEquals(1)
        .linksOnPage(URL).matches(EXPECTED_LINKS);
  }

  @Test
  void followInternalCanonicalLinks() throws IOException, InterruptedException {
    var URL_1 = "https://www.spidertest.com/1.html";
    var URL_2 = "https://www.spidertest.com/11.html";
    var EXPECTED_LINKS_1 = links(URL_2);
    var EXPECTED_LINKS_2 = NO_LINKS;
    givenStartPage(URL_1)
        .andPageWithLinks(URL_1, EXPECTED_LINKS_1)
        .andPageWithLinks(URL_2, EXPECTED_LINKS_2)
        .whenCrawled()
        .then()
        .pageCountEquals(2)
        .linksOnPage(URL_1).matches(EXPECTED_LINKS_1)
        .linksOnPage(URL_2).matches(EXPECTED_LINKS_2);
  }

  @Test
  void doNotFollowPreviouslyVisitedLinks() throws IOException, InterruptedException {
    var URL = "https://www.spidertest.com/start.html";
    var EXPECTED_LINKS = links(URL);
    givenStartPage(URL)
        .andPageWithLinks(URL, EXPECTED_LINKS)
        .whenCrawled()
        .then()
        .pageCountEquals(1)
        .linksOnPage(URL).matches(EXPECTED_LINKS);
  }

  @Test
  void followInternalCanonicalPathLinks() throws IOException, InterruptedException {
    var URL_1 = "https://www.spidertest.com/a/b.html";
    var URL_2 = "/c.html";
    var CANONICAL_URL_2 = "https://www.spidertest.com/c.html";
    var EXPECTED_LINKS_1 = links(URL_2);
    var EXPECTED_LINKS_2 = NO_LINKS;
    givenStartPage(URL_1)
        .andPageWithLinks(URL_1, EXPECTED_LINKS_1)
        .andPageWithLinks(CANONICAL_URL_2, EXPECTED_LINKS_2)
        .whenCrawled()
        .then()
        .pageCountEquals(2)
        .linksOnPage(URL_1).matches(EXPECTED_LINKS_1)
        .linksOnPage(CANONICAL_URL_2).matches(EXPECTED_LINKS_2);
  }

  @Test
  void followInternalRelativePathLinks() throws IOException, InterruptedException {
    var URL_1 = "https://www.spidertest.com/a/b.html";
    var URL_2 = "c.html";
    var CANONICAL_URL_2 = "https://www.spidertest.com/a/c.html";
    var EXPECTED_LINKS_1 = links(URL_2);
    var EXPECTED_LINKS_2 = NO_LINKS;
    givenStartPage(URL_1)
        .andPageWithLinks(URL_1, EXPECTED_LINKS_1)
        .andPageWithLinks(CANONICAL_URL_2, EXPECTED_LINKS_2)
        .whenCrawled()
        .then()
        .pageCountEquals(2)
        .linksOnPage(URL_1).matches(EXPECTED_LINKS_1)
        .linksOnPage(CANONICAL_URL_2).matches(EXPECTED_LINKS_2);
  }

  @Test
  void doNotFollowExternalLinks() throws IOException, InterruptedException {
    var URL = "https://www.spidertest.com/1.html";
    var LINK = "http://a.b.c";
    var EXPECTED_LINKS = links(LINK);
    givenStartPage(URL)
        .andPageWithLinks(URL, EXPECTED_LINKS)
        .whenCrawled()
        .then()
        .pageCountEquals(1)
        .linksOnPage(URL).matches(EXPECTED_LINKS);
  }

  @Test
  void doNotFollowSubdomainLinks() throws IOException, InterruptedException {
    var URL = "https://www.spidertest.com/1.html";
    var LINK = "https://support.spidertest.com";
    var EXPECTED_LINKS = links(LINK);
    givenStartPage(URL)
        .andPageWithLinks(URL, EXPECTED_LINKS)
        .whenCrawled()
        .then()
        .pageCountEquals(1)
        .linksOnPage(URL).matches(EXPECTED_LINKS);
  }

  @Test
  void doNotFollowInvalidLinks() throws IOException, InterruptedException {
    var URL = "https://www.spidertest.com/1.html";
    var LINK = "https://support.spidertest.com/invalid£link£with£pounds";
    var EXPECTED_LINKS = links(LINK);
    givenStartPage(URL)
        .andPageWithLinks(URL, EXPECTED_LINKS)
        .whenCrawled()
        .then()
        .pageCountEquals(1)
        .linksOnPage(URL).matches(EXPECTED_LINKS);
  }

  private PageParserMockConfigurer givenStartPage(String url) {
    return new PageParserMockConfigurer(spider, crawlRecorder, parser, url);
  }

  private List<String> links(String... links) {
    var result = new ArrayList<String>();
    Arrays.asList(links).forEach(result::add);
    return result;
  }
}

@RequiredArgsConstructor
class PageParserMockConfigurer {
  final Spider spider;
  final CrawlRecorder crawlRecorder;
  final PageParser parser;
  final String startURL;

  public PageParserMockConfigurer andPageWithLinks(String page, List<String> links) throws IOException {
    Mockito.when(parser.parse(page)).thenReturn(new ParsedPage(page, links));
    return this;
  }

  public CrawlRecorder whenCrawled() throws InterruptedException {
    spider.crawl(startURL, crawlRecorder);
    return crawlRecorder;
  }
}