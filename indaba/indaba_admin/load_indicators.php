<?php
session_start();
if ( ! isset( $_SESSION['authuser'] )) exit;

require_once("./include/config.inc");

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>Load Indicators from File</title>
	<link rel="stylesheet" href="css/basic.css" type="text/css" />
	<script src="include/jquery.js" language="javascript"  type="text/javascript"></script>
	<script src="include/ajaxfileupload.js" language="javascript"  type="text/javascript"></script>
	<script language=javascript>
function ajaxFileUpload() {
	$("#loading")
	.ajaxStart(function(){
		$(this).show();
	})
	.ajaxComplete(function(){
		$(this).hide();
	});

	var validate_only = 0;
	if ( $("#validate_only").attr("checked") == true ) {
		validate_only = 1;
	}
	var url = 'batch_indicator.php?validate_only=' + validate_only;

	$.ajaxFileUpload ({
		url:			url,
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
					$("#status_area").append('-----------\n');
					$("#status_area").append(data.result);
					alert( data.msg );
				}
			}
		},
		error: function (data, status, e) {
			alert( e );
		}
	});
	return false;
}


	</script>
</head>
<body>
	<div style='width:760px; height:580px; font-size:12px;'>
		<div style='margin: 5px 10px 10px 50px;'>
			Please download the samples from the table below. Make sure to save the <br />
			ready-to-upload file in WINDOWS CSV format with coresponding file names before upload.<br />
			When Validate Only is checked, the file will not be loaded into the database. 
		</div>
		<div style='margin: 5px 10px 10px 20px;'>
			File Name Naming Conventions and sample files<br />
			<div style='width:610px; margin-left:20px; border: 1px solid black;'>
				<div style='float:left; width:100px; border: 1px solid black;'>Indicator Type</div>
				<div style='float:left; width:100px; border: 1px solid black;'>Upload File Name</div>
				<div style='float:left; width:200px; border: 1px solid black;'>Download Sample File</div>
				<div style='float:left; width:200px; border: 1px solid black;'>Process Log</div>

				<div style='float:left; width:100px; border: 1px solid black;'>Integer</div>
				<div style='float:left; width:100px; border: 1px solid black;'>integer.csv</div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='templates/integer.xls'>integer.xls</a></div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='logs/integer.log' target=_blank>integer.log</a></div>

				<div style='float:left; width:100px; border: 1px solid black;'>Float</div>
				<div style='float:left; width:100px; border: 1px solid black;'>float.csv</div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='templates/float.xls'>float.xls</a></div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='logs/float.log' target=_blank>float.log</a></div>

				<div style='float:left; width:100px; border: 1px solid black;'>Text</div>
				<div style='float:left; width:100px; border: 1px solid black;'>text.csv</div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='templates/Text.xls'>Text.xls</a></div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='logs/text.log' target=_blank>text.log</a></div>

				<div style='float:left; width:100px; border: 1px solid black;'>Single Choice</div>
				<div style='float:left; width:100px; border: 1px solid black;'>single.csv</div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='templates/single.xls'>single.xls</a></div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='logs/single.log' target=_blank>single.log</a></div>

				<div style='float:left; width:100px; border: 1px solid black;'>Multiple Choice</div>
				<div style='float:left; width:100px; border: 1px solid black;'>multiple.csv</div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='templates/multiple.xls'>multiple.xls</a></div>
				<div style='float:left; width:200px; border: 1px solid black;'><a href='logs/multiple.log' target=_blank>multiple.log</a></div>
			</div>
		</div>
		<div style='float:left; width:630px;' >
			<div style='float:left; width:30px; height:30px; ' ></div>
			<div id=loading style='float:left; width:500; margin: 5px 10px 10px 50px; height:30px; display:none;'>
				Loading ... <img src="images/loading.gif" width=25px height=25px >
			</div>
		</div>
		<div style='float:left; width:630px;' >
		<form name='file_form' method=POST enctype="multipart/form-data"> 
			<div style="width:80px; height:20px; float:left; " ></div>
			<div style="width:230px; height:20px; float:left; text-align=center;" > 
				<INPUT id="fileToUpload" type="file" name="fileToUpload" /> 
			</div> 
			<div style="width:200px; height:20px; float:left; margin-left:10px;" > 
				Validate Only <INPUT id="validate_only" type="checkbox" value=0 checked=checked /> 
			</div> 
			<div class=btn style="width:80px; height:20px; float:left; margin-left:20px;" 
					align=center onClick="return ajaxFileUpload();" >Upload</div> 
		</form> 
		</div>
		<div style='float:left; text-align:left; width:650px; height:15px; font-color:black; margin:30px 20px 2px 25px; '>
			File Process Stats:
		</div>
		<div style='float:left; text-align:left; width:650px; height:300px; font-color:black; margin:3px 20px 5px 20px;' >
			<TEXTAREA id=status_area style="width:650px; height:300px;" readonly=readonly ></TEXTAREA>
		</div>
	</div>
</body>
</html>
