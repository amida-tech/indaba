/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.AnswerObjectChoiceDAO;
import com.ocs.indaba.dao.AnswerObjectFloatDAO;
import com.ocs.indaba.dao.AnswerObjectIntegerDAO;
import com.ocs.indaba.dao.AnswerObjectTextDAO;
import com.ocs.indaba.dao.AnswerTypeDAO;
import com.ocs.indaba.dao.AtcChoiceDAO;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.ReferenceDAO;
import com.ocs.indaba.dao.ReferenceObjectDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.dao.SurveyCategoryDAO;
import com.ocs.indaba.dao.SurveyConfigDAO;
import com.ocs.indaba.dao.SurveyContentDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.SurveyPeerReviewDAO;
import com.ocs.indaba.dao.SurveyQuestionDAO;
import com.ocs.indaba.dao.ToolDAO;
import com.ocs.indaba.po.AnswerObjectChoice;
import com.ocs.indaba.po.AnswerObjectFloat;
import com.ocs.indaba.po.AnswerObjectInteger;
import com.ocs.indaba.po.AnswerObjectText;
import com.ocs.indaba.po.AnswerTypeFloat;
import com.ocs.indaba.po.AnswerTypeInteger;
import com.ocs.indaba.po.AnswerTypeText;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Reference;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.po.SurveyContentObject;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.po.SurveyQuestion;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.vo.PeerReviewComment;
import com.ocs.indaba.vo.SurveyAnswerProblemVO;
import com.ocs.indaba.vo.SurveyAnswerSubmitView;
import com.ocs.indaba.vo.SurveyAnswerView;
import com.ocs.indaba.vo.SurveyCategoryView;
import com.ocs.indaba.vo.SurveyPeerReviewVO;
import java.net.URLDecoder;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.AnswerTypeTableDAO;
import com.ocs.indaba.dao.AtcChoiceIntlDAO;
import com.ocs.indaba.dao.ContentVersionDAO;
import com.ocs.indaba.dao.SurveyAnswerVersionDAO;
import com.ocs.indaba.po.SurveyConfig;
import com.ocs.indaba.dao.SurveyIndicatorIntlDAO;
import com.ocs.indaba.po.AnswerTypeTable;
import com.ocs.indaba.po.AtcChoiceIntl;
import com.ocs.indaba.po.ContentVersion;
import com.ocs.indaba.po.SurveyAnswerVersion;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import com.ocs.indaba.survey.table.SurveyTableService;
import com.ocs.indaba.vo.SurveyPeerReviewBasicView;
import com.ocs.util.StringUtils;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author luwb
 */
public class SurveyService {

    private static final Logger logger = Logger.getLogger(SurveyService.class);
    private SurveyQuestionDAO surveyQuestionDao = null;
    private SurveyAnswerDAO surveyAnswerDao = null;
    private SurveyAnswerVersionDAO surveyAnswerVersionDao = null;
    private SurveyCategoryDAO surveyCategoryDao = null;
    private SurveyIndicatorDAO surveyIndicatorDao = null;
    private SurveyIndicatorIntlDAO surveyIndicatorIntlDao = null;
    private SurveyContentDAO surveyContentDao = null;
    private SurveyConfigDAO surveyConfigDao = null;
    private SurveyPeerReviewDAO surveyPeerReviewDao = null;
    private AnswerTypeDAO answerTypeDao = null;
    private AnswerTypeTableDAO answerTypeTableDao = null;
    private AtcChoiceDAO atcChoiceDao = null;
    private AtcChoiceIntlDAO atcChoiceIntlDao = null;
    private ReferenceDAO referenceDao = null;
    private ReferenceObjectDAO referenceObjectDao = null;
    //private AnswerObjectDAO answerObjectDao = null;
    private AnswerObjectChoiceDAO answerObjectChoiceDao = null;
    private AnswerObjectFloatDAO answerObjectFloatDao = null;
    private AnswerObjectIntegerDAO answerObjectIntegerDao = null;
    private AnswerObjectTextDAO answerObjectTextDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    private SurveyCategoryService surveyCategoryService = null;
    private TaskService taskService;
    private HorseService horseService;
    private SiteMessageService siteMsgSrvc;
    private AssignmentService assignmentService;
    private SurveyTableService surveyTableService;
    private ToolDAO toolDao;
    private ContentVersionDAO contentVersionDao = null;
    public static final int SURVEY_UNKNOWN = 0;

    @Autowired
    public void setSurveyQuestionDao(SurveyQuestionDAO surveyQuestionDao) {
        this.surveyQuestionDao = surveyQuestionDao;
    }

    @Autowired
    public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
        this.surveyAnswerDao = surveyAnswerDao;
    }

    @Autowired
    public void setSurveyCategoryDao(SurveyCategoryDAO surveyCategoryDao) {
        this.surveyCategoryDao = surveyCategoryDao;
    }

    @Autowired
    public void setSurveyIndicatorDao(SurveyIndicatorDAO surveyIndicatorDao) {
        this.surveyIndicatorDao = surveyIndicatorDao;
    }

    @Autowired
    public void setSurveyIndicatorIntlDao(SurveyIndicatorIntlDAO surveyIndicatorIntlDao) {
        this.surveyIndicatorIntlDao = surveyIndicatorIntlDao;
    }

    @Autowired
    public void setSurveyConfigDAO(SurveyConfigDAO surveyConfigDao) {
        this.surveyConfigDao = surveyConfigDao;
    }

    @Autowired
    public void setSurveyPeerReviewDao(SurveyPeerReviewDAO surveyPeerReviewDao) {
        this.surveyPeerReviewDao = surveyPeerReviewDao;
    }

    @Autowired
    public void setSurveyContentDAO(SurveyContentDAO surveyContentDao) {
        this.surveyContentDao = surveyContentDao;
    }

    @Autowired
    public void setSurveyAnswerDAO(SurveyAnswerDAO surveyAnswerDao) {
        this.surveyAnswerDao = surveyAnswerDao;
    }

    @Autowired
    public void setAnswerTypeDao(AnswerTypeDAO answerTypeDao) {
        this.answerTypeDao = answerTypeDao;
    }

    @Autowired
    public void setAnswerTypeTableDao(AnswerTypeTableDAO answerTypeTableDao) {
        this.answerTypeTableDao = answerTypeTableDao;
    }

    @Autowired
    public void setAtcChoiceDao(AtcChoiceDAO atcChoiceDao) {
        this.atcChoiceDao = atcChoiceDao;
    }

    @Autowired
    public void setAtcChoiceIntlDao(AtcChoiceIntlDAO atcChoiceIntlDao) {
        this.atcChoiceIntlDao = atcChoiceIntlDao;
    }

    @Autowired
    public void setReferenceDao(ReferenceDAO referenceDao) {
        this.referenceDao = referenceDao;
    }

    @Autowired
    public void setReferenceObjectDao(ReferenceObjectDAO referenceObjectDao) {
        this.referenceObjectDao = referenceObjectDao;
    }
    /*
     @Autowired
     public void setAnswerObjectDao(AnswerObjectDAO answerObjectDao) {
     this.answerObjectDao = answerObjectDao;
     }
     */

    @Autowired
    public void setAnswerObjectChoiceDao(AnswerObjectChoiceDAO answerObjectChoiceDao) {
        this.answerObjectChoiceDao = answerObjectChoiceDao;
    }

    @Autowired
    public void setAnswerObjectFloatDao(AnswerObjectFloatDAO answerObjectFloatDao) {
        this.answerObjectFloatDao = answerObjectFloatDao;
    }

    @Autowired
    public void setAnswerObjectIntegerDao(AnswerObjectIntegerDAO answerObjectIntegerDao) {
        this.answerObjectIntegerDao = answerObjectIntegerDao;
    }

    @Autowired
    public void setAnswerObjectTextDao(AnswerObjectTextDAO answerObjectTextDao) {
        this.answerObjectTextDao = answerObjectTextDao;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setContentVersionDao(ContentVersionDAO cntVerDao) {
        this.contentVersionDao = cntVerDao;
    }

    @Autowired
    public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
        this.surveyCategoryService = surveyCategoryService;
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
    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Autowired
    public void setSurveyTableService(SurveyTableService surveyTableService) {
        this.surveyTableService = surveyTableService;
    }

    @Autowired
    public void setToolDao(ToolDAO toolDao) {
        this.toolDao = toolDao;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setSurveyAnswerVersionDao(SurveyAnswerVersionDAO surveyAnswerVersionDao) {
        this.surveyAnswerVersionDao = surveyAnswerVersionDao;
    }

    public List<SurveyAnswerProblemVO> getSurveyAnswerProblems(int horseId, int langId) {
        List<SurveyAnswerProblemVO> problems = surveyAnswerDao.selectSuveryAnswerProblems(horseId);
        if (problems == null || problems.isEmpty()) {
            return problems;
        }

        List<Integer> indicatorIds = new ArrayList<Integer>();
        for (SurveyAnswerProblemVO p : problems) {
            indicatorIds.add(p.getIndicatorId());
        }

        List<SurveyIndicatorIntl> intlIndicators = surveyIndicatorIntlDao.findByIndicatorIdsAndLanguage(indicatorIds, langId);

        if (intlIndicators == null || intlIndicators.isEmpty()) {
            return problems;
        }

        for (SurveyAnswerProblemVO p : problems) {
            for (SurveyIndicatorIntl intl : intlIndicators) {
                if (p.getIndicatorId() == intl.getSurveyIndicatorId()) {
                    p.setQuestion(intl.getQuestion());
                    break;
                }
            }
        }

        return problems;
    }

    public List<SurveyAnswer> getAllSurveyAnswersByHorse(int horseId) {
        return surveyAnswerDao.getAllSurveyAnswersByHorse(horseId);
    }

    public void addAllSurveyAnswersToQuestionList(int horseId) {
        surveyAnswerDao.addAllSurveyAnswersToQuestionList(horseId);
    }

    public void addTaggedSurveyAnswersToQuestionList(int horseId, String tag) {
        surveyAnswerDao.addTaggedSurveyAnswersToQuestionList(horseId, tag);
    }

    public SurveyIndicator getSurveyIndicatorByQuestionId(int questionId) {
        return surveyIndicatorDao.selectSurveyIndicatorByQuestionId(questionId);
    }

    public List<SurveyCategoryView> getSurveyCategoryTree(int surveyCategoryId) {
        if (surveyCategoryId <= 0) {
            return null;
        }
        List<SurveyCategoryView> surveyCategoryViewList = new ArrayList<SurveyCategoryView>();
        while (surveyCategoryId > 0) {
            SurveyCategory surveyCategory = surveyCategoryDao.get(surveyCategoryId);
            if (surveyCategory == null) {
                break;
            }

            SurveyCategoryView view = new SurveyCategoryView();
            view.setId(surveyCategory.getId());
            view.setParentId(surveyCategory.getParentCategoryId());
            view.setLabel(surveyCategory.getLabel());
            view.setTitle(surveyCategory.getTitle());
            surveyCategoryViewList.add(0, view);
            surveyCategoryId = surveyCategory.getParentCategoryId();
        }
        if (surveyCategoryViewList.size() > 0) {
            return surveyCategoryViewList;
        } else {
            return null;
        }
    }

    public List<Integer> getSurveyContentObjectIdbyContentHeaderId(List<Integer> contentHeaderIds) {
        ArrayList<Integer> surveyContentObjectIds = new ArrayList<Integer>();
        SurveyContentObject surveyContentObject = null;

        if (contentHeaderIds != null) {
            for (int id : contentHeaderIds) {
                surveyContentObject = new SurveyContentObject();
                surveyContentObject = surveyContentDao.getSurveyContentObjectbyContentHeaderId(id);
                if (surveyContentObject != null) {
                    surveyContentObjectIds.add(surveyContentObject.getId());
                } else {
                    surveyContentObjectIds.add(-1);
                }
            }
        }
        return surveyContentObjectIds;
    }

    public List<Integer> getAnswerObjectIdby(List<Integer> surveyContentObjectIds, int surveyQuestionId) {
        ArrayList<Integer> answerObjectId = new ArrayList<Integer>();
        SurveyAnswer surveyAnswer = null;

        if (surveyContentObjectIds != null) {
            for (int id : surveyContentObjectIds) {
                surveyAnswer = new SurveyAnswer();
                surveyAnswer = surveyAnswerDao.selectSurveyAnswerBy(id, surveyQuestionId);
                if (surveyAnswer != null) {
                    answerObjectId.add(surveyAnswer.getAnswerObjectId());
                } else {
                    answerObjectId.add(-1);
                }
            }
        }
        return answerObjectId;
    }

    public int getQuestionIdbySurveyAnswerId(int surveyAnswerId) {
        if (surveyAnswerId <= 0) {
            return -1;
        }
        return surveyAnswerDao.getQuestionIdbySurveyAnswerId(surveyAnswerId);
    }

    public SurveyAnswer getSurveyAnswerById(int surveyAnswerId) {
        return surveyAnswerDao.get(surveyAnswerId);
    }

    public void saveSurveyAnswer(SurveyAnswer answer) {
        SurveyAnswer answer1 = surveyAnswerDao.update(answer);
        SurveyAnswer answer2 = surveyAnswerDao.get(answer.getId());
    }

    private SurveyIndicator getIntlIndicator(int indicatorId, int langId) {
        SurveyIndicator indicator = surveyIndicatorDao.selectSurveyIndicatorById(indicatorId);

        if (langId <= 0) {
            return indicator;
        }

        SurveyIndicatorIntl indicatorIntl = surveyIndicatorIntlDao.findByIndicatorIdAndLanguage(indicatorId, langId);

        if (indicatorIntl != null) {
            if (!StringUtils.isEmpty(indicatorIntl.getQuestion())) {
                indicator.setQuestion(indicatorIntl.getQuestion());
            }

            if (!StringUtils.isEmpty(indicatorIntl.getTip())) {
                indicator.setTip(indicatorIntl.getTip());
            }
        }

        return indicator;
    }

    public SurveyAnswerView getSurveyAnswerView(int horseId, int questionId, int userId, int langId) {
        int survyeAnswerId = getSurveyAnswerId(horseId, questionId, userId);
        return getSurveyAnswerView(survyeAnswerId, langId);
    }

    public SurveyAnswerView getSurveyAnswerView(int surveyAnswerId, int langId) {
        SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
        if (surveyAnswer == null) {
            return null;
        }
        SurveyConfig surveyConfig = surveyConfigDao.selectByAnswerId(surveyAnswerId);
        if (surveyConfig == null) {
            return null;
        }

        SurveyQuestion surveyQuestion = surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId());
        if (surveyQuestion == null) {
            return null;
        }
        List<SurveyCategoryView> surveyCategoryViewList = getSurveyCategoryTree(surveyQuestion.getSurveyCategoryId());

        SurveyIndicator indicator = getIntlIndicator(surveyQuestion.getSurveyIndicatorId(), langId);

        Reference ref = this.referenceDao.selectReferenceById(indicator.getReferenceId());
        SurveyAnswerView view = new SurveyAnswerView();
        view.setSurveyAnswerId(surveyAnswerId);
        view.setCategoryViewList(surveyCategoryViewList);
        view.setAnswerType(indicator.getAnswerType());
        view.setAnswerTypeId(indicator.getAnswerTypeId());
        view.setName(indicator.getName());
        view.setPublicName(surveyQuestion.getPublicName());
        view.setQuestion(indicator.getQuestion());
        view.setTip(indicator.getTip());
        view.setTipDisplayMethod(surveyConfig.getTipDisplayMethod());
        view.setReferType(ref.getChoiceType());
        view.setReferId(ref.getId());
        view.setReferName(ref.getName());
        view.setRefdescrition(ref.getDescription());

        view.setReferenceObjectId(surveyAnswer.getReferenceObjectId());
        view.setAnswerObjectId(surveyAnswer.getAnswerObjectId());
        view.setComments(surveyAnswer.getComments());
        view.setPreVersionMode(false);
        return view;
    }

    public SurveyAnswerView getSurveyAnswerView(int surveyAnswerId) {
        return getSurveyAnswerView(surveyAnswerId, 1);
    }

    public SurveyAnswerView getSurveyAnswerPreVersionView(int cntVersionId, int surveyQuestionId) {
        return getSurveyAnswerPreVersionView(cntVersionId, surveyQuestionId, 0);
    }

    public SurveyAnswerView getSurveyAnswerPreVersionView(int cntVersionId, int surveyQuestionId, int langId) {
        SurveyAnswerVersion surveyAnswerVersion = surveyAnswerVersionDao.getSurveyAnswerVersionByContentVersionAndSurveyQuestion(cntVersionId, surveyQuestionId);
        if (surveyAnswerVersion == null) {
            return null;
        }
        SurveyQuestion surveyQuestion = surveyQuestionDao.get(surveyAnswerVersion.getSurveyQuestionId());
        if (surveyQuestion == null) {
            return null;
        }
        List<SurveyCategoryView> surveyCategoryViewList = getSurveyCategoryTree(surveyQuestion.getSurveyCategoryId());
        SurveyIndicator indicator = surveyIndicatorDao.selectSurveyIndicatorById(surveyQuestion.getSurveyIndicatorId(), langId);
        Reference ref = this.referenceDao.selectReferenceById(indicator.getReferenceId());
        SurveyAnswerView view = new SurveyAnswerView();
        view.setSurveyAnswerId(surveyAnswerVersion.getId());
        view.setCategoryViewList(surveyCategoryViewList);
        view.setAnswerType(indicator.getAnswerType());
        view.setAnswerTypeId(indicator.getAnswerTypeId());
        view.setName(indicator.getName());
        view.setPublicName(surveyQuestion.getPublicName());
        view.setQuestion(indicator.getQuestion());
        view.setReferType(ref.getChoiceType());
        view.setReferId(ref.getId());
        view.setReferName(ref.getName());
        view.setRefdescrition(ref.getDescription());

        view.setReferenceObjectId(surveyAnswerVersion.getReferenceObjectId());
        view.setAnswerObjectId(surveyAnswerVersion.getAnswerObjectId());
        view.setComments(surveyAnswerVersion.getComments());
        view.setPreVersionMode(true);
        return view;
    }

    public AnswerTypeFloat getAnswerTypeFloat(int answerTypeId) {
        return answerTypeDao.getAnswerTypeFloat(answerTypeId);
    }

    public AnswerTypeInteger getAnswerTypeInteger(int answerTypeId) {
        return answerTypeDao.getAnswerTypeInteger(answerTypeId);
    }

    public AnswerTypeText getAnswerTypeText(int answerTypeId) {
        return answerTypeDao.getAnswerTypeText(answerTypeId);
    }

    public AnswerTypeTable getAnswerTypeTable(int answerTypeId) {
        return answerTypeTableDao.get(answerTypeId);
    }

    public List<AtcChoice> getAnswerTypeChoice(int answerTypeId) {
        return atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(answerTypeId);
    }

    // get the choice appropriate for the language
    public List<AtcChoice> getAnswerTypeChoice(int answerTypeId, int langId) {
        List<AtcChoice> acList = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(answerTypeId);

        if (acList == null || acList.isEmpty()) {
            return acList;
        }

        List<AtcChoiceIntl> aciList = atcChoiceIntlDao.selectByTypeIdAndLanguage(answerTypeId, langId);

        if (aciList == null || aciList.isEmpty()) {
            return acList;
        }

        for (AtcChoice ac : acList) {
            for (AtcChoiceIntl aci : aciList) {
                if (aci.getAtcChoiceId() == ac.getId()) {
                    if (!StringUtils.isEmpty(aci.getCriteria())) {
                        ac.setCriteria(aci.getCriteria());
                    }
                    if (!StringUtils.isEmpty(aci.getLabel())) {
                        ac.setLabel(aci.getLabel());
                    }
                    break;
                }
            }
        }

        return acList;
    }

    public AnswerObjectChoice getAnswerObjectChoice(int answerObjectId) {
        return this.answerObjectChoiceDao.getAnswerObjectChoicebyId(answerObjectId);
    }

    public AnswerObjectFloat getAnswerObjectFloat(int answerObjectId) {
        return this.answerObjectFloatDao.getAnswerObjectFloatbyId(answerObjectId);
    }

    public AnswerObjectInteger getAnswerObjectInteger(int answerObjectId) {
        return this.answerObjectIntegerDao.getAnswerObjectIntegerbyId(answerObjectId);
    }

    public AnswerObjectText getAnswerObjectText(int answerObjectId) {
        return this.answerObjectTextDao.getAnswerObjectTextbyId(answerObjectId);
    }

    public SurveyAnswerSubmitView submitSurveyAnswer(int surveyAnswerId, String selection, String source, String sourceDesc, String comments, int uid, int assignId, int horseId, int type, int languageId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        String errorMsg;
        view.setSucceed(false);
        SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
        if (surveyAnswer == null) {
            view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDANSWER, languageId));
            return view;
        }
        SurveyIndicator surveyIndicator = surveyIndicatorDao.get(surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId()).getSurveyIndicatorId());

        if (surveyIndicator == null) {
            view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR, languageId));
            return view;
        }

        int answerObjectId = surveyAnswer.getAnswerObjectId();
        switch (surveyIndicator.getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                long singleChoice = NumberUtils.toLong(selection, -1);
                if (singleChoice < 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SECION, languageId));
                    return view;
                } else {
                    answerObjectId = answerObjectChoiceDao.insertAnswerObjectChoice(answerObjectId, singleChoice);
                }
                break;
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                long multiChoice = getMultiChoiceResult(selection);
                if (multiChoice == 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SECION, languageId));
                    return view;
                } else {
                    answerObjectId = answerObjectChoiceDao.insertAnswerObjectChoice(answerObjectId, multiChoice);
                }
                break;
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                float f = NumberUtils.toFloat(selection, -1);
                if (f < 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SECION, languageId));
                    return view;
                } else {
                    if (this.checkAnswerTypeFloat(surveyIndicator.getAnswerTypeId(), f)) {
                        answerObjectId = answerObjectFloatDao.insertAnswerObjectFloat(answerObjectId, f);
                    } else {
                        errorMsg = Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_INVALID_ANSWER_VALUE, languageId);
                        view.setErrorMsg(errorMsg);
                        return view;
                    }
                }
                break;
            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                int i = NumberUtils.toInt(selection, -1);
                if (i < 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SECION, languageId));
                    return view;
                } else {
                    if (this.checkAnswerTypeInteger(surveyIndicator.getAnswerTypeId(), i)) {
                        answerObjectId = answerObjectIntegerDao.insertAnswerObjectInteger(answerObjectId, i);
                    } else {
                        errorMsg = Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_INVALID_ANSWER_VALUE, languageId);
                        view.setErrorMsg(errorMsg);
                        return view;
                    }
                }
                break;
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                answerObjectId = answerObjectTextDao.insertAnswerObjectText(answerObjectId, selection);
                break;
            default:
                view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
                return view;
        }

        Reference ref = referenceDao.selectReferenceById(surveyIndicator.getReferenceId());
        if (ref == null) {
            view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDREF, languageId));
            return view;
        }

        int referObjectId = surveyAnswer.getReferenceObjectId();
        switch (ref.getChoiceType()) {
            case Constants.REFERENCE_TYPE_NO_CHOICE:
                referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, 0);
                break;
            case Constants.REFERENCE_TYPE_SINGLE_CHOICE:
                int singleChoice = NumberUtils.toInt(source, 0);
                if (singleChoice < 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
                    return view;
                } else {
                    referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, singleChoice);
                }
                break;
            case Constants.REFERENCE_TYPE_MULTI_CHOICE:
                long multiChoice = getMultiChoiceResult(source);
                if (multiChoice == 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
                    return view;
                } else {
                    referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, multiChoice);
                }
                break;
            default:
                view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
                return view;
        }
        logger.debug("<<<<======" + type);
        if (type == Constants.TASK_TYPE_SURVEY_CREATE) {
            surveyAnswerDao.updateSurveyAnswerById(answerObjectId, referObjectId, comments, uid, surveyAnswerId);
        } else if (type == Constants.TASK_TYPE_SURVEY_EDIT) {
            surveyAnswerDao.editUpdateSurveyAnswerById(answerObjectId, referObjectId, comments, surveyAnswerId);
        } else if (type == Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW) {
            surveyAnswerDao.overallReviewUpdateSurveyAnswerById(answerObjectId, referObjectId, comments, surveyAnswerId);
        }

        logger.debug("---------TASK TYPE------>>>" + type);
        if (type == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE) {
            surveyAnswerDao.updateAuthorResponded(surveyAnswerId);
        }
        if (assignId > 0) {
            this.updateTaskAssignmentPercentage(assignId, horseId, type);
        }

        view.setSucceed(true);

        ContentHeader ch = horseService.getContentHeaderByHorseId(horseId);
        if (ch.getAuthorUserId() == 0) {
            ch.setAuthorUserId(uid);
        }
        // Commented by Jeff - Fix bug #552.
        /*if (ch.getSubmitTime() == null) {
         ch.setSubmitTime(new Date());
         }*/
        ch.setLastUpdateUserId(uid);
        ch.setLastUpdateTime(new Date());
        horseService.saveContentHeader(ch);

        return view;
    }

    private long getMultiChoiceResult(String selection) {
        String[] ss = selection.split(",");
        long result = 0;

        if (ss != null) {
            for (String s : ss) {
                if (s.length() != 0) {
                    int value = NumberUtils.toInt(s, 0);
                    if (value == 0) {
                        return 0;
                    }
                    result += value;
                }
            }
        }
        return result;
    }

    private boolean checkAnswerTypeFloat(int answerTypeId, float f) {
        AnswerTypeFloat value = this.answerTypeDao.getAnswerTypeFloat(answerTypeId);
        if ((value.getMinValue() <= f) && (value.getMaxValue() >= f)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAnswerTypeInteger(int answerTypeId, int val) {
        AnswerTypeInteger value = this.answerTypeDao.getAnswerTypeInteger(answerTypeId);
        if ((value.getMinValue() <= val) && (value.getMaxValue() >= val)) {
            return true;
        } else {
            return false;
        }
    }

    public int getSurveyAnswerId(int surveyQuestionId, int horseId, int userId) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return -1;
        }
        int contentObjectId = cntHdr.getContentObjectId();
        SurveyAnswer answer = surveyAnswerDao.selectSurveyAnswerBy(contentObjectId, surveyQuestionId);
        if (answer != null) {
            return answer.getId();
        }

        answer = new SurveyAnswer();
        answer.setSurveyQuestionId(surveyQuestionId);
        answer.setSurveyContentObjectId(contentObjectId);
        answer.setAnswerUserId(userId);
        answer.setAnswerObjectId(0);
        answer.setReferenceObjectId(0);
        SurveyAnswer result = surveyAnswerDao.save(answer);
        return result.getId();
    }

    private boolean updateTaskAssignmentPercentage(int assignId, int horseId, int type) {
        TaskAssignment ta = taskService.getTaskAssignment(assignId);
        if (ta != null && ta.getStatus() == Constants.TASK_STATUS_NOTICED) {
            taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_STARTED);
        }

        Float f = ta.getPercent();
        if ((f != null) && (f.floatValue() >= Constants.TASK_ASSIGNMENT_PERCENT_CAPPED)) {
            return true;


        }
        float percentage;

        if (type == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE) {
            int respondedProblemCount = surveyCategoryService.getRespondedProblemCountByHorseId(horseId);
            int problemCount = surveyCategoryService.getProblemCountByHorseId(horseId);
            percentage = (float) respondedProblemCount / problemCount;
        } else {
            int allSurveyQuestions = this.surveyCategoryService.getSurveyQuestionCountByHorseId(horseId);
            int completeSurveyAnswers = 0;
            if (type == Constants.TASK_TYPE_SURVEY_CREATE) {
                completeSurveyAnswers = this.surveyCategoryService.getCompletedSurveyAnswerCountByHorseId(horseId);
            } else if (type == Constants.TASK_TYPE_SURVEY_EDIT) {
                completeSurveyAnswers = this.surveyCategoryService.getCompletedEditedSurveyAnswerCountByHorseId(horseId);
            } else if (type == Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW) {
                completeSurveyAnswers = this.surveyCategoryService.getCompletedOverallReviewedSurveyAnswerCountByHorseId(horseId);
            }
            percentage = (float) completeSurveyAnswers / allSurveyQuestions;
        }

        if (percentage > Constants.TASK_ASSIGNMENT_PERCENT_CAPPED) {
            percentage = Constants.TASK_ASSIGNMENT_PERCENT_CAPPED;

        }
        this.taskService.updateTaskAssignment(assignId, percentage);
        return true;
    }

    public String getInstructionsbyHorseId(int horseId) {
        return surveyConfigDao.getInstructionsbyHorseId(horseId);
    }

    public void resetSurveyAnswerFlag(int assignId, int horseId) {
        Tool tool = toolDao.selectToolByTaskAssignmentId(assignId);
        int taskType = (tool == null) ? -1 : tool.getTaskType();
        if (taskType != -1) {
            surveyAnswerDao.resetSurveyAnswerFlag(taskType, horseId);
        }
    }

    public void updateSurveyAnswerFlag(int assignId, int horseId, int surveyAnswerId) {
        Tool tool = toolDao.selectToolByTaskAssignmentId(assignId);
        int task_type = (tool == null) ? -1 : tool.getTaskType();
        if (task_type != -1) {
            surveyAnswerDao.updateSurveyAnswerFlag(task_type, surveyAnswerId);
        }
    }

    public boolean allSurveyProblemAnswered(int horseId) {
        List<SurveyAnswer> answerList = surveyAnswerDao.getUnansweredSurveyProblemsByHorse(horseId);
        return (answerList == null || answerList.isEmpty() || answerList.isEmpty());
    }

    public void cancelProblemSurveyAnswers(int horseId) {
        List<SurveyAnswer> answerList = surveyAnswerDao.getProblemSurveyAnswersByHorse(horseId);

        if (answerList != null) {
            for (SurveyAnswer answer : answerList) {
                answer.setReviewerHasProblem(false);
                answer.setAuthorResponded(false);
                surveyAnswerDao.update(answer);
            }
        }
    }

    public void resetProblemSurveyAnswers(int horseId) {
        List<SurveyAnswer> answerList = surveyAnswerDao.getProblemSurveyAnswersByHorse(horseId);

        if (answerList != null) {
            for (SurveyAnswer answer : answerList) {
                answer.setAuthorResponded(false);
                surveyAnswerDao.update(answer);
            }
        }
    }

    public SurveyAnswerSubmitView submitIndicatorReview(int horseId, int assignId, int surveyAnswerId, String source, String sourceDesc, String comments, String prreviews, String answer, int languageId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        view.setSucceed(false);

        SurveyAnswer surveyAnswer = getSurveyAnswerById(surveyAnswerId);
        SurveyIndicator surveyIndicator = selectSurveyIndicatorByQuesitonId(surveyAnswer.getSurveyQuestionId(), languageId);

        if (surveyIndicator == null) {
            view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR, languageId));
            return view;
        }
        int answerObjectId = getSurveyAnswerObjectId(answer, surveyAnswer.getAnswerObjectId(), surveyIndicator, view, languageId);
        if (StringUtils.isNotBlank(view.getErrorMsg())) {
            return view;
        }
        surveyAnswer.setAnswerObjectId(answerObjectId);
        boolean success = fillSourceCommentsAndPrreviews(view, surveyAnswer, surveyIndicator, source, sourceDesc, comments, prreviews, languageId);
        if (!success) {
            return view;
        }
        /*
         Reference ref = referenceDao.selectReferenceById(surveyIndicator.getReferenceId());
         if (ref == null) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDREF, languageId));
         return view;
         }

         int referObjectId = surveyAnswer.getReferenceObjectId();
         switch (ref.getChoiceType()) {
         case Constants.REFERENCE_TYPE_NO_CHOICE:
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, 0);
         break;
         case Constants.REFERENCE_TYPE_SINGLE_CHOICE:
         int singleChoice = NumberUtils.toInt(source, 0);
         if (singleChoice < 0) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
         return view;
         } else {
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, singleChoice);
         }
         break;
         case Constants.REFERENCE_TYPE_MULTI_CHOICE:
         long multiChoice = getMultiChoiceResult(source);
         if (multiChoice == 0) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
         return view;
         } else {
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, multiChoice);
         }
         break;
         default:
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
         return view;
         }
         surveyAnswer.setReferenceObjectId(referObjectId);
         surveyAnswer.setComments(comments);
         */
        surveyAnswer.setStaffReviewed(true);
        surveyAnswerDao.update(surveyAnswer);

        if (StringUtils.isNotBlank(prreviews)) {
            Type t = new TypeToken<List<SurveyPeerReviewVO>>() {
            }.getType();
            List<SurveyPeerReviewVO> sprs = new Gson().fromJson(prreviews, t);

            if (sprs != null) {
                for (SurveyPeerReviewVO spr : sprs) {
                    SurveyPeerReview pspr = surveyPeerReviewDao.get(spr.getId());
                    if (pspr != null) {
                        final short opinion = spr.getOpinion();
                        pspr.setOpinion(opinion);
                        if (opinion == Constants.SURVEY_PEER_DISAGREE) {
                            answerObjectId = getSurveyAnswerObjectId(spr.getSuggestedScore(), pspr.getSuggestedAnswerObjectId(), surveyIndicator, view, languageId);
                            if (StringUtils.isNotBlank(view.getErrorMsg())) {
                                return view;
                            }

                            pspr.setSuggestedAnswerObjectId(answerObjectId);
                        }
                        pspr.setComments(spr.getComments());
                        surveyPeerReviewDao.update(pspr);
                    }
                }
            }
        }

        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_STARTED) {
                assignment.setStatus((short) Constants.TASK_STATUS_STARTED);
            }
            int completed = surveyAnswerDao.getReviewedAnswerCountByHorseId(horseId);
            int total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);

            if (total != 0) {
                final float percent = new Float(completed) / new Float(total);
                assignment.setPercent(percent > 0.95f ? 0.95f : percent);
            } else {
                assignment.setPercent(0f);
            }
            taskService.updateTaskAssignment(assignment);
        }

        view.setSucceed(true);

        return view;
    }

    public SurveyAnswerSubmitView submitIndicatorPrReview(int horseId, int assignId, int surveyAnswerId, String source, String sourceDesc, String comments, String prreviews, String answer, int languageId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        view.setSucceed(false);

        SurveyAnswer surveyAnswer = getSurveyAnswerById(surveyAnswerId);
        SurveyIndicator surveyIndicator = selectSurveyIndicatorByQuesitonId(surveyAnswer.getSurveyQuestionId(), languageId);

        if (surveyIndicator == null) {
            view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR, languageId));
            return view;
        }

        int answerObjectId = getSurveyAnswerObjectId(answer, surveyAnswer.getAnswerObjectId(), surveyIndicator, view, languageId);
        if (StringUtils.isNotBlank(view.getErrorMsg())) {
            return view;
        }
        surveyAnswer.setAnswerObjectId(answerObjectId);
        /*
         Reference ref = referenceDao.selectReferenceById(surveyIndicator.getReferenceId());
         if (ref == null) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDREF, languageId));
         return view;
         }

         int referObjectId = surveyAnswer.getReferenceObjectId();
         switch (ref.getChoiceType()) {
         case Constants.REFERENCE_TYPE_NO_CHOICE:
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, 0);
         break;
         case Constants.REFERENCE_TYPE_SINGLE_CHOICE:
         int singleChoice = NumberUtils.toInt(source, 0);
         if (singleChoice < 0) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
         return view;
         } else {
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, singleChoice);
         }
         break;
         case Constants.REFERENCE_TYPE_MULTI_CHOICE:
         long multiChoice = getMultiChoiceResult(source);
         if (multiChoice == 0) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
         return view;
         } else {
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, multiChoice);
         }
         break;
         default:
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
         return view;
         }

         surveyAnswer.setReferenceObjectId(referObjectId);
         surveyAnswer.setComments(comments);
         */
        boolean success = fillSourceCommentsAndPrreviews(view, surveyAnswer, surveyIndicator, source, sourceDesc, comments, prreviews, languageId);
        if (!success) {
            return view;
        }
        surveyAnswer.setPrReviewed(true);
        surveyAnswerDao.update(surveyAnswer);

        if (StringUtils.isNotBlank(prreviews)) {
            Type t = new TypeToken<List<SurveyPeerReviewVO>>() {
            }.getType();
            List<SurveyPeerReviewVO> sprs = new Gson().fromJson(prreviews, t);

            if (sprs != null) {
                for (SurveyPeerReviewVO spr : sprs) {
                    SurveyPeerReview pspr = surveyPeerReviewDao.get(spr.getId());
                    if (pspr != null) {
                        final short opinion = spr.getOpinion();
                        pspr.setOpinion(opinion);
                        if (opinion == Constants.SURVEY_PEER_DISAGREE) {
                            if (surveyIndicator == null) {
                                view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR, languageId));
                                return view;
                            }

                            answerObjectId = getSurveyAnswerObjectId(spr.getSuggestedScore(), pspr.getSuggestedAnswerObjectId(), surveyIndicator, view, languageId);
                            if (StringUtils.isNotBlank(view.getErrorMsg())) {
                                return view;
                            }

                            pspr.setSuggestedAnswerObjectId(answerObjectId);
                        }
                        pspr.setComments(spr.getComments());
                        surveyPeerReviewDao.update(pspr);
                    }
                }
            }
        }

        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_STARTED) {
                assignment.setStatus((short) Constants.TASK_STATUS_STARTED);


            }
            int completed = surveyAnswerDao.getPRReviewedAnswerCountByHorseId(horseId);
            int total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);

            if (total != 0) {
                final float percent = new Float(completed) / new Float(total);
                assignment.setPercent(percent > 0.95f ? 0.95f : percent);
            } else {
                assignment.setPercent(0f);
            }
            taskService.updateTaskAssignment(assignment);
        }

        view.setSucceed(true);
        return view;
    }

    public SurveyAnswerSubmitView submitIndicatorAnswer(int userId, String type, int horseId,
            int assignId, int surveyAnswerId, String answer, String source, String sourceDesc,
            String comments, String prreviews, int languageId) {
        SurveyAnswerSubmitView view = null;
        if (Constants.SURVEY_ACTION_REVIEW.equalsIgnoreCase(type)) {
            view = submitIndicatorReview(horseId, assignId, surveyAnswerId, source, sourceDesc, comments, prreviews, answer, languageId);
        } else if (Constants.SURVEY_ACTION_PRREVIEW.equalsIgnoreCase(type)) {
            view = submitIndicatorPrReview(horseId, assignId, surveyAnswerId, source, sourceDesc, comments, prreviews, answer, languageId);
        } else {
            view = submitIndicator(userId, type, horseId, assignId, surveyAnswerId, answer, source, sourceDesc, comments, prreviews, languageId);
        }
        return view;
    }

    public SurveyAnswerSubmitView submitIndicatorOverallReview(int userId, int horseId,
            int assignId, int surveyAnswerId, String answer, String source, String sourceDesc, String comments,
            String prreviews, int languageId) {
        return submitIndicator(userId, Constants.SURVEY_ACTION_OVERALLREVIEW, horseId, assignId, surveyAnswerId, answer, source, sourceDesc, comments, prreviews, languageId);
    }

    public void saveSourceCommentsAndPrreviews(SurveyAnswerSubmitView view, int mainSurveyAnswerId,
            String source, String sourceDesc, String comments, String prreviews, int languageId) {
        SurveyAnswer surveyAnswer = getSurveyAnswerById(mainSurveyAnswerId);
        SurveyIndicator surveyIndicator = selectSurveyIndicatorByQuesitonId(surveyAnswer.getSurveyQuestionId(), languageId);
        boolean success = fillSourceCommentsAndPrreviews(view, surveyAnswer, surveyIndicator, source, sourceDesc, comments, prreviews, languageId);
        if (success) {
            surveyAnswerDao.update(surveyAnswer);
        }
    }



    public SurveyAnswerSubmitView processSurveyAnswerPage(HttpServletRequest request, int surveyAnswerId, int horseId, int mainSurveyQuestionId, int userId, int langId, String prreviews) {
        SurveyAnswerSubmitView view = surveyTableService.saveComponentAnswers(request, surveyAnswerId, horseId, mainSurveyQuestionId, userId, langId);
        if (view.getErrorMsg() != null) return view;

        if (StringUtils.isEmpty(prreviews)) return view;

        Type t = new TypeToken<List<SurveyPeerReviewVO>>() {}.getType();
        List<SurveyPeerReviewVO> sprs = new Gson().fromJson(prreviews, t);
        if (sprs == null || sprs.isEmpty()) return view;
        for (SurveyPeerReviewVO spr : sprs) {
            if (spr.getOpinion() == Constants.SURVEY_PEER_DISAGREE) {
                SurveyAnswerSubmitView view2 = surveyTableService.savePeerReviewComponentAnswers(request, spr.getId(), mainSurveyQuestionId, userId, langId);
                if (view2.getErrorMsg() != null) return view2;
            }
        }

        return view;
    }




    private boolean fillSourceCommentsAndPrreviews(SurveyAnswerSubmitView view, SurveyAnswer surveyAnswer, SurveyIndicator surveyIndicator,
            String source, String sourceDesc, String comments, String prreviews, int languageId) {
        Reference ref = referenceDao.selectReferenceById(surveyIndicator.getReferenceId());

        if (ref == null) {
            view.setSucceed(false);
            view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDREF, languageId));
            return false;
        }
        int referObjectId = surveyAnswer.getReferenceObjectId();
        switch (ref.getChoiceType()) {
            case Constants.REFERENCE_TYPE_NO_CHOICE:
                referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, 0);
                break;
            case Constants.REFERENCE_TYPE_SINGLE_CHOICE:
                int singleChoice = NumberUtils.toInt(source, 0);
                if (singleChoice < 0) {
                    view.setSucceed(false);
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
                    return false;
                } else {
                    referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, singleChoice);
                }
                break;
            case Constants.REFERENCE_TYPE_MULTI_CHOICE:
                long multiChoice = getMultiChoiceResult(source);
                if (multiChoice == 0) {
                    view.setSucceed(false);
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
                    return false;
                } else {
                    referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, multiChoice);
                }
                break;
            default:
                view.setSucceed(false);
                view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
                return false;
        }

        surveyAnswer.setReferenceObjectId(referObjectId);
        surveyAnswer.setComments(comments);
        return true;
    }

    public SurveyAnswerSubmitView submitIndicator(int userId, String type, int horseId,
            int assignId, int surveyAnswerId, String answer, String source, String sourceDesc, String comments,
            String prreviews, int languageId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        view.setSucceed(false);

        SurveyAnswer surveyAnswer = getSurveyAnswerById(surveyAnswerId);
        SurveyIndicator surveyIndicator = selectSurveyIndicatorByQuesitonId(surveyAnswer.getSurveyQuestionId(), languageId);
        if (surveyIndicator == null) {
            view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR, languageId));
            return view;
        }
        int answerObjectId = getSurveyAnswerObjectId(answer, surveyAnswer.getAnswerObjectId(), surveyIndicator, view, languageId);
        if (StringUtils.isNotBlank(view.getErrorMsg())) {
            return view;
        }

        surveyAnswer.setAnswerObjectId(answerObjectId);
        boolean success = fillSourceCommentsAndPrreviews(view, surveyAnswer, surveyIndicator, source, sourceDesc, comments, prreviews, languageId);
        if (!success) {
            return view;
        }
        /*
         Reference ref = referenceDao.selectReferenceById(surveyIndicator.getReferenceId());
         if (ref == null) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDREF, languageId));
         return view;
         }
         int referObjectId = surveyAnswer.getReferenceObjectId();
         switch (ref.getChoiceType()) {
         case Constants.REFERENCE_TYPE_NO_CHOICE:
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, 0);
         break;
         case Constants.REFERENCE_TYPE_SINGLE_CHOICE:
         int singleChoice = NumberUtils.toInt(source, 0);
         if (singleChoice < 0) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
         return view;
         } else {
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, singleChoice);
         }
         break;
         case Constants.REFERENCE_TYPE_MULTI_CHOICE:
         long multiChoice = getMultiChoiceResult(source);
         if (multiChoice == 0) {
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SOURCE, languageId));
         return view;
         } else {
         referObjectId = referenceObjectDao.insertReferenceObject(referObjectId, ref.getId(), sourceDesc, comments, multiChoice);
         }
         break;
         default:
         view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
         return view;
         }

         surveyAnswer.setReferenceObjectId(referObjectId);
         surveyAnswer.setComments(comments);

         */
        if (Constants.SURVEY_ACTION_OVERALLREVIEW.equalsIgnoreCase(type)) {
            surveyAnswer.setOverallReviewed(true);
        } else if (Constants.SURVEY_ACTION_CREATE.equalsIgnoreCase(type)) {
            surveyAnswer.setAnswerTime(new Date());
            surveyAnswer.setAnswerUserId(userId);
        } else if (Constants.SURVEY_ACTION_REVIEWRESPONSE.equalsIgnoreCase(type)) {
            surveyAnswer.setAuthorResponded(true);
        } else if (Constants.SURVEY_ACTION_EDIT.equalsIgnoreCase(type)) {
            surveyAnswer.setEdited(true);
        }

        surveyAnswerDao.update(surveyAnswer);

        if (StringUtils.isNotBlank(prreviews)) {
            Type t = new TypeToken<List<SurveyPeerReviewVO>>() {
            }.getType();
            List<SurveyPeerReviewVO> sprs = new Gson().fromJson(prreviews, t);

            if (sprs != null) {
                for (SurveyPeerReviewVO spr : sprs) {
                    SurveyPeerReview pspr = surveyPeerReviewDao.get(spr.getId());
                    if (pspr != null) {
                        final short opinion = spr.getOpinion();
                        pspr.setOpinion(opinion);
                        if (opinion == Constants.SURVEY_PEER_DISAGREE) {
                            if (surveyIndicator == null) {
                                view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR, languageId));
                                return view;
                            }

                            int suggestedAnswerObjectId = getSurveyAnswerObjectId(spr.getSuggestedScore(), pspr.getSuggestedAnswerObjectId(), surveyIndicator, view, languageId);
                            if (StringUtils.isNotBlank(view.getErrorMsg())) {
                                return view;
                            }

                            pspr.setSuggestedAnswerObjectId(suggestedAnswerObjectId);
                        }
                        pspr.setComments(spr.getComments());
                        surveyPeerReviewDao.update(pspr);
                    }
                }
            }
        }

        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_STARTED) {
                assignment.setStatus((short) Constants.TASK_STATUS_STARTED);
            }
            int completed = 0;
            int total = 0;

            if (Constants.SURVEY_ACTION_OVERALLREVIEW.equalsIgnoreCase(type)) {
                completed = surveyAnswerDao.selectCompletedOverallReviewedSurveyAnswerCountByHorseId(horseId);
                total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);
            } else if (Constants.SURVEY_ACTION_CREATE.equalsIgnoreCase(type)) {
                completed = surveyAnswerDao.selectCompletedSurveyAnswerCountByHorseId(horseId);
                total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);
            } else if (Constants.SURVEY_ACTION_REVIEWRESPONSE.equalsIgnoreCase(type)) {
                completed = surveyAnswerDao.getRespondedProblemCountByHorseId(horseId);
                total = surveyAnswerDao.getProblemCountByHorseId(horseId);
            } else if (Constants.SURVEY_ACTION_EDIT.equalsIgnoreCase(type)) {
                completed = surveyAnswerDao.selectCompletedEditedSurveyAnswerCountByHorseId(horseId);
                total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);
            }

            if (total != 0) {
                final float percent = new Float(completed) / new Float(total);
                assignment.setPercent(percent > 0.95f ? 0.95f : percent);
            } else {
                assignment.setPercent(0f);
            }
            taskService.updateTaskAssignment(assignment);
        }

        view.setSucceed(true);
        return view;
    }

    private SurveyAnswerSubmitView submitIndicatorPeerReview(SurveyAnswerSubmitView view, SurveyPeerReview spr, int uid, int horseId, int assignId, int answerObjectId, int opinion, String comments, int languageId) {
        if (view == null) {
            view = new SurveyAnswerSubmitView();
        }
        view.setSucceed(false);

        spr.setOpinion(Integer.valueOf(opinion).shortValue());
        spr.setComments(comments);
        spr.setLastChangeTime(new Date());

        if (opinion == Constants.SURVEY_PEER_DISAGREE) {
            spr.setSuggestedAnswerObjectId(answerObjectId);
        }

        spr.setSubmitTime(new Date());

        surveyPeerReviewDao.update(spr);

        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_STARTED) {
                assignment.setStatus((short) Constants.TASK_STATUS_STARTED);


            }
            int completed = surveyAnswerDao.getPeerReviewedAnswerCountByHorseIdAndReviewerId(horseId, uid);
            int total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);

            if (total != 0) {
                final float percent = new Float(completed) / new Float(total);
                assignment.setPercent(percent > 0.95f ? 0.95f : percent);
            } else {
                assignment.setPercent(0f);


            }
            taskService.updateTaskAssignment(assignment);
        }

        view.setSucceed(true);

        return view;
    }

    public SurveyAnswerSubmitView submitTableMainIndicatorPeerReview(SurveyAnswerSubmitView view, int uid, int horseId, int assignId, int surveyAnswerId, int opinion, String comments, int languageId) {
        SurveyPeerReview spr = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(surveyAnswerId, uid);
        return this.submitIndicatorPeerReview(view, spr, uid, horseId, assignId, -1, opinion, comments, languageId);
    }



    public SurveyAnswerSubmitView submitIndicatorPeerReview(int uid, int horseId, int assignId, int surveyAnswerId, int opinion, String comments, String suggestedScore, int languageId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        view.setSucceed(false);

        SurveyPeerReview spr = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(surveyAnswerId, uid);
        spr.setOpinion(new Integer(opinion).shortValue());
        spr.setComments(comments);
        spr.setLastChangeTime(new Date());
        spr.setSubmitTime(new Date());

        if (opinion == Constants.SURVEY_PEER_DISAGREE) {
            SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
            SurveyIndicator surveyIndicator = surveyIndicatorDao.get(surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId()).getSurveyIndicatorId());

            if (surveyIndicator == null) {
                view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOTFINDSURVEYINDICATOR, languageId));
                return view;
            }

            int answerObjectId = getSurveyAnswerObjectId(suggestedScore, spr.getSuggestedAnswerObjectId(), surveyIndicator, view, languageId);
            if (StringUtils.isNotBlank(view.getErrorMsg())) {
                return view;
            }

            spr.setSuggestedAnswerObjectId(answerObjectId);
        }

        surveyPeerReviewDao.update(spr);

        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_STARTED) {
                assignment.setStatus((short) Constants.TASK_STATUS_STARTED);


            }
            int completed = surveyAnswerDao.getPeerReviewedAnswerCountByHorseIdAndReviewerId(horseId, uid);
            int total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);

            if (total != 0) {
                final float percent = new Float(completed) / new Float(total);
                assignment.setPercent(percent > 0.95f ? 0.95f : percent);
            } else {
                assignment.setPercent(0f);


            }
            taskService.updateTaskAssignment(assignment);
        }

        view.setSucceed(true);

        return view;
    }


    public SurveyAnswerSubmitView saveSurveyAnswerPeerReviewOpinions(int horseId, int assignId, int surveyAnswerId, int uid, int opinion, String comments, String suggestedScore, int languageId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        view.setSucceed(false);

        SurveyPeerReview spr = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(surveyAnswerId, uid);
        spr.setOpinion(new Integer(opinion).shortValue());
        spr.setComments(comments);
        spr.setLastChangeTime(new Date());

        SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
        SurveyIndicator surveyIndicator = surveyIndicatorDao.get(surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId()).getSurveyIndicatorId());

        int answerObjectId = getSurveyAnswerObjectId(suggestedScore, spr.getSuggestedAnswerObjectId(), surveyIndicator, view, languageId);
        if (StringUtils.isNotBlank(view.getErrorMsg())) {
            return view;
        }

        spr.setSuggestedAnswerObjectId(answerObjectId);

        surveyPeerReviewDao.update(spr);

        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_STARTED) {
                assignment.setStatus((short) Constants.TASK_STATUS_STARTED);

                taskService.updateTaskAssignment(assignment);
            }
        }

        view.setSucceed(true);

        return view;
    }

    private int getSurveyAnswerObjectId(String suggestedScore,
            int answerObjectId, SurveyIndicator surveyIndicator,
            SurveyAnswerSubmitView view, int languageId) {
        //String errorMsg;
        switch (surveyIndicator.getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                long singleChoice = NumberUtils.toLong(suggestedScore, -1);
                if (singleChoice < 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SECION, languageId));
                }
                answerObjectId = answerObjectChoiceDao.insertAnswerObjectChoice(answerObjectId, singleChoice);
                break;
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                long multiChoice = getMultiChoiceResult(suggestedScore);
                if (multiChoice == 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM_SECION, languageId));
                }
                answerObjectId = answerObjectChoiceDao.insertAnswerObjectChoice(answerObjectId, multiChoice);
                break;
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                float f = NumberUtils.toFloat(suggestedScore, -1);
                if (f < 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_INVALID_ANSWER_VALUE, languageId));
                } else if (this.checkAnswerTypeFloat(surveyIndicator.getAnswerTypeId(), f)) {
                    answerObjectId = answerObjectFloatDao.insertAnswerObjectFloat(answerObjectId, f);
                } else {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_INVALID_ANSWER_VALUE, languageId));
                }
                break;
            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                int i = NumberUtils.toInt(suggestedScore, -1);
                if (i < 0) {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_INVALID_ANSWER_VALUE, languageId));
                } else if (this.checkAnswerTypeInteger(surveyIndicator.getAnswerTypeId(), i)) {
                    answerObjectId = answerObjectIntegerDao.insertAnswerObjectInteger(answerObjectId, i);
                } else {
                    view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_INVALID_ANSWER_VALUE, languageId));
                }

                break;
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                answerObjectId = answerObjectTextDao.insertAnswerObjectText(answerObjectId, suggestedScore);
                break;

            case Constants.SURVEY_ANSWER_TYPE_TABLE:
                answerObjectId = 1;  // indicate there is data
                break;
                
            default:
                view.setErrorMsg(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_BADPARAM, languageId));
        }
        return answerObjectId;
    }

    public int submitSurveyPeerReviewAssignment(int reviewerId, int horseId, int assignId) {
        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        if (assignment != null) {
            int completed = surveyAnswerDao.getPeerReviewedAnswerCountByHorseIdAndReviewerId(horseId, reviewerId);
            int total = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);

            if (completed == total) {
                assignment.setPercent(1f);
                assignment.setStatus((short) Constants.TASK_STATUS_DONE);
                assignment.setCompletionTime(new Date());
                taskService.updateTaskAssignment(assignment);
                return 0;
            } else {
                return total - completed;
            }
        }

        return -1;
    }

    public int submitSurveyReviewAssignment(int reviewerId, int horseId, int assignId) {
        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        TaskAssignment ta = taskService.findExistingSurveyReviewResponseAssignment(horseId);
        if (ta != null) {
            cancelProblemSurveyAnswers(horseId);
            assignmentService.cancelSurveyReviewResponseAssignment(horseId);
            siteMsgSrvc.sendContentNotice(horseId, ta.getAssignedUserId(),
                    Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_CANCELED);
        }

        if (assignment != null) {
            assignment.setPercent(1f);
            assignment.setStatus((short) Constants.TASK_STATUS_DONE);
            assignment.setCompletionTime(new Date());
            taskService.updateTaskAssignment(assignment);

            // reset staff_reviewed, reviewer_has_problem, author_responded to zero
            surveyAnswerDao.resetSurveyAnswerFlag(Constants.TASK_TYPE_SURVEY_REVIEW, horseId);
        }

        return 0;
    }

    public int submitSurveyPrReviewAssignment(int reviewerId, int horseId, int assignId) {
        final TaskAssignment assignment = taskService.getTaskAssignment(assignId);
        TaskAssignment ta = taskService.findExistingSurveyReviewResponseAssignment(horseId);
        if (ta != null) {
            cancelProblemSurveyAnswers(horseId);
            assignmentService.cancelSurveyReviewResponseAssignment(horseId);
            siteMsgSrvc.sendContentNotice(horseId, ta.getAssignedUserId(), Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_CANCELED);
        }

        if (assignment != null) {
            assignment.setPercent(1f);
            assignment.setStatus((short) Constants.TASK_STATUS_DONE);
            assignment.setCompletionTime(new Date());
            taskService.updateTaskAssignment(assignment);

            // reset staff_reviewed, reviewer_has_problem, author_responded to zero
            surveyAnswerDao.resetSurveyAnswerFlag(Constants.TASK_TYPE_SURVEY_PR_REVIEW, horseId);
        }

        return 0;
    }

    public List<SurveyPeerReviewVO> getPeerReviewDisagreementList(int horseId) {
        logger.debug("======>>> HorseID: " + horseId);
        return surveyPeerReviewDao.selectSurveyPeerReviewByOpinionAndHorseId(Constants.SURVEY_PEER_DISAGREE, horseId);
    }

    public boolean hasSubmittedPeerReviews(int surveyAnswerId) {
        List<SurveyPeerReview> surveyPeerReviewList = this.surveyPeerReviewDao.getSubmittedSurveyPeerReviewsBySurveyAnswerId(surveyAnswerId);
        if (surveyPeerReviewList != null && surveyPeerReviewList.size() > 0) {
            return true;
        }

        return false;
    }

    public void updateSumittedPeerReviews(String prJson) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<PeerReviewComment>>() {
        }.getType();
        List<PeerReviewComment> lists = gson.fromJson(prJson, listType);

        if (lists != null) {
            for (PeerReviewComment prc : lists) {
                try {
                    String strId = prc.getId();
                    int index = strId.indexOf('_');
                    if (index != -1) {
                        int id = NumberUtils.toInt(strId.substring(index + 1), -1);
                        if (id != -1) {
                            String comment = (prc.getComment() == null) ? "" : URLDecoder.decode(prc.getComment(), "UTF-8");
                            logger.debug("=====================================SuveyEdit update SurveyPeerReview get id:" + id + " comment:" + comment);
                            SurveyPeerReview spr = surveyPeerReviewDao.get(id);
                            if (spr != null) {
                                spr.setComments(comment);
                                spr.setLastChangeTime(new Date());
                                surveyPeerReviewDao.update(spr);
                            }
                        }
                    }
                } catch (UnsupportedEncodingException ex) {
                    java.util.logging.Logger.getLogger(SurveyService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<SurveyPeerReview> selectSurveyPeerReviewsByHorseId(int horseId) {
        return surveyPeerReviewDao.selectSurveyPeerReviewsByHorseId(horseId);
    }

    public List<SurveyPeerReviewBasicView> selectSurveyPeerReviewBasicViewsByUserAndAssignment(int userId, int assignmentId) {
        return surveyPeerReviewDao.getPeerReviewBasicViewsByUserAndAssignment(userId, assignmentId);
    }

    public List<ContentVersion> getAllContentVersions(int horseId) {
        return contentVersionDao.getAllContentVersionsByHorseId(horseId);
    }

    public ContentVersion getLastestContentVersionByHorseId(int horseId) {
        return contentVersionDao.getLastestContentVersionByHorseId(horseId);
    }

    public ContentVersion getContentVersion(int versionId) {
        return contentVersionDao.get(versionId);
    }

    public SurveyIndicator selectSurveyIndicatorByQuesitonId(int questionId, int languageId) {
        SurveyIndicator si = surveyIndicatorDao.selectSurveyIndicatorByQuesitonId(questionId);
        if (si == null) return null;

        if (languageId == si.getLanguageId()) return si;

        // now determine the best text for the indicator
        SurveyIndicatorIntl sii = surveyIndicatorIntlDao.findByIndicatorIdAndLanguage(si.getId(), languageId);
        if (sii != null) {
            si.setQuestion(sii.getQuestion());
            si.setTip(sii.getTip());
        }
        return si;
    }
}
