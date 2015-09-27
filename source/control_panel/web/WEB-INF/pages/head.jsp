<div id="head">
    <div class="logo"><img alt="Logo" src="${contextPath}/resources/images/indaba-logo.gif" style="vertical-align: middle"/>
        <span style="position:relative;top:4px; font-size: 14px;font-weight: bold; color: #666"> |&nbsp;&nbsp;</span><span class="title">CONTROL PANEL</span>
    </div>
    <div class="login">Welcome <span class="username">${user.firstname} ${user.lastname}</span>! | 
        <a href="/indaba" target="_blank">Builder</a> | 
        <a href="/ids" target="_blank">Publisher</a> |
        <a href="${contextPath}/login!logout">Log out</a>
    </div>
</div>
<div id="nav_tabs"></div>
<script type="text/javascript" charset="utf-8">
    $(function() {
        tabbar = new dhtmlXTabBar("nav_tabs", "top", "30");
    
        tabbar.setSkin('modern');
        tabbar.enableAutoSize(true, true);
    
        tabbar.setImagePath("${contextPath}/resources/plugins/dhtmlx/imgs/");

        tabbar.addTab("projects", $.i18n.message('cp.tab1.projects'), "100%");
        tabbar.addTab("organizations", $.i18n.message('cp.tab1.organizations'), "140px");
        tabbar.addTab("indicatorLib", $.i18n.message('cp.tab1.indicatorlibs'), "170px");
        tabbar.addTab("targetLib", $.i18n.message('cp.tab1.targetlibs'), "170px");
        tabbar.addTab("surveyConfig", "SURVEY CONFIGURATION", "180px");
        tabbar.addTab("notifications", $.i18n.message('cp.tab1.notifications'), "160px");
        
        var style = "font-size: 12px; font-family: Arial; top: 4px;";
        tabbar.setCustomStyle("projects", null, null, style);
        tabbar.setCustomStyle("organizations", null, null, style);
        tabbar.setCustomStyle("indicatorLib", null, null, style);
        tabbar.setCustomStyle("targetLib", null, null, style);
        tabbar.setCustomStyle("surveyConfig", null, null, style);
        tabbar.setCustomStyle("notifications", null, null, style);

        tabbar.setContentHTML("${param.active}", $('#${param.content}').html());
        $('#${param.content}').remove();
        tabbar.setTabActive("${param.active}");

        tabbar.attachEvent("onSelect", function() {
            var target = null;
            if("projects" == arguments[0]) {
                target = "${contextPath}/proj/projects";
            } else if("organizations" == arguments[0]) {
                target = "${contextPath}/organizations";
            } else if("indicatorLib" == arguments[0]) {
                target = "${contextPath}/lib/indicator-lib";
            }  else if("targetLib" == arguments[0]) {
                target = "${contextPath}/lib/target-lib";
            } else if("surveyConfig" == arguments[0]) {
                target = "${contextPath}/surveyconf/survey-config";
            } else if ("notifications" == arguments[0]) {
                target = "${contextPath}/notif/notifications";
            }
            window.location.href=target; 
            return false;
        });

        $.ajaxSetup({error: defaultAjaxErrorHanlde, global: true,
            beforeSend: function(){
                if(this._loading) {
                    var loadingText = (this._loading.text)?this._loading.text:'Waiting...';
                    var loading=new ol.loading({id:this._loading.id, loadingText:loadingText});
                    loading.show();
                    this._loading = loading;
                }
            },
            complete: function(){
                if(this._loading) {
                    this._loading.hide();
                }
            }
        });
    
    });
    
</script>
