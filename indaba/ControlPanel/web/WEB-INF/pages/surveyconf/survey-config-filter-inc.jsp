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
    <form id="surveyConfig-filter-form" class="surveyconfig-filter-form" action="${contextPath}/surveyconf/survey-config!find" method="POST">
        <fieldset>
            <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.survey_config_filter"  /></span></legend>
                <%--
                <dl>
                    <dt><label><indaba:msg key="cp.label.visibility"/></label></dt>
                    <dd>
                        <select name="visibility" id="visibility" style="width: 200px; height: 15px;" class="pretty dk" data-placeholder="<indaba:msg key='cp.text.choose_organization'/>">
                            <option value="0" selected><indaba:msg key="cp.text.all" /></option>
                            <option value="1"><indaba:msg key="cp.label.public" /></option>
                            <option value="2"><indaba:msg key="cp.label.private" /></option>
                        </select>
                    </dd>
                </dl>
                --%>
            <input type="hidden" name="visibility" id="visibility" value="${visibility}"/>
            <dl>
                <dt><label><indaba:msg key="cp.label.organization"  /></label></dt>
                <dd>
                    <%--
                    <select name="filterOrgIds" id="filterOrgIds" style="width: 250px; height: 15px;" class="pretty dk" >
                <option value="-1"><indaba:msg key="cp.text.all"  /></option>
                <c:forEach items="${orgs}" var="org">
                    <option value="${org.id}">${org.name}</option>
                </c:forEach>
                </select>
                    --%>
                    <select name="filterOrgIds" id="filterOrgIds" style="width: 200px; height: 15px;" class="pretty dk" onchange="$('#surveyConfig-flexgrid').flexReload({newp: 1, dataType: 'json'});">
                        <option value="-1"><indaba:msg key="cp.text.all"  /></option>
                        <c:forEach items="${orgs}" var="org">
                            <option value="${org.id}">${org.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
        </fieldset>
    </form>
</div>

