<%--
    Document   : Survey Config - 'Indicator Tree'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" href="${contextPath}/resources/plugins/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">

<style type="text/css">
    .ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
    .ztree li ul.level0 {padding:0; background:none;}
    .ztree li span.button.rootIcon_ico_open{margin-right:2px; background: url(${contextPath}/resources/plugins/zTree/css/zTreeStyle/img/diy/1_open.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
    .ztree li span.button.rootIcon_ico_close{margin-right:2px; background: url(${contextPath}/resources/plugins/zTree/css/zTreeStyle/img/diy/1_close.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
    #scTreeBox {
        background: none repeat scroll 0 0 #fefefe;
        border: 1px solid #eee;
        font-family: Verdana;
        padding: 15px;
    }
    div#rMenu {
        background: none repeat scroll 0 0 #F0F0F0;
        border: 1px solid #979797;
        box-shadow: 1px 1px 2px #999;

        left: 0;
        margin: 0;
        min-width: 180px;
        position: absolute;

        z-index: 99;
        visibility:hidden;
        top:0; 
        text-align: left;
        padding: 2px;
        display: block;
    }
    div#rMenu ul li{
        font-family: Verdana;
        padding: 0;
        position: relative;
        display: block;
        list-style-type: none;
        margin: 0;
    }
    div#rMenu ul li a {
        display: block;
        line-height: 17px;
        margin: 1px 1px 0;
        padding: 1px 6px;
        text-decoration: none;
    }
    div#rMenu ul li span {
        float: left;
        height: 16px;
        margin: 2px;
        text-decoration: none;
        width: 16px;
    }
    div#rMenu ul li#m_add_cat span {
        background-image: url(${contextPath}/resources/images/add-group.png);
    }
    div#rMenu ul li#m_add_qstn span {
        background-image: url(${contextPath}/resources/images/add-qstn.png);
    }
    div#rMenu ul li#m_del span {
        background-image: url(${contextPath}/resources/images/remove.png);
    }
    div#rMenu ul li#m_edit span {
        background-image: url(${contextPath}/resources/images/edit.png);
    }
    div#rMenu ul li.separator {
        background: none repeat scroll 0 0 white;
        border-top: 1px solid #E0E0E0;
        margin: 0;
        font-size: 1px;
        height: 1px;
        line-height: 1px;
        min-height: 0;
        padding: 0;
    }
    div#rMenu ul li a:hover {
        background: none repeat scroll 0 0 #E8EFF7;
        border: 1px solid #AECFF7;
        border-radius: 2px 2px 2px 2px;
        color: black;
        padding: 0 5px;
    }
    .inlineBox {
        width: 100%;
        margin-top: 1px;
        z-index: 10000;
    }
    .inlineBox .inner{
        margin-left: 18px;
        border: 1px solid #888;
        box-shadow: 2px 2px 2px 2px #eee;
        border-radius: 2px 2px 2px 2px;
    }
    .inlineBox .inner:hover{
        border: 1px solid #ffb951;
    }
    .inlineBox a.btn {
        padding:5px 10px;
        margin: 0px 10px;
        height: 16px;
    }
    .inlineBox .inner form{
        margin: 10px 10px 5px 10px;
    }
    .inlineBox form fieldset.block {
        margin:0px;
        width: 100%;
        border: 0px;
        padding: 0px;
    }
    .inlineBox li {
        white-space: normal;
    }
    .inlineBox dd {
        margin: 2px 10px 6px 0;
    }
    .inlineBox dt {
        margin: 4px 10px 6px 0;
        width: 70px;
    }
    .inlineBox .ol_loading span.ol_loading_text {
        margin-left: 110px;
        color:#1e62d0;
    }
    .ztree span.loading {
        background-image: url(${contextPath}/resources/plugins/zTree/css/zTreeStyle/img/loading.gif);
        vertical-align: top;
        background-attachment: scroll;
        background-color: transparent;
        background-repeat: no-repeat;
        border: 0 none;
        cursor: pointer;
        display: inline-block;
        height: 16px;
        line-height: 0;
        margin: 0px 2px 0px 0px;
        outline: medium none;
        width: 16px;
    }
    .ztree li a span.button.ico_docu {
        background-image: url("${contextPath}/resources/images/qstn.png");
        background-position: 0px 0px;
    }
    .notes {
        background: #fefefe;
        border: 1px solid #EEEEEE;
        margin: 0px 0px 10px;
        padding: 10px;
        color: #444;
        box-shadow: 1px 1px 1px #ddd;
    }
    .notes .help{
        background: url('${contextPath}/resources/images/help.png') center left no-repeat;
        padding: 0px 20px;
    }
</style>
<div id="surveyTreeTab" >
    <div class="notes">
        <span class="help"> <indaba:msg key="cp.text.survey_tree_help" /></span>
    </div>
    <ul id="scTreeBox" class="ztree"></ul>
    <div id="rMenu">
        <ul>
            <li id="m_edit"><span> </span><a href="#">Edit</a></li>
            <li class="separator m_edit_sep"></li>
            <li id="m_add_cat"><span> </span><a href="#">New Category</a></li>
            <li class="separator m_add_cat_sep"></li>
            <li id="m_add_qstn"><span> </span><a href="#">New Question</a></li>
            <li class="separator m_add_qstn_sep"></li>
            <li id="m_del" ><span> </span><a href="#">Remove</a></li>
        </ul>
    </div>
    <jsp:include page="survey-category-form-inc.jsp" flush="true" />
    <jsp:include page="survey-question-form-inc.jsp" flush="true" />
</div>
<script type="text/javascript" charset="utf-8">
    var scTreeInitialized = false;
    var sconfId = ${sconfId};
    var zTree, rMenu;
    $(function(){
        if(!scTreeInitialized) {
            scTreeInitialized = true;
            var canEdit = true, canMove = true;
    <c:if test="${used}">
                canEdit = false; canMove = false;
    </c:if>
                var settings = {
                    edit: { enable: canEdit, isMove: canMove, showRemoveBtn: false, showRenameBtn: false, drag: {inner: dropInner} },
                    data: { 
                        simpleData: { enable: true },
                        key: {title: "title"}
                    },
                    view: {selectedMulti:false},
                    callback:{
                        onRightClick: doRightClick,
                        beforeDrag: beforeDrag,
                        beforeDrop: beforeDrop,
                        beforeDragOpen: beforeDragOpen,
                        onDrag: onDrag,
                        onDrop: onDrop,
                        onExpand: onExpand,
                        onCollapse: function(){fixDHTMLXTabbarHeight("content"); return true; }
                    }
                };
                $.ajax({
                    url:'${contextPath}/surveyconf/survey-config!getTree',
                    data: { sconfId: '${sconfId}' }, type: 'POST', dataType: 'json',
                    success: function(data){
                        $.fn.zTree.init($("#scTreeBox"), settings, data);
                        zTree = $.fn.zTree.getZTreeObj("scTreeBox");
                        rMenu = $("#rMenu");
                        fixDHTMLXTabbarHeight("content");
                    }
                });
                initialize('${contextPath}', $('div#catFormBox'), $('div#qstnFormBox'));
            }
        });
        function doRightClick(event, treeId, treeNode) {
            if(${used}) {
                return false;
            }
            var node = $('li#'+treeNode.tId +'>a>span#'+treeNode.tId+'_span');
            var left = node[0].offsetLeft+2;
            var top = node[0].offsetTop + node.height() + 4;
            //alert(treeNode.offsetLeft + ", " + treeNode.offsetTop);
            if (!treeNode || !treeNode.pId) {
                zTree.selectNode(treeNode);
                showRMenu("root", left, top);
            } else if (treeNode && !treeNode.noR) {
                zTree.selectNode(treeNode);
                showRMenu(treeNode.isParent?"catetory":"question", left, top);
            }
        }
        function showRMenu(type, x, y) {
            $("#rMenu ul").show();
            $("#rMenu ul li").show();
            if (type=="root") {
                $("#rMenu li#m_edit").hide();
                $("#rMenu li.m_edit_sep").hide();
                $("#rMenu li#m_del").hide();
                $("#rMenu li.m_add_qstn_sep").hide();
            } else if (type=="catetory") {
            } else {
                $("#rMenu li#m_add_cat").hide();
                $("#rMenu li.m_add_cat_sep").hide();
                $("#rMenu li#m_add_qstn").hide();
                $("#rMenu li.m_add_qstn_sep").hide();
            }
            rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

            $("body").bind("mousedown", onBodyMouseDown);
        }
        function hideRMenu() {
            if (rMenu) rMenu.css({"visibility": "hidden"});
            $("body").unbind("mousedown", onBodyMouseDown);
        }
        function onBodyMouseDown(event){
            if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
                rMenu.css({"visibility" : "hidden"});
            }
        }
        var className = "dark", curDragNodes, autoExpandNode;
        /**************************************************************
         *
         * Call back functions on Tree
         *
         **************************************************************/
        function dropPrev(treeId, nodes, targetNode) {
            var pNode = targetNode.getParentNode();
            if (pNode && pNode.dropInner === false) {
                return false;
            } else {
                for (var i=0,l=curDragNodes.length; i<l; i++) {
                    var curPNode = curDragNodes[i].getParentNode();
                    if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
                        return false;
                    }
                }
            }
            return true;
        }
        function dropInner(treeId, nodes, targetNode) {
            if (targetNode && targetNode.dropInner === false) {
                return false;
            } else {
                for (var i=0,l=curDragNodes.length; i<l; i++) {
                    if (!targetNode && curDragNodes[i].dropRoot === false) {
                        return false;
                    } else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
                        return false;
                    }
                }
            }
            return true;
        }
        function dropNext(treeId, nodes, targetNode) {
            var pNode = targetNode.getParentNode();
            if (pNode && pNode.dropInner === false) {
                return false;
            } else {
                for (var i=0,l=curDragNodes.length; i<l; i++) {
                    var curPNode = curDragNodes[i].getParentNode();
                    if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
                        return false;
                    }
                }
            }
            return true;
        }

        function beforeDrag(treeId, treeNodes) {
            className = (className === "dark" ? "":"dark");
            for (var i=0,l=treeNodes.length; i<l; i++) {
                if (treeNodes[i].drag === false) {
                    curDragNodes = null;
                    return false;
                } else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
                    curDragNodes = null;
                    return false;
                }
            }
            curDragNodes = treeNodes;
            return true;
        }
        function beforeDragOpen(treeId, treeNode) {
            autoExpandNode = treeNode;
            return true;
        }
        function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
            className = (className === "dark" ? "":"dark");
            /*
        $('.debug-log').empty();
        $('.debug-log').append("<b>moveType</b>=" + moveType + "<br/>"+
            "<b>origNodeId</b>="+getNodeId(treeNodes[0]) + ", <b>origName</b>=<span style='color:red'>" +treeNodes[0].name +"</span><br/>" +
            '<b>targetNodeId</b>=' + getNodeId(targetNode) + ", <b>targetName</b>=<span style='color:red'>" +targetNode.name + '</span>, <b>pid</b>=' + getNodeId(targetNode, 'pId'));
             */
            moveNode('${contextPath}', sconfId, getNodeId(treeNodes[0]), treeNodes[0].isParent, getNodeId(targetNode), getNodeId(targetNode, 'pId'), targetNode.isParent, moveType);
            return true;
        }
        function onDrag(event, treeId, treeNodes) {
            className = (className === "dark" ? "":"dark");
        }
        function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
            className = (className === "dark" ? "":"dark");
        }
        function onExpand(event, treeId, treeNode) {
            if (treeNode === autoExpandNode) {
                className = (className === "dark" ? "":"dark");
            }
            fixDHTMLXTabbarHeight("content");
            return true;
        }
</script>
<script type="text/javascript" src="${contextPath}/resources/plugins/zTree/js/jquery.ztree.all-3.5.js"></script>