<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="indaba" uri="indabaTaglib"%>

<link type="text/css" rel="stylesheet" href="css/menu.css"/>
<link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
<div id="project-selector-wrapper" style="display:none;">
    <div id="project-selector">
        <c:if test="${prjList != null && fn:length(prjList) > 1 }">
            <ul>
                <c:forEach items="${prjList}" var="prj">
                    <c:if test="${prjid != prj.id}">
                        <li><a href="switchToProject.do?prjid=${prj.id}&returl=${returl}">${prj.codeName}</a></li>
                    </c:if>
                </c:forEach>
            </ul>
        </c:if>

    </div>
</div>
<div id="mainheader" class="noprint">
    <a href="yourcontent.do" title="<indaba:msg key='jsp.header.home' />"><img src="images/indaba-logo.gif" id="mainlogo"/></a>
    <ul id="project">
        <li id="project-menu">
            <table id="project_info" width="100%">
                <tr>
                    <td valign="middle" nowrap>
                        <!--<a href="/projectMenu">Project Menu</a> -->
                            <c:choose>
                                <c:when test="${(prjList == null) || (empty prjList)}">
                                    <label><indaba:msg key='common.msg.noproject' /></label>
                                </c:when>
                                <c:when test="${fn:length(prjList) <= 1}">
                                    <label>${prjName}</label>
                                </c:when>
                                <c:otherwise>
                                    <a id="select-project" href="" title="switch project" onclick="return false;">${prjName}</a>
                                </c:otherwise>
                            </c:choose>                                                                
                    </td>
                    <td style="text-align: right">
                        <a class="userid" href="profile.do?targetUid=${uid}" style="color: #009900;">
                            <indaba:abbreviate length="24" value="${(empty name) ? '' : name}"/></a>
                        <a href="logout.do"><indaba:msg key='jsp.header.logout' /></a>
                        <indaba:view prjid="${prjid}" uid="${uid}" right="use indaba admin">
                            <a href="${admintoolBaseURL}/login.php" target="admintool"><indaba:msg key='jsp.header.admin' /></a>
                        </indaba:view>
                </td>
                </tr>
            </table>
        </li>
        <!-- Set the highlight of the active tab -->
        <c:set var="mycntclr" value="#FFFFFF" scope="request"/>
        <c:set var="allcntclr" value="#FFFFFF" scope="request"/>
        <c:set var="queuesclr" value="#FFFFFF" scope="request"/>
        <c:set var="casesclr" value="#FFFFFF" scope="request"/>
        <c:set var="peopleclr" value="#FFFFFF" scope="request"/>
        <c:set var="msgclr" value="#FFFFFF" scope="request"/>
        <c:set var="helpclr" value="#FFFFFF" scope="request"/>
        <!--c:set var="active" value='${sessionScope["active"]}' scope="request"/ -->
        <c:if test='${at eq "yourcontent"}'>
            <c:set var="mycntclr" value="#FFCC00" scope="request"/>
        </c:if>
        <c:if test='${at eq "allcontent"}'>
            <c:set var="allcntclr" value="#FFCC00" scope="request"/>
        </c:if>
        <c:if test='${at eq "queues"}'>
            <c:set var="queuesclr" value="#FFCC00" scope="request"/>
        </c:if>
        <c:if test='${at eq "cases"}'>
            <c:set var="casesclr" value="#FFCC00" scope="request"/>
        </c:if>
        <c:if test='${at eq "people"}'>
            <c:set var="peopleclr" value="#FFCC00" scope="request"/>
        </c:if>
        <c:if test='${at eq "messaging"}'>
            <c:set var="msgclr" value="#FFCC00" scope="request"/>
        </c:if>
        <c:if test='${at eq "help"}'>
            <c:set var="helpclr" value="#FFCC00" scope="request"/>
        </c:if>
        <!-- End set-->
        <li>
            <ul id="nav">
                <li class="tab">
                    <a class="tab" href="yourcontent.do" style="color: ${mycntclr}"><indaba:msg key='common.msg.youcontent' /></a>
                </li>
                <indaba:view prjid="${prjid}" uid="${uid}" right="see all content">
                    <li class="tab">
                        <a class="tab" href="allcontent.do" style="color: ${allcntclr}"><indaba:msg key='jsp.header.menu.allcontent' /></a>
                    </li>
                </indaba:view>
                <indaba:view prjid="${prjid}" uid="${uid}" right="see task queues | manage queues">
                    <li class="tab">
                        <a class="tab" href="queuesv2.do" style="color: ${queuesclr}"><indaba:msg key='jsp.header.menu.queue' /></a>
                    </li>
                </indaba:view>
                <li class="tab">
                    <a class="tab" href="cases.do" style="color: ${casesclr}"><indaba:msg key='common.label.cases' /></a>
                </li>
                <li class="tab">
                    <a class="tab" href="people.do" style="color: ${peopleclr}"><indaba:msg key='jsp.header.menu.people' /></a>
                </li>
                <li class="tab">
                    <a class="tab" href="messages.do" style="color: ${msgclr}"><indaba:msg key='jsp.header.menu.messaging' /></a>
                </li>
                <li class="tab">
                    <a class="tab" href="help.do" target="_blank" style="color: ${helpclr}"><indaba:msg key='jsp.header.menu.help' /></a>
                </li>
            </ul>
        </li>
    </ul>
</div>
<link type="text/css" rel="stylesheet" href="plugins/qtip/jquery.qtip.css"/>
<script type="text/javascript" src="plugins/qtip/jquery.qtip.js"></script>

<c:if test="${prjList != null && fn:length(prjList) > 1 }">
<script type="text/javascript">   
    $(function() {       
        $('#select-project').qtip({
            id: '#selectprj',
            position: {
                my: 'top left',
                at: 'bottom left',
                adjust: {
                    x: 14
                }
            },
            overwrite: false,
            suppress: false,
            style: {
                tip: {corner: false},
                widget: false,
                width: 500,
                classes: 'qtip-blue'
            },
            show: {event: 'click', modal: {on: false}, solo: true},
            hide: {
                event: 'click unfocus',
                inactive: 5000
            },
            content: {
                title: false,
                button: "",
                text: $('#project-selector-wrapper').html()
            }
        });
               
    });

</script>
</c:if>