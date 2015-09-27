SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
-- SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `scorecard_b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scorecard_b` ;

CREATE  TABLE IF NOT EXISTS `scorecard_b` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `scorecard_id` INT NOT NULL ,
  `content_header_id` INT NOT NULL COMMENT 'The survey content object that the scorecard belongs to. content_header is defined in the Builder DB.' ,
  `target_id` INT NOT NULL ,
  `product_id` INT NOT NULL ,
  `project_id` INT NOT NULL ,
  `org_id` INT NOT NULL ,
  `study_period_id` INT NOT NULL ,
  `survey_config_id` INT NOT NULL ,
  `status` TINYINT NOT NULL COMMENT 'Status of the scorecard in Builder:\n\n1 - no data; 2 - in progress; 3 - submitted; 4 - published;' ,
  `last_update_time` DATETIME NOT NULL COMMENT 'When this scorecard was last updated' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Scorecard contains flattened raw data values of a survey content object. The values of answers are copied from the survey_answer objects in the Builder DB.';


-- -----------------------------------------------------
-- Table `scorecard_answer_b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scorecard_answer_b` ;

CREATE  TABLE IF NOT EXISTS `scorecard_answer_b` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `scorecard_id` INT NOT NULL ,
  `question_id` INT NOT NULL COMMENT 'ID of the question (in Builder DB) this answer corresponds to.' ,
  `indicator_id` INT NOT NULL ,
  `data_type` TINYINT NOT NULL COMMENT '1 - single choice; 2 - multi choice; 3 - integer; 4 - float; 5 - text' ,
  `value` TEXT NOT NULL COMMENT 'This is the the representation of the answer value. For multi choice, it is expressed as the labels of selected choices separated by the vertical bar char.\n' ,
  `score` DECIMAL(22,6) NULL COMMENT 'The score associated with the answer. Only for single-choice, int, and float data types. Integer is converted to float.' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_sa_card`
    FOREIGN KEY (`scorecard_id` )
    REFERENCES `scorecard_b` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Answers in the scorecard.';

CREATE INDEX `fk_sa_card` ON `scorecard_answer_b` (`scorecard_id` ASC) ;

CREATE UNIQUE INDEX `uq_sa_card_qst` ON `scorecard_answer_b` (`scorecard_id` ASC, `question_id` ASC) ;

CREATE UNIQUE INDEX `uq_sa_card_ind` ON `scorecard_answer_b` (`scorecard_id` ASC, `indicator_id` ASC) ;


-- -----------------------------------------------------
-- Table `otis_value_b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `otis_value_b` ;

CREATE  TABLE IF NOT EXISTS `otis_value_b` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `org_id` INT NOT NULL ,
  `target_id` INT NOT NULL ,
  `indicator_id` INT NOT NULL ,
  `study_period_id` INT NOT NULL ,
  `data_type` TINYINT NOT NULL COMMENT '1 - single choice; 2 - multi choice; 3 - integer; 4 - float; 5 - text.\nCopied from scorecard_answer.' ,
  `value` TEXT NOT NULL COMMENT 'Copied from scorecard_answer.' ,
  `score` DECIMAL(22,6) NULL COMMENT 'Copied from scorecard_answer.' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table contains data values in the OTIS data space';

CREATE UNIQUE INDEX `uq_otisv_otis` ON `otis_value_b` (`org_id` ASC, `target_id` ASC, `indicator_id` ASC, `study_period_id` ASC) ;


-- -----------------------------------------------------
-- Table `workset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `workset` ;

CREATE  TABLE IF NOT EXISTS `workset` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `defined_by_user_id` INT NOT NULL ,
  `defined_time` DATETIME NOT NULL ,
  `org_id` INT NOT NULL COMMENT 'Organization the workset belongs to. Inherits the org of the user who defined the ws.' ,
  `visibility` TINYINT NOT NULL DEFAULT 1 COMMENT '1 - public; 2 - private' ,
  `is_active` TINYINT(1)  NOT NULL COMMENT 'Whether this WS is active. Inactive WS is ignored by the aggregation process.' ,
  `last_update_time` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;

CREATE UNIQUE INDEX `name_UNIQUE` ON `workset` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `ws_project`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ws_project` ;

CREATE  TABLE IF NOT EXISTS `ws_project` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workset_id` INT NOT NULL ,
  `project_id` INT NOT NULL COMMENT 'ID of the project (in Builder DB) included into the WS' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_wsp_ws`
    FOREIGN KEY (`workset_id` )
    REFERENCES `workset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Projects that are included into a WS';

CREATE INDEX `fk_wsp_ws` ON `ws_project` (`workset_id` ASC) ;

CREATE UNIQUE INDEX `uq_wsp_wsp` ON `ws_project` (`workset_id` ASC, `project_id` ASC) ;


-- -----------------------------------------------------
-- Table `ws_target`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ws_target` ;

CREATE  TABLE IF NOT EXISTS `ws_target` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workset_id` INT NOT NULL ,
  `target_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_wst_ws`
    FOREIGN KEY (`workset_id` )
    REFERENCES `workset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Targets to be included into a WS';

CREATE INDEX `fk_wst_ws` ON `ws_target` (`workset_id` ASC) ;

CREATE UNIQUE INDEX `uq_wst` ON `ws_target` (`workset_id` ASC, `target_id` ASC) ;


-- -----------------------------------------------------
-- Table `ws_indicator_instance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ws_indicator_instance` ;

CREATE  TABLE IF NOT EXISTS `ws_indicator_instance` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workset_id` INT NOT NULL ,
  `indicator_id` INT NOT NULL ,
  `org_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_wsi_ws`
    FOREIGN KEY (`workset_id` )
    REFERENCES `workset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Indicators to be included into a WS';

CREATE INDEX `fk_wsi_ws` ON `ws_indicator_instance` (`workset_id` ASC) ;

CREATE UNIQUE INDEX `uq_wsi` ON `ws_indicator_instance` (`workset_id` ASC, `indicator_id` ASC, `org_id` ASC) ;


-- -----------------------------------------------------
-- Table `ws_puser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ws_puser` ;

CREATE  TABLE IF NOT EXISTS `ws_puser` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workset_id` INT NOT NULL ,
  `user_id` INT NOT NULL COMMENT 'ID of the privileged user (in Builder DB) ' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_wspu_ws`
    FOREIGN KEY (`workset_id` )
    REFERENCES `workset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Privileged users of the WS';

CREATE INDEX `fk_wspu_ws` ON `ws_puser` (`workset_id` ASC) ;

CREATE INDEX `uq_wspu` ON `ws_puser` (`workset_id` ASC, `user_id` ASC) ;


-- -----------------------------------------------------
-- Table `aggr_method`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aggr_method` ;

CREATE  TABLE IF NOT EXISTS `aggr_method` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `module_name` VARCHAR(100) NULL COMMENT 'Name of the Java module' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;

CREATE UNIQUE INDEX `name_UNIQUE` ON `aggr_method` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `datapoint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `datapoint` ;

CREATE  TABLE IF NOT EXISTS `datapoint` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workset_id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `aggr_method_id` INT NOT NULL COMMENT 'ID of the aggr method' ,
  `hole_policy` TINYINT NOT NULL DEFAULT 1 COMMENT '1 - do not compute; 2 - ignore missing values' ,
  `short_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_dp_ws`
    FOREIGN KEY (`workset_id` )
    REFERENCES `workset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_dp_method`
    FOREIGN KEY (`aggr_method_id` )
    REFERENCES `aggr_method` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Data point definition ';

CREATE INDEX `fk_dp_ws` ON `datapoint` (`workset_id` ASC) ;

CREATE INDEX `fk_dp_method` ON `datapoint` (`aggr_method_id` ASC) ;

CREATE UNIQUE INDEX `uq_dp_wsn` ON `datapoint` (`workset_id` ASC, `name` ASC) ;

CREATE UNIQUE INDEX `uq_dp_wssn` ON `datapoint` (`workset_id` ASC, `short_name` ASC) ;


-- -----------------------------------------------------
-- Table `dp_member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dp_member` ;

CREATE  TABLE IF NOT EXISTS `dp_member` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `datapoint_id` INT NOT NULL COMMENT 'The datapoint this member belongs to.' ,
  `indicator_instance_id` INT NULL COMMENT 'ID of the member indicator instance. 0 if not an indicator instance.' ,
  `dp_id` INT NULL COMMENT 'ID of the member data point. 0 if not a data point.' ,
  `weight` INT NULL COMMENT 'Relative weight assigned to this member. Must be >= 0.' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_dpm_dp`
    FOREIGN KEY (`datapoint_id` )
    REFERENCES `datapoint` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_dpm_ii`
    FOREIGN KEY (`indicator_instance_id` )
    REFERENCES `ws_indicator_instance` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Define members of a data point definition';

CREATE INDEX `fk_dpm_dp` ON `dp_member` (`datapoint_id` ASC) ;

CREATE UNIQUE INDEX `uq_dpm` ON `dp_member` (`datapoint_id` ASC, `indicator_instance_id` ASC, `dp_id` ASC) ;

CREATE INDEX `fk_dpm_ii` ON `dp_member` (`indicator_instance_id` ASC) ;


-- -----------------------------------------------------
-- Table `dataset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dataset` ;

CREATE  TABLE IF NOT EXISTS `dataset` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workset_id` INT NOT NULL ,
  `includes_nonpub_data` TINYINT(1)  NOT NULL COMMENT 'Whether this dataset includes non-published data' ,
  `last_update_time` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ds_ws`
    FOREIGN KEY (`workset_id` )
    REFERENCES `workset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Datasets are used to store results of aggregation computation for worksets. Up to two datasets could be generated for the same workset: one using only published survey results, and one using both published and non-published (but usable) survey results.';

CREATE INDEX `fk_ds_ws` ON `dataset` (`workset_id` ASC) ;

CREATE UNIQUE INDEX `uq_ds` ON `dataset` (`workset_id` ASC, `includes_nonpub_data` ASC) ;


-- -----------------------------------------------------
-- Table `tds_value_b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tds_value_b` ;

CREATE  TABLE IF NOT EXISTS `tds_value_b` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dataset_id` INT NOT NULL ,
  `target_id` INT NOT NULL ,
  `datapoint_id` INT NOT NULL ,
  `study_period_id` INT NOT NULL ,
  `value` DECIMAL(22,6) NOT NULL COMMENT 'Computed value of the aggregation' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_tdsv_ds`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tdsv_dp`
    FOREIGN KEY (`datapoint_id` )
    REFERENCES `datapoint` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'The TDS (target, datapoint, study-period) data space for storing aggregated data.';

CREATE INDEX `fk_tdsv_ds` ON `tds_value_b` (`dataset_id` ASC) ;

CREATE INDEX `fk_tdsv_dp` ON `tds_value_b` (`datapoint_id` ASC) ;

CREATE UNIQUE INDEX `uq_tdsv_dd` ON `tds_value_b` (`dataset_id` ASC, `datapoint_id` ASC, `target_id` ASC, `study_period_id` ASC) ;


-- -----------------------------------------------------
-- Table `export_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `export_log` ;

CREATE  TABLE IF NOT EXISTS `export_log` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `time` DATETIME NOT NULL ,
  `workset_id` INT NULL COMMENT 'Only for dataset export' ,
  `product_id` INT NULL COMMENT 'Only for journal export' ,
  `user_id` INT NOT NULL COMMENT 'ID of the user (in Builder DB) that did the export. 0 if unknown.' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_exp_ws`
    FOREIGN KEY (`workset_id` )
    REFERENCES `workset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This table keeps audit log of all export activities';

CREATE INDEX `fk_exp_ws` ON `export_log` (`workset_id` ASC) ;


-- -----------------------------------------------------
-- Table `exp_target`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `exp_target` ;

CREATE  TABLE IF NOT EXISTS `exp_target` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `export_log_id` INT NOT NULL ,
  `target_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_expt_exp`
    FOREIGN KEY (`export_log_id` )
    REFERENCES `export_log` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Targets included into an export';

CREATE INDEX `fk_expt_exp` ON `exp_target` (`export_log_id` ASC) ;

CREATE UNIQUE INDEX `uq_expt` ON `exp_target` (`export_log_id` ASC, `target_id` ASC) ;


-- -----------------------------------------------------
-- Table `exp_study_period`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `exp_study_period` ;

CREATE  TABLE IF NOT EXISTS `exp_study_period` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `export_log_id` INT NOT NULL ,
  `study_period_id` INT NOT NULL COMMENT 'ID of study period in Builder DB' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_expsp_exp`
    FOREIGN KEY (`export_log_id` )
    REFERENCES `export_log` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Study periods in an export';

CREATE INDEX `fk_expsp_exp` ON `exp_study_period` (`export_log_id` ASC) ;

CREATE UNIQUE INDEX `uq_expsp` ON `exp_study_period` (`export_log_id` ASC, `study_period_id` ASC) ;


-- -----------------------------------------------------
-- Table `widget`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `widget` ;

CREATE  TABLE IF NOT EXISTS `widget` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `tech_name` VARCHAR(255) NOT NULL COMMENT 'Technical or internal name of the widget. Provided by the widget developer.' ,
  `display_name` VARCHAR(255) NOT NULL COMMENT 'Display name of the widget when listed in widget library.' ,
  `description` TEXT NULL ,
  `author` VARCHAR(45) NULL ,
  `org_id` INT NOT NULL DEFAULT 1 COMMENT 'ID of the organization that owns this widget. Organizations are defined in the Builder DB.' ,
  `version` VARCHAR(45) NOT NULL DEFAULT '1.0' COMMENT 'In the form of major.minor.' ,
  `visibility` TINYINT NOT NULL DEFAULT 1 COMMENT '1 - public;\n2 - private;\n3 - authenticated;' ,
  `target_url` VARCHAR(255) NOT NULL ,
  `icon_file_name` VARCHAR(100) NOT NULL ,
  `params` TEXT NULL COMMENT 'comma separated standard parameters' ,
  `content_types` INT NOT NULL ,
  `config_type` INT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table contains only the meta attributes of widgets';


-- -----------------------------------------------------
-- Table `config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `config` ;

CREATE  TABLE IF NOT EXISTS `config` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `site_name` VARCHAR(45) NULL ,
  `site_intro` TEXT NULL ,
  `srv_otis_value` VARCHAR(45) NOT NULL ,
  `srv_scorecard` VARCHAR(45) NOT NULL ,
  `srv_scorecard_answer` VARCHAR(45) NOT NULL ,
  `srv_tds_value` VARCHAR(45) NOT NULL ,
  `com_otis_value` VARCHAR(45) NOT NULL ,
  `com_scorecard` VARCHAR(45) NOT NULL ,
  `com_scorecard_answer` VARCHAR(45) NOT NULL ,
  `com_tds_value` VARCHAR(45) NOT NULL ,
  `table_swap_time` DATETIME NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `scorecard_a`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scorecard_a` ;

CREATE  TABLE IF NOT EXISTS `scorecard_a` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `scorecard_id` INT NOT NULL ,
  `content_header_id` INT NOT NULL COMMENT 'The survey content object that the scorecard belongs to. content_header is defined in the Builder DB.' ,
  `target_id` INT NOT NULL ,
  `product_id` INT NOT NULL ,
  `project_id` INT NOT NULL ,
  `org_id` INT NOT NULL ,
  `study_period_id` INT NOT NULL ,
  `survey_config_id` INT NOT NULL ,
  `status` TINYINT NOT NULL COMMENT 'Status of the scorecard in Builder:\n\n1 - no data; 2 - in progress; 3 - submitted; 4 - published;' ,
  `last_update_time` DATETIME NOT NULL COMMENT 'When this scorecard was last updated' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Scorecard contains flattened raw data values of a survey content object. The values of answers are copied from the survey_answer objects in the Builder DB.';


-- -----------------------------------------------------
-- Table `scorecard_answer_a`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scorecard_answer_a` ;

CREATE  TABLE IF NOT EXISTS `scorecard_answer_a` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `scorecard_id` INT NOT NULL ,
  `question_id` INT NOT NULL COMMENT 'ID of the question (in Builder DB) this answer corresponds to.' ,
  `indicator_id` INT NOT NULL ,
  `data_type` TINYINT NOT NULL COMMENT '1 - single choice; 2 - multi choice; 3 - integer; 4 - float; 5 - text' ,
  `value` TEXT NOT NULL COMMENT 'This is the the representation of the answer value. For multi choice, it is expressed as the labels of selected choices separated by the vertical bar char.\n' ,
  `score` DECIMAL(22,6) NULL COMMENT 'The score associated with the answer. Only for single-choice, int, and float data types. Integer is converted to float.' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_sa_card`
    FOREIGN KEY (`scorecard_id` )
    REFERENCES `scorecard_b` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Answers in the scorecard.';

CREATE INDEX `fk_sa_card` ON `scorecard_answer_a` (`scorecard_id` ASC) ;

CREATE UNIQUE INDEX `uq_sa_card_qst` ON `scorecard_answer_a` (`scorecard_id` ASC, `question_id` ASC) ;

CREATE UNIQUE INDEX `uq_sa_card_ind` ON `scorecard_answer_a` (`scorecard_id` ASC, `indicator_id` ASC) ;


-- -----------------------------------------------------
-- Table `tds_value_a`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tds_value_a` ;

CREATE  TABLE IF NOT EXISTS `tds_value_a` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dataset_id` INT NOT NULL ,
  `target_id` INT NOT NULL ,
  `datapoint_id` INT NOT NULL ,
  `study_period_id` INT NOT NULL ,
  `value` DECIMAL(22,6) NOT NULL COMMENT 'Computed value of the aggregation' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_tdsv_ds`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tdsv_dp`
    FOREIGN KEY (`datapoint_id` )
    REFERENCES `datapoint` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'The TDS (target, datapoint, study-period) data space for storing aggregated data.';

CREATE INDEX `fk_tdsv_ds` ON `tds_value_a` (`dataset_id` ASC) ;

CREATE INDEX `fk_tdsv_dp` ON `tds_value_a` (`datapoint_id` ASC) ;

CREATE UNIQUE INDEX `uq_tdsv_dd` ON `tds_value_a` (`dataset_id` ASC, `datapoint_id` ASC, `target_id` ASC, `study_period_id` ASC) ;


-- -----------------------------------------------------
-- Table `otis_value_a`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `otis_value_a` ;

CREATE  TABLE IF NOT EXISTS `otis_value_a` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `org_id` INT NOT NULL ,
  `target_id` INT NOT NULL ,
  `indicator_id` INT NOT NULL ,
  `study_period_id` INT NOT NULL ,
  `data_type` TINYINT NOT NULL COMMENT '1 - single choice; 2 - multi choice; 3 - integer; 4 - float; 5 - text.\nCopied from scorecard_answer.' ,
  `value` TEXT NOT NULL COMMENT 'Copied from scorecard_answer.' ,
  `score` DECIMAL(22,6) NULL COMMENT 'Copied from scorecard_answer.' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table contains data values in the OTIS data space';

CREATE UNIQUE INDEX `uq_otisv_otis` ON `otis_value_a` (`org_id` ASC, `target_id` ASC, `indicator_id` ASC, `study_period_id` ASC) ;



-- SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
