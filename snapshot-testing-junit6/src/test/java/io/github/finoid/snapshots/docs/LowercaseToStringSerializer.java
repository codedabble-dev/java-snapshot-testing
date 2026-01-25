package io.github.finoid.snapshots.docs;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.serializers.SerializerType;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;

public class LowercaseToStringSerializer implements SnapshotSerializer {
    @Override
    public Snapshot apply(Object object, SnapshotSerializerContext gen) {
        return gen.toSnapshot(object.toString().toLowerCase());
    }

    @Override
    public String getOutputFormat() {
        return SerializerType.TEXT.name();
    }
}
