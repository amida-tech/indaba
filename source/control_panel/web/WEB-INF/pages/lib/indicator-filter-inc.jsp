<%-- 
    Document   : indicator-filter.jsp
    Created on : 2012-5-28, 16:56:12
    Author     : Jeff Jiang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<un:useConstants var="Constants" className="com.ocs.indaba.controlpanel.common.ControlPanelConstants"/>
<div class="filter">
    <form id="${param.boxId}" class="jqtransform indicator-filter-form" action="#" onsubmit="$('#indicator-flexgrid').flexReload({newp: 1,dataType: 'json'});return false;" method="POST">
        <fieldset>
            <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.indicator_filter"/></span></legend>
            <div class="field">
                <label><indaba:msg key="cp.label.user_tags"/></label><input name="userTag" type="text" style="width: 180px;" />
                <c:choose>
                    <c:when test="${visibility == Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED}">
                        <c:set var="state" value="${Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED}" />
                    </c:when>
                    <c:when test="${visibility == Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED}">
                        <c:set var="state" value="${Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED}" />
                    </c:when>
                    <c:when test="${visibility == Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_TEST}">
                        <c:set var="state" value="${Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_TEST}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="state" value="-1" />
                    </c:otherwise>
                </c:choose>
                <input id="state" type="hidden" name="state" value="${state}" />
                <input id="lib-type" type="hidden" name="visibility" value="${visibility}" />
            </div>
            <div class="field">
                <label><indaba:msg key="cp.label.indicator_tags"/></label>
                <select id="indicatorTag" name="indicatorTag" style="width: 180px;">
                    <option value="0">All</option>
                    <c:forEach items="${itags}" var="tag">
                        <option value="${tag.id}">${tag.term}</option>
                    </c:forEach>
                </select>
            </div>
            <div id="organization" class="field">
                <label><indaba:msg key="cp.label.organizations"/></label>
                <select name="organization" style="width: 180px;">
                    <option value="0">All</option>
                    <c:forEach items="${orgs}" var="org">
                        <option value="${org.id}">${org.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div style="text-align: right;width: 200px; float: right;">
                <a id="submitFilter" class="button"><indaba:msg key="cp.btn.find"/></a>
                <a id="resetFilter" class="button reset"><indaba:msg key="cp.btn.reset"/></a>
            </div>
        </fieldset>
    </form>
</div>