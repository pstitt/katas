package kata.sortingcharacters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SortingCharactersTest {
  private static final String EMPTY_STRING = "";
  CharacterSorter sorter = new FastCharacterSorter();

  @Test
  void mixedSentence() {
    String INPUT = """
      When not studying nuclear physics, Bambi likes to play
      beach volleyball.
    """;
    assertEquals("aaaaabbbbcccdeeeeeghhhiiiiklllllllmnnnnooopprsssstttuuvwyyyy", sorter.sort(INPUT));
  }

  @Test
  void nonAlpha() {
    String INPUT = "aa £b^c*d";
    assertEquals("aabcd", sorter.sort(INPUT));
  }

  @Test
  void duplicateMixedcaseCharacters() {
    String INPUT = "AaBbCcDdd";
    assertEquals("aabbccddd", sorter.sort(INPUT));
  }

  @Test
  void duplicateLowercaseCharacters() {
    String INPUT = "aabbccddd";
    assertEquals("aabbccddd", sorter.sort(INPUT));
  }

  @Test
  void uniqueLowercaseCharacters() {
    String INPUT = "abcd";
    assertEquals("abcd", sorter.sort(INPUT));
  }

  @Test
  void emptyInput() {
    assertEquals(EMPTY_STRING, sorter.sort(EMPTY_STRING));
  }

  @Test
  void nullInput() {
    assertNull(sorter.sort(null));
  }
}
