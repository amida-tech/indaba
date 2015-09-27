<%-- 
    Document   : notifications-view-inc.jsp
    Created on : Feb 12, 2014, 2:21:11 PM
    Author     : ningshan
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/ui.multiselect.css">
<div id="notif-view-popup" title="<indaba:msg key='cp.title.add_notification' />" class="hidden">
    <div id="notificationView" class="popup-container">
        <form id="notif-view-form" action="#" method="POST" onsubmit="return false;">
            <fieldset>
                <dl>
                    <dt><label for="name"><indaba:msg key="cp.label.name" /></label></dt>
                    <dd><input type="text" name="vwName" maxlength="100" id="vwName" class="short-input" disabled="true"/></dd>
                </dl>
                <dl>
                    <dt><label for="type"><indaba:msg key="cp.label.type" /></label></dt>
                    <dd><input type="text" name="vwType" maxlength="100" id="vwType" class="short-input" disabled="true"/></dd>
                </dl>
                <dl>
                    <dt><label for="notifilanguage"><indaba:msg key="cp.label.language" /></label></dt>
                    <dd><input type="text" name="vwLanguage" maxlength="100" id="vwLanguage" class="short-input" disabled="true"/></dd>
                </dl>
                <dl>
                    <dt><label for="subject"><indaba:msg key="cp.label.subject" /></label></dt>
                    <dd><input type="text" name="vwSubject" maxlength="200" id="vwSubject" class="long-input" disabled="true"/></dd>
                </dl>                    
                <dl id="body-dl">
                    <dt><label for="notifibody"><indaba:msg key="cp.label.body" /></label></dt>
                    <dd><textarea id="vwNotifibody" name="vwNotifibody" class="long-input" disabled="true"></textarea></dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>    
<script type="text/javascript" charset="utf-8">
    $(function() {
        var ntdialog = $('#notif-view-popup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            cache: false,
            open: function(){

            },
            close: function(){
            },
            buttons: {
                "OK": function(){
                    ntdialog.dialog("close");
                }
            }
        });
                
    });
    
</script>