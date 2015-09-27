/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.controller.proj;

import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.notifimporter.ImpProjectNotif;
import com.ocs.indaba.controlpanel.notifimporter.ProjectNotifExporter;
import com.ocs.indaba.controlpanel.notifimporter.ProjectNotifImporter;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.NotificationType;
import com.ocs.indaba.po.ProjectNotif;
import com.ocs.indaba.po.Tokenset;
import com.ocs.indaba.service.RoleService;
import com.ocs.indaba.service.LanguageService;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.vo.ProjectNotifSaveResult;
import com.ocs.indaba.vo.ProjectNotifView;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ResultPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Notification Controller
 * @author ningshan
 */

@ResultPath("/WEB-INF/pages")
public class NotificationController extends BaseController {
    
    private static final long serialVersionUID = 7850588769978800001L;
    private static final Logger logger = Logger.getLogger(NotificationController.class);
    private static final String PARAM_NAME = "name";
    private static final String PARAM_BODY_TEXT = "body";
    private static final String PARAM_ROLE_IDS = "roleIds[]";
    private static final String PARAM_DESCRIPTION = "desc";
    private static final String PARAM_LANGUAGE_ID = "languageId";
    private static final String PARAM_TYPE_ID = "typeId";
    private static final String PARAM_MODE = "mode"; 
    private static final String PARAM_SUBJECT = "subject";
    private static final String PARAM_FILTER_LANGID = "filterLanguageId";
    private static final String PARAM_FILTER_ROLEID = "filterRoleId";
    private static final String PARAM_FILTER_TYPEID = "filterTypeId";
    private static final String PARAM_QTYPE ="qtype";
    private static final String PARAM_NOTIFICATION_ID = "notifiId";
    private static final String PARAM_ID_LIST= "idList[]";
    @Autowired
    private RoleService roleSrvc;
    @Autowired
    private LanguageService languageSrvc;
    @Autowired
    private NotificationItemService notificationItemSrvc;
    
    public String find() {
        
        try {
            
            logger.debug("Enter find");
            printURLRequestQuery();
            
            int projectId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
            int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
            int pageNum = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
            String sortName = request.getParameter(PARAM_SORT_NAME);
            String sortOrder = request.getParameter(PARAM_SORT_ORDER);
            int filterLangId = StringUtils.str2int(request.getParameter(PARAM_FILTER_LANGID));
            int filterRoleId = StringUtils.str2int(request.getParameter(PARAM_FILTER_ROLEID));
            int filterTypeId = StringUtils.str2int(request.getParameter(PARAM_FILTER_TYPEID));
            String queryType = request.getParameter(PARAM_QTYPE);
            String query = request.getParameter(PARAM_QUERY);
            if (sortName.equalsIgnoreCase("type"))
                sortName = "type_name";
            else if (sortName.equalsIgnoreCase("language"))
                sortName="lang_name";
           //findProjectNotifs(int projectId, int filterLangId, int filterRoleId, int filterTypeId, String searchCol, String searchTerm, String sortCol, String sortOrder, int pageNum, int pageSize)
            Pagination<ProjectNotifView> pagination = notificationItemSrvc.findProjectNotifs(projectId, 
                            filterLangId, filterRoleId, filterTypeId, queryType, query, sortName, sortOrder, pageNum, pageSize);

            logger.debug(pagination.toJsonString());
            sendResponseMessage(pagination.toJsonString());
            
        }
        catch(Exception ex) {
            logger.error("Error occurs!", ex);
        }
        finally {
            logger.debug("Exit find");
        }
        return RESULT_EMPTY;
                
    }

    public String get() {
        
        try {
            logger.debug("Enter get");
            printURLRequestQuery();
            int notifiId = StringUtils.str2int(request.getParameter(PARAM_NOTIFICATION_ID));
            ProjectNotifView vo = notificationItemSrvc.getProjectNotifView(notifiId);
            if (vo != null) {
                logger.debug(JSONUtils.toJsonString(vo));
                JSONObject root = JSONUtils.parseJSONStr(JSONUtils.toJsonString(vo));
                super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
            }
            else {
                logger.warn("no record found for id:" + notifiId);
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.NOTIF_ERROR_NOT_FOUND));
            }
        }
        finally {
            logger.debug("Exit get");
        }
        return RESULT_EMPTY;
        
    }
    
    public String save() {

        try {
            logger.debug("Enter save");
            printURLRequestQuery();
            List<Integer> roles;
            
            String mode = request.getParameter(PARAM_MODE);
            String[] strs = request.getParameterValues(PARAM_ROLE_IDS);
            roles = StringUtils.strArr2IntList(strs);

            //get type ID
            int typeId = StringUtils.str2int(request.getParameter(PARAM_TYPE_ID));

            //get Language ID
            int langId = StringUtils.str2int(request.getParameter(PARAM_LANGUAGE_ID));
            int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));

            ProjectNotif notif;
            if (!mode.equalsIgnoreCase("EDIT")) {
                notif = new ProjectNotif();
                notif.setId(0);
            } else {
                int notifiId = StringUtils.str2int(request.getParameter(PARAM_NOTIFICATION_ID));
                ProjectNotifView notifv = notificationItemSrvc.getProjectNotifView(notifiId);
                if (notifv == null) {
                    logger.error("no record found for id:" + notifiId);
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                            getMessage(ControlPanelMessages.NOTIF_ERROR_NOT_FOUND));
                    return RESULT_EMPTY;
                }
                notif = notifv;
            }
            notif.setNotificationTypeId(typeId);
            notif.setLanguageId(langId);
            notif.setProjectId(projId);
            
            //get Name
            String str = request.getParameter(PARAM_NAME);
            notif.setName((str == null)?"":str.trim());

            //get Description
            str = request.getParameter(PARAM_DESCRIPTION);
            notif.setDescription((str != null)?str.trim():"");

            //get Subject
            str = request.getParameter(PARAM_SUBJECT);
            notif.setSubjectText((str==null)?"":str.trim());
            
            //get BodyText 
            str = request.getParameter(PARAM_BODY_TEXT);
            notif.setBodyText((str == null)?"":str.trim());
            
            ProjectNotifSaveResult result = notificationItemSrvc.saveProjectNotif(notif, roles);

            switch (result.getResultCode()) {
                case ProjectNotifSaveResult.RESULT_CODE_CONFLICT:
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.NOTIF_ERROR_CONFLICT, result.getProjectNotif().getName()));
                    break;

                case ProjectNotifSaveResult.RESULT_CODE_OK:
                    super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
                    break;

                default:
                    super.sendResponseResult(ControlPanelErrorCode.ERR_DB,
                        getMessage(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL));
            }
        }
        finally {
            logger.debug("Exit save");
        }
        return RESULT_EMPTY;      
    }
    
    public String groupdelete() {
        try {
            if (logger.isDebugEnabled()) {
               logger.debug("Enter delete");
            }
            printURLRequestQuery();
            JSONObject retObj = new JSONObject();
            List<Integer> lstNotifications = StringUtils.strArr2IntList(request.getParameterValues(PARAM_ID_LIST));
            if (lstNotifications != null) {
                notificationItemSrvc.deleteProjectNotif(lstNotifications);
            }
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            super.sendResponseJson(retObj);
        }
        finally {
            logger.debug("Exit delete");
        }
        return RESULT_EMPTY; 
    }
   
    public String delete() {
        try {
            if (logger.isDebugEnabled()) {
               logger.debug("Enter delete");
            }
            printURLRequestQuery();
            JSONObject retObj = new JSONObject();
            int notifiId = StringUtils.str2int(request.getParameter(PARAM_NOTIFICATION_ID));
            List<Integer> lstNotifications = new ArrayList<Integer>(notifiId);
            lstNotifications.add(notifiId);
            notificationItemSrvc.deleteProjectNotif(lstNotifications);
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            super.sendResponseJson(retObj);
        }
        finally {
               logger.debug("Exit delete");
        }
        return RESULT_EMPTY; 
    }



    private JSONObject importResultToJson(ProjectNotifImporter importer) {
        JSONObject root = new JSONObject();
        root.put("errCount", importer.getErrorCount());
        if (importer.getErrorCount() > 0) {
            JSONArray errorArray = new JSONArray();
            List<String> errors = importer.getErrors();
            for (String err : errors) {
                errorArray.add(err);
            }
            root.put("errors", errorArray);
        }

        List<ImpProjectNotif> notifs = importer.getNotifs();
        root.put("count", (notifs == null) ? 0 : notifs.size());
        return root;
    }

    
    public String validateCsv() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.MUST_LOGIN);
            return RESULT_EMPTY;
        }

        int projectId = StringUtils.str2int(request.getParameter("projId"));
        if (projectId <= 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL);
            return RESULT_EMPTY;
        }

        boolean valid = false;
        String filename = request.getParameter("filename"); // the filename is the name from the user
        File uploadFile = super.getUploadFile(filename, ControlPanelConstants.UPLOAD_TYPE_NOTIF);

        try {
            // now uploadFile is at the system's storage folder!
            FileUtils.copyInputStreamToFile(request.getInputStream(), uploadFile);
            if (loginUser == null || loginUser.getUser() == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.MUST_LOGIN);
                return RESULT_EMPTY;
            }
            
            logger.debug("Receive uploaded file: " + filename + "(size=" + uploadFile.length() + ", user=" + loginUser.getUserId() + ").");

            ProjectNotifImporter importer = new ProjectNotifImporter(loginUser, uploadFile, projectId);
            importer.validate();           
            JSONObject result = importResultToJson(importer);
            if (importer.getErrorCount() == 0) {
                valid = true;

                // The file name of uploadFile is the base name of the file in the system's storage
                // We send it to the client to remember it. It will be sent back to us when user clicks import
                result.put("uploadedFilename", uploadFile.getName());
            }
            super.sendResponseResult(ControlPanelErrorCode.OK, true, result, "OK");
        } catch (Exception ex) {
            logger.error("Fail to validate indicator import file." + uploadFile, ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, (uploadFile != null), "Error: " + ex);
        } finally {
            if (!valid) { // delete invalid file
                //uploadFile.delete();
            }
        }
        return RESULT_EMPTY;
    }


    public String importNotifs(){
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.MUST_LOGIN);
            return RESULT_EMPTY;
        }

        int projectId = StringUtils.str2int(request.getParameter("projId"));
        if (projectId <= 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL);
            return RESULT_EMPTY;
        }

        // uploadedFilename is the base name of the uploaded file in system's storage
        // The file is already stored there!
        String uploadedFilename = request.getParameter("csvFilename");
        File uploadFile = super.getUploadFileByFilename(uploadedFilename, ControlPanelConstants.UPLOAD_TYPE_NOTIF);

        ProjectNotifImporter importer = new ProjectNotifImporter(loginUser, uploadFile, projectId);
        importer.load();
        JSONObject result = importResultToJson(importer);

        super.sendResponseResult(ControlPanelErrorCode.OK, result, "OK");
        return RESULT_EMPTY;        
    }
    
    
    public String export() {
       LoginUser loginUser = super.getLoginUser();
       try {
            logger.debug("Enter export");
            printURLRequestQuery();
            
            if (loginUser == null || loginUser.getUser() == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.MUST_LOGIN);
                return RESULT_EMPTY;
            }

            String[] idArr = request.getParameterValues("idList");
            List<Integer> idList = new ArrayList<Integer>();
            if (idArr != null && idArr.length !=0 ) {
                for (String idStr : idArr) {
                    int id = StringUtils.str2int(idStr);
                    if (id > 0) {
                        idList.add(id);
                    }
                }
            }
            if (idList.size() == 0) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.NOTIF_EXPORT_NO_SELECTION));
                return RESULT_EMPTY;
            }
            logger.debug("Export indicators: idList=" + idList);
            int langId = StringUtils.str2int(request.getParameter(PARAM_LANG_ID));
            if (langId <= 0) {
                langId = ControlPanelConstants.DEFAULT_LANGUAGE_ID;
            }
            String forTrans = request.getParameter("forTrans");
            response.setContentType("");
            response.setHeader("Content-Disposition", "attachment; filename=NotificationExport.xls");
            
            OutputStream output = response.getOutputStream();
            ProjectNotifExporter exporter = new ProjectNotifExporter(notificationItemSrvc, idList, output);
            exporter.export();
       }
       catch(Exception ex) {
            logger.error("Error occurs!",ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.NOTIF_EXPORT_INTERNAL_ERROR));
       }
       finally {
           logger.debug("Exit export");
       }
       
       return RESULT_EMPTY;
    }
    
    public String hasNotification() {
        try {
            logger.debug("Enter hasNotification");
            printURLRequestQuery();
            
            JSONObject retObj = new JSONObject();
            int notifiId = StringUtils.str2int(request.getParameter(PARAM_NOTIFICATION_ID));
            List<Integer> projectNotifIds = new ArrayList<Integer>();
            projectNotifIds.add(notifiId);
            List<ProjectNotifView> lst = notificationItemSrvc.findProjectNotifs(projectNotifIds);
            if (lst == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, "no record exist");
            }
            else
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
            return RESULT_EMPTY;
            
        }
        finally {
            logger.debug("Exit hasNotification");
        }
    }
    public String getAllOptions() {
        
        JSONObject root = new JSONObject();
        
        try {
            logger.debug("\nEnter getAllOptions");
            printURLRequestQuery();
            int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
            
            
            List<Language> languages = languageSrvc.getAllLanguages();
            JSONArray langArr = new JSONArray();
            root.put("languages", langArr);
            if (languages != null && !languages.isEmpty()) {
               for (Language l : languages) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", l.getId());
                    jsonObj.put("name",l.getLanguageDesc() );
                    langArr.add(jsonObj);
                }
            }

            List<Role> roles = roleSrvc.getAllRoles(projId);
            JSONArray roleArr = new JSONArray();
            root.put("roles", roleArr);
            if (roles != null && !roles.isEmpty()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", 0);
                jsonObj.put("name", "DEFAULT");
                roleArr.add(jsonObj);
                for (Role r : roles) {
                    jsonObj = new JSONObject();
                    jsonObj.put("id", r.getId());
                    jsonObj.put("name", r.getName());
                    roleArr.add(jsonObj);
                }
            }
            
            List<NotificationType> nTypes = notificationItemSrvc.getProjectCustomizableNotificationTypes();
            JSONArray typeArr = new JSONArray();
            root.put("ntypes", typeArr);
            if (nTypes != null && !nTypes.isEmpty()) {
                for (NotificationType nt : nTypes) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", nt.getId());
                    jsonObj.put("name", nt.getName());
                    jsonObj.put("category", nt.getCategory());
                    typeArr.add(jsonObj);
                }
            }
            List<Tokenset> tklist = notificationItemSrvc.getTokensets();
            JSONArray categoryArr = new JSONArray();
            root.put("categories", categoryArr);
            if (tklist != null && !tklist.isEmpty()) {
                for (Tokenset tk : tklist) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", tk.getId());
                    jsonObj.put("tokens", tk.getTokens());
                    jsonObj.put("category", tk.getCatgeory());
                    categoryArr.add(jsonObj);
                }
            }
            super.sendResponseJson(root);

            return RESULT_EMPTY;
        }
        finally {
                logger.debug("\nExit getAllOptions"+ root + "\n");
        }
    }
    
    private void printURLRequestQuery() {
        if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
            Enumeration enums = request.getParameterNames();
            while (enums.hasMoreElements()){
                StringBuffer printStr = new StringBuffer();
                String name = (String)enums.nextElement();
                String[] values = request.getParameterValues(name);
                printStr.append(name).append(" :");
                for (String v : values)
                    printStr.append(" ").append(v);
                if (logger.isTraceEnabled())
                    logger.trace(printStr.toString());
                else
                    logger.debug(printStr.toString());
            }
        }
    }
}
