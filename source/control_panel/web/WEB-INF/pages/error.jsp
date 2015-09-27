<%-- 
    Document   : error
    Created on : 2012-12-6, 12:29:00
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - <indaba:msg key="cp.label.error" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link REL="SHORTCUT ICON" href="${contextPath}/resources/images/indaba_icon.ico"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/jquery-ui-1.8.21.custom.css">

        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/ocs-common.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/json2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery-1.7.2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.i18n.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/jsI18nMessages"></script>

        <style type="text/css">
            #errcontainer {
                margin: 15px;
                border: 1px solid #ddd;
                background-color: #fdfdfd;
            }
            #content.err{
                padding: 20px;
            }
            #content.err h1{
                color: orangered;
            }
            #content.err .errdesc{
                background: url("${contextPath}/resources/images/warning.png") no-repeat 5px 12px;
                border: 1px solid #ddd;
                padding: 10px 21px;
                font-size: 14px;
            }
            .errdesc a.btn {
                padding: 2px 10px;
            }
            .errdesc span.backbtn{
                margin-left: 10px;
            }
        </style>
    </head>
    <body>
        <div id="indaba">
            <div class="wrapper">
                <div id="head">
                    <div class="logo"><img alt="Logo" src="${contextPath}/resources/images/indaba-logo.gif" style="vertical-align: middle"/>
                        <span style="position:relative;top:4px; font-size: 14px;font-weight: bold; color: #666"> |&nbsp;&nbsp;</span><span class="title">CONTROL PANEL</span>
                    </div>
                    <div class="login">Welcome <span class="username">${user.firstname} ${user.lastname}</span>! |
                        <a href="/indaba" target="_blank">Builder</a> |
                        <a href="/aggregation" target="_blank">Publisher</a> |
                        <a href="${contextPath}/login!logout">Log out</a>
                    </div>
                </div>
                <div class="clear"></div>

                <div id="errcontainer">
                    <div id="emptyToolbar"></div>
                    <div id="content" class="err">
                        <h1><indaba:msg key="cp.label.error" /></h1>
                        <div class="errdesc">
                            <div>
                                <span class="errmsg">
                                    <c:choose>
                                        <c:when test="${not empty errMsg}">
                                            ${errMsg}
                                        </c:when>
                                        <c:otherwise>
                                            <indaba:msg key="cp.err.server_error" />
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="backbtn"><a class="btn" href="javascript:history.back();" style="font-weight: normal" >Back</a></span>
                                <%--
                                <c:if test="${(not empty exception.message) and (not empty exceptionStack)}">
                                    <a class="toggle-details expand" href="#"><indaba:msg key="cp.text.show_details"/></a>
                                </c:if>
                                --%>
                            </div>

                            <%--
                                                        <div class="errdetails">
                                                            <c:if test="${not empty exception.message}"><div class="exmsg">${exception.message}</div></c:if>
                                                            <div class="exstack">${exceptionStack}</div>
                                                        </div>
                            --%>
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="footer.jsp" flush="true" />
        </div>

    </body>
</html>
<%--
<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        $('.errdesc .toggle-details').click(function(){
            if($(this).hasClass('expand')) {
                $('div.errdetails').show();
                $(this).removeClass('expand').addClass('collapse').text($.i18n.message("cp.text.hide_details"));
            } else{
                $('div.errdetails').hide();
                $(this).removeClass('collapse').addClass('expand').text($.i18n.message("cp.text.show_details"));
            }
        });
    }
</script>
--%>