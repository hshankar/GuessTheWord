package gtw;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class UserInputGuessStrategy implements GuessStrategy {
  List<String> _words;
  int _numChars;
  Scanner _userInput;

  @Override
  public void reset(Vocabulary vocab, int numChars) {
    _words = vocab.getWords().collect(Collectors.toList());
    _numChars = numChars;
    _userInput = new Scanner(System.in);
    System.out.println("Number of characters is: " + numChars);
  }

  @Override
  public String nextGuess() {
    System.out.println("Enter your guess: ");
    String guess = _userInput.next().toLowerCase();
    if(!valid(guess)) {
      System.out.println("Invalid guess: " + guess + ". Only a-z are allowed");
    }
    return guess;
  }

  private boolean valid(String guess) {
    for (char c : guess.toCharArray()) {
      if (c < 'a' || c > 'z') {
        return false;
      }
    }
    return true;
  }

  @Override
  public void updateResult(String guess, int matches) {
    System.out.println("Your guess: " + guess + " has " + matches + " matches");
  }

  @Override
  public void foundMatch(String guess, int attempts) {
    System.out.println("Your guessed it! The word was: " + guess + ". You took " + attempts + " attempts.");
  }

  @Override
  public void maxAttemptsReached(String word, int numAttempts) {
    System.out.println("Your lost. Reached max attempts: " + numAttempts + ". The word was: " + word);
  }
}
