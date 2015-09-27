<? 
session_start();
if ( ! isset( $_SESSION['authuser'] )) exit;
if ( !isset($_GET['project_id']) ) exit;
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Manage Running status of project</title >
<link rel="stylesheet" href="css/basic.css" type="text/css" /> 
<style type="text/css">
.st_el {
	width: 180px;
	height: 40px;
	float: left;
	padding-top: 10px;
	font-size: 12px;
	text-align: center;
	border: 1px solid black;
}
</style>
<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script language=javascript type="text/javascript">
function start_target(id) {
	//alert( " Starting all horses for target " + $(id).parent().attr('target_id') );
	var go = confirm("Are you sure you want to start all horses for this target?");
	if ( go == false ) {
		return;
	}
	var me = $(id).parent();
	var target = $(me).parent();
	$(target).children().not(':eq(0)').each( function() {
		return start_horse($(this));
	});
}

function stop_target(id) {
	//alert( " Stopping all horses for target " + $(id).parent().attr('target_id') );
	var go = confirm("Are you sure you want to stop all horses for this target?");
	if ( go == false ) {
		return;
	}
	var me = $(id).parent();
	var target = $(me).parent();
	$(target).children().not(':eq(0)').each( function() {
		return stop_horse($(this));
	});
}

function cancel_target(id) {
	// alert( " Cancelling all horses for target " + $(id).attr('target_id') );
	var go = confirm("Are you sure you want to cancel all horses for this target? This action is IRREVERSIBLE!");
	if ( go == false ) {
		return;
	}
	var me = $(id).parent();
	var target = $(me).parent();
	$(target).children().not(':eq(0)').each( function() {
		return cancel_horse($(this), 1);
	});
}

function start_product(id) {
	//alert( " Starting all horses for product " + $(id).parent().attr('ph_id'));
	var go = confirm("Are you sure you want to start all horses for this product?");
	if ( go == false ) {
		return;
	}
	var product_id = $(id).parent().attr('ph_id');
	$('[product_id=' + product_id + ']').each( function() {
		return start_horse($(this));
	});
}
function stop_product(id) {
	//alert( " Stopping all horses for product " + $(id).parent().attr('ph_id'));
	var go = confirm("Are you sure you want to stop all horses for this product?");
	if ( go == false ) {
		return;
	}
	var product_id = $(id).parent().attr('ph_id');
	$('[product_id=' + product_id + ']').each( function() {
		return stop_horse($(this));
	});
}

function cancel_product(id) {
	//alert( " Canceling all horses for product " + $(id).parent().attr('ph_id'));
	var go = confirm("Are you sure you want to cancel all horses for this product? This action is IRREVERSIBLE!");
	if ( go == false ) {
		return;
	}
	var product_id = $(id).parent().attr('ph_id');
	$('[product_id=' + product_id + ']').each( function() {
		return cancel_horse($(this), 1) ;
	});
}

function start_horse(id) {
	//alert( " Starting horse for target_id " + $(id).attr('target_id') + " product_id " + $(id).attr('product_id') );
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'horse_functions.php',
		data:		'action=start_horse&target_id=' + $(id).attr('target_id') + '&product_id=' + $(id).attr('product_id') ,
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				$(id).html(obj.horse);
				return true;
			} else {
				alert(obj.query_msg);
				return false;
			}
		}
	});
}

function stop_horse(id) {
	//alert( " Stopping horse for target_id " + $(id).attr('target_id') + " product_id " + $(id).attr('product_id') );
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'horse_functions.php',
		data:		'action=stop_horse&target_id=' + $(id).attr('target_id') + '&product_id=' + $(id).attr('product_id') ,
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				$(id).html(obj.horse);
				return true;
			} else {
				alert(obj.query_msg);
				return false;
			}
		}
	});

}
function cancel_horse(id, go) {
	//alert( " Cancelling horse for target_id " + $(id).attr('target_id') + " product_id " + $(id).attr('product_id') );
	if ( ! go ) { 
		var go = confirm("Are you sure you want to cancel this horse? This action is IRREVERSIBLE!");
		if ( go == false ) {
			return;
		}
	}
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'horse_functions.php',
		data:		'action=cancel_horse&target_id=' + $(id).attr('target_id') + '&product_id=' + $(id).attr('product_id') ,
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				$(id).html(obj.horse);
				return true;
			} else {
				alert(obj.query_msg);
				return false;
			}
		}
	});

}

</script>
</head>
<body>
	<div style="height:20px; width:700px; font-size:14px; float:left; text-align:center;">Legend</div>
	<div style="height:120px; width:700px; font-size:12px; float:left; border: 1px solid black;">
		<div style="height:30px; width:700px; float:left; margin: 5px 5px 5px 10px;">
			<div style="width:120px; margin-left:10px; float:left;">
				<img src="./images/play.png" />Start
			</div>
			<div style="width:120px; margin-left:10px; float:left;">
				<img src="./images/stop.png" />Stop
			</div>
			<div style="width:120px; margin-left:10px; float:left;">
				<img src="./images/eject.png" />Cancel
			</div>
			<div style="width:120px; margin-left:10px; float:left;">
				<img src="./images/completed.png" />Completed
			</div>
		</div>
		<div style="height:20px; width:590px; float:left; margin:5px 10px 10px 20px;">
			Color buttons indicate the possible status the horse can be changed. Click the button to change status
		</div>
		<div style="height:30px; width:600px; float:left; margin: 5px 10px 5px 10px;">
			<div style="width:120px; margin-left:10px; height:30px; float:left;">
				<img src="./images/play-disabled.png" />&nbsp;
				<img src="./images/stop-disabled.png" />&nbsp;
				<img src="./images/eject-disabled.png" />&nbsp;
			</div>
			<div style="width:380px; height:30px; float:left; ">Gray buttons indicate the current status of the horse
			</div>
		</div>
	</div>
	<div style="float:left; margin-top:15px; width:700px; ">
<?
require_once("./include/config.inc");

// define workflow object status (horse status)
define('WAITING', 1);
define('STARTED', 2);
define('COMPLETED', 3);
define('SUSPENDED', 4);
define('CANCELED', 5);

// Get all targets for this project, keyed by target id, value is target name
$target_sql = sprintf("SELECT pt.target_id, t.name FROM project_target pt, target t
							WHERE pt.target_id = t.id
								AND pt.project_id = %d", $_GET['project_id']);
$st = mysql_query($target_sql);
if ( mysql_num_rows($st) == 0 ) {
	echo "At least one target should be assigned to the project.";
	exit;
}
while ( $target = mysql_fetch_assoc($st) ) {
	$targets[$target['target_id']] = $target['name'];
}
// Get all products - array keyed by product_id, each element is a row of product
$product_sql = sprintf("SELECT * FROM product where project_id = %d",  $_GET['project_id']);
$st = mysql_query($product_sql); 
if ( mysql_num_rows($st) == 0 ) {
	echo "At least one product should be defined for the project.";
	exit;
}
// check user assignment
$user_sql = sprintf("SELECT * FROM project_membership WHERE project_id = %d", $_GET['project_id']);
$user_st = mysql_query($user_sql);
if ( mysql_num_rows($user_st) == 0 ) {
	echo "At least one user should be assigned to the project.";
	exit;
}
while ( $product = mysql_fetch_assoc($st) ) {
	$products[$product['id']] = $product;
}

// Now we draw a table, each column is a product, row is a target
// each element is the horse

$all_product_buttons = <<< EOF
	<img src="./images/play.png" onClick="start_product(this);" />&nbsp;
	<img src="./images/stop.png" onClick="stop_product(this);" />&nbsp;
	<img src="./images/eject.png"  onClick="cancel_product(this);"/>
EOF;

// Get the headers first
$style = "'width: " . ( count($products) + 2 ) * 180 . "px;' ";
echo " <div style=" . $style . " ><div class=st_el >&nbsp;</div>";
foreach ( $products as $product_id => $product_detail ) {
	echo " <div class=st_el ph_id='" . $product_id . "' > " . $product_detail['name'] . "<br />";
	echo $all_product_buttons . "</div>";
}
echo "  </div>";

$all_target_buttons = <<< EOF
	<img src="./images/play.png" onClick="start_target(this);" />&nbsp;
	<img src="./images/stop.png" onClick="stop_target(this);" />&nbsp;
	<img src="./images/eject.png"  onClick="cancel_target(this);"/>
EOF;
$play_btn = '<img src="./images/play.png" onClick="start_horse($(this).parent());" />&nbsp;';
$play_disabled = '<img src="./images/play-disabled.png" />&nbsp;';
$stop_btn = '<img src="./images/stop.png" onClick="stop_horse($(this).parent());" />&nbsp;';
$stop_disabled = '<img src="./images/stop-disabled.png" />&nbsp;';
$cancel_btn = '<img src="./images/eject.png" onClick="cancel_horse($(this).parent());" />&nbsp;';
$cancel_disabled = '<img src="./images/eject-disabled.png" />&nbsp;';

// Now the table contents
foreach ( $targets as $target_id => $target_name ) {
	echo "<div id=top style=" . $style . "><div class=st_el id=target_id_" . $target_id . " target_id=" . $target_id . " > " . $target_name . "<br />" . $all_target_buttons . "</div>";
	foreach ( $products as $product_id => $product_detail ) {
		// check horse with target_id and product_detail['id'], and workflow_object status
		$wo_sql = sprintf("SELECT h.*, wo.start_time as wo_start_time, wo.status 
							FROM horse h, workflow_object wo
							WHERE h.workflow_object_id = wo.id
								AND h.product_id = %d
								AND h.target_id = %d",
						$product_detail['id'], $target_id);
		$st = mysql_query($wo_sql);
		$horse = mysql_fetch_assoc($st);
		echo " <div class=st_el id='" . $target_id . "-" . $product_id . "' product_id=" . $product_id . " target_id=" . $target_id . " > ";
		// put buttons there based on current status of the horse
		if ( empty( $horse['status'] ) || $horse['status'] == WAITING  ) {
			// NULL or WAITING, can be started or cancelled
			echo $play_btn . $cancel_btn;
		} elseif ( $horse['status'] == STARTED ) {
			// can be  stopped, or cancelled
			echo $play_disabled . $stop_btn . $cancel_btn;
		} elseif ( $horse['status'] == SUSPENDED ) {
			// can be started, stopped, or cancelled
			echo $play_btn . $stop_disabled . $cancel_btn;
		} elseif ( $horse['status'] == CANCELED ) {
			// can NOT be restarted per #853
			echo $cancel_disabled;
		} elseif ( $horse['status'] == COMPLETED ) {
			// done with it, still can be cancelled per #853
			echo '<img src="./images/completed.png" />' . $cancel_btn;
		}
		echo "</div>";
	}
	echo "</div>";
}

?>
</div>
</body>
</html>
