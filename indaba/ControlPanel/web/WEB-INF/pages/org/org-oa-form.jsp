<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<form id="org-oa-form">
    <fieldset id="org-oa" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
        <legend style="font-weight: bold"><indaba:msg key="cp.label.administrator" /></legend>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.email" /></label>
            <div class="field">
                <input id="oa-email" name="email" class="validate[required,custom[email]]" type="text" title="Organization Administrator's Email Address"  style="width:50%" /><span id="oa-user-hint" style="margin-left: 10px;color: green"></span>
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.first_name" /></label>
            <div class="field">
                <input id="oa-first-name" name="first-name" class="validate[required]" type="text" title="Organization Administrator's First Name" style="width:30%" />
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.last_name" /></label>
            <div class="field">
                <input id="oa-last-name" name="last-name" class="validate[required]" type="text" title="Organization Administrator's Last Name" style="width:30%" />
            </div>
        </div>
        
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.user_name" /></label>
            <div class="field">
                <input id="oa-user-name" name="user-name" class="validate[required]" type="text" title="Organization Administrator's User Name"  style="width:30%" /><span id="oa-user-name-hint" style="margin-left: 10px;color: red"></span>
            </div>
        </div>
        <div class="row-high">
            <label style="width:120px"><indaba:msg key="cp.label.note" /></label>
            <div class="field">
                <textarea id="oa-note" name="note" title="Note Sent to this Organization Administrator" style="width: 100%;"></textarea>
            </div>
        </div>
    </fieldset>
</form>
<script>
    $(function(){
        $('#org-oa-form').validationEngine();
        
        $('#oa-email').unbind('change');
        $('#oa-email').change(function(){
            if($('#oa-email').val() == '') {
                return;
            }
            if(false == isValidEmailAddress($('#oa-email').val())) {
                return;
            }            
            $.ajax({
                url: '${contextPath}/organizations!generateUserName',
                data: {email: $('#oa-email').val()},
                type: 'POST',
                dataType: 'json',
                success: function(r){
                    $('#oa-user-name').val(r['username']);
                    $('#oa-user-name-hint').text('');
                    if(r['newuser']) {
                        $('#oa-user-hint').text('New User');
                        $('#oa-user-name').removeAttr('readonly');
                        $('#oa-first-name').removeAttr('readonly');
                        $('#oa-last-name').removeAttr('readonly');
                    }
                    else {
                        $('#oa-user-hint').text('Existing User');
                        $('#oa-user-name').attr('readonly', 'readonly');
                        $('#oa-first-name').val(r['firstname']);
                        $('#oa-first-name').attr('readonly', 'readonly');
                        $('#oa-last-name').val(r['lastname']);
                        $('#oa-last-name').attr('readonly', 'readonly');
                    }
                }
            });
        });
        
        $('#oa-user-name').unbind('change');
        $('#oa-user-name').change(function(){
        if($('#oa-user-name').val() == '' || $('#oa-email').val() =='') {
            return;
        }
        if(false == isValidEmailAddress($('#oa-email').val())) {
            return;
        }
        $.ajax({
            url: '${contextPath}/organizations!checkAndSuggestUserName',
            data:{userName: $('#oa-user-name').val(), email: $('#oa-email').val()},
            type: 'POST',
            dataType: 'json',
            success: function(r) {
                if(r['newuser']) {
                    if (r['nameused']) {
                        var msg = 'Username ' + r['username'] + ' is not available';
                        $('#oa-user-name-hint').text(msg);
                        $('#oa-user-name').val(r['suggest']);
                    } else {
                        $('#oa-user-name-hint').text("");
                    }
                }                
            }
        });
        });
        
    });
    
    function reset_org_oa_form() {
        $('#org-oa-form').each(function(){
            this.reset();
        });
        
        $('#oa-user-hint').text('');
        $('#oa-user-name-hint').text('');
 
        $('#org-oa-form').validationEngine('hide');    
    }
    
    function add_org_oa(orgId, success_callback, error_callback) {
        if(!$('#org-oa-form').validationEngine('validate')){
            return false;
        }
        $.ajax({
            url: '${contextPath}/organizations!addOA',
            type: 'POST',
            dataType: 'json',
            data: {id: orgId,
                firstName: $('#oa-first-name').val(), 
                lastName: $('#oa-last-name').val(), 
                email: $('#oa-email').val(), 
                userName: $('#oa-user-name').val(), 
                note: $('#oa-note').val()},
            success: function(){success_callback();},
            error: function(r) {error_callback();defaultAjaxErrorHanlde(r);}
        });
    }
    
    function save_org_oa(oaId) {
        if(!$('#org-oa-form').validationEngine('validate')){
            return false;
        }        
    }
    function remove_org_oa(oaId, success_callback, error_callback) {
        $.ajax({
            url: '${contextPath}/organizations!removeOA',
            type: 'POST',
            dataType: 'json',
            data: {id: oaId},
            success: function(){success_callback();},
            error: function(r) {error_callback();defaultAjaxErrorHanlde(r);}
        });
    }
    function isValidEmailAddress(email) {
            var pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
            return pattern.test(email);
     }
</script>
