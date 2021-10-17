package gtw;

public interface GuessStrategy {
  void reset(Vocabulary vocab, int numWords);

  String nextGuess();

  default void updateResult(String guess, int matches) {};

  default void maxAttemptsReached(String word, int attempts) {}

  default void foundMatch(String guess, int attempts) {};

  default void invalidGuess(String guess, int numAttempts) {};
}
