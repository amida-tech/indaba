/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.survey.table;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.AnswerObjectChoiceDAO;
import com.ocs.indaba.dao.AnswerObjectDAO;
import com.ocs.indaba.dao.AnswerObjectFloatDAO;
import com.ocs.indaba.dao.AnswerObjectIntegerDAO;
import com.ocs.indaba.dao.AnswerObjectTextDAO;
import com.ocs.indaba.dao.AnswerTypeChoiceDAO;
import com.ocs.indaba.dao.AnswerTypeFloatDAO;
import com.ocs.indaba.dao.AnswerTypeIntegerDAO;
import com.ocs.indaba.dao.AnswerTypeTableDAO;
import com.ocs.indaba.dao.AnswerTypeTextDAO;
import com.ocs.indaba.dao.SprComponentDAO;
import com.ocs.indaba.dao.SurveyAnswerComponentDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.SurveyIndicatorIntlDAO;
import com.ocs.indaba.po.AnswerObjectChoice;
import com.ocs.indaba.po.AnswerObjectFloat;
import com.ocs.indaba.po.AnswerObjectInteger;
import com.ocs.indaba.po.AnswerObjectText;
import com.ocs.indaba.po.AnswerTypeFloat;
import com.ocs.indaba.po.AnswerTypeInteger;
import com.ocs.indaba.po.AnswerTypeTable;
import com.ocs.indaba.po.AnswerTypeText;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.po.SprComponent;
import com.ocs.indaba.po.SurveyAnswerComponent;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.AnswerTypeChoiceDef;
import com.ocs.indaba.vo.SurveyAnswerObject;
import com.ocs.indaba.vo.SurveyAnswerSubmitView;
import com.ocs.ssu.ExcelCell;
import com.ocs.ssu.ExcelWriter;
import com.ocs.util.StringUtils;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class SurveyTableService {

    private static final Logger logger = Logger.getLogger(SurveyTableService.class);
    @Autowired
    private SurveyIndicatorDAO surveyIndicatorDao = null;
    @Autowired
    private SurveyIndicatorIntlDAO surveyIndicatorIntlDao = null;
    @Autowired
    private AnswerTypeTableDAO atTableDao = null;
    @Autowired
    private AnswerTypeIntegerDAO atIntegerDao = null;
    @Autowired
    private AnswerTypeFloatDAO atFloatDao = null;
    @Autowired
    private AnswerTypeChoiceDAO atChoiceDao = null;
    @Autowired
    private AnswerTypeTextDAO atTextDao = null;
    @Autowired
    private AnswerObjectIntegerDAO aoIntegerDao = null;
    @Autowired
    private AnswerObjectFloatDAO aoFloatDao = null;
    @Autowired
    private AnswerObjectChoiceDAO aoChoiceDao = null;
    @Autowired
    private AnswerObjectTextDAO aoTextDao = null;
    @Autowired
    private SurveyAnswerComponentDAO sacDao = null;
    @Autowired
    private SurveyAnswerDAO saDao = null;
    @Autowired
    private AnswerObjectDAO answerObjDao = null;
    @Autowired
    private SprComponentDAO sprComponentDao = null;
    private SurveyService surveyService;

    private class HorseAnswerObjectRetriever implements AnswerObjectRetriever {

        @Override
        public List<SurveyAnswerObject> getAnswerObjects(int horseId, AnswerObjectDAO answerObjDao, int surveyQuestionId) {
            return answerObjDao.selectComponentAnswerObjects(horseId, surveyQuestionId);
        }
    }

    private class VersionAnswerObjectRetriever implements AnswerObjectRetriever {

        @Override
        public List<SurveyAnswerObject> getAnswerObjects(int contentVersionId, AnswerObjectDAO answerObjDao, int surveyQuestionId) {
            return answerObjDao.selectContentVersionComponentAnswerObjects(contentVersionId, surveyQuestionId);
        }
    }

    private class PeerReviewAnswerObjectRetriever implements AnswerObjectRetriever {

        @Override
        public List<SurveyAnswerObject> getAnswerObjects(int surveyPeerReviewId, AnswerObjectDAO answerObjDao, int surveyQuestionId) {
            return answerObjDao.selectPeerReviewComponentAnswerObjects(surveyPeerReviewId);
        }
    }

    private class PeerReviewVersionAnswerObjectRetriever implements AnswerObjectRetriever {

        @Override
        public List<SurveyAnswerObject> getAnswerObjects(int surveyPeerReviewVersionId, AnswerObjectDAO answerObjDao, int surveyQuestionId) {
            return answerObjDao.selectPeerReviewVersionComponentAnswerObjects(surveyPeerReviewVersionId);
        }
    }

    /**
     * Generate HTML for rendering the table for the work version
     *
     * @param horseId - horse id
     * @param surveyQuestionId - parent survey question id
     * @param langId - user's language
     * @param inputDisabled - editable or not?
     * @return
     */
    public String generateTableHtml(int horseId, int mainSurveyQuestionId, int userId, int langId, boolean inputDisabled) {
        return generateHtml(horseId, new HorseAnswerObjectRetriever(), mainSurveyQuestionId,
                langId, inputDisabled, "main");
    }


    /*
     * Generate HTML for rendering the table for a content version
     */
    public String generateContentVersionTableHtml(int contentVersionId, int mainSurveyQuestionId, int langId) {
        return generateHtml(contentVersionId, new VersionAnswerObjectRetriever(), mainSurveyQuestionId,
                langId, true, "cv" + contentVersionId);
    }


    /*
     * Generate HTML for rendering the table for a peer review.
     * Note: use this method only if the survey peer review has the opinion DISAGREE
     */
    public String generatePeerReviewTableHtml(int surveyPeerReviewId, int mainSurveyQuestionId, int langId, boolean inputDisabled) {
        return generateHtml(surveyPeerReviewId, new PeerReviewAnswerObjectRetriever(), mainSurveyQuestionId,
                langId, inputDisabled, "pr" + surveyPeerReviewId);
    }

    /*
     * Generate HTML for rendering the table for a peer review version.
     * Note: use this method only if the survey peer review has the opinion DISAGREE
     */
    public String generatePeerReviewVersionTableHtml(int surveyPeerReviewVersionId, int mainSurveyQuestionId, int langId) {
        return generateHtml(surveyPeerReviewVersionId, new PeerReviewVersionAnswerObjectRetriever(), mainSurveyQuestionId,
                langId, true, "prv" + surveyPeerReviewVersionId);
    }

    private String generateHtml(int anchorObjId, AnswerObjectRetriever retriever, int mainSurveyQuestionId, int langId, boolean inputDisabled, String prefix) {
        TableServiceContext ctx = this.createTableServiceContext(mainSurveyQuestionId, langId);

        if (ctx == null) return null;
       
        List<SurveyAnswerObject> answerObjs = retriever.getAnswerObjects(anchorObjId, answerObjDao, mainSurveyQuestionId);

        if (answerObjs != null) {
            for (SurveyAnswerObject obj : answerObjs) {
                ctx.setAnswerObject(obj);                
            }
        }

        return generateHtml(ctx, inputDisabled, prefix);
    }

    private String getQuestionCellId(int indicatorId, String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        return prefix + "_cell_" + indicatorId;
    }

    private void addStartTag(StringBuilder sb, Block block, String tag, String cssClass) {
        addStartTag(sb, block, tag, cssClass, null);
    }

    private void addStartTag(StringBuilder sb, Block block, String tag, String cssClass, String prefix) {
        sb.append("<").append(tag).append(" align='left' ");

        if (block.getDataType() == Block.BLOCK_DATA_TYPE_QUESTION && prefix != null) {
            String cellId = getQuestionCellId(block.getIndicatorId(), prefix);
            sb.append(" id='").append(cellId).append("'");
        }

        if (cssClass != null) {
            sb.append(" class='").append(cssClass).append("'");
        }

        if (block.getBlockType() == Block.BLOCK_TYPE_REGION) {
            sb.append(" colspan='").append(block.getColSpan()).append("' rowspan='").append(block.getRowSpan()).append("'");
        }
        sb.append(">");
    }
    static private final String SPECIAL_CHARS[] = {"&", "\"", "<", ">"};
    static private final String REPLACEMENT_CHARS[] = {"&amp;", "&quot;", "&lt;", "&gt;"};

    private String escapeHtmlChars(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        for (int i = 0; i < SPECIAL_CHARS.length; i++) {
            StringUtils.replace(str, SPECIAL_CHARS[i], REPLACEMENT_CHARS[i]);
        }
        return str;
    }

    private String getComponentDomName(int indicatorId, String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        return prefix + "_qst_" + indicatorId;
    }

    private void addInput(StringBuilder sb, IndicatorInfo info, boolean disabled, String prefix) {
        if (info == null) {
            return;
        }

        SurveyIndicator indicator = info.getIndicator();
        String compName = getComponentDomName(indicator.getId(), prefix);
        SurveyAnswerObject sao = info.getAnswerObject();
        switch (indicator.getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                sb.append("<textarea ").append(disabled ? " disabled='disabled'" : "").append(" name='").append(compName).append("' id='").append(compName).append("'>");
                if (sao != null) {
                    String textValue = (String) sao.getValue();
                    escapeHtmlChars(textValue);
                    sb.append(textValue);
                }

                sb.append("</textarea>");
                break;

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                sb.append("<input class='validate[custom[integer]]' type='text' size='8' name='").append(compName).append("' id='").append(compName).append("'");
                if (disabled) {
                    sb.append(" disabled='disabled'");
                }
                if (sao != null) {
                    int intValue = (Integer) sao.getValue();
                    sb.append(" value='").append(intValue).append("' ");
                }
                sb.append(">");
                break;

            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                sb.append("<input class='validate[custom[number]]' type='text' size='10' name='").append(compName).append("' id='").append(compName).append("'");
                if (disabled) {
                    sb.append(" disabled='disabled'");
                }
                if (sao != null) {
                    float floatValue = (Float) sao.getValue();
                    sb.append(" value='").append(floatValue).append("' ");
                }
                sb.append(">");
                break;

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                AnswerTypeChoiceDef def = (AnswerTypeChoiceDef) info.getAnswerTypeObject();

                for (AtcChoice ac : def.getChoices()) {
                    sb.append("<label>");
                    sb.append("<input type='radio' name='").append(compName).append("' id='").append(compName).append("'");
                    if (sao != null) {
                        long choices = (Long) sao.getValue();
                        if ((choices & ac.getMask()) != 0) {
                            // selected
                            sb.append(" checked");
                        }
                    }
                    if (disabled) {
                        sb.append(" disabled='disabled'");
                    }
                    sb.append(" value='").append(ac.getMask()).append("'>");
                    sb.append(ac.getLabel()).append("<br>");
                    sb.append("</label>");
                }
                break;

            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                def = (AnswerTypeChoiceDef) info.getAnswerTypeObject();
                for (AtcChoice ac : def.getChoices()) {
                    sb.append("<label>");
                    sb.append("<input type='checkbox' name='").append(compName).append("' id='").append(compName).append("'");
                    if (sao != null) {
                        long choices = (Long) sao.getValue();
                        if ((choices & ac.getMask()) != 0) {
                            // selected
                            sb.append(" checked");
                        }
                    }
                    if (disabled) {
                        sb.append(" disabled='disabled'");
                    }

                    sb.append(" value='").append(ac.getMask()).append("'>");
                    sb.append(ac.getLabel()).append("<br>");
                    sb.append("</label>");
                }
                break;
        }
    }

    private String generateHtml(TableServiceContext ctx, boolean inputDisabled, String prefix) {
        List<List<Block>> blockGrid = ctx.getBlockGrid();

        // determine the max number of columns
        int maxCols = 0;
        for (List<Block> rowBlocks : blockGrid) {
            int numCols = rowBlocks.size();
            if (numCols > maxCols) maxCols = numCols;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<table class='qstn-type-tbl");

        if (maxCols == 1) {
            // single column table - add a class for it so work around Firefox table display problem
            sb.append(" single-col-tbl");
        }

        sb.append("'>");
        for (List<Block> rowBlocks : blockGrid) {
            if (rowBlocks.isEmpty()) continue;
            
            sb.append("<tr>");

            for (Block block : rowBlocks) {
                switch (block.getDataType()) {
                    case Block.BLOCK_DATA_TYPE_HEADER:
                        addStartTag(sb, block, "th", "hd");
                        sb.append(block.getData());
                        sb.append("</th>");
                        break;

                    case Block.BLOCK_DATA_TYPE_LABEL:
                        addStartTag(sb, block, "td", "lbl");
                        sb.append(block.getData());
                        sb.append("</td>");
                        break;

                    default:
                        // add answer type and data
                        IndicatorInfo info = ctx.getIndicatorInfo(block.getIndicatorId());

                        // question type
                        addStartTag(sb, block, "td", getCssClass(info.getIndicator().getAnswerType()), prefix);

                        addInput(sb, info, inputDisabled, prefix);

                        sb.append("</td>");
                }
            }

            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }

    private String userMessage(int row, int col, String key, int langId, Object... arguments) {
        int argLen = (arguments != null) ? arguments.length : 0;
        Object args[] = new Object[argLen + 2];
        args[0] = row + 1;
        args[1] = col + 1;

        if (arguments != null) {
            for (int i = 0; i < argLen; i++) {
                args[i + 2] = arguments[i];
            }
        }
        String pattern = Messages.getInstance().getMessage(key, langId);
        String msg = MessageFormat.format(pattern, args);
        return msg;
    }
    private static final float ERR_FLOAT = Float.MIN_VALUE;
    private static final int ERR_INTEGER = Integer.MIN_VALUE;

    private SurveyAnswerObject createAnswerObject(short answerType, int indicatorId, Object obj) {
        SurveyAnswerObject sao = new SurveyAnswerObject();
        sao.setAnswerType(answerType);
        sao.setIndicatorId(indicatorId);
        sao.setValue(obj);

        return sao;
    }

    private Map<Integer, IndicatorInfo> getIndicatorInfoMap(int mainIndicatorId, int langId) {
        List<SurveyIndicator> componentIndicators = surveyIndicatorDao.selectComponentIndicators(mainIndicatorId);

        if (componentIndicators == null) {
            logger.debug("Table indicator " + mainIndicatorId + " has no components!");
            return null;
        }

        Map<Integer, IndicatorInfo> indicatorMap = new HashMap<Integer, IndicatorInfo>();

        for (SurveyIndicator ind : componentIndicators) {
            IndicatorInfo info = new IndicatorInfo();
            info.setIndicator(ind);
            info.setAnswerObject(null);
            indicatorMap.put(ind.getId(), info);
            Object answerTypeObj = null;

            switch (ind.getAnswerType()) {
                case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                    answerTypeObj = atFloatDao.get(ind.getAnswerTypeId());
                    break;

                case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                    answerTypeObj = atIntegerDao.get(ind.getAnswerTypeId());
                    break;

                case Constants.SURVEY_ANSWER_TYPE_TEXT:
                    answerTypeObj = atTextDao.get(ind.getAnswerTypeId());
                    break;

                case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                    answerTypeObj = atChoiceDao.getDefinition(ind.getAnswerTypeId(), langId);
                    break;

                default:
                    break;
            }
            info.setAnswerTypeObject(answerTypeObj);
        }

        // Try to set the best language for component indicators
        List<SurveyIndicatorIntl> compIntls = surveyIndicatorIntlDao.findComponentIndicatorIntl(mainIndicatorId, langId);
        if (compIntls != null) {
            for (SurveyIndicatorIntl intl : compIntls) {
                IndicatorInfo info = indicatorMap.get(intl.getSurveyIndicatorId());
                if (info == null) continue;
                SurveyIndicator ind = info.getIndicator();
                ind.setTip(intl.getTip());
                ind.setQuestion(intl.getQuestion());
            }
        }

        return indicatorMap;
    }

    /*
     * Save user entered answers to the DB.
     * If there is error, return error and no data is saved;
     * If no error, return null.
     */
    public SurveyAnswerSubmitView saveComponentAnswers(HttpServletRequest request, int mainAnswerId, int horseId, int mainSurveyQuestionId, int userId, int langId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        AnswerValidationResult result = validateTableAnswers(request, mainSurveyQuestionId, langId, "main");

        if (result.getError() != null) {
            view.setSucceed(false);
            view.setErrorMsg(result.getError());
            view.setErrorField(result.getErrorField());
            return view;
        }

        // Validated - save the answers to DB
        for (IndicatorInfo info : result.getIndicatorMap().values()) {
            int componentIndicatorId = info.getIndicator().getId();
            SurveyAnswerComponent sac = sacDao.selectAnswerComponent(mainAnswerId, componentIndicatorId);
            if (info.getAnswerObject() == null) {
                // The user didn't provide answer - remove existing SAC if any
                deleteSurveyAnswerComponent(info, sac);
                continue;
            }

            if (sac == null) {
                // create a new answer object
                int answerObjId = createAnswerObject(info);

                // create a new SAC
                sac = new SurveyAnswerComponent();
                sac.setAnswerTime(new Date());
                sac.setAnswerUserId(userId);
                sac.setComponentIndicatorId(componentIndicatorId);
                sac.setSurveyAnswerId(mainAnswerId);
                sac.setAnswerObjectId(answerObjId);
                sacDao.create(sac);
            } else {
                // the answer component already exists
                int answerObjId = updateAnswerObject(info, sac.getAnswerObjectId());
                if (answerObjId != sac.getAnswerObjectId()) {
                    sac.setAnswerObjectId(answerObjId);
                }
                sac.setAnswerTime(new Date());
                sac.setAnswerUserId(userId);
                sacDao.update(sac);
            }
        }

        saDao.updateCompletedStatus(mainAnswerId, result.getAnswerCount(), result.getQuestionCount());

        view.setSucceed(true);

        return view;
    }


    /*
     * Save user entered peer review table answers to the DB.
     * Only used when the peer review opinion is DISAGREE.
     * If there is error, return error and no data is saved;
     * If no error, return null.
     */
    public SurveyAnswerSubmitView savePeerReviewComponentAnswers(HttpServletRequest request, int surveyPeerReviewId, int mainSurveyQuestionId, int userId, int langId) {
        SurveyAnswerSubmitView view = new SurveyAnswerSubmitView();
        AnswerValidationResult result = validateTableAnswers(request, mainSurveyQuestionId, langId, "pr" + surveyPeerReviewId);

        if (result.getError() != null) {
            view.setSucceed(false);
            view.setErrorMsg(result.getError());
            view.setErrorField(result.getErrorField());
            return view;
        }

        // Validated - save the answers to DB
        for (Object value : result.getIndicatorMap().values()) {
            IndicatorInfo info = (IndicatorInfo) value;
            int componentIndicatorId = info.getIndicator().getId();
            SprComponent comp = sprComponentDao.selectPeerReviewComponent(surveyPeerReviewId, componentIndicatorId);
            if (info.getAnswerObject() == null) {
                // remove existing SAC if any
                deleteSurveyPeerReviewComponent(info, comp);
                continue;
            }

            if (comp == null) {
                // create a new answer object
                int answerObjId = createAnswerObject(info);

                // create a new SAC
                comp = new SprComponent();
                comp.setComponentIndicatorId(componentIndicatorId);
                comp.setSurveyPeerReviewId(surveyPeerReviewId);
                comp.setAnswerObjectId(answerObjId);
                sprComponentDao.create(comp);
            } else {
                // the component already exists
                int answerObjId = updateAnswerObject(info, comp.getAnswerObjectId());
                if (answerObjId != comp.getAnswerObjectId()) {
                    comp.setAnswerObjectId(answerObjId);
                    sprComponentDao.update(comp);
                }
            }
        }
        view.setSucceed(true);

        return view;
    }

    private AnswerValidationResult validateTableAnswers(HttpServletRequest request, int mainSurveyQuestionId, int langId, String prefix) {
        AnswerValidationResult result = new AnswerValidationResult();
        SurveyIndicator mainIndicator = surveyIndicatorDao.selectSurveyIndicatorByQuestionId(mainSurveyQuestionId);
        AnswerTypeTable tableDef = atTableDao.get(mainIndicator.getAnswerTypeId());
        List<List<Block>> blockGrid = Block.fromJson(tableDef.getData());

        // get all component indicators based on mainSurveyQuestionId
        Map<Integer, IndicatorInfo> indicatorMap = getIndicatorInfoMap(mainIndicator.getId(), langId);
        result.setIndicatorMap(indicatorMap);

        // validate the answers
        int questionCount = 0;
        int answerCount = 0;

        for (List<Block> bl : blockGrid) {
            for (Block block : bl) {
                if (block.getDataType() != Block.BLOCK_DATA_TYPE_QUESTION) {
                    continue;
                }

                questionCount++;
                int row = block.getRowNumber();
                int col = block.getColNumber();
                int indicatorId = block.getIndicatorId();
                IndicatorInfo info = indicatorMap.get(indicatorId);

                if (info == null) {
                    logger.error("Indicator " + indicatorId + " in table cell " + row + "." + col
                            + " is not a component indicator of main indicator " + mainIndicator.getId());
                    continue;
                }

                String compName = getComponentDomName(indicatorId, prefix);

                short answerType = info.getIndicator().getAnswerType();
                switch (answerType) {
                    case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                        AnswerTypeFloat atf = (AnswerTypeFloat) info.getAnswerTypeObject();
                        String parm = request.getParameter(compName);
                        if (!StringUtils.isEmpty(parm)) {
                            float vf = com.ocs.util.StringUtils.str2float(parm, ERR_FLOAT);
                            if (vf == ERR_FLOAT) {
                                result.setError(userMessage(row, col, Messages.KEY_ERR_INVALID_VALUE, langId));
                                result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                return result;
                            } else if (vf < atf.getMinValue() || vf > atf.getMaxValue()) {
                                result.setError(userMessage(row, col, Messages.KEY_ERR_VALUE_OUT_OF_BOUND, langId, "" + atf.getMinValue(), "" + atf.getMaxValue()));
                                result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                return result;
                            } else {
                                info.setAnswerObject(createAnswerObject(answerType, indicatorId, (Float) vf));
                                answerCount++;
                            }
                        }
                        break;

                    case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                        AnswerTypeInteger ati = (AnswerTypeInteger) info.getAnswerTypeObject();
                        parm = request.getParameter(compName);
                        if (!StringUtils.isEmpty(parm)) {
                            int vi = com.ocs.util.StringUtils.str2int(parm, ERR_INTEGER);
                            if (vi == ERR_INTEGER) {
                                result.setError(userMessage(row, col, Messages.KEY_ERR_INVALID_VALUE, langId));
                                result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                return result;
                            } else if (vi < ati.getMinValue() || vi > ati.getMaxValue()) {
                                result.setError(userMessage(row, col, Messages.KEY_ERR_VALUE_OUT_OF_BOUND, langId, "" + ati.getMinValue(), "" + ati.getMaxValue()));
                                result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                return result;
                            } else {
                                info.setAnswerObject(createAnswerObject(answerType, indicatorId, (Integer) vi));
                                answerCount++;
                            }
                        }
                        break;

                    case Constants.SURVEY_ANSWER_TYPE_TEXT:
                        AnswerTypeText att = (AnswerTypeText) info.getAnswerTypeObject();
                        parm = request.getParameter(compName);
                        if (parm != null) {
                            parm = parm.trim();
                        }
                        if (!StringUtils.isEmpty(parm)) {
                            if (parm.length() < att.getMinChars()) {
                                result.setError(userMessage(row, col, Messages.KEY_ERR_NOT_ENOUGH_TEXT_DATA, langId, "" + att.getMinChars()));
                                result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                return result;
                            } else if (parm.length() > att.getMaxChars()) {
                                result.setError(userMessage(row, col, Messages.KEY_ERR_TOO_MUCH_TEXT_DATA, langId, "" + att.getMaxChars()));
                                result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                return result;
                            } else {
                                info.setAnswerObject(createAnswerObject(answerType, indicatorId, parm));
                                answerCount++;
                            }
                        }
                        break;

                    case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                        String selection = request.getParameter(compName);
                        if (selection != null) {
                            long mask = StringUtils.str2long(selection, ERR_INTEGER);
                            if (mask <= 0) {
                                result.setError(userMessage(row, col, Messages.KEY_ERR_INVALID_VALUE, langId));
                                result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                return result;
                            }
                            info.setAnswerObject(createAnswerObject(answerType, indicatorId, (Long) mask));
                            answerCount++;
                        }

                        break;

                    case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                        //AnswerTypeChoiceDef atcd = (AnswerTypeChoiceDef) info.getAnswerTypeObject();
                        String selections[] = request.getParameterValues(compName + "[]");
                        long choices = 0;

                        if (selections != null) {
                            for (String sel : selections) {
                                long mask = StringUtils.str2long(sel, ERR_INTEGER);
                                if (mask <= 0) {
                                    result.setError(userMessage(row, col, Messages.KEY_ERR_INVALID_VALUE, langId));
                                    result.setErrorField(getQuestionCellId(indicatorId, prefix));
                                    return result;
                                }
                                choices |= mask;
                            }
                        }
                        info.setAnswerObject(createAnswerObject(answerType, indicatorId, (Long) choices));
                        answerCount++;
                        break;
                    default:
                        break;
                }

            }
        }

        result.setAnswerCount(answerCount);
        result.setQuestionCount(questionCount);
        return result;
    }

    private int createAnswerObject(IndicatorInfo info) {
        SurveyAnswerObject sao = info.getAnswerObject();
        switch (info.getIndicator().getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                AnswerObjectFloat aof = new AnswerObjectFloat();
                aof.setValue((Float) sao.getValue());
                aof = aoFloatDao.create(aof);
                return aof.getId();

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                AnswerObjectInteger aoi = new AnswerObjectInteger();
                aoi.setValue((Integer) sao.getValue());
                aoi = aoIntegerDao.create(aoi);
                return aoi.getId();

            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                AnswerObjectText aot = new AnswerObjectText();
                aot.setValue((String) sao.getValue());
                aot = aoTextDao.create(aot);
                return aot.getId();

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                AnswerObjectChoice aoc = new AnswerObjectChoice();
                aoc.setChoices((Long) sao.getValue());
                aoc = aoChoiceDao.create(aoc);
                return aoc.getId();

            default:
                return 0;
        }
    }

    private int updateAnswerObject(IndicatorInfo info, int answerObjId) {
        SurveyAnswerObject sao = info.getAnswerObject();
        switch (info.getIndicator().getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                AnswerObjectFloat aof = aoFloatDao.get(answerObjId);
                if (aof == null) {
                    return createAnswerObject(info);
                }
                aof.setValue((Float) sao.getValue());
                aoFloatDao.update(aof);
                return aof.getId();

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                AnswerObjectInteger aoi = aoIntegerDao.get(answerObjId);
                if (aoi == null) {
                    return createAnswerObject(info);
                }
                aoi.setValue((Integer) sao.getValue());
                aoIntegerDao.update(aoi);
                return aoi.getId();

            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                AnswerObjectText aot = aoTextDao.get(answerObjId);
                if (aot == null) {
                    return createAnswerObject(info);
                }
                aot.setValue((String) sao.getValue());
                aoTextDao.update(aot);
                return aot.getId();

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                AnswerObjectChoice aoc = aoChoiceDao.get(answerObjId);
                if (aoc == null) {
                    return createAnswerObject(info);
                }
                aoc.setChoices((Long) sao.getValue());
                aoChoiceDao.update(aoc);
                return aoc.getId();

            default:
                return 0;
        }
    }

    private void deleteAnswerObject(short type, int answerObjectId) {
        switch (type) {
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                aoFloatDao.delete(answerObjectId);
                return;

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                aoIntegerDao.delete(answerObjectId);
                return;

            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                aoTextDao.delete(answerObjectId);
                return;

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                aoChoiceDao.delete(answerObjectId);
        }
    }

    private void deleteSurveyAnswerComponent(IndicatorInfo info, SurveyAnswerComponent sac) {
        if (sac == null) {
            return;
        }
        sacDao.delete(sac.getId());
        deleteAnswerObject(info.getIndicator().getAnswerType(), sac.getAnswerObjectId());
    }

    private void deleteSurveyPeerReviewComponent(IndicatorInfo info, SprComponent comp) {
        if (comp == null) {
            return;
        }
        sprComponentDao.delete(comp.getId());
        deleteAnswerObject(info.getIndicator().getAnswerType(), comp.getAnswerObjectId());
    }

    private String getCssClass(int answerType) {
        String cssClass = null;
        switch (answerType) {
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                cssClass = "float";
                break;

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                cssClass = "int";
                break;

            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                cssClass = "txt";
                break;

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                cssClass = "s-choice";
                break;
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                cssClass = "m-choice";
                break;
            default:
                break;
        }
        return cssClass;
    }


    private String generateExcel(OutputStream output, TableServiceContext ctx, int anchorObjId, AnswerObjectRetriever retriever) {
        List<SurveyAnswerObject> answerObjs = retriever.getAnswerObjects(anchorObjId, answerObjDao, ctx.getMainSurveyQuestionId());

        if (answerObjs != null) {
            for (SurveyAnswerObject obj : answerObjs) {
                ctx.setAnswerObject(obj);
            }
        }
        return generateExcel(output, ctx);
    }



    public String generateExcel(OutputStream output, TableServiceContext ctx, int horseId) {
        return generateExcel(output, ctx, horseId, new HorseAnswerObjectRetriever());
    }


    public TableServiceContext createTableServiceContext(int mainSurveyQuestionId, int langId) {
        TableServiceContext.setDAOs(surveyService, atTableDao, atIntegerDao, atFloatDao, atChoiceDao, atTextDao, surveyIndicatorDao, surveyIndicatorIntlDao);
        TableServiceContext ctx = new TableServiceContext(mainSurveyQuestionId, langId);
        if (ctx.getError() != null) return null;
        return ctx;
    }


    private String generateExcel(OutputStream output, TableServiceContext ctx) {
        List<List<Block>> blockGrid = ctx.getBlockGrid();
        List<List<ExcelCell>> rows = new ArrayList<List<ExcelCell>>();
       
        for (List<Block> rowBlocks : blockGrid) {
            if (rowBlocks.isEmpty()) continue;

            List<ExcelCell> row = new ArrayList<ExcelCell>();
            rows.add(row);
            for (Block block : rowBlocks) {
                ExcelCell cell = new ExcelCell();
                row.add(cell);
                cell.setRow(block.getRowNumber());
                cell.setCol(block.getColNumber());

                cell.setColSpan(1);
                cell.setRowSpan(1);

                if (block.getBlockType() == Block.BLOCK_TYPE_REGION) {
                    cell.setColSpan(block.getColSpan());
                    cell.setRowSpan(block.getRowSpan());
                }

                switch (block.getDataType()) {
                    case Block.BLOCK_DATA_TYPE_HEADER:                    
                    case Block.BLOCK_DATA_TYPE_LABEL:                       
                        cell.setData(block.getData());
                        break;

                    default:
                        // add answer type and data
                        IndicatorInfo info = ctx.getIndicatorInfo(block.getIndicatorId());
                        cell.setData(getAnswerText(info));
                }
            }
        }

        ExcelWriter writer = new ExcelWriter(output);

        try {
            writer.writeCells(rows);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            logger.error("Failed to create Excel workbook: " + ex);
            return "Can't create Excel workbook";
        }

        return null;
    }


    private String getAnswerText(IndicatorInfo info) {
        if (info == null) return "";

        SurveyIndicator indicator = info.getIndicator();
        SurveyAnswerObject sao = info.getAnswerObject();
        if (sao == null) return "";

        switch (indicator.getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                return (String) sao.getValue();

            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                int intValue = (Integer) sao.getValue();
                return ""+intValue;

            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                float floatValue = (Float) sao.getValue();
                return ""+floatValue;

            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                AnswerTypeChoiceDef def = (AnswerTypeChoiceDef) info.getAnswerTypeObject();
                for (AtcChoice ac : def.getChoices()) {
                    long choices = (Long) sao.getValue();
                    if ((choices & ac.getMask()) != 0) return ac.getLabel();
                }
                return "";  // none selected

            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                StringBuilder sb = new StringBuilder();
                def = (AnswerTypeChoiceDef) info.getAnswerTypeObject();
                boolean isFirst = true;
                for (AtcChoice ac : def.getChoices()) {
                    long choices = (Long) sao.getValue();
                    if ((choices & ac.getMask()) != 0) {
                        if (!isFirst) sb.append("; ");
                        sb.append(ac.getLabel());
                        isFirst = false;
                    }
                }
                return sb.toString();
        }

        return "";  // not supported type
    }



    @Autowired
    public void setSurveyIndicatorDao(SurveyIndicatorDAO surveyIndicatorDao) {
        this.surveyIndicatorDao = surveyIndicatorDao;
    }

    @Autowired
    public void setAtTableDao(AnswerTypeTableDAO atTableDao) {
        this.atTableDao = atTableDao;
    }

    @Autowired
    public void setAtIntegerDao(AnswerTypeIntegerDAO atIntegerDao) {
        this.atIntegerDao = atIntegerDao;
    }

    @Autowired
    public void setAtFloatDao(AnswerTypeFloatDAO atFloatDao) {
        this.atFloatDao = atFloatDao;
    }

    @Autowired
    public void setAtChoiceDao(AnswerTypeChoiceDAO atChoiceDao) {
        this.atChoiceDao = atChoiceDao;
    }

    @Autowired
    public void setAtTextDao(AnswerTypeTextDAO atTextDao) {
        this.atTextDao = atTextDao;
    }

    @Autowired
    public void setAoIntegerDao(AnswerObjectIntegerDAO aoIntegerDao) {
        this.aoIntegerDao = aoIntegerDao;
    }

    @Autowired
    public void setAoFloatDao(AnswerObjectFloatDAO aoFloatDao) {
        this.aoFloatDao = aoFloatDao;
    }

    @Autowired
    public void setAoChoiceDao(AnswerObjectChoiceDAO aoChoiceDao) {
        this.aoChoiceDao = aoChoiceDao;
    }

    @Autowired
    public void setAoTextDao(AnswerObjectTextDAO aoTextDao) {
        this.aoTextDao = aoTextDao;
    }

    @Autowired
    public void setSacDao(SurveyAnswerComponentDAO sacDao) {
        this.sacDao = sacDao;
    }

    @Autowired
    public void setSaDao(SurveyAnswerDAO saDao) {
        this.saDao = saDao;
    }

    @Autowired
    public void setAnswerObjDao(AnswerObjectDAO answerObjDao) {
        this.answerObjDao = answerObjDao;
    }

    @Autowired
    public void setSprComponentDao(SprComponentDAO sprComponentDao) {
        this.sprComponentDao = sprComponentDao;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }
}
