grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.repos.default = "taktik"

grails.project.dependency.resolution = {
	// inherit Grails' default dependencies
	inherits("global") {
		// uncomment to disable ehcache
		// excludes 'ehcache'
	}
	log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	repositories {
		mavenRepo(id: 'maven.taktik.be', url: 'http://maven.taktik.be/nexus/content/groups/public/')

		grailsCentral()
		mavenLocal()
		mavenCentral()
	}
	dependencies {
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		compile 'org.taktik.commons:commons-uti:1.1-SNAPSHOT'
	}
	plugins {
        build(	":release:3.0.1",
              	":rest-client-builder:1.0.3") {
            export = false
        }
		compile ":scaffolding:2.1.2"
		compile ":asset-pipeline:1.9.9"
		compile ":less-asset-pipeline:1.10.0"

	}
}
