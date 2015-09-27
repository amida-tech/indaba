<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="org-edit-popup" title="<indaba:msg key='cp.label.edit_organization' />" class="hidden">
    <div id="edit-org"style="clear: both; width: 550px; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <form id="org-edit-form" action="add-org" method="POST">
            <fieldset id="edit-org-basic" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
                <legend><indaba:msg key='cp.label.base_information' /></legend>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.name' /></label>
                    <div class="field">
                        <input id="name-edit" name="name" type="text" title="Organization Name" class="validate[required]" style="width:50%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.address' /></label>
                    <div class="field">
                        <input id="address-edit" name="address" type="text" title="Organization Address"  style="width:100%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.web_site_url' /></label>
                    <div class="field">
                        <input id="url-edit" name="url" type="text" title="Organization Web Site URL"  style="width:100%" />
                    </div>
                </div>
                <div class="row">
                    <label style="width:120px"><indaba:msg key='cp.label.enforce_api_security' /></label>
                    <select id="enforce-api-security-edit" title="Enforce API Security on this Organization">
                        <option value="true">Yes</option>
                        <option value="false">No</option>
                    </select>
                </div>
            </fieldset>
            <div style="margin: 10px 0px 10px 0px; float: right;">
                <a id="save-org-btn" href="javascript:void(0)">Save</a>
            </div>
        </form>
        <div class="row"></div>
        <form id="add-oa-form">
            <div>
                <table id="oas-tbl" border="1" style="border-collapse:collapse;border-color: #dfd8d8">
                    <caption style="text-align: left;background-color: #dfd8d8;font-weight: bold;font-size: 11px;color: black;line-height: 25px;padding-left: 8px"><indaba:msg key="cp.label.administrators" /></caption>
                    <colgroup>
                        <col style="width:155px;text-align: left" />
                        <col style="width:140px;text-align: left" />
                        <col style="width:155px;text-align: left" />
                        <col style="width:50px;text-align: left" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th style="text-align: left;font-weight: normal;color: black"><indaba:msg key="cp.title.name" /></th>
                    <th style="text-align: left;font-weight: normal;color: black"><indaba:msg key="cp.title.user_name" /></th>
                    <th style="text-align: left;font-weight: normal;color: black"><indaba:msg key="cp.title.email" /></th>
                    <th style="text-align: left;font-weight: normal;color: black"><indaba:msg key="cp.title.actions" /></th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                    <tfoot></tfoot>
                </table>
            </div>
            <div class="row">
                <div id="add-oa-btn" style="margin: 10px 0px 10px 0px; float: right;">
                    <a id="show-btn" href="javascript:void(0)"><indaba:msg key="cp.label.add_admin" /></a>
                </div>
            </div>
            <div id="add-oa-div" class="hidden">
                <fieldset id="org-oa" style="border-color:#E2DED6;border-width:1px;border-style:Solid;padding: 10px;">
                    <legend><indaba:msg key="cp.label.add_admin" /></legend>
                    <div class="row">
                        <label style="width:120px"><indaba:msg key="cp.label.first_name" /></label>
                        <div class="field">
                            <input id="first-name-edit" class="validate[required]" name="first-name" type="text" title="Organization Administrator's First Name"  style="width:30%" />
                        </div>
                    </div>
                    <div class="row">
                        <label style="width:120px"><indaba:msg key="cp.label.last_name" /></label>
                        <div class="field">
                            <input id="last-name-edit" class="validate[required]" name="last-name" type="text" title="Organization Administrator's Last Name"  style="width:30%" />
                        </div>
                    </div>
                    <div class="row">
                        <label style="width:120px"><indaba:msg key="cp.label.email" /></label>
                        <div class="field">
                            <input id="email-edit" class="validate[required,custom[email]]" name="email" type="text" title="Organization Administrator's Email Address"  style="width:50%" />
                        </div>
                    </div>
                    <div class="row">
                        <label style="width:120px"><indaba:msg key="cp.label.user_name" /></label>
                        <div class="field">
                            <input id="user-name-edit" class="validate[required]" name="user-name" type="text" title="Organization Administrator's User Name"  style="width:30%" />
                        </div>
                    </div>
                    <div class="row-high">
                        <label style="width:120px"><indaba:msg key="cp.label.note" /></label>
                        <div class="field">
                            <textarea id="note-edit" name="note" title="Note Sent to this Organization Administrator" style="width: 100%;"></textarea>
                        </div>
                    </div>
                    <div class="row" style="padding: 10px 0px 0px 0px;">
                        <div style="float:right">
                            <a id="add-org-btn" href="javascript:void(0)"><indaba:msg key="cp.btn.add" /></a>
                            &nbsp;&nbsp;
                            <a id="cancel-add-org-btn" href="javascript:void(0)"><indaba:msg key="cp.btn.cancel" /></a>
                            &nbsp;&nbsp;
                        </div>
                    </div>
                </fieldset>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('#org-edit-form').validationEngine();
        $('#add-oa-form').validationEngine();
        $('#org-edit-popup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            open: function(){

            },
            close: function(){

            },
            buttons: {
                "Close": function(){
                    $(this).dialog("close");
                }
            }
        });
        $('#save-org-btn').button();
        $('#show-btn').button();
        $('#add-org-btn').button();
        $('#cancel-add-org-btn').button();
        $('#show-btn').click(function(){
            $('#show-btn').hide();
            $('#add-oa-div').show();
        });
        $('#cancel-add-org-btn').click(function(){
            $('#add-oa-div').hide();
            $('#show-btn').show();
        });
    });
    function initEditBox(data) {
        $('#name-edit').val(data['name']);
        $('#address-edit').val(data['address']);
        $('#url-edit').val(data['url']);
        $('#enforce-api-security-edit').val(data['enforce_api_security']);
        $('#save-org-btn').click(function(){var id="'" + data['id'] +"'"; saveOrg(id);});
        // remove all existing rows first
        $('#oas-tbl tbody').empty();
        // add row based on returned data
        for(var i = 0; i < data['oas'].length; ++i) {
            var row_html = '<tr><td>' + data['oas'][i]['name'] + 
                '</td><td>' + data['oas'][i]['username'] + 
                '</td><td>' + data['oas'][i]['email'] + 
                '</td><td><a class="link" style="color:#0F71C7" href="javascript:void(0)" onclick="return removeOA(\'' + data['oas'][i]['id'] + '\',\'' + data['oas'][i]['name'] + '\')"><img height="12px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.remove" /></a></td></tr>';
            var row = $(row_html);
            $('#oas-tbl tbody').append(row);
        }
    }
    function removeOA(id, name) {
        ocsConfirm($.i18n.message('cp.text.confirm_remove_admin',name), $.i18n.message('cp.title.confirm'),
        function(choice){
            if(choice){
            }
        });
        return false;
    }
    function saveOrg(orgId) {
        if(!$('#org-edit-form').validationEngine('validate')){
            return false;
        }
        $.ajax({
            url: '${contextPath}/organizations!save',
            type: 'POST',
            dataType: 'json',
            data: {id: orgId,
                name: $('#name-edit').val(), 
                address: $('#address-edit').val(),
                url: $('#url').val(),
                enforceapisecurity: $('#enforce-api-security-edit').val()
            },
            success: function(r) {
            }
        });
        return false;
    }
</script>