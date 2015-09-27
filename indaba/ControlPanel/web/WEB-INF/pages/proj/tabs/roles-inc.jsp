<%--
    Document   : Project Tab - 'Roles'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="projectRolesTab">
    <form id="projectRolesForm" action="#" method="POST">  <fieldset class="block">
            <input type="hidden" name="projId" id="projId" value="${projId}"/>
            <input type="hidden" name="updateField"  value="roles"/>
            <dl>
                <dt><label for="roles"><indaba:msg key="cp.label.roles"  /></label></dt>
                <dd>
                    <select name="roles" id="roles" multiple data-placeholder="<indaba:msg key='cp.text.add_roles'  />" class="long-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${roles}" var="role">
                            <option value="${role.id}">${role.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
        </fieldset>
    </form>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        var rolesForm = $('#projectRolesTab');
        if($('#result', rolesForm).length == 0) {
            doAjaxChange($('#roles', rolesForm), '${contextPath}/proj/projects!updateData');
        }
    });
</script>