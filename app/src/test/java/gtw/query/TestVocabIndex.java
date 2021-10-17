package gtw.query;

import gtw.InputListVocab;
import gtw.Vocabulary;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class TestVocabIndex {
  Vocabulary _vocab = new InputListVocab(List.of("apple", "banana", "onion", "orange", "kiwi"));

  @Test
  public void emptyQuery() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of("apple", "banana", "onion", "orange", "kiwi"));
  }

  @Test
  public void emptyVocab() {
    VocabIndex vocabIndex = new VocabIndex(new InputListVocab(List.of()));
    Query query = Query.builder().build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of());
    query = Query.builder().withRequiredChar('a').build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of());
  }

  @Test
  public void noMatch() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().withRequiredChar('x').build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of());
  }

  @Test
  public void oneMatch() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().withRequiredChar('w').build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of("kiwi"));
  }

  @Test
  public void multipleMatches() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().withRequiredChar('a').build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of("apple", "banana", "orange"));
  }

  @Test
  public void multipleLetters() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().withRequiredChar('a').withRequiredChar('e').build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of("apple", "orange"));
  }

  @Test
  public void minLength() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().withRequiredChar('a').withMinLength(6).build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of("banana", "orange"));
  }

  @Test
  public void maxLength() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().withRequiredChar('a').withMaxLength(5).build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of("apple"));
  }

  @Test
  public void numResults() {
    VocabIndex vocabIndex = new VocabIndex(_vocab);
    Query query = Query.builder().withRequiredChar('a').withNumResults(2).build();
    assertThat(vocabIndex.query(query)).isEqualTo(List.of("apple", "banana"));
  }
}
