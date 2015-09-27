<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="indabaTaglib" prefix="indaba" %>
<html>
    <head>
        <title><indaba:msg key='jsp.error.pagetitle' /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div id="main-column">
                    <!-- invitation_boxes --><!-- end of new content, close this div on line 367 -->
                    <div class="box icon-home-assignment">
                        <h3 style="color: #FF9966;"><indaba:msg key='jsp.error.errors' /></h3>
                        <div class="content" style="padding: 0px">
                            <div style="line-height: 40px; color: orange; font-size: 13px; vertical-align: middle;">
                                &nbsp;&nbsp;
                                <img src="images/warning.gif" alt="Error" />
                                <c:choose>
                                    <c:when test="${empty errMsg}" >
                                        <indaba:msg key='jsp.error.errMsg' />
                                    </c:when>
                                    <c:otherwise>
                                        ${errMsg}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:history.back(1); return false;"><indaba:msg key='jsp.error.back' /></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
