package gtw.query;

class Query {
  private boolean[] _charsPresent;
  // boolean[] _charsAbsent;
  private int _minLength;
  private int _maxLength;
  private int _numResults;

  private Query(boolean[] charsPresent, int minLength, int maxLength, int numResults) {
    _charsPresent = charsPresent;
    // _charsAbsent = new boolean[26];
    _minLength = minLength;
    _maxLength = maxLength;
    _numResults = numResults;
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

  static class Builder {
    private boolean[] _charsPresent;
    private int _minLength;
    private int _maxLength;
    private int _numResults;

    private Builder() {
      _charsPresent = new boolean[26];
      _minLength = 0;
      _maxLength = Integer.MAX_VALUE;
      _numResults = 10;
    }

    public Builder withRequiredChar(char c) {
      _charsPresent[c - 'a'] = true;
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
      return new Query(_charsPresent, _minLength, _maxLength, _numResults);
    }
  }
}
