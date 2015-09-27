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
        <title>Indaba Publisher - Create aggregation step #2</title>
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
            .subTitle{
                font-size: 16px;
                font-weight: bold;
                margin-top: 0px;
                padding-left: 0px;
                margin-left: 0px;
                /*    text-align: left;*/
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
                    $("input[name='indicator-checkbox']").each(function(){
                        var tr = $(this).parent("span").parent("div").parent("td").parent("tr");
                        if(tr.css("display") == "none"){
                            return;
                        }
                        this.checked = check_status;
                        if(check_status) {
                            $(this).parent("span").attr("class", "checked");
                        } else {
                            $(this).parent("span").attr("class", "");
                        }
                    });
                });

                <c:if test="${showOrigin}">
                    addSelections();
                </c:if>
            });

            function addSelections() {
                $("td>div.checker>span.checked").each(function(){
                    //$(this).parent("span").attr("class", "checked");
                    var id = $(this).parent("div").parent("td").parent("tr").attr("id");
                    //var idVal = id.replace("indicator-", "");
                    var idVal = id;
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

                //if(selectedIndicators.length >= size) {
                //    $('#indicator-0').css("display", "table-row");
               // }

                return false;
            }

            function removeSelection(id) { // id: "indicator-###"
                $("#preview-" + id).remove();
                $("#input-" + id).check = false;
                $("#input-" + id).parent("span").attr("class", "");
                //selectedIndicators.remove(id.replace("indicator-", ""));
                selectedIndicators.remove(id);
                if(selectedIndicators.length <= 0) {
                    $('#preview').hide();
                }

                //if(selectedIndicators.length != size) {
                //    $('#indicator-0').css("display", "none");
                //}

                return false;
            }
            function doSubmit() {
                if(selectedIndicators.length < 1){
                    alert("You must choose more than one indicator")
                    return false;
                }
                $('#indicatorlist').val(selectedIndicators);
            }

            function filterIndicator(type, keyword, offset){
                $("#rtable .tdTitle").each(function(){
                    var indicatorName = $(this).find('td').eq(offset).html().toLowerCase();
                    if(type == 1){
                        var name = this.id.substring(0, 9);
                        if(name == "indicator"){
                            if(indicatorName.indexOf(keyword, 0) < 0)
                                $(this).hide();
                            else
                                $(this).show();
                        }
                        else
                            $(this).hide();
                    }else if(type == 2){
                        var name = this.id.substring(0, 9);
                        if(name == "datapoint"){
                            if(indicatorName.indexOf(keyword, 0) < 0)
                                $(this).hide();
                            else
                                $(this).show();
                        }
                        else
                            $(this).hide();
                    }else if(type == 3){
                        if(indicatorName.indexOf(keyword, 0) < 0)
                            $(this).hide();
                        else
                            $(this).show();
                    }
                });
                return false;
            }

            function filterIndicatorById(){
                var type = $('input[name=indicatorType]:checked').val();
                var keyword = $("#filterById").val().toLowerCase();
                var offset = 2;
                return filterIndicator(type, keyword, offset);
            }

            function filterIndicatorByName(){
                var type = $('input[name=indicatorType]:checked').val();
                var keyword = $("#filterByDisplayName").val().toLowerCase();
                var offset = 3;
                return filterIndicator(type, keyword, offset);
            }

            function filterIndicatorByTag(){
                var type = $('input[name=indicatorType]:checked').val();
                var keyword = $("#filterByTag").val().toLowerCase();
                var offset = 4;
                return filterIndicator(type, keyword, offset);
            }

            function filterIndicatorType(type){
                if(type == 3){
                    $("#rtable .tdTitle").each(function(){
                        $(this).show();
                    });
                }else if(type == 1){
                    $("#rtable .tdTitle").each(function(){
                        var name = this.id.substring(0, 9);
                        if(name == "indicator")
                            $(this).show();
                        else
                            $(this).hide();
                    });
                }else if(type == 2){
                    $("#rtable .tdTitle").each(function(){
                        var name = this.id.substring(0, 9);
                        if(name == "datapoint")
                            $(this).show();
                        else
                            $(this).hide();
                    });
                }
            }

            function showAllIndicators(){
                var indicatorType = $('input[name=indicatorType]:checked').val();
                filterIndicatorType(indicatorType)
                return false;
            }

            function quitAggregation(){
                jConfirm("Your work has not been saved. Are you sure to quit", "Confirm Quit Aggregation", function(r) {
                    if (r){
                        var worksetId = $('#worksetId').val();
                        var toUrl = "quitaggregation.do?worksetId=" + worksetId;
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
            <c:set var="active" value="createaggr" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create an aggregation &nbsp;&nbsp;&nbsp;&nbsp;<small>- <a href="#" onclick="quitAggregation();">Quit this aggregation</a></small></h2>
                    <ul id="steps-bar" class="seg-4">
                        <li class="lastDone">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Label your aggregated value</dd>
                            </dl>
                        </li>
                        <li class="current">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Create a selection</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Choose aggregation method</dd>
                            </dl>
                        </li>
                        <li class="last">
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Confirm your result</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <fieldset>
                        <legend>Create a selection</legend>
                        <div id="agglabel" align="left" valign="top" style="display:block; margin-top: 10px;" >
                            <div class="subTitle">Label:<a href="createaggregation.do?step=1">(change this)</a></div>
                            <div class="line" ></div>
                            <ul>
                                <li>
                                    <label>Display Name:${aggName}</label>
                                </li>
                               <li>
                                    <label>Short Name:${aggShortName}</label>
                                </li>
                                <li>
                                    <label>Working set:${worksetName}</label>
                                </li>
                            </ul>
                        </div>
                        <form id="createAgg" action="createaggregation.do?step=3" method="post" >
                            <div id="aggSelect" align="left" valign="top" style="display:block; margin-top: 20px;" >
                                <div class="subTitle">Create selection from:</div>
                                <div class="line" ></div>
                                <table style="width: 800px; font-size: 13px; " >
                                    <tr>
                                        <td align="left">
                                            <input id="rawIndicatorType" name="indicatorType" type="radio" value="1" onclick="filterIndicatorType(1);"/> Raw Data
                                        </td>
                                        <td align="right" width="420px" >
                                            <button onclick="return filterIndicatorById();">Filter by ID</button>
                                        </td>
                                        <td >
                                            <input id="filterById" name="filterById" type="text" size="36" maxlength="40" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left">
                                            <input id="aggIndicatorType" name="indicatorType" type="radio" value="2" onclick="filterIndicatorType(2);"/> Aggregation Data
                                        </td>
                                        <td align="right">
                                            <button onclick="return filterIndicatorByName();">Filter by display name</button>
                                        </td>
                                        <td>
                                            <input id="filterByDisplayName" name="filterByDisplayName" type="text" size="36" maxlength="40" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left">
                                            <input id="bothIndicatorType" name="indicatorType" type="radio" value="3"  checked onclick="filterIndicatorType(3);"/> Both
                                        </td>
                                        <td align="right">
                                            <button  onclick="return filterIndicatorByTag();">Filter by tag</button>
                                        </td>
                                        <td>
                                            <input id="filterByTag" name="filterByTag" type="text" size="36" maxlength="40" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td align="right">
                                            <button onclick="return showAllIndicators();">Show All</button>
                                        </td>
                                        <td></td>
                                    </tr>
                                </table>
                                <table class="tablesorter" name="rtable" id="rtable">
                                    <thead>
                                        <tr>
                                            <th><input type="checkbox" id="checkall" name="checkall" /></th>
                                            <th title="Click to order" width="120px" >Organization &nbsp;&nbsp;&nbsp;&nbsp;</th>
                                            <th title="Click to order" width="120px">Indicator ID</th>
                                            <th title="Click to order">Display Name</th>
                                            <th title="Click to order">Tags</th>
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
                                    <c:forEach items="${aggIndicators}" var="indicator">
                                        <tr id="${indicator.id}" class="tdTitle">
                                            <td>
                                                <input type="checkbox" name="indicator-checkbox" id="input-${indicator.id}" <c:if test="${indicator.checked}">checked=""</c:if> />
                                            </td>
                                            <td class="org">${indicator.orgName}</td>
                                            <td class="indicator-name">${indicator.name}</td>
                                            <td class="indicator-desc">${indicator.displayName}</td>
                                            <td class="indicator-tag">${indicator.tag}</td>
                                        </tr>
                                    </c:forEach>
                                    <!--<tr id="indicator-0" style="<c:if test="${fn:length(indicators) != 0}">display: none; </c:if>height: 10px; width: 100%">
                                        <td colspan="4"></td>
                                    </tr>-->
                                </table>
                                <input type="submit" value="Add to selection preview" onclick="return addSelections();" />
                            </div>

                            <div id="preview" align="left" valign="top" style="display:none; margin-top: 10px;" >
                                <div class="subTitle">Your Selection Preview:</div>
                                <div class="line" ></div>
                                <table id="preview-list">
                                </table>
                                <input id="indicatorlist" name="indicatorlist" value="" type="hidden"/>
                                <div id="submitButtons"  class="submitButtons" >
                                    <input type="submit" value="NEXT STEP" onclick="return doSubmit();" />
                                </div>
                            </div>
                            <input type="hidden" name="worksetId" id="worksetId" value="${sessionScope.aggregation.worksetId}"/>
                        </form>
                   </fieldset>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
