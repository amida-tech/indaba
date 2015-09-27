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
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Create aggregation step #4</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
        <script src="js/jquery.alerts.js" type="text/javascript"></script>
        <link href="css/jquery.alerts.css" rel="stylesheet" type="text/css" />
        <link type="text/css" rel="stylesheet" href="css/tablesorter/themes/default/style.css" />
        <style type="text/css">
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
            $(function(){
                //$("input").uniform();
            });

            function saveAggregation(){
                var data = "step=5";
                var worksetId = $('#worksetId').val();
                $.ajax({
                    type: "POST",
                    url: "createaggregation.do",
                    data: data,
                    cache: false,
                    async: false,
                    success: function(result) {
                        var json = eval("(" + result + ")");
                        if(json.code == 0){
                            jAlert("", "Save Aggregation Falied", null);
                        }else{
                            //jAlert("", "Save Aggregation Success", null);
                            //$('#submitButtons').hide();
                            var mUrl = "manageaggregation.do?worksetId=" + worksetId;
                            location.href = mUrl;
                        }
                    }
                });
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
                        <li class="done">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Label your aggregated value</dd>
                            </dl>
                        </li>
                        <li class="done">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Create a selection</dd>
                            </dl>
                        </li>
                        <li class="lastDone">
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Choose aggregation method</dd>
                            </dl>
                        </li>
                        <li class="last current">
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Confirm your result</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <fieldset>
                        <legend>Confirm your result</legend>
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
                        <div id="aggselect" align="left" valign="top" style="display:block; margin-top: 20px;" >
                            <table style="width: 100%; font-size: 13px; " id="aggtable">
                                <tr>
                                    <td align="left"><span class="subTitle">Your selection:<a href="createaggregation.do?step=2">(change this)</a></span></td>
                                    <td colspan="2"></td>
                                    <c:if test="${aggMethodView.showWeight == 1}">
                                    <td align="center"><span class="subTitle">Weighting Points</span></td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td colspan="4"><div class="line" ></div></td>
                                </tr>
                                <c:forEach items="${aggIndicators}" var="indicator">
                                    <tr id="${indicator.id}">
                                        <td class="org" align="left">${indicator.orgName}</td>
                                        <td class="indicator-name" align="left">${indicator.name}</td>
                                        <td class="indicator-desc" align="left">${indicator.displayName}</td>
                                        <c:if test="${aggMethodView.showWeight == 1}">
                                            <td align="center">${indicator.weight}</td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <div id="aggMethod" align="left" valign="top" style="display:block; margin-top: 20px;" >
                            <div class="subTitle">Aggregation Method:<a href="createaggregation.do?step=3">(change this)</a></div>
                            <div class="line" ></div>
                            <div>${aggMethodView.name}</div>
                        </div>
                        <c:if test="${showSaveButton}">
                            <div id="submitButtons"  class="submitButtons" align="center" style="clear:both; margin-top: 20px;">
                                <input type="submit" value="Save this aggregation" onclick="return saveAggregation();"/>
                            </div>
                        </c:if>
                        <input type="hidden" name="worksetId" id="worksetId" value="${sessionScope.aggregation.worksetId}"/>
                   </fieldset>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
