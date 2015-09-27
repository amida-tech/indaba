<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Indaba - Widget How-to</title>
        <link type="text/css" href="css/base/jquery.ui.all.css" rel="Stylesheet" />
        <link rel="stylesheet" href="css/wms.css" />
        <link rel="stylesheet" href="css/howto.css" />
        <script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
    </head>
    <body>
        <div id="doc">
            <a href="wms.jsp"><h1 id="logo">Indaba - Widget Management System</h1></a>
            <div id="doc-content">
                <div id="widget">
                    <h2>How to include Indaba Widget in your site page</h2>
                    <hr/>
                    <dl>
                        <dt>
                            <em>1</em>
                            Put "indaba_widget_helper.html" on your web server
                        </dt>
                        <dd>
                            Right click on this link: <a href="indaba_widget_helper.html">indaba_widget_helper.html</a>,
                            and select "Save as..." to save the helper html to local.
                            Then put this html file anywhere on your web server.
                            Remember the url, it may be something like <span class="parameter"><u>http://www.yoursite.com/to/path/indaba_widget_helper.html</u></span>.
                            <div class="note">
                                NOTE:
                                <div>This helper page is an empty content page, it contains some JavaScript which helps communication between widget page and your site page.</div>
                            </div>
                            <hr/>
                        </dd>
                        <dt>
                            <em>2</em>
                            Put "indaba_widget.js" on your web server, and include it in your page
                        </dt>
                        <dd>
                            Right click on this link: <a href="indaba_widget.js">indaba_widget.js</a>,
                            and select "Save as..." to save the JavaScript file to local.
                            Then put it anywhere on your web server.
                            Remember the url, it may be something like "<u>http://www.yoursite.com/to/path/indaba_widget.js</u>".
                            <div class="note">
                                NOTE:
                                <ul style="list-style-position:outside;margin-left:1.5em;">
                                    <li>This JavaScript file serves 2 major purposes:
                                        <ol style="margin-left:1.5em;">
                                            <li>Auto Resizing iframe height according to widget content</li>
                                            <li>Parsing <u>Permlink</u> to restore widgets content status<br/>
                                                <a href="#" onclick="$('#permlink').toggle('slow');return false;">Learn more about Permlink</a>
                                                <div id="permlink" style="display:none;">
                                                    <p>"Permlink" is a url that contains each framed widget content status,
                                                    especially useful when you're navigating multi-step widgets and want to share your page
                                                    status with your friend.</p>
                                                    <p>Please check the <a href="sample/demo4.html">Permlink Demo</a> for live example.</p>
                                                </div>
                                            </li>
                                        </ol>
                                    </li>
                                    <li>We suggest this JavaScript file to be included right before the end of the &lt;/body> tag in your page.</li>
                                    <li>You can also customize this JavaScript file, to invoke $widget.loadPermlink() method anywhere you want.</li>
                                    <li>The JavaScript file provides an API: $widget.getPermlink(), which returns the Permlink url.
                                        It's your call how you will present the Permlink to your site user.</li>
                                </ul>
                            </div>
                            <hr/>
                        </dd>
                        <dt>
                            <em>3</em>
                            Include Indaba Widget in your page
                        </dt>
                        <dd>
                            Indaba Widget should be included by an IFRAME, yoru page source may look like following.
                            Please note that <span class="result-src">IFRAME_SRC_GENERATE_BY_WMS</span> is a URL string
                            generated by our online tool - <a href="wms.jsp">WMS (Widget Management System)</a>.
                            <pre>
                            &lt;html>
                                &lt;head>
                                    &lt;meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
                                    &lt;title>your page title&lt;/title>
                                &lt;/head>
                                &lt;body>
                                    &lt;!-- your page content -->
                                    &lt;iframe id="<span class="parameter">frame1</span>" frameborder="0" scrolling="no" src="<span class="result-src">IFRAME_SRC_GENERATED_BY_WMS</span>" width="xxx">&lt;/iframe>
                                    &lt;!-- your other page content, maybe more widget frames -->
                                    &lt;script type="text/javascript" src="http://www.yoursite.com/to/path/indaba_widget.js">&lt;/script>
                                &lt;/body>
                            &lt;/html>
                            </pre>
                        </dd>
                    </dl>
                </div>
                <div id="sidebar">
                    <h2>Examples:</h2>
                    <ul>
                        <li><span class="ui-icon ui-icon-carat-1-e"></span><a href="sample/demo1.html">Demo 1</a></li>
                        <li><span class="ui-icon ui-icon-carat-1-e"></span><a href="sample/demo2.html">Demo 2</a></li>
                        <li><span class="ui-icon ui-icon-carat-1-e"></span><a href="sample/demo3.html">Demo 3</a></li>
                        <li><span class="ui-icon ui-icon-carat-1-e"></span><a href="sample/demo4.html">Permlink</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
