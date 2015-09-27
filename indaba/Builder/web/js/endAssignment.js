/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function endPeerReviewAssignment(id, userName, taskName, horseName) {
    /*
        document.getElementById('exitAssignmentId').value = id;
        document.getElementById('exitTaskName').innerHTML = taskName;
        document.getElementById('exitHorseName').innerHTML = horseName;
        document.getElementById('exitUserName').innerHTML = userName;
            */
    var msg = $.i18n.message('common.js.msg.endassignment', [userName, taskName, horseName])
    + "<div style='height:16px'>&nbsp;</div>"
    + "<input type='radio' name='exitOption' value='0' checked>" + $.i18n.message('common.js.msg.partialsubmission')
    + "<div style='height:8px'>&nbsp;</div>"
    + "<input type='radio' name='exitOption' value='1'>" + $.i18n.message('common.js.msg.forcecomplete')
    + "<div style='height:8px'>&nbsp;</div>"
    + "<input type='hidden' id='exitAssignmentId' name='exitAssignmentId' value='" + id + "'/>";

    var result;
    jConfirm(msg, $.i18n.message('common.js.msg.exitassignment'), function(r){
        if (r) {
            confirmEndPeerReviewAssignment(id, userName, taskName, horseName, result);
        }
    },
    function(){
        result = $("input[name='exitOption']:checked").val();
    });
    return false;
}

function endAssignment(id, userName, taskName, horseName) {
    var msg = $.i18n.message('common.js.msg.endassignment', [userName, taskName, horseName]) + "<br/><br/>" + $.i18n.message('common.js.alert.undone');
    jConfirm(msg, $.i18n.message('common.js.alert.title.confirm'), function(r) {
        if (r){
            submitEndAssignment(id, 0);
        }
    });
}

function confirmEndPeerReviewAssignment(id, userName, taskName, horseName, option) {
    var msg = $.i18n.message('common.js.msg.endassignment', [userName, taskName, horseName]) + ".<br/><br/>";
    var selectionMsg = $.i18n.message('common.js.msg.selected');
    if (option == 0) {
        selectionMsg +=  $.i18n.message('common.js.msg.partialsubmission');
    } else {
        selectionMsg += $.i18n.message('common.js.msg.forcecomplete');
    }
    selectionMsg += "<br/><br/>" + $.i18n.message('common.js.alert.undone')
    jConfirm(msg + selectionMsg, $.i18n.message('common.js.alert.title.confirm'), function(r) {
        if (r){
            submitEndAssignment(id, option);
        }
    });
}

function submitEndAssignment(id, option) {
    var parameters = new Object();
    parameters.assignid = id;
    parameters.exitOption = option;
    parameters.action = "exitAssignment";

    $.ajax({
        type: "POST",
        url: "assignment.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            //$.fancybox.close();
            window.location.reload();
        },
        error: function(response) {
            alert(response);
        //$.fancybox.close();
        }
    });
}



