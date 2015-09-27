<%-- 
    Document   : surveyAnswerDisplayNavigation
    Created on : 2010-6-14, 1:00:45
    Author     : luwb
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<c:if test="${not empty urlView}">
    <c:choose>
        <c:when test="${param.action eq 'surveyAnswerDisplay.do'}">
            <c:set var="prevCatUrl" value="${param.action}?questionid=${urlView.upId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&contentVersionId=${param.contentVersionId}&returl=${urlView.returnUrl}" />
            <c:set var="nextCatUrl" value="${param.action}?questionid=${urlView.downId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&contentVersionId=${param.contentVersionId}&returl=${urlView.returnUrl}" />

            <c:set var="prevIndicatorUrl" value="${param.action}?questionid=${urlView.previousId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&contentVersionId=${param.contentVersionId}&returl=${urlView.returnUrl}" />  
            <c:set var="nextIndicatorUrl" value="${param.action}?questionid=${urlView.nextId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&contentVersionId=${param.contentVersionId}&returl=${urlView.returnUrl}" />
        </c:when>
        <c:otherwise>
            <c:set var="prevCatUrl" value="${param.action}?questionid=${urlView.upId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&returl=${urlView.returnUrl}" />
            <c:set var="nextCatUrl" value="${param.action}?questionid=${urlView.downId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&returl=${urlView.returnUrl}" />

            <c:set var="prevIndicatorUrl" value="${param.action}?questionid=${urlView.previousId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&returl=${urlView.returnUrl}" />  
            <c:set var="nextIndicatorUrl" value="${param.action}?questionid=${urlView.nextId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&returl=${urlView.returnUrl}" />
        </c:otherwise>
    </c:choose>
    <div id="scorecard-navigator-box" class="box">
        <%--<h3><indaba:msg key='common.msg.scorecardnav' /></h3>--%>
        <ul class="scorecard-navigator">
            <li id="navigator-home" onclick="location.href = '${param.returl}'" title="<indaba:msg key='common.msg.home' />"></li>
            <li id="navigator-flag" title="<indaba:msg key='common.title.flags'/>"></li>
            <li class="navi">
                <ul>
                    <c:choose>
                        <c:when test="${urlView.previousId == 0 || from == 'surveyReviewResponse.do'}">
                            <li id="navigator-prev-indicator-disabled" title="<indaba:msg key='common.msg.previndicator' />"></li>
                            </c:when>
                            <c:otherwise>
                            <li id="navigator-prev-indicator" onclick="location.href = '${prevIndicatorUrl}'" title="<indaba:msg key='common.msg.previndicator' />"></li>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${urlView.nextId == 0 || from == 'surveyReviewResponse.do'}">
                            <li id="navigator-next-indicator-disabled" title="<indaba:msg key='common.msg.nextindicator' />"></li>
                            </c:when>
                            <c:otherwise>
                            <li id="navigator-next-indicator" onclick="location.href = '${nextIndicatorUrl}'" title="<indaba:msg key='common.msg.nextindicator' />"></li>
                            </c:otherwise>
                        </c:choose>
                </ul>
            </li>
            <li class="navi last">
                <ul>
                    <c:choose>
                        <c:when test="${urlView.upId == 0 || from == 'surveyReviewResponse.do'}">
                            <li id="navigator-prev-cat-disabled" title="<indaba:msg key='common.msg.prevcategory' />"></li>
                            </c:when>
                            <c:otherwise>
                            <li id="navigator-prev-cat" onclick="location.href = '${prevCatUrl}'" title="<indaba:msg key='common.msg.prevcategory' />"></li>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${urlView.downId == 0 || from == 'surveyReviewResponse.do'}">
                            <li id="navigator-next-cat-disabled" title="<indaba:msg key='common.msg.nextcategory' />"></li>
                            </c:when>
                            <c:otherwise>
                            <li id="navigator-next-cat" onclick="location.href = '${nextCatUrl}'" title="<indaba:msg key='common.msg.nextcategory' />"></li>
                            </c:otherwise>
                        </c:choose>
                </ul>
            </li>
            
        </ul>
        <div style="clear:both"></div>
    </div>
    <div id="no-display-warpper" style="display:none;">
        <div id="flag-list-box">
            <ul class="flag-tabs">
                <li title="<indaba:msg key='common.title.flagsassignedtome'/>"><a href="#assgined-to-me" class="assigned-to-me"></a></li>
                <li title="<indaba:msg key='common.title.flagsraisedbyme'/>"><a href="#raised-by-me" class="raised-by-me"></a></li>
                <li title="<indaba:msg key='common.title.flagothers'/>"><a href="#flags-other" class="flags-other"></a></li>
            </ul>
            <div id="raised-by-me" class="flag-list"></div>
            <div id="assgined-to-me" class="flag-list"></div>
            <div id="flags-other" class="flag-list"></div>
        </div>
    </div>
    <link type="text/css" rel="stylesheet" href="plugins/qtip/jquery.qtip.css"/>
    <script type="text/javascript" src="plugins/qtip/jquery.qtip.js"></script>
    <script type="text/javascript">
                $(function() {
                    $('ul.scorecard-navigator li#navigator-flag').click(function() {
                        return false;
                    });
                    $('ul.scorecard-navigator li#navigator-flag').qtip({
                        id: 'flags',
                        position: {
                            my: 'top center',
                            at: 'bottom center'
                        },
                        overwrite: true,
                        suppress: false,
                        events: {
                            visible: function(event, api) {
                                loadFlagData($('#flag-list-box .ui-tabs-active'));
                            },
                            render: function(event, api) {
                                $('#no-display-warpper').remove();
                                var content = api.elements.content;
                                $('#flag-list-box', content).tabs({
                                    active: 0,
                                    activate: function(event, ui) {
                                        loadFlagData(ui.newTab);
                                    }
                                });
                            },
                            hide: function(event, api) {
                                $('.flag-list').empty();
                            }
                        },
                        style: {tip: {corner: 'top center'}, widget: true},
                        show: {event: 'click', modal: {on: false}, solo: true},
                        hide: {
                            event: 'click unfocus',
                            inactive: 5000
                        },
                        content: {
                            title: $.i18n.message('common.title.flags'),
                            button: true,
                            text: $('#no-display-warpper').html()
                        }
                    });

                    function loadFlagData(tab) {
                        var $flagListElem = $(tab.find('a').attr('href')), action;

                        switch ($flagListElem.attr('id')) {
                            case 'raised-by-me':
                                action = 'getActiveFlagsRaisedByMe';
                                break;
                            case 'assgined-to-me':
                                action = 'getActiveFlagsAssignedToMe';
                                break;
                            case 'flags-other':
                            default:
                                action = 'getActiveFlagsOther';

                        }
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            url: '${contextPath}/commPanel.do',
                            data: {action: action, horseid: '${param.horseid}'},                           
                            success: function(data) {
                                $flagListElem.empty();
                                if (data && data.length > 0) {
                                    var $list = $('<ul class="flag-list-data"></ul>');
                                    $.each(data, function(i, elem) {
                                        var flagUrl;
                                        <c:choose>
                                        <c:when test="${param.action eq 'surveyAnswerDisplay.do'}">
                                            flagUrl = "${param.action}?questionid=" + elem.surveyQuestionId +
                                                "&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}&contentVersionId=${param.contentVersionId} " +
                                                "&initialFlagGroupId=" + elem.groupobjId +
                                                "&returl=${urlView.returnUrl}";
                                        </c:when>
                                        <c:otherwise>
                                            flagUrl = "${param.action}?questionid=" + elem.surveyQuestionId +
                                                "&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.from}" +
                                                "&initialFlagGroupId=" + elem.groupobjId +
                                                "&returl=${urlView.returnUrl}";
                                        </c:otherwise>
                                        </c:choose>
                                        $list.append('<li><span class="' + (elem.worked ? 'flag-worked' : '') + '"></span><a class="title" href="' + flagUrl + '">' + elem.index + ': ' + elem.title + '</a></li>');
                                    });
                                    $flagListElem.append($list);
                                } else {
                                    $flagListElem.append('<div class="msg">' + $.i18n.message('common.msg.noflags') + '</div>');
                                }
                            }
                        });
                    }
                });
    </script>
</c:if>
