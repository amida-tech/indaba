<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link type="text/css" rel="stylesheet" href="css/menu.css"/>
<!--<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>-->
<script type="text/javascript" src="./js/jquery.mousewheel-3.0.4.pack.js"></script>
<script type="text/javascript" src="./js/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="./js/jquery.urldecoder.min.js"></script>
<link rel="stylesheet" type="text/css" href="./css/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript">
    $(function() {
        $("ul.select li a").click(function() {
            $("ul.current").children("li").children("ul").attr("class", "sub");
            $("ul.current").attr("class", "select");
            $(this).parent("li").parent("ul").attr("class", "current");
            $(this).parent("li").children("ul").attr("class", "sub_active");
        });

        var cur_li;
        if (${active == null}) {
            cur_li = $("#dataexport");
        } else {
            cur_li = $("#${active}");
        }

        cur_li.attr("class", "current_sub");
        cur_li.parent("ul").attr("class", "sub_active");
        cur_li.parent("ul").parent("li").parent("ul").attr("class", "current");
    });
</script>

<div id="header" style="margin-top: 10px;" >
    <div id="indaba-logo"><a href="http://indabaplatform.com/ids/introduction"><img alt="Indaba Logo" title="Indaba Logo" src="images/IndabaPublisher-2.gif" /></a></div>
    <div id="nav">
        <ul class="select">
            <li><a href="introduction.do"><b>Home</b></a>
                <ul class="sub">
                    <li class="sub_li" id="home"><a href="introduction.do">Introduction</a></li>
                    <li class="sub_li" id="help"><a href="help.do">Help</a></li>
                </ul>
            </li>
        </ul>
        <ul class="select">
            <li><a href="createJournalExport.do?step=1"><b>Export</b></a>
                <ul class="sub">
                    <li class="sub_li" id="docexport"><a href="createJournalExport.do?step=1">Export Document Library</a></li>
                    <li class="sub_li" id="dataexport"><a href="createDataExport.do?step=1">Export Data Library</a></li>

                    <c:choose>
                    <c:when test="${!(empty uid) && (exportScorecardReportVisible)}">
                    <li class="sub_li" id="scorecardexport"><a href="createScorecardExport.do">Export Scorecard Report</a></li>
                    </c:when>
                    <c:otherwise>
                    <li class="sub_li" id="scorecardexport" style="display: none"><a id="scorecardexportlink" href="dummy">Export Scorecard Report</a></li>
                    </c:otherwise>
                    </c:choose>
                    <!--
                    <li class="sub_li" id=""><a href="#">Get Widget</a></li>
                    -->
                </ul>
            </li>
        </ul>
        <ul class="select">
            <li><a href="widgetlibrary.do"><b>Widget Library</b></a>
                <ul class="sub">
                    <li class="sub_li" id="widgetlibrary"><a href="widgetlibrary.do">Get Widget</a></li>
                    <c:choose>
                    <c:when test="${!(empty uid) && (manageWidgetVisible)}">
                        <li class="sub_li" id="managewidgets"><a href="managewidget.do?widgetId=1">Manage Widgets</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="sub_li" id="managewidgets" style="display: none"><a id="managewidgetslink" href="dummy">Manage Widgets</a></li>
                    </c:otherwise>
                    </c:choose>
                    <li class="sub_li" id="widgethowto"><a href="howto.jsp">How to</a></li>
                </ul>
            </li>
        </ul>
        <c:choose>
            <c:when test="${!(empty uid) && (manageWorksetVisible)}">
                <ul class="select" id="manageworksets">
                <li><a href="manageworkset.do?step=1"><b>Working Set</b></a>
                    <ul class="sub">                        
                        <li class="sub_li" id="manageworkset"><a href="manageworkset.do">Manage Working Sets</a></li>
                        <li class="sub_li" id="createworkset"><a href="createworkset.do">Create Working Set</a></li>
                    </ul>
                </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="select" id="manageworksets" style="display: none">
                <li><a href="manageworkset.do?step=1"><b>Working Set</b></a>
                    <ul class="sub">
                        <li class="sub_li" id="manageworkset"><a id="manageworksetlink" href="dummy">Manage Working Sets</a></li>
                        <li class="sub_li" id="createworkset"><a id="createworksetlink" href="dummy">Create Working Set</a></li>
                    </ul>
                </li>
                </ul>
            </c:otherwise>
        </c:choose>
            <!--<ul class="select">
                <li><a href="createaggregation.do?step=1"><b>Aggregation</b></a>
                    <ul class="sub">
                        <li class="sub_li" id="createaggr"><a href="createaggregation.do">Create Aggregation</a></li>
                        <li class="sub_li" id="manageaggr"><a href="manageaggregation.do">Manage Aggregations</a></li>
                    </ul>
                </li>
            </ul>-->
        <!--
        <ul class="select">
            <li><a href="help.jsp"><b>Help</b></a>
                <ul class="sub">
                    <li class="sub_li" id="help"><a href="help.jsp">Help</a></li>
                </ul>
            </li>
        </ul>
        -->
        <div id="userlogin">
            <c:choose>
                <c:when test="${(empty name)}">
                    <a id="login" href="#loginDiv">Login</a>
                </c:when>
                <c:otherwise>
                    Welcome <span>${name}</span>!&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a id="logout" href="logout.do">Log out</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<div style="display: none" >
    <div id="loginDiv">
        <jsp:include page="login_inc.jsp" flush="true" />
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("#login").fancybox({
            'titlePosition'		: 'outside',
            'width'                     : 200,
            'transitionIn'		: 'none',
            'transitionOut'		: 'none'
        });
    });
</script>