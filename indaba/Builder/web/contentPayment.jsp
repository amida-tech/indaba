<%-- 
    Document   : contentPayment
    Created on : 2010-5-15, 16:12:08
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
        <title><indaba:msg key='jsp.contentPayment.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <style type="text/css">
            .box h3 a {
                font-size: 10px;
                padding: 0 20px;
                float: right;
            }
            .row-item{
                min-height:20px;
                line-height:20px;
                margin:5px 0;
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
            .row-item .prompt{
                color:red;
                display:none;
                font-style:italic;
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

            function validateAmount(action) {
                var amount = document.getElementsByName("amount")[0].value;
                var pattern = /^(([1-9]\d*)|0)(\.\d+)?$/;
                if (!pattern.exec(amount)) {
                    $('.prompt').filter('.amount').show('fast');
                    return false;
                } else {
                    $('.prompt').filter('.amount').hide('fast');
                    document.forms["contentPayment"].elements["action"].value = action;
                    return true;
                }
            }
        </script>
    </head>
    <body>
        <div id="indaba">
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <form action="contentPayment.do?horseid=${horseid}" name="contentPayment">
                    <div class="box" id="contentPayment">
                        <h3><indaba:msg key='jsp.contentPayment.instruction' /></h3>
                        <div class="content">
                            <br/>
                            <div class="row-item">
                                <label for="amount"><indaba:msg key='jsp.contentPayment.paymentMount' />: </label>
                                <input type="text" name="amount" id="amount" value="${contentPayment.amount}" onchange="validateAmount()" size="80" />
                                <div class="prompt amount"><indaba:msg key='jsp.contentPayment.prompt' /></div>
                            </div>
                            <div class="row-item">
                                <label for="payees"><indaba:msg key='jsp.contentPayment.payees' />: </label>
                                <input type="text" name="payees" id="payees" value="${contentPayment.payees}" size="80" />
                            </div>
                            <div class="row-item">
                                <label><indaba:msg key='jsp.contentPayment.paymentDate' />:</label>
                                ${contentPayment.time}
                            </div>
                            <div class="row-item">
                                <label for="note"><indaba:msg key='common.label.note' />: </label>
                                <textarea class="text" rows="8" name="note" id="note" >${contentPayment.note}</textarea>
                            </div>
                                <input name="horseid" type="hidden" value="${param.horseid}" />
                                <input name="assignid" type="hidden" value="${param.assignid}" />
                                <input name="action" type="hidden" value="" />
                                <div class="row-item">
                                    <label>&nbsp;</label>
                                    <button type ="submit" id="saveBtn" name="save" class="large button blue icon-save" onclick="return validateAmount('save');"><indaba:msg key='common.btn.save' /></button>
                                    <button type ="submit" id="doneBtn" name="saveAndDone" class="iamdone large button blue icon-check" onclick="return validateAmount('saveAndDone');"><indaba:msg key='common.btn.donesubmit' /></button>
                                    <!input type="button" name="back" value="CANCEL" onclick="cancelClick();" /-->
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
