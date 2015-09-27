<?php
session_start();
if ( ! isset( $_SESSION['authuser'] )) exit;
if ( !isset($_GET['workflow_id']) ) exit;

require_once("include/config.inc");
require_once("horse_inc.php");

$workflow_id = $_GET['workflow_id'];

function get_out($status, $msg, $sql, $error) {
	$rt = array(
			'status'	=> $status,
			'query_msg'		=> $msg,
			'sql'		=> $sql,
			'error'		=> $error);
	echo json_encode($rt);
	exit;
}

// get all products that are using this workflow
$sql = "SELECT * FROM product WHERE workflow_id = " . $workflow_id;
$st = mysql_query($sql);
while ( $product = mysql_fetch_assoc($st) ) {
	$check_rt = check_horse($product['id']);
	if ( $check_rt['query_status'] == 1 ) {
		echo json_encode($check_rt);
		exit;
	}
	$p_sql = sprintf("SELECT * FROM horse WHERE product_id = %d", $product['id']);
	$p_st = mysql_query($p_sql);
	while ( $pt = mysql_fetch_assoc($p_st) ) {
		$sync = sync_horse($pt['target_id'], $pt['product_id']);
		if ( $sync['query_status'] == 1 ) {
			get_out(1, "Error sync workflow for product [" . $product['name'] . "] ", '', $sync['query_msg']);
		}
	}
}
get_out(0, "All products using this workflow have been synced", '', '');

?>
