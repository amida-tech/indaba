/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.tableprocessor;

import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.survey.table.Block;
import com.ocs.ssu.CellGrid;
import com.ocs.ssu.ExcelReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author yc06x
 */
public class TableParser {

    private static final Logger logger = Logger.getLogger(TableParser.class);

    private ExcelReader reader = null;
    private String fileName;
    private LoginUser user;
    private List<String> errors = null;
    private ArrayList<List<Block>> blocks = null;

    public TableParser(LoginUser user, String fileName) {
        this.fileName = fileName;
        this.user = user;
        reader = null;
    }

    public List<String> getErrors() {
        return errors;
    }

    public int getErrorCount() {
        return (errors == null) ? 0 : errors.size();
    }

    public List<List<Block>> getBlocks() {
        return blocks;
    }


    private void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        errors.add(error);
    }


    public void parse() throws IOException {
        reader = new ExcelReader(fileName);

        try {
            if (reader.setWorkSheet(1) == null) {
                addError(user.message(ControlPanelMessages.TABLE_PARSER__NO_TABLE_SHEET));
                return;
            }
        } catch (Exception ex) {
            addError(user.message(ControlPanelMessages.TABLE_PARSER__NO_TABLE_SHEET));
            return;
        }

        CellGrid cellGrid = reader.getCellGrid();
        if (cellGrid == null) {
            addError(user.message(ControlPanelMessages.TABLE_PARSER__NO_CELLS));
            return;
        }

        List<CellRangeAddress> regions = reader.getMergedRegions();

        // create blocks from cell grid and regions
        blocks = new ArrayList<List<Block>>(cellGrid.getMaxRowNumber()+1);

        for (int row = 0; row <= cellGrid.getMaxRowNumber(); row++) {
            List<Block> rowBlocks = new ArrayList<Block>();
            blocks.add(row, rowBlocks);

            for (int col = 0; col <= cellGrid.getMaxColumnNumber(); col++) {
                // find the region that covers this cell
                CellRangeAddress region = findCoverRegion(regions, row, col);
                AugmentedBlock block;
                if (region == null) {
                    block = createBlock(AugmentedBlock.BLOCK_TYPE_CELL, cellGrid.getCell(row, col), row, col);
                } else if (region.getFirstColumn() == col && region.getFirstRow() == row) {
                    // the block starts with this cell
                    block = createBlock(AugmentedBlock.BLOCK_TYPE_REGION, cellGrid.getCell(row, col), row, col);
                    block.setColSpan(region.getLastColumn() - region.getFirstColumn() + 1);
                    block.setRowSpan(region.getLastRow() - region.getFirstRow() + 1);
                } else {
                    // the cell is covered by the region, no block here
                    block = null;
                }

                if (block != null) rowBlocks.add(block);
            }
        }
    }


    private AugmentedBlock createBlock(short blockType, String data, int row, int col) {
        if (data == null) data = "";
        data = data.trim();
        short dataType;

        if (data.startsWith("<H>")) {
            dataType = AugmentedBlock.BLOCK_DATA_TYPE_HEADER;
            data = data.substring(3);
        } else if (data.startsWith("{") && data.endsWith("}")) {
            dataType = AugmentedBlock.BLOCK_DATA_TYPE_QUESTION;
            data = data.substring(1, data.length()-1);
        } else {
            dataType = AugmentedBlock.BLOCK_DATA_TYPE_LABEL;
        }

        AugmentedBlock block = new AugmentedBlock(blockType, dataType, row, col);
        block.setData(data);
        return block;
    }


    private CellRangeAddress findCoverRegion(List<CellRangeAddress> regions, int row, int col) {
        if (regions == null || regions.isEmpty()) return null;

        for (CellRangeAddress region : regions) {
            if (row >= region.getFirstRow() && row <= region.getLastRow() &&
                col >= region.getFirstColumn() && col <= region.getLastColumn()) return region;
        }

        return null;
    }

    public void close() {
        if (reader != null) reader.close();
    }

}
