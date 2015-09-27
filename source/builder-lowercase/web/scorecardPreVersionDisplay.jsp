<%--
    Document   : surveyPreVersionDisplay
    Created on : 2013-1-24, 16:54:35
    Author     : Jeff Jiang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Cache-Control" content="no-cache" />
        <meta http-equiv="Expires" content="0" />
        <title><indaba:msg key='jsp.scorecardDisplay.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="plugins/chosen/chosen.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
        <link type="text/css" rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" media="screen" />
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css"/>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/tipTip.css"/>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>
        <script type="text/javascript" src="plugins/chosen/jquery.chosen.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.tipTip.js"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/surveyReview.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script type="text/javascript" src="js/scorecardNavigation.js"></script>
        <script type="text/javascript" src="js/indicatorSearch.js"></script>
        <script type="text/javascript" src="js/jquery.alerts.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js"></script>

        <script type="text/javascript">
            $(function() {
                $('a.send-popup').each(function(){
                    var sendBox =  $('#send-box');
                    $(this).fancybox({
                        onStart: function(){
                            resetRespondentFilter(sendBox, '${target.id}');
                        }
                    })
                });
            });

            $(document).bind('commonWidgetReady', function(event, num){
                if (num == 0){
                    $('#cntgeneral').remove();
                    $('hr').each(function(index, hr){
                        if ($(hr).prev('.wrapper').children().length == 0){
                            $(hr).prev('.wrapper').remove();
                            $(hr).hide();
                        }
                    })
                }
            })

            buttonDeffered = true;

            function checkChecked() {
                var contents = "";
                $('input[name=content]:checked').each(function(){
                    contents += $(this).val();
                });

                if (contents == "") {
                    document.getElementById("submitBtn").style.visibility = 'hidden';
                } else {
                    document.getElementById("submitBtn").style.visibility = 'visible';
                }
            }
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <jsp:include page="discussion.jsp" flush="true" >
                    <jsp:param name="checkPermission" value="1"/>
                    <jsp:param name="viewMode" value="preVersion"/>
                    <jsp:param value="1" name="folded"/>
                </jsp:include>
                <jsp:include page="surveyStaffDiscussion.jsp" flush="true" >
                    <jsp:param name="checkPermission" value="1"/>
                    <jsp:param name="viewMode" value="preVersion"/>
                    <jsp:param value="1" name="folded"/>
                </jsp:include>
            </div>
            <hr/>
            <div class="wrapper">
                <script type="text/javascript">
                    loadScorecardNavigation({horseid: "${horseid}", assignid: "0", action: "preVersionDisplay", returl: "${returl}", contentVersionId: "${contentVersionId}"});
                </script>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>