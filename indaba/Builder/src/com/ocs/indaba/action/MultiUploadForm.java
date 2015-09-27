/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author lukeshi
 */
public class MultiUploadForm extends org.apache.struts.action.ActionForm {

    private List<FormFile> fileList = new ArrayList<FormFile>();

    public void setFile(int i, FormFile file) {
        fileList.add(file);
    }

    public List<FormFile> getFiles() {
        return fileList;
    }

    /**
     *
     */
    public MultiUploadForm() {
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
