/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.JournalVersionService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.JournalVersionView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff Jiang
 */
public class JournalContentVersionAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(JournalContentVersionAction.class);
    private static final String ATTR_JOURNAL_CONTENT_VERSION = "journalContentVersion";
    private JournalVersionService jouralVersionService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        preprocess(mapping, request, response);

        int contentVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_VERSION_ID), Constants.INVALID_INT_ID);

        JournalVersionView journalContentVersion = jouralVersionService.getJournalContentVersion(contentVersionId);
        request.setAttribute(ATTR_JOURNAL_CONTENT_VERSION, journalContentVersion);
        request.setAttribute("attachments", journalContentVersion.getJournalAttachmentVersions());

        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setJournalVersionService(JournalVersionService jouralVersionService) {
        this.jouralVersionService = jouralVersionService;
    }
}
