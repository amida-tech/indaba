<%-- 
    Document   : indicator-filter.jsp
    Created on : 2012-5-28, 16:56:12
    Author     : Jeff Jiang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<un:useConstants var="Constants" className="com.ocs.indaba.controlpanel.common.ControlPanelConstants"/>
<div class="filter">
    <form id="notification_filter-form" class="jqtransform notification-filter-form" action="#" onsubmit="$('#notifications-grid').flexReload({newp: 1,dataType: 'json'});return false;" method="POST">
        <fieldset>
            <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.notification_filter"/></span></legend>
            <div class="field">
                <label><indaba:msg key="cp.label.language"/></label>
                <select id="selectlanguage" name="selectlanguage" style="width:200px; height: 25px;"  class="pretty dk" >
                    <option value="0"><indaba:msg key="cp.text.all"  /></option>
                    <c:forEach var="lang" items="${languages}">
                        <option value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label><indaba:msg key="cp.label.type"/></label>
                <select id="selecttype" name="selecttype" style="width: 180px; height: 25px;"  class="pretty dk" >
                    <option value="0"><indaba:msg key="cp.text.all"  /></option>
                    <c:forEach var="ntype" items="${notificationtypes}">
                        <option value="${ntype.id}">${ntype.name}</option>
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