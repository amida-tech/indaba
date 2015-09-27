<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach var="problem" items="${problems}" >
    <c:if test='${action == "surveyReview.do" || action == "surveyPRReview.do"}'>
        <a href="#" onclick="removeQuestion('${problem.answerId}', '${problem.publicName}: ${problem.question}'); return false;"><img src="images/delete.png" alt="Delete" style="width: 12px;position:relative;top:2px;"/></a>
    </c:if>

    <c:choose>
        <c:when test='${action == "surveyPRReview.do"}'>
            <b>${problem.publicName}:</b> <a href="surveyAnswerPRReview.do?answerid=${problem.answerId}&questionid=${problem.questionId}&horseid=${horseid}&assignid=${assignid}&action=${action}&returl=${returl}">${problem.question}</a>
        </c:when>
        <c:when test='${action == "surveyReview.do"}'>
            <b>${problem.publicName}:</b> <a href="surveyAnswerReview.do?answerid=${problem.answerId}&questionid=${problem.questionId}&horseid=${horseid}&assignid=${assignid}&action=${action}&returl=${returl}">${problem.question}</a>
        </c:when>
        <c:otherwise>
            <b>${problem.publicName}:</b> <a href="surveyAnswer.do?answerid=${problem.answerId}&questionid=${problem.questionId}&horseid=${horseid}&assignid=${assignid}&action=${action}&returl=${returl}">${problem.question}</a>
        </c:otherwise>
    </c:choose>
    <c:if test="${problem.responded}"><img src="images/check.png" alt=""/></c:if>
    <br/>
</c:forEach>
