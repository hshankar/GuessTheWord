package gtw.query;

import gtw.Vocabulary;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class VocabIndex {
  private Vocabulary _vocab;
  private List<String> _words;
  int[][] _invIdx;
  int[] _counts;

  public VocabIndex(Vocabulary vocab) {
    _vocab = vocab;
    createIndex();
  }

  void createIndex() {
    _words = _vocab.getWords().map(String::toLowerCase).collect(Collectors.toList());
    _invIdx = new int[26][];
    for (int i = 0; i < 26; ++i) {
      _invIdx[i] = new int[50];
    }
    _counts = new int[26];
    for (int i = 0; i < _words.size(); ++i) {
      String word = _words.get(i);
      boolean[] done = new boolean[26];
      for (char c : word.toCharArray()) {
        if (done[c - 'a']) {
          continue;
        }
        ensureSpace(c);
        _invIdx[c - 'a'][_counts[c - 'a']] = i;
        _counts[c - 'a']++;
        done[c - 'a'] = true;
      }
    }
  }

  public Stream<String> query(Query query) {
    Stream<String> ret;
    boolean[] reqChars = query.getCharsPresent();
    if (reqChars == null) {
      ret = _words.stream();
    } else {
      List<int[]> arrays = new ArrayList<>(reqChars.length);
      List<Integer> boundsList = new ArrayList<>();
      for (int i = 0; i < reqChars.length; ++i) {
        if (reqChars[i]) {
          arrays.add(_invIdx[i]);
          boundsList.add(_counts[i]);
        }
      }
      ret = IntersectionUtil.intersectSortedArrays(arrays, boundsList).mapToObj(_words::get);
    }
    if (query.getMinLength() > 0 || query.getMaxLength() < Integer.MAX_VALUE) {
      ret = ret.filter(s -> s.length() >= query.getMinLength() && s.length() <= query.getMaxLength());
    }
    return ret.limit(query.getNumResults());
  }

  private void ensureSpace(char c) {
    if (_invIdx[c - 'a'].length - _counts[c - 'a'] < 1) {
      int[] newArr = new int[_invIdx[c - 'a'].length * 2];
      for (int j = 0; j < _counts[c - 'a']; ++j) {
        newArr[j] = _invIdx[c - 'a'][j];
      }
      _invIdx[c - 'a'] = newArr;
    }
  }
}
