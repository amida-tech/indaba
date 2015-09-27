<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : yourcontent
    Created on : Jan 22, 2010, 11:26:15 PM
    Author     : Jeff Jiang
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="indaba" uri="indabaTaglib"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.yourcontent.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>

        <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js?v=2.0.6"></script>
        <link rel="stylesheet" type="text/css" href="css/jquery.fancybox-1.3.4.css?v=2.0.6" media="screen" />

        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
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
            <!--c:set var="active" value="yourcontent" scope="request"/-->
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div id="main-column">
                    <!-- invitation_boxes --><!-- end of new content, close this div on line 367 -->
                    <c:if test="${currentProject != null}">
                        <div class="box" id="sponsor">
                            <h3><indaba:msg key='jsp.yourcontent.sponsors' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a> ${prjName}</h3>
                            <div class="content" style="padding: 6px">
                                <c:if test="${currentProject.sponsorLogos != null && fn:length(fn:trim(currentProject.sponsorLogos)) > 0}">
                                <div style="padding: 0px">
                                    <table width="100%">
                                        <tr>
                                            <c:set var="sponserLogos" value="${fn:split(currentProject.sponsorLogos, ' ')}"/>
                                            <c:forEach items="${sponserLogos}" var="logo">
                                                <td align="center"><img style="width:120px;" src="${sponsorLogosBaseUrl}/${logo}" alt="${sponsorLogosBaseUrl}/${logo}"/></td>
                                            </c:forEach>
                                        </tr>
                                    </table>
                                </div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                    
                    <indaba:view prjid="${prjid}" uid="${uid}" right="read announcements">
                    <div class="box" id="announcement" <c:if test="${announcementSize==0}">style="display:none"</c:if>>
                        <h3><indaba:msg key='jsp.yourcontent.adminAnnounce' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                        <div class="content" style="padding: 6px">
                            <table>
                                <c:forEach items="${announcements}" var="announcement">
                                    <tr>
                                        <td>
                                            <b>${announcement.title}</b> &nbsp;&nbsp;&nbsp;
                                            <span style="float:right;"><fmt:formatDate value="${announcement.createdTime}" pattern="yyyy-MM-dd"/></span>
                                            <br/>
                                            ${announcement.body}
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
                    </indaba:view>

                    <div class="box icon-home-assignment">
                        <h3><indaba:msg key='jsp.yourcontent.bar.assignments' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                        <div class="content" style="padding: 0px">
                            <table>
                                <indaba:yourassginments prjid="${prjid}" targetUid="${uid}" subjectUid="${uid}" />
                            </table>
                        </div>
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
                    <jsp:include page="myOpenCases.jsp" flush="true"/>

                    <indaba:view prjid="${prjid}" uid="${uid}" right="see your content box">
                    <div class="box icon-home-content">
                        <h3><indaba:msg key='common.msg.youcontent' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                        <div class="content" style="padding: 0px">
                            <table>
                                <indaba:yourcontent prjid="${prjid}" uid="${uid}" />
                            </table>
                        </div>
                    </div>
                    </indaba:view>
                    <indaba:teamcontent prjid="${prjid}" uid="${uid}" />
                </div>
                <div id="sidebar">
                    <jsp:include page="messageSidebar.jsp" flush="true" />
                </div>
                <div id="chartdiv" align="center" style="display: none">
                    <indaba:msg key='common.msg.charterror' />
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
