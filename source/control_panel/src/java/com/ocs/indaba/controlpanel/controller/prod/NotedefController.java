/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.controller.prod;

import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.po.Notedef;
import com.ocs.indaba.po.NotedefRole;
import com.ocs.indaba.po.NotedefUser;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.CommPanelDefService;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.vo.NotedefActionResult;
import com.ocs.indaba.vo.NotedefRoleView;
import com.ocs.indaba.vo.NotedefUserView;
import com.ocs.indaba.vo.NotedefView;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ningshan
 */
@ResultPath("/WEB-INF/pages")
@Results({
   @Result(name = "index", location = "product.jsp")})
public class NotedefController extends BaseController{
    
    private static final Logger logger = Logger.getLogger(NotedefController.class);
    private static final long serialVersionUID = -1L;
    
    private static final String PARAM_NOTEDEF_ID ="notedefId";
    private static final String PARAM_NT_NAME="nt-name";
    private static final String PARAM_NT_PROD_ID="nt-prodId";
    private static final String PARAM_NT_PROJ_ID="nt-projId";
    private static final String PARAM_NT_WEIGHT="nt-weight";
    private static final String PARAM_NT_MODE="nt-mode";
    private static final String PARAM_NT_DESC="nt-desc";
    private static final String PARAM_NT_USERS="users";
    private static final String PARAM_NT_ROLES="roles";
    private static final String PARAM_JO_ID = "id";
    private static final String PARAM_JO_NAME = "name";
    private static final String PARAM_JO_PERMISSION = "permission";
    
    @Autowired
    private CommPanelDefService commPanelDefService;
    
    @Autowired
    private ProjectService projectService;
    
    public String find() {
        
        try {
            logger.debug("Enter find");
            
            int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
            int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
            int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
            String sortName = request.getParameter(PARAM_SORT_NAME);
            String sortOrder = request.getParameter(PARAM_SORT_ORDER);
            //LoginUser loginUser = super.getLoginUser();
            //int userId = (loginUser == null) ? 1 : loginUser.getUserId();
            //boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();
            // TODO: check user access permission.
            printURLRequestQuery();
        
            sortOrder = "asc";
            sortName = "weight";
            
            try {
                Pagination<NotedefView> pagination = commPanelDefService.getNotedefViewsByProductId(prodId, sortName, sortOrder, page, pageSize);
            
                pagination.addProperty(PARAM_PRODUCT_ID, prodId);
                
                JSONObject root = new JSONObject();
                root.put("total", pagination.getTotal());
                root.put("offset", (pagination.getPage() - 1) * pagination.getPageSize());
                root.put("page", pagination.getPage());
                root.put("pageSize", pagination.getPageSize());
                root.put(PARAM_PRODUCT_ID, prodId);

                JSONArray jsonArr = new JSONArray();
                if (pagination.getRows() != null && !pagination.getRows().isEmpty()) {
                    for (NotedefView item : pagination.getRows()) {
                        JSONObject oj = new JSONObject();
                        oj.put("notedef_id", item.getId());
                        oj.put("name", item.getName());
                        oj.put("weight", item.getWeight());
                        oj.put("description", item.getDescription());
                        oj.put("productId", item.getProductId());
                        oj.put("enabled", item.getEnabled());

                        String users = "";
                        if (item.getUsers() != null && !item.getUsers().isEmpty()) {
                            for (NotedefUserView u : item.getUsers()) {
                                if (users.length()!=0) users += ", ";
                                users += u.getUserName();
                            }
                        }
                        oj.put("users", users);
                        String roles = "";
                        if (item.getRoles() != null && !item.getRoles().isEmpty()) {
                            for (NotedefRoleView r : item.getRoles()) {
                                if (roles.length()!=0) roles += ", ";
                                roles += r.getRoleName();
                            }
                        }
                        oj.put("roles", roles);
                        jsonArr.add(oj);
                    }
                }
                root.put("rows", jsonArr);
                
                logger.debug(root.toString());
                sendResponseMessage(root.toString());
            }  
            catch (Exception ex) {
                logger.error("Error occurs!", ex);
            }

            return RESULT_EMPTY;
        }
        finally {
            logger.debug("Exit find");
        }
        
    }

    public String get() {
        
        JSONObject retObj = new JSONObject();
        try {
            logger.debug("Enter get");
            printURLRequestQuery();
            int notedefId = StringUtils.str2int(request.getParameter(PARAM_NOTEDEF_ID));
            NotedefView nv = commPanelDefService.getNotedefViewsByNotedefID(notedefId);
            
            if (nv == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.NOTIF_ERROR_NOT_FOUND));
            }
            else {
                JSONObject oj = new JSONObject();
                oj.put("notedef_id", nv.getId());
                oj.put("name", nv.getName());
                oj.put("weight", nv.getWeight());
                oj.put("description", nv.getDescription());
                oj.put("productId", nv.getProductId());
                oj.put("enabled", nv.getEnabled());
                
                JSONArray users = new JSONArray();
                if (nv.getUsers() != null && !nv.getUsers().isEmpty()) {
                    for (NotedefUserView u : nv.getUsers()) {
                        JSONObject o = new JSONObject();
                        o.put("userId", u.getUserId());
                        o.put("name", u.getUserName());
                        o.put("permission", u.getPermissions());
                        users.add(o);
                    }
                }
                oj.put("users", users);
                
                JSONArray roles = new JSONArray();
                if (nv.getRoles() != null && !nv.getRoles().isEmpty()) {
                    for (NotedefRoleView r : nv.getRoles()) {
                        JSONObject o = new JSONObject();
                        o.put("roleId",r.getRoleId());
                        o.put("name", r.getRoleName());
                        o.put("permission",r.getPermissions());
                        roles.add(o);
                    }
                }
                oj.put("roles", roles);
                super.sendResponseResult(ControlPanelErrorCode.OK, oj, "OK");
             }
        }
        finally {
            logger.debug("Exit get");
        }
        return RESULT_EMPTY;
    }

   
    public String delete() {        
        try {
            logger.debug("Enter delete");
            printURLRequestQuery();
            int notedefId = StringUtils.str2int(request.getParameter(PARAM_NOTEDEF_ID));
            JSONObject retObj = new JSONObject();
            commPanelDefService.deleteNotedef(notedefId);
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            sendResponseJson(retObj);
        }
        finally {
            logger.debug("Exit delete");
        }
        return RESULT_EMPTY;
    }


    public String disable() {
        try {
            logger.debug("Enter disable");
            printURLRequestQuery();
            int notedefId = StringUtils.str2int(request.getParameter(PARAM_NOTEDEF_ID));
            JSONObject retObj = new JSONObject();
            commPanelDefService.disableNotedef(notedefId);
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            sendResponseJson(retObj);
        }
        finally {
            logger.debug("Exit disable");
        }
        return RESULT_EMPTY;
    }


    public String enable() {
        try {
            logger.debug("Enter enable");
            printURLRequestQuery();
            int notedefId = StringUtils.str2int(request.getParameter(PARAM_NOTEDEF_ID));
            JSONObject retObj = new JSONObject();
            commPanelDefService.enableNotedef(notedefId);
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            sendResponseJson(retObj);
        }
        finally {
            logger.debug("Exit enable");
        }
        return RESULT_EMPTY;
    }


    
    public String index() {
        try {
            logger.debug("Enter index");
            return RESULT_INDEX;
        }
        finally {
            logger.debug("End index");
        }
    }
    public String execute() {
        try {
            logger.debug("Enter execute");
            return index();
        }
        finally {
            logger.debug("Exit execute");
        }
    }

    public String getUsersRolesForNotedef() {
        try {
            logger.debug("Enter getUserRolesForNotedef");
            printURLRequestQuery();
            int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
            List<User> users = projectService.getUsersByProjectId(projId);
            JSONObject root = new JSONObject();
            JSONArray us = new JSONArray();
            root.put("Users", us);
            if (users != null && !users.isEmpty()) {
               for (User user : users) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(PARAM_JO_ID, user.getId());
                    jsonObj.put(PARAM_JO_NAME,user.getFirstName()+" " +user.getLastName());
                    us.add(jsonObj);
                }
            }
            logger.debug(us);
            List<Role> roles = projectService.getRolesByProjectId(projId);
            JSONArray rs = new JSONArray();
            root.put("Roles", rs);
            if (users != null && !users.isEmpty()) {
               for (Role role : roles) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(PARAM_JO_ID, role.getId());
                    jsonObj.put(PARAM_JO_NAME,role.getName());
                    rs.add(jsonObj);
                }
            }
            logger.debug(rs);
            
            
            super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
        }
        finally {
            logger.debug("Exit getUserRolesForNotedef");
        }
        return RESULT_EMPTY;
    }
    
    public String save() {
        
	JSONObject retObj = new JSONObject();
        try {
            logger.debug("Enter save");
            printURLRequestQuery();
            
            String name = request.getParameter(PARAM_NT_NAME);
            int notedefId = StringUtils.str2int(request.getParameter(PARAM_NOTEDEF_ID));
            int prodId = StringUtils.str2int(request.getParameter(PARAM_NT_PROD_ID));
            int projId = StringUtils.str2int(request.getParameter(PARAM_NT_PROJ_ID));
            int weight = StringUtils.str2int(request.getParameter(PARAM_NT_WEIGHT));
            String mode = request.getParameter(PARAM_NT_MODE);
            String desc = request.getParameter(PARAM_NT_DESC);
            String usersJson = request.getParameter(PARAM_NT_USERS);
            String rolesJson = request.getParameter(PARAM_NT_ROLES);
            if ("add".equalsIgnoreCase(mode)) {
                notedefId = -1;
            }
            JSONParser jsonParser = new JSONParser();
            List<NotedefUser> users = new ArrayList<NotedefUser>();
            if (!StringUtils.isEmpty(usersJson)) {
                try {
                    JSONArray arr = (JSONArray) jsonParser.parse(usersJson);
                    int size = arr.size();
                    for (int i = 0; i!= size; i++) {
                        JSONObject jo = (JSONObject)arr.get(i);
                        NotedefUser user = new NotedefUser();
                        user.setUserId(StringUtils.str2int((String)jo.get(PARAM_JO_ID)));
                        user.setPermissions(((Long)jo.get(PARAM_JO_PERMISSION)).intValue());
                        users.add(user);
                    }
                } catch (ParseException ex) {
                    logger.error("Fail to parse JSON string: " + usersJson, ex);
                    super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                                    getMessage(ControlPanelMessages.PROGRAM_ERROR));
                    return RESULT_EMPTY;
                }
            }
            
            List<NotedefRole> roles = new ArrayList<NotedefRole>();
            if (!StringUtils.isEmpty(rolesJson)) {
                try {
                    JSONArray arr = (JSONArray) jsonParser.parse(rolesJson);
                    int size = arr.size();
                    for (int i = 0; i!= size; i++) {
                        JSONObject jo = (JSONObject)arr.get(i);
                        NotedefRole role = new NotedefRole();
                        role.setRoleId(StringUtils.str2int((String)jo.get(PARAM_JO_ID)));
                        role.setPermissions(((Long)jo.get(PARAM_JO_PERMISSION)).intValue());
                        roles.add(role);
                    }
                } catch (ParseException ex) {
                    logger.error("Fail to parse JSON string: " + rolesJson, ex);
                    super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                                    getMessage(ControlPanelMessages.PROGRAM_ERROR));
                    return RESULT_EMPTY;
                }
            }
            Notedef notedef = new Notedef();
            notedef.setDescription(desc);
            notedef.setName(name);
            notedef.setWeight(weight);
            notedef.setProductId(prodId);
            notedef.setId(notedefId);
            notedef.setEnabled(Boolean.TRUE);
                retObj.put(KEY_RET, ControlPanelErrorCode.OK);	
                retObj.put(KEY_DESC, "OK");
            logger.debug("Notedef : "+ notedef.toString());
            logger.debug("Users : " + users);
            logger.debug("Roles : " + roles);
            
            NotedefActionResult rs = commPanelDefService.save(notedef, users, roles);
            if (rs.getCode() == NotedefActionResult.RESULT_CODE_OK) {
                retObj.put(KEY_RET, ControlPanelErrorCode.OK);	
                retObj.put(KEY_DESC, "OK");
            }
            else {
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_UNKNOWN);
                retObj.put(KEY_DESC, rs.getErrMessage());
            }
            
        }
        finally {
            logger.debug("Exit save");
        }
        super.sendResponseJson(retObj);
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
    
}
