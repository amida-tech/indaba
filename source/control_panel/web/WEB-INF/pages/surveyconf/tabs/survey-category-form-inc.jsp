<%-- 
    Document   : survey-question-form-inc
    Created on : 2012-11-27, 11:59:18
    Author     : Administrator
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="catFormBox" class="inlineBox hidden">
    <div id="catFormInnerBox" class="inner">
        <form id="catForm" action="#" method="POST">
            <input type="hidden" name="nodeTId"/>
            <input type="hidden" name="action"/>
            <input type="hidden" name="catId"/>
            <input type="hidden" name="sconfId" id="sconfId"/>
            <input type="hidden" name="pCatId"/>
            <input type="hidden" name="weight"/>
            <fieldset id="catFormFs" class="block">
                <dl>
                    <dt><label for="label"><indaba:msg key="cp.label.label"/></label></dt>
                    <dd><input type="text" maxlength="45" name="label" id="label" class="middle-input validate[required]"/></dd>
                </dl>
                <dl>
                    <dt><label for="title"><indaba:msg key="cp.label.title"/></label></dt>
                    <dd><textarea name="title" id="title" class="middle-input validate[required]"></textarea></dd>

                </dl>
                <dl>
                    <dt><label for="description"><indaba:msg key="cp.label.description"/></label></dt>
                    <dd><textarea name="description" id="description" class="middle-input"></textarea></dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd>
                        <div class="btn-icons">
                            <a class="btn save">Save</a>
                            <a class="btn cancel">Cancel</a>
                        </div>
                    </dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>
