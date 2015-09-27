/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.dao.CTagDAO;
import com.ocs.indaba.dao.CaseAttachmentDAO;
import com.ocs.indaba.dao.CaseDAO;
import com.ocs.indaba.dao.CaseObjectDAO;
import com.ocs.indaba.dao.CaseTagDAO;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.MessageDAO;
import com.ocs.indaba.dao.MessageboardDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.dao.WorkflowDAO;
import com.ocs.indaba.dao.WorkflowObjectDAO;
import com.ocs.indaba.po.CaseAttachment;
import com.ocs.indaba.po.CaseObject;
import com.ocs.indaba.po.CaseTag;
import com.ocs.indaba.po.Cases;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Ctags;
import com.ocs.indaba.po.Message;
import com.ocs.indaba.po.Msgboard;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.User;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.vo.CaseInfo;
import com.ocs.indaba.vo.CasePriority;
import com.ocs.indaba.vo.CaseStatus;
import com.ocs.indaba.vo.MessageVO;
import com.ocs.indaba.vo.ObjectType;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.indaba.vo.ProjectUserView;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author menglong
 */
public class CaseService {

    private CaseDAO caseDao;
    private UserDAO userDAO;
    private MessageDAO messageDao;
    private WorkflowDAO workflowDAO;
    private CaseObjectDAO caseObjectDAO;
    private ContentHeaderDAO contentHeaderDAO;
    private CTagDAO ctagDAO;
    private CaseAttachmentDAO caseAttachmentDAO;
    private CaseTagDAO caseTagDAO;
    private MessageboardDAO messageboardDao;
    private ViewPermissionService viewPermissionService;
    private WorkflowObjectDAO workflowObjectDAO;
    private TaskDAO taskDao;
    private ProductDAO productDao;
    private ProjectDAO projectDao;
    private SiteMessageService siteMessageService;
    private NotificationItemService notificationItemService;
    private EventService eventService;

    public Cases getCaseById(int caseId) {
        return caseDao.selectCaseById(caseId);
    }

    public Cases getCase(int horseId, int projectId, int productId, int goalId,
            int openedUserId, int assignedUserId, String title, String description) {
        return caseDao.selectCase(horseId, projectId, productId, goalId, openedUserId, assignedUserId, title, description);
    }

    public Cases addCase(Cases caze) {
        return caseDao.create(caze);
    }

    public void addCaseObject(CaseObject caseObj) {
        caseObjectDAO.insertSingleCaseObject(caseObj);
    }
    public boolean checkCaseObjectExists(CaseObject caseObj) {
        return (0 < caseObjectDAO.selectCaseObjectExists(caseObj.getCasesId(), caseObj.getObjectType(), caseObj.getObjectId()));
    }

    public CaseInfo getCaseById(int prjid, int uid, int caseId) {
        //get po from DB;
        Cases caseTmp = caseDao.selectCaseById(caseId);

        if (caseTmp == null) {
            return null;
        }

        CaseInfo result = new CaseInfo();

        //set fields don't need trans
        result.setCaseId(caseTmp.getId());
        result.setTitle(caseTmp.getTitle());

        CaseStatus caseStatus = CaseStatus.fromCaseStatusCode(caseTmp.getStatus());
        result.setStatus(caseStatus.getStatusDesc());
        result.setStatusCode(caseStatus.getStatusCode());
        result.setPriority(CasePriority.fromCasePriorityCode(caseTmp.getPriority()).getPriorityDesc());
        result.setPriorityCode(caseTmp.getPriority());

        result.setOpenedTime(caseTmp.getOpenedTime());
        result.setDescription(caseTmp.getDescription());
        result.setBlockWorkFlow(caseTmp.getBlockWorkflow());
        result.setBlockPulishing(caseTmp.getBlockPublishing());

        result.setProjectId(caseTmp.getProjectId());
        result.setProductId(caseTmp.getProductId());
        result.setHorseId(caseTmp.getHorseId());
        result.setGoalId(caseTmp.getGoalId());

        result.setStaffMsgboardId(caseTmp.getStaffMsgboardId());
        result.setUserMsgboardId(caseTmp.getUserMsgboardId());
        result.setLastUpdateTime(caseTmp.getLastUpdatedTime());

        //set tags;
        result.setTags(ctagDAO.selectTagsByCaseId(caseTmp.getId()));

        //set fields need trans
        User openUser = userDAO.selectUserById(caseTmp.getOpenedByUserId());
        if (openUser != null) {
            result.setOpenedByUserName((openUser.getUsername()));
            result.setOpenedByUserId(openUser.getId());
            result.setOwner(openUser.getFirstName() + " " + openUser.getLastName());
        }
        User assignUser = userDAO.selectUserById(caseTmp.getAssignedUserId());
        if (assignUser != null) {
            result.setAssignedUserName(assignUser.getUsername());
        }
        result.setAssignedUserId(caseTmp.getAssignedUserId());

        //set message info;
        List<Message> userMessageList =
                messageDao.getMessagesByMessageBoardId(caseTmp.getUserMsgboardId(), -1, -1).getAllElements();
        List<MessageVO> userMessageVOList = new ArrayList<MessageVO>();

        if (userMessageList != null) {
            for (Message message : userMessageList) {
                MessageVO messageVO = new MessageVO(message);
                messageVO.setAuthor(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, message.getAuthorUserId()));
                userMessageVOList.add(messageVO);
            }
        }
        result.setUserMessageList(userMessageVOList);

        List<Message> staffMessageList =
                messageDao.getMessagesByMessageBoardId(caseTmp.getStaffMsgboardId(), -1, -1).getAllElements();
        List<MessageVO> staffMessageVOList = new ArrayList<MessageVO>();

        if (staffMessageList != null) {
            for (Message message : staffMessageList) {
                MessageVO messageVO = new MessageVO(message);
                messageVO.setAuthor(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, message.getAuthorUserId()));
                staffMessageVOList.add(messageVO);
            }
        }
        result.setStaffMessageList(staffMessageVOList);

        //set attach user list;
        List<CaseObject> caseObjectOfUserList = caseObjectDAO.selectCaseObjectByCaseIdAndObjectType(
                caseTmp.getId(), ObjectType.USER.getTypeCode());
        List<User> userList = new ArrayList<User>();

        if (caseObjectOfUserList != null) {
            for (CaseObject caseObject : caseObjectOfUserList) {
                User user = userDAO.selectUserById(caseObject.getObjectId());
                userList.add(user);
            }
        }
        result.setAttachUsers(userList);

        //set attach content info;
//        result.setAttachContentIds(getContentIdListByCaseId(caseTmp.getId()));
//        result.setAttachContentTitles(getContentTitleListByCaseId(caseTmp.getId()));
        setContentListByCaseId(caseTmp.getId(), result);

        //set attach file info;
        result.setAttachFiles(caseAttachmentDAO.selectAttachmentsByCaseId(caseTmp.getId()));

        return result;

    }

    private void setContentListByCaseId(int caseId, CaseInfo caseInfo) {
        List<CaseObject> caseObjectOfContentList = caseObjectDAO.selectCaseObjectByCaseIdAndObjectType(
                caseId, ObjectType.CONTENT.getTypeCode());
        List<Long> attachContentIdList = new ArrayList<Long>();
        List<String> attachContentTitleList = new ArrayList<String>();

        if (caseObjectOfContentList != null) {
            for (CaseObject caseObject : caseObjectOfContentList) {
                ContentHeader contentHeader = contentHeaderDAO.selectContentHeaderById(caseObject.getObjectId());
                if (contentHeader != null) {
                    attachContentIdList.add((long) contentHeader.getId());
                    attachContentTitleList.add(contentHeader.getTitle());
                }
            }
        }
        caseInfo.setAttachContentIds(attachContentIdList);
        caseInfo.setAttachContentTitles(attachContentTitleList);
        caseInfo.setAttachContents(getContentListByCaseId(caseId));
    }

    private List<ContentHeader> getContentListByCaseId(int caseId) {
        List<CaseObject> caseObjectOfContentList = caseObjectDAO.selectCaseObjectByCaseIdAndObjectType(
                caseId, ObjectType.CONTENT.getTypeCode());
        List<ContentHeader> attachContentList = new ArrayList<ContentHeader>();

        if (caseObjectOfContentList != null) {
            for (CaseObject caseObject : caseObjectOfContentList) {
                ContentHeader contentHeader = contentHeaderDAO.selectContentHeaderById(caseObject.getObjectId());
                if (contentHeader != null) {
                    attachContentList.add(contentHeader);
                }
            }
        }
        return attachContentList;
    }

    private List<String> getContentTitleListByCaseId(int caseId) {
        List<CaseObject> caseObjectOfContentList = caseObjectDAO.selectCaseObjectByCaseIdAndObjectType(
                caseId, ObjectType.CONTENT.getTypeCode());
        List<String> attachContentTitleList = new ArrayList<String>();

        if (caseObjectOfContentList != null) {
            for (CaseObject caseObject : caseObjectOfContentList) {
                ContentHeader contentHeader = contentHeaderDAO.selectContentHeaderById(caseObject.getObjectId());
                if (contentHeader != null) {
                    String contentTitle = contentHeader.getTitle();
                    attachContentTitleList.add(contentTitle);
                }
            }
        }
        return attachContentTitleList;
    }

    private List<ContentHeader> getContentListByFilter(int caseId,
            List<Integer> targetFilter, List<Integer> productFilter) {

        return contentHeaderDAO.selectContentHeaderByFilter(caseId,
                targetFilter, productFilter);
    }

    private List<String> getContentTitleListByFilter(int caseId,
            List<Integer> targetFilter, List<Integer> productFilter) {

        return contentHeaderDAO.selectContentHeaderTitleByFilter(caseId,
                targetFilter, productFilter);
    }

    public List<CaseInfo> getCaseListByUserIdAndProjectId(int userId, int projectId) {
        List<Cases> caseList = caseDao.selectCasesByAssociatedUserIdAndProjectId(userId, projectId);
        return convertToCaseInfo(caseList);
    }

    public List<CaseInfo> getOpenCaseListByUserIdAndProjectId(int userId, int projectId) {
        List<Cases> caseList = caseDao.selectOpenCasesByUserIdAndProjectId(userId, projectId);
        return convertToCaseInfo(caseList);
    }

    public List<CaseInfo> getOpenCasesByAssignUserId(int userId) {
        List<Cases> caseList = caseDao.selectOpenCasesByAssignUserId(userId);
        return convertToCaseInfo(caseList);
    }

    public List<CaseInfo> getAllCasesByProjectId(int projectId) {
        List<Cases> caseList = caseDao.selectAllCasesByProjectId(projectId);
        return convertToCaseInfo(caseList);
    }

    public Long createCase(CaseInfo caseInfo) {
        //insert table cases;
        Long caseId = caseDao.insertCase(caseInfo);
        if (caseId <= 0) {
            return caseId;
        }
        //insert table case_tag;
        List<CaseTag> caseTagList = new ArrayList<CaseTag>();
        List<Ctags> ctagList = caseInfo.getTags();

        if (ctagList != null) {
            for (Ctags ctag : ctagList) {
                CaseTag caseTag = new CaseTag();
                caseTag.setCasesId(caseId.intValue());
                caseTag.setCtagsId(ctag.getId());
                caseTagList.add(caseTag);
            }
            caseTagDAO.insertCaseTags(caseTagList);
        }

        //insert table case_object;
        List<CaseObject> caseObjectList = new ArrayList<CaseObject>();
        // add user object;
        if (caseInfo.getAttachUsers() != null) {
            for (User user : caseInfo.getAttachUsers()) {
                CaseObject caseObject = new CaseObject();
                caseObject.setCasesId(caseId.intValue());
                caseObject.setObjectType(ObjectType.USER.getTypeCode());
                caseObject.setObjectId(user.getId());
                caseObjectList.add(caseObject);
            }
        }

        if (caseInfo.getAttachContentIds() != null) {
            for (Long contentHeadId : caseInfo.getAttachContentIds()) {
                CaseObject caseObject = new CaseObject();
                caseObject.setCasesId(caseId.intValue());
                caseObject.setObjectType(ObjectType.CONTENT.getTypeCode());
                caseObject.setObjectId(contentHeadId.intValue());
                caseObjectList.add(caseObject);

                // change workflow status
                workflowObjectDAO.updateWorkflowObjectByContentHeaderId(contentHeadId.intValue(), caseInfo.getBlockWorkFlow());
            }
        }
        // add content object;
        caseObjectDAO.insertCaseObject(caseObjectList);



        //insert table case_attachment;
        String srcPath, destPath;
        String uploadBase = Config.getString(Config.KEY_STORAGE_UPLOAD_BASE);
        String pathFormat = Config.getString(Config.KEY_STORAGE_PATH_FORMAT);

        if (caseInfo.getAttachFiles() != null) {
            for (CaseAttachment caseAttachment : caseInfo.getAttachFiles()) {
                caseAttachment.setCasesId(caseId.intValue());


                srcPath = MessageFormat.format(pathFormat, uploadBase + "/caseattachment",
                        "temp", caseInfo.getOpenedByUserId(), caseAttachment.getFileName());

                destPath = uploadBase + "/caseattachment/" + caseId + "/" + caseAttachment.getFileName();
                
                caseAttachment.setFilePath(destPath.substring(uploadBase.length()));

                File srcFile = new File(srcPath);
                File destFile = new File(destPath);
                destFile.getParentFile().mkdirs();
                srcFile.renameTo(destFile);

                caseAttachmentDAO.insertCaseAttachment(caseAttachment);
            }
        }
        // TODO: clean up temp dir


        //return case id;
        return caseId;
    }

    public int updateCase(CaseInfo caseInfo) {
        //update attach user;
        //delete all old attach user;
        if (caseInfo.getAttachUsers() != null) {
            List<CaseObject> caseObjectList = new ArrayList<CaseObject>();
            caseObjectDAO.deleteCaseObjectByCaseIdAndObjectType(caseInfo.getCaseId(),
                    ObjectType.USER.getTypeCode());
            for (User user : caseInfo.getAttachUsers()) {
                CaseObject caseObject = new CaseObject();
                caseObject.setCasesId(caseInfo.getCaseId());
                caseObject.setObjectType(ObjectType.USER.getTypeCode());
                caseObject.setObjectId(user.getId());
                caseObjectList.add(caseObject);
            }
            caseObjectDAO.insertCaseObject(caseObjectList);
        }

        if (caseInfo.getAttachContentIds() != null) {
            List<CaseObject> caseObjectList = new ArrayList<CaseObject>();
            workflowObjectDAO.resetWorkflowObjectByCaseId(caseInfo.getCaseId());
            caseObjectDAO.deleteCaseObjectByCaseIdAndObjectType(caseInfo.getCaseId(),
                    ObjectType.CONTENT.getTypeCode());
            for (Long contentHeadId : caseInfo.getAttachContentIds()) {
                CaseObject caseObject = new CaseObject();
                caseObject.setCasesId(caseInfo.getCaseId());
                caseObject.setObjectType(ObjectType.CONTENT.getTypeCode());
                caseObject.setObjectId(contentHeadId.intValue());
                caseObjectList.add(caseObject);

                // change workflow status
                workflowObjectDAO.updateWorkflowObjectByContentHeaderId(contentHeadId.intValue(), caseInfo.getBlockWorkFlow());
            }
            caseObjectDAO.insertCaseObject(caseObjectList);
        }

        if (caseInfo.getTags() != null) {
            //update attach tags;
            //delete all tags
            caseTagDAO.deleteCaseTagsByCaseId(caseInfo.getCaseId());
            //insert new tags
            List<CaseTag> caseTagList = new ArrayList<CaseTag>();
            for (Ctags ctag : caseInfo.getTags()) {
                CaseTag caseTag = new CaseTag();
                caseTag.setCasesId(caseInfo.getCaseId());
                caseTag.setCtagsId(ctag.getId());
                caseTagList.add(caseTag);
            }
            caseTagDAO.insertCaseTags(caseTagList);
        }

        //insert new attach files into table case_attachment;
        if (caseInfo.getAttachFiles() != null) {
            for (CaseAttachment caseAttachment : caseInfo.getAttachFiles()) {
                caseAttachmentDAO.insertCaseAttachment(caseAttachment);
            }
        }

        //update case info
        return caseDao.updateCase(caseInfo);

    }

    public List<CaseInfo> getCasesByHorseId(Integer horseId) {
        List<Cases> caseList = caseDao.selectCasesByHorseId(horseId);
        return convertToCaseInfo(caseList);
    }

    public List<CaseInfo> searchCasesByFilter(List<Integer> targetFilter,
            List<Integer> productFilter, List<Integer> statusFilter,
            List<Integer> cTagFilter, int projectId) {

        return convertToCaseInfo(caseDao.searchCasesByFilter(projectId, statusFilter),
                targetFilter, productFilter, cTagFilter);
    }

    public List<CaseInfo> convertToCaseInfo(List<Cases> caseList,
            List<Integer> targetFilter, List<Integer> productFilter,
            List<Integer> cTagFilter) {
        if (caseList == null) {
            return null;
        }
        List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();
        List<String> contentTitleList;
        List<ContentHeader> contentHeaderList;
        List<Ctags> ctagList;
        CaseInfo caseInfo;

        if (caseList != null) {
            for (Cases c : caseList) {
                caseInfo = new CaseInfo();
                contentHeaderList = getContentListByFilter(c.getId(),
                        targetFilter, productFilter);
                contentTitleList = getContentTitleListByFilter(c.getId(),
                        targetFilter, productFilter);
                if (contentTitleList == null) {
                    continue;
                } else {
                    caseInfo.setAttachContents(contentHeaderList);
                    caseInfo.setAttachContentTitles(contentTitleList);
                }

                caseInfo.setCaseId(c.getId());
                caseInfo.setTitle(c.getTitle());
                caseInfo.setStatus(CaseStatus.fromCaseStatusCode(c.getStatus()).getStatusDesc());
                caseInfo.setPriority(CasePriority.fromCasePriorityCode(c.getPriority()).getPriorityDesc());
                caseInfo.setPriorityCode(c.getPriority());
                ctagList = ctagDAO.selectTagsByFilter(c.getId(), cTagFilter);
                if (ctagList == null) {
                    continue;
                }
                caseInfo.setTags(ctagList);
                caseInfo.setAssignedUserId(c.getAssignedUserId());
                User owner = userDAO.selectUserById(c.getAssignedUserId());
                if (owner != null) {
                    caseInfo.setOwner(owner.getFirstName() + " " + owner.getLastName());
                }

                caseInfo.setLastUpdateTime(c.getLastUpdatedTime());
                caseInfo.setStaffMsgboardId(c.getStaffMsgboardId());
                caseInfo.setUserMsgboardId(c.getUserMsgboardId());
                caseInfo.setOpenedTime(c.getOpenedTime());
                caseInfoList.add(caseInfo);
            }
        }
        return caseInfoList;
    }

    public List<CaseInfo> convertToCaseInfo(List<Cases> caseList) {
        if (caseList == null) {
            return null;
        }
        List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();
        if (caseList != null) {
            for (Cases c : caseList) {
                CaseInfo caseInfo = new CaseInfo();
                caseInfo.setCaseId(c.getId());
                caseInfo.setTitle(c.getTitle());
                caseInfo.setStatus(CaseStatus.fromCaseStatusCode(c.getStatus()).getStatusDesc());
                caseInfo.setPriority(CasePriority.fromCasePriorityCode(c.getPriority()).getPriorityDesc());
                caseInfo.setPriorityCode(c.getPriority());
                caseInfo.setTags(ctagDAO.selectTagsByCaseId(c.getId()));
                caseInfo.setAssignedUserId(c.getAssignedUserId());
                User owner = userDAO.selectUserById(c.getAssignedUserId());
                if (owner != null) {
                    caseInfo.setOwner(owner.getFirstName() + " " + owner.getLastName());
                }
                caseInfo.setAttachContents(getContentListByCaseId(c.getId()));
                caseInfo.setAttachContentTitles(getContentTitleListByCaseId(c.getId()));
                caseInfo.setLastUpdateTime(c.getLastUpdatedTime());
                caseInfo.setStaffMsgboardId(c.getStaffMsgboardId());
                caseInfo.setUserMsgboardId(c.getUserMsgboardId());
                caseInfo.setOpenedTime(c.getOpenedTime());
                caseInfoList.add(caseInfo);

            }
        }
        return caseInfoList;
    }

    public Boolean addNoteInCase(Long caseId, String type, String messageBody,
            Integer messageBoardId, Integer userId) {
        if (messageBoardId <= 0) {
            // create a new message board;
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            msgboard = messageboardDao.create(msgboard);
            if (msgboard == null || msgboard.getId() <= 0) {
                return false;
            }
            messageBoardId = msgboard.getId();
            //update case
            int rows = 0;
            if (type.equals(Constants.CASE_NOTE_TYPE_USER)) {
                rows = caseDao.updateUserMsgBoardId(messageBoardId, caseId);
            } else if (type.equals(Constants.CASE_NOTE_TYPE_STAFF)) {
                rows = caseDao.updateStaffMsgBoardId(messageBoardId, caseId);
            } else {
                return false;
            }

            if (rows <= 0) {
                return false;
            }
        }
        //create message
        Message msg = new Message();

        msg.setAuthorUserId(userId);
        msg.setTitle("");
        msg.setBody(messageBody);
        msg.setCreatedTime(new Date());
        msg.setMsgboardId(messageBoardId);
        messageDao.create(msg);
        return true;
    }

    public HashSet<User> getCaseUsersByCaseId(int caseId) {
        HashSet<User> users = new HashSet<User>();
        List<CaseObject> caseObjectList = caseObjectDAO.selectCaseObjectByCaseIdAndObjectType(caseId, ObjectType.USER.getTypeCode());

        if (caseObjectList != null) {
            for (CaseObject caseObject : caseObjectList) {
                users.add(userDAO.selectUserById(caseObject.getObjectId()));
            }
        }
        return users;
    }

    public Integer getCaseUserMsgBoardId(int caseId) {
        final Cases cases = caseDao.selectCaseById(caseId);
        if (cases.getUserMsgboardId() <= 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);
            cases.setUserMsgboardId(msgboard.getId());
            caseDao.update(cases);
        }
        return cases.getUserMsgboardId();
    }

    public Integer getCaseStaffMsgBoardId(int caseId) {
        final Cases cases = caseDao.selectCaseById(caseId);
        if (cases.getStaffMsgboardId() <= 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);
            cases.setStaffMsgboardId(msgboard.getId());
            caseDao.update(cases);
        }
        return cases.getStaffMsgboardId();
    }

    public String getAttachmentsFilePathById(int id) {
        List<CaseAttachment> tmp_result = caseAttachmentDAO.selectAttachmentsFilePathById(id);
        if (tmp_result.size() <= 0) {
            return null;
        } else {
            return getAttachmentFullPath(tmp_result.get(0).getFilePath(), 0);
        }
    }

    public int deleteAttachmentsById(int id) {
        String tmps = this.getAttachmentsFilePathById(id);
        if (tmps != null) {
            File f = new File(tmps);
            f.delete();
        }
        return caseAttachmentDAO.deleteAttachmentsById(id);
    }

    public Long openCaseForProjectAdmin(TaskAssignment ta, String title, String notificationTypeName) {
        if (eventService.findMsgEvent(ta, notificationTypeName + " Open Case")) {
            return (long) 0;
        }
        Task task = taskDao.get(ta.getTaskId());
        Product product = productDao.get(task.getProductId());
        Project project = projectDao.get(product.getProjectId());

        ProjectUserView user = userDAO.selectProjectUser(project.getId(), project.getAdminUserId());
        Map parameters = siteMessageService.getParameters(ta, user);
        NotificationView notification = notificationItemService.getProjectNotificationView(notificationTypeName, project.getId(), user.getRoleId(), user.getLanguageId(), parameters);

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setTitle(title);
        caseInfo.setDescription(notification.getBody());
        caseInfo.setOpenedByUserId(1);
        caseInfo.setAssignedUserId(project.getAdminUserId());
        caseInfo.setStatusCode(CaseStatus.OPENNEW.getStatusCode());
        caseInfo.setProjectId(project.getId());
        caseInfo.setProductId(product.getId());
        caseInfo.setBlockPulishing(false);
        caseInfo.setBlockWorkFlow(false);
        caseInfo.setPriorityCode((short) 2);
        caseInfo.setAttachUsers(new ArrayList<User>());
        caseInfo.setAttachFiles(new ArrayList<CaseAttachment>());
        caseInfo.setTags(new ArrayList<Ctags>());
        List<Long> attachContentIdList = new ArrayList<Long>();
        int contentId = contentHeaderDAO.selectContentHeaderByHorseId(ta.getHorseId()).getId().intValue();
        attachContentIdList.add(new Long((long) contentId));
        caseInfo.setAttachContentIds(attachContentIdList);
        Long caseId = createCase(caseInfo);
        eventService.addMsgEvent(ta, notificationTypeName + " Open Case");
        return caseId;
    }


    /*
     * SFN - Storage File Name
     */

    public String getAttachmentFullPath(String fileName, int caseId) {
        if (fileName.startsWith("/caseattachment")) {
            return Config.getString(Config.KEY_STORAGE_UPLOAD_BASE) + fileName;
        }
        
        return Config.getString(Config.KEY_STORAGE_UPLOAD_BASE) + "/caseattachment/" + caseId + "/" + fileName;
    }


    public String getAttachmentTempPath(int uid, String fileName) {
        return Config.getString(Config.KEY_STORAGE_UPLOAD_BASE) + "/caseattachment/temp/" + uid + "/" + fileName;
    }

    public String getAttachmentPartialPath(String fileName, int caseId) {
        return "/caseattachment/" + caseId + "/" + fileName;
    }
    

    @Autowired
    public void setCaseDao(CaseDAO caseDao) {
        this.caseDao = caseDao;
    }

    @Autowired
    public void setUserDao(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setWorkflowDao(WorkflowDAO workflowDAO) {
        this.workflowDAO = workflowDAO;
    }

    @Autowired
    public void setMessageDao(MessageDAO messageDao) {
        this.messageDao = messageDao;
    }

    @Autowired
    public void setCaseObjectDao(CaseObjectDAO caseObjectDAO) {
        this.caseObjectDAO = caseObjectDAO;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDAO) {
        this.contentHeaderDAO = contentHeaderDAO;
    }

    @Autowired
    public void setCtagDao(CTagDAO ctagDAO) {
        this.ctagDAO = ctagDAO;
    }

    @Autowired
    public void setCaseAttachmentDao(CaseAttachmentDAO caseAttachmentDAO) {
        this.caseAttachmentDAO = caseAttachmentDAO;
    }

    @Autowired
    public void setCaseTagDao(CaseTagDAO caseTagDAO) {
        this.caseTagDAO = caseTagDAO;
    }

    @Autowired
    public void setMessageboardDao(MessageboardDAO messageboardDao) {
        this.messageboardDao = messageboardDao;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }

    @Autowired
    public void setWorkflowObejctDao(WorkflowObjectDAO workflowObejctDAO) {
        this.workflowObjectDAO = workflowObejctDAO;
    }

    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMessageService = siteMessageService;
    }

    @Autowired
    public void setNotificationItemService(NotificationItemService notificationItemService) {
        this.notificationItemService = notificationItemService;
    }

    public int getAttachmentByCaseIdAndFileName(int caseId, String fileName) {
        return caseAttachmentDAO.selectAttachmentByCaseIdAndFileName(caseId, fileName);
    }
}
