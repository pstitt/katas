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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class Spider implements Crawler {
  private final PageParser parser;
  private final SpiderContext context = new SpiderContext();
  private ExecutorService executor;

  @Override
  public void crawl(String startURL, PageVisitor pageVisitor) throws InterruptedException {
    executor = Executors.newFixedThreadPool(10);
    processPage(startURL, pageVisitor);
    waitToComplete();
    executor.shutdown();
  }

  private void waitToComplete() throws InterruptedException {
    // A bit of a hack - couldn't quickly figure out a way of using ExecutorService to allow new tasks and wait for current and new tasks to finish
    Thread.sleep(50);
    while (context.stillProcessing()) {
      log.debug(
          "Waiting for crawl to complete. Current pages being processed = {}",
          context.getNumberOfPagesBeingProcessed());
      Thread.sleep(5000);
    }
    log.debug("Crawl complete.");
  }

  private void processPage(String pageURL, PageVisitor pageVisitor) {
    final var link = toLink(pageURL);
    synchronized (context) {
      if (context.alreadyVisited(link)) {
        return;
      }
      context.startedPage();
      context.markLinkAsVisited(link);
    }

    Runnable processPageTask =
        () -> {
          synchronized (context) {
            pageVisitor.visit(link.canonicalURL());
          }
          parsePageAndProcess(link, pageVisitor);

          synchronized (context) {
            context.finishedPage();
          }
        };
    executor.execute(processPageTask);
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
    processPage(link, pageVisitor);
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
    private int pagesCurrentlyBeingProcessed = 0;

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

    private void startedPage() {
      pagesCurrentlyBeingProcessed++;
    }

    private void finishedPage() {
      pagesCurrentlyBeingProcessed--;
    }

    private int getNumberOfPagesBeingProcessed() {
      return pagesCurrentlyBeingProcessed;
    }

    private boolean stillProcessing() {
      return pagesCurrentlyBeingProcessed > 0;
    }
  }
}
