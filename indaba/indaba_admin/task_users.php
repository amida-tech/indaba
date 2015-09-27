<? 
// revert back to r4372, only swap user, do not clean up tables
session_start();
if ( ! isset( $_SESSION['authuser'] )) exit;

require_once("./include/config.inc");

if ( isset($_GET['action']) && $_GET['action'] == 'show_users' ) {
	$source_list = '';
	$s_sql = sprintf("SELECT u.* FROM user u, project_membership pm, task_role tr
						WHERE u.id = pm.user_id 
							AND pm.project_id = %d
							AND tr.task_id = %d
							AND tr.role_id = pm.role_id
							AND u.status = 1
							AND u.id NOT IN 
								(SELECT assigned_user_id FROM task_assignment 
										WHERE task_id = %d AND target_id = %d) 
						ORDER BY u.last_name ", 
				$_GET['project_id'], $_GET['task_id'], $_GET['task_id'], $_GET['target_id']);
	$st = @mysql_query($s_sql);
	$source_list = "";
	while ( $user = @mysql_fetch_assoc($st) ) {
		$source_list .= "<li user_id=" . $user['id'] . "  id=user_id_" . $user['id'] . ">" . $user['last_name'] . ", " . $user['first_name'] . "</li>";
	}
	$t_sql = sprintf("SELECT u.* FROM user u, task_assignment ta
						WHERE
							u.id = ta.assigned_user_id
							AND u.status = 1
							AND ta.task_id = %d
							AND ta.target_id = %d
							AND ta.assigned_user_id <> 0
						ORDER BY u.last_name ",
						$_GET['task_id'], $_GET['target_id']);
	$st = @mysql_query($t_sql);
	$target_list = "";
	while ( $user = mysql_fetch_assoc($st) ) {
		$target_list .= "<li user_id=" . $user['id'] . " id=assigned_user_id_" . $user['id'] . ">" . $user['last_name'] . ", " . $user['first_name'] . "</li>";
	}
	$rt = array('source_list' => $source_list, 'target_list' => $target_list, 'source_sql' => $s_sql, 'target_sql' => $t_sql);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == "saveAssignment" ) {
	$error = "";
	if ( isset($_GET['remove_from_assignment']) && count($_GET['remove_from_assignment']) > 0 ) {
		$delete_sql = sprintf("DELETE FROM task_assignment 
									WHERE task_id = %d 
										AND target_id = %d 
										AND assigned_user_id IN (", 
							$_GET['task_id'], $_GET['target_id']);
		foreach ( $_GET['remove_from_assignment'] as $user_id ) {
			$delete_sql .=  $user_id . ", ";
		}
		$delete_sql = preg_replace('/, $/', ')', $delete_sql);
		$del_st = mysql_query($delete_sql);
		if ( ! $del_st ) {
			$error = "DELETE ERROR: " . mysql_error();
		}
	}
	if ( isset($_GET['add_to_assignment']) ) {
		// init horse if it is not created already
		// get product_id
		$sql = "SELECT product_id FROM task WHERE id = " . $_GET['task_id'];
		$st = mysql_query($sql);
		$row = mysql_fetch_assoc($st);
		$product_id = $row['product_id'];
		require_once('horse_inc.php');
		sync_horse($_GET['target_id'], $product_id);
		$horse = get_horse($_GET['target_id'], $product_id);
		$horse_id = $horse['id'];
		// get goal_object_id based on horse_id and task_id
		$go_sql = sprintf("SELECT horse.id AS horse_id, goal_object.goal_id, goal_object.id AS goal_object_id, goal_object.status, goal.duration
							FROM horse, workflow_object, sequence_object, goal_object, task, goal
							WHERE horse.workflow_object_id = workflow_object.id
								AND workflow_object.id = sequence_object.workflow_object_id 
								AND sequence_object.id = goal_object.sequence_object_id 
								AND goal_object.goal_id = task.goal_id 
								AND goal_object.goal_id = goal.id
								AND horse.id = %d and task.id = %d",
						$horse_id, $_GET['task_id']);
		$go_st = mysql_query($go_sql);
		$go_row = mysql_fetch_assoc($go_st);
		// update task_assignment with horse_id and goal_object_id
		// if the goal object is started, set TA status to 1, start_time = now(), due_time = start_time + goal duration
		if ( $go_row['status'] == 2 ) {	// goal object already started
			$status = 1;
			$start_time = "now()";
			$due_time = "NOW() + INTERVAL " . $go_row['duration'] . " DAY";
		} else {
			$status = 0;
			$start_time = "NULL";
			$due_time = "NULL";
		}
		$insert_sql = "INSERT INTO task_assignment (task_id, target_id, assigned_user_id, q_last_assigned_time, status, horse_id, goal_object_id, start_time, due_time ) VALUES ";
		foreach ( $_GET['add_to_assignment'] as $user_id ) {
			$insert_sql .= sprintf("(%d, %d, %d, now(), %d, %d, %d, %s, %s),", 
								$_GET['task_id'], $_GET['target_id'], $user_id, $status, $horse_id, $go_row['goal_object_id'], $start_time, $due_time);
		}
		$insert_sql = preg_replace('/,$/', '', $insert_sql);
		$ins_st = mysql_query($insert_sql);
		if ( ! $ins_st ) {
			$error .= " INSERT ERROR: " . mysql_error();
		} else {
			$delete_sql = sprintf("DELETE FROM task_assignment WHERE task_id = %d AND target_id = %s AND assigned_user_id = 0",
					$_GET['task_id'], $_GET['target_id']);
			$del_st = mysql_query($delete_sql);
		}
	}
	// $rt = array("delete_sql" => $delete_sql, "insert_sql" => $insert_sql, 'status_msg' => $error);
	if ( $error == '' ) {
		$error = "Assignment saved";
	}
	$rt = array('status_msg' => $error);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == "switchUser" ) {
	$sql = sprintf("UPDATE task_assignment SET assigned_user_id = %d, q_last_assigned_time = now(), q_last_assigned_uid = %d
						WHERE task_id = %d AND target_id = %s AND assigned_user_id = %d",
				$_GET['new_user'], $_GET['original_user'], $_GET['task_id'], $_GET['target_id'], $_GET['original_user']);
	mysql_query($sql);
	if ( mysql_affected_rows() == 1 ) {
		$error = 0;
		$msg = "User switched successfully";
	} else {
		$error = 1;
		$msg = mysql_error();
	}
	echo json_encode(array('sql' => $sql, 'error' => $error, 'msg' => $msg));
	exit;
}

$sql = sprintf("SELECT * FROM project where id = '%s'", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$project = @mysql_fetch_assoc($st);


$sql = sprintf("SELECT a.target_id as target_id, b.name as name, b.description as description FROM project_target a, target b WHERE project_id = '%s' AND a.target_id = b.id", $_GET['project_id']);
$targetOpt = "<SELECT id=target_id onChange='targetSelected();' ><option value=''>Select target for user assignment</option>";
$target_st = @mysql_query($sql, $indaba_dbh);
while ( $target = @mysql_fetch_assoc($target_st) ) {
	$targetOpt .= "<option name=a value='" . $target['target_id'] . "'>" . $target['name'] . "</option>";
}
$targetOpt .= "</SELECT>";


?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Managing Users for Task <? echo $project['code_name'] . " - " . $_GET['product_name'] . " - " . $_GET['task_name']; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script language=javascript>

$(function() {
    $("#target_list").selectable({
        stop: function () {
			var i = 0;
			var j = 0;
            $("#source_list .ui-selected").each( function () {
                //$(this).removeClass("ui-selected");
				i++;
            })
            $("#target_list .ui-selected").each( function () {
				j++;
			})
			if ( i == 1 && j == 1 ) {
				$("#switchuser").attr("disabled", false);
			} else {
				$("#switchuser").attr("disabled", true);
			}
        }
    });
});     
$(function() {
    $("#source_list").selectable({
        stop: function () {
			var i = 0;
			var j = 0;
            $("#target_list .ui-selected").each( function () {
				i++;
            })
            $("#source_list .ui-selected").each( function () {
				j++;
			})
			if ( i == 1 && j == 1 ) {
				$("#switchuser").attr("disabled", false);
			} else {
				$("#switchuser").attr("disabled", true);
			}
        }
    });
});

function add_to_target() {
    $("#source_list").find('.ui-selected').each( function() {
        var add_clone = $(this).clone();
        $(add_clone).appendTo("#target_list");
        $(this).remove();
    });
}

function add_to_source() {
    $("#target_list").find('.ui-selected').each( function() {
        var add_clone = $(this).clone();
        $(add_clone).appendTo("#source_list");
        $(this).remove();
    });
}

function targetSelected(id) {
	var target_id = $("#target_id").val();
	if ( target_id <= 0 || id == -1 ) {
		$("#source_list").html('');
		$("#target_list").html('');
		return;
	}
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"task_users.php",
		data:		"action=show_users&task_id=" + $("#task_id").val() + "&target_id=" + $("#target_id").val() + "&project_id=" + $("#project_id").val(),
		success:	function(obj) {
			$("#source_list").html(obj.source_list);
			$("#target_list").html(obj.target_list);
		}
	});
}

function saveAssignment() {
	var q_string = "&task_id=" + $("#task_id").val() + "&target_id=" + $("#target_id").val();
	$("#target_list").find('li').each( function() {
								var this_id = $(this).attr("id");
								if ( this_id.search(/user_id/) == 0 ) {
									q_string += "&add_to_assignment[]=" + this_id.replace("user_id_", "");
								}
						});
	$("#source_list").find('li').each( function() {
								var this_id = $(this).attr("id");
								if ( this_id.search(/assigned/) == 0 ) {
									q_string += "&remove_from_assignment[]=" + this_id.replace("assigned_user_id_", "");
								}
						});
	// alert(q_string);
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"task_users.php",
		data:		"action=saveAssignment" + q_string,
		success:	function(obj) {
			$("#status_area").html( obj.status_msg );
			targetSelected($("#target_id").val());
		}
	});
}

function close_window() {
	var changed = ""
	$("#target_list").find('li').each( function() {
		var this_id = $(this).attr("id");
		if ( this_id.search(/user_id/) == 0 ) {
			changed = "changed";
		}
	});
	$("#source_list").find('li').each( function() {
		var this_id = $(this).attr("id");
		if ( this_id.search(/assigned/) == 0 ) {
			changed = "changed";
		}
	});
	if ( changed == "changed" ) {
		var go = confirm("You have not saved changes, do you want to discard the changes and close the window?");
		if ( go == false ) {
			return;
		}
	}
	self.close();
}

function switchUser() {
	var go = confirm("Are you sure you want to switch the user for the task? The change will be saved to the database directly.");
	if ( ! go ) {
		return;
	} 
	var q_string;
	var s_user_id = $("#source_list .ui-selected").attr("user_id");
	var t_user_id = $("#target_list .ui-selected").attr("user_id");
	var base = "&task_id=" + $("#task_id").val() + "&target_id=" + $("#target_id").val() + "&project_id=" + $("#project_id").val();
	var q_string = base + "&original_user=" + t_user_id + "&new_user=" + s_user_id;
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"task_users.php",
		data:		"action=switchUser" + q_string,
		success:	function(obj) {
			if ( obj.error == 0 ) {
				targetSelected($("#target_id").val());
			}
			$("#status_area").html( obj.msg );
		}
	});
}

</script>
</head>
<body>

<form name="manage_users">
    <table width="600" border="0">
        <tr>
            <td colspan="3" align="left" valign="top"><p>Please select target from the dropdown list first, then select users from the list, click on the buttons to move user in/out task. Click "Save Changes" buttons to save the changes you made.</p>
			  <p>When user is removed from the project, the user targets for this project will be removed as well as team membership.</p>
			</td>
        </tr>
		<tr>
			<td colspan=3 align="center">Targets for project <? echo $project['code_name'];?><br><? echo $targetOpt; ?>
				<INPUT type=hidden id=project_id value=<? echo $_GET['project_id']; ?> />
				<INPUT type=hidden id=task_id value=<? echo $_GET['task_id']; ?> />
			</td>
		</tr>
		<tr><td colspan=3 align=right><div id=status_area></div></td></tr>
        <tr>
            <td width="280" align="center" valign="top">Available Users </td>
            <td width="40" align="center" valign="middle"></td>
            <td width="280" align="center" valign="top">Users on Task </td>
        </tr>
        <tr>
            <td align="center" valign="top"><div id=source_list style="max-height:200px;" ></div>
			</td>
            <td align="center" valign="top">
				<div style='width:40px; height: 200px;'>
                    <div style='float:left; width:40px; height:20px; margin-top:30px; font-size:0.8em ' class=btn onClick='add_to_target();' > &gt;&gt; </div>
                    <div style='float:left; width:40px; height:20px; margin-top:70px; font-size:0.8em ' class=btn onClick='add_to_source();' > &lt;&lt; </div>
                </div>
            </td>
            <td align="center" valign="top"><div id=target_list style="max-height:200px;" ></div> </td>
        </tr>
        <tr>
            <td align="center" colspan="3">&nbsp;</td>
		</tr>
		<tr>
            <td align="right" ><input type="button" class=btn value="Save Changes" onClick="saveAssignment();"></td>
            <td align="center" ><input type="reset" class=btn value="Cancel Changes" onClick="return targetSelected(-1);"  /></td>
            <td align="left" ><input type="button" id=switchuser name=switchuser class=btn value="Switch User" disabled=true onClick="switchUser();"  /></td>
		</tr>
		<tr>
            <td align="center" colspan="3">&nbsp;</td>
		</tr>
		<tr>
            <td align="center" colspan="3"><input type="button" class=btn value="Done and Close Window" onClick="close_window();"></td>
        </tr>
    </table>
	<INPUT type=hidden name=project_id value='<? echo $project['id']; ?>' />
</form>
</body>
</html>
