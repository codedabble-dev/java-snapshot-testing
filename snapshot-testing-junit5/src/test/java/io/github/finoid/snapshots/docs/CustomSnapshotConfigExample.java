package io.github.finoid.snapshots.docs;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.annotations.UseSnapshotConfig;
import io.github.finoid.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SnapshotExtension.class)
// apply your custom snapshot configuration to this test class
@UseSnapshotConfig(LowercaseToStringSnapshotConfig.class)
public class CustomSnapshotConfigExample {

    @Test
    public void myTest(Expect expect) {
        expect.toMatchSnapshot("hello world");
    }
}
