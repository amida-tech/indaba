<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div id="send-question-popup" style="display:none">
    <div id="send-box">
        <div style="text-align: center; background-color:steelblue; font-size: 16px; padding: 5px; color: white">
            <b><indaba:msg key='common.msg.sendQuestions' /></b>
        </div>
        <fieldset style="margin: 5px 0; background-color: #f6f6f6; border: 1px solid #ccc;">
            <legend style="margin-left: 20px;padding: 0px 5px;"><img width="16px" src="images/search.png"><span><indaba:msg key="common.msg.respondentFilter" /></span></legend>
            <dl>
                <dt><label for="roleId"><indaba:msg key="common.label.role"/></label></dt>
                <dd>
                    <select name="roleId" id="roleId" style="width: 200px; height: 15px;" data-placeholder="<indaba:msg key='common.msg.chooseReole'/>">
                        <option value="-1" selected><indaba:msg key="common.choice.all" /></option>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label><indaba:msg key="common.msg.target" /></label></dt>
                <dd>
                    <select name="targetId" id="targetId" style="width: 200px; height: 15px;" data-placeholder="<indaba:msg key='common.msg.chooseTarget'/>" >
                        <option value="-1"><indaba:msg key="common.choice.all" /></option>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label><indaba:msg key="common.label.username" /></label></dt>
                <dd><input type="text" name="username" style="width: 196px; height: 20px;background-color: #fafafa"/></dd>
            </dl>
            <dl>
                <dt></dt>
                <dd>
                    <a class="button reset" href="#"><indaba:msg key="common.btn.reset"/></a>
                    <a class="button find" href="#"><indaba:msg key="common.js.ckedit.toolbar.find"/></a>
                </dd>
            </dl>
        </fieldset>
        <table style="font-size: 13px" cellpadding="5" cellspacing="8">
            <tr>
                <td align="right"><b><indaba:msg key='common.msg.respondent' /></b></td>
                <td colspan="2">
                    <select id="ruserid" name="ruserid" style="font-size: 13px; width: 200px">
                        <c:forEach var="user" items="${assignedusers}" >
                            <option value="${user.userId}"
                                    <c:if test='${user.userId == auserid}'>selected</c:if>
                                    >${user.displayUsername}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td valign="top" align="right"><b><indaba:msg key='common.msg.actions'/></b></td>
                <td><b><indaba:msg key='common.msg.original'/></b></td>
                <td><b><indaba:msg key='common.msg.peerreviews'/></b></td>
            </tr>
            <tr>
                <td></td>
                <td nowrap>
                    <input name="content" type="checkbox" value="A" onclick="checkChecked();"
                           <c:if test="${empty param.contents || fn:contains(param.contents, 'A')}">checked="yes"</c:if>
                           > <indaba:msg key='common.js.msg.discussion'/><br>
            <input name="content" type="checkbox" value="B" onclick="checkChecked();"
                   <c:if test="${empty param.contents || fn:contains(param.contents, 'B')}">checked="yes"</c:if>
                   > <indaba:msg key='common.msg.changescores'/><br>
            <input name="content" type="checkbox" value="C" onclick="checkChecked();"
                   <c:if test="${empty param.contents || fn:contains(param.contents, 'C')}">checked="yes"</c:if>
                   > <indaba:msg key='common.msg.changeSrcDesc'/><br>
            <input name="content" type="checkbox" value="D" onclick="checkChecked();"
                   <c:if test="${empty param.contents || fn:contains(param.contents, 'D')}">checked="yes"</c:if>
                   > <indaba:msg key='common.msg.changeComments'/><br>
            <input name="content" type="checkbox" value="E" onclick="checkChecked();"
                   <c:if test="${empty param.param.contents || fn:contains(param.contents, 'E')}">checked="yes"</c:if>
                   > <indaba:msg key='common.msg.changeAttachments'/>
            </td>
            <td nowrap style="vertical-align: top">
                <input name="content" type="checkbox" value="F" onclick="checkChecked();"
                       <c:if test="${fn:contains(param.contents, 'F')}">checked="yes"</c:if>
                       > <indaba:msg key='common.js.msg.discussion'/><br>
            <input name="content" type="checkbox" value="G" onclick="checkChecked();"
                   <c:if test="${fn:contains(param.contents, 'G')}">checked="yes"</c:if>
                   > <indaba:msg key='common.msg.changeOptions'/>
            </td>
            </tr>
            <tr>
                <td align="right"><b><indaba:msg key='common.msg.dueDate'/></b></td>
                <td colspan="2">
                    <input id="duedate" name="duedate" STYLE="font-size: 13px" type="text" size="10" maxlength="10" value="${param.duedate}">
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2">
                    <button name="cancel" onclick="$.fancybox.close();" class="large button blue icon-cancel"><span style="font-size: 13px"><indaba:msg key='common.btn.cancel'/></span></button>
                </td>
                <td>
                    <button id="submitBtn" name="submit" onclick="submitProblems({ctx:'${param.cntxtPath}', horseid:'${param.horseId}', assignid:'${param.assignId}'});" class="large button blue icon-check">
                        <span style="font-size: 13px"><indaba:msg key='common.btn.submit'/></span>
                    </button>
                </td>
            </tr>
        </table>
    </div>
</div>
<script type="text/javascript">
    $(function() {
        var sendBox =  $('#send-box');
        sendBox.find('input:checkbox, input:text').uniform();
        sendBox.find('select').chosen();
        sendBox.find("#duedate").datepicker(({
            dateFormat: 'yy-mm-dd'
        }));
        initRespondentFilter('${param.cntxtPath}', $('#send-box'),${param.targetId});
        findSurveyReviewRespondentUsers('${param.cntxtPath}', $('#send-box'),'${param.aUserId}');
        resetRespondentFilter(sendBox, '${param.targetId}');
        sendBox.find('a.find').click(function(){
            findSurveyReviewRespondentUsers('${param.cntxtPath}', $('#send-box'), ${param.aUserId});
            return false;
        });
        sendBox.find('a.reset').click(function(){
            resetRespondentFilter(sendBox, '${param.targetId}');
            findSurveyReviewRespondentUsers('${param.cntxtPath}', $('#send-box'), ${param.aUserId});
            return false;
        });
        sendBox.find('input[name=content]').click(function() {
            for (var i = 1; i < 8; i++) {
                if (document.getElementById("box" + i).checked) {
                    document.getElementById("submitBtn").disabled = false;
                    return;
                }
                document.getElementById("submitBtn").disabled = true;
            }
            return false;
        });
    });
    function resetSendQuestionForm(boxElem, defaultUserId, contents, duedate){
        boxElem.find('select option').removeAttr('selected');
        boxElem.find('select#ruserid option[value='+defaultUserId+']').attr('selected', 'selected');
        boxElem.find('select').trigger("liszt:updated");
        boxElem.find('input#duedate').val(duedate);
        uncheckUniformChoice(boxElem.find('input:checkbox'));
        if(!contents || contents=='') {
            checkUniformChoice(boxElem.find('input:checkbox[value=A]'));
            checkUniformChoice(boxElem.find('input:checkbox[value=B]'));
            checkUniformChoice(boxElem.find('input:checkbox[value=C]'));
            checkUniformChoice(boxElem.find('input:checkbox[value=D]'));
            checkUniformChoice(boxElem.find('input:checkbox[value=E]'));
        } else {
            boxElem.find('input:checkbox').each(function(){
                var $this = $(this);
                var val = $this.val();
                if(contents.indexOf(val)>=0) {
                    checkUniformChoice($this);
                }
            });
        }
    }

    function initRespondentFilter(ctx, boxElem, defaultTargetId){
        $.ajax({
            type: "POST",
            url: ctx + "/srQstnRespond.do",
            dataType: 'json',
            data: {
                action: 'getOptions'
            },
            success: function(data) {
                if(data && data.roles) {
                    $.each(data.roles, function(index, value){
                        boxElem.find('select[name=roleId]').append("<option value='" + value.id + "'>" + value.name + "</option>");
                    });
                }
                if(data && data.targets) {
                    $.each(data.targets, function(index, value){
                        boxElem.find('select[name=targetId]').append("<option value='" + value.id + "' "+(value.id==defaultTargetId?"selected":"")+">" + value.name + "</option>");
                    });
                }
                boxElem.find('select').trigger("liszt:updated");
            },
            error: function(response) {
                jAlert($.i18n.message('common.js.err.interval'));
            }
        });
    }

    function resetRespondentFilter(boxElem, defaultTargetId){
        //boxElem.find('select option').removeAttr('selected');
        boxElem.find('select[name=roleId]').removeAttr('selected').find('option:first').attr('selected', 'selected');
        boxElem.find('select[name=targetId]').removeAttr('selected').find('option[value='+defaultTargetId+']').attr('selected', 'selected');
        boxElem.find('input[name=username]').val('');
        boxElem.find('select').trigger("liszt:updated");
    }

    function findSurveyReviewRespondentUsers(ctx, boxElem, defaultUserId){
        $.ajax({
            type: "POST",
            url: ctx + "/srQstnRespond.do",
            dataType: 'json',
            data: {
                action: 'findSurveyReviewRespondentUsers',
                roleId: boxElem.find('select[name=roleId]').val(),
                targetId: boxElem.find('select[name=targetId]').val(),
                username: boxElem.find('input[name=username]').val()
            },
            success: function(data) {
                if(data) {
                    var selectElem = boxElem.find('select[name=ruserid]');
                    selectElem.empty();
                    $.each(data, function(index, value){
                        selectElem.append("<option value='" + value.id + "' "+(value.id==defaultUserId?"selected":"")+">" + value.firstName +" "+ value.lastName + "</option>");
                    });
                }
                boxElem.find('select').trigger("liszt:updated");
            },
            error: function(response) {
                jAlert($.i18n.message('common.js.err.interval'));
            }
        });
    }

    function checkUniformChoice(elem) {
        elem.parent().addClass('checked');
        elem.attr('checked', true);
    }
    function uncheckUniformChoice(elem) {
        elem.parent().removeClass('checked');
        elem.removeAttr('checked');
    }
</script>