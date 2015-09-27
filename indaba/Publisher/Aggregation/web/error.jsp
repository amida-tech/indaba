<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <title>Error Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                        <h3 style="color: #FF9966;">ERRORS</h3>
                        <div class="content" style="padding: 0px">
                            <div style="line-height: 40px; color: orange; font-size: 13px; vertical-align: middle;">
                                &nbsp;&nbsp;
                                <img src="images/warning.gif" alt="Error" />
                                <c:if test="${errMsg == null}" >
                                    <c:set var="errMsg" value="Internal server error/exception occurs. Please contact your administrator." />
                                </c:if>
                                <span >${errMsg}</span>
                            </div>
                            <div>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:history.back(1)">Back</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
