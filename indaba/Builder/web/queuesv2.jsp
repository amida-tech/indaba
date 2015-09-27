<%-- 
    Document   : queues
    Created on : Feb 22, 2010, 9:34:20 PM
    Author     : menglong luwb
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.queues.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <link type="text/css" rel="stylesheet" href="plugins/chosen/chosen.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/dialog/ocs-dlg.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="plugins/chosen/jquery.chosen.js"></script>
        <script type="text/javascript" src="plugins/dialog/ocs-dlg.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/indaba.queues.js"></script>
    </head>
    <body>
        <div id="indaba">
            <!--c:set var="active" value="queues" scope="request"/-->
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper" style="overflow:visible" >
                <c:if test="${not empty prdList}">
                    <div class="filter">
                        <fieldset style="padding: 10px 20px;">
                            <legend class="head"><indaba:msg key='common.label.queue_action'/></legend>
                            <input name="viewmode" value="1" type="radio" checked onclick="showAllTasks()" /><indaba:msg key="jsp.msg.show.all.tasks"/>&nbsp;&nbsp;&nbsp;
                            <input name="viewmode" value="0" type="radio" onclick="showStartingTasks()"/><indaba:msg key="jsp.msg.show.starting.tasks"/>
                        </fieldset>
                    </div>

                  <div class="filter" id="queueFilter">
                    <form action="queuesv2.do" method="POST">
                        <fieldset style="padding: 10px 20px;">
                            <legend class="head"><indaba:msg key='common.label.queue_filter'/></legend>
                            <dl>
                                <dt><label><indaba:msg key="jsp.queueFilter.product"/></label></dt>
                                <dd>
                                    <select name="productId" data-placeholder="<indaba:msg key='common.msg.click_to_choose_product'/>" class="long-input">
                                        <option value=""></option>
                                        <c:forEach items="${prdList}" var="prod" varStatus="status" >
                                            <option value="${prod.id}" <c:if test="${status.first}">selected="selected"</c:if>>${prod.name}</option>
                                        </c:forEach>
                                    </select>
                                    <span class="error"><indaba:msg key='common.err.must_choose_product'/></span>
                                </dd>
                            </dl>
                            <dl>
                                <dt><label><indaba:msg key="jsp.queueFilter.task" /></label></dt>
                                <dd>
                                    <select name="taskId" data-placeholder="<indaba:msg key='common.msg.click_to_choose_task'/>" class="long-input">
                                        <option value=""></option>
                                        <c:forEach items="${taskList}" var="task" varStatus="status" >
                                            <option value="${task.id}" <c:if test="${status.first}">selected="selected"</c:if>>${task.taskName}</option>
                                        </c:forEach>
                                
                                    </select>
                                </dd>
                            </dl>
                            <dl>
                                <dt><span title='<indaba:msg key="jsp.queueFilter.assignmentStatusExp"/>'><label><indaba:msg key="jsp.queueFilter.assignmentStatus"/></label></span></dt>
                                <dd style="line-height: 22px;">
                                    <input name="status" value="0" type="radio" checked /><indaba:msg key="common.choice.all"/>&nbsp;&nbsp;&nbsp;
                                    <input name="status" value="1" type="radio" /><indaba:msg key="common.label.over"/>&nbsp;&nbsp;&nbsp;
                                    <input name="status" value="2" type="radio" /><indaba:msg key="common.label.inflight"/>&nbsp;&nbsp;&nbsp;
                                    <input name="status" value="3" type="radio" /><indaba:msg key="common.label.starting"/>&nbsp;&nbsp;&nbsp;
                                    <input name="status" value="4" type="radio" /><indaba:msg key="common.label.inactive"/>&nbsp;&nbsp;&nbsp;
                                </dd>
                                <dt><label><indaba:msg key="jsp.queueFilter.assignedStatus"/></label></dt>
                                <dd style="line-height: 22px;">
                                    <input name="assignstatus" value="0" type="radio" checked /><indaba:msg key="common.choice.all"/>&nbsp;&nbsp;&nbsp;
                                    <input name="assignstatus" value="1" type="radio" /><indaba:msg key="common.label.assigned"/>&nbsp;&nbsp;&nbsp;
                                    <input name="assignstatus" value="2" type="radio" /><indaba:msg key="common.label.not_assigned"/>&nbsp;&nbsp;&nbsp;
                                </dd>
                            </dl>                       
                            <dl>
                                <dt><label><indaba:msg key="jsp.queueFilter.target" /></label></dt>
                                <dd>
                                    <select name="targetIds" data-placeholder="<indaba:msg key='common.msg.click_to_add_target'/>" multiple class="long-input">
                                        <option value=""></option>
                                        <c:forEach items="${targetList}" var="target" varStatus="status" >
                                            <option value="${target.id}">${target.name}</option>
                                        </c:forEach>
                                    </select>
                                </dd>
                            </dl>                   
                            <div class="buttons" >
                                <input class="small button blue btn-submit-filter" type="submit" value="<indaba:msg key='common.btn.refresh'/>" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </div>
                        </fieldset>
                    </form>
                  </div>
                </c:if>
                <div class="results">
                    <div class="nodata"><indaba:msg key='jsp.queues.noQuenes' /></div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>

<c:if test="${not empty prdList}">
<script type="text/javascript">
    $(function(){
        var filterForm = $('.filter form');
        //resetForm(filterForm);
        findQueues(filterForm);
        $('select', filterForm).chosen();
        $('input:radio').uniform();
        $('select[name=productId]', filterForm).change(function(){
            var productId = $(this).val();
            if(productId == ''){
                $('.error', filterForm).show();
                return false;
            }
            $('.error', filterForm).hide();
            $('select[name=taskId],select[name=targetIds]', filterForm).each(function(){
                $('option', $(this)).removeAttr('selected');
                $(this).trigger("liszt:updated");
            });

            $.ajax({
                url:'queuesv2.do', type: 'POST',dataType: 'json',data: {action: 'filterOptions', productId: productId},
                success: function(resp){
                    setChosenOptions($('select[name=taskId]', filterForm), resp.tasks, true);
                    setChosenOptions($('select[name=targetIds]', filterForm), resp.targets);
                    $('.btn-submit-filter', filterForm).trigger('click');
                }
            });          
        });
        
        $('select[name=taskId], select[name=targetIds], input', filterForm).change(function(){
            $('.btn-submit-filter', filterForm).trigger('click');
        });

        $('.btn-submit-filter', filterForm).click(function(){
            findQueues(filterForm);
            return false;
        });
    });


    function showStartingTasks() {
        $("#queueFilter").hide();
        
        var formData = {};
        formData["tasktype"] = "starting";
        findFilteredQueues(formData);
        return false;
    }

    function showAllTasks() {
        $("#queueFilter").show();
        var filterForm = $('.filter form');
        findQueues(filterForm);
        return false;
    }


    function findFilteredQueues(formData) {
        $.ajax({
            url:'queuesv2.do',
            type: 'POST',
            dataType: 'json',
            data: $.extend(formData, {action: 'findQueues'}),
            success: function(data){
                if(data.qtList && data.qtList.length > 0) {
                    $.each(data.qtList, function(i, item){
                        createTaskItem(${uid}, data.isPM, data.productId, item);
                    });
                    if($('div.results div').length == 0){
                        $('div.results').append("<div class='nodata'><indaba:msg key='jsp.queues.noQuenes'/></div>");
                    }
                } else {
                    $('div.results').append("<div class='nodata'><indaba:msg key='jsp.queues.noQuenes'/></div>");
                }
            },
            beforeSend: function(){
                $('div.results').empty();
                $('div.results').append('<div class="loading"><img src="images/ajax-loader.gif"/></div>')
            },
            complete: function(){
                $('div.results .loading').remove();
            },
            error: function(){
                $('div.results .loading').remove();
            }
        });
    }

    function findQueues(filterForm) {
        if($('select[name=productId]', filterForm).val() == ''){
            $('.error', filterForm).show();
            return false;
        }
        $('.error', filterForm).hide();
        var formData = extractFormDataToJson(filterForm);
        findFilteredQueues(formData);
    }
    
</script>
</c:if>