package gtw;

public class GuessTheWord {
  private final int _maxAttempts;
  WordPicker _wordPicker;
  Vocabulary _vocab;
  GuessStrategy _guessStrategy;

  public GuessTheWord(WordPicker wordPicker, Vocabulary vocab, GuessStrategy guessStrategy, int maxAttempts) {
    _wordPicker = wordPicker;
    _vocab = vocab;
    _guessStrategy = guessStrategy;
    _maxAttempts = maxAttempts;
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
        _guessStrategy.foundMatch(guess, numAttempts);
        break;
      }
      if (numAttempts == _maxAttempts) {
        _guessStrategy.maxAttemptsReached(word, numAttempts);
        break;
      }
      int matches = getNumMatchedChars(getHistogram(word), guess);
      _guessStrategy.updateResult(guess, matches);
    }
    return found ? RoundResult.found(word, numAttempts, guessTime)
        : RoundResult.notFound(word, numAttempts, guessTime);
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

  int getNumMatchedChars(String word, String guess) {
    return getNumMatchedChars(getHistogram(word), guess);
  }
}
