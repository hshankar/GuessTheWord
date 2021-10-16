package gtw;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class RandomWordPickerWithLengthLimit implements WordPicker {
  List<String> _validWords;

  public RandomWordPickerWithLengthLimit(Vocabulary vocab, int minLength, int maxLength) {
    _validWords =
        vocab.getWords().filter(s -> s.length() >= minLength && s.length() <= maxLength).collect(Collectors.toList());
  }

  @Override
  public String pickWord() {
    return _validWords.get(ThreadLocalRandom.current().nextInt(_validWords.size()));
  }
}
