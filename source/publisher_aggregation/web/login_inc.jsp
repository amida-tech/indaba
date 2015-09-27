<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
<link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
<link rel="stylesheet" type="text/css" href="js/chosen/chosen.css"/>
<script type="text/javascript" charset="utf-8" src="js/chosen/jquery.chosen.js"></script>
<style type="text/css">
    #loginForm table tr {
        font-size: 13px;
    }

    #loginForm A:link {font-family: Helvetica, arial, sans-serif;font-size:13px;color:#FF6600;text-decoration: none}
    #loginForm A:visited {font-family: Helvetica, arial, sans-serif; color:#FF6600;text-decoration: none}
    #loginForm A:hover {font-family: Helvetica, arial, sans-serif;color:#FF6600;text-decoration: underline}

    #loginForm .input {
        background-color: #F1FCFB;
        border: 1px solid gray;
        color: #000000;
        font-size: 12px;
        font-family: Helvetica, arial, sans-serif;
        height: 17px;
        width: 210px;
    }
    #loginForm .username{
        background-position: 1px 1px;
        background-repeat:no-repeat;
    }
    #loginForm .username:hover {
        background-color: #FFFFB0;
        border: 1px solid orange;
    }
    #loginForm .password {
        background-position: 1px 1px;
        background-repeat:no-repeat;
    }
    #loginForm .password:hover {
        background-color: #FFFFB0;
        border: 1px solid orange;
    }
    #loginForm .errtxt {
        color: #ff8040;
        padding-right: 2px;
        text-align: left;
        font-size: 12px;
        font-family: Helvetica, arial, sans-serif;
    }
</style>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('input, textarea, button').uniform();
        $('select').chosen();
    });
</script>
<script type="text/javascript">
    function highlight(element) {
        element.style.border="1px solid orange";
        element.style.backgroundColor="#FFFFB0";
        element.focus();
    }

    function unhighlight(element) {
        element.style.border="1px solid gray";
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
            errElmt.innerHTML = "Checking the validity of username now.";
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
                if(result==("Validity username")){
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

    function updateHeader(username, managewidgets_url, exportscorecardreport_url, manageworksets_url, createworkset_url) {
        document.getElementById('userlogin').innerHTML =
                    "Welcome <span>" + username + "</span>!&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                    "<a id='logout' href='logout.do'>Log out</a>";
        
        if (managewidgets_url) {
            document.getElementById('managewidgets').style.display = 'block';
            document.getElementById('managewidgetslink').href = managewidgets_url;
        }

        if (exportscorecardreport_url) {
            document.getElementById('scorecardexport').style.display = 'block';
            document.getElementById('scorecardexportlink').href = exportscorecardreport_url;
        }

        if (manageworksets_url) {
            document.getElementById('manageworksets').style.display = 'block';
            document.getElementById('manageworksetlink').href = manageworksets_url;
            document.getElementById('createworksetlink').href = createworkset_url;
        }
    }


    function login() {
        if(!validate()) {
            return false;
        }
        $.post("login.do",
        {
            username: $("#username").val(), 
            password: $("#password").val(), 
            cururl: $("#cururl").val(), 
            asyncmode: $("#asyncmode").val()
        },
        function(data) {
            if (data) {
                var part = data.split(":");
                var cmd = part[0];
                if (cmd == "RELOAD") {
                    var url = $.url.decode($("#cururl").val());
                    if(/^\//.test(url)) {
                        url = url.substr(1);
                    }
                    if(/logout/.test(url)) {
                        url = "index.jsp"
                    }
                    self.location = url;
                } else {
                    parent.$.fancybox.close();
                    
                    var username = part[1];
                    var managewidgets_url = part[2];
                    var exportscorecardreport_url = part[3];
                    var manageworksets_url = part[4];
                    var createworkset_url = part[5];

                    updateHeader(username, managewidgets_url, exportscorecardreport_url, manageworksets_url, createworkset_url);
                }
            } else {
                $("#errmsg").text("Invalid username/password!");
                $("#errmsg").show();
            }
        });
        return false;
    }
</script>
<form id="loginForm" name="loginForm" action="login.do" method="POST" onsubmit="return login();">
    <table width="380" cellpadding="0" cellspacing="0" border="0" align="center">
        <tr>
            <td>
                <a href="/indaba" style="margin: 0px; padding: 0px;" ><img src="images/IndabaPublisher-2.gif" alt="Indaba Logo" border="0"/></a>
            </td>
        </tr>
        <tr>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
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
                                                    <c:when test="${errmsg != null}">
                                                        <c:set var="show" value="inline"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="show" value="none"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <span id="errmsg" class="errtxt">${errmsg}</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 100px; font-weight: bold; padding-left: 0px; text-align: right;">User Name&nbsp;</td>
                                        <td><input id="username" name="username" type="text" maxlength="40" size="40"
                                                   onfocus="highlight(this); unhighlight(document.getElementById('password')); this.focus();"
                                                   onmouseover="highlight(this); unhighlight(document.getElementById('password')); this.focus();"
                                                   onblur="unhighlight(this); this.blur();"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width: 100px; font-weight: bold; padding-left: 0px; text-align: right;">Password&nbsp;</td>
                                        <td><input id="password" name="password" type="password" maxlength="40"  size="40" value=""
                                                   onfocus="highlight(this); unhighlight(document.getElementById('username')); this.focus();"
                                                   onmouseover="highlight(this); unhighlight(document.getElementById('username')); this.focus();"
                                                   onblur="unhighlight(this); this.blur();"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="hidden" id="cururl" name="cururl" value="${returl}" />
                                            <input type="hidden" id="asyncmode" name="asyncmode" value="true" />
                                        </td>
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
