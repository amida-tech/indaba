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
        <title>Indaba Publisher - Creating aggregation step #1</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script src="js/jquery.alerts.js" type="text/javascript"></script>
        <link href="css/jquery.alerts.css" rel="stylesheet" type="text/css" />

        <script type="text/javascript" charset="utf-8">
            $(function(){
                //$('input, textarea, select, button').uniform();
                /*$('#worksetId').change(function(){
                    // make all workset notes disappeared firstly
                    $('#worksetNotes>div').each(function(){
                        $(this).css("display", "none");
                    });

                    var checkedValue = $('#worksetId').val();
                    var worksetNote = "#worksetNote_" + checkedValue;
                    $(worksetNote).css("display", "block");
                     
                });*/

            });

            function checkName(){
                if($('#aggName').val().empty()) {
                    $('#aggName-warn1').css("display", "block");
                    $('#aggName-warn2').css("display", "none");
                    return false;
                }
                var worksetId = $('#worksetId').val();
                var checkData = "type=name&worksetId=" + worksetId + "&name=" + encodeURIComponent($('#aggName').val());
                var checkResult = false;
                $.ajax({
                    type: "POST",
                    url: "checkaggregation.do",
                    data: checkData,
                    cache: false,
                    async: false,
                    success: function(result) {
                        var json = eval("(" + result + ")");
                        if(json.code == 0){
                            $('#aggName-warn1').css("display", "none");
                            $('#aggName-warn2').css("display", "block");
                        }else{
                            checkResult = 1;
                            $('#aggName-warn1').css("display", "none");
                            $('#aggName-warn2').css("display", "none");
                        }
                    }
                });
                return checkResult;
            }

            function checkShortName(){
                if($('#aggShortName').val().empty()) {
                    $('#aggShortName-warn1').css("display", "block");
                    $('#aggShortName-warn2').css("display", "none");
                    return false;
                }

                var worksetId = $('#worksetId').val();
                var checkData = "type=shortname&worksetId=" + worksetId + "&name=" + encodeURIComponent($('#aggShortName').val());
                var checkResult = false;
                $.ajax({
                    type: "POST",
                    url: "checkaggregation.do",
                    data: checkData,
                    cache: false,
                    async: false,
                    success: function(result) {
                        var json = eval("(" + result + ")");
                        if(json.code == 0){
                            $('#aggShortName-warn1').css("display", "none");
                            $('#aggShortName-warn2').css("display", "block");
                        }else{
                            checkResult = true;
                            $('#aggShortName-warn1').css("display", "none");
                            $('#aggShortName-warn2').css("display", "none");
                        }
                    }
                });
                return checkResult;
            }


            function doSubmit() {
                if(!checkName())
                    return false;

                if(!checkShortName())
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
        <style type="text/css">
            .worksetNotes{
                font-size: 13px;
                font-weight: bold;
                margin-top: 0px;
                padding-left: 0px;
                margin-left: 0px;
                color: red;
            }
        </style>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="createaggr" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create an aggregation &nbsp;&nbsp;&nbsp;&nbsp;<small>- <a href="#" onclick="quitAggregation();">Quit this aggregation</a></small></h2>
                    <ul id="steps-bar" class="seg-4">
                        <li class="current">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Label your aggregated value</dd>
                            </dl>
                        </li>
                        <li>
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
                    <form id="createAgg" action="createaggregation.do?step=2" method="post" >
                        <fieldset>
                            <legend>Label your aggregated value</legend>
                            <table>
                                <tr>
                                    <td>
                                        <ul>
                                            <li>
                                                <label>Working set: ${workset.name}</label>
                                                <!--<select class="middlebox" id="worksetId" name="worksetId" style="width: 290px;" >
                                                    <c:forEach items="${worksets}" var="workset"  varStatus="status">
                                                        <option value="${workset.id}" <c:if test="${workset.id == selectWorkset}">selected</c:if>>${workset.name}</option>
                                                    </c:forEach>
                                                </select>-->
                                            </li>
                                            <li>
                                                <label>Working set notes: ${workset.description}</label>
                                                <!--<div id="worksetNotes" name="worksetNotes" class="worksetNotes">
                                                <c:forEach items="${worksets}" var="workset"  varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${workset.id == selectWorkset}">
                                                                <div id="worksetNote_${workset.id}" name="worksetNote_${workset.id}" style="width: 290px;display: block">
                                                                    ${workset.description}
                                                                </div>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div id="worksetNote_${workset.id}" name="worksetNote_${workset.id}" style="width: 290px;display: none">
                                                                    ${workset.description}
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                </c:forEach>
                                                </div>-->
                                            </li>
                                            <li>
                                                <label>Display name:</label>
                                                <input id="aggName" name="aggName" type="text" size="43" <c:if test="${showOrigin}">value="${sessionScope.aggregation.name}"</c:if>/>
                                                <br/>
                                                <span id="aggName-warn1" class="warn">Must specify aggregation name!</span>
                                                <span id="aggName-warn2" class="warn">Name has been used, please choose another one!</span>
                                            </li>
                                            <li>
                                                <label>Short Name:</label>
                                                <input id="aggShortName" name="aggShortName" type="text" size="43" <c:if test="${showOrigin}">value="${sessionScope.aggregation.shortName}"</c:if>/>
                                                <br/>
                                                <span id="aggShortName-warn1" class="warn">Must specify aggregation short name!</span>
                                                <span id="aggShortName-warn2" class="warn">Short Name has been used, please choose another one!</span>
                                                <br/>
                                            </li>
                                            <li>
                                                <label>Notes:</label>
                                                <textarea id="aggNotes" name="aggNotes" type="text" cols="41" rows="4"><c:if test="${showOrigin}">${sessionScope.aggregation.description}</c:if></textarea>
                                                <br/>
                                            </li>
                                        </ul>
                                    </td>
                                </tr>
                            </table>
                            <div id="submitButtons" class="submitButtons">
                                <input type="submit" value="NEXT STEP" onclick="return doSubmit();" />
                           </div>
                           <input type="hidden" name="worksetId" id="worksetId" value="${workset.id}"/>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
