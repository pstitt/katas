package spider.application;

import spider.crawler.PageVisitor;

import java.io.PrintStream;

public record PagePrinter(PrintStream out) implements PageVisitor {
  @Override
  public void visit(String page) {
    out.printf("Page Visit: %s%n", page);
  }

  @Override
  public void link(String page, String link) {
    out.printf("Found link on page: %s : %s%n", page, link);
  }
}
