/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author Jeff
 */
public class ScoreVO {
    
        int usedCount = 0;
        double score = 0d;

        public ScoreVO() {
        }

        public ScoreVO(int usedCount, double score) {
            this.usedCount = usedCount;
            this.score = score;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public int getUsedCount() {
            return usedCount;
        }

        public void setUsedCount(int usedCount) {
            this.usedCount = usedCount;
        }

        @Override
        public String toString() {
            return "Score{" + "usedCount=" + usedCount + ", score=" + score + '}';
        }
}
