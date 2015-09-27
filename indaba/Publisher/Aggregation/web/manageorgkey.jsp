<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Author     : luwb
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!--%@ taglib uri="indabaTaglib" prefix="indaba"%-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Manage Working Sets</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
        <script src="js/jquery.alerts.js" type="text/javascript"></script>
        <link href="css/jquery.alerts.css" rel="stylesheet" type="text/css" />
        <link type="text/css" rel="stylesheet" href="css/tablesorter/themes/default/style.css" />
        <style type="text/css">
            table tr td.preview-name {
                width: 150px;
            }
            table#preview-list tr:hover {
                background-color: #CCFFFF;
            }
            .edit_button {
                background-image: url("images/edit.jpg");
            }
            .delete_button {
                background-image: url("images/trash.jpg");
            }
            .tdTitle{
                font-size: 16px;
                font-weight: bold;
                margin-top: 0px;
                padding-left: 0px;
                margin-left: 0px;
                /*    text-align: left;*/
            }
        </style>
        <script type="text/javascript" charset="utf-8">
            $(function(){
                $("table.tablesorter").tablesorter( {
                    headers: {
                        // assign the secound column (we start counting zero)
                        3: {
                            // disable it by setting the property sorter to false
                            sorter: false
                        }}});
            });

            function gerenateOrgkey(orgId){
                jConfirm("the operation will generate API keys for organization, continue?", "Confirm", function(r) {
                    if (r){
                        var data = "type=generate&orgId=" + orgId;
                        $.ajax({
                            type: "POST",
                            url: "manageorgkey.do",
                            data: data,
                            cache: false,
                            async: false,
                            success: function(result) {
                                var json = eval("(" + result + ")");
                                if(json.code == 0){
                                    jAlert("", "Generate Key Falied", null);
                                }else{
                                    //simple implement, just refresh page
                                    window.location.href = "manageorgkey.do"
                                }
                            }
                        });
                    }
                });
                return false;
            }
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="manageorgkey" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="interactive" style=" text-align: center; ">
                <table class="tablesorter" id="rtable" name="rtable" style="margin: auto; width: 95%" >
                    <tr>
                        <td></td>
                        <td width="50px" align="center">OrgId</td>
                        <td width="120px" align="center">OrgName</td>
                        <td width="50px" align="center">Version</td>
                        <td width="120px" align="center">Key</td>
                        <td width="100px" align="center">Effective Time</td>
                        <td width="50px" align="center">Valid Days</td>
                    </tr>
                    <c:choose>
                        <c:when test="${empty orgkeys}">
                            <tr style="text-align: center; font-size: 12px;">
                                <td colspan="6"  align="center">
                                    No Organization Information
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${orgkeys}" var="org">
                                <c:choose>
                                    <c:when test="${org.keys == null}">
                                    <tr>
                                        <td  align="center">
                                            <input type="submit" value="Generate Orgkey" onclick="return gerenateOrgkey(${org.orgId});"/>
                                        </td>
                                        <td  align="center" class="tdTitle" >${org.orgId}</td>
                                        <td  align="center" class="tdTitle" >${org.orgName}</td>
                                        <td  align="center">-</td>
                                        <td  align="center">-</td>
                                        <td  align="center">-</td>
                                        <td  align="center">-</td>
                                    </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${org.keys}" var="orgkey" varStatus="status">
                                            <tr>
                                            <c:choose>
                                                <c:when test="${status.first}">
                                                    <td>
                                                        <input type="submit" value="Generate Orgkey" onclick="return gerenateOrgkey(${org.orgId});"/>
                                                    </td>
                                                    <td align="center" class="tdTitle" >${org.orgId}</td>
                                                    <td align="center" class="tdTitle" >${org.orgName}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td  align="center" colspan="3"></td>
                                                </c:otherwise>
                                            </c:choose>
                                                    <td align="center" class="tdTitle">${orgkey.version}</td>
                                                    <td align="center" class="tdTitle">${orgkey.data}</td>
                                                    <td align="center" class="tdTitle"><fmt:formatDate value="${orgkey.effectiveTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                    <td align="center" class="tdTitle">${orgkey.validDays}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </table>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
