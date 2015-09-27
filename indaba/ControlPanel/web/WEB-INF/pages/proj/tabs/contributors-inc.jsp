<%--
    Document   : Project Tab - 'Contributors'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="contributorsTab">
    <div class="filter">
        <form id="contributor-filter-form" action="${contextPath}/lib/project-membership!find" method="POST">
            <fieldset>
                <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.contributor_filter"  /></span></legend>
                <div id="filterRole" class="field" style="margin-top: 0px;">
                    <label><indaba:msg key="cp.label.role"  /></label>
                    <select name="filterRoleId" id="filterRoleId" style="width: 200px; height: 15px;"class="pretty dk" onchange="loadContributorsFlexGrid();" >
                    </select>
                </div>
            </fieldset>
        </form>
    </div>
    <div id="contributorsFlexGrid" style="margin: 10px">
        <div id="contributors-flexgrid"></div>
    </div>
    <div id="contributorsFormPopup" title="<indaba:msg key='cp.title.add_contributor' />" class="hidden">
        <div id="contributorsFormBox" class="popup-container">
            <form id="contributorsForm" action="#" method="POST" onsubmit="return false;">
                <fieldset>
                    <input type="hidden" name="mode" id="mode" value="ADD"/>
                    <input type="hidden" name="pmId" id="pmId" value="-1"/>
                    <dl>
                        <dt><label for="email"><indaba:msg key="cp.label.email" /></label></dt>
                        <dd><input type="text" name="email" maxlength="255" id="email" class="short-input validate[required, custom[email]]"/><span id="userHint" style="margin-left: 10px;color: green"></span></dd>
                    </dl>
                    <dl>
                        <dt><label for="firstName"><indaba:msg key="cp.label.first_name" /></label></dt>
                        <dd><input type="text" name="firstName" maxlength="45" id="firstName" class="short-input validate[required]"/></dd>
                    </dl>
                    <dl>
                        <dt><label for="lastName"><indaba:msg key="cp.label.last_name" /></label></dt>
                        <dd><input type="text" name="lastName" id="lastName" maxlength="45" class="short-input validate[required]"/></dd>
                    </dl>
                    <dl>
                        <dt><label for="userName"><indaba:msg key="cp.label.user_name" /></label></dt>
                        <dd><input type="text" name="userName" maxlength="100" id="userName" class="short-input validate[required]"/><span id="userNameHint" style="margin-left: 10px;color: red"></span></dd>
                    </dl>
                    <dl>
                        <dt><label for="role"><indaba:msg key="cp.label.role" /></label></dt>
                        <dd>
                            <select name="role" id="role" data-placeholder="<indaba:msg key='cp.text.choose_role'  />" class="short-input validate[required]">
                            </select>
                        </dd>
                    </dl>
                    <dl id="note-dl">
                        <dt><label for="note"><indaba:msg key="cp.label.note" /></label></dt>
                        <dd>
                            <textarea id="note" name="note" class="long-input"></textarea>
                        </dd>
                    </dl>
                </fieldset>
            </form>
        </div>
    </div>
    <div id="removeContributorsFormPopup" title="<indaba:msg key='cp.title.remove_contributor' />" class="hidden">
        <div id="removeContributorsFormBox" class="popup-container">
            <form id="removeContributorsForm" action="#" method="POST" onsubmit="return false;">
                <fieldset>
                    <dl>
                        <dt></dt>
                        <dd>
                        <input type="checkbox" id="sendNotify" name="sendNotify" value="true"/><indaba:msg key='cp.title.send_notify_to_contributor' /><br/>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="removeNote"><indaba:msg key="cp.label.note" /></label></dt>
                        <dd>
                            <textarea id="removeNote" name="removeNote" class="long-input"></textarea>
                        </dd>
                    </dl>
                </fieldset>
            </form>
        </div>
    </div>
    <jsp:include page="contributor-import-inc.jsp" flush="true" />
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        /*
        $('#resetFilter').click(function(){
            selectChosenOption($('select#filterRoleId'), 0);
            return false;
        });
        */
        //////////////////////////////////////////////////////////////////////////
        //
        // 'Contributors' Flexgrid
        //
        //////////////////////////////////////////////////////////////////////////
        var contributorsForm = $('#contributorsForm');
        //$('input, textarea', contributorsForm).uniform();
        //$("select",contributorsForm).chosen();
        //$('a.ui-btn, button').button();
        //contributorsForm.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
        contributorsForm.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
        // Dialog
        $('#contributorsFormPopup').dialog({
            autoOpen: false,
            width: 622,
            resizable: false,
            minHeight: 370,
            modal: true,
            open: function(){
                  
            },
            beforeClose: function(){
                resetProjectContributorForm();
            },
            buttons: {
                "Save": function() {
                    var mode = $('#mode');
                    if (mode.val() == 'ADD') {
                        addProjectContributor();
                    }
                    else if (mode.val() == 'EDIT') {
                        updateProjectContributor();
                    }
                },
                "Cancel": function() {
                    $('#contributorsFormPopup').dialog('close');
                }
            }
        });
        
        $("#contributors-flexgrid").flexigrid({
            url: '${contextPath}/proj/project-membership!find?projId=' + ${projId},
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'id', width : 0, sortable : false, hide: true},
                {display: 'USER ID', name : 'userId', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.user"/>', name : 'displayUserName', width : 200, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.status"/>', name : 'status', width : 70, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.first_name"/>', name : 'firstName', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.last_name"/>', name : 'lastName', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.user_name"/>', name : 'userName', width : 0, sortable : false, hide: true},
                {display: 'ROLE ID', name : 'roleId', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.role"  />', name : 'roleName', width : 190, sortable : false, align: 'left'},
                {display: '<indaba:msg key="cp.ch.email"  />', name : 'email', width : 240, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.actions"  />', name : 'actions', width : 100, sortable : false, align: 'left'}
            ],
            preProcess: function(data) {
                $.each(data.rows, function(index, elem){
                    var pmId = elem.id;
                    var userId = elem.userId;
                    var roleId = elem.roleId;
                    var email = elem.email;
                    var userName = elem.userName;
                    var firstName = elem.firstName;
                    var lastName = elem.lastName;
                    var actions = '<a class="link" href="javascript:void(0)" onclick="return doEditProjectContributor(\''+ email + '\',\'' + firstName + '\',\'' + lastName + '\',\'' + userName + '\',\'' + roleId + '\',\'' + pmId+'\');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"  /></a>'
                        + '<a class="link" href="" onclick="return doDeleteProjectMembership(\'${contextPath}\','+ pmId +');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"  /></a>';
                    elem.actions = actions;

                    if (elem.status == 1) {
                        // active
                        elem.status = '<img height="14px" width="14px" src="${contextPath}/resources/images/task3.gif" title="' + $.i18n.message('cp.text.active_user') + '">';
                    } else {
                        elem.status = '<img height="14px" width="14px" src="${contextPath}/resources/images/task1.gif" title="' + $.i18n.message('cp.text.inactive_user') + '">';
                    }
                });
                return data;
            },
            buttons : [
                {name: '<indaba:msg key="cp.btn.add"  />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doAddProjectContributor();
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.import"  />', bclass: 'import', bimage: '${contextPath}/resources/images/import.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doImportContributor();
                        return false;
                    }
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "USERNAME",
            sortorder: "asc",
            usepager: true,
            title: '<indaba:msg key="cp.title.project_contributors"  />',
            useRp: true,
            rp: 50,
            rpOptions: [30, 50, 100, 200, 500],
            showToggleBtn: false,
            autoload: false,
            width: 865,
            height: '100%',
            //page: 1,
            pagestat: 'Displaying {from} to {to} of {total} items',
            searchitems: [
                {display: '<indaba:msg key="cp.ch.user"/>', name: 'username'},
                {display: '<indaba:msg key="cp.ch.first_name"/>', name: 'firstname'},
                {display: '<indaba:msg key="cp.ch.last_name"/>', name: 'lastname'},
                {display: '<indaba:msg key="cp.ch.email"/>', name: 'email'}],
            doSearch: function() {
            },
            onError: function(){
                fixDHTMLXTabbarHeight("content");
            },
            onNoData: function(){
                fixDHTMLXTabbarHeight("content");
            },
            onSuccess: function(){
                fixDHTMLXTabbarHeight("content");
            },
            onSubmit: function(){
                var p = [];
                $('select, input', $('#contributor-filter-form')).each(function(){
                    p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                });
                this.params = p;
                return true;
            }
        });
        $('#removeContributorsFormPopup').dialog({
            autoOpen: false,
            width: 622,
            resizable: false,
            modal: true,
            open: function(){
                  
            },
            beforeClose: function(){
                resetForm($('#removeContributorsForm'));
            },
            buttons: {
                "OK": function() {
                    removeProjectMembership();
                },
                "Cancel": function() {
                    $('#removeContributorsFormPopup').dialog('close');
                }
            }
        });
        // since validationEngine ajax doesn't fit in this situation (validate and bring more information),
        // write specific one here
        $('#email').unbind('change');
        $('#email').change(function(){
            var email = $('#email').val();
            if(email == '') {
                return;
            }           
            email = email.replace(/^\s+|\s+$/g,'');
            if(false == isValidEmailAddress(email)) {
                return;
            }            
            $.ajax({
                url: '${contextPath}/proj/project-membership!userExists',
                data: {projId: ${projId}, email: email},
                type: 'POST',
                dataType: 'json',
                success: function(data, textStatus, jqXHR){
                    if (data['ret'] != 0) {
                        ocsError(data.desc);
                    }
                    else {
                        $('#userName').val(data['username']);
                        $('#userNameHint').text('');
                        if(data['newuser']) {
                            $('#userHint').text('New User');
                            $('#userName').removeAttr('readonly');
                            $('#firstName').removeAttr('readonly');
                            $('#lastName').removeAttr('readonly');
                        }
                        else {
                            $('#userHint').text('Existing User');
                            $('#userName').attr('readonly', 'readonly');
                            $('#firstName').val(data['firstname']);
                            $('#firstName').attr('readonly', 'readonly');
                            $('#lastName').val(data['lastname']);
                            $('#lastName').attr('readonly', 'readonly');
                        }
                    }
                }
            });
        });
        $('#userName').unbind('change');
        $('#userName').change(function(){
            if($('#userName').val() == '' || $('#email').val() =='') {
                return;
            }            
            $.ajax({
                url: '${contextPath}/proj/user!userNameExists',
                data:{userName: $('#userName').val(), email: $('#email').val()},
                type: 'POST',
                dataType: 'json',
                success: function(data, textStatus, jqXHR) {
                    if(data['newuser']) {
                        if (data['nameused']) {
                            var msg = 'username ' + data['username'] + ' is taken';
                            $('#userNameHint').text(msg);
                            $('#userName').val(data['suggest']);
                        } else {
                            $('#userNameHint').text("");
                        }
                    }                
                }
            });
        });
    });
    function addProjectContributor() {
        if(!$('#contributorsForm').validationEngine('validate')){
            return false;
        }
        $.ajax({
            url: '${contextPath}/proj/project-membership!create',
            data: {projId: ${projId}, 
                email: $('#email').val(),
                firstName: $('#firstName').val(),
                lastName: $('#lastName').val(),
                userName: $('#userName').val(),
                roleId: $('#role').val(),
                note: $('#note').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                if (data['ret'] == 0) {
                    $('#contributorsFormPopup').dialog('close');
                    loadContributorsFlexGrid();
                }
                else {
                    ocsError(data['desc']);
                }
            }
        });
    }
    function updateProjectContributor()
    {
        $.ajax({
            url: '${contextPath}/proj/project-membership!updateRole',
            data: {pmId: $('#pmId').val(), 
                roleId: $('#role').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                if (data['ret'] == 0) {
                    $('#contributorsFormPopup').dialog('close');
                    loadContributorsFlexGrid();
                }
                else {
                    ocsError(data['desc']);
                }
            }
        });
    }
    function resetProjectContributorForm() {
        resetForm($('#contributorsForm'));
        $('#contributorsForm').validationEngine('hideAll');
        $('#userHint').text('');
        $('#userNameHint').text('');
    }
    function doAddProjectContributor() {
        $('#contributorsForm input').removeAttr('disabled');
        $('#mode').val('ADD');
        $('#contributorsFormPopup').dialog('option', 'title', "<indaba:msg key='cp.title.add_contributor' />");
        $('#note-dl').show();
        $('#contributorsFormPopup').dialog('open');
        return false;
    }
    function doEditProjectContributor(email, firstName, lastName, userName, roleId, pmId) {
        $('#email').val(email);
        $('#firstName').val(firstName);
        $('#lastName').val(lastName);
        $('#userName').val(userName);
        selectChosenOptions($('#role'), roleId);
        $('#mode').val('EDIT');
        $('#pmId').val(pmId);
        $('#contributorsForm input').attr('disabled', 'disabled');
        $('#contributorsFormPopup').dialog('option', 'title', "<indaba:msg key='cp.title.edit_contributor' />");
        $('#note-dl').hide();
        $('#contributorsFormPopup').dialog('open');
        return false;
    }
    
    function doDeleteProjectMembership(contextPath, id) {
        $('#pmId').val(id);
        $.ajax({
            url: '${contextPath}/proj/project-membership!hasTask',
            data: {pmId: id},
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                if (data['ret'] != 0) {
                    ocsError(data.desc);
                }
                else {
                    $('#removeContributorsFormPopup').dialog('open');
                }
            }
        });
        return false;
    }
    
    function removeProjectMembership() {
        var checked = ('checked' == $('#sendNotify').attr('checked')) ? true : false;
        $.ajax({
            url: '${contextPath}/proj/project-membership!delete',
            data: {pmId: $('#pmId').val(), sendNotify: checked, note: $('#removeNote').val()},
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                if (data['ret'] != 0) {
                    ocsError(data.desc);
                }
                else {
                    $('#removeContributorsFormPopup').dialog('close');
                    loadContributorsFlexGrid();
                }
            }
        });
        return false;
    }
    
    function initContributorTab() {
        $.ajax({
            url: '${contextPath}/proj/project-membership!getProjectRoles',
            data: {projId: ${projId}},
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                setOptions($('#contributorsForm select#role'), data.roles);
                var filterRoles = [{id: 0, name: '<indaba:msg key="cp.text.all"  />'}];
                for(var i = 0; i < data.roles.length; ++i) {
                    filterRoles.push(data.roles[i]);
                }
                setOptions($('#contributor-filter-form select#filterRoleId'), filterRoles, null, null, false, true);
                selectChosenOption($('#contributor-filter-form select#filterRoleId'), 0);
                $('#contributor-filter-form').jqTransform();
                $('#contributor-filter-form select').jqTransSelect();
                loadContributorsFlexGrid();
            }
        });
    }

    function loadContributorsFlexGrid() {
        $("#contributors-flexgrid").flexReload({newp: 1,dataType: 'json'});
    }
</script>