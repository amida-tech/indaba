<%--
    Document   : people
    Created on : Mar 2, 2010, 8:38:56 PM
    Author     : Luke
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.peopleProfile.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" media="screen"/>
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/button.js"></script>

        <script type="text/javascript">
            var isIE = false;
            var isFF = false;
            var isSa = false;

            if((navigator.userAgent.indexOf("MSIE")>0) && (parseInt(navigator.appVersion) >=4))
                isIE = true;
            if(navigator.userAgent.indexOf("Firefox")>0)
                isFF = true;
            if(navigator.userAgent.indexOf("Safari")>0)
                isSa = true;

 

            function updatepwd(){
                var newpwd = document.getElementById('newpwd').value;
                var confirm = document.getElementById('confirm').value;
                var errElmt2 = document.getElementById("errmsgNew");
                errElmt2.innerHTML="";
                var errElmt3 = document.getElementById("errmsgCon");
                errElmt3.innerHTML="";
                if((newpwd + '.' == ".")||(confirm + '.' == ".")){
                    if(newpwd + '.' == "."){
                        var errElmt = document.getElementById("errmsgNew");
                        errElmt.innerHTML = $.i18n.message('common.js.alert.newpasswdempty');
                    }
                    if(confirm + '.' == "."){
                        var errElmt = document.getElementById("errmsgCon");
                        errElmt.innerHTML = $.i18n.message('common.js.alert.confirmpasswdempty');
                    }
                    return false;
                }else{
                    if(newpwd==confirm){
                        return true;
                    }else
                    {
                        var errElmt = document.getElementById("errmsgNew");
                        errElmt.innerHTML = $.i18n.message('common.js.alert.passwdinconsistent');
                        return false;
                    }
                }
            }
        </script>

        <style type="text/css">
            .row-item{
                min-height:20px;
                line-height:20px;
                padding-left:170px;
                position:relative;
            }
            .row-item label{
                display:inline;
                font-weight:bold;
                left:0;
                position:absolute;
                text-align:right;
                width:160px;
            }
            h3 a span {
                color: #194e96;
            }
        </style>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="people" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper people">
                <!--<div id="main-column">-->
                <div class="box">
                    <h3><indaba:msg key='jsp.peopleProfile.user' />: ${user.username}
                    </h3>
                    <table>
                        <tr>
                            <td>                        
                                    <div id ="changepwd"class="content">
                                        <form action="activate.do" method="POST" onsubmit="return updatepwd();">
                                        <table width="100%">
                                            <tr><div><td></td><td><b>Set New Password to Activate Your Account</b></td></div></tr>
                                            <tr><div>
                                                    <td nowrap><p align="right"><b><indaba:msg key='jsp.peopleProfile.pwd.new' /></b></p></td>
                                                    <td><p/><input type="password" name="newpwd" id="newpwd" value="" size="40"/></td>
                                                    <td><font color="red"><p class="errtxt" id="errmsgNew">
                                                                <logic:present name="errmsgNew" scope="request">
                                                                    <bean:define id="errmsgNew" name="errmsgNew"  toScope="request"/>
                                                                    <bean:write name="errmsgNew" /></logic:present></p></font>
                                                    </td>
                                                </div></tr>
                                            <tr><div>
                                                    <td nowrap><p align="right"><b><indaba:msg key='jsp.peopleProfile.pwd.new.confirm' /></b></p></td>
                                                    <td><p/><input type="password" name="confirm" id="confirm" value="" size="40"/></td>
                                                    <td><font color="red"><p class="errtxt" id="errmsgCon">
                                                                <logic:present name="errmsgCon" scope="request">
                                                                    <bean:define id="errmsgCon" name="errmsgCon"  toScope="request"/>
                                                                    <bean:write name="errmsgCon" /></logic:present></p></font>
                                                    </td>
                                                </div></tr>
                                        </table>
                                        <p align="center">
                                            <input type ="submit" name="submitpwd" id="submitpwd" class='small button blue' value="<indaba:msg key='common.btn.submit' />" />               
                                        </p>
                                            <input type="input" name="username" id="username" value="${username}" style="display: none" />
                                            <input type="password" name="curpwd" id="curpwd" value="${password}" style="display: none" />
                                        </from>
                                    </div>
                                </td>
                        </tr>
                    </table>
                </div>
                <!--</div>-->

                <div class="clear">
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
