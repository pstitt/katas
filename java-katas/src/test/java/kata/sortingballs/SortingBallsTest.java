package kata.sortingballs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortingBallsTest {
  static final int TEN = 10;
  static final int TWENTY = 20;
  static final int THIRTY = 30;
  Rack rack = new Rack();

  @Test
  void startingRackIsEmpty() {
    assertEquals(0, rack.size());
  }

  @Test
  void oneBall() {
    rack.add(TWENTY);
    assertEquals(1, rack.size());
    assertEquals(TWENTY, rack.get(0));
  }

  @Test
  void twoBallsAddedInOrder() {
    rack.add(TWENTY);
    rack.add(THIRTY);
    assertEquals(2, rack.size());
    assertEquals(TWENTY, rack.get(0));
    assertEquals(THIRTY, rack.get(1));
  }

  @Test
  void twoBallsAddedInReverseOrder() {
    rack.add(THIRTY);
    rack.add(TWENTY);
    assertEquals(2, rack.size());
    assertEquals(TWENTY, rack.get(0));
    assertEquals(THIRTY, rack.get(1));
  }

  @Test
  void threeBallsAddedOutOfOrder() {
    rack.add(THIRTY);
    rack.add(TEN);
    rack.add(TWENTY);
    assertEquals(3, rack.size());
    assertEquals(TEN, rack.get(0));
    assertEquals(TWENTY, rack.get(1));
    assertEquals(THIRTY, rack.get(2));
  }
}
