$j(function(){
    behaveToggleVisibleForCase();
});

function behaveToggleVisibleForCase(){
    // expand/hide this
    $j('.box h3:not([bound])').has('a.toggleVisible')
                            .attr({bound:true})
                            .addClass('togglable')
                            .click(function(e){
        e.preventDefault();
        var content = $j(this).parent().children(".content");

        if (content.css("display") == "none") {
            content.slideDown("fast");
            $j('a.toggleVisible', this).html("<img src='images/collapse_icon.png' alt='collapse' />");
        } else {
            content.slideUp("fast");
            $j('a.toggleVisible', this).html("<img src='images/expand_icon.png' alt='expand' />");
        }
    })
}