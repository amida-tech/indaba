<%--
    Document   : surveyAnswerFlagResponseNavigation
    Created on : Apr 1, 2014, 2:29:54 PM
    Author     : yc06x
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<c:if test="${not empty flagNavView}">
    <c:if test="${flagNavView.prevFlag != null}">
        <c:set var="prevFlagUrl" value="${contextPath}/flagResponse.do?horseid=${flagNavView.horseId}&flagid=${flagNavView.flagId}&assignid=${assignid}&dir=-1&fromurl=${flagNavView.exitUrl}"/>
    </c:if>
    <c:if test="${flagNavView.nextFlag != null}">
        <c:set var="nextFlagUrl" value="${contextPath}/flagResponse.do?horseid=${flagNavView.horseId}&flagid=${flagNavView.flagId}&assignid=${assignid}&dir=1&fromurl=${flagNavView.exitUrl}"/>
    </c:if>
    <c:set var="exitUrl" value="${contextPath}/yourcontent.do"/>
    <c:if test='${not empty flagNavView.exitUrlDecoded}'>
        <c:set var="exitUrl" value="${flagNavView.exitUrlDecoded}"/>
    </c:if>

    <div id="scorecard-navigator-box" class="box flag-response-box">
        <ul class="scorecard-navigator">
            <li id="navigator-home" onclick="location.href = '${flagNavView.homeUrl}'" title="<indaba:msg key='common.msg.home' />"></li>
            <li id="navigator-flag" title="<indaba:msg key='common.title.flags'/>"></li>
            <li class="navi">
                <ul>
                    <li id="navigator-prev-indicator"
                        onclick="moveToFlag('${contextPath}', ${flagNavView.horseId}, ${flagNavView.flagId}, ${assignid}, '${flagNavView.exitUrl}', -1);"
                        title="Previous flag"></li>

                    <li id="navigator-next-indicator"
                        onclick="moveToFlag('${contextPath}', ${flagNavView.horseId}, ${flagNavView.flagId}, ${assignid}, '${flagNavView.exitUrl}', 1);"
                        title="Next flag"></li>
                </ul>
            </li>
            <li id="navigator-exit"
                onclick="location.href='${exitUrl}'" title="Exit"></li>
        </ul>
        <div style="clear:both"></div>
    </div>
    <div id="no-display-warpper" style="display:none;">
        <div id="flag-list-box">
            <div id="assgined-to-me" class="flag-list"></div>
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
                                $('#no-display-warpper').remove();
                                loadFlagData();
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
                            title: 'Flags Assigned to Me',
                            button: true,
                            text: $('#no-display-warpper').html()
                        }
                    });

                    function loadFlagData() {
                        var $flagListElem = $('#assgined-to-me');
                        var action = 'getActiveFlagsAssignedToMe';

                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            url: '${contextPath}/commPanel.do',
                            data: {action: action, horseid: '${param.horseid}'},                           
                            success: function(data) {
                                if (data && data.length > 0) {
                                    var $list = $('<ul class="flag-list-data"></ul>');
                                    $.each(data, function(i, elem) {
                                        var flagUrl;
                                        flagUrl = "${contextPath}/flagResponse.do?horseid=${flagNavView.horseId}" +
                                            "&flagid=" + elem.flagId +
                                            "&assignid=${assignid}&questionid=" + elem.surveyQuestionId +
                                            "&initialFlagGroupId=" + elem.groupobjId +
                                            "&fromurl=${flagNavView.exitUrl}";

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
