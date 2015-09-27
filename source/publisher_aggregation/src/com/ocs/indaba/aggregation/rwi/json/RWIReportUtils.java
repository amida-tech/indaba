/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.rwi.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScoreVO;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.util.Tree;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff Jiang
 */
public class RWIReportUtils {

    protected final static int ORIG_CAT_COUNT = 2;
    private static final String JSON_KEY_RANGE = "range";
    //private static final String JSON_KEY_LEVEL = "level";
    private static final String JSON_KEY_LABEL = "label";
    private static final String JSON_KEY_COLOR = "color";
    private static final String COLOR_COMPREHENSIVE = "green";
    private static final String COLOR_PARTITIAL = "yellow";
    private static final String COLOR_LIMITED = "orange";
    private static final String COLOR_RED = "red";

    public static JSONArray generateScoreDisplayJsonObj() {
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(generateRangeJsonObj(Constants.SCORE_LABEL_STRONG, 75, Integer.MAX_VALUE, COLOR_COMPREHENSIVE));
        jsonArr.add(generateRangeJsonObj(Constants.SCORE_LABEL_MODERATE, 50, 75, COLOR_PARTITIAL));
        jsonArr.add(generateRangeJsonObj(Constants.SCORE_LABEL_WEEK, 25, 50, COLOR_LIMITED));
        jsonArr.add(generateRangeJsonObj(Constants.SCORE_LABEL_VERY_WEAK, Integer.MIN_VALUE, 25, COLOR_RED));
        return jsonArr;
    }

    public static JSONObject generateRangeJsonObj(String label, int from, int to, String color) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(JSON_KEY_LABEL, label);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(from);
        jsonArr.add(to);
        jsonObj.put(JSON_KEY_RANGE, jsonArr);
        jsonObj.put(JSON_KEY_COLOR, color);
        return jsonObj;
    }

    public static ScoreVO computeAverageScore(List<Tree<ScorecardBaseNode>> questionSet, int index) {
        if (questionSet == null || questionSet.isEmpty()) {
            return null;
        }
        double score = 0.0d;
        CategoryNode catNode = null;
        int count = 0;
        switch (index) {
            case 1: {
                //catNode = (CategoryNode) questionSet.get(0).getNode();
                //score = (catNode.hasUseScore()) ? catNode.getMean() : -1;
                score = -1;
                break;
            }
            case 2: {
                for (int i = 1, n = questionSet.size() - 1; i < n; ++i) {
                    catNode = (CategoryNode) questionSet.get(i).getNode();
                    if (catNode.hasUseScore()) {
                        //count += catNode.getUsedScoreCount();
                        //score += (catNode.getMean() * catNode.getUsedScoreCount());
                        ++count;
                        score += catNode.getMean();
                    }
                }
                score = (count > 0) ? (score / count) : -1;
                break;
            }
            case 3: {
                catNode = (CategoryNode) questionSet.get(questionSet.size() - 1).getNode();
                score = (catNode.hasUseScore()) ? catNode.getMean() : -1;
                //count = catNode.getUsedScoreCount();
                count = 1;
                break;
            }
        }
        return new ScoreVO(count, score);
    }

    public static ScoreVO computeAverageScore4IV2(List<Tree<ScorecardBaseNode>> questionSet) {
        if (questionSet == null || questionSet.isEmpty()) {
            return null;
        }
        double score = 0.0d;
        int count = 0;

        List<Tree<ScorecardBaseNode>> questions = (List<Tree<ScorecardBaseNode>>) questionSet.get(1).getChildren();
        if (questions != null && !questions.isEmpty()) {
            for (int size = questions.size(), i = size - 1; (i >= 0 && i + 4 >= size); --i) {
                ScorecardBaseNode node = questions.get(i).getNode();
                if (node.hasUseScore() && node instanceof QuestionNode) {
                    ++count;
                    score += ((QuestionNode) node).getScore();
                }
            }
        }
        Tree<ScorecardBaseNode> qsTree = (Tree<ScorecardBaseNode>) questionSet.get(2);
        if (qsTree != null && qsTree.getNode().hasUseScore()) {
            score += ((CategoryNode) qsTree.getNode()).getMean();
            ++count;
        }
        score = (count > 0) ? (score / count) : -1;
        return new ScoreVO(count, score);
    }

    /**
     * Generate virtural subcat node id
     *
     * @param nodeId
     * @param index
     * @return
     */
    public static int generateVirtualSubcatNodeId(int nodeId, int index) {
        int bit = 4;
        if (index / 10 == 0) {
            bit = 1;
        } else if (index / 100 == 0) {
            bit = 2;
        } else if (index / 1000 == 0) {
            bit = 3;
        }
        return Integer.parseInt("-" + bit + index + nodeId);
    }

    /**
     * Parse virtual subcat node id
     */
    public static int[] parseVirtualSubcatNodeId(int val) {
        int[] arr = new int[]{0, 0};
        if (val >= 0) {
            arr[0] = val;
            return arr;
        }
        String s = String.valueOf(val * (-1));
        int bit = (s.charAt(0) - '0');
        int nodeId = Integer.parseInt(s.substring(1 + bit));
        int index = Integer.parseInt(s.substring(1, 1 + bit));
        arr[0] = nodeId;
        arr[1] = index;
        return arr;
    }

    public static String fixQuestionLabel(String label, String prefixLabel) {
        int index = findIndexOfAt(label, ".", 3);

        return (index == -1) ? prefixLabel : prefixLabel + label.substring(index);
    }

    public static String fixQuestionSetLabel(String label, String prefixLabel) {
        int index = findIndexOfAt(label, ".", 3);
        return (index == -1) ? prefixLabel : prefixLabel + label.substring(index);
    }

    public static int findIndexOfAt(String s, String sub, int at) {
        int index = 0;
        int from = 0;
        int i = 0;
        while (i < at && index != -1) {
            index = s.indexOf(sub, from);
            if (index == -1) {
                return index;
            }
            from = index + 1;
            ++i;
        }

        return (i == at) ? index : -1;
    }

    public static void main(String args[]) {
        String ss[] = {"3.1.1", "3.1.2", "3.1.2.038", "3.1.2.038.j", "3.1.2", "3.1.2.043", "3.1.2.037"};
        for (String s : ss) {
            System.err.println("BEFOR: " + s + "\t==> " + fixQuestionSetLabel(s, "3.2.1"));
        }
    }
}
