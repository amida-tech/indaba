$(function(){
    //expand and hide text summary/fulltext
    $(".discussions table.normalTable span.expand").live("click", function(event){
        event.preventDefault();
        var expanded = $(this).hasClass("expanded");
        var root = $(this).parent("div");
        if (expanded) {
            //change to summary mode
            $(this).removeClass("expanded");
            $(".summary", root).show("fast");
            $(".fullText", root).hide("fast");
        }
        else {
            $(this).addClass("expanded");
            $(".summary", root).hide("fast");
            $(".fullText", root).show("fast");
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
	this.folded = initParams.folded;
	this.postUrl = "/discussionBoard.do";
	this.filter = "";
	this.sortColumn = "createdTime";
	this.sortOrder = "asc";
	this.discussions = new Array();
	
	this.init();
	this.load();
}

_discussionBoard.prototype.getFilterCtrl = function() {
	return $(".select", this.containner)[0];
};

_discussionBoard.prototype.getThead = function() {
	return this.containner.getElementsByTagName("THEAD")[0];
};

_discussionBoard.prototype.getTbody = function() {
	return this.containner.getElementsByTagName("TBODY")[0];
};

_discussionBoard.prototype.init = function() {
	this.containner = $("#" + this.containnerName)[0];
		
	var self = this;
	
	$("select", this.containner).live("change", function(){
		self.load();
		return false;
	});
	
	$("th.time", this.containner).live("click", function(){
		if (self.sortColumn == "createdTime")
			self.sort("createdTime", self.sortOrder == "asc" ? "desc" : "asc");
		else {
			self.sortColumn = "createdTime";
			self.sort("createdTime", "asc");
		}
		
		return false;
	});
	
	$("th.title", this.containner).live("click", function(){
		if (self.sortColumn == "title")
			self.sort("title", self.sortOrder == "asc" ? "desc" : "asc");
		else {
			self.sortColumn = "title";
			self.sort("title", "asc");
		}
		
		return false;
	});
	
	$("button.add", this.containner).live("click", function(event) {
		event.preventDefault();
		self.add();
	});
	
    $("a.enhance", this.containner).live("click", function(event){
        event.preventDefault();
        $(this).hide();
        var root = $(this).parents("td");
        var msgId = $(this).parents("tr")[0].id;
        $(".save", root).show();
        var text = $("div.fullText", root).html();
        $(".enhanced", root).show();
        
        // RichText implementation specific
        // if (CKEDITOR.instances["EnhanceEditor" + msgId]) {
        //     CKEDITOR.remove(CKEDITOR.instances["EnhanceEditor" + msgId]);
        // }
        //        
        // CKEDITOR.replace("EnhanceEditor" + msgId);
    });
	
    $(".discussions table.normalTable a.save", this.containner).live("click", function(event){
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
	parameters.folded = this.folded;
	
	$.ajax({
		type: "POST",
		url: self.ctxPath + self.postUrl,
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			$(self.containner).html(result);

                        $('.box h3 a.toggleVisible:not([bound])').attr('bound',true).click(function(e){
                            e.preventDefault();
                            var content = $(this).parent().parent().children(".content");

                            if (content.css("display") == "none") {
                                content.slideDown("fast");
                                $(this).html("<img src='images/collapse_icon.png' alt='' />");
                            } else {
                                content.slideUp("fast");
                                $(this).html("<img src='images/expand_icon.png' alt='' />");
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
	
	var title = $("input[name=title]", this.containner)[0].value;
	if (title == "") {
		jAlert($.i18n.message('common.js.alert.topictitle'));
		return;
	}
	
	// Plain text implementation
	var body = $("textarea[name=body]", this.containner).val();
        if (body == "") {
            jAlert($.i18n.message('common.js.alert.comments'));
            return;
        }
	
	parameters.name = this.name;
	parameters.displayName = this.displayName;
	parameters.type = this.type;
	parameters.filter = this.getFilter();
	parameters.sc = this.sortColumn;
	parameters.so = this.sortOrder;
	
	parameters.title = title;
	parameters.body = body;
	
	parameters.action = "add";
	
	$("input[name=title]", this.containner)[0].value = "";
//	eval("CKEDITOR.instances." + this.name + "NewEditor" + ".setData(\"\")");
	$("textarea[name=body]", this.containner).val("");
	
	$.ajax({
		type: "POST",
		url: self.ctxPath + self.postUrl,
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			self.load();
		},
		error: function(result) {
			jAlert(result);
		}
	});
	
	if (this.syncStatus) {
		this.syncStatusFunction.call();
	}
};

_discussionBoard.prototype.enhance = function(target) {
	var self = this;
	
	var parameters = new Object();
	
	var msgId = $(target).parents("tr")[0].id;
	
	// RichText implementation
	// var enhanceBody = eval("CKEDITOR.instances.EnhanceEditor" + msgId + ".getData()");
	
	var enhanceBody = $("textarea[id=EnhanceEditor" + msgId + "]", this.containner).val();
	
	if (enhanceBody == "") {
		jAlert($.i18n.message('common.js.alert.enhancedtext'));
		return;
	}
	
	parameters.msgId = msgId;
	parameters.enhancebody = enhanceBody;
	parameters.publishable = document.getElementById("PublishCheckBox" + msgId).checked;
	
	$.ajax({
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
	$("th.time", this.containner)[0].innerHTML = $.i18n.message('common.label.time');
	$("th.title", this.containner)[0].innerHTML = $.i18n.message('common.label.topictitle');
	
	var sortOrderColumn = document.createElement("SPAN");
	sortOrderColumn.className = so;
	sortOrderColumn.innerHTML = "&nbsp;";
	
	if (sc == "createdTime")
		$("th.time", this.containner)[0].appendChild(sortOrderColumn);
	else
		$("th.title", this.containner)[0].appendChild(sortOrderColumn);
	
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