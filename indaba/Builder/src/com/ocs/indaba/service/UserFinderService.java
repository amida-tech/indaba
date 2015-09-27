/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.UserFinderDAO;
import com.ocs.indaba.dao.UserFinderEventDAO;
import com.ocs.indaba.po.Userfinder;
import com.ocs.indaba.po.UserfinderEvent;
import com.ocs.indaba.vo.UserfinderVO;
import com.ocs.util.Pagination;
import java.util.Date;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class UserFinderService {

    private static final Logger logger = Logger.getLogger(UserFinderService.class);
    private UserFinderDAO userFinderDao;
    private UserFinderEventDAO userFinderEventDao;

    public Userfinder getUserfinder(int id) {
        return userFinderDao.get(id);
    }

    public Userfinder updateUserfinder(Userfinder userFinder) {
        return userFinderDao.update(userFinder);
    }

    public void addUserFinder(Userfinder userFinder) {
        userFinderDao.create(userFinder);
    }

    public void addUserFinderEvent(UserfinderEvent userFinderEvent) {
        userFinderEventDao.create(userFinderEvent);
    }

    public void deleteUserFinderEvent(int eventId) {
        userFinderEventDao.delete(eventId);
    }

    public void deleteUserFinder(int userFinderId) {
        userFinderDao.delete(userFinderId);
    }

    public boolean existsUserfinderByProjectIdAndRoleId(int projectId, int productId, int roleId, int excludeUserFinderId) {
        return userFinderDao.existsUserfinderByProjectIdAndRoleId(projectId, productId, roleId, excludeUserFinderId);
    }

    public List<UserfinderVO> getAllUserfinders() {
        List<UserfinderVO> userfinders = userFinderDao.selectAllUserfinders();
        return userfinders;
    }

    public List<UserfinderVO> getAllUserfindersByStatus(int id, int status) {
        return (id > 0) ? userFinderDao.selectAllUserfindersByIdAndStatus(id, status) : userFinderDao.selectAllUserfindersByStatus(status);
    }

    public List<UserfinderVO> getAllUserfindersByStatus(int status) {
        return userFinderDao.selectAllUserfindersByStatus(status);
    }

    public List<UserfinderVO> getAllUserfindersByIdAndStatus(int id, int status) {
        return userFinderDao.selectAllUserfindersByIdAndStatus(id, status);
    }

    public UserfinderEvent getUserFinderEvent(int userfinderId, int userId, Date completionTime) {
        return userFinderEventDao.selectUserFinderEvent(userfinderId, userId, completionTime);
    }

    public List<UserfinderEvent> getUserFinderEvents(int userfinderId, int userId) {
        return userFinderEventDao.selectUserFinderEvents(userfinderId, userId);
    }

    public Pagination<UserfinderVO> getAllUserfindersByProjectId(int projectId, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        long totalCount = userFinderDao.countOfAllUserfindersByProjectId(projectId);
        if (offset < 0) {
            offset = 0;
        }
        List<UserfinderVO> targets = userFinderDao.selectAllUserfindersByProjectId(projectId, sortName, sortOrder, offset, count);

        Pagination<UserfinderVO> pagination = new Pagination<UserfinderVO>(totalCount, page, pageSize);
        pagination.setRows(targets);

        return pagination;
    }

    @Autowired
    public void setUserFinderDao(UserFinderDAO userFinderDao) {
        this.userFinderDao = userFinderDao;
    }

    @Autowired
    public void setUserFinderEventDao(UserFinderEventDAO userFinderEventDao) {
        this.userFinderEventDao = userFinderEventDao;
    }
}
