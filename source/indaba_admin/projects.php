<?php
require_once('header.php');
?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script src="include/ajaxfileupload.js" language="javascript"  type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="css/basic.css"/>
<script language=javascript>
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}

$(document).ready(function() {
	get_all_projects();
});
function get_all_projects(id) {
	var show_id = '';
	if ( typeof(id) != 'undefined' ) {
		show_id = '&id=' + id;
	}
	$.ajax({
		type:           'GET',
		cache:          'false',
		dataType:       'json',
		data:           'action=show_project_list' + show_id,
		url:			'project_functions.php',
		success:		function(obj) {
			$("#source_list").html(obj.project_list);
			$("#admin_user_list").html(obj.admin_user_list);
			$("#organization_list").html(obj.organization_list);
			$("#access_matrix_list").html(obj.access_matrix_list);
			$("#view_matrix_list").html(obj.view_matrix_list);
			$("#study_period_list").html(obj.study_period_list);
			if ( !isNaN(id) && id > 0 ) {
				show_project(id);
			}
		}
	});
}

function filter_project() {
	var name_like = $("#search_project").val();
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
					$("#project_id").val(selected);
                    show_project(selected);
                } else {
                    $(this).removeClass("ui-selected");
                } 
                i ++;
            });
        }
    });
});

function show_project(id) {
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		data:		'action=show_project&id=' + id,
		url:		'project_functions.php',
		success:	function(obj) {
			if ( obj.query_status == 1 ) {
				$("#status_area").html(obj.query_msg);
			} else {
				$("#status_area").html('');
				$("#project_owner").html(obj.project_owner);
				$("#project_id").val(id);
				$("#code_name").val(obj.code_name);
				$("#description").val(obj.description);
				$("#admin_user_id").val(obj.admin_user_id);
				$("#organization_id").val(obj.organization_id);
				$("#access_matrix_id").val(obj.access_matrix_id);
				$("#view_matrix_id").val(obj.view_matrix_id);
				$("#start_time").val(obj.start_time);
				$("#start_time_org").val(obj.start_time);
				$("#close_time").val(obj.close_time);
				$("#ready_to_start").val(obj.ready_to_start);
				$("#study_period_id").val(obj.study_period_id);
				if ( id > 0 ) {
					$("#photo_area").css("display", "inline");
					$("#project_buttons").css("display", "inline");
					if ( obj.logo_path != '' ) {
						$("#photo").html("<img src='" + obj.logo_path + "' width=120px height=140px  >");
					} else {
						$("#photo").html('');
					}
				} else {
					$("#photo_area").css("display", "none");
					$("#project_buttons").css("display", "none");
				}
				if ( obj.is_active == 0 ) {
					$("input[name='is_active']")[0].checked = true;
				} else {
					$("input[name='is_active']")[1].checked = true;
				}
				if ( obj.visibility == 1 ) {
					$("input[name='visibility']")[0].checked = true;
				} else {
					$("input[name='visibility']")[1].checked = true;
				}
			}
		}
	});
}

function ajaxFileUpload() {
	$("#loading")
	.ajaxStart(function(){
		$(this).show();
	})
	.ajaxComplete(function(){
		$(this).hide();
	});

	$.ajaxFileUpload ({
		url:			'doajaxfileupload.php',
		method:			'GET',
		secureuri:		false,
		fileElementId:	'fileToUpload',
		dataType: 		'json',
		success: 		function (data, status) {
			if(typeof(data.error) != 'undefined') {
				if(data.error != '') {
					alert(data.error);
				}else {
					// alert(data.msg);
					$("#photo").html("<img id=logo_path src='" + data.filename + "' width=140px height=140px >");
				}
			}
			alert( data.msg );
		},
		error: function (data, status, e) {
			alert( e );
		}
	});
	return false;
}


function updateProject() {
	// check code_name
	var q_string = '';
	var code_name = $("#code_name").val();
	if ( ! /^[a-zA-Z0-9\._\-\s]{4,}$/.test(code_name) ) {
		alert("Code name can only contain letters, numbers, space, and [.], [-], [_]. It must be at least 4 letters");
		return false;
	}
	q_string += "&code_name=" + code_name;
	var description = $("#description").val().trim();
	q_string += "&description=" + encodeURIComponent(description);
	// check admin_user_id
	var admin_user_id = $("#admin_user_id").val();
	if ( admin_user_id <= 0 ) {
		alert("Please select an admin user from the list");
		return false;
	}
	q_string += "&admin_user_id=" + admin_user_id;
	if ( $("#organization_id").val() <= 0 ) {
		alert("Please select organization for this project");
		return false;
	}
	q_string += "&organization_id=" + $("#organization_id").val();
	// check access matrix
	if ( $("#access_matrix_id").val() < 0 ) {
		alert("Please select an Action Rights Setting for this project!");
		return false;
	}
	q_string += "&access_matrix_id=" + $("#access_matrix_id").val();

	// view matrix
	if ( $("#view_matrix_id").val() < 0 ) {
		alert("Please select an Action Rights Setting for this project!");
		return false;
	}
	q_string += "&view_matrix_id=" + $("#view_matrix_id").val();

	// start time
	if ( !/^\d{4}\-\d{2}\-\d{2}/.test($("#start_time").val()) ) {
		alert("Please use YYYY-MM-DD format to enter a start time!");
		return false;
	}
	q_string += "&start_time=" + $("#start_time").val();

	// close time
	if ( !/^\d{4}\-\d{2}\-\d{2}/.test($("#close_time").val()) && $("#close_time").val().trim() != '' ) {
		alert("Please use YYYY-MM-DD format to enter a close time!");
		return false;
	}
	q_string += "&close_time=" + $("#close_time").val();

	// study period
	if ( $("#study_period_id").val() < 0 ) {
		alert("Please select Study Period for this project!");
		return false;
	}
	q_string += "&study_period_id=" + $("#study_period_id").val();

	q_string += "&project_id=" + $("#project_id").val();
	q_string += "&is_active=" + $("#is_active:checked").val();
	q_string += "&visibility=" + $("#visibility:checked").val();
	// alert(q_string);
	$.ajax({
		type: "GET",
		cache: "false",
		dataType: "json",
		url: "project_functions.php",
		data: "action=save_project" + q_string,
		success: function(obj) {
			$("#status_area").html(obj.query_msg);
			if ( obj.query_status == 1 || project_id > 0 ) {
				return;
			}
			// need to refresh project list
			get_all_projects(obj.project_id);
			$("#project_id").val(obj.project_id);
			// enable photo upload
			if ( project_id == 0 ) {
				// clear logo
				$("#photo").html('');
			}
			$("#photo_area").css("display", "inline");
			// enable buttons
			$("#project_buttons").css("display", 'inline');
		}
	});
}

function deleteProject() {
	var go = confirm("Are you sure you want to delete this project? All properties of this project will be deleted as well!");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		data:		'action=delete_project&project_id=' + $("#project_id").val(),
		url:		'project_functions.php',
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				get_all_projects();
				show_project(0);
			}
			$("#status_area").html(obj.query_msg);
		}
	});
}


function showTargets() {
	var project_id = $("#project_id").val();
	window.open("manage_targets.php?project_id=" + project_id, "manage_targets", "menubar=0,height=600,width=800,scrollbars=1");
	return false;
}
function showRoles() {
	var project_id = $("#project_id").val();
	window.open("manage_roles.php?project_id=" + project_id, "manage_roles", "menubar=0,height=600,width=800,scrollbars=1");
	return false;
}
function showUsers() {
	var project_id = $("#project_id").val();
	window.open("manage_users.php?project_id=" + project_id, "manage_users", "menubar=0,height=600,width=800");
	return false;
}
 
function showTeams() {
	var project_id = $("#project_id").val();
	window.open("manage_teams.php?project_id=" + project_id, "manage_teams", "menubar=0,height=600,width=800");
	return false;
}
 
function showContacts() {
	var project_id = $("#project_id").val();
	window.open("manage_contacts.php?project_id=" + project_id, "manage_contacts", "menubar=0,height=600,width=800");
	return false;
}
 
function showStatus() {
	var project_id = $("#project_id").val();
	if ( $("#ready_to_start").val() < 0 ) {
		alert("This project can only be started on or after defined start_time [" + $("#start_time_org").val() + "].");
		return;
	}
	window.open("manage_status.php?project_id=" + project_id, "manage_status", "menubar=0,scrollbars=yes,height=600,width=800");
	return false;
}

function showLogos() {
	var project_id = $("#project_id").val();
	window.open("manage_logos.php?project_id=" + project_id, "manage_logos", "menubar=0,height=600,width=800");
	return false;
}

</script> 

<form id=func_form name=func_form > 
<table border=0> 
    <tr> 
        <th width="20"></th> 
    <th width="187" align=left> 
        PROJECT LIST    </th> 
    <th width="600" align=center> 
        PROJECT DETAILS <font style="font-size:13px; color:red">* are required fields</font> 
		<div id=status_area style="float:right;"></div>
    </th> 
    </tr> 
	<tr><td colspan=3 align=right><font style='font-size:12px; color:red;'> 
					</font></td> 
	</tr> 
    <tr> 
        <td></td> 
    <td valign=top> 
		<div id=source_list style='width:200px; height:400px;'>
		</div>
	 </td> 
    <td valign="top" width=70%><!-- display project details - only when display==1 --> 
      <table border=0> 
        <tr> 
          <td width=150px>Project Owner: </td> 
          <td width=350px align=left><div id=project_owner></div></td> 
        </tr> 
        <tr> 
          <td >Code Name: </td> 
          <td align=left><INPUT type=text size=25 id='code_name' name='code_name' /><font color=red>*</font></td> 
		  <td > 
		  </td> 
 
        </tr> 
        <tr> 
          <td>Description: </td> 
          <td align=left><INPUT type=text size=25 id='description' name='description' /><font color=red>*</font></td>
        </tr> 
        <tr> 
          <td>Admin User: </td> 
          <td align=left>
			<div id=admin_user_list>
			</div></td>
	  	  </td> 
        </tr>
        <tr> 
          <td>Organization: </td> 
          <td align=left>
			<div id=organization_list>
			</div></td>
	  	  </td> 
        </tr> 
        <tr> 
          <td>Action Rights Setting: </td> 
          <td align=left>
		  	<div id=access_matrix_list>
			</div></td>
        </tr> 
        <tr> 
          <td>User Privacy Setting: </td> 
          <td align=left>
		  	<div id=view_matrix_list>
			</div></td>
        </tr> 
        <tr> 
          <td>Start Time: </td> 
          <td align=left><input name="start_time" type="text" id="start_time" size=12 /><font color=red>*</font> in YYYY-MM-DD format 
		  				 <input name="start_time_org" type="hidden" id="start_time_org" />
		  </td>
        </tr> 
        <tr> 
          <td>Close Time: </td> 
          <td align=left><input name="close_time" type="text" id="close_time" size=12 /> in YYYY-MM-DD format 
		  </td>
        </tr> 
		<tr>
		  <td>Is Active: </td>
		  <td align=left><input name="is_active" type=radio id=is_active value=0 />No&nbsp;&nbsp;
		  				<input name="is_active" type=radio id=is_active value=1 />Yes
		  </td>
		</tr>
		<tr>
		  <td>Visibility: </td>
		  <td align=left><input name="visibility" type=radio id=visibility value=1 />Public&nbsp;&nbsp;
		  				<input name="visibility" type=radio id=visibility value=2 />Private
		  </td>
		</tr>
        <tr> 
          <td>Study Period : </td> 
          <td align=left>
		  	<div id=study_period_list>
			</div></td>
        </tr> 
		<tr><td colspan=2>&nbsp;</td> 
        </tr> 
        <tr valign="top"> <!-- button --> 
          <td colspan=2 align=center> 
          <INPUT type=button class=btn name='updateproject' id='updateproject' value='Update Project' onClick='updateProject();' /><INPUT type=button class=btn name='deleteproject' value='Delete Project' id='deleteproject'  onClick='deleteProject();' /><INPUT type=button class=btn value='Reset Changes' onClick='show_project($("#project_id").val());' >
		  <INPUT type=hidden id='project_id' name='project_id' />          
		  <INPUT type=hidden id='ready_to_start' value=-1 />          </td> 
        </tr> 
				<tr><td colspan=2>&nbsp</td></tr> 
		<tr> 
		  <td colspan=2><table id=project_buttons style='display:none;' border=0 width='100%'> 
		    <tr> 
			  <td align=center> 
				<INPUT class=btn type=button value='Add Targets' onClick='showTargets();' />			  </td> 
			  <td align=center> 
				<INPUT class=btn type=button value='Add Roles' onClick='showRoles();' />			  </td> 
			  <td align=center> 
				<INPUT class=btn type=button value='Add Users' onClick='showUsers();' />			  </td> 
			  <td align=center> 
				<INPUT class=btn type=button value='Add Teams' onClick='showTeams();' />			  </td> 
			  <td align=center> 
				<INPUT class=btn type=button value='Add Contacts' onClick='showContacts();' />			  </td> 
			</tr> 
			<tr><td colspan=5> &nbsp;</td></tr> 
			<tr><td colspan=5 align=center > 
				<table>
				  <tr>
				    <td width=50%>
					  <INPUT class=btn type=button value='Manage Project Status' onClick='showStatus();' />				
					</td>
				    <td width=50%>
					  <INPUT class=btn type=button value='Manage Sponsor Logos' onClick='showLogos();' />				
					</td>
				  </tr>
				</table
			</td></tr> 
	      </table></td> 
		</tr> 
		      </table> 
    </td> 
	<td width=30% valign=top> 
			<div id=photo_area style="width:120px; display:none; " align=center > 
				<div align=center>Project Logo</div> 
				<div>&nbsp;</div> 
		  		<div id=photo align=center style="width:140px; hight:140;"> 
									</div> 
				<div><img id="loading" src="images/loading.gif" style="display:none;"></div> 
				<div>&nbsp;</div> 
				<form name='photo_form' method=POST enctype="multipart/form-data"> 
					<div> 
						<INPUT id="fileToUpload" type="file" name="fileToUpload" /> 
					</div> 
					<div>&nbsp;</div> 
					<div class=btn style="width:100px; height:20px;" align=center onClick="return ajaxFileUpload();" >Upload</div> 
				</form> 
			</div> 
	</td> 
    </tr> 
</table> 
</form> 
