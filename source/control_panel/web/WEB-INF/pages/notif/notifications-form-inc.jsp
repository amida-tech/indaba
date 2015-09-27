<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/ui.multiselect.css">
<div id="notifications-popup" title="<indaba:msg key='cp.title.add_notification' />" class="hidden">
    <div id="notificationsFormBox" class="popup-container">
        <form id="notifications-form" action="#" method="POST" onsubmit="return false;">
            <fieldset>
                <input type="hidden" name="mode" id="mode" value="ADD"/>
                <input type="hidden" name="notifiId" id="notifiId" value="-1"/>
                <dl>
                    <dt><label for="name"><indaba:msg key="cp.label.name" /></label></dt>
                    <dd><input type="text" name="name" maxlength="100" id="name" class="short-input validate[required]"/><span id="userHint" style="margin-left: 10px;color: green"></span></dd>
                </dl>
                <dl>
                    <dt><label for="type"><indaba:msg key="cp.label.type" /></label></dt>
                    <dd>
                        <select name="notifitype" id="notifitype" data-placeholder="<indaba:msg key='cp.text.choose_type'  />" class="short-input validate[required]" onchange="loadtokens(this.value);
                return false;">
                            <option value=""></option>
                            <c:forEach var="ntype" items="${notificationtypes}">
                                <option value="${ntype.id}">${ntype.name}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="notifilanguage"><indaba:msg key="cp.label.language" /></label></dt>
                    <dd>
                        <select name="notifilanguage" id="notifilanguage" data-placeholder="<indaba:msg key='cp.text.choose_language'  />" class="short-input validate[required]">
                            <option value=""></option>
                            <c:forEach var="lang" items="${languages}">
                                <option value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="subject"><indaba:msg key="cp.label.subject" /></label></dt>
                    <dd><input type="text" name="subject" maxlength="200" id="subject" class="long-input validate[required]"/>
                        <span id="subjectHint" style="margin-left: 10px;color: red"></span></dd>
                </dl>                    
                <dl id="body-dl">
                    <dt><label for="notifibody"><indaba:msg key="cp.label.body" /></label></dt>
                    <dd>
                        <textarea id="notifibody" name="notifibody" class="long-input validate[required]"></textarea>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="tokens"><indaba:msg key="cp.label.tokens" /></label></dt>
                    <dd><textarea id="tokens" class="tokenset long-input" disabled="disabled" rows="8"></textarea></dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>    
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.tablednd.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery.localisation.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery.scrollTo.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/ui.multiselect.js"></script>
<script type="text/javascript" charset="utf-8">
            var categories = {};
            var type2cat = {};
            var initilized = false;

            $(function() {
                if (!initilized) {
                    init();
                    initilized = true;
                }

        $('input').uniform();
        //$('select').uniform();
        $('textarea').uniform();
        //$('form#notifications-form select').chosen();
        $('#notifications-form').validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
        var ntdialog = $('#notifications-popup').dialog({
            autoOpen: false,
            width: 662,
            resizable: false,
            modal: true,
            context: $(this),
            cache: false,
            open: function(){

                    },
                    close: function() {
                        reset_notification_form();
                    },
                    buttons: {
                        "Save": function() {
                            if (!$('#notifications-form').validationEngine('validate')) {
                                return false;
                            }
                            $.ajax({
                                url: '${contextPath}/notif/notifications!update',
                                type: 'POST',
                                dataType: 'json',
                                data: {name: $('#name').val(),
                                    mode: $('#mode').val(),
                                    notifiId: $('#notifiId').val(),
                                    typeId: $('#notifitype').val(),
                                    languageId: $('#notifilanguage').val(),
                                    subject: $('#subject').val(),
                                    notifibody: $('#notifibody').val()},
                                success: function(data) {
                                    if (data['ret'] == 0) {
                                        ntdialog.dialog("close");
                                        $('#notifications-grid').flexReload({dataType: 'json'});
                                    }
                                    else {
                                        ocsError(data['desc']);
                                    }
                                }
                            });
                        },
                        "Cancel": function() {
                            ntdialog.dialog("close");
                            //reset_notification_form();
                        }
                    }
                });

            });

            function loadtokens(value) {
                if (value <= 0)
                    $('#tokens', $('#notifications-form')).val("");
                else
                    $('#tokens', $('#notifications-form')).val(categories[type2cat[value].category].tokens);
                $("#tokens", $('#notifications-form')).attr("disabled", true);
                // $.uniform.update();
            }

            function reset_notification_form() {
                $('#name', $('#notifications-form')).val('');
                $('#subject', $('#notifications-form')).val('');
                $('#notifibody', $('#notifications-form')).val('');
                $('#notifiId', $('#notifications-form')).val('');
                $('#tokens', $('#notifications-form')).val("");
                //$('#notifications-form').validationEngine('hide');
                $("select#notifilanguage option").each(function() {
                    $(this).removeAttr('disabled').trigger("liszt:updated");
                });
                selectChosenOptions($('select#notifitype', $('#notifications-form')), "");
                selectChosenOptions($('select#notifilanguage', $('#notifications-form')), "");
            }

            function init() {
                $.ajax({
                    url: '${contextPath}/notif/notifications!getTypeTokens',
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, textStatus, jqXHR) {
                        if (data.ntypes) {
                            for (var i = 0; i < data.ntypes.length; ++i) {
                                type2cat[data.ntypes[i].id] = {category: data.ntypes[i].category};
                            }
                        }
                        if (data.categories) {
                            for (var i = 0; i < data.categories.length; ++i) {
                                categories[data.categories[i].category] = {
                                    id: data.categories[i].id,
                                    tokens: data.categories[i].tokens};
                            }
                        }
                    }
                });

            }

            function excludeDisableChosenOption(id) {
                if (id === 1) {
                    $("#notifilanguage option").each(function() {
                        if ($(this).val() !== id)
                            $(this).attr('disabled', 'disabled').trigger("liszt:updated");
                    });
                }
            }

</script>