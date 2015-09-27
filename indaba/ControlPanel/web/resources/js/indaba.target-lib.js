
function initTargetLibToolbar(visibility, contextPath){
    var targetLibToolbarData = {
        parent: "targetLibToolbar",
        icon_path: contextPath + "/resources/images/",
        items: [
        {
            type: "buttonTwoState", 
            id: "pubTargtLib", 
            text: $.i18n.message('cp.tab2.target.pub_lib'),
            img: "pub_ext_lib.png"
        },
        {
            type: "separator", 
            id: "sep1"
        },
        {
            type: "buttonTwoState", 
            id: "priTargetLib",
            text: $.i18n.message('cp.tab2.target.priv_lib'),
            img: "private_lib.png"
        }
        ]
    };
    var toolbar = new dhtmlXToolbarObject(targetLibToolbarData, "dhx_blue");
                    
    fixDHTMLXTabbarHeight("content");
    
    if(visibility == '1') {
        toolbar.setItemState("pubTargtLib", true);
    } else if(visibility == '2') {
        toolbar.setItemState("priTargetLib", true);
    }else{
        toolbar.setItemState("pubTargtLib", true);
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
            if("pubTargtLib" == id) {
                visibility = 1;
            } else if("priTargetLib" == id) {
                visibility = 2;
            }
            toolbar.setItemState(id, true);
            toolbar.setItemState(pressedBar, false);
            window.location.href= contextPath + "/lib/target-lib?visibility=" + visibility;
        }
        return false;
    });
}

function doEditTarget(contextPath, id, viewonly){
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: contextPath + '/lib/target-lib!get',
        data: {
            id:id
        },
        success: function(resp){
            if(resp.ret == 0) {
                if (!resp.data.editable) viewonly = true;
                
                initTargetForm(resp.data, viewonly);

                if (viewonly) {
                    $('#targetFormPopup').dialog('option', 'title', $.i18n.message('cp.title.view_target'));
                } else {
                    $('#targetFormPopup').dialog('option', 'title', $.i18n.message('cp.title.edit_target'));
                }
                $('#targetFormPopup').dialog('open');
                return false;
            }
        }
    });
    
                        
    return false;
}

function initTargetForm(data, viewonly) {
    var p = $('#targetForm');
    $('input[name=id]', p).val(data.id);
    $('input:text[name=name]', p).val(data.name);
    $('input:text[name=shortName]', p).val(data.shortName);
    $('input:text[name=orgname]', p).val(data.orgname);
    $('input:text[name=guid]', p).val(data.guid);
    $('textarea[name=description]', p).val(data.description);

    selectChosenOption($('select[name=language]', p), data.languageId);

    if (viewonly) {
        // Done by Jeff: show the "orgname" element"
        // Hide the organization select
        // IMPLEMENTED BY setViewMode(...)
    } else {
        selectChosenOption($('select[name=organization]', p), data.ownerOrgId);
        // Done by Jeff: hide the "orgname" element
        // IMPLEMENTED BY setViewMode(...)
    }
   setViewMode(viewonly);

    selectChosenOption($('select[name=targetType]', p), data.targetType);
    if(data.tagIds) {
        for(var i = 0; i < data.tagIds.length; ++i) {
            selectChosenOption($('select[name=tags]', p), data.tagIds[i]);
        }
    }
}
function resetTargetForm(elem) {
    $('select option', elem).removeAttr('selected');
    $('input, select, button, textarea', elem).removeAttr("disabled");
    $('select', elem).trigger("liszt:updated");
    
    var p = $('#targetForm');
    $('input[name=id]', p).val(-1);

    // clear the target name and short name
    $('input:text[name=name]', p).val('');
    $('input:text[name=shortName]', p).val('');

}

function setViewMode(viewonly) {
    var p = $('#targetForm');
    if(viewonly) {
        $('dl#orgname-item', p).show();
        $('dl#org-item', p).hide();
        $('input, select, button, textarea', p).attr("disabled","disabled");
        $('input, select, button, textarea', p).css("background-color","#fafafa");
    } else {
        $('dl#orgname-item', p).hide();
        $('dl#org-item', p).show();
        $('input, select, button, textarea', p).removeAttr("disabled");
        $('input, select, button, textarea', p).css("background-color","#fff");
    }
    $('select').trigger("liszt:updated");
}