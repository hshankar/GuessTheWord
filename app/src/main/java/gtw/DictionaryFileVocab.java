package gtw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DictionaryFileVocab implements Vocabulary {
  Path _vocabFile;
  List<String> _cachedList;
  public DictionaryFileVocab(Path vocabFile) {
    _vocabFile = vocabFile;
  }

  @Override
  public Stream<String> getWords() {
    try {
      if (_cachedList == null) {
        _cachedList = Files.lines(_vocabFile).map(String::toLowerCase).filter(Utils::validChars).collect(Collectors.toList());
      }
      return _cachedList.stream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
