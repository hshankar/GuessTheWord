package gtw.query;

import java.util.List;
import org.junit.Test;

import static gtw.query.Intersector.*;
import static org.assertj.core.api.Assertions.*;


public class TestIntersector {
  @Test
  public void nWayIntersect() {
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5}))).containsExactly(3, 5);
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5, 7}))).containsExactly(3, 5);
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{5, 7}))).containsExactly(5);
    assertThat(getIntersectionArray(List.of(new int[]{3, 4, 7}, new int[]{5, 7}))).containsExactly(7);
    assertThat(getIntersectionArray(List.of(new int[]{}, new int[]{5, 7}))).containsExactly();
    assertThat(
        getIntersectionArray(List.of(new int[]{1, 2, 3}, new int[]{2, 4, 6}, new int[]{2, 4, 6}))).containsExactly(2);
    assertThat(
        getIntersectionArray(List.of(new int[]{1, 2, 3}, new int[]{2, 4}, new int[]{2, 6, 8, 10, 11}))).containsExactly(
        2);
    assertThat(
        getIntersectionArray(List.of(new int[]{1}, new int[]{2, 4}, new int[]{2, 6, 8, 10, 11}))).containsExactly();
  }

  @Test
  public void nWayIntersectWithBounds() {
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5}), new int[]{1, 1})).containsExactly(3);
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5}), new int[]{2, 0})).containsExactly();
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5}), new int[]{0, 2})).containsExactly();
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5}), new int[]{1, 2})).containsExactly(3);
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5}), new int[]{2, 1})).containsExactly(3);
    assertThat(getIntersectionArray(List.of(new int[]{3, 5}, new int[]{3, 5}), new int[]{2, 2})).containsExactly(3, 5);

    assertThat(
        getIntersectionArray(List.of(new int[]{1, 2, 3, 4, 5, 6}, new int[]{2, 4, 6}, new int[]{2, 6, 8, 10, 11}),
            new int[]{2, 2, 3})).containsExactly(2);
    assertThat(
        getIntersectionArray(List.of(new int[]{1, 2, 3, 4, 5, 6}, new int[]{2, 4, 6}, new int[]{2, 6, 8, 10, 11}),
            new int[]{2, 2, 0})).containsExactly();
  }

  private int[] getIntersectionArray(List<int[]> arrays) {
    return intersectSortedArrays(arrays).toArray();
  }

  private int[] getIntersectionArray(List<int[]> arrays, int[] bounds) {
    return intersectSortedArrays(arrays, bounds).toArray();
  }
}
