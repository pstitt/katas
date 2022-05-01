package spider.testhelpers;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class CrawlAsserter {
  private final Map<String, List<String>> crawlDetails;

  public CrawlAsserter pageCountEquals(int expected) {
    assertEquals(expected, crawlDetails.keySet().size(), "Number of pages crawled");
    return this;
  }

  public LinksAsserter linksOnPage(String page) {
    return new LinksAsserter(this, page, crawlDetails.get(page));
  }
}