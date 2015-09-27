/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import com.ocs.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.UserService;
import com.ocs.util.CookieUtils;
import com.ocs.util.UserSessionManager;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.Interceptor;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class BaseInterceptor implements Interceptor, ServletRequestAware, ServletResponseAware {

    private static Logger logger = Logger.getLogger(BaseInterceptor.class);
    private HttpServletRequest request = null;
    private HttpServletResponse response = null;
    @Autowired
    private UserService userSrvc;
    private String contextPath = null;

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation ai) throws Exception {
        ActionContext context = ai.getInvocationContext();
        HttpServletRequest request = ServletActionContext.getRequest();
        contextPath = request.getContextPath();
        request.setAttribute("contextPath", contextPath);
        logger.debug("################################ " + ai.getInvocationContext().getName() + ", " + contextPath);

        context.put("contextPath", contextPath);
        String token = CookieUtils.getTokenCookie(request);
        int uid = UserSessionManager.getInstance().getUserId(token);

        User user = userSrvc.getUser(uid);
        LoginUser loginUser = null;
        if (user != null) {
            loginUser = new LoginUser(user);
            request.setAttribute("user", loginUser);

            if (!loginUser.getChecker().hasAnyOrgAuthority()) {
                // the user has no access
                request.setAttribute("errMsg", ControlPanelMessages.NO_SYSTEM_ACCESS);
                String loginPage = request.getContextPath();
                response.sendRedirect(loginPage);
                return null;
            }
        }

        String result = null;
        try {
            result = ai.invoke();
        } catch (Exception ex) {
            logger.error("Error occurred", ex);
            BaseExceptionHandler.handle(loginUser, request, ex);
            
            if (result == null) {
                result = "ex";
            }
        }
        return result;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    private String findResultFromExceptions(List exceptionMappings, Throwable t) {
        String result = null;

        // Check for specific exception mappings.
        if (exceptionMappings != null) {
            int deepest = Integer.MAX_VALUE;
            for (Iterator iter = exceptionMappings.iterator(); iter.hasNext();) {
                ExceptionMappingConfig exceptionMappingConfig = (ExceptionMappingConfig) iter.next();
                int depth = getDepth(exceptionMappingConfig.getExceptionClassName(), t);
                if (depth >= 0 && depth < deepest) {
                    deepest = depth;
                    result = exceptionMappingConfig.getResult();
                }
            }
        }

        return result;
    }

    public int getDepth(String exceptionMapping, Throwable t) {
        return getDepth(exceptionMapping, t.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class exceptionClass, int depth) {
        if (exceptionClass.getName().indexOf(exceptionMapping) != -1) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass.equals(Throwable.class)) {
            return -1;
        }
        return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
    }

    /**
     * Default implementation to handle ExceptionHolder publishing. Pushes given ExceptionHolder on the stack.
     * Subclasses may override this to customize publishing.
     *
     * @param invocation The invocation to publish Exception for.
     * @param exceptionHolder The exceptionHolder wrapping the Exception to publish.
     */
    protected void publishException(ActionInvocation invocation, ExceptionHolder exceptionHolder) {
        invocation.getStack().push(exceptionHolder);
    }
}
