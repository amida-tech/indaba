/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.po.Scorecard;
import com.ocs.indaba.aggregation.po.ScorecardAnswer;
import com.ocs.indaba.aggregation.service.ScorecardService;
import com.ocs.indaba.aggregation.vo.*;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.util.StandardAggregation;
import com.ocs.indaba.util.Tree;
import java.math.BigDecimal;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class SRFBuilder extends DataBuilder {
    private ScorecardService scorecardSrvc = null;

    public SRFBuilder() {
        log = Logger.getLogger(SRFBuilder.class);
    }

    @Autowired
    public void setScorecardSrvc(ScorecardService scorecardSrvc) {
        this.scorecardSrvc = scorecardSrvc;
    }
    
    @Override
    public void clean() {
    }

    @Override
    public void build(String baseDir, String cacheDir) {
        //log.info("Loading scorecard data from Builder database ...");
        List<Horse> horses = scorecardSrvc.getAllTscScorecardHorses();
        if (horses == null || horses.isEmpty()) {
            return;
        }

        writeLogLn("Clear all scorecard data in Publisher DB");
        log.info("Clear all scorecard data in Publisher DB");
        scorecardSrvc.deleteAllScorecard();

        writeLogLn("Clear all scorecard_data data in Publisher DB");
        log.info("Clear all scorecard_data data in Publisher DB");
        scorecardSrvc.deleteAllScorecardAnswer();

        for (Horse horse : horses) {
            /*
             * if (horse.getProductId() != 1) { continue; }
             */
            writeLogLn("==============================================");
            writeLogLn("Load scorecard of horse [" + horse.getId() + "].");

            // Cache all answers of the horse
            List<AnswerVO> answers = scorecardSrvc.getAnswersByHorseId(horse.getId());
            Map<Integer, AnswerVO> cachedAnswerMap = new HashMap<Integer, AnswerVO>();
            if (answers != null && !answers.isEmpty()) {
                for (AnswerVO answer : answers) {
                    cachedAnswerMap.put(answer.getIndicatorId(), answer);
                }
            }

            ScorecardInfo scorecardInfo = buildScorecardTree(horse, cachedAnswerMap);

            writeLogLn("Computing scorecard standard aggreation data of horse [" + horse.getId() + "].");
            //StandardAggregation standardAggr = new StandardAggregation(ScorecardInfo scorecard);
            StandardAggregation.compute(scorecardInfo);

            writeLogLn("Generating SRF for horse [" + horse.getId() + "]...");
            SRFPersistence persistence = new SRFPersistence(baseDir);
            persistence.serializeSRF(scorecardInfo);
            writeLogLn("SRF for horse [" + horse.getId() + "] is generated: " + BasePersistence.getSRFFilepath(horse.getProductId(), horse.getId()));

            this.saveScorecardToPublisherDB(scorecardInfo);

            List<QuestionNode> questions = new ArrayList<QuestionNode>();
            ScorecardFinder.listAllQuestions(questions, scorecardInfo.getRootCategories());
            this.saveAnswersToPublisherDB(scorecardInfo.getId(), questions);

            cachedAnswerMap.clear();
            cachedAnswerMap = null;
            scorecardInfo = null;

            //break;
            //scorecard = persistence.deserializeSRF(new File("/data/indaba/aggregation/products", "PRD1/HRS1.srf"));
            //JSONObject jsonObj = SRFJsonUtils.scorecard2Json(scorecard);
            //System.out.println(jsonObj.toJSONString());
            //break;
        }

    }

    private ScorecardInfo buildScorecardTree(Horse horse, Map<Integer, AnswerVO> cachedAnswerMap) {
        List<SurveyCategory> surveyCategories = scorecardSrvc.getRootSurveyCategoriesByHorseId(horse.getId());
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            return null;
        }

        ScorecardInfo scorecard = scorecardSrvc.getScorecardInfoByHorseId(horse.getId());
        for (SurveyCategory sc : surveyCategories) {
            Tree<ScorecardBaseNode> rootCategory = new Tree<ScorecardBaseNode>(new CategoryNode(sc.getId(), sc.getName(), sc.getTitle(), sc.getLabel(), sc.getWeight()));

            buildSubCategories(horse.getId(), rootCategory, sc.getId(), cachedAnswerMap);
            scorecard.addRootCategory(rootCategory);
        }
        // Compute MOE
        //int indicatorCount = scorecardSrvc.getQuestionCountByHorseId(horse.getId());
        //int peerReviewCount = scorecardSrvc.getPeerReviewedCountByHorseId(horse.getId());
        //int peerReviewerCount = scorecardSrvc.getUniquePeerReviewerCountByHorseId(horse.getId());
        //scorecard.setMoe(computeMoe(indicatorCount, peerReviewCount, peerReviewerCount));
        scorecard.setMoe(0.0d);
        return scorecard;
    }

    private void buildSubCategories(int horseId, Tree<ScorecardBaseNode> parent, int parentId, Map<Integer, AnswerVO> cachedAnswerMap) {
        List<SurveyCategory> surveyCategories = scorecardSrvc.getSubSuveryCategories(parentId);
        if (surveyCategories == null || surveyCategories.isEmpty()) {// LEAF: indicators
            List<QuestionNode> indicators = scorecardSrvc.getIndicatorsByCategoryId(parentId);
            if (indicators != null && !indicators.isEmpty()) {
                for (QuestionNode indicator : indicators) {//List<QuestionOption> options = indicatorDao.selectQuestionOptionsByIndicatorId(indicator.getId());
                    AnswerVO answer = null;
                    switch (indicator.getQuestionType()) {
                        case Constants.ANSWER_TYPE_SINGLE: {
                            indicator.setOptions(scorecardSrvc.getQuestionOptionsByIndicatorId(indicator.getId()));
                            answer = scorecardSrvc.getSingleChoiceAnswerByHorseId(indicator.getId(), horseId);
                        }
                        break;
                        case Constants.ANSWER_TYPE_MULTI: {
                            indicator.setOptions(scorecardSrvc.getQuestionOptionsByIndicatorId(indicator.getId()));
                            answer = scorecardSrvc.getMultiChoiceAnswerByHorseId(indicator.getId(), horseId);
                        }
                        break;
                        case Constants.ANSWER_TYPE_INTEGER:
                        case Constants.ANSWER_TYPE_FLOAT:
                        case Constants.ANSWER_TYPE_TEXT: {
                            answer = scorecardSrvc.getInputAnswerByHorseId(indicator.getId(), horseId, indicator.getQuestionType());
                        }
                        break;
                    }
                    //indicator.setOptions(scorecardSrvc.getQuestionOptionsByIndicatorId(indicator.getId()));
                    //AnswerVO answer = cachedAnswerMap.get(indicator.getId());
                    if (horseId == 1 && answer == null) { // Why does it ignore horse_id=1?
                        log.debug("Answer is Empty for Indicator: " + indicator.getId());
                    }
                    if (answer != null) {
                        indicator.setCompleted(true);
                        indicator.setChoiceId(answer.getChoiceId());
                        indicator.setChoices(answer.getChoices());
                        indicator.setInputValue(answer.getInputValue());
                        indicator.setCriteria(answer.getCriteria());
                        indicator.setTip(answer.getTip());
                        indicator.setReferenceId(answer.getReferenceId());
                        indicator.setReferenceName(answer.getReferenceName());
                        indicator.setComments(answer.getComments());
                        indicator.setAnswerUserId(answer.getAnswerUserId());
                        indicator.setReferenceObjectId(answer.getReferenceObjectId());
                        indicator.setReferences(answer.getReferences());
                        //indicator.setReviews(answer.getReviews());
                        indicator.setScore(answer.getScore());
                        indicator.setUseScore(answer.hasUseScore());
                        indicator.setLabel(answer.getLabel());
                        indicator.setAnswerId(answer.getId());
                        indicator.setInternalMsgboardId(answer.getInternalMsgboardId());
                        indicator.setStaffAuthorMsgboardId(answer.getStaffAuthorMsgboardId());
                        List<SurveyPeerReview> peerReviews = scorecardSrvc.getSurveyPeerReviewsByAnswerId(answer.getId());
                        if (peerReviews != null && !peerReviews.isEmpty()) {
                            for (SurveyPeerReview rev : peerReviews) {
                                String comment = rev.getComments();
                                if (comment != null) {
                                    indicator.addReview(comment);
                                }
                            }
                        }
                        List<Attachment> attachements = scorecardSrvc.getSurveyAnswerAttachmentsByAnswerId(answer.getId());
                        indicator.setAttachements(attachements);
                        //if (horseId == 1) {
                        //log.debug(">>> " + indicator.getId() + "::: " + indicator);
                        //}
                    }
                    parent.addChild(new Tree<ScorecardBaseNode>(indicator, parent));
                }
            }
        } else { // categories
            for (SurveyCategory cat : surveyCategories) {
                Tree<ScorecardBaseNode> child = new Tree<ScorecardBaseNode>();
                child.setParent(parent);
                //log.debug("CAT_ID: " + cat.getId() + ", NAME: " + cat.getName() + ", TITLE: " + cat.getTitle());
                child.setNode(new CategoryNode(cat.getId(), cat.getName(), cat.getTitle(), cat.getLabel(), cat.getWeight()));
                parent.addChild(child);
                buildSubCategories(horseId, child, cat.getId(), cachedAnswerMap);
            }
        }
    }

    private double computeMoe(int indicatorCount, int peerReviewCount, int apr) {
        if (indicatorCount == 0) {
            indicatorCount = 1;
        }
        if (apr == 0) {
            apr = 1;
        }

        return (((float) (peerReviewCount)) / ((float) indicatorCount)) * (((float) Constants.MOE_MPR) / apr) * 100;
    }

    public void saveScorecardToPublisherDB(ScorecardInfo scorecardInfo) {
        Scorecard sc = new Scorecard();
        sc.setScorecardId(scorecardInfo.getId());
        sc.setOrgId(scorecardInfo.getOrgId());
        sc.setProductId(scorecardInfo.getProductId());
        sc.setProjectId(scorecardInfo.getProjectId());
        sc.setStudyPeriodId(scorecardInfo.getStudyPeriodId());
        sc.setSurveyConfigId(scorecardInfo.getSurveyConfigId());
        sc.setContentHeaderId(scorecardInfo.getContentHeaderId());
        sc.setTargetId(scorecardInfo.getTargetId());
        sc.setOrgId(scorecardInfo.getOrgId());
        sc.setStatus(Integer.valueOf(scorecardInfo.getStatus()).shortValue());
        sc.setLastUpdateTime(new Date());
        scorecardSrvc.addScorecard(sc);
    }

    public void saveAnswersToPublisherDB(int scorecardId, List<QuestionNode> questionNodes) {
        if (questionNodes == null || questionNodes.isEmpty()) {
            return;
        }

        for (QuestionNode node : questionNodes) {
            if (!node.hasUseScore()) {
                //log.debug("Ignore answer: [id=" + node.getQuestionId() + "] [use_score=" + node.hasUseScore() + "] [scorecardId=" + scorecardId + "].");
                continue;
            }
            //log.debug("Save question: [id=" + node.getQuestionId() + "] [scorecardId=" + scorecardId + "].");
            ScorecardAnswer answer = new ScorecardAnswer();
            answer.setQuestionId(node.getQuestionId());
            answer.setDataType(new Integer(node.getQuestionType()).shortValue());
            answer.setIndicatorId(node.getId());
            answer.setScore(BigDecimal.valueOf(node.getScore()));
            //answer.setValue(node.getPublicName());
            answer.setValue(node.getLabel());
            answer.setScorecardId(scorecardId);
            scorecardSrvc.addScorecardAnswer(answer);
        }
    }
}
