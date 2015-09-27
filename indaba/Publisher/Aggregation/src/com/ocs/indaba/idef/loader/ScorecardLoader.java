/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.loader;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Answer;
import com.ocs.indaba.idef.xo.Review;
import com.ocs.indaba.idef.xo.Scorecard;
import com.ocs.indaba.idef.xo.SurveyAnswer;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.ProjectTarget;
import com.ocs.indaba.po.SurveyContentObject;
import com.ocs.indaba.po.WorkflowObject;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ScorecardLoader extends Loader {

    

    public ScorecardLoader(ProcessContext ctx) {
        super.createLoader(ctx);
    }

    public void load() {
        List<Scorecard> list = ctx.getScorecards();

        if (list == null || list.isEmpty()) {
            ctx.addError("No scorecards to load!");
            return;
        }

        for (Scorecard c : list) {
            loadScorecard(c);
        }
    }


    private void loadScorecard(Scorecard xo) {
        WorkflowObject wfo = new WorkflowObject();
        wfo.setWorkflowId(1);
        wfo.setIsCancelled(false);
        wfo.setStartTime(xo.getSubmitTime());
        wfo.setStatus(Constants.WORKFLOW_OBJECT_STATUS_DONE);
        wfoDao.create(wfo);

        Horse horse = new Horse();
        horse.setCompletionTime(xo.getSubmitTime());
        horse.setProductId(xo.getProduct().getDboId());
        horse.setStartTime(xo.getSubmitTime());
        horse.setTargetId(xo.getTarget().getId());
        horse.setWorkflowObjectId(wfo.getId());
        horseDao.create(horse);

        ProjectTarget pt = new ProjectTarget();
        pt.setProjectId(xo.getProduct().getProject().getDboId());
        pt.setTargetId(xo.getTarget().getId());
        projTargetDao.create(pt);

        SurveyContentObject sco = new SurveyContentObject();
        sco.setSurveyConfigId(xo.getProduct().getSurveyConfig().getDboId());
        sco.setContentHeaderId(0);
        scoDao.create(sco);

        ContentHeader ch = new ContentHeader();
        ch.setAuthorUserId(xo.getUser().getDboId());
        ch.setContentType(xo.getProduct().getContentType());
        ch.setCreateTime(xo.getSubmitTime());
        ch.setHorseId(horse.getId());
        ch.setProjectId(xo.getProduct().getProject().getDboId());
        ch.setStatus((short)Constants.CONTENT_HEADER_STATUS_DONE);
        ch.setSubmitTime(xo.getSubmitTime());
        ch.setTitle(xo.getTarget().getName() + " - " + xo.getProduct().getName());
        ch.setContentObjectId(sco.getId());
        chDao.create(ch);

        sco.setContentHeaderId(ch.getId());
        scoDao.update(sco);

        horse.setContentHeaderId(ch.getId());
        horseDao.update(horse);

        xo.setDboContentHeaderId(ch.getId());
        xo.setDboHorseId(horse.getId());
        xo.setDboSurveyContentObjectId(sco.getId());

        // now load all answers
        List<SurveyAnswer> answers = xo.getSurveyAnswers();
        
        if (answers != null) {
            for (SurveyAnswer sa : answers) {
                loadAnswer(sa, xo);
            }
        }
    }
    

    private int createAnswerObject(Answer answer) {
        int answerObjectId = 0;

        switch (answer.getType()) {
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                // create an Answer object
                com.ocs.indaba.po.AnswerObjectChoice aoc = new com.ocs.indaba.po.AnswerObjectChoice();
                aoc.setChoices(answer.getChoiceValue());
                aocDao.create(aoc);
                answerObjectId = aoc.getId();
                break;

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                com.ocs.indaba.po.AnswerObjectInteger aoi = new com.ocs.indaba.po.AnswerObjectInteger();
                aoi.setValue(answer.getIntValue());
                aoiDao.create(aoi);
                answerObjectId = aoi.getId();
                break;

            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                com.ocs.indaba.po.AnswerObjectFloat aof = new com.ocs.indaba.po.AnswerObjectFloat();
                aof.setValue((float)answer.getFloatValue());
                aofDao.create(aof);
                answerObjectId = aof.getId();
                break;

            case Constants.SURVEY_ANSWER_TYPE_TEXT:
            default:
                com.ocs.indaba.po.AnswerObjectText aot = new com.ocs.indaba.po.AnswerObjectText();
                aot.setValue(answer.getTextValue());
                aotDao.create(aot);
                answerObjectId = aot.getId();
                break;
        }

        return answerObjectId;
    }


    private void loadAnswer(SurveyAnswer sa, Scorecard scorecard) {
        int answerObjectId = createAnswerObject(sa.getAnswer());

        // create reference object
        com.ocs.indaba.po.ReferenceObject refObj = new com.ocs.indaba.po.ReferenceObject();
        refObj.setChoices(sa.getRefChoices());
        refObj.setComments(sa.getComments());
        refObj.setReferenceId(sa.getQuestion().getIndicator().getRef().getDboId());
        refObj.setSourceDescription(sa.getRefDesc());
        refObjDao.create(refObj);

        com.ocs.indaba.po.SurveyAnswer saPo = new com.ocs.indaba.po.SurveyAnswer();
        saPo.setAnswerObjectId(answerObjectId);
        saPo.setAnswerTime(scorecard.getSubmitTime());
        saPo.setAnswerUserId(scorecard.getUser() == null ? 0 : scorecard.getUser().getDboId());
        saPo.setComments(sa.getComments());
        saPo.setSurveyContentObjectId(scorecard.getDboSurveyContentObjectId());
        saPo.setSurveyQuestionId(sa.getQuestion().getDboId());
        saPo.setReferenceObjectId(refObj.getId());
        saDao.create(saPo);

        // load peer reviews if any
        List<Review> reviews = sa.getReviews();
        if (reviews == null || reviews.isEmpty()) return;

        for (Review review : reviews) {
            loadOneReview(review, saPo, scorecard);
        }

    }


    private void loadOneReview(Review review, com.ocs.indaba.po.SurveyAnswer sa, Scorecard scorecard) {
        int answerObjectId = 0;

        if (review.getOpinion() == com.ocs.indaba.idef.common.Constants.PEER_REVIEW_OPINION_DISAGREE) {
            // need to create a new answer object
            answerObjectId = createAnswerObject(review.getAnswer());
        }

        com.ocs.indaba.po.SurveyPeerReview spr = new com.ocs.indaba.po.SurveyPeerReview();
        spr.setComments(review.getComments());
        spr.setOpinion(review.getOpinion());
        spr.setReviewerUserId((review.getReviewer() == null) ? 0 : review.getReviewer().getDboId());
        spr.setSuggestedAnswerObjectId(answerObjectId);
        spr.setSurveyAnswerId(sa.getId());
        spr.setLastChangeTime(scorecard.getSubmitTime());
        spr.setSubmitTime(scorecard.getSubmitTime());

        sprDao.create(spr);
    }

}
