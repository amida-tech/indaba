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
import com.ocs.indaba.controlpanel.model.*;
import com.ocs.indaba.controlpanel.model.IndicatorImportValidation;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.service.IndicatorTagService;
import com.ocs.indaba.service.LanguageService;
import com.ocs.indaba.service.ReferenceService;
import com.ocs.indaba.controlpanel.service.impl.IndicatorLibraryServiceImpl;
import com.ocs.indaba.controlpanel.service.impl.TableValidationResult;
import com.ocs.indaba.controlpanel.tableprocessor.TableLoader;
import com.ocs.indaba.po.AnswerTypeTable;
import com.ocs.indaba.survey.table.Block;
import com.ocs.indaba.util.Arrays;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import com.opensymphony.xwork2.ModelDriven;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
@Results({
    @Result(name = "success", type = "redirectAction", params = {"actionName", "libraries"}),
    @Result(name = "indicatorlist", location = "indicator-list.jsp"),
    @Result(name = "indicator-add", location = "indicator-add.jsp")
})
public class IndicatorLibController extends BaseController implements ModelDriven<Object> {

    private static final Logger logger = Logger.getLogger(IndicatorLibController.class);
    private static final String PARAM_ORGANIZATION = "organization";
    private static final String PARAM_VISIBILITY = "visibility";
    private static final String PARAM_STATE = "state";
    private static final String PARAM_USER_TAG = "userTag";
    private static final String PARAM_INDICATOR_TAG = "indicatorTag";
    private static final String PARAM_SURVEY_INDICATOR_ID = "indicatorId";
    private static final String PARAM_SURVEY_INDICATOR_INTL_ID = "indicatorIntlId";
    private static final String PARAM_SURVEY_ATC_CHOICE_ID = "atcChoiceId";
    private static final String PARAM_SURVEY_ATC_CHOICE_INTL_ID = "atcChoiceIntlId";
    private static final String PARAM_QUESTION = "question";
    private static final String PARAM_TIP = "tip";
    private static final String PARAM_ANSWER_TYPE = "answerType";
    private static final String PARAM_ANSWER_TYPE_ID = "answerTypeId";
    private static final String PARAM_CHOICES = "choices";
    private static final String PARAM_LABEL = "label";
    private static final String PARAM_CRITERIA = "criteria";
    private static final String PARAM_USE_SCORE = "useScore";
    private static final String PARAM_FILENAME = "filename";
    private static final String PARAM_UPLOADED_FILENAME = "uploadedFilename";
    // Attribute Keys
    private static final String ATTR_ORGANIZATIONS = "orgs";
    private static final String ATTR_OWN_ORGS = "ownOrgs";

    private static final String ATTR_REFERENCES = "references";
    private static final String ATTR_TAGS = "itags";
    private static final String MUST_LOGIN = ControlPanelMessages.MUST_LOGIN;
    //
    @Autowired
    private IndicatorLibraryServiceImpl indicatorSrvc;
    @Autowired
    private LanguageService langSrvc;
    @Autowired
    private ReferenceService refSrvc;
    @Autowired
    private IndicatorTagService itagSrvc;
    @Autowired
    private SurveyIndicatorDAO surveyIndicatorDao = null;
    private File uploadFile;

    public String index() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        int orgVisibility;

        switch (visibility) {
            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PRIVATE:
                orgVisibility = Constants.VISIBILITY_PRIVATE;
                break;

            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED:
            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED:
            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PUBLIC_TEST:
                orgVisibility = Constants.VISIBILITY_PUBLIC;
                break;

            default:
                orgVisibility = Constants.VISIBILITY_PUBLIC;
                visibility = 0;   // for all
        }

        logger.debug("Request Params: \n\tvisibility=" + visibility);
        request.setAttribute(ATTR_LANGUAGES, langSrvc.getAllLanguages());
        request.setAttribute(ATTR_ORGANIZATIONS, loginUser.getAccessibleOrgs(orgVisibility));
        request.setAttribute(ATTR_OWN_ORGS, loginUser.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE));
        request.setAttribute(ATTR_REFERENCES, refSrvc.getAllReferences());
        request.setAttribute(ATTR_TAGS, itagSrvc.getAllITags());       
        request.setAttribute(PARAM_VISIBILITY, visibility);

        return RESULT_INDEX;
    }
    // FIND - indicator filter

    public String find() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        int lib = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY)); // what we get is really lib info
        int state = StringUtils.str2int(request.getParameter(PARAM_STATE));
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int orgId = StringUtils.str2int(request.getParameter(PARAM_ORGANIZATION));
        String userTag = request.getParameter(PARAM_USER_TAG);
        int indicatorTag = StringUtils.str2int(request.getParameter(PARAM_INDICATOR_TAG));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        String queryType = request.getParameter(PARAM_QUERY_TYPE);
        String query = request.getParameter(PARAM_QUERY);
        int visibility;

        logger.debug("Request Params: \n\tlib=" + lib
                + "\n\tstate=" + state + "\n\tuserTag=" + userTag + "\n\tindicatorTag=" + indicatorTag
                + "\n\torganization=" + orgId + "\n\tpageSize=" + pageSize + "\n\tpage=" + page
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder
                + "\n\tqueryType=" + queryType
                + "\n\tquery=" + query);

        switch (lib) {
            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PRIVATE:
                visibility = Constants.VISIBILITY_PRIVATE;
                state = 0;
                break;

            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED:
                visibility = Constants.VISIBILITY_PUBLIC;
                state = Constants.RESOURCE_STATE_ENDORSED;
                break;

            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED:
                visibility = Constants.VISIBILITY_PUBLIC;
                state = Constants.RESOURCE_STATE_EXTENDED;
                break;

            case ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PUBLIC_TEST:
                visibility = Constants.VISIBILITY_PUBLIC;
                state = Constants.RESOURCE_STATE_TEST;
                break;

            default:
                state = 0;
                visibility = 0;   // for all
        }

        try {
            List<Integer> oaOfOrgIds;
            if (loginUser.isSiteAdmin()) {
                // super user is treated as OA of all orgs
                oaOfOrgIds = null;
            } else {
                oaOfOrgIds = loginUser.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE);
            }

            if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
                sortOrder = "asc";
            }

            Pagination<IndicatorVO> indicatorPage = indicatorSrvc.findAllIndicators(oaOfOrgIds, orgId, visibility, state, indicatorTag, userTag, sortName, sortOrder, page, pageSize, queryType, query);

            List<IndicatorVO> indicatorList = indicatorPage.getRows();
            int moveable = 0;
            int deleteable = 0;

            for (IndicatorVO vo : indicatorList) {
                vo.setOrgName(loginUser.getOrg(vo.getOrgId()).getName());
                vo.setTypeName(vo.getTypeId() + " - " + loginUser.message(Constants.SURVEY_ANSWER_TYPE_TEXT_KEYS[vo.getTypeId()]));
                vo.setLibName(loginUser.message(Constants.INDICATOR_LIB_TEXT_KEYS[vo.getLibId()]));
                vo.setDeleteable(indicatorSrvc.hasDeleteAuthority(loginUser, vo));
                vo.setEditable(indicatorSrvc.hasEditAuthority(loginUser, vo));
                vo.setMoveable(vo.getLibId() != Constants.INDICATOR_LIB_VISIBILITY_PRIVATE && indicatorSrvc.hasMoveAuthority(loginUser, vo));
                vo.setTranslateable(indicatorSrvc.hasTranslateAuthority(loginUser, vo));

                if (vo.isDeleteable()) {
                    deleteable++;
                }
                if (vo.isMoveable()) {
                    moveable++;
                }
            }

            if (deleteable > 0) {
                indicatorPage.addProperty("massdelete", "yes");
            } else {
                indicatorPage.addProperty("massdelete", "no");
            }
            if (moveable > 0) {
                indicatorPage.addProperty("massmove", "yes");
            } else {
                indicatorPage.addProperty("massmove", "no");
            }

            // only private and public test libraries allow adding and importing indicators
            switch (lib) {
                case Constants.INDICATOR_LIB_VISIBILITY_PRIVATE:
                case Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_TEST:
                    indicatorPage.addProperty("addindicator", "yes");
                    indicatorPage.addProperty("importindicators", "yes");
                    break;

                default:
                    indicatorPage.addProperty("addindicator", "no");
                    indicatorPage.addProperty("importindicators", "no");
            }

            String json = indicatorPage.toJsonString();
            logger.debug("JSON: " + json);

            sendResponseMessage(json);
        } catch (Exception ex) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.PROGRAM_ERROR));

            logger.error("Error: ", ex);
        }
        return RESULT_EMPTY;
    }

    // CREATE a new indicator
    public String create() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }
        printURLRequestQuery();
        IndicatorDetailVO indicator = new IndicatorDetailVO();
        try {
            int errCode = ControlPanelErrorCode.OK;
            String errMsg = null;
            indicator.initializeObject(super.getParameters());
            String[] indicatorTags = request.getParameterValues("itags[]");
            if (indicatorTags != null) {
                for (String tag : indicatorTags) {
                    int tagId = StringUtils.str2int(tag);
                    if (tagId > 0) {
                        indicator.addItag(tagId);
                    }
                }
            }
            indicator.setUserId(loginUser.getUserId());
            logger.debug(indicator.toString());
            if (indicator.getId() > 0) {
                // edit the indicator
                SurveyIndicator si = surveyIndicatorDao.get(indicator.getId());

                if (si == null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                            getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_INDICATOR));
                    return RESULT_EMPTY;
                }

                if (!indicatorSrvc.hasEditAuthority(loginUser, si)) {
                    errCode = ControlPanelErrorCode.ERR_NO_PERMISSION;
                    errMsg = loginUser.message(ControlPanelMessages.INDICATOR_EDIT__NOT_AUTHORIZED);
                } else {
                    String bans = indicatorSrvc.checkEditBans(loginUser, si);
                    if (bans != null) {
                        errCode = ControlPanelErrorCode.ERR_NO_PERMISSION_BANS;
                        errMsg = bans;
                    } else {
                        errMsg = indicatorSrvc.editIndicator(loginUser, indicator);
                        if (errMsg == null) {
                            errMsg = loginUser.message(ControlPanelMessages.INDICATOR__SAVED);
                        } else {
                            errCode = ControlPanelErrorCode.ERR_UNKNOWN;
                        }
                    }
                }
            } else {
                errMsg = indicatorSrvc.createIndicator(loginUser, indicator);
                if (errMsg == null) {
                    errMsg = loginUser.message(ControlPanelMessages.INDICATOR__CREATED);
                } else {
                    errCode = ControlPanelErrorCode.ERR_UNKNOWN;
                }
            }

            super.sendResponseResult(errCode, indicator.toJson(), errMsg);
        } catch (Exception ex) {
            logger.error("Fail to initialize value object.", ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.PROGRAM_ERROR));
        }
        return RESULT_EMPTY;
    }

    // GET a specified indicator
    public String get() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        int indicatorId = StringUtils.str2int(request.getParameter(PARAM_ID));
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANG_ID));

        logger.debug("Request Params: \n\tindicatorId=" + indicatorId + "\n\tlangId=" + langId);
        try {
            SurveyIndicator si = surveyIndicatorDao.get(indicatorId);

            if (si == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_INDICATOR));
                return RESULT_EMPTY;
            }

            IndicatorDetailVO indicator = indicatorSrvc.getIndicator(loginUser, indicatorId, langId);

            if (indicator == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_INDICATOR));
                return RESULT_EMPTY;
            }

            // determine whether the indicator is editable
            String notEditableReason = null;
            if (!indicatorSrvc.hasEditAuthority(loginUser, si)) {
                notEditableReason = loginUser.message(ControlPanelMessages.INDICATOR_EDIT__NOT_AUTHORIZED);
            } else {
                notEditableReason = indicatorSrvc.checkEditBans(loginUser, si);
            }

            if (notEditableReason == null) {
                // the indicator is editable
                // TBD -- tell the client to set the indicator form in EDIT mode
                indicator.setWritable(true);
                notEditableReason = "OK";
            } else {
                // the indicator is not editable
                // TBD -- tell the client to set the indicator form in VIEW mode with the reason
                indicator.setWritable(false);
            }
            String json = indicator.toJsonString();
            logger.debug("INDICATOR JSON: " + json);

            super.sendResponseResult(ControlPanelErrorCode.OK, indicator.toJson(), notEditableReason);
        } catch (Exception ex) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.PROGRAM_ERROR));

            logger.error("Fail to convert indicator to json object.", ex);
        }
        return RESULT_EMPTY;
    }

    // GET a specified indicator
    public String translate() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        int indicatorId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_INDICATOR_ID));
        int indicatorIntlId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_INDICATOR_INTL_ID));
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANG_ID));
        String question = request.getParameter(PARAM_QUESTION);
        String tip = request.getParameter(PARAM_TIP);
        short answerType = StringUtils.str2short(request.getParameter(PARAM_ANSWER_TYPE));
        int answerTypeId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_TYPE_ID));
        String choicesJson = request.getParameter(PARAM_CHOICES);

        logger.debug("Request Params: "
                + "\n\tindicatorId=" + indicatorId
                + "\n\tindicatorIntlId=" + indicatorIntlId
                + "\n\tlangId=" + langId
                + "\n\tquestion=" + question
                + "\n\tanswerType=" + answerType
                + "\n\tanswerTypeId=" + answerTypeId
                + "\n\ttip=" + tip
                + "\n\tchoices=" + choicesJson);
        
        SurveyIndicator si = surveyIndicatorDao.get(indicatorId);

        if (si == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_INDICATOR));
            return RESULT_EMPTY;
        }

        List<AnswerChoiceSettingVO> choices = null;
        if (!StringUtils.isEmpty(choicesJson)) {
            JSONParser jsonParser = new JSONParser();
            try {
                JSONArray arr = (JSONArray) jsonParser.parse(choicesJson);
                choices = new ArrayList<AnswerChoiceSettingVO>(arr.size());
                for (int i = 0, size = arr.size(); i < size; ++i) {
                    JSONObject jsonObj = (JSONObject) arr.get(i);
                    AnswerChoiceSettingVO choice = new AnswerChoiceSettingVO();
                    choice.setId(StringUtils.str2int((String) jsonObj.get(PARAM_SURVEY_ATC_CHOICE_INTL_ID)));
                    choice.setAtcChoiceId(StringUtils.str2int((String) jsonObj.get(PARAM_SURVEY_ATC_CHOICE_ID)));
                    choice.setUseScore((Boolean) jsonObj.get(PARAM_USE_SCORE));
                    choice.setLabel((String) jsonObj.get(PARAM_LABEL));
                    choice.setCriteria((String) jsonObj.get(PARAM_CRITERIA));
                    choices.add(choice);
                }
            } catch (ParseException ex) {
                logger.error("Fail to parse JSON string: " + choicesJson, ex);
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                        loginUser.message(ControlPanelMessages.PROGRAM_ERROR));
                return RESULT_EMPTY;
            }

        }

        //si.setAnswerTypeId(enSi.getAnswerType());
        IndicatorDetailVO indicator = new IndicatorDetailVO();
        indicator.setId(indicatorIntlId);
        indicator.setSurveyIndicatorId(indicatorId);
        indicator.setLanguage(langId);
        indicator.setQuestion(question);
        indicator.setTip(tip);
        indicator.setAnswerType(answerType);
        indicator.setAnswerTypeId(answerTypeId);
        indicator.setAnswerChoices(choices);

        indicatorSrvc.translate(loginUser, indicatorId, langId, indicator);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");

        return RESULT_EMPTY;
    }

    public String addpage() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        request.setAttribute("type", visibility);
        return "indicator-add";
    }

    public String editpage() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        request.setAttribute("type", visibility);
        return "indicator-add";
    }

    // DELETE
    public String delete() {
        //int indicatorId = StringUtils.str2int(request.getParameter(PARAM_ID));
        //logger.debug("Request Params: \n\tindicatorId=" + indicatorId);
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        String[] idList = request.getParameterValues("idList[]");
        logger.debug("Remove indicators: idList=" + Arrays.asList(idList));
        ArrayList<ReportVO> reports = new ArrayList<ReportVO>();

        if (idList != null) {
            for (String s : idList) {
                int id = NumberUtils.toInt(s, 0);
                if (id > 0) {
                    SurveyIndicator si = surveyIndicatorDao.get(id);
                    if (si == null) continue;

                    ReportVO report = new ReportVO();

                    if (!indicatorSrvc.hasDeleteAuthority(loginUser, si)) {
                        report.setErrCode(ControlPanelErrorCode.ERR_NO_PERMISSION);
                        report.setErrMsg(si.getName() + ": "
                                + loginUser.message(ControlPanelMessages.INDICATOR_EDIT__NOT_AUTHORIZED));
                    } else {
                        String bans = indicatorSrvc.checkDeleteBans(loginUser, si);
                        if (bans != null) {
                            report.setErrCode(ControlPanelErrorCode.ERR_NO_PERMISSION_BANS);
                            report.setErrMsg(si.getName() + ": " + bans);
                        } else {
                            String errMsg = indicatorSrvc.deleteIndicator(loginUser, id);
                            if (errMsg == null) {
                                report.setErrCode(ControlPanelErrorCode.OK);
                                report.setErrMsg(si.getName() + ": "
                                        + loginUser.message(ControlPanelMessages.KEY_OK));
                            } else {
                                report.setErrCode(ControlPanelErrorCode.ERR_UNKNOWN);
                                report.setErrMsg(si.getName() + ": " + errMsg);
                            }
                        }
                    }
                    reports.add(report);
                }
            }
        } else {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.KEY_ERR_INDICATORS_NOT_SPECIFIED));
            return RESULT_EMPTY;
        }

        JSONArray arr = new JSONArray();
        for (ReportVO rpt : reports) {
            arr.add(JSONUtils.toJson(rpt));
        }
        // TBD - send the report to client
        super.sendResponseResult(ControlPanelErrorCode.OK, arr, "OK");
        return RESULT_EMPTY;
    }

    public String importIndicators() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        String visibility = request.getParameter("visibility");

        // uploadedFilename is the base name of the uploaded file in system's storage
        // The file is already stored there!
        String uploadedFilename = request.getParameter(PARAM_UPLOADED_FILENAME);
        uploadFile = super.getUploadFileByFilename(uploadedFilename, ControlPanelConstants.UPLOAD_TYPE_INDICATOR);
        logger.debug("Import indicators: visibility=" + visibility + ", file: " + uploadedFilename);
        int count = indicatorSrvc.importIndicators(loginUser, uploadFile);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("count", count);
        super.sendResponseResult(ControlPanelErrorCode.OK, jsonObj, "OK");
        return RESULT_EMPTY;
    }

    public String validateIndicatorImport() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        boolean valid = false;
        try {
            String filename = request.getParameter(PARAM_FILENAME); // the filename is the name from the user
            if (uploadFile == null) {
                uploadFile = super.getUploadFile(filename, ControlPanelConstants.UPLOAD_TYPE_INDICATOR);

                // now uploadFile is at the system's storage folder!
                FileUtils.copyInputStreamToFile(request.getInputStream(), uploadFile);
            }
            logger.debug("Receive uploaded file: " + filename + "(size=" + uploadFile.length() + ", user=" + loginUser.getUserId() + ").");
            IndicatorImportValidation validationResult = indicatorSrvc.validateIndicatorImport(loginUser, uploadFile);
            if (validationResult != null) {
                JSONObject result = validationResult.toJson();
                if (validationResult.getErrorCount() == 0) {
                    valid = true;

                    // The file name of uploadFile is the base name of the file in the system's storage
                    // We send it to the client to remember it. It will be sent back to us when user clicks import
                    result.put(PARAM_UPLOADED_FILENAME, uploadFile.getName());
                }
                super.sendResponseResult(ControlPanelErrorCode.OK, true, result, "OK");
            } else {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, (uploadFile != null), "Fail to validate indicator import file.");
            }
        } catch (Exception ex) {
            logger.error("Fail to validate indicator import file." + uploadFile, ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, (uploadFile != null), "Error: " + ex);
        } finally {
            if (!valid) { // delete invalid file
                //uploadFile.delete();
            }
        }
        return RESULT_EMPTY;
    }

    public String validateTableImport() {
        
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        boolean valid = false;
        String filename="";
        try {
            filename = request.getParameter(PARAM_FILENAME);
            logger.debug("Receive uploaded ( file: " + filename + ", user=" + loginUser.getUserId() + ").");
            TableValidationResult validationResult = indicatorSrvc.validateTableDefinition(loginUser, request.getInputStream(), filename);
                    
            if (validationResult != null) {
                JSONObject result = JSONUtils.parseJSONStr(JSONUtils.toJsonString(validationResult));
                if (validationResult.getErrCount() == 0) {
                    valid = true;
                    result.put(PARAM_UPLOADED_FILENAME, filename);
                }
                logger.debug(result);
                super.sendResponseResult(ControlPanelErrorCode.OK, true, result, "OK");
            } else {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, true, "Fail to validate table file.");
            }
        } catch (Exception ex) {
            logger.error("Fail to validate indicator table file. " + filename, ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, true, "Error: " + ex);
        } finally {
            if (!valid) { // delete invalid file
                //uploadFile.delete();
            }
        }
        return RESULT_EMPTY;
    }


    public String downloadTableDef() throws IOException {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        String tdfFileName = request.getParameter("tdfFileName");
        String pathName = request.getParameter("pathName");

        if (StringUtils.isEmpty(pathName)) {
            logger.error("Missing TDF path name");
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL);
            return RESULT_EMPTY;
        }

        if (StringUtils.isEmpty(tdfFileName)) {
            tdfFileName = pathName;
        }

        File storedFile = super.getUploadFileByFilename(pathName, ControlPanelConstants.UPLOAD_TYPE_TABLE);
        if (storedFile == null || !storedFile.isFile() || !storedFile.exists()) {
            logger.error("TDF file doesn't exist: " + pathName);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL);
            return RESULT_EMPTY;
        }

        response.setContentType("");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + tdfFileName + "\"");
        response.setCharacterEncoding("utf-8");

        OutputStream output = response.getOutputStream();
        InputStream bufIn = new BufferedInputStream(new FileInputStream(storedFile));
        com.ocs.util.FileUtils.loadStream(output, bufIn);
        return RESULT_EMPTY;
    }
    
    public String importIndicatorTranslations() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        String visibility = request.getParameter("visibility");
        String uploadedFilename = request.getParameter(PARAM_UPLOADED_FILENAME);
        uploadFile = super.getUploadFileByFilename(uploadedFilename, ControlPanelConstants.UPLOAD_TYPE_INDICATOR);
        logger.debug("Import indicators: visibility=" + visibility + ", file: " + uploadedFilename);
        int count = indicatorSrvc.importTranslations(loginUser, uploadFile);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("count", count);
        super.sendResponseResult(ControlPanelErrorCode.OK, jsonObj, "OK");
        return RESULT_EMPTY;
    }

    public String validateIndicatorTranslations() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        boolean valid = false;
        try {
            String filename = request.getParameter(PARAM_FILENAME);
            if (uploadFile == null) {
                uploadFile = super.getUploadFile(filename, ControlPanelConstants.UPLOAD_TYPE_INDICATOR);
                FileUtils.copyInputStreamToFile(request.getInputStream(), uploadFile);
            }
            logger.debug("Receive uploaded file: " + filename + "(size=" + uploadFile.length() + ", user=" + loginUser.getUserId() + ").");
            IndicatorTransValidation validationResult = indicatorSrvc.validateTranslationImport(loginUser, uploadFile);
            if (validationResult != null) {
                JSONObject result = validationResult.toJson();
                if (validationResult.getErrorCount() == 0) {
                    valid = true;
                    result.put(PARAM_UPLOADED_FILENAME, uploadFile.getName());
                }
                super.sendResponseResult(ControlPanelErrorCode.OK, true, result, "OK");
            } else {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, (uploadFile != null), "Fail to validate indicator import file.");
            }
        } catch (Exception ex) {
            logger.error("Fail to validate indicator import file." + uploadFile, ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, (uploadFile != null), "Error: " + ex);
        } finally {
            if (!valid) { // delete invalid file
                //uploadFile.delete();
            }
        }
        return RESULT_EMPTY;
    }

    // Export
    public String export() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        String[] idArr = request.getParameterValues("idList");
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANG_ID));
        String forTrans = request.getParameter("forTrans");

        logger.debug("Request Params: idList=" + idArr + ", lang=" + langId + ", forTrans=" + forTrans);
        try {
            if (idArr != null && idArr.length > 0) {
                List<Integer> ids = new ArrayList<Integer>();
                for (String idStr : idArr) {
                    int id = StringUtils.str2int(idStr);
                    if (id > 0) {
                        ids.add(id);
                    }
                }
                logger.debug("Export indicators: idList=" + ids);
                response.setContentType("");
                response.setHeader("Content-Disposition", "attachment; filename=IndicatorExport.xls");
                // response.setCharacterEncoding("utf-8");

                if (langId <= 0) {
                    langId = ControlPanelConstants.DEFAULT_LANGUAGE_ID;
                }

                OutputStream output = response.getOutputStream();
                if (forTrans != null && forTrans.equalsIgnoreCase("yes")) {                    
                    indicatorSrvc.exportIndicatorText(loginUser, ids, langId, output);
                } else {
                    indicatorSrvc.exportIndicators(loginUser, ids, langId, output);
                }
            } else {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                        loginUser.message(ControlPanelMessages.KEY_ERR_INDICATORS_NOT_SPECIFIED));
            }
        } catch (Exception ex) {
            logger.debug("Error occurs!", ex);
        }
        //response.getOutputStream().write('xxxxxx');
        //super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }
    // MOVE

    public String move() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        ArrayList<ReportVO> reports = new ArrayList<ReportVO>();
        String[] idList = request.getParameterValues("idList[]");
        int toLib = Integer.valueOf(request.getParameter("toLib"));
        logger.debug("Move indicators: idList=" + Arrays.asList(idList) + ", toLib=" + toLib);
        if (idList != null) {
            for (String s : idList) {
                int id = NumberUtils.toInt(s, 0);
                if (id > 0) {
                    ReportVO report = new ReportVO();
                    SurveyIndicator si = surveyIndicatorDao.get(id);

                    if (si == null) continue;

                    if (!indicatorSrvc.hasMoveAuthority(loginUser, si)) {
                        report.setErrCode(ControlPanelErrorCode.ERR_NO_PERMISSION);
                        report.setErrMsg(si.getName() + ": "
                                + loginUser.message(ControlPanelMessages.INDICATOR_MOVE__NOT_AUTHORIZED));
                    } else {
                        String bans = indicatorSrvc.checkMoveBans(loginUser, si, toLib);
                        if (bans != null) {
                            report.setErrCode(ControlPanelErrorCode.ERR_NO_PERMISSION_BANS);
                            report.setErrMsg(si.getName() + ": " + bans);
                        } else {
                            String errMsg = indicatorSrvc.moveIndicator(loginUser, id, toLib);

                            if (errMsg == null) {
                                report.setErrCode(ControlPanelErrorCode.OK);
                                report.setErrMsg(si.getName() + ": "
                                        + loginUser.message(ControlPanelMessages.KEY_OK));
                            } else {
                                report.setErrCode(ControlPanelErrorCode.ERR_UNKNOWN);
                                report.setErrMsg(si.getName() + ": " + errMsg);
                            }
                        }
                    }
                    reports.add(report);
                }
            }
        } else {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                    loginUser.message(ControlPanelMessages.KEY_ERR_INDICATORS_NOT_SPECIFIED));
            return RESULT_EMPTY;
        }

        JSONArray arr = new JSONArray();
        for (ReportVO rpt : reports) {
            arr.add(JSONUtils.toJson(rpt));
        }
        // TBD - send the report to client
        super.sendResponseResult(ControlPanelErrorCode.OK, arr, "OK");
        return RESULT_EMPTY;
    }

    public String clone() {
        LoginUser loginUser = super.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, MUST_LOGIN);
            return RESULT_EMPTY;
        }

        int indicatorId = StringUtils.str2int(request.getParameter(PARAM_ID));
        logger.debug("Request Params: \n\tindicatorId=" + indicatorId);
        int orgId = StringUtils.str2int(request.getParameter(PARAM_ORGANIZATION));
        String name = request.getParameter("name");
        String visibility = request.getParameter("visibility");
        logger.debug("Clone indicator: id=" + indicatorId
                + ",\norganization=" + orgId
                + ",\ncloned name=" + name
                + ",\nvisibility=" + visibility);

        SurveyIndicator si = surveyIndicatorDao.get(indicatorId);

        if (si == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_INDICATOR));
            return RESULT_EMPTY;
        }
        
        IndicatorCloneVO ic = new IndicatorCloneVO(indicatorId, loginUser.getUserId(), orgId, name, StringUtils.str2int(visibility));
        String errMsg = indicatorSrvc.cloneIndicator(loginUser, ic);
        int errCode;

        if (errMsg == null) {
            errCode = ControlPanelErrorCode.OK;
            errMsg = "OK";
        } else {
            errCode = ControlPanelErrorCode.ERR_UNKNOWN;
        }

        super.sendResponseResult(errCode, errMsg);
        return RESULT_EMPTY;
    }

    public Object getModel() {
        return null;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String test() {
        LoginUser user = super.getLoginUser();
        String fileName = request.getParameter("file");
        TableLoader loader = new TableLoader(user, "/tmp/"+fileName);
        String result = null;

        // Note: the TDF file must have been validated already
        try {
            loader.validate();

            List<String> errors = loader.getErrors();
            if (errors == null || errors.isEmpty()) {
                loader.setMainIndicatorInfo("TableTest", 99, 3, 3, 3, 3);
                loader.load();
                List<List<Block>> blocks = loader.getBlocks();
                result = Block.toJson(blocks);
            } else {
                StringBuilder sb = new StringBuilder();
                for (String err : errors) {
                    sb.append(err).append("\n");
                }
                result = sb.toString();
            }

        } catch (Exception ex) {
            result = "Internal Error";
        }

        super.sendResponseMessage(result);
        return null;
    }
    
    private void printURLRequestQuery() {
        if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
            Enumeration enums = request.getParameterNames();
            while (enums.hasMoreElements()){
                StringBuffer printStr = new StringBuffer();
                String name = (String)enums.nextElement();
                String[] values = request.getParameterValues(name);
                printStr.append(name).append(" :");
                for (String v : values)
                    printStr.append(" ").append(v);
                if (logger.isTraceEnabled())
                    logger.trace(printStr.toString());
                else
                    logger.debug(printStr.toString());
            }
        }
    }
}
