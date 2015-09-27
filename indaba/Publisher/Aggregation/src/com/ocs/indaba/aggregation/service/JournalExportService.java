/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import au.com.bytecode.opencsv.CSVWriter;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.common.Config;
import com.ocs.indaba.builder.dao.JournalExportDAO;
import com.ocs.indaba.aggregation.vo.JournalContentObjectInfo;
import com.ocs.indaba.aggregation.vo.JournalForm;
import com.ocs.indaba.aggregation.vo.JournalReview;
import com.ocs.indaba.aggregation.vo.JournalSummaryInfo;
import com.ocs.indaba.po.Attachment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sjf
 * 1. author_user_id may be null
 * 2. could test without actual attachment e.x.URL
 * 3. what is field seprate for files?
 * 4. hwo to seprate reviews?
 */
public class JournalExportService extends ExportService {

    protected Logger logger = Logger.getLogger(JournalExportService.class);
    private JournalObjectService journalObjectService = null;
    private JournalExportDAO journalExportDao = null;
    //private JounalDataDAO journalDataDao = null;
    private JournalSummaryService journalSummaryService = null;
    private JournalForm journalForm = null;

    
    public void createJournalExport(JournalForm journalForm, HttpSession session) {
        this.journalForm = journalForm;
        this.session = session;
        initExportPath();
        createExportFolder();
        executeExport();

        String basePath = Config.getString(Constants.KEY_EXPORT_BASE_PATH);
        createZIPFile(basePath + EXPORT_TEMP_FILE_FOLDER, basePath + EXPORT_TEMP_ZIP_FOLDER);
    }

    protected void executeExport() {
        df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        createSummaryCSV();
        createJournalContent();
    }

    private void createJournalContent() {
        JournalContentObjectInfo targetJournal = null;
        Integer horseId = null, targetId = null, exportableItems = null;
        boolean includeAttachments, includeReviews;
        List<Integer> targetIdList = journalForm.getTargetIds();
        for (int i = 0; i < targetIdList.size(); i++) {
            targetId = targetIdList.get(i);
            horseId = journalExportDao.selectHorseIdbyProductIdandTargetId(journalForm.getProductId(), targetId);
            exportableItems = journalExportDao.selectExportableItemsByHorseId(horseId);
            includeAttachments = (exportableItems % 2 == 0) ? true : false;
            includeReviews = (exportableItems < 2) ? true : false;
            targetJournal = getJournalContentObjectInfo(horseId.intValue(), includeReviews, includeAttachments);
            if (targetJournal != null) {
                targetFileNames = new ArrayList<String>();
                if (targetJournal.getBody() != null) {
                    createTargetBody(targetJournal.getTargetShortName(), horseId.intValue(), targetJournal.getBody());
                }
                if (targetJournal.getAttachments() != null && !targetJournal.getAttachments().isEmpty()) {
                    createAttachements(targetJournal);
                }
                if (targetJournal.getJournalReviews() != null && !targetJournal.getJournalReviews().isEmpty()) {
                    createComments(targetJournal);
                }
                createTargetInfoCSV(targetJournal);
            } else {
                logger.error("Journal not found! horseId=" + horseId.intValue());
            }
            DecimalFormat myFormatter = new DecimalFormat("#.#");
            session.setAttribute("processPercent", myFormatter.format(100 * (double) (i + 1) / (double) targetIdList.size()));
        }
    }

    private void createComments(JournalContentObjectInfo targetJournal) {
        JournalReview review = null;
        List<JournalReview> journalReviews = targetJournal.getJournalReviews();
        String reviewFileName = "/" + targetJournal.getTargetShortName() + ".comments.csv";
        targetFileNames.add(reviewFileName.substring(1));
        List<String[]> csv = new ArrayList<String[]>();
        ArrayList<String> col1 = new ArrayList<String>();
        ArrayList<String> cols = new ArrayList<String>();
        try {
            FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH) + EXPORT_TEMP_FILE_FOLDER + reviewFileName, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter write = new BufferedWriter(osw);
            col1.add("Reviewer");
            col1.add("Time");
            col1.add("Comment");
            csv.add(col1.toArray(new String[]{}));
            for (int i = 0; i < journalReviews.size(); i++) {
                cols = new ArrayList<String>();
                review = journalReviews.get(i);
                cols.add("reviewer " + review.getUserId());
                if (review.getLastUpdated() != null) {
                    cols.add(df.format(review.getLastUpdated()));
                } else {
                    cols.add(df.format(review.getSubmitTime()));
                }
                cols.add(review.getComment());
                csv.add(cols.toArray(new String[]{}));
            }
            CSVWriter csvWriter = new CSVWriter(write);
            csvWriter.writeAll(csv);
            csvWriter.close();
            write.close();

            // CLOSEFILE
            osw.close();
            fos.close();
        } catch (IOException e) {
            logger.error("Meet error when create comments, horseId = " + targetJournal.getHorseId(), e);
        }

    }

    private void createAttachements(JournalContentObjectInfo targetJournal) {
        Attachment att = null;
        String shortName = targetJournal.getTargetShortName();
        String filename = null;
        String fileInputPath = null;
        String fileOutputPath = null;
        List<Attachment> attachements = targetJournal.getAttachments();
        for (int i = 0; i < attachements.size(); i++) {
            att = attachements.get(i);
            if (i < 9) {
                filename = shortName + ".file.0" + (i + 1) + "." + att.getType();
            } else {
                filename = shortName + ".file." + (i + 1) + "." + att.getType();
            }
            targetFileNames.add(filename);
            fileInputPath = MessageFormat.format(Config.getString(Constants.KEY_CONTENT_ATTACHMENT_PATH),
                    Config.getString(Constants.KEY_STORAGE_UPLOAD_BASE), targetJournal.getHorseId(), att.getName());

            fileOutputPath = Config.getString(Constants.KEY_EXPORT_BASE_PATH) + EXPORT_TEMP_FILE_FOLDER + "/" + filename;

            copyFiletoTemporaryFolder(fileInputPath, fileOutputPath);
        }
    }

    private JournalContentObjectInfo getJournalContentObjectInfo(int horseId, boolean includeReviews, boolean includeAttachments) {
        JournalContentObjectInfo journal = journalObjectService.getJournalInfoByHorseId(horseId);
        if (journal != null) {
            journal.setAuthor(journalObjectService.getAuthorNamebyHorseId(horseId));
            if (includeAttachments) {
                journal.setAttachments(journalObjectService.getAttachmentsByHorseId(horseId));
            }
            if (includeReviews) {
                journal.setJournalReviews(journalObjectService.getJournalPeerReviewByContentObjectId(journal.getId()));
            }
        }
        return journal;
    }

    private void createTargetBody(String targetShortName, int horseId, String targetBody) {
        String bodyFileName = "/" + targetShortName + ".body.txt";
        targetFileNames.add(bodyFileName.substring(1));
        try {
            FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH) + EXPORT_TEMP_FILE_FOLDER + bodyFileName, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter write = new BufferedWriter(osw);
            write.write(targetBody);
            write.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            logger.error("Meet error when create target body, horseId = " + horseId, e);
        }
    }

    private void createTargetInfoCSV(JournalContentObjectInfo targetJournal) {
        String infoFileName = "/" + targetJournal.getTargetShortName() + ".info.csv";
        targetFileNames.add(0, infoFileName.substring(1));
        List<String[]> csv = new ArrayList<String[]>();
        ArrayList<String> col1 = new ArrayList<String>();
        ArrayList<String> cols = new ArrayList<String>();
        try {
            FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH) + EXPORT_TEMP_FILE_FOLDER + infoFileName, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter write = new BufferedWriter(osw);
            col1.add("Target");
            cols.add(targetJournal.getTargetName());
            col1.add("Author");
            cols.add(targetJournal.getAuthor());

            col1.add("Creation");
            cols.add(df.format(targetJournal.getCreation()));
            String temp = "";
            col1.add("Files(" + targetFileNames.size() + ")");
            if (!targetFileNames.isEmpty()) {
                for (int i = 0; i < targetFileNames.size(); i++) {
                    if (i != targetFileNames.size() - 1) {
                        temp += targetFileNames.get(i) + "|";
                    } else {
                        temp += targetFileNames.get(i);
                    }
                }
                cols.add(temp);
            } else {
                cols.add("no file for this target");
            }
            csv.add(col1.toArray(new String[]{}));
            csv.add(cols.toArray(new String[]{}));
            CSVWriter csvWriter = new CSVWriter(write);
            csvWriter.writeAll(csv);
            csvWriter.close();
            write.close();

            // CLOSEFILE
            osw.close();
            fos.close();
        } catch (IOException e) {
            logger.error("Meet error when create target body, horseId = " + targetJournal.getHorseId(), e);
        }
    }

    protected void createSummaryCSV() {
        JournalSummaryInfo journalSummary =
                journalSummaryService.selectJournalInfo(journalForm.getProductId(),
                journalForm.getProjectId(), journalForm.getTargetIds());
        try {
            FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH)
                    + EXPORT_TEMP_FILE_FOLDER + EXPORT_SUMMARY_NAME, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter write = new BufferedWriter(osw);
            CSVWriter csvWriter = new CSVWriter(write);
            csvWriter.writeAll(journalSummary.toCsv());
            csvWriter.close();
            write.close();

            // CLOSEFILE
            osw.close();
            fos.close();
        } catch (IOException e) {
            logger.error("Meet error when create summary csv", e);
        }
    }

    protected void initExportPath() {             
        EXPORT_BASE_PATH = Config.getString(Constants.KEY_EXPORT_BASE_PATH);
        EXPORT_TEMP_FILE_FOLDER = "/journal_export_temporary";
        EXPORT_TEMP_ZIP_FOLDER = "/journal_export_zip";
        EXPORT_ZIP_NAME = "/indaba_export.zip";
        EXPORT_SUMMARY_NAME = "/summary.csv";
    }

    private void copyFiletoTemporaryFolder(String inputPath, String outputPath) {
        try {
            FileInputStream fis = new FileInputStream(inputPath);
            BufferedInputStream inBuff = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream(outputPath);
            BufferedOutputStream outBuff = new BufferedOutputStream(fos);

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();

            inBuff.close();
            outBuff.close();

            // CLOSEFILE
            fis.close();
            fos.close();
        } catch (Exception e) {
            logger.error("Error copying files [inputPath=" + inputPath + " outputPath=" + outputPath + "]" + e.getLocalizedMessage());
        }
    }

    @Autowired
    public void setJournalObjectService(JournalObjectService journalObjectService) {
        this.journalObjectService = journalObjectService;
    }

    @Autowired
    public void setJournalExportDAO(JournalExportDAO jounalExportDao) {
        this.journalExportDao = jounalExportDao;
    }
/*
    @Autowired
    public void setJournalDataDAO(JounalDataDAO journalDataDao) {
        this.journalDataDao = journalDataDao;
    }
*/
    @Autowired
    public void setJournalSummaryService(JournalSummaryService journalSummaryService) {
        this.journalSummaryService = journalSummaryService;
    }

    /**
     * @return the session
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(HttpSession session) {
        this.session = session;
    }
}
