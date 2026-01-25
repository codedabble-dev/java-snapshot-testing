package io.github.finoid.snapshots.config;

import io.github.finoid.snapshots.comparators.PlainTextEqualsComparator;
import io.github.finoid.snapshots.comparators.SnapshotComparator;
import io.github.finoid.snapshots.reporters.PlainTextSnapshotReporter;
import io.github.finoid.snapshots.reporters.SnapshotReporter;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;
import io.github.finoid.snapshots.serializers.ToStringSnapshotSerializer;

import java.util.Collections;
import java.util.List;

public class BaseSnapshotConfig implements SnapshotConfig {

    @Override
    public String getOutputDir() {
        return "src/test/java";
    }

    @Override
    public String getSnapshotDir() {
        return "__snapshots__";
    }

    @Override
    public SnapshotSerializer getSerializer() {
        return new ToStringSnapshotSerializer();
    }

    @Override
    public SnapshotComparator getComparator() {
        return new PlainTextEqualsComparator();
    }

    @Override
    public List<SnapshotReporter> getReporters() {
        return Collections.singletonList(new PlainTextSnapshotReporter());
    }

    @Override
    public boolean isCI() {
        return false;
    }
}
