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
        <title>Indaba Publisher - Create aggregation step #3</title>
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
            .methodDiv{
                float: left;
                width: 50%;
                text-align:center;
            }
            .validateDiv{
                text-align: center;
                margin-left: 50%;
                }
        </style>
        <script type="text/javascript" charset="utf-8">
            var selectedIndicators = new Array();
            <c:choose>
                <c:when test="${showWeights}">
                    var useWeight = 1;
                </c:when>
                <c:otherwise>
                    var useWeight = 0;
                </c:otherwise>
            </c:choose>
            $(function(){
                //$("input").uniform();
            });

            function radioChange(value, showWeight){
                if(showWeight){
                    useWeight = 1;
                    $("input[name='aggWeight']").each(function(){
                        this.value = "";
                        this.disabled = false;
                    });
                }else{
                    useWeight = 0;
                    $("input[name='aggWeight']").each(function(){
                        this.value = "";
                        this.disabled = true;
                    });
                }
            }

            function checkWeight(value){
                var regNum =/^\d*$/;
                if(!regNum.test(value)){
                    alert("Please Input Numeric Characters");
                }
            }
            
            function doSubmit() {
                if(useWeight == 0)
                    return;
                
                var inWeights = "";
                var regNum =/^\d*$/;
                var count = 0;
                var fillall = 0;
                var validated = 0;
                $("input[name='aggWeight']").each(function(){
                    count++;
                    if(this.disabled){
                        fillall++;
                    }
                    if(this.value == ""){
                        return;
                    }
                    fillall++;
                    
                    if(!regNum.test(this.value)){
                        return;
                    }
                    validated++;
                    inWeights += this.value;
                    inWeights += ",";
                });

                if(fillall != count){
                    alert("Must Fill All Weighting Points");
                    return false;
                }

                if(validated != count){
                    alert("Weighting Points Must Be numeric");
                    return false;
                }else{
                    $('#aggWeights').val(inWeights);
                }
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
                    <h2>Create an aggregation &nbsp;&nbsp;&nbsp;&nbsp;<small>- <a href="#" onclick="quitAggregation()">Quit this aggregation</a></small></h2>
                    <ul id="steps-bar" class="seg-4">
                        <li class="done">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Label your aggregated value</dd>
                            </dl>
                        </li>
                        <li class="lastDone">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Create a selection</dd>
                            </dl>
                        </li>
                        <li class="current">
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
                        <legend>Choose aggregation method</legend>
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
                                    <td align="left"><span class="subTitle">Weighting Points</span></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><div class="line" ></div></td>
                                </tr>
                                <c:forEach items="${aggIndicators}" var="indicator">
                                    <tr id="${indicator.id}">
                                        <td class="org" align="left">${indicator.orgName}</td>
                                        <td class="indicator-name" align="left">${indicator.name}</td>
                                        <td class="indicator-desc" align="left">${indicator.displayName}</td>
                                        <td align="left">
                                            <c:choose>
                                                <c:when test="${showWeights}">
                                                    <input type="text" name="aggWeight" value="${indicator.weight}"></input>
                                                </c:when>
                                                <c:otherwise>
                                                <input type="text" name="aggWeight" value="n/a" disabled="disabled"></input>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <form id="createAgg" action="createaggregation.do?step=4" method="post" >
                            <div align="left" valign="top" style="display:block; margin-top: 30px;">
                                <div class="methodDiv">
                                    <div class="subTitle" align="left">Aggregation math applied to selection:</div>
                                    <table>
                                    <c:forEach items="${methods}" var="method"  varStatus="status">
                                        <tr>
                                            <td NOWRAP valign="top" align="left" style="padding-left:50px">
                                                <input type="radio" id="aggMethod" onchange="radioChange(${method.id}, ${method.showWeight})" name="aggMethod" value="${method.id}" <c:if test="${method.id == selectMethod}">CHECKED</c:if> ></input>${method.name}
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </table>
                                </div>
                                <div class="validateDiv">
                                    <div class="subTitle" align="left">Source validation:</div>
                                    <table>
                                        <tr>
                                            <td NOWRAP valign="top" align="left" style="padding-left:50px">
                                                <input type="radio" name="aggValidate" value="1" <c:if test="${selectHole == 1}">CHECKED</c:if> ></input>Strict(Strongly Recommended)
                                            </td>
                                        </tr>
                                        <tr>
                                            <td NOWRAP valign="top" align="left" style="padding-left:50px">
                                                <input type="radio" name="aggValidate" value="2" <c:if test="${selectHole == 2}">CHECKED</c:if>></input>Casual
                                            </td>
                                        </tr>
                                        <tr>
                                            <td NOWRAP valign="top" align="left" style="padding-left:50px">In the "strict" algorithm, aggregate scores will not be</td>
                                        </tr>
                                        <tr>
                                            <td NOWRAP valign="top" align="left" style="padding-left:50px">created when source data is missing</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <input id="aggWeights" name="aggWeights"  type="hidden"/>
                            <div id="submitButtons"  class="submitButtons" align="center" style="clear:both; margin-top: 20px;">
                                <input type="submit" value="NEXT STEP" onclick="return doSubmit();" />
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
