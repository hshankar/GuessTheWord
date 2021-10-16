package gtw;

public class RoundResult {
  private final boolean _found;
  private final String _word;
  private final int _numAttempts;
  private final long _guessTime;

  private RoundResult(boolean found, String word, int numAttempts, long guessTime) {
    _found = found;
    _word = word;
    _numAttempts = numAttempts;
    _guessTime = guessTime;
  }

  public static RoundResult found(String word, int numAttempts, long guessTime) {
    return new RoundResult(true, word, numAttempts, guessTime);
  }

  public static RoundResult notFound(String word, int numAttempts, long guessTime) {
    return new RoundResult(false, word, numAttempts, guessTime);
  }

  public boolean isFound() {
    return _found;
  }

  public int getNumAttempts() {
    return _numAttempts;
  }

  public String getWord() {
    return _word;
  }

  public long getGuessTimeNs() {
    return _guessTime;
  }
}
