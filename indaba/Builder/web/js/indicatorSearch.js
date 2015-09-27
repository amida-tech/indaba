
function clearTag() {
    document.getElementById("startWithTag").value = "";
    $("#indicatorSearch").remove();
    $("#indicatorSearchContent").remove();
}

function searchIndicator(horseid, assignid) {
    $.ajax({
        type: "POST",
        url: "scorecardIndicatorSearch.do",
        data: "horseid=" + horseid + "&assignid=" + assignid,
        success: function(result) {
            if (result.toString() == "") {
                $("#indicatorSearchBox").remove();
            } else {
                alert("1");
                $("#indicatorSearch").remove();
                $("#indicatorSearchContent").remove();
                $("#indicatorSearchBar").after(result.toString());
            }   
        },
        error: function(result) {
            alert(result);
        }
    });
}

function getIndicatorContent(data) {
    $.ajax({
        type: "POST",
        url: "scorecardIndicatorSearchContent.do",
        data: data + "&returl=" + location.href,
        success: function(result) {
            $("#indicatorSearchContent").remove();
            $("#indicatorSearch").after(result.toString());
        },
        error: function(result) {
            alert(result);
        }
    });
}
