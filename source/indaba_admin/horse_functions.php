<?php
session_start();
if ( ! isset( $_SESSION['authuser'] ) || ! isset($_GET['target_id']) || ! isset($_GET['product_id']) ) exit;
require_once("./include/config.inc");
require_once("./horse_inc.php");

$target_id = $_GET['target_id'];
$product_id = $_GET['product_id'];

$rt = array('query_status' => 0, 'query_msg' => '');

$rt = check_horse($product_id);


if ( $rt['query_status'] == 1 ) {
	echo json_encode($rt);
	exit;
}

$rt = sync_horse($target_id, $product_id);
$horse = get_horse($target_id, $product_id);

if ( isset($_GET['action']) && $_GET['action'] == 'start_horse' ) {
	$sql = sprintf("UPDATE horse, workflow_object 
						SET horse.start_time = now(), workflow_object.status = %d, workflow_object.start_time = now() 
						WHERE workflow_object.id = %d AND horse.workflow_object_id = workflow_object.id",
			STARTED, $horse['workflow_object_id']);
	$st = mysql_query($sql);
	$rt['query_status'] = $st ? 0 : 1;
	$rt['start_sql'] = $sql;
	$rt['query_msg'] = $st ? 'Horse started' : 'Error starting horse:' . mysql_error();
	if ( $rt['query_status'] == 0 ) {
		$rt['horse'] = '<img src="./images/play-disabled.png" />&nbsp;
							<img src="./images/stop.png" onClick="stop_horse($(this).parent());" />&nbsp;
							<img src="./images/eject.png" onClick="cancel_horse($(this).parent());" />&nbsp;';
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'stop_horse' ) {
	$sql = sprintf("UPDATE workflow_object SET status = %d WHERE id = %d",
				SUSPENDED, $horse['workflow_object_id']);
	$st = mysql_query($sql);
	$rt['query_status'] = $st ? 0 : 1;
	$rt['start_sql'] = $sql;
	$rt['query_msg'] = $st ? 'Horse stopped' : 'Error stopping horse:' . mysql_error();
	if ( $rt['query_status'] == 0 ) {
		$rt['horse'] = '<img src="./images/play.png" onClick="start_horse($(this).parent());" />&nbsp;
							<img src="./images/stop-disabled.png" />&nbsp;
							<img src="./images/eject.png" onClick="cancel_horse($(this).parent());" />&nbsp;';
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'cancel_horse' ) {
	$sql = sprintf("UPDATE workflow_object SET status = %d WHERE id = %d",
				CANCELED, $horse['workflow_object_id']);
	$st = mysql_query($sql);
	$rt['query_status'] = $st ? 0 : 1;
	$rt['start_sql'] = $sql;
	$rt['query_msg'] = $st ? 'Horse canceled' : 'Error canceling horse:' . mysql_error();
	if ( $rt['query_status'] == 0 ) {
		$rt['horse'] = ' <img src="./images/eject-disabled.png"  />&nbsp;';
	}
	echo json_encode($rt);
	exit;
}

?>
