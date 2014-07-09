package org.taktik.utils.grails

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.springframework.web.multipart.commons.CommonsMultipartFile

import java.security.SecureRandom
/** Created by aduchate on 05/03/13, 08:22 */
class GrailsUtils {
	static String encodeCommaDelimitedToManyProperties(GrailsDomainClass clz, def params) {
		params.each { k, v ->
			if (clz.hasPersistentProperty(k)) {
				def p = clz.getPropertyByName(k)
				if (p.oneToMany || p.manyToMany) {
					if (v instanceof String) {
						while (v.startsWith(',')) { v = v[1..-1] }
						while (v.endsWith(',')) { v = v[0..-2] }
						params[k] = v.split(',')
					}
				}
			}
		}
	}

	static void deleteBelongsToObjectsRemovedFromToMany(GrailsDomainClass clz, def domain, def params, def force = []) {
		for (def p in clz.persistentProperties) {
			if (p.oneToMany && (p.isOwningSide() || force?.contains(p.name))) {
				def obj = params[p.name]
				if (obj != null) {
					obj = new HashSet(Arrays.asList(obj))
					def ref = domain[p.name]
					for (def o in new ArrayList(ref)) {
						if (!obj.contains(o.id as String)) {
							o.delete()
							ref.remove(o)
						}
					}
				}
			}
		}
	}

	static void pruneUnchangedBlobsAndCheckFormat(GrailsDomainClass clz, def params, Integer sizeLimit = 0, List<String> allowedTypes = null) {
		for (def p in clz.persistentProperties) {
			if (([] as Byte[]).class.isAssignableFrom(p.type) || ([] as byte[]).class.isAssignableFrom(p.type)) {
				def obj = params[p.name]
				if (obj != null && (obj instanceof String)) {
					if (obj == '<UNCHANGED>') {
						params.remove(p.name)
					} else if (obj == '') {
						params[p.name]=null
					}
					continue
				}
				if (sizeLimit) {
					if (obj instanceof CommonsMultipartFile) {
						if (((CommonsMultipartFile)obj)?.size>sizeLimit) {
							params.remove(p.name)
							throw new IllegalArgumentException("Size of file exceeds maximum limit of "+sizeLimit/1024+" kb")
						}
						if (allowedTypes != null) {
							boolean rejected = true;
							def type = ((CommonsMultipartFile) obj)?.contentType
							for (String typ in allowedTypes) {
								if (type?.matches(typ)) {
									rejected = false
									break
								}
							}
							if (rejected) {
								params.remove(p.name)
								throw new IllegalArgumentException("File type "+type+" is not allowed")
							}
						}
					}
					if (obj instanceof byte[] && obj instanceof Byte[] && (obj?.length>sizeLimit)) {
						params.remove(p.name)
						throw new IllegalArgumentException("Size of file exceeds maximum limit of "+sizeLimit/1024+" kb")
					}
				}
			}
		}
	}

	static void errorRedirect(def params, def action = null, def controller = null) {
		params['error_redirect_controller'] = controller ?: params.controller
		params['error_redirect_action'] = action ?: params.action
	}

	static String passwordGenerator(int nWords) {
		//Cryptographically secure password generator that gives 64000000 possibilities per word...
		//3 words is good, 4 words is a lot, 5 words is overkill (1192908395633424619122481111799589048051 possibilities > 10^40)
		SecureRandom random = new SecureRandom()

		URL r = GrailsUtils.class.getResource('words.txt')
		int nLines = 0
		//First pass, read lines
		r.eachLine { nLines++ }

		def indexes = []
		for (int i = 0; i < nWords; i++) {
			int rnd = random.nextInt(nLines - 1)
			indexes << rnd
		}
		def words = [:]
		try {
			def sIndexes = indexes.sort()
			nLines = 0
			r.eachLine {
				if (nLines == sIndexes[0]) {
					words[sIndexes[0]] = it.replaceAll('\n', '')
					if (sIndexes.size() == 1) {
						throw new InterruptedException()
					}
					sIndexes = sIndexes[1..-1]
				}
				nLines++
			}
		} catch (InterruptedException e) {}

		def scramblers = [
				{ it.replaceAll('e', '3') }, { it.replaceAll('a', '@') }, { it.replaceAll('a', '4') }, { it.replaceAll('s', '5') }, { it.replaceAll('o', '0') },
				{ it.capitalize() }, { it.toUpperCase() }, { it.replaceAll('i', '1') }, { it + '?' }, { it + '!' }
		]

		StringBuffer sb = new StringBuffer()
		for (int i in indexes) {
			sb.append(scramblers[random.nextInt(scramblers.size() - 1)](scramblers[random.nextInt(scramblers.size() - 1)](words[i]))).append(' ')
		}

		return (sb.toString())[0..-2];
	}
}
