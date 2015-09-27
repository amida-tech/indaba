<%-- 
    Document   : survey-question-form-inc
    Created on : 2012-11-27, 11:59:18
    Author     : Administrator
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="qstnFormBox" class="inlineBox hidden">
    <div id="qstnFormInnerBox" class="inner">
        <form id="qstnForm" action="#" method="POST">
            <input type="hidden" name="nodeTId"/>
            <input type="hidden" name="action" id="action"/>
            <input type="hidden" name="qstnId" id="qstnId"/>
            <input type="hidden" name="sconfId" id="sconfId"/>
            <input type="hidden" name="pCatId"/>
            <input type="hidden" name="weight"/>
            <fieldset id="qstnFormFs" class="block">
                <dl>
                    <dt><label for="label"><indaba:msg key="cp.label.label"/></label></dt>
                    <dd><input type="text" name="label" maxlength="45" id="label" class="middle-input validate[required]"/></dd>
                </dl>                
                <dl>
                    <dt><label for="indicatorId"><indaba:msg key="cp.label.question"  /></label></dt>
                    <dd>
                        <select name="indicatorId" id="indicatorId" data-placeholder="<indaba:msg key='cp.text.choose_question'/>" class="large-input validate[required,ajax[ajaxValidateSurveyQuestionExists]]">
                            <option value=""></option>
                        </select>
                        <div style="padding-top: 5px; color: #666;"><indaba:msg key="cp.text.hint_add_indicators"/></div>
                    </dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd>
                        <div class="btn-icons">
                            <a class="btn save">Save</a>
                            <a class="btn cancel">Cancel</a>
                        </div>
                    </dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var sqFormInitialized = false;
    $(function(){
        if(!sqFormInitialized) {
            sqFormInitialized = true;
            $('#qstnForm input[name=library]').change(function(){
                var lib = parseInt($(this).val());
                var indicatorElem = $('#qstnForm select[name=indicatorId]');
                var indicatorVal = indicatorElem.val();
                if(lib == 0) {
                    indicatorElem.find('option').removeAttr('disabled');
                } else {
                    indicatorElem.find('option').attr('disabled', 'disabled');
                    indicatorElem.find('option[state='+lib+']').removeAttr('disabled');
                    indicatorElem.find('option').removeAttr('selected');
                    if(indicatorElem.find('option[value='+indicatorVal+'][state='+lib+']').length>0){
                        indicatorElem.find('option[value='+indicatorVal+'][state='+lib+']').attr('selected','selected');
                    } else{
                        indicatorElem.find('option:first').removeAttr('disabled');
                        indicatorElem.find('option:first').attr('selected','selected');
                    }
                }
                indicatorElem.trigger("liszt:updated");
            });
        }
    });
</script>