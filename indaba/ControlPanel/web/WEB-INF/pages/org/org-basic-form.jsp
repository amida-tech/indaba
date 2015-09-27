<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<form id="org-basic-form">
    <fieldset id="org-basic" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
        <legend><indaba:msg key="cp.label.base_information" /></legend>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.name" /></label>
            <div class="field">
                <input id="name" name="name" type="text" title="Organization Name" class="validate[required]" style="width:50%" /><span id="org-name-hint" style="margin-left: 10px;color: red">
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.address" /></label>
            <div class="field">
                <input id="address" name="address" type="text" title="Organization Address"  style="width:100%" />
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.web_site_url" /></label>
            <div class="field">
                <input id="url" name="url" type="text" title="Organization Web Site URL"  style="width:100%" />
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.enforce_api_security" /></label>
            <select id="enforce-api-security" title="Enforce API Security on this Organization" style="width:25%">
                <option value="true"><indaba:msg key="cp.label.yes" /></option>
                <option value="false"><indaba:msg key="cp.label.no" /></option>
            </select>
        </div>
    </fieldset>
    <div class="row"></div>
    <fieldset id="org-oa" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
        <legend><indaba:msg key="cp.label.primary_admin" /></legend>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.first_name" /></label>
            <div class="field">
                <input id="first-name" name="first-name" class="validate[required]" readonly="readonly" type="text" title="Organization Administrator's First Name"  style="width:30%" />
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.last_name" /></label>
            <div class="field">
                <input id="last-name" name="last-name" class="validate[required]" readonly="readonly" type="text" title="Organization Administrator's Last Name"  style="width:30%" />
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.email" /></label>
            <div class="field">
                <input id="email" name="email" class="validate[required,custom[email]]" readonly="readonly" type="text" title="Organization Administrator's Email Address"  style="width:50%" /><span id="user-hint" style="margin-left: 10px;color: red"></span>
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.user_name" /></label>
            <div class="field">
                <input id="user-name" name="user-name" class="validate[required]" readonly="readonly" type="text" title="Organization Administrator's User Name"  style="width:30%" /><span id="name-hint" style="margin-left: 10px;color: red"></span>
            </div>
        </div>
    </fieldset>
</form>
<script>
    $(function(){
        $('#org-basic-form').validationEngine();
        
        $('#name').focusout(function(){
        $.ajax({
            url: '${contextPath}/organizations!checkOrgName',
            data:{name: $('#name').val()},
            type: 'POST',
            dataType: 'json',
            success: function(r) {
                if(r['exist'] == true && r['id'] != ${orgid}) {
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
    
    function load_org_basic_info(contextPath, orgId) {
        $.ajax({
            url: '${contextPath}/organizations!getOrgInfo',
            type: 'POST',
            dataType: 'json',
            data:{id: orgId},
            success: function(data){
                $('#name').val(data['name']);
                $('#address').val(data['address']);
                $('#url').val(data['url']);
                if(data['enforceAPISecurity'] == false) {
                    $('#enforce-api-security').val('false');
                    $('#enforce-api-security').trigger('liszt:updated');
                }
                else {
                    $('#enforce-api-security').val('true');
                    $('#enforce-api-security').trigger('liszt:updated');
                }
                $('#first-name').val(data['firstName']);
                $('#last-name').val(data['lastName']);
                $('#email').val(data['email']);
                $('#user-name').val(data['userName']);
                $('#org-name-hint').text('');
                $('#name-hint').text('');
                $('#user-hint').text('');
            }
        });
    }
    function reset_org_basic_form()
    {
        $('#org-basic-form').each(function(){
            this.reset();
        });
        $('#org-basic-form').validationEngine('hide');
    }
    
    function save_org_basic_form(id) {
        if(!$('#org-basic-form').validationEngine('validate')){
            return false;
        }
    }
    function update_org_basic(contextPath, orgId) {
        if(!$('#org-basic-form').validationEngine('validate')){
            return false;
        }
        // hint for non-unique organization name
        if($('#org-name-hint').text() != '') {
            return false;
        }
        $.ajax({
            url: '${contextPath}/organizations!update',
            type: 'POST',
            dataType: 'json',
            data: {id: orgId,
                name: $('#name').val(), 
                address: $('#address').val(),
                url: $('#url').val(),
                enforceAPISecurity: $('#enforce-api-security').val()
            },
            success: function(r){
                ocsSuccess("Organization infromation saved!");
            }
        });        
    }
</script>
