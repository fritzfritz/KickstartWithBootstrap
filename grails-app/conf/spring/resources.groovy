import kickstart.CustomDateEditorRegistrar

import org.taktik.commons.uti.SimpleUTIDetector

// Place your Spring DSL code here
beans = {
	customPropertyEditorRegistrar(CustomDateEditorRegistrar) {
		grailsApplication = ref("grailsApplication")
	}
	utiDetector(SimpleUTIDetector)
}
