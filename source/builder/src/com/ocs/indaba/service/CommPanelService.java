/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.dao.GoalObjectDAO;
import com.ocs.indaba.dao.GroupdefDAO;
import com.ocs.indaba.dao.GroupdefRoleDAO;
import com.ocs.indaba.dao.GroupdefUserDAO;
import com.ocs.indaba.dao.GroupobjCommentDAO;
import com.ocs.indaba.dao.GroupobjDAO;
import com.ocs.indaba.dao.GroupobjFlagDAO;
import com.ocs.indaba.dao.LanguageDAO;
import com.ocs.indaba.dao.NotedefDAO;
import com.ocs.indaba.dao.NotedefRoleDAO;
import com.ocs.indaba.dao.NotedefUserDAO;
import com.ocs.indaba.dao.NoteobjDAO;
import com.ocs.indaba.dao.NoteobjIntlDAO;
import com.ocs.indaba.dao.NoteobjVersionDAO;
import com.ocs.indaba.dao.NoteobjVersionIntlDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.TargetDAO;
import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.dao.WorkflowObjectDAO;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.po.Groupdef;
import com.ocs.indaba.po.GroupdefRole;
import com.ocs.indaba.po.GroupdefUser;
import com.ocs.indaba.po.Groupobj;
import com.ocs.indaba.po.GroupobjComment;
import com.ocs.indaba.po.GroupobjFlag;
import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.Notedef;
import com.ocs.indaba.po.NotedefRole;
import com.ocs.indaba.po.NotedefUser;
import com.ocs.indaba.po.Noteobj;
import com.ocs.indaba.po.NoteobjIntl;
import com.ocs.indaba.po.NoteobjVersion;
import com.ocs.indaba.po.NoteobjVersionIntl;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ProjectAdmin;
import com.ocs.indaba.po.ProjectMembership;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.User;
import com.ocs.indaba.po.WorkflowObject;
import com.ocs.indaba.service.ViewPermissionService.ViewPermissionCtx;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.vo.CommMemberInfo;
import com.ocs.indaba.vo.FlagDetail;
import com.ocs.indaba.vo.FlagNavView;
import com.ocs.indaba.vo.FlagResponseDestination;
import com.ocs.indaba.vo.FlagWorkView;
import com.ocs.indaba.vo.GroupActionResult;
import com.ocs.indaba.vo.GroupComment;
import com.ocs.indaba.vo.GroupContentResult;
import com.ocs.indaba.vo.GroupMembershipResult;
import com.ocs.indaba.vo.GroupobjSummary;
import com.ocs.indaba.vo.GroupobjUIState;
import com.ocs.indaba.vo.GroupobjView;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.NoteActionResult;
import com.ocs.indaba.vo.NoteMembershipResult;
import com.ocs.indaba.vo.NoteTextResult;
import com.ocs.indaba.vo.NoteobjPage;
import com.ocs.indaba.vo.NoteobjView;
import com.ocs.indaba.vo.TaskAssignmentStatusData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class CommPanelService {

    private static final Logger logger = Logger.getLogger(CommPanelService.class);

    private static final int ALL_PERMISSIONS = 0x00FFFFFF;
    private static final int NO_PERMISSIONS = -1;
    private static final int ACCESS_PERMISSIONS = 0;

    private static final String MSG_USER_SESSION_EXPIRED = "You must log in to access this function.";

    private static final String MSG_INVALID_PROJECT = "comm.panel.msg.invalid.project";
    private static final String MSG_USER_NOT_IN_PROJECT = "comm.panel.msg.user.not.in.project";
    private static final String MSG_BAD_NOTEOBJ = "comm.panel.msg.bad.noteobj";
    private static final String MSG_BAD_LANGUAGE = "comm.panel.msg.bad.language";
    private static final String MSG_NOTE_REMOVED = "comm.panel.msg.note.removed";
    private static final String MSG_NOTE_DISABLED = "comm.panel.msg.note.disabled";
    private static final String MSG_GROUP_REMOVED = "comm.panel.msg.group.removed";
    private static final String MSG_GROUP_DISABLED = "comm.panel.msg.group.disabled";
    private static final String MSG_NO_GROUP_ACCESS = "comm.panel.msg.no.group.access";
    private static final String MSG_FLAG_STANDING = "comm.panel.msg.flag.standing";
    private static final String MSG_BAD_FLAG = "comm.panel.msg.bad.flag";
    private static final String MSG_BAD_ASSIGNED_USER = "comm.panel.msg.bad.assigned.user";
    private static final String MSG_FLAG_ALREADY_UNSET = "comm.panel.msg.flag.already.unset";
    private static final String MSG_WRONG_FLAG_GROUP = "comm.panel.msg.wrong.flag.group";
    private static final String MSG_NO_ACTIVE_GOAL = "comm.panel.msg.no.active.goal";
    private static final String MSG_HORSE_COMPLETED = "comm.panel.msg.horse.completed";
    private static final String MSG_FLAG_NO_LONGER_ACTIVE = "comm.panel.msg.flag.no.longer.active";
    private static final String MSG_FLAG_REASSIGNED = "comm.panel.msg.flag.reassigned";
    private static final String MSG_FLAG_ALREADY_RESPONDED = "comm.panel.msg.already.responded";

    private static boolean PA_HAS_SPECIAL_RIGHTS = false;

    @Autowired
    private NotedefDAO notedefDao = null;

    @Autowired
    private NoteobjDAO noteobjDao = null;

    @Autowired
    private NotedefUserDAO notedefUserDao = null;

    @Autowired
    private NotedefRoleDAO notedefRoleDao = null;

    @Autowired
    private UserDAO userDao = null;

    @Autowired
    private LanguageDAO langDao = null;

    @Autowired
    private ProjectDAO projDao = null;

    @Autowired
    private NoteobjIntlDAO noteobjIntlDao = null;

    @Autowired
    private GroupdefDAO groupdefDao = null;

    @Autowired
    private GroupdefUserDAO groupdefUserDao = null;

    @Autowired
    private GroupdefRoleDAO groupdefRoleDao = null;

    @Autowired
    private GroupobjDAO groupobjDao = null;

    @Autowired
    GroupobjFlagDAO groupobjFlagDao = null;

    @Autowired
    GroupobjCommentDAO groupobjCommentDao = null;

    @Autowired
    private TaskAssignmentDAO taskAssignmentDao = null;

    @Autowired
    private GoalObjectDAO goalObjDao = null;

    @Autowired
    private TargetDAO targetDao = null;

    @Autowired
    private WorkflowObjectDAO wfoDao = null;

    @Autowired
    private NoteobjVersionDAO noteobjVersionDao = null;

    @Autowired
    private NoteobjVersionIntlDAO noteobjVersionIntlDao = null;

    @Autowired
    private ProjectService projService = null;

    @Autowired
    private ViewPermissionService viewPermissionService = null;

    @Autowired
    private SurveyConfigService surveyConfigService = null;


    public static void setPaHasSpecialRight(boolean hasSpecialRight) {
        PA_HAS_SPECIAL_RIGHTS = hasSpecialRight;
    }


    /*
     * getNoteobjs
     *
     * Use this method to retrieve the note objects applicable to the indicator detail page identified by horseId and surveyQuestionId.
     * The returned NoteobjPage contains all data needed to render the note objects on the indicator detail page.
     *
     * System Processing:
     *
     * - For each notedef, create noteobj in DB based on the horseId and surveyQuestionId, if not existent already;
     * - For each noteobj, includes NoteobjText for each supported language. If not existent, create a fake one with empty text.
     * - Determine members for each noteobj.
     */

    public NoteobjPage getNoteobjs(LoginUser user, int horseId, int surveyQuestionId) {
        NoteobjPage result = new NoteobjPage();
        if (!checkNoteState(user, 0, result)) return result;

        // get all enabled notedefs
        List<Notedef> defs = notedefDao.selectEnabledNotedefsByHorseId(horseId);
        if (defs == null || defs.isEmpty()) return result;

        boolean canAccessAll = (PA_HAS_SPECIAL_RIGHTS) ? isProjectAdmin(user, horseId) : false;

        // get enabled languages
        List<Language> langs = langDao.selectAllLanguages();

        // get or create noteobjs for the notedefs
        List<NoteobjView> views = new ArrayList<NoteobjView>();
        for (Notedef def : defs) {
            int permissions = (canAccessAll) ? ALL_PERMISSIONS : getNotePermissionForUser(user, def.getId(), horseId);

            if (permissions == NO_PERMISSIONS) continue;  // user has no permission to this note

            Noteobj obj = noteobjDao.getOrCreate(def.getId(), horseId, surveyQuestionId);
            NoteobjView view = new NoteobjView();
            view.setName(def.getName());
            view.setDescription(def.getDescription());
            view.setObjId(obj.getId());
            view.setCanEditNote((permissions & Constants.NOTEDEF_PERMISSION_EDIT) != 0);
            view.setCanTranslateNote((permissions & Constants.NOTEDEF_PERMISSION_TRANSLATE) != 0);
            views.add(view);

            // get the default text and language
            List<NoteobjIntl> noiList = noteobjIntlDao.selectByNoteobjOrderByCreateTime(obj.getId());
            if (noiList == null || noiList.isEmpty()) {
                view.setText("");
                view.setLanguageId(0);
            } else {
                // determine the best text to show
                NoteobjIntl bestNoi = noiList.get(0);
                for (NoteobjIntl noi : noiList) {
                    if (noi.getLanguageId() == user.getLanguageId()) {
                        bestNoi = noi;
                    }
                }
                view.setText(bestNoi.getNote());
                view.setLanguageId(bestNoi.getLanguageId());
            }
            view.setLanguages(langs);
        }

        result.setNoteobjs(views);

        return result;
    }


    
    /*
     * getNoteobjMembers
     *
     * Call this method to retrieve membership info of the noteobj when the user clicks the membership button.
     * 
     */
    public NoteMembershipResult getNoteobjMembers(LoginUser user, int noteobjId) {
        NoteMembershipResult result = new NoteMembershipResult();

        if (!checkNoteState(user, noteobjId, result)) return result;

        if (noteobjId <= 0) {
            result.setCode(ErrorCode.ERR_BAD_OBJECT_ID);
            result.setMsg(user.getMessage(MSG_BAD_NOTEOBJ));
            return result;
        }

        int projId = user.getPrjid();
        Project project = projService.getProjectById(projId);

        if (project == null) {
            result.setCode(ErrorCode.ERR_INVALID_PARAM);
            result.setState(NoteActionResult.NOTE_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_INVALID_PROJECT));
            return result;
        }

        ProjectMembership membership = projService.getProjectMembership(projId, user.getUid());

        if (membership == null) {
            // the user is not part of the project any more
            result.setCode(ErrorCode.ERR_NO_PERMISSION);
            result.setState(NoteActionResult.NOTE_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_USER_NOT_IN_PROJECT));
            return result;
        }

        List<Integer> uids = new ArrayList<Integer>();

        if (PA_HAS_SPECIAL_RIGHTS) {
            uids.add(project.getAdminUserId());

            // get project admins
            List<ProjectAdmin> pas = projService.getProjectAdminsOfProject(projId);
            if (pas != null && !pas.isEmpty()) {
                for (ProjectAdmin pa : pas) {
                    uids.add(pa.getUserId());
                }
            }
        }

        // get explicit users of the noteobj
        List<NotedefUser> ndus = notedefUserDao.selectByNoteobj(noteobjId);
        if (ndus != null && !ndus.isEmpty()) {
            for (NotedefUser ndu : ndus) {
                uids.add(ndu.getUserId());
            }
        }

        // get users based on roles
        List<User> users = userDao.getUsersByNoteobjBasedOnRoles(noteobjId);
        if (users != null && !users.isEmpty()) {
            for (User u : users) {
                uids.add(u.getId());
            }
        }

        List<CommMemberInfo> members = this.determineMembersInfo(projId, uids, user.getUid(), membership.getRoleId(), 0);
        result.setMembers(members);

        return result;
    }

    /*
     * saveNoteobj
     *
     * Use this method to save user-entered text for the note object:
     * - when the user enters text into the note box,
     * - when the user translates text.
     *
     * The noteobjId identifies the note object.
     * 
     */

    public NoteTextResult saveNoteobj(LoginUser user, int noteobjId, int languageId, String text) {
        NoteTextResult result = new NoteTextResult();
        if (!checkNoteState(user, noteobjId, result)) return result;

        if (noteobjId <= 0) {
            result.setCode(ErrorCode.ERR_BAD_OBJECT_ID);
            result.setMsg(user.getMessage(MSG_BAD_NOTEOBJ));
            return result;
        }

        if (languageId <= 0) {
            result.setCode(ErrorCode.ERR_LANGUAGE);
            result.setMsg(user.getMessage(MSG_BAD_LANGUAGE));
            return result;
        }

        if (text == null) text = "";

        // save the text
        noteobjIntlDao.saveText(user.getUid(), noteobjId, languageId, text);

        result.setText(null); // ask the client to keep the text
        return result;

    }


    /*
     * getNoteobj
     *
     * Use this method to get the latest data of the note object:
     *
     * The noteobjId identifies the note object.
     *
     *
     */

    public NoteTextResult getNoteobj(LoginUser user, int noteobjId, int languageId) {
        NoteTextResult result = new NoteTextResult();
        if (!checkNoteState(user, noteobjId, result)) return result;
        
        if (noteobjId <= 0) {
            result.setCode(ErrorCode.ERR_BAD_OBJECT_ID);
            result.setMsg(user.getMessage(MSG_BAD_NOTEOBJ));
            return result;
        }

        if (languageId <= 0) {
            result.setCode(ErrorCode.ERR_LANGUAGE);
            result.setMsg(user.getMessage(MSG_BAD_LANGUAGE));
            return result;
        }

        // save the text
        NoteobjIntl noi = noteobjIntlDao.selectByNoteobjAndLanguage(noteobjId, languageId);
        result.setText((noi != null) ? noi.getNote() : "");
        return result;
    }


    /*
     * getNoteobjVersions
     *
     * Use this method to retrieve the note object versions applicable to the indicator detail page identified by contentVersionId,  horseId and surveyQuestionId.
     * The returned NoteobjPage contains all data needed to render the note objects on the indicator detail page.
     *
     * System Processing:
     *
     * - For each noteobj, includes NoteobjText for each supported language.
     */

    public NoteobjPage getNoteobjVersions(LoginUser user, int contentVersionId, int horseId, int surveyQuestionId) {
        NoteobjPage result = new NoteobjPage();
        if (!checkNoteState(user, 0, result)) return result;

        // get all enabled notedefs
        List<Notedef> defs = notedefDao.selectEnabledNotedefsByHorseId(horseId);
        if (defs == null || defs.isEmpty()) return result;

        boolean canAccessAll = (PA_HAS_SPECIAL_RIGHTS) ? isProjectAdmin(user, horseId) : false;

        // get enabled languages
        List<Language> langs = langDao.selectAllLanguages();

        // get noteobj versions for the notedefs
        List<NoteobjView> views = new ArrayList<NoteobjView>();
        for (Notedef def : defs) {
            int permissions = (canAccessAll) ? ALL_PERMISSIONS : getNotePermissionForUser(user, def.getId(), horseId);

            if (permissions == NO_PERMISSIONS) continue;  // user has no permission to this note

            NoteobjVersion obj = noteobjVersionDao.getVersion(def.getId(), surveyQuestionId, contentVersionId);

            if (obj == null) continue;

            // get the default text and language
            List<NoteobjVersionIntl> noiList = noteobjVersionIntlDao.getAllVersions(obj.getId());
            if (noiList == null || noiList.isEmpty()) continue;

            // determine best text
            NoteobjVersionIntl bestNoi = noiList.get(0);
            List<Language> availableLangs = new ArrayList<Language>();

            for (NoteobjVersionIntl noi : noiList) {
                if (noi.getLanguageId() == user.getLanguageId()) {
                    bestNoi = noi;
                }
                for (Language lang : langs) {
                    if (noi.getLanguageId() == lang.getId()) {
                        availableLangs.add(lang);
                    }
                }
            }

            NoteobjView view = new NoteobjView();

            view.setText(bestNoi.getNote());
            view.setLanguageId(bestNoi.getLanguageId());
            view.setName(def.getName());
            view.setDescription(def.getDescription());
            view.setObjId(obj.getId());   // this is the noteobj_version_id
            view.setCanEditNote(false);
            view.setCanTranslateNote(false);
            view.setLanguages(availableLangs);
            views.add(view);           
        }
        
        result.setNoteobjs(views);

        return result;
    }


    /*
     * getNoteobjVersion
     *
     * Use this method to get the latest data of the note object:
     *
     * The noteobjVersionId identifies the note object version.
     *
     *
     */

    public NoteTextResult getNoteobjVersion(LoginUser user, int noteobjVersionId, int languageId) {
        NoteTextResult result = new NoteTextResult();
        if (!checkNoteState(user, 0, result)) return result;

        if (noteobjVersionId <= 0) {
            result.setCode(ErrorCode.ERR_BAD_OBJECT_ID);
            result.setMsg(user.getMessage(MSG_BAD_NOTEOBJ));
            return result;
        }

        if (languageId <= 0) {
            result.setCode(ErrorCode.ERR_LANGUAGE);
            result.setMsg(user.getMessage(MSG_BAD_LANGUAGE));
            return result;
        }

        // get the text
        NoteobjVersionIntl noi = noteobjVersionIntlDao.selectByObjAndLanguage(noteobjVersionId, languageId);
        result.setText((noi != null) ? noi.getNote() : "");
        return result;
    }


    /*
     * getGroupSummary
     *
     * Use this method to get summary information about the discussion groups for the user.
     *
     * The client calls this method when the Comm Panel is first displayed. Initially all groups are shown in closed state.
     */
    public GroupobjSummary getGroupSummary(LoginUser user, int taskType, int horseId, int surveyQuestionId) {
        GroupobjSummary result = new GroupobjSummary();

        if (!checkGroupState(user, 0, result)) return result;
        result.setTimestamp(0);

        // get all enabled groupdefs
        List<Groupdef> defs = groupdefDao.selectEnabledGroupdefsByHorseId(horseId);
        if (defs == null || defs.isEmpty()) return result;

        boolean canAccessAll = (PA_HAS_SPECIAL_RIGHTS) ? isProjectAdmin(user, horseId) : false;

        // get or create noteobjs for the notedefs
        List<GroupobjView> views = new ArrayList<GroupobjView>();
        for (Groupdef def : defs) {            
            Groupobj obj = groupobjDao.getOrCreate(def.getId(), horseId, surveyQuestionId);

            int permissions = (canAccessAll) ? ALL_PERMISSIONS : getGroupPermissionForUser(user, obj);

            if (permissions == NO_PERMISSIONS) continue;  // user has no permission to this group

            GroupobjView view = new GroupobjView();
            view.setName(def.getName());
            view.setDescription(def.getDescription());
            view.setObjId(obj.getId());
            view.setCanManageComments((permissions & Constants.GROUPDEF_PERMISSION_MANAGE) != 0);

            // since the client will call getComments immediately, we'll set default states
            GroupobjUIState uis = new GroupobjUIState();
            uis.setFlagButtonState(GroupobjUIState.FLAG_BUTTON_STATE_UNSET);
            uis.setTextBoxState(GroupobjUIState.TEXT_BOX_STATE_REGULAR);
            uis.setTitleBarState(GroupobjUIState.TITLE_BAR_STATE_NO_FLAG);
            view.setUIState(uis);
            views.add(view);
        }

        result.setGroups(views);
        return result;
    }


    /*
     * GROUP ACTIONS
     *
     * The client enables/disables group object actions based on the received GroupobjUIState data.
     *
     * When the user performs any group object action, the client calls the appropriate methods (below).
     * Depending on the actual data state in DB, the action call may or may not succeed.
     *
     * The client must update the UI based on the received result data:
     *
     * - show error message if any
     * - update the UI states (flag button, text box, and title bar).
     * - keep the received timestamp for later calls
     * - append additional comments, if any, to the comment list.
     * 
     */

    /*
     * getGroupMembers
     *
     * Call this method to retrieve membership info of the groupobj when the user clicks the membership button.
     *
     */
    public GroupMembershipResult getGroupMembers(LoginUser user, int groupobjId, long timestamp) {
        GroupMembershipResult result = new GroupMembershipResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;

        int projId = user.getPrjid();
        Project project = projService.getProjectById(projId);

        if (project == null) {
            result.setCode(ErrorCode.ERR_INVALID_PARAM);
            result.setState(NoteActionResult.NOTE_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_INVALID_PROJECT));
            return result;
        }

        ProjectMembership membership = projService.getProjectMembership(projId, user.getUid());

        if (membership == null) {
            // the user is not part of the project any more
            result.setCode(ErrorCode.ERR_NO_PERMISSION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_USER_NOT_IN_PROJECT));
            return result;
        }

        List<Integer> uids = new ArrayList<Integer>();

        if (PA_HAS_SPECIAL_RIGHTS) {
            // The primary admin user is always a member
            uids.add(project.getAdminUserId());

            // get secondary project admins - they are members of all groups
            List<ProjectAdmin> pas = projService.getProjectAdminsOfProject(projId);
            if (pas != null && !pas.isEmpty()) {
                for (ProjectAdmin pa : pas) {
                    uids.add(pa.getUserId());
                }
            }
        }

        // get explicit users of the groupobj
        List<GroupdefUser> gdus = groupdefUserDao.selectByGroupobj(groupobjId);
        if (gdus != null && !gdus.isEmpty()) {
            for (GroupdefUser gdu : gdus) {
                uids.add(gdu.getUserId());
            }
        }

        // get users based on roles
        List<User> users = userDao.getUsersByGroupobjBasedOnRoles(groupobjId);
        if (users != null && !users.isEmpty()) {
            for (User u : users) {
                uids.add(u.getId());
            }
        }

        // get users assigned to any active flags in this groupobj
        List<GroupobjFlag> flags = groupobjFlagDao.selectActiveFlagsByGroupobj(groupobjId);
        if (flags != null && !flags.isEmpty()) {
            for (GroupobjFlag flag : flags) {
                uids.add(flag.getAssignedUserId());
            }
        }

        List<CommMemberInfo> members = this.determineMembersInfo(projId, uids, user.getUid(), membership.getRoleId(), 0);
        result.setMembers(members);

        return result;
    }

    /*
     * getGroupComments
     * 
     * Retrieve comments of the specified group for the user. 
     * Client uses this method when user clicks to the open the discussion group the first time.
     * If the timestamp is specified, only comments after the specified timestamp are returned.
     * The client must send the timestamp received earlier from other protocols.
     * 
     */
    public GroupContentResult getGroupComments(LoginUser user, int taskType, int groupobjId, long timestamp, boolean canManageComments) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;
        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
        return result;
    }

    /*
     * hideComment
     *
     * Use this method when the user clicks the "hide" icon of the comment (the user must be able to manage comments).
     *
     * If successful, client must then change the "hide" icon to unhide icon.
     *
     */
    public GroupContentResult hideComment(LoginUser user, int taskType, int groupobjId, int commentId, long timestamp) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;
        groupobjCommentDao.hideComment(commentId, user.getUid());
        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, true, result);
        return result;
    }


    /*
     * unhideComment
     *
     * Use this method when the user clicks the "unhide" icon of the comment (the user must be able to manage comments).
     *
     * If successful, client must then change the icon to hide icon.
     *
     */
    public GroupContentResult unhideComment(LoginUser user, int taskType, int groupobjId, int commentId, long timestamp) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;
        groupobjCommentDao.unhideComment(commentId, user.getUid());
        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, true, result);
        return result;
    }

    /*
     * saveRegularComment
     *
     * Save a regular user comment for the specified group.
     * Client uses this method when user clicks to add a new comment when the Text Box state is TEXT_BOX_STATE_REGULAR.
     * Client also must send the timestamp received before.
     * Client must update the UI based on the result returned.
     *
     */
    public GroupContentResult saveRegularComment(LoginUser user, int taskType, int groupobjId, long timestamp, String text, boolean canManageComments) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;

        GroupobjComment comment = new GroupobjComment();
        comment.setAuthorUserId(user.getUid());
        comment.setComment(text);
        comment.setCreateTime(new Date());
        comment.setDeleteUserId(0);
        comment.setGroupobjFlagId(0);
        comment.setGroupobjId(groupobjId);
        comment.setType(Constants.GROUP_COMMENT_TYPE_REGULAR);
        groupobjCommentDao.create(comment);

        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
        return result;
    }

    /*
     * saveFlagResponse
     *
     * Save a flag response comment for the specified group.
     * Client uses this method when user clicks to add a new comment when the Text Box state is TEXT_BOX_STATE_FLAG_RESPONSE.
     * Client also must send the timestamp received before.
     * Client must update the UI based on the result returned.
     *
     * PROCESSING:
     *
     * Conditions:
     * - flag doesn't exist: error msg
     * - flag not active: treat as regular comment
     * - flag not assigned to the user: treat as regular comment
     * - flag already responded: treat as regular comment
     * - otherwise: generate flag-response comment; mark flag responded;
     *   check whether the user responded to all flags. If so, enable the SUBMIT action.
     *
     */
    public GroupContentResult saveFlagResponse(LoginUser user, int horseId, int taskType, int groupobjId, int flagId, long timestamp, String text, boolean canManageComments) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;

        GroupobjFlag flag = this.checkFlag(user, groupobjId, flagId, result);
        if (flag != null) {
            GroupobjComment comment = new GroupobjComment();
            comment.setAuthorUserId(user.getUid());
            comment.setComment(text);
            comment.setCreateTime(new Date());
            comment.setDeleteUserId(0);
            comment.setGroupobjFlagId(flagId);
            comment.setGroupobjId(groupobjId);

            if (flag.getRespondTime() == null && flag.getUnsetTime() == null && flag.getAssignedUserId() == user.getUid()) {
                flag.setRespondTime(new Date());
                groupobjFlagDao.update(flag);
                comment.setType(Constants.GROUP_COMMENT_TYPE_FLAG_RESPONSE);
                adjustFlagResponseAssignment(user.getUid(), horseId, null, true);
                this.adjustFlagUnsetAssignment(flag.getRaiseUserId(), horseId, null, false);
            } else {
                if (flag.getUnsetTime() != null) {
                    result.setMsg(user.getMessage(MSG_FLAG_ALREADY_UNSET));
                } else if (flag.getAssignedUserId() != user.getUid()) {
                    result.setMsg(user.getMessage(MSG_FLAG_REASSIGNED));
                } else {
                    result.setMsg(user.getMessage(MSG_FLAG_ALREADY_RESPONDED));
                }
                // the flag has a response already - treat it as a normal comment
                comment.setType(Constants.GROUP_COMMENT_TYPE_REGULAR);
            }
            
            groupobjCommentDao.create(comment);
            
        }

        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
        return result;
    }


    /*
     * raiseFlag
     *
     * Use this method to raise a new flag.
     * Client uses this method when the user clicks the flag button to raise a new flag.
     * The user can only do so when the flag button is in the state of FLAG_BUTTON_STATE_UNSET.
     *
     * PROCESSING:
     *
     * - user not a member of the group: error msg
     * - group already has active flag: error msg
     *
     * Check whether the assigned user already has FLAG RESPONSE assignment. If not, create assignment and notify the user.
     *
     */
    public GroupContentResult raiseFlag(LoginUser user, int horseId, int taskAssignmentId, int taskType, int groupobjId, long timestamp,
            boolean canManageComments, String issueDescription, int assignedUserId, Date dueTime, int permissions) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;

        // check for standing flag
        GroupobjFlag standingFlag = groupobjFlagDao.getStandingFlag(groupobjId);
        if (standingFlag != null) {
            result.setMsg(user.getMessage(MSG_FLAG_STANDING));
            this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
            return result;
        } 
        
        // see whether the horse is already done
        // check whether the horse if completed
        WorkflowObject wfo = wfoDao.selectWorkflowObjectByHorse(horseId);
        if (wfo == null || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_DONE || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_CANCELLED) {
            result.setMsg(user.getMessage(MSG_HORSE_COMPLETED));
            this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
            return result;
        }

        this.setFlag(user, taskType, horseId, taskAssignmentId, groupobjId, null, assignedUserId, issueDescription, dueTime, permissions, result);
        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
        return result;
    }


    /*
     * unsetFlag
     *
     * Use this method to unset the specified flag.
     *
     * Client uses this method when the user clicks the flag button to unset a new flag.
     * The user can only do so when the flag button is in the state of FLAG_BUTTON_STATE_SET_BY_USER.
     *
     */
    public GroupContentResult unsetFlag(LoginUser user, int horseId, int taskType, int groupobjId, long timestamp, int flagId, String text, boolean canManageComments) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;

        GroupobjFlag flag = this.checkFlag(user, groupobjId, flagId, result);
        if (flag != null && flag.getUnsetTime() != null) {
            result.setMsg(user.getMessage(MSG_FLAG_ALREADY_UNSET));
        } else if (flag != null) {
            flag.setUnsetComment(text);
            flag.setUnsetTime(new Date());
            flag.setUnsetUserId(user.getUid());
            groupobjFlagDao.update(flag);

            GroupobjComment comment = new GroupobjComment();
            comment.setAuthorUserId(user.getUid());
            comment.setComment(text);
            comment.setCreateTime(new Date());
            comment.setDeleteUserId(0);
            comment.setGroupobjFlagId(flagId);
            comment.setGroupobjId(groupobjId);
            comment.setType(Constants.GROUP_COMMENT_TYPE_UNSET_FLAG);
            groupobjCommentDao.create(comment);

            // Check whether the user still has any flags that are not unset
            // If not, then cancel the UNSET_FLAG task assignment
            this.adjustFlagUnsetAssignment(user.getUid(), horseId, null, true);

            // check whether the assigned user still has flag-response task
            this.adjustFlagResponseAssignment(flag.getAssignedUserId(), horseId, null, false);
        }

        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
        return result;
    }


    /*
     * reassignFlag
     *
     * Use this method to reassign a flag.
     * Client uses this method when the user clicks the flag button to reassign the flag to a user.
     * The user can only do so when the flag button is in the state of FLAG_BUTTON_STATE_SET_BY_USER.
     *
     */
    public GroupContentResult reassignFlag(LoginUser user, int horseId, int taskAssignmentId, int taskType, int groupobjId, long timestamp,
            boolean canManageComments, int flagId, String issueDescription, int assignedUserId, Date dueTime, int permissions) {
        GroupContentResult result = new GroupContentResult();
        result.setTimestamp(timestamp);
        if (!checkGroupState(user, groupobjId, result)) return result;

        GroupobjFlag flag = this.checkFlag(user, groupobjId, flagId, result);
        if (flag != null) {
            if (flag.getUnsetTime() != null) {
                // this flag is already unset - can't reassign any more
                result.setMsg(user.getMessage(MSG_FLAG_ALREADY_UNSET));
            } else {
                this.setFlag(user, taskType, horseId, taskAssignmentId, groupobjId, flag, assignedUserId, issueDescription, dueTime, permissions, result);
            }
        }

        this.getCurrentGroupComments(user, taskType, groupobjId, timestamp, canManageComments, result);
        return result;
    }


    /*
     * getNewFlagInfo
     *
     * Use this method to retrieve data needed for raising a new flag.
     * Client must get such data when user clicks to raise a new flag.
     *
     */
    public FlagDetail getNewFlagInfo(LoginUser user, int horseId, int groupobjId, int taskAssignmentId) {
        FlagDetail result = new FlagDetail();
        if (!checkGroupState(user, groupobjId, result)) return result;

        int projId = user.getPrjid();
        ProjectMembership membership = projService.getProjectMembership(projId, user.getUid());

        if (membership == null) {
            // the user is not part of the project any more
            result.setCode(ErrorCode.ERR_NO_PERMISSION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_USER_NOT_IN_PROJECT));
            return result;
        }

        // check whether the horse if completed
        WorkflowObject wfo = wfoDao.selectWorkflowObjectByHorse(horseId);
        if (wfo == null || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_DONE || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_CANCELLED) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setMsg(user.getMessage(MSG_HORSE_COMPLETED));
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
            return result;
        }

        // check whether there is already a standing flag
        GroupobjFlag standingFlag = groupobjFlagDao.getStandingFlag(groupobjId);
        if (standingFlag != null) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setMsg(user.getMessage(MSG_FLAG_STANDING));
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
            return result;
        }

        TaskAssignment refTa = this.getReferenceTaskAssignment(horseId, taskAssignmentId);
        if (refTa == null) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setMsg(user.getMessage(MSG_NO_ACTIVE_GOAL));
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
            return result;
        }

        result.setAssignedUserId(0);
        result.setTimestamp(0);  // client must ignore this
        result.setDescription("");
        result.setDueTime(refTa.getDueTime());
        result.setPermissions(0);
        result.setFlagId(0);

        // everyone in the project could be a candidate
        result.setCandidates(this.determineMembersInfo(projId, null, user.getUid(), membership.getRoleId(), user.getUid()));

        return result;
    }


    /*
     * getFlagInfo
     *
     * Use this method to retrieve data of an existing flag
     * Client must get such data when user clicks to unset or reassign the flag.
     *
     */
    public FlagDetail getFlagInfo(LoginUser user, int horseId, int groupobjId, int flagId) {
        FlagDetail result = new FlagDetail();
        if (!checkGroupState(user, groupobjId, result)) return result;

        int projId = user.getPrjid();
        ProjectMembership membership = projService.getProjectMembership(projId, user.getUid());

        if (membership == null) {
            // the user is not part of the project any more
            result.setCode(ErrorCode.ERR_NO_PERMISSION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_USER_NOT_IN_PROJECT));
            return result;
        }

        // check whether the horse if completed
        WorkflowObject wfo = wfoDao.selectWorkflowObjectByHorse(horseId);
        if (wfo == null || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_DONE || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_CANCELLED) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setMsg(user.getMessage(MSG_HORSE_COMPLETED));
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
            return result;
        }

        // check whether there is already a standing flag
        GroupobjFlag standingFlag = groupobjFlagDao.getStandingFlag(groupobjId);
        if (standingFlag == null) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setMsg(user.getMessage(MSG_FLAG_NO_LONGER_ACTIVE));
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
            return result;
        }

        if (standingFlag.getId() != flagId) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setMsg(user.getMessage(MSG_FLAG_STANDING));
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
            return result;
        }

        result.setAssignedUserId(standingFlag.getAssignedUserId());
        result.setTimestamp(0);  // client must ignore this
        result.setDescription(standingFlag.getIssueDescription());
        result.setDueTime(standingFlag.getDueTime());
        result.setPermissions(standingFlag.getPermissions());
        result.setRaiseTime(standingFlag.getRaiseTime());
        result.setRaiseUserId(standingFlag.getRaiseUserId());
        result.setFlagId(flagId);

        // everyone in the project could be a candidate
        result.setCandidates(this.determineMembersInfo(projId, null, user.getUid(), membership.getRoleId(), user.getUid()));

        return result;
    }


    /*
     * getActiveFlags
     *
     * Get all active flags on the horse visible to the me.
     *
     * Client shows the flags in the appropriate list on the sidebar depending on the flagType (assignedToMe, raisedByMe, or other)
     */
    public List<FlagWorkView> getAllActiveFlags(LoginUser user, int horseId) {
        List<FlagWorkView> result;
        boolean canAccessAll = (PA_HAS_SPECIAL_RIGHTS) ? isProjectAdmin(user, horseId) : false;
        if (canAccessAll) {
            result = groupobjFlagDao.selectAllActiveFlagWorkViewsOfHorse(horseId, user.getUid());
        } else {
            int projId = user.getPrjid();
            ProjectMembership membership = projService.getProjectMembership(projId, user.getUid());
            if (membership == null) return null;
            result = groupobjFlagDao.selectUserAccessibleActiveFlagWorkViews(horseId, user.getUid(), membership.getRoleId());
        }
        return sortFlags(result, horseId);
    }


    private static class FlagComparer implements Comparator<FlagWorkView> {
        @Override
        public int compare(FlagWorkView x, FlagWorkView y) {
            int a = x.getIndex() - y.getIndex();
            return (a != 0) ? a : x.getFlagId() - y.getFlagId();
        }
    }


    private List<FlagWorkView> sortFlags(List<FlagWorkView> flags, int horseId) {
        if (flags == null || flags.isEmpty()) return null;

        // get the question list
        SurveyTree tree = surveyConfigService.buildTreeByHorse(horseId);
        List<Node> questions = tree.listQuestions();
        Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
        int index = 1;
        for (Node qst : questions) {
            indexMap.put(qst.getId(), index);
            index++;
        }

        ArrayList<FlagWorkView> result = new ArrayList<FlagWorkView>();
        for (FlagWorkView flag : flags) {
            Integer idx = indexMap.get(flag.getSurveyQuestionId());
            if (idx != null) {
                flag.setIndex(idx);
                result.add(flag);
            }
        }

        if (!result.isEmpty()) {
            // sort it
            Collections.sort(result, new FlagComparer());
        }

        return result;
    }


    public List<FlagWorkView> getActiveFlagsAssignedToMe(LoginUser user, int horseId) {
        return getActiveFlagsAssignedToUser(user.getUid(), horseId);
    }


    public List<FlagWorkView> getActiveFlagsAssignedToUser(int userId, int horseId) {
        return sortFlags(groupobjFlagDao.selectActiveFlagWorkViewsAssignedToMe(horseId, userId), horseId);
    }


    public List<FlagWorkView> getActiveFlagsRaisedByMe(LoginUser user, int horseId) {
        return sortFlags(groupobjFlagDao.selectActiveFlagWorkViewsRaisedByMe(horseId, user.getUid()), horseId);
    }


    public List<FlagWorkView> getActiveFlagsRaisedByUser(int userId, int horseId) {
        return sortFlags(groupobjFlagDao.selectActiveFlagWorkViewsRaisedByMe(horseId, userId), horseId);
    }


    public List<FlagWorkView> getActiveFlagsOther(LoginUser user, int horseId) {
        List<FlagWorkView> flags = this.getAllActiveFlags(user, horseId);
        if (flags == null || flags.isEmpty()) return null;

        List<FlagWorkView> result = new ArrayList<FlagWorkView>();
        for (FlagWorkView flag : flags) {
            if (flag.getFlagType() == FlagWorkView.FLAG_TYPE_OTHER) result.add(flag);
        }
        return sortFlags(result, horseId);
    }

    
    private boolean isProjectAdmin(LoginUser user, int horseId) {
        if (user.isSiteAdmin()) return true;
       
        Project project = projDao.selectProjectByHorseId(horseId);
        if (project == null) return false;

        // see if the user is the Primary Admin of the project
        if (user.getUid() == project.getAdminUserId())return true;

        // see if the user is a secondary admin of the project
        if (projService.getProjectAdmin(project.getId(), user.getUid()) != null) return true;

        return false;
    }

    private int getNotePermissionForUser(LoginUser user, int notedefId, int horseId) {
        NotedefUser ndu = notedefUserDao.selectByDefAndUser(notedefId, user.getUid());
        if (ndu != null) return ndu.getPermissions();

        NotedefRole ndr = notedefRoleDao.selectByDefAndUserOnHorse(notedefId, user.getUid(), horseId);
        if (ndr != null) return ndr.getPermissions();

        return NO_PERMISSIONS;
    }

    private int getGroupPermissionForUser(LoginUser user, Groupobj groupObj) {
        GroupdefUser gdu = groupdefUserDao.selectByDefAndUser(groupObj.getGroupdefId(), user.getUid());
        if (gdu != null) return gdu.getPermissions();

        GroupdefRole gdr = groupdefRoleDao.selectByDefAndUserOnHorse(groupObj.getGroupdefId(), user.getUid(), groupObj.getHorseId());
        if (gdr != null) return gdr.getPermissions();

        // see whether the user is assigned to any active flags of this groupobj
        List<GroupobjFlag> flags = groupobjFlagDao.selectActiveFlagsByGroupobjAndUser(groupObj.getId(), user.getUid());

        if (flags != null && !flags.isEmpty()) return ACCESS_PERMISSIONS;  // basic permission

        return NO_PERMISSIONS;
    }


    private GroupobjUIState determineUIState(int userId, int groupobjId, int taskType) {
        GroupobjUIState uis = new GroupobjUIState();

        uis.setTextBoxState(GroupobjUIState.TEXT_BOX_STATE_REGULAR);

        // get the standing flag
        GroupobjFlag standingFlag = groupobjFlagDao.getStandingFlag(groupobjId);

        if (standingFlag != null) {
            // there is standing flag
            uis.setTitleBarState(GroupobjUIState.TITLE_BAR_STATE_HAS_FLAG);

            // see whether horse is completed
            WorkflowObject wfo = wfoDao.selectWorkflowObjectByGroupobj(groupobjId);

            if (wfo == null || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_DONE || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_CANCELLED) {
                uis.setFlagButtonState(GroupobjUIState.FLAG_BUTTON_STATE_HIDE);
            } else if (standingFlag.getRaiseUserId() == userId) {
                // flag raised by me
                uis.setFlagButtonState(GroupobjUIState.FLAG_BUTTON_STATE_SET_BY_ME);
            } else if (standingFlag.getAssignedUserId() == userId) {
                // flag assigned to me
                uis.setFlagButtonState(GroupobjUIState.FLAG_BUTTON_STATE_ASSIGNED_TO_ME);

                if (standingFlag.getRespondTime() == null) {
                    // not responded yet
                    // further check task type
                    if (taskType == Constants.TASK_TYPE_SURVEY_FLAG_RESPONSE) {
                        uis.setTextBoxState(GroupobjUIState.TEXT_BOX_STATE_FLAG_RESPONSE);
                    }
                }
            } else {
                // flag set and assigned to other
                uis.setFlagButtonState(GroupobjUIState.FLAG_BUTTON_STATE_SET_OTHERS);
            }
        } else {
            // no standing flag
            uis.setTitleBarState(GroupobjUIState.TITLE_BAR_STATE_NO_FLAG);

            // see whether horse is completed
            WorkflowObject wfo = wfoDao.selectWorkflowObjectByGroupobj(groupobjId);

            if (wfo == null || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_DONE || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_CANCELLED) {
                uis.setFlagButtonState(GroupobjUIState.FLAG_BUTTON_STATE_HIDE);
            } else {
                uis.setFlagButtonState(GroupobjUIState.FLAG_BUTTON_STATE_UNSET);
            }
        }

        return uis;
    }


    private boolean checkNoteState(LoginUser user, int noteobjId, NoteActionResult result) {
        result.setCode(ErrorCode.OK);
        result.setState(NoteActionResult.NOTE_STATE_NORMAL);

        if (user == null) {
            // user not logged in
            result.setCode(ErrorCode.ERR_NO_PERMISSION);
            result.setState(NoteActionResult.NOTE_STATE_REMOVED);
            result.setMsg(MSG_USER_SESSION_EXPIRED);
            return false;
        }

        if (noteobjId <= 0) return true;
        
        Noteobj obj = noteobjDao.get(noteobjId);

        if (obj == null) {
            result.setCode(ErrorCode.ERR_BAD_OBJECT_ID);
            result.setState(NoteActionResult.NOTE_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_NOTE_REMOVED));
            return false;
        }

        Notedef def = notedefDao.get(obj.getNotedefId());

        if (def == null) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(NoteActionResult.NOTE_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_NOTE_REMOVED));
            return false;
        }
        if (!def.getEnabled()) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(NoteActionResult.NOTE_STATE_DISABLED);
            result.setMsg(user.getMessage(MSG_NOTE_DISABLED));
            return false;
        }

        return true;
    }


    private boolean checkGroupState(LoginUser user, int groupobjId, GroupActionResult result) {
        result.setCode(ErrorCode.OK);
        result.setState(GroupActionResult.GROUP_STATE_NORMAL);

        if (user == null) {
            // user not logged in
            result.setCode(ErrorCode.ERR_NO_PERMISSION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(MSG_USER_SESSION_EXPIRED);
            return false;
        }

        if (groupobjId <= 0) return true;

        Groupobj obj = groupobjDao.get(groupobjId);

        if (obj == null) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_GROUP_REMOVED));
            return false;
        }

        Groupdef def = groupdefDao.get(obj.getGroupdefId());

        if (def == null) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_GROUP_REMOVED));
            return false;
        }
        if (!def.getEnabled()) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(GroupActionResult.GROUP_STATE_DISABLED);
            result.setMsg(user.getMessage(MSG_GROUP_DISABLED));
            return false;
        }

        // check whether the user is still a member of the group
        int permissions = this.getGroupPermissionForUser(user, obj);
        if (permissions != NO_PERMISSIONS) return true;

        // user has no access permission to this group. Check whethere the user has special rights
        if (PA_HAS_SPECIAL_RIGHTS && isProjectAdmin(user, obj.getHorseId())) {
            return true;
        } else {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_NO_GROUP_ACCESS));
            return false;
        }
    }


    private static class MemberComparer implements Comparator<CommMemberInfo> {
        @Override
        public int compare(CommMemberInfo x, CommMemberInfo y) {
            String namex = x.getDisplayName();
            String namey = y.getDisplayName();
            return namex.compareTo(namey);
        }
    }

    private List<CommMemberInfo> determineMembersInfo(int projId, List<Integer> uids, int thisUserId, int thisUserRoleId, int excludeUserId) {
        List<CommMemberInfo> members = userDao.getCommMemberInfo(projId, uids, excludeUserId);
        ViewPermissionCtx ctx = viewPermissionService.getViewPermissionCtx(projId, Constants.DEFAULT_VIEW_MATRIX_ID, thisUserRoleId);
        viewPermissionService.setDisplayNames(ctx, members, thisUserId);

        // sort the display names
        Collections.sort(members, new MemberComparer());

        return members;
    }


    private GroupContentResult getCurrentGroupComments(LoginUser user, int taskType, int groupobjId, long timestamp, boolean canManageComments, GroupContentResult result) {
        result.setTimestamp(timestamp);

        int projId = user.getPrjid();
        ProjectMembership membership = projService.getProjectMembership(projId, user.getUid());

        if (membership == null) {
            // the user is not part of the project any more
            result.setCode(ErrorCode.ERR_NO_PERMISSION);
            result.setState(GroupActionResult.GROUP_STATE_REMOVED);
            result.setMsg(user.getMessage(MSG_USER_NOT_IN_PROJECT));
            return result;
        }

        result.setUIState(determineUIState(user.getUid(), groupobjId, taskType));

        // get standing flag
        GroupobjFlag standingFlag = groupobjFlagDao.getStandingFlag(groupobjId);
        result.setFlagId(standingFlag != null ? standingFlag.getId() : 0);

        // retrieve all comments after the specified timestamp
        List<GroupobjComment> comments = canManageComments ?
            groupobjCommentDao.getAllCommentsByGroupobj(groupobjId, timestamp) :
            groupobjCommentDao.getUnhiddenCommentsByGroupobj(groupobjId, timestamp);

        List<GroupComment> cmts = new ArrayList<GroupComment>();
        if (comments != null && !comments.isEmpty()) {
            List<Integer> uids = new ArrayList<Integer>();
            for (GroupobjComment comment : comments) {
                uids.add(comment.getAuthorUserId());
            }
            List<CommMemberInfo> members = this.determineMembersInfo(projId, uids, user.getUid(), membership.getRoleId(), 0);

            for (GroupobjComment comment : comments) {
                GroupComment cmt = new GroupComment();
                cmt.setCommentId(comment.getId());
                cmt.setTime(comment.getCreateTime());
                cmt.setType(comment.getType());
                cmt.setState((comment.getDeleteTime() != null) ? GroupComment.COMMENT_STATE_HIDDEN : GroupComment.COMMENT_STATE_NORMAL);
                cmt.setText(comment.getComment());

                for (CommMemberInfo m : members) {
                    if (m.getUserId() == comment.getAuthorUserId()) {
                        cmt.setUserDisplayName(m.getDisplayName());
                        break;
                    }
                }
                cmts.add(cmt);
            }
        }

        result.setComments(cmts);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }


    private String generateFlagText(String issueDescription, Date dueTime, User assignedUser, int permissions) {
        StringBuilder sb = new StringBuilder();
        String dateStr = (dueTime == null) ? "" : new SimpleDateFormat("yyyy-MM-dd").format(dueTime);
        sb.append(issueDescription).append("\n<br/>");
        sb.append("Assigned To: ").append(assignedUser.getFirstName()).append(" ").append(assignedUser.getLastName()).append("\n<br/>");
        sb.append("Due: ").append(dateStr).append("\n<br/>");
        sb.append("Actions:");

        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_ATTACHMENT) != 0) {
            sb.append(" Attachment");
        }
        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_COMMENT) != 0) {
            sb.append(" Comment");
        }
        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_SCORE) != 0) {
            sb.append(" Score");
        }
        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_SOURCE) != 0) {
            sb.append(" Source");
        }
        if ((permissions & Constants.FLAG_PERMISSION_PR_CHANGE_OPINION) != 0) {
            sb.append(" Opinion");
        }
        if ((permissions & Constants.FLAG_PERMISSION_PR_DISCUSSION) != 0) {
            sb.append(" Discussion");
        }

        return sb.toString();
    }


    private GroupobjFlag checkFlag(LoginUser user, int groupobjId, int flagId, GroupActionResult result) {
        GroupobjFlag flag = groupobjFlagDao.get(flagId);
        if (flag == null) {
            result.setCode(ErrorCode.ERR_NON_EXISTENT);
            result.setMsg(user.getMessage(MSG_BAD_FLAG));
            return null;
        }

        if (flag.getGroupobjId() != groupobjId) {
            result.setCode(ErrorCode.ERR_BAD_OBJECT_ID);
            result.setMsg(user.getMessage(MSG_WRONG_FLAG_GROUP));
            return null;
        }

        return flag;
    }



    private TaskAssignment getReferenceTaskAssignment(int horseId, int taskAssignmentId) {
        GoalObject activeGoalObj = null;
        TaskAssignment ta = null;

        if (taskAssignmentId > 0) {
            activeGoalObj = goalObjDao.selectGoalObjectByTaskAssignmentId(taskAssignmentId);
        }

        if (activeGoalObj != null && activeGoalObj.getStatus() != Constants.GOAL_OBJECT_STATUS_DONE) {
            ta = taskAssignmentDao.get(taskAssignmentId);
        }

        if (ta == null) {
            // Use any active task assignment as the reference
            ta = taskAssignmentDao.selectActiveAssignmentByHorse(horseId);
        }

        if (ta == null) return null;

        // adjust the due time if necessary
        Date now = new Date();
        if (ta.getDueTime() == null || ta.getDueTime().before(now)) {
            ta.setDueTime(now);
        }
        return ta;
    }



    private GroupobjFlag setFlag(LoginUser user, int taskType, int horseId, int taskAssignmentId, int groupobjId, GroupobjFlag flag, int assignedUserId, String issueDescription, Date dueTime, int permissions, GroupActionResult result) {
        User assignedUser = userDao.get(assignedUserId);
        if (assignedUser == null) {
            result.setCode(ErrorCode.ERR_INVALID_PARAM);
            result.setMsg(user.getMessage(MSG_BAD_ASSIGNED_USER));
            return flag;
        }

        TaskAssignment refTa = getReferenceTaskAssignment(horseId, taskAssignmentId);

        if (refTa == null) {
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setMsg(user.getMessage(MSG_NO_ACTIVE_GOAL));
            return flag;
        } 

        Date now = new Date();
        int prevAssignedUserId = 0;
        if (flag == null) {
            flag = new GroupobjFlag();
        } else if (flag.getAssignedUserId() != assignedUserId) {
            prevAssignedUserId = flag.getAssignedUserId();
        }

        flag.setAssignedUserId(assignedUserId);
        flag.setDueTime(dueTime);
        flag.setGroupobjId(groupobjId);
        flag.setIssueDescription(issueDescription);
        flag.setPermissions(permissions);
        flag.setRaiseTime(now);
        flag.setRaiseUserId(user.getUid());
        flag.setRespondTime(null);
        flag.setUnsetComment(null);
        flag.setUnsetTime(null);
        flag.setUnsetUserId(0);
        groupobjFlagDao.save(flag);

        GroupobjComment comment = new GroupobjComment();
        comment.setAuthorUserId(user.getUid());
        comment.setComment(generateFlagText(issueDescription, dueTime, assignedUser, permissions));
        comment.setCreateTime(now);
        comment.setDeleteUserId(0);
        comment.setGroupobjFlagId(flag.getId());
        comment.setGroupobjId(groupobjId);
        comment.setType(Constants.GROUP_COMMENT_TYPE_SET_FLAG);
        groupobjCommentDao.create(comment);

        // create a new task assignment for the assigned user if not already existing
        this.adjustFlagResponseAssignment(assignedUserId, horseId, refTa, false);
   
        if (prevAssignedUserId > 0) {
            // now the flag is reassigned to differen user, check the prev user still has any pending flags.
            // If not, then remove the flag response task
            adjustFlagResponseAssignment(prevAssignedUserId, horseId, refTa, false);
        }

        // See whether the current user already has a UNSET_FLAG task assignment. If not, create one.
        adjustFlagUnsetAssignment(user.getUid(), horseId, refTa, false);

        return flag;
    }


    public void adjustFlagUnsetAssignment(int assignedUserId, int horseId, TaskAssignment refTa, boolean adjustAssignmentStatus) {
        List<GroupobjFlag> flags = groupobjFlagDao.selectActiveFlagsRaisedByUser(horseId, assignedUserId);
        int responded = 0;
        int total = 0;
        Date dueTime = null;

        if (flags != null && !flags.isEmpty()) {
            total = flags.size();
            for (GroupobjFlag flag : flags) {
                if (flag.getRespondTime() != null) {
                    responded++;
                }

                if (dueTime == null || dueTime.before(flag.getDueTime())) dueTime = flag.getDueTime();
            }
        }

        float percent = (float) ((total > 0) ? (float) responded / (float) total : 0.0);
        TaskAssignmentStatusData tasd = new TaskAssignmentStatusData();
        tasd.setCompletedItems(responded);
        tasd.setTotalItems(total);

        TaskAssignment ta = taskAssignmentDao.selectActiveAssignmentsByUserHorseAndTask(assignedUserId, horseId, Constants.TASK_ID_UNSET_FLAG);
        if (ta == null) {
            if (total == 0) return;

            // create a new assignment for the current user in any active goal of this horse
            if (refTa == null) refTa = getReferenceTaskAssignment(horseId, 0);
            if (refTa == null) return; // should never happen

            Target target = targetDao.selectTargetByHorseId(horseId);

            Date now = new Date();

            ta = new TaskAssignment();
            ta.setTaskId(Constants.TASK_ID_UNSET_FLAG);
            ta.setTargetId(target.getId());
            ta.setAssignedUserId(assignedUserId);
            ta.setDueTime(dueTime);
            ta.setGoalObjectId(refTa.getGoalObjectId());
            ta.setHorseId(horseId);
            ta.setStatus((short)Constants.TASK_STATUS_ACTIVE);
            ta.setQEnterTime(now);
            ta.setQLastAssignedTime(now);
            ta.setPercent(percent);
            ta.setStatusData(tasd.encode());
            if (adjustAssignmentStatus) ta.setStatus((short)Constants.TASK_STATUS_STARTED);
            taskAssignmentDao.create(ta);
        } else if (total == 0) {
            // all flags have been unset - remove the task
            taskAssignmentDao.delete(ta.getId());
            return;
        } else {
            // update the percent and due time
            ta.setDueTime(dueTime);
            ta.setPercent(percent);
            ta.setStatusData(tasd.encode());
            if (adjustAssignmentStatus) ta.setStatus((short)Constants.TASK_STATUS_STARTED); 
            taskAssignmentDao.update(ta);
        }
    }


    public void adjustFlagResponseAssignment(int assignedUserId, int horseId, TaskAssignment refTa, boolean adjustAssignmentStatus) {
        // update this user's FLAG_RESPONSE task assignment, if any      
        // check whether this user still has any assigned flags. If not, cancel the user's FLAG_RESPONSE task.
        List<GroupobjFlag> flags = groupobjFlagDao.selectActiveFlagsAssignedToUser(horseId, assignedUserId);
        int responded = 0;
        int total = 0;
        Date dueTime = null;

        if (flags != null && !flags.isEmpty()) {
            total = flags.size();
            for (GroupobjFlag flag : flags) {
                if (flag.getRespondTime() != null) {
                    responded++;
                }

                if (dueTime == null || dueTime.after(flag.getDueTime())) dueTime = flag.getDueTime();
            }
        }

        float percent = (float) ((total > 0) ? (float) responded / (float) total : 0.0);
        TaskAssignmentStatusData tasd = new TaskAssignmentStatusData();
        tasd.setCompletedItems(responded);
        tasd.setTotalItems(total);

        TaskAssignment ta = taskAssignmentDao.selectActiveAssignmentsByUserHorseAndTask(assignedUserId, horseId, Constants.TASK_ID_FLAG_RESPONSE);

        if (ta == null) {
            if (total == 0) return;

            // create a new assignment
            if (refTa == null) refTa = getReferenceTaskAssignment(horseId, 0);
            if (refTa == null) return; // should never happen

            Target target = targetDao.selectTargetByHorseId(horseId);
            
            Date now = new Date();

            ta = new TaskAssignment();
            ta.setTaskId(Constants.TASK_ID_FLAG_RESPONSE);
            ta.setTargetId(target.getId());
            ta.setAssignedUserId(assignedUserId);
            ta.setDueTime(dueTime);
            ta.setGoalObjectId(refTa.getGoalObjectId());
            ta.setHorseId(horseId);
            ta.setStatus((short)Constants.TASK_STATUS_ACTIVE);
            ta.setQEnterTime(now);
            ta.setQLastAssignedTime(now);
            ta.setPercent(percent);
            ta.setStatusData(tasd.encode());
            if (adjustAssignmentStatus) ta.setStatus((short)Constants.TASK_STATUS_STARTED);
            taskAssignmentDao.create(ta);
        } else if (total == 0) {
            // all flags have been unset - remove the task
            taskAssignmentDao.delete(ta.getId());
            return;
        } else {
            // update the percent and due time
            ta.setDueTime(dueTime);
            ta.setPercent(percent);
            ta.setStatusData(tasd.encode());
            if (adjustAssignmentStatus) ta.setStatus((short)Constants.TASK_STATUS_STARTED); 
            taskAssignmentDao.update(ta);
        }
    }



    public FlagResponseDestination getFlagResponseDestination(LoginUser user, int horseId, int groupobjId, int flagId) {
        FlagResponseDestination result = new FlagResponseDestination();

        if (!checkGroupState(user, groupobjId, result)) return result;

        GroupobjFlag flag = this.checkFlag(user, groupobjId, flagId, result);

        if (flag == null) {
            result.setMsg(user.getMessage(MSG_BAD_FLAG));
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
        } else if (flag.getUnsetTime() != null) {
            result.setMsg(user.getMessage(MSG_FLAG_ALREADY_UNSET));
            result.setCode(ErrorCode.ERR_INVALID_OPERATION);
            result.setState(GroupActionResult.GROUP_STATE_OUT_OF_SYNC);
        } else {
            TaskAssignment ta = taskAssignmentDao.selectActiveAssignmentsByUserHorseAndTask(user.getUid(), horseId, Constants.TASK_ID_FLAG_RESPONSE);
            int taskAssignmentId = (ta != null) ? ta.getId() : 0;
            result.setTaskAssignmentId(taskAssignmentId);
        }

        return result;
    }


    public GroupobjFlag getFlag(int flagId) {
        return groupobjFlagDao.get(flagId);
    }


    public FlagWorkView getFlagWorkView(int flagId, int userId) {
        return groupobjFlagDao.getFlagWorkView(flagId, userId);
    }


    private FlagNavView getNavViewForFlags(List<FlagWorkView> flags, int horseId, int flagId) {
        FlagNavView result = new FlagNavView();
        if (flags == null || flags.isEmpty()) return result;

        FlagWorkView prevFlag = null;
        FlagWorkView nextFlag = null;
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).getFlagId() == flagId) {
                if (i > 0) prevFlag = flags.get(i-1);
                if (i < flags.size()-1) nextFlag = flags.get(i+1);
                break;
            }
        }

        if (prevFlag != null) {
            result.setPrevFlag(prevFlag);
        }
        if (nextFlag != null) {
            result.setNextFlag(nextFlag);
        }
        result.setHorseId(horseId);
        result.setFlagId(flagId);
        return result;
    }


    private FlagWorkView getDirectedFlag(List<FlagWorkView> flags, int horseId, int userId, int flagId, int dir) {
        if (flags == null || flags.isEmpty()) return null;

        // see whether the flag is in the list
        FlagWorkView flag = null;
        for (FlagWorkView f : flags) {
            if (f.getFlagId() == flagId) {
                flag = f;
                break;
            }
        }

        if (flag == null) {
            // not in the list
            flag = groupobjFlagDao.getFlagWorkView(flagId, userId);
            if (flag == null) return null;
            flags.add(flag);
        }

        flags = this.sortFlags(flags, horseId);

        FlagNavView nav = this.getNavViewForFlags(flags, horseId, flagId);

        return (dir > 0) ? nav.getNextFlag() : nav.getPrevFlag();
    }


    public FlagWorkView getDirectedFlagAssignedToMe(LoginUser user, int horseId, int flagId, int dir) {
        return getDirectedFlag(groupobjFlagDao.selectActiveFlagWorkViewsAssignedToMe(horseId, user.getUid()),
                horseId, user.getUid(), flagId, dir);
    }


    public FlagWorkView getDirectedFlagRaisedByMe(LoginUser user, int horseId, int flagId, int dir) {
        return getDirectedFlag(groupobjFlagDao.selectActiveFlagWorkViewsRaisedByMe(horseId, user.getUid()),
                horseId, user.getUid(), flagId, dir);
    }


    public FlagNavView getNavViewForFlagsAssignedToMe(LoginUser user, int horseId, int flagId) {
        return getNavViewForFlags(getActiveFlagsAssignedToMe(user, horseId), horseId,  flagId);
    }


    public FlagNavView getNavViewForFlagsRaisedByMe(LoginUser user, int horseId, int flagId) {
        return getNavViewForFlags(getActiveFlagsRaisedByMe(user, horseId), horseId,  flagId);
    }

}
