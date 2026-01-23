package au.com.origin.snapshots;

import au.com.origin.snapshots.exceptions.MissingSnapshotPropertiesKeyException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings({"checkstyle:all", "ImmutableEnumChecker"}) // TODO (nw) rewrite
public enum SnapshotProperties {
    INSTANCE;

    Properties snapshotProperties = new Properties();

    SnapshotProperties() {
        try {
            InputStream in =
                SnapshotProperties.class.getClassLoader().getResourceAsStream("snapshot.properties");
            snapshotProperties.load(in);
        } catch (Exception e) {
            // It's ok, if the SnapshotConfig implementation attempts to get a property they will receive
            // a MissingSnapshotPropertiesKeyException
        }
    }

    public static String getOrThrow(String key) {
        Object value = INSTANCE.snapshotProperties.get(key);
        if (value == null) {
            throw new MissingSnapshotPropertiesKeyException(key);
        }
        return value.toString();
    }

    @SuppressWarnings("TypeParameterUnusedInFormals") // TODO (nw) rewrite
    public static <T> T getInstance(String key) {
        String value = SnapshotProperties.getOrThrow(key);
        return createInstance(value);
    }

    public static <T> List<T> getInstances(String key) {
        String value = SnapshotProperties.getOrThrow(key);
        return Arrays.stream(value.split(","))
            .map(String::trim)
            .map(it -> (T) createInstance(it))
            .collect(Collectors.toList());
    }

    @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"}) // TODO (nw) rewrite
    private static <T> T createInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate class " + className, e);
        }
    }
}
