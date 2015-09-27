/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AnswerObjectText;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sjf
 */
public class AnswerObjectTextDAO extends SmartDaoMySqlImpl<AnswerObjectText, Integer> {

    private static final Logger logger = Logger.getLogger(AnswerObjectIntegerDAO.class);
    private static final String GET_AnswerObjectText_BY_ID = "select * from answer_object_text where id = ?";

    public AnswerObjectText getAnswerObjectTextbyId(int answerObjectId) {
        return this.findSingle(GET_AnswerObjectText_BY_ID, answerObjectId);
    }

    public int insertAnswerObjectText(int answerObjectId, String value){
         if(answerObjectId > 0){
             this.updateAnswerObjectText(answerObjectId, value);
             return answerObjectId;
         }
         AnswerObjectText answer = new AnswerObjectText();
         answer.setValue(value);
         AnswerObjectText result = super.save(answer);
         return result.getId();
     }

     public void updateAnswerObjectText(int answerObjectId, String value){
         AnswerObjectText answer = new AnswerObjectText();
         answer.setValue(value);
         answer.setId(answerObjectId);
         super.update(answer);
     }

     private static final String SELECT_OBJECTS_BY_PRODUCT_ID =
             "SELECT DISTINCT aot.* " +
             "FROM answer_object_text aot, survey_answer sa, survey_content_object sco, content_header ch, horse h, survey_indicator si, survey_question sq " +
             "WHERE sco.content_header_id = ch.id AND sa.survey_content_object_id = sco.id AND h.id = ch.horse_id " +
             "AND h.product_id=? AND aot.id = sa.answer_object_id AND sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id " +
             "AND si.answer_type=5";

     public List<AnswerObjectText> getAnswerObjectsOfProduct(int productId) {
         return super.find(SELECT_OBJECTS_BY_PRODUCT_ID, productId);
     }

     private static final String SELECT_OBJECTS_OF_SPR_BY_PRODUCT_ID =
             "SELECT DISTINCT aot.* " +
             "FROM answer_object_text aot, survey_answer sa, survey_content_object sco, content_header ch, horse h, survey_indicator si, survey_question sq, survey_peer_review spr " +
             "WHERE sco.content_header_id = ch.id AND sa.survey_content_object_id = sco.id AND h.id = ch.horse_id " +
             "AND h.product_id=? AND aot.id = spr.suggested_answer_object_id AND sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id " +
             "AND si.answer_type=5 AND spr.survey_answer_id=sa.id AND spr.opinion=2";

     public List<AnswerObjectText> getAnswerObjectsOfProductSPR(int productId) {
         return super.find(SELECT_OBJECTS_OF_SPR_BY_PRODUCT_ID, productId);
     }

     private static final String SELECT_COMPONENT_OBJECT =
             "SELECT aot.* " +
             "FROM answer_object_text aot, survey_content_object sco, horse h, survey_answer sa, survey_answer_component sac " +
             "WHERE h.id=? AND sco.content_header_id=h.content_header_id " +
             "AND sa.survey_content_object_id=sco.id AND sa.survey_question_id=? " +
             "AND sac.survey_answer_id=sa.id AND sac.component_indicator_id=? " +
             "AND aot.id=sac.answer_object_id";

     public AnswerObjectText getComponentAnswerObject(int horseId, int mainQuestionId, int componentIndicatorId) {
        return super.findSingle(SELECT_COMPONENT_OBJECT, horseId, mainQuestionId, componentIndicatorId);
     }

     private static final String SELECT_COMPONENT_VERSION_OBJECT =
             "SELECT aot.* " +
             "FROM answer_object_text aot, survey_answer_version sav, survey_answer_component_version sacv " +
             "WHERE sav.content_version_id=? AND sav.survey_question_id=? " +
             "AND sacv.survey_answer_version_id=sav.id AND sacv.component_indicator_id=? " +
             "AND aot.id=sacv.answer_object_id";

     public AnswerObjectText getComponentVersionAnswerObject(int contentVersionId, int mainQuestionId, int componentIndicatorId) {
         return super.findSingle(SELECT_COMPONENT_VERSION_OBJECT, contentVersionId, mainQuestionId, componentIndicatorId);
     }

     private static final String SELECT_SPR_COMPONENT_OBJECT =
             "SELECT aot.* " +
             "FROM answer_object_text aot, spr_component sprc " +
             "WHERE sprc.survey_peer_review_id=? AND sprc.component_indicator_id=? " +
             "AND aot.id=sprc.answer_object_id";

     public AnswerObjectText getPeerReviewComponentAnswerObject(int surveyPeerReviewId, int componentIndicatorId) {
         return super.findSingle(SELECT_SPR_COMPONENT_OBJECT, surveyPeerReviewId, componentIndicatorId);
     }

     private static final String SELECT_SPR_VERSION_COMPONENT_OBJECT =
             "SELECT aot.* " +
             "FROM answer_object_text aot, spr_component_version sprcv " +
             "WHERE sprcv.survey_peer_review_version_id=? AND sprcv.component_indicator_id=? " +
             "AND aot.id=sprcv.answer_object_id";

     public AnswerObjectText getPeerReviewVersionComponentAnswerObject(int surveyPeerReviewVersionId, int componentIndicatorId) {
         return super.findSingle(SELECT_SPR_VERSION_COMPONENT_OBJECT, surveyPeerReviewVersionId, componentIndicatorId);
     }

}
