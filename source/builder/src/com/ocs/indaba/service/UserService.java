/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.AssignedTaskDisplay;
import com.ocs.indaba.vo.ProjectUserView;
import com.ocs.indaba.vo.QueueUser;
import com.ocs.indaba.vo.UserDisplay;
import com.ocs.indaba.vo.UserListInfo;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class helps to manager the user information and operations(i.e.,
 * modification, creation, authentication, deletion, etc.).
 *
 * @author Jeff
 *
 */
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);
    private UserDAO userDao;
    private CaseDAO caseDao;
    private TeamDAO teamDao;
    private RoleDAO roleDAO;
    private TaskDAO taskDao;
    private TargetDAO targetDao;
    private HorseDAO horseDao;
    private ProjectDAO projectDao;
    private ProjectAdminDAO projectAdminDao;
    private ViewPermissionService viewPermisssionService;
    private ContentHeaderDAO contentHeaderDao;
    private OrgadminDAO orgAdminDao;
    private OrganizationDAO organizationDao;
    // for generating a random password
    private Random random = new Random(System.currentTimeMillis());
    private static final char[] symbols = new char[62];

    static {
        for (int i = 0; i < 10; ++i) {
            symbols[i] = (char) ('0' + i);
        }
        for (int i = 10; i < 36; ++i) {
            symbols[i] = (char) ('a' + i - 10);
        }
        for (int i = 36; i < 62; ++i) {
            symbols[i] = (char) ('A' + i - 36);
        }
    }
    private static final int DEFAULT_PASSWORD_LENGTH = 8;

    /**
     * Check if the speicified user is valid or not
     *
     * @param username
     * @param password
     * @return
     */
    public boolean authenticate(String username, String password) {
        return (userDao.selectUserByNameAndPwd(username, password) != null);
    }

    public boolean authenticate(String username, String password, short status) {
        return (userDao.selectUserByNameAndPwd(username, password, status) != null);
    }

    public boolean authenticateAdministrator(String username, String password) {
        User user = userDao.selectUserByNameAndPwd(username, password);
        if (user == null) {
            return false;
        }

        if (user.getSiteAdmin() == Constants.USER_SITE_ADMINISTRATOR) {
            return true;
        }

        // see if the user is the primary admin of any orgs
        List<Organization> orgs = organizationDao.selectOrgsByUserIdFromOrg(user.getId());
        if (orgs != null && orgs.size() > 0) {
            return true;
        }

        List<Orgadmin> admins = orgAdminDao.selectOrgadminByUserId(user.getId());
        if (admins != null && admins.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Get User
     *
     * @param username
     * @param password
     * @return
     */
    public User getUser(String username) {
        return userDao.selectUserByName(username);
    }

    public User getUser(int userId) {
        return userDao.selectUserById(userId);
    }

    public ProjectUserView getProjectUserView(int projectId, int userId) {
        return userDao.selectProjectUser(projectId, userId);
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public User createUser(User user) {
        return userDao.create(user);
    }

    public User getUserByEmail(String email) {
        return userDao.selectUserByEmail(email);
    }

    public boolean isUserNameExist(String usrName) {
        return userDao.selectUserByName(usrName) != null ? true : false;
    }

    public String suggestUserNameByEmail(String email) {
        User user = getUserByEmail(email);
        if (user != null) {
            return user.getUsername();
        } else {
            String usrName = email.substring(0, email.indexOf('@'));
            return suggestUserName(usrName);
        }
    }

    public String suggestUserName(String preferedUserName) {

        if (isUserNameExist(preferedUserName)) {
            int i = 1;
            String usrName = preferedUserName + Integer.toString(i);
            while (isUserNameExist(usrName)) {
                ++i;
                usrName = preferedUserName + Integer.toString(i);
            }
            return usrName;
        } else {
            return preferedUserName;
        }
    }

    public void updateUserDefaultProjectId(int userId, int projectId) {
        userDao.updateUserDefaultProjectId(userId, projectId);
    }

    public List<QueueUser> getUsersByTaskId(int taskId, int projectId) {
        return userDao.selectUsersByTaskId(taskId, projectId);
    }

    public List<UserListInfo> getAllUserInfoListByProjectId(Integer projectId) {
        List<UserListInfo> userInfoList = new ArrayList<UserListInfo>();
        List<User> userList = userDao.selectAllUsersByProjectId(projectId);

        if (userList != null) {
            for (User user : userList) {
                UserListInfo userListInfo = new UserListInfo();
                userListInfo.setId(user.getId());
                userListInfo.setUsername(user.getUsername());
                userListInfo.setOpenCases(caseDao.selectOpenCasesByUserIdAndProjectId(user.getId(), projectId));

                userListInfo.setTeams(teamDao.selectTeamsByUserAndProject(user.getId(), projectId));
                userListInfo.setRole(roleDAO.selectRoleNameByProjectIdAndUserId(projectId, user.getId()));
                userListInfo.setTargets(targetDao.selectTargetsByUserAndProject(user.getId(), projectId));

                List<String> contents = new ArrayList<String>();
                List<AssignedTaskDisplay> assignedTaskDisplay = new ArrayList<AssignedTaskDisplay>();

                List<AssignedTask> assignedTaskList = taskDao.selectAssignedTasksByUserId(user.getId(), projectId, true);
                if (assignedTaskList != null) {
                    for (AssignedTask assignedTask : assignedTaskList) {
                        String tempStr = assignedTask.getTargetName() + " - " + assignedTask.getProductName();
                        if (!contents.contains(tempStr)) {
                            contents.add(tempStr);
                            AssignedTaskDisplay tempDisplay;
                            ContentHeader contentHeader = contentHeaderDao.selectContentHeaderById(assignedTask.getHorseId());
                            if (contentHeader.getSubmitTime() != null) {
                                assignedTask.setClickable(true);
                            }
                            if (assignedTask.isClickable()) {
                                tempDisplay = new AssignedTaskDisplay(contents.get(contents.size() - 1), assignedTask.getHorseId(), assignedTask.getProductName(), assignedTask.getWorkflowObjectStatus());
                            } else {
                                tempDisplay = new AssignedTaskDisplay(contents.get(contents.size() - 1), -1, assignedTask.getProductName(), assignedTask.getWorkflowObjectStatus());
                            }
                            tempDisplay.setContentType(contentHeader.getContentType());
                            assignedTaskDisplay.add(tempDisplay);
                        }
                    }
                }
                userListInfo.setAssignContent(contents);
                userListInfo.setAssignedTaskDisplay(assignedTaskDisplay);
                userInfoList.add(userListInfo);
            }
        }
        return userInfoList;
    }

//    public List<? extends GenericClass> setFieldsByFilter(List<? extends GenericClass> list, List<Integer> filter) {
//        if (list.isEmpty()) {
//            return list;
//        }
//
//        for (int i = list.size() - 1; i >= 0; i --) {
//            if (filter.get(0).equals(0)) {    // user choose "Exclude Selected" (means contained in this filter)
//                if (filter.lastIndexOf(list.get(i).getId()) < 1) {
//                    list.remove(list.get(i));
//                }
//            } else {    // user choose "Only Selected" (means not contained in this filter)
//                if (filter.lastIndexOf(list.get(i).getId()) > 0) {
//                    list.remove(list.get(i));
//                }
//            }
//        }
//        return list.isEmpty() ? null : list;
//    }
//    public List<UserListInfo> getAllUserInfoListByFilterStupid(Integer projectId,
//            List<Integer> targetFilter, List<Integer> productFilter,
//            List<Integer> roleFilter, List<Integer> teamFilter,
//            Integer hasCaseFilter, Integer statusFilter) {
//
//        List<UserListInfo> userInfoList = new ArrayList<UserListInfo>();
//        List<User> userList = userDAO.selectAllUsersByProjectId(projectId);
//        for (User user : userList) {
//            UserListInfo userListInfo = new UserListInfo();
//            userListInfo.setId(user.getId());
//            userListInfo.setUsername(user.getUsername());
//
//            userListInfo.setOpenCases(caseDao.selectOpenCasesByOpenUserId(user.getId()));   // TODO
//
//            // team filter
//            List<? extends GenericClass> teamList = teamDao.selectTeamsByUserId(user.getId());
//            teamList = setFieldsByFilter(teamList, teamFilter);
//            if (teamList == null) {
//                continue;
//            }
//            userListInfo.setTeams((List<Team>)teamList);
//
//            // role filter
//            Role role = roleDAO.selectRoleByProjectIdAndUserId(projectId, user.getId());
//            if (roleFilter.get(0).equals(0)) {    // user choose "Exclude Selected" (means contained in this filter)
//                if (roleFilter.lastIndexOf(role.getId()) < 1) {
//                    continue;
//                }
//            } else {    // user choose "Only Selected" (means not contained in this filter)
//                if (roleFilter.lastIndexOf(role.getId()) > 0) {
//                    continue;
//                }
//            }
//            userListInfo.setRole(role.getName());
//
//            // target filter
//            List<? extends GenericClass> targetList = targetDao.selectTargetsByUserId(user.getId());
//            targetList = setFieldsByFilter(targetList, targetFilter);
//            if (targetList == null) {
//                continue;
//            }
//            userListInfo.setTargets((List<Target>)targetList);
//
//            // product filter
//            List<String> contents = new ArrayList<String>();
//            List<AssignedTask> assignedTaskList = taskDao.selectAssignedTasksByUserId(user.getId());
//            for (AssignedTask assignedTask : assignedTaskList) {
//                if (targetFilter.get(0).equals(0)) {    // user choose "Exclude Selected" (means contained in this filter)
//                    if (productFilter.lastIndexOf(new Integer(assignedTask.getProductId())) > 0) {
//                        contents.add(assignedTask.getTaskName() + "-" + assignedTask.getProductName() + "-" + assignedTask.getTargetName());
//                    }
//                } else {    // user choose "Only Selected" (means not contained in this filter)
//                    if (productFilter.lastIndexOf(new Integer(assignedTask.getProductId())) < 1) {
//                        contents.add(assignedTask.getTaskName() + "-" + assignedTask.getProductName() + "-" + assignedTask.getTargetName());
//                    }
//                }
//            }
//            userListInfo.setAssignContent(contents);
//            userInfoList.add(userListInfo);
//        }
//        return userInfoList;
//    }
    public List<UserListInfo> getAllUserInfoListByFilter(int projectId,
            List<Integer> targetFilter, List<Integer> productFilter,
            List<Integer> roleFilter, List<Integer> teamFilter,
            Integer hasCaseFilter, Integer statusFilter) {

        List<UserListInfo> userInfoList = new ArrayList<UserListInfo>();
        List<User> userList = userDao.selectAllUsersByProjectId(projectId);

        if (userList != null) {
            for (User user : userList) {
                UserListInfo userListInfo = new UserListInfo();
                userListInfo.setId(user.getId());
                userListInfo.setUsername(user.getUsername());

                List<Cases> caseList = caseDao.selectOpenCasesByUserIdAndProjectId(user.getId(), projectId);
                switch (hasCaseFilter) {
                    case 1:
                        if (caseList.isEmpty()) {
                            continue;
                        }
                        break;
                    case 2:
                        if (!caseList.isEmpty()) {
                            continue;
                        }
                        break;
                }
                userListInfo.setOpenCases(caseList);

                List<Team> teamList = teamDao.selectTeamsByFilter(user.getId(), teamFilter);
                if (teamList == null) {
                    continue;
                }
                userListInfo.setTeams(teamList);

                Role role = roleDAO.selectRoleByProjectIdAndUserId(projectId, user.getId());
                if (roleFilter.get(0).equals(0)) {    // user choose "Exclude Selected" (means contained in this filter)
                    if (roleFilter.lastIndexOf(role.getId()) < 1) {
                        continue;
                    }
                } else {    // user choose "Only Selected" (means not contained in this filter)
                    if (roleFilter.lastIndexOf(role.getId()) > 0) {
                        continue;
                    }
                }
                userListInfo.setRole(role.getName());

                List<Target> targetList = targetDao.selectTargetsByFilter(user.getId(), targetFilter, projectId);
                if (targetList == null) {
                    continue;
                }
                userListInfo.setTargets(targetList);

                List<String> contents = new ArrayList<String>();
                List<AssignedTask> assignedTaskList = taskDao.selectAssignedTasksByFilter(user.getId(), projectId, statusFilter, targetFilter, productFilter);
                if (assignedTaskList == null) {
                    continue;
                }
                List<AssignedTaskDisplay> assignedTaskDisplay = new ArrayList<AssignedTaskDisplay>();
                for (AssignedTask assignedTask : assignedTaskList) {
                    String tempStr = assignedTask.getTargetName() + " - " + assignedTask.getProductName();
                    if (!contents.contains(tempStr)) {
                        contents.add(tempStr);
                        AssignedTaskDisplay tempDisplay;
                        ContentHeader contentHeader = contentHeaderDao.selectContentHeaderById(assignedTask.getHorseId());
                        if (contentHeader.getSubmitTime() != null) {
                            assignedTask.setClickable(true);
                        }
                        if (assignedTask.isClickable()) {
                            tempDisplay = new AssignedTaskDisplay(contents.get(contents.size() - 1), assignedTask.getHorseId(), assignedTask.getProductName(), assignedTask.getWorkflowObjectStatus());
                        } else {
                            tempDisplay = new AssignedTaskDisplay(contents.get(contents.size() - 1), -1, assignedTask.getProductName(), assignedTask.getWorkflowObjectStatus());
                        }
                        tempDisplay.setContentType(contentHeader.getContentType());
                        assignedTaskDisplay.add(tempDisplay);
                    }
                }

                if (contents.isEmpty() && statusFilter == 2) {
                    continue;
                }
                userListInfo.setAssignContent(contents);
                userListInfo.setAssignedTaskDisplay(assignedTaskDisplay);
                userInfoList.add(userListInfo);
            }
        }
        return userInfoList;
    }

    public List<UserListInfo> getAllUserInfoList() {
        List<UserListInfo> userInfoList = new ArrayList<UserListInfo>();
        List<User> userList = userDao.selectAllUsers();
        if (userList != null) {
            for (User user : userList) {
                UserListInfo userListInfo = new UserListInfo();
                userListInfo.setId(user.getId());
                userListInfo.setUsername(user.getUsername());
                userListInfo.setOpenCases(caseDao.selectOpenCasesByOpenUserId(user.getId()));
                userListInfo.setTeams(teamDao.selectTeamsByUserId(user.getId()));

                //TODO
                userListInfo.setRole(null);
                userListInfo.setTargets(null);
                userListInfo.setAssignContent(null);
                userInfoList.add(userListInfo);
            }
        }
        return userInfoList;
    }

    public HashSet<User> getDefaultAssignedUser(int projectId) {
        HashSet<User> retUsers = new HashSet<User>();
        retUsers.addAll(userDao.selectUsersByProjectContact(projectId));
        retUsers.addAll(userDao.selectOwnerAndAdminByProjectId(projectId));
        return retUsers;
    }

    public TreeSet<User> getDefaultAssignedUserOrderByLastname(int projectId, int userId, int userId2) {
        TreeSet<User> retUsers = new TreeSet<User>(
                new Comparator() {

                    public int compare(Object o1, Object o2) {
                        return ((User) o1).getLastName().compareTo(((User) o2).getLastName());
                    }
                });
        retUsers.addAll(userDao.selectUsersByProjectContact(projectId));
        retUsers.addAll(userDao.selectOwnerAndAdminByProjectId(projectId));
        
        if (userId > 0) retUsers.add(userDao.selectUserById(userId));
        if (userId2 > 0 && userId2 != userId) retUsers.add(userDao.selectUserById(userId2));
        return retUsers;
    }

    public List<UserDisplay> getUserByProjectContact(int projectId, int userId) {
        List<User> users = userDao.selectUsersByProjectContact(projectId);
        List<UserDisplay> userDisplays = new ArrayList<UserDisplay>();
        if (users != null) {
            for (User user : users) {
                userDisplays.add(viewPermisssionService.getUserDisplayOfProject(projectId, Constants.DEFAULT_VIEW_MATRIX_ID, userId, user.getId()));
            }
        }
        return userDisplays;
    }

    public List<Project> getUserProjects(int userId) {
        return projectDao.selectProjectsByUserId(userId);
    }

    public List<User> getAllUsersOrderByLastname(int projectId, int userId) {
        return userDao.selectAllUsersOrderByLastName(projectId, userId);
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public List<User> getAllActiveUsersByStatus(List<Integer> excludes) {
        return userDao.selectAllActiveUsers(excludes);
    }

    public List<User> getSiteAdminUsers() {
        return userDao.selectSiteAdminUsers();
    }

    public User createUser(String firstName, String lastName, String email, String userName, int orgId) {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setUsername(userName);
        u.setOrganizationId(orgId);
        u.setStatus(Constants.USER_STATUS_INACTIVE);
        u.setLanguageId(Constants.LANGUAGE_ID_EN);
        u.setPassword(generatePassword());
        u.setCreateTime(new Date());
        u.setForwardInboxMsg(true);
        u.setEmailDetailLevel((short)1);
        
        return userDao.create(u);
    }

    public User createUser(String firstName, String lastName, String email, String userName) {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setUsername(userName);
        u.setStatus(Constants.USER_STATUS_INACTIVE);
        u.setLanguageId(Constants.LANGUAGE_ID_EN);
        u.setPassword(generatePassword());
        u.setCreateTime(new Date());
        u.setForwardInboxMsg(true);
        u.setEmailDetailLevel((short)1);
        return userDao.create(u);
    }

    private String generatePassword() {
        char[] buf = new char[DEFAULT_PASSWORD_LENGTH];
        for (int i = 0; i < buf.length; ++i) {
            buf[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }

    @Autowired
    public void setUserDao(UserDAO userDAO) {
        this.userDao = userDAO;
    }

    @Autowired
    public void setCaseDao(CaseDAO caseDao) {
        this.caseDao = caseDao;
    }

    @Autowired
    public void setProjectAdminDao(ProjectAdminDAO projectAdminDao) {
        this.projectAdminDao = projectAdminDao;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setTeamDao(TeamDAO teamDao) {
        this.teamDao = teamDao;
    }

    @Autowired
    public void setRoleDao(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setTargetDao(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }

    @Autowired
    public void setHorseDao(HorseDAO horseDao) {
        this.horseDao = horseDao;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermisssionService) {
        this.viewPermisssionService = viewPermisssionService;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setOrgadminDAO(OrgadminDAO orgAdminDao) {
        this.orgAdminDao = orgAdminDao;
    }

    @Autowired
    public void setOrganizationDAO(OrganizationDAO organizationDao) {
        this.organizationDao = organizationDao;
    }

    public List<UserDisplay> getActiveUsersInViewOfProject(int projectId, int uid) {
        List<User> userList = userDao.selectAllUsersByProjectId(projectId);
        List<UserDisplay> userDisplays = new ArrayList<UserDisplay>();

        if (userList != null) {
            Collections.sort(userList, new Comparator<User>() {

                public int compare(User o1, User o2) {
                    return o1.getLastName().compareTo(o2.getLastName());
                }
            });


            for (User u : userList) {
                if (u.getStatus() == 1) {
                    userDisplays.add(viewPermisssionService.getUserDisplayOfProject(projectId, Constants.DEFAULT_VIEW_MATRIX_ID, uid, u.getId()));
                }
            }
        }

        return userDisplays;
    }

    private List<Integer> getOrgAdminUserIdsByOrgId(List<Integer> userIds, int orgId) {
        if (userIds == null) {
            userIds = new ArrayList<Integer>();
        }
        List<Orgadmin> orgAdmins = orgAdminDao.selectOrgadminByOrgId(orgId);
        if (orgAdmins != null && !orgAdmins.isEmpty()) {
            for (Orgadmin oa : orgAdmins) {
                if (!userIds.contains(oa.getUserId())) {
                    userIds.add(oa.getUserId());
                }
            }
        }
        Organization org = organizationDao.get(orgId);
        if (org != null && org.getAdminUserId() > 0) {
            if (!userIds.contains(org.getAdminUserId())) {
                userIds.add(org.getAdminUserId());
            }
        }
        return userIds;
    }

    public List<User> getOrgAdminUsersByOrgId(int orgId) {
        List<Integer> userIds = new ArrayList<Integer>();
        return userDao.selectUsersByIds(getOrgAdminUserIdsByOrgId(userIds, orgId));
    }

    /**
     *
     * @param orgIds
     * @return
     */
    public List<User> getOrgAdminUsersByOrgIds(List<Integer> orgIds) {
        if (orgIds == null || orgIds.isEmpty()) {
            return null;
        }
        List<Integer> userIds = new ArrayList<Integer>();
        for (int orgId : orgIds) {
            userIds = getOrgAdminUserIdsByOrgId(userIds, orgId);
        }
        return userDao.selectUsersByIds(userIds);
    }

    public List<Integer> getOrgAdminUserIdsByOrgIds(List<Integer> orgIds) {
        if (orgIds == null || orgIds.isEmpty()) {
            return null;
        }

        List<Integer> userIds = new ArrayList<Integer>();
        for (int orgId : orgIds) {
            userIds = getOrgAdminUserIdsByOrgId(userIds, orgId);
        }

        return userIds;
    }

    public boolean isOrgAdmin(int userId, int orgId) {
        return orgAdminDao.existsByUserIdAndOrgId(userId, orgId);
    }

    public boolean isProjectAdmin(int userId, int projectId) {
        return projectAdminDao.existsByUserIdAndOrgId(userId, projectId);
    }

    public Map<Integer, String> getUsernameMap(List<Integer> userIds) {
        return userDao.selectUsernameMap(userIds);
    }

    public List<User> findSurveyReviewRespondentUsers(int projId, int roleId, int targetId, String username){
        return userDao.findSurveyReviewRespondentUsers(projId, roleId, targetId, username);
    }
}
