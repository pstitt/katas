package spider.testhelpers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class LinksAsserter {
  private final CrawlAsserter crawlAsserter;
  private final String page;
  private final List<String> actual;

  public CrawlAsserter matches(List<String> expected) {
    assertEquals(expected, actual, "Links on page " + page);
    return crawlAsserter;
  }
}
