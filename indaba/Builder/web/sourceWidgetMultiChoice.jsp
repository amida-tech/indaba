<%-- 
    Document   : sourceWidgetMultiChoice
    Created on : 2010-5-10, 14:51:48
    Author     : Luke Shi
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<b><indaba:msg key='common.title.sources' />:</b>
<br>
<indaba:msg key='jsp.sourceWMChoice.sources.desc' /><br>
<br>
<table>
    <tr>
        <td><input type="checkbox" name="source" value="0" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.report' /></p></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="source" value="1" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.acadamic' /></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="source" value="2" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.govStudy' /></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="source" value="3" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.organic' /></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="source" value="4" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.govOfficial' /></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="source" value="5" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.interAcadamic' /></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="source" value="6" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.civilSociety' /></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="source" value="7" /></td>
        <td><p><indaba:msg key='jsp.sourceWMChoice.sources.journal' /></td>
    </tr>
</table>
<br><br>
<b><indaba:msg key='common.title.comments' />:</b>
<br>
<TEXTAREA NAME="comments" class="text" ROWS="6" onclick="resetStatus();"></TEXTAREA>
