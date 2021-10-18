package gtw;

import java.util.Set;
import java.util.stream.Collectors;


public class GuessTheWord {
  private final int _maxAttempts;
  WordPicker _wordPicker;
  Vocabulary _vocab;
  Set<String> _validGuesses;
  GuessStrategy _guessStrategy;

  public GuessTheWord(WordPicker wordPicker, Vocabulary vocab, GuessStrategy guessStrategy, int maxAttempts) {
    _wordPicker = wordPicker;
    _vocab = vocab;
    _validGuesses = vocab.getWords().map(String::toLowerCase).filter(s -> s.length() >= 3).collect(Collectors.toSet());
    _guessStrategy = guessStrategy;
    _maxAttempts = maxAttempts;
  }

  public RoundResult playOneRound() {
    String word = _wordPicker.pickWord().toLowerCase();
    System.out.println("Word is " + word);
    _guessStrategy.reset(_vocab, word.length());
    int numAttempts = 0;
    boolean found = false;
    long guessTime = 0;
    while (true) {
      if (numAttempts == _maxAttempts) {
        _guessStrategy.maxAttemptsReached(word, numAttempts);
        break;
      }
      long start = System.nanoTime();
      String guess = _guessStrategy.nextGuess().toLowerCase();
      guessTime += (System.nanoTime() - start);
      numAttempts++;
      if (!valid(guess)) {
        _guessStrategy.invalidGuess(guess, numAttempts);
        continue;
      }
      if (guess.equals(word)) {
        found = true;
        _guessStrategy.foundMatch(guess, numAttempts);
        break;
      }
      int matches = getNumMatchedChars(Utils.getHistogram(word), guess);
      _guessStrategy.updateResult(guess, matches);
    }
    return found ? RoundResult.found(word, numAttempts, guessTime)
        : RoundResult.notFound(word, numAttempts, guessTime);
  }

  private boolean valid(String guess) {
    return _validGuesses.contains(guess.toLowerCase());
  }

  private int getNumMatchedChars(int[] wordHist, String guess) {
    int[] guessHist = Utils.getHistogram(guess);
    int matches = 0;
    for (int i = 0; i < wordHist.length; i++) {
      matches += Math.min(wordHist[i], guessHist[i]);
    }
    return matches;
  }

  int getNumMatchedChars(String word, String guess) {
    return getNumMatchedChars(Utils.getHistogram(word), guess);
  }
}
