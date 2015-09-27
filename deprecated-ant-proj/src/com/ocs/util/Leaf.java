/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

/**
 *
 * @author Jeff Jiang
 */
public class Leaf<T> {

    private T value;

    public Leaf() {
    }
    
    public Leaf(T value) {
        this.value = value;
    }
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Leaf{" + "value=" + value + '}';
    }
    
}
