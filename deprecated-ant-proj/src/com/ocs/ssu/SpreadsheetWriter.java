/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import java.io.IOException;

/**
 *
 * @author yc06x
 */
public interface SpreadsheetWriter {

    public void writeNext(String[] row) throws IOException;

    public void flush() throws IOException;

    public void close();

}
