package au.com.origin.snapshots.jackson3.serializers.v1;

import au.com.origin.snapshots.jackson3.serializers.DeterministicCollectionModule;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Attempts to deterministically render a snapshot.
 *
 * <p>This can help in situations where collections are rendering in a different order on subsequent
 * runs.
 *
 * <p>Note that collections will be ordered which mar or may not be desirable given your use case.
 */
public class DeterministicJackson3SnapshotSerializer extends Jackson3SnapshotSerializer {

  @Override
  protected JsonMapper.Builder configure(JsonMapper.Builder builder) {
    return super.configure(builder)
        .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
        .disable(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST)
        .addModule(new DeterministicCollectionModule());
  }
}
