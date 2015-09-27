/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author Jeanbone
 */
public class TagContent {

    private int tag_id;
    private int survey_answer_id;
//    private Date tagging_time;
//    private int user_id;
    private String tag_label;

    private int survey_question_id;
//    private int survey_indicator_id;
//    private int survey_category_id;

    private String question;
    private String q_public_name;
    private String action_url;

    /**
     * @return the tag_id
     */
    public int getTag_id() {
        return tag_id;
    }

    /**
     * @param tag_id the tag_id to set
     */
    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    /**
     * @return the survey_answer_id
     */
    public int getSurvey_answer_id() {
        return survey_answer_id;
    }

    /**
     * @param survey_answer_id the survey_answer_id to set
     */
    public void setSurvey_answer_id(int survey_answer_id) {
        this.survey_answer_id = survey_answer_id;
    }

    /**
     * @return the tag_label
     */
    public String getTag_label() {
        return tag_label;
    }

    /**
     * @param tag_label the tag_label to set
     */
    public void setTag_label(String tag_label) {
        this.tag_label = tag_label;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the q_public_name
     */
    public String getQ_public_name() {
        return q_public_name;
    }

    /**
     * @param q_public_name the q_public_name to set
     */
    public void setQ_public_name(String q_public_name) {
        this.q_public_name = q_public_name;
    }

    /**
     * @return the action_url
     */
    public String getAction_url() {
        return action_url;
    }

    /**
     * @param action_url the action_url to set
     */
    public void setAction_url(String action_url) {
        this.action_url = action_url;
    }

    /**
     * @return the survey_question_id
     */
    public int getSurvey_question_id() {
        return survey_question_id;
    }

    /**
     * @param survey_question_id the survey_question_id to set
     */
    public void setSurvey_question_id(int survey_question_id) {
        this.survey_question_id = survey_question_id;
    }

    
}
