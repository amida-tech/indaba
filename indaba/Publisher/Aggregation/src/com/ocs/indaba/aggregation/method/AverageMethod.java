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
public class AverageMethod<T extends Number> extends BaseMethod<T>{

    public double calculate(List<T> inputs) {
        double result = 0;
        for(T t : inputs){
            result += t.doubleValue();
        }
        return result/inputs.size();
    }

    public double calculateWithWeight(List<T> inputs, List<Integer> weights) {
        return calculate(inputs);
    }

}
