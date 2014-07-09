<%=packageName%>
<% import grails.persistence.Event %>

<% excludedProps = Event.allEvents.toList() << 'version' << 'dateCreated' << 'lastUpdated'
persistentPropNames = domainClass.persistentProperties*.name
boolean hasHibernate = pluginManager?.hasGrailsPlugin('hibernate')
if (hasHibernate && org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainBinder.getMapping(domainClass)?.identity?.generator == 'assigned') {
  persistentPropNames << domainClass.identifier.name
}
props = domainClass.properties.findAll { persistentPropNames.contains(it.name) && !excludedProps.contains(it.name) }
Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))

int idx = 0;
int extent = 0;

int pidx = 0;
for (p in props) {
  if (p.embedded) {
    def embeddedPropNames = p.component.persistentProperties*.name
    def embeddedProps = p.component.properties.findAll { embeddedPropNames.contains(it.name) && !excludedProps.contains(it.name) }
    Collections.sort(embeddedProps, comparator.constructors[0].newInstance([p.component] as Object[]))
%><fieldset class="embedded"><legend><g:message code="${domainClass.propertyName}.${p.name}.label"
                                                default="${p.naturalName}"/></legend><%
    int propIndex = 0
    for (ep in p.component.properties) {
      if (isWide(ep,p.component)) {
        extent = 2;
        idx = 0;
      } else {
        if (idx == 0 && (propIndex == p.component.properties.size() - 1 || isWide(p.component.properties[propIndex + 1],p.component))) {
          extent = 2;
        } else {
          extent = 1;
        }
      }
      renderFieldForProperty(ep, p.component, "${p.name}.", idx, extent)
      propIndex++
    }
%></fieldset><%
    } else {
      if (isWide(p,domainClass)) {
        extent = 2;
        idx = 0;
      } else {
        if (idx == 0 && (pidx == props.size() - 1 || isWide(props[pidx + 1],domainClass))) {
          extent = 2;
        } else {
          extent = 1;
        }
      }
      if (renderFieldForProperty(p, domainClass, idx, extent)) {
        idx = (idx + extent) % 2
      }

    }
    pidx++
  }

  private boolean isWide(p, owningClass) {
    def cp = owningClass.constrainedProperties[p.name]
    return p.oneToMany || p.manyToMany || ("textarea" == cp.widget || (cp.maxSize > 250 && !cp.password && !cp.inList))
  }

  private boolean renderFieldForProperty(p, owningClass, prefix = "", idx, extent) {
    boolean hasHibernate = pluginManager?.hasGrailsPlugin('hibernate')
    boolean display = true
    boolean required = false
    if (hasHibernate) {
      cp = owningClass.constrainedProperties[p.name]
      display = (cp ? cp.display : true) && (cp && cp.hasProperty('bindable') ? cp.bindable : true)
      required = (cp ? !(cp.propertyType in [boolean, Boolean]) && !cp.nullable && (cp.propertyType != String || !cp.blank) : false)
    }
    if (display) {
      if (idx == 0) { %>
<div class="row-fluid form-display">
  <% } %>
  <div class="${extent == 1 ? 'span6' : 'span12'} form-display-column1">
    <div class="${extent == 1 ? 'span4' : 'span2'} form-display-label control-group">
      <label for="${prefix}${p.name}"><g:message
              code="${domainClass.propertyName}.${prefix}${p.name}.label"
              default="${p.naturalName}"/><% if (required) { %><span class="required-indicator">*</span><% } %>
      </label></div>

    <div class="${extent == 1 ? 'span8' : 'span10'}">
        ${renderEditor(p)}
        <span class="help-inline">\${hasErrors(bean: ${propertyName}, field: '${p.name}', 'error')}</span>
    </div>
  </div>
  <% if (idx + extent == 2) { %>
</div>
<% }
  return true
}
  return false
} %>
