<%--
    Document   : Project Tab - 'scIndicators'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="scIndicatorsTab">
    <div>
        <div id="scIndicatorsFlexGrid" style="margin: 10px">
            <div id="scIndicators-flexgrid"></div>
        </div>
        <div id="indicatorFormPopup" title="<indaba:msg key='cp.title.add_scindicator'/>">
            <div id="indicatorFitlerBox" class="popup-container" style="width: 885px; max-width: 885px; margin: 0px; padding: 0px;">
                <div class="filter">
                    <form id="indicatorFilterForm" action="#" method="POST">
                        <fieldset id="indicatorFs">
                            <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.indicator_filter"  /></span></legend>
                            <input type="hidden" name="sconfId" id="sconfId" value="${sconfId}" />                            
                            <dl>
                                <dt><label for="name"><indaba:msg key='cp.label.name'/></label></dt>
                                <dd>
                                    <input type="text" name="name" class="middle-input"/>
                                </dd>
                            </dl>
                            <dl>
                                <dt><label for="question"><indaba:msg key='cp.label.question'/></label></dt>
                                <dd>
                                    <input type="text" name="question" class="middle-input"/>
                                </dd>
                            </dl>
                        </fieldset>
                    </form>
                </div>
                <div id="indicatorsFlexGrid" style="margin: 10px">
                    <div id="indicators-flexgrid"></div>
                    <div id="freeow-br" class="freeow freeow-top-right"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        //////////////////////////////////////////////////////////////////////////
        //
        // 'scIndicators' Flexgrid
        //
        //////////////////////////////////////////////////////////////////////////

        $("#scIndicators-flexgrid").flexigrid({
            // url: '${contextPath}/surveyconf/survey-config!findScIndicators?sconfId=${sconfId}',
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'indicatorId', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 134, sortable : true},
                {display: '<indaba:msg key="cp.ch.question"/>', name : 'question', width : 445, sortable : true},
                {display: '<indaba:msg key="cp.ch.organization"/>', name : 'orgName', width : 160, sortable : true},
                {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 100, sortable : false}
            ],
            preProcess: function(data) {
                var isOwner = (data.owned == 'yes');
                if (!isOwner) {
                    $('.addIndicator').parent().parent().hide();
                }

                $.each(data.rows, function(index, elem){
                    var indicatorId = elem.indicatorId;
                    var actions = '';
                    if(isOwner && !elem.used) {
                        actions += '<a class="link" href="javascript:void(0)" onclick="return doDeleteGroupIndicators(\'${contextPath}\',$(\'#scIndicators-flexgrid\'),${sconfId},'+indicatorId+');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"  /></a>';
                    }
                    elem.actions = actions;
                });
                return data;
            },
            buttons : [
                {name: '<indaba:msg key="cp.btn.add"  />', bclass: 'addIndicator', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        openIndicatorDlg();
                        return false;
                    }
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "NAME",
            sortorder: "asc",
            usepager: true,
            title: 'Indicators',
            useRp: true,
            rp: 100,
            rpOptions: [30, 50, 100, 200],
            showTableToggleBtn: false,
            showToggleBtn: false,
            width: 865,
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
        
        var indicatorFilterForm = $('#indicatorFilterForm');
        resetForm(indicatorFilterForm);
        $('#indicatorFormPopup').dialog({
            autoOpen: false,
            width: 918,
            resizable: false,
            //maxHeight: $(window).height(),
            modal: true,
            open: function(){
                $("#indicators-flexgrid").flexOptions({url: '${contextPath}/surveyconf/survey-config!findAvailableIndicators?sconfId=${sconfId}'}).flexReload({newp: 1,dataType: 'json'});
            },
            beforeClose: function(){
                clearFormFields(indicatorFilterForm);
                loadScIndicatorsFlexGrid();
            },
            buttons: {
                "Close": function() {
                    clearFormFields(indicatorFilterForm);
                    loadScIndicatorsFlexGrid();
                    $(this).dialog("close");
                }
            }
        });
        $('select, input', $('#indicatorFilterForm')).change(function(){
            $("#indicators-flexgrid").flexReload({newp: 1,dataType: 'json'});
        });
        $("#indicators-flexgrid").flexigrid({
            // url: '${contextPath}/surveyconf/survey-config!findAvailableIndicators?sconfId=${sconfId}',
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'indicatorId', width : 0, sortable : true, hide: true},
                //{display: '', name : 'ck', width : 20, sortable : true},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 128, sortable : true},
                {display: '<indaba:msg key="cp.ch.question"/>', name : 'question', width : 567, sortable : true},
                {display: '<indaba:msg key="cp.ch.organization"/>', name : 'orgName', width : 150, sortable : true}//,
                //{display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 75, sortable : false}
            ],
            preProcess: function(data) {
                var isSA = (data.isSysAdmin == 'yes');

                $.each(data.rows, function(index, elem){
                    //elem.ck = '<input type="checkbox" name="apply" value="'+elem.id+'"/>';
                    // Edit
                    //var actions = '';
                    // actions = '<a class="link" href="javascript:void(0)" onclick="return redirectToIndicatorPage(${projId},'+prodId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"  /></a>';
                    //elem.actions = actions;
                });

                // reset to 'select all'
                var elem = $('.select');
                elem.empty();
                elem.append('<img src="${contextPath}/resources/images/uncheck.png">' + $.i18n.message('cp.btn.select_all'));
                return data;
            },
            buttons : [
                {name: '<indaba:msg key="cp.btn.add_selected"  />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doAddGroupIndicators('${contextPath}', $("#indicators-flexgrid"), ${sconfId});
                        return false;
                    }
                },
                {separator: true},
                {name: '<indaba:msg key="cp.btn.select_all"/>', bclass: 'select', bimage: '${contextPath}/resources/images/uncheck.png', onpress : function(){
                        return doSelectIndicators('${contextPath}');}
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "NAME",
            sortorder: "asc",
            usepager: true,
            title: 'Indicators',
            useRp: true,
            rp: 50,
            rpOptions: [30, 50, 100, 200, 500],
            showTableToggleBtn: false,
            showToggleBtn: false,
            width: 865,
            height: '100%',
            //page: 1,
            pagestat: 'Displaying {from} to {to} of {total} items',
            onSuccess: function(){
                //$("#indicators-flexgrid tbody tr td[abbr=ck] input").uniform();
            },
            onSubmit: function(){
                var p = [];
                $('select, input', $('#indicatorFilterForm')).each(function(){
                    p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                });
                this.params = p;
                return true;
            }
        });
    });
    function openIndicatorDlg() {
        $('#indicatorFormPopup').dialog('open');
    }
    function loadScIndicatorsFlexGrid() {
        $("#scIndicators-flexgrid").flexOptions({url: '${contextPath}/surveyconf/survey-config!findScIndicators?sconfId=${sconfId}'}).flexReload({newp: 1,dataType: 'json'});
    }
</script>