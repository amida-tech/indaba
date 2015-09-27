/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Cases;
import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.indaba.vo.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author Jeff Jiang
 */
public abstract class BaseHorseContentTagHandler extends BaseTagHandler {

    private static final Logger logger = Logger.getLogger(BaseHorseContentTagHandler.class);
    private static final int MAX_BLOCKS_PER_LINE = 15;
    private static final String TASK_DISPLAY_PATTERN = "{0} {1}";
    private AccessPermissionService accessPermissionService = (AccessPermissionService) SpringContextUtil.getBean("accessPermissionService");
    private ViewPermissionService viewPermissionService = (ViewPermissionService) SpringContextUtil.getBean("viewPermissionService");
    private TaskService taskService = (TaskService) SpringContextUtil.getBean("taskService");
    private UserDAO userDao = (UserDAO) SpringContextUtil.getBean("userDao");
    /*
     * attribute for access
     */
    protected int prjid = 0;
    protected int uid = 0;
    private int blocks = 0;


    protected void outputHorse(JspWriter out, ActiveHorseView horseView) throws IOException {
        if (horseView == null) {
            return;
        }
        blocks = 0;
        StringBuilder sBuf = new StringBuilder();
        out.print("<tr>");

        // Content name
        out.print("<td>");
        out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
        String contentName = getContentName(horseView.getTargetName(), horseView.getProductName());
        sBuf.append(contentName);

        String contentDeailUrl = getContentUrl(horseView.getContentType(), horseView.getHorseId());

        boolean showUrl = true;
        if (horseView.getWorkflowObjectStatus() == Constants.WORKFLOW_OBJECT_STATUS_WAITING) {
            showUrl = false;
        } else {
            // LIGHTHOUSE-94: check overall right READ_CONTENT_DETAILS
            if (horseView.isUserJoinedIn()) {
                showUrl = accessPermissionService.isPermitted(prjid, uid, Rights.READ_CONTENT_DETAILS);
            }
            else {
                showUrl = accessPermissionService.isPermitted(prjid, uid, Rights.READ_CONTENT_DETAILS_OF_OTHERS);
            }
        }
        // accessPermissionService.isPermitted(prjid, uid, "display content");
        /*
         * if (showUrl) { sBuf.append("<a
         * href='").append(contentDeailUrl).append("'>").
         * append(contentName).append("</a>"); } else {
         * sBuf.append(contentName); }
         *
         */
        //if (horseView.getWorkflowObjectStatus() == Constants.WORKFLOW_OBJECT_STATUS_SUSPENDED) {
        if (horseView.getWorkflowObjectStatus() < 0) { // Suspended workflow object
            sBuf.append("&nbsp;&nbsp;<img width='15' src='./images/stop_sign.png' alt='").
                    append(getI18nMessage(Messages.KEY_COMMON_REMARK_SUSPENDED)).append("'/>");
        }
        out.print(sBuf.toString());
        out.print("</td>");

        // Goals
        out.print(" <td valign='top'>");
        List<SequenceObjectView> sequences = horseView.getSequences();
        if (sequences != null) {
            int seqId = 0;
            for (SequenceObjectView seqObj : sequences) {
                ++seqId;
                sBuf.setLength(0);// clear buffer
                appendSequence(sBuf, seqObj, seqId, horseView.getHorseId());
                //sBuf.append("<br/>");
                //sBuf.append("<span class='blk_blank_green'/>");
                out.print(sBuf.toString());
            }
            out.flush();
            sequences.clear();
            sequences = null;
        }

        out.print("</td>");

        if (showUrl) {
            out.print("<td align='center'><a href='" + contentDeailUrl + "' title='" + getI18nMessage(Messages.KEY_COMMON_ALT_VIEWCONTENT) + "'>"
                    + "<img src='images/view.png' alt='" + getI18nMessage(Messages.KEY_COMMON_ALT_VIEWCONTENT) + "'/></a></td>");
        } else {
            out.print("<td></td>");
        }

        // History Chart
        if (horseView.getWorkflowObjectStatus() == Constants.WORKFLOW_OBJECT_STATUS_WAITING) {
            out.print("<td align='center'><img src='images/chart-bw.png' alt='" + getI18nMessage(Messages.KEY_COMMON_ALT_HISTORYCHART) + "'/></td>");
        } else {
            out.print("<td align='center'><a href='#loadChart' onclick='loadChart(\"" + horseView.getHorseId() + "\")' title='" + getI18nMessage(Messages.KEY_COMMON_ALT_HISTORYCHART) + "'>"
                    + "<img src='images/chart.png' alt='" + getI18nMessage(Messages.KEY_COMMON_ALT_HISTORYCHART) + "'/></a></td>");
        }

        // Goal
        out.print("<td nowrap>");
        String durTime = DateUtils.date2Str(horseView.getDueTime(), DateUtils.DEFAULT_DATE_FORMAT_2);
        if (durTime == null || "".equals(durTime)) {
            durTime = "--";
        }
        out.print(durTime);
        out.print("</td>");

        // Estimation
        out.print("<td nowrap>");
        String estTime = DateUtils.date2Str(horseView.getEstimationTime(), DateUtils.DEFAULT_DATE_FORMAT_2);
        if (estTime == null || "".equals(estTime)) {
            estTime = "--";
        }
        out.print(estTime);
        out.print("</td></tr>");

        //________________ Next Step
        AssignedTask task = horseView.getActiveTask();

        if (task == null) {
            out.print("<td colspan='5'><span class='small_txt' style='color: gray'>&nbsp;&nbsp;&nbsp;&nbsp;" + getI18nMessage(Messages.KEY_COMMON_MSG_NEXT_STEP) + ": -- </span></td></tr>");
            out.flush();
            return;
        }

        UserDisplay userDisplay = viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, task.getAssignedUserId());
        String displayName = (userDisplay != null) ? userDisplay.getDisplayUsername() : userDao.selectUserById(task.getAssignedUserId()).getUsername();

        if ((task != null) && (task.getAssignedUserId() == uid)) {
            displayName = " " + getI18nMessage(Messages.KEY_COMMON_LABEL_YOU) + " ";
        }
        out.print("<td colspan='5'>");
        out.print("<span class='small_txt'>&nbsp;&nbsp;&nbsp;&nbsp;" + getI18nMessage(Messages.KEY_COMMON_MSG_NEXT_STEP));
        sBuf.setLength(0);
        if (displayName == null) {
            sBuf.append(" <span class='red_small_txt'>").
                    append(super.getI18nMessage(Messages.KEY_COMMON_MSG_NO_ASSIGN, task.getProductName())).append("'</span>");
        } else {
            String userLink = null;
            if (userDisplay != null && userDisplay.getPermission() != Constants.VIEW_PERMISSION_NONE) {
                userLink = (" <a href='profile.do?targetUid=") + task.getAssignedUserId() + "'>" + displayName + ("</a>");
            } else {
                userLink = (" <span style='color: gray'>") + displayName + ("</span>");
            }
            sBuf.append(MessageFormat.format(getI18nMessage(Messages.KEY_COMMON_LABEL_WILLDO), userLink, "'" + task.getTaskName() + "'"));
        }
        out.print(sBuf.toString());
        out.print("</span></tr>");
        out.flush();
    }

    protected void outputHorseDetails(JspWriter out, ActiveHorseView horseView) throws IOException {
        outputHorseDetails(out, horseView, true);
    }

    protected void outputHorseDetails(JspWriter out, ActiveHorseView horseView, boolean checkUid) throws IOException {
        if (horseView == null) {
            return;
        }
        blocks = 0;
        StringBuilder sBuf = new StringBuilder();
        out.print("<tr>");

        // Content name
        out.print("<td>");
        String contentName = getContentName(horseView.getTargetName(), horseView.getProductName());
        sBuf.append("<span>").append(contentName).append("</span>");

        String contentDeailUrl = getContentUrl(horseView.getContentType(), horseView.getHorseId());

        boolean showUrl = true;
        if (horseView.getWorkflowObjectStatus() == Constants.WORKFLOW_OBJECT_STATUS_WAITING) {
            showUrl = false;
        } else if (checkUid) {
            if (horseView.isUserJoinedIn()) {
                showUrl = accessPermissionService.isPermitted(prjid, uid, Rights.READ_CONTENT_DETAILS);
            } else {
                showUrl = accessPermissionService.isPermitted(prjid, uid, Rights.READ_CONTENT_DETAILS_OF_OTHERS);
            }
        }

        // accessPermissionService.isPermitted(prjid, uid, "display content");
        /*
         * if (showUrl) { sBuf.append("<a
         * href='").append(contentDeailUrl).append("'>").
         * append(contentName).append("</a>"); } else {
         * sBuf.append("<span>").append(contentName).append("</span>"); }
         *
         */
        if (horseView.getWorkflowObjectStatus() < 0) { // " + getI18nMessage(KEY_BASEHORSECONTENT_TITLE_SUSPEND) + " workflow object
            sBuf.append("&nbsp;&nbsp;<img width='15' src='./images/stop_sign.png' alt='").
                    append(getI18nMessage(Messages.KEY_COMMON_REMARK_SUSPENDED)).append("'/>");
        }
        out.print(sBuf.toString());
        out.print("</td>");

        // Goal's Status
        out.print(" <td>");
        List<SequenceObjectView> sequences = horseView.getSequences();
        if (sequences != null) {
            int seqId = 0;
            for (SequenceObjectView seqObj : sequences) {
                ++seqId;
                sBuf.setLength(0);// clear buffer
                appendSequence(sBuf, seqObj, seqId, horseView.getHorseId());
                out.print(sBuf.toString());
            }
            out.flush();
            sequences.clear();
            sequences = null;
        }
        out.print("</td>");

        // Done
        out.print("<td>");
        int percentage = (int) (horseView.getCompleted() * 100);
        if (percentage < 0) {
            percentage = 0;
        }
        out.print(percentage + "%");
        out.print("</td>");

        if (showUrl) {
            out.print("<td align='center'><a href='" + contentDeailUrl + "' title='view the content'><img src='images/view.png' alt='" + getI18nMessage(Messages.KEY_COMMON_ALT_VIEWCONTENT) + "'/></a></td>");
        } else {
            out.print("<td></td>");
        }

        // " + getI18nMessage(Messages.KEY_COMMON_ALT_HISTORYCHART) + "(if the horse is not started, keep it unclickable.)
        if (horseView.getWorkflowObjectStatus() == Constants.WORKFLOW_OBJECT_STATUS_WAITING) {
            out.print("<td align='center'><img src='images/chart-bw.png' alt='" + getI18nMessage(Messages.KEY_COMMON_ALT_HISTORYCHART) + "'/></td>");
        } else {
            out.print("<td align='center'><a href='#loadChart' onclick='loadChart(\"" + horseView.getHorseId() + "\")' title='" + getI18nMessage(Messages.KEY_COMMON_ALT_HISTORYCHART) + "'><img src='images/chart.png' alt='" + getI18nMessage(Messages.KEY_COMMON_ALT_HISTORYCHART) + "'/></a></td>");
        }

        // Next Due
        out.print("<td>");
        AssignedTask nextTask = horseView.getActiveTask();
        String dueTime = "--";
        if (nextTask != null) {
            Date startTime = nextTask.getStartTime();
            if (nextTask.getDurTime() != null) {
                dueTime = DateUtils.date2Str(nextTask.getDurTime(), DateUtils.DEFAULT_DATE_FORMAT_2);
            } else {
                if (startTime == null) {
                    startTime = new Date();
                }

                dueTime = DateUtils.date2Str(DateUtils.nextIntervalDays(startTime, nextTask.getDuration()), DateUtils.DEFAULT_DATE_FORMAT_2);
            }
        }
        out.print(dueTime);
        out.print("</td>");

        // Open Cases
        out.print("<td>");
        sBuf.setLength(0);// clear buffer
        List<Cases> openCases = horseView.getOpenCases();
        if (openCases == null || openCases.size() <= 0) {
            sBuf.append("--");
        } else {
            Cases caze = null;
            for (int i = 0, n = openCases.size(); i < n; ++i) {
                caze = openCases.get(i);
                sBuf.append("<a href='casedetail.do?caseid=").append(caze.getId()).
                        append("' title='").append(caze.getTitle()).append("'>#").
                        append(caze.getId()).append("</a>");
                if (i != n - 1) {
                    sBuf.append(", ");
                }
            }
        }
        out.print(sBuf.toString());
        out.print("</td>");

        // People Assigned
        out.print("<td class='td-cls-people-assigned'>");
        sBuf.setLength(0);// clear buffer
        List<UserDisplay> userDisplays = horseView.getPeopleAssigned();
        if (userDisplays == null || userDisplays.size() <= 0) {
            sBuf.append("--");
        } else {
            UserDisplay ud = null;
            for (int i = 0, n = userDisplays.size(); i < n; ++i) {
                ud = userDisplays.get(i);
                if (ud.getPermission() == Constants.ACCESS_PERMISSION_NO) {
                    sBuf.append(ud.getDisplayUsername());
                } else {
                    sBuf.append("<a href='profile.do?targetUid=").
                            append(ud.getUserId()).append("' title='").
                            append(ud.getDisplayUsername()).append("'>").
                            append(ud.getDisplayUsername()).append("</a>");
                }
                if (i != n - 1) {
                    sBuf.append(", ");
                }
            }
            userDisplays.clear();
            userDisplays = null;
        }
        out.print(sBuf.toString());
        out.print("</td></tr>");
        out.flush();
    }

    /**
     * Get horseView content name
     *
     * @param targetName
     * @param productName
     * @return
     */
    protected String getContentName(String targetName, String productName) {
        return MessageFormat.format(TASK_DISPLAY_PATTERN, targetName, productName);
    }

    /**
     * Get the horseView content url
     *
     * @param contentType
     * @param horseId
     * @return
     */
    protected String getContentUrl(int contentType, int horseId) {
        String action = (contentType == Constants.CONTENT_TYPE_JOURNAL) ? Constants.CONTENT_JOURNAL_ACTION : Constants.CONTENT_SURVEY_ACTION;
        StringBuilder sBuf = new StringBuilder();
        sBuf.append(action).append("?action=display&horseid=").append(horseId);
        return sBuf.toString();
    }

    /**
     * Get the horseView content url
     *
     * @param contentType
     * @param horseId
     * @return
     */
    protected void appendSequence(StringBuilder sBuf, SequenceObjectView seqObj, int seqId, int horseId) {
        List<GoalObjectView> goalObjectList = null;
        if (seqObj == null || (goalObjectList = seqObj.getGoalObjects()) == null) {
            return;
        }

        for (GoalObjectView goalObj : goalObjectList) {
            ++blocks;
            appendGoal(sBuf, goalObj, seqId, horseId);
            //sBuf.append("<span class='blk_blank'/>");
        }
    }

    protected void appendGoal(StringBuilder sBuf, GoalObjectView goalObj, int seqId, int horseId) {
        Date enterTime = goalObj.getEnterTime();
        long t1 = enterTime == null ? 0 : (enterTime.getTime() + goalObj.getDuration() * Constants.MILLSECONDS_PER_DAY);
        Date maxDuetime = taskService.getMaxDuetimeByGoalObjectId(goalObj.getGoalObjId());
        long t2 = (maxDuetime == null) ? 0L : maxDuetime.getTime();
        if (goalObj.getStatus() != Constants.GOAL_OBJECT_STATUS_DONE && enterTime != null && (t1 > t2 ? t1 : t2) < System.currentTimeMillis()) {
            goalObj.setStatus(Constants.GOAL_OBJECT_STATUS_OVERDUE);
        }
        String cssStyle = "";
        switch (goalObj.getStatus()) {
            case Constants.GOAL_OBJECT_STATUS_WAITING: {
                cssStyle = "status-blk-waiting";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_STARTING: {
                cssStyle = "status-blk-starting";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_STARTED: {
                cssStyle = "status-blk-started";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_DONE: {
                cssStyle = "status-blk-completed";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_OVERDUE:
            default: {
                cssStyle = "status-blk-overdue";
                break;
            }
        }

        sBuf.append("<a href='#' onclick='displayTasks(").
                append(horseId).
                append(",").
                append(goalObj.getGoalId()).
                append("); return false;' class='status-blk ").
                append(cssStyle).append("' title='").append(goalObj.getGoalName()).append("'>").append("</a>");
        //sBuf.append("<a  href = '#'  onclick='return false;' class='status-blk ").append(cssStyle).append("' title='").append(goalObj.getGoalName()).append("'>").append(seqId).append("</a>");
        if (blocks > MAX_BLOCKS_PER_LINE) {
            blocks = 0;
            sBuf.append("<br/>");
        }
    }

    /**
     * @param prjid the prjid to set
     */
    public void setPrjid(Object prjid) {
        try {
            this.prjid = (Integer) ExpressionEvaluatorManager.evaluate("prjid", prjid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Object uid) {
        try {
            this.uid = (Integer) ExpressionEvaluatorManager.evaluate("uid", uid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }
}
