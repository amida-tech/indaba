/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.LoginUser;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Jeff
 */
public class BaseExceptionHandler {

    public static void handle(LoginUser user, HttpServletRequest httpReq, Exception ex) {
        if (ex == null) {
            return;
        }
        String errMsg = ex.getMessage();
        if (ex instanceof java.lang.NullPointerException) {
            if (ex.getStackTrace() != null && "handleUnknownActionMethod".equals(ex.getStackTrace()[0].getMethodName())) {
                errMsg = getMessage(user, ControlPanelMessages.KEY_ERROR_INVALID_REQUEST_URL);
            }
        } else if (ex instanceof java.lang.NoSuchMethodException) {
            if (ex.getStackTrace() != null && "getMethod".equals(ex.getStackTrace()[0].getMethodName())) {
                errMsg = getMessage(user, ControlPanelMessages.KEY_ERROR_INVALID_REQUEST_URL);
            }
        }
        httpReq.setAttribute("errMsg", errMsg);
    }

    private static String getMessage(LoginUser user, String key) {
        if (user == null) {
            return Messages.getInstance().getMessage(key, ControlPanelConstants.DEFAULT_LANGUAGE_ID);
        } else {
            return user.message(key);
        }
    }
}
