<?php
require_once('header.php');
define ("SINGLE", 1);
define ("MULTIPLE", 2);
define ("INTEGER", 3);
define ("FLOAT", 4);
define ("TEXT", 5);
?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script language="javascript"  type="text/javascript"> 
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}

$(document).ready(function() {
	get_all_indicators();
});

function get_all_indicators(id) {
	var show_id = '';
	if ( typeof(id) != 'undefined' ) {
		show_id = '&id=' + id;
	}
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		data:			'action=show_indicator' + show_id,
		url:			'indicator_functions.php',
		success:		function(obj) {
			$("#source_list").html(obj.indicator_list);
		}
	});
}



function filter_indicator() {
	var name_like = $("#search_indicator").val();
	$("#source_list").find('li').each( function() {
		if (  $(this).attr('id') > 0 && $(this).text().search(new RegExp(name_like, 'i')) < 0 ) { 
			$(this).hide();
		} else {
			$(this).show();
		}
	});
}

$(function() {
	$("#source_list").selectable( {
		click: function () {
			$(this).toggleClass("ui-selected").siblings().removeClass("ui-selected");
		},
		stop: function() {
			var i=0;
			var selected;
			$(".ui-selected", this).each(function() {
				var index = $("#source_list li").index(this);
				if ( i == 0 ) {
					selected = $(this).attr("id");
					// alert($(this).attr("id") + $(this).text() + " is selected");
					// alert("ID " + selected + " is selected");
					show_indicator(selected);
				} else {
					$(this).removeClass("ui-selected");
				}
				i ++;
			});
		}
	});
});

$(function() {
	$("#tag_list").selectable( {
		click: function () {
			$(this).toggleClass("ui-selected").sliblings().removeClass("ui-selected");
		}
	});
});

function add_tag() {
	// alert("ID => " + $("#itag_id option:selected").val() + " VALUE => " + $("#itag_id option:selected").attr('term'));
	if ( $("#itag_id option:selected").val() <= 0 ) {
		alert("Please select a tag from the drop down list");
		return false;
	}
	var newli = "<li id='" + $("#itag_id option:selected").val() + 
					"' term='" + $("#itag_id option:selected").attr('term') + 
					"' class='ui-widget-content'>" + 
					$("#itag_id option:selected").text() + "</li>";
	$("#tag_list").append( newli );
	$("#itag_id option[value='" + $("#itag_id option:selected").val() + "']").remove();
}
function remove_tag() {
	//$("tag_list").remove();
	$("#tag_list").find(".ui-selected").each( function() {
		// alert( $(this).attr('id'));
		// remove selected item from tag_list
		$("#tag_list li[id='" + $(this).attr('id') + "']" ).remove();
		// add them back to select list
		var newopt = "<option id='" + $(this).attr('id') + "' term='" +
						$(this).attr('term') + "'>" +
						$(this).text() + "</option>";
		$("#itag_id").append( newopt );

	});
}
function show_indicator(id, atc_row) {
	if ( isNaN(id) || typeof(id) == 'undefined' ) {
		var id = $("#indicator_id").val();
	}
	$("#buttons").css('display', 'inline');
	if  ( id <= 0 ) {
		$("#source_list").find("li").each( function() {
			$(this).removeClass("ui-selected");
		});
		clear_details();
	} 
	$("#indicator_details").css("display", "inline");
	$.ajax({
		type:			"GET",
		cache:			'false',
		dataType:		'json',
		url:			'indicator_functions.php',
		data:			'action=show_indicator&indicator_id=' + id,
		success:		function(obj) {
			$("#name").val(obj.name);
			$("#indicator_id").val(obj.id);
			$("#question").val(obj.question);
			$("#tip").val(obj.tip);
			$("#referenceOpt").html(obj.referenceOpt);
			$("#tagOpt").html(obj.tagOpt);
			$("#tag_list").html(obj.tagList);
			$("#addbtn").val("Add");
			$("#rembtn").val("Remove");
			$("#answer_type").val(obj.answer_type);
			$("#answer_type_org").val(obj.answer_type);
			$("#answer_type_id").val(obj.answer_type_id);
			$("#created_by").html(obj.created_by);
			$("#status_area").html(obj.query_msg);
			answer_typeSelected(atc_row);
			// $(atc_row).children().css('background-color', 'yellow');
			$("#disable_clone").val('false');
		}
	});
}

function clear_details() {
	$("#answer_area input").each(function() {
		$(this).val('');
	});
	$("#indicator_area input").each(function() {
		$(this).val('');
	});
	$("textarea").each(function() {
		$(this).val('');
	});

}


function answer_typeSelected(atc_row) {
	//alert('Reference ID ' + $("#reference_id").val() + " is selected");
	if ( $("#answer_type").val() == 1 ) {
		if ( $("#indicator_id").val() > 0 && $("#answer_type_org").val() <= 2  ) {
			$("#answer_type_choice").css('display', 'inline');
		} else {
			$("#answer_type_choice").css('display', 'none');
		}
		$("#answer_type_number").css('display', 'none');
		$("#answer_type_text").css('display', 'none');
	} else if ( $("#answer_type").val() == 2 ) {
		if ( $("#indicator_id").val() > 0 && $("#answer_type_org").val() <= 2) {
			$("#answer_type_choice").css('display', 'inline');
		} else {
			$("#answer_type_choice").css('display', 'none');
		}
		$("#answer_type_number").css('display', 'none');
		$("#answer_type_text").css('display', 'none');
	} else if ( $("#answer_type").val() <= 4 ) {
		$("#answer_type_choice").css('display', 'none');
		$("#answer_type_number").css('display', 'inline');
		$("#answer_type_text").css('display', 'none');
	} else {
		$("#answer_type_choice").css('display', 'none');
		$("#answer_type_number").css('display', 'none');
		$("#answer_type_text").css('display', 'inline');
	}
	// if answer_type is not orginal, will use 0 as answer_type_id 
	var answer_type_id = $("#answer_type_id").val();
	if ( $("#answer_type").val() != $("#answer_type_org").val() ) {
		answer_type_id = 0;
	}
	$.ajax({
		type:           'GET',
		cache:          'false',
		dataType:       'json',
		url:            'indicator_functions.php',
		data:           'action=show_answers&answer_type_id=' + answer_type_id + '&answer_type=' + $("#answer_type").val(),
		success:        function(obj) {
			$("#selected_atc").val('');
			if ( $("#answer_type").val() <= 2 ) { // choices
				$("#atc_choices").html(obj.atc_choices);
				$("#choice_label").val('');
				$("#choice_score").val('');
				$("#not_use_score").attr('checked', false);
				$("#choice_criteria").val('');
				if ( typeof(atc_row) != 'undefined' ) {
					atcSelected($(atc_row));
				}
			} else if ( $("#answer_type").val() <=4 ) {		// numbers
				$("#number_min").val(obj.min_value);
				$("#number_max").val(obj.max_value);
				$("#number_default").val(obj.default_value);
				$("#number_criteria").val(obj.criteria);
			} else {										// text
				$("#char_min").val(obj.min_chars);
				$("#char_max").val(obj.max_chars);
				$("#text_criteria").val(obj.criteria);
			}
		}
	});
}

function atcSelected(id) {
	// clear all 
	$("#atc_choices").find("input").each( function() {
		$(this).css('background-color', 'white');
	});
	// set back ground for selected row
	$(id).children().css('background-color', 'yellow');
	// var label = $(id).children(".choice_label").val();
	// var defaults = $(id).children(".choice_default").val();
	var atc_choice_id = $(id).attr('id').replace(/atc_choice_/g, '');
	// alert ( "ATC Choice ID is " + atc_choice_id);
	$("#selected_atc").val(atc_choice_id);
	var selected_id = $(id).attr('id');
	$("#selected_id").val(selected_id);
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		data:			'action=show_atc_choice&atc_choice_id=' + atc_choice_id,
		url:			'indicator_functions.php',
		success:		function(obj) {
			$("#choice_label").val(obj.label);
			if ( obj.use_score == 1 ) {
				$("#not_use_score").attr('checked', false);
			} else {
				$("#not_use_score").attr('checked', true);
			}
			$("#choice_score").val(obj.score);
			if ( obj.default_selected == 1 ) {
				$("#choice_default").attr('checked', true);
			} else {
				$("#choice_default").attr('checked', false);
			}
			$("#choice_criteria").val(obj.criteria);
			$("#status_area").html('');
		}
	});
	$(id).children().css('background-color', 'yellow');
}
function reload_atc(id) {
	atcSelected($("#" + id));
}

function check_score() {
	if ( $("#not_use_score").attr('checked') ) {
		// N/A checked. Need to clear off the score field and disable it
		// $("#choice_score").val("");
		$("#choice_score").attr('disabled', true);
	} else {
		$("#choice_score").attr('disabled', false);
	}
	return;
}

function add_new_choice() {
	// alert("Adding atc_id " + $("#selected_atc").val());
	// cannot add if no indicator is selected
	if ( $("#indicator_id").val() <= 0 ) {
		alert("Please save the indicator first before adding new choices");
		return;
	}
	if ( $("#choice_label").val().trim() == '' ) {
		alert("Please enter a label for this choice");
		return;
	}
	var use_score = 0;
	// only validate score if not_use_score is checked
	if ( $("#not_use_score").attr('checked') == false ) {
		if ( isNaN($("#choice_score").val()) || $("#choice_score").val().trim() == ''  ) {
			alert("Please enter a numeric score for this choice");
			return;
		}
		use_score = 1;
	}
	if ( $("#choice_criteria").val().trim() == '' ) {
		//alert("Please enter criteria for this choice");
		//return;
	}
	// make sure the user does not change answer type between single and multiple as they are two different types with same structure.
	if ( $("#answer_type").val() != $("#answer_type_org").val() ) {
		alert("You may NOT change the answer type while making changes to the choices. Save the indicator first.");
		return;
	}
	var is_default = 0;
	if ( $("#choice_default").attr('checked') == true ) {
		// Check this is single or multiple, if it is single, only one default can be set
		var error = 0;
		if ( $("#answer_type").val() == 1 ) {
			$(".choice_default").each(function() {
				if ( $(this).val() == "Yes" ) {
					alert("Only one choice can be default, modify the attribute for the others before setting this to default");
					error = 1;
				}
			});
		}
		if ( error == 1 ) {
			return;
		}
		is_default = 1;
	}
	var q_string = "&indicator_id=" + $("#indicator_id").val() + "&choice_label=" + $("#choice_label").val().trim();
	q_string += "&choice_score=" + $("#choice_score").val() + "&use_score=" + use_score +
			"&choice_criteria=" + encodeURIComponent($("#choice_criteria").val().trim()) +
			"&atc_id=" + $("#selected_atc").val() + "&is_default=" + is_default +
			"&answer_type=" + $("#answer_type").val() +
			"&answer_type_id=" + $("#answer_type_id").val();
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		url:			'indicator_functions.php',
		data:			'action=save_choice' + q_string,
		success:		function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				show_indicator($("#indicator_id").val());
			}
		}
	});
}

function move_choice(move_type) {
	// alert("Moving atc_id " + $("#selected_atc").val() + " " + move_type );
	var atc_id = $("#selected_atc").val();
	if ( isNaN(atc_id) || atc_id <=0 ) {
		return;
	}
	var atc_row = "#atc_choice_" + atc_id;
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		url:			'indicator_functions.php',
		data:			'action=move_choice&direction=' + move_type + "&atc_id=" + atc_id,
		success:		function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				show_indicator($("#indicator_id").val(), atc_row);
			}
		}
	});
}

function delete_choice() {
	var atc_id = $("#selected_atc").val();
	if ( isNaN(atc_id) || atc_id <=0 ) {
		return;
	}
	var go = confirm("Are you sure you want to delete the highlighted choice?" );
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		url:			'indicator_functions.php',
		data:			'action=delete_choice&atc_id=' + atc_id,
		success:		function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				show_indicator($("#indicator_id").val());
				$("#selected_atc").val(0);
			}
		}
	});
}

function save_indicator() {
	// check input first
	var name = $("#name").val().trim();
	if ( name == '' ) {
		alert("Please enter a name for this indicator");
		return;
	}
	var q_string = "&name=" + name;
	var question = $("#question").val().trim();
	if ( question == '' ) {
		alert("Please enter questin text for this indicator");
		return;
	}
	q_string += "&question=" + encodeURIComponent(question);
	q_string += "&tip=" + encodeURIComponent($("#tip").val());
	q_string += "&reference_id=" + $("#reference_id").val();
	var answer_type = $("#answer_type").val();
	if ( answer_type <= 0 ) {
		alert("Please select answer type from the list");
		return;
	}
	q_string += "&answer_type=" + answer_type;
	if ( answer_type <= 2 ) {
		// choices, don't worry about the answer defination
	} else if ( answer_type == 3 ) {
		// integer type
		var min = parseInt($("#number_min").val());
		if ( isNaN(min) ) {
			alert("Please enter an integer value for Min");
			return;
		}
		q_string += "&min=" + min;
		var max = parseInt($("#number_max").val());
		if ( isNaN(max) || max < min ) {
			alert("Please enter an integer value for Max which also needs to be larger than Min");
			return;
		}
		q_string += "&max=" + max;
		q_string += "&default_value=" + $("#number_default").val();
		var criteria = $("#number_criteria").val().trim();
		if ( criteria == '' ) {
			alert("Please enter criteria for this indicator");
			return;
		}
		q_string += "&criteria=" + encodeURIComponent(criteria);
	} 	else if ( answer_type == 4 ) {
		// float type
		var min = $("#number_min").val();
		if ( isNaN(min) || min < 0 ) {
			alert("Please enter an positive value for Min");
			return;
		}
		q_string += "&min=" + min;
		var max = $("#number_max").val();
		if ( isNaN(max) || max < 0 || max < min ) {
			alert("Please enter an positive value for Max which also needs to be larger than Min");
			return;
		}
		q_string += "&max=" + max;
		q_string += "&default_value=" + $("#number_default").val();
		var criteria = $("#number_criteria").val().trim();
		if ( criteria == '' ) {
			alert("Please enter criteria for this indicator");
			return;
		}
		q_string += "&criteria=" + encodeURIComponent(criteria);
	} else {
		// text type
		var min = parseInt($("#char_min").val());
		if ( isNaN(min) ) {
			alert("Please enter an integer value for Min Chars");
			return;
		}
		q_string += "&min=" + min;
		var max = parseInt($("#char_max").val());
		if ( isNaN(max) || max < min ) {
			alert("Please enter an integer value for Max Chars which is larger than Min Chars");
			return;
		}
		q_string += "&max=" + max;
		var criteria = $("#text_criteria").val().trim();
		if ( criteria == '' ) {
			alert("Please enter criteria for this indicator");
			return;
		}
		q_string += "&criteria=" + encodeURIComponent(criteria);
	}
	if ( $("#answer_type").val() != $("#answer_type_org").val() ) {
		q_string += "&answer_type_id=0";
	} else {
		q_string += "&answer_type_id=" + $("#answer_type_id").val();
	}

	var this_id;
	$("#itag_id").find('option').each( function() {
		this_id = $(this).attr('id');
		if ( this_id.search(/exist_/) == 0 ) {
			q_string += "&remove_tag_id[]=" + this_id.replace("exist_", "");
		}
	});
	$("#tag_list").find('li').each( function() {
		this_id = $(this).attr('id');
		if ( this_id.search(/tag_/) == 0 ) {
			q_string += "&add_tag_id[]=" + this_id.replace("tag_", "");
		}
	});

	// we should have all needed fields
	// alert(q_string);
	$.ajax({
		type:			'GET',
		cache:			'false',
		dataType:		'json',
		url:			'indicator_functions.php',
		data:			'action=save_indicator&indicator_id=' + $("#indicator_id").val() + q_string,
		success:		function(obj) {
			if ( obj.query_status == 0 ) {
				get_all_indicators(obj.id);
				show_indicator(obj.id);
			}
			$("#status_area").html(obj.query_msg);
		}
	});
}

function delete_indicator() {
	if ( $("#indicator_id").val() == 0 ) {
		return;
	}
	var go = confirm("Are you sure you want to delete the indicator? All answer definations for this indicator will also be deleted!");
	if ( go == false ) {
		return;
	}
	// alert("Deleting " + $("#indicator_id").val());
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'indicator_functions.php',
		data:		'action=delete_indicator&indicator_id=' + $("#indicator_id").val(),
		success:	function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 0 ) {
				clear_details();
				get_all_indicators();
				show_indicator(0);
			}
		}
	});
}
$(document).ready(function(){
	$("#indicator_details").find('input, textarea').each( function() {
		$(this).keyup( function() {
			$("#disable_clone").val('true');
		});
	});
	$("#indicator_details").children().each( function() {
		$(this).change( function() {
			$("#disable_clone").val('true');
		});
	});
});

function clone_indicator() {
	if ( $("#disable_clone").val() == 'true' ) {
		alert("Please save your changes first then click 'Clone' button!");
		return;
	}
	$.ajax({
		type:		'GET',
		dataType:	'json',
		cache:		'false',
		url:		'indicator_functions.php',
		data:		'action=clone&indicator_id=' + $("#indicator_id").val(),
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				get_all_indicators(obj.id);
				show_indicator(obj.id);
			}
			$("#status_area").html(obj.query_msg);
		}
	});
}

function openBatch() {
	window.open("load_indicators.php", "batch_load", "menubar=0,height=600,width=800");
	return;
}
</script>
 
<link rel="stylesheet" href="css/basic.css" type="text/css" />

<style type="text/css">
.right_row_box {
	float: left;
	height: 15px;
	width: 960px;
	margin: 2px;
	padding: 3px;
	font-size: 11px;
}
.right_text_box {
	float: left;
	height: 55px;
	width: 960px;
	margin: 3px;
	padding: 3px;
	font-size: 11px;
}
.row_label {
	float: left;
	text-align: right;
	width: 100px;
	margin: 3px 3px 3px 15px;
}
.text_label {
	float: left;
	text-align: right;
	width: 100px;
	height: 55px;
	margin: 3px 20px 3px 15px;
}
.right_text_box textarea {
	float: left;
	width: 500px;
	height: 55px;
}
.choice_label {
	width:288px;
	height:18px;
	float:left;
	margin-left: 5px;
	font-size: 11px;
	border:1px solid black ;
}
.choice_default {
	width:47px;
	height:18px;
	float:left;
	font-size: 11px;
	border:1px solid black ;
}

</style>

<div style='height: 250px; width: 970px; border: 1px solid black; float: left; font-size: 14pt;'>
	<div style='float:left; height:220px; width:300px; ' >
		<div class=btn style='float:left; height:20px; width:130px; margin:50px 30px 2px 70px; font-size:11px; width:160px;' onClick='openBatch();' >Batch Load</div>
		<div style='float:left; margin:20px 5px 2px 70px; padding-right: 5px; font-size:11px;  width:160px;'>
			Filter&nbsp;&nbsp;<INPUT type=text id=search_indicator size=15 onKeyUp='filter_indicator();' /></div>
		<div id=add_new class=btn style='float:left; height:20px; width:130px; margin:20px 30px 2px 70px; font-size:11px; width:160px;' onClick='show_indicator(0);' >Add New Indicator</div>
	</div>
	<div style='float:left; height:220px; width:620px; margin:10px 20px 10px 20px; font-size:12px; border:2px solid black; overflow: auto;' > 
		<div id=source_list ></div>
	</div>
</div>

<div id=indicator_details style='height: 600px; width: 970px; border: 1px solid black; float: left; font-size: 10pt; display:none;'>
	<div style='font-size: 14pt;'>Indicator Details</div>
	<div id=indicator_area style='height: 240px; width: 970px; border: 1px solid black; float: left; padding-top:10px; font-size: 11pt;'>
	    <div style='float:left; width:650px;'>
		    <div id=created_by class=right_row_box style='width:230px; margin-left:10px; font-size:8pt;' ></div>
			<div id=status_area class=right_row_box style='width:200px; right-margin:40px; font-size:8pt;' ></div>
			<div class=right_row_box > 
				<div class=row_label >Name</div>
				<div style='float:left; margin-left:14px; '><INPUT type=text id=name size=80 />
										<INPUT id=indicator_id type=hidden />
										<INPUT id=answer_type_id type=hidden />
										<INPUT id=answer_type_org type=hidden />
										<INPUT id=disable_clone type=hidden value='false' />
				</div>
			</div>
	
			<div class=right_text_box >
				<div class=text_label >Question</div>
				<textarea id=question cols=80 rows=2 ></textarea>
			</div>
			<div class=right_text_box >
				<div class=text_label >Tip</div>
				<textarea id=tip cols=80 rows=2 ></textarea>
			</div>
			<div class=right_row_box>
				<div class=row_label > Reference </div>
				<div style='float:left; width=200px; margin-left:14px;' id=referenceOpt>
					<SELECT id='reference_id' ><option value=0>Select reference From the List</option></SELECT>
				</div>
				<div class=row_label style='margin-left:20px;' > Answer Type </div>
				<div style='width:200px; float:left;  margin-left:14px;' id=answer_typeOpt>
					<SELECT id='answer_type' onChange='answer_typeSelected();' >
						<option value=0>Select Answer Type From the List</option>
						<option value=1>Single Choice</option>
						<option value=2>Multiple Choice</option>
						<option value=3>Integer</option>
						<option value=4>Float</option>
						<option value=5>Text</option>
					</SELECT></div>
				</div>
		
			</div>

		<div  style='float:left; width:250px; margin-left:25px;'>
			<div style='margin-bottom:10px;'>
				<div  style='width:230px;'>Indicator Tags </div>
				<div style='float:left; width=200px; margin-left:14px;' id=tagOpt>
						<SELECT id='itag_id' ><option value=0>Select tag From the List</option></SELECT>
				</div>
			</div>
			<div style='margin-top:40px;' id=tagbtn >
				<INPUT type="button" id=addbtn class=btn value="Add Tag" onClick="return add_tag();"/>
				<INPUT type="button" id=rembtn class=btn value="Remove Tag" onClick="return remove_tag();"/>
			</div>
			<div style='float:left; margin:10px 20px 10px 20px; ' > 
				<div style='float:left; min-width: 220px; height:130px; font-size:12px; border:1px solid black; overflow:auto;'>
					<UL id=tag_list>
					</UL>
				</div>
			</div>
		</div>
	</div>
	<div class=right_row_box id=buttons style='display:none;' >
		<div class=row_label style='width:20px;' ></div>
			<div class=row_label><INPUT id=clone type=button class=btn value=Clone onClick='clone_indicator();' /></div>
		<div class=row_label style='width:20px;' ></div>
			<div class=row_label><INPUT type=button class=btn value=Save onClick='save_indicator();' /></div>
		<div class=row_label style='width:20px;' ></div>
			<div class=row_label><INPUT type=reset class=btn value=Delete onClick='return delete_indicator();' /></div>
		<div class=row_label style='width:20px;' ></div>
			<div class=row_label><INPUT type=reset class=btn value=Reset onClick='return show_indicator($("#indicator_id").val()); reload_atc($("#selected_id").val());' /></div>
	</div>
	<div id=answer_area style='height: 220px; width: 970px; float: left; font-size: 11pt;'>
	<div style='font-size:12pt; width:970px; margin-top:15px;'>Answer Definition</div>
	<div id=answer_type_number style='height: 180px; width: 970px; float: left; font-size: 11pt; display:none;'>
		  <div style='width:970px; float:left; ' >
			  <div style='width:200px; float:left;'> Min <INPUT type=text id=number_min size=4 /></div>
			  <div style='width:200px; float:left;'>Max <INPUT type=text id=number_max size=4 /></div>
			  <div style='width:200px; float:left;'>Default <INPUT type=text id=number_default size=4 /></div>
		  </div>
		  <div style='width:970px; float:left; ' >
			<div style='margin:20px 10px 10px 50px; float:left; width:70px;' >Criteria </div>
			<div style='float:left; margin:10px 5px 5px 5px; width:600px;' ><textarea id=number_criteria cols=70 rows=4></textarea></div>
		  </div> 
	</div>
	<div id=answer_type_text style='height: 180px; width: 970px; float: left; font-size: 11pt; display:none;'>
	  <div style='width:300px; float:left;'>Min Chars <INPUT type=text id=char_min size=4 /></div>
	  <div style='width:300px; float:left;'> Max Chars <INPUT type=text id=char_max size=4 /></div>
	  <div style='width:300px; float:left;'> </div>
	  <div style='width:970px; float:left; ' >
		  <div style='margin:20px 10px 10px 80px; float:left; ' >Criteria </div>
		  <div style='float:left; margin:10px 5 5 10px;' ><textarea id=text_criteria cols=70 rows=4></textarea></div>
	  </div>
	</div>
	<div id=answer_type_choice style='height: 180px; width: 970px; margin:5px; float: left; font-size: 11pt; display:none;'>
		<div style='width:400px; height:200px; float:left;'>
			<div id=choices style='background-color:#fff; width:370px; height:180px; float:left; overflow:auto;'>
				<div class=choice_label style='font-size:14px;' >Choice</div>
				<div class=choice_default style='font-size:14px;' >Default</div>
				<div style='width:350px; height:160px; float:left;' id=atc_choices > </div>
		</div>
	    <div style='width:28px; height:180px;  float:left;'>
		  	<div style='width:27px; '><img src='images/up_btn.gif' width='27px' height='27px' alt="Move Answer Up" onClick="move_choice('up');" /></div>
			<p></p>
		  	<div style='width:27px; '><img src='images/add_btn.gif' width='27px' height='27px' alt="Add Answer to List" onClick="add_new_choice();" /></div>
			<p></p>
		  	<div style='width:27px; '><img src='images/delete_btn.gif' width='27px' height='27px' alt="Remove Answer from List" onClick="delete_choice();" /></div>
			<p></p>
		  	<div style='width:27px; '><img src='images/down_btn.gif' width='27px' height='27px' alt="Move Answer Down" onClick="move_choice('down');" /></div>
			<p></p>
		  </div>
	  </div>
	  <div style='width:530px; float:left; '>
		  <div style='width:70px; float:left; margin-left:8px;'>Label  </div>
		  <div style='width:370px; float:left; margin-left:10px;'><INPUT align=left type=text id=choice_label size=38 /></div>
	 </div>
	  <div style='width:530px; float:left; '>
		  <div style='width:70px; float:left; margin-left:8px;'>Score  </div>
	  		<div style='width:135px; float:left; margin-left:13px;'><INPUT align=left type=text id=choice_score size=5 /></div>
		  <div style='width:40px; float:left; margin-left:8px;'>N/A</div>
	  		<div style='width:30px; float:left; margin-left:10px;'><INPUT type=checkbox id=not_use_score onClick='check_score();' /></div>
		  <div style='width:60px; float:left; margin-left:10px;'>Default</div>
	  		<div style='width:30px; float:left; margin-left:10px;'><INPUT type=checkbox id=choice_default /></div>
																	<INPUT type=hidden id=selected_atc />
																	<INPUT type=hidden id=selected_id />
	  </div>
	  <div style='width:530px; float:left; '>
		  <div style='margin:5px 5px 5px 8px; float:left; width:70px; ' >Criteria </div>
		  <div style='float:left; width:370px; height:65px; margin-left:20px; ' ><textarea id=choice_criteria cols=45 rows=4></textarea></div>
	  </div>
	</div>
	</div>
</div>


<!-- close tags from header.php, do not remove -->
</div></body>
