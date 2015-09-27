<?php
require_once('header.php');
?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>

<script type="text/javascript" src="include/tinybox.js"></script>
<link rel="stylesheet" href="css/tinybox.css" />
<script language="javascript"  type="text/javascript"> 

// Get drop down
$(document).ready(function() {
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"html",
		url:		"matrix_functions.php",
		data:		"action=show_access_matrix_opt",
		success:	function(data) {
			$("#access_matrix_dd").html(data);
		}
	});
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"html",
		url:		"matrix_functions.php", 
		data:       "action=show_view_matrix_opt",
		success:	function(data) {
			$("#view_matrix_dd").html(data);
		}
	});

});

function access_matrixSelected() {
	if ( $("#access_matrix_id").val() == -1 ) {
		$("#edit_area").html('');
		$("#status_area").html('');
		return;
	}
	if ( $("#status_area").text() == "Change not saved" ) {
		var go = confirm("Do you want to discard the changes you just made?");
		if ( go == false ) {
			return;
		}
	}
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "matrix_functions.php",
		data:		"action=show_access_matrix&access_matrix_id=" + $("#access_matrix_id").val(),
		success:    function(obj) {
			//$("#view_matrix_dd:selected").val('-1');
			$("#view_matrix_dd option:nth(0)").attr("selected", "selected");
			$("#edit_area").html(obj.form);
			$("#rights_area p").html(obj.rights_form);
			$("#status_area").html(obj.status);
			if ( $("#access_matrix_id").val() > 0 ) {
				$("#clone").css("display", "inline");
			} else {
				$("#clone").css("display", "none");
			}
			T$('showRight').onclick = function() {
				var rights_id = "rights_id_" + $("#rights_id").val();
				var msg = $("#" + rights_id).val();
				TINY.box.show(msg, 0,0,0,0,10);
			};
		}
	})
}

function view_matrixSelected() {
	if ( $("#view_matrix_id").val() == -1 ) {
		$("#edit_area").html('');
		$("#status_area").html('');
		return;
	}
	if ( $("#status_area").text() == "Change not saved" ) {
		var go = confirm("Do you want to discard the changes you just made?");
		if ( go == false ) {
			return;
		}
	}
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "matrix_functions.php",
		data:		"action=show_view_matrix&view_matrix_id=" + $("#view_matrix_id").val(),
		success:    function(obj) {
			$("#access_matrix_dd option:nth(0)").attr("selected", "selected");
			$("#edit_area").html(obj.form);
			$("#rights_area p").html(obj.rights_form);
			$("#status_area").html(obj.status);
			if ( $("#view_matrix_id").val() > 0 ) {
				$("#clone").css("display", "inline");
			} else {
				$("#clone").css("display", "none");
			}
		}
	})
}

function clone_access_matrix() {
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "matrix_functions.php",
		data:       "action=clone_access_matrix&access_matrix_id=" + $("#access_matrix_id").val() + 
						"&name=" + encodeURIComponent($("#name").val()) +
						"&description=" + encodeURIComponent($("#description").val()) + 
						"&default_value=" + $("#default_value:checked").val(),
		success:    function(obj) {
			$("#status_area").html(obj.msg);
			if ( obj.status == 0 ) {
				$("#access_matrix_dd").html(obj.access_matrix_opt);
				$("#edit_area").html(obj.form);
				$("#rights_area p").html(obj.rights_form);
				$("#name").val(obj.name);
			}
		}
	});
}
function clone_view_matrix() {
	$.ajax({
		type:       "GET",
		cache:      "false",
		dataType:   "json",
		url:        "matrix_functions.php",
		data:       "action=clone_view_matrix&view_matrix_id=" + $("#view_matrix_id").val() + 
						"&name=" + encodeURIComponent($("#name").val()) +
						"&description=" + encodeURIComponent($("#description").val()) + 
						"&default_value=" + $("#default_value:checked").val(),
		success:    function(obj) {
			$("#status_area").html(obj.msg);
			if ( obj.status == 0 ) {
				$("#view_matrix_dd").html(obj.view_matrix_opt);
				$("#edit_area").html(obj.form);
				$("#rights_area p").html(obj.rights_form);
				$("#name").val(obj.name);
			}
		}
	});
}




</script>
 
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<form >
<table border=0 width='100%' height="70" align=center>
    <tr><!-- function buttons -->
      <td align=center width='50%'><div id=access_matrix_dd></div></td>
      <td align=center width='50%'><div id=view_matrix_dd></div></td>
    </tr>
    <!-- end of User functions -->
</table>
</form>
<table border=0 width=100%>
  <tr>
    <td align=right><div id=status_area><p></p></div></td>
  </tr>
  <tr>
    <td align=center><div id=edit_area><p></p></div></td>
  </tr>
  <tr>
    <td align=center><div id=rights_area><p></p></div></td>
  </tr>
</table>
</div></body>
