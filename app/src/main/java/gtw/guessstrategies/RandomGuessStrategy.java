package gtw.guessstrategies;

import gtw.GuessStrategy;
import gtw.Vocabulary;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class RandomGuessStrategy implements GuessStrategy {
  List<String> _words;
  int _numWords;
  @Override
  public void reset(Vocabulary vocab, int numWords) {
    _words = vocab.getWords().collect(Collectors.toList());
    _numWords = numWords;
  }

  @Override
  public String nextGuess() {
    return _words.get(ThreadLocalRandom.current().nextInt(_words.size()));
  }
}
