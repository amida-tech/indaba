/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.common;

import com.ocs.common.Config;
import com.ocs.util.FileUtils;
import java.io.File;
import java.text.MessageFormat;
import java.util.UUID;

/**
 *
 * @author yc06x
 */
public class StorageUtils {

    static public String getUploadFilePathOfFilename(String filename, String uploadType) {
        String basePath = Config.getString(ControlPanelConfig.KEY_STORAGE_UPLOAD_BASE);
        return MessageFormat.format(Config.getString(ControlPanelConfig.KEY_STORAGE_PATH_FORMAT), basePath, uploadType, filename);
    }

    static public String getUploadFilePath(String origFilename, String uploadType) {
        String filename = generateUniqueFilename(FileUtils.getFileExtention(origFilename));
        return getUploadFilePathOfFilename(filename, uploadType);
    }

    static public File getUploadFile(String origFilename, String uploadType) {
        File uploadFile = new File(getUploadFilePath(origFilename, uploadType));
        File directory = uploadFile.getParentFile();
        System.out.println("########################### " + getUploadFilePath(origFilename, uploadType));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return uploadFile;
    }

    static public File getUploadFileByFilename(String filename, String uploadType) {
        File uploadFile = new File(getUploadFilePathOfFilename(filename, uploadType));
        File directory = uploadFile.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return uploadFile;
    }

    static public String generateUniqueFilename(String ext) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "." + ext;
    }

}
