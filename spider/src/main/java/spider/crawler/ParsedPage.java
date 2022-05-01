package spider.crawler;

import java.util.List;

public record ParsedPage(String url, List<String> links) {
}
