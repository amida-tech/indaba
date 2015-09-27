/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import static com.ocs.indaba.action.BaseAction.PARAM_HORSE_ID;
import static com.ocs.indaba.action.BaseAction.PARAM_QUESTION_ID;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.CommPanelService;
import com.ocs.indaba.vo.FlagDetail;
import com.ocs.indaba.vo.FlagResponseDestination;
import com.ocs.indaba.vo.FlagWorkView;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.GroupActionResult;
import com.ocs.indaba.vo.GroupContentResult;
import com.ocs.indaba.vo.GroupMembershipResult;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.NoteActionResult;
import com.ocs.indaba.vo.NoteTextResult;
import com.ocs.indaba.vo.NoteobjPage;
import com.ocs.util.DateUtils;
import com.ocs.util.JSONUtils;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class CommPanelAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CommPanelAction.class);
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_NOTEOBJID = "noteobjId";
    private static final String PARAM_LANGID = "langId";
    private static final String PARAM_TASK_TYPE = "taskType";
    private static final String PARAM_GROUPOBJ_ID = "groupobjId";
    private static final String PARAM_TASKASSIGNMENT_ID = "taskAssignmentId";
    private static final String PARAM_COMMENT_ID = "commentId";
    private static final String PARAM_TIMESTAMP = "ts";
    private static final String PARAM_CANMANAGECOMMENTS = "canManageComments";
    private static final String PARAM_ISSUE_DESCRIPTION = "issueDescription";
    private static final String PARAM_ASSIGNED_USERID = "assignedUserId";
    private static final String PARAM_DUE_TIME = "dueTime";
    private static final String PARAM_FLAG_ID = "flagId";
    private static final String PARAM_WHAT = "what";
    private static final String PARAM_CONTENTVERSION_ID = "contentVersionId";
    private static final String PARAM_NOTEOBJVERSIONID = "noteobjVersionId";
    private static final String ACTION_GET_NOTEOBJS = "getNoteobjs";
    private static final String ACTION_GET_NOTEOBJVERSIONS = "getNoteobjVersions";
    private static final String ACTION_GET_NOTEOBJ = "getNoteobj";
    private static final String ACTION_GET_NOTEOBJVERSION = "getNoteobjVersion";
    private static final String ACTION_SAVE_NOTEOBJ = "saveNoteobj";
    private static final String ACTION_GET_NOTEOBJ_MEMBERS = "getNoteobjMembers";
    private static final String ACTION_GET_GROUP_SUMMARY = "getGroupSummary";
    private static final String ACTION_GET_GROUP_COMMENTS = "getGroupComments";
    private static final String ACTION_HIDE_COMMENT = "hideComment";
    private static final String ACTION_UNHIDE_COMMENT = "unhideComment";
    private static final String ACTION_GET_GROUP_MEMBERS = "getGroupMembers";
    private static final String ACTION_SAVE_FLAG_RESPONSE = "saveFlagResponse";
    private static final String ACTION_SAVE_REGULAR_COMMENT = "saveRegularComment";
    private static final String ACTION_RAISE_FLAG = "raiseFlag";
    private static final String ACTION_UNSET_FLAG = "unsetFlag";
    private static final String ACTION_REASSIGN_FLAG = "reassignFlag";
    private static final String ACTION_GET_ACTIVE_FLAGS_RAISED_BY_ME = "getActiveFlagsRaisedByMe";
    private static final String ACTION_GET_ACTIVE_FLAGS_ASSIGNED_TO_ME = "getActiveFlagsAssignedToMe";
    private static final String ACTION_GET_ACTIVE_FLAGS_OTHER = "getActiveFlagsOther";
    private static final String ACTION_GET_NEW_FLAG_INFO = "getNewFlagInfo";
    private static final String ACTION_GET_FLAG_INFO = "getFlagInfo";
    private static final String ACTION_GET_FLAG_RESPONSE_DESTINATION = "getFlagResponseDestination";
    private CommPanelService commPanelService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionFwd = null;
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);
        String action = request.getParameter(PARAM_ACTION);
        logger.debug("CommPanelAction -> " + action);
        if (ACTION_GET_NOTEOBJS.equals(action)) {
            actionFwd = getNoteobjs(loginUser, request, response);
        } else if (ACTION_GET_NOTEOBJVERSIONS.equals(action)) {
            actionFwd = getNoteobjVersions(loginUser, request, response);
        } else if (ACTION_GET_NOTEOBJ.equals(action)) {
            actionFwd = getNoteobj(loginUser, request, response);
        } else if (ACTION_GET_NOTEOBJVERSION.equals(action)) {
            actionFwd = getNoteobjVersion(loginUser, request, response);
        } else if (ACTION_SAVE_NOTEOBJ.equals(action)) {
            actionFwd = saveNoteobj(loginUser, request, response);
        } else if (ACTION_GET_NOTEOBJ_MEMBERS.equals(action)) {
            actionFwd = getNoteobjMembers(loginUser, request, response);
        } else if (ACTION_GET_GROUP_SUMMARY.equals(action)) {
            actionFwd = getGroupSummary(loginUser, request, response);
        } else if (ACTION_GET_GROUP_COMMENTS.equals(action)) {
            actionFwd = getGroupComments(loginUser, request, response);
        } else if (ACTION_HIDE_COMMENT.equals(action)) {
            actionFwd = hideComment(loginUser, request, response);
        } else if (ACTION_UNHIDE_COMMENT.equals(action)) {
            actionFwd = unhideComment(loginUser, request, response);
        } else if (ACTION_GET_GROUP_MEMBERS.equals(action)) {
            actionFwd = getGroupMembers(loginUser, request, response);
        } else if (ACTION_RAISE_FLAG.equals(action)) {
            actionFwd = raiseFlag(loginUser, request, response);
        } else if (ACTION_UNSET_FLAG.equals(action)) {
            actionFwd = unsetFlag(loginUser, request, response);
        } else if (ACTION_REASSIGN_FLAG.equals(action)) {
            actionFwd = reassignFlag(loginUser, request, response);
        } else if (ACTION_GET_ACTIVE_FLAGS_RAISED_BY_ME.equals(action)) {
            actionFwd = getActiveFlagsRaisedByMe(loginUser, request, response);
        } else if (ACTION_GET_ACTIVE_FLAGS_ASSIGNED_TO_ME.equals(action)) {
            actionFwd = getActiveFlagsAssignedToMe(loginUser, request, response);
        } else if (ACTION_GET_ACTIVE_FLAGS_OTHER.equals(action)) {
            actionFwd = getActiveFlagsOther(loginUser, request, response);
        } else if (ACTION_SAVE_FLAG_RESPONSE.equals(action)) {
            actionFwd = saveFlagResponse(loginUser, request, response);
        } else if (ACTION_SAVE_REGULAR_COMMENT.equals(action)) {
            actionFwd = saveRegularComment(loginUser, request, response);
        } else if (ACTION_GET_NEW_FLAG_INFO.equals(action)) {
            actionFwd = getNewFlagInfo(loginUser, request, response);
        } else if (ACTION_GET_FLAG_INFO.equals(action)) {
            actionFwd = getFlagInfo(loginUser, request, response);
        } else if (ACTION_GET_FLAG_RESPONSE_DESTINATION.equals(action)) {
            actionFwd = getFlagResponseDestination(loginUser, request, response);
        } else {
            logger.info("Unsupported commPanel action: " + action);
        }

        return actionFwd;
    }

    public ActionForward getNoteobjs(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int questionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID));
        logger.debug("Parameters: " + "\n\t horseId=" + horseId + "\n\t questionId=" + questionId);
        NoteobjPage noteobjPage = commPanelService.getNoteobjs(loginUser, horseId, questionId);
        try {
            String resultJson = JSONUtils.toJsonString(noteobjPage);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getNoteobjVersions(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int questionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID));
        int contentVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTENTVERSION_ID));
        logger.debug("Parameters: " + "\n\t horseId=" + horseId + "\n\t contentVersionId=" + contentVersionId + "\n\t questionId=" + questionId);
        NoteobjPage noteobjPage = commPanelService.getNoteobjVersions(loginUser, contentVersionId, horseId, questionId);
        try {
            String resultJson = JSONUtils.toJsonString(noteobjPage);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getNoteobj(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int noteobjId = StringUtils.str2int(request.getParameter(PARAM_NOTEOBJID));
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANGID));
        logger.debug("Parameters: " + "\n\t noteobjId=" + noteobjId + "\n\t langId=" + langId);
        NoteActionResult result = commPanelService.getNoteobj(loginUser, noteobjId, langId);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getNoteobjVersion(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int noteobjVersionId = StringUtils.str2int(request.getParameter(PARAM_NOTEOBJID));
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANGID));
        logger.debug("Parameters: " + "\n\t noteobjVersionId=" + noteobjVersionId + "\n\t langId=" + langId);
        NoteActionResult result = commPanelService.getNoteobjVersion(loginUser, noteobjVersionId, langId);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward saveNoteobj(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int noteobjId = StringUtils.str2int(request.getParameter(PARAM_NOTEOBJID));
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANGID));
        String text = request.getParameter(PARAM_TEXT);
        logger.debug("Parameters: " + "\n\t noteobjId=" + noteobjId + "\n\t langId=" + langId + "\n\t text=" + text);
        NoteTextResult result = commPanelService.saveNoteobj(loginUser, noteobjId, langId, text);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getNoteobjMembers(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int noteobjId = StringUtils.str2int(request.getParameter(PARAM_NOTEOBJID));
        logger.debug("Parameters: " + "\n\t noteobjId=" + noteobjId);
        NoteActionResult result = commPanelService.getNoteobjMembers(loginUser, noteobjId);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getGroupSummary(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int questionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID));
        logger.debug("Parameters: " + "\n\t taskType=" + taskType + "\n\t horseId=" + horseId + "\n\t questionId=" + questionId);
        GroupActionResult result = commPanelService.getGroupSummary(loginUser, taskType, horseId, questionId);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getGroupComments(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        logger.debug("Parameters: " + "\n\t taskType=" + taskType + "\n\t groupobjId=" + groupobjId + "\n\t timestamp=" + timestamp);
        GroupContentResult result = commPanelService.getGroupComments(loginUser, taskType, groupobjId, timestamp, canManageComments);
        try {
            String jsonStr = JSONUtils.toJsonString(result);
            logger.debug("Result: " + jsonStr);
            super.writeMsgLnUTF8(response, jsonStr);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward hideComment(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        int commentId = StringUtils.str2int(request.getParameter(PARAM_COMMENT_ID));
        String tsStr = request.getParameter(PARAM_TIMESTAMP);
        long timestamp = StringUtils.str2long(tsStr);
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        logger.debug("Parameters: " + "\n\t taskType=" + taskType + "\n\t groupobjId=" + groupobjId + "\n\t commentId=" + commentId + "\n\t timestamp=" + timestamp);
        GroupContentResult result = commPanelService.hideComment(loginUser, taskType, groupobjId, commentId, timestamp);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward unhideComment(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        int commentId = StringUtils.str2int(request.getParameter(PARAM_COMMENT_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        logger.debug("Parameters: " + "\n\t taskType=" + taskType + "\n\t groupobjId=" + groupobjId + "\n\t commentId=" + commentId + "\n\t timestamp=" + timestamp);
        GroupContentResult result = commPanelService.unhideComment(loginUser, taskType, groupobjId, commentId, timestamp);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getGroupMembers(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        logger.debug("Parameters: " + "\n\t groupobjId=" + groupobjId + "\n\t timestamp=" + timestamp);
        GroupMembershipResult result = commPanelService.getGroupMembers(loginUser, groupobjId, timestamp);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward raiseFlag(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_TASKASSIGNMENT_ID));
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        String issueDescription = request.getParameter(PARAM_ISSUE_DESCRIPTION);
        int assignedUserId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNED_USERID));
        String dueTimeStr = request.getParameter(PARAM_DUE_TIME);
        Date dueTime = DateUtils.parse(dueTimeStr, DateUtils.DATE_FORMAT_6);
        String[] whatStr = request.getParameterValues(PARAM_WHAT);
        int what = 0;
        if (whatStr != null && whatStr.length > 0) {
            for (String s : whatStr) {
                what |= StringUtils.str2int(s);
            }
        }
        logger.debug("Parameters: "
                + "\n\t groupobjId=" + groupobjId
                + "\n\t timestamp=" + timestamp
                + "\n\t taskAssignmentId=" + taskAssignmentId
                + "\n\t horseId=" + horseId
                + "\n\t taskType=" + taskType
                + "\n\t canManageComments=" + canManageComments
                + "\n\t issueDescription=" + issueDescription
                + "\n\t assignedUserId=" + assignedUserId
                + "\n\t dueTimeStr=" + dueTimeStr);
        GroupContentResult result = commPanelService.raiseFlag(loginUser, horseId, assignedUserId, taskType, groupobjId, timestamp, canManageComments, issueDescription, assignedUserId, dueTime, what);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward unsetFlag(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        //long taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_TASKASSIGNMENT_ID));
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        String issueDescription = request.getParameter(PARAM_ISSUE_DESCRIPTION);
        // int assignedUserId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNED_USERID));
        int flagId = StringUtils.str2int(request.getParameter(PARAM_FLAG_ID));

        logger.debug("Parameters: "
                + "\n\t groupobjId=" + groupobjId
                + "\n\t timestamp=" + timestamp
                //+ "\n\t taskAssignmentId=" + taskAssignmentId
                + "\n\t horseId=" + horseId
                + "\n\t taskType=" + taskType
                + "\n\t canManageComments=" + canManageComments
                + "\n\t issueDescription=" + issueDescription
                // + "\n\t assignedUserId=" + assignedUserId
                + "\n\t flagId=" + flagId);
        GroupContentResult result = commPanelService.unsetFlag(loginUser, horseId, taskType, groupobjId, timestamp, flagId, issueDescription, canManageComments);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward reassignFlag(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_TASKASSIGNMENT_ID));
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        String issueDescription = request.getParameter(PARAM_ISSUE_DESCRIPTION);
        int assignedUserId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNED_USERID));
        int flagId = StringUtils.str2int(request.getParameter(PARAM_FLAG_ID));
        String dueTimeStr = request.getParameter(PARAM_DUE_TIME);
        Date dueTime = DateUtils.parse(dueTimeStr, DateUtils.DATE_FORMAT_6);
        String[] whatStr = request.getParameterValues(PARAM_WHAT);
        int what = 0;
        if (whatStr != null && whatStr.length > 0) {
            for (String s : whatStr) {
                what |= StringUtils.str2int(s);
            }
        }
        logger.debug("Parameters: "
                + "\n\t groupobjId=" + groupobjId
                + "\n\t timestamp=" + timestamp
                + "\n\t taskAssignmentId=" + taskAssignmentId
                + "\n\t horseId=" + horseId
                + "\n\t taskType=" + taskType
                + "\n\t canManageComments=" + canManageComments
                + "\n\t issueDescription=" + issueDescription
                + "\n\t assignedUserId=" + assignedUserId
                + "\n\t flagId=" + flagId
                + "\n\t dueTimeStr=" + dueTimeStr);
        GroupContentResult result = commPanelService.reassignFlag(loginUser, horseId, assignedUserId, taskType, groupobjId, timestamp, canManageComments, flagId, issueDescription, assignedUserId, dueTime, what);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getActiveFlagsRaisedByMe(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        logger.debug("Parameters: " + "\n\t horseId=" + horseId);
        List<FlagWorkView> flags = commPanelService.getActiveFlagsRaisedByMe(loginUser, horseId);
        try {
            String resultJson = JSONUtils.toJsonString(JSONUtils.listToJson(flags));
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getActiveFlagsAssignedToMe(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        logger.debug("Parameters: " + "\n\t horseId=" + horseId);
        List<FlagWorkView> flags = commPanelService.getActiveFlagsAssignedToMe(loginUser, horseId);
        try {
            String resultJson = JSONUtils.toJsonString(JSONUtils.listToJson(flags));
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getActiveFlagsOther(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        logger.debug("Parameters: " + "\n\t horseId=" + horseId);
        List<FlagWorkView> flags = commPanelService.getActiveFlagsOther(loginUser, horseId);
        try {
            String resultJson = JSONUtils.toJsonString(JSONUtils.listToJson(flags));
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward saveRegularComment(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        String text = request.getParameter(PARAM_TEXT);
        logger.debug("Parameters: "
                + "\n\t groupobjId=" + groupobjId
                + "\n\t timestamp=" + timestamp
                + "\n\t taskType=" + taskType
                + "\n\t canManageComments=" + canManageComments
                + "\n\t text=" + text);
        GroupContentResult result = commPanelService.saveRegularComment(loginUser, taskType, groupobjId, timestamp, text, canManageComments);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward saveFlagResponse(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int taskType = StringUtils.str2int(request.getParameter(PARAM_TASK_TYPE), Constants.TASK_TYPE_SURVEY_VIEW);
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        int flagId = StringUtils.str2int(request.getParameter(PARAM_FLAG_ID));
        long timestamp = StringUtils.str2long(request.getParameter(PARAM_TIMESTAMP));
        boolean canManageComments = StringUtils.str2boolean(request.getParameter(PARAM_CANMANAGECOMMENTS));
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));

        String text = request.getParameter(PARAM_TEXT);
        logger.debug("Parameters: "
                + "\n\t horseId=" + horseId
                + "\n\t groupobjId=" + groupobjId
                + "\n\t flagId=" + flagId
                + "\n\t timestamp=" + timestamp
                + "\n\t taskType=" + taskType
                + "\n\t canManageComments=" + canManageComments
                + "\n\t text=" + text);
        GroupContentResult result = commPanelService.saveFlagResponse(loginUser, horseId, taskType, groupobjId, flagId, timestamp, text, canManageComments);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getNewFlagInfo(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_TASKASSIGNMENT_ID));
        logger.debug("Parameters: " + "\n\t groupobjId=" + groupobjId + "\n\t horseId=" + horseId);
        FlagDetail result = commPanelService.getNewFlagInfo(loginUser, horseId, groupobjId, taskAssignmentId);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }

    public ActionForward getFlagInfo(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        int flagId = StringUtils.str2int(request.getParameter(PARAM_FLAG_ID));
        logger.debug("Parameters: " + "\n\t groupobjId=" + groupobjId + "\n\t horseId=" + horseId + "\n\t flagId=" + flagId);
        FlagDetail result = commPanelService.getFlagInfo(loginUser, horseId, groupobjId, flagId);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }


    public ActionForward getFlagResponseDestination(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID));
        int groupobjId = StringUtils.str2int(request.getParameter(PARAM_GROUPOBJ_ID));
        int flagId = StringUtils.str2int(request.getParameter(PARAM_FLAG_ID));
        logger.debug("Parameters: " + "\n\t groupobjId=" + groupobjId + "\n\t horseId=" + horseId + "\n\t flagId=" + flagId);
        FlagResponseDestination result = commPanelService.getFlagResponseDestination(loginUser, horseId, groupobjId, flagId);
        try {
            String resultJson = JSONUtils.toJsonString(result);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }


    @Autowired
    public void setCommPanelService(CommPanelService commPanelService) {
        this.commPanelService = commPanelService;
    }
}
