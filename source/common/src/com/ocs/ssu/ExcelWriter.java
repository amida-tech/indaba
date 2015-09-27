/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author yc06x
 */
public class ExcelWriter implements SpreadsheetWriter {

    private OutputStream output = null;
    private HSSFWorkbook workbook = null;
    private HSSFSheet sheet = null;
    private int nextRowNum = 0;

    public ExcelWriter(OutputStream output) {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Sheet1");
        this.output = output;
    }

    public void writeNext(String[] line) throws IOException {
        if (line == null || line.length <= 0) return;

        Row row = sheet.createRow(nextRowNum++);
        for (int cellNum = 0; cellNum < line.length; cellNum++) {
            Cell cell = row.createCell(cellNum);
            cell.setCellValue(line[cellNum] == null ? "" : line[cellNum]);
        }
    }


    public void writeCells(List<List<ExcelCell>> rows) throws IOException {
        if (rows == null || rows.isEmpty()) return;

        // determine regions
        for (List<ExcelCell> row : rows) {
            if (row.isEmpty()) continue;
            for (ExcelCell cell : row) {
                if (cell.getRowSpan() < 1) cell.setRowSpan(1);
                if (cell.getColSpan() < 1) cell.setColSpan(1);

                if (cell.getRowSpan() == 1 && cell.getColSpan() == 1) continue;

                sheet.addMergedRegion(new CellRangeAddress(
                        cell.getRow(), cell.getRow()+cell.getRowSpan()-1,
                        cell.getCol(), cell.getCol()+cell.getColSpan()-1));
            }
        }

        // write data
        for (List<ExcelCell> row : rows) {
            if (row.isEmpty()) continue;

            Row rowline = null;
            for (ExcelCell cell : row) {                
                if (rowline == null) {
                    rowline = sheet.createRow(cell.getRow());
                }

                Cell cellLine = rowline.createCell(cell.getCol());
                cellLine.setCellValue(cell.getData() == null ? "" : cell.getData());
            }
        }
    }


    public void flush() throws IOException {
        if (output == null) return;
        workbook.write(output);
    }

    public void close() {
        if (output == null) return;

        try {
            output.close();
        } catch (IOException ex) {
        }         
    }

}
