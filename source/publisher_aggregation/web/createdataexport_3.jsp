<%-- 
    Document   : export_data_3
    Created on : 2011-2-27, 21:13:13
    Author     : Jeanbone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Create a data export</title>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/tablesorter/themes/default/style.css" />
        <link type="text/css" href="css/jquery-ui-1.8.6.custom.css" rel="stylesheet" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.6.custom.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
        <script type="text/javascript" src="js/selectTargets.js"></script>
        <script type="text/javascript" charset="utf-8">
            var selectedItems = new Array();
            $(function(){
                $("table.tablesorter").each(function() {
                    if ($(this).find("tr.odd, tr.even").length > 0) {
                        $(this).tablesorter({headers:{0:{sorter: false}}});
                    }
                });
                initCheckAll();
                $("#targetName").autocomplete({
                    source: eval('(${targetNames})'),
                    minLength: 0,
                    change: function() {
                        targetFilter();
                    }
                });

                togglePreviewList();
                if (${fn:length(dataForm.targetIds) > 0}) {
                    var targetIdsArr = eval("${dataForm.targetIds}");
                    $("tr.even, tr.odd").each(function() {
                        if (targetIdsArr.indexOf(parseInt($(this).find("input").val())) >= 0) {
                            $(this).find("[type=checkbox]").attr("checked", true);
                        }
                    });
                    addSelections();
                }
            });
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="dataexport" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <jsp:include page="dataexportinstructions.jsp" flush="true" />
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create a data export</h2>
                    <ul id="steps-bar" class="seg-5">
                        <li class="done">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Choose a working set</dd>
                            </dl>
                        </li>
                        <li class="lastDone">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators/datapoints</dd>
                            </dl>
                        </li>
                        <li class="current">
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Select targets</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Save study periods</dd>
                            </dl>
                        </li>
                        <li class="last">
                            <dl>
                                <dt>Step 5:</dt>
                                <dd>Download your files</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <form id="chooseTargets" action="createDataExport.do?step=4" method="post" >
                        <fieldset>
                            <legend>Select targets</legend>
                            <table>
                                <tr><td>
                                        <ul>
                                            <li>
                                                <table style="width: 800px; font-size: 13px; " >
                                                    <tr>
                                                        <td width="500px" >
                                                            <!--                                                            <input name="dataType" type="radio" size="43" value="1" /> Raw Data-->
                                                        </td>
                                                        <td align="right" width="200">
                                                            <button onclick="targetFilter();return false;">Filter by Name</button>
                                                        </td>
                                                        <td align="right" >
                                                            <input id="targetName" type="text" size="29" maxlength="26" />
                                                        </td>
                                                    </tr>
                                                    <tr >
                                                        <td width="500px">
                                                            <!--                                                            <input name="dataType" type="radio" size="43" value="2" /> Aggregation Data-->
                                                        </td>
                                                        <td align="right">
                                                            <button style="width: 120px" onclick="typeFilter();return false;">Filter by Type</button>
                                                        </td>
                                                        <td align="right">
                                                            <select class="middlebox" onchange="typeFilter();" id="targetType">
                                                                <c:forEach items="${targetTypes}" var="type" varStatus="status">
                                                                    <c:choose>
                                                                        <c:when test="${status.last}">
                                                                            <option value="${status.index}" selected>${type}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${status.index}">${type}</option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="500px">
                                                            <!--                                                            <input name="dataType" type="radio" size="43" value="3" /> Both Data-->
                                                        </td>
                                                        <td align="right">
                                                            <button style="width: 120px" onclick="tagFilter();return false;">Filter by Tag</button>
                                                        </td>
                                                        <td align="right">
                                                            <select class="middlebox" onchange="tagFilter();" id="targetTag">
                                                                <c:forEach items="${targetTags}" var="tag" varStatus="status">
                                                                    <c:choose>
                                                                        <c:when test="${status.last}">
                                                                            <option value="${status.index}" selected>${tag}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${status.index}">${tag}</option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </li>
                                            <li>
                                                <table class="tablesorter">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox" id="checkall" /></th>
                                                            <th title="Click to order">Target Name</th>
                                                            <th title="Click to order">Target Type</th>
                                                            <th title="Click to order">Target Tag</th>
                                                        </tr>
                                                    </thead>
                                                    <tfoot>
                                                        <tr>
                                                            <th></th>
                                                            <th title="Click to order">Target Name</th>
                                                            <th title="Click to order">Target Type</th>
                                                            <th title="Click to order">Target Tag</th>
                                                        </tr>
                                                    </tfoot>
                                                    <c:forEach items="${targets}" var="target" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${status.index % 2 == 0}">
                                                                <tr class="even" id="tr_${target.target.id}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                <tr class="odd" id="tr_${target.target.id}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td name="id">
                                                                <input type="checkbox" class="target-id" value="${target.target.id}" />
                                                            </td>
                                                            <td class="target-name">
                                                                ${target.target.name}
                                                            </td>
                                                            <td class="target-type">
                                                                ${targetTypes[target.target.targetType]}
                                                            </td>
                                                            <td class="target-tag">
                                                                ${target.tags}
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </li>
                                            <li>
                                                <input type="submit" value="Add to selection preview" onclick="return addSelections();" />
                                                <input type="button" value="BACK" onclick="location.href='createDataExport.do?step=2'; return false;" />
                                            </li>
                                        </ul>
                                    </td>
                                </tr>
                            </table>

                            <div id="preview" align="left" valign="top" style="display:block; margin-top: 10px;" >
                                <div class="preview">Your Selection Preview:</div>
                                <div style="border-top:2px solid #cccccc;margin-top: 5px; height: 5px;overflow:hidden; margin-right: 70px;"></div>
                                <table id="preview-list"></table>
                                <input type="hidden" id="targetList" name="targetIds" value="" />
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
