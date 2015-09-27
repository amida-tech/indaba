<%@ page pageEncoding="UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<span>
	<ul>
		<c:forEach items="${userList}" var="user">
                    <c:if test="${user.permission gt 0 && 
                                  (fn:trim(varName) == '' || fn:indexOf(fn:toLowerCase(user.displayUsername), fn:toLowerCase(varName)) > -1)}">
                        <li class="user" id="${user.userId}" inputName="receiverIds">${user.displayUsername}</li>
                    </c:if>
		</c:forEach>
                <c:forEach items="${teamList}" var="team">
                    <c:if test="${(fn:trim(varName) == '' || fn:indexOf(fn:toLowerCase(team.teamName), fn:toLowerCase(varName)) > -1)}">
                        <li class="team" id="${team.id}">${team.teamName}</li>
                    </c:if>
		</c:forEach>
                <c:forEach items="${roleList}" var="role">
                    <c:if test="${(fn:trim(varName) == '' || fn:indexOf(fn:toLowerCase(role.name), fn:toLowerCase(varName)) > -1)}">
                        <li class="role" id="${role.id}">${role.name}</li>
                    </c:if>
		</c:forEach>
	</ul>
</span>