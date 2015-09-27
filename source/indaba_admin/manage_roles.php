<?php
session_start();
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php 
//if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authuser'] )) exit;
if ( !isset($_GET['project_id']) ) exit;

require_once("./include/config.inc");

if ( isset($_GET['func']) ) {
	$roles = array();
	$roles = explode(':', $_GET['role_id']);
	unset($roles[0]);
	if ( $_GET['func'] == 'addroles_to_project' ) {
		$i = 0;
		foreach ( $roles as $inx => $role ) {
			$sql = sprintf("INSERT INTO project_roles ( role_id, project_id ) 
								VALUES ('%s', '%s')",
							$role, $_GET['project_id']);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
		//indaba_alert( $i . " roles has been added to project");
	}
	if ( $_GET['func'] == 'remove_roles' ) {
		$i = 0;
		$msg = '';
		foreach ( $roles as $inx => $role ) {
			// check project_membership first
			$chk_sql = sprintf("SELECT r.name, pm.* FROM role r, project_membership pm
									WHERE r.id = pm.role_id 
									AND pm.project_id = '%s' AND pm.role_id = '%s'", 
							$_GET['project_id'], $role);
			$st = mysql_query($chk_sql);
			$pr = mysql_fetch_assoc($st) ; 
			if ( $pr ) {
				// role has users assigned, cannot delete
				$msg .= " [" . $pr['name'] . "]";
			} else {
				$sql = sprintf("DELETE FROM project_roles WHERE project_id = '%s' AND role_id = '%s'", $_GET['project_id'], $role);
				$st = @mysql_query($sql, $indaba_dbh);
				if ( mysql_affected_rows() == 1 ) $i ++;
			}
		}
		//indaba_alert( $i . " roles has been removed from project");
	}

}

$sql = sprintf("SELECT * FROM project where id = '%s'", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$project = @mysql_fetch_assoc($st);
if ( empty($project['code_name']) ) {
	indaba_alert("Project not found");
	header("Location: projects.php");
}

$sql = sprintf("SELECT * FROM role where id NOT IN ( SELECT role_id FROM project_roles WHERE project_id = '%s')", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$avarolesOpt = "<SELECT name=role_id class=sel_list size=25 multiple>";
while ( $role = @mysql_fetch_assoc($st) ) {
	$avarolesOpt .= "<option name=a value=" . $role['id'] . ">" . $role['name'] . "</option>";
}
$avarolesOpt .= "</SELECT>";

$sql = sprintf("SELECT a.role_id as role_id, b.name as name FROM project_roles a, role b WHERE a.role_id = b.id and a.project_id = '%s'", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$projectrolesOpt = "<SELECT name=project_role_id class=sel_list size=25 multiple>";
while ( $role = @mysql_fetch_assoc($st) ) {
	$projectrolesOpt .= "<option name=a value=" . $role['role_id'] . ">" . $role['name'] . "</option>";
}
$projectrolesOpt .= "</SELECT>";


?>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Managing Roles for Project <? echo $project['code_name']; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script language=javascript>
function buttonActions() {
    var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var button = target.id;
	var selected_roles = "-1";
	if ( button == "addrole" ) {
		if ( document.roles.role_id.selectedIndex == -1 ) {
			alert("Please select roles you want to assign to project then click 'Assign Roles to Project' button");
			return;
		}
		for ( var i = 0; i < document.roles.role_id.length; i ++ ) {
			if ( document.roles.role_id[i].selected ) 
				selected_roles = selected_roles + ":" + document.roles.role_id[i].value;
		}
		window.location = "manage_roles.php?func=addroles_to_project&role_id=" + selected_roles + "&project_id=" + document.roles.project_id.value;
	}
	if ( button == "remove_roles" ) {
		if ( document.roles.project_role_id.selectedIndex == -1 ) {
			alert("Please select roles you want to remove from project then click 'Remove Roles' button");
			return;
		}
		for ( var i = 0; i < document.roles.project_role_id.length; i ++ ) {
			if ( document.roles.project_role_id[i].selected ) 
				selected_roles = selected_roles + ":" + document.roles.project_role_id[i].value;
		}
		window.location = "manage_roles.php?func=remove_roles&role_id=" + selected_roles + "&project_id=" + document.roles.project_id.value;
	}
	if ( button == "close_window" ) {
		self.close();
	}
	//return false;
}
</script>

</head>
<body>

<form name="roles" >
	<INPUT type=hidden name=project_id value="<? echo $project['id']; ?>" />
    <table width="600" border="0">
        <tr>
            <td colspan="3" align="left" valign="top"><p>Please select roles from the list and use the buttons to move roles in/out project.</p>
			  <p>When role is removed from the project, all users assigned with the role will be REMOVED from the project as well as the teams.</p>
				<?php
					if ( isset($msg) && $msg != '' ) {
						echo "<p><font style='font-size:12px; color:red;'>Role " . $msg . " already have users assigned to, please unassign the users before remove the roles. </font></p>";
					}
				?>
			</td>
        </tr>
        <tr>
            <td align="center" valign="top">Available Roles </td>
            <td align="center" valign="middle">Action</td>
            <td align="center" valign="top">Roles in Project </td>
        </tr>
        <tr>
            <td align="center" valign="top"><? echo $avarolesOpt; ?> </td>
            <td align="center" valign="middle"><table width="100%"  border="0">
                <tr>
                    <td align="center"><input type="button" id="addrole" name="addrole" class=btn value="Assign Roles to Project >>" onClick="buttonActions();"></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="center"><input type="button" id="remove_roles" name="remove_roles" class=btn value="<< Remove Roles" onClick="buttonActions();"></td>
                </tr>
            </table></td>
            <td align="center" valign="top"><? echo $projectrolesOpt; ?>
            </td>
        </tr>
        <tr>
            <td align="center" valign="top">&nbsp;</td>
            <td align="center" valign="middle"><input type="button" id="close_window" name="close_window" class=btn value="Done and Close Window" onClick="buttonActions();"></td>
            <td align="center" valign="top">&nbsp;</td>
        </tr>
    </table>
</form>
</body>
</html>
