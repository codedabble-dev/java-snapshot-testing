package io.github.finoid.snapshots.serializers;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;

import java.util.function.BiFunction;

public interface SnapshotSerializer
    extends BiFunction<Object, SnapshotSerializerContext, Snapshot> {
    String getOutputFormat();
}
