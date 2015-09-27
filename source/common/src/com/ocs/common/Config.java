/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.common;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author Jeff
 */
public class Config {
    // Configuration key

    public static final String KEY_SMTP_HOST = "mail.smtp.host";
    public static final String KEY_SMTP_PORT = "mail.smtp.port";
    public static final String KEY_SMTP_AUTH = "mail.smtp.auth";
    public static final String KEY_SMTP_SENDER = "mail.smtp.sender";
    public static final String KEY_SMTP_USERNAME = "mail.smtp.username";
    public static final String KEY_SMTP_PASSWORD = "mail.smtp.password";
    public static final String KEY_COOKIE_PERSISTENT_ENABLED = "cookie.persistent.enabled";
    public static final String KEY_SESSION_TOKEN_TIMEOUT = "session.token.timeout";

    public static final String KEY_MESSAGE_SESSION_TIMEOUT = "session.timeout";
    public static final String KEY_STORAGE_UPLOAD_BASE = "storage.upload.base";
    public static final String KEY_STORAGE_PATH_FORMAT = "storage.path.format";
    public static final String KEY_STORAGE_DATE_FORMAT = "storage.date.format";
    public static final String KEY_CONTENT_ATTACHMENT_PATH = "storage.upload.contentattachment.path";
    public static final String KEY_MESSAGE_INBOX_SIDEBAR_SIZE = "message.inbox.sidebar.size";
    public static final String KEY_MESSAGE_INBOX_PAGESIZE = "message.inbox.pagesize";
    public static final String KEY_STORAGE_IMAGE_BASE = "storage.img.base";
    public static final String KEY_STORAGE_IMAGE_FILENAME_FORMAT = "storage.img.filename.fmt";
    public static final String KEY_IMAGE_TYPE = "img.type";
    public static final String KEY_IMAGE_WAIT = "img.wait";
    public static final String KEY_IMAGE_WIDTH = "img.width";
    public static final String KEY_IMAGE_HEIGHT = "img.height";
    public static final String KEY_PEOPLE_ICON_PATH = "storage.img.peopleicon.path";
    public static final String KEY_SPONSOR_LOGO_PATH = "storage.img.sponsorlogo.path";
    public static final String KEY_LOADER_SPONSOR_LOGO_BASEURL = "loader.sponsorlogo.baseurl";
    public static final String KEY_ADMINTOOL_BASEURL = "admintool.baseurl";
    public static final String KEY_WORKFLOW_ALERT_RESEND_INTERVAL = "workflow.alert_resend_interval";
    public static final String KEY_WORKFLOW_RELOAD_INTERVAL = "workflow.reload_interval";
    public static final String KEY_I18N_RESOURCE_SOURCE_ORIG_DIR = "i18n.resource.src.orig.dir";
    public static final String KEY_I18N_RESOURCE_SOURCE_TAGGED_DIR = "i18n.resource.src.tagged.dir";
    public static final String KEY_I18N_RESOURCE_BACKUP_DIR = "i18n.resource.backup.dir";
    public static final String KEY_I18N_RESOURCE_OUTPUT_DIR = "i18n.resource.output.dir";

    public static final String KEY_MAIL_BATCH_MAX_SEND = "mailbatch.max.send";
    public static final String KEY_MAIL_BATCH_MAX_SIZE = "mailbatch.max.size";

    public static final String KEY_AUTH_IGNORE_PATHS = "auth.ingore.paths";
    public static final String KEY_AUTH_RESTRICTED_PATHS = "auth.restricted.paths";
    public static final String KEY_AUTH_ACCESS_WHITELIST = "auth.access.whitelist";

    public static final String KEY_NEW_USER_ACCESS_LINK = "new.user.access.link";

    public static final String KEY_SYSTEM_MODE          = "system.mode";

    // Default values
    public static final String DEFAULT_ADMINTOOL_BASEURL = "http://localhost:8899";
    // Config instance
    private static Config instance = null;
    private static final PropertiesConfiguration config = new PropertiesConfiguration();

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void init(InputStream in) throws ConfigurationException {
        config.load(new InputStreamReader(in));
    }

    public void init(String in) throws ConfigurationException {
        config.load(new StringReader(in));
    }

    public static String getString(String key) {
        return config.getString(key);
    }

    public static String getString(String key, String defaultStr) {
        return config.getString(key, defaultStr);
    }

    public static int getByte(String key) {
        return config.getByte(key);
    }

    public static int getByte(String key, byte defaultValue) {
        return config.getByte(key, defaultValue);
    }

    public static int getInt(String key) {
        return config.getInt(key);
    }

    public static int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public static long getLong(String key) {
        return config.getLong(key);
    }

    public static long getLong(String key, long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public static short getShort(String key) {
        return config.getShort(key);
    }

    public static short getShort(String key, short defaultValue) {
        return config.getShort(key, defaultValue);
    }

    public static double getDouble(String key) {
        return config.getDouble(key);
    }

    public static double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public static List getList(String key) {
        return config.getList(key);
    }

    public static String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

    public static List getList(String key, List defaultValue) {
        return config.getList(key, defaultValue);
    }
}
