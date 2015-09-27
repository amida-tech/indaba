<?php

$db_host = "127.0.0.1";
$db_port = "3306";
$db_user = "indaba_user";
$db_pwd = "indaba_pwd";
$db_name = "indaba";

$upload_dir = "upload_files/";
$rule_file_dir = "rule_files/";
$sponsor_logos = $upload_dir . "/" . "sponsor_logos";
$peopleicons = $upload_dir . "/" . "peopleicons/";

define('SURVEY', 0);
define('JOURNAL', 1);

$moe = array('Default' => 1, 'Something' => 2);

$indaba_dbh = mysql_connect($db_host . ":" . $db_port, $db_user, $db_pwd);
@mysql_select_db($db_name, $indaba_dbh);

if (!function_exists('mysql_set_charset')) {
	function mysql_set_charset($charset,$dbh) {
			    return mysql_query("set names $charset",$dbh);
	}
}

mysql_set_charset('UTF8', $indaba_dbh);


function indaba_alert( $msg ) {
	$alert = empty($msg) ? "" : "alert(\"" . $msg . "\");";
	echo "<script language=javascript>" .  $alert .  "</script>";
}

function SQLString($theValue, $theType, $theDefinedValue = "", $theNotDefinedValue = "")
{
  $theValue = (!get_magic_quotes_gpc()) ? addslashes($theValue) : $theValue;

  switch ($theType) {
    case "text":
      $theValue = ($theValue != "") ? "'" . $theValue . "'" : "NULL";
      break;
    case "long":
    case "int":
      $theValue = ($theValue != "") ? intval($theValue) : "NULL";
      break;
    case "double":
      $theValue = ($theValue != "") ? "'" . doubleval($theValue) . "'" : "NULL";
      break;
    case "date":
      $theValue = ($theValue != "") ? "'" . $theValue . "'" : "NULL";
      break;
    case "defined":
      $theValue = ($theValue != "") ? $theDefinedValue : $theNotDefinedValue;
      break;
  }
  return $theValue;
}

function fetch_html_entities($st) {
	if ( $row = mysql_fetch_assoc($st) ) {
		foreach ( $row as $key => $value ) {
			// $row[$key] = mb_convert_encoding($value, 'UTF-8');
			// $row[$key] = $value;
			$row[$key] = htmlentities($value, ENT_QUOTES, 'UTF-8');
		}
		return $row;
	}
	return null;
}
?>
