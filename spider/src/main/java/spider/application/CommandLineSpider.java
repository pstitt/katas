package spider.application;

import spider.crawler.internal.JSoupPageParser;
import spider.crawler.Spider;

public class CommandLineSpider {
  public static void main(String[] args) {
    assertValidCommandLineArguments(args);
    String startURL = args[0];
    crawlAndPrintToConsole(startURL);
  }

  private static void crawlAndPrintToConsole(String startURL) {
    var parser = new JSoupPageParser();
    var spider = new Spider(parser);
    var consolePagePrinter = new PagePrinter(System.out);
    spider.crawl(startURL, consolePagePrinter);
  }

  private static void assertValidCommandLineArguments(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Invalid command line arguments - expected a single argument with a start URL");
    }
  }
}
