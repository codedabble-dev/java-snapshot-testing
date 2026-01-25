package io.github.finoid.snapshots;

import io.github.finoid.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@ExtendWith({SnapshotExtension.class})
public class NestedClassTest {

    @AfterAll
    public static void afterAll() {
        Path path =
            Paths.get("src/test/java/io/github/finoid/snapshots/__snapshots__/NestedClassTest.snap");
        assertThat(Files.exists(path)).isFalse();
    }

    @Nested
    class NestedClassWithExpectArgument {

        @Test
        public void helloWorldTest(Expect expect) {
            expect.toMatchSnapshot("Hello World");
        }
    }

    @Nested
    class NestedClassWithoutSnapshot {

        @Test
        public void helloWorldTest() {
            assertThat(true).isTrue();
        }
    }

    @Nested
    class NestedClassWithExpectInstance {

        Expect expect;

        @Test
        public void helloWorldTest() {
            expect.toMatchSnapshot("Hello World");
        }
    }
}
