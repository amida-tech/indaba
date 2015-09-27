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
        <button onclick='return false;' class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button> =>
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
    <div class="filter">
        <h4><indaba:msg key='common.label.role' /></h4>
        <script>
            var obj_role = {};
                <c:forEach var="role" items="${roleList}">
                obj_target['${role.name}'] = ${role.id};
            </c:forEach>
        </script>
        <select id="role" name="role" style="width:150px;">
            <!--<option value="0" <c:if test="${roleId==0}">selected="yes"</c:if>>All</option>-->
            <c:forEach var="role" items="${roleList}">
                <option  value="${role.id}" <c:if test="${roleId==role.id}">selected="yes"</c:if>>${role.name}</option>
            </c:forEach>
        </select>
        <button onclick="return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button> =>
        <div class="selectedFilters">
            <div id="selectedList" style="display: none;">
            </div>
        </div>

        <div class="clear"></div>
        <div>
            <label><input type="radio" id="roleAll" name="roleSelectionType" value="0" checked="true"/><indaba:msg key='common.choice.exclude' /></label>
            <label><input type="radio" id="roleOnly" name="roleSelectionType" value="1"/><indaba:msg key='common.choice.onlyselected' /></label>
        </div>
    </div>
    <hr/>
    <div class="filter">
        <h4><indaba:msg key='jsp.peopleFilter.team' /></h4>
        <script>
            var obj_team = {};
            <c:forEach var="team" items="${teamList}">
                obj_target['${team.teamName}'] = ${team.id};
            </c:forEach>
        </script>
        <select id="team" name="team" style="width:150px;">
            <!--<option value="0" <c:if test="${teamId==0}">selected="yes"</c:if>>All</option>-->
            <c:forEach var="team" items="${teamList}">
                <option  value="${team.id}" <c:if test="${teamId==team.id}">selected="yes"</c:if>>${team.teamName}</option>
            </c:forEach>
        </select>
        <button onclick="return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button> =>
        <div class="selectedFilters">
            <div id="selectedList" style="display: none;">
            </div>
        </div>

        <div class="clear"></div>
        <div>
            <label><input type="radio" id="teamAll" name="teamSelectionType" value="0" checked="true"/><indaba:msg key='common.choice.exclude' /></label>
            <label><input type="radio" id="teamOnly" name="teamSelectionType" value="1"/><indaba:msg key='common.choice.onlyselected' /></label>
        </div>
    </div>
    <hr/>
    <table width="90%">
        <tr>
            <td>
                <div class="filter">
                    <h4><indaba:msg key='jsp.peopleFilter.caseAssociation' /></h4>
                    <div>
                        <label><input type="radio" name="hasCase" value="0" <c:if test="${hasCase==0}">checked</c:if> /><indaba:msg key='common.choice.all' /></label>
                        <label><input type="radio" name="hasCase" value="1" <c:if test="${hasCase==1}">checked</c:if> /><indaba:msg key='common.boolean.yes' /></label>
                        <label><input type="radio" name="hasCase" value="2" <c:if test="${hasCase==2}">checked</c:if> /><indaba:msg key='common.boolean.no' /></label>
                    </div>
                </div>
            </td>
            <td>
                <div class="filter">
                    <h4><indaba:msg key='jsp.peopleFilter.assignmentStatus' /></h4>
                    <div>
                        <label><input type="radio" name="status" value="0" <c:if test="${status==0}">checked</c:if> /><indaba:msg key='common.choice.all' /></label>
                        <label><input type="radio" name="status" value="1" <c:if test="${status==1}">checked</c:if> /><indaba:msg key='common.choice.notoverdue' /></label>
                        <label><input type="radio" name="status" value="2" <c:if test="${status==2}">checked</c:if> /><indaba:msg key='common.choice.overdue' /></label>
                    </div>
                </div>
            </td>
        </tr>
    </table>
</div>

