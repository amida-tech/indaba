<%-- 
    Document   : sourceWidgetText
    Created on : 2010-5-10, 14:51:22
    Author     : Luke Shi
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<b><indaba:msg key='common.title.sources' />:</b>
<br>
<indaba:msg key='jsp.sourceWidgetText.sourcedesc' />
<br>
<TEXTAREA NAME="sources" class="text" ROWS="6" onclick="resetStatus();"></TEXTAREA>
<br><br>
<b><indaba:msg key='common.title.comments' />:</b>
<br>
<TEXTAREA NAME="comments"  class="text" ROWS="6" onclick="resetStatus();"></TEXTAREA>
