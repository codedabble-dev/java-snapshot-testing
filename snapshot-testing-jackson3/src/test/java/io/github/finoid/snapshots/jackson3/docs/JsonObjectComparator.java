package io.github.finoid.snapshots.jackson3.docs;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.comparators.SnapshotComparator;
import lombok.SneakyThrows;
import tools.jackson.databind.json.JsonMapper;

public class JsonObjectComparator implements SnapshotComparator {
    @SneakyThrows
    private static Object asObject(String snapshotName, String json) {
        return new JsonMapper().readValue(json.replaceFirst(snapshotName + "=", ""), Object.class);
    }

    @Override
    public boolean matches(Snapshot previous, Snapshot current) {
        return asObject(previous.getName(), previous.getBody())
            .equals(asObject(current.getName(), current.getBody()));
    }
}
