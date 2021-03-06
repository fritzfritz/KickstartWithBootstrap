<%@ page import="kickstart._DemoPage" %>
<!doctype html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="kickstart" />
	<g:set var="entityName" value="${message(code: '_DemoPage.label', default: '_DemoPage')}" />
	<title><g:message code="default.create.label" args="${[message(code:"controller.${params.controller}")]}" /></title>
</head>

<body>

<section id="create-_DemoPage" class="first">

	<g:hasErrors bean="${_DemoPageInstance}">
	<div class="alert alert-danger">
		<g:renderErrors bean="${_DemoPageInstance}" as="list" />
	</div>
	</g:hasErrors>
	
	<g:form useToken="true" action="save" class="form-horizontal"  enctype="multipart/form-data">
		<fieldset class="form">
			<g:render template="form"/>
		</fieldset>
		<div class="form-actions">
			<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
            <button class="btn" type="reset">Cancel</button>
		</div>
	</g:form>
	
</section>
		
</body>

</html>
