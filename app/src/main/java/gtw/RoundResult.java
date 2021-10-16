package gtw;

public class RoundResult {
  private final boolean _found;
  private final int _length;
  private final int _numAttempts;
  private final long _guessTime;

  private RoundResult(boolean found, int length, int numAttempts, long guessTime) {
    _found = found;
    _length = length;
    _numAttempts = numAttempts;
    _guessTime = guessTime;
  }

  public static RoundResult found(int length, int numAttempts, long guessTime) {
    return new RoundResult(true, length, numAttempts, guessTime);
  }

  public static RoundResult notFound(int length, int numAttempts, long guessTime) {
    return new RoundResult(false, length, numAttempts, guessTime);
  }

  public boolean isFound() {
    return _found;
  }

  public int getNumAttempts() {
    return _numAttempts;
  }

  public int getLength() {
    return _length;
  }

  public long getGuessTime() {
    return _guessTime;
  }
}
