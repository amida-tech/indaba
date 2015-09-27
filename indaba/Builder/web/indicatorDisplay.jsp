<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="indaba" uri="indabaTaglib" %>

<c:set var="editable" value="false"/>
<c:set var="sourcesEditable" value="false"/>
<c:set var="commentsEditable" value="false"/>

<c:if test="${param.editable}">
    <c:set var="editable" value="true" />
</c:if>
<c:if test="${param.sourcesEditable}">
    <c:set var="sourcesEditable" value="true" />
</c:if>
<c:if test="${param.commentsEditable}">
    <c:set var="commentsEditable" value="true"/>
</c:if>

<c:forEach items="${view.categoryViewList}" var="category" varStatus="status">
    <div class="content">
        <div class="box"<%--<c:if test="${status.last}">style="background-color:white;"</c:if>--%>>
            <h3>${category.label}&nbsp;&nbsp;&nbsp;&nbsp;${category.title}</h3>
        </c:forEach>
        <form id="uploadForm" <c:if test="${empty surveyAnswer.categoryViewList}">class="survey-answer"</c:if>
              method="post" action="surveysubmit.do" enctype="multipart/form-data">
                <div class="content survey-answer-content">
                <c:if test="${view.answerObjectId == 0}">
                    <font color="red"><indaba:msg key='common.alert.noanswer' /></font><br/><br/>
                </c:if>
                <div class="answer-part-score">
                <table>
                    <tr>
                        <td valign="top" width="1%" nowrap>
                            <b>${view.publicName}:</b>
                        </td>
                        <td>
                            <b>${view.question}</b>
                        </td>
                        <c:if test="${tipinfo != null && tipDisplayMethod == 1}">
                            <td>
                                <a id="indicator-tip" href="#" class="tipTip rightDisplay" title="${fn:replace(tipinfo, "\"", '&quot;')}"  onclick="return false;"><img src="images/hint_icon.png" alt="TIP"/></a>
                                <script>$(".tipTip").tipTip();</script>
                            </td>
                        </c:if>
                    </tr>
                </table>
                <c:if test="${tipinfo != null && tipDisplayMethod == 2}">
                    <div class="tipinfo">${tipinfo}</div>
                </c:if>
                <c:choose>
                    <c:when test="${view.answerType eq 6}"> <%-- table question --%>
                        <indaba:tableAnswer horseId="${horseid}" 
                                            mainQuestionId="${questionid}" 
                                            disabled="${not editable}" />
                    </c:when>
                    <c:otherwise> <%-- simple question(int, float, single/multi choice, text) --%>
                        <indaba:simpleAnswer answerType="${view.answerType}" 
                                             answerTypeId="${view.answerTypeId}" 
                                             name="selection" 
                                             showAnswer="true" 
                                             answerObjectId="${view.answerObjectId}" 
                                             disabled="${not editable}" />
                    </c:otherwise>
                </c:choose>
                </div>
                <br/>
                <div class="answer-part-source">
                <b><indaba:msg key='common.title.sources' />:</b><br/>${view.refdescrition}<br/>
                <indaba:source referenceType="${view.referType}" referenceId="${view.referId}" name="source" showAnswer="true" referenceObjectId="${view.referenceObjectId}" disabled="${not sourcesEditable}"></indaba:source>
                </div>
                <br/><br/>
                <!-- include attachment widget -->
                <div class="answer-part-attachment">
                <jsp:include page="attachment.jsp" flush="true" >
                    <jsp:param name="contentType" value="survey" />
                    <jsp:param name="answerId" value="${view.surveyAnswerId}" />
                    <jsp:param name="horseId" value="${param.horseid}" />
                </jsp:include>
                </div>
                <div class="answer-part-comment">
                <b><indaba:msg key='common.title.comments' />:</b>
                <br/>
                <TEXTAREA NAME="comments" class="text" ROWS="6" onclick="resetStatus();" <c:if test="${not commentsEditable}">disabled="disabled"</c:if>>${view.comments}</TEXTAREA>
                </div>
            </div>
        </form>
        <c:forEach items="${view.categoryViewList}" var="category">
        </div>
    </div>
</c:forEach>