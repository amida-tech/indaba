<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<form id="org-key-form">
    <fieldset id="org-key" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
        <legend style="font-weight: bold"><indaba:msg key="cp.label.security_key" /></legend>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.validation_algorithm" /></label>
            <select id="validation-algorithm" title="<indaba:msg key="cp.label.validation_algorithm" />" style="width:100%">
                <option value="sha1">SHA-1</option>
            </select>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.effective_date" /></label>
            <div class="field">
                <input id="effective-date" name="effective-date" type="text" title="Effective Date"  style="width:60%" />
            </div>
        </div>
        <div class="row">
            <label style="width:120px"><indaba:msg key="cp.label.valid_days" /></label>
            <div class="field">
                <input id="valid-days" name="valid-days" type="text" title="Valid Days" value="90"  style="width:20%" />
            </div>
        </div>
    </fieldset>
</form>
<script>
    $(function(){
        $('#org-key-form').validationEngine();
        $('#effective-date').datepicker();
    });
    
    function init_org_key_form()
    {
        $('#effective-date').datepicker('setDate', new Date());
        $('#valid-days').val('90');
    }
    function reset_org_key_form()
    {
        $('#org-key-form').each(function(){
            this.reset();
        });
        $('#org-key-form').validationEngine('hide');
    }
    
    function save_org_key_form(id) {
        if(!$('#org-key-form').validationEngine('validate')){
            return false;
        }
    }
    
    function add_org_key(orgId, success_callback, error_callback) {
        if(!$('#org-key-form').validationEngine('validate')){
            return false;
        }
        $.ajax({
            url: '${contextPath}/organizations!addKey',
            data: {id: orgId, hashAlgorithm: $('#validation-algorithm').val(), effectiveTime: $('#effective-date').val(),validDays: $('#valid-days').val()},
            type: 'POST',
            dataType: 'json',
            success: function(r) { success_callback();},
            error: function(r){ error_callback();defaultAjaxErrorHanlde(r);}
        });
    }
    
    function revoke_org_key(keyId, reason, success_callback, error_callback) {
        $.ajax({
            url: '${contextPath}/organizations!revokeKey',
            data: {id: keyId, revokeReason: reason},
            type: 'POST',
            dataType: 'json',
            success: function(r) { success_callback();},
            error: function(r){ error_callback();defaultAjaxErrorHanlde(r);}
        });
    }
</script>
