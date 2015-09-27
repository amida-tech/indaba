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
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <title><indaba:msg key='jsp.allcontent.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        
        <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js?v=2.0.6"></script>
	<link rel="stylesheet" type="text/css" href="css/jquery.fancybox-1.3.4.css?v=2.0.6" media="screen" />
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <script type="text/javascript" src="js/jquery.blockUI.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/FusionCharts.js"></script>
        <script type="text/javascript" src="js/jquery.blockUI.js"></script>
        <script type="text/javascript" src="js/chart.js"></script>
        <script type="text/javascript" src="js/button.js"></script>        
        <script type="text/javascript" src="js/taskList.js"></script>
        <style type="text/css">
            .fancybox-custom .fancybox-skin {
                box-shadow: 0 0 50px #222;
            }
	</style>
    </head>
    <body>
        <div id="indaba">
            <!-- c:set var="active" value="allcontent" scope="request"/ -->
            <jsp:include page="header.jsp" flush="true" />
            <div id="all-content-wrapper" class="wrapper">
                <!-- invitation_boxes --><!-- end of new content, close this div on line 367 -->
                <form action="allcontent.do">
                    <c:if test="${reportUrl != null || analyticsUrl != null}">
                        <c:if test="${analyticsUrl != null}">
                                <a href="${analyticsUrl}" target="_blank" style="float: right; margin-right: 8px;">
                                    <indaba:msg key='common.btn.analytics' />
                                </a>
                            </c:if>
                            <c:if test="${reportUrl != null}">
                                <a href="${reportUrl}" target="_blank" style="float: right; margin-right: 8px;">
                                    <img src="images/doc.png" alt="report" height="12" ></img><indaba:msg key='common.btn.report' />
                                </a>
                            </c:if>                        
                            <br/>
                    </c:if>
                    <div class="box">                      
                        <h3><indaba:msg key='jsp.allcontent.allcontents' />
                            <a href="#" class="toggleFilter" style="float: right; margin-right: 8px;"><indaba:msg key='common.filter.clickopen' />&nbsp; <img style="vertical-middle" src='images/expand_icon.png' alt='expand' /></a>
                        </h3>
                        <div class="content" 
                             <c:if test="${targetId == '' && productId == '' && status == ''}">
                                style="display: none;"
                             </c:if>
                             >
                            <jsp:include page="filters.jsp" flush="true" />
                        </div>                        
                    </div>
                </form>
                <br/>
                <div class="icon-home-content" align="center">
                    <table id="all-content-table" align="center">
                        <indaba:allcontent prjid="${prjid}" uid="${uid}" status="${status}" targetIds="${targetIds}" prodIds="${prodIds}" />
                    </table>
                </div>
                <div id="chartdiv" align="center" style="display: none">
                    <indaba:msg key='common.msg.charterror' />
                </div>
                <div style="display: none;">
                <div id="deadline" style="width:400px;">
                    <table>
                        <th align="center">
                            <indaba:msg key='common.btn.editdeadline' />
                        </th>
                        <tr>
                            <td>
                                <indaba:msg debug="true" key="common.msg.setdeadline">
                                    <indaba:arg value='<span id="userName"></span>' />
                                    <indaba:arg value='<br/><div id="taskName"></div>' />
                                </indaba:msg>
                            </td>
                        </tr>
                        <tr>
                            <td><indaba:msg key='jsp.yourcontent.deadline' />: <input type="text" id="newDeadline" name="newDeadline" size="10" maxlength="10"/></td>
                        </tr>
                        <td align="center">
                            <br/>
                            <input type="button" value='<indaba:msg debug="true" key="common.btn.save" />' onclick="saveDeadline();"/>
                            <input type="button" value='<indaba:msg debug="true" key="common.btn.cancel" />' onclick="$.fancybox.close();"/>
                            <input type="hidden" id="assignmentId" name="assignmentId"/>
                            <input type="hidden" id="dueTime" name="dueTime"/>
                        </td>
                    </table>
                </div>
            </div>
            </div>
        </div>      
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>