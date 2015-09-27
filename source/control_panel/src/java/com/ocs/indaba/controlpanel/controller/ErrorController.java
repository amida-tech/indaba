/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import org.apache.log4j.Logger;

/**
 * 
 * @author Jeff Jiang
 *
 */
public class ErrorController extends BaseController {

    private static final long serialVersionUID = -2487852558172383390L;
    private static final Logger logger = Logger.getLogger(ErrorController.class);

    public String index() {
        return RESULT_INDEX;
    }

    public String execute() {
        return index();
    }

    public String get() {
        return index();
    }
}
