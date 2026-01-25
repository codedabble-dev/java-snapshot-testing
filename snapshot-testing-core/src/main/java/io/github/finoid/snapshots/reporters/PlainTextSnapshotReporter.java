package io.github.finoid.snapshots.reporters;

import io.github.finoid.snapshots.logging.LoggingHelper;
import lombok.extern.slf4j.Slf4j;

@Deprecated
@Slf4j
public class PlainTextSnapshotReporter
    extends io.github.finoid.snapshots.reporters.v1.PlainTextSnapshotReporter {

    public PlainTextSnapshotReporter() {
        super();
        LoggingHelper.deprecatedV5(
            log,
            "Update to `v1.reporters.io.github.finoid.snapshots.PlainTextSnapshotReporter` in `snapshot.properties`");
    }
}
