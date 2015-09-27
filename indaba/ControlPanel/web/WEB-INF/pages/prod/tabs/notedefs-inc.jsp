<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="productNotedefsTab">
    <div id="notedefsFlexGrid" style="margin-left: -20px; margin-top: 0px;">
        <div id="notedefs-flexgrid"></div>
    </div>
</div>
<jsp:include page="notedef-form-inc.jsp" flush="true" />
<script type="text/javascript" charset="utf-8">
    var ntAllUsers = [];
    var ntpAllRoles = [];
    var ntInited = false;
    $(function(){
        if (!ntInited) {
            NTinitUsersRoles();
            $("#notedefs-flexgrid").flexigrid({
                autoload: false,
                //url: '${contextPath}/prod/notedef!find?prodId=',
                method: 'POST',
                dataType: 'json',
                colModel : [
                    {display: 'ID', name : 'notedef_id', width : 10, sortable : false, hide: true},
                    {display: '<indaba:msg key="cp.ch.name"  />', name : 'name', width : 100, sortable : false},
                    {display: '<indaba:msg key="cp.ch.description"  />', name : 'description', width : 230, sortable : false},
                    {display: '<indaba:msg key="cp.ch.users"  />', name : 'users', width : 152, sortable : false},
                    {display: '<indaba:msg key="cp.ch.roles"  />', name : 'roles', width : 152, sortable : false},
                    {display: '<indaba:msg key="cp.ch.weight"  />', name : 'weight', width : 50, sortable : false},
                    {display: '<indaba:msg key="cp.ch.status"  />', name : 'status', width : 50, sortable : false},
                    {display: '<indaba:msg key="cp.ch.actions"  />', name : 'actions', width : 150, sortable : false, align: 'left'}
                ],
                        
                preProcess: function(data) {
                    $.each(data.rows, function(index, elem){
                        var actions = 
                                 '<a class="link" href="javascript:void(0)" onclick="return doEditNotedef('+elem.notedef_id+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"  /></a>' +
                                 '<a class="link" href="" onclick="return doDeleteNotedef('+elem.notedef_id +');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"/></a>';
                        if (elem.enabled) {
                            elem.status = "Enabled";
                            actions = actions + '<a class="link" href="" onclick="return doDisableNotedef('+elem.notedef_id +');"><indaba:msg key="cp.btn.disable"/></a>';
                        } else {
                            elem.status = "Disabled";
                            actions = actions + '<a class="link" href="" onclick="return doEnableNotedef('+elem.notedef_id +');"><indaba:msg key="cp.btn.enable"/></a>';
                        }
                        elem.actions = actions;
                    });

                    // reset to 'select all'
                    var elem = $('.select');
                    elem.empty();
                    elem.append('<img src="${contextPath}/resources/images/uncheck.png">' + $.i18n.message('cp.btn.select_all'));
                    return data;
                },
                buttons : [
                    {name: '<indaba:msg key="cp.btn.add"  />', 
                     bclass: 'add',
                     bimage: '${contextPath}/resources/images/add.png',
                     attrs:[{name:'rel', value: '#'}],
                     onpress : function(){
                            setOptions($('#notedefFormPopup select#nt-usernames'), ntAllUsers);
                            setOptions($('#notedefFormPopup select#nt-rolenames'), ntAllRoles);
                            $('#notedefFormPopup').dialog('option', 'title', $.i18n.message('cp.label.add_notedef'));
                            $('#nt-mode', $('#notedefFormPopup')).val("add");
                            $('#notedefFormPopup').dialog('open');
                            return false;
                          }
                    }
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "name",
                sortorder: "asc",
                usepager: true,
                title: 'Manage Notes',
                useRp: true,
                rp: 15,
                showTableToggleBtn: false,
                showToggleBtn: false,
                width: 924,
                height: '100%',
                pagestat: 'Displaying {from} to {to} of {total} items',
                onError: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onNoData: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onSuccess: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onSubmit: function(){                   
                    return true;
                }
            });

            $("#notedefs-flexgrid").flexReload({
                newp: 1,
                dataType: 'json',
                url: '${contextPath}/prod/notedef!find?prodId='+${prodId}
            });

            ntInited = true;
        }
    });
    
    function NTinitUsersRoles() {
        ntAllUsers = [];
        ntAllRoles = [];
        $.ajax({type: 'POST',dataType: 'json',url:'${contextPath}/prod/notedef!getUsersRolesForNotedef',
            data: {projId:${projId}},
            success: function(data){
                if(data.ret == 0) {
                   if (data.data.Users) {   
                    $.each(data.data.Users, function(index, val){
                        var obj = {
                                id: val.id,
                                name: val.name
                            };
                            ntAllUsers.push(obj);
                        });
                    }
                    if (data.data.Roles) {
                    $.each(data.data.Roles, function(index, val){
                        var obj = {
                                id: val.id,
                                name: val.name
                            };
                            ntAllRoles.push(obj);
                        });
                    }
                }
                return false;
            },
            error: function(data) {
                defaultAjaxErrorHanlde(data);
            }
        });
        return false;    
    }
    
    function doDeleteNotedef(notedefId) {
        
        ocsConfirm($.i18n.message('cp.text.confirm_delete_notedef'),
            $.i18n.message('cp.title.confirm'),
            function(confirmed){
                if(confirmed) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '${contextPath}/prod/notedef!delete',
                        data: { notedefId: notedefId },
                        success: function(resp){
                            if(resp.ret == 0) {
                                $("#notedefs-flexgrid").flexReload({});
                            } else {
                                ocsError(resp.desc);
                            }}
                     });
                }                
         });

        return false;
    }


    function doEnableNotedef(notedefId) {

        ocsConfirm($.i18n.message('cp.text.confirm_enable_notedef'),
            $.i18n.message('cp.title.confirm'),
            function(confirmed){
                if(confirmed) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '${contextPath}/prod/notedef!enable',
                        data: { notedefId: notedefId },
                        success: function(resp){
                            if(resp.ret == 0) {
                                $("#notedefs-flexgrid").flexReload({});
                            } else {
                                ocsError(resp.desc);
                            }}
                     });
                }
         });

        return false;
    }


    function doDisableNotedef(notedefId) {

        ocsConfirm($.i18n.message('cp.text.confirm_disable_notedef'),
            $.i18n.message('cp.title.confirm'),
            function(confirmed){
                if(confirmed) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '${contextPath}/prod/notedef!disable',
                        data: { notedefId: notedefId },
                        success: function(resp){
                            if(resp.ret == 0) {
                                $("#notedefs-flexgrid").flexReload({});
                            } else {
                                ocsError(resp.desc);
                            }}
                     });
                }
         });

        return false;
    }

    
    function doEditNotedef(notedefId) {
    
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url:'${contextPath}/prod/notedef!get',
            data: { notedefId: notedefId },
            success: function(resp){
                if(resp.ret == 0) {
                        setOptions($('#notedefFormPopup select#nt-usernames'), ntAllUsers);
                        setOptions($('#notedefFormPopup select#nt-rolenames'), ntAllRoles);
                        $('#nt-prodId', $('#notedefFormPopup')).val(${prodId});
                        $('#nt-projId', $('#notedefFormPopup')).val(${projId});
                        $('#notedefId', $('#notedefFormPopup')).val(notedefId);
                        $('#notedefFormPopup').dialog('option', 'title', $.i18n.message('cp.label.edit_notedef'));
                        $('#nt-mode', $('#notedefFormPopup')).val("edit");
                        $('#nt-name', $('#notedefFormPopup')).val(resp.data.name);
                        $('#nt-desc', $('#notedefFormPopup')).val(resp.data.description);
                        $('#nt-weight',$('#notedefFormPopup')).val(resp.data.weight);

                        var $list = $("select#nt-usernames");
                        $.each(resp.data.users, function(index, val){
                                var data = {
                                    id: val.userId,
                                    name: val.name
                                };
                                NTaddTableRow(data, true, 
                                        ((val.permission == 1 || val.permission == 3)?true:false),
                                        ((val.permission == 2 || val.permission == 3)?true:false));
                                var $item = $("select#nt-usernames option[value='"+data.id+"']");
                                toggleChosenItem($list, $item, false, false);

                        });
                        $list.trigger("liszt:updated");

                        $list = $("select#nt-rolenames");
                        $.each(resp.data.roles, function(index, val){
                                var data = {
                                    id: val.roleId,
                                    name: val.name
                                };
                                NTaddTableRow(data, false,
                                        ((val.permission == 1 || val.permission == 3)?true:false),
                                        ((val.permission == 2 || val.permission == 3)?true:false));
                                var $item = $("select#nt-rolenames option[value='"+data.id+"']");
                                toggleChosenItem($list, $item, false, false);
                        });
                        $list.trigger("liszt:updated");
                        
                        $('#notedefFormPopup').dialog('open');                       
                        return false;
                }
            }
        });
        return false;        
    }
    
    function loadNotedefFlexGrid(prodId) {
        $("#notedefs-flexgrid").flexReload({
            newp: 1,
            dataType: 'json',
            url: '${contextPath}/prod/notedef!find?prodId='+${prodId}
        });
        
    }
    
</script>
