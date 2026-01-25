package io.github.finoid.snapshots.serializers;

import io.github.finoid.snapshots.logging.LoggingHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * This Serializer does a snapshot of the {@link Object#toString()} method.
 *
 * <p>Will render each toString() on a separate line
 */
@Slf4j
@Deprecated
public class ToStringSnapshotSerializer
    extends io.github.finoid.snapshots.serializers.v1.ToStringSnapshotSerializer {
    public ToStringSnapshotSerializer() {
        super();
        LoggingHelper.deprecatedV5(
            log,
            "Update to `v1.serializers.io.github.finoid.snapshots.ToStringSnapshotSerializer` in `snapshot.properties`");
    }
}
