package gtw.guessstrategies;

import gtw.GuessStrategy;
import gtw.Vocabulary;
import java.util.Arrays;
import java.util.Iterator;


public class EliminationStrategy implements GuessStrategy {
  int _numChars;
  boolean[] _eliminated;
  Iterator<String> _words;
  Iterator<String> _eliminationWords;
  int _numElim = 0;

  @Override
  public void reset(Vocabulary vocab, int numChars) {
    _words = vocab.getWords().iterator();
    _eliminationWords = generateEliminationWords(vocab);
    _numChars = numChars;
    _eliminated = new boolean[26];
    Arrays.fill(_eliminated, false);
  }

  private Iterator<String> generateEliminationWords(Vocabulary vocab) {
    return new Iterator<>() {
      boolean[] covered = new boolean[26];
      String next = null;
      Iterator<String> words = vocab.getWords().map(String::toLowerCase).iterator();

      @Override
      public boolean hasNext() {
        if (next != null) {
          return true;
        }
        if (next == null && !_words.hasNext()) {
          return false;
        }
        while (words.hasNext()) {
          String word = words.next();
          if (word.length() >= 3 && word.length() <= 5 && allDifferenAndNotCovered(word, covered)) {
            next = word;
            return true;
          }
        }
        return false;
      }

      private boolean allDifferenAndNotCovered(String word, boolean[] covered) {
        boolean[] seen = new boolean[26];
        for (char c : word.toCharArray()) {
          if (seen[c - 'a'] || covered[c - 'a']) {
            return false;
          }
          seen[c - 'a'] = true;
        }
        return true;
      }

      @Override
      public String next() {
        for (char c : next.toCharArray()) {
          covered[c - 'a'] = true;
        }
        String ret = next;
        next = null;
        return ret;
      }
    };
  }

  @Override
  public String nextGuess() {
    if (_eliminationWords.hasNext()) {
      return _eliminationWords.next();
    }
    while (_words.hasNext()) {
      String word = _words.next().toLowerCase();
      if (!containsEliminatedChar(word) && word.length() == _numChars) {
        return word;
      }
    }
    return "";
  }

  private boolean containsEliminatedChar(String word) {
    for (char c : word.toCharArray()) {
      if (isEliminated(c)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void updateResult(String guess, int matches) {
    if (matches == 0) {
      for (char c : guess.toCharArray()) {
        setEliminated(c);
      }
    }
  }

  private void setEliminated(char c) {
    if (!_eliminated[c - 'a']) {
      _eliminated[c - 'a'] = true;
      _numElim++;
    }
  }

  private boolean isEliminated(char c) {
    return _eliminated[c - 'a'];
  }
}
