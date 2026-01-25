package io.github.finoid.snapshots.jackson3.serializers.v1;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.exceptions.SnapshotExtensionException;
import io.github.finoid.snapshots.serializers.SerializerType;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.Separators;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("checkstyle:all") // TODO (nw) rewrite
public class JacksonSnapshotSerializer implements SnapshotSerializer {
    static DefaultPrettyPrinter.Indenter lfOnlyIndenter = new DefaultIndenter("  ", "\n");
    private static final DefaultPrettyPrinter pp = new DefaultPrettyPrinter() {
        {
            this.indentArraysWith(lfOnlyIndenter);
            this.indentObjectsWith(lfOnlyIndenter);

            Separators separators = Separators.createDefaultInstance()
                .withRootSeparator("");
            this.withSeparators(separators);
        }

        // It's a requirement
        // @see https://github.com/FasterXML/jackson-databind/issues/2203
        public DefaultPrettyPrinter createInstance() {
            return new DefaultPrettyPrinter(this);
        }
    }.withArrayIndenter(lfOnlyIndenter);
    private final JsonMapper jsonMapper = createMapper();

    private JsonMapper createMapper() {
        JsonMapper.Builder builder =
            JsonMapper.builder()
                .defaultPrettyPrinter(pp)
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .enable(DateTimeFeature.WRITE_DATES_WITH_ZONE_ID)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL))
                .changeDefaultVisibility(visibility ->
                    visibility.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

        if (shouldFindAndRegisterModules()) {
            builder.findAndAddModules();
        }

        configure(builder);

        return builder.build();
    }

    /**
     * Override to customize the Jackson jsonMapper
     *
     * @param builder the builder to be built
     */
    public void configure(final JsonMapper.Builder builder) {
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

            String body = jsonMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(objects);
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
