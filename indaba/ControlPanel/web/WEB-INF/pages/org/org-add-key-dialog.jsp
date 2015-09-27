<div id="add-key-popup" class="hidden">
    <div id style="clear: both; width: 550px; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <jsp:include page="org-key-form.jsp" flush="true" />
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('#add-key-popup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            open: function(){
                init_org_key_form();
            },
            close: function(){
            },
            buttons: {
                "Add": function() {
                    add_org_key('${orgid}', function(){ $('#key-grid').flexReload({dataType:'json'});}, function(){});
                    $(this).dialog("close");
                    reset_org_key_form();
                },
                "Cancel": function(){
                    $(this).dialog("close");
                    reset_org_key_form();
                }
            }
        });
    });
    
</script>