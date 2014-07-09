<%=packageName%>
<!doctype html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="kickstart"/>
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}"/>
  <title><g:message code="default.edit.label" args="\${[message(code:"controller.\${params.controller}")]}"/></title>
</head>

<body>

<section id="edit-${domainClass.propertyName}" class="first">

  <g:hasErrors bean="\${${propertyName}}">
    <div class="alert alert-error">
      <g:renderErrors bean="\${${propertyName}}" as="list"/>
    </div>
  </g:hasErrors>

  <g:form useToken="true" method="post" class="form-horizontal"  params="\${['pc':params.pc]}" <%=multiPart ? ' enctype="multipart/form-data"' : '' %>>
  <g:hiddenField name="id" value="\${${propertyName}?.id}"/>
  <g:hiddenField name="version" value="\${${propertyName}?.version}"/>
  <fieldset class="form">
    <g:render template="form"/>
  </fieldset>

  <div class="form-actions">
    <g:actionSubmit class="btn btn-primary" action="update"
                    value="\${message(code: 'default.button.update.label', default: 'Update')}"/>
    <button class="btn" type="reset" onclick="window.location.href = '\${createLink(action: params.pc?params.pc:'show', id:params.id)}';">Cancel</button>
    <g:actionSubmit class="btn btn-danger pull-right" action="delete"
                    value="\${message(code: 'default.button.delete.label', default: 'Delete')}"
                    onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
  </div>
  </g:form>

</section>

</body>

</html>
