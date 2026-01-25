package io.github.finoid.snapshots.comparators.v1;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.comparators.SnapshotComparator;

public class PlainTextEqualsComparator implements SnapshotComparator {

    @Override
    public boolean matches(Snapshot previous, Snapshot current) {
        return previous.getBody().equals(current.getBody());
    }
}
