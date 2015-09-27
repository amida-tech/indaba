(function($) {
    $(document).ready(function() {
        behaveToggleVisible();
    });
})(jQuery);

function behaveToggleVisible($node, afterExpandCallback, afterCollapseCallback) {
    var $toggleNode = $node ? $node.find('h3:not([bound])') : $('.box h3:not([bound])');
    // expand/hide this
    $toggleNode.has('a.toggleVisible')
            .attr({bound: true})
            .addClass('togglable')
            .click(function(e) {
        e.preventDefault();
        var content = $(this).parent().children(".content");

        if (content.css("display") === "none") {
            content.slideDown("fast");
            $('a.toggleVisible', this).html("<img style='vertical-align:middle' src='images/collapse_icon.png' alt='collapse' />");
            if (afterExpandCallback) {
                afterExpandCallback(content);
            }
        } else {
            content.slideUp("fast");
            $('a.toggleVisible', this).html("<img style='vertical-align:middle' src='images/expand_icon.png' alt='expand' />");
            if (afterCollapseCallback) {
                afterCollapseCallback(content);
            }
        }
    });

    $toggleNode.has('a.toggleFilter')
            .attr({bound: true})
            .addClass('togglable')
            .click(function(e) {
        e.preventDefault();
        var content = $(this).parent().children(".content");

        if (content.css("display") === "none") {
            content.slideDown("fast");
            $('a.toggleFilter', this).html($.i18n.message('common.filter.clickclose') + "&nbsp; <img style='vertical-align:middle' src='images/collapse_icon.png' alt='collapse' />");
        } else {
            content.slideUp("fast");
            $('a.toggleFilter', this).html($.i18n.message('common.filter.clickopen') + "&nbsp; <img style='vertical-align:middle' src='images/expand_icon.png' alt='expand' />");
        }
    })
}