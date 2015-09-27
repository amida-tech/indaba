<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="revoke-key-popup" class="hidden">
    <div id style="clear: both; width: 550px; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <form onsubmit="return false;">
            <div class="row-high">
                <label style="width:120px">Revoke Reason</label>
                <div class="field">
                    <textarea id="reason" name="reason" title="Revoke Reason" style="width:100%"></textarea>
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('#revoke-key-popup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            open: function(){
            },
            close: function(){
            },
            buttons: {
                "Revoke": function() {
                    $(this).dialog("close");
                },
                "Cancel": function(){
                    $(this).dialog("close");
                }
            }
        });
    });
  
    function doRevokeKey(contextPath, keyId) {
            $('#revoke-key-popup').dialog('option', 'title', 'Revoke Security Key');
            $('#revoke-key-popup').dialog({buttons:[{
                text: 'Revoke',
                click: function(){
                    $.ajax({
                    url: '${contextPath}/organizations!revokeKey',
                    type: 'POST',
                    dataType: 'json',
                    data: {id: keyId, revokeReason: $('#reason').val()},
                    success: function(r){$('#key-grid').flexReload({dataType:'json'});$('#revoke-key-popup').dialog('close');}
                });}
            },{
                text: 'Cancel',
                click: function(){$(this).dialog('close'); $('#reason').val('');}
            }]});
            $('#revoke-key-popup').dialog('open');
            return false; 
    }
</script>