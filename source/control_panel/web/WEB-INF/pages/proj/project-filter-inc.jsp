<%-- 
    Document   : project-filter-inc
    Created on : 2012-6-11, 14:57:20
    Author     : Jeff Jiang
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div class="filter">
    <form id="project-filter-form" class="jqtransform project-filter-form" action="${contextPath}/lib/project-lib!find" method="POST">
        <fieldset>
            <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.project_filter"  /></span></legend>
            <input type="hidden" name="visibility" id="visibility" value="${visibility}"/>
            <!--
            <div class="field">
                <label>Tag</label><input name="tag" type="text" style="width: 200px;" />
            </div>
            -->
            <div id="organization" class="field">
                <label><indaba:msg key="cp.label.organization"  /></label>
                <select name="filterOrgId" id="filterOrgId" style="width: 200px; height: 15px;" class="pretty dk" onchange="$('#project-flexgrid').flexReload({dataType: 'json'});">
                    <option value="0"><indaba:msg key="cp.text.all"  /></option>
                    <c:forEach items="${orgs}" var="org">
                        <option value="${org.id}" <c:if test="${filterOrgId == org.id}">selected</c:if>>${org.name}</option>
                    </c:forEach>
                </select>
            </div>
        </fieldset>
    </form>
</div>

