package spider.crawler;

import java.net.MalformedURLException;

public interface Crawler {
  void crawl(String page, PageVisitor pageVisitor) throws MalformedURLException;
}
