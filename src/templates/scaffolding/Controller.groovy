<%=packageName ? "package ${packageName}\n\n" : ''%>

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.GrailsClass
import org.springframework.dao.DataIntegrityViolationException
import org.taktik.commons.uti.UTIDetector
import org.taktik.utils.grails.GrailsUtils

/**
 * ${className}Controller
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Secured(['ROLE_ADMIN'])
class ${className}Controller {
	def grailsApplication
	def ${className[0].toLowerCase()+className[1..-1]}Service
	UTIDetector utiDetector

	static allowedMethods = [save: "POST", update: "POST", delete: ["GET","POST"]]

	def index() {
		redirect(action: "list", params: params)
	}

	def list() {
		if (!params.ajaxSource) { params.ajaxSource = 'listObjects' }
		render(view: 'list')
	}

	def select() {
		if (!params.ajaxSource) { params.ajaxSource = 'listObjects' }
		render(view: 'select')
	}

	def listObjects() {
		_listObjects(params, { r ->  params.mode == 'select' ? "<button onclick=\"if (hiddenfield_add('hidden-\${params.table_id?.encodeAsJavaScript()}','\${r.id}')) select_ajax_modal('\${params.sender_id}','\${params.table_id?.encodeAsJavaScript()}','\${params.callback_url}','\${r.id}');\" class=\"btn btn-mini btn-primary\">\${message(code:'default.button.select.label')}</button>" : """
                \${g.link(action: "edit", id: r.id, role: "button", 'class': "btn btn-mini btn-primary", params: ['pc': 'list'], message(code: "default.button.edit.label"))}
                \${sajx.remoteLink(action: "delete", id: r.id, role: "button", 'class': "btn btn-mini btn-danger", before: "if (!confirm('\${message(code: 'default.button.delete.confirm.message', args: ['${className}']).encodeAsHTML()}')) {return}", onSuccess: "\\\$('#${domainClass.propertyName}-table').dataTable().fnReloadAjax()", message(code: 'default.button.delete.label'))}
"""})
	}

	def listForSelection() {
		assert params.callback_function?.matches('[a-zA-Z_]+')
		_listObjects(params, { r-> "<button onclick=\"\$params.callback_function('\${r.id}');\" class=\"btn btn-mini btn-primary\">\${message(code:'default.button.select.label')}</button>"})
	}

	private void _listObjects(def params, def actions) {
		//Convert datatable params to standard
		params.offset = params.offset ?: params.iDisplayStart;
		params.max = params.max ?: params.iDisplayLength;

		def props = params.props?.split(',')?.collect { it.replaceAll('[^_a-zA-Z0-9\\\\.]', '') }

		params.sort = params.sort ?: (params.iSortCol_0?.length() && params.iSortCol_0?.matches(/[0-9]+/) && params.int('iSortCol_0') < props.size() ? props[params.int('iSortCol_0')] : null)
		params.order = params.order ?: params.sSortDir_0

		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def ${propertyName}List = params.sSearch?.length() ? ${className}.findAllByNameIlike("%\${params.sSearch}%", params) : ${className}.list(params);
		def ${propertyName}Total = ${className}.count();

		def res = ${propertyName}List.collect { r ->
			def first = true
			props?.collect { p ->
				String res = p == '_actions' ? actions(r) : (formatProperty(r."\${p}",r,"\${p}") ?: '')
				if (first) {
					res = link(action:"show",id:"\${r.id}",res)
					first = false
				}
				return res
			}
		}

		render([sEcho: params.sEcho, iTotalRecords: ${propertyName}Total, iTotalDisplayRecords: res.size(), aaData: res] as JSON)
	}

	def formatProperty(def p, def o, def pName) {
		if (p == null) { return ''}
		if (p.class == Boolean.class || p.class == boolean.class) {
			return formatBoolean(boolean:p)
		} else if (Date.class.isAssignableFrom(p.class) || Date.class.isAssignableFrom(p.class)) {
			return formatDate(date:p)
		} else if (p.class == ([] as Byte[]).class || p.class == ([] as byte[]).class) {
			return "<img src=\"\${createLink(action: 'displayImage', id: o.id, params: [property:pName])}\" style=\"height:80px;\"/>"
		} else {
			return p?.encodeAsHTML()
		}
	}


	def create() {
		[${propertyName}: new ${className}(params)]
	}

	def save() {
		org.taktik.utils.grails.GrailsUtils.errorRedirect(params,'create')
		withForm {
			def clazz = grailsApplication.getDomainClass(${className}.class.getName())
			GrailsUtils.encodeCommaDelimitedToManyProperties(clazz, params)

			def ${propertyName} = new ${className}(params)
			if (!${propertyName}.save(flush: true)) {
				render(view: "create", model: [${propertyName}: ${propertyName}])
				return
			}

			flash.message = message(code: 'default.created.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
			redirect(action: "show", id: ${propertyName}.id)
		}.invalidToken {
			response.status = 405
		}
	}

	def saveAjax() {
		withForm {
			def clazz = grailsApplication.getDomainClass(${className}.class.getName())
			GrailsUtils.encodeCommaDelimitedToManyProperties(clazz, params)

			def ${propertyName} = new ${className}(params)
			if (!${propertyName}.save(flush: true)) {
				render ([id:null] as JSON)
			}

			render ([id:${propertyName}.id] as JSON)
		}.invalidToken {
			response.status = 405
		}
	}

	def show() {
		def ${propertyName} = ${className[0].toLowerCase()+className[1..-1]}Service.get(params.id)
		if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
			redirect(action: "list")
			return
		}

		[${propertyName}: ${propertyName}]
	}

	def edit() {
		def ${propertyName} = ${className[0].toLowerCase()+className[1..-1]}Service.get(params.id)
		if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
			redirect(action: "list")
			return
		}

		[${propertyName}: ${propertyName}]
	}

	def update() {
		org.taktik.utils.grails.GrailsUtils.errorRedirect(params,'edit')
		withForm {
			def ${propertyName} = ${className[0].toLowerCase()+className[1..-1]}Service.get(params.id)
			if (!${propertyName}) {
				flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
				redirect(action: "list")
				return
			}

			if (params.version) {
				def version = params.version.toLong()
				if (${propertyName}.version > version) {<% def lowerCaseName = grails.util.GrailsNameUtils.getPropertyName(className) %>
								${propertyName}.errors.rejectValue("version", "default.optimistic.locking.failure",
												[message(code: '${domainClass.propertyName}.label', default: '${className}')] as Object[],
												"Another user has updated this ${className} while you were editing")
					render(view: "edit", model: [${propertyName}: ${propertyName}])
					return
				}
			}

			def clazz = grailsApplication.getDomainClass(${className}.class.getName())
			GrailsUtils.encodeCommaDelimitedToManyProperties(clazz, params)
			GrailsUtils.deleteBelongsToObjectsRemovedFromToMany(clazz,${propertyName}, params)

			${propertyName}.properties[<% print "${domainClass.persistentProperties*.name.collect {"'${it}'"}.join(',')}" %>] = params

			if (!${propertyName}.save(flush: true)) {
				render(view: "edit", model: [${propertyName}: ${propertyName}])
				return
			}

			flash.message = message(code: 'default.updated.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
			redirect(action: "show", id: ${propertyName}.id)
		}.invalidToken {
			response.status = 405
		}
	}

	def delete() {
		org.taktik.utils.grails.GrailsUtils.errorRedirect(params,'list')
		def ${propertyName} = ${className[0].toLowerCase()+className[1..-1]}Service.get(params.id)
		if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
			redirect(action: "list")
			return
		}

		try {
			${propertyName}.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
			redirect(action: "show", id: params.id)
		}
	}

	def displayImage() {
		assert params.id
		assert params.property

		def ${propertyName} = ${className[0].toLowerCase()+className[1..-1]}Service.get(params.id)
		def clazz = grailsApplication.getDomainClass(${className}.class.getName())

		if (clazz.hasPersistentProperty(params.property)) {
			def data = ${propertyName}."\${params.property}"
			if (data != null && (([] as Byte[]).class.isAssignableFrom(data.class)||([] as byte[]).class.isAssignableFrom(data.class))) {
				if (data.length >= 10) {
					response.contentType = utiDetector.detectUTI(new ByteArrayInputStream(data),null,null,null)?.mimeTypes?.first() ?: 'image/png'
					response.contentLength = data.length
					response.outputStream.write(data)
				}
			}
		}

		def resource = grailsApplication.mainContext.getResource('images/noImage.gif');

		response.contentType = 'image/gif'
		response.contentLength = resource.contentLength()
		response.outputStream.write(resource.getInputStream().bytes)

	}

	def insert() {
		assert params.objectId
		assert params.property
		assert params.tableId
		params.node = params.node?:'table'

		GrailsClass dc = grailsApplication.getDomainClass(${className}.class.getName())

		def property = dc.getPropertyByName(params.property)
		if (property.manyToMany || property.oneToMany) {
			def item = property.referencedDomainClass.clazz.get(params.objectId)
			if (params.node=='table') {
				render([link(controller:property.referencedDomainClass.propertyName,action:"show",id:item.id,item?.encodeAsHTML()),
								"<button class='btn btn-mini btn-danger' onclick=\"if (confirm('\${message(code: "default.button.\${params.deleteLabel?:'delete'}.confirm.message", args: [message(code:org.hibernate.Hibernate.getClass(item).name).split(/\\./).last(), message(code:'${domainClass.propertyName}.label')]).encodeAsHTML()}')) {hiddenfield_delete('hidden-\${params.tableId}','\${item.id}');\\\$('#\${params.tableId}').dataTable().fnDeleteRow(\\\$('#\${params.tableId}').dataTable().fnGetPosition(\\\$(this).closest('tr').get(0)));}\">\${message(code:"default.button.\${params.delete?:'deleteLabel'}.label").encodeAsHTML()}</div>"] as JSON)
			} else {
				render(["<li>\${link(controller:property.referencedDomainClass.propertyName,action:'show',id:item.id,item?.encodeAsHTML())}<span class='btn btn-mini' onclick=\"if (confirm('\${message(code: "default.button.\${params.deleteLabel?:'delete'}.confirm.message", args: [message(code:org.hibernate.Hibernate.getClass(item).name).split(/\\./).last(), message(code:'${domainClass.propertyName}.label')]).encodeAsHTML()}')) {hiddenfield_delete('hidden-\${params.tableId}','\${item.id}');\\\$(this).closest('li').remove();}\">x</span></li>"] as JSON)
			}
		}
		throw new IllegalArgumentException('Invalid parameters')
	}

}
