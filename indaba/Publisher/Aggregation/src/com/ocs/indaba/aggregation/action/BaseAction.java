/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.common.Messages;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * This is a base action.
 *
 * @author Jeff
 *
 */
public abstract class BaseAction extends Action {

    private static final Logger logger = Logger.getLogger(BaseAction.class);
    protected static final String FWD_LOGIN = "login";
    protected static final String FWD_SUCCESS = "success";
    protected static final String FWD_FAILURE = "failure";
    protected static final String FWD_STEP1 = "step1";
    protected static final String FWD_STEP2 = "step2";
    protected static final String FWD_STEP3 = "step3";
    protected static final String FWD_STEP4 = "step4";
    protected static final String FWD_DONE = "done";
    protected static final String FWD_ERROR = "error";
    protected static final String ATTR_JOURNAL = "journal";
    protected static final String ATTR_ACTIVE = "active";
    protected static final String ATTR_ERR_MSG = "errMsg";
    protected static final String ATTR_USERID = "uid";
    protected static final String ATTR_USERNAME = "username";
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_USER_ORG_ID = "userOrgId";
    protected static final String ATTR_MANAGEWIDGET_VISIBLE = "manageWidgetVisible";
    protected static final String ATTR_MANAGEWORKSET_VISIBLE = "manageWorksetVisible";
    protected static final String ATTR_EXPORTSCORECARDREPORT_VISIBLE = "exportScorecardReportVisible";
    protected static final String ATTR_SITE_ADMIN = "siteAdmin";
    protected static final String ATTR_RET_URL = "returl";
    protected static final String ATTR_WORKINGSET = "workingset";
    protected static final String ATTR_ORGANIZATIONS = "organizations";
    protected static final String ATTR_INDICATORS = "indicators";
    protected static final String ATTR_WORKSET = "workset";
    protected static final String ATTR_DATA = "data";
    protected static final String ATTR_TARGETS = "targets";
    protected static final String TAB_EXPORT = "export";
    protected static final String TAB_INTRODUCTION = "introduction";
    protected static final String TAB_HELP = "TAB_HELP";
    protected static final String PARAM_STEP = "step";
    // Session
    protected HttpSession session = null;
    protected int targetId = -1;
    protected int productId = -1;
    protected int roleId = -1;
    protected int teamId = -1;
    protected int status = -1;
    protected int hasCase = -1;
    protected int cTagId = -1;
    protected int superuser = -1;
    protected int uid = Constants.INVALID_INT_ID;
    protected String username = null;
    protected int prjid = Constants.INVALID_INT_ID;
    protected String prjName = null;
    protected String actionPath = "";

    protected boolean manageWidgetVisible = false;
    protected boolean manageWorksetVisible = false;
    protected boolean exportScorecardReportVisible = false;


    // Message resources
    protected final static MessageResources msgRsrcs = MessageResources.getMessageResources("ApplicationResources");

    /**
     * Get resource messages
     * @param key
     * @param arguments
     * @return
     */
    protected String getMessage(String key, Object... arguments) {
        String message = msgRsrcs.getMessage(key);
        return (message == null) ? msgRsrcs.getMessage(Messages.KEY_ERR_INTERNAL_SERVER) : MessageFormat.format(message, arguments);
    }

    protected ActionForward preprocess(ActionMapping mapping, HttpServletRequest request) {
        return preprocess(mapping, request, true, false);

    }

    protected ActionForward preprocess(ActionMapping mapping, HttpServletRequest request, boolean setRetUrl) {
        return preprocess(mapping, request, setRetUrl, false);

    }

    protected ActionForward preprocess(ActionMapping mapping, HttpServletRequest request, boolean setRetUrl, boolean withAbsoluteUrlPath) {
        return preprocess(mapping, request, setRetUrl, withAbsoluteUrlPath, false);
    }

    protected ActionForward preprocess(ActionMapping mapping, HttpServletRequest request, boolean setRetUrl, boolean withAbsoluteUrlPath, boolean checkExisted) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error("Invalid encoding utf-8", ex);
        }

        if (setRetUrl) {
            setReturnUrl(request, withAbsoluteUrlPath, checkExisted);
        }
        actionPath = request.getServletPath();
        if (actionPath != null) {
            actionPath = actionPath.replaceAll("/", "");
        } else {
            actionPath = "";
        }

        session = request.getSession(true);
        Object value = null;
        if (session != null) {
            if ((value = session.getAttribute(ATTR_USERID)) != null) {
                uid = Integer.valueOf(value.toString());
            } else {
                uid = Constants.INVALID_INT_ID;
            }

            if ((value = session.getAttribute(ATTR_MANAGEWIDGET_VISIBLE)) != null) {
                manageWidgetVisible = Boolean.valueOf(value.toString());
            }

            if ((value = session.getAttribute(ATTR_MANAGEWORKSET_VISIBLE)) != null) {
                manageWorksetVisible = Boolean.valueOf(value.toString());
            }

            if ((value = session.getAttribute(ATTR_EXPORTSCORECARDREPORT_VISIBLE)) != null) {
                exportScorecardReportVisible = Boolean.valueOf(value.toString());
            }

            if ((value = session.getAttribute(ATTR_USERNAME)) != null) {
                username = (String) value;
            } else {
                username = null;
            }
            // set default active tab
            if ((value = session.getAttribute(ATTR_ACTIVE)) == null) {
                session.setAttribute(ATTR_ACTIVE, TAB_EXPORT);
            }
        }
        /*
        if (username == null) {
        request.setAttribute("errmsg", "Please sign in!");
        setReturnUrl(request, true);
        return mapping.findForward(FWD_LOGIN);
        } else {*/
        if (username == null) {
            request.setAttribute("errmsg", "Please sign in!");
            setReturnUrl(request, true);
            return mapping.findForward(FWD_LOGIN);
        } else {
            request.setAttribute(ATTR_USERNAME, username);
        }
        //request.setAttribute(ATTR_NAME, session.getAttribute(ATTR_NAME));
        if (uid > 0) {
            request.setAttribute(ATTR_USERID, uid);
        }

        return null;
        //}
    }

    protected void setReturnUrl(HttpServletRequest request) {
        setReturnUrl(request, true);
    }

    protected void setReturnUrl(HttpServletRequest request, boolean withAbsoluteUrlPath) {
        setReturnUrl(request, withAbsoluteUrlPath, true);
    }

    protected void setReturnUrl(HttpServletRequest request, boolean withAbsoluteUrlPath, boolean checkExisted) {
        if (checkExisted && request.getAttribute(TAB_EXPORT) != null) {
            return;
        }
        StringBuffer reqUrlBuf = (withAbsoluteUrlPath) ? request.getRequestURL() : new StringBuffer(request.getServletPath());
        String query = request.getQueryString();
        if (query != null) {
            reqUrlBuf.append('?').append(query);
        }
        String reqUrl = null;
        try {
            reqUrl = URLEncoder.encode(reqUrlBuf.toString(), "UTF-8");
        } catch (Exception ex) {
        }
        logger.debug("Set attribute returl=" + reqUrl);
        request.setAttribute(ATTR_RET_URL, reqUrl);
    }

    public void writeMsg(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.flush();
        writer.close();
    }

    public void writeMsg(HttpServletResponse response, long val) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.print(val);
        writer.flush();
        writer.close();
    }

    public void writeMsgLn(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println(msg);
        writer.flush();
        writer.close();
    }

    public void writeMsgLn(HttpServletResponse response, long val) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println(val);
        writer.flush();
        writer.close();
    }

    public void writeMsg(HttpServletResponse response, byte[] bytes) throws IOException {
        response.setContentType("text/html");
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(bytes);
        out.flush();
        out.close();
    }

    protected ActionForward noAccess(ActionMapping mapping, HttpServletRequest request) {
        request.setAttribute("errmsg", "You have no access to this function");
        session.invalidate();
        return mapping.findForward(FWD_LOGIN);
    }
}
