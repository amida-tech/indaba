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
        <title>Indaba Data Services API</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/common.js"></script>

        <style type="text/css">
            .action {
                font-weight: bold;
            }
            .param {
                font-weight: bold;
            }
            .must {
                color: orange;
            }
            .option {
                color: #5f5f5f;
            }
            .section {
                margin-bottom: 10px;
            }
            .note {
                color: #5f5f5f;
                background-color:lightgray;
                font-size: 12px;
                margin-right: 10px;
                margin-bottom: 10px;
                padding: 10px;
            }
            .warn {
                color: orange;
                display: none;
            }

            #params {
                padding: 10px 0px 10px 10px;
            }

            #param-note {
                margin: 0px 20px 10px 0px;
                padding: 5px 5px 5px 5px;
                color: #3f3f3f;
                background-color: #DEDEDE
            }
            a.help {
                position: relative;
                top: 6px;
            }
        </style>
        <script type="text/javascript" charset="utf-8" src="js/upload/fileuploader.js"></script>
        <link rel="stylesheet" href="js/messi/messi.min.css" />
        <script type="text/javascript" charset="utf-8" src="js/messi/messi.min.js"></script>

        <script type='text/javascript' charset="utf-8" src="js/collapsible/jquery.collaps.js"></script>
        <script type='text/javascript' charset="utf-8" src="js/collapsible/jquery.collaps.min.js"></script>
        <link rel="stylesheet" type="text/css" href="js/collapsible/default.css"/>

        <script type="text/javascript" charset="utf-8">
            $(function(){
                $('input, textarea, select, button').uniform();
                //isMuliInt("1+2");

                //collapsible management
                $('.collapsible').collapsible({
                    defaultOpen: 'widget-section',
                    cookieName: 'body'
                });

                var fileUploader = new qq.FileUploader({
                    element: document.getElementById('idef-file-uploader'),
                    label: '<strong>Click Here to Upload IDEF ZIP File</strong>',
                    action: 'idef.do',
                    allowedExtensions: ['zip'],
                    onSubmit: function(){
                        fileUploader.setParams({
                            action: 'upload'
                        });
                    },
                    onProgress: function(id, fileName, loaded, total){},
                    onComplete: function(id, fileName, data){
                        new Messi(data.msg,
                        {title: 'Validation Result',
                         center: false,
                         viewport: {top: '50px', left: '150px'},
                         width: '800px',
                         closeButton: true,
                         buttons: [{id: 0, label: 'Close', val: 'X'}]
                        });
                    }
                });
            });

            var newWindow = null;
            function openJsonViewer(form)
            {
                var url = "about:blank";
                var urlParam = form.action + "?";
                $(form).find(":text").each(function(){
                    if(!$(this).val().empty()) {
                        urlParam += $(this).attr("name");
                        urlParam += "=";
                        urlParam += $(this).val();
                        urlParam += "&";
                    }
                });
                
                urlParam = encodeURIComponent(urlParam);
                url = "jsonViewer.do?url=" + urlParam;
                newWindow = window.open(url,'resultWindow','toolbar=0,resizable=0,scrollbars,status=no,toolbar=no,location=no,menu=no,width=800,height=600');
                newWindow.document.title=form.action;
                newWindow.focus();
                //form.target = 'resultWindow';
                //return true;

                return false;
            }

            function openWidget(btn, options)
            {
                var form = $(btn).closest('form');
                var url = options.url + '?';
                form.find(":text").each(function(){
                    if(!$(this).val().empty()) {
                        url += $(this).attr("name") + '=' + $(this).val() + '&';
                    }
                });
                url = url.substr(0, url.length-1);
                newWindow = window.open(url,'widgetWindow','toolbar=0,resizable=0,scrollbars,status=no,toolbar=no,location=no,menu=no,width='+options.width+',height='+options.height);
                newWindow.document.title=options.title;
                newWindow.focus();
                return false;
            }

            function openWindow(form)
            {
                newWindow = window.open('about:blank','dataloaderWindow','toolbar=0,resizable=0,scrollbars,status=no,toolbar=no,location=no,menu=no,width=800,height=600');
                newWindow.document.title=form.action;
                newWindow.focus();
                form.target = 'dataloaderWindow';
                return true;
            }

            function clearWarnMsg() {
                $(".warn").each(function(){
                    $(this).text("");
                    $(this).hide();
                });
            }
            
            function sendRequest(obj) { 
                clearWarnMsg();
                var valid = true;
                var invalidFields = new Array();
                $(obj).parents("div.section").children(":text.must").each(function(){
                    if($(this).val().empty()) {
               
                        valid = false;
                        invalidFields.push($(this).attr("name"));
                    }
                });
                if(!valid) {
                    if(invalidFields.length > 0) {
                        var errMsg = "[WARN]: Must specify parameter(s) ";
                        for(var i = 0, n = invalidFields.length; i < n; ++i) {
                            errMsg += "'" + invalidFields[i] + "'";
                            if( i == n - 2) {
                                errMsg += " and ";
                            } else if(i != n-1) {
                                errMsg += ", ";
                            }
                        }
                        errMsg +=" !";
                        $(obj).parents("div.section").children(".warn").text(errMsg);
                        $(obj).parents("div.section").children(".warn").show();
                    }
                    return false;
                }
                /*
                $(obj).parents("div.section").children(":text.int").each(function(){
                    if(!$(this).val().empty() && !isPositiveInteger($(this).val()) && $(this).val() != "0") {
                        valid = false;
                        invalidFields.push($(this).attr("name"));
                    }
                });*/
                if(!valid) {
                    if(invalidFields.length > 0) {
                        var errMsg = "[WARN]: The parameter(s) ";
                        for(var i = 0, n = invalidFields.length; i < n; ++i) {
                            errMsg += "'" + invalidFields[i] + "'";
                            if( i == n - 2) {
                                errMsg += " and ";
                            } else if(i != n-1) {
                                errMsg += ", ";
                            }
                        }
                        errMsg +=" must be a positive integer!";
                        $(obj).parents("div.section").children(".warn").text(errMsg);
                        $(obj).parents("div.section").children(".warn").show();
                    }
                    return false;
                }

                $(obj).parents("div.section").children(":text.multi-int").each(function(){
                    if(!$(this).val().empty() && !isMuliInt($(this).val())) {
                        valid = false;
                        invalidFields.push($(this).attr("name"));
                    }
                });
                if(!valid) {
                    if(invalidFields.length > 0) {
                        var errMsg = "[WARN]: The parameter(s) ";
                        for(var i = 0, n = invalidFields.length; i < n; ++i) {
                            errMsg += "'" + invalidFields[i] + "'";
                            if( i == n - 2) {
                                errMsg += " and ";
                            } else if(i != n-1) {
                                errMsg += ", ";
                            }
                        }
                        errMsg +=" must have one more integers joined by '+'. e.g. 1+2+5+7!";
                        $(obj).parents("div.section").children(".warn").text(errMsg);
                        $(obj).parents("div.section").children(".warn").show();
                    }
                    return false;
                }
                return true;
            }

            function isMuliInt(s) {
                var items = s.split("+");
                for(var i = 0, n = items.length; i < n; ++i) {
                    if(!isPositiveInteger(items[i])){
                        return false;
                    }
                }
                return true;
            }

            function isPositiveInteger(s) {
                var regex = /^[0-9]*[1-9][0-9]*$/;
                return regex.test(s);
            }

            function generateApiRequest(obj){
                var baseRequest = $('#baseRequest').val();
                var so = $('#so').val();
                var sv = $('#sv').val();
                var key = $('#key').val();
                var checkData = "so=" + so + "&sv=" + sv + "&key=" + key +  "&request=" + encodeURIComponent(baseRequest);
                $.ajax({
                    type: "POST",
                    url: "generateApi.do",
                    data: checkData,
                    cache: false,
                    async: false,
                    success: function(result) {
                        var json = eval("(" + result + ")");
                        if(json.code == 0){
                            $(obj).parents("div.section").children(".warn").text(json.reason);
                            $(obj).parents("div.section").children(".warn").show();
                        }else{
                            $('#basicRequest').val(json.basicRequest);
                            $('#serverRequest').val(json.serverRequest);
                            $('#computationStr').val(json.computationStr);
                        }
                    }
                });
            }
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <%
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String path = request.getContextPath();
        String basePath = scheme + "://" + host + ((port == 80) ? "" : ":" + port) + path;
    %>
    <body>
        <div id="indaba">
            <div class="interactive" style=" margin-top: 20px;">
                <h1>Indaba Data Services</h1>
                <div class="wrapper">
                    <fieldset>
                        <legend>WELCOME</legend>

                        <div class="section">
                            Welcome to Indaba Data Services. This page is designed to help you understand and program with Indaba
                            Data API and Indaba Data Exchange Format (IDEF). The functions are organized in 3 expandable sections:
                            <br/><br/>
                            WIDGETS AND DATA API - this section shows you how to program with Indaba Data API with many live examples. <br/>
                            SECURE API REQUEST PRACTICE - this section shows you how to generate secure data requests. <br/>
                            IDEF DATA VALIDATION - this section helps you validate IDEF data set and makes sure it conforms to IDEF Spec.
                            <br/><br/>
                            Please click to expand the sections you are interested in.
                          
                        </div>
                    </fieldset>
                    
                    <!-- collapsible -->
                    <h3 class="collapsible" id="widgets-section">WIDGETS AND DATA API<span></span></h3>
                    <div class="top">
                    <fieldset>
                        <legend>Widget How-tos</legend>

                        <div class="section">
                            <div class="note">
                                This section is to help you understand how to use and program with INDABA Data API. The data API is developed initially
                                to support the development of INDABA widgets. However, since the data API simply returns raw data in JSON structures, you can
                                use it for your own data-based ideas. Each box in this section demonstrates one API. Click the GET button
                                to send the request and look at the returned JSON data. Click the WIDGET button to see the corresponding
                                INDABA widget.
                                <br/><br/>
                                All widgets require the <em>horseId</em> parameter and/or <em>productId</em> parameter. You should get the list of horse IDs
                                and the product ID from your project manager. If you don't have horse IDs and only want to see how API works, you can use 1 to 5 for scorecard
                                widgets, and 37 for journal widgets. Use 1 for product Id.
                            </div>

                            <ul style="line-height:1.8em;">
                                <li>>&nbsp;&nbsp;<a href="widgets/howto.html">How to include Indaba Widgets in your site page</a></li>
                                <li>>&nbsp;&nbsp;<a href="widgets/sample/demo1.html">Demo 1</a>
                                    &nbsp;&nbsp;<a href="widgets/sample/demo2.html">Demo 2</a>
                                    &nbsp;&nbsp;<a href="widgets/sample/demo3.html">Demo 3</a>
                                    &nbsp;&nbsp;<a href="widgets/sample/demo4.html">Permlink Demo</a></li>
                            </ul>
                        </div>
                    </fieldset>
                    <form id="journal" action="journal.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Journal Data</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">journal.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param option">includeReviews</span>=<input type="text" class="int" name="includeReviews" size="5" />&
                                <span class="param option">includeLogo</span>=<input type="text" class="int" name="includeLogo" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'widgets/journalDisplay.html', width:800, height:600, title:'Journal Display'})" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="vcardSummary" action="vcardSummary.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Vertical Scorecard Summary</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">vcardSummary.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param option">includeLogo</span>=<input type="text" class="int" name="includeLogo" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'widgets/vcardDisplay.html', width:800, height:600, title:'Vertical Scorecard Display'})" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="vcardSummary4RWI" action="vcardSummary4RWI.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Vertical Scorecard Summary (<span style="color:red;">Customized For RWI</span>)</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">vcardSummary4RWI.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param option">includeLogo</span>=<input type="text" class="int" name="includeLogo" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'widgets/vcardDisplay4RWI.html', width:800, height:600, title:'Vertical Scorecard Display'})" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="vcardSubcat" action="vcardSubcat.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Vertical Scorecard Subcat Detail</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">vcardSubcat.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param must">subcatId</span>=<input type="text" class="must int" name="subcatId" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="vcardSubcat4RWI" action="vcardSubcat4RWI.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Vertical Scorecard Subcat Detail (<span style="color:red;">Customized For RWI</span>)</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">vcardSubcat4RWI.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param must">subcatId</span>=<input type="text" class="must int" name="subcatId" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="vcardQuestionSet" action="vcardQuestionSet.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Vertical Scorecard Question Set Detail</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">vcardQuestionSet.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param must">questionSetId</span>=<input type="text" class="must int" name="questionSetId" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="vcardQuestionSet4RWI" action="vcardQuestionSet4RWI.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Vertical Scorecard Question Set Detail (<span style="color:red;">Customized For RWI</span>)</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">vcardQuestionSet4RWI.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param must">questionSetId</span>=<input type="text" class="must int" name="questionSetId" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="hcardSummary" action="hcardSummary.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Horizontal Scorecard Summary</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">hcardSummary.do</span>?
                                <span class="param must">productId</span>=<input type="text" class="must int" name="productId" size="5" />&
                                <span class="param option">includeLogo</span>=<input type="text" class="int" name="includeLogo" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'widgets/hcardDisplay.html', width:800, height:600, title:'Horizontal Scorecard Display'})" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="hcardSubcatSummary" action="hcardSubcatSummary.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Horizontal Scorecard Sub-Category Summary <img style="width:22px; position: relative; top: 6px;" src="images/new.png" alt="[New]"/>&nbsp;</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">hcardSubcatSummary.do</span>?
                                <span class="param must">productId</span>=<input type="text" class="must int" name="productId" size="5" />&
                                <span class="param must">subcatId</span>=<input type="text" class="must int" name="subcatId" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'widgets/hcardDisplay.html', width:800, height:600, title:'Horizontal Scorecard Display'})" />
                                <a class="help" href="widgets/api_help.html#hcardSubcatSummary" target="_blank"><img src="widgets/image/question-mark.png" /></a>
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="hcardQuestionSummary" action="hcardQuestionSummary.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Horizontal Scorecard Question Summary</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">hcardQuestionSummary.do</span>?
                                <span class="param must">productId</span>=<input type="text" class="must int" name="productId" size="5" />&
                                <span class="param must">questionId</span>=<input type="text" class="must int" name="questionId" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="scorecardAnswer" action="scorecardAnswer.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Scorecard Answer</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">scorecardAnswer.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param must">questionId</span>=<input type="text" class="must int" name="questionId" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="dataSummary" action="dataSummary.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Data Summary</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">dataSummary.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param option">genericLabel</span>=<input type="text" class="int" name="genericLabel" size="5" />&
                                <span class="param option">includeLogo</span>=<input type="text" class="int" name="includeLogo" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'dataSummary.jsp', width:400, height:600, title:'Data Summary'})" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="indicatorSummary" action="indicatorSummary.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Indictor Summary</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">indicatorSummary.do</span>?
                                <span class="param must">horseId</span>=<input type="text" class="must int" name="horseId" size="5" />&
                                <span class="param option">genericLabel</span>=<input type="text" class="int" name="genericLabel" size="5" />&
                                <span class="param option">includeLogo</span>=<input type="text" class="int" name="includeLogo" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'indicatorSummary.jsp', width:550, height:600, title:'Indicator Summary'})" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="sparklineData" action="sparklineData.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Sparkline Data</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">sparklineData.do</span>?
                                <span class="param must">productId</span>=<input type="text" class="must int" name="productId" size="5" />&
                                <span class="param must">horseId</span>=<input type="text" class="must multi-int" name="horseId" size="5" />&
                                <span class="param option">includeLogo</span>=<input type="text" class="int" name="includeLogo" size="5" />&
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <input type="button" value="Widget" onClick="return sendRequest(this) && openWidget(this, {url:'sparkline.jsp', width:400, height:600, title:'Sparkline'})" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    <form id="journalSummary" action="journalSummary.do" onsubmit="return openJsonViewer(this)" method="get" >
                        <fieldset>
                            <legend>Get Journal Summary</legend>
                            <div class="section">
                                <%= basePath%>/<span class="action">journalSummary.do</span>?
                                <span class="param must">productId</span>=<input type="text" class="must int" name="productId" size="5" />&
                                <!--<span class="param option">projectId</span>=<input type="text" class="must int" name="projectId" size="5" />&-->
                                <span class="param option">version</span>=<input type="text" class="int" name="version" size="1" disabled value="1" style=" text-align: center;" />&nbsp;
                                <input type="submit" value="Get" onClick="return sendRequest(this)" />
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </form>
                    </div><!-- end of collapsible -->

                    <!-- collapsible -->
                    <h3 class="collapsible" id="api-section">SECURE API REQUEST PRACTICE<span></span></h3>
                    <div class="top">
                    <div id="generateApi">
                      
                        <fieldset>
                            <legend>Generate Secure API Request</legend>

                            <div class="section">
                                <div class="note">
                                    This section is to help you create <em>secure</em> API requests properly. To generate a secure API
                                    request, you must have the following: <br/><br/>
                                        Org ID - the ID assigned to your organization. Enter it into the <em>so</em> field. <br/>
                                        Security Key - the security key generated for your organization. Enter it into the <em>key</em> field.<br/>
                                        Key Version - the version number of your security key. Enter it into the <em>sv</em> field.<br/>
                                    <br/>
                                    In the <em>Request</em> field, append whatever API request you want to send (see WIDGETS AND DATA API section
                                    for request formats).
                                    <br/><br/>
                                    Click GENERATE button to generate the request.
                                    <br/><br/>
                                    The "BASIC REQUEST" box shows you what the basic request looks like. Note that new parameters <em>st (timestamp)</em>
                                    and <em>sr (a random number)</em> are generated.<br/>
                                    The "STRING FOR DIGEST COMPUTATION" box shows you the string to be used for computing message digest. Note that
                                    the key value is appended to the end of the basic request.<br/>
                                    The "SERVER REQUEST" shows you what the actual request looks like. The <em>sd</em> parameter is added to the end and
                                    it is the digest computed using SHA1 against the string in "STRING FOR DIGEST COMPUTATION" box. 
                                    <br/><br/>
                                    Note: your security key is only used to compute the digest. It is NEVER included into the actual request
                                    sent to the server. Please keep your key securely. For the practice, you don't have to use the real key - any arbitrary
                                    string works.
                                    <br/><br/>
                                    Note: the server request is NOT actually sent to the server. This practice is only intended to help you understand how
                                    a secure request is created. Please read "Indaba Data API Security Programming Guide" for more details of the algorithm.
                                </div>
                            </div>
                            
                            <div class="section">
                                <div style="margin:10px 5px;">
                                    <span>Request</span>
                                    <input id="baseRequest" name="baseRequest" type="text" value="<%= basePath%>/" size="100"></input>
                                </div>
                                <div style="margin:10px 5px;">
                                    <span>so</span>
                                    <input id="so" name="so" type="text" size="10"></input>
                                    <span>sv</span>
                                    <input id="sv" name="sv" type="text" size="10"></input>
                                    <span>key</span>
                                    <input id="key" name="key" type="text" size="40"></input>
                                </div>
                                <input type="submit" value="Generate" onclick="return generateApiRequest(this);" />
                                <div>
                                    <label>BASIC REQUEST</label>
                                    <textarea id="basicRequest" name="basicRequest" type="text" cols="100" rows="3"></textarea>
                                </div>
                                <div>
                                    <label>STRING FOR DIGEST COMPUTATION (SHA1)</label>
                                    <textarea id="computationStr" name="computationStr" type="text" cols="100" rows="3"></textarea>
                                </div>
                                <div>
                                    <label>SERVER REQUEST</label>
                                    <textarea id="serverRequest" name="serverRequest" type="text" cols="100" rows="3"></textarea>
                                </div>
                                <div class="warn"></div>
                            </div>
                        </fieldset>
                    </div>
                    </div><!-- end of collapsible -->

                    <!-- collapsible -->
                    <h3 class="collapsible" id="idef-section">IDEF DATA VALIDATION<span></span></h3>
                    <div class="top">
                    <div id="idef">
                        <fieldset>
                            <legend>Upload IDEF Data For Validation</legend>
                            <div class="section">
                                <div class="note">
                                    This section is for validating IDEF data set. The data set must be prepared according to
                                    the IDEF Spec and packaged into a ZIP file. The data will only be validated. Even if it is validated successfully,
                                    it will NOT be automatically loaded into INDABA Platform.
                                </div>
                            </div>
                            <div id="idef-file-uploader">
                                <noscript>
                                    <p>Please enable JavaScript to use file uploader.</p>
                                </noscript>
                            </div>
                        </fieldset>
                    </div>
                    </div><!-- end of collapsible -->
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
