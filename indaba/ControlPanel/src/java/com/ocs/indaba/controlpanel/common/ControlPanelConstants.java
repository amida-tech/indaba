/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.common;

import com.ocs.common.Constants;

/**
 *
 * @author jiangjeff
 */
public class ControlPanelConstants extends Constants {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int DEFAULT_LANGUAGE_ID = 1;
    //
    // Library type
    public static final int INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED = 1;
    public static final int INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED = 2;
    public static final int INDICATOR_LIB_VISIBILITY_PUBLIC_TEST = 3;
    public static final int INDICATOR_LIB_VISIBILITY_PRIVATE = 4;
    //
    // Target type
    public static final int TARGET_LIB_VISIBILITY_PUBLIC = 1;
    public static final int TARGET_LIB_VISIBILITY_PRIVATE = 2;
    //
    // Resources
    //1 = indicator;2 = target;3 = survey config;4 = study period;5 = workflow;6 = rights setting;7 = privacy setting;8 = reference;
    public static final int RESOURCE_INDICATOR = 1;
    public static final int RESOURCE_TARGET = 2;
    public static final int RESOURCE_SURVEY_CONFIG = 3;
    public static final int RESOURCE_STUDY_PERIOD = 4;
    public static final int RESOURCE_WORKFLOW = 5;
    public static final int RESOURCE_RIGHTS_SETTING = 6;
    public static final int RESOURCE_PRIVACY_SETTING = 7;
    public static final int RESOURCE_REFERENCE = 8;
    //
    // Resoruce Visibility
    // 1 = public;2 = private;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_PRIVATE = 2;
    //
    // Resoruce Status
    // 1 = active;2 = deleted;
    public static final int RESOURCE_STATUS_ACTIVE = 1;
    public static final int RESOURCE_STATUS_DELETED = 2;
    //
    // Resource State
    // 
    public static final int RESOURCE_STATE_ENDORSED = 1;
    public static final int RESOURCE_STATE_EXTENDED = 2;
    public static final int RESOURCE_STATE_TEST = 3;
    //
    // Resoruce Status
    // 1 = active;2 = deleted;
    public static final short TARGET_STATUS_ACTIVE = 1;
    public static final short TARGET_STATUS_INACTIVE = 2;
    public static final short TARGET_STATUS_DELETED = 3;
    //
    // Project isActive
    // 1 = active;2 = inactive;
    public static final short PROJECT_IS_ACTIVE = 1;

    // I18n key regex
    public static final String I18N_KEY_REGEX = "^cp.*";
    
    //
    // Security Key
    //
    public static final short SECURITY_KEY_STATUS_NORMAL = 1;
    public static final short SECURITY_KEY_STATUS_REVOKED = 2;
    public static final short SECURITY_KEY_VERSION_START = 1;

    //
    // Upload Type
    //
    public static final String UPLOAD_TYPE_INDICATOR = "indicator";
    public static final String UPLOAD_TYPE_TRANSLATE = "translate";
    public static final String UPLOAD_TYPE_CONTRIBUTOR = "contributor";
    public static final String UPLOAD_TYPE_TABLE = "table";
    public static final String UPLOAD_TYPE_NOTIF = "notif";

    public static final int PROJECT_SECONDARY_OWNER_ACTION_ADD = 1;
    public static final int PROJECT_SECONDARY_OWNER_ACTION_DEL = 2;

    public static final int PRODUCT_NOT_READY_REASON_TARGET = 1;
    public static final int PRODUCT_NOT_READY_REASON_CONTRIBUTOR = 2;
    public static final int PRODUCT_NOT_READY_REASON_TASK_FOR_EACH_GOAL = 3;
    public static final int PRODUCT_NOT_READY_REASON_TASK_BELONG_TO_GOAL = 4;
    public static final int PRODUCT_NOT_READY_REASON_TASK_VALID_ID_TYPE = 5;
    public static final int PRODUCT_NOT_READY_REASON_NO_GOAL = 6;
    public static final int PRODUCT_NOT_READY_REASON_NO_TASK = 7;

    public static final int ADD_ASSIGNMENT_RT_OK = 0;
    public static final int ADD_ASSIGNMENT_RT_GOAL_DONE = 1;
    public static final int ADD_ASSIGNMENT_RT_SQL_EXCEPTION = 2;
    public static final int ADD_ASSIGNMENT_RT_NO_PRODUCT_OR_IN_CONFIG = 3;
    public static final int ADD_ASSIGNMENT_RT_NO_HORSE = 4;
    public static final int ADD_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_SINGLE = 5;
    public static final int ADD_ASSIGNMENT_RT_INVALID_TASK = 6;
    public static final int ADD_ASSIGNMENT_RT_NO_SUCH_USER = 7;
    public static final int ADD_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_MULTI = 8;

    public static final int UPDATE_ASSIGNMENT_RT_OK = 0;
    public static final int UPDATE_ASSIGNMENT_RT_NO_SUCH_ASSIGNMENT = 1;
    public static final int UPDATE_ASSIGNMENT_RT_SQL_EXCEPTION = 2;
    public static final int UPDATE_ASSIGNMENT_RT_NO_SUCH_USER = 3;
    public static final int UPDATE_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_SINGLE = 4;
    public static final int UPDATE_ASSIGNMENT_RT_INVALID_TASK = 5;
    public static final int UPDATE_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_MULTI = 6;

    public static final int MAX_LENGTH_INDICATOR_NAME = 255;
    public static final int MAX_LENGTH_INDICATOR_CHOICE_LABEL = 300;
    public static final int MAX_LENGTH_USER_FIRST_NAME = 45;
    public static final int MAX_LENGTH_USER_LAST_NAME = 45;
    public static final int MAX_LENGTH_USER_EMAIL = 255;

    public static final int MAX_LENGTH_NOTIF_NAME = 100;
}
