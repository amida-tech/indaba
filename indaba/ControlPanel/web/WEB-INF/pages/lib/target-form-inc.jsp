<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/ui.multiselect.css">

<div id="targetFormPopup" title="Add Indicator" class="hidden">
    <div id="targetFormBox" class="popup-container">
        <form id="targetForm" action="#" method="POST">
            <fieldset>
                <input type="hidden" name="visibility" id="visibility" value="${visibility}"/>
                <input type="hidden" name="id" id="targetId" value="-1"/>
                <dl>
                    <dt><label for="target"><indaba:msg key="cp.label.target_name"/></label></dt>
                    <dd><input type="text" name="name" id="name" size="30" maxlength="100" class="validate[required, ajax[ajaxValidateTargetNameExists]]"/></dd>
                </dl>
                <dl>
                    <dt><label for="shortName"><indaba:msg key="cp.label.short_name"/></label></dt>
                    <dd><input type="text" name="shortName" id="shortName" size="30" maxlength="45" class="validate[required, custom[validateTargetShortName], ajax[ajaxValidateTargetShortNameExists]]"/></dd>
                </dl>
                <dl id="orgname-item">
                    <dt><label for="orgName"><indaba:msg key="cp.label.organization"/></label></dt>
                    <dd><input type="text" name="orgname" id="orgname" size="30" maxlength="100" readonly="readonly"/></dd>
                </dl>
                <dl id="org-item">
                    <dt><label for="organization"><indaba:msg key="cp.label.organization"/></label></dt>
                    <dd>
                        <select name="organization" id="organization" style="width:300px;" data-placeholder="<indaba:msg key='cp.text.choose_organization' />" class="validate[required]">
                            <option value=""></option>
                            <c:forEach items="${ownOrgs}" var="org">
                                <option value="${org.id}">${org.name}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="guid"><indaba:msg key="cp.label.guid"/><span class="tiptxt" title="<indaba:msg key='cp.tip.target_guid'/>">&nbsp;</span></label></dt>
                    <dd><input type="text" name="guid" id="guid" size="60" maxlength="255" /></dd>
                </dl>
                <dl>
                    <dt><label for="targetType"><indaba:msg key="cp.label.target_type"/></label></dt>
                    <dd>
                        <select name="targetType" id="targetType" style="width:200px;" data-placeholder="<indaba:msg key='cp.text.choose_type' />" class="validate[required]">
                            <option value=""></option> 
                            <option value="0">Country</option>
                            <option value="1">International Region</option>
                            <option value="2">Sub-national: Province</option>
                            <option value="3">Sub-national: State</option>
                            <option value="4">Sub-national: Region</option>
                            <option value="5">Sub-national: City/Municipality</option>
                            <option value="6">Organization</option>
                            <option value="7">Government Unit/Project</option>
                            <option value="8">Sector</option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="language"><indaba:msg key="cp.label.language"/></label></dt>
                    <dd>
                        <select name="language" id="language" style="width:200px;" data-placeholder="<indaba:msg key='cp.text.choose_language' />" class="validate[required]">
                            <option value=""></option>
                            <c:forEach var="lang" items="${languages}">
                                <option value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="tags"><indaba:msg key="cp.label.tags"/></label></dt>
                    <dd>
                        <select name="tags" id="tags" multiple style="width:400px;" data-placeholder="<indaba:msg key='cp.text.add_tag' />">
                            <option value=""></option> 
                            <c:forEach items="${tags}" var="tag">
                                <option value="${tag.id}">${tag.term}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="description"><indaba:msg key="cp.label.description"/><span class="tiptxt" title="<indaba:msg key='cp.tip.target_description' />">&nbsp;</span></label></dt>
                    <dd><textarea name="description" maxlength="255" id="description" rows="4" cols="60"></textarea></dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>

<script type="text/javascript" charset="utf-8">
    var targetForm = $('#targetFormBox');
    $('input, textarea', targetForm).uniform();
    $("select",targetForm).chosen();
    $('a.ui-btn, button').button();
    clearFormFields(targetForm);
    $('form', targetForm).validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
    $('.tiptxt', targetForm).tooltip({
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
    $('#targetFormPopup').dialog({
        autoOpen: false,
        width: 622,
        resizable: false,
        //maxHeight: $(window).height(),
        modal: true,
        open: function(){
            $("input[name=visibility]",targetForm).val('${visibility}').trigger("liszt:updated");
        },
        beforeClose: function(){
            resetTargetForm(targetForm);
            targetForm.validationEngine('hideAll');
        },
        buttons: {
            "Save": function() {
                if(!$('form', targetForm).validationEngine('validate')) {
                    return false;
                }
                var reqData = extractFormDataToJson(targetForm);
                var loading=new ol.loading({id:"targetFormBox", loadingText:"Waiting..."});
                loading.show();
                var dlg = $(this);
                $.ajax({
                    url:'${contextPath}/lib/target-lib!create', 
                    type: 'POST', 
                    dataType: 'json',
                    data: reqData, 
                    success: function(data){
                        loading.hide();
                        if(data.ret != 0) {
                            ocsError(data.desc);
                        } else {
                            $("#target-flexgrid").flexReload({
                                dataType: 'json'
                            });
                            clearFormFields(targetForm);
                            dlg.dialog("close");
                            ocsSuccess("Target definition saved!");
                        }
                        resetTargetForm(targetForm);
                    },
                    error: function(data) {
                        loading.hide();
                        resetTargetForm(targetForm);
                        defaultAjaxErrorHanlde(data);
                    }
                });
                return false;
            },
            "Cancel": function() {
                resetTargetForm(targetForm);
                clearFormFields(targetForm);
                $(this).dialog("close");
            }
        }
    });
</script>