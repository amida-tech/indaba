use staging

SET @org_id = 1;

-- No way to reliablesplit by organization
DROP TABLE IF EXISTS `mailbatch`;
CREATE TABLE IF NOT EXISTS `mailbatch` LIKE `indaba`.`mailbatch`;
INSERT INTO `mailbatch` SELECT * FROM indaba.mailbatch;

-- Systemwide???
DROP TABLE IF EXISTS `config`;
CREATE TABLE IF NOT EXISTS `config` LIKE `indaba`.`config`;
INSERT INTO `config` SELECT * FROM indaba.config;

DROP TABLE IF EXISTS `right_category`;
CREATE TABLE IF NOT EXISTS `right_category` LIKE `indaba`.`right_category`;
INSERT INTO `right_category` SELECT * FROM indaba.right_category;

DROP TABLE IF EXISTS `rights`;
CREATE TABLE IF NOT EXISTS `rights` LIKE `indaba`.`rights`;
INSERT INTO `rights` SELECT * FROM indaba.rights;

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` LIKE `indaba`.`role`;
ALTER TABLE `role` AUTO_INCREMENT=60;
INSERT INTO `role` SELECT * FROM indaba.role;

DROP TABLE IF EXISTS `rule`;
CREATE TABLE IF NOT EXISTS `rule` LIKE `indaba`.`rule`;
INSERT INTO `rule` SELECT * FROM indaba.rule;

DROP TABLE IF EXISTS `text_item`;
CREATE TABLE IF NOT EXISTS `text_item` LIKE `indaba`.`text_item`;
INSERT INTO `text_item` SELECT * FROM indaba.text_item;

DROP TABLE IF EXISTS `text_resource`;
CREATE TABLE IF NOT EXISTS `text_resource` LIKE `indaba`.`text_resource`;
INSERT INTO `text_resource` SELECT * FROM indaba.text_resource;

DROP TABLE IF EXISTS `reference`;
CREATE TABLE IF NOT EXISTS `reference` LIKE `indaba`.`reference`;
INSERT INTO `reference` SELECT * FROM indaba.reference;

DROP TABLE IF EXISTS `reference_choice`;
CREATE TABLE IF NOT EXISTS `reference_choice` LIKE `indaba`.`reference_choice`;
INSERT INTO `reference_choice` SELECT * FROM indaba.reference_choice;

DROP TABLE IF EXISTS `reference_object`;
CREATE TABLE IF NOT EXISTS `reference_object` LIKE `indaba`.`reference_object`;
INSERT INTO `reference_object` SELECT * FROM indaba.reference_object;

DROP TABLE IF EXISTS `source_file`;
CREATE TABLE IF NOT EXISTS `source_file` LIKE `indaba`.`source_file`;
INSERT INTO `source_file` SELECT * FROM indaba.source_file;

DROP TABLE IF EXISTS `source_file_text_resource`;
CREATE TABLE IF NOT EXISTS `source_file_text_resource` LIKE `indaba`.`source_file_text_resource`;
INSERT INTO `source_file_text_resource` SELECT * FROM indaba.source_file_text_resource;

DROP TABLE IF EXISTS `token`;
CREATE TABLE IF NOT EXISTS `token` LIKE `indaba`.`token`;
INSERT INTO `token` SELECT * FROM indaba.token;

DROP TABLE IF EXISTS `tokenset`;
CREATE TABLE IF NOT EXISTS `tokenset` LIKE `indaba`.`tokenset`;
INSERT INTO `tokenset` SELECT * FROM indaba.tokenset;

\! echo 'upload_file'
DROP TABLE IF EXISTS `upload_file`;
CREATE TABLE IF NOT EXISTS `upload_file` LIKE `indaba`.`upload_file`;
INSERT INTO `upload_file` SELECT * FROM indaba.upload_file;

\! echo 'operatinal table, skip data'
DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` LIKE `indaba`.`session`;

DROP TABLE IF EXISTS `announcement`;
CREATE TABLE IF NOT EXISTS `announcement` LIKE `indaba`.`announcement`;
INSERT INTO `announcement` SELECT * FROM indaba.announcement;

DROP TABLE IF EXISTS `language`;
CREATE TABLE IF NOT EXISTS `language` LIKE `indaba`.`language`;
INSERT INTO `language` SELECT * FROM indaba.language;

\! echo 'notification_item'
DROP TABLE IF EXISTS `notification_item`;
CREATE TABLE IF NOT EXISTS `notification_item` LIKE `indaba`.`notification_item`;
INSERT INTO `notification_item` SELECT * FROM indaba.notification_item;

\! echo 'notification_type'
DROP TABLE IF EXISTS `notification_type`;
CREATE TABLE IF NOT EXISTS `notification_type` LIKE `indaba`.`notification_type`;
INSERT INTO `notification_type` SELECT * FROM indaba.notification_type;

-- IIII
DROP TABLE IF EXISTS `organization`;
CREATE TABLE IF NOT EXISTS `organization` LIKE `indaba`.`organization`;
INSERT INTO `organization` SELECT * FROM indaba.organization WHERE id = @org_id;

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` LIKE `indaba`.`user`;
INSERT INTO `user` SELECT * FROM indaba.user WHERE organization_id = @org_id;

DROP TABLE IF EXISTS `project`;
CREATE TABLE IF NOT EXISTS `project` LIKE `indaba`.`project`;
INSERT INTO `project` SELECT * FROM indaba.project WHERE organization_id = @org_id;

DROP TABLE IF EXISTS `study_period`;
CREATE TABLE IF NOT EXISTS `study_period` LIKE `indaba`.`study_period`;
INSERT INTO `study_period` SELECT * FROM indaba.study_period WHERE id IN (SELECT study_period_id FROM indaba.project WHERE organization_id = @org_id);

\! echo 'message'
DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` LIKE `indaba`.`message`;
-- INSERT INTO `message` SELECT * FROM indaba.message WHERE author_user_id IN(SELECT id FROM user WHERE organization_id = @org_id) OR author_user_id = 0;
INSERT INTO `message` SELECT * FROM indaba.message WHERE msgboard_id IN(SELECT msgboard_id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `msg_reading_status`;
CREATE TABLE IF NOT EXISTS `msg_reading_status` LIKE `indaba`.`msg_reading_status`;
INSERT INTO `msg_reading_status` SELECT * FROM indaba.msg_reading_status;

DROP TABLE IF EXISTS `msgboard`;
CREATE TABLE IF NOT EXISTS `msgboard` LIKE `indaba`.`msgboard`;
INSERT INTO `msgboard` SELECT * FROM indaba.msgboard where id IN (SELECT msgboard_id FROM indaba.project WHERE organization_id = @org_id);

\! echo 'imports'
DROP TABLE IF EXISTS `imports`;
CREATE TABLE IF NOT EXISTS `imports` LIKE `indaba`.`imports`;
INSERT INTO `imports` SELECT * FROM indaba.imports WHERE id IN (SELECT import_id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_admin`;
CREATE TABLE IF NOT EXISTS `project_admin` LIKE `indaba`.`project_admin`;
INSERT INTO `project_admin` SELECT * FROM indaba.project_admin WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_contact`;
CREATE TABLE IF NOT EXISTS `project_contact` LIKE `indaba`.`project_contact`;
INSERT INTO `project_contact` SELECT * FROM indaba.project_contact WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_membership`;
CREATE TABLE IF NOT EXISTS `project_membership` LIKE `indaba`.`project_membership`;
INSERT INTO `project_membership` SELECT * FROM indaba.project_membership WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_notif`;
CREATE TABLE IF NOT EXISTS `project_notif` LIKE `indaba`.`project_notif`;
INSERT INTO `project_notif` SELECT * FROM indaba.project_notif WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_notif_role`;
CREATE TABLE IF NOT EXISTS `project_notif_role` LIKE `indaba`.`project_notif_role`;
INSERT INTO `project_notif_role` SELECT * FROM indaba.project_notif_role WHERE project_notif_id IN (SELECT pn.id FROM indaba.project p JOIN indaba.project_notif pn ON p.id = pn.project_id WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_owner`;
CREATE TABLE IF NOT EXISTS `project_owner` LIKE `indaba`.`project_owner`;
INSERT INTO `project_owner` SELECT * FROM indaba.project_owner WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_roles`;
CREATE TABLE IF NOT EXISTS `project_roles` LIKE `indaba`.`project_roles`;
INSERT INTO `project_roles` SELECT * FROM indaba.project_roles WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `project_target`;
CREATE TABLE IF NOT EXISTS `project_target` LIKE `indaba`.`project_target`;
INSERT INTO `project_target` SELECT * FROM indaba.project_target WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product` LIKE `indaba`.`product`;
INSERT INTO `product` SELECT * FROM indaba.product WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `access_matrix`;
CREATE TABLE IF NOT EXISTS `access_matrix` LIKE `indaba`.`access_matrix`;
INSERT INTO `access_matrix` SELECT * FROM indaba.access_matrix WHERE id IN (SELECT access_matrix_id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `access_permission`;
CREATE TABLE IF NOT EXISTS `access_permission` LIKE `indaba`.`access_permission`;
INSERT INTO `access_permission` SELECT * FROM indaba.access_permission WHERE access_matrix_id IN (SELECT access_matrix_id FROM indaba.project p JOIN indaba.access_matrix am ON p.access_matrix_id = am.id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `view_matrix`;
CREATE TABLE IF NOT EXISTS `view_matrix` LIKE `indaba`.`view_matrix`;
INSERT INTO `view_matrix` SELECT * FROM indaba.view_matrix WHERE id IN (SELECT view_matrix_id FROM indaba.project WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `view_permission`;
CREATE TABLE IF NOT EXISTS `view_permission` LIKE `indaba`.`view_permission`;
INSERT INTO `view_permission` SELECT * FROM indaba.view_permission WHERE view_matrix_id IN (SELECT view_matrix_id FROM indaba.project p JOIN indaba.view_matrix vm ON p.view_matrix_id = vm.id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `notedef`;
CREATE TABLE IF NOT EXISTS `notedef` LIKE `indaba`.`notedef`;
INSERT INTO `notedef` SELECT * FROM indaba.notedef WHERE product_id IN (SELECT pd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `notedef_role`;
CREATE TABLE IF NOT EXISTS `notedef_role` LIKE `indaba`.`notedef_role`;
INSERT INTO `notedef_role` SELECT * FROM indaba.notedef_role WHERE notedef_id IN (SELECT nd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `notedef_user`;
CREATE TABLE IF NOT EXISTS `notedef_user` LIKE `indaba`.`notedef_user`;
INSERT INTO `notedef_user` SELECT * FROM indaba.notedef_user WHERE notedef_id IN (SELECT nd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `noteobj`;
CREATE TABLE IF NOT EXISTS `noteobj` LIKE `indaba`.`noteobj`;
INSERT INTO `noteobj` SELECT * FROM indaba.noteobj WHERE notedef_id IN (SELECT nd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `noteobj_intl`;
CREATE TABLE IF NOT EXISTS `noteobj_intl` LIKE `indaba`.`noteobj_intl`;
INSERT INTO `noteobj_intl` SELECT * FROM indaba.noteobj_intl WHERE noteobj_id IN (SELECT nt.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id JOIN noteobj nt ON nd.id = nt.notedef_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `noteobj_version`;
CREATE TABLE IF NOT EXISTS `noteobj_version` LIKE `indaba`.`noteobj_version`;
INSERT INTO `noteobj_version` SELECT * FROM indaba.noteobj_version WHERE notedef_id IN (SELECT nd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `noteobj_version_intl`;
CREATE TABLE IF NOT EXISTS `noteobj_version_intl` LIKE `indaba`.`noteobj_version_intl`;
INSERT INTO `noteobj_version_intl` SELECT * FROM indaba.noteobj_version_intl WHERE noteobj_version_id IN (SELECT nd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id JOIN noteobj_version nv ON nv.notedef_id = nd.id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `horse`;
CREATE TABLE IF NOT EXISTS `horse` LIKE `indaba`.`horse`;
INSERT INTO `horse` SELECT * FROM indaba.horse WHERE id IN (SELECT nt.horse_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id JOIN noteobj nt ON nd.id = nt.notedef_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `dead_horse`;
CREATE TABLE IF NOT EXISTS `dead_horse` LIKE `indaba`.`dead_horse`;
INSERT INTO `dead_horse` SELECT * FROM indaba.dead_horse WHERE id IN (SELECT nt.horse_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN notedef nd ON nd.product_id = pd.id JOIN noteobj nt ON nd.id = nt.notedef_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `workflow`;
CREATE TABLE IF NOT EXISTS `workflow` LIKE `indaba`.`workflow`;
INSERT INTO `workflow` SELECT * FROM indaba.workflow WHERE id IN (SELECT pd.workflow_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id WHERE p.organization_id = @org_id);

\! echo 'workflow_object'
DROP TABLE IF EXISTS `workflow_object`;
CREATE TABLE IF NOT EXISTS `workflow_object` LIKE `indaba`.`workflow_object`;
INSERT INTO `workflow_object` SELECT * FROM indaba.workflow_object WHERE workflow_id IN (SELECT pd.workflow_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.workflow wf ON wf.id = pd.workflow_id WHERE p.organization_id = @org_id);

\! echo 'sequence_object'
DROP TABLE IF EXISTS `sequence_object`;
CREATE TABLE IF NOT EXISTS `sequence_object` LIKE `indaba`.`sequence_object`;
INSERT INTO `sequence_object` SELECT * FROM indaba.sequence_object WHERE workflow_object_id IN (SELECT wfo.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.workflow wf ON wf.id = pd.workflow_id JOIN indaba.workflow_object wfo ON wfo.workflow_id = pd.workflow_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `workflow_sequence`;
CREATE TABLE IF NOT EXISTS `workflow_sequence` LIKE `indaba`.`workflow_sequence`;
INSERT INTO `workflow_sequence` SELECT * FROM indaba.workflow_sequence WHERE workflow_id IN (SELECT pd.workflow_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.workflow wf ON wf.id = pd.workflow_id WHERE p.organization_id = @org_id);

\! echo 'pregoal'
DROP TABLE IF EXISTS `pregoal`;
CREATE TABLE IF NOT EXISTS `pregoal` LIKE `indaba`.`pregoal`;
INSERT INTO `pregoal` SELECT * FROM indaba.pregoal WHERE workflow_sequence_id IN (SELECT wfs.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.workflow wf ON wf.id = pd.workflow_id JOIN indaba.workflow_sequence wfs ON wfs.workflow_id = pd.workflow_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `task`;
CREATE TABLE IF NOT EXISTS `task` LIKE `indaba`.`task`;
INSERT INTO `task` SELECT * FROM indaba.task WHERE product_id IN (SELECT pd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `task_assignment`;
CREATE TABLE IF NOT EXISTS `task_assignment` LIKE `indaba`.`task_assignment`;
INSERT INTO `task_assignment` SELECT * FROM indaba.task_assignment WHERE task_id IN (SELECT ta.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `task_role`;
CREATE TABLE IF NOT EXISTS `task_role` LIKE `indaba`.`task_role`;
INSERT INTO `task_role` SELECT * FROM indaba.task_role WHERE task_id IN (SELECT ta.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `tasksub`;
CREATE TABLE IF NOT EXISTS `tasksub` LIKE `indaba`.`tasksub`;
INSERT INTO `tasksub` SELECT * FROM indaba.tasksub WHERE task_id IN (SELECT ta.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id WHERE p.organization_id = @org_id);

\! echo 'goal'
DROP TABLE IF EXISTS `goal`;
CREATE TABLE IF NOT EXISTS `goal` LIKE `indaba`.`goal`;
INSERT INTO `goal` SELECT * FROM indaba.goal WHERE id IN (SELECT ta.goal_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id WHERE p.organization_id = @org_id);

DROP TABLE IF EXISTS `goal_object`;
CREATE TABLE IF NOT EXISTS `goal_object` LIKE `indaba`.`goal_object`;
INSERT INTO `goal_object` SELECT * FROM indaba.goal_object WHERE goal_id IN (SELECT g.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id JOIN goal g ON g.id = ta.goal_id WHERE p.organization_id = @org_id);

\! echo 'tool'
DROP TABLE IF EXISTS `tool`;
CREATE TABLE IF NOT EXISTS `tool` LIKE `indaba`.`tool`;
INSERT INTO `tool` SELECT * FROM indaba.tool WHERE id IN (SELECT ta.tool_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id WHERE p.organization_id = @org_id);


CREATE OR REPLACE VIEW task_v AS SELECT ta.*, organization_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id;
CREATE OR REPLACE VIEW task_assignment_v AS SELECT taa.*, organization_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.task ta ON pd.id=ta.product_id JOIN indaba.task_assignment taa ON ta.id = taa.task_id;


\! echo 'target'
DROP TABLE IF EXISTS `target`;
CREATE TABLE IF NOT EXISTS `target` LIKE `indaba`.`target`;
INSERT INTO `target` SELECT * FROM indaba.target WHERE id IN (SELECT target_id FROM task_assignment_v WHERE organization_id = @org_id);

\! echo 'target_tag'
DROP TABLE IF EXISTS `target_tag`;
CREATE TABLE IF NOT EXISTS `target_tag` LIKE `indaba`.`target_tag`;
INSERT INTO `target_tag` SELECT * FROM indaba.target_tag WHERE target_id IN (SELECT target_id FROM task_assignment_v WHERE organization_id = @org_id);

\! echo 'ttags'
DROP TABLE IF EXISTS `ttags`;
CREATE TABLE IF NOT EXISTS `ttags` LIKE `indaba`.`ttags`;
INSERT INTO `ttags` SELECT * FROM indaba.ttags WHERE id IN (SELECT ttg.ttags_id FROM task_assignment_v ta JOIN indaba.target_tag ttg ON ta.target_id = ttg.target_id WHERE organization_id = @org_id);

\! echo 'tag'
DROP TABLE IF EXISTS `tag`;
CREATE TABLE IF NOT EXISTS `tag` LIKE `indaba`.`tag`;
INSERT INTO `tag` SELECT * FROM indaba.tag WHERE user_id IN (SELECT id FROM indaba.user WHERE organization_id = @org_id);

\! echo 'content_header'
DROP TABLE IF EXISTS `content_header`;
CREATE TABLE IF NOT EXISTS `content_header` LIKE `indaba`.`content_header`;
INSERT INTO `content_header` SELECT * FROM indaba.content_header WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

\! echo 'attachment'
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE IF NOT EXISTS `attachment` LIKE `indaba`.`attachment`;
INSERT INTO `attachment` SELECT * FROM indaba.attachment WHERE content_header_id IN (SELECT ch.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `content_approval`;
CREATE TABLE IF NOT EXISTS `content_approval` LIKE `indaba`.`content_approval`;
INSERT INTO `content_approval` SELECT * FROM indaba.content_approval WHERE content_header_id IN (SELECT ch.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `content_payment`;
CREATE TABLE IF NOT EXISTS `content_payment` LIKE `indaba`.`content_payment`;
INSERT INTO `content_payment` SELECT * FROM indaba.content_payment WHERE content_header_id IN (SELECT ch.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id WHERE organization_id = @org_id);

DROP TABLE IF EXISTS `content_version`;
CREATE TABLE IF NOT EXISTS `content_version` LIKE `indaba`.`content_version`;
INSERT INTO `content_version` SELECT * FROM indaba.content_version WHERE content_header_id IN (SELECT ch.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id WHERE organization_id = @org_id);

\! echo 'survey_content_object'
DROP TABLE IF EXISTS `survey_content_object`;
CREATE TABLE IF NOT EXISTS `survey_content_object` LIKE `indaba`.`survey_content_object`;
INSERT INTO `survey_content_object` SELECT * FROM indaba.survey_content_object WHERE id IN (SELECT ch.content_object_id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id WHERE organization_id = @org_id AND ch.content_type = 0); -- 0 - survey

\! echo 'survey_config'
DROP TABLE IF EXISTS `survey_config`;
CREATE TABLE IF NOT EXISTS `survey_config` LIKE `indaba`.`survey_config`;
INSERT INTO `survey_config` SELECT * FROM indaba.survey_config WHERE id IN (SELECT co.survey_config_id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id JOIN indaba.survey_content_object co ON ch.content_object_id = co.content_header_id WHERE organization_id = @org_id AND ch.content_type = 0); -- 0 - survey

\! echo 'sc_contributor'
DROP TABLE IF EXISTS `sc_contributor`;
CREATE TABLE IF NOT EXISTS `sc_contributor` LIKE `indaba`.`sc_contributor`;
INSERT INTO `sc_contributor` SELECT * FROM indaba.sc_contributor WHERE survey_config_id IN (SELECT sc.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id JOIN indaba.survey_content_object co ON ch.content_object_id = co.content_header_id  JOIN indaba.survey_config sc ON co.survey_config_id = sc.id WHERE organization_id = @org_id AND ch.content_type = 0); -- 0 - survey

\! echo 'sc_indicator'
DROP TABLE IF EXISTS `sc_indicator`;
CREATE TABLE IF NOT EXISTS `sc_indicator` LIKE `indaba`.`sc_indicator`;
INSERT INTO `sc_indicator` SELECT * FROM indaba.sc_indicator WHERE survey_config_id IN (SELECT sc.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id JOIN indaba.survey_content_object co ON ch.content_object_id = co.content_header_id  JOIN indaba.survey_config sc ON co.survey_config_id = sc.id WHERE organization_id = @org_id AND ch.content_type = 0); -- 0 - survey

\! echo 'survey_category'
DROP TABLE IF EXISTS `survey_category`;
CREATE TABLE IF NOT EXISTS `survey_category` LIKE `indaba`.`survey_category`;
INSERT INTO `survey_category` SELECT * FROM indaba.survey_category WHERE survey_config_id IN (SELECT sc.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id JOIN indaba.survey_content_object co ON ch.content_object_id = co.content_header_id  JOIN indaba.survey_config sc ON co.survey_config_id = sc.id WHERE organization_id = @org_id AND ch.content_type = 0); -- 0 - survey

\! echo 'create temp view'
CREATE OR REPLACE VIEW survey_question_id_v AS SELECT sq.id, sq.survey_config_id, sq.survey_indicator_id, p.organization_id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id JOIN indaba.survey_content_object co ON ch.content_object_id = co.content_header_id  JOIN indaba.survey_config sc ON co.survey_config_id = sc.id JOIN indaba.survey_question sq ON sq.survey_config_id = sc.id WHERE ch.content_type = 0;

\! echo 'survey_question'
DROP TABLE IF EXISTS `survey_question`;
CREATE TABLE IF NOT EXISTS `survey_question` LIKE `indaba`.`survey_question`;
INSERT INTO `survey_question` SELECT * FROM indaba.survey_question WHERE id IN (SELECT id FROM survey_question_id_v WHERE organization_id = @org_id);

\! echo 'survey_question_visit'
DROP TABLE IF EXISTS `survey_question_visit`;
CREATE TABLE IF NOT EXISTS `survey_question_visit` LIKE `indaba`.`survey_question_visit`;
INSERT INTO `survey_question_visit` SELECT * FROM indaba.survey_question_visit WHERE survey_question_id IN (SELECT id FROM survey_question_id_v WHERE organization_id = @org_id);

\! echo 'survey_answer'
DROP TABLE IF EXISTS `survey_answer`;
CREATE TABLE IF NOT EXISTS `survey_answer` LIKE `indaba`.`survey_answer`;
INSERT INTO `survey_answer` SELECT * FROM indaba.survey_answer WHERE survey_question_id IN (SELECT id FROM survey_question_id_v WHERE organization_id = @org_id);

\! echo 'survey_answer_version'
DROP TABLE IF EXISTS `survey_answer_version`;
CREATE TABLE IF NOT EXISTS `survey_answer_version` LIKE `indaba`.`survey_answer_version`;
INSERT INTO `survey_answer_version` SELECT * FROM indaba.survey_answer_version WHERE answer_object_id IN (SELECT sa.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id WHERE organization_id = @org_id);

\! echo 'survey_answer_attachment'
DROP TABLE IF EXISTS `survey_answer_attachment`;
CREATE TABLE IF NOT EXISTS `survey_answer_attachment` LIKE `indaba`.`survey_answer_attachment`;
INSERT INTO `survey_answer_attachment` SELECT * FROM indaba.survey_answer_attachment WHERE survey_answer_id IN (SELECT sa.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id WHERE organization_id = @org_id);

\! echo 'survey_answer_attachment_version'
DROP TABLE IF EXISTS `survey_answer_attachment_version`;
CREATE TABLE IF NOT EXISTS `survey_answer_attachment_version` LIKE `indaba`.`survey_answer_attachment_version`;
INSERT INTO `survey_answer_attachment_version` SELECT * FROM indaba.survey_answer_attachment_version WHERE survey_answer_version_id IN (SELECT sav.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id JOIN indaba.survey_answer_version sav ON sav.answer_object_id = sa.id WHERE organization_id = @org_id);

\! echo 'survey_answer_component'
DROP TABLE IF EXISTS `survey_answer_component`;
CREATE TABLE IF NOT EXISTS `survey_answer_component` LIKE `indaba`.`survey_answer_component`;
INSERT INTO `survey_answer_component` SELECT * FROM indaba.survey_answer_component WHERE survey_answer_id IN (SELECT sa.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id WHERE organization_id = @org_id);

\! echo 'survey_answer_component_version'
DROP TABLE IF EXISTS `survey_answer_component_version`;
CREATE TABLE IF NOT EXISTS `survey_answer_component_version` LIKE `indaba`.`survey_answer_component_version`;
INSERT INTO `survey_answer_component_version` SELECT * FROM indaba.survey_answer_component_version WHERE survey_answer_version_id IN (SELECT sav.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id JOIN indaba.survey_answer_version sav ON sav.answer_object_id = sa.id WHERE organization_id = @org_id);

\! echo 'survey_indicator'
DROP TABLE IF EXISTS `survey_indicator`;
CREATE TABLE IF NOT EXISTS `survey_indicator` LIKE `indaba`.`survey_indicator`;
INSERT INTO `survey_indicator` SELECT * FROM indaba.survey_indicator WHERE id in (SELECT survey_indicator_id FROM survey_question_id_v WHERE organization_id = @org_id);

\! echo 'survey_indicator_intl'
DROP TABLE IF EXISTS `survey_indicator_intl`;
CREATE TABLE IF NOT EXISTS `survey_indicator_intl` LIKE `indaba`.`survey_indicator_intl`;
INSERT INTO `survey_indicator_intl` SELECT * FROM indaba.survey_indicator_intl WHERE id in (SELECT survey_indicator_id FROM survey_question_id_v WHERE organization_id = @org_id);

\! echo 'indicator_tag'
DROP TABLE IF EXISTS `indicator_tag`;
CREATE TABLE IF NOT EXISTS `indicator_tag` LIKE `indaba`.`indicator_tag`;
INSERT INTO `indicator_tag` SELECT * FROM indaba.indicator_tag WHERE survey_indicator_id in (SELECT survey_indicator_id FROM survey_question_id_v WHERE organization_id = @org_id);

\! echo 'itags'
DROP TABLE IF EXISTS `itags`;
CREATE TABLE IF NOT EXISTS `itags` LIKE `indaba`.`itags`;
INSERT INTO `indicator_tag` SELECT * FROM indaba.indicator_tag WHERE id in (SELECT it.itags_id FROM survey_question_id_v sq JOIN indaba.indicator_tag it ON it.survey_indicator_id = sq.survey_indicator_id  WHERE organization_id = @org_id);

\! echo 'survey_peer_review'
DROP TABLE IF EXISTS `survey_peer_review`;
CREATE TABLE IF NOT EXISTS `survey_peer_review` LIKE `indaba`.`survey_peer_review`;
INSERT INTO `survey_peer_review` SELECT * FROM indaba.survey_peer_review WHERE survey_answer_id IN (SELECT sa.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id WHERE organization_id = @org_id);

\! echo 'survey_peer_review_version'
DROP TABLE IF EXISTS `survey_peer_review_version`;
CREATE TABLE IF NOT EXISTS `survey_peer_review_version` LIKE `indaba`.`survey_peer_review_version`;
INSERT INTO `survey_peer_review_version` SELECT * FROM indaba.survey_peer_review_version WHERE survey_answer_version_id IN (SELECT sav.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id JOIN indaba.survey_answer_version sav ON sav.answer_object_id = sa.id WHERE organization_id = @org_id);

\! echo 'spr_component'
DROP TABLE IF EXISTS `spr_component`;
CREATE TABLE IF NOT EXISTS `spr_component` LIKE `indaba`.`spr_component`;
INSERT INTO `spr_component` SELECT * FROM indaba.spr_component WHERE survey_peer_review_id IN (SELECT spr.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id JOIN indaba.survey_peer_review spr ON spr.survey_answer_id = sa.id WHERE organization_id = @org_id);

\! echo 'spr_component_version'
DROP TABLE IF EXISTS `spr_component_version`;
CREATE TABLE IF NOT EXISTS `spr_component_version` LIKE `indaba`.`spr_component_version`;
INSERT INTO `spr_component_version` SELECT * FROM indaba.spr_component_version WHERE survey_peer_review_version_id IN (SELECT spr.id FROM survey_question_id_v sq JOIN indaba.survey_answer sa ON sa.survey_question_id = sq.id JOIN indaba.survey_answer_version sav ON sav.answer_object_id = sa.id JOIN indaba.survey_peer_review_version spr ON spr.survey_answer_version_id = sav.id WHERE organization_id = @org_id);

\! echo 'answer_object_float'
DROP TABLE IF EXISTS `answer_object_float`;
CREATE TABLE IF NOT EXISTS `answer_object_float` LIKE `indaba`.`answer_object_float`;
INSERT INTO `answer_object_float` SELECT * FROM indaba.answer_object_float WHERE  id in (SELECT answer_object_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE answer_type = 4 AND organization_id = @org_id ); -- 4 - float

DROP TABLE IF EXISTS `answer_object_integer`;
CREATE TABLE IF NOT EXISTS `answer_object_integer` LIKE `indaba`.`answer_object_integer`;
INSERT INTO `answer_object_integer` SELECT * FROM indaba.answer_object_integer WHERE  id in (SELECT answer_object_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE answer_type = 3 AND organization_id = @org_id ); -- 3 - integer

DROP TABLE IF EXISTS `answer_object_choice`;
CREATE TABLE IF NOT EXISTS `answer_object_choice` LIKE `indaba`.`answer_object_choice`;
INSERT INTO `answer_object_choice` SELECT * FROM indaba.answer_object_choice WHERE  id in (SELECT answer_object_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE (answer_type = 1 OR answer_type = 2) AND organization_id = @org_id ); -- 1 - single choice, 2 - multi choice

DROP TABLE IF EXISTS `answer_object_text`;
CREATE TABLE IF NOT EXISTS `answer_object_text` LIKE `indaba`.`answer_object_text`;
INSERT INTO `answer_object_text` SELECT * FROM indaba.answer_object_text WHERE  id in (SELECT answer_object_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE answer_type = 5 AND organization_id = @org_id ); -- 5 - text

DROP TABLE IF EXISTS `answer_type_choice`;
CREATE TABLE IF NOT EXISTS `answer_type_choice` LIKE `indaba`.`answer_type_choice`;
INSERT INTO `answer_type_choice` SELECT * FROM indaba.answer_type_choice WHERE  id in (SELECT answer_type_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE (answer_type = 1)  AND organization_id = @org_id ); -- 1 - single choice

DROP TABLE IF EXISTS `answer_type_float`;
CREATE TABLE IF NOT EXISTS `answer_type_float` LIKE `indaba`.`answer_type_float`;
INSERT INTO `answer_type_float` SELECT * FROM indaba.answer_type_float WHERE  id in (SELECT answer_type_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE answer_type = 4 AND organization_id = @org_id ); -- 4 - float

DROP TABLE IF EXISTS `answer_type_integer`;
CREATE TABLE IF NOT EXISTS `answer_type_integer` LIKE `indaba`.`answer_type_integer`;
INSERT INTO `answer_type_integer` SELECT * FROM indaba.answer_type_integer WHERE  id in (SELECT answer_type_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE answer_type = 3 AND organization_id = @org_id ); -- 3 - integer

\! echo 'answer_type_table'
DROP TABLE IF EXISTS `answer_type_table`;
CREATE TABLE IF NOT EXISTS `answer_type_table` LIKE `indaba`.`answer_type_table`;
INSERT INTO `answer_type_table` SELECT * FROM indaba.answer_type_table;

DROP TABLE IF EXISTS `answer_type_text`;
CREATE TABLE IF NOT EXISTS `answer_type_text` LIKE `indaba`.`answer_type_text`;
INSERT INTO `answer_type_text` SELECT * FROM indaba.answer_type_text WHERE  id in (SELECT answer_type_id FROM survey_question_id_v sq JOIN indaba.survey_indicator si ON si.id = sq.survey_indicator_id JOIN indaba.survey_answer sa ON sq.id = sa.survey_question_id WHERE answer_type = 5 AND organization_id = @org_id ); -- 5 - text

\! echo 'journal_content_object'
DROP TABLE IF EXISTS `journal_content_object`;
CREATE TABLE IF NOT EXISTS `journal_content_object` LIKE `indaba`.`journal_content_object`;
INSERT INTO `journal_content_object` SELECT * FROM indaba.journal_content_object WHERE id IN (SELECT ch.content_object_id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id WHERE organization_id = @org_id AND ch.content_type = 1); -- 1 - survey

\! echo 'journal_config'
DROP TABLE IF EXISTS `journal_config`;
CREATE TABLE IF NOT EXISTS `journal_config` LIKE `indaba`.`journal_config`;
INSERT INTO `journal_config` SELECT * FROM indaba.journal_config WHERE id IN (SELECT jco.journal_config_id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id JOIN indaba.journal_content_object jco ON ch.content_object_id = jco.content_header_id WHERE organization_id = @org_id AND ch.content_type = 1); -- 1 - survey

\! echo 'journal_peer_review'
DROP TABLE IF EXISTS `journal_peer_review`;
CREATE TABLE IF NOT EXISTS `journal_peer_review` LIKE `indaba`.`journal_peer_review`;
INSERT INTO `journal_peer_review` SELECT * FROM indaba.journal_peer_review WHERE journal_content_object_id IN (SELECT jco.id FROM indaba.project p JOIN indaba.content_header ch ON p.id = ch.project_id JOIN indaba.journal_content_object jco ON ch.content_object_id = jco.content_header_id WHERE organization_id = @org_id AND ch.content_type = 1); -- 1 - survey

\! echo 'groupdef'
DROP TABLE IF EXISTS `groupdef`;
CREATE TABLE IF NOT EXISTS `groupdef` LIKE `indaba`.`groupdef`;
INSERT INTO `groupdef` SELECT * FROM indaba.groupdef WHERE product_id IN (SELECT pd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id WHERE p.organization_id = @org_id);

\! echo 'groupdef_role'
DROP TABLE IF EXISTS `groupdef_role`;
CREATE TABLE IF NOT EXISTS `groupdef_role` LIKE `indaba`.`groupdef_role`;
INSERT INTO `groupdef_role` SELECT * FROM indaba.groupdef_role WHERE groupdef_id IN (SELECT gd.id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.groupdef gd ON gd.product_id =  pd.id WHERE p.organization_id = @org_id);

CREATE OR REPLACE VIEW groupdef_v AS SELECT DISTINCT gd.id, p.organization_id FROM indaba.project p JOIN indaba.product pd ON p.id = pd.project_id JOIN indaba.groupdef gd ON gd.product_id =  pd.id

\! echo 'groupdef_user'
DROP TABLE IF EXISTS `groupdef_user`;
CREATE TABLE IF NOT EXISTS `groupdef_user` LIKE `indaba`.`groupdef_user`;
INSERT INTO `groupdef_user` SELECT * FROM indaba.groupdef_user WHERE groupdef_id IN (SELECT id FROM groupdef_v WHERE organization_id = @org_id);

\! echo 'groupobj'
DROP TABLE IF EXISTS `groupobj`;
CREATE TABLE IF NOT EXISTS `groupobj` LIKE `indaba`.`groupobj`;
INSERT INTO `groupobj` SELECT * FROM indaba.groupobj WHERE groupdef_id IN (SELECT id FROM groupdef_v WHERE organization_id = @org_id);

\! echo 'groupobj_comment'
DROP TABLE IF EXISTS `groupobj_comment`;
CREATE TABLE IF NOT EXISTS `groupobj_comment` LIKE `indaba`.`groupobj_comment`;
INSERT INTO `groupobj_comment` SELECT * FROM indaba.groupobj_comment WHERE groupobj_id IN (SELECT go.id FROM groupdef_v gd JOIN indaba.groupobj go ON gd.id = go.groupdef_id WHERE organization_id = @org_id);

\! echo 'groupobj_flag'
DROP TABLE IF EXISTS `groupobj_flag`;
CREATE TABLE IF NOT EXISTS `groupobj_flag` LIKE `indaba`.`groupobj_flag`;
INSERT INTO `groupobj_flag` SELECT * FROM indaba.groupobj_flag WHERE groupobj_id IN (SELECT go.id FROM groupdef_v gd JOIN indaba.groupobj go ON gd.id = go.groupdef_id WHERE organization_id = @org_id);

\! echo 'apicall'
DROP TABLE IF EXISTS `apicall`;
CREATE TABLE IF NOT EXISTS `apicall` LIKE `indaba`.`apicall`;
INSERT INTO `apicall` SELECT * FROM indaba.apicall WHERE organization_id = @org_id;

\! echo 'cases'
DROP TABLE IF EXISTS `cases`;
CREATE TABLE IF NOT EXISTS `cases` LIKE `indaba`.`cases`;
INSERT INTO `cases` SELECT * FROM indaba.cases WHERE opened_by_user_id IN (SELECT id FROM indaba.user WHERE  organization_id = @org_id);

\! echo 'case_object'
DROP TABLE IF EXISTS `case_object`;
CREATE TABLE IF NOT EXISTS `case_object` LIKE `indaba`.`case_object`;
INSERT INTO `case_object` SELECT * FROM indaba.case_object WHERE cases_id IN (SELECT c.id FROM indaba.user u JOIN  indaba.cases c ON u.id = c.opened_by_user_id  WHERE  organization_id = @org_id);

\! echo 'case_attachment'
DROP TABLE IF EXISTS `case_attachment`;
CREATE TABLE IF NOT EXISTS `case_attachment` LIKE `indaba`.`case_attachment`;
INSERT INTO `case_attachment` SELECT * FROM indaba.case_attachment WHERE cases_id IN (SELECT c.id FROM indaba.user u JOIN  indaba.cases c ON u.id = c.opened_by_user_id  WHERE  organization_id = @org_id);

\! echo 'case_tag'
DROP TABLE IF EXISTS `case_tag`;
CREATE TABLE IF NOT EXISTS `case_tag` LIKE `indaba`.`case_tag`;
INSERT INTO `case_tag` SELECT * FROM indaba.case_tag WHERE cases_id IN (SELECT c.id FROM indaba.user u JOIN  indaba.cases c ON u.id = c.opened_by_user_id  WHERE  organization_id = @org_id);

\! echo 'ctags'
DROP TABLE IF EXISTS `ctags`;
CREATE TABLE IF NOT EXISTS `ctags` LIKE `indaba`.`ctags`;
INSERT INTO `ctags` SELECT * FROM indaba.ctags WHERE id IN (SELECT ct.ctags_id FROM indaba.user u JOIN  indaba.cases c ON u.id = c.opened_by_user_id JOIN indaba.case_tag ct ON ct.cases_id = c.id WHERE  organization_id = @org_id);

\! echo 'team'
DROP TABLE IF EXISTS `team`;
CREATE TABLE IF NOT EXISTS `team` LIKE `indaba`.`team`;
INSERT INTO `team` SELECT * FROM indaba.team WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

\! echo 'team_user'
DROP TABLE IF EXISTS `team_user`;
CREATE TABLE IF NOT EXISTS `team_user` LIKE `indaba`.`team_user`;
INSERT INTO `team_user` SELECT * FROM indaba.team_user WHERE team_id IN (SELECT t.id FROM indaba.project p JOIN indaba.team t ON p.id = t.project_id WHERE organization_id = @org_id);

\! echo 'userfinder'
DROP TABLE IF EXISTS `userfinder`;
CREATE TABLE IF NOT EXISTS `userfinder` LIKE `indaba`.`userfinder`;
INSERT INTO `userfinder` SELECT * FROM indaba.userfinder WHERE project_id IN (SELECT id FROM indaba.project WHERE organization_id = @org_id);

\! echo 'userfinder_event'
DROP TABLE IF EXISTS `userfinder_event`;
CREATE TABLE IF NOT EXISTS `userfinder_event` LIKE `indaba`.`userfinder_event`;
INSERT INTO `userfinder_event` SELECT * FROM indaba.userfinder_event WHERE userfinder_id IN (SELECT uf.id FROM indaba.project p JOIN indaba.userfinder uf ON p.id = uf.project_id WHERE organization_id = @org_id);

\! echo 'can''t really split event and event_log by organization'
\! echo 'event'
DROP TABLE IF EXISTS `event`;
CREATE TABLE IF NOT EXISTS `event` LIKE `indaba`.`event`;
INSERT INTO `event` SELECT * FROM indaba.event;

\! echo 'event_log'
DROP TABLE IF EXISTS `event_log`;
CREATE TABLE IF NOT EXISTS `event_log` LIKE `indaba`.`event_log`;
INSERT INTO `event_log` SELECT * FROM indaba.event_log;

\! echo 'orgadmin'
DROP TABLE IF EXISTS `orgadmin`;
CREATE TABLE IF NOT EXISTS `orgadmin` LIKE `indaba`.`orgadmin`;
INSERT INTO `orgadmin` SELECT * FROM indaba.orgadmin WHERE organization_id = @org_id;

\! echo 'orgkey'
DROP TABLE IF EXISTS `orgkey`;
CREATE TABLE IF NOT EXISTS `orgkey` LIKE `indaba`.`orgkey`;
INSERT INTO `orgkey` SELECT * FROM indaba.orgkey WHERE organization_id = @org_id;




\! echo 'journal_attachment_version TODO - find a link to core tables'
DROP TABLE IF EXISTS `journal_attachment_version`;
CREATE TABLE IF NOT EXISTS `journal_attachment_version` LIKE `indaba`.`journal_attachment_version`;
INSERT INTO `journal_attachment_version` SELECT * FROM indaba.journal_attachment_version;

\! echo 'journal_content_version TODO - find a link to core tables'
DROP TABLE IF EXISTS `journal_content_version`;
CREATE TABLE IF NOT EXISTS `journal_content_version` LIKE `indaba`.`journal_content_version`;
INSERT INTO `journal_content_version` SELECT * FROM indaba.journal_content_version;


\! echo 'atc_choice TODO - find a link to core tables'
DROP TABLE IF EXISTS `atc_choice`;
CREATE TABLE IF NOT EXISTS `atc_choice` LIKE `indaba`.`atc_choice`;
INSERT INTO `atc_choice` SELECT * FROM indaba.atc_choice

\! echo 'atc_choice_intl TODO - find a link to core tables'
DROP TABLE IF EXISTS `atc_choice_intl`;
CREATE TABLE IF NOT EXISTS `atc_choice_intl` LIKE `indaba`.`atc_choice_intl`;
INSERT INTO `atc_choice_intl` SELECT * FROM indaba.atc_choice_intl
