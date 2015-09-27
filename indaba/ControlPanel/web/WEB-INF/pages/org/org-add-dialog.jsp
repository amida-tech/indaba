<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="org-add-popup" title="<indaba:msg key='cp.label.add_organization' />" class="hidden">
    <div id="add-org"style="clear: both; width: 550px; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <form id="add-org-form" action="add-org" method="POST">
            <fieldset id="org-basic" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
                <legend><indaba:msg key='cp.label.base_information' /></legend>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.name' /></label>
                    <div class="field">
                        <input id="name" name="name" type="text" maxlength="255" title="Organization Name" class="validate[required]" style="width:50%" /><span id="org-name-hint" style="margin-left: 10px;color: red"></span>
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.address' /></label>
                    <div class="field">
                        <input id="address" name="address" type="text" title="Organization Address"  style="width:100%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.web_site_url' /></label>
                    <div class="field">
                        <input id="url" name="url" type="text" maxlength="255" title="Organization Web Site URL"  style="width:100%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.enforce_api_security' /></label>
                    <select id="enforce-api-security" title="Enforce API Security on this Organization">
                        <option value="true"><indaba:msg key='cp.label.yes' /></option>
                        <option value="false"><indaba:msg key='cp.label.no' /></option>
                    </select>
                </div>
            </fieldset>
            <div class="row"></div>
            <fieldset id="org-oa" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
                <legend><indaba:msg key='cp.label.assign_admin' /></legend>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.email' /></label>
                    <div class="field">
                        <input id="email" name="email" maxlength="255" class="validate[required,custom[email]]" type="text" title="Organization Administrator's Email Address"  style="width:50%" /><span id="user-hint" style="margin-left: 10px;color: green"></span>
                    </div>
                </div>
                
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.first_name' /></label>
                    <div class="field">
                        <input id="first-name" name="first-name" maxlength="45" class="validate[required]" type="text" title="Organization Administrator's First Name"  style="width:30%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.last_name' /></label>
                    <div class="field">
                        <input id="last-name" name="last-name" maxlength="45" class="validate[required]" type="text" title="Organization Administrator's Last Name"  style="width:30%" />
                    </div>
                </div>
                
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.user_name' /></label>
                    <div class="field">
                        <input id="user-name" name="user-name" maxlength="100" class="validate[required]" type="text" title="Organization Administrator's User Name"  style="width:30%" /><span id="user-name-hint" style="margin-left: 10px;color: red"></span>
                    </div>
                </div>
                <div class="row-high">
                    <label style="width:120px"><indaba:msg key='cp.label.note' /></label>
                    <div class="field">
                        <textarea id="note" name="note" title="Note Sent to this Organization Administrator" style="width: 100%;"></textarea>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('input').uniform();
        $('select').uniform();
        $('textarea').uniform();
        $('#add-org-form').validationEngine();
        $('#org-add-popup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            open: function(){

            },
            close: function(){
                reset_org_form();
            },
            buttons: {
                "Save": function(){
                    if(!$('#add-org-form').validationEngine('validate')){
                        return false;
                    }
                    // hint for non-unique organization name
                    if($('#org-name-hint').text() != '') {
                        return false;
                    }
                    $.ajax({
                        url: '${contextPath}/organizations!create',
                        type: 'POST',
                        dataType: 'json',
                        data: {name: $('#name').val(), 
                            address: $('#address').val(),
                            url: $('#url').val(),
                            enforceAPISecurity: $('#enforce-api-security').val(),
                            firstName: $('#first-name').val(), 
                            lastName: $('#last-name').val(), 
                            email: $('#email').val(), 
                            userName: $('#user-name').val(), 
                            note: $('#note').val()},
                        success: function(r) {
                             $("#orgs-grid").flexReload({
                                    dataType: 'json'
                                });
                        }
                    });
                    $(this).dialog("close");
                    reset_org_form();
                    return false;
                },
                "Cancel": function(){
                    $(this).dialog("close");
                    reset_org_form();
                }
            }
        });
        
        $('#email').change(function() { 
        if($('#email').val() == '') {
            return;
        }
        if(false == isValidEmailAddress($('#email').val())) {
            return;
        }
        $.ajax({
            url: '${contextPath}/organizations!generateUserName',
            data: {email: $('#email').val()},
            type: 'POST',
            dataType: 'json',
            success: function(r){
                if(r['newuser']) {
                    $('#user-hint').text('New User');
                    $('#user-name').removeAttr('readonly');
                    $('#first-name').removeAttr('readonly');
                    $('#last-name').removeAttr('readonly');
                }
                else {
                    $('#user-hint').text('Existing User');
                    $('#user-name').attr('readonly', 'readonly');
                    $('#first-name').val(r['firstname']);
                    $('#first-name').attr('readonly', 'readonly');
                    $('#last-name').val(r['lastname']);
                    $('#last-name').attr('readonly', 'readonly');
                }
                $('#user-name').val(r['username']);
                $('#user-name-hint').text('');
            }
        });
        });
        
        $('#user-name').change(function(){
        if($('#user-name').val() == '' || $('#email').val() =='') {
            return;
        }
        if(false == isValidEmailAddress($('#email').val())) {
            return;
        }
        $.ajax({
            url: '${contextPath}/organizations!checkAndSuggestUserName',
            data:{userName: $('#user-name').val(), email: $('#email').val()},
            type: 'POST',
            dataType: 'json',
            success: function(r) {
                if(r['equal'] == 'false') {
                    var msg = 'user name ' + $('#user-name').val() + ' exists';
                    $('#user-name-hint').text(msg);
                }
                $('#user-name').val(r['username']);
            }
        });
        });
        
        $('#name').change(function(){
        if($('#name').val() =='') {
            return;
        }
        $.ajax({
            url: '${contextPath}/organizations!checkOrgName',
            data:{name: $('#name').val()},
            type: 'POST',
            dataType: 'json',
            success: function(r) {
                if(r['exist'] == true) {
                    var msg = $('#name').val() + ' exists';
                    $('#org-name-hint').text(msg);
                }
                else {
                    $('#org-name-hint').text('');
                }
            }
        });
        });
        
    });
    
    function reset_org_form(){
        $('#name').val('');
        $('#address').val('');
        $('#url').val('');
        //$('#enforce-api-security').val(''),
        $('#first-name').val('');
        $('#last-name').val('');
        $('#email').val('');
        $('#user-name').removeAttr('readonly');
        $('#first-name').removeAttr('readonly');
        $('#last-name').removeAttr('readonly');
        $('#user-name').val(''); 
        $('#note').val('');
        $('#add-org-form').validationEngine('hide');
        $('#user-hint').text('');
        $('#user-name-hint').text('');
        $('#org-name-hint').text('');
        }
        
        function isValidEmailAddress(email) {
            var pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
            return pattern.test(email);
        }
</script>
