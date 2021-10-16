package gtw;

public interface GuessStrategy {
  void reset(Vocabulary vocab, int numWords);

  String nextGuess();

  void updateResult(String guess, int matches);
}
