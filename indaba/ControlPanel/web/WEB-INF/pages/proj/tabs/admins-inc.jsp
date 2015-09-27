<%--
    Document   : Project Tab - 'Administrators'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="projectAdminsTab">
    <form id="projectAdminsForm" action="#" method="POST">  <fieldset class="block">
            <input type="hidden" name="projId" id="projId" value="${projId}"/>
            <input type="hidden" name="updateField"  value="secondaryAdmins"/>
            <dl>
                <dt><label for="secondaryAdmins"><indaba:msg key="cp.label.secondary_admin"  /></label></dt>
                <dd>
                    <select id="secondaryAdmins" name="secondaryAdmins" id="secondaryAdmins" multiple data-placeholder="<indaba:msg key='cp.text.choose_secondary_admin'  />" class="long-input">
                        <option value=""></option>
                        <c:forEach items="${orgAdmins}" var="oa">
                            <option value="${oa.id}">${oa.firstName} ${oa.lastName}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${(not isSA) && (not isPA)}">
                        <div class="viewonly-note"><span>You can view it ONLY. Please contact the system admin to edit it if necessary.</span></div>
                    </c:if>
                </dd>
            </dl>
        </fieldset>
    </form>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        var adminsForm =  $('#projectAdminsForm');
        if($('#result', adminsForm).length == 0) {
            doAjaxChange($('#secondaryAdmins', adminsForm), '${contextPath}/proj/projects!updateData');
        }
    });
</script>