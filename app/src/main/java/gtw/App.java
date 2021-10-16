package gtw;

import java.nio.file.Path;
import java.nio.file.Paths;


public class App {
  static Path VOCAB_FILE = Paths.get("/usr/share/dict/words");

  public static void main(String[] args) {
    DictionaryFileVocab vocab = new DictionaryFileVocab(VOCAB_FILE);
    WordPicker wp = new RandomWordPickerWithLengthLimit(vocab, 4, 8);
    GuessStrategy guessStrategy = new RandomGuessStrategy();
    GuessTheWord guessTheWord = new GuessTheWord(wp, vocab, guessStrategy);
    guessTheWord.playOneRound();
  }
}
