<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <style type="text/css">
            table tr {
                font-size: 13px;
            }

            A:link {font-family: Helvetica, arial, sans-serif;font-size:13px;color:#FF6600;text-decoration: none}
            A:visited {font-family: Helvetica, arial, sans-serif; color:#FF6600;text-decoration: none}
            A:hover {font-family: Helvetica, arial, sans-serif;color:#FF6600;text-decoration: underline}

            .input {
                background-color: #F1FCFB;
                border: 1px solid gray;
                color: #000000;
                font-size: 12px;
                font-family: Helvetica, arial, sans-serif;
                height: 25px;
                line-height: 25px;
                padding: 0px 2px;
                width: 210px;
            }
            .username{
                background-position: 1px 1px;
                background-repeat:no-repeat;
            }
            .username:hover {
                background-color: #FFFFB0;
                border: 1px solid orange;
            }
            .password {
                background-position: 1px 1px;
                background-repeat:no-repeat;
            }
            .password:hover {
                background-color: #FFFFB0;
                border: 1px solid orange;
            }
            .errtxt {
                color: #ff8040;
                padding-right: 2px;
                text-align: left;
                font-size: 12px;
                font-family: Helvetica, arial, sans-serif;
            }
        </style>

        <script type="text/javascript" src="js/jquery-1.7.1.js">
        </script>

        <script type="text/javascript">
            function highlight(element) {
                element.style.border="1px solid orange";
                element.style.backgroundColor="#FFFFB0";
                element.focus();
            }

            function unhighlight(element) {
                element.style.border="1px solid #ccc";
                element.style.backgroundColor="#FFFFFF";
                element.blur();
            }

            function validate() {
                var username = document.loginForm.username.value;
                var password = document.loginForm.password.value;
                var usernameElement = document.getElementById("username");
                var passwordElement = document.getElementById("password");
                var errElmt = document.getElementById("errmsg");
                if (username + '.' == ".") {
                    errElmt.innerHTML = "User name can't be empty!";
                    highlight(usernameElement);
                    unhighlight(passwordElement);
                } else if (password + '.' == ".") {
                    errElmt.innerHTML = "Password can't be empty!";
                    highlight(passwordElement);
                    unhighlight(usernameElement);
                } else {
                    return true;
                }
                return false;
            }

            function forgetPassWord(){
                var username = document.loginForm.username.value;
                var errElmt = document.getElementById("errmsg");
                var usernameElement = document.getElementById("username");
                var passwordElement = document.getElementById("password");
                if ((username + '.' == ".")) {                 
                    errElmt.innerHTML = "Please input user name";
                    highlight(usernameElement);
                    unhighlight(passwordElement);
                    document.getElementById("warnimg").style.display="inline";
                    return false;
                } else{
                    //errElmt.innerHTML = "Checking the validity of username now.";
                    checkValidity();
                }
            }

            function checkValidity() {
                var username = document.loginForm.username.value;
                var errElmt = document.getElementById("errmsg");
                $.ajax({
                    type: "POST",
                    url: "forgetpwd.do",
                    data: "username=" + username+"&action=1",
                    success: function(result) {
                        errElmt.innerHTML = result;
                        if(result==("validusername")){
                            errElmt.innerHTML = "Sending password to your email...";
                            postPassword();
                        } else {
                            highlight(document.getElementById("username"));
                        }
                    },
                    error: function(result) {
                        errElmt.innerHTML ="ERROR when check validity!";
                    }
                });
            }

            function postPassword() {
                var username = document.loginForm.username.value;
                var errElmt = document.getElementById("errmsg");
                $.ajax({
                    type: "POST",
                    url: "forgetpwd.do",
                    data: "username=" + username+"&action=2",
                    success: function(result) {
                        errElmt.innerHTML = result;
                    },
                    error: function(result) {
                        errElmt.innerHTML ="ERROR when send email!";
                    }
                });
            }
        </script>
        <title>Indaba Login</title>
    </head>
    <body style="font-family: Helvetica, arial, sans-serif; background-color: #ffffff;">
        <br/><br/><br/><br/><br/><br/>
        <form name="loginForm" action="login.do" method="POST" onsubmit="return validate();">
        <table width="380" cellpadding="0" cellspacing="0" border="0" align="center">
          <tr>
              <td align="center"><a href="/indaba"><img src="images/indaba-logo.gif" alt="Indaba Logo" border="0" /></a></td>
          </tr>
          <tr>
            <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin-top: 10px;">
                <tr>
                    <td align="left" width="18"><img src="images/box_gray_topl.gif" width="18" height="15" alt="" border="0"></td>
                    <td background="images/box_gray_top.gif"><img src="images/box_gray_top.gif" height="15" width="378" alt="" border="0"></td>
                    <td><img src="images/box_gray_topr.gif" width="19" height="15" alt="" border="0"></td>
                </tr>
                <tr>
                    <td background="images/box_gray_l.gif"></td>
                    <td>
                      <div style="margin:0 auto 20px; width: 380px;">
                        <table width="380px" align="center">
                            <tr>
                                <td></td>
                                <td style="height: 25px;">
                                    <div style="text-align: left;">
                                        <c:choose>
                                            <c:when test="${!(empty errMsg)}">
                                                <c:set var="show" value="inline"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="show" value="none"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <span id="errmsg" class="errtxt">${errMsg}</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 100px; font-weight: bold; padding-left: 0px; text-align: right;">User Name&nbsp;</td>
                                <td><input id="username" name="username" type="text" maxlength="30" class="input username"
                                           onfocus="highlight(this); unhighlight(document.getElementById('password')); this.focus();"
                                           onmouseover="highlight(this); unhighlight(document.getElementById('password')); this.focus();"
                                           onblur="unhighlight(this); this.blur();"/></td>
                            </tr>
                            <tr>
                                <td style="width: 100px; font-weight: bold; padding-left: 0px; text-align: right;">Password&nbsp;</td>
                                <td><input id="password" name="password" type="password" maxlength="30" value="" class="input password"
                                           onfocus="highlight(this); unhighlight(document.getElementById('username')); this.focus();"
                                           onmouseover="highlight(this); unhighlight(document.getElementById('username')); this.focus();"
                                           onblur="unhighlight(this); this.blur();"/></td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    &nbsp;<br/>
                                    <input border="0" alt="Sign In" type="image" name="1.Continue" src="images/btn_signin.png" />
                                    &nbsp;
                                    <a href="#" onclick="forgetPassWord();"><img border="0" src="images/btn_forgotpassword.png" alt="Forgot Password" /></a>
                                </td>
                            </tr>
                        </table>
                      </div>
                    </td>
                    <td background="images/box_gray_r.gif"><img src="images/box_gray_r.gif" alt="" border="0"></td>
                </tr>
                <tr>
                    <td align="left"><img src="images/box_gray_botl.gif" width="18" height="15" alt="" border="0"></td>
                    <td background="images/box_gray_bot.gif"><img src="images/box_gray_bot.gif" height="15" width="378" alt="" border="0"></td>
                    <td align="left"><img src="images/box_gray_botr.gif" width="19" height="15" alt="" border="0"></td>
                </tr>
              </table>
            </td>
            </tr>
        </table>
        </form>
    </body>
</html>
