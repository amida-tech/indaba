/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.common;

/**
 *
 * @author Jeff
 */
public class Tools {

    public static final String APPROVE = "approve";
    public static final String JOURNAL_CREATE = "journal create";
    public static final String JOURNAL_EDIT = "journal edit";
    public static final String JOURNAL_OVERALL_REVIEW = "journal overall review";
    public static final String JOURNAL_PEER_REVIEW = "journal peer review";
    public static final String JOURNAL_PR_REVIEW = "journal pr review";
    public static final String JOURNAL_REVIEW = "journal review";
    public static final String JOURNAL_REVIEW_RESPONSE = "journal review response";
    public static final String JOURNAL_VIEW = "journal view";
    public static final String PAYMENT = "payment";
    public static final String START_HORSE = "start horse";
    public static final String SURVEY_CREATE = "survey create";
    public static final String SURVEY_EDIT = "survey edit";
    public static final String SURVEY_OVERALL_REVIEW = "survey overall review";
    public static final String SURVEY_PEER_REVIEW = "survey peer review";
    public static final String SURVEY_PR_REVIEW = "survey pr review";
    public static final String SURVEY_REVIEW = "survey review";
    public static final String SURVEY_REVIEW_RESPONSE = "survey review response";
    public static final String SURVEY_VIEW = "survey view";

    public static void main(String args[]) {
       
            System.out.println(java.text.MessageFormat.format("{0,number,#}", 1234567890));
    }
    private static final String[] tools = new String[]{"approve",
        "journal create",
        "journal edit",
        "journal overall review",
        "journal peer review",
        "journal pr review",
        "journal review",
        "journal review response",
        "journal view",
        "payment",
        "start horse",
        "survey create",
        "survey edit",
        "survey overall review",
        "survey peer review",
        "survey pr review",
        "survey review",
        "survey review response",
        "survey view"};
}
