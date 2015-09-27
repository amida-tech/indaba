<%-- 
    Document   : target-filter-inc
    Created on : 2012-6-11, 14:57:20
    Author     : Jeff Jiang
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div class="filter">
    <form id="target-filter-form" class="jqtransform target-filter-form" action="#" onsubmit="$('#target-flexgrid').flexReload({newp: 1,dataType: 'json'});return false" method="POST">
        <fieldset>
            <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.target_filter"/></span></legend>
            <input type="hidden" name="visibility" id="visibility" value="${visibility}"/>
             <div class="field">
                <label><indaba:msg key="cp.label.tags"/></label><input name="userTag" type="text" style="width: 180px;" />
            </div>
            <div id="organization" class="field">
                <label><indaba:msg key="cp.label.organization"/></label>
                <select name="organization" style="width: 200px; height: 15px;"class="pretty dk">
                    <option value="0"><indaba:msg key="cp.text.all"/></option>
                    <c:forEach items="${orgs}" var="org">
                        <option value="${org.id}">${org.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <a id="submitFilter" class="button">&nbsp;&nbsp;&nbsp;<indaba:msg key="cp.btn.find"/>&nbsp;&nbsp;&nbsp;</a>
                <a id="resetFilter" class="button reset"><indaba:msg key="cp.btn.reset"/></a>
            </div>
        </fieldset>
    </form>
</div>
