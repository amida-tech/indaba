/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ContentVersion;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class ContentVersionDAO extends SmartDaoMySqlImpl<ContentVersion, Integer> {

    private static final Logger logger = Logger.getLogger(ContentVersionDAO.class);
    private static final String SELECT_ALL_CONTENT_VERSIONS_BY_CONTENT_HEADER_ID =
            "SELECT * FROM content_version WHERE content_header_id=? ORDER BY create_time DESC";
    private static final String SELECT_ALL_CONTENT_VERSIONS_BY_HORSE_ID =
            "SELECT cntVer.* FROM content_version cntVer, content_header cntHdr "
            + "WHERE cntHdr.horse_id=? AND cntHdr.id=cntVer.content_header_id ORDER BY cntVer.create_time DESC";

    public List<ContentVersion> getAllContentVersions(int cntHdrId) {
        return super.find(SELECT_ALL_CONTENT_VERSIONS_BY_CONTENT_HEADER_ID, cntHdrId);
    }

    public List<ContentVersion> getAllContentVersionsByHorseId(int horseId) {
        return super.find(SELECT_ALL_CONTENT_VERSIONS_BY_HORSE_ID, horseId);
    }

    public ContentVersion getLastestContentVersionByHorseId(int horseId) {
        return super.findSingle(SELECT_ALL_CONTENT_VERSIONS_BY_HORSE_ID, horseId);
    }

    public ContentVersion getCurrentContentVersion(int cntHdrId) {
        List<ContentVersion> contentVersions = super.find(SELECT_ALL_CONTENT_VERSIONS_BY_CONTENT_HEADER_ID, cntHdrId);
        return (contentVersions != null && contentVersions.size() > 0) ? contentVersions.get(0) : null;
    }

    public ContentVersion getFirstContentVersion(int cntHdrId) {
        List<ContentVersion> contentVersions = super.find(SELECT_ALL_CONTENT_VERSIONS_BY_CONTENT_HEADER_ID, cntHdrId);
        return (contentVersions != null && contentVersions.size() > 0) ? contentVersions.get(contentVersions.size()-1) : null;
    }


    public void saveAnnouncement(ContentVersion cntVer) {
        super.save(cntVer);
    }
}
