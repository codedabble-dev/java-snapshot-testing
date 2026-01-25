package io.github.finoid.snapshots;

import io.github.finoid.snapshots.annotations.SnapshotName;
import io.github.finoid.snapshots.config.BaseSnapshotConfig;
import io.github.finoid.snapshots.exceptions.SnapshotExtensionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SnapshotNameAnnotationWithDuplicatesTest {

    @SnapshotName("hello_world")
    @Test
    void canUseSnapshotNameAnnotation(TestInfo testInfo) {
        assertThrows(
            SnapshotExtensionException.class,
            () -> new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get()),
            "Oops, looks like you set the same name of two separate snapshots @SnapshotName(\"hello_world\") in "
                + "class io.github.finoid.snapshots.SnapshotNameAnnotationTest");
    }

    @SnapshotName("hello_world")
    private void anotherMethodWithSameSnapshotName() {
    }
}
