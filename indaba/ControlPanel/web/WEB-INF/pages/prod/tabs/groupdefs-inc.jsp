<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="productGroupdefsTab">
    <div id="groupdefsFlexGrid" style="margin-left: -20px; margin-top: 0px;">
        <div id="groupdefs-flexgrid"></div>
    </div>
</div>
<jsp:include page="groupdef-form-inc.jsp" flush="true" />
<script type="text/javascript" charset="utf-8">
    var grpAllUsers = [];
    var grpAllRoles = [];
    var grpInited = false;
    $(function(){
        if (!grpInited) {
            GRPinitUsersRoles();
            $("#groupdefs-flexgrid").flexigrid({
                autoload: false,
                //url: '${contextPath}/prod/notedef!find?prodId=',
                method: 'POST',
                dataType: 'json',
                colModel : [
                    {display: 'ID', name : 'groupdef_id', width : 10, sortable : false, hide: true},
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
                                 '<a class="link" href="javascript:void(0)" onclick="return doEditGroupdef('+elem.groupdef_id+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"  /></a>' +
                                 '<a class="link" href="" onclick="return doDeleteGroupdef('+elem.groupdef_id +');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"/></a>';
                        if (elem.enabled) {
                            elem.status = "Enabled";
                            actions = actions + '<a class="link" href="" onclick="return doDisableGroupdef('+elem.groupdef_id +');"><indaba:msg key="cp.btn.disable"/></a>';
                        } else {
                            elem.status = "Disabled";
                            actions = actions + '<a class="link" href="" onclick="return doEnableGroupdef('+elem.groupdef_id +');"><indaba:msg key="cp.btn.enable"/></a>';
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
                            setOptions($('#groupdefFormPopup select#grp-usernames'), grpAllUsers);
                            setOptions($('#groupdefFormPopup select#grp-rolenames'), grpAllRoles);
                            $('#groupdefFormPopup').dialog('option', 'title', $.i18n.message('cp.label.add_groupdef'));
                            $('#grp-mode', $('#groupdefFormPopup')).val("add");
                            $('#groupdefFormPopup').dialog('open');
                            return false;
                          }
                    }
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "name",
                sortorder: "asc",
                usepager: true,
                title: 'Manage Discussion Groups',
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
            $("#groupdefs-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/groupdef!find?prodId='+${prodId}});
            grpInited = true;
        }
    });
    
    function GRPinitUsersRoles() {
        grpAllUsers = [];
        grpAllRoles = [];
        $.ajax({type: 'POST',dataType: 'json',url:'${contextPath}/prod/groupdef!getUsersRolesForGroupdef',
            data: {projId:${projId}},
            success: function(data){
                if(data.ret == 0) {
                   if (data.data.Users) {   
                    $.each(data.data.Users, function(index, val){
                        var obj = {
                                id: val.id,
                                name: val.name
                            };
                            grpAllUsers.push(obj);
                        });
                    }
                    if (data.data.Roles) {
                    $.each(data.data.Roles, function(index, val){
                        var obj = {
                                id: val.id,
                                name: val.name
                            };
                            grpAllRoles.push(obj);
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
    

    function doDeleteGroupdef(groupdefId) {

        ocsConfirm($.i18n.message('cp.text.confirm_delete_groupdef'),
            $.i18n.message('cp.title.confirm'),
            function(confirmed){
                if(confirmed) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '${contextPath}/prod/groupdef!delete',
                        data: { groupdefId: groupdefId },
                        success: function(resp){
                            if(resp.ret == 0) {
                                $("#groupdefs-flexgrid").flexReload({});
                            } else {
                                ocsError(resp.desc);
                            }}
                     });
                }
         });

        return false;
    }



    function doDisableGroupdef(groupdefId) {

        ocsConfirm($.i18n.message('cp.text.confirm_disable_groupdef'),
            $.i18n.message('cp.title.confirm'),
            function(confirmed){
                if(confirmed) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '${contextPath}/prod/groupdef!disable',
                        data: { groupdefId: groupdefId },
                        success: function(resp){
                            if(resp.ret == 0) {
                                $("#groupdefs-flexgrid").flexReload({});
                            } else {
                                ocsError(resp.desc);
                            }}
                     });
                }
         });

        return false;
    }


    function doEnableGroupdef(groupdefId) {

        ocsConfirm($.i18n.message('cp.text.confirm_enable_groupdef'),
            $.i18n.message('cp.title.confirm'),
            function(confirmed){
                if(confirmed) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '${contextPath}/prod/groupdef!enable',
                        data: { groupdefId: groupdefId },
                        success: function(resp){
                            if(resp.ret == 0) {
                                $("#groupdefs-flexgrid").flexReload({});
                            } else {
                                ocsError(resp.desc);
                            }}
                     });
                }
         });

        return false;
    }

    
    function doEditGroupdef(groupdefId) {
    
        $.ajax({
            type: 'POST', dataType: 'json', url:'${contextPath}/prod/groupdef!get', data: { groupdefId: groupdefId },
            success: function(resp){
                if(resp.ret == 0) {
                        setOptions($('#groupdefFormPopup select#grp-usernames'), grpAllUsers);
                        setOptions($('#groupdefFormPopup select#grp-rolenames'), grpAllRoles);
                        $('#grp-prodId', $('#groupdefFormPopup')).val(${prodId});
                        $('#grp-projId', $('#groupdefFormPopup')).val(${projId});
                        $('#groupdefId', $('#groupdefFormPopup')).val(groupdefId);
                        $('#groupdefFormPopup').dialog('option', 'title', $.i18n.message('cp.label.edit_groupdef'));
                        $('#grp-mode', $('#groupdefFormPopup')).val("edit");
                        $('#grp-name', $('#groupdefFormPopup')).val(resp.data.name);
                        $('#grp-desc', $('#groupdefFormPopup')).val(resp.data.description);
                        $('#grp-weight',$('#groupdefFormPopup')).val(resp.data.weight);

                        var $list = $("select#grp-usernames");
                        $.each(resp.data.users, function(index, val){
                                var data = {
                                    id: val.userId,
                                    name: val.name
                                };
                                GRPaddTableRow(data, true, 
                                        ((val.permission == 1)?true:false));
                                var $item = $("select#grp-usernames option[value='"+data.id+"']");
                                toggleChosenItem($list, $item, false, false);

                        });
                        $list.trigger("liszt:updated");

                        $list = $("select#grp-rolenames");
                        $.each(resp.data.roles, function(index, val){
                                var data = {
                                    id: val.roleId,
                                    name: val.name
                                };
                                GRPaddTableRow(data, false,
                                        ((val.permission == 1)?true:false));
                                var $item = $("select#grp-rolenames option[value='"+data.id+"']");
                                toggleChosenItem($list, $item, false, false);
                        });
                        $list.trigger("liszt:updated");
                        
                        $('#groupdefFormPopup').dialog('open');                       
                        return false;
                }
            }
        });
        return false;        
    }
    
    function loadGroupdefFlexGrid(prodId) {
        $("#groupdefs-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/groupdef!find?prodId='+${prodId}});
    }
    
</script>
