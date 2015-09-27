<%-- 
    Document   : messageList
    Created on : 2010-4-12, 11:33:21
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.messages.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
    </head>
    <body>
        <div id="indaba">
            <!--c:set var="active" value="messaging" scope="request"/-->
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper message">
                <a id="write-new-message" href="${contextPath}/newmsg.do"><indaba:msg key='jsp.messages.createNewMsg' /></a>
                <div class="box" id="inbox">
                    <h3><img class="icon" src="images/inbox.png"/>&nbsp;<span><indaba:msg key='common.msg.yourinbox' />&nbsp;(${inboxNewMsgCount} <indaba:msg key='jsp.messages.newCount' /> / ${inboxMsgCount} <indaba:msg key='jsp.messages.total' />)</span><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                    <div class="content">
                        <table id="msg-inbox-table">
                            <thead>
                                <tr char="thead">
                                    <th><indaba:msg key='jsp.messages.from' /></th>
                                    <th><indaba:msg key='common.label.title' /></th>
                                    <th><indaba:msg key='jsp.messages.date' /></th>
                                    <th><indaba:msg key='common.label.status' /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${inboxMessages.thisPageElements}" var="msg">
                                    <tr <c:if test="${not msg.readStatus}">class="new"</c:if>>
                                            <td class="sender">
                                            <c:choose>
                                                <c:when test="${msg.author.permission eq 0}">${msg.author.displayUsername}</c:when>
                                                <c:when test="${msg.author.permission eq 1}">
                                                    <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                                                </c:when>
                                                <c:when test="${msg.author.permission eq 2}">
                                                    <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                                                </c:when>
                                                <c:when test="${msg.author.permission eq 3}">
                                                    <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="messagedetail.do?msgId=${msg.id}&msgType=1&boxType=1"><indaba:abbreviate length="80" value="${msg.title}"/></a>
                                        </td>
                                        <td class="timestamp">
                                            <fmt:formatDate value="${msg.createdTime}" pattern="yyyy-MM-dd HH:mm"/>
                                        </td>
                                        <td class="status">
                                            <c:if test="${not msg.readStatus}">
                                                <indaba:msg key='jsp.messages.new' />
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>                              
                            </tbody>
                        </table>
                        <hr/>
                        <div class="msg-navigate-div">
                            <c:if test="${not inboxMessages.firstPage}"><a id="previous-inbox" class="msg-prev-page" href="${contextPath}/messages.do?inpn=${inboxMessages.pageNumber - 1}&inps=${inboxMessages.pageSize}&inpn=${inboxMessages.pageNumber}&inps=${inboxMessages.pageSize}&msgType=1" title="previous page"></a></c:if>
                                <indaba:msg key='jsp.messages.rows' />:
                                <span>
                                <c:choose>
                                    <c:when test="${inboxMessages.pageSize eq 10}">10</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?inpn=1&inps=10&inpn=${inboxMessages.pageNumber}&inps=${inboxMessages.pageSize}&msgType=1">10</a></c:otherwise>
                                </c:choose>
                            </span> -
                            <span>
                                <c:choose>
                                    <c:when test="${inboxMessages.pageSize eq 25}">25</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?inpn=1&inps=25&inpn=${inboxMessages.pageNumber}&inps=${inboxMessages.pageSize}&msgType=1">25</a></c:otherwise>
                                </c:choose>
                            </span> -
                            <span>
                                <c:choose>
                                    <c:when test="${inboxMessages.pageSize eq 50}">50</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?inpn=1&inps=50&inpn=${inboxMessages.pageNumber}&inps=${inboxMessages.pageSize}&msgType=1">50</a></c:otherwise>
                                </c:choose>
                            </span> -
                            <span>
                                <c:choose>
                                    <c:when test="${inboxMessages.pageSize eq 100}">100</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?inpn=1&inps=100&inpn=${inboxMessages.pageNumber}&inps=${inboxMessages.pageSize}&msgType=1">100</a></c:otherwise>
                                </c:choose>
                            </span>
                            <c:if test="${not inboxMessages.lastPage}"><a id="next-inbox" class="msg-next-page" href="${contextPath}/messages.do?inpn=${inboxMessages.pageNumber + 1}&inps=${inboxMessages.pageSize}&inpn=${inboxMessages.pageNumber}&inps=${inboxMessages.pageSize}&msgType=1" title="next page"></a></c:if>
                            </div>
                        </div>
                    </div>

                    <div class="box" id="outbox">
                        <h3><img class="icon" src="images/outbox.png"/>&nbsp;<span><indaba:msg key='common.msg.youroutbox' />&nbsp;(${outboxNewMsgCount} <indaba:msg key='jsp.messages.newCount' /> / ${outboxMsgCount} <indaba:msg key='jsp.messages.total' />)</span><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                    <div class="content">
                        <table id="msg-inbox-table">
                            <thead>
                                <tr char="thead">
                                    <th><span style="text-transform: uppercase"><indaba:msg key='common.msg.to' /></span></th>
                                    <th><indaba:msg key='common.label.title' /></th>
                                    <th><indaba:msg key='jsp.messages.date' /></th>
                                    <th><indaba:msg key='common.label.status' /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${outboxMessages.thisPageElements}" var="msg">
                                    <tr <c:if test="${not msg.readStatus}">class="new"</c:if>>
                                            <td class="sender">
                                            <c:choose>
                                                <c:when test="${msg.isToUser}">
                                                    <c:choose>
                                                        <c:when test="${msg.toUser.permission eq 0}">${msg.toUser.displayUsername}</c:when>
                                                        <c:when test="${msg.toUser.permission eq 1}">
                                                            <a href="${contextPath}/profile.do?targetUid=${msg.toUser.userId}">${msg.toUser.displayUsername}</a>
                                                        </c:when>
                                                        <c:when test="${msg.toUser.permission eq 2}">
                                                            <a href="${contextPath}/profile.do?targetUid=${msg.toUser.userId}">${msg.toUser.displayUsername}</a>
                                                        </c:when>
                                                        <c:when test="${msg.toUser.permission eq 3}">
                                                            <a href="<%=request.getContextPath() %>/profile.do?targetUid=${msg.toUser.userId}">${msg.toUser.displayUsername}</a>
                                                        </c:when>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <span>${msg.toProject.codeName}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="messagedetail.do?msgId=${msg.id}&msgType=3&boxType=2"><indaba:abbreviate length="80" value="${msg.title}"/></a>
                                        </td>
                                        <td class="timestamp">
                                            <fmt:formatDate value="${msg.createdTime}" pattern="yyyy-MM-dd HH:mm"/>
                                        </td>
                                        <td class="status">
                                            <c:if test="${not msg.readStatus}">
                                                <indaba:msg key='jsp.messages.new' />
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>                              
                            </tbody>
                        </table>
                        <hr/>
                        <div class="msg-navigate-div">
                            <c:if test="${not outboxMessages.firstPage}"><a id="previous-inbox" class="msg-prev-page" href="${contextPath}/messages.do?inpn=${outboxMessages.pageNumber - 1}&inps=${outboxMessages.pageSize}&outpn=${inboxMessages.pageNumber}&outps=${inboxMessages.pageSize}&msgType=2" title="previous page"></a></c:if>
                                <indaba:msg key='jsp.messages.rows' />:
                                <span>
                                <c:choose>
                                    <c:when test="${outboxMessages.pageSize eq 10}">10</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?outpn=1&outps=10&outpn=${outboxMessages.pageNumber}&outps=${outboxMessages.pageSize}&msgType=2">10</a></c:otherwise>
                                </c:choose>
                            </span> -
                            <span>
                                <c:choose>
                                    <c:when test="${outboxMessages.pageSize eq 25}">25</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?outpn=1&outps=25&outpn=${outboxMessages.pageNumber}&outps=${outboxMessages.pageSize}&msgType=2">25</a></c:otherwise>
                                </c:choose>
                            </span> -
                            <span>
                                <c:choose>
                                    <c:when test="${outboxMessages.pageSize eq 50}">50</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?outpn=1&outps=50&outpn=${outboxMessages.pageNumber}&outps=${outboxMessages.pageSize}&msgType=2">50</a></c:otherwise>
                                </c:choose>
                            </span> -
                            <span>
                                <c:choose>
                                    <c:when test="${outboxMessages.pageSize eq 100}">100</c:when>
                                    <c:otherwise><a href="${contextPath}/messages.do?outpn=1&outps=100&outpn=${outboxMessages.pageNumber}&outps=${outboxMessages.pageSize}&msgType=2">100</a></c:otherwise>
                                </c:choose>
                            </span>
                            <c:if test="${not outboxMessages.lastPage}"><a id="next-outbox" class="msg-next-page" href="${contextPath}/messages.do?outpn=${outboxMessages.pageNumber + 1}&outps=${outboxMessages.pageSize}&outpn=${outboxMessages.pageNumber}&outps=${outboxMessages.pageSize}&msgType=2" title="next page"></a></c:if>
                            </div>
                        </div>
                    </div>

                    <indaba:view prjid="${prjid}" uid="${uid}" right="read project wall">
                    <div class="box" id="projmsg">
                        <h3><img class="icon" src="images/project.png"/>&nbsp;<span><indaba:msg key='common.msg.prjupdates' />&nbsp;(${projNewMsgCount} <indaba:msg key='jsp.messages.newCount' /> / ${projMsgCount} <indaba:msg key='jsp.messages.total' />)</span><a class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                        <div class="content">
                            <table id="msg-project-table">
                                <thead>
                                    <tr class="thead">
                                        <th><indaba:msg key='jsp.messages.from' /></th>
                                        <th><indaba:msg key='common.label.title' /></th>
                                        <th><indaba:msg key='jsp.messages.date' /></th>
                                        <th><indaba:msg key='common.label.status' /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${projectMessages.thisPageElements}" var="msg">
                                        <tr <c:if test="${not msg.readStatus}">class="new"</c:if>>
                                                <td class="sender">
                                                <c:choose>
                                                    <c:when test="${msg.author.permission eq 0}">
                                                        ${msg.author.displayUsername}
                                                    </c:when>
                                                    <c:when test="${msg.author.permission eq 1}">
                                                        <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                                                    </c:when>
                                                    <c:when test="${msg.author.permission eq 2}">
                                                        <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                                                    </c:when>
                                                    <c:when test="${msg.author.permission eq 3}">
                                                        <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="messagedetail.do?msgId=${msg.id}&msgType=3&boxType=1"><indaba:abbreviate length="80" value="${msg.title}"/></a>
                                            </td>
                                            <td class="timestamp">
                                                <fmt:formatDate value="${msg.createdTime}" pattern="yyyy-MM-dd HH:mm"/>
                                            </td>
                                            <td class="status">
                                                <c:if test="${not msg.readStatus}">
                                                    <indaba:msg key='jsp.messages.new' />
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <hr/>
                            <div class="msg-navigate-div">
                                <c:if test="${not projectMessages.firstPage}"><a id="previous-project" class="msg-prev-page" href="${contextPath}/messages.do?pupn=${projectMessages.pageNumber - 1}&pups=${projectMessages.pageSize}&pupn=${projectMessages.pageNumber}&pups=${projectMessages.pageSize}&msgType=3" title="previous page"></a></c:if>
                                    <indaba:msg key='jsp.messages.rows' />:
                                    <span>
                                    <c:choose>
                                        <c:when test="${projectMessages.pageSize eq 10}">10</c:when>
                                        <c:otherwise><a href="${contextPath}/messages.do?pupn=1&pups=10&pupn=${projectMessages.pageNumber}&pups=${projectMessages.pageSize}&msgType=3">10</a></c:otherwise>
                                    </c:choose>
                                </span> -
                                <span>
                                    <c:choose>
                                        <c:when test="${projectMessages.pageSize eq 25}">25</c:when>
                                        <c:otherwise><a href="${contextPath}/messages.do?pupn=1&pups=25&pupn=${projectMessages.pageNumber}&pups=25&msgType=3">25</a></c:otherwise>
                                    </c:choose>
                                </span> -
                                <span>
                                    <c:choose>
                                        <c:when test="${projectMessages.pageSize eq 50}">50</c:when>
                                        <c:otherwise><a href="${contextPath}/messages.do?pupn=1&pups=50&pupn=${projectMessages.pageNumber}&pups=50&msgType=3">50</a></c:otherwise>
                                    </c:choose>
                                </span> -
                                <span>
                                    <c:choose>
                                        <c:when test="${projectMessages.pageSize eq 100}">100</c:when>
                                        <c:otherwise><a href="${contextPath}/messages.do?pupn=1&pups=100&pupn=${projectMessages.pageNumber}&pups=100&msgType=3">100</a></c:otherwise>
                                    </c:choose>
                                </span>
                                <c:if test="${not projectMessages.lastPage}"><a id="next-project" class="msg-next-page" href="${contextPath}/messages.do?pupn=${projectMessages.pageNumber + 1}&pups=${projectMessages.pageSize}&pupn=${projectMessages.pageNumber}&pups=${projectMessages.pageSize}&msgType=3" title="next page"></a></c:if>
                                </div>
                            </div>
                        </div>
                    </indaba:view>
                </div>
            </div>
        <jsp:include page="footer.jsp" flush="true" />

        <script type="text/javascript" charset="utf-8">
            $(function() {
                var msgType = '${msgType}';
                var boxId = 'inbox';
                switch(msgType){
                    case '2':
                        boxId = 'outbox';
                        break;
                    case '3':
                        boxId = 'projmsg';
                        break;
                    case '1':
                    default:
                        boxId = 'inbox';
                        break;
                }
		
                $('.box h3 a.toggleVisible').each(function(){
                    var box = $(this).parent().parent();
                    if(box.attr('id') != boxId) {
                        $(".content",box).slideUp("fast");
                        $('a.toggleFilter', this).html($.i18n.message('common.filter.clickopen') + "&nbsp; <img style='vertical-align:middle' src='images/expand_icon.png' alt='expand' />");
                    }
                });
                //$('html,body').animate({scrollTop: $('#'+boxId).offset().top}, 'slow');
            });
        </script>
    </body>
</html>