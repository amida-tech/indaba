<?php 
session_start(); ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php 
//if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authuser'] )) exit;
if ( !isset($_GET['project_id']) ) exit;

require_once("./include/config.inc");

if ( isset($_GET['func']) ) {
	$users = array();
	$users = explode(':', $_GET['user_ids']);
	unset($users[0]);
	if ( $_GET['func'] == 'addusers' ) {
		$i = 0;
		foreach ( $users as $ind => $user_id ) {
			$sql = sprintf("INSERT INTO project_membership (user_id, role_id, project_id) 
							VALUES ('%s', '%s', '%s')",
						$user_id, $_GET['role_id'], $_GET['project_id']);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
	}
	if ( $_GET['func'] == 'removeusers' ) {
		$i = 0;
		foreach ( $users as $ind => $user_id ) {
			$sql = sprintf("DELETE FROM project_membership WHERE project_id = '%s' and user_id = '%s'",
						$_GET['project_id'], $user_id);
			$st = @mysql_query($sql, $indaba_dbh);
			$sql = sprintf("DELETE team_user FROM team, team_user 
								WHERE 
									team.id = team_user.team_id
									AND team.project_id = %d
									AND team_user.user_id = %d",
						$_GET['project_id'], $user_id);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
	}

}

$sql = sprintf("SELECT * FROM project where id = '%s'", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$project = @mysql_fetch_assoc($st);
if ( empty($project['code_name']) ) {
	indaba_alert("Project not found");
	header( "Location: projects.php");
}

if ( preg_match('/^publisher$/i', $project['code_name'] ) ) {
	$sql = sprintf("SELECT u.* FROM user u WHERE u.status = 1");
} else {
	$sql = sprintf("SELECT u.* FROM user u, project p 
				WHERE u.status = 1 
				AND p.id = %d
				AND u.id NOT IN (SELECT user_id FROM project_membership WHERE project_id = '%s') ORDER BY last_name ", 
					$_GET['project_id'], $_GET['project_id']);
}
$alluser_st = @mysql_query($sql, $indaba_dbh);
$allusersOpt = "<SELECT id=alluser_user_id name=alluser_user_id class=sel_list size=25 multiple>";
while ( $user = @mysql_fetch_assoc($alluser_st) ) {
	$allusersOpt .= "<option name=a value=" . $user['id'] . ">" . $user['username'] .
					" - " . $user['last_name'] . ", " . $user['first_name'] . "</option>";
}
$allusersOpt .= "</SELECT>";

$sql = sprintf("SELECT a.user_id as user_id, b.first_name, b.last_name, b.username as username, c.name as role FROM project_membership a, user b, role c WHERE a.project_id = '%s' AND a.user_id = b.id AND a.role_id = c.id AND b.status = 1 ORDER BY last_name", $_GET['project_id']);
$projuser_st = @mysql_query($sql, $indaba_dbh);
$projusersOpt = "<SELECT name=project_user_id class=sel_list size=25 multiple>";
while ( $user = fetch_html_entities($projuser_st) ) {
	$projusersOpt .= "<option name=a value=" . $user['user_id'] . ">" . $user['username'] .
					" - " . $user['role'] . " - " . $user['last_name'] . ", " . $user['first_name'] . "</option>";
}
$projusersOpt .= "</SELECT>";

$sql = sprintf("SELECT a.role_id as role_id, b.name as name, b.description as description FROM project_roles a, role b WHERE project_id = '%s' AND a.role_id = b.id", $_GET['project_id']);
$roleOpt = "<SELECT name=role_id><option name=a value=''>Select role for users</option>";
$role_st = @mysql_query($sql, $indaba_dbh);
while ( $role = @mysql_fetch_assoc($role_st) ) {
	$roleOpt .= "<option name=a value='" . $role['role_id'] . "'>" . $role['name'] . "</option>";
}
$roleOpt .= "</SELECT>";


?>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Managing Users for Project <? echo $project['code_name']; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script language=javascript>
function buttonActions() {
	var evt = window.event || arguments.callee.caller.arguments[0];
	var target = evt.target || evt.srcElement;
	var button = target.id;
	var selected_users = "-1";
	if ( button == "adduser" ) {
		if ( document.manage_users.alluser_user_id.selectedIndex == -1 || document.manage_users.role_id.selectedIndex < 1 ) {
			alert("Please select users you want to add to the project, the role you want to assign to the selected users and then click 'Add User to Project' button");
			return;
		}
		for ( var i = 0; i < document.manage_users.alluser_user_id.length; i++ ) {
			if ( document.manage_users.alluser_user_id[i].selected ) {
				selected_users = selected_users + ":" + document.manage_users.alluser_user_id[i].value;
			}
		}
		window.location = "manage_users.php?func=addusers&user_ids=" + selected_users + "&project_id=" 
			+ document.manage_users.project_id.value 
			+ "&role_id=" + document.manage_users.role_id.value;
	}
	if ( button == "removeuser" ) {
		if ( document.manage_users.project_user_id.selectedIndex == -1 ) {
			alert("Please select users you want to remove from project then click 'Remove User' button");
			return;
		}
		for ( var i = 0; i < document.manage_users.project_user_id.length; i ++ ) {
			if ( document.manage_users.project_user_id[i].selected )
				selected_users = selected_users + ":" + document.manage_users.project_user_id[i].value;
		}
		var go = confirm("Are you sure you want to remove the users from this project? The selected users will be removed from the assigned tasks as well!");
		if ( go == false ) {
			return;
		}
		window.location = "manage_users.php?func=removeusers&user_ids=" + selected_users + "&project_id="
			+ document.manage_users.project_id.value
			+ "&role_id=" + document.manage_users.role_id.value;
	}
	if ( button == "close_window" ) self.close();
}

function filter_user() {
	var name_like = $("#filter").val();
	if ( name_like.length < 3 ) {
		return;
	}
	$("#alluser_user_id option").each( function() {
		if ( $(this).text().search(new RegExp(name_like, 'i')) < 0 ) {
			$(this).attr("disabled", "disabled");
		} else {
			$(this).attr("disabled", false);
		}
	});
}
		

</script>
</head>
<body>

<form name="manage_users">
    <table width="600" border="0">
        <tr>
            <td colspan="3" align="left" valign="top"><p>Please select users from the list and use the buttons to move user in/out project.</p>
			  <p>When user is first assigned to a project, there will be no roles associated with the user. Please use manage roles botton to assign/unassign roles to user.</p>
			  <p>When user is removed from the project, the user roles for this project will be removed as well as team membership.</p>
			</td>
        </tr>
        <tr>
            <td align="center" valign="top">Available Users </td>
            <td align="center" valign="middle"></td>
            <td align="center" valign="top">Users in Project </td>
        </tr>
        <tr>
            <td align="center" valign="top"><? echo $allusersOpt; ?>
			</td>
            <td align="center" valign="top"><table width="100%"  border="0">
				<tr>
				    <td align="center"><? echo $roleOpt; ?></td>
				</tr>
				<tr>
				    <td align="center">&nbsp</td>
				</tr>
				<tr>
				    <td align="center">&nbsp</td>
				</tr>
                <tr>
                    <td align="center"><input type="button" id="adduser" class=btn value="Add User to Project >>" onClick="buttonActions();"></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="center"><input type="button" id="removeuser" class=btn value="<< Remove User" onClick="buttonActions();"></td>
                </tr>
            </table></td>
            <td align="center" valign="top"><? echo $projusersOpt; ?>
            </td>
        </tr>
        <tr>
            <td align="center" valign="top"><input id=filter size=20 onKeyUp="filter_user();"/></td>
            <td align="center" valign="middle"><input type="button" id="close_window" class=btn value="Done and Close Window" onClick="buttonActions();"></td>
            <td align="center" valign="top">&nbsp;</td>
        </tr>
    </table>
	<INPUT type=hidden name=project_id value='<? echo $project['id']; ?>' />
</form>
</body>
</html>
