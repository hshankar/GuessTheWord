package gtw;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class InputListVocab implements Vocabulary {
  List<String> _list;

  public InputListVocab(List<String> vocab) {
    _list = new ArrayList<>(vocab);
  }

  @Override
  public Stream<String> getWords() {
    return _list.stream();
  }
}
