package gtw.query;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class TestIntersector {
  @Test
  public void twoArrays() {
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{3, 5}).getIntersection()).isEqualTo(new int[]{3, 5});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{3}).getIntersection()).isEqualTo(new int[]{3});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{5}).getIntersection()).isEqualTo(new int[]{5});
    assertThat(new Intersector(new int[]{}).add(new int[]{3}).getIntersection()).isEqualTo(new int[]{});
    assertThat(new Intersector(new int[]{}).add(new int[]{}).getIntersection()).isEqualTo(new int[]{});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{3, 7}).getIntersection()).isEqualTo(new int[]{3});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{5, 7}).getIntersection()).isEqualTo(new int[]{5});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{6, 7}).getIntersection()).isEqualTo(new int[]{});
  }

  @Test
  public void threeArrays() {
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{3, 5}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{3, 5});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{3}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{3});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{5}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{5});
    assertThat(new Intersector(new int[]{}).add(new int[]{3}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{});
    assertThat(new Intersector(new int[]{}).add(new int[]{}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{3, 7}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{3});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{5, 7}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{5});
    assertThat(new Intersector(new int[]{3, 5}).add(new int[]{6, 7}).add(new int[]{3, 5}).getIntersection()).isEqualTo(
        new int[]{});
    assertThat(new Intersector(new int[]{3, 5, 7}).add(new int[]{5}).add(new int[]{7}).getIntersection()).isEqualTo(
        new int[]{});
    assertThat(new Intersector(new int[]{3, 5, 7}).add(new int[]{5, 6, 7}).add(new int[]{4, 5, 7}).getIntersection()).isEqualTo(
        new int[]{5, 7});
  }
}
