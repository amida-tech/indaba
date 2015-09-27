/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;

/**
 *
 * @author yc06x
 */
public class SmartCsvReader extends SpreadsheetReader {

    private CSVReader reader = null;
    private int lineNum = 0;

    public SmartCsvReader(CSVReader reader) {
        this.reader = reader;
        lineNum = 0;
    }

    public String[] nextLine() throws IOException {
        String row[];

        while ((row = reader.readNext()) != null) {
            lineNum++;
            boolean isBlank = true;

            for (int i = 0; i < row.length; i++) {
                row[i] = row[i].trim();
                if (!row[i].isEmpty()) {
                    isBlank = false;
                }
            }
            if (!isBlank) {
                break;
            }
        }

        return row;
    }

    public int getLineNumber() {
        return lineNum;
    }

    public void close() {
        try {
            if (reader != null) reader.close();
        } catch (Exception e) {}
    }

}
