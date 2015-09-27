
String.prototype.empty = function() {
    return (this==null || this == "" || this.length == 0 ) ? true:false;
}

String.prototype.endsWith = function(str) {
    return (this.match(str+"$")==str);
}

Array.prototype.remove = function(s) {
    var i = this.indexOf(s);
    if(i != -1) this.splice(i, 1);
}

Array.prototype.S=String.fromCharCode(2);
/*
Array.prototype.contain=function(e)
{
    var r=new RegExp(this.S+e+this.S);
    return (r.test(this.S+this.join(this.S)+this.S));
}
 */
Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
}
String.prototype.checkTime = function() {
    var regex = new RegExp("^(0\\d|1[0-2]):[0-5]\\d (AM|PM)$", 'i');
    return regex.test(this);
}

String.prototype.trim = function()
{
    return this.replace(/(^[\\s]*)|([\\s]*$)/g, "");
}
String.prototype.lTrim = function()
{
    return this.replace(/(^[\\s]*)/g, "");
}
String.prototype.rTrim = function()
{
    return this.replace(/([\\s]*$)/g, "");
}
String.prototype.extension = function()
{
    return this.split('.').pop();
}
Array.prototype.remove = function(removeItem) 
{
    return $.grep(this, function(value) {
        return value != removeItem;
    });
}
String.prototype.isImage = function()
{
    return(this.match(/\.(jpeg|jpg|gif|png|bmp)$/i) != null);
}
function countReadableSize(size) {
    var displaySize = '';
    var v = 0;
    if((v = Math.floor(size / (1024*1024*1024))) > 0) {
        displaySize = v + 'G';
    } else if((v = Math.floor(size / (1024*1024))) > 0) {
        displaySize = v + 'M';
    } else if ((v = Math.floor(size / 1024)) > 0) {
        displaySize = v + 'K';
    } else {
        displaySize = size + 'B';
    }
    return displaySize;
}
/**
 * DHTMLX tabbar has a minor issue(cannot automatically auto resize dynamic tab 
 * content with ajax loading). So, this function can fix DHTMLX's auto resize issue.
 * 
 * contentBoxId - the parent box id of tab.
 * paddingHeight - padding height. By default, $jQuery.height() can only compute 
 * the height of the block itself, and cannot get padding/margin's height. This parameter
 * is used to specify all sub elements' the padding or margin's height.
 * width - tab's width.
 */
function fixDHTMLXTabbarHeight(contentBoxId, paddingHeight, width) {
    var id = "content";
    var pH = 0;
    var w = 952;
    if(typeof paddingHeight != "undefined" && paddingHeight > 0) {
        pH = paddingHeight;
    }
    if(typeof width != "undefined" && width > 0) {
        w = width;
    }
    if(typeof contentBoxId != "undefined" && !contentBoxId.empty()) {
        id = contentBoxId;
    }
    $('.dhx_tabbar_zone').css('height', '100%');
    tabbar.setSize(w, (40 + $('#'+id).innerHeight()) + pH);
}

function clearFormFields(formElem, clearHidden) {
    if(clearHidden) {
        $('input[type=hidden]', formElem).val('');
    }
    clearUniformFields(formElem);
}

function clearUniformFields(formElem) {
    $('input, select, button, textarea', formElem).removeAttr("disabled");
    $('input:radio, input:checkbox', formElem).each(function(){
        $(this).parent('span').removeClass('checked');
        $(this).removeAttr('checked');
    });
    $('select', formElem).each(function(){
        resetUniformSelect($(this));
        $(this).find('option').removeAttr('selected');
    });
    $('input[type=text], textarea', formElem).val('');
}

function extractFormDataToJson(formElem) {
    var data = {};
    $('input:text, input:checkbox:checked, input:hidden, input:radio:checked, textarea, select', formElem).each(function(){
        var name = $(this).attr('name');
        if(typeof name != 'undefined' && name != '' && name != 'undefined') {
            data[name] = $(this).val();
        }
    });
    return data;
}
                

/**
 * Clean uniform select
 */
function resetUniformSelect(elem) {
    var selected = elem.find("option:first");
    var p = elem.parent('div.selector');
    p.children('span').text(selected.html());
}

function resetUniformFile(elem) {
    var newFileHtml = $('<input type="file" />');
    if(elem.attr('id')) {
        newFileHtml.attr('id', elem.attr('id'));
    }
    if(elem.attr('name')) {
        newFileHtml.attr('name', elem.attr('name'));
    }
    if(elem.attr('size')) {
        newFileHtml.attr('size', elem.attr('size'));
    }
    var $parent = elem.parent();
    var children = $parent.parent().children();
    if(children.length == 0) {
        newFileHtml.appendTo($parent.parent());
    } else {
        newFileHtml.insertBefore(children[0]);
    }
    newFileHtml.uniform();
    $parent.remove();
}

function selectChosenOption(elem, v, extra) {
    var extraSelector = '';
    if(extra) {
        $.each(extra, function(index, val){
            extraSelector += '[' + val.k + '=' + val.v+']';
        });
    }
    var option = elem.find("option[value="+ v +"]" + extraSelector);
    option.attr('selected', 'selected').trigger("liszt:updated");
}

function selectChosenOptions(elem, values) {
    if(values) {
        $.each(values, function(index, val){
            var option = elem.find("option[value="+ val +"]");
            option.attr('selected', 'selected')
        });
    }
    elem.trigger("liszt:updated");
}

function disableChosenOption(elem, v) {
    var option = elem.find("option[value="+ v +"]");
    option.attr('disabled', 'disabled').trigger("liszt:updated");
}

function selectUniformOption(elem, v) {
    var selected = elem.find("option[value="+ v +"]");
    var p = elem.parent('div.selector');
    p.children('span').text(selected.html());
    elem.val(v);
}

function checkUniformChoice(elem) {
    elem.parent().addClass('checked');
    elem.attr('checked', true);
}

function uncheckUniformChoice(elem) {
    elem.parent().removeClass('checked');
    elem.removeAttr('checked');
}

function checkUniformChoiceV2(formElem, elemName, val) {
    $('input:radio[name='+elemName+']', formElem).parent().removeClass('checked');
    $('input:radio[name='+elemName+'][value='+val+']', formElem).parent().addClass('checked');
    $('input:radio[name='+elemName+'][value='+val+']', formElem).attr('checked', true);
}

function updateSelectField(elem, values) {
    $.each(values, function(index, value){
        $('option[value="'+value+'"]', elem).attr('selected', 'selected');
    });
    elem.trigger("liszt:updated");
}

function removeUniformField(elem) {
    elem.parent().remove();
}

function validateRequiredFields(p) {
    var valid = true;
    var errTxt = '';
    var errElm = null;
    $('.error-box').each(function(){
        $(this).parent().remove();
    });
    
    $('input[required]:text, input[required]:radio, textarea[required], select[required]', p).each(function(){
        var tag = this.nodeName.toLowerCase();
        if(tag == 'input') {
            if($(this).attr('type') == 'text' && $(this).val().empty()) {
                errTxt = ('Please complete "'+$(this).attr('title')+'" field!');
                errElm = $(this);
                return (valid=false);
            } else if ($(this).attr('type') == 'radio' && typeof $('input[name=answerType]:checked').val() == 'undefined'){
                errTxt = ('Please choose an "'+$(this).attr('title')+'"!');
                errElm = $(this);
                return (valid=false);
                        
            }
        } else if(tag == 'textarea' && $(this).val().empty()) {
            errTxt = ('Please complete "'+$(this).attr('title')+'" field!');
            errElm = $(this);
            return (valid=false);
        } else if(tag == 'select' && (typeof $(this).val() === "undefined") || $(this).val().empty() || $(this).val() == 0) {
            errTxt = ('Please complete "'+$(this).attr('title')+'" field!');
            errElm = $(this);
            return (valid=false); 
        }
    });
    if(!valid) {
        errElm.focus();
        errElm.parents('div.row').after('<div class="row"><div style="width: 262px;margin-left:100px;" class="error-box"><span class="err-txt">'+errTxt+'</span></div></div>');
    }
    return valid;
}

function defaultAjaxErrorHanlde(jqXHR, textStatus, errorThrown) {
    var redirectReason = jqXHR.getResponseHeader('Redirect-Reason');
    if(redirectReason && redirectReason=='no-auth') {
        window.location.href='${contextPath}/login?expired=true';
        return false;
    }
    ocsError($.i18n.message('cp.err.server_error'));
    return false;
}

function initFormButtons(param) {
    var btnGroup = $('<dl class="form-buttons"></dl>');
    btnGroup.append('<dt></dt>');
    var list = $('<dd></dd>');
    if(param.buttons) {
        $.each(param.buttons, function(index, elem){
            var btn = $('<a id="'+elem.id+'" class="btn" href="javascript:void(0)" '+(elem.style?'style="'+elem.style+'"':'')+'>'+elem.btnTxt+'</a>');
            if(elem.style) {
                btn.attr('style', elem.style);
            }
            if(elem.onClick) {
                btn.click(function(obj){
                    return elem.onClick(obj);
                });
            }
            list.append(btn);
        });
    }
    btnGroup.append(list);
    btnGroup.insertAfter($('fieldset', param.formElem));
}

function resetForm(formElem) {
    clearFormFields(formElem);
    $('input:text, input:file, textarea, input:radio, input:checkbox', formElem).uniform();
    $("select", formElem).val('').chosen().trigger("liszt:updated");
    $('input.datepicker', formElem).datepicker();
    $('button', formElem).button();
    $('form', formElem).validationEngine({
        prettySelect : true,
        useSuffix: "_chzn",
        validationEventTrigger:"change"
    });
}
/**
 * Set the options of the list with the specified options
 *  selectElem - the select node
 *  options - key-value pairs
 *  keys - id: value="option[id]", text: "option[attr[0]] option[attr[1]] ...", sep: separator
 *  args - extra arguments
 *  append - if appended?
 */
function setOptions(selectElem, options, keys, args, append, noDefaultEmptyOption) {
    if(!append) {
        selectElem.empty();
        if(!noDefaultEmptyOption) {
            selectElem.append('<option value=""></option>');
        }
    }

   if(options) {
        $.each(options, function(index, elem){
            var extra = '';
            if(args) {
                $.each(args, function(k, v){
                    if(k == '_attrs') {
                        $.each(v, function(kk, vv){
                            extra += vv + '="' + elem[vv]+ '" ';
                        });
                    } else{
                        extra += k + '="' + v+ '" ';
                    }
                });
            }
            var id = '';
            var val = '';
            if(keys) {
                id = (keys.id)?elem[keys.id]:elem.id;
                var sep = (keys.sep)?keys.sep: ' ';
                $.each(keys.attrs, function(index, v){
                    val += elem[v] + sep;
                });
            } else {
                id = elem.id;
                val = elem.name;
            }
            // selectElem.append('<option value="'+id+'" '+extra+'>'+val+'</option>').trigger("liszt:updated");
            selectElem.append('<option value="'+id+'" '+extra+'>'+val+'</option>');
        })
    }

    selectElem.trigger("liszt:updated");
}

function isValidEmailAddress(email) {
    var pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
    return pattern.test(email);
}