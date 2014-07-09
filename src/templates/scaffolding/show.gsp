<% import grails.persistence.Event %>
<%=packageName%>
<!doctype html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="kickstart"/>
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}"/>
  <title><g:message code="default.show.label" args="\${[message(code:"controller.\${params.controller}")]}"/></title>
</head>

<body>

<section id="show-${domainClass.propertyName}" class="first">

  <% excludedProps = Event.allEvents.toList() << 'id' << 'version'
  allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
  props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) }
  Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))

  int idx = 0;
  int extent = 0;

  props.eachWithIndex { p, propIndex ->
    if (p.oneToMany || p.manyToMany) {
      extent = 2;
      idx = 0;
    } else {
      if (idx == 0 && (propIndex == props.size() - 1 || props[propIndex + 1].oneToMany || props[propIndex + 1].manyToMany)) {
        extent = 2;
      } else {
        extent = 1;
      }
    }
  %>
  <% if (idx == 0) { %>
  <div class="row-fluid form-display">
    <% } %>
    <div class="${extent == 1 ? 'span6' : 'span12'} form-display-column1">
      <div class="${extent == 1 ? 'span4' : 'span2'} form-display-label"><g:message
              code="${domainClass.propertyName}.${p.name}.label"
              default="${p.naturalName}"/></div>

      <div class="${extent == 1 ? 'span8' : 'span10'}">
        <% if (p.isEnum()) { %>
        \${${propertyName}?.${p.name}?.encodeAsHTML()}
        <% } else if (p.oneToMany || p.manyToMany) { %>
        <tk:toManyDisplay list="\${${propertyName}.${p.name}}" var="${p.name[0]}" limit="100" listClass="to-many-display">
          <li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show"
                      id="\${${p.name[0]}.id}" params="\${['bc':\"\${params.bc?params.bc+'>':''}${className}:show:\${params.id}"]}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
        </tk:toManyDisplay>
        <% } else if (p.manyToOne || p.oneToOne) { %>
        <g:link controller="${p.referencedDomainClass?.propertyName}" action="show"
                id="\${${propertyName}?.${p.name}?.id}" params="\${['bc':\"\${params.bc?params.bc+'>':''}${className}:show:\${params.id}"]}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link>
        <% } else if (p.type == Boolean.class || p.type == boolean.class) { %>
        <bs:checkBox name="\${propertyName}" value="\${${propertyName}?.${p.name}}" readonly="true" disabled="true" />
        <% } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
        <g:formatDate date="\${${propertyName}?.${p.name}}"/>
        <% } else if (p.type == ([] as Byte[]).class || p.type == ([] as byte[]).class) { %>
        <img src="\${createLink(action: 'displayImage', id: ${propertyName}.id, params: [property:'${p.name}'])}" style="height:80px;"/>
        <% } else if (!p.type.isArray()) { %>
        \${fieldValue(bean: ${propertyName}, field: "${p.name}")}
        <% } %>
      </div>
    </div>
    <% if (idx + extent == 2) { %>
  </div>
  <% }
    idx = (idx + extent) % 2;
  } %>
</section>

</body>

</html>
