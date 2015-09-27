<%-- 
    Document   : horseApproval
    Created on : 2010-7-9, 12:06:21
    Author     : Luke
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><indaba:msg key='jsp.horseApproval.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <style type="text/css">
            .box h3 a {
                font-size: 10px;
                padding: 0 20px;
                float: right;
            }
        </style>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
    </head>
    <body>
        <div id="indaba">
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <form action="horseApproval.do">
                    <input name="horseid" type="hidden" value="${param.horseid}" />
                    <input name="assignid" type="hidden" value="${param.assignid}" />
                    <input name="saveAndDone" type="hidden" value="TRUE" />
                    <div class="box" id="horseApproval">
                        <h3><indaba:msg key='jsp.horseApproval.instruction' /></h3>
                        <div class="content" style="padding: 0px; margin-left: 5%">
                            <br/>
                            <p><b>${horseInfo.targetName} ${horseInfo.productName}</b></p>
                            <br/>
                            <div align="center">
                                <button type ="submit" id="doneBtn" name="saveAndDone" class="iamdone large button blue icon-check"><indaba:msg key='jsp.horseApproval.approveDone' /></button>
                                <%--
                                <input type="submit" name="saveAndDone" value="Approval - I'm Done" />
                                --%>
                            </div>
                            <br/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
