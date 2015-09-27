
function loadScorecardNavigation(params) {
    var parameters = {};
    parameters.horseid = params.horseid;
    parameters.assignid = params.assignid;
    parameters.action = params.action;
    parameters.returl = params.returl;
    parameters.contentVersionId = params.contentVersionId;
    $.post('scorecardNavigation.do', parameters,
    function(data) {
        var indabaDiv = document.getElementById("indaba");
        var wrapDiv = document.createElement("div");
        $(wrapDiv).addClass('wrapper');
        wrapDiv.innerHTML = data;
        indabaDiv.appendChild(wrapDiv);

        // script block of innerHTML does not execute in IE
        if ($.browser.msie){
            $(".tipTip").tipTip();
        }

        $.ajax({
            type: "POST",
            url: "scorecardIndicatorSearch.do",
            data: "horseid=" + parameters.horseid + "&assignid=" + parameters.assignid + "&action=" + parameters.action,
            success: function(result) {
                if (result.toString() == "") {
                    $("#indicatorSearchBox").remove();
                } else {
                    $("#indicatorSearch").remove();
                    $("#indicatorSearchContent").remove();
                    $("#indicatorSearchBar").after(result.toString());
                }
            },
            error: function(result) {
                alert(result);
            }
        });

        $('.box h3:not([bound])').has('a.toggleVisible')
                            .attr({bound:true})
                            .addClass('togglable')
                            .click(function(e){
            e.preventDefault();
            var content = $(this).parent().children(".content");

            if (content.css("display") == "none") {
                content.slideDown("fast");
                $('a.toggleVisible', this).html("<img src='images/collapse_icon.png' alt='collapse' />");
            } else {
                content.slideUp("fast");
                $('a.toggleVisible', this).html("<img src='images/expand_icon.png' alt='expand' />");
            }
        })

        buttonDeffered = false;
        $(document).trigger('ready');
    });
}


