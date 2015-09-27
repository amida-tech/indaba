/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Announcement;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sjf
 */
public class AnnouncementDAO extends SmartDaoMySqlImpl<Announcement, Integer> {//Integer : 主键是Integer

    private static final Logger logger = Logger.getLogger(AnnouncementDAO.class);

    public List<Announcement> getAllActiveAnnouncements() {
        return find("SELECT * FROM announcement WHERE active = ? and CURDATE() < expiration", true);
    }

    public void saveAnnouncement(Announcement announcement) {
        save(announcement);
    }
}
