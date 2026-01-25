package io.github.finoid.snapshots.comparators;

import io.github.finoid.snapshots.Snapshot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PlainTextEqualsComparatorTest {

    private static final PlainTextEqualsComparator COMPARATOR = new PlainTextEqualsComparator();

    @Test
    void successfulComparison() {
        Snapshot snap1 = Snapshot.builder().name("snap1").scenario("A").body("foo").build();
        Snapshot snap2 = Snapshot.builder().name("snap1").scenario("A").body("foo").build();
        Assertions.assertThat(COMPARATOR.matches(snap1, snap2)).isTrue();
    }

    @Test
    void failingComparison() {
        Snapshot snap1 = Snapshot.builder().name("snap1").scenario("A").body("foo").build();
        Snapshot snap2 = Snapshot.builder().name("snap1").scenario("A").body("bar").build();
        Assertions.assertThat(COMPARATOR.matches(snap1, snap2)).isFalse();
    }
}
