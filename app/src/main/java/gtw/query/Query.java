package gtw.query;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public class Query {
  private final Set<String> _ignoredWords;
  private boolean[] _charsPresent;
  // boolean[] _charsAbsent;
  private int _minLength;
  private int _maxLength;
  private int _numResults;

  private Query(boolean[] charsPresent, int minLength, int maxLength, int numResults, Set<String> ignoredWords) {
    _charsPresent = charsPresent;
    // _charsAbsent = new boolean[26];
    _minLength = minLength;
    _maxLength = maxLength;
    _numResults = numResults;
    _ignoredWords = ignoredWords;
  }

  public static Builder builder() {
    return new Builder();
  }

  public boolean[] getCharsPresent() {
    return _charsPresent;
  }

  public int getMinLength() {
    return _minLength;
  }

  public int getMaxLength() {
    return _maxLength;
  }

  public int getNumResults() {
    return _numResults;
  }

  public Set<String> getIgnoredWords() {
    return _ignoredWords;
  }

  public static class Builder {
    private boolean[] _charsPresent;
    private int _minLength;
    private int _maxLength;
    private int _numResults;
    private Set<String> _ignoredWords;

    private Builder() {
      _charsPresent = null;
      _minLength = 0;
      _maxLength = Integer.MAX_VALUE;
      _numResults = 100;
    }

    public Builder withRequiredChar(char c) {
      initCharsPresent();
      _charsPresent[c - 'a'] = true;
      return this;
    }

    private void initCharsPresent() {
      _charsPresent = new boolean[26];
    }

    public Builder withRequiredChars(char[] cs) {
      initCharsPresent();
      for (char c : cs) {
        _charsPresent[c - 'a'] = true;
      }
      return this;
    }

    public Builder withRequiredChars(Collection<Character> cs) {
      initCharsPresent();
      for (char c : cs) {
        _charsPresent[c - 'a'] = true;
      }
      return this;
    }

    public Builder withMinLength(int n) {
      _minLength = n;
      return this;
    }

    public Builder withMaxLength(int n) {
      _maxLength = n;
      return this;
    }

    public Builder withNumResults(int n) {
      _numResults = n;
      return this;
    }

    public Query build() {
      return new Query(_charsPresent, _minLength, _maxLength, _numResults, _ignoredWords);
    }
  }
}
