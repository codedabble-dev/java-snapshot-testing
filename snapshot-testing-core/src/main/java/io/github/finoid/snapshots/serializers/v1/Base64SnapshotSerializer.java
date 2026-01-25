package io.github.finoid.snapshots.serializers.v1;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.serializers.SerializerType;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This Serializer converts a byte[] into a base64 encoded string. If the input is not a byte[] it
 * will be converted using `.getBytes(StandardCharsets.UTF_8)` method
 */
public class Base64SnapshotSerializer implements SnapshotSerializer {
    private static final ToStringSnapshotSerializer TO_STRING_SNAPSHOT_SERIALIZER = new ToStringSnapshotSerializer();

    @Override
    public Snapshot apply(Object object, SnapshotSerializerContext gen) {
        if (object == null) {
            TO_STRING_SNAPSHOT_SERIALIZER.apply("", gen);
        }
        byte[] bytes =
            object instanceof byte[]
                ? (byte[]) object
                : object.toString().getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return TO_STRING_SNAPSHOT_SERIALIZER.apply(encoded, gen);
    }

    @Override
    public String getOutputFormat() {
        return SerializerType.BASE64.name();
    }
}
