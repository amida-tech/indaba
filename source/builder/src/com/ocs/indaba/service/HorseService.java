/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.CaseDAO;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.GoalObjectDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.PregoalDAO;
import com.ocs.indaba.dao.SequenceObjectDAO;
import com.ocs.indaba.dao.TeamDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.dao.WorkflowObjectDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.SequenceObject;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.User;
import com.ocs.indaba.po.WorkflowObject;
import com.ocs.indaba.util.ChartUtil;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.util.PriorityQueue;
import com.ocs.indaba.vo.ActiveHorseView;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.GoalObjectView;
import com.ocs.indaba.vo.PreGoalView;
import com.ocs.indaba.vo.SequenceObjectView;
import com.ocs.indaba.vo.UserDisplay;
import com.ocs.indaba.vo.UserTeamInfo;
import com.ocs.indaba.vo.HorseInfo;
import com.ocs.util.Pagination;
import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class HorseService {

    private static final Logger log = Logger.getLogger(HorseService.class);
    private HorseDAO horseDao = null;
    private PregoalDAO pregoalDao = null;
    private CaseDAO caseDao = null;
    private TeamDAO teamDao = null;
    private SequenceObjectDAO seqObjDao = null;
    private UserDAO userDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    private ViewPermissionService viewPermisssionService;
    private GoalObjectDAO goalObjectDao;
    private TaskAssignmentDAO taskAssignmentDao;
    private TaskService taskService;
    private WorkflowObjectDAO workflowObjectDao;

    public HorseInfo getHorseInfo(int horseId) {
        return horseDao.selectHorseInfo(horseId);
    }

    public Horse getHorseByTaskId(int taskId) {
        return horseDao.selectHorseByTaskId(taskId);
    }

    public Horse getHorseById(int horseId) {
        return horseDao.get(horseId);
    }

    public Horse getHorseByProductAndTarget(int productId, int targetId) {
        return horseDao.selectHorseByProductIdAndTargetId(productId, targetId);
    }

    public Horse getHorseByWorkflowObjectId(int wfoId) {
        return horseDao.selectHorseByWorkflowObjectId(wfoId);
    }

    public int getContentTypeByHorseId(int horseId) {
        return horseDao.selectContentTypeByHorseId(horseId);
    }

    public boolean hasJoinedInHorse(int userId, int horseId) {
        return horseDao.selectUserJoinInHorse(userId, horseId);
    }

    /**
     * Get the currently completed percentage of the specified horse id
     *
     * @param horseId
     * @return the percentage(with 'double' type)
     */
    public double getCompletedPercentage(int horseId) {
        log.debug("Get the completed percentage for horse: " + horseId);
        int allDur = horseDao.selectAllDuratinOfHorse(horseId);
        int completedDur = horseDao.selectCompletedDurtion(horseId);
        return (allDur == 0) ? 0 : (completedDur * 1.0d) / allDur;
    }

    public int getIncompletedDurtion(int horseId) {
        return horseDao.selectIncompletedDurtion(horseId);
    }

    public int getCompletedDurtion(int horseId) {
        return horseDao.selectCompletedDurtion(horseId);
    }

    public int getAllDurtion(int horseId) {
        return horseDao.selectAllDuratinOfHorse(horseId);
    }
    
    public List<UserDisplay> getAssignedUsers(int horseId, int projectId, int userId)
    {
        List<User> users = userDao.selectAssignedUsers(horseId);
        List<UserDisplay> userDisplays = new ArrayList<UserDisplay>();
        if (users != null) {
            for (User user : users) {
                userDisplays.add(viewPermisssionService.getUserDisplayOfProject(projectId, Constants.DEFAULT_VIEW_MATRIX_ID, userId, user.getId()));
            }
        }
        return userDisplays;
    }

    /**
     * Get all of the active horses in the indaba
     *
     * @return List of ActiveHorseView
     */
    public List<ActiveHorseView> getAllActiveHorses(int userId, int projId, List<Integer> targetIds, List<Integer> productIds, int status) {
        List<ActiveHorseView> activeHorses = horseDao.selectAllActiveHorses(projId, targetIds, productIds, status);
        if (activeHorses != null) {
            for (ActiveHorseView h : activeHorses) {
                h.setUserJoinedIn(hasJoinedInHorse(userId, h.getHorseId()));
                // set completed
                h.setCompleted(getCompletedPercentage(h.getHorseId()));
                // set open cases
                h.setOpenCases(caseDao.selectCasesByHorseId(h.getHorseId()));
                // set assigned people
                /*
                List<User> users = userDao.selectAssignedUsers(h.getHorseId());
                List<UserDisplay> userDisplays = new ArrayList<UserDisplay>();
                if (users != null) {
                    for (User user : users) {
                        userDisplays.add(viewPermisssionService.getUserDisplayOfProject(h.getProjectId(), Constants.DEFAULT_VIEW_MATRIX_ID, userId, user.getId()));
                    }
                }
                * 
                */
                
                List<UserDisplay> userDisplays = getAssignedUsers(h.getHorseId(), h.getProjectId(), userId);
                h.setPeopleAssigned(userDisplays);

                // set workflow sequence
                List<SequenceObject> seqObjList = seqObjDao.selectSequenceObjectsByWorkflowObjectId(h.getWorkflowObjectId());
                List<SequenceObjectView> seqObjViewList = new ArrayList<SequenceObjectView>();
                Map<Integer, GoalObjectView> govMap = new HashMap<Integer, GoalObjectView>();
                if (seqObjList != null) {
                    for (SequenceObject seqObj : seqObjList) {
                        SequenceObjectView seqObjView = new SequenceObjectView();
                        seqObjView.setSeqObj(seqObj);
                        List<GoalObjectView> goalObjList = seqObjDao.selectGoalObjectsBySequenceObjectId(seqObj.getId());
                        seqObjView.setGoalObjects(goalObjList);
                        seqObjViewList.add(seqObjView);

                        if (goalObjList != null) {
                            for (GoalObjectView goalObj : goalObjList) {
                                govMap.put(goalObj.getGoalId(), goalObj);
                            }
                        }
                    }
                    for (int i = 0; i < seqObjList.size(); i++) {
                        SequenceObjectView seqObjView = seqObjViewList.get(i);
                        List<GoalObjectView> goalObjList = seqObjView.getGoalObjects();
                        if (goalObjList != null && goalObjList.size() > 0) {
                            List<Integer> goalObjIds = seqObjDao.selectPregoalIdBySequenceObjectId(
                                    seqObjView.getSeqObj().getWorkflowSequenceId());

                            if (goalObjIds != null) {
                                for (Integer goalObjId : goalObjIds) {
                                    GoalObjectView tmpgov = govMap.get(goalObjId);
                                    if (tmpgov != null) {
                                        goalObjList.get(0).addParent(tmpgov);
                                        tmpgov.addChild(goalObjList.get(0));
                                    }
                                }
                            }
                            for (int j = 0; j < goalObjList.size(); j++) {
                                goalObjList.get(j).setIndexOfSeq(i);
                                goalObjList.get(j).setIndexOfGoal(j);
                            }
                        }
                    }
                }

                AssignedTask activeTask = horseDao.selectNextTask(h.getHorseId());

                if (activeTask != null) {
                    //UserDisplay userDisplay = viewPermisssionService.getUserDisplayOfProject(projId, Constants.DEFAULT_VIEW_MATRIX_ID, userId, activeTask.getAssignedUserId());
                    //String displayUsername = (userDisplay != null) ? userDisplay.getDisplayUsername() : userDao.selectUserById(activeTask.getAssignedUserId()).getUsername();
                    //activeTask.setAssignedUsername(displayUsername);
                    h.setActiveTask(activeTask);
                }

                h.setSequences(seqObjViewList);
            }
        }
        return activeHorses;
    }

    public String getChartPathByHorseId(final int horseId) {

        long oldDate, newDate = new Date().getTime();
        String caption = "Indaba " + contentHeaderDao.selectContentHeaderByHorseId(horseId).getTitle();
        String basePath = Config.getString(Config.KEY_STORAGE_IMAGE_BASE);
        String pathFormat = Config.getString(Config.KEY_STORAGE_IMAGE_FILENAME_FORMAT);
        String newPath = MessageFormat.format(pathFormat, basePath, horseId, newDate + "", "jpg");

        File dir = new File(basePath);
        String[] files = dir.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                File file = new File(dir + "\\" + name);
                return (file.isFile() && name.startsWith(horseId + "_"));
            }
        });
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                oldDate = Long.parseLong(files[i].substring(files[i].indexOf("_") + 1, files[i].length() - 4));
                if (newDate - oldDate <= DateUtils.MILLISECONDS_OF_MINUTE) {
                    return (horseId + "_" + oldDate);
                }
            }
        }

        List<SequenceObjectView> seqObjViewList = getSequenceObjectViewListByHorseId(horseId);
        List<GoalObjectView> sortedGovList = sortGoal(seqObjViewList, false);
        ChartUtil.generateAxisChart(sortedGovList, caption, newPath);

        return (horseId + "_" + newDate);
    }

    public List<SequenceObjectView> getSequenceObjectViewListByHorseId(int horseId) {

        int workflowObjectId = horseDao.selectWorkflowObjectIdByHorseId(horseId);
        List<SequenceObject> seqObjList = seqObjDao.selectSequenceObjectsByWorkflowObjectId(workflowObjectId);
        List<SequenceObjectView> seqObjViewList = new ArrayList<SequenceObjectView>();
        Map<Integer, GoalObjectView> govMap = new HashMap<Integer, GoalObjectView>();
        if (seqObjList != null) {
            for (SequenceObject seqObj : seqObjList) {
                SequenceObjectView seqObjView = new SequenceObjectView();
                seqObjView.setSeqObj(seqObj);
                List<GoalObjectView> goalObjList = seqObjDao.selectGoalObjectsBySequenceObjectId(seqObj.getId());
                seqObjView.setGoalObjects(goalObjList);
                seqObjViewList.add(seqObjView);

                if (goalObjList != null) {
                    for (GoalObjectView goalObj : goalObjList) {
                        govMap.put(goalObj.getGoalId(), goalObj);
                    }
                }
            }
            for (int i = 0; i < seqObjList.size(); i++) {
                SequenceObjectView seqObjView = seqObjViewList.get(i);
                List<GoalObjectView> goalObjList = seqObjView.getGoalObjects();
                if (goalObjList.size() > 0) {
                    List<Integer> goalObjIds = seqObjDao.selectPregoalIdBySequenceObjectId(
                            seqObjView.getSeqObj().getWorkflowSequenceId());

                    if (goalObjIds != null) {
                        for (Integer goalObjId : goalObjIds) {
                            GoalObjectView tmpgov = govMap.get(goalObjId);
                            if (tmpgov != null) {
                                goalObjList.get(0).addParent(tmpgov);
                                tmpgov.addChild(goalObjList.get(0));
                            }
                        }
                    }
                    for (int j = 0; j < goalObjList.size(); j++) {
                        goalObjList.get(j).setIndexOfSeq(i);
                        goalObjList.get(j).setIndexOfGoal(j);
                    }
                }
            }
        }
        return seqObjViewList;
    }

    public List<GoalObjectView> sortGoal(List<SequenceObjectView> seqObjViewList, Boolean isPlan) {
        List<GoalObjectView> retGoalObjectList = new ArrayList<GoalObjectView>();
        PriorityQueue<GoalObjectView> pq = new PriorityQueue<GoalObjectView>();
        int workloadHorse = calHorseCompletionTime(seqObjViewList, isPlan);
        double workload = 0;
        GoalObjectView gov;

        // init priority queue
        for (SequenceObjectView sov : seqObjViewList) {
            pq.offer(sov.getGoalObjects().get(0));
        }

        // sort
        retGoalObjectList.add(new GoalObjectView());
        while (!pq.isEmpty()) {
            gov = pq.peek();
            List<GoalObjectView> govList = seqObjViewList.get(gov.getIndexOfSeq()).getGoalObjects();
            if (gov.getIndexOfGoal() == govList.size() - 1) {
                gov = pq.poll();
            } else {
                gov = pq.replaceAtTop(govList.get(gov.getIndexOfGoal() + 1));
            }
            workload += (double) gov.getDuration() / workloadHorse;
            gov.setWorkload(workload);
            retGoalObjectList.add(gov);
        }

        gov = retGoalObjectList.get(0);
        gov.setCompletionTime(goalObjectDao.selectHorseStartTimeByGoalObjectId(
                retGoalObjectList.get(1).getGoalObjId()));

        return retGoalObjectList;
    }

    public int calHorseCompletionTime(List<SequenceObjectView> seqObjViewList,
            Boolean isPlan) {
        int workloadHorse = 0;
        GoalObjectView gov = null;
        for (SequenceObjectView sov : seqObjViewList) {
            gov = sov.getGoalObjects().get(0);
            if (gov.getCompletionTime() == null) {
                workloadHorse += calCompletionTimes(seqObjViewList, gov, isPlan);
            }
        }
        return workloadHorse;
    }

    public int calCompletionTimes(List<SequenceObjectView> seqObjViewList,
            GoalObjectView gov_cur_head, Boolean isPlan) {

        int workload = 0;
        List<Date> dateList = new ArrayList<Date>();
        if (gov_cur_head.getParent() != null) {
            for (GoalObjectView gov_par : gov_cur_head.getParent()) {
                if (gov_par.getCompletionTime() == null) {
                    workload += calCompletionTimes(seqObjViewList,
                            seqObjViewList.get(gov_par.getIndexOfSeq()).
                            getGoalObjects().get(0), isPlan);
                }
                dateList.add(gov_par.getCompletionTime());
            }
        }
        workload += calSeqCompletionTime(seqObjViewList.get(gov_cur_head.getIndexOfSeq()).getGoalObjects(),
                dateList, isPlan);
        return workload;
    }

    private int calSeqCompletionTime(List<GoalObjectView> govList, List<Date> dateList,
            Boolean isPlan) {
        int workloadSeq = 0;
        GoalObjectView preGov;
        for (GoalObjectView gov : govList) {
            if (gov.getIndexOfGoal() > 0) {
                preGov = govList.get(gov.getIndexOfGoal() - 1);
                dateList = new ArrayList<Date>();
                dateList.add(preGov.getCompletionTime());
            }
            workloadSeq += calGoalCompletionTime(dateList, gov, isPlan);
        }
        return workloadSeq;
    }

    private int calGoalCompletionTime(List<Date> dateList,
            GoalObjectView curGov, Boolean isPlan) {

        if (isPlan) {
            return calPlanCompletionTime(dateList, curGov);
        }

        switch (curGov.getStatus()) {
            case Constants.GOAL_OBJECT_STATUS_WAITING:
            case Constants.GOAL_OBJECT_STATUS_STARTING:
                return calWaitCompletionTime(dateList, curGov, isPlan);
            case Constants.GOAL_OBJECT_STATUS_DONE:
                return calDoneCompletionTime(curGov);
            case Constants.GOAL_OBJECT_STATUS_STARTED:
                return calStartedCompletionTime(curGov);
            case Constants.GOAL_OBJECT_STATUS_OVERDUE:
                return calOverdueCompletionTime(curGov);
        }
        return 0;
    }

    private int calPlanCompletionTime(List<Date> dateList, GoalObjectView curGov) {
        if (dateList.isEmpty()) {   // start
            return calGoalCompletionTime(dateList, curGov, false);
        } else {
            return calWaitCompletionTime(dateList, curGov, true);
        }
    }

    private int calWaitCompletionTime(List<Date> dateList, GoalObjectView curGov, Boolean isPlan) {
        Date date;
        if (dateList.isEmpty()) { // get horse start time
            dateList.add(goalObjectDao.selectHorseStartTimeByGoalObjectId(curGov.getGoalObjId()));
        }
        if (!isPlan) {
            dateList.add(new Date());
        }
        date = getLatestDate(dateList);
        date.setTime(date.getTime() + curGov.getDuration() * Constants.MILLSECONDS_PER_DAY);
        //date.setHours(date.getHours() + 24 * curGov.getDuration());
        curGov.setCompletionTime(date);
        return curGov.getDuration();
    }

    private int calDoneCompletionTime(GoalObjectView curGov) {
        Date date = new Date(curGov.getExitTime().getTime());
        curGov.setCompletionTime(date);
        return curGov.getDuration();
    }

    private int calStartedCompletionTime(GoalObjectView curGov) {
        Date date = new Date();
        long t1 = curGov.getEnterTime().getTime() + curGov.getDuration() * Constants.MILLSECONDS_PER_DAY;
        Date maxDuetime = taskService.getMaxDuetimeByGoalObjectId(curGov.getGoalObjId());
        long t2 = (maxDuetime == null) ? 0L : maxDuetime.getTime();

        date.setTime((t1 > t2) ? t1 : t2);
        curGov.setCompletionTime(date);
        return curGov.getDuration();
    }

    private int calOverdueCompletionTime(GoalObjectView curGov) {
        Date date = new Date();
        //date.setHours(date.getHours() + 48);
        date.setTime(date.getTime() + 2 * Constants.MILLSECONDS_PER_DAY);
        curGov.setCompletionTime(date);
        return curGov.getDuration();
    }

    private Date getLatestDate(List<Date> dateList) {
        Date retDate = new Date(dateList.get(0).getTime());

        if (dateList != null) {
            for (int i = 1; i < dateList.size(); i++) {
                if (retDate.before(dateList.get(i))) {
                    retDate.setTime(dateList.get(i).getTime());
                }
            }
        }
        return retDate;
    }

    /**
     * Get next tasks
     * 
     * @param horseView
     * @return
     */
    public List<AssignedTask> getNextTasks(ActiveHorseView horseView) {
        List<SequenceObjectView> seqObjViewList = horseView.getSequences();
        if (seqObjViewList != null) {
            for (SequenceObjectView seqObjView : seqObjViewList) {
                List<GoalObjectView> goalObjViews = seqObjView.getGoalObjects();
                if (goalObjViews != null) {
                    for (GoalObjectView goalObj : goalObjViews) {
                        if (Constants.GOAL_OBJECT_STATUS_DONE == goalObj.getStatus()) {
                        }
                    }
                }
            }
        }
        return null;
    }

    public void computeGoalWorkload(ActiveHorseView horseView) {
        List<SequenceObjectView> sequences = horseView.getSequences();
        if (sequences == null) {
            return;
        }
        for (SequenceObjectView seq : sequences) {
            List<GoalObjectView> goalObjects = seq.getGoalObjects();
            if (goalObjects == null) {
                continue;
            }
            for (GoalObjectView goal : goalObjects) {
                switch (goal.getStatus()) {
                    case Constants.GOAL_OBJECT_STATUS_WAITING: {
                        List<GoalObjectView> parentGoals = goal.getParent();
                        if (parentGoals != null) {
                            long maxCompletionTime = 0;
                            long curCompletionTime = 0;
                            Date completionTime = null;
                            for (GoalObjectView pGoal : parentGoals) {
                                completionTime = pGoal.getCompletionTime();
                                if (completionTime != null) {
                                    curCompletionTime = completionTime.getTime();
                                    if (curCompletionTime > maxCompletionTime) {
                                        maxCompletionTime = curCompletionTime;
                                    }
                                }
                            }

                        }
                        break;
                    }
                    case Constants.GOAL_OBJECT_STATUS_STARTED:
                        break;
                    case Constants.GOAL_OBJECT_STATUS_DONE:
                    default:

                }
            }
        }
    }

    /**
     * List all of the active horses in which the specified user took part in
     * @param userId
     * @return
     */
    public List<ActiveHorseView> getActiveHorsesOfUser(int userId, int projectId) {
        List<ActiveHorseView> list = getActiveHorsesOfUsers(new Integer[]{userId}, projectId);
        if (list != null) {
            for (ActiveHorseView h : list) {
                h.setUserJoinedIn(true);
            }
        }
        return list;
    }

    /**
     * List all of the active horses in which the specified user took part in
     * @param userId
     * @return
     */
    public List<ActiveHorseView> getActiveHorsesOfUsers(Integer[] userIds, int projectId) {
        List<ActiveHorseView> activeHorses = horseDao.selectActiveHorsesByUsers(userIds, projectId);
        if (activeHorses != null) {
            for (ActiveHorseView h : activeHorses) {
                Date startTime = h.getStartTime();

                //______ Due Time
                if (startTime != null) { // set dur time(start_time + total_duration)
                    int totalDuration = horseDao.selectAllDuratinOfHorse(h.getHorseId());
                    Date dueTime = new Date();
                    dueTime.setTime(startTime.getTime() + totalDuration * Constants.MILLSECONDS_PER_DAY);
                    h.setDueTime(dueTime);
                }

                //______ Estimation Time
                Date estimationTime = new Date();
                estimationTime.setTime(System.currentTimeMillis() + horseDao.selectIncompletedDurtion(h.getHorseId()) * Constants.MILLSECONDS_PER_DAY);
                h.setEstimationTime(estimationTime);

                List<SequenceObject> seqObjList = seqObjDao.selectSequenceObjectsByWorkflowObjectId(h.getWorkflowObjectId());

                List<SequenceObjectView> seqObjViewList = new ArrayList<SequenceObjectView>();
                if (seqObjList != null) {
                    for (SequenceObject seqObj : seqObjList) {
                        SequenceObjectView seqObjView = new SequenceObjectView();
                        seqObjView.setSeqObj(seqObj);
                        seqObjView.setGoalObjects(seqObjDao.selectGoalObjectsBySequenceObjectId(seqObj.getId()));
                        seqObjViewList.add(seqObjView);
                    }
                    // find the pre-goal and contruct the tree of goals
                    for (SequenceObjectView seqObjView : seqObjViewList) {
                        int seqId = seqObjView.getSeqObj().getWorkflowSequenceId();
                        List<PreGoalView> pregoals = pregoalDao.selectPreGoals(seqId);
                        if (pregoals != null && seqObjView.getGoalObjects().size() > 0) {
                            GoalObjectView firstGoal = seqObjView.getGoalObjects().get(0);
                            for (PreGoalView pregoal : pregoals) {
                                int preGoalId = pregoal.getPreGoalId();
                                boolean found = false;
                                for (SequenceObjectView sov : seqObjViewList) {
                                    // ignore if the sequence is itself OR it is not the pre-workflow
                                    if (sov.getSeqObj().getWorkflowSequenceId() == seqId
                                            || sov.getSeqObj().getWorkflowSequenceId() != pregoal.getPreWorkflowSequenceId()) {
                                        continue;
                                    }
                                    List<GoalObjectView> goalObjectViews = sov.getGoalObjects();
                                    if (goalObjectViews != null) {
                                        for (GoalObjectView gov : goalObjectViews) {
                                            if (preGoalId == gov.getGoalId()) {
                                                found = true;
                                                // Link the sequence by pre-goal
                                                firstGoal.addParent(gov);
                                                gov.addChild(firstGoal);
                                                break;
                                            }
                                        }
                                    }
                                    if (found) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                AssignedTask activeTask = horseDao.selectNextTask(h.getHorseId());

                if (activeTask != null) {
                    h.setActiveTask(activeTask);
                }
                h.setSequences(seqObjViewList);
            }
        }
        return activeHorses;
    }

    /**
     * Get the active horses of the specified user's team. Note, it returns a map of active horses by product.
     * That is, the key is the team name and the value is the active horse list.
     * 
     * @param userId
     * @return A map of the active horses keyed by the product.
     */
    public Map<String, List<ActiveHorseView>> getActiveHorsesOfTeam(int userId, int projectId) {
        // store the active horses by product. KEY: team name; VALUE: active horses of the product
        Map<String, List<ActiveHorseView>> map = new HashMap<String, List<ActiveHorseView>>();
        List<ActiveHorseView> activeHorses = null;
        List<UserTeamInfo> userTeams = teamDao.selectTeamsOfUserIdAndProjectId(userId, projectId);
        List<Integer> idList = null;
        int n = 0;
        if (userTeams != null && (n = userTeams.size()) > 0) {
            idList = new ArrayList<Integer>(n);
            int uid = 0;
            for (int i = 0; i < n; ++i) {
                uid = userTeams.get(i).getUserid();
                if (!idList.contains(uid)) {
                    idList.add(uid);
                }
            }
            Integer[] uids = new Integer[idList.size()];
            // get the active horses
            activeHorses = getActiveHorsesOfUsers(idList.toArray(uids), projectId);
            if (activeHorses == null) {
                return map;
            }
            Integer[] horseIds = new Integer[activeHorses.size()];
            for (int i = 0, size = activeHorses.size(); i < size; ++i) {
                horseIds[i] = activeHorses.get(i).getHorseId();
            }

            // To improve the performance, get all the joined users in advance.
            Map<Integer, List<Integer>> userJoinHorseMap = horseDao.selectUsersJoinInHorses(uids, horseIds);

            //List<Integer> userJoins
            for (UserTeamInfo aTeam : userTeams) {
                for (ActiveHorseView ah : activeHorses) {
                    List<Integer> joinedUsers = userJoinHorseMap.get(ah.getHorseId());
                    if (joinedUsers == null || joinedUsers.size() == 0) {
                        continue;
                    }
                    List<ActiveHorseView> list = map.get(aTeam.getTeamName());
                    if (list == null) {
                        list = new ArrayList<ActiveHorseView>();
                        map.put(aTeam.getTeamName(), list);
                    }

                    // check if it is existed or not
                    if (joinedUsers.contains(aTeam.getUserid()) && !hasExisted(list, ah)) {
                        ah.setUserJoinedIn(joinedUsers.contains(userId));
                        list.add(ah);
                    }
                }
            }
        }
        return map;
    }

    public ContentHeader getContentHeaderByHorseId(int horseId) {
        //log.debug("Select content header by id: " + horseId + ".");
        return contentHeaderDao.selectContentHeaderByHorseId(horseId);
    }

    public void saveContentHeader(ContentHeader ch) {
        contentHeaderDao.update(ch);
    }

    public String getInstructionsByHorseId(int horseId) {
        log.debug("Select content header by id: " + horseId + ".");
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return null;
        }
        String instructions = null;
        if (Constants.CONTENT_TYPE_JOURNAL == cntHdr.getContentType()) {
            instructions = horseDao.selectJournalInstructionsByCntObjId(cntHdr.getContentObjectId());
        } else {
            instructions = horseDao.selectSurveyInstructionsByCntObjId(cntHdr.getContentObjectId());
        }
        return instructions;
    }

    // Check if the specified horse object is existed in the list. ONLY check by the horseId.
    private boolean hasExisted(List<ActiveHorseView> list, ActiveHorseView obj) {
        if (list == null) {
            return false;
        }
        for (ActiveHorseView h : list) {
            if (h.getHorseId() == obj.getHorseId()) {
                return true;
            }
        }
        return false;
    }

    public Horse getHorseByContentHeaderId(Long contentHeaderId) {
        return horseDao.selectHorseByContentHeaderId(contentHeaderId);
    }

    public List<Horse> getHorsesWithTheSameProdIdByHorseId(int horseId) {
        int productId = horseDao.get(horseId).getProductId();
        List<Horse> horses = horseDao.selectHorseByProdId(productId);
        return horses;
    }

    public double getGoalObjectCompletedPercentage(GoalObject goalObject) {
        log.debug("Get the completed percentage for goalObject: " + goalObject.getId());
        List<TaskAssignment> taList = taskAssignmentDao.selectTaskAssignmentsByGoalObject(goalObject.getId());
        if (taList == null) {
            return 0;
        }
        int allTa = taList.size();
        int completedTa = 0;
        for (TaskAssignment ta : taList) {
            if (ta.getStatus() == (short) Constants.TASK_STATUS_DONE) {
                completedTa++;
            }
        }
        return (allTa == 0) ? 0 : (completedTa * 1.0d) / allTa;
    }

    public int getGoalObjectCompletedCount(GoalObject goalObject) {
        log.debug("Get the completed count for goalObject: " + goalObject.getId());
        List<TaskAssignment> taList = taskAssignmentDao.selectCompletedTaskAssignmentsByGoalObject(goalObject.getId());
        int count = (taList == null) ? 0 : taList.size();
        log.debug("============>>>>> " + count);
        return count;
    }

    public Pagination<HorseInfo> getHorsesByProductId(int productId, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        long totalCount = horseDao.countOfHorseCountByProductId(productId);
        List<HorseInfo> horses = horseDao.selectHorsesByProductId(productId, sortName, sortOrder, offset, count);

        Pagination<HorseInfo> pagination = new Pagination<HorseInfo>(totalCount, page, pageSize);
        pagination.setRows(horses);
        return pagination;
    }

    public Map<Integer, String> getTargetsByHorseIds(List<Integer> horseIds) {
        return horseDao.selectTargetsByHorseIds(horseIds);
    }

    public List<Horse> getHorsesByIds (List<Integer> horseIds) {
        return horseDao.selectHorsesByIds(horseIds);
    }


    public WorkflowObject getWorkflowObject(int workflowObjectId){
        return workflowObjectDao.get(workflowObjectId);
    }

    public void updateWorkflowObjectStatus(int workflowObjectId, int newStatus){
       workflowObjectDao.updateWorkflowObjectStatusById(workflowObjectId, newStatus);
    }

    public void updateWorkflowObjectCancel(int workflowObjectId){
        workflowObjectDao.updateWorkflowObjectCancelById(workflowObjectId);
    }

    public void updateWorkflowObjectUncancel(int workflowObjectId){
        workflowObjectDao.updateWorkflowObjectUncancelById(workflowObjectId);
    }

    public int resetHorse(Horse horse){
        int rt = horseDao.call(Constants.PROCEDURE_INIT_HORSE, horse.getProductId(), horse.getTargetId());
        return rt;
    }

    public int createSurveyVersion(int horseId, String description, int userId){
        int rt = horseDao.call(Constants.PROCEDURE_CREATE_SURVEY_VERSION, horseId, description, userId);
        return rt;
    }

    @Autowired
    public void setTaskDao(HorseDAO horseDao) {
        this.horseDao = horseDao;
    }

    @Autowired
    public void setSeqenceObjectDao(SequenceObjectDAO seqObjDao) {
        this.seqObjDao = seqObjDao;
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setTeamDao(TeamDAO teamDao) {
        this.teamDao = teamDao;
    }

    @Autowired
    public void setCaseDao(CaseDAO caseDao) {
        this.caseDao = caseDao;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermisssionService) {
        this.viewPermisssionService = viewPermisssionService;
    }

    @Autowired
    public void setPregoalDao(PregoalDAO pregoalDao) {
        this.pregoalDao = pregoalDao;
    }

    @Autowired
    public void setGoalObjectDao(GoalObjectDAO goalObjectDao) {
        this.goalObjectDao = goalObjectDao;
    }

    @Autowired
    public void setTaskAssignmentDao(TaskAssignmentDAO taskAssignmentDao) {
        this.taskAssignmentDao = taskAssignmentDao;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setWorkflowObjectDao(WorkflowObjectDAO workflowObjectDao) {
        this.workflowObjectDao = workflowObjectDao;
    }
}
