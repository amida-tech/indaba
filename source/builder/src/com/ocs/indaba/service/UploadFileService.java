/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.UploadFileDAO;
import com.ocs.indaba.po.UploadFile;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class UploadFileService {

    private UploadFileDAO uploadFileDao = null;

    @Autowired
    public void setUploadFilesDao(UploadFileDAO uploadFilesDao) {
        this.uploadFileDao = uploadFilesDao;
    }

    public UploadFile getUploadFile(int id) {
        return uploadFileDao.get(id);
    }

    public UploadFile addUploadFile(UploadFile uploadFile) {
        return uploadFileDao.create(uploadFile);
    }

    public UploadFile updateUploadFile(UploadFile uploadFile) {
        return uploadFileDao.update(uploadFile);
    }
    
    public UploadFile getUploadFileByName(String filename) {
        return uploadFileDao.selectUploadFileByName(filename);
    }
    public List<UploadFile> getUploadFilesByNames(List<String> filenames) {
        return uploadFileDao.selectUploadFileByNames(filenames);
    }

    public List<UploadFile> getAllUploadFiles() {
        return uploadFileDao.findAll();
    }
}
