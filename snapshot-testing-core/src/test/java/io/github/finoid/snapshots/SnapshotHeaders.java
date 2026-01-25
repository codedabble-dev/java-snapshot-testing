package io.github.finoid.snapshots;

import io.github.finoid.snapshots.config.BaseSnapshotConfig;
import io.github.finoid.snapshots.config.SnapshotConfig;
import io.github.finoid.snapshots.serializers.LowercaseToStringSerializer;
import io.github.finoid.snapshots.serializers.SerializerType;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("checkstyle:all") // TODO (nw) rewrite
@ExtendWith(MockitoExtension.class)
public class SnapshotHeaders {

    private static final SnapshotConfig DEFAULT_CONFIG = new BaseSnapshotConfig();

    static SnapshotVerifier snapshotVerifier;

    @BeforeAll
    static void beforeAll() {
        SnapshotUtils.copyTestSnapshots();
        snapshotVerifier = new SnapshotVerifier(DEFAULT_CONFIG, SnapshotHeaders.class);
    }

    @AfterAll
    static void afterAll() {
        snapshotVerifier.validateSnapshots();
    }

    @Test
    void shouldBeAbleToSnapshotASingleCustomHeader(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.serializer(CustomHeadersSerializer.class).toMatchSnapshot("Hello World");
    }

    @Test
    void shouldBeAbleToSnapshotMultipleCustomHeader(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.serializer(CustomHeadersSerializer.class).toMatchSnapshot("Hello World");
    }

    @NoArgsConstructor
    private static class CustomHeadersSerializer extends LowercaseToStringSerializer {
        @Override
        public String getOutputFormat() {
            return SerializerType.JSON.name();
        }

        @Override
        public Snapshot apply(Object object, SnapshotSerializerContext snapshotSerializerContext) {
            snapshotSerializerContext.getHeader().put("custom", "anything");
            snapshotSerializerContext.getHeader().put("custom2", "anything2");
            return super.apply(object, snapshotSerializerContext);
        }
    }
}
