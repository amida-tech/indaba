(function($) {
    $.ocsdlg = {
        options:{
            buttons: {
                'OK': null, 
                'Cancel': null
            }
        },
        alert: function(message, title, callback) {
            if( title == null ) title = 'Alert';
            $.ocsdlg._show(message, title, 'alert', function(result) {
                if( callback ) callback(result);
            });
        },
        success: function(message, title, callback) {
            if( title == null ) title = 'Success';
            $.ocsdlg._show(message, title, 'success', function(result) {
                if( callback ) callback(result);
            });
        },
        info: function(message, title, callback) {
            if( title == null ) title = 'Information';
            $.ocsdlg._show(message, title, 'info', function(result) {
                if( callback ) callback(result);
            });
        },
        error: function(message, title, callback) {
            if( title == null ) title = 'Error';
            $.ocsdlg._show(message, title, 'error', function(result) {
                if( callback ) callback(result);
            });
        },
        confirm: function(message, title, callback) {
            if( title == null ) title = 'Confirm';
            $.ocsdlg._show(message, title, 'confirm', function(result) {
                if( callback ) callback(result);
            });
        },
			
        prompt: function(message, title, callback) {
            if( title == null ) title = 'Prompt';
            $.ocsdlg._show(message, title, 'prompt', function(result) {
                if( callback ) callback(result);
            });
        },
        _show: function(msg, title, type, callback) {
            var elem = $('<div id="ocsdlg" class="'+type+'"><div class="ocsdlg-msg">' + msg + '</div></div>');
            var btnList = {};
            switch( type ) {
                case 'success':
                case 'alert':
                case 'error':
                case 'info':
                    btnList = {
                        'OK':function(){
                            elem.dialog('close');
                            if(callback){
                                callback(true)
                            }
                        }
                    }
                    break;
                case 'confirm':
                    btnList = {
                        'OK':function(){
                            elem.dialog('close');
                            if(callback){
                                callback(true)
                            }
                        },
                        'Cancel':function(){
                            elem.dialog('close');
                            if(callback){
                                callback(false)
                            }
                        }
                    }
                    break;
            }
            elem.dialog({
                width: 460,
                title: title,
                resizable: false,
                modal: true,
                position: ["center", "center"],
                buttons: btnList
            });
        }
    }
                
    // Shortuct functions
    ocsSuccess = function(message, title, callback) {
        $.ocsdlg.success(message, title, callback);
    };
    ocsError = function(message, title, callback) {
        $.ocsdlg.error(message, title, callback);
    };
    ocsInfo = function(message, title, callback) {
        $.ocsdlg.info(message, title, callback);
    };
	
    ocsAlert = function(message, title, callback) {
        $.ocsdlg.alert(message, title, callback);
    }
	
    ocsConfirm = function(message, title, callback) {
        $.ocsdlg.confirm(message, title, callback);
    };
		
    showPrompt = function(message, title, callback) {
        $.ocsdlg.prompt(message,  title, callback);
    };
})(jQuery);

