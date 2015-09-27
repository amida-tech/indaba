<?php

session_start();
require_once('include/config.inc');
define ('SINGLE', 1);
define ('MULTIPLE', 2);
define ('INTEGER', 3);
define ('FLOAT', 4);
define ('TEXT', 5);

if ( isset($_GET['action']) && $_GET['action'] == 'show_survey_config_list' ) {
	$sql = "SELECT * FROM survey_config ORDER BY name";
	$st = mysql_query($sql);
	$survey_list =  "<div id=survey_config_id_0  onClick='survey_configSelected(this);' ><INPUT style='font-size:14px; text-align:center;color:red;' class=select_input size=25 align=center readonly='readonly' value='Add New Survey Config' /></div>";
	while ( $survey = fetch_html_entities($st) ) {
		$survey_list .= "<div id=survey_config_id_" . $survey['id'] . " onClick='survey_configSelected(this);' ><INPUT class=select_input readonly='readonly' size=25 value='" . $survey['name'] . "' /></div>";
	}
	$rt = array('sql' => $sql, 'survey_config_list' => $survey_list);
	echo json_encode($rt);
	exit;
}
if ( isset($_GET['action']) && $_GET['action'] == 'show_indicator_list' ) {
	$sql = sprintf("SELECT * FROM survey_indicator WHERE id NOT IN 
						( SELECT survey_indicator_id FROM survey_question WHERE survey_config_id = %d) ORDER BY name",
				$_GET['survey_config_id']);
	$st = mysql_query($sql);
	$indicator_list =  "<div >Filter <INPUT type=text id=filter_indicator style='font-size:12px; text-align:left;color:red;' size=15 align=center ' onKeyUp='filter_indicator();' /></div>";
	while ( $indicators = fetch_html_entities($st) ) {
		$indicator_list .= '<div id=indicator_id_' . $indicators['id'] . ' onClick="indicatorSelected(this);" ><INPUT class=select_input readonly="readonly" size=25 value="' . $indicators['name'] . " - " . $indicators['question'] . '" /></div>';
	}
	$rt = array('sql' => $sql, 'indicator_list' => $indicator_list);
	echo json_encode($rt);
	exit;
}


if ( isset($_GET['action']) && $_GET['action'] == 'show_survey_config' ) {
	$id = $_GET['id'];
	if ( $id <= 0 ) {
		$rt = array('name' => '', 'description' => '', 'instructions' => '');
	} else {
		$sql = sprintf("SELECT * FROM survey_config where id = %d ORDER BY name", $id);
		$st = mysql_query($sql);
		$rt = fetch_html_entities($st);
		$rt['sql'] = $sql;
		$rt['query_msg'] = mysql_error();
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_categories' ) {
	$survey_config_id = $_GET['survey_config_id'];
	$rt = show_categories($survey_config_id);
	echo json_encode($rt);
	exit;
}

function show_categories($survey_config_id) {
	$sql = sprintf("SELECT * FROM survey_category WHERE survey_config_id = %d AND ( parent_category_id = 0 OR parent_category_id IS NULL) ORDER BY weight", $survey_config_id);
	$st = mysql_query($sql);
	$cate_list =  "<div id=cate_id_0 style='width:255px;' onClick='categorySelected(this);' > <INPUT style='font-size:14px; text-align:center;color:red;' class=select_input size=25 align=center readonly='readonly' id=add_new_cate value='Add New Category' /></div>";
	while ( $category = fetch_html_entities($st) ) { 
		$cate_list .= "<div id=cate_id_" . $category['id'] . " onClick='categorySelected(this);' ><INPUT class=select_input readonly='readonly' size=25 value='" . $category['name'] . "' /></div>";
	}
	$rt = array('sql' => $sql, 'categories_list' => $cate_list, 'query_msg' => mysql_error());
	return $rt;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_category_by_id' ) {
	$sql = sprintf("SELECT * FROM survey_category WHERE id = %d ORDER BY name", $_GET['category_id']);
	$st = mysql_query($sql);
	$rt = fetch_html_entities($st);
	if ( mysql_num_rows($st) == 0 ) {
		$rt = array('name' => '', 'description' => '', 'label' => '', 'title' => '');
	}
	$rt['sql'] = $sql;
	$rt['query_msg'] = mysql_error();
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_sub_categories' ) {
	// difference between sub and question set is the parent category id
	// so they can be shared
	$parent_category_id = $_GET['category_id'];
	$survey_config_id = $_GET['survey_config_id'];
	$current_type = $_GET['current_type'];
	$rt = show_sub_categories($current_type, $parent_category_id, $survey_config_id);
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'show_question' ) {
	$sql = sprintf("SELECT * FROM survey_question WHERE id = %d ORDER BY name ", $_GET['question_id']);
	$st = mysql_query($sql);
	$question = fetch_html_entities($st);
	// now find the indicator defincation for this question, also get the default answer
	$ind_sql = sprintf("SELECT * FROM survey_indicator WHERE id = %d ORDER BY name ", $question['survey_indicator_id']);
	$st = mysql_query($ind_sql);
	$indicator = fetch_html_entities($st);
	if ( $indicator['answer_type'] == INTEGER ) {
		$ans_sql = "SELECT * FROM answer_type_integer WHERE id = " . $indicator['answer_type_id'];
		$my_sql = "SELECT * FROM answer_object_integer WHERE id = " . $question['default_answer_id'];
	} elseif ( $indicator['answer_type'] == FLOAT ) {
		$ans_sql = "SELECT * FROM answer_type_float WHERE id = " . $indicator['answer_type_id'];
		$my_sql = "SELECT * FROM answer_object_float WHERE id = " . $question['default_answer_id'];
	} elseif ( $indicator['answer_type'] == TEXT ) {
		$ans_sql = "SELECT * FROM answer_type_text WHERE id = " . $indicator['answer_type_id'];
		$my_sql = "";
	} else {
		$ans_sql = sprintf("SELECT * FROM atc_choice WHERE answer_type_choice_id = %d ORDER BY weight", $indicator['answer_type_id']);
		$my_sql = "SELECT * FROM answer_object_choice WHERE id = " . $question['default_answer_id'];
	}
	$st = mysql_query($ans_sql);
	$default_answer = fetch_html_entities($st);
	if ( !empty($question['default_answer_id'] ) ) {
		$st = mysql_query($my_sql);
		$my_answer = fetch_html_entities($st);
	} else {
		$my_answer['value'] = '';
	}
	// construct question editing area
	$question_edit = "<div> Editing Question " . $question['name'] . "</div>";
	$question_edit .= "<INPUT type=hidden id=answer_type value='" . $indicator['answer_type'] . "' />";
	$question_edit .= "<INPUT type=hidden id=answer_id value='" . $question['default_answer_id'] . "' />";

	$question_edit .= "<div style='text-align:left;'>Indicator - " . $indicator['name'] . " - " . $indicator['question'] . "</div>";
	$question_edit .= "<div>Criteria</div>" . "<div><textarea style='font-size:10px;' cols=40 rows=4 readonly=readonly >" . $default_answer['criteria']. "</textarea></div>";
	$question_edit .= "<div class=answer_label style='width:80px;'>Name</div>";
	$question_edit .= "<div ><INPUT class=answer_default style='width:180px;' id=question_name type=text size=10 value='" . $question['name'] . "' /></div>";
	$question_edit .= "<div class=answer_label style='width:80px;'>Label</div>";
	$question_edit .= "<div ><INPUT class=answer_default style='width:180px;' id=question_public_name type=text size=10 value='" . $question['public_name'] . "' /></div>";
	if ( $indicator['answer_type'] == INTEGER || $indicator['answer_type'] == FLOAT ) {
		$question_edit .= "<div class=answer_label style='width:90px;'>Min Value</div>";
		$question_edit .= "<div ><INPUT id=answer_min class=readonly_input readonly=readonly type=text size=4 value='" . $default_answer['min_value'] . "' /></div>";
		$question_edit .= "<div class=answer_label>Max Value</div>";
		$question_edit .= "<div ><INPUT id=answer_max class=readonly_input readonly=readonly type=text size=4 value='" . $default_answer['max_value'] . "' /></div>";
		$question_edit .= "<div class=answer_label style='width:90px;'>Indicator Default</div>";
		$question_edit .= "<div ><INPUT class=readonly_input readonly=readonly type=text size=4 value='" . $default_answer['default_value'] . "' /></div>";
		$question_edit .= "<p><div class=answer_label >My Default</div>";
		$question_edit .= "<div ><INPUT id=answer_default style='width:40px;' type=text size=4 value='" . $my_answer['value'] . "' /></div></p>";
	} elseif ( $indicator['answer_type'] == TEXT ) {
		$question_edit .= "<div class=answer_label>Min Chars</div>";
		$question_edit .= "<div><INPUT id=answer_min_chars class=readonly_input readonly=readonly type=text size=4 value='" . $default_answer['min_chars'] . "' /></div>";
		$question_edit .= "<div class=answer_label>Max Chars</div>";
		$question_edit .= "<div><INPUT id=answer_max_chars class=readonly_input readonly=readonly type=text size=4 value='" . $default_answer['max_chars'] . "' /></div>";
	} else {
		// the answer type is choice, we may have multiple rows from atc_choice table
		// run the query again
		$st = mysql_query($ans_sql);
		if ( mysql_num_rows($st) > 0 ) {
			$question_edit .= "<div style='width:320px; margin-top:10px; '>";
			$question_edit .= "<div style='width:320px; height:15px; float:left; '>";
			$question_edit .= "		<div style='width:110px; height:15px; border: 1px solid black; float:left;  font-weight:bold;'>Label</div>";
			$question_edit .= "		<div style='width:100px; height:15px; border: 1px solid black; float:left;  font-weight:bold;'>Indicator Default</div>";
			$question_edit .= "		<div style='width:100px; height:15px; border: 1px solid black; float:left; font-weight:bold; '>My Default</div>";
			$question_edit .= "  </div>";
			while ( $atc = fetch_html_entities($st) ) {
				$question_edit .= "<div style='width:320px; height:15px; float:left; font-size:10px;'>";
				$question_edit .= "<div style='width:110px; height:15px; border: 1px solid black; float:left; '>" . $atc['label'] . "</div>";
				$is_default = $atc['default_selected'] == 1 ? "Yes" : "No";
				$question_edit .= "<div style='width:100px; height:15px; border: 1px solid black; float:left; '>" 
										. $is_default . "</div>";
				$checked = "";
				// calculate bitmask
				if ( isset($my_answer['choices']) && ( $my_answer['choices']*1 & $atc['mask']*1 ) == $atc['mask']*1 ) {
					$checked =  " checked=checked ";
				}
				if ( $indicator['answer_type'] == SINGLE ) {
					$type = "radio";
					// add a radio button to set No Default
					$nodefault = "<div style='width:212px; height:15px; border: 1px solid black; float:left; '>Check this if there is no default</div>
									<div style='width:100px; height:15px; border: 1px solid black; float:left; '>
										<INPUT type=" . $type . " name=mask id=mask value=0></INPUT></div>";
				} else {
					$type = "checkbox";
					$nodefault = "";
				}
				$question_edit .= "<div style='width:100px; height:15px; border: 1px solid black; float:left; '>
									<INPUT type=" . $type . " name=mask " . $checked . " id=mask value='" . $atc['mask'] . "' >
								</INPUT></div>";
				$question_edit .=  "</div>";
			}
			$question_edit .= $nodefault . "</div>";
		}
	}
	$question_edit .= "</div>";
	$question_edit .= "<div style='width:310px; height:30px;float:left; margin-top:10px;padding-top:20px;'><div style='width:150px; height:30px; float:left;'>
						<INPUT type=button class=btn value='Save' onClick='save_my_answer();' /></div>";
	$question_edit .= "<div style='width:150px; height:30px; float:left;'>
						<INPUT type=button class=btn value='Reset' onClick='return reload_question();' /></div>";

	$rt = array('query_msg' => mysql_error(), 'question_edit' => $question_edit, 'ans_sql' => $ans_sql, 'my_sql' => $my_sql, 'question' => $question );
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'add_indicator_to_question_set' ) {
	// first to get the indicator name
	$sql = "SELECT * FROM survey_indicator where id = " . $_GET['indicator_id'];
	$st = mysql_query($sql);
	$indicator = fetch_html_entities($st);
	// get max weight for this category
	$sql = sprintf("SELECT survey_config_id, max(weight) as weight FROM survey_question WHERE survey_category_id = %d GROUP BY survey_category_id",
			 $_GET['question_set_id']);
	$st = mysql_query($sql);
	$ex_question = fetch_html_entities($st);
	$weight = is_numeric($ex_question['weight']) && $ex_question['weight'] > 0 ? $ex_question['weight'] + 1 : 1;
	// now we should be good, use indicator name as name and public name for now
	$name = $indicator['name'];
	$sql = sprintf("INSERT INTO survey_question (name, survey_config_id, survey_indicator_id, survey_category_id, public_name, weight)
						VALUES ( '%s', %d, %d, %d, '%s', %d)", 
					$name, $_GET['survey_config_id'], $_GET['indicator_id'], $_GET['question_set_id'], $name, $weight);
	$st = mysql_query($sql);
	if ( ! $st ) {
		$rt = array('query_msg' => "Error adding indicator: " . mysql_error(),
					'query_status' => 1,
					'sql' => $sql);
	} else {
		$rt = array('query_msg' => "Indicator added to the question set",
					'query_status' => 0,
					'question_id' => mysql_insert_id(),
					'sql' => $sql);
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'save_question' ) {
	$name = SQLString($_GET['name'], 'text');
	$public_name = SQLString($_GET['public_name'], 'text');
	$question_id = $_GET['question_id'];
	if ( $_GET['answer_type'] == SINGLE || $_GET['answer_type'] == MULTIPLE ) {
		// save name, public_name in survey question, mask in answer_object_choice
		if ( isset($_GET['answer_id']) && is_numeric($_GET['answer_id']) && $_GET['answer_id'] > 0 ) {
			// answer_object_choice record already exist, update
			$aoc_sql = sprintf("UPDATE answer_object_choice SET choices = %d WHERE id = %d",
						$_GET['mask'], $_GET['answer_id']);
			$st = mysql_query($aoc_sql);
			$answer_id = $_GET['answer_id'];
		} else {
			// insert into answer_object_choice
			$aoc_sql = sprintf("INSERT INTO answer_object_choice (choices) VALUES (%d)", $_GET['mask']);
			$st = mysql_query($aoc_sql);
			$answer_id = mysql_insert_id();
		}
	} elseif ( $_GET['answer_type'] == INTEGER ) {
		$answer_default = SQLString($_GET['answer_default'], 'int');
		if ( isset($_GET['answer_id']) && is_numeric($_GET['answer_id']) && $_GET['answer_id'] > 0 ) {
			$aoc_sql = sprintf("UPDATE answer_object_integer SET value = %s WHERE id = %d",
						$answer_default, $_GET['answer_id']);
			$st = mysql_query($aoc_sql);
			$answer_id = $_GET['answer_id']; 
		} else {
			// insert into answer_object_integer
			$aoc_sql = sprintf("INSERT INTO answer_object_integer (value) VALUES (%s)", $answer_default);
			$st = mysql_query($aoc_sql);
			$answer_id = mysql_insert_id();
		}
	} elseif ( $_GET['answer_type'] == FLOAT ) {
		$answer_default = SQLString(isset($_GET['answer_default']) ? $_GET['answer_default'] : null, 'double');
		if ( isset($_GET['answer_id']) && is_numeric($_GET['answer_id'])  && $_GET['answer_id'] > 0) {
			$aoc_sql = sprintf("UPDATE answer_object_float SET value = %s WHERE id = %d",
						$answer_default, $_GET['answer_id']);
			$st = mysql_query($aoc_sql);
			$answer_id = $_GET['answer_id'];
		} else {
			$aoc_sql = sprintf("INSERT INTO answer_object_float (value) VALUES (%s)", $answer_default);
			$st = mysql_query($aoc_sql);
			$answer_id = mysql_insert_id();
		}
	} elseif ( $_GET['answer_type'] == TEXT ) {
		// for text type, no default value, set answer_id 0
		$answer_id = "NULL";
	}
	// now update survey_question table
	$sq_sql = sprintf("UPDATE survey_question 
						SET name = %s, public_name = %s, default_answer_id = %d
						WHERE id = %d",
					$name, $public_name, $answer_id, $question_id);
	$st = mysql_query($sq_sql);
	$rt = array('sq_sql' => $sq_sql, 'aoc_sql' => $aoc_sql);
	if ( ! $st ) { 
		$rt['query_msg'] = "Error saving answer: " . mysql_error();
		$rt['query_status'] = 1;
	} else {
		$rt['query_msg'] = "Answer saved";
		$rt['query_status'] = 0;
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'delete_question' ) {
	$sql = "DELETE FROM survey_question WHERE id = " . $_GET['question_id'];
	$st = mysql_query($sql);
	if ( ! $st ) {
		$rt = array('query_msg' => 'Error deleting question: ' . mysql_error(),
					'query_status' => 1);
	} else {
		$rt = array('query_msg' => 'question deleted',
					'query_status' => 0);
	}
	$rt['sql'] = $sql;
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'move_question' ) {
	$chk_sql = "SELECT * FROM survey_question WHERE id = " . $_GET['question_id'];
	$st = mysql_query($chk_sql);
	$selected = fetch_html_entities($st);
	if (  $_GET['direction'] == 'up' ) {
		$new_sql = sprintf("SELECT * FROM survey_question WHERE survey_category_id = %d AND weight < %d ORDER BY weight DESC LIMIT 1",
					$_GET['question_set_id'], $selected['weight']);
	} else {
		$new_sql = sprintf("SELECT * FROM survey_question WHERE survey_category_id = %d AND weight > %d ORDER BY weight LIMIT 1",
					$_GET['question_set_id'], $selected['weight']);
	}
	$st = mysql_query($new_sql);
	$rt = array('chk_sql' => $chk_sql, 'new_sql' => $new_sql);
	if ( mysql_num_rows($st) == 0 ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Question is already at edge, cannot move";
		echo json_encode($rt);
		exit;
	}
	$new = fetch_html_entities($st);
	$upd_sql = sprintf("UPDATE survey_question SET weight = %d WHERE id = %d", $selected['weight'], $new['id']);
	$st = mysql_query($upd_sql);
	$upd_sql = sprintf("UPDATE survey_question SET weight = %d WHERE id = %d", $new['weight'], $selected['id']);
	$st = mysql_query($upd_sql);
	$rt['upd_sql'] = $upd_sql;
	$rt['query_status'] = 0;
	$rt['query_msg'] = "Question moved";
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'move_question_set' ) {
	$chk_sql = "SELECT * FROM survey_category WHERE id = " . $_GET['question_set_id'];
	$st = mysql_query($chk_sql);
	$selected = fetch_html_entities($st);
	if (  $_GET['direction'] == 'up' ) {
		$new_sql = sprintf("SELECT * FROM survey_category WHERE parent_category_id = %d AND weight < %d ORDER BY weight DESC LIMIT 1",
					$selected['parent_category_id'], $selected['weight']);
	} else {
		$new_sql = sprintf("SELECT * FROM survey_category WHERE parent_category_id = %d AND weight > %d ORDER BY weight LIMIT 1",
					$selected['parent_category_id'], $selected['weight']);
	}
	$st = mysql_query($new_sql);
	$rt = array('chk_sql' => $chk_sql, 'new_sql' => $new_sql);
	if ( mysql_num_rows($st) == 0 ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Question set is already at edge, cannot move";
		echo json_encode($rt);
		exit;
	}
	$new = fetch_html_entities($st);
	$upd_sql = sprintf("UPDATE survey_category SET weight = %d WHERE id = %d", $selected['weight'], $new['id']);
	$st = mysql_query($upd_sql);
	$upd_sql = sprintf("UPDATE survey_category SET weight = %d WHERE id = %d", $new['weight'], $selected['id']);
	$st = mysql_query($upd_sql);
	$rt['upd_sql'] = $upd_sql;
	$rt['query_status'] = 0;
	$rt['query_msg'] = "Question moved";
	// now get the sub category list refreshed
	$sub_rt = show_sub_categories("Sub Category", $selected['parent_category_id'], $selected['survey_config_id'] );
	$rt['question_set_list'] = $sub_rt['sub_list'];
	$rt['parent_category_id'] = $selected['parent_category_id'];
	$rt['sub_sql'] = $sub_rt['sql'];
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'move_sub_category' ) {
	$chk_sql = "SELECT * FROM survey_category WHERE id = " . $_GET['sub_category_id'];
	$st = mysql_query($chk_sql);
	$selected = fetch_html_entities($st);
	if (  $_GET['direction'] == 'up' ) {
		$new_sql = sprintf("SELECT * FROM survey_category WHERE parent_category_id = %d AND weight < %d ORDER BY weight DESC LIMIT 1",
					$selected['parent_category_id'], $selected['weight']);
	} else {
		$new_sql = sprintf("SELECT * FROM survey_category WHERE parent_category_id = %d AND weight > %d ORDER BY weight LIMIT 1",
					$selected['parent_category_id'], $selected['weight']);
	}
	$st = mysql_query($new_sql);
	$rt = array('chk_sql' => $chk_sql, 'new_sql' => $new_sql);
	if ( mysql_num_rows($st) == 0 ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Sub Category is already at edge, cannot move";
		echo json_encode($rt);
		exit;
	}
	$new = fetch_html_entities($st);
	$upd_sql = sprintf("UPDATE survey_category SET weight = %d WHERE id = %d", $selected['weight'], $new['id']);
	$st = mysql_query($upd_sql);
	$upd_sql = sprintf("UPDATE survey_category SET weight = %d WHERE id = %d", $new['weight'], $selected['id']);
	$st = mysql_query($upd_sql);
	$rt['upd_sql'] = $upd_sql;
	$rt['query_status'] = 0;
	$rt['query_msg'] = "Sub Category moved";
	$sub_rt = show_sub_categories("Category", $selected['parent_category_id'], $selected['survey_config_id'] );
	$rt['sub_category_list'] = $sub_rt['sub_list'];
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'move_category' ) {
	$chk_sql = "SELECT * FROM survey_category WHERE id = " . $_GET['category_id'];
	$st = mysql_query($chk_sql);
	$selected = fetch_html_entities($st);
	if (  $_GET['direction'] == 'up' ) {
		$new_sql = sprintf("SELECT * FROM survey_category 
						WHERE (parent_category_id = 0 OR parent_category_id IS NULL) 
							AND weight < %d 
							AND survey_config_id = %d
						ORDER BY weight DESC LIMIT 1",
					 $selected['weight'], $_GET['survey_config_id']);
	} else {
		$new_sql = sprintf("SELECT * FROM survey_category 
						WHERE (parent_category_id = 0 OR parent_category_id IS NULL) 
							AND weight > %d 
							AND survey_config_id = %d
						ORDER BY weight LIMIT 1",
					$selected['weight'], $_GET['survey_config_id']);
	}
	$st = mysql_query($new_sql);
	$rt = array('chk_sql' => $chk_sql, 'new_sql' => $new_sql);
	if ( mysql_num_rows($st) == 0 ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Category is already at edge, cannot move";
		echo json_encode($rt);
		exit;
	}
	$new = fetch_html_entities($st);
	$upd_sql = sprintf("UPDATE survey_category SET weight = %d WHERE id = %d", $selected['weight'], $new['id']);
	$st = mysql_query($upd_sql);
	$upd_sql = sprintf("UPDATE survey_category SET weight = %d WHERE id = %d", $new['weight'], $selected['id']);
	$st = mysql_query($upd_sql);
	$rt['upd_sql'] = $upd_sql;
	$rt['query_status'] = 0;
	$rt['query_msg'] = "Category moved";
	$sub_rt =  show_categories($_GET['survey_config_id']);
	$rt['category_list'] = $sub_rt['categories_list'];
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'save_category' ) {
	$name = SQLString($_GET['name'], 'text');
	$description = SQLString($_GET['description'], 'text');
	$label = SQLString($_GET['label'], 'text');
	$title = isset($_GET['title']) ? $_GET['title'] : NULL;
	$title = SQLString(urldecode($title), 'text');
	$parent_category_id = $_GET['parent_category_id'];
	$survey_config_id = $_GET['survey_config_id'];
	$id = $_GET['id'];
	// just in case parent_category_id is NULL
	$parent_cluse = ' parent_category_id = ' . $parent_category_id ;
	if ( empty($parent_category_id ) || $parent_category_id == 0 ) {
		$parent_cluse = " ( parent_category_id IS NULL OR parent_category_id = 0 )";
		// however, we want to use 0 in the future
		$parent_category_id = 0;
	}

	$chk_sql = sprintf("SELECT max(weight) as weight FROM survey_category 
							WHERE survey_config_id = %d AND %s
							GROUP BY parent_category_id",
					$survey_config_id, $parent_cluse);
	$st = mysql_query($chk_sql);
	$ext_row = fetch_html_entities($st);
	$weight = (isset($ext_row['weight']) && !empty($ext_row['weight']))? $ext_row['weight'] + 1 : 1;

	if ( $id == 0 ) {
		// insert, need to get max weight for same survey_config_id and parent_category_id
		$ins_sql = sprintf("INSERT INTO survey_category (survey_config_id, parent_category_id, name, description, label, title, weight)
								VALUES ( %d, %d, %s, %s, %s, %s, %d)",
						$survey_config_id, $parent_category_id, $name, $description, $label, $title, $weight);
		$st = mysql_query($ins_sql);
		if ( ! $st ) {
			$rt = array('chk_sql' => $chk_sql, 
						'ins_sql' => $ins_sql, 
						'query_status' => 1, 
						'query_msg' => 'Error saving item:' . mysql_error());
			echo json_encode($rt);
			exit;
		}
		$rt = array('chk_sql' => $chk_sql,
					'ins_sql' => $ins_sql, 
					'query_status' => 0,
					'query_msg' => 'Item saved',
					'category_id'	=> mysql_insert_id(),
					'parent_category_id' => $parent_category_id);
		echo json_encode($rt);
		exit;
	}
	// now update
	$upd_sql = sprintf("UPDATE survey_category
							SET name = %s, description = %s, label = %s, title = %s
							WHERE id = %d",
						$name, $description, $label, $title, $id);
	$st = mysql_query($upd_sql);
	if ( ! $st ) {
		$rt = array('chk_sql' => $chk_sql, 
					'upd_sql' => $upd_sql, 
					'query_status' => 1, 
					'query_msg' => 'Error saving item:' . mysql_error());
	} else {
		$rt = array('chk_sql' => $chk_sql, 
					'upd_sql' => $upd_sql, 
					'query_status' => 0,
					'query_msg' => 'Item saved',
					'category_id'   => $id,
					'parent_category_id' => $parent_category_id);
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'save_survey_config' ) {
	$name = SQLString($_GET['name'], 'text');
	$description = SQLString($_GET['description'], 'text');
	$moe = $_GET['moe'];
	$id = $_GET['id'];
	$instructions = urldecode($_GET['instructions']);
	$instructions = SQLString($instructions, 'text');
	if ( $id == 0 ) {
		// insert
		$sql = sprintf("INSERT INTO survey_config (name, description, instructions, moe_algorithm)
							VALUES (%s, %s, %s, %d)",
					$name, $description, $instructions, $moe);
	} else {
		//update
		$sql = sprintf("UPDATE survey_config SET name = %s, description = %s, instructions = %s, moe_algorithm = %d
							WHERE id = %d",
					$name, $description, $instructions, $moe, $id);
	}
	$st = mysql_query($sql);
	$rt = array('sql' => $sql);
	if ( ! $st ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Error saving survey config: " . mysql_error();
	} else {
		$rt['query_status'] = 0;
		$rt['query_msg'] = "Survey Config saved";
		$new_id = mysql_insert_id();
		$rt['id'] = isset($new_id) && $new_id > 0 ? $new_id : $id;
	}
	echo json_encode($rt);
	exit;
}

if ( isset($_GET['action']) && $_GET['action'] == 'delete_survey_config' ) {
	// check whether it is used in product -- content_type = 0 and product_config_id = $id
	$id = $_GET['id'];
	$chk_sql = "SELECT * FROM product where content_type = 0 AND product_config_id = " . $id;
	$st = mysql_query($chk_sql);
	if ( mysql_num_rows($st) > 0 ) {
		// it is used, get all the product names
		$msg = "This survey config is used by product:";
		while ( $products = fetch_html_entities($st) ) {
			$msg .= " " . $products['name'];
		}
		$msg .= ". Please unassign the config from the products before delete.";
		echo json_encode(array('query_status' => 1, 'query_msg' => $msg) );
		exit;
	} else {
		$del_sql = "DELETE FROM survey_config, survey_category, survey_question
						USING survey_config  INNER JOIN (survey_category, survey_question)
						ON (survey_config.id = survey_category.survey_config_id
							AND (  survey_category.id = survey_question.survey_category_id
								OR survey_config.id = survey_question.survey_config_id) )
						WHERE survey_config.id = " . $id;
		$st = mysql_query($del_sql);
		$rt = array('chk_sql' => $chk_sql, 'del_sql' => $del_sql);
		if ( ! $st ) {
			$rt['query_msg'] = "Error deleting survey config: " . mysql_error();
			$rt['query_status'] = 1;
		} else {
			$rt['query_msg'] = "Survey Config deleted";
			$rt['query_status'] = 0;
		}
		echo json_encode($rt);
		exit;
	}
}

if ( isset($_GET['action']) && $_GET['action'] == 'delete_category' ) {
	// no check
	// get all categories under in the tree
	$sql = sprintf("SELECT distinct s1.id FROM survey_category s1 INNER JOIN ( survey_category s2, survey_category s3 )
						ON ( 
							( s1.id = %d  ) 
							OR ( s1.parent_category_id = s2.id AND s2.id = %d)
							OR ( s1.parent_category_id = s2.id AND s2.parent_category_id = s3.id AND s3.id = %d) 
							)
						ORDER BY s1.id",
		$_GET['id'], $_GET['id'], $_GET['id']);
	$st = mysql_query($sql);
	$in_list = "(";
	while ( $row = fetch_html_entities($st) ) {
		// build the IN list
		$in_list .= $row['id'] . ',';
	}
	// remove the trailing ',', replace by closing )
	$in_list = preg_replace("/,$/", ')', $in_list);

	$sql = sprintf("DELETE FROM survey_category WHERE id IN %s", $in_list);
	$st = mysql_query($sql);
	$sql = sprintf("DELETE FROM survey_question WHERE survey_category_id IN %s", $in_list);
	$st = mysql_query($sql);
	$rt = array('sql' => $sql);
	if ( ! $st ) {
		$rt['query_status'] = 1;
		$rt['query_msg'] = "Error deleting category: " . mysql_error();
	} else {
		$rt['query_status'] = 0;
		$rt['query_msg'] = "Category deleted";
	}
	echo json_encode($rt);
	exit;
}

function show_sub_categories($current_type, $parent_category_id, $survey_config_id) {
	if ( $current_type == 'Category' ) {
		// show Sub Category list
		$id_name = "sub_category_id_";
		$func_name = "sub_categorySelected";
		$sub_list = "<div id=sub_category_id_0 style='width:255px;' onClick='" . $func_name . "(this);' ><INPUT style='font-size:14px; text-align:center;color:red;' id=add_new_sub class=select_input size=25 align=center readonly='readonly' value='Add New Sub Category' /></div>";
	} elseif ( $current_type == 'Sub Category' ) { 
		// show Question Set list
		$id_name = "question_set_id_";
		$func_name = "question_setSelected";
		$sub_list = "<div id=" . $id_name . "0 onClick='" . $func_name . "(this);' ><INPUT style='font-size:14px; text-align:center;color:red;' id=add_new_set class=select_input size=25 align=center readonly='readonly' value='Add New Question Set' /></div>";
	} else {
		// show Questions list
		$id_name = "question_id_";
		$func_name = "questionSelected";
		$sub_list = "";
		$sql = sprintf("SELECT * FROM survey_question WHERE survey_category_id = %d ORDER by weight", $parent_category_id);
		$st = mysql_query($sql);
		while ( $questions = fetch_html_entities($st) ) {
			$sub_list .= "<div id=" . $id_name . $questions['id'] . " onClick='" . $func_name . "(this);' >
				<INPUT class=select_input id=q_" . $id_name . $questions['id'] . " readonly='readonly' size=25 value='" . $questions['name'] . "' /></div>";
		}
		$rt = array('sql' => $sql, 'query_msg' => mysql_error(), 'sub_list' => $sub_list);
		echo json_encode($rt);
		exit;
	}

	$sql = sprintf("SELECT * FROM survey_category WHERE survey_config_id = %d AND parent_category_id = %d ORDER BY weight", 
				$survey_config_id, $parent_category_id);
	$st = mysql_query($sql);
	while ( $sub = fetch_html_entities($st) ) {
		$sub_list .= "<div id=" . $id_name . $sub['id'] . " onClick='" . $func_name . "(this);' ><INPUT class=select_input readonly='readonly' size=25 value='" . $sub['name'] . "' /></div>";
	}
	$rt = array('sql' => $sql, 'sub_list' => $sub_list, 'query_msg' => mysql_error());
	return $rt;
}


?>
