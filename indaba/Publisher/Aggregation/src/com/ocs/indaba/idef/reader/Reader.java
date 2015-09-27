/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.ssu.NotUTF8EncodedException;
import com.ocs.ssu.ReaderFactory;
import com.ocs.ssu.SpreadsheetReader;
import com.ocs.ssu.UnsupportedFileTypeException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author yc06x
 */
public class Reader {

    private SpreadsheetReader reader;
    protected ProcessContext ctx;
    private File file;

    private int errCount = 0;

    protected void createReader(ProcessContext ctx, File file, String[] colDefs) {
        this.ctx = ctx;
        this.file = file;

        try {
            reader = ReaderFactory.createReader(file);
            String[] line = reader.readNext();
            if (line == null) {
                addError("File contains no data: " + file.getAbsolutePath());
            } else {
                checkHeaderLine(line, colDefs);
            }
        } catch (FileNotFoundException ex) {
            addError("File not found: " + file.getAbsolutePath());
        } catch (IOException ex) {
            addError("Cannot read file: " + file.getAbsolutePath());
        } catch (NotUTF8EncodedException ex) {
            addError("File not UTF-8 encoded: " + file.getAbsolutePath());
        } catch (UnsupportedFileTypeException ex) {
            addError("Unsupported file type: " + file.getAbsolutePath());
        }        
    }


    protected int getLineNumber() {
        return reader.getLineNumber();
    }

    protected String[] readNext() {
        String[] line = null;
        try {
            line = reader.readNext();
        } catch (IOException ex) {
            addError("Failed to read file: " + file.getAbsolutePath());
            line = null;
        }
        return line;
    }


    protected void returnLine(String[] line) {
        reader.returnLine(line);
    }


    public void close() {
        if (reader != null) reader.close();
    }

    public File getFile() {
        return file;
    }

    protected void addLineError(String msg) {
        String err = "File: " + file.getName() + " @" + reader.getLineNumber() + ": " + msg;
        ctx.addError(err);
        errCount++;
    }

    protected void addError(String msg) {
        ctx.addError("File: " + file.getName() + ": " + msg);
        errCount++;
    }

    public int getErrorCount() {
        return errCount;
    }

    protected String getColumn(String[] line, int col) {
        if (line == null || line.length == 0) return null;
        if (col < 0 || col >= line.length) return null;
        return line[col];
    }


    protected boolean checkHeaderLine(String[] line, String[] colDefs) {

        if (line == null || line.length == 0) {
            // missing header line
            addLineError("missing header line.");
            return false;
        }

        if (line.length < colDefs.length) {
            addLineError("invalid header line: must contain " + colDefs.length + " columns.");
            return false;
        }

        int numErrs = 0;
        for (int i = 0; i < colDefs.length; i++) {
            if (!colDefs[i].equalsIgnoreCase(getColumn(line, i))) {
                addLineError("column " + i + " must be " + colDefs[i]);
                numErrs++;
            }
        }

        return numErrs == 0;
    }


    protected boolean checkRequiredColumns(String[] line, String[] colDefs) {
        int numErrs = 0;

        for (int i = 0; i < colDefs.length; i++) {
            if (Character.isUpperCase(colDefs[i].charAt(0))) {
                // this is a required columns
                String col = getColumn(line, i);
                if (col == null || col.isEmpty()) {
                    addLineError("missing value for required column [" + colDefs[i] + "].");
                    numErrs ++;
                }
            }
        }

        return numErrs == 0;
    }


    protected String[] parseOrgNames(String names) {
        String[] result = names.split("[;]");
        return result;
    }


    protected Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try {
            result = format.parse(date);
        } catch (ParseException ex) {
            result = null;
        }

        return result;
    }


    protected short parseVisibility(String vis) {
        if (vis == null || vis.isEmpty()) return -1;

        if (vis.equalsIgnoreCase("PUBLIC")) {
            return Constants.VISIBILITY_PUBLIC;
        } else if (vis.equalsIgnoreCase("PRIVATE")) {
            return Constants.VISIBILITY_PRIVATE;
        } else {
            return -1;
        }
    }


    protected short parseAnswerType(String type) {
        if (type == null || type.isEmpty()) return -1;

        if (type.equalsIgnoreCase("SINGLE CHOICE")) {
            return Constants.INDICATOR_TYPE_SINGLE_CHOICE;
        } else if (type.equalsIgnoreCase("MULTI CHOICE")) {
            return Constants.INDICATOR_TYPE_MULTI_CHOICE;
        } else if (type.equalsIgnoreCase("INT") || type.equalsIgnoreCase("INTEGER")) {
            return Constants.INDICATOR_TYPE_INTEGER;
        } else if (type.equalsIgnoreCase("FLOAT")) {
            return Constants.INDICATOR_TYPE_FLOAT;
        } else if (type.equalsIgnoreCase("TEXT")) {
            return Constants.INDICATOR_TYPE_TEXT;
        } else {
            return -1;
        }
    }

}
