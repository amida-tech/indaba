/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.ToolDAO;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.vo.ToolIntl;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jeff
 */
@Component
public class ToolService {

    private static final Logger logger = Logger.getLogger(ToolService.class);
    private static ToolDAO toolDao = null;
    private static List<Tool> toolList = null;
    private static HashMap<Integer, Tool> toolMap = null;

    private static void getTools() {
        if (toolList != null) {
            return;
        }

        toolList = toolDao.selectTools();
        toolMap = new HashMap<Integer, Tool>();

        for (Tool tool : toolList) {
            toolMap.put(tool.getId(), tool);
        }
    }

    public List<Tool> getAllTools() {
        getTools();
        return toolList;
    }

    public Tool getToolById(int toolId) {
        getTools();
        return toolMap.get(toolId);
    }

    public Tool getToolActionByTaskAssignmentId(int taskAssignmentId) {
        return toolDao.selectToolByTaskAssignmentId(taskAssignmentId);
    }

    public int getToolTaskTypeByTaskAssignmentId(int taskAssignmentId) {
        Tool tool = toolDao.selectToolByTaskAssignmentId(taskAssignmentId);
        return (tool == null) ? -1 : tool.getTaskType();
    }

    public ToolIntl getToolIntl(int toolId, int languageId) {
        Tool tool = toolDao.get(toolId);
        if (tool == null) return null;
        ToolIntl toolIntl = new ToolIntl();
        toolIntl.setToolId(toolId);
        toolIntl.setLanguageId(languageId);
        toolIntl.setInactiveReason(Messages.getInstance().getMessage(tool.getInactiveReason(), languageId));
        toolIntl.setPurpose(Messages.getInstance().getMessage(tool.getPurpose(), languageId));
        return toolIntl;
    }

    @Autowired
    public void setToolDao(ToolDAO toolDAO) {
        toolDao = toolDAO;
    }

}
