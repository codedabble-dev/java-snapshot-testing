package io.github.finoid.snapshots;

import io.github.finoid.snapshots.annotations.SnapshotName;
import io.github.finoid.snapshots.comparators.SnapshotComparator;
import io.github.finoid.snapshots.config.SnapshotConfig;
import io.github.finoid.snapshots.exceptions.ReservedWordException;
import io.github.finoid.snapshots.exceptions.SnapshotExtensionException;
import io.github.finoid.snapshots.exceptions.SnapshotMatchException;
import io.github.finoid.snapshots.reporters.SnapshotReporter;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("NullAway") // TODO (nw) refactor
public class SnapshotContext {

    private static final List<String> RESERVED_WORDS = Arrays.asList("=", "[", "]");

    private final SnapshotConfig snapshotConfig;
    private final SnapshotFile snapshotFile;
    private final boolean isCI;

    @Getter
    private final Class<?> testClass;
    @Getter
    private final Method testMethod;
    private final Object current;

    @Setter
    @Getter
    private String scenario;
    @Getter
    private SnapshotHeader header = new SnapshotHeader();
    @Setter
    private SnapshotSerializer snapshotSerializer;
    @Setter
    private SnapshotComparator snapshotComparator;
    @Setter
    private List<SnapshotReporter> snapshotReporters;

    @SuppressWarnings("NullAway") // TODO (nw) refactor
    SnapshotContext(
        SnapshotConfig snapshotConfig,
        SnapshotFile snapshotFile,
        Class<?> testClass,
        Method testMethod,
        Object current) {
        this.snapshotConfig = snapshotConfig;
        this.snapshotFile = snapshotFile;
        this.testClass = testClass;
        this.testMethod = testMethod;
        this.current = current;

        this.isCI = snapshotConfig.isCI();
        this.snapshotSerializer = snapshotConfig.getSerializer();
        this.snapshotComparator = snapshotConfig.getComparator();
        this.snapshotReporters = snapshotConfig.getReporters();
        this.scenario = null;
    }

    public void toMatchSnapshot() {

        Set<Snapshot> rawSnapshots = snapshotFile.getSnapshots();
        Snapshot previousSnapshot = getRawSnapshot(rawSnapshots);
        Snapshot currentSnapshot = takeSnapshot();

        if (previousSnapshot != null && shouldUpdateSnapshot()) {
            snapshotFile.getSnapshots().remove(previousSnapshot);
            previousSnapshot = null;
        }

        if (previousSnapshot != null) {
            snapshotFile.pushDebugSnapshot(currentSnapshot);

            // Match existing Snapshot
            if (!snapshotComparator.matches(previousSnapshot, currentSnapshot)) {
                snapshotFile.createDebugFile(currentSnapshot);

                List<SnapshotReporter> reporters =
                    snapshotReporters.stream()
                        .filter(reporter -> reporter.supportsFormat(snapshotSerializer.getOutputFormat()))
                        .collect(Collectors.toList());

                if (reporters.isEmpty()) {
                    String comparator = snapshotComparator.getClass().getSimpleName();
                    throw new IllegalStateException(
                        "No compatible reporters found for comparator " + comparator);
                }

                List<Throwable> errors = new ArrayList<>();

                for (SnapshotReporter reporter : reporters) {
                    try {
                        reporter.report(previousSnapshot, currentSnapshot);
                    } catch (Throwable t) {
                        errors.add(t);
                    }
                }

                if (!errors.isEmpty()) {
                    throw new SnapshotMatchException("Error(s) matching snapshot(s)", errors);
                }
            }
        } else {
            if (this.isCI) {
                log.error(
                    "We detected you are running on a CI Server - if this is incorrect please override the isCI() method in SnapshotConfig");
                throw new SnapshotMatchException(
                    "Snapshot ["
                        + resolveSnapshotIdentifier()
                        + "] not found. Has this snapshot been committed ?");
            } else {
                log.warn(
                    "We detected you are running on a developer machine - if this is incorrect please override the isCI() method in SnapshotConfig");
                // Create New Snapshot
                snapshotFile.pushSnapshot(currentSnapshot);
                snapshotFile.pushDebugSnapshot(currentSnapshot);
            }
        }
    }

    private boolean shouldUpdateSnapshot() {
        if (snapshotConfig.updateSnapshot().isPresent() && snapshotConfig.isCI()) {
            throw new SnapshotExtensionException(
                "isCI=true & update-snapshot="
                    + snapshotConfig.updateSnapshot()
                    + ". Updating snapshots on CI is not allowed");
        }
        if (snapshotConfig.updateSnapshot().isPresent()) {
            return resolveSnapshotIdentifier().contains(snapshotConfig.updateSnapshot().get());
        } else {
            return false;
        }
    }

    private Snapshot getRawSnapshot(Collection<Snapshot> rawSnapshots) {
        synchronized (rawSnapshots) {
            for (Snapshot rawSnapshot : rawSnapshots) {
                if (rawSnapshot.getIdentifier().equals(resolveSnapshotIdentifier())) {
                    return rawSnapshot;
                }
            }
        }
        return null;
    }

    private Snapshot takeSnapshot() {
        SnapshotSerializerContext sg = SnapshotSerializerContext.from(this);
        return snapshotSerializer.apply(current, sg);
    }

    String resolveSnapshotIdentifier() {
        String scenarioFormat = scenario == null ? "" : "[" + scenario + "]";
        return snapshotName() + scenarioFormat;
    }

    private String snapshotName() {
        SnapshotName snapshotName = testMethod.getAnnotation(SnapshotName.class);
        return snapshotName == null
            ? testClass.getName() + "." + testMethod.getName()
            : snapshotName.value();
    }

    void checkValidContext() {
        for (String rw : RESERVED_WORDS) {
            if (snapshotName().contains(rw)) {
                throw new ReservedWordException("snapshot name", rw, RESERVED_WORDS);
            }
            if (scenario != null && scenario.contains(rw)) {
                throw new ReservedWordException("scenario name", rw, RESERVED_WORDS);
            }
        }
    }
}
