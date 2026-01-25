package io.github.finoid.snapshots.jackson3.serializers.v1;

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
@SuppressWarnings("checkstyle:all") // TODO (nw) rewrite
public class DeterministicJacksonSnapshotSerializer extends JacksonSnapshotSerializer {

    /**
     * @param builder the builder to be built
     */
    @Override
    public void configure(final JsonMapper.Builder builder) {
        builder.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        builder.addModule(new DeterministicCollectionModule());
    }
}
