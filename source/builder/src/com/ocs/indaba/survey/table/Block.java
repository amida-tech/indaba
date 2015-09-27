/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.survey.table;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.ocs.util.JSONUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author yc06x
 */
public class Block {

    static public final short BLOCK_TYPE_CELL = 1;
    static public final short BLOCK_TYPE_REGION = 2;

    static public final short BLOCK_DATA_TYPE_LABEL = 1;
    static public final short BLOCK_DATA_TYPE_HEADER = 2;
    static public final short BLOCK_DATA_TYPE_QUESTION = 3;

    private short blockType;
    private short dataType;
    private int col;
    private int row;
    private int colSpan;
    private int rowSpan;
    private String data;

    private int indicatorId;

    public Block(short blockType, short dataType, int row, int col) {
        this.blockType = blockType;
        this.dataType = dataType;
        this.col = col;
        this.row = row;
        this.data = null;
        this.indicatorId = 0;
        this.colSpan = 1;
        this.rowSpan = 1;
    }

    public void setData(String data) {
        this.data = data;
    }

    public short getBlockType() {
        return blockType;
    }

    public short getDataType() {
        return dataType;
    }

    public int getRowNumber() {
        return row;
    }

    public int getColNumber() {
        return col;
    }

    public String getData() {
        return data;
    }

    public int getIndicatorId() {
        return this.indicatorId;
    }

    public void setIndicatorId(int value) {
        this.indicatorId = value;
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

    public static String toJson(List<List<Block>> blocks) {
        JSONObject root = new JSONObject();
        JSONArray rowArray = new JSONArray();
        root.put("rows", rowArray);

        for (int row = 0; row < blocks.size(); row++) {
            JSONObject rowObj = new JSONObject();
            rowArray.add(rowObj);
            rowObj.put("row", row);
            JSONArray colArray = new JSONArray();
            rowObj.put("cols", colArray);

            List<Block> rowBlocks = blocks.get(row);
            for (Block block : rowBlocks) {
                JSONObject colObj = new JSONObject();
                colArray.add(colObj);
                colObj.put("type", block.getBlockType());
                colObj.put("dtype", block.getDataType());
                colObj.put("col", block.getColNumber());

                if (block.getBlockType() == Block.BLOCK_TYPE_REGION) {
                    colObj.put("colSpan", block.getColSpan());
                    colObj.put("rowSpan", block.getRowSpan());
                }

                if (block.getDataType() == Block.BLOCK_DATA_TYPE_QUESTION) {
                    colObj.put("data", block.getIndicatorId());
                } else {
                    colObj.put("data", block.getData());
                }
            }
        }

        return root.toJSONString();
    }


    public static List<List<Block>> fromJson(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) return null;

        JSONObject root = JSONUtils.parseJSONStr(jsonString);

        if (root == null) return null;

        List<List<Block>> blocks = new ArrayList<List<Block>>();

        JSONArray rowArray = (JSONArray)root.get("rows");
        Iterator it = rowArray.iterator();
        while (it.hasNext()) {
            JSONObject rowObj = (JSONObject)it.next();
            int row = JSONUtils.getIntValue(rowObj, "row");
            List<Block> rowBlocks = new ArrayList<Block>();
            blocks.add(row, rowBlocks);

            JSONArray colArray = (JSONArray)rowObj.get("cols");
            Iterator colIt = colArray.iterator();
            while (colIt.hasNext()) {
                JSONObject colObj = (JSONObject)colIt.next();
                int col = JSONUtils.getIntValue(colObj, "col");
                short blockType = JSONUtils.getShortValue(colObj, "type");
                short dataType = JSONUtils.getShortValue(colObj, "dtype");

                Block block = new Block(blockType, dataType, row, col);
                rowBlocks.add(block);
                if (blockType == Block.BLOCK_TYPE_REGION) {
                    block.setRowSpan(JSONUtils.getIntValue(colObj, "rowSpan"));
                    block.setColSpan(JSONUtils.getIntValue(colObj, "colSpan"));
                }
                if (dataType == Block.BLOCK_DATA_TYPE_QUESTION) {
                    block.setIndicatorId(JSONUtils.getIntValue(colObj, "data"));
                } else {
                    block.setData(JSONUtils.getStringValue(colObj, "data"));
                }
            }
        }
        return blocks;
    }

}
