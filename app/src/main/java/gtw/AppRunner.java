package gtw;

import gtw.guessstrategies.UserInputGuessStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;


public class AppRunner {
  static Path VOCAB_FILE = Paths.get("/usr/share/dict/words");
  static int MAX_ATTEMPTS = 100;

  public static void main(String[] args) {
    // GuessStrategy guessStrategy = new RandomGuessStrategy();
    GuessStrategy guessStrategy = new UserInputGuessStrategy();
    // GuessStrategy guessStrategy = new EliminationStrategy();

    DictionaryFileVocab vocab = new DictionaryFileVocab(VOCAB_FILE);
    WordPicker wp = new RandomWordPickerWithLengthLimit(vocab, 4, 8);

    GuessTheWord guessTheWord = new GuessTheWord(wp, vocab, guessStrategy, MAX_ATTEMPTS);
    int numRounds = 100;
    double overallScore = 0.0;
    int guessed = 0;
    for (int i=0;i<numRounds;++i) {
      RoundResult result = guessTheWord.playOneRound();
      if (result.isFound()) {
        guessed++;
        double score = result.getWord().length() * result.getWord().length() * 1.0 / (result.getNumAttempts() + result.getGuessTimeNs() * 1.0/ 10_000_000_000L);
        overallScore = (overallScore * i + score) / (i+1);
        System.out.println(
            "Guessed " + result.getWord() + " in " + result.getNumAttempts() + " attempts. Score = " + score
                + " , Total score = " + overallScore);
      } else {
        System.out.println("Did not guess: " + result.getWord() + " in " + result.getNumAttempts() + " attempts.");
      }
    }
    System.out.println("Guessed " + guessed + " out of "+ numRounds + ". Total score " + overallScore);
  }
}
