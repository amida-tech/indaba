<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Data Summary</title>
        <link rel="stylesheet" href="widgets/css/common.css" />
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="widgets/js/widget.common.js"></script>
        <script type="text/javascript" src="js/loadCharts.js"></script>
        <script type="text/javascript" src="js/FusionCharts.js"></script>
        <script type="text/javascript">
            function download() {
                $.getJSON("dataSummaryChart.do?size=large&genericLabel=${param.genericLabel}&horseId=${param.horseId}", {t:new Date()}, function(data) {
                    window.location.href = "highResDownload.do?filename=" + data.charts.substring(data.charts.lastIndexOf("/") + 1);
                });
            }
            
            $(function() {
                $.getJSON("dataSummaryChart.do?genericLabel=${param.genericLabel}&horseId=${param.horseId}", {t:new Date()}, function(data) {

                    if ("${param.size}" == "large") {
                        $(document.body).addClass('large');
                    }
                    
                    $("#chartDiv").empty();
                    if (data.charts == "No Data") {
                        $("<div>No Data</div>").appendTo("#chartDiv").load(function(){
                            $WIDGET.widgetResizePipe('DATA_SUMMARY');});
                        $("#highRes").hide();
                    } else {
                        $("<img/>", {
                            "id"    :   "chartImg",
                            "src"   :   data.charts,
                            "alt"   :   ""
                        }).appendTo("#chartDiv").load(function(){
                            $WIDGET.widgetResizePipe('DATA_SUMMARY');});
                        $("#highRes").show();
                    }
                    $WIDGET.widgetResizePipe('DATA_SUMMARY');
                });
            });
        </script>
    </head>
    <body>
        <div class="widget-container" style="width: 300px">
            <div id="chartDiv"></div>
            <br/>
            <div style="text-align:right">
                <input id="highRes" type="button" value="Get High-Res Image" onclick="download();" />
            </div>
        </div>
    </body>
</html>
