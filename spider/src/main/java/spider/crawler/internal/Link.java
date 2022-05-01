package spider.crawler.internal;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;

@Slf4j
public record Link(String canonicalURL) {
  public static Link fromURL(String url) {
    return new Link(url);
  }

  public static Link fromBaseURLAndPage(String baseURL, String page) throws MalformedURLException {
    return new Link(makeCanonical(baseURL, page));
  }

  private static String makeCanonical(String baseURL, String page) throws MalformedURLException {
    if (page.matches("[A-z]*://.*")) {
      return page;
    } else if (page.startsWith("/")) {
      URL url = new URL(baseURL);
      return format("%s://%s%s", url.getProtocol(), url.getHost(), page);
    } else {
      return format("%s/%s", baseURL, page);
    }
  }

  public String getBaseURL() throws MalformedURLException {
    URL url = new URL(canonicalURL);
    if (url.getFile().equals("")) {
      return canonicalURL;
    } else {
      return canonicalURL.substring(0, canonicalURL.lastIndexOf('/'));
    }
  }

  public boolean isInternal(String baseURL) {
    try {
      String baseHost = new URL(baseURL).getHost();
      String host = new URL(canonicalURL).getHost();
      return baseHost.equals(host);
    } catch (MalformedURLException e) {
      log.warn("Unable to determine if link {} is internal to {} - assuming it is external", canonicalURL, baseURL, e);
    }
    return false;
  }
}
