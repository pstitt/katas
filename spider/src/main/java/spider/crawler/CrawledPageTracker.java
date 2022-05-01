package spider.crawler;

import java.util.HashSet;
import java.util.Set;

public class CrawledPageTracker {
  private final Set<String> visitedPages = new HashSet<>();

  public void markVisited(String page) {
    visitedPages.add(page);
  }

  public boolean alreadyVisited(String page) {
    return visitedPages.contains(page);
  }
}
