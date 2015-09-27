/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.dao.ConfigDAO;
import com.ocs.indaba.aggregation.dao.ScorecardADAO;
import com.ocs.indaba.aggregation.dao.ScorecardAnswerADAO;
import com.ocs.indaba.aggregation.dao.ScorecardAnswerBDAO;
import com.ocs.indaba.aggregation.dao.ScorecardBDAO;
import com.ocs.indaba.aggregation.po.Config;
import com.ocs.indaba.aggregation.po.Scorecard;
import com.ocs.indaba.aggregation.po.ScorecardAnswer;
import com.ocs.indaba.aggregation.vo.AnswerVO;
import com.ocs.indaba.aggregation.vo.HorseVO;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.builder.dao.ScorecardQuestionDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.SurveyCategoryDAO;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.aggregation.vo.QuestionOption;
import com.ocs.indaba.aggregation.vo.ScorecardAnswerHelper;
import com.ocs.indaba.aggregation.vo.ScorecardHelper;
import com.ocs.indaba.builder.dao.MessageDAO;
import com.ocs.indaba.dao.AttachmentDAO;
import com.ocs.indaba.dao.SurveyPeerReviewDAO;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.Message;
import com.ocs.indaba.po.ReferenceObject;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.service.ReferenceService;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jiangjeff
 */
public class ScorecardService {

    private static final Logger log = Logger.getLogger(HorseDAO.class);
    private HorseDAO horseDao = null;
    private SurveyCategoryDAO surveyCategoryDao = null;
    private ScorecardQuestionDAO scorecardQuestionDao = null;
    private SurveyPeerReviewDAO surveyPeerReviewDao = null;
    private ScorecardAnswerADAO scorecardAnswerADao = null;
    private ScorecardADAO scorecardADao = null;
    private ScorecardAnswerBDAO scorecardAnswerBDao = null;
    private ScorecardBDAO scorecardBDao = null;
    private ConfigDAO configDao = null;
    private MessageDAO msgDao = null;
    private AttachmentDAO attachmentDao = null;
    //private MessageService msgSrvc = null;
    private ReferenceService referenceSrvc = null;

    public void deleteAllScorecard() {
        Config config = configDao.getConfig();
        if (Constants.TABLE_NAME_SCORECARD_A.equals(config.getComScorecard())) {
            scorecardADao.deleteAll();
        } else {
            scorecardBDao.deleteAll();
        }
    }

    public void addScorecard(Scorecard sc) {
        Config config = configDao.getConfig();
        if (Constants.TABLE_NAME_SCORECARD_A.equals(config.getComScorecard())) {
            scorecardADao.create(ScorecardHelper.sc2ScA(sc));
        } else {
            scorecardBDao.create(ScorecardHelper.sc2ScB(sc));
        }
    }

    public void deleteAllScorecardAnswer() {
        Config config = configDao.getConfig();
        if (Constants.TABLE_NAME_SCORECARD_ANSWER_A.equals(config.getComScorecardAnswer())) {
            scorecardAnswerADao.deleteAll();
        } else {
            scorecardAnswerBDao.deleteAll();
        }
    }

    public void addScorecardAnswer(ScorecardAnswer answer) {
        Config config = configDao.getConfig();
        if (Constants.TABLE_NAME_SCORECARD_ANSWER_A.equals(config.getComScorecardAnswer())) {
            scorecardAnswerADao.create(ScorecardAnswerHelper.sca2scaA(answer));
        } else {
            scorecardAnswerBDao.create(ScorecardAnswerHelper.sca2scaB(answer));
        }
    }

    public List<HorseVO> getHorsesByProductId(int productId) {
        return scorecardQuestionDao.selectAllHorsesByProductId(productId);
    }

    public ProductInfo getProductInfo(int productId) {
        return scorecardQuestionDao.selectProductInfoByProdId(productId);
    }

    public List<SurveyCategory> getRootSurveyCategories(int productId) {
        return surveyCategoryDao.selectRootSurveyCategoriesByProductId(productId);
    }

    public List<SurveyCategory> getSubSuveryCategories(int parentId) {
        return surveyCategoryDao.selectSubSurveyCategories(parentId);
    }

    public List<SurveyCategory> getRootSurveyCategoriesByHorseId(int horseId) {
        return surveyCategoryDao.selectRootSurveyCategoriesByHorseId(horseId);
    }

    public ScorecardInfo getScorecardInfoByHorseId(int horseId) {
        return scorecardQuestionDao.selectScorecardInfoByHorseId(horseId);
    }

    public List<Horse> getAllScorecardHorses() {
        return horseDao.selectAllNotCancelledScorecardHorses();
    }

    public List<Horse> getAllTscScorecardHorses() {
        return horseDao.selectAllNotCancelledTscScorecardHorses();
    }

    public List<QuestionNode> getIndicatorsByCategoryId(int parentId) {
        return scorecardQuestionDao.selectIndicatorsByCategoryId(parentId);
    }

    public List<QuestionOption> getQuestionOptionsByIndicatorId(int indicatorId) {
        return scorecardQuestionDao.selectQuestionOptionsByIndicatorId(indicatorId);
    }

    public AnswerVO getSingleChoiceAnswerByHorseId(int indicatorId, int horseId) {
        return scorecardQuestionDao.selectSingleChoiceAnswerByHorseId(indicatorId, horseId);
    }

    public AnswerVO getMultiChoiceAnswerByHorseId(int indicatorId, int horseId) {
        return scorecardQuestionDao.selectMultiChoiceAnswerByHorseId(indicatorId, horseId);
    }

    public AnswerVO getInputAnswerByHorseId(int indicatorId, int horseId, int answerType) {
        return scorecardQuestionDao.selectInputAnswerByHorseId(indicatorId, horseId, answerType);
    }
    public String getNonChoiceQuestionCriteraByIndicatorId(int indicatorId, int answerType) {
        return scorecardQuestionDao.selectNonChoiceQuestionCriteraByIndicatorId(indicatorId, answerType);
    }

    /**
     * Get all answers 
     * @param horseId
     * @return 
     */
    public List<AnswerVO> getAnswersByHorseId(int horseId) {
        return scorecardQuestionDao.selectAnswersByHorseId(horseId);
    }

    public int getUniquePeerReviewerCountByHorseId(int horseId) {
        return scorecardQuestionDao.selectUniquePeerReviewerCountByHorseId(horseId);
    }

    public int getPeerReviewedCountByHorseId(int horseId) {
        return scorecardQuestionDao.selectPeerReviewedCountByHorseId(horseId);
    }

    public int getQuestionCountByHorseId(int horseId) {
        return scorecardQuestionDao.selectQuestionCountByHorseId(horseId);
    }

    /**
     * Get survey peer review list by answer id.
     * 
     * @param answerId
     * @return 
     */
    public List<SurveyPeerReview> getSurveyPeerReviewsByAnswerId(int answerId) {
        return surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerId(answerId);
    }

    public List<Message> getMessagesByMsgBoardId(int msgBoardId) {
        return msgDao.selectMessagesByMsgBoardId(msgBoardId);
    }


    public Map<Integer, ReferenceObject> getReferenceObjectsByProductId(int productId) {
        Map<Integer, ReferenceObject> refMap = new HashMap<Integer, ReferenceObject>();
        List<ReferenceObject> refList = referenceSrvc.getReferenceObjectsByProductId(productId);
        if (refList != null && !refList.isEmpty()) {
            for (ReferenceObject refObj : refList) {
                if (!StringUtils.isEmpty(refObj.getSourceDescription())) {
                    refMap.put(refObj.getId(), refObj);
                }
            }
            refList.clear();
        }
        return refMap;
    }


     public Map<Integer, List<SurveyPeerReview>> getSurveyPeerReviewsByProductId(int productId) {
        Map<Integer, List<SurveyPeerReview>> prMap = new HashMap<Integer, List<SurveyPeerReview>>();
        List<SurveyPeerReview> prList = surveyPeerReviewDao.selectSurveyPeerReviewsByProductId(productId);
        if (prList != null && !prList.isEmpty()) {
            for (SurveyPeerReview pr : prList) {               
                List<SurveyPeerReview> list = prMap.get(pr.getSurveyAnswerId());
                if (list == null) {
                    list = new ArrayList<SurveyPeerReview>();
                    prMap.put(pr.getSurveyAnswerId(), list);
                }
                list.add(pr);
            }
            prList.clear();
        }
        return prMap;
    }



     private void addMessages(Map<Integer, List<Message>> toMsgMap, List<Message> msgList) {
         if (msgList != null && !msgList.isEmpty()) {
            for (Message msg : msgList) {
                if (StringUtils.isEmpty(msg.getBody())) {
                    continue;
                }
                List<Message> list = toMsgMap.get(msg.getMsgboardId());
                if (list == null) {
                    list = new ArrayList<Message>();
                    toMsgMap.put(msg.getMsgboardId(), list);
                }
                list.add(msg);
            }
        }
     }


    public void getInternalMessagesByProductId(int productId, Map<Integer, List<Message>> msgMap) {
        List<Message> msgList = msgDao.selectInternalMessagesByProductId(productId);
        addMessages(msgMap, msgList);
    }

    public void getStaffAuthorMessagesByProductId(int productId, Map<Integer, List<Message>> msgMap) {
        List<Message> msgList = msgDao.selectStaffAuthorMessagesByProductId(productId);
        addMessages(msgMap, msgList);
    }

    public void getPeerReviewMessagesByProductId(int productId, Map<Integer, List<Message>> msgMap) {
        List<Message> msgList = msgDao.selectPeerReviewMessagesByProductId(productId);
        addMessages(msgMap, msgList);
    }


    public List<Attachment> getSurveyAnswerAttachmentsByAnswerId(int answerId) {
        return attachmentDao.selectSurveyAnswerAttachmentsByAnswerId(answerId);
    }

    @Autowired
    public void setHorseDao(HorseDAO horseDao) {
        this.horseDao = horseDao;
    }

    @Autowired
    public void setSurveyCategoryDao(SurveyCategoryDAO surveyCategoryDao) {
        this.surveyCategoryDao = surveyCategoryDao;

    }

    @Autowired
    public void setScorecardQuestionDao(ScorecardQuestionDAO scorecardQuestionDao) {
        this.scorecardQuestionDao = scorecardQuestionDao;
    }

    @Autowired
    public void setSurveyPeerReviewDao(SurveyPeerReviewDAO SurveyPeerReviewDao) {
        this.surveyPeerReviewDao = SurveyPeerReviewDao;
    }

    @Autowired
    public void setScorecardAnswerADAO(ScorecardAnswerADAO scorecardAnswerDao) {
        this.scorecardAnswerADao = scorecardAnswerDao;
    }

    @Autowired
    public void setScorecardADAO(ScorecardADAO scorecardDao) {
        this.scorecardADao = scorecardDao;
    }

    @Autowired
    public void setScorecardAnswerBDAO(ScorecardAnswerBDAO scorecardAnswerDao) {
        this.scorecardAnswerBDao = scorecardAnswerDao;
    }

    @Autowired
    public void setScorecardBDAO(ScorecardBDAO scorecardDao) {
        this.scorecardBDao = scorecardDao;
    }

    @Autowired
    public void setConfigDAO(ConfigDAO configDao) {
        this.configDao = configDao;
    }

    @Autowired
    public void setMessageDAO(MessageDAO msgDao) {
        this.msgDao = msgDao;
    }

    @Autowired
    public void setReferenceServcie(ReferenceService referenceSrvc) {
        this.referenceSrvc = referenceSrvc;
    }

    @Autowired
    public void setAttachmentDAO(AttachmentDAO attachmentDao) {
        this.attachmentDao = attachmentDao;
    }
}
