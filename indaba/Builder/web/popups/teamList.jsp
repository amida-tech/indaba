<%@ page pageEncoding="UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<span>
    <ul>
        <c:forEach items="${teamList}" var="team">
            <c:if test="${(fn:trim(varName) == '' || fn:indexOf(fn:toLowerCase(team.teamName), fn:toLowerCase(varName)) > -1)}">
                <li class="team" id="${team.id}" inputName="teamIds">${team.teamName}</li>
            </c:if>
        </c:forEach>
    </ul>
</span>