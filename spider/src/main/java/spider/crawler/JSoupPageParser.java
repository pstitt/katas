package spider.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JSoupPageParser implements PageParser {
  @Override
  public ParsedPage parse(String page) throws IOException {
    var doc =
        Jsoup.connect(page)
            .data("query", "Java")
            .userAgent("Mozilla")
            .cookie("auth", "token")
            .timeout(3000)
            .get();

    List<String> links =
        doc.select("a[href]").stream().map(element -> element.attr("href")).toList();

    return new ParsedPage(page, links);
  }
}
