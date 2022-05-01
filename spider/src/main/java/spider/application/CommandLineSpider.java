package spider.application;

import spider.crawler.internal.JSoupPageParser;
import spider.crawler.Spider;

public class CommandLineSpider {
  public static void main(String[] args) throws InterruptedException {
    String startURL = args[0];
    try {
      assertValidCommandLineArguments(args);
      crawlAndPrintToConsole(startURL);
    } catch (Exception e) {
      System.err.printf("Crawl of %s ended with exception", startURL);
      e.printStackTrace();
      throw e;
    }
  }

  private static void crawlAndPrintToConsole(String startURL) throws InterruptedException {
    var parser = new JSoupPageParser();
    var spider = new Spider(parser);
    var consolePagePrinter = new PagePrinter(System.out);
    spider.crawl(startURL, consolePagePrinter);
  }

  private static void assertValidCommandLineArguments(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException(
          "Invalid command line arguments - expected a single argument with a start URL");
    }
  }
}
