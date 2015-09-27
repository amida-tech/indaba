/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.service.AnswerObjectService;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.TargetTips;
import java.io.PrintWriter;
import java.util.ArrayList;
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
 * @author sjf
 */
public class TargetHoverAction extends com.ocs.indaba.action.BaseAction {

    private static final Logger logger = Logger.getLogger(TargetHoverAction.class);
    //private static final String FWD_Targets = "Targets";
    //private static final String TIPS = "Tips";
    //private static final String TARGETS = "Targets";
    private static final String HORSEID = "horseid";
    //private static final String SURVEY_ANSWER_ID = "surveyanswerid";
    private static final String QUESTION_ID = "questionid";
    private static final String TARGET_NAME = "targetname";
    private HorseService horseService = null;
    private SurveyService surveyService = null;
    private AnswerObjectService answerObjectService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        preprocess(mapping, request, response);
        
        PrintWriter out = response.getWriter();
        int surveyAnswerId = -1;
        int questionid = -1;
        int horseid = -1;
        /*
        String temp = request.getParameter(SURVEY_ANSWER_ID);
        if (temp != null && temp.length() > 0 && !temp.substring(0, 3).equals("NaN")) {
        surveyAnswerId = Integer.parseInt(temp);
        }
        if (surveyAnswerId == -1) {//we will get questionid*/
        String temp = request.getParameter(QUESTION_ID).trim();
        if (temp != null && temp.length() > 0 ) {
            questionid = Integer.parseInt(temp);
        }
        // }
        temp = request.getParameter(HORSEID).trim();
        if (temp != null && temp.length() > 0) {
            horseid = Integer.parseInt(temp);
        }
        temp = request.getParameter(TARGET_NAME);
        if (questionid == -1 && surveyAnswerId == -1) {
            out.write(getMessage(request, Messages.KEY_COMMON_ERR_NOTFINDQUESTION));
        } else if (temp != null && temp.length() > 0) {
            String name = temp.trim();
            if (questionid == -1 && surveyAnswerId != -1) {
                questionid = surveyService.getQuestionIdbySurveyAnswerId(surveyAnswerId);
            }
            int answerType = -1;
            int answerTypeId = -1;
            SurveyIndicator surveyIndicator = surveyService.getSurveyIndicatorByQuestionId(questionid);
            if (surveyIndicator != null) {
                answerType = surveyIndicator.getAnswerType();
                answerTypeId = surveyIndicator.getAnswerTypeId();
            }
            ArrayList<Integer> targetIds = new ArrayList<Integer>();
            ArrayList<Integer> contentHeaderIds = new ArrayList<Integer>();
            List<Horse> horses = horseService.getHorsesWithTheSameProdIdByHorseId(horseid);
            if (horses != null) {
                for (Horse horse : horses) {
                    contentHeaderIds.add(horse.getContentHeaderId());
                    targetIds.add(horse.getTargetId());
                }
            }
            ArrayList<TargetTips> targets = new ArrayList<TargetTips>();
            for (int id : targetIds) {
                targets.add(new TargetTips(trgtService.getTargetById(id)));
            }

            List<Integer> surveyContentObjectIds = surveyService.getSurveyContentObjectIdbyContentHeaderId(contentHeaderIds);
            List<Integer> surveyAnswerObjectIds = surveyService.getAnswerObjectIdby(surveyContentObjectIds, questionid);
            for (int i = 0; i < targets.size(); i++) {
                if (targets.get(i).getName().equals(name)) {
                    out.write(answerObjectService.getAnswerObjectby(surveyAnswerObjectIds.get(i), answerType, answerTypeId));
                    break;
                }
            }
        } else {
            out.write(getMessage(request, Messages.KEY_COMMON_ERR_NOFINDCOUNTTRY));
        }
        return null;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setAnswerObjectService(AnswerObjectService answerObjectService) {
        this.answerObjectService = answerObjectService;
    }
}
