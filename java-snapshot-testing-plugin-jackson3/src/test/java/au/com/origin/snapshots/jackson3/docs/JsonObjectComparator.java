package au.com.origin.snapshots.jackson3.docs;

import au.com.origin.snapshots.Snapshot;
import au.com.origin.snapshots.comparators.SnapshotComparator;
import lombok.SneakyThrows;
import tools.jackson.databind.json.JsonMapper;

public class JsonObjectComparator implements SnapshotComparator {
  @Override
  public boolean matches(Snapshot previous, Snapshot current) {
    return asObject(previous.getName(), previous.getBody())
        .equals(asObject(current.getName(), current.getBody()));
  }

  @SneakyThrows
  private static Object asObject(String snapshotName, String json) {
    return new JsonMapper().readValue(json.replaceFirst(snapshotName + "=", ""), Object.class);
  }
}
