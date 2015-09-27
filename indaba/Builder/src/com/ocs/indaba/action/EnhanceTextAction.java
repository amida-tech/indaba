/**
 * 
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.DiscussionBoardService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Tiger Tang
 *
 */
public class EnhanceTextAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(EnhanceTextAction.class);

    private static final String PARAM_MESSAGE_ID = "msgId";
    private static final String PARAM_ENHANCE_TITLE = "enhancetitle";
    private static final String PARAM_ENHANCE_BODY = "enhancebody";
    private static final String PARAM_PUBLISHABLE = "publishable";

    private DiscussionBoardService discussionBoardService;

    @Autowired
    public void setDiscussionBoardService(DiscussionBoardService discussionBoardService) {
            this.discussionBoardService = discussionBoardService;
    }
	
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginUser loginUser = preprocess(mapping, request, response);

            logger.debug("EnhanceTextAction - Write new message request: [userid=" + loginUser.getUid() + "].");

            int messageId = StringUtils.str2int(request.getParameter(PARAM_MESSAGE_ID), Constants.INVALID_INT_ID);

            String enhanceTitle = request.getParameter(PARAM_ENHANCE_TITLE);
            String enhanceBody = request.getParameter(PARAM_ENHANCE_BODY);
            boolean publishable = "true".equalsIgnoreCase(request.getParameter(PARAM_PUBLISHABLE));
            discussionBoardService.enhance(loginUser.getUid(), messageId, enhanceTitle, enhanceBody, publishable);
            writeMsgUTF8(response, "OK");
        } catch (IOException e) {
            logger.error(e);
        }
        
        return null;
    }

}
