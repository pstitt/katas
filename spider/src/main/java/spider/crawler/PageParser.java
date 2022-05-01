package spider.crawler;

import java.io.IOException;

public interface PageParser {
  ParsedPage parse(String page) throws IOException;
}
