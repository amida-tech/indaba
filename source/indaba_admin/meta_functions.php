<?php

require_once("include/config.inc");

// Organization functions
function processOrganization($action, $id, $name, $address, $url, $admin_user_id) {
	if ( $action == 'showoption' ) {
		$organizationOpt = organizationOption($id);
		echo $organizationOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$organizationDetail = showOrganization($action, $id);
		echo json_encode(array( 'form' => $organizationDetail));
		return;
	}
	$show_id = updateOrganization($action, $id, $name, $address, $url, $admin_user_id);
	if ( $action == 'update' || $action == 'addnew' ) {
		$organizationDetail = showOrganization('show', $show_id);
		$organizationOpt = organizationOption($show_id);
		$rt = array( 'form' => $organizationDetail, 'Opt' => $organizationOpt, 'msg' => 'Organization added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$organizationOpt = organizationOption(0);
		$rt = array('form' => '', 'Opt' => $organizationOpt, 'msg' => 'Organization deleted');
		echo json_encode($rt);
		return;
	}
}


function organizationOption($id) {
	$sql = "SELECT * FROM organization ORDER BY name"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$organizationOption = "<select id=organization_id name=organization_id onChange='showMeta();'><option value=-1>Select Organization to Manage</option><option value=0>Add New Organization</option>";
	$i = 0;
	while ( $organization = fetch_html_entities($st) ) {
		$selected = '';
		if ( isset( $id ) && $organization['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$organizationOption .= "<option value=\"" . $organization['id'] . "\" " . $selected . " >" . $organization['name'] . "</option>";
	}
	$organizationOption .= "</SELECT>";
	return $organizationOption;
}

function updateOrganization($action, $id, $name, $address, $url, $admin_user_id) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO organization ( name, address, url, admin_user_id)
							VALUES ( %s, %s, %s, %d)",
						SQLString($name, "text"), SQLString($address, "text"), SQLString($url, "text"), $admin_user_id);
		$st = mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE organization SET name = %s, address = %s, url = %s, admin_user_id = %d WHERE id = '%s'",
				SQLString($name, "text"), SQLString($address, "text"), SQLString($url, "text"), $admin_user_id, $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// Check if the organization is in use by project
		$sql = "SELECT p.code_name FROM project p, organization o WHERE o.id = p.organization_id AND o.id = " . $id;
		$st = mysql_query($sql);
		$msg = 'This organization has assigned projects:';
		while ( $project = fetch_html_entities($st)) {
			$msg .= " " . $project['code_name'];
		}
		if ( mysql_num_rows($st) >= 1 ) {
			$msg .= ". Please unassign the organization from project before delete.";
			return $msg;
		}
	// Check if the organization is in use by users
		$sql = "SELECT u.username FROM user u, organization o WHERE o.id = u.organization_id AND o.id = " . $id;
		$st = mysql_query($sql);
		$msg = 'This organization has assigned users:';
		while ( $user = fetch_html_entities($st)) {
			$msg .= " " . $user['username'];
		}
		if ( mysql_num_rows($st) >= 1 ) {
			$msg .= ". Please unassign the organization from users before delete.";
			return $msg;
		}
		$sql = sprintf("DELETE FROM organization WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showOrganization($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateOrganization();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateOrganization();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateOrganization();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM organization WHERE id = '%s'", $id);
		$st = @mysql_query($sql);
		$organization = fetch_html_entities($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=organization_id name=organization_id value=\"" . $organization['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$name = isset($organization['name']) ? $organization['name'] : '';
	$address = isset($organization['address']) ? $organization['address'] : '';
	$url = isset($organization['url']) ? $organization['url'] : '';
	$admin_user_id = isset($organization['admin_user_id']) ? $organization['admin_user_id'] : 0;

	$form =  "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Organization Name</td>
		<td align=center>URL</td>
		<td align=center>Address</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=organization_name name=organization_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=url name=url value=\"" . htmlentities($url, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  	<td colspan=2 align=center><textarea cols=30 rows=5 id=address name=address>" . htmlentities($address, ENT_QUOTES, 'UTF-8') . "</textarea></td>
	  </tr>
	  <tr>
		<td colspan=3>&nbsp</td>
	  </tr>
	  <tr><td colspan=3 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END organization functions

// Role functions
function processRole($action, $id, $name, $description) {
	if ( $action == 'showoption' ) {
		$roleOpt = roleOption($id);
		echo $roleOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$roleDetail = showRole($action, $id);
		echo json_encode(array( 'form' => $roleDetail));
		return;
	}
	$show_id = updateRole($action, $id, $name, $description);
	if ( $action == 'update' || $action == 'addnew' ) {
		$roleDetail = showRole('show', $show_id);
		$roleOpt = roleOption($show_id);
		$rt = array( 'form' => $roleDetail, 'Opt' => $roleOpt, 'msg' => 'Role added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$roleOpt = roleOption(0);
		$rt = array('form' => '', 'Opt' => $roleOpt, 'msg' => 'Role deleted');
		echo json_encode($rt);
		return;
	}
}


function roleOption($id) {
	$sql = "SELECT * FROM role ORDER BY name"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$roleOption = "<select id=role_id name=role_id onChange='showMeta();'><option value=-1>Select Role to Manage</option><option value=0>Add New Role</option>";
	$i = 0;
	while ( $role = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $role['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$roleOption .= "<option value=\"" . $role['id'] . "\" " . $selected . " >" . $role['name'] . "</option>";
	}
	$roleOption .= "</SELECT>";
	return $roleOption;
}

function updateRole($action, $id, $name, $description) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO role ( name, description)
							VALUES ( %s, %s)",
						SQLString($name, "text"), SQLString($description, "text"));
		$st = @mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE role SET name = %s, description = %s WHERE id = '%s'",
				SQLString($name, "text"), SQLString($description, "text"), $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// Check if the role is in use
		$sql = "SELECT p.code_name FROM project p, project_roles pr WHERE p.id = pr.project_id AND pr.role_id = " . $id;
		$st = mysql_query($sql);
		$msg = 'This role is used by projects:';
		while ( $project = mysql_fetch_assoc($st)) {
			$msg .= " " . $project['code_name'];
		}
		if ( mysql_num_rows($st) >= 1 ) {
			$msg .= ". Please unassign the role from project before delete.";
			return $msg;
		}
		$sql = sprintf("DELETE FROM role WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showRole($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateRole();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateRole();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateRole();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM role WHERE id = '%s'", $id);
		$st = @mysql_query($sql);
		$role = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=role_id name=role_id value=\"" . $role['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$name = isset($role['name']) ? $role['name'] : '';
	$description = isset($role['description']) ? $role['description'] : '';

	$form =  "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Role Name</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=role_name name=role_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=role_description name=role_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Role functions


function processLanguage($action, $id, $name, $description, $status) {
	if ( $action == 'showoption' ) {
		$languageOpt = languageOption($id);
		echo $languageOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$languageDetail = showLanguage($action, $id);
		echo json_encode(array( 'form' => $languageDetail) );
		return;
	}
	$show_id = updateLanguage($action, $id, $name, $description, $status);
	if ( $action == 'update' || $action == 'addnew') {
		$languageDetail = showLanguage('show', $show_id);
		$languageOpt = languageOption($show_id);
		$rt = array( 'form' => $languageDetail, 'Opt' => $languageOpt, 'msg' => 'Language added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$languageOpt = languageOption(0);
		$rt = array('form' => '', 'Opt' => $languageOpt, 'msg' => 'Language deleted');
		echo json_encode($rt);
		return;
	}
}


function languageOption($id) {
	$sql = "SELECT * FROM language ORDER BY language";
	$st = @mysql_query($sql);
	$languageOption = "<select id=language_id name=language_id onChange='showMeta();'><option value=-1>Select Language to Manage</option><option value=0>Add New Language</option>";
	$i = 0;
	while ( $language = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $language['id'] == $id && $id <> 0 ) {
			$selected = " selected=selected ";
		}
		$languageOption .= "<option value=\"" . $language['id'] . "\" " . $selected . " >" . $language['language'] . "</option>";
	}
	$languageOption .= "</SELECT>";
	return $languageOption;
}

function updateLanguage($action, $id, $name, $description, $status) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO language ( language, language_desc, status)
							VALUES ( %s, %s, '%s')",
						SQLString($name, "text"), SQLString($description, "text"), $status);
		$st = @mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE language SET language = %s, language_desc = %s, status = '%s' WHERE id = '%s'",
				SQLString($name, "text"), SQLString($description, "text"), $status, $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// do not check before delete
		$sql = sprintf("DELETE FROM language WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showLanguage($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateLanguage();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateLanguage();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateLanguage();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM language WHERE id = '%s'", $id);
		$st = @mysql_query($sql);
		$language = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=language_id name=language_id value=\"" . $language['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$name = isset($language['language']) ? $language['language'] : '';
	$description = isset($language['language_desc']) ? $language['language_desc'] : '';
	$checked = isset($language['status']) && $language['status'] == 0 ? 'checked=checked' : '';

	$form =  "<form> <table width='80%'border=0>
	  <tr>
		<td align=center>Language</td>
		<td align=center>Description</td>
		<td align=center>Active</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=language_name name=language_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=language_description name=language_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=checkbox id=language_status name=language_status " . $checked . " /></td>
	  </tr>
	  <tr>
		<td colspan=3>&nbsp</td>
	  </tr>
	  <tr><td colspan=3 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}

// Reference functions
function processReference($action, $id, $name, $description, $choice_type, $choices) {
	if ( $action == 'showoption' ) {
		$referenceOpt = referenceOption($id);
		echo $referenceOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$referenceDetail = showReference($action, $id);
		echo json_encode(array('form' => $referenceDetail) );
		return;
	}
	$show_id = updateReference($action, $id, $name, $description, $choice_type, $choices);
	if ( $action == 'update' || $action == 'addnew' ) {
		$referenceDetail = showReference('show', $show_id);
		$referenceOpt = referenceOption($show_id);
		$rt = array('form' => $referenceDetail, 'Opt' => $referenceOpt, 'msg' => 'Reference added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$referenceOpt = referenceOption(0);
		$rt = array('form' => '', 'Opt' => $referenceOpt, 'msg' => 'Reference deleted');
		echo json_encode($rt);
		return;
	}
}


function referenceOption($id) {
	$sql = "SELECT * FROM reference ORDER BY name"; 
	$st = @mysql_query($sql);
	$referenceOption = "<select id=reference_id name=reference_id onChange='showMeta();'><option value=-1>Select Reference to Manage</option><option value=0>Add New Reference</option>";
	$i = 0;
	while ( $reference = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $reference['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$referenceOption .= "<option value=\"" . $reference['id'] . "\" " . $selected . " >" . $reference['name'] . "</option>";
	}
	$referenceOption .= "</SELECT>";
	return $referenceOption;
}

function updateReference($action, $id, $name, $description, $choice_type, $choices) {
	if ( $action == 'delete' ) {
		// check if the reference is used elsewhere
		$sql = "SELECT si.* FROM survey_indicator WHERE reference_id = " . $id;
		$st = mysql_query($sql);
		$msg = "This Reference is used by survey indicators:";
		while ( $list = mysql_fetch_assoc($st)) {
			$msg .= " " . $list['name'];
		}
		if ( mysql_num_rows($st) >= 1 ) {
			$msg .= ". Please unassign the reference from survey indicator before delete.";
			return $msg;
		}
		$sql = sprintf("DELETE FROM reference WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		$sql = sprintf("DELETE FROM reference_choice WHERE reference_id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO reference ( name, description, choice_type)
							VALUES ( %s, %s, %d)",
						SQLString($name, "text"), SQLString($description, "text"), $choice_type);
		$st = @mysql_query($sql);
		$id = mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE reference SET name = %s, description = %s, choice_type = %d WHERE id = '%s'",
				SQLString($name, "text"), SQLString($description, "text"), $choice_type, $id);
		$st = mysql_query($sql);
	}
	// update choices for this refernece
	$sql = "DELETE FROM reference_choice WHERE reference_id = " . $id;
	$st = mysql_query($sql);
	foreach ( $choices as $key => $value ) {
		$sql = sprintf("INSERT INTO reference_choice ( reference_id, label, weight, mask ) VALUES (%d, %s, %d, %d)",
					$id, SQLString($value, "text"), $key, pow(2, $key));
		$st = mysql_query($sql);
		if ( ! $st ) {
			continue;
		}
	}
	return $id;
}

function showReference($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateReference();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateReference();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset onClick='return show_choices();' /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateReference();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset onClick='return show_choices();' /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM reference WHERE id = '%s'", $id);
		$st = @mysql_query($sql);
		$reference = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=reference_id name=reference_id value=\"" . $reference['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$name = isset($reference['name']) ? $reference['name'] : '';
	$description = isset($reference['description']) ? $reference['description'] : '';
	$choice_type = isset($reference['choice_type']) ? $reference['choice_type'] : '';
	$checked = array('0' => "", '1' => '', '2' => '');
	$checked[$choice_type] = " checked=checked ";

	$rt = "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Reference</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=reference_name name=reference_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=60 id=reference_description name=reference_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr>
		<td colspan=2 align=center>Choices &nbsp;<fieldset>
						<INPUT type=radio id=choice_type name=choice_type value=0 " .  $checked[0] . " onClick='hide_choices();' >No Choice &nbsp;</INPUT>
						<INPUT type=radio id=choice_type name=choice_type value=1 " .  $checked[1] . " onClick='show_choices();' >Single Choice &nbsp;</INPUT>
						<INPUT type=radio id=choice_type name=choice_type value=2 " .  $checked[2] . " onClick='show_choices();' >Multiple Choice &nbsp;</INPUT>
						<p>
						<div id=choices_area style='display: none;'>
							<div style='width:700px; '>
								<div style='font-size:11px; float:left'>
									Edit choices in the box below. The choices are separated by newline (return).
									<textarea align=left id=edit_choice cols=100 rows=15></textarea>
							</div><p>
						</div>
					</fieldset>
		</td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	$rt .= <<< EOF
<script language=javascript>
$(function() {
	$("#source_list, #target_list").sortable({
		connectWith: '.connectedSortable',
    }).disableSelection();
});

</script>
EOF;
	return $rt;

}
// END Reference functions


// StudyPeriod functions
function processStudyPeriod($action, $id, $name, $description) {
	if ( $action == 'showoption' ) {
		$studyperiodOpt = studyperiodOption($id);
		echo $studyperiodOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$studyperiodDetail = showStudyPeriod($action, $id);
		echo json_encode(array('form' => $studyperiodDetail));
		return;
	}
	$show_id = updateStudyPeriod($action, $id, $name, $description);
	if ( $action == 'update' || $action == 'addnew' ) {
		$studyperiodDetail = showStudyPeriod('show', $show_id);
		$studyperiodOpt = studyperiodOption($show_id);
		$rt = array('form' => $studyperiodDetail, 'Opt' => $studyperiodOpt, 'msg' => 'Study Period added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$studyperiodOpt = studyperiodOption(0);
		$rt = array('form' => '', 'Opt' => $studyperiodOpt, 'msg' => 'Study Period deleted');
		echo json_encode($rt);
		return;
	}
}


function studyperiodOption($id) {
	$sql = "SELECT * FROM study_period ORDER BY name"; 
	$st = @mysql_query($sql);
	$studyperiodOption = "<select id=study_period_id name=study_period_id onChange='showMeta();'><option value=-1>Select Study Period to Manage</option><option value=0>Add New Study Period</option>";
	$i = 0;
	while ( $studyperiod = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $studyperiod['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$studyperiodOption .= "<option value=\"" . $studyperiod['id'] . "\" " . $selected . " >" . $studyperiod['name'] . "</option>";
	}
	$studyperiodOption .= "</SELECT>";
	return $studyperiodOption;
}

function updateStudyPeriod($action, $id, $name, $description) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO study_period ( name, description)
							VALUES ( %s, %s)",
						SQLString($name, "text"), SQLString($description, "text"));
		$st = @mysql_query($sql);
		echo mysql_error();
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE study_period SET name = %s, description = %s WHERE id = '%s'",
				SQLString($name, "text"), SQLString($description, "text"), $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// check before delete
		$sql = "SELECT * FROM project WHERE study_period_id = " . $id;
		$st = mysql_query($sql);
		$msg = "This Study Peroid is used by projects:";
		while ( $list = mysql_fetch_assoc($st)) {
			$msg .= " " . $list['code_name'];
		}
		if ( mysql_num_rows($st) >= 1 ) {
			$msg .= ". Please unassign this study period from project before delete.";
			return $msg;
		}
		$sql = sprintf("DELETE FROM study_period WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showStudyPeriod($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateStudyPeriod();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateStudyPeriod();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateStudyPeriod();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM study_period WHERE id = '%s'", $id);
		$st = @mysql_query($sql);
		$studyperiod = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=study_period_id name=study_period_id value=\"" . $studyperiod['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$name = isset($studyperiod['name']) ? $studyperiod['name'] : '';
	$description = isset($studyperiod['description']) ? $studyperiod['description'] : '';

	$form = "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Study Period Name</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=15 id=study_period_name name=study_period_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=study_period_description name=study_period_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Study Period functions



// Notification Type functions
function processNotificationType($action, $id, $name, $description, $language_id, $subject_text, $body_text) {
	if ( $action == 'showoption' ) {
		$notificationtypeOpt = notificationtypeOption($id);
		echo $notificationtypeOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$notificationtypeDetail = showNotificationType($action, $id);
		echo json_encode(array( 'form' => $notificationtypeDetail ));
		return;
	}
	$show_id = updateNotificationType($action, $id, $name, $description, $language_id, $subject_text, $body_text);
	if ( $action == 'update' || $action == 'addnew') {
		$notificationtypeDetail = showNotificationType('show', $show_id);
		$notificationtypeOpt = notificationtypeOption($show_id);
		$rt = array( 'form' => $notificationtypeDetail, 'Opt' => $notificationtypeOpt, 'msg' => 'Notification Type added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$notificationtypeOpt = notificationtypeOption(0);
		$rt = array('form' => '', 'Opt' => $notificationtypeOpt, 'msg' => 'Notification Type deleted');
		echo json_encode($rt);
		return;
	}
}


function notificationtypeOption($id) {
	$sql = "SELECT * FROM notification_type ORDER BY name"; 
	$st = mysql_query($sql);
	$notificationtypeOption = "<select id=notification_type_id name=notification_type_id onChange='showMeta();'><option value=-1>Select User Notification Type to Manage</option><option value=0 >Add New User Notification Type</option>";
	while ( $notificationtype = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $notificationtype['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$notificationtypeOption .= "<option value=\"" . $notificationtype['id'] . "\" " . $selected . " >" . $notificationtype['name'] . "</option>";
	}
	$notificationtypeOption .= "</SELECT>";
	return $notificationtypeOption;
}

function updateNotificationType($action, $id, $name, $description, $language_id, $subject_text, $body_text) {
	if ( $action == 'delete' ) {
		// do not check before delete
		$sql = sprintf("DELETE FROM notification_type, notification_item 
								USING notification_type LEFT JOIN notification_item ON 
									notification_type.id = notification_item.notification_type_id
								WHERE notification_type.id = '%s'", 
					$id);
		$st = mysql_query($sql);
		return null;
	}
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO notification_type ( name, description )
							VALUES ( %s, %s)",
						SQLString($name, "text"), SQLString($description, "text"));
		$st = @mysql_query($sql);
		echo mysql_error();
		$id = mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE notification_type SET name = %s, description = %s
						 WHERE id = '%s'",
				SQLString($name, "text"), SQLString($description, "text"), $id);
		$st = mysql_query($sql);
	}
	if ( $language_id > 0 ) {
		$sql = sprintf("INSERT INTO notification_item (notification_type_id, language_id, subject_text, body_text) VALUES ( %d, %d, %s, %s)
						ON DUPLICATE KEY UPDATE subject_text = %s, body_text = %s",
					$id, $language_id, SQLString($subject_text, "text"), SQLString($body_text, "text"), SQLString($subject_text, "text"), SQLString($body_text, "text"));
		$st = mysql_query($sql);
	}
	return $id;
}

function showNotificationType($action, $id) {

	$display = $id == 0 ? "none" : "inline";
	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Save id='update' onClick='updateNotificationType();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' style='display:" . $display . ";' onClick='updateNotificationType();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$button = $updateButton;

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM notification_type WHERE id = '%s'", $id);
		$st = @mysql_query($sql);
		$notificationtype = @mysql_fetch_assoc($st);
		$id = "<INPUT type=hidden id=notification_type_id name=notification_type_id value=\"" . $notificationtype['id'] . "\" />";
	}

	// build language selection option
	$sql = "SELECT * FROM language";
	$st = @mysql_query($sql);
	$languageOpt = "<SELECT id=n_lang_id name=n_lang_id onChange='show_notification_item();' ><option value=0>Select Language</option>";
	$inactive = "";
	while ( $language = @mysql_fetch_assoc($st) ) {
		$status = isset($language['status']) && $language['status'] == 1 ? " - inactive" : "";
		$languageOpt .= "<option value=" . $language['id'] . ">" . $language['language_desc'] . $status . "</option>";
	}
	$languageOpt .= "</SELECT>";

	//if ( $action == 'add' ) {
	//	$button = $addButton;
	//	$id = '';
	//}

	$name = isset($notificationtype['name']) ? $notificationtype['name'] : '';
	$description = isset($notificationtype['description']) ? $notificationtype['description'] : '';

	$form = <<< EOF
	<script type="text/javascript" src="include/tinybox.js"></script>
	<link rel="stylesheet" href="css/tinybox.css" />
	<script language=javascript>
	T$('showTokens').onclick = function(){
		TINY.box.show('showTokens.php',1,400,350,1)
	};
	function show_notification_item() {
		if ( $("#n_lang_id").val() <= 0 ) {
			$("#notification_item").css("display","none");
			$("#subject_text").attr("disabled", true);
			$("#subject_text").val('');
			$("#body_text").val('');
			$("#body_text").attr("disabled", true);
			return;
		} else {
			$("#subject_text").attr("disabled", false);
			$("#body_text").attr("disabled", false);
		}
		$.ajax({
			type:			"GET",
			cache:			"false",
			dataType:		"json",
			url:			"meta_functions.php",
			data:			"action=show_notification_item&notification_type_id=" + $("#notification_type_id").val() + "&language_id=" + $("#n_lang_id").val(),
			success:		function(obj) {
				$("#notification_item").attr("display","inline");
				$("#subject_text").val(obj.subject_text);
				$("#body_text").val(obj.body_text);
			}
		});
	}

	</script>
EOF;
	$form .= "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>User Notification Type Name</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=30 id=notification_type_name name=notification_type_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=notification_type_description name=notification_type_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td align=center>Language </td>
		<td align=center></td>
	  </tr>
	  <tr>
		<td align=center>" . $languageOpt . "</td>
		<td align=center><div id=showTokens class=btn style='width:150px; height:20px;' >Click for Available Tokens</div></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <div id=notification_item style='display:none; '>
	  <tr>
	    <td align=center>Subject Text &nbsp;</td><td><INPUT id=subject_text disabled=true type=text size=60 /></td>
	  <tr>
	    <td align=center valign=middle>Body Text &nbsp;</td><td><textarea id=body_text disabled=true cols=80 rows=15 ></textarea> </td>
	  </tr>
	  </div>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END User Notification Type functions


// Target functions
function processTarget($action, $id, $name, $target_type, $description, $short_name, $guid, $add_to_target_tag, $remove_from_target_tag) {
	if ( $action == 'showoption' ) {
		$targetOpt = targetOption($id);
		echo $targetOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$targetDetail = showTarget($action, $id);
		echo json_encode(array( 'form' => $targetDetail) );
		return;
	}
	$show_id = updateTarget($action, $id, $name, $target_type, $description, $short_name, $guid, $add_to_target_tag, $remove_from_target_tag);
	if ( $action == 'update' || $action == 'addnew' ) {
		if ( ! is_numeric($show_id) ) {
			// we have an error from updateTarget
			echo json_encode(array("err" => 1, "msg" => $show_id));
			return;
		}
		$targetDetail = showTarget('show', $show_id);
		$targetOpt = targetOption($show_id);
		$rt = array( 'form' => $targetDetail, 'Opt' => $targetOpt, 'msg' => 'Target added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$targetOpt = targetOption(0);
		$rt = array('form' => '', 'Opt' => $targetOpt, 'msg' => 'Target deleted');
		echo json_encode($rt);
		return;
	}
}


function targetOption($id) {
	$sql = "SELECT * FROM target ORDER BY name"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$targetOption = "<select id=target_id name=target_id onChange='showMeta();'><option value=-1>Select Target to Manage</option><option value=0>Add New Target</option>";
	$i = 0;
	while ( $target = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $target['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$targetOption .= "<option value=\"" . $target['id'] . "\" " . $selected . " >" . $target['name'] . "</option>";
	}
	$targetOption .= "</SELECT>";
	return $targetOption;
}

function updateTarget($action, $id, $name, $target_type, $description , $short_name, $guid, $add_to_target_tag, $remove_from_target_tag) {
	// check if the short name already exist under another name
	if ( $id <= 0 ) {
		// this is a new target
		$sql = sprintf("SELECT name FROM target WHERE name <> %s AND short_name = %s", SQLString($name, "text"), SQLString($short_name, "text"));
	} else {
		// existing target
		$sql = sprintf("SELECT name FROM target WHERE id <> %d  AND short_name = %s", $id, SQLString($short_name, "text"));
	}
	$st = @mysql_query($sql);
	while ( $dup = @mysql_fetch_assoc($st)) {
		return "The short name [" . $short_name . "] is already used by " . $dup['name'];
	}
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO target ( name, target_type, description, short_name, guid)
							VALUES ( %s, '%s', %s, %s, %s)",
						SQLString($name, "text"), $target_type, SQLString($description, "text"), SQLString($short_name, "text"), SQLString($guid, "text"));
		$st = @mysql_query($sql);
		$target_id =  mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE target SET name = %s, target_type = '%s', description = %s , short_name = %s, guid = %s WHERE id = '%s'",
				SQLString($name, "text"), $target_type, SQLString($description, "text"), SQLString($short_name, "text"), SQLString($guid, "text"), $id);
		$st = @mysql_query($sql);
		$target_id = $id;
	}
	if ( $action == 'delete' ) {
		// check product before delete
		$sql = "SELECT t.name FROM task t, task_assignment ta WHERE t.id = ta.task_id and ta.target_id = " . $id;
		$st = mysql_query($sql);
		$msg = "This Target is used by tasks:";
		while ( $list = mysql_fetch_assoc($st)) {
			$msg .= " " . $list['name'];
		}
		if ( mysql_num_rows($st) >= 1 ) {
			$msg .= ". Please unassign this target from task before delete.";
			return $msg;
		}
		$sql = sprintf("DELETE target, target_tag 
							FROM target 
							LEFT JOIN target_tag ON target.id = target_tag.target_id
							WHERE target.id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
	if ( isset($remove_from_target_tag) ) {
		// remove from target_tag
		$delete_sql = sprintf("DELETE FROM target_tag 
									WHERE target_id = %d
										AND ttags_id in (",
							$target_id);
		foreach ( $remove_from_target_tag as $ttag_id ) {
			$delete_sql .= $ttag_id . ",";
		}
		$delete_sql = preg_replace('/,$/', ')', $delete_sql);
		$del_st = mysql_query($delete_sql);
	}
	if ( isset($add_to_target_tag) ) {
		// add ttags to target_tag
		$add_sql = "INSERT INTO target_tag (target_id, ttags_id) VALUES";
		foreach ( $add_to_target_tag as $ttag_id ) {
			$add_sql .= sprintf("(%d, %d),", $target_id, $ttag_id);
		}
		$add_sql = preg_replace('/,$/', '', $add_sql);
		$add_st = mysql_query($add_sql);
	}
	return $target_id;

}

function showTarget($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateTarget();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateTarget();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateTarget();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	$target_id = $id;
	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM target WHERE id = '%s' ORDER BY name", $id);
		$st = @mysql_query($sql);
		$target = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=target_id name=target_id value=\"" . $target['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$name = isset($target['name']) ? $target['name'] : '';
	$description = isset($target['description']) ? $target['description'] : '';
	$short_name = isset($target['short_name']) ? $target['short_name'] : '';
	$guid = isset($target['guid']) ? $target['guid'] : '';
	$target_types = array(
						"0" => "Country", 
						"1" => "International Region", 
						"2" => "Sub-national: Province",
						"3" => "Sub-national: State",
						"4" => "Sub-national: Region",
						"5" => "Sub-national: City/Municipality",
						"6" => "Organization",
						"7" => "Government Unit/Project",
						"8" => "Sector");
	$selected = array("0" => '', "1" => '', "2" => '', '3' => '', '4' => '', '5' => '', '6' => '', '7' => '', '8' => '');
	if ( isset($target['target_type'])) {
		$selected[$target['target_type']] = " selected=selected ";
	}
	$target_type_opt = "<SELECT name=target_type id=target_type>";
	foreach ( $target_types as $key => $label ) {
		$target_type_opt .= "<option value=" . $key . $selected[$key] . " >" . $label . "</option>";
	}
	$target_type_opt .= "</SELECT>";
	$tagform = <<< EOF
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
		function filter_tags() {
			var name_like = $("#search_tags").val();
			$("#source_list").find('li').each( function() {
				if ( $(this).text().search(new RegExp(name_like, 'i')) < 0 ) {
					$(this).hide();
				} else {
					$(this).show();
				}
			});
		}


		</script>
		<div style='width:700px; height:20px; margin:10px 0px 0px 10px;' >
			<div style='width:150px; height:20px; float:left; text-align:center; '>Available Tags</div>
			<div style='width:150px; height:20px; float:left; text-align:right; font-size:11px; '>Filter
				<INPUT id=search_tags size=10 style='font-size:11px; text-align:left;color:red;' onKeyUp="filter_tags();" /></div>
			<div style='width:60px; height:20px; float:left; text-align:center; '> </div>
			<div style='width:300px; height:20px; float:left; text-align:center; '>Target Tags</div>
		</div>
		<div style='width:700px; height:200px; margin:5px 0px 50px 10px;' >
			<div id=source_list style='width:300px; height:200px; float:left; border:1px solid black; '>AVA_TAGS</div>
			<div style='width:60px; height:200px; float:left; margin:0px 10px 0px 10px; ' >
				<div style='float:left; text-align:center; width:30px; height:18px; margin:40px 10px 0px 10px; font-size:0.8em ' class=btn onClick='add_to_target();' > &gt;&gt; </div>
				<div style='float:left; text-align:center; width:30px; height:18px; margin:60px 10px 0px 10px; font-size:0.8em ' class=btn onClick='add_to_source();' > &lt;&lt; </div>	
			</div>
			<div id=target_list style='width:300px; height:200px; float:left; border:1px solid black; '>TTAGS</div>
		</div>
EOF;

	$ava_tags = '';
	$ava_sql = sprintf("SELECT * FROM ttags WHERE id NOT IN ( SELECT ttags_id FROM target_tag WHERE target_id = %d) ORDER BY term",
					$target_id);
	$ava_st = mysql_query($ava_sql);
	while ( $row = mysql_fetch_assoc($ava_st) ) {
		$ava_tags .= "<li id=ava_tags_id_" . $row['id'] . ">" . $row['term'] .
						" - " . $row['description'] . "</li>";
	}

	$ttags = '';
	$ttags_sql = sprintf("SELECT a.* FROM ttags a, target_tag b WHERE a.id = b.ttags_id AND b.target_id = %d ORDER BY term", 
					$target_id);
	$ttags_st = mysql_query($ttags_sql);
	while ( $row = mysql_fetch_assoc($ttags_st) ) {
		$ttags .= "<li id=ttags_id_" . $row['id'] . ">" . $row['term'] .
						" - " . $row['description'] . "</li>";
	}

	$tagform = preg_replace("/AVA_TAGS/", $ava_tags, $tagform);
	$tagform = preg_replace("/TTAGS/", $ttags, $tagform);

	$form = "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Target Name</td>
		<td align=center>Description</td>
		<td align=center>Target Type</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=target_name name=target_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=target_description name=target_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center>" . $target_type_opt . "</td>
	  </tr>
	  <tr>
	    <td align=center>Short Name</td>
	    <td align=center colspan=2>GUID</td>
	  </tr>
	  <tr>
	    <td align=center><INPUT type=text size=40 id=short_name name=short_name value=\"" . htmlentities($short_name, ENT_QUOTES, 'UTF-8') . "\" /></td>
	    <td align=center colspan=2><INPUT type=text size=40 id=guid name=guid value=\"" . htmlentities($guid, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=3>" . $tagform . "</td>
	  </tr>
	  <tr><td colspan=3 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Target functions

// Indicator Tags functions
function processItags($action, $id, $name, $description) {
	if ( $action == 'showoption' ) {
		$itagsOpt = itagsOption($id);
		echo $itagsOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$itagsDetail = showItags($action, $id);
		echo json_encode(array( 'form' => $itagsDetail));
		return;
	}
	$show_id = updateItags($action, $id, $name, $description);
	if ( $action == 'update' || $action == 'addnew' ) {
		$itagsDetail = showItags('show', $show_id);
		$itagsOpt = itagsOption($show_id);
		$rt = array( 'form' => $itagsDetail, 'Opt' => $itagsOpt, 'msg' => 'Indicator Tag added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$itagsOpt = itagsOption(0);
		$rt = array('form' => '', 'Opt' => $itagsOpt, 'msg' => 'Indicator Tag deleted');
		echo json_encode($rt);
		return;
	}
}


function itagsOption($id) {
	$sql = "SELECT * FROM itags ORDER BY term"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$itagsOption = "<select id=itags_id name=itags_id onChange='showMeta();'><option value=-1>Select Indicator Tag to Manage</option><option value=0>Add New Indicator Tag</option>";
	$i = 0;
	while ( $itags = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $itags['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$itagsOption .= "<option value=\"" . $itags['id'] . "\" " . $selected . " >" . $itags['term'] . "</option>";
	}
	$itagsOption .= "</SELECT>";
	return $itagsOption;
}

function updateItags($action, $id, $term, $description) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO itags ( term, description)
							VALUES ( %s, %s)",
						SQLString($term, "text"), SQLString($description, "text"));
		$st = @mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE itags SET term = %s, description = %s WHERE id = '%s'",
				SQLString($term, "text"), SQLString($description, "text"), $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// check case_tag 
		$sql = "SELECT * FROM indicator_tag WHERE itags_id = " . $id;
		$st = mysql_query($sql);
		if ( mysql_num_rows($st) >= 1 ) {
			$msg = 'This Tag is used by indicators. You may not delete it.';
			return $msg;
		}
		$sql = sprintf("DELETE FROM itags WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showItags($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateItags();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateItags();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateItags();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM itags WHERE id = '%s' ORDER BY term", $id);
		$st = @mysql_query($sql);
		$itags = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=itags_id name=itags_id value=\"" . $itags['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$term = isset($itags['term']) ? $itags['term'] : '';
	$description = isset($itags['description']) ? $itags['description'] : '';

	$form =  "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Indicator Tag Name</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=itags_term term=itags_term value=\"" . htmlentities($term, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=itags_description name=itags_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Indicator Tags functions

// Case Tags functions
function processCtags($action, $id, $name, $description) {
	if ( $action == 'showoption' ) {
		$ctagsOpt = ctagsOption($id);
		echo $ctagsOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$ctagsDetail = showCtags($action, $id);
		echo json_encode(array( 'form' => $ctagsDetail));
		return;
	}
	$show_id = updateCtags($action, $id, $name, $description);
	if ( $action == 'update' || $action == 'addnew' ) {
		$ctagsDetail = showCtags('show', $show_id);
		$ctagsOpt = ctagsOption($show_id);
		$rt = array( 'form' => $ctagsDetail, 'Opt' => $ctagsOpt, 'msg' => 'Ctags added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$ctagsOpt = ctagsOption(0);
		$rt = array('form' => '', 'Opt' => $ctagsOpt, 'msg' => 'Case Tag deleted');
		echo json_encode($rt);
		return;
	}
}


function ctagsOption($id) {
	$sql = "SELECT * FROM ctags ORDER BY term"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$ctagsOption = "<select id=ctags_id name=ctags_id onChange='showMeta();'><option value=-1>Select Case Tag to Manage</option><option value=0>Add New Case Tag</option>";
	$i = 0;
	while ( $ctags = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $ctags['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$ctagsOption .= "<option value=\"" . $ctags['id'] . "\" " . $selected . " >" . $ctags['term'] . "</option>";
	}
	$ctagsOption .= "</SELECT>";
	return $ctagsOption;
}

function updateCtags($action, $id, $term, $description) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO ctags ( term, description)
							VALUES ( %s, %s)",
						SQLString($term, "text"), SQLString($description, "text"));
		$st = @mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE ctags SET term = %s, description = %s WHERE id = '%s'",
				SQLString($term, "text"), SQLString($description, "text"), $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// check case_tag 
		$sql = "SELECT c.* FROM cases c, case_tag ct WHERE c.id = ct.cases_id AND ct.ctags_id = " . $id;
		$st = mysql_query($sql);
		if ( mysql_num_rows($st) >= 1 ) {
			$msg = 'This Tag is used by cases. You may not delete it.';
			return $msg;
		}
		$sql = sprintf("DELETE FROM ctags WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showCtags($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateCtags();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateCtags();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateCtags();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM ctags WHERE id = '%s' ORDER BY term", $id);
		$st = @mysql_query($sql);
		$ctags = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=ctags_id name=ctags_id value=\"" . $ctags['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$term = isset($ctags['term']) ? $ctags['term'] : '';
	$description = isset($ctags['description']) ? $ctags['description'] : '';

	$form =  "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Case Tag Name</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=ctags_term term=ctags_term value=\"" . htmlentities($term, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=ctags_description name=ctags_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Case Tags functions

// Target Tags functions
function processTtags($action, $id, $name, $description) {
	if ( $action == 'showoption' ) {
		$ttagsOpt = ttagsOption($id);
		echo $ttagsOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$ttagsDetail = showTtags($action, $id);
		echo json_encode(array( 'form' => $ttagsDetail));
		return;
	}
	$show_id = updateTtags($action, $id, $name, $description);
	if ( $action == 'update' || $action == 'addnew' ) {
		$ttagsDetail = showTtags('show', $show_id);
		$ttagsOpt = ttagsOption($show_id);
		$rt = array( 'form' => $ttagsDetail, 'Opt' => $ttagsOpt, 'msg' => 'Ttags added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$ttagsOpt = ttagsOption(0);
		$rt = array('form' => '', 'Opt' => $ttagsOpt, 'msg' => 'Target Tag deleted');
		echo json_encode($rt);
		return;
	}
}


function ttagsOption($id) {
	$sql = "SELECT * FROM ttags ORDER BY term"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$ttagsOption = "<select id=ttags_id name=ttags_id onChange='showMeta();'><option value=-1>Select Target Tag to Manage</option><option value=0>Add New Target Tag</option>";
	$i = 0;
	while ( $ttags = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $ttags['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$ttagsOption .= "<option value=\"" . $ttags['id'] . "\" " . $selected . " >" . $ttags['term'] . "</option>";
	}
	$ttagsOption .= "</SELECT>";
	return $ttagsOption;
}

function updateTtags($action, $id, $term, $description) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO ttags ( term, description)
							VALUES ( %s, %s)",
						SQLString($term, "text"), SQLString($description, "text"));
		$st = @mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE ttags SET term = %s, description = %s WHERE id = '%s'",
				SQLString($term, "text"), SQLString($description, "text"), $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// check target_tag 
		$sql = "SELECT c.* FROM targets c, target_tag ct WHERE c.id = ct.targets_id AND ct.ttags_id = " . $id;
		$st = mysql_query($sql);
		if ( mysql_num_rows($st) >= 1 ) {
			$msg = 'This Tag is used by targets. You may not delete it.';
			return $msg;
		}
		$sql = sprintf("DELETE FROM ttags WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showTtags($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateTtags();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateTtags();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateTtags();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM ttags WHERE id = '%s'", $id);
		$st = @mysql_query($sql);
		$ttags = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=ttags_id name=ttags_id value=\"" . $ttags['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$term = isset($ttags['term']) ? $ttags['term'] : '';
	$description = isset($ttags['description']) ? $ttags['description'] : '';

	$form =  "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Target Tag Name</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=10 id=ttags_term term=ttags_term value=\"" . htmlentities($term, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=ttags_description name=ttags_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Target Tags functions


// Journal Config functions
function processJournalconfig($action, $id, $name, $description, $instructions, $min_words, $max_words, $exportable_items) {
	if ( $action == 'showoption' ) {
		$journal_configOpt = journal_configOption($id);
		echo $journal_configOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$journal_configDetail = showJournalconfig($action, $id);
		echo json_encode(array( 'form' => $journal_configDetail));
		return;
	}
	$show_id = updateJournalconfig($action, $id, $name, $description,  $instructions, $min_words, $max_words, $exportable_items);
	if ( $action == 'update' || $action == 'addnew' ) {
		$journal_configDetail = showJournalconfig('show', $show_id);
		$journal_configOpt = journal_configOption($show_id);
		$rt = array( 'form' => $journal_configDetail, 'Opt' => $journal_configOpt, 'msg' => 'Journal Config added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$journal_configOpt = journal_configOption(0);
		$rt = array('form' => '', 'Opt' => $journal_configOpt, 'msg' => 'Journal Config deleted');
		echo json_encode($rt);
		return;
	}
}


function journal_configOption($id) {
	$sql = "SELECT * FROM journal_config ORDER BY name"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$journal_configOption = "<select id=journal_config_id name=journal_config_id onChange='showMeta();'><option value=-1>Select Journal Config to Manage</option><option value=0>Add New Journal Config</option>";
	$i = 0;
	while ( $journal_config = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $journal_config['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$journal_configOption .= "<option value=\"" . $journal_config['id'] . "\" " . $selected . " >" . $journal_config['name'] . "</option>";
	}
	$journal_configOption .= "</SELECT>";
	return $journal_configOption;
}

function updateJournalconfig($action, $id, $name, $description, $instructions, $min_words, $max_words, $exportable_items) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO journal_config ( name, description, instructions, min_words, max_words, exportable_items)
							VALUES ( %s, %s, %s, %d, %d, %d)",
						SQLString($name, "text"), SQLString($description, "text"), SQLString($instructions, "text"), $min_words, $max_words, $exportable_items);
		$st = @mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE journal_config SET name = %s, description = %s, instructions = %s, min_words = %d, max_words = %d, exportable_items = %d
						 WHERE id = '%s'",
				SQLString($name, "text"), SQLString($description, "text"), SQLString($instructions, "text"), $min_words, $max_words, $exportable_items, $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// check product
		$sql = "SELECT name FROM product WHERE content_type = 1 AND product_config_id = " . $id;
		$st = mysql_query($sql);
		$msg = 'This Journal Config is used by products:';
		while ( $product = mysql_fetch_assoc($st)) {
			$msg .= " " . $product['name'];
		}
		if ( mysql_num_rows($st) >= 1 ) {
			$msg .= ". Please unassign the journal_config from product before delete.";
			return $msg;
		}
		$sql = sprintf("DELETE FROM journal_config WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showJournalconfig($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value=Update id='update' onClick='updateJournalconfig();' /></td>" 
					. "<td align=center><INPUT type=button class=btn value=Delete id='delete' onClick='updateJournalconfig();' /></td>"
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateJournalconfig();' /></td>" 
					. "<td align=center><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM journal_config WHERE id = '%s' ORDER BY name", $id);
		$st = @mysql_query($sql);
		$journal_config = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=journal_config_id name=journal_config_id value=\"" . $journal_config['id'] . "\" />";
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$name = isset($journal_config['name']) ? $journal_config['name'] : '';
	$description = isset($journal_config['description']) ? $journal_config['description'] : '';
	$instructions = isset($journal_config['instructions']) ? $journal_config['instructions'] : '';
	$min_words = isset($journal_config['min_words']) ? $journal_config['min_words'] : '';
	$max_words = isset($journal_config['max_words']) ? $journal_config['max_words'] : '';
	$check_c = $check_a = $check_b = '';
	switch ( $journal_config['exportable_items'] ) {
		case 0:
			$check_b = "checked";
			break;
		case 1:
			$check_c = "checked";
			break;
		case 2:
			$check_a = "checked";
			break; 
	}

	$form =  "<form> <table width='80%' border=0>
	  <tr>
		<td align=center>Journal Config Name</td>
		<td align=center>Description</td>
	  </tr>
	  <tr>
		<td align=center><INPUT type=text size=30 id=journal_config_name name=journal_config_name value=\"" . htmlentities($name, ENT_QUOTES, 'UTF-8') . "\" /></td>
		<td align=center><INPUT type=text size=40 id=journal_config_description name=journal_config_description value=\"" . htmlentities($description, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr>
		<td align=right valigh=middle>Instructions &nbsp;&nbsp;</td>
		<td align=center valign=middle><textarea id=instructions rows=15 cols=50>" . $instructions . " </textarea></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr>
	    <td align=center>Min words &nbsp;&nbsp;<INPUT type=text size=4 id=min_words value=\"" . $min_words . "\" /></td>
	    <td align=center>Max words &nbsp;&nbsp;<INPUT type=text size=4 id=max_words value=\"" . $max_words . "\" /></td>
	  </tr>
	  <tr>
	  	<td colspan=2><fieldset><legend>Exportable Items</legend>
	  		<div style='width:200px; float:left;'><INPUT type='radio' name='exportable_items' id='exportable_items' value='1' " . $check_c . " />Content</div>
	  		<div style='width:200px; float:left;'><INPUT type='radio' name='exportable_items' id='exportable_items' value='2' " . $check_a . " />Attachment</div>
	  		<div style='width:200px; float:left;'><INPUT type='radio' name='exportable_items' id='exportable_items' value='0' " . $check_b . " />Content and Attachment</div>
	  		</fieldset>
	  	</td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Journal Config functions

// Announcement functions
function processAnnouncement($action, $id, $title, $body, $expiration, $active) {
	if ( $action == 'showoption' ) {
		$announcementOpt = announcementOption($id);
		echo $announcementOpt;
		return;
	}
	if ( $action == 'show' || $action == 'add' ) {
		$announcementDetail = showAnnouncement($action, $id);
		echo json_encode(array( 'form' => $announcementDetail, 'request' => "showAnnouncement($action, $id)" ));
		return;
	}
	$show_id = updateAnnouncement($action, $id, $title, $body, $expiration, $active);
	if ( $action == 'update' || $action == 'addnew' ) {
		$announcementDetail = showAnnouncement('show', $show_id);
		$announcementOpt = announcementOption($show_id);
		$rt = array( 'form' => $announcementDetail, 'Opt' => $announcementOpt, 'msg' => 'Announcement added/updated');
		echo json_encode($rt);
		return;
	}
	if ( $action == 'delete' ) {
		if ( isset($show_id) ) {
			$rt['msg'] = $show_id;
			echo json_encode($rt);
			return;
		}
		$announcementOpt = announcementOption(0);
		$rt = array('form' => '', 'Opt' => $announcementOpt, 'msg' => 'Announcement deleted');
		echo json_encode($rt);
		return;
	}
}


function announcementOption($id) {
	$sql = "SELECT * FROM announcement ORDER BY expiration"; // do not allow update/delete super user
	$st = @mysql_query($sql);
	$announcementOption = "<select id=announcement_id name=announcement_id onChange='showMeta();'><option value=-1>Select Announcement to Manage</option><option value=0>Add New Announcement</option>";
	$i = 0;
	while ( $announcement = @mysql_fetch_assoc($st) ) {
		$selected = '';
		if ( isset( $id ) && $announcement['id'] == $id ) {
			$selected = " selected=selected ";
		}
		$announcementOption .= "<option value=\"" . $announcement['id'] . "\" " . $selected . " >" . $announcement['title'] . "</option>";
	}
	$announcementOption .= "</SELECT>";
	return $announcementOption;
}

function updateAnnouncement($action, $id, $title, $body, $expiration, $active) {
	if ( $action == 'addnew' ) {
		$sql = sprintf("INSERT INTO announcement ( title, body, expiration, active, created_by_user_id, created_time)
							VALUES ( %s, %s, %s, %d, %d, now())",
						SQLString($title, "text"), SQLString($body, "text"), SQLString($expiration, "text"), $active, $_SESSION['user_id']);
		$st = @mysql_query($sql);
		return mysql_insert_id();
	}
	if ( $action == 'update' ) {
		$sql = sprintf("UPDATE announcement SET title = %s, body = %s, expiration = '%s', active = %d
						 WHERE id = '%s'",
				SQLString($title, "text"), SQLString($body, "text"), $expiration, $active, $id);
		$st = mysql_query($sql);
		return $id;
	}
	if ( $action == 'delete' ) {
		// no check
		$sql = sprintf("DELETE FROM announcement WHERE id = '%s'", $id);
		$st = mysql_query($sql);
		return null;
	}
}

function showAnnouncement($action, $id) {

	$updateButton = "<table border=0 width='80%'><tr><td align=center width=33%><INPUT type=button class=btn value=Update id='update' onClick='updateAnnouncement();' /></td>" 
					. "<td align=center width=33%><INPUT type=button class=btn value=Delete id='delete' onClick='updateAnnouncement();' /></td>"
					. "<td align=center width=33%><INPUT type=reset class=btn value=Reset /></td></tr></table>";
	$addButton = "<table border=0 width='80%'><tr><td align=center width=33%><INPUT type=button class=btn value='Add New' id='addnew' onClick='updateAnnouncement();' /></td>" 
					. "<td align=center width=33%><INPUT type=reset class=btn value=Reset /></td></tr></table>";

	if ( $action == 'show' ) {
		$sql = sprintf("SELECT * FROM announcement WHERE id = '%s' order by expiration", $id);
		$st = @mysql_query($sql);
		$announcement = @mysql_fetch_assoc($st);
		$button = $updateButton;
		$id = "<INPUT type=hidden id=announcement_id title=announcement_id value=" . $announcement['id'] . " />";
		$sql = "SELECT * FROM user where id = " . $announcement['created_by_user_id'];
		$st = mysql_query($sql);
		$user = mysql_fetch_assoc($st);
	}

	if ( $action == 'add' ) {
		$button = $addButton;
		$id = '';
	}

	$title = isset($announcement['title']) ? $announcement['title'] : '';
	$body = isset($announcement['body']) ? $announcement['body'] : '';
	$expiration = isset($announcement['expiration']) ? $announcement['expiration'] : '';
	$created_time = isset($announcement['created_time']) ? $announcement['created_time'] : '';
	$created_by = isset($user['username']) ? $user['username'] : '';
	$time = preg_split('/ /', $expiration);
	$expiration = $time[0];
	$checked = isset($announcement['active']) && $announcement['active'] == 1 ? " checked=checked " : '';

	$form =  "<form> <table width='80%' border=0>
	  <tr>
		<td align=left width=150px></td>
		<td align=right><div style='font-size:11px;' >Created by " . $created_by . " on " . $created_time . "</div></td>
	  </tr>
	  <tr>
		<td align=right width=150px>Title &nbsp;</td>
		<td align=left><INPUT type=text size=20 id=announcement_title title=announcement_title value=\"" . htmlentities($title, ENT_QUOTES, 'UTF-8') . "\" /></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr>
		<td align=right valigh=middle width=150px >Announcement Body &nbsp;&nbsp;</td>
		<td align=left valign=middle><textarea id=body rows=15 cols=50>" . $body . " </textarea></td>
	  </tr>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr>
		<table border=0 width=100%>
		  <tr>
			<td align=center style='width:350px; margin-right:20px;'>Expiration (YYYY-MM-DD)&nbsp;&nbsp;<INPUT type=text size=10 id=expiration value=\"" . $expiration . "\" /></td>
			<td align=left style='width:200px; margin-left:20px;'>Active &nbsp;&nbsp;<INPUT type=checkbox id=active value=1 " . $checked . " /></td>
		  </tr>
		</table>
	  <tr>
		<td colspan=2>&nbsp</td>
	  </tr>
	  <tr><td colspan=2 align=center>" . $button . $id .  "</td>
	  </tr>
	</table></form>";
	return $form;

}
// END Announcement functions

session_start();

if ( isset($_GET['action']) && $_GET['action'] == 'show_choices' ) {
	$sql = sprintf("SELECT * FROM reference_choice WHERE reference_id = %d ORDER BY weight ", $_GET['reference_id']);
	$st = mysql_query($sql);
	$choice_list = '';
	while ( $choice = mysql_fetch_assoc($st) ) {
		$choice_list .= $choice['label'] . "\r\n";
	}
	echo $choice_list;
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_notification_item' ) {
	$sql = sprintf("SELECT * FROM notification_item where language_id = %d AND notification_type_id = %d",
				$_GET['language_id'], $_GET['notification_type_id']);
	$st = mysql_query($sql);
	$item = mysql_fetch_assoc($st);
	if ( mysql_num_rows($st) != 1 ) {
		$item = array( 'body_text' => '', 'subject_text' => '');
	}
	echo json_encode($item);
	exit;
}
$name = isset($_GET['name']) ? html_entity_decode($_GET['name'], ENT_NOQUOTES, 'UTF-8') : '';
$description = isset($_GET['description']) ? html_entity_decode($_GET['description'], ENT_NOQUOTES, 'UTF-8') : '';
$id = isset($_GET['id']) && $_GET['id'] > 0 ? $_GET['id'] : '';
$choice_type = isset($_GET['choice_type']) ? $_GET['choice_type'] : '';

switch ( $_GET['meta'] ) {
	case "organization_id":
		$address = isset($_GET['address']) ? $_GET['address'] : '';
		$url = isset($_GET['url']) ? $_GET['url'] : '';
		$admin_user_id = isset($_GET['admin_user_id']) ? $_GET['admin_user_id'] : 0;
		processOrganization($_GET['action'], $id, $name, $address, $url, $admin_user_id);
		break;
	case "role_id":
		processRole($_GET['action'], $id, $name, $description);
		break;
	case 'language_id':
		$status = isset($_GET['status']) ? $_GET['status'] : 1;
		processLanguage($_GET['action'], $id, $name, $description, $status);
		break;
	case "reference_id":
		$rec = isset($_GET['choices']) ? html_entity_decode($_GET['choices'], ENT_NOQUOTES, 'UTF-8') : '';
		$choices = array();
		$choices = preg_split('/\n/', $rec, null, PREG_SPLIT_NO_EMPTY);
		processReference($_GET['action'], $id, $name, $description, $choice_type, $choices);
		break;
	case "study_period_id":
		processStudyPeriod($_GET['action'], $id, $name, $description);
		break;
	case "view_matrix_id":
		processViewMatrix($_GET['action'], $id, $name, $description);
		break;
	case "access_matrix_id":
		processAccessMatrix($_GET['action'], $id, $name, $description);
		break;
	case "notification_type_id":
		$subject_text = isset($_GET['subject_text']) ? html_entity_decode($_GET['subject_text'], ENT_NOQUOTES, 'UTF-8') : '';
		$body_text = isset($_GET['body_text']) ? html_entity_decode($_GET['body_text'], ENT_NOQUOTES, 'UTF-8') : '';
		$language_id = isset($_GET['language_id']) ? $_GET['language_id'] : '';
		processNotificationType($_GET['action'], $id, $name, $description, $language_id, $subject_text, $body_text );
		break;
	case "target_id":
		$target_type = isset($_GET['target_type']) ? $_GET['target_type'] : '';
		$short_name = isset($_GET['short_name']) ? $_GET['short_name'] : '';
		$guid = isset($_GET['guid']) ? $_GET['guid'] : '';
		$add_to_target_tag = isset($_GET['add_to_target_tag']) ? $_GET['add_to_target_tag'] : null;
		$remove_from_target_tag = isset($_GET['remove_from_target_tag']) ? $_GET['remove_from_target_tag'] : null;
		processTarget($_GET['action'], $id, $name, $target_type, $description, $short_name, $guid, $add_to_target_tag, $remove_from_target_tag);
		break;
	case "itags_id":
		$term = isset($_GET['term']) ? html_entity_decode($_GET['term'], ENT_NOQUOTES, 'UTF-8') : '';
		processItags($_GET['action'], $id, $term, $description);
		break;
	case "ctags_id":
		$term = isset($_GET['term']) ? html_entity_decode($_GET['term'], ENT_NOQUOTES, 'UTF-8') : '';
		processCtags($_GET['action'], $id, $term, $description);
		break;
	case "ttags_id":
		$term = isset($_GET['term']) ? html_entity_decode($_GET['term'], ENT_NOQUOTES, 'UTF-8') : '';
		processTtags($_GET['action'], $id, $term, $description);
		break;
	case "journal_config_id":
		$instructions = isset($_GET['instructions']) ? html_entity_decode($_GET['instructions'], ENT_NOQUOTES, 'UTF-8') : '';
		$min_words = isset($_GET['min_words']) ? $_GET['min_words'] : '';
		$max_words = isset($_GET['max_words']) ? $_GET['max_words'] : '';
		$exportable_items = isset($_GET['exportable_items']) ? $_GET['exportable_items'] : 0;
		processJournalconfig($_GET['action'], $id, $name, $description, $instructions, $min_words, $max_words, $exportable_items);
		break;
	case "announcement_id":
		$body = isset($_GET['body']) ? html_entity_decode($_GET['body'], ENT_NOQUOTES, 'UTF-8') : '';
		$title = isset($_GET['title']) ? html_entity_decode($_GET['title'], ENT_NOQUOTES, 'UTF-8') : '';
		$expiration = isset($_GET['expiration']) ? $_GET['expiration'] : '';
		$active = isset($_GET['active']) ? $_GET['active'] : 0;
		processAnnouncement($_GET['action'], $id, $title, $body, $expiration, $active);
		break;

}

?>
