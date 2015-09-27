<%-- 
    Document   : assignmentAlertMessage
    Created on : 2010-7-12, 19:26:44
    Author     : Jeanbone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <title><indaba:msg key='jsp.assignmentMessage.pagetitle' /></title>
    </head>
    <body>
        <script type="text/javascript">
            <c:if test="${alertInfo != null}">
                alert('${alertInfo}');
            </c:if>
            location.href = "yourcontent.do";
        </script>
    </body>
</html>
