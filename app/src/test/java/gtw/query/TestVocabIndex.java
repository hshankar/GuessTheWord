package gtw.query;

import gtw.InputListVocab;
import gtw.Vocabulary;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class TestVocabIndex {
  Vocabulary _vocab = new InputListVocab(List.of("apple", "banana", "onion", "orange", "kiwi"));
  VocabIndex _vocabIndex;

  @Before
  public void setup() {
    _vocabIndex = new VocabIndex(_vocab);
  }

  @Test
  public void emptyQuery() {
    Query query = Query.builder().build();
    assertThat(_vocabIndex.query(query)).isEqualTo(List.of("apple", "banana", "onion", "orange", "kiwi"));
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
    Query query = Query.builder().withRequiredChar('x').build();
    assertThat(runQuery(query)).containsExactly();
  }

  @Test
  public void oneMatch() {
    Query query = Query.builder().withRequiredChar('w').build();
    assertThat(runQuery(query)).containsExactly("kiwi");
  }

  @Test
  public void multipleMatches() {
    Query query = Query.builder().withRequiredChar('a').build();
    assertThat(runQuery(query)).containsExactly("apple", "banana", "orange");
  }

  @Test
  public void multipleLetters() {
    Query query = Query.builder().withRequiredChar('a').withRequiredChar('e').build();
    assertThat(runQuery(query)).containsExactly("apple", "orange");
  }

  @Test
  public void minLength() {
    Query query = Query.builder().withRequiredChar('a').withMinLength(6).build();
    assertThat(runQuery(query)).containsExactly("banana", "orange");
  }

  @Test
  public void maxLength() {
    Query query = Query.builder().withRequiredChar('a').withMaxLength(5).build();
    assertThat(runQuery(query)).containsExactly("apple");
  }

  @Test
  public void numResults() {
    Query query = Query.builder().withRequiredChar('a').withNumResults(2).build();
    assertThat(runQuery(query)).containsExactly("apple", "banana");
  }

  private String[] runQuery(Query query) {
    return _vocabIndex.query(query).toArray(n -> new String[n]);
  }
}
