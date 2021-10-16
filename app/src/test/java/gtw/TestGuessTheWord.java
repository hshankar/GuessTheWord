package gtw;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TestGuessTheWord {
  @Test
  public void firstMatch() {
    WordPicker wp = mock(WordPicker.class);
    Vocabulary vocab = mock(Vocabulary.class);
    GuessStrategy gs = mock(GuessStrategy.class);

    String word = "word";
    when(wp.pickWord()).thenReturn(word);
    when(gs.nextGuess()).thenReturn(word);
    GuessTheWord gtw = new GuessTheWord(wp, vocab, gs);
    RoundResult result = gtw.playOneRound();
    assertThat(result.isFound()).isTrue();
    assertThat(result.getLength()).isEqualTo(word.length());
    assertThat(result.getNumAttempts()).isEqualTo(1);
    assertThat(result.getGuessTime()).isGreaterThan(0);
  }

  @Test
  public void secondMatch() {
    WordPicker wp = mock(WordPicker.class);
    Vocabulary vocab = mock(Vocabulary.class);
    GuessStrategy gs = mock(GuessStrategy.class);

    String word = "word";
    when(wp.pickWord()).thenReturn(word);
    when(gs.nextGuess()).thenReturn(word + "foo").thenReturn(word);
    GuessTheWord gtw = new GuessTheWord(wp, vocab, gs);
    RoundResult result = gtw.playOneRound();
    assertThat(result.isFound()).isTrue();
    assertThat(result.getLength()).isEqualTo(word.length());
    assertThat(result.getNumAttempts()).isEqualTo(2);
    assertThat(result.getGuessTime()).isGreaterThan(0);
  }

  @Test
  public void noMatch() {
    WordPicker wp = mock(WordPicker.class);
    Vocabulary vocab = mock(Vocabulary.class);
    GuessStrategy gs = mock(GuessStrategy.class);

    String word = "word";
    when(wp.pickWord()).thenReturn(word);
    when(gs.nextGuess()).thenReturn(word + "foo");
    GuessTheWord gtw = new GuessTheWord(wp, vocab, gs);
    RoundResult result = gtw.playOneRound();
    assertThat(result.isFound()).isFalse();
    assertThat(result.getLength()).isEqualTo(word.length());
    assertThat(result.getNumAttempts()).isEqualTo(GuessTheWord.MAX_ATTEMPTS);
    assertThat(result.getGuessTime()).isGreaterThan(0);
  }
}
