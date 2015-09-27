<?php
session_start();
if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authuser'] )) exit;
require_once("./include/config.inc");

$_SESSION['photo_type'] = 'project_logo';

$required = "<font color=red>*</font>";

if ( isset($_GET['action']) && $_GET['action'] == 'show_project_list' ) {
	// get project list
	$project_id = isset($_GET['id']) ? $_GET['id'] : '';
	$project_list = "<li class='ui-widget-content' style='margin:5px 40px 5px 50px; ' id=0 >Add New Project</li>";
	$p_sql = "SELECT * FROM project";
	$st = mysql_query($p_sql);
	while ( $project = mysql_fetch_assoc($st) ) {
		$selected = $project_id == $project['id'] ? " class=ui-selected " : " class=ui-widget-content ";
		$project_list .= "<li " . $selected . " style='float:left; margin-left:10px; width:180px;' id=" . $project['id'] . ">" .
							$project['code_name'] . "</li>";
	}
	// get admin_user_list drop down
	$admin_user_list = "<SELECT id=admin_user_id ><option value=0>Select Admin User</option>";
	$u_sql = "SELECT * FROM user WHERE status = 1 ORDER BY last_name";
	$st = mysql_query($u_sql);
	while ( $user = fetch_html_entities($st) ) {
		$user['first_name'] = $user['first_name'];
		$user['last_name'] = $user['last_name'];
		$admin_user_list .= "<option value=" . $user['id'] . ">" . $user['last_name'] . ", " . $user['first_name'] . "</option>";
	}
	$admin_user_list .= "</SELECT>" . $required;
	// get organization_list drop down
	$organization_list = "<SELECT id=organization_id><option value=0>Select Organization</option>";
	$o_sql = "SELECT * FROM organization";
	$st = mysql_query($o_sql);
	while ( $org = mysql_fetch_assoc($st) ) {
		$org['name'] = htmlentities($org['name'], ENT_QUOTES, 'UTF-8');
		$organization_list .= "<option value=" . $org['id'] . ">" . $org['name'] . "</option>";
	}
	$organization_list .= "</SELECT>" . $required;
	// get access_matrix_list drop down
	$access_matrix_list = "<SELECT id=access_matrix_id><option value=0>Select Action Rights Setting</option>";
	$a_sql = "SELECT * FROM access_matrix";
	$st = mysql_query($a_sql);
	while ( $access_matrix = mysql_fetch_assoc($st) ) {
		$access_matrix_list .= "<option value=" . $access_matrix['id'] . ">" . $access_matrix['name'] . "</option>";
	}
	$access_matrix_list .= "</SELECT>" . $required;
	// get view_matrix_list drop down
	$view_matrix_list = "<SELECT id=view_matrix_id><option value=0>Select User Privacy Setting</option>";
	$v_sql = "SELECT * FROM view_matrix";
	$st = mysql_query($v_sql);
	while ( $view_matrix = mysql_fetch_assoc($st) ) {
		$view_matrix_list .= "<option value=" . $view_matrix['id'] . ">" . $view_matrix['name'] . "</option>";
	}
	$view_matrix_list .= "</SELECT>" . $required;
	// get study_period_list drop down
	$study_period_list = "<SELECT id=study_period_id><option value=0>Select Study Period</option>";
	$s_sql = "SELECT * FROM study_period";
	$st = mysql_query($s_sql);
	while ( $study_period = mysql_fetch_assoc($st) ) {
		$study_period_list .= "<option value=" . $study_period['id'] . ">" . $study_period['name'] . "</option>";
	}
	$study_period_list .= "</SELECT>" . $required;
	
	$rt = array('project_list' => $project_list,
				'admin_user_list'	=> $admin_user_list,
				'organization_list'		=> $organization_list,
				'access_matrix_list'	=> $access_matrix_list,
				'view_matrix_list'		=> $view_matrix_list,
				'study_period_list'		=> $study_period_list,
				'query_status'	=> 0,
				'query_msg'		=> '');
	echo json_encode($rt);
	exit;
}
if ( isset($_GET['action']) && $_GET['action'] == 'show_project' ) {
	$id = $_GET['id'];
	$rt = array('project_owner' => '',
				'code_name'		=> '',
				'description'	=> '',
				'admin_user_id'	=> 0,
				'organization_id'	=> 0,
				'access_matrix_id'	=> 0,
				'view_matrix_id'	=> 0,
				'start_time'	=> '',
				'close_time'	=> '',
				'study_period_id'	=> 0,
				'ready_to_start'	=> 0,
				'is_active'		=> 0,
				'visibility'	=> 1,
				'query_status'	=> 0,
				'query_msg'		=> '');
	if ( $id == 0 ) {
		echo json_encode($rt);
		exit;
	}
	$sql = sprintf("SELECT p.*, u.last_name, u.first_name, datediff(curdate(), p.start_time) as ready_to_start
						FROM project p, user u
						WHERE p.owner_user_id = u.id
							AND p.id = %d",
				$id);
	$st = mysql_query($sql);
	if ( ! $st ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Error: " . mysql_error();
		echo json_encode($rt);
		exit;
	}
	$project = mysql_fetch_assoc($st);
	$project['logo_path'] = empty($project['logo_path']) ? '' : $project['logo_path'];
	$rt = array('project_owner' => $project['last_name'] . ", " . $project['first_name'],
			'code_name'		=> $project['code_name'],
			'description'	=> $project['description'],
			'admin_user_id'	=> $project['admin_user_id'],
			'organization_id'	=> $project['organization_id'],
			'access_matrix_id'	=> $project['access_matrix_id'],
			'view_matrix_id'	=> $project['view_matrix_id'],
			'start_time'	=> $project['start_time'],
			'close_time'	=> $project['close_time'],
			'ready_to_start'	=> $project['ready_to_start'],
			'study_period_id'	=> $project['study_period_id'],
			'logo_path'	=> $project['logo_path'],
			'is_active'	=> $project['is_active'],
			'visibility'=> $project['visibility'],
			'sql'			=> $sql,
			'query_status'	=> 0,
			'query_msg'		=> '');

	echo json_encode($rt);
	$_SESSION['current_project_id'] = $id;
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'save_project' ) {
	$code_name = SQLString($_GET['code_name'], 'text');
	$description = SQLString($_GET['description'], 'text');
	$access_matrix_id = $_GET['access_matrix_id'];
	$view_matrix_id = $_GET['view_matrix_id'];
	$study_period_id = $_GET['study_period_id'];
	$start_time = SQLString($_GET['start_time'], 'text');
	$close_time = SQLString($_GET['close_time'], 'text');
	$admin_user_id = $_GET['admin_user_id'];
	$organization_id = $_GET['organization_id'];
	$is_active = $_GET['is_active'];
	$visibility = $_GET['visibility'];
	$id = $_GET['project_id'];

	if ( $id == 0 ) {
		// INSERT
		$sql = sprintf("INSERT INTO project ( code_name, description, owner_user_id, creation_time, access_matrix_id, view_matrix_id, 
												start_time, close_time, study_period_id, status, admin_user_id, is_active, organization_id, visibility)
							VALUES (%s, %s, %d, now(), %d, %d, %s, %s, %d, 0, %d, %d, %d, %d)",
					$code_name, $description, $_SESSION['user_id'], $access_matrix_id, $view_matrix_id,
					$start_time, $close_time, $study_period_id, $admin_user_id, $is_active, $organization_id, $visibility);
	} else {
		// UPDATE
		$sql = sprintf("UPDATE project
							SET code_name = %s, description = %s, access_matrix_id = %d,
								view_matrix_id = %d, start_time = %s, close_time = %s, study_period_id = %d, 
								admin_user_id = %d, is_active = %d, organization_id = %d, visibility = %d
							WHERE id = %d",
					$code_name, $description, $access_matrix_id, $view_matrix_id,
					$start_time, $close_time, $study_period_id, $admin_user_id, $is_active, $organization_id, $visibility, $id);
	}
	$st = mysql_query($sql);
	if ( ! $st ) {
		$rt['sql'] = $sql;
		$rt['query_status'] = 1;
		$rt['query_msg'] = 'Error saving project: ' . mysql_error();
		echo json_encode($rt);
		exit;
	}
	$project_id =  $id == 0 ? mysql_insert_id() : $id;
	$_SESSION['current_project_id'] = $project_id;
	$rt = array('sql' => $sql,
				'project_id'	=> $project_id,
				'query_status'	=> 0,
				'query_msg'		=> "Project [" . $code_name . "] saved."
				);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'delete_project' ) {
	// delete from project, project_contact, project_target, project_membership, project_roles, team, team_user, product, horse
	$delsql = sprintf("DELETE FROM project, project_contact, project_target, project_membership, project_roles, 
									team, team_user, product, horse
						USING project LEFT JOIN 
							( project_contact, project_target, project_membership, project_roles, team, team_user, product, horse )
						ON (
							project.id = project_contact.project_id
							AND project.id = project_target.project_id
							AND project.id = project_membership.project_id
							AND project.id = project_roles.project_id
							AND project.id = team.project_id
							AND team.id = team_user.team_id 
							AND project.id = product.project_id
							AND product.id = horse.product_id)
						WHERE
							project.id = %d ",
					$_GET['project_id']);
	$st = mysql_query($delsql);
	if ( $st ) {
		$query_status = 0;
		$query_msg = "Project deleted";
	} else {
		$query_status = 1;
		$query_msg = "Error deleting project: " . mysql_error();
	}
	$rt = array('query_status' => $query_status,
				'query_msg'		=> $query_msg,
				'sql'			=> $delsql);
	echo json_encode($rt);
	exit;
}


?>
