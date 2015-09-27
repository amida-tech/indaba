<%-- 
    Document   : contributor-dlg
    Created on : Oct 5, 2012, 6:41:02 PM
    Author     : Rick Qiu, qiudejun@gmail.com
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="contributor-dlg" class="hiden">
    <div id style="clear: both; width: 570px; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <form id="project-contributor-form">
            <fieldset id="project-contributor" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
                <legend style="font-weight: bold"><indaba:msg key="cp.label.contributor" /></legend>
                <div class="row">
                    <label style="width:120px"><indaba:msg key="cp.label.email" /></label>
                    <div class="field">
                        <input id="contributor-email" maxlength="255" name="contributor-email" class="validate[required,custom[email]]" type="text" title="Contributor's Email Address"  style="width:50%" /><span id="contributor-user-hint" style="margin-left: 10px;color: green"></span>
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key="cp.label.first_name" /></label>
                    <div class="field">
                        <input id="contributor-first-name" maxlength="45" name="contributor-first-name" class="validate[required]" type="text" title="Contributor's First Name" style="width:30%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key="cp.label.last_name" /></label>
                    <div class="field">
                        <input id="contributor-last-name" maxlength="45" name="contributor-last-name" class="validate[required]" type="text" title="Contributor's Last Name" style="width:30%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key="cp.label.user_name" /></label>
                    <div class="field">
                        <input id="contributor-user-name" maxlength="100" name="contributor-user-name" class="validate[required]" type="text" title="Contributor's User Name"  style="width:30%" /><span id="contributor-user-name-hint" style="margin-left: 10px;color: red"></span>
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key="cp.label.role" /></label>
                    <div class="field">
                        <select id="contributor-role" name="contributor-role" data-placeholder='<indaba:msg key="cp.text.dataplcaeholder.choose_role" />' class="validate[required]" title="Contributor's Role"  style="width:50%" >
                            <option value=""></option>
                            <c:forEach items="${roles}" var="role">
                                <option value="${role.id}">${role.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row-high" id="contributor-note-div">
                    <label style="width:120px"><indaba:msg key="cp.label.note" /></label>
                    <div class="field">
                        <textarea id="contributor-note" name="note" title="Note Sent to this Contributor" style="width: 100%;"></textarea>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('#contributor-dlg').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            open: function() {},
            close: function() {reset_project_contributor_form();},
            buttons: {
                '<indaba:msg key="cp.btn.save" />': function() {add_project_contributor();},
                '<indaba:msg key="cp.btn.cancel" />': function() {$('#contributor-dlg').dialog('close');}
            }
        });
        
        $('#project-contributor-form').validationEngine();
        
        $('#contributor-email').blur(function(){
            if($('#contributor-email').val() == '') {
                return;
            }
            if(false == isValidEmailAddress($('#contributor-email').val())) {
                return;
            }
            $.ajax({
                url: '${contextPath}/proj/project-membership!checkUser',
                data: {projId: ${projId}, email: $('#contributor-email').val()},
                type: 'POST',
                dataType: 'json',
                success: function(data, textStatus, jqXHR){
                    if (data['ret'] != 0) {
                        ocsError(data.desc);
                    }
                    else {
                        $('#contributor-user-name').val(data['username']);
                        $('#contributor-user-name-hint').text('');
                        if(data['newuser']) {
                            $('#contributor-user-hint').text('New User');
                            $('#contributor-user-name').removeAttr('readonly');
                            $('#contributor-first-name').removeAttr('readonly');
                            $('#contributor-last-name').removeAttr('readonly');
                        }
                        else {
                            $('#contributor-user-hint').text('Existing User');
                            $('#contributor-user-name').attr('readonly', 'readonly');
                            $('#contributor-first-name').val(data['firstname']);
                            $('#contributor-first-name').attr('readonly', 'readonly');
                            $('#contributor-last-name').val(data['lastname']);
                            $('#contributor-last-name').attr('readonly', 'readonly');
                        }
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {defaultAjaxErrorHanlde(jqXHR, textStatus, errorThrown);}
            });
        });
        
        $('#contributor-user-name').blur(function(){
            if($('#contributor-user-name').val() == '' || $('#contributor-email').val() =='') {
                return;
            }
            if(false == isValidEmailAddress($('#contributor-email').val())) {
                return;
            }
            $.ajax({
                url: '${contextPath}/proj/project-membership!checkUserName',
                data:{userName: $('#contributor-user-name').val(), email: $('#contributor-email').val()},
                type: 'POST',
                dataType: 'json',
                success: function(data, textStatus, jqXHR) {
                    if(data['newuser']) {
                        if (data['nameused']) {
                            var msg = 'Username ' + data['username'] + ' is not available';
                            $('#contributor-user-name-hint').text(msg);
                            $('#contributor-user-name').val(data['suggest']);
                        } else {
                            $('#contributor-user-name-hint').text("");
                        }
                    }                
                },
                error: function(jqXHR, textStatus, errorThrown) {defaultAjaxErrorHanlde(jqXHR, textStatus, errorThrown);}
            });
        });        
    });
    
    function add_project_contributor() {
        if(!$('#project-contributor-form').validationEngine('validate')){
            return false;
        }
        $.ajax({
            url: '${contextPath}/proj/project-membership!create',
            data: {projId: ${projId}, 
                email: $('#contributor-email').val(),
                firstName: $('#contributor-first-name').val(),
                lastName: $('#contributor-last-name').val(),
                userName: $('#contributor-user-name').val(),
                roleId: $('#contributor-role').val(),
                note: $('#contributor-note').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                if (data['ret'] == 0) {
                    $('#contributor-dlg').dialog('close');
                    $('#contributors-flexgrid').flexReload({dataType: 'json'});
                }
                else {
                    ocsError(data['desc']);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {defaultAjaxErrorHanlde(jqXHR, textStatus, errorThrown);}
        });
    }
    function reset_project_contributor_form() {
        resetForm($('#project-contributor-form'));
        $('#project-contributor-form').validationEngine('hide');
        $('#contributor-user-hint').text('');
        $('#contributor-user-name-hint').text('');
    }
    function doAddProjectContributor() {
        $('#project-contributor-form input').removeAttr('disabled');
        $('#contributor-note-div').removeAttr('style');
        $('#contributor-dlg').dialog('option', 'title', '<indaba:msg key="cp.title.add_project_contributor" />');
        $('#contributor-dlg').dialog('open');
    }
    function doEditProjectContributor(pmId) {
        $('#project-contributor-form input').attr('disabled', 'disabled');
        $('#contributor-note-div').attr('style', 'display:none;')
        $('#contributor-dlg').dialog('option', 'title', '<indaba:msg key="cp.title.edit_project_contributor" />');
        $('#contributor-dlg').dialog('open');
        return false;
    }
</script>
