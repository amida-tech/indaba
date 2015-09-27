/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.surveyconf;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.exception.NoDataException;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.ScContributor;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.po.SurveyConfig;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyQuestion;
import com.ocs.indaba.service.SurveyConfigService;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.vo.SurveyConfigVO;
import com.ocs.indaba.vo.SurveyIndicatorView;
import com.ocs.indaba.vo.SurveyQuestionTreeView;
import com.ocs.util.JSONUtils;
import com.ocs.util.ListUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Jeff Jiang
 *
 */
@Results({
    @Result(name = "edit", location = "survey-config-content.jsp"),
    @Result(name = "create", location = "survey-config-content.jsp")})
public class SurveyConfigController extends BaseController {

    private static final long serialVersionUID = -2487852558172383390L;
    private static final Logger logger = Logger.getLogger(SurveyConfigController.class);
    private static final String PARAM_VISIBILITY = "visibility";
    private static final String PARAM_TIP_DISPLAY_METHOD = "tipDisplayMethod";
    private static final String PARAM_FILTER_ORG_IDS = "filterOrgIds";
    private static final String PARAM_INDICATOR_IDS = "indicatorIds[]";
    private static final String PARAM_QUESTION = "question";
    private static final String PARAM_SURVEY_CONFIG_ID = "sconfId";
    private static final String PARAM_SOURCE_SURVEY_CONFIG_ID = "srcSconfId";
    private static final String PARAM_SURVEY_CATEGORY_ID = "catId";
    private static final String PARAM_PARENT_SURVEY_CATEGORY_ID = "pCatId";
    private static final String PARAM_SURVEY_QUESTION_ID = "qstnId";
    private static final String PARAM_SURVEY_CONFIG_NAME = "scName";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_INSTRUCTIONS = "instructions";
    private static final String PARAM_SECONDARY_ORGS = "secondaryOrgs[]";
    private static final String PARAM_PRIMARY_ORG = "primaryOrg";
    private static final String PARAM_ORGANIZATION = "organization";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_LABEL = "label";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_WEIGHT = "weight";
    private static final String PARAM_SURVEY_INDICATOR_ID = "indicatorId";
    private static final String PARAM_ORIG_NODE_ID = "origNodeId";
    private static final String PARAM_ORIG_NODE_IS_CATEGORY = "origNodeIsCat";
    private static final String PARAM_TARGET_NODE_ID = "targetNodeId";
    private static final String PARAM_TARGET_PARENT_ID = "targetParentId";
    private static final String PARAM_TARGET_NODE_IS_CATEGORY = "targetNodeIsCat";
    private static final String PARAM_MOVE_TYPE = "moveType";
    private static final String ATTR_SURVEY_CONFIG = "sc";
    private static final String ATTR_SURVEY_CONFIG_USED = "used";
    private static final String ATTR_SURVEY_CONFIG_OWNED = "owned";
    private static final String ATTR_ORGANIZATIONS = "orgs";
    private static final String ATTR_SC_ORGS = "scorgs";
    private static final String ATTR_PRIMARY_ORGS = "primaryorgs";
    private static final String ATTR_SECONDARY_ORGS = "secondaryorgs";
    private static final String MOVE_TYPE_INNER = "inner";
    private static final String MOVE_TYPE_NEXT = "next";
    private static final String MOVE_TYPE_PRE = "prev";
    @Autowired
    private SurveyConfigService surveyConfigSrvc = null;

    public String index() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        logger.debug("Request Params: \n\tvisibility=" + visibility);
        LoginUser loginUser = getLoginUser();

        int orgVisibility = (visibility > 0) ? visibility : ControlPanelConstants.VISIBILITY_PUBLIC;
        request.setAttribute(ATTR_ORGANIZATIONS, loginUser.getAccessibleOrgs(orgVisibility));
        request.setAttribute(PARAM_VISIBILITY, orgVisibility);
        return RESULT_INDEX;
    }

    public String execute() {
        return index();
    }

    public String find() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        String orgIdsStr = request.getParameter(PARAM_FILTER_ORG_IDS);
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        List<Integer> filterOrgIds = null;
        if (StringUtils.isEmpty(orgIdsStr)) {
            filterOrgIds = new ArrayList<Integer>();
        } else {
            filterOrgIds = ListUtils.strArrToList(orgIdsStr.split(","));
        }
        logger.debug("Request Params: "
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tvisibility=" + visibility
                + "\n\tfilterOrgIds=" + orgIdsStr
                + "\n\tfilterOrgIds[]=" + filterOrgIds
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);

        LoginUser loginUser = super.getLoginUser();
        //boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            Map<Integer, Boolean> ownedOrgMap = new HashMap<Integer, Boolean>();
            List<Integer> ownOrgIds = loginUser.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE);
            for (Integer orgId : ownOrgIds) {
                ownedOrgMap.put(orgId, Boolean.TRUE);
            }

            Pagination<SurveyConfigVO> pagination = surveyConfigSrvc.getSurveyConfigs(loginUser.isSiteAdmin(), loginUser.getUserId(), ownedOrgMap, visibility, filterOrgIds, sortName, sortOrder, page, pageSize);
            //pagination.addProperty("isSysAdmin", isSysAdmin ? "yes" : "no");
            logger.debug(pagination.toJsonString());
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String create() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));

        logger.debug("Request Params: "
                + "\n\tsconfId=" + sconfId
                + "\n\tvisibility=" + visibility);

        request.setAttribute(PARAM_SURVEY_CONFIG_ID, sconfId);
        request.setAttribute(PARAM_VISIBILITY, visibility);
        request.setAttribute(PARAM_TIP_DISPLAY_METHOD, 1);
        request.setAttribute(ATTR_SURVEY_CONFIG_USED, true);
        LoginUser loginUser = getLoginUser();
        request.setAttribute(ATTR_PRIMARY_ORGS, loginUser.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE));
        request.setAttribute(ATTR_SECONDARY_ORGS, loginUser.getAccessibleOrgs(Constants.VISIBILITY_PUBLIC));
        return RESULT_CREATE;
    }

    public String edit() throws NoDataException {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));

        request.setAttribute(PARAM_SURVEY_CONFIG_ID, sconfId);
        request.setAttribute(ATTR_SURVEY_CONFIG_USED, surveyConfigSrvc.checkSurveyConfigInActiveUse(sconfId));
        LoginUser loginUser = super.getLoginUser();

        SurveyConfig sc = surveyConfigSrvc.getSurveyConfig(sconfId);

        if (sc == null) {
            logger.error("Non-existent Survey Config ID: " + sconfId);
            throw new NoDataException(getMessage(ControlPanelMessages.KEY_ERROR_INVALID_SURVEYCONFIG_ID));
        }

        // see if the user is an owner
        List<Integer> ownOrgIds = loginUser.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE);
        List<Organization> primaryOrgs;
        boolean owned;

        if (ownOrgIds.contains(sc.getOwnerOrgId())) {
            // this is an owner
            owned = true;

            // only own org could be used as primary/owner org of the SC
            primaryOrgs = loginUser.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE);
            logger.debug("Set PRIMARY ORGS ATTR to my accessible orgs.");
        } else {
            owned = false;
            primaryOrgs = new ArrayList<Organization>();
            primaryOrgs.add(loginUser.getOrg(sc.getOwnerOrgId()));
            logger.debug("Set PRIMARY ORGS ATTR to SC's owner org.");

        }

        request.setAttribute(PARAM_VISIBILITY, sc.getVisibility());
        request.setAttribute(ATTR_SURVEY_CONFIG_OWNED, owned);
        request.setAttribute(ATTR_PRIMARY_ORGS, primaryOrgs);

        // Any org could be a secondary org of the SC
        request.setAttribute(ATTR_SECONDARY_ORGS, loginUser.getAccessibleOrgs(Constants.VISIBILITY_PUBLIC));

        return RESULT_EDIT;
    }

    public String get() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        SurveyConfig surveyConfig = null;
        JSONObject data = new JSONObject();
        if (sconfId > 0 && (surveyConfig = surveyConfigSrvc.getSurveyConfig(sconfId)) != null) {
            data.put(ATTR_ERROR_CODE, ControlPanelErrorCode.OK);
            data.put(ATTR_SURVEY_CONFIG, JSONUtils.toJson(surveyConfig));
            data.put(ATTR_ORGANIZATIONS, surveyConfigSrvc.getSecondaryOrgIdsBySurveyConfigId(sconfId));
        } else {
            data.put(ATTR_ERROR_CODE, ControlPanelErrorCode.ERR_NOT_EXISTS);
            data.put(ATTR_ERROR_MESSAGE, getMessage(ControlPanelMessages.KEY_ERROR_INVALID_SURVEYCONFIG_ID));
        }
        super.sendResponseJson(data);
        return RESULT_EMPTY;
    }

    public String delete() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        logger.debug("Request Params: \n\tsconfId=" + sconfId);
        if (surveyConfigSrvc.checkSurveyConfigInUse(sconfId)) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_USED, getMessage(ControlPanelMessages.KEY_ERROR_SURVEY_CONFIG_IS_USED));
            return RESULT_EMPTY;
        }

        SurveyConfig surveyConfig = surveyConfigSrvc.getSurveyConfig(sconfId);
        if (surveyConfig == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_SURVEY_CONFIG));
        } else {
            surveyConfigSrvc.deleteSurveyConfig(sconfId);
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        }
        return RESULT_EMPTY;
    }

    public String save() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        String name = request.getParameter(PARAM_SURVEY_CONFIG_NAME);
        String description = request.getParameter(PARAM_DESCRIPTION);
        String instructions = request.getParameter(PARAM_INSTRUCTIONS);
        short visibility = StringUtils.str2short(request.getParameter(PARAM_VISIBILITY));
        short tipDisplayMethod = StringUtils.str2short(request.getParameter(PARAM_TIP_DISPLAY_METHOD));
        int primaryOrg = StringUtils.str2int(request.getParameter(PARAM_PRIMARY_ORG));
        String[] secondaryOrgs = request.getParameterValues(PARAM_SECONDARY_ORGS);
        List<Integer> secondaryOrgIds = ListUtils.strArrToList(secondaryOrgs);
        boolean inUse = false;

        logger.debug("Request Params: "
                + "\n\tsconfId=" + sconfId
                + "\n\tname=" + name
                + "\n\tdescription=" + description
                + "\n\tinstructions=" + instructions
                + "\n\tvisibility=" + visibility
                + "\n\ttipDisplayMethod=" + tipDisplayMethod
                + "\n\tprimaryOrg=" + primaryOrg
                + "\n\tsecondaryOrgIdse=" + secondaryOrgIds);

        if (sconfId > 0 && surveyConfigSrvc.checkSurveyConfigInActiveUse(sconfId)) {
            inUse = true;
        }

        SurveyConfig surveyConfig = null;
        if (sconfId > 0) {
            surveyConfig = surveyConfigSrvc.getSurveyConfigByName(name);
            if (surveyConfig == null) {
                surveyConfig = surveyConfigSrvc.getSurveyConfig(sconfId);
            } else if (surveyConfig.getId() != sconfId) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_SURVEY_CONFIG_NAME, name));
                return RESULT_EMPTY;
            }
            if (surveyConfig == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_SURVEY_CONFIG));
                return RESULT_EMPTY;
            }

            surveyConfig.setName(name);
            surveyConfig.setDescription(description);
            surveyConfig.setTipDisplayMethod(tipDisplayMethod);
            surveyConfig.setInstructions(instructions);

            if (!inUse) {
                surveyConfig.setVisibility(visibility);
                surveyConfig.setOwnerOrgId(primaryOrg);
            }

            surveyConfig = surveyConfigSrvc.updateSurveyConfig(surveyConfig);
        } else {
            // create a new SC
            if (surveyConfigSrvc.getSurveyConfigByName(name) != null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_SURVEY_CONFIG_NAME, name));
                return RESULT_EMPTY;
            }
            surveyConfig = new SurveyConfig();
            surveyConfig.setName(name);
            surveyConfig.setStatus((short) 1);
            surveyConfig.setDescription(description);
            surveyConfig.setVisibility(visibility);
            surveyConfig.setTipDisplayMethod(tipDisplayMethod);
            surveyConfig.setOwnerOrgId(primaryOrg);
            surveyConfig.setInstructions(instructions);
            surveyConfig.setLanguageId(ControlPanelConstants.DEFAULT_LANGUAGE_ID);
            surveyConfig.setMoeAlgorithm((short) 1);
            LoginUser user = getLoginUser();
            surveyConfig.setCreatorOrgId(user.getOrgId());
            surveyConfig.setCreateTime(new Date());
            surveyConfig = surveyConfigSrvc.addSurveyConfig(surveyConfig);
        }

        if (!inUse && !ListUtils.isEmptyList(secondaryOrgIds)) {
            for (int orgId : secondaryOrgIds) {
                ScContributor scc = new ScContributor();
                scc.setSurveyConfigId(surveyConfig.getId());
                scc.setOrgId(orgId);
                surveyConfigSrvc.addScContributor(scc);
            }
        }

        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String clone() {
        int srcSconfId = StringUtils.str2int(request.getParameter(PARAM_SOURCE_SURVEY_CONFIG_ID));
        String name = request.getParameter(PARAM_SURVEY_CONFIG_NAME);
        short visibility = StringUtils.str2short(request.getParameter(PARAM_VISIBILITY));
        int orgId = StringUtils.str2int(request.getParameter(PARAM_ORGANIZATION));
        logger.debug("Request Params: "
                + "\n\tsrcSconfId=" + srcSconfId
                + "\n\tname=" + name
                + "\n\tvisibility=" + visibility
                + "\n\torgId=" + orgId);

        /*
        LoginUser loginUser = getLoginUser();
        List<Organization> ownedOrgs = orgSrvc.getOrganizationsByConfigIdAndOrgIds(sconfId, loginUser.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE));
        if(ListUtils.isEmptyList(ownedOrgs)) {
        super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION, getMessage(ControlPanelMessages.KEY_ERROR_SA_OPERATION_ONLY));
        return RESULT_EMPTY;
        }*/
        if (surveyConfigSrvc.getSurveyConfigByName(name) != null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_SURVEY_CONFIG_NAME, name));
            return RESULT_EMPTY;
        }
        int rc = surveyConfigSrvc.cloneSurveyConfig(srcSconfId, name, orgId);

        if (rc != 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_DB,
                    getMessage(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL));
            return RESULT_EMPTY;
        } else {
            logger.debug("Survey config cloned successfully.");
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        }
        return RESULT_EMPTY;
    }

    public String exists() {
        LoginUser user = getLoginUser();
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        String fieldId = request.getParameter(PARAM_FIELD_ID);
        String fieldValue = request.getParameter(PARAM_FIELD_VALUE);
        logger.debug("Request Params: \n\tsconfId=" + sconfId + "\n\tfieldId=" + fieldId + "\n\tfieldValue=" + fieldValue);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(fieldId);
        SurveyConfig surveyConfig = surveyConfigSrvc.getSurveyConfigByName(fieldValue);

        if (surveyConfig != null && surveyConfig.getId() != sconfId) {
            jsonArr.add(false);
            jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_SURVEY_CONFIG_NAME, fieldValue));
        } else {
            jsonArr.add(true);
        }
        super.sendResponseJson(jsonArr);
        return RESULT_EMPTY;
    }

    public String existsSurveyQuestion() {
        LoginUser user = getLoginUser();
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        int qstnId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_QUESTION_ID));
        String fieldId = request.getParameter(PARAM_FIELD_ID);
        String fieldValue = request.getParameter(PARAM_FIELD_VALUE);
        logger.debug("Request Params: \n\tqstnId=" + qstnId + "\n\tsconfId=" + sconfId + "\n\tfieldId=" + fieldId + "\n\tfieldValue=" + fieldValue);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(fieldId);

        //SurveyQuestion question = surveyService.getSurveyQuestion(qstnId);
        int surveyIndicatorId = StringUtils.str2int(fieldValue);
        SurveyQuestion surveyQuestion = surveyConfigSrvc.getSurveyQuestionExcept(qstnId, sconfId, surveyIndicatorId);

        if (surveyQuestion != null && surveyQuestion.getId() != qstnId) {
            jsonArr.add(false);
            jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_SURVEY_QUESTION));
        } else {
            jsonArr.add(true);
        }
        super.sendResponseJson(jsonArr);
        return RESULT_EMPTY;
    }

    public String getTree() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        logger.debug("Request Params: \n\tsconfId=" + sconfId);

        SurveyTree tree = surveyConfigSrvc.buildTree(sconfId);

        if (tree == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, getMessage(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL));
        } else {
            List<Node> nodeList = tree.listNodes();
            JSONArray catTreeJsonArr = nodeList2ZTreeJSON(nodeList, sconfId);
            super.sendResponseJson(catTreeJsonArr);
        }

        return RESULT_EMPTY;
    }

    public String getOrganizations() {
        LoginUser user = getLoginUser();
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        SurveyConfig sc = surveyConfigSrvc.getSurveyConfig(sconfId);
        List<Organization> orgs = null;

        if (sc != null) {
            if (sc.getVisibility() == ControlPanelConstants.VISIBILITY_PUBLIC) {
                orgs = user.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE);
            } else {
                orgs = new ArrayList<Organization>();
                orgs.add(user.getOrg(sc.getOwnerOrgId()));
            }
        }
        super.sendResponseJson(JSONUtils.listObjToJSONArray(orgs));
        return RESULT_EMPTY;
    }

    /**
     * Find survey indicators
     * @return
     */
    public String findScIndicators() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        logger.debug("Request Params: "
                + "\n\tsconfId=" + sconfId
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            LoginUser user = super.getLoginUser();
            Pagination<SurveyIndicatorView> pagination = surveyConfigSrvc.findScIndicators(sconfId, sortName, sortOrder, page, pageSize);
            pagination.addProperty("owned", userOwnsThisSC(user, sconfId) ? "yes" : "no");

            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    /**
     * Find survey indicators
     * @return
     */
    public String findAvailableIndicators() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        String name = request.getParameter(PARAM_NAME);
        String question = request.getParameter(PARAM_QUESTION);
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);

        logger.debug("Request Params: "
                + "\n\tsconfId=" + sconfId
                + "\n\tname=" + name
                + "\n\tquestion=" + question
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        LoginUser user = getLoginUser();
        SurveyConfig sc = surveyConfigSrvc.getSurveyConfig(sconfId);

        if (sc == null) {
            logger.error("Non-existent Survey Config ID: " + sconfId);
            return RESULT_EMPTY;
        }

        // get org candidates
        List<Integer> candidateOrgIds;
        if (sc.getVisibility() == Constants.VISIBILITY_PUBLIC) {
            // all orgs are candidates for indicators
            candidateOrgIds = user.getAccessibleOrgIds(Constants.VISIBILITY_PUBLIC);
        } else {
            // only the orgs of the SC AND accessible by the user
            List<Integer> scOrgIds = surveyConfigSrvc.getOrgIdsBySurveyConfigId(sconfId);
            List<Integer> ownedOrgIds = user.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE);
            candidateOrgIds = new ArrayList<Integer>();

            for (Integer orgId : scOrgIds) {
                if (ownedOrgIds.contains(orgId)) {
                    candidateOrgIds.add(orgId);
                }
            }
        }

        try {
            Pagination<SurveyIndicatorView> pagination = surveyConfigSrvc.findIndicators(sconfId, sc.getVisibility(), candidateOrgIds, name, question, sortName, sortOrder, page, pageSize);
            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String addIndicators() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        String[] indicatorIdArr = request.getParameterValues(PARAM_INDICATOR_IDS);
        List<Integer> indicatorIds = null;
        if (indicatorIdArr != null) {
            indicatorIds = ListUtils.strArrToList(indicatorIdArr);
        }
        logger.debug("Request Params: "
                + "\n\tsconfId=" + sconfId
                + "\n\tindicatorIds[]=" + indicatorIds);

        try {
            surveyConfigSrvc.addSurveyConfigIndicators(sconfId, indicatorIds);
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String deleteIndicators() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        String[] indicatorIdArr = request.getParameterValues(PARAM_INDICATOR_IDS);
        List<Integer> indicatorIds = null;
        if (indicatorIdArr != null) {
            indicatorIds = ListUtils.strArrToList(indicatorIdArr);
        }
        logger.debug("Request Params: "
                + "\n\tsconfId=" + sconfId
                + "\n\tindicatorIds[]=" + indicatorIds);

        try {
            surveyConfigSrvc.deleteScIndicatorsByConfigIdAndIndicatorIds(sconfId, indicatorIds);
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String getSurveyCategory() {
        int catId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CATEGORY_ID));
        logger.debug("Request Params: \n\tcatId=" + catId);
        SurveyCategory cat = surveyConfigSrvc.getSurveyCateogry(catId);
        if (cat == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_SURVEY_CATEGORY));
            return RESULT_EMPTY;
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, JSONUtils.toJson(cat), "OK");
        return RESULT_EMPTY;
    }

    public String getAllAvailableScIndicators() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        logger.debug("Request Params: \n\tsconfId=" + sconfId);
        try {
            List<SurveyIndicator> indicators = surveyConfigSrvc.getAvaialbleScIndicators(sconfId, -1);
            super.sendResponseResult(ControlPanelErrorCode.OK, JSONUtils.listObjToJSONArray(indicators), "OK");
        } catch (Exception ex) {
            logger.error("error", ex);
        }
        return RESULT_EMPTY;
    }

    public String getSurveyQuestion() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        int qstnId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_QUESTION_ID));
        logger.debug("Request Params: \n\tqstnId=" + qstnId + "\n\tsconfId=" + sconfId);
        //LoginUser loginUser = getLoginUser();
        //List<SurveyIndicator> indicators = surveySrvc.getAllAvailableIndicators(loginUser.getLangId(), loginUser.getAccessibleOrgIds(), sconfId, qstnId);

        SurveyQuestion question = surveyConfigSrvc.getSurveyQuestion(qstnId);
        if (question == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_SURVEY_QUESTION));
            return RESULT_EMPTY;
        }

        List<SurveyIndicator> indicators = surveyConfigSrvc.getAvaialbleScIndicators(sconfId, qstnId);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("question", JSONUtils.toJson(question));
        jsonObj.put("indicators", JSONUtils.listObjToJSONArray(indicators));
        super.sendResponseResult(ControlPanelErrorCode.OK, jsonObj, "OK");
        return RESULT_EMPTY;
    }

    public String deleteSurveyCategory() {
        int catId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CATEGORY_ID));
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        logger.debug("Request Params: \n\tcatId=" + catId
                + "\n\tsconfId=" + sconfId);
        if (surveyConfigSrvc.checkSurveyConfigInActiveUse(sconfId)) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_USED, getMessage(ControlPanelMessages.KEY_ERROR_SURVEY_CONFIG_IS_USED));
            return RESULT_EMPTY;
        }
        surveyConfigSrvc.deleteSurveyCategory(sconfId, catId);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String deleteSurveyQuestion() {
        int qstnId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_QUESTION_ID));
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        logger.debug("Request Params: \n\tqstnId=" + qstnId
                + "\n\tsconfId=" + sconfId);
        if (surveyConfigSrvc.checkSurveyConfigInActiveUse(sconfId)) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_USED, getMessage(ControlPanelMessages.KEY_ERROR_SURVEY_CONFIG_IS_USED));
            return RESULT_EMPTY;
        }
        surveyConfigSrvc.deleteSurveyQuestion(sconfId, qstnId);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String saveSurveyCategory() {
        int catId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CATEGORY_ID));
        int pCatId = StringUtils.str2int(request.getParameter(PARAM_PARENT_SURVEY_CATEGORY_ID));
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        int weight = StringUtils.str2int(request.getParameter(PARAM_WEIGHT));
        String label = request.getParameter(PARAM_LABEL);
        String title = request.getParameter(PARAM_TITLE);
        String description = request.getParameter(PARAM_DESCRIPTION);
        logger.debug("Request Params: \n\tcatId=" + catId
                + "\n\tpCatId=" + pCatId
                + "\n\tsconfId=" + sconfId
                + "\n\tweight=" + weight
                + "\n\tlabel=" + label
                + "\n\ttitle=" + title
                + "\n\tdescription=" + description);

        if (surveyConfigSrvc.checkSurveyConfigInActiveUse(sconfId)) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_USED, getMessage(ControlPanelMessages.KEY_ERROR_SURVEY_CONFIG_IS_USED));
            return RESULT_EMPTY;
        }
        SurveyCategory cat = null;
        if (catId > 0) {
            cat = surveyConfigSrvc.getSurveyCateogry(catId);
            cat.setLabel(label);
            cat.setName(label);
            cat.setTitle(title);
            cat.setDescription(description);
            surveyConfigSrvc.updateSurveyCateogry(cat);
        } else {
            cat = new SurveyCategory();
            cat.setLabel(label);
            cat.setName(label);
            cat.setParentCategoryId(pCatId);
            cat.setTitle(title);
            cat.setDescription(description);
            cat.setParentCategoryId(pCatId);
            cat.setSurveyConfigId(sconfId);

            weight = surveyConfigSrvc.getMaxSurveyCategoryWeight(sconfId, pCatId) + 1;
            cat.setWeight(weight);
            surveyConfigSrvc.addSurveyCateogry(cat);
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, JSONUtils.toJson(cat), "OK");
        return RESULT_EMPTY;
    }

    public String saveSurveyQuestion() {
        int qstnId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_QUESTION_ID));
        int pCatId = StringUtils.str2int(request.getParameter(PARAM_PARENT_SURVEY_CATEGORY_ID));
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        int weight = StringUtils.str2int(request.getParameter(PARAM_WEIGHT));
        String label = request.getParameter(PARAM_LABEL);
        int indicatorId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_INDICATOR_ID));
        logger.debug("Request Params: \n\tqstnId=" + qstnId
                + "\n\tpCatId=" + pCatId
                + "\n\tsconfId=" + sconfId
                + "\n\tweight=" + weight
                + "\n\tlabel=" + label
                + "\n\tindicatorId=" + indicatorId);
        if (surveyConfigSrvc.checkSurveyConfigInActiveUse(sconfId)) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_USED, getMessage(ControlPanelMessages.KEY_ERROR_SURVEY_CONFIG_IS_USED));
            return RESULT_EMPTY;
        }
        SurveyQuestion question = null;
        if (qstnId > 0) {
            question = surveyConfigSrvc.getSurveyQuestionExcept(qstnId, sconfId, indicatorId);
            if (question == null) {
                question = surveyConfigSrvc.getSurveyQuestion(qstnId);
            } else {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_SURVEY_QUESTION));
                return RESULT_EMPTY;
            }
            question.setName(label);
            question.setPublicName(label);
            question.setSurveyIndicatorId(indicatorId);
            surveyConfigSrvc.updateSurveyQuestion(question);
        } else {
            if (surveyConfigSrvc.getSurveyQuestionExcept(qstnId, sconfId, indicatorId) != null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_SURVEY_QUESTION));
                return RESULT_EMPTY;
            }
            question = new SurveyQuestion();
            question.setName(label);
            question.setPublicName(label);
            question.setSurveyIndicatorId(indicatorId);
            question.setSurveyCategoryId(pCatId);
            question.setSurveyConfigId(sconfId);

            weight = surveyConfigSrvc.getMaxSurveyQuestionWeight(sconfId, pCatId) + 1;
            question.setWeight(weight);
            surveyConfigSrvc.addSurveyQuestion(question);
        }

        SurveyQuestionTreeView qst = surveyConfigSrvc.getSurveyQuestionTreeView(question.getId());

        super.sendResponseResult(ControlPanelErrorCode.OK, JSONUtils.toJson(qst), "OK");
        return RESULT_EMPTY;
    }

    public String move() {
        int sconfId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_CONFIG_ID));
        int origNodeId = StringUtils.str2int(request.getParameter(PARAM_ORIG_NODE_ID));
        boolean origNodeIsCat = StringUtils.str2boolean(request.getParameter(PARAM_ORIG_NODE_IS_CATEGORY));
        int targetNodeId = StringUtils.str2int(request.getParameter(PARAM_TARGET_NODE_ID));
        int targetParentId = StringUtils.str2int(request.getParameter(PARAM_TARGET_PARENT_ID));
        boolean targetNodeIsCat = StringUtils.str2boolean(request.getParameter(PARAM_TARGET_NODE_IS_CATEGORY));
        String moveType = request.getParameter(PARAM_MOVE_TYPE);
        logger.debug("Request Params: \n\tsconfId=" + sconfId
                + "\n\torigNodeId=" + origNodeId
                + "\n\torigNodeIsCat=" + origNodeIsCat
                + "\n\ttargetNodeId=" + targetNodeId
                + "\n\ttargetParentId=" + targetParentId
                + "\n\ttargetNodeIsCat=" + targetNodeIsCat
                + "\n\tmoveType=" + moveType);

        if (surveyConfigSrvc.checkSurveyConfigInActiveUse(sconfId)) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_USED, getMessage(ControlPanelMessages.KEY_ERROR_SURVEY_CONFIG_IS_USED));
            return RESULT_EMPTY;
        }
        int rc = 0;

        try {
            if (MOVE_TYPE_INNER.equals(moveType)) {
                rc = surveyConfigSrvc.handleMove(sconfId, origNodeId, origNodeIsCat, targetNodeId, targetParentId, targetNodeIsCat, SurveyConfigService.MOVE_INNER);
            } else if (MOVE_TYPE_NEXT.equals(moveType)) {
                rc = surveyConfigSrvc.handleMove(sconfId, origNodeId, origNodeIsCat, targetNodeId, targetParentId, targetNodeIsCat, SurveyConfigService.MOVE_AFTER);
            } else if (MOVE_TYPE_PRE.equals(moveType)) {
                rc = surveyConfigSrvc.handleMove(sconfId, origNodeId, origNodeIsCat, targetNodeId, targetParentId, targetNodeIsCat, SurveyConfigService.MOVE_BEFORE);
            } else {
                logger.debug("Unknow move type: " + moveType);
            }
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
            rc = -1;
        }

        switch (rc) {
            case -1:
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, getMessage(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL));
                break;

            case SurveyConfigService.MOVE_RC_OK:
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
                break;

            case SurveyConfigService.MOVE_RC_NO_SURVEY_CONFIG:
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_SURVEY_CONFIG));
                break;

            default:
                super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, getMessage(ControlPanelMessages.KEY_ERROR_OBSOLETE_SURVEY_TREE));
        }

        return RESULT_EMPTY;
    }

    private JSONArray nodeList2ZTreeJSON(List<Node> list, int scId) {
        JSONArray jsonArr = new JSONArray();
        JSONObject root = new JSONObject();
        root.put("id", "c0");
        root.put("name", "ROOT");
        root.put("title", "[SCID: " + scId + "] ROOT");
        root.put("pId", "0");
        root.put("iconSkin", "rootIcon");
        root.put("open", true);
        root.put("isParent", true);
        jsonArr.add(root);

        if (!ListUtils.isEmptyList(list)) {
            for (Node node : list) {
                jsonArr.add(node2ZTreeJSON(node));

            }
        }
        return jsonArr;
    }

    private JSONObject node2ZTreeJSON(Node node) {

        JSONObject jsoObj = new JSONObject();

        if (node.getType() == Node.NODE_TYPE_CATEGORY) {
            jsoObj.put("id", "c" + node.getId());
            jsoObj.put("pId", "c" + node.getParentId());
            jsoObj.put("isParent", true);
            jsoObj.put("open", true);
            jsoObj.put("name", node.getName());
            jsoObj.put("title", "[CID: " + node.getId() + "] " + node.getName());
        } else {
            jsoObj.put("id", "q" + node.getId());
            jsoObj.put("pId", "c" + node.getParentId());
            jsoObj.put("dropInner", false);
            jsoObj.put("name", node.getName());
            jsoObj.put("title", "[QID: " + node.getId() + "] " + node.getName());
        }

        return jsoObj;
    }

    private boolean userOwnsThisSC(LoginUser user, int scId) {
        if (user.isSiteAdmin()) {
            return true;
        }

        List<Integer> scOrgIds = surveyConfigSrvc.getOrgIdsBySurveyConfigId(scId);

        if (scOrgIds == null || scOrgIds.isEmpty()) {
            return false;
        }

        List<Integer> myOrgIds = user.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE);

        if (myOrgIds == null || myOrgIds.isEmpty()) {
            return false;
        }

        for (int scOrgId : scOrgIds) {
            for (int myOrgId : myOrgIds) {
                if (scOrgId == myOrgId) {
                    return true;
                }
            }
        }

        return false;
    }
}
