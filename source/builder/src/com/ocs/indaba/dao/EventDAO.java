/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Event;
import com.ocs.indaba.util.DateUtils;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class EventDAO extends SmartDaoMySqlImpl<Event, Integer> {

    private static final Logger log = Logger.getLogger(EventDAO.class);
    private static final String SELECT_EVENT_BEFORE = "SELECT * FROM event WHERE user_id=? AND event_type=? AND timestamp<=?";
    private static final String SELECT_EVENT_AFTER = "SELECT * FROM event WHERE user_id=? AND event_type=? AND timestamp>=?";
    private static final String SELECT_ALL_EVENTS_BY_USERID = "SELECT * FROM event WHERE user_id=?";
    private static final String SELECT_ALL_EVENTS_BY_USERID_AND_EVENTTYPE = "SELECT * FROM event WHERE user_id=? AND event_type=?";
    private static final String SELECT_SPECIAL_EVENT_BEFORE_BY_USERID_AND_EVENTTYPE = "SELECT * FROM event WHERE user_id=? AND event_type=? AND timestamp<=? AND event_data LIKE ?";
    private static final String SELECT_SPECIAL_EVENT_AFTER_BY_USERID_AND_EVENTTYPE = "SELECT * FROM event WHERE user_id=? AND event_type=? AND timestamp>=? AND event_data LIKE ?";
    private static final String SELECT_SPECIAL_EVENT_BY_TASK_ASSIGNMENT_ID_AND_EVENTTYPE =
            "SELECT e.* FROM event e " +
            "JOIN event_log el ON (e.event_log_id = el.id) " +
            "JOIN task_assignment ta ON (ta.event_log_id = el.id) " +
            "WHERE ta.id = ? AND e.event_type = ? AND e.event_data LIKE ";
    private static final String SELECT_SPECIAL_EVENT_BY_GOAL_OBJECT_ID_AND_EVENTTYPE =
            "SELECT e.* FROM event e " +
            "JOIN event_log el ON (e.event_log_id = el.id) " +
            "JOIN goal_object go ON (go.event_log_id = el.id) " +
            "WHERE go.id = ? AND e.event_type = ? AND e.event_data LIKE ";

    public void insertEvent(Event event) {
        log.debug("Insert event");
        super.create(event);
    }

    public Event selectEvent(int eventId) {
        log.debug("Select event by id: " + eventId);
        return super.get(eventId);
    }

    public Event selectEventBefore(int userId, int eventType, Date date) {
        log.debug("Select event before: " + date + "[userid=" + userId + ", eventType=" + eventType + "].");
        String dateStr = DateUtils.datetime2Str(date);
        return super.findSingle(SELECT_EVENT_BEFORE, (Object) userId, eventType, dateStr);
    }

    public Event selectEventAfter(int userId, int eventType, Date date) {
        log.debug("Select event after: " + date + "[userid=" + userId + ", eventType=" + eventType + "].");
        String dateStr = DateUtils.datetime2Str(date);
        return super.findSingle(SELECT_EVENT_AFTER, (Object) userId, eventType, dateStr);
    }

    public List<Event> selectAllEventsBefore(int userId, int eventType, Date date) {
        log.debug("Select event before: " + date + "[userid=" + userId + ", eventType=" + eventType + "].");
        String dateStr = DateUtils.datetime2Str(date);
        return super.find(SELECT_EVENT_BEFORE, (Object) userId, eventType, dateStr);
    }

    public List<Event> selectAllEventsAfter(int userId, int eventType, Date date) {
        log.debug("Select event after: " + date + "[userid=" + userId + ", eventType=" + eventType + "].");
        String dateStr = DateUtils.datetime2Str(date);
        return super.find(SELECT_EVENT_AFTER, (Object) userId, eventType, dateStr);
    }

    public List<Event> selectAllEvents(int userId, int eventType) {
        log.debug("Select events: [userid=" + userId + ", eventType=" + eventType + "].");
        return super.find(SELECT_ALL_EVENTS_BY_USERID_AND_EVENTTYPE, (Object) userId, eventType);
    }

    public List<Event> selectAllEvents(int userId) {
        log.debug("Select events: [userid=" + userId + "].");
        return super.find(SELECT_ALL_EVENTS_BY_USERID, userId);
    }

    public Event selectSpacialEventBefore(int userId, int eventType, Date date, String containedEventData) {
        log.debug("Select events: " + SELECT_SPECIAL_EVENT_BEFORE_BY_USERID_AND_EVENTTYPE
                + " [userid=" + userId + ", eventType=" + eventType + ", date=" + date + "].");
        return super.findSingle(SELECT_SPECIAL_EVENT_BEFORE_BY_USERID_AND_EVENTTYPE, userId, eventType, date, containedEventData);
    }

    public Event selectSpacialEventAfter(int userId, int eventType, Date date, String containedEventData) {
        log.debug("Select events: [userid=" + userId + "].");
        return super.findSingle(SELECT_SPECIAL_EVENT_AFTER_BY_USERID_AND_EVENTTYPE, userId, eventType, date, containedEventData);
    }

    public Event selectSpecialEventByTaskAssignmentIdAndEventType(
        int assignid, int eventType, String containedEventData) {
        return super.findSingle(SELECT_SPECIAL_EVENT_BY_TASK_ASSIGNMENT_ID_AND_EVENTTYPE + containedEventData + " ORDER BY e.id DESC",
                assignid, eventType);
    }

    public Event selectSpecialEventByGoalObjectIdAndEventType(
        int assignid, int eventType, String containedEventData) {
        return super.findSingle(SELECT_SPECIAL_EVENT_BY_GOAL_OBJECT_ID_AND_EVENTTYPE + containedEventData + " ORDER BY e.id DESC",
                assignid, eventType);
    }
}
