package io.github.finoid.snapshots.docs;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.annotations.UseSnapshotConfig;
import io.github.finoid.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SnapshotExtension.class)
@UseSnapshotConfig(LowercaseToStringSnapshotConfig.class)
public class JUnit5ResolutionHierarchyExample {

    private Expect expect;

    @Test
    public void aliasMethodTest() {
        expect
            .serializer("json") // <------ Using snapshot.properties
            .toMatchSnapshot(new TestObject());
    }

    @Test
    public void customSerializerTest() {
        expect
            .serializer(UppercaseToStringSerializer.class) // <------ Using custom serializer
            .toMatchSnapshot(new TestObject());
    }

    // Read from LowercaseToStringSnapshotConfig defined on the class
    @Test
    public void lowercaseTest() {
        expect.toMatchSnapshot(new TestObject());
    }
}
