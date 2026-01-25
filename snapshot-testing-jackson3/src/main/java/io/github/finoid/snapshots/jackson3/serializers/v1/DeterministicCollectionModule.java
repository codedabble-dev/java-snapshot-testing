package io.github.finoid.snapshots.jackson3.serializers.v1;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Inspired by: https://www.stubbornjava.com/posts/creating-a-somewhat-deterministic-jackson-objectmapper.
 */
@Slf4j
public class DeterministicCollectionModule extends SimpleModule {

    public DeterministicCollectionModule() {
        addSerializer(Collection.class, new CollectionSerializer());
    }

    /**
     * Collections gets converted into a sorted Object[]. This then gets serialized using the default
     * Array serializer.
     */
    private static class CollectionSerializer<T> extends ValueSerializer<Collection<T>> {

        @Override
        public void serialize(Collection value, JsonGenerator gen, SerializationContext ctxt)
            throws JacksonException {
            Object[] sorted = convert(value);

            if (value == null) {
                ctxt.getDefaultNullValueSerializer().serialize(null, gen, ctxt);
            } else {
                ctxt.findTypedValueSerializer(Object[].class, true).serialize(sorted, gen, ctxt);
            }
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
                log.warn(
                    "Unable to sort() collection - this may result in a non deterministic snapshot.\n"
                        + "Consider adding a custom serializer for this type via the JacksonSnapshotSerializer#configure() method.\n"
                        + ex.getMessage());
                return value.toArray();
            }
        }
    }
}
