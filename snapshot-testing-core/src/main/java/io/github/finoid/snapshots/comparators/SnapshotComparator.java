package io.github.finoid.snapshots.comparators;

import io.github.finoid.snapshots.Snapshot;

public interface SnapshotComparator {
    boolean matches(Snapshot previous, Snapshot current);
}
