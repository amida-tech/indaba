/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.common.ContentType;
import com.ocs.indaba.service.CaseService;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class DownloadFileAction extends BaseAction {

    private CaseService caseService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        preprocess(mapping, request, response);

        String type = request.getParameter("type");

        if (type.equals("caseAttachment")){
            int id = Integer.parseInt(request.getParameter("fileId"));
            String filePath = caseService.getAttachmentsFilePathById(id);
            int index = filePath.lastIndexOf("\\")+1;
            if (index == 0) {
                index = filePath.lastIndexOf("/")+1;
            }
            String fileName = filePath.substring(index);
            response.addHeader("Content-Disposition", "attachment;filename=\""+fileName+"\"");
            String suffix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
            response.setContentType(ContentType.getContentType(suffix));

            OutputStream os = null;
            FileInputStream fis = null;
            try{
                os=response.getOutputStream();
                fis =new FileInputStream(filePath);
                byte[]b=new byte[1024];
                int i = 0;
                while ((i = fis.read(b)) > 0 ){
                      os.write(b, 0, i);
                }
                os.flush();
            }
            catch(Exception e ) {

            }
        }
        return null;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
