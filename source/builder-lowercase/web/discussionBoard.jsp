<%@page pageEncoding="UTF-8" language="java"%>
<%@ page import="com.ocs.indaba.common.Rights" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<un:useConstants var="Rights" className="com.ocs.indaba.common.Rights"/>

<c:set var="rightName" value="" />
<c:choose>
    <c:when test="${param.type eq 'ccw'}"><c:set var="rightName" value="${Rights.MANAGE_CONTENT_INTERNAL_DISCUSSION}" /></c:when>
    <c:when test="${param.type eq 'jsr'}"><c:set var="rightName" value="${Rights.MANAGE_JOURNAL_STAFF_AUTHOR_DISCUSSION}" /></c:when>
    <c:when test="${param.type eq 'jpr'}"><c:set var="rightName" value="${Rights.MANAGE_JOURNAL_PEER_REVIEW_DISCUSSIONS}" /></c:when>
    <c:when test="${param.type eq 'jprr'}"><c:set var="rightName" value="${Rights.MANAGE_JOURNAL_PEER_REVIEW_DISCUSSIONS}" /></c:when>
    <c:when test="${(param.type eq 'ssr') && (param.tool eq 'spr')}"><c:set var="rightName" value="${Rights.MANAGE_SURVEY_STAFF_AUTHOR_DISCUSSION_ON_PEER_REVIEW}" /></c:when>
    <c:when test="${param.type eq 'ssr'}"><c:set var="rightName" value="${Rights.MANAGE_SURVEY_STAFF_AUTHOR_DISCUSSION}" /></c:when>
    <c:when test="${param.type eq 'spr'}"><c:set var="rightName" value="${Rights.MANAGE_SURVEY_PEER_REVIEW_DISCUSSIONS}" /></c:when>
    <c:when test="${param.type eq 'sprr'}"><c:set var="rightName" value="${Rights.MANAGE_SURVEY_PEER_REVIEW_DISCUSSIONS}" /></c:when>
    <c:otherwise><c:set var="rightName" value="${Rights.NON_EXISTED_RIGHT}" /></c:otherwise>
</c:choose>
<h3>${param.displayName}
    <a class="toggleVisible" href="#">
        <c:choose>
            <c:when test="${param.folded eq 1}"><img src='images/expand_icon.png' alt='expand' /></c:when>
            <c:otherwise><img src='images/collapse_icon.png' alt='collapse' /></c:otherwise>
        </c:choose>
    </a>
</h3>
<div class="content" <c:if test="${param.folded eq 1}">style="display:none;"</c:if>>
    <div class="discussion-board">
        <div class="discussion-items">
            <c:forEach items="${messages}" var="msg">
                <c:choose>
                    <c:when test="${msg.deleteUserId <= 0}">
                        <div class="discussion-item" messageId="${msg.id}">
                            <div style="float:left;">
                                <strong>
                                    <c:choose>
                                        <c:when test="${msg.author.permission eq 0}">${msg.author.displayUsername}:</c:when>
                                        <c:when test="${msg.author.permission eq 1}">
                                            <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>:
                                        </c:when>
                                        <c:when test="${msg.author.permission eq 2}">
                                            <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>:
                                        </c:when>
                                        <c:when test="${msg.author.permission eq 3}">
                                            <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>:
                                        </c:when>
                                    </c:choose>
                                </strong>
                            </div>
                            <c:if test="${!(param.type eq 'case') and !(viewMode eq 'preVersion')}">
                                <indaba:view prjid="${prjid}" uid="${uid}" right="${rightName}">
                                    <div style="float:right;" class="delete">
                                        <a href="#"><span><img alt="delete" src="images/delete.png" />&nbsp;<indaba:msg key='common.btn.delete' /></span></a>
                                    </div>
                                </indaba:view>
                            </c:if>
                            <div style="clear: both">
                                ${msg.dispBody}
                            </div>
                            <div class="discussion-item-foot">
                                <fmt:formatDate value="${msg.createdTime}" pattern="yyyy-MM-dd" />
                            </div>
                            <indaba:view prjid="${prjid}" uid="${uid}" right="${rightName}">
                                <div class="discussion-item-enhance">
                                    <c:choose>
                                        <c:when test="${msg.enhanceBody ne null && msg.enhanceBody ne ''}">
                                            <c:set var="enhanceBody" value="${msg.enhanceBody}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="enhanceBody" value="${msg.body}" />
                                        </c:otherwise>
                                    </c:choose>
                                    <textarea class="text" name="enhanceBody" id="EnhanceEditor${msg.id}">${enhanceBody}</textarea>
                                    <label>
                                        <input id="PublishCheckBox${msg.id}" type="checkbox" /><indaba:msg key='common.btn.publish' />
                                    </label>&nbsp;&nbsp;
                                    <button class='small blue button btn-save'><indaba:msg key='common.btn.save' /></button>
                                </div>
                            </indaba:view>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <indaba:view prjid="${prjid}" uid="${uid}" right="${rightName}">
                            <div class="discussion-item" messageId="${msg.id}">
                                <div style="float:left;">
                                    <strong>
                                        <c:choose>
                                            <c:when test="${msg.author.permission eq 0}">${msg.author.displayUsername}:</c:when>
                                            <c:when test="${msg.author.permission eq 1}">
                                                <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>:
                                            </c:when>
                                            <c:when test="${msg.author.permission eq 2}">
                                                <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>:
                                            </c:when>
                                            <c:when test="${msg.author.permission eq 3}">
                                                <a href="${contextPath}/profile.do?targetUid=${msg.author.userId}">${msg.author.displayUsername}</a>:
                                            </c:when>
                                        </c:choose>
                                    </strong>
                                </div>
                                <c:if test="${!(param.type eq 'case') and !(viewMode eq 'preVersion')}">
                                    <indaba:view prjid="${prjid}" uid="${uid}" right="${rightName}">
                                        <div style="float:right;" class="delete">
                                            <a href="#"><span><img alt="undelete" src="images/undelete.png" />&nbsp;<indaba:msg key='common.btn.undelete' /></span></a>
                                        </div>
                                    </indaba:view>
                                </c:if>
                                <div style="clear: both">
                                    ${msg.dispBody}
                                </div>
                                <div class="discussion-item-foot">
                                    <fmt:formatDate value="${msg.createdTime}" pattern="yyyy-MM-dd" />
                                </div>
                                <c:if test="${(viewMode ne 'preVersion')}">
                                    <indaba:view prjid="${prjid}" uid="${uid}" right="${rightName}">
                                        <div class="discussion-item-enhance">
                                            <c:choose>
                                                <c:when test="${msg.enhanceBody ne null && msg.enhanceBody ne ''}">
                                                    <c:set var="enhanceBody" value="${msg.enhanceBody}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="enhanceBody" value="${msg.body}" />
                                                </c:otherwise>
                                            </c:choose>
                                            <textarea class="text" name="enhanceBody" id="EnhanceEditor${msg.id}">${enhanceBody}</textarea>
                                            <label>
                                                <input id="PublishCheckBox${msg.id}" type="checkbox" /><indaba:msg key='common.btn.publish' />
                                            </label>&nbsp;&nbsp;
                                            <button class='small blue button btn-save'><indaba:msg key='common.btn.save' /></button>
                                        </div>
                                    </indaba:view>
                                </c:if>
                            </div>
                        </indaba:view>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

        <c:if test="${(param.type eq 'ccw') && (viewMode ne 'preVersion')}">
            <indaba:view prjid="${prjid}" uid="${uid}" right="${Rights.WRITE_CONTENT_INTERNAL_DISCUSSION}">
                <div class="discussion-add-comment">
                    <textarea class="text" id="${param.name}NewEditor" name="body" ${param.disabled}></textarea>
                    <button class="add medium button blue icon-add">
                        <indaba:msg key='common.btn.addcomment' />
                    </button>
                    <span><indaba:msg key='common.label.adding' /></span>
                </div>
            </indaba:view>
        </c:if>

        <c:if test="${(param.type eq 'case')  || ((param.checkPermission ne '1') && (param.type eq 'jsr' || param.type eq 'jpr' || param.type eq 'jprr' || param.type eq 'ssr' || param.type eq 'spr' || param.type eq 'sprr')) }">
            <div class="discussion-add-comment">
                <c:if test="${(param.disabled ne 'disabled')}">
                    <textarea class="text" id="${param.name}NewEditor" name="body" ${param.disabled}></textarea>
                    <button class="add medium button blue icon-add">
                        <indaba:msg key='common.btn.addcomment' />
                    </button>
                    <span><indaba:msg key='common.label.adding' /></span>
                </c:if>
            </div>
        </c:if>
    </div>
</div>

<indaba:view prjid="${prjid}" uid="${uid}" right="${rightName}">
    <script type="text/javascript">
        $(function(){
            $('.discussion-item .delete a').click(function(){
                var self = $(this);
                var action = $('img', $(this)).attr('alt');
                var msgId = $(this).parents('.discussion-item').attr('messageId');
                
                $.ajax(
                {type: "POST",
                    url: "discussionBoard.do",
                    data: {action: action, msgId: msgId},
                    success: function(result) {
                        self.empty();
                        if(action == 'undelete') {
                            self.append('<span><img alt="delete" src="images/delete.png" />&nbsp;<indaba:msg key="common.btn.delete" /></span>');
                        } else {
                            self.append('<span><img alt="undelete" src="images/undelete.png" />&nbsp;<indaba:msg key="common.btn.undelete" /></span>');
                        }
                    }
                });
                return false;
            });
        });
    </script>
</indaba:view>