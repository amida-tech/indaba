/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.userimporter;

import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.Utilities;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.service.RoleService;
import com.ocs.ssu.NotUTF8EncodedException;
import com.ocs.ssu.ReaderFactory;
import com.ocs.ssu.SpreadsheetReader;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.util.SendMail;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author yc06x
 */
@Component
public class CsvContributorParser {

    private static final Logger logger = Logger.getLogger(CsvContributorParser.class);

    private static final int COL_COUNT = 4;
    private static final int COL_POS_EMAIL = 0;
    private static final int COL_POS_FIRST_NAME = 1;
    private static final int COL_POS_LAST_NAME = 2;
    private static final int COL_POS_ROLE = 3;

    private static final String COL_LABEL_EMAIL = "EMAIL";
    private static final String COL_LABEL_FIRST_NAME = "FIRSTNAME";
    private static final String COL_LABEL_LAST_NAME = "LASTNAME";
    private static final String COL_LABEL_ROLE = "ROLE";

    private static RoleService roleSrvc = null;

    private SpreadsheetReader reader = null;
    private File file;
    private LoginUser user;
    private int projectId;


    @Autowired
    public void setRoleService(RoleService srvc) {
        logger.debug("Got Role Service");
        roleSrvc = srvc;
    }

    // This dummy constructor is needed for autowired to work
    public CsvContributorParser() {}


    public CsvContributorParser(LoginUser user, File file, int projectId) {
        this.file = file;
        this.user = user;
        this.projectId = projectId;
        reader = null;
    }

    public CsvContributorParser(LoginUser user, String file, int projectId) {
        this.file = new File(file);
        this.user = user;
        this.projectId = projectId;
        reader = null;
    }


    private boolean checkHeaderLine(String[] row, CsvParseResult result) {
        if (row.length < COL_COUNT) {
            result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INCOMPLETE_HEADER));
            return false;
        }

        for (int i = 0; i < row.length; i++) {
            if (row[i].isEmpty()) {
                result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_HEADER));
                return false;
            }
        }

        if (!Utilities.similar(row[COL_POS_EMAIL], COL_LABEL_EMAIL)) {
            result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    COL_LABEL_EMAIL, row[COL_POS_EMAIL]));
            return false;
        } else if (!Utilities.similar(row[COL_POS_FIRST_NAME], COL_LABEL_FIRST_NAME)) {
            result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, 
                    COL_LABEL_FIRST_NAME, row[COL_POS_FIRST_NAME]));
            return false;
        } else if (!Utilities.similar(row[COL_POS_LAST_NAME], COL_LABEL_LAST_NAME)) {
            result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, 
                    COL_LABEL_LAST_NAME, row[COL_POS_LAST_NAME]));
            return false;
        } else if (!Utilities.similar(row[COL_POS_ROLE], COL_LABEL_ROLE)) {
            result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, 
                    COL_LABEL_ROLE, row[COL_POS_ROLE]));
            return false;
        } else {
            return true;
        }
    }

    public CsvParseResult parse() throws IOException {

        CsvParseResult result = new CsvParseResult();

        try {
            reader = ReaderFactory.createReader(file);
        } catch (UnsupportedFileTypeException ex) {
            result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FILE_TYPE_NOT_SUPPORTED, ex.getFileType()));
            return result;
        } catch (NotUTF8EncodedException ex) {
            result.addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FILE_NOT_UTF8));
            return result;
        }
        
        String[] row;

        row = reader.readNext();

        // Look for header line
        if (!checkHeaderLine(row, result)) {
            return result;
        }

        Map<String, Role> roleMap = new HashMap<String, Role>();

        List<Role> allRoles = roleSrvc.getAllRoles(projectId);
        for (Role role : allRoles) {
            roleMap.put(role.getName().toLowerCase(), role);
        }

        while ((row = reader.readNext()) != null) {
            if (row.length < COL_POS_EMAIL+1 || StringUtils.isEmpty(row[COL_POS_EMAIL])) {
                result.addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, COL_LABEL_EMAIL)));
                continue;
            }

            if (row.length < COL_POS_FIRST_NAME+1 || StringUtils.isEmpty(row[COL_POS_FIRST_NAME])) {
                result.addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, COL_LABEL_FIRST_NAME)));
                continue;
            }
            if (row.length < COL_POS_LAST_NAME+1 || StringUtils.isEmpty(row[COL_POS_LAST_NAME])) {
                result.addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, COL_LABEL_LAST_NAME)));
                continue;
            }
            if (row.length < COL_POS_ROLE+1 || StringUtils.isEmpty(row[COL_POS_ROLE])) {
                result.addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, COL_LABEL_ROLE)));
                continue;
            }

            String email = row[COL_POS_EMAIL];
            String firstName = row[COL_POS_FIRST_NAME];
            String lastName = row[COL_POS_LAST_NAME];
            String roleName = row[COL_POS_ROLE];

            if (!SendMail.ValidateAddress(email)) {
                result.addError(formatError(user.message(ControlPanelMessages.USER_IMPORT__INVALID_EMAIL, email)));
                continue;
            }

            if (email.length() > ControlPanelConstants.MAX_LENGTH_USER_EMAIL) {
                result.addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FIELD_TOO_LONG, "EMAIL",
                        ControlPanelConstants.MAX_LENGTH_USER_EMAIL)));
                continue;
            }

            roleName = (roleName != null) ? roleName.toLowerCase() : null;
            Role role = (roleName == null) ? null : roleMap.get(roleName);
            if (role == null) {
                result.addError(formatError(user.message(ControlPanelMessages.USER_IMPORT__INVALID_ROLE, roleName)));
                continue;
            }

            if (firstName.length() > ControlPanelConstants.MAX_LENGTH_USER_FIRST_NAME) {
                result.addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FIELD_TOO_LONG, "FIRST NAME",
                        ControlPanelConstants.MAX_LENGTH_USER_FIRST_NAME)));
                continue;
            }

            if (lastName.length() > ControlPanelConstants.MAX_LENGTH_USER_LAST_NAME) {
                result.addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FIELD_TOO_LONG, "LAST NAME",
                        ControlPanelConstants.MAX_LENGTH_USER_LAST_NAME)));
                continue;
            }

            logger.debug("Contributer: email=[" + email +"] firstName=[" + firstName + "] role=[" + role.getName() + "]");
            result.addUser(new CsvContributor(email, firstName, lastName, role.getId()));
        }       
        reader.close();

        if (result.getErrorCount() == 0) {
            List<CsvContributor> users = result.getUsers();

            if (users == null || users.isEmpty()) {
                result.addError(user.message(ControlPanelMessages.USER_IMPORT__NO_USERS));
            }
        }

        return result;
    }

    private String formatError(String error) {
        return "Line " + reader.getLineNumber() + ": " + error;
    }
        
}
