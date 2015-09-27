<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="edit-key-popup" class="hidden">
    <div id style="clear: both; width: 200px; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <form id="org-edit-key-form" onsubmit="return change_valid_days();">
            <div class="row">
                <label style="width:120px"><indaba:msg key="cp.label.valid_days" /></label>
                <div class="field" style="width:50px">
                    <span id="key-id" style="display:none" ></span>
                    <input id="edit-valid-days" name="edit-valid-days" class="validate[required,custom[integer],min[1]]" type="text" title="Valid Days" style="width:100%" />                    
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        
        $('#org-edit-key-form').validationEngine();
        
        $('#edit-key-popup').dialog({
            autoOpen: false,
            width: 312,
            resizable: false,
            modal: true,
            open: function(){
            },
            close: function(){
                $('#edit-valid-days').val('');
            },
            buttons: {}
        });
    });
    function change_valid_days() {
        if(!$('#org-edit-key-form').validationEngine('validate')){
            return false;
        }
        
        $.ajax({
                    url: '${contextPath}/organizations!changeValidDays',
                    type: 'POST',
                    dataType: 'json',
                    data: {id: $('#key-id').text(), validDays: $('#edit-valid-days').val()},
                    success: function(r){$('#edit-key-popup').dialog('close'); $('#key-grid').flexReload({dataType:'json'}); }
                });
                
        return false;
    }
    function doEditKey(contextPath, keyId) {
            $('#key-id').text(keyId);
            $('#edit-key-popup').dialog('option', 'title', 'Edit Security Key');
            $('#edit-key-popup').dialog({buttons:[{
                text: 'Save',
                click: function() {change_valid_days();}
            },{
                text: 'Cancel',
                click: function(){$(this).dialog('close');}
            }]});
            $.ajax({
                    url: '${contextPath}/organizations!getCurrentValidDays',
                    type: 'POST',
                    dataType: 'json',
                    data: {id: keyId},
                    success: function(r){$('#edit-valid-days').val(r['valid-days']);}
                });
            $('#edit-key-popup').dialog('open');
            return false; 
    }
</script>