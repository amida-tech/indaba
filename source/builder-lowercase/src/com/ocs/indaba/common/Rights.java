/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.common;

/**
 *
 * @author Jeff
 */
public final class Rights {

    public static final String NON_EXISTED_RIGHT = "NON_EXISTED_RIGHT";
    public static final String READ_ANNOUNCEMENTS = "read announcements";
    public static final String READ_PROJECT_WALL = "read project wall";
    public static final String WRITE_PROJECT_WALL = "write project wall";
    public static final String READ_CONTENT_DETAILS = "read content details";
    public static final String SEE_YOUR_CONTENT_BOX = "see your content box";
    public static final String READ_CONTENT_DETAILS_OF_OTHERS = "read content details of others";
    public static final String READ_CONTENT_STATUS_OF_OTHERS = "read content status of others";
    public static final String SEE_ALL_CONTENT = "see all content";
    public static final String MANAGE_QUEUES = "manage queues";
    public static final String OPEN_CASES = "open cases";
    public static final String CLOSE_ALL_CASES = "close all cases";
    public static final String ATTACH_ANY_CONTENT_TO_CASES = "attach any content to cases";
    public static final String ATTACH_ANY_USER_TO_CASES = "attach any user to cases";
    public static final String ASSIGN_ANY_USER_TO_CASES = "assign any user to cases";
    public static final String MANAGE_ALL_CASES = "manage all cases";
    public static final String BLOCK_CONTENT_PROGRESS = "block content progress";
    public static final String MANAGE_ALL_USERS = "manage all users";
    public static final String WRITE_TO_TEAMS = "write to teams";
    public static final String SEE_CONTENT_CURRENT_TASKS = "see content current tasks";
    public static final String SEE_CONTENT_CASES = "see content cases";
    public static final String READ_CONTENT_INTERNAL_DISCUSSION = "read content internal discussion";
    public static final String WRITE_CONTENT_INTERNAL_DISCUSSION = "write content internal discussion";
    public static final String MANAGE_CONTENT_INTERNAL_DISCUSSION = "manage content internal discussion";
    public static final String READ_JOURNAL_STAFF_AUTHOR_DISCUSSION = "read journal staff-author discussion";
    public static final String MANAGE_JOURNAL_STAFF_AUTHOR_DISCUSSION = "manage journal staff-author discussion";
    public static final String READ_JOURNAL_PEER_REVIEWS = "read journal peer reviews";
    public static final String READ_JOURNAL_PEER_REVIEW_DISCUSSIONS = "read journal peer review discussions";
    public static final String MANAGE_JOURNAL_PEER_REVIEW_DISCUSSIONS = "manage journal peer review discussions";
    public static final String READ_SURVEY_STAFF_AUTHOR_DISCUSSION = "read survey staff-author discussion";
    public static final String MANAGE_SURVEY_STAFF_AUTHOR_DISCUSSION = "manage survey staff-author discussion";
    public static final String READ_SURVEY_PEER_REVIEWS = "read survey peer reviews";
    public static final String READ_SURVEY_PEER_REVIEW_DISCUSSIONS = "read survey peer review discussions";
    public static final String MANAGE_SURVEY_PEER_REVIEW_DISCUSSIONS = "manage survey peer review discussions";
    public static final String TAG_INDICATORS = "tag indicators";
    public static final String SEE_TARGET_INDICATOR_VALUES = "see target indicator values";
    public static final String USE_INDABA_ADMIN = "use indaba admin";
    public static final String MANAGE_SITE = "manage site";
    public static final String ACCESS_CASE_STAFF_NOTES = "access case staff notes";
    public static final String EDIT_ANY_CASES = "edit any cases";
    public static final String SUPER_EDIT_CONTENT = "super edit content";
    public static final String WRITE_TO_ROLES = "write to roles";
    public static final String EDIT_ASSIGNMENT_DEADLINES = "edit assignment deadlines";
    public static final String SEE_JOURNAL_FILE_ATTACHMENTS = "see journal file attachments";
    public static final String SEE_INDICATOR_FILE_ATTACHMENTS = "see indicator file attachments";
    public static final String MANAGE_WIDGETS = "manage widgets";
    public static final String MANAGE_WORKING_SETS = "manage working sets";
    public static final String EXPORT_SCORECARD_REPORTS = "export scorecard reports";
    public static final String DOWNLOAD_AND_ATTACH_FILES_TO_JOURNAL = "download and attach files to journal";
    public static final String DELETE_FILES_FROM_JOURNAL = "delete files from journal";
    public static final String DOWNLOAD_AND_ATTACH_FILES_TO_SURVEY = "download and attach files to survey";
    public static final String DELETE_FILES_FROM_SURVEY = "delete files from survey";
    public static final String SEE_TASK_QUEUES = "see task queues";
    public static final String SEE_SURVEY_VERSIONS = "see survey versions";
    public static final String READ_SURVEY_STAFF_AUTHOR_DISCUSSION_ON_PEER_REVIEW = "read survey staff-author discussion on peer review";
    public static final String MANAGE_SURVEY_STAFF_AUTHOR_DISCUSSION_ON_PEER_REVIEW = "manage survey staff-author discussion on peer review";
    public static final String SEE_TASK_OVERVIEW_GRID = "see task overview grid";
    
    private static final String ss[] = new String[]{
        "read announcements",
        "read project wall",
        "write project wall",
        "read content details of others",
        "read content status of others",
        "see all content",
        "manage queues",
        "open cases",
        "close all cases",
        "attach any content to cases",
        "attach any user to cases",
        "assign any user to cases",
        "manage all cases",
        "block content progress",
        "manage all users",
        "write to teams",
        "see content current tasks",
        "see content cases",
        "read content internal discussion",
        "write content internal discussion",
        "manage content internal discussion",
        "read journal staff-author discussion",
        "manage journal staff-author discussion",
        "read journal peer reviews",
        "read journal peer review discussions",
        "manage journal peer review discussions",
        "read survey staff-author discussion",
        "manage survey staff-author discussion",
        "read survey peer reviews",
        "read survey peer review discussions",
        "manage survey peer review discussions",
        "tag indicators",
        "see target indicator values",
        "use indaba admin",
        "manage site",
        "access case staff notes",
        "edit any cases",
        "super edit content",
        "write to roles",
        "edit assignment deadlines",
        "see journal file attachments",
        "see indicator file attachments",
        "manage widgets",
        "manage working sets",
        "export scorecard reports",
        "download and attach files to journal",
        "delete files from journal",
        "download and attach files to survey",
        "delete files from survey",
        "see task queues",
        "see survey versions",
        "read survey staff-author discussion on peer review",
        "manage survey staff-author discussion on peer review"
    };

    public static void main(String args[]) {
        for (String s : ss) {
            StringBuffer sBuf = new StringBuffer("public static final String ");
            sBuf.append(s.replaceAll(" ", "_").replaceAll("-", "_").toUpperCase());
            sBuf.append(" = \"").append(s).append("\";");
            System.out.println(sBuf.toString());
        }
    }
}
