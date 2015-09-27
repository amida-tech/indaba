function loadColumn(divId, result, width, height) {
    var myChart = new FusionCharts("FusionCharts/Column2D.swf", "Column2D", width, height);
    myChart.setDataXML(result);
    myChart.render(divId);
}

function loadHLinearGauge(divId, result, width, height) {
    var myChart = new FusionCharts("FusionCharts/HLinearGauge.swf", "HLinearGauge", width, height);
    myChart.setDataXML(result);
    myChart.render(divId);
}

function loadSparkline(divId, result, width, height) {
    var myChart = new FusionCharts("FusionCharts/SparkColumn.swf", "SparkColumn", width, height);
    myChart.setDataXML(result);
    myChart.render(divId);
}


