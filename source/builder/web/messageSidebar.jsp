<%--
    Document   : messageSidebar
    Created on : 2010-4-12, 11:33:21
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div class="box" id="your_inbox">
    <h3><a href="${contextPath}/messages.do?msgType=1"><img class="icon" src="images/inbox.png"/>&nbsp;<indaba:msg key='common.msg.yourinbox' /></a></h3>
    <div class="content">
        <dl>
            <c:forEach items="${inboxMessages.thisPageElements}" var="msg">
                <dt class="message <c:if test="${not msg.readStatus}">new</c:if>">
                <a href="<%=request.getContextPath()%>/messagedetail.do?msgId=${msg.id}&msgType=1">
                    ${msg.title}
                </a>
                </dt>
                <dd class="message">
                    <fmt:formatDate value="${msg.createdTime}" pattern="yyyy-MM-dd"/>
                    &nbsp;<indaba:msg key='jsp.messageSidebar.from' />
                    <c:choose>
                        <c:when test="${msg.author.permission eq 0}">${msg.author.displayUsername}</c:when>
                        <c:when test="${msg.author.permission eq 1}">
                        <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                    </c:when>
                    <c:when test="${msg.author.permission eq 2}">
                        <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                    </c:when>
                    <c:when test="${msg.author.permission eq 3}">
                        <a href="<%=request.getContextPath() %>/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                    </c:when>
                </c:choose>
                </dd>
            </c:forEach>
            <c:if test="${inboxMsgCount gt 5}">
                <a href="${contextPath}/messages.do"><indaba:msg key='jsp.messageSidebar.more' /></a>
            </c:if>
        </dl>
    </div>
</div>

<indaba:view prjid="${prjid}" uid="${uid}" right="read project wall">
    <div class="box" id="project_wall">
        <h3><a href="${contextPath}/messages.do?msgType=3"><img class="icon" src="images/project.png"/>&nbsp;<indaba:msg key='common.msg.prjupdates' /></a></h3>
        <div class="content">
            <dl>
                <c:forEach items="${projectMessages.thisPageElements}" var="msg">
                    <dt class="message
                        <c:if test="${not msg.readStatus}"> <indaba:msg key='jsp.messageSidebar.new' /></c:if>
                            ">
                        <a href="<%=request.getContextPath()%>/messagedetail.do?msgId=${msg.id}&msgType=3">
                        ${msg.title}
                        <%-- <indaba:abbreviate length="56" value="${msg.title}"/> --%>
                    </a>
                    </dt>
                    <dd class="message">
                        <fmt:formatDate value="${msg.createdTime}" pattern="yyyy-MM-dd"/>
                        &nbsp;<indaba:msg key='jsp.messageSidebar.from' />
                        <c:choose>
                            <c:when test="${msg.author.permission eq 0}">${msg.author.displayUsername}</c:when>
                            <c:when test="${msg.author.permission eq 1}">
                            <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                        </c:when>
                        <c:when test="${msg.author.permission eq 2}">
                            <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                        </c:when>
                        <c:when test="${msg.author.permission eq 3}">
                            <a href="<%=request.getContextPath() %>/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                        </c:when>
                    </c:choose>
                    </dd>
                </c:forEach>
                <c:if test="${projMsgCount gt 5}">
                    <a href="${contextPath}/messages.do"><indaba:msg key='jsp.messageSidebar.more' /></a>
                </c:if>
            </dl>
        </div>
    </div>
</indaba:view>
