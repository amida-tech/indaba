<?php
require_once('header.php');
?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script language="javascript"  type="text/javascript">

String.prototype.trim = function() {
	    return this.replace(/^\s+|\s+$/g,"");
}

$(document).ready(function() {
	// hide the indicator box
	$("#questions_box").css("display", "none");
	get_all_survey_config();
});

function show_survey_config_box() {
	$("#survey_clip").css("display","inline");
	$("#survey_expand").css("display","none");
	$("#survey_config_box").css("display","inline");
}
function hide_survey_config_box() {
	$("#survey_clip").css("display","none");
	$("#survey_expand").css("display","inline");
	$("#survey_config_box").css("display","none");
}
function show_category_box() {
	$("#category_clip").css("display","inline");
	$("#category_expand").css("display","none");
	$("#categories_box").css("display","inline");
}
function hide_category_box() {
	$("#category_clip").css("display","none");
	$("#category_expand").css("display","inline");
	$("#categories_box").css("display","none");
}
function show_indicator_box() {
	$("#indicator_clip").css("display","inline");
	$("#indicator_expand").css("display","none");
	$("#questions_box").css("display","inline");
}
function hide_indicator_box() {
	$("#indicator_clip").css("display","none");
	$("#indicator_expand").css("display","inline");
	$("#questions_box").css("display","none");
}
function clean_questions() {
	$("#selected_question_id").val('');
	$("#questions").html('');
	$("#question_edit").html('');
	$("#indicators").html('');
	$("#selected_indicator_id").val('');
}
function clean_question_set() {
	$("#selected_question_set_id").val('');
	$("#question_sets").html('');
	clean_questions();
}
function clean_sub_category() {
	$("#selected_sub_category_id").val('');
	$("#sub_categories").html('');
	clean_question_set();
	// also clear edit area
	$("#category_name").val('');
	$("#category_description").val('');
	$("#category_label").val('');
	$("#category_title").val('');
	$("#cat_current").html('');
	$("#category_edit_box").css("display", "none");
}
function filter_indicator() {
	var name_like = $("#filter_indicator").val();
	$("#indicators").children().each( function() {
		if ( typeof($(this).children('.select_input').val()) != 'undefined' ) {
			if ( $(this).children('.select_input').val().search(new RegExp(name_like, 'i')) < 0 ) {
				$(this).hide();
			} else {
				$(this).show();
			}
		}
	});
}


function get_all_survey_config() {
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		url:			'survey_functions.php',
		data:			'action=show_survey_config_list',
		success:		function(obj) {
			$("#survey_config_id").html(obj.survey_config_list);
		}
	});
}

function get_all_indicators(survey_config_id) {
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		url:			'survey_functions.php',
		data:			'action=show_indicator_list&survey_config_id=' + survey_config_id,
		success:		function(obj) {
			$("#indicators").html(obj.indicator_list);
		}
	});
}


function survey_configSelected(id) {
	$("#survey_config_id").find('input').each( function() {
		$(this).css('background-color', '#ccc');
	});
	// set back ground for selected row
	$(id).children().css('background-color', 'yellow');
	var survey_config_id = $(id).attr('id').replace(/^survey_config_id_/g, '');
	$("#selected_survey_config_id").val(survey_config_id);
	show_survey_config_by_id(survey_config_id);
	clean_sub_category();
}

function show_survey_config_by_id (id) {
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		url:			'survey_functions.php',
		data:			'action=show_survey_config&id=' + id,
		success:		function(obj) {
			$("#status_area").html(obj.query_msg);
			$("#survey_config_name").val(obj.name);
			$("#survey_config_description").val(obj.description);
			$("#survey_config_instructions").val(obj.instructions);
			if ( obj.id > 0 ) {
				show_categories(obj.id);
				//if ( $("#current_type").val() == "Category" && $("#selected_category_id").val() != '' ) {
				//	var cate_id = "#cate_id_" + $("#selected_category_id").val();
				//	categorySelected($(cate_id));
				//}
				$("#current_type").val('survey_config');
				$("#selected_survey_config_id").val(obj.id);
				$("#moe").val(obj.moe_algorithm);
				show_category_box();
			} else {
				$("#categories").html('');
			}
		}
	});
}

function show_categories(survey_config_id, category_id) {
	$.ajax({
		type:           'GET',
		cache:          'false',
		async:          'false',
		dataType:       'json',
		url:            'survey_functions.php',
		data:           'action=show_categories&survey_config_id=' + survey_config_id,
		success:        function(obj) {
			$("#status_area").html(obj.query_msg);
			$("#categories").html(obj.categories_list);
			if ( category_id ) {
				categorySelected($("#cate_id_" + category_id));
			}
		}
	});
}

function categorySelected(id) {
	$("#categories").find('input').each( function() {
		$(this).css('background-color', '#ccc');
	});
	// set background for selected
	var category_id = $(id).attr('id').replace(/^cate_id_/g, '');
	$("#selected_category_id").val(category_id);
	$("#cat_current").html("Editing Category");
	$("#current_type").val('Category');
	show_category_by_id(category_id);
	$(id).children().css('background-color', 'yellow');
	if ( category_id > 0 ) {
		show_sub_categories(category_id);
		$("#selected_category_id").val(category_id);
	} else {
		$("#sub_categories").html('');
	}
	clean_question_set();
}

function show_category_by_id(id) {
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=show_category_by_id&category_id=' + id,
		success:	function(obj) {
			$("#category_name").val(obj.name);
			$("#category_description").val(obj.description);
			$("#category_label").val(obj.label);
			$("#category_title").val(obj.title);
			$("#category_edit_box").css("display", "inline");
		}
	});
}

function show_sub_categories(category_id, sub_id) {
	$.ajax({
		type:           'GET',
		cache:          'false',
		dataType:       'json',
		url:            'survey_functions.php',
		data:           'action=show_sub_categories&category_id=' + category_id + 
							"&survey_config_id=" + $("#selected_survey_config_id").val() +
							'&current_type=' + $("#current_type").val(),
		success:        function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( $("#current_type").val() == 'Category' ) {
				$("#sub_categories").html(obj.sub_list);
			} else if ( $("#current_type").val() == 'Sub Category' ) {
				$("#question_sets").html(obj.sub_list);
			} else {
				$("#questions").html(obj.sub_list);
				show_indicator_box();
				if ( typeof(sub_id) != 'undefined' ) {
					var q_id = "#" + $(sub_id).attr('id');
					questionSelected($(q_id));
				}
			}
		}
	});
}

function sub_categorySelected(id) {
	$("#sub_categories").find('input').each( function() {
		$(this).css('background-color', '#ccc');
	});
	// set background for selected
	$(id).children().css('background-color', 'yellow');
	var sub_category_id = $(id).attr('id').replace(/^sub_category_id_/g, '');
	$("#selected_sub_category_id").val(sub_category_id);
	show_category_by_id(sub_category_id);
	$("#cat_current").html("Editing Sub Category");
	$("#current_type").val('Sub Category');
	if ( sub_category_id > 0 ) {
		show_sub_categories(sub_category_id);
	} else {
		$("#question_sets").html('');
	}
	// clean up question and question edit area
	clean_questions();
}

function question_setSelected(id, q_id) {
	$("#question_sets").find('input').each( function() {
		$(this).css('background-color', '#ccc');
	});
	// set background for selected
	$(id).children().css('background-color', 'yellow');
	var question_set_id = $(id).attr('id').replace(/^question_set_id_/g, '');
	$("#selected_question_set_id").val(question_set_id);
	$("#cat_current").html("Editing Question Set");
	$("#current_type").val('Question Set');
	hide_survey_config_box();
	show_category_by_id(question_set_id);
	if ( question_set_id > 0 ) {
		show_sub_categories(question_set_id, q_id);
		get_all_indicators($("#selected_survey_config_id").val());
		show_indicator_box();
	} else {
		$("#questions").html('');
		hide_indicator_box();
	}

}

function indicatorSelected(id) {
	$("#indicators").find('input').each( function() {
		 $(this).css('background-color', '#ccc');
	});
	$(id).children().css('background-color', 'yellow');
	var indicator_id = $(id).attr('id').replace(/^indicator_id_/g, '');
	$("#selected_indicator_id").val(indicator_id);
}

function questionSelected(id) {
	$("#questions").find('input').each( function() {
		$(this).css('background-color', '#ccc');
	});
	if ( typeof( $(id).attr('id') ) == 'undefined' ) {
		var question_id = id.replace(/^question_id_/g, '');
		var q_id = "#" + id;
	} else {
		var question_id = $(id).attr('id').replace(/^question_id_/g, '');
		q_id = id;
	}
	$("#selected_question_id").val(question_id);
	$(q_id).children().css("background-color", "yellow");
	show_question_by_id(question_id);
}
function show_question_by_id(question_id) {
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=show_question&question_id=' + question_id,
		success:	function(obj) {
			$("#status_area").html(obj.query_msg);
			$("#question_edit").html(obj.question_edit);
		}
	});
}

function add_indicator() {
	// add indicator to question set
	// alert("Adding indicator " + $("#selected_indicator_id").val() + " To Question Set " + $("#selected_question_set_id").val() ) ;
	if ( $("#selected_indicator_id").val() == '' ) {
		return;
	}
	var question_set_id = "question_set_id_" + $("#selected_question_set_id").val();
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=add_indicator_to_question_set&indicator_id=' + $("#selected_indicator_id").val() +
						'&question_set_id=' + $("#selected_question_set_id").val() + 
						'&survey_config_id=' + $("#selected_survey_config_id").val(),
		success:	function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				//get_all_indicators($("#selected_question_set_id").val());
				var qs_id = "#" + question_set_id;
				var q_id = "#question_id_" + obj.question_id;
				question_setSelected($(qs_id), q_id);
			}
		}
	});
}

function save_my_answer() {
	var mask=0;
	$("#question_edit").find("input:checked").each( function() {
		mask = mask + parseInt($(this).val());
	});
	// Check name, public_name, and answer_default
	var name = $("#question_name").val().trim();
	if ( name.length <= 0 ) {
		alert("Please enter a name for this question");
		return;
	}
	var public_name = $("#question_public_name").val().trim();
	if ( public_name.length <= 0 ) {
		alert("Please enter a public name for this question");
		return;
	}
	var answer_type = $("#answer_type").val();
	var q_string = "&question_id=" + $("#selected_question_id").val() + 
				"&answer_id=" + $("#answer_id").val() + "&answer_type=" + answer_type +
				"&name=" + name + "&public_name=" + public_name + "&mask=" + mask;
	// check if a default answer is set, which only apply to type 3 and 4
	if ( answer_type == 3 || answer_type == 4 ) {
		var answer_default = $("#answer_default").val();
		if ( typeof($("#answer_default").val()) != "undefined" && answer_default.trim() != '' ) {
			var min = parseFloat($("#answer_min").val());
			var max = parseFloat($("#answer_max").val());
			if ( isNaN( answer_default ) || answer_default < min || answer_default > max ) {
				alert("Please enter the default answer as numeric and fit in between of Min and Max");
				return;
			}
			q_string += "&answer_default=" + answer_default;
		}
	}
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=save_question' + q_string,
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				// reload question list
				var qs_id = "question_set_id_" + $("#selected_question_set_id").val();
				var q_id = "question_id_" + $("#selected_question_id").val();
				question_setSelected($("#" + qs_id), $("#" + q_id));
				alert("Question saved");
			}
			$("#status_area").html(obj.query_msg);
		}
	});
}
function delete_question() {
	if ( $("#selected_question_id").val() == '' ) {
		return;
	}
	var go=confirm("Are you sure you want to delete this question?");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=delete_question&question_set_id=' + 
						$("#selected_question_set_id").val() + '&question_id=' + $("#selected_question_id").val(),
		success:	function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				var q_id = "question_set_id_" + $("#selected_question_set_id").val();
				question_setSelected($("#" + q_id));
				$("#question_edit").html('');
			}
		}
	});
}
function reload_question() {
	if ( $("#selected_question_id").val() == '' ) {
		return;
	}
	var question_id = "#question_id_" + $("#selected_question_id").val();
	questionSelected($(question_id));
}
function move_question(direction) {
	var question_id = $("#selected_question_id").val();
	if ( question_id == '' ) {
		return;
	}
	var question_set_id = $("#selected_question_set_id").val();
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=move_question&direction=' + direction + '&question_id=' + question_id + '&question_set_id=' + question_set_id,
		success:	function(obj) {
			if ( obj.query_status == 1 ) {
				alert(obj.query_msg);
			} else {
				var qs_id = "question_set_id_" + question_set_id;
				var q_id = "question_id_" + question_id;
				question_setSelected($("#" + qs_id), $("#" + q_id));
			}
		}
	});
}
function move_question_set(direction) {
	// var question_id = $("#selected_question_id").val();
	var question_set_id = $("#selected_question_set_id").val();
	if ( question_set_id == '' ) {
		return;
	}
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=move_question_set&direction=' + direction + '&question_set_id=' + question_set_id,
		success:	function(obj) {
			if ( obj.query_status == 1 ) {
				alert(obj.query_msg);
			} else {
				var qs_id = "#question_set_id_" + question_set_id;
				var sub_id = "#sub_category_id_" + $("#selected_sub_category_id").val();
				$("#question_sets").html(obj.question_set_list);
				$(qs_id).children().css('background-color', 'yellow');
			}
		}
	});
}
function move_sub_category(direction) {
	var sub_category_id = $("#selected_sub_category_id").val();
	if ( sub_category_id == '' ) {
		return;
	}
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=move_sub_category&direction=' + direction + '&sub_category_id=' + sub_category_id,
		success:	function(obj) {
			if ( obj.query_status == 1 ) {
				alert(obj.query_msg);
			} else {
				var sc_id = "#sub_category_id_" + sub_category_id;
				var cate_id = "#cate_id_" + $("#selected_category_id").val();
				//categorySelected($(cate_id));
				$("#sub_categories").html(obj.sub_category_list);
				$(sc_id).children().css('background-color', 'yellow');
			}
		}
	});
}
function move_category(direction) {
	var category_id = $("#selected_category_id").val();
	if ( category_id == '' ) {
		return;
	}
	var config_id = "#survey_config_id_" + $("#selected_survey_config_id").val();
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'survey_functions.php',
		data:		'action=move_category&direction=' + direction + '&category_id=' + category_id +
						 '&survey_config_id=' + $("#selected_survey_config_id").val(),
		success:	function(obj) {
			if ( obj.query_status == 1 ) {
				alert(obj.query_msg);
			} else {
				//survey_configSelected($(config_id));
				$("#categories").html(obj.category_list);
				$("#cate_id_"+category_id).children().css('background-color', 'yellow');
			}
		}
	});
}

function save_category() {
	var survey_config_id = $("#selected_survey_config_id").val();
	var name = $("#category_name").val().trim();
	var current_type = $("#current_type").val();
	if ( name.length == 0 ) {
		alert("Please enter a name for this " + current_type );
		return;
	}
	var description = $("#category_description").val().trim();
	if ( description.length == 0 ) {
		alert("Please enter a description for this " + current_type );
		return;
	}
	var label = $("#category_label").val();
	if ( label.length == 0 ) {
		alert("Please enter a label for this " + current_type );
		return;
	}
	var title = encodeURIComponent($("#category_title").val().trim());
	var parent_id;
	var id;
	if ( current_type == "Category" ) {
		parent_category_id = 0;
		id = $("#selected_category_id").val();
	} else if ( current_type == "Sub Category" ) {
		parent_category_id = $("#selected_category_id").val();
		id = $("#selected_sub_category_id").val();
	} else {
		parent_category_id = $("#selected_sub_category_id").val();
		id = $("#selected_question_set_id").val();
	}
	var q_string = '&id=' + id + '&survey_config_id=' + survey_config_id +
					'&parent_category_id=' + parent_category_id +
					'&name=' + name + '&description=' + description +
					'&label=' + label + '&title=' + title + "&current_type=" + current_type;
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		async:          'false',
		url:		'survey_functions.php',
		data:		'action=save_category' + q_string,
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				// always return inserted/updated category_id and parent category_id
				if ( current_type == "Category" ) {
					$("#selected_category_id").val(obj.category_id);
					alert(current_type + " saved");
					show_categories(survey_config_id, obj.category_id);
				} else if ( current_type == "Sub Category" ) {
					categorySelected($("#cate_id_" + obj.parent_category_id));
					alert(current_type + " saved");
					sub_categorySelected($("#sub_category_id_" + obj.category_id));
				} else {
					sub_categorySelected($("#sub_category_id_" + obj.parent_category_id));
					alert(current_type + " saved");
					question_setSelected($("#question_set_id_" + obj.category_id));
				}
			}
			$("#status_area").html(obj.query_msg);
		}
	});
}

function save_survey_config() {
	if ( $("#selected_survey_config_id").val() == '' ) {
		return;
	}
	var name = $("#survey_config_name").val().trim();
	if ( name.length == 0 ) {
		alert("Please enter a name for the Survey Config");
		return;
	}
	var description = $("#survey_config_description").val().trim();
	var instructions = $("#survey_config_instructions").val().trim();
	if ( instructions.length == 0 ) {
		alert("Please enter instructions for the Survey Config");
		return;
	}
	instructions = encodeURIComponent(instructions);
	var id = $("#selected_survey_config_id").val();
	var q_string = '&id=' + id + '&name=' + name + '&description=' + description + '&instructions=' + instructions + '&moe=' + $('#moe').val();
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		data:		'action=save_survey_config' + q_string,
		url:		'survey_functions.php',
		success:	function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				get_all_survey_config();
				var survey_id = "#survey_config_id_" + obj.id;
				alert("Survey Config saved");
				survey_configSelected(survey_id);
			}
		}
	});
}

function del_survey_config() {
	var id = $("#selected_survey_config_id").val();
	if ( id == 0 ) {
		return;
	}
	var go = confirm("Are you sure you want to delete the Survey Config? All categories/question sets will be deleted as well!");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:		'GET',
		dataType:	'json',
		cache:		'false',
		url:		'survey_functions.php',
		data:		'action=delete_survey_config&id=' + id,
		success:	function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				get_all_survey_config();
				$("#survey_config_name").val('');
				$("#survey_config_description").val('');
				$("#survey_config_instructions").val('');
				$("#selected_survey_config_id").val('');
				$("#categories").html('');
			}
		}
	})
}
function reload_survey_config() {
	var survey_config_id = "#survey_config_id_" + $("#selected_survey_config_id").val();
	survey_configSelected($(survey_config_id));
}

function delete_category() {
	var current_type = $("#current_type").val();
	var id;
	if ( current_type == "Category" ) {
		id = $("#selected_category_id").val();
		parent_id = $("#selected_survey_config_id").val();
	} else if ( current_type == "Sub Category") {
		id = $("#selected_sub_category_id").val();
		parent_id = $("#selected_category_id").val();
	} else {
		id = $("#selected_question_set_id").val();
		parent_id = $("#selected_sub_category_id").val();
	}
	if ( id == 0 ) {
		return;
	}
	var go = confirm("Are you sure you want to delete the " + current_type + "? All child items will be deleted as well!");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:		'GET',
		dataType:	'json',
		cache:		'false',
		url:		'survey_functions.php',
		data:		'action=delete_category&id=' + id,
		success:	function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				if ( current_type == 'Category' ) {
					survey_configSelected($("#survey_config_id_" + parent_id));
				} else if ( current_type == 'Sub Category' ) {
					categorySelected($("#cate_id_" + parent_id));
				} else {
					sub_categorySelected($("#sub_category_id_" + parent_id));
				}
			}
		}
	});
}

function reload_category() {
	var current_type = $("#current_type").val();
	if ( current_type == 'Category' ) {
		categorySelected($("#cate_id_" + $("#selected_category_id").val()));
	} else if ( current_type == 'Sub Category' ) {
		sub_categorySelected($("#sub_category_id_" + $("#selected_sub_category_id").val()));
	} else {
		question_setSelected($("#question_set_id_" + $("#selected_question_set_id").val()));
	}
}

</script>

<link rel="stylesheet" href="css/basic.css" type="text/css" />
<style type="text/css">
<!--
.right_row_box {
    float: left;
    height: 20px;
    width: 660px;
    margin: 3px;
    padding: 3px;
    font-size: 11px;
}
.right_text_box {
    float: left;
    height: 55px;
    width: 660px;
    margin: 3px;
    padding: 3px;
    font-size: 11px;
}
.row_label {
    float: left;
    width: 100px;
    margin: 3px 10px 3px 5px;
	text-align: right;
}
.text_label {
    float: left;
    width: 100px;
    height: 55px;
    margin: 3px 20px 3px 5px;
}
.right_text_box textarea {
    float: left;
    width: 500px;
    height: 55px;
}
.box {
	list-style-type: none;
	overflow: none;
    width:270px;
    height:230px;
	margin-left: 4px;
	padding-left: 4px;
    float:left;
	text-align: center;
	border: none;
}
.box_label {
	height:18px;
	width: 270px;
	font-size: 13px;
}

-->
</style>
<div id=status_area style="width: 975px; height:18px; text-align:right; " ></div>

<!-- box for survey config -->
<div id=survey_expand class=ex_icon style="display:none; " >
	<INPUT type=text size=3 readonly=readonly class=expand value="[+]" onClick='show_survey_config_box()' />Show Survey Configs
</div>
<div id=survey_clip class=ex_icon>
	<INPUT type=text size=3 readonly=readonly value="[-]" class=expand onClick='hide_survey_config_box();' />Hide Survey Configs
</div>
<div id=survey_config_box style='height: 280px; width: 970px; border: 1px solid black; float: left; font-size: 12px; '>
	<div class=box >
		<div class=box_label > Survey Config</div>
		<div id=survey_config_id style="height: 250px;" ></div>
		<INPUT type=hidden id=current_type />
		<INPUT type=hidden id=selected_survey_config_id />
		<INPUT type=hidden id=selected_category_id />
		<INPUT type=hidden id=selected_sub_category_id />
		<INPUT type=hidden id=selected_question_set_id />
		<INPUT type=hidden id=selected_indicator_id />
		<INPUT type=hidden id=selected_question_id />
	</div>
	<div class=box id=survey_config_edit style="width:500px; margin-top:20px; font-size:13px;">
		<div class=right_row_box id=sc_cur_name style="margin-left:100px;text-align:left;font-size:14px;" ></div>
		<div class=right_row_box>
			<div class=row_label>Name</div>
				<INPUT type=text size=40 id=survey_config_name style='float:left;' />
		</div>
		<div class=right_row_box>
			<div class=row_label>Description</div>
				<INPUT type=text size=40 id=survey_config_description style='float:left;' />
		</div>
		<div class=right_text_box>
			<div class=row_label>Instruction</div>
				<textarea id=survey_config_instructions style='float:left;' cols=40 rows=3 ></textarea>
		</div>
		<div class=right_row_box>
			<div class=row_label>MOE:</div>
			<div class=row_label>
				<SELECT id=moe>
				<?php 
					foreach ( $moe as $option => $value ) {
						echo "<OPTION value=" . $value . ">" . $option . "</OPTION>";
					}
				?>
				</SELECT>
			</div>
		</div>
		<div class=right_row_box style='width:180px;' ></div> 
		<div class=right_row_box>
			<div class=row_label style='width:180px;'>
				<INPUT type=button class=btn value="Save" onClick="save_survey_config();" />
			</div>
			<div class=row_label>
				<INPUT type=button class=btn value="Delete" onClick="del_survey_config();" />
			</div>
			<div class=row_label>
				<INPUT type=reset class=btn value="Reset" onClick="return reload_survey_config();" />
			</div>
		</div>
	</div>
</div>

<!-- box for categories -->
<div id=category_expand class=ex_icon style="display:none; " >
	<INPUT type=text size=3 readonly=readonly class=expand value="[+]" onClick='show_category_box()' />Show Categories
</div>
<div id=category_clip class=ex_icon >
	<INPUT type=text size=3 readonly=readonly value="[-]" class=expand onClick='hide_category_box();' />Hide Categories
</div>

<div id=categories_box style='height: 250px; width: 970px; border: 1px solid black; float: left; font-size: 12px;'>
	<div class=box>
		<div class=box_label >Category</div>
		<div id=categories ></div>
	</div>
	<div style='width:35px; margin-top:28px; float:left; margin-left:10px;' >
		<div style='width:30px; height:50px; '>
			<img src='images/up_btn.gif' width='27px' height='27px' alt="Move Category Up" onClick="move_category('up');" />
		</div>
		<div style='width:27px;  height:50px; '>
		</div>
		<div style='width:27px;  height:50px; '>
		</div>
		<div style='width:27px;  height:50px; '>
			<img src='images/down_btn.gif' width='27px' height='27px' alt="Move Category Down" onClick="move_category('down');" />
		</div>
	</div>
	<div class=box>
		<div class=box_label >Sub Category</div>
		<div id=sub_categories ></div>
	</div>
	<div style='width:35px; margin-top:28px; float:left; margin-left:10px;' >
		<div style='width:27px; height:50px;'>
			<img src='images/up_btn.gif' width='27px' height='27px' alt="Move Sub Category Up" onClick="move_sub_category('up');" />
		</div>
		<div style='width:27px;  height:50px; '>
		</div>
		<div style='width:27px;  height:50px; '>
		</div>
		<div style='width:27px;  height:50px; '>
			<img src='images/down_btn.gif' width='27px' height='27px' alt="Move Sub Category down" onClick="move_sub_category('down');" />
		</div>
	</div>
	<div class=box>
		<div class=box_label >Question Set</div>
		<div id=question_sets ></div>
	</div>
	<div style='width:35px; margin-top:28px; float:left; margin-left:10px;' >
		<div style='width:27px; height:50px;'>
			<img src='images/up_btn.gif' width='27px' height='27px' alt="Move Question Set Up" onClick="move_question_set('up');" />
		</div>
		<div style='width:27px;  height:50px; '>
		</div>
		<div style='width:27px;  height:50px; '>
		</div>
		<div style='width:27px;  height:50px; '>
			<img src='images/down_btn.gif' width='27px' height='27px' alt="Move Question Set Down" onClick="move_question_set('down');" />
		</div>
	</div>
</div>
<!-- box for edit categories -->
<div id=category_edit_box style='height: 220px; width: 970px; border: 1px solid black; float: left; font-size: 12px; display:none;'>
	<div id=category_help style="height:180px; float:left; width:270px; text-align:left; margin:20px 20px 5px 10px; " >
		Edit selected category/sub category/question set on the right. 
		Use <img src='images/up_btn.gif' width='20px' height='20px' /> and
			<img src='images/down_btn.gif' width='20px' height='20px' /> to move the position (adjust order) of highlighted item.
	</div>
	<div id=category_edit style="width:200px; float:left;" >
		<div class=right_row_box id=cat_current style='text-align:left; margin-left:140px;' ></div>
		<div class=right_row_box>
			<div class=row_label>Name</div><INPUT style='float:left;' id=category_name type=text size=40 />
		</div>
		<div class=right_row_box>
			<div class=row_label>Description</div><INPUT style='float:left;' id=category_description type=text size=40 />
		</div>
		<div class=right_row_box>
			<div class=row_label>Label</div><INPUT style='float:left;' id=category_label type=text size=40 />
		</div>
		<div class=right_row_box>
			<div class=row_label>Title</div><textarea style='float:left;width:250px;height:30px;' id=category_title ></textarea>
		</div>
		<div class=right_row_box style='margin-top:15px;'>
			<div class=row_label style='width:150px;'>
				<INPUT type=button class=btn value="Save" onClick="save_category();" />
			</div>
			<div class=row_label>
				<INPUT type=button class=btn value="Delete" onClick="delete_category();" />
			</div>
			<div class=row_label>
				<INPUT type=reset class=btn value="Reset" onClick="return reload_category();" />
			</div>
		</div>

	</div>
</div>

<!-- box for question -->
<div id=indicator_expand class=ex_icon >
	<INPUT type=text size=3 readonly=readonly class=expand value="[+]" onClick='show_indicator_box()' />Show Indicators
</div>
<div id=indicator_clip class=ex_icon style="display:none; " >
	<INPUT type=text size=3 readonly=readonly value="[-]" class=expand onClick='hide_indicator_box();' />Hide Indicators
</div>
<div id=questions_box style='width: 970px; border: 1px solid black; float: left; font-size: 12px;'>
	<div class=box>
		<div class=box_label >Indicator Library</div>
		<div id=indicators></div>
	</div>
	<div style='width:30px; margin-top:100px;' class=box >
		<img src='images/right_btn.gif' width='27px' height='27px' alt='Add indicator to Answer Set' onClick='add_indicator();' />
	</div>
	<div class=box>
		<div class=box_label >Questions</div>
		<div id=questions ></div>
	</div>
	<div style='width:35px; margin-top:28px; float:left; margin-left:10px;' >
		<div style='width:27px; height:50px;'>
			<img src='images/up_btn.gif' width='27px' height='27px' alt="Move Answer Up" onClick="move_question('up');" />
		</div>
		<div style='width:27px;  height:50px; '>
			<img src='images/delete_btn.gif' width='27px' height='27px' alt="Move Answer Up" onClick="delete_question();" />
		</div>
		<div style='width:27px;  height:50px; '>
			<img src='images/down_btn.gif' width='27px' height='27px' alt="Move Answer Up" onClick="move_question('down');" />
		</div>
	</div>
	<div id=question_edit class=box style='width:300px;' >
	</div>
</div>
<div style='float:left;' ><p>&nbsp;</p></div>


<!-- close tags from header.php, do not remove -->
</div></body>
