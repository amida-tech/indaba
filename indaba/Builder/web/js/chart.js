function loadChart(horseid) {
    var data = "horseid=" + horseid;

    $.ajax({
        type:"post",
        url:"loadHistoryChart.do",
        data: data,
        cache:false,
        async: false,
        success: function(result) {
            var myChart = new FusionCharts("charts/MSArea.swf", "MSArea", 640, 480);
            myChart.setDataXML(result);
            myChart.render("chartdiv");

            $.blockUI({
                message: $('#chartdiv'),
                css: {
                    top:            ($(window).height() - 480) /2 + 'px',
                    left:           ($(window).width() - 640) /2 + 'px',
                    textAlign:      'center',
                    color:          '#000',
                    border:         '3px solid #aaa',
                    backgroundColor:'#fff',
                    cursor:         'wait'
                }
            });
            $('.blockOverlay').attr('title',$.i18n.message('common.js.alt.unblock')).click($.unblockUI);
        }
    });
}


