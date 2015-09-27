/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.common.StorageUtils;
import com.ocs.indaba.controlpanel.model.*;
import com.ocs.indaba.controlpanel.service.IIndicatorLibraryService;
import com.ocs.indaba.controlpanel.model.IndicatorImportValidation;
import com.ocs.indaba.controlpanel.tableprocessor.TableLoader;
import com.ocs.indaba.dao.AnswerTypeChoiceDAO;
import com.ocs.indaba.dao.AnswerTypeDAO;
import com.ocs.indaba.dao.AnswerTypeFloatDAO;
import com.ocs.indaba.dao.AnswerTypeIntegerDAO;
import com.ocs.indaba.dao.AnswerTypeTableDAO;
import com.ocs.indaba.dao.AnswerTypeTextDAO;
import com.ocs.indaba.dao.AtcChoiceDAO;
import com.ocs.indaba.dao.AtcChoiceIntlDAO;
import com.ocs.indaba.dao.SurveyConfigDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.SurveyIndicatorIntlDAO;
import com.ocs.indaba.po.AnswerTypeChoice;
import com.ocs.indaba.po.AnswerTypeFloat;
import com.ocs.indaba.po.AnswerTypeInteger;
import com.ocs.indaba.po.AnswerTypeTable;
import com.ocs.indaba.po.AnswerTypeText;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.po.AtcChoiceIntl;
import com.ocs.indaba.po.IndicatorTag;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import com.ocs.indaba.service.IndicatorTagService;
import com.ocs.indaba.survey.table.Block;
import org.apache.commons.io.FileUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IndicatorLibraryServiceImpl implementation class
 * 
 * @author Jeff Jiang
 */
@Service
public class IndicatorLibraryServiceImpl implements IIndicatorLibraryService {

    private static final Logger logger = Logger.getLogger(IndicatorLibraryServiceImpl.class);
    @Autowired
    private SurveyIndicatorDAO surveyIndicatorDao = null;
    @Autowired
    private AtcChoiceDAO atcChoiceDao = null;
    @Autowired
    private AnswerTypeTableDAO answerTypeTableDao = null;
    @Autowired
    private AnswerTypeChoiceDAO answerTypeChoiceDao = null;
    @Autowired
    private AnswerTypeFloatDAO answerTypeFloatDao = null;
    @Autowired
    private AnswerTypeIntegerDAO answerTypeIntegerDao = null;
    @Autowired
    private AnswerTypeTextDAO answerTypeTextDao = null;
    @Autowired
    private AnswerTypeDAO answerTypeDao = null;
    @Autowired
    private SurveyConfigDAO surveyConfigDao = null;
    @Autowired
    private SurveyIndicatorIntlDAO surveyIndicatorIntlDao = null;
    @Autowired
    private AtcChoiceIntlDAO atcChoiceIntlDao = null;
    @Autowired
    private IndicatorTagService itagSrvc;

    /**
     *
     * @param oaOfOrgIds
     * @param orgId
     * @param visibility
     * @param state
     * @param iTagId
     * @param userTag
     * @param sortName
     * @param sortOrder
     * @param offset
     * @param count
     * @return
     */
    public Pagination<IndicatorVO> findAllIndicators(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String userTag, String sortName, String sortOrder, int page, int pageSize, String queryType, String query) {
        List<SurveyIndicator> indicators = new ArrayList<SurveyIndicator>();
        int offset = (page - 1) * pageSize;
        if (iTagId > 0 && !StringUtils.isEmpty(userTag)) {
            indicators = surveyIndicatorDao.findAllIndicatorsByITagAndUTag(oaOfOrgIds, orgId, visibility, state, iTagId, userTag, sortName, sortOrder, offset, pageSize, queryType, query);
        } else if (iTagId <= 0 && StringUtils.isEmpty(userTag)) {
            indicators = surveyIndicatorDao.findAllIndicators(oaOfOrgIds, orgId, visibility, state, sortName, sortOrder, offset, pageSize, queryType, query);
        } else if (iTagId > 0 && StringUtils.isEmpty(userTag)) {
            indicators = surveyIndicatorDao.findAllIndicatorsByITag(oaOfOrgIds, orgId, visibility, state, iTagId, sortName, sortOrder, offset, pageSize, queryType, query);
        } else if (iTagId <= 0 && !StringUtils.isEmpty(userTag)) {
            indicators = surveyIndicatorDao.findAllIndicatorsByUTag(oaOfOrgIds, orgId, visibility, state, userTag, sortName, sortOrder, offset, pageSize, queryType, query);
        }
        long total = findCountOfAllIndicators(oaOfOrgIds, orgId, visibility, state, iTagId, userTag, queryType, query);
        Pagination<IndicatorVO> indicatorPage = new Pagination<IndicatorVO>(total, page, pageSize);
        indicatorPage.setRows(IndicatorVO.initWithSurveyIndicators(indicators));
        return indicatorPage;
    }

    /**
     *
     * @param oaOfOrgIds
     * @param orgId
     * @param visibility
     * @param state
     * @param iTagId
     * @param userTag
     * @return
     */
    public long findCountOfAllIndicators(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String userTag, String queryType, String query) {
        if (iTagId > 0 && !StringUtils.isEmpty(userTag)) {
            return surveyIndicatorDao.findCountOfAllIndicatorsByITagAndUTag(oaOfOrgIds, orgId, visibility, state, iTagId, userTag, queryType, query);
        } else if (iTagId <= 0 && StringUtils.isEmpty(userTag)) {
            return surveyIndicatorDao.findCountOfAllIndicators(oaOfOrgIds, orgId, visibility, state, queryType, query);
        } else if (iTagId > 0 && StringUtils.isEmpty(userTag)) {
            return surveyIndicatorDao.findCountOfAllIndicatorsByITag(oaOfOrgIds, orgId, visibility, state, iTagId, queryType, query);
        } else if (iTagId <= 0 && !StringUtils.isEmpty(userTag)) {
            return surveyIndicatorDao.findCountOfAllIndicatorsByUTag(oaOfOrgIds, orgId, visibility, state, userTag, queryType, query);
        }
        return 0l;
    }

    private IndicatorDetailVO getIndicator(SurveyIndicator si, LoginUser user) {
        IndicatorDetailVO indicatorVo = IndicatorDetailVO.initWithSurveyIndicator(si);
        indicatorVo.setOrgname(user.getOrg(indicatorVo.getOrganization()).getName());
        indicatorVo.setLanguage(si.getLanguageId());
        
        switch (si.getAnswerType()) {
            case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
            case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                List<AtcChoice> atcChoices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(si.getAnswerTypeId());
                if (atcChoices != null && !atcChoices.isEmpty()) {
                    List<AnswerChoiceSettingVO> answerChoices = new ArrayList<AnswerChoiceSettingVO>();
                    for (AtcChoice c : atcChoices) {
                        answerChoices.add(AnswerChoiceSettingVO.initWithAtcChoice(c));
                    }
                    indicatorVo.setAnswerChoices(answerChoices);
                }
                break;
            case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_INTEGER:
                AnswerTypeInteger ati = answerTypeDao.getAnswerTypeInteger(si.getAnswerTypeId());
                indicatorVo.setAnswerValueSetting(new AnswerValueSettingVO(ati.getId(), ati.getMinValue(), ati.getMaxValue(), ati.getDefaultValue(), ati.getCriteria()));
                break;
            case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_FLOAT:
                AnswerTypeFloat atl = answerTypeDao.getAnswerTypeFloat(si.getAnswerTypeId());
                indicatorVo.setAnswerValueSetting(new AnswerValueSettingVO(atl.getId(), atl.getMinValue(), atl.getMaxValue(), atl.getDefaultValue(), atl.getCriteria()));
                break;
            case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_TEXT:
                AnswerTypeText att = answerTypeDao.getAnswerTypeText(si.getAnswerTypeId());
                indicatorVo.setAnswerValueSetting(new AnswerValueSettingVO(att.getId(), att.getMinChars(), att.getMaxChars(), 0f, att.getCriteria()));
                break;
            case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_TABLE:
                AnswerTypeTable attbl = answerTypeTableDao.get(si.getAnswerTypeId());
                AnswerTableSettingVO answerTableSetting = new AnswerTableSettingVO();
                answerTableSetting.setId(attbl.getId());
                answerTableSetting.setPathName(attbl.getFilePath());
                answerTableSetting.setTdfFileName(attbl.getTdfFileName());
                indicatorVo.setAnswerTableSetting(answerTableSetting);
                break;
        }
        List<IndicatorTag> indicatorTags = itagSrvc.getIndicatorTagsByIndicatorId(si.getId());
        if (indicatorTags != null && !indicatorTags.isEmpty()) {
            for (IndicatorTag itag : indicatorTags) {
                indicatorVo.addItag(itag.getItagsId());
            }
        }
        return indicatorVo;
    }

    public IndicatorDetailVO getIndicator(LoginUser user, int indicatorId, int languageId) {

        SurveyIndicator si = surveyIndicatorDao.get(indicatorId);
        if (si == null) {
            return null;
        }
        if (languageId <= 0 || languageId == si.getLanguageId()) {
            return getIndicator(si, user);
        }

        // get VO for intl resource
        IndicatorDetailVO indicatorVo = IndicatorDetailVO.initWithSurveyIndicator(si);
        indicatorVo.setOrgname(user.getOrg(indicatorVo.getOrganization()).getName());

        SurveyIndicatorIntl siIntl = surveyIndicatorIntlDao.findByIndicatorIdAndLanguage(indicatorId, languageId);

        if (siIntl != null) {
            indicatorVo.setId(siIntl.getId());
            indicatorVo.setLanguage(languageId);
            indicatorVo.setQuestion(siIntl.getQuestion());
            indicatorVo.setSurveyIndicatorId(siIntl.getSurveyIndicatorId());
            indicatorVo.setTip(siIntl.getTip());

            switch (si.getAnswerType()) {
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                    List<AtcChoice> atcChoices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(si.getAnswerTypeId());
                    if (atcChoices != null && !atcChoices.isEmpty()) {
                        List<AnswerChoiceSettingVO> answerChoices = new ArrayList<AnswerChoiceSettingVO>();
                        for (AtcChoice c : atcChoices) {
                            AtcChoiceIntl acIntl = atcChoiceIntlDao.findByChoiceIdAndLanguage(c.getId(), languageId);
                            if (acIntl != null) {
                                AnswerChoiceSettingVO setting = new AnswerChoiceSettingVO();
                                setting.setCriteria(acIntl.getCriteria());
                                setting.setLabel(acIntl.getLabel());
                                setting.setId(acIntl.getId());
                                setting.setAtcChoiceId(c.getId());
                                setting.setUseScore(c.getUseScore());
                                setting.setValue(c.getScore());
                                answerChoices.add(setting);
                            }
                        }
                        indicatorVo.setAnswerChoices(answerChoices);
                    }
                    break;
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_INTEGER:
                    AnswerTypeInteger ati = answerTypeDao.getAnswerTypeInteger(si.getAnswerTypeId());
                    indicatorVo.setAnswerValueSetting(new AnswerValueSettingVO(ati.getId(), ati.getMinValue(), ati.getMaxValue(), ati.getDefaultValue(), ati.getCriteria()));
                    break;
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_FLOAT:
                    AnswerTypeFloat atl = answerTypeDao.getAnswerTypeFloat(si.getAnswerTypeId());
                    indicatorVo.setAnswerValueSetting(new AnswerValueSettingVO(atl.getId(), atl.getMinValue(), atl.getMaxValue(), atl.getDefaultValue(), atl.getCriteria()));
                    break;
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_TEXT:
                    AnswerTypeText att = answerTypeDao.getAnswerTypeText(si.getAnswerTypeId());
                    indicatorVo.setAnswerValueSetting(new AnswerValueSettingVO(att.getId(), att.getMinChars(), att.getMaxChars(), 0f, att.getCriteria()));
                    break;
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_TABLE:
                    AnswerTypeTable attbl = answerTypeTableDao.get(si.getAnswerTypeId());
                    AnswerTableSettingVO answerTableSetting = new AnswerTableSettingVO();
                    answerTableSetting.setId(attbl.getId());
                    answerTableSetting.setPathName(attbl.getFilePath());
                    answerTableSetting.setTdfFileName(attbl.getTdfFileName());
                    indicatorVo.setAnswerTableSetting(answerTableSetting);
                    break;
            }
        } else {
            indicatorVo.setId(-1);
            indicatorVo.setLanguage(languageId);
            indicatorVo.setQuestion("");
            indicatorVo.setSurveyIndicatorId(indicatorId);
            indicatorVo.setTip("");
            switch (si.getAnswerType()) {
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                case com.ocs.indaba.common.Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                    List<AtcChoice> atcChoices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(si.getAnswerTypeId());
                    if (atcChoices != null && !atcChoices.isEmpty()) {
                        List<AnswerChoiceSettingVO> answerChoices = new ArrayList<AnswerChoiceSettingVO>();
                        for (AtcChoice c : atcChoices) {
                            AnswerChoiceSettingVO setting = new AnswerChoiceSettingVO();
                            setting.setCriteria("");
                            setting.setLabel("");
                            setting.setId(-1);
                            setting.setUseScore(c.getUseScore());
                            setting.setValue(c.getScore());
                            answerChoices.add(setting);
                        }
                        indicatorVo.setAnswerChoices(answerChoices);
                    }
                    break;                
            }
        }
        
        return indicatorVo;
    }

    public String deleteIndicator(LoginUser user, int id) {
        SurveyIndicator si = surveyIndicatorDao.get(id);
        if (si == null) {
            return user.message(ControlPanelMessages.PROGRAM_ERROR);
        }

        surveyIndicatorDao.markIndicatorDeleted(id, user.getUserId());

        return null;
    }

    public String editIndicator(LoginUser user, IndicatorDetailVO indicator) {
        if (indicator == null) {
            return user.message(ControlPanelMessages.PROGRAM_ERROR);
        }

        SurveyIndicator si = surveyIndicatorDao.get(indicator.getId());
        if (si == null) {
            return user.message(ControlPanelMessages.PROGRAM_ERROR);
        }

        if (indicator.getName() == null || indicator.getName().isEmpty()) {
            return user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "Name");
        }

        if (!indicator.getName().equalsIgnoreCase(si.getName())) {
            // name has changed - make sure the new name doesn't already exist in DB
            if (surveyIndicatorDao.selectSurveyIndicatorByName(indicator.getName()) != null) {
                return user.message(ControlPanelMessages.INDICATOR_IMPORT__NAME_ALREADY_IN_DB);
            }
        }

        si.setId(indicator.getId());
        si.setLanguageId(indicator.getLanguage());
        si.setName(indicator.getName());
        si.setQuestion(indicator.getQuestion());
        si.setReferenceId(indicator.getReference());
        si.setTip(indicator.getTip());
        si.setOwnerOrgId(indicator.getOrganization());//todo should check

        //survey answer issue
        short newType = (short) indicator.getAnswerType();
        short oldType = si.getAnswerType();
        if (oldType == newType) {//not change answer type, try to update
            int newAnswerTypeId = updateSurveyAnswerType(si, indicator);
            if (newAnswerTypeId > 0) {
                si.setAnswerTypeId(newAnswerTypeId);
            }
        } else {//answer type has been changed, so remove old answer object, assign new one
            // removeSurveyAnswerType(si);
            int answerTypeId = createSurveyAnswerType(indicator);
            if (answerTypeId > 0) {
                si.setAnswerTypeId(answerTypeId);
            }
            si.setAnswerType(newType);
        }
        
        surveyIndicatorDao.update(si);
        
        if (si.getAnswerType() == Constants.SURVEY_ANSWER_TYPE_TABLE) {
            createComponentIndicators(user, si);
        }

        // handle indicator tags
        itagSrvc.deleteIndicatorTagsByIndicatorId(si.getId());
        itagSrvc.addIndicatorTags(si.getId(), indicator.getItags());

        return null;
    }


    public String cloneIndicator(LoginUser user, IndicatorCloneVO ic) {
        SurveyIndicator si = surveyIndicatorDao.get(ic.getIndicatorId());

        if (si == null) {
            return user.message(ControlPanelMessages.PROGRAM_ERROR);
        }

        // check the uniqueness of the indicator name
        if (ic.getName() == null || ic.getName().isEmpty()) {
            return user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "Name");
        }
        if (surveyIndicatorDao.selectSurveyIndicatorByName(ic.getName()) != null) {
            return user.message(ControlPanelMessages.INDICATOR_IMPORT__NAME_ALREADY_IN_DB);
        }

        int answerType = si.getAnswerType();
        int answerTypeId = si.getAnswerTypeId();
        int newAnswerTypeId = 0;
        AnswerTypeTable tableDef = null;

        switch (answerType) {
            case Constants.SURVEY_ANSWER_TYPE_FLOAT: {
                AnswerTypeFloat at = answerTypeFloatDao.get(answerTypeId);
                if (at != null) {
                    at.setId(null);
                    AnswerTypeFloat newAt = answerTypeFloatDao.create(at);
                    newAnswerTypeId = newAt.getId();
                }
                break;
            }
            case Constants.SURVEY_ANSWER_TYPE_INTEGER: {
                AnswerTypeInteger at = answerTypeIntegerDao.get(answerTypeId);
                if (at != null) {
                    at.setId(null);
                    AnswerTypeInteger newAt = answerTypeIntegerDao.create(at);
                    newAnswerTypeId = newAt.getId();
                }
                break;
            }
            case Constants.SURVEY_ANSWER_TYPE_TEXT: {
                AnswerTypeText at = answerTypeTextDao.get(answerTypeId);
                if (at != null) {
                    at.setId(null);
                    AnswerTypeText newAt = answerTypeTextDao.create(at);
                    newAnswerTypeId = newAt.getId();
                }
                break;
            }
            case Constants.SURVEY_ANSWER_TYPE_TABLE: {
                AnswerTypeTable at = answerTypeTableDao.get(answerTypeId);
                if (at != null) {
                    at.setId(null);
                    tableDef = answerTypeTableDao.create(at);
                    newAnswerTypeId = tableDef.getId();
                }
                break;
            }

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE: {
                AnswerTypeChoice at = answerTypeChoiceDao.get(answerTypeId);
                if (at != null) {
                    at.setId(null);
                    AnswerTypeChoice newAt = answerTypeChoiceDao.create(at);
                    newAnswerTypeId = newAt.getId();
                    List<AtcChoice> choices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(answerTypeId);
                    if (choices != null && !choices.isEmpty()) {
                        for (AtcChoice choice : choices) {
                            choice.setId(null);
                            choice.setAnswerTypeChoiceId(newAnswerTypeId);
                            atcChoiceDao.create(choice);
                        }
                    }
                }
                break;
            }
            default:
                return null;
        }

        si.setId(null);
        si.setAnswerTypeId(newAnswerTypeId);
        si.setOriginalIndicatorId(ic.getIndicatorId());
        si.setName(ic.getName());
        si.setOwnerOrgId(ic.getOrgId());
        si.setCreatorOrgId(ic.getOrgId());
        si.setCreateUserId(ic.getUserId());
        si.setCreateTime(Calendar.getInstance().getTime());
        si.setStatus(Constants.INDICATOR_STATUS_ACTIVE);
        si.setVisibility((short)ic.getVisibity());

        // newly created indicators are always put in test state
        si.setState((short)Constants.RESOURCE_STATE_TEST);

        SurveyIndicator newIndicator = surveyIndicatorDao.create(si);
        if (newIndicator == null) {
            logger.error("Failed to create cloned indicator: " + si.getName());
            return user.message(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL);
        }

        // clone tags if any
        List<IndicatorTag> indicatorTags = itagSrvc.getIndicatorTagsByIndicatorId(ic.getIndicatorId());
        if (indicatorTags != null && !indicatorTags.isEmpty()) {
            for (IndicatorTag itag : indicatorTags) {
                itagSrvc.addIndicatorTag(newIndicator.getId(), itag.getItagsId());
            }
        }

        if (answerType == Constants.SURVEY_ANSWER_TYPE_TABLE) {
            // process the TDF and create component indicators
            createComponentIndicators(user, newIndicator, tableDef);
        }

        return null;
    }

    public String createIndicator(LoginUser user, IndicatorDetailVO indicator) {
        // check the uniqueness of the indicator name
        if (indicator.getName() == null || indicator.getName().isEmpty()) {
            return user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "Name");
        }
        if (surveyIndicatorDao.selectSurveyIndicatorByName(indicator.getName()) != null) {
            return user.message(ControlPanelMessages.INDICATOR_IMPORT__NAME_ALREADY_IN_DB);
        }

        SurveyIndicator si = new SurveyIndicator();
        si.setLanguageId(indicator.getLanguage());
        si.setName(indicator.getName());
        si.setQuestion(indicator.getQuestion());
        si.setReferenceId(indicator.getReference());
        si.setTip(indicator.getTip());
        si.setCreateUserId(indicator.getUserId());
        si.setCreateTime(Calendar.getInstance().getTime());
        si.setReusable(true);//todo what this means?
        si.setCreatorOrgId(indicator.getOrganization());
        si.setOwnerOrgId(indicator.getOrganization());//Owner – the org that owns the resource. Initially it is the same as the creator.

        si.setVisibility((indicator.getVisibility() == ControlPanelConstants.INDICATOR_LIB_VISIBILITY_PRIVATE) ? Constants.VISIBILITY_PRIVATE : Constants.VISIBILITY_PUBLIC);//Creator creates the resource and sets it as public.
        si.setState((short) Constants.RESOURCE_STATE_TEST);//The system sets the resource in test state initially.
        si.setStatus(Constants.INDICATOR_STATUS_ACTIVE);
        si.setParentIndicatorId(0);

        //set survey answer
        si.setAnswerType((short) indicator.getAnswerType());
        int answerTypeId = createSurveyAnswerType(indicator);
        if (answerTypeId > 0) {
            si.setAnswerTypeId(answerTypeId);
        }

        SurveyIndicator newSi = surveyIndicatorDao.create(si);
        indicator.setId(newSi.getId());

        if (newSi.getAnswerType() == Constants.SURVEY_ANSWER_TYPE_TABLE) {
            createComponentIndicators(user, newSi);
        }

        // handle indicator tags
        itagSrvc.addIndicatorTags(si.getId(), indicator.getItags());

        return null;
    }


    private String createComponentIndicators(LoginUser user, SurveyIndicator si) {
        AnswerTypeTable tableDef = answerTypeTableDao.get(si.getAnswerTypeId());
        return createComponentIndicators(user, si, tableDef);
    }


    private String createComponentIndicators(LoginUser user, SurveyIndicator si, AnswerTypeTable tableDef) {
        if (si == null) return user.message(ControlPanelMessages.PROGRAM_ERROR);

        // remove old components if any
        surveyIndicatorDao.deleteComponentIndicators(si.getId());

        String filePath = StorageUtils.getUploadFilePathOfFilename(
                tableDef.getFilePath(), ControlPanelConstants.UPLOAD_TYPE_TABLE);
        TableLoader loader = new TableLoader(user, filePath);

        // Note: the TDF file must have been validated already
        try {
            loader.validate();
        } catch (Exception ex) {
            logger.error("Failed to validate TDF file: " + filePath);
            loader.close();
            return user.message(ControlPanelMessages.PROGRAM_ERROR);
        }

        List<String> errors = loader.getErrors();
        if (errors != null && !errors.isEmpty()) {
            return errors.get(0);
        }

        loader.setMainIndicatorInfo(si.getName(), si.getId(), si.getOwnerOrgId(),
                si.getVisibility(), si.getLanguageId(), si.getState());

        loader.load();

        List<List<Block>> blocks = loader.getBlocks();
        String jsonData = Block.toJson(blocks);
        tableDef.setData(jsonData);
        answerTypeTableDao.update(tableDef);
        loader.close();

        return null;
    }


    public TableValidationResult validateTableDefinition(LoginUser user, InputStream fileData, String origFileName) {
        TableValidationResult result = new TableValidationResult();
        result.setOriginalFileName(origFileName);

        List<String> errors = new ArrayList<String>();

        File uploadFile = StorageUtils.getUploadFile(origFileName, ControlPanelConstants.UPLOAD_TYPE_TABLE);

        // now uploadFile is at the system's storage folder!
        try {
            FileUtils.copyInputStreamToFile(fileData, uploadFile);
        } catch (Exception ex) {
            errors.add("Internal Error - cannot copy file data.");
            result.setErrors(errors);
            return result;
        }

        String filePath = uploadFile.getPath();
        result.setFilePath(uploadFile.getName());

        TableLoader loader = new TableLoader(user, filePath);
        try {
            loader.validate();
        } catch (Exception ex) {
            logger.error("Failed to validate TDF file: " + filePath);
            errors.add("The file is not a valid Table Definition File.");
            result.setErrors(errors);
            loader.close();
            return result;
        }

        result.setErrors(loader.getErrors());
        loader.close();
        return result;
    }
    

    public int importIndicators(LoginUser user, File file) {
        IndicatorImporter importer = new IndicatorImporter(user, file);
        int count = importer.load();

        logger.debug("Indicators imported: " + count);

        return count;
    }

    public IndicatorImportValidation validateIndicatorImport(LoginUser user, File file) {
        IndicatorImportValidator validator = new IndicatorImportValidator(user, file);
        IndicatorImportValidation result = null;

        try {
            result = validator.validate();
        } catch (Exception ex) {
            logger.error("Fail to validate indicators.", ex);
        }

        logger.debug("Finished validating file " + file);
        if (result != null) {
            logger.debug("Error Count: " + result.getErrorCount());
        }

        return result;
    }


    public int exportIndicators(LoginUser user, List<Integer> indicatorIDs, int languageId, OutputStream output) {
        try {
            IndicatorExporter exp = new IndicatorExporter(indicatorIDs, languageId, output);
            return exp.export();
        } catch (Exception e) {
            logger.error("Fail to export indicators.", e);
            return 0;
        }
    }


    public int exportIndicatorText(LoginUser user, List<Integer> indicatorIDs, int languageId, OutputStream output) {
        try {
            IndicatorTextExporter exp = new IndicatorTextExporter(indicatorIDs, languageId, output);
            return exp.export();
        } catch (Exception e) {
            logger.error("Fail to export indicators.", e);
            return 0;
        }
    }


    public String moveIndicator(LoginUser user, int id, int toLib) {

        int state = 0;
        switch (toLib) {
            case Constants.INDICATOR_LIB_VISIBILITY_PRIVATE:
                // can't move to private
                return user.message(ControlPanelMessages.INDICATOR_MOVE__PRIVATE_NO_MOVE);

            case Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED:
                state = Constants.RESOURCE_STATE_ENDORSED;
                break;

            case Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED:
                state = Constants.RESOURCE_STATE_EXTENDED;
                break;

            default:
                state = Constants.RESOURCE_STATE_TEST;
                break;
        }

        SurveyIndicator si = surveyIndicatorDao.get(id);
        if (si == null) {
            return user.message(ControlPanelMessages.PROGRAM_ERROR);
        }
       
        if (si.getVisibility() != Constants.VISIBILITY_PUBLIC) {
            // can't move private indicators
            return user.message(ControlPanelMessages.INDICATOR_MOVE__PRIVATE_NO_MOVE);
        }
        
        if (si.getState() == state) {
            // already in the right state - no move necessary
            return null;
        }
        
        int ownerOrgId = si.getOwnerOrgId();

        /*** don't change owner_org_id for now - YC
        if (state == Constants.RESOURCE_STATE_TEST) {
            // set owner org id back to creator org
            ownerOrgId = si.getCreatorOrgId();
        } else if (si.getState() == Constants.RESOURCE_STATE_TEST) {
            // move the indicator from test to other states - set owenr org to ORG 1
            ownerOrgId = INDABA_ORG_ID;
        }
         * ***/
        
        surveyIndicatorDao.updateMoveState(id, Constants.VISIBILITY_PUBLIC, state, ownerOrgId);
        
        return null;
    }


    private int createSurveyAnswerType(IndicatorDetailVO indicator) {
        int answerTypeId = -1;

        switch (indicator.getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_INTEGER: {
                AnswerValueSettingVO as = indicator.getAnswerValueSetting();
                if (as != null) {
                    AnswerTypeInteger at = new AnswerTypeInteger();
                    at.setCriteria(as.getCriteria());
                    at.setDefaultValue(as.getIntDefaultVal());
                    at.setMaxValue(as.getIntMaxVal());
                    at.setMinValue(as.getIntMinVal());
                    AnswerTypeInteger newAt = answerTypeIntegerDao.create(at);
                    as.setId(newAt.getId());
                    answerTypeId = newAt.getId();
                }
            }
            break;
            case Constants.SURVEY_ANSWER_TYPE_FLOAT: {
                AnswerValueSettingVO as = indicator.getAnswerValueSetting();
                if (as != null) {
                    AnswerTypeFloat at = new AnswerTypeFloat();
                    at.setCriteria(as.getCriteria());
                    at.setDefaultValue(as.getDefaultVal());
                    at.setMaxValue(as.getMaxVal());
                    at.setMinValue(as.getMinVal());
                    AnswerTypeFloat newAt = answerTypeFloatDao.create(at);
                    as.setId(newAt.getId());
                    answerTypeId = newAt.getId();
                }
            }
            break;
            case Constants.SURVEY_ANSWER_TYPE_TEXT: {
                AnswerValueSettingVO as = indicator.getAnswerValueSetting();
                if (as != null) {
                    AnswerTypeText at = new AnswerTypeText();
                    at.setCriteria(as.getCriteria());
                    at.setMaxChars(as.getIntMaxVal());
                    at.setMinChars(as.getIntMinVal());
                    AnswerTypeText newAt = answerTypeTextDao.create(at);
                    as.setId(newAt.getId());
                    answerTypeId = newAt.getId();
                }
            }
            case Constants.SURVEY_ANSWER_TYPE_TABLE: {
                AnswerTableSettingVO as = indicator.getAnswerTableSetting();
                if (as != null) {
                    AnswerTypeTable at = new AnswerTypeTable();
                    at.setFilePath(as.getPathName());
                    at.setTdfFileName(as.getTdfFileName());
                    AnswerTypeTable newAt = answerTypeTableDao.create(at);
                    as.setId(newAt.getId());
                    answerTypeId = newAt.getId();
                }
            }
            break;
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE: {
                List<AnswerChoiceSettingVO> answerChoices = indicator.getAnswerChoices();
                if (answerChoices != null && !answerChoices.isEmpty()) {
                    AnswerTypeChoice choice = new AnswerTypeChoice();
                    AnswerTypeChoice newChoice = answerTypeChoiceDao.create(choice);
                    answerTypeId = newChoice.getId();
                    int weight = 1;
                    int mask = 1;
                    for (AnswerChoiceSettingVO as : answerChoices) {
                        AtcChoice ac = new AtcChoice();
                        ac.setAnswerTypeChoiceId(answerTypeId);
                        ac.setCriteria(as.getCriteria());
                        ac.setLabel(as.getLabel());
                        ac.setMask(mask);
                        ac.setScore((int)(as.getValue() * 10000));  // need to multiply by 10000
                        ac.setWeight(weight);
                        ac.setUseScore(as.isUseScore());
                        AtcChoice newAc = atcChoiceDao.create(ac);
                        as.setId(newAc.getId());
                        weight++;
                        mask = mask << 1;
                    }
                }
            }
            break;
        }
        return answerTypeId;
    }

    /*
    private void removeSurveyAnswerType(SurveyIndicator indicator) {
        int type = indicator.getAnswerType();
        int id = indicator.getAnswerTypeId();
        if (id <= 0) {
            return;
        }

        switch (type) {
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                answerTypeTextDao.delete(id);
                break;

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                answerTypeIntegerDao.delete(id);
                break;

            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                answerTypeFloatDao.delete(id);
                break;

            case Constants.SURVEY_ANSWER_TYPE_TABLE:
                answerTypeTableDao.delete(id);
                break;

            default:
                atcChoiceDao.deleteByAnswerTypeId(id);
                answerTypeChoiceDao.delete(id);
        }
    }
     * */
     

    private int updateSurveyAnswerType(SurveyIndicator si, IndicatorDetailVO indicator) {
        int type = si.getAnswerType();
        int id = si.getAnswerTypeId();

        switch(type) {

            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                AnswerValueSettingVO as = indicator.getAnswerValueSetting();
                if (as == null) return -1;

                AnswerTypeText att = answerTypeTextDao.get(id);
                boolean isOld = true;
                if (att == null) {
                    att = new AnswerTypeText();
                    isOld = false;
                }
                att.setCriteria(as.getCriteria());
                att.setMaxChars(as.getIntMaxVal());
                att.setMinChars(as.getIntMinVal());
                if (isOld) {
                    answerTypeTextDao.update(att);
                } else {
                    att = answerTypeTextDao.create(att);
                }

                return att.getId();

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                as = indicator.getAnswerValueSetting();
                if (as == null) return -1;

                AnswerTypeInteger ati = answerTypeIntegerDao.get(id);
                isOld = true;
                if (ati == null) {
                    ati = new AnswerTypeInteger();
                    isOld = false;
                }
                ati.setCriteria(as.getCriteria());
                ati.setDefaultValue(as.getIntDefaultVal());
                ati.setMaxValue(as.getIntMaxVal());
                ati.setMinValue(as.getIntMinVal());
                if (isOld) {
                    answerTypeIntegerDao.update(ati);
                } else {
                    ati = answerTypeIntegerDao.create(ati);
                }

                return ati.getId();

            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                as = indicator.getAnswerValueSetting();
                if (as == null) return -1;
                AnswerTypeFloat atf = answerTypeFloatDao.get(id);
                isOld = true;
                if (atf == null) {
                    atf = new AnswerTypeFloat();
                    isOld = false;
                }
                atf.setCriteria(as.getCriteria());
                atf.setDefaultValue(as.getDefaultVal());
                atf.setMaxValue(as.getMaxVal());
                atf.setMinValue(as.getMinVal());
                if (isOld) {
                    answerTypeFloatDao.update(atf);
                } else {
                    atf = answerTypeFloatDao.create(atf);
                }

                return atf.getId();

            case Constants.SURVEY_ANSWER_TYPE_TABLE:
                AnswerTableSettingVO ats = indicator.getAnswerTableSetting();
                if (ats == null) return -1;

                AnswerTypeTable atb = answerTypeTableDao.get(id);
                isOld = true;
                if (atb == null) {
                    atb = new AnswerTypeTable();
                    isOld = false;
                }
                atb.setFilePath(ats.getPathName());
                atb.setTdfFileName(ats.getTdfFileName());
                if (isOld) {
                    answerTypeTableDao.update(atb);
                } else {
                    atb = answerTypeTableDao.create(atb);
                }

                return atb.getId();

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                List<AnswerChoiceSettingVO> answerChoices = indicator.getAnswerChoices();
                if (answerChoices == null || answerChoices.isEmpty()) {
                    return -1;
                }

                int atcId = 0;
                AnswerTypeChoice at = answerTypeChoiceDao.get(id);
                if (at == null) {
                    // create the answerTypeChoice record
                    at = new AnswerTypeChoice();
                    AnswerTypeChoice newAt = answerTypeChoiceDao.create(at);
                    atcId = newAt.getId();
                } else {
                    atcId = at.getId();

                    // remove existing atcChoice records
                    // YANTODO: don't remove all choices. Try to keep the ones that already exist so their
                    // translations can be kept!!!
                    atcChoiceDao.deleteByAnswerTypeId(atcId);
                }

                // add AtcChoice records
                int weight = 1;
                int mask = 1;
                for (AnswerChoiceSettingVO acs : answerChoices) {
                    AtcChoice ac = new AtcChoice();
                    ac.setAnswerTypeChoiceId(atcId);
                    ac.setCriteria(acs.getCriteria());
                    ac.setLabel(acs.getLabel());
                    ac.setMask(mask);//todo
                    ac.setUseScore(acs.isUseScore());
                    ac.setScore((int) (acs.getValue() * 10000.0));
                    ac.setWeight(weight);//todo
                    AtcChoice newAc = atcChoiceDao.create(ac);
                    acs.setId(newAc.getId());
                    weight++;
                    mask = mask << 1;
                }
                return atcId;
        }
        return -1;
    }

    public int translate(LoginUser user, int origIndicatorId, int langId, IndicatorDetailVO indicatorVo) {

        TransValidation trans = new TransValidation();
        trans.setIndicatorId(origIndicatorId);
        trans.setLangId(indicatorVo.getLanguage());
        trans.setAnswerTypeId(indicatorVo.getAnswerTypeId());
        trans.setName(indicatorVo.getName());
        trans.setQuestion(indicatorVo.getQuestion());
        trans.setTip(indicatorVo.getTip());
        trans.setType(indicatorVo.getAnswerType());
        if (indicatorVo.getAnswerType() == Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE
                || indicatorVo.getAnswerType() == Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE) {
            List<AnswerChoiceSettingVO> choiceSettings = indicatorVo.getAnswerChoices();
            if (choiceSettings != null && !choiceSettings.isEmpty()) {
                for (AnswerChoiceSettingVO vo : choiceSettings) {
                    IndicatorChoiceTrans ict = new IndicatorChoiceTrans();
                    ict.setAtcChoiceId(vo.getAtcChoiceId());
                    ict.setCriteria(vo.getCriteria());
                    ict.setLabel(vo.getLabel());
                    trans.addChoice(ict);
                }
            }
        }
        trans.load();
        return 0;
    }

    public IndicatorTransValidation validateTranslationImport(LoginUser user, File file) {
        TransValidator validator = new TransValidator(user, file);
        IndicatorTransValidation result = null;

        try {
            result = validator.validate();
        } catch (Exception ex) {
            logger.error("Fail to validate indicators.", ex);
        }

        logger.debug("Finished validating file " + file);
        if (result != null) {
            logger.debug("Error Count: " + result.getErrorCount());
        }

        return result;
    }

    public int importTranslations(LoginUser user, File file) {
        TransImporter importer = new TransImporter(user, file);
        int count = importer.load();

        logger.debug("Translation imported for indicators: " + count);

        return count;
    }


     /**
     * Whether the user has editing authority for the indicator
     * @param userId
     * @param indicatorId
     * @return
     */
    public boolean hasEditAuthority(LoginUser user, SurveyIndicator indicator) {
        if (user == null || indicator == null) return false;
        return user.getChecker().hasOrgAuthority(indicator.getOwnerOrgId());
    }

    public boolean hasEditAuthority(LoginUser user, IndicatorVO indicator) {
        if (user == null || indicator == null) return false;
        return user.getChecker().hasOrgAuthority(indicator.getOrgId());
    }

    /**
     * check any bans on edit
     * @param indicatorId
     * @return error string if there is any ban. Null if no ban. The error string is localized.
     */
    public String checkEditBans(LoginUser user, SurveyIndicator indicator) {
        // the indicator is not editable if it is used by any survey configs
        List<String> surveyConfNames = surveyConfigDao.getActiveSurveyConfigNameByIndicatorId(indicator.getId());
        if (surveyConfNames != null && surveyConfNames.size() > 0) {
            return user.message(ControlPanelMessages.INDICATOR_EDIT__USED_IN_SC);
        }

        return null;
    }

    
    public boolean hasMoveAuthority(LoginUser user, SurveyIndicator indicator) {
        if (user == null || indicator == null) return false;

        // only super user can move indicators
        return user.isSiteAdmin();
    }

    public boolean hasMoveAuthority(LoginUser user, IndicatorVO indicator) {
        if (user == null || indicator == null) return false;

        // only super user can move indicators
        return user.isSiteAdmin();
    }

    /**
     * check any bans on move
     *
     * @param indicatorId
     * @return error string if there is any ban. Null if no ban. The error string is localized.
     */
    public String checkMoveBans(LoginUser user, SurveyIndicator indicator, int toLib) {
        if (indicator.getVisibility() == Constants.VISIBILITY_PRIVATE) {
            // private indicators cannot be moved to public
            return user.message(ControlPanelMessages.INDICATOR_MOVE__PRIVATE_NO_MOVE);
        } else {
            // public indicator
            if (toLib == Constants.INDICATOR_LIB_VISIBILITY_PRIVATE) {
                // private indicators cannot be moved to public
                return user.message(ControlPanelMessages.INDICATOR_MOVE__PUBLIC_TO_PRIVATE);
            }
        }
        return null;
    }


    public boolean hasDeleteAuthority(LoginUser user, SurveyIndicator indicator) {
        if (user == null || indicator == null) return false;
        return user.getChecker().hasOrgAuthority(indicator.getOwnerOrgId());
    }

    public boolean hasDeleteAuthority(LoginUser user, IndicatorVO indicator) {
        if (user == null || indicator == null) return false;
        return user.getChecker().hasOrgAuthority(indicator.getOrgId());
    }


    public String checkDeleteBans(LoginUser user, SurveyIndicator indicator) {
        // the indicator can not be deleted if it is used by any survey configs
        List<String> surveyConfNames = surveyConfigDao.getActiveSurveyConfigNameByIndicatorId(indicator.getId());
        
        if (surveyConfNames != null && surveyConfNames.size() > 0) {
            return user.message(ControlPanelMessages.INDICATOR_DELETE__USED_IN_SC);
        }

        return null;
    }

    public boolean hasTranslateAuthority(LoginUser user, IndicatorVO indicator) {
        if (user == null || indicator == null) return false;
        return user.getChecker().hasOrgAuthority(indicator.getOrgId());
    }

    
}
