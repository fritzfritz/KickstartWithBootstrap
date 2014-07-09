<%=packageName%>
<!doctype html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="kickstart" />
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  <title><g:message code="default.create.label" args="\${[message(code:"controller.\${params.controller}")]}" /></title>
</head>

<body>

<section id="create-${domainClass.propertyName}" class="first">

  <g:hasErrors bean="\${${propertyName}}">
    <div class="alert alert-error">
      <g:renderErrors bean="\${${propertyName}}" as="list" />
    </div>
  </g:hasErrors>

  <g:form useToken="true" action="save" class="form-horizontal"  params="\${['pc':params.pc]}" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
  <fieldset class="form">
    <g:render template="form"/>
  </fieldset>
  <div class="form-actions">
    <g:if test="\${params.mode?.equals('select')}">
      <g:submitToRemote url="\${[action:'saveAjax']}" onSuccess="if (confirm_ajax_save('hidden-\${params.table_id?.encodeAsJavaScript()}',data)) select_ajax_modal('\${params.sender_id}','\${params.table_id?.encodeAsJavaScript()}','\${params.callback_url?.encodeAsJavaScript()}',data.id)" name="create" class="btn btn-primary" value="\${message(code: 'default.button.create.label', default: 'Create')}" />
      <button class="btn" type="reset" onclick="\$('#\${params.sender_id}').modal('hide')">Cancel</button>
    </g:if>
    <g:else>
      <g:submitButton name="create" class="btn btn-primary" value="\${message(code: 'default.button.create.label', default: 'Create')}" />
      <button class="btn" type="reset" onclick="window.location.href = '\${createLink(action: params.pc?params.pc:'show', id:params.id)}';">Cancel</button>
    </g:else>
  </div>
  </g:form>

</section>

</body>

</html>
