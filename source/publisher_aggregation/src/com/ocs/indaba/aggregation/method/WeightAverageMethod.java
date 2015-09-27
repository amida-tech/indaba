/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.method;

import java.util.List;

/**
 *
 * @author luwb
 */
public class WeightAverageMethod<T extends Number> extends BaseMethod<T> {

    @Override
    public double calculate(List<T> inputs) {
        throw new UnsupportedOperationException("Not supported for WeightAverageMethod.");
    }

    @Override
    public double calculateWithWeight(List<T> inputs, List<Integer> weights) {
        if(inputs.size() != weights.size())
            return 0;

        double result = 0;
        int total = 0;
        for(int i=0; i<inputs.size(); i++){
            double value = inputs.get(i).doubleValue();
            int weight = weights.get(i);
            total += weight;
            result += value * weight;
        }
        return result/total;
    }
}
