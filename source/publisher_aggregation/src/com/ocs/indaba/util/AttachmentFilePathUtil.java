/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import java.text.MessageFormat;

/**
 *
 * @author Jeff
 */
public class AttachmentFilePathUtil {

    public final static String getContentAttachmentPath(int horseId, String filename) {
        String baseUploadPath = Config.getString(Constants.KEY_STORAGE_UPLOAD_BASE);
        String attchmentPath = Config.getString(Constants.KEY_CONTENT_ATTACHMENT_PATH);
        return MessageFormat.format(attchmentPath, baseUploadPath, horseId, filename);
    }

}
