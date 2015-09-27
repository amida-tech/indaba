/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.User;
import com.ocs.util.SendMail;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
/**
 *
 * @author Rick Qiu, qiudejun@gmail.com
 */
public class UserController extends BaseController{
    
    public String userNameExists()
    {
        logger.debug("UserController::userNameExists -- user name: " + userName + ", email: " + email);
        JSONObject obj = new JSONObject();
        LoginUser loginUser = getLoginUser();
        if (userName == null || email == null || !SendMail.ValidateAddress(email)) {
            obj.put(KEY_RET, ControlPanelErrorCode.ERR_INVALID_PARAM);
            obj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_INVALID_PARAMETER));
        }
        else {
            User user = userSrvc.getUserByEmail(email);
            if (user == null) {
                obj.put(KEY_USER_NAME, userName);
                obj.put(KEY_NEW_USER, true);
                if (userSrvc.getUser(userName) == null) {   
                    obj.put(KEY_USER_NAME_USED, false);
                } else {
                    obj.put(KEY_USER_NAME_USED, true);
                    obj.put(KEY_USER_NAME_SUGGEST, userSrvc.suggestUserName(userName));
                }                
            }
            else {
                obj.put(KEY_RET, ControlPanelErrorCode.OK);
                obj.put(KEY_USER_NAME, userName);
                obj.put(KEY_NEW_USER, false);
            }
        }

        logger.debug("UserController::userNameExists -- returned json string: " + obj.toJSONString());
        sendResponseMessage(obj.toJSONString());
        return null;
    }
    
    public String addUser() {        
        User u = userSrvc.createUser(firstName, lastName, email, userName);
        boolean result = (u == null)? false: true;
        JSONObject obj = new JSONObject();
        obj.put("success", result);
        sendResponseMessage(obj.toJSONString());
        return null;
    }
    
    public String bulkAddUser() {
        return null;
    }
    
    public String verifyUser() {
        logger.debug("UserController::verifyUser");
        LoginUser loginUser = getLoginUser();
        boolean success = this.userSrvc.authenticate(loginUser.getUsername(), password);
        JSONObject json = new JSONObject();
        json.put("success", success);
        if (success == false) {
            json.put("userName", loginUser.getUsername());
        }
        this.sendResponseMessage(json.toJSONString());
        logger.debug("UserController::verifyUser --" + json.toJSONString());
        return null;
    }
    
    private static final Logger logger = Logger.getLogger(UserController.class);
    // keys for returned JSON data
    private static String KEY_USER_NAME     = "username";
    private static String KEY_NEW_USER      = "newuser";
    private static String KEY_USER_NAME_USED = "nameused";
    private static String KEY_USER_NAME_SUGGEST = "suggest";
    // locus of request data
    private String email;
    public void setEmail(String email) {
        this.email = email;
    }
    private String userName;
    public void setUserName(String userName) {
        this.userName = (userName == null) ? null : userName.trim();
    }
    private String password;
    public void setPassword(String password) {
        this.password = password;
    }
    private String firstName;
    public void setFirstName(String firstName) {
        this.firstName = (firstName == null) ? null : firstName.trim();
    }
    private String lastName;
    public void setLastName(String lastName) {
        this.lastName = (lastName == null) ? null : lastName.trim();
    }
}
