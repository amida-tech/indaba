<%--
    Document   : Project Tab - 'Userfinders'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="userfindersTab">
    <div id="query-result">
        <div id="userfinder-flexgrid"></div>
    </div>
    <div id="userfinderFormPopup" title="<indaba:msg key='cp.title.editUserfinder' />" class="hidden">
        <div id="userfinderFormBox" class="popup-container" style="width: 650px; max-width: 650px; padding-right: 20px;">
            <form id="userfindersForm" action="#" method="POST">  <fieldset class="block">
                    <input type="hidden" name="projId" id="projId" value="${projId}"/>
                    <input type="hidden" name="userfinderId" id="userfinderId" value="-1"/>
                    <dl>
                        <dt><label for="prodId">Scope</label></dt>
                        <dd>
                            <select name="prodId" id="prodId" data-placeholder="Project-wide" class="long-input">
                                <option value="0"></option>
                            </select>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="roleId"><indaba:msg key="cp.label.monitored_role" /></label></dt>
                        <dd>
                            <select name="roleId" id="roleId" data-placeholder="<indaba:msg key='cp.text.choose_role' />" class="long-input validate[required]">
                                <option value="0"></option>
                            </select>
                        </dd>
                    </dl>                    
                    <dl>
                        <dt><label for="assignedUserId"><indaba:msg key="cp.label.assign_case_to" /></label></dt>
                        <dd>
                            <select name="assignedUserId" id="assignedUserId" data-placeholder="<indaba:msg key='cp.text.choose_user' />" class="long-input validate[required]">
                                <option value="0"></option>
                            </select>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="caseSubject"><indaba:msg key="cp.label.caseSubject" /></label></dt>
                        <dd><textarea name="caseSubject" maxlength="100" id="caseSubject" class="long-input validate[required]"></textarea></dd>
                    </dl>
                    <dl>
                        <dt><label for="caseBody"><indaba:msg key="cp.label.caseBody" /></label></dt>
                        <dd><textarea name="caseBody" id="caseBody" class="long-input validate[required]"></textarea></dd>
                    </dl>
                    <dl>
                        <dt><label for="casePriority"><indaba:msg key="cp.label.casePriority"/></label></dt>
                        <dd class="radio">
                            <input type="radio" name="casePriority" value="0" class="validate[required]" /><span style="color: #666"><indaba:msg key="cp.text.low"/></span>
                            <input type="radio" name="casePriority" value="1" class="validate[required]" /><span style="color: #666"><indaba:msg key="cp.text.medium"/></span>
                            <input type="radio" name="casePriority" value="2" class="validate[required]" /><span style="color: #666"><indaba:msg key="cp.text.high"/></span>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="attachUser"><indaba:msg key="cp.label.attachUser" /></label></dt>
                        <dd><input name="attachUser" id="attachUser" type="checkbox" value="true" /></dd>
                    </dl>
                    <dl>
                        <dt><label for="attachContent"><indaba:msg key="cp.label.attachContent" /></label></dt>
                        <dd><input name="attachContent" id="attachContent" type="checkbox" value="true" /></dd>
                    </dl>
                    <dl>
                        <dt><label for="status"><indaba:msg key="cp.label.status"/></label></dt>
                        <dd class="radio">
                            <input type="radio" name="status" value="1" class="validate[required]" /><span style="color: #666"><indaba:msg key="cp.text.active"/></span>
                            <input type="radio" name="status" value="2" class="validate[required]" /><span style="color: #666"><indaba:msg key="cp.text.inactive"/></span>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="description"><indaba:msg key="cp.label.description" /></label></dt>
                        <dd><textarea name="description" id="description" maxlength="255" class="long-input"></textarea></dd>
                    </dl>
                </fieldset>
                <div id="resultBox" style="background:#FFFFCC;" class="error-box hidden"></div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var userfindersTabInitialized = false;
    $(function(){
        if(!userfindersTabInitialized) {
            userfindersTabInitialized = true;
            initUserfinderOptions();
            $("#userfinder-flexgrid").flexigrid({
                // url: '${contextPath}/proj/userfinder!find?projId=${projId}&visibility=${visibility}',
                method: 'POST',
                dataType: 'json',
                preProcess: function(data) {
                    if (data.canAddUserfinders != 'yes') {
                        $('.adduserfinder').parent().parent().hide();
                    }

                    $.each(data.rows, function(index, elem){
                        var userfinderId = elem.id;
                        var userfinderName = elem.name;
                        var actions = "";
                        actions += '<a class="link" href="javascript:void(0)" onclick="return doOpenUserfinderDlg('+userfinderId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                        actions += '<a class="link" href="javascript:void(0)" onclick="return doDeleteUserfinder(' + userfinderId + ',\''+userfinderName+'\');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"/></a>';
                        elem.actions = actions;

                        if (!elem.productName) {
                            elem.productName = '<em>Project-wide</em>';
                        }
                    });
                    return data;
                },
                colModel : [
                    {display: '<indaba:msg key="cp.ch.id"/>', name : 'id', width : 0, sortable : false, align: 'right', hide: true},
                    {display: 'SCOPE', name : 'productName', width : 200, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.role"/>', name : 'roleName', width : 100, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.assignedUser"/>', name : 'assignedUsername', width : 100, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.caseSubject"/>', name : 'caseSubject', width : 314, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 100, sortable : false, align: 'left'}
                ],
                buttons : [
                    {name: '<indaba:msg key="cp.btn.add" />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png',  onpress : function(){
                            doOpenUserfinderDlg(0);
                            return false;
                        }
                    }
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "productName",
                sortorder: "asc",
                usepager: true,
                title: '<indaba:msg key="cp.label.userfinders" />',
                useRp: true,
                rp: 15,
                showTableToggleBtn: false,
                showToggleBtn: false,
                width: "auto",
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
                    var p = [];
                    $('select, input', $('#userfinder-filter-form')).each(function(){
                        p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                    });
                    this.params = p;
                    return true;
                }
            });
            var userfindersForm = $('#userfindersForm');
            var result = $('#resultBox', userfindersForm);
            userfindersForm.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
            $('#userfinderFormPopup').dialog({
                autoOpen: false,
                width: 722,
                resizable: false,
                modal: true,
                open: function(){
                    result.text('');
                    result.hide();
                },
                beforeClose: function(){
                },
                close: function(){
                    resetForm($('#userfindersForm'));
                    $('#userfindersForm').find('input[name=userfinderId]').val('-1');
                },
                buttons: {
                    "Save": function() {
                        if(!userfindersForm.validationEngine('validate')) {
                            return false;
                        }
                        var dlg = $(this);
                        var reqData = extractFormDataToJson(userfindersForm);
                        $.ajax({
                            url:'${contextPath}/proj/userfinder!save',
                            type: 'POST',
                            dataType: 'json',
                            data: reqData,
                            success: function(resp){
                                if(resp.ret != 0) {
                                    ocsError(resp.desc);
                                } else {
                                    ocsSuccess($.i18n.message('cp.text.success_save_userfinder'));
                                    loadUserfinderFlexGrid();
                                    dlg.dialog("close");
                                }
                            }
                        });
                        return false;
                    },
                    "Cancel": function() {
                        $(this).dialog("close");
                    }
                }
            });
        }
    });
    function initUserfinderOptions() {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: '${contextPath}/proj/userfinder!getOptions',
            data: {projId: ${projId}},
            success: function(data){
                var form = $('#userfindersForm');
                if(data.roles) {
                    setOptions(form.find('select[name=roleId]'), data.roles);
                }
                if(data.products) {
                    setOptions(form.find('select[name=prodId]'), data.products);
                }
                if(data.pmList) {
                    setOptions(form.find('select[name=assignedUserId]'), data.pmList, {id:"userId", attrs: ["firstName", "lastName"]}, {_attrs: ["roleId"]});
                }
            }
        });
    }
    function initUserfinderFormWithData(data) {
        var form = $('#userfindersForm');
        form.find('input[name=userfinderId]').val(data.id);
        selectChosenOptions(form.find('select[name=roleId]'), [data.roleId]);
        selectChosenOptions(form.find('select[name=prodId]'), [data.productId]);
        selectChosenOptions(form.find('select[name=assignedUserId]'),  [data.assignedUserId]);
        form.find('textarea[name=caseSubject]').val(data.caseSubject);
        form.find('textarea[name=caseBody]').val(data.caseBody);
        if(data.attachUser) {
            checkUniformChoice(form.find('input[name=attachUser]'));
        }else{
            uncheckUniformChoice(form.find('input[name=attachUser]'));
        }
        if(data.attachContent) {
            checkUniformChoice(form.find('input[name=attachContent]'));
        }else{
            uncheckUniformChoice(form.find('input[name=attachContent]'));
        }
        checkUniformChoiceV2(form, 'casePriority', data.casePriority);
        checkUniformChoiceV2(form, 'status', data.status);
        form.find('textarea[name=description]').val(data.description);
    }

    function doOpenUserfinderDlg(userfinderId) {
        if(!userfinderId || userfinderId<=0) {
            $('#userfinderFormPopup').dialog('option', 'title', "<indaba:msg key='cp.title.addUserfinder' />");
            $('#userfinderFormPopup').dialog('open');
        } else {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '${contextPath}/proj/userfinder!get',
                data: {userfinderId: userfinderId},
                success: function(resp){
                    if(resp.ret == 0) {
                        if(resp.data) {
                            initUserfinderFormWithData(resp.data);
                            $('#userfinderFormPopup').dialog('option', 'title', "<indaba:msg key='cp.title.editUserfinder' />");
                            $('#userfinderFormPopup').dialog('open');
                        }
                    } else {
                        oscError(resp.desc);
                    }
                }
            });
        }
    }
    function doDeleteUserfinder(userfinderId, userfinderName) {
        ocsConfirm($.i18n.message('cp.text.confirm_delete_userfinder'), $.i18n.message('cp.title.confirm'),
        function(confirmed){
            if(confirmed) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '${contextPath}/proj/userfinder!delete',
                    data: {userfinderId: userfinderId},
                    success: function(data){
                        if(data.ret == 0) {
                            loadUserfinderFlexGrid();
                        } else {
                            ocsError(data.desc);
                        }
                    }
                });
            }
        });
    }

    function loadUserfinderFlexGrid() {
        $("#userfinder-flexgrid").flexOptions({url: '${contextPath}/proj/userfinder!find?projId=${projId}&visibility=${visibility}'}).flexReload({newp: 1,dataType: 'json'});
    }
</script>