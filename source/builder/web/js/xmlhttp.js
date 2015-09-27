// Utility function used for timeout capability
function fireTimer(obj) {
    var myObj = obj;
    function timerFired() {
        if (myObj.inprogress){myObj._abortRequest(myObj);}
    }
    myObj._timeoutID = window.setTimeout(timerFired, myObj.timeoutMS);
}

// Code originated from article on http://jibbering.com
// Constructor for generic R9HTTP client
function AsyncAjax() {};

// Add methods and properties as array
AsyncAjax.prototype = {
    // lousy browers caching gets hack
    cachehack:false,
	// stuff for caller to hide junk
    uservars: null,
    url: null,
    postbuffer: null,
    // Instance of XMLHttpRequest
    xmlhttp: null,
    // Used to make sure multiple calls are not placed
    // with the same client object while another in progress
    inprogress: false,
    // The user defined handler - see MyHandler below
    thecallback: null,

    timeoutMS: -1,

    _timeoutID: null,

    // Tracks user initiated cancellation via timeout or explicit call
    cancelled: false,

    // Request id  - useful for keeping track of request order
    requestid: null,

    // Use this to setup what you will be calling
    //      -- method is GET/POST
    //      -- url is URL to invoke
    //      -- postbuffer is used to post url-form-encoded data with request
    init: function(url, postbuffer, args) {
        this.url = url;
        this.uservars = args;
        this.postbuffer = postbuffer;

        if (window.ActiveXObject) {
            this.xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        else if (window.XMLHttpRequest) {
            this.xmlhttp = new XMLHttpRequest();
        }

		if (!this.xmlhttp) {
            window.alert('Sorry your browser does not support XML HTTP');
        }
    },

    // Use this before calling asynchGET to make the request timeout after a given period
    setTimeout: function(timeInMillisec) {
        this.timeoutMS = timeInMillisec;
    },

    // set request id 
    setRequestId: function(id) {
        this.requestid = id;
    },

    // get request id
    getRequestId: function() {return this.requestid;},

    // client argument is a user defined object to be called
    asyncGET: function (client) {

        // Prevent multiple calls
        if (this.inprogress) {
            throw "Call in progress";
        };

        // Have to assign "this" to a variable
        var self = this;

        if (client==null) {client=self;}
        this.thecallback = client;
        var args = null;

        // Open an async request - third argument makes it async
        if (this.postbuffer == null) {
			var realurl;
			if (this.cachehack){
				var hackparameter=(this.url.indexOf("?")!=-1) ? "&"
					+ new Date().getTime() : "?" + new Date().getTime();
				realurl = this.url + hackparameter;
			}
			else {realurl = this.url;}
            this.xmlhttp.open('GET',realurl,true);
        } else {
            this.xmlhttp.open("POST", this.url, true);
            this.xmlhttp.setRequestHeader("Content-Type", 
										  "application/x-www-form-urlencoded; charset=UTF-8");
            args = this.postbuffer;
        }

        // Assign a closure to the onreadystatechange callback
        this.xmlhttp.onreadystatechange = function() {
            self.stateChangeCallback(self);
        }

        // Send the request
        this.inprogress = true;
        this.cancelled = false;

        this.xmlhttp.send(args);

        // Fire timeout if set
        if (this.timeoutMS > 0) {
            fireTimer(this);
        }
    },

    stateChangeCallback: function(client) {
        switch (client.xmlhttp.readyState) 
        {
            // Request not yet made
            case 1:
                try {
                    if (! client.cancelled) {
                        client.thecallback.onInit();
                    }
                } catch (e) { /* client method not defined */ }
            break;

            // Contact established with server but nothing downloaded yet
            case 2:
                try {
                    // Check for HTTP status 200
                    // the != 0 hack is for an Opera bug.
                    if ( client.xmlhttp.status != 200 && client.xmlhttp.status != 0 ) {
                        if (! client.cancelled) {
                        window.status="error";
                        client.thecallback.onError(client.xmlhttp.status, 
												   client.xmlhttp.statusText, client);
                        }

                        // Abort the request
                        client.xmlhttp.abort();

                        // Call no longer in progress
                        client.inprogress = false;
                    }
                } catch (e) {
                    /* client method not defined */
                }
            break;

            // Called multiple while downloading in progress
            case 3:
                // Notify user client of download progress
                var contentLength;
                try {
                    // Get the total content length
                    // -useful to work out how much has been downloaded
                    try {
                        contentLength =
                            client.xmlhttp.getResponseHeader("Content-Length");
                    } catch (e) {
                        contentLength = NaN;
                    }

                    // Call the progress client with what we've got
                    if (!client.cancelled) {
                        window.status="ping";
                        client.thecallback.onProgress(client.xmlhttp.responseText,
													  contentLength);
                    }

                } catch (e) { /* client method not defined */ }
            break;

            // Download complete
            case 4:
                try {
                    if (client._timeoutID) {
                        window.clearTimeout(client._timeoutID);
                        client._timeoutID = null;
                    }
                    // Suppress multiple IE5 events
                    if (client.inprogress) {
                        client.inprogress = false;
                        if (! client.cancelled) {
                            window.status="done";
                            client.thecallback.onLoad(client);
                        }
                    }
                } catch (e) {
                    /* client method not defined */
                } finally {
                    // Call no longer in progress
                    client.inprogress = false;
                }
            break;
        }
    },

    cancelRequest: function() {
        // kill timer
        var gizmo = this;

        // cancel to suppress message
        this.cancelled = true;

        if (this._timeoutID) {
            window.clearTimeout(this._timeoutID);
            this._timeoutID = null;
        }

        if(gizmo.inprogress) gizmo._abortRequest(gizmo);
    },

    _abortRequest: function(gizmo) {
        if (gizmo.xmlhttp!=null) {
            // Abort the request

            try {
                gizmo.xmlhttp.abort();
                //if (gizmo.inprogress){
                //    window.status="abort";
                //    gizmo.thecallback.onError('timeout', 'Your request has timed out.',gizmo);
                //}
            } catch (e) {}
    
            // Call no longer in progress
            gizmo.cancelled = true;
            gizmo.inprogress = false;
        }
    },

    // utility to get tags on results
    getText: function () {
        return this.xmlhttp.responseText;
    },

    // utility to get tags on results
    getXML: function () {
        return this.xmlhttp.responseXML;
    },

    // utility to get tags on results
    getTags: function (tagname) {
        try {
            return this.xmlhttp.responseXML.getElementsByTagName(tagname);
        } catch (e) {return null;}
    },

    // utility to get subtags -- from opera.com folks
    getOperaText: function (parent,item,index)
    {
        //opera.postError("hello world, opera function");

        try {
            var result = parent.getElementsByTagName(item)[index];
            
            if (result) {
                var returnText='', i=0, node;
                while (node = result.childNodes[i]) {
                    returnText += node.nodeValue;
                    i++;
                }
                return returnText;
            } else {
                return "";
            }
        } catch (e) {
            opera.postError("exception: " + e);
        }
    },

    getOpera: function (parent,item,index)
    {
        return "foo";
    },

    // utility to get subtags 
    getTagText: function (parent,item,index)
    {
        var result = parent.getElementsByTagName(item)[index];
        if (result)
        {
            if (result.childNodes.length > 1) {
                return result.childNodes[1].nodeValue; }
            else if (result.childNodes.length==1) {
                return result.firstChild.nodeValue; }
        }
        else { 
            return ""; 
        }
    },


    onProgress:function(t,l){},
    onError:function(s,t,c){},
    onLoad:function(c) { },
    onInit:function(c) { }

}
var jsck_xmlhttp="v149";
