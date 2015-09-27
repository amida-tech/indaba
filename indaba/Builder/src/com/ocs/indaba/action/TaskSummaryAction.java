
package com.ocs.indaba.action;

import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.TaskService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskSummaryAction extends BaseAction {

    @Autowired
    private TaskService taskService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // get all current task assignments (for now, only flag related)
        taskService.generateTaskSummary();
        return null;
    }
}
