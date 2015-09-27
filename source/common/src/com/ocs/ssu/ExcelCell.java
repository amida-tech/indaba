/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

/**
 *
 * @author yc06x
 */
public class ExcelCell {

    private int col;
    private int row;
    private int colSpan;
    private int rowSpan;
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public void setRowSpan(int value) {
        this.rowSpan = value;
    }

    public int getRowSpan() {
        return this.rowSpan;
    }

    public void setColSpan(int value) {
        this.colSpan = value;
    }

    public int getColSpan() {
        return this.colSpan;
    }
}
