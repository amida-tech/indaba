<?php
require_once('include/config.inc');
$sql = "SELECT * FROM token";
$st = mysql_query($sql);
$table = "<table border=1 width=400px height=200px >";
$table .= "  <th>Name</th><th>Description</th>";
while ( $token = mysql_fetch_assoc($st) ) {
	$table .= "<tr>";
	$name = preg_replace('/</', '&lt;', $token['name']);
	$name = preg_replace('/>/', '&gt;', $name);
	$table .= "  <td>" . $name . "</td>";
	$table .= "  <td>" . $token['description'] . "</td>";
	$table .= "</tr>";
}
$table .= "</table>";
echo $table;
exit;
