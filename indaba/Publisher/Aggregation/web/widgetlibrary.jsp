<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Widget Library</title>
        <link type="text/css" href="widgets/css/base/jquery.ui.all.css" rel="Stylesheet" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="widgets/js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="widgets/js/jquery.placeholder.js"></script>
        <script type="text/javascript" src="widgets/js/jquery.tmpl.min.js"></script>
        <script type="text/javascript" src="widgets/js/jquery-ui.js"></script>
        <script type="text/javascript" src="js/widgetlibrary.js"></script>
        <script type="text/javascript" src="widgets/ZeroClipboard.js"></script>
        <jsp:include page="widgets/jqTemplate.jsp" />
        <script type="text/javascript">
            // variables: widgetId, widgets, widget
            this.widgetId = ${widgetId};
            this.widgets = [
            <c:forEach items="${widgets}" var="widget" varStatus="status">
                    {
                        id:'${widget.id}',
                        name: '${widget.displayName}',
                        image: '${widget.iconFileName}',
                        target:'widgetlibrary.do?widgetId=${widget.id}',
                        url:'${widget.targetUrl}',
                        params:'${widget.params}',
                        configType:'${widget.configType}',
                        description:'${widget.description}'
                    }<c:if test="${!(status.last)}">,</c:if>
            </c:forEach>
                ];

                var self = this;
                $.each(this.widgets, function(){
                    if (this.id == self.widgetId){
                        self.widget = this;
                    }
                });

                var horseIds = [], targetIds = [], targetNames = [], includeLogos = [];

                // steps
                var steps;
                if (self.widget){
                    steps = this.widget.configType!=2
                        ? ['Choose Product', 'Select Target(s)', 'Misc', 'Display Widget URL']
                    : ['Choose Product', 'Misc', 'Display Widget URL'];
                }

                $(document).ready(function(){
                    // steps bar
                    $('#stepTemplate').tmpl({steps:steps}).appendTo('#steps-bar');
                    if (self.widget && self.widget.configType==2)
                        $('#steps-bar>li').css('width', '227px');
                
                    // populate product table
                    var products = [];
            <c:forEach items="${products}" var="prd">
                    products.push({id:${prd.productId}, project:'${prd.projectName}', product:'${prd.productName}'});
            </c:forEach>

                    if (products.length == 0){
                        $('#steps-bar, #steps-content').hide();
                        $('#no-data-msg').show();
                    }
          
                    $('#productTemplate').tmpl(products).appendTo('#tbl-product>tbody');
                    $('#tbl-product').tablesorter({headers:{0:{sorter:false}}});

                    // next step button
                    $('#btn-next').click(function(){
                        var copyToClipboard = false;
                        if (validateStep()){
                            var idx = ++self.currentStep;
                            if (idx > 0 && self.widget.configType == 2)
                                idx++;
                            switch(idx){
                                case 1:
                                    var url = 'targets4prd.do?productId=' + $('input[name="product"]:checked').val();
                                    $.getJSON(url, function(json){
                                        $('#tbl-target>tbody').empty();
                                        if (self.widget.configType == 3)
                                            $('#targetTemplate2').tmpl(json.tlist).appendTo('#tbl-target>tbody');
                                        else
                                            $('#targetTemplate1').tmpl(json.tlist).appendTo('#tbl-target>tbody');
                                        $('#tbl-target').tablesorter();
                                    });
                                    break;
                                case 2:
                                    if (self.widget.configType != 2){
                                        horseIds=[], targetIds = [], targetNames = [], includeLogos = [];
                                        $.each($('input[name="target"]:checked'), function(){
                                            var tr = $(this).closest('tr');
                                            targetIds.push($(this).val());
                                            horseIds.push($(this).siblings('input[name="horse-'+ $(this).val() + '"]').val());
                                            targetNames.push($('td:eq(1)', tr).text());
                                            includeLogos.push($('input[name="includeLogo"]', tr).attr('checked') ? 1 : 0);
                                        });
                                    }
                                    break;
                                case 3:
                                    var productId = $('input[name="product"]:checked').val();
                                    var urls = [];
                                    var url = self.widget.url;
                                    url = location.href.split('/').slice(0,-1).concat(url).join('/');
                                    // parameters
                                    var params = [];
                                    params.push('helper='+encodeURIComponent($('#param_helper').val()));
                                    params.push('frameId='+$('#param_frameId').val());
                                    // imported css
                                    if ($('#imported-css > li').length > 0){
                                        var files = [];
                                        $.each($('#imported-css > li'), function(){
                                            files.push($(this).text());
                                        })
                                        params.push('csslink='+encodeURIComponent(files.join(',')));
                                    }
                                    
                                    if (self.widget.configType == 1){
                                        for (var i=0; i<targetIds.length; i++){
                                            var str = url + (url.indexOf('?')==-1 ? '?' : '&') + params.join('&');
                                            str += '&horseId=' + horseIds[i];
                                            str += (includeLogos[i] ? '&includeLogo=1' : '');
                                            urls.push({target:targetNames[i], url:str, configType:1});
                                        }
                                    }
                                    else if (self.widget.configType == 2){
                                        var str = url + '?' + params.join('&');
                                        str += '&productId=' + productId;
                                        if ($('#param_includeLogo').attr('checked'))
                                            str += '&includeLogo=1';
                                        urls.push({target:'', url:str, configType:2});
                                    }
                                    else if (self.widget.configType == 3){
                                        var str = url + '?' + params.join('&');
                                        str += '&productId=' + productId;
                                        str += '&horseId=' + horseIds.join('+');
                                        if ($('#param_includeLogo').attr('checked'))
                                            str += '&includeLogo=1';
                                        var targetStr = targetNames.join(', ');
                                        if (targetNames.length > 4)
                                            targetStr = targetNames.slice(0, 2).join(', ') + ' and ' + (targetNames.length-2) +' other targets'
                                        urls.push({target:targetStr, url:str, configType:3});
                                    }
                                    
                                    $('#tbl-url>tbody').empty();
                                    $('#urlTemplate').tmpl(urls).appendTo('#tbl-url>tbody');
                                    $('#tbl-url').tablesorter();
                                    copyToClipboard = true;
                                    break;
                            }
                            adjustSteps();
                            if (copyToClipboard){
                                // copy to clipboard
                                $.each($('.btn-copy-url'), function(idx, elm){
                                   var obj = $(elm);
                                   obj.parent().attr('id', 'td-copy-'+idx)
                                   var url = $('td:eq(1)', obj.closest('tr')).text();
                                   var clip = new ZeroClipboard.Client();
                                    clip.setText(url);
                                    clip.glue(elm, 'td-copy-'+idx);
                                });
                            }
                        }
                    });
                });
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="widgetlibrary" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <link rel="stylesheet" href="css/widgetlibrary.css" />
            <div class="interactive">
                <div class="wrapper">
                    <c:choose>
                        <c:when test="${(widgetId > 0)}">
                            <div id="widget">
                                <script>document.write('<h2>Widget: '+this.widget.name+'</h2>');</script>
                                <ul id="steps-bar">

                                </ul>
                                <div id="steps-content">
                                    <div class="step-content">
                                        <table id="tbl-product" class="step-table">
                                            <thead>
                                                <tr>
                                                    <th width="50"></th>
                                                    <th>PROJECT</th>
                                                    <th>PRODUCT</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="step-content">
                                        <table class="step-table">
                                            <thead>
                                                <tr>
                                                    <th width="75" align="center">
                                                        SELECT <input type="checkbox"/>
                                                    </th>
                                                    <th class="sortable-th">TARGET</th>
                                                    <th id="th-include-logo" width="120">INCLUDE LOGO <input type="checkbox"/></th>
                                                </tr>
                                            </thead>
                                        </table>
                                        <div class="step-table">
                                            <table id="tbl-target" class="step-table">
                                                <thead style="display:none">
                                                    <tr>
                                                        <th width="85" align="center">
                                                        </th>
                                                        <th>TARGET</th>
                                                        <th width="120">INCLUDE LOGO <input type="checkbox"/></th>
                                                    </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="step-content">
                                        <table cellpadding="4">
                                            <tr>
                                                <td width="80" align="right">helper:</td>
                                                <td><input type="text" id="param_helper" class="text-input" placeholder="http://www.yoursite.com/to/path/indaba_widget_helper.html"/></td>
                                            </tr>
                                            <tr>
                                                <td align="right">frameId:</td>
                                                <td><input type="text" id="param_frameId" class="text-input" placeholder="frame id"/></td>
                                            </tr>
                                            <tr id="tr-include-logo">
                                                <td align="right">Include Logo:</td>
                                                <td><input type="checkbox" id="param_includeLogo"/></td>
                                            </tr>
                                            <tr>
                                                <td align="right"></td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td width="80" align="right">CSS Path:</td>
                                                <td><input type="text" id="css_path" class="text-input" placeholder="http://www.yoursite.com/to/path/your_css_file.css"/></td>
                                            </tr>
                                            <tr>
                                                <td align="right"></td>
                                                <td><button class="medium button blue" id="import-css-btn">Import</button></td>
                                            </tr>
                                            <tr>
                                                <td align="right" valign="top">Imported CSS:</td>
                                                <td>
                                                    <em class="param-tip">
                                                        If you're importing multiple css files, click on and drag a block to re-order css importing sequence.
                                                    </em>
                                                    <ul id="imported-css">
                                                    </ul>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="step-content">
                                        <table class="step-table">
                                            <thead>
                                                <tr>
                                                    <th width="60" class="sortable-th">TARGET</th>
                                                    <th class="sortable-th">WIDGET URL</th>
                                                    <th width="70">Preview</th>
                                                    <th width="90">Copy URL</th>
                                                </tr>
                                            </thead>
                                        </table>
                                        <div class="step-table">
                                            <table id="tbl-url" class="step-table">
                                                <thead style="display:none;">
                                                    <tr>
                                                        <th width="60">TARGET</th>
                                                        <th>WIDGET URL</th>
                                                        <th width="70">Preview</th>
                                                        <th width="90">Copy URL</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <button class="medium button blue" id="btn-previous">Previous</button>
                                    <button class="medium button blue" id="btn-next">Next</button>
                                    <button class="medium button blue" id="btn-done">Done</button>
                                </div>
                                <div id="no-data-msg">
                                    Sorry, there is no widget data available for getting widget.
                                </div>
                            </div>
                            <script type="text/javascript">
                                if (this.widget.configType==2)
                                    $('.step-content:eq(1)').remove();
                            </script>
                            <div id="sidebar">
                                <div id="tabs">
                                    <div id="tabs-1">
                                        <h2>Gallery</h2>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div id="doc-gallary">
                                <h2>Indaba Widget Gallery</h2>
                                <div>
                                Select the widget you would like to download. This will start a step-by-step process through which 
                                you will customize your widget for installation into your website.<br></br>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#uniform-btn-previous').add('#uniform-btn-done').hide();
                $('#uniform-btn-done').click(function(){
                    location.href = './widgetlibrary.do';
                });
            });
        </script>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>