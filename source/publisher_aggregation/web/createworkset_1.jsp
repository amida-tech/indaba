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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Creating Working Set Step #1</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script src="js/jquery.alerts.js" type="text/javascript"></script>
        <link href="css/jquery.alerts.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" charset="utf-8">
            var selectedProjects = [];
        
            $(function(){
                //$("table").tablesorter();
                //$('input, textarea, select, button').uniform();
                $('#organization').change(function(){
                    getProjects();
                    // make all project options disappeared firstly
                    /*$('#projects>option').each(function(){
                        $(this).css("display", "none");
                    });
                    
                    var checkedValues = $.makeArray($("#organization").val());
                    // make those project options diaplay which belongs the checked organization
                    for(var i = 0; i < checkedValues.length; ++i) {
                        $('#projects>option.belong-org-' + checkedValues[i]).each(function(){
                            // it is not included in selected project list
                            if($.inArray($(this).val(), selectedProjects) == -1) {
                                $(this).css("display", "block");
                            }
                        });
                    }*/
                    //var checkIndex=$("#organization ").get(0).selectedIndex;
                    //var maxIndex=$("#organization option:last").attr("index");
                    /*alert('ÊÇ®ÈÄâÊã©ÁöÑTextÊòØÔºö '+checkText+"\r\n"+
                        "ÊÇ®ÈÄâÊã©ÁöÑValueÊòØÔºö "+checkValue+"\r\nÊÇ®ÈÄâÊã©ÁöÑÁ¥¢ÂºïÂÄºÊòØÔºö "+
                        checkIndex+"\r\nÊúÄÂ§ßÁ¥¢ÂºïÂÄºÊòØÔºö "+maxIndex);
                     */
                });
                $('#worksetName').keyup(function(){
                    if(!$(this).val().empty()) {
                        $('#worksetName-warn').css("display", "none");
                    }
                });
                $('#projects').change(function(){
                    $('#proj-warn').css("display", "none");
                });
                
            });

            function getProjects(){
                var orgId = $("#organization").find("option:selected").val();
                var visibility = $("input[name='visibility']:checked").val();
                var data = "orgId=" + orgId + "&visibility=" + visibility;
                $.ajax({
                    type: "POST",
                    url: "getprojects.do",
                    data: data,
                    cache: false,
                    async: false,
                    success: function(result) {
                        $('#projects').empty();
                        $('#preview-list').empty();
                        $('#preview').css("display", "none");
                        selectedProjects = [];
                        if(result != ""){
                            $('#projects').append(result);                           
                        }
                        $("#projects").trigger("liszt:updated");
                    }
                });
                return false;
            }

            function doAddSelectedProjectToPreview() {
                var selectedValues = $.makeArray($('#projects').val());
                if(selectedValues.length == 0) {
                    $('#proj-warn').css("display", "block");
                    return false;
                }
                selectedProjects = selectedProjects.concat(selectedValues);
                for(var i = 0; i < selectedValues.length; ++i) {
                    var projObj = $('#proj-' + selectedValues[i]);
                    if(projObj.css("display") == "none") {
                        continue;
                    }
                    var projName = $.trim(projObj.text());
                    var item = "<li id='preview-proj-" + selectedValues[i] + "'><span class='preview-name'> (<a href='#' onclick='return doRemoveProjectFromPreview(\"" + selectedValues[i] + "\",\"" + projName + "\");'><img src='images/delete.png' height='12px' /></a>) " + projName + "</span></li>";
                    // hide the project item firstly
                    //projObj .css("display", "none");
                    projObj.remove();
                    // add into preview list
                    $('#preview-list').append(item);
                }
                $('#projects').val([]);
                $('#preview').css("display", "block");
                return false;
            }
            
            function doRemoveProjectFromPreview(projId, projectName) {
                var projOption = "<option id='proj-" + projId + "' value='" + projId + "'>" + projectName + "</option>";
                $('#projects').append(projOption);
                $("#projects").trigger("liszt:updated");

                //$('#proj-' + projId).css("display", "block");

                $('#preview-proj-' + projId).remove();
                if($('#preview-list').children().length == 0) {
                    $('#preview').css("display", "none");
                }
                
                selectedProjects.remove(projId);
                return false;
            }

            function allAddProjects(){
                selectedProjects = [];
                $('#projects>option').each(function(){
                    selectedProjects.push(this.value);
                    var projObj = $('#proj-' + this.value);
                    if(projObj.css("display") == "none") {
                        return;
                    }
                    var projName = $.trim(projObj.text());
                    var item = "<li id='preview-proj-" + this.value + "'><span class='preview-name'> (<a href='#' onclick='return doRemoveProjectFromPreview(\"" + this.value + "\",\"" + projName + "\");'><img src='images/delete.png' height='12px' /></a>) " + projName + "</span></li>";
                    // hide the project item firstly
                    //projObj.css("display", "none");
                    projObj.remove();
                    // add into preview list
                    $('#preview-list').append(item);
                });
                $('#preview').css("display", "block");
                return false;
            }

            function doSubmit() {
                if($('#worksetName').val().empty()) {
                    $('#worksetName-warn').css("display", "block");
                    return false;
                }
                $('#projectlist').val(selectedProjects);
            }

            function quitWorkset(){
                jConfirm("Your work has not been saved. Are you sure to quit", "Confirm Quit Workset", function(r) {
                    if (r){
                        var toUrl = "quitworkset.do";
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
            <c:set var="active" value="createworkset" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create a Working Set &nbsp;&nbsp;&nbsp;&nbsp;<small>- <a href="#" onclick="quitWorkset();">Quit this Working Set</a></small></h2>
                    <ul id="steps-bar" class="seg-4">
                        <li class="current">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Select name and projects</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Select targets</dd>
                            </dl>
                        </li>
                        <li class="last">
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Save Working Set</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <form id="chooseWorkSet" action="createworkset.do?step=2" method="post" >
                        <fieldset>
                            <legend>Select name and projects</legend>
                            <table>
                                <tr>
                                    <td>
                                        <ul>
                                            <li>
                                                <label for="worksetName">Working Set name:</label>
                                                <input id="worksetName" name="worksetName" type="text" size="43" <c:if test="${showOrigin}">value="${sessionScope.workset.name}"</c:if>/>
                                                <br/>
                                                <span id="worksetName-warn" class="warn">Must specify working set name!</span>
                                            </li>
                                            <li>
                                                <label for="worksetNotes">Working Set notes:</label>
                                                <textarea id="worksetNotes" name="worksetNotes" type="text" cols="41" rows="4" ><c:if test="${showOrigin}">${sessionScope.workset.notes}</c:if></textarea>
                                                <br/>
                                            </li>
                                            <li>
                                                <label for="visibility">Visibility:</label>
                                                <input id="vPublic" name="visibility" type="radio" value="1"  <c:if test="${visibility == 1}">checked</c:if> onclick="getProjects();"/> Public
                                                <input id="vPrivate" name="visibility" type="radio" value="2" <c:if test="${visibility == 2}">checked</c:if> onclick="getProjects();"/> Private
                                                <br/>
                                            </li>
                                            <li>
                                                <label for="organization">Select organization:</label>
                                                <select class="middlebox" id="organization" name="orgid" style="width: 290px;" >
                                                    <c:forEach items="${organizations}" var="org"  varStatus="status">
                                                        <option value="${org.id}" <c:if test="${org.checked}">selected</c:if>>${org.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </li>
                                            <li>
                                                <label for="projects">Select projects:</label>
                                                <select class="middlebox" id="projects" name="projects" multiple="true" size="5" style="width: 290px; height: 80px; " >
                                                    <c:forEach items="${wsProjects}" var="proj"  varStatus="projStatus">
                                                        <option id="proj-${proj.projectId}" value="${proj.projectId}"
                                                            <c:if test="${proj.checked}">selected</c:if>>
                                                        ${proj.projcetName}</option>
                                                    </c:forEach>
                                                </select>
                                                <div style="height: 5px; margin: 5px;" />
                                                <span id="proj-warn" class="warn" >You haven't select any projects!</span>
                                            </li>
                                            <li>
                                                <input type="submit" value="Add to selection preview" onclick="return doAddSelectedProjectToPreview();" />
                                                <input type="submit" value="Add All" onclick="return allAddProjects();" />
                                            </li>
                                        </ul>
                                    </td>
                                    <td id="preview" align="left" valign="top" style="display: none;" >
                                        <div class="preview">Your Selection Preview:</div>
                                        <div class="line"></div>
                                        <div>
                                            <ul id="preview-list" >
                                            </ul>
                                        </div>
                                        <input id="projectlist" name="projectlist" value="" type="hidden"/>
                                        <div id="submitButtons" class="submitButtons">
                                            <input type="submit" value="NEXT STEP" onclick="return doSubmit();" />
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
