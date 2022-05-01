package spider.crawler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spider.crawler.internal.CrawledPageTracker;
import spider.crawler.internal.Link;
import spider.crawler.internal.PageParser;
import spider.crawler.internal.ParsedPage;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@RequiredArgsConstructor
public class Spider implements Crawler {
  private final PageParser parser;
  private String baseURL;
  private final CrawledPageTracker tracker = new CrawledPageTracker();

  @Override
  public void crawl(String pageURL, PageVisitor pageVisitor) {
    Link link = toLink(pageURL);
    setBaseURLIfNotSet(link);
    var canonicalURL = link.canonicalURL();

    if (tracker.alreadyVisited(canonicalURL)) {
      return;
    }
    tracker.markVisited(canonicalURL);

    pageVisitor.visit(canonicalURL);

    try {
      var parsedPage = parser.parse(canonicalURL);
      processLinks(parsedPage, pageVisitor);
    } catch (IOException e) {
      log.warn("Unable to parse html for page: {}", pageURL, e);
    }
  }

  private Link toLink(String pageURL) {
    if (baseURL == null) {
      return Link.fromURL(pageURL);
    } else {
      try {
        return Link.fromBaseURLAndPage(baseURL, pageURL);
      } catch (MalformedURLException e) {
        log.warn("Unable to parse link URL: {}", pageURL, e);
        return Link.fromURL(pageURL);
      }
    }
  }

  private void setBaseURLIfNotSet(Link link) {
    if (baseURL == null) {
      try {
        baseURL = link.getBaseURL();
      } catch (MalformedURLException e) {
        log.warn("Unable to get base URL for: {}", link.canonicalURL(), e);
      }
    }
  }

  private void processLinks(ParsedPage page, PageVisitor pageVisitor) {
    page.links().forEach(link -> processLink(page.url(), link, pageVisitor));
  }

  private void processLink(String page, String linkURL, PageVisitor pageVisitor) {
    pageVisitor.link(page, linkURL);
    Link link = toLink(linkURL);
    if (link.isInternal(baseURL)) {
      followLink(linkURL, pageVisitor);
    }
  }

  private void followLink(String link, PageVisitor pageVisitor) {
    crawl(link, pageVisitor);
  }
}
