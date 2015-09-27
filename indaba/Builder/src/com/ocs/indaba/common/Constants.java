/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.common;

import java.util.Map;
import java.util.HashMap;

/**
 * Common constants
 * 
 * @author Jeff
 */
public class Constants {

    public static final String INIT_PARAM_INDABA_CONFIG_FILE = "indabaConfigFile";
    public static final String INIT_PARAM_INTL_RESOURCE_DIR = "intlResourceDir";

    public static final long MILLSECONDS_PER_DAY = 24 * 3600 * 1000;
    public static final int COOKIE_MAX_AGE_DEL_WHEN_EXITS = -1;
    public static final int COOKIE_90_DAYS_AGE = 90 * 24 * 3600;
    public static final int LANG_EN = 1;
    public static final int LANG_FR = 2;
    public static final String CHARSET_UTF8 = "utf-8";
    
    ////////////////////////////////////////////////
    //
    // Constants definition for 'USER STATUS'
    //
    ////////////////////////////////////////////////
    public static final short USER_STATUS_INACTIVE = 0;
    public static final short USER_STATUS_ACTIVE = 1;
    public static final short USER_STATUS_DELETED = 2;
    //
    // User TYPE
    //
    public static final short USER_SITE_ADMINISTRATOR = 1;
    public static final short USER_NOT_SITE_ADMINISTRATOR = 0;
    // Status
    public static final int STATUS_INIT = 0;
    public static final int STATUS_ASSIGNED = 1;
    public static final int STATUS_CREATED = 2;
    public static final int STATUS_EDITED1 = 3;
    public static final int STATUS_REVIEWED = 4;
    public static final int STATUS_EDITED2 = 5;
    public static final int STATUS_PEER_REVIEWED = 6;
    public static final int STATUS_APPROVED = 7;
    public static final int STATUS_PAID = 8;
    public static final int STATUS_FINISHED = 9;
    // Product Content Type
    //public static final int PRODUCT_CONTENT_TYPE_JOURNAL = 1;
    //public static final int PRODUCT_CONTENT_TYPE_SURVEY = 2;
    // All
    public static final int SELECT_ALL = 0;
    //
    // Invalid id
    public static final int INVALID_INT_ID = -1;
    public static final long INVALID_LONG_ID = -1L;
    public static final float INVALID_FLOAT_ID = -1f;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'CONTENT TYPE'
    //
    ////////////////////////////////////////////////
    public static final int CONTENT_TYPE_SURVEY = 0;
    public static final int CONTENT_TYPE_JOURNAL = 1;
    public static final String CONTENT_SURVEY_ACTION = "surveyDisplay.do";
    public static final String CONTENT_PREVERSION_SURVEY_ACTION = "surveyPreVersionDisplay.do";
    public static final String CONTENT_JOURNAL_ACTION = "notebook.do";
    ////////////////////////////////////////////////
    //
    // Constants definition for 'SEQUENCE'
    //
    ////////////////////////////////////////////////
    public static final int SEQUENCE_OBJECT_STATUS_WAITING = 0;
    public static final int SEQUENCE_OBJECT_STATUS_STARTED = 1;
    public static final int SEQUENCE_OBJECT_STATUS_DONE = 2;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'GOAL'
    //
    ////////////////////////////////////////////////
    public static final int GOAL_OBJECT_STATUS_WAITING = 0;
    public static final int GOAL_OBJECT_STATUS_STARTING = 1;
    public static final int GOAL_OBJECT_STATUS_STARTED = 2;
    public static final int GOAL_OBJECT_STATUS_DONE = 3;
    public static final int GOAL_OBJECT_STATUS_OVERDUE = 9;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'GOAL OBJECT'
    //
    ////////////////////////////////////////////////
    //public static final int GOAL_OBJECT_STATUS_WAITING = 0;
    //public static final int GOAL_OBJECT_STATUS_INFLIGHT = 1;
    //public static final int GOAL_OBJECT_STATUS_DONE = 2;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'TASK'
    //
    ////////////////////////////////////////////////
    // Task status
    public static final int TASK_STATUS_INACTIVE = 0;
    public static final int TASK_STATUS_ACTIVE = 1;
    public static final int TASK_STATUS_AWARE = 2;
    public static final int TASK_STATUS_NOTICED = 3;
    public static final int TASK_STATUS_STARTED = 4;
    public static final int TASK_STATUS_DONE = 5;

    public static final short TASK_EXIT_TYPE_NORMAL = 0;
    public static final short TASK_EXIT_TYPE_FORCED = 1;

    // Task action
    public static final int TASK_ACTION_USERCOMPLETED = 1;
    public static final int TASK_ACTION_USERINPROGRESS = 2;
    public static final int TASK_ACTION_USERCLICKED = 3;
    public static final int TASK_ACTION_USERLOGIN = 4;
    public static final int TASK_ACTION_USERNOTICED = 5;
    public static final int TASK_ACTION_OTHERS = 6;
    // Task Tool
    public static final int TASK_TOOL_NOACTION = 0;
    public static final int TASK_TOOL_CREATE = 1;
    public static final int TASK_TOOL_EDIT = 2;
    public static final int TASK_TOOL_REVIEW = 3;
    public static final int TASK_TOOL_PEER_REVIEW = 5;
    public static final int TASK_TOOL_PAY = 6;
    public static final int TASK_TOOL_APPROVE = 7;
    public static final int TASK_TOOL_SUBMIT = 0xFF;
    //Task assginment method
    public static final short TASK_ASSIGNMENT_METHOD_MANUAL = 1;
    public static final short TASK_ASSIGNMENT_METHOD_QUEUE = 2;
    public static final short TASK_ASSIGNMENT_METHOD_DYNAMIC = 3;
    //Task assginment Status
    public static final short TASK_ASSIGNMENT_STATUS_INACTIVE = 0;
    public static final short TASK_ASSIGNMENT_STATUS_ACTIVE = 1;
    public static final short TASK_ASSIGNMENT_STATUS_AWARE = 2;
    public static final short TASK_ASSIGNMENT_STATUS_NOTICED = 3;
    public static final short TASK_ASSIGNMENT_STATUS_STARTED = 4;
    public static final short TASK_ASSIGNMENT_STATUS_DONE = 5;
    // TASK ASSIGNMENT PRIORITY
    public static final short TASK_ASSIGNMENT_PRIORITY_LOW = 1;
    public static final short TASK_ASSIGNMENT_PRIORITY_MEDIUM = 2;
    public static final short TASK_ASSIGNMENT_PRIORITY_HIGH = 3;
    
    // Task Action
    public static final String TASK_ACTION_NOACTION = "noaction";
    public static final String TASK_ACTION_CREATE = "create";
    public static final String TASK_ACTION_EDIT = "edit";
    public static final String TASK_ACTION_SAVE = "save";
    public static final String TASK_ACTION_REVIEW = "review";
    public static final String TASK_ACTION_PEER_REVIEW = "peerreview";
    public static final String TASK_ACTION_PAY = "pay";
    public static final String TASK_ACTION_APPROVE = "approve";
    public static final String TASK_ACTION_SUBMIT = "submit";
    public static final String TASK_ACTION_OVERALL_REVIEW = "overall";
    //
    // Map of tool and action
    //
    public static final Map<Integer, String> ACTION_MAP = new HashMap<Integer, String>();

    static {
        ACTION_MAP.put(TASK_TOOL_NOACTION, TASK_ACTION_NOACTION);
        ACTION_MAP.put(Constants.TASK_TYPE_JOURNAL_CREATE, TASK_ACTION_CREATE);
        ACTION_MAP.put(Constants.TASK_TYPE_JOURNAL_CREATE, TASK_ACTION_CREATE);
        ACTION_MAP.put(Constants.TASK_TYPE_JOURNAL_EDIT, TASK_ACTION_EDIT);
        ACTION_MAP.put(Constants.TASK_TYPE_SURVEY_EDIT, TASK_ACTION_EDIT);
        ACTION_MAP.put(Constants.TASK_TYPE_JOURNAL_REVIEW, TASK_ACTION_REVIEW);
        ACTION_MAP.put(Constants.TASK_TYPE_SURVEY_REVIEW, TASK_ACTION_REVIEW);
        ACTION_MAP.put(Constants.TASK_TYPE_JOURNAL_PEER_REVIEW, TASK_ACTION_PEER_REVIEW);
        ACTION_MAP.put(Constants.TASK_TYPE_SURVEY_PEER_REVIEW, TASK_ACTION_PEER_REVIEW);
        ACTION_MAP.put(Constants.TASK_TYPE_PAYMENT, TASK_ACTION_PAY);
        ACTION_MAP.put(Constants.TASK_TOOL_APPROVE, TASK_ACTION_APPROVE);
        ACTION_MAP.put(TASK_TOOL_SUBMIT, TASK_ACTION_SUBMIT);
        ACTION_MAP.put(Constants.TASK_TYPE_JOURNAL_REVIEW_RESPONSE, TASK_ACTION_CREATE);
        ACTION_MAP.put(Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE, TASK_ACTION_CREATE);
        //ACTION_MAP.put(Constants.TASK_TYPE_JOURNAL_OVERALL_REVIEW, TASK_ACTION_OVERALL_REVIEW);
    }
    ////////////////////////////////////////////////
    //
    // Constants definition for 'WORKFLOW'
    //
    ////////////////////////////////////////////////
    public static final int WORKFLOW_OBJECT_STATUS_WAITING = 1;
    public static final int WORKFLOW_OBJECT_STATUS_STARTED = 2;
    public static final int WORKFLOW_OBJECT_STATUS_DONE = 3;
    public static final int WORKFLOW_OBJECT_STATUS_SUSPENDED = 4;
    public static final int WORKFLOW_OBJECT_STATUS_CANCELLED = 5;
    //
    // Parameters(which are specified in all content page with filters)
    //
    public static final int PARAM_ALL_PRODUCTS = 0;
    public static final int PARAM_ALL_TARGETS = 0;
    public static final int PARAM_ALL_ROLES = 0;
    public static final int PARAM_ALL_TEAMS = 0;
    public static final int PARAM_ALL_HAS_CASE = 0;
    public static final int PARAM_HAS_CASE_YES = 1;
    public static final int PARAM_HAS_CASE_NO = 2;
    public static final int PARAM_ALL_STATUSES = 0;
    public static final int PARAM_STATUS_NOT_OVERDUE = 1;
    public static final int PARAM_STATUS_OVERDUE = 2;
    //
    // Actions
    //
    public static final int ACTION_NOACTION = 0;
    public static final int ACTION_CREATE = 1;
    public static final int ACTION_EDIT1 = 2;
    public static final int ACTION_STAFF_REVIEW = 3;
    public static final int ACTION_EDIT2 = 4;
    public static final int ACTION_PEER_REVIEW = 5;
    public static final int ACTION_APPROVE = 6;
    public static final int ACTION_PAY = 7;
    public static final int ACTION_SEND_MESSAGE = 108;
    // timeout for in-flight rule
    public static final int ACTION_TIMEOUT = 5000; // milliseconds
    // default attachment type
    public static final String DEFAULT_FILE_TYPE = "unkown";
    ////////////////////////////////////////////////
    //
    // Constants definition for 'VIEW PERMISSION'
    //
    ////////////////////////////////////////////////
    public static final int DEFAULT_VIEW_MATRIX_ID = 1;
    public static final int VIEW_PERMISSION_NONE = 0;
    public static final int VIEW_PERMISSION_LIMITED = 1;
    public static final int VIEW_PERMISSION_FULL = 2;
    public static final int VIEW_PERMISSION_STATS = 3;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'SYSTEM ADMIN DISPLAY NAME'
    //
    ////////////////////////////////////////////////
    public static final String SYSTEM_ADMIN_DISPLAY_NAME = "Indaba System";
    ////////////////////////////////////////////////
    //
    // Constants definition for 'ACCESS PERMISSION'
    //
    ////////////////////////////////////////////////
    public static final int ACCESS_MATRIX_UNDIFINED = 0;
    // access permission type[Refer to DB table 'access_matrix' and 'access_permission']
    public static final int ACCESS_PERMISSION_NO = 0;
    public static final int ACCESS_PERMISSION_YES = 1;
    public static final int ACCESS_PERMISSION_UNDIFINED = 2;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'CASE'
    //
    ////////////////////////////////////////////////
    public static final String CASE_NOTE_TYPE_USER = "user";
    public static final String CASE_NOTE_TYPE_STAFF = "staff";
    // CASE PRIORITY
    public static final short CASE_PRIORITY_LOW = 0;
    public static final short CASE_PRIORITY_MEDIUM = 1;
    public static final short CASE_PRIORITY_HIGH = 2;
    // CASE STATUS
    public static final short CASE_STATUS_OPEN = 0;
    public static final short CASE_STATUS_CLOSED = 1;
    // CASE SUB STATUS
    public static final short CASE_SUB_STATUS_NEW = 1;
    public static final short CASE_SUB_STATUS_ASSIGNED = 2;
    public static final short CASE_SUB_STATUS_RESOLVED = 101;
    public static final short CASE_SUB_STATUS_INVALID = 102;
    public static final short CASE_SUB_STATUS_WITHDRAWN = 103;
    public static final short CASE_SUB_STATUS_DUPLICATE = 104;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'CONTENT_HEADER'
    //
    ////////////////////////////////////////////////
    public static final int CONTENT_HEADER_STATUS_IN_FLIGHT = 0;
    public static final int CONTENT_HEADER_STATUS_LOCKED = 1;
    public static final int CONTENT_HEADER_STATUS_DELETED = 2;
    public static final int CONTENT_HEADER_STATUS_DONE = 3;
    public static final int CONTENT_HEADER_STATUS_PUBLISHED = 4;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'SURVEY ANSWER'
    //
    ////////////////////////////////////////////////
    public static final int SURVEY_ANSWER_TYPE_SINGLE_CHOICE = 1;
    public static final int SURVEY_ANSWER_TYPE_MULTI_CHOICE = 2;
    public static final int SURVEY_ANSWER_TYPE_INTEGER = 3;
    public static final int SURVEY_ANSWER_TYPE_FLOAT = 4;
    public static final int SURVEY_ANSWER_TYPE_TEXT = 5;
    public static final int SURVEY_ANSWER_TYPE_TABLE = 6;

    public static final String[] SURVEY_ANSWER_TYPE_TEXT_KEYS = {
        "dummy",
        "cp.text.answertype.singlechoice",
        "cp.text.answertype.multichoice",
        "cp.text.answertype.integer",
        "cp.text.answertype.float",
        "cp.text.answertype.text",
        "cp.text.answertype.table",
    };


    ////////////////////////////////////////////////
    //
    // Constants definition for 'REFERENCE CHOICE TYPE'
    //
    ////////////////////////////////////////////////
    public static final int REFERENCE_TYPE_NO_CHOICE = 0;
    public static final int REFERENCE_TYPE_SINGLE_CHOICE = 1;
    public static final int REFERENCE_TYPE_MULTI_CHOICE = 2;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'EVENT LOG'
    //
    ////////////////////////////////////////////////
    public static final int DEFAULT_EVENTLOG_ID = 1;
    // event type
    public static final short EVENT_TYPE_LOGIN = 1;
    public static final short EVENT_TYPE_LOGOUT = 2;
    public static final short EVENT_TYPE_CLICK_ASSIGNMENT = 3;
    public static final short EVENT_TYPE_WORK_ON_TASK = 4;
    public static final short EVENT_TYPE_COMPLETE_TASK = 5;
    // complete percentage(for NOTEBOOK only)
    public static final float COMPLETE_PERCENTAGE_INACTIVE = 0;
    public static final float COMPLETE_PERCENTAGE_USERLOGIN = 0.05f;
    public static final float COMPLETE_PERCENTAGE_USERCLICKED = 0.15f;
    public static final float COMPLETE_PERCENTAGE_USERSAVE = 0.75f;
    public static final float COMPLETE_PERCENTAGE_USERSAVEANDDONE = 1.0f;
    public static final String SURVEY_ACTION_VIEW = "view";
    public static final String SURVEY_ACTION_REVIEW = "review";
    public static final String SURVEY_ACTION_CREATE = "create";
    public static final String SURVEY_ACTION_EDIT = "edit";
    public static final String SURVEY_ACTION_DISPLAY = "display";
    public static final String SURVEY_ACTION_PEERREVIEW = "peerreview";
    public static final String SURVEY_ACTION_PRREVIEW = "prreview";
    public static final String SURVEY_ACTION_OVERALLREVIEW = "overallreview";
    public static final String SURVEY_ACTION_REVIEWRESPONSE = "reviewresponse";
    public static final String SURVEY_ACTION_FLAGRESPONSE = "flagresponse";
    public static final String SURVEY_ACTION_FLAGRESOLVE = "flagresolve";

    public static final Map<String, String> surveyActionMap = new HashMap<String, String>();

    static {
        surveyActionMap.put("surveyCreate.do", "surveyAnswer.do");
        surveyActionMap.put("surveyEdit.do", "surveyAnswer.do");
        surveyActionMap.put("surveyReviewResponse.do", "surveyAnswer.do"); // survey response
        surveyActionMap.put("surveyReview.do", "surveyAnswerReview.do");
        surveyActionMap.put("surveyPeerReview.do", "surveyAnswerPeerReview.do");
        surveyActionMap.put("surveyPRReview.do", "surveyAnswerPRReview.do");
        surveyActionMap.put("surveyDisplay.do", "surveyAnswerDisplay.do");
        surveyActionMap.put("surveyOverallReview.do", "surveyAnswer.do");
        surveyActionMap.put(SURVEY_ACTION_DISPLAY, "surveyAnswerDisplay.do");
    }
    ////////////////////////////////////////////////
    //
    // Constants definition for 'LANGUAGE'
    //
    ////////////////////////////////////////////////
    public static final int LANGUAGE_STATUS_ACTIVE = 0;
    public static final int LANGUAGE_STATUS_INACTIVE = 1;
    ////////////////////////////////////////////////
    public static final int LANGUAGE_ID_EN = 1;
    public static final int LANGUAGE_ID_FR = 2;
    public static final int LANGUAGE_ID_ES = 3;
    public static final int LANGUAGE_ID_CN = 4;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'RULE TYPE'
    //
    ////////////////////////////////////////////////
    public static final int RULE_TYPE_ENTRANCE = 1;
    public static final int RULE_TYPE_INFLIGHT = 2;
    public static final int RULE_TYPE_EXIT = 3;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'SURVEY PEER OPTION'
    //
    ////////////////////////////////////////////////
    public static final int SURVEY_PEER_OPTION_AGREE = 0;
    public static final int SURVEY_PEER_OPTION_AGREE_WITH_COMMENTS = 1;
    public static final int SURVEY_PEER_DISAGREE = 2;
    public static final int SURVEY_PEER_NOT_QUALIFIED = 3;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'TAGGED OBJECT TYPE'
    //
    ////////////////////////////////////////////////
    public static final int USER_TAG = 0;
    public static final int SYSTEM_TAG = 1;
    public static final int TAGGED_OBJECT_TYPE_SURVEY_ANSWER = 1;
    public static final int TAGGED_OBJECT_TYPE_CONTENT_OBJECT = 2;
    public static final int TAGGED_OBJECT_TYPE_USER = 3;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'TARGET TYPE'
    //
    ////////////////////////////////////////////////
    public static final int TARGET_TYPE_REGION = 0;
    public static final int TARGET_TYPE_COUNTRY = 1;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'EMAIL DETAIL LEVEL'
    //
    ////////////////////////////////////////////////
    public static final int TARGET_TYPE_ALERT_ONLY = 0;
    public static final int TARGET_TYPE_FULL_MESSAAGE = 1;
    ////////////////////////////////////////////////
    //
    // Constants definition for 'NOTIFICATION_TOKENS '
    //
    ////////////////////////////////////////////////
    public static String NOTIFICATION_TOKEN_USER_NAME = "username";
    public static String NOTIFICATION_TOKEN_PROJECT_NAME = "projectname";
    public static String NOTIFICATION_TOKEN_PRODUCT_NAME = "productname";
    public static String NOTIFICATION_TOKEN_TARGET_NAME = "targetname";
    public static String NOTIFICATION_TOKEN_TASK_DUE_TIME = "taskduetime";
    public static String NOTIFICATION_TOKEN_DAYS_BEFORE_DUE = "daysbeforedue";
    public static String NOTIFICATION_TOKEN_DAYS_AFTER_DUE = "daysafterdue";
    public static String NOTIFICATION_TOKEN_PROJECT_START_TIME = "projectstarttime";
    public static String NOTIFICATION_TOKEN_GOAL_NAME = "goalname";
    public static String NOTIFICATION_TOKEN_TASK_NAME = "taskname";
    public static String NOTIFICATION_TOKEN_GOAL_DUE_TIME = "goalduetime";
    public static String NOTIFICATION_TOKEN_CONTENT_TITLE = "contenttitle";
    public static String NOTIFICATION_TOKEN_PROJECT_ADMIN = "projectadmin";
    public static String NOTIFICATION_TOKEN_CASE_NAME = "casename";
    public static String NOTIFICATION_TOKEN_CASE_STATUS = "casestatus";
    public static String NOTIFICATION_TOKEN_CASE_ID = "caseid";
    public static String NOTIFICATION_TOKEN_FULL_NAME = "fullname";
    public static String NOTIFICATION_TOKEN_ADDER_NAME = "addername";
    public static String NOTIFICATION_TOKEN_ORG_NAME = "orgname";
    public static String NOTIFICATION_TOKEN_NOTE = "note";
    public static String NOTIFICATION_TOKEN_FIRST_NAME = "firstname";
    public static String NOTIFICATION_TOKEN_LAST_NAME = "lastname";
    public static String NOTIFICATION_TOKEN_EMAIL = "email";
    public static String NOTIFICATION_TOKEN_ACCESS_LINK = "accesslink";
    public static String NOTIFICATION_TOKEN_ROLE_NAME = "rolename";
    //public static final String NOTIFICATION_TOKEN_DUETIME = "duetime";
    ////////////////////////////////////////////////
    //
    // Constants definition for 'NOTIFICATION_NAME'
    //
    ////////////////////////////////////////////////
    public static final String NOTIFICATION_TYPE_ALERT_ABOUT_DUE = "Alert - Milestone About Due";
    public static final String NOTIFICATION_TYPE_ALERT_OVERDUE = "Alert - Milestone Overdue";
    public static final String NOTIFICATION_TYPE_ALERT_TASK_NONASSIGNED = "Alert - Task Not Assigned";
    public static final String NOTIFICATION_TYPE_ALERT1_TASK_OVERDUE = "Alert 1 - Task Overdue";
    public static final String NOTIFICATION_TYPE_ALERT2_TASK_OVERDUE = "Alert 2 - Task Overdue";
    public static final String NOTIFICATION_TYPE_CONFIRM_TASK_COMPLETED = "Confirm - Task Completed";
    public static final String NOTIFICATION_TYPE_MSG_THANK_YOU = "Msg - Thank You";
    public static final String NOTIFICATION_TYPE_MSG_WELCOME = "Msg - Welcome";
    public static final String NOTIFICATION_TYPE_NOTICE1_TASK_OVERDUE = "Notice 1 - Task overdue";
    public static final String NOTIFICATION_TYPE_NOTICE2_TASK_OVERDUE = "Notice 2 - Task overdue";
    public static final String NOTIFICATION_TYPE_NOTIFY_MILESTONE_COMPLETED = "Notify - Milestone Completed";
    public static final String NOTIFICATION_TYPE_NOTIFY_CLAIM = "Notify - Please Claim";
    public static final String NOTIFICATION_TYPE_NOTIFY_PROJECT_DONE = "Notify - Project Done";
    public static final String NOTIFICATION_TYPE_NOTIFY_TASK_ACTIVATED = "Notify - Task Activated";
    public static final String NOTIFICATION_TYPE_REMINDER1_ABOUT_DUE = "Reminder 1 - Task about due";
    public static final String NOTIFICATION_TYPE_REMINDER2_ABOUT_DUE = "Reminder 2 - Task about due";
    public static final String NOTIFICATION_TYPE_SYS_ALERT_SITE_MSG = "Sys - Site Message Alert";
    public static final String NOTIFICATION_TYPE_SYS_CASE_ASSIGNED = "Sys - Case Assigned ";
    public static final String NOTIFICATION_TYPE_SYS_CASE_ATTACHED = "Sys - Case Attached";
    public static final String NOTIFICATION_TYPE_SYS_CASE_UPDATED = "Sys - Case Updated";
    public static final String NOTIFICATION_TYPE_SYS_CASE_STATUS_CHANGE = "Sys - Case Status Change";
    public static final String NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_POSTED = "Sys - Review Feedback Posted";
    public static final String NOTIFICATION_TYPE_SYS_REVIEW_RESPONSE_POSTED = "Sys - Review Feedback Response Posted";
    public static final String NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_CANCELED = "Sys - Review Feedback Canceled";
    public static final String NOTIFICATION_TYPE_POST_TASK_COMPLETED = "Post - Task Completed";
    public static final String NOTIFICATION_TYPE_POST_HORSE_COMPLETED = "Post - Horse Completed";
    public static final String NOTIFICATION_TYPE_SYS_USER_PASSWORD = "Sys - User Password";
    public static final String NOTIFICATION_TYPE_SYS_TASK_ASSIGNED = "Sys - Task Assigned";
    public static final String NOTIFICATION_TYPE_NOTIFY_PAYMENT_SENT = "Notify - Payment Sent";
    public static final String NOTIFICATION_TYPE_NOTIFY_HORSE_COMPLETION = "Notify - Horse Completion";
    public static final String NOTIFICATION_TYPE_POST_PEER_REVIEW_START = "Post - Peer Review Start";
    public static final String NOTIFICATION_TYPE_POST_PEER_REVIEW_COMPLETE = "Post - Peer Review Complete";
    public static final String NOTIFICATION_TYPE_TASK_DEADLINE_CHANGE = "Notify - Task Deadline Changed";
    public static final String NOTIFICATION_TYPE_NOTIFY_SITE_MESSAGE = "Notify - Site message/mail";
    public static final String NOTIFICATION_TYPE_NOTIFY_NEW_USER_OA = "Notify - new user added as OA";
    public static final String NOTIFICATION_TYPE_NOTIFY_EXISTING_USER_OA = "Notify - existing user added as OA";
    public static final String NOTIFICATION_TYPE_NOTIFY_NEW_USER_PC = "Notify - new user added as PC";
    public static final String NOTIFICATION_TYPE_NOTIFY_EXISTING_USER_PC = "Notify - existing user added as PC";
    public static final String NOTIFICATION_TYPE_NOTIFY_ADD_USER_PA = "Notify - user added as PA";
    public static final String NOTIFICATION_TYPE_NOTIFY_DEL_USER_PA = "Notify - user removed as PA";
    public static final String NOTIFICATION_TYPE_NOTIFY_DEL_USER_PC = "Notify - user removed as PC";
    public static final String NOTIFICATION_TYPE_NOTIFY_DEL_USER_OA = "Notify - user removed as OA";
    public static final String NOTIFICATION_TYPE_TASK_SUMMARY = "Sys - task summary";
    
    ////////////////////////////////////////////////
    //
    // Constants definition for 'TASK_TYPE'
    //
    ////////////////////////////////////////////////
    public static final int TASK_TYPE_JOURNAL_CREATE = 1;
    public static final int TASK_TYPE_JOURNAL_EDIT = 2;
    public static final int TASK_TYPE_JOURNAL_REVIEW = 3;
    public static final int TASK_TYPE_JOURNAL_PEER_REVIEW = 4;
    public static final int TASK_TYPE_JOURNAL_VIEW = 5;
    public static final int TASK_TYPE_SURVEY_CREATE = 6;
    public static final int TASK_TYPE_SURVEY_EDIT = 7;
    public static final int TASK_TYPE_SURVEY_REVIEW = 8;
    public static final int TASK_TYPE_SURVEY_PEER_REVIEW = 9;
    public static final int TASK_TYPE_SURVEY_VIEW = 10;
    public static final int TASK_TYPE_PAYMENT = 11;
    public static final int TASK_TYPE_APPROVE = 12;
    public static final int TASK_TYPE_JOURNAL_PR_REVIEW = 13;
    public static final int TASK_TYPE_SURVEY_PR_REVIEW = 14;
    public static final int TASK_TYPE_START_HORSE = 15;
    public static final int TASK_TYPE_JOURNAL_REVIEW_RESPONSE = 16;
    public static final int TASK_TYPE_SURVEY_REVIEW_RESPONSE = 17;
    public static final int TASK_TYPE_JOURNAL_OVERALL_REVIEW = 18;
    public static final int TASK_TYPE_SURVEY_OVERALL_REVIEW = 19;
    public static final int TASK_TYPE_SURVEY_FLAG_RESPONSE = 20;
    public static final int TASK_TYPE_SURVEY_UNSET_FLAG = 21;

    public static final float TASK_ASSIGNMENT_PERCENT_CAPPED = 0.95f;
    public static final String QUEUE_ASSIGN_NOBODY = "NO ONE";
    //
    // Survey review options: agree, clarification, disagree, no qualification
    public static final int SURVEY_REVIEW_OPTION_NONE = -1;
    public static final int SURVEY_REVIEW_OPTION_AGREE = 0;
    public static final int SURVEY_REVIEW_OPTION_AGREE_BUT_CLARIFICATION = 1;
    public static final int SURVEY_REVIEW_OPTION_DISAGREE = 2;
    public static final int SURVEY_REVIEW_OPTION_NO_QUALIFICATION = 3;
    //
    // Userfinder status
    public static final short USERFINDER_STATUS_ACTIVE = 1;
    public static final short USERFINDER_STATUS_INACTIVE = 2;
    public static final short USERFINDER_STATUS_DELETE = 3;
    //
    // Case object type
    public static final short CASE_OBJECT_TYPE_USER = 0;
    public static final short CASE_OBJECT_TYPE_CONTENT = 1;
    //
    // Session token timeout  (s)
    public static final int DEFAULT_SESSION_TOKEN_TIMEOUT = 20 * 60;
    //
    // Cookie name
    public static final String COOKIE_TOKEN = "t";
    public static final String COOKIE_ACTIVE_TAB = "at";
    public static final String COOKIE_CASE_ID = "cid";
    public static final String COOKIE_LANGUAGE = "lang";
    //
    // Attribute name
    public static final String ATTR_USERID = "uid";
    public static final String ATTR_USERNAME = "userName";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_PROJECT_LIST = "prjList";
    public static final String ATTR_PROJECT_NAME = "prjName";
    public static final String ATTR_PROJECT_ID = "prjid";
    public static final String ATTR_CONTEXT_PATH = "contextPath";
    public static final String ATTR_ADMINTOOL_BASEURL = "admintoolBaseURL";
    public static final String ATTR_ERR_MSG = "errMsg";
    public static final String ATTR_LANG = "lang";
    
    //
    // Tab names
    public static final String TAB_YOURCONTENT = "yourcontent";
    public static final String TAB_ALLCONTENT = "allcontent";
    public static final String TAB_QUEUES = "queues";
    public static final String TAB_CASES = "cases";
    public static final String TAB_PEOPLE = "people";
    public static final String TAB_MESSAGING = "messaging";
    public static final String TAB_HELP = "help";
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
    public static final short VISIBILITY_PUBLIC = 1;
    public static final short VISIBILITY_PRIVATE = 2;

    //
    // Library type
    public static final short INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED = 1;
    public static final short INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED = 2;
    public static final short INDICATOR_LIB_VISIBILITY_PUBLIC_TEST = 3;
    public static final short INDICATOR_LIB_VISIBILITY_PRIVATE = 4;

    public static final String[] INDICATOR_LIB_TEXT_KEYS = {
        "dummy",
        "cp.text.indlib.endorsed",
        "cp.text.indlib.extended",
        "cp.text.indlib.test",
        "cp.text.indlib.private",
    };

    //
    // Resoruce Status
    // 1 = active;2 = deleted;
    public static final int RESOURCE_STATUS_ACTIVE = 1;
    public static final int RESOURCE_STATUS_DELETED = 2;
    //
    //
    public static final short INDICATOR_STATUS_ACTIVE = 1;
    public static final short INDICATOR_STATUS_INACTIVE = 2;
    public static final short INDICATOR_STATUS_DELETED = 3;
    //
    // Resource State
    // 
    public static final int RESOURCE_STATE_ENDORSED = 1;
    public static final int RESOURCE_STATE_EXTENDED = 2;
    public static final int RESOURCE_STATE_TEST = 3;
    //
    // Message Type
    // 
    public static final short MESSAGE_TYPE_INBOX = 1;
    public static final short MESSAGE_TYPE_OUTBOX = 2;
    public static final short MESSAGE_TYPE_PROJECT = 3;
    //
    // Message Type
    // 
    public static final short MESSAGE_SEND_TYPE_NEW = 1;
    public static final short MESSAGE_SEND_TYPE_REPLY = 2;
    public static final short MESSAGE_SEND_TYPE_FORWARD = 3;
    //
    // System Mode
    //
    public static final String SYSTEM_MODE_DEMO = "DEMO";
    public static final String SYSTEM_MODE_PROD = "PRODUCTION";

    //Product Mode
    public static final short PRODUCT_MODE_CONFIG = 1;
    public static final short PRODUCT_MODE_TEST = 2;
    public static final short PRODUCT_MODE_PROD = 3;

    //Horse Status
    public static final int HORSE_STATUS_INITIAL = 1;
    public static final int HORSE_STATUS_RUNNING = 2;
    public static final int HORSE_STATUS_COMPLETED = 3;
    public static final int HORSE_STATUS_STOPPED = 4;
    public static final int HORSE_STATUS_CANCELLED = 5;

     //procedure function
    public static final String PROCEDURE_INIT_HORSE = "init_horse";
    public static final String PROCEDURE_DEL_HORSE = "del_horse";
    public static final String PROCEDURE_RESET_PRODUCT = "reset_product";
    public static final String PROCEDURE_START_PRODUCT = "start_product";
    public static final String PROCEDURE_DEL_PRODUCT = "del_product";
    public static final String PROCEDURE_DEL_TARGET = "del_target";
    public static final String PROCEDURE_ADD_ASSIGNMENT = "add_assignment";
    public static final String PROCEDURE_CLONE_PRODUCT = "clone_product";
    public static final String PROCEDURE_CLONE_SURVEY_CONFIG = "clone_survey_config";
    public static final String PROCEDURE_CREATE_SURVEY_VERSION = "create_survey_version";
    public static final String PROCEDURE_DELETE_NOTEDEF = "delete_notedef";
    public static final String PROCEDURE_DELETE_GROUPDEF = "delete_groupdef";

    public static final int TASK_VALIDITIY_FULL = 3;
    public static final int TASK_VALIDITIY_HALF = 2;
    public static final int TASK_VALIDITIY_NONE = 1;

    public static final int TASK_USER_TYPE_SINGLE = 1;
    public static final int TASK_USER_TYPE_MULTI = 2;
    public static final int TASK_USER_TYPE_UNKNOWN = 3;

    public static final char SEND_QST_OPTION_MAIN_DISCUSSION = 'A';
    public static final char SEND_QST_OPTION_MAIN_CHANGE_SCORE = 'B';
    public static final char SEND_QST_OPTION_MAIN_CHANGE_SRC_DESC = 'C';
    public static final char SEND_QST_OPTION_MAIN_CHANGE_COMMENTS = 'D';
    public static final char SEND_QST_OPTION_MAIN_CHANGE_ATTACHMENT = 'E';
    public static final char SEND_QST_OPTION_PR_DISCUSSION = 'F';
    public static final char SEND_QST_OPTION_PR_CHANGE_OPTIONS = 'G';

    public static final int FLAG_PERMISSION_MAIN_CHANGE_SCORE = 1;
    public static final int FLAG_PERMISSION_MAIN_CHANGE_SOURCE = 2;
    public static final int FLAG_PERMISSION_MAIN_CHANGE_COMMENT = 4;
    public static final int FLAG_PERMISSION_MAIN_CHANGE_ATTACHMENT = 8;
    public static final int FLAG_PERMISSION_PR_CHANGE_OPINION = 16;
    public static final int FLAG_PERMISSION_PR_DISCUSSION = 32;

    public static final short GROUP_COMMENT_TYPE_REGULAR = 0;
    public static final short GROUP_COMMENT_TYPE_SET_FLAG = 1;
    public static final short GROUP_COMMENT_TYPE_FLAG_RESPONSE = 2;
    public static final short GROUP_COMMENT_TYPE_UNSET_FLAG = 3;

    public static final int NOTEDEF_PERMISSION_EDIT = 1;
    public static final int NOTEDEF_PERMISSION_TRANSLATE = 2;

    public static final int GROUPDEF_PERMISSION_MANAGE = 1;

    public static final int TASK_ID_FLAG_RESPONSE = -3;
    public static final int TASK_ID_UNSET_FLAG = -4;

    public static final String TASK_STATUS_ICON_IDLE = "status_gray.png";
    public static final String TASK_STATUS_ICON_STOPPED = "status_red.png";
    public static final String TASK_STATUS_ICON_STARTED = "status_green.png";
    public static final String TASK_STATUS_ICON_FLAG_TO_ME = "task-status-flag-to-me.png";
    public static final String TASK_STATUS_ICON_FLAG_BY_ME = "task-status-flag-by-me.png";

}
