/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.TargetDAO;
import com.ocs.indaba.dao.TargetTagDAO;
import com.ocs.indaba.dao.TtagsDAO;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.TargetTag;
import com.ocs.indaba.po.Ttags;
import com.ocs.indaba.vo.ProjectTargetVO;
import com.ocs.util.Pagination;
import java.util.List;
import org.apache.log4j.Logger;
import org.drools.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class TargetService {

    private static final Logger logger = Logger.getLogger(NoteBookService.class);
    private TargetDAO targetDao = null;
    private HorseDAO horseDao = null;
    private TtagsDAO ttagsDao = null;
    private TargetTagDAO targetTagDao = null;

    public Target addTarget(Target target) {
        return targetDao.create(target);
    }

    public Target updateTarget(Target target) {
        return targetDao.update(target);
    }

    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public List<Target> getAllTargets() {
        logger.debug("Get all of the targets");

        return targetDao.selectAllTargets();
    }

    public List<Integer> findAllTargetIdsByTagname(String tagName) {
        return targetDao.selectAllTargetIdsByTagname(tagName);
    }

    public int getAllTargetCount(List<Integer> oaOfOrgIds, String tagName, int orgId, int visibility, String queryType, String query) {
        if (StringUtils.isEmpty(tagName)) {
            return Long.valueOf(targetDao.selectAllTargetCount(oaOfOrgIds, orgId, visibility, queryType, query)).intValue();
        } else {
            List<Integer> fromTargetIds = targetDao.selectAllTargetIdsByTagname(tagName);
            return Long.valueOf(targetDao.selectAllTargetCountWithinIds(fromTargetIds, oaOfOrgIds, orgId, visibility, queryType, query)).intValue();
        }
    }

    public List<Target> getAllTargets(List<Integer> oaOfOrgIds, String tagName, int orgId, int visibility, String sortName, String sortOrder, int offset, int count, String queryType, String query) {
        if (StringUtils.isEmpty(tagName)) {
            return targetDao.selectAllTargets(oaOfOrgIds, orgId, visibility, sortName, sortOrder, offset, count, queryType, query);
        } else {
            List<Integer> fromTargetIds = targetDao.selectAllTargetIdsByTagname(tagName);
            return targetDao.selectAllTargetsWithinIds(fromTargetIds, oaOfOrgIds, orgId, visibility, sortName, sortOrder, offset, count, queryType, query);
        }
    }

    public Pagination<ProjectTargetVO> getAllTargetsByProjectId(int projectId, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        long totalCount = targetDao.countTargetByProjectId(projectId);

        if (offset < 0) {
            offset = 0;
        }

        List<ProjectTargetVO> targets = targetDao.selectAllTargetsByProjectId(projectId, sortName, sortOrder, offset, count);

        Pagination<ProjectTargetVO> pagination = new Pagination<ProjectTargetVO>(totalCount, page, pageSize);
        pagination.setRows(targets);

        return pagination;
    }

    public List<ProjectTargetVO> getAllTargetsByProjectId(int projectId) {
        return targetDao.selectAllTargetsByProjectId(projectId, null, "ASC", 0, -1);
    }

    public List<Target> getAllTargets(int offset, int count) {
        return targetDao.selectAllTargets(offset, count);
    }

    /**
     * Get all PUBLIC targets and PRIVATE targets whose creator_org_id are in the
     * specified org id list
     *
     * @param orgIds
     * @return
     */
    public List<Target> getAllTargets(List<Integer> orgIds) {
        return targetDao.selectAllTargetsByOrgIds(orgIds);
    }

    /**
     * Get all PUBLIC targets and PRIVATE targets whose owner_org_id are in the
     * specified org id list
     *
     * @param orgIds
     * @return
     */
    public List<Target> getAllTargetsByVisibility(int visibility) {
        return targetDao.selectAllTargetsByVisibility(visibility);
    }

    public List<Target> getAllAvailableTargetsByProjectId(int project) {
        return targetDao.selectAllAvailableTargetsByProjectId(project);
    }

    public List<Target> getTargetsByProductId(int productId) {
        return targetDao.selectTargetsByProductId(productId);
    }

    /**
     * Select all of the target count
     *
     * @return list of target
     */
    public int getTargetCount() {
        return targetDao.selectTargetCount();
    }

    public Target getTargetById(int targetId) {
        logger.debug("Get the target by id: " + targetId);

        return targetDao.selectTargetById(targetId);
    }

    public Target getTargetByHorseId(int horseId) {
        logger.debug("Get the target by horse id: " + horseId);

        return targetDao.selectTargetByHorseId(horseId);
    }

    public List<Ttags> getAllTtags() {
        return ttagsDao.findAll();
    }

    public Ttags getTtags(int id) {
        return ttagsDao.get(id);
    }

    public TargetTag getTargetTag(int id) {
        return targetTagDao.get(id);
    }

    public TargetTag addTargetTag(TargetTag ttag) {
        return targetTagDao.create(ttag);
    }

    public void deleteTagsByIds(List<Integer> ids) {
        targetTagDao.deleteByIds(ids);
    }

    public void deleteTagsByTargetId(int targetId) {
        targetTagDao.deleteByTargetId(targetId);
    }

    public List<TargetTag> getTargetTagsByTargetId(int targetId) {
        return targetTagDao.selectTagsByTargetId(targetId);
    }

    public List<Integer> getTtagsIdsByTargetId(int targetId) {
        return targetTagDao.selectTtagsIdsByTargetId(targetId);
    }

    @Autowired
    public void setTargetDao(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }

    @Autowired
    public void setHorseDao(HorseDAO horseDao) {
        this.horseDao = horseDao;
    }

    @Autowired
    public void setTTagsDao(TtagsDAO ttagsDao) {
        this.ttagsDao = ttagsDao;
    }

    @Autowired
    public void setTargetTagDao(TargetTagDAO targetTagDao) {
        this.targetTagDao = targetTagDao;
    }

    //get all the other targets belong to the same product except the one of giving horseId
    public List<Target> getOtherTargetsByHorseId(int horseId) {
        Horse horse = horseDao.get(horseId);
        return targetDao.selectTargetByProductIdAndTargetId(horse.getProductId(), horse.getTargetId());
    }

    public List<Target> getTargetsByProjectId(int projectId) {
        return targetDao.selectTargetsByProjectId(projectId);
    }

    public List<Target> getNotCancelledTargetsForProduct(int projectId) {
        return targetDao.selectNotCancelledTargetsForProduct(projectId);
    }

    public boolean existsByName(int targetId, String targetName) {
        return targetDao.existsByName(targetId, targetName);
    }

    public boolean existsByShortName(int targetId, String targetShortName) {
        return targetDao.existsByShortName(targetId, targetShortName);
    }
}
