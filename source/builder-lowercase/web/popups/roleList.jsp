<%@ page pageEncoding="UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<span>
    <ul>
        <c:forEach items="${roleList}" var="role">
            <c:if test="${(fn:trim(varName) == '' || fn:indexOf(fn:toLowerCase(role.name), fn:toLowerCase(varName)) > -1)}">
                <li class="role" id="${role.id}" inputName="roleIds">${role.name}</li>
            </c:if>
        </c:forEach>
    </ul>
</span>