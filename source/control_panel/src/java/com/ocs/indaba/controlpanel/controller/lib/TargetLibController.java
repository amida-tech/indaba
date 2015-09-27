/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.lib;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.TargetVO;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.TargetTag;
import com.ocs.indaba.service.LanguageService;
import com.ocs.indaba.service.OrganizationService;
import com.ocs.indaba.service.TargetService;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrator
 */
@Results({
    @Result(name = "success", type = "redirectAction", params = {"actionName", "libraries"})
})
public class TargetLibController extends BaseController {

    private static final long serialVersionUID = 7550588769978806967L;
    private static final Logger logger = Logger.getLogger(TargetLibController.class);
    private static final String PARAM_ORGANIZATION = "organization";
    private static final String PARAM_VISIBILITY = "visibility";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_USER_TAG = "userTag";
    private static final String PARAM_SHORTNAME = "shortName";
    private static final String PARAM_TARGET_TYPE = "targetType";
    private static final String PARAM_DESCRIPTON = "description";
    private static final String PARAM_GUID = "guid";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_TAGS = "tags[]";
    private static final String PARAM_TARGET_ID = "targetId";
    private static final String ATTR_ORGANIZATIONS = "orgs";
    private static final String ATTR_OWN_ORGS = "ownOrgs";
    private static final String ATTR_TAGS = "tags";
    private static final String TARGET_NAME = "tname";
    private static final String TARGET_SHORT_NAME = "tsname";
    @Autowired
    private TargetService targetSrvc;
    @Autowired
    private OrganizationService orgSrvc;
    @Autowired
    private LanguageService langSrvc;

    public String index() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        logger.debug("Request Params: \n\tvisibility=" + visibility);
        //Pagination<IndicatorVO> indicatorPage = libSrvc.findIndicators(type, userTag, indicatorTag, organization);
        //request.setAttribute("totalCount", indicatorPage.getCurPageList().size());
        request.setAttribute(PARAM_VISIBILITY, (visibility > 0) ? visibility : ControlPanelConstants.TARGET_LIB_VISIBILITY_PUBLIC);
        request.setAttribute(ATTR_TAGS, targetSrvc.getAllTtags());
        request.setAttribute(ATTR_LANGUAGES, langSrvc.getAllLanguages());
        LoginUser loginUser = getLoginUser();

        int orgVisibility = (visibility > 0) ? visibility : Constants.VISIBILITY_PUBLIC;
        request.setAttribute(ATTR_ORGANIZATIONS, loginUser.getAccessibleOrgs(orgVisibility));
        request.setAttribute(ATTR_OWN_ORGS, loginUser.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE));
        return RESULT_INDEX;
    }

    public String find() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int orgId = StringUtils.str2int(request.getParameter(PARAM_ORGANIZATION));
        String filterUserTag = request.getParameter(PARAM_USER_TAG);
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        String queryType = request.getParameter(PARAM_QUERY_TYPE);
        String query = request.getParameter(PARAM_QUERY);

        logger.debug("Request Params: \n\tvisibility=" + visibility
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\torgId=" + orgId
                + "\n\tfilterUserTag=" + filterUserTag
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder
                + "\n\tqueryType=" + queryType
                + "\n\tquery=" + query);

        List<TargetVO> targetVoList = null;
        int totalCount = 0;
        LoginUser loginUser = super.getLoginUser();

        List<Integer> oaOfOrgIds = loginUser.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE);

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }

        try {
            totalCount = targetSrvc.getAllTargetCount(oaOfOrgIds, filterUserTag, orgId, visibility, queryType, query);
            List<Target> targets = targetSrvc.getAllTargets(oaOfOrgIds, filterUserTag, orgId, visibility, sortName, sortOrder, (page - 1) * pageSize, pageSize, queryType, query);
            if (targets != null && !targets.isEmpty()) {
                //totalCount = targets.size();
                targetVoList = new ArrayList<TargetVO>(targets.size());
                for (Target t : targets) {
                    TargetVO vo = TargetVO.initWithTarget(t);
                    vo.setOrgname(loginUser.getOrg(vo.getOwnerOrgId()).getName());
                    vo.setEditable(loginUser.getChecker().hasOrgAuthority(vo.getOwnerOrgId()));
                    targetVoList.add(vo);
                }
            }
        } catch (Exception ex) {
            logger.error("Fail to get targets.", ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.PROGRAM_ERROR));
            return RESULT_EMPTY;
        }

        Pagination<TargetVO> pagination = new Pagination<TargetVO>(totalCount, page, pageSize);

        /** who can add targets? - TBD
        pagination.addProperty("canAddTargets", 
        (visibility == ControlPanelConstants.TARGET_LIB_VISIBILITY_PRIVATE || loginUser.isSiteAdmin()) ? "yes" : "no");
         * **/
        pagination.addProperty("canAddTargets", "yes");
        pagination.setRows(targetVoList);
        String json = pagination.toJsonString();

        logger.debug("JSON: " + json);

        sendResponseMessage(json);

        return RESULT_EMPTY;
    }

    public String create() {
        logger.debug("Received Create request");

        logger.debug("PARAM ID: " + request.getParameter(PARAM_ID));
        int targetId = StringUtils.str2int(request.getParameter(PARAM_ID));

        logger.debug("PARAM VIS: " + request.getParameter(PARAM_VISIBILITY));
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));

        String name = request.getParameter(PARAM_NAME);
        String shortName = request.getParameter(PARAM_SHORTNAME);

        logger.debug("PARAM TARGET_TYPE: " + request.getParameter(PARAM_TARGET_TYPE));
        short targetType = StringUtils.str2short(request.getParameter(PARAM_TARGET_TYPE));

        logger.debug("PARAM LANGUAGE: " + request.getParameter(PARAM_LANGUAGE));
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANGUAGE));

        logger.debug("PARAM ORGANIZATION: " + request.getParameter(PARAM_ORGANIZATION));
        int orgId = StringUtils.str2int(request.getParameter(PARAM_ORGANIZATION));

        logger.debug("PARAM_GUID: " + request.getParameter(PARAM_GUID));
        String guid = request.getParameter(PARAM_GUID);

        logger.debug("PARAM_DESCRIPTON: " + request.getParameter(PARAM_DESCRIPTON));
        String description = request.getParameter(PARAM_DESCRIPTON);

        logger.debug("PARAM_TAGS: " + request.getParameter(PARAM_TAGS));
        String[] tags = request.getParameterValues(PARAM_TAGS);

        logger.debug("Get LoginUser");
        LoginUser loginUser = getLoginUser();

        logger.debug("Request Params: "
                + "\n\ttargetId=" + targetId
                + "\n\tvisibility=" + visibility
                + "\n\tname=" + name
                + "\n\tshortName=" + shortName
                + "\n\tlanguage=" + langId
                + "\n\torganization=" + orgId
                + "\n\ttargetType=" + targetType
                + "\n\tguid=" + guid
                + "\n\tdescription=" + description);

        try {
            if (!loginUser.getChecker().hasOrgAuthority(orgId)) {
                logger.error("The user is not OA of organization: " + orgId);
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                        loginUser.message(ControlPanelMessages.TARGET_EDIT__NOT_AUTHORIZED));
                return RESULT_EMPTY;
            }

            // Check for name conflict
            if (targetSrvc.existsByName(targetId, name)) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                        loginUser.message(ControlPanelMessages.KEY_DUPLICATED_TARGET_NAME, name));
                return RESULT_EMPTY;
            }

            if (targetSrvc.existsByShortName(targetId, shortName)) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                        loginUser.message(ControlPanelMessages.KEY_DUPLICATED_TARGET_SHORT_NAME, shortName));
                return RESULT_EMPTY;
            }

            Target target = null;
            if (targetId <= 0) { // create a new target
                target = new Target();
                target.setName(name);
                target.setShortName(shortName);
                target.setTargetType(targetType);
                target.setGuid(guid);
                target.setLanguageId(langId);
                target.setOwnerOrgId(orgId);
                target.setCreatorOrgId(orgId);
                target.setStatus(ControlPanelConstants.TARGET_STATUS_ACTIVE);
                target.setVisibility((short) visibility);
                target.setDescription(description);
                Date now = new Date();
                target.setCreateTime(now);
                target = targetSrvc.addTarget(target);
            } else { // update a existed target
                target = targetSrvc.getTargetById(targetId);
                if (target != null) {
                    target.setName(name);
                    target.setShortName(shortName);
                    target.setShortName(shortName);
                    target.setTargetType(targetType);
                    target.setGuid(guid);
                    target.setLanguageId(langId);
                    target.setOwnerOrgId(orgId);
                    target.setCreatorOrgId(orgId);
                    target.setStatus(ControlPanelConstants.TARGET_STATUS_ACTIVE);
                    target.setVisibility((short) visibility);
                    target.setDescription(description);
                    targetSrvc.updateTarget(target);
                    targetSrvc.deleteTagsByTargetId(targetId);
                }
            }
            if (tags != null && target != null) {
                for (String tag : tags) {
                    int tagId = StringUtils.str2int(tag);
                    if (tagId < 0) {
                        continue;
                    }
                    TargetTag targetTag = new TargetTag();
                    targetTag.setTargetId(target.getId());
                    targetTag.setTtagsId(tagId);
                    targetSrvc.addTargetTag(targetTag);
                }
            }
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        } catch (Exception ex) {
            logger.error("Fail to initialize value object.", ex);

            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.PROGRAM_ERROR));

            return RESULT_EMPTY;
        }
        return RESULT_EMPTY;
    }

    // GET a specified indicator
    public String get() {
        int targetId = StringUtils.str2int(request.getParameter(PARAM_ID));
        LoginUser loginUser = super.getLoginUser();

        logger.debug("Request Params: \n\ttargetId=" + targetId);
        Target target = targetSrvc.getTargetById(targetId);
        List<Integer> ttagsIds = targetSrvc.getTtagsIdsByTargetId(targetId);
        TargetVO targetVo = TargetVO.initWithTarget(target);
        targetVo.addTagIds(ttagsIds);
        targetVo.setOrgname(loginUser.getOrg(targetVo.getOwnerOrgId()).getName());
        targetVo.setEditable(loginUser.getChecker().hasOrgAuthority(targetVo.getOwnerOrgId()));

        super.sendResponseResult(ControlPanelErrorCode.OK, targetVo.toJson(), "OK");
        return RESULT_EMPTY;
    }

    public String exists() {
        LoginUser user = getLoginUser();
        int targetId = StringUtils.str2int(request.getParameter(PARAM_TARGET_ID));
        String fieldId = request.getParameter(PARAM_FIELD_ID);
        String fieldName = request.getParameter(PARAM_FIELD_NAME);
        String fieldValue = request.getParameter(PARAM_FIELD_VALUE);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(fieldId);

        if (TARGET_SHORT_NAME.equals(fieldName)) {
            if (targetSrvc.existsByShortName(targetId, fieldValue)) {
                jsonArr.add(false);
                jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_TARGET_SHORT_NAME, fieldValue));
            } else {
                jsonArr.add(true);
                //jsonArr.add("OK");
            }
        } else {
            if (targetSrvc.existsByName(targetId, fieldValue)) {
                jsonArr.add(false);
                jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_TARGET_NAME, fieldValue));
            } else {
                jsonArr.add(true);
                //jsonArr.add("OK");
            }
        }
        super.sendResponseJson(jsonArr);
        return RESULT_EMPTY;
    }

    public static void main(String args[]) {
        for (int i = 0; i < 33; ++i) {
            System.out.println("(" + (i + 1) + "," + 1 + "," + i + ")");
        }
    }
}
