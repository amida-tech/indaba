<?php
session_start();
require_once('include/config.inc');
$error = "";
$msg = "";

define('INTEGER_FILE', 'integer.csv');
define('FLOAT_FILE', 'float.csv');
define('TEXT_FILE', 'text.csv');
define('SINGLE_FILE', 'single.csv');
define('MULTIPLE_FILE', 'multiple.csv');

define('SINGLE', 1);
define('MULTIPLE', 2);
define('INTEGER', 3);
define('FLOAT', 4);
define('TEXT', 5);

foreach ( $_FILES as $key => $value ) {
	$item_name = $key;
}

$file_name = $_FILES[$item_name]['name'];

$validate_only = $_GET['validate_only'];

switch ( $file_name ) {
	case INTEGER_FILE:
		$type = 'integer';
		break;
	case FLOAT_FILE:
		$type = 'float';
		break;
	case TEXT_FILE:
		$type = 'text';
		break;
	case SINGLE_FILE:
		$type = 'single';
		break;
	case MULTIPLE_FILE:
		$type = 'multiple';
		break;
	default:
		$error = "File uploaded has wrong file name, please rename the file before upload.";
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
	$date = date('Ymd-G:i:s');
	$saved_file = $upload_dir . $date . "." . $file_name;
	$msg .= $file_name . " has been uploaded.";
	move_uploaded_file($_FILES[$item_name]['tmp_name'], $saved_file);
}		

// open log file
$log_file = "logs/" . $type . ".log";
$logfh = fopen($log_file, 'a');
if ( ! $logfh ) {
	$error = "Cannot open logfile " . $log_file . " for write.";
	echo json_encode(array("error" => $error ) );
	exit;
}
fwrite($logfh, "-------------------\n-------------------\n");
if ( $validate_only == 0 ) {
	fwrite($logfh, "Uploaded " . $file_name . " for process at " . $date . "\n");
} else {
	fwrite($logfh, "Uploaded " . $file_name . " for validate at " . $date . "\n");
}

// now process the file
$total_rows = 0;
$comments = 0;
$result = '';
$saved = 0;
$exceptions = 0;
// get all available reference types first
$sql = "SELECT id, lower(name) as ref_name FROM reference";
$st = mysql_query($sql);
$references = array();
$patterns = array( '/^\s+|\s+$/', '/\s+/');
$replace = array( '', ' ');
while ( $row = mysql_fetch_assoc($st) ) {
	$references[$row['id']] = preg_replace($patterns, $replace, $row['ref_name']);
}

// get all indicator names
$sql = "SELECT id, lower(name) as indicator_name from survey_indicator";
$st = mysql_query($sql);
$indicators = array();
while ( $row = mysql_fetch_assoc($st) ) {
	$indicators[$row['id']] = $row['indicator_name'];
}

// open saved file
if (($handle = fopen($saved_file, "r")) !== FALSE) {
	// get the headers
	$cols = 0;
	// process each row
	while ( ($data = fgetcsv($handle)) !== FALSE) {
		$cols = count($data);
		for ( $c = 0; $c < $cols; $c++ ) {
			$result .= $data[$c] . "<br />\n";
		}
		if ( preg_match('/^\#/', $data[0]) ) {
			$comments ++;
			continue;
		}
		$total_rows ++;
		$rt = process_data($type, $references, $indicators, $validate_only, $data, $logfh);
		if ( empty($rt['error'])  ) {
			$saved ++;
		} else {
			$exceptions ++;
			fwrite($logfh, "INDICATOR NAME: " . $data[0] . " :: " . $rt['error'] . "\n");
		}
	}
	$result = "File " . $file_name . " saved as " . $saved_file . ".\n" . 
			"   Total " . $total_rows . " records in the file. Total " . $comments . " rows of comments.\n" .
			"   Total " . $saved . " records been saved to the database or validated.\n" .
			"   Total " . $exceptions . " rows errored with exceptions. Check " . $log_file . " for details.\n";
	fwrite($logfh, $result);
	if ( $total_rows == 0 ) {
		$error = "Only headers found in the file, please make sure the CSV file is saved in Windows format.";
	}
	fclose($handle);
} else {
	echo json_encode(array('error' => "Cannot open file " . $saved_file . " for read.", 'msg' => $saved_file));
	exit;
}

$rt = array('error' => $error,
			'msg'	=> $msg,
			'result' => $result,
			'filename'	=> $file_name);
echo json_encode($rt);
exit;

function process_data($type, $references, $indicators, $validate_only, $data, $logfh) {
	$rt = array( 'error' => '');
	// first 3 columns of any type are required.
	$name = $data[0];
	if ( in_array(strtolower($name), $indicators) ) {
		$rt['error'] .= "Name already exist. ";
	}
	$question = $data[1];
	if ( $question == '' ) {
		$rt['error'] .= "Missing required [Question] field. ";
	}
	$patterns = array( '/^\s+|\s+$/', '/\s+/');
	$replace = array( '', ' ');
	$reference = preg_replace($patterns, $replace, strtolower($data[2]));
	$reference_id = array_search( $reference, $references );
	if ( ! isset($reference_id) || $reference_id == 0 || $reference_id == FALSE ) {
		$rt['error'] .= "Reference [" . $reference . "]" . " is not defined in the database." . " Reference1: [" . $references[0] . "]";
		$rt['status'] = 1;
	}
	$tip = $data[3];

	switch ( $type ) {
		case 'integer':
			$min = SQLString($data[4], 'int');
			$max = SQLString($data[5], 'int');
			$criteria = $data[6];
			$default = SQLString($data[7], 'int');
			// check min and max, should be integer
			$rt['error'] .= is_integer($min) ? '' : " Min value is required and must be integer [" . $min . "].";
			$rt['error'] .= is_integer($max) ? '' : " Max value is required and must be integer [" . $max . "].";
			$rt['error'] .= $min <= $max ? '' : " Min value [" . $min . "] must be less than or equal to Max value [" . $max . "].";
			// check criteria
			// $rt['error'] .= ! empty($criteria) ? '' : " Criteria is required.";
			// check default value
			$rt['error'] .= isset($default) && $default != "NULL" && ( (! is_integer($default)) || ( $default < $min || $default > $max ) ) ?
								" Default value [" . $default . "] must be integer and must be between Min value [" . 
								$min . "] and Max value [" . $max . "]." : '';
			if ( !empty( $rt['error'] ) || $validate_only ) {
				return $rt;
			}
			// finished check. compose sql
			$criteria = SQLString($criteria, 'text');
			$sql = sprintf("INSERT INTO answer_type_integer (min_value, max_value, default_value, criteria)
								VALUES ( %d, %d, %s, %s)",
						$min, $max, $default, $criteria);
			$st = mysql_query($sql);
			if ( ! $st ) {
				$rt['error'] .= " Error insert into answer_type_integer: " . mysql_error() . "\n";
				$rt['error'] .= "\tSQL:: [" . $sql . "]\n";
			} else {
				$answer_type_id = mysql_insert_id();
				$sql = sprintf("INSERT INTO survey_indicator ( name, question, answer_type, answer_type_id, reference_id, tip,
																create_user_id, create_time)
									VALUES (%s, %s, %d, %d, %d, %s, %d, now())",
								SQLString($name, 'text'), SQLString($question, 'text'), INTEGER, $answer_type_id, $reference_id,
								SQLString($tip, 'text'), $_SESSION['user_id']);
				$st = mysql_query($sql);
				if ( ! $st ) {
					$rt['error'] .= " Error inserting into survey_indicator: " . mysql_error() . "\n";
					$rt['error'] .= "\tSQL:: [" . $sql . "]\n";
				}
			}
			return $rt;
			break;
		case 'float':
			$min = $data[4];
			$max = $data[5];
			$criteria = $data[6];
			$default = $data[7];
			// check min and max, should be float
			$rt['error'] .= is_numeric($min) ? '' : " Min value is required and must be float [" . $min . "].";
			$rt['error'] .= is_numeric($max) ? '' : " Max value is required and must be float [" . $max . "].";
			$rt['error'] .= $min <= $max ? '' : " Min value [" . $min . "] must be less than or equal to Max value [" . $max . "].";
			// check criteria
			// $rt['error'] .= ! empty($criteria) ? '' : " Criteria is required.";
			// check default value
			$rt['error'] .= isset($default) && $default != '' && ( (! is_numeric($default)) || ( $default < $min || $default > $max ) ) ?
								" Default value [" . $default . "] must be float and must be between Min value [" . 
								$min . "] and Max value [" . $max . "]." : '';
			if ( !empty( $rt['error'] ) || $validate_only ) {
				return $rt;
			}
			// finished check. compose sql
			$criteria = SQLString($criteria, 'text');
			$default = SQLString($default, 'double');
			$sql = sprintf("INSERT INTO answer_type_float (min_value, max_value, default_value, criteria)
								VALUES ( %f, %f, %s, %s)",
						$min, $max, $default, $criteria);
			$st = mysql_query($sql);
			if ( ! $st ) {
				$rt['error'] .= " Error insert into answer_type_float: " . mysql_error() . "\n";
				$rt['error'] .= "\tSQL:: [" . $sql . "]\n";
			} else {
				$answer_type_id = mysql_insert_id();
				$sql = sprintf("INSERT INTO survey_indicator ( name, question, answer_type, answer_type_id, reference_id, tip,
																create_user_id, create_time)
									VALUES (%s, %s, %d, %d, %d, %s, %d, now())",
								SQLString($name, 'text'), SQLString($question, 'text'), FLOAT, $answer_type_id, $reference_id,
								SQLString($tip, 'text'), $_SESSION['user_id']);
				$st = mysql_query($sql);
				if ( ! $st ) {
					$rt['error'] .= " Error inserting into survey_indicator: " . mysql_error() . "\n";
					$rt['error'] .= "\tSQL:: [" . $sql . "]\n";
				}
			}
			return $rt;
			break;
		case 'text':
			$min = SQLString($data[4], 'int');
			$max = SQLString($data[5], 'int');
			$criteria = $data[6];
			// check min and max, should be integer
			$rt['error'] .= is_integer($min) ? '' : " Min length is required and must be integer [" . $min . "].";
			$rt['error'] .= is_integer($max) ? '' : " Max length is required and must be integer [" . $max . "].";
			$rt['error'] .= $min <= $max ? '' : " Min length [" . $min . "] must be less than or equal to Max length [" . $max . "].";
			// check criteria
			// $rt['error'] .= ! empty($criteria) ? '' : " Criteria is required.";
			if ( !empty( $rt['error'] ) || $validate_only ) {
				return $rt;
			}
			// finished check. compose sql
			$criteria = SQLString($criteria, 'text');
			$sql = sprintf("INSERT INTO answer_type_text (min_chars, max_chars, criteria)
								VALUES ( %d, %d, %s)",
						$min, $max, $criteria);
			$st = mysql_query($sql);
			if ( ! $st ) {
				$rt['error'] .= " Error insert into answer_type_text: " . mysql_error() . "\n";
				$rt['error'] .= "\tSQL:: [" . $sql . "]\n";
			} else {
				$answer_type_id = mysql_insert_id();
				$sql = sprintf("INSERT INTO survey_indicator ( name, question, answer_type, answer_type_id, reference_id, tip,
																create_user_id, create_time)
									VALUES (%s, %s, %d, %d, %d, %s, %d, now())",
								SQLString($name, 'text'), SQLString($question, 'text'), TEXT, $answer_type_id, $reference_id,
								SQLString($tip, 'text'), $_SESSION['user_id']);
				$st = mysql_query($sql);
				if ( ! $st ) {
					$rt['error'] .= " Error inserting into survey_indicator: " . mysql_error() . "\n";
					$rt['error'] .= "\tSQL:: [" . $sql . "]\n";
				}
			}
			return $rt;

			break;
		case ( $type == 'single' || $type == 'multiple' ) :
			// fields 4-11 are required since at least 2 sets are needed
			// only one set can be default
			$have_default = 0;
			$set = 0;
			$base = 4;
			$size = 4;
			while ( 1 ) {
				if ( ! isset($data[$base]) || $data[$base] == '' ) {
					break;
				}
				$label[$set] = $data[$base];
				$score[$set] = $data[($base + 1)];
				$criteria[$set] = $data[$base + 2];
				$default[$set] = strtolower($data[$base + 3]);
				if (  ! isset($score[$set]) ) {
					$rt['error'] .= " Score for choice set " . ($set + 1) . " is required. " . " Base label: " . $data[$base] . ", Score: " . $score[$set];
				} elseif ( ! is_numeric($score[$set]) ) {
					$rt['error'] .= " Score for choice set " . ($set + 1) . " must be a number. ";
				}
				// $rt['error'] .= ( ! empty($data[$base + 2]) ) ?
				// 			'' : " Criteria for choice set " . ($set + 1) . " is required. ";
				$rt['error'] .= ( $default[$set] == 'yes' || $default[$set] == 'no' ) ?
							'' : " Is_Default for choice set " . ($set + 1) . " is required and must be a value of 'YES' or 'NO' ";
				if ( $default[$set] == 'yes' ) {
					if ( $have_default == 1 && $type == 'single' ) {
						$rt['error'] .= " More than one default answer is specified for single choice type.";
					} else {
						$have_default = 1;
						$default[$set] = 1;
					}
				}
				$base = $base + $size; // move on to next set
				$set ++;
			}
			if ( $set < 2 ) { 
				$rt['error'] .= " At least 2 sets of choices are required.";
			}
			if ( ! empty($rt['error']) || $validate_only == 1 ) {
				// have a problem or just want to validate.
				break;
			}
			// run sql
			// get an id from answer_type_choice first
			$atc_sql = "INSERT INTO answer_type_choice VALUES () ";
			$st = mysql_query($atc_sql);
			if ( ! $st ) {
				$rt['error'] .= "Error inserting into answer_type_choice: " . mysql_error();
				return $rt;
			}
			$atc_id = mysql_insert_id();
			$weight = 1;
			$mask = 1;
			foreach ( $label as $ind => $value ) {
				//LABEL=>$value  SCORE=> $score[$ind] CRITERIA=>$criteria[$ind] DEFAULT=> $default[$ind] 
				$atc_sql = sprintf("INSERT INTO atc_choice (answer_type_choice_id, label, score, criteria, weight, mask, default_selected)
										VALUES ( %d, %s, %d, %s, %d, %d, %d)",
								$atc_id, SQLString($value, 'text'), $score[$ind] * 10000, 
								SQLString($criteria[$ind], 'text'), $weight, $mask, $default[$ind]);
				$st = mysql_query($atc_sql);
				if ( ! $st ) {
					$rt['error'] .= "Error inserting into atc_choice: " . mysql_error() . "\n";
					$rt['error'] .= "\tSQL:: [" . $atc_sql . "]\n";
					return $rt;
				}
				$weight ++;
				$mask = $mask * 2;
			}
			// ready to insert survey_indicator
			$si_sql = sprintf("INSERT INTO survey_indicator (name, question, answer_type, answer_type_id, reference_id, tip, 
																create_user_id, create_time)
									VALUES (%s, %s, %d, %d, %d, %s, %d, now())",
							SQLString($name, 'text'), SQLString($question, 'text'), SINGLE, $atc_id, $reference_id,
							SQLString($tip, 'text'), $_SESSION['user_id']);
			$st = mysql_query($si_sql);
			if ( ! $st ) {
				$rt['error'] .= "Error inserting into survey_indicator: " . mysql_error() . "\n";
				$rt['error'] .= "\tSQL:: [" . $si_sql . "]\n";
				return $rt;
			}
			break;
		default:
			$rt['status'] = 1;
			$rt['error'] = 'Unknown type.';
			break;
	}
	return $rt;
}

?>
