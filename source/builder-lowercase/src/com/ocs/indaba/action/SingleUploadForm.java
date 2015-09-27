/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author lukeshi
 */
public class SingleUploadForm extends org.apache.struts.action.ActionForm {

    private FormFile file = null;

    public void setFile(FormFile formFile) {
        this.file = formFile;
    }

    public FormFile getFile() {
        return file;
    }

    /**
     *
     */
    public SingleUploadForm() {
        super();
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        /*if (getName() == null || getName().length() < 1) {
        errors.add("name", new ActionMessage("error.name.required"));
        }*/
        return errors;
    }
}
