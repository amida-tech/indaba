(function($){
    $(document).ready(function(){
        behaveToggleVisible();
    });
})(jQuery);

function behaveToggleVisible(){
    // expand/hide this
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
}