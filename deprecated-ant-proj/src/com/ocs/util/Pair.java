/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

/**
 *
 * @author Jeff Jiang
 */
public class Pair<K1, K2> {

    private K1 k1 = null;
    private K2 k2 = null;

    public Pair() {
    }

    public Pair(K1 k1, K2 k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    public K1 getK1() {
        return k1;
    }

    public void setK1(K1 k1) {
        this.k1 = k1;
    }

    public K2 getK2() {
        return k2;
    }

    public void setK2(K2 k2) {
        this.k2 = k2;
    }
}
