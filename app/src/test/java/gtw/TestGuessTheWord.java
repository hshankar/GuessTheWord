package gtw;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TestGuessTheWord {
  WordPicker _wp;
  Vocabulary _vocab;
  GuessStrategy _gs;
  GuessTheWord _gtw;
  int _maxAttempts = 100;

  @Before
  public void setup() {
    _wp = mock(WordPicker.class);
    _vocab = mock(Vocabulary.class);
    _gs = mock(GuessStrategy.class);
    _gtw = new GuessTheWord(_wp, _vocab, _gs, _maxAttempts);
  }

  @Test
  public void firstMatch() {
    String word = "word";
    when(_wp.pickWord()).thenReturn(word);
    when(_gs.nextGuess()).thenReturn(word);
    RoundResult result = _gtw.playOneRound();
    assertThat(result.isFound()).isTrue();
    assertThat(result.getWord()).isEqualTo(word);
    assertThat(result.getNumAttempts()).isEqualTo(1);
    assertThat(result.getGuessTimeNs()).isGreaterThan(0);
  }

  @Test
  public void secondMatch() {
    String word = "word";
    when(_wp.pickWord()).thenReturn(word);
    when(_gs.nextGuess()).thenReturn(word + "foo").thenReturn(word);
    RoundResult result = _gtw.playOneRound();
    assertThat(result.isFound()).isTrue();
    assertThat(result.getWord()).isEqualTo(word);
    assertThat(result.getNumAttempts()).isEqualTo(2);
    assertThat(result.getGuessTimeNs()).isGreaterThan(0);
  }

  @Test
  public void noMatch() {
    String word = "word";
    when(_wp.pickWord()).thenReturn(word);
    when(_gs.nextGuess()).thenReturn(word + "foo");
    RoundResult result = _gtw.playOneRound();
    assertThat(result.isFound()).isFalse();
    assertThat(result.getWord()).isEqualTo(word);
    assertThat(result.getNumAttempts()).isEqualTo(_maxAttempts);
    assertThat(result.getGuessTimeNs()).isGreaterThan(0);
  }

  @Test
  public void matchedChars() {
    assertThat(_gtw.getNumMatchedChars("abc", "abc")).isEqualTo(3);
    assertThat(_gtw.getNumMatchedChars("abc", "abbc")).isEqualTo(3);
    assertThat(_gtw.getNumMatchedChars("", "abbc")).isEqualTo(0);
    assertThat(_gtw.getNumMatchedChars("abc", "")).isEqualTo(0);
    assertThat(_gtw.getNumMatchedChars("feed", "deer")).isEqualTo(3);
    assertThat(_gtw.getNumMatchedChars("feed", "dear")).isEqualTo(2);
    assertThat(_gtw.getNumMatchedChars("fEEd", "reEd")).isEqualTo(3);
  }
}
