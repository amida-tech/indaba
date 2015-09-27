/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author luwb
 */
public class MedianMethod <T extends Number> extends BaseMethod<T>{

    @Override
    public double calculate(List<T> inputs) {
        /*Comparator cm = new Comparator(){
            public int compare(Object o1, Object o2) {
                T t1 = (T)o1;
                T t2 = (T)o2;
                if(t1.doubleValue() < t2.doubleValue())
                    return -1;
                else if(t1.doubleValue() > t2.doubleValue())
                    return 1;
                else
                    return 0;
            }
        };*/
        List<Double> list = new ArrayList<Double>();
        for(T t : inputs){
            list.add(t.doubleValue());
        }
         Collections.sort(list);
         int index = list.size() / 2;
         return list.get(index);
    }

    @Override
    public double calculateWithWeight(List<T> inputs, List<Integer> weights) {
        return calculate(inputs);
    }

}
