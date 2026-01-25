package io.github.finoid.snapshots.logging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingHelper {

    public static void deprecatedV5(Logger log, String message) {
        log.warn(
            "\n\n** Deprecation Warning **\nThis feature will be removed in version 5.X\n"
                + message
                + "\n\n");
    }
}
