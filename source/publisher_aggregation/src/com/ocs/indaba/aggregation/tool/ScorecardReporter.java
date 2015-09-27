/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.PIFPersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.IndicatorService;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.HorseVO;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.QuestionDef;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.QuestionOption;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.po.AtcChoiceIntl;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ReferenceObject;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import com.ocs.indaba.util.Tree;
import com.ocs.util.StringUtils;
import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.ocs.util.StringUtils;

/**
 *
 * @author Jeff
 */
public class ScorecardReporter extends BaseScorecardReporter {

    private static final Logger log = Logger.getLogger(ScorecardReporter.class);
    
    private Row headRow = null;
    private Row overallRow = null;
    private Row legalRow = null;
    private Row implRow = null;
    private Row gapRow = null;
    private int catStartRowNum = 0;
    private int catStartCellNum = 0;
    private int curRowNum = 0;
    private int cellNumOfTitle = 0;
    private int cellNumOfOptions = 0;
    private int cellNumOfScore = 0;
    private int cellNumOfCriteria = 0;
    private int cellNumOfHint = 0;
    private int cellNumOfMedian = 0;
    private int cellNumOfElements = 0;
    private int cellNumOfTargetBegin = 0;

    static private IndicatorService indService = null;
    
    private Map<Integer, Row> rowMap = null;

    // dummy constructor needed by bean autowiring
    // public ScorecardReporter() {}

    public ScorecardReporter(int productId, int includeOptions, int langId) {
        rowMap = new HashMap<Integer, Row>();
        super.init(productId, includeOptions, langId);
        
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        if (indService == null) indService = (IndicatorService) ctx.getBean("indicatorService");
    }

    private void clear() {
        rowMap.clear();
    }

    private void initSheet() {
        headRow = sheet.createRow(0);
        catStartCellNum = (Constants.includeGlobalIntegrity(includeOptions) ? 1 : 0);
        headRow.createCell(catStartCellNum).setCellValue(CELL_CATETORY);
        //createCell(headRow).setCellValue(CELL_CATETORY);
        createCell(headRow).setCellValue(CELL_SUBCAT);
        createCell(headRow).setCellValue(CELL_QUESTION_SET);
        createCell(headRow).setCellValue(CELL_QUESTION_ID);
        cellNumOfTitle = headRow.getLastCellNum();
        createCell(headRow).setCellValue(CELL_TITLE);

        if (Constants.includeScoringOptions(includeOptions)) {
            cellNumOfOptions = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_OPTIONS);
            cellNumOfScore = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_SCORE);
            cellNumOfCriteria = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_CRITERIA);
            cellNumOfHint = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_HINT);
        }

        if (Constants.includeGlobalIntegrity(includeOptions)) {
            cellNumOfMedian = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_MEDIAN);
        }
        if (includeOptions > 0 && (includeOptions - Constants.INCLUDE_GLOBAL_INTEGRITY) != 0) {
            cellNumOfElements = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_ELEMENTS);
        }

        cellNumOfTargetBegin = headRow.getLastCellNum();
        if (Constants.includeGlobalIntegrity(includeOptions)) {
            overallRow = createRow();
            overallRow.createCell(0).setCellValue(CELL_OVERALL);

            legalRow = createRow();
            legalRow.createCell(0).setCellValue(CELL_LEGAL_FRAMEWORK);

            implRow = createRow();
            implRow.createCell(0).setCellValue(CELL_ACTUAL_IMPLEMENTATION);

            gapRow = createRow();
            gapRow.createCell(0).setCellValue(CELL_IMPLEMENTATION_GAP);
        }
        catStartRowNum = sheet.getLastRowNum();

    }

 
    public Workbook createWorkbook() {
        clear();

        File pifFile = BasePersistence.getPIFFile(productId, null);
        if (pifFile == null) {
            log.error("PIF file is not found: [" + productId + "].");
            return null;
        }
        PIFPersistence persistence = new PIFPersistence(null);
        ProductInfo prodInfo = persistence.deserializePIF(pifFile);
        if (prodInfo == null) {
            log.error("Fail to read PIF file: [" + productId + "].");
            return null;
        }


        // get the right language for text
        // replace text with right language
        // get intl resource map
        HashMap<Integer, SurveyIndicatorIntl> siiMap = new HashMap<Integer, SurveyIndicatorIntl>();
        HashMap<Integer, AtcChoiceIntl> atciMap = new HashMap<Integer, AtcChoiceIntl>();
        Map<Integer, Integer> idMap = indService.getQuestionToIndicatorIdMap(prodInfo.getSurveyConfigId());

        indService.getTextMaps(prodInfo.getSurveyConfigId(), langId, siiMap, atciMap);

        List<Tree<ScorecardBaseNode>> rootCats = prodInfo.getRootCategories();
        if (rootCats != null) {
            for (Tree<ScorecardBaseNode> subTree : rootCats) {
                translateText(subTree, siiMap, atciMap, idMap);
            }
        }

        Workbook workbook = super.newWorkbook();
        initSheet();

        curRowNum = catStartRowNum;
        handlePIFCategories(rootCats, catStartCellNum);

        handleHorses(prodInfo.getHorses());

        return workbook;
    }



    private void translateText(Tree<ScorecardBaseNode> tree, Map<Integer, SurveyIndicatorIntl> siiMap, Map<Integer, AtcChoiceIntl> atciMap, Map<Integer, Integer> idMap) {
        if (siiMap.isEmpty() && atciMap.isEmpty()) return;

        if (tree.getNode().getNodeType() == Constants.NODE_TYPE_CATEGORY) {
            List<Tree<ScorecardBaseNode>> children = tree.getChildren();
            if (children != null) {
                for (Tree<ScorecardBaseNode> child : children) {
                    translateText(child, siiMap, atciMap, idMap);
                }
            }
        } else {
            // question node
            QuestionNode qNode = (QuestionNode) tree.getNode();
            int indicatorId = idMap.get(qNode.getQuestionId());
            SurveyIndicatorIntl sii = siiMap.get(indicatorId);
            if (sii != null) {
                if (!StringUtils.isEmpty(sii.getQuestion())) qNode.setQuestionText(sii.getQuestion());
                if (!StringUtils.isEmpty(sii.getTip())) qNode.setTip(sii.getTip());
            }

            List<QuestionOption> options = qNode.getOptions();
            if (options != null) {
                for (QuestionOption opt : options) {
                    AtcChoiceIntl atci = atciMap.get(opt.getId());
                    if (atci != null) {
                        if (!StringUtils.isEmpty(atci.getCriteria())) {
                            opt.setCriteria(atci.getCriteria());
                        }
                        if (!StringUtils.isEmpty(atci.getLabel())) {
                            opt.setLabel(atci.getLabel());
                        }
                    }
                }
            }
        }
    }


    private void handlePIFCategories(List<Tree<ScorecardBaseNode>> categories, int curCellNum) {
        if (categories == null || categories.isEmpty()) {
            return;
        }

        for (Tree<ScorecardBaseNode> subTree : categories) {
            handlePIFScorecardNode(subTree, curCellNum);
        }
    }

    private void handlePIFScorecardNode(Tree<ScorecardBaseNode> tree, int curCellNum) {
        if (tree == null || tree.getNode() == null) {
            return;
        }
        ++curRowNum;
        Row row = findOrCreateRow(curRowNum);

        if (tree.getNode().getNodeType() == Constants.NODE_TYPE_CATEGORY) {
            CategoryNode catNode = (CategoryNode) tree.getNode();
            createCell(row, curCellNum).setCellValue(catNode.getLabel());
            createCell(row, cellNumOfTitle).setCellValue(catNode.getTitle());
            if (Constants.includeGlobalIntegrity(includeOptions)) {
                createCell(row, cellNumOfMedian).setCellValue(formatNum(catNode.getMedian()));
            }

            //System.out.println("Category [" + catNode.getId() + "]: " + " label=" + catNode.getLabel() + ", curRowNum: " + curRowNum + ", curCellNum: " + curCellNum);
            handlePIFCategories(tree.getChildren(), ++curCellNum);
        } else {
            //System.out.println("Question [" + ((QuestionNode) tree.getNode()).getQuestionId() + "]: curRowNum: " + curRowNum + ", curCellNum: " + curCellNum);
            QuestionNode qNode = (QuestionNode) tree.getNode();
            createCell(row, curCellNum).setCellValue(qNode.getPublicName());
            createCell(row, curCellNum + 1).setCellValue(qNode.getQuestionText());
            //createCell(row, curCellNum + 1).setCellValue("Question Text.");

            if (Constants.includeScoringOptions(includeOptions)) {
                createCell(row, cellNumOfHint).setCellValue(qNode.getTip());

                if (qNode.getQuestionType() == Constants.ANSWER_TYPE_SINGLE || qNode.getQuestionType() == Constants.ANSWER_TYPE_MULTI) { // for choice answers
                    List<QuestionOption> qOptions = qNode.getOptions();
                    if (qOptions != null && !qOptions.isEmpty()) {
                        for (QuestionOption opt : qOptions) {
                            ++curRowNum;
                            createCell(findOrCreateRow(curRowNum), cellNumOfOptions).setCellValue(opt.getLabel());
                            if (opt.hasUseScore()) {
                              createCell(findOrCreateRow(curRowNum), cellNumOfScore).setCellValue(MessageFormat.format("{0,number,#}", opt.getScore()));
                            } else {
                              createCell(findOrCreateRow(curRowNum), cellNumOfScore).setCellValue("");
                            }
                            createCell(findOrCreateRow(curRowNum), cellNumOfCriteria).setCellValue(opt.getCriteria());
                        }
                    }
                } else { // for non choice answers
                    createCell(row, cellNumOfCriteria).setCellValue(qNode.getCriteria());
                }
            }

            if (Constants.includeAnswerLabels(includeOptions)) {
                ++curRowNum;
                createCell(findOrCreateRow(curRowNum), cellNumOfElements).setCellValue(CELL_NAME_ANSWER_LABEL);
            }
            if (Constants.includeAuthorComments(includeOptions)) {
                ++curRowNum;
                createCell(findOrCreateRow(curRowNum), cellNumOfElements).setCellValue(CELL_NAME_AUTHOR_COMMENTS);
            }
            if (Constants.includeReferences(includeOptions)) {
                ++curRowNum;
                createCell(findOrCreateRow(curRowNum), cellNumOfElements).setCellValue(CELL_NAME_SOURCE_DESC);
            }
            if (Constants.includePeerReviews(includeOptions)) {
                ++curRowNum;
                createCell(findOrCreateRow(curRowNum), cellNumOfElements).setCellValue(CELL_NAME_PEER_REVIEWS);
            }
            if (Constants.includeStaffReviews(includeOptions)) {
                ++curRowNum;
                createCell(findOrCreateRow(curRowNum), cellNumOfElements).setCellValue(CELL_NAME_STAFF_REVIEWS);
            }
            if (Constants.includeDiscussions(includeOptions)) {
                ++curRowNum;
                createCell(findOrCreateRow(curRowNum), cellNumOfElements).setCellValue(CELL_NAME_DISCUSSIONS);
            }
            if (Constants.includeAttachedFiles(includeOptions)) {
                ++curRowNum;
                createCell(findOrCreateRow(curRowNum), cellNumOfElements).setCellValue(CELL_NAME_ATTACHMENTS);
            }
        }
    }

    private void handleHorses(List<HorseVO> horses) {
        if (horses == null && horses.isEmpty()) {
            return;
        }
        SRFPersistence srfPersistence = new SRFPersistence(null);
        int curCellNum = cellNumOfTargetBegin;
        for (HorseVO horse : horses) {
            ScorecardInfo scorecard = srfPersistence.deserializeSRF(BasePersistence.getSRFFile(horse.getHorseId(), null));
            if (scorecard == null) {
                continue;
            }
            log.info("Export horse[" + horse.getHorseId() + "] for target: '" + scorecard.getTargetName() + "'.");

            

            /**
            if (Constants.includeStaffReviews(includeOptions) || Constants.includeDiscussions(includeOptions)) {
                msgMap = scorecardSrvc.getAllMessagesByHorseId(horse.getHorseId());
            }
            ***/
            curRowNum = catStartRowNum;
            createCell(headRow, curCellNum).setCellValue(scorecard.getTargetName());
            if (Constants.includeGlobalIntegrity(includeOptions)) {
                createCell(overallRow, curCellNum).setCellValue(formatNum(scorecard.getOverall()));
                if (scorecard.getLegalFrameworkCount() > 0) {
                    createCell(legalRow, curCellNum).setCellValue(formatNum(scorecard.getLegalFramework()));
                } else {
                    createCell(legalRow, curCellNum).setCellValue(NOT_AVAILABLE);
                }
                if (scorecard.getImplementationCount() > 0) {
                    createCell(implRow, curCellNum).setCellValue(formatNum(scorecard.getImplementation()));
                } else {
                    createCell(implRow, curCellNum).setCellValue(NOT_AVAILABLE);
                }
                createCell(gapRow, curCellNum).setCellValue(formatNum(scorecard.getLegalFramework() - scorecard.getImplementation()));
            }
            handleSRFCategories(scorecard.getHorseId(), scorecard.getTargetName(), scorecard.getTargetShortName(), scorecard.getRootCategories(), curCellNum);

            ++curCellNum;
        }
    }

    private void handleSRFCategories(int horseId, String targetName, String targetShortName, List<Tree<ScorecardBaseNode>> categories, int curCellNum) {
        if (categories == null || categories.isEmpty()) {
            return;
        }

        for (Tree<ScorecardBaseNode> subTree : categories) {
            handleSRFScorecardNode(horseId, targetName, targetShortName, subTree, curCellNum);
        }
    }

    private void handleSRFScorecardNode(int horseId, String targetName, String targetShortName, Tree<ScorecardBaseNode> tree, int curCellNum) {
        if (tree == null || tree.getNode() == null) {
            return;
        }
        ++curRowNum;
        Row row = findOrCreateRow(curRowNum);
        if (tree.getNode().getNodeType() == Constants.NODE_TYPE_CATEGORY) {
            CategoryNode catNode = (CategoryNode) tree.getNode();
            if (Constants.includeGlobalIntegrity(includeOptions) || tree.getParent() != null) {
                createCell(row, curCellNum).setCellValue(formatNum(catNode.getMean()));
            }
            //System.out.println("Category [" + catNode.getId() + "]: " + " label=" + catNode.getLabel() + ", curRowNum: " + curRowNum + ", curCellNum: " + curCellNum);
            handleSRFCategories(horseId, targetName, targetShortName, tree.getChildren(), curCellNum);
        } else {
            QuestionNode question = (QuestionNode) tree.getNode();
            //System.out.println("Question [" + ((QuestionNode) tree.getNode()).getQuestionId() + "]: curRowNum: " + curRowNum + ", curCellNum: " + curCellNum);
            if (Constants.ANSWER_TYPE_SINGLE == question.getQuestionType() && question.isCompleted()) {

                // Get the selected choice
                List<QuestionOption> options = question.getOptions();
                boolean has_score = false;
                if (options != null && !options.isEmpty()) {
                    for (QuestionOption option : options) {
                        if (question.getChoiceId() == option.getId()) {
                            has_score = option.hasUseScore();
                            break;
                        }
                    }
                }

                if (has_score) {
                    createCell(row, curCellNum).setCellValue(formatNum(question.getScore()));
                }
            }
            //createCell(row, curCellNum + 1).setCellValue(((QuestionNode) tree.getNode()).getQuestionText());
            if (Constants.includeScoringOptions(includeOptions)) {
                switch (question.getQuestionType()) {
                    case Constants.ANSWER_TYPE_SINGLE:
                    case Constants.ANSWER_TYPE_MULTI:
                        List<QuestionOption> options = question.getOptions();
                        if (options != null && !options.isEmpty()) {
                            curRowNum += options.size();
                        }
                        break;
                    case Constants.ANSWER_TYPE_INTEGER:
                    case Constants.ANSWER_TYPE_FLOAT:
                    case Constants.ANSWER_TYPE_TEXT:
                    default: {
                        // ++curRowNum;
                    }
                }

            }
            //////////////////////////////////////////////
            // Answer Labels
            //////////////////////////////////////////////
            if (Constants.includeAnswerLabels(includeOptions)) {
                ++curRowNum;
                List<QuestionOption> options = question.getOptions();
                StringBuilder sb = new StringBuilder();
                switch (question.getQuestionType()) {
                    case Constants.ANSWER_TYPE_SINGLE: {
                        if (options != null && !options.isEmpty()) {
                            for (QuestionOption option : options) {
                                if (question.getChoiceId() == option.getId()) {
                                    sb.append(option.getLabel());
                                    break;
                                }
                            }
                        }
                    }
                    break;
                    case Constants.ANSWER_TYPE_MULTI: {
                        if (options != null && !options.isEmpty()) {
                            for (QuestionOption option : options) {
                                if ((question.getChoices() & option.getMask()) == option.getMask()) {
                                    sb.append(option.getLabel()).append(',');
                                }
                            }
                        }
                    }
                    break;
                    case Constants.ANSWER_TYPE_INTEGER:
                    case Constants.ANSWER_TYPE_FLOAT:
                    case Constants.ANSWER_TYPE_TEXT:
                    default: {
                        String value = question.getInputValue();
                        sb.append(StringUtils.isEmpty(value) ? "" : value);
                    }
                    break;
                }
                if (sb.toString().endsWith(",")) {
                    sb.setLength(sb.length() - 1);
                }
                saveCellText(targetShortName, createCell(findOrCreateRow(curRowNum), curCellNum), sb.toString(), question.getPublicName(), CELL_NAME_ANSWER_LABEL);
            }
            //////////////////////////////////////////////
            // Author Comments
            //////////////////////////////////////////////
            if (Constants.includeAuthorComments(includeOptions)) {
                ++curRowNum;
                //log.debug(">>> " + question.getQuestionId() + ": " + question.getComments());
                if (!StringUtils.isEmpty(question.getComments())) {
                    saveCellText(targetShortName, createCell(findOrCreateRow(curRowNum), curCellNum), composeComment(question.getAnswerUserId(), question.getComments()), question.getPublicName(), CELL_NAME_AUTHOR_COMMENTS);
                }
            }
            //////////////////////////////////////////////
            // References
            //////////////////////////////////////////////
            if (Constants.includeReferences(includeOptions)) {
                ++curRowNum;
                if (question.getReferenceObjectId() > 0) {
                    // ReferenceObject refObj = refObjDao.get(question.getReferenceObjectId());
                    ReferenceObject refObj = refObjMap.get(question.getReferenceObjectId());
                    if (refObj != null && !refObj.getSourceDescription().isEmpty()) {
                        saveCellText(targetShortName, createCell(findOrCreateRow(curRowNum), curCellNum), refObj.getSourceDescription(), question.getPublicName(), CELL_NAME_SOURCE_DESC);
                    }
                }
            }
            //////////////////////////////////////////////
            // Peer Reviews
            //////////////////////////////////////////////
            if (Constants.includePeerReviews(includeOptions)) {
                ++curRowNum;
                String prComments = getPeerReviewText(new QuestionDef(question.getId(), question.getQuestionId(), question.getQuestionType(),
                                    question.getQuestionName(), question.getPublicName(),
                                    question.getQuestionText(), question.getTip(), question.getCriteria(), question.getOptions()),
                                    question.getAnswerId());

                if (prComments != null) {
                    saveCellText(targetShortName, createCell(findOrCreateRow(curRowNum), curCellNum), prComments, question.getPublicName(), CELL_NAME_PEER_REVIEWS);
                }
            }
            //////////////////////////////////////////////
            // Staff Reviews
            //////////////////////////////////////////////
            if (Constants.includeStaffReviews(includeOptions)) {
                ++curRowNum;
                String msgs = getMessages(question.getInternalMsgboardId());
                if (msgs != null) {
                    saveCellText(targetShortName, createCell(findOrCreateRow(curRowNum), curCellNum), msgs, question.getPublicName(), CELL_NAME_STAFF_REVIEWS);
                }
            }
            //////////////////////////////////////////////
            // Staff/Author Discussions
            //////////////////////////////////////////////
            if (Constants.includeDiscussions(includeOptions)) {
                ++curRowNum;
                String msgs = getMessages(question.getStaffAuthorMsgboardId());
                if (msgs != null) {
                    saveCellText(targetShortName, createCell(findOrCreateRow(curRowNum), curCellNum), msgs, question.getPublicName(), CELL_NAME_DISCUSSIONS);
                }
            }
            
            if (Constants.includeAttachedFiles(includeOptions)) {
                ++curRowNum;
                List<Attachment> attachments = question.getAttachements();
                String names = super.addAttachments(attachments, question.getPublicName(), question.getId(), horseId, targetShortName);

                if (names != null) {
                    saveCellText(targetShortName, createCell(findOrCreateRow(curRowNum), curCellNum), names, question.getPublicName(), CELL_NAME_ATTACHMENTS);
                }
            }
        }
    }

    private Row findOrCreateRow(int rowRum) {
        Row row = rowMap.get(rowRum);
        if (row == null) {
            row = createRow(rowRum);
            rowMap.put(rowRum, row);
        }
        return row;
    }

}
