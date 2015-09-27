<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : workset
    Created on : Feb 21, 2011, 11:26:15 PM
    Author     : Jeff Jiang
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html id="ext-gen6" class=" ext-strict x-viewport">
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <title>Online JSON Viewer</title>
        <!--<link href="http://jsonviewer.stack.hu/extjs/css/ext-gray-all.css" type="text/css" rel="stylesheet">-->
        <link href="jsonviewer/css/ext-gray-all.css" type="text/css" rel="stylesheet">
        <!--<script src="http://jsonviewer.stack.hu/jsonviewer-all.js?v5" type="text/javascript"></script>-->
        <script src="js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="jsonviewer/jsonviewer-all.js" type="text/javascript"></script>
        <style type="text/css" id="styleSheetIconCls">/* Ext.ux.iconCls */</style>
        <style type="text/css">
            .br0 {color:#090 }
            .st0 {color:#36C }
            .sy0 {color:#393 }
        </style>
        <script type="text/javascript" charset="utf-8">
            $(function(){
                //var urlStr = "http://localhost:8080/aggregation/journal.do?horseId=1&includeReviews=1";
                var urlStr = $("#jsonUrl").val();
                $.ajax({
                    type: "GET",
                    url: urlStr,
                    success: function(result){
                        $('#edit').val(result);
                    }
                });
            });
        </script>
    </head>
    <body class=" ext-gecko ext-gecko3 ext-mac " id="ext-gen11">
        <div class="x-tab-panel" id="ext-comp-1004" style="width: 1297px;">
            <div class="x-tab-panel-header x-unselectable" id="ext-gen23" style="-moz-user-select: none; width: 1295px;">
                <div class="x-tab-strip-wrap" id="ext-gen27">
                    <ul class="x-tab-strip x-tab-strip-top" id="ext-gen29">
                        <li id="ext-comp-1004__viewerPanel" class=" ">
                            <a onclick="return false;" class="x-tab-strip-close"></a>
                            <a onclick="return false;" href="#" class="x-tab-right">
                                <em class="x-tab-left">
                                    <span class="x-tab-strip-inner">
                                        <span class="x-tab-strip-text ">Viewer</span>
                                    </span>
                                </em>
                            </a>
                        </li>
                        <li id="ext-comp-1004__textPanel" class=" x-tab-strip-active ">
                            <a onclick="return false;" class="x-tab-strip-close"></a>
                            <a onclick="return false;" href="#" class="x-tab-right">
                                <em class="x-tab-left">
                                    <span class="x-tab-strip-inner">
                                        <span class="x-tab-strip-text ">Text</span>
                                    </span>
                                </em>
                            </a>
                        </li>
                        <li class="x-tab-edge" id="ext-gen30"></li>
                        <div class="x-clear" id="ext-gen31"></div>
                    </ul>
                </div>
                <div class="x-tab-strip-spacer" id="ext-gen28"></div>
            </div>
            <div class="x-tab-panel-bwrap" id="ext-gen24">
                <div class="x-tab-panel-body x-tab-panel-body-top" id="ext-gen25" style="width: 1295px; height: 166px;">
                    <div class="x-panel x-panel-noborder" id="textPanel" style="width: 1295px;">
                        <div class="x-panel-bwrap" id="ext-gen38">
                            <div class="x-panel-tbar x-panel-tbar-noheader x-panel-tbar-noborder" id="ext-gen39">
                                <div class="x-toolbar x-small-editor" id="ext-comp-1013">
                                    <table cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td id="ext-gen41">
                                                    <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn " id="ext-comp-1014" style="width: auto;">
                                                        <tbody>
                                                            <tr>
                                                                <td class="x-btn-left">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                                <td class="x-btn-center">
                                                                    <em unselectable="on">
                                                                        <button type="button" class="x-btn-text" id="ext-gen43">Paste</button>
                                                                    </em>
                                                                </td>
                                                                <td class="x-btn-right">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                                <td id="ext-gen49">
                                                    <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn " id="ext-comp-1015" style="width: auto;">
                                                        <tbody>
                                                            <tr>
                                                                <td class="x-btn-left">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                                <td class="x-btn-center">
                                                                    <em unselectable="on">
                                                                        <button type="button" class="x-btn-text" id="ext-gen51">Copy</button>
                                                                    </em>
                                                                </td>
                                                                <td class="x-btn-right">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                                <td>
                                                    <span class="ytb-sep" id="ext-gen57"></span>
                                                </td>
                                                <td id="ext-gen58">
                                                    <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn " id="ext-comp-1016" style="width: auto;">
                                                        <tbody>
                                                            <tr>
                                                                <td class="x-btn-left">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                                <td class="x-btn-center">
                                                                    <em unselectable="on">
                                                                        <button type="button" id="format" class="x-btn-text" id="ext-gen60">Format</button>
                                                                    </em>
                                                                </td>
                                                                <td class="x-btn-right">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                                <td id="ext-gen66">
                                                    <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn " id="ext-comp-1017" style="width: auto;">
                                                        <tbody>
                                                            <tr>
                                                                <td class="x-btn-left">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                                <td class="x-btn-center">
                                                                    <em unselectable="on">
                                                                        <button type="button" class="x-btn-text" id="ext-gen68">Remove white space</button>
                                                                    </em>
                                                                </td>
                                                                <td class="x-btn-right">
                                                                    <i>&nbsp;</i>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                                <td>
                                                    <span class="ytb-sep" id="ext-gen74"></span>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="x-panel-body x-panel-body-noheader x-panel-body-noborder" id="ext-gen40" style="width: 1295px; height: 140px;">
                                <textarea name="edit" id="edit" autocomplete="off" style="width: 1287px; height: 134px; font-family: monospace;" class=" x-form-textarea x-form-field x-form-empty-field"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <h1>Online JSON Viewer</h1>
        <div class="tab">
            <h2>About JSON</h2>
            <div>
                <b>JSON</b>
                , short for
                <b>JavaScript Object Notation</b>
                , is a lightweight computer data interchange format. JSON is a text-based, human-readable format for representing simple data structures and associative arrays (called objects).
                <br>
                <br>
                Read more:
                <a target="_blank" href="http://json.org">json.org</a>
                ,
                <a target="_blank" href="http://en.wikipedia.org/wiki/JSON">wikipedia</a>
                ,
                <a target="_blank" href="http://www.google.com/search?q=json">google</a>
                <br>
                <br>
                <h3>In JSON, they take on these forms</h3>
                <img width="598" height="113" alt="JSON object" src="images/jsonviewer/object.png">
                <br>
                <br>
                <img width="598" height="113" alt="JSON array" src="images/jsonviewer/array.png">
                <br>
                <br>
                <img width="598" height="278" alt="JSON value" src="images/jsonviewer/value.png">
                <br>
                <br>
                <img width="598" height="413" alt="JSON string" src="images/jsonviewer/string.png">
                <br>
                <br>
                <img width="598" height="266" alt="JSON number" src="images/jsonviewer/number.png">
            </div>
        </div>
        <div class="tab">
            <h2>Example</h2>
            <div>
                <pre>
<span class="br0">{</span>
<span class="st0">"firstName"</span>
<span class="sy0">:</span>
<span class="st0">"John"</span>
<span class="sy0">,</span>
<span class="st0">"lastName"</span>
<span class="sy0">:</span>
<span class="st0">"Smith"</span>
<span class="sy0">,</span>
<span class="st0">"gender"</span>
<span class="sy0">:</span>
<span class="st0">"man"</span>
<span class="sy0">,</span>
<span class="st0">"age"</span>
<span class="sy0">:</span>
<span class="st0">32</span>
<span class="sy0">,</span>
<span class="st0">"address"</span>
<span class="sy0">:</span>
<span class="br0">{</span>
<span class="st0">"streetAddress"</span>
<span class="sy0">:</span>
<span class="st0">"21 2nd Street"</span>
<span class="sy0">,</span>
<span class="st0">"city"</span>
<span class="sy0">:</span>
<span class="st0">"New York"</span>
<span class="sy0">,</span>
<span class="st0">"state"</span>
<span class="sy0">:</span>
<span class="st0">"NY"</span>
<span class="sy0">,</span>
<span class="st0">"postalCode"</span>
<span class="sy0">:</span>
<span class="st0">"10021"</span>
<span class="br0">}</span>
<span class="sy0">,</span>
<span class="st0">"phoneNumbers"</span>
<span class="sy0">:</span>
<span class="br0">[</span>
<span class="br0">{</span>
<span class="st0">"type"</span>
<span class="sy0">:</span>
<span class="st0">"home"</span>
<span class="sy0">,</span>
<span class="st0">"number"</span>
<span class="sy0">:</span>
<span class="st0">"212 555-1234"</span>
<span class="br0">}</span>
<span class="sy0">,</span>
<span class="br0">{</span>
<span class="st0">"type"</span>
<span class="sy0">:</span>
<span class="st0">"fax"</span>
<span class="sy0">,</span>
<span class="st0">"number"</span>
<span class="sy0">:</span>
<span class="st0">"646 555-4567"</span>
<span class="br0">}</span>
<span class="br0">]</span>
<span class="br0">}</span>
                </pre>
            </div>
        </div>
        <div class="tab">
            <h2>About Online JSON Viewer</h2>
            <div>
                Convert JSON Strings to a Friendly Readable Format
                <br>
                The application using
                <a target="_blank" href="http://extjs.com">Ext JS</a>
                .
                <br>
                Author: {gabor}
                <br>
                Made in 2009-2010.
            </div>
        </div>
        <div class="x-tip" id="ext-comp-1001" style="position: absolute; z-index: 20000; visibility: hidden; display: none;">
            <div class="x-tip-tl">
                <div class="x-tip-tr">
                    <div class="x-tip-tc">
                        <div class="x-tip-header x-unselectable" id="ext-gen12" style="-moz-user-select: none;">
                            <span class="x-tip-header-text"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="x-tip-bwrap" id="ext-gen13">
                <div class="x-tip-ml">
                    <div class="x-tip-mr">
                        <div class="x-tip-mc">
                            <div class="x-tip-body" id="ext-gen14" style="height: auto;"></div>
                        </div>
                    </div>
                </div>
                <div class="x-tip-bl x-panel-nofooter">
                    <div class="x-tip-br">
                        <div class="x-tip-bc"></div>
                    </div>
                </div>
            </div>
        </div>
        <select class="middlebox" class="x-grid-editor x-hide-display">
            <option value="true">true</option>
            <option value="false">false</option>
        </select>
        <div id="livemargins_control" style="position: absolute; display: none; z-index: 9999;">
            <img width="77" height="5" style="position: absolute; left: -77px; top: -5px;" src="chrome://livemargins/skin/monitor-background-horizontal.png"/>
            <img style="position: absolute; left: 0pt; top: -5px;" src="chrome://livemargins/skin/monitor-background-vertical.png"/>
            <img style="position: absolute; left: 1px; top: 0pt; opacity: 0.5; cursor: pointer;" onmouseout="this.style.opacity=0.5" onmouseover="this.style.opacity=1" src="chrome://livemargins/skin/monitor-play-button.png" id="monitor-play-button"/>
        </div>
        <div class="x-window-proxy" id="ext-gen159" style="display: none;"></div>
        <div class="ext-el-mask" id="ext-gen160" style="display: none; width: 1297px; height: 501px; z-index: 9000;"></div>
        <div class="x-shadow" id="ext-gen183" style="z-index: 9002; left: 549px; top: 199px; width: 199px; height: 110px; display: none;">
            <div class="xst">
                <div class="xstl"></div>
                <div class="xstc" style="width: 187px;"></div>
                <div class="xstr"></div>
            </div>
            <div class="xsc" style="height: 98px;">
                <div class="xsml"></div>
                <div class="xsmc" style="width: 187px;"></div>
                <div class="xsmr"></div>
            </div>
            <div class="xsb">
                <div class="xsbl"></div>
                <div class="xsbc" style="width: 187px;"></div>
                <div class="xsbr"></div>
            </div>
        </div>
        <div class="x-window x-window-plain x-window-dlg" id="ext-comp-1020" style="position: absolute; z-index: 9003; visibility: hidden; left: -10000px; top: -10000px; width: 191px; display: block;">
            <div class="x-window-tl">
                <div class="x-window-tr">
                    <div class="x-window-tc">
                        <div class="x-window-header x-unselectable x-window-draggable" id="ext-gen119" style="-moz-user-select: none;">
                            <div class="x-tool x-tool-close" id="ext-gen163" style="display: none;">&nbsp;</div>
                            <span class="x-window-header-text" id="ext-gen161">JSON error</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="x-window-bwrap" id="ext-gen120">
                <div class="x-window-ml">
                    <div class="x-window-mr">
                        <div class="x-window-mc">
                            <div class="x-window-body" id="ext-gen121" style="width: 159px; height: auto;">
                                <div id="ext-gen170">
                                    <div class="ext-mb-icon ext-mb-error" id="ext-gen171"></div>
                                    <div class="ext-mb-content">
                                        <span class="ext-mb-text" id="ext-gen172">Invalid JSON variable</span>
                                        <br>
                                        <div class="ext-mb-fix-cursor">
                                            <input type="text" class="ext-mb-input" id="ext-gen173" style="display: none;">
                                            <textarea class="ext-mb-textarea" id="ext-gen175" style="display: none;"></textarea>
                                        </div>
                                    </div>
                                    <div class="x-progress-wrap x-hide-display" id="ext-comp-1025">
                                        <div class="x-progress-inner">
                                            <div class="x-progress-bar" id="ext-gen177" style="height: 16px; width: 0px;">
                                                <div class="x-progress-text " id="ext-gen178" style="z-index: 99; width: 0px;">
                                                    <div id="ext-gen180" style="width: 382px; height: 18px;">&nbsp;</div>
                                                </div>
                                            </div>
                                            <div class="x-progress-text x-progress-text-back" id="ext-gen179">
                                                <div id="ext-gen181" style="width: 382px; height: 18px;">&nbsp;</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="x-clear" id="ext-gen182"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="x-window-bl">
                    <div class="x-window-br">
                        <div class="x-window-bc">
                            <div class="x-window-footer" id="ext-gen122">
                                <div class="x-panel-btns-ct">
                                    <div class="x-panel-btns x-panel-btns-center">
                                        <table cellspacing="0">
                                            <tbody>
                                                <tr>
                                                    <td class="x-panel-btn-td" id="ext-gen125">
                                                        <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn " id="ext-comp-1021" style="width: 75px;">
                                                            <tbody>
                                                                <tr>
                                                                    <td class="x-btn-left">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                    <td class="x-btn-center">
                                                                        <em unselectable="on">
                                                                            <button type="button" class="x-btn-text" id="ext-gen127">OK</button>
                                                                        </em>
                                                                    </td>
                                                                    <td class="x-btn-right">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                    <td class="x-panel-btn-td x-hide-offsets" id="ext-gen133">
                                                        <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn" id="ext-comp-1022" style="width: 75px;">
                                                            <tbody>
                                                                <tr>
                                                                    <td class="x-btn-left">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                    <td class="x-btn-center">
                                                                        <em unselectable="on">
                                                                            <button type="button" class="x-btn-text" id="ext-gen135">Yes</button>
                                                                        </em>
                                                                    </td>
                                                                    <td class="x-btn-right">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                    <td class="x-panel-btn-td x-hide-offsets" id="ext-gen141">
                                                        <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn" id="ext-comp-1023" style="width: 75px;">
                                                            <tbody>
                                                                <tr>
                                                                    <td class="x-btn-left">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                    <td class="x-btn-center">
                                                                        <em unselectable="on">
                                                                            <button type="button" class="x-btn-text" id="ext-gen143">No</button>
                                                                        </em>
                                                                    </td>
                                                                    <td class="x-btn-right">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                    <td class="x-panel-btn-td x-hide-offsets" id="ext-gen149">
                                                        <table cellspacing="0" cellpadding="0" border="0" class="x-btn-wrap x-btn" id="ext-comp-1024" style="width: 75px;">
                                                            <tbody>
                                                                <tr>
                                                                    <td class="x-btn-left">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                    <td class="x-btn-center">
                                                                        <em unselectable="on">
                                                                            <button type="button" class="x-btn-text" id="ext-gen151">Cancel</button>
                                                                        </em>
                                                                    </td>
                                                                    <td class="x-btn-right">
                                                                        <i>&nbsp;</i>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <div class="x-clear"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <a tabindex="-1" class="x-dlg-focus" href="#" id="ext-gen157">&nbsp;</a>
        </div>
        <input type="text" id="jsonUrl" name="jsonUrl" value="${url}" style="display: none;" />
    </body>
</html>