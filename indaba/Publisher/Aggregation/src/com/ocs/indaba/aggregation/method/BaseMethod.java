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
public abstract class BaseMethod<T extends Number> {
    public abstract double calculate(List<T> inputs);
    public abstract double calculateWithWeight(List<T> inputs, List<Integer> weights);
}
