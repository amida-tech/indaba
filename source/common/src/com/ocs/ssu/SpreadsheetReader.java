/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

import java.io.IOException;
import java.util.Stack;

/**
 *
 * @author yc06x
 */
public abstract class SpreadsheetReader {

    private Stack<String[]> stack = new Stack<String[]>();

    public void returnLine(String[] line) {
        if (line != null) stack.push(line);
    }

    protected abstract String[] nextLine() throws IOException;

    public String[] readNext() throws IOException {
        if (!stack.isEmpty()) return stack.pop();
        return nextLine();
    }

    abstract public int getLineNumber();

    abstract public void close();
}
