/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.loader;

import com.ocs.indaba.dao.AnswerObjectChoiceDAO;
import com.ocs.indaba.dao.AnswerObjectFloatDAO;
import com.ocs.indaba.dao.AnswerObjectIntegerDAO;
import com.ocs.indaba.dao.AnswerObjectTextDAO;
import com.ocs.indaba.dao.AnswerTypeChoiceDAO;
import com.ocs.indaba.dao.AnswerTypeFloatDAO;
import com.ocs.indaba.dao.AnswerTypeIntegerDAO;
import com.ocs.indaba.dao.AnswerTypeTextDAO;
import com.ocs.indaba.dao.AtcChoiceDAO;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.ProjectOwnerDAO;
import com.ocs.indaba.dao.ProjectTargetDAO;
import com.ocs.indaba.dao.ReferenceObjectDAO;
import com.ocs.indaba.dao.ScContributorDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.dao.SurveyCategoryDAO;
import com.ocs.indaba.dao.SurveyConfigDAO;
import com.ocs.indaba.dao.SurveyContentDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.SurveyPeerReviewDAO;
import com.ocs.indaba.dao.SurveyQuestionDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.dao.WorkflowObjectDAO;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.service.SurveyConfigService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author yc06x
 */
public class Loader {

    protected ProcessContext ctx;

    static protected UserDAO userDao = null;
    static protected AnswerTypeChoiceDAO atcDao = null;
    static protected AtcChoiceDAO atccDao = null;
    static protected SurveyIndicatorDAO indicatorDao = null;
    static protected AnswerTypeIntegerDAO atiDao = null;
    static protected AnswerTypeFloatDAO atfDao = null;
    static protected AnswerTypeTextDAO attDao = null;

    static protected SurveyConfigDAO scDao = null;
    static protected ScContributorDAO sccDao = null;
    static protected SurveyCategoryDAO catDao = null;
    static protected SurveyQuestionDAO qstDao = null;
    static protected SurveyConfigService surveyConfigService = null;

    static protected ProductDAO prodDao = null;
    static protected ProjectDAO projDao = null;
    static protected ProjectOwnerDAO projOwnerDao = null;

    static protected WorkflowObjectDAO wfoDao = null;
    static protected HorseDAO horseDao = null;
    static protected ContentHeaderDAO chDao = null;
    static protected SurveyContentDAO scoDao = null;
    static protected AnswerObjectChoiceDAO aocDao = null;
    static protected AnswerObjectIntegerDAO aoiDao = null;
    static protected AnswerObjectFloatDAO aofDao = null;
    static protected AnswerObjectTextDAO aotDao = null;
    static protected ReferenceObjectDAO refObjDao = null;
    static protected SurveyAnswerDAO saDao = null;
    static protected ProjectTargetDAO projTargetDao = null;
    static protected SurveyPeerReviewDAO sprDao = null;

    static {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        userDao = (UserDAO) ctx.getBean("userDao");
        atcDao = (AnswerTypeChoiceDAO) ctx.getBean("answerTypeChoiceDao");
        atccDao = (AtcChoiceDAO) ctx.getBean("atcChoiceDAO");
        indicatorDao = (SurveyIndicatorDAO) ctx.getBean("surveyIndicatorDao");
        atiDao = (AnswerTypeIntegerDAO) ctx.getBean("answerTypeIntegerDao");
        atfDao = (AnswerTypeFloatDAO) ctx.getBean("answerTypeFloatDao");
        attDao = (AnswerTypeTextDAO) ctx.getBean("answerTypeTextDao");

        scDao = (SurveyConfigDAO) ctx.getBean("surveyConfigDao");
        sccDao = (ScContributorDAO) ctx.getBean("scContributorDao");
        catDao = (SurveyCategoryDAO) ctx.getBean("surveyCategoryDao");
        qstDao = (SurveyQuestionDAO) ctx.getBean("surveyQuestionDao");
        surveyConfigService = (SurveyConfigService) ctx.getBean("surveyConfigService");

        prodDao = (ProductDAO) ctx.getBean("productDao");
        projDao = (ProjectDAO) ctx.getBean("projectDao");
        projOwnerDao = (ProjectOwnerDAO) ctx.getBean("projectOwnerDao");

        wfoDao = (WorkflowObjectDAO) ctx.getBean("workflowObjectDao");
        horseDao = (HorseDAO) ctx.getBean("horseDao");
        chDao = (ContentHeaderDAO) ctx.getBean("contentHeaderDao");
        scoDao = (SurveyContentDAO) ctx.getBean("surveyContentDao");
        aocDao = (AnswerObjectChoiceDAO) ctx.getBean("answerObjectChoiceDAO");
        aoiDao = (AnswerObjectIntegerDAO) ctx.getBean("answerObjectIntegerDAO");
        aofDao = (AnswerObjectFloatDAO) ctx.getBean("answerObjectFloatDAO");
        aotDao = (AnswerObjectTextDAO) ctx.getBean("answerObjectTextDAO");
        refObjDao = (ReferenceObjectDAO) ctx.getBean("referenceObjectDao");
        saDao = (SurveyAnswerDAO) ctx.getBean("surveyAnswerDao");

        projTargetDao = (ProjectTargetDAO) ctx.getBean("projectTargetDao");
        sprDao = (SurveyPeerReviewDAO) ctx.getBean("surveyPeerReviewDao");

    }


    protected void createLoader(ProcessContext ctx) {
        this.ctx = ctx;
    }


    protected String getQualifiedName(String name) {
        return "[IMP-" + ctx.getImportId() + "] " + name.trim();
    }

}
