<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : export_data_1
    Created on : Feb 21, 2011, 11:26:15 PM
    Author     : Jeff Jiang
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:bind var="ANONYMIZE_REPORT"  field="ANONYMIZE_REPORT"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="COMMENTS"  field="INCLUDE_AUTHOR_COMMENTS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="REFERNCES"  field="INCLUDE_REFERENCES"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="PEER_REVIEWS"  field="INCLUDE_PEER_REVIEWS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="STAFF_REVIEWS"  field="INCLUDE_STAFF_REVIEWS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="DISCUSSIONS"  field="INCLUDE_DISCUSSIONS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="GLOBAL_INTEGRITY"  field="INCLUDE_GLOBAL_INTEGRITY"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="ATTACHED_FILES"  field="INCLUDE_ATTACHED_FILES"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="ANSWER_LABELS"  field="INCLUDE_ANSWER_LABELS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="SCORING_OPTIONS"  field="INCLUDE_SCORING_OPTIONS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="KEEP_CRLF"  field="KEEP_CRLF"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="INCLUDE_TREE"  field="INCLUDE_TREE"  type="com.ocs.indaba.aggregation.common.Constants"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Create a data export</title>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="js/flexgrid/jquery.flexigrid.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <script type="text/javascript" charset="utf-8" src="js/jquery-1.7.2.js"></script>
        <script type="text/javascript" charset="utf-8" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" charset="utf-8" src="js/common.js"></script>
        <script type="text/javascript" charset="utf-8" src="js/jquery.cookie.js"></script>
        <script type="text/javascript" charset="utf-8" src="js/flexgrid/jquery.flexigrid.js"></script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="scorecardexport" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="box">
                <div class="wrapper">
                    <h3 style="padding: 0px; font-size: 18px;" >Instructions</h3>
                    <div class="instructions">
                        Use this to export any and all data associated with an Indaba scorecard. This tool is only available to Indaba users with permission to download auto-aggregated scorecard files (if you are reading this text, you have that permission).
                        <br/><br/>
                        You can choose to download text fields associated with your questions and answers as well (“author comment” and “source type” are popular choices). Click the Export button.
                        <br/><br/>
                        Indaba will then compile your scorecard into a spreadsheet. For large exports, this might take several minutes, especially if your question lists and number of targets are large. The more fields you choose to include will also lead to larger file size and longer download times, but don’t worry. The site is working -- keep the web page open and your download will start when the file is ready.
                        <br/><br/>
                        The export is a CSV or EXCEL file or a ZIP file that includes a large spreadsheet -- this is your data -- and possibly several small text files. Because spreadsheets do not allow very long text comments, Indaba will put any long comments into a text file. The spreadsheet will replace the comment with the filename -- to read the full comment, see the text file indicated.
                    </div>
                </div>
            </div>
            <div class="interactive">
                <div class="wrapper">
                    <h2>Scorecard Data Export</h2>
                    <ul id="steps-bar" class="seg-3">
                        <li id="step1" class="current">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Select product</dd>
                            </dl>
                        </li>
                        <li id="step2" class="last">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Specify data matrix and download your files</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <div class="step-container" id="step1-box">
                        <form id="selectProduct" action="scorecardReport.do" method="POST" >
                            <fieldset>
                                <legend>&nbsp;&nbsp;Choose a product&nbsp;&nbsp;</legend>
                                <div id="query-result">
                                    <div id="org-filter">
                                        <label for="orgId" style="float:left;">Organization:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                        <select class="longbox" id="orgId" name="orgId" >
                                            <option value="-1">All</option>
                                            <c:forEach items="${orgs}" var="org">
                                                <option value="${org.id}" <c:if test="${orgid eq org.id}">selected</c:if> >${org.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div id="prod-list">
                                        <h3>Products: </h3>
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th class="prod"><a href="#">PRODUCT<span class="asc-inactive"></span></a></th>
                                                    <th class="proj"><a href="#">PROJECT<span class="asc-inactive"></span></a></th>
                                                    <th class="org"><a href="#">ORGANIZATION<span class="asc-inactive"></span></a></th>
                                                    <th class="sp"><a href="#">STUDY PERIOD<span class="asc-inactive"></span></a></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty products}">
                                                        <tr><td colspan="5"></td></tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach items="${products}" var="prod" varStatus="status">
                                                            <tr id="${prod.id}" <c:if test="${status.index%2 == 1}">class="o"</c:if>>
                                                                <td class="c"><input type="radio" name="choice" value="${prod.id}" /></td>
                                                                <td class="prod">${prod.prodName}</td>
                                                                <td class="proj">${prod.projName}</td>
                                                                <td class="org">${prod.orgName}</td>
                                                                <td class="sp">${prod.studyPeriod}</td>
                                                                <c:choose>
                                                                    <c:when test="${prod.isTsc == 1}">
                                                                        <td class="istsc hiddenfromview" >TSC</td>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <td class="istsc hiddenfromview" >BSC</td>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </tr>
                                                        </c:forEach> 
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>
                                    </div>
                                    <!--
                                    <div id="product-flexgrid"></div>
                                    <div id="freeow-br" class="freeow freeow-top-right"></div>
                                    -->
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <div class="step-container" id="step2-box" style="display: none;">
                        <form id="scorecardReport" action="scorecardReport.do" method="POST" >
                            <fieldset>
                                <legend>&nbsp;&nbsp;Specify data matrix&nbsp;&nbsp;</legend>
                                <div>
                                    <h3 class="prod-selected">You are going to export the product:&nbsp;&nbsp;<span class="prod-name"></span><input type="hidden" id="productId" name="productId" value="-1" /></h3>
                                </div>
                                <div><label>Choose the export language:</label>
                                    <select class="middlebox" id="lang" name="lang">
                                        <option value="0">Original</option>
                                        <c:forEach var="lang" items="${languages}">
                                            <option value="${lang.id}">${lang.languageDesc}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div><label>Data to be included in the report:</label></div>
                                <ul id="data-options">
                                    <li><input type="checkbox" class="options" name="anonymizeReport" value="${ANONYMIZE_REPORT}"/><span class="cb-txt">Anonymize report (replaces real names with role identities)</span></li>
                                    <li><input type="checkbox" class="options" name="includeAuthorComments" value="${COMMENTS}"/><span class="cb-txt">Include Author Comments</span></li>
                                    <li><input type="checkbox" class="options" name="includeReferences" value="${REFERNCES}"/><span class="cb-txt">Include References</span></li>
                                    <li><input type="checkbox" class="options" name="includePeerReviews" value="${PEER_REVIEWS}"/><span class="cb-txt">Include Peer Reviews</span></li>
                                    <li><input type="checkbox" class="options" name="includeStaffReviews" value="${STAFF_REVIEWS}"/><span class="cb-txt">Include Private Discussions</span></li>
                                    <li><input type="checkbox" class="options" name="includeDiscussions" value="${DISCUSSIONS}"/><span class="cb-txt">Include General Discussions</span> </li>
                                    <li id="includeGlobalIntegrityItem"><input type="checkbox" class="options" name="includeGlobalIntegrity" id="includeGlobalIntegrity" value="${GLOBAL_INTEGRITY}" /><span class="cb-txt">Include Global Integrity Standard Aggregation (pre-2013 surveys/questionnaires only).</span></li>
                                    <li><input type="checkbox" class="options" name="includeAttachedFiles" value="${ATTACHED_FILES}" /><span class="cb-txt">Include Attached Files</span></li>
                                    <li><input type="checkbox" class="options" name="includeAnswerLabels" value="${ANSWER_LABELS}" /><span class="cb-txt">Include Answer Labels</span></li>
                                    <li><input type="checkbox" class="options" name="includeScoringOptions" value="${SCORING_OPTIONS}" /><span class="cb-txt">Include Scoring Criteria (recommend for surveys with single or multi-choice questions)</span></li>
                                    <li><input type="checkbox" class="options" name="keepCRLF" value="${KEEP_CRLF}" /><span class="cb-txt">Keep Line Breaks in Cells (only if your spreadsheet software can handle Line Breaks properly. EXCEL cannot.)</span></li>
                                    <li id="includeTreeItem"><input type="checkbox" class="options" name="includeTree" id="includeTree" value="${INCLUDE_TREE}" /><span class="cb-txt">Include tree structure of the scorecard (pre-2013 surveys/questionnaires only).</span></li>
                                    <li>
                                        <input type="hidden" name="exportFilename" value="" id="exportFilename" />
                                        <input type="hidden" name="format" value="" id="scorecardReportFormat" />
                                        <%-- <input type="submit" value="Export EXCEL" />  &nbsp; --%>
                                    </li>
                                </ul>
                            </fieldset>
                        </form>
                    </div>
                    <div class="btn-group">
                        <div class="err-box"></div>
                        <a class="btn" id="btn-pre" href="#" style="display:none;">Previous</a>
                        <a class="btn" id="btn-next" href="#">Next</a>
                        <a class="btn" id="btn-export" href="#" style="display:none;">Export CSV</a>
                        <a class="btn" id="btn-export-excel" href="#" style="display:none;">Export EXCEL</a>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
<script type="text/javascript" charset="utf-8">
    $(function(){
        var sortName = '${sortname}';
        var sortType = '${sorttype}';
        $('#productId').val('-1');
        $('div.checker span').removeClass('checked');
        var step1Box = $('#step1-box');
        var step2Box = $('#step2-box');
        var stepsBar = $('#steps-bar');
        $('a#btn-next').click(function(){
            if(parseInt($('#productId').val()) <=0) {
                $('.err-box').text('WARN: You must choose a product!');
                $('.err-box').show();
                return false;
            }
            step1Box.hide();
            step2Box.show();
            $('li', stepsBar).removeClass('current');
            $('li#step1', stepsBar).addClass('lastDone');
            $('li#step2', stepsBar).addClass('current');
            $(this).hide();
            $('a#btn-pre').show();
            $('a#btn-export').show();
            $('a#btn-export-excel').show();
            return false;
        });
        $('a#btn-pre').click(function(){
            step1Box.show();
            step2Box.hide();           
            $('li', stepsBar).removeClass('current');
            $('li#step1', stepsBar).addClass('current');
            $('li#step1', stepsBar).removeClass('lastDone');
            $('a#btn-next').show();
            $('a#btn-export').hide();
            $('a#btn-export-excel').hide();
            return false;
        });
        $("select").chosen();
        $("input:radio").uniform();
        $('a#btn-export').click(function(){
            var prodId = $('#productId').val();
            if(prodId <= 0) {
                $('.err-box').text('WARN: You must choose a product!');
                $('.err-box').show();
                return false;
            }
            var prodName = $('.prod-selected .prod-name').text().trim().replaceAll(' ', '_');
            $('#exportFilename').val('ScorecardExport_' + prodName + ".csv");
            $('#scorecardReportFormat').val('csv');
            $('form#scorecardReport').submit();
            return false;
        });

        $('a#btn-export-excel').click(function(){
            var prodId = $('#productId').val();
            if(prodId <= 0) {
                $('.err-box').text('WARN: You must choose a product!');
                $('.err-box').show();
                return false;
            }
            var prodName = $('.prod-selected .prod-name').text().trim().replaceAll(' ', '_');
            $('#exportFilename').val('ScorecardExport_' + prodName + ".xls");
            $('#scorecardReportFormat').val('excel');
            $('form#scorecardReport').submit();
            return false;
        });

        $('select#orgId').change(function(){
            // $("#product-flexgrid").flexReload({newp: 1, dataType: 'json'});
            window.location = "createScorecardExport.do?orgid=" + $(this).val();
            return false;
        });
        $('input[type="checkbox"]').click(function(){
            var txtElmt = $(this).parent().parent().siblings('span');
            if($(this).parent().attr('class') == 'checked') {
                txtElmt.attr('class', 'cb-txt-selected');
            }else{
                txtElmt.attr('class', 'cb-txt');
            }
            return true;
        });
        $('#prod-list input:radio') .change(function(){
            $('.prod-selected #productId').val($(this).val());
            $('.prod-selected .prod-name').text($('#prod-list #'+$(this).val() + " .prod").text());

            var isTsc = $('#prod-list #'+$(this).val() + " .istsc").text();

            if (isTsc == "BSC") {
                $('#includeTreeItem').hide();
                $('#includeTree').attr("disabled", true);
                $('#includeGlobalIntegrityItem').hide();
                $('#includeGlobalIntegrity').attr("disabled", true);
            } else {
                $('#includeTreeItem').show();
                $('#includeTree').attr("disabled", false);
                $('#includeGlobalIntegrityItem').show();
                $('#includeGlobalIntegrity').attr("disabled", false);
            }

            $('.err-box').hide();
        });
        $('input:radio').each(function(){
            $(this).parent('span').removeClass('checked');
        });


        $('#prod-list table thead th.'+sortName+' a span').attr('class', sortType+'-active');
        $('#prod-list table thead th a').click(function(){
            var sortType = 'asc';
            var sortIcon = $('span', $(this));
            if($(this).parents('th').hasClass(sortName)) {
                if(sortIcon.hasClass('asc-active') || sortIcon.hasClass('desc-inactive')) {
                    sortType = "desc";
                } else {
                    sortType = "asc";
                }
            } else {
                if(sortIcon.hasClass('asc-inactive') || sortIcon.hasClass('desc-active')) {
                    sortType = "asc";
                } else {
                    sortType = "desc";
                }
            }
            var sortName = $(this).parents('th').attr('class');
            var orgId =$('select#orgId').val();
            window.location = "createScorecardExport.do?orgid=" + orgId + "&sortname=" + sortName + "&sorttype=" + sortType;
            return false;
        });
        $('#prod-list table thead th').hover(function(){
            var sortbox = $('span', $(this));
            if(!$(this).hasClass(sortName)) {
                sortbox.attr('class', 'asc-active');
            }
        },function(){
            var sortbox = $('span', $(this));
            if(!$(this).hasClass(sortName)) {
                sortbox.attr('class', 'asc-inactive');
            }
        });
        
    });
</script>
