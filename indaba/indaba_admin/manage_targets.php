<?php
session_start();
//if ( ! isset($_SERVER['HTTP_REFERER']) || ! isset( $_SESSION['authuser'] )) exit;
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php 
if ( !isset($_GET['project_id']) ) exit;

require_once("./include/config.inc");

if ( isset($_GET['func']) ) {
	$targets = array();
	$targets = explode(':', $_GET['target_id']);
	unset($targets[0]);
	if ( $_GET['func'] == 'addtargets_to_project' ) {
		$i = 0;
		foreach ( $targets as $inx => $target ) {
			$sql = sprintf("INSERT INTO project_target ( target_id, project_id ) 
								VALUES ('%s', '%s')",
							$target, $_GET['project_id']);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
		//indaba_alert( $i . " targets has been added to project");
	}
	if ( $_GET['func'] == 'remove_targets' ) {
		$i = 0;
		foreach ( $targets as $inx => $target ) {
			$sql = sprintf("DELETE FROM project_target WHERE project_id = '%s' AND target_id = '%s'", $_GET['project_id'], $target);
			$st = @mysql_query($sql, $indaba_dbh);
			if ( mysql_affected_rows() == 1 ) $i ++;
		}
		//indaba_alert( $i . " targets has been removed from project");
	}

}

$sql = sprintf("SELECT * FROM project where id = '%s' ORDER BY code_name", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$project = @mysql_fetch_assoc($st);
if ( empty($project['code_name']) ) {
	indaba_alert("Project not found");
	header("Location: projects.php");
}

$sql = sprintf("SELECT * FROM target where id NOT IN ( SELECT target_id FROM project_target WHERE project_id = '%s') ORDER BY name", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$avatargetsOpt = "<SELECT name=target_id class=sel_list size=25 multiple>";
while ( $target = @mysql_fetch_assoc($st) ) {
	$avatargetsOpt .= "<option name=a value=" . $target['id'] . ">" . $target['name'] . "</option>";
}
$avatargetsOpt .= "</SELECT>";

$sql = sprintf("SELECT a.target_id as target_id, b.name as name FROM project_target a, target b WHERE a.target_id = b.id and a.project_id = '%s' ORDER BY b.name", $_GET['project_id']);
$st = @mysql_query($sql, $indaba_dbh);
$projecttargetsOpt = "<SELECT name=project_target_id class=sel_list size=25 multiple>";
while ( $target = @mysql_fetch_assoc($st) ) {
	$projecttargetsOpt .= "<option name=a value=" . $target['target_id'] . ">" . $target['name'] . "</option>";
}
$projecttargetsOpt .= "</SELECT>";


?>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Managing Targets for Project <? echo $project['code_name']; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script language=javascript>
function buttonActions() {
    var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var button = target.id;
	var selected_targets = "-1";
	if ( button == "addtarget" ) {
		if ( document.targets.target_id.selectedIndex == -1 ) {
			alert("Please select targets you want to assign to project then click 'Assign Targets to Project' button");
			return;
		}
		for ( var i = 0; i < document.targets.target_id.length; i ++ ) {
			if ( document.targets.target_id[i].selected ) 
				selected_targets = selected_targets + ":" + document.targets.target_id[i].value;
		}
		window.location = "manage_targets.php?func=addtargets_to_project&target_id=" + selected_targets + "&project_id=" + document.targets.project_id.value;
	}
	if ( button == "remove_targets" ) {
		if ( document.targets.project_target_id.selectedIndex == -1 ) {
			alert("Please select targets you want to remove from project then click 'Remove Targets' button");
			return;
		}
		for ( var i = 0; i < document.targets.project_target_id.length; i ++ ) {
			if ( document.targets.project_target_id[i].selected ) 
				selected_targets = selected_targets + ":" + document.targets.project_target_id[i].value;
		}
		window.location = "manage_targets.php?func=remove_targets&target_id=" + selected_targets + "&project_id=" + document.targets.project_id.value;
	}
	if ( button == "close_window" ) {
		self.close();
	}
	//return false;
}
</script>

</head>
<body>

<form name="targets" >
	<INPUT type=hidden name=project_id value="<? echo $project['id']; ?>" />
    <table width="600" border="0">
        <tr>
            <td colspan="3" align="left" valign="top"><p>Please select targets from the list and use the buttons to move targets in/out project.</p>
			  <p>When target is removed from the project, all users assigned with the target will be REMOVED from the project as well as the teams.</p>
			</td>
        </tr>
        <tr>
            <td align="center" valign="top">Available Targets </td>
            <td align="center" valign="middle">Action</td>
            <td align="center" valign="top">Targets in Project </td>
        </tr>
        <tr>
            <td align="center" valign="top"><? echo $avatargetsOpt; ?> </td>
            <td align="center" valign="middle"><table width="100%"  border="0">
                <tr>
                    <td align="center"><input type="button" id="addtarget" name="addtarget" class=btn value="Assign Targets to Project >>" onClick="buttonActions();"></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="center"><input type="button" id="remove_targets" name="remove_targets" class=btn value="<< Remove Targets" onClick="buttonActions();"></td>
                </tr>
            </table></td>
            <td align="center" valign="top"><? echo $projecttargetsOpt; ?>
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
