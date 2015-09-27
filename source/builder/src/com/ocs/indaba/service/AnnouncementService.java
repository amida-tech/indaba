/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.AnnouncementDAO;
import com.ocs.indaba.po.Announcement;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sjf
 */
public class AnnouncementService {

    private static final Logger log = Logger.getLogger(AnnouncementService.class);
    private AnnouncementDAO announcementDao = new AnnouncementDAO();

    public List<Announcement> getAnnouncementByProject() {
        return announcementDao.getAllActiveAnnouncements();
    }

    @Autowired
    public void setAnnouncementDao(AnnouncementDAO announcementDao) {
        this.announcementDao = announcementDao;
    }
}
