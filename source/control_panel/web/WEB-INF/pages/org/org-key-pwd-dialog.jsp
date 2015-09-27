<div id="key-pwd-popup" class="hidden">
    <div id style="clear: both; overflow: auto; background-color: #fff; padding: 20px 20px 20px 20px;margin: 10px;border: 3px #888 solid;">
        <form id="org-key-pwd-form" onsubmit="return verify_user();">
            <div id="hint" class="row hidden" style="color: red">
                <span>Incorrect Password!</span><br>
                <span id="hint-text"></span>
            </div>
            <div class="row">
                <label style="width:120px">Password</label>
                <input id="password" name="password" type="password" maxlength="30" value="" style="width:150px;"/>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function(){
        $('#key-pwd-popup').dialog({
            autoOpen: false,
            width: 375,
            resizable: false,
            modal: true,
            open: function(){
                reset_pwd_form();
            },
            close: function(){
                reset_pwd_form();
            },
            buttons: {
                "OK": function() {
                    var pwdDlg = this;
                    $.ajax({
                        url: '${contextPath}/organizations!verifyUser',
                        type: 'POST',
                        dataType: 'json',
                        data:{password: $('#password').val()},
                        success: function(r){
                            if(r['success'] == true) {
                                $(pwdDlg).dialog("close");
                                $('#key-grid').flexReload({dataType:'json'});
                            }
                            else {
                                var msg = 'Please enter password for user ' + r['userName'];
                                $('#hint-text').text(msg);
                                $('#hint').removeClass('hidden');
                            }
                        }
                    });
                },
                "Cancel": function(){
                    $(this).dialog("close");
                    $('#org-detail-tabs').tabs('select', 0); 
                }
            }
        });
    });
    
    function reset_pwd_form() {
        $('#password').val('');
        $('#hint').addClass('hidden');
    }
    
    function verify_user() {
        $.ajax({
                    url: '${contextPath}/organizations!verifyUser',
                    type: 'POST',
                    dataType: 'json',
                    data:{password: $('#password').val()},
                    success: function(r){
                        if(r['success'] == true) {
                            $('#key-pwd-popup').dialog("close");
                            $('#key-grid').flexReload({dataType:'json'});
                        }
                        else {
                            var msg = 'Please enter password for user ' + r['userName'];
                            $('#hint-text').text(msg);
                            $('#hint').removeClass('hidden');
                        }
                    }
                });
        return false;
    }
</script>