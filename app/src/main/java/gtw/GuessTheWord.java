package gtw;

public class GuessTheWord {
  public static int MAX_ATTEMPTS = 100;
  WordPicker _wordPicker;
  Vocabulary _vocab;
  GuessStrategy _guessStrategy;

  public GuessTheWord(WordPicker wordPicker, Vocabulary vocab, GuessStrategy guessStrategy) {
    _wordPicker = wordPicker;
    _vocab = vocab;
    _guessStrategy = guessStrategy;
  }

  public RoundResult playOneRound() {
    String word = _wordPicker.pickWord().toLowerCase();
    _guessStrategy.reset(_vocab, word.length());
    int numAttempts = 0;
    boolean found = false;
    long guessTime = 0;
    while (true) {
      long start = System.nanoTime();
      String guess = _guessStrategy.nextGuess().toLowerCase();
      guessTime += (System.nanoTime() - start);
      numAttempts++;
      if (guess.equals(word)) {
        found = true;
        break;
      }
      if (numAttempts == MAX_ATTEMPTS) {
        break;
      }
      int matches = getNumMatchedChars(getHistogram(word), guess);
      _guessStrategy.updateResult(guess, matches);
    }
    return found ? RoundResult.found(word.length(), numAttempts, guessTime)
        : RoundResult.notFound(word.length(), numAttempts, guessTime);
  }

  private int[] getHistogram(String word) {
    int[] hist = new int[26];
    for (char c : word.toLowerCase().toCharArray()) {
      hist[c - 'a']++;
    }
    return hist;
  }

  private int getNumMatchedChars(int[] wordHist, String guess) {
    int[] guessHist = getHistogram(guess);
    int matches = 0;
    for (int i = 0; i < wordHist.length; i++) {
      matches += Math.min(wordHist[i], guessHist[i]);
    }
    return matches;
  }
}
