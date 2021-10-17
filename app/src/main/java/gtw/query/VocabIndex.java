package gtw.query;

import gtw.Vocabulary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

  public List<String> query(Query query) {
    List<String> ret = new ArrayList<>(query.getNumResults());
    boolean[] reqChars = query.getCharsPresent();
    Intersector intersector = null;
    for (int i = 0; i < reqChars.length; ++i) {
      if (reqChars[i]) {
        if (intersector == null) {
          intersector = new Intersector(Arrays.copyOf(_invIdx[i], _counts[i]));
        } else {
          intersector.add(Arrays.copyOf(_invIdx[i], _counts[i]));
        }
      }
    }
    if (intersector == null) {
      ret = new ArrayList<>(_words);
    } else {
      int[] indices = intersector.getIntersection();
      for (int i : indices) {
        ret.add(_words.get(i));
      }
    }
    ret = ret.size() < query.getNumResults() ? ret : ret.subList(0, query.getNumResults());
    if (query.getMinLength() > 0 || query.getMaxLength() < Integer.MAX_VALUE) {
      return ret.stream()
          .filter(s -> s.length() >= query.getMinLength() && s.length() <= query.getMaxLength())
          .collect(Collectors.toList());
    }
    return ret;
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
