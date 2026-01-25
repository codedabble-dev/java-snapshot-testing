package io.github.finoid.snapshots.reporters;

import io.github.finoid.snapshots.Snapshot;

public interface SnapshotReporter {

    boolean supportsFormat(String outputFormat);

    void report(Snapshot previous, Snapshot current);
}
