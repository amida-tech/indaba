/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.INTEGER;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Rule;

/**
 * Operation on User table by accessing DB
 * 
 * @author Luke
 * 
 */
public class RuleDAO extends SmartDaoMySqlImpl<Rule, Integer> {

    private static final Logger log = Logger.getLogger(SequenceObjectDAO.class);
    private static final String SELECT_ENTRANCE_RULE_BY_GOALID =
            "SELECT rule.* FROM rule, goal WHERE goal.id=? AND rule.id=goal.entrance_rule_id";
    private static final String SELECT_INFLIGHT_RULE_BY_GOALID =
            "SELECT rule.* FROM rule, goal WHERE goal.id=? AND rule.id=goal.inflight_rule_id";
    private static final String SELECT_EXIT_RULE_BY_GOALID =
            "SELECT rule.* FROM rule, goal WHERE goal.id=? AND rule.id=goal.exit_rule_id";
    
    /**
     * Select all duration of the specified goal id.
     * @param horseId
     * @return duration days
     */
    public Rule selectEntranceRuleByGoalId(int goalId) {
        log.debug("Select table Rule: " + SELECT_ENTRANCE_RULE_BY_GOALID + "[goalId=" + goalId + "].");

        RowMapper mapper = new RuleRowMapper();
        List<Rule> rules = getJdbcTemplate().query(SELECT_ENTRANCE_RULE_BY_GOALID,
                new Object[]{goalId},
                mapper);
        return rules.get(0);
    }

    public Rule selectInFlightRuleByGoalId(int goalId) {
        log.debug("Select table Rule: " + SELECT_INFLIGHT_RULE_BY_GOALID + "[goalId=" + goalId + "].");

        RowMapper mapper = new RuleRowMapper();
        List<Rule> rules = getJdbcTemplate().query(SELECT_INFLIGHT_RULE_BY_GOALID,
                new Object[]{goalId},
                mapper);
        return rules.get(0);
    }

    public Rule selectExitRuleByGoalId(int goalId) {
        log.debug("Select table Rule: " + SELECT_EXIT_RULE_BY_GOALID + "[goalId=" + goalId + "].");

        RowMapper mapper = new RuleRowMapper();
        List<Rule> rules = getJdbcTemplate().query(SELECT_EXIT_RULE_BY_GOALID,
                new Object[]{goalId},
                mapper);
        return rules.get(0);
    }
}

class RuleRowMapper implements RowMapper {

    public Rule mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rule rule = new Rule();
        rule.setId(rs.getInt("id"));
        rule.setName(rs.getString("name"));
        rule.setRuleType((short)rs.getInt("rule_type"));
        rule.setDescription(rs.getString("description"));
        rule.setFileName(rs.getString("file_name"));
        return rule;
    }
}



