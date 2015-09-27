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
            } else if(toolbar.getItemState(itemId)) {
                pressedBar = itemId;
            }
            return true;
        });
        if(!clickPressedBar) {
            var visibility = 0;
            if("pubProj" == id) {
                visibility = 1;
            } else if("priProj" == id) {
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
/////////////////////////////////////////
//
function initProductForm(form, product) {
    $('#projId', form).val(product.projectId);
    $('#prodId', form).val(product.id);
    $('#prodName', form).val(product.name);
    $('#description', form).val(product.description);
    checkUniformChoice($('input:radio[name=contentType][value='+product.contentType+']', form));
    selectChosenOption($('#contentConfig', form), product.productConfigId);
    selectChosenOption($('#workflow', form), product.workflowId);
    checkUniformChoice($('input:radio[name=mode][value='+product.mode+']', form));
}

function doOpenProductFormDlg(contextPath, formElem, projId, prodId) {
    if(!prodId) {
        prodId = -1;
    }
    $('#productFormPopup').dialog('option', 'title', (prodId> 0?'Edit':'Add') + ' Project Product');
    $('#productFormPopup').dialog('open');
   
    if(prodId <= 0) {
        return false;
    }
    $.ajax({
        url: contextPath + '/proj/product!get',
        type: 'POST', 
        dataType: 'json',
        data: {
            prodId: prodId
        },
        success: function(resp){
            if(resp.ret != 0) {
                ocsError(resp.desc + '(ERROR=' + resp.ret+ ')');
            } else {
                resp.data.projId = projId;
                initProductForm(formElem, resp.data);
            }
        }
    });
    return false;
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

