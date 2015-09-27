/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.tool;

import java.util.HashMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.IndicatorService;
import com.ocs.indaba.aggregation.service.ProductService;
import com.ocs.indaba.aggregation.vo.QuestionDef;
import com.ocs.indaba.aggregation.vo.QuestionOption;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.SurveyAnswerAttachmentDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.po.AnswerObjectChoice;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.po.ReferenceObject;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.service.SurveyConfigService;
import com.ocs.indaba.survey.table.SurveyTableService;
import com.ocs.indaba.survey.table.TableServiceContext;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.vo.HorseInfo;
import com.ocs.indaba.vo.SurveyAnswerAttachmentVO;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 *
 * @author yc06x
 */
public class SimpleScorecardReporter extends BaseScorecardReporter {

    private static final Logger logger = Logger.getLogger(SimpleScorecardReporter.class);
    
    // Map horse id to column number
    private HashMap<Integer, Integer> horseMap = new HashMap<Integer, Integer>();
    private List<HorseInfo> horses;

    // Map question id to info
    private HashMap<Integer, QuestionInfo> qstMap = new HashMap<Integer, QuestionInfo>();
    private HashMap<Integer, List<Attachment>> saaMap = new HashMap<Integer, List<Attachment>>();

    private List<Node> surveyTreeNodes = null;
    private boolean isValid = false;

    static private HorseDAO horseDao = null;
    static private SurveyConfigService scService = null;
    static private ProductService productService = null;
    static private IndicatorService indService = null;
    static private SurveyAnswerDAO saDao = null;
    static private SurveyAnswerAttachmentDAO saaDao = null;
    static private SurveyTableService surveyTableService = null;

    private class OptionLayout {
        QuestionOption opt;
        Row row;
    }

    private class PeerReviewerLayout {
        int id;
        Row nameRow;
        Row opinionRow;
        Row suggestedScoreRow;
        Row commentRow;
        Row discussionRow;
    }

    private class QuestionInfo {
        QuestionDef def;
        Row mainRow = null;
        Row answerLabelRow = null;
        Row authorCommentRow = null;
        Row sourceDescRow = null;
        Row sourceOptionsRow = null;
        Row internalDiscussionRow = null;
        Row staffAuthorDiscussionRow = null;
        Row attachmentRow = null;
        TableServiceContext tableServiceCtx = null;  // only for table type question
        List<OptionLayout> optLayouts;
        List<PeerReviewerLayout> prLayouts;
    }


    public SimpleScorecardReporter(int productId, int includeOptions, int langId, List<HorseInfo> horses) {
        super.init(productId, includeOptions, langId);

        this.horses = horses;
       
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        if (horseDao == null) horseDao = (HorseDAO) ctx.getBean("horseDao");
        if (scService == null) scService = (SurveyConfigService) ctx.getBean("surveyConfigService");
        if (productService == null) productService = (ProductService) ctx.getBean("productPubService");
        if (indService == null) indService = (IndicatorService) ctx.getBean("indicatorService");
        if (surveyTableService == null) surveyTableService = (SurveyTableService) ctx.getBean("surveyTableService");
        if (saDao == null) saDao = (SurveyAnswerDAO) ctx.getBean("surveyAnswerDao");
        if (saaDao == null) saaDao = (SurveyAnswerAttachmentDAO) ctx.getBean("surveyAnswerAttachmentDao");

        // get all questions
        Product product = productService.getProduct(productId);

        if (product == null) {
            logger.error("Non-existent product: " + productId);
            return;
        }

        // get all question defs
        List<QuestionDef> defs = indService.getQuestionDefinitionsByLanguage(product.getProductConfigId(), langId);
        if (defs == null || defs.isEmpty()) {
            logger.error("No question definitions for product " + productId + " using Survey Config " + product.getProductConfigId());
            return;
        }

        SurveyTree tree = scService.buildTree(product.getProductConfigId());

        if (tree == null) {
            logger.error("Can't build survey tree for product: " + productId + " using Survey Config " + product.getProductConfigId());
            return;
        }

        surveyTreeNodes = tree.listNodes();

        if (surveyTreeNodes == null || surveyTreeNodes.isEmpty()) {
            logger.error("Survey tree has no nodes for product " + productId + " using Survey Config " + product.getProductConfigId());
            return;
        }

        // build question map
        for (QuestionDef def : defs) {
            QuestionInfo qi = new QuestionInfo();
            qi.def = def;

            if (def.getType() == Constants.ANSWER_TYPE_TABLE) {
                qi.tableServiceCtx = surveyTableService.createTableServiceContext(def.getQuestionId(), langId);
            }

            qstMap.put(def.getQuestionId(), qi);
        }

        // build survey answer maps
        cacheAOCObjects(answerObjectChoiceDAO.getAnswerObjectsOfProduct(productId));
        cacheAOIObjects(answerObjectIntegerDAO.getAnswerObjectsOfProduct(productId));
        cacheAOFObjects(answerObjectFloatDAO.getAnswerObjectsOfProduct(productId));
        cacheAOTObjects(answerObjectTextDAO.getAnswerObjectsOfProduct(productId));

        if (Constants.includeAttachedFiles(includeOptions)) {
            // build SA attachments
            List<SurveyAnswerAttachmentVO> saaList = saaDao.selectSurveyAnswerAttachmentVOsByProductId(productId);
            if (saaList != null) {
                for (SurveyAnswerAttachmentVO saa : saaList) {
                    List<Attachment> list = saaMap.get(saa.getSurveyAnswerId());
                    if (list == null) {
                        list = new ArrayList<Attachment>();
                        saaMap.put(saa.getSurveyAnswerId(), list);
                    }
                    Attachment a = saa.getAttachment();
                    if (a != null) list.add(a);
                }
            }
        }

        this.isValid = true;
    }



    public SimpleScorecardReporter(SimpleScorecardReporter r, List<HorseInfo> horses) {
        super.init(r);
        this.surveyTreeNodes = r.surveyTreeNodes;
        this.qstMap = r.qstMap;
        this.saaMap = r.saaMap;
        this.isValid = r.isValid;
        this.horses = horses;
    }


    public Workbook createWorkbook() {

        int cellNumOfQid = 0;
        int cellNumOfTitle = 0;
        int cellNumOfOptions = 0;
        int cellNumOfDataType = 0;
        int cellNumOfScore = 0;
        int cellNumOfCriteria = 0;
        int cellNumOfHint = 0;
        int cellNumOfElements = 0;

        Workbook workbook = super.newWorkbook();

        // create the header row
        Row headRow = sheet.createRow(0);
        Cell cell = headRow.createCell(0);
        cell.setCellValue(CELL_QUESTION_ID);

        cellNumOfTitle = headRow.getLastCellNum();
        createCell(headRow).setCellValue(CELL_TITLE);

        if (Constants.includeScoringOptions(includeOptions)) {
            cellNumOfDataType = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_DATA_TYPE);

            cellNumOfOptions = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_OPTIONS);

            cellNumOfScore = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_SCORE);

            cellNumOfCriteria = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_CRITERIA);

            cellNumOfHint = headRow.getLastCellNum();
            createCell(headRow).setCellValue(CELL_HINT);
        }

        
        cellNumOfElements = headRow.getLastCellNum();
        createCell(headRow).setCellValue(CELL_ELEMENTS);

        // get all horses
        // List<HorseInfo> horses = horseDao.selectNotCancelledHorsesByProductId(productId);

        if (horses != null && !horses.isEmpty()) {
            // create a cell for each horse
            for (HorseInfo h : horses) {
                int cellNum = headRow.getLastCellNum();
                createCell(headRow).setCellValue(h.getTargetName());
                horseMap.put(h.getId(), cellNum);
            }
        }

        Map<QuestionInfo, Integer> prCountMap = null;
        if (Constants.includePeerReviews(includeOptions)) {
            prCountMap = getPeerReviewCountMap(horses);
        }

        // set up all question rows
        for (Node node : surveyTreeNodes) {
            if (node.getType() != Node.NODE_TYPE_QUESTION) continue;
            QuestionInfo qi = qstMap.get(node.getId());
            if (qi == null) {
                logger.error("No Question Info for question " + node.getId());
                continue;
            }

            qi.mainRow = super.createRow();

            super.createCell(qi.mainRow, cellNumOfQid, qi.def.getPublicName());
            super.createCell(qi.mainRow, cellNumOfTitle, qi.def.getText());

            if (Constants.includeScoringOptions(includeOptions)) {
                cell = super.createCell(qi.mainRow, cellNumOfDataType);
                String type = null;
                switch(qi.def.getType()) {
                    case Constants.ANSWER_TYPE_SINGLE:
                        type = DATA_TYPE_SINGLE_CHOICE;
                        break;

                    case Constants.ANSWER_TYPE_MULTI:
                        type = DATA_TYPE_MULTI_CHOICE;
                        break;

                    case Constants.ANSWER_TYPE_INTEGER:
                        type = DATA_TYPE_INT;
                        break;

                   case Constants.ANSWER_TYPE_FLOAT:
                        type = DATA_TYPE_FLOAT;
                        break;

                   case Constants.ANSWER_TYPE_TEXT:
                        type = DATA_TYPE_TEXT;
                        break;

                   case Constants.ANSWER_TYPE_TABLE:
                        type = DATA_TYPE_TABLE;
                        break;
                }
                cell.setCellValue(type);

                super.createCell(qi.mainRow, cellNumOfHint, qi.def.getTip());
                super.createCell(qi.mainRow, cellNumOfCriteria, qi.def.getCriteria());

                if (qi.def.getType() == Constants.ANSWER_TYPE_SINGLE || qi.def.getType() == Constants.ANSWER_TYPE_MULTI) {
                    List<QuestionOption> opts = qi.def.getOptions();
                    if (opts != null && !opts.isEmpty()) {
                        qi.optLayouts = new ArrayList<OptionLayout>();

                        for (QuestionOption opt : opts) {
                            Row row = super.createRow();
                            super.createCell(row, cellNumOfOptions, opt.getLabel());

                            if (opt.hasUseScore()) {
                                double score = opt.getScore() / 10000.0;
                                super.createCell(row, cellNumOfScore, "" + score);
                            }

                            super.createCell(row, cellNumOfCriteria, opt.getCriteria());
                            super.createCell(row, cellNumOfHint, opt.getHint());

                            // need to remember the row for each opt of the question
                            OptionLayout optLayout = new OptionLayout();
                            optLayout.opt = opt;
                            optLayout.row = row;
                            qi.optLayouts.add(optLayout);
                        }
                    }
                }                
            }

            super.createCell(qi.mainRow, cellNumOfElements, CELL_SCORE);

            // create data element rows
            if (Constants.includeAnswerLabels(includeOptions)) {
                qi.answerLabelRow = super.createRow();
                super.createCell(qi.answerLabelRow, cellNumOfElements, CELL_NAME_ANSWER_LABEL);
            }

            if (Constants.includeAuthorComments(includeOptions)) {
                qi.authorCommentRow = super.createRow();
                super.createCell(qi.authorCommentRow, cellNumOfElements, CELL_NAME_AUTHOR_COMMENTS);
            }

            if (Constants.includeReferences(includeOptions)) {
                qi.sourceOptionsRow = super.createRow();
                super.createCell(qi.sourceOptionsRow, cellNumOfElements, CELL_NAME_SOURCE_OPTIONS);

                qi.sourceDescRow = super.createRow();
                super.createCell(qi.sourceDescRow, cellNumOfElements, CELL_NAME_SOURCE_DESC);
            }

            if (Constants.includePeerReviews(includeOptions)) {
                // need to include PR details:
                // compute the largest number of reviewers across all targets for this question
                // create rows (name, suggested score, comment, discussion) for each reviewer
                // remember these rows in the QI.

                Integer maxPrCount = prCountMap.get(qi);
                if (maxPrCount != null && maxPrCount > 0) {
                    qi.prLayouts = new ArrayList<PeerReviewerLayout>();

                    for (int i = 0; i < maxPrCount; i++) {
                        PeerReviewerLayout prLayout = new PeerReviewerLayout();
                        prLayout.id = i;

                        prLayout.nameRow = super.createRow();
                        super.createCell(prLayout.nameRow, cellNumOfElements, CELL_NAME_PR_REVIEWER);

                        prLayout.opinionRow = super.createRow();
                        super.createCell(prLayout.opinionRow, cellNumOfElements, CELL_NAME_PR_OPINION);

                        prLayout.suggestedScoreRow = super.createRow();
                        super.createCell(prLayout.suggestedScoreRow, cellNumOfElements, CELL_NAME_PR_SUGGESTED_SCORE);

                        prLayout.commentRow = super.createRow();
                        super.createCell(prLayout.commentRow, cellNumOfElements, CELL_NAME_PR_COMMENT);

                        prLayout.discussionRow = super.createRow();
                        super.createCell(prLayout.discussionRow, cellNumOfElements, CELL_NAME_PR_DISCUSSIONS);

                        qi.prLayouts.add(prLayout);
                    }
                }           
            }

            if (Constants.includeStaffReviews(includeOptions)) {
                qi.internalDiscussionRow = super.createRow();
                super.createCell(qi.internalDiscussionRow, cellNumOfElements, CELL_NAME_STAFF_REVIEWS);
            }

            if (Constants.includeDiscussions(includeOptions)) {
                qi.staffAuthorDiscussionRow = super.createRow();
                super.createCell(qi.staffAuthorDiscussionRow, cellNumOfElements, CELL_NAME_DISCUSSIONS);
            }

            if (Constants.includeAttachedFiles(includeOptions)) {
                qi.attachmentRow = super.createRow();
                super.createCell(qi.attachmentRow, cellNumOfElements, CELL_NAME_ATTACHMENTS);
            }
        }

        // process horses
        for (HorseInfo h : horses) {            
            ExportHorse(h);
        }

        return workbook;
    }


    private void processPrCount(HorseInfo horse, Map<QuestionInfo, Integer> prCountMap) {
        List<SurveyAnswer> saList = saDao.getAllSurveyAnswersByHorse(horse.getId());
        if (saList == null || saList.isEmpty()) return;

        for (SurveyAnswer sa : saList) {
            QuestionInfo qi = qstMap.get(sa.getSurveyQuestionId());

            if (qi == null) {
                logger.error("Question " + sa.getSurveyQuestionId() + " not in hash table for product " + productId);
                continue;
            }

            List<SurveyPeerReview> peerReviews = prMap.get(sa.getId());
            int count = (peerReviews == null) ? 0 : peerReviews.size();
            Integer curCount = prCountMap.get(qi);
            if (curCount == null || count > curCount) {
                prCountMap.put(qi, count);
            }
        }
    }


    private Map<QuestionInfo, Integer> getPeerReviewCountMap(List<HorseInfo>horses) {
        HashMap<QuestionInfo, Integer> prCountMap = new HashMap<QuestionInfo, Integer>();

        for (HorseInfo horse : horses) {
            processPrCount(horse, prCountMap);
        }
        
        return prCountMap;
    }


    private void ExportHorse(HorseInfo horse) {
        List<SurveyAnswer> saList = saDao.getAllSurveyAnswersByHorse(horse.getId());
        if (saList == null || saList.isEmpty()) return;

        Integer cellNum = horseMap.get(horse.getId());

        if (cellNum == null) {
            logger.error("Horse " + horse.getId() + " not in hash table for product " + productId);
            return;
        }

        // process each answer
        for (SurveyAnswer sa : saList) {
            QuestionInfo qi = qstMap.get(sa.getSurveyQuestionId());

            if (qi == null) {
                logger.error("Question " + sa.getSurveyQuestionId() + " not in hash table for product " + productId);
                continue;
            }

            String tableFileName = null;

            // Set score value
            if (qi.def.getType() == Constants.ANSWER_TYPE_SINGLE ||
                qi.def.getType() == Constants.ANSWER_TYPE_MULTI) {
                String score = getChoiceScoreText(qi.def, sa.getAnswerObjectId());
                if (score != null) {
                    super.createCell(qi.mainRow, cellNum, score);
                }
            } else if (qi.def.getType() == Constants.ANSWER_TYPE_TABLE) {
                tableFileName = createTableFile(horse, qi);
                super.createCell(qi.mainRow, cellNum, tableFileName);
            }

            // set option selections
            if ((qi.def.getType() == Constants.ANSWER_TYPE_SINGLE || qi.def.getType() == Constants.ANSWER_TYPE_MULTI) &&
                qi.optLayouts != null && !qi.optLayouts.isEmpty()) {
                for (OptionLayout optLayout : qi.optLayouts) {
                    String optStr = getSelectedOptionText(sa.getAnswerObjectId(), optLayout.opt);
                    if (optStr != null) {
                        super.createCell(optLayout.row, cellNum, optStr);
                    }
                }
            }

            if (qi.answerLabelRow != null) {
                String value = null;

                if (qi.def.getType() == Constants.ANSWER_TYPE_TABLE) {
                    value = tableFileName;
                } else {
                    Object answerObj = null;

                    switch (qi.def.getType()) {
                        case Constants.ANSWER_TYPE_SINGLE:
                        case Constants.ANSWER_TYPE_MULTI:
                            answerObj = aocMap.get(sa.getAnswerObjectId());
                            break;

                        case Constants.ANSWER_TYPE_INTEGER:
                            answerObj = aoiMap.get(sa.getAnswerObjectId());
                            break;

                        case Constants.ANSWER_TYPE_FLOAT:
                            answerObj = aofMap.get(sa.getAnswerObjectId());
                            break;

                        case Constants.ANSWER_TYPE_TEXT:
                            answerObj = aotMap.get(sa.getAnswerObjectId());
                            break;
                    }

                    // this method needs to be modified to use new format for MC
                    value = super.getAnswerString(qi.def, answerObj);
                }
                super.createCell(qi.answerLabelRow, cellNum, value);
            }

            if (qi.authorCommentRow != null) {
                Cell cell = super.createCell(qi.authorCommentRow, cellNum);
                saveCellText(horse.getTargetShortName(), cell, composeComment(sa.getAnswerUserId(), sa.getComments()), qi.def.getPublicName(), CELL_NAME_AUTHOR_COMMENTS);
            }

            if (qi.sourceOptionsRow != null) {
                if (sa.getReferenceObjectId() > 0) {
                    // ReferenceObject refObj = refObjDao.get(question.getReferenceObjectId());
                    ReferenceObject refObj = refObjMap.get(sa.getReferenceObjectId());
                    if (refObj != null) {
                        String optText = getSourceOptionText(refObj);
                        if (optText != null) {
                            super.createCell(qi.sourceOptionsRow, cellNum, optText);
                        }
                    }
                }
            }

            // Report source description
            if (qi.sourceDescRow != null) {
                if (sa.getReferenceObjectId() > 0) {
                    // ReferenceObject refObj = refObjDao.get(question.getReferenceObjectId());
                    ReferenceObject refObj = refObjMap.get(sa.getReferenceObjectId());
                    if (refObj != null) {
                        Cell cell = super.createCell(qi.sourceDescRow, cellNum);
                        saveCellText(horse.getTargetShortName(), cell, refObj.getSourceDescription(), qi.def.getPublicName(), CELL_NAME_SOURCE_DESC);
                    }
                }
            }

            // need to modify the PR section to report for multiple reviewers
            if (qi.prLayouts != null && !qi.prLayouts.isEmpty()) {
                List<SurveyPeerReview> peerReviews = prMap.get(sa.getId());
                if (peerReviews != null && !peerReviews.isEmpty()) {
                    int reviewIdx = 0;
                    for (SurveyPeerReview spr : peerReviews) {
                        PeerReviewerLayout prLayout = qi.prLayouts.get(reviewIdx);
                        reviewIdx++;

                        String userName = getUserName(spr.getReviewerUserId());
                        super.createCell(prLayout.nameRow, cellNum, userName);

                        super.createCell(prLayout.opinionRow, cellNum, getPeerReviewOpinionText(spr.getOpinion()));

                        super.createCell(prLayout.commentRow, cellNum, spr.getComments());

                        // add suggested score
                        if (spr.getOpinion() == com.ocs.indaba.common.Constants.SURVEY_PEER_DISAGREE) {
                            String answer = getAnswerString(spr.getSuggestedAnswerObjectId(), qi.def);
                            super.createCell(prLayout.suggestedScoreRow, cellNum, answer);
                        }

                        // add discussions
                        String discussions = getMessages(spr.getMsgboardId());
                        if (discussions != null) {
                            super.createCell(prLayout.discussionRow, cellNum, discussions);
                        }
                    }
                }
            }
           
            if (qi.internalDiscussionRow != null) {
                String msg = getMessages(sa.getInternalMsgboardId());
                if (msg != null) {
                    Cell cell = super.createCell(qi.internalDiscussionRow, cellNum);
                    saveCellText(horse.getTargetShortName(), cell, msg, qi.def.getPublicName(), CELL_NAME_STAFF_REVIEWS);
                }
            }

            if (qi.staffAuthorDiscussionRow != null) {
                String msg = getMessages(sa.getStaffAuthorMsgboardId());
                if (msg != null) {
                    Cell cell = super.createCell(qi.staffAuthorDiscussionRow, cellNum);
                    saveCellText(horse.getTargetShortName(), cell, msg, qi.def.getPublicName(), CELL_NAME_DISCUSSIONS);
                }
            }

            if (qi.attachmentRow != null) {
                List<Attachment> attachments = saaMap.get(sa.getId());
                String names = super.addAttachments(attachments, qi.def.getPublicName(), qi.def.getQuestionId(), horse.getId(), horse.getTargetShortName());
                Cell cell = super.createCell(qi.attachmentRow, cellNum);
                saveCellText(horse.getTargetShortName(), cell, names, qi.def.getPublicName(), CELL_NAME_ATTACHMENTS);
            }
        }
    }


    private String createTableFile(HorseInfo horse, QuestionInfo qi) {
        logger.debug("Creating table file for horse: " + horse.getTargetShortName() + " question " + qi.def.getQuestionId());

        if (qi.tableServiceCtx == null) return "ERROR: no context";

        File exportBasePath = getOrCreateExportFolderFile();
        String label = qi.def.getPublicName().trim().replaceAll("\\s+", "");
        String filename = horse.getTargetShortName() + "." + label + "." + qi.def.getQuestionId() + ".tvf.xls";
        File outputFile = new File(exportBasePath, filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            qi.tableServiceCtx.clearAnswerObjects();
            surveyTableService.generateExcel(outputStream, qi.tableServiceCtx, horse.getId());
        } catch (Exception ex) {
            logger.error("Failed to generate table data file: " + filename + " exception: " + ex);
        }
        return filename;
    }


    private String getSelectedOptionText(int answerObjectId, QuestionOption opt) {
        AnswerObjectChoice aoc = aocMap.get(answerObjectId);
        if (aoc == null) return null;

        long value = aoc.getChoices();
        long mask = opt.getMask();
        if ((value & mask) == mask) {
            double score = opt.getScore() / 10000;
            return opt.getLabel() + WORD_SEPARATOR + score;
        }
        return null;
    }


    private String getChoiceScoreText(QuestionDef def, int answerObjectId) {
        if (def.getOptions() == null) return "MissIndicatorTypeDef"+def.getIndicatorId();  // the def is bad!!!
        
        AnswerObjectChoice aoc = aocMap.get(answerObjectId);
        if (aoc == null) return "";

        long value = aoc.getChoices();
        String result = null;
        for (QuestionOption opt : def.getOptions()) {
            long mask = opt.getMask();
            if ((value & mask) == mask) {
                if (opt.hasUseScore()) {
                    double score = opt.getScore() / 10000;
                    if (result == null) {
                        result = "" + score;
                    } else {
                        result = result + WORD_SEPARATOR + score;
                    }
                } 
            }
        }
        return result;
    }


    private String getSourceOptionText(ReferenceObject refObj) {
        List<ReferenceChoice> choices = refChoiceMap.get(refObj.getReferenceId());
        if (choices == null || choices.isEmpty()) return null;

        String result = null;
        for (ReferenceChoice rc : choices) {
            if ((refObj.getChoices() & rc.getMask()) != 0) {
                if (result == null) {
                    result = rc.getLabel();
                } else {
                    result += WORD_SEPARATOR + rc.getLabel();
                }
            }
        }
        return result;
    }
}
