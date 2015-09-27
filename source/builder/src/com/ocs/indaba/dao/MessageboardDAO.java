/**
 * 
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Msgboard;

/**
 * @author Tiger Tang
 *
 */
public class MessageboardDAO extends SmartDaoMySqlImpl<Msgboard, Integer> {

	public Msgboard getMsgboardByUser(int uid) {
		return findSingle("SELECT FROM messageboard WHERE ", uid);
	}

}
