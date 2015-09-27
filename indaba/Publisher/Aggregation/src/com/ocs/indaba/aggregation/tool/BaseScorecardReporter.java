/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.ScorecardService;
import com.ocs.indaba.aggregation.vo.*;
import com.ocs.indaba.builder.dao.UserDAO;
import com.ocs.indaba.dao.AnswerObjectChoiceDAO;
import com.ocs.indaba.dao.AnswerObjectFloatDAO;
import com.ocs.indaba.dao.AnswerObjectIntegerDAO;
import com.ocs.indaba.dao.AnswerObjectTextDAO;
import com.ocs.indaba.dao.ReferenceChoiceDAO;
import com.ocs.indaba.po.AnswerObjectChoice;
import com.ocs.indaba.po.AnswerObjectFloat;
import com.ocs.indaba.po.AnswerObjectInteger;
import com.ocs.indaba.po.AnswerObjectText;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.Message;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.po.ReferenceObject;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.util.AttachmentFilePathUtil;
import com.ocs.indaba.util.FileUtil;
import com.ocs.indaba.vo.UserView;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.ocs.util.StringUtils;


/**
 *
 * @author yc06x
 */
public abstract class BaseScorecardReporter {

    private static final Logger log = Logger.getLogger(BaseScorecardReporter.class);

    protected static final int MAX_CELL_TEXT_LENGTH = 30 * 1024;
    protected static final String WORD_SEPARATOR = " | ";

    protected static final NumberFormat NUMBER_FORMAT = new DecimalFormat("##0.000000");
    protected static final String ATTACH_ATTACHED_FOLDER = "attachments";
    protected static final String ATTACH_FILENAME_PATTERN = "{0}-{1}-{2,number,#}-{3,number,#}.{4}";
    protected static final String CELL_CATETORY = "Category";
    protected static final String CELL_SUBCAT = "Subcat";
    protected static final String CELL_QUESTION_SET = "Question Set";
    protected static final String CELL_QUESTION_ID = "QID";
    protected static final String CELL_TITLE = "Questions";
    protected static final String CELL_OPTIONS = "Label Options";
    protected static final String CELL_SCORE = "Score";
    protected static final String CELL_CRITERIA = "Criteria";
    protected static final String CELL_HINT = "Tips";
    protected static final String CELL_MEDIAN = "Median";
    protected static final String CELL_ELEMENTS = "Data Elements";
    protected static final String CELL_OVERALL = "Overall";
    protected static final String CELL_LEGAL_FRAMEWORK = "Legal Framework";
    protected static final String CELL_ACTUAL_IMPLEMENTATION = "Actual Implementation";
    protected static final String CELL_IMPLEMENTATION_GAP = "Implementation Gap";
    protected static final String CELL_NAME_ANSWER_LABEL = "Answer Label";
    protected static final String CELL_NAME_AUTHOR_COMMENTS = "Author Comments";
    protected static final String CELL_NAME_SOURCE_DESC = "Source Description";
    protected static final String CELL_NAME_SOURCE_OPTIONS = "Source Options";
    protected static final String CELL_NAME_PEER_REVIEWS = "Reviews (Comments)";
    protected static final String CELL_NAME_STAFF_REVIEWS = "Private Discussions";
    protected static final String CELL_NAME_DISCUSSIONS = "General Discussions";
    protected static final String CELL_NAME_ATTACHMENTS = "Attachements";
    protected static final String CELL_NAME_PR_REVIEWER = "Reviewer";
    protected static final String CELL_NAME_PR_OPINION = "Opinion";
    protected static final String CELL_NAME_PR_SUGGESTED_SCORE = "Suggested Score";
    protected static final String CELL_NAME_PR_COMMENT = "Comments";
    protected static final String CELL_NAME_PR_DISCUSSIONS = "Peer Review Discussions";

    protected static final String CELL_DATA_TYPE = "Data Type";

    protected static final String DATA_TYPE_SINGLE_CHOICE = "Single Choice";
    protected static final String DATA_TYPE_MULTI_CHOICE = "Multiple Choice";
    protected static final String DATA_TYPE_INT = "Integer";
    protected static final String DATA_TYPE_FLOAT = "Decimal";
    protected static final String DATA_TYPE_TEXT = "Text";
    protected static final String DATA_TYPE_TABLE = "Table";

    protected static final String NOT_AVAILABLE = "NA";

    protected Sheet sheet = null;

    protected int productId;
    protected int includeOptions;

    // class members
    private static UserDAO userDao = null;
    private static ScorecardService scorecardSrvc = null;

    protected static AnswerObjectChoiceDAO answerObjectChoiceDAO = null;
    protected static AnswerObjectFloatDAO answerObjectFloatDAO = null;
    protected static AnswerObjectIntegerDAO answerObjectIntegerDAO = null;
    protected static AnswerObjectTextDAO answerObjectTextDAO = null;
    protected static ReferenceChoiceDAO refChoiceDAO = null;

    private File exportBasePath = null;
    private int attachedFileSeqNum = 0;
    protected String exportPathName = null;

    protected Map<Integer, UserView> userMap = null;
    protected Map<Integer, ReferenceObject> refObjMap = null;
    protected Map<Integer, List<ReferenceChoice>> refChoiceMap = null;
    protected Map<Integer, List<SurveyPeerReview>> prMap = null;
    protected Map<Integer, List<Message>> msgMap = null;

    // Map answerObjectId to object
    protected HashMap<Integer, AnswerObjectChoice> aocMap = new HashMap<Integer, AnswerObjectChoice>();
    protected HashMap<Integer, AnswerObjectInteger> aoiMap = new HashMap<Integer, AnswerObjectInteger>();
    protected HashMap<Integer, AnswerObjectFloat> aofMap = new HashMap<Integer, AnswerObjectFloat>();
    protected HashMap<Integer, AnswerObjectText> aotMap = new HashMap<Integer, AnswerObjectText>();

    protected int langId = 0;

    abstract public Workbook createWorkbook();

    protected void init(int productId, int includeOptions, int langId) {
        this.productId = productId;
        this.includeOptions = includeOptions;
        this.langId = langId;

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        if (userDao == null) userDao = (UserDAO) ctx.getBean("userBuildDao");
        if (scorecardSrvc == null) scorecardSrvc = (ScorecardService) ctx.getBean("scorecardService");
        if (answerObjectChoiceDAO == null) answerObjectChoiceDAO = (AnswerObjectChoiceDAO) ctx.getBean("answerObjectChoiceDAO");
        if (answerObjectFloatDAO == null) answerObjectFloatDAO = (AnswerObjectFloatDAO) ctx.getBean("answerObjectFloatDAO");
        if (answerObjectTextDAO == null) answerObjectTextDAO = (AnswerObjectTextDAO) ctx.getBean("answerObjectTextDAO");
        if (answerObjectIntegerDAO == null) answerObjectIntegerDAO = (AnswerObjectIntegerDAO) ctx.getBean("answerObjectIntegerDAO");
        if (refChoiceDAO == null) refChoiceDAO = (ReferenceChoiceDAO) ctx.getBean("referenceChoiceDao");

        userMap = userDao.selectAllUsers(productId);
        msgMap = new HashMap<Integer, List<Message>>();
        if (Constants.includeStaffReviews(includeOptions)) {
            scorecardSrvc.getInternalMessagesByProductId(productId, msgMap);
        }
        if (Constants.includeDiscussions(includeOptions)) {
            scorecardSrvc.getStaffAuthorMessagesByProductId(productId, msgMap);
        }
        if (Constants.includePeerReviews(includeOptions)) {
            scorecardSrvc.getPeerReviewMessagesByProductId(productId, msgMap);
        }

        if (Constants.includeReferences(includeOptions)) {
            refObjMap = scorecardSrvc.getReferenceObjectsByProductId(productId);
            refChoiceMap = new HashMap<Integer, List<ReferenceChoice>>();

            List<ReferenceChoice> refChoices = refChoiceDAO.findAll();
            if (refChoices != null && !refChoices.isEmpty()) {
                for (ReferenceChoice rc : refChoices) {
                    List<ReferenceChoice> rcList = refChoiceMap.get(rc.getReferenceId());
                    if (rcList == null) {
                        rcList = new ArrayList<ReferenceChoice>();
                        refChoiceMap.put(rc.getReferenceId(), rcList);
                    }
                    rcList.add(rc);
                }
            }
        }

        if (Constants.includePeerReviews(includeOptions)) {
            prMap = scorecardSrvc.getSurveyPeerReviewsByProductId(productId);
            cacheAOCObjects(answerObjectChoiceDAO.getAnswerObjectsOfProductSPR(productId));
            cacheAOIObjects(answerObjectIntegerDAO.getAnswerObjectsOfProductSPR(productId));
            cacheAOFObjects(answerObjectFloatDAO.getAnswerObjectsOfProductSPR(productId));
            cacheAOTObjects(answerObjectTextDAO.getAnswerObjectsOfProductSPR(productId));
        }

        exportPathName = UUID.randomUUID().toString();
    }


    protected void init(BaseScorecardReporter r) {
        this.productId = r.productId;
        this.includeOptions = r.includeOptions;
        this.exportPathName = r.exportPathName;
        this.userMap = r.userMap;
        this.msgMap = r.msgMap;
        this.refObjMap = r.refObjMap;
        this.refChoiceMap = r.refChoiceMap;
        this.prMap = r.prMap;
        this.aocMap = r.aocMap;
        this.aoiMap = r.aoiMap;
        this.aofMap = r.aofMap;
        this.aotMap = r.aotMap;
        this.langId = r.langId;
    }



    protected void cacheAOCObjects(List<AnswerObjectChoice> list) {
        if (list != null && !list.isEmpty()) {
            for (AnswerObjectChoice obj : list) {
                aocMap.put(obj.getId(), obj);
            }
        }
    }


    protected void cacheAOIObjects(List<AnswerObjectInteger> list) {
        if (list != null && !list.isEmpty()) {
            for (AnswerObjectInteger obj : list) {
                aoiMap.put(obj.getId(), obj);
            }
        }
    }

    protected void cacheAOFObjects(List<AnswerObjectFloat> list) {
        if (list != null && !list.isEmpty()) {
            for (AnswerObjectFloat obj : list) {
                aofMap.put(obj.getId(), obj);
            }
        }
    }


    protected void cacheAOTObjects(List<AnswerObjectText> list) {
        if (list != null && !list.isEmpty()) {
            for (AnswerObjectText obj : list) {
                aotMap.put(obj.getId(), obj);
            }
        }
    }


    public File getExportBasePath() {
        return exportBasePath;
    }

    public boolean hasMultipleFiles() {
        return (exportBasePath != null && exportBasePath.exists());
    }

    public void exportToCSV(Workbook workbook, OutputStream out) {
        if (workbook == null) {
            return;
        }
        StringBuilder sBuf = new StringBuilder();
        for (int i = 0, lastRowNum = sheet.getLastRowNum(); i <= lastRowNum; ++i) {
            Row row = sheet.getRow(i);
            for (int j = 0, lastCellNum = row.getLastCellNum(); j <= lastCellNum; ++j) {
                Cell cell = row.getCell((short) j);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            sBuf.append(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            sBuf.append('"').append(fixCSVCellText(cell.getStringCellValue())).append('"');
                            break;
                        default:
                            break;
                    }
                }
                if (j < lastCellNum) sBuf.append(',');
            }
            sBuf.append("\r\n");

            try {
                byte[] bytes = sBuf.toString().getBytes("UTF-8");
                out.write(bytes, 0, bytes.length);
                // out.write(sBuf.toString().getBytes("UTF-8"), 0, sBuf.length());
                // out.write(sBuf.toString().getBytes(), 0, sBuf.length());

            } catch (IOException ex) {
            } finally {
                try {
                    out.flush();
                } catch (IOException ex) {
                }
            }
            sBuf.setLength(0);
        }
    }


    public void exportToExcel(Workbook workbook, OutputStream out) {
        if (workbook == null) {
            return;
        }
        // style(sheet);
        try {
            workbook.write(out);
            out.flush();
        } catch (Exception ex) {
            log.error("Fail to generate EXCEL.", ex);
        } finally {
        }
    }


    public void cleanup() {
        log.info("Cleanup temp files");
        if (exportBasePath != null && exportBasePath.exists()) {
            log.info("Delete file: " + exportBasePath.getAbsolutePath());
            FileUtil.delete(exportBasePath);
        }
        if (userMap != null) {
            userMap.clear();
            userMap = null;
        }
        if (refObjMap != null) {
            refObjMap.clear();
            refObjMap = null;
        }
        if (prMap != null) {
            prMap.clear();
            prMap = null;
        }
        if (msgMap != null) {
            msgMap.clear();
            msgMap = null;
        }
    }

    protected Workbook newWorkbook() {
        Workbook workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Sheet1");
        return workbook;
    }

    protected void saveCellText(String targetName, Cell cell, String text, String publicName, String commentType) {
        if (StringUtils.isEmpty(text)) {
            text = "";
        }

        text = text.trim();

        if (text.length() < MAX_CELL_TEXT_LENGTH) {
            cell.setCellValue(text);
        } else {
            exportBasePath = getOrCreateExportFolderFile();
            String filename = targetName + "-" + colNumToString(cell.getColumnIndex() + 1) + "-" + (cell.getRowIndex() + 1) + ".txt";
            serializeMaxCellTextToFile(exportBasePath, filename, text, targetName, publicName, commentType);
            cell.setCellValue(filename);
        }
    }

    private void serializeMaxCellTextToFile(File baseDir, String filename, String text, String targetName, String publicName, String commentType) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(baseDir, filename)));
            writer.write(targetName + ", " + publicName + ", " + commentType + "\n\n");
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            log.error("IO Excepton.", ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
    }


    protected String getAnswerString(QuestionDef question, Object answerObj) {
        if (answerObj == null) return "";

        switch(question.getType()) {
            case Constants.ANSWER_TYPE_SINGLE:
            case Constants.ANSWER_TYPE_MULTI:

                List<QuestionOption> options = question.getOptions();
                if (options == null || options.isEmpty()) return "";
                AnswerObjectChoice answerObjectChoice = (AnswerObjectChoice)answerObj;
                String result = "";
                long value = answerObjectChoice.getChoices();
                long mask = -1;
                for (QuestionOption opt : options) {
                    mask = opt.getMask();
                    if ((value & mask) == mask) {
                        if (result.length() != 0) {
                            result += WORD_SEPARATOR + opt.getLabel();
                        } else {
                            result += opt.getLabel();
                        }
                    }
                }
                return result;


            case Constants.ANSWER_TYPE_INTEGER:
                AnswerObjectInteger answerObjectInteger = (AnswerObjectInteger)answerObj;
                return "" + answerObjectInteger.getValue();

            case Constants.ANSWER_TYPE_FLOAT:
                AnswerObjectFloat answerObjectFloat = (AnswerObjectFloat)answerObj;
                return "" + answerObjectFloat.getValue();

            case Constants.ANSWER_TYPE_TEXT:
                AnswerObjectText answerObjectText = (AnswerObjectText)answerObj;
                return answerObjectText.getValue();

            default:
                return "";
        }
    }


    protected String getAnswerString(int answerObjectId, QuestionDef question) {
        Object answerObj = null;

        switch(question.getType()) {
            case Constants.ANSWER_TYPE_SINGLE:
            case Constants.ANSWER_TYPE_MULTI:
                answerObj = aocMap.get(answerObjectId);
                break;

            case Constants.ANSWER_TYPE_INTEGER:
                answerObj = aoiMap.get(answerObjectId);
                break;

            case Constants.ANSWER_TYPE_FLOAT:
                answerObj = aofMap.get(answerObjectId);
                break;

            case Constants.ANSWER_TYPE_TEXT:
                answerObj = aotMap.get(answerObjectId);
                break;

            default:
                return "";
        }

        return getAnswerString(question, answerObj);
    }

    protected String getUserName(int userId) {
        StringBuilder sb = new StringBuilder();

        UserView uv = userMap.get(userId);
        if (uv != null) {

            if (Constants.anonymizeReport(includeOptions)) {
                // use role name as user name
                if (!StringUtils.isEmpty(uv.getRole())) {
                    sb.append(uv.getRole()).append(userId);
                } else {
                    sb.append("user").append(userId);
                }
            } else {
                // regular full name
                if (!StringUtils.isEmpty(uv.getFirstName())) {
                    sb.append(uv.getFirstName()).append(' ');
                }
                if (!StringUtils.isEmpty(uv.getLastName())) {
                    sb.append(uv.getLastName());
                }
            }
        }

        return sb.toString();
    }

    protected String composeComment(int userId, String comment) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(comment)) {
            String userName = getUserName(userId);
            if (userName.length() > 0) {
                sb.append(userName).append(": ");
            }
            sb.append(comment);
        }
        return sb.toString();
    }

    private String fixCSVCellText(String cellText) {
        if (StringUtils.isEmpty(cellText)) return cellText;
        cellText = cellText.replaceAll("\"", "\"\"");

        // Remove embedded CRLF in the cell since EXCEL can't handle them properly
        if (!Constants.keepCRLF(includeOptions)) {
            cellText = cellText.replaceAll("\r", " ");
            cellText = cellText.replaceAll("\n", " ");
        }

        return cellText;
    }

    private static String colNumToString(int colNum) {
        if (colNum <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        final int count = 'z' - 'a' + 1;
        int v = 0, n = 0;
        do {
            v = colNum / count;
            n = (colNum - 1) % count;
            sb.append((char) (n + 'a'));
            colNum = (n == 'z' - 'a') ? (v - 1) : v;
        } while (colNum > 0);
        return sb.reverse().toString().toUpperCase();
    }

    protected static double formatNum(double num) {
        return Double.valueOf(NUMBER_FORMAT.format(num));
    }

    protected Row createRow() {
        return createRow(sheet.getLastRowNum() + 1);
    }

    protected Row createRow(int rowNum) {
        return sheet.createRow(rowNum);
    }

    protected Cell createCell(Row row) {
        int num = row.getLastCellNum();
        return row.createCell(num);
    }

    protected Cell createCell(Row row, int cellNum) {
        return row.createCell(cellNum);
    }

    protected Cell createCell(Row row, int cellNum, String text) {
        Cell cell = row.createCell(cellNum);
        cell.setCellValue(text);
        return cell;
    }

    public File getOrCreateExportFolderFile() {
        if (exportBasePath != null && exportBasePath.exists()) {
            return exportBasePath;
        }
        exportBasePath = new File(Config.getString(Constants.KEY_EXPORT_BASE_PATH), exportPathName);

        if (!exportBasePath.exists()) {
            exportBasePath.mkdirs();
        }

        return exportBasePath;
    }

    protected File getOrCreateAttachedExportFolderFile() {
        File attachedFolder = new File(getOrCreateExportFolderFile(), ATTACH_ATTACHED_FOLDER);
        if (!attachedFolder.exists()) {
            attachedFolder.mkdirs();
        }

        return attachedFolder;
    }


    protected String getPeerReviewOpinionText(int opinion) {
        switch (opinion) {
            case com.ocs.indaba.common.Constants.SURVEY_PEER_OPTION_AGREE:
                return "Agree";

            case com.ocs.indaba.common.Constants.SURVEY_PEER_OPTION_AGREE_WITH_COMMENTS:
                return "Agree with Comments";

            case com.ocs.indaba.common.Constants.SURVEY_PEER_DISAGREE:
                return "Disagree";

            default:
                return "Not Qualified";
        }
    }


    protected String getPeerReviewText(QuestionDef def, int surveyAnswerId) {
        List<SurveyPeerReview> peerReviews = prMap.get(surveyAnswerId);

        if (peerReviews == null || peerReviews.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (SurveyPeerReview pv : peerReviews) {
            if (sb.length() > 0) {
                sb.append("||\n");
            }

            // add user name
            sb.append("REVIEWER: ");
            String userName = getUserName(pv.getReviewerUserId());
            if (userName.length() > 0) {
                sb.append(userName).append(";\n");
            }

            // add opinion
            String opinion = getPeerReviewOpinionText(pv.getOpinion());
            sb.append("OPINION: ").append(opinion).append(";\n");

            // add comments
            String comments = pv.getComments();
            if (comments != null) {
                sb.append("COMMENTS: ").append(comments).append(";\n");
            }

            // add suggested score
            if (pv.getOpinion() == com.ocs.indaba.common.Constants.SURVEY_PEER_DISAGREE) {
                String answer = getAnswerString(pv.getSuggestedAnswerObjectId(), def);
                sb.append("SUGGESTED ANSWER: ").append(answer).append(";\n");
            }
        }

        return sb.toString();
    }


    protected String getMessages(int msgboardId) {
        List<Message> messages = msgMap.get(msgboardId);
        if (messages == null || messages.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        for (Message msg : messages) {
            sb.append(composeComment(msg.getAuthorUserId(), msg.getBody())).append('\n');
        }
        return sb.toString();
    }
    

    protected String addAttachments(List<Attachment> attachments, String label, int questionId, int horseId, String targetShortName) {
        if (label == null || label.isEmpty()) label = "";
        label = label.trim();

        if (attachments == null || attachments.isEmpty()) return null;

        boolean first = true;
        StringBuilder sBuf = new StringBuilder();
        for (Attachment attached : attachments) {
            ++attachedFileSeqNum;
            if (StringUtils.isEmpty(attached.getType())) {
                attached.setType("dat");
            }
            String filename = MessageFormat.format(ATTACH_FILENAME_PATTERN, targetShortName, label, questionId, attachedFileSeqNum, attached.getType());
           
            String sourceFileName = AttachmentFilePathUtil.getContentAttachmentPath(horseId, attached.getFilePath());
            File destFile = new File(getOrCreateAttachedExportFolderFile(), filename);

            try {
                log.debug("Copying file " + sourceFileName + " to " + destFile);
                FileUtils.copyFile(new File(sourceFileName), destFile);
            } catch (Exception e) {
                log.error("Can't copy file " + sourceFileName + " to " + destFile);
            }

            //String downloadedFilepath =
            //createFileWithRandomContent("/data/upload/contentattachment/" + horseId + "/" + attached.getFilePath());
            if (!first) {
                sBuf.append(", ");
            } else {
                first = false;
            }
            sBuf.append(filename);
        }

        return sBuf.toString();
    }


    void setCellText(Cell cell, String text) {
        if (cell != null && text != null) {
            int maxLen = text.length();
            if (maxLen > MAX_CELL_TEXT_LENGTH) maxLen = MAX_CELL_TEXT_LENGTH;
            cell.setCellValue(text.substring(0, maxLen));
        }
    }

}
