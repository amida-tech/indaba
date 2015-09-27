/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.common;

/**
 *
 * @author jiangjeff
 */
public class Constants {

    public final static String CONFIG_FILE = "indaba_config.properties";
    public static final String INIT_PARAM_INDABA_CONFIG_FILE = "indabaConfigFile";
    public static final long MILLSECONDS_PER_DAY = 24 * 3600 * 1000;
    //
    public static final String JSON_VERSION = "1.0";
    public static final int SCORE_BASE_VALUE = 10000;
    public static final int MOE_MPR = 2;
    // Invalid id
    public static final int INVALID_INT_ID = -1;
    public static final long INVALID_LONG_ID = -1L;
    public static final double INVALID_FLOAT_ID = -1d;
    public static final short WORKSET_VISIBILITY_PUBLIC = 1;
    public static final short WORKSET_VISIBILITY_PRIVATE = 2;
    public static final int NODE_TYPE_CATEGORY = 1;
    public static final int NODE_TYPE_QUESTION = 2;
    public static final int WIDGET_VISIBILITY_PUBLIC = 1;
    public static final int WIDGET_VISIBILITY_PRIVATE = 2;
    public static final int WIDGET_VISIBILITY_AUTHENTICATED = 3;
    public static final int PROJECT_VISIBILITY_PUBLIC = 1;
    public static final int PROJECT_VISIBILITY_PRIVATE = 2;
    public final static String PROJECT_OF_PUBLISHER = "publisher";
    public final static String RIGHT_OF_MANAGEWIDGET = "manage widgets";
    public final static String RIGHT_OF_MANAGEWORKSET = "manage working sets";
    public final static String RIGHT_OF_EXPORTSCORECARDREPORT = "export scorecard reports";
    public final static String PRODUCT_FILENAME_PREFIX = "PRD";
    public final static String HORSE_FILENAME_PREFIX = "HRS";
    public final static String SCORE_LABEL_VERY_WEAK = "Very Weak";
    public final static String SCORE_LABEL_WEEK = "Weak";
    public final static String SCORE_LABEL_MODERATE = "Moderate";
    public final static String SCORE_LABEL_STRONG = "Strong";
    public final static String SCORE_LABEL_VERY_STRONG = "Very Strong";
    // Implementation Gap Label 
    public final static String GAP_LABEL_SMALL = "Small";
    public final static String GAP_LABEL_MODERATE = "Moderate";
    public final static String GAP_LABEL_LARGE = "Large";
    public final static String GAP_HUGE = "Huge";
//    public final static String REFERENCE_LEGAL_1 = "In Law 1";
//    public final static String REFERENCE_LEGAL_2 = "In Law 2";
//    public final static String REFERENCE_IMPLEMENTATION = "In Practice";
//    public final static int REFERENCE_LEGAL_1_ID = 1;
//    public final static int REFERENCE_LEGAL_2_ID = 2;
//    public final static int REFERENCE_IMPLEMENTATION_ID = 3;
    public final static String QUESTION_TYPE_SINGLE = "Single";
    public final static String QUESTION_TYPE_MULTI = "Multi";
    public final static String QUESTION_TYPE_INTEGER = "Integer";
    public final static String QUESTION_TYPE_FLOAT = "Float";
    public final static String QUESTION_TYPE_TEXT = "Text";
    public final static int ANSWER_TYPE_SINGLE = 1;
    public final static int ANSWER_TYPE_MULTI = 2;
    public final static int ANSWER_TYPE_INTEGER = 3;
    public final static int ANSWER_TYPE_FLOAT = 4;
    public final static int ANSWER_TYPE_TEXT = 5;
    public final static int ANSWER_TYPE_TABLE = 6;
    public final static int SCORECARD_STATUS_NO_DATA = 1;
    public final static int SCORECARD_STATUS_IN_PROGRESS = 2;
    public final static int SCORECARD_STATUS_SUBMITTED = 3;
    public final static int SCORECARD_STATUS_COMPLETED = 4;
    public final static int WIDGET_TYPE_JOURNAL = 1;
    public final static int WIDGET_TYPE_SURVEY = 2;
    public final static int WIDGET_TYPE_ALL = 3;
    public final static int PUBLIC_WORKSET = 1;
    public final static int PRIVATE_WORKSET = 2;
    public static final String[] ANSWER_TYPES = {"", "Single", "Multi", "Integer", "Float", "Text"};
    public static final String[] TARGET_TYPES = {"Country", "International Region",
        "Sub-national: Province", "Sub-national: State",
        "Sub-national: Region", "Sub-national: City/Municipality",
        "Organization", "Government Unit/Project", "Sector", ""};
    public final static String TABLE_NAME_SCORECARD_A = "scorecard_a";
    public final static String TABLE_NAME_SCORECARD_B = "scorecard_b";
    public final static String TABLE_NAME_SCORECARD_ANSWER_A = "scorecard_answer_a";
    public final static String TABLE_NAME_SCORECARD_ANSWER_B = "scorecard_answer_b";
    public final static String TABLE_NAME_OTIS_VALUE_A = "otis_value_a";
    public final static String TABLE_NAME_OTIS_VALUE_B = "otis_value_b";
    public final static String TABLE_NAME_TDS_VALUE_A = "tds_value_a";
    public final static String TABLE_NAME_TDS_VALUE_B = "tds_value_b";
    public static final String RWI_PRODUCT_NAME = "RWI Index Questionnaire";
    public static final int INCLUDE_AUTHOR_COMMENTS = 1;
    public static final int INCLUDE_REFERENCES = 2;
    public static final int INCLUDE_PEER_REVIEWS = 2 << 2;
    public static final int INCLUDE_STAFF_REVIEWS = 2 << 3;
    public static final int INCLUDE_DISCUSSIONS = 2 << 4;
    public static final int INCLUDE_GLOBAL_INTEGRITY = 2 << 5;
    public static final int INCLUDE_ATTACHED_FILES = 2 << 6;
    public static final int INCLUDE_ANSWER_LABELS = 2 << 7;
    public static final int INCLUDE_SCORING_OPTIONS = 2 << 8;
    public static final int KEEP_CRLF = 2 << 9;
    public static final int INCLUDE_TREE = 2 << 10;
    public static final int ANONYMIZE_REPORT = 2 << 11;

    /*api call*/
    public static final short AUTHENTICATION_OK = 0;
    public static final short AUTHENTICATION_BAD_FUNCTION = 1;
    public static final short AUTHENTICATION_BAD_SYNTAX = 2;
    public static final short AUTHENTICATION_BAD_ORG = 3;
    public static final short AUTHENTICATION_KEY_NOT_FOUND = 4;
    public static final short AUTHENTICATION_KEY_EXPIRED = 5;
    public static final short AUTHENTICATION_KEY_REVOKED = 6;
    public static final short AUTHENTICATIOND_BAD_DIGEST = 7;
    public static final short AUTHENTICATION_CALL_TOO_OLD = 8;
    public static final short AUTHENTICATION_REPLAY = 9;
    public static final short AUTHORIZATION_OK = 0;
    public static final short AUTHORIZATION_FAIL = 1;

    public static final String KEY_EXPORT_BASE_PATH = "export.base.path";
    public static final String KEY_EXPORT_MAX_HORSES_PER_SHEET = "export.max.horses.per.sheet";

    public static final String KEY_EXPORT_FILENAME_PATTERN = "export.filename.pattern";
    public static final String KEY_STORAGE_BASE_PATH = "storage.base.path";
    public static final String KEY_PROD_DIRPATH_PATTERN = "prod.dirpath.pattern";
    public static final String KEY_SRF_FILENAME_PATTERN = "srf.filename.pattern";
    public static final String KEY_PIF_FILENAME = "pif.filename";
    public static final String KEY_SCORECARD_FILEPATH_PATTERN = "scorecard.filepath.pattern";
    public static final String KEY_JOURNAL_ATTACHMENT_URL = "journal.attachment.url";
    public static final String KEY_SCORECARD_ATTACHMENT_URL = "scorecard.attachment.url";
    public static final String KEY_STORAGE_UPLOAD_BASE = "storage.upload.base";
    public static final String KEY_CONTENT_ATTACHMENT_PATH = "storage.upload.contentattachment.path";

    public static final String KEY_WORK_STORAGE_BASE_PATH = "work.storage.base.path";
    public static final String KEY_WORK_CACHE_BASE_PATH = "work.cache.base.path";

    public static final String KEY_CACHE_BASE_PATH = "cache.base.path";
    public static final String KEY_INDICATOR_SUMMARY_FILENAME_PATTERN = "indicator.summary.filename.pattern";
    public static final String KEY_DATA_SUMMARY_FILENAME_PATTERN = "data.summary.filename.pattern";
    public static final String KEY_DATA_AGGREGATION_ENABLE = "data.aggregation.enable";
    public static final String KEY_DATA_AGGREGATION_WHITELIST = "data.aggregation.whitelist";

    public static final String KEY_WORKSET_BASE_PATH = "workset.root.path";
    public static final String KEY_WORKSET_PUBLISHED_SUFFIX = "workset.published.suffix";
    public static final String KEY_WORKSET_USABLE_SUFFIX = "workset.usable.suffix";

    public static final String KEY_AGGREGATION_LOCAL_CONTEXT_PATH = "aggregation.local.context.path";
    public static final String KEY_WKHTMLTOPDF_CMD = "export.wkhtmltopdf.cmd";
    public static final String KEY_WKHTMLTOPDF_TIMEOUT = "export.wkhtmltopdf.run.timeout";
    public static final String KEY_WKHTMLTOPDF_JAVA_EXECUTOR_THREADNUM = "export.wkhtmltopdf.java.executor.threadNum";

    public static final String KEY_PDFTK_CMD = "export.pdftk.cmd";
    public static final String KEY_PDFTK_TIMEOUT = "export.pdftk.run.timeout";

    public static final String KEY_API_TIMESTAMP_INTERVAL = "api.security.timestamp.interval";//interval in seconds
    public static final String KEY_IDEF_WORK_DIR = "idef.work.dir";
    public static final String KEY_IDEF_EXPORT_DIR = "idef.export.dir";
    public static final String KEY_IDEF_PWD = "idef.pwd";



    public static boolean includeAuthorComments(int val) {
        return ((INCLUDE_AUTHOR_COMMENTS & val) == INCLUDE_AUTHOR_COMMENTS);
    }

    public static boolean anonymizeReport(int val) {
        return ((ANONYMIZE_REPORT & val) == ANONYMIZE_REPORT);
    }

    public static boolean includeReferences(int val) {
        return ((INCLUDE_REFERENCES & val) == INCLUDE_REFERENCES);
    }

    public static boolean includePeerReviews(int val) {
        return ((INCLUDE_PEER_REVIEWS & val) == INCLUDE_PEER_REVIEWS);
    }

    public static boolean includeStaffReviews(int val) {
        return ((INCLUDE_STAFF_REVIEWS & val) == INCLUDE_STAFF_REVIEWS);
    }

    public static boolean includeDiscussions(int val) {
        return ((INCLUDE_DISCUSSIONS & val) == INCLUDE_DISCUSSIONS);
    }

    public static boolean includeGlobalIntegrity(int val) {
        return ((INCLUDE_GLOBAL_INTEGRITY & val) == INCLUDE_GLOBAL_INTEGRITY);
    }

    public static boolean includeAttachedFiles(int val) {
        return ((INCLUDE_ATTACHED_FILES & val) == INCLUDE_ATTACHED_FILES);
    }

    public static boolean includeAnswerLabels(int val) {
        return ((INCLUDE_ANSWER_LABELS & val) == INCLUDE_ANSWER_LABELS);
    }

    public static boolean includeScoringOptions(int val) {
        return ((INCLUDE_SCORING_OPTIONS & val) == INCLUDE_SCORING_OPTIONS);
    }

    public static boolean keepCRLF(int val) {
        return ((KEEP_CRLF & val) == KEEP_CRLF);
    }

    public static boolean includeTree(int val) {
        return ((INCLUDE_TREE & val) == INCLUDE_TREE);
    }

    public static final int REF_CLASSIFICATION_PRACTICE = 0;
    public static final int REF_CLASSIFICATION_LEGAL = 1;

    public static final short IDEF_IMPORT_RESULT_SUCCESS = 0;
    public static final short IDEF_IMPORT_RESULT_FAILURE = 1;
    public static final short IDEF_IMPORT_RESULT_DELETED = 2;

}
