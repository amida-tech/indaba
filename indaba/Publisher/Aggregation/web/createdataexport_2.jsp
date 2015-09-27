<%-- 
    Document   : export_data_2
    Created on : 2011-2-27, 20:58:44
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
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
        <link type="text/css" rel="stylesheet" href="css/tablesorter/themes/default/style.css" />
        <script type="text/javascript" src="js/jquery-ui-1.8.6.custom.min.js"></script>
        <script type="text/javascript" charset="utf-8">
            function indicatorIdFilter() {
                var indicatorId = $("#indicatorId").val().toLowerCase();
                $("td.indicator-id").parent("tr").show();
                var count = 0;
                $("tr.even, tr.odd").each(function() {
                    if (indicatorId.length > 0 && $.trim($(this).find("td.indicator-id").text()).toLowerCase().indexOf(indicatorId) < 0) {
                        $(this).find("span").attr("class", "");     // uncheck
                        $(this).hide();                             // filtered
                    } else {
                        $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
                        count++;
                    }
                });
            }

            function indicatorNameFilter() {
                var indicatorName = $("#indicatorDisplayName").val().toLowerCase();
                $("td.indicator-id").parent("tr").show();
                var count = 0;
                $("tr.even, tr.odd").each(function() {
                    if (indicatorName.length > 0 && $.trim($(this).find("td.indicator-name").text()).toLowerCase().indexOf(indicatorName) < 0) {
                        $(this).find("span").attr("class", "");     // uncheck
                        $(this).hide();                             // filtered
                    } else {
                        $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
                        count++;
                    }
                });
            }
            
            function indicatorTagFilter() {
                var indicatorTag = $("#indicatorTag").val().toLowerCase();
                $("td.indicator-id").parent("tr").show();
                var count = 0;
                $("tr.even, tr.odd").each(function() {
                    if (indicatorTag.length > 0 && $.trim($(this).find("td.indicator-tag").text()).toLowerCase().indexOf(indicatorTag) < 0) {
                        $(this).find("span").attr("class", "");     // uncheck
                        $(this).hide();                             // filtered
                    } else {
                        $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
                        count++;
                    }
                });
            }

            function datapointIdFilter() {
                var datapointId = $("#datapointId").val().toLowerCase();
                $("td.datapoint-id").parent("tr").show();
                var count = 0;
                $("tr.even, tr.odd").each(function() {
                    if (datapointId.length > 0 && $.trim($(this).find("td.datapoint-id").text()).toLowerCase().indexOf(datapointId) < 0) {
                        $(this).find("span").attr("class", "");     // uncheck
                        $(this).hide();                             // filtered
                    } else {
                        $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
                        count++;
                    }
                });
            }

            function datapointNameFilter() {
                var datapointName = $("#datapointDisplayName").val().toLowerCase();
                $("td.datapoint-id").parent("tr").show();
                var count = 0;
                $("tr.even, tr.odd").each(function() {
                    if (datapointName.length > 0 && $.trim($(this).find("td.datapoint-name").text()).toLowerCase().indexOf(datapointName) < 0) {
                        $(this).find("span").attr("class", "");     // uncheck
                        $(this).hide();                             // filtered
                    } else {
                        $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
                        count++;
                    }
                });
            }

            function initAutoComplete() {
                
                var len = '${indicatorNames}'.length;
                $("#indicatorId").autocomplete({
                    source: '${indicatorNames}'.substr(2, len-4).split('","'),
                    minLength: 0,
                    change: function() {
                        indicatorIdFilter();
                    }
                });

                len = '${indicatorQuestions}'.length;
                $("#indicatorDisplayName").autocomplete({
                    source: '${indicatorQuestions}'.substr(2, len-4).split('","'),
                    minLength: 0,
                    change: function() {
                        indicatorNameFilter();
                    }
                });

                len = '${indicatorTags}'.length;
                $("#indicatorTag").autocomplete({
                    source: '${indicatorTags}'.substr(2, len-4).split('","'),
                    minLength: 0,
                    change: function() {
                        indicatorTagFilter();
                    }
                });

                len = '${datapointNames}'.length;
                $("#datapointId").autocomplete({
                    source: '${datapointNames}'.substr(2, len-4).split('","'),
                    minLength: 0,
                    change: function() {
                        datapointIdFilter();
                    }
                });

                len = '${datapointDescriptions}'.length;
                $("#datapointDisplayName").autocomplete({
                    source: '${datapointDescriptions}'.substr(2, len-4).split('","'),
                    minLength: 0,
                    change: function() {
                        datapointNameFilter();
                    }
                });
            }

            $(function(){
                initAutoComplete();
                $("table.tablesorter").each(function() {
                    if ($(this).find("tr.odd, tr.even").length > 0) {
                        $(this).tablesorter({headers:{0:{sorter: false}}});
                    }
                });
                $("[name=checkall]").click(function(){
                    var check_status = this.checked;
                    var items;
                    if ($("[type=radio]:checked").val() == "1") {   // raw data
                        items = $("input[name='indicatorId[]']");
                    } else {    // aggregation data
                        items = $("input[name='datapointId[]']");
                    }
                    items.each(function(){
                        if($(this).parents("tr").css("display") != "none") {
                            this.checked = check_status;
                            if(check_status) {
                                $(this).parent("span").attr("class", "checked");
                            } else {
                                $(this).parent("span").attr("class", "");
                            }
                        }
                    });
                });
                toggleList();

                // for session
                if (${fn:length(dataForm.wsIndicatorIds) > 0}) {
                    var indicatorIdsArr = eval("${dataForm.wsIndicatorIds}");
                    $("#indicator tr.even, #indicator tr.odd").each(function() {
                        if (indicatorIdsArr.indexOf(parseInt($(this).find("input").val())) >= 0) {
                            $(this).find("[type=checkbox]").attr("checked", true);
                        }
                    });

                }
                if (${fn:length(dataForm.datapointIds) > 0}) {
                    var datapointIdsArr = eval("${dataForm.datapointIds}");
                    $("#datapoint tr.even, #datapoint tr.odd").each(function() {
                        if (datapointIdsArr.indexOf(parseInt($(this).find("input").val())) >= 0) {
                            $(this).find("[type=checkbox]").attr("checked", true);
                        }
                    });
                        
                }
                addSelections();
            });

            function toggleList() {
                //                resetSelection();
                if ($("[type=radio]:checked").val() == "1") {   // raw data
                    $("[name=indicator]").show();
                    $("[name=datapoint]").hide();
                } else {    // aggregation data
                    $("[name=indicator]").hide();
                    $("[name=datapoint]").show();
                }
            }

            var selectedIndicatorItems = new Array(), selectedDatapointItems = new Array();

            //            function resetSelection() {
            //                selectedIndicatorItems.length = 0;
            //                $("#preview-list").empty();
            //                $("#preview").hide();
            //                $("[name=checkall]").parent("span").attr("class", "");
            //                $("tr.even, tr.odd").each(function() {
            //                    $(this).find("[type=checkbox]").attr("checked", false);
            //                    $(this).find("span").removeClass("checked");
            //                    $(this).show();
            //                });
            //            }

            function addSelection(container, tag, targetArr) {
                var id = $(container).parent("tr").attr("id");
                var idVal = id.replace(tag, "");
                if (targetArr.indexOf(idVal) > -1) {
                    return;
                }
                targetArr.push(idVal);
                var idStr = $.trim($(container).siblings("td." + tag + "id").text());
                var nameStr = $.trim($(container).siblings("td." + tag + "name").text());
                var itagStr = $.trim($(container).siblings("td." + tag + "tag").text());
                var idTag = "<td class='preview-name'>(<a href='#' onclick='return removeSelection(\"" + id +
                    "\")'><img src='images/delete.png' height='12' /></a>) " + idStr + "</td>";
                var nameTag = "<td class='preview-desc'>" + nameStr + "</td>";
                $("#preview-list").append("<tr id='preview-" + id + "'>" + idTag + nameTag + "</tr>");
                $(container).parent("tr").hide();
            }

            function addSelections() {
                $("#indicator td[name=id]:has([type=checkbox]:checked)").each(function() {
                    addSelection(this, "indicator-", selectedIndicatorItems);
                });

                $("#datapoint td[name=id]:has([type=checkbox]:checked)").each(function() {
                    addSelection(this, "datapoint-", selectedDatapointItems);
                });
                
                if(selectedIndicatorItems.length > 0 || selectedDatapointItems.length > 0) {
                    $('#preview').show();
                }
                return false;
            }

            function removeSelection(id) { // id: "indicator-###" or "datapoint-###"
                $("#preview-" + id).remove();
                
                $("#" + id).find("[type=checkbox]").attr("checked", false);
                $("#" + id).find("span").removeClass("checked");
                $("#" + id).show();

                if (id.indexOf("indicator") > -1) {  // raw data
                    selectedIndicatorItems.remove(id.replace("indicator-", ""));
                } else {
                    selectedDatapointItems.remove(id.replace("datapoint-", ""));
                }
                
                if(selectedIndicatorItems.length <= 0 && selectedDatapointItems.length <= 0) {
                    $('#preview').hide();
                }
                return false;
            }

            function doSubmit() {
                $('#selectedIndicatorList').val(selectedIndicatorItems);
                $('#selectedDatapointList').val(selectedDatapointItems);
                return true;
            }
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
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
                        <li class="lastDone">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Choose a working set</dd>
                            </dl>
                        </li>
                        <li class="current">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators/datapoints</dd>
                            </dl>
                        </li>
                        <li>
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
                    <form id="chooseIndicatorOrDatapoint" action="createDataExport.do?step=3" method="post" >
                        <fieldset>
                            <legend>Select indicators/datapoints</legend>
                            <table>
                                <tr><td>
                                        <ul>
                                            <li>
                                                <table style="width: 800px; font-size: 13px; " >
                                                    <tr>
                                                        <td width="370px" >
                                                            <input name="dataType" type="radio" size="43" value="1" onchange="toggleList();" 
                                                                   <c:if test="${dataType == 1}">checked</c:if> /> Raw Data
                                                            </td>
                                                            <td align="right" name="indicator" width="200">
                                                                <button onclick="indicatorIdFilter();return false;">Filter by ID</button>
                                                            </td>
                                                            <td align="right" name="indicator">
                                                                <input id="indicatorId" name="indicatorId" type="text" size="43" maxlength="40" />
                                                            </td>
                                                            <td align="right" name="datapoint" width="200">
                                                                <button onclick="datapointIdFilter();return false;">Filter by ID</button>
                                                            </td>
                                                            <td align="right" name="datapoint">
                                                                <input id="datapointId" name="datapointId" type="text" size="43" maxlength="40" />
                                                            </td>
                                                        </tr>
                                                        <tr >
                                                            <td width="370px">
                                                                <input name="dataType" type="radio" size="43" value="2" onchange="toggleList();"
                                                                <c:if test="${dataType == 2}">checked</c:if> /> Aggregation Data
                                                            </td>
                                                            <td align="right" name="indicator">
                                                                <button style="width: 120px" onclick="indicatorNameFilter();return false;">Filter by name</button>
                                                            </td>
                                                            <td align="right" name="indicator">
                                                                <input id="indicatorDisplayName" name="indicatorName" type="text" size="43" maxlength="40" />
                                                            </td>
                                                            <td align="right" name="datapoint">
                                                                <button style="width: 120px" onclick="datapointNameFilter();return false;">Filter by name</button>
                                                            </td>
                                                            <td align="right" name="datapoint">
                                                                <input id="datapointDisplayName" name="datapointName" type="text" size="43" maxlength="40" />
                                                            </td>
                                                        </tr>
                                                        <tr name="indicator">
                                                            <td width="370px">
                                                            </td>
                                                            <td align="right" name="indicator">
                                                                <button style="width: 120px" onclick="indicatorTagFilter();return false;">Filter by tag</button>
                                                            </td>
                                                            <td align="right" name="indicator">
                                                                <input id="indicatorTag" name="indicatorTag" type="text" size="43" maxlength="40" />
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </li>
                                                <li>
                                                    <table class="tablesorter" id="indicator" name="indicator">
                                                        <thead>
                                                            <tr>
                                                                <th><input type="checkbox" id="checkallIndicator" name="checkall" /></th>
                                                                <th title="Click to order" width="120px" >Organization</th>
                                                                <th title="Click to order" width="120px" >Indicator ID</th>
                                                                <th title="Click to order">Display Name</th>
                                                                <th title="Click to order">Indicator Tags</th>
                                                            </tr>
                                                        </thead>
                                                        <tfoot>
                                                            <tr>
                                                                <th></th>
                                                                <th>Organization</th>
                                                                <th>Indicator ID</th>
                                                                <th>Display Name</th>
                                                                <th>Indicator Tags</th>
                                                            </tr>
                                                        </tfoot>
                                                    <c:forEach items="${indicatorVOList}" var="indicatorVO" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${status.index % 2 == 0}">
                                                                <tr class="even" id="indicator-${indicatorVO.wsIndicatorId}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                <tr class="odd" id="indicator-${indicatorVO.wsIndicatorId}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td name="id">
                                                                <input type="checkbox" name="indicatorId[]" value="${indicatorVO.wsIndicatorId}" />
                                                            </td>
                                                            <td class="org">${organization.name}</td>
                                                            <td class="indicator-id">${indicatorVO.indicator.name}</td>
                                                            <td class="indicator-name" >${indicatorVO.indicator.question}</td>
                                                            <td class="indicator-tag" >${indicatorVO.itags}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                                <table class="tablesorter" id="datapoint" name="datapoint">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox" id="checkallDatapoint" name="checkall" /></th>
                                                            <th title="Click to order" width="120px" >Datapoint ID</th>
                                                            <th title="Click to order">Display Name</th>
                                                        </tr>
                                                    </thead>
                                                    <tfoot>
                                                        <tr>
                                                            <th></th>
                                                            <th>Datapoint ID</th>
                                                            <th>Display Name</th>
                                                        </tr>
                                                    </tfoot>
                                                    <c:forEach items="${datapointList}" var="datapoint" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${status.index % 2 == 0}">
                                                                <tr class="odd" id="datapoint-${datapoint.id}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                <tr class="even" id="datapoint-${datapoint.id}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td name="id">
                                                                <input type="checkbox" name="datapointId[]" value="${datapoint.id}" />
                                                            </td>
                                                            <td class="datapoint-id">${datapoint.name}</td>
                                                            <td class="datapoint-name" >${datapoint.description}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </li>
                                            <li>
                                                <input type="submit" value="Add to selection preview" onclick="return addSelections();" />
                                                <input type="button" value="BACK" onclick="location.href='createDataExport.do?step=1'; return false;" />                                            </li>
                                        </ul>
                                    </td>
                                </tr>
                            </table>

                            <div id="preview" align="left" valign="top" style="display:none; margin-top: 10px;" >
                                <div class="preview">Your Selection Preview:</div>
                                <div class="line" ></div>
                                <table id="preview-list"></table>
                                <input id="selectedIndicatorList" name="selectedIndicatorList" value="" type="hidden" />
                                <input id="selectedDatapointList" name="selectedDatapointList" value="" type="hidden" />
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
