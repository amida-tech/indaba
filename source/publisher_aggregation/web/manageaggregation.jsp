<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Author     : luwb
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Manage Aggregation</title>
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
                //$("input, textarea, select").uniform();
                $("table.tablesorter").tablesorter( {
                    headers: {
                        // assign the secound column (we start counting zero)
                        2: {
                            // disable it by setting the property sorter to false
                            sorter: false
                        }}});
            });

            function editAggregation(dpId){
                url = "manageaggregation.do?type=edit&id=" + dpId;
                window.location.href = url;
            }

            function deleteAggregation(dpId){
                jConfirm("Are you sure to delete datapoint?", "Confirm Delete", function(r) {
                    if (r){
                        var data = "type=delete&id=" + dpId;
                        $.ajax({
                            type: "POST",
                            url: "manageaggregation.do",
                            data: data,
                            cache: false,
                            async: false,
                            success: function(result) {
                                var json = eval("(" + result + ")");
                                if(json.code == 0){
                                    jAlert("", "Delete Falied", null);
                                }else if(json.code == 2){
                                    jAlert("", "This Aggregation has been referenced, can't delete", null);
                                }else{
                                    $("tr#"+dpId).remove();
                                }
                            }
                        });
                    }
                });
                return false;
            }
            
            function filterById(){
            var keyword = $("#filterById").val().toLowerCase();
                if(keyword.empty())
                    return false;

                $("#rtable .tdTitle").each(function(){
                    var id = $(this).find('td').eq(0).html().toLowerCase();
                    if(id.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                 })
                 return false;
            }

            function filterByName(){
                var keyword = $("#filterByDisplayName").val().toLowerCase();
                if(keyword.empty())
                    return false;

                $("#rtable .tdTitle").each(function(){
                    var id = $(this).find('td').eq(1).html().toLowerCase();
                    if(id.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                 })
                 return false;
            }

            function showAllAggregation(){
                $("#rtable .tdTitle").each(function(){
                    $(this).show();
                })
                return false;
            }

        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="manageaggr" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div class="preview">Working Set: ${worksetName}</div>
                <table style="width: 800px; font-size: 13px; " >
                    <tr>
                        <td align="right" width="600px" >
                            <button onclick="filterById();">Filter by ID</button>
                        </td>
                        <td >
                            <input id="filterById" name="filterById" type="text" size="36" maxlength="40" />
                        </td>
                    </tr>
                    <tr >
                        <td align="right">
                            <button onclick="filterByName();">Filter by Display Name</button>
                        </td>
                        <td>
                            <input id="filterByDisplayName" name="filterByDisplayName" type="text" size="36" maxlength="40" />
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <button onclick="showAllAggregation();">Show All</button>
                        </td>
                        <td></td>
                    </tr>
                </table>
                <table class="tablesorter" id="rtable" name="rtable">
                    <thead>
                        <tr>
                            <th title="Click to order" width="120px" align="center">Datapoint ID</th>
                            <th title="Click to order" width="120px" align="center">Display Name</th>
                            <th width="50px" align="center">Action</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th align="center">Datapoint ID</th>
                            <th align="center">Display Name</th>
                            <th align="center">Action</th>
                        </tr>
                    </tfoot>
                    <c:if test="${ShowDatapoints}">
                    <c:forEach items="${datapoints}" var="datapoint">
                        <tr id="${datapoint.id}" class="tdTitle">
                            <td align="center">${datapoint.shortName}</td>
                            <td align="center">${datapoint.name}</td>
                            <td align="center">
                                <a href='#' onclick='return editAggregation(${datapoint.id});'><font size=3">Edit</font></a>
                                <a href='#' onclick='return deleteAggregation(${datapoint.id});'><font size=3">Delete</font></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </c:if>
                </table>
                <div class="preview">
                    <a href="createaggregation.do?worksetId=${param.worksetId}">Add Aggregation</a>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
