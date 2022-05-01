package spider.crawler.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {
  @Test
  void determineCanonicalUrlFromValidCanonicalUrl() {
    final String CANONICAL_URL = "http://www.google.com";
    Link link = Link.fromURL(CANONICAL_URL);
    assertEquals(CANONICAL_URL, link.canonicalURL());
  }

  @Test
  void determineCanonicalUrlFromBaseURLAndRelativePath() throws MalformedURLException {
    final String BASE_URL = "http://www.google.com/level1";
    final String RELATIVE_PATH = "level2";
    final String CANONICAL_URL = "http://www.google.com/level1/level2";
    Link link = Link.fromBaseURLAndPage(BASE_URL, RELATIVE_PATH);
    assertEquals(CANONICAL_URL, link.canonicalURL());
  }

  @Test
  void determineCanonicalUrlFromBaseUrlAndCanonicalPath() throws MalformedURLException {
    final String BASE_URL = "http://www.google.com/level1";
    final String CANONICAL_PATH = "/help";
    final String CANONICAL_URL = "http://www.google.com/help";
    Link link = Link.fromBaseURLAndPage(BASE_URL, CANONICAL_PATH);
    assertEquals(CANONICAL_URL, link.canonicalURL());
  }

  @Test
  void determineCanonicalUrlFromBaseUrlAndCanonicalUrl() throws MalformedURLException {
    final String BASE_URL = "http://www.google.com/level1";
    final String CANONICAL_URL = "http://www.facebook.com";
    Link link = Link.fromBaseURLAndPage(BASE_URL, CANONICAL_URL);
    assertEquals(CANONICAL_URL, link.canonicalURL());
  }

  @ParameterizedTest
  @CsvSource({"http://a.b.c/d/e, http://a.b.c/d", "http://a.b.c, http://a.b.c"})
  void determineBaseURLFromValidURLs(String url, String expectedBaseUrl) throws MalformedURLException {
    assertEquals(expectedBaseUrl, Link.fromURL(url).getBaseURL());
  }

  @ParameterizedTest
  @CsvSource({"http://a.b.c/d/e, http://a.b.c/d", "http://a.b.c, http://a.b.c/e"})
  void correctlyIdentifiesInternalUrl(String url1, String url2) {
    assertTrue(Link.fromURL(url1).isInternal(url2));
  }

  @ParameterizedTest
  @CsvSource({"http://a.b.c/d/e, http://f.g.h", "http://a1.b.c, http://a2.b.c"})
  void correctlyIdentifiesExternalUrl(String url1, String url2) {
    assertFalse(Link.fromURL(url1).isInternal(url2));
  }
}
