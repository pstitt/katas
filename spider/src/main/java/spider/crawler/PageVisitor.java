package spider.crawler;

public interface PageVisitor {
  void visit(String page);

  void link(String page, String link);
}
