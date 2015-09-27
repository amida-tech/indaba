<?php

require_once("include/config.inc");

$p_name = array( '0' => "No", '1' => 'Yes', '2' => 'Undefined');
$vp_name = array( '0' => "None", '1' => 'Limited', '2' => 'Full', '3' => 'Full+');
if ( isset($_GET['action']) && $_GET['action'] == 'show_access_matrix_opt' ) {
	$access_matrix_opt = show_access_matrix_opt(-1);
	echo $access_matrix_opt;
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_view_matrix_opt' ) {
	$view_matrix_opt = show_view_matrix_opt(-1);
	echo $view_matrix_opt;
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_access_matrix' ) {
	$rt = show_access_matrix($_GET['access_matrix_id']);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'add_access_permission' ) {
	$sql = sprintf("REPLACE INTO access_permission (access_matrix_id, role_id, rights_id , permission)
						VALUES (%d, %d, %d, %d)",
				$_GET['access_matrix_id'], $_GET['role_id'], $_GET['rights_id'], $_GET['permission']);
	$st = @mysql_query($sql);
	if ( $st ) {
		$rt['status'] = 0;
		$rt['form'] = access_rights($_GET['access_matrix_id'], $_GET['access_matrix_name'] );
	} else {
		$rt['status'] = 1;
	}
	echo json_encode($rt);
	exit;
}
if ( isset($_GET['action']) && $_GET['action'] == 'clone_access_matrix' ) {
	$name = SQLString($_GET['name'] . "-clone", "text");
	$r_name = $_GET['name'] . "-clone";
	$description = SQLString($_GET['description'], 'text');
	$rt = save_access_matrix(0, $name, $description, $_GET['default_value']);
	if ( $rt['status'] == 1 ) {
		$rt['msg'] = "Error cloning access matrix: " . mysql_error();
		echo json_encode($rt);
		exit;
	}
	// now clone permissions
	$sql = sprintf("INSERT INTO access_permission ( access_matrix_id, role_id, rights_id, permission)
						SELECT %d, role_id, rights_id, permission 
							FROM access_permission
							WHERE access_matrix_id = %d",
					$rt['access_matrix_id'], $_GET['access_matrix_id']);
	$st = mysql_query($sql);
	$rt['rights_form'] = access_rights($rt['access_matrix_id'], $name);
	if ( $st) {
		$rt['msg'] = "Action Right and Permissins have been cloned.";
	} else {
		$rt['msg'] = "Cloned Action Right but error when cloning permissions: " . mysql_error();
	}
	$rt['p_sql'] = $sql;
	$rt['name'] = $r_name;
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'del_access_permission' ) {
	$sql = sprintf("DELETE FROM access_permission WHERE id = %d", $_GET['permission_id']);
	$st = @mysql_query($sql);
	if ( $st ) {
		$rt['status'] = 0;
		$rt['form'] = access_rights($_GET['access_matrix_id'], $_GET['access_matrix_name'] );
	} else {
		$rt['status'] = 1;
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'save_access_matrix') {
	$access_matrix_id = isset($_GET['access_matrix_id']) ? $_GET['access_matrix_id'] : '';
	$name = SQLString($_GET['name'], "text");
	$description = SQLString($_GET['description'], 'text');
	$rt = save_access_matrix($access_matrix_id, $name, $description, $_GET['default_value']);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'delete_access_matrix' ) {
	// check if this access_matrix_id is used in goal
	$sql = sprintf("SELECT * FROM goal WHERE access_matrix_id = %d", $_GET['access_matrix_id']);
	$st = @mysql_query($sql);
	$goal_names = '';
	while ( $row = @mysql_fetch_assoc($st) ) {
		$goal_names .= " " . $row['name'];
	}
	if ( strlen($goal_names) > 0 ) {
		$rt['status'] = 1;
		$rt['msg'] = "This Matrix is used by goal: " . $goal_names . ". Please unassign before delete.";
		echo json_encode($rt);
		exit;
	}
	// check if it is used by project
	$sql = sprintf("SELECT * FROM project WHERE access_matrix_id = %d", $_GET['access_matrix_id']);
	$st = @mysql_query($sql);
	$project_names = '';
	while ( $row = @mysql_fetch_assoc($st) ) {
		$project_names .= $row['code_name'];
	}
	if ( strlen($project_names) > 0 ) {
		$rt['status'] = 1;
		$rt['msg'] = "This Matrix is used by projects: " . $project_names . ". Please unassign before delete.";
		echo json_encode($rt);
		exit;
	}
	// check if it is used by product
	$sql = sprintf("SELECT * FROM product WHERE access_matrix_id = %d", $_GET['access_matrix_id']);
	$st = @mysql_query($sql);
	$product_names = '';
	while ( $row = @mysql_fetch_assoc($st) ) {
		$product_names .= $row['name'];
	}
	if ( strlen($product_names) > 0 ) {
		$rt['status'] = 1;
		$rt['msg'] = "This Matrix is used by products: " . $product_names . ". Please unassign before delete.";
		echo json_encode($rt);
		exit;
	}

	$sql = sprintf("DELETE FROM access_matrix, access_permission
				USING access_matrix LEFT JOIN access_permission 
					ON  access_matrix.id = access_permission.access_matrix_id 
				WHERE access_matrix.id = %d", $_GET['access_matrix_id']);
	$st = @mysql_query($sql);
	$rt['sql'] = $sql;
	if ( $st ) {
		$rt['status'] = 0;
		$rt['msg'] = "Matrix " . $_GET['name'] . " is deleted.";
		$rt['access_matrix_opt'] = show_access_matrix_opt(-1);
	} else {
		$rt['status'] = 1;
		$rt['msg'] = "Error delete " . $_GET['name'] . ": " . mysql_error();
	}
	echo json_encode($rt);
	exit;
}

// VIEW MATRIX

if ( isset($_GET['action']) && $_GET['action'] == 'show_view_matrix' ) {
	$rt = show_view_matrix($_GET['view_matrix_id']);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'add_view_permission' ) {
	$sql = sprintf("REPLACE INTO view_permission (view_matrix_id, subject_role_id, target_role_id, permission)
						VALUES (%d, %d, %d, %d)",
				$_GET['view_matrix_id'], $_GET['subject_role_id'], $_GET['target_role_id'], $_GET['permission']);
	$st = @mysql_query($sql);
	if ( $st ) {
		$rt['status'] = 0;
		$rt['form'] = view_rights($_GET['view_matrix_id'], $_GET['view_matrix_name'] );
	} else {
		$rt['status'] = 1;
		$rt['msg'] = mysql_error();
	}
	echo json_encode($rt);
	exit;
}
if ( isset($_GET['action']) && $_GET['action'] == 'del_view_permission' ) {
	$sql = sprintf("DELETE FROM view_permission WHERE id = %d", $_GET['permission_id']);
	$st = @mysql_query($sql);
	if ( $st ) {
		$rt['status'] = 0;
		$rt['form'] = view_rights($_GET['view_matrix_id'], $_GET['view_matrix_name'] );
	} else {
		$rt['status'] = 1;
		$rt['msg'] = mysql_error();
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'save_view_matrix') {
	$view_matrix_id = isset($_GET['view_matrix_id']) ? $_GET['view_matrix_id'] : '';
	$name = SQLString($_GET['name'], "text");
	$description = SQLString($_GET['description'], 'text');
	$rt = save_view_matrix($view_matrix_id, $name, $description, $_GET['default_value']);
	echo json_encode($rt);
	exit;
}
if ( isset($_GET['action']) && $_GET['action'] == 'clone_view_matrix' ) {
	$name = SQLString($_GET['name'] . "-clone", "text");
	$r_name = $_GET['name'] . "-clone";
	$description = SQLString($_GET['description'], 'text');
	$rt = save_view_matrix(0, $name, $description, $_GET['default_value']);
	if ( $rt['status'] == 1 ) {
		$rt['msg'] = "Error cloning view matrix: " . mysql_error();
		echo json_encode($rt);
		exit;
	}
	// now clone permissions
	$sql = sprintf("INSERT INTO view_permission ( view_matrix_id, subject_role_id, target_role_id, permission)
						SELECT %d, subject_role_id, target_role_id, permission 
							FROM view_permission
							WHERE view_matrix_id = %d",
					$rt['view_matrix_id'], $_GET['view_matrix_id']);
	$st = mysql_query($sql);
	$rt['rights_form'] = view_rights($rt['view_matrix_id'], $name);
	if ( $st) {
		$rt['msg'] = "User Privacy and Permissins have been cloned.";
	} else {
		$rt['msg'] = "Cloned User Privacy but error when cloning permissions: " . mysql_error();
	}
	$rt['p_sql'] = $sql;
	$rt['name'] = $r_name;
	echo json_encode($rt);
	exit;
}


if ( isset($_GET['action']) && $_GET['action'] == 'delete_view_matrix' ) {
	// check if this view_matrix_id is used in goal
	$sql = sprintf("SELECT * FROM goal WHERE view_matrix_id = %d", $_GET['view_matrix_id']);
	$st = @mysql_query($sql);
	$goal_names = '';
	while ( $row = @mysql_fetch_assoc($st) ) {
		$goal_names .= " " . $row['name'];
	}
	if ( strlen($goal_names) > 0 ) {
		$rt['status'] = 1;
		$rt['msg'] = "This Matrix is used by goal: " . $goal_names . ". Please unassign before delete.";
		echo json_encode($rt);
		exit;
	}
	// check if it is used by project
	$sql = sprintf("SELECT * FROM project WHERE view_matrix_id = %d", $_GET['view_matrix_id']);
	$st = @mysql_query($sql);
	$project_names = '';
	while ( $row = @mysql_fetch_assoc($st) ) {
		$project_names .= $row['code_name'];
	}
	if ( strlen($project_names) > 0 ) {
		$rt['status'] = 1;
		$rt['msg'] = "This Matrix is used by projects: " . $project_names . ". Please unassign before delete.";
		echo json_encode($rt);
		exit;
	}
	$sql = sprintf("DELETE FROM view_matrix, view_permission
				USING view_matrix LEFT JOIN view_permission 
					ON  view_matrix.id = view_permission.view_matrix_id 
				WHERE view_matrix.id = %d", $_GET['view_matrix_id']);
	$st = @mysql_query($sql);
	$rt['sql'] = $sql;
	if ( $st ) {
		$rt['status'] = 0;
		$rt['msg'] = "Matrix " . $_GET['name'] . " is deleted.";
		$rt['view_matrix_opt'] = show_view_matrix_opt(-1);
	} else {
		$rt['status'] = 1;
		$rt['msg'] = "Error delete " . $_GET['name'] . ": " . mysql_error();
	}
	echo json_encode($rt);
	exit;
}



////////////// 
// Functions
//////////////

function show_access_matrix_opt($access_matrix_id) {
	$access_matrix_opt = "<SELECT id=access_matrix_id onChange='access_matrixSelected();'>
				<option value=-1>Select Action Right from the list</option>";
	$access_matrix_opt .= "<option value=0>Add New Action Right</option>";
	$sql = "SELECT * FROM access_matrix ORDER BY name";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$selected = $access_matrix_id == $row['id'] ? " selected=selected " : "";
		$access_matrix_opt .= "<option value=" . $row['id'] . $selected . ">" . $row['name'] . "</option>";
	}
	$access_matrix_opt .= "</SELECT>";
	return $access_matrix_opt;
}

function show_access_matrix($access_matrix_id) {
	$sql="";
	if ( is_numeric($access_matrix_id) && $access_matrix_id > 0 ) {
		$sql = "SELECT * FROM access_matrix WHERE id = " . $access_matrix_id;
		$st = @mysql_query($sql);
		$row = @mysql_fetch_assoc($st);
	}

	$form = <<< EOF_FORM
	<script language="javascript"  type="text/javascript">
	function addPermission() {
		var q_string = "&access_matrix_id=" + $("#access_matrix_id").val();
		q_string += "&access_matrix_name=" + $("#name").text();
		var rights_id = $("#rights_id").val();
		q_string += "&rights_id=" + rights_id;
		var role_id =  $("#role_id").val();
		q_string += "&role_id=" + role_id;
		q_string += "&permission=" + $("#permission").val();
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:       "action=add_access_permission" + q_string,
			success:    function(obj) {
							if ( obj.status == 0 ) {
								$("#rights_area p").html(obj.form);
							}
							$("#rights_id").val(rights_id);
							$("#role_id").val(role_id);
			}
		});
	}
	function deletePermission(permission_id) {
		var q_string = "&access_matrix_id=" + $("#access_matrix_id").val();
		q_string += "&access_matrix_name=" + $("#name").text();
		q_string += "&permission_id=" + permission_id;
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:       "action=del_access_permission" + q_string,
			success:    function(obj) {
							if ( obj.status == 0 ) {
								$("#rights_area p").html(obj.form);
							}
			} 
		});
	} 
	function save_access_matrix() {
		var q_string = "action=save_access_matrix";
		if ( $("#name").val() == "" ) {
			alert("Please enter a name for the Matrix");
			return;
		}
		if ( $("#description").val() == "" ) {
			alert("Please enter description for the Matrix");
			return;
		}
		if ( isNaN($("#default_value:checked").val())  ) {
			alert("Please select a default value for the Matrix");
			return;
		}
		q_string += "&access_matrix_id=" + $("#access_matrix_id").val();
		q_string += "&name=" + $("#name").val();
		q_string += "&description=" + $("#description").val();
		q_string += "&default_value=" + $("#default_value:checked").val();
		//alert(q_string);
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:		q_string,
			success:    function(obj) {
				$("#status_area").html(obj.msg);
				if ( obj.status == 0 ) {
					$("#access_matrix_dd").html(obj.access_matrix_opt);
					$("#edit_area").html(obj.form);
					$("#rights_area p").html(obj.rights_form);
				}
			}
		})
		return;
	}

	function delete_access_matrix() {
		var go = confirm("Do you want to delete the Matrix?");
		if ( go == false ) {
			return;
		}
		var q_string = "action=delete_access_matrix";
		q_string += "&access_matrix_id=" + $("#access_matrix_id").val();
		q_string += "&name=" + $("#name").val();
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:       q_string,
			success:    function(obj) {
				$("#status_area").html(obj.msg);
				if ( obj.status == 0 ) {
					$("#access_matrix_dd").html(obj.access_matrix_opt);
					$("#edit_area").html('');
					$("#rights_area p").html('');
				}
			}
		});
		return;
	}

	</script>

	<form><fieldset><legend>CREATE/EDIT Action Right</legend>
	<table width="100%"  border="0">
		<tr>
			<td>Name</td>
			<td><INPUT type=text id=name size=20 value="NAME" onKeyUp="$('#status_area').html('Change not saved');" /></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><INPUT type=text id=description size=40 value="DESCRIPTION" onKeyUp="$('#status_area').html('Change not saved');" /></td>
		</tr>
		<tr>
			<td>Default Value</td>
			<td><table width="100%"  border="0">
					<tr>
						<td>No <INPUT type=radio id=default_value value="0" name=default_value 
								NO_CHECKED onChange="$('#status_area').html('Change not saved');" /></td>
						<td>Yes <INPUT type=radio id=default_value value="1" name=default_value 
								YES_CHECKED onChange="$('#status_area').html('Change not saved');" /></td>
						<td>Undefined <INPUT type=radio id=default_value value="2" name=default_value 
								UD_CHECKED onChange="$('#status_area').html('Change not saved');" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td colspan=2>&nbsp;</td></tr>
		<tr>
			<td colspan=2 align=center>
				<table border="0" width="80%">
				  <tr><td align=center>
						<INPUT type=button id=save value="Save" class=btn onClick="save_access_matrix();" /></td>
					  <td align=center>
						<INPUT type=button id=delete value="Delete" class=btn onClick="delete_access_matrix();" /></td>
					  <td align=center>
						<INPUT type=reset id=reset class=btn value="Reset" onClick="$('#status_area').html('');" /></td>
					  <td align=center>
					    <INPUT type=button id=clone value="Clone Action Right" style='display:none;' class=btn onClick="clone_access_matrix();" /></td>
				  </tr></table>
			</td>
		</tr>
	</table>
	</form></fieldset>
EOF_FORM;
	$name = isset($row['name']) ? $row['name'] : "";
	$description = isset($row['description']) ? $row['description'] : "";
	$no_checked = isset($row['default_value']) && $row['default_value'] == 0 ? "checked" : "";
	$yes_checked = isset($row['default_value']) && $row['default_value'] == 1 ? "checked" : "";
	$ud_checked = isset($row['default_value']) && $row['default_value'] == 2 ? "checked" : "";
	// replace the content
	$form = preg_replace("/NAME/", $name, $form);
	$form = preg_replace("/DESCRIPTION/", $description, $form);
	$form = preg_replace("/NO_CHECKED/", $no_checked, $form);
	$form = preg_replace("/YES_CHECKED/", $yes_checked, $form);
	$form = preg_replace("/UD_CHECKED/", $ud_checked, $form);
	
	// get rights
	$rights_form = "";
	if ( $access_matrix_id > 0 ) {
		$rights_form = access_rights($access_matrix_id, $name);
	}
	// return array
	$rt = array('status'		=> '',
				'form'			=> $form,
				'rights_form'	=> $rights_form,
				'sql'			=> $sql
				);
	return $rt;
}

function access_rights($matrix_id, $matrix_name ) {
	global $p_name;
	$rights_form = <<< EOF_FORM
	<script language="javascript"  type="text/javascript">
	  function filterRights() {
		  var right_like = $("#search_rights").val();
		  var role_like = $("#search_roles").val();
		  $("#rights_table tr").each(function() {
			  var row_text = $(this).find("td:first").html();
			  var role_text = $(this).find("td:eq(1)").html();
			  if ( row_text.search(new RegExp(right_like, 'i')) < 0 || role_text.search(new RegExp(role_like, 'i')) < 0 ) {
				  $(this).hide();
			  } else {
				  $(this).show();
			  }
		  });
	  }
	  function filterRoles() {
		  var role_like = $("#search_roles").val();
		  var right_like = $("#search_rights").val();
		  $("#rights_table tr").each(function() {
			  var row_text = $(this).find("td:eq(1)").html();
			  var right_text = $(this).find("td:eq(0)").html();
			  if ( row_text.search(new RegExp(role_like, 'i')) < 0 || row_text.search(new RegExp(right_like, 'i')) < 0 ) {
				  $(this).hide();
			  } else {
				  $(this).show();
			  }
		  });
	  }

	</script>
	<p>&nbsp;</p>
	<form><fieldset><legend>Manage Permission for ACCESS_MATRIX_NAME</legend>
	  <table border=0 width=100%>
	    <tr>
		  <td align=center width="30%">Rights</td>
		  <td align=center width="25%">Role</td>
		  <td align=center width="20%">Value</td>
		  <td align=center width="25%">&nbsp;</td>
		</tr>
	    <tr>
		  <td align=center width="30%">RIGHT_DD</td>
		  <td align=center width="25%">ROLE_DD</td>
		  <td align=center width="20%">VALUE_DD</td>
		  <td align=center width="25%"><INPUT type=button id=add_right class=btn value="Add/Update Permission" onClick="addPermission();" /></td>
		</tr>
		<tr><td colspan=4>&nbsp;</td>
		<tr><td colspan=2 align=left >Filter Rights &nbsp;<INPUT id=search_rights type=text size=20 onKeyUp="filterRights();" /></td>
		    <td colspan=2 align=left >Filter Roles &nbsp;<INPUT id=search_roles type=text size=20 onKeyUp="filterRoles();" /></td>
		<tr>
		  <td colspan=4 align=center>
		    <table border=0 width="100%">
			  <tr>
			    <td width="40%" align=center>RIGHT</td>
				<td width="25%" align=center>ROLE</td>
				<td width="15%" align=center>VALUE</td>
				<td width="15%" align=center>ACTION</td>
			  </tr>
			</table>
			<div style="border:1px black solid; width:100%; height:300px; overflow:auto;">
				RIGHTS_TABLE
			</div>
		  </td>
		</tr>
	  </table>
	</form></fieldset>
EOF_FORM;
	
	$sql = sprintf("SELECT r.label as rights, ro.name as role, ap.id as permission_id, ap.permission
				FROM access_permission ap, role ro, rights r 
				WHERE ap.role_id = ro.id 
					AND ap.rights_id = r.id 
					AND ap.access_matrix_id = %d
				ORDER BY rights, role ", $matrix_id);
	$rights_table = "<table id=rights_table border='2' width='100%' class=box>";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$row['permission'] = isset($row['permission']) ? $row['permission'] : 2;
		$rights_table .= "<tr><td width='40%' align=center>" . $row['rights'] . "</td>";
		$rights_table .= "<td width='25%' align=center>" . $row['role'] . "</td>";
		$rights_table .= "<td width='15%' align=center>" . $p_name[$row['permission']] . "</td>";
		$rights_table .= "<td width='15%' align=center class=btn onClick='deletePermission(" . $row['permission_id'] .");' >Delete</td></tr>";
	}
	$rights_table .= "</table>";
	// get rights drop down
	$right_dd = "<div style='float:left;'><SELECT id=rights_id>";
	$sql = "SELECT * FROM rights ORDER BY name";
	$st = @mysql_query($sql);
	$hidden = '';
	while ( $row = @mysql_fetch_assoc($st) ) {
		$right_dd .= "<option value=" . $row['id'] . ">" . $row['label'] . "</option>";
		$hidden .= "<INPUT type=hidden id=rights_id_" . $row['id'] . " value='" . $row['description'] . "' />";
	}
	$right_dd .= "</SELECT></div><div id=showRight class=btn style='width:15px; height:15px; float:left;'>?</div>";
	$right_dd .= $hidden;
	
	// get roles drop down
	$role_dd = "<SELECT id=role_id>";
	$sql = "SELECT * FROM role";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$role_dd .= "<option value=" . $row['id'] . ">" . $row['name'] . "</option>";
	}
	$role_dd .= "</SELECT>";
	
	// get value drop down
	$value_dd = "<SELECT id=permission>";
	foreach ( $p_name as $key => $value ) {
		$value_dd .= "<option value=" . $key . ">" . $value . "</option>";
	}
	$value_dd .= "</SELECT>";



	$rights_form = preg_replace("/ACCESS_MATRIX_NAME/", $matrix_name, $rights_form);
	$rights_form = preg_replace("/RIGHTS_TABLE/", $rights_table, $rights_form);
	$rights_form = preg_replace("/RIGHT_DD/", $right_dd, $rights_form);
	$rights_form = preg_replace("/ROLE_DD/", $role_dd, $rights_form);
	$rights_form = preg_replace("/VALUE_DD/", $value_dd, $rights_form);
	
	return $rights_form;

}

function save_access_matrix($access_matrix_id, $name, $description, $default_value) {
	if ( is_numeric($access_matrix_id) && $access_matrix_id > 0 ) {
		// this is update
		$sql = sprintf("UPDATE access_matrix SET name = %s, description = %s, default_value = %d WHERE id = %d", 
				$name, $description, $default_value, $access_matrix_id);
	} else {
		// insert
		$sql = sprintf("INSERT INTO access_matrix (name, description, default_value)
							VALUES ( %s, %s, %d )",
					$name, $description, $default_value);
	}
	$st = @mysql_query($sql);
	if ( $st ) {
		$last_id = mysql_insert_id();
		$access_matrix_id = $last_id == 0 ? $access_matrix_id : $last_id ;
		$access_matrix_opt = show_access_matrix_opt($access_matrix_id);
		$rights_form = access_rights($access_matrix_id, $name);
		$return = array('access_matrix_id' => $access_matrix_id,
						'access_matrix_opt' => $access_matrix_opt,
						'rights_form'		=> $rights_form,
						'sql'	=> $sql,
						'status' => 0,
						'msg' => 'Matrix ' . $name . ' saved successfully');
	} else {
		$return = array('access_matrix_id' => $access_matrix_id, 
						'status' => 1,
						'sql'	=> $sql,
						'msg' => 'Error saving Matrix: ' . mysql_error());
	}
	return $return;
}

/////////////////////
// VIEW MATRIX Func
/////////////////////

function show_view_matrix_opt($view_matrix_id) {
	$view_matrix_opt = "<SELECT id=view_matrix_id onChange='view_matrixSelected();'>
				<option value=-1>Select User Privacy from the list</option>";
	$view_matrix_opt .= "<option value=0>Add New User Privacy</option>";
	$sql = "SELECT * FROM view_matrix ORDER BY name";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$selected = $view_matrix_id == $row['id'] ? " selected=selected " : "";
		$view_matrix_opt .= "<option value=" . $row['id'] . $selected . ">" . $row['name'] . "</option>";
	}
	$view_matrix_opt .= "</SELECT>";
	return $view_matrix_opt;
}

function show_view_matrix($view_matrix_id) {
	$sql="";
	if ( is_numeric($view_matrix_id) && $view_matrix_id > 0 ) {
		$sql = "SELECT * FROM view_matrix WHERE id = " . $view_matrix_id;
		$st = @mysql_query($sql);
		$row = @mysql_fetch_assoc($st);
	}

	$form = <<< EOF_FORM
	<script language="javascript"  type="text/javascript">
	function addPermission() {
		var q_string = "&view_matrix_id=" + $("#view_matrix_id").val();
		q_string += "&view_matrix_name=" + $("#name").text();
		var rights_id = $("#rights_id").val();
		q_string += "&rights_id=" + rights_id;
		var subject_role_id =  $("#subject_role_id").val();
		q_string += "&subject_role_id=" + subject_role_id;
		var target_role_id = $("#target_role_id").val();
		q_string += "&target_role_id=" + target_role_id;
		q_string += "&permission=" + $("#permission").val();
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:       "action=add_view_permission" + q_string,
			success:    function(obj) {
							if ( obj.status == 0 ) {
								$("#rights_area p").html(obj.form);
								$("#rights_id").val(rights_id);
								$("#subject_role_id").val(subject_role_id);
								$("#target_role_id").val(target_role_id);
							}
			}
		});
	}
	function deletePermission(permission_id) {
		var q_string = "&view_matrix_id=" + $("#view_matrix_id").val();
		q_string += "&view_matrix_name=" + $("#name").text();
		q_string += "&permission_id=" + permission_id;
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:       "action=del_view_permission" + q_string,
			success:    function(obj) {
							if ( obj.status == 0 ) {
								$("#rights_area p").html(obj.form);
							}
			} 
		});
	} 
	function save_view_matrix() {
		var q_string = "action=save_view_matrix";
		if ( $("#name").val() == "" ) {
			alert("Please enter a name for the Matrix");
			return;
		}
		if ( $("#description").val() == "" ) {
			alert("Please enter description for the Matrix");
			return;
		}
		if ( isNaN($("#default_value:checked").val())  ) {
			alert("Please select a default value for the Matrix");
			return;
		}
		q_string += "&view_matrix_id=" + $("#view_matrix_id").val();
		q_string += "&name=" + $("#name").val();
		q_string += "&description=" + $("#description").val();
		q_string += "&default_value=" + $("#default_value:checked").val();
		//alert(q_string);
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:		q_string,
			success:    function(obj) {
				$("#status_area").html(obj.msg);
				if ( obj.status == 0 ) {
					$("#view_matrix_dd").html(obj.view_matrix_opt);
					$("#edit_area").html(obj.form);
					$("#rights_area p").html(obj.rights_form);
				}
			}
		})
		return;
	}

	function delete_view_matrix() {
		var go = confirm("Do you want to delete the Matrix?");
		if ( go == false ) {
			return;
		}
		var q_string = "action=delete_view_matrix";
		q_string += "&view_matrix_id=" + $("#view_matrix_id").val();
		q_string += "&name=" + $("#name").val();
		$.ajax({
			type:       "GET",
			cache:      "false",
			dataType:   "json",
			url:        "matrix_functions.php",
			data:       q_string,
			success:    function(obj) {
				$("#status_area").html(obj.msg);
				if ( obj.status == 0 ) {
					$("#view_matrix_dd").html(obj.view_matrix_opt);
					$("#edit_area").html('');
					$("#rights_area p").html('');
				}
			}
		});
		return;
	}

	</script>

	<form><fieldset><legend>CREATE/EDIT User Privacy</legend>
	<table width="100%"  border="0">
		<tr>
			<td>Name</td>
			<td><INPUT type=text id=name size=20 value="NAME" onKeyUp="$('#status_area').html('Change not saved');" /></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><INPUT type=text id=description size=40 value="DESCRIPTION" onKeyUp="$('#status_area').html('Change not saved');" /></td>
		</tr>
		<tr>
			<td>Default Value</td>
			<td><table width="100%"  border="0">
					<tr>
						<td>None <INPUT type=radio id=default_value value="0" name=default_value 
								NO_CHECKED onChange="$('#status_area').html('Change not saved');" /></td>
						<td>Limited <INPUT type=radio id=default_value value="1" name=default_value 
								YES_CHECKED onChange="$('#status_area').html('Change not saved');" /></td>
						<td>Full <INPUT type=radio id=default_value value="2" name=default_value 
								UD_CHECKED onChange="$('#status_area').html('Change not saved');" /></td>
						<td>Full+ <INPUT type=radio id=default_value value="3" name=default_value 
								ST_CHECKED onChange="$('#status_area').html('Change not saved');" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td colspan=2>&nbsp;</td></tr>
		<tr>
			<td colspan=2 align=center>
				<table border="0" width="80%">
				  <tr><td align=center>
						<INPUT type=button id=save value="Save" class=btn onClick="save_view_matrix();" /></td>
					  <td align=center>
						<INPUT type=button id=delete value="Delete" class=btn onClick="delete_view_matrix();" /></td>
					  <td align=center>
						<INPUT type=reset id=reset class=btn value="Reset" onClick="$('#status_area').html('');" /></td>
					  <td align=center>
					    <INPUT type=button id=clone value="Clone User Privacy" style='display:none;' class=btn onClick="clone_view_matrix();" /></td>
				  </tr></table>
			</td>
		</tr>
	</table>
	</form></fieldset>
EOF_FORM;
	$name = isset($row['name']) ? $row['name'] : "";
	$description = isset($row['description']) ? $row['description'] : "";
	$no_checked = isset($row['default_value']) && $row['default_value'] == 0 ? "checked" : "";
	$yes_checked = isset($row['default_value']) && $row['default_value'] == 1 ? "checked" : "";
	$ud_checked = isset($row['default_value']) && $row['default_value'] == 2 ? "checked" : "";
	$st_checked = isset($row['default_value']) && $row['default_value'] == 3 ? "checked" : "";
	// replace the content
	$form = preg_replace("/NAME/", $name, $form);
	$form = preg_replace("/DESCRIPTION/", $description, $form);
	$form = preg_replace("/NO_CHECKED/", $no_checked, $form);
	$form = preg_replace("/YES_CHECKED/", $yes_checked, $form);
	$form = preg_replace("/UD_CHECKED/", $ud_checked, $form);
	$form = preg_replace("/ST_CHECKED/", $st_checked, $form);
	
	// get rights
	$rights_form = "";
	if ( $view_matrix_id > 0 ) {
		$rights_form = view_rights($view_matrix_id, $name);
	}
	// return array
	$rt = array('status'		=> '',
				'form'			=> $form,
				'rights_form'	=> $rights_form,
				'sql'			=> $sql
				);
	return $rt;
}

function view_rights($matrix_id, $matrix_name ) {
	global $vp_name;
	$rights_form = <<< EOF_FORM
	<p>&nbsp;</p>
	<script language="javascript"  type="text/javascript">
	  function filterRole() {
		  $("#rights_table tr").each(function() {
			  var name_like = $("#search_roles").val();
			  var row_text = $(this).find("td:first").html();
			  var row_text2 = $(this).find("td").eq(1).html();
			  if ( row_text.search(new RegExp(name_like, 'i')) < 0 && row_text2.search(new RegExp(name_like, 'i')) < 0 ) {
				  $(this).hide();
			  } else {
				  $(this).show();
			  }
		  });
	  }
	</script>
	<form><fieldset><legend>Manage Permission for VIEW_MATRIX_NAME</legend>
	  <table border=0 width=100%>
	    <tr>
		  <td align=center width="25%">Viewer</td>
		  <td align=center width="25%">Viewee</td>
		  <td align=center width="25%">Value</td>
		  <td align=center width="25%">&nbsp;</td>
		</tr>
	    <tr>
		  <td align=center width="25%">SUBJECT_ROLE_DD</td>
		  <td align=center width="25%">TARGET_ROLE_DD</td>
		  <td align=center width="25%">VALUE_DD</td>
		  <td align=center width="25%"><INPUT type=button id=add_right class=btn value="Add/Update Permission" onClick="addPermission();" /></td>
		</tr>
		<tr><td colspan=5>&nbsp;</td>
		<tr><td colspan=5>Filter Roles &nbsp;<INPUT id=search_roles size=20 type=text onKeyUp="filterRole();" /></td>
		<tr>
		  <td colspan=5 align=center>
		    <table border=0 width="100%">
			  <tr>
				<td width="25%" align=center>VIEWER</td>
				<td width="25%" align=center>VIEWEE</td>
				<td width="25%" align=center>VALUE</td>
				<td width="25%" align=center>ACTION</td>
			  </tr>
			</table>
			<div style="border:1px black solid; width:100%; height:300px; overflow:auto;">
				RIGHTS_TABLE
			</div>
		  </td>
		</tr>
	  </table>
	</form></fieldset>
EOF_FORM;
	
	$sql = sprintf("SELECT so.name as subject_role,  tto.name as target_role, ap.id as permission_id , ap.permission
				FROM view_permission ap, role so, role tto
				WHERE ap.subject_role_id = so.id 
					AND ap.target_role_id = tto.id
					AND ap.view_matrix_id = %d
				ORDER BY subject_role, target_role ", $matrix_id);
	$rights_table = "<table id=rights_table border='2' width='100%' class=box>";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$row['permission'] = isset($row['permission']) ? $row['permission'] : 2;
		$rights_table .= "<td width='25%' align=center>" . $row['subject_role'] . "</td>";
		$rights_table .= "<td width='25%' align=center>" . $row['target_role'] . "</td>";
		$rights_table .= "<td width='25%' align=center>" . $vp_name[$row['permission']] . "</td>";
		$rights_table .= "<td width='25%' align=center class=btn onClick='deletePermission(" . $row['permission_id'] .");' >Delete</td></tr>";
	}
	$rights_table .= "</table>";
	// get rights drop down
	$right_dd = "<SELECT id=rights_id>";
	$sql = "SELECT * FROM rights ORDER BY name";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$right_dd .= "<option value=" . $row['id'] . ">" . $row['label'] . "</option>";
	}
	$right_dd .= "</SELECT>";
	
	// get subject and target roles drop down
	$subject_role_dd = "<SELECT id=subject_role_id>";
	$target_role_dd = "<SELECT id=target_role_id>";
	$sql = "SELECT * FROM role ORDER BY name";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$target_role_dd .= "<option value=" . $row['id'] . ">" . $row['name'] . "</option>";
		$subject_role_dd .= "<option value=" . $row['id'] . ">" . $row['name'] . "</option>";
	}
	$subject_role_dd .= "</SELECT>";
	$target_role_dd .= "</SELECT>";
	
	// get value drop down
	$value_dd = "<SELECT id=permission>";
	foreach ( $vp_name as $key => $value ) {
		$value_dd .= "<option value=" . $key . ">" . $value . "</option>";
	}
	$value_dd .= "</SELECT>";



	$rights_form = preg_replace("/VIEW_MATRIX_NAME/", $matrix_name, $rights_form);
	$rights_form = preg_replace("/RIGHTS_TABLE/", $rights_table, $rights_form);
	$rights_form = preg_replace("/RIGHT_DD/", $right_dd, $rights_form);
	$rights_form = preg_replace("/SUBJECT_ROLE_DD/", $subject_role_dd, $rights_form);
	$rights_form = preg_replace("/TARGET_ROLE_DD/", $target_role_dd, $rights_form);
	$rights_form = preg_replace("/VALUE_DD/", $value_dd, $rights_form);
	
	return $rights_form;

}

function save_view_matrix($view_matrix_id, $name, $description, $default_value) {
	if ( is_numeric($view_matrix_id) && $view_matrix_id > 0 ) {
		// this is update
		$sql = sprintf("UPDATE view_matrix SET name = %s, description = %s, default_value = %d WHERE id = %d", 
				$name, $description, $default_value, $view_matrix_id);
	} else {
		// insert
		$sql = sprintf("INSERT INTO view_matrix (name, description, default_value)
							VALUES ( %s, %s, %d )",
					$name, $description, $default_value);
	}
	$st = @mysql_query($sql);
	if ( $st ) {
		$last_id = mysql_insert_id();
		$view_matrix_id = $last_id == 0 ? $view_matrix_id : $last_id ;
		$view_matrix_opt = show_view_matrix_opt($view_matrix_id);
		$rights_form = view_rights($view_matrix_id, $name);
		$return = array('view_matrix_id' => $view_matrix_id,
						'view_matrix_opt' => $view_matrix_opt,
						'rights_form'		=> $rights_form,
						'sql'	=> $sql,
						'status' => 0,
						'msg' => 'Matrix ' . $name . ' saved successfully');
	} else {
		$return = array('view_matrix_id' => $view_matrix_id, 
						'status' => 1,
						'sql'	=> $sql,
						'msg' => 'Error saving Matrix: ' . mysql_error());
	}
	return $return;
}


?>
