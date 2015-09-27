/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.JournalAttachmentVersion;
import com.ocs.indaba.util.FileStorageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.struts.upload.FormFile;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class AttachmentAdapter {
    private static final Logger logger = Logger.getLogger(AttachmentAdapter.class);

    public static final JournalAttachmentVersion attachmentToAttachmentVersion(Attachment attach) {
        if (attach == null) {
            return null;
        }
        JournalAttachmentVersion attachVer = new JournalAttachmentVersion();
        attachVer.setFilePath(attach.getFilePath());
        attachVer.setName(attach.getName());
        attachVer.setNote(attach.getNote());
        attachVer.setSize(attach.getSize());
        attachVer.setType(attach.getType());
        attachVer.setUpdateTime(attach.getUpdateTime());
        attachVer.setUserId(attach.getUserId());
        return attachVer;
    }

    public static final Attachment attachmentVersionToAttachment(JournalAttachmentVersion attachVer) {
        if (attachVer == null) {
            return null;
        }
        Attachment attach = new Attachment();
        attach.setFilePath(attachVer.getFilePath());
        attach.setName(attachVer.getName());
        attach.setNote(attachVer.getNote());
        attach.setSize(attachVer.getSize());
        attach.setType(attachVer.getType());
        attach.setUpdateTime(attachVer.getUpdateTime());
        return attach;
    }

    public static List<Attachment> formFileMap2AttachmentList(Map<String, FormFile> fileMap, Map<String, String> descMap, int cntHdrId) {
        List<Attachment> attachments = new ArrayList<Attachment>();
        if (fileMap != null && fileMap.size() > 0) {
            Set<String> keys = fileMap.keySet();
            for (String aKey : keys) {
                FormFile f = fileMap.get(aKey);
                attachments.add(formFile2Attachment(f, aKey, cntHdrId, descMap.get(f.getFileName())));
            }
        }
        return attachments;
    }
/*
    public static Attachment formFile2Attachment(FormFile formFile, int cntHdrId, String desc) {
        return formFile2Attachment(formFile, null, cntHdrId, desc);
    }
*/

    public static Attachment formFile2Attachment(FormFile formFile, String fullFilePath, int cntHdrId, String desc) {
        Attachment attachment = new Attachment();
        //attachment.setId(Constants.INVALID_INT_ID);
        attachment.setContentHeaderId(cntHdrId);
        attachment.setFilePath((fullFilePath == null)? formFile.getFileName(): fullFilePath);
        attachment.setName(formFile.getFileName());
        attachment.setSize(formFile.getFileSize());
        attachment.setType(FileStorageUtil.getSuffix(formFile.getFileName()));
        attachment.setNote(desc);
        return attachment;
    }
}
