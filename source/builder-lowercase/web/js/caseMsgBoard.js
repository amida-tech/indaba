function loadCaseUserMsgBoard(param) {
    var parameters = new Object();
    parameters.caseid = param.caseId;
    parameters.action = "getCaseUserMsgboardId";

    var msgboardId;

    jQuery.ajax({
        type: "POST",
        url: param.ctxPath + "/caseMsgBoard.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            if (result.indexOf("invalid") > -1)
                window.location.href = param.ctxPath;
            if (result.indexOf("error") > -1)
                alert(result);
            msgboardId = JSON.parse(result);
        },
        error: function(result) {
            alert(result);
        }
    });

    parameters = new Object();
    parameters.ctxPath = param.ctxPath;
    parameters.userId = param.userId;
    parameters.userName = param.userName;
    parameters.name = param.containerName;
    parameters.containerName = param.containerName;
    parameters.displayName = $j.i18n.message('common.js.msg.usernotes');
    parameters.type = "case";
    parameters.msgboardId = msgboardId;
    new discussionBoard(parameters);
}

function loadCaseStaffMsgBoard(param) {
    var parameters = new Object();
    parameters.caseid = param.caseId;
    parameters.action = "getCaseStaffMsgboardId";

    var msgboardId;

    jQuery.ajax({
        type: "POST",
        url: param.ctxPath + "/caseMsgBoard.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            if (result.indexOf("invalid") > -1)
                window.location.href = param.ctxPath;
            if (result.indexOf("error") > -1)
                alert(result);
            msgboardId = JSON.parse(result);
        },
        error: function(result) {
            alert(result);
        }
    });

    parameters = new Object();
    parameters.ctxPath = param.ctxPath;
    parameters.userId = param.userId;
    parameters.userName = param.userName;
    parameters.name = param.containerName;
    parameters.containerName = param.containerName;
    parameters.displayName = $j.i18n.message('common.js.msg.staffnotes');
    parameters.type = "case";
    parameters.msgboardId = msgboardId;
    new discussionBoard(parameters);
}