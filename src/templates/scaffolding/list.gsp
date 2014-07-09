<% import grails.persistence.Event %>
<%=packageName%>
<!doctype html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="kickstart" />
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  <title><g:message code="default.list.label" args="\${[message(code:"controller.\${params.controller}")]}" /></title>
</head>

<body>

<content tag="explanationMessage">\${message(code: '${className}.message')}</content>

<section id="list-${domainClass.propertyName}" class="first">
  <g:render template="select" />
  <r:require module="datatable"/>
</section>

</body>

</html>
