package kata.sortingcharacters;

import java.util.Arrays;

public class FastCharacterSorter implements CharacterSorter {
  @Override
  public String sort(String text) {
    if (text == null) return null;
    int[] counts = new int[26];
    Arrays.fill(counts, 0);

    byte[] bytes = text.toLowerCase().getBytes();
    for (int i=0; i<bytes.length; i++) {
      char ch = (char)bytes[i];
      int index = (int)ch - (int)'a';
      if (index >= 0 && index < 26) {
        counts[index]++;
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i=0; i<26; i++) {
      for (int j=0; j<counts[i]; j++) {
        sb.append((char)((int)'a'+i));
      }
    }

    return sb.toString();
  }
}
