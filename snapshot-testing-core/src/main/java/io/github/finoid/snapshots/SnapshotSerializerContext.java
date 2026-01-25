package io.github.finoid.snapshots;

import io.github.finoid.snapshots.annotations.SnapshotName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * Contains details of the pending snapshot that can be modified in the Serializer prior to calling
 * toSnapshot().
 */
@AllArgsConstructor
public class SnapshotSerializerContext {

    @Getter
    @Setter
    private final String name;
    @Getter
    @Setter
    private final String scenario;
    @Getter
    @Setter
    private final SnapshotHeader header;
    @Getter
    private final Class<?> testClass;
    @Getter
    private final Method testMethod;

    public static SnapshotSerializerContext from(SnapshotContext context) {
        SnapshotName snapshotName = context.getTestMethod().getAnnotation(SnapshotName.class);
        String name =
            snapshotName == null
                ? context.getTestClass().getName() + "." + context.getTestMethod().getName()
                : snapshotName.value();
        return new SnapshotSerializerContext(
            name,
            context.getScenario(),
            context.getHeader(),
            context.getTestClass(),
            context.getTestMethod());
    }

    public Snapshot toSnapshot(String body) {
        return Snapshot.builder().name(name).scenario(scenario).header(header).body(body).build();
    }
}
