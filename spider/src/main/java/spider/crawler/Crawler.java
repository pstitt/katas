package spider.crawler;

public interface Crawler {
  void crawl(String page, PageVisitor pageVisitor) throws InterruptedException;
}
