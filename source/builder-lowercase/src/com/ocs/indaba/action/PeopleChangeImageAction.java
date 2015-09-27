/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.indaba.util.FilePathUtil;
import com.ocs.indaba.util.FileStorageUtil;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sjf
 */
public class PeopleChangeImageAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(PeopleChangeImageAction.class);
    private static final String PATH_OF_PEOPLEICONS = "peopleicons";
    private static final String FWD_PEOPLE_PROFILE = "profile";
    private static final String ATTR_USER = "user";
    private static final String ATTR_OPEN_CASE_LIST = "openCases";
    private static final String ATTR_ASSIGNED_TASKS = "assignedTasks";
    private static final String ATTR_ROLE_NAME = "roleName";
    private static final String KNOW_USER = "knowUser";
    
    private UserService userService;
    private CaseService caseService;
    private TaskService taskService = null;
    private ViewPermissionService viewPermisssionService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser= preprocess(mapping, request, response);
        String imgBasePath = Config.getString(Config.KEY_STORAGE_IMAGE_BASE);
        String peopleIconBasePath = imgBasePath + "/" + PATH_OF_PEOPLEICONS;

        Object maxLenExceeded = request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
        if (maxLenExceeded != null && ((Boolean) maxLenExceeded).booleanValue()) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_EXCEED_MAX_FILESIZE));
            return mapping.findForward(FWD_ERROR);
        }

        String filename = "";

        SingleUploadForm fileUploadForm = (SingleUploadForm) form;
        FormFile formFile = fileUploadForm.getFile();
        if (formFile != null) {
            try {
                filename = loginUser.getUid() + "." + FilePathUtil.getFileExtention(formFile.getFileName());
                String absoluteFileName = FilePathUtil.getPeopleIconPath(filename);
                FileStorageUtil.store(formFile.getInputStream(), absoluteFileName);
                logger.debug("Success to save uploaded file: " + absoluteFileName + ". [contentType=" + formFile.getContentType()
                        + ", fileName=" + formFile.getFileName() + ", fileSize=" + formFile.getFileSize() + ", filePath=" + peopleIconBasePath + "].");
            } catch (Exception ex) {
                logger.error("Fail to store uploaded file.", ex);
            }
        }
        if (!StringUtils.isEmpty(filename)) {
            User user = userService.getUser(loginUser.getUid());
            String oldPhoto = user.getPhoto();
            if(!StringUtils.isEmpty(oldPhoto) && !oldPhoto.equals(filename)) {
                File oldPhotoFile = new File(FilePathUtil.getPeopleIconPath(oldPhoto));
                if(oldPhotoFile.exists()) {
                    logger.info("Remove old existing people icon: " + oldPhotoFile.getAbsolutePath());
                    oldPhotoFile.delete();
                }
            }
            user.setPhoto(filename);
            userService.updateUser(user);
        }
        //set user basic info;
        request.setAttribute(ATTR_USER, userService.getUser(loginUser.getUid()));
        request.setAttribute(ATTR_ROLE_NAME, roleService.getRoleByProjectIdAndUserId(loginUser.getPrjid(), loginUser.getUid()));
        request.setAttribute(KNOW_USER, viewPermisssionService.getUserDisplayOfProject(loginUser.getPrjid(), Constants.DEFAULT_VIEW_MATRIX_ID, loginUser.getUid(), loginUser.getUid()));
        //set open case info;
        request.setAttribute(ATTR_OPEN_CASE_LIST,
                caseService.getOpenCasesByAssignUserId(loginUser.getUid()));
        //set user basic info;
        request.setAttribute(ATTR_ASSIGNED_TASKS, taskService.getAssignedTasksByUserId(loginUser.getUid(), loginUser.getPrjid()));

        return mapping.findForward(FWD_PEOPLE_PROFILE);
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermisssionService) {
        this.viewPermisssionService = viewPermisssionService;
    }
}
