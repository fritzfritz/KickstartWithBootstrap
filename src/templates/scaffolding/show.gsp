<% import grails.persistence.Event %>
<%=packageName%>
<!doctype html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="kickstart"/>
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}"/>
  <title><g:message code="default.show.label" args="[entityName]"/></title>
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
    if (p.oneToMany() || p.manyToMany) {
      extent = 2;
      idx = 0;
    } else {
      if (idx == 0 && (propIndex == props.size() - 1 || props[propIndex + 1].oneToMany() || props[propIndex + 1].manyToMany())) {
        extent = 2;
      } else {
        extent == 1;
      }
    }
  %>
  <g:if test="${idx == 0}">
    <div class="row-fluid form-display">
  </g:if>
  <div class="${extent == 1 ? 'span6' : 'span12'} form-display-column1">
    <div class="${extent == 1 ? 'span2' : 'span4'} form-display-label"><g:message
            code="${domainClass.propertyName}.${p.name}.label"
            default="${p.naturalName}"/></div>

    <div class="${extent == 1 ? 'span10' : 'span8'}">
      <% if (p.isEnum()) { %>
      \${${propertyName}?.${p.name}?.encodeAsHTML()}
      <% } else if (p.oneToMany || p.manyToMany) { %>
        <g:tomanydisplay list="\${${propertyName}.${p.name}}" var="${p.name[0]}" limit="0" listClass="to-many-display">
          <li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show"
                      id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
        </g:tomanydisplay>
      <% } else if (p.manyToOne || p.oneToOne) { %>
      <g:link controller="${p.referencedDomainClass?.propertyName}" action="show"
              id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link>
      <% } else if (p.type == Boolean.class || p.type == boolean.class) { %>
      <g:formatBoolean boolean="\${${propertyName}?.${p.name}}"/>
      <% } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
      <g:formatDate date="\${${propertyName}?.${p.name}}"/>
      <% } else if (!p.type.isArray()) { %>
      \${fieldValue(bean: ${propertyName}, field: "${p.name}")}
      <% } %>
    </div>
  </div>
  <g:if test="${idx + extent == 2}">
    </div>
  </g:if>
  <% idx = (idx + extent) % 2;
  } %>
</section>

</body>

</html>
