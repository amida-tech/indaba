/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.common.Messages;
import com.ocs.indaba.common.ContentType;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.service.JournalService;
import com.ocs.indaba.service.JournalVersionService;
import com.ocs.indaba.util.AttachmentFilePathUtil;
import com.ocs.indaba.util.FileStorageUtil;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.JournalAttachmentVersionView;
import com.ocs.util.StringUtils;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login Action. To help handle the user login request. <br/>
 * 
 * Actually, although struts already provides an useful LoginForm component, in
 * this example we don't plan to leverage it. In a general use, we directly get
 * the request parameters fromHttpServletRequest.
 * 
 * @author Jeff
 * 
 */
public class AttachmentAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(AttachmentAction.class);
    private static final int BLOCK_SIZE = 4096;
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_ATTACHMENT_ID = "attachid";
    private static final String ACTION_DOWNLOAD = "download";
    private JournalService journalService = null;
    protected JournalVersionService jouralVersionService = null;
    private String action = null;
    private int attachid = -1;
    private int preVersion = -1;
    private int horseId = -1;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves

        action = request.getParameter(PARAM_ACTION);
        attachid = StringUtils.str2int(request.getParameter(PARAM_ATTACHMENT_ID), Constants.INVALID_INT_ID);
        logger.debug("Attachment action: " + action + ", attachment_id: " + attachid);

        if (ACTION_DOWNLOAD.equals(action)) {
            return downloadAttachment(mapping, request, response);
        } else {
            // not allowed
            String errMsg = (action == null || action.trim().length() == 0)
                    ? getMessage(Messages.KEY_ERR_INVALID_PARAMETER, "action")
                    : getMessage(Messages.KEY_ERR_INVALID_UNRECONGNIZED, action);
            writeMsg(response, "ERROR\n" + errMsg);
            logger.error(errMsg);
            return null;
        }
    }

    private ActionForward downloadAttachment(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String attachFilename = "";
        int cntHdrId = 0;
        if (preVersion == 1) {// get journal attachment previous version
            JournalAttachmentVersionView attachVersionView = jouralVersionService.getJournalAttachmentVersionByAttachVersionId(attachid);
            if (attachVersionView != null) {
                attachFilename = attachVersionView.getName();
                cntHdrId = attachVersionView.getContentHeaderId();
            }
        } else {
            Attachment attachment = journalService.getAttachmentById(attachid);
            if (attachment != null) {
                attachFilename = attachment.getName();
                cntHdrId = attachment.getContentHeaderId();
            }
        }

        ContentHeader cntHdr = journalService.getContentHeaderById(cntHdrId);
        if (cntHdr != null) {
            horseId = cntHdr.getHorseId();
        }

        //String filename = attachment.getFilePath();
        String attachFilePath = AttachmentFilePathUtil.getContentAttachmentPath(horseId, attachFilename);
        logger.debug("Read attachment file: " + attachFilePath);
        File file = new File(attachFilePath);

        if (StringUtils.isEmpty(attachFilename) || horseId == 0 || !file.exists()) {
            request.setAttribute(ATTR_ERR_MSG, getMessage(Messages.KEY_ERR_ATTACHMENT_MISSED));
            return mapping.findForward(FWD_ERROR);
        }

        FileInputStream fis = new FileInputStream(file);
        InputStream bufIn = new BufferedInputStream(fis);
        response.setContentType(ContentType.getContentType(FileStorageUtil.getSuffix(attachFilePath)));
        response.setHeader("Content-Length", file.length() + "");
        response.setHeader("Content-Disposition", "attachment;filename=" + attachFilename);
        OutputStream bufOut = new BufferedOutputStream(response.getOutputStream());
        byte[] block = new byte[BLOCK_SIZE];
        int n = -1;
        while ((n = bufIn.read(block, 0, BLOCK_SIZE)) != -1) {
            bufOut.write(block, 0, n);
        }
        bufOut.flush();
        bufOut.close();
        
        // CLOSEFILE
        fis.close();
        bufIn.close();
        return null;
    }

    @Autowired
    public void setJournalService(JournalService journalService) {
        this.journalService = journalService;
    }

    @Autowired
    public void setJournalVersionService(JournalVersionService jouralVersionService) {
        this.jouralVersionService = jouralVersionService;
    }
}
