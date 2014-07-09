<% import grails.persistence.Event %>
<%=packageName%>
<table id="${domainClass.propertyName}-table" class="table table-bordered first">
  <thead>
  <tr>
    <% excludedProps = Event.allEvents.toList() << 'id' << 'version'
    allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
    props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && !Collection.isAssignableFrom(it.type) }
    Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
    props.eachWithIndex { p, i ->
      if (i < 6) {
        if (p.isAssociation()) { %>
    <th><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}"/></th>
    <% } else { %>
    <g:sortableColumn property="${p.name}"
                      title="\${message(code: '${domainClass.propertyName}.${p.name}.label', default: '${p.naturalName}')}"/>
    <% }
    }
    } %>
    <th><g:message code="general.actions"/></th>
  </tr>
  </thead>
  <tbody>
  </tbody>
</table>
<r:script>
\$(document).ready(function () {
      \$('#${domainClass.propertyName}-table').dataTable( {
        "aoColumnDefs": [{ "sWidth": "120px", "aTargets": [-1] }],
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": "\${createLink(action:params.ajaxSource)}\${params.ajaxParams ? '?'+params.ajaxParams :''}",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "props","value": "${props[0..Math.min(props.size() - 1, 5)]*.name.join(',')},_actions"} );
            aoData.push( { "name": "mode","value": "\${params.mode}"} );
            aoData.push( { "name": "table_id","value": "<%= '<'+'%=params.table_id?.encodeAsJavaScript()%'+'>'%>"} );
            aoData.push( { "name": "sender_id","value": "\${params.sender_id}"} );
            aoData.push( { "name": "callback_url","value": "\${params.callback_url}"} );
        }
      } );
    })
</r:script>
