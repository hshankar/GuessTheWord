package gtw;

public class Utils {
  public static int[] getHistogram(String word) {
    int[] hist = new int[26];
    for (char c : word.toLowerCase().toCharArray()) {
      hist[c - 'a']++;
    }
    return hist;
  }

  public static boolean validChars(String word) {
    for (char c : word.toLowerCase().toCharArray()) {
      if (c < 'a' || c > 'z') {
        return false;
      }
    }
    return true;
  }
}
