/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.User;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class UserReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_NAME = 1;
    static private final int COL_EMAIL = 2;
    static private final int COL_FIRST_NAME = 3;
    static private final int COL_LAST_NAME = 4;
    static private final int COL_PHONE = 5;
    static private final int COL_CELL = 6;
    static private final int COL_ADDR = 7;
    static private final int COL_BIO = 8;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "username",
        "EMAIL",
        "FIRST NAME",
        "LAST NAME",
        "phone",
        "cell",
        "address",
        "bio"
    };

    public UserReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;
        int userCount = 0;

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String userId = super.getColumn(line, COL_ID);
            String userName = super.getColumn(line, COL_NAME);
            String email = super.getColumn(line, COL_EMAIL);
            String firstName = super.getColumn(line, COL_FIRST_NAME);
            String lastName = super.getColumn(line, COL_LAST_NAME);
            String phone = super.getColumn(line, COL_PHONE);
            String cell = super.getColumn(line, COL_CELL);
            String addr = super.getColumn(line, COL_ADDR);
            String bio = super.getColumn(line, COL_BIO);

            int numErrs = 0;

            if (ctx.getUser(userId) != null) {
                super.addLineError("duplicate user ID: " + userId);
                numErrs++;
            }

            if (numErrs != 0) continue;  // don't create object

            // create user
            User user = new User();
            user.setId(userId);
            user.setAddress(addr);
            user.setBio(bio);
            user.setCell(cell);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setUserName(userName);

            ctx.addUser(userId, user);
            userCount++;
        }

        if (super.getErrorCount() == 0 && userCount == 0) {
            super.addError("No users specified.");
        }
    }

}
