<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : workset
    Created on : Feb 21, 2011, 11:26:15 PM
    Author     : Jeff Jiang
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Create Working Set Step #2</title>
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
        </style>
        <script type="text/javascript" charset="utf-8">
            var selectedIndicators = new Array();
            var size = ${fn:length(indicators)};
            $(function(){
                //$("input, textarea, select").uniform();
                $("table.tablesorter").tablesorter( {
                    headers: {
                        // assign the secound column (we start counting zero)
                        0: {
                            // disable it by setting the property sorter to false
                            sorter: false
                        }}});
                $(":checkbox").parent("span").each(function(){
                    $(this).attr("class", "");
                });
                
                $("#checkall").click(function(){
                    var check_status = this.checked;
                    $("input[name='indicatorId[]']").each(function(){
                        var tr = $(this).parent("span").parent("div").parent("td").parent("tr");
                        if(tr.css("display") == "none"){
                            return;
                        }
                        this.checked = check_status;
                        //$(this).attr("checked", check_status);
                        // this.checkAll();
                        if(check_status) {
                            $(this).parent("span").attr("class", "checked");
                        } else {
                            $(this).parent("span").attr("class", "");
                        }
                    });
                });
            });

            function addSelections() {
                $("td>div.checker>span.checked").each(function(){
                    var id = $(this).parent("div").parent("td").parent("tr").attr("id");
                    var idVal = id.replace("indicator-", "");

                    if(selectedIndicators.indexOf(idVal) != -1) {
                        return;
                    }
                    selectedIndicators.push(idVal);
                    var indicatorName = $.trim($(this).parent("div").parent("td").siblings("td.indicator-name").text());
                    var indicatorDesc = $.trim($(this).parent("div").parent("td").siblings("td.indicator-desc").text());
                    //alert("id: " + id + ",indicatorId: " +indicatorName + ", indicatorDesc: " + indicatorDesc);
                    var indicatorNameTag = "<td class='preview-name'>(<a href='#' onclick='return removeSelection(\"" + id + "\")'><img src='images/delete.png' height='12' /></a>) " + indicatorName + "</td>";
                    var indicatorDescTag = "<td class='preview-desc'>" + indicatorDesc + "</td>";
                    $("#preview-list").append("<tr id='preview-" + id + "'>" + indicatorNameTag + indicatorDescTag + "</tr>");
                    $(this).attr("class", "");
                    $(this).parent("div").parent("td").parent("tr").hide();
                    
                });
                
                if(selectedIndicators.length > 0) {
                    $('#preview').show();
                }
                
                if(selectedIndicators.length >= size) {
                    $('#indicator-0').css("display", "table-row");
                }
                
                return false;
            }
            function removeSelection(id) { // id: "indicator-###"
                $("#preview-" + id).remove();
                $("#" + id).show();
                selectedIndicators.remove(id.replace("indicator-", ""));
                if(selectedIndicators.length <= 0) {
                    $('#preview').hide();
                }

                if(selectedIndicators.length != size) {
                    $('#indicator-0').css("display", "none");
                }

                return false;
            }
            function doSubmit() {
                $('#indicatorlist').val(selectedIndicators);
            }

            function filterIndicatorById(){
                var keyword = $("#filterById").val().toLowerCase();
                if(keyword.empty())
                    return false;

                $("#rtable .tdTitle").each(function(){
                    var name = $(this).find('td').eq(2).html().toLowerCase();
                    if(name.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                 })

                return false;
            }

            function filterIndicatorByName(){
                var keyword = $("#filterByDisplayName").val().toLowerCase();
                if(keyword.empty())
                    return false;

                $("#rtable .tdTitle").each(function(){
                    var name = $(this).find('td').eq(3).html().toLowerCase();
                    if(name.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                 })

                return false;
            }


            function filterIndicatorByTag(){
                var keyword = $("#filterByTag").val().toLowerCase();
                if(keyword.empty())
                    return false;

                $("#rtable .tdTitle").each(function(){
                    var name = $(this).find('td').eq(4).html().toLowerCase();
                    if(name.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                 })

                return false;
            }

            function showAllIndicators(){
                $("#rtable .tdTitle").each(function(){
                    $(this).show();
                })
                return false;
            }

            function quitWorkset(){
                jConfirm("Your work has not been saved. Are you sure to quit", "Confirm Quit Workset", function(r) {
                    if (r){
                        var toUrl = "quitworkset.do";
                        location.href = toUrl;
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
            <c:set var="active" value="createworkset" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
 
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create a Working Set &nbsp;&nbsp;&nbsp;&nbsp;<small>- <a href="#" onclick="quitWorkset();">Quit this Working Set</a></small></h2>
                    <ul id="steps-bar" class="seg-4">
                        <li class="lastDone">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Select name and projects</dd>
                            </dl>
                        </li>
                        <li class="current">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Select targets</dd>
                            </dl>
                        </li>
                        <li class="last">
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Save Working Set</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <form id="chooseWorkSet" action="createworkset.do?step=3" method="post" >
                        <fieldset>
                            <legend>Select indicators</legend>
                            <table style="width: 800px; font-size: 13px; " >
                                <tr>
                                    <td align="right" width="600px" >
                                        <button onclick="return filterIndicatorById();">Filter by ID</button>
                                    </td>
                                    <td >
                                        <input id="filterById" name="filterById" type="text" size="36" maxlength="40" />
                                    </td>
                                </tr>
                                <tr >
                                    <td align="right">
                                        <button  onclick="return filterIndicatorByName();">Filter by display name</button>
                                    </td>
                                    <td>
                                        <input id="filterByDisplayName" name="filterByDisplayName" type="text" size="36" maxlength="40" />
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <button  onclick="return filterIndicatorByTag();">Filter by tag</button>
                                    </td>
                                    <td>
                                        <input id="filterByTag" name="filterByTag" type="text" size="36" maxlength="40" />
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <button onclick="return showAllIndicators();">Show All</button>
                                    </td>
                                    <td></td>
                                </tr>
                            </table>
                            <table class="tablesorter" id="rtable" name="rtable">
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="checkall" name="checkall" /></th>
                                        <th title="Click to order" width="120px" >Organization &nbsp;&nbsp;&nbsp;&nbsp;</th>
                                        <th title="Click to order" width="120px">Indicator ID</th>
                                        <th title="Click to order">Display Name</th>
                                        <th title="Click to order" width="50">Tags</th>
                                    </tr>
                                </thead>
                                <tfoot>
                                    <tr>
                                        <th></th>
                                        <th>Organization</th>
                                        <th>Indicator ID</th>
                                        <th>Display Name</th>
                                        <th>Tags</th>
                                    </tr>
                                </tfoot>
                                <c:forEach items="${indicators}" var="indicator">
                                    <tr id="indicator-${indicator.id}" class="tdTitle">
                                        <td>
                                            <input type="checkbox" name="indicatorId[]" value="${indicator.id}" <c:if test="${indicator.checked}">checked=""</c:if>/>
                                        </td>
                                        <td class="org">${indicator.orgName}</td>
                                        <td class="indicator-name">${indicator.name}</td>
                                        <td class="indicator-desc">${indicator.questionTitle}</td>
                                        <td class="indicator-tags">${indicator.tag}</td>
                                    </tr>
                                </c:forEach>
                                <tr id="indicator-0" style="<c:if test="${fn:length(indicators) != 0}">display: none; </c:if>height: 10px; width: 100%">
                                    <td colspan="5"></td>
                                </tr>
                            </table>
                            <input type="submit" value="Add to selection preview" onclick="return addSelections();" />
                            <!--input type="submit" value="Add All" onclick="return checkForm();" /-->

                            <div id="preview" align="left" valign="top" style="display:none; margin-top: 10px;" >
                                <div class="preview">Your Selection Preview:</div>
                                <div class="line" ></div>
                                <table id="preview-list">
                                    <!--
                                    <div>
                                        <span class="preview-name">GIR-public-002 (<a href="#"><img src="images/delete.png" height="12px" /></a>)</span>
                                        <span class="preview-desc">In practice are people able to get access to public documents of XYZ type in a reasonable time period?</span>
                                    </div>
                                    -->
                                </table>
                                <input id="indicatorlist" name="indicatorlist" value="" type="hidden"/>
                                <div id="submitButtons"  class="submitButtons" >
                                    <input type="submit" value="NEXT STEP" onclick="return doSubmit();" />
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
