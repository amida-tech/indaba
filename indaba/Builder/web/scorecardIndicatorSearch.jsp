<%--
    Document   : scorecardIndicatorSearch
    Created on : 2010-5-21, 16:20:15
    Author     : william
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<indaba:view prjid="${prjid}" uid="${uid}" right="tag indicators">
    <div class="box" id="indicatorSearchBox">
        <h3><indaba:msg key='jsp.scorecardIndicatorSearch.tags' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
        <div class="content" style="padding: 15px;" align="center">
            <div id="indicatorSearchBar">
            </div>
        </div>
    </div>
</indaba:view>
<script type="text/javascript">
    searchIndicator(${horseid}, ${assignid});
</script>