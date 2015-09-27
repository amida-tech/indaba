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

<un:bind var="COMMENTS"  field="INCLUDE_AUTHOR_COMMENTS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="REFERNCES"  field="INCLUDE_REFERENCES"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="PEER_REVIEWS"  field="INCLUDE_PEER_REVIEWS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="STAFF_REVIEWS"  field="INCLUDE_STAFF_REVIEWS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="DISCUSSIONS"  field="INCLUDE_DISCUSSIONS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="GLOBAL_INTEGRITY"  field="INCLUDE_GLOBAL_INTEGRITY"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="ATTACHED_FILES"  field="INCLUDE_ATTACHED_FILES"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="ANSWER_LABELS"  field="INCLUDE_ANSWER_LABELS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<un:bind var="SCORING_OPTIONS"  field="INCLUDE_SCORING_OPTIONS"  type="com.ocs.indaba.aggregation.common.Constants"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Create a data export</title>
        <script type="text/javascript" src="js/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" charset="utf-8">
            $(function(){
                $('input[value="Export EXCEL"]').click(function(){
                    var prodId = $('#productId').val();
                    if(prodId <= 0) {
                        if($('.error').size() == 0) {
                            $('<span class="error">ERROR: You haven\'t choose a product!</span>').appendTo($('label[for="productId"]').parent());
                        }
                        $('#productId').focus();
                        return false;
                    } else {
                        $('.error').remove();
                    }
                
                    var prodName = $('option[value="'+ prodId +'"]').text().trim().replaceAll(' ', '_');
                    //$('#scorecardReport').attr('action', 'scorecardReport.xls'); 
                    $('#exportFilename').val('ScorecardExport_' + prodName + ".xls");
                    $('#scorecardReportFormat').val('excel'); 
                    return true;
                });
                $('input[value="Export CSV"]').click(function(){
                    var prodId = $('#productId').val();
                    if(prodId <= 0) {
                        if($('.error').size() == 0) {
                            $('<span class="error">ERROR: You haven\'t choose a product!</span>').appendTo($('label[for="productId"]').parent());
                        }
                        $('#productId').focus();
                        return false;
                    } else {
                        $('.error').remove();
                    }
                    var prodName = $('option[value="'+ prodId +'"]').text().trim().replaceAll(' ', '_');
                    //$('#scorecardReport').attr('action', 'scorecardReport.csv'); 
                    $('#exportFilename').val('ScorecardExport_' + prodName + ".csv");
                    $('#scorecardReportFormat').val('csv'); 
                    return true;
                });
                $('input[type="checkbox"]').click(function(){
                    var txtElmt = $(this).parent().parent().siblings('span');
                    if($(this).parent().attr('class') == 'checked') {
                        txtElmt.attr('class', 'cb-txt');
                    }else{
                        txtElmt.attr('class', 'cb-txt-selected');
                    }
                    return true;
                });
                $('#productId').change(function(){
                    if($('#productId').val() > 0) {
                        $('.error').remove();
                    } 
                }); 
            });
            function validate() {
            }
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
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
                        You can choose to download text fields associated with your indicator questions and answers as well (“author comment” and “references” are popular choices). Choose Excel (XLS) or CSV format, and click the button.
                        <br/><br/>
                        Indaba will then compile your scorecard into a spreadsheet. For large exports, this might take several minutes, especially if your question lists and number of targets are large. The more text fields you choose to include will also lead to larger file size and longer download times, but don’t worry. The site is working -- keep the web page open and your download will start when the file is ready.
                        <br/><br/>
                        The export is a ZIP file which includes a large spreadsheet -- this is your data -- and possibly several small text files. Because spreadsheets do not allow very long text comments, Indaba will put any long comments into a text file. The spreadsheet will replace the comment with the filename -- to read the full comment, see the text file indicated.
                    </div>
                </div>
            </div>
            <div class="interactive">
                <div class="wrapper">
                    <h2>Scorecard Data Export</h2>
                    <c:choose>
                        <c:when test="${empty products}">
                            <div id="no-data-msg">
                                Sorry, there is no export data available!
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form id="scorecardReport" action="scorecardReport.do" method="POST" >
                                <fieldset>
                                    <legend> Choose a product </legend>
                                    <ul>
                                        <li>
                                            <label for="productId">Product:</label>
                                            <select class="middlebox" id="productId" name="productId">
                                                <option value="-1">-- Choose product --</option>
                                                <c:forEach var="prod" items="${products}">
                                                    <option value="${prod.productId}">${prod.productName}</option>
                                                </c:forEach>
                                            </select>
                                        </li>
                                        <li>
                                            <label>Data to be included in the report:</label>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeAuthorComments" value="${COMMENTS}"/><span class="cb-txt">Include Author Comments</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeReferences" value="${REFERNCES}"/><span class="cb-txt">Include References</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includePeerReviews" value="${PEER_REVIEWS}"/><span class="cb-txt">Include Peer Reviews</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeStaffReviews" value="${STAFF_REVIEWS}"/><span class="cb-txt">Include Staff Reviews</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeDiscussions" value="${DISCUSSIONS}"/><span class="cb-txt">Include Staff/Author Discussions</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeGlobalIntegrity" value="${GLOBAL_INTEGRITY}" /><span class="cb-txt">Include Global Integrity Standard Aggregation</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeAttachedFiles" value="${ATTACHED_FILES}" /><span class="cb-txt">Include Attached Files</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeAnswerLabels" value="${ANSWER_LABELS}" /><span class="cb-txt">Include Answer Labels</span>
                                        </li>
                                        <li>
                                            <input type="checkbox" name="includeScoringOptions" value="${SCORING_OPTIONS}" /><span class="cb-txt">Include Scoring Options</span>
                                        </li>
                                        <li>
                                            <input type="hidden" name="exportFilename" value="" id="exportFilename" />
                                            <input type="hidden" name="format" value="" id="scorecardReportFormat" />
                                            <%-- <input type="submit" value="Export EXCEL" />  &nbsp; --%>
                                            <input type="submit" value="Export CSV" />
                                        </li>
                                    </ul>
                                </fieldset>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
