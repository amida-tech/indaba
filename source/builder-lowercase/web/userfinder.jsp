<%-- 
    Document  : userfinder
    Created on: 2011-11-10, 12:21:26
    Author    : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="css/flexigrid.pack.css" />
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/flexigrid.pack.js"></script>
        <script type="text/javascript" src="js/common.js" charset="utf-8"></script>
        <style type="text/css">
            /* Some basic styles for the overall page - you can ignore these! */

            body { width: 1088px; }

            body, h1, p, fieldset, label, legend, input, select, textarea, table, tr, td, div { font-family: Arial, Helvetica, sans-serif; font-size: 14px; }

            * {padding: 0px; margin: 0px;}

            fieldset {margin: 10px 0px 0px 10px; padding: 5px 10px;-moz-border-radius: 5px; -webkit-border-radius: 5px;border-radius: 5px;}
            .blk {
                display: block;
            }


            fieldset { border: 1px solid #d5dfe5; margin-bottom: 10px; padding: 0px 15px 15px 15px; }
            fieldset legend { font-size: 1.2em; font-family: Arial, Helvetica, sans-serif; color: #1B75BB; margin: 0; padding: 0 10px 0 10px; font-weight: bold; text-transform: uppercase; }
            fieldset label { display: block;  margin-top: 5px; font-weight: bold;}

            textarea {width: 500px; height: 30px;}
            .btn-blk { padding: 0px; text-align: center}
            .required {color: orangered; padding: 0px 1px;}
            .err{color: orangered; margin: 10px 0px 0px 0px; display: none;}
            ul.tabs {
                margin: 0;
                padding: 0;
                float: left;
                list-style: none;
                height: 32px; /*--Set height of tabs--*/
                border-bottom: 1px solid #999;
                border-left: 1px solid #999;
                width: 100%;
            }
            ul.tabs li {
                float: left;
                margin: 0;
                padding: 0;
                height: 31px; /*--Subtract 1px from the height of the unordered list--*/
                line-height: 31px; /*--Vertically aligns the text within the tab--*/
                border: 1px solid #999;
                border-left: none;
                margin-bottom: -1px; /*--Pull the list item down 1px--*/
                overflow: hidden;
                position: relative;
                background: #e0e0e0;
            }
            ul.tabs li a {
                text-decoration: none;
                color: #000;
                display: block;
                font-size: 12px;
                color: #333;
                padding: 0 20px;
                border: 1px solid #fff; /*--Gives the bevel look with a 1px white border inside the list item--*/
                outline: none;
            }
            ul.tabs li a:hover {
                background: #ccc;
            }
            html ul.tabs li.active, html ul.tabs li.active a:hover  { /*--Makes sure that the active tab does not listen to the hover properties--*/
                background: #fff;
                border-bottom: 1px solid #fff; /*--Makes the active tab look like it's connected with its content--*/
            }
            .main-content {
                margin-left: auto;
                margin-right: auto;
                width: 100%;
                margin: 20px 10px 0px 20px;
            }
            .tab-container {
                border: 1px solid #999;
                border-top: none;
                overflow: hidden;
                clear: both;
                float: left; 
                background: #fff;
                width: 100%;
                margin: 0px 0px 20px 0px;
            }
            .tab-content {
                padding: 20px;
                font-size: 1.2em;
            }
            table.userfinder-list {
                border: 1px solid #d5dfe5;
            }
            table.userfinder-list thead{
                background: #1B75BB;
                color: #fff;
            }
            table.userfinder-list thead th{
                padding: 5px 2px;
            }
            table.userfinder-list tr td{
                border: 1px solid #d5dfe5;
            }
            table.userfinder-list tr td{
                padding: 5px 2px;
            }

            table div{
                font-size: 12px;
                white-space:normal;
                word-break: break-all;
            }
            div.ftitle {
                color: #1B75BB;
            }
            a {
                text-decoration: none;
                color: #2575cf;
            }
            a:hover {
                color: orange;
            }
            td.cf-action {
                vertical-align: middle;
                text-align: center;
            }
            div.cf-action-blk {
                width: 105px;
                vertical-align: middle;
                text-align: center;
            }
            span.cf-fire-count {
                color: orange;
            }
        </style>
        <script type="text/javascript" charset="utf-8">
            $(function() {
                $('input,textarea,select').uniform();
                
                //When page loads...
                $(".tab-content").hide(); //Hide all content
                $("ul.tabs li:first").addClass("active").show(); //Activate first tab
                $(".tab-content:first").show(); //Show first tab content

                //On Click Event
                $("ul.tabs li").click(function() {

                    $("ul.tabs li").removeClass("active"); //Remove any "active" class
                    $(this).addClass("active"); //Add "active" class to selected tab
                    $(".tab-content").hide(); //Hide all tab content

                    var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
                    $(activeTab).fadeIn(); //Fade in the active ID content
                    return false;
                });
                ////////////////////////////////////

                $(".flexigrider").flexigrid({
                    colModel: [
                        {display: $.i18n.message('common.label.project'), name: 'project', width: 150, sortable: true, align: 'left'},
                        {display: $.i18n.message('common.label.role'), name: 'role', width: 100, sortable: true, align: 'left'},
                        {display: $.i18n.message('common.label.assigneduser'), name: 'assigned_user', width: 120, sortable: true, align: 'left'},
                        {display: $.i18n.message('common.label.casesubject'), name: 'case_subject', width: 200, sortable: true, align: 'left'},
                        {display: $.i18n.message('common.label.priority'), name: 'case_body', width: 300, sortable: true, align: 'left'},
                        {display: $.i18n.message('common.label.action') + ' | <span id="fire-all-blk"><a href="#" onclick="return fireAllUserfinders();"><img src="images/fire.png" /> '+$.i18n.message('common.label.fireall')+'</a></span>', name: 'action', width: 106, sortable: true, align: 'left'}
                        //{display: 'Priority', name: 'case_priority', width: 37, sortable: true, align: 'left'}
                    ],
                    searchitems: [
                        {display: $.i18n.message('common.label.project'), name: 'project'},
                        {display: $.i18n.message('common.label.assigneduser'), name: 'assigned_user', isdefault: true}
                    ],
                    sortname: $.i18n.message('common.label.project'),
                    sortorder: "asc",
                    //usepager: true,
                    title: $.i18n.message('common.js.msg.usertriggerlist'),
                    //useRp: true,
                    //rp: 15,
                    //showTableToggleBtn: true,
                    width: 1050,
                    height: 400
                });
                ///////////////////////////////////
                $('#project-id').change(function(){
                    var prjid = $(this).val();
                    $('#role-id').children(function(){
                        if(!$(this).val().empty() && $(this).val() > 0) {
                            $(this).remove();
                        }
                    });
                    if(prjid > 0) {
                        $.post('userfinder.do',
                        {
                            action: 'getrole',
                            prjid: prjid
                        },
                        function(resp){
                            //alert(resp);
                            var json = JSON.parse(resp);
                            if(json.ret == 0) {
                                if(json.idList.length > 0) {
                                    for(var i = 0; i < json.idList.length; ++i) {
                                        $('#role-id').append('<option value="'+ json.idList[i]+'">' + json.nameList[i] + '</option>');
                                    }
                                }
                            } else {
                                //showErrorDlg('Error', 'Fail to achive goal.');
                            }
                        });
                    }
                });
            });

            function validate() {
                return (checkField('desc', $.i18n.message('common.js.alert.descempty')) &&
                    checkField('project-id', $.i18n.message('common.js.alert.prjnoselected')) &&
                    checkField('role-id', $.i18n.message('common.js.alert.rolenoselected')) &&
                    checkField('assigned-user-id', $.i18n.message('common.js.alert.noassigneduser')) &&
                    checkField('case-subject', $.i18n.message('common.js.alert.nocasesubject')) &&
                    checkField('case-body', $.i18n.message('common.js.alert.nocasebody'))
            );
            }

            function checkField(id, errmsg) {
                if($('#' + id).val().empty()) {
                    $('#errmsg').text(errmsg);
                    $('.err').show();
                    return false;
                }else{
                    $('.err').hide();
                    return true;
                }

            }
            function fireAllUserfinders() {
                //$('#fire-all-blk').empty();
                //$('#fire-all-blk').append('<img src="images/fire.png"/>');
                $('div.cf-action-blk a').each(function(){
                    fireUserfinder($(this).attr('cfid'));
                });
                //$('#fire-all-blk').empty();
                //$('#fire-all-blk').append('<a href="#" onclick="return fireAllUserfinders();">Fire All</a>');
                return false;
            }
            function fireUserfinder(cfid){
                $('td#cf-action-' + cfid).empty();
                $('td#cf-action-' + cfid).append('<div class="cf-action-blk"><img src="images/wait.gif" width="20px;"></div>');
                $.post('fireuserfinder.do',
                {
                    userfinderId: cfid,
                    format: 'json'
                },
                function(resp){
                    var json = JSON.parse(resp);
                    if(json.ret == 0) {
                        $('td#cf-action-' + cfid).empty();
                            
                        var cfActionHtml = '<div class="cf-action-blk"><a href="#" onclick="return fireUserfinder(' + cfid + ')" cfid="' + cfid + '"><img src="images/fire.png"/> '+$.i18n.message('common.label.fire')+'</a>' +
                            '<div>'+$.i18n.message('common.js.msg.total')+' <span class="cf-fire-count">' + json.total + '</span>, '+$.i18n.message('common.js.msg.new')+' <span class="cf-fire-count">' + json.newadd + '</span></div></div>';
                        $('td#cf-action-' + cfid).append(cfActionHtml);
                    } else {
                        //showErrorDlg('Error', 'Fail to achive goal.');
                    }
                });
                return false; 
            };
        </script>
        <title><indaba:msg key='jsp.userfinder.pagetitle' /></title>
    </head>
    <body>
        <div class="main-content">
            <ul class="tabs">
                <li><a href="#user-trigger-list"><indaba:msg key='jsp.userfinder.viewList' /></a></li>
                <!-- <li><a href="#view-user-trigger-list">View User Tirgger List</a></li> -->
                <li><a href="#add-user-trigger"><indaba:msg key='common.msg.addtrigger' /></a></li>
            </ul>
            <div class="tab-container">
                <div id="user-trigger-list" class="tab-content">
                    <c:choose>
                        <c:when test="${empty userTriggers}">
                            <div><indaba:msg key='jsp.userfinder.noData' /></div>
                        </c:when>
                        <c:otherwise>
                            <table class="flexigrider" cellspacing="0" cellpadding="0" border="0" >
                                <tbody>
                                    <c:forEach var="cf" items="${userTriggers}">
                                        <tr>
                                            <td>${cf.projectName}</td>
                                            <td>${cf.roleName}</td>
                                            <td>${cf.assignedUsername}</td>
                                            <td>${cf.caseSubject}</td>
                                            <td>${cf.caseBody}</td>
                                            <td id="cf-action-${cf.id}" class="cf-action"><div class="cf-action-blk"><a href="#" cfid="${cf.id}" onclick="return fireUserfinder(${cf.id})"><img src="images/fire.png" /> <indaba:msg key='common.label.fire' /></a></div></td>
                                            <!--
                                            <td>
                                            <c:choose>
                                                <c:when test="${cf.casePriority == 1}">
                                                    Low
                                                </c:when>
                                                <c:when test="${cf.casePriority == 2}">
                                                    Medium
                                                </c:when>
                                                <c:otherwise>
                                                    High
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                            -->
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
                <!--
                <div id="view-user-trigger-list" class="tab-content">
                <c:choose>
                    <c:when test="${empty userTriggers}">
                        <div>No User Trigger Defined.</div>
                    </c:when>
                    <c:otherwise>
                        <table class="userfinder-list" cellpadding="0" cellspacing="0" >
                            <thead>
                            <th>Project</th>
                            <th>Role</th>
                            <th>Assigned User</th>
                            <th>Case Subject</th>
                            <th>Case Body</th>
                            <th>Case Priority</th>
                            </thead>
                            <tbody>
                        <c:forEach var="cf" items="${userTriggers}">
                            <tr>
                                <td>${cf.projectName}</td>
                                <td>${cf.roleName}</td>
                                <td>${cf.assignedUsername}</td>
                                <td>${cf.caseSubject}</td>
                                <td>${cf.caseBody}</td>
                                <td>${cf.casePriority}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                    </c:otherwise>
                </c:choose>
            </div>
                -->
                <div id="add-user-trigger" class="tab-content">
                    <form method="post" action="userfinder.do" onsubmit="return validate()" >
                        <fieldset>
                            <legend><indaba:msg key='common.msg.addtrigger' /></legend>
                            <input type="hidden" name="action" value="add" />

                            <label for="desc" class="blk"><indaba:msg key='common.label.desc' />:</label>
                            <textarea id="desc" name="desc"></textarea>

                            <label for="prjid"><indaba:msg key='common.label.project' /> (<span class="required">*</span>): </label>
                            <select id="project-id" name="prjid" class="default" >
                                <option value=""><indaba:msg key='common.label.projectChoose' /></option>
                                <c:forEach var="prj" items="${projects}">
                                    <option value="${prj.id}">${prj.codeName}</option>
                                </c:forEach>
                            </select>

                            <label for="roleid"><indaba:msg key='common.label.role' /> (<span class="required">*</span>): </label>
                            <select id="role-id" name="roleid" class="default" >
                                <option value=""><indaba:msg key='jsp.userfinder.add.roleChoose' /></option>
                            </select>

                            <label for="assignedUserId"><indaba:msg key='common.label.assigneduser' /> (<span class="required">*</span>): </label>
                            <select id="assigned-user-id" name="assignedUserId" class="default" >
                                <option value=""><indaba:msg key='jsp.userfinder.add.userChoose' /></option>
                                <c:forEach var="user" items="${users}">
                                    <option value="${user.id}">${user.username}</option>
                                </c:forEach>
                            </select>

                            <label for="caseSubject"><indaba:msg key='jsp.userfinder.add.caseSubject' /> (<span class="required">*</span>): </label>
                            <input id="case-subject" type="text" size="94" name="caseSubject" />

                            <label for="caseBody" class="blk"><indaba:msg key='common.label.casebody' />: </label>
                            <textarea id="case-body" name="caseBody"></textarea>

                            <!--
                            <label for="casePriority">Case Priority (<span class="required">*</span>): </label>
                            <input type="radio" name="casePriority" value="1" /> Low
                            <input type="radio" name="casePriority" value="2"/> Medium
                            <input type="radio" name="casePriority" value="3" checked="checked"/> High
                                                        <label for="attachUser">Attach User (<span class="required">*</span>): </label>
                                                        <input type="radio" name="attachUser" value="true" checked="checked" /> Yes
                                                        <input type="radio" name="attachUser" value="false"/> No

                                                        <label for="attachContent">Attach Content (<span class="required">*</span>): </label>
                                                        <input type="radio" name="attachContent" value="true" checked="checked" /> Yes
                                                        <input type="radio" name="attachContent" value="false"/> No


                                                        <label for="status">Status (<span class="required">*</span>): </label>
                                                        <input type="radio" name="status" value="0" checked="checked"/> Open
                                                        <input type="radio" name="status" value="1"/> Closed
                            -->
                            <div class="err"><strong><indaba:msg key='jsp.userfinder.add.error' />: </strong><span id="errmsg"></span></div>
                        </fieldset>
                        <div class="btn-blk">
                            <input type="reset" name="reset" value="Reset" />
                            <input type="submit" name="submit" value="Add User Tirgger" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
