package gtw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class DictionaryFileVocab implements Vocabulary {
  Path _vocabFile;
  public DictionaryFileVocab(Path vocabFile) {
    _vocabFile = vocabFile;
  }

  @Override
  public Stream<String> getWords() {
    try {
      return Files.lines(_vocabFile).map(String::toLowerCase).filter(Utils::validChars);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
