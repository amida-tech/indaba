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
        <title>Indaba Publisher - Manage Widgets</title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript">
            this.widgetId = ${widget.id};
            this.widgets = [
            <c:forEach items="${widgets}" var="widget" varStatus="status">
                    {
                        id:'${widget.id}',
                        name: '${widget.displayName}',
                        image: '${widget.iconFileName}',
                        target:'managewidget.do?widgetId=${widget.id}',
                        url:'${widget.targetUrl}',
                        params:'${widget.params}',
                        configType:'${widget.configType}'
                    }<c:if test="${!(status.last)}">,</c:if>
            </c:forEach>
                ];

                $(document).ready(function(){
                    $.each(widgets, function(idx, elm){
                        var html = '<div class="gallary-item">';
                        var cls = '';
                        if (idx + 1 == widgetId)
                            cls = 'class="selected"';
                        html += '<a href="' + this.target+ '" ' + cls +'>';
                        html += '<img src="' + this.image + '" width="88" height="88" alt=""/>';
                        html += '<h5>' + this.name + '</h5>';
                        html += '</a></div>';
                        $('#sidebar').append(html);
                    });
                    $('#sidebar').append('<div style="clear:both;"/>');
                });
        </script>
        <script type="text/javascript">
            function onSaveWidget() {
                var params = new Object();
                params.widgetId = ${widget.id};
                params.displayName = $('#meta_display_name').val();
                params.desc = $('#meta_discription').val();
                params.author = $('#meta_author').val();
                
                $.ajax({
                    type: "POST",
                    url: "savewidget.do",
                    data: params,
                    cache: false,
                    async: false,
                    success: function(result) {
                        var json = eval("(" + result + ")");
                        if(json.ret == 0){
                            $('#widgetDisplayName').text($('#meta_display_name').val());
                            $('#save_result').css("display", "inline");
                            $('#save_result').text("Saved!");
                        }else{
                            $('#save_result').css("display", "inline");
                            $('#save_result').text("Failed!");
                        }
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="managewidgets" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <link rel="stylesheet" href="css/widgetlibrary.css" />
            <style>
                #sidebar{
                    float:left;
                    position:static;
                    text-align:center;
                }
                #meta-container{
                    float:left;
                    margin-left:1.2em;
                }
                h2{
                    border-bottom:1px solid #000;
                    line-height:24px;
                    padding:5px 0;
                }
                table td{
                    font-size:13px;
                }
            </style>
            <div class="interactive">
                <div class="wrapper">
                    <div id="sidebar">
                        <h2>Gallery</h2>
                    </div>
                    <div id="meta-container">
                        <h2 id="widgetDisplayName">${widget.displayName}</h2>
                        <table cellpadding="5">
                            <form action="savewidget.do" method="post">
                                <tr>
                                    <td width="90" align="right">Tech Name:</td>
                                    <td>Journal_Display</td>
                                </tr>
                                <tr>
                                    <td align="right">Display Name:</td>
                                    <td><input type="text" id="meta_display_name" class="text-input-tab" name="displayName" value="${widget.displayName}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" valign="top">Description:</td>
                                    <td><textarea id="meta_discription" class="text-input-tab" name="desc" value="${widget.description}"></textarea></td>
                                </tr>
                                <tr>
                                    <td align="right">Author:</td>
                                    <td><input type="text" id="meta_author" class="text-input-tab" name="author" value="${widget.author}"/></td>
                                </tr>
                                <tr>
                                    <td align="right">Version:</td>
                                    <td>1.0</td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td>
                                        <input type="submit" class="small button blue" value="Save" onclick="return onSaveWidget();" />
                                        <span id="save_result" style="display:none; padding-left: 10px; color: red;"></span>
                                    </td>
                                </tr>
                            </form>
                        </table>
                    </div>
                    <c:choose>
                        <c:when test="${(widgetId > 0)}">

                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                    </c:choose>
                    <div style="clear:both;"></div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>