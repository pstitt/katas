package kata.primefactors;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimeFactorsTest {
  @ParameterizedTest
  @CsvSource({
      "1,",
      "2, 2",
      "3, 3",
      "4, 2;2",
      "12, 2;2;3",
      "52, 2;2;13",
      "150, 2;3;5;5",
      })
  void generate(int value, String expectedAsString) {
    var expected = parse(expectedAsString);
    assertEquals(expected, PrimeFactors.generate(value), "expected array of prime factors");
  }

  private List<Integer> parse(String expectedAsString) {
    if ( Strings.isBlank(expectedAsString)) return new ArrayList<Integer>();
    return Arrays.stream(expectedAsString.split(";"))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }
}