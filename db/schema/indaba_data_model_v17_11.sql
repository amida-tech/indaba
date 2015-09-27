SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
-- SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `language` ;

CREATE  TABLE IF NOT EXISTS `language` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `language` CHAR(2) NOT NULL COMMENT 'Language abbreviation: EN, CN, JP, etc' ,
  `language_desc` VARCHAR(45) NOT NULL ,
  `status` TINYINT NOT NULL COMMENT 'Whether this lanaguge is enabled:\n\n0 - active\n1 - inactive' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table stores languages supported. All language using 2 letter code. (ISO -639-1)';

CREATE UNIQUE INDEX `language_UNIQUE` ON `language` (`language` ASC) ;


-- -----------------------------------------------------
-- Table `msgboard`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `msgboard` ;

CREATE  TABLE IF NOT EXISTS `msgboard` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID of the msgboard' ,
  `create_time` DATETIME NOT NULL COMMENT 'When the msgboard was created' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'A msgboard contains multiple messages. It is used for discussion and user inboxes.';


-- -----------------------------------------------------
-- Table `organization`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `organization` ;

CREATE  TABLE IF NOT EXISTS `organization` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL ,
  `address` VARCHAR(255) NULL ,
  `admin_user_id` INT NOT NULL ,
  `url` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_org_admin_user`
    FOREIGN KEY (`admin_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE UNIQUE INDEX `name_UNIQUE` ON `organization` (`name` ASC) ;

CREATE INDEX `fk_org_admin_user` ON `organization` (`admin_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE  TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(20) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `password` VARCHAR(20) NOT NULL COMMENT 'clear text password for easy management' ,
  `last_password_change_time` DATETIME NULL ,
  `create_time` DATETIME NOT NULL ,
  `last_logout_time` DATETIME NULL ,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'account status.\n0 - inactive\n1 - active\n2 - deleted' ,
  `timezone` INT NULL ,
  `language_id` INT NULL ,
  `msgboard_id` INT NULL ,
  `forward_inbox_msg` TINYINT(1)  NULL DEFAULT true ,
  `number_msgs_per_screen` INT NULL DEFAULT 10 ,
  `first_name` VARCHAR(45) NULL ,
  `last_name` VARCHAR(45) NULL ,
  `phone` VARCHAR(45) NULL ,
  `cell_phone` VARCHAR(45) NULL ,
  `address` VARCHAR(255) NULL ,
  `bio` TEXT NULL ,
  `photo` VARCHAR(255) NULL COMMENT 'file path of user\'s photo' ,
  `location` VARCHAR(255) NULL COMMENT 'List of role IDs to whom the profile is visible. ' ,
  `email_detail_level` TINYINT NULL DEFAULT 1 COMMENT '0 - Alert only\n1 - Full message' ,
  `default_project_id` INT NULL DEFAULT '-1' ,
  `last_login_time` DATETIME NULL ,
  `site_admin` TINYINT NULL DEFAULT 0 ,
  `organization_id` INT NOT NULL DEFAULT 1 COMMENT 'The organization the user belongs to' ,
  `privacy_policy_accept_time` TIMESTAMP NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_user_language`
    FOREIGN KEY (`language_id` )
    REFERENCES `language` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_messageboard`
    FOREIGN KEY (`msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_org`
    FOREIGN KEY (`organization_id` )
    REFERENCES `organization` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'The user account structure';

CREATE INDEX `fk_user_language` ON `user` (`language_id` ASC) ;

CREATE INDEX `fk_user_messageboard` ON `user` (`msgboard_id` ASC) ;

CREATE UNIQUE INDEX `username_UNIQUE` ON `user` (`username` ASC) ;

CREATE INDEX `fk_user_org` ON `user` (`organization_id` ASC) ;


-- -----------------------------------------------------
-- Table `access_matrix`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `access_matrix` ;

CREATE  TABLE IF NOT EXISTS `access_matrix` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL COMMENT 'Each matrix has a unique name' ,
  `description` VARCHAR(255) NULL ,
  `default_value` TINYINT NULL COMMENT 'Default permission value if the value for a role/right combination is not explicitly defined.\n\n0 - No\n1 - Yes\n2 - Undefined' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'An access matrix defines permission values for role/right combinations';

CREATE UNIQUE INDEX `name_UNIQUE` ON `access_matrix` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `study_period`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study_period` ;

CREATE  TABLE IF NOT EXISTS `study_period` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Definition of study period';

CREATE UNIQUE INDEX `name_UNIQUE` ON `study_period` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `view_matrix`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `view_matrix` ;

CREATE  TABLE IF NOT EXISTS `view_matrix` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `default_value` TINYINT NOT NULL COMMENT '0 - none\n1 - limited\n2 - full' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Controls what roles can see what other roles\' profile';

CREATE UNIQUE INDEX `name_UNIQUE` ON `view_matrix` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `project`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project` ;

CREATE  TABLE IF NOT EXISTS `project` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the project' ,
  `organization_id` INT NOT NULL DEFAULT 1 COMMENT 'ID of the organization the project belongs to' ,
  `code_name` VARCHAR(45) NOT NULL COMMENT 'Code name of the project' ,
  `description` VARCHAR(255) NOT NULL ,
  `owner_user_id` INT NOT NULL COMMENT 'Project owner\'s uid' ,
  `creation_time` DATETIME NULL COMMENT 'When the project was created' ,
  `access_matrix_id` INT NOT NULL COMMENT 'Project-level default access matrix' ,
  `view_matrix_id` INT NOT NULL COMMENT 'ID of the view matrix for this project' ,
  `start_time` DATE NOT NULL COMMENT 'When the project is to be opened' ,
  `study_period_id` INT NOT NULL COMMENT 'ID of the study period specification' ,
  `status` INT NOT NULL DEFAULT 0 COMMENT 'Status of the project\n\n0 - waiting\n1 - in-flight\n2 - completed\n3 - suspended\n4 - abandoned\n' ,
  `logo_path` VARCHAR(255) NULL COMMENT 'File path to the project\'s logo' ,
  `msgboard_id` INT NULL COMMENT 'ID of the msgboard for project-level discussions: the project wall' ,
  `admin_user_id` INT NOT NULL COMMENT 'UID of the project admin' ,
  `sponsor_logos` VARCHAR(256) NULL ,
  `is_active` TINYINT(1)  NOT NULL DEFAULT 1 COMMENT 'whether the project is active' ,
  `close_time` DATE NULL COMMENT 'When the project is expected to be closed' ,
  `visibility` TINYINT NOT NULL DEFAULT 1 COMMENT 'Visibility type of the project. 1 = public; 2 = private' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_project_user1`
    FOREIGN KEY (`owner_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_table11`
    FOREIGN KEY (`access_matrix_id` )
    REFERENCES `access_matrix` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_study_period1`
    FOREIGN KEY (`study_period_id` )
    REFERENCES `study_period` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_view_matrix1`
    FOREIGN KEY (`view_matrix_id` )
    REFERENCES `view_matrix` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_messageboard1`
    FOREIGN KEY (`msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_admin`
    FOREIGN KEY (`admin_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_project_org`
    FOREIGN KEY (`organization_id` )
    REFERENCES `organization` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Definition of project';

CREATE UNIQUE INDEX `code_name_UNIQUE` ON `project` (`code_name` ASC) ;

CREATE INDEX `fk_project_user1` ON `project` (`owner_user_id` ASC) ;

CREATE INDEX `fk_project_table11` ON `project` (`access_matrix_id` ASC) ;

CREATE INDEX `fk_project_study_period1` ON `project` (`study_period_id` ASC) ;

CREATE INDEX `fk_project_view_matrix1` ON `project` (`view_matrix_id` ASC) ;

CREATE INDEX `fk_project_messageboard1` ON `project` (`msgboard_id` ASC) ;

CREATE INDEX `fk_project_admin` ON `project` (`admin_user_id` ASC) ;

CREATE INDEX `fk_project_org` ON `project` (`organization_id` ASC) ;


-- -----------------------------------------------------
-- Table `team`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `team` ;

CREATE  TABLE IF NOT EXISTS `team` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `project_id` INT NOT NULL ,
  `team_name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_team_project1`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM;

CREATE INDEX `fk_team_project1` ON `team` (`project_id` ASC) ;

CREATE UNIQUE INDEX `uq_pid_name` ON `team` (`project_id` ASC, `team_name` ASC) ;


-- -----------------------------------------------------
-- Table `team_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `team_user` ;

CREATE  TABLE IF NOT EXISTS `team_user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_id` INT NOT NULL ,
  `team_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_team_user_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_team_user_team1`
    FOREIGN KEY (`team_id` )
    REFERENCES `team` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM;

CREATE INDEX `fk_team_user_user1` ON `team_user` (`user_id` ASC) ;

CREATE INDEX `fk_team_user_team1` ON `team_user` (`team_id` ASC) ;

CREATE UNIQUE INDEX `uq_tu` ON `team_user` (`user_id` ASC, `team_id` ASC) ;


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role` ;

CREATE  TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique id of the role' ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table contains definitions of all roles in the system';

CREATE UNIQUE INDEX `name_UNIQUE` ON `role` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `project_membership`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_membership` ;

CREATE  TABLE IF NOT EXISTS `project_membership` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_id` INT NOT NULL COMMENT 'UID of the user who is included into a project' ,
  `role_id` INT NOT NULL COMMENT 'ID of the role the user takes in the project' ,
  `project_id` INT NOT NULL COMMENT 'ID of the project' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_project_membership_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_membership_project_roles1`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`role_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_pm_project`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Defines user membership in a project. User can be a member of many projects.A user can take only one role in a project.';

CREATE INDEX `fk_project_membership_user1` ON `project_membership` (`user_id` ASC) ;

CREATE INDEX `fk_project_membership_project_roles1` ON `project_membership` (`role_id` ASC) ;

CREATE UNIQUE INDEX `uq_pu` ON `project_membership` (`user_id` ASC, `project_id` ASC) ;

CREATE INDEX `fk_pm_project` ON `project_membership` (`project_id` ASC) ;


-- -----------------------------------------------------
-- Table `right_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `right_category` ;

CREATE  TABLE IF NOT EXISTS `right_category` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID of the category' ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Define categories for user rights.';

CREATE UNIQUE INDEX `name_UNIQUE` ON `right_category` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `rights`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rights` ;

CREATE  TABLE IF NOT EXISTS `rights` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `right_category_id` INT NOT NULL COMMENT 'ID of the category the right belongs to' ,
  `name` VARCHAR(45) NOT NULL COMMENT 'Unique name of the right' ,
  `label` VARCHAR(45) NOT NULL COMMENT 'Label is displayed to end users' ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_right_category`
    FOREIGN KEY (`right_category_id` )
    REFERENCES `right_category` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'System function access is controlled by the role-based authorization system, which defines what roles have what rights. This table defines all available user rights.';

CREATE INDEX `fk_right_category` ON `rights` (`right_category_id` ASC) ;

CREATE UNIQUE INDEX `name_UNIQUE` ON `rights` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `access_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `access_permission` ;

CREATE  TABLE IF NOT EXISTS `access_permission` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `access_matrix_id` INT NOT NULL COMMENT 'ID of the access matrix that this permission belongs to' ,
  `role_id` INT NOT NULL COMMENT 'ID of the role in role/right combination' ,
  `rights_id` INT NOT NULL COMMENT 'ID of the right in role/right combination' ,
  `permission` TINYINT NOT NULL COMMENT 'Permission value:\n0 - No\n1 - Yes\n2 - Undefined' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_permission_matrix1`
    FOREIGN KEY (`access_matrix_id` )
    REFERENCES `access_matrix` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_permission_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_permission_right`
    FOREIGN KEY (`rights_id` )
    REFERENCES `rights` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This defines the permission value for a role/right combination';

CREATE INDEX `fk_permission_matrix1` ON `access_permission` (`access_matrix_id` ASC) ;

CREATE INDEX `fk_permission_role1` ON `access_permission` (`role_id` ASC) ;

CREATE UNIQUE INDEX `uq_mrr` ON `access_permission` (`access_matrix_id` ASC, `role_id` ASC, `rights_id` ASC) ;

CREATE INDEX `fk_permission_right` ON `access_permission` (`rights_id` ASC) ;


-- -----------------------------------------------------
-- Table `view_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `view_permission` ;

CREATE  TABLE IF NOT EXISTS `view_permission` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `view_matrix_id` INT NOT NULL ,
  `subject_role_id` INT NOT NULL ,
  `target_role_id` INT NOT NULL ,
  `permission` TINYINT NOT NULL DEFAULT false COMMENT '0 - none\n1 - limited\n2 - full' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_view_permission_view_matrix1`
    FOREIGN KEY (`view_matrix_id` )
    REFERENCES `view_matrix` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_view_permission_role1`
    FOREIGN KEY (`subject_role_id` )
    REFERENCES `role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_view_permission_role2`
    FOREIGN KEY (`target_role_id` )
    REFERENCES `role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM;

CREATE INDEX `fk_view_permission_view_matrix1` ON `view_permission` (`view_matrix_id` ASC) ;

CREATE INDEX `fk_view_permission_role1` ON `view_permission` (`subject_role_id` ASC) ;

CREATE INDEX `fk_view_permission_role2` ON `view_permission` (`target_role_id` ASC) ;

CREATE UNIQUE INDEX `uq_vst` ON `view_permission` (`view_matrix_id` ASC, `subject_role_id` ASC, `target_role_id` ASC) ;


-- -----------------------------------------------------
-- Table `project_contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_contact` ;

CREATE  TABLE IF NOT EXISTS `project_contact` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `project_id` INT NOT NULL COMMENT 'ID of the project' ,
  `user_id` INT NOT NULL COMMENT 'UID of the user who will be a contact person in the project' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_project_contact_project1`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_contact_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM
COMMENT = 'This table stores users who are to be the contact people of a project';

CREATE INDEX `fk_project_contact_project1` ON `project_contact` (`project_id` ASC) ;

CREATE INDEX `fk_project_contact_user1` ON `project_contact` (`user_id` ASC) ;

CREATE UNIQUE INDEX `uq_fc` ON `project_contact` (`project_id` ASC, `user_id` ASC) ;


-- -----------------------------------------------------
-- Table `message`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `message` ;

CREATE  TABLE IF NOT EXISTS `message` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `msgboard_id` INT NOT NULL COMMENT 'ID of the msgboard the msg is posted to' ,
  `publishable` TINYINT(1)  NULL DEFAULT false COMMENT 'Whether this msg is publishable' ,
  `author_user_id` INT NOT NULL DEFAULT 0 COMMENT 'uid of the auther. 0 means system generated' ,
  `created_time` DATETIME NOT NULL ,
  `title` VARCHAR(255) NULL ,
  `body` TEXT NULL ,
  `enhancer_user_id` INT NULL COMMENT 'UID of the user who enhanced this msg' ,
  `enhance_time` DATETIME NULL COMMENT 'Time at which the user enhanced the msg' ,
  `enhance_body` TEXT NULL COMMENT 'Enhanced title by the enhancer' ,
  `enhance_title` VARCHAR(255) NULL COMMENT 'Enhanced title by the enhancer' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_message_messageboard1`
    FOREIGN KEY (`msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_message_author`
    FOREIGN KEY (`author_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_message_enhancer`
    FOREIGN KEY (`enhancer_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'A message is posted to a message board.';

CREATE INDEX `fk_message_messageboard1` ON `message` (`msgboard_id` ASC) ;

CREATE INDEX `fk_message_author` ON `message` (`author_user_id` ASC) ;

CREATE INDEX `fk_message_enhancer` ON `message` (`enhancer_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `project_roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_roles` ;

CREATE  TABLE IF NOT EXISTS `project_roles` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `project_id` INT NOT NULL COMMENT 'ID of the project' ,
  `role_id` INT NOT NULL COMMENT 'ID of the role to be included into this project' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_project_roles_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_project_roles_project1`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM
COMMENT = 'Which roles apply to this project. The system could define many roles, but only a subset of roles may be applicable to a project';

CREATE INDEX `fk_project_roles_role1` ON `project_roles` (`role_id` ASC) ;

CREATE INDEX `fk_project_roles_project1` ON `project_roles` (`project_id` ASC) ;

CREATE UNIQUE INDEX `uq_proj_role` ON `project_roles` (`role_id` ASC, `project_id` ASC) ;


-- -----------------------------------------------------
-- Table `target`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `target` ;

CREATE  TABLE IF NOT EXISTS `target` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `target_type` TINYINT NOT NULL COMMENT '0 - region\nmore to follow' ,
  `short_name` VARCHAR(45) NOT NULL COMMENT 'Short name of the target. ' ,
  `gid` VARCHAR(255) NULL COMMENT 'global id of the target. typically a uri.' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Unique definition for each country/region';

CREATE UNIQUE INDEX `name_UNIQUE` ON `target` (`name` ASC) ;

CREATE UNIQUE INDEX `short_name_UNIQUE` ON `target` (`short_name` ASC) ;


-- -----------------------------------------------------
-- Table `workflow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `workflow` ;

CREATE  TABLE IF NOT EXISTS `workflow` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `created_time` DATETIME NOT NULL ,
  `created_by_user_id` INT NOT NULL ,
  `total_duration` INT NULL COMMENT 'Total length (number of days) for the workflow to complete ' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_workflow_user1`
    FOREIGN KEY (`created_by_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM
COMMENT = 'Structure of workflow definition';

CREATE UNIQUE INDEX `name_UNIQUE` ON `workflow` (`name` ASC) ;

CREATE INDEX `fk_workflow_user1` ON `workflow` (`created_by_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_config` ;

CREATE  TABLE IF NOT EXISTS `survey_config` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL COMMENT 'Unique name of the config' ,
  `description` VARCHAR(255) NULL ,
  `instructions` TEXT NOT NULL COMMENT 'ID of the text resource used as user instructions' ,
  `moe_algorithm` TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Each product must have a config for its content type. This table saves configurations of survey products. Survey config includes survey element organizations described thru survey_element_propertries table.';

CREATE UNIQUE INDEX `name_UNIQUE` ON `survey_config` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `workflow_sequence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `workflow_sequence` ;

CREATE  TABLE IF NOT EXISTS `workflow_sequence` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'sequence starts from 1 for each workflow' ,
  `workflow_id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_workflow_sequence_workflow1`
    FOREIGN KEY (`workflow_id` )
    REFERENCES `workflow` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM
COMMENT = 'each workflow can have multiple sequence';

CREATE INDEX `fk_workflow_sequence_workflow1` ON `workflow_sequence` (`workflow_id` ASC) ;


-- -----------------------------------------------------
-- Table `product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `product` ;

CREATE  TABLE IF NOT EXISTS `product` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workflow_id` INT NOT NULL COMMENT 'ID of the workflow that drives the product creation' ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `project_id` INT NOT NULL COMMENT 'ID of the project the product belongs to' ,
  `access_matrix_id` INT NULL ,
  `product_config_id` INT NOT NULL COMMENT 'ID of the product config, either journal_config or survey_config, depending on the content_type' ,
  `content_type` TINYINT NOT NULL COMMENT '0 - survey\n1 - journal' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_prod_proj`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prod_matrix`
    FOREIGN KEY (`access_matrix_id` )
    REFERENCES `access_matrix` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prod_wfd`
    FOREIGN KEY (`workflow_id` )
    REFERENCES `workflow` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_prod_proj` ON `product` (`project_id` ASC) ;

CREATE INDEX `fk_prod_matrix` ON `product` (`access_matrix_id` ASC) ;

CREATE INDEX `fk_prod_wfd` ON `product` (`workflow_id` ASC) ;

CREATE UNIQUE INDEX `name_UNIQUE` ON `product` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `workflow_object`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `workflow_object` ;

CREATE  TABLE IF NOT EXISTS `workflow_object` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workflow_id` INT NOT NULL ,
  `start_time` DATETIME NULL ,
  `status` INT NOT NULL COMMENT '1 - waiting\n2 - started\n3 - completed\n4 - suspended\n5 - cancelled' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_wfi_workflow`
    FOREIGN KEY (`workflow_id` )
    REFERENCES `workflow` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_wfi_workflow` ON `workflow_object` (`workflow_id` ASC) ;


-- -----------------------------------------------------
-- Table `content_header`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `content_header` ;

CREATE  TABLE IF NOT EXISTS `content_header` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `project_id` INT NOT NULL COMMENT 'ID of the project that the content belongs to' ,
  `content_type` INT NOT NULL COMMENT 'Content type:\n\n0 - survey\n1 - journal' ,
  `content_object_id` INT NOT NULL COMMENT 'ID of the content object in the appropriate content object table: survey_content_object or journal_content_object' ,
  `horse_id` INT NOT NULL COMMENT 'ID of the horse that the content belongs to' ,
  `title` VARCHAR(255) NOT NULL COMMENT 'Title of the content' ,
  `author_user_id` INT NULL ,
  `create_time` DATETIME NOT NULL COMMENT 'Create time of the content' ,
  `last_update_time` DATETIME NULL COMMENT 'Last update time of the content' ,
  `last_update_user_id` INT NULL ,
  `delete_time` DATETIME NULL ,
  `deleted_by_user_id` INT NULL ,
  `status` TINYINT NOT NULL COMMENT 'Status of the content\n0 - in-flight\n1 - locked\n2 - deleted\n3 - completed\n4 - published' ,
  `internal_msgboard_id` INT NULL COMMENT 'ID of the message board used for internal staff discussions about this content' ,
  `staff_author_msgboard_id` INT NULL COMMENT 'ID of the message board used for staff/author discussions about this content' ,
  `editable` TINYINT(1)  NULL ,
  `reviewable` TINYINT(1)  NULL ,
  `peer_reviewable` TINYINT(1)  NULL ,
  `approvable` TINYINT(1)  NULL ,
  `submit_time` DATETIME NULL COMMENT 'when the content was submitted' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_content_header_user1`
    FOREIGN KEY (`author_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_content_header_messageboard1`
    FOREIGN KEY (`internal_msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ch_mb2`
    FOREIGN KEY (`staff_author_msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ch_horse`
    FOREIGN KEY (`horse_id` )
    REFERENCES `horse` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ch_deletedby`
    FOREIGN KEY (`deleted_by_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ch_lastupdate_user`
    FOREIGN KEY (`last_update_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'defines the common attri of content instances of all types';

CREATE INDEX `fk_content_header_user1` ON `content_header` (`author_user_id` ASC) ;

CREATE INDEX `fk_content_header_messageboard1` ON `content_header` (`internal_msgboard_id` ASC) ;

CREATE INDEX `fk_ch_mb2` ON `content_header` (`staff_author_msgboard_id` ASC) ;

CREATE INDEX `fk_ch_horse` ON `content_header` (`horse_id` ASC) ;

CREATE INDEX `fk_ch_deletedby` ON `content_header` (`deleted_by_user_id` ASC) ;

CREATE INDEX `fk_ch_lastupdate_user` ON `content_header` (`last_update_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `horse`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horse` ;

CREATE  TABLE IF NOT EXISTS `horse` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `product_id` INT NOT NULL COMMENT 'ID of the product' ,
  `target_id` INT NOT NULL COMMENT 'ID of the target' ,
  `start_time` DATETIME NULL COMMENT 'When the process was started' ,
  `completion_time` DATETIME NULL COMMENT 'When the process was completed' ,
  `content_header_id` INT NOT NULL ,
  `workflow_object_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_horse_target1`
    FOREIGN KEY (`target_id` )
    REFERENCES `target` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_horse_prod`
    FOREIGN KEY (`product_id` )
    REFERENCES `product` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_horse_workflow_object1`
    FOREIGN KEY (`workflow_object_id` )
    REFERENCES `workflow_object` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_horse_content`
    FOREIGN KEY (`content_header_id` )
    REFERENCES `content_header` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Each target/product produces a horse. The horse keeps state of the product creation process.';

CREATE INDEX `fk_horse_target1` ON `horse` (`target_id` ASC) ;

CREATE UNIQUE INDEX `uq_horse_target_prod` ON `horse` (`product_id` ASC, `target_id` ASC) ;

CREATE INDEX `fk_horse_prod` ON `horse` (`product_id` ASC) ;

CREATE INDEX `fk_horse_workflow_object1` ON `horse` (`workflow_object_id` ASC) ;

CREATE INDEX `fk_horse_content` ON `horse` (`content_header_id` ASC) ;


-- -----------------------------------------------------
-- Table `attachment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `attachment` ;

CREATE  TABLE IF NOT EXISTS `attachment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content_header_id` INT NOT NULL COMMENT 'ID of the content that the file is attached to' ,
  `name` VARCHAR(255) NOT NULL COMMENT 'Name of the attached file' ,
  `size` INT NOT NULL DEFAULT 0 COMMENT 'file size (bytes)' ,
  `type` VARCHAR(8) NOT NULL COMMENT 'file type (suffix, i.e. txt, doc, pdf, etc.)' ,
  `note` VARCHAR(255) NULL COMMENT 'Any notes to be attached' ,
  `file_path` VARCHAR(255) NOT NULL COMMENT 'Path to the attached file in the file system after uploaded' ,
  `update_time` TIMESTAMP NOT NULL COMMENT 'the file upload time' ,
  `user_id` INT NULL DEFAULT 0 COMMENT 'user who uploaded the file' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_attachment_content_header1`
    FOREIGN KEY (`content_header_id` )
    REFERENCES `content_header` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_attachment_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'defines file attachment.';

CREATE INDEX `fk_attachment_content_header1` ON `attachment` (`content_header_id` ASC) ;

CREATE INDEX `fk_attachment_user` ON `attachment` (`user_id` ASC) ;


-- -----------------------------------------------------
-- Table `rule`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rule` ;

CREATE  TABLE IF NOT EXISTS `rule` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL COMMENT 'Unique name of the rule' ,
  `rule_type` TINYINT NULL ,
  `file_name` VARCHAR(216) NULL ,
  `description` TEXT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table stores predefined biz rules for either validation or workflow goal execution.';

CREATE UNIQUE INDEX `name_UNIQUE` ON `rule` (`name` ASC) ;

CREATE UNIQUE INDEX `file_name_UNIQUE` ON `rule` (`file_name` ASC) ;

CREATE UNIQUE INDEX `id_UNIQUE` ON `rule` (`id` ASC) ;


-- -----------------------------------------------------
-- Table `goal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `goal` ;

CREATE  TABLE IF NOT EXISTS `goal` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workflow_sequence_id` INT NOT NULL COMMENT 'ID of the sequence that the goal belongs to' ,
  `weight` INT NOT NULL COMMENT 'Weight of the goal in the sequence. Lighter goals go first.' ,
  `name` VARCHAR(45) NOT NULL COMMENT 'Unique name of the goal' ,
  `description` VARCHAR(255) NULL ,
  `access_matrix_id` INT NOT NULL COMMENT 'ID of the access matrix used within the sscope of the goal' ,
  `duration` INT NOT NULL COMMENT 'How many days allowed to complete this goal' ,
  `entrance_rule_file_name` VARCHAR(255) NULL COMMENT 'ID of the entrance rule set' ,
  `inflight_rule_file_name` VARCHAR(255) NULL COMMENT 'ID of the inflight rule ' ,
  `exit_rule_file_name` VARCHAR(255) NULL COMMENT 'ID of the exit rule' ,
  `entrance_rule_desc` TEXT NOT NULL ,
  `inflight_rule_desc` TEXT NOT NULL ,
  `exit_rule_desc` TEXT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_goal_workflow_sequence1`
    FOREIGN KEY (`workflow_sequence_id` )
    REFERENCES `workflow_sequence` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_goal_access_matrix1`
    FOREIGN KEY (`access_matrix_id` )
    REFERENCES `access_matrix` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_goal_entrance_rule`
    FOREIGN KEY (`entrance_rule_file_name` )
    REFERENCES `rule` (`file_name` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_goal_inflight_rule`
    FOREIGN KEY (`inflight_rule_file_name` )
    REFERENCES `rule` (`file_name` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_goal_exit_rule`
    FOREIGN KEY (`exit_rule_file_name` )
    REFERENCES `rule` (`file_name` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'A goal is a milestone in a project\'s workflow. This table defines structure of goals. By allowing a goal to be included into different workflow sequences, goals become reusable in different workflows. ';

CREATE INDEX `fk_goal_workflow_sequence1` ON `goal` (`workflow_sequence_id` ASC) ;

CREATE INDEX `fk_goal_access_matrix1` ON `goal` (`access_matrix_id` ASC) ;

CREATE INDEX `fk_goal_entrance_rule` ON `goal` (`entrance_rule_file_name` ASC) ;

CREATE INDEX `fk_goal_inflight_rule` ON `goal` (`inflight_rule_file_name` ASC) ;

CREATE INDEX `fk_goal_exit_rule` ON `goal` (`exit_rule_file_name` ASC) ;

CREATE UNIQUE INDEX `uq_goal_name_seq` ON `goal` (`workflow_sequence_id` ASC, `name` ASC) ;


-- -----------------------------------------------------
-- Table `sequence_object`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sequence_object` ;

CREATE  TABLE IF NOT EXISTS `sequence_object` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID of the SO' ,
  `workflow_object_id` INT NOT NULL COMMENT 'ID of the WFO that this SO belongs to' ,
  `workflow_sequence_id` INT NOT NULL COMMENT 'The ID of the workflow sequence that this SO is instantiated from' ,
  `status` TINYINT NOT NULL COMMENT '0 - waiting\n1 - started\n2 - done' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_sequence_object_workflow_sequence1`
    FOREIGN KEY (`workflow_sequence_id` )
    REFERENCES `workflow_sequence` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sequence_object_wfi_id`
    FOREIGN KEY (`workflow_object_id` )
    REFERENCES `workflow_object` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'A Sequence Object (SO) is instantiated from workflow sequence definition. It is used to record sequence state data during the workflow execution for a horse';

CREATE INDEX `fk_sequence_object_workflow_sequence1` ON `sequence_object` (`workflow_sequence_id` ASC) ;

CREATE INDEX `fk_sequence_object_wfi_id` ON `sequence_object` (`workflow_object_id` ASC) ;


-- -----------------------------------------------------
-- Table `event_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event_log` ;

CREATE  TABLE IF NOT EXISTS `event_log` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the log book' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Event log is used to record events that happened during workflow execution';


-- -----------------------------------------------------
-- Table `goal_object`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `goal_object` ;

CREATE  TABLE IF NOT EXISTS `goal_object` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `goal_id` INT NOT NULL COMMENT 'ID of the goal that this GO is instantiated from' ,
  `enter_time` DATETIME NULL COMMENT 'When this goal is started (entered)' ,
  `exit_time` DATETIME NULL COMMENT 'When the goal was ended (exited)' ,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Goal execution status:\n\n0 - waiting\n1 - starting\n2 - started\n3 - done\n' ,
  `sequence_object_id` INT NOT NULL COMMENT 'ID of the sequence object (SO) that the GO belongs to' ,
  `event_log_id` INT NULL COMMENT 'ID of the event log used to record events that occurred during this GO execution' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_goal_object_goal1`
    FOREIGN KEY (`goal_id` )
    REFERENCES `goal` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_goal_object_seq`
    FOREIGN KEY (`sequence_object_id` )
    REFERENCES `sequence_object` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_goal_object_log`
    FOREIGN KEY (`event_log_id` )
    REFERENCES `event_log` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Goal Object is instantiated from a Goal Definition and is used to record the execution state of the goal in a Workflow Object (WFO).';

CREATE INDEX `fk_goal_object_goal1` ON `goal_object` (`goal_id` ASC) ;

CREATE INDEX `fk_goal_object_seq` ON `goal_object` (`sequence_object_id` ASC) ;

CREATE UNIQUE INDEX `uq_seq_goal` ON `goal_object` (`goal_id` ASC, `sequence_object_id` ASC) ;

CREATE INDEX `fk_goal_object_log` ON `goal_object` (`event_log_id` ASC) ;


-- -----------------------------------------------------
-- Table `reference`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reference` ;

CREATE  TABLE IF NOT EXISTS `reference` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` TEXT NOT NULL ,
  `choice_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0 - no choice\n1 - single choice\n2 - multi choice' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'predefined to be used to provide standardized Reference requirements to the user for a given indicator';

CREATE UNIQUE INDEX `name_UNIQUE` ON `reference` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `survey_indicator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_indicator` ;

CREATE  TABLE IF NOT EXISTS `survey_indicator` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `question` TEXT NOT NULL ,
  `answer_type` TINYINT NOT NULL COMMENT '1 - single choice\n2 - multi choice\n3 - integer\n4 - float\n5 - text\n\n' ,
  `answer_type_id` INT NOT NULL COMMENT 'ID of the type definition in answer_type_choice, answer_type_number, or answer_type_text table' ,
  `reference_id` INT NOT NULL ,
  `tip` TEXT NULL COMMENT 'Additional tip text' ,
  `create_user_id` INT NULL ,
  `create_time` DATETIME NULL ,
  `reusable` TINYINT(1)  NULL ,
  `original_indicator_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_survey_indicator_document_type1`
    FOREIGN KEY (`reference_id` )
    REFERENCES `reference` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_survey_user`
    FOREIGN KEY (`create_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_survey_original_ind`
    FOREIGN KEY (`original_indicator_id` )
    REFERENCES `survey_indicator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Survey Element structure defines the header info of the indicator. Survey Group doesnt need additional info – the Survey Element structure is sufficient for group';

CREATE INDEX `fk_survey_indicator_document_type1` ON `survey_indicator` (`reference_id` ASC) ;

CREATE INDEX `fk_survey_user` ON `survey_indicator` (`create_user_id` ASC) ;

CREATE INDEX `fk_survey_original_ind` ON `survey_indicator` (`original_indicator_id` ASC) ;

CREATE UNIQUE INDEX `name_UNIQUE` ON `survey_indicator` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `survey_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_category` ;

CREATE  TABLE IF NOT EXISTS `survey_category` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `survey_config_id` INT NOT NULL ,
  `parent_category_id` INT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(225) NOT NULL ,
  `label` VARCHAR(45) NOT NULL COMMENT 'Displayed before the title text' ,
  `title` TEXT NULL ,
  `weight` INT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_sc_parent`
    FOREIGN KEY (`parent_category_id` )
    REFERENCES `survey_category` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sc_config`
    FOREIGN KEY (`survey_config_id` )
    REFERENCES `survey_config` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_sc_parent` ON `survey_category` (`parent_category_id` ASC) ;

CREATE INDEX `fk_sc_config` ON `survey_category` (`survey_config_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_question`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_question` ;

CREATE  TABLE IF NOT EXISTS `survey_question` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `survey_config_id` INT NOT NULL ,
  `survey_indicator_id` INT NOT NULL ,
  `survey_category_id` INT NOT NULL DEFAULT 0 COMMENT '0 if this element is the root (no parent)' ,
  `public_name` VARCHAR(45) NOT NULL ,
  `default_answer_id` INT NULL COMMENT 'The ID of default answer object: answer_choice_object, answer_number_object, or answer_text_object' ,
  `weight` INT NULL COMMENT 'used to determine display order of the element in the survey' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_survey_item_config`
    FOREIGN KEY (`survey_config_id` )
    REFERENCES `survey_config` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_survey_item_indicator`
    FOREIGN KEY (`survey_indicator_id` )
    REFERENCES `survey_indicator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_survey_item_category`
    FOREIGN KEY (`survey_category_id` )
    REFERENCES `survey_category` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'associate indicator with survey category\n';

CREATE INDEX `fk_survey_item_config` ON `survey_question` (`survey_config_id` ASC) ;

CREATE UNIQUE INDEX `uq_sce` ON `survey_question` (`survey_config_id` ASC, `survey_indicator_id` ASC) ;

CREATE INDEX `fk_survey_item_indicator` ON `survey_question` (`survey_indicator_id` ASC) ;

CREATE INDEX `fk_survey_item_category` ON `survey_question` (`survey_category_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_content_object`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_content_object` ;

CREATE  TABLE IF NOT EXISTS `survey_content_object` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the SCO' ,
  `content_header_id` INT NOT NULL COMMENT 'ID of the content header that keeps common attributes for this SCO.' ,
  `survey_config_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_sco_ch`
    FOREIGN KEY (`content_header_id` )
    REFERENCES `content_header` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sco_config`
    FOREIGN KEY (`survey_config_id` )
    REFERENCES `survey_config` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This table keeps all created suvey content objects (SCO). A SCO has multiple indicator values. Actual indicator values of the SCO are saved in survey_element_object table.';

CREATE INDEX `fk_sco_ch` ON `survey_content_object` (`content_header_id` ASC) ;

CREATE UNIQUE INDEX `content_header_id_UNIQUE` ON `survey_content_object` (`content_header_id` ASC) ;

CREATE INDEX `fk_sco_config` ON `survey_content_object` (`survey_config_id` ASC) ;


-- -----------------------------------------------------
-- Table `reference_object`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reference_object` ;

CREATE  TABLE IF NOT EXISTS `reference_object` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `reference_id` INT NOT NULL ,
  `source_description` TEXT NOT NULL ,
  `comments` TEXT NULL ,
  `choices` BIGINT UNSIGNED NULL COMMENT 'Bit mask that contains all selected choices' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ro_ref`
    FOREIGN KEY (`reference_id` )
    REFERENCES `reference` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_ro_ref` ON `reference_object` (`reference_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_answer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_answer` ;

CREATE  TABLE IF NOT EXISTS `survey_answer` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `survey_content_object_id` INT NOT NULL ,
  `survey_question_id` INT NOT NULL ,
  `answer_object_id` INT NULL COMMENT 'ID of the answer object in answer_choice_object, answer_number_object or answer_text_object, depending on answer type' ,
  `reference_object_id` INT NULL ,
  `comments` TEXT NULL ,
  `answer_time` DATETIME NULL ,
  `answer_user_id` INT NULL ,
  `internal_msgboard_id` INT NULL ,
  `staff_author_msgboard_id` INT NULL ,
  `reviewer_has_problem` TINYINT(1)  NOT NULL DEFAULT 0 ,
  `problem_submit_time` DATETIME NULL COMMENT 'Time at which the problem is last submitted by reviewer' ,
  `problem_resp_time` DATETIME NULL COMMENT 'Time at which  the author provided last response' ,
  `staff_reviewed` TINYINT(1)  NOT NULL DEFAULT 0 ,
  `edited` TINYINT(1)  NOT NULL DEFAULT 0 ,
  `pr_reviewed` TINYINT(1)  NOT NULL DEFAULT 0 ,
  `overall_reviewed` TINYINT(1)  NOT NULL DEFAULT 0 ,
  `author_responded` TINYINT(1)  NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_sa_co`
    FOREIGN KEY (`survey_content_object_id` )
    REFERENCES `survey_content_object` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sa_internal_msgboard`
    FOREIGN KEY (`internal_msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sa_uid`
    FOREIGN KEY (`answer_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sa_question`
    FOREIGN KEY (`survey_question_id` )
    REFERENCES `survey_question` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sa_sad_msgboard`
    FOREIGN KEY (`staff_author_msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sa_ref`
    FOREIGN KEY (`reference_object_id` )
    REFERENCES `reference_object` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_sa_co` ON `survey_answer` (`survey_content_object_id` ASC) ;

CREATE INDEX `fk_sa_internal_msgboard` ON `survey_answer` (`internal_msgboard_id` ASC) ;

CREATE INDEX `fk_sa_uid` ON `survey_answer` (`answer_user_id` ASC) ;

CREATE INDEX `fk_sa_question` ON `survey_answer` (`survey_question_id` ASC) ;

CREATE INDEX `fk_sa_sad_msgboard` ON `survey_answer` (`staff_author_msgboard_id` ASC) ;

CREATE INDEX `fk_sa_ref` ON `survey_answer` (`reference_object_id` ASC) ;

CREATE UNIQUE INDEX `uq_sa_co_qst` ON `survey_answer` (`survey_content_object_id` ASC, `survey_question_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_peer_review`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_peer_review` ;

CREATE  TABLE IF NOT EXISTS `survey_peer_review` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `survey_answer_id` INT NOT NULL ,
  `opinion` TINYINT NOT NULL COMMENT '0 - agree\n1 - agree with comments\n2 - disagree\n3 - not qualified' ,
  `suggested_answer_object_id` INT NULL COMMENT 'Referenced table depends answer type: answer_object_choice, number, or text' ,
  `comments` TEXT NULL ,
  `last_change_time` DATETIME NOT NULL ,
  `reviewer_user_id` INT NOT NULL COMMENT 'uid who created this review' ,
  `msgboard_id` INT NOT NULL ,
  `submit_time` DATETIME NULL COMMENT 'When the peer review was submitted' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_survey_peer_review_user1`
    FOREIGN KEY (`reviewer_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_survey_peer_review_messageboard1`
    FOREIGN KEY (`msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_spr_answer`
    FOREIGN KEY (`survey_answer_id` )
    REFERENCES `survey_answer` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM;

CREATE INDEX `fk_survey_peer_review_user1` ON `survey_peer_review` (`reviewer_user_id` ASC) ;

CREATE INDEX `fk_survey_peer_review_messageboard1` ON `survey_peer_review` (`msgboard_id` ASC) ;

CREATE INDEX `fk_spr_answer` ON `survey_peer_review` (`survey_answer_id` ASC) ;

CREATE UNIQUE INDEX `uq_spr_answer_user` ON `survey_peer_review` (`survey_answer_id` ASC, `reviewer_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tag` ;

CREATE  TABLE IF NOT EXISTS `tag` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `tag_type` TINYINT NOT NULL COMMENT '0 - user tag\n1 - system tag' ,
  `tagged_object_type` SMALLINT NOT NULL COMMENT 'Object type' ,
  `tagged_object_id` INT NOT NULL ,
  `tagged_object_scope_id` INT NULL COMMENT 'ID of the object that defines the scope of the tagged object' ,
  `tagging_time` DATETIME NOT NULL ,
  `user_id` INT NOT NULL COMMENT 'user who applied the tag' ,
  `label` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_tag_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM;

CREATE INDEX `fk_tag_user1` ON `tag` (`user_id` ASC) ;

CREATE UNIQUE INDEX `uq_tag` ON `tag` (`tagged_object_type` ASC, `tagged_object_id` ASC, `label` ASC) ;


-- -----------------------------------------------------
-- Table `journal_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `journal_config` ;

CREATE  TABLE IF NOT EXISTS `journal_config` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `instructions` TEXT NOT NULL COMMENT 'ID of the instruction text for users' ,
  `min_words` INT NOT NULL ,
  `max_words` INT NOT NULL ,
  `exportable_items` TINYINT NOT NULL DEFAULT 0 COMMENT '0 - content and attachments\n1 - content only\n2 - attachments only' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Journal Config defines configuration infor for a journal product. ';


-- -----------------------------------------------------
-- Table `journal_content_object`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `journal_content_object` ;

CREATE  TABLE IF NOT EXISTS `journal_content_object` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the JCO' ,
  `content_header_id` INT NOT NULL COMMENT 'ID of the content header that contains common attributes for this JCO' ,
  `body` TEXT NULL COMMENT 'Content body of this JCO' ,
  `journal_config_id` INT NOT NULL COMMENT 'ID of the journal config' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_journal_content_object_content_header1`
    FOREIGN KEY (`content_header_id` )
    REFERENCES `content_header` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_jco_config`
    FOREIGN KEY (`journal_config_id` )
    REFERENCES `journal_config` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Defines journal specific content object structure (JCO). Common attributes are defined in content_header structure.';

CREATE INDEX `fk_journal_content_object_content_header1` ON `journal_content_object` (`content_header_id` ASC) ;

CREATE UNIQUE INDEX `content_header_id_UNIQUE` ON `journal_content_object` (`content_header_id` ASC) ;

CREATE INDEX `fk_jco_config` ON `journal_content_object` (`journal_config_id` ASC) ;


-- -----------------------------------------------------
-- Table `cases`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cases` ;

CREATE  TABLE IF NOT EXISTS `cases` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `opened_by_user_id` INT NOT NULL COMMENT 'opened by this user' ,
  `assigned_user_id` INT NULL COMMENT 'User assigned to be responsible' ,
  `opened_time` DATETIME NOT NULL ,
  `title` VARCHAR(255) NOT NULL ,
  `description` TEXT NULL ,
  `priority` TINYINT NOT NULL DEFAULT 0 COMMENT '1 - low\n2 - medium\n3 - high' ,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Status code of the case.\n0 - open\n1 - closed' ,
  `substatus` TINYINT NULL COMMENT 'Substatus code. \nWhen status is open:\n1 - New\n2 - Assigned\n\nWhen status is closed\n101 - Resolved\n102 - Invalid\n103 - Withdrawn\n104 - Duplicate' ,
  `block_workflow` TINYINT(1)  NULL DEFAULT 0 COMMENT 'whether this case blocks workflow from being executed\n0 - no\n1 - yes' ,
  `block_publishing` TINYINT(1)  NULL DEFAULT 0 COMMENT 'whether this case blocks publishing\n0 - no\n1 - yes' ,
  `project_id` INT NULL ,
  `product_id` INT NULL ,
  `horse_id` INT NULL ,
  `goal_id` INT NULL ,
  `staff_msgboard_id` INT NULL ,
  `user_msgboard_id` INT NULL ,
  `last_updated_time` DATETIME NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_case_user1`
    FOREIGN KEY (`opened_by_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_case_project1`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_case_staff_mb`
    FOREIGN KEY (`staff_msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_case_user_mb`
    FOREIGN KEY (`user_msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_case_product`
    FOREIGN KEY (`product_id` )
    REFERENCES `product` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_case_horse`
    FOREIGN KEY (`horse_id` )
    REFERENCES `horse` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_case_goal`
    FOREIGN KEY (`goal_id` )
    REFERENCES `goal` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_case_user1` ON `cases` (`opened_by_user_id` ASC) ;

CREATE INDEX `fk_case_project1` ON `cases` (`project_id` ASC) ;

CREATE INDEX `fk_case_staff_mb` ON `cases` (`staff_msgboard_id` ASC) ;

CREATE INDEX `fk_case_user_mb` ON `cases` (`user_msgboard_id` ASC) ;

CREATE INDEX `fk_case_product` ON `cases` (`product_id` ASC) ;

CREATE INDEX `fk_case_horse` ON `cases` (`horse_id` ASC) ;

CREATE INDEX `fk_case_goal` ON `cases` (`goal_id` ASC) ;


-- -----------------------------------------------------
-- Table `announcement`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `announcement` ;

CREATE  TABLE IF NOT EXISTS `announcement` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(255) NOT NULL COMMENT 'Title of the announcement' ,
  `body` TEXT NULL COMMENT 'Announcement body' ,
  `created_by_user_id` INT NOT NULL COMMENT 'ID of user who created this announcement' ,
  `created_time` DATETIME NOT NULL COMMENT 'Time at which the announcement was created' ,
  `expiration` DATETIME NOT NULL COMMENT 'When this announcement will expire' ,
  `active` TINYINT(1)  NOT NULL DEFAULT 0 COMMENT 'Whether the announcement is active\n\n0 - inactive\n1 - active' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_announcement_user1`
    FOREIGN KEY (`created_by_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = MyISAM;

CREATE INDEX `fk_announcement_user1` ON `announcement` (`created_by_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `case_object`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `case_object` ;

CREATE  TABLE IF NOT EXISTS `case_object` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `cases_id` INT NOT NULL COMMENT 'ID of the case that the object is attached to' ,
  `object_type` TINYINT NOT NULL COMMENT 'Only two kinds of objects for now:\n\n0 - user\n1 - content' ,
  `object_id` INT NOT NULL COMMENT 'The ID of the attached object, either content_header_id or uid, depending on the object_type.' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_co_case`
    FOREIGN KEY (`cases_id` )
    REFERENCES `cases` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Multiple objects could be associated with a case.';

CREATE UNIQUE INDEX `uq_cto` ON `case_object` (`cases_id` ASC, `object_type` ASC, `object_id` ASC) ;

CREATE INDEX `fk_co_case` ON `case_object` (`cases_id` ASC) ;


-- -----------------------------------------------------
-- Table `notification_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `notification_type` ;

CREATE  TABLE IF NOT EXISTS `notification_type` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Stores predefined notification types';


-- -----------------------------------------------------
-- Table `project_target`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_target` ;

CREATE  TABLE IF NOT EXISTS `project_target` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `project_id` INT NOT NULL COMMENT 'ID of the project' ,
  `target_id` INT NOT NULL COMMENT 'ID of the target to be included into the project' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_project`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_target`
    FOREIGN KEY (`target_id` )
    REFERENCES `target` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'The system could define many targets, but only a subset of them are applicable to a project. This table stores what targets are applicable to what projects';

CREATE INDEX `fk_project` ON `project_target` (`project_id` ASC) ;

CREATE INDEX `fk_target` ON `project_target` (`target_id` ASC) ;

CREATE UNIQUE INDEX `uq_pt` ON `project_target` (`project_id` ASC, `target_id` ASC) ;


-- -----------------------------------------------------
-- Table `tool`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tool` ;

CREATE  TABLE IF NOT EXISTS `tool` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL COMMENT 'Technical name used by system code' ,
  `label` VARCHAR(45) NOT NULL COMMENT 'Label is displayed to end users' ,
  `description` VARCHAR(255) NULL ,
  `access_matrix_id` INT NULL COMMENT 'ID of the access matrix that configures the tool' ,
  `action` VARCHAR(32) NULL ,
  `task_type` TINYINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_tool_config_matrix`
    FOREIGN KEY (`access_matrix_id` )
    REFERENCES `access_matrix` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE UNIQUE INDEX `name_UNIQUE` ON `tool` (`name` ASC) ;

CREATE INDEX `fk_tool_config_matrix` ON `tool` (`access_matrix_id` ASC) ;


-- -----------------------------------------------------
-- Table `task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `task` ;

CREATE  TABLE IF NOT EXISTS `task` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `task_name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `goal_id` INT NOT NULL ,
  `product_id` INT NOT NULL ,
  `tool_id` INT NOT NULL ,
  `assignment_method` TINYINT NOT NULL COMMENT '1 - manual\n2 - queue\n3 - dynamic' ,
  `instructions` TEXT NOT NULL ,
  `type` TINYINT NOT NULL DEFAULT -1 COMMENT 'Task type' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_task_goal`
    FOREIGN KEY (`goal_id` )
    REFERENCES `goal` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_tool`
    FOREIGN KEY (`tool_id` )
    REFERENCES `tool` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_product`
    FOREIGN KEY (`product_id` )
    REFERENCES `product` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE UNIQUE INDEX `task_name_UNIQUE` ON `task` (`task_name` ASC, `product_id` ASC) ;

CREATE INDEX `fk_task_goal` ON `task` (`goal_id` ASC) ;

CREATE INDEX `fk_task_tool` ON `task` (`tool_id` ASC) ;

CREATE INDEX `fk_task_product` ON `task` (`product_id` ASC) ;


-- -----------------------------------------------------
-- Table `task_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `task_role` ;

CREATE  TABLE IF NOT EXISTS `task_role` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `task_id` INT NOT NULL ,
  `role_id` INT NOT NULL ,
  `can_claim` TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_task_role_task`
    FOREIGN KEY (`task_id` )
    REFERENCES `task` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_role_role`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_task_role_task` ON `task_role` (`task_id` ASC) ;

CREATE INDEX `fk_task_role_role` ON `task_role` (`role_id` ASC) ;

CREATE UNIQUE INDEX `uq_tr` ON `task_role` (`task_id` ASC, `role_id` ASC) ;


-- -----------------------------------------------------
-- Table `task_assignment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `task_assignment` ;

CREATE  TABLE IF NOT EXISTS `task_assignment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `task_id` INT NOT NULL ,
  `target_id` INT NOT NULL ,
  `assigned_user_id` INT NOT NULL ,
  `due_time` DATETIME NULL ,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 - inactive\n1 - active\n2 - aware\n3 - noticed\n4 - started\n5 - done' ,
  `start_time` DATETIME NULL ,
  `completion_time` DATETIME NULL ,
  `event_log_id` INT NULL ,
  `q_enter_time` DATETIME NULL ,
  `q_last_assigned_time` DATETIME NULL ,
  `q_last_assigned_uid` INT NULL ,
  `q_last_return_time` DATETIME NULL ,
  `q_priority` TINYINT NULL COMMENT '1 - low\n2 - medium\n3 - high' ,
  `data` TEXT NULL COMMENT 'Used by app to stored any app-processing data' ,
  `goal_object_id` INT NULL COMMENT 'Used by app to remember id of the goal object that the assignment belongs to' ,
  `percent` FLOAT NULL DEFAULT -1 COMMENT 'Percent of completion' ,
  `horse_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ta_task`
    FOREIGN KEY (`task_id` )
    REFERENCES `task` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ta_target`
    FOREIGN KEY (`target_id` )
    REFERENCES `target` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ta_assigned`
    FOREIGN KEY (`assigned_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ta_event_log`
    FOREIGN KEY (`event_log_id` )
    REFERENCES `event_log` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ta_goal_object`
    FOREIGN KEY (`goal_object_id` )
    REFERENCES `goal_object` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ta_horse`
    FOREIGN KEY (`horse_id` )
    REFERENCES `horse` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_ta_task` ON `task_assignment` (`task_id` ASC) ;

CREATE INDEX `fk_ta_target` ON `task_assignment` (`target_id` ASC) ;

CREATE UNIQUE INDEX `uq_ta_task_horse_uid` ON `task_assignment` (`task_id` ASC, `assigned_user_id` ASC, `horse_id` ASC) ;

CREATE INDEX `fk_ta_assigned` ON `task_assignment` (`assigned_user_id` ASC) ;

CREATE INDEX `fk_ta_event_log` ON `task_assignment` (`event_log_id` ASC) ;

CREATE INDEX `fk_ta_goal_object` ON `task_assignment` (`goal_object_id` ASC) ;

CREATE INDEX `fk_ta_horse` ON `task_assignment` (`horse_id` ASC) ;


-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event` ;

CREATE  TABLE IF NOT EXISTS `event` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `event_log_id` INT NOT NULL COMMENT 'ID of the log book on that the event will be recorded' ,
  `event_type` SMALLINT NOT NULL COMMENT 'Type of the event' ,
  `event_data` TEXT NULL COMMENT 'Any event specific data' ,
  `user_id` INT NULL COMMENT 'ID of the user who caused the event to happen' ,
  `timestamp` TIMESTAMP NOT NULL COMMENT 'Time at whioch the event ocurred' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_event_log_id`
    FOREIGN KEY (`event_log_id` )
    REFERENCES `event_log` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This table stores events that occured during workflow execution';

CREATE INDEX `fk_event_log_id` ON `event` (`event_log_id` ASC) ;

CREATE INDEX `fk_event_user` ON `event` (`user_id` ASC) ;


-- -----------------------------------------------------
-- Table `text_resource`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `text_resource` ;

CREATE  TABLE IF NOT EXISTS `text_resource` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `resource_name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;

CREATE UNIQUE INDEX `resource_name_UNIQUE` ON `text_resource` (`resource_name` ASC) ;


-- -----------------------------------------------------
-- Table `text_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `text_item` ;

CREATE  TABLE IF NOT EXISTS `text_item` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `text_resource_id` INT NOT NULL ,
  `language_id` INT NOT NULL ,
  `text` TEXT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ti_lang`
    FOREIGN KEY (`language_id` )
    REFERENCES `language` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ti_resource`
    FOREIGN KEY (`text_resource_id` )
    REFERENCES `text_resource` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_ti_lang` ON `text_item` (`language_id` ASC) ;

CREATE UNIQUE INDEX `uq_rlc` ON `text_item` (`text_resource_id` ASC, `language_id` ASC) ;

CREATE INDEX `fk_ti_resource` ON `text_item` (`text_resource_id` ASC) ;


-- -----------------------------------------------------
-- Table `journal_peer_review`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `journal_peer_review` ;

CREATE  TABLE IF NOT EXISTS `journal_peer_review` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `journal_content_object_id` INT NOT NULL COMMENT 'ID of the JCO that the peer review is for' ,
  `reviewer_user_id` INT NOT NULL COMMENT 'UID of the user who wrote the peer review' ,
  `opinions` TEXT NULL COMMENT 'Opinions of the peer reviewer' ,
  `last_change_time` DATETIME NULL COMMENT 'Last change time of the peer review' ,
  `msgboard_id` INT NULL COMMENT 'ID of the message board used for communication between the peer reviewer and staff members' ,
  `submit_time` DATETIME NULL COMMENT 'when the review was submitted' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_jpr_jobj`
    FOREIGN KEY (`journal_content_object_id` )
    REFERENCES `journal_content_object` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_jpr_author`
    FOREIGN KEY (`reviewer_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_jpr_msgboard`
    FOREIGN KEY (`msgboard_id` )
    REFERENCES `msgboard` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Keeps peer review data for a journal content object (JCO). A JCO could have multiple peer reviews';

CREATE INDEX `fk_jpr_jobj` ON `journal_peer_review` (`journal_content_object_id` ASC) ;

CREATE INDEX `fk_jpr_author` ON `journal_peer_review` (`reviewer_user_id` ASC) ;

CREATE INDEX `fk_jpr_msgboard` ON `journal_peer_review` (`msgboard_id` ASC) ;


-- -----------------------------------------------------
-- Table `msg_reading_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `msg_reading_status` ;

CREATE  TABLE IF NOT EXISTS `msg_reading_status` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `message_id` INT NOT NULL COMMENT 'ID of the msg read' ,
  `user_id` INT NOT NULL COMMENT 'UID of the user who read the msg' ,
  `timestamp` TIMESTAMP NOT NULL COMMENT 'Time at which the msg was read' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_mrs_uid`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mrs_msg`
    FOREIGN KEY (`message_id` )
    REFERENCES `message` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This table stores message reading status for a user ';

CREATE INDEX `fk_mrs_uid` ON `msg_reading_status` (`user_id` ASC) ;

CREATE INDEX `fk_mrs_msg` ON `msg_reading_status` (`message_id` ASC) ;

CREATE UNIQUE INDEX `uq_uid_msg` ON `msg_reading_status` (`message_id` ASC, `user_id` ASC) ;


-- -----------------------------------------------------
-- Table `pregoal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pregoal` ;

CREATE  TABLE IF NOT EXISTS `pregoal` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `workflow_sequence_id` INT NOT NULL COMMENT 'ID of the sequence ' ,
  `pre_goal_id` INT NOT NULL COMMENT 'ID of the goal that is a pre-requisite goal of the sequence' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_pg_seqid`
    FOREIGN KEY (`workflow_sequence_id` )
    REFERENCES `workflow_sequence` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pg_goal`
    FOREIGN KEY (`pre_goal_id` )
    REFERENCES `goal` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Defines pre-requisite goals that must be completed before the sequence can start. A sequence can have multiple pre-requisite goals.';

CREATE INDEX `fk_pg_seqid` ON `pregoal` (`workflow_sequence_id` ASC) ;

CREATE INDEX `fk_pg_goal` ON `pregoal` (`pre_goal_id` ASC) ;

CREATE UNIQUE INDEX `uq_seq_goal` ON `pregoal` (`workflow_sequence_id` ASC, `pre_goal_id` ASC) ;


-- -----------------------------------------------------
-- Table `ctags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ctags` ;

CREATE  TABLE IF NOT EXISTS `ctags` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `term` VARCHAR(45) NOT NULL COMMENT 'The term of the tag to be displayed' ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table keeps predefined tags that can be used to classify cases';


-- -----------------------------------------------------
-- Table `case_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `case_tag` ;

CREATE  TABLE IF NOT EXISTS `case_tag` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `cases_id` INT NOT NULL COMMENT 'ID of the case' ,
  `ctags_id` INT NOT NULL COMMENT 'ID of the ctag to be associated with the case' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ct_case`
    FOREIGN KEY (`cases_id` )
    REFERENCES `cases` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ct_tag`
    FOREIGN KEY (`ctags_id` )
    REFERENCES `ctags` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Define case/tag association';

CREATE UNIQUE INDEX `uq_ct_casetag` ON `case_tag` (`cases_id` ASC, `ctags_id` ASC) ;

CREATE INDEX `fk_ct_case` ON `case_tag` (`cases_id` ASC) ;

CREATE INDEX `fk_ct_tag` ON `case_tag` (`ctags_id` ASC) ;


-- -----------------------------------------------------
-- Table `case_attachment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `case_attachment` ;

CREATE  TABLE IF NOT EXISTS `case_attachment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `cases_id` INT NOT NULL COMMENT 'ID of the case the file is attached to' ,
  `file_name` VARCHAR(45) NOT NULL COMMENT 'Name of the attached file' ,
  `file_path` VARCHAR(255) NOT NULL COMMENT 'Path to file in the file system' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ca_case`
    FOREIGN KEY (`cases_id` )
    REFERENCES `cases` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Stores information about files attached to cases. A case could have 0 or more attached files.';

CREATE UNIQUE INDEX `uq_ca_case_file` ON `case_attachment` (`cases_id` ASC, `file_name` ASC) ;

CREATE INDEX `fk_ca_case` ON `case_attachment` (`cases_id` ASC) ;


-- -----------------------------------------------------
-- Table `answer_type_choice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_type_choice` ;

CREATE  TABLE IF NOT EXISTS `answer_type_choice` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `atc_choice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `atc_choice` ;

CREATE  TABLE IF NOT EXISTS `atc_choice` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `answer_type_choice_id` INT NOT NULL ,
  `label` VARCHAR(45) NOT NULL COMMENT 'Display label of the choice' ,
  `score` INT NULL COMMENT 'Score associated with the choice' ,
  `criteria` TEXT NULL COMMENT 'Text resource ID of the choice\'s criteria' ,
  `weight` INT NOT NULL COMMENT 'Weight that defines the display order of the choice ' ,
  `mask` BIGINT UNSIGNED NOT NULL COMMENT 'The bit mask representing this choice in the ATC' ,
  `default_selected` TINYINT(1)  NOT NULL DEFAULT false COMMENT 'Whether this choice is selected by default' ,
  `use_score` TINYINT(1)  NOT NULL DEFAULT true COMMENT 'Whether the score is applicable' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_atcc_atc`
    FOREIGN KEY (`answer_type_choice_id` )
    REFERENCES `answer_type_choice` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Choices for a ATC';

CREATE INDEX `fk_atcc_atc` ON `atc_choice` (`answer_type_choice_id` ASC) ;


-- -----------------------------------------------------
-- Table `answer_type_integer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_type_integer` ;

CREATE  TABLE IF NOT EXISTS `answer_type_integer` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `min_value` INT NOT NULL ,
  `max_value` INT NOT NULL ,
  `default_value` INT NULL ,
  `criteria` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Structure of the Number answer type of indicator';


-- -----------------------------------------------------
-- Table `answer_type_text`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_type_text` ;

CREATE  TABLE IF NOT EXISTS `answer_type_text` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `min_chars` INT NOT NULL ,
  `max_chars` INT NOT NULL ,
  `criteria` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Structure of answer type Text';


-- -----------------------------------------------------
-- Table `reference_choice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reference_choice` ;

CREATE  TABLE IF NOT EXISTS `reference_choice` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `reference_id` INT NOT NULL ,
  `label` VARCHAR(255) NOT NULL COMMENT 'Text to be displayed to the user' ,
  `weight` INT NULL COMMENT 'Display order of the choice' ,
  `mask` BIGINT UNSIGNED NOT NULL COMMENT 'Bit mask that represents the choice' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_rc_ref`
    FOREIGN KEY (`reference_id` )
    REFERENCES `reference` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'Choices for reference';

CREATE INDEX `fk_rc_ref` ON `reference_choice` (`reference_id` ASC) ;


-- -----------------------------------------------------
-- Table `answer_object_choice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_object_choice` ;

CREATE  TABLE IF NOT EXISTS `answer_object_choice` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `choices` BIGINT UNSIGNED NOT NULL COMMENT 'Bit mask that contains all selected choices. ' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `answer_object_integer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_object_integer` ;

CREATE  TABLE IF NOT EXISTS `answer_object_integer` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `value` INT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `answer_object_text`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_object_text` ;

CREATE  TABLE IF NOT EXISTS `answer_object_text` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `value` TEXT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `content_payment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `content_payment` ;

CREATE  TABLE IF NOT EXISTS `content_payment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content_header_id` INT NOT NULL ,
  `paid_by_user_id` INT NOT NULL ,
  `amount` DECIMAL(10,2) NOT NULL ,
  `time` DATETIME NOT NULL ,
  `payees` VARCHAR(255) NULL ,
  `note` TEXT NULL ,
  `task_assignment_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_cp_user`
    FOREIGN KEY (`paid_by_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cp_content`
    FOREIGN KEY (`content_header_id` )
    REFERENCES `content_header` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cp_task`
    FOREIGN KEY (`task_assignment_id` )
    REFERENCES `task_assignment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_cp_user` ON `content_payment` (`paid_by_user_id` ASC) ;

CREATE INDEX `fk_cp_content` ON `content_payment` (`content_header_id` ASC) ;

CREATE INDEX `fk_cp_task` ON `content_payment` (`task_assignment_id` ASC) ;

CREATE UNIQUE INDEX `uq_cp_ct_task` ON `content_payment` (`content_header_id` ASC, `task_assignment_id` ASC) ;


-- -----------------------------------------------------
-- Table `content_approval`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `content_approval` ;

CREATE  TABLE IF NOT EXISTS `content_approval` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content_header_id` INT NOT NULL ,
  `user_id` INT NOT NULL ,
  `time` DATETIME NOT NULL ,
  `note` TEXT NULL ,
  `task_assignment_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ca_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ca_content`
    FOREIGN KEY (`content_header_id` )
    REFERENCES `content_header` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ca_task`
    FOREIGN KEY (`task_assignment_id` )
    REFERENCES `task_assignment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_ca_user` ON `content_approval` (`user_id` ASC) ;

CREATE INDEX `fk_ca_content` ON `content_approval` (`content_header_id` ASC) ;

CREATE INDEX `fk_ca_task` ON `content_approval` (`task_assignment_id` ASC) ;

CREATE UNIQUE INDEX `uq_chtask` ON `content_approval` (`content_header_id` ASC, `task_assignment_id` ASC) ;


-- -----------------------------------------------------
-- Table `answer_type_float`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_type_float` ;

CREATE  TABLE IF NOT EXISTS `answer_type_float` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `min_value` FLOAT NOT NULL ,
  `max_value` FLOAT NOT NULL ,
  `default_value` FLOAT NULL ,
  `criteria` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `answer_object_float`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `answer_object_float` ;

CREATE  TABLE IF NOT EXISTS `answer_object_float` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `value` FLOAT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `notification_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `notification_item` ;

CREATE  TABLE IF NOT EXISTS `notification_item` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `subject_text` VARCHAR(255) NOT NULL ,
  `body_text` TEXT NULL ,
  `language_id` INT NULL ,
  `notification_type_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_language`
    FOREIGN KEY (`language_id` )
    REFERENCES `language` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_notification_type`
    FOREIGN KEY (`notification_type_id` )
    REFERENCES `notification_type` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_language` ON `notification_item` (`language_id` ASC) ;

CREATE INDEX `fk_notification_type` ON `notification_item` (`notification_type_id` ASC) ;

CREATE UNIQUE INDEX `uni_ind` ON `notification_item` (`notification_type_id` ASC, `language_id` ASC) ;


-- -----------------------------------------------------
-- Table `config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `config` ;

CREATE  TABLE IF NOT EXISTS `config` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `default_language_id` INT NOT NULL ,
  `platform_name` VARCHAR(255) NOT NULL ,
  `platform_admin_user_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_config_admin_user`
    FOREIGN KEY (`platform_admin_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_config_admin_user` ON `config` (`platform_admin_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `token` ;

CREATE  TABLE IF NOT EXISTS `token` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Tokens defined in this table is only for display to end users';

CREATE UNIQUE INDEX `name_UNIQUE` ON `token` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `ttags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ttags` ;

CREATE  TABLE IF NOT EXISTS `ttags` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `term` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'Predefined target tags';

CREATE UNIQUE INDEX `term_UNIQUE` ON `ttags` (`term` ASC) ;


-- -----------------------------------------------------
-- Table `target_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `target_tag` ;

CREATE  TABLE IF NOT EXISTS `target_tag` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `target_id` INT NOT NULL ,
  `ttags_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_tt_target`
    FOREIGN KEY (`target_id` )
    REFERENCES `target` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tt_ttags`
    FOREIGN KEY (`ttags_id` )
    REFERENCES `ttags` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_tt_target` ON `target_tag` (`target_id` ASC) ;

CREATE INDEX `fk_tt_ttags` ON `target_tag` (`ttags_id` ASC) ;

CREATE UNIQUE INDEX `uq_tt_tt` ON `target_tag` (`target_id` ASC, `ttags_id` ASC) ;


-- -----------------------------------------------------
-- Table `content_version`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `content_version` ;

CREATE  TABLE IF NOT EXISTS `content_version` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content_header_id` INT NOT NULL ,
  `create_time` DATETIME NOT NULL ,
  `user_id` INT NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `cv_header`
    FOREIGN KEY (`content_header_id` )
    REFERENCES `content_header` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `cv_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `cv_header` ON `content_version` (`content_header_id` ASC) ;

CREATE INDEX `cv_user` ON `content_version` (`user_id` ASC) ;


-- -----------------------------------------------------
-- Table `journal_content_version`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `journal_content_version` ;

CREATE  TABLE IF NOT EXISTS `journal_content_version` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content_version_id` INT NOT NULL ,
  `body` TEXT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `jcv_version`
    FOREIGN KEY (`content_version_id` )
    REFERENCES `content_version` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE UNIQUE INDEX `journal_content_object_id_UNIQUE` ON `journal_content_version` (`content_version_id` ASC) ;

CREATE UNIQUE INDEX `jcv_header_task` ON `journal_content_version` (`content_version_id` ASC) ;

CREATE INDEX `jcv_version` ON `journal_content_version` (`content_version_id` ASC) ;


-- -----------------------------------------------------
-- Table `journal_attachment_version`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `journal_attachment_version` ;

CREATE  TABLE IF NOT EXISTS `journal_attachment_version` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content_version_id` INT NOT NULL ,
  `name` VARCHAR(255) NULL ,
  `size` INT NULL ,
  `type` VARCHAR(8) NULL ,
  `note` VARCHAR(255) NULL ,
  `file_path` VARCHAR(255) NOT NULL ,
  `update_time` DATETIME NULL COMMENT 'file upload time' ,
  `user_id` INT NULL DEFAULT 0 COMMENT 'user who uploaded the file' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `jav_version`
    FOREIGN KEY (`content_version_id` )
    REFERENCES `content_version` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_jav_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `jav_version` ON `journal_attachment_version` (`content_version_id` ASC) ;

CREATE INDEX `fk_jav_user` ON `journal_attachment_version` (`user_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_answer_version`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_answer_version` ;

CREATE  TABLE IF NOT EXISTS `survey_answer_version` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content_version_id` INT NOT NULL ,
  `survey_question_id` INT NOT NULL ,
  `answer_object_id` INT NOT NULL ,
  `reference_object_id` INT NOT NULL ,
  `comments` TEXT NULL ,
  `answer_time` DATETIME NOT NULL ,
  `answer_user_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `sav_question`
    FOREIGN KEY (`survey_question_id` )
    REFERENCES `survey_question` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `sav_ref`
    FOREIGN KEY (`reference_object_id` )
    REFERENCES `reference_object` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `sav_user`
    FOREIGN KEY (`answer_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `sav_question` ON `survey_answer_version` (`survey_question_id` ASC) ;

CREATE INDEX `sav_ref` ON `survey_answer_version` (`reference_object_id` ASC) ;

CREATE INDEX `sav_user` ON `survey_answer_version` (`answer_user_id` ASC) ;

CREATE UNIQUE INDEX `sav_content_qst` ON `survey_answer_version` (`content_version_id` ASC, `survey_question_id` ASC) ;


-- -----------------------------------------------------
-- Table `itags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `itags` ;

CREATE  TABLE IF NOT EXISTS `itags` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `term` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = 'This table contains indicator tags';

CREATE UNIQUE INDEX `term_UNIQUE` ON `itags` (`term` ASC) ;


-- -----------------------------------------------------
-- Table `indicator_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `indicator_tag` ;

CREATE  TABLE IF NOT EXISTS `indicator_tag` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `survey_indicator_id` INT NOT NULL ,
  `itags_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_it_ind`
    FOREIGN KEY (`survey_indicator_id` )
    REFERENCES `survey_indicator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_it_tag`
    FOREIGN KEY (`itags_id` )
    REFERENCES `itags` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This table contains indicator and itags association';

CREATE UNIQUE INDEX `uq_it_td` ON `indicator_tag` (`survey_indicator_id` ASC, `itags_id` ASC) ;

CREATE INDEX `fk_it_ind` ON `indicator_tag` (`survey_indicator_id` ASC) ;

CREATE INDEX `fk_it_tag` ON `indicator_tag` (`itags_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_answer_attachment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_answer_attachment` ;

CREATE  TABLE IF NOT EXISTS `survey_answer_attachment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `survey_answer_id` INT NOT NULL ,
  `attachment_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_saa_answer`
    FOREIGN KEY (`survey_answer_id` )
    REFERENCES `survey_answer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_saa_attach`
    FOREIGN KEY (`attachment_id` )
    REFERENCES `attachment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_saa_answer` ON `survey_answer_attachment` (`survey_answer_id` ASC) ;

CREATE INDEX `fk_saa_attach` ON `survey_answer_attachment` (`attachment_id` ASC) ;

CREATE UNIQUE INDEX `uq_saa_saa` ON `survey_answer_attachment` (`survey_answer_id` ASC, `attachment_id` ASC) ;


-- -----------------------------------------------------
-- Table `survey_answer_attachment_version`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `survey_answer_attachment_version` ;

CREATE  TABLE IF NOT EXISTS `survey_answer_attachment_version` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `survey_answer_version_id` INT NOT NULL ,
  `name` VARCHAR(255) NULL ,
  `size` INT NULL ,
  `type` VARCHAR(8) NULL ,
  `note` VARCHAR(255) NULL ,
  `file_path` VARCHAR(255) NOT NULL ,
  `update_time` DATETIME NULL COMMENT 'file upload time' ,
  `user_id` INT NULL DEFAULT 0 COMMENT 'user who uploaded the file' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_saav_version`
    FOREIGN KEY (`survey_answer_version_id` )
    REFERENCES `survey_answer_version` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_saav_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;

CREATE INDEX `fk_saav_version` ON `survey_answer_attachment_version` (`survey_answer_version_id` ASC) ;

CREATE INDEX `fk_saav_user` ON `survey_answer_attachment_version` (`user_id` ASC) ;


-- -----------------------------------------------------
-- Table `userfinder`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `userfinder` ;

CREATE  TABLE IF NOT EXISTS `userfinder` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(255) NULL ,
  `project_id` INT NOT NULL ,
  `role_id` INT NOT NULL ,
  `assigned_user_id` INT NOT NULL ,
  `case_subject` TEXT NOT NULL ,
  `case_body` TEXT NOT NULL ,
  `case_priority` TINYINT NOT NULL ,
  `attach_user` TINYINT(1)  NOT NULL ,
  `attach_content` TINYINT(1)  NOT NULL ,
  `status` TINYINT NOT NULL COMMENT '1 = active;\n2 = inactive;\n3 = deleted;' ,
  `create_time` DATETIME NOT NULL ,
  `last_update_time` DATETIME NOT NULL ,
  `delete_time` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_uf_project`
    FOREIGN KEY (`project_id` )
    REFERENCES `project` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uf_role`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uf_assigned`
    FOREIGN KEY (`assigned_user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This table contains userfinder triggers';

CREATE INDEX `fk_uf_project` ON `userfinder` (`project_id` ASC) ;

CREATE INDEX `fk_uf_role` ON `userfinder` (`role_id` ASC) ;

CREATE INDEX `fk_uf_assigned` ON `userfinder` (`assigned_user_id` ASC) ;

CREATE UNIQUE INDEX `uq_uf_projrole` ON `userfinder` (`project_id` ASC, `role_id` ASC) ;


-- -----------------------------------------------------
-- Table `userfinder_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `userfinder_event` ;

CREATE  TABLE IF NOT EXISTS `userfinder_event` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `userfinder_id` INT NOT NULL ,
  `user_id` INT NOT NULL COMMENT 'User who has completed all assignments' ,
  `cases_id` INT NOT NULL COMMENT 'Case that was opened' ,
  `exe_time` DATETIME NOT NULL COMMENT 'when the even was fired' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ufe_uf`
    FOREIGN KEY (`userfinder_id` )
    REFERENCES `userfinder` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ufe_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ufe_case`
    FOREIGN KEY (`cases_id` )
    REFERENCES `cases` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
COMMENT = 'This table keeps fired userfinder events';

CREATE UNIQUE INDEX `uq_ufe_event` ON `userfinder_event` (`userfinder_id` ASC, `user_id` ASC) ;

CREATE INDEX `fk_ufe_uf` ON `userfinder_event` (`userfinder_id` ASC) ;

CREATE INDEX `fk_ufe_user` ON `userfinder_event` (`user_id` ASC) ;

CREATE INDEX `fk_ufe_case` ON `userfinder_event` (`cases_id` ASC) ;


-- -----------------------------------------------------
-- Table `session`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `session` ;

CREATE  TABLE IF NOT EXISTS `session` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_id` INT NOT NULL ,
  `ticket` VARCHAR(32) NOT NULL COMMENT 'User sessions.' ,
  `last_updated_time` TIMESTAMP NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;

CREATE UNIQUE INDEX `id_UNIQUE` ON `session` (`id` ASC) ;

CREATE UNIQUE INDEX `user_id_UNIQUE` ON `session` (`user_id` ASC) ;

CREATE UNIQUE INDEX `ticket_UNIQUE` ON `session` (`ticket` ASC) ;


-- -----------------------------------------------------
-- Table `source_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `source_file` ;

CREATE  TABLE IF NOT EXISTS `source_file` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `filename` VARCHAR(256) NOT NULL ,
  `path` VARCHAR(256) NOT NULL DEFAULT '' ,
  `extension` VARCHAR(10) NOT NULL DEFAULT '' ,
  `status` TINYINT NOT NULL DEFAULT 1 ,
  `last_update_time` TIMESTAMP NULL ,
  `note` VARCHAR(512) NULL DEFAULT '' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;

CREATE INDEX `idx_filename` ON `source_file` (`filename` ASC) ;

CREATE INDEX `idx_status` ON `source_file` (`status` ASC) ;


-- -----------------------------------------------------
-- Table `source_file_text_resource`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `source_file_text_resource` ;

CREATE  TABLE IF NOT EXISTS `source_file_text_resource` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `source_file_id` INT NOT NULL ,
  `text_resource_id` INT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM;

CREATE INDEX `IDX_SOURCE_FILE_ID` ON `source_file_text_resource` (`source_file_id` ASC) ;



-- SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS