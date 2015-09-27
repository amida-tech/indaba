/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.userimporter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class CsvParseResult {

    private List<CsvContributor> users = null;
    private List<String> errors = null;
    private int errorCount = 0;

    public void addUser(CsvContributor user) {
        if (users == null) {
            users = new ArrayList<CsvContributor>();
        }

        users.add(user);
    }

    public List<CsvContributor> getUsers() {
        return users;
    }

    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        errors.add(error);
        errorCount++;
    }

    public List<String> getErrors() {
        return errors;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

}
