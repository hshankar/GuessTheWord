package gtw.guessstrategies;

import gtw.GuessStrategy;
import gtw.Utils;
import gtw.Vocabulary;
import gtw.query.Query;
import gtw.query.VocabIndex;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class WeighingStrategy implements GuessStrategy {
  private static final double CONFIDENCE_THRESHOLD = 0.5;
  private static final double MAX_CONFIDENCE_CHARS_RATIO = 0.6;
  int _numChars;
  VocabIndex _vocabIndex;
  List<String> _randomWords;
  double[] _weights = new double[26];
  int[] _timesOccurred = new int[26];
  Map<Character, Double> _likelyChars;
  Set<Character> _confidentlyAbsent;
  Set<Character> _confidentlyPresent;
  Set<String> _tried;
  Map<Set<Character>, Iterator<String>> _queryCache;
  int _maxConfidenceChars;

  @Override
  public void reset(Vocabulary vocab, int numChars) {
    _vocabIndex = new VocabIndex(vocab);
    _randomWords = vocab.getWords()
        .map(String::toLowerCase)
        .filter(s -> s.length() >= 3 && s.length() <= 4)
        .collect(Collectors.toList());
    _numChars = numChars;
    Arrays.fill(_weights, 0);
    Arrays.fill(_timesOccurred, 0);
    _likelyChars = new HashMap<>();
    _tried = new HashSet<>();
    _queryCache = new HashMap<>();
    _confidentlyAbsent = new HashSet<>();
    _confidentlyPresent = new HashSet<>();
    _maxConfidenceChars = (int) (_numChars * MAX_CONFIDENCE_CHARS_RATIO);
  }

  @Override
  public String nextGuess() {
    String pick = null;
    Iterator<String> queryResults = null;
    if (_likelyChars.size() >= 2) {
      log("using confident chars: " + _likelyChars);
      int minLength = 3;
      if (_confidentlyPresent.size() >= _maxConfidenceChars) {
        minLength = _numChars;
      }
      Query query =
          Query.builder().withRequiredChars(_likelyChars.keySet()).withMinLength(minLength).withMaxLength(_numChars).build();
      if (_queryCache.get(_likelyChars.keySet()) != null) {
        log("using cached query: " + _likelyChars);
        queryResults = _queryCache.get(_likelyChars.keySet());
      } else {
        queryResults = _vocabIndex.query(query).iterator();
        _queryCache.put(_likelyChars.keySet(), queryResults);
      }
    }
    do {
      if (queryResults != null && queryResults.hasNext()) {
        pick = queryResults.next();
      } else {
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
    int normLength = guess.length(); // length excluding known chars not present and chars present for sure
    int normMatches = matches; // matches excluding chars we know are present
    for (char c : guess.toCharArray()) {
      if (_confidentlyAbsent.contains(c)) {
        log("Excluding " + c + " as it is absent for sure");
        normLength--;
      }
      if (_confidentlyPresent.contains(c)) {
        log("Excluding " + c + " as it is Present for sure");
        normMatches--;
        normLength--;
      }
    }
    for (char c : guess.toCharArray()) {
      if (_confidentlyPresent.contains(c) || _confidentlyAbsent.contains(c)) {
        continue;
      }
      if (normMatches == 0) {
        log(c + " must be absent for sure");
        _weights[c - 'a'] = -10000;
        _confidentlyAbsent.add(c);
      } else if (normMatches == normLength) {
        log(c + " must be Present for sure");
        _weights[c - 'a'] = 10000;
        _confidentlyPresent.add(c);
      }
    }
//    if (matches == 0) {
//      for (char c : guess.toCharArray()) {
//        _weights[c - 'a'] = -10000;
//      }
//    } else if (matches == guess.length()) {
//      for (char c : guess.toCharArray()) {
//        _weights[c - 'a'] = 10000;
//      }
//    }

    for (char c : guess.toCharArray()) {
      int oldTimesOccurred = _timesOccurred[c - 'a'];
      _timesOccurred[c - 'a'] += hist[c - 'a'];
      if (!_confidentlyPresent.contains(c) && !_confidentlyAbsent.contains(c)) {
        _weights[c - 'a'] =
            (_weights[c - 'a'] * oldTimesOccurred + matches * 1.0 * hist[c - 'a'] / guess.length()) / _timesOccurred[c
                - 'a'];
      }
      log("new weight for " + c + " = " + _weights[c - 'a']);
      if (_weights[c - 'a'] > CONFIDENCE_THRESHOLD) {
        _likelyChars.put(c, _weights[c - 'a']);
        trimConfidenceChars();
      } else {
        _likelyChars.remove(c);
      }
    }
  }

  private void trimConfidenceChars() {
    if (_likelyChars.size() > _maxConfidenceChars) {
      _likelyChars = _likelyChars.entrySet()
          .stream()
          .sorted(Comparator.<Map.Entry<Character, Double>>comparingDouble(e -> e.getValue()).reversed())
          .limit(_maxConfidenceChars + _confidentlyPresent.size())
          .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }
  }

  public static void log(String s) {
    //System.out.println(s);
  }
}
