/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.common;

import com.ocs.intl.Language;
import com.ocs.intl.ResourceLoader;
import com.ocs.intl.ResourceManager;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public final class Messages {

    private static final Logger logger = Logger.getLogger(Messages.class);
    public static final String KEY_COMMON_REGEX = "^common.*";
    /**
     * COMMON ERROR
     */
    public static final String KEY_COMMON_ERR_INVALID_USER = "common.err.invalid.user";
    public static final String KEY_COMMON_ERR_ATTACHMENT_MISSED = "common.err.attachment.missed";
    public static final String KEY_COMMON_ERR_INTERNAL_SERVER = "common.err.internal.server";
    public static final String KEY_COMMON_ERR_INVALID_UNRECONGNIZED = "common.err.invalid.parameter.unrecongnized";
    public static final String KEY_COMMON_ERR_INVALID_PARAMETER = "common.err.invalid.parameter";
    public static final String KEY_COMMON_ERR_DATA_NOTEXISTED = "common.err.data.notexisted";
    public static final String KEY_COMMON_ERR_EXCEED_MAX_FILESIZE = "common.err.exceed.max.filesize";
    public static final String KEY_COMMON_ERR_INVALIDUSER = "common.err.invaliduser";
    public static final String KEY_COMMON_ERR_BADPARAM = "common.err.badparam";
    public static final String KEY_COMMON_ERR_NOTFINDQUESTION = "common.err.notfindquestion";
    public static final String KEY_COMMON_ERR_NOFINDCOUNTTRY = "common.err.notfindcountry";
    public static final String KEY_COMMON_ERR_INVALID_ANSWER_VALUE = "common.err.invalid.answer.value";
    public static final String KEY_COMMON_ERR_BADPARAM_SECION = "common.err.badparam.section";
    public static final String KEY_COMMON_ERR_BADPARAM_SOURCE = "common.err.badparam.source";
    public static final String KEY_COMMON_ERR_NOTFINDANSWER = "common.err.notfind.answer";
    public static final String KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR = "common.err.notfind.surveyindicator";
    public static final String KEY_COMMON_ERR_NOTFINDREF = "common.err.notfind.ref";
    public static final String KEY_COMMON_ERR_PROJECT_CONTRIBUTOR_EXISTS = "common.err.project.contributor.exists";
    public static final String KEY_COMMON_ERR_CONTRIBUTOR_STILL_HAS_TASK = "common.err.controbutor.has.task";
    public static final String KEY_COMMON_ERR_TASK_ASSIGNMENT_NOT_EXIST = "common.err.task_assignment.not_exist";
    public static final String KEY_COMMON_ERR_TASK_ASSIGNMENT_INFLIGHT = "common.err.task_assignment.inflight";
    public static final String KEY_COMMON_ERR_TASK_ASSIGNMENT_DONE = "common.err.task_assignment.done";
    public static final String KEY_COMMON_ERR_PROD_NO_OR_CONFIG = "common.err.product.not_exist_or_config_mode";
    public static final String KEY_COMMON_ERR_HORSE_NOT_EXIST = "common.err.horse.not_exist";
    public static final String KEY_COMMON_ERR_NO_PERMISSION_UPDATE_QUEUE = "common.err.nopermission_update_queue";
    public static final String KEY_COMMON_ERR_NOT_EXISTS_TA = "common.err.not_exists_ta";
    public static final String KEY_COMMON_ERR_ONLLY_ONE_TA = "common.err.only_one_ta";
    public static final String KEY_COMMON_ERR_ALREADY_EXISTS_TA = "common.err.already_exists_ta";
    public static final String KEY_COMMON_JS_NO_ONE = "common.js.no_one";
    public static final String KEY_COMMON_ERR_CANT_SUBMIT_TA_FLAGS_ASSIGNED = "common.err.cant_submit_ta.flags_assigned";
    public static final String KEY_COMMON_ERR_CANT_SUBMIT_TA_FLAGS_RAISED = "common.err.cant_submit_ta.flags_raised";


    /**
     * COMMON MESSAGE
     */
    //public static final String KEY_COMMON_MSG_EVENT_LOGIN = "common.msg.event.login";
    //public static final String KEY_COMMON_MSG_EVENT_LOGOUT = "common.msg.event.logout";
    //public static final String KEY_COMMON_MSG_EVENT_TASK_CLICK = "common.msg.event.task.click";
    //public static final String KEY_COMMON_MSG_EVENT_TASK_SUBMIT = "common.msg.event.task.submit";
    //public static final String KEY_COMMON_MSG_EVENT_TASK_COMPLETE = "common.msg.event.task.complete";
    public static final String KEY_COMMON_MSG_NEXT_STEP = "common.msg.nextstep";
    public static final String KEY_COMMON_MSG_NO_ASSIGN = "common.msg.noassign";
    public static final String KEY_COMMON_MSG_NODATA = "common.msg.nodata";
    public static final String KEY_COMMON_MSG_TEAM_CONTENT = "common.label.teamcontent";
    public static final String KEY_COMMON_MSG_PRODUCT = "common.msg.product";
    public static final String KEY_COMMON_MSG_TARGET = "common.msg.target";
    public static final String KEY_COMMON_MSG_STATUS = "common.label.status";//"common.msg.status";
    //public static final String KEY_COMMON_MSG_FIREUSERFINDER_SUMMARY = "common.msg.fireuserfinder.summary";
    public static final String KEY_COMMON_MSG_NOPROJECT = "common.msg.noproject";
    public static final String KEY_COMMON_MSG_VALIDITYUSER = "common.label.validityuser";
    public static final String KEY_COMMON_MSG_FNAME_ANONYMOUS = "common.msg.fname.anonymous";
    public static final String KEY_COMMON_MSG_LNAME_CONTRIBUTOR = "common.msg.lname.contributor";
    public static final String KEY_COMMON_JS_MSG_END_ASSIGNMENT = "common.js.msg.exitassignment";
    //
    public static final String KEY_COMMON_CONTENT = "jsp.queues.content";
    public static final String KEY_COMMON_LABEL_YOU = "common.label.you";
    public static final String KEY_COMMON_LABEL_WILLDO = "common.label.willdo";
    public static final String KEY_COMMON_MSG_TASKSTATUS_DONE = "common.msg.taskstatus.done";
    public static final String KEY_COMMON_MSG_TASKSTATUS_INFLIGHT = "common.msg.taskstatus.inflight";
    public static final String KEY_COMMON_MSG_TASKSTATUS_INACTIVE = "common.msg.taskstatus.inactive";
    public static final String KEY_COMMON_MSG_TASKSTATUS_OVERDUE = "common.msg.taskstatus.overdue";
    public static final String KEY_COMMON_MSG_TASKSTATUS_FORCED_EXIT = "common.msg.taskstatus.forcedexit";
    public static final String KEY_COMMON_MSG_TASKSTATUS_UNASSIGNED = "common.msg.taskstatus.unassigned";
    /**
     * COMMON ALERT
     */
    public static final String KEY_COMMON_ALERT_TASK_DONE = "common.alert.task.done";
    public static final String KEY_COMMON_ALERT_TASK_SAVE = "common.alert.task.save";
    public static final String KEY_COMMON_ALERT_ADDCASENOTE_SUCCESS = "common.alert.addcasenotes.success";
    public static final String KEY_COMMON_ALERT_ADDCASENOTE_FAILL = "common.alert.addcasenotes.fail";
    public static final String KEY_COMMON_ALERT_CREATECASE_SUCCESS = "common.alert.createcase.success";
    public static final String KEY_COMMON_ALERT_CREATECASE_FAIL = "common.alert.createcase.fail";
    public static final String KEY_COMMON_ALERT_FIREUSERFINDER_SUCCESS = "common.alert.fireuserfinder.success";
    public static final String KEY_COMMON_ALERT_FIREUSERFINDER_FAIL = "common.alert.fireuserfinder.fail";
    public static final String KEY_COMMON_ALERT_CHANGEPASSWD_SUCCESS = "common.alert.chngpasswd.success";
    public static final String KEY_COMMON_ALERT_CHANGEPASSWD_FAIL = "common.alert.chngpasswd.fail";
    public static final String KEY_COMMON_ALERT_SURVEYANSWER_SUCCESS = "common.alert.surveyanswer.success";
    public static final String KEY_COMMON_ALERT_SURVEYANSWER_FAIL = "common.alert.surveyanswer.fail";
    public static final String KEY_COMMON_ALERT_UPDATECASE_SUCCESS = "common.alert.upcatecase.success";
    public static final String KEY_COMMON_ALERT_UPDATECASE_FAIL = "common.alert.upcatecase.fail";
    /**
     * COMMON REMARK
     */
    public static final String KEY_COMMON_REMARK_SUSPENDED = "common.remark.suspended";
    public static final String KEY_COMMON_REMARK_INACTIVE = "common.remark.inactive";
    public static final String KEY_COMMON_REMARK_ACTIVE = "common.remark.active";
    public static final String KEY_COMMON_REMARK_AWARE = "common.remark.aware";
    public static final String KEY_COMMON_REMARK_STARTED = "common.remark.started";
    public static final String KEY_COMMON_REMARK_DONE = "common.remark.done";
    /**
     * COMMON TH(TABLE HEAD)
     */
    public static final String KEY_COMMON_TH_STATUS = "common.label.status";
    public static final String KEY_COMMON_TH_VIEW = "common.label.view";
    public static final String KEY_COMMON_TH_HISTORY = "common.label.history";
    public static final String KEY_COMMON_TH_GOAL = "common.label.goal";
    public static final String KEY_COMMON_TH_ESTIMATE = "common.label.estimate";
    public static final String KEY_COMMON_TH_DONE_PERCENT = "common.label.donepercent";
    public static final String KEY_COMMON_TH_NEXTDUE = "common.label.nextdue";
    public static final String KEY_COMMON_TH_OPENCASES = "common.label.opencases";
    public static final String KEY_COMMON_TH_PEOPLEASSIGNED = "common.label.peopleassigned";
    public static final String KEY_COMMON_TH_DEADLINE = "common.label.deadline";
    public static final String KEY_COMMON_TH_TASKSTATUS = "common.label.taskStatus";
    public static final String KEY_COMMON_TH_COMPLETEPERCENTAGE = "common.label.completepercentage";
    public static final String KEY_COMMON_TH_CASE = "common.label.case";
    public static final String KEY_COMMON_TH_TITLE = "common.label.title";
    public static final String KEY_COMMON_TH_PRIORITY = "common.label.priority";
    public static final String KEY_COMMON_TH_TAGS = "common.label.tags";
    public static final String KEY_COMMON_TH_OWNER = "common.label.owner";
    public static final String KEY_COMMON_TH_ATTACHEDCONTNET = "common.label.attachedcontent";
    public static final String KEY_COMMON_TH_NAME = "common.label.name";
    public static final String KEY_COMMON_TH_ROLE = "common.label.role";
    public static final String KEY_COMMON_TH_COUNTRY = "common.label.country";
    public static final String KEY_COMMON_TH_TEAMS = "common.label.teams";
    public static final String KEY_COMMON_TH_ASSIGNEDCONTENT = "common.label.assignedcontent";
    /**
     * COMMON ALT('alt' attribute in html img tag)
     */
    public static final String KEY_COMMON_ALT_NOTSTARTED = "common.alt.notstarted";
    public static final String KEY_COMMON_ALT_COMPLETED = "common.alt.completed";
    public static final String KEY_COMMON_ALT_INPROGRESS = "common.alt.inprogress";
    public static final String KEY_COMMON_ALT_NOTICED = "common.alt.noticed";
    public static final String KEY_COMMON_ALT_SIGNEDIN = "common.alt.signedin";
    public static final String KEY_COMMON_ALT_VIEWCONTENT = "common.alt.viewcontent";
    public static final String KEY_COMMON_ALT_HISTORYCHART = "common.alt.historychart";
    public static final String KEY_COMMON_ALT_ANSWER = "common.alt.answer";
    /**
     * COMMON CHOICE
     */
    public static final String KEY_COMMON_CHOICE_EXCLUDE = "common.choice.exclude";
    public static final String KEY_COMMON_CHOICE_INCLUDE = "common.choice.include";
    public static final String KEY_COMMON_CHOICE_ALL = "common.choice.all";
    public static final String KEY_COMMON_CHOICE_OVERDUE = "common.choice.overdue";
    public static final String KEY_COMMON_CHOICE_NOTOVERDUE = "common.choice.notoverdue";
    /**
     * COMMON ACTION
     */
    public static final String KEY_COMMON_BUTTON_ADD = "common.btn.add";
    public static final String KEY_COMMON_BUTTON_EDIT = "common.btn.edit";
    public static final String KEY_COMMON_BUTTON_EDITDEADLINE = "common.btn.editdeadline";
    /**
     * FILTER STATUS
     */
    public static final String KEY_FILTER_STATUS_INFLIGHT = "common.label.inflight";
    public static final String KEY_FILTER_STATUS_STARTING = "common.label.starting";
    public static final String KEY_FILTER_STATUS_OVER = "common.label.over";
    public static final String KEY_FILTER_STATUS_INACTIVE = "common.label.inactive";
    /**
     * ERROR 
     */
    public static final String KEY_ERR_INVALID_VALUE = "jsp.table.err.invalid.value";
    public static final String KEY_ERR_VALUE_OUT_OF_BOUND = "jsp.table.err.value.out.of.bound";
    public static final String KEY_ERR_NOT_ENOUGH_TEXT_DATA = "jsp.table.err.text.not.enough.data";
    public static final String KEY_ERR_TOO_MUCH_TEXT_DATA = "jsp.table.err.text.too.much.data";

    /**
     * JSP QUEUES
     */
    public static final String KEY_JSP_QUEUES_IWANTTHIS = "jsp.queues.iWantThis";
    public static final String KEY_JSP_QUEUES_RETURNTOQUEUE = "jsp.queues.returnToQueue";
    public static final String KEY_JSP_QUEUES_NOASSIGN = "jsp.queues.noAssign";
    public static final String KEY_JSP_QUEUES_ASSIGNTO = "jsp.queues.assignTo";
    public static final String KEY_JSP_QUEUES_LOW = "jsp.queues.low";
    public static final String KEY_JSP_QUEUES_MEDIUM = "jsp.queues.medium";
    public static final String KEY_JSP_QUEUES_HIGH = "jsp.queues.high";

    private static Messages instance = null;
    private static ResourceManager manager = null;
    private static String intlResourceDir = null;

    private Messages() {
    }


    public static void init(String dir) {
        intlResourceDir = dir;
    }

    public static Messages getInstance() {
        if (intlResourceDir == null) return null;
        
        if (instance == null) {
            // create a new resource manager
            ResourceLoader loader = new ResourceLoader();
            ResourceManager mgr = loader.load(intlResourceDir);

            if (mgr == null) {
                List<String> errors = loader.getErrors();
                logger.fatal("Can't load Intl Resources from " + intlResourceDir);

                if (errors != null) {
                    for (String err : errors) {
                        logger.error(err);
                    }
                }
                return null;
            } else {
                logger.info("Intl resources loaded from " + intlResourceDir);
            }

            instance = new Messages();
            manager = mgr;
        }
        
        return instance;
    }

    
    public String getMessage(String key, int langId) {
        if (key == null || manager == null) return null;
        return manager.getResource(key, langId);
    }

    public String getLanguage(int languageId) {
        if (manager == null) return null;
        Language lang = manager.getLanguage(languageId);
        return (lang != null) ? lang.getName() : null;
    }

    public Map<String, String> getJSMessages(int langId) {
        if (manager == null) return null;
        return manager.getResourcesRegex(langId, KEY_COMMON_REGEX);
    }

    public Map<String, String> getMessagesWithRegexKey(int langId, String regex) {
        if (manager == null) return null;
        return manager.getResourcesRegex(langId, regex);
    }

}
