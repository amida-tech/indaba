package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.MessageDAO;
import com.ocs.indaba.po.Message;
import com.ocs.indaba.vo.MessageVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DiscussionBoardService {

    private MessageDAO messageDao;
    private ViewPermissionService viewPermissionService;
    private AccessPermissionService accessPermissionService;

    @Autowired
    public void setMessageDao(MessageDAO messageDao) {
        this.messageDao = messageDao;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }
    
    @Autowired
    public void setAccessPermissionService(
            AccessPermissionService accessPermissionService) {
        this.accessPermissionService = accessPermissionService;
    }

    public List<MessageVO> loadAll(int prjid, int uid, int msgboardId, int authorId, String sortColumn, String sortOrder) {
        final List<Message> list = messageDao.getAllMessagesByMessageBoardId(msgboardId, authorId, sortColumn, sortOrder);
        List<MessageVO> result = new ArrayList<MessageVO>();

        if (list == null) return result;
        
        for (Message m : list) {
            MessageVO mvo = new MessageVO(m);
            mvo.setAuthor(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, m.getAuthorUserId()));
            result.add(mvo);
        }

        return result;
    }

    public List<MessageVO> loadActiveOnly(int prjid, int uid, int msgboardId, int authorId, String sortColumn, String sortOrder) {
        final List<Message> list = messageDao.getActiveMessagesByMessageBoardId(msgboardId, authorId, sortColumn, sortOrder);

        List<MessageVO> result = new ArrayList<MessageVO>();

        if (list == null) return result;

        for (Message m : list) {
            MessageVO mvo = new MessageVO(m);
            mvo.setAuthor(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, m.getAuthorUserId()));
            result.add(mvo);
        }

        return result;
    }

    public void add(int uid, int msgboardId, String title, String body) {
        Message msg = new Message();

        msg.setTitle(title);
        msg.setBody(body);
        msg.setCreatedTime(new Date());
        msg.setAuthorUserId(uid);
        msg.setMsgboardId(msgboardId);

        messageDao.create(msg);
    }

    public void enhance(int uid, int messageId, String enhanceTitle, String enhanceBody, boolean publishable) {
        Message msg = messageDao.get(messageId);

        if (msg == null) {
            // TODO - should throw out an exception
            return;
        }

        // TODO - check permission


        msg.setEnhancerUserId(uid);
        msg.setEnhanceTitle(enhanceTitle);
        msg.setEnhanceBody(enhanceBody);
        msg.setPublishable(publishable);
        msg.setEnhanceTime(new Date());

        messageDao.update(msg);
    }
    
    public void deleteMessage(int msgId, int userId) {
        messageDao.deleteMessage(msgId, userId);
    }
    public void undeleteMessage(int msgId) {
        messageDao.undeleteMessage(msgId);
    }
}
