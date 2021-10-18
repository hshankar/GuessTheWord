package gtw.query;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;


// Intersect sorted int arrays
public class IntersectionUtil {

  private IntersectionUtil() {
  }

  public static IntStream intersectSortedArrays(List<int[]> arrays) {
    return intersectSortedArrays(arrays, arrays.stream().mapToInt(a -> a.length).boxed().collect(Collectors.toList()));
  }

  public static IntStream intersectSortedArrays(List<int[]> arrays, List<Integer> bounds) {
    int maxsize = min(bounds);
    if (maxsize == 0) {
      return IntStream.builder().build();
    }
    return StreamSupport.intStream(new SortedIntStream(arrays, bounds), false);
  }

  private static boolean matchesAll(int value, List<int[]> arrays, int[] indices) {
    for (int i = 0; i < indices.length; ++i) {
      if (arrays.get(i)[indices[i]] != value) {
        return false;
      }
    }
    return true;
  }

  private static int min(List<Integer> bounds) {
    int min = Integer.MAX_VALUE;
    for (int i : bounds) {
      if (i < min) {
        min = i;
      }
    }
    return min;
  }

  static class ValueAndArrayIdx {
    private int _value;
    private int _arrayIdx;

    ValueAndArrayIdx(int value, int arrayIdx) {
      _value = value;
      _arrayIdx = arrayIdx;
    }

    public int getValue() {
      return _value;
    }

    public int getArrayIdx() {
      return _arrayIdx;
    }
  }

  private static class SortedIntStream implements Spliterator.OfInt {
    private final List<int[]> _arrays;
    private final List<Integer> _bounds;
    private PriorityQueue<ValueAndArrayIdx> _pq;
    private int[] _indices;
    private boolean _ended = false;

    public SortedIntStream(List<int[]> arrays, List<Integer> bounds) {
      _arrays = arrays;
      _bounds = bounds;
      _pq = new PriorityQueue<>(Comparator.comparing(ValueAndArrayIdx::getValue));
      _indices = new int[_arrays.size()];
      for (int i = 0; i < _indices.length; ++i) {
        if (_indices[i] >= bounds.get(i)) {
          _ended = true;
          break;
        }
        _pq.add(new ValueAndArrayIdx(_arrays.get(i)[_indices[i]], i));
      }
    }

    @Override
    public OfInt trySplit() {
      return null;
    }

    @Override
    public long estimateSize() {
      return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
      return ORDERED | DISTINCT | SORTED | NONNULL | IMMUTABLE;
    }

    @Override
    public Comparator<? super Integer> getComparator() {
      return null;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
      if (_ended) {
        return false;
      }
      outer:
      while (true) {
        ValueAndArrayIdx lowest = _pq.poll();
        if (matchesAll(lowest.getValue(), _arrays, _indices)) {
          action.accept(lowest.getValue());
          for (int i = 1; i < _arrays.size(); ++i) {
            _pq.poll(); // remove these elems from PQ as they have matched
          }
          for (int i = 0; i < _indices.length; ++i) {
            _indices[i]++;
            if (_indices[i] >= _bounds.get(i)) {
              _ended = true;
              break outer;
            }
            _pq.offer(new ValueAndArrayIdx(_arrays.get(i)[_indices[i]], i));
          }
          return true;
        } else {
          int idx = lowest.getArrayIdx();
          _indices[idx]++;
          if (_indices[idx] >= _bounds.get(idx)) {
            _ended = true;
            break outer;
          }
          _pq.offer(new ValueAndArrayIdx(_arrays.get(idx)[_indices[idx]], idx));
        }
      }
      return false;
    }
  }
}
