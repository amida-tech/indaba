<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sparkline</title>
        <link rel="stylesheet" href="widgets/css/common.css" />
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="widgets/js/widget.common.js"></script>
        <script type="text/javascript" src="js/loadCharts.js"></script>
        <script type="text/javascript" src="js/FusionCharts.js"></script>
        <script type="text/javascript">
            $(function() {
                $.getJSON("sparklineChart.do?productId=${param.productId}&horseId=${param.horseId}", {t:new Date()}, function(data) {

                    var base = data.base;
                    $("#title").text(base.surveyName);
                    $("#studyPeriod").text(base.studyPeroid);
                    
                    var charts = eval(data.charts);
                    var chart_width;
                    if (base.charts.length > 0) {
                        chart_width = (base.charts[0].values.length + 2) * 3 + 12;
                    } else {
                        return;
                    }
                    for (i = 0; i < charts.length; i++) {
                        var html = '<div class="sparkline-item">';
                        html += '<label>'+base.charts[i].targetName+'</label>';
                        html += '<div id="chart_target_'+i+'"/>';
                        html += '</div>';
                        $('#charts').append(html);

                        if (charts[i].charAt(0) == '.') {
                            $("<img/>", {
                                "id"    :   "chart_target_" + i + "_chart",
                                "src"   :   charts[i],
                                "alt"   :   "",
                                "width" :   chart_width + "px",
                                "height":   "25px"}).appendTo("#chart_target_" + i);
                        } else {
                            loadSparkline("chart_target_" + i, "chart_target_" + i + "_chart", charts[i], chart_width, 13 + 12);
                        }
                    }
                    $(".sparkline-item").css("width", chart_width + 100 + 10 + "px");

                    $("#sparkline").show();
                    $WIDGET.widgetResizePipe('SPARKLINE');
                });
            });
        </script>
    </head>
    <body>
        <div class="widget-container">
            <div id="exeTime" style="display: none">
                <span id="start" style="display: none"></span>
                Load json : <span id="getJson"></span> ms<br />
                Show basic info : <span id="getBase"></span> ms<br />
                Load charts: <span id="end"></span> ms<br /><br />
            </div>
            <div id="sparkline" style="display: none">
                <div id="baseInfo">
                    <span id="title"></span><br />
                    <span id="studyPeriod"></span><br /><br />
                </div>
                <div id="charts"></div>
            </div>
        </div>
    </body>
</html>
