/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import au.com.bytecode.opencsv.CSVReader;
import com.ocs.intl.UTF8Support;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author yc06x
 */
public class ReaderFactory {

    public static SpreadsheetReader createReader(File file) throws FileNotFoundException, IOException, NotUTF8EncodedException, UnsupportedFileTypeException {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf('.');
        String ext = fileName.substring(pos+1);

        if ("csv".equalsIgnoreCase(ext) || "bcsv".equalsIgnoreCase(ext)) {
            if (!UTF8Support.isUTF8Encoded(file)) {
                throw new NotUTF8EncodedException();
            }
            return new SmartCsvReader(new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8")));
        } else if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext)) {
            return new ExcelReader(new FileInputStream(file), ext);
        } else {
            throw new UnsupportedFileTypeException(ext);
        }
    }

}
