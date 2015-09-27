<%@page pageEncoding="UTF-8" contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.newMessage.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/token-input-facebook.css"/>
        <style>
            ul.token-input-list-facebook{
                position:relative;
                width:640px;
            }
            div.token-input-dropdown-facebook{
                max-height:200px;
                overflow-y:auto;
                width:640px;
            }
            
            .icon-add-user{
                background:url(images/icon-user.png) no-repeat center center;
                cursor:pointer;
                height:22px;
                float:right;
                right:50px;
                margin:2px 2px 0 0;
                width:22px;
            }
            .icon-add-role{
                background:url(images/icon-role.png) no-repeat center center;
                cursor:pointer;
                height:22px;
                float:right;
                right:26px;
                margin:2px 2px 0 0;
                width:22px;
            }
            .icon-add-team{
                background:url(images/icon-team.png) no-repeat center center;
                cursor:pointer;
                height:22px;
                float:right;
                right:2px;
                margin:2px 2px 0 0;
                width:22px;
            }
        </style>
        <script src="${contextPath}/js/xmlhttp.js" type="text/javascript"></script>
        <script src="${contextPath}/js/jquery-1.7.1.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script src="${contextPath}/js/jquery.tokeninput.js" type="text/javascript"></script>
        <script type="text/javascript" src="${contextPath}/js/toggleDisplay.js"></script>
        <script type="text/javascript">
            var settings = {
                    contentType:'html',
                    hintText: $.i18n.message('common.js.token.hint'),
                    noResultsText: $.i18n.message('common.js.token.noResult'),
                    searchingText: $.i18n.message('common.js.token.search'),
                    classes: {
                        tokenList: "token-input-list-facebook",
                        token: "token-input-token-facebook",
                        tokenDelete: "token-input-delete-token-facebook",
                        selectedToken: "token-input-selected-token-facebook",
                        highlightedToken: "token-input-highlighted-token-facebook",
                        dropdown: "token-input-dropdown-facebook",
                        dropdownItem: "token-input-dropdown-item-facebook",
                        dropdownItem2: "token-input-dropdown-item2-facebook",
                        selectedDropdownItem: "token-input-selected-dropdown-item-facebook",
                        inputToken: "token-input-input-token-facebook"
                    }
                };
        </script>
        <c:if test="${receiver ne null}">
	    	<script type="text/javascript">
                    settings.prePopulate = [{
                                    id:'${receiver.userId}',
                                    name:'${receiver.displayUsername}',
                                    listType:'user',
                                    inputName:'receiverIds'
                            }];
	    	</script>
    	</c:if>
        <script type="text/javascript">
            $(function(){
                $("#tokenize").tokenInput("${contextPath}/userlist.do", settings);               
                <indaba:view prjid="${prjid}" uid="${uid}" right="write to teams">
                 $('<div/>').addClass('icon-add-team')
                            .attr('title', $.i18n.message('common.js.hint.team.list'))
                            .appendTo('.token-input-list-facebook')
                            .click(function(){
                                   $('input', '.token-input-list-facebook').trigger('keydown', [{type:'team', url:'${contextPath}/teamlist.do'}])
                            });
                </indaba:view>
                <indaba:view prjid="${prjid}" uid="${uid}" right="write to roles">
                $('<div/>').addClass('icon-add-role')
                           .attr('title', $.i18n.message('common.js.hint.role.list'))
                           .appendTo('.token-input-list-facebook')
                           .click(function(){
                               $('input', '.token-input-list-facebook').trigger('keydown', [{type:'role', url:'${contextPath}/rolelist.do'}])
                           });
                </indaba:view>
                $('<div/>').addClass('icon-add-user')
                           .attr('title', $.i18n.message('common.js.hint.user.list'))
                           .appendTo('.token-input-list-facebook')
                           .click(function(){
                               $('input', '.token-input-list-facebook').trigger('keydown', [{type:'user', url:'${contextPath}/userlist.do'}])
                           });
            });
			
            function validateInput() {
                var b = true;
                if (document.forms[0].title.value == "") {
                    $('.prompt').filter('.subject').show('fast');
                    b = false;
                } else {
                    $('.prompt').filter('.subject').hide('fast');
                }

                if (document.forms[0].body.value == "") {
                    $('.prompt').filter('.message').show('fast');
                    b = false;
                } else {
                    $('.prompt').filter('.message').hide('fast');
                }

                if ($('li[listType]', '.token-input-list-facebook').size() == 0
                    && ($('#stpmb').size() == 0 || ($('#stpmb').size() > 0 && !$("#stpmb").attr('checked')))) {
                    $('.prompt').filter('.to').show('fast');
                    b = false;
                }
                else
                    $('.prompt').filter('.to').hide('fast');

                // create hidden fields
                $('li[listType]', '.token-input-list-facebook').each(function(index, el){
                    $('<input/>').attr({type:'hidden', value:$(el).attr('listId'), name:$(el).attr('inputName')})
                                 .appendTo('.token-input-list-facebook');
                });
                
                return b;
            }
        </script>
        
    </head>
	<body>
	    <div id="indaba">
            <c:set var="active" value="messaging" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper message">
            	<form action="${contextPath}/createmsg.do" method="post">
	            	<input type="hidden" name="action" value="create"/>
                        <h3 class="newMessage"><indaba:msg key='jsp.newMessage.newMsg' />:</h3>
                        <div id="messageHeader" class="item">
                            <label><indaba:msg key='common.msg.to' />:</label>
                            <input type="text" id="tokenize" name="blah" /><br/>
	                        <indaba:view prjid="${prjid}" uid="${uid}" right="write project wall">
                                    <input type="checkbox" id="stpmb" name="sendToProjectMsgboard"/><indaba:msg key='jsp.newMessage.projectUpdate' />
                                </indaba:view>
                                <div class="prompt to">
                                    <indaba:msg key='common.alert.noreceiver' />
                                </div>
	                </div>
	                <div class="messageBody">
	                    <div class="item">
	                        <label><indaba:msg key='common.title.from' />:</label>
	                        <a href="${contextPath}/profile.do?targetUid=${sender.id}">${sender.firstName}&nbsp;${sender.lastName}</a>
	                    </div>
	                    <div class="item">
	                        <label><indaba:msg key='common.label.subject' />: </label>
	                        <input type="text" name="title" size="100" maxlength="90"/>
                                <div class="prompt subject">
                                    <indaba:msg key='common.alert.nosubject' />
                                </div>
	                    </div>
	                    <div class="item">
	                        <label><indaba:msg key='common.label.message' />: </label>
                                <textarea id="body" name="body" class="ckeditor" rows="20" cols="97"></textarea>
                                <div class="prompt message">
                                    <indaba:msg key='common.alert.nomessage' />
                                </div>
	                    </div>
                            <div class="item last-item">
                                <label>&nbsp;</label>
                                <input class="large button blue" type="submit" value=" <indaba:msg key='common.btn.sendmsg' /> " onclick="document.charset='UTF-8'; return validateInput();"/>
                            </div>
	                </div>
                </form>
            </div>
         </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>