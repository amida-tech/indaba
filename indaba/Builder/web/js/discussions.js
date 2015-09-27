function discussionBoard(initParams) {
    this.name = initParams.name;
    this.userId = initParams.userId;
    this.userName = initParams.userName;
    this.type = initParams.type;
    this.checkPermission = initParams.checkPermission;
    this.displayName = initParams.displayName;
    this.msgboardId = initParams.msgboardId;
    this.containerName = initParams.containerName;
    this.ctxPath = initParams.ctxPath;
    this.syncStatus = initParams.syncStatus;
    this.syncStatusFunction = initParams.syncStatusFun;
    this.folded = initParams.folded;
    this.disabled = initParams.disabled;
    this.viewMode = initParams.viewMode;
    this.discussionType = initParams.discussionType;
    this.tool = initParams.tool;
    this.postUrl = "/discussionBoard.do";
    this.filter = "";
    this.sortColumn = "createdTime";
    this.sortOrder = "asc";
    this.discussions = new Array();

    this.container = jQuery("#" + this.containerName)[0];

    this.load();
}

discussionBoard.prototype.behave = function(context) {
    var self = this;
    
    // publish this
    jQuery('.discussion-item-foot > a', context).click(function(event){
        var item = jQuery('.discussion-item').has(this);
        jQuery('.discussion-item-enhance', item).toggle();
        event.preventDefault();
    });

    jQuery('.btn-save', context).click(function(event){
        var item = jQuery('.discussion-item').has(this);
        self.enhance(item.attr('messageId'));
    });
};

discussionBoard.prototype.load = function() {
    var self = this;

    var parameters = new Object();

    parameters.name = this.name;
    parameters.type = this.type;
    parameters.displayName = this.displayName;
    parameters.msgboardid = this.msgboardId;
    parameters.sc = this.sortColumn;
    parameters.so = this.sortOrder;
    parameters.action = "load";
    parameters.checkPermission = this.checkPermission;
    parameters.folded = this.folded;
    parameters.disabled = this.disabled;
    parameters.discussionType = this.discussionType;
    parameters.viewMode = this.viewMode;
    parameters.tool = this.tool;

    jQuery.ajax({
        type: "POST",
        url: self.ctxPath + self.postUrl,
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            jQuery(self.container).html(result);

            // add comment
            jQuery("button.add", self.container).click(function(event) {
                event.preventDefault();
                self.add();
            });
            self.behave(self.container);

            // expand/hide this icon
            jQuery('.box h3:not([bound])').has('a.toggleVisible')
                .attr({bound:true})
                .addClass('togglable')
                .click(function(e){
                e.preventDefault();
                var content = jQuery(this).parent().children(".content");

                if (content.css("display") == "none") {
                    content.slideDown("fast");
                    jQuery('a.toggleVisible', this).html("<img src='images/collapse_icon.png' alt='collapse' />");
                } else {
                    content.slideUp("fast");
                    jQuery('a.toggleVisible', this).html("<img src='images/expand_icon.png' alt='expand' />");
                }
            })
        }
    });
};

discussionBoard.prototype.add = function() {
    var self = this;
    var parameters = new Object();

    parameters.msgboardid = this.msgboardId;

    var txt = jQuery("textarea[name=body]", this.container);
    var body = txt.val();
    if (body == "") {
        jAlert($.i18n.message('common.js.alert.comments'));
        return;
    }

    parameters.name = this.name;
    parameters.displayName = this.displayName;
    parameters.folded = this.folded;
    parameters.type = this.type;
    parameters.sc = this.sortColumn;
    parameters.so = this.sortOrder;

    parameters.body = body;

    parameters.action = "add";

    var adding = jQuery('.discussion-add-comment span', self.container);
    adding.show();
    jQuery.ajax({
        type: "POST",
        url: self.ctxPath + self.postUrl,
        data: parameters,
        cache: false,
        async: true,
        success: function(result) {
            var items = jQuery('.discussion-item', result).filter(function(index){
                return index >= jQuery('.discussion-item', self.container).size();
            });
            jQuery('.discussion-items', self.container).append(items);
            self.behave(items);
            
            txt.val("");
            adding.hide();
            items.highlightFade({speed:1500});
        },
        error: function(result) {
            adding.hide();
            alert(result);
        }
    });

    if (this.syncStatus) {
        this.syncStatusFunction.call();
    }
};

discussionBoard.prototype.enhance = function(msgId) {
    var self = this;
    var parameters = new Object();

    var enhanceBody = jQuery("textarea[id=EnhanceEditor" + msgId + "]", self.container).val();

    if (enhanceBody == "") {
		jAlert($.i18n.message('common.js.alert.enhancedtext'));
        return;
    }

    parameters.msgId = msgId;
    parameters.enhancebody = enhanceBody;
    parameters.publishable = document.getElementById("PublishCheckBox" + msgId).checked;

    jQuery.ajax({
        type: "POST",
        url: self.ctxPath + "/enhance.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            if (result.indexOf("OK") > -1){
                jInfo($.i18n.message('common.js.alert.successenhance'), $.i18n.message('common.js.alert.title.success'), null);
            }
            else if (result.indexOf("invalid") > -1)
                window.location.href = self.ctxPath + "/login";
        },
        error: function(result) {
            alert(result);
        }
    });
};