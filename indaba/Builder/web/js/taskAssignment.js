
function setAssignment(id, name, dueTime) {
    document.getElementById('assignmentId').value = id;
    document.getElementById('taskName').innerHTML = name;
    document.getElementById('dueTime').value = dueTime;
    document.getElementById('newDeadline').value = dueTime;
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
