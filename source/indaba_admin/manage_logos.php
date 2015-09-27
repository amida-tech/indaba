<? 
session_start();
if ( ! isset( $_SESSION['authuser'] )) exit;
if ( !isset($_GET['project_id']) ) exit;
require_once('include/config.inc');
$project_id = $_GET['project_id'];
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Manage Sponsor Logos</title >
<link rel="stylesheet" href="css/basic.css" type="text/css" /> 
<style type="text/css">
.img_box {
	width: 180px;
	height: 140px;
	float: left;
	padding: 5px 5px 5px 5px;
	font-size: 12px;
	text-align: center;
	border: 1px solid black;
}
</style>
<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
<script src="include/ajaxfileupload.js" language="javascript"  type="text/javascript"></script>
<script language=javascript type="text/javascript">

function upload_logo_file() {
    $("#loading")
    .ajaxStart(function(){
        $(this).show();
    })
    .ajaxComplete(function(){
        $(this).hide();
    });
	var url = "upload_logo_file.php?project_id=" + $("#project_id").val();
    $.ajaxFileUpload ({
        url:        	url, 
        method:     	"GET",
        secureuri:  	false,
        fileElementId:  "filename_id",
        dataType:   	'json',
        success:    	function(data, status) {
            if ( typeof(data.error) != 'undefined') {
                if ( data.error != '' ) {
                    alert("error => " + data.error);
                } else {
                    alert( data.msg );
                }
            }
			window.location.reload();
        },
        error: function (data, status, e) {
            alert( e );
        }
    });
    return false;
}

function delete_logo(logo) {
	// alert("Deleting logo " + logo);
	$.ajax({
		type:		'GET',
		cache:		'false',
		dataType:	'json',
		url:		'delete_logo_file.php',
		data:		'project_id=' + $("#project_id").val() + "&logo=" + logo,
		success:        function(obj) {
			if ( obj.error == 0 ) {
				alert("Logo deleted");
				window.location.reload();
			} else {
				alert(obj.msg);
			}
		}
	});
	return;
}

</script>
</head>
<body>

<div style="float:left; width:100%; height:40px; margin-top:30px;">
	<INPUT TYPE=hidden id=project_id value="<?php echo $_GET['project_id']; ?>" />
	<INPUT TYPE=file id="filename_id" name="filename_id" /><INPUT type=button value="Upload Sponsor Logo" class=btn onClick="upload_logo_file();" />
</div>
<div style="float:left; width:100%; height:40px; margin-top:10px;">
	<img id="loading" src="images/loading.gif" style="display:none;">
</div>

<div style="float:left; width:610px; margin-top:40px; border:solid black 1px;">
	<?php
		$sql = "SELECT sponsor_logos FROM project WHERE id = " . $project_id;
		$st = mysql_query($sql);
		$row = mysql_fetch_assoc($st);
		$logos = preg_split('/ /', $row['sponsor_logos'], -1, PREG_SPLIT_NO_EMPTY);
		$i = 0;
		foreach ( $logos as $logo ) {
			if ( $i == 4 ) {
				$i = 0;
				echo "</div>";
				echo "<div style='float:left; width:700px; margin-top:40px; border:solid black 1px;'>";
			}
			echo  "<div style='float:left; width:100px; height:100px; border:solid black 1px;'><img src='" . $sponsor_logos ."/" . $logo . "' width=80px height=80px ></div>";
			echo  "<div style='float:left; width:50px; height:100px; border:solid black 1px;'><img src='images/delete_btn.gif' width=30px height=30px onClick=\"delete_logo('" . $logo . "');\"></div>";
			$i++;
		}
	?>
</div>

</body>
</html>
