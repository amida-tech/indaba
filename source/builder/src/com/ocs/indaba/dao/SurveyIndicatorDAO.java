/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.vo.SurveyIndicatorView;
import com.ocs.util.ListUtils;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author luwb
 */
public class SurveyIndicatorDAO extends SmartDaoMySqlImpl<SurveyIndicator, Integer> {

    private static final Logger log = Logger.getLogger(SurveyIndicatorDAO.class);

    private static final String SELECT_BY_ITAGS_ID = "SELECT si.* FROM survey_indicator si, indicator_tag it"
            + " WHERE si.status=1 AND si.parent_indicator_id=0 AND si.id = it.survey_indicator_id and it.itags_id =?";

    private static final String SELECT_BY_USER_TAGS = "SELECT si.* FROM survey_indicator si, survey_question sq, survey_answer sa, tag t"
            + " WHERE si.status=1 AND si.parent_indicator_id=0 AND t.tag_type=0 AND t.tagged_object_type=1 AND sa.id=t.tagged_object_id AND"
            + " sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id AND t.label='?'";
    
    private static final String SELECT_BY_ORG_ID = "SELECT * from survey_indicator WHERE status=1 AND si.parent_indicator_id=0 AND owner_org_id = ?";
    
    private static final String SELECT_BY_ORG_ID_AND_IDS = "SELECT * from survey_indicator WHERE status=1 AND si.parent_indicator_id=0 AND owner_org_id=? AND id in ?";

    // 0: org and visibility condition; 1: indicator tag and user tag condition; 2: order by name; 3: order type(asc or desc)
    // Component indicators not included
    private static final String FIND_ALL_INDICATORS = "SELECT si.* FROM survey_indicator si, organization org WHERE si.status=1 AND si.parent_indicator_id=0 AND {0} AND org.id=si.owner_org_id ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";

    private static final String FIND_COUNT_OF_ALL_INDICATORS = "SELECT COUNT(1) FROM survey_indicator si WHERE si.status=1 AND si.parent_indicator_id=0 AND {0}";
    // select by indicator tag
    private static final String FIND_ALL_INDICATORS_BY_ITAG =
            "SELECT si.* FROM survey_indicator si, indicator_tag it WHERE si.status=1 AND si.parent_indicator_id=0 AND {0} AND it.itags_id={1,number,#} AND si.id=it.survey_indicator_id ORDER BY {2} {3} LIMIT {4,number,#},{5,number,#}";

    private static final String FIND_COUNT_OF_ALL_INDICATORS_BY_TAG =
            "SELECT COUNT(1) FROM survey_indicator si, indicator_tag it WHERE si.status=1 AND {0} AND it.itags_id={1,number,#} AND si.id=it.survey_indicator_id";

    // select by user tag
    private static final String FIND_ALL_INDICATORS_BY_UTAG =
            "SELECT si.* FROM survey_indicator si, survey_question sq, survey_answer sa, tag t "
            + "WHERE si.status=1 AND si.parent_indicator_id=0 AND {0} AND t.tag_type=0 AND t.tagged_object_type=1 AND sa.id=t.tagged_object_id AND "
            + "sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id AND t.label LIKE ''{1}'' ORDER BY {2} {3} LIMIT {4,number,#},{5,number,#}";

    private static final String FIND_COUNT_OF_ALL_INDICATORS_BY_UAG =
            "SELECT COUNT(1) FROM survey_indicator si, survey_question sq, survey_answer sa, tag t "
            + "WHERE si.status=1 AND si.parent_indicator_id=0 AND {0} AND t.tag_type=0 AND t.tagged_object_type=1 AND sa.id=t.tagged_object_id AND "
            + "sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id AND t.label LIKE ''{1}''";
    
    // select by user tag and indicator tag. Used by control panel.
    // NOTE: component indicators are NOT included
    private static final String FIND_ALL_INDICATORS_BY_ITAG_AND_UTAG =
            "SELECT si.* FROM survey_indicator si, survey_question sq, survey_answer sa, tag t, indicator_tag it "
            + "WHERE si.status=1 AND si.parent_indicator_id=0 AND {0} AND (it.itags_id={1,number,#} AND si.id=it.survey_indicator_id) AND t.tag_type=0 AND t.tagged_object_type=1 AND sa.id=t.tagged_object_id AND "
            + "sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id AND t.label LIKE ''{2}'' ORDER BY {3} {4} LIMIT {5,number,#},{6,number,#}";

    private static final String FIND_COUNT_OF_ALL_INDICATORS_BY_ITAG_AND_UTAG =
            "SELECT COUNT(1) FROM survey_indicator si, survey_question sq, survey_answer sa, tag t, indicator_tag it "
            + "WHERE si.status=1 AND si.parent_indicator_id=0 AND {0} AND (it.itags_id={1,number,#} AND si.id=it.survey_indicator_id) AND t.tag_type=0 AND t.tagged_object_type=1 AND sa.id=t.tagged_object_id AND "
            + "sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id AND t.label LIKE ''{2}''";

    private static final String SELECT_SURVEY_INDICATOR_BY_NAME = "SELECT * FROM survey_indicator si WHERE si.status=1 AND name=?";
    private static final String UPDATE_SURVEY_INDICATOR_VISIBILITY_BY_ID = "UPDATE survey_indicator SET visibility=? WHERE id=?";
    private static final String UPDATE_SURVEY_INDICATOR_STATE_BY_ID = "UPDATE survey_indicator SET state=? WHERE id=?";
    private static final String UPDATE_SURVEY_INDICATOR_STATUS_BY_ID = "UPDATE survey_indicator SET status=? WHERE id=?";
    private static final String MARK_INDICATOR_DELETEED = "UPDATE survey_indicator SET delete_time=now(), status=3, delete_user_id=?, name=CONCAT('[DELETED-', id, ']', name) WHERE id=?";
    private static final String SELECT_SURVEY_INDICATOR_BY_LANGUAGE = "SELECT * FROM survey_indicator WHERE id=? AND language_id=?";
    private static final String UPDATE_SURVEY_INDICATOR_MOVE_STATE = "UPDATE survey_indicator SET visibility=?, state=?, owner_org_id=? WHERE id=?";
    private static final String SELECT_SURVEY_INDICATOR_BY_CONFIG_ID =
            "SELECT si.* FROM survey_indicator si, survey_question sq WHERE si.id = sq.survey_indicator_id AND sq.survey_config_id=?";
    /*
    private static final String SELECT_ALL_AVAIALABLE_INDICATORS =
    "SELECT si.* FROM survey_indicator si WHERE si.language_id={0} AND si.owner_org_id IN ({1}) AND "
    + "si.id NOT IN(SELECT sq.survey_indicator_id FROM survey_question sq WHERE sq.survey_config_id={2} AND sq.id!={3,number,#}) "
    + "ORDER BY si.name, si.question;";
     */
    private static final String COUNT_SURVEY_INDICATOR_BY_CONFIGID =
            "SELECT COUNT(1) FROM survey_indicator si JOIN sc_indicator sci ON si.id=sci.survey_indicator_id "
            + "WHERE sci.survey_config_id=?";

    private static final String SELECT_ALL_AVAIALABLE_INDICATORS =
            "SELECT si.* FROM survey_indicator si, survey_question sq "
            + "WHERE sq.id=? AND si.id=sq.survey_indicator_id "
            + "UNION DISTINCT "
            + "SELECT si.* FROM survey_indicator si JOIN sc_indicator sci ON (sci.survey_config_id=? AND si.id=sci.survey_indicator_id) "
            + "WHERE si.status=1 AND si.parent_indicator_id=0 AND si.id NOT IN (SELECT sq.survey_indicator_id FROM survey_question sq WHERE sq.survey_config_id=? AND sq.id!=?) "
            + "ORDER BY name";

    private static final String SELECT_SURVEY_INDICATOR_BY_CONFIGID =
            "SELECT si.*, o.id org_id, o.name org_name "
            + "FROM survey_indicator si JOIN sc_indicator sci ON si.id=sci.survey_indicator_id JOIN organization o ON o.id=si.owner_org_id "
            + "WHERE sci.survey_config_id={0,number,#} "
            + "ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";

    private static final String SELECT_USED_SURVEY_INDICATOR_IDS =
            "SELECT survey_indicator_id FROM survey_question WHERE survey_config_id=?";

    private static final String BASE_AVAILABLE_SURVEY_INDICATORS_BY_CONFIGID =
            "FROM survey_indicator si JOIN organization o ON o.id=si.owner_org_id "
            + "WHERE si.status=1 AND si.parent_indicator_id=0 AND si.id NOT IN (SELECT survey_indicator_id FROM sc_indicator WHERE survey_config_id={0,number,#}) {1} ";

    private static final String COUNT_AVAILABLE_SURVEY_INDICATORS_BY_CONFIGID =
            "SELECT COUNT(1) " + BASE_AVAILABLE_SURVEY_INDICATORS_BY_CONFIGID;

    private static final String FIND_AVAILABLE_SURVEY_INDICATORS_BY_CONFIGID =
            "SELECT si.*, o.id org_id, o.name org_name "
            + BASE_AVAILABLE_SURVEY_INDICATORS_BY_CONFIGID
            + " ORDER BY {2} {3} LIMIT {4,number,#},{5,number,#}";
    private static final String SELECT_BY_QUESTION = 
            "SELECT si.* FROM survey_indicator si JOIN survey_question sq ON sq.survey_indicator_id=si.id WHERE sq.id=?";
    
    public SurveyIndicator selectSurveyIndicatorByQuesitonId(int questionId) {
        return super.findSingle(SELECT_BY_QUESTION, questionId);
    }
    
    public long countOfAvailableSurveyIndicatorsByConfigId(int surveyConfigId, int visibility, List<Integer> candidateOrgIds, String name, String question) {
        return super.count(MessageFormat.format(COUNT_AVAILABLE_SURVEY_INDICATORS_BY_CONFIGID, surveyConfigId,
                composeCretireaSql(visibility, candidateOrgIds, name, question)));
    }

    public List<SurveyIndicatorView> selectAvailableSurveyIndicatorsByConfigId(int surveyConfigId, int visibility, List<Integer> candidateOrgIds, String name, String question, String sortName, String sortOrder, int offset, int count) {
        if ("question".equalsIgnoreCase(sortName)) {
            sortName = "si.question";
        } else if ("org".equalsIgnoreCase(sortName)) {
            sortName = "o.name";
        } else {
            sortName = "si.name";
        }

        String sqlStr = MessageFormat.format(FIND_AVAILABLE_SURVEY_INDICATORS_BY_CONFIGID, surveyConfigId,
                composeCretireaSql(visibility, candidateOrgIds, name, question), sortName, sortOrder, offset, count);
        return super.getJdbcTemplate().query(sqlStr, new SurveyIndicatorViewMapper());
    }

    public long countOfSurveyIndicatorsByConfigId(int surveyConfigId) {
        return super.count(COUNT_SURVEY_INDICATOR_BY_CONFIGID, surveyConfigId);
    }

    public List<SurveyIndicatorView> selectSurveyIndicatorsByConfigId(int surveyConfigId, String sortName, String sortOrder, int offset, int count) {
        if ("question".equalsIgnoreCase(sortName)) {
            sortName = "si.question";
        } else if ("org".equalsIgnoreCase(sortName)) {
            sortName = "o.name";
        } else {
            sortName = "si.name";
        }
        String sqlStr = MessageFormat.format(SELECT_SURVEY_INDICATOR_BY_CONFIGID, surveyConfigId, sortName, sortOrder, offset, count);
        return super.getJdbcTemplate().query(sqlStr, new SurveyIndicatorViewMapper());
    }

    public List<Integer> selectUsedSurveyIndicatorIds(int surveyConfigId) {
        return getJdbcTemplate().query(SELECT_USED_SURVEY_INDICATOR_IDS, new Object[]{surveyConfigId}, getIdRowMapper("survey_indicator_id"));
    }

    public List<SurveyIndicator> selectAvailableScIndicators(int surveyConfigId, int withQstnId) {
        return super.find(SELECT_ALL_AVAIALABLE_INDICATORS, (Object) withQstnId, surveyConfigId, surveyConfigId, withQstnId);
    }
    /*
    public List<SurveyIndicator> selectAllAvailableSurveyIndicators(int languageId, List<Integer> orgIds, int surveyConfigId, int withQuestionId) {
    return super.find(MessageFormat.format(SELECT_ALL_AVAIALABLE_INDICATORS, languageId, ListUtils.listToString(orgIds), surveyConfigId, withQuestionId));
    }
     */

    public SurveyIndicator selectSurveyIndicatorById(int surveyIndicatorId) {
        return super.get(surveyIndicatorId);
    }

    public SurveyIndicator selectSurveyIndicatorById(int id, int languageId) {
        return super.findSingle(SELECT_SURVEY_INDICATOR_BY_LANGUAGE, id, languageId);
    }

    public List<SurveyIndicator> selectSurveyIndicatorByItagsId(int tagId) {
        return super.find(SELECT_BY_ITAGS_ID, tagId);
    }

    public List<SurveyIndicator> selectSurveryIndicatorByUserTag(String userTag) {
        return super.find(SELECT_BY_USER_TAGS, userTag);
    }

    public List<SurveyIndicator> selectSurveyIndicatorByOrgId(int orgId) {
        return super.find(SELECT_BY_ORG_ID, orgId);
    }

    public List<SurveyIndicator> selectSurveyIndicatorByOrdIdAndIds(int orgId, List<Integer> ids) {
        String strIds = ids.toString().replace("[", "(").replace("]", ")");
        log.debug("select survey indicators by orgId " + orgId + " and ids " + strIds);
        return super.find(SELECT_BY_ORG_ID_AND_IDS, orgId, strIds);
    }

    public List<SurveyIndicator> findAllIndicatorsByITagAndUTag(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String userTag, String sortName, String sortOrder, int offset, int count, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_ALL_INDICATORS_BY_ITAG_AND_UTAG,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query),
                iTagId, userTag, fixSortName(sortName), sortOrder, offset, count);

        log.debug("Select all of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",state=" + state + ",userTag=" + userTag
                + ",sortName=" + sortName + ",sortOrder=" + sortName
                + ",offset=" + offset + ",count=" + count + "]:" + sqlStr);

        return super.find(sqlStr);
    }

    public long findCountOfAllIndicatorsByITagAndUTag(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String userTag, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_COUNT_OF_ALL_INDICATORS_BY_ITAG_AND_UTAG,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query), iTagId, userTag);

        log.debug("Select all of count of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId=" + orgId + ",visibility=" + visibility
                + ",state=" + state + ",userTag=" + userTag + "]:" + sqlStr);

        return super.count(sqlStr);
    }

    public List<SurveyIndicator> findAllIndicatorsByUTag(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, String userTag, String sortName, String sortOrder, int offset, int count, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_ALL_INDICATORS_BY_UTAG,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query),
                userTag, fixSortName(sortName), sortOrder, offset, count);

        log.debug("Select all of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",state=" + state + ",userTag=" + userTag
                + ",sortName=" + sortName + ",sortOrder=" + sortName
                + ",offset=" + offset + ",count=" + count + "]:" + sqlStr);

        return super.find(sqlStr);
    }

    public long findCountOfAllIndicatorsByUTag(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, String userTag, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_COUNT_OF_ALL_INDICATORS_BY_UAG,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query), userTag);

        log.debug("Select all of count of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId=" + orgId + ",visibility=" + visibility
                + ",state=" + state + ",userTag=" + userTag + "]:" + sqlStr);

        return super.count(sqlStr);
    }

    public List<SurveyIndicator> findAllIndicatorsByITag(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String sortName, String sortOrder, int offset, int count, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_ALL_INDICATORS_BY_ITAG,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query),
                iTagId, fixSortName(sortName), sortOrder, offset, count);

        log.debug("Select all of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",state=" + state + ",iTagId=" + iTagId
                + ",sortName=" + sortName + ",sortOrder=" + sortName
                + ",offset=" + offset + ",count=" + count + "]:" + sqlStr);

        return super.find(sqlStr);
    }

    public long findCountOfAllIndicatorsByITag(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_COUNT_OF_ALL_INDICATORS_BY_TAG,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query), iTagId);

        log.debug("Select all of count of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",state=" + state + "]:" + sqlStr);

        return super.count(sqlStr);
    }

    public List<SurveyIndicator> findAllIndicators(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, String sortName, String sortOrder, int offset, int count, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_ALL_INDICATORS,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query),
                fixSortName(sortName), sortOrder, offset, count);

        log.debug("Select all of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",state=" + state + ",sortName=" + sortName + ",sortOrder=" + sortName
                + ",offset=" + offset + ",count=" + count + "]:" + sqlStr);
        return super.find(sqlStr);
    }

    public long findCountOfAllIndicators(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, String queryType, String query) {
        String sqlStr = MessageFormat.format(FIND_COUNT_OF_ALL_INDICATORS,
                generateQuestionConditionForFindAll(oaOfOrgIds, orgId, visibility, state, queryType, query));

        log.debug("Select all of count of indicators[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",state=" + state + "]:" + sqlStr);
        return super.count(sqlStr);
    }

    private static String fixSortName(String sortName) {
        if ("name".equalsIgnoreCase(sortName)) {
            sortName = "si.name";
        } else if ("title".equalsIgnoreCase(sortName) || "question".equalsIgnoreCase(sortName)) {
            sortName = "si.question";
        } else if ("type".equalsIgnoreCase(sortName) || "typeName".equalsIgnoreCase(sortName)) {
            sortName = "si.answer_type";
        } else if ("orgName".equalsIgnoreCase(sortName) || "organization".equalsIgnoreCase(sortName)) {
            sortName = "org.name";
        } else {
            sortName = "si.name";
        }
        return sortName;
    }

    private static String escapeQuotes(String input) {
        return input.replaceAll("\"", "\\\\\"");
    }

    private static String generateQuestionConditionForFindAll(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, String queryType, String query) {
        StringBuilder sb = new StringBuilder();
        String cond = generateQueryConditionStatement(oaOfOrgIds, orgId, visibility, state);

        if (cond != null && !cond.isEmpty()) {
            sb.append(cond);
        } else {
            sb.append("(1=1)");
        }

        if (state > 0) {
            sb.append(" AND si.state=").append(state);
        }

        if (queryType != null && query != null && !query.isEmpty()) {
            if (queryType.equalsIgnoreCase("name")) {
                sb.append(" AND si.name LIKE \"%").append(escapeQuotes(query)).append("%\"");
            } else if (queryType.equalsIgnoreCase("question")) {
                sb.append(" AND si.question LIKE \"%").append(escapeQuotes(query)).append("%\"");
            }
        }

        return sb.toString();
    }

    private static String generateQueryConditionStatement(List<Integer> oaOfOrgIds, int orgId, int visibility, int state) {
        StringBuilder sb = new StringBuilder();
        String orgs = null;
        if (oaOfOrgIds != null && !oaOfOrgIds.isEmpty()) {
            StringBuilder orgIds = new StringBuilder("(");
            for (int v : oaOfOrgIds) {
                orgIds.append(v).append(',');
            }
            orgIds.setLength(orgIds.length() - 1);
            orgIds.append(')');
            orgs = orgIds.toString();
        } else if (oaOfOrgIds != null) {
            orgs = "(-1)";
        } else {
            orgs = null;
        }

        if (visibility > 0 && orgId > 0) {
            // visibility is specified and org specified
            sb.append("si.visibility=").append(visibility);

            if (visibility == Constants.VISIBILITY_PRIVATE) {
                if (orgs == null || oaOfOrgIds.contains(orgId)) {
                    // user has access to the org
                    sb.append(" AND si.owner_org_id=").append(orgId);
                } else {
                    // user has no access to private indicators of the org
                    sb.append(" AND (1=0)");
                }
            } else {
                // public indicators
                if (state == Constants.RESOURCE_STATE_TEST && orgs != null && !oaOfOrgIds.contains(orgId)) {
                    // user only has access to his own test indicators
                    sb.append(" AND (1=0)");
                } else {
                    // user has access to public indicators of the org
                    sb.append(" AND si.owner_org_id=").append(orgId);
                }
            }
        } else if (visibility > 0 && orgId <= 0) {
            // visibility spceified, org is any
            sb.append("si.visibility=").append(visibility);

            if (orgs != null) {
                // user is not super user
                if (visibility == Constants.VISIBILITY_PRIVATE || state == Constants.RESOURCE_STATE_TEST) {
                    // user only has access to private indicators of orgs he is OA of
                    sb.append(" AND si.owner_org_id IN ").append(orgs);
                }
            }
        } else if (visibility <= 0 && orgId > 0) {
            // visibility is any, org specified
            sb.append("si.owner_org_id=").append(orgId);

            if (orgs != null && !oaOfOrgIds.contains(orgId)) {
                // the user has no authority to the specified org. Only public non-test indicators are available.
                sb.append(" AND si.state != ").append(Constants.RESOURCE_STATE_TEST).append(" AND si.visibility=").append(Constants.VISIBILITY_PUBLIC);
            } else {
                // either user is spuer user or user has access to the specified org
                // user can access all indicators of this org
            }
        } else {
            // org is any, visibility is any
            if (orgs != null) {
                // non super user: can access all public indicators and private indicators of his own
                sb.append("(si.owner_org_id IN ").append(orgs).append(" OR (si.state !=").append(Constants.RESOURCE_STATE_TEST).append(" AND si.visibility=").append(Constants.VISIBILITY_PUBLIC).append("))");
            }
        }
        return sb.toString();
    }

    public SurveyIndicator selectSurveyIndicatorByName(String name) {
        return super.findSingle(SELECT_SURVEY_INDICATOR_BY_NAME, name);
    }

    public void updateVisibilityById(short visibility, int id) {
        Object[] values = new Object[]{visibility, id};
        super.update(UPDATE_SURVEY_INDICATOR_VISIBILITY_BY_ID, values);
    }

    public void updateStateById(short state, int id) {
        Object[] values = new Object[]{state, id};
        super.update(UPDATE_SURVEY_INDICATOR_STATE_BY_ID, values);
    }

    public void updateStatusById(short status, int id) {
        Object[] values = new Object[]{status, id};
        super.update(UPDATE_SURVEY_INDICATOR_STATUS_BY_ID, values);
    }

    public void updateMoveState(int indicatorId, int visibility, int state, int ownerOrgId) {
        Object[] values = new Object[]{visibility, state, ownerOrgId, indicatorId};
        super.update(UPDATE_SURVEY_INDICATOR_MOVE_STATE, values);
    }

    public void markIndicatorDeleted(int indicatorId, int userId) {
        super.update(MARK_INDICATOR_DELETEED, userId, indicatorId);
    }

    public List<SurveyIndicator> selectSurveyIndicatorByConfigId(int surveyConfigId) {
        return super.find(SELECT_SURVEY_INDICATOR_BY_CONFIG_ID, surveyConfigId);
    }

    private String composeCretireaSql(int visibility, List<Integer> candidateOrgIds, String name, String question) {
        StringBuilder sBuf = new StringBuilder();
        sBuf.append(" AND si.visibility=").append(visibility);

        if (visibility == Constants.VISIBILITY_PRIVATE) {
            sBuf.append(" AND (si.owner_org_id IN (").append(ListUtils.listToString(candidateOrgIds)).append("))");
        }

        if (!StringUtils.isEmpty(name)) {
            sBuf.append(" AND si.name LIKE '%").append(name.replaceAll("'", "''")).append("%' ");
        }

        if (!StringUtils.isEmpty(question)) {
            sBuf.append(" AND si.question LIKE '%").append(question.replaceAll("'", "''")).append("%' ");
        }
        return sBuf.toString();
    }

    class SurveyIndicatorViewMapper implements RowMapper {

        public SurveyIndicatorView mapRow(ResultSet rs, int rowNum) throws SQLException {
            SurveyIndicatorView si = new SurveyIndicatorView();
            si.setIndicatorId(rs.getInt("si.id"));
            si.setName(rs.getString("si.name"));
            si.setQuestion(rs.getString("si.question"));
            si.setOrgId(rs.getInt("org_id"));
            si.setOrgName(rs.getString("org_name"));
            return si;
        }
    }


    private static final String SELECT_COMPONENT_INDICATORS_BY_PARENT_ID =
            "SELECT * FROM survey_indicator WHERE parent_indicator_id=?";

    public List<SurveyIndicator> selectComponentIndicators(int parentIndicatorId) {
        return super.find(SELECT_COMPONENT_INDICATORS_BY_PARENT_ID, parentIndicatorId);
    }
    
    private static final String SELECT_COMPONENT_INDICATORS_BY_QUESTION_ID =
            "SELECT si.* FROM survey_indicator si, survey_question sq " +
            "WHERE sq.id=? AND si.parent_indicator_id=sq.survey_indicator_id";

    public List<SurveyIndicator> selectComponentIndicatorsOfQuestion(int questionId) {
        return super.find(SELECT_COMPONENT_INDICATORS_BY_QUESTION_ID, questionId);
    }

    private static final String SELECT_INDICATOR_OF_QUESTION =
            "SELECT si.* FROM survey_indicator si, survey_question sq " +
            "WHERE sq.id=? AND si.id=sq.survey_indicator_id";

    public SurveyIndicator selectSurveyIndicatorByQuestionId(int questionId) {
        return super.findSingle(SELECT_INDICATOR_OF_QUESTION, questionId);
    }

    static private final String DELETE_COMPONENT_INDICATORS =
            "DELETE FROM survey_indicator WHERE parent_indicator_id=?";

    static private final String DELETE_COMPONENT_INDICATORS_INTL =
            "DELETE sii FROM survey_indicator si, survey_indicator_intl sii " +
            "WHERE si.parent_indicator_id=? AND sii.survey_indicator_id=si.id";

    static private final String DELETE_COMPONENT_ATI =
            "DELETE at FROM survey_indicator si, answer_type_integer at " +
            "WHERE si.parent_indicator_id=? AND si.answer_type=3 AND at.id=si.answer_type_id";

    static private final String DELETE_COMPONENT_ATF =
            "DELETE at FROM survey_indicator si, answer_type_float at " +
            "WHERE si.parent_indicator_id=? AND si.answer_type=4 AND at.id=si.answer_type_id";

    static private final String DELETE_COMPONENT_ATT =
            "DELETE at FROM survey_indicator si, answer_type_text at " +
            "WHERE si.parent_indicator_id=? AND si.answer_type=5 AND at.id=si.answer_type_id";

    static private final String DELETE_COMPONENT_ATC =
            "DELETE atc, atcc FROM survey_indicator si, answer_type_choice atc, atc_choice atcc " +
            "WHERE si.parent_indicator_id=? AND (si.answer_type=1 OR si.answer_type=2) AND atc.id=si.answer_type_id " +
            "AND atcc.answer_type_choice_id=atc.id";

    static private final String DELETE_COMPONENT_ATC_INTL =
            "DELETE atci FROM survey_indicator si, atc_choice atcc, atc_choice_intl atci " +
            "WHERE si.parent_indicator_id=? AND (si.answer_type=1 OR si.answer_type=2)  " +
            "AND atcc.answer_type_choice_id=si.answer_type_id AND atci.atc_choice_id=atcc.id";


    public void deleteComponentIndicators(int mainIndicatorId) {
        if (mainIndicatorId <= 0) return;
        super.delete(DELETE_COMPONENT_ATI, mainIndicatorId);
        super.delete(DELETE_COMPONENT_ATF, mainIndicatorId);
        super.delete(DELETE_COMPONENT_ATT, mainIndicatorId);
        super.delete(DELETE_COMPONENT_ATC_INTL, mainIndicatorId);
        super.delete(DELETE_COMPONENT_ATC, mainIndicatorId);
        super.delete(DELETE_COMPONENT_INDICATORS_INTL, mainIndicatorId);
        super.delete(DELETE_COMPONENT_INDICATORS, mainIndicatorId);
    }

}
