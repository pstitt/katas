package kata.primefactors;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.math.IntMath.isPrime;

public class PrimeFactors {
  public static List<Integer> generate(int value) {
    var primeFactors = new ArrayList<Integer>();
    appendAdditionalPrimeFactors(primeFactors, value);
    return primeFactors;
  }

  private static void appendAdditionalPrimeFactors(ArrayList<Integer> primeFactors, int value) {
    if (value < 2) {
      return;
    }
    for (int i=2; i<=value; i++) {
      if (isPrime(i) && isFactor(i, value)) {
        primeFactors.add(i);
        appendAdditionalPrimeFactors(primeFactors, value/i);
        break;
      }
    }
  }

  private static boolean isFactor(int i, int value) {
    return (value/i)*i == value;
  }
}
