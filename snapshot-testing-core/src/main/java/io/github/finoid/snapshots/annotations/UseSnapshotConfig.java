package io.github.finoid.snapshots.annotations;

import io.github.finoid.snapshots.config.SnapshotConfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface UseSnapshotConfig {
    Class<? extends SnapshotConfig> value();
}
