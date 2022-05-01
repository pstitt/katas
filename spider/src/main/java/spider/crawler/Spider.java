package spider.crawler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spider.crawler.internal.Link;
import spider.crawler.internal.PageParser;
import spider.crawler.internal.ParsedPage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class Spider implements Crawler {
  private final PageParser parser;
  private final SpiderContext context = new SpiderContext();

  @Override
  public void crawl(String pageURL, PageVisitor pageVisitor) {
    final var link = toLink(pageURL);

    if (context.alreadyVisited(link)) {
      return;
    }

    context.markLinkAsVisited(link);
    pageVisitor.visit(link.canonicalURL());
    parsePageAndProcess(link, pageVisitor);
  }

  private void parsePageAndProcess(Link link, PageVisitor pageVisitor) {
    final Consumer<ParsedPage> processLinks = parsedPage -> processLinks(parsedPage, pageVisitor);
    parsePage(link.canonicalURL()).ifPresent(processLinks);
  }

  private Optional<ParsedPage> parsePage(String url) {
    try {
      var parsedPage = parser.parse(url);
      return Optional.of(parsedPage);
    } catch (IOException e) {
      log.warn("Unable to parse html for page: {}", url, e);
      return Optional.empty();
    }
  }

  private void processLinks(ParsedPage page, PageVisitor pageVisitor) {
    page.links().forEach(link -> processLink(page.url(), link, pageVisitor));
  }

  private void processLink(String page, String linkURL, PageVisitor pageVisitor) {
    pageVisitor.link(page, linkURL);
    Link link = toLink(linkURL);
    if (link.isInternal(context.baseURL)) {
      followLink(linkURL, pageVisitor);
    }
  }

  private void followLink(String link, PageVisitor pageVisitor) {
    crawl(link, pageVisitor);
  }

  private Link toLink(String pageURL) {
    if (context.baseURL == null) {
      return Link.fromURL(pageURL);
    } else {
      try {
        return Link.fromBaseURLAndPage(context.baseURL, pageURL);
      } catch (MalformedURLException e) {
        log.warn("Unable to parse link URL: {}", pageURL, e);
        return Link.fromURL(pageURL);
      }
    }
  }

  private static class SpiderContext {
    private String baseURL;
    private final Set<String> visitedPages = new HashSet<>();

    private void markLinkAsVisited(Link link) {
      setBaseURLIfNotSet(link);
      visitedPages.add(link.canonicalURL());
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

    private boolean alreadyVisited(Link link) {
      return visitedPages.contains(link.canonicalURL());
    }
  }
}
