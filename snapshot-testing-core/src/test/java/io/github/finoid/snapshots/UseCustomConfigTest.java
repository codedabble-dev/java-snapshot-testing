package io.github.finoid.snapshots;

import io.github.finoid.snapshots.annotations.UseSnapshotConfig;
import io.github.finoid.snapshots.config.BaseSnapshotConfig;
import io.github.finoid.snapshots.config.SnapshotConfig;
import io.github.finoid.snapshots.config.ToStringSnapshotConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@UseSnapshotConfig(ToStringSnapshotConfig.class)
@ExtendWith(MockitoExtension.class)
public class UseCustomConfigTest {

    private static final SnapshotConfig DEFAULT_CONFIG = new BaseSnapshotConfig();

    @BeforeAll
    static void beforeAll() {
        SnapshotUtils.copyTestSnapshots();
    }

    @Test
    void canUseSnapshotConfigAnnotationAtClassLevel(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(DEFAULT_CONFIG, testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot(new TestObject());
        snapshotVerifier.validateSnapshots();
    }

    private class TestObject {
        @Override
        public String toString() {
            return "This is a snapshot of the toString() method";
        }
    }
}
