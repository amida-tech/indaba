/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.dao.GoalDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.TargetDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.dao.ToolDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.po.WorkflowObject;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.ProductService;
import com.ocs.indaba.service.ProjectService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author luwenbin
 */
@Service
public class ProductControlPanelService {
    private static final Logger log = Logger.getLogger(ProductControlPanelService.class);
    @Autowired
    private ProjectDAO projectDao = null;

    @Autowired
    private TargetDAO targetDao = null;

    @Autowired
    private UserDAO userDao = null;

    @Autowired
    private ProductDAO productDao;

    @Autowired
    private GoalDAO goalDao;

    @Autowired
    private TaskDAO taskDao;

    @Autowired
    private ToolDAO toolDao;

    @Autowired
    private HorseDAO horseDao;

    @Autowired
    private SurveyIndicatorDAO surveyIndicatorDao;

    @Autowired
    private HorseService horseSrvc;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProductService prodSrvc;

    public int checkReadyForTest(Product product){
        int productId = product.getId();
        int projectId = product.getProjectId();
        //check "There has to be at least one target in the project"
        if(!projectService.existsTargetsByProjectId(projectId))
            return ControlPanelConstants.PRODUCT_NOT_READY_REASON_TARGET;

        //check "There has to be at least one contributor in the project"
        if(!projectService.existsMembershipByProjectId(projectId))
            return ControlPanelConstants.PRODUCT_NOT_READY_REASON_CONTRIBUTOR;

        List<Goal> goals = goalDao.selectGoalsByWorkflowId(product.getWorkflowId());
        List<Task> tasks = taskDao.selectTasksByProductId(productId);
        if(goals == null || goals.size() == 0)
            return ControlPanelConstants.PRODUCT_NOT_READY_REASON_NO_GOAL;

        if(tasks == null || tasks.size() == 0)
            return ControlPanelConstants.PRODUCT_NOT_READY_REASON_NO_TASK;

        if (tasks.size() < goals.size()) {
            return ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_FOR_EACH_GOAL;
        }

        // used to check whether each goal is used in any task
        HashMap<Integer, Boolean> goalMap = new HashMap<Integer, Boolean>();
        for (Goal g : goals) {
            goalMap.put(g.getId(), Boolean.FALSE);
        }

        for (Task t : tasks) {
            if (t.getGoalId() < 0) {
                // this task has no goal
                return ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_BELONG_TO_GOAL;
            }
            Boolean used = goalMap.get(t.getGoalId());

            if (used == null) {
                // the referenced goal doesn't belong to the workflow
                return ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_BELONG_TO_GOAL;
            }

            goalMap.put(t.getGoalId(), true);
        }

        for (Boolean used : goalMap.values()) {
            if (!used) {
                // not every goal has a task
                return ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_FOR_EACH_GOAL;
            }
        }

        List<Tool> tools= toolDao.findAll();
        Set<Integer> toolIds = new HashSet<Integer>();
        if(tools != null && tools.size() > 0) {
            for(Tool tool : tools) toolIds.add(tool.getId());
        }

        for(Task task : tasks){            
            //check The tool_id and type must be set to valid values
            int toolId = task.getToolId();
            int type = task.getType();
            if(!toolIds.contains(toolId) || !toolIds.contains(type))
                return ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_VALID_ID_TYPE;
        }

        return 0;
    }

     public int cloneProduct(int fromProductId, String prodName){
        int rt = productDao.call(Constants.PROCEDURE_CLONE_PRODUCT, fromProductId, prodName);
        if(rt != 0)
            log.error("Call 'clone_product' procedure return " + rt);
        return rt;
    }

    public int startProduct(int productId){
        int rt = productDao.call(Constants.PROCEDURE_START_PRODUCT, productId);
        if (rt != 0) {
            log.error("Call 'start_product' procedure return " + rt);
        } else {
            prodSrvc.updateProductMode(productId,  Constants.PRODUCT_MODE_TEST);
        }
        return rt;
    }

     public int resetProduct(int productId){
        int rt = productDao.call(Constants.PROCEDURE_RESET_PRODUCT, productId);

        if (rt != 0) {
            log.error("Call 'reset_product' procedure return " + rt);
        } else {
            prodSrvc.updateProductMode(productId,  Constants.PRODUCT_MODE_CONFIG);
        }
        return rt;
    }

    public void resetHorseForProduct(int productId){
        List<Horse> horses = horseDao.selectHorseByProdId(productId);
        if(horses != null && horses.size() > 0){
            for(Horse horse : horses){
                WorkflowObject wo = horseSrvc.getWorkflowObject(horse.getWorkflowObjectId());
                if(wo.getStatus() != Constants.HORSE_STATUS_INITIAL){
                    int rt = productDao.call(Constants.PROCEDURE_INIT_HORSE, horse.getProductId(), horse.getTargetId());
                    if(rt != 0)
                        log.error("Call 'init horse' procedure return " + rt);
                }
            }
        }
    }

    public void promoteIndicatorsUsedByProduct(Product product){
        //moves all “public test” indicators in the product’s survey_config to “public extended” state.
        List<SurveyIndicator> indicators = surveyIndicatorDao.selectSurveyIndicatorByConfigId(product.getProductConfigId());
        for(SurveyIndicator indicator : indicators){
            if(indicator.getState() == Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_TEST)
                surveyIndicatorDao.updateStateById(Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED, indicator.getId());
        }
    }
}
