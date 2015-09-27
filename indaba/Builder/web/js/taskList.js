/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    /* This is basic - uses default settings */
    $("a#single_image").fancybox();

    /* Using custom settings */
    $("a#inline").fancybox({
        'hideOnContentClick': false
    });

    /* Apply fancybox to multiple items */
    $("a.group").fancybox({
        'transitionIn'	:	'elastic',
        'transitionOut'	:	'elastic',
        'speedIn'	:	600,
        'speedOut'	:	200,
        'overlayShow'	:	false
    });

    $("#newDeadline").datepicker(({
        dateFormat: 'yy-mm-dd'
    }));
});

function setAssignment(id, name, dueTime, userName) {
    document.getElementById('assignmentId').value = id;
    document.getElementById('taskName').innerHTML = name;
    document.getElementById('dueTime').value = dueTime;
    document.getElementById('newDeadline').value = dueTime;
    document.getElementById('userName').innerHTML = userName;
}

function saveDeadline() {
    if (document.getElementById('newDeadline').value == document.getElementById('dueTime').value) {
        $.fancybox.close();
        return;
    }

    var deadlineStr = document.getElementById('newDeadline').value.replace(/-/ig, "/");
    if (new Date(deadlineStr) < new Date()) {
        $.fancybox.close();
        return;
    }

    var parameters = new Object();
    parameters.assignid = document.getElementById('assignmentId').value;
    parameters.deadline = document.getElementById('newDeadline').value;
    parameters.action = "saveDeadline";

    $.ajax({
        type: "POST",
        url: "assignment.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            $.fancybox.close();
            window.location.reload();
        },
        error: function(response) {
            alert(response);
            $.fancybox.close();
        }
    });
}

function displayTasks(horseid, goalid) {
    $.fancybox({
        href : 'tasklist.do?horseid='+horseid+'&goalid='+goalid+'&t='+(new Date().getTime()),
        type : 'ajax',
        padding : 5,
        wrapCSS    : 'fancybox-custom',
        closeClick : true,
        helpers : {
            title : null,
            overlay : {
                css : {
                    'background-color' : '#eee'
                }
            }
        }
    });
    return false;
}
