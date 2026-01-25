package io.github.finoid.snapshots.serializers;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;

public class UppercaseToStringSerializer implements SnapshotSerializer {
    @Override
    public Snapshot apply(Object object, SnapshotSerializerContext gen) {
        return gen.toSnapshot(object.toString().toUpperCase());
    }

    @Override
    public String getOutputFormat() {
        return SerializerType.TEXT.name();
    }
}
