package com.ocs.indaba.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.JournalContentObject;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.JournalService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.ReviewService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.JournalContentView;
import com.ocs.indaba.vo.LoginUser;

public class JournalPeerReviewAction extends BaseAction {
	
    private static final Logger logger = Logger.getLogger(JournalPeerReviewAction.class);
    
    private static final String NOTEBOOK_ACTION_DISPLAY = "display";
    private static final String PARAM_CONTENT_ID = "cntid";
    private static final String PARAM_HORSE_ID = "horseid";
    private static final String ATTR_JOURNAL_TITLE = "journalTitle";
    private static final String ATTR_JOURNAL_BODY = "journalBody";
    private static final String ATTR_JOURNAL_MINWORDS = "minWords";
    private static final String ATTR_JOURNAL_MAXWORDS = "maxWords";
    private static final String ATTR_ATTACHMENTS = "attachments";
    private static final String ATTR_JOURNAL_CONTENT_HEADER = "contentHeader";
    /*private NoteBookService notebookService = null;
    private AssignmentService assignmentService = null;*/
    private JournalService journalService = null;
    private TaskService taskService;
    private ReviewService reviewService;

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
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);
        
        ActionForward actionFwd = super.checkAssignmentStatus(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }
        //
        // Permitted parameters:
        //  * action   - [MUST]     the action: create, edit, display, review, peerreview, pay, approve
        //  * horseid  - [MUST]     the horse id related with this notebook
        //  * cntid    - [OPTIONAL] the journal content object id of this notebook
        //
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);

        int cntId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_ID), Constants.INVALID_INT_ID);

        if (horseId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }
        //set horse id
        request.setAttribute(PARAM_HORSE_ID, horseId);
        request.setAttribute(PARAM_ASSIGNID, request.getParameter(PARAM_ASSIGNID));
        request.setAttribute("peerReview", reviewService.getJournalPeerReview(loginUser.getPrjid(), loginUser.getUid(), horseId));
        
        final int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_NOTICED) {
                taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_NOTICED);
            }
        }
        
        actionFwd = handleDisplay(mapping, request, cntId, horseId);
        return actionFwd;
    }
    // Handle display

    private ActionForward handleDisplay(ActionMapping mapping,
            HttpServletRequest request, int cntId, int horseId) {
        preHandle(request, cntId, horseId);
        return mapping.findForward(NOTEBOOK_ACTION_DISPLAY);
    }

    // pre handle
    private void preHandle(HttpServletRequest request, int cntId, int horseId) {
    	String title = null;
        String body = null;
        List<Attachment> attachments = null;

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
        /*
        Horse horse = horseDao.get(horseId);
        if(horse != null) {
        request.setAttribute(ATTR_WORKFLOW_OBJECT_ID, horse.getWorkflowObjectId());
        }*/
        request.setAttribute(ATTR_JOURNAL_TITLE, title);
        request.setAttribute(ATTR_JOURNAL_BODY, body);
        request.setAttribute(ATTR_JOURNAL_MINWORDS, minWords);
        request.setAttribute(ATTR_JOURNAL_MAXWORDS, maxWords);
        request.setAttribute(ATTR_ATTACHMENTS, attachments);
    }

    @Autowired
    public void setJournalService(JournalService jouralService) {
        this.journalService = jouralService;
    }
    
    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
}
