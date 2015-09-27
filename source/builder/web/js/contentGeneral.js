
function loadContentGeneral(horseid, assignid) {
    $.post('cntgeneral.do', {
        horseid: horseid, assignid: assignid
    },
    function(data) {
        var indabaDiv = document.getElementById("indaba");
        var wrapDiv = document.createElement("div");
        wrapDiv.innerHTML = data;
        var children_len = $('.wrapper', wrapDiv).children().length;

        indabaDiv.insertBefore(wrapDiv.lastChild, document.getElementById("mainheader").nextSibling);

        $(document).trigger('commonWidgetReady', [children_len]);

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
    });
}
