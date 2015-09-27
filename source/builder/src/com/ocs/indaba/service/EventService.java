/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.EventDAO;
import com.ocs.indaba.dao.EventLogDAO;
import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.dao.GoalObjectDAO;
import com.ocs.indaba.po.Event;
import com.ocs.indaba.po.EventLog;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.util.DateUtils;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class EventService {
    private static final Logger logger = Logger.getLogger(EventService.class);
    private EventDAO eventDao;
    private EventLogDAO eventLogDao;
    private TaskAssignmentDAO taskAssignmentDao;
    private GoalObjectDAO goalObjectDao;

    public void addEvent(Event event) {
        eventDao.insertEvent(event);
    }

    public List<Event> getAllEventsBefore(int userId, int eventType, Date date) {
        return eventDao.selectAllEventsBefore(userId, eventType, date);
    }

    public List<Event> getAllEventsAfter(int userId, int eventType, Date date) {
        return eventDao.selectAllEventsAfter(userId, eventType, date);
    }
    public Event getEventBefore(int userId, int eventType, Date date) {
        return eventDao.selectEventBefore(userId, eventType, date);
    }

    public Event getEventAfter(int userId, int eventType, Date date) {
        return eventDao.selectEventAfter(userId, eventType, date);
    }

    public Event getSpacialEventBefore(int userId, int eventType, Date date, String containedEventData) {
        return eventDao.selectSpacialEventBefore(userId, eventType, date, containedEventData);
    }

    public Event getSpacialEventAfter(int userId, int eventType, Date date, String containedEventData) {
        return eventDao.selectSpacialEventAfter(userId, eventType, date, containedEventData);
    }

    public List<Event> getAllEvents(int userId, int eventType) {
        return eventDao.selectAllEvents(userId, eventType);
    }

    public List<Event> getAllEvents(int userId) {
        return eventDao.selectAllEvents(userId);
    }


    public boolean findMsgEvent(TaskAssignment ta, String notificationType) {
        String containedEventData = "'%[assignid=" + ta.getId() + "][nt=" + notificationType + "]%'";
        Event event = eventDao.selectSpecialEventByTaskAssignmentIdAndEventType(
                ta.getId(), 1, containedEventData);
        return (event != null);
    }

    public int findDaysFromMsgEvent(TaskAssignment ta, String notificationType) {
        String containedEventData = "'%[assignid=" + ta.getId() + "][nt=" + notificationType + "]%'";
        Event event = eventDao.selectSpecialEventByTaskAssignmentIdAndEventType(
                ta.getId(), 1, containedEventData);
        if (event == null)
            return -1;
        return DateUtils.getDaysFrom(event.getTimestamp());
    }

    public int findMinutesFromMsgEvent(TaskAssignment ta, String notificationType) {
        String containedEventData = "'%[assignid=" + ta.getId() + "][nt=" + notificationType + "]%'";
        Event event = eventDao.selectSpecialEventByTaskAssignmentIdAndEventType(
                ta.getId(), 1, containedEventData);
        if (event == null)
            return -1;
        logger.debug("======### Found Event:" + event.getId() + " ###=======");
        return DateUtils.getMinutesFrom(event.getTimestamp());
    }

    public boolean findMsgEventByGoalObject(GoalObject go, String notificationType) {
        String containedEventData = "'%[assignid=" + go.getId() + "][nt=" + notificationType + "]%'";
        Event event = eventDao.selectSpecialEventByGoalObjectIdAndEventType(
                go.getId(), 1, containedEventData);
        return (event != null);
    }

    public void addMsgEvent(TaskAssignment ta, String notificationType) {
        String containedEventData = "Message Notification.[assignid=" + ta.getId() + "][nt=" + notificationType + "]";
        if (ta.getEventLogId() == null || ta.getEventLogId() == 0) {
            long eventLogId = eventLogDao.insertEventLog(new EventLog());
            ta.setEventLogId((int)eventLogId);
            taskAssignmentDao.updateTaskAssignmentEventLog(ta.getId(), ta.getEventLogId());
        }

        Event event = new Event();
        event.setEventLogId(ta.getEventLogId());
        event.setEventType((short)1);
        event.setTimestamp(new Date());
        event.setUserId(new Integer(1));
        event.setEventData(containedEventData);
        eventDao.insertEvent(event);
    }

    public void addMsgEventByGoalObject(GoalObject go, String notificationType) {
        String containedEventData = "Message Notification.[assignid=" + go.getId() + "][nt=" + notificationType + "]";
        if (go.getEventLogId() == null || go.getEventLogId() == 0) {
            long eventLogId = eventLogDao.insertEventLog(new EventLog());
            go.setEventLogId((int)eventLogId);
            goalObjectDao.updateEventLogId(go);
        }

        Event event = new Event();
        event.setEventLogId(go.getEventLogId());
        event.setEventType((short)1);
        event.setTimestamp(new Date());
        event.setUserId(new Integer(1));
        event.setEventData(containedEventData);
        eventDao.insertEvent(event);
    }

    @Autowired
    public void setEventDao(EventDAO eventDao) {
        this.eventDao = eventDao;
    }

    @Autowired
    public void setEventLogDao(EventLogDAO eventLogDao) {
        this.eventLogDao = eventLogDao;
    }

    @Autowired
    public void setTaskAssignmentDao(TaskAssignmentDAO taskAssignmentDao) {
        this.taskAssignmentDao = taskAssignmentDao;
    }

    @Autowired
    public void setGoalObjectDao(GoalObjectDAO goalObjectDao) {
        this.goalObjectDao = goalObjectDao;
    }
}
