<?php
	require_once('include/config.inc');
	if ( ! isset($_GET['project_id']) || ! isset($_GET['logo']) ) {
		exit;
	}
	$project_id = $_GET['project_id'];
	$logo = $_GET['logo'];
	$error = 0;
	$msg = "";
	$sql = "SELECT * FROM project WHERE id = " . $project_id;
	$st = mysql_query($sql);
	$row = mysql_fetch_assoc($st);
	$logos = $row['sponsor_logos'];
	$patten = "/" . $logo . "/";
	$logos = preg_replace($patten, '', $logos);
	$sql = sprintf("UPDATE project SET sponsor_logos = '%s' WHERE id = %d",
				$logos, $project_id);
	$st = mysql_query($sql);
	if ( ! $st ) {
		$msg = mysql_error();
		$error = 1;
	} else {
		unlink($sponsor_logos . "/" . $logo);
	}
	echo json_encode(array('error' => $error, 'msg' => $msg));
?>
