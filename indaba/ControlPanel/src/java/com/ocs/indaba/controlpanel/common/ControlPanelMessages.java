/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.common;

/**
 *
 * @author Jeff Jiang
 */
public class ControlPanelMessages {

    public static final String INDICATOR_IMPORT__FILE_NOT_UTF8 = "jsp.err.file.not.utf8";
    public static final String INDICATOR_IMPORT__FILE_TYPE_NOT_SUPPORTED = "jsp.err.unupported.file.type";

    public static final String INDICATOR_IMPORT__MISSING_LINE = "jsp.err.indicatorimport.missing.line";  // 6000221
    public static final String INDICATOR_IMPORT__INVALID_LINE = "jsp.err.indicatorimport.invalid.line";  // 6000222
    public static final String INDICATOR_IMPORT__INVALID_LINE_VALUE = "jsp.err.indicatorimport.invalid.lv"; // 6000223
    public static final String INDICATOR_IMPORT__MISSING_LINE_VALUE = "jsp.err.indicatorimport.missing.lv"; // 6000224
    public static final String INDICATOR_IMPORT__MISSING_HEADER = "jsp.err.indicatorimport.missing.header";  // 6000225
    public static final String INDICATOR_IMPORT__INVALID_COLUMN = "jsp.err.indicatorimport.invalid.column";  // 6000226
    public static final String INDICATOR_IMPORT__INVALID_COLUMN_VALUE = "jsp.err.indicatorimport.invalid.cv";  // 6000227
    public static final String INDICATOR_IMPORT__MISSING_COLUMN_VALUE = "jsp.err.indicatorimport.missing.cv";  // 6000228
    public static final String INDICATOR_IMPORT__INCOMPLETE_HEADER = "jsp.err.indicatorimport.incomp.header";  // 6000229
    public static final String INDICATOR_IMPORT__MIN_GREATER_THAN_MAX = "jsp.err.indicatorimport.min.gt.max"; // 6000248
    public static final String INDICATOR_IMPORT__NO_ORG_ACCESS = "jsp.err.indicatorimport.noorgaccess";  // 6000265
    public static final String INDICATOR_IMPORT__NAME_NOT_UNIQUE = "jsp.err.indicatorimport.name.not.unique";  // 6000266
    public static final String INDICATOR_IMPORT__NO_INDICATORS = "jsp.err.indicatorimport.no.indicators";  // 6000267
    public static final String INDICATOR_IMPORT__NAME_ALREADY_IN_DB = "jsp.err.indicatorimport.name.in.db";   // 6000268

    public static final String INDICATOR_IMPORT__FIELD_TOO_LONG = "jsp.err.indicatorimport.field.too.long";

    public static final String INDICATOR_TRANS__WRONG_CHOICE_KEY = "jsp.err.indicatortrans.wrong.choice.key"; // 6000275 ####
    public static final String INDICATOR_TRANS__LANG_INDICATOR_ALREADY_EXISTS = "jsp.err.indicatortrans.lang.exists";  // 6000276
    public static final String INDICATOR_TRANS__INCONSISTENT_LANG = "jsp.err.indicatortrans.inconsistent.lang"; // 6000277   ###
    public static final String INDICATOR_TRANS__WRONG_INDICATOR_KEY = "jsp.err.indicatortrans.wrong.ind.key"; // 6000278   ###
    public static final String INDICATOR_EDIT__USED_IN_SC = "jsp.err.indicatoredit.used.in.sc"; // 6000291
    public static final String INDICATOR_EDIT__NOT_AUTHORIZED = "jsp.err.indicatoredit.not.authorized"; // 6000292

    public static final String INDICATOR__SAVED = "cp.ok.indicator.saved";
    public static final String INDICATOR__CREATED = "cp.ok.indicator.created";

    public static final String INDICATOR_DELETE__USED_IN_SC = "jsp.err.indicatordelete.used.in.sc";  // 6000293
    public static final String INDICATOR_DELETE__NOT_AUTHORIZED = "jsp.err.indicatordelete.not.authorized"; // 6000294
    public static final String INDICATOR_MOVE__PRIVATE_NO_MOVE = "jsp.err.indicatormove.private.no.move";  // 6000295
    public static final String INDICATOR_MOVE__PUBLIC_TO_PRIVATE = "jsp.err.indicatormove.public.to.private";  // 6000296
    public static final String INDICATOR_MOVE__NOT_AUTHORIZED = "jsp.err.indicatormove.not.authorized";  // 6000297

    public static final String KEY_ERR_NOT_EXISTS = "cp.error.not_exists";
    public static final String KEY_ERR_DELETE_ACTIVE_INDICATOR = "cp.error.delete_active_indicator";
    public static final String KEY_OK = "cp.text.ok";
    public static final String KEY_ERR_INDICATORS_NOT_SPECIFIED = "cp.error.indicators.not.specified"; // 6000298
    public static final String KEY_DUPLICATED_TARGET_SHORT_NAME = "cp.err.duplicated_target_short_name"; // 6000304
    public static final String KEY_DUPLICATED_TARGET_NAME = "cp.err.duplicated_target_name"; // 6000303
    public static final String KEY_DUPLICATED_PROJECT_NAME = "cp.err.duplicated_project_name";  // 6000302
    public static final String KEY_DUPLICATED_PRODUCT_NAME = "cp.err.duplicated_product_name";  // 6000302
    public static final String KEY_DUPLICATED_SURVEY_CONFIG_NAME = "cp.err.duplicated_survey_config_name";
    public static final String KEY_DUPLICATED_USERFINDER = "cp.err.duplicated_userfinder";
    public static final String KEY_DUPLICATED_SURVEY_QUESTION = "cp.err.duplicated_survey_question";
    public static final String PROGRAM_ERROR = "cp.error.internal";  // 6000299
    public static final String TARGET_EDIT__NOT_AUTHORIZED = "cp.err.targetedit.not.authorized";  // 6000316
    public static final String MUST_LOGIN = "You must Login first";
    public static final String NO_SYSTEM_ACCESS = "You have no access to this system.";
    public static final String KEY_SESSION_TIMEOUT = "cp.err.session_expired";
    public static final String KEY_ERROR_ORG_ADMIN_USED = "cp.err.org_admin_used";
    public static final String KEY_ERROR_PRIMARY_ORG_ADMIN_USED = "cp.err.primary_org_admin_used";
    public static final String KEY_ERROR_ORG_PRIVATE_TARGETS_USED = "cp.err.org_private_targets_used";
    public static final String KEY_ERROR_PRIMARY_ORG_PRIVATE_TARGETS_USED = "cp.err.primary_org_private_targets_used";
    public static final String KEY_ERROR_NON_EXISTENT_PROJECT_OWNER = "cp.err.nonexisient_project_owner";
    public static final String KEY_ERROR_INVALID_ORG_ID = "cp.err.invalid_organization_id";
    public static final String KEY_ERROR_ALREADY_EXISTS_PRIMARY_OWNER = "cp.err.exists_as_primary_owner";
    public static final String KEY_ERROR_ALREADY_EXISTS_SECONDARY_OWNER = "cp.err.exists_as_secondary_owner";
    public static final String KEY_ERROR_ALREADY_EXISTS_PRIMARY_ADMIN = "cp.err.exists_as_primary_admin";
    public static final String KEY_ERROR_ALREADY_EXISTS_SECONDARY_ADMIN = "cp.err.exists_as_secondary_admin";
    public static final String KEY_ERROR_INVALID_USER_ID = "cp.err.invalid_user_id";
    public static final String KEY_ERROR_NON_EXISTENT_PROJECT_ADMIN = "cp.err.nonexisient_project_admin";
    public static final String KEY_ERROR_INVALID_ROLE_ID = "cp.err.invalid_role_id";
    public static final String KEY_ERROR_ALREADY_EXISTS_PROJECT_ROLE = "cp.err.exists_project_role";
    public static final String KEY_ERROR_NON_EXISTENT_PROJECT_ROLE = "cp.err.nonexistent_project_role";
    public static final String KEY_ERROR_PROJECT_ROLE_USED = "cp.err.project_role_used";
    public static final String KEY_ERROR_SERVER_INTERNAL = "cp.error.internal";
    public static final String KEY_ERROR_NON_EXISTENT_PRODUCT = "cp.err.nonexistent_prod";
    public static final String KEY_ERROR_NON_EXISTENT_SURVEY_CATEGORY = "cp.err.nonexistent_survey_category";
    public static final String KEY_ERROR_NON_EXISTENT_SURVEY_QUESTION = "cp.err.nonexistent_survey_question";
    public static final String KEY_ERROR_NON_EXISTENT_SURVEY_CONFIG = "cp.err.nonexistent_survey_config";
    public static final String KEY_ERROR_NON_EXISTENT_USERFINDER = "cp.err.nonexistent_userfinder";
    public static final String KEY_ERROR_GOAL_HAS_ONLY_ONE_TASK = "cp.err.goal_has_only_one_task";
    public static final String KEY_ERROR_HORSE_MUST_BE_INIT_OR_STOPPED = "cp.err.horse_must_be_init_or_stopped";
    public static final String KEY_ERROR_HORSE_MUST_BE_RUNNING = "cp.err.horse_must_be_running";
    public static final String KEY_ERROR_HORSE_IS_CANCELLED = "cp.err.horse_is_cancelled";
    public static final String KEY_ERROR_HORSE_MUST_BE_CANCELLED = "cp.err.horse_must_be_cancelled";
    public static final String KEY_ERROR_HORSE_MUST_BE_INIT = "cp.err.horse_must_be_init";
    public static final String KEY_ERROR_INVOKE_DB_STORE_PROCEDURE = "cp.err.invoke_db_stored_procedure";


    public static final String KEY_ERROR_CANNOT_START_PROD = "cp.err.cannot_start_prod";
    public static final String KEY_ERROR_NO_TARGET_IN_PROD = "cp.err.no_target_in_prod";
    public static final String KEY_ERROR_NO_CONTRIBUTOR_IN_PROD = "cp.err.no_contributor_in_prod";
    public static final String KEY_ERROR_GOAL_MUST_HAVE_TASK = "cp.err.goal_must_have_task";
    public static final String KEY_ERROR_NO_GOAL_FOR_TASK = "cp.err.no_goal_for_task";
    public static final String KEY_ERROR_INVALID_TOOLID_AND_TYPE = "cp.err.invalid_toolid_and_type";
    public static final String KEY_ERROR_FAIL_TO_INVOKE_STORED_PROCEDURE = "cp.err.fail_invoke_stored_procedure";

    public static final String KEY_DUPLICATED_TASK_NAME = "cp.err.duplicated_task_name";

    public static final String KEY_ERROR_NON_EXISTENT_TASK = "cp.err.nonexistent_task";
    public static final String KEY_ERROR_NON_EXISTENT_PROJECT = "cp.err.nonexistent_project";

    public static final String KEY_ERROR_MISSING_TASK_NAME = "cp.err.missing_task_name";
    public static final String KEY_ERROR_MISSING_TASK_INSTRUCTIONS = "cp.err.missing_task_instructions";
    public static final String KEY_ERROR_MISSING_TASK_ROLES = "cp.err.missing_task_roles";
    public static final String KEY_ERROR_MISSING_TASK_TOOL = "cp.err.missing_task_tool";
    public static final String KEY_ERROR_MISSING_TASK_GOAL = "cp.err.missing_task_goal";

    public static final String KEY_ERROR_PROD_ALREADY_IN_CONFIG = "cp.err.already_in_config";
    public static final String KEY_ERROR_PROD_ALREADY_IN_TEST = "cp.err.already_in_test";
    public static final String KEY_ERROR_PROD_ALREADY_IN_PROD = "cp.err.already_in_prod";
    public static final String KEY_ERROR_NO_CONFIG_TO_PROD = "cp.err.no_config_to_prod";
    public static final String KEY_ERROR_SA_OPERATION_ONLY = "cp.err.sa_operation_only";
    public static final String KEY_ERROR_CANT_DELETE_STICKY_TASK = "cp.err.cant_delete_sticky_task";
    public static final String KEY_ERROR_MISSING_TARGETS = "cp.err.missing_targets";
    public static final String KEY_ERROR_NON_EXISTENT_MEMBERSHIP = "cp.err.nonexistent_membership";
    public static final String KEY_ERROR_NON_EXISTENT_INDICATOR = "cp.err.nonexistent_indicator";
    public static final String KEY_ERROR_WRONG_PROJECT_OF_PRODUCT = "cp.err.wrong_project_of_product";
    public static final String KEY_ERROR_NON_EXISTENT_TARGET = "cp.err.nonexistent_target";
    public static final String KEY_ERROR_NON_EXISTENT_ASSIGNMENT = "cp.err.nonexistent_assignment";
    public static final String KEY_ERROR_NON_EXISTENT_USER = "cp.err.nonexistent_user";
    public static final String KEY_ERROR_DUPLICATE_ASSIGNMENT_SINGLE = "cp.err.duplicate_assignment_single";
    public static final String KEY_ERROR_DUPLICATE_ASSIGNMENT_MULTI = "cp.err.duplicate_assignment_multi";
    public static final String KEY_ERROR_INVALID_TASK_DEF = "cp.err.invalid_task_def";
    public static final String KEY_ERROR_SC_VISIBILITY_VIOLATED_ADD_ORG = "cp.err.add_org.sc_visibility_violated";
    public static final String KEY_ERROR_SC_VISIBILITY_VIOLATED_CHANGE_PRIMARY = "cp.err.change_primary.sc_visibility_violated";
    public static final String KEY_ERROR_CONTRIBUTOR_HAS_TASK = "cp.err.controbutor.has.task";
    public static final String KEY_ERROR_SURVEY_CONFIG_IS_USED = "cp.err.surveyconfig.used";
    public static final String KEY_ERROR_OBSOLETE_SURVEY_TREE = "cp.err.surveyconfig.obsolete.tree";
    public static final String KEY_ERROR_INVALID_REQUEST_URL = "cp.err.invalid_req_url";
    public static final String KEY_ERROR_INVALID_PROJECT_ID = "cp.err.invalid_proj_id";
    public static final String KEY_ERROR_INVALID_SURVEYCONFIG_ID = "cp.err.invalid_surveyconf_id";

    public static final String USER_IMPORT__INVALID_EMAIL = "jsp.err.userimp.invalid.email";
    public static final String USER_IMPORT__INVALID_ROLE = "jsp.err.userimp.invalid.role";
    public static final String USER_IMPORT__NO_USERS = "jsp.err.userimp.no.users";

    public static final String NOTIF_IMPORT__NOTIF_CONFLICT = "jsp.err.notifimp.conflict";
    public static final String NOTIF_IMPORT__INVALID_TYPE = "jsp.err.notifimp.invalid.type";
    public static final String NOTIF_IMPORT__INVALID_LANGUAGE = "jsp.err.notifimp.invalid.language";
    public static final String NOTIF_IMPORT__NO_NOTIFS = "jsp.err.notifimp.no.notifs";
    public static final String NOTIF_IMPORT__DB_CONFLICT = "jsp.err.notifimp.db.conflict";

    public static final String NOTIF_ERROR_NOT_FOUND = "cp.err.notif.not.found";
    public static final String NOTIF_ERROR_CONFLICT = "cp.err.notif.conflict";

    public static final String NOTIF_EXPORT_NO_SELECTION = "cp.notif.exp.no.selection";
    public static final String NOTIF_EXPORT_INTERNAL_ERROR = "cp.notif.exp.error";

    public static final String TABLE_PARSER__NO_TABLE_SHEET = "jsp.err.table.no.table.sheet";
    public static final String TABLE_PARSER__NO_CELLS = "jsp.err.table.no.cells";
    public static final String TABLE_LOADER__BAD_INDICATOR_REF = "jsp.err.table.bad.indicator.ref";
    public static final String TABLE_LOADER__UNUSED_INDICATOR = "jsp.err.table.unused.indicator";

    public static final String NOTEDEF_ERROR_NOT_FOUND = "cp.err.notedef.not.found";
    public static final String GROUPDEF_ERROR_NOT_FOUND = "cp.err.groupdef.not.found";
 
}
