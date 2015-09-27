<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<div class="filters">
    <div class="filter">
        <h4><indaba:msg key='common.msg.target' /></h4>
        <script>
            var obj_target = {};
            <c:forEach var="target" items="${trgtListByPrj}">
                obj_target['${target.shortName}'] = ${target.id};
            </c:forEach>
        </script>
        <select id="target" name="target" style="width:150px;">
            <!--<option  value="0" <c:if test="${targetId==0}">selected="yes"</c:if>>All</option>-->
            <c:forEach var="target" items="${trgtListByPrj}">
                <option value="${target.id}" <c:if test="${targetId==target.id}">selected="yes"</c:if>>${target.shortName}</option>
            </c:forEach>
        </select>
        <button onclick="return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button> =>
        <div class="selectedFilters">
            <div id="selectedList" style="display: none;">
            </div>
        </div>
        <div class="clear"></div>
        <div>
            <label><input type="radio" id="targetAll" name="targetSelectionType" value="0" checked="true"/><indaba:msg key='common.choice.exclude' /></label>
            <label><input type="radio" id="targetOnly" name="targetSelectionType" value="1"/><indaba:msg key='common.choice.onlyselected' /></label>
        </div>
    </div>
    <hr/>
    <div class="filter">
        <h4><indaba:msg key='common.msg.product' /></h4>
        <script>
            var obj_product = {};
            <c:forEach var="product" items="${prdListByPrj}">
                obj_target['${product.name}'] = ${product.id};
            </c:forEach>
        </script>
        <select id="product" name="product" style="width:150px;">
        <!--<option value="0" <c:if test="${productId==0}">selected="yes"</c:if>>All</option>-->
            <c:forEach var="product" items="${prdListByPrj}">
                <option  value="${product.id}" <c:if test="${productId==product.id}">selected="yes"</c:if>>${product.name}</option>
            </c:forEach>
        </select>
        <button onclick="return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button> =>
        <div class="selectedFilters">
            <div id="selectedList" style="display: none;">
            </div>
        </div>

        <div class="clear"></div>
        <div>
            <label><input type="radio" id="productAll" name="prodSelectionType" value="0" checked="true"/><indaba:msg key='common.choice.exclude' /></label>
            <label><input type="radio" id="productOnly" name="prodSelectionType" value="1"/><indaba:msg key='common.choice.onlyselected' /></label>
        </div>
    </div>
    <hr/>

    <c:if test="${not empty cTagList}">
            <div class="filter">
                <h4><indaba:msg key='jsp.caseFilter.tag' /></h4>
                <script>
                    var obj_cTag = {};
                    <c:forEach var="cTag" items="${cTagList}">
                        obj_target['${cTag.term}'] = ${cTag.id};
                </c:forEach>
            </script>
            <select id="cTag" name="cTag" style="width:150px;">
            <!--<option value="0" <c:if test="${cTagId==0}">selected="yes"</c:if>>All</option>-->
                <c:forEach var="cTag" items="${cTagList}">
                    <option  value="${cTag.id}" <c:if test="${cTagId==cTag.id}">selected="yes"</c:if>>${cTag.term}</option>
                </c:forEach>
            </select>
            <button onclick="return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button> =>
            <div class="selectedFilters">
                <div id="selectedList" style="display: none;">
                </div>
            </div>

            <div class="clear"></div>
            <div>
                <label><input type="radio" id="cTagAll" name="cTagSelectionType" value="0" checked="true"/><indaba:msg key='common.choice.exclude' /></label>
                <label><input type="radio" id="cTagOnly" name="cTagSelectionType" value="1"/><indaba:msg key='common.choice.onlyselected' /></label>
            </div>
        </div>
        <hr/>
    </c:if>

    <div class="filter">
        <h4><indaba:msg key='common.label.status' /></h4>
        <div>
            <c:forEach var="status" items="${caseStatusList}">
                <%--<label><input type="checkbox" name="caseStatus" value="${status.statusCode}" <c:if test="${status.statusCode == 0}">checked</c:if> />${status.statusDesc}</label>--%>
                <label><input type="checkbox" name="caseStatus" value="${status.statusCode}" checked /><c:if test="${status.statusDesc == 'Open'}"><indaba:msg key='common.label.open' /></c:if><c:if test="${status.statusDesc == 'Closed'}"><indaba:msg key='common.label.closed' /></c:if></label>
                </c:forEach>
        </div>
    </div>
</div>

