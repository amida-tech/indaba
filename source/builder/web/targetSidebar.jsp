<%-- 
    Document   : targetSidebar
    Created on : 2010-5-9, 11:33:21
    Author     : Luke Shi
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<script type="text/javascript">

    var detailpage = null;

    function targetClicked(el, name)
    {
        var div = $(el).closest('div');
        if (!div.hasClass('target-box-item-clicked')) {
            div.addClass('target-box-item-clicked');
            var span = $('.target-box-item-value', div);
            span.show();
            postNames(name, span);
        }
    }

    function postNames(name, span) {
        var questionid = ${param.questionid};
        var horseid =${param.horseid};
        $.ajax({
            type: "POST",
            url: "setSpans.do",
            data: "targetname=" + name + "&horseid=" + horseid + "&questionid=" + questionid,
            success: function(result) {
                if (result.toString() === 'No Answer') {
                    span.addClass('target-box-no-answer');
                    span.html(result.toString());
                }
                else {
                    span.addClass('target-box-has-answer');
                    span.html("<a>" + result.toString() + "</a>");
                }
                //span.text(result.toString()+" <a>details</a>");
            },
            error: function(result) {
                //$("#status")[0].innerHTML = "Targets";
                //jAlert("ERROR during loading!");
            }
        });
    }

    function answerClick(el, name, id) {
        var span = $(el).closest('span');
        var horseid =${param.horseid};
        var questionid = ${param.questionid};
        if (span.hasClass('target-box-has-answer')) {
            if (detailpage !== undefined && get_cookie('popped') !== '') {
                detailpage.close();
            }
            detailpage = window.open('surveyAnswerDisplay.do?targetname=' + name + "&targetid=" + id + "&horseid=" + horseid + "&questionid=" + questionid, "yes");//"",winName,features);
            if (get_cookie('popped') === '') {
                document.cookie = "popped=yes";
            }
        }
    }

    function get_cookie(Name) {
        var search = Name + "=";
        var returnvalue = "";
        if (document.cookie.length > 0) {
            offset = document.cookie.indexOf(search);
            if (offset !== -1) {
                offset += search.length;
                end = document.cookie.indexOf(";", offset);
                if (end === -1)
                    end = document.cookie.length;
                returnvalue = unescape(document.cookie.substring(offset, end));
            }
        }
        return returnvalue;
    }
    $(function() {
        $('div.target-vertical-scrollbar').mCustomScrollbar({
            theme: 'dark-thick', autoDraggerLength: true, horizontalScroll: false
        });
    });
</script>

<indaba:view prjid="${prjid}" uid="${uid}" right="see target indicator values">
    <div id="targets-box" class="box">
        <h3><span id="status"><indaba:msg key='jsp.sidebar.h3' /></span><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
        <div class="content target-vertical-scrollbar">
            <c:forEach items="${targets}" var="t">
                <div class="target-box-item">
                    <span class="target-box-item-name" onclick="targetClicked(this, '${t.name}');" title="click to view answer">${t.name}</span>
                    &nbsp;&nbsp;
                    <span class="target-box-item-value" onclick="answerClick(this, '${t.name}', '${t.id}');" title="click to view details"><indaba:msg key='jsp.sidebar.loadanswer' /></span>
                </div>
            </c:forEach>
        </div>
    </div>
</indaba:view>