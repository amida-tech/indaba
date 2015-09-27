/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.notif;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.notifimporter.ImpNotificationItem;
import com.ocs.indaba.controlpanel.notifimporter.NotificationItemExporter;
import com.ocs.indaba.controlpanel.notifimporter.NotificationItemImporter;
import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.NotificationItem;
import com.ocs.indaba.po.NotificationType;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.Tokenset;
import com.ocs.indaba.service.LanguageService;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.vo.NotificationItemView;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import com.opensymphony.xwork2.ModelDriven;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ResultPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ningshan
 */
@ResultPath("/WEB-INF/pages")
public class NotificationsController extends BaseController implements ModelDriven<Object> {    
    
    private static final Logger logger = Logger.getLogger(NotificationsController.class);
    
    private static final String PARAM_FILTER_LANGID = "selectlanguage";
    private static final String PARAM_FILTER_TYPEID = "selecttype";
    private static final String ATTR_NOTIFICATION_TYPES = "notificationtypes";
    private static final String ATTR_TOKENS = "tokens";
    private static final String PARAM_QTYPE ="qtype";
    private static final String PARAM_ID_LIST= "idList[]";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_BODY_TEXT = "notifibody";
    private static final String PARAM_LANGUAGE_ID = "languageId";
    private static final String PARAM_TYPE_ID = "typeId";
    private static final String PARAM_MODE = "mode"; 
    private static final String PARAM_SUBJECT = "subject";
    private static final String PARAM_NOTIFICATION_ID = "notifiId";
    
    @Autowired
    private LanguageService langSrvc;
    @Autowired
    private NotificationItemService notificationItemSrvc;
  
    
    public String index() {
        try {
            logger.debug("Enter index");
            printURLRequestQuery();
            LoginUser loginUser = getLoginUser();
/*
            if (loginUser.isSiteAdmin()) {
                request.setAttribute(ATTR_SITE_ADMINISTRATOR, true);
            } else {
                request.setAttribute(ATTR_SITE_ADMINISTRATOR, false);
            }
*/
            request.setAttribute(ATTR_LANGUAGES, langSrvc.getAllLanguages());
            request.setAttribute(ATTR_NOTIFICATION_TYPES, notificationItemSrvc.getNotificationTypes());
            request.setAttribute(ATTR_TOKENS, notificationItemSrvc.getTokensets());
        }
        finally {
            logger.debug("Exit index");
        }
        return RESULT_INDEX;
  }
    
    public String find() {
        
        try {
            logger.debug("Enter find");
            printURLRequestQuery();
            int filterLangId = StringUtils.str2int(request.getParameter(PARAM_FILTER_LANGID));
            int filterTypeId = StringUtils.str2int(request.getParameter(PARAM_FILTER_TYPEID));
            int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
            int pageNum = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
            String sortName = request.getParameter(PARAM_SORT_NAME);
            String sortOrder = request.getParameter(PARAM_SORT_ORDER);
            String queryType = request.getParameter(PARAM_QTYPE);
            String query = request.getParameter(PARAM_QUERY);
            if (sortName.equalsIgnoreCase("type"))
                sortName = "type_name";
            else if (sortName.equalsIgnoreCase("language"))
                sortName="language_name";
           //findProjectNotifs(int projectId, int filterLangId, int filterRoleId, int filterTypeId, String searchCol, String searchTerm, String sortCol, String sortOrder, int pageNum, int pageSize)
            Pagination<NotificationItemView> pagination = notificationItemSrvc.findNotificationItems(filterLangId, filterTypeId, queryType, query, sortName, sortOrder, pageNum, pageSize);

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
    public String delete() {
        try {
            if (logger.isDebugEnabled()) {
               logger.debug("Enter delete");
            }
            printURLRequestQuery();
            JSONObject retObj = new JSONObject();
            List<Integer> lstNotifications = StringUtils.strArr2IntList(request.getParameterValues(PARAM_ID_LIST));
            if (lstNotifications != null) {
                notificationItemSrvc.deleteNotificationItems(lstNotifications);
            }
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            super.sendResponseJson(retObj);
        }
        finally {
            logger.debug("Exit delete");
        }
        return RESULT_EMPTY; 
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
    
    public String get() {
        try {
            
            logger.debug("Enter get");
            printURLRequestQuery();
            int typeId = StringUtils.str2int(request.getParameter(PARAM_TYPE_ID));
            int langId = StringUtils.str2int(request.getParameter(PARAM_LANGUAGE_ID));
            NotificationItem item = notificationItemSrvc.getNotificationItemByTypeIdAndLanguageId(typeId, langId);
            if (item != null) {
                logger.debug(JSONUtils.toJsonString(item));
                JSONObject root = JSONUtils.parseJSONStr(JSONUtils.toJsonString(item));
                super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
            }
            else {
                logger.warn("no record found for type " + typeId + "  and language " + langId);
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.NOTIF_ERROR_NOT_FOUND));
            }
        }
        finally {
            logger.debug("Exit get");
        }
        return RESULT_EMPTY;
    }

    public String getById() {
        try {
            
            logger.debug("Enter getById");
            printURLRequestQuery();
            int notifId = StringUtils.str2int(request.getParameter(PARAM_NOTIFICATION_ID));
            List<Integer> list = new ArrayList<Integer>();
            list.add(notifId);
            List<NotificationItemView> views = notificationItemSrvc.findNotificationItemsByIds(list);
            if (views != null && views.size()==1) {
                logger.debug(JSONUtils.toJsonString(views.get(0)));
                JSONObject root = JSONUtils.parseJSONStr(JSONUtils.toJsonString(views.get(0)));
                super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
            }
            else {
                logger.warn("no record found for id " + notifId);
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.NOTIF_ERROR_NOT_FOUND));
            }
        }
        finally {
            logger.debug("Exit getById");
        }
        return RESULT_EMPTY;
    }
    
    public String update() {

        try {
            logger.debug("Enter update");
            printURLRequestQuery();
            String mode = request.getParameter(PARAM_MODE);
            int typeId = StringUtils.str2int(request.getParameter(PARAM_TYPE_ID));
            int langId = StringUtils.str2int(request.getParameter(PARAM_LANGUAGE_ID));
            int notifiId = StringUtils.str2int(request.getParameter(PARAM_NOTIFICATION_ID));

            NotificationItem oldItem = notificationItemSrvc.getNotificationItemByTypeIdAndLanguageId(typeId, langId);
            if (oldItem != null) {
                boolean bError = false;
                if (!("EDIT".equalsIgnoreCase(mode))) {
                    bError = true;
                }
                else  if (notifiId != oldItem.getId()) {
                    bError = true;
                }   
                if (bError) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.NOTIF_ERROR_CONFLICT, oldItem.getName()));
                    return RESULT_EMPTY;  
                }
            }
            NotificationItem notif = new NotificationItem();
            if (!mode.equalsIgnoreCase("EDIT")) {
                notif.setId(0);
            } else {
                
                NotificationItem notifv = notificationItemSrvc.getNotificationItem(notifiId);
                if (notifv == null) {
                    logger.error("no record found for id:" + notifiId);
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                            getMessage(ControlPanelMessages.NOTIF_ERROR_NOT_FOUND));
                    return RESULT_EMPTY;
                }
                notif=notifv;
            }
            notif.setNotificationTypeId(typeId);
            notif.setLanguageId(langId);
            
            //get Name
            String str = request.getParameter(PARAM_NAME);
            notif.setName((str == null)?"":str.trim());

            //get Subject
            str = request.getParameter(PARAM_SUBJECT);
            notif.setSubjectText((str==null)?"":str.trim());
            
            //get BodyText 
            str = request.getParameter(PARAM_BODY_TEXT);
            notif.setBodyText((str == null)?"":str.trim());
            try {
                if ("ADD".equalsIgnoreCase(mode)) 
                    notificationItemSrvc.addNotificationItem(notif);
                else
                    notificationItemSrvc.updateNotificationItem(notif);
            
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
            }
            catch(Exception e) {
                logger.error(e.getMessage());
                super.sendResponseResult(ControlPanelErrorCode.ERR_DB,
                    getMessage(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL));
            }
        }
        finally {
            logger.debug("Exit update");
        }
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
            logger.debug("Export notification items : idList=" + idList);
            int langId = StringUtils.str2int(request.getParameter(PARAM_LANG_ID));
            if (langId <= 0) {
                langId = ControlPanelConstants.DEFAULT_LANGUAGE_ID;
            }
            response.setContentType("");
            response.setHeader("Content-Disposition", "attachment; filename=NotificationItemExport.xls");
            
            OutputStream output = response.getOutputStream();
            NotificationItemExporter exporter = new NotificationItemExporter(notificationItemSrvc, idList, output);
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

    public String getTypeTokens() {
        
        JSONObject root = new JSONObject();
        
        try {
            logger.debug("\nEnter getTypeTokens");
            printURLRequestQuery();
            
            List<NotificationType> nTypes = notificationItemSrvc.getNotificationTypes();
            JSONArray typeArr = new JSONArray();
            root.put("ntypes", typeArr);
            if (nTypes != null && !nTypes.isEmpty()) {
                for (NotificationType nt : nTypes) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", nt.getId());
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
                logger.debug("\nExit getTypeTokens"+ root + "\n");
        }
    }
    public String validateCsv() {

        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.MUST_LOGIN);
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

            NotificationItemImporter importer = new NotificationItemImporter(loginUser, uploadFile);
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


    public String importNotifications(){

        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.MUST_LOGIN);
            return RESULT_EMPTY;
        }

        // uploadedFilename is the base name of the uploaded file in system's storage
        // The file is already stored there!
        String uploadedFilename = request.getParameter("csvFilename");
        File uploadFile = super.getUploadFileByFilename(uploadedFilename, ControlPanelConstants.UPLOAD_TYPE_NOTIF);

        NotificationItemImporter importer = new NotificationItemImporter(loginUser, uploadFile);
        importer.load();
        JSONObject result = importResultToJson(importer);

        super.sendResponseResult(ControlPanelErrorCode.OK, result, "OK");
        return RESULT_EMPTY;   

    }
    
    private JSONObject importResultToJson(NotificationItemImporter importer) {
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

        List<ImpNotificationItem> notifs = importer.getNotifs();
        root.put("count", (notifs == null) ? 0 : notifs.size());
        return root;
    }

    public Object getModel() {
        return null;
    }
    
}
