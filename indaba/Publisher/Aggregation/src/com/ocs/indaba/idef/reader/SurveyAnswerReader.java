/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.xo.Answer;
import com.ocs.indaba.idef.xo.AnswerChoice;
import com.ocs.indaba.idef.xo.Indicator;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Product;
import com.ocs.indaba.idef.xo.Ref;
import com.ocs.indaba.idef.xo.RefChoice;
import com.ocs.indaba.idef.xo.Review;
import com.ocs.indaba.idef.xo.Scorecard;
import com.ocs.indaba.idef.xo.SurveyAnswer;
import com.ocs.indaba.idef.xo.SurveyConfig;
import com.ocs.indaba.idef.xo.SurveyQuestion;
import com.ocs.indaba.idef.xo.User;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author yc06x
 */
public class SurveyAnswerReader extends Reader {

    private static final Logger log = Logger.getLogger(SurveyAnswerReader.class);

    static private final int COL_SCORECARD_ID = 0;
    static private final int COL_QUESTION_ID = 1;
    static private final int COL_ANSWER = 2;
    static private final int COL_COMMENTS = 3;
    static private final int COL_REF_CHOICES = 4;
    static private final int COL_REF_DESC = 5;
    static private final int COL_REVIEWER_UID = 6;
    static private final int COL_REVIEW_OPINION = 7;
    static private final int COL_REVIEW_ANSWER = 8;
    static private final int COL_REVIEW_COMMENTS = 9;


    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "SCORECARD ID",
        "QUESTION ID",
        "ANSWER",
        "comments",
        "reference choices",
        "reference description",
        "peer reviewer uid",
        "peer review opinion",
        "peer review answer",
        "peer review comments"
    };

    public SurveyAnswerReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;
        int saCount = 0;

        try {
        // process projects
        while ((line = readNext()) != null) {

            //log.error("Processing line " + super.getLineNumber());
            
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String scorecardId = super.getColumn(line, COL_SCORECARD_ID);
            String qstId = super.getColumn(line, COL_QUESTION_ID);
            String answerStr = super.getColumn(line, COL_ANSWER);
            String comments = super.getColumn(line, COL_COMMENTS);
            String refChoiceStr = super.getColumn(line, COL_REF_CHOICES);
            String refDesc = super.getColumn(line, COL_REF_DESC);
            String reviewerUid = super.getColumn(line, COL_REVIEWER_UID);
            String reviewOpinionStr = super.getColumn(line, COL_REVIEW_OPINION);
            String reviewAnswerStr = super.getColumn(line, COL_REVIEW_ANSWER);
            String reviewComments = super.getColumn(line, COL_REVIEW_COMMENTS);

            int numErrs = 0;

            Scorecard card = ctx.getScorecard(scorecardId);
            if (card == null) {
                super.addLineError("referenced scorecard does not exist: " + scorecardId);
                numErrs++;
            }

            SurveyQuestion qst = ctx.getQuestion(qstId);
            if (qst == null) {
                super.addLineError("referenced question does not exist: " + qstId);
                numErrs++;
            }

            if (ctx.getCardQstAnswer(scorecardId, qstId) != null) {
                super.addLineError("duplicate answer for scorecard " + scorecardId + " and question " + qstId);
                numErrs++;
            }

            if (card != null && qst != null) {
                // make sure the answer belongs to the right suvey question
                Product prod = card.getProduct();
                SurveyConfig prodSc = prod.getSurveyConfig();
                SurveyConfig qstSc = qst.getSurveyConfig();
                if (qstSc != prodSc) {
                    super.addLineError("question " + qstId + " doesn't belong to product " + prod.getId());
                    numErrs++;
                }
            }

            int refChoiceMask = 0;
            Answer answer = null;
            List<Review> reviews = null;

            if (qst != null) {
                Indicator indicator = qst.getIndicator();

                // process references
                Ref ref = indicator.getRef();
                switch (ref.getType()) {
                    case Constants.REF_TYPE_SINGLE_CHOICE:
                        if (StringUtils.isEmpty(refChoiceStr)) {
                            super.addLineError("missing reference choice.");
                            numErrs++;
                        } else {
                            RefChoice rc = findChoice(refChoiceStr, ref);
                            if (rc == null) {
                                super.addLineError("invalid ref choice: " + refChoiceStr);
                                numErrs++;
                            } else {
                                refChoiceMask = rc.getMask();
                            }
                        }
                        break;

                    case Constants.REF_TYPE_MULTI_CHOICE:
                        String[] choiceStrs = refChoiceStr.split(",");
                        for (String str : choiceStrs) {
                            RefChoice rc = findChoice(str, ref);
                            if (rc == null) {
                                super.addLineError("invalid ref choice: " + refChoiceStr);
                                numErrs++;
                            } else {
                                refChoiceMask |= rc.getMask();
                            }
                        }
                        break;

                    default:
                        break;
                }


                // try to parse answer
                answer = parseAnswer("author answer",answerStr, indicator);
                if (answer == null) numErrs++;

                // check for peer reviews
                reviews = new ArrayList<Review>();

                if (!StringUtils.isEmpty(reviewerUid) || !StringUtils.isEmpty(reviewOpinionStr) || !StringUtils.isEmpty(reviewAnswerStr) || !StringUtils.isEmpty(reviewComments)) {
                    Review review = parseReview(line, indicator);
                    if (review == null) numErrs++;
                    else reviews.add(review);
                }

                // read additional reviews if any
                while ((line = readNext()) != null) {
                    if (StringUtils.isEmpty(super.getColumn(line, 0))) {
                        Review review = parseReview(line, indicator);
                        if (review == null) numErrs++;
                        else reviews.add(review);
                    } else {
                        super.returnLine(line);
                        break;
                    }
                }
            }

            if (numErrs > 0) continue;

            // create object
            SurveyAnswer sa = new SurveyAnswer();

            sa.setAnswer(answer);
            sa.setComments(comments);
            sa.setQuestion(qst);
            sa.setRefChoices(refChoiceMask);
            sa.setRefDesc(refDesc);
            sa.setReviews(reviews);
            sa.setScorecard(card);
            card.addSurveyAnswer(sa);

            ctx.addCardQstAnswer(scorecardId, qstId, sa);
            saCount ++;
        }

        if (super.getErrorCount() == 0 && saCount == 0) {
            super.addError("No survey answers specified.");            
        }
        } catch (Exception ex) {
            super.addError("Bad");
        }
    }

    
    private RefChoice findChoice(String sname, Ref ref) {
        for (RefChoice rc : ref.getChoices()) {
            if (sname.equalsIgnoreCase(rc.getSname())) {
                return rc;
            }
        }

        return null;
    }

    private Answer parseAnswer(String type, String answerStr, Indicator indicator) {
        List<AnswerChoice> choices = indicator.getChoices();
        Answer answer = null;

        switch (indicator.getType()) {
            case Constants.INDICATOR_TYPE_SINGLE_CHOICE:
                int pos = getChoicePos(type, answerStr, choices.size());
                if (pos < 0) return null;
                AnswerChoice ac = choices.get(pos);
                answer = new Answer();
                answer.setSingleChoiceValue(ac.getMask());
                break;

            case Constants.INDICATOR_TYPE_MULTI_CHOICE:
                String[] choiceStr = answerStr.split(",");
                int mask = 0;
                for (String str : choiceStr) {
                    pos = getChoicePos(type, str, choices.size());
                    if (pos >= 0) {
                        mask |= choices.get(pos).getMask();
                    }
                }
                answer = new Answer();
                answer.setMultiChoiceValue(mask);
                break;

            case Constants.INDICATOR_TYPE_INTEGER:
                int valInt = StringUtils.str2int(answerStr);
                if (valInt == Integer.MIN_VALUE) {
                    super.addLineError("invalid integer value: " + answerStr);
                    return null;
                }
                
                int mini = indicator.getMinInt();
                int maxi = indicator.getMaxInt();
                if (valInt < mini || valInt > maxi) {
                    super.addLineError("integer value out of bound: " + answerStr + " not in [" + mini + ", " + maxi + "].");
                    return null;
                }
                answer = new Answer();
                answer.setIntValue(valInt);
                break;

            case Constants.INDICATOR_TYPE_FLOAT:
                double valFloat = StringUtils.str2double(answerStr, Constants.DOUBLE_ERROR);
                if (valFloat == Constants.DOUBLE_ERROR) {
                    super.addLineError("invalid float value: " + answerStr);
                    return null;
                } 
                double minf = indicator.getMinDouble();
                double maxf = indicator.getMaxDouble();
                if (valFloat < minf || valFloat > maxf) {
                    super.addLineError("integer value out of bound: " + answerStr + " not in [" + minf + ", " + maxf + "].");
                    return null;
                }
                answer = new Answer();
                answer.setFloatValue(valFloat);
                break;

            default:
                // text
                answer = new Answer();
                answer.setTextValue(answerStr);
                break;
        }

        return answer;
    }

    
    private Review parseReview(String[] line, Indicator indicator) {
        String reviewerUid = super.getColumn(line, COL_REVIEWER_UID);
        String reviewOpinionStr = super.getColumn(line, COL_REVIEW_OPINION);
        String reviewAnswerStr = super.getColumn(line, COL_REVIEW_ANSWER);
        String reviewComments = super.getColumn(line, COL_REVIEW_COMMENTS);

        User reviewer = null;
        if (!StringUtils.isEmpty(reviewerUid)) {
            reviewer = ctx.getUser(reviewerUid);
            if (reviewer == null) {
                super.addLineError("reviewer does not exist: " + reviewerUid);
                return null;
            }
        }

        if (StringUtils.isEmpty(reviewOpinionStr)) {
            super.addLineError("missing review opinion.");
            return null;
        }

        short opinion = 0;
        if (reviewOpinionStr.equalsIgnoreCase("agree")) {
            opinion = Constants.PEER_REVIEW_OPINION_AGREE;
        } else if (reviewOpinionStr.equalsIgnoreCase("AGREE WITH COMMENT")) {
            opinion = Constants.PEER_REVIEW_OPINION_AGREE_COMMENT;
        } else if (reviewOpinionStr.equalsIgnoreCase("DISAGREE")) {
            opinion = Constants.PEER_REVIEW_OPINION_DISAGREE;
        } else if (reviewOpinionStr.equalsIgnoreCase("NOT QUALIFIED")) {
            opinion = Constants.PEER_REVIEW_OPINION_NOT_QUALIFIED;
        } else {
            super.addLineError("invalid opinion value: " + reviewOpinionStr);
            return null;
        }

        Answer answer = null;
        if (opinion == Constants.PEER_REVIEW_OPINION_DISAGREE) {
            if (StringUtils.isEmpty(reviewAnswerStr)) {
                super.addLineError("missing review answer.");
                return null;
            }
            answer = parseAnswer("reviewer answer", reviewAnswerStr, indicator);
            if (answer == null) return null;
        }

        // create review object
        Review review = new Review();
        review.setAnswer(answer);
        review.setComments(reviewComments);
        review.setReviewer(reviewer);
        review.setOpinion(opinion);

        return review;
    }


    private int getChoicePos(String choiceType, String choiceStr, int numChoices) {
        int choice = StringUtils.str2int(choiceStr);
        if (choice <= 0) {
            super.addLineError("invalid " + choiceType + " choice value: " + choiceStr);
            return -1;
        }

        if (choice > numChoices) {
            super.addLineError(choiceType + " choice value out of bound: " + choiceStr + " >" + numChoices);
            return -1;
        }

        return choice-1;
    }


}
