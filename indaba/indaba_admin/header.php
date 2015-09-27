<?php
session_start();
require_once('./include/config.inc');
if ( ! isset( $_SESSION['authuser']) ) {  //|| !defined($_SESSION['authuser'] ) ) {
	if ( isset($_SERVER['REQUEST_URI']) ) {
		$_SESSION['ref'] = $_SERVER['REQUEST_URI'];
	}
    header("Location: login.php");
}
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>

<link rel="stylesheet" href="css/menu.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="css/style.css"/>

<body><div id="indaba">

<div id="mainheader" class="noprint">
    <a href="/" title="Indaba Home"><img src="images/header-logo.png" id="mainlogo" alt="Indaba Home"/></a>
    <ul id="project">
	<li align=left> 
	    Welcome <? echo isset($_SESSION['authuser']) ? $_SESSION['authuser'] : ''; ?>
	</li>
        <li>
            <ul id="nav">
                <li class="tab">
                    <a class="tab" href="meta.php">Metadata</a>
                </li>
                <li class="tab">
                    <a class="tab" href="users.php">Users</a>
                </li>
                <li class="tab">
                    <a class="tab" href="matrix.php">Rights</a>
                </li>
                <li class="tab">
                    <a class="tab" href="workflows.php">Workflow</a>
                </li>
                <li class="tab">
                    <a class="tab" href="projects.php">Projects</a>
                </li>
                <li class="tab">
                    <a class="tab" href="products.php">products</a>
                </li>
            </ul>
			<ul id="nav">
                <li class="tab">
                    <a class="tab" href="indicators.php">indicators</a>
                </li>
                <li class="tab">
                    <a class="tab" href="survey_config.php">Survey config</a>
                </li>
			</ul>
        </li>
    </ul>
    <ul id="membership">
        <li id="membership-indaba-menu">
            <a href="/indaba/">Indaba Menu</a>
        </li>
        <li id="membership-logout">
            <a href="logout.php">Logout</a>
        </li>
        <li id="privacy-policy">
            <a href="http://getindaba.org/indaba-privacy-policy/">Privacy Policy</a>
        </li>
    </ul>
</div>
