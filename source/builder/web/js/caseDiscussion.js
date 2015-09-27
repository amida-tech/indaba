$j(function(){
    //expand and hide text summary/fulltext
    $j("span.expand").live("click", function(event){
        event.preventDefault();
        var expanded = $j(this).hasClass("expanded");
        var root = $j(this).parent("div");
        if (expanded) {
            //change to summary mode
            $j(this).removeClass("expanded");
            $j(".summary", root).show("fast");
            $j(".fullText", root).hide("fast");
        }
        else {
            $j(this).addClass("expanded");
            $j(".summary", root).hide("fast");
            $j(".fullText", root).show("fast");
        }
    });
});

function _discussionBoard(initParams) {
	this.name = initParams.name;
	this.type = initParams.type;
	this.checkPermission = initParams.checkPermission;
	this.displayName = initParams.displayName;
	this.msgboardId = initParams.msgboardId;
	this.containnerName = initParams.containerName;
	this.ctxPath = initParams.ctxPath;
	this.syncStatus = initParams.syncStatus;
	this.syncStatusFunction = initParams.syncStatusFun;
	this.postUrl = "/discussionBoard.do";
	this.filter = "";
	this.sortColumn = "createdTime";
	this.sortOrder = "asc";
	this.discussions = new Array();
	
	this.init();
	this.load();
}

_discussionBoard.prototype.getFilterCtrl = function() {
	return $j(".select", this.containner)[0];
};

_discussionBoard.prototype.getThead = function() {
	return this.containner.getElementsByTagName("THEAD")[0];
};

_discussionBoard.prototype.getTbody = function() {
	return this.containner.getElementsByTagName("TBODY")[0];
};

_discussionBoard.prototype.init = function() {
	this.containner = $j("#" + this.containnerName)[0];
		
	var self = this;
	
	$j("select", this.containner).live("change", function(){
		self.load();
		return false;
	});
	
	$j("th.time", this.containner).live("click", function(){
		if (self.sortColumn == "createdTime")
			self.sort("createdTime", self.sortOrder == "asc" ? "desc" : "asc");
		else {
			self.sortColumn = "createdTime";
			self.sort("createdTime", "asc");
		}
		
		return false;
	});
	
	$j("th.title", this.containner).live("click", function(){
		if (self.sortColumn == "title")
			self.sort("title", self.sortOrder == "asc" ? "desc" : "asc");
		else {
			self.sortColumn = "title";
			self.sort("title", "asc");
		}
		
		return false;
	});
	
	$j("button.add", this.containner).live("click", function(event) {
		event.preventDefault();
		self.add();
	});
	
    $j("a.enhance", this.containner).live("click", function(event){
        event.preventDefault();
        $j(this).hide();
        var root = $j(this).parents("td");
        var msgId = $j(this).parents("tr")[0].id;
        $j(".save", root).show();
        var text = $j("div.fullText", root).html();
        $j(".enhanced", root).show();
        
        // RichText implementation specific
        // if (CKEDITOR.instances["EnhanceEditor" + msgId]) {
        //     CKEDITOR.remove(CKEDITOR.instances["EnhanceEditor" + msgId]);
        // }
        //        
        // CKEDITOR.replace("EnhanceEditor" + msgId);
    });
	
    $j(".discussions table.normalTable a.save", this.containner).live("click", function(event){
        self.enhance(this);
    });
};

_discussionBoard.prototype.load = function() {
	var self = this;
	
	this.filter = this.getFilter();
	
	var parameters = new Object();
	
	parameters.name = this.name;
	parameters.type = this.type;
	parameters.displayName = this.displayName;
	parameters.msgboardid = this.msgboardId;
	parameters.filter = this.getFilter();
	parameters.sc = this.sortColumn;
	parameters.so = this.sortOrder;
	parameters.action = "load";
	parameters.checkPermission = this.checkPermission;
	
	$j.ajax({
		type: "POST",
		url: self.ctxPath + self.postUrl,
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			$j(self.containner).html(result);

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
	});
	
	// RichText implementation specific
	// if (CKEDITOR.instances[self.name + "NewEditor"]) {
	//     CKEDITOR.remove(CKEDITOR.instances[self.name + "NewEditor"]);
	// }
	//    
	// CKEDITOR.replace(self.name + "NewEditor");
};

_discussionBoard.prototype.add = function() {
	var self = this;
	
	var parameters = new Object();
	
	parameters.msgboardid = this.msgboardId;
	
	var title = $j("input[name=title]", this.containner)[0].value;
	if (title == "") {
		jAlert($.i18n.message('common.js.alert.topictitle'));
		return;
	}
	
	// Richtext implementation
	// var body = eval("CKEDITOR.instances." + this.name + "NewEditor" + ".getData()");
	// if (body == "") {
	//     alert("Please input the topic body!");
	//     return;
	// }
	
	// Plain text implementation
	var body = $j("textarea[name=body]", this.containner).val();
	
	parameters.name = this.name;
	parameters.displayName = this.displayName;
	parameters.type = this.type;
	parameters.filter = this.getFilter();
	parameters.sc = this.sortColumn;
	parameters.so = this.sortOrder;
	
	parameters.title = title;
	parameters.body = body;
	
	parameters.action = "add";
	
	$j("input[name=title]", this.containner)[0].value = "";
//	eval("CKEDITOR.instances." + this.name + "NewEditor" + ".setData(\"\")");
	$j("textarea[name=body]", this.containner).val("");
	
	$j.ajax({
		type: "POST",
		url: self.ctxPath + self.postUrl,
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			self.load();
		},
		error: function(result) {
			alert(result);
		}
	});
	
	if (this.syncStatus) {
		this.syncStatusFunction.call();
	}
};

_discussionBoard.prototype.enhance = function(target) {
	var self = this;
	
	var parameters = new Object();
	
	var msgId = $j(target).parents("tr")[0].id;
	
	// RichText implementation
	// var enhanceBody = eval("CKEDITOR.instances.EnhanceEditor" + msgId + ".getData()");
	
	var enhanceBody = $j("textarea[id=EnhanceEditor" + msgId + "]", this.containner).val();
	
	if (enhanceBody == "") {
		alert("Please input your enhanced text!");
		return;
	}
	
	parameters.msgId = msgId;
	parameters.enhancebody = enhanceBody;
	parameters.publishable = document.getElementById("PublishCheckBox" + msgId).checked;
	
	$j.ajax({
		type: "POST",
		url: self.ctxPath + "/enhance.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			if (result.indexOf("OK") > -1)
				self.load();
			else if (result.indexOf("invalid") > -1)
				window.location.href = self.ctxPath + "/login";
		},
		error: function(result) {
			alert(result);
		}
	});
};

_discussionBoard.prototype.sort = function(sc, so) {
	$j("th.time", this.containner)[0].innerHTML = "TIME";
	$j("th.title", this.containner)[0].innerHTML = "TOPIC TITLE";
	
	var sortOrderColumn = document.createElement("SPAN");
	sortOrderColumn.className = so;
	sortOrderColumn.innerHTML = "&nbsp;";
	
	if (sc == "createdTime")
		$j("th.time", this.containner)[0].appendChild(sortOrderColumn);
	else
		$j("th.title", this.containner)[0].appendChild(sortOrderColumn);
	
	this.sortColumn = sc;
	this.sortOrder = so;
	
	this.load();
};

_discussionBoard.prototype.getFilter = function() {
	if (this.getFilterCtrl())
		return this.getFilterCtrl().options[this.getFilterCtrl().selectedIndex].value;
	else
		return "";
};