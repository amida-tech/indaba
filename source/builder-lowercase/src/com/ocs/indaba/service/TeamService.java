/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.TeamDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Team;
import com.ocs.indaba.po.User;
import com.ocs.indaba.vo.TeamInfo;
import com.ocs.indaba.vo.UserDisplay;
import com.ocs.indaba.vo.UserTeamInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong
 */
public class TeamService {

    private static final Logger logger = Logger.getLogger(TeamService.class);
    private TeamDAO teamDao;
    private UserDAO userDAO;
    private ViewPermissionService viewPermisssionService;

    public List<TeamInfo> getTeamsByUserId(int userId) {
        List<TeamInfo> teamInfoList = new ArrayList<TeamInfo>();
        List<Team> teamList = teamDao.selectTeamsByUserId(userId);
        if (teamList != null) {
            for (Team team : teamList) {
                TeamInfo teamInfo = new TeamInfo();
                teamInfo.setId(team.getId());
                teamInfo.setName(team.getTeamName());
                teamInfo.setUserList(userDAO.selectUsersByTeamId(team.getId()));
                teamInfoList.add(teamInfo);
            }
        }
        return teamInfoList;
    }

    public List<TeamInfo> getTeamsCanSee(int projectId, int userId) {
        List<TeamInfo> teamInfoList = new ArrayList<TeamInfo>();
        //List<Team> teamList = teamDao.selectTeamsByUserId(userId);
        List<Team> teamList = teamDao.selectTeamsByUserAndProject(userId, projectId);
        if (teamList != null) {
            for (Team team : teamList) {
                TeamInfo teamInfo = new TeamInfo();
                teamInfo.setId(team.getId());
                teamInfo.setName(team.getTeamName());
                List<User> users = userDAO.selectUsersByTeamId(team.getId());
                teamInfo.setUserList(users);
                /*if (users != null) {
                List<UserDisplay> userDisplays = new ArrayList<UserDisplay>();
                for (User user : users) {
                userDisplays.add(viewPermisssionService.getUserDisplayOfProject(projectId, Constants.DEFAULT_VIEW_MATRIX_ID, userId, user.getId()));
                }
                teamInfo.setUserDisplays(userDisplays);
                }else
                teamInfo.setUserDisplays(new ArrayList<UserDisplay>());*/
                teamInfoList.add(teamInfo);
            }
        }
        return teamInfoList;
    }

    public List<TeamInfo> getTeamsByUserIdwithPermission(int projectId, int userId) {
        List<TeamInfo> teamInfoList = new ArrayList<TeamInfo>();
        List<Team> teamList = teamDao.selectTeamsByUserId(userId);
        if (teamList != null) {
            for (Team team : teamList) {
                TeamInfo teamInfo = new TeamInfo();
                teamInfo.setId(team.getId());
                teamInfo.setName(team.getTeamName());
                teamInfo.setUserList(userDAO.selectUsersByTeamId(team.getId()));
                teamInfoList.add(teamInfo);
            }
        }
        return teamInfoList;
    }

    public List<UserTeamInfo> getTeamsByUserIdAndProjectId(long userId, int projectId) {
        return teamDao.selectTeamsByUserIdAndProjectId(userId, projectId);
    }

    @Autowired
    public void setTeamDao(TeamDAO teamDao) {
        this.teamDao = teamDao;
    }

    @Autowired
    public void setUserDao(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermisssionService) {
        this.viewPermisssionService = viewPermisssionService;
    }

    public List<Team> getTeamsByProjectId(Integer prjid, int uid) {
        return teamDao.selectTeamsByProjectId(prjid);
    }

    public List<Team> getAllTeams(Integer prjid) {
        return teamDao.selectTeamsByProjectId(prjid);
    }
}
