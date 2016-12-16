package util;

import java.util.Vector;

public class Sorting {

  public interface Comparator {

    public boolean isLessThan(final Object a, final Object b);
  }

  public static void sort(final Vector input, final Comparator c) {
    // simple insertion sort.
    for (int i = 1; i < input.size(); i++) {
      for (int j = i; j > 0; j--) {
        Object a = input.elementAt(j);
        Object b = input.elementAt(j - 1);
        if (c.isLessThan(a, b)) {
          input.setElementAt(b, j);
          input.setElementAt(a, j - 1);
        }
      }
    }
  }
}
