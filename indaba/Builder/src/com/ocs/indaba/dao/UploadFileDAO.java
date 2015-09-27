/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.UploadFile;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

public class UploadFileDAO extends SmartDaoMySqlImpl<UploadFile, Integer> {
    
    private static final Logger log = Logger.getLogger(UploadFileDAO.class);
    private static final String SELECT_UPLOAD_FILE_BY_NAME = "SELECT * FROM upload_file WHERE file_name=?";
    private static final String SELECT_UPLOADS_FILE_BY_NAMES = "SELECT * FROM upload_file WHERE {0}";
    
    public UploadFile selectUploadFileByName(String filename) {
        return super.findSingle(SELECT_UPLOAD_FILE_BY_NAME, filename);
    }
    
    public List<UploadFile> selectUploadFileByNames(List<String> filenames) {
        if (filenames == null || filenames.isEmpty()) {
            return null;
        }
        StringBuilder sBuf = new StringBuilder();
        boolean isFirst = false;
        for (String item : filenames) {
            if (!isFirst) {
                isFirst = true;
            } else {
                sBuf.append(" OR ");
            }
            sBuf.append("file_name='").append(item).append("'");
        }
        return super.find(MessageFormat.format(SELECT_UPLOADS_FILE_BY_NAMES, sBuf.toString()));
    }
}
