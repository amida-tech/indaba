/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.UserfinderEvent;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class UserFinderEventDAO extends SmartDaoMySqlImpl<UserfinderEvent, Integer> {

    private static final Logger log = Logger.getLogger(UserFinderEventDAO.class);
    private static final String SELECT_USERFINDER_EVNET = "SELECT * FROM userfinder_event WHERE userfinder_id=? AND user_id=? AND exe_time >= ?";
    private static final String SELECT_USERFINDER_EVNETS = "SELECT * FROM userfinder_event WHERE userfinder_id=? AND user_id=?";

    public UserfinderEvent selectUserFinderEvent(int userfinderId, int userId, Date completionDate) {
        return super.findSingle(SELECT_USERFINDER_EVNET, userfinderId, userId, completionDate);
    }

    public List<UserfinderEvent> selectUserFinderEvents(int userfinderId, int userId) {
        return super.find(SELECT_USERFINDER_EVNET, new Object[]{userfinderId, userId});
    }
}
