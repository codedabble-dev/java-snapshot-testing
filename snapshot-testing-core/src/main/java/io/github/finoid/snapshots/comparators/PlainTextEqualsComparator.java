package io.github.finoid.snapshots.comparators;

import io.github.finoid.snapshots.logging.LoggingHelper;
import lombok.extern.slf4j.Slf4j;

@Deprecated
@Slf4j
public class PlainTextEqualsComparator
    extends io.github.finoid.snapshots.comparators.v1.PlainTextEqualsComparator {

    public PlainTextEqualsComparator() {
        super();
        LoggingHelper.deprecatedV5(
            log,
            "Update to `v1.comparators.io.github.finoid.snapshots.PlainTextEqualsComparator` in `snapshot.properties`");
    }
}
