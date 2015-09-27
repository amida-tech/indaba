/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import com.ocs.common.Config;
import com.ocs.indaba.controlpanel.common.ControlPanelConfig;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.UploadFile;
import com.ocs.indaba.service.UploadFileService;
import com.ocs.indaba.util.FilePathUtil;
import java.io.File;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.drools.util.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class FileUploadController extends BaseController {

    private static final Logger logger = Logger.getLogger(FileUploadController.class);
    private static final String UPLOAD_BASEURL = Config.getString(ControlPanelConfig.KEY_STORAGE_UPLOAD_BASE);
    private static final String PROJECT_LOGO_DIR = Config.getString(ControlPanelConfig.KEY_STORAGE_DIR_PROJECTLOGO);
    private static final String SPONSOR_LOGO_DIR = Config.getString(ControlPanelConfig.KEY_STORAGE_DIR_SPONSORLOGO);
    
    private File upload = null;
    private String type = null;
    private String use = null;
    private String uploadFileName = null;


    @Autowired
    private UploadFileService uploadFileSrvc = null;

    public String index() {
        return RESULT_INDEX;
    }

    public String create() {
        LoginUser loginUser = getLoginUser();
        logger.debug("Upload File[type=" + type + ", use=" + use + "] [" + uploadFileName + "]: " + upload.getAbsolutePath());
        String filePath = null;
        
        if ("projectLogo".equalsIgnoreCase(use)) {
            filePath = PROJECT_LOGO_DIR;
        } else if ("sponsorLogo".equalsIgnoreCase(use)) {
            filePath = SPONSOR_LOGO_DIR;
        }

        if (filePath == null) {
            if ("IMAGE".equalsIgnoreCase(type)) {
                filePath = UPLOAD_BASEURL + "/images";
            } else {
                filePath = UPLOAD_BASEURL + "/files";
            }
        }

        String extension = FilePathUtil.getFileExtention(uploadFileName);
        String uuid = UUID.randomUUID().toString();
        String uniqueFilename = DigestUtils.md5Hex(uuid + System.currentTimeMillis()) +
                (StringUtils.isEmpty(extension) ? "" : "." + extension);
        File uploadFile = new File(filePath + "/" + uniqueFilename);
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }

        try {
            FileUtils.copyFile(upload, uploadFile);
            logger.debug("Saved upload file to :" + uploadFile.getAbsolutePath());
            UploadFile uploadFilePO = new UploadFile();
            uploadFilePO.setFileName(uniqueFilename);
            uploadFilePO.setDisplayName(uploadFileName);
            uploadFilePO.setSize(Long.valueOf(upload.length()).intValue());
            uploadFilePO.setFilePath(filePath);
            uploadFilePO.setType(extension);
            uploadFilePO.setUpdateTime(new Date());
            uploadFilePO.setUserId(loginUser.getUserId());
            uploadFilePO = uploadFileSrvc.addUploadFile(uploadFilePO);
            JSONObject data = new JSONObject();
            data.put("dname", uploadFileName);
            data.put("size", upload.length());
            data.put("id", uploadFilePO.getId());
            data.put("uname", uploadFilePO.getFileName());
            super.sendResponseResult(ControlPanelErrorCode.OK, data, "OK");
            upload.delete();
        } catch (Exception ex) {
            logger.error("Fail to save upload file: " + uploadFileName);
            JSONObject data = new JSONObject();
            data.put("dname", uploadFileName);
            data.put("size", upload.length());
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, data, "Fail to save upload file!");
        }
        return RESULT_EMPTY;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        logger.debug("Set upload ...");
        this.upload = upload;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        logger.debug("Set type: " + type);
        this.type = type;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        logger.debug("Set use: " + use);
        this.use = use;
    }
}
