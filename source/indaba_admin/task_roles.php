<? 
session_start();
//if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authrole'] )) exit;
// if ( !isset($_GET['project_id']) || !isset($_GET['task_id']) ) exit;

require_once("./include/config.inc");

if ( isset($_GET['action']) && $_GET['action'] == 'show_roles' ) {
	$source_list = '';
	$s_sql = sprintf("SELECT * FROM role 
						WHERE id NOT IN 
								(SELECT role_id FROM task_role WHERE task_id = %d)",
				$_GET['task_id']);
	$st = @mysql_query($s_sql);
	$source_list = "";
	while ( $role = @mysql_fetch_assoc($st) ) {
		$source_list .= "<li id=role_id_" . $role['id'] . ">" . $role['name'] .
					" - " . $role['description'] . "</li>";
	}
	$t_sql = sprintf("SELECT r.* FROM role r, task_role tr
						WHERE
							r.id = tr.role_id
							AND tr.task_id = %d",
						$_GET['task_id']);
	$st = @mysql_query($t_sql);
	$target_list = "";
	while ( $role = mysql_fetch_assoc($st) ) {
		$target_list .= "<li id=assigned_role_id_" . $role['id'] . ">" . $role['name'] .
					" - " . $role['description'] . "</li>";
	}
	$rt = array('source_list' => $source_list, 'target_list' => $target_list, 'source_sql' => $s_sql, 'target_sql' => $t_sql);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == "saveAssignment" ) {
	$error = "";
	if ( isset($_GET['remove_from_assignment']) && count($_GET['remove_from_assignment']) > 0 ) {
		$delete_sql = sprintf("DELETE FROM task_role 
									WHERE task_id = %d 
										AND role_id IN (", 
							$_GET['task_id']);
		foreach ( $_GET['remove_from_assignment'] as $role_id ) {
			$delete_sql .=  $role_id . ", ";
		}
		$delete_sql = preg_replace('/, $/', ')', $delete_sql);
		$del_st = mysql_query($delete_sql);
		if ( ! $del_st ) {
			$error = "DELETE ERROR: " . mysql_error();
		}
	}
	if ( isset($_GET['add_to_assignment']) ) {
		$insert_sql = "INSERT INTO task_role (task_id, role_id ) VALUES ";
		foreach ( $_GET['add_to_assignment'] as $role_id ) {
			$insert_sql .= sprintf("(%d, %d),", $_GET['task_id'], $role_id);
		}
		$insert_sql = preg_replace('/,$/', '', $insert_sql);
		$ins_st = mysql_query($insert_sql);
		if ( ! $ins_st ) {
			$error .= " INSERT ERROR: " . mysql_error();
		}
	}
	// $rt = array("delete_sql" => $delete_sql, "insert_sql" => $insert_sql, 'status_msg' => $error);
	$rt = array('status_msg' => $error);
	echo json_encode($rt);
	exit;
}

?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Managing Roles for Task <? echo $_GET['project_name'] . " - " . $_GET['product_name'] . " - " . $_GET['task_name']; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script language=javascript>

$(function() {
	$("#target_list").selectable({
		stop: function () {
			$("#source_list").children().each( function () {
				$(this).removeClass("ui-selected");
			})
		}
	});
});
$(function() {
	$("#source_list").selectable({
		stop: function () {
			$("#target_list").children().each( function () {
				$(this).removeClass("ui-selected");
			})
		}
	});
});


$(document).ready( function() {
	load_lists();
});

function load_lists() {
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"task_roles.php",
		data:		"action=show_roles&task_id=" + $("#task_id").val(),
		success:	function(obj) {
			$("#source_list").html(obj.source_list);
			$("#target_list").html(obj.target_list);
		}
	});
}

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

function saveAssignment() {
	var q_string = "&task_id=" + $("#task_id").val() + "&target_id=" + $("#target_id").val();
	$("#target_list").find('li').each( function() {
								var this_id = $(this).attr("id");
								if ( this_id.search(/role_id/) == 0 ) {
									q_string += "&add_to_assignment[]=" + this_id.replace("role_id_", "");
								}
						});
	$("#source_list").find('li').each( function() {
								var this_id = $(this).attr("id");
								if ( this_id.search(/assigned/) == 0 ) {
									q_string += "&remove_from_assignment[]=" + this_id.replace("assigned_role_id_", "");
								}
						});
	// alert(q_string);
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"task_roles.php",
		data:		"action=saveAssignment" + q_string,
		success:	function(obj) {
			if ( obj.error ) {
				$("#status_area").html( obj.error );
			} else {
				$("#status_area").html("Saved");
				load_lists();
			}
		}
	});
}

function close_window() {
	var changed = ""
	$("#target_list").find('li').each( function() {
		var this_id = $(this).attr("id");
		if ( this_id.search(/role_id/) == 0 ) {
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


</script>
</head>
<body>

<form name="manage_roles">
    <table width="600" border="0">
        <tr>
            <td colspan="3" align="left" valign="top"><p>Please select roles from the lists and then click the buttons to move them in/out task.<br />
														When finished, click on "Save Changes" button to save the changes.</p>
			</td>
        </tr>
		<tr>
			<td colspan=3 align="center">
				<INPUT type=hidden id=task_id value=<? echo $_GET['task_id']; ?> />
			</td>
		</tr>
		<tr><td colspan=3 align=right><div id=status_area></div></td></tr>
        <tr>
            <td align="center" valign="top">Available Roles </td>
            <td align="center" valign="middle"></td>
            <td align="center" valign="top">Roles on Task </td>
        </tr>
        <tr>
            <td align="center" valign="top"><div id=source_list ></div>
			</td>
            <td align="center" valign="top">
				<div style='width:40px; height: 200px;'>
					<div style='float:left; width:40px; height:20px; margin-top:30px; font-size:0.8em ' class=btn onClick='add_to_target();' > &gt;&gt; </div>
					<div style='float:left; width:40px; height:20px; margin-top:70px; font-size:0.8em ' class=btn onClick='add_to_source();' > &lt;&lt; </div>
				</div>
            </td>
            <td align="center" valign="top"><div id=target_list ></div> </td>
        </tr>
        <tr>
            <td align="center" colspan="3">&nbsp;</td>
		</tr>
		<tr>
            <td align="right" ><input type="button" class=btn value="Save Changes" onClick="saveAssignment();"></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
            <td align="left" ><input type="reset" class=btn value="Cancel Changes" onClick="return window.location.reload();"  /></td>
		</tr>
		<tr>
            <td align="center" colspan="3">&nbsp;</td>
		</tr>
		<tr>
            <td align="center" colspan="3"><input type="button" class=btn value="Done and Close Window" onClick="close_window();"></td>
        </tr>
    </table>
</form>
</body>
</html>
