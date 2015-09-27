<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<script type="text/javascript">

    function resetAllFilters() {
        document.getElementById("allExcpectProd").setAttribute("checked", "true");
        document.getElementById("onlySelectedProd").removeAttribute("checked");
        
        document.getElementById("allExcpectTarget").setAttribute("checked", "true");
        document.getElementById("onlySelectedTarget").removeAttribute("checked");

        document.getElementById("allStatus").setAttribute("checked", "true");
        document.getElementById("notOverdueStatus").removeAttribute("checked");
        document.getElementById("overdueStatus").removeAttribute("checked");
        
        return true;
    }
</script>
<div id="filters-box" class="box">
    <indaba:filter name="target" selectedIds="${targetId}" include="${targetSelectionType}" prjid="${prjid}"/>
    <hr/>
    <indaba:filter name="product" selectedIds="${productId}" include="${prodSelectionType}" prjid="${prjid}"/>
    <hr/>
    <indaba:filter name="status" status="${status}" />

    <p align="middle">
        <input type="reset" name="reset" class="small button blue" id="resetFilters" onclick='$(".removeSelection a").click();resetAllFilters();' value="<indaba:msg key='common.btn.reset' />"/>&nbsp;&nbsp;
        <input type="submit" name="apply" class="small button blue" value="<indaba:msg key='common.btn.apply' />"/>
    </p>
</div>

