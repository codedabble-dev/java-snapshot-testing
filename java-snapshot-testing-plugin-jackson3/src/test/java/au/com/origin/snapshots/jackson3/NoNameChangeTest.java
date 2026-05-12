package au.com.origin.snapshots.jackson3;

import static org.assertj.core.api.Assertions.assertThat;

import au.com.origin.snapshots.jackson3.serializers.v1.DeterministicJackson3SnapshotSerializer;
import au.com.origin.snapshots.jackson3.serializers.v1.Jackson3SnapshotSerializer;
import org.junit.jupiter.api.Test;

public class NoNameChangeTest {

  @Test
  public void serializersApiShouldNotChange() {
    assertThat(Jackson3SnapshotSerializer.class.getName())
        .isEqualTo("au.com.origin.snapshots.jackson3.serializers.v1.Jackson3SnapshotSerializer");
    assertThat(DeterministicJackson3SnapshotSerializer.class.getName())
        .isEqualTo(
            "au.com.origin.snapshots.jackson3.serializers.v1.DeterministicJackson3SnapshotSerializer");
  }
}
