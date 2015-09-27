/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author yc06x
 */
public class ExcelReader extends SpreadsheetReader {

    public static final String FILE_TYPE_XSL = "xls";
    public static final String FILE_TYPE_XSLX = "xlsx";

    private Workbook workbook = null;
    Sheet sheet = null;
    private int curRowNum = 0;
    private int firstRowNum = -1;
    private int lastRowNum = 0;
    private FileInputStream file = null;
    DataFormatter dataFormatter = new DataFormatter();


    private void createReader(FileInputStream file, String fileType) throws IOException {
        if (FILE_TYPE_XSL.equalsIgnoreCase(fileType)) {
            workbook = new HSSFWorkbook(file);
        } else if (FILE_TYPE_XSLX.equalsIgnoreCase(fileType)) {
            workbook = new XSSFWorkbook(file);
        } else {
            throw new UnsupportedOperationException("Not supported file type " + fileType);
        }

        this.file = file;
        sheet = workbook.getSheetAt(0);
        if (sheet != null) {
            firstRowNum = sheet.getFirstRowNum();
            lastRowNum = sheet.getLastRowNum();
            curRowNum = firstRowNum;
        }
    }

    public ExcelReader(FileInputStream file, String fileType) throws IOException {
        createReader(file, fileType);
    }

    public ExcelReader(String fileName) throws IOException {
        int pos = fileName.lastIndexOf('.');
        String fileType = fileName.substring(pos+1);

        createReader(new FileInputStream(new File(fileName)), fileType);
    }

    public Sheet setWorkSheet(int sheetNumber) {
        if (sheetNumber < 0) return null;
        
        sheet = workbook.getSheetAt(sheetNumber);
        if (sheet != null) {
            firstRowNum = sheet.getFirstRowNum();
            lastRowNum = sheet.getLastRowNum();
            curRowNum = firstRowNum;
        }
        return sheet;
    }


    public List<CellRangeAddress> getMergedRegions() {
        if (sheet == null) return null;
        
        int numRegions = sheet.getNumMergedRegions();
        if (numRegions <= 0) return null;

        ArrayList<CellRangeAddress> regions = new ArrayList<CellRangeAddress>();
        for (int i = 0; i < numRegions; i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            regions.add(region);
        }

        return regions;
    }


    public CellGrid getCellGrid() {
        if (firstRowNum < 0) return null;
        CellGrid grid = new CellGrid(this.lastRowNum);

        for (int i = this.firstRowNum; i <= this.lastRowNum; i++) {
            Row row = sheet.getRow(i);
            grid.setRow(i, toLine(row));
        }
        grid.dropDanglingRows();
        return grid;
    }


    protected String[] nextLine() throws IOException {
        if (firstRowNum < 0) return null;

        String[] line;
        while (curRowNum <= lastRowNum) {
            Row row = sheet.getRow(curRowNum);
            curRowNum++;

            line = toLine(row);
            int goodFields = 0;

            if (line != null) {
                for (int i = 0; i < line.length; i++) {
                    if (line[i] == null) line[i]="";
                    else {
                        line[i] = line[i].trim();
                        if (!line[i].isEmpty()) {
                            goodFields++;
                        }
                    }
                }
            }
            if (goodFields > 0) return line;
        }
        return null;
    }


    private String[] toLine(Row row) {
        if (row == null) return null;

        short firstCellNum = row.getFirstCellNum();
        short lastCellNum = row.getLastCellNum();  // this is 1-based
        String[] line = new String[lastCellNum];

        for (short cellNum = 0; cellNum < lastCellNum; cellNum++) {
            Cell cell = cellNum < firstCellNum ? null : row.getCell(cellNum);

            if (cell == null) {
                line[cellNum] = "";
                continue;
            }


            switch(cell.getCellType()) {

                case Cell.CELL_TYPE_BOOLEAN:
                case Cell.CELL_TYPE_NUMERIC:
                    String cellStr = dataFormatter.formatCellValue(cell);
                    line[cellNum] = cellStr;
                    break;

                case Cell.CELL_TYPE_STRING:
                    line[cellNum] = cell.getStringCellValue();
                    break;

                default:
                    line[cellNum] = "";
                    break;
            }
        }

        return line;
    }

    @Override
    public int getLineNumber() {
        return curRowNum;
    }

    @Override
    public void close() {
        if (file == null) return;
        try {
            file.close();
        } catch (Exception e) {}
    }


}
