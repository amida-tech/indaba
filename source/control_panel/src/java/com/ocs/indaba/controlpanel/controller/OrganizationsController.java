/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import org.apache.struts2.convention.annotation.ResultPath;
import com.opensymphony.xwork2.ModelDriven;
import com.ocs.indaba.controlpanel.service.IOrganizationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;
import com.ocs.util.Pagination;
import com.ocs.indaba.po.User;
import com.ocs.indaba.po.Organization;
import org.json.simple.JSONObject;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.ocs.indaba.service.MailbatchService;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.model.*;
import java.util.Date;
import java.text.MessageFormat;
import com.ocs.common.Config;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.po.Orgadmin;
import com.ocs.util.StringUtils;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author rick
 *
 */
@ResultPath("/WEB-INF/pages/org")
public class OrganizationsController extends BaseController implements ModelDriven<Object> {

    private static final long serialVersionUID = -2487852558172383390L;
    private static final Logger logger = Logger.getLogger(OrganizationsController.class);

    public String index() {
        logger.debug("OrganizationsController::index");
        LoginUser loginUser = getLoginUser();
        if (loginUser.isSiteAdmin()) {
            request.setAttribute(ATTR_SITE_ADMINISTRATOR, true);
        } else {
            request.setAttribute(ATTR_SITE_ADMINISTRATOR, false);
        }
        return RESULT_INDEX;
    }

    @Override
    public String execute() {
        return index();
    }

    private class OrgComparator implements Comparator<OrganizationVO> {

        private String sortName = "name";
        private String sortOrder = "asc";

        public OrgComparator(String sortName, String sortOrder) {
            if (sortName != null) {
                this.sortName = sortName;
            }
            if (sortOrder != null) {
                this.sortOrder = sortOrder;
            }
        }

        @Override
        public int compare(OrganizationVO o1, OrganizationVO o2) {
            String v1;
            String v2;

            if (sortName.equalsIgnoreCase("address")) {
                v1 = o1.getAddress();
                v2 = o2.getAddress();
            } else {
                // sort by name
                v1 = o1.getName();
                v2 = o2.getName();
            }

            v1 = (v1 == null) ? "" : v1.toLowerCase();
            v2 = (v2 == null) ? "" : v2.toLowerCase(); 

            if (sortOrder.equalsIgnoreCase("asc")) {
                return v1.compareTo(v2);
            } else {
                return v2.compareTo(v1);
            }
        }
    }

    // handle request organizations!find
    // request data: rp -- page size, page -- the page number starting with one
    public String find() {
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        String queryType = request.getParameter(PARAM_QUERY_TYPE);
        String query = request.getParameter(PARAM_QUERY);

        logger.debug("Request Params: "
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder
                + "\n\tqueryType=" + queryType
                + "\n\tquery=" + query);

        logger.debug("OrganizationsController::find -- page: " + page + ", page size: " + pageSize);
        LoginUser loginUser = getLoginUser();
        List<OrganizationVO> orgVOs = orgService.getVisibleOrgVO(loginUser);

        Collections.sort(orgVOs, new OrgComparator(sortName, sortOrder));

        Pagination<OrganizationVO> orgPage = new Pagination<OrganizationVO>(orgVOs.size(), page, rp);

        int startIndex = (page - 1) * rp > orgVOs.size() ? orgVOs.size() : (page - 1) * rp;
        int endIndex = page * rp > orgVOs.size() ? orgVOs.size() : page * rp;
        orgPage.addRow(orgVOs.subList(startIndex, endIndex));

        orgPage.addProperty("siteAdmin", loginUser.isSiteAdmin() ? "yes" : "no");

        sendResponseMessage(orgPage.toJsonString());
        logger.debug("OrganizationsController::find -- " + orgPage.toJsonString());
        return null;
    }

    public String get() {
        logger.debug("OrganizationsController::get");
        OrganizationDetailVO org = orgService.getOrgDetail(id);
        sendResponseMessage(org.toJsonString());
        return null;
    }

    public String detail() {
        logger.debug("OrganizationsController::detail");
        request.setAttribute("orgid", Integer.toString(id));
        return "detail";
    }

    public String getOAs() {
        logger.debug("OrganizationsController::getOAs");
        boolean isPOA = false;
        Organization org = orgDao.get(id);
        if (org != null) {
            LoginUser loginUser = getLoginUser();
            if (loginUser != null && loginUser.isSiteAdmin()) {
                isPOA = true;
            } else if (loginUser != null && org.getAdminUserId() == loginUser.getUserId()) {
                isPOA = true;
            }
        }

        logger.debug("User is POA: " + isPOA);

        List<OrgAdminVO> oas = orgService.getOrgOA(id);

        for (OrgAdminVO oa : oas) {
            if (isPOA) {
                oa.setActions("delete");
            }
        }

        if (org != null && org.getAdminUserId() > 0) {
            OrgAdminVO poa = new OrgAdminVO();
            User poaUser = userSrvc.getUser(org.getAdminUserId());

            if (poaUser != null) {
                poa.setEmail(poaUser.getEmail());
                poa.setFirstName(poaUser.getFirstName());
                poa.setLastName(poaUser.getLastName());
                poa.setUserName(poaUser.getUsername());
                poa.setActions("poa");
                oas.add(0, poa);
            }
        }
        // since pager is removed at client side, always set page to 1 and rp to the number of keys
        // keep paging code here in case enabling it later
        page = 1;
        rp = oas.size();
        Pagination<OrgAdminVO> oaPage = new Pagination<OrgAdminVO>(oas.size(), page, rp);
        int startIndex = (page - 1) * rp > oas.size() ? oas.size() : (page - 1) * rp;
        int endIndex = page * rp > oas.size() ? oas.size() : page * rp;
        oaPage.addRow(oas.subList(startIndex, endIndex));

        oaPage.addProperty("poa", isPOA ? "yes" : "no");

        sendResponseMessage(oaPage.toJsonString());
        logger.debug("OrganizationsController::getOAs -- " + oaPage.toJsonString());
        return null;
    }

    public String create() {
        logger.debug("OrganizationsController::create");

        Organization org = orgService.AddOrg(name, address, url, enforceAPISecurity);
        // check if the assigned super administrator exists
        User u = userSrvc.getUserByEmail(email);
        boolean isNewUser = u == null ? true : false;
        if (u == null) {
            u = userSrvc.createUser(firstName, lastName, email, userName, org.getId());
        }
        org.setAdminUserId(u.getId());
        orgService.UpdateOrg(org);
        // send email notification
        sendAddOANotification(org, u, email, isNewUser);
        logger.debug("OrganizationsController::create successfully");
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String update() {
        logger.debug("OrganizationsController::update");
        Organization org = orgService.getOrgPO(id);

        // update organization super administrator
        /**** super admin cannot be changed - YC
        User currentAdmin = userSrvc.getUser(org.getAdminUserId());
        if(!currentAdmin.getEmail().equals(email)) {
        // super administrator changed, check if the user exists
        User newAdmin = userSrvc.getUserByEmail(email);
        boolean isNewUser = newAdmin == null ? true : false;
        if(newAdmin == null) {
        newAdmin = userSrvc.createUser(firstName, lastName, email, userName, org.getId());
        }
        org.setAdminUserId(newAdmin.getId());
        sendNotification(org, newAdmin, isNewUser);
        }
        else {
        // handle first name, last name change, although it almost not possible
        currentAdmin.setFirstName(firstName);
        currentAdmin.setLastName(lastName);
        userSrvc.updateUser(currentAdmin);
        }
         ****/
        // update organization information
        org.setName(name);
        org.setAddress(address);
        org.setUrl(url);
        org.setEnforceApiSecurity(enforceAPISecurity);
        orgService.UpdateOrg(org);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String save() {
        logger.debug("OrganizationsController::save");
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String addOA() {
        logger.debug("OrganizationsController::addOA");
        User oa = userSrvc.getUserByEmail(email);
        boolean isNewUser = (oa == null) ? true : false;
        if (oa == null) {
            // new user, create a user first
            oa = userSrvc.createUser(firstName, lastName, email, userName, id);
        }
        orgService.addOrgOA(id, oa.getId().intValue());
        sendAddOANotification(orgService.getOrgPO(id), oa, email, isNewUser);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String removeOA() {
        logger.debug("OrganizationsController::removeOA" + "OAID=" + id);
        Orgadmin oa = orgService.removeOrgOA(id);

        if (oa != null) {
            sendRemoveOANotification(orgService.getOrgPO(oa.getOrganizationId()), userSrvc.getUser(oa.getUserId()));
        }

        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String generateUserName() {
        logger.debug("OrganizationsController::generateUserName -- email: " + email);
        String str;
        User u = userSrvc.getUserByEmail(email);
        if (u == null) {
            String userName = userSrvc.suggestUserNameByEmail(email);
            JSONObject obj = new JSONObject();
            obj.put("username", userName);
            obj.put("newuser", true);
            str = obj.toJSONString();
        } else {
            JSONObject obj = new JSONObject();
            obj.put("username", u.getUsername());
            obj.put("newuser", false);
            obj.put("firstname", u.getFirstName());
            obj.put("lastname", u.getLastName());
            str = obj.toJSONString();
        }
        logger.debug("OrganizationsController::generateUserName -- returned json string: " + str);
        sendResponseMessage(str);
        return null;
    }

    public String checkAndSuggestUserName() {
        logger.debug("OrganizationsController::checkAndSuggestUserName -- user name: " + userName + ", email: " + email);
        User u = userSrvc.getUserByEmail(email);
        JSONObject obj = new JSONObject();
        if (u == null) {
            // new user
            // see if the provided user name is good
            if (userSrvc.getUser(userName) == null) {
                // the provided user name is fine
                obj.put("email", email);
                obj.put("username", userName);
                obj.put("newuser", true);
                obj.put("nameused", false);
            } else {
                // provided user name is already taken
                obj.put("email", email);
                obj.put("username", userName);
                obj.put("nameused", true);
                obj.put("newuser", true);
                obj.put("suggest", userSrvc.suggestUserName(userName));
            }
        } else {
            // the user already exists - cannot change user name
            obj.put("email", email);
            obj.put("username", userName);
            obj.put("newuser", false);
        }

        logger.debug("OrganizationsController::checkAndSuggestUserName -- returned json string: " + obj.toJSONString());
        sendResponseMessage(obj.toJSONString());
        return null;
    }

    public String checkOrgName() {
        logger.debug("OrganizationsController::checkOrgName");
        OrganizationVO org = orgService.getOrg(name);
        JSONObject obj = new JSONObject();
        if (org != null) {
            obj.put("exist", true);
            obj.put("id", org.getId());
        } else {
            obj.put("exist", false);
        }

        logger.debug("OrganizationsController::checkOrgName -- returned json string: " + obj.toJSONString());
        sendResponseMessage(obj.toJSONString());
        return null;
    }

    public String getOrgInfo() {
        logger.debug("OrganizationsController::getOrgInfo");
        OrganizationVO org = orgService.getOrg(id);
        sendResponseMessage(org.toJsonString());
        return null;
    }

    public Object getModel() {
        return null;
    }

    private void sendAddOANotification(Organization org, User u, String emailAddr, boolean isNewUser) {
        if (Constants.SYSTEM_MODE_DEMO.equals(Config.getString(Config.KEY_SYSTEM_MODE, Constants.SYSTEM_MODE_PROD))) {
            logger.info("System is running in demo mode, skip to send notification to " + u.getUsername());
            return;
        }
        Map<String, String> tokens = new HashMap<String, String>();
        if (isNewUser) {
            String pattern = Config.getString(Config.KEY_NEW_USER_ACCESS_LINK);
            String accessLink = MessageFormat.format(pattern, u.getUsername(), u.getPassword());
            tokens.put(Constants.NOTIFICATION_TOKEN_ACCESS_LINK, accessLink);
        }
        LoginUser loginUser = getLoginUser();
        tokens.put(Constants.NOTIFICATION_TOKEN_ADDER_NAME, loginUser.getFirstname() + " " + loginUser.getLastname());
        tokens.put(Constants.NOTIFICATION_TOKEN_EMAIL, email);
        tokens.put(Constants.NOTIFICATION_TOKEN_FIRST_NAME, u.getFirstName());
        tokens.put(Constants.NOTIFICATION_TOKEN_FULL_NAME, u.getFirstName() + " " + u.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_LAST_NAME, u.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_ORG_NAME, org.getName());
        tokens.put(Constants.NOTIFICATION_TOKEN_USER_NAME, u.getUsername());
        tokens.put(Constants.NOTIFICATION_TOKEN_NOTE, note);
        String notificationType = isNewUser ? Constants.NOTIFICATION_TYPE_NOTIFY_NEW_USER_OA
                : Constants.NOTIFICATION_TYPE_NOTIFY_EXISTING_USER_OA;
        NotificationView notification = notificationItemService.getDefaultNotificationView(notificationType, u.getLanguageId(), tokens);
        mailService.addSystemMail(emailAddr, notification.getSubject(), notification.getBody());
    }


    private void sendRemoveOANotification(Organization org, User u) {
        if (Constants.SYSTEM_MODE_DEMO.equals(Config.getString(Config.KEY_SYSTEM_MODE, Constants.SYSTEM_MODE_PROD))) {
            logger.info("System is running in demo mode, skip to send notification to " + u.getUsername());
            return;
        }

        if (u == null || org == null) return;

        Map<String, String> tokens = new HashMap<String, String>();
        
        LoginUser loginUser = getLoginUser();
        tokens.put(Constants.NOTIFICATION_TOKEN_ADDER_NAME, loginUser.getFirstname() + " " + loginUser.getLastname());
        tokens.put(Constants.NOTIFICATION_TOKEN_EMAIL, email);
        tokens.put(Constants.NOTIFICATION_TOKEN_FIRST_NAME, u.getFirstName());
        tokens.put(Constants.NOTIFICATION_TOKEN_FULL_NAME, u.getFirstName() + " " + u.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_LAST_NAME, u.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_ORG_NAME, org.getName());
        tokens.put(Constants.NOTIFICATION_TOKEN_USER_NAME, u.getUsername());
        tokens.put(Constants.NOTIFICATION_TOKEN_NOTE, note);
        String notificationType = Constants.NOTIFICATION_TYPE_NOTIFY_DEL_USER_OA;
        NotificationView notification = notificationItemService.getDefaultNotificationView(notificationType, u.getLanguageId(), tokens);
        mailService.addSystemMail(u.getEmail(), notification.getSubject(), notification.getBody());
    }

    // handle security keys

    public String verifyUser() {
        logger.debug("OrganizationsController::verifyUser");
        LoginUser loginUser = getLoginUser();
        boolean success = this.userSrvc.authenticate(loginUser.getUsername(), password);
        JSONObject json = new JSONObject();
        json.put("success", success);
        if (success == false) {
            json.put("userName", loginUser.getUsername());
        }
        this.sendResponseMessage(json.toJSONString());
        logger.debug("OrganizationsController::verifyUser --" + json.toJSONString());
        return null;
    }

    public String getKeys() {
        logger.debug("OrganizationsController::getKeys");
        List<OrgKeyVO> keys = orgService.getSecurityKeys(id);
        // since pager is removed at client side, always set page to 1 and rp to the number of keys
        // keep paging code here in case enabling it later
        page = 1;
        rp = keys.size();
        Pagination<OrgKeyVO> keyPage = new Pagination<OrgKeyVO>(keys.size(), page, rp);
        int startIndex = (page - 1) * rp > keys.size() ? keys.size() : (page - 1) * rp;
        int endIndex = page * rp > keys.size() ? keys.size() : page * rp;
        keyPage.addRow(keys.subList(startIndex, endIndex));
        sendResponseMessage(keyPage.toJsonString());
        logger.debug("OrganizationsController::getKeys -- " + keyPage.toJsonString());
        return null;
    }

    public String addKey() {
        LoginUser loginUser = getLoginUser();
        orgService.AddSecurityKey(id, hashAlgorithm, effectiveTime, validDays, loginUser.getUserId());
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String revokeKey() {
        LoginUser loginUser = getLoginUser();
        orgService.RevokeKey(id, loginUser.getUserId(), revokeReason);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String changeValidDays() {
        orgService.ChangeKeyValidDays(id, validDays);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return null;
    }

    public String getCurrentValidDays() {
        OrgKeyVO key = orgService.getSecurityKey(id);
        JSONObject json = new JSONObject();
        json.put("valid-days", key.getValidDays().toString());
        sendResponseMessage(json.toJSONString());
        return null;
    }
    // dependent service

    @Autowired
    public void setOrgService(IOrganizationsService orgService) {
        this.orgService = orgService;
    }
    private IOrganizationsService orgService;

    @Autowired
    public void setMailService(MailbatchService mailService) {
        this.mailService = mailService;
    }
    private MailbatchService mailService;

    @Autowired
    public void setNotificationItemService(NotificationItemService notificationItemService) {
        this.notificationItemService = notificationItemService;
    }
    private NotificationItemService notificationItemService;

    @Autowired
    public void setOrganizationDao(OrganizationDAO dao) {
        this.orgDao = dao;
    }
    private OrganizationDAO orgDao;
    // locus of request data
    private String name;

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }
    private String address;

    public void setAddress(String addr) {
        address = addr;
    }

    public String getAddress() {
        return address;
    }
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    private boolean enforceAPISecurity;

    public void setEnforceAPISecurity(boolean ef) {
        enforceAPISecurity = ef;
    }

    public boolean getEnforceAPISecurity() {
        return enforceAPISecurity;
    }
    private String firstName;

    public void setFirstName(String name) {
        firstName = name;
    }

    public String getFirstName() {
        return firstName;
    }
    private String lastName;

    public void setLastName(String name) {
        lastName = name;
    }

    public String getLastName() {
        return lastName;
    }
    private String email;

    public void setEmail(String e) {
        email = e;
    }

    public String getEmail() {
        return email;
    }
    private String userName;

    public void setUserName(String name) {
        userName = name;
    }

    public String getUserName() {
        return userName;
    }
    private String note;

    public void setNote(String n) {
        note = n;
    }

    public String getNote() {
        return note;
    }
    int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    int page;

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
    // this is page size!
    int rp;

    public void setRp(int rp) {
        this.rp = rp;
    }

    public int getRp() {
        return rp;
    }
    Date effectiveTime;

    public void setEffectiveTime(Date date) {
        this.effectiveTime = date;
    }

    public Date getEffectiveTime() {
        return this.effectiveTime;
    }
    int validDays;

    public void setValidDays(int days) {
        this.validDays = days;
    }

    public int getValidDays() {
        return this.validDays;
    }
    String hashAlgorithm;

    public void setHashAlgorithm(String algorithom) {
        this.hashAlgorithm = algorithom;
    }

    public String getHashAlogorithom() {
        return this.hashAlgorithm;
    }
    String revokeReason;

    public void setRevokeReason(String reason) {
        this.revokeReason = reason;
    }

    public String getRevokeReason() {
        return this.revokeReason;
    }
    String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
    private static String ATTR_SITE_ADMINISTRATOR = "siteAdmin";
    private static String ATTR_PRIMARY_OA = "primaryOA";
}
