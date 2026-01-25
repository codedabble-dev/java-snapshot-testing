package io.github.finoid.snapshots;

import io.github.finoid.snapshots.annotations.SnapshotName;
import io.github.finoid.snapshots.config.BaseSnapshotConfig;
import io.github.finoid.snapshots.exceptions.ReservedWordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SnapshotNameAnnotationTest {

    @BeforeEach
    void beforeEach() {
        SnapshotUtils.copyTestSnapshots();
    }

    @SnapshotName("can_use_snapshot_name")
    @Test
    void canUseSnapshotNameAnnotation(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot("Hello World");
        snapshotVerifier.validateSnapshots();
    }

    @SnapshotName("can use snapshot name with spaces")
    @Test
    void canUseSnapshotNameAnnotationWithSpaces(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot("Hello World");
        snapshotVerifier.validateSnapshots();
    }

    @SnapshotName("can't use '=' character in snapshot name")
    @Test
    void cannotUseEqualsInsideSnapshotName(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(ReservedWordException.class, () -> expect.toMatchSnapshot("FooBar"));
    }

    @SnapshotName("can't use '[' character in snapshot name")
    @Test
    void cannotUseOpeningSquareBracketInsideSnapshotName(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(ReservedWordException.class, () -> expect.toMatchSnapshot("FooBar"));
    }

    @SnapshotName("can't use ']' character in snapshot name")
    @Test
    void cannotUseClosingSquareBracketInsideSnapshotName(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(ReservedWordException.class, () -> expect.toMatchSnapshot("FooBar"));
    }

    @Test
    void cannotUseEqualsInsideScenarioName(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(
            ReservedWordException.class,
            () -> expect.scenario("can't use = symbol in scenario").toMatchSnapshot("FooBar"));
    }

    @Test
    void cannotUseOpeningSquareBracketInsideScenarioName(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(
            ReservedWordException.class,
            () -> expect.scenario("can't use [ symbol in scenario").toMatchSnapshot("FooBar"));
    }

    @Test
    void cannotUseClosingSquareBracketInsideScenarioName(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(
            ReservedWordException.class,
            () -> expect.scenario("can't use ] symbol in scenario").toMatchSnapshot("FooBar"));
    }
}
