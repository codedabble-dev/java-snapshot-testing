package io.github.finoid.snapshots.docs;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.SnapshotVerifier;
import io.github.finoid.snapshots.config.PropertyResolvingSnapshotConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

// Notice we aren't using any framework extensions
public class CustomFrameworkExample {

    private static SnapshotVerifier snapshotVerifier;

    @BeforeAll
    static void beforeAll() {
        snapshotVerifier =
            new SnapshotVerifier(new PropertyResolvingSnapshotConfig(), CustomFrameworkExample.class);
    }

    @AfterAll
    static void afterAll() {
        snapshotVerifier.validateSnapshots();
    }

    @Test
    void shouldMatchSnapshotOne(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot("Hello World");
    }
}
