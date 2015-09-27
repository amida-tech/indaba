<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<? 
session_start();
//if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authuser'] )) exit;
if ( !isset($_GET['project_id']) ) exit;

require_once("./include/config.inc");

if ( isset($_GET['func']) ) {
	$users = array();
	if ( isset($_GET['user_ids']) ) {
		$users = explode(':', $_GET['user_ids']);
		unset($users[0]);
	}
	if ( $_GET['func'] == 'addusers' ) {
		foreach ( $users as $ind => $user_id ) {
			$sql = sprintf("INSERT INTO team_user (user_id, team_id) 
							VALUES ('%s', '%s' )",
						$user_id, $_GET['team_id']);
			$st = @mysql_query($sql, $indaba_dbh);
		}
		header("Location: manage_teams.php?project_id=" .  $_GET['project_id'] . "&team_id=" . $_GET['team_id']);
	}
	if ( $_GET['func'] == 'removeusers' ) {
		$i = 0;
		foreach ( $users as $ind => $user_id ) {
			$sql = sprintf("DELETE FROM team_user WHERE team_id = '%s' and user_id = '%s'",
						$_GET['team_id'], $user_id);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
		header("Location: manage_teams.php?project_id=" .  $_GET['project_id'] . "&team_id=" . $_GET['team_id']);
	}
	if ( $_GET['func'] == 'removeteam' ) {
		$sql = sprintf("DELETE FROM team_user WHERE team_id = '%s'", $_GET['team_id']);
		$i =  mysql_affected_rows();
		$sql = sprintf("DELETE FROM team WHERE id = '%s' and project_id = '%s'", $_GET['team_id'], $_GET['project_id']);
		$j =  mysql_affected_rows();
	}
	if ( $_GET['func'] == 'addteam' ) {
		$sql = sprintf("INSERT INTO team ( project_id, team_name, description ) VALUES ( '%s', '%s', '%s' )",
					$_GET['project_id'], $_GET['added_team'], $_GET['added_desc']);
		$st = @mysql_query( $sql, $indaba_dbh);
		if ( mysql_affected_rows() == 1 ) { 
			//header("Location: manage_teams.php?project_id=" .  $_GET['project_id'] . "&team_name=" . $_GET['added_team']);
		} else {
			indaba_alert("Error adding team: " . mysql_error());
			header("Location: manage_teams.php?project_id=" .  $_GET['project_id'] );
		}
	}

}

// Build team list
$sql = "SELECT * FROM team WHERE project_id = '" . $_GET['project_id'] . "'";
$st = @mysql_query( $sql, $indaba_dbh );
$teamOpt = "<SELECT name=team_id id=team_id onChange='getTeam();' ><option name=a value=''>Select a team to manage</option>";
while ( $teams = mysql_fetch_assoc($st) ) {
	$selected = "";
	if ( isset($_GET['team_id'] ) ) {
		$selected =  $_GET['team_id'] == $teams['id'] ? "selected=selected" : "" ;
	}
	$teamOpt .= "<option name=a value='" . $teams['id'] . "' " . $selected . ">" . $teams['team_name'] . "</option>";
}
$teamOpt .= "</SELECT>";

$sql = sprintf("SELECT * FROM project where id = '%s'", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$project = @mysql_fetch_assoc($st);
if ( empty($project['code_name']) ) {
	//indaba_redirect("Project not found", "projects.php");
	echo "Project Not Found";
	header("Location: manage_teams.php?project_id=" .  $_GET['project_id'] );
}

$allusersOpt = "<SELECT name=alluser_user_id class=sel_list size=25 multiple>";
$teamusersOpt = "<SELECT name=project_user_id class=sel_list size=25 multiple>";
if ( isset($_GET['team_id']) ) {
	// build available user list
	$sql = sprintf("SELECT p.user_id as user_id, u.username as username, u.first_name as first_name, u.last_name as last_name
					 FROM project_membership p, user u
					 WHERE p.project_id = '%s' 
					    AND p.user_id = u.id
						AND u.status = 1
						AND p.user_id NOT IN 
							(SELECT user_id FROM team_user WHERE team_id = %d) 
					 ORDER BY last_name",
					$_GET['project_id'], $_GET['team_id']);
	$alluser_st = @mysql_query($sql, $indaba_dbh);
	//echo $sql;
	while ( $user = @mysql_fetch_assoc($alluser_st) ) {
		$allusersOpt .= "<option name=a value=" . $user['user_id'] . ">" . $user['last_name'] . ", " . $user['first_name'] . "</option>";
	}
	if ( mysql_num_rows($alluser_st) == 0 ) {
		$noavail = 1;
	}

    // build team user list for team
	$sql = sprintf("SELECT tu.user_id as user_id, u.username as username, u.first_name as first_name, u.last_name as last_name 
					FROM team_user tu, user u, team t
					WHERE tu.team_id = '%s' 
						AND tu.user_id = u.id 
						AND u.status = 1
						AND tu.team_id = t.id
						AND t.project_id = '%s'
					ORDER BY u.last_name ",
					 $_GET['team_id'], $_GET['project_id']);
	$teamuser_st = @mysql_query($sql, $indaba_dbh);
	//echo $sql;
	while ( $user = @mysql_fetch_assoc($teamuser_st) ) {
		$teamusersOpt .= "<option name=a value=" . $user['user_id'] . ">" . $user['last_name'] . ", " . $user['first_name'] . "</option>";
	}
	if ( mysql_num_rows($teamuser_st) == 0 ) {
		if ( isset($noavail) ) 
			$nousers = "<p>No users have been added to the project yet, use 'Assign Users' button on PROJECTS tab to assign users to a project.</p>";
	}
}
$allusersOpt .= "</SELECT>";
$teamusersOpt .= "</SELECT>";

?>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Managing Teams for Project <? echo $project['code_name']; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script language=javascript>
function buttonActions() {
	var evt = window.event || arguments.callee.caller.arguments[0];
	var target = evt.target || evt.srcElement;
	var button = target.id;
	var selected_users = "-1";
	if ( button == "adduser" ) {
		if ( document.manage_teams.alluser_user_id.selectedIndex == -1 || document.manage_teams.team_id.selectedIndex < 1 ) {
			alert("Please select users you want to add to the team and then click 'Add User to Team' button");
			return;
		}
		for ( var i = 0; i < document.manage_teams.alluser_user_id.length; i++ ) {
			if ( document.manage_teams.alluser_user_id[i].selected ) {
				selected_users = selected_users + ":" + document.manage_teams.alluser_user_id[i].value;
			}
		}
		window.location = "manage_teams.php?func=addusers&user_ids=" + selected_users + "&project_id=" 
			+ document.manage_teams.project_id.value 
			+ "&team_id=" + document.manage_teams.team_id.value;
	}
	if ( button == "removeuser" ) {
		if ( document.manage_teams.project_user_id.selectedIndex == -1 ) {
			alert("Please select users you want to remove from project then click 'Remove User' button");
			return;
		}
		for ( var i = 0; i < document.manage_teams.project_user_id.length; i ++ ) {
			if ( document.manage_teams.project_user_id[i].selected )
				selected_users = selected_users + ":" + document.manage_teams.project_user_id[i].value;
		}
		window.location = "manage_teams.php?func=removeusers&user_ids=" + selected_users + "&project_id="
			+ document.manage_teams.project_id.value
			+ "&team_id=" + document.manage_teams.team_id.value;
	}
	if ( button == "addteam" ) {
		if ( document.manage_teams.added_team.value == "" ) {
			alert("Please enter a team name before clicking the 'Add Team' button.");
			return;
		}
		if ( document.manage_teams.added_desc.value == "" ) {
			alert("Please enter a added_desc for the team before clicking the 'Add Team' button.");
			return;
		}
		window.location = "manage_teams.php?func=addteam&project_id=" 
			+ document.manage_teams.project_id.value
			+ "&added_team=" 
			+ document.manage_teams.added_team.value
			+ "&added_desc="
			+ document.manage_teams.added_desc.value;
	}
	if ( button == "close_window" ) self.close();
}

function getTeam() {
	var i = document.manage_teams.team_id.selectedIndex;
	if ( i > 0 ) {
		window.location = "manage_teams.php?project_id="
			+ document.manage_teams.project_id.value
			+ "&team_id="
			+ document.manage_teams.team_id[i].value;
	}
	return;
}
		

</script>
</head>
<body>

<form name="manage_teams">
    <table width="600" border="0">
        <tr>
            <td colspan="3" align="left" valign="top">
				<? 
					echo isset($nousers) ? 
						$nousers : 
						"<p>Please select team from the list, select users from the list and use the buttons to move user in/out selected team.</p>";
				?>
			</td>
        </tr>
		<tr><td colspan="3" valign="top"><fieldset><legend align=left>Add A New Team</legend><table border=0 width="100%">
			  <tr>
			    <td>Team Name: <INPUT type=text name=added_team size=10 /></td>
				<td>Description: <INPUT type=text name=added_desc size=30 /></td>
				<td> <INPUT type=button class=btn id=addteam name=addteam value="Add a Team" onClick="buttonActions();" /></td>
			  </tr>
			</table></fieldset></td>
		</tr>
        <tr>
            <td align="center" valign="top">Available Users </td>
            <td align="center" valign="middle">Managed Team</td>
            <td align="center" valign="top">Users in Team <? echo isset($_GET['team_name']) ? $_GET['team_name'] : ''; ?></td>
        </tr>
        <tr>
            <td align="center" valign="top"><? echo $allusersOpt; ?>
			</td>
            <td align="center" valign="top"><table width="100%"  border="0">
				<tr>
				    <td align="center"><? echo $teamOpt; ?></td>
				</tr>
				<tr>
				    <td align="center">&nbsp</td>
				</tr>
				<tr>
				    <td align="center">&nbsp</td>
				</tr>
                <tr>
                    <td align="center"><input type="button" id="adduser" class=btn value="Add User to Team >>" onClick="buttonActions();"></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="center"><input type="button" id="removeuser" class=btn value="<< Remove User" onClick="buttonActions();"></td>
                </tr>
            </table></td>
            <td align="center" valign="top"><? echo $teamusersOpt; ?>
            </td>
        </tr>
        <tr>
            <td align="center" valign="top">&nbsp;</td>
            <td align="center" valign="middle"><input type="button" id="close_window" class=btn value="Done and Close Window" onClick="buttonActions();"></td>
            <td align="center" valign="top">&nbsp;</td>
        </tr>
    </table>
	<INPUT type=hidden name=project_id value='<? echo $project['id']; ?>' />
</form>
</body>
</html>
