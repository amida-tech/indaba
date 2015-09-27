<%-- 
    Document   : indicatorAddTag
    Created on : 2010-5-17, 11:55:48
    Author     : luke
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<script type="text/javascript">
    function submitAddTag(answerId, contentobjectId, action, tag){
        // some cases for scorecard display, new tag block is not displayed
        if ($('input[name=new_tag]').size() == 0)
            return;

        String.prototype.trim = function(){
            return this.replace(/(^\s*)|(\s*$)/g,"");
        }
        if (tag == "")
            tag = $('input[name=new_tag]').val().toLowerCase().trim();
        var allData = "new_tag=" + tag + "&answerid=" + answerId +
                      "&contentobjectid=" + contentobjectId +
                      "&action=" + action;

        $.ajax({
            type: "POST",
            url: "tag.do",
            data: allData,
            cache: false,
            async: false,
            success: function(result) {
                document.getElementById("tags").innerHTML = result;
            }
        });
    }

    function getKey(event){
        if (event.keyCode == 13) {
            document.getElementById("addButton").click();
        }
    }
</script>
<jsp:include page="indicatorAddTag.do" flush="true"/>

<indaba:view prjid="${prjid}" uid="${uid}" right="tag indicators">
<div class="box">
    <h3><indaba:msg key='jsp.indicatorTag.instruction' /> <a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
    <div class="content" style="padding:0 0 28px;position:relative;">
        <div id="tags" style="padding:10px;">
        </div>
        <div style="position:absolute;bottom:0;left:0;background-color:#eee;width:100%;padding:4px 0;">
            &nbsp;&nbsp;
            <input type="text" class="text" name="new_tag" size="74" value="" onkeypress="getKey(event)" style="background:#fff">
            &nbsp;&nbsp;
            <input type="button" class="small button blue icon-add" id="addButton" name="addButton" value="<indaba:msg key='jsp.indicatorTag.buttonAddTag' />"
                           onclick="submitAddTag(${answerid},${contentobjectid},0,'');"/>
        </div>
    </div>
</div>
</indaba:view>
<script type="text/javascript">
    submitAddTag(${answerid},${contentobjectid},0,"");
</script>
