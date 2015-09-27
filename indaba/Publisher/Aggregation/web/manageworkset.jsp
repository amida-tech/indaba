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

            function editWorkset(worksetId){
                url = "manageworkset.do?type=edit&id=" + worksetId;
                window.location.href = url;
            }

            function deleteWorkset(worksetId){
                jConfirm("the operation will delete all the datapoint associated with this Working Set, continue?", "Confirm Delete", function(r) {
                    if (r){
                        var data = "type=delete&id=" + worksetId;
                        $.ajax({
                            type: "POST",
                            url: "manageworkset.do",
                            data: data,
                            cache: false,
                            async: false,
                            success: function(result) {
                                var json = eval("(" + result + ")");
                                if(json.code == 0){
                                    jAlert("", "Delete Falied", null);
                                }else{
                                    $("tr#"+worksetId).remove();
                                }
                            }
                        });
                    }
                });
                return false;
            }

            function enableWorkset(worksetId){
                if(worksetId <= 0)
                    return false;

                jConfirm("If you have changed the indicators after creating Working Set, the system will automatic remove expired datapoints", "Confirm Enable Working Set", function(r) {
                    if (r){
                        var data = "type=enable&id=" + worksetId;
                        $.ajax({
                            type: "POST",
                            url: "manageworkset.do",
                            data: data,
                            cache: false,
                            async: false,
                            success: function(result) {
                                var json = eval("(" + result + ")");
                                if(json.code == 0){
                                    jAlert("", "Enable Falied", null);
                                }else if(json.code == 1){
                                    var enableDiv = "enableInput-" + worksetId;
                                    var disableDiv = "disableInput-" + worksetId;
                                    $('#'+enableDiv).hide();
                                    $('#'+disableDiv).show();
                                }else if(json.code == 2){
                                    jAlert("", "The Working Set has been enabled yet", null);
                                }
                            }
                        });
                    }
                });
                return false;
            }

            function disableWorkset(worksetId){
                if(worksetId <= 0)
                    return false;

                jConfirm("", "Confirm Disable Working Set", function(r) {
                    if (r){
                        var data = "type=disable&id=" + worksetId;
                        $.ajax({
                            type: "POST",
                            url: "manageworkset.do",
                            data: data,
                            cache: false,
                            async: false,
                            success: function(result) {
                                var json = eval("(" + result + ")");
                                if(json.code == 0){
                                    jAlert("", "Disable Falied", null);
                                }else if(json.code == 1){
                                    var enableDiv = "enableInput-" + worksetId;
                                    var disableDiv = "disableInput-" + worksetId;
                                    $('#'+enableDiv).show();
                                    $('#'+disableDiv).hide();
                                }else if(json.code == 2){
                                    jAlert("", "The Working Set has been Disabled yet", null);
                                }
                            }
                        });
                    }
                });
                return false;
            }

            function filterWorksetByName(){
                var keyword = $("#filterByName").val().toLowerCase();
                if(keyword.empty())
                    return false;

                $("#rtable .tdTitle").each(function(){
                    var name = $(this).find('td').eq(0).html().toLowerCase();
                    if(name.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                })
                return false;
            }

            function filterWorksetByOrg(){
                var keyword = $("#filterByOrg").find("option:selected").text().toLowerCase();
                if(keyword.empty())
                    return false;

                $("#rtable .tdTitle").each(function(){
                    var name = $(this).find('td').eq(1).html().toLowerCase();
                    if(name.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                })
                return false;
            }

            function filterWorksetByVisibility(visibility){
                var keyword;
                if(visibility == 1)
                    keyword = "public";
                else
                    keyword = "private";
                $("#rtable .tdTitle").each(function(){
                    var value = $(this).find('td').eq(2).html().toLowerCase().trim();
                    if(value == keyword){
                        $(this).show();
                    }else{
                        $(this).hide();
                    }
                })
                return false;
            }

            function showAllWorkset(){
                $("#rtable .tdTitle").each(function(){
                    $(this).show();
                })
                return false;
            }

            function manageAggregation(worksetId){
                var manageUrl = "manageaggregation.do?worksetId=" + worksetId;
                location.href = manageUrl;
            }
            
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="manageworkset" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="interactive" style=" text-align: center; ">
                <table style="width: 800px; font-size: 13px; float: right; margin: 20px 20px 20px 20px; " >
                    <tr>
                        <td align="right" width="600px" >
                            <button onclick="filterWorksetByName();">Filter by Name</button>
                        </td>
                        <td >
                            <input id="filterByName" name="filterByName" type="text" size="36" maxlength="40" />
                        </td>
                    </tr>
                    <tr>
                        <td align="right" width="600px" >
                            <button onclick="filterWorksetByOrg();">Filter by org</button>
                        </td>
                        <td >
                            <select class="middlebox" id="filterByOrg" name="filterByOrg">
                                <c:forEach items="${orgNames}" var="orgName"  varStatus="status">
                                    <option <c:if test="${status.first}">selected</c:if>>${orgName}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <button disabled>Filter by Visibility</button>
                        </td>
                        <td>
                            <input id="vPublic" name="filterByVisibility" type="radio" value="1"  onclick="filterWorksetByVisibility(1);" checked /> Public
                            <input id="vPrivate" name="filterByVisibility" type="radio" value="2" onclick="filterWorksetByVisibility(2);"/> Private
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <button onclick="showAllWorkset();">Show All</button>
                        </td>
                        <td></td>
                    </tr>
                </table>
                <table class="tablesorter" id="rtable" name="rtable" style="margin: auto; width: 95%" >
                    <thead>
                        <tr>
                            <th title="Click to order" width="120px" align="center">Name</th>
                            <th title="Click to order" width="120px" align="center">Org</th>
                            <th title="Click to order" width="120px" align="center">Visibility</th>
                            <th width="50px" align="center">Action</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th align="center">Name</th>
                            <th align="center">Org</th>
                            <th align="center">Visibility</th>
                            <th></th>
                        </tr>
                    </tfoot>
                    <c:if test="${empty worksets}">
                        <tr style="text-align: center; font-size: 12px;">
                            <td colspan="4">
                                No workset data
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${worksets}" var="workset">
                        <tr id="${workset.id}" class="tdTitle" >
                            <td align="center">${workset.name}</td>
                            <td align="center">${workset.orgName}</td>
                            <td align="center">
                                <c:choose>
                                    <c:when test="${workset.visibility == 1}">
                                        public
                                    </c:when>
                                    <c:otherwise>
                                        private
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td align="center">
                                <a href='#' onclick='return editWorkset(${workset.id});'><font size=3">Edit</font></a>
                                <a href='#' onclick='return deleteWorkset(${workset.id});'><font size="3">Delete</font></a>
                                <c:choose>
                                    <c:when test="${!workset.isActive}">
                                        <a id ="enableInput-${workset.id}" href="#" onclick="return enableWorkset(${workset.id});">
                                            <font size="3">Enable</font>
                                        </a>
                                        <a id ="disableInput-${workset.id}" style="display: none" href="#" onclick="return disableWorkset(${workset.id});">
                                            <font size="3">Disable</font>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a id ="enableInput-${workset.id}" style="display: none" href="#" onclick="return enableWorkset(${workset.id});">
                                            <font size=3">Enable</font>
                                        </a>
                                        <a id ="disableInput-${workset.id}" href="#" onclick="return disableWorkset(${workset.id});">
                                            <font size=3">Disable</font>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                                <!--<c:if test="${workset.hasDatapoint}">
                                    <div><input type="submit" value="Manage Aggregations" onclick="return manageAggregation(${workset.id});"/></div>
                                //</c:if>-->
                                <div><input type="submit" value="Manage Aggregations" onclick="return manageAggregation(${workset.id});"/></div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="preview" style="margin: 10px; " >
                    <form action="createworkset.do">
                        <button>Create Working Set</button>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
