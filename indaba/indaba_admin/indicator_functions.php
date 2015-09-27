<?php
session_start();
require_once('include/config.inc');

// define answer types
define ('SINGLE', 1);
define ('MULTIPLE', 2);
define ('INTEGER', 3);
define ('FLOAT', 4);
define ('TEXT', 5);

if ( isset($_GET['action']) && $_GET['action'] == 'show_indicator' ) {
	$where = ' ORDER BY name ';
	if ( isset($_GET['indicator_id']) ) {
		$where = " WHERE id = " . $_GET['indicator_id'];
		$indicator_id = $_GET['indicator_id'];
	} else {
		$indicator_id = 0;
	}
	$sql = "SELECT * FROM survey_indicator " . $where;
	$st = mysql_query($sql);
	$indicator_list = "";
	while ( $indicator = fetch_html_entities($st) ) {
		$name = substr($indicator['name'] . ' - ' . $indicator['question'], 0, 125);
		$name = htmlentities($name, ENT_QUOTES  , 'UTF-8');
		// $name = substr($indicator['name'] . ' - ' . $indicator['question'], 0, 55);
		$class = "ui-widget-content";
		if ( isset($_GET['id']) && $_GET['id'] == $indicator['id'] ) {
			$class = "ui-selected";
		}
		$indicator_list .= "<li class='" . $class . "' style='float:left; margin-left:10px; width:600px;' id=" . $indicator['id'] . ">" . $name . "</li>";
		$rt = $indicator;
	}
	if ( ! isset($_GET['indicator_id']) || $_GET['indicator_id'] <= 0 ) {
		$rt['id'] = 0;
		$rt['answer_type'] = 0;
		$rt['answer_type_id'] = 0;
		$rt['reference_id'] = 0;
		$rt['created_by'] = '';
	} else {
		$sql = "SELECT * FROM user WHERE id = " . $rt['create_user_id'];
		$st = mysql_query($sql);
		$user = fetch_html_entities($st);
		$rt['created_by'] = "Created by " . $user['username'] . " on " . $rt['create_time'];
	}
	$rt['indicator_list'] = $indicator_list;
	// get reference options
	$sql = "SELECT * FROM reference ORDER BY name";
	$st = mysql_query($sql);
	$referenceOpt = "<SELECT id='reference_id' onChange='return true;' >";
	while ( $reference = fetch_html_entities($st) ) {
		$selected = $reference['id'] == $rt['reference_id'] ? ' selected=selected ' : '';
		$referenceOpt .= "<option value=" . $reference['id'] . $selected . ">" . $reference['name'] . "</option>";
	}
	$referenceOpt .= "</SELECT>";
	$rt['referenceOpt'] = $referenceOpt;
	// get tag options
	$sql = sprintf("SELECT * FROM itags WHERE id NOT IN ( SELECT itags_id FROM indicator_tag WHERE survey_indicator_id = %d)",
					$indicator_id);
	$st = mysql_query($sql);
	$tagOpt = "<SELECT id='itag_id' ><option value=0>Select tag From the List</option>";
	while ( $tag = fetch_html_entities($st) ) {
		$tagOpt .= "<option term='" . $tag['term'] . "' value='tag_" . $tag['id'] . "'>" . $tag['term'] . ' - ' . $tag['description'] . "</option>";
	}
	$tagOpt .= "</SELECT>";
	$rt['tagOpt'] = $tagOpt;
	
	$tagList = "";
	$sql = sprintf("SELECT t.id, t.term, t.description FROM itags t, indicator_tag it
						WHERE t.id = it.itags_id AND it.survey_indicator_id = %d",
					$indicator_id);
	$st = mysql_query($sql);
	while ( $it = fetch_html_entities($st) ) {
		$tagList .= "<li class='ui-widget-content' id='exist_" . $it['id'] . "' term='" . $it['term'] . "'>" . $it['term'] . " - " . $it['description'] . "</li>";
	}
	$rt['tagList'] = $tagList;
	
	// generate answer type options
	$answer_typeOpt = "<SELECT id='answer_type' onChange='answer_typeSelected();'>";
	$answer_type = array('1' => 'Single Choice', '2' => 'Multiple Choice', '3' => 'Integer', '4' => 'Float', '5' => 'Text');
	for ( $i = 1; $i<=5; $i ++ ) {
		$selected = $i == $rt['answer_type']? " selected=selected " : '';
		$answer_typeOpt .= "<option value=" . $i . $selected . ">" . $answer_type[$i] . "</option>";
	}
	$answer_typeOpt .= "</SELECT>";
	$rt['answer_typeOpt'] = $answer_typeOpt;
	$rt['sql'] = $sql;
	$rt['query_msg'] = mysql_error();
	echo json_encode($rt);
	exit;
}


if ( isset($_GET['action']) && $_GET['action'] == 'show_answers' ) {
	switch ( $_GET['answer_type'] ) {
		case 1:
			$sql = sprintf("SELECT * FROM atc_choice WHERE answer_type_choice_id = %d ORDER BY weight", $_GET['answer_type_id']);
			break;
		case 2:
			$sql = sprintf("SELECT * FROM atc_choice WHERE answer_type_choice_id = %d ORDER BY weight", $_GET['answer_type_id']);
			break;
		case 3:
			$sql = sprintf("SELECT * FROM answer_type_integer WHERE id = %d", $_GET['answer_type_id']);
			break;
		case 4:
			$sql = sprintf("SELECT * FROM answer_type_float WHERE id = %d", $_GET['answer_type_id']);
			break;
		case 5:
			$sql = sprintf("SELECT * FROM answer_type_text WHERE id = %d", $_GET['answer_type_id']);
			break;
		default:
			echo json_encode(array('error' => 'No answer_type is given'));
			exit;
			break;
	}
	$st = mysql_query($sql);
	if ( $_GET['answer_type'] <= 2 ) { // choices, multiple row might be returned so only return list of label and default
		$atc_choices = "<div id=atc_new style='background-color: #29efff;' onClick='atcSelected(this);' ><INPUT style='font-size:14px; text-align:center;color:red;' class=choice_label align=center readonly='readonly' value='Add New Choice' /><input class=choice_default readonly='readonly'/></div>";
		while ( $choice = fetch_html_entities($st) ) {
			$default = isset($choice['default_selected']) && $choice['default_selected'] == 1? "True" : "False";
			$atc_choices .= "<div id=atc_choice_" . $choice['id'] . " onClick='atcSelected(this);' ><INPUT readonly='readonly' id=" . $choice['answer_type_choice_id'] .
								" class=choice_label value='" . $choice['label'] . "' />";
			$atc_choices .= "<input class=choice_default readonly='readonly' value='" . $default . "' /></div>";
		}
		echo json_encode(array('atc_choices' => $atc_choices));
		exit;
	} else {
		$rt = fetch_html_entities($st);
	}
	$rt['sql'] = $sql;
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_atc_choice' ) {
	$sql = "SELECT * FROM atc_choice WHERE id = " . $_GET['atc_choice_id'];
	$st = mysql_query($sql);
	$rt = fetch_html_entities($st);
	if ( ! $rt ) {
		$rt = array(
					'answer_type_choice_id'	=> 0,
					'label'			=> '',
					'score'			=> '',
					'use_score'		=> 1,
					'default_selected' => 0,
					'criteria'		=> '');
	} else {
		$rt['score'] = $rt['score'] != '' ? $rt['score'] / 10000 : '';
	}
	$rt['sql'] = $sql;
	echo json_encode($rt);
	exit;
}
if ( isset($_GET['action']) && $_GET['action'] == 'save_choice' ) {
	$indicator_id = $_GET['indicator_id'];
	$label = SQLString($_GET['choice_label'], 'text');
	$use_score = $_GET['use_score'];
	$score = isset($_GET['choice_score']) && $_GET['choice_score'] != '' ? SQLString($_GET['choice_score'] * 10000, 'int') : 'NULL';
	$criteria = SQLString(urldecode($_GET['choice_criteria']), 'text');
	$atc_choice_id = $_GET['atc_id'];
	$is_default = $_GET['is_default'];
	$answer_type = $_GET['answer_type'];
	$answer_type_choice_id = $_GET['answer_type_id'];
	if ( $atc_choice_id <= 0 || ! is_numeric($atc_choice_id) ) {
		// this is an insert, need to get the largetest weight first
		$sql = sprintf("SELECT max(weight) as max_weight, max(mask) as max_mask, answer_type_id
					FROM survey_indicator 
						LEFT JOIN  atc_choice ON survey_indicator.answer_type_id = atc_choice.answer_type_choice_id
					WHERE survey_indicator.answer_type = %d
						AND survey_indicator.id = %d
					GROUP BY answer_type_id", $answer_type, $indicator_id);
		$st = mysql_query($sql);
		$rt = array('check_sql' => $sql);
		$current_max = fetch_html_entities($st);
		// if returned answer_type_id is NULL, the answer type is changed
		$weight = isset($current_max['max_weight']) && is_numeric($current_max['max_weight']) ? $current_max['max_weight'] + 1 : '1';
		$mask = isset($current_max['max_mask']) && is_numeric($current_max['max_mask']) ? $current_max['max_mask'] * 2 : '1';
		$sql = sprintf("INSERT INTO atc_choice (answer_type_choice_id, label, score, criteria, weight, mask, default_selected, use_score)
							VALUES (%d, %s, %s, %s, %d, %d, %d, %d)",
					$answer_type_choice_id, $label, $score, $criteria, $weight, $mask, $is_default, $use_score);
		$rt['insert_sql'] = $sql;
		$st = mysql_query($sql);
		if ( ! $st ) {
			$rt['query_msg'] = "Error saving new chioce: " . mysql_error();
			$rt['query_status' ] = 1;
		} else {
			$rt['query_msg'] = "Choice [" . $label . "] saved";
			$rt['query_status'] = 0;
			$rt['inserted_id'] = mysql_insert_id();
		}
		echo json_encode($rt);
		exit;
	} else {
		// update given choice
		$sql = sprintf("UPDATE atc_choice SET label = %s, score = %s, default_selected = %d, criteria = %s, use_score = %d
							WHERE id = %d",
						$label, $score, $is_default, $criteria, $use_score, $atc_choice_id);
		$st = mysql_query($sql);
		if ( ! $st ) {
			$rt['query_msg'] = "Error saving chioce: " . mysql_error();
			$rt['query_status' ] = 1;
		} else {
			$rt['query_msg'] = "Choice [" . $label . "] saved";
			$rt['query_status'] = 0;
		}
		$rt['sql'] = $sql;
		echo json_encode($rt);
		exit;
	}
}

// Delete single/multiple choice
if ( isset($_GET['action']) && $_GET['action'] == 'delete_choice' ) {
	$atc_choice_id = $_GET['atc_id'];
	$sql = "DELETE FROM atc_choice WHERE id = " . $atc_choice_id;
	$st = mysql_query($sql);
	if ( ! $st ) {
		$rt['query_msg'] = "Error deleting chioce: " . mysql_error();
		$rt['query_status' ] = 1;
	} else {
		$rt['query_msg'] = "Choice deleted";
		$rt['query_status'] = 0;
	}
	$rt['sql'] = $sql;
	echo json_encode($rt);
	exit;
}
// Move single/multiple choice up or down
if ( isset($_GET['action']) && $_GET['action'] == 'move_choice' ) {
	$direction = $_GET['direction'];
	$edge = $direction == 'up' ? 'top' : 'bottom';
	$atc_choice_id = $_GET['atc_id'];
	// get weight for this atc_id
	$sql = "SELECT * FROM atc_choice WHERE id = " . $atc_choice_id;
	$st = mysql_query($sql);
	$mover = fetch_html_entities($st);
	// get the one we want to switch weight
	if ( $direction == 'up' ) {
		$sql = sprintf("SELECT * FROM atc_choice WHERE answer_type_choice_id = %d AND weight < %d ORDER BY weight DESC LIMIT 1",
				$mover['answer_type_choice_id'], $mover['weight']);
	} else {
		$sql = sprintf("SELECT * FROM atc_choice WHERE answer_type_choice_id = %d AND weight > %d ORDER BY weight LIMIT 1",
				$mover['answer_type_choice_id'], $mover['weight']);
	}
	$rt['sql'] = $sql;
	$st = mysql_query($sql);
	if ( mysql_num_rows($st) == 0  ) {
		$rt['query_msg'] = "Cannot move [" . $mover['label'] . "]. Already at " . $edge;
		$rt['query_status'] = 1;
	} else {
		$replace = fetch_html_entities($st);
		$sql = sprintf("UPDATE atc_choice SET weight = %d WHERE id = %d", $mover['weight'], $replace['id']);
		$st = mysql_query($sql);
		$rt['replace_sql'] = $sql;
		$sql = sprintf("UPDATE atc_choice SET weight = %d WHERE id = %d", $replace['weight'], $mover['id']);
		$rt['mover_sql'] = $sql;
		$st = mysql_query($sql);
		$rt['query_msg'] = "Moved [" . $mover['label'] . "] " . $direction;
		$rt['query_status'] = 0;
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'save_indicator' ) {
	$indicator_id = $_GET['indicator_id'];
	$answer_type = $_GET['answer_type'];
	$answer_type_id = $_GET['answer_type_id'];
	$name = SQLString($_GET['name'], 'text');
	$question = SQLString(urldecode($_GET['question']), 'text');
	$tip = SQLString(urldecode($_GET['tip']), 'text');
	$criteria = isset($_GET['criteria']) ? SQLString(urldecode($_GET['criteria']), 'text') : '';
	$reference_id = $_GET['reference_id'];
	$default_value = isset($_GET['default_value']) && is_numeric($_GET['default_value']) ? $_GET['default_value'] : "NULL";
	if ( $answer_type == SINGLE || $answer_type == MULTIPLE  ) {
		// choices
		if ( $indicator_id <= 0 || $answer_type_id == 0 ) {
			// insert answer_type_choice first
			$at_sql = "INSERT INTO answer_type_choice VALUES ()";
			$st = mysql_query($at_sql);
			$answer_type_id = mysql_insert_id();
		}
	} elseif ( $answer_type == INTEGER || $answer_type == FLOAT ) {
		$table = $answer_type == INTEGER ? "answer_type_integer" : "answer_type_float";
		if ( $indicator_id <= 0 || $answer_type_id == 0 ) {
			// insert answer_type_integer first
			$at_sql = sprintf("INSERT INTO %s (min_value, max_value, default_value, criteria)
									VALUES (%s, %s, %s, %s)",
							$table, $_GET['min'], $_GET['max'], $default_value, $criteria);
		} else {
			// update answer_type_integer
			$at_sql = sprintf("UPDATE %s
								SET min_value = %s, max_value=%s, default_value=%s, criteria=%s
								WHERE id = %d",
							$table, $_GET['min'], $_GET['max'], $default_value, $criteria, $answer_type_id);
		}
		$st = mysql_query($at_sql);
		$rt['at_sql'] = $at_sql;
		$rt['default_value'] = $default_value;
		if ( ! $st ) {
			$rt['query_status'] = 1;
			$rt['query_msg'] = "Error saving answer: " . mysql_error();
			echo json_encode($rt);
			exit;
		}
		$answer_type_id = $answer_type_id == 0? mysql_insert_id() : $answer_type_id ;
	} elseif ( $answer_type == TEXT ) {
		if ( $indicator_id <= 0 || $answer_type_id == 0 ) {
			// insert answer_type_text first
			$at_sql = sprintf("INSERT INTO answer_type_text (min_chars, max_chars, criteria)
									VALUES (%d, %d, %s)",
							$_GET['min'], $_GET['max'], $criteria);
		} else {
			// update answer_type_text
			$at_sql = sprintf("UPDATE answer_type_text
								SET min_chars = %d, max_chars=%d, criteria=%s
								WHERE id = %d",
							$_GET['min'], $_GET['max'], $criteria, $answer_type_id);
		}
		$st = mysql_query($at_sql);
		$rt['at_sql'] = $at_sql;
		if ( ! $st ) {
			$rt['query_status'] = 1;
			$rt['query_msg'] = "Error saving answer: " . mysql_error();
			echo json_encode($rt);
			exit;
		}
		$answer_type_id = $answer_type_id == 0? mysql_insert_id() : $answer_type_id ;
	}
	// answer should be updated/inserted by now, deal with indicator itself
	if ( $indicator_id <= 0 ) {
		$upd_sql = sprintf("INSERT INTO survey_indicator 
								(name, question, answer_type, answer_type_id, reference_id, tip, create_user_id, create_time ) 
							VALUES (%s, %s, %d, %d, %d, %s, %d, now())",
						$name, $question, $answer_type, $answer_type_id, $reference_id, $tip, $_SESSION['user_id'] );
		$st = mysql_query($upd_sql);
		$indicator_id = mysql_insert_id();
	} else {
		// update indicator
		$upd_sql = sprintf("UPDATE survey_indicator 
								SET name=%s, question=%s, answer_type=%d, answer_type_id=%d, reference_id=%d, tip = %s
								WHERE id = %d",
						$name, $question, $answer_type, $answer_type_id, $reference_id, $tip, $indicator_id);
		$st = mysql_query($upd_sql);
	}
	// now deal with the tags
	if ( isset($_GET['remove_tag_id']) && count($_GET['remove_tag_id']) > 0 ) {
		$delete_sql = sprintf("DELETE FROM indicator_tag WHERE survey_indicator_id = %d AND itags_id IN (",
							$indicator_id);
		foreach ( $_GET['remove_tag_id'] as $tag_id ) {
			$delete_sql .= $tag_id . ", ";
		}
		$delete_sql = preg_replace('/, $/', ')', $delete_sql);
		$st = mysql_query($delete_sql);
	}
	if ( isset($_GET['add_tag_id']) && count($_GET['add_tag_id']) > 0 ) {
		$ins_sql = "INSERT INTO indicator_tag ( survey_indicator_id, itags_id ) VALUES ";
		foreach ( $_GET['add_tag_id'] as $tag_id ) {
			$ins_sql .= sprintf("(%d, %d),", $indicator_id, $tag_id);
		}
		$ins_sql = preg_replace('/,$/', '', $ins_sql);
		$st = mysql_query($ins_sql);
	}
	
	//$rt = array('at_sql' => $at_sql, 'upd_sql' => $upd_sql);
	if ( ! $st ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Error saving indicator: " . mysql_error();
		echo json_encode($rt);
		exit;
	}
	$rt['query_status'] = 0;
	$rt['query_msg'] = "Indicator saved";
	$rt['id'] = $indicator_id == 0 ? mysql_insert_id() : $indicator_id;
	echo json_encode($rt);
	exit;
}
// delete indicator
if ( isset($_GET['action']) && $_GET['action'] == 'delete_indicator' ) {
	// check if this indicator is used in survey_question
	$sql = "SELECT * FROM survey_question WHERE survey_indicator_id = " . $_GET['indicator_id'];
	$st = mysql_query($sql);
	if ( mysql_num_rows($st) == 0 ) {
		$sql = "SELECT * FROM survey_indicator WHERE id = " . $_GET['indicator_id'];
		$st = mysql_query($sql);
		$indicator = fetch_html_entities($st);
		if ( $indicator['answer_type'] == INTEGER ) {
			$del_sql = "DELETE FROM survey_indicator, answer_type_integer
							USING survey_indicator LEFT JOIN answer_type_integer
								ON survey_indicator.answer_type_id = answer_type_integer.id
							WHERE survey_indicator.id = " . $_GET['indicator_id'];
		} elseif ( $indicator['answer_type'] == FLOAT ) {
			$del_sql = "DELETE FROM survey_indicator, answer_type_float
							USING survey_indicator LEFT JOIN answer_type_float
								ON survey_indicator.answer_type_id = answer_type_float.id
							WHERE survey_indicator.id = " . $_GET['indicator_id'];
		} elseif ( $indicator['answer_type'] == TEXT ) {
			$del_sql = "DELETE FROM survey_indicator, answer_type_text
							USING survey_indicator LEFT JOIN answer_type_text
								ON survey_indicator.answer_type_id = answer_type_text.id
							WHERE survey_indicator.id = " . $_GET['indicator_id'];
		} else {
			$del_sql = sprintf("DELETE FROM survey_indicator, answer_type_choice, atc_choice
									USING survey_indicator LEFT JOIN (answer_type_choice, atc_choice)
										ON (
											survey_indicator.answer_type_id = answer_type_choice.id
											AND survey_indicator.answer_type_id = atc_choice.answer_type_choice_id)
									WHERE survey_indicator.id = %d", $_GET['indicator_id']);
		}

		$st = mysql_query($del_sql);
		$rt = array('sql' => $sql, 'del_sql' => $del_sql);
		if ( ! $st ) {
			$rt['query_status'] = 1;
			$rt['query_msg'] = "Error deleting indicator: " . mysql_error();
		} else {
			$rt['query_status'] = 0;
			$rt['query_msg'] = "Indicator deleted.";
		}
		// delete from atc_choice
		echo json_encode($rt);
		exit;
	}
	// it is used 
	while ( $sq = fetch_html_entities($st) ) {
		$sq_questions .= $sq['name'] . " ";
	}
	$rt['query_status'] = 1;
	$rt['query_msg'] = "This indicator is used by survey questions: " . $sq_questions;
	echo json_encode($rt);
	exit;
}

// clone indicator
if ( isset($_GET['action']) && $_GET['action'] == 'clone' ) {
	$org_id = $_GET['indicator_id'];
	$sql = sprintf("INSERT INTO survey_indicator (name, question, answer_type, answer_type_id, reference_id, tip, create_user_id, create_time, original_indicator_id)
						SELECT concat(name, '-clone'), question, answer_type, answer_type_id, reference_id, tip, create_user_id, now(), id
							FROM survey_indicator
							WHERE id = %d", 
					$org_id);
	$st = mysql_query($sql);
	if ( $st ) {
		$query_status = 0;
		$query_msg = "Indicator cloned";
		$id = mysql_insert_id();
	} else {
		$query_status = 1;
		$query_msg = "Error cloning indicator: " . mysql_error();
		exit;
	}
	// now clone answer
	$sel_sql = "SELECT * FROM survey_indicator WHERE id = " . $id;
	$st = mysql_query($sel_sql);
	$new = fetch_html_entities($st);
	if ( $new['answer_type'] == SINGLE || $new['answer_type'] == MULTIPLE  ) {
		$at_sql = "INSERT INTO answer_type_choice VALUES ()";
		$st = mysql_query($at_sql);
		$answer_type_choice_id = mysql_insert_id();
		$at_sql = sprintf("INSERT INTO atc_choice (answer_type_choice_id, label, score, criteria, weight, mask, default_selected)
							SELECT %d, label, score, criteria, weight, mask, default_selected
								FROM atc_choice
								WHERE answer_type_choice_id = %d",
						$answer_type_choice_id, $new['answer_type_id']);
	} elseif ( $new['answer_type'] == INTEGER || $new['answer_type'] == FLOAT ) {
		$table = $new['answer_type'] == INTEGER ? "answer_type_integer" : "answer_type_float";
		$at_sql = sprintf("INSERT INTO %s (min_value, max_value, default_value, criteria)
								SELECT min_value, max_value, default_value, criteria
									FROM %s 
									WHERE id = %d",
							$table, $table, $new['answer_type_id']);
	} else {
		$at_sql = sprintf("INSERT INTO answer_type_text (min_chars, max_chars, criteria)
								SELECT min_chars, max_chars, criteria
									FROM answer_type_text
									WHERE id = %d", 
							$new['answer_type_id']);
	}
	$st = mysql_query($at_sql);
	if ( $st ) {
		$query_msg .= " and answer is cloned as well";
		$answer_type_id = mysql_insert_id();
	} else {
		$query_msg .= " But error when cloning answer: " . mysql_error();
	}
	$new_at_id = isset($answer_type_choice_id) ? $answer_type_choice_id : $answer_type_id;
	$upd_sql = sprintf("UPDATE survey_indicator SET answer_type_id = %d WHERE id = %d",
					$new_at_id, $id);
	$st = mysql_query($upd_sql);
	$rt = array('id' => $id, 'org_id' => $org_id, 'sql' => $sql, 'query_status' => $query_status, 'query_msg' => $query_msg);
	echo json_encode($rt);
	exit;
}
?>


