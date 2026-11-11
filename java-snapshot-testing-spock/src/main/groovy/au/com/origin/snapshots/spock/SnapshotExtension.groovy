package au.com.origin.snapshots.spock

import au.com.origin.snapshots.SnapshotVerifier
import au.com.origin.snapshots.config.PropertyResolvingSnapshotConfig
import au.com.origin.snapshots.config.SnapshotConfig
import au.com.origin.snapshots.config.SnapshotConfigInjector
import groovy.transform.CompileStatic
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.SpecInfo

@CompileStatic
class SnapshotExtension extends AbstractAnnotationDrivenExtension<EnableSnapshots> implements SnapshotConfigInjector {

	SnapshotVerifier snapshotVerifier;

	void visitSpecAnnotation(EnableSnapshots annotation, SpecInfo spec) {
		this.snapshotVerifier = new SnapshotVerifier(getSnapshotConfig(), spec.reflection, false)
	}

	void visitSpec(SpecInfo spec) {
		def snapshotMethodInterceptor = new SnapshotMethodInterceptor(snapshotVerifier)
		for (FeatureInfo feature : spec.allFeatures) {
			feature.featureMethod.addInterceptor(snapshotMethodInterceptor)
		}
		spec.addCleanupSpecInterceptor(snapshotMethodInterceptor)
	}

	@Override
	SnapshotConfig getSnapshotConfig() {
		return new PropertyResolvingSnapshotConfig()
	}
}
