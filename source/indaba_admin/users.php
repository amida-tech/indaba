<?php
require_once('header.php');
$required = "<font color=red>*</font>";
?>

<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/jquery-ui.js" language="javascript"  type="text/javascript"></script>
<script src="include/ajaxfileupload.js" language="javascript"  type="text/javascript"></script>

<script language="javascript"  type="text/javascript"> 
$(document).ready(function() {
	userSelected(-1);
});

String.prototype.trim = function() {
	    return this.replace(/^\s+|\s+$/g,"");
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
                    $("#user_id").val(selected);
                    userSelected(selected);
                } else {
                    $(this).removeClass("ui-selected");
                }
                i ++;
            });
        }
    });
});

function userSelected(id) {
	if ( isNaN(id) ) {
		id = $("#user_id").val();
	}
	$.ajax({
		type: "GET",
		cache: "false",
		dataType: "json",
		url: "user_functions.php",
		data: "action=showUser&id=" + id,
		success: function(obj) {
			$("#source_list").html(obj.user_list);
			$("#username").val(obj.username);
			$("#first_name").val(obj.first_name);
			$("#last_name").val(obj.last_name);
			$("#email").val(obj.email);
			$("#user_language").html(obj.user_language);
			$("#user_organization").html(obj.user_organization);
			if ( obj.forward_inbox_msg == 1 ) {
				$("#forward_inbox_msg").attr('checked',true);
			} else {
				$("#forward_inbox_msg").attr('checked',false);
			}
			if ( obj.status == 1 ) {
				$("input[name='status']")[0].checked = true;
			} else {
				$("input[name='status']")[1].checked = true;
			}
			$("#number_msgs_per_screen").val(obj.number_msgs_per_screen);
			if ( obj.email_detail_level == 0 ) {
				$("input[name='email_detail_level']")[0].checked = true;
			} else {
				$("input[name='email_detail_level']")[1].checked = true;
			}
			if ( obj.site_admin == 1 ) {
				$("#site_admin").attr('checked', true);
			} else {
				$("#site_admin").attr('checked', false);
			}
			$("#timezone").val(obj.timezone);
			$("#password").val(obj.password);
			$("#password2").val(obj.password);
			$("#phone").val(obj.phone);
			$("#cell_phone").val(obj.cell_phone);
			$("#address").val(obj.address);
			$("#location").val(obj.location);
			$("#bio").val(obj.bio);
			$("#loading").attr("display", "none");
			if ( $("#user_id").val() > 0 ) {
				$("#photo_area").show();
			} else {
				$("#photo_area").hide();
			}
			if ( typeof(obj.photo) != 'undefined' && obj.photo != null ) {
				var photo_html = "<img src='" + obj.photo + "' width=120px hight=140px >";
				$("#photo").html(photo_html);
			} else {
				$("#photo").html('');
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
					$("#photo").html("<img id=user_file src='" + data.filename + "' width=120px height=140px >");
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


function deleteUser() {
	if ( $("#user_id").val() <= 0 ) {
		return;
	}
	if ( $("#user_id").val() == 1 ) {
		alert("The super user cannot be deleted.");
		return;
	}
	var go = confirm("Are you sure you want to delete the user?");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:		"GET",
		cache:		"false",
		dataType:	"json",
		url:		"user_functions.php",
		data:		"action=deleteUser&id=" + $("#user_id").val(),
		success:	function(obj) {
			if ( obj.query_status == 0 ) {
				userSelected(-1);
			} 
			$("#status_area").html(obj.status_msg);
		}
	});
}

function filterUser() {
	var name_like = $("#search_username").val();
	$("#source_list").find('li').each(function() {
		if ( $(this).attr('id') > 0 && $(this).text().search(new RegExp(name_like, 'i')) < 0 ) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
}

function saveUser() {
	// validate input
	var q_string = '';
	// check username
	if (  ! /^[a-zA-Z0-9._-]{4,}$/.test($("#username").val()) ) {
		alert("User name can only contain letters, numbers, and [.], [-], [_]. It must be at least 4 letters");
		return false;
	}
	q_string += "&username=" + $("#username").val();
	var forward_inbox_msg = 0;
	if ( $("#forward_inbox_msg:checked").val() == 'on' ) {
		forward_inbox_msg = 1;
	}
	var site_admin = 0;
	if ( $("#site_admin:checked").val() == 'on' ) {
		site_admin = 1;
	}
	// first and last name
	var first_name = $("#first_name").val();
	var last_name = $("#last_name").val();
	if (  first_name.trim() == "" ||  last_name.trim() == "" ) {
		alert("User's first name and last name are required");
		return false;
	}
	q_string += "&first_name=" + encodeURIComponent(first_name) + "&last_name=" + encodeURIComponent(last_name);
	//organization
	var organization_id = $("#organization_id").val();
	if ( organization_id <= 0 ) {
		alert("Please assign this user to an organization from the list");
		return false;
	}
	q_string += "&organization_id=" + organization_id;
	//email
	if (! /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test($("#email").val()) ) {
		alert("Please enter a valid email address!");
		return false;
	}
	q_string += "&email=" + $("#email").val();
	// password
	if ( $("#password").val().length < 3 ) {
		alert("Password must be at least 3 letters");
		return false;
	}
	if ( $("#password").val() != $("#password2").val() ) {
		alert("Passwords entered do not match");
		return false;
	}
	if (( isNaN($("#number_msgs_per_screen").val()) || $("#number_msgs_per_screen").val() < 0 ) && $("#number_msgs_per_screen").val() != '' ) {
		alert("Msg per Screen must be a positive number");
		return;
	}
	q_string += "&password=" + encodeURIComponent($("#password").val());
	q_string += "&id=" + $("#user_id").val();
	q_string += "&forward_inbox_msg=" + forward_inbox_msg;
	q_string += "&site_admin=" + site_admin;
	q_string += "&language_id=" + $("#language_id").val();
	q_string += "&status=" + $("#status:checked").val();
	q_string += "&email_detail_level=" + $("#email_detail_level:checked").val();
	q_string += "&number_msgs_per_screen=" + $("#number_msgs_per_screen").val();
	q_string += "&timezone=" + encodeURIComponent($("#timezone").val());
	q_string += "&phone=" + encodeURIComponent($("#phone").val());
	q_string += "&cell_phone=" + encodeURIComponent($("#cell_phone").val());
	q_string += "&address=" + encodeURIComponent($("#address").val());
	q_string += "&location=" + encodeURIComponent($("#location").val());
	q_string += "&bio=" + encodeURIComponent($("#bio").val());
	// alert(q_string);
	$.ajax({
		type: "GET",
		cache: "false",
		dataType: "json",
		url: "user_functions.php",
		data: "action=saveUser" + q_string,
		success: function(obj) {
			userSelected(obj.id);
			$("#status_area").html(obj.status_msg);
			$("#user_id").val(obj.id);
		}
	});
}

</script>

<link rel="stylesheet" href="css/basic.css" type="text/css" />
<table border=0 width=900>
  <tr>
    <td colspan=2 align=right>Search Users &nbsp;<INPUT type=text id=search_username size=10 onKeyUp='filterUser();' /></td>
  </tr>
  <tr><td colspan=2>&nbsp</td></tr>
  <tr>
    <td>&nbsp;</td><td align=right><div id=status_area class=status></div></td>
  </tr>
  <tr>
    <td width=300 align=center>USER LIST</td>
	<td widtn=600 align=center>USER DETAILS <font style="font-size:13px; color:red">* are required fields</font></td>
  </tr>
  <tr>
    <td valign=top align=center ><div style="height:500px;width:310px; overflow:auto;" ><UL id=source_list style="min-height:500px;overflow:none;" ></UL></div></td>
	<td valign=top align=left >
	  <table border=0 width=100%>
	    <tr>
		  <td width=230 align=right>User Name &nbsp;</td>
		  <td width=150 align=left><INPUT id=username type=text size=20 /><? echo $required; ?></td>
		  <td width=120 rowspan=10>
		  	<div id=photo_area style="width:120px; display:none;" align=center >
				<div align=center>User Photo</div>
				<div>&nbsp;</div>
		  		<div id=photo align=center style="width:120px; hight:140;"></div>
				<div><img id="loading" src="images/loading.gif" style="display:none;"></div>
				<div>&nbsp;</div>
				<form name='photo_form' method=POST enctype="multipart/form-data">
					<div>
						<INPUT id="fileToUpload" type="file" name="fileToUpload" />
					</div>
					<div>&nbsp;</div>
					<div class=btn align=center onClick="return ajaxFileUpload();" >Upload</div>
				</form>
			</div></td>
		</tr>
		<tr>
		  <td align=right>First Name &nbsp;</td>
		  <td align=left><INPUT id=first_name type=text size=20 /><? echo $required; ?></td>
		</tr>
		<tr>
		  <td align=right>Last Name &nbsp;</td>
		  <td align=left><INPUT id=last_name type=text size=20 /><? echo $required; ?></td>
		</tr>
		<tr>
		  <td align=right>Email Address &nbsp;</td>
		  <td align=left><INPUT id=email type=text size=20 /><? echo $required; ?></td>
		</tr>
		<tr>
		  <td align=right>Password &nbsp;</td>
		  <td align=left><INPUT id=password type=text size=20 /><? echo $required; ?></td>
		</tr>
		<tr>
		  <td align=right>Repeat Password &nbsp;</td>
		  <td align=left><INPUT id=password2 type=text size=20 /><? echo $required; ?></td>
		</tr>
		<tr>
		  <td align=right>Organization &nbsp;</td>
		  <td align=left><div id=user_organization></div></td>
		</tr>
		<tr>
		  <td align=right>User Status &nbsp;</td>
		  <td align=left><INPUT id=status type=radio name=status value=1 />Active
		  				 <INPUT id=status type=radio name=status value=0 />Blocked
		  </td>
		</tr>
		<tr>
		  <td align=right>Site Admin &nbsp;</td>
		  <td align=left><INPUT id=site_admin type=checkbox />
		  </td>
		</tr>
		<tr>
		  <td align=right>User Language &nbsp;</td>
		  <td align=left><div id=user_language></div>
		</tr>
		<tr>
		  <td align=right>Forward Inbox &nbsp;</td>
		  <td align=left><INPUT id=forward_inbox_msg type=checkbox /></td>
		</tr>
		<tr>
		  <td align=right>Msg per Screen &nbsp;</td>
		  <td align=left><INPUT id=number_msgs_per_screen type=text size=4 /></td>
		</tr>
		<tr>
		  <td align=right>Email Detail Level &nbsp;</td>
		  <td align=left><INPUT id=email_detail_level name=email_detail_level type=radio value=0 />Alert Only
		  				 <INPUT id=email_detail_level name=email_detail_level type=radio value=1 />Full</td>
		</tr>
		<tr>
		  <td align=right>TimeZone (UTC offset)&nbsp;</td>
		  <td align=left><INPUT id=timezone type=text size=4 /></td>
		</tr>
		<tr>
		  <td align=right>Phone Number &nbsp;</td>
		  <td align=left><INPUT id=phone type=text size=20 /></td>
		</tr>
		<tr>
		  <td align=right>Cell Phone &nbsp;</td>
		  <td align=left><INPUT id=cell_phone type=text size=20 /></td>
		</tr>
		<tr>
		  <td align=right>Location &nbsp;</td>
		  <td align=left><INPUT id=location type=text size=20 /></td>
		</tr>
		<tr>
		  <td align=right>Address &nbsp;</td>
		  <td align=left><textarea id=address cols=30 rows=4 ></textarea></td>
		</tr>
		<tr>
		  <td align=right valign=middle>User Bio &nbsp;</td>
		  <td align=left><textarea id=bio cols=30 rows=6 ></textarea></td>
		</tr>
	  </table>
	</td>
  </tr>
  <tr><td colspan=2>&nbsp;</td></tr>
  <tr><td colspan=2 align=center>
        <table border=0 width=100%>
		  <tr>
		    <td width=33% align=center><INPUT type=button class=btn id=save_user value="Save User" onClick="saveUser();" /></td>
		    <td width=33% align=center><INPUT type=button class=btn id=del_user value="Delete User" onClick="deleteUser();" /></td>
		    <td width=33% align=center><INPUT type=button class=btn id=reset value="Cancel" onClick="userSelected();" />
				<INPUT type=hidden id=user_id />
			</td>
		  </tr>
		</table></td>
  </tr>
  <tr><td colspan=2>&nbsp;</td></tr>

</table>
