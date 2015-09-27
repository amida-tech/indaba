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
        <title>Indaba Publisher - Create Working Set Step #3</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
        <script src="js/jquery.alerts.js" type="text/javascript"></script>
        <link href="css/jquery.alerts.css" rel="stylesheet" type="text/css" />
        <link type="text/css" rel="stylesheet" href="css/tablesorter/themes/default/style.css" />

        <script type="text/javascript" charset="utf-8">
            var selectedTargets = new Array();
            var size = ${fn:length(targets)};
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
                    $("input[name='targetId[]']").each(function(){
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
            });

            function addSelections() {
                $("td>div.checker>span.checked").each(function(){
                    var id = $(this).parent("div").parent("td").parent("tr").attr("id");
                    var idVal = id.replace("target-", "");

                    if(selectedTargets.indexOf(idVal) != -1) {
                        return;
                    }
                
                    selectedTargets.push(idVal);
                    var targetName = $.trim($(this).parent("div").parent("td").siblings("td.target-name").text());
                    var targetNameTag = "(<a href='#' onclick='return removeSelection(\"" + id + "\")'><img src='images/delete.png' height='12' /></a>) " + targetName;
                    
                    $("#preview-list").append("<div id='preview-" + id + "'>" + targetNameTag + "</div>");
                    $(this).attr("class", "");
                    $(this).parent("div").parent("td").parent("tr").hide();

                });

                if(selectedTargets.length > 0) {
                    $('#preview').show();
                }

                if(selectedTargets.length >= size) {
                    $('#target-0').css("display", "table-row");
                }
                
                return false;
            }
            function removeSelection(id) { // id: "target-###"
                $("#preview-" + id).remove();
                $("#" + id).show();
                selectedTargets.remove(id.replace("target-", ""));
                if(selectedTargets.length <= 0) {
                    $('#preview').hide();
                }

                if(selectedTargets.length != size) {
                    $('#target-0').css("display", "none");
                }

                return false;
            }
            function doSubmit() {
                $('#targetlist').val(selectedTargets);
            }

            function filterTargetByName(){
                var keyword = $("#filterByName").val().toLowerCase();
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

            function filterTargetByTag(){
                var keyword = $("#filterByTag").val().toLowerCase();
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

            function filterTargetByType(){
                var keyword = $("#targetTypes").find("option:selected").text();
                 $("#rtable .tdTitle").each(function(){
                    var value = $(this).find('td').eq(2).html();
                    if(value.indexOf(keyword, 0) < 0){
                        $(this).hide();
                    }else{
                        $(this).show();
                    }
                 });
                return false;
            }

            function showAllTargets(){
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
                        <li class="done">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Select name and projects</dd>
                            </dl>
                        </li>
                        <li class="lastDone">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators</dd>
                            </dl>
                        </li>
                        <li class="current">
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
                    <form id="chooseWorkSet" action="createworkset.do?step=4" method="post" >
                        <fieldset>
                            <legend>Select targets</legend>
                            <table style="width: 800px; font-size: 13px; " >
                                <tr>
                                    <td align="right" width="600px" >
                                        <button onclick="return filterTargetByName();">Filter by Name</button>
                                    </td>
                                    <td >
                                        <input id="filterByName" name="filterByName" type="text" size="36" maxlength="40" />
                                    </td>
                                </tr>
                                <tr >
                                    <td align="right">
                                        <button onclick="return filterTargetByType();">Filter by type</button>
                                    </td>
                                    <td>
                                        <select class="middlebox" id="targetTypes" name="targetTypes">
                                            <option selected>Country</option>
                                            <option>International Region</option>
                                            <option>Sub-national: Province</option>
                                            <option>Sub-national: State</option>
                                            <option>Sub-national: Region</option>
                                            <option>Sub-national: City/Municipality</option>
                                            <option>Organization</option>
                                            <option>Government Unit/Project</option>
                                            <option>Sector</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr >
                                    <td align="right">
                                        <button onclick="return filterTargetByTag();">Filter by tag</button>
                                    </td>
                                    <td>
                                        <input id="filterByTag" name="filterByTag" type="text" size="36" maxlength="40" />
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <button onclick="return showAllTargets();">Show All</button>
                                    </td>
                                    <td></td>
                                </tr>
                            </table>
                            <table class="tablesorter" id="rtable" name="rtable">
                                <thead>
                                    <tr>
                                        <th>
                                            <input type="checkbox" id="checkall" name="checkall" />
                                        </th>
                                        <th>Target Name</th>
                                        <th>Target Type</th>
                                        <th>Target Tags</th>
                                    </tr>
                                </thead>
                                <tfoot>
                                    <tr>
                                        <th></th>
                                        <th>Target Name</th>
                                        <th>Target Type</th>
                                        <th>Target Tags</th>
                                    </tr>
                                </tfoot>
                                <c:forEach items="${targets}" var="target">
                                    <tr id="target-${target.targetId}" class="tdTitle">
                                        <td>
                                            <input type="checkbox" name="targetId[]" value="${target.targetId}" <c:if test="${target.checked}">checked=""</c:if>/>
                                        </td>
                                        <td class="target-name">${target.targetName}</td>
                                        <td>${target.typeName}</td>
                                        <td>${target.tag}</td>
                                    </tr>
                                </c:forEach>
                                <tr id="target-0" style="<c:if test="${fn:length(targets) != 0}">display: none; </c:if>height: 10px; width: 100%">
                                    <td colspan="4"></td>
                                </tr>
                            </table>
                            <input type="submit" value="Add to selection preview" onclick="return addSelections();" />


                            <div id="preview" align="left" valign="top" style="display:none; margin-top: 10px;" >
                                <div class="preview">Your Selection Preview:</div>
                                <div class="line" ></div>
                                <div id="preview-list">
                                    <!--
                                    <div>
                                        <span class="preview-name">GIR-public-002 (<a href="#"><img src="images/delete.png" height="12px" /></a>)</span>
                                        <span class="preview-desc">In practice are people able to get access to public documents of XYZ type in a reasonable time period?</span>
                                    </div>
                                    -->
                                </div>
                                <input id="targetlist" name="targetlist" value="" type="hidden"/>
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
