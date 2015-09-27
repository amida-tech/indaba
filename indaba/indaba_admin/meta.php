<?php
// 
// Define meta data.
// Including: Language, Documentation Type, Answer Type, Notification Type, 
//
require_once('header.php');

?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script language=javascript>
String.prototype.trim = function() {
	        return this.replace(/^\s+|\s+$/g,"");
}

// load drop down lists
$(document).ready(function() {
	$(function(){
		$('#organizationOpt').load('meta_functions.php', 'meta=organization_id&action=showoption');
		$('#roleOpt').load('meta_functions.php', 'meta=role_id&action=showoption');
		$('#languageOpt').load('meta_functions.php', 'meta=language_id&action=showoption');
		$('#referenceOpt').load('meta_functions.php', 'meta=reference_id&action=showoption');
		$('#answertypeOpt').load('meta_functions.php', 'meta=answer_type_id&action=showoption');
		$('#studyperiodOpt').load('meta_functions.php', 'meta=study_period_id&action=showoption');
		$('#textresourceOpt').load('meta_functions.php', 'meta=text_resource_id&action=showoption');
		$('#notificationtypeOpt').load('meta_functions.php', 'meta=notification_type_id&action=showoption');
		$('#targetOpt').load('meta_functions.php', 'meta=target_id&action=showoption');
		$('#itagsOpt').load('meta_functions.php', 'meta=itags_id&action=showoption');
		$('#ctagsOpt').load('meta_functions.php', 'meta=ctags_id&action=showoption');
		$('#ttagsOpt').load('meta_functions.php', 'meta=ttags_id&action=showoption');
		$('#journal_configOpt').load('meta_functions.php', 'meta=journal_config_id&action=showoption');
		$('#announcementOpt').load('meta_functions.php', 'meta=announcement_id&action=showoption');
	});
});

var path = new Array();
path['role_id'] = 'Roles';
path['language_id'] = 'Language';
path['reference_id'] = "Reference";
path['study_period_id'] = 'Study Period';
path['notification_type_id'] = 'Notification Type';
path['target_id'] = 'Target';
path['ctags_id'] = "Case Tags";
path['ttags_id'] = "Target Tags";
path['journal_config_id'] = "Journal Config";
path['announcement_id'] = "Announce";
path['organization_id'] = 'Organizations';
path['itags_id'] = 'Indicator Tags';

function showMeta() {
	// metaitems in the form. 
	var evt = window.event || arguments.callee.caller.arguments[0];
	var target = evt.target || evt.srcElement;
	var meta;
	if ( document.getElementById('meta') ) {
		meta = document.getElementById('meta').value;
	} else {
		meta = target.id;
	}
	var selected = target.selectedIndex;
	// alert("Selected Meta: " + meta + " Selected Index: " + selected);
	if ( selected == 0 ) {
		$("#metadetails p").html('');
		$("#meta_label").html("Metadata Details ");
		return;
	} 
	var action = "";
	if ( selected == 1 ) {
		// this is an add click 
		action = "&action=add&id=0";
	} else {
		action = "&action=show&id=" + target.value ;
	}
	// reset all others
	$("#metalist").children().find('select').each( function() {
		if ( $(this).attr('id') != meta ) {
			$(this).val(-1);
		}
	});
	var item = target[selected].text;

	$.ajax({
		type: "GET",
		cache: "false",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=" + meta + action,
		success: function(obj) {
			$("#metadetails p").html(obj.form);
			$("#meta_label").html("Metadata Details => " + path[meta] + " => " + item);
			$("#status_area").html('');
			if ( meta == 'reference_id' && $("#choice_type:checked").val() > 0 ) {
				show_choices();
			}
		 }
	 });
}

//Organization functions
function updateOrganization() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
	var action = target.id;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Organization?");
		if ( go == false ) {
			return;
		}
	}
    var organization_id = document.getElementById('organization_id').value;
    var name = document.getElementById('organization_name').value.trim();
	if ( ! ( action == 'delete' ) && name == '' ) {
		alert("Please enter a name for the organization.");
		return;
	}
    var address = document.getElementById('address').value;
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=organization_id&action=" + action + 
			"&id=" + organization_id +
			"&name=" + encodeURIComponent(name) + 
			"&address=" + encodeURIComponent(address) +
			"&url=" + encodeURIComponent($("#url").val()) +
			"&admin_user_id=" + $("#admin_user_id").val(),
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Roles => " + name + " ");
			}
			$("#organizationOpt").html(obj.Opt);
			$("#metadetails p").html(obj.form);
		}
	});
}

// Role functions
function updateRole() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
	var action = target.id;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Role?");
		if ( go == false ) {
			return;
		}
	}
    var role_id = document.getElementById('role_id').value;
    var name = document.getElementById('role_name').value.trim();
	if ( ! ( action == 'delete' ) && name == '' ) {
		alert("Please enter a name for the role.");
		return;
	}
    var description = document.getElementById('role_description').value;
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=role_id&action=" + action + 
			"&id=" + role_id +
			"&name=" + encodeURIComponent(name) + 
			"&description=" + encodeURIComponent(description),
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Roles => " + name + " ");
			}
			$("#roleOpt").html(obj.Opt);
			$("#metadetails p").html(obj.form);
		}
	});
}
	
// Reference Type functions
function updateReference() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
	var action = target.id;
    var reference_id = document.getElementById('reference_id').value;
    var name = document.getElementById('reference_name').value.trim();
    var description = document.getElementById('reference_description').value;
	var choice_type = $("#choice_type:checked").val();
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Reference?");
		if ( go == false ) {
			return;
		}
	} else if ( name == '' ) {
		alert("Please enter a name for the reference.");
		return;
	}
	if ( choice_type == 0 ) {
		$("#edit_choice").val('');
	}
	$.ajax({
		type: "GET",
		cache: "false",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=reference_id&action=" + action + 
			"&id=" + reference_id +
			"&name=" + encodeURIComponent(name) + "&choices=" + encodeURIComponent($("#edit_choice").val()) +
			"&description=" + encodeURIComponent(description) + "&choice_type=" + choice_type,
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Reference => " + name );
			}
			$("#referenceOpt").html(obj.Opt);
			$("#metadetails p").html( obj.form );
			if ( choice_type > 0 ) {
				show_choices();
			}
		}
	});
}

// language

function updateLanguage() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var name = document.getElementById('language_name').value.trim();
	var action = target.id;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Language?");
		if ( go == false ) {
			return;
		}
	} else if ( name == '' ) {
		alert("Please enter a name for the language.");
		return;
	}
    var language_id = document.getElementById('language_id').value;
    var description = document.getElementById('language_description').value;
    var status = 1;
	if ( document.getElementById('language_status').value == 'on' ) {
		status = 0;
	}
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=language_id&action=" + action + 
			"&id=" + language_id +
			"&name=" + encodeURIComponent(name) + 
			"&status=" + status +
			"&description=" + encodeURIComponent(description),
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Language => " + name);
			}
			$("#languageOpt").html(obj.Opt);
			$("#metadetails p").html( obj.form );
		}
	});
}

// study_period function
function updateStudyPeriod() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
	var action = target.id;
    var name = document.getElementById('study_period_name').value.trim();
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Study Period?");
		if ( go == false ) {
			return;
		}
	} else if ( name == '' ) {
		alert("Please enter a name for the study period.");
		return;
	}
    var study_period_id = document.getElementById('study_period_id').value;
    var description = document.getElementById('study_period_description').value;
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=study_period_id&action=" + action + 
			"&id=" + study_period_id +
			"&name=" + encodeURIComponent(name) + 
			"&description=" + encodeURIComponent(description),
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Study Period => " + name);
			}
			$("#studyperiodOpt").html(obj.Opt);
			$("#metadetails p").html( obj.form );
		}
	});
}

// notification_type function
function updateNotificationType() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
	var action = target.id;
    var name = document.getElementById('notification_type_name').value.trim();
    var description = document.getElementById('notification_type_description').value;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Notification Type?");
		if ( go == false ) {
			return;
		}
	} else if ( name == '' ) {
		alert("Please enter a name for the notification type.");
		return;
	}
    var notification_type_id = document.getElementById('notification_type_id').value;
	if ( notification_type_id == 0 ) {
		action = 'addnew';
	}
	var q_string = '';
	if ( $("#n_lang_id").val() > 0 ) { 
		if ( $("#subject_text").val().trim() == '' || $("#body_text").val().trim() == '') {
			alert("Please enter subject and body text for the selected language");
			return;
		}
		var subject_text = encodeURIComponent($("#subject_text").val());
		var body_text = encodeURIComponent($("#body_text").val());
		q_string = "&subject_text=" + subject_text + "&body_text=" + body_text;
	}
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=notification_type_id&action=" + action + 
			"&id=" + notification_type_id +
			"&name=" + encodeURIComponent(name) + 
			"&description=" + encodeURIComponent(description) + "&language_id=" + $("#n_lang_id").val() + q_string,
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#metadetails p").html( obj.form );
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Notification Type => " + name);
				$("#delete").css("display", "inline");
			}
			$("#notificationtypeOpt").html(obj.Opt);
		}
	});
}

// Target functions
function updateTarget() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var name = document.getElementById('target_name').value.trim();
	var action = target.id;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Target?");
		if ( go == false ) {
			return;
		}
	} else if ( name == '' ) {
		alert("Please enter a name for the target.");
		return;
	}
    var target_id = document.getElementById('target_id').value;
    var name = document.getElementById('target_name').value;
    var target_type = document.getElementById('target_type').value;
    var description = document.getElementById('target_description').value;
    var short_name = document.getElementById('short_name').value.trim();
	if ( short_name == '' ) {
		alert("Please enter the short name for this target.");
		return;
	}
	var left = short_name.replace(/\w|-|_/gi, '');
	if ( left != '' ) {
		alert("Short name can only contain alphanumeric characters, underscores, and hyphens.");
		return;
	}
    var guid = document.getElementById('guid').value;
	var q_string = '';
	$("#target_list").find('li').each( function() {
		var this_id = $(this).attr("id");
		if ( this_id.search(/ava_tags_id/) == 0 ) {
			q_string += "&add_to_target_tag[]=" + this_id.replace("ava_tags_id_", '');
		}
	});
	$("#source_list").find('li').each( function() {
		var this_id = $(this).attr("id");
		if ( this_id.search(/ttags_id/) == 0 ) {
			q_string += "&remove_from_target_tag[]=" + this_id.replace("ttags_id_", '');
		}
	});
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=target_id&action=" + action + 
			"&id=" + target_id +
			"&name=" + encodeURIComponent(name) + 
			"&target_type=" + target_type + 
			"&description=" + encodeURIComponent(description) + q_string +
			"&short_name=" + encodeURIComponent(short_name) +
			"&guid=" + encodeURIComponent(guid),
		success: function(obj) {
			if ( obj.err == 1 ) {
				alert(obj.msg);
				return;
			}
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Target => " + name);
			}
			$("#targetOpt").html(obj.Opt);
			$("#metadetails p").html( obj.form );
		}
	});
}
//Indicator Tags functions
function updateItags() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var term = document.getElementById('itags_term').value.trim();
	var action = target.id;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Indicator Tag?");
		if ( go == false ) {
			return;
		}
	} else if ( term == '' ) {
		alert("Please enter a name for this indicator tag.");
		return;
	}
    var itags_id = document.getElementById('itags_id').value;
    var description = document.getElementById('itags_description').value;
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=itags_id&action=" + action + 
			"&id=" + itags_id +
			"&term=" + encodeURIComponent(term) + 
			"&description=" + encodeURIComponent(description),
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Indicator Tags => " + term + " ");
			}
			$("#itagsOpt").html(obj.Opt);
			$("#metadetails p").html(obj.form);
		}
	});
}
// Case Tags functions
function updateCtags() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var term = document.getElementById('ctags_term').value.trim();
	var action = target.id;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Case Tag?");
		if ( go == false ) {
			return;
		}
	} else if ( term == '' ) {
		alert("Please enter a name for this case tag.");
		return;
	}
    var ctags_id = document.getElementById('ctags_id').value;
    var description = document.getElementById('ctags_description').value;
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=ctags_id&action=" + action + 
			"&id=" + ctags_id +
			"&term=" + encodeURIComponent(term) + 
			"&description=" + encodeURIComponent(description),
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Case Tags => " + term + " ");
			}
			$("#ctagsOpt").html(obj.Opt);
			$("#metadetails p").html(obj.form);
		}
	});
}
// Target Tags functions
function updateTtags() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
    var term = document.getElementById('ttags_term').value.trim();
	var action = target.id;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Target Tag?");
		if ( go == false ) {
			return;
		}
	} else if ( term == '' ) {
		alert("Please enter a name for this target tag.");
		return;
	}
    var ttags_id = document.getElementById('ttags_id').value;
    var description = document.getElementById('ttags_description').value;
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=ttags_id&action=" + action + 
			"&id=" + ttags_id +
			"&term=" + encodeURIComponent(term) + 
			"&description=" + encodeURIComponent(description),
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Target Tags => " + term + " ");
			}
			$("#ttagsOpt").html(obj.Opt);
			$("#metadetails p").html(obj.form);
		}
	});
}


// Journal Config functions
function updateJournalconfig() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
	var action = target.id;
    var journal_config_id = document.getElementById('journal_config_id').value;
    var name = document.getElementById('journal_config_name').value;
    var description = document.getElementById('journal_config_description').value;
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Journal Config?");
		if ( go == false ) {
			return;
		}
	} else {
		// check input
		if ( name.trim() == "" ) {
			alert("Please enter a name for this config");
			return;
		}
		if ( $("#instructions").val().trim() == "" ) {
			alert("Please enter instruction for this config");
			return;
		}
		if ( isNaN( $("#min_words").val() ) || isNaN( $("#max_words").val() ) )  {
			alert("Please enter min_words/max_words");
			return;
		}
		if ( parseInt($("#min_words").val()) > parseInt($("#max_words").val()) ) {
			alert("Min words cannot be larger than max words");
			return;
		}
	}
	var exportable_items = $('input:radio[name=exportable_items]:checked').val();

	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=journal_config_id&action=" + action + 
			"&id=" + journal_config_id + "&instructions=" + encodeURIComponent($("#instructions").val()) +
			"&name=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(description) + 
			"&min_words=" + $("#min_words").val() + "&max_words=" + $("#max_words").val() + "&exportable_items=" + exportable_items,
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Journal Config => " + name + " ");
			}
			$("#journal_configOpt").html(obj.Opt);
			$("#metadetails p").html(obj.form);
		}
	});
}
	
// Announcement functions
function updateAnnouncement() {
	var evt = window.event || arguments.callee.caller.arguments[0];
    var target = evt.target || evt.srcElement;
	var action = target.id;
    var announcement_id = document.getElementById('announcement_id').value;
    var title = document.getElementById('announcement_title').value;
    var body = document.getElementById('body').value;
	var active = $("#active:checked").val();
	if ( action == 'delete' ) {
		var go = confirm("Are you sure you want to delete this Announcement?");
		if ( go == false ) {
			return;
		}
	} else {
		// check input
		if ( title.trim() == "" ) {
			alert("Please enter a title for this config");
			return;
		}
		if ( $("#body").val().trim() == "" ) {
			alert("Please enter announcement body for this config");
			return;
		}
		// start time
		if ( !/\d{4}-\d{2}-\d{2}/.test($("#expiration").val()) ) {
			alert("Please use YYYY-MM-DD format to enter an expiration time");
			return false;
		}	
	}
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "json",
		url: "meta_functions.php",
		data: "meta=announcement_id&action=" + action + 
			"&id=" + announcement_id + "&body=" + encodeURIComponent($("#body").val()) +
			"&title=" + encodeURIComponent(title) + 
			"&expiration=" + $("#expiration").val() + "&active=" + active,
		success: function(obj) {
			$("#status_area").html( obj.msg );
			if ( action == 'delete' ) {
				if (  typeof(obj.form) == 'undefined' ) {
					return;
				}
				$("#meta_label").html("Metadata Details ");
			} else {
				$("#meta_label").html("Metadata Details => Announcement => " + title + " ");
			}
			$("#announcementOpt").html(obj.Opt);
			$("#metadetails p").html(obj.form);
		}
	});
}



function getTextItems() {
	var text_resource_id = document.getElementById('text_resource_id').value;
	var name = document.getElementById('text_resource_name').value;
	var description = document.getElementById('text_resource_description').value;
	window.open("manage_text_items.php?text_resource_id=" + text_resource_id + "&name=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(description), "manage_text_items", "menubar=0,height=600,width=800");
}

function hide_choices() {
	$("#choices_area").hide();
}
function show_choices() {
	$("#choices_area").show();
	$.ajax({
		type:			"GET",
		cache:			"false",
		dataType:		"html",
		url:			"meta_functions.php",
		data:			"action=show_choices&reference_id=" + $("#reference_id").val(),
		success:		function(data) {
			$("#edit_choice").val( data );
		}
	});
}



</script>

<link rel="stylesheet" href="css/basic.css" type="text/css" />

<table width="100%" height="350"  border="0">
  <tr>
    <td height="58">Please select the meta data from the lists below to add/modify/delete items. </td>
  </tr>
  <tr>
    <td height="174"><form name="metalist" id="metalist">
      <table width="90%"  border="0">
      	<tr>
      	  <td width="20%" align="right">Organizations &nbsp&nbsp</td>
          <td width="25%" align="left"><div id=organizationOpt></div></td>
          <td width="5%">&nbsp;</td>
          <td width="20%" align="right">Languages &nbsp&nbsp</td>
          <td width="30%" align="left"><div id=languageOpt></div></td>
        </tr>
        <tr>
          <td width="20%" align="right">Roles &nbsp&nbsp</td>
          <td width="25%" align="left"><div id=roleOpt></div></td>
          <td width="20%" width="5%">&nbsp;</td>
          <td align="right">References &nbsp&nbsp</td>
          <td width="25%" align="left"><div id=referenceOpt></div></td>
        </tr>
        <tr>
          <td align="right">Notification Types &nbsp&nbsp</td>
          <td align="left"><div id=notificationtypeOpt></div></td>
          <td>&nbsp;</td>
          <td align="right">Targets &nbsp;&nbsp;</td>
          <td align="left"><div id=targetOpt></div></td>
        </tr>
        <tr>
          <td align="right">Study Periods &nbsp&nbsp</td>
          <td width="25%" align="left"><div id=studyperiodOpt></div></td>
          <td>&nbsp;</td>
          <td align="right">Announcements &nbsp;</td>
          <td align="left"><div id=announcementOpt></div></td>
        </tr>
        <tr>
          <td align="right">Indicator Tags &nbsp&nbsp</td>
          <td  align="left"><div id=itagsOpt></div></td>
          <td>&nbsp;</td>
          <td align="right">Case Tags &nbsp;</td>
          <td align="left"><div id=ctagsOpt></div></td>
        </tr>
        <tr>
          <td align="right">Journal Configs &nbsp&nbsp</td>
          <td  align="left"><div id=journal_configOpt></div></td>
          <td>&nbsp;</td>
          <td align="right">Target Tags &nbsp;</td>
          <td align="left"><div id=ttagsOpt></div></td>
        </tr>
      </table>
    </form></td>
  </tr>
  <tr>
    <td>
	  <form >
		<fieldset><legend align=left><div id=meta_label>Metadata Details: </div></legend>
		<div id=metadetails>
		  <p align=center></p>
		</div>
		<div id=status_area>
		  <p align=center></p>
		</div>
		</fieldset>
	  </form></td>
  </tr>
</table>

<!-- close out tags inheritated from header -->
</div>
</body>

