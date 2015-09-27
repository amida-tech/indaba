/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.GroupobjFlag;
import com.ocs.indaba.vo.FlagWorkView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import static java.sql.Types.INTEGER;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author yc06x
 */
public class GroupobjFlagDAO extends SmartDaoMySqlImpl<GroupobjFlag, Integer>  {


    private static final String SELECT_ACTIVE_FLAGS_BY_GROUPOBJ_AND_USER =
            "SELECT * FROM groupobj_flag WHERE groupobj_id=? AND unset_time is NULL AND (assigned_user_id=? OR raise_user_id=?)";

    public List<GroupobjFlag> selectActiveFlagsByGroupobjAndUser(int groupobjId, int userId) {
        return super.find(SELECT_ACTIVE_FLAGS_BY_GROUPOBJ_AND_USER, (long)groupobjId, userId, userId);
    }

    private static final String SELECT_STANDING_FLAG =
            "SELECT * FROM groupobj_flag WHERE groupobj_id=? AND unset_time IS NULL";

    public GroupobjFlag getStandingFlag(int groupobjId) {
        return super.findSingle(SELECT_STANDING_FLAG, groupobjId);
    }

    private static final String SELECT_ACTIVE_FLAGS_BY_GROUPOBJ =
            "SELECT * FROM groupobj_flag WHERE groupobj_id=? AND unset_time IS NULL";

    public List<GroupobjFlag> selectActiveFlagsByGroupobj(int groupobjId) {
        return super.find(SELECT_ACTIVE_FLAGS_BY_GROUPOBJ, groupobjId);
    }


    private static final String SELECT_ACTIVE_FLAGS_ASSIGNED_TO_USER =
            "SELECT f.* FROM groupobj_flag f, groupobj g, groupdef def " +
            "WHERE def.id=g.groupdef_id AND def.enabled != 0 AND g.id=f.groupobj_id AND g.horse_id=? AND f.assigned_user_id=? AND f.unset_time IS NULL";

    public List<GroupobjFlag> selectActiveFlagsAssignedToUser(int horseId, int userId) {
        return super.find(SELECT_ACTIVE_FLAGS_ASSIGNED_TO_USER, (long)horseId, userId);
    }

    private static final String SELECT_ALL_ACTIVE_FLAGS_ASSIGNED_TO_USER =
            "SELECT f.* FROM groupobj_flag f, groupobj g, groupdef def " +
            "WHERE def.id=g.groupdef_id AND def.enabled != 0 AND g.id=f.groupobj_id AND f.assigned_user_id=? AND f.unset_time IS NULL";

    public List<GroupobjFlag> selectAllActiveFlagsAssignedToUser(int userId) {
        return super.find(SELECT_ALL_ACTIVE_FLAGS_ASSIGNED_TO_USER, userId);
    }


    private static final String SELECT_ACTIVE_FLAGS_RAISED_BY_USER =
            "SELECT f.* FROM groupobj_flag f, groupobj g, groupdef def " +
            "WHERE def.id=g.groupdef_id AND def.enabled != 0 AND g.id=f.groupobj_id AND g.horse_id=? AND f.raise_user_id=? AND f.unset_time IS NULL";

    public List<GroupobjFlag> selectActiveFlagsRaisedByUser(int horseId, int userId) {
        return super.find(SELECT_ACTIVE_FLAGS_RAISED_BY_USER, (long)horseId, userId);
    }


    private static final String SELECT_ALL_ACTIVE_FLAGS_RAISED_BY_USER =
            "SELECT f.* FROM groupobj_flag f, groupobj g, groupdef def " +
            "WHERE def.id=g.groupdef_id AND def.enabled != 0 AND g.id=f.groupobj_id AND f.raise_user_id=? AND f.unset_time IS NULL";

    public List<GroupobjFlag> selectAllActiveFlagsRaisedByUser(int userId) {
        return super.find(SELECT_ALL_ACTIVE_FLAGS_RAISED_BY_USER, userId);
    }


    private static final String SELECT_ACTIVE_FLAG_WORK_VIEWS_RAISED_BY_ME =
            "SELECT DISTINCT f.id flagId, f.permissions perm, f.groupobj_id groupobjId, go.survey_question_id, f.issue_description issue, f.respond_time, f.raise_user_id, f.assigned_user_id " +
            "FROM groupobj go, groupobj_flag f, groupdef def " +
            "WHERE go.horse_id=? AND f.groupobj_id=go.id AND f.unset_time IS NULL " +
            "AND def.id=go.groupdef_id AND def.enabled != 0 AND f.raise_user_id=?";



    public List<FlagWorkView> selectActiveFlagWorkViewsRaisedByMe(int horseId, final int userId) {
        List<FlagWorkView> list = getJdbcTemplate().query(
                SELECT_ACTIVE_FLAG_WORK_VIEWS_RAISED_BY_ME,
                new Object[]{horseId, userId},
                new int[]{INTEGER, INTEGER},
                new FlagWorkRowMapper());

        if (list != null) {
            for (FlagWorkView view : list) {
                view.setFlagType(FlagWorkView.FLAG_TYPE_RAISED_BY_ME);
            }
        }

        return list;
    }


    private static final String SELECT_FLAG_WORK_VIEW =
            "SELECT DISTINCT f.id flagId, f.permissions perm, f.groupobj_id groupobjId, go.survey_question_id, f.issue_description issue, f.respond_time, f.raise_user_id, f.assigned_user_id " +
            "FROM groupobj go, groupobj_flag f " +
            "WHERE f.id=? AND f.groupobj_id=go.id";

    public FlagWorkView getFlagWorkView(int flagId, int userId) {
        List<FlagWorkView> list = getJdbcTemplate().query(
                SELECT_FLAG_WORK_VIEW,
                new Object[]{flagId},
                new int[]{INTEGER},
                new FlagWorkRowMapper());

        if (list != null) {
            processFlagViews(list, userId);
            return list.get(0);
        }
        return null;
    }
    

    private static final String SELECT_ACTIVE_FLAG_WORK_VIEWS_ASSIGNED_TO_ME =
            "SELECT DISTINCT f.id flagId, f.permissions perm, f.groupobj_id groupobjId, go.survey_question_id, f.issue_description issue, f.respond_time, f.raise_user_id, f.assigned_user_id " +
            "FROM groupobj go, groupobj_flag f, groupdef def " +
            "WHERE go.horse_id=? AND f.groupobj_id=go.id AND f.unset_time IS NULL " +
            "AND def.id=go.groupdef_id AND def.enabled != 0 AND f.assigned_user_id=?";

    public List<FlagWorkView> selectActiveFlagWorkViewsAssignedToMe(int horseId, int userId) {
        List<FlagWorkView> list = getJdbcTemplate().query(
                SELECT_ACTIVE_FLAG_WORK_VIEWS_ASSIGNED_TO_ME,
                new Object[]{horseId, userId},
                new int[]{INTEGER, INTEGER},
                new FlagWorkRowMapper());

        if (list != null) {
            for (FlagWorkView view : list) {
                view.setFlagType(FlagWorkView.FLAG_TYPE_ASSIGNED_TO_ME);
            }
        }

        return list;
    }

    

    private static final String SELECT_ALL_USER_ACCESSIBLE_ACTIVE_FLAG_WORK_VIEWS =
        "SELECT DISTINCT f.id flagId, f.permissions perm, f.groupobj_id groupobjId, go.survey_question_id, f.issue_description issue, f.respond_time, f.raise_user_id, f.assigned_user_id " +
        "FROM groupobj go, groupobj_flag f, groupdef def, groupdef_user du, groupdef_role dr, task_assignment ta " +
        "WHERE go.horse_id=? AND f.groupobj_id=go.id AND f.unset_time IS NULL AND def.id=go.groupdef_id AND def.enabled != 0 " +
        "AND (f.raise_user_id=? OR f.assigned_user_id=? " +
        "OR (du.groupdef_id=def.id AND du.user_id=?) " +
        "OR (dr.groupdef_id=def.id AND dr.role_id=? AND ta.horse_id=? AND ta.assigned_user_id=?) " +
        "OR (f.groupobj_id IN (" +
        "SELECT go2.id FROM groupobj_flag f2, groupobj go2 " +
        "WHERE go2.id=f2.groupobj_id AND go2.horse_id=? AND (f2.raise_user_id=? OR f2.assigned_user_id=?))))";

    public List<FlagWorkView> selectUserAccessibleActiveFlagWorkViews(int horseId, int userId, int roleId) {

        List<FlagWorkView> list = getJdbcTemplate().query(
                SELECT_ALL_USER_ACCESSIBLE_ACTIVE_FLAG_WORK_VIEWS,
                new Object[]{horseId, userId, userId, userId, roleId, horseId, userId, horseId, userId, userId},
                new int[]{INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER},
                new FlagWorkRowMapper());

        processFlagViews(list, userId);

        return list;
    }


    private static final String SELECT_ALL_ACTIVE_FLAG_WORK_VIEWS_OF_HORSE =
        "SELECT DISTINCT f.id flagId, f.permissions perm, f.groupobj_id groupobjId, go.survey_question_id, f.issue_description issue, f.respond_time, f.raise_user_id, f.assigned_user_id " +
        "FROM groupobj go, groupobj_flag f, groupdef def " +
        "WHERE go.horse_id=? AND f.groupobj_id=go.id AND f.unset_time IS NULL AND def.id=go.groupdef_id AND def.enabled != 0";

    public List<FlagWorkView> selectAllActiveFlagWorkViewsOfHorse(int horseId, int userId) {

        List<FlagWorkView> list = getJdbcTemplate().query(
                SELECT_ALL_ACTIVE_FLAG_WORK_VIEWS_OF_HORSE,
                new Object[]{horseId},
                new int[]{INTEGER},
                new FlagWorkRowMapper());

        processFlagViews(list, userId);

        return list;
    }


    private void processFlagViews(List<FlagWorkView> list, int userId) {
        if (list != null) {
            for (FlagWorkView view : list) {
                short flagType;
                if (view.getRaiseUserId() == userId) flagType = FlagWorkView.FLAG_TYPE_RAISED_BY_ME;
                else if (view.getAssignedUserId() == userId) flagType = FlagWorkView.FLAG_TYPE_ASSIGNED_TO_ME;
                else flagType = FlagWorkView.FLAG_TYPE_OTHER;
                view.setFlagType(flagType);            }
        }
    }


    public class FlagWorkRowMapper implements RowMapper {

            public FlagWorkView mapRow(ResultSet rs, int rowNum) throws SQLException {
                FlagWorkView view = new FlagWorkView();

                view.setFlagId(rs.getInt("flagId"));
                view.setGroupobjId(rs.getInt("groupobjId"));
                view.setSurveyQuestionId(rs.getInt("survey_question_id"));
                view.setTitle(rs.getString("issue"));

                Date respTime = rs.getDate("respond_time");
                view.setWorked(respTime != null);

                view.setRaiseUserId(rs.getInt("raise_user_id"));
                view.setAssignedUserId(rs.getInt("assigned_user_id"));
                view.setPermissions(rs.getInt("perm"));

                return view;
            }
    };

}
