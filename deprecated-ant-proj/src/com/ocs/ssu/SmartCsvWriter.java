/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author yc06x
 */
public class SmartCsvWriter implements SpreadsheetWriter {

    private CSVWriter writer = null;

    public SmartCsvWriter(OutputStream output) throws UnsupportedEncodingException {
        writer = new CSVWriter(new OutputStreamWriter(output, "UTF-8"));
    }

    public void writeNext(String[] row) throws IOException {
        if (writer != null) writer.writeNext(row);
    }

    public void flush() throws IOException {
        if (writer != null) writer.flush();
    }

    public void close() {
        if (writer != null) try {
            writer.close();
        } catch (IOException ex) {}
    }

}
