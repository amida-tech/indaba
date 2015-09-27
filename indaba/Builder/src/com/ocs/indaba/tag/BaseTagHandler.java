/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import java.text.MessageFormat;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jeff
 */
public class BaseTagHandler extends BodyTagSupport {

    /**
     * KEY FOR BaseHorseContentTagHandler
     */
    //protected static final String KEY_BASEHORSECONTENT_TITLE_VIEW_CONTENT = "java.tag.basehorse.title.viewcontent";
    //protected static final String KEY_BASEHORSECONTENT_ITLE_HISTORY_CHART = "java.tag.basehorse.title.historychart";
    //protected static final String KEY_BASEHORSECONTENT_TITLE_SUSPENDED = "java.tag.basehorse.title.suspended";
    //protected static final String KEY_BASEHORSECONTENT_NEXT_STEP = "java.tag.basehorse.nextstep";
    //protected static final String KEY_BASEHORSECONTENT_NO_ASSIGN = "java.tag.basehorse.noassign";
    /**
     * KEY FOR YourAssignmentTagHandler
     */
    //protected static final String KEY_YOURASSIGNMENT_TH_STATUS = "java.tag.yourassignment.th.status";
    //protected static final String KEY_YOURASSIGNMENT_th_DEADLINE = "java.tag.yourassignment.th.deadline";
    //protected static final String KEY_YOURASSIGNMENT_REMARK_SUSPEND = "java.tag.yourassignment.remark.suspend";
    //protected static final String KEY_YOURASSIGNMENT_REMARK_INACTIVE = "java.tag.yourassignment.remark.inactive";
    //protected static final String KEY_YOURASSIGNMENT_REMARK_ACTIVE = "java.tag.yourassignment.remark.active";
    //protected static final String KEY_YOURASSIGNMENT_REMARK_AWARE = "java.tag.yourassignment.remark.aware";
    //protected static final String KEY_YOURASSIGNMENT_REMARK_NOTICED = "java.tag.yourassignment.remark.noticed";
    //protected static final String KEY_YOURASSIGNMENT_REMARK_STARTED = "java.tag.yourassignment.remark.started";
    //protected static final String KEY_YOURASSIGNMENT_REMARK_DONE = "java.tag.yourassignment.remark.done";
    //protected static final String KEY_YOURASSIGNMENT_NODATA = "java.tag.yourassignment.nodata";
    //protected static final String KEY_YOURASSIGNMENT_EDIT = "java.tag.yourassignment.edit";
    /**
     * KEY FOR AllContentTagHandler
     */
    //protected static final String KEY_ALLCONTENT_TH_STATUS = "java.tag.allcontent.th.status";
    //protected static final String KEY_ALLCONTENT_TH_DONE_PERCENT = "java.tag.allcontent.th.donepercent";
    //protected static final String KEY_ALLCONTENT_TH_VIEW = "java.tag.allcontent.th.view";
    //protected static final String KEY_ALLCONTENT_TH_HISTORY = "java.tag.allcontent.th.history";
    //protected static final String KEY_ALLCONTENT_TH_NEXTDUE = "java.tag.allcontent.th.nextdue";
    //protected static final String KEY_ALLCONTENT_TH_OPENCASES = "java.tag.allcontent.th.opencases";
    //protected static final String KEY_ALLCONTENT_TH_PEOPLEASSIGNED = "java.tag.allcontent.th.peopleassigned";
    /**
     * KEY FOR YourContentTagHandler
     */
    /*
     protected static final String KEY_YOURCONTENT_TH_STATUS = "java.tag.yourcontent.th.status";
     protected static final String KEY_YOURCONTENT_TH_VIEW = "java.tag.yourcontent.th.view";
     protected static final String KEY_YOURCONTENT_TH_HISTORY = "java.tag.yourcontent.th.history";
     protected static final String KEY_YOURCONTENT_TH_GOAL = "java.tag.yourcontent.th.goal";
     protected static final String KEY_YOURCONTENT_TH_ESTIMATE = "java.tag.yourcontent.th.estimate";
     protected static final String KEY_YOURCONTENT_NODATA = "java.tag.yourcontent.nodata";
     */
    /**
     * KEY FOR AccessTagHandler(Ignored)
     */
    /**
     * KEY FOR FilterTagHandler(Ignored)
     */
    /*
     protected static final String KEY_FILTER_H4_PRODUCT = "java.tag.filter.h4.product";
     protected static final String KEY_FILTER_CHOICE_EXCLUDE = "java.tag.filter.choice.exclude";
     protected static final String KEY_FILTER_CHOICE_INCLUDE = "java.tag.filter.choice.include";
     protected static final String KEY_FILTER_CHOICE_ALL = "java.tag.filter.choice.all";
     protected static final String KEY_FILTER_CHOICE_OVERDUE = "java.tag.filter.choice.overdue";
     protected static final String KEY_FILTER_CHOICE_NOTOVERDUE = "java.tag.filter.choice.notoverdue";
     protected static final String KEY_FILTER_ACTION_ADD = "java.tag.filter.action.add";
     */
    /**
     * KEY FOR MessageAssignmentsTagHandler(Ignored)
     */
    //protected static final String KEY_MSGASSIGN_TH_STATUS = "java.tag.msgassign.th.status";
    //protected static final String KEY_MSGASSIGN_TH_DEADLINE = "java.tag.msgassign.th.deadline";
    /*
     protected static final String KEY_MSGASSIGN_REMARK_SUSPENDED = "java.tag.msgassign.remark.suspended";
     protected static final String KEY_MSGASSIGN_REMARK_DONE = "java.tag.msgassign.remark.done";
     protected static final String KEY_MSGASSIGN_REMARK_INACTIVE = "java.tag.msgassign.remark.inactive";
     protected static final String KEY_MSGASSIGN_REMARK_ACTIVE = "java.tag.msgassign.remark.active";
     protected static final String KEY_MSGASSIGN_REMARK_AWARE = "java.tag.msgassign.remark.aware";
     protected static final String KEY_MSGASSIGN_REMARK_STARTED = "java.tag.msgassign.remark.started";
     protected static final String KEY_MSGASSIGN_REMARK_NOTICED = "java.tag.msgassign.remark.noticed";
     */
    /**
     * KEY FOR SurveyAnswerTagHandler(Ignored)
     */
    //protected static final String KEY_SURVEYANSWER_TITLE_ANSWER = "java.tag.surveyanswer.title.answer";
    /**
     * KEY FOR TaskStatusTagHandler(Ignored)
     */
    /*
     protected static final String KEY_TASKSTATUS_ALT_NOTSTARTED = "java.tag.taskstatus.alt.notstarted";
     protected static final String KEY_TASKSTATUS_ALT_COMPLETED = "java.tag.taskstatus.alt.completed";
     protected static final String KEY_TASKSTATUS_ALT_INPROGRESS = "java.tag.taskstatus.alt.inprogress";
     protected static final String KEY_TASKSTATUS_ALT_NOTICED = "java.tag.taskstatus.alt.noticed";
     protected static final String KEY_TASKSTATUS_ALT_SIGNEDIN = "java.tag.taskstatus.alt.signedin";
     protected static final String KEY_TASKSTATUS_COMPLETEPERCENTAGE = "java.tag.taskstatus.completepercentage";
     */
    /**
     * KEY FOR TeamContentTagHandler
     */
    //protected static final String KEY_TEAMCONTENT_H3_TEAM_CONTENT = "java.tag.teamcontent.h3.teamcontent";
    /*
     protected static final String KEY_TEAMCONTENT_TH_STATUS = "java.tag.teamcontent.th.status";
     protected static final String KEY_TEAMCONTENT_TH_VIEW = "java.tag.teamcontent.th.view";
     protected static final String KEY_TEAMCONTENT_TH_HISTORY = "java.tag.teamcontent.th.history";
     protected static final String KEY_TEAMCONTENT_TH_GOAL = "java.tag.yourassignment.th.goal";
     protected static final String KEY_YOURASSIGNMENT_th_ESTIMATE = "java.tag.yourassignment.th.estimate";
     */
    public int getLanguageId() {
        String lang = (String) pageContext.getRequest().getAttribute(Constants.ATTR_LANG);

        int langId = Constants.LANG_EN;
        if (!StringUtils.isEmpty(lang)) {
            langId = Integer.parseInt(lang);
        }

        return langId;
    }

    public int getUserId() {
        return (Integer) pageContext.getRequest().getAttribute(Constants.ATTR_USERID);
    }

    public String getI18nMessage(String key) {
        return Messages.getInstance().getMessage(key, getLanguageId());
    }

    public String getI18nMessage(String key, Object... arguments) {
        return MessageFormat.format(Messages.getInstance().getMessage(key, getLanguageId()), arguments);
    }
}
