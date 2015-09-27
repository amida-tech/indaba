/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.common.Messages;
import com.ocs.indaba.aggregation.service.ProjectService;
import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.service.UserService;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login Action. To help handle the user login request. <br/>
 * 
 * Actually, although struts already provides an useful LoginForm component, in
 * this example we don't plan to leverage it. In a general use, we directly get
 * the request parameters fromHttpServletRequest.
 * 
 * @author Jeff
 * 
 */
public class LoginAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(LoginAction.class);
    private UserService userSrvc = null;
    private ProjectService pubProjectService = null;
    private AccessPermissionService accessPermissionService = null;
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_CURURL = "cururl";
    private static final String PARAM_ASYNCMODE = "asyncmode";
    private static final String ATTR_ERRMSG = "errmsg";

    static private final String[] RELOAD_PATHS = {
        "createjournalexport.do?step=1",
        "createdataexport.do?step=1"
    };

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        username = request.getParameter(PARAM_USERNAME);
        String password = request.getParameter(PARAM_PASSWORD);
        String asyncmode = request.getParameter(PARAM_ASYNCMODE);
        String cururl = request.getParameter(PARAM_CURURL);

        if (cururl != null && !cururl.isEmpty()) {
            cururl = URLDecoder.decode(cururl.toLowerCase(), "UTF-8");
            int idx = cururl.lastIndexOf('/');
            cururl = cururl.substring(idx+1);
        } else {
            cururl = "";
        }
        logger.debug("User login: " + username + " CUR URL: " + cururl);

        // To effectively reduce the database load, it is a better way to do
        // some pre-checks of the validation of parameters. Note, generally
        // the validation should be handled by javascript in front web page.
        boolean valid = userSrvc.authenticate(username, password);

        //String retUrl = request.getParameter(PARAM_CURURL);
        if (valid) {
            User user = userSrvc.getUser(username);
            // TODO: do some extra works, e.g. record user login status(by
            // cookie, session or rewrite url)
            request.setAttribute(PARAM_USERNAME, username);

            // save session
            session = request.getSession(true);
            String name = "";
            if (user.getFirstName() != null) {
                name += user.getFirstName() + " ";
            }
            if (user.getLastName() != null) {
                name += user.getLastName();
            }            

            if (user.getSiteAdmin() == 1) {
                manageWidgetVisible = true;
                manageWorksetVisible = true;
                exportScorecardReportVisible = true;
            } else {
                boolean manageWorksetVisible1 = false;
                boolean exportScorecardReportVisible1 = false;

                Project project = pubProjectService.getProjectByName(Constants.PROJECT_OF_PUBLISHER);

                if (project != null) {
                    boolean inManageWidgetProject = pubProjectService.checkUserInProject(user.getId(), Constants.PROJECT_OF_PUBLISHER);
                    if (inManageWidgetProject) {
                        manageWidgetVisible = accessPermissionService.checkProjectPermission(project.getId(), user.getId(), Constants.RIGHT_OF_MANAGEWIDGET);
                        manageWorksetVisible1 = accessPermissionService.checkProjectPermission(project.getId(), user.getId(), Constants.RIGHT_OF_MANAGEWORKSET);
                        exportScorecardReportVisible1 = accessPermissionService.checkProjectPermission(project.getId(), user.getId(), Constants.RIGHT_OF_EXPORTSCORECARDREPORT);
                    }
                }

                // check org-based permissions
                boolean manageWorksetVisible2 = false;
                boolean exportScorecardReportVisible2 = false;

                OrgAuthorizer orgAuth = new OrgAuthorizer(user);

                if (orgAuth.hasAnyOrgAuthority()) {
                    exportScorecardReportVisible2 = true;
                    manageWorksetVisible2 = true;
                }

                manageWorksetVisible = manageWorksetVisible1 || manageWorksetVisible2;
                exportScorecardReportVisible = exportScorecardReportVisible1 || exportScorecardReportVisible2;
            }
            
            session.setAttribute(ATTR_NAME, name);
            session.setAttribute(ATTR_USERNAME, username);
            session.setAttribute(ATTR_SITE_ADMIN, user.getSiteAdmin());
            session.setAttribute(ATTR_MANAGEWIDGET_VISIBLE, manageWidgetVisible);
            session.setAttribute(ATTR_MANAGEWORKSET_VISIBLE, manageWorksetVisible);
            session.setAttribute(ATTR_EXPORTSCORECARDREPORT_VISIBLE, exportScorecardReportVisible);
            session.setAttribute(ATTR_USERID, user.getId());
            session.setAttribute(ATTR_USER_ORG_ID, user.getOrganizationId());

            logger.debug("Authentication success.");
            //user.setLastLoginTime(new Date());
            //userSrvc.updateUser(user);
            if ("true".equals(asyncmode)) {
                boolean reload = false;
                for (String s : RELOAD_PATHS) {
                    if (cururl.startsWith(s)) {
                        reload = true;
                        break;
                    }
                }

                logger.debug("Reload: " + reload);

                String resp;

                if (reload) {
                    resp = "RELOAD";
                } else {
                    resp = "UPDATE:" +
                        ((name != null) ? name : username) + ":" +
                        (manageWidgetVisible ? "managewidget.do?widgetId=1" : "") + ":" +
                        (exportScorecardReportVisible ? "createScorecardExport.do" : "") + ":" +
                        (manageWorksetVisible ? "manageworkset.do" : "") + ":" +
                        (manageWorksetVisible ? "createworkset.do" : "");

                }

                super.writeMsg(response, resp);
                return null;
            } else {
                //if (StringUtils.isEmpty(retUrl) || retUrl.contains("logout.do")) {
                return mapping.findForward(FWD_SUCCESS);
                //} else {
                //   retUrl = URLDecoder.decode(retUrl, "UTF-8");
                //   logger.debug("Forward to returl: " + retUrl);
                //    ActionForward actionFwd = new ActionForward(retUrl);
                // return actionFwd;
                // }
            }
        } else {
            // not allowed
            request.setAttribute(ATTR_ERRMSG, getResources(request).getMessage(
                    Messages.KEY_ERR_INVALID_USER));
            logger.debug("Authentication failure.");
            if ("true".equals(asyncmode)) {
                super.writeMsg(response, "");
                return null;
            } else {
                //if (StringUtils.isEmpty(retUrl)) {
                //    ActionForward actionFwd = new ActionForward(retUrl);
                //    return actionFwd;
                //} else {
                return mapping.findForward(FWD_FAILURE);
                //}
            }
        }
    }

    @Autowired
    public void setUserSrvc(UserService userSrvc) {
        this.userSrvc = userSrvc;
    }

    @Autowired
    public void setProjectService(ProjectService pubProjectService) {
        this.pubProjectService = pubProjectService;
    }

    @Autowired
    public void setAccessPermissionService(AccessPermissionService accessPermissionService) {
        this.accessPermissionService = accessPermissionService;
    }
}
