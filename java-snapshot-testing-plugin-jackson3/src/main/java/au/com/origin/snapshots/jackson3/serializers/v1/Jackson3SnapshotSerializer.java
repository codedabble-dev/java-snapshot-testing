package au.com.origin.snapshots.jackson3.serializers.v1;

import au.com.origin.snapshots.Snapshot;
import au.com.origin.snapshots.SnapshotSerializerContext;
import au.com.origin.snapshots.exceptions.SnapshotExtensionException;
import au.com.origin.snapshots.serializers.SerializerType;
import au.com.origin.snapshots.serializers.SnapshotSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import java.util.List;
import tools.jackson.core.PrettyPrinter;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

public class Jackson3SnapshotSerializer implements SnapshotSerializer {

  private final PrettyPrinter pp = new SnapshotPrettyPrinter();
  private final ObjectMapper objectMapper = buildObjectMapper();

  protected JsonMapper.Builder configure(JsonMapper.Builder builder) {
    return builder;
  }

  protected boolean shouldFindAndRegisterModules() {
    return true;
  }

  private ObjectMapper buildObjectMapper() {
    JsonMapper.Builder builder =
        JsonMapper.builder()
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(DateTimeFeature.WRITE_DATES_WITH_ZONE_ID)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .changeDefaultPropertyInclusion(
                inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL))
            .changeDefaultVisibility(
                visibility ->
                    visibility
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

    if (shouldFindAndRegisterModules()) {
      builder.findAndAddModules();
    }
    return configure(builder).build();
  }

  @Override
  public Snapshot apply(Object object, SnapshotSerializerContext gen) {
    try {
      List<?> objects = Arrays.asList(object);
      String body = objectMapper.writer().with(pp).writeValueAsString(objects);
      return gen.toSnapshot(body);
    } catch (Exception e) {
      throw new SnapshotExtensionException("Jackson Serialization failed", e);
    }
  }

  @Override
  public String getOutputFormat() {
    return SerializerType.JSON.name();
  }
}
