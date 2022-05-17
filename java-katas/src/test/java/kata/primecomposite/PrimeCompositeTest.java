package kata.primecomposite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrimeCompositeTest {

  @Test
  void invalidRangeStartGreaterThanEnd() {
    assertThrows(
        IllegalArgumentException.class,
        () -> PrimeCompositeCalculator.withRange(1, 0));
  }

  @Test
  void invalidRangeStartNegative() {
    assertThrows(
        IllegalArgumentException.class,
        () -> PrimeCompositeCalculator.withRange(-1, 0));
  }

  @Test
  void range0_0() {
    assertArrayEquals(new String[] {"0"}, PrimeCompositeCalculator.withSingleValue(0).process());
  }

  @Test
  void range0_1() {
    assertArrayEquals(new String[] {"0", "1"}, PrimeCompositeCalculator.withRange(0, 1).process());
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 5, 7})
  void prime(int prime) {
    assertArrayEquals(
        new String[] {"prime"}, PrimeCompositeCalculator.withSingleValue(prime).process());
  }

  @ParameterizedTest
  @ValueSource(ints = {9, 15, 21, 25, 49, 63, 121})
  void composite(int composite) {
    assertArrayEquals(
        new String[] {"composite"},
        PrimeCompositeCalculator.withSingleValue(composite).process());
  }

  @Test
  void range0_10() {
    assertArrayEquals(
        new String[] {"0", "1", "prime", "prime", "4", "prime", "6", "prime", "8", "composite", "10"},
        PrimeCompositeCalculator.withRange(0, 10).process());
  }
}
