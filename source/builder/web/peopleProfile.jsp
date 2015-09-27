<%--
    Document   : people
    Created on : Mar 2, 2010, 8:38:56 PM
    Author     : Luke
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.peopleProfile.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" media="screen"/>
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/button.js"></script>

        <script type="text/javascript">
            var isIE = false;
            var isFF = false;
            var isSa = false;

            if((navigator.userAgent.indexOf("MSIE")>0) && (parseInt(navigator.appVersion) >=4))
                isIE = true;
            if(navigator.userAgent.indexOf("Firefox")>0)
                isFF = true;
            if(navigator.userAgent.indexOf("Safari")>0)
                isSa = true;

            function edit()
            {
                if(document.getElementById('personalinformation').style.display !="none"){
                    document.getElementById('personalinformation').style.display = "none";
                    document.getElementById('editpersonalinfo').style.display="";
                    document.getElementById('photo').style.display="none";
                    document.getElementById("editButton").innerHTML = $.i18n.message('common.btn.view');
                } else {
                    document.getElementById('photo').style.display="";
                    cancelinfo();
                }
                document.getElementById('changepwd').style.display="none";
            }

            function submitinfo(){
                $.alerts.okButton=$.i18n.message('common.btn.save');
                jConfirm($.i18n.message('common.js.peopleProfile.savePopup'), "", function(r) {
                            if (r){
                                postdata();
                                document.getElementById('changepwd').style.display="none";
                                document.getElementById('uploadimg').style.display="none";
                            }
                        });
            }

            function cancelinfo()
            {
                var radios = document.getElementsByName("emaillevelname");
                for(var i=0;i<radios.length;i++)
                {
                    if(radios[i].value == ${user.emailDetailLevel})
                    {
                        radios[i].checked = true;
                    }
                }
                document.getElementById('personalinformation').style.display = "";
                document.getElementById('editpersonalinfo').style.display="none";
                document.getElementById('changepwd').style.display="none";
                document.getElementById('uploadimg').style.display="none";
                document.getElementById("editButton").innerHTML = $.i18n.message('common.btn.edit');
            }

            function change(){
                document.getElementById('changepwd').style.display="";
                document.getElementById('uploadimg').style.display="none";
                document.getElementById('editpersonalinfo').style.display="none";
            }

            function uploadphoto(){
                $('#peolple-icon-file').attr("value", "");
                document.getElementById('uploadimg').style.display="";
                //document.getElementById('changepwd').style.display="none";
            }

            function loadImage(id, imgUrl) {
                var maxWidth = 160;
                var maxHeight = 240;
                var img = new Image();
                img.onload=function() {
                    var width = img.width;
                    var height = img.height;
                    if(width > maxWidth){
                        height = height * (maxWidth / width);
                        width = maxWidth;
                    }
                    if(height > maxHeight){
                        width = width * (maxHeight / height);
                        height = maxHeight;
                    }
                    $(id).css("width", width);
                    $(id).css("height", height);
                    $(id).attr("src", imgUrl);
                };
                img.src=imgUrl;
            }

            function checkPhotoType() {
                var filename = $('#peolple-icon-file').attr("value");
                if( filename == null || !filename.toLowerCase().match( "/\\.jpeg$|\\.jpg$|\\.gif$|\\.png$|\\.bmp$/i" ) ) {
                    $("#err-hint").html($.i18n.message('common.js.alert.invalidimage', ['<I>jpg, gif, png, bmp, ico and svg</I>.']));
                    $('#err-box').css("display", "block");
                    $('#preview-box').css("display", "none");
                    $('#peolple-icon-file').attr("value", "");
                } else {
                    $("#err-hint").html("");
                    $('#err-box').css("display", "none");
                    var maxWidth = 160;
                    var maxHeight = 240;
                    var img = new Image();
                    var imgUrl=(isIE)?document.getElementById('peolple-icon-file').value:$('#peolple-icon-file').get(0).files[0].getAsDataURL();
                    img.onload = function() {
                        //alert("========= onload ============");
                        var width = img.width;
                        var height = img.height;
                        if(width > maxWidth){
                            height = height * (maxWidth / width);
                            width = maxWidth;
                        }
                        if(height > maxHeight){
                            width = width * (maxHeight / height);
                            height = maxHeight;
                        }
                        
                        $('#icon-preview').css("width", width);
                        $('#icon-preview').css("height", height);
                        $('#icon-preview').attr("src", imgUrl);
                        $('#preview-box').css("display", "block");
                    };
                    img.src=imgUrl;
                    //alert("[after] > imgUrl: " + imgUrl);
                }
            }
            
            function checkPhoto() {
                var filename = $('#peolple-icon-file').attr("value");
                if (filename != "") {
                    return ture;
                }
                else{
                    $("#err-hint").html($.i18n.message('common.js.alert.noimagespecified'));
                    $('#err-box').css("display", "block");
                    return false;
                }
            }

            function cancelUploadphoto(){
                $('#peolple-icon-file').attr("value", "");
                $('#uploadimg').css("display", "none");
                $('#err-box').css("display", "none");
                $('#preview-box').css("display", "none");
                $('#err-hint').html("");
            }

            function submitpwd(){
                document.getElementById('personalinformation').style.display = "none";
                var curpwd = document.getElementById('curpwd').value;
                var newpwd = document.getElementById('newpwd').value;
                var confirm = document.getElementById('confirm').value;
                var errElmt1 = document.getElementById("errmsgCur");
                errElmt1.innerHTML="";
                var errElmt2 = document.getElementById("errmsgNew");
                errElmt2.innerHTML="";
                var errElmt3 = document.getElementById("errmsgCon");
                errElmt3.innerHTML="";
                if((curpwd + '.' == ".")||(newpwd + '.' == ".")||(confirm + '.' == ".")){
                    if (curpwd + '.' == "."){
                        var errElmt = document.getElementById("errmsgCur");
                        errElmt.innerHTML = $.i18n.message('common.js.alert.curpasswdempty');
                    }
                    if(newpwd + '.' == "."){
                        var errElmt = document.getElementById("errmsgNew");
                        errElmt.innerHTML = $.i18n.message('common.js.alert.newpasswdempty');
                    }
                    if(confirm + '.' == "."){
                        var errElmt = document.getElementById("errmsgCon");
                        errElmt.innerHTML = $.i18n.message('common.js.alert.confirmpasswdempty');
                    }
                }else{
                    if(newpwd==confirm){
                        postpwd();
                    }else
                    {
                        var errElmt = document.getElementById("errmsgNew");
                        errElmt.innerHTML = $.i18n.message('common.js.alert.passwdinconsistent');
                    }
                }
            }

            function cancelpwd(){
                var errElmt1 = document.getElementById("errmsgCur");
                errElmt1.innerHTML="";
                var errElmt2 = document.getElementById("errmsgNew");
                errElmt2.innerHTML="";
                var errElmt3 = document.getElementById("errmsgCon");
                errElmt3.innerHTML="";
                document.getElementById('personalinformation').style.display = "none";
                document.getElementById('editpersonalinfo').style.display="";
                document.getElementById('changepwd').style.display="none";
            }

            function postdata(){
                var radios = document.getElementsByName("emaillevelname");
                var elevel;
                for(i=0; i<radios.length; i++){
                    if(radios[i].checked){
                        elevel = radios[i].value;
                        break;
                    }
                }
                radios = document.getElementsByName("forwardmsg");
                var forwardmsg;
                for(i=0; i<radios.length; i++){
                    if(radios[i].checked){
                        forwardmsg = radios[i].value;
                        break;
                    }
                }
                $.ajax({                                                 //调用jquery的ajax方法
                    type: "POST",
                    url: "changeinfo.do",
                    data: "firstName="+$("#firstName").val()+"&lastName="+$("#lastName").val()
                        +"&email="+$("#email").val()+"&phone="+$("#phone").val()
                        +"&cellPhone="+$("#cellPhone").val()+"&address="+$("#address").val()
                        +"&bio="+$("#bio").val()+"&location="+$("#location").val()
                        +"&languageId="+$("#languageId").val()+"&forwardmsg="+forwardmsg
                        +"&uid="+${uid}+"&emaillevel="+elevel,
                    success: function(){
                        window.location.reload();
                    }
                });
            }

            function postpwd(){
                $.ajax({                                                 //调用jquery的ajax方法
                    type: "POST",
                    url: "changepwd.do",
                    data: "curpwd="+$("#curpwd").val()+"&newpwd="+$("#newpwd").val()
                        +"&uid="+${uid},
                    success: function(msg){
                        if(msg.toString().length==29){
                            var errElmt = document.getElementById("errmsgCur");
                            errElmt.innerHTML = $.i18n.message('common.js.alert.passwdinvalid');
                        }else if(msg.toString().length==26){
                            var errElmt = document.getElementById("errmsgCur");
                            errElmt.innerHTML = $.i18n.message('common.js.alert.passwdchngsuccess');;
                            window.location.reload();
                        }
                    }
                });
            }
        </script>
        <script type="text/javascript">
            $(document).ready(function() {
                /* This is basic - uses default settings */
                $("a#single_image").fancybox();

                /* Using custom settings */
                //$("a#inline").fancybox({'hideOnContentClick': false});

                /* Apply fancybox to multiple items */
                $("a.group").fancybox({
                    'transitionIn'	:	'elastic',
                    'transitionOut'	:	'elastic',
                    'speedIn'	:	600,
                    'speedOut'	:	200,
                    'overlayShow'	:	false
                });
            });


            function setAssignment(id, name, dueTime, userName) {
                /*
                document.getElementById('assignmentId').value = id;
                document.getElementById('taskName').innerHTML = name;
                document.getElementById('dueTime').value = dueTime;
                document.getElementById('newDeadline').value = dueTime;
                document.getElementById('userName').innerHTML = userName;
                 */

                var msg = "<div style='margin-left:-20px'>"
                    + $.i18n.message('common.msg.setdeadline', [userName, ""]) 
                    + "<div style='height:8px'>&nbsp;</div>"
                    + name + "<div style='height:8px'>&nbsp;</div>"
                    + $.i18n.message('common.js.msg.newdeadline') + " (YYYY-MM-DD): <input type='text' id='newDeadline' name='newDeadline' size='10' maxlength='10' value='" + dueTime + "'>"
                    + "</div>";

                var result;
                jConfirm(msg, $.i18n.message('common.btn.editdeadline'), function(r){
                    if (r)
                        saveDeadline(result, dueTime, id);
                },
                function(){
                    result = $("#newDeadline").val();
                },
                function(){
                    $("#newDeadline").datepicker(({ dateFormat: 'yy-mm-dd' }));
                });
                return false;
            }

            function endPeerReviewAssignment(id, userName, taskName, horseName) {
                /*
                document.getElementById('exitAssignmentId').value = id;
                document.getElementById('exitTaskName').innerHTML = taskName;
                document.getElementById('exitHorseName').innerHTML = horseName;
                document.getElementById('exitUserName').innerHTML = userName;
                 */
                var msg = $.i18n.message('common.js.msg.endassignment', [userName, taskName, horseName])
                    + "<div style='height:16px'>&nbsp;</div>"
                    + "<input type='radio' name='exitOption' value='0' checked>" + $.i18n.message('common.js.msg.partialsubmission')
                    + "<div style='height:8px'>&nbsp;</div>"
                    + "<input type='radio' name='exitOption' value='1'>" + $.i18n.message('common.js.msg.forcecomplete')
                    + "<div style='height:8px'>&nbsp;</div>"
                    + "<input type='hidden' id='exitAssignmentId' name='exitAssignmentId' value='" + id + "'/>";

                var result;
                jConfirm(msg, $.i18n.message('common.js.msg.exitassignment'), function(r){
                    if (r) {
                        confirmEndPeerReviewAssignment(id, userName, taskName, horseName, result);
                    }
                },
                function(){
                    result = $("input[name='exitOption']:checked").val();
                });
                return false;
            }

            function endAssignment(id, userName, taskName, horseName) {
                var msg = $.i18n.message('common.js.msg.endassignment', [userName, taskName, horseName]) + "<br/><br/>" + $.i18n.message('common.js.alert.undone');
                jConfirm(msg, $.i18n.message('common.js.alert.title.confirm'), function(r) {
                    if (r){
                        submitEndAssignment(id, 0);
                    }
                });
            }

            function confirmEndPeerReviewAssignment(id, userName, taskName, horseName, option) {
                var msg = $.i18n.message('common.js.msg.endassignment', [userName, taskName, horseName]) + ".<br/><br/>";
                var selectionMsg = $.i18n.message('common.js.msg.selected');
                if (option == 0) {
                    selectionMsg +=  $.i18n.message('common.js.msg.partialsubmission');
                } else {
                    selectionMsg += $.i18n.message('common.js.msg.forcecomplete');
                }
                selectionMsg += "<br/><br/>" + $.i18n.message('common.js.alert.undone')
                jConfirm(msg + selectionMsg, $.i18n.message('common.js.alert.title.confirm'), function(r) {
                    if (r){
                        submitEndAssignment(id, option);
                    }
                });
            }

            function submitEndAssignment(id, option) {
                var parameters = new Object();
                parameters.assignid = id;
                parameters.exitOption = option;
                parameters.action = "exitAssignment";

                $.ajax({
                    type: "POST",
                    url: "assignment.do",
                    data: parameters,
                    cache: false,
                    async: false,
                    success: function(response) {
                        //$.fancybox.close();
                        window.location.reload();
                    },
                    error: function(response) {
                        alert(response);
                        //$.fancybox.close();
                    }
                });
            }

            /*
            function submitEndPeerReviewAssignment() {
                //$.fancybox.close();
                var assignid = document.getElementById('exitAssignmentId').value;
                var userName = document.getElementById('exitUserName').innerHTML;
                var taskName = document.getElementById('exitTaskName').innerHTML;
                var horseName = document.getElementById('exitHorseName').innerHTML;
                var option = $("input[name=exitOption][type=radio]:checked").val();

                confirmEndPeerReviewAssignment(assignid, userName, taskName, horseName, option);
            }
             */

            function saveDeadline(newDeadline, dueTime, assignmentId) {
                if (newDeadline == dueTime)
                    return;
                
                var deadlineStr = newDeadline.replace(/-/ig, "/");
                if (new Date(deadlineStr) < new Date())
                    return;
                
                var parameters = new Object();
                parameters.assignid = assignmentId;
                parameters.deadline = newDeadline;
                parameters.action = "saveDeadline";

                $.ajax({
                    type: "POST",
                    url: "assignment.do",
                    data: parameters,
                    cache: false,
                    async: false,
                    success: function(response) {
                        window.location.reload();
                    },
                    error: function(response) {
                        alert(response);
                    }
                });
            }
        </script>
        <style type="text/css">
            .row-item{
                min-height:20px;
                line-height:20px;
                padding-left:170px;
                position:relative;
            }
            .row-item label{
                display:inline;
                font-weight:bold;
                left:0;
                position:absolute;
                text-align:right;
                width:160px;
            }
            h3 a span {
                color: #194e96;
            }
        </style>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="people" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper people">
                <!--<div id="main-column">-->
                <div class="box">
                    <h3><indaba:msg key='jsp.peopleProfile.user' />: ${user.username}
                        <c:if test="${user.username == userName}">
                            <a href="#" class="rightDisplay" onclick="edit();"><span id="editButton"><indaba:msg key='common.btn.edit' /></span></a><!--class="rightDisplay"-->
                        </c:if>
                    </h3>
                    <table>
                        <tr>
                            <td>                        
                                <div id ="personalinformation"class="content">
                                    <c:if test="${knowUser.permission >= 1}">
                                        <b><u><indaba:msg key='jsp.peopleProfile.limitedBio' /></u></b><br/>
                                        <div class="row-item">
                                            <label><indaba:msg key='common.label.firstname' />:</label>
                                            ${user.firstName}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='common.label.lastname' />:</label>
                                            ${user.lastName}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='common.label.bio' />:</label>
                                            ${fn:escapeXml(user.bio)}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='common.label.location' />:</label>
                                            ${user.location}
                                        </div>
                                        <%--
                                        <div class="row-item">
                                            <label>Timezone:</label>
                                            ${timeZone}
                                        </div>
                                        --%>
                                        <!--
                                        <tr> <div>
                                            <b>Last Login Date:</b> ${lastLoginDate}
                                        </div></tr>  -->
                                    </c:if>
                                    <c:if test="${knowUser.permission >= 2}">
                                        <br/><b><u><indaba:msg key='jsp.peopleProfile.fullBio' /></u></b><br/>
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.phone' />:</label>
                                            ${user.phone}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.mobilePhone' />:</label>
                                            ${user.cellPhone}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='common.label.mailaddr' />:</label>
                                            ${user.address}
                                        </div>
                                    </c:if>
                                    <c:if test="${knowUser.permission == 3}">
                                        <br/><b><u><indaba:msg key='jsp.peopleProfile.sysInfo' /></u></b><br/>
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.email' />:</label>
                                            ${user.email}
                                        </div>                               
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.forwardInboxMsg' />:</label>
                                            <c:if test="${user.forwardInboxMsg == true}"><indaba:msg key='common.boolean.yes' /></c:if>
                                            <c:if test="${user.forwardInboxMsg == false}"><indaba:msg key='common.boolean.no' /></c:if>
                                            </div>
                                            <div class="row-item">
                                                <label><indaba:msg key='common.label.emailmsgdetaillevel' />:</label>
                                            <c:if test="${user.emailDetailLevel == 0}"><indaba:msg key='common.label.emailmsgdetaillevel.alert' /></c:if>
                                            <c:if test="${user.emailDetailLevel == 1}"><indaba:msg key='common.label.emailmsgdetaillevel.fullMsg' /></c:if>
                                            </div>
                                            <div class="row-item">
                                                <label><indaba:msg key='jsp.peopleProfile.language' />:</label>
                                            ${language.languageDesc}
                                        </div>
                                        <br/>
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.project' />:</label>
                                            <c:forEach items="${projects}" var="project" varStatus="status">
                                                ${project.codeName}<c:if test="${!status.last}">, </c:if>
                                            </c:forEach>
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.role' />:</label>
                                            ${roleName}
                                        </div>
                                        <br/>
                                        <div class="row-item">
                                            <label><indaba:msg key='common.label.createtime' />:</label>
                                            ${user.createTime}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.lastLoginTime' />:</label>
                                            ${user.lastLoginTime}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='jsp.peopleProfile.lastLogoutTime' />:</label>
                                            ${user.lastLogoutTime}
                                        </div>
                                        <div class="row-item">
                                            <label><indaba:msg key='common.label.status' />:</label>
                                            <c:if test="${user.status == 0}"><indaba:msg key='jsp.peopleProfile.inactive' /></c:if>
                                            <c:if test="${user.status == 1}"><indaba:msg key='jsp.peopleProfile.active' /></c:if>
                                            <c:if test="${user.status == 2}"><indaba:msg key='jsp.peopleProfile.deleted' /></c:if>
                                            </div>
                                    </c:if>
                                    <p align="center"><input type="button" class="medium button blue" onclick="window.location='newmsg.do?receiverId=${user.id}'" value="<indaba:msg key='jsp.peopleProfile.sendSitemail' />"/></p>
                                </div>

                                <div id ="editpersonalinfo" class="content" style="display:none">
                                    <indaba:msg key='jsp.peopleProfile.aboutYourUserInfo' /><br/>
                                    <table>
                                        <tr>
                                            <td nowrap width="100px" align="right"><b><indaba:msg key='common.label.firstname' /></b></td>
                                            <td><input type="text" name="firstName" id="firstName" value="${user.firstName}" size="40"/></td>
                                        </tr>
                                        <tr>
                                            <td nowrap align="right"><b><indaba:msg key='common.label.lastname' /></b></td>
                                            <td><input type="text" name="lastName" id="lastName" value="${user.lastName}" size="40"/></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><b><indaba:msg key='common.label.bio' /></b></td>
                                            <td><input type="text" name="bio" id="bio"  value="${fn:escapeXml(user.bio)}" size="90"/></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><b><indaba:msg key='common.label.location' /></b></td>
                                            <td><input type="text" name="location" id="location"  value="${user.location}" size="90"/></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><b><indaba:msg key='jsp.peopleProfile.language' /></b></td>
                                            <td>
                                                <select name="languageId" id="languageId" >
                                                    <c:forEach items="${languages}" var="language" varStatus="status">
                                                        <c:if test="${user.languageId == language.id}">
                                                            <option value="${language.id}" selected>${language.languageDesc}</option>
                                                        </c:if>
                                                        <c:if test="${user.languageId != language.id}">
                                                            <option value="${language.id}">${language.languageDesc}</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                        </tr>
                                    </table><br/>
                                    <b><indaba:msg key='jsp.peopleProfile.fullBioE' />:</b><br/>
                                    <table>
                                        <tr>
                                            <td width="100px" align="right"><b><indaba:msg key='jsp.peopleProfile.phone' /></b></td>
                                            <td><input type="text" name="phone" id="phone"  value="${user.phone}" size="40"/></td>
                                        </tr>
                                        <tr>
                                            <td nowrap align="right"><b><indaba:msg key='jsp.peopleProfile.mobilePhone' /></b></td>
                                            <td><input type="text" name="cellPhone" id="cellPhone"  value="${user.cellPhone}" size="40"/></td>
                                        </tr>
                                        <tr>
                                            <td nowrap align="right"><b><indaba:msg key='common.label.mailaddr' /></b></td>
                                            <td><input type="text" name="address" id="address"  value="${user.address}" size="90"/></td>
                                        </tr>
                                    </table><br/>
                                    <b><indaba:msg key='jsp.peopleProfile.sysInfoE' />:</b><br/>
                                    <table>
                                        <tr>
                                            <td nowrap width="100px" align="right"><b><indaba:msg key='jsp.peopleProfile.sysEmail' /></b></td>
                                            <td><input type="text" name="email" id="email" value="${user.email}" size="80" readonly /></td>
                                        </tr>
                                        <tr>
                                            <td nowrap align="right"><b><indaba:msg key='jsp.peopleProfile.forwardInboxMsg' /></b></td>
                                            <td nowrap>
                                                <c:if test="${user.forwardInboxMsg == true}">
                                                    <input type="radio"  name="forwardmsg" value="1" checked=/><indaba:msg key='common.boolean.yes' /> &nbsp;
                                                    <input type="radio"   name="forwardmsg" value="0"/><indaba:msg key='common.boolean.no' /></c:if>
                                                <c:if test="${user.forwardInboxMsg == false}">
                                                    <input type="radio"   name="forwardmsg" value="1" /><indaba:msg key='common.boolean.yes' /> &nbsp;
                                                    <input type="radio"  name="forwardmsg" value="0" checked/><indaba:msg key='common.boolean.no' /></c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td nowrap align="right"><b><indaba:msg key='common.label.emailmsgdetaillevel' /></b></td>
                                                <td nowrap>
                                                <c:if test="${user.emailDetailLevel == 0}">
                                                    <input type="radio"  name="emaillevelname" value="0" checked="true"/><indaba:msg key='common.label.emailmsgdetaillevel.alert' /> &nbsp;
                                                    <input type="radio"   name="emaillevelname" value="1"/><indaba:msg key='common.label.emailmsgdetaillevel.fullMsg' /></c:if>
                                                <c:if test="${user.emailDetailLevel == 1}">
                                                    <input type="radio"   name="emaillevelname" value="0" /><indaba:msg key='common.label.emailmsgdetaillevel.alert' /> &nbsp;
                                                    <input type="radio"  name="emaillevelname" value="1" checked/><indaba:msg key='common.label.emailmsgdetaillevel.fullMsg' /></c:if></td>
                                            </tr>
                                        </table><br/>
                                        <indaba:msg key='jsp.peopleProfile.sysTips' /> <br/>
                                        <br/>
                                        <div align="center">
                                            <input type ="button" name="save_change" id="save_change" class='large button blue icon-save' value="<indaba:msg key='common.btn.save' />" onclick="submitinfo();"/>
                                            &nbsp;&nbsp;&nbsp;
                                            <input type ="button" name="cancelinfo" id="cancelinfo" class='large button blue' value="<indaba:msg key='common.btn.cancel' />" onclick="cancelinfo();"/>
                                            &nbsp;&nbsp;&nbsp;
                                            <a href="#" onclick="change();"><indaba:msg key='jsp.peopleProfile.pwd.change' /></a>
                                        </div>
                                    </div>
                                    <div id ="changepwd"class="content" style="display:none">
                                        <table width="100%">
                                            <tr><div>
                                                    <td nowrap><p align="right"><b><indaba:msg key='jsp.peopleProfile.pwd.cur' /></b></p></td>
                                                    <td><p/><input name="curpwd" id="curpwd" type="password" value="" size="40"/></td>
                                                    <td><font color="red"><p class="errtxt" id="errmsgCur">
                                                                <logic:present name="errmsgCur" scope="request">
                                                                    <bean:define id="errmsgCur" name="errmsgCur"  toScope="request"/>
                                                                    <bean:write name="errmsgCur" /></logic:present></p></font>
                                                    </td>
                                                </div></tr>
                                            <tr><div>
                                                    <td nowrap><p align="right"><b><indaba:msg key='jsp.peopleProfile.pwd.new' /></b></p></td>
                                                    <td><p/><input type="password" name="newpwd" id="newpwd" value="" size="40"/></td>
                                                    <td><font color="red"><p class="errtxt" id="errmsgNew">
                                                                <logic:present name="errmsgNew" scope="request">
                                                                    <bean:define id="errmsgNew" name="errmsgNew"  toScope="request"/>
                                                                    <bean:write name="errmsgNew" /></logic:present></p></font>
                                                    </td>
                                                </div></tr>
                                            <tr><div>
                                                    <td nowrap><p align="right"><b><indaba:msg key='jsp.peopleProfile.pwd.new.confirm' /></b></p></td>
                                                    <td><p/><input type="password" name="confirm" id="confirm" value="" size="40"/></td>
                                                    <td><font color="red"><p class="errtxt" id="errmsgCon">
                                                                <logic:present name="errmsgCon" scope="request">
                                                                    <bean:define id="errmsgCon" name="errmsgCon"  toScope="request"/>
                                                                    <bean:write name="errmsgCon" /></logic:present></p></font>
                                                    </td>
                                                </div></tr>
                                        </table>
                                        <p align="center">
                                            <input type ="button" name="submitpwd" id="submitpwd" class='small button blue' value="<indaba:msg key='common.btn.submit' />" onclick="submitpwd();"/>
                                            &nbsp;&nbsp;&nbsp;
                                            <input type ="button" name="cancelpwd" id="cancelpwd" class='small button blue' value="<indaba:msg key='common.btn.cancel' />" onclick="cancelpwd();"/>
                                        </p>
                                    </div>
                                </td>
                                <td valign="top" style="width:200px;">
                                    <div id="photo" style="width:200px;">
                                    <c:choose>
                                        <c:when test="${user.username == userName}">
                                            <a href="#" onclick="uploadphoto();" title="<indaba:msg key='jsp.peopleProfile.changIconTips' />"><img id='peopleicon' src='' alt='people icon' style='margin:5px;' /></a>
                                        </c:when>
                                        <c:otherwise>
                                            <img id='peopleicon' src='' alt='<indaba:msg key='jsp.peopleProfile.peopleIcon' />' style='margin:5px;' />
                                        </c:otherwise>
                                    </c:choose>
                                    <script type="text/javascript">
                                        <!--
                                        loadImage("#peopleicon", "peopleicon?uid=${user.id}");
                                        //-->
                                    </script>
                                    <div id ="uploadimg" class="content" style="display:none;">
                                        <form method="post" action="changeimg.do" enctype="multipart/form-data" name="changeimg" id="changeimg">
                                            <input type="file" name="file" id="peolple-icon-file"  value="" size="12" onchange="checkPhotoType();" />
                                            <div style="margin-top:10px;">
                                                <input type ="submit" name="submit" id="submit" value="<indaba:msg key='jsp.peopleProfile.upload' />" onclick="return checkPhoto();"/>
                                                <input type ="button" name="cancel_upload" id="cancel_upload" value="<indaba:msg key='common.btn.cancel' />" onclick="cancelUploadphoto();"/>
                                            </div>
                                        </form>
                                        <div id="err-box" style="display:none; margin-top:10px; text-align: left; color: #CC0000; padding-left: 3px">
                                            <span style="font-weight: bold; text-decoration: underline;"><img src="images/warning.gif" alt=""/></span>
                                            <span id="err-hint"></span>
                                        </div>

                                        <div id="preview-box" style="display:none; margin-top:10px; text-align: left;">
                                            <span style="font-weight: bold; text-decoration: underline;"><indaba:msg key='common.btn.preview' />:</span><br/>
                                            <div style="text-align: center; padding-top: 10px;">
                                                <img id="icon-preview" src="" alt="icon preview" style="text-align: center; border: 1px solid gray; " align="center" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <!--</div>-->

                <div class="clear">
                </div>

                <indaba:view prjid="${prjid}" uid="${uid}" right="manage all users">
                    <div class="box">
                        <h3><indaba:msg key='jsp.peopleProfile.assignment' /><a href="#" class="toggleVisible"><img src='images/expand_icon.png' alt='expand' /></a></h3>
                        <div class="content" style="padding: 10px; display: none;">
                            <table width="100%" style="font-size: 12px;" class="highlight_row_onhover">
                                <indaba:manageassginments prjid="${prjid}" targetUid="${user.id}" subjectUid="${uid}" />
                            </table>
                        </div>
                    </div>

                    <c:if test='${fn:length(openCases) > 0}'>
                        <div class="box">
                            <h3><indaba:msg key='jsp.peopleProfile.openCases' /><a href="#" class="toggleVisible"><img src='images/expand_icon.png' alt='expand' /></a></h3>
                            <div class="content" style="display: none;">
                                <table width="100%" cellspacing="0" cellpadding="0" border="0">
                                    <thead>
                                        <tr class="thead">
                                            <th><indaba:msg key='common.label.case' /> #</th>
                                            <th><indaba:msg key='common.label.title' /></th>
                                            <th><indaba:msg key='common.label.status' /></th>
                                            <th><indaba:msg key='common.label.priority' /></th>
                                            <th><indaba:msg key='common.label.tags' /></th>
                                            <th><indaba:msg key='common.label.owner' /></th>
                                            <th><indaba:msg key='common.label.attachedcontent' /></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${openCases}" var="case" varStatus="status1">
                                            <tr>
                                                <td><a href="casedetail.do?caseid=${case.caseId}">#${case.caseId}</a></td>
                                                <td>${case.title}</td>
                                                <td>
                                                    <c:if test="${case.status == 'Open'}"><indaba:msg key='common.label.open' /></c:if>
                                                    <c:if test="${case.status == 'Closed'}"><indaba:msg key='common.label.closed' /></c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${case.priority == 'Low'}"><indaba:msg key='jsp.queues.low' /></c:if>
                                                    <c:if test="${case.priority == 'Medium'}"><indaba:msg key='jsp.queues.medium' /></c:if>
                                                    <c:if test="${case.priority == 'High'}"><indaba:msg key='jsp.queues.high' /></c:if>
                                                </td>
                                                <td>
                                                    <c:forEach items="${case.tags}" var="tag" varStatus="status">
                                                        ${tag.term}<c:if test="${!status.last}">, </c:if>
                                                    </c:forEach>
                                                </td>
                                                <td><a href="profile.do?targetUid=${case.assignedUserId}">${case.owner}</a></td>
                                                <td>
                                                    <c:forEach items="${case.attachContentTitles}" var="title" varStatus="status">
                                                        <a href="#">${title}</a><c:if test="${!status.last}">, </c:if>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:if>
                </indaba:view>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
