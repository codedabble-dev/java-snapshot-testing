package au.com.origin.snapshots.jackson3.serializers;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

/**
 * Inspired by:
 * https://www.stubbornjava.com/posts/creating-a-somewhat-deterministic-jackson-objectmapper
 */
public class DeterministicCollectionModule extends SimpleModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeterministicCollectionModule.class);

  public DeterministicCollectionModule() {
    addSerializer(Collection.class, new CollectionSerializer());
  }

  private static class CollectionSerializer extends ValueSerializer<Collection> {

    @Override
    public void serialize(Collection value, JsonGenerator gen, SerializationContext context) {
      Object[] sorted = convert(value);
      context.writeValue(gen, sorted);
    }

    private Object[] convert(Collection<?> value) {
      if (value == null || value.isEmpty()) {
        return Collections.emptyList().toArray();
      }

      try {
        return value.stream()
            .filter(Objects::nonNull)
            .sorted()
            .collect(Collectors.toList())
            .toArray();
      } catch (ClassCastException ex) {
        LOGGER.warn(
            "Unable to sort() collection - this may result in a non deterministic snapshot.\n"
                + "Consider adding a custom serializer for this type via the Jackson3SnapshotSerializer#configure() method.\n"
                + ex.getMessage());
        return value.toArray();
      }
    }
  }
}
