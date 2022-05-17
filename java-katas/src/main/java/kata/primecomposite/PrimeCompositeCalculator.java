package kata.primecomposite;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PrimeCompositeCalculator {
  final int start;
  final int end;

  public static PrimeCompositeCalculator withRange(int start, int end) {
    assertValidRange(start, end);
    return new PrimeCompositeCalculator(start, end);
  }

  public static PrimeCompositeCalculator withSingleValue(int value) {
    return PrimeCompositeCalculator.withRange(value, value);
  }

  private static void assertValidRange(int start, int end) {
    if (start > end) throw new IllegalArgumentException("invalid range: start must be <= end");
    if (start < 0) throw new IllegalArgumentException("invalid range: start must be >= 0");
  }

  public String[] process() {
    int length = end-start+1;
    String[] result = new String[length];

    for (int i=0; i<length; i++) {
      int number = i+start;
      if (isPrime(number)) {
        result[i] = "prime";
      } else if (isComposite(number)) {
        result[i] = "composite";
      } else {
        result[i] = value(number);
      }
    }

    return result;
  }

  private boolean isPrime(int number) {
    if (number < 2) return false;
    for (int i=2; i<=number/2; i++) {
      if (number%i==0) return false;
    }
    return true;
  }

  private boolean isComposite(int number) {
    for (int i=3; i<number/2; i++) {
      if (number%i==0) return true;
    }
    return false;
  }

  private static String value(int number) {
    return Integer.toString(number);
  }
}
