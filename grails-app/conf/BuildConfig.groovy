grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.repos.taktik.url = "http://maven.taktik.be/nexus/content/repositories/snapshots"
grails.project.repos.default = "taktik"
grails.project.repos.taktik.type = "maven"
grails.project.repos.taktik.username = "aduchate"
grails.project.repos.taktik.password = "m0slnd@"

grails.project.dependency.resolution = {
	// inherit Grails' default dependencies
	inherits("global") {
		// uncomment to disable ehcache
		// excludes 'ehcache'
	}
	log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	repositories {
		mavenRepo "http://maven.taktik.be/nexus/content/groups/public"

		grailsCentral()
		mavenLocal()
		mavenCentral()
	}
	credentials {
		realm = "Sonatype Nexus Repository Manager"
		host = "maven.taktik.be"
		username = "aduchate "
		password = "m0slnd@"
	}
	dependencies {
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		compile 'org.taktik.commons:commons-uti:1.1-SNAPSHOT'
	}
	plugins {
//		runtime	(":hibernate:$grailsVersion")		{ export = false }		// needed for testing the plugin as an app
//		build	(":tomcat:$grailsVersion")			{ export = false }		// needed for testing the plugin as an app
//		build	(":rest-client-builder:1.0.3")		{ export = false }		// needed for plugin deployment
		build(":release:2.2.1") { export = false }    // needed for plugin deployment

		runtime(":jquery:1.8.3") { export = true }    // needed for the Bootstrap datepicker
//		runtime	(":jquery:1.10.2")					{ export = true }		// WARN: does not work with Bootstrap Datepicker !
		runtime(":resources:1.2") { export = true }    // needed for Bootstrap's less files

		compile(":lesscss-resources:1.3.3") { export = true }    // needed for Bootstrap's less files

		compile(":font-awesome-resources:3.2.1.2") { export = true }    // needed for Bootstrap's image replacement
	}
}
