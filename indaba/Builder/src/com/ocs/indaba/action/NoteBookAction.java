/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.po.*;
import com.ocs.indaba.service.*;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class NoteBookAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(NoteBookAction.class);
    protected static final String NOTEBOOK_ACTION_EDIT = "edit";
    protected static final String NOTEBOOK_ACTION_CREATE = "create";
    protected static final String NOTEBOOK_ACTION_DISPLAY = "display";
    protected static final String NOTEBOOK_ACTION_SAVE = "save";
    protected static final String NOTEBOOK_ACTION_SAVEANDDONE = "save&done";
    protected static final String NOTEBOOK_ACTION_OVERALLSAVE = "overallsave";
    private static final String PARAM_NOTEBOOK_ACTION = "action";
    protected static final String PARAM_DESCRIPTION = "description_";
    protected static final String PARAM_BODY = "body";
    protected static final String ATTR_JOURNAL_TITLE = "journalTitle";
    protected static final String ATTR_JOURNAL_BODY = "journalBody";
    protected static final String ATTR_JOURNAL_MINWORDS = "minWords";
    protected static final String ATTR_JOURNAL_MAXWORDS = "maxWords";
    protected static final String ATTR_ATTACHMENTS = "attachments";
    protected static final String ATTR_JOURNAL_CONTENT_HEADER = "contentHeader";
    protected String NOTEBOOK_EDIT_INSTRUCTION = "notebookinstr";
    protected JournalService journalService = null;
    protected JournalVersionService jouralVersionService = null;
    protected TaskService taskService = null;
    protected HorseService horseService = null;
    protected UserService userService = null;
    protected SiteMessageService siteMsgSrvc = null;
    private TaskDAO taskDao = null;
    private ProjectDAO projectDao = null;

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Object maxLenExceeded = request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);

        if (maxLenExceeded != null && ((Boolean) maxLenExceeded).booleanValue()) {
            logger.error("Exceeds max file size.");
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_EXCEED_MAX_FILESIZE));
            return mapping.findForward(FWD_ERROR);
        }

        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, false, true);

        //
        // Permitted parameters:
        //  * action   - [MUST]     the action: create, edit, display, review, peerreview, pay, approve
        //  * horseid  - [MUST]     the horse id related with this notebook
        //  * cntid    - [OPTIONAL] the journal content object id of this notebook
        //
        String action = (String) request.getAttribute(ATTR_ACTION);

        if (action == null) {
            action = request.getParameter(ATTR_ACTION);
        }
        if (action == null) {
            action = NOTEBOOK_ACTION_SAVE;
        }
        int horseId = Constants.INVALID_INT_ID;
        if (request.getAttribute(PARAM_HORSE_ID) != null) {
            horseId = (Integer) request.getAttribute(PARAM_HORSE_ID);
        } else {
            horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        }

        int assignId = Constants.INVALID_INT_ID;
        if (request.getAttribute(PARAM_ASSIGNID) != null) {
            assignId = (Integer) request.getAttribute(PARAM_ASSIGNID);
        } else {
            assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        }

        int taskType = Constants.INVALID_INT_ID;
        if (request.getAttribute(ATTR_TASK_TYPE) != null) {
            taskType = (Integer) request.getAttribute(ATTR_TASK_TYPE);
        } else {
            taskType = StringUtils.str2int(request.getParameter(ATTR_TASK_TYPE), Constants.INVALID_INT_ID);
        }

        int cntId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_ID), Constants.INVALID_INT_ID);
        logger.debug("action: " + action + ", horseId: " + horseId + ", asignId: " + assignId);

        if (StringUtils.isEmpty(action)) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "action"));
            return mapping.findForward(FWD_ERROR);
        }

        request.setAttribute(ATTR_ACTION, action);
        if (assignId == Constants.INVALID_INT_ID && !NOTEBOOK_ACTION_DISPLAY.equals(action)) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "assignid"));
            return mapping.findForward(FWD_ERROR);
        }
        request.setAttribute(PARAM_ASSIGNID, assignId);
        if (horseId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }
        //set horse id
        request.setAttribute(PARAM_HORSE_ID, horseId);
        request.setAttribute(PARAM_NOTEBOOK_ACTION, action);
        String task = request.getParameter(ATTR_TASK);
        request.setAttribute(ATTR_TASK, StringUtils.isEmpty(task) ? action : task);

        logger.debug("NoteBook request: [horseid=" + horseId + ", action=" + action + "].");

        preHandle(request, assignId, cntId, horseId);

        ActionForward actionFwd = null;
        if (NOTEBOOK_ACTION_EDIT.equals(action) || NOTEBOOK_ACTION_CREATE.equals(action)) {
            actionFwd = super.checkAssignmentStatus(mapping, request);
            if (actionFwd != null) {
                return actionFwd;
            }
            request.setAttribute(ATTR_ACTION, NOTEBOOK_ACTION_SAVE);
            actionFwd = mapping.findForward(NOTEBOOK_ACTION_EDIT); //handleEdit(mapping, request, assignId, cntId, horseId);
        } else if (NOTEBOOK_ACTION_DISPLAY.equals(action)) {
            actionFwd = mapping.findForward(NOTEBOOK_ACTION_DISPLAY); //handleDisplay(mapping, request, assignId, cntId, horseId);
        } else if (NOTEBOOK_ACTION_SAVE.equals(action)) {
            actionFwd = handleSave(mapping, form, request, assignId, cntId, horseId, loginUser.getUid());
        } else if (NOTEBOOK_ACTION_SAVEANDDONE.equals(action)) {
            actionFwd = handleSaveAndDone(mapping, form, request, assignId, cntId, horseId, loginUser);
        } else if (NOTEBOOK_ACTION_OVERALLSAVE.equals(action)) {
            actionFwd = handleOveralSave(mapping, form, request, assignId, cntId, horseId, loginUser.getUid());
        } else {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_UNRECONGNIZED, "action"));
            actionFwd = mapping.findForward(FWD_ERROR);
        }
        return actionFwd;
    }

    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    /******************************************
    // Handle edit
    private ActionForward handleEdit(ActionMapping mapping,
    HttpServletRequest request, int assignId, int cntId, int horseId) {
    preHandle(request, assignId, cntId, horseId);
    return mapping.findForward(NOTEBOOK_ACTION_EDIT);
    }
    // Handle display
    private ActionForward handleDisplay(ActionMapping mapping,
    HttpServletRequest request, int assignId, int cntId, int horseId) {
    preHandle(request, assignId, cntId, horseId);
    return mapping.findForward(NOTEBOOK_ACTION_DISPLAY);
    }
     ***************/
    // Handle submit
    private ActionForward handleSave(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, int assignId, int cntId, int horseId, int userId) {
        int assignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        int toolId = StringUtils.str2int(request.getParameter(PARAM_TOOL_ID), Constants.INVALID_INT_ID);

        String taskName = request.getParameter(ATTR_TASK);

        TaskAssignment ta = taskService.getTaskAssignment(assignId);
        if (ta != null) {// && ta.getStatus() == Constants.TASK_STATUS_NOTICED) {
            float percentage = 0f;
            if (Constants.TASK_ACTION_CREATE.equals(taskName)
                    || Constants.TASK_ACTION_EDIT.equals(taskName)
                    || Constants.TASK_ACTION_SAVE.equals(taskName)) {
                String body = request.getParameter(PARAM_BODY);

                long words = (body == null) ? 0 : StringUtils.wordcount(body);
                int maxWords = StringUtils.str2int(request.getParameter(ATTR_JOURNAL_MAXWORDS), Constants.INVALID_INT_ID);
                int minWords = StringUtils.str2int(request.getParameter(ATTR_JOURNAL_MINWORDS), Constants.INVALID_INT_ID);
                if (maxWords == 0 && minWords == 0) {
                    //logger.debug("===-----00000000=>>>" + taskName);
                    percentage = 0.500001f;
                } else {
                    percentage = (minWords > 0) ? (float) words / minWords : 0f;
                }
            } else {
                //logger.debug("====>>>" + taskName);
                percentage = 0.500001f;
            }
            //logger.debug("-------*******[" + percentage + "]*******-----");

            int taskStatus = (ta.getStatus() < Constants.TASK_STATUS_STARTED) ? Constants.TASK_STATUS_STARTED : ta.getStatus();
            taskService.updateTaskAssignment(assignId, taskStatus, (percentage > 0.95001) ? .95001f : percentage);
        }
        //Log event
        super.logEventByKey(userId, Constants.DEFAULT_EVENTLOG_ID, Constants.EVENT_TYPE_WORK_ON_TASK, 
                "User submit the content for the assigned task. [assignid={0}][toolid={1}][prdyype={2}]",
                assignmentId, toolId, Constants.CONTENT_TYPE_JOURNAL);

        handleSubmit(NOTEBOOK_ACTION_SAVE, form, request, cntId, horseId, userId);
        request.setAttribute(ATTR_ACTION, NOTEBOOK_ACTION_DISPLAY);
        String forward = null;
        String returlParm = request.getParameter(ATTR_RET_URL);
        if (returlParm != null) {
            try {
                forward = URLDecoder.decode(returlParm, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
            }
        }
        
        logger.debug("Forward to '" + forward + "'.");
        // NOTEBOOK_ACTION_EDIT
        return new ActionForward(forward, true);
    }

    private ActionForward handleOveralSave(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, int assignId, int cntId, int horseId, int userId) {
        int assignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        int toolId = StringUtils.str2int(request.getParameter(PARAM_TOOL_ID), Constants.INVALID_INT_ID);

        TaskAssignment ta = taskService.getTaskAssignment(assignId);
        String taskName = request.getParameter(ATTR_TASK);

        if (ta != null) {// && ta.getStatus() == Constants.TASK_STATUS_NOTICED) {
            float percentage = 0f;
            if (Constants.TASK_ACTION_CREATE.equals(taskName)
                    || Constants.TASK_ACTION_EDIT.equals(taskName)
                    || Constants.TASK_ACTION_SAVE.equals(taskName)) {
                String body = request.getParameter(PARAM_BODY);

                int words = (body == null) ? 0 : body.length();
                int maxWords = StringUtils.str2int(request.getParameter(ATTR_JOURNAL_MAXWORDS), Constants.INVALID_INT_ID);
                int minWords = StringUtils.str2int(request.getParameter(ATTR_JOURNAL_MINWORDS), Constants.INVALID_INT_ID);
                if (maxWords == 0 && minWords == 0) {
                    percentage = 0.500001f;
                } else {
                    percentage = (minWords > 0) ? (float) words / minWords : 0f;
                }
            } else {
                percentage = 0.500001f;
            }

            int taskStatus = (ta.getStatus() < Constants.TASK_STATUS_STARTED) ? Constants.TASK_STATUS_STARTED : ta.getStatus();
            taskService.updateTaskAssignment(assignId, taskStatus, (percentage > 0.95001) ? .95001f : percentage);
        }
        //Log event
        super.logEventByKey(userId, Constants.DEFAULT_EVENTLOG_ID, Constants.EVENT_TYPE_WORK_ON_TASK, 
                "User submit the content for the assigned task. [assignid={0}][toolid={1}][prdyype={2}]",
                assignmentId, toolId, Constants.CONTENT_TYPE_JOURNAL);

        handleSubmit(NOTEBOOK_ACTION_OVERALLSAVE, form, request, cntId, horseId, userId);

        // Fix bug #695: After the user click on 'Save' on the notebook super-edit tool,
        // currently the user stays on the same page. Please change it such that the user
        // is returned to the content display page for this notebook.
        request.setAttribute(ATTR_ACTION, NOTEBOOK_ACTION_DISPLAY);

        int superEdit = StringUtils.str2int(request.getParameter(PARAM_SUPER_EDIT), Constants.INVALID_INT_ID);
        ActionForward actionFwd = null;
        if (superEdit == 1) {
            String forward = "/notebook.do?action=display&horseid=" + horseId;
            logger.debug("Forward to '" + forward + "'.");
            actionFwd = new ActionForward(forward, true);
        } else {
            actionFwd = mapping.findForward(FWD_JOURNAL_OVERALLREVIEW);
        }
        return actionFwd;
    }

    private ActionForward handleSaveAndDone(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, int assignId, int cntId, int horseId, LoginUser loginUser) {
        int userId = loginUser.getUid();
        int toolId = StringUtils.str2int(request.getParameter(PARAM_TOOL_ID), Constants.INVALID_INT_ID);
        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);

        if ((assignedTask != null) && (assignedTask.getTaskType() == Constants.TASK_TYPE_JOURNAL_REVIEW_RESPONSE)) {
            int uid = assignedTask.getUserId();

            ProjectUserView user = userService.getProjectUserView(loginUser.getPrjid(), uid);
            if (user != null) {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("username", user.getFirstName() + " " + user.getLastName());
                ContentHeader ch = horseService.getContentHeaderByHorseId(horseId);
                parameters.put("contenttitle", ch.getTitle());
                com.ocs.indaba.po.Project project = projectDao.selectProjectByHorseId(horseId);
                parameters.put("projectname", project.getCodeName());
                siteMsgSrvc.deliver(user, Constants.NOTIFICATION_TYPE_SYS_REVIEW_RESPONSE_POSTED, parameters);
                taskService.removeTaskAssignment(assignId);
            }
        }

        taskService.updateTaskAssignment(assignId, Constants.TASK_STATUS_DONE, 1.0001f, new Date());

        //Log event
        logEventByKey(userId, Constants.DEFAULT_EVENTLOG_ID, Constants.EVENT_TYPE_COMPLETE_TASK,
                "User complete the content for the assigned task. [assignid={0}][toolid={1}][prdtype={2}]",
                assignId, toolId, Constants.CONTENT_TYPE_JOURNAL);
        handleSubmit(NOTEBOOK_ACTION_SAVEANDDONE, form, request, cntId, horseId, userId);
        // Add content version when submitting
        if (assignedTask != null) {
            addContentVersion(cntId, horseId, (assignedTask == null) ? "--" : assignedTask.getTaskName(), userId);
        }
        return mapping.findForward(FWD_YOURCONTENT);
    }

    private void handleSubmit(String action, ActionForm form,
            HttpServletRequest request, int cntId, int horseId, int userId) {
        ContentHeader cntHdr = null;
        JournalContentObject cntObj = null;
        JournalContentView journalCntView = null;
        List<Attachment> attachments = null;
        String storedFilename = null;
        FormFile formFile = null;
        if (((SingleUploadForm) form).getFile() != null) {
            storedFilename = digestJournalAttachmentFilename(((SingleUploadForm) form).getFile().getFileName(), horseId);
            formFile = extractSingleUploadFormFiles((SingleUploadForm) form, storedFilename, horseId);
        }
        // if cntid specified
        journalCntView = journalService.getJournalContentByCntObjIdOrHorseId(cntId, horseId);
        if (journalCntView != null) { // if existing, load it
            //cntObj = jouralService.getJournalById(Integer.valueOf(cntidStr).intValue());
            //logger.debug("Journal Content is existed!");
            cntHdr = journalCntView.getContentHeader();
            if (cntHdr == null) {
                logger.debug("Create Content Header ...");
                cntHdr = new ContentHeader();
                journalCntView.setContentHeader(cntHdr);
            }
            if (cntHdr.getAuthorUserId() <= 0) {
                cntHdr.setAuthorUserId(userId);
            }
            cntHdr.setLastUpdateUserId(userId);
            cntHdr.setLastUpdateTime(new Date());
            //cntHdr.setTitle(request.getParameter(PARAM_TITLE));
            cntObj = journalCntView.getJournalContentObject();
            if (cntObj == null) {
                cntObj = new JournalContentObject();
                journalCntView.setJournalContentObject(cntObj);
            }

            String body = request.getParameter(PARAM_BODY);
            //try {
            //    body = new String(body.getBytes("ISO-8859-1"), "UTF-8");
            //} catch (Exception e) {}
            cntObj.setBody(body);

            attachments = journalCntView.getAttachments();
            if (attachments == null) {
                attachments = new ArrayList<Attachment>();
                journalCntView.setAttachments(attachments);
            }
            if (formFile != null && !StringUtils.isEmpty(formFile.getFileName())) {
                String fileDesc = request.getParameter(PARAM_FILE_DESC);
                attachments.add(AttachmentAdapter.formFile2Attachment(formFile, storedFilename, cntHdr.getId(), fileDesc));
            }
        } else {// otherwise, create a new journal content object
            // Set Journal Content Object
            logger.debug("Journal Content isn't existed!");
            cntObj = new JournalContentObject();
            cntObj.setId(Constants.INVALID_INT_ID);
            cntObj.setBody(request.getParameter(PARAM_BODY));

            //Set Content Header
            cntHdr = new ContentHeader();
            cntHdr.setAuthorUserId(userId);
            cntHdr.setLastUpdateTime(new Date());
            cntHdr.setLastUpdateUserId(userId);
            //cntHdr.setTitle(request.getParameter(PARAM_TITLE));
            // set horse id
            cntHdr.setHorseId(StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID));
            if (formFile != null && !StringUtils.isEmpty(formFile.getFileName())) {
                String fileDesc = request.getParameter(PARAM_FILE_DESC);
                attachments.add(AttachmentAdapter.formFile2Attachment(formFile, storedFilename, cntHdr.getId(), fileDesc));
            }
            journalCntView = new JournalContentView(cntHdr, cntObj, attachments);
        }
        if (NOTEBOOK_ACTION_SAVEANDDONE.equals(action) && cntHdr != null && cntHdr.getSubmitTime() == null) {
            cntHdr.setSubmitTime(new Date());
        }
        journalService.saveJournal(journalCntView, userId);

        request.setAttribute(ATTR_JOURNAL_TITLE, cntHdr.getTitle());
        request.setAttribute(ATTR_JOURNAL_BODY, cntObj.getBody());
        request.setAttribute(ATTR_ATTACHMENTS, journalCntView.getAttachments());

        //request.setAttribute(PARAM_NOTEBOOK_ACTION, NOTEBOOK_ACTION_DISPLAY);
        //return mapping.findForward(forward);
    }

    // Save the updated content and attachment to the backup version tables
    private void addContentVersion(int cntId, int horseId, String taskName, int userId) {
        JournalContentView journalCntView = journalService.getJournalContentByCntObjIdOrHorseId(cntId, horseId);
        ContentHeader cntHdr = journalCntView.getContentHeader();
        JournalContentObject cntObj = journalCntView.getJournalContentObject();
        List<Attachment> attachments = journalCntView.getAttachments();
        ContentVersion cntVer = new ContentVersion(null, cntHdr.getId(), new Date(), userId);
        cntVer.setDescription(taskName);
        JournalContentVersion journalCntVer = new JournalContentVersion();
        if (cntObj != null) {
            journalCntVer.setBody(cntObj.getBody());
        }
        List<JournalAttachmentVersion> journalAttachVerions = null;
        if (attachments != null && attachments.size() > 0) {
            journalAttachVerions = new ArrayList<JournalAttachmentVersion>(attachments.size());
            for (Attachment attach : attachments) {
                journalAttachVerions.add(AttachmentAdapter.attachmentToAttachmentVersion(attach));
            }
        }
        JournalVersionView journalVersionView = new JournalVersionView();
        journalVersionView.setContentVersion(cntVer);
        journalVersionView.setJournalContentVersion(journalCntVer);
        journalVersionView.setJournalAttachmentVersions(journalAttachVerions);
        jouralVersionService.saveJournalVersion(journalVersionView);
    }

    // pre handle
    protected void preHandle(HttpServletRequest request, int assignId, int cntId, int horseId) {
        String title = null;
        String body = null;
        List<Attachment> attachments = null;

        TaskAssignment ta = taskService.getTaskAssignment(assignId);
        if (ta != null && ta.getStatus() < Constants.TASK_STATUS_NOTICED) {
            taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_NOTICED);
        }
        JournalContentView journalCntView = journalService.getJournalContentByCntObjIdOrHorseId(cntId, horseId);
        int minWords = 0;
        int maxWords = 0;
        if (journalCntView != null) {
            minWords = journalCntView.getMinWords();
            maxWords = journalCntView.getMaxWords();
            ContentHeader cntHdr = journalCntView.getContentHeader();
            JournalContentObject journalCntObj = journalCntView.getJournalContentObject();

            if (cntHdr != null) {
                title = cntHdr.getTitle();

            }
            if (journalCntObj != null) {
                body = journalCntObj.getBody();
            }
            attachments = journalCntView.getAttachments();

            request.setAttribute(ATTR_JOURNAL_CONTENT_HEADER, cntHdr);
        }

        if (ta != null) {
            Task task = taskDao.get(ta.getTaskId());
            request.setAttribute(ATTR_TASK_TYPE, task.getType());
        } else {
            request.setAttribute(ATTR_TASK_TYPE, 0);
        }

        request.setAttribute(ATTR_JOURNAL_TITLE, title);
        request.setAttribute(ATTR_JOURNAL_BODY, body);
        request.setAttribute(PARAM_HORSE_ID, horseId);
        request.setAttribute(ATTR_JOURNAL_MINWORDS, minWords);
        request.setAttribute(ATTR_JOURNAL_MAXWORDS, maxWords);
        request.setAttribute(ATTR_ATTACHMENTS, attachments);
        request.setAttribute(NOTEBOOK_EDIT_INSTRUCTION, journalService.getInstrunctionbyhorseID(horseId));
        request.setAttribute(ATTR_CONTENT_VERSION_LIST, jouralVersionService.getAllContentVersions(horseId));
    }

    @Autowired
    public void setJournalService(JournalService jouralService) {
        this.journalService = jouralService;
    }

    @Autowired
    public void setJournalVersionService(JournalVersionService jouralVersionService) {
        this.jouralVersionService = jouralVersionService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMsgSrvc = siteMessageService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }
}
