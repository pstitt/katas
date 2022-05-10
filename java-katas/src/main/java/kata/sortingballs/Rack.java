package kata.sortingballs;

import java.util.ArrayList;
import java.util.Collections;

public class Rack extends ArrayList<Integer> {
  @Override
  public boolean add(Integer integer) {
    if (super.add(integer)) {
      Collections.sort(this);
      return true;
    } else {
      return false;
    }
  }
}
