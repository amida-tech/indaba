<%-- 
    Document   : contentApproval
    Created on : 2010-5-9, 12:06:21
    Author     : Jeanbone
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
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <title><indaba:msg key='jsp.contentApproval.pagetitle' /></title>
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
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" charset="utf-8">
            function cancelClick() {
                location.href = "/yourcontent.do";
            }

            function chkClick() {
                if (document.getElementsByName("chkContentApproval")[0].checked) {
                    document.getElementsByName("saveAndDone")[0].disabled = false;
                    document.getElementsByName("save")[0].disabled = false;
                } else {
                    document.getElementsByName("saveAndDone")[0].disabled = true;
                    document.getElementsByName("save")[0].disabled = true;
                }
            }

            function setAction(action) {
                document.forms["contentApproval"].elements["action"].value = action;
            }
        </script>
    </head>
    <body>
        <div id="indaba">
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <form action="contentApproval.do" name="contentApproval">
                    <div class="box" id="contentApproval">
                        <h3><indaba:msg key='jsp.contentApproval.ApproveInstruction' /></h3>
                        <div class="content" style="padding: 0px; margin-left: 15%">
                                <label><input type="checkbox" name="chkContentApproval" checked="true" onclick="chkClick()" /><indaba:msg key='jsp.contentApproval.ApproveThisContent' /></label><br><br>
                                <div style="width:240px; height:140px">
                                    <div style="top: 0px; float: left; width: 40px">
                                        <label><indaba:msg key='common.label.note' />: </label>
                                    </div>
                                    <div style="top: 0px; float: left; width: 200px">
                                        <textarea class="text" rows="8" name="note">${contentApproval.note}</textarea>
                                    </div>
                                </div>
                                <br><br>
                                <input name="horseid" type="hidden" value="${param.horseid}" />
                                <input name="assignid" type="hidden" value="${param.assignid}" />
                                <input name="action" type="hidden" value="" />
                                <div style="margin-left: 25%">
                                    <button type="submit" name="save" onclick="setAction('save');"><img src="images/save.gif" alt=""/>&nbsp;&nbsp;Save&nbsp;&nbsp;</button>
                                    <button type="submit" name="saveAndDone" class="iamdone large button blue icon-check" onclick="setAction('saveAndDone');"><indaba:msg key='common.btn.donesubmit' /></button>
                                    <!--input type="button" name="back" value="CANCEL" onclick="cancelClick();" /-->
                                </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
