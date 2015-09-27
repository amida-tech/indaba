package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.service.DiscussionBoardService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.MessageVO;
import com.ocs.indaba.vo.UserDisplay;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

public class DiscussionBoardAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(DiscussionBoardAction.class);
//	private static final String PARAM_TYPE = "type";
//	private static final String PARAM_MSGBOARD_NAME = "msgboardname";
    private static final String PARAM_FILTER = "filter";
    private static final String PARAM_SORT_COLUMN = "sc";
    private static final String PARAM_SORT_ORDER = "so";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_BODY = "body";
    private static final String PARAM_MSG_ID= "msgId";
    private static final String PARAM_VIEW_MODE= "viewMode";
    private static final String PARAM_DISCCUSTION_TYPE = "discussionType";
    private static final String ACTION_LOAD = "load";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_UNDELETE = "undelete";
    private DiscussionBoardService discussionBoardService;

    @Autowired
    public void setDiscussionBoardService(DiscussionBoardService discussionBoardService) {
        this.discussionBoardService = discussionBoardService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = preprocess(mapping, request, response);

        logger.debug("DiscussionBoardAction - Write new message request: [userid=" + loginUser.getUid() + "].");

        String action = request.getParameter(PARAM_ACTION);

        if (ACTION_ADD.equalsIgnoreCase(action)) {
            return handleAdd(mapping, form, request, response, loginUser);
        } else if (ACTION_DELETE.equalsIgnoreCase(action)) {
            return handleDelete(mapping, form, request, response, loginUser);
        } else if (ACTION_UNDELETE.equalsIgnoreCase(action)) {
            return handleUndelete(mapping, form, request, response);
        } else {
            return handleLoad(mapping, form, request, response, loginUser);
        }
    }

    private ActionForward handleDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        int msgId = StringUtils.str2int(request.getParameter(PARAM_MSG_ID));
        discussionBoardService.deleteMessage(msgId, loginUser.getUid());
        return null;

    }

    private ActionForward handleUndelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        int msgId = StringUtils.str2int(request.getParameter(PARAM_MSG_ID));
        discussionBoardService.undeleteMessage(msgId);
        return null;

    }

    private ActionForward handleLoad(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        int msgboardId = StringUtils.str2int(request.getParameter("msgboardid"), Constants.INVALID_INT_ID);
        int filter = StringUtils.str2int(request.getParameter(PARAM_FILTER), Constants.INVALID_INT_ID);
        String sortColumn = request.getParameter(PARAM_SORT_COLUMN);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        String discussionType = request.getParameter(PARAM_DISCCUSTION_TYPE);

        List<MessageVO> messages = null;
        
        logger.debug("Load discussions: "
                + "\nmsgboardId=" + msgboardId
                + "\nfilter=" + filter
                + "\nsortColumn=" + sortColumn
                + "\nsortOrder=" + sortOrder
                + "\ndiscussionType=" + discussionType);

        String rightName = null;
        if ("CONTENT_INTERNAL_DISCUSSION".equals(discussionType)) {
            rightName = Rights.MANAGE_CONTENT_INTERNAL_DISCUSSION;
        } else if ("JOURNAL_PEER_REVIEW_DISCUSSIONS".equals(discussionType)) {
            rightName = Rights.MANAGE_JOURNAL_PEER_REVIEW_DISCUSSIONS;
        } else if ("JOURNAL_STAFF_AUTHOR_DISCUSSION".equals(discussionType)) {
            rightName = Rights.MANAGE_JOURNAL_STAFF_AUTHOR_DISCUSSION;
        } else if ("SURVEY_PEER_REVIEW_DISCUSSIONS".equals(discussionType)) {
            rightName = Rights.MANAGE_SURVEY_PEER_REVIEW_DISCUSSIONS;
        } else if ("SURVEY_STAFF_AUTHOR_DISCUSSION".equals(discussionType)) {
            rightName = Rights.MANAGE_SURVEY_STAFF_AUTHOR_DISCUSSION;
        }
        boolean hasManageDisucssionPermission = true;
        
        if (!StringUtils.isEmpty(rightName)) {
            hasManageDisucssionPermission = accessPermissionService.checkProjectPermission(loginUser.getPrjid(), loginUser.getUid(), rightName);
            if (hasManageDisucssionPermission) {
                messages = (msgboardId > 0) ? discussionBoardService.loadAll(loginUser.getPrjid(), loginUser.getUid(), msgboardId, Constants.INVALID_INT_ID, sortColumn, sortOrder) : null;
            } else {
                messages = (msgboardId > 0) ? discussionBoardService.loadActiveOnly(loginUser.getPrjid(), loginUser.getUid(), msgboardId, Constants.INVALID_INT_ID, sortColumn, sortOrder) : null;
            }
        } else {
            messages = (msgboardId > 0) ? discussionBoardService.loadAll(loginUser.getPrjid(), loginUser.getUid(), msgboardId, Constants.INVALID_INT_ID, sortColumn, sortOrder) : null;
        }

        final List<MessageVO> filteredMessages = new ArrayList<MessageVO>();

        if (filter != Constants.INVALID_INT_ID) {
            if (messages != null) {
                for (MessageVO mvo : messages) {
                    if (mvo.getAuthor().getUserId() == filter) {
                        filteredMessages.add(mvo);
                    }
                }
            }
        } else if (messages != null) {
            filteredMessages.addAll(messages);
        }

        request.setAttribute("hasManageDisucssionPermission", hasManageDisucssionPermission);
        request.setAttribute("messages", filteredMessages);
        request.setAttribute(PARAM_VIEW_MODE, request.getParameter(PARAM_VIEW_MODE));

        
        List<UserDisplay> authors;

        if (messages != null) {
            final Set<UserDisplay> authorSet = new HashSet<UserDisplay>();
            for (MessageVO msg : messages) {
                authorSet.add(msg.getAuthor());
            }

            authors = new ArrayList<UserDisplay>(authorSet);
            Collections.sort(authors, new Comparator<UserDisplay>() {

                public int compare(UserDisplay o1, UserDisplay o2) {
                    return o1.getDisplayUsername().compareTo(o2.getDisplayUsername());
                }
            });
        } else {
            authors = new ArrayList<UserDisplay>();
        }

        request.setAttribute("authors", authors);

        return mapping.findForward(FWD_SUCCESS);
    }

    private ActionForward handleAdd(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        int msgboardId = StringUtils.str2int(request.getParameter("msgboardid"), Constants.INVALID_INT_ID);
        String title = request.getParameter(PARAM_TITLE);
        String body = request.getParameter(PARAM_BODY);

        /**
         * * This is to simulate multiple requests in parallel try {
         * Thread.currentThread().sleep(5000); } catch (InterruptedException ex)
         * {
         * java.util.logging.Logger.getLogger(DiscussionBoardAction.class.getName()).log(Level.SEVERE,
         * null, ex); } **
         */
        logger.debug("Adding new discussion comment:"
                + "\nauthor_user_id: " + loginUser.getUid()
                + "\nmsgboardId: " + msgboardId
                + "\ntitle: " + title
                + "\nbody: " + body);

        if (msgboardId <= 0) {
            logger.error("Received bad msgboard ID when adding new discussion comment:"
                + "\nauthor_user_id: " + loginUser.getUid()
                + "\nmsgboardId: " + msgboardId
                + "\ntitle: " + title
                + "\nbody: " + body);

            return mapping.findForward(FWD_ERROR);
        }
        
        discussionBoardService.add(loginUser.getUid(), msgboardId, title, body);

        return handleLoad(mapping, form, request, response, loginUser);
    }
}
