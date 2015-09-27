/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.ContentVersionDAO;
import com.ocs.indaba.dao.SurveyAnswerAttachmentVersionDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.dao.SurveyAnswerVersionDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.SurveyQuestionDAO;
import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.ContentVersion;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyAnswerVersion;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.AnswerObjectChoiceDAO;
import com.ocs.indaba.dao.AnswerObjectFloatDAO;
import com.ocs.indaba.dao.AnswerObjectIntegerDAO;
import com.ocs.indaba.dao.AnswerObjectTextDAO;
import com.ocs.indaba.dao.AttachmentDAO;
import com.ocs.indaba.dao.ReferenceDAO;
import com.ocs.indaba.dao.ReferenceObjectDAO;
import com.ocs.indaba.dao.SurveyAnswerAttachmentDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.AnswerObjectChoice;
import com.ocs.indaba.po.AnswerObjectFloat;
import com.ocs.indaba.po.AnswerObjectInteger;
import com.ocs.indaba.po.AnswerObjectText;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.Reference;
import com.ocs.indaba.po.ReferenceObject;
import com.ocs.indaba.po.SurveyAnswerAttachment;
import com.ocs.indaba.po.SurveyAnswerAttachmentVersion;
import com.ocs.indaba.po.SurveyQuestion;
import com.ocs.indaba.po.User;
import com.ocs.indaba.vo.SurveyAnswerOriginalView;
import com.ocs.indaba.vo.SurveyCategoryView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrator
 */
public class SurveyAnswerService {

    private static final Logger logger = Logger.getLogger(SurveyAnswerService.class);
    private SurveyAnswerDAO surveyAnswerDao = null;
    private SurveyAnswerAttachmentDAO surveyAnswerAttachmentDao = null;
    private SurveyAnswerAttachmentVersionDAO surveyAnswerAttachmentVersionDao = null;
    private ContentVersionDAO contentVersionDao = null;
    private SurveyAnswerVersionDAO surveyAnswerVersionDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    private TaskAssignmentDAO taskAssignmentDao = null;
    private TaskDAO taskDao = null;
    private SurveyService surveyService = null;
    private SurveyQuestionDAO surveyQuestionDao = null;
    private SurveyIndicatorDAO surveyIndicatorDao = null;
    private AnswerObjectChoiceDAO answerObjectChoiceDao = null;
    private AnswerObjectFloatDAO answerObjectFloatDao = null;
    private AnswerObjectIntegerDAO answerObjectIntegerDao = null;
    private AnswerObjectTextDAO answerObjectTextDao = null;
    private ReferenceDAO referenceDao = null;
    private ReferenceObjectDAO referenceObjectDao = null;
    private UserDAO userDao = null;
    private AttachmentDAO attachmentDao = null;

    @Autowired
    public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
        this.surveyAnswerDao = surveyAnswerDao;
    }

    @Autowired
    public void setSurveyAnswerAttachmentDao(SurveyAnswerAttachmentDAO surveyAnswerAttachmentDao) {
        this.surveyAnswerAttachmentDao = surveyAnswerAttachmentDao;
    }

    @Autowired
    public void setSurveyAnswerAttachmentVersionDao(SurveyAnswerAttachmentVersionDAO surveyAnswerAttachmentVersionDao) {
        this.surveyAnswerAttachmentVersionDao = surveyAnswerAttachmentVersionDao;
    }

    @Autowired
    public void setContentVersionDao(ContentVersionDAO contentVersionDao) {
        this.contentVersionDao = contentVersionDao;
    }

    @Autowired
    public void setSurveyAnswerVersionDao(SurveyAnswerVersionDAO surveyAnswerVersionDao) {
        this.surveyAnswerVersionDao = surveyAnswerVersionDao;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setTaskAssignmentDao(TaskAssignmentDAO taskAssignmentDao) {
        this.taskAssignmentDao = taskAssignmentDao;
    }

    @Autowired
    public void setSurveyQuestionDao(SurveyQuestionDAO surveyQuestionDao) {
        this.surveyQuestionDao = surveyQuestionDao;
    }

    @Autowired
    public void setSurveyIndicatorDao(SurveyIndicatorDAO surveyIndicatorDao) {
        this.surveyIndicatorDao = surveyIndicatorDao;
    }

    @Autowired
    public void setAnswerObjectChoiceDao(AnswerObjectChoiceDAO answerObjectChoiceDao) {
        this.answerObjectChoiceDao = answerObjectChoiceDao;
    }

    @Autowired
    public void setAnswerObjectFloatDao(AnswerObjectFloatDAO answerObjectFloatDao) {
        this.answerObjectFloatDao = answerObjectFloatDao;
    }

    @Autowired
    public void setAnswerObjectIntegerDao(AnswerObjectIntegerDAO answerObjectIntegerDao) {
        this.answerObjectIntegerDao = answerObjectIntegerDao;
    }

    @Autowired
    public void setAnswerObjectTextDao(AnswerObjectTextDAO answerObjectTextDao) {
        this.answerObjectTextDao = answerObjectTextDao;
    }

    @Autowired
    public void setReferenceDao(ReferenceDAO referenceDao) {
        this.referenceDao = referenceDao;
    }

    @Autowired
    public void setReferenceObjectDao(ReferenceObjectDAO referenceObjectDao) {
        this.referenceObjectDao = referenceObjectDao;
    }

    @Autowired
    public void setUserDAO(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setAttachmentDAO(AttachmentDAO attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    public void copySurveyAnswers(int horseId, int taskAssignmentId, int userId) {
        List<SurveyAnswer> answers = surveyAnswerDao.getAllCompletedSurveyAnswersByHorse(horseId);
        //create new content version
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        Task task = taskDao.get(taskAssignmentDao.get(taskAssignmentId).getTaskId());
        ContentVersion contentVersion = new ContentVersion();
        contentVersion.setContentHeaderId(cntHdr.getId());
        contentVersion.setUserId(userId);
        contentVersion.setDescription(task.getTaskName());
        contentVersion.setCreateTime(Calendar.getInstance().getTime());
        ContentVersion newContentVersion = contentVersionDao.save(contentVersion);

        //create new survey_answer_version;
        int contentVersionId = newContentVersion.getId();

        if (answers != null) {
            for (SurveyAnswer answer : answers) {
                SurveyAnswerVersion answerVersion = new SurveyAnswerVersion();
                answerVersion.setSurveyQuestionId(answer.getSurveyQuestionId());
                answerVersion.setAnswerUserId(answer.getAnswerUserId());
                answerVersion.setAnswerTime(answer.getAnswerTime());
                answerVersion.setComments(answer.getComments());
                answerVersion.setContentVersionId(contentVersionId);
                //create new answerObject and ReferenceObject
                SurveyIndicator surveyIndicator = surveyIndicatorDao.get(surveyQuestionDao.get(answer.getSurveyQuestionId()).getSurveyIndicatorId());
                if (surveyIndicator == null) {
                    continue;
                }
                answerVersion.setAnswerObjectId(copyAnswerObject(answer.getAnswerObjectId(), surveyIndicator.getAnswerType()));

                Reference ref = referenceDao.selectReferenceById(surveyIndicator.getReferenceId());
                if (ref == null) {
                    continue;
                }
                answerVersion.setReferenceObjectId(copyReferenceObject(answer.getReferenceObjectId(), ref.getChoiceType()));
                answerVersion = surveyAnswerVersionDao.save(answerVersion);

                List<Attachment> attachments = attachmentDao.selectSurveyAnswerAttachmentsByAnswerId(answer.getId());
                if (attachments != null) {
                    logger.debug("Copy survey answer's attachment[answerId=" + answer.getId() + "answerVersionId=" + answerVersion.getId() + "].");
                    for (Attachment attach : attachments) {
                        SurveyAnswerAttachmentVersion attachVer = new SurveyAnswerAttachmentVersion();
                        attachVer.setSurveyAnswerVersionId(answerVersion.getId());
                        attachVer.setFilePath(attach.getFilePath());
                        attachVer.setName(attach.getName());
                        attachVer.setNote(attach.getNote());
                        attachVer.setSize(attach.getSize());
                        attachVer.setType(attach.getType());
                        attachVer.setUpdateTime(attach.getUpdateTime());
                        attachVer.setUserId(attach.getUserId());
                        surveyAnswerAttachmentVersionDao.create(attachVer);
                    }
                }
            }
        }
    }

    private int copyAnswerObject(int answerObjectId, int answerType) {
        switch (answerType) {
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                AnswerObjectChoice choice = answerObjectChoiceDao.getAnswerObjectChoicebyId(answerObjectId);
                AnswerObjectChoice newChoice = new AnswerObjectChoice();
                newChoice.setChoices(choice.getChoices());
                newChoice = answerObjectChoiceDao.save(newChoice);
                return newChoice.getId();
            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                AnswerObjectInteger resultInt = this.answerObjectIntegerDao.getAnswerObjectIntegerbyId(answerObjectId);
                AnswerObjectInteger newResultInt = new AnswerObjectInteger();
                newResultInt.setValue(resultInt.getValue());
                newResultInt = this.answerObjectIntegerDao.save(newResultInt);
                return newResultInt.getId();
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                AnswerObjectFloat resultFloat = this.answerObjectFloatDao.getAnswerObjectFloatbyId(answerObjectId);
                AnswerObjectFloat newResultFloat = new AnswerObjectFloat();
                newResultFloat.setValue(resultFloat.getValue());
                newResultFloat = this.answerObjectFloatDao.save(newResultFloat);
                return newResultFloat.getId();
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                AnswerObjectText text = this.answerObjectTextDao.getAnswerObjectTextbyId(answerObjectId);
                AnswerObjectText newText = new AnswerObjectText();
                newText.setValue(text.getValue());
                newText = this.answerObjectTextDao.save(newText);
                return newText.getId();
            default:
                return -1;
        }
    }

    private int copyReferenceObject(int refObjectId, int refType) {
        ReferenceObject ref = this.referenceObjectDao.getReferenceObjectById(refObjectId);
        ReferenceObject newRef = new ReferenceObject();
        newRef.setChoices(ref.getChoices());
        newRef.setComments(ref.getComments());
        newRef.setReferenceId(ref.getReferenceId());
        newRef.setSourceDescription(ref.getSourceDescription());
        newRef = this.referenceObjectDao.save(newRef);
        return newRef.getId();
    }

    public SurveyAnswerOriginalView getSurveyAnswerOriginalView(int horseId, int surveyAnswerId) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return null;
        }

        ContentVersion contentVersion = contentVersionDao.getFirstContentVersion(cntHdr.getId());
        if (contentVersion == null) {
            return null;
        }

        SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
        if (surveyAnswer == null) {
            return null;
        }
        SurveyQuestion surveyQuestion = surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId());
        SurveyAnswerVersion sav = surveyAnswerVersionDao.getSurveyAnswerVersionByContentVersionAndSurveyQuestion(contentVersion.getId(), surveyQuestion.getId());
        if (sav == null) {
            return null;
        }

        List<SurveyCategoryView> surveyCategoryViewList = surveyService.getSurveyCategoryTree(surveyQuestion.getSurveyCategoryId());
        SurveyIndicator indicator = surveyIndicatorDao.selectSurveyIndicatorById(surveyQuestion.getSurveyIndicatorId());
        Reference ref = this.referenceDao.selectReferenceById(indicator.getReferenceId());

        SurveyAnswerOriginalView view = new SurveyAnswerOriginalView();
        view.setContentVersionId(contentVersion.getId());

        User user = userDao.get(contentVersion.getUserId());
        view.setTaskName(contentVersion.getDescription());
        view.setUser(user);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = dateFormat.format(contentVersion.getCreateTime());
        view.setCreateTime(createTime);
        view.setSurveyAnswerId(surveyAnswerId);
        view.setAnswerType(indicator.getAnswerType());
        view.setAnswerTypeId(indicator.getAnswerTypeId());
        view.setName(indicator.getName());
        view.setPublicName(surveyQuestion.getPublicName());
        view.setQuestion(indicator.getQuestion());
        view.setReferType(ref.getChoiceType());
        view.setReferId(ref.getId());
        view.setReferName(ref.getName());
        view.setCategoryViewList(surveyCategoryViewList);
        view.setRefdescrition(ref.getDescription());
        view.setReferenceObjectId(sav.getReferenceObjectId());
        view.setAnswerObjectId(sav.getAnswerObjectId());
        view.setComments(sav.getComments());
        return view;
    }

    public boolean hasOriginalAnswer(int horseId, int surveyAnswerId) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return false;
        }

        ContentVersion contentVersion = contentVersionDao.getFirstContentVersion(cntHdr.getId());
        if (contentVersion == null) {
            return false;
        }

        SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
        if (surveyAnswer == null) {
            return false;
        }

        SurveyQuestion surveyQuestion = surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId());
        SurveyAnswerVersion sav = surveyAnswerVersionDao.getSurveyAnswerVersionByContentVersionAndSurveyQuestion(contentVersion.getId(), surveyQuestion.getId());
        if (sav == null) {
            return false;
        } else {
            return true;
        }
    }

    public void addSurveyAnswerAttachment(SurveyAnswerAttachment surveyAnswerAttachment) {
        surveyAnswerAttachmentDao.create(surveyAnswerAttachment);
    }

    public List<Attachment> getSurveyAnswerAttachments(int answerId) {
        return attachmentDao.selectSurveyAnswerAttachmentsByAnswerId(answerId);
    }

    public List<Attachment> getSurveyAnswerAttachmentsByVersionId(int answerVersionId) {
        List<SurveyAnswerAttachmentVersion> attachmentsByVersion = surveyAnswerAttachmentVersionDao.selectByVersionId(answerVersionId);
        List<Attachment> attachments = new ArrayList<Attachment>();
        if (attachmentsByVersion != null && !attachmentsByVersion.isEmpty()) {
            for (SurveyAnswerAttachmentVersion item : attachmentsByVersion) {
                Attachment attachment = new Attachment();
                attachment.setFilePath(item.getFilePath());
                attachment.setId(item.getId());
                attachment.setName(item.getName());
                attachment.setNote(item.getNote());
                attachment.setSize(item.getSize());
                attachment.setType(item.getType());
                attachment.setUpdateTime(item.getUpdateTime());
                attachment.setUserId(item.getUserId());
                attachments.add(attachment);
            }
        }
        return attachments;
    }

    public List<Attachment> getSurveyAnswerAttachmentsVersion(int contentVersionId, int answerId) {
        return attachmentDao.selectSurveyAnswerAttachmentsVersionByAnswerId(contentVersionId, answerId);
    }

    public Attachment getSurveyAnswerAttachment(int attachId) {
        return attachmentDao.selectAttachmentById(attachId);
    }

    public Attachment getSurveyAnswerAttachmentVersion(int attachId) {
        return attachmentDao.selectSurveyAnswerAttachmentVersionByAttachId(attachId);
    }

    public void deleteSurveyAnswerAttachment(int attachId, int answerId) {
        surveyAnswerAttachmentDao.deleteSurveyAnswerAttachment(attachId, answerId);
        attachmentDao.delete(attachId);
    }

    public SurveyAnswer getSurveyAnswerByFlag(int flagId) {
        return surveyAnswerDao.getSurveyAnswerByFlag(flagId);
    }
}
