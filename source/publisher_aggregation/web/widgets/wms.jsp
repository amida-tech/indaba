<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" language="java" %>
<%@page import="java.sql.*" %>
<%
    ArrayList widgets = new ArrayList();
    widgets.add("Journal Display");
    widgets.add("Vertical Scorecard");
    widgets.add("Horizontal Scorecard");
    widgets.add("Data Summary");
    widgets.add("Indicator Summary");
    widgets.add("Sparkline");

    String widgetId = request.getParameter("widgetId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Indaba - Widget Management System</title>
        <link type="text/css" href="css/base/jquery.ui.all.css" rel="Stylesheet" />
        <link rel="stylesheet" href="css/wms.css" />
        <script type="text/javascript" src="ZeroClipboard.js"></script>
        <script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/jquery.placeholder.js"></script>
        <script type="text/javascript" src="js/jquery.tmpl.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui.js"></script>
        <script type="text/javascript" src="js/wms.js"></script>
        <jsp:include page="jqTemplate.jsp"></jsp:include>
        <script type="text/javascript">
            this.widgetId = <%=widgetId%>;
            $(document).ready(function(){
                // populate product table
                var products = [];
                <%
                String connectionURL = "jdbc:mysql://127.0.0.1:3306/indaba";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(connectionURL, "indaba_user", "indaba_pwd");
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery ("select product.id, project.code_name project, product.name product  from product inner join project on product.project_id=project.id");
                while (res.next()){
                %>
                products.push({id:<%=res.getString(1)%>, project:'<%=res.getString(2)%>', product:'<%=res.getString(3)%>'});
                <%
                }
                stmt.close();
                conn.close();
                %>
                $('#productTemplate').tmpl(products).appendTo('#tbl-product>tbody');
                $('#tbl-product').tablesorter({headers:{0:{sorter:false}}});

                // TODO: above logic can be replaced by ajax call
                /*
                $.getJSON(serviceUrl, function(data){
                    var array = data.d.results;
                    $('#productTemplate').tmpl(products).appendTo('#tbl-product>tbody');
                    $('#tbl-product').tablesorter({headers:{0:{sorter:false}}});
                })
                */

                // next step button
                $('#btn-next').click(function(){
                    if (validateStep()){
                        window.currentStep++;
                        switch(window.currentStep){
                            case 1:
                                // TODO: use selected product id
                                var productId = $('input[name="product"]:checked').val();

                                var targets = []
                                <%
                                conn = DriverManager.getConnection(connectionURL, "indaba_user", "indaba_pwd");
                                stmt = conn.createStatement();
                                res = stmt.executeQuery ("select id, name from target");
                                while (res.next()){
                                %>
                                targets.push({id:<%=res.getString(1)%>, name:'<%=res.getString(2)%>'});
                                <%
                                }
                                stmt.close();
                                conn.close();
                                %>
                                $('#tbl-target>tbody').empty();
                                $('#targetTemplate').tmpl(targets).appendTo('#tbl-target>tbody');
                                $('#tbl-target').tablesorter();
                                break;
                            case 2:
                                // TODO: use selected target ids
                                var targetIds = [];
                                $.each($('input[name="target"]:checked'), function(){
                                   targetIds.push($(this).val());
                                });
                                targetIds = targetIds.join(',');
                                //alert(targetIds);

                                break;
                            case 3:
                                var urls = [];
                                $.each($('input[name="target"]:checked'), function(){
                                    var tr = $(this).closest('tr');
                                    var target = $('td:eq(1)', tr).text();
                                    var url = window.widgets[window.widgetId-1].url;
                                    url = location.href.split('/').slice(0, -2).concat(url).join('/');

                                    // parameters
                                    var params = [];
                                    if ($('input[name="includeLogo"]', tr).attr('checked'))
                                        params.push('includeLogo=1');
                                    if ($('input[name="includeReview"]', tr).attr('checked'))
                                        params.push('includeReview=1');
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
                                    url += '?'+params.join('&');
                                    urls.push({target:target, url:url});
                                    // TODO: use productId, horseId, etc.
                                });
                                $('#tbl-url>tbody').empty();
                                $('#urlTemplate').tmpl(urls).appendTo('#tbl-url>tbody');
                                $('#tbl-url').tablesorter();
                                break;
                        }
                        adjustSteps();
                    }
                });
            });
        </script>
    </head>
    <body>
        <div id="doc">
            <a href="wms.jsp"><h1 id="logo">Indaba - Widget Management System</h1></a>
            <div id="doc-content">
                <%
                    if (widgetId != null && widgetId.trim().length() > 0){
                %>
                <div id="widget">
                    <h2>Widget: <% out.print(widgets.get(Integer.parseInt(widgetId)-1)); %></h2>
                    <hr/>
                    <ul id="steps-bar">
                        <li class="current">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Choose Product</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select Target(s)</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Misc</dd>
                            </dl>
                        </li>
                        <li class="last">
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Display Widget URL</dd>
                            </dl>
                        </li>
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
                                        <th width="120">INCLUDE LOGO <input type="checkbox"/></th>
                                        <th width="135">INCLUDE REVIEW <input type="checkbox"/></th>
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
                                            <th width="135">INCLUDE REVIEW <input type="checkbox"/></th>
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
                                    </tr>
                                </thead>
                            </table>
                            <div class="step-table">
                                <table id="tbl-url" class="step-table">
                                    <thead style="display:none;">
                                        <tr>
                                            <th width="60">TARGET</th>
                                            <th>WIDGET URL</th>
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
                </div>
                <div id="sidebar">
                    <a id="how-to" href="howto.jsp">
                        How to include Indaba Widget?
                    </a>
                    <div id="tabs">
                        <ul>
                            <li><a href="#tabs-1">Gallery</a></li>
                            <li><a href="#tabs-2">Metadata</a></li>
                        </ul>
                        <div id="tabs-1">
                        </div>
                        <div id="tabs-2">
                            <table id="meta-table" cellpadding="0">
                                <tr>
                                    <td width="60" align="right">Tech Name:</td>
                                    <td><input type="text" id="meta_tech_name" class="text-input-tab"/></td>
                                </tr>
                                <tr>
                                    <td align="right">Display Name:</td>
                                    <td><input type="text" id="meta_display_name" class="text-input-tab"/></td>
                                </tr>
                                <tr>
                                    <td align="right">Discription:</td>
                                    <td><input type="text" id="meta_discription" class="text-input-tab"/></td>
                                </tr>
                                <tr>
                                    <td align="right">Author:</td>
                                    <td><input type="text" id="meta_author" class="text-input-tab"/></td>
                                </tr>
                                <tr>
                                    <td align="right">Version:</td>
                                    <td><input type="text" id="meta_version" class="text-input-tab"/></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td><input type="submit" class="small button blue" value="Save"/></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <% }else{ %>
                <div id="doc-gallary">
                    <h2>Indaba Widget Gallery</h2>
                    Select the widget you would like to download. This will start a step-by-step process through which 
                    you will customize your widget for installation into your website.
                </div>
                <% } %>
            </div>
        </div>
    </body>
</html>
