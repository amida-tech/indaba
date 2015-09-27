var $widget = $widget || (function(){
    // get params object
    var params = {};
    var url = location.search;
        if (url){
            url = url.replace('?', '');
            var queries = url.split('&');
            for(var q in queries) {
                var param = queries[q].split('=');
                params[param[0]] = param[1];
        }
    }

    function jq_param(){
        var ss = [];
        for (var k in params){
            ss.push(encodeURIComponent(k)+'='+encodeURIComponent(params[k]));
        }
        return ss.join('&');
    }

    return {
        updateWidgetLink: function(frameId, link){
            if (frameId && link)
                params[frameId] = decodeURIComponent(link);
        },

        getPermlink: function(){
            if (location.search)
                return location.href.replace(location.search, '?' + jq_param());
            else
                return location.href + '?' + jq_param();
        },

        resizeIframe: function(frameId, height){
            setTimeout('document.getElementById("'+frameId+'").height = parseInt('+height+')');
        },

        loadPermlink: function(){
            var frames = document.getElementsByTagName('IFRAME');
            for (var i=0; i<frames.length; i++){
                if (frames[i].id && params[frames[i].id]){
                    frames[i].src = decodeURIComponent(decodeURIComponent(params[frames[i].id]));
                }
            }
        }
    };
})();

// loadPermlink function is supposed to be invoked
// after all widget IFRAMEs are in the DOM tree
$widget.loadPermlink();