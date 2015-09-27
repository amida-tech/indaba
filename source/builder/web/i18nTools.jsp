<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : allcontent
    Created on : Mar 16, 2012, 11:26:15 PM
    Author     : Jeff Jiang
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<c:choose>
    <c:when test="${(empty authenticated) || !authenticated}">
        <c:set scope="request" var="errMsg" value="You have no permission to access this page!" />
        <logic:forward name="error.jsp" />
    </c:when>
    <c:otherwise>
        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
            <head>
                <title>Indaba Builder | I18N Resource Message Editor</title>
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
                <link type="text/css" rel="stylesheet" href="css/style.css"/>
                <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
                <link type="text/css" rel="stylesheet" href="css/jquery.flexigrid.css"/>
                <link type="text/css" rel="stylesheet" href="css/jquery.jqgrid.css"/>
                <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css"/>
                <link type="text/css" rel="stylesheet" href="css/redmond/jquery-ui-1.8.17.custom.css"/>

                <script type="text/javascript" src="js/json2.js"></script>
                <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
                <script type="text/javascript" src="js/jquery.i18n.js"></script>
                <script type="text/javascript" src="jsI18nMsg.do"></script>
                <script type="text/javascript" src="js/jquery.alerts.js"></script>
                <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
                <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>
                <script type="text/javascript" src="js/jquery.flexigrid.js"></script>
                <script type="text/javascript" src="js/grid.locale-en.js"></script>
                <script type="text/javascript" src="js/jquery.jqGrid.min.js"></script>
                <script type="text/javascript" src="js/common.js"></script>

                <style type="text/css">
                    h2 {
                        text-align: center;
                        margin: 10px 0px;
                        color: #fff;
                        font-size: 14px;
                        background: url("css/redmond/images/ui-bg_gloss-wave_55_5c9ccc_500x100.png") repeat-x scroll 50% 50% #5C9CCC;
                        padding: 10px 0px;
                        border-radius: 5px;
                    }
                    body, span, div, a {
                        font-family: Arial, Helvetica;
                        font-size: 12px;
                    }
                    div#main {
                        width: 900px;
                    }
                    .flexigrid div.fbutton .add {
                        background: url("css/flexigrid/add.png") no-repeat scroll left center transparent;
                    }
                    .flexigrid div.fbutton .delete {
                        background: url("css/flexigrid/close.png") no-repeat scroll left center transparent;
                    }
                    .ui-tabs-nav li {position: relative;}
                    .ui-tabs-selected a span {padding-right: 10px;}
                    .ui-tabs-close {display: none;position: absolute;top: 3px;right: 0px;z-index: 800;width: 16px;height: 14px;font-size: 10px; font-style: normal;cursor: pointer;}
                    .ui-tabs-selected .ui-tabs-close {display: block;}
                    .ui-layout-west .ui-jqgrid tr.jqgrow td { border-bottom: 0px none;}
                    .ui-datepicker {z-index:1200;}
                    .rotate
                    {
                        /* for Safari */
                        -webkit-transform: rotate(-90deg);

                        /* for Firefox */
                        -moz-transform: rotate(-90deg);

                        /* for Internet Explorer */
                        filter: progid:DXImageTransform.Microsoft.BasicImage(rotation=3);
                    }
                    hr.ui-widget-content {
                        height: 1px;
                    }
                    td.DataTD input {
                        width: 270px;
                    }
                    #quick-start {
                        text-align: left;
                    }
                    #quick-start h3{
                        font-size: 14px;
                        padding: 5px 5px;
                        border: 1px solid #c8e3ff;
                        background-color: #eef8ff;
                        margin: 5px 0px;
                    }
                    #quick-start i{
                        color: #4297d7;
                    }
                    #quick-start h3 a {
                        font-size: 14px;
                    }
                    #quick-start h3 a:hover {
                        text-decoration: none;
                    }
                    #quick-start ol, #quick-start ul{
                        margin: 0px 25px; 
                    }
                    #quick-start ol li{
                        line-height: 18px;
                    }
                    #quick-start ul li{
                        margin: 5px 0px;
                    }
                    .prop {
                        color: #4297d7;
                        display: inline-block;
                        width: 165px;
                    }
                    .tag {
                        color: #4297d7;
                        display: inline-block;
                    }
                    .modified-row {
                        background: #fbec88 url(css/redmond/images/ui-bg_highlight-hard_30_285c00_1x100.png) 50% 50% repeat-x; color: #fff;
                    }
                    .disabled-row {
                        background: #fbec88 url(css/redmond/images/ui-bg_flat_0_eeeeee_40x100.png) 50% 50% repeat-x; color: #363636;
                    }
                    div#run-btn{
                        margin: 5px 0px 15px 0px; 
                    }
                    div#run-btn a {
                        padding: 8px 10px;
                        margin: 5px 5px;
                        background-color: #749a02;
                        -moz-border-radius: 5px;
                        color: #fff;
                    }
                    div#run-btn a:hover {
                        background-color: #FF6600;
                        text-decoration: none;
                    }
                </style>
                <script type="text/javascript">
                    $.extend($.fn.fmatter,{
                        htmlContent:function(cellval,options,rowdata){
                            return $.jgrid.htmlEncode(cellval);
                        }
                    });
 
                    $.extend($.fn.fmatter.htmlContent,{
                        unformat:function(cellval,options){
                            return $.jgrid.htmlDecode(cellval);
                        }
                    });
                    $(function(){
                        var maintab =$('#tabs').tabs({
                            add: function(e, ui) {
                                // append close thingy
                                $(ui.tab).parents('li:first')
                                .append('<span class="ui-tabs-close ui-icon ui-icon-close" title="Close Tab"></span>')
                                .find('span.ui-tabs-close')
                                .click(function() {
                                    maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
                                });
                                // select just added tab
                                maintab.tabs('select', '#' + ui.panel.id);
                            }  
                        });
                
                        $('#runall').click(function(){
                            jConfirm("Are you sure to run the tag engine on all tagged JSP/HTML source files?", "Confirmation", function(choice){
                                if(choice) {
                                    openWindow('textResourceTemplate.do?action=runall');
                                }
                            });
                            return false;
                        });
                        $('#owall').click(function(){
                            jConfirm("Are you sure to overwrite all the original source files with the generated customerized JSP tag files?", "Confirmation", function(choice){
                                if(choice) {
                                    openWindow('textResourceTemplate.do?action=owall');
                                }
                            });
                            return false;
                        });
                        $('#txtrsrclist').click(function(){
                            openWindow('textResourceTemplate.do?action=txtrsrclist');
                            return false;
                        });
                        $('#checkdup').click(function(){
                            openWindow('textResourceTemplate.do?action=checkdup');
                            return false;
                        });
                
                        $('#load').click(function(){
                            $('#loadcsv').dialog();
                            document.getElementById("loadcsvForm").reset();
                            //$("#loadcsvForm").attr("action", 'textResourceTemplate.do?action=loadtxtrsrc');
                            return false;
                        });
                        //$('input.loadtype').click(function(){
                        //$(this).parent().attr("action", 'textResourceTemplate.do?action=' + $('input.loadtype:checked').val());
                        //alert($('input[name="loadtype"]:checked').val());
                        //return true;
                        //});
                
                        $("#grid-tbl").jqGrid({ 
                            url:'sourceFile.do', 
                            datatype: "json", 
                            rowNum:20, rowList:[10,20,30,50], sortname: 'file', sortorder: "asc", viewrecords: true, 
                            colNames:['Actions', 'No.', 'Source File', 'Type', 'Total Text Resources', 'Status'], 
                            colModel:[ 
                                {name:'act',index:'act', width:74,sortable:false, align:'right', search: false},
                                {name:'id', key : true, index:'id', width:50, align:'right', search: false }, 
                                {name:'file',index:'srcFile', width:500, stype:"text",
                                    searchoptions: {
                                        odata:['contains'], sopt:['cn']
                                    } }, 
                                {name:'ext',index:'ext', width:100, search: false }, 
                                {name:'count', index:'count', width:120, align:'right', search: false},
                                {name:'status', index:'status', hidden:true, editable:true, search: false }
                            ],
                            postData:{oper:"list"},
                            pager: '#grid-pager', 
                            jsonReader: { repeatitems : false }, 
                            //caption: "I18N Source Files",
                            height: '100%', 
                            gridComplete: function(){ 
                                var ids = $("#grid-tbl").jqGrid('getDataIDs'); 
                                for(var i=0;i < ids.length;i++){ 
                                    var rowdata = $("#grid-tbl").jqGrid('getRowData',ids[i]);
                                    var editTxtRsrc = '<a href="#" title="Edit text resources" style="float: left; margin-right: 4px;" onclick="return openTextResourceTab('+ids[i]+');"><span class="ui-icon ui-icon-document"></span></a>';
                                    var runTmpl = '<a href="#" title="Run text resource template of this file" style="float: left; margin-right: 4px;" onclick="return runTextResourceTemplate('+ids[i]+');"><span class="ui-icon ui-icon-gear"></span></a>';
                                    var overwrite = '<a href="#" title="Overwrite the source file with generated file " style="float: left;" onclick="return overwriteSourceFile('+ids[i]+');"><span class="ui-icon ui-icon-arrowthickstop-1-e"></span></a>';
                                    if(rowdata.status == 1) {
                                        if(rowdata.ext == "common" || rowdata.ext == "java") {
                                            jQuery("#grid-tbl").jqGrid('setRowData',ids[i],{act: editTxtRsrc}); 
                                        } else {
                                            jQuery("#grid-tbl").jqGrid('setRowData',ids[i],{act: editTxtRsrc + runTmpl + overwrite}); 
                                        }
                                    }
                            
                                    if(rowdata.count > 0) {
                                        $('table#grid-tbl tr#' + rowdata.id).toggleClass('modified-row');
                                    } else if(rowdata.status != 1) {
                                        $('table#grid-tbl tr#' + rowdata.id).toggleClass('disabled-row');
                                    }
                                }
                            },
                            beforeSelectRow: function(rowid, e) {
                                return false;
                            }

                            /*
                            onSelectRow: function(rowid) {
                                openTextResourceTab(rowid);
                            }*/
                        }); 
                        $("#grid-tbl").jqGrid('navGrid','#grid-pager',{edit:false,add:false,del:false, refresh:true});
                
                        $("#n-tbl").jqGrid({ 
                            url:'notificationEdit.do', 
                            editurl : 'notificationEdit.do',
                            datatype: "json", 
                            rowNum:20, rowList:[10,20,30,50], sortname: 'subject', sortorder: "asc", viewrecords: true, 
                            colNames:['Actions', 'typeId', 'Subject', 'Subject(Hidden)', 'English Item Id','English Text','French Item Id','French Text'], 
                            colModel:[ 
                                {name:'actions', width:70, fixed:true, key: false, sortable:false, resize:false, sortable:false, resize:false, search: false, align:'right', formatter:'actions', 
                                    formatoptions:{
                                        keys:false,
                                        editformbutton: false,
                                        extraparam: {oper:'edit'},
                                        afterSave:  function(rowid){
                                            //var rowdata = $("#n-tbl").jqGrid('getRowData',rowid);
                                            //alert(rowdata.frText);
                                            //var e = $('table#n-tbl tr#' + rowid + ' td[aria-describedby="n-tbl_frText"]');
                                            //e.text(rowdata.frText);
                                        }
                                    }},
                                {name:'typeId',index:'typeId', hidden:true, editable:true}, 
                                {name:'subject', index:'subject', width:170, align:'left', editable:false, search: false}, 
                                {name:'subject', index:'subjectHidden', hidden:true, editable:true}, 
                                {name:'enItemId',index:'enItemId', hidden:true, editable:true}, 
                                {name:'enText',index:'enText', width:305, editable:false, search: false }, 
                                {name:'frItemId', index:'frItemId', hidden:true, editable:true, search: false},
                                {name:'frText', index:'frText', formatter:'htmlContent', width:305, editable:true, search:false, edittype:"textarea", editoptions:{rows:"9",cols:"42" }}
                            ],
                            postData:{oper:"list"},
                            pager: '#n-pager', 
                            jsonReader: { repeatitems : false }, 
                            //caption: "I18N Source Files",
                            height: '100%', 
                            gridComplete: function(){ 
                                var ids = $("#n-tbl").jqGrid('getDataIDs'); 
                                for(var i=0;i < ids.length;i++){ 
                                    var rowdata = $("#n-tbl").jqGrid('getRowData',ids[i]);
                                    if(rowdata.frText != "") {
                                        //$('table#n-tbl tr#' + ids[i]).toggleClass('modified-row');
                                        var e = $('table#n-tbl tr#' + ids[i] + ' td[aria-describedby="n-tbl_frText"]');
                                        e.empty();
                                        e.text(rowdata.frText);
                                    }
                                    $('table#n-tbl div.ui-inline-del').remove();
                                }
                            },
                            beforeSelectRow: function(rowid, e) {
                                return false;
                            }
                        }); 
                        $("#n-tbl").jqGrid('navGrid','#n-pager',{edit:false,add:false,del:false, refresh:true});
                
                        $('#quick-start h3 a').click(function(){
                            if($('span', this).hasClass('ui-icon-triangle-1-s')){
                                $('span', this).removeClass('ui-icon-triangle-1-s');
                                $('span', this).toggleClass('ui-icon-triangle-1-e');
                                $(this).parent().parent().children('ul, ol').hide();
                            } else {
                                $('span', this).removeClass('ui-icon-triangle-1-e');
                                $('span', this).toggleClass('ui-icon-triangle-1-s');
                                $(this).parent().parent().children('ul, ol').show();
                            }
                            return false;
                        });
                        $('.ui-icon-triangle-1-e').each(function(){
                            $(this).parent().parent().parent().children('ul, ol').hide();
                        });
                    });
                    function openTextResourceTab(rowid) {
                        var rowdata = $("#grid-tbl").jqGrid('getRowData',rowid);
                        var blkId = "#src-file-" + rowdata.id;
                        var gridId = "grid-src-file-" + rowdata.id;
                        var pageId = "page-src-file-" + rowdata.id;
                        if($(blkId, "#tabs")[0]) {
                            var index = 0;
                            $('#tabs>div').each(function() {
                                if('#' + $(this).attr('id') == blkId) {
                                    return false;
                                }
                                index++;
                            });
                            $('#tabs').tabs("select", index);
                        } else {
                            $('#tabs').tabs("add", blkId, rowdata.file);
                            $(blkId).append('<table id="' + gridId+'"></table>');
                            $(blkId).append('<div id="' + pageId+'"></div>');
                            textResourceGrid(rowdata.id, '#' + gridId, '#' + pageId);
                        }
                        return false;
                    }
                    function textResourceGrid(srcFileId, gridId, pageId) {
                        $(gridId).jqGrid({ 
                            url:'textResource.do?srcFileId=' + srcFileId, 
                            editurl : 'textResource.do?srcFileId=' + srcFileId,
                            cellurl : 'textResource.do?srcFileId=' + srcFileId, 
                            datatype: "json", 
                            rowNum:20, rowList:[10,20,30,50],sortname: 'rsrcName', sortorder: "asc",
                            viewrecords: true, 
                            colNames:['Actions','Resource Id','Resource Name','English Item Id','English Text','French Item Id','French Text'], 
                            colModel:[ 
                                {name: 'actions', width:70, fixed:true, key: false, sortable:false, resize:false, sortable:false, resize:false, search: false, align:'right', formatter:'actions', 
                                    formatoptions:{
                                        keys:false,
                                        extraparam: {oper:'edit'}
                                    }},
                                {name:'rsrcId', key: true, index:'rsrcId', hidden:true, editable:true, search: false }, 
                                {name:'rsrcName', index:'rsrcName', width:240, sortable: true, align:'left', editable:true,
                                    searchoptions: {
                                        odata:['contains'], sopt:['cn']
                                    }  }, 
                                {name:'enItemId', index:'enItemId', hidden:true, editable:true, search: false }, 
                                {name:'enText',index:'enText', width:270, align:'left', editable:true,edittype:"textarea", editoptions:{rows:"3",cols:"41"},
                                    searchoptions: {
                                        odata:['contains'], sopt:['cn']
                                    }  },
                                {name:'frItemId', index:'frItemId', hidden:true, editable:true, search: false },  
                                {name:'frText',index:'frText', width:270, align:'left', editable:true,edittype:"textarea", editoptions:{rows:"3",cols:"41"},
                                    searchoptions: {
                                        odata:['contains'], sopt:['cn']
                                    }  }
                            ],  
                            postData:{oper:"list"},
                            prmNames:{id:"rsrcId"},
                            pager: pageId, 
                            jsonReader: { repeatitems : false }, 
                            /*caption: "I18N Source Files", */
                            height: '100%', 
                            gridComplete: function(){ 
                                var ids = $(gridId).jqGrid('getDataIDs'); 
                                for(var i=0;i < ids.length;i++){ 
                                    var rowdata = $(gridId).jqGrid('getRowData',ids[i]);
                                    if(rowdata.frText != "") {
                                        $(gridId + ' tr#' + rowdata.rsrcId).toggleClass('modified-row');
                                    }
                                }
                            },
                            beforeSelectRow: function(rowid, e) {
                                return false;
                            }
                        }); 
                        $(gridId).jqGrid('navGrid',pageId,{edit:false,del:false});
                    }
                    function runTextResourceTemplate(rowid) {
                        var rowdata = $("#grid-tbl").jqGrid('getRowData',rowid);
                        jConfirm("You will be running the text resource tag engine. Are you sure?", "Confirmation", function(choice){
                            if(choice) {
                                $.ajax({
                                    type: "POST",
                                    url: "textResourceTemplate.do",
                                    data: {srcFileId: rowdata.id},
                                    success: function(result) {
                                        var json = JSON.parse(result);
                                        $("#grid-tbl").jqGrid('setRowData',rowid, {"count":json.data});
                                        if(json.data > 0) {
                                            $('table#grid-tbl tr#' + rowdata.id).toggleClass('modified-row');
                                        }
                                        if(json.ret == 0) {
                                            jInfo(json.desc, 'Success');
                                        } else {
                                            jAlert(json.desc, 'Error');
                                        }
                                    }
                                });
                            }
                        });
                        return false;
                    }
                    function overwriteSourceFile(rowid) {
                        var rowdata = $("#grid-tbl").jqGrid('getRowData',rowid);
                        jConfirm("You will overwrite the resource file with the generated file with JSP customized tags. <br/>Are you sure?", "Confirmation", function(choice){
                            if(choice) {
                                $.ajax({
                                    type: "POST",
                                    url: "textResourceTemplate.do",
                                    data: {
                                        action: "overwrite",
                                        srcFileId: rowdata.id
                                    },
                                    success: function(result) {
                                        var json = JSON.parse(result);
                                        if(json.ret == 0) {
                                            jInfo(json.desc, 'Success');
                                        } else {
                                            jAlert(json.desc, 'Error');
                                        }
                                    }
                                });
                            }
                        });
                        return false;
                    }
                    function switchToQuickStartTab(){ 
                        $('#tabs').tabs("select", 0);
                        return false;
                    }
            
                    function openWindow(url)
                    {
                        newWindow = window.open(url,'txtRsrcTmpl','toolbar=0,resizable=0,scrollbars,status=no,toolbar=no,location=no,menu=no,width=700,height=600');
                        newWindow.focus();
                        return false;
                    }
                    function postWindow(form){
                        $("#loadcsvForm").attr("action", 'textResourceTemplate.do?action=' + $('input.loadtype:checked').val());
                        $('#loadcsv').dialog("close");
                        //
                        newWindow = window.open('about:blank','loadWindow','toolbar=0,resizable=0,scrollbars,status=no,toolbar=no,location=no,menu=no,width=800,height=600');
                        newWindow.document.title=form.action;
                        newWindow.focus();
                        form.target = 'loadWindow';
                        return false;
                    }
                </script>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            </head>
            <body>
                <div id="loadcsv" title="Load Text Resource/Notification" style="display:none;" >
                    <form id="loadcsvForm" action="" method="POST" enctype="multipart/form-data" style="margin: 20px 0px;" onsubmit="postWindow(this);">
                        <input type="radio" name="loadtype" value="loadtxtrsrc" checked class="loadtype" /> Text Resources &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                        <input type="radio" name="loadtype" value="loadnotification" class="loadtype" /> Notification Items <br/><br/>
                        <input type="file" name="csvfile" id="csvfile" /><br/><br/>
                        <input type="reset" name="resetIt" value="Reset" /> &nbsp;&nbsp;
                        <input id="loadsubmit" type="submit" name="submit" value="Submit" />
                    </form>
                </div>
                <div id="indaba">
                    <div class="wrapper" style="text-align: center;">
                        <h2>Indaba Builder | I18N Resource Message Editor | <a href="#" style="color: #FF6600" onclick="switchToQuickStartTab()" >Quick Start</a></h2>
                        <div id="tabs" class="jqgtabs">
                            <ul>
                                <li><a href="#quick-start">Quick Start</a></li>
                                <li><a href="#notification-list">Notification Canned Text</a></li>
                                <li><a href="#source-file-list">I18N Source Files</a></li>
                            </ul>
                            <div id="notification-list" style="font-size:12px;">
                                <table id="n-tbl">

                                </table>
                                <div id="n-pager">

                                </div>
                            </div>
                            <div id="source-file-list" style="font-size:12px;">
                                <div id="run-btn">
                                    <a id="runall" href="#" >Run All Tagged Files</a>
                                    <a id="owall" href="#" >Overwrite All Original Files</a>
                                    <a id="txtrsrclist" href="#" >List All Resources</a>
                                    <a id="checkdup" href="#" >Check Duplicated Resources</a>
                                    <a id="load" href="#" >Load Resources</a>
                                </div>
                                <table id="grid-tbl">

                                </table>
                                <div id="grid-pager">

                                </div>
                            </div>
                            <div id="quick-start">
                                <div>
                                    <h3><a href="#"><span style="float:left;" class="ui-icon ui-icon-triangle-1-s"></span>Introduction</a></h3>
                                    <ul>
                                        <li>This page is designed to support i18n text resources editing. It includes all text resources in source files(JSP, HTML, JS, JAVA, etc.).</li>
                                        <li>
                                            To efficiently extract the text resources in the source files, we provide an useful tool to make it happen. 
                                            In fact, what you need to do is just marking/ wrapping all the resource texts that need to be localized in 
                                            source files with a predefined tag. Then run the tag engine to parse and extract the tagged resource text.
                                            Finally, the tagged resource texts will be automatically substituted with a customized JSP tag, e.g. 
                                            <span class="tag">&lt;<strong>indaba:msg</strong></span> <span style="color:green;">key="xxx"</span> <span class="tag">/&gt;</span>).
                                            This tag will fill with the real text based the locale in runtime. 
                                        </li>
                                        <li>
                                            <img alt="i18n tag handle process" src="images/i18n-tag-process.png"/>
                                        </li>
                                        <li>
                                            Additionally, after the resources extracted, you can easily edit each resources in the page one by one. 
                                            Certainly, you can manually add/delete any resources as well if needed.
                                        </li>
                                        <li>
                                            Sounds a good idea(Thanks Luke's hints). Right? Let's start an amazing experience now! Go...
                                        </li>
                                    </ul>
                                </div>
                                <div>
                                    <h3><a href="#"><span style="float:left;" class="ui-icon ui-icon-triangle-1-e"></span>Preparation</a></h3>
                                    <ol>
                                        <li>Check out the lastest source code from SVN.</li>
                                        <li>Upgrade data model to v17_9(<i>schema/indaba_data_model_v17_9.sql</i>) and load the test data v17_9(<i>schema/indaba_test_data_v17_9.sql</i>) as well.</li>
                                        <li>
                                            Specify the related i18n source file configuration(<i>web/WEB-INF/config/indaba_config.properties</i>):<br/>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <span class="prop">i18n.resource.src.orig.dir:</span>Directory of original source file.<br/>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <span class="prop">i18n.resource.src.tagged.dir:</span>Directory of tagged source file.<br/>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <span class="prop">i18n.resource.backup.dir:</span>Directory of backup file(generated before tag engine running).<br/>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <span class="prop">i18n.resource.output.dir:</span>Directory of output file(gnerated after tag extracted).<br/>
                                            Please guarantee Tomcat user MUST have the READ and WRITE permissions on the above folders.
                                        </li>
                                        <li>Start up Tomcat and access url: <a class="tag" href="http://localhost:8080/indaba/sourceFile.html">http://yourhost:8080/indaba/sourceFile.html</a></li>
                                    </ol>
                                </div>
                                <div>
                                    <h3><a href="#"><span style="float:left;" class="ui-icon ui-icon-triangle-1-e"></span>Costomerized I18n Tags</a></h3>
                                    <ul>
                                        <li>
                                            There are 2 kinds of customized tags in this tool: <strong>I18n Resource Tag</strong>(for extracting data) and <strong>JSP Tag</strong>(for i18n message display), respectively.
                                        </li>
                                        <li>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span>
                                            <strong>Resource Tag</strong> is defined as below:
                                            <div style="margin: 10px 20px; padding: 5px 10px; border:1px solid #ccc;">
                                                <span class="tag"><strong>#I18NSTART</strong> key=<i>xxxx</i><strong>#</strong>......<strong>#I18NEND#</strong></span>
                                            </div>
                                            This tag is used to mark those text resources that need to be localized with different language. 
                                            Thus, the text enclosed between <span class="tag"><strong>#I18NSTART#</strong></span> and <span class="tag"><strong>#I18NEND#</strong></span>
                                            will be extracted as i18n resource. In addition, each tag supports to specify a parameter '<span class="tag">key</span>' which is used to set as the resource name.
                                            Actually, the enclosed text will be saved into DB and finally the enclosed text will be substituted with the customized JSP tag <span class="tag">&lt;indaba:msg&gt;</span>. 
                                            And the '<span class="tag">key</span>' will be also set to the JSP tag as well. If the key is not specified in resource tag, by default, the engine will automatically 
                                            generate a unreadable key(namely resource name) based the sequence number. It strongly suggests to explicitly specify the resource <span class="tag">key</span>.
                                        </li>
                                        <li>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <strong>I18n JSP Tag</strong> is defined as below: 
                                            <div style="margin: 10px 20px; padding: 5px 10px; border:1px solid #ccc;">
                                                <span class="tag">&lt;<strong>indaba:msg</strong></span> 
                                                <span style="color:green;">key="xxx"</span>  
                                                <span style="color:green;">args="${xxx}"</span> 
                                                <span class="tag">/&gt;</span><br/>
                                                <div style="margin: 0px 20px;">
                                                    <span class="tag">&lt;<strong>indaba:arg</strong></span> 
                                                    <span style="color:green;">value="${xxx}"</span> 
                                                    <span class="tag">&gt;</span><br/>
                                                </div>
                                                <span class="tag">&lt;/<strong>indaba:msg</strong>&gt;</span>
                                                <ul style="margin: 0px; padding: 0px;">
                                                    <li><span class="tag" style="width:30px" >key</span> - [MANDATORY] The resource name.</li>
                                                    <li><span class="tag" style="width:30px">args</span> - [OPTIONAL] The argument will be used to replace the pattern. e.g. Welcome {0}. NOte, this argument MUST be a request attribute(${xxx}).</li>
                                                    <li>
                                                        <span class="tag" style="width:160px">
                                                            <span class="tag">&lt;<strong>indaba:arg</strong></span> <span style="color:green;">value="${xxx}"</span> <span class="tag">&gt;</span>
                                                        </span> - [OPTIONAL] If a test resource includes one more arguments, &lt;indaba:msg&gt; can have multiple sub tags &lt;indaba:arg&gt;.
                                                    </li>
                                                </ul>
                                                This tag is a customized JSP tag for i18n message. In runtime, the tag engine will output and display the excepted localized message.
                                                Typically, this tag will look like: <span class="tag">&lt;<strong>indaba:msg</strong></span> <span style="color:green;">key="xxx"</span> <span class="tag">/&gt;</span>
                                        </li>
                                    </ul>
                                </div>
                                <div>
                                    <h3><a href="#"><span style="float:left;" class="ui-icon ui-icon-triangle-1-e"></span>Best Practice & Process</a></h3>
                                    <ol>
                                        <li>
                                            <strong>Configuration</strong>. First of all, correctly configure the work directories: <span class="tag">i18n.resource.src.orig.dir</span>, <span class="tag">i18n.resource.src.tagged.dir</span> and <span class="tag">i18n.resource.output.dir</span>.
                                            It is better set these directories under your local workspace. Assume work home of indaba is $INDABA_HOME(e.g. D:\workspace\indaba\trunk\Builder). Thus, these 3 directory will be:<br/>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <span class="prop">i18n.resource.src.orig.dir:</span>$INDABA_HOME\web<br/>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <span class="prop">i18n.resource.src.tagged.dir:</span>$INDABA_HOME\web\i18n_tagged<br/>
                                            <span style="float:left;" class="ui-icon ui-icon-bullet"></span> <span class="prop">i18n.resource.output.dir:</span>$INDABA_HOME\web\i18n_output<br/>
                                        </li>
                                        <li>
                                            <strong>Copy the source file(JSP or HTML)</strong> that needs to be localizationed from <span class="tag">i18n.resource.src.orig.dir</span> to <span class="tag">i18n.resource.src.tagged.dir</span>.   
                                        </li>
                                        <li>
                                            <strong>Mark/enclose the text resources</strong> in the files under <span class="tag">i18n.resource.src.tagged.dir</span> with 
                                            the customized tag(<span class="tag"><strong>#I18NSTART</strong> key=<i>xxxx</i><strong>#</strong>......<strong>#I18NEND#</strong></span>).   
                                        </li>
                                        <li>
                                            <strong>Run tag engine to extract the text resources.</strong> After marked all the text resources, go to 'I18N Source Files' tab and find out the corresponding file. 
                                            Finally, click <span style="display: inline-block;"><span class="ui-icon ui-icon-gear" style="float: left;"></span></span> to extract all resource 
                                            tags and generate/output the file with customized JSP tags under <span class="tag">i18n.resource.output.dir</span>.
                                        </li>
                                        <li>
                                            <strong>Verify if tags works.</strong>. On 'I18N Source Files' tab, click <span style="display: inline-block;"><span class="ui-icon ui-icon-arrowthickstop-1-e" style="float: left;"></span></span> to 
                                            copy the output file(with customized JSP tags) to original source directory <span class="tag">i18n.resource.src.orig.dir</span>. This operation will overwrite the original JSP/HTML file.
                                            Finally, refresh the corresponding page of the source file.
                                        </li>
                                        <li>
                                            <strong>Submit tagged files.</strong> Finally, after completed and verified the tags in the file, 
                                            you should check in the tagged files under <span class="tag">i18n.resource.src.tagged.dir</span> to SVN. 
                                            PLEASE NEVER CHECK IN THOSE FILES UNDER <span class="tag">i18n.resource.src.orig.dir</span>.
                                            <span>
                                        </li>
                                    </ol>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </body>
        </html>
    </c:otherwise>
</c:choose>