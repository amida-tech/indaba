<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : allcontent
    Created on : Jan 22, 2010, 11:26:15 PM
    Author     : Jeff Jiang
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
        <title><indaba:msg key='jsp.notebook.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="notebook" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div id="main-column">
                    <!-- invitation_boxes --><!-- end of new content, close this div on line 367 -->
                    <div class="box icon-home-assignment">
                        <h3><indaba:msg key='jsp.notebook.notification' /></h3>
                        <div>
                            <span style="font-weight: bold; font-size: 13px;">&nbsp;&nbsp;&nbsp;&nbsp;<indaba:msg key='jsp.notebook.youHave' /> <span style="color: orange;">${count}</span> <indaba:msg key='jsp.notebook.assignedTasks' /></span>
                            <br/><br/>
                            <ul>
                                <c:forEach items="${assginmentList}" var="assignment">
                                    <c:if test="${assignment.taskid == 1}">
                                        <c:set var="action" value="Create NoteBook" scope="request"/>
                                    </c:if>
                                    <c:if test="${assignment.taskid == 2}">
                                        <c:set var="action" value="NoteBook Edit 1" scope="request"/>
                                    </c:if>
                                    <c:if test="${assignment.taskid == 3}">
                                        <c:set var="action" value="NoteBook Staff Review" scope="request"/>
                                    </c:if>
                                    <c:if test="${assignment.taskid == 4}">
                                        <c:set var="action" value="NoteBook Edit 2" scope="request"/>
                                    </c:if>
                                    <c:if test="${assignment.taskid == 5}">
                                        <c:set var="action" value="NoteBook Peer Review" scope="request"/>
                                    </c:if>
                                    <c:if test="${assignment.taskid == 6}">
                                        <c:set var="action" value="NoteBook Admin Approve" scope="request"/>
                                    </c:if>
                                    <c:if test="${assignment.taskid == 7}">
                                        <c:set var="action" value="NoteBook Pay" scope="request"/>
                                    </c:if>
                                    <li style="font-size: 12px;">&nbsp;&nbsp;&nbsp;&clubs; ${action} &nbsp;&nbsp;<a href="createnb.do?horseid=${assignment.horseId}&action=${assignment.taskid}">&rarr;<indaba:msg key='jsp.notebook.enter' /></a></li>
                                </c:forEach>
                            </ul>
                        </div>

                        <!--
                        <h3>Staff/Author Discussion Box</h3>
                        <p>
                            <textarea id="discussion" name="nbcnt" rows="10" cols="136"></textarea>
                        </p>
                        <p align="middle">
                            <input type="reset" name="reset" value=" &nbsp;&nbsp;Reset&nbsp;&nbsp; "/>&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="submit" name="submit" value=" &nbsp;&nbsp;Submit&nbsp;&nbsp; "/>
                        </p>
                        -->
                    </div>
                    <c:if test="${msgCount != 0}">
                        <div class="box icon-home-assignment">
                            <h3><indaba:msg key='common.label.reviews' /></h3>
                            <c:forEach items="${mbMsgList}" var="msg">
                                <span style="color: red;font-size: 12px;">&nbsp;&nbsp;&nbsp;&nbsp;&clubs; ${msg.message}</span>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>