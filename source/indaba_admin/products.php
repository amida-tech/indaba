<?php
require_once('header.php');
require_once('include/config.inc');

?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script type="text/javascript" src="include/tinybox.js"></script>
<link rel="stylesheet" href="css/tinybox.css" />

<script language="javascript"  type="text/javascript"> 

String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}


// Get product drop down
$(document).ready(function() {
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"html",
		url:		"product_functions.php",
		data:		"action=show_project_opt",
		success:	function(data) {
			$("#project_dd").html(data);
		}
	});

});

function projectSelected() {
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"html",
		url:		"product_functions.php",
		data:		"action=show_product_opt&project_id=" + $("#project_id").val(),
		success:	function(data) {
			$("#products_dd").html(data);
			$("#edit_area").html('');
		}
	});
}

function productSelected() {
	//$("#edit_area").html("HELLO, " + $("#product_opt :selected").val() + " selected" + ": <div id=new_id></div>" + " here");
	if ( $("#product_opt").val() == -1 ) {
		$("#edit_area").html('');
		$("#status_area").html('');
		return;
	}
	$.ajax({
		type:       "GET",
		cache:		"false",
		dataType:	"json",
		url:		"product_functions.php",
		data:		"action=show_product&product_id=" + $("#product_opt").val(),
		success:	function(obj) {
			$("#edit_area").html(obj.form);
			$("#status_area").html(obj.status);
		}
	});
}

function saveProduct() {
	// check input and build query string
	var q_string = "action=save_product";
	if ( $("#name").val() == "" ) {
		alert("Please enter a name for the product");
		return;
	}
	q_string += "&name=" + $("#name").val();
	if ( $("#description").val() == "" ) {
		alert("Please enter a description for the product");
		return;
	}
	q_string += "&description=" + $("#description").val();
	if ( isNaN($("#content_type:checked").val()) ) {
		alert("Please select product content type");
		return;
	}
	q_string += "&content_type=" + $("#content_type:checked").val();
	if ( $("#project_id").val() <= 0 ) {
		alert("Please select project from the list");
		return;
	}
	q_string += "&project_id=" + $("#project_id").val();
	if ( $("#workflow_id").val() <= 0 ) {
		alert("Please select workflow from the list");
		return;
	}
	q_string += "&workflow_id=" + $("#workflow_id").val();
	if ( $("#access_matrix_id").val() <= 0 ) {
		q_string += "&access_matrix_id=null";
	} else {
		q_string += "&access_matrix_id=" + $("#access_matrix_id").val();
	}
	if ( $("#product_config_id").val() <= 0 ) {
		alert("Please select content config from the list");
		return;
	}
	q_string += "&product_config_id=" + $("#product_config_id").val();
	q_string += "&product_id=" + $("#product_id").val();
	//alert(q_string);
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"product_functions.php",
		data:		q_string,
		success:	function(obj) {
			$("#products_dd").html(obj.product_opt);
			productSelected();
			$("#status_area").html(obj.msg);
		}
	});
}

function deleteProduct(product_id) {
	if ( $("#product_id").val() <= 0 ) {
		return;
	}
	var go = confirm("Are you sure you want to delete this product and all its tasks?");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"product_functions.php",
		data:		"action=delete_product&product_id=" + $("#product_id").val(),
		success:	function(obj) {
			$("#status_area").html(obj.msg);
			if ( obj.status == 0 ) {
				$("#products_dd").html(obj.product_opt);
				$("#edit_area").html("");
			} 
			$("#manage_task").html();
		}
	});
}

function show_config_opt(content_type) {
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "html",
		url:        "product_functions.php",
		data:       "action=show_config_opt&content_type=" + content_type,
		success:	function(data) {
			$("#product_config_dd").html(data);
		}
	});
}

function show_task_details() {
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "product_functions.php",
		data:       "action=show_task_details&task_id=" + $("#task_list :selected").val() + "&workflow_id=" + $("#workflow_id").val(),
		success:	function(obj) {
			$("#task_goal").html(obj.goal_opt);
			$("#task_tool").html(obj.tool_opt);
			$("#task_name").val(obj.task_name);
			if ( obj.assignment_method == 1 ) {
				$("input[name='assignment_method']")[0].checked = true;
				$("input[name='assignment_method']")[1].checked = false;
				$("input[name='assignment_method']")[2].checked = false;
			} else if( obj.assignment_method == 2 ) {
				$("input[name='assignment_method']")[1].checked = true;
				$("input[name='assignment_method']")[0].checked = false;
				$("input[name='assignment_method']")[2].checked = false;
			} else {
				$("input[name='assignment_method']")[0].checked = false;
				$("input[name='assignment_method']")[1].checked = false;
				$("input[name='assignment_method']")[2].checked = true;
			}
			$("#task_description").val(obj.description);
			if ( $("#task_list :selected").val() > 0 && obj.assignment_method <= 2 ) {
				$("#assign_role,#assign_user").css("display","inline");
			} else {
				$("#assign_role,#assign_user").css("display","none");
			}
			$("#task_instructions").val(obj.instructions);
			T$('showtool').onclick = function() {
				var tool_id = "tool_id_" + $("#tool_id").val();
				var msg = $("#" + tool_id).val();
				TINY.box.show(msg, 0,0,0,0,10);
			};
			T$('showgoal').onclick = function() {
				var goal_id = "goal_id_" + $("#goal_id").val();
				var msg = "<div style='text-align:left;' > " + $("#" + goal_id).val() + "</div>";
				TINY.box.show(msg, 0,400,0,0,10);
			};
		}
	});
}

function saveTask() {
	if ( $("#task_name").val().trim() == "" ) {
		alert("Please give a name to the task");
		return;
	}
	var q_string = "&task_name=" + $("#task_name").val().trim();
	if ( $("#task_description").val().trim() == "" ) {
		alert("Please enter description of this task");
		return;
	}
	if ( $("#task_instructions").val().trim() == "") {
		alert("Please enter instructions for this task");
		return;
	}
	q_string += "&task_instructions=" + encodeURIComponent($("#task_instructions").val().trim());
	q_string += "&task_description=" + $("#task_description").val().trim();
	q_string += "&goal_id=" + $("#goal_id").val();
	q_string += "&tool_id=" + $("#tool_id").val();
	q_string += "&task_id=" + $("#task_list").val();
	q_string += "&product_id=" + $("#product_id").val();
	q_string += "&assignment_method=" + $("#assignment_method:checked").val();
	$.ajax({
		type:		"GET",
		dataType:	"json",
		cache:		"false",
		url:		"product_functions.php",
		data:		"action=saveTask" + q_string,
		success:	function(obj) {
			$("#status_area").html(obj.status_msg);
			if ( obj.query_status == 0 && obj.last_id > 0 ) {
				$("#task_list").append(
					$('<option></option>').val(obj.last_id).html(obj.task_name));
				obj.task_id = obj.last_id;
				if ( $("#assignment_method:checked").val() <= 2 ) {
					$("#assign_role,#assign_user").css("display","inline");
				}
			}
			$("#task_list").val(obj.task_id);
			$("#manage_product :input").each(function() {
				$(this).attr('disabled', false);
			});
		}
	});

}
function show_buttons() {
	if ( $("#task_list").val() <= 0 ) {
		return;
	}
	if ( $("#assignment_method:checked").val() <= 2 ) {
		$("#assign_role,#assign_user").css("display","inline");
	} else {
		$("#assign_role,#assign_user").css("display","none");
	}
}
function deleteTask() {
	if ( $("#task_list").val() <= 0 ) {
		return;
	}
	var go = confirm("Are you sure you want to delete this task?");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"product_functions.php",
		data:		"action=deleteTask&task_id=" + $("#task_list").val(),
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				$("#task_list option[value='" + obj.task_id + "']").remove();
				$("#task_list").val(-1);
				show_task_details();
			}
			$("#status_area").html(obj.status_msg);
			$("#manage_product :input").each(function() {
				$(this).attr('disabled', false);
			});
		}
	});
}

function cancel_all() {
	$("#manage_product :input").each(function() {
		$(this).attr('disabled', false);
	});
	$("#manage_task :input").each(function() {
		$(this).attr('disabled', false);
	});
	$("#assign_role,#assign_user").css("display", "none");
	return true;
}

function assignRole() {
	window.open("task_roles.php?task_name=" + $("#task_list :selected").text() + "&task_id=" + $("#task_list").val() + "&project_name=" + $("#project_id :selected").text() + "&product_name=" + $("#product_opt :selected").text(), "assign_task_roles", "menubar=0,height=600,width=800");
}
function assignUser() {
	window.open("task_users.php?project_id=" + $("#project_id").val() + "&task_id=" + $("#task_list").val() + "&project_name=" + $("#project_id :selected").text() + "&product_name=" + $("#product_opt :selected").text() + "&task_name=" + $("#task_list :selected").text(), "assign_task_users", "menubar=0,height=600,width=800");
}

$("#product_name").change(function() {
	alert($('.target').text() + " changed");
	$("#manage_task :button").each(function() {
		$(this).attr('disabled', true);
	}).change();
});

</script>
 
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<form name='product' >
<table border=0 width='100%' height="70" align=center>
    <tr><!-- dropdown list -->
	  <td width='50%' align='center'>Project<br><div id=project_dd></div></td>
      <td width='50%' align='center'>Product<br><div id=products_dd>
	    </div></td>
    </tr>
    <!-- end of dropdown lists -->
</table>
</form>
<table border=0 width=100% aligh=center>
  <tr>
    <td align=right><div id=status_area>&nbsp;</div></td>
  </tr>
  <tr>
    <td align=center><div id=edit_area></div></td>
  </tr>
</table>
</div></body>
