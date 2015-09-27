/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author yc06x
 */
public class WriterFactory {

    static public final int FILE_TYPE_CSV = 1;
    static public final int FILE_TYPE_XLS = 2;


    static public SpreadsheetWriter createWriter(OutputStream output, int fileType) throws UnsupportedFileTypeException, UnsupportedEncodingException {
        switch (fileType) {
            case FILE_TYPE_CSV:
                return new SmartCsvWriter(output);

            case FILE_TYPE_XLS:
                return new ExcelWriter(output);
                
            default:
                throw new UnsupportedFileTypeException(""+fileType);
        }
    }

}
