/**
 * 
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.MsgReadingStatus;
import org.apache.log4j.Logger;

/**
 * @author Tiger Tang
 *
 */
public class MsgReadingStatusDAO extends SmartDaoMySqlImpl<MsgReadingStatus, Integer> {

    private static final Logger logger = Logger.getLogger(MsgReadingStatusDAO.class);

	public MsgReadingStatus getMsgReadingStatus(int messageId, int uid) {
                logger.debug("Get msg reading status for msgId=" + messageId + " userId=" + uid);
		MsgReadingStatus result = findSingle("select * from msg_reading_status where message_id = ? and user_id = ?", messageId, uid);
                logger.debug("Got msg reading status for msgId=" + messageId + " userId=" + uid);
                return result;
	}

}
