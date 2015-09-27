$(function(){
    var $top = $("<div id='h_goTop' title='"+$.i18n.message('common.js.msg.goToTop')+"'></div>");
    var $bottom = $("<div id='h_goBottom' title='"+$.i18n.message('common.js.msg.goToBottom')+"'></div>");
    $bottom.add($top).css({
        'background':'url(./images/go_bottom_blue.png) no-repeat center center',
        'width':'80px',
        'height':'70px',
        'margin-top':'20px',
        'position':'fixed',
        'right':'30px',
        'cursor':'pointer',
        'bottom':'55px',
        'margin-left':'500px',
        'left':'50%'
    }).appendTo($(document.body));
    $top.css({
        'background':'url(./images/go_top_blue.png) no-repeat center center',
        'display':'none',
        'bottom':'110px',
        'margin-left':'500px',
        'left':'50%'
    }).appendTo($(document.body));
    $bottom.hover(function(){
        $(this).css('background','url(./images/go_bottom_red.png) no-repeat center center');
        $(this).css('color','#ff0000');
    },function(){
        $(this).css('background','url(./images/go_bottom_blue.png) no-repeat center center');
        $(this).css('color','#666');
    });
    $top.hover(function(){
        $(this).css('background','url(./images/go_top_red.png) no-repeat center center');
        $(this).css('color','#ff0000');
    },function(){
        $(this).css('background','url(./images/go_top_blue.png) no-repeat center center');
        $(this).css('color','#666');
    });
    if($.browser.msie&&$.browser.version=='6.0'){
        $bottom.add($top).css({
            'position':'absolute'
        });
    };
    var wh = $(window).height();
    $(window).bind('scroll',function(){
        ($(this).scrollTop()!=0)?$top.fadeIn():$top.fadeOut();
        ($(window).scrollTop() + wh!=$(document).height())?$bottom.fadeIn():$bottom.fadeOut();
        if($.browser.msie&&$.browser.version=='6.0'){
            $bottom.css({
                top:$(this).scrollTop()+wh-84
                });
            $top.css({
                top:$(this).scrollTop()+wh-154
                });
        }
    })
    $top.click(function(){
        $("html,body").animate({
            scrollTop:0
        },"fast");
    });
    $bottom.click(function(){
        $("body,html").animate({
            scrollTop:document.body.clientHeight
            },500);
    })
})
