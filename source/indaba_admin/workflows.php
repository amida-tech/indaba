<?php
require_once('header.php');
require_once('include/config.inc');

?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/tinybox.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>

<script language="javascript"  type="text/javascript"> 

// Get rule drop down
$(document).ready(function() {
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"html",
		url:		"workflow_functions.php", 
		data:       "action=show_workflow_opt",
		success:	function(data) {
			$("#workflow_dd").html(data);
		}
	});
});


function workflowSelected() {
	if ( $("#workflow_opt").val() <= 0 ) {
		$("#sequence_opt").attr({disabled : true});
		$("#sync").css("display", "none");
	} 
	if ( $("#workflow_opt").val() == -1 ) {
		$("#edit_area").html('');
		$("#status_area").html('');
		return;
	}
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       "action=show_workflow&workflow_id=" + $("#workflow_opt").val(),
		success:	function(obj) {
			if ( $("#workflow_opt").val() <= 0 ) {
				$("#sequence_opt").attr({disabled : true});
			} else {
				$("#sequence_opt").attr({disabled : false});
			}
			$("#goal_opt").attr({disabled : true});
			$("#edit_area").html(obj.form);
			$("#status_area").html(obj.status);
			if ( $("#workflow_id").val() > 0 ) {
				$("#sequence_dd").html(obj.sequence_opt);
				$("#clone").css("display", "inline");
				$("#sync").css("display", "inline");
			} else {
				$("#clone").css("display", "none");
				$("#sync").css("display", "none");
			}
		}
	});
}

function saveWorkflow() {
	var q_string = "action=save_workflow";
	if ( $("#name").val() == "" ) {
		alert("Please enter a name for the workflow");
		return;
	}
	q_string += "&name=" + $("#name").val();
	if ( $("#description").val() == "" ) {
		alert("Please enter a description for the workflow");
		return;
	}
	q_string += "&description=" + $("#description").val();
	q_string += "&workflow_id=" + $("#workflow_id").val();
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       q_string,
		success:    function(obj) {
			$("#status_area").html(obj.msg );
			$("#workflow_dd").html(obj.workflow_opt);
			$("#workflow_id").val(obj.workflow_id);
			$("#sequence_dd").html(obj.sequence_opt);
		}
	});
}

function deleteWorkflow() {
	var del = confirm("Are you sure you want to delete ALL items belong to this workflow??");
	if ( del == false ) {
		return;
	}
	$.ajax({
		type:		"GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:		"action=delete_workflow&workflow_id=" + $("#workflow_id").val(),
		success:	function(obj) {
			$("#status_area").html( obj.msg );
			if ( obj.status == 0 ) {
				$("#edit_area").html("");
				$("#workflow_dd").html(obj.workflow_opt);
				$("#sequence_opt").attr({disabled:true});
			}
		}
	});
}

// sequence functions
function sequenceSelected() {
	if ( $("#sequence_opt").val() <= 0 ) {
		$("#goal_opt").attr({disabled:true});
	}
	if ( $("#sequence_opt").val() == -1 ) {
		return;
	}
	if ( $("#status_area").text() == "Change not saved yet" ) {
		var go = confirm("The changes made to workflow have not been saved yet, do you want discard the changes?");
		if ( go == false ) {
			return;
		}
	}
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       "action=show_sequence&workflow_id=" + $("#workflow_id").val() + "&sequence_id=" + $("#sequence_opt").val(),
		success:    function(obj) {
			if ( obj.status == 1 ) {
				$("#status_area").html(obj.msg);
			} else {
				$("#edit_area").html(obj.form);
				$("#status_area").html('');
			}
			if ( obj.sequence_id > 0 ) {
				$("#goal_dd").html(obj.goal_opt);
			}
		}
	})
}

function saveSequence() {
	if ( $("#name").val() == "" ) {
		alert("Please enter a name for the sequence");
		return;
	}
	if ( $("#description").val() == "" ) {
		alert("Please enter a description for the sequence");
		return;
	}
	var q_string = $("#target_list").sortable('serialize');
	// if we see ava_goal in target, we need change them to pre_goal
	q_string = q_string.replace(/ava\_goal/g, 'add_pre_goal');

	var del = "&" + $("#source_list").sortable('serialize');
	// if we see pre_goal in source, we need change them to del_pre_goal
	del = del.replace(/pre\_goal/g, 'del_pre_goal');
	q_string = q_string + del;

	q_string += "&description=" + $("#description").val();
	q_string += "&workflow_id=" + $("#workflow_opt").val();
	q_string += "&sequence_id=" + $("#sequence_id").val();
	q_string += "&name=" + $("#name").val();
	q_string += "&action=save_sequence";
	//alert(q_string);
	//return;
	$.ajax({
		type:		"GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       q_string,
		success:    function(obj) {
			$("#status_area").html(obj.msg );
			$("#sequence_dd").html(obj.sequence_opt);
			$("#goal_dd").html(obj.goal_opt);
			$("#sequence_id").val(obj.sequence_id);
			$("#workflow_id").val(obj.workflow_id);
		}
	});
}

function deleteSequence() {
	var del = confirm("Are you sure you want to delete ALL items belong to this sequence??");
	if ( del == false ) {
		return;
	}
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       "action=delete_sequence&sequence_id=" + $("#sequence_id").val() + "&workflow_id=" + $("#workflow_id").val(),
		success:    function(obj) {
			$("#status_area").html(obj.msg);
			if ( obj.status == 0 ) {
				$("#edit_area").html("");
				$("#sequence_dd").html(obj.sequence_opt);
				$("#goal_opt").attr({disabled:true});
			}
		}
	});
}

// goal functions
function goalSelected() {
	if ( $("#goal_opt").val() == -1 ) {
		return;
	}
	if ( $("#status_area").text() == "Change not saved yet" ) {
		var go = confirm("The changes made to sequence have not been saved yet, do you want to discard the changes?");
		if ( go == false ) {
			return;
		}
	}
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       "action=show_goal&sequence_id=" + $("#sequence_opt").val() + "&goal_id=" + $("#goal_opt").val(),
		success:    function(obj) {
			if ( obj.status == 1 ) {
				$("#status_area").html(obj.msg);
			} else {
				$("#edit_area").html(obj.form);
				$("#status_area").html('');
				$("#rule_id").change();
			}
		}
	});
}

function saveGoal() {
	if ( $("#name").val() == "" ) {
		alert("Please enter a name for the goal");
		return;
	}
	if ( $("#duration").val() == "" || isNaN($("#duration").val()) || $("#duration").val() <= 0 ) {
		alert("Please enter an integer for the duration");
		return;
	}
	// if ( $("#access_matrix_id").val() == -1 ) {
	// 	alert("Please select an Access Matrix from the list");
	// 	return;
	// }
	if ( $("#entrance_rule_desc").val() == '' ) {
		alert("Please enter description for the entrance rule");
		return;
	}
	if ( $("#inflight_rule_desc").val() == '' ) {
		alert("Please enter description for the inflight rule");
		return;
	}
	if ( $("#exit_rule_desc").val() == '' ) {
		alert("Please enter description for the exit rule");
		return;
	}
	// replace current_item with normal id
	//var sortablelink = $("#source_list")
	//$(sortablelink).sortable();
	//var q_string = $(sortablelink).serialize();
	var q_string = $("#source_list").sortable('serialize');
	q_string = q_string.replace(/current\_item/g, 'goal_list');
	q_string += "&name=" + $("#name").val();
	q_string += "&workflow_id=" + $("#workflow_opt").val();
	q_string += "&description=" + $("#description").val();
	q_string += "&duration=" + $("#duration").val();
	// q_string += "&access_matrix_id=" + $("#access_matrix_id").val();
	q_string += "&entrance_rule_desc=" + encodeURIComponent($("#entrance_rule_desc").val());
	q_string += "&inflight_rule_desc=" + encodeURIComponent($("#inflight_rule_desc").val());
	q_string += "&exit_rule_desc=" + encodeURIComponent($("#exit_rule_desc").val());
	q_string += "&entrance_rule_file_name=" + $("#entrance_rule_file_name").val();
	q_string += "&inflight_rule_file_name=" + $("#inflight_rule_file_name").val();
	q_string += "&exit_rule_file_name=" + $("#exit_rule_file_name").val();
	q_string += "&sequence_id=" + $("#sequence_id").val();
	q_string += "&goal_id=" + $("#goal_id").val();
	q_string += "&action=save_goal";

	//alert(q_string);
	//return;
	$(".btn").each(function() {
		$(this).attr('disabled', true);
	});
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       q_string,
		success:    function(obj) {
			$("#status_area").text(obj.msg );
			$("#goal_id").val(obj.goal_id);
			$("#sequence_id").val(obj.sequence_id);
			$("#goal_dd").html(obj.goal_opt);
			$(".btn").each(function() {
				$(this).attr('disabled', false);
			});
		}
	});
}
function deleteGoal() {
	var del = confirm("Are you sure you want to delete ALL items belong to this goal??");
	if ( del == false ) {
		return;
	}
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "workflow_functions.php",
		data:       "action=delete_goal&goal_id=" + $("#goal_id").val() + "&sequence_id=" + $("#sequence_id").val() + "&workflow_id=" + $("#workflow_opt").val(),
		success:    function(obj) {
			$("#status_area").text(obj.msg);
			if ( obj.status == 0 ) {
				$("#edit_area").html("");
				$("#goal_dd").html(obj.goal_opt);
			}
		}
	});
}
function show_entrance_rule() {
	if ( $("#current_rule").val() == "entrance" ) {
		return;
	} else {
		$("#current_rule").val("entrance");
		$("#show_entrance_rule").css("background-color", "yellow");
		$("#show_inflight_rule").css("background-color", "#96d1f5");
		$("#show_exit_rule").css("background-color", "#96d1f5");
		$("#inflight_rule").hide();
		$("#exit_rule").hide();
		$("#entrance_rule").show();
		show_rules_dd(1);
	}
}
function show_inflight_rule() {
	if ( $("#current_rule").val() == "inflight" ) {
		return;
	} else {
		$("#current_rule").val("inflight");
		$("#show_entrance_rule").css("background-color", "#96d1f5");
		$("#show_inflight_rule").css("background-color", "yellow");
		$("#show_exit_rule").css("background-color", "#96d1f5");
		$("#exit_rule").hide();
		$("#entrance_rule").hide();
		$("#inflight_rule").show();
		show_rules_dd(2);
	}
}

function show_exit_rule() {
	if ( $("#current_rule").val() == "exit" ) {
		return;
	} else {
		$("#current_rule").val("exit");
		$("#show_entrance_rule").css("background-color", "#96d1f5");
		$("#show_inflight_rule").css("background-color", "#96d1f5");
		$("#show_exit_rule").css("background-color", "yellow");
		$("#inflight_rule").hide();
		$("#entrance_rule").hide();
		$("#exit_rule").show();
		show_rules_dd(3);
	}
}

function show_rules_dd(type) {
	var goal_id = $("#goal_id").val();
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		data:		'action=show_rules_dd&type=' + type + '&goal_id=' + goal_id,
		url:		'workflow_functions.php',
		success:	function(obj) {
			$("#rules_dd").html(obj.rules_dd);
			$("#canned_rule_desc").val(obj.desc);
		}
	});
}

function cloneWorkflow() {
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		data:		'action=clone&workflow_id=' + $("#workflow_id").val(),
		url:		'workflow_functions.php',
		success:	function(obj) {
			if ( obj.status == 0 ) {
				$("#workflow_dd").html(obj.workflow_opt);
				$("#workflow_id").val(obj.workflow_id);
				$("#sequence_dd").html(obj.sequence_opt);
			}
			$("#status_area").html(obj.msg);
		}
	});
}

function syncWorkflow() {
	var go = confirm("Do you want to sync this workflow for ALL products on newly added sequences/goals?")
	if ( go == false ) {
		return false;
	} 
	$("#sync").css("display", "none");
	$("#loading").css("display", "inline");
	$.ajax({
		type:       'GET',
		cache:      'false',
		dataType:   'json',
		data:       'workflow_id=' + $("#workflow_opt").val(),
		url:        'sync_workflow.php',
		success:    function(obj) {
			alert(obj.query_msg);
			$("#loading").css("display", "none");
			$("#sync").css("display", "inline");
		}
	});
}

</script>
 
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<form name='workflows' >
<table border=0 width='100%' height="70" align=center>
    <tr><!-- dropdown lists -->
      <td width='40%' align='center'>Workflows<br /><div id=workflow_dd style="float:left; margin-left:90px;" >
	    <SELECT id=workflow_opt name=workflow_opt onChange="workflowSelected();" >
		  <option value=-1>Select Workflow from List</option>
		  <option value=0>Add New Workflow</option>
		</SELECT></div>
	    <div style="float:left; margin: 0px 10px 0px 10px; display: none; " id=loading >
		  <img src="./images/loading.gif" width=25px height=25px />
		</div>
	  </td>
      <td width='30%' align='center'>Sequences<br /><div id=sequence_dd>
	    <SELECT id=sequence_opt name=sequence_opt disabled=true onChange="sequenceSelected();" >
		  <option value=-1>Select Sequence from List</option>
		</SELECT></div></td>
      <td width='30%' align='center'>Goals<br /><div id=goal_dd>
	    <SELECT id=goal_opt name=goal_opt disabled onChange="goalSelected();" >
		  <option value=-1>Select Goal from list</option>
		</SELECT></div></td>
    </tr>
    <tr>
      <td colspan=3 align='center'>
        <div style="float:left; margin: 5px 10px 0px 100px; display: none; " id=sync onClick="return syncWorkflow();">
		  <img src="./images/sync.png" width=25px height=25px /> <span style=" margin: 5px 10px 0px 10px;">Click to sync workflow</span>
		</div>
	  </td>
	</tr>
    <!-- end of dropdown lists -->
</table>
</form>
<table border=0 width=95% aligh=center>
  <tr>
    <td align=right><div id=status_area>&nbsp;</div></td>
  </tr>
  <tr>
    <td align=center><div id=edit_area></div></td>
  </tr>
</table>
</div></body>
