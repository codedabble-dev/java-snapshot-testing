package io.github.finoid.snapshots.serializers;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotHeader;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringSnapshotSerializerTest {
    ToStringSnapshotSerializer serializer = new ToStringSnapshotSerializer();

    private SnapshotSerializerContext mockSnapshotGenerator =
        new SnapshotSerializerContext(
            "base64Test",
            null,
            new SnapshotHeader(),
            ToStringSnapshotSerializerTest.class,
            null // it's not used in these scenarios
        );

    @Test
    void shouldSnapshotAnyString() {
        Snapshot result = serializer.apply("John Doe", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\nJohn Doe\n]");
    }

    @Test
    void shouldSnapshotUnicode() {
        Snapshot result = serializer.apply("🤔", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\n🤔\n]");
    }

    @Test
    void shouldSnapshotAnyObject() {
        Snapshot result = serializer.apply(new Dummy(1, "John Doe"), mockSnapshotGenerator);
        assertThat(result.getBody())
            .isEqualTo("[\nToStringSerializerTest.Dummy(id=1, name=John Doe)\n]");
    }

    @Test
    void shouldSnapshotMultipleObjects() {
        Snapshot result = serializer.apply(new Dummy(1, "John Doe"), mockSnapshotGenerator);
        assertThat(result.getBody())
            .isEqualTo("[\nToStringSerializerTest.Dummy(id=1, name=John Doe)\n]");
    }

    @Test
    void shouldSupportBase64SerializerType() {
        assertThat(serializer.getOutputFormat()).isEqualTo("TEXT");
    }

    @Test
    void shouldReplaceThreeConsecutiveNewLines() {
        Snapshot result = serializer.apply("John\n\n\nDoe", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\nJohn\n.\n.\nDoe\n]");
    }

    @Test
    void shouldReplaceTwoConsecutiveNewLinesAtEnd() {
        Snapshot result = serializer.apply("John Doe\n\n", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\nJohn Doe\n.\n.\n]");
    }

    @Test
    void shouldReplaceTwoConsecutiveNewLinesAtBeginning() {
        Snapshot result = serializer.apply("\n\nJohn Doe", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\n.\n.\nJohn Doe\n]");
    }

    @Test
    void shouldReplaceIllegalNewlineSequencesEverywhere() {
        Snapshot result = serializer.apply("\n\nJohn\n\n\nDoe\n\n", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\n.\n.\nJohn\n.\n.\nDoe\n.\n.\n]");
    }

    @AllArgsConstructor
    @Data
    private static class Dummy {
        private int id;
        private String name;

        public String toString() {
            return "ToStringSerializerTest.Dummy(id=" + this.getId() + ", name=" + this.getName() + ")";
        }
    }
}
