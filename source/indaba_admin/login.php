<?php

session_start();
$PHP_SELF = "./login.php";
if ( ! isset($_POST['submit'])  ) {
?>
<!-- start login screen -->
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" href="css/basic.css" type="text/css" />
        <script type="text/javascript">
            function validate() {
                var username = document.loginForm.username.value;
                var password = document.loginForm.password.value;
                if ((username + '.' == ".") || (password + '.' == ".")) {
                    var errElmt = document.getElementById("errmsg");
                    errElmt.innerHTML = "username or password canot be null. Please Retry again!";
                    return false;
                } else {
					document.loginForm.authreq.value = "true";
                    return true;
                }
            }
        </script>
        <title>Indaba Login Page</title>
    </head>
    <body>
        <div id="login">
            <form name="loginForm" action="./login.php" method="POST"
                  onsubmit="return validate();">
                <h2 id="login_title" style="margin-top: 10px;">Sign in to Indaba Admin interface</h2>
                <p class="errtxt" id="errmsg"></p>
                <p>Username: <input id="username" name="username" type="text"
                                    class="input" /></p>
                <p>Password: <input id="password" name="password" type="password"
                                    value="" class="input" /></p>
                <p><input id="submit" name="submit" type="submit" value="Log In" class="btn" />
                <p><input id="authreq" name="authreq" type="hidden" value="false" />
                </p>
            </form>
        </div>
    </body>
</html>
<!-- end of login screen -->
<? 
} else {
    // trying to authenticate user
    require_once("./include/config.inc");
    $query = sprintf ( "SELECT * FROM user WHERE username = '%s' and password = %s and status = 1 and site_admin = 1 ", 
		mysql_real_escape_string($_POST['username']), 
		SQLString($_POST['password'], "text") );
    $rt = @mysql_query($query, $indaba_dbh);
    if ( mysql_num_rows($rt) == 1 ) {
		$user = mysql_fetch_assoc($rt);
		$_SESSION['authuser'] = $_POST['username'];
		$_SESSION['user_id'] = $user['id'];
		if ( isset($_SESSION['ref']) ) {
			header('Location: ' . $_SESSION['ref']);
		} else {
			header('Location: index.php');
		}
    } else {
		header('Location: login.php');
    }
}
?>
