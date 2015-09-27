<%--
    Document   : Project Tab - 'Administrators'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="projectBaseTab">
    <form id="baseinfoForm" action="#" method="POST">
        <div class="form-buttons" style="margin-bottom: 10px;">
            <a id="save" <c:if test="${projId>0}">style="display: none;"</c:if> href="javascript:void(0)" class="btn save"><img src="${contextPath}/resources/images/save.png" /><indaba:msg key="cp.btn.save" /></a>
            <a id="cancel" <c:if test="${projId>0}">style="display: none;"</c:if> href="javascript:void(0)" class="btn cancel"><img src="${contextPath}/resources/images/undo.png" /><indaba:msg key="cp.btn.cancel" /></a>
                <c:if test="${projId>0}">
                <a id="edit" href="javascript:void(0)" class="btn edit"><img src="${contextPath}/resources/images/btn_edit.png" /><indaba:msg key="cp.btn.edit" /></a>
                </c:if>
        </div>

        <fieldset class="block">
            <input type="hidden" name="projId" id="projId" value="${projId}"/>
            <dl>
                <dt><label for="project"><indaba:msg key="cp.label.project_name"  /></label></dt>
                <dd><input type="text" name="projName" id="projName" maxlength="100" class="long-input validate[required<c:if test="${projJson == null}">, ajax[ajaxValidateProjectExists]</c:if>]"/></dd>
            </dl>
            <!--
            <dl>
                <dt><label for="language"><indaba:msg key="cp.label.language"/></label></dt>
                <dd>
                    <select id="language" name="language" data-placeholder="<indaba:msg key='cp.text.choose_language'/>" class="small-input validate[required]">
                        <option value=""></option>
            <c:forEach var="lang" items="${languages}">
                <option value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>
            </c:forEach>
        </select>
    </dd>
</dl>
            -->
            <dl>
                <dt><label for="description"><indaba:msg key="cp.label.description"  /></label></dt>
                <dd><textarea name="description" maxlength="255" id="description" class="long-input"></textarea></dd>
            </dl>
            <dl>
                <dt><label for="primaryOrg"><indaba:msg key="cp.label.primary_owner"  /></label></dt>
                <dd>
                    <select name="primaryOrg" id="primaryOrg" data-placeholder="<indaba:msg key='cp.text.choose_primary_owner'  />" class="short-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${orgs}" var="org">
                            <option value="${org.id}">${org.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="primaryAdmin"><indaba:msg key="cp.label.primary_admin"  /></label></dt>
                <dd>
                    <select name="primaryAdmin" id="primaryAdmin" data-placeholder="<indaba:msg key='cp.text.choose_primary_admin'  />" class="short-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${orgAdmins}" var="oa">
                            <option value="${oa.id}">${oa.firstName} ${oa.lastName}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="visibility"><indaba:msg key="cp.label.visibility"  /></label></dt>
                <dd class="radio">
                    <input type="radio" name="visibility" value="1" class="validate[required]" <c:if test="${visibility == 1}"> checked="checked"</c:if> disabled="true" /><span style="color: #666"><indaba:msg key="cp.label.public"/></span>
                    <input type="radio" name="visibility" value="2" class="validate[required]" <c:if test="${visibility != 1}"> checked="checked"</c:if> disabled="true" /><span style="color: #666"><indaba:msg key="cp.label.private"/></span>
                </dd>
            </dl>
            <dl>
                <dt><label for="actionRight"><indaba:msg key="cp.label.action_rights"  /></label></dt>
                <dd>
                    <select name="actionRight" id="actionRight" data-placeholder="<indaba:msg key='cp.text.add_action_right'  />" class="long-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${actionRights}" var="right">
                            <option value="${right.id}">${right.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="viewRight"><indaba:msg key="cp.label.view_rights"  /></label></dt>
                <dd>
                    <select name="viewRight" id="viewRight" data-placeholder="<indaba:msg key='cp.text.add_view_right'  />" class="long-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${viewRights}" var="right">
                            <option value="${right.id}">${right.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="startTime"><indaba:msg key="cp.label.start_time"  /></label></dt>
                <dd><input type="text" name="startTime" id="startTime" class="datepicker mini-input validate[required, funcCall[validateProjectTime]]"/></dd>
            </dl>
            <dl>
                <dt><label for="endTime"><indaba:msg key="cp.label.end_time"  /></label></dt>
                <dd><input type="text" name="endTime" id="endTime" class="datepicker mini-input validate[required, funcCall[validateProjectTime]]"/></dd>
            </dl>
            <dl>
                <dt><label for="active"><indaba:msg key="cp.label.is_active"  /></label></dt>
                <dd class="radio">
                    <input type="radio" name="active" value="1" class="validate[required]"  checked="checked"/><span class="choice-txt"><indaba:msg key="cp.label.active"  /></span>
                    <input type="radio" name="active" value="2" class="validate[required]" /><span class="choice-txt"><indaba:msg key="cp.label.inactive"  /></span>
                </dd>
            </dl>
            <dl>
                <dt><label for="studyPeriod"><indaba:msg key="cp.label.study_period"  /></label></dt>
                <dd>
                    <select name="studyPeriod" id="studyPeriod" data-placeholder="<indaba:msg key='cp.text.add_study_period'  />" class="short-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${studyPeriods}" var="period">
                            <option value="${period.id}">${period.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl style="display: none">
                <dt><label for="projLogo"><indaba:msg key="cp.label.project_logo"  /></label></dt>
                <dd>
                    <div class="fileUpload">
                        <span class="uploadControl">
                            <input type="file" name="upload" id="projLogo"/>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a class="btn gray" id="projLogoUploadBtn" href="javascript:void(0)"><indaba:msg key="cp.btn.upload"  /></a>
                            <img class="uploading-img" alt="uploading image" src="${contextPath}/resources/images/ajax-loader.gif" width="20px" style="display:none; vertical-align: middle;">
                            <span class="error-box hidden"></span>
                        </span>
                        <ul class="logoUploadSuccess"></ul>
                    </div>
                </dd>
            </dl>
            <dl>
                <dt><label for="sponsorLogo"><indaba:msg key="cp.label.sponsor_logo"  /></label></dt>
                <dd>
                    <div class="fileUpload">
                        <span class="uploadControl">
                            <input type="file" name="upload" id="sponsorLogo" class="mini-input"/>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a class="btn gray" id="sponsorLogoUploadBtn" href="javascript:void(0)">&nbsp;&nbsp;&nbsp;<indaba:msg key="cp.btn.add"  />&nbsp;&nbsp;&nbsp;</a>
                            <img class="uploading-img" alt="uploading image" src="${contextPath}/resources/images/ajax-loader.gif" width="20px" style="display:none; vertical-align: middle;">
                            <span class="error-box hidden"></span>
                        </span>
                        <ul class="logoUploadSuccess"></ul>
                    </div>
                </dd>
            </dl>
        </fieldset>
        <div class="form-buttons">
            <a id="save" <c:if test="${projId>0}">style="display: none;"</c:if> href="javascript:void(0)" class="btn save"><img src="${contextPath}/resources/images/save.png" /><indaba:msg key="cp.btn.save" /></a>
            <a id="cancel" <c:if test="${projId>0}">style="display: none;"</c:if> href="javascript:void(0)" class="btn cancel"><img src="${contextPath}/resources/images/undo.png" /><indaba:msg key="cp.btn.cancel" /></a>
                <c:if test="${projId>0}">
                <a id="edit" href="javascript:void(0)" class="btn edit"><img src="${contextPath}/resources/images/btn_edit.png" /><indaba:msg key="cp.btn.edit" /></a>
                </c:if>
        </div>
    </form>
</div>
<script type="text/javascript" charset="utf-8">
    //var projectBaseInfoForm = $('#baseinfoForm');
    var baseInfoinitialized = false;
    $(function(){
        if(!baseInfoinitialized) {
            baseInfoinitialized = true;
            $('form').each(function(){
                if($(this).attr('id') != 'contributor-filter-form') {
                    resetForm($(this));
                }
            });
            checkUniformChoice($('input[name=visibility][value='+${visibility}+']'));
            $('input:radio[name=visibility]').attr('disabled', 'disabled');
            if(${projId} > 0) {
                setFormFieldEditable(false);
            }
            initPorject(${projId}, true);
            $('#baseinfoForm').validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});

            $('.tiptxt', $('#baseinfoForm')).tooltip({
                // place tooltip on the right edge
                position: "center right",
                // a little tweaking of the position
                offset: [-2, 10],
                // use the built-in fadeIn/fadeOut effect
                effect: "fade",
                // custom opacity setting
                opacity: 0.7
            });
            $('span.filename').click(function(){
                $('input#projLogo').trigger('click');
            });
            var orgs = [];
            $('#primaryOrg', $('#baseinfoForm')).change(function(){
                var selectedOrgs = [];
                selectedOrgs[0] = $(this).val();
                $.ajax({
                    url: '${contextPath}/proj/projects!updateOrgData',
                    type: 'POST',
                    dataType: 'json',
                    data: {"orgIds": selectedOrgs},
                    success: function(resp){
                        if(resp.ret != 0) {
                            ocsError(resp.desc + '(ERROR=' + resp.ret+ ')');
                        } else {
                            var options = [{id:"", val:""}];
                            $.each(resp.data.admins, function(index, value){
                                options[options.length] = {id: value.id, val: value.firstName + " " + value.lastName};
                            });
                            orgs = selectedOrgs;
                            var elem = $('#primaryAdmin', $('#baseinfoForm'));
                            updateChosen(elem, options);
                        }
                    }
                });
            });
            $('#baseinfoForm div.form-buttons #edit').click(function(){
                setFormFieldEditable(true);
                return false;
            });
            $('#baseinfoForm div.form-buttons #save').click(function(){
                doSaveProject();
                return false;
            });
            $('#baseinfoForm div.form-buttons #cancel').click(function(){
                ocsConfirm($.i18n.message('cp.text.confirm_cancel_project'), $.i18n.message('cp.title.confirm'),
                function(choice){
                    if(choice){
                        initPorject(${projId});
                        if(${projId > 0}) {
                            setFormFieldEditable(false);
                        } else {
                            history.back();
                        }
                    }
                });
                return false;
            });
            $('#projLogoUploadBtn').click(function(){
                return fileUpload('${contextPath}', '#projLogo', $(this), false, "projectLogo");
            });
            // Multiple file upload
            $('#sponsorLogoUploadBtn').click(function(){
                return fileUpload('${contextPath}', '#sponsorLogo', $(this), true, "sponsorLogo");
            });
            $('.fileUpload input:file').change(function(){
                return checkImage($(this));
            });
        }
    });
    function initPorject(projId, updateOtherTabs){
        if(projId <= 0) {
            return;
        }
        $.ajax({
            url:'${contextPath}/proj/projects!getProject', 
            type: 'POST',dataType: 'json',data: {projId: projId},
            success: function(resp){
                if(resp.ret != 0) {
                    ocsError(resp.desc);
                } else {
                    initProjectForm(resp.data, updateOtherTabs);
                    if(${projId} > 0) {
                        setFormFieldEditable(false);
                    }
                    $('#baseinfoForm').validationEngine('hideAll');
                }
            }
        });
    }
    function initProjectForm(data, updateOtherTabs) {
        $('form #projId').each(function(){
            $(this).val(data.id);
        });
        $('#projName').val(data.name);
        $('#nameLabel').text(data.name);
        updateSelectField($('#language'), [data.langId]);
        $('#description').val(data.description);
        updateSelectField($('#primaryOrg'), [data.primaryOrgId]);
        updateSelectField($('#primaryAdmin'), [data.primaryAdminId]);
        checkUniformChoice($('input[name=visibility][value='+data.visibility+']'));
        checkUniformChoice($('input[name=active][value='+(data.active?'1':'2')+']'));
        updateSelectField($('#actionRight'), [data.actionRightId]);
        updateSelectField($('#viewRight'), [data.viewRightId]);
        $('#startTime').val(data.startTimeTxt);
        $('#endTime').val(data.endTimeTxt);
        updateSelectField($('#studyPeriod'), [data.studyPeriodId]);
        $('input[name=active]').removeAttr('checked');
        $('input[name=active][value='+(data.active?1:2)+']').attr('checked', 'checked');
        $("select").trigger("liszt:updated");
        var projLogo = data.projLogoFile;
        var sponsorLogos = data.sponsorLogoFiles;
        if(projLogo) {
            addUploadedItem('${contextPath}', $('#projLogo').parents("div.fileUpload"), projLogo, false, function(){
                handleUpdateProjLogo('${contextPath}', data.id);
            });
        }
        if(sponsorLogos) {
            $.each(sponsorLogos, function(index, sponsorLogo){
                addUploadedItem('${contextPath}', $('#sponsorLogo').parents("div.fileUpload"), sponsorLogo, true, function(){
                    handleUpdateSponsorLogo('${contextPath}', data.id);
                });
            });
        }
        if(updateOtherTabs) {
            $.each(data.secondaryOrgIds, function(index, value){
                $('option[value="'+value+'"]', $('#secondaryOrgs')).attr('selected', 'selected').trigger("liszt:updated");
            });
            updateSelectField($('#secondaryOrgs'), data.secondaryOrgIds);
            disableChosenOption($('#secondaryOrgs'), data.primaryOrgId);
            if(${not isSA}) { // disable those unselected options
                $('#secondaryOrgs option').attr('disabled', 'disabled');
            }
            updateSelectField($('#secondaryAdmins'), data.secondaryAdminIds);
            disableChosenOption($('#secondaryAdmins'), data.primaryAdminId);
            if(${(not isSA) && (not isPA)}) { // disable those unselected options
                $('#secondaryAdmins option').attr('disabled', 'disabled');
            }
            updateSelectField($('#roles'), data.roleIds);
            //updateSelectField($('#targets'), data.targetIds);
            $('select').trigger("liszt:updated");
        }
    }
    function doSaveProject(projId, visibility){
        if(!$('#baseinfoForm').validationEngine('validate')) {
            return false;
        }
        var reqData = extractFormDataToJson($('#baseinfoForm'));
        reqData['projLogo'] = $('ul.logoUploadSuccess span.uploaded-logo', $('#projLogo', $('#baseinfoForm')).parents("div.fileUpload")).attr('uname');

        var sponsorLogos = [];
        $('ul.logoUploadSuccess span.uploaded-logo', $('#sponsorLogo', $('#baseinfoForm')).parents("div.fileUpload")).each(function(){
            sponsorLogos[sponsorLogos.length] = $(this).attr('uname');
        });
        reqData['sponsorLogos'] = sponsorLogos;
        $.ajax({
            type: 'POST',dataType: 'json',data: reqData,
            url: '${contextPath}/proj/projects!create',
            success: function(data){
                if(data.ret != 0) {
                    ocsError(data.desc + '(ERROR=' + data.ret+ ')');
                } else {
                    ocsSuccess((${projId<=0})?$.i18n.message('cp.text.success_create_project'):$.i18n.message('cp.text.success_save_project'),
                    $.i18n.message('cp.title.success'), function(){
                        if(${projId > 0}) {
                           // setFormFieldEditable(false);
                            window.location.href= "${contextPath}/proj/projects!projectForm?visibility=${visibility}&projId=${projId}";
                        } else {
                            history.back();
                        }
                    });
                }
            }
        });
        return false;
    }
    function setFormFieldEditable(editable) {
        if(editable) {
            $('#baseinfoForm div.form-buttons').find('#save, #cancel').show();
            $('#baseinfoForm div.form-buttons').find('#edit').hide();
            $('#baseinfoForm input, #baseinfoForm textarea, #baseinfoForm select').removeAttr('disabled');

            <c:if test="${not isSA}">
                $('#baseinfoForm #primaryOrg').attr('disabled', 'disabled');
                $('#baseinfoForm #primaryAdmin').attr('disabled', 'disabled');
            </c:if>

            $('#baseinfoForm .fileUpload').find('a.btn').css('display','inline');
            $('#baseinfoForm .choice-txt,#baseinfoForm input[class!=default],#baseinfoForm textarea').css('color','#000');
            if($('#baseinfoForm .fileUpload span.action').hasClass('disabled')) {
                $('#baseinfoForm .fileUpload').find('span.action').removeClass('disabled');
            }
            $('#baseinfoForm input:radio[name=visibility]').attr('disabled', 'disabled');
            $('#baseinfoForm .fileUpload a.deleteLogoBtn').each(function() {
                if($(this).hasClass('view')) {
                    $(this).removeClass('view')
                }
                if(!$(this).hasClass('remove')) {
                    $(this).addClass('remove')
                }
                $(this).css('cursor', 'pointer');
            });
        } else {
            $('#baseinfoForm div.form-buttons').find('#save, #cancel').hide();
            $('#baseinfoForm div.form-buttons').find('#edit').show();
            $('#baseinfoForm input, #baseinfoForm textarea, #baseinfoForm select').attr('disabled', 'disabled');
            $('#baseinfoForm .fileUpload').find('a.btn').css('display','none');
            $('#baseinfoForm .choice-txt,#baseinfoForm input,#baseinfoForm textarea').css('color','#666');

            if(!$('#baseinfoForm .fileUpload span.action').hasClass('disabled')) {
                $('#baseinfoForm .fileUpload').find('span.action').addClass('disabled');
            }
            $('#baseinfoForm .fileUpload a.deleteLogoBtn').each(function() {
                if($(this).hasClass('remove')) {
                    $(this).removeClass('remove')
                }
                if(!$(this).hasClass('view')) {
                    $(this).addClass('view')
                }
                $(this).css('cursor', 'default');
            });
        }
        $('#baseinfoForm select').trigger("liszt:updated");
    }
</script>