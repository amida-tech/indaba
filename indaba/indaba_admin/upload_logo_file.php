<?php
	require_once('include/config.inc');
	if ( ! isset($_GET['project_id']) ) {
		exit;
	}
	$project_id = $_GET['project_id'];
	$error = "";
	$msg = "";
	foreach ( $_FILES as $key => $value ) {
		$item_name = $key;
	}
	if ( $_FILES[$item_name]['type'] !=  "image/gif" &&
		 $_FILES[$item_name]['type'] !=  "image/jpeg" ) {
		$error = "You may only upload GIF or JPEG files";
		$rt = array('error' => $error, 'msg' => $msg );
		echo json_encode($rt);
		exit;
	}

	if(!empty($_FILES[$item_name]['error'])) {
		switch($_FILES[$item_name]['error']) {
			case '1':
				$error = 'The uploaded file exceeds the upload_max_filesize directive in php.ini';
				break;
			case '2':
				$error = 'The uploaded file exceeds the MAX_FILE_SIZE directive that was specified in the HTML form';
				break;
			case '3':
				$error = 'The uploaded file was only partially uploaded';
				break;
			case '4':
				$error = 'No file was uploaded.';
				break;
			case '6':
				$error = 'Missing a temporary folder';
				break;
			case '7':
				$error = 'Failed to write file to disk';
				break;
			case '8':
				$error = 'File upload stopped by extension';
				break;
			case '999':
			default:
				$error = 'No error code avaiable';
		}
	} elseif (empty($_FILES[$item_name]['tmp_name']) || $_FILES[$item_name]['tmp_name'] == 'none') {
		$error = 'No file was uploaded..';
	} else {

		$short_name = "project_" . $project_id . "_" . $_FILES[$item_name]['name'];
		$file_name = $sponsor_logos . "/" . $short_name;
		$msg .= $_FILES[$item_name]['name'] . " has been uploaded.";
		move_uploaded_file($_FILES[$item_name]['tmp_name'], $file_name);

		$sql = sprintf("UPDATE project SET sponsor_logos = concat(IFNULL(concat(sponsor_logos, ' '), ''), '%s') WHERE id = %d",
					$short_name, $project_id);
		$st = mysql_query($sql);
		if ( mysql_error() ) {
			$error = mysql_error();
		}
	}		
	$rt = array('error' => $error,
				'msg'	=> $msg,
				'filename'	=> $_FILES[$item_name]['name']);
	echo json_encode($rt);
?>
