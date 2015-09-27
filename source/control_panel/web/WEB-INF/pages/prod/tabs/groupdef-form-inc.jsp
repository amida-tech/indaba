<%-- 
    Document   : groupdef-form-inc
    Created on : Mar 23, 2014, 10:36:10 PM
    Author     : ningshan
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/ui.multiselect.css">
<style type="text/css">
    #grp-userFs div.field, #grp-userFs div.field, #grp-roleFs div.field, #grp-roleFs div.field {
        display: inline-block;
        width: 350px;
    }

    fieldset#grp-userFs, fieldset#grp-roleFs {
        border: 1px solid #A4BED4;
        padding: 10px;
        /*width: 499px;*/
    }

    fieldset#grp-userFs legend, fieldset#grp-roleFs legend {
        margin-left: 20px;
        font-size: 14px;
        font-weight: bold;
    }

    .groupdef-added table {
        margin: 10px 0px;
        width: 100%;
        border: 1px solid #A4BED4;
    }
    .groupdef-added thead {
        /*font-weight: bold;
        background-color:#888;
        color: #fff;*/
    }

    .groupdef-added thead tr td{
        padding: 10px 0px;
    }
    .groupdef-added table tr td{
        padding: 5px;
    }

    thead td.name {
        border-bottom: 1px solid #A4BED4;
    }
    thead td.manage {
        border-left: 1px solid #A4BED4;
        border-bottom: 1px solid #A4BED4;
    }
        
    .groupdef-added thead td.manage {
        width: 30%;
        text-align:center;
    }
    .groupdef-added thead .name {
        width: 70%;
        text-align:center;
    }
    tr.item td {
        border-bottom: 1px solid #ccc;
    }
    .groupdef-added table tr td a.del {
        cursor:pointer;
        position: relative;
        top: 3px;
    }
    .groupdef-added table img {
        margin-right: 3px;
    }
    tr.nodata, tr.dnd-hint {
        color: #aaa;
        text-align: center;
    }
    tfoot tr.dnd-hint{
        background-color: #EFEFEF;
        display: none;
    }
    .drag {
        background-color: #A4BED4;
    }
    tr.odd {
        background-color: #EFEFEF;
    }
    #grp-userBox, #grp-roleBox {
        width:516px;
    }
    #grp-user-def-content, #grp-role-def-content {
        padding: 0px 5px 5px 5px;
        /*display: none;*/
    }
    div.button-row {
        float: right;
    }
    .multiselect {
        width: 407px;
        height: 120px;
    }
    .selectedUser {
        display: none;
    }
</style>
<div id="groupdefFormPopup" title="Create/Edit Groupdef" class="hidden">
    <div id="groupdefFormBox" class="popup-container" style="width: 590px; max-width: 590px">
        <form id="groupdefForm" action="#" method="POST" >
            <fieldset id="groupdefFs">
                <input type="hidden" name="grp-mode" id="grp-mode" value="ADD" />
                <input type="hidden" name="grp-prodId" id="grp-prodId" value="${prodId}" />
                <input type="hidden" name="grp-projId" id="grp-projId" value="${projId}" />
                <input type="hidden" name="groupdefId" id="groupdefId" value="-1" />
                <dl>
                    <dt><label for="grp-name"><indaba:msg key='cp.label.name'/></label></dt>
                    <dd>
                        <input type="text" id="grp-name" name="grp-name" class="long-input validate[required]"/>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="grp-desc"><indaba:msg key='cp.label.description'/></label></dt>
                    <dd> <textarea id="grp-desc" name="grp-desc" class="long-input"></textarea></dd>
                </dl>
                <dl>
                    <dt><label for="grp-weight"><indaba:msg key='cp.label.weight'/></label></dt>
                    <dd>
                        <input type="text" id="grp-weight" name="grp-weight" class="short-input validate[required]"/>
                    </dd>
                </dl>
            </fieldset>
        </form>
        <div class="row">
            <div id="grp-userselect1" style="margin: 4px 2px; width: 280px; float: left;">
                <select id="grp-usernames" name="grp-usernames" data-placeholder="Please choose a user" class="short-input " style="width:270px" onChange="GRPaddSelectedUser(this);">
                </select>
            </div>
            <div id="grp-userselect2" style="margin: 4px 2px; width: 280px; float: right;">
                <select id="grp-rolenames" name="grp-rolenames" data-placeholder="Please choose a role" class="short-input " style="width:270px" onChange="GRPaddSelectedRole(this)">
                </select>
            </div>
        </div>
        <div class="groupdef-added" id="grpBox">
            <table id="grpTbl" cellspacing="0">
                <thead>
                    <tr>
                        <td class="name"><indaba:msg key="cp.ch.usersroles"/></td>
                        <td class="manage"><indaba:msg key="cp.ch.managecommemts"/></td>
                    </tr>
                </thead>
                <tbody>
                    <tr class="nodata"><td colspan="2"><indaba:msg key="cp.text.no_userrole_selected"/></td></tr>
                </tbody>
                <tfoot>
                    <tr class="dnd-hint"><td colspan="2"><img style="position:relative;top:3px;padding-right: 2px;" src="${contextPath}/resources/images/updown.png"><indaba:msg key="cp.text.click_drag_notes"/></td></tr>
                </tfoot>
            </table>
        </div>
        
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var groupdefFormBox = $('#groupdefFormBox');
    var groupdefForm = $('#groupdefForm', groupdefFormBox);
    $(function(){
        $('input:text, input:radio, input:checkbox, textarea', groupdefFormBox).uniform();
        $("select",groupdefFormBox).chosen();
        $('a.ui-btn, button', groupdefFormBox).button();
        clearFormFields(groupdefFormBox);
        groupdefForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
        $('.tiptxt').tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [-2, 10],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            opacity: 0.7
        });
        
        // Dialog
        $('#groupdefFormPopup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            //maxHeight: $(window).height(),
            modal: true,
            open: function(){
                $('form', $('#groupdefFormPopup')).validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
            },
            beforeClose: function(){
                clearFormFields(groupdefFormBox);
                $('form',$('#groupdefFormPopup')).validationEngine('hideAll');
            },
            close: function(){
                GRPresetTable();
            },
            buttons: {
                "Save": function() {
                    if(!$('form:first', groupdefFormBox).validationEngine('validate')) {
                        return false;
                    }
                    var reqData = GRPgenerateJsonData();
                    
                    var dlg = $(this);
                    $.ajax({
                        url:'${contextPath}/prod/groupdef!save',
                        type: 'POST',
                        dataType: 'json',
                        data: reqData,
                        success: function(data){
                            //loading.hide();
                            if(data.ret != 0) {
                                ocsError(data.desc);
                            } else {
                                $("#groupdefs-flexgrid").flexReload({
                                    dataType: 'json'
                                });
                                clearFormFields(groupdefFormBox);
                                dlg.dialog("close");
                                ocsSuccess('Your work has been saved.');
                            }
                        },
                        error: function(data) {
                            defaultAjaxErrorHanlde(data);
                        }
                    });
                    
                    return false;
                },
                "Cancel": function() {
                    clearFormFields(groupdefFormBox);
                    $(this).dialog("close");
                }
            }
        });

    });

    groupdefForm.find('#grp-weight').keyup(function() {
        $(this).val($(this).val().replace(/[^0-9|^\-]/g, ''));

    });


    function GRPaddSelectedUser(elem) {
        var obj = {
                id: elem.value,
                name: $("select#grp-usernames option[value='"+elem.value+"']").text()
            };
        var $item = $("select#grp-usernames option[value='"+elem.value+"']");
        var $select = $('select#grp-usernames');
        toggleChosenItem($select, $item, false, true);
        GRPaddTableRow(obj, true, false);
    }
    
    function GRPaddSelectedRole(elem) {
        var obj = {
                id: elem.value,
                name: $("select#grp-rolenames option[value='"+elem.value+"']").text()
            };
        var $item = $("select#grp-rolenames option[value='"+elem.value+"']");
        var $select = $("select#grp-rolenames");
        toggleChosenItem($select, $item, false, true);
        GRPaddTableRow(obj, false, false);
    }
    
    function GRPaddTableRow(data, bUser, bManageComments) {
        if (data.id == "" || data.name=="")
            return false;
	var ID = ((bUser)?'user-':'role-')+data.id;
        var grpTbl = $('#grpTbl');
        var newtr = $('<tr class="item" id="tr-'+ID+'">');
        var lblTd = $('<td class="name"></td>');
        if (bUser)
            lblTd.append('<a class="del" href="javascript:void(0)"><img title="delete" style="position: relative; top:-4px; width: 10px;" src="${contextPath}/resources/images/delete.png" /></a><img height="14px" src="${contextPath}/resources/images/edit.png">');
        else
            lblTd.append('<a class="del" href="javascript:void(0)"><img title="delete" style="position: relative; top:-4px; width: 10px;" src="${contextPath}/resources/images/delete.png" /></a>');
        lblTd.append('<span class="lbl">'+data.name+'</span>');
        newtr.append(lblTd);

        lblTd = $('<td class="manage"></td>');
        if (bManageComments)
            lblTd.append('<input type="checkbox" id="chk1-'+ID+'" name="chk1-'+ID+'"  checked>');
        else
            lblTd.append('<input type="checkbox" id="chk1-'+ID+'" name="chk1-'+ID+'">');
        newtr.append(lblTd);

        // Add a new row
        newtr.appendTo($('tbody', grpTbl));

        $('a.del',newtr).click(function(){ // Bind delete click event           
            var trElm = $(this).parent().parent();
            var $item, $select;
            if (bUser) {
                  $item = $("select#grp-usernames option[value='"+data.id+"']");
                  $select = $('select#grp-usernames');
            }
            else {
                  $item = $("select#grp-rolenames option[value='"+data.id+"']");
                  $select = $('select#grp-rolenames');                 
            }
            toggleChosenItem($select, $item, true, true);
            trElm.remove();
            var count = $('tbody tr.item', grpTbl).length;
            if(count == 0) {
              $('tbody tr.nodata', grpTbl).show();
            }
            if(count <= 1) {
              $('tfoot tr.dnd-hint', grpTbl).hide();
            }
            return false;
        });

        var itemCount = $('tbody tr.item', grpTbl).length;
        if(itemCount > 0) { // Remove nodata row if any
            $('tbody tr.nodata', grpTbl).hide();
        }
        $('#grpTbl tfoot').show();
    }
    
    function GRPgenerateJsonData() {
        var p = $('#groupdefFs');
        var data = extractFormDataToJson(p);
        
        var grpTbl = $('#grpTbl');
        var userList = [];
        var roleList = [];
        
        $('tbody tr.item', grpTbl).each(function(){
           var tr = $(this);
           var ids = tr.attr('id').split('-');
           var id = ids[2];
           var name = $('td.name span.lbl', tr).text();
           var permission = 0;
           var chkId = "#chk1-"+ids[1]+"-"+ids[2];
           if ($(chkId, tr).is(':checked'))
               permission = 1;
           var obj = {
               id : id,
               name : name,
               permission : permission
           };
           if ("user" == ids[1]) {
               userList[userList.length] = obj;
           }
           else {
               roleList[roleList.length] = obj;
           }
       });
       data['users'] = JSON.stringify(userList);
       data['roles'] = JSON.stringify(roleList);
       return data;
    }

    function GRPresetTable() {
        var p = $('#grpBox');
        p.hide();
        $('#grpTbl tr.item', p).remove();
        $('#grpTbl tr.nodata', p).show();
        $('#grpTbl tr.dnd-hint', p).hide();
        p.show();
        $('input[name=groupdefId]', $('#groupdefForm')).val(-1);
    }
       
</script>