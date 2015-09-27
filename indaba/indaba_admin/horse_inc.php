<?php

// define workflow object status (horse status)
define('WAITING', 1);
define('STARTED', 2);
define('COMPLETED', 3);
define('SUSPENDED', 4);
define('CANCELED', 5);


function get_horse($target_id, $product_id) {
	$sql = sprintf("SELECT * FROM horse WHERE target_id = %d AND product_id = %d",
				$target_id, $product_id);
	$st = mysql_query($sql);
	$horse = mysql_fetch_assoc($st);
	return $horse;
}

function get_product($product_id) {
	// get product info for future use
	$sql = "SELECT * FROM product WHERE id = " . $product_id;
	$st = mysql_query($sql);
	$product = mysql_fetch_assoc($st);
	return $product;
}

function check_horse($product_id) {

  $check_rt = array('query_status' => 0, 'query_msg' => '');

  // check workflow readiness
  $sql = sprintf("SELECT workflow_sequence.* FROM workflow_sequence, product 
  					WHERE product.workflow_id = workflow_sequence.workflow_id 
  						AND product.id = %d",
  		$product_id);
  $st = mysql_query($sql);
  if ( mysql_num_rows($st) == 0 ) {
  	$check_rt['query_status'] = 1;
  	$check_rt['query_msg'] = "No workflow sequence defined for the workflow. ";
  	return ($checkrt);
  }
  while ( $ws = mysql_fetch_assoc($st) ) {
  	// check goal readiness
  	$sql = sprintf("SELECT * FROM goal
  						WHERE goal.workflow_sequence_id = %d",
  		$ws['id']);
  	$g_st = mysql_query($sql);
  	if ( mysql_num_rows($g_st) == 0 ) {
  		$check_rt['query_status'] = 1;
  		$check_rt['query_msg'] .= "No goals defined for workflow sequence [" . $ws['name']  . "]. ";
  		break;
  	}
  	while ( $goal = mysql_fetch_assoc($g_st) ) {
  		// check task for goal
  		$sql = sprintf("SELECT * FROM task WHERE goal_id = %d and product_id = %d",
  					$goal['id'], $product_id);
  		$t_st = mysql_query($sql);
  		if ( mysql_num_rows($t_st) == 0 ) {
  			$check_rt['query_status'] = 1;
  			$check_rt['query_msg'] .= "Please define tasks for goal [" . $goal['name'] . "] from PRODUCTS tab before continue. ";
  			break 2;
  		} else {
  			while ( $task = mysql_fetch_assoc($t_st) ) {
  				// check whether role is assigned to the task
  				$r_sql = sprintf("SELECT task_role.*, task.assignment_method 
  									FROM task 
  									LEFT JOIN task_role ON task_role.task_id = task.id  
  									WHERE task.id = %d", 
  							$task['id']);
  				$r_st = mysql_query($r_sql);
  				$row = mysql_fetch_assoc($r_st);
  				if ( empty($row['id']) && $row['assignment_method'] == 2 ) {
  					$check_rt['query_status'] = 1;
  					$check_rt['r_sql'] = $r_sql;
  					$check_rt['query_msg'] .= "Please assign roles to task [" . $task['task_name'] . "] before continue. ";
  					break 3;
  				}
  			}
  		}
  	}
  }
  return ($check_rt );

}


function get_target($target_id) {
	// get target info for future use
	$sql = "SELECT * FROM target WHERE id = " . $target_id;
	$st = mysql_query($sql);
	$target = mysql_fetch_assoc($st);
	return $target;
}
function sync_horse($target_id, $product_id) {
	$rt = array();
	$rt['query_status'] = 0;
	$rt['query_msg'] = '';
	$rt['so_sql'] = '';
	$product = get_product($product_id);
	$target = get_target($target_id);
	// first create workflow_object if workflow_object is not there
	$sql = sprintf("SELECT * FROM horse WHERE product_id = %d AND target_id = %d",
				$product_id, $target_id);
	$st = mysql_query($sql);
	$h = mysql_fetch_array($st);
	if ( ! $h || empty($h['workflow_object_id']) ) {
		// Create workflow_object
		$sql = sprintf("INSERT INTO workflow_object (workflow_id, start_time, status)
							SELECT workflow_id, NULL, %d FROM product WHERE id = %d",
					WAITING, $product_id);
		$st = mysql_query($sql);
		$rt['query_status'] = $st ? $rt['query_status'] : 1;
		$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating workflow_object:" . mysql_error();
		$workflow_object_id = mysql_insert_id();
	} else {
		$workflow_object_id = $h['workflow_object_id'];
		// also need to check whether this workflow_object is for the workflow we are syncing.
		// If not, we need to create new workflow_object and update horse, remove this orphan workflow_object
		$sql = sprintf("SELECT wo.workflow_id FROM workflow_object wo, product p WHERE wo.workflow_id = p.workflow_id AND wo.id = %d AND p.id = %d",
						$workflow_object_id, $product_id);
		$st = mysql_query($sql);
		$wo = mysql_fetch_array($st);
		if ( ! $wo || empty($wo['workflow_id'] ) ) {
			// the existing workflow_object_id in the horse is not used anymore
			// create workflow_object
			$sql = sprintf("INSERT INTO workflow_object (workflow_id, start_time, status)
									SELECT workflow_id, NULL, %d FROM product WHERE id = %d",
							WAITING, $product_id);
			$st = mysql_query($sql);
			$workflow_object_id = mysql_insert_id();
			// update horse for the new workflow_object_id
			$sql = sprintf("UPDATE horse SET workflow_object_id = %d WHERE  product_id = %d AND target_id = %d",
						$workflow_object_id, $product_id, $target_id);
			$st = mysql_query($sql);
		}
	}
	$rt['wo_sql'] = $sql;
	$rt['workflow_object_id'] = $workflow_object_id;

	// create sequence_object entries if it is not there
	$sql = sprintf("SELECT wo.workflow_id, wo.id AS workflow_object_id, ws.id AS workflow_sequence_id, so.id AS sequence_object_id   
						FROM workflow_object wo
						INNER JOIN workflow_sequence ws ON ws.workflow_id = wo.workflow_id
						LEFT JOIN sequence_object so ON wo.id = so.workflow_object_id AND ws.id = so.workflow_sequence_id
						WHERE wo.id = %d",
				$workflow_object_id );
	$st = mysql_query($sql);
	if ( mysql_num_rows($st) == 0 ) {
		// new workflow object, need to create one sequence_object for each
		$sql = sprintf("INSERT INTO sequence_object (workflow_object_id, workflow_sequence_id, status)
							SELECT %d, workflow_sequence.id, 0 FROM workflow_sequence, workflow_object
								WHERE workflow_sequence.workflow_id = workflow_object.workflow_id
									AND workflow_object.id = %d",
					$workflow_object_id, $workflow_object_id);
		$wo_st = mysql_query($sql);
		$rt['query_status'] = $wo_st ? $rt['query_status'] : 1;
		$rt['query_msg'] = $wo_st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating sequence_object:" . mysql_error();
		$rt['so_sql'] = $sql;
	} else {
		// loop through to see any new sequence created
		while ( $workflow = @mysql_fetch_assoc($st) ) {
			if ( empty( $workflow['sequence_object_id'] ) ) {
				// create sequence_object
				$so_sql = sprintf("INSERT INTO sequence_object ( workflow_object_id, workflow_sequence_id, status )
										VALUES ( %d, %d, 0)",
								$workflow['workflow_object_id'], $workflow['workflow_sequence_id']);
				$so_st = mysql_query($so_sql);
				if ( ! $so_st ) {
					get_out(1, "Failed to create sequence_object for workflow_object_id " .
						$workflow['workflow_object_id'] .
						" workflow_sequence_id " . $workflow['workflow_sequence_id'] , $so_sql, mysql_error());
				} else {
					$workflow['sequence_object_id'] = mysql_insert_id();
				}
			}
		}
	}
	// check and create goal_object entries
	$sql = sprintf("SELECT go.id AS goal_object_id, g.id AS goal_id, so.workflow_sequence_id, so.id AS sequence_object_id
						FROM sequence_object so
						INNER JOIN goal g ON g.workflow_sequence_id = so.workflow_sequence_id
						LEFT JOIN goal_object go ON g.id = go.goal_id AND go.sequence_object_id = so.id
						WHERE so.workflow_object_id = %d",
				$workflow_object_id);
	$st = mysql_query($sql);
	$rt['query_status'] = $st ? $rt['query_status'] : 1;
	while ( $go = mysql_fetch_assoc($st) ) {
		if ( empty($go['goal_object_id']) ) {
			// need to create goal_object
			$sql = sprintf("INSERT INTO goal_object (goal_id, status, sequence_object_id )
								VALUES ( %d, 0, %d)",
					$go['goal_id'], $go['sequence_object_id']);
			$st1 = mysql_query($sql);
			$rt['query_status'] = $st ? $rt['query_status'] : 1;
			$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating goal_object:" . mysql_error();
			$rt['so_sql'] .= " " . $sql;
		}
	}

	// check again if this is a new horse
	if ( $h ) {
		// this is an old horse, we can get out now
		$rt['horse_id'] = $h['id'];
		return $rt;
	}

	// we can create horse now, since content_header is not created yet, use 0 for now
	$sql = sprintf("INSERT INTO horse (product_id, target_id, start_time, content_header_id, workflow_object_id)
						VALUES ( %d, %d, NULL, 0, %d)",
				$product_id, $target_id, $workflow_object_id);
	$st = mysql_query($sql);
	$rt['query_status'] = $st ? $rt['query_status'] : 1;
	$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating horse:" . mysql_error();
	$horse_id = mysql_insert_id();
	$rt['h_sql'] = $sql;
	$rt['horse_id'] = $horse_id;

	// create content header record
	$sql = sprintf("INSERT INTO content_header (project_id, content_type, content_object_id, horse_id, title, create_time, status)
						VALUES (%d, %d, 0, %d, '%s', now(), 0)",
				$product['project_id'], $product['content_type'], $horse_id, $target['short_name'] . " - " . $product['name']);
	$st = mysql_query($sql);
	$rt['query_status'] = $st ? $rt['query_status'] : 1;
	$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating content_header:" . mysql_error();
	$content_header_id = mysql_insert_id();
	$rt['ch_sql'] = $sql;
	$rt['content_header_id'] = $content_header_id;
	// update horse with newly generated content_header_id
	$sql = sprintf("UPDATE horse SET content_header_id = %d WHERE id = %d",
			$content_header_id, $horse_id);
	$st = mysql_query($sql);
	$rt['query_status'] = $st ? $rt['query_status'] : 1;
	$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem updating horse: " . mysql_error();
	// create content object
	if ( $product['content_type'] == JOURNAL ) {
		// create entry in journal_content_object
		$sql = sprintf("INSERT INTO journal_content_object (content_header_id, body, journal_config_id) VALUES (%d, null, %d)",
				$content_header_id, $product['product_config_id']);
		$st = mysql_query($sql);
		$rt['query_status'] = $st ? $rt['query_status'] : 1;
		$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating  journal_content_object: " . mysql_error();
		$content_object_id = mysql_insert_id();
		$rt['jco_sql'] = $sql;
		// update content header using newly generated content_object_id
		$sql = sprintf("UPDATE content_header SET content_object_id = %d WHERE id = %d",
				$content_object_id, $content_header_id);
		$st = mysql_query($sql);
		$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem updating content_header: " . mysql_error();
	} else {
		// survey type, create survey_content_object entry
		$sql = sprintf("INSERT INTO survey_content_object (content_header_id, survey_config_id) VALUES ( %d, %d)",
				$content_header_id, $product['product_config_id']);
		$st = mysql_query($sql);
		$rt['query_status'] = $st ? $rt['query_status'] : 1;
		$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating content_header: " . mysql_error();
		$content_object_id = mysql_insert_id();
		$rt['sco_sql'] = $sql;
		// update content header using newly generated content_object_id
		$sql = sprintf("UPDATE content_header SET content_object_id = %d WHERE id = %d",
				$content_object_id, $content_header_id);
		$st = mysql_query($sql);
		$rt['query_status'] = $st ? $rt['query_status'] : 1;
		$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem updating content_header: " . mysql_error();

		// create survey_answer entries
		$sql = sprintf("INSERT INTO survey_answer (survey_content_object_id, survey_question_id, answer_object_id, answer_user_id)
							SELECT %d, id, null, null FROM survey_question
								WHERE survey_config_id = %d",
					$content_object_id, $product['product_config_id']);
		$st = mysql_query($st);
		$rt['query_status'] = $st ? $rt['query_status'] : 1;
		$rt['query_msg'] = $st ? $rt['query_msg'] : $rt['query_msg'] . ". Problem creating survey_answer entries: " . mysql_error();
		$rt['sa_sql'] = $sql;
	}
	$rt['content_object_id'] = $content_object_id;
	return $rt;
}

?>
