<%--
    Document   : Project Tab - 'Organizations'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="projectOrgsTab">
    <form id="projectOrgsForm" action="#" method="POST">
        <fieldset class="block">
            <input type="hidden" name="projId" id="projId" value="${projId}"/>
            <input type="hidden" name="updateField" id="updateField"  value="secondaryOrgs"/>
            <dl>
                <dt><label for="secondaryOrgs"><indaba:msg key="cp.label.secondary_owner"  /></label></dt>
                <dd>
                    <select name="secondaryOrgs" id="secondaryOrgs" multiple data-placeholder="<indaba:msg key='cp.text.choose_secondary_owner'  />" class="long-input">
                        <option value=""></option>
                        <c:forEach items="${orgs}" var="org">
                            <option value="${org.id}" <c:if test="${not isSA}">deletable="false"</c:if>>${org.name}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${not isSA}">
                        <div class="viewonly-note"><span>You can view it ONLY. Please contact the system admin to edit it if necessary.</span></div>
                    </c:if>
                </dd>
            </dl>
        </fieldset>
    </form>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        var orgsForm = $('#projectOrgsForm');
        if($('#result', orgsForm).length == 0) {
            doAjaxChange($('#secondaryOrgs', orgsForm), '${contextPath}/proj/projects!updateData', function(){
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '${contextPath}/proj/projects!getAdminSelectionList',
                    data: {projId: ${projId}},
                    success: function(resp){
                        if(resp.data) {
                            var elem = $('#secondaryAdmins');
                            var selectedAdmins = elem.val();
                            elem.empty();
                            $.each(resp.data, function(index, item){
                                elem.append('<option value="'+item.id+'">'+item.firstName + ' ' + item.lastName +'</option>');
                            })
                            selectChosenOptions(elem, selectedAdmins);
                            disableChosenOption(elem, $('#primaryAdmin').val());
                        }
                    }
                });
            });
        }
    });
</script>