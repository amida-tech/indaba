/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.google.gson.Gson;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.CaseService;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class FileUploadAction extends BaseAction {
    private static final String ATTR_CASEID = "caseid";
    private CaseService caseService;

    private static final Logger logger = Logger.getLogger(FileUploadAction.class);


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        //Map<String, String> retVal = new HashMap<String, String>();
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");

        logger.debug("Got uploaded file.");

        try {
            List<FileItem> items = upload.parseRequest(request);
            if (items != null) {
                Iterator itr = items.iterator();
                while (itr.hasNext()) {
                    FileItem item = (FileItem)itr.next();
                    if (!item.isFormField()) {
                        String uidStr = request.getParameter(Constants.ATTR_USERID);
                        int userId = StringUtils.str2int(uidStr, 0);
                        String tempFilePath = caseService.getAttachmentTempPath(userId, item.getName());
                        File file = new File(tempFilePath);
                        file.getParentFile().mkdirs();
                        item.write(file);

                        String caseIdStr = request.getParameter(ATTR_CASEID);
                        int caseAttachmentId = -1;
                        if (caseIdStr != null && caseIdStr.trim().length() > 0) {    // check if exists
                            caseAttachmentId = caseService.getAttachmentByCaseIdAndFileName(
                                    Integer.parseInt(caseIdStr), item.getName());
                            if (caseAttachmentId > -1) {
                                response.getWriter().write(new Gson().toJson(caseAttachmentId));
                            }
                        } else {
                            response.getWriter().write(new Gson().toJson(0));
                        }
                    }
                }
            }
        } catch (Exception e) {
            response.getWriter().write(new Gson().toJson(e.getMessage()));
        } finally {
            return null;
        }
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
