package gtw.guessstrategies;

import gtw.GuessStrategy;
import gtw.Utils;
import gtw.Vocabulary;
import gtw.query.Query;
import gtw.query.VocabIndex;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class WeighingStrategy implements GuessStrategy {
  private static final double CONFIDENCE_THRESHOLD = 0.25;
  int _numChars;
  VocabIndex _vocabIndex;
  List<String> _randomWords;
  double[] _weights = new double[26];
  int[] _timesOccurred = new int[26];
  Set<Character> _confidentChars;
  Set<String> _tried;

  @Override
  public void reset(Vocabulary vocab, int numChars) {
    _vocabIndex = new VocabIndex(vocab);
    _randomWords = vocab.getWords()
        .map(String::toLowerCase)
        .filter(s -> s.length() >= 3 && s.length() <= 5)
        .collect(Collectors.toList());
    _numChars = numChars;
    Arrays.fill(_weights, 0);
    Arrays.fill(_timesOccurred, 0);
    _confidentChars = new HashSet<>();
    _tried = new HashSet<>();
  }

  @Override
  public String nextGuess() {
    String pick = null;
    Query query;
    do {
      if (_confidentChars.size() >= 2) {
        log("using confident chars: " + _confidentChars);
        query = Query.builder().withRequiredChars(_confidentChars).withMinLength(3).withMaxLength(_numChars).build();
        pick = _vocabIndex.query(query).findFirst().orElse(null);
      }
      if (pick == null) {
        pick = pickRandom(_randomWords);
      }
    } while (_tried.contains(pick));
    _tried.add(pick);
    log("trying: " + pick);
    return pick;
  }

  private String pickRandom(List<String> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size()));
  }

  @Override
  public void updateResult(String guess, int matches) {
    log("Got " + matches + " matches for " + guess);
    int[] hist = Utils.getHistogram(guess);
    for (char c : guess.toCharArray()) {
      int oldTimesOccurred = _timesOccurred[c - 'a'];
      _timesOccurred[c - 'a'] += hist[c - 'a'];
      _weights[c - 'a'] =
          (_weights[c - 'a'] * oldTimesOccurred + matches * 1.0 * hist[c - 'a'] / guess.length()) / _timesOccurred[c
              - 'a'];
      log("new weight for " + c + " = " + _weights[c - 'a']);
      if (_weights[c - 'a'] > CONFIDENCE_THRESHOLD) {
        _confidentChars.add(c);
      } else {
        _confidentChars.remove(c);
      }
    }
  }

  public static void log(String s) {
    System.out.println(s);
  }
}
