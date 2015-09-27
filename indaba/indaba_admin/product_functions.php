<?php

require_once("include/config.inc");
session_start();

// define content_type

$radio = array( SURVEY => '', JOURNAL => '');

// get project_dd
// show project options
if ( isset($_GET['action']) && $_GET['action'] == 'show_project_opt' ) {
	$sql = "SELECT * FROM project ORDER BY code_name";
	$st = @mysql_query($sql);
	$project_dd = "<SELECT id=project_id onChange='projectSelected();' ><option value=-1>Select Project from the list</option>";
	while ( $project = @mysql_fetch_assoc($st) ) {
		$selected = isset($product['project_id']) && $project['id'] == $product['project_id'] ? " selected=selected " : "";
		$project_dd .= "<option value=" . $project['id'] . $selected . ">" . $project['code_name'] . "</option>";
	}
	$project_dd .= "</SELECT>";
	echo $project_dd;
	exit;
}

// generate product options
if ( isset($_GET['action']) && $_GET['action'] == 'show_product_opt' ) {
	$return = show_product_opt($_GET['project_id'], -1);
	echo $return;
	exit;
}

// show product in edit area 
if ( isset($_GET['action']) && $_GET['action'] == 'show_product' ) {
	$return = show_product($_GET['product_id']);
	echo json_encode($return);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_config_opt' ) {
	$product_config_id = isset($_GET['product_config_id']) ? $_GET['product_config_id'] : -1;
	$rt = show_config_opt($_GET['content_type'], $product_config_id);
	echo $rt;
	exit;
}
// save/insert product
if ( isset($_GET['action']) && $_GET['action'] == 'save_product' ) {
	$product_id = $_GET['product_id'];
	$name = SQLString($_GET['name'], "text");
	$description = SQLString($_GET['description'], 'text');
	$content_type = $_GET['content_type'];
	$workflow_id = $_GET['workflow_id'];
	$project_id = $_GET['project_id'];
	$access_matrix_id = $_GET['access_matrix_id'];
	$product_config_id = $_GET['product_config_id'];
	$rt = save_product($product_id, $name, $description, $content_type, $workflow_id, $project_id, $access_matrix_id, $product_config_id);
	if ( $rt['status'] == 0 ) {
		$return = array('status' => 0, 'msg' => "Product " . $name . " saved successfully", 'sql' => $rt['sql'],
						'product_opt' => $rt['product_opt'], 'product_id' => $rt['product_id']);
	} else {
		$return = array('status' => 1, 'msg' => $rt['msg'] );
	}
	echo json_encode($return);
	exit;
}

// delete product
if ( isset($_GET['action']) && $_GET['action'] == 'delete_product' ) {
	$sql = sprintf("SELECT * FROM goal WHERE entrance_product_id = %d OR inflight_product_id = %d OR exit_product_id = %d",
			$_GET['product_id'], $_GET['product_id'], $_GET['product_id']);
	$st = @mysql_query($sql);
	if ( @mysql_num_rows($st) > 0 ) {
		$return = array('status' => 1, 'msg' => 'Cannot delete product because it is used by one or more goals');
		echo json_encode($return);
		exit;
	}
	$sql = sprintf("DELETE FROM product, task USING product LEFT JOIN task
								ON product.id = task.product_id
						WHERE product.id = %d", $_GET['product_id']);
	$st = @mysql_query($sql);
	if ( $st ) {
		$product_opt = show_product_opt($_GET['project_id'], -1);
		$return = array('status' => 0, 'msg' => 'Product deleted successfully', 'product_opt' => $product_opt);
	} else {
		$return = array('status' => 1, 'msg' => 'Error deleting product:' . mysql_error() );
	}
	echo json_encode($return);
	exit;
}

// show_task_details
if ( isset($_GET['action']) && $_GET['action'] == 'show_task_details' ) {
	if ( isset($_GET['task_id']) && $_GET['task_id'] > 0 ) {
		$sql = "SELECT * FROM task WHERE id = " . $_GET['task_id'];
		$st = @mysql_query($sql);
		$task = mysql_fetch_assoc($st);
	}
	$task['task_name'] = empty($task['task_name'])? '' : $task['task_name'];
	$task['description'] = empty($task['description'])? '' : $task['description'];
	$task['instructions'] = empty($task['instructions'])? '' : $task['instructions'];
	$task['assignment_method'] = empty($task['assignment_method'])? '' : $task['assignment_method'];
	// get goals opt
	$goal_opt = "<div style='float:left;' ><SELECT id=goal_id>";
	$hidden = "";   
	$sql = "SELECT g.* FROM goal g, workflow_sequence ws WHERE g.workflow_sequence_id = ws.id AND ws.workflow_id = " . $_GET['workflow_id'];
	$st = @mysql_query($sql);
	$selected = "";
	while ( $goal = mysql_fetch_assoc($st) ) {
		$selected = isset($task['goal_id']) && $task['goal_id'] == $goal['id'] ? " selected=selected " : '';
		$goal_opt .= "<option value=" . $goal['id'] . " " . $selected . ">" . substr($goal['name'] . " - " . $goal['description'], 0, 40)  . "</option>";
		$hidden .= "<INPUT type=hidden id=goal_id_" . $goal['id'] . " value='" . 
					htmlentities(" ENTRANCE RULE: " . $goal['entrance_rule_desc'] . "<br />" . 
					" INFLIGHT RULE: " . $goal['inflight_rule_desc'] . "<br />" . 
					" EXIT RULE: " . $goal['exit_rule_desc'] . "<br />", ENT_QUOTES) . 
					"' /> " ;
	}
	$goal_opt .= "</SELECT></div><div id=showgoal class=btn style='width:15px; height:15px; float:left; margin-top:3px;'>?</div>";
	$goal_opt .= $hidden;
	// get tools opt
	$tool_opt = "<div style='float:left;' ><SELECT id=tool_id>";
	$sql = "SELECT * FROM tool ORDER BY label";
	$st = @mysql_query($sql);
	$hidden = "";
	while ( $tool = mysql_fetch_assoc($st) ) {
		$selected = isset($task['tool_id']) && $task['tool_id'] == $tool['id'] ? " selected=selected " : '';
		$tool_opt .= "<option value=" . $tool['id'] . " " . $selected . ">" . substr($tool['label'] . " - " . $tool['description'], 0, 40)  . "</option>";
		$hidden .= "<INPUT type=hidden id=tool_id_" . $tool['id'] . " value='" . htmlentities($tool['name'] . ": " . $tool['description'], ENT_QUOTES) . "' />";
	}
	$tool_opt .= "</SELECT></div> <div id=showtool class=btn style='width:15px; height:15px; float:left; margin-top:3px;'>?</div>";
	$rt = array('task_name'		=> $task['task_name'],
				'description'	=> $task['description'],
				'assignment_method' => $task['assignment_method'],
				'instructions' => $task['instructions'],
				'goal_opt'		=> $goal_opt,
				'tool_opt'		=> $tool_opt . $hidden );
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'saveTask' ) {
	if ( $_GET['task_id'] > 0 ) {
		// update
		$sql = sprintf("UPDATE task, tool
								SET task.task_name = %s, task.description = %s, 
									task.goal_id =%d, task.product_id = %d,  task.tool_id =tool.id, 
									task.assignment_method = %d, task.instructions = %s,
									task.type = tool.task_type
								WHERE
									task.id = %d
									AND tool.id = %d",
					SQLString($_GET['task_name'], "text"),
					SQLString($_GET['task_description'], "text"),
					$_GET['goal_id'], $_GET['product_id'], $_GET['assignment_method'], 
					SQLString(urldecode($_GET['task_instructions']), "text"), $_GET['task_id'], $_GET['tool_id']);
	} else {
		// insert
		$sql = sprintf("INSERT INTO task (task_name, description, goal_id, product_id, tool_id, type, assignment_method, instructions)
								SELECT %s, %s, %d, %d, id, task_type, %d, %s
									FROM tool
									WHERE id = %d",
					SQLString($_GET['task_name'], "text"),
					SQLString($_GET['task_description'], "text"),
					$_GET['goal_id'], $_GET['product_id'], $_GET['assignment_method'],
					SQLString(urldecode($_GET['task_instructions']), "text"),
					$_GET['tool_id']);
	}
	$st = mysql_query($sql);
    if ( mysql_affected_rows() == 1 ) {
        $status_msg = "Task " . $_GET['task_name'] . " has been saved successfully.";
        $query_status = 0;
    } else {
        $status_msg = "Error saving task " . $_GET['task_name'] . ". " . mysql_error();
        $query_status = 1;
    }
	$last_id = mysql_insert_id();
    $rt = array('status_msg' => $status_msg, 'query_status' => $query_status, 'last_id' => $last_id,
				'task_name'  => $_GET['task_name'], 'task_id' => $_GET['task_id'], 'sql' => $sql );
    echo json_encode($rt);
    exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'deleteTask' ) {
	$sql = "DELETE FROM task WHERE id = " . $_GET['task_id'];
	$st = mysql_query($sql);
	if ( $st ) {
		$rt['query_status'] = 0;
		$rt['status_msg'] = 'Task deleted successfully';
	} else {
		$rt['query_status'] = 1;
		$rt['status_msg'] = 'Error deleting task. ' . mysql_error();
	}
	$rt['sql'] = $sql;
	$rt['task_id'] = $_GET['task_id'];
	echo json_encode($rt);
	exit;
}


///////////////////////////
// FUNCTIONS
//////////////////////////

function show_product_opt ($project_id, $product_id) {
	//$sql = "SELECT id, name FROM product";
	$sql = sprintf("SELECT * FROM product WHERE project_id = %d ORDER BY name", $project_id);
	$st = @mysql_query($sql);
	$selected = "";
	$product_opt = "<SELECT id=product_opt name=product_opt onChange='productSelected();'>";
	$product_opt .= "<option value=-1>Select Product from list</option>";
	$product_opt .= "<option value=0>Add New Product</option>";
	while ( $row = @mysql_fetch_assoc($st) ) {
		$selected = $product_id == $row['id'] ? " selected=selected " : "";
		$product_opt .= "<option value=" . $row['id'] . $selected . ">" . $row['name'] . "</option>";
	}
	$product_opt .= "</SELECT>";
	return $product_opt;
}

function show_product($product_id) {

	global $radio;
	// build html 
	$form = <<< EOF_FORM
<script type="text/javascript" language="javascript" >
$("#manage_product").change(function() {
	$("#manage_task :input").each(function() {
		$(this).attr('disabled', true);
	});
});
$("#manage_task").change(function() {
	$("#manage_product :input").each(function() {
		$(this).attr('disabled', true);
	});
});

</script>
<div id=manage_product><form><fieldset><legend align="center">Create/Edit Product</legend>
<table width="90%"  border="0" align=center>
  <tr>
    <td width="25%">Name</td>
    <td width="75%"><INPUT id=name name=name type=text size=50 value="PRODUCT_NAME" /></td>
  </tr>
  <tr>
    <td>Description</td>
    <td ><INPUT id=description name=description type=text size=50 value="PRODUCT_DESCRIPTION" /></td>
  </tr>
  <tr>
    <td>Content Type </td>
    <td><table border="0" aligh=center width="100%">
	      <tr>
		    <td width="50%" aligh=center>Survey <INPUT type=radio id=content_type name=content_type value=0 RADIO_0 
					onClick="show_config_opt(0);" /></td>
		    <td width="50%" aligh=center>Journal <INPUT type=radio id=content_type name=content_type value=1 RADIO_1 
					onClick="show_config_opt(1);" /></td>
	      </tr>
		</table>
    </td>
  </tr>
  <tr>
    <td colspan=3><table border="0" aligh=center width="100%">
					<tr>
					  <td width='25%' align=center>Workflow </td>
					  <td width='25%' align=center>Access Matrix </td>
					  <td width='25%' align=center>Content Config</td>
					</tr>
					<tr>
					  <td width='25%' align=center>WORKFLOW_DD</td>
					  <td width='25%' align=center>ACCESS_MATRIX_DD</td>
					  <td width='25%' align=center><div id=product_config_dd>PRODUCT_CONFIG_DD</div></td>
					</tr>
				  </table>
	</td>
  </tr>
  <tr><td colspan=2>&nbsp;</td></tr>
  <tr>
    <td colspan=2 align=center><table width="80%" align=center border=0>
	      <tr>
		    <td width="33%" align=center><INPUT type=button id=save class=btn value="Save Product" onClick="saveProduct();" /></td>
		    <td width="33%" align=center><INPUT type=button id=delete class=btn value="Delete Product" onClick="deleteProduct();" /></td>
		    <td width="33%" align=center><INPUT type=reset id=reset class=btn value="Reset" onClick="return cancel_all();" /></td>
		  </tr>
		</table>
	</td>
  </tr>
  <tr><td colspan=2>&nbsp;</td></tr>
</table>
<INPUT type=hidden name=product_id id=product_id value=PRODUCT_ID />
</fieldset>
</form></div>
<table width=100% border=0>
  <tr><td align=center width="80%">TASK_TABLE</td></tr>
</table>
  
EOF_FORM;

	$task_table = "";
	// replace the content
	if ( $product_id > 0 ) {
		// show edit for exiting product
		$sql = "SELECT * FROM product WHERE id = " . $product_id;
		$st = @mysql_query($sql);
		$product = @mysql_fetch_assoc($st);
		// show tasks
		$sql = sprintf("SELECT * FROM task WHERE product_id = %d ORDER BY task_name ", $product_id);
		$st = @mysql_query($sql);
		$task_table .= "<div id=manage_task><form><fieldset><legend align='center'>Manage Tasks for product [" . $product['name'] . "] </legend>";
		$task_table .= "<table border=0 width='90%'><tr><td width='180' align='left'>";
		$task_table .= "<SELECT id=task_list class='sel_list' multiple onChange='show_task_details();'>";
		$task_table .= "<option value=0>Add New Task</option>";
		while ( $tasks = mysql_fetch_assoc($st) ) {
			$task_table .= "<option value=" . $tasks['id'] . ">" . $tasks['task_name'] . "</option>";
		}
		$task_table .= "</SELECT></td>";
		// left side of the table, put details and buttons
		$task_table .= <<< EOF
<td valign='top' align='left'>
  <table border=0 width=100%>
		<tr><td width='35%' align='right'>Name &nbsp;</td><td width='65%' align=left><INPUT id=task_name size=20 /></td></tr>
		<tr><td align=right>Description &nbsp;</td><td align=left><INPUT id=task_description size=30 /><td align=center></td></tr>
		<tr><td align=right>Instructions &nbsp;</td><td align=left><textarea id=task_instructions cols=55 rows=4 ></textarea><td align=center></td></tr>
		<tr><td align=right>Goal &nbsp;</td><td align=left><div id=task_goal ></div><td align=center></td></tr>
		<tr><td align=right>Tool &nbsp;</td><td align=left><div id=task_tool></div><td align=center></td></tr>
		<tr><td align=right>Assignment Method &nbsp;</td><td align=left>
			<INPUT type=radio name=assignment_method id=assignment_method value=1 onClick="show_buttons();" />Manual
			<INPUT type=radio name=assignment_method id=assignment_method value=2 onClick="show_buttons();" />Queue
			<INPUT type=radio name=assignment_method id=assignment_method value=3 onClick="show_buttons();" />Dynamic
		</td></tr>
		<tr><td colspan=3>&nbsp;</td></tr>
		<tr><td colspan=3><table border=0 width=100%>
		    <tr><td width=33% align=center><INPUT type=button class=btn value="Save Task" onClick="saveTask();" /></td>
			    <td width=33% align=center><INPUT type=button class=btn value="Delete Task" onClick="deleteTask();" /></td>
				<td width=33% align=center><INPUT type=reset class=btn value=Reset onClick="return cancel_all();" /></td>
			</tr>
			<tr><td width=100%  colspan=3 align=center>&nbsp;</td></tr>
			<tr><td width=100% colspan=3 align=center>
			  <div id=assign_role style="display:none;">
			    <INPUT type=button class=btn value="Assign Task Roles" onClick="assignRole()" /></div>
			  <div id=assign_user style="display:none;">
			    <INPUT type=button class=btn value="Assign Task Users" onClick="assignUser();" /></div>
			</td></tr>
		  </table>
		</table>
EOF;

		// finish table
		$task_table .= "</td></tr></table></fieldset></form></div>";
	}
	// get workflow_dd
	$sql = "SELECT * FROM workflow ORDER BY name";
	$st = @mysql_query($sql);
	$workflow_dd = "<SELECT id=workflow_id><option value=-1>Select Workflow from the list</option>";
	while ( $workflow = @mysql_fetch_assoc($st) ) {
		$selected = isset($product['workflow_id']) && $workflow['id'] == $product['workflow_id'] ? " selected=selected " : "";
		$workflow_dd .= "<option value=" . $workflow['id'] . $selected . ">" . $workflow['name'] . "</option>";
	}
	$workflow_dd .= "</SELECT>";
	// get access_matrix_dd
	$sql = "SELECT * FROM access_matrix ORDER BY name";
	$st = @mysql_query($sql);
	$access_matrix_dd = "<SELECT id=access_matrix_id><option value=-1>Select Access Matrix from the list</option>";
	while ( $access_matrix = @mysql_fetch_assoc($st) ) {
		$selected = isset($product['access_matrix_id']) && $access_matrix['id'] == $product['access_matrix_id'] ? " selected=selected " : "";
		$access_matrix_dd .= "<option value=" . $access_matrix['id'] . $selected . ">" . $access_matrix['name'] . "</option>";
	}
	$access_matrix_dd .= "</SELECT>";


	$product_name = isset($product['name']) ? $product['name'] : '';
	$product_id = isset($product['id']) ? $product['id'] : '';
	$product_config_id = isset($product['product_config_id']) ? $product['product_config_id'] : '';
	$product_desc = isset($product['description']) ? $product['description'] : '';
	$content_config_dd = '';
	if ( isset($product['content_type']) ) {
		$radio[$product['content_type']] = " checked=checked ";
		$content_config_dd = show_config_opt($product['content_type'], $product['product_config_id']);
	}

	$form = preg_replace("/PRODUCT_CONFIG_DD/", $content_config_dd, $form);
	$form = preg_replace("/PRODUCT_NAME/", $product_name, $form);
	$form = preg_replace("/PRODUCT_ID/", $product_id, $form);
	$form = preg_replace("/PRODUCT_CONFIG_ID/", $product_config_id, $form);
	$form = preg_replace("/WORKFLOW_DD/", $workflow_dd, $form);
	$form = preg_replace("/ACCESS_MATRIX_DD/", $access_matrix_dd, $form);
	$form = preg_replace("/PRODUCT_DESCRIPTION/", $product_desc, $form);
	$form = preg_replace("/RADIO_0/", $radio[SURVEY], $form);
	$form = preg_replace("/RADIO_1/", $radio[JOURNAL], $form);
	$form = preg_replace("/TASK_TABLE/", $task_table, $form);

	$status = "";
	$return = array( 'form' => $form, 'status' => $status);
	return $return;

}


function show_config_opt($content_type, $product_config_id) {
	$content_config_opt = "<SELECT id=product_config_id>";
	if ( $content_type == SURVEY ) {
		$sql = "SELECT * FROM survey_config ORDER BY name";
		$content_config_opt .= "<option value=-1>Select Survey Config</option>";
	} else {
		$sql = "SELECT * FROM journal_config ORDER BY name";
		$content_config_opt .= "<option value=-1>Select Journal Config</option>";
	}
	$st = @mysql_query($sql);
	while ( $content_config = @mysql_fetch_array($st) ) {
		$selected = $content_config[0] == $product_config_id ? " selected=selected " : "";
		$content_config_opt .= "<option value=" . $content_config[0] . $selected . ">" . $content_config[1] . "</option>";
	}
	$content_config_opt .= "</SELECT>";
	return $content_config_opt;
}

function save_product($product_id, $name, $description, $content_type, $workflow_id, $project_id, $access_matrix_id, $product_config_id) {
	if ( is_numeric($product_id) && $product_id > 0 ) {
		// this is an update
		$sql = sprintf("UPDATE product SET name = %s, description = %s, content_type = %d, workflow_id = %d, project_id = %d, access_matrix_id = %d, product_config_id = %d WHERE id = %d",
					$name, $description, $content_type, $workflow_id, $project_id, $access_matrix_id, $product_config_id, $product_id);
	} else { 
		// this is an insert
		$sql = sprintf("INSERT INTO product (name, description, content_type, workflow_id, project_id, access_matrix_id, product_config_id) 
							VALUES (%s, %s, %d, %d, %d, %s, %d)",
					$name, $description, $content_type, $workflow_id, $project_id, $access_matrix_id, $product_config_id);
	}
	$st = @mysql_query($sql);
	$product_opt = "";
	if ( $st ) {
		$last_id = mysql_insert_id();
		$product_id = $last_id == 0 ? $product_id : $last_id ;
		$product_opt = show_product_opt($project_id, $product_id);
		$return = array('product_id' => $product_id, 'product_opt' => $product_opt, 'sql' => $sql, 'status' => 0, 'msg' => " Inserted ID " . $last_id);
	} else {
		$return = array('msg' => "Error saving product: " . mysql_error(), 'status' => 1 );
	}
	return $return;
}

?>
