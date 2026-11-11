package au.com.origin.snapshots.spock

import spock.lang.Specification

class GroovyRuntimeCompatibilitySpec extends Specification {

	private static final String REMOVED_IN_GROOVY_6 = 'org/codehaus/groovy/runtime/callsite/CallSiteArray'

	def "extension classes do not reference the call site cache that Groovy 6 removed"() {
		given:
		def classesRoot = new File(SnapshotExtension.protectionDomain.codeSource.location.toURI())
		def extensionClasses = []
		classesRoot.eachFileRecurse { file ->
			if (file.name.endsWith('.class')) {
				extensionClasses << file
			}
		}

		expect:
		!extensionClasses.empty
		extensionClasses.findAll { new String(it.bytes, 'ISO-8859-1').contains(REMOVED_IN_GROOVY_6) }.empty
	}
}
