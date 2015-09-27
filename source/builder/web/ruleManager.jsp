<%-- 
    Document   : ruleManager
    Created on : 2010-5-20, 11:22:55
    Author     : Luke Shi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<indaba:view prjid="${prjid}" uid="${uid}" right="use indaba admin">
<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <title><indaba:msg key='jsp.ruleManager.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/btn.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript">
            function loadFile(file) {
                $.ajax({
                    type: "POST",
                    url: "ruleManager.do",
                    data: "action=load&fileName=" + file,
                    success: function(result) {
                        //document.getElementById("fileName").innerHTML = file;
                        document.myform.fileName.value = file;
                        document.myform.fileContent.value = result;
                        return false;
                    },
                    error: function(result) {
                        alert(result);
                    }
                });
            }

            function editFile(file) {   
                loadFile(file);
                return false;
            }

            function editRule(rule) {
                loadFile(rule);
                return false;
            }

            function getSelections() {
                var rule = document.getElementsByName("rule");
                var rules = "";
                for (var i = 0; i < rule.length; i++) {
                    if (rule[i].checked) {
                        if (rules != "")
                            rules += ",";
                        rules += rule[i].value;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "ruleManager.do",
                    data: "action=merge&rules=" + rules,
                    success: function(result) {
                        document.myform.fileContent.value = result;
                        return false;
                    },
                    error: function(result) {
                        alert(result);
                    }
                });
                return;
            }

            function saveFile() {
                var path = document.myform.path.value;
                var fileName = document.myform.fileName.value;
                var content = document.myform.fileContent.value;
                $.ajax({
                    type: "POST",
                    url: "ruleManager.do",
                    data: "action=save&fileName=" + fileName + "&content=" + content + "&path=" + path,
                    success: function(result) {
                        window.location.reload();
                        alert("File Saved " + path+"/"+fileName);
                        return false;
                    },
                    error: function(result) {
                        alert(result);
                    }
                });
                return false;
            }

            function deleteFile() {
                var path = document.myform.path.value;
                var fileName = document.myform.fileName.value;
                $.ajax({
                    type: "POST",
                    url: "ruleManager.do",
                    data: "fileName=" + fileName + "&action=delete&path=" + path,
                    success: function(result) {
                        window.location.reload();
                        alert("File Deleted: " + path+"/"+fileName);
                        return false;
                    },
                    error: function(result) {
                        alert(result);
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div class="box" id="ruleMgr">
                    <h3><indaba:msg key='common.msg.rulemanager' />
                        <a class="toggleVisible" href="#"><img src='images/collapse_icon.png' alt='collapse' /></a>
                    </h3>
                    <div class="content" style="padding: 20px" align="center">
                        <table width="100%" style="font-size: 13px;">
                        <tr>
                            <th><indaba:msg key='jsp.ruleManager.ruleFiles' /></th>
                            <th><indaba:msg key='jsp.ruleManager.ruleComponents' /></th>
                        </tr>
                        <tr>
                            <td valign="top">
                                <table>
                                    <c:forEach items="${files}" var="file">
                                    <tr>
                                        <td>
                                            <input type="radio" name="file" value="${file}" onclick="document.myform.fileName.value = '${file}'; " >
                                            <a href="#" onclick="editFile('${file}'); return false;">${file}</a>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                </table>
                            </td>
                            <td valign="top">
                                <table>
                                    <c:forEach items="${rules}" var="rule">
                                    <tr>
                                        <td>
                                            <input type="checkbox" name="rule" value="${rule}" onclick="getSelections();" >
                                            <a href="#" onclick="editRule('${rule}'); return false;">${rule}</a>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                </table>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!--
                    <p align="center" />
                    <input type="button" onclick="getSelections()" value="Merge">
                    -->
                </div>

                <div class="box">
                    <form name="myform">
                    &nbsp;&nbsp;&nbsp;&nbsp;<b><indaba:msg key='jsp.ruleManager.path' /></b> <input id="path" name="path" size="100" value="${path}" /><br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;<b><indaba:msg key='common.label.file' /></b>&nbsp;&nbsp; <input id="fileName" name="fileName" size="100" />
                    <a href="#" onclick="deleteFile();"><img src="images/delete.png" alt="Delete" /></a>
                    <!--<input type="button" onclick="deleteFile()" value="Delete">
                    <h3><div id="fileName">Rule / File</div></h3>-->
                    <div class="content" style="padding: 10px">
                        <textarea id="fileContent" name="fileContent" class="text" rows="20"></textarea>
                    </div>
                    </form>
                    <br/>
                    <p align="center" />
                    <button class="large button blue icon-save" type="submit" id="savebutton" name="savebutton" onclick="saveFile();"><indaba:msg key='common.btn.save' /></button>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
</indaba:view>
