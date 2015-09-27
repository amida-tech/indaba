<?php

session_start();
unset($_SESSION['authuser']);
header("Location: login.php");

?>
