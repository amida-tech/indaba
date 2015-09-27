/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.ContentType;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.SurveyAnswerAttachment;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.JournalService;
import com.ocs.indaba.service.JournalVersionService;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.util.FilePathUtil;
import com.ocs.indaba.util.FileStorageUtil;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AttachmentAdapter;
import com.ocs.indaba.vo.JournalAttachmentVersionView;
import com.ocs.indaba.vo.LoginUser;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.json.simple.JSONObject;
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
    private static final String ATTACH_TYPE_SURVEY = "survey";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_ATTACHMENT_ID = "attachid";
    private static final String PARAM_PREVERSION = "preversion";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_DOWNLOAD = "download";
    private static final String JSON_ATTR_RET = "ret";
    private static final String JSON_ATTR_FILE_SIZE = "fsize";
    private static final String JSON_ATTR_ATTACH_ID = "attachid";
    private static final String JSON_ATTR_DESC = "desc";
    private HorseService horseService = null;
    private JournalService journalService = null;
    protected JournalVersionService jouralVersionService = null;
    private SurveyAnswerService surveyAnswerService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves

        LoginUser loginUser = preprocess(mapping, request, response);

        String action = request.getParameter(PARAM_ACTION);

        logger.debug("Attachment form: " + form
                + ", action: " + action
                + ", type: " + request.getParameter(PARAM_TYPE)
                + ", answerid: " + request.getParameter(PARAM_ANSWER_ID)
                + ", attachment_id: " + request.getParameter(PARAM_ATTACHMENT_ID)
                + ", preversion: " + request.getParameter(PARAM_PREVERSION)
                + ", horse_id: " + request.getParameter(PARAM_HORSE_ID)
                + ", desc:" + request.getParameter(PARAM_FILE_DESC));

        if (ACTION_DELETE.equals(action)) {
            return deleteAttachment(request, response);
        } else if (ACTION_ADD.equals(action)) {
            return addAttachment(form, request, response, loginUser);
        } else if (ACTION_DOWNLOAD.equals(action)) {
            return downloadAttachment(mapping, request, response);
        } else {
            // not allowed
            String errMsg = (action == null || action.trim().length() == 0)
                    ? getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "action")
                    : getMessage(request, Messages.KEY_COMMON_ERR_INVALID_UNRECONGNIZED, action);
                    writeMsgLnUTF8(response, responseJson(ErrorCode.ERR_INVALID_PARAM, errMsg, null));
            //writeMsg(response, "ERROR\n" + errMsg);
            logger.error(errMsg);
            return null;
        }
    }

    private ActionForward addAttachment(ActionForm form, HttpServletRequest request, HttpServletResponse response, LoginUser user) throws IOException {
        Object maxLenExceeded = request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);

        if (maxLenExceeded != null && ((Boolean) maxLenExceeded).booleanValue()) {
            logger.error(getMessage(request, Messages.KEY_COMMON_ERR_EXCEED_MAX_FILESIZE));
            writeMsgLnUTF8(response, responseJson(ErrorCode.ERR_UPLOAD_EXCEEDS_FILE_SIZE, getMessage(request, Messages.KEY_COMMON_ERR_EXCEED_MAX_FILESIZE), null));
            return null;
        }
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);

        if (horseId == Constants.INVALID_INT_ID) {
            writeMsgLnUTF8(response, responseJson(ErrorCode.ERR_INVALID_PARAM, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseId"), null));
            return null;
        }
        
        String desc = request.getParameter(PARAM_FILE_DESC);

        String fileDesc = "";
        if (desc != null) {
            fileDesc = java.net.URLDecoder.decode(desc, "utf-8");
        }
        if (form != null) {
            String storedFilename = digestSurveyAnswerAttachmentFilename(((SingleUploadForm) form).getFile().getFileName(), horseId);
            FormFile formFile = extractSingleUploadFormFiles((SingleUploadForm) form, storedFilename, horseId);
            ContentHeader cntHdr = journalService.getContentHeaderByHorseId(horseId);
            Attachment attached = journalService.addAttachment(AttachmentAdapter.formFile2Attachment(formFile, storedFilename, cntHdr.getId(), fileDesc), user.getUid());

            //String filename = formFile.getFileName();
            if (ATTACH_TYPE_SURVEY.equals(request.getParameter(PARAM_TYPE))) {
                int answerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);

                if (answerId == Constants.INVALID_INT_ID) {
                    writeMsgLnUTF8(response, responseJson(ErrorCode.ERR_INVALID_PARAM, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerId"), null));
                    return null;
                }

                SurveyAnswerAttachment attachment = new SurveyAnswerAttachment();
                attachment.setAttachmentId(attached.getId());
                attachment.setSurveyAnswerId(answerId);
                surveyAnswerService.addSurveyAnswerAttachment(attachment);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(JSON_ATTR_FILE_SIZE, attached.getSize());
            map.put(JSON_ATTR_ATTACH_ID, attached.getId());
            writeMsgUTF8(response, responseJson(ErrorCode.OK, "OK", map));
            //writeMsgLn(response, "formFile");
        } else {
            logger.debug("File is empty!");
            writeMsgLnUTF8(response, responseJson(ErrorCode.ERR_UNKNOWN, "File is empty!", null));
        }
        return null;
    }

    private String responseJson(int ret, String desc, Map<String, Object> map) {
        JSONObject root = new JSONObject();
        root.put(JSON_ATTR_RET, ret);
        root.put(JSON_ATTR_DESC, desc);
        if(map != null && map.size() > 0) {
            Set<String> keys = map.keySet();
            for(String key: keys) {
                root.put(key, map.get(key));
            }
        }
        return root.toJSONString();
    }

    private ActionForward deleteAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int attachid = StringUtils.str2int(request.getParameter(PARAM_ATTACHMENT_ID), Constants.INVALID_INT_ID);
        if (ATTACH_TYPE_SURVEY.equals(request.getParameter(PARAM_TYPE))) {
            int answerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
            surveyAnswerService.deleteSurveyAnswerAttachment(attachid, answerId);
        } else {
            journalService.deleteAttachmentById(attachid);
        }
        writeMsgUTF8(response, responseJson(ErrorCode.OK, "OK", null));
        return null;
    }

    private ActionForward downloadAttachment(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String storedFilename = "";
        String originFilename = "";
        int cntHdrId = 0;
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int attachid = StringUtils.str2int(request.getParameter(PARAM_ATTACHMENT_ID), Constants.INVALID_INT_ID);
        int preVersion = StringUtils.str2int(request.getParameter(PARAM_PREVERSION), Constants.INVALID_INT_ID);

        if (preVersion == 1) {
            if (ATTACH_TYPE_SURVEY.equals(request.getParameter(PARAM_TYPE))) {
                Attachment attachment = surveyAnswerService.getSurveyAnswerAttachmentVersion(attachid);
                if (attachment != null) {
                    storedFilename = attachment.getFilePath();
                    originFilename = attachment.getName();                  
                    logger.debug("==========>>>>>>" + storedFilename + ":" + originFilename);
                }
            } else {
                // get journal attachment previous version
                JournalAttachmentVersionView attachVersionView = jouralVersionService.getJournalAttachmentVersionByAttachVersionId(attachid);
                if (attachVersionView != null) {
                    storedFilename = attachVersionView.getFilePath();
                    originFilename = attachVersionView.getName();
                    cntHdrId = attachVersionView.getContentHeaderId();
                }
            }
        } else {
            Attachment attachment = null;
            if (ATTACH_TYPE_SURVEY.equals(request.getParameter(PARAM_TYPE))) {
                attachment = surveyAnswerService.getSurveyAnswerAttachment(attachid);
            } else {
                attachment = journalService.getAttachmentById(attachid);
            }
            if (attachment != null) {
                storedFilename = attachment.getFilePath();
                originFilename = attachment.getName();
                cntHdrId = attachment.getContentHeaderId();
            }
        }

        ContentHeader cntHdr = journalService.getContentHeaderById(cntHdrId);
        if (cntHdr != null) {
            horseId = cntHdr.getHorseId();
        }

        //String filename = attachment.getFilePath();
        String attachFilePath = FilePathUtil.getContentAttachmentPath(horseId, storedFilename);
        logger.debug("Read attachment file: " + attachFilePath);
        File file = new File(attachFilePath);

        if (StringUtils.isEmpty(storedFilename) || !file.exists()) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_ATTACHMENT_MISSED));
            return mapping.findForward(FWD_ERROR);
        }

        InputStream bufIn = new BufferedInputStream(new FileInputStream(file));
        response.setContentType(ContentType.getContentType(FileStorageUtil.getSuffix(attachFilePath)));
        response.setHeader("Content-Length", file.length() + "");
        // Add double quotation marks around the file name to let it work correctly in Firefox when there are spaces in file name.
        response.setHeader("Content-Disposition", "attachment;filename=" + "\"" + originFilename + "\"");
        OutputStream bufOut = new BufferedOutputStream(response.getOutputStream());
        byte[] block = new byte[BLOCK_SIZE];
        int n = -1;
        while ((n = bufIn.read(block, 0, BLOCK_SIZE)) != -1) {
            bufOut.write(block, 0, n);
        }
        bufOut.flush();
        bufOut.close();
        bufIn.close();
        return null;
    }

    @Autowired
    public void setUserManager(JournalService journalService) {
        this.journalService = journalService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setJournalVersionService(JournalVersionService jouralVersionService) {
        this.jouralVersionService = jouralVersionService;
    }

    @Autowired
    public void setSurveyAnswerService(SurveyAnswerService surveyAnswerService) {
        this.surveyAnswerService = surveyAnswerService;
    }
}
