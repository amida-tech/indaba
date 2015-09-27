<%-- 
    Document   : messageDetails
    Created on : 2010-4-12, 11:33:21
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<un:useConstants var="Constants" className="com.ocs.indaba.common.Constants"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.messageDetail.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <style type="text/css">
            .item {
                padding-left: 70px;
                position: relative;
                width: 800px;
            }
            .item label {
                float:left;
                width:100px;
                font-weight: bold;
                left: 0;
                text-align: left;
            }
        </style>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="messaging" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div id="msg-detail-wrapper" class="wrapper message">
                <div class="actions">
                    <span style="float:left; font-size:14px">
                        <a href="${contextPath}/replymsg.do?msgId=${message.id}&sendType=${msgType}">
                            <c:if test="${message.author.userId > 0}">
                                <c:choose>
                                    <c:when test="${msgType == Constants.MESSAGE_SEND_TYPE_REPLY || msgType == Constants.MESSAGE_SEND_TYPE_NEW}"><img src="images/reply_icon.png" style="vertical-align:middle" alt="" />&nbsp;<b><indaba:msg key='jsp.messageDetail.reply' /></b></c:when>
                                    <c:when test="${msgType == Constants.MESSAGE_SEND_TYPE_FORWARD}"><img src="images/forward.png" style="vertical-align:middle" alt="" />&nbsp;<b><indaba:msg key='common.btn.forward' /></b></c:when>
                                    <c:otherwise><img src="images/reply_icon.png" style="vertical-align:middle" alt="" />&nbsp;<b><indaba:msg key='jsp.messageDetail.reply' /></b></c:otherwise>
                                </c:choose>
                            </c:if>
                        </a>
                    </span>
                    <span style="float:right">
                        <a href="${contextPath}/messages.do"><img src="images/mailbox.png" alt="Back To List" /></a>
                        <c:if test="${prevMsgId ne null}"><a href="${contextPath}/messagedetail.do?msgId=${prevMsgId}&amp;boxType=${boxType}"><img src="images/icon_arrow_left.png" alt="< Prev" /></a></c:if>
                        <c:if test="${nextMsgId ne null}"><a href="${contextPath}/messagedetail.do?msgId=${nextMsgId}&amp;boxType=${boxType}"><img src="images/icon_arrow_right.png" alt="Next >" /></a></c:if>
                    </span>
                </div>
                <br/><br/>
                <hr/>
                <div class="messageBody">
                    <div class="item">
                        <label><indaba:msg key='common.msg.to' />:</label>
                        <a href="${contextPath}/profile.do?targetUid=${receiver.id}">${receiver.firstName}&nbsp;${receiver.lastName}</a>
                    </div>
                    <div class="item">
                        <label><indaba:msg key='common.title.from' />:</label>
                        <c:choose>
                            <c:when test="${message.author.permission eq 0}">${message.author.displayUsername}</c:when>
                            <c:when test="${message.author.permission eq 1}">
                                <a href="${contextPath}/profile.do?targetUid=${message.author.userId}">${message.author.displayUsername}</a>
                            </c:when>
                            <c:when test="${message.author.permission eq 2}">
                                <a href="${contextPath}/profile.do?targetUid=${message.author.userId}">${message.author.displayUsername}</a>
                            </c:when>
                            <c:when test="${message.author.permission eq 3}">
                                <a href="${contextPath}/profile.do?targetUid=${message.author.userId}">${message.author.displayUsername}</a>
                            </c:when>
                        </c:choose>
                        &nbsp;&nbsp;<fmt:formatDate value="${message.createdTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </div>
                    <div class="item">
                        <label><indaba:msg key='jsp.messageDetail.title' />: </label>
                        ${message.title}
                    </div>
                    <div class="item">
                        <label><indaba:msg key='common.label.message' />: </label>
                        <div class="messageText">
                            <pre>${message.body}</pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
