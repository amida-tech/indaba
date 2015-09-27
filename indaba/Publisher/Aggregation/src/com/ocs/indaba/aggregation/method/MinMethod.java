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
public class MinMethod<T extends Number> extends BaseMethod<T>{

    public double calculate(List<T> inputs) {
        double min = inputs.get(0).doubleValue();
        for(T t : inputs){
            if(t.doubleValue() < min)
                min = t.doubleValue();
        }
        return min;
    }

    public double calculateWithWeight(List<T> inputs, List<Integer> weights) {
        return calculate(inputs);
    }
    
}
