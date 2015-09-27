<%-- 
    Document   : surveyAnswerInternalDiscussion.jsp
    Created on : 2010-3-23, 20:26:46
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<indaba:view prjid="${prjid}" uid="${uid}" right="read content internal discussion">
    <div id="discussion" class="box"></div>
    <script type="text/javascript">
        $(document).ready(function(){
            var parameters = {};
            parameters.userId = '${uid}';
            parameters.userName = "${name}";
            parameters.horseId = '${param.horseid}';
            parameters.questionId = '${param.questionid}';
            parameters.ctxPath = "${contextPath}";
            parameters.folded = "${param.folded}";
            parameters.viewMode = "${param.viewMode}";
            parameters.contentVersionId = '${param.contentVersionId}';
            loadSurveyAnswerDiscussions(parameters);
        });
    </script>
</indaba:view>