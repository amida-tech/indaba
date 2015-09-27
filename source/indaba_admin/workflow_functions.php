<?php

require_once("include/config.inc");
session_start();


// generate role options
if ( isset($_GET['action']) && $_GET['action'] == 'show_rule_opt' ) {
	$return = show_rule_opt(-1);
	echo $return;
	exit;
}


// show workflow options
if ( isset($_GET['action']) && $_GET['action'] == 'show_workflow_opt' ) {
	$return = show_workflow_opt(-1);
	echo $return;
	exit;
}
// show workflow in edit area
if ( isset($_GET['action']) && $_GET['action'] == 'show_workflow' ) {
	$return = show_workflow($_GET['workflow_id']);
	echo json_encode($return);
	exit;
}
// save/insert workflow
if ( isset($_GET['action']) && $_GET['action'] == 'save_workflow' ) {
	$workflow_id = isset($_GET['workflow_id']) ? $_GET['workflow_id'] : '';
	$name = SQLString($_GET['name'], "text");
	$description = SQLString($_GET['description'], 'text');
	$rt = save_workflow($workflow_id, $name, $description);
	echo json_encode($rt);
	exit;
}
// delete workflow
if ( isset($_GET['action']) && $_GET['action'] == 'delete_workflow' ) {
	$sql = sprintf("SELECT * FROM product WHERE workflow_id = %d", $_GET['workflow_id']);
	$st = @mysql_query($sql);
	if ( @mysql_num_rows($st) > 0 ) {
		$return = array('status' => 1, 'msg' => 'Cannot delete workflow because it is used by a product');
		echo json_encode($return);
		exit;
	}
	// now we need to delete everything in workflow_object, workflow_sequence, goal, sequence_object, goal_object, horse
	$sql = sprintf("
				DELETE FROM
						workflow, workflow_object, workflow_sequence, goal, sequence_object, goal_object, horse
					USING workflow LEFT JOIN ( workflow_object, workflow_sequence, goal, sequence_object, goal_object, horse)
					ON ( 
						workflow.id				= workflow_object.workflow_id
						AND workflow.id 			= workflow_sequence.workflow_id
						AND workflow_sequence.id 	= goal.workflow_sequence_id
						AND workflow_sequence.id 	= sequence_object.workflow_sequence_id
						AND goal.id 					= goal_object.goal_id
						AND workflow_object.id 			= horse.workflow_object_id)
					WHERE
						workflow.id 				= %d
					",
			$_GET['workflow_id']);
	$st = @mysql_query($sql);
	if ( $st ) {
		$workflow_opt = show_workflow_opt(-1);
		$sequence_opt = show_sequence_opt(-1, -1);
		$return = array('status' => 0, 
						'msg' => "Workflow and associated items have been deleted successfully",
						'sequence_opt' => $sequence_opt,
						'workflow_opt' => $workflow_opt, 
						'sql' => $sql );
	} else {
		$return = array('status' => 1, 'msg' => "Error deleting workflow: " . mysql_error(), 'sql' => $sql  );
	}
	echo json_encode($return);
	exit;
}

// show sequence options
if ( isset($_GET['action']) && $_GET['action'] == 'show_sequence_opt' ) {
	$return = show_sequence_opt($workflow_id);
	echo $return;
	exit;
}
// show sequence in edit area
if ( isset($_GET['action']) && $_GET['action'] == 'show_sequence' ) {
	$return = show_sequence($_GET['workflow_id'], $_GET['sequence_id']);
	echo json_encode($return);
	exit;
}
// save/insert sequence
if ( isset($_GET['action']) && $_GET['action'] == 'save_sequence' ) {
	$sequence_id = isset($_GET['sequence_id']) ? $_GET['sequence_id'] : '';
	$name = SQLString($_GET['name'], "text");
	$description = SQLString($_GET['description'], 'text');
	$rt = save_sequence($_GET['workflow_id'], $sequence_id, $name, $description);
	if ( $rt['status'] == 0 && isset($_GET['add_pre_goal']) ) {
		$error = 0;
		$rt['pre_sql'] = '';
		foreach ( $_GET['add_pre_goal'] as $key => $value ) {
			// need to make sure the pregoal will not cause circulation
			$nested = 0;
			$finish_check = 0;
			// first check is based on goal_id 
			// find sequence id for $value first
			$s_sql = "SELECT workflow_sequence_id FROM goal WHERE id = " . $value;
			$s_st = mysql_query($s_sql);
			$row = mysql_fetch_assoc($s_st);
			$ws_ids = $row['workflow_sequence_id'];
			while ( $nested == 0 && $finish_check == 0 ) {
				$AND = sprintf(" AND p1.workflow_sequence_id in (%s)", $ws_ids);
				$check_sql = sprintf("SELECT p1.workflow_sequence_id as ps_id, p1.pre_goal_id,
											g1.workflow_sequence_id as ws_id, g1.id as g_id,
											g2.workflow_sequence_id as ws_id2, g2.name as name
										FROM pregoal p1, goal g1, goal g2
										WHERE g1.workflow_sequence_id = p1.workflow_sequence_id
											AND p1.pre_goal_id = g2.id %s",
								$AND);
				$check_st = mysql_query($check_sql);
				$ws_ids = '';
				while ( $pregoal = mysql_fetch_assoc($check_st) ) {
					// when we get same workflow_sequence_id as the saved sequence_id, it is a circulation
					if ( $sequence_id == $pregoal['ws_id2'] ) {
						$nested = 1;
						break;
					}
					$ws_ids .= $pregoal['g_id'] . ",";
				}
				if ( $ws_ids == '' ) {
					// no more dependent workflow_sequence
					$finish_check = 1;
				} else {
					// remove last comma from ws_ids
					$ws_ids = preg_replace('/,$/', '', $ws_ids);
				}
			}
			if ( $nested == 1 ) {
				$rt['msg'] .= " But the pre-requisite goal [" . $pregoal['name'] . "] will cause circulation.";
				$error = 1;
				break;
			}

			$sql = sprintf("REPLACE INTO pregoal ( workflow_sequence_id, pre_goal_id ) VALUES ( %d, %d )",
					$rt['sequence_id'], $value);
			$st = @mysql_query($sql);
			$rt['pre_sql'] .= $sql . "; ";
			if ( ! $st ) {
				// failed to update pre-goal
				$rt['msg'] .= " But failed to update pre-requisite goals. Error: " . mysql_error();
				$error = 1;
				break;
				$rt['sql'] = $sql;
			}
		}
		if ( $error == 0 ) {
			$rt['msg'] .= " All pre-requisite goals are updated.";
		}
	}
	if ( $rt['status'] == 0 && isset($_GET['del_pre_goal']) ) {
		foreach ( $_GET['del_pre_goal'] as $key => $value ) {
			// delete those from pregoal
			$sql = sprintf("DELETE FROM pregoal WHERE pre_goal_id = %d AND workflow_sequence_id = %d", $value, $rt['sequence_id']);
			$st = @mysql_query($sql);
		}
	}
	$rt['total_duration'] = update_total_duration($_GET['workflow_id']);
	echo json_encode($rt);
	exit;
}

// delete sequence
if ( isset($_GET['action']) && $_GET['action'] == 'delete_sequence' ) {
	// check if this sequence's goal is pregoal of other sequence
	$sql = sprintf("SELECT DISTINCT pg.workflow_sequence_id, w.name as workflow_name, ws.name as sequence_name
						FROM pregoal pg, goal g, workflow w, workflow_sequence ws
						WHERE
							g.id = pg.pre_goal_id
							AND g.workflow_sequence_id = ws.id
							AND ws.workflow_id = w.id
							AND g.workflow_sequence_id = %d",
					$_GET['sequence_id']);
	$st = mysql_query($sql);
	$status_msg = "This sequence's goal is pre-requsite goal of workflow [";
	while ( $pregoal = mysql_fetch_assoc($st) ) {
		$status_msg .= $pregoal['workflow_name'] . "] => sequence [" . $pregoal['sequence_name'] . "]<br>";
		$nodelete = 1;
	}
	if ( isset($nodelete) ) {
		$status_msg .= "Please remove the dependencies before delete this sequence.";
		$rt = array('statua' => 1, 'msg' => $status_msg, 'sql' => $sql);
		echo json_encode($rt);
		exit;
	}
	$sql = sprintf("DELETE FROM
						workflow_sequence, goal, sequence_object, goal_object
					USING workflow_sequence LEFT JOIN ( goal, sequence_object, goal_object )
					ON ( 
						workflow_sequence.id = goal.workflow_sequence_id
						AND workflow_sequence.id = sequence_object.workflow_sequence_id
						AND goal.id = goal_object.goal_id ) 
					WHERE 
						workflow_sequence.id = %d", 
				$_GET['sequence_id']);
	$st = @mysql_query($sql);
	if ( $st ) { 
		$sequence_opt = show_sequence_opt($_GET['workflow_id'], -1);
		$return = array('status' => 0,
						'msg' => "Sequence and associated items have been deleted successfully",
						'sequence_opt' => $sequence_opt,
						'sql' => $sql);
	} else {
		$return = array('status' => 1, 'msg' => "Error deleting Sequence: " . mysql_error(), 'sql' => $sql  );
	}
	echo json_encode($return);
	exit;
}

// show goal in edit area
if ( isset($_GET['action']) && $_GET['action'] == 'show_goal' ) {
	$return = show_goal($_GET['sequence_id'], $_GET['goal_id']);
	echo json_encode($return);
}
// save/insert sequence
if ( isset($_GET['action']) && $_GET['action'] == 'save_goal' ) {
	$goal_id = isset($_GET['goal_id']) ? $_GET['goal_id'] : '';
	$name = SQLString($_GET['name'], "text");
	$description = SQLString($_GET['description'], 'text');
	$rt = save_goal($_GET['sequence_id'], $goal_id, $name, $description, 
					$_GET['duration'], urldecode($_GET['entrance_rule_desc']),
					urldecode($_GET['inflight_rule_desc']), urldecode($_GET['exit_rule_desc']),
					$_GET['entrance_rule_file_name'], $_GET['inflight_rule_file_name'], $_GET['exit_rule_file_name']);
	if ( $rt['status'] == 0 && isset($_GET['goal_list']) ) {
		// update weight 
		$error = 0;
		foreach ( $_GET['goal_list'] as $weight => $id ) {
			// if $id == 0, it is the current/highlighted item. use goal_id 
			$id = $id == 0 ? $rt['goal_id'] : $id;
			$sql = sprintf("UPDATE goal SET weight = %d WHERE id = %d", $weight, $id);
			$st = @mysql_query($sql);
			if ( ! $st ) {
				// failed to update weight
				$rt['msg'] .= " But failed to update weight. Error: " . mysql_error();
				$error = 1;
				break;
			}
			$rt['sql'] .= " " . $sql;
		}
		if ( $error == 0 ) {
			$rt['msg'] .= " Weight for goals are updated.";
		}
	}
	$rt['total_duration'] = update_total_duration($_GET['workflow_id']);
	echo json_encode($rt);
	exit;
}

// delete goal
if ( isset($_GET['action']) && $_GET['action'] == 'delete_goal' ) {
	// check if the goal is a pre-requisite of other sequence
	$sql = sprintf("SELECT ws.name FROM workflow_sequence ws, pregoal p
						WHERE
							p.pre_goal_id = %d AND ws.id = p.workflow_sequence_id",
					$_GET['goal_id']);
	$st = @mysql_query($sql);
	$i = 0;
	$seqs = '';
	while ( $row = @mysql_fetch_assoc($st) ) {
		$i ++;
		$seqs .= $row['name'] . " ";
	}
	if ( $i > 0 ) {
		$status = 1;
		$msg = "This goal is a pre-requsite of sequence " . $seqs . ". Please unassign before delete.";
		$rt = array('status' => $status, 'msg' => $msg);
		echo json_encode($rt);
		exit;
	}
	$sql = sprintf("DELETE FROM
						goal, goal_object, sequence_object
					USING goal LEFT JOIN ( goal_object, sequence_object )
					ON ( 
						goal_object.sequence_object_id = sequence_object.id
						AND goal.id = goal_object.goal_id ) 
					WHERE 
						goal.id = %d", 
				$_GET['goal_id']);
	$st = @mysql_query($sql);
	if ( $st ) { 
		$goal_opt = show_goal_opt($_GET['sequence_id'], -1);
		$return = array('status' => 0,
						'msg' => "Goal and associated items have been deleted successfully",
						'goal_opt' => $goal_opt,
						'sql' => $sql);
		$return['total_duration'] = update_total_duration($_GET['workflow_id']);
	} else {
		$return = array('status' => 1, 'msg' => "Error deleting Goal: " . mysql_error(), 'sql' => $sql  );
	}
	echo json_encode($return);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'clone' ) {
	$workflow_id = $_GET['workflow_id'];
	// dup workflow
	$w_i_sql = sprintf("INSERT INTO workflow (name, description, created_time, created_by_user_id) 
							SELECT concat(name, '-clone'), description, now(), %d 
								FROM workflow 
								WHERE
									id = %d",
					$_SESSION['user_id'], $workflow_id);
	$st = mysql_query($w_i_sql);
	if ( ! $st ) {
		echo json_encode(array('query_status' => 1, 'query_msg' => 'Error cloning workflow: ' . mysql_error(), 'sql' => $w_i_sql));
		exit;
	}
	$id = mysql_insert_id();

	// get workflow sequence
	$ws_sql = sprintf("SELECT * FROM workflow_sequence WHERE workflow_id = %d", $workflow_id);
	$ws_st = mysql_query($ws_sql);
	while ( $ws = mysql_fetch_assoc($ws_st) ) {
		$ws_i_sql = sprintf("INSERT INTO workflow_sequence (workflow_id, name, description)
								SELECT %d, name, description FROM workflow_sequence
									WHERE id = %d",
							$id, $ws['id']);
		$st = mysql_query($ws_i_sql);
		if ( ! $st ) {
			echo json_encode(array('query_status' => 1, 'query_msg' => 'Error cloning workflow sequence: ' . mysql_error() ) );
			exit;
		}
		$ws_id = mysql_insert_id();
		$ws_new_id[$ws['id']] = $ws_id;
		// clone goal
		$g_sql = sprintf("SELECT * FROM goal WHERE workflow_sequence_id = %d", $ws['id']);
		$g_st = mysql_query($g_sql);
		while ( $goal = mysql_fetch_assoc($g_st) ) {
			$g_i_sql = sprintf("INSERT INTO goal (workflow_sequence_id, weight, name, description, access_matrix_id, duration,
												entrance_rule_file_name, entrance_rule_desc, inflight_rule_file_name, 
												inflight_rule_desc, exit_rule_file_name, exit_rule_desc)
							SELECT %d, weight, name, description, access_matrix_id, duration,
										entrance_rule_file_name, entrance_rule_desc, inflight_rule_file_name,
										inflight_rule_desc, exit_rule_file_name, exit_rule_desc
									FROM goal 
									WHERE id = %d",
						$ws_id, $goal['id']);
			$st = mysql_query($g_i_sql);
			if ( ! $st ) {
				echo json_encode(array('query_status' => 1, 'query_msg' => 'Error cloning goals: ' . mysql_error() ) ) ;
				exit;
			}
			$goal_id = mysql_insert_id();
			$goal_new_id[$goal['id']] = $goal_id;
			
		}
	}
	// clone pre-goal
	foreach( $ws_new_id as $old_ws_id => $new_ws_id ) {
		foreach ( $goal_new_id as $old_goal_id => $new_goal_id ) {
			$pg_sql = sprintf("INSERT INTO pregoal (workflow_sequence_id, pre_goal_id) SELECT %d, %d FROM pregoal WHERE workflow_sequence_id = %d AND pre_goal_id = %d",
						$new_ws_id, $new_goal_id, $old_ws_id, $old_goal_id);
			$pg_sql_a .= $pg_sql . "XXXXX";
			$pg_st = mysql_query($pg_sql);
			if ( ! $pg_st ) {
				echo json_encode(array('query_status' => 1, 'query_msg' => 'Error cloning pre-goals: ' . mysql_error() ) ) ;
				exit;
			}
		}
	}

	$rt = array();
	$rt['workflow_id'] = $id;
	$rt['workflow_opt'] = show_workflow_opt($id);
	$rt['sequence_opt'] = show_sequence_opt($id, -1);
	$rt['status'] = 0;
	$rt['pg_sql_a'] = $pg_sql_a;
	$rt['msg'] = "Workflow cloned successfully";
	$rt['pg_sql'] = $pg_sql;
	$rt['total_duration'] = update_total_duration($id);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_rules_dd') {
	list ($rules_dd, $desc) = get_rules_dd($_GET['type'], $_GET['goal_id']);
	$rt = array('rules_dd' => $rules_dd, 'desc' => $desc);
	echo json_encode($rt);
	exit;
}



///////////////////////////
// FUNCTIONS
//////////////////////////

// workflow functions
function show_workflow_opt( $workflow_id ) {
	$sql = "SELECT id, name FROM workflow ORDER BY name";
	$st = @mysql_query($sql);
	$selected = "";
	$workflow_opt = "<SELECT id=workflow_opt name=workflow_opt onChange='workflowSelected();' >";
	$workflow_opt .= "<option value=-1>Select Workflow from list</option>";
	$workflow_opt .= "<option value=0>Add New Workflow</option>";
	while ( $row = @mysql_fetch_assoc($st) ) {
		$selected = $workflow_id == $row['id'] ? " selected=selected " : "";
		$workflow_opt .= "<option value=" . $row['id'] . $selected . ">" . $row['name'] . "</option>";
	}
	$workflow_opt .= "</SELECT>";
	return $workflow_opt;
}

function show_workflow($workflow_id) {
	// build html
	$form = <<< EOF_FORM
<form ><fieldset><legend align="center">Create/Edit Workflow</legend>
<table width="90%"  border="0" align=center>
  <tr>
    <td width="25%">Name</td>
    <td width="75%"><INPUT id=name name=name type=text size=50 value="WORKFLOW_NAME" onKeyPress="$('#status_area').html('Change not saved yet');" /></td>
  </tr>
  <tr>
    <td>Description</td>
    <td width="75%"><INPUT id=description name=description type=text size=50 value="WORKFLOW_DESCRIPTION" onKeyPress="$('#status_area').html('Change not saved yet');" /></td>
  </tr>
  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>
  <tr>
    <td colspan=2 ><table width="100%" align=center border=0>
	      <tr>
		    <td width="25%" align=center><INPUT type=button id=save class=btn value="Save" onClick="saveWorkflow();" /></td>
		    <td width="25%" align=center><INPUT type=button id=delete class=btn value="Delete" onClick="deleteWorkflow();" /></td>
		    <td width="25%" align=center><INPUT type=reset id=reset class=btn value="Reset" onClick="return workflowSelected();" /></td>
		    <td width="25%" align=center><INPUT type=button id=clone class=btn value="Clone" onClick="cloneWorkflow();" /></td>
		  </tr>
		</table>
	</td>
  </tr>
</table>
<INPUT type=hidden name=workflow_id id=workflow_id value=WORKFLOW_ID />
</fieldset>
</form>
EOF_FORM;
	// replace the content
	if ( $workflow_id > 0 ) {
		$sql = "SELECT * FROM workflow WHERE id = " . $workflow_id;
		$st = @mysql_query($sql);
		$row = @mysql_fetch_assoc($st);
	}
	$workflow_name = isset($row['name']) ? $row['name'] : '';
	$workflow_id = isset($row['id']) ? $row['id'] : '';
	$workflow_description = isset($row['description']) ? $row['description'] : '';
	$form = preg_replace("/WORKFLOW_NAME/", $workflow_name, $form);
	$form = preg_replace("/WORKFLOW_DESCRIPTION/", $workflow_description, $form);
	$form = preg_replace("/WORKFLOW_ID/", $workflow_id, $form);
	$status = '';
	$sequence_opt = show_sequence_opt($workflow_id, -1);
	$return = array( 'form' => $form, 'status' => $status, 'sequence_opt' => $sequence_opt );
	return $return;
}

function save_workflow( $workflow_id, $name, $description ) {
	if ( is_numeric($workflow_id) && $workflow_id > 0 ) {
		// this is update
		$sql = sprintf("UPDATE workflow SET name = %s, description = %s WHERE id = %d", 
				$name, $description, $workflow_id);
	} else {
		// insert
		$sql = sprintf("INSERT INTO workflow (name, description, created_time, created_by_user_id)
							VALUES ( %s, %s, now(), %d )",
					$name, $description, $_SESSION['user_id']);
	}
	$st = @mysql_query($sql);
	if ( $st ) {
		$last_id = mysql_insert_id();
		$workflow_id = $last_id == 0 ? $workflow_id : $last_id ;
		$workflow_opt = show_workflow_opt($workflow_id);
		$sequence_opt = show_sequence_opt($workflow_id, -1);
		$return = array('workflow_id' => $workflow_id,
						'workflow_opt' => $workflow_opt,
						'sequence_opt' => $sequence_opt,
						'sql'	=> $sql,
						'status' => 0,
						'msg' => 'Workflow ' . $name . ' saved successfully');
	} else {
		$return = array('workflow_id' => $workflow_id, 
						'status' => 1,
						'sql' => $sql,
						'msg' => 'Error saving workflow: ' . mysql_error());
	}
	return $return;
}

function show_sequence_opt($workflow_id, $sequence_id) {
	$sql = sprintf("SELECT id, name from workflow_sequence WHERE workflow_id = %d ORDER BY name", $workflow_id);
	$st = @mysql_query($sql);
	$selected = "";
	$sequence_opt = "<SELECT id=sequence_opt name=sequence_opt onChange='sequenceSelected();' >";
	$sequence_opt .= "<option value=-1>Select Sequence from list</option>";
	$sequence_opt .= "<option value=0>Add New Sequence</option>";
	while ( $row = @mysql_fetch_assoc($st) ) {
		$selected = $sequence_id == $row['id'] ? " selected=selected " : "";
		$sequence_opt .= "<option value=" . $row['id'] . $selected . ">" . $row['name'] . "</option>";
	}
	$sequence_opt .= "</SELECT>";
	return $sequence_opt;
}

function show_sequence($workflow_id, $sequence_id) {
	// build html
	$form = <<< EOF_FORM
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#source_list, #target_list").sortable({
		connectWith: '.connectedSortable'
	}).disableSelection();
});
</script>
<form ><fieldset><legend align="center">Create/Edit Sequence</legend>
<table width="90%"  border="0" align=center>
  <tr>
    <td width="25%">Name</td>
    <td width="75%"><INPUT id=name name=name type=text size=50 value="SEQUENCE_NAME" onKeyPress="$('#status_area').html('Change not saved yet');" /></td>
  </tr>
  <tr>
    <td>Description</td>
    <td width="75%"><INPUT id=description name=description type=text size=50 value="SEQUENCE_DESCRIPTION" onKeyPress="$('#status_area').html('Change not saved yet');" /></td>
  </tr>
  <tr>
    <td colspan=2 align=center>
	  <table border=0 width="90%">
	    <tr>
		  <td align=center width="30%">Available Goals</td>
		  <td width="30%"></td>
		  <td align=center width="30%">Pre-Requisite Goals</td>
		</tr>
		<tr>
		  <td colspan=3>
		  <div style="width:800px; height:300px; " >
			  <div  id=source_list class="connectedSortable" style="float:left; width:300px; height:300px;" >AVA_GOALS_OPT</div>
			  <div style="float:left; width:190px; height:300px; margin-top:40px; " >You may drag and drop the goals between the two boxes.</p> Click "Save" button below to save the changes</div>
			  <div style="float:left; width:300px; height:300px;" id=target_list class="connectedSortable" >PRE_GOALS_OPT</div>
		  </div>
		  </td>
		</tr>
	  </table>
	</td>
  </tr>
  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>
  <tr>
    <td colspan=2><table width="100%" align=center border=0>
	      <tr>
		    <td width="33%" align=center><INPUT type=button id=save class=btn value="Save" onClick="saveSequence();" /></td>
		    <td width="33%" align=center><INPUT type=button id=delete class=btn value="Delete" onClick="deleteSequence();" /></td>
		    <td width="33%" align=center><INPUT type=reset id=reset class=btn value="Reset" onClick="return sequenceSelected();" /></td>
		  </tr>
		</table>
	</td>
  </tr>
</table>
<INPUT type=hidden name=sequence_id id=sequence_id value=SEQUENCE_ID />
<INPUT type=hidden name=workflow_id id=workflow_id value=WORKFLOW_ID />
</fieldset>
</form>
EOF_FORM;
	$AND = "";
	// replace the content
	$pre_goal_opt = '';

	$goal_opt = '';
	if ( $sequence_id > 0 ) {
		// get sequence name and description
		$sql = "SELECT * FROM workflow_sequence WHERE id = " . $sequence_id;
		$st = @mysql_query($sql);
		$row = @mysql_fetch_assoc($st);

		$AND = " AND g.workflow_sequence_id <> " . $sequence_id;
		// only populate pre_goal option when the sequence is already there
		$sql = "SELECT g.name, p.pre_goal_id, p.workflow_sequence_id FROM goal g, pregoal p WHERE g.id = p.pre_goal_id and p.workflow_sequence_id = " . $sequence_id;
		$st = @mysql_query($sql);
		while ( $row_opt = @mysql_fetch_assoc($st) ) {
			$pre_goal_opt .= "<li id=pre_goal_" . $row_opt['pre_goal_id'] . " class='ui-state-default'>" . $row_opt['name'] . "</li>";
			$AND .= " AND g.id <> " . $row_opt['pre_goal_id'] ;
		}
		$goal_opt = show_goal_opt($sequence_id, -1);
	}
	$sequence_name = isset($row['name']) ? $row['name'] : '';
	$sequence_id = isset($row['id']) ? $row['id'] : '';
	$sequence_description = isset($row['description']) ? $row['description'] : '';
	$workflow_id = isset($row['workflow_id']) ? $row['workflow_id'] : $workflow_id;
	// populate ava_goals 
	$ava_goal_opt = '';
	$sql = "SELECT g.* FROM workflow_sequence ws, goal g WHERE ws.id = g.workflow_sequence_id AND ws.workflow_id = " . $workflow_id . $AND;
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		$ava_goal_opt .= "<li id=ava_goal_" . $row['id'] . " class='ui-state-default' >" . $row['name'] . "</li>";
	}

	$form = preg_replace("/SEQUENCE_NAME/", $sequence_name, $form);
	$form = preg_replace("/SEQUENCE_DESCRIPTION/", $sequence_description, $form);
	$form = preg_replace("/SEQUENCE_ID/", $sequence_id, $form);
	$form = preg_replace("/AVA_GOALS_OPT/", $ava_goal_opt, $form);
	$form = preg_replace("/PRE_GOALS_OPT/", $pre_goal_opt, $form);
	$form = preg_replace("/WORKFLOW_ID/", $workflow_id, $form);
	$status = '';
	$return = array( 'form' => $form, 'status' => $status, 'goal_opt' => $goal_opt, 'sequence_id' => $sequence_id );
	return $return;
}

function save_sequence( $workflow_id, $sequence_id, $name, $description) {
	if ( is_numeric($sequence_id) && $sequence_id > 0 ) {
		// update
		$sql = sprintf("UPDATE workflow_sequence SET name = %s, description = %s WHERE id = %d",
				$name, $description, $sequence_id);
	} else {
		// insert
		$sql = sprintf("INSERT INTO workflow_sequence (workflow_id, name, description)
							VALUES ( %d, %s, %s )",
					$workflow_id, $name, $description);
	}
	$st = @mysql_query($sql);
	if ( $st ) {
		$last_id = mysql_insert_id();
		$sequence_id = $last_id == 0 ? $sequence_id : $last_id;
		$sequence_opt = show_sequence_opt($workflow_id, $sequence_id);
		$goal_opt = show_goal_opt($sequence_id, -1);
		$return = array('sequence_id' => $sequence_id, 
						'workflow_id' => $workflow_id,
						'sequence_opt'  => $sequence_opt,
						'goal_opt'		=> $goal_opt,
						'sql'			=> $sql,
						'status'		=> 0,
						'msg'			=> 'Sequence ' . $name . ' saved successfully.');
	} else {
		$return = array('workflow_id' => $workflow_id,
						'sequence_id' => $sequence_id,
						'status' => 1,
						'msg' => 'Error saving sequence: ' . mysql_error());
	}
	return $return;
}

function show_goal_opt($sequence_id, $goal_id) {
	$sql = sprintf("SELECT id, name FROM goal WHERE workflow_sequence_id = %d ORDER BY name ", $sequence_id);
	$st = @mysql_query($sql);
	$selected = "";
	$goal_opt = "<SELECT id=goal_opt name=goal_opt onChange='goalSelected();' >";
	$goal_opt .= "<option value=-1>Select Goal from list</option>";
	$goal_opt .= "<option value=0>Add New Goal</option>";
	while ( $row = @mysql_fetch_assoc($st) ) {
		$selected = $goal_id == $row['id'] ? " selected=selected " : "";
		$goal_opt .= "<option value=" . $row['id'] . $selected . ">" . $row['name'] . "</option>";
	}
	$goal_opt .= "</SELECT>";
	return $goal_opt;
}

function show_goal($sequence_id, $goal_id) {
	// build html form
	$form = <<< EOF_FORM

<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script src="include/tinybox.js" language="javascript"  type="text/javascript"></script>
<script type="text/javascript">

$(function() {
	$("#source_list").sortable({
		connectWith: '.connectedSortable'
	}).disableSelection();
}); 

$(document).ready(function() {
	$("#current_item_0").css("background", "yellow");
});

function updateList() {
	$('#current_item_0').text($('#name').val());
}

$("#rule_id").change( function() {
  show_rule_desc();
});

function show_rule_desc() {
	var rule_id = $("#rule_id").val();
	if ( rule_id == -1 ) {
		// just return
		$("#canned_rule_desc").val("Apply this will empty the rule for this goal");
		return;
	}
	var desc = $("#rule_id_" + rule_id).val();
	$("#canned_rule_desc").val(desc);
	return;

}



function addRule() {
	var current_rule = $("#current_rule").val() + "_rule_desc";
	var rule_id = 'rule_id_' + $("#rule_id").val();
	var rule_desc = $("#" + rule_id ).val();
	var type = $("#current_type").val();
	var rule_file = $("#rule_file_" + rule_id).val();
	
	if ( $("#rule_id").val() == -1 ) {
		rule_desc = "No rule is defined";
		rule_file = "";
	}
	
	$("#" + current_rule).text(rule_desc);
	if ( type == 1 ) {
		$("#entrance_rule_file_name").val(rule_file);
	}
	if ( type == 2 ) {
		$("#inflight_rule_file_name").val(rule_file);
	}	
	if ( type == 3 ) {
		$("#exit_rule_file_name").val(rule_file);
	}	
}

</script>

<form ><fieldset><legend align="center">Create/Edit Goal</legend>
<table width="100%"  border="0" align=center>
  <tr>
    <td width="10%">Name</td>
    <td width="50%"><INPUT id=name name=name type=text size=40 value="GOAL_NAME" onKeyUp="$('#status_area').html('Change not saved yet');
																								updateList(); " /></td>
    <td width="40%" rowspan="8" valign=top><table width="90%"  border="0">
      <tr>
        <td >Drag/drop the goal in the box below to adjust its order </td>
      </tr>
      <tr>
        <td align=center><ul id=source_list  >GOAL_LIST</ul></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>Description</td>
    <td ><INPUT id=description name=description type=text size=40 value="GOAL_DESCRIPTION" onKeyPress="$('#status_area').html('Change not saved yet');" /></td>
    </tr>
  <tr>
    <td>Duration</td>
    <td ><INPUT id=duration name=duration type=text size=10 value="GOAL_DURATION" onKeyPress="$('#status_area').html('Change not saved yet');" /> Days</td>
    </tr>
  <tr>
    <td colspan=2 align=center>
      <table border=0 width="90%">
        <tr>
          <td align=center width="100%">
			<form name='photo_form' method=POST enctype="multipart/form-data">
			<div id=canned_rule style="float:left; width:500px; height:23px; margin:10px 5px 10px 5px;">
				<div id=rules_dd style="float:left; width:300px;" >
					RULES_DD
				</div>
				<div id=add_rule class=btn style="float:left; width:100px; height:13px; margin:3px 30px 3px 50px; " onClick="addRule();" >Set Rule</div>
			</div>
			<div style="float:left; width:450px; height:35px; margin:5px 5px 5px 15px; ">
				<textarea id=canned_rule_desc cols=55 rows=2 style='resize:none' readonly=true >CANNED_RULE_DESC</textarea>
			</div>
			<div style="width:100%; margin-top:5px; ">
				<INPUT TYPE=hidden id=current_rule value=entrance />
				<div id=show_entrance_rule class=btn style="float:left; font-size:10px; width:100px; height:13px; background-color:yellow;" onClick="show_entrance_rule();">Entrance rule</div>
				<div id=show_inflight_rule class=btn style="float:left; font-size:10px; width:100px; height:13px;" onClick="show_inflight_rule();">Inflight rule</div>
				<div id=show_exit_rule class=btn style="float:left; font-size:10px; width:100px; height:13px;" onClick="show_exit_rule();">Exit rule</div>
				<div><img id="loading" src="images/loading.gif" style="display:none;"></div>
			</div>
			<div id=entrance_rule style="float:left; width:450px;" >
				<fieldset><legend>ENTRANCE RULE</legend>
					<div style="float:left; width:450px;" >
						<TEXTAREA id=entrance_rule_desc cols=55 rows=5 readonly=true style='resize:none' >ENTRANCE_RULE_DESC</textarea>
					</div>
					<div style="float:left; width:450px;" >
						Entrance Rule File Name: <INPUT TYPE=text size=25 id=entrance_rule_file_name readonly=true value="ENTRANCE_RULE_FILE" />
					</div>
				</fieldset>
			</div>
			  <div id=inflight_rule style="float:left; display:none; width:450px;">
				<fieldset><legend>INFLIGHT RULE</legend>
					<div style="float:left; width:450px;" >
						<TEXTAREA id=inflight_rule_desc cols=55 rows=5 readonly=true style='resize:none' >INFLIGHT_RULE_DESC</textarea>
					</div>
					<div style="float:left; width:450px;" >
						Inflight Rule File Name: <INPUT TYPE=text id=inflight_rule_file_name size=25 readonly=true value="INFLIGHT_RULE_FILE" />
					</div>
				</fieldset>
			</div>
			<div id=exit_rule style="float:left; display:none; width:450px;">
				<fieldset><legend>EXIT RULE</legend>
					<div style="float:left; width:450px;" >
						<TEXTAREA id=exit_rule_desc cols=55 rows=5 readonly=true style='resize:none'>EXIT_RULE_DESC</textarea>
					</div>
					<div style="float:left; width:450px;" >
						Exit Rule File Name: <INPUT TYPE=text id=exit_rule_file_name size=25 readonly=true value="EXIT_RULE_FILE" />
					</div>
				</fieldset>
			</div>
		    </form>
		  </td>
        </tr>
      </table>
    </td>
	</tr>
	<tr>
    <td colspan=2>&nbsp;</td>
	</tr>
	<tr>
    <td colspan=2><table width="100%" align=center border=0>
          <tr>
            <td width="33%" align=center><INPUT type=button id=save class=btn value="Save" onClick="saveGoal();" /></td>
            <td width="33%" align=center><INPUT type=button id=delete class=btn value="Delete" onClick="deleteGoal();" /></td>
            <td width="33%" align=center><INPUT type=button id=reset class=btn value="Reset" onClick="return goalSelected();"/></td>
          </tr>
        </table>
    </td>
    </tr>
</table>
<INPUT type=hidden name=sequence_id id=sequence_id value=SEQUENCE_ID />
<INPUT type=hidden name=goal_id id=goal_id value=GOAL_ID />
</fieldset></form>
EOF_FORM;

	// replace the content
	$goal_list_opt = '';
	if ( $goal_id > 0 ) {
		// get goal name and description
		$sql = "SELECT * FROM goal WHERE id = " . $goal_id;
		$st = @mysql_query($sql);
		$row = @mysql_fetch_assoc($st);
	} else {
		$goal_list_opt .= "<li id=current_item_0></li>";
	}
	$goal_name = isset($row['name']) ? $row['name'] : '';
	$goal_description = isset($row['description']) ? $row['description'] : '';
	$goal_duration = isset($row['duration']) ? $row['duration'] : '';
	// $goal_access_matrix_id = isset($row['access_matrix_id']) ? $row['access_matrix_id'] : -1;
	$entrance_rule_file_name = isset($row['entrance_rule_file_name']) ? $row['entrance_rule_file_name'] : '';
	$inflight_rule_file_name = isset($row['inflight_rule_file_name']) ? $row['inflight_rule_file_name'] : '';
	$exit_rule_file_name = isset($row['exit_rule_file_name']) ? $row['exit_rule_file_name'] : '';
	$entrance_rule_desc = isset($row['entrance_rule_desc']) ? $row['entrance_rule_desc'] : '';
	$inflight_rule_desc = isset($row['inflight_rule_desc']) ? $row['inflight_rule_desc'] : '';
	$exit_rule_desc = isset($row['exit_rule_desc']) ? $row['exit_rule_desc'] : '';

	// populate Access Matrix drop down
	/* according to ticket #102, this is not needed
	$goal_a_opt = '<SELECT id=access_matrix_id ><option value=-1>Select Access Matrix</option>';
	$sql = "SELECT id as access_matrix_id, name from access_matrix";
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st)) {
		$selected = $row['access_matrix_id'] == $goal_access_matrix_id ? " selected=selected " : "";
		$goal_a_opt .= "<option value=" . $row['access_matrix_id'] . $selected .  " >" . $row['name'] . "</option>";
	}
	$goal_a_opt .= "</SELECT>";

	*/

	// populate goal list
	$sql = sprintf("SELECT * FROM goal WHERE workflow_sequence_id = %d ORDER BY weight", $sequence_id);
	$st = @mysql_query($sql);
	while ( $row = @mysql_fetch_assoc($st) ) {
		if ( $row['id'] == $goal_id ) {
			$goal_list_opt .= "<li id=current_item_0>" . $row['name'] . "</li>";
		} else {
			$goal_list_opt .= "<li id=goal_list_" . $row['id'] . ">" . $row['name'] . "</li>";
		}
	}

	// get canned rules
	/*
	$rules_dd = '<SELECT id=rule_id> ';
	$hidden = '';
	$sql = "SELECT * FROM rule ORDER BY name";
	$st = mysql_query($sql);
	while ( $rule = mysql_fetch_assoc($st) ) {
		$rules_dd .= "<option value=" . $rule['id'] . " >" . $rule['name'] . "</option>";
		$hidden .= "<INPUT type=hidden id=rule_id_" . $rule['id'] . " value='" . htmlentities($rule['description'], ENT_QUOTES) . "' />";
	}
  $hidden .= "<INPUT type=hidden id=rule_id_-1 value='No rule is defined' />";
	$rules_dd .= "</SELECT>" . $hidden;
	*/
	list ($rules_dd, $desc) = get_rules_dd(1, $goal_id);

	$_SESSION['goal_id'] = $goal_id;
	$form = preg_replace("/GOAL_ID/", $goal_id, $form);
	$form = preg_replace("/SEQUENCE_ID/", $sequence_id, $form);
	$form = preg_replace("/GOAL_NAME/", $goal_name, $form);
	$form = preg_replace("/GOAL_DESCRIPTION/", $goal_description, $form);
	$form = preg_replace("/GOAL_DURATION/", $goal_duration, $form);
	//$form = preg_replace("/ACCESS_MATRIX/", $goal_a_opt, $form);
	$form = preg_replace("/ENTRANCE_RULE_FILE/", $entrance_rule_file_name, $form);
	$form = preg_replace("/INFLIGHT_RULE_FILE/", $inflight_rule_file_name, $form);
	$form = preg_replace("/EXIT_RULE_FILE/", $exit_rule_file_name, $form);
	$form = preg_replace("/ENTRANCE_RULE_DESC/", $entrance_rule_desc, $form);
	$form = preg_replace("/INFLIGHT_RULE_DESC/", $inflight_rule_desc, $form);
	$form = preg_replace("/EXIT_RULE_DESC/", $exit_rule_desc, $form);
	$form = preg_replace("/GOAL_LIST/", $goal_list_opt, $form);
	$form = preg_replace("/RULES_DD/", $rules_dd, $form);
	$form = preg_replace("/CANNED_RULE_DESC/", $desc, $form);

	$status = '';
	$return = array( 'form' => $form, 'status' => $status, 'sequence_id' => $sequence_id );
	return $return;
}

// function to generate rules drop down list by type, if a rule is already defined, make it selected
function get_rules_dd($type, $goal_id) {
	// get the rule file names for goal_id first
	if ( isset($goal_id) ) {
		$sql = "SELECT * FROM goal WHERE id = $goal_id";
		$st = mysql_query($sql);
		$goal_rule = mysql_fetch_assoc($st);
	}
	
	// type 1 => Entrance rule; 2 => Inflight rule; 3 => Exit rule
	switch ( $type ) {
		case 1:
			$label = "Canned Entrance Rules";
			$rule_file_name = isset($goal_rule['entrance_rule_file_name']) ? $goal_rule['entrance_rule_file_name'] : null;
			break;
		case 2:
			$label = "Canned Inflight Rules";
			$rule_file_name = isset($goal_rule['inflight_rule_file_name']) ? $goal_rule['inflight_rule_file_name'] : null;
			break;
		case 3:
			$label = "Canned Exit Rules";
			$rule_file_name = isset($goal_rule['exit_rule_file_name']) ? $goal_rule['exit_rule_file_name'] : null;
			break;
	}

	$rules_dd = $label . '<br/><INPUT type=hidden id=current_type value=' . $type . '><SELECT id=rule_id onChange="return show_rule_desc();"> ';
	if ( ! $rule_file_name ) {
		$rules_dd .= '<option value=-1 selected>No rule is defined</option>';
	} else {
		$rules_dd .= '<option value=-1 >No rule is defined</option>';
	}
	$hidden = '';
	$sql = "SELECT * FROM rule WHERE rule_type = $type ORDER BY name";
	$st = mysql_query($sql);
	while ( $rule = mysql_fetch_assoc($st) ) {
		$selected = $rule_file_name == $rule['file_name'] ? " selected " : "";
		$rule_desc = htmlentities($rule['description'], ENT_QUOTES);
		if ( $rule_file_name == $rule['file_name'] ) {
			$selected = " selected ";
		} else {
			$selected = " ";
		}
		$rules_dd .= "<option value=" . $rule['id'] . " " . $selected . " file_name='" . $rule['file_name'] . "' >" . $rule['name'] . "</option>";
		$hidden .= "<INPUT type=hidden id=rule_id_" . $rule['id'] . " value='" . htmlentities($rule['description'], ENT_QUOTES) . "' />";
		$hidden .= "<INPUT type=hidden id=rule_file_rule_id_" . $rule['id'] . " value='" . htmlentities($rule['file_name'], ENT_QUOTES) . "' />";
		//$hidden .= "<INPUT type=hidden id=goal_rule_rule_id_" . $rule['id'] . " value='" . htmlentities($rule_file_name, ENT_QUOTES) . "' />";
	}
	$hidden .= "<INPUT type=hidden id=rule_id_-1 value='Apply this will empty the rule for this goal' />";
	$rules_dd .= "</SELECT>" . $hidden;
	return array($rules_dd, $rule_desc);
}

//function save_goal($sequence_id, $goal_id, $name, $description, $duration, $access_matrix_id, $entrance_rule_desc, $inflight_rule_desc, $exit_rule_desc ) {
function save_goal($sequence_id, $goal_id, $name, $description, $duration, $entrance_rule_desc, $inflight_rule_desc, $exit_rule_desc,
					$entrance_rule_file_name, $inflight_rule_file_name, $exit_rule_file_name ) {
	$entrance_rule_desc = SQLString($entrance_rule_desc, "text");
	$inflight_rule_desc = SQLString($inflight_rule_desc, "text");
	$exit_rule_desc = SQLString($exit_rule_desc, "text");
	$entrance_rule_file_name = SQLString($entrance_rule_file_name, "text");
	$inflight_rule_file_name = SQLString($inflight_rule_file_name, "text");
	$exit_rule_file_name = SQLString($exit_rule_file_name, "text");
	if ( is_numeric($goal_id) && $goal_id > 0 ) {
		// update
		$sql = sprintf("UPDATE goal 
							SET 
								name = %s, description = %s, duration = %d, 
								access_matrix_id = 0, entrance_rule_desc = %s, inflight_rule_desc = %s, exit_rule_desc = %s,
								entrance_rule_file_name = %s, inflight_rule_file_name = %s, exit_rule_file_name = %s
							WHERE
								id = %d", 
					$name, $description, $duration, $entrance_rule_desc, $inflight_rule_desc, $exit_rule_desc,
					$entrance_rule_file_name, $inflight_rule_file_name, $exit_rule_file_name, $goal_id);
	} else {
		// insert
		$sql = sprintf("INSERT INTO goal 
								(workflow_sequence_id, name, description, duration, access_matrix_id, entrance_rule_desc, inflight_rule_desc, exit_rule_desc, entrance_rule_file_name, inflight_rule_file_name, exit_rule_file_name)
							VALUES
								(%d, %s, %s, %d, %d, %s, %s, %s, %s, %s, %s)",
					$sequence_id, $name, $description, $duration, 0, $entrance_rule_desc, $inflight_rule_desc, $exit_rule_desc,
					$entrance_rule_file_name, $inflight_rule_file_name, $exit_rule_file_name);
	}
	$st = @mysql_query($sql);
	if ( $st ) {
		$last_id = mysql_insert_id();
		$goal_id = $last_id == 0 ? $goal_id : $last_id;
		$goal_opt = show_goal_opt( $sequence_id, $goal_id);
		$return = array('sequence_id' => $sequence_id,
						'goal_id'	=> $goal_id,
						'goal_opt'	=> $goal_opt,
						'sql'		=> $sql,
						'status'	=> 0,
						'msg'		=> 'Goal ' . $name . ' saved successfully.');
	} else {
		$return = array('sequence_id' => $sequence_id,
						'goal_id'	=> $goal_id,
						'msg'		=> 'Error saving goal: ' . mysql_error(),
						'sql'		=> $sql);
	}
	return $return;
}

function update_total_duration($workflow_id) {
// get all sequence with their pregoals for this workflow
	$sql = sprintf("SELECT ws.id, pg.pre_goal_id FROM workflow_sequence ws
						LEFT JOIN pregoal pg ON ws.id = pg.workflow_sequence_id
						WHERE ws.workflow_id = %d",
				$workflow_id);
	$st = mysql_query($sql);
	$max_duration = 0;
	while ( $ws = mysql_fetch_assoc($st) ) {
        $ws_duration = 0;
        $pre_duration = 0;
        $pregoal_id = $ws['pre_goal_id'];
        $ws_id = $ws['id'];
        while ( isset($pregoal_id) ) {
                // loop until no more pregoals in this chain
                // printf("\tWorkflow Sequence %d has pregoal, pregoal_id is %d\n", $ws_id, $pregoal_id);
                // find duration of the pre-sequence - weight up to the pregoal
                $d_sql = sprintf("SELECT sum(a.duration) as duration, a.workflow_sequence_id as ws_id FROM goal a, goal b 
                                                        WHERE a.workflow_sequence_id = b.workflow_sequence_id 
                                                                AND a.weight <= b.weight
                                                                AND b.id = %d",
                                        $pregoal_id);
                $d_st = mysql_query($d_sql);
                $row = mysql_fetch_assoc($d_st);
                $pre_duration += $row['duration'];
				// printf("\tWorkflow Sequence [%d] pregoal duration is %d.\n", $ws_id, $row['duration']);
                //printf("\t\tPregoal %d belongs to workflow sequence [%d], whos duration is %d\n", $pregoal_id, $row['ws_id'], $row['duration']);

                // find pregoal for this sequence, if any
                $pg_sql = sprintf("SELECT workflow_sequence_id, pre_goal_id
                                                        FROM  pregoal
                                                        WHERE workflow_sequence_id = %d", 
                                                $row['ws_id']);
                $pg_st = mysql_query($pg_sql);
                $pregoal = mysql_fetch_assoc($pg_st);
                $pregoal_id = $pregoal['pre_goal_id'];
                $ws_id = $pregoal['workflow_sequence_id'];
        }

        // for each sequence, find it's total duration
        $g_sql = sprintf("SELECT sum(duration) as duration FROM goal WHERE workflow_sequence_id = %d",
                                $ws['id']);
        $g_st = mysql_query($g_sql);
        $row = mysql_fetch_assoc($g_st);
        $ws_duration = $pre_duration + $row['duration'];
		// printf("Workflow sequence %d itself duration is %d, pre-glao duration is %d, total is %d\n", $ws['id'], $row['duration'], $pre_duration, $ws_duration);
        $max_duration = $ws_duration > $max_duration? $ws_duration : $max_duration;
	}
	// printf("Workflow's max duration is %d.\n", $max_duration);
	$wf_sql = sprintf("UPDATE workflow SET total_duration = %d WHERE id = %d", $max_duration, $workflow_id);
	$st = mysql_query($wf_sql);
	return $wf_sql;
}


?>
