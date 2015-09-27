<%--
    Document   : Project Tab - 'Products'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="productsTab">
    <div id="productsFlexGrid" style="margin: 10px">
        <div id="products-flexgrid"></div>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        //////////////////////////////////////////////////////////////////////////
        //
        // 'Products' Flexgrid
        //
        //////////////////////////////////////////////////////////////////////////
        var productForm = $('#productForm');
        //$('input, textarea', productForm).uniform();
        //$("select",productForm).chosen();
        //$('a.ui-btn, button').button();
        //productForm.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
        // Dialog
        $('#productFormPopup').dialog({
            autoOpen: false,
            width: 622,
            resizable: false,
            //maxHeight: $(window).height(),
            modal: true,
            open: function(){
                resetProduct(productForm);
                $('input[name=type]', productForm).change(function(){
                    var type = $(this).val();
                    var addedProducts = [];
                    $('td:first div', $("#products-flexgrid tbody tr")).each(function(){
                        var s = $(this).text();
                        if(!s.empty()) {
                            addedProducts[addedProducts.length] = $(this).text().trim();
                        }
                    });
                    listProducts(productForm, type, addedProducts);
                });
            },
            beforeClose: function(){
                clearFormFields(productForm);
                productForm.validationEngine('hideAll');
                resetProduct(productForm);
            },
            buttons: {
                "Save": function() {
                    if(!productForm.validationEngine('validate')) {
                        return false;
                    }
                    var reqData = extractFormDataToJson(productForm);
                    var dlg = $(this);
                    $.ajax({
                        url:'${contextPath}/proj/product!save',type: 'POST',dataType: 'json',data: reqData,_loading: {show:true,id:"projectTabs"},
                        success: function(data){
                            if(data.ret != 0) {
                                ocsError(data.desc);
                            } else {
                                clearFormFields(productForm);
                                dlg.dialog("close");
                                loadProductsFlexGrid();
                                ocsSuccess($.i18n.message('cp.text.success_save_product'), $.i18n.message('cp.title.success'));
                            }
                        },
                        error: function(data) {
                            defaultAjaxErrorHanlde(data);
                        }
                    });
                    return false;
                },
                "Cancel": function() {
                    clearFormFields(productForm);
                    $(this).dialog("close");
                }
            }
        });
        $("#products-flexgrid").flexigrid({
            // url: '${contextPath}/prod/product!find?projId=${projId}',
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'id', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 228, sortable : true},
                {display: '<indaba:msg key="cp.ch.type"/>', name : 'contentType', width : 70, sortable : true},
                {display: '<indaba:msg key="cp.ch.config_name"/>', name : 'configName', width : 160, sortable : true},
                {display: '<indaba:msg key="cp.ch.mode"/>', name : 'mode', width : 70, sortable : true},
                {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 275, sortable : false}
            ],
            preProcess: function(data) {
                var isSA = (data.isSysAdmin == 'yes');

                $.each(data.rows, function(index, elem){
                    var prodId = elem.id;
                    var prodName = elem.name;
                    switch(elem.contentType) {
                        case 0:
                            elem.contentType = 'SURVEY';
                            break;
                        case 1:
                            elem.contentType = 'JOURNAL';
                            break;
                        case 2:
                        default:
                            elem.contentType = 'UNKNOWN';
                            break;
                    }
                    
                    // Edit
                    var actions = '';

                    actions = '<a class="link" href="javascript:void(0)" onclick="return redirectToProductPage(${projId},'+prodId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"  /></a>';

                    // Delete
                    actions += '<a class="link" href="javascript:void(0)" onclick="return doDeleteProduct(' + prodId +',\''+ elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"  /></a>';
                    // Clone
                    actions += '<a class="link" href="javascript:void(0)" onclick="return doCloneProduct('+prodId+',\''+prodName+'\');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.clone"  /></a>';

                    switch(elem.mode) {
                        case 1:
                            elem.mode = $.i18n.message('cp.label.mode_config');
                            // Test
                            actions += '<a class="link" href="javascript:void(0)" onclick="return doApplyMode(\'test\','+prodId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.test"  /></a>';
                            break;

                        case 2:
                            elem.mode = $.i18n.message('cp.label.mode_test');                            
                            actions += '<a class="link" href="javascript:void(0)" onclick="return doApplyMode(\'launch\','+prodId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.launch"  /></a>';
                            // Configure
                            actions += '<a class="link" href="javascript:void(0)" onclick="return doApplyMode(\'config\','+prodId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.config"  /></a>';
                            break;
                            
                        case 3:
                            elem.mode = $.i18n.message('cp.label.mode_launched');;
                            // Test
                            actions += '<a class="link" href="javascript:void(0)" onclick="return doApplyMode(\'test\','+prodId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.test"  /></a>';
                            // Configure
                            actions += '<a class="link" href="javascript:void(0)" onclick="return doApplyMode(\'config\','+prodId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.config"  /></a>';
                            break;

                        default:
                            elem.mode = 'UNKNOWN';
                            break;
                        }
                        elem.actions = actions;
                    });
                    return data;
                },
                buttons : [
                    {name: '<indaba:msg key="cp.btn.add"  />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                            //openProductDlg(${projId}, -1);
                            redirectToProductPage(${projId});
                            return false;
                        }
                    }
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "PRODNAME",
                sortorder: "asc",
                usepager: true,
                title: 'Products',
                useRp: true,
                rp: 15,
                showTableToggleBtn: false,
                showToggleBtn: false,
                width: 865,
                height: '100%',
                //page: 1,
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
                    $('select, input', $('#project-filter-form')).each(function(){
                        p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                    });
                    this.params = p;
                    return true;
                }
            });
        });
        function redirectToProductPage(projId, prodId) {
            window.location.href='${contextPath}/prod/product?visibility=${visibility}&projId='+projId+(prodId?('&prodId=' + prodId):'');
            return false;
        }
        function openProductDlg(projId, prodId) {
            var disabled;
            if(prodId > 0) {
                disabled = false;
            } else {
                disabled = [1,2,3];
            }
            $("#productTabs" ).tabs({ selected: 0, disabled:disabled});
            initProductForms(projId, prodId);
        }
        function doDeleteProduct(prodId, prodName) {
            ocsConfirm($.i18n.message('cp.text.confirm_delete_product', [prodName]), $.i18n.message('cp.title.confirm'), function(confirmed){
                if(confirmed) {
                    $.ajax({ type: 'POST', dataType: 'json', url: '${contextPath}/prod/product!delete', data: {prodId: prodId},
                        success: function(resp){
                            if(resp.ret == 0) {
                                loadProductsFlexGrid();
                            } else {
                                ocsError(resp.desc);
                            }
                        }
                    });
                }
            });
            return false;
        }
        function doCloneProduct(prodId, prodName) {
            var ocsDlg = $('<div id="cloneProdDlg" class="dlg hidden"><div id="cloneProdBox" class="popup-container"><h3 style="margin: 10px;">'+$.i18n.message('cp.text.specify_clone_product')+'</h3></div></div>');
            var form = $( '<form name="cloneProd" action="#" method="POST" style="margin-top: 10px;" onsubmit="return false;"><fieldset class="block" style="margin-right: 15px;"><input id="prodId" name="prodId" type="hidden" value="-1"/>'+
                '<dl><dt><label for="prodName">'+$.i18n.message('cp.label.product_name')+'</label></dt><dd><input id="prodName" name="prodName" title="Product name" type="text" class="middle-input validate[required,ajax[ajaxValidateProductExists]]"/></dd></dl>' +
                '</fieldset></form>');
            $('#cloneProdBox', ocsDlg).append(form);
            $('input:text', form).uniform();
            form.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
            ocsDlg.dialog({
                width: 622,
                title: $.i18n.message('cp.title.clone_indicator'),
                resizable: false,
                modal: true,
                open: function() {
                    $('div.ui-dialog').css('overflow', 'visible');
                },
                close: function() {
                    ocsDlg.parents('div.ui-dialog').remove();
                    ocsDlg.remove();
                },
                buttons: {
                    "OK": function() {
                        if(!form.validationEngine('validate')) {
                            return false;
                        }
                        var reqData = extractFormDataToJson(form);
                        reqData.prodId = prodId;
                        $.ajax({type: 'POST',dataType: 'json',url:  '${contextPath}/prod/product!clone', data:reqData,
                            success: function(resp){
                                if(resp.ret == 0) {
                                    loadProductsFlexGrid();
                                    ocsSuccess($.i18n.message('cp.text.success_clone_prod', [reqData.prodName, prodName]));
                                    ocsDlg.dialog("close");
                                } else {
                                    ocsError(resp.desc);
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
            return false;
            return false;
        }
        /**
         * mode: test, launch, config
         */
        function doApplyMode(mode, prodId) {

            var msg;
            var modeText;

            switch(mode) {
                case 'test':
                    msg = $.i18n.message('cp.text.confirm_test_prod');
                    modeText = $.i18n.message('cp.label.mode_test');
                    break;
                case 'config':
                    msg = $.i18n.message('cp.text.confirm_config_prod');
                    modeText = $.i18n.message('cp.label.mode_config');
                   break;
                default:
                    msg = $.i18n.message('cp.text.confirm_launch_prod');
                    modeText = $.i18n.message('cp.label.mode_launched');
                    break;
            }
            
            ocsConfirm(msg, $.i18n.message('cp.title.confirm'), function(confirmed){
                if (confirmed) {
                  $.ajax({ type: 'POST', dataType: 'json', url: '${contextPath}/prod/product!applyMode', data: {mode: mode, prodId: prodId},
                    success: function(resp){
                        if(resp.ret == 0) {
                            ocsSuccess($.i18n.message('cp.text.success_apply_product', [modeText]));
                            loadProductsFlexGrid();
                        } else {
                            ocsError(resp.desc);
                        }
                    }
                 });
              }
            });

            return false;
        }

        function loadProductsFlexGrid() {
            $("#products-flexgrid").flexOptions({url: '${contextPath}/prod/product!find?projId=${projId}'}).flexReload({newp: 1,dataType: 'json'});
        }
</script>