<?
require_once("include/config.inc");
$text_resource_id = $_GET['text_resource_id'];

// show the text_item
if ( isset($_GET['action']) && $_GET['action'] == 'showTextItem' ) {
	$sql = sprintf("SELECT * FROM text_item WHERE text_resource_id = %d AND language_id = %d", 
			$_GET['text_resource_id'], $_GET['language_id']);
	$st = mysql_query($sql);
	$rt = mysql_fetch_assoc($st);
	$rt['text_item_id'] = isset($rt['id']) ? $rt['id'] : -1;
	$rt['text'] = isset($rt['text']) ? $rt['text'] : '';
	$rt['sql'] = $sql;
	$rt['query_status'] = mysql_error();
	echo json_encode($rt);
	exit;
}

// update/save text_item
if ( isset($_GET['action']) && $_GET['action'] == 'saveTextItem') {
	$text = isset($_GET['text']) ? urldecode($_GET['text']) : "";
	$text = SQLString($text, "text");
	if ( isset($_GET['text_item_id']) && $_GET['text_item_id'] > 0 ) {
		// update
		$sql = sprintf("UPDATE text_item SET text = %s WHERE id = %d",
				$text, $_GET['text_item_id']);
	} else {
		// insert
		$sql = sprintf("INSERT INTO text_item (text_resource_id, language_id, text) VALUES(%d, %d, %s) ",
				$_GET['text_resource_id'], $_GET['language_id'], $text);
	}
	$st = mysql_query($sql);
	$rt['text_item_id'] = mysql_insert_id() > 0 ?  mysql_insert_id() : $_GET['text_item_id'];
	if ( $st ) {
		$rt['status_msg'] = "Text item saved successfully";
	} else {
		$rt['status_msg'] = mysql_error();
	}
	echo json_encode($rt);
	exit;
}

// deleteTextItem
if ( isset($_GET['action']) && $_GET['action'] == 'deleteTextItem' ) {
	$sql = "DELETE FROM text_item where id = " . $_GET['text_item_id'];
	$st = mysql_query($sql);
	if ( $st ) {
		$rt = array('query_status' => 0, 'status_msg' => 'Text item deleted successfully', 'sql' => $sql);
	} else {
		$rt = array('query_status' => 1, 'status_msg' => mysql_error(), 'sql' => $sql);
	}
	echo json_encode($rt);
	exit;
}
				
$name = urldecode($_GET['name']);
$description = urldecode($_GET['description']);

// get active language list and build initial select options
$sql = "SELECT * FROM language WHERE status = 0";
$st = @mysql_query($sql);
$languageOpt = "<SELECT id=language_id name=language_id onChange='showTextItem();'>";
$languageOpt .= "<option value=-1>Select Language from list</option>";
while ( $language = @mysql_fetch_assoc($st) ) {
	$languageOpt .= "<option value=" . $language['id'] . ">" . $language['language'] . " - " . $language['language_desc'] . "</option>";
}
$languageOpt .= "</SELECT>";

?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage text items for resource <? echo $name; ?></title>
<link rel="stylesheet" href="css/basic.css" type="text/css" />
<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script language=javascript>

function showTextItem( id ) {
	if ( language_id < 0 ) {
		$("#text").val('');
		return;
	}
	var query = "&language_id=" + $("#language_id").val() + "&text_resource_id=" + $("#text_resource_id").val();
	if ( $('#text_item_id').val() > 0 ) {
		query += "&text_item_id=" + $('#text_item_id').val();
	}
	//var text = $("#text").val().replace(/\n/g, '<br>');
	var text = encodeURIComponent($("#text").val());
	//$.ajaxSetup({ scriptCharset: "utf-8" ,contentType: "application/x-www-form-urlencoded; charset=UTF-8" });
	$.ajax({
		type: "GET",
		cache: "false",
		dataType: "json",
		url: "manage_text_items.php",
		data: "action=showTextItem" + query,
		success: function(obj) {
			$("#status").html(obj.status);
			$("#text").val(obj.text);
			if ( obj.text_item_id > 0 ) {
				$('#text_item_id').val( obj.text_item_id );
			} else {
				$('#text_item_id').val( -1 ) ;
			}
			showButtons();
		}
	});
}

function saveTextItem() {
	if ( $("#text").val().length = 0 ) {
		alert("Please enter text for this item");
		return;
	}
	var q_string = "&language_id=" + $("#language_id").val() + "&text=" + encodeURIComponent($("#text").val());
	if (  $("#text_item_id").val() > 0 ) {
		q_string += "&text_item_id=" + $("#text_item_id").val();
	}
	q_string += "&text_resource_id=" + $("#text_resource_id").val();
	$.ajax({
		type:			"GET",
		cache:			"false",
		dataType:		"json",
		data:			"action=saveTextItem" + q_string,
		url:			"manage_text_items.php",
		success:		function(obj) {
			$("#status_area").html(obj.status_msg);
			$("#text_item_id").val(obj.text_item_id);
			showButtons();
		}
	});
}

function deleteTextItem() {
	var go = confirm("Are you sure you want to delete this text item?");
	if ( go == false ) {
		return;
	}
	$.ajax({
		type:			"GET",
		cache:			"false",
		dataType:		"json",
		data:			"action=deleteTextItem&text_item_id=" + $("#text_item_id").val(),
		url:			"manage_text_items.php",
		success:		function(obj) {
			$("#status_area").html(obj.status_msg);
			if ( obj.query_status == 0 ) {
				$("#language_id option:nth(0)").attr("selected", "selected");
				$("#text").val("");
			}
			showButtons();
		}
	});
}

function showButtons() {
	var update_b = "<INPUT type=button class=btn id=update value=Update onClick='saveTextItem();' />";
	var add_b = "<INPUT type=button class=btn id=add value='Add New' onClick='saveTextItem();' />";
	var delete_b = "<INPUT type=button class=btn id=delete value=Delete onClick='deleteTextItem();' />";
	if ( document.text_item.language_id.selectedIndex == 0 ) {
		$("#buttons").html('');
		return;
	}
	if ( document.getElementById('text_item_id').value > 0 ) {
		// show update and delete button
		$("#buttons").html(update_b + '&nbsp;&nbsp;&nbsp' + delete_b);
	} else {
		// show add button
		$("#buttons").html(add_b);
	}
	return;
}

</script>
</head>

<body>
<form name="text_item" >
	<INPUT type=hidden id=text_resource_id value=<? echo $text_resource_id; ?> />
	<INPUT type=hidden id=text_item_id value=-1 />
    <table width="100%" height="297"  border="0">
        <tr>
            <td height="75" align="center">Managing text items for resource name <? echo $name; ?>
			<p>Description: <? echo $description; ?></p>
            <p>Please select from available languages and enter necessary information then click Update button </p></td>
        </tr>
        <tr>
            <td align="center"><table width="70%" height="87"  border="0">
				<tr><td colspan=3 align=right><div id=status_area></div></td></tr>
                <tr>
                    <td width="11%" align="right">Language</td>
                    <td width="5%" align="left">&nbsp;</td>
                    <td width="84%" align="left"><div id=languageOpt><?echo $languageOpt; ?></div></td>
                </tr>
                <tr>
                    <td height="32" align="right" valign="top">Text</td>
                    <td valign="top">&nbsp;</td>
                    <td valign="top">
					    <textarea id=text name=text rows=10 cols=60 onkeypress="showButtons();"></textarea>
					</td>
                </tr>
                <tr>
                    <td colspan=3 valign="top">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="3" align="center" valign="top"><div id=buttons></div></td>
				</tr>
                <tr>
                    <td colspan=3 valign="top">&nbsp;</td>
                </tr>
            </table></td>
        </tr>
        <tr>
            <td align="center"><div id=status></div></td>
        </tr>
        <tr>
            <td align="center"><div id=debug></div></td>
        </tr>
    </table>
</form>
</body>
</html>

