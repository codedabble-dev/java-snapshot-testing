package io.github.finoid.snapshots.jackson.serializers.v1;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.exceptions.SnapshotExtensionException;
import io.github.finoid.snapshots.serializers.SerializerType;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({"checkstyle:all", "deprecation"}) // TODO (nw) rewrite
public class JacksonSnapshotSerializer implements SnapshotSerializer {

    private final PrettyPrinter pp = new SnapshotPrettyPrinter();
    private final ObjectMapper objectMapper =
        new ObjectMapper() {
            {
                this.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
                this.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
                this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                this.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                if (shouldFindAndRegisterModules()) {
                    this.findAndRegisterModules();
                }

                this.setVisibility(
                    this.getSerializationConfig()
                        .getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
                JacksonSnapshotSerializer.this.configure(this);
            }
        };

    /**
     * Override to customize the Jackson objectMapper
     *
     * @param objectMapper existing ObjectMapper
     */
    public void configure(ObjectMapper objectMapper) {
    }

    /**
     * Override to control the registration of all available jackson modules within the classpath
     * which are locatable via JDK ServiceLoader facility, along with module-provided SPI.
     */
    protected boolean shouldFindAndRegisterModules() {
        return true;
    }

    @Override
    public Snapshot apply(Object object, SnapshotSerializerContext gen) {
        try {
            List<?> objects = Collections.singletonList(object);
            String body = objectMapper.writer(pp).writeValueAsString(objects);
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
