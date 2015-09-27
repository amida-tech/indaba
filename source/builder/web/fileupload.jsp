<%-- 
    Document   : fileupload
    Created on : 2010-7-4, 16:35:55
    Author     : Jeanbone
--%>
<%--
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/mootools/1.2.2/mootools.js"></script>
<script type="text/javascript" src="fancyupload/source/Swiff.Uploader.js"></script>
<script type="text/javascript" src="fancyupload/source/Fx.ProgressBar.js"></script>
<script type="text/javascript" src="http://github.com/mootools/mootools-more/raw/master/Source/Core/Lang.js"></script>
<script type="text/javascript" src="fancyupload/source/FancyUpload2.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link type="text/css" rel="stylesheet" href="fancyupload/source/style.css"/>
<script type="text/javascript">
    window.addEvent('domready', function() { // wait for the content
        // our uploader instance        
        var up = new FancyUpload2($('demo-status'), $('demo-list'), { // options object
            // we console.log infos, remove that in production!!
            verbose: true,
            //url: 'fileupload.do?uid=<%=session.getAttribute("uid")%>&caseid=' + getQueryString("caseid"),
            url: 'fileupload.do?uid=${uid}&caseid=' + getQueryString("caseid"),
            // path to the SWF file
            path: 'fancyupload/source/Swiff.Uploader.swf',

            // remove that line to select all files, or edit it, add more items
            typeFilter: {
                //                            'Images (*.jpg, *.jpeg, *.gif, *.png)': '*.jpg; *.jpeg; *.gif; *.png'
            },

            // this is our browse button, *target* is overlayed with the Flash movie
            target: 'demo-browse',

            // graceful degradation, onLoad is only called if all went well with Flash
            onLoad: function() {
                $('demo-status').removeClass('hide'); // we show the actual UI
                $('demo-fallback').destroy(); // ... and hide the plain form

                // We relay the interactions with the overlayed flash to the link
                this.target.addEvents({
                    click: function() {
                        return false;
                    },
                    mouseenter: function() {
                        this.addClass('hover');
                    },
                    mouseleave: function() {
                        this.removeClass('hover');
                        this.blur();
                    },
                    mousedown: function() {
                        this.focus();
                    }
                });

                // Interactions for the 2 other buttons

                $('demo-clear').addEvent('click', function() {
                    up.remove(); // remove all files
                    return false;
                });

                $('demo-upload').addEvent('click', function() {
                    up.start(); // start upload
                    return false;
                });
            },

            // Edit the following lines, it is your custom event handling

            /**
             * Is called when files were not added, "files" is an array of invalid File classes.
             *
             * This example creates a list of error elements directly in the file list, which
             * hide on click.
             */
            onSelectFail: function(files) {
                files.each(function(file) {
                    new Element('li', {
                        'class': 'validation-error',
                        html: file.validationErrorMessage || file.validationError,
                        title: MooTools.lang.get('FancyUpload', 'removeTitle'),
                        events: {
                            click: function() {
                                this.destroy();
                            }
                        }
                    }).inject(this.list, 'top');
                }, this);
            },

            /**
             * This one was directly in FancyUpload2 before, the event makes it
             * easier for you, to add your own response handling (you probably want
             * to send something else than JSON or different items).
             */
            onFileSuccess: function(file, response) {
                //var json = new Hash(JSON.decode(response, true) || {});
                var json = JSON.parse(response);
                if (json.toString() == "0" || json.toString() == "Index: 0, Size: 0") {
                    file.element.addClass('file-success');
                    file.info.set('html', '<strong>'+$.i18n.message('common.js.alert.uploadsuccess')+'</strong>');
                } else {
                    // check deleted
                    var deleteFileIds = document.getElementById("deleteFileIds").value.split(";");
                    var isDel = false;
                    for (i = 0; i < deleteFileIds.length; i ++) {
                        if (deleteFileIds[i] == json.toString()) {
                            isDel = true;
                            break;
                        }
                    }
                    if (isDel) {
                        file.element.addClass('file-success');
                        file.info.set('html', '<strong>'+$.i18n.message('common.js.alert.uploadsuccess')+'</strong>');
                    } else {
                        file.element.addClass('file-failed');
                        file.info.set('html', '<strong>'+$.i18n.message('common.js.alert.fileexisted')+'</strong>');
                    }
                }
            },

            /**
             * onFail is called when the Flash movie got bashed by some browser plugin
             * like Adblock or Flashblock.
             */
            onFail: function(error) {
                switch (error) {
                    case 'hidden': // works after enabling the movie and clicking refresh
                        alert('To enable the embedded uploader, unblock it in your browser and refresh (see Adblock).');
                        break;
                    case 'blocked': // This no *full* fail, it works after the user clicks the button
                        alert('To enable the embedded uploader, enable the blocked Flash movie (see Flashblock).');
                        break;
                    case 'empty': // Oh oh, wrong path
                        alert('A required file was not found, please be patient and we fix this.');
                        break;
                    case 'flash': // no flash 9+ :(
                        alert('To enable the embedded uploader, install the latest Adobe Flash plugin.')
                }
            }
        });
    });

    function getQueryString(name) {

        if(location.href.indexOf("?")==-1 || location.href.indexOf(name+'=')==-1) {
            return '';
        }

        var queryString = location.href.substring(location.href.indexOf("?")+1);
        var parameters = queryString.split("&");
        var pos, paraName, paraValue;

        for(var i=0; i<parameters.length; i++) {
            pos = parameters[i].indexOf('=');
            if(pos == -1) { continue; }

            paraName = parameters[i].substring(0, pos);
            paraValue = parameters[i].substring(pos + 1);
            if(paraName == name) {
                return unescape(paraValue.replace(/\+/g, " "));
            }
        }
        return '';
    }
</script>



<div>
    <fieldset id="demo-fallback">
        <label for="demo-photoupload">
        </label>
    </fieldset>
    <div id="demo-status" class="hide">
        <p>
            <a href="#" id="demo-browse"><indaba:msg key='jsp.fileupload.browseFiles' /></a> |
            <a href="#" id="demo-clear"><indaba:msg key='jsp.fileupload.clearList' /></a> |
            <a href="#" id="demo-upload"><indaba:msg key='jsp.fileupload.startUpload' /></a>
        </p>
        <div>
            <strong class="overall-title"></strong><br />
            <img src="fancyupload/assets/progress-bar/bar.gif" class="progress overall-progress" />
        </div>
        <div>
            <strong class="current-title"></strong><br />
            <img src="fancyupload/assets/progress-bar/bar.gif" class="progress current-progress" />
        </div>
        <div class="current-text"></div>
    </div>
    <ul id="demo-list"></ul>
</div>

