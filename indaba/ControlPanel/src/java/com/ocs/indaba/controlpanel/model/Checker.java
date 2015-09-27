/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.po.User;


/**
 *
 * @author seanpcheng
 */

public class Checker extends OrgAuthorizer {

    public Checker(User user) {
        super(user);
    }
}
