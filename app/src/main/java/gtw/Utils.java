package gtw;

public class Utils {
  public static int[] getHistogram(String word) {
    int[] hist = new int[26];
    for (char c : word.toLowerCase().toCharArray()) {
      hist[c - 'a']++;
    }
    return hist;
  }
}
