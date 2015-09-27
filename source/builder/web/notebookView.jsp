<%-- 
    Document   : notebookView
    Created on : 2010-3-24, 14:59:00
    Author     : Luke Shi
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div>
    <h2 class="pos-relative">${journalTitle} (Read Only View)
        <c:if test="${action == 'display' }">
            <indaba:view prjid="${prjid}" uid="${uid}" right="super edit content">
                <div id="super-edit-div">
                    <c:choose>
                        <c:when test="${contentHeader.submitTime != null}">
                            <a id="super-edit-link" href="journalOverallReview.do?toolid=18&tasktype=18&horseid=${horseid}&assignid=0&superedit=1"><img src="images/edit.png" alt=""/>&nbsp;<indaba:msg key='jsp.notebookView.editThis' /></a>
                            </c:when>
                            <c:otherwise>
                            <indaba:msg key='common.btn.editdisabled' />
                        </c:otherwise>
                    </c:choose>
                </div>
            </indaba:view>
        </c:if>
    </h2>
    <div class="clear">
    </div>
    <c:if test="${maxWords > 0}">
        <div class="box">
            <h3><a name="journalContent"><indaba:msg key='jsp.notebookView.notebookContent' /></a><a href="#journalContent" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
            <hr/>
            <div class="content notebookview" style="margin: 10px; <c:if test='${empty journalBody}'>height: 40px;</c:if> ">
                ${journalBody}
            </div>
        </div>
    </c:if>

    <!-- include attachment widget -->
    <jsp:include page="attachment.jsp" flush="true" >
        <jsp:param name="contentType" value="journal" />
        <jsp:param name="horseId" value="${horseid}" />
        <jsp:param name="readonly" value="${(empty param.noreadonly) || !param.noreadonly}" />
    </jsp:include>

</div>

