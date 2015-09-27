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
    #nt-userFs div.field, #nt-userFs div.field, #nt-roleFs div.field, #nt-roleFs div.field {
        display: inline-block;
        width: 350px;
    }

    fieldset#nt-userFs, fieldset#nt-roleFs {
        border: 1px solid #A4BED4;
        padding: 10px;
        /*width: 499px;*/
    }

    fieldset#nt-userFs legend, fieldset#nt-roleFs legend {
        margin-left: 20px;
        font-size: 14px;
        font-weight: bold;
    }

    .notedef-added table {
        margin: 10px 0px;
        width: 100%;
        border: 1px solid #A4BED4;
    }
    .notedef-added thead {
        /*font-weight: bold;*/
        /*background-color:#888;*/
        /*color: #fff;*/
    }

    .notedef-added thead tr td{
        padding: 10px 0px;
    }
    .notedef-added table tr td{
        padding: 5px;
    }

    thead td.name {
        border-bottom: 1px solid #A4BED4;
    }

    thead td.edit,  thead td.translate, thead td.name {
        border-left: 1px solid #A4BED4;
        border-bottom: 1px solid #A4BED4;
    }
    
    thead td.permission {
        border-left: 2px solid #EFEFEF;
        border-bottom: 2px solid #EFEFEF;
    }
    
    .notedef-added thead td.permission {
        width: 40%;
        text-align:center;
    }
    
    .notedef-added thead td.edit {
        width: 20%;
        text-align:center;
    }
    .notedef-added thead td.translate {
        width: 20%;
        text-align:center;
    }
    .notedef-added thead .name {
        width: 60%;
        text-align:center;
    }
    tr.item td {
        border-bottom: 1px solid #ccc;
    }
    .notedef-added table tr td a.del {
        cursor:pointer;
        position: relative;
        top: 3px;
    }
    .notedef-added table img {
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
    #nt-userBox, #nt-roleBox {
        width:516px;
    }
    #nt-user-def-content, #nt-role-def-content {
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
<div id="notedefFormPopup" title="Create/Edit Groupdef" class="hidden">
    <div id="notedefFormBox" class="popup-container" style="width: 590px; max-width: 590px">
        <form id="notedefForm" action="#" method="POST" >
            <fieldset id="notedefFs">
                <input type="hidden" name="nt-mode" id="nt-mode" value="ADD" />
                <input type="hidden" name="nt-prodId" id="nt-prodId" value="${prodId}" />
                <input type="hidden" name="nt-projId" id="nt-projId" value="${projId}" />
                <input type="hidden" name="notedefId" id="notedefId" value="-1" />
                <dl>
                    <dt><label for="nt-name"><indaba:msg key='cp.label.name'/></label></dt>
                    <dd>
                        <input type="text" id="nt-name" name="nt-name" class="long-input validate[required]"/>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="nt-desc"><indaba:msg key='cp.label.description'/></label></dt>
                    <dd> <textarea id="nt-desc" name="nt-desc" class="long-input"></textarea></dd>
                </dl>
                <dl>
                    <dt><label for="nt-weight"><indaba:msg key='cp.label.weight'/></label></dt>
                    <dd>
                        <input type="text" id="nt-weight" name="nt-weight" class="short-input validate[required]"/>
                    </dd>
                </dl>
            </fieldset>
        </form>
        <div class="row">
            <div id="nt-userselect1" style="margin: 4px 2px; width: 280px; float: left;">
                <select id="nt-usernames" name="nt-usernames" class="short-input " data-placeholder="Choose a user" style="width:270px" onChange="NTaddSelectedUser(this);">
                </select>
            </div>
            <div id="nt-userselect2" style="margin: 4px 2px; width: 280px; float: right;">
                <select id="nt-rolenames" name="nt-rolenames" class="short-input " data-placeholder="Choose a role" style="width:270px" onChange="NTaddSelectedRole(this)">
                </select>
            </div>
        </div>
        <div class="notedef-added" id="ntBox">
            <table id="ntTbl" cellspacing="0">
                <thead>
                    <tr>
                        <td class="name"><indaba:msg key="cp.ch.usersroles"/></td>
                        <td class="edit"><indaba:msg key="cp.ch.edit"/></td>
                        <td class="translate"><indaba:msg key="cp.ch.translate"/></td>
                    </tr>
                </thead>
                <tbody>
                    <tr class="nodata"><td colspan="3"><indaba:msg key="cp.text.no_userrole_selected"/></td></tr>
                </tbody>
                <tfoot>
                    <tr class="dnd-hint"><td colspan="3"><img style="position:relative;top:3px;padding-right: 2px;" src="${contextPath}/resources/images/updown.png"><indaba:msg key="cp.text.click_drag_notes"/></td></tr>
                </tfoot>
            </table>
        </div>
        
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var notedefFormBox = $('#notedefFormBox');
    var notedefForm = $('#notedefForm', notedefFormBox);
    $(function(){
        $('input:text, input:radio, input:checkbox, textarea', notedefFormBox).uniform();
        $("select",notedefFormBox).chosen();
        $('a.ui-btn, button', notedefFormBox).button();
        clearFormFields(notedefFormBox);
        notedefForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
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
        $('#notedefFormPopup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            //maxHeight: $(window).height(),
            modal: true,
            open: function(){
                $('form', $('#notedefFormPopup')).validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
            },
            beforeClose: function(){
                clearFormFields(notedefFormBox);
                $('form',$('#notedefFormPopup')).validationEngine('hideAll');
            },
            close: function(){
                NTresetTable();
            },
            buttons: {
                "Save": function() {
                    if(!$('form:first', notedefFormBox).validationEngine('validate')) {
                        return false;
                    }
                    var reqData = NTgenerateJsonData();
                    
                    var dlg = $(this);
                    $.ajax({
                        url:'${contextPath}/prod/notedef!save',
                        type: 'POST',
                        dataType: 'json',
                        data: reqData,
                        success: function(data){
                            //loading.hide();
                            if(data.ret != 0) {
                                ocsError(data.desc);
                            } else {
                                $("#notedefs-flexgrid").flexReload({
                                    dataType: 'json'
                                });
                                clearFormFields(notedefFormBox);
                                dlg.dialog("close");
                                ocsSuccess("Your work has been saved.");
                            }
                        },
                        error: function(data) {
                            defaultAjaxErrorHanlde(data);
                        }
                    });
                    
                    return false;
                },
                "Cancel": function() {
                    clearFormFields(notedefFormBox);
                    $(this).dialog("close");
                }
            }
        });

    });

    notedefForm.find('#nt-weight').keyup(function() {
        $(this).val($(this).val().replace(/[^0-9|^\-]/g, ''));

    });

    
    function NTaddSelectedUser(elem) {
        var obj = {
                id: elem.value,
                name: $("select#nt-usernames option[value='"+elem.value+"']").text()
            };
        var $item = $("select#nt-usernames option[value='"+elem.value+"']");
        var $list = $("select#nt-usernames");
        toggleChosenItem($list, $item, false, true);
        NTaddTableRow(obj, true, true, false);
    }
    
    function NTaddSelectedRole(elem) {
        var obj = {
                id: elem.value,
                name: $("select#nt-rolenames option[value='"+elem.value+"']").text()
            };
        var $item = $("select#nt-rolenames option[value='"+elem.value+"']");
        var $list = $("select#nt-rolenames");
        toggleChosenItem($list, $item, false, true);
        NTaddTableRow(obj, false, true, false);
    }
    
    function NTaddTableRow(data, bUser, bEdit, bTranslate) {
        if (data.id == "" || data.name=="") return false;
	var ID = ((bUser)?'user-':'role-')+data.id;
        var ntTbl = $('#ntTbl');
        var newtr = $('<tr class="item" id="tr-'+ID+'">');
        var lblTd = $('<td class="name"></td>');
        if (bUser)
            lblTd.append('<a class="del" href="javascript:void(0)"><img title="delete" style="position: relative; top:-4px; width: 10px;" src="${contextPath}/resources/images/delete.png" /></a><img height="14px" src="${contextPath}/resources/images/edit.png">');
        else
            lblTd.append('<a class="del" href="javascript:void(0)"><img title="delete" style="position: relative; top:-4px; width: 10px;" src="${contextPath}/resources/images/delete.png" /></a>');
        lblTd.append('<span class="lbl">'+data.name+'</span>');
        newtr.append(lblTd);

        lblTd = $('<td class="edit"></td>');
        if (bEdit)
            lblTd.append('<input type="checkbox" id="chk1-'+ID+'" name="chk1-'+ID+'"  checked>');
        else
            lblTd.append('<input type="checkbox" id="chk1-'+ID+'" name="chk1-'+ID+'">');
        newtr.append(lblTd);

        lblTd = $('<td class="translate"></td>');
        if (bTranslate)
            lblTd.append('<input type="checkbox" id="chk2-'+ID+'" name="chk2-'+ID+'"  checked>');
        else
            lblTd.append('<input type="checkbox" id="chk2-'+ID+'" name="chk2-'+ID+'">');
        newtr.append(lblTd);

        // Add a new row
        newtr.appendTo($('tbody', ntTbl));
        
        $('a.del',newtr).click(function(){ // Bind delete click event          
            var trElm = $(this).parent().parent();
            var $item, $list;
            if (bUser) {
                  $item = $("select#nt-usernames option[value='"+data.id+"']");
                  $list = $('select#nt-usernames');
            }
            else {
                  $item = $("select#nt-rolenames option[value='"+data.id+"']");
                  $list = $('select#nt-rolenames');
            }
            toggleChosenItem($list, $item, true, true);

            trElm.remove();
            var count = $('tbody tr.item', ntTbl).length;
            if(count == 0) {
              $('tbody tr.nodata', ntTbl).show();
            }
            if(count <= 1) {
              $('tfoot tr.dnd-hint', ntTbl).hide();
            }
            return false;
        });

        var itemCount = $('tbody tr.item', ntTbl).length;
        if(itemCount > 0) { // Remove nodata row if any
            $('tbody tr.nodata', ntTbl).hide();
        }
        $('#ntTbl tfoot').show();
    }
    
    function NTgenerateJsonData() {
        var p = $('#notedefFs');
        var data = extractFormDataToJson(p);
        
        var ntTbl = $('#ntTbl');
        var userList = [];
        var roleList = [];
        
        $('tbody tr.item', ntTbl).each(function(){
           var tr = $(this);
           var ids = tr.attr('id').split('-');
           var id = ids[2];
           var name = $('td.name span.lbl', tr).text();
           var permission = 0;
           var chkId = "#chk1-"+ids[1]+"-"+ids[2];
           if ($(chkId, tr).is(':checked'))
               permission = 1;
           chkId = "#chk2-"+ids[1]+"-"+ids[2];
           if ($(chkId, tr).is(':checked'))
               permission |= 2;
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

    function NTresetTable() {
        var p = $('#ntBox');
        p.hide();
        $('#ntTbl tr.item', p).remove();
        $('#ntTbl tr.nodata', p).show();
        $('#ntTbl tr.dnd-hint', p).hide();
        p.show();
        $('input[name=notedefId]', $('#notedefForm')).val(-1);
    }
       
</script>