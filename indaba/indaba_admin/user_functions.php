<?php

session_start();
if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authuser'] )) exit;
require_once("./include/config.inc");

define ('INACTIVE', 0);
define ('ACTIVE', 1);
define ('DELETED', 2);

if ( isset($_GET['action']) && $_GET['action'] == 'showUser' ) {
	$rt = showUser($_GET['id']);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'saveUser' ) {
	$username = isset($_GET['username']) ? SQLString($_GET['username'], "text") : '';
	$first_name = isset($_GET['first_name']) ? SQLString(html_entity_decode($_GET['first_name'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$last_name = isset($_GET['last_name']) ? SQLString(html_entity_decode($_GET['last_name'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$email = isset($_GET['email']) ? SQLString($_GET['email'], "text") : '';
	$password = isset($_GET['password']) ? SQLString(html_entity_decode($_GET['password'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$timezone = isset($_GET['timezone']) ? SQLString(html_entity_decode($_GET['timezone'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$phone = isset($_GET['phone']) ? SQLString(html_entity_decode($_GET['phone'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$cell_phone = isset($_GET['cell_phone']) ? SQLString(html_entity_decode($_GET['cell_phone'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$address = isset($_GET['address']) ? SQLString(html_entity_decode($_GET['address'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$location = isset($_GET['location']) ? SQLString(html_entity_decode($_GET['location'], ENT_NOQUOTES, 'UTF-8'), "text") : '';
	$bio = isset($_GET['bio']) ? html_entity_decode($_GET['bio'], ENT_NOQUOTES, 'UTF-8') : "";
	$bio = SQLString($bio, "text");

	if ( $_GET['id'] > 0 ) {
		// update user
		$sql = sprintf("UPDATE user SET
							username = %s,
							first_name = %s,
							last_name = %s,
							email = %s,
							password = %s,
							language_id = %s,
							forward_inbox_msg = %d,
							site_admin = %d,
							number_msgs_per_screen = %d,
							email_detail_level = %d,
							status = %d,
							timezone = %s,
							phone = %s,
							cell_phone = %s,
							address = %s,
							location = %s,
							bio = %s,
							organization_id = %d
						WHERE id = %d",
					$username, $first_name, $last_name, $email, $password, 
					$_GET['language_id'], $_GET['forward_inbox_msg'], $_GET['site_admin'], $_GET['number_msgs_per_screen'],
					$_GET['email_detail_level'], $_GET['status'], $timezone, $phone, $cell_phone,
					$address, $location, $bio, $_GET['organization_id'], $_GET['id']);
	} else {
		// insert
		$sql = sprintf("INSERT INTO user (username, first_name, last_name, email, password, language_id, 
										forward_inbox_msg, site_admin, number_msgs_per_screen, email_detail_level, status,
										timezone, phone, cell_phone, address, location, bio, create_time, organization_id)
								VALUES (%s, %s, %s, %s, %s, %d, 
										%d, %d, %d, %d, %d,
										%s, %s, %s, %s, %s, %s, now(), %d)",
					$username, $first_name, $last_name, $email, $password, $_GET['language_id'], 
					$_GET['forward_inbox_msg'], $_GET['site_admin'], $_GET['number_msgs_per_screen'], $_GET['email_detail_level'], $_GET['status'], 
					$timezone, $phone, $cell_phone, $address, $location, $bio, $_GET['organization_id']);
	}
	
	$st = mysql_query($sql);
	if ( ! mysql_error() ) {
		$status_msg = "User " . $username . " has been saved successfully.";
		$query_status = 0;
	} else {
		$status_msg = "Error saving user " . $username . ". " . mysql_error();
		$query_status = 1;
	}
	if ( isset($_GET['id']) && $_GET['id'] > 0 ) {
		$id = $_GET['id'];
	} elseif ( mysql_insert_id() > 0 ) {
		$id = mysql_insert_id();
	} else {
		$id = -1;
	}
	$rt = array('sql' => $sql, 'status_msg' => $status_msg, 'query_status' => $query_status, 'id' => $id );
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'deleteUser' ) {
	// make sure you do not delete yourself
	if ( $_GET['id'] == $_SESSION['user_id'] ) {
		$query_status = 1;
		$status_msg = "You cannot delete yourself";
	} else {
		// check if the user is owner of something
		$sql = "SELECT * FROM project where owner_user_id = " . $_GET['id'];
		$st = mysql_query($sql);
		if ( mysql_num_rows($st) >= 1 ) {
			$row = mysql_fetch_assoc($st);
			$status_msg = "This user is the owner of project [" . $row['code_name'] . "], please re-assign the owner before deleting this user";
			$query_status = 1;
		} else {
			$sql = sprintf("UPDATE user 
							SET 
								status = %d, password = '', first_name = '', last_name = '', 
								email = '', phone = '', cell_phone = '', bio = '', 
								address = '', location = '', photo = '',
								username = 'DELETED-%d'  WHERE id = %d",
					DELETED, $_GET['id'], $_GET['id']);
			$st = mysql_query($sql);
			$query_status = $st? 0 : 1;
			if ( $query_status == 1 ) {
				$status_msg = "Error deleting user. " . mysql_error();
			} else {
				$status_msg = "User deleted sucessfully";
				// now clean up task_assignment, project_contact, project_membership, team_user
				$sql = sprintf("DELETE FROM task_assignment WHERE assigned_user_id = %d", $_GET['id']);
				$st = mysql_query($sql); 
				$sql = sprintf("DELETE FROM project_contact WHERE user_id = %d", $_GET['id']);
				$st = mysql_query($sql);
				$sql = sprintf("DELETE FROM project_membership WHERE user_id = %d", $_GET['id']); 
				$st = mysql_query($sql); 
				$sql = sprintf("DELETE  FROM team_user WHERE user_id = %d", $_GET['id']);
				$st = mysql_query($sql); 
				// update project if the user is admin user
				$sql = sprintf("UPDATE project SET admin_user_id = owner_user_id WHERE admin_user_id = %d", $_GET['id']);
				$st = mysql_query($sql); 
			}
		}
	}
	$id = $query_status == 1 ? $_GET['id'] : -1;
	$rt = array('status_msg' => $status_msg, 'query_status' => $query_status, 'id' => $id);
	echo json_encode($rt);
	exit;
}


function showUser($id) {
	// build userlist
	$rt = array();
	$user_list = "<li class='ui-widget-content' style='margin:5px 40px 5px 50px; ' id=0 >Add New User</li>";
	$sql = sprintf("SELECT * FROM user WHERE status <> %d ORDER BY last_name ", DELETED) ;
	$st = @mysql_query($sql);
	while ( $user = fetch_html_entities($st) ) {
		$selected = " class=ui-widget-content ";
		$font_begin = '';
		$font_end = '';
		//$user['last_name'] = htmlentities($user['last_name'], ENT_QUOTES, "UTF-8");
		//$user['first_name'] = htmlentities($user['first_name'], ENT_QUOTES, "UTF-8");
		//$user['username'] = htmlentities($user['username'], ENT_QUOTES, "UTF-8");
		//$user['bio'] = htmlentities($user['bio'], ENT_QUOTES, "UTF-8");
		//$user['address'] = htmlentities($user['address'], ENT_QUOTES, "UTF-8");
		if ( $user['id'] == $id ) {
			// return details
			$rt = $user;
			$rt['photo'] = $rt['photo'] == ''  ? NULL : "upload_files/peopleicons/" . $rt['photo'] ;
			$selected = " class=ui-selected ";
		}
		if ( $user['status'] == INACTIVE ) {
			$font_begin = "<font style='color:red; '>";
			$font_end = "</font>";
		}
		$user_list .= "<li " . $selected . "style='float:left; margin-left:10px; width:280px;' id=" . $user['id'] . ">" . 
						$font_begin. $user['username'] . " - " . $user['last_name'] . ", " . $user['first_name'] . $font_end .
						"</li>";
	}
	if ( empty($rt) ) {
		$rt = array('username' => '',
					'first_name'	=> '',
					'last_name'		=> '',
					'organization_id'	=> 0,
					'email'			=> '',
					'password'		=> '',
					'forward_inbox_msg'	=> 1,
					'site_admin'	=> 0,
					'number_msgs_per_screen'	=> 10,
					'email_detail_level'	=> 1,
					'status'	=> 1,
					'timezone'		=> -5,
					'phone'			=> '',
					'cell_phone'	=> '',
					'address'		=> '',
					'location'		=> '',
					'photo'			=> '',
					'bio'			=> '');
	} else {
		$_SESSION['current_user_id'] = $id;
		$_SESSION['photo_type'] = "user_photo";
	}
	$rt['user_list'] = $user_list;
	// build user_language
	$user_language = "<SELECT id=language_id>";
	$sql = "SELECT * FROM language WHERE status = 0";
	$st = mysql_query($sql);
	while ( $language = mysql_fetch_assoc($st) ) {
		$selected = isset($rt['language_id']) && $language['id'] == $rt['language_id'] ? " selected=selected " : '';
		$user_language .= "<option value=" . $language['id'] . $selected . ">" . $language['language'] . " - " . $language['language_desc'] . "</option>";
	}
	$user_language .= "</SELECT>";
	$rt['user_language'] = $user_language;
	// build organization list
	$user_organization = "<SELECT id=organization_id>";
	$sql = "SELECT * FROM organization";
	$st = mysql_query($sql);
	while ( $org = mysql_fetch_assoc($st) ) {
		$selected = isset($rt['organization_id']) && $org['id'] == $rt['organization_id'] ? " selected=selected " : '';
		$user_organization .= "<option value=" . $org['id'] . $selected . ">" . $org['name'] . "</option>";
	}
	$rt['user_organization'] = $user_organization;
	return $rt;
}

