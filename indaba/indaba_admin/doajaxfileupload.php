<?php
	session_start();
	require_once('include/config.inc');
	$error = "";
	$msg = "";
	$fileElementName = 'fileToUpload';
	if ( $_FILES[$fileElementName]['type'] != "image/gif" &&
			$_FILES[$fileElementName]['type'] != "image/jpeg" ) {
		$error = "You may only upload file in gif or jpeg format";
		$msg = "Only GIF or JPEG format file can be uploaded";
		$rt = array('error' => $error, 'msg' => $msg);
		echo json_encode($rt);
		exit;
	}
	if(!empty($_FILES[$fileElementName]['error'])) {
		switch($_FILES[$fileElementName]['error']) {
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
	} elseif (empty($_FILES['fileToUpload']['tmp_name']) || $_FILES['fileToUpload']['tmp_name'] == 'none') {
		$error = 'No file was uploaded..';
	}else {
		// $msg .= " File Name: " . $_FILES['fileToUpload']['name'] . ", ";
		// $msg .= " File Size: " . @filesize($_FILES['fileToUpload']['tmp_name']);
		//for security reason, we force to remove all uploaded file
		// @unlink($_FILES['fileToUpload']);		
		if ( $_SESSION['photo_type'] == 'user_photo' ) {
			$file_name =  $_SESSION['current_user_id'] . ".jpg";
			$msg .= " File saved as " . $file_name;
			move_uploaded_file($_FILES[$fileElementName]['tmp_name'], "$peopleicons" . "/" . $file_name);
			$sql = sprintf("UPDATE user set photo = '%s' WHERE id = %d", $file_name, $_SESSION['current_user_id']);
			$st = mysql_query($sql);
		} elseif ( $_SESSION['photo_type'] == 'project_logo' ) {
			$file_name = $upload_dir . "project_" . $_SESSION['current_project_id'] . ".jpg";
			$msg .= " File saved as " . $file_name;
			move_uploaded_file($_FILES[$fileElementName]['tmp_name'], $file_name);
			$sql = sprintf("UPDATE project set logo_path = '%s' WHERE id = %d", $file_name, $_SESSION['current_project_id']);
			$st = mysql_query($sql);
		}
		if ( mysql_error() ) {
			$error = mysql_error();
		}
	}		
	$rt = array('error' => $error,
				'msg'	=> $msg,
				'filename'	=> $peopleicons . $file_name);
	echo json_encode($rt);
?>
