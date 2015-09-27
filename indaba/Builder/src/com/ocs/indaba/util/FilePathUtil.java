/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.common.Config;
import java.text.MessageFormat;

/**
 *
 * @author Jeff
 */
public class FilePathUtil {

    public static String getContentAttachmentPath(int horseId, String filename) {
        return MessageFormat.format(Config.getString(Config.KEY_CONTENT_ATTACHMENT_PATH),
                Config.getString(Config.KEY_STORAGE_UPLOAD_BASE), horseId, filename);
    }

    public static String getPeopleIconPath(String filename) {
        return MessageFormat.format(Config.getString(Config.KEY_PEOPLE_ICON_PATH),
                Config.getString(Config.KEY_STORAGE_IMAGE_BASE), filename);
    }

    public static String getSponsorLogoPath(int prjId, String filename) {
        return MessageFormat.format(Config.getString(Config.KEY_SPONSOR_LOGO_PATH),
                Config.getString(Config.KEY_STORAGE_IMAGE_BASE), prjId, filename);
    }

    public static String getFileExtention(String filename) {
        if (filename == null) {
            return "";
        }
        int pos = filename.lastIndexOf('.');
        return ((pos >= 0) ? filename.substring(pos + 1) : "");
    }

    public static String getFilename(String filename) {
        if (filename == null) {
            return "";
        }
        int pos = filename.lastIndexOf('.');
        return ((pos >= 0) ? filename.substring(0, pos) : filename);
    }
    public static void main(String args[]) {
        String s = java.text.MessageFormat.format("{0}/i18n_backup/{1}.{2}.{3}", "aaaa", "404",
                DateUtils.date2Str(new java.util.Date(), DateUtils.DEFAULT_DATETIME_FORMAT), getFileExtention("404.html"));
        System.out.println(s);
    }
}
