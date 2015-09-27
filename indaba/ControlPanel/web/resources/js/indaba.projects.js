
function initProjectsToolbar(visibility, contextPath){
    var projectsToolbarData = {
        parent: "projectsToolbar",
        icon_path: contextPath + "/resources/images/",
        items: [
        {
            type: "buttonTwoState",
            id: "pubProj",
            text: $.i18n.message('cp.tab2.pub_proj'),
            img: "pub_ext_lib.png"
        },
        {
            type: "separator",
            id: "sep1"
        },

        {
            type: "buttonTwoState",
            id: "priProj",
            text: $.i18n.message('cp.tab2.priv_proj'),
            img: "private_lib.png"
        }
        ]
    };
    var toolbar = new dhtmlXToolbarObject(projectsToolbarData, "dhx_blue");
    fixDHTMLXTabbarHeight("content");
    
    if(visibility == '1') {
        toolbar.setItemState("pubProj", true);
    }else if(visibility == '2') {
        toolbar.setItemState("priProj", true);
    }else{
        toolbar.setItemState("pubProj", true);
    } 
    $('div.dhx_toolbar_btn').css("cursor", "pointer");
    toolbar.attachEvent("onStateChange", function(id, pressed){
        var clickPressedBar = false;
        var pressedBar = null;
        toolbar.forEachItem(function(itemId){
            if(itemId == id) {
                if(!pressed) {
                    clickPressedBar = true;
                    toolbar.setItemState(id, true);
                }
                return false;
            }else if(toolbar.getItemState(itemId)) {
                pressedBar = itemId;
            }
            return true;
        });
        if(!clickPressedBar) {
            var visibility = 0;
            if("pubProj" == id) {
                visibility = 1;
            }else if("priProj" == id) {
                visibility = 2;
            }
            toolbar.setItemState(id, true);
            toolbar.setItemState(pressedBar, false);
            window.location.href= contextPath + "/proj/projects?visibility=" + visibility;
        }
        return false;
    });
}

function updateChosen(elem, options) {
    var selected = elem.val();
    elem.empty();
    $.each(options, function(index, value){
        elem.append('<option value="'+value.id+'" '+(($.inArray('' + value.id, selected) != -1)? "selected" : "")+'>'+value.val+'</options>');
    });
    elem.trigger("liszt:updated");
}
function addUploadedItem(contextPath, fileUploadElem, fileData, multiUpload, ajaxDeleteFunc) {
    if($('ul.logoUploadSuccess', fileUploadElem).find('li span[uname="'+fileData.uname+'"]').length > 0) {
        return;
    }

    var uploadedHtml = $('<li><a class="deleteLogoBtn remove" href="javascript:void(0)"></a><span class="uploaded-logo" dname="'+fileData.dname + '" uname="'+fileData.uname+'">'+fileData.dname+' ('+countReadableSize(fileData.size)+')</span></li>');
    uploadedHtml.appendTo($('ul.logoUploadSuccess', fileUploadElem));
    if(!multiUpload) {
        $('.uploadControl', fileUploadElem).hide();
    }
    $('.deleteLogoBtn', fileUploadElem).click(function(){
        var $this = $(this);
        if($this.hasClass('view')){
            return;
        }
        $this.parents("li").remove();
        if(!multiUpload) {
            $('.uploadControl', fileUploadElem).show();
        }
        fixDHTMLXTabbarHeight("content", 40);
        if(ajaxDeleteFunc) {
            ajaxDeleteFunc();
        }
    });
    fixDHTMLXTabbarHeight("content", 40);
}
/**
 *
 * @param {jqObject} the field where the validation applies
 * @param {Array[String]} validation rules for this field
 * @param {int} rule index
 * @param {Map} form options
 * @return an error string if validation failed
 */
function validateProjectTime(field, rules, i, options){
    var sTimeStr = $('#startTime').val();
    var eTimeStr = $('#endTime').val();
    if(!sTimeStr.empty() && !eTimeStr.empty()) {
        var sTime = $.datepicker.parseDate("mm/dd/yy", sTimeStr);
        var eTime = $.datepicker.parseDate("mm/dd/yy", eTimeStr);
        if(sTime > eTime) {
            return (field.attr('id')=='startTime')?"'Start Time' MUST be before 'End Time'!":"'End Time' MUST be after 'Start Time'!";
        }
    }
    $('div.formError',$('#startTime').parents('dd')).remove();
    $('div.formError',$('#endTime').parents('dd')).remove();
}

function checkImage(fileElem) {
    var filename = fileElem.val();
    var errorBox = fileElem.parents('.uploadControl').find('.error-box');
    if(!filename.isImage()) {
        fileElem.val('');
        fileElem.parent().find('.filename').text('');
        errorBox.text($.i18n.message("cp.err.only_image_supported"));
        errorBox.show();
        return false;
    } else {
        errorBox.text('');
        errorBox.hide();
        return true;
    }
}

function fileUpload(contextPath, fileFieldId, elem, multiUpload, fileuse, success, ajaxDeleteFunc){
    var fileUpload = elem.parents("div.fileUpload");
    var uploaded;
    var fileElem;
    fileElem = fileUpload.find('input:file');
    if(fileElem.length <= 0 || fileElem.val().empty()) {
        ocsError($.i18n.message('cp.text.must_select_file'));
        return false;
    }
    if(!checkImage(fileElem)) {
        return false;
    }
    if(multiUpload) {
        uploaded = $('ul.logoUploadSuccess', fileUpload);
        fileElem = fileUpload.find('input:file');

        var duplicated = false;
        $('li span', uploaded).each(function(){
            if(fileElem.val() == $(this).attr('dname')) {
                duplicated = true;
                ocsConfirm($.i18n.message('cp.text.confirm_upload_dup_file', [fileElem.val()]), $.i18n.message('cp.title.confirm'), function(choice){
                    if(choice) {
                        handleFileUpload(contextPath, $(fileFieldId), multiUpload, fileuse, success, ajaxDeleteFunc);
                    }
                });
            }
        });
        if(duplicated){
            return false;
        }
    } else {
        uploaded = $('ul.logoUploadSuccess', fileUpload);
    }
    handleFileUpload(contextPath, $(fileFieldId), multiUpload, fileuse, success, ajaxDeleteFunc);
            
    return false;
}

function handleFileUpload(contextPath, elem, multiUpload, use, success) {
    var fileUpload = elem.parents("div.fileUpload");
    var loadImg = $(".uploading-img", fileUpload);
    elem.hide();
    loadImg.show();
    $.ajaxFileUpload({
        url: contextPath + '/file-upload',
        secureuri: false,
        dataType: 'json',
        data: {
            type: 'image',
            use: use
        },
        fileElementId: elem.attr('id'),
        success: function(resp, status, jqXHR) {    
            resetUniformFile($('input:file', fileUpload));
            addUploadedItem(contextPath, fileUpload, {
                uname: resp.data.uname, 
                dname: resp.data.dname, 
                size: countReadableSize(resp.data.size)
            }, multiUpload, function(){
                ajaxDeleteFunc(contextPath, parseInt($('#projId').val()));
            });
            if(success) {
                success(resp,status, jqXHR);
            }
            loadImg.hide();
            elem.show();
            return false;
        },
        error: function(data, status, e) {
            loadImg.hide();
            elem.show();
            defaultAjaxErrorHanlde(data, status, e);
            return false;
        }
    }); 
}

function handleUpdateProjLogo(contextPath, projId, success, error) {
    if(projId<=0) {
        return;
    } 
    var projectForm = $('#projectBaseTab');
    var projLogo =   $('ul.logoUploadSuccess span.uploaded-logo', $('#projLogo', projectForm).parents("div.fileUpload")).attr('uname');
    $.ajax({
        url: contextPath + '/proj/projects!updateProjLogo',
        type: 'POST',
        dataType: 'json',
        data: {
            projId: projId,
            projLogo:projLogo
        },
        success: function(data, textStatus, jqXHR) {
            if(success) {
                success(data, textStatus, jqXHR);
            }
        },
        error:  function(jqXHR, textStatus, errorThrown) {
            if(error) {
                error(jqXHR, textStatus, errorThrown);
            }
        }
    });
}

function handleUpdateSponsorLogo(contextPath, projId, success, error) {
    if(projId<=0) {
        return;
    }
    var projectForm = $('#projectBaseTab');
    var sponsorLogos = [];
    $('ul.logoUploadSuccess span.uploaded-logo', $('#sponsorLogo', projectForm).parents("div.fileUpload")).each(function(){
        sponsorLogos[sponsorLogos.length] = $(this).attr('uname');
    });
    $.ajax({
        url: contextPath + '/proj/projects!updateSponsorLogo',
        type: 'POST',
        dataType: 'json',
        data: {
            projId: projId,
            sponsorLogos:sponsorLogos
        },
        success: function(data, textStatus, jqXHR) {
            if(success) {
                success(data, textStatus, jqXHR);
            }
        },
        error:  function(jqXHR, textStatus, errorThrown) {
            if(error) {
                error(jqXHR, textStatus, errorThrown);
            }
        }
    });
}
/////////////////////////////////////////
//
function initProjectMembershipForm(formElem, data) {
    //var formElem = $('#formElem');
    $('#pmId', formElem).val(data.pmId);
    $('#projId', formElem).val(data.projId);
    
    $('select#user', formElem).empty();
    $('select#user', formElem).append($('<option value=""></opion>'));
    $.each(data.users, function(index, value){
        var option = $('<option value="'+value.id+'">'+value.firstName +' ' + value.lastName +'</opion>');
        if(data.userId == value.id) {
            option.attr('selected', 'selected');
        }
        $('select#user', formElem).append(option);
    });
    if(data.userId > 0) {
        $('select#user', formElem).attr('disabled', 'disabled');
    }
    
    $('#selectrole', formElem).empty();
    $('select#role', formElem).append($('<option value=""></opion>'));
    $.each(data.roles, function(index, value){
        var option = $('<option value="'+value.id+'">'+value.name +'</opion>');
        if(data.roleId == value.id) {
            option.attr('selected', 'selected');
        }
        $('select#role', formElem).append(option);
    });
    
    $("select", formElem).trigger("liszt:updated");
}

function doOpenProjectMembershipFormDlg(contextPath, formElem, projId, pmId, userId, roleId) {
    $('#contributorsFormPopup').dialog('option', 'title', (pmId> 0?'Edit':'Add') + ' Project User');
    $('#contributorsFormPopup').dialog('open');
    $.ajax({
        url: contextPath + '/proj/project-membership!getAllUsersAndRoles',
        type: 'POST', 
        dataType: 'json',
        data: {
            projId: projId, 
            userId: userId
        },
        success: function(resp){
            if(resp.ret != 0) {
                ocsError(resp.desc + '(ERROR=' + resp.ret+ ')');
            } else {
                resp.data.projId = projId;
                resp.data.pmId = pmId;
                resp.data.userId = userId;
                resp.data.roleId = roleId;
                initProjectMembershipForm(formElem, resp.data);
            }
        }
    });
    return false;
}


/////////////////////////////////////////
//
function initProductForm(formElem, data) {
    //var formElem = $('#formElem');
    $('#prodId', formElem).val(data.id);
    $('#projId', formElem).val(data.projId);
    $('#configId', formElem).val(data.configId);
    $('#workflowId', formElem).val(data.workflowId);
    
    $('#prodName', formElem).val(data.name);
    $('#description', formElem).val(data.description);
    checkUniformChoice($('input:radio[name=type][value='+data.type+']', formElem));
    checkUniformChoice($('input:radio[name=mode][value='+data.mode+']', formElem));
    selectChosenOption($('#prodActionRight', formElem), data.actionRight);
}

function resetProduct(form) {
    var prodElem = $('select[name=productId]', form);
    $('option', prodElem).each(function(){
        if($(this).val().empty()) {
            $(this).attr('selected', 'selected');
        } else{
            $(this).removeAttr('selected');
            $(this).hide();
        }
    });
    prodElem.trigger("liszt:updated");
}

function listProducts(form, type, addedProducts) {
    var prodElem = $('select[name=productId]', form);
    $('option', prodElem).each(function(){
        if($(this).val().empty()) {
            $(this).attr('selected', 'selected');
        } else{
            $(this).removeAttr('selected');
            if(type == $(this).attr('ct') || addedProducts.contains($(this).val())) {
                $(this).hide();
            } else {
                $(this).show();
            }
        }
    });
    prodElem.trigger("liszt:updated");
}
/////////////////////////////////////////
//
function initTriggerForm(formElem, data) {
    //var formElem = $('#formElem');
    $('#triggerId', formElem).val(data.id);
    $('#projId', formElem).val(data.projId);
    $('#configId', formElem).val(data.configId);
    $('#workflowId', formElem).val(data.workflowId);
    
    $('#triggerName', formElem).val(data.name);
    $('#description', formElem).val(data.description);
    checkUniformChoice($('input:radio[name=type][value='+data.type+']', formElem));
    checkUniformChoice($('input:radio[name=mode][value='+data.mode+']', formElem));
    selectChosenOption($('#triggerActionRight', formElem), data.actionRight);
}

function doOpenTriggerFormDlg(contextPath, formElem, triggerId) {
    if(!triggerId) {
        triggerId = -1;
    }
    $('#manageTriggerFormPopup').dialog('option', 'title', (triggerId> 0?'Edit':'Add') + ' Project Trigger');
    $('#manageTriggerFormPopup').dialog('open');
   
    if(triggerId <= 0) {
        return false;
    }
    $.ajax({
        url: contextPath + '/proj/triggeruct!get',
        type: 'POST', 
        dataType: 'json',
        data: {
            triggerId: triggerId
        },
        success: function(resp){
            if(resp.ret != 0) {
                ocsError(resp.desc + '(ERROR=' + resp.ret+ ')');
            } else {
                initTriggerForm(formElem, resp.data);
            }
        }
    });
    return false;
}

function doDeleteTrigger(contextPath, pmId) {
    ocsConfirm($.i18n.message("cp.text.confirm_delete_trigger"), $.i18n.message('cp.title.confirm'), function(choice){
        if(choice) {
            $.ajax({
                url: contextPath + '/proj/project-membership!delete?pmId='+pmId,
                type: 'POST', 
                dataType: 'json',
                success: function(resp){
                    $("#managerTriggers-flexgrid").flexReload({
                        dataType: 'json'
                    });
                }
            });
        }
    });
    return false;
}

function doAjaxChange(selectElem, actionUrl, successCallback) {
    var form = selectElem.parents('form');
    var formData = extractFormDataToJson(form);
    var result = $('<div id="result" class="hidden"><span></span></div>');
    result.appendTo(form);
    var textSpan = $('span', result);
    selectElem.change(function(data, eventObject){
        var action = 'na';
        var val = -1;
        if(eventObject.selected) {
            action = 'add';
            val = eventObject.selected;
        } else if(eventObject.deselected) {
            action = 'delete';
            val = eventObject.deselected;
        }
        var reqData = {
            action: action,
            value: val
        };
        var optionElem = $('option[value="'+val+'"]', selectElem);
        var optionTxt = optionElem.text();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: actionUrl,
            data: $.extend(reqData, formData),
            beforeSend: function() {
                result.attr('class','waiting');
                textSpan.text('Waiting...');
            },
            success: function(data){
                if(data.ret == 0) {
                    result.attr('class','success');
                    // textSpan.text('Success to '+action+' "' +optionTxt+ '"!');
                    textSpan.text('');
                    if(successCallback) {
                        successCallback();
                    }
                } else {
                    if(action == 'add') {
                        optionElem.removeAttr('selected');
                    } else {
                        optionElem.attr('selected', 'selected');
                    }
                    optionElem.trigger("liszt:updated");
                    result.attr('class','error');
                    textSpan.text('Fail to '+action+' "' +optionTxt+ '": ' + data.desc);
                }
                fixDHTMLXTabbarHeight("content");
                result.show();
            },
            error: function(jqXHR) {
                var redirectReason = jqXHR.getResponseHeader('Redirect-Reason');
                if(redirectReason && redirectReason=='no-auth') {
                    window.location.href='${contextPath}/login?expired=true';
                } else {
                    result.hide();
                    if(action == 'add') {
                        optionElem.removeAttr('selected');
                    } else {
                        optionElem.attr('selected', 'selected');
                    }
                    optionElem.trigger("liszt:updated");
                    ocsError($.i18n.message('cp.error.internal'));
                }
                fixDHTMLXTabbarHeight("content");
                return true;
            }
        });
        return false;
    });
}
