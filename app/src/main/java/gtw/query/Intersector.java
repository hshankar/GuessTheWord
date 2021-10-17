package gtw.query;

import java.util.Arrays;


// Intersect sorted int arrays
public class Intersector {
  private int[] _intersection;
  private boolean[] _valid;
  private int _count;

  public Intersector(int[] init) {
    _intersection = Arrays.copyOf(init, init.length);
    _count = init.length;
    _valid = new boolean[_count];
    Arrays.fill(_valid, true);
  }

  public Intersector add(int[] another) {
    int i = 0;
    int j = 0;
    while (i < _intersection.length) {
      if (!_valid[i]) {
        i++;
        continue;
      }
      if (j >= another.length || _intersection[i] < another[j]) {
        _valid[i] = false;
        _count--;
        i++;
      } else if (_intersection[i] == another[j]) {
        i++;
        j++;
      } else if (_intersection[i] > another[j]) {
        j++;
      }
    }
    return this;
  }

  public int[] getIntersection() {
    int[] ret = new int[_count];
    int j = 0;
    for (int i = 0; i < _intersection.length; ++i) {
      if (_valid[i]) {
        ret[j] = _intersection[i];
        j++;
      }
    }
    return ret;
  }
}
