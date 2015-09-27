/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.indaba.aggregation.common.Constants;

/**
 *
 * @author jiangjeff
 */
public class LabelUtils {

    public static String getScoreLabel(double score) {
        if (score < 60) {
            return Constants.SCORE_LABEL_VERY_WEAK;
        } else if (score >= 60 && score < 70) {
            return Constants.SCORE_LABEL_WEEK;
        } else if (score >= 70 && score < 80) {
            return Constants.SCORE_LABEL_MODERATE;
        } else if (score >= 80 && score < 90) {
            return Constants.SCORE_LABEL_STRONG;
        } else {
            return Constants.SCORE_LABEL_VERY_STRONG;
        }
    }
}
