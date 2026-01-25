package io.github.finoid.snapshots.config;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.serializers.SerializerType;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;

public class ToStringSnapshotConfig extends BaseSnapshotConfig {

    @Override
    public SnapshotSerializer getSerializer() {
        return new SnapshotSerializer() {
            @Override
            public String getOutputFormat() {
                return SerializerType.TEXT.name();
            }

            @Override
            public Snapshot apply(Object object, SnapshotSerializerContext gen) {
                return gen.toSnapshot(object.toString());
            }
        };
    }
}
