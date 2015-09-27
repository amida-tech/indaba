<div id="add-oa-popup" title="Add Administrator" class="hidden">
    <div id style="clear: both; width: 550px; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <jsp:include page="org-oa-form.jsp" flush="true" />
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('#add-oa-popup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            open: function(){

            },
            close: function(){
                reset_org_oa_form();
            },
            buttons: {
                "Add": function() {
                    add_org_oa('${param.orgid}', function(){$('#oa-grid').flexReload({dataType: 'json'});$('#add-oa-popup').dialog("close");reset_org_oa_form();}, function(){});
                },
                "Cancel": function(){
                    $(this).dialog("close");
                    reset_org_oa_form();
                }
            }
        });
    });
    
</script>