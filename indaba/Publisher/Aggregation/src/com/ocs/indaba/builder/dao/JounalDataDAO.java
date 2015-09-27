/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import com.ocs.indaba.aggregation.vo.JournalContentObjectInfo;
import com.ocs.indaba.aggregation.vo.JournalReview;
import com.ocs.indaba.aggregation.vo.JournalSummaryInfo;
import static java.sql.Types.INTEGER;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.JournalContentObject;
import com.ocs.indaba.util.ResultSetUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author jiangjeff
 */
public class JounalDataDAO extends SmartDaoMySqlImpl<JournalContentObject, Integer> {

    private static final Logger log = Logger.getLogger(JounalDataDAO.class);
    private static final String SELECT_JOURNAL_DATA_BY_HORSE_ID = "SELECT jco.id journal_content_object_id, h.id horse_id, t.id target_id,t.short_name target_short_name, t.name target_name, prd.id product_id, prd.name product_name, jco.body, ch.create_time "
            + "FROM horse h, target t, product prd, content_header ch, journal_content_object jco "
            + "WHERE h.id=? AND h.target_id=t.id AND h.product_id=prd.id AND prd.content_type=1 AND h.id=ch.horse_id AND ch.content_object_id=jco.id ";
    private static final String SELECT_JOURNAL_PEER_REVIEW_BY_CONTENT_OBJECT_ID = "SELECT jpr.opinions, jpr.last_change_time, jpr.submit_time, u.id user_id, u.username, u.first_name, u.last_name "
            + "FROM journal_peer_review jpr, user u WHERE jpr.journal_content_object_id=? AND jpr.submit_time IS NOT NULL AND jpr.reviewer_user_id=u.id";
    private static final String SELECT_JOURNAL_SUMMARY_BY_PRODUCT_ID_AND_PROJECT_ID = "select A.code_name as project_name, B.name as organization_name,C.name as product_name,D.name as study_period_name "
            + "from(select code_name,organization_id,study_period_id from project where id = ?) as A, organization as B,"
            + "(select name from product where id = ?) as C, study_period as D where A.organization_id = B.id and D.id = A.study_period_id";
    private static final String SELECT_JOURNAL_SUMMARY_BY_PRODUCT_ID = "SELECT proj.code_name project_name, org.name organization_name, prod.name product_name, sp.name study_period_name "
            + "FROM product prod, project proj, organization org, study_period sp "
            + "WHERE prod.id=? AND proj.id=prod.project_id AND proj.organization_id=org.id AND sp.id=proj.study_period_id";
    private static final String SELECT_TARGET_NAME_LIST_BY_PRODUCT_ID = "select  name as long_name, short_name as short_name from target where id in (select target_id from horse where product_id = ?)";
    private static final String SELECT_AUTHOR_NAME_BY_HORSE_ID = "select u.first_name, u.last_name from user u, content_header ch where u.id = ch.author_user_id AND ch.horse_id = ?";
    private static final String SELECT_TARGET_NAME_BY_TARGET_ID = "select  name as long_name, short_name from target where id = ?";

    public JournalContentObjectInfo selectJournalInfoByHorseId(int horseId) {
        logger.debug("Select indicatorss by category id: " + horseId);
        RowMapper mapper = new RowMapper() {

            public JournalContentObjectInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                JournalContentObjectInfo journal = new JournalContentObjectInfo();
                journal.setId(ResultSetUtil.getInt(rs, "journal_content_object_id"));
                journal.setHorseId(ResultSetUtil.getInt(rs, "horse_id"));
                journal.setTargetId(ResultSetUtil.getInt(rs, "target_id"));
                journal.setTargetName(ResultSetUtil.getString(rs, "target_name"));
                journal.setTargetShortName(ResultSetUtil.getString(rs, "target_short_name"));
                journal.setProductId(ResultSetUtil.getInt(rs, "product_id"));
                journal.setProductName(ResultSetUtil.getString(rs, "product_name"));
                journal.setBody(ResultSetUtil.getString(rs, "body"));
                journal.setCreation(ResultSetUtil.getDate(rs, "create_time"));
                return journal;
            }
        };
        List<JournalContentObjectInfo> list = getJdbcTemplate().query(
                SELECT_JOURNAL_DATA_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public List<JournalReview> selectJournalPeerReviewByContentObjectId(int contentObjectId) {
        logger.debug("Select journal peer review by content object id: " + contentObjectId);
        RowMapper mapper = new RowMapper() {

            public JournalReview mapRow(ResultSet rs, int rowNum) throws SQLException {
                JournalReview review = new JournalReview();
                review.setComment(ResultSetUtil.getString(rs, "opinions"));
                review.setUserId(ResultSetUtil.getInt(rs, "user_id"));
                review.setUserName(ResultSetUtil.getString(rs, "username"));
                review.setFirstName(ResultSetUtil.getString(rs, "first_name"));
                review.setLastName(ResultSetUtil.getString(rs, "last_name"));
                review.setLastUpdated(ResultSetUtil.getDate(rs, "last_change_time"));
                review.setSubmitTime(ResultSetUtil.getDate(rs, "submit_time"));
                return review;
            }
        };
        return getJdbcTemplate().query(
                SELECT_JOURNAL_PEER_REVIEW_BY_CONTENT_OBJECT_ID,
                new Object[]{contentObjectId},
                new int[]{INTEGER},
                mapper);
    }

    public JournalSummaryInfo selectJournalInfoByProductIdandProjectId(int productId, int projectId) {
        logger.debug("Select journal summary by product id: " + productId + " and project id" + projectId);
        RowMapper mapper = new RowMapper() {

            public JournalSummaryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                JournalSummaryInfo journal = new JournalSummaryInfo();
                journal.setProjectName(rs.getString("project_name"));
                journal.setOrganizationName(rs.getString("organization_name"));
                journal.setProductName(rs.getString("product_name"));
                journal.setStudyPeriod(rs.getString("study_period_name"));
                return journal;
            }
        };
        List<JournalSummaryInfo> list = getJdbcTemplate().query(
                SELECT_JOURNAL_SUMMARY_BY_PRODUCT_ID_AND_PROJECT_ID,
                new Object[]{projectId, productId},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public JournalSummaryInfo selectJournalInfoByProductId(int productId) {
        logger.debug("Select journal summary by product id: " + productId);
        RowMapper mapper = new RowMapper() {

            public JournalSummaryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                JournalSummaryInfo journal = new JournalSummaryInfo();
                journal.setProjectName(rs.getString("project_name"));
                journal.setOrganizationName(rs.getString("organization_name"));
                journal.setProductName(rs.getString("product_name"));
                journal.setStudyPeriod(rs.getString("study_period_name"));
                return journal;
            }
        };
        List<JournalSummaryInfo> list = getJdbcTemplate().query(
                SELECT_JOURNAL_SUMMARY_BY_PRODUCT_ID,
                new Object[]{productId},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public List<String> selectTargetNameListByProductI(int productId) {
        logger.debug("Select target name listby product id: " + productId);
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String result = "";
                result += rs.getString("long_name") + ":" + rs.getString("short_name");
                return result;
            }
        };
        return getJdbcTemplate().query(
                SELECT_TARGET_NAME_LIST_BY_PRODUCT_ID,
                new Object[]{productId},
                mapper);

    }

    public String selectTargetNameByTargetId(int targetId) {
        logger.debug("Select target name by targett id: " + targetId);
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String result = "";
                result += rs.getString("long_name") + ":" + rs.getString("short_name");
                return result;
            }
        };
        List<String> name = getJdbcTemplate().query(SELECT_TARGET_NAME_BY_TARGET_ID,
                new Object[]{targetId},
                mapper);
        return name != null ? name.get(0) : "";

    }

    public String getAuthorNamebyHorseId(int horseId) {

        logger.debug("Select author name by horse id: " + horseId);
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                return rs.getString("first_name") + " " + rs.getString("last_name");
            }
        };
        List<String> list = getJdbcTemplate().query(
                SELECT_AUTHOR_NAME_BY_HORSE_ID,
                new Object[]{horseId},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
