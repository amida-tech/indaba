/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.util.SpringContextUtil;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author Jeff
 */
public class TaskStatusTagHandler extends BaseTagHandler {

    private static final Logger logger = Logger.getLogger(TaskStatusTagHandler.class);
    private static final String DEFAULT_IMAGE_BASE = "./images";
    private static final int DEFAULT_WIDTH = 12;
    private static final String IMG_STATUS_OTHERS = "status_grey.png";
    private static final String IMG_STATUS_USERLOGIN = "status_dred.png";
    private static final String IMG_STATUS_USERCLICKED = "status_pink.png";
    private static final String IMG_STATUS_WORKING = "status_blue.png";
    private static final String IMG_STATUS_COMPLETED = "status_green.png";
    private int uid = 0;
    private int assignid = 0;
    private int width = DEFAULT_WIDTH;
    private String imgbase = DEFAULT_IMAGE_BASE;

    @Override
    public int doStartTag() {
        logger.debug("########################### <indaba:taskstatus> Tag Begin ###########################");
        logger.debug("Task Staus: [userId=" + uid + ", assignid=" + assignid + "].");
        TaskService taskService = (TaskService) SpringContextUtil.getBean("taskService");
        int status = taskService.getTaskStatus(uid, assignid);
        String img = IMG_STATUS_OTHERS;
        String alt = getI18nMessage(Messages.KEY_COMMON_ALT_NOTSTARTED);
        float completed = 0;
        switch (status) {
            case Constants.TASK_ACTION_USERCOMPLETED:
                img = IMG_STATUS_COMPLETED;
                alt = getI18nMessage(Messages.KEY_COMMON_ALT_COMPLETED);
                completed = Constants.COMPLETE_PERCENTAGE_USERSAVEANDDONE;
                break;
            case Constants.TASK_ACTION_USERINPROGRESS:
                img = IMG_STATUS_WORKING;
                alt = getI18nMessage(Messages.KEY_COMMON_ALT_INPROGRESS);
                completed = Constants.COMPLETE_PERCENTAGE_USERSAVE;
                break;
            case Constants.TASK_ACTION_USERCLICKED:
                img = IMG_STATUS_USERCLICKED;
                alt = getI18nMessage(Messages.KEY_COMMON_ALT_NOTICED);
                completed = Constants.COMPLETE_PERCENTAGE_USERCLICKED;
                break;
            case Constants.TASK_ACTION_USERLOGIN:
                img = IMG_STATUS_USERLOGIN;
                alt = getI18nMessage(Messages.KEY_COMMON_ALT_SIGNEDIN);
                completed = Constants.COMPLETE_PERCENTAGE_USERLOGIN;
                break;
            default:
                img = IMG_STATUS_OTHERS;
                alt = getI18nMessage(Messages.KEY_COMMON_ALT_NOTSTARTED);
                completed = Constants.COMPLETE_PERCENTAGE_INACTIVE;

        }
        StringBuilder sBuf = new StringBuilder();
        sBuf.append("<span class='percentage_text'>");
        sBuf.append("<img src='").append(imgbase).append("/").append(img).append("' width='").
                append(width).append("'  alt='").append(alt).append("' ").append("title='").append(alt).append("'").append("/>");
        sBuf.append("&nbsp;&nbsp;").append((int)(completed * 100)).append(getI18nMessage(Messages.KEY_COMMON_TH_COMPLETEPERCENTAGE)).append("</span>");
        JspWriter out = pageContext.getOut();
        try {
            out.print(sBuf.toString());
        } catch (IOException ex) {
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:taskstatus> Tag End ###########################");
        return Tag.EVAL_PAGE;

    }

    public void setBodyContent() {
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

    /**
     * @param right the right to set
     */
    public void setAssignid(Object assignid) {
        try {
            this.assignid = (Integer) ExpressionEvaluatorManager.evaluate("right", assignid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set task assignment id error" + ex);
        }
    }

    /**
     * @param uid the uid to set
     */
    public void setWidth(Object width) {
        try {
            this.width = (Integer) ExpressionEvaluatorManager.evaluate("uid", width.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set width error" + ex);
        }
    }

    /**
     * @param uid the uid to set
     */
    public void setImgbase(Object imgbase) {
        try {
            this.imgbase = (String) ExpressionEvaluatorManager.evaluate("uid", imgbase.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set imgbase error" + ex);
        }
    }
}

