<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<? 
session_start();
//if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authuser'] )) exit;
if ( !isset($_GET['project_id']) ) exit;

require_once("./include/config.inc");

if ( isset($_GET['func']) ) {
	$contacts = array();
	$contacts = explode(':', $_GET['user_id']);
	unset($contacts[0]);
	if ( $_GET['func'] == 'addcontacts_to_project' ) {
		$i = 0;
		foreach ( $contacts as $inx => $user ) {
			$sql = sprintf("INSERT INTO project_contact ( user_id, project_id ) 
								VALUES ('%s', '%s')",
							$user, $_GET['project_id']);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
		//indaba_alert( $i . " contacts has been added to project");
	}
	if ( $_GET['func'] == 'remove_contacts' ) {
		$i = 0;
		foreach ( $contacts as $inx => $user ) {
			$sql = sprintf("DELETE FROM project_contact WHERE project_id = '%s' AND user_id = '%s'", $_GET['project_id'], $user);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
		//indaba_alert( $i . " contacts has been removed from project");
	}

}

$sql = sprintf("SELECT * FROM project where id = '%s'", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$project = @mysql_fetch_assoc($st);
if ( empty($project['code_name']) ) {
	indaba_alert("Project not found");
	header("Location: projects.php");
}

$sql = sprintf("SELECT u.* FROM user u, project_membership pm
				where u.id = pm.user_id
						AND pm.project_id = %d
						AND u.id NOT IN ( SELECT user_id FROM project_contact WHERE project_id = '%s') 
						AND u.status = 1
					ORDER BY last_name ", $_GET['project_id'], $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$avacontactsOpt = "<SELECT name=user_id class=sel_list size=25 multiple>";
while ( $user = @mysql_fetch_assoc($st) ) {
	$avacontactsOpt .= "<option value=" . $user['id'] . ">" . $user['last_name'] . ", " . $user['first_name'] . "</option>";
}
$avacontactsOpt .= "</SELECT>";

$sql = sprintf("SELECT a.user_id as user_id, b.last_name as last_name, b.first_name as first_name FROM project_contact a, user b 	
					WHERE a.user_id = b.id 
						and a.project_id = '%s' 
						AND b.status = 1
					ORDER BY last_name", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$projectcontactsOpt = "<SELECT name=project_user_id class=sel_list size=25 multiple>";
while ( $user = @mysql_fetch_assoc($st) ) {
	$projectcontactsOpt .= "<option name=a value=" . $user['user_id'] . ">" . $user['last_name'] . ", " . $user['first_name'] . "</option>";
}
$projectcontactsOpt .= "</SELECT>";


?>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Managing Contacts for Project <? echo $project['code_name']; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script language=javascript>
function buttonActions() {
    var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var button = target.id;
	var selected_contacts = "-1";
	if ( button == "adduser" ) {
		if ( document.contacts.user_id.selectedIndex == -1 ) {
			alert("Please select contacts you want to assign to project then click 'Assign Contacts to Project' button");
			return;
		}
		for ( var i = 0; i < document.contacts.user_id.length; i ++ ) {
			if ( document.contacts.user_id[i].selected ) 
				selected_contacts = selected_contacts + ":" + document.contacts.user_id[i].value;
		}
		window.location = "manage_contacts.php?func=addcontacts_to_project&user_id=" + selected_contacts + "&project_id=" + document.contacts.project_id.value;
	}
	if ( button == "remove_contacts" ) {
		if ( document.contacts.project_user_id.selectedIndex == -1 ) {
			alert("Please select contacts you want to remove from project then click 'Remove Contacts' button");
			return;
		}
		for ( var i = 0; i < document.contacts.project_user_id.length; i ++ ) {
			if ( document.contacts.project_user_id[i].selected ) 
				selected_contacts = selected_contacts + ":" + document.contacts.project_user_id[i].value;
		}
		window.location = "manage_contacts.php?func=remove_contacts&user_id=" + selected_contacts + "&project_id=" + document.contacts.project_id.value;
	}
	if ( button == "close_window" ) {
		self.close();
	}
	//return false;
}
</script>

</head>
<body>

<form name="contacts" >
	<INPUT type=hidden name=project_id value="<? echo $project['id']; ?>" />
    <table width="600" border="0">
        <tr>
            <td colspan="3" align="left" valign="top"><p>Please select contacts from the list and use the buttons to move contacts in/out project.</p>
			  <p>When user is removed from the project, all users assigned with the user will be REMOVED from the project as well as the teams.</p>
			</td>
        </tr>
        <tr>
            <td align="center" valign="top">Available Contacts </td>
            <td align="center" valign="middle">Action</td>
            <td align="center" valign="top">Contacts in Project </td>
        </tr>
        <tr>
            <td align="center" valign="top"><? echo $avacontactsOpt; ?> </td>
            <td align="center" valign="middle"><table width="100%"  border="0">
                <tr>
                    <td align="center"><input type="button" id="adduser" name="adduser" class=btn value="Assign Contacts to Project >>" onClick="buttonActions();"></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="center"><input type="button" id="remove_contacts" name="remove_contacts" class=btn value="<< Remove Contacts" onClick="buttonActions();"></td>
                </tr>
            </table></td>
            <td align="center" valign="top"><? echo $projectcontactsOpt; ?>
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
