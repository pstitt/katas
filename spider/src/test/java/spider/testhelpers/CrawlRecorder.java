package spider.testhelpers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import spider.crawler.PageVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public class CrawlRecorder implements PageVisitor {
  private Map<String, List<String>> crawlDetails = new HashMap<>();

  @Override
  public void visit(String page) {
    log.info("Page Visit: {}", page);
    crawlDetails.put(page, new ArrayList<>());
  }

  @Override
  public void link(String page, String link) {
    log.info("Found link on page: {} : {}", page, link);
    crawlDetails.get(page).add(link);
  }

  public CrawlAsserter then() {
    return new CrawlAsserter(crawlDetails);
  }
}
