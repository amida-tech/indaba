/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelConfig;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.StorageUtils;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.service.UserService;
import com.ocs.util.CookieUtils;
import com.ocs.util.FileUtils;
import com.ocs.util.StringUtils;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jiangjeff
 */
public abstract class BaseController extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String MIME_JSON = "text/x-json";
    public static final String MIME_HTML = "text/html";
    protected static final String RESULT_ADD = "add";
    protected static final String RESULT_CREATE = "create";
    protected static final String RESULT_EDIT = "edit";
    protected static final String RESULT_SUCCESS = "success";
    protected static final String RESULT_INDEX = "index";
    protected static final String RESULT_UPDATE = "update";
    protected static final String RESULT_EMPTY = null;
    protected static final String KEY_RET = "ret";
    protected static final String KEY_DESC = "desc";
    protected static final String KEY_DATA = "data";
    protected static final String KEY_UPLOAD_RESULT = "uploadRet";
    protected static final String PARAM_ID = "id";
    protected static final String PARAM_ACTION = "action";
    protected static final String PARAM_USER_ID = "userId";
    protected static final String PARAM_LANG_ID = "langId";
    protected static final String PARAM_PROJECT_ID = "projId";
    protected static final String PARAM_PRODUCT_ID = "prodId";
    protected static final String PARAM_TARGET_ID = "targetId";
    protected static final String PARAM_HORSE_IDS = "horseIds[]";
    protected static final String PARAM_TASK_ID = "taskId";
    protected static final String PARAM_SORT_NAME = "sortname";
    protected static final String PARAM_SORT_ORDER = "sortorder";
    protected static final String PARAM_QUERY_TYPE = "qtype";
    protected static final String PARAM_QUERY = "query";
    protected static final String PARAM_PAGINATION_PAGE = "page";
    protected static final String PARAM_PAGINATION_PAGESIZE = "rp";
    protected static final String PARAM_FIELD_ID = "fieldId";
    protected static final String PARAM_FIELD_NAME = "fieldName";
    protected static final String PARAM_FIELD_VALUE = "fieldValue";
    protected static final String PARAM_EXTRA_DATA = "extraData";
    protected static final String ATTR_USER = "user";
    protected static final String ATTR_LANGUAGES = "languages";
    protected static final String ATTR_IS_SITE_ADMIN = "isSA";
    protected static final String ATTR_IS_PROJECT_ADMIN = "isPA";
    protected static final String ATTR_IS_ORG_ADMIN = "isOA";
    protected static final String ATTR_ERROR_CODE = "errCode";
    protected static final String ATTR_ERROR_MESSAGE = "errMsg";
    protected HttpServletRequest request = null;
    protected HttpServletResponse response = null;
    @Autowired
    protected UserService userSrvc;

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public int getLanguageId() {
        int langId = StringUtils.str2int(CookieUtils.getCookie(request, Constants.COOKIE_LANGUAGE));

        if (langId <= 0) {
            langId = com.ocs.indaba.common.Constants.LANGUAGE_ID_EN;
            CookieUtils.setCookie(response, Constants.COOKIE_LANGUAGE, langId);
        }
        return langId;
    }

    public LoginUser getLoginUser() {
        return (LoginUser) request.getAttribute(ATTR_USER);
    }

    public Map<String, String> getParameters() {
        Map<String, String> params = new HashMap<String, String>();
        Enumeration e = request.getParameterNames();
        if (e != null) {
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                params.put(name, request.getParameter(name));
            }
        }
        return params;
    }

    protected void sendResponseMessage(String msg) {
        sendResponseMessage(msg, MIME_TEXT_PLAIN);
    }

    protected void sendResponseJson(JSONObject jsonObj) {
        sendResponseMessage(jsonObj.toJSONString(), MIME_TEXT_PLAIN);
    }

    protected void sendResponseJson(JSONArray jsonArr) {
        sendResponseMessage(jsonArr.toJSONString(), MIME_TEXT_PLAIN);
    }

    protected void sendResponseResult(int ret, JSONObject data, String desc) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_RET, ret);
        if (data != null) {
            jsonObj.put(KEY_DATA, data);
        }
        jsonObj.put(KEY_DESC, desc);
        sendResponseJson(jsonObj);
    }

    protected void sendResponseResult(int ret, boolean uploadRet, JSONObject data, String desc) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_RET, ret);
        if (data != null) {
            jsonObj.put(KEY_DATA, data);
        }
        jsonObj.put(KEY_UPLOAD_RESULT, uploadRet);
        jsonObj.put(KEY_DESC, desc);
        sendResponseJson(jsonObj);
    }

    protected void sendResponseResult(int ret, String desc) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_RET, ret);
        jsonObj.put(KEY_DESC, desc);
        sendResponseJson(jsonObj);
    }

    protected void sendResponseResult(int ret, boolean uploadRet, String desc) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_RET, ret);
        jsonObj.put(KEY_UPLOAD_RESULT, uploadRet);
        jsonObj.put(KEY_DESC, desc);
        sendResponseJson(jsonObj);
    }

    protected void sendResponseResult(int ret, JSONArray data, String desc) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_RET, ret);
        if (data != null) {
            jsonObj.put(KEY_DATA, data);
        }
        jsonObj.put(KEY_DESC, desc);
        sendResponseJson(jsonObj);
    }

    protected void sendResponseMessage(String msg, String mimeType) {
        response.setContentType(mimeType);
        //response.setContentLength(msg.length());
        response.setCharacterEncoding("UTF-8");
        Writer writer = null;
        try {
            writer = response.getWriter();
            writer.write(msg);
            writer.flush();
        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

    protected String getUploadFilePath(String origFilename, String uploadType) {
        return StorageUtils.getUploadFilePath(origFilename, uploadType);
    }

    protected String getUploadFilePathOfFilename(String filename, String uploadType) {
        return StorageUtils.getUploadFilePathOfFilename(filename, uploadType);
    }

    protected File getUploadFile(String origFilename, String uploadType) {
        return StorageUtils.getUploadFile(origFilename, uploadType);
    }

    protected File getUploadFileByFilename(String filename, String uploadType) {
        return StorageUtils.getUploadFileByFilename(filename, uploadType);
    }

    protected String generateUniqueFilename(String ext) {
        return StorageUtils.generateUniqueFilename(ext);
    }

    protected String getMessage(String key) {
        LoginUser user = getLoginUser();

        if (user == null) {
            return Messages.getInstance().getMessage(key, ControlPanelConstants.DEFAULT_LANGUAGE_ID);
        } else {
            return user.message(key);
        }
    }

    protected String getMessage(String key, Object... args) {
        LoginUser user = getLoginUser();

        if (user == null) {
            return Messages.getInstance().getMessage(key, ControlPanelConstants.DEFAULT_LANGUAGE_ID);
        } else {
            return user.message(key, args);
        }
    }
}
