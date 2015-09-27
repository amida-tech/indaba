(function(global){
    var HELP_FRAME = 'help_frame';
    var oLocation = global.location;
    var params = {};
    
    global.$WIDGET = {
        getUrlParameter: function(name){
            var s = location.search;
            var re = new RegExp('&'+name+'(?:=([^&]*))?(?=&|$)','i');
            return (s=s.replace(/^\?/,'&').match(re)) ? (typeof s[1] == 'undefined' ? '' : decodeURIComponent(s[1])) : undefined;
        },

        getStrongnessHtml: function(strongnessNumber, strongnessDesc, useFilters, useScore){
            var cls = useScore ? 'cls-'+strongnessDesc.toLowerCase().split(' ').join('-') : 'cls-no-score';
            var html = '<span class="' + cls + '"><em>' + (useScore ? Math.round(strongnessNumber) : 'N/A');
            if (useFilters || !useScore)
                html += '</em><label>&nbsp;</label></span>';
            else
                html += '</em><label>' + strongnessDesc + '</label></span>';
            return html;
        },
        
        getStrongnessHtml4RWI: function(strongnessNumber, useFilters, useScore, scoreRangeDefArr, noscoreTxt){
            var coloredBlk = '';
            if(useScore) {
                var scoreLevel = $WIDGET.getRWIScoreDisplay(scoreRangeDefArr, strongnessNumber);
                coloredBlk = '<label style="border-left: 18px solid ' + scoreLevel.color + ';">&nbsp;</label>';
            }
            var naTxt = noscoreTxt?noscoreTxt:'';
            var html = '<span class="cls-no-score"><em>' + (useScore ? Math.round(strongnessNumber) : naTxt);
            if (useFilters || !useScore)
                html += '</em>' + coloredBlk + '</span>';
            else
                html += '</em></span>';
            return html;
        },

        appendIFrame: function(){
            $('<iframe id="'+HELP_FRAME+'" height="0" width="0" frameborder="0"></iframe>').appendTo($(global.document.body));
        },

        newLocationSearch: function(){
            return '?' + this.getParamUrlComponent();
        },

        setParameter: function(name, value){
            params[name] = value;
        },

        deleteParameter: function(name){
            delete params[name];
        },

        // arguments are parameters to be excluded
        getParamUrlComponent: function(){
            var args = Array.prototype.slice.call(arguments);
            var ss = [];
            for(var k in params){
                if ($.inArray(k, args) == -1)
                    ss.push(k+'='+params[k]);
            }
            return ss.join('&');
        },

        widgetResizePipe: function(widgetId){
            var height = $(global.document.body).height() + 20;
            var pipe = global.document.getElementById(HELP_FRAME);
            var helper = decodeURIComponent($WIDGET.getUrlParameter('helper'));
            if (pipe && helper){
                pipe.contentWindow.location.replace(helper+'?widgetId='+(params['frameId'] || widgetId)+'&height='+height+'&cacheb='+Math.random()
                    +'&widgetLink='+encodeURIComponent(location.href));
            }
        },
        
        getRWIScoreDisplay: function(scoreRangeArr, score) {
            for(var i = 0, len = scoreRangeArr.length; i < len; ++i) {
                if(score >= scoreRangeArr[i].range[0] && score <= scoreRangeArr[i].range[1]) {
                    return scoreRangeArr[i];
                }
            }
            return null;
        }
    };

    // init
    $(global.document).ready(function(){
        $('body').delegate('a', 'click', function(){
            var loc = $(this).attr('href');
            $(this).attr('href', '');
            location.replace(loc);
            return false;
        })
        // append iframe to document.body
        global.$WIDGET.appendIFrame();

        // get params object
        var url = location.search;
        url = url.replace('?', '');
        var queries = url.split('&');
        for(var q in queries) {
            var param = queries[q].split('=');
            params[param[0]] = param[1];
        }

        if (eval(global.$WIDGET.getUrlParameter('includeLogo'))){
            $('.widget-container').append('<a id="indaba-powered" href="#" onclick="window.open(\'http://getindaba.org\');return false;"></a>')
            .css({
                'padding-bottom':'35px'
            });
        }

        // hook up 3rd site css
        if (params['csslink']){
            var styles = decodeURIComponent(params['csslink']).split(',');
            $.each(styles, function(idx, link){
                var $link = $('<link rel="stylesheet" href="'+link+'" type="text/css" />');
                $('head').append($link);
                var img = $('<image width="0" height="0"/>').appendTo(document.body);
                img.attr('src', link).bind('error', function(){
                    //console.log('css file loaded: '+$(this).attr('src'));
                    setTimeout('$WIDGET.widgetResizePipe()', 100);
                });
            });
        }
    });
})(window);