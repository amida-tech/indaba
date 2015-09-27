<%-- 
    Document   : export_doc_1
    Created on : 2011-2-27, 21:55:44
    Author     : Jeanbone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Create a document export</title>
        <link type="text/css" rel="stylesheet" href="css/tablesorter/themes/default/style.css" />
        <link type="text/css" href="css/jquery-ui-1.8.6.custom.css" rel="stylesheet" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.6.custom.min.js"></script>
        <script type="text/javascript" charset="utf-8">
            function projFilter() {
                var projectName = $("#projectName").val().toLowerCase();
                $("td.project-name").parent("tr").show();
                var count = 0;
                $("tr.even, tr.odd").each(function() {
                    if (projectName.length > 0 && $.trim($(this).find("td.project-name").text()).toLowerCase().indexOf(projectName) < 0) {
                        $(this).find("span").attr("class", "");     // uncheck
                        $(this).hide();                             // filtered
                    } else {
                        $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
                        count++;
                    }
                });
            }

            function prodFilter() {
                var productName = $("#productName").val().toLowerCase();
                $("td.project-name").parent("tr").show();
                var count = 0;
                $("tr.even, tr.odd").each(function() {
                    if (productName.length > 0 && $.trim($(this).find("td.product-name").text()).toLowerCase().indexOf(productName) < 0) {
                        $(this).find("span").attr("class", "");     // uncheck
                        $(this).hide();                             // filtered
                    } else {
                        $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
                        count++;
                    }
                });
            }
            
            $(function(){
                if ($("table.tablesorter tbody").find("tr").length > 0) {
                    $("table.tablesorter").tablesorter({headers:{0:{sorter: false}}});
                }

                $("#projectName").autocomplete({
                    source: eval('(${projectNames})'),
                    minLength: 0,
                    change: function() {
                        projFilter();
                    }
                });

                $("#productName").autocomplete({
                    source: eval('(${productNames})'),
                    minLength: 0,
                    change: function() {
                        prodFilter();
                    }
                });
                
                if (${journalForm.productId > 0}) {
                    $("tr.even, tr.odd").each(function() {
                        if ($(this).find("input").val() == "${journalForm.productId}") {
                            $(this).find("span").attr("class", "checked");
                        }
                    });
                }
            });

            function doSubmit() {
                if ($("span.checked").length <= 0) {
                    alert("Please select a product.");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="docexport" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <jsp:include page="docexportinstructions.jsp" flush="true" />
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create a document export</h2>
                    <ul id="steps-bar" class="seg-3">
                        <li class="current">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Select Products</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select Targets</dd>
                            </dl>
                        </li>
                        <li class="last">
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Download your files</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <form id="chooseProduct" action="createJournalExport.do?step=2" method="post" >
                        <fieldset>
                            <legend>Select products</legend>
                            <table>
                                <tr><td>
                                        <ul>
                                            <li>
                                                <table style="width: 800px; font-size: 13px;">
                                                    <tr>
                                                        <td width="490px">
                                                        </td>
                                                        <td align="right" width="200px">
                                                            <button onclick="projFilter();return false;">Filter by Project</button>
                                                        </td>
                                                        <td align="right">
                                                            <input id="projectName" type="text" size="29" maxlength="26" />
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="490px">
                                                        </td>
                                                        <td align="right" width="200px">
                                                            <button onclick="prodFilter();return false;">Filter by Product</button>
                                                        </td>
                                                        <td align="right">
                                                            <input id="productName" type="text" size="29" maxlength="26" />
                                                        </td>
                                                    </tr>
                                                </table>
                                            </li>
                                            <li>
                                                <table class="tablesorter">
                                                    <thead>
                                                        <tr>
                                                            <th>Choose</th>
                                                            <th title="Click to order">Project Name</th>
                                                            <th title="Click to order">Product Name</th>
                                                        </tr>
                                                    </thead>
                                                    <tfoot>
                                                        <tr>
                                                            <th></th>
                                                            <th>Project Name</th>
                                                            <th>Product Name</th>
                                                        </tr>
                                                    </tfoot>
                                                    <c:forEach items="${products}" var="productVO" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${status.index % 2 == 0}">
                                                                <tr class="even" id="tr_${productVO.product.id}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                <tr class="odd" id="tr_${productVO.product.id}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td>
                                                                <input type="radio" name="productId" value="${productVO.product.id}" />
                                                            </td>
                                                            <td class="project-name">
                                                                ${productVO.projectName}
                                                            </td>
                                                            <td class="product-name">
                                                                ${productVO.product.name}

                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </li>
                                            <li>
                                                <input type="submit" value="NEXT STEP" onclick="return doSubmit();" />
                                            </li>
                                        </ul>
                                    </td>
                                </tr>
                            </table>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
