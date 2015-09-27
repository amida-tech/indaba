/**
 * 
 */
package com.ocs.indaba.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.ContentVersionDAO;
import com.ocs.indaba.dao.JournalPeerReviewDAO;
import com.ocs.indaba.dao.MessageboardDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.dao.SurveyAnswerVersionDAO;
import com.ocs.indaba.dao.SurveyPeerReviewDAO;
import com.ocs.indaba.dao.SurveyPeerReviewVersionDAO;
import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.ContentVersion;
import com.ocs.indaba.po.JournalPeerReview;
import com.ocs.indaba.po.Msgboard;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyAnswerVersion;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.po.SurveyPeerReviewVersion;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.vo.JournalPeerReviewVO;
import com.ocs.indaba.vo.SurveyPeerReviewVO;
import org.apache.log4j.Logger;

/**
 * @author Tiger Tang
 *
 */
public class ReviewService {

    private static final Logger logger = Logger.getLogger(ReviewService.class);

    public Integer getInternalMsgboardId(int horseId) {
        //logger.debug("========>>>> " + horseId + " ============");
        final ContentHeader contentHeader = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (contentHeader == null) {
            logger.debug("======== " + horseId + ": contentHeader=>NULL !!!!!============");
            return 0;
        } else if (contentHeader.getInternalMsgboardId() <= 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            contentHeader.setInternalMsgboardId(msgboard.getId());
            contentHeaderDao.update(contentHeader);
        }

        return contentHeader.getInternalMsgboardId();
    }

    public int getInternalMsgboardIdByVersionId(int cntVersionId) {
        ContentVersion cntVer = contentVersionDao.get(cntVersionId);
        return (cntVer == null) ? -1 : cntVer.getInternalMsgboardId();
    }

    public int getStaffAuthorMsgboardId(int horseId) {
        final ContentHeader contentHeader = contentHeaderDao.selectContentHeaderByHorseId(horseId);

        if (contentHeader.getStaffAuthorMsgboardId() <= 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            contentHeader.setStaffAuthorMsgboardId(msgboard.getId());
            contentHeaderDao.update(contentHeader);
        }

        return contentHeader.getStaffAuthorMsgboardId();
    }

    public int getStaffAuthorMsgboardIdByVersionId(int cntVersionId) {
        ContentVersion cntVer = contentVersionDao.get(cntVersionId);
        return (cntVer == null) ? -1 : cntVer.getStaffAuthorMsgboardId();
    }

    public JournalPeerReviewVO getJournalPeerReview(int prjid, int uid, int horseId) {
        final ContentHeader contentHeader = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        JournalPeerReview jpr = journalPeerReviewDao.getPeerReviewsByJournalContentAndUserId(contentHeader.getContentObjectId(), uid);

        if (jpr == null) {
            jpr = new JournalPeerReview();
            jpr.setJournalContentObjectId(contentHeader.getContentObjectId());
            jpr.setReviewerUserId(uid);

            Msgboard mb = new Msgboard();
            mb.setCreateTime(new Date());
            messageboardDao.create(mb);
            jpr.setMsgboardId(mb.getId());
            journalPeerReviewDao.create(jpr);
        }

        if (jpr.getMsgboardId() == null || jpr.getMsgboardId() <= 0) {
            Msgboard mb = new Msgboard();
            mb.setCreateTime(new Date());
            messageboardDao.create(mb);
            jpr.setMsgboardId(mb.getId());
            journalPeerReviewDao.update(jpr);
        }

        JournalPeerReviewVO result = new JournalPeerReviewVO();
        result.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, jpr.getReviewerUserId()));
        result.setMsgboardId(jpr.getMsgboardId());
        result.setOpinions(jpr.getOpinions());

        return result;
    }

    public int getSurveyInternalMsgboardId(int surveyAnswerId) {
        SurveyAnswer sa = surveyAnswerDao.get(surveyAnswerId);

        if (sa.getInternalMsgboardId() == 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            sa.setInternalMsgboardId(msgboard.getId());
            surveyAnswerDao.update(sa);
        }

        return sa.getInternalMsgboardId();
    }

    public int getSurveyInternalMsgboardIdByVersionId(int versionId, int surveyQuestionId) {
        SurveyAnswerVersion surveyAnswerVersion = surveyAnswerVersionDao.getSurveyAnswerVersionByContentVersionAndSurveyQuestion(versionId, surveyQuestionId);
        return (surveyAnswerVersion == null) ? -1 : surveyAnswerVersion.getInternalMsgboardId();
    }

    public int getSurveyStaffAuthorMsgboardId(int surveyAnswerId) {
        SurveyAnswer sa = surveyAnswerDao.get(surveyAnswerId);

        if (sa.getStaffAuthorMsgboardId() == 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            sa.setStaffAuthorMsgboardId(msgboard.getId());
            surveyAnswerDao.update(sa);
        }

        return sa.getStaffAuthorMsgboardId();
    }

    public int getSurveyStaffAuthorMsgboardIdByVersionId(int versionId, int surveyQuestionId) {
        SurveyAnswerVersion surveyAnswerVersion = surveyAnswerVersionDao.getSurveyAnswerVersionByContentVersionAndSurveyQuestion(versionId, surveyQuestionId);
        return (surveyAnswerVersion == null) ? -1 : surveyAnswerVersion.getStaffAuthorMsgboardId();
    }

    public SurveyPeerReviewVO getSurveyPeerReview(int prjid, int uid, int surveyAnswerId) {
        SurveyPeerReview spr = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(surveyAnswerId, uid);

        if (spr == null) {
            spr = new SurveyPeerReview();
            spr.setReviewerUserId(uid);
            spr.setSurveyAnswerId(surveyAnswerId);
            spr.setOpinion((short) -1);
            spr.setSuggestedAnswerObjectId(-1);
            spr.setLastChangeTime(new Date());

            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            spr.setMsgboardId(msgboard.getId());

            surveyPeerReviewDao.create(spr);
        }

        if (spr.getMsgboardId() == 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            spr.setMsgboardId(msgboard.getId());
            surveyPeerReviewDao.update(spr);
        }

        SurveyPeerReviewVO sprvo = new SurveyPeerReviewVO();

        sprvo.setMsgboardId(spr.getMsgboardId());
        sprvo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, spr.getReviewerUserId()));
        sprvo.setComments(spr.getComments());
        sprvo.setId(spr.getId());
        sprvo.setLastChangeTime(spr.getLastChangeTime());
        sprvo.setOpinion(spr.getOpinion());
        sprvo.setSuggestedAnswerObjectId(spr.getSuggestedAnswerObjectId());
        sprvo.setAnswerId(spr.getSurveyAnswerId());

        return sprvo;
    }

    public SurveyPeerReviewVO getSurveyPeerReviewByVersion(int prjid, int uid, int versionId) {
        return getSurveyPeerReviewByVersion(prjid, uid, uid, versionId);
    }

    public SurveyPeerReviewVO getSurveyPeerReview(int prjid, int uid, int reviewerId, int surveyAnswerId) {
        SurveyPeerReview spr = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(surveyAnswerId, reviewerId);

        if (spr == null) {
            spr = new SurveyPeerReview();
            spr.setReviewerUserId(reviewerId);
            spr.setSurveyAnswerId(surveyAnswerId);
            spr.setLastChangeTime(new Date());

            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            spr.setMsgboardId(msgboard.getId());

            surveyPeerReviewDao.create(spr);
        }

        if (spr.getMsgboardId() == 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            spr.setMsgboardId(msgboard.getId());
            surveyPeerReviewDao.update(spr);
        }

        SurveyPeerReviewVO sprvo = new SurveyPeerReviewVO();

        sprvo.setMsgboardId(spr.getMsgboardId());
        sprvo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, reviewerId));
        sprvo.setComments(spr.getComments());
        sprvo.setId(spr.getId());
        sprvo.setLastChangeTime(spr.getLastChangeTime());
        sprvo.setOpinion(spr.getOpinion());
        sprvo.setSuggestedAnswerObjectId(spr.getSuggestedAnswerObjectId());
        sprvo.setAnswerId(spr.getSurveyAnswerId());
        sprvo.setSubmitTime(spr.getSubmitTime());

        return sprvo;
    }

    public SurveyPeerReviewVO getSurveyPeerReviewByVersion(int prjid, int uid, int reviewerId, int cntVersionId) {
        SurveyPeerReviewVersion sprv = surveyPeerReviewVersionDao.selectByVersionId(cntVersionId, reviewerId);
        SurveyPeerReview spr = new SurveyPeerReview();
        spr.setSurveyAnswerId(-1);
        if (sprv == null) {
            spr.setReviewerUserId(reviewerId);
            spr.setLastChangeTime(new Date());
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);
            spr.setMsgboardId(msgboard.getId());
            spr.setSuggestedAnswerObjectId(-1);

            //surveyPeerReviewDao.create(spr);
        } else {
            spr.setComments(sprv.getComments());
            spr.setId(sprv.getId());
            spr.setLastChangeTime(sprv.getLastChangeTime());
            spr.setMsgboardId(sprv.getMsgboardId());
            spr.setOpinion(sprv.getOpinion());
            spr.setReviewerUserId(sprv.getReviewerUserId());
            spr.setSubmitTime(sprv.getSubmitTime());
            spr.setSuggestedAnswerObjectId(sprv.getSuggestedAnswerObjectId());
        }

        if (spr.getMsgboardId() == 0) {
            Msgboard msgboard = new Msgboard();
            msgboard.setCreateTime(new Date());
            messageboardDao.create(msgboard);

            spr.setMsgboardId(msgboard.getId());
            surveyPeerReviewDao.update(spr);
        }

        SurveyPeerReviewVO sprvo = new SurveyPeerReviewVO();

        sprvo.setMsgboardId(spr.getMsgboardId());
        sprvo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, reviewerId));
        sprvo.setComments(spr.getComments());
        sprvo.setId(spr.getId());
        sprvo.setLastChangeTime(spr.getLastChangeTime());
        sprvo.setOpinion(spr.getOpinion());
        sprvo.setSuggestedAnswerObjectId(spr.getSuggestedAnswerObjectId());
        sprvo.setAnswerId(spr.getSurveyAnswerId());
        sprvo.setSubmitTime(spr.getSubmitTime());
        sprvo.setSurveyPeerReviewVersionId((sprv != null) ? sprv.getId() : -1);

        return sprvo;
    }

    public List<SurveyPeerReviewVO> getSurveyPRReviews(int prjid, int uid, int surveyAnswerId) {
        final List<com.ocs.indaba.po.SurveyPeerReview> list = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerId(surveyAnswerId);

        if (list != null) {
            for (com.ocs.indaba.po.SurveyPeerReview jpr : list) {
                if (jpr.getMsgboardId() <= 0) {
                    Msgboard mb = new Msgboard();
                    mb.setCreateTime(new Date());
                    messageboardDao.create(mb);
                    jpr.setMsgboardId(mb.getId());
                    surveyPeerReviewDao.update(jpr);
                }
            }
        }

        List<SurveyPeerReviewVO> result = new ArrayList<SurveyPeerReviewVO>();

        if (list != null) {
            for (com.ocs.indaba.po.SurveyPeerReview spr : list) {
                SurveyPeerReviewVO vo = new SurveyPeerReviewVO();

                vo.setMsgboardId(spr.getMsgboardId());
                vo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, spr.getReviewerUserId()));
                vo.setComments(spr.getComments());
                vo.setId(spr.getId());
                vo.setLastChangeTime(spr.getLastChangeTime());
                vo.setOpinion(spr.getOpinion());
                vo.setSuggestedAnswerObjectId(spr.getSuggestedAnswerObjectId());
                vo.setAnswerId(spr.getSurveyAnswerId());
                vo.setSubmitTime(spr.getSubmitTime());

                result.add(vo);
            }
        }
        return result;
    }

    public List<SurveyPeerReviewVO> getSubmittedSurveyPRReviews(int prjid, int uid, int surveyAnswerId, boolean byUserSelfOnly) {
        List<SurveyPeerReview> list = byUserSelfOnly
                ? surveyPeerReviewDao.getSubmittedSurveyPeerReviewsBySurveyAnswerIdAndUserId(surveyAnswerId, uid)
                : surveyPeerReviewDao.getSubmittedSurveyPeerReviewsBySurveyAnswerId(surveyAnswerId);

        if (list == null || list.isEmpty()) {
            return null;
        }

        for (SurveyPeerReview jpr : list) {
            if (jpr.getMsgboardId() <= 0) {
                Msgboard mb = new Msgboard();
                mb.setCreateTime(new Date());
                messageboardDao.create(mb);
                jpr.setMsgboardId(mb.getId());
                surveyPeerReviewDao.update(jpr);
            }
        }

        List<SurveyPeerReviewVO> result = new ArrayList<SurveyPeerReviewVO>();
        for (SurveyPeerReview spr : list) {
            SurveyPeerReviewVO vo = new SurveyPeerReviewVO();

            vo.setMsgboardId(spr.getMsgboardId());
            vo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, spr.getReviewerUserId()));
            vo.setComments(spr.getComments());
            vo.setId(spr.getId());
            vo.setLastChangeTime(spr.getLastChangeTime());
            vo.setOpinion(spr.getOpinion());
            vo.setSuggestedAnswerObjectId(spr.getSuggestedAnswerObjectId());
            vo.setAnswerId(spr.getSurveyAnswerId());
            vo.setSubmitTime(spr.getSubmitTime());

            result.add(vo);
        }
        return result;
    }

    public List<SurveyPeerReviewVO> getSubmittedSurveyPRReviewsByVersionId(int prjid, int uid, int surveyAnswerId, boolean byUserSelfOnly, int saVerId) {
        List<SurveyPeerReviewVersion> sprvList = byUserSelfOnly
                ? surveyPeerReviewVersionDao.selectAllByVersionId(saVerId, uid)
                : surveyPeerReviewVersionDao.selectAllByVersionId(saVerId);
        if (sprvList == null || sprvList.isEmpty()) {
            return null;
        }

        List<SurveyPeerReview> list = new ArrayList<SurveyPeerReview>(sprvList.size());
        for (SurveyPeerReviewVersion sprv : sprvList) {
            SurveyPeerReview spr = new SurveyPeerReview();
            spr.setComments(sprv.getComments());
            spr.setId(sprv.getId());
            spr.setLastChangeTime(sprv.getLastChangeTime());
            spr.setMsgboardId(sprv.getMsgboardId());
            spr.setOpinion(sprv.getOpinion());
            spr.setReviewerUserId(sprv.getReviewerUserId());
            spr.setSubmitTime(sprv.getSubmitTime());
            spr.setSuggestedAnswerObjectId(sprv.getSuggestedAnswerObjectId());
            //spr.setSurveyAnswerId(sprv.getSuggestedAnswerObjectId());
            list.add(spr);
        }
        for (SurveyPeerReview jpr : list) {
            if (jpr.getMsgboardId() <= 0) {
                Msgboard mb = new Msgboard();
                mb.setCreateTime(new Date());
                messageboardDao.create(mb);
                jpr.setMsgboardId(mb.getId());
                surveyPeerReviewDao.update(jpr);
            }
        }

        List<SurveyPeerReviewVO> result = new ArrayList<SurveyPeerReviewVO>();
        for (SurveyPeerReview spr : list) {
            SurveyPeerReviewVO vo = new SurveyPeerReviewVO();

            vo.setMsgboardId(spr.getMsgboardId());
            vo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, spr.getReviewerUserId()));
            vo.setComments(spr.getComments());
            vo.setId(spr.getId());
            vo.setLastChangeTime(spr.getLastChangeTime());
            vo.setOpinion(spr.getOpinion());
            vo.setSuggestedAnswerObjectId(spr.getSuggestedAnswerObjectId());
            vo.setAnswerId(spr.getSurveyAnswerId());
            vo.setSubmitTime(spr.getSubmitTime());

            result.add(vo);
        }
        return result;
    }

    public void saveJournalPeerReviewOpinions(int horseId, int assignId, int uid, String opinions) {
        final ContentHeader contentHeader = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        JournalPeerReview jpr = journalPeerReviewDao.getPeerReviewsByJournalContentAndUserId(contentHeader.getContentObjectId(), uid);

        final TaskAssignment assignment = taskAssignmentDao.get(assignId);
        if (assignment != null) {
            if (assignment.getStatus() < Constants.TASK_STATUS_STARTED) {
                assignment.setStatus((short) Constants.TASK_STATUS_STARTED);
            }
            if (assignment.getPercent() == null || assignment.getPercent() < 0.0001f) {
                assignment.setPercent(0.5f);
            }
            taskAssignmentDao.update(assignment);
        }

        jpr.setOpinions(opinions);
        jpr.setLastChangeTime(new Date());
        journalPeerReviewDao.update(jpr);
    }

    public void changeJournalPeerReviewOpinions(int journalPeerReviewId, String opinions) {
        JournalPeerReview jpr = journalPeerReviewDao.get(journalPeerReviewId);
        if (jpr != null) {
            jpr.setOpinions(opinions);
            journalPeerReviewDao.update(jpr);
        }
    }

    public List<JournalPeerReviewVO> getJournalPRReviews(int prjid, int uid, int horseId) {
        List<JournalPeerReviewVO> result = new ArrayList<JournalPeerReviewVO>();

        final ContentHeader contentHeader = contentHeaderDao.selectContentHeaderByHorseId(horseId);

        if (contentHeader == null) return result;

        List<JournalPeerReview> peerReviews = journalPeerReviewDao.getPeerReviewsByJournalContent(contentHeader.getContentObjectId());

        if (peerReviews == null) return result;
        
        for (JournalPeerReview jpr : peerReviews) {
            if (jpr.getMsgboardId() == null || jpr.getMsgboardId() <= 0) {
                Msgboard mb = new Msgboard();
                mb.setCreateTime(new Date());
                messageboardDao.create(mb);

                jpr.setMsgboardId(mb.getId());
                journalPeerReviewDao.update(jpr);
            }

            JournalPeerReviewVO jprvo = new JournalPeerReviewVO();
            jprvo.setId(jpr.getId());
            jprvo.setMsgboardId(jpr.getMsgboardId());
            jprvo.setSubmitTime(jpr.getSubmitTime());
            jprvo.setOpinions(jpr.getOpinions());
            jprvo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, jpr.getReviewerUserId()));

            result.add(jprvo);
        }

        return result;
    }

    public List<JournalPeerReviewVO> getSubmittedJournalPRReviews(int prjid, int uid, int horseId) {
        List<JournalPeerReviewVO> result = new ArrayList<JournalPeerReviewVO>();
        final ContentHeader contentHeader = contentHeaderDao.selectContentHeaderByHorseId(horseId);

        if (contentHeader == null) return result;

        List<JournalPeerReview> peerReviews = journalPeerReviewDao.getSubmittedPeerReviewsByJournalContent(contentHeader.getContentObjectId());

        if (peerReviews == null) return result;
        
        for (JournalPeerReview jpr : peerReviews) {
            if (jpr.getMsgboardId() == null || jpr.getMsgboardId() <= 0) {
                Msgboard mb = new Msgboard();
                mb.setCreateTime(new Date());
                messageboardDao.create(mb);

                jpr.setMsgboardId(mb.getId());
                journalPeerReviewDao.update(jpr);
            }

            JournalPeerReviewVO jprvo = new JournalPeerReviewVO();
            jprvo.setId(jpr.getId());
            jprvo.setMsgboardId(jpr.getMsgboardId());
            jprvo.setOpinions(jpr.getOpinions());
            jprvo.setSubmitTime(jpr.getSubmitTime());
            jprvo.setReviewer(viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, uid, jpr.getReviewerUserId()));

            result.add(jprvo);
        }

        return result;
    }
    private ContentHeaderDAO contentHeaderDao;
    private ContentVersionDAO contentVersionDao;
    private JournalPeerReviewDAO journalPeerReviewDao;
    private SurveyAnswerDAO surveyAnswerDao;
    private SurveyAnswerVersionDAO surveyAnswerVersionDao;
    private SurveyPeerReviewDAO surveyPeerReviewDao;
    private SurveyPeerReviewVersionDAO surveyPeerReviewVersionDao;
    private MessageboardDAO messageboardDao;
    private TaskAssignmentDAO taskAssignmentDao;

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setContentVersionDao(ContentVersionDAO contentVersionDao) {
        this.contentVersionDao = contentVersionDao;
    }

    @Autowired
    public void setJournalPeerReviewDao(JournalPeerReviewDAO journalPeerReviewDao) {
        this.journalPeerReviewDao = journalPeerReviewDao;
    }

    @Autowired
    public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
        this.surveyAnswerDao = surveyAnswerDao;
    }

    @Autowired
    public void setSurveyAnswerVersionDao(SurveyAnswerVersionDAO surveyAnswerVersionDao) {
        this.surveyAnswerVersionDao = surveyAnswerVersionDao;
    }

    @Autowired
    public void setSurveyPeerReviewDao(SurveyPeerReviewDAO surveyPeerReviewDao) {
        this.surveyPeerReviewDao = surveyPeerReviewDao;
    }

    @Autowired
    public void setSurveyPeerReviewVersionDao(SurveyPeerReviewVersionDAO surveyPeerReviewVersionDao) {
        this.surveyPeerReviewVersionDao = surveyPeerReviewVersionDao;
    }

    @Autowired
    public void setMessageboardDao(MessageboardDAO messageboardDao) {
        this.messageboardDao = messageboardDao;
    }

    @Autowired
    public void setTaskAssignmentDao(TaskAssignmentDAO taskAssignmentDao) {
        this.taskAssignmentDao = taskAssignmentDao;
    }
    private ViewPermissionService viewPermissionService;

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }
}
