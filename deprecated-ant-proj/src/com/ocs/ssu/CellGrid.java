/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import com.ocs.util.StringUtils;

/**
 *
 * @author yc06x
 */
public class CellGrid {

    private int maxRowNum;
    private int maxColNum;

    String[] grid[] = null;

    public CellGrid(int maxRowNum) {
        grid = new String[maxRowNum+1][];
        this.maxRowNum = maxRowNum;
        maxColNum = 0;
    }

    public void setRow(int rowNum, String[] row) {
        if (rowNum < 0 || rowNum > maxRowNum) return;
        if (row != null && row.length > 0) {
            if (maxColNum < row.length-1) maxColNum = row.length-1;
        }
        grid[rowNum] = row;
    }

    public String[] getRow(int rowNum) {
        if (rowNum < 0 || rowNum > maxRowNum) return null;
        return grid[rowNum];
    }

    public String getCell(int rowNum, int colNum) {
        if (colNum < 0 || colNum > maxColNum) return null;

        String[] row = getRow(rowNum);
        if (row == null) return "";

        return (colNum+1 > row.length) ? "" : row[colNum];
    }


    public int getMaxRowNumber() {
        return this.maxRowNum;
    }


    public int getMaxColumnNumber() {
        return this.maxColNum;
    }

    /*
     * Empty rows at the bottom of the grid are dnagling rows.
     */
    public void dropDanglingRows() {
        int lastRowNum = maxRowNum;
        for (int i = maxRowNum; i > 0; i--) {
            if (isEmptyRow(grid[i])) lastRowNum --;
            else break;
        }
        maxRowNum = lastRowNum;
    }


    private boolean isEmptyRow(String[] row) {
        if (row == null || row.length == 0) return true;
        for (int i = 0; i < row.length; i++) {
            String col = row[i];
            if (col == null) continue;
            if (!StringUtils.isEmpty(col.trim())) return false;
        }
        return true;
    }

}
