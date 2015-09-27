/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import com.ocs.indaba.util.DefaultPageImpl;
import com.ocs.indaba.util.Page;
import com.ocs.indaba.vo.MessageVO;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.indaba.vo.ProjectUserView;
import com.ocs.indaba.vo.UserDisplay;
import java.text.MessageFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Tiger Tang
 */
public class MessageService {

    private static final Logger logger = Logger.getLogger(MessageService.class);
    private MessageDAO messageDao;
    private MessageboardDAO messageboardDao;
    private MsgReadingStatusDAO msgReadingStatusDao;
    private UserDAO userDao;
    private ProjectDAO projectDao;
    private ViewPermissionService viewPermissionService;
    private NotificationItemService notificationItemService;
    private MailbatchService mailbatchService = null;

    @Autowired
    public void setMessageDao(MessageDAO messageDao) {
        this.messageDao = messageDao;
    }

    @Autowired
    public void setMessageboardDao(MessageboardDAO messageboardDao) {
        this.messageboardDao = messageboardDao;
    }

    @Autowired
    public void setMsgReadingStatusDao(MsgReadingStatusDAO msgReadingStatusDAO) {
        this.msgReadingStatusDao = msgReadingStatusDAO;
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }

    @Autowired
    public void setNotificationItemService(NotificationItemService notificationItemService) {
        this.notificationItemService = notificationItemService;
    }

    @Autowired
    public void setMailbatchService(MailbatchService mailbatchService) {
        this.mailbatchService = mailbatchService;
    }

    public Page<MessageVO> getInboxMessagesOfUserMsgboard(int prjid, int uid, int pageNumber, int pageSize) {
        logger.debug("Get messages by msgboard_id: [uid=" + uid
                + ", pageNumber=" + pageNumber
                + ", pageSize=" + pageSize + "]");

        User user = userDao.get(uid);

        final Page<Message> messages = messageDao.getInboxMessageByMsgboard(user.getMsgboardId(), pageNumber, pageSize);

        return new DefaultPageImpl<MessageVO>(pageNumber, pageSize, messages.getTotalNumberOfElements(),
                assembleMessageVO(messages.getThisPageElements(), prjid, uid));
    }

    public Page<MessageVO> getOutboxMessagesByUserId(int prjid, int uid, int pageNumber, int pageSize) {
        logger.debug("Get messages by msgboard_id: [uid=" + uid
                + ", pageNumber=" + pageNumber
                + ", pageSize=" + pageSize + "]");

        final Page<Message> messages = messageDao.getOutboxMessageByUserId(uid, pageNumber, pageSize);


        return new DefaultPageImpl<MessageVO>(pageNumber, pageSize, messages.getTotalNumberOfElements(),
                assembleMessageVO(messages.getThisPageElements(), prjid, uid, true));
    }

    private List<MessageVO> assembleMessageVO(List<Message> messages, int prjid, int uid) {
        return assembleMessageVO(messages, prjid, uid, false);
    }


    private MessageVO assembleMessageVO(Message msg, int prjid, int subjectUid, boolean fillMsgTo) {
        MessageVO messageVO = new MessageVO(msg);

        if (msg.getAuthorUserId() > 0) {
            messageVO.setAuthor(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, subjectUid, msg.getAuthorUserId()));
        } else { // System generated message
            UserDisplay author = new UserDisplay();
            author.setPermission(Constants.VIEW_PERMISSION_NONE);
            author.setDisplayUsername(Constants.SYSTEM_ADMIN_DISPLAY_NAME);
            messageVO.setAuthor(author);
        }
        messageVO.setReadStatus(msgReadingStatusDao.getMsgReadingStatus(msg.getId(), subjectUid) != null);

        if (fillMsgTo) {
            User user = userDao.selectUserByMsgboradId(msg.getMsgboardId());
            if (user != null) {
                UserDisplay userDisplay = viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, subjectUid, user.getId());
                messageVO.setToUser(userDisplay);
                messageVO.setIsToUser(true);
            } else {
                Project project = projectDao.selectProjectByMsgboradId(msg.getMsgboardId());
                messageVO.setToProject(project);
                messageVO.setIsToUser(false);
            }
        }

        return messageVO;
    }


    private List<MessageVO> assembleMessageVO(List<Message> messages, int prjid, int uid, boolean fillMsgTo) {
        List<MessageVO> messageVOs = new ArrayList<MessageVO>();

        if (messages != null && !messages.isEmpty()) {
            for (Message m : messages) {
                MessageVO messageVO = assembleMessageVO(m, prjid, uid, fillMsgTo);
                messageVOs.add(messageVO);
            }
        }
        return messageVOs;
    }

    public MessageVO viewMessageDetail(int prjid, int uid, int messageId) {
        logger.debug("Get messages by msg_id: [uid=" + uid + ", msg_id=" + messageId + "]");

        final Message message = messageDao.get(messageId);

        if (msgReadingStatusDao.getMsgReadingStatus(messageId, uid) == null) {
            final MsgReadingStatus mrs = new MsgReadingStatus();
            mrs.setMessageId(messageId);
            mrs.setUserId(uid);
            mrs.setTimestamp(new Date());
            msgReadingStatusDao.create(mrs);
        }

        final MessageVO messageVO = assembleMessageVO(message, prjid, uid, true);

        return messageVO;
    }

    public Integer getPreviousMsgId(Message msg, int msgboardId) {
        return messageDao.getPreviousMsgId(msg, msgboardId);
    }

    public Integer getNextMsgId(Message msg, int msgboardId) {
        return messageDao.getNextMsgId(msg, msgboardId);
    }

    public Integer getPreviousOutMsgId(Message msg, int userId) {
        return messageDao.getPreviousOutMsgId(msg, userId);
    }

    public Integer getNextOutMsgId(Message msg, int userId) {
        return messageDao.getNextOutMsgId(msg, userId);
    }

    public long getInboxNewMessageCountByUserId(int uid) {
        final User user = userDao.get(uid);
        if (user.getMsgboardId() == null || user.getMsgboardId() <= 0) {
            return 0L;
        }
        return messageDao.getInboxNewMessageCountByMsgboard(uid, user.getMsgboardId());
    }

    public long getOutboxNewMessageCountOfUser(int uid) {
        return messageDao.getOutboxNewMessageCountByUserId(uid);
    }

    public void newMessage(int senderId, Message message,
            int projectId, int[] receiverIds, int[] roleIds,
            int[] teamIds, boolean copyToProjectWall) {
        message.setCreatedTime(new Date());
        message.setAuthorUserId(senderId);

        Set<Integer> receiveUserIds = new HashSet<Integer>();
        Set<Integer> receiveMsgboardIds = new HashSet<Integer>();

        if (receiverIds != null) {
            for (int receiverId : receiverIds) {
                receiveUserIds.add(receiverId);
            }
        }

        if (roleIds != null) {
            List<Integer> userIds = userDao.getUserIdsByProjectAndRoles(projectId, roleIds);

            if (userIds != null) {
                for (int userId : userIds) {
                    if (viewPermissionService.getUserDisplayOfProject(projectId, Constants.DEFAULT_VIEW_MATRIX_ID, senderId, userId).getPermission() > 0) {
                        receiveUserIds.add(userId);
                    }
                }
            }
        }

        if (teamIds != null) {
            List<Integer> userIds = userDao.getUserIdsByProjectAndTeams(projectId, teamIds);
            if (userIds != null) {
                for (int userId : userIds) {
                    if (viewPermissionService.getUserDisplayOfProject(projectId, Constants.DEFAULT_VIEW_MATRIX_ID, senderId, userId).getPermission() > 0) {
                        receiveUserIds.add(userId);
                    }
                }
            }
        }

        for (int receiverId : receiveUserIds) {
            User u = userDao.get(receiverId);
            if (u == null) {
                continue;
            }

            if (u.getMsgboardId() == null || u.getMsgboardId() == 0) {
                Msgboard msgboard = new Msgboard();

                msgboard.setCreateTime(new Date());

                messageboardDao.create(msgboard);

                u.setMsgboardId(msgboard.getId());

                userDao.update(u);

                logger.debug("user_default_project_id: " + u.getDefaultProjectId());

                receiveMsgboardIds.add(msgboard.getId());
            } else {
                receiveMsgboardIds.add(u.getMsgboardId());
            }
        }

        if (copyToProjectWall) {
            final Project project = projectDao.get(projectId);

            if (project != null) {

                if (project.getMsgboardId() == null || project.getMsgboardId() == 0) {
                    Msgboard msgboard = new Msgboard();

                    msgboard.setCreateTime(new Date());

                    messageboardDao.create(msgboard);

                    project.setMsgboardId(msgboard.getId());

                    projectDao.update(project);
                }
                receiveMsgboardIds.add(project.getMsgboardId());
            }
        }

        for (Integer msgboardId : receiveMsgboardIds) {
            message.setId(null);//david, create need id to null
            message.setMsgboardId(msgboardId);
            messageDao.create(message);
        }

        try {
            for (Integer receiverId : receiveUserIds) {
                ProjectUserView u = userDao.selectProjectUser(projectId, receiverId);
                if (u.getForwardInboxMsg()) {                    
                    final UserDisplay userDisplay = viewPermissionService.getUserDisplayOfProject(projectId, Constants.DEFAULT_VIEW_MATRIX_ID, receiverId, senderId);
                    final String senderDisplayUsername = userDisplay.getDisplayUsername();
                    String receiverName = u.getFirstName() + ' ' + u.getLastName();

                    if (u.getEmailDetailLevel() == Constants.TARGET_TYPE_ALERT_ONLY) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username", receiverName);
                        NotificationView notification = notificationItemService.getProjectNotificationView(Constants.NOTIFICATION_TYPE_SYS_ALERT_SITE_MSG, projectId, u.getRoleId(), u.getLanguageId(), map);

                        mailbatchService.addSystemMail(
                                u.getEmail(),
                                notification.getSubject(),
                                notification.getBody());
                    } else if (u.getEmailDetailLevel() == Constants.TARGET_TYPE_FULL_MESSAAGE) {                   
                        NotificationItem notification = notificationItemService.getNotificationItemByNameAndLanguage(Constants.NOTIFICATION_TYPE_NOTIFY_SITE_MESSAGE, u.getLanguageId());

                        if (notification == null && u.getLanguageId() != Constants.LANG_EN) { // if no language text defined, use English text by default.
                            logger.warn("No language text defined('" + Constants.NOTIFICATION_TYPE_NOTIFY_SITE_MESSAGE + "'), use English text.");
                            notification = notificationItemService.getNotificationItemByNameAndLanguage(Constants.NOTIFICATION_TYPE_NOTIFY_SITE_MESSAGE, Constants.LANG_EN);
                        }
                        String msgBody = MessageFormat.format(notification.getBodyText(), senderDisplayUsername, u.getFirstName(), u.getLastName(), message.getTitle(), message.getBody());
                        mailbatchService.addSystemMail(
                                u.getEmail(),
                                MessageFormat.format(notification.getSubjectText(), message.getTitle()),
                                msgBody);
                    }
                }
            }
        } catch (Exception e) {
            // No email server configured
            logger.error("Fail to send site message.", e);
        }
    }

    public MessageVO getMessage(int prjid, int uid, Integer msgId) {
        final Message m = messageDao.get(msgId);
        final MessageVO messageVO = new MessageVO(m);

        if (m.getAuthorUserId() > 0) {
            messageVO.setAuthor(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, m.getAuthorUserId()));
        } else { // System generated message
            UserDisplay author = new UserDisplay();
            author.setPermission(Constants.VIEW_PERMISSION_NONE);
            author.setDisplayUsername(Constants.SYSTEM_ADMIN_DISPLAY_NAME);
            messageVO.setAuthor(author);
        }

        return messageVO;
    }

    public Long getNewMessageCountOfProject(int uid, int prjid) {
        Project project = projectDao.get(prjid);

        if (project == null || project.getMsgboardId() == 0) {
            return 0L;
        }

        return messageDao.getInboxNewMessageCountByMsgboard(uid, project.getMsgboardId());
    }

    public String sendSysMails(String receiveName, String subject, String content, String receiveAdd) {
        mailbatchService.addSystemMail(receiveAdd, subject, content);
        return "Password sent to your email.";
    }

    @SuppressWarnings("unchecked")
    public Page<MessageVO> getMessagesOfProjectMsgboard(int prjid, int uid, int pageNumber, int pageSize) {
        Project project = projectDao.get(prjid);
        if (project == null) {
            return null;
        }

        if (project.getMsgboardId() == 0) {
            Msgboard board = new Msgboard();
            board.setCreateTime(new Date());
            messageboardDao.create(board);

            project.setMsgboardId(board.getId());
            projectDao.update(project);
        }

        final Page<Message> messages = messageDao.getInboxProjectMessageByMsgboard(project.getMsgboardId(), pageNumber, pageSize);

        return new DefaultPageImpl<MessageVO>(pageNumber, pageSize, messages.getTotalNumberOfElements(),
                assembleMessageVO(messages.getThisPageElements(), prjid, uid));
    }

    public long getInboxMessageCountByUserId(int uid) {
        User u = userDao.get(uid);

        if (u.getMsgboardId() == null || u.getMsgboardId() == 0) {
            return 0L;
        }

        return messageDao.getInboxMessageCountByMsgboard(u.getMsgboardId());
    }

    public long getOutboxMessageCountByUserId(int uid) {
        return messageDao.getOutboxMessageCountByUserId(uid);
    }

    public long getMessageCountOfProject(Integer prjid) {
        if (prjid == null) {
            return 0L;
        }

        Project p = projectDao.get(prjid);
        if (p == null) {
            return 0L;
        }

        if (p.getMsgboardId() == null || p.getMsgboardId() == 0) {
            return 0L;
        }

        return messageDao.getInboxMessageCountByMsgboard(p.getMsgboardId());
    }

    public List<Message> getAllMessagesByHorseId(int horseId) {
        List<Message> msgList = new ArrayList<Message>();
        List<Message> internalMsgList = messageDao.selectInternalMessagesByHorseId(horseId);
        if (internalMsgList != null && !internalMsgList.isEmpty()) {
            msgList.addAll(internalMsgList);
        }
        List<Message> staffAuthorMsgList = messageDao.selectStaffAuthorMessagesByHorseId(horseId);
        if (staffAuthorMsgList != null && !staffAuthorMsgList.isEmpty()) {
            msgList.addAll(staffAuthorMsgList);
        }
        return msgList;
    }
}
