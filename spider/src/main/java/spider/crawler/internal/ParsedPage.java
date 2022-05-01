package spider.crawler.internal;

import java.util.List;

public record ParsedPage(String url, List<String> links) {
}
