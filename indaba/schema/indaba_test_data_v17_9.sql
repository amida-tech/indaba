# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.5.9)
# Database: indaba
# Generation Time: 2012-03-05 21:39:08 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table access_matrix
# ------------------------------------------------------------

LOCK TABLES `access_matrix` WRITE;
/*!40000 ALTER TABLE `access_matrix` DISABLE KEYS */;

INSERT INTO `access_matrix` (`id`, `name`, `description`, `default_value`)
VALUES
	(1,'Project Default','The default access matrix for projects',1);

/*!40000 ALTER TABLE `access_matrix` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table access_permission
# ------------------------------------------------------------

LOCK TABLES `access_permission` WRITE;
/*!40000 ALTER TABLE `access_permission` DISABLE KEYS */;

INSERT INTO `access_permission` (`id`, `access_matrix_id`, `role_id`, `rights_id`, `permission`)
VALUES
	(1,1,2,1,1),
	(2,1,5,3,0),
	(3,1,6,3,0),
	(4,1,7,3,0),
	(5,1,7,4,0),
	(6,1,6,4,0),
	(7,1,5,4,0),
	(8,1,5,6,0),
	(9,1,6,6,0),
	(12,1,7,6,0),
	(13,1,7,7,0),
	(14,1,6,7,0),
	(15,1,5,7,0),
	(16,1,4,7,0),
	(17,1,3,7,0),
	(18,1,8,7,0),
	(19,1,9,7,0),
	(20,1,3,9,0),
	(21,1,4,9,0),
	(22,1,5,9,0),
	(24,1,6,9,0),
	(25,1,7,9,0),
	(26,1,8,9,0),
	(27,1,9,9,0),
	(29,1,9,10,1),
	(30,1,7,10,0),
	(31,1,6,10,0),
	(32,1,5,10,0),
	(33,1,5,11,0),
	(34,1,6,11,0),
	(35,1,7,11,0),
	(36,1,7,12,0),
	(37,1,6,12,0),
	(38,1,5,12,0),
	(39,1,3,13,0),
	(40,1,4,13,0),
	(41,1,5,13,0),
	(42,1,6,13,0),
	(43,1,7,13,0),
	(45,1,8,13,1),
	(46,1,9,13,1),
	(47,1,5,14,0),
	(48,1,6,14,0),
	(49,1,7,14,0),
	(50,1,7,15,0),
	(51,1,6,15,0),
	(53,1,5,15,0),
	(54,1,5,16,0),
	(55,1,6,16,0),
	(56,1,8,16,0),
	(57,1,7,16,0),
	(58,1,7,19,0),
	(59,1,6,19,0),
	(60,1,5,19,0),
	(63,1,5,20,0),
	(62,1,6,20,0),
	(64,1,7,20,0),
	(65,1,7,21,0),
	(66,1,6,21,0),
	(67,1,5,21,0),
	(68,1,8,21,0),
	(69,1,4,21,0),
	(70,1,3,21,0),
	(71,1,9,21,1),
	(72,1,3,22,0),
	(73,1,5,22,0),
	(74,1,6,22,0),
	(75,1,7,22,0),
	(76,1,8,22,0),
	(77,1,8,23,0),
	(78,1,7,23,0),
	(79,1,6,23,0),
	(80,1,5,23,0),
	(81,1,4,23,0),
	(82,1,3,23,0),
	(83,1,3,24,0),
	(84,1,4,24,0),
	(85,1,5,24,0),
	(86,1,6,24,0),
	(87,1,7,24,0),
	(88,1,8,24,0),
	(89,1,8,25,0),
	(90,1,7,25,0),
	(91,1,6,25,0),
	(92,1,5,25,0),
	(93,1,4,25,0),
	(94,1,3,25,0),
	(95,1,3,26,0),
	(96,1,4,26,0),
	(97,1,5,26,0),
	(98,1,6,26,0),
	(99,1,7,26,0),
	(100,1,8,26,0),
	(101,1,8,27,0),
	(102,1,7,27,0),
	(103,1,6,27,0),
	(104,1,5,27,0),
	(105,1,4,27,0),
	(106,1,3,27,0),
	(107,1,4,28,0),
	(108,1,5,28,0),
	(109,1,6,28,0),
	(110,1,8,28,0),
	(111,1,7,28,0),
	(112,1,7,29,0),
	(114,1,8,29,0),
	(115,1,6,29,0),
	(116,1,5,29,0),
	(117,1,4,29,0),
	(118,1,3,29,0),
	(119,1,3,30,0),
	(120,1,4,30,0),
	(121,1,5,30,0),
	(122,1,6,30,0),
	(123,1,7,30,0),
	(124,1,8,30,0),
	(125,1,8,31,0),
	(127,1,7,31,0),
	(128,1,6,31,0),
	(129,1,5,31,0),
	(130,1,4,31,0),
	(131,1,3,31,0),
	(132,1,8,32,0),
	(133,1,8,33,0),
	(134,1,7,33,0),
	(135,1,6,33,0),
	(136,1,5,33,0),
	(137,1,6,36,0),
	(138,1,7,36,0),
	(139,1,6,37,0),
	(140,1,7,37,0);

/*!40000 ALTER TABLE `access_permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table announcement
# ------------------------------------------------------------



# Dump of table answer_object_choice
# ------------------------------------------------------------

LOCK TABLES `answer_object_choice` WRITE;
/*!40000 ALTER TABLE `answer_object_choice` DISABLE KEYS */;

INSERT INTO `answer_object_choice` (`id`, `choices`)
VALUES
	(1,0),
	(2,0),
	(3,0),
	(4,0),
	(5,0),
	(6,0),
	(7,0),
	(8,0),
	(9,0),
	(10,0),
	(11,0),
	(12,0),
	(13,0),
	(14,0),
	(15,0),
	(16,0),
	(17,0),
	(18,0),
	(19,0),
	(20,0),
	(21,0),
	(22,0);

/*!40000 ALTER TABLE `answer_object_choice` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table answer_object_float
# ------------------------------------------------------------

LOCK TABLES `answer_object_float` WRITE;
/*!40000 ALTER TABLE `answer_object_float` DISABLE KEYS */;

INSERT INTO `answer_object_float` (`id`, `value`)
VALUES
	(1,NULL),
	(2,NULL),
	(3,NULL);

/*!40000 ALTER TABLE `answer_object_float` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table answer_object_integer
# ------------------------------------------------------------

LOCK TABLES `answer_object_integer` WRITE;
/*!40000 ALTER TABLE `answer_object_integer` DISABLE KEYS */;

INSERT INTO `answer_object_integer` (`id`, `value`)
VALUES
	(1,NULL),
	(2,NULL);

/*!40000 ALTER TABLE `answer_object_integer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table answer_object_text
# ------------------------------------------------------------



# Dump of table answer_type_choice
# ------------------------------------------------------------

LOCK TABLES `answer_type_choice` WRITE;
/*!40000 ALTER TABLE `answer_type_choice` DISABLE KEYS */;

INSERT INTO `answer_type_choice` (`id`)
VALUES
	(1),
	(2),
	(3),
	(4),
	(5),
	(6),
	(7),
	(8),
	(9),
	(10),
	(11),
	(12),
	(13),
	(14),
	(15),
	(16),
	(17),
	(18),
	(19),
	(20),
	(21),
	(22),
	(23),
	(24);

/*!40000 ALTER TABLE `answer_type_choice` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table answer_type_float
# ------------------------------------------------------------

LOCK TABLES `answer_type_float` WRITE;
/*!40000 ALTER TABLE `answer_type_float` DISABLE KEYS */;

INSERT INTO `answer_type_float` (`id`, `min_value`, `max_value`, `default_value`, `criteria`)
VALUES
	(1,0,1000,NULL,'Average hourly wage is across all states in the nation.'),
	(2,1,100,NULL,'Major cities are the executive regions directly under the central government'),
	(3,1,100,NULL,'Major cities are the executive regions directly under the central government');

/*!40000 ALTER TABLE `answer_type_float` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table answer_type_integer
# ------------------------------------------------------------

LOCK TABLES `answer_type_integer` WRITE;
/*!40000 ALTER TABLE `answer_type_integer` DISABLE KEYS */;

INSERT INTO `answer_type_integer` (`id`, `min_value`, `max_value`, `default_value`, `criteria`)
VALUES
	(1,1,100,NULL,'States and provinces are the executive regions directly under the central government'),
	(2,1,100,NULL,'Major cities are the executive regions directly under the central government');

/*!40000 ALTER TABLE `answer_type_integer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table answer_type_text
# ------------------------------------------------------------

LOCK TABLES `answer_type_text` WRITE;
/*!40000 ALTER TABLE `answer_type_text` DISABLE KEYS */;

INSERT INTO `answer_type_text` (`id`, `min_chars`, `max_chars`, `criteria`)
VALUES
	(1,1,1000,'The biggest cities are based on the size of populations');

/*!40000 ALTER TABLE `answer_type_text` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table atc_choice
# ------------------------------------------------------------

LOCK TABLES `atc_choice` WRITE;
/*!40000 ALTER TABLE `atc_choice` DISABLE KEYS */;

INSERT INTO `atc_choice` (`id`, `answer_type_choice_id`, `label`, `score`, `criteria`, `weight`, `mask`, `default_selected`, `use_score`)
VALUES
	(1,1,'YES',1000000,'A YES score is earned when freedom to assemble into groups promoting good governance or anti-corruption is protected by law, regardless of political ideology, religion or objectives. Groups with a history of violence or terrorism (within last ten years) may be banned. Groups sympathetic to or related to banned groups must be allowed if they have no history of violence.',1,1,0,1),
	(2,1,'NO',0,'A NO score is earned when any single non-violent group is legally prohibited from organizing to promote good governance or anti-corruption. These groups may include non-violent separatist groups, political parties or religious groups.',2,2,1,1),
	(5,2,'YES',1000000,'A YES score is earned if anti-corruption/good governance CSOs face no legal or regulatory restrictions to raise or accept funds from any foreign or domestic sources.  A YES score may still be earned if funds from groups with a history of violence or terrorism (within last ten years) are banned.',1,1,0,1),
	(6,2,'NO',0,'A NO score is earned if there any formal legal or regulatory bans on foreign or domestic funding sources for CSOs focused on anti-corruption or good governance.',2,2,1,1),
	(7,3,'YES',1000000,'A YES score is earned if anti-corruption/good governance CSOs are required to publicly disclose their sources of funding.',1,1,0,1),
	(10,4,'50',500000,'CSOs focused on promoting good governance or anti-corruption must go through formal steps to form, requiring interaction with the state such as licenses or registration. Formation is possible, though there is some burden on the CSO. Some unofficial barriers, such as harassment of minority groups, may occur.',2,2,0,1),
	(8,3,'NO',0,'A NO score is earned if no such public disclosure requirement exists.',2,2,1,1),
	(9,4,'100',1000000,'CSOs focused on promoting good governance or anti-corruption can freely organize with little to no interaction with the government, other than voluntary registration.',1,1,0,1),
	(11,4,'0',0,'Other than pro-government groups, CSOs focused on promoting good governance or anti-corruption are effectively prohibited, either by official requirements or by unofficial means, such as intimidation or fear.',3,4,0,1),
	(12,5,'100',1000000,'Civil society organizations focused on anti-corruption or good governance are an essential component of the political process. CSOs provide widely valued insights and have political power. Those CSOs play a leading role in shaping public opinion on political matters.',1,1,0,1),
	(13,5,'50',500000,'Anti-corruption/good governance CSOs are active, but may not be relevant to political decisions or the policymaking process. Those CSOs are willing to articulate opinions on political matters, but have little access to decision makers. They have some influence over public opinion, but considerably less than political figures.',2,2,0,1),
	(19,5,'0',0,'Anti-corruption/good governance CSOs are effectively prohibited from engaging in the political process. Those CSOs are unwilling to take positions on political issues. They are not relevant to changes in public opinion.',4,8,0,1),
	(24,8,'YES',1000000,'A YES score is earned if there were no CSOs shut down by the government or forced to cease operations because of their work on corruption-related issues during the study period.  YES is a positive score.',1,1,0,1),
	(25,8,'NO',0,'A NO score is earned if any CSO has been effectively shut down by the government or forced to cease operations because of its work on corruption-related issues during the study period.  The causal relationship between the cessation of operations and the CSO\'s work may not be explicit, however the burden of proof here is low. If it seems likely that the CSO was forced to cease operations due to its work, then the indicator is scored as a NO. Corruption is defined broadly to include any abuses of power, not just the passing of bribes.',2,2,0,1),
	(26,9,'Protest',500000,'Citizens can organize protests against government corruptions legally.',1,1,0,1),
	(27,9,'Strike',200000,'Citizens can organize strikes against government corruptions legally.',2,2,0,1),
	(28,9,'Social Media',200000,'Citizens can criticize government corruptions using social media such as newspapers, websites, etc.',3,4,0,1),
	(29,9,'Law Suit',100000,'Citizens can file law suits against corrupted government officials.',4,8,0,1),
	(30,10,'YES',1000000,'A YES score is earned if there is a statutory or other framework enshrined in law that mandates elections at reasonable intervals.',1,1,0,1),
	(31,10,'NO',0,'A NO score is earned if no such framework exists.',2,2,0,1),
	(32,11,'100',1000000,'Voting is open to all citizens regardless of race, gender, prior political affiliations, physical disability, or other traditional barriers.',1,1,0,1),
	(33,11,'50',500000,'Voting is often open to all citizens regardless of race, gender, prior political affiliations, physical disability, or other traditional barriers, with some exceptions.',2,2,0,1),
	(34,11,'00',0,'Voting is not available to some demographics through some form of official or unofficial pressure. Voting may be too dangerous, expensive, or difficult for many people.',3,4,0,1),
	(35,12,'100',1000000,'Ballots are secret, or there is a functional equivalent protection, in all cases.',1,1,0,1),
	(36,12,'50',500000,'Ballots are secret, or there is a functional equivalent protection, in most cases. Some exceptions to this practice have occurred.  Ballots may be subject to tampering during transport or counting.',2,2,0,1),
	(37,12,'0',0,'Ballot preferences are not secret.  Ballots are routinely tampered with during transport and counting.',3,4,0,1),
	(38,13,'100',1000000,'Elections are always held according to a regular schedule, or there is a formal democratic process for calling a new election, with deadlines for mandatory elections.',1,1,0,1),
	(39,13,'50',500000,'Elections are normally held according to a regular schedule, but there have been recent exceptions. The formal process for calling a new election may be flawed or abused.',2,2,0,1),
	(40,13,'00',0,'Elections are called arbitrarily by the government. There is no functioning schedule or deadline for new elections.',3,4,0,1),
	(41,14,'YES',1000000,'Answer YES if the government has laws, regulations about energy conservation and/or alternative energy.',1,1,0,1),
	(42,14,'NO',0,'Answer NO if the government does not have such laws or regulations.',2,2,0,1),
	(43,15,'Coal',-500000,'Coal is a major energy source.',1,1,0,1),
	(44,15,'Nuclear',-100000,'Nuclear energy is a major energy source.',2,2,0,1),
	(45,15,'Solar',0,'Solar is a major energy source',3,4,0,1),
	(46,15,'Wind',0,'Wind is a major energy source',4,8,0,1),
	(47,15,'Water',-5,'Water is a major energy source',5,16,0,1),
	(48,16,'YES',1000000,'Answer YES if the government offers incentives for the use of green energies.',1,1,0,1),
	(49,16,'NO',0,'Answer NO if the government does not offer such programs.',2,2,0,1),
	(50,17,'Coal',0,'Check this if Coal is the primary energy source in the nation.',1,1,0,1),
	(51,17,'Nuclear',500000,'Check this if Nuclear is the primary energy source in the nation.',2,2,0,1),
	(52,17,'Solar',1000000,'Check this if solar energy is the primary energy source in the nation.',3,4,0,1),
	(53,17,'Wind',1000000,'Check this if Wind is the primary energy source in the nation.',4,8,0,1),
	(54,17,'Water',800000,'Check this if Water is the primary energy source in the nation.',5,16,0,1),
	(55,18,'YES',1000000,'A YES score is earned if freedom of the press is guaranteed in law, including to all political parties, religions, and ideologies.',1,1,0,1),
	(56,18,'NO',0,'A NO score is earned if any specific publication relating to government affairs is legally banned, or any general topic is prohibited from publication. Specific restrictions on media regarding privacy or slander are allowed, but not if these amount to legal censorship of a general topic, such as corruption or defense.  A NO score is earned if non-government media is prohibited or restricted.',2,2,0,1),
	(57,19,'YES',1000000,'A YES score is earned if freedom of individual speech is guaranteed in law, including to all political parties, religions, and ideologies.',1,1,0,1),
	(58,19,'NO',0,'A NO score is earned if any individual speech is legally prohibited, regardless of topic. Specific exceptions for speech linked with a criminal act, such as a prohibition on death threats, are allowed. However, any non-specific prohibition earns a NO score.',2,2,0,1),
	(59,20,'100',1000000,'Print media entities can freely organize with little to no interaction with the government. This score may still be earned if groups or individuals with a history of political violence or terrorism (within last ten years) are banned from forming media entities.',1,1,0,1),
	(60,20,'50',500000,'Formation of print media groups is possible, though there is some burden on the media group including overly complicated registration or licensing requirements. Some unofficial barriers, such as harassment of minority groups, may occur.',2,2,0,1),
	(61,20,'000',0,'Print media groups are effectively prohibited, either by official requirements or by unofficial means, such as intimidation or fear.',3,4,0,1),
	(62,21,'YES',1000000,'A YES score is earned if there is, in law or in accompanying regulations, a formal process to appeal a denied print media license, including through the courts. A YES score is also earned if no print license is necessary.',1,1,0,1),
	(63,21,'NO',0,'A NO score is earned if there is no appeal process for print media licenses.',2,2,0,1),
	(64,22,'100',1000000,'Licenses are not required or licenses can be obtained within two months.',1,1,0,1),
	(65,22,'50',500000,'Licensing is required and takes more than two months.  Some groups may be delayed up to six months.',2,2,0,1),
	(66,22,'00',0,'Licensing takes close to or more than one year for most groups.',3,4,0,1),
	(67,23,'100',1000000,'Licenses are not required or can be obtained at minimal cost to the organization.  Licenses can be obtained on-line or through the mail.',1,1,0,1),
	(68,23,'50',500000,'Licenses are required, and impose a financial burden on the organization. Licenses may require a visit to a specific office, such as a regional or national capital.',2,2,0,1),
	(69,23,'00',0,'Licenses are required, and impose a major financial burden on the organization. Licensing costs are prohibitive to the organization.',3,4,0,1),
	(70,24,'YES',1000000,'A YES score is earned if the right to vote is guaranteed to all citizens of the country (basic age limitations are allowed). A YES score can still be earned if voting procedures are, in practice, inconvenient or unfair.',1,1,0,1),
	(71,24,'NO',0,'A NO score is earned if suffrage is denied by law to any group of adult citizens for any reason. Citizen is defined broadly, to include all ethnicities, or anyone born in the country.  A NO score is earned if homeless or impoverished people are legally prohibited from voting.',2,2,1,1);

/*!40000 ALTER TABLE `atc_choice` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table attachment
# ------------------------------------------------------------



# Dump of table case_attachment
# ------------------------------------------------------------



# Dump of table case_object
# ------------------------------------------------------------



# Dump of table case_tag
# ------------------------------------------------------------



# Dump of table cases
# ------------------------------------------------------------



# Dump of table config
# ------------------------------------------------------------

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;

INSERT INTO `config` (`id`, `default_language_id`, `platform_name`, `platform_admin_user_id`)
VALUES
	(1,1,'Indaba',1);

/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table content_approval
# ------------------------------------------------------------



# Dump of table content_header
# ------------------------------------------------------------

LOCK TABLES `content_header` WRITE;
/*!40000 ALTER TABLE `content_header` DISABLE KEYS */;

INSERT INTO `content_header` (`id`, `project_id`, `content_type`, `content_object_id`, `horse_id`, `title`, `author_user_id`, `create_time`, `last_update_time`, `last_update_user_id`, `delete_time`, `deleted_by_user_id`, `status`, `internal_msgboard_id`, `staff_author_msgboard_id`, `editable`, `reviewable`, `peer_reviewable`, `approvable`, `submit_time`)
VALUES
	(1,1,1,1,1,'US - Notebook',16,'2010-06-16 00:00:00','2010-08-08 10:19:36',16,NULL,0,0,2,1,0,0,0,0,NULL),
	(2,1,0,1,2,'US - Scorecard',9,'2010-06-16 10:33:42',NULL,0,NULL,0,0,0,8,0,0,0,0,NULL),
	(3,1,1,2,3,'Argentina - Notebook',13,'2010-06-16 10:33:49',NULL,0,NULL,0,0,0,0,0,0,0,0,NULL),
	(4,1,1,3,4,'Brazil - Notebook',14,'2010-06-16 10:33:49',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(5,1,1,4,5,'China - Notebook',15,'2010-06-16 10:33:49',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(6,1,0,2,6,'Brazil - Scorecard',11,'2010-06-16 10:33:55',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(7,1,0,3,7,'Argentina - Scorecard',10,'2010-06-16 10:33:55',NULL,0,NULL,0,0,0,0,0,0,0,0,NULL),
	(8,1,0,4,8,'China - Scorecard',12,'2010-06-16 10:33:55',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*!40000 ALTER TABLE `content_header` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table content_payment
# ------------------------------------------------------------



# Dump of table content_version
# ------------------------------------------------------------



# Dump of table ctags
# ------------------------------------------------------------

LOCK TABLES `ctags` WRITE;
/*!40000 ALTER TABLE `ctags` DISABLE KEYS */;

INSERT INTO `ctags` (`id`, `term`, `description`)
VALUES
	(1,'Administrative',''),
	(2,'Technical',''),
	(3,'Editorial',''),
	(4,'Contractual','');

/*!40000 ALTER TABLE `ctags` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table event
# ------------------------------------------------------------

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;

INSERT INTO `event` (`id`, `event_log_id`, `event_type`, `event_data`, `user_id`, `timestamp`)
VALUES
	(1,1,2,'User logout',3,'2010-07-29 01:42:12'),
	(2,1,1,'User login',16,'2010-07-29 01:42:17'),
	(3,1,3,'User click the assigned task. [assignid=1][toolid=1][prdtype=1]',16,'2010-07-29 01:42:18'),
	(4,1,5,'User submit the content for the assigned task. [assignid=1][toolid=1][prdyype=1]',16,'2010-07-29 01:42:31'),
	(5,1,2,'User logout',16,'2010-07-29 01:42:34'),
	(6,1,1,'User login',3,'2010-07-29 01:42:37'),
	(7,1,2,'User logout',3,'2010-07-29 01:42:59'),
	(8,1,1,'User login',16,'2010-07-29 01:43:02'),
	(9,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:54:04'),
	(10,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:54:28'),
	(11,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:54:35'),
	(12,1,2,'User logout',16,'2010-08-08 09:54:44'),
	(13,1,1,'User login',16,'2010-08-08 09:54:46'),
	(14,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:54:48'),
	(15,1,4,'User submit the content for the assigned task. [assignid=5][toolid=1][prdyype=1]',16,'2010-08-08 09:55:00'),
	(16,1,4,'User submit the content for the assigned task. [assignid=5][toolid=-1][prdyype=1]',16,'2010-08-08 09:55:19'),
	(17,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:55:39'),
	(18,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:55:39'),
	(19,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:55:45'),
	(20,1,2,'User logout',16,'2010-08-08 09:55:51'),
	(21,1,1,'User login',16,'2010-08-08 09:55:52'),
	(22,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 09:55:56'),
	(23,1,1,'User login',16,'2010-08-08 10:05:22'),
	(24,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 10:05:25'),
	(25,1,4,'User submit the content for the assigned task. [assignid=5][toolid=1][prdyype=1]',16,'2010-08-08 10:07:04'),
	(26,1,3,'User click the assigned task. [assignid=5][toolid=1][prdtype=1]',16,'2010-08-08 10:07:12'),
	(27,1,5,'User submit the content for the assigned task. [assignid=5][toolid=1][prdyype=1]',16,'2010-08-08 10:07:23'),
	(28,1,1,'User login',16,'2010-08-08 10:10:12'),
	(29,1,3,'User click the assigned task. [assignid=101][toolid=16][prdtype=1]',16,'2010-08-08 10:10:40'),
	(30,1,4,'User submit the content for the assigned task. [assignid=101][toolid=16][prdyype=1]',16,'2010-08-08 10:11:05'),
	(31,1,4,'User submit the content for the assigned task. [assignid=101][toolid=-1][prdyype=1]',16,'2010-08-08 10:11:17'),
	(32,1,3,'User click the assigned task. [assignid=101][toolid=16][prdtype=1]',16,'2010-08-08 10:19:20'),
	(33,1,5,'User submit the content for the assigned task. [assignid=101][toolid=16][prdyype=1]',16,'2010-08-08 10:19:36'),
	(34,1,2,'User logout',16,'2010-08-08 10:59:53'),
	(35,1,1,'User login',5,'2010-08-08 10:59:57'),
	(36,1,1,'User login',16,'2010-08-08 11:06:42'),
	(37,1,1,'User login',16,'2010-08-08 15:59:28'),
	(38,1,3,'User click the assigned task. [assignid=1][toolid=1][prdtype=1]',16,'2010-08-08 15:59:33'),
	(39,1,3,'User click the assigned task. [assignid=1][toolid=1][prdtype=1]',16,'2010-08-08 16:05:36'),
	(40,1,3,'User click the assigned task. [assignid=1][toolid=1][prdtype=1]',16,'2010-08-08 16:06:07'),
	(41,1,1,'common.msg.event.login',16,'2012-02-21 23:40:35'),
	(42,1,1,'common.msg.event.login',16,'2012-02-22 00:10:11'),
	(43,1,3,'User click the assigned task. [assignid=1][toolid=1][prdtype=1]',16,'2012-02-22 00:12:51'),
	(44,1,1,'User Login',10,'2012-03-05 10:12:46'),
	(45,1,1,'User Login',9,'2012-03-05 10:13:28'),
	(46,1,1,'User Login',9,'2012-03-05 10:53:22'),
	(47,1,1,'User Login',9,'2012-03-05 10:55:47'),
	(48,1,2,'User Logout',9,'2012-03-05 10:55:53'),
	(49,1,1,'User Login',5,'2012-03-05 10:56:02'),
	(50,1,2,'User Logout',5,'2012-03-05 11:01:15'),
	(51,1,1,'User Login',10,'2012-03-05 11:01:21'),
	(52,1,1,'User Login',10,'2012-03-05 11:03:01'),
	(53,1,1,'User Login',9,'2012-03-05 11:43:13'),
	(54,1,2,'User Logout',9,'2012-03-05 11:57:41'),
	(55,1,1,'User Login',10,'2012-03-05 11:57:51'),
	(56,1,2,'User Logout',10,'2012-03-05 11:58:24'),
	(57,1,1,'User Login',6,'2012-03-05 11:58:32'),
	(58,1,1,'User Login',9,'2012-03-05 12:41:11'),
	(59,1,1,'User Login',9,'2012-03-05 13:02:01'),
	(60,1,1,'User Login',9,'2012-03-05 13:11:40'),
	(61,1,1,'User Login',9,'2012-03-05 13:14:44'),
	(62,1,2,'User Logout',9,'2012-03-05 13:14:55'),
	(63,1,1,'User Login',10,'2012-03-05 13:15:00'),
	(64,1,2,'User Logout',10,'2012-03-05 13:15:03'),
	(65,1,1,'User Login',10,'2012-03-05 13:15:10'),
	(66,1,1,'User Login',5,'2012-03-05 13:28:03'),
	(67,1,1,'User Login',9,'2012-03-05 13:32:27');

/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table event_log
# ------------------------------------------------------------



# Dump of table goal
# ------------------------------------------------------------

LOCK TABLES `goal` WRITE;
/*!40000 ALTER TABLE `goal` DISABLE KEYS */;

INSERT INTO `goal` (`id`, `workflow_sequence_id`, `weight`, `name`, `description`, `access_matrix_id`, `duration`, `entrance_rule_file_name`, `inflight_rule_file_name`, `exit_rule_file_name`, `entrance_rule_desc`, `inflight_rule_desc`, `exit_rule_desc`)
VALUES
	(1,1,2,'nb_edit_1','First edit',0,5,NULL,NULL,NULL,'If task not assigned, send msgs to all editors to claim the task;Otherwise send notification to assigned user about the task;','If the task becomes unassigned for more than 1 day, send msg to all editors to claim the task','Exit if assignment is done.'),
	(2,1,3,'nb_edit_2','2nd edit',0,5,NULL,NULL,NULL,'If task not assigned, send messages to all editors to claim assignments from queue;Otherwise send notification to assigned user about the task;','If the task becomes unassigned for more than 1 day, send msg to editors to claim the task','Exit if all assignments are done'),
	(3,1,4,'nb_edit_review','Review editing results',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim the task;Otherwise send notification to assigned user about the task;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim the task','Exit if the assignment is done'),
	(4,1,9,'nb_manager_review','Final manager review',0,2,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about the task;','Nothing','Exit when the assignment is done;Set the content status to DONE;Send msg to project admin about the completion of the horse;Send congrats to all users on the horse;'),
	(5,1,7,'nb_peer_review','Peer review',0,10,NULL,NULL,NULL,'Send alert to project admin if no one is assigned to the task;Otherwise send notification to assigned user about the task;','Send 1st reminder 5 days before due; Send 2nd reminder 1 day before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due, and copy project admin;','Send \'Thank You\' notice to the peer reviewer.'),
	(6,1,8,'nb_pr_review','Review results of peer review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim the task;Otherwise send notification to assigned user about the task;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim the task','Exit when assignment is done'),
	(7,1,1,'nb_staff_review_1','First staff review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim the task;Otherwise send notification to assigned user about the task;','If task becomes unassigned for more than one day, send msg to reviewers to claim the task','Exit when assignment is done'),
	(8,1,6,'nb_staff_review_2','Second staff review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim the task; Otherwise send notification to assigned user about the task;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim the task','Exit when the assignment is done'),
	(9,1,0,'nb_submit_1','1st content submission',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about the task;','Send 1st reminder 5 days before due; Send 2nd reminder 1 day before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due, and copy project admin;','Exit when task is done'),
	(10,1,5,'nb_submit_2','2nd submission',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about the task;','Send 1st reminder 5 days before due; Send 2nd reminder 1 day before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due, and copy project admin;','Send \'Thank You\' msg to assigned user.'),
	(11,2,0,'sc_submit_1','1st submission',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;','Exit when assignment is done;'),
	(12,2,1,'sc_review_1','1st staff review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;','Exit when assignment is done;'),
	(13,2,2,'sc_submit_2','2nd submission',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;','Exit when assignment is done;'),
	(14,2,3,'sc_review_2','2nd staff review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;','Exit when assignment is done;'),
	(15,2,4,'sc_peer_review','Peer review',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;','When assignment is done, send thank-you msg to the assigned user;'),
	(16,2,6,'sc_submit_3','3rd submission',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;','Exit when assignment is done;'),
	(17,2,7,'sc_review_3','3rd staff review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;','Exit when assignment is done;'),
	(18,2,5,'sc_pr_review','Review of peer reviews',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;','Exit when assignment is done;'),
	(19,4,0,'sc_pay1','1st payment',0,2,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Nothing','After done, send notification to the content author that the 1st payment has been made;'),
	(20,5,0,'sc_pay2','2nd payment',0,2,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','nothing','After done, send notification to the content author that the 2nd payment has been made;'),
	(21,6,0,'sc_start_horse','Start horse',0,5,NULL,NULL,NULL,'none','none','none'),
	(22,7,1,'sc_edit','Edit scorecard content',0,5,NULL,NULL,NULL,'If task not assigned, send msg to editors to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to editors to claim task;','Exit when assignment is done;'),
	(25,11,1,'ep_submit','Submit content',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;','Exit when assignment is done;'),
	(24,8,0,'sc_finish','Finish up',0,1,NULL,NULL,NULL,'If task not assigned, send alert to project admin.','If task becomes unassigned for more than 1 day, send alert to project admin to assign user;','Set content status to done; Send notification to project admin about the completion of the horse;'),
	(26,11,2,'ep_review','Staff review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;','Exit when assignment is done;'),
	(27,11,3,'ep_edit','Edit content',0,5,NULL,NULL,NULL,'If task not assigned, send msg to editors to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to editors to claim task;','Exit when assignment is done;'),
	(28,11,4,'ep_peer_review','Peer review',0,10,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;','Exit when assignment is done;'),
	(29,11,5,'ep_pr_review','Review of peer review',0,5,NULL,NULL,NULL,'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;','Exit when assignment is done;'),
	(30,11,6,'ep_approve','Approve the content',0,1,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Nothing','Exit when assignment is done;'),
	(31,11,7,'ep_pay','Pay the author',0,1,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','Nothing','Exit when the assignment is done;Send msg to author that the payment has been made;'),
	(32,11,0,'ep_start','Ask to approve start of the workflow',0,1,NULL,NULL,NULL,'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;','If task becomes unassigned for more than 1 day, send alert to project admin;','Exit when assignment is done;');

/*!40000 ALTER TABLE `goal` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table goal_object
# ------------------------------------------------------------

LOCK TABLES `goal_object` WRITE;
/*!40000 ALTER TABLE `goal_object` DISABLE KEYS */;

INSERT INTO `goal_object` (`id`, `goal_id`, `enter_time`, `exit_time`, `status`, `sequence_object_id`, `event_log_id`)
VALUES
	(1,1,NULL,NULL,0,1,NULL),
	(2,2,NULL,NULL,0,1,NULL),
	(3,3,NULL,NULL,0,1,NULL),
	(4,4,NULL,NULL,0,1,NULL),
	(5,5,NULL,NULL,0,1,NULL),
	(6,6,NULL,NULL,0,1,NULL),
	(7,7,NULL,NULL,0,1,NULL),
	(8,8,NULL,NULL,0,1,NULL),
	(9,9,NULL,NULL,0,1,NULL),
	(10,10,NULL,NULL,0,1,NULL),
	(11,15,NULL,NULL,0,2,NULL),
	(12,18,NULL,NULL,0,2,NULL),
	(13,12,NULL,NULL,0,2,NULL),
	(14,14,NULL,NULL,0,2,NULL),
	(15,17,NULL,NULL,0,2,NULL),
	(16,11,NULL,NULL,0,2,NULL),
	(17,13,NULL,NULL,0,2,NULL),
	(18,16,NULL,NULL,0,2,NULL),
	(19,19,NULL,NULL,0,3,NULL),
	(20,20,NULL,NULL,0,4,NULL),
	(21,21,NULL,NULL,0,5,NULL),
	(22,22,NULL,NULL,0,6,NULL),
	(23,24,NULL,NULL,0,7,NULL),
	(24,1,NULL,NULL,0,8,NULL),
	(25,2,NULL,NULL,0,8,NULL),
	(26,3,NULL,NULL,0,8,NULL),
	(27,4,NULL,NULL,0,8,NULL),
	(28,5,NULL,NULL,0,8,NULL),
	(29,6,NULL,NULL,0,8,NULL),
	(30,7,NULL,NULL,0,8,NULL),
	(31,8,NULL,NULL,0,8,NULL),
	(32,9,NULL,NULL,0,8,NULL),
	(33,10,NULL,NULL,0,8,NULL),
	(34,1,NULL,NULL,0,9,NULL),
	(35,2,NULL,NULL,0,9,NULL),
	(36,3,NULL,NULL,0,9,NULL),
	(37,4,NULL,NULL,0,9,NULL),
	(38,5,NULL,NULL,0,9,NULL),
	(39,6,NULL,NULL,0,9,NULL),
	(40,7,NULL,NULL,0,9,NULL),
	(41,8,NULL,NULL,0,9,NULL),
	(42,9,NULL,NULL,0,9,NULL),
	(43,10,NULL,NULL,0,9,NULL),
	(44,1,NULL,NULL,0,10,NULL),
	(45,2,NULL,NULL,0,10,NULL),
	(46,3,NULL,NULL,0,10,NULL),
	(47,4,NULL,NULL,0,10,NULL),
	(48,5,NULL,NULL,0,10,NULL),
	(49,6,NULL,NULL,0,10,NULL),
	(50,7,NULL,NULL,0,10,NULL),
	(51,8,NULL,NULL,0,10,NULL),
	(52,9,NULL,NULL,0,10,NULL),
	(53,10,NULL,NULL,0,10,NULL),
	(54,15,NULL,NULL,0,11,NULL),
	(55,18,NULL,NULL,0,11,NULL),
	(56,12,NULL,NULL,0,11,NULL),
	(57,14,NULL,NULL,0,11,NULL),
	(58,17,NULL,NULL,0,11,NULL),
	(59,11,NULL,NULL,0,11,NULL),
	(60,13,NULL,NULL,0,11,NULL),
	(61,16,NULL,NULL,0,11,NULL),
	(62,19,NULL,NULL,0,12,NULL),
	(63,20,NULL,NULL,0,13,NULL),
	(64,21,NULL,NULL,0,14,NULL),
	(65,22,NULL,NULL,0,15,NULL),
	(66,24,NULL,NULL,0,16,NULL),
	(67,15,NULL,NULL,0,17,NULL),
	(68,18,NULL,NULL,0,17,NULL),
	(69,12,NULL,NULL,0,17,NULL),
	(70,14,NULL,NULL,0,17,NULL),
	(71,17,NULL,NULL,0,17,NULL),
	(72,11,NULL,NULL,0,17,NULL),
	(73,13,NULL,NULL,0,17,NULL),
	(74,16,NULL,NULL,0,17,NULL),
	(75,19,NULL,NULL,0,18,NULL),
	(76,20,NULL,NULL,0,19,NULL),
	(77,21,NULL,NULL,0,20,NULL),
	(78,22,NULL,NULL,0,21,NULL),
	(79,24,NULL,NULL,0,22,NULL),
	(80,15,NULL,NULL,0,23,NULL),
	(81,18,NULL,NULL,0,23,NULL),
	(82,12,NULL,NULL,0,23,NULL),
	(83,14,NULL,NULL,0,23,NULL),
	(84,17,NULL,NULL,0,23,NULL),
	(85,11,NULL,NULL,0,23,NULL),
	(86,13,NULL,NULL,0,23,NULL),
	(87,16,NULL,NULL,0,23,NULL),
	(88,19,NULL,NULL,0,24,NULL),
	(89,20,NULL,NULL,0,25,NULL),
	(90,21,NULL,NULL,0,26,NULL),
	(91,22,NULL,NULL,0,27,NULL),
	(92,24,NULL,NULL,0,28,NULL);

/*!40000 ALTER TABLE `goal_object` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table horse
# ------------------------------------------------------------

LOCK TABLES `horse` WRITE;
/*!40000 ALTER TABLE `horse` DISABLE KEYS */;

INSERT INTO `horse` (`id`, `product_id`, `target_id`, `start_time`, `completion_time`, `content_header_id`, `workflow_object_id`)
VALUES
	(1,1,1,'2010-06-16 10:22:55',NULL,1,1),
	(2,2,1,'2010-06-16 10:33:42',NULL,2,2),
	(3,1,2,'2010-06-16 10:33:49',NULL,3,3),
	(4,1,3,'2010-06-16 10:33:49',NULL,4,4),
	(5,1,4,'2010-06-16 10:33:49',NULL,5,5),
	(6,2,3,'2010-06-16 10:33:55',NULL,6,6),
	(7,2,2,'2010-06-16 10:33:55',NULL,7,7),
	(8,2,4,'2010-06-16 10:33:55',NULL,8,8);

/*!40000 ALTER TABLE `horse` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table indicator_tag
# ------------------------------------------------------------



# Dump of table itags
# ------------------------------------------------------------



# Dump of table journal_attachment_version
# ------------------------------------------------------------



# Dump of table journal_config
# ------------------------------------------------------------

LOCK TABLES `journal_config` WRITE;
/*!40000 ALTER TABLE `journal_config` DISABLE KEYS */;

INSERT INTO `journal_config` (`id`, `name`, `description`, `instructions`, `min_words`, `max_words`, `exportable_items`)
VALUES
	(1,'Anti-Corruption Essay','Journal about anti-corruption status','You are required to write an article about the government\'s anti-corruption effort in your country. Please provide sufficient objective evidence to support your points.\n\nPlease make sure that the article\'s length is from 500 to 1000 words.',500,1000,0),
	(2,'Energy Policy Essay','Analysis of the nation\'s energy policy','Please provide a detailed analysis on the nation\'s energy policy. Use real examples, projects, data to support your view points. ',500,2000,0);

/*!40000 ALTER TABLE `journal_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table journal_content_object
# ------------------------------------------------------------

LOCK TABLES `journal_content_object` WRITE;
/*!40000 ALTER TABLE `journal_content_object` DISABLE KEYS */;

INSERT INTO `journal_content_object` (`id`, `content_header_id`, `body`, `journal_config_id`)
VALUES
	(1,1,'<p>\r\n	ghghfgdhdfh dfgzxx</p>\r\n<p>\r\n	&nbsp;</p>\r\n<p>\r\n	daasasasa&nbsp; dsfdsfdsf</p>\r\n',1),
	(2,3,NULL,1),
	(3,4,NULL,1),
	(4,5,NULL,1);

/*!40000 ALTER TABLE `journal_content_object` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table journal_content_version
# ------------------------------------------------------------



# Dump of table journal_peer_review
# ------------------------------------------------------------



# Dump of table language
# ------------------------------------------------------------

LOCK TABLES `language` WRITE;
/*!40000 ALTER TABLE `language` DISABLE KEYS */;

INSERT INTO `language` (`id`, `language`, `language_desc`, `status`)
VALUES
	(1,'en','English',0),
	(4,'cn','Chinese',1),
	(3,'es','Spanish',1),
	(2,'fr','French',0);

/*!40000 ALTER TABLE `language` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table message
# ------------------------------------------------------------



# Dump of table msg_reading_status
# ------------------------------------------------------------



# Dump of table msgboard
# ------------------------------------------------------------

LOCK TABLES `msgboard` WRITE;
/*!40000 ALTER TABLE `msgboard` DISABLE KEYS */;

INSERT INTO `msgboard` (`id`, `create_time`)
VALUES
	(1,'2010-07-29 01:42:19'),
	(2,'2010-07-29 01:42:40'),
	(3,'2010-07-29 01:42:54'),
	(4,'2010-08-08 10:13:08'),
	(5,'2010-08-08 10:15:22'),
	(6,'2010-08-08 10:19:36'),
	(7,'2012-02-21 23:36:07'),
	(8,'2012-03-05 11:46:00'),
	(9,'2012-03-05 11:58:32');

/*!40000 ALTER TABLE `msgboard` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table notification_item
# ------------------------------------------------------------

LOCK TABLES `notification_item` WRITE;
/*!40000 ALTER TABLE `notification_item` DISABLE KEYS */;

INSERT INTO `notification_item` (`id`, `subject_text`, `body_text`, `language_id`, `notification_type_id`)
VALUES
	(1,'Alert - Milestone about due','Dear <username>:\n\nThis is to inform you that the milestone <goalname> of Product <productname> of Target <targetname> in the Project <projectname> is approaching its due time <duetime>.      \n\nSincerely,\nIndaba System Admin',1,1),
	(2,'Alert - Milestone over due','Dear <username>:\n\nThis is to inform you that the milestone <goalname> of Product <productname> of Target <targetname> in the Project <projectname> has passed its due time <duetime>.    \n\nSincerely,          \nIndaba System Admin',1,2),
	(3,'Alert - Task Not Assigned','Dear <username>:\n\nThis is to inform you that no user has been assigned to Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname>. Your immediate attention is required.\n\nSincerely,\nIndaba System Admin',1,3),
	(5,'Confirm - Task Completed','Dear <username>:          \n\nThis is to confirm that you have completed the Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname>.  Thank you for your hard work!\n\nSincerely,    \nIndaba System Admin',1,5),
	(6,'Thank You','Dear <username>:                    \n\nThank you for working on Project <projectname>. We look forward to working with you again in the future.\n\nSincerely,        \nIndaba System Admin',1,6),
	(7,'Welcome','Dear <username>:                    \n\nWelcome to the project team of <projectname>. We look forward to working with you.\n\nSincerely,        \nIndaba System Admin',1,7),
	(8,'Notice - Task Overdue','Dear <username>:              \n\nThis is a friendly reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> has reached its deadline <duetime>. Please try to complete the assignment ASAP.         \n\nSincerely,      \nIndaba System Admin',1,8),
	(9,'Second Notice - Task Overdue','Dear <username>:                \n\nThis is the 2nd reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> has reached its deadline <duetime>. Please try to complete the assignment ASAP. If the task is completed in <numdays> days, we may have to suspend this task.\n\nSincerely,      \nIndaba System Admin',1,9),
	(10,'Notify - Milestone Completed','Dear <username>:\n                   \nThis is to inform you that the milestone <goalname> of Product <productname> of Target <targetname> in the Project <projectname> has been completed.  \n\nSincerely,         \nIndaba System Admin',1,10),
	(11,'Notify - Please Claim Task','Dear <username>:        \n\nThe task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> is ready to be claimed, please login to Indaba to claim it.    \n\nSincerely,   \nIndaba System Admin',1,11),
	(12,'Notify - Project Done','Dear <username>: \n\nThis is to inform you that all tasks in the Project <projectname> have been completed. \n \nSincerely,         \nIndaba System Admin',1,12),
	(13,'Notify - Task Activated','Dear <username>:    \n\nThis is to inform you that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin',1,13),
	(14,'Reminder - Task About Due','Dear <username>:           \n\nThis is a friendly reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> is approaching its deadline <duetime>. Please try to complete the assignment ASAP.       \n\nSincerely,     \nIndaba System Admin',1,14),
	(15,'Second Reminder - Task About Due','Dear <username>:           \n\nThis is the second reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> is approaching its deadline <duetime>. Please try to complete the assignment ASAP.       \n\nSincerely,     \nIndaba System Admin',1,15),
	(16,'Alert - New Site Message','Dear <username>,\n\nThis is to notify you that there is a new message for you on Indaba website. Please login to the site to view the message.\n\nSincerely,\nIndaba System Administrator',1,16),
	(17,'Notify - Case Assigned','Dear <username>:  \n \nThis is to inform you that the new case <casename> has been assigned to you. Please logon to Indaba system and resolve the issue as soon as possible.\n\nSincerely, \nIndaba System Admin',1,17),
	(18,'Notify - Case Attached','Dear <username>:  \n \nThis is to inform you that you have been attached to the case <casename>. Please logon to Indaba system and review the issue as soon as possible.\n\nSincerely, \nIndaba System Admin',1,18),
	(19,'Notify - Case Status Change','Dear <username>: \n \nThis is to inform you that the status of the case <casename> has been changed to <casestatus>.\n  \nSincerely, \nIndaba System Admin',1,19),
	(20,'Notify - Review Feedback Posted','Dear <username>: \n \nThis is to inform you that review feedback has been posted for <contenttitle>. Please provide your responses as soon as possible.\n  \nSincerely, \nIndaba System Admin',1,20),
	(21,'Notify - Review Feedback Response Posted','Dear <username>: \n \nThis is to inform you that responses to your review feedback have been posted for <contenttitle>. Please continue your review as soon as possible.\n  \nSincerely, \nIndaba System Admin',1,21),
	(22,'Task Completed','<username> has competed <taskname> for <contenttitle>. Hooray!',1,22),
	(23,'Horse Completed','<contenttitle> is done. Hip Hip Hooray!',1,23),
	(24,'Your Indaba Password','Dear <username>: \n \nYour Indaba password is <password>. \n  \nSincerely, \nIndaba System Admin',1,24),
	(25,'Notify - Review Feedback Canceled','Dear <username>: \n \nThis is to inform you that review feedback for <contenttitle> has been canceled. You no longer need to provide responses.\n  \nSincerely, \nIndaba System Admin',1,25),
	(26,'Notify - Task Assigned','Dear <username>:    \n\nThis is to inform you that you have been assigned the Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname>. \n\nSincerely,  \nIndaba System Admin',1,26);

/*!40000 ALTER TABLE `notification_item` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table notification_type
# ------------------------------------------------------------

LOCK TABLES `notification_type` WRITE;
/*!40000 ALTER TABLE `notification_type` DISABLE KEYS */;

INSERT INTO `notification_type` (`id`, `name`, `description`)
VALUES
	(1,'Alert - Milestone About Due','Alert user that the milestone is reaching due time'),
	(2,'Alert - Milestone Overdue','Alert user that the milestone is overdue'),
	(3,'Alert - Task Not Assigned','Alert user that nobody is assigned to a task'),
	(5,'Confirm - Task Completed','Confirmation message that a task has been completed'),
	(6,'Msg - Thank You','Send this msg to thank a user after he\'s done with the project'),
	(7,'Msg - Welcome','Welcome the user to a project'),
	(8,'Notice 1 - Task overdue','First notice to a user that assigned task is overdue'),
	(9,'Notice 2 - Task overdue','Second notice to a user that assigned task is overdue'),
	(10,'Notify - Milestone Completed','Notify a user that a milestone has been completed'),
	(11,'Notify - Please Claim','Ask a user to claim a task from queue'),
	(12,'Notify - Project Done','Notify user that a project is done'),
	(13,'Notify - Task Activated','Notify a user that her task assignment is now activated'),
	(14,'Reminder 1 - Task about due','First reminder to a user that her task assignment is about due'),
	(15,'Reminder 2 - Task about due','Second reminder to a user that her task assignment is about due'),
	(16,'Sys - Site Message Alert','Used to notify user of new site messages'),
	(17,'Sys - Case Assigned ','Message for case assignment notification'),
	(18,'Sys - Case Attached','Notify user that he is attached to a case'),
	(19,'Sys - Case Status Change','Notify user of case status change'),
	(20,'Sys - Review Feedback Posted','Notify user of review feedback'),
	(21,'Sys - Review Feedback Response Posted','Notify user of responses to review feedback'),
	(22,'Post - Task Completed','Post the task completion msg to project wall'),
	(23,'Post - Horse Completed','Post the horse completion msg to project wall'),
	(24,'Sys - User Password','Send password to user through email'),
	(25,'Sys - Review Feedback Canceled','Notify the author that review feedback has been canceled'),
	(26,'Sys - Task Assigned','Notify a user that she has been assigned to a task');

/*!40000 ALTER TABLE `notification_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table organization
# ------------------------------------------------------------

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;

INSERT INTO `organization` (`id`, `name`, `address`, `admin_user_id`, `url`)
VALUES
	(1,'Global Integrity',NULL,1,'http://globalintegrity.org');

/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table pregoal
# ------------------------------------------------------------

LOCK TABLES `pregoal` WRITE;
/*!40000 ALTER TABLE `pregoal` DISABLE KEYS */;

INSERT INTO `pregoal` (`id`, `workflow_sequence_id`, `pre_goal_id`)
VALUES
	(4,0,14),
	(7,0,17),
	(3,0,15),
	(6,4,14),
	(8,9,22),
	(9,0,21),
	(10,5,17),
	(11,6,15),
	(12,7,14),
	(13,8,21),
	(14,8,22),
	(15,8,17);

/*!40000 ALTER TABLE `pregoal` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table product
# ------------------------------------------------------------

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;

INSERT INTO `product` (`id`, `workflow_id`, `name`, `description`, `project_id`, `access_matrix_id`, `product_config_id`, `content_type`)
VALUES
	(1,1,'Notebook','Notebook product of Indaba 2010 project',1,NULL,1,1),
	(2,2,'Scorecard','Survey of anti-corruption status',1,NULL,1,0),
	(3,3,'Energy Policy Analysis','An essay that analyzes the energy policy of the nation',2,0,2,1),
	(4,3,'Energy Policy Survey','Survey of national energy policy ',2,NULL,2,0);

/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table project
# ------------------------------------------------------------

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;

INSERT INTO `project` (`id`, `organization_id`, `code_name`, `description`, `owner_user_id`, `creation_time`, `access_matrix_id`, `view_matrix_id`, `start_time`, `study_period_id`, `status`, `logo_path`, `msgboard_id`, `admin_user_id`, `sponsor_logos`, `is_active`, `close_time`, `visibility`)
VALUES
	(1,1,'Integrity 2010','World Anti-Corruption Study',1,NULL,1,1,'2010-06-01',1,1,NULL,7,1,NULL,1,'2010-12-31',1),
	(2,1,'Green Energy 2010','World Energy Policy Study',1,NULL,1,1,'2010-10-01',1,1,NULL,9,1,NULL,1,'2010-12-31',1);

/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table project_contact
# ------------------------------------------------------------

LOCK TABLES `project_contact` WRITE;
/*!40000 ALTER TABLE `project_contact` DISABLE KEYS */;

INSERT INTO `project_contact` (`id`, `project_id`, `user_id`)
VALUES
	(1,1,5),
	(2,1,6),
	(3,2,5),
	(4,2,1),
	(5,2,6);

/*!40000 ALTER TABLE `project_contact` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table project_membership
# ------------------------------------------------------------

LOCK TABLES `project_membership` WRITE;
/*!40000 ALTER TABLE `project_membership` DISABLE KEYS */;

INSERT INTO `project_membership` (`id`, `user_id`, `role_id`, `project_id`)
VALUES
	(1,13,6,1),
	(2,15,6,1),
	(3,14,6,1),
	(4,16,6,1),
	(5,9,7,1),
	(6,11,7,1),
	(7,12,7,1),
	(8,10,7,1),
	(9,2,3,1),
	(10,20,3,1),
	(11,3,4,1),
	(12,4,4,1),
	(13,5,2,1),
	(14,6,2,1),
	(15,7,8,1),
	(16,8,8,1),
	(22,2,3,2),
	(18,21,5,1),
	(19,17,5,1),
	(35,18,5,1),
	(21,19,5,1),
	(23,3,4,2),
	(24,4,4,2),
	(25,5,2,2),
	(26,6,2,2),
	(27,17,5,2),
	(28,18,5,2),
	(29,19,5,2),
	(30,21,5,2),
	(31,22,6,2),
	(32,24,6,2),
	(33,23,7,2),
	(34,25,7,2);

/*!40000 ALTER TABLE `project_membership` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table project_roles
# ------------------------------------------------------------

LOCK TABLES `project_roles` WRITE;
/*!40000 ALTER TABLE `project_roles` DISABLE KEYS */;

INSERT INTO `project_roles` (`id`, `project_id`, `role_id`)
VALUES
	(1,1,2),
	(2,1,4),
	(3,1,3),
	(4,1,7),
	(5,1,6),
	(6,1,8),
	(10,2,2),
	(8,2,6),
	(9,1,5),
	(11,2,3),
	(12,2,4),
	(13,2,5),
	(14,2,7),
	(16,2,9);

/*!40000 ALTER TABLE `project_roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table project_target
# ------------------------------------------------------------

LOCK TABLES `project_target` WRITE;
/*!40000 ALTER TABLE `project_target` DISABLE KEYS */;

INSERT INTO `project_target` (`id`, `project_id`, `target_id`)
VALUES
	(1,1,2),
	(2,1,1),
	(3,1,3),
	(4,1,4),
	(8,2,6),
	(9,2,7);

/*!40000 ALTER TABLE `project_target` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table reference
# ------------------------------------------------------------

LOCK TABLES `reference` WRITE;
/*!40000 ALTER TABLE `reference` DISABLE KEYS */;

INSERT INTO `reference` (`id`, `name`, `description`, `choice_type`)
VALUES
	(1,'In Law 1','Identify and describe the name(s) and section(s) of the applicable law(s), statute(s), regulation(s), or constitutional provision(s).  Provide a web link to the source(s) if available. ',0),
	(2,'In Law 2 ','Identify and describe the name(s) of the institution(s), agency (or agencies), government entity (or entities), or mechanism(s) by name.  Also identify the law(s), statute(s), regulation(s), or constitutional provision(s) that created the institution(s), entity (or entities), or mechanism(s).  Provide a web link to the source(s) if available.',0),
	(3,'In Practice','Identify two or more of the following sources to support your score (two sources of the same type are sufficient, provided they are both identified separately in the same text box):',2);

/*!40000 ALTER TABLE `reference` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table reference_choice
# ------------------------------------------------------------

LOCK TABLES `reference_choice` WRITE;
/*!40000 ALTER TABLE `reference_choice` DISABLE KEYS */;

INSERT INTO `reference_choice` (`id`, `reference_id`, `label`, `weight`, `mask`)
VALUES
	(1,3,'1) media reports (identify the publication, author, date published, title, and website if available)',0,1),
	(2,3,'2) academic, policy or professional studies (identify the publication, author, date published, title, and website if available)',1,2),
	(3,3,'3) government studies (identify the publication, author, date published, title, and website if available)',2,4),
	(4,3,'4) international organization studies (identify the publication, author, date published, title, and website if available)',3,8),
	(5,3,'5) interviews with government officials (identify the person by name, title, organization, date of interview, and place of interview)',4,16),
	(6,3,'6) interviews with academics (identify the person by name, title, organization, date of interview, and place of interview)',5,32),
	(7,3,'7) interviews with civil society or NGO representatives (identify the person by name, title, organization, date of interview, and place of interview)',6,64),
	(8,3,'8) interviews with journalists or media representatives (identify the person by name, title, organization, date of interview, and place of interview)',7,128);

/*!40000 ALTER TABLE `reference_choice` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table reference_object
# ------------------------------------------------------------



# Dump of table right_category
# ------------------------------------------------------------

LOCK TABLES `right_category` WRITE;
/*!40000 ALTER TABLE `right_category` DISABLE KEYS */;

INSERT INTO `right_category` (`id`, `name`, `description`)
VALUES
	(1,'indaba','Indaba rules');

/*!40000 ALTER TABLE `right_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rights
# ------------------------------------------------------------

LOCK TABLES `rights` WRITE;
/*!40000 ALTER TABLE `rights` DISABLE KEYS */;

INSERT INTO `rights` (`id`, `right_category_id`, `name`, `label`, `description`)
VALUES
	(1,1,'read announcements','read announcements','Allows users to read admin announcements'),
	(2,1,'read project wall','read project wall','Allows to read messages on the project wall'),
	(3,1,'write project wall','write project wall','Allows users to post to the project wall'),
	(4,1,'read content details of others','read content details of others','Allows user to read details of the content that he is not part of'),
	(5,1,'read content status of others','read content status of others','Allows user to see status of content that he is not part of '),
	(6,1,'see all content','see all content','Allows user to access all content and its status, associated cases and users'),
	(7,1,'manage queues','manage queues','Allows user to assign/remove users to tasks in the queue, change task priority'),
	(8,1,'open cases','open cases','Allows user to open new cases.'),
	(9,1,'close all cases','close all cases','Allows user to close all cases, including cases not opened by herself'),
	(10,1,'attach any content to cases','attach any content to cases','Allows user to attach to the case content that the user is not part of'),
	(11,1,'attach any user to cases','attach any user to cases','Allows user to attach to the case any user in the project'),
	(12,1,'assign any user to cases','assign any user to cases','Allows user to assign the case to any user in the project'),
	(13,1,'manage all cases','manage all cases','Allows the user to read and edit all cases, including those that are not hers'),
	(14,1,'block content progress','block content progress','Allows the user to block progress and publishing of the content attached to the case'),
	(15,1,'manage all users','manage all users','Allows to search and view details of users'),
	(16,1,'write to teams','write to teams','Allows the user to send messages to teams'),
	(17,1,'see content current tasks','see content current tasks','Allows the user to see currently active tasks of the content'),
	(18,1,'see content cases','see content cases','Allows the user to see the \'Cases\' section of the content page'),
	(19,1,'read content internal discussion','read content internal discussion','Allows user to access internal discussion message board'),
	(20,1,'write content internal discussion','write content internal discussion','Allows the user to add comments to the internal discussion message board'),
	(21,1,'manage content internal discussion','manage content internal discussion','Allows user to enhance/publish comments on the internal discussion MB'),
	(22,1,'read journal staff-author discussion','read journal staff-author discussion','Allows user to access staff-author discussion message board of journal content'),
	(23,1,'manage journal staff-author discussion','manage journal staff-author discussion','Allows user to enhance/publish comments on the staff-author discussion MB'),
	(24,1,'read journal peer reviews','read journal peer reviews','Allows the user to read peer reviewers\' opinions'),
	(25,1,'read journal peer review discussions','read journal peer review discussions','Allows user to access peer review discussion message boards of journal content'),
	(26,1,'manage journal peer review discussions','manage journal peer review discussions','Allows user to enhance and publish comments on peer review discussion'),
	(27,1,'read survey staff-author discussion','read survey staff-author discussion','Allows user to access staff-author discussion message board of survey content'),
	(28,1,'manage survey staff-author discussion','manage survey staff-author discussion','Allows user to enhance and publish comments on the staff-author discussion MB'),
	(29,1,'read survey peer reviews','read survey peer reviews','Allows the user to read peer reviewers\' opinions of survey content'),
	(30,1,'read survey peer review discussions','read survey peer review discussions','Allows user to access peer review discussion message boards of survey content'),
	(31,1,'manage survey peer review discussions','manage survey peer review discussions','Allows user to enhance and publish comments on peer review discussion MB\'s'),
	(32,1,'tag indicators','tag indicators','Allows user to add tags to indicators and to search indicators with tags'),
	(33,1,'see target indicator values','see target indicator values','Allows user to see indicator values of other targets'),
	(34,1,'use indaba admin','use indaba admin','Allows user to use Indaba Admin tool (Loader)'),
	(35,1,'manage site','manage site','Allows user to manage the site(for admin or super user)'),
	(36,1,'access case staff notes','access case staff notes','Allows user to read/write staff notes to cases'),
	(37,1,'edit any cases','edit any cases','Allows user to edit any cases including those opened by others'),
	(38,1,'super edit content','super edit content','Allows user to super-edit any content');

/*!40000 ALTER TABLE `rights` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `name`, `description`)
VALUES
	(2,'manager','Project manager'),
	(3,'editor','Content editor'),
	(4,'reviewer','Staff reviewer'),
	(5,'peer reviewer','Content peer reviewer'),
	(6,'reporter','Reporters write journal reports'),
	(7,'researcher','Researchers answer scorecards'),
	(8,'support','Project support personnel'),
	(9,'publisher','Content publisher');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rule
# ------------------------------------------------------------

LOCK TABLES `rule` WRITE;
/*!40000 ALTER TABLE `rule` DISABLE KEYS */;

INSERT INTO `rule` (`id`, `name`, `rule_type`, `file_name`, `description`)
VALUES
	(1,'Check task before due 1',NULL,NULL,'IF assignment is {5} days before due, THEN send \'Reminder 1 - Task about due\' to the assigned user;'),
	(2,'Check task before due 2',NULL,NULL,'IF assignment is {2} days before due, THEN send \'Reminder 2 - Task about due\' to the assigned user;'),
	(3,'Check task overdue 1',NULL,NULL,'IF assignment is {2} days overdue, THEN send \'Notice 1 - Task overdue\' to the assigned user;'),
	(4,'Check task overdue 2',NULL,NULL,'IF assignment is {5} days overdue, THEN send \'Notice 2 - Task overdue\' to the assigned user;'),
	(5,'Welcome user',NULL,NULL,'IF task activated THEN send \'Msg - Welcome\' to the assigned user;'),
	(6,'Thank user',NULL,NULL,'IF task is done THEN send \'Msg - Thank you\' to the assigned user;'),
	(7,'Confirm task completion',NULL,NULL,'IF task is done THEN send \'Confirm - Task Completed\' to the assigned user;'),
	(8,'Post task completion',NULL,NULL,'IF task is done THEN post \'Post - Task Completed\' to project wall;'),
	(9,'Notify payment',NULL,NULL,'IF task is done THEN send \'Notify - Payment sent\' to the author;'),
	(10,'Report milestone done',NULL,NULL,'IF all assignments are done THEN send \'Notify - Milestone Completed\' to project admin;'),
	(11,'Report milestone about due',NULL,NULL,'IF not all assignments are done AND it is {5} days before due THEN send \'Alert - Milestone about due\' to project admin;'),
	(12,'Report milestone overdue',NULL,NULL,'IF not all assignments are done AND it is {2} days after due THEN send \'Alert - Milestone Overdue\' to project admin;'),
	(13,'Report horse completion',NULL,NULL,'IF all assignments are done THEN send \'Notify - Horse Completion\' to project admin, and post \'Post - Horse completed\' to the Project Wall;'),
	(14,'Post horse completion',NULL,NULL,'IF all assignments are done THEN post \'Post - Horse Completion\' to project wall;'),
	(15,'Goal Exit - normal',NULL,NULL,'IF all assignments are done THEN exit the goal;'),
	(16,'Goal Exit - overdue',NULL,NULL,'IF the goal is {30} days overdue THEN exit the goal;'),
	(17,'Goal Exit - partial completion',NULL,NULL,'IF {80%} of assignments are completed AND the goal is {10} days overdue THEN exit the goal;'),
	(18,'Report task overdue 1',NULL,NULL,'IF assignment is {5} days overdue THEN send \'Alert 1 - Task Overdue\' to the project admin, and open a \'Task Overdue\' case for the Project Admin;'),
	(19,'Report task overdue 2',NULL,NULL,'IF assignment is {10} days overdue THEN send \'Alert 2 - Task Overdue\' to the project admin;');

/*!40000 ALTER TABLE `rule` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sequence_object
# ------------------------------------------------------------

LOCK TABLES `sequence_object` WRITE;
/*!40000 ALTER TABLE `sequence_object` DISABLE KEYS */;

INSERT INTO `sequence_object` (`id`, `workflow_object_id`, `workflow_sequence_id`, `status`)
VALUES
	(1,1,1,0),
	(2,2,2,0),
	(3,2,4,0),
	(4,2,5,0),
	(5,2,6,0),
	(6,2,7,0),
	(7,2,8,0),
	(8,3,1,0),
	(9,4,1,0),
	(10,5,1,0),
	(11,6,2,0),
	(12,6,4,0),
	(13,6,5,0),
	(14,6,6,0),
	(15,6,7,0),
	(16,6,8,0),
	(17,7,2,0),
	(18,7,4,0),
	(19,7,5,0),
	(20,7,6,0),
	(21,7,7,0),
	(22,7,8,0),
	(23,8,2,0),
	(24,8,4,0),
	(25,8,5,0),
	(26,8,6,0),
	(27,8,7,0),
	(28,8,8,0);

/*!40000 ALTER TABLE `sequence_object` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table session
# ------------------------------------------------------------



# Dump of table source_file
# ------------------------------------------------------------

LOCK TABLES `source_file` WRITE;
/*!40000 ALTER TABLE `source_file` DISABLE KEYS */;

INSERT INTO `source_file` (`id`, `filename`, `path`, `extension`, `status`, `last_update_time`, `note`)
VALUES
	(1,'common','','common',1,NULL,'Virtual file for common resources.'),
	(3,'allContent.jsp','web','jsp',1,NULL,''),
	(4,'answerDetails.jsp','web','jsp',1,NULL,''),
	(5,'assignmentMessage.jsp','web','jsp',1,NULL,''),
	(6,'attachment.jsp','web','jsp',1,NULL,''),
	(7,'casefilter.jsp','web','jsp',1,NULL,''),
	(8,'casePage.jsp','web','jsp',1,NULL,''),
	(9,'cases.jsp','web','jsp',1,NULL,''),
	(10,'contentApproval.jsp','web','jsp',1,NULL,''),
	(11,'contentGeneral.jsp','web','jsp',1,NULL,''),
	(12,'contentPayment.jsp','web','jsp',1,NULL,''),
	(13,'discussion.jsp','web','jsp',1,NULL,''),
	(14,'discussionBoard.jsp','web','jsp',1,NULL,''),
	(15,'error.jsp','web','jsp',1,NULL,''),
	(16,'fileupload.jsp','web','jsp',1,NULL,''),
	(17,'filters.jsp','web','jsp',1,NULL,''),
	(18,'header.jsp','web','jsp',1,NULL,''),
	(19,'help.jsp','web','jsp',1,NULL,''),
	(20,'horseApproval.jsp','web','jsp',1,NULL,''),
	(21,'index.jsp','web','jsp',2,NULL,''),
	(22,'indicatorAddTag.jsp','web','jsp',1,NULL,''),
	(23,'indicatorDisplay.jsp','web','jsp',1,NULL,''),
	(24,'indicatorPeerReviewPage.jsp','web','jsp',1,NULL,''),
	(25,'indicatorPRReviewPage.jsp','web','jsp',1,NULL,''),
	(26,'indicatorReviewPage.jsp','web','jsp',1,NULL,''),
	(27,'journalContentVersion.jsp','web','jsp',1,NULL,''),
	(28,'journalOverallReview.jsp','web','jsp',1,NULL,''),
	(29,'journalPeerReview.jsp','web','jsp',1,NULL,''),
	(30,'journalPRReviews.jsp','web','jsp',1,NULL,''),
	(31,'journalReview.jsp','web','jsp',1,NULL,''),
	(32,'journalReviewResponse.jsp','web','jsp',1,NULL,''),
	(33,'login.jsp','web','jsp',1,NULL,''),
	(34,'messageDetail.jsp','web','jsp',1,NULL,''),
	(35,'messages.jsp','web','jsp',1,NULL,''),
	(36,'messageSidebar.jsp','web','jsp',1,NULL,''),
	(37,'myOpenCases.jsp','web','jsp',1,NULL,''),
	(38,'newMessage.jsp','web','jsp',1,NULL,''),
	(39,'notebook.jsp','web','jsp',1,NULL,''),
	(40,'notebookDisplay.jsp','web','jsp',1,NULL,''),
	(41,'notebookEdit.jsp','web','jsp',1,NULL,''),
	(42,'notebookEditor.jsp','web','jsp',1,NULL,''),
	(43,'notebookView.jsp','web','jsp',1,NULL,''),
	(44,'peerReview.jsp','web','jsp',1,NULL,''),
	(45,'people.jsp','web','jsp',1,NULL,''),
	(46,'peopleFilter.jsp','web','jsp',1,NULL,''),
	(47,'peopleProfile.jsp','web','jsp',1,NULL,''),
	(48,'prReviews.jsp','web','jsp',1,NULL,''),
	(49,'queues.jsp','web','jsp',1,NULL,''),
	(50,'replyMessage.jsp','web','jsp',1,NULL,''),
	(51,'ruleManager.jsp','web','jsp',1,NULL,''),
	(52,'scorecardDisplay.jsp','web','jsp',1,NULL,''),
	(53,'scorecardIndicatorSearch.jsp','web','jsp',1,NULL,''),
	(54,'scorecardNavigation.jsp','web','jsp',1,NULL,''),
	(55,'scorecardPeerReviewDisagreementList.jsp','web','jsp',1,NULL,''),
	(56,'sourceWidgetMultiChoice.jsp','web','jsp',1,NULL,''),
	(57,'sourceWidgetText.jsp','web','jsp',1,NULL,''),
	(58,'staffDiscussion.jsp','web','jsp',2,NULL,''),
	(59,'surveyAnswer.jsp','web','jsp',1,NULL,''),
	(60,'surveyAnswerDisplay.jsp','web','jsp',1,NULL,''),
	(61,'surveyAnswerDisplayNavigation.jsp','web','jsp',1,NULL,''),
	(62,'surveyAnswerInternalDiscussion.jsp','web','jsp',2,NULL,''),
	(63,'surveyAnswerNavigation.jsp','web','jsp',1,NULL,''),
	(64,'surveyAnswerOriginal.jsp','web','jsp',1,NULL,''),
	(65,'surveyAnswerPeerReview.jsp','web','jsp',1,NULL,''),
	(66,'surveyAnswerPeerReviewDisplay.jsp','web','jsp',1,NULL,''),
	(67,'surveyAnswerPeerReviewsForReviewReponse.jsp','web','jsp',1,NULL,''),
	(68,'surveyAnswerPRReviews.jsp','web','jsp',1,NULL,''),
	(69,'surveyAnswerQuestionSidebar.jsp','web','jsp',1,NULL,''),
	(70,'surveyAnswerReview.jsp','web','jsp',2,NULL,''),
	(71,'surveyOverallReview.jsp','web','jsp',1,NULL,''),
	(72,'surveyStaffDiscussion.jsp','web','jsp',2,NULL,''),
	(73,'targetSidebar.jsp','web','jsp',1,NULL,''),
	(74,'userfinder.jsp','web','jsp',1,NULL,''),
	(75,'welcomeStruts.jsp','web','jsp',2,NULL,''),
	(76,'workflowConsole.jsp','web','jsp',1,NULL,''),
	(77,'yourContent.jsp','web','jsp',1,NULL,''),
	(78,'ajaxfileupload.js','web/js','js',2,NULL,''),
	(79,'assignment.js','web/js','js',2,NULL,''),
	(80,'base_type.js','web/js','js',2,NULL,''),
	(81,'button.js','web/js','js',2,NULL,''),
	(82,'casedetail.js','web/js','js',2,NULL,''),
	(83,'caseDiscussion.js','web/js','js',2,NULL,''),
	(84,'caseMsgBoard.js','web/js','js',2,NULL,''),
	(85,'chart.js','web/js','js',2,NULL,''),
	(86,'ckeditor_config.js','web/js','js',2,NULL,''),
	(87,'ckeditor_readonly_config.js','web/js','js',2,NULL,''),
	(88,'common.js','web/js','js',2,NULL,''),
	(89,'contentGeneral.js','web/js','js',2,NULL,''),
	(90,'discussion.js','web/js','js',2,NULL,''),
	(91,'discussions.js','web/js','js',2,NULL,''),
	(92,'filter.js','web/js','js',2,NULL,''),
	(93,'flexigrid.js','web/js','js',2,NULL,''),
	(94,'flexigrid.pack.js','web/js','js',2,NULL,''),
	(95,'grid.locale-en.js','web/js','js',2,NULL,''),
	(96,'indicatorSearch.js','web/js','js',2,NULL,''),
	(97,'journalReview.js','web/js','js',2,NULL,''),
	(98,'json2.js','web/js','js',2,NULL,''),
	(99,'msgboardpicklist.js','web/js','js',2,NULL,''),
	(100,'scorecardNavigation.js','web/js','js',2,NULL,''),
	(101,'surveyReview.js','web/js','js',2,NULL,''),
	(102,'taskAssignment.js','web/js','js',2,NULL,''),
	(103,'toggleDisplay.js','web/js','js',2,NULL,''),
	(104,'toggleDisplayForCase.js','web/js','js',2,NULL,''),
	(105,'xmlhttp.js','web/js','js',2,NULL,''),
	(106,'ApplicationResources.properties','src','properties',2,NULL,''),
	(107,'AddCaseNotesAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(108,'AllContentAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(109,'AnswerDetailsAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(110,'AssignmentAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(111,'AttachmentAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(112,'BaseAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(113,'CaseMessageBoardAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(114,'CasesAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(115,'CasesDetailAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(116,'CasesFilterAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(117,'ContentApprovalAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(118,'ContentGeneralAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(119,'ContentPaymentAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(120,'CreateCaseAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(121,'CreateMessageAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(122,'DiscussionBoardAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(123,'DownloadFileAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(124,'EnhanceTextAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(125,'FileUploadAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(126,'FireUserFinderAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(127,'ForgetPasswordAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(128,'HelpAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(129,'HistoryChartAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(130,'HorseApprovalAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(131,'I18nTestTagAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(132,'IHaveQuestionsAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(133,'ImageServlet.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(134,'InitializationServletListener.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(135,'JournalContentVersionAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(136,'JournalOverallReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(137,'JournalPeerReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(138,'JournalPRReviewsAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(139,'JournalReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(140,'JournalReviewResponseAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(141,'LoadHistoryChartAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(142,'LoadTagAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(143,'LoginAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(144,'LogoutAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(145,'MessageDetailAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(146,'MessagesAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(147,'MultiUploadForm.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(148,'NewCaseAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(149,'NewMessageAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(150,'NoteBookAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(151,'PeopleAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(152,'PeopleChangeImageAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(153,'PeopleChangeInfoAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(154,'PeopleChangepwdAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(155,'PeopleFilterAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(156,'PeopleIconServlet.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(157,'PeopleProfileAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(158,'ProjectSwitchAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(159,'QueueAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(160,'QueueSubmitAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(161,'ReplyMessageAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(162,'ReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(163,'RuleManagerAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(164,'RunWorkflowAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(165,'ScorecardIndicatorSearchAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(166,'ScorecardIndicatorSearchContentAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(167,'ScorecardNavigationAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(168,'ScorecardPeerReviewDisagreementListAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(169,'SingleUploadForm.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(170,'SourceFileAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(171,'SponsorLogoServlet.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(172,'SubmitIndicatorAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(173,'SubmitJournalAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(174,'SubmitSurveyAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(175,'SurveyAnswerAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(176,'SurveyAnswerDisplayAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(177,'SurveyAnswerOriginalAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(178,'SurveyAnswerPeerReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(179,'SurveyAnswerPeerReviewDisplayAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(180,'SurveyAnswerPRReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(181,'SurveyAnswerReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(182,'SurveyAnswerSubmitAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(183,'SurveyDisplayAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(184,'SurveyEditAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(185,'SurveyOverallReviewAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(186,'SurveyProblemListAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(187,'SurveySubmitAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(188,'TagAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(189,'TargetHoverAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(190,'TaskAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(191,'UpdateCaseAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(192,'UpdateSurveyAnswerFlagAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(193,'UpdateTaskAssignmentStatusAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(194,'UserFinderAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(195,'WorkflowConsoleAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(196,'YourContentAction.java','src/com/ocs/indaba/action','java',2,NULL,''),
	(197,'AbbreviateStringHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(198,'AccessTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(199,'AllContentTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(200,'BaseHorseContentTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(201,'FilterTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(202,'GoalTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(203,'ManageAssignmentsTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(205,'MessageTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(206,'SurveyAnswerTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(207,'SurveyIndicatorTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(208,'SurveyReferenceTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(209,'TaskStatusTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(210,'TeamContentTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(211,'UserDisplayTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(212,'ViewTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(213,'YourAssignmentsTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(214,'YourContentTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(204,'MessageArgumentTagHandler.java','src/com/ocs/indaba/tag','java',2,NULL,''),
	(215,'sample/client_page.html','web/sample','html',1,NULL,''),
	(216,'sample/config_tool.html','web/sample','html',1,NULL,''),
	(217,'sample/widget-1.html','web/sample','html',1,NULL,''),
	(218,'sample/widget-2.html','web/sample','html',1,NULL,''),
	(219,'sample/widget-3.html','web/sample','html',1,NULL,''),
	(220,'popups/receiverList.jsp','web/popups','jsp',2,NULL,''),
	(221,'popups/roleList.jsp','web/popups','jsp',2,NULL,''),
	(222,'popups/teamList.jsp','web/popups','jsp',2,NULL,''),
	(223,'popups/userList.jsp','web/popups','jsp',2,NULL,'');

/*!40000 ALTER TABLE `source_file` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table source_file_text_resource
# ------------------------------------------------------------

LOCK TABLES `source_file_text_resource` WRITE;
/*!40000 ALTER TABLE `source_file_text_resource` DISABLE KEYS */;

INSERT INTO `source_file_text_resource` (`id`, `source_file_id`, `text_resource_id`)
VALUES
	(10000,1,10000),
	(10001,1,10001),
	(10002,1,10002),
	(10003,1,10003),
	(10004,1,10004),
	(10005,1,10005),
	(10006,1,10006),
	(10007,1,10007),
	(10008,1,10008),
	(10009,1,10009),
	(10010,1,10010),
	(10011,1,10011),
	(10012,1,10012),
	(10013,1,10013),
	(10014,1,10014),
	(10015,1,10015),
	(10016,1,10016),
	(10017,1,10017),
	(10018,1,10018),
	(10019,1,10019),
	(10020,1,10020),
	(10021,1,10021),
	(10022,1,10022),
	(10023,1,10023),
	(10024,1,10024),
	(10025,1,10025),
	(10026,1,10026),
	(10027,1,10027),
	(10028,1,10028),
	(10029,1,10029),
	(10030,1,10030),
	(10031,1,10031),
	(10032,1,10032),
	(10033,1,10033),
	(10034,1,10034),
	(10035,1,10035),
	(10036,1,10036),
	(10037,1,10037),
	(10038,1,10038),
	(10039,1,10039),
	(10040,1,10040),
	(10041,1,10041),
	(10042,1,10042),
	(10043,1,10043),
	(10044,1,10044),
	(10045,1,10045),
	(10046,1,10046),
	(10047,1,10047),
	(10048,1,10048),
	(10049,1,10049),
	(10050,1,10050),
	(10051,1,10051),
	(10052,1,10052),
	(10053,1,10053),
	(10054,1,10054),
	(10055,1,10055),
	(10056,1,10056),
	(10057,1,10057),
	(10058,1,10058),
	(10059,1,10059),
	(10060,1,10060),
	(10061,1,10061),
	(10062,1,10062),
	(10063,1,10063),
	(10064,1,10064),
	(10065,1,10065),
	(10066,1,10066),
	(10067,1,10067),
	(10068,1,10068),
	(10069,1,10069),
	(10070,1,10070),
	(10071,1,10071),
	(10072,1,10072),
	(10073,1,10073),
	(10074,1,10074),
	(10075,1,10075),
	(10076,1,10076),
	(10077,1,10077),
	(10078,1,10078),
	(10079,1,10079),
	(10080,1,10080),
	(10081,1,10081),
	(10082,1,10082),
	(10083,1,10083),
	(10084,1,10084),
	(10085,1,10085),
	(10086,1,10086),
	(10087,1,10087),
	(10088,1,10088),
	(10089,1,10089),
	(10090,1,10090),
	(10091,1,10091),
	(10092,1,10092),
	(10093,1,10093),
	(10094,1,10094),
	(10095,1,10095),
	(10096,1,10096),
	(10097,1,10097),
	(10098,1,10098),
	(10099,1,10099),
	(10100,1,10100),
	(10101,1,10101),
	(10102,1,10102),
	(10103,1,10103),
	(10104,1,10104),
	(10105,1,10105),
	(10106,1,10106),
	(10107,1,10107),
	(10108,1,10108),
	(10109,1,10109),
	(10110,1,10110),
	(10111,1,10111),
	(10112,1,10112),
	(10113,1,10113),
	(10114,1,10114),
	(10115,1,10115),
	(10116,1,10116),
	(10117,1,10117),
	(10118,1,10118),
	(10119,1,10119),
	(10120,1,10120),
	(10121,1,10121),
	(10122,1,10122),
	(10123,1,10123),
	(10124,1,10124),
	(10125,1,10125),
	(10126,1,10126),
	(10127,1,10127),
	(10128,1,10128),
	(10129,1,10129),
	(10130,1,10130),
	(10131,1,10131),
	(10132,1,10132),
	(10133,1,10133),
	(10134,1,10134),
	(10135,1,10135),
	(10136,1,10136),
	(10137,1,10137),
	(10138,1,10138),
	(10139,1,10139),
	(10140,1,10140),
	(10141,1,10141),
	(10142,1,10142),
	(10143,1,10143),
	(10144,1,10144),
	(10146,1,10146),
	(10147,1,10147),
	(10148,1,10148),
	(10149,1,10149),
	(10150,1,10150),
	(10151,1,10151),
	(10152,1,10152),
	(10153,1,10153),
	(10154,1,10154),
	(10155,1,10155),
	(10156,1,10156),
	(10157,1,10157),
	(10158,1,10158),
	(10159,1,10159),
	(10160,1,10160),
	(10161,1,10161),
	(10162,1,10162),
	(10163,1,10163),
	(10164,1,10164),
	(10165,1,10165),
	(10166,1,10166),
	(10167,1,10167),
	(10168,1,10168),
	(10169,1,10169),
	(10170,1,10170),
	(10171,1,10171),
	(10172,1,10172),
	(10173,1,10173),
	(10174,1,10174),
	(10175,1,10175),
	(10176,1,10176),
	(10177,1,10177),
	(10178,1,10178),
	(10179,1,10179),
	(10180,1,10180),
	(10181,1,10181),
	(10182,1,10182),
	(10183,1,10183),
	(10184,1,10184),
	(10185,1,10185),
	(10186,1,10186),
	(10187,1,10187),
	(10188,1,10188),
	(10189,1,10189),
	(10190,1,10190),
	(10191,1,10191),
	(10192,1,10192),
	(10193,1,10193),
	(10194,1,10194),
	(30000,3,30000),
	(30001,3,30001),
	(30002,3,30002),
	(40000,4,40000),
	(40001,4,40001),
	(40002,4,40002),
	(50000,5,50000),
	(60000,6,60000),
	(60001,6,60001),
	(60002,6,60002),
	(60003,6,60003),
	(60004,6,60004),
	(60005,6,60005),
	(60006,6,60006),
	(60007,6,60007),
	(60008,6,60008),
	(60009,6,60009),
	(60010,6,60010),
	(60011,6,60011),
	(60012,6,60012),
	(60013,6,60013),
	(70000,7,70000),
	(70001,7,70001),
	(70002,7,70002),
	(70003,7,70003),
	(70004,7,70004),
	(70005,7,70005),
	(70006,7,70006),
	(70007,7,70007),
	(70008,7,70008),
	(70009,7,70009),
	(70010,7,70010),
	(70011,7,70011),
	(70012,7,70012),
	(80000,8,80000),
	(80001,8,80001),
	(80002,8,80002),
	(80003,8,80003),
	(80004,8,80004),
	(80005,8,80005),
	(80006,8,80006),
	(80007,8,80007),
	(80008,8,80008),
	(80009,8,80009),
	(80010,8,80010),
	(80011,8,80011),
	(80012,8,80012),
	(80013,8,80013),
	(80014,8,80014),
	(80015,8,80015),
	(80016,8,80016),
	(80017,8,80017),
	(80018,8,80018),
	(80019,8,80019),
	(80020,8,80020),
	(80021,8,80021),
	(80022,8,80022),
	(80023,8,80023),
	(80024,8,80024),
	(80025,8,80025),
	(80026,8,80026),
	(90000,9,90000),
	(90001,9,90001),
	(90002,9,90002),
	(90003,9,90003),
	(90004,9,90004),
	(90005,9,90005),
	(90006,9,90006),
	(90007,9,90007),
	(90008,9,90008),
	(90009,9,90009),
	(90010,9,90010),
	(100000,10,100000),
	(100001,10,100001),
	(100002,10,100002),
	(100003,10,100003),
	(100004,10,100004),
	(110000,11,110000),
	(110001,11,110001),
	(110002,11,110002),
	(110003,11,110003),
	(110004,11,110004),
	(110005,11,110005),
	(110006,11,110006),
	(110007,11,110007),
	(110008,11,110008),
	(110009,11,110009),
	(110010,11,110010),
	(110011,11,110011),
	(110012,11,110012),
	(120000,12,120000),
	(120001,12,120001),
	(120002,12,120002),
	(120003,12,120003),
	(120004,12,120004),
	(120005,12,120005),
	(120006,12,120006),
	(120007,12,120007),
	(120008,12,120008),
	(140000,14,140000),
	(140001,14,140001),
	(140002,14,140002),
	(140003,14,140003),
	(140004,14,140004),
	(140005,14,140005),
	(140006,14,140006),
	(140007,14,140007),
	(140008,14,140008),
	(140009,14,140009),
	(140010,14,140010),
	(140011,14,140011),
	(140012,14,140012),
	(140013,14,140013),
	(140014,14,140014),
	(140015,14,140015),
	(140016,14,140016),
	(140017,14,140017),
	(140018,14,140018),
	(140019,14,140019),
	(140020,14,140020),
	(140021,14,140021),
	(140022,14,140022),
	(140023,14,140023),
	(140024,14,140024),
	(140025,14,140025),
	(140026,14,140026),
	(140027,14,140027),
	(140028,14,140028),
	(140029,14,140029),
	(140030,14,140030),
	(140031,14,140031),
	(140032,14,140032),
	(140033,14,140033),
	(140034,14,140034),
	(140035,14,140035),
	(140036,14,140036),
	(150000,15,150000),
	(150001,15,150001),
	(150002,15,150002),
	(150003,15,150003),
	(160000,16,160000),
	(160001,16,160001),
	(160002,16,160002),
	(170000,17,170000),
	(170001,17,170001),
	(180000,18,180000),
	(180001,18,180001),
	(180002,18,180002),
	(180003,18,180003),
	(180004,18,180004),
	(180005,18,180005),
	(180006,18,180006),
	(180007,18,180007),
	(180008,18,180008),
	(180009,18,180009),
	(180010,18,180010),
	(190000,19,190000),
	(190001,19,190001),
	(190002,19,190002),
	(190003,19,190003),
	(190004,19,190004),
	(190005,19,190005),
	(190006,19,190006),
	(190007,19,190007),
	(190008,19,190008),
	(200000,20,200000),
	(200001,20,200001),
	(200002,20,200002),
	(220000,22,220000),
	(220001,22,220001),
	(230000,23,230000),
	(230001,23,230001),
	(230002,23,230002),
	(240000,24,240000),
	(240001,24,240001),
	(240002,24,240002),
	(250000,25,250000),
	(250001,25,250001),
	(250002,25,250002),
	(250003,25,250003),
	(260000,26,260000),
	(260001,26,260001),
	(260002,26,260002),
	(260003,26,260003),
	(270000,27,270000),
	(270001,27,270001),
	(270002,27,270002),
	(280000,28,280000),
	(280001,28,280001),
	(290000,29,290000),
	(290001,29,290001),
	(300000,30,300000),
	(300001,30,300001),
	(300002,30,300002),
	(310000,31,310000),
	(310001,31,310001),
	(310002,31,310002),
	(320000,32,320000),
	(320001,32,320001),
	(330000,33,330000),
	(330001,33,330001),
	(330002,33,330002),
	(330003,33,330003),
	(330004,33,330004),
	(330005,33,330005),
	(330006,33,330006),
	(330007,33,330007),
	(330008,33,330008),
	(330009,33,330009),
	(340000,34,340000),
	(340001,34,340001),
	(340002,34,340002),
	(340003,34,340003),
	(340004,34,340004),
	(340005,34,340005),
	(350000,35,350000),
	(350001,35,350001),
	(350002,35,350002),
	(350003,35,350003),
	(350004,35,350004),
	(350005,35,350005),
	(350006,35,350006),
	(350007,35,350007),
	(350008,35,350008),
	(350009,35,350009),
	(350010,35,350010),
	(350011,35,350011),
	(360000,36,360000),
	(360001,36,360001),
	(360002,36,360002),
	(360003,36,360003),
	(360004,36,360004),
	(370000,37,370000),
	(370001,37,370001),
	(370002,37,370002),
	(370003,37,370003),
	(370004,37,370004),
	(370005,37,370005),
	(370006,37,370006),
	(370007,37,370007),
	(380000,38,380000),
	(380001,38,380001),
	(380002,38,380002),
	(380003,38,380003),
	(380004,38,380004),
	(380005,38,380005),
	(380006,38,380006),
	(380007,38,380007),
	(380008,38,380008),
	(380009,38,380009),
	(380010,38,380010),
	(390000,39,390000),
	(390001,39,390001),
	(390002,39,390002),
	(390003,39,390003),
	(390004,39,390004),
	(390005,39,390005),
	(400000,40,400000),
	(410000,41,410000),
	(420000,42,420000),
	(420001,42,420001),
	(420002,42,420002),
	(420003,42,420003),
	(420004,42,420004),
	(420005,42,420005),
	(420006,42,420006),
	(430000,43,430000),
	(430001,43,430001),
	(430002,43,430002),
	(440000,44,440000),
	(440001,44,440001),
	(440002,44,440002),
	(440003,44,440003),
	(450000,45,450000),
	(450001,45,450001),
	(450002,45,450002),
	(450003,45,450003),
	(450004,45,450004),
	(450005,45,450005),
	(450006,45,450006),
	(450007,45,450007),
	(450008,45,450008),
	(450009,45,450009),
	(450010,45,450010),
	(450011,45,450011),
	(450012,45,450012),
	(460000,46,460000),
	(460001,46,460001),
	(460002,46,460002),
	(460003,46,460003),
	(460004,46,460004),
	(460005,46,460005),
	(460006,46,460006),
	(460007,46,460007),
	(460008,46,460008),
	(460009,46,460009),
	(460010,46,460010),
	(460011,46,460011),
	(460012,46,460012),
	(460013,46,460013),
	(470000,47,470000),
	(470001,47,470001),
	(470002,47,470002),
	(470003,47,470003),
	(470004,47,470004),
	(470005,47,470005),
	(470006,47,470006),
	(470007,47,470007),
	(470008,47,470008),
	(470009,47,470009),
	(470010,47,470010),
	(470011,47,470011),
	(470012,47,470012),
	(470013,47,470013),
	(470014,47,470014),
	(470015,47,470015),
	(470016,47,470016),
	(470017,47,470017),
	(470018,47,470018),
	(470019,47,470019),
	(470020,47,470020),
	(470021,47,470021),
	(470022,47,470022),
	(470023,47,470023),
	(470024,47,470024),
	(470025,47,470025),
	(470026,47,470026),
	(470027,47,470027),
	(470028,47,470028),
	(470029,47,470029),
	(470030,47,470030),
	(470031,47,470031),
	(470032,47,470032),
	(470033,47,470033),
	(470034,47,470034),
	(470035,47,470035),
	(470036,47,470036),
	(470037,47,470037),
	(470038,47,470038),
	(470039,47,470039),
	(470040,47,470040),
	(470041,47,470041),
	(470042,47,470042),
	(470043,47,470043),
	(470044,47,470044),
	(470045,47,470045),
	(470046,47,470046),
	(470047,47,470047),
	(470048,47,470048),
	(470049,47,470049),
	(470050,47,470050),
	(470051,47,470051),
	(470052,47,470052),
	(470053,47,470053),
	(470054,47,470054),
	(470055,47,470055),
	(470056,47,470056),
	(470057,47,470057),
	(470058,47,470058),
	(470059,47,470059),
	(470060,47,470060),
	(480000,48,480000),
	(490000,49,490000),
	(490001,49,490001),
	(490002,49,490002),
	(490003,49,490003),
	(490004,49,490004),
	(490005,49,490005),
	(490006,49,490006),
	(490007,49,490007),
	(490008,49,490008),
	(490009,49,490009),
	(490010,49,490010),
	(490011,49,490011),
	(490012,49,490012),
	(490013,49,490013),
	(490014,49,490014),
	(490015,49,490015),
	(490016,49,490016),
	(490017,49,490017),
	(490018,49,490018),
	(490019,49,490019),
	(500000,50,500000),
	(500001,50,500001),
	(500002,50,500002),
	(500003,50,500003),
	(500004,50,500004),
	(500005,50,500005),
	(500006,50,500006),
	(500007,50,500007),
	(500008,50,500008),
	(500009,50,500009),
	(500010,50,500010),
	(510000,51,510000),
	(510001,51,510001),
	(510002,51,510002),
	(510003,51,510003),
	(510004,51,510004),
	(510005,51,510005),
	(510006,51,510006),
	(2150000,215,2150000),
	(2150001,215,2150001),
	(2160000,216,2160000),
	(2160001,216,2160001),
	(2160002,216,2160002),
	(2160003,216,2160003),
	(2160004,216,2160004),
	(2160005,216,2160005),
	(2160006,216,2160006),
	(2160007,216,2160007),
	(2160008,216,2160008),
	(2160009,216,2160009),
	(2160010,216,2160010),
	(2170000,217,2170000),
	(2170001,217,2170001),
	(2170002,217,2170002),
	(2180000,218,2180000),
	(2180001,218,2180001),
	(2180002,218,2180002),
	(2180003,218,2180003),
	(2180004,218,2180004),
	(2180005,218,2180005),
	(2180006,218,2180006),
	(2190000,219,2190000),
	(2190001,219,2190001),
	(2190002,219,2190002),
	(520000,52,520000),
	(530000,53,530000),
	(540000,54,540000),
	(540001,54,540001),
	(540002,54,540002),
	(540003,54,540003),
	(540004,54,540004),
	(540005,54,540005),
	(540006,54,540006),
	(550000,55,550000),
	(560000,56,560000),
	(560001,56,560001),
	(560002,56,560002),
	(560003,56,560003),
	(560004,56,560004),
	(560005,56,560005),
	(560006,56,560006),
	(560007,56,560007),
	(560008,56,560008),
	(560009,56,560009),
	(560010,56,560010),
	(570000,57,570000),
	(570001,57,570001),
	(570002,57,570002),
	(590000,59,590000),
	(590001,59,590001),
	(590002,59,590002),
	(590003,59,590003),
	(590004,59,590004),
	(590005,59,590005),
	(590006,59,590006),
	(590007,59,590007),
	(590008,59,590008),
	(600000,60,600000),
	(600001,60,600001),
	(600002,60,600002),
	(600003,60,600003),
	(600004,60,600004),
	(600005,60,600005),
	(610000,61,610000),
	(610001,61,610001),
	(610002,61,610002),
	(610003,61,610003),
	(610004,61,610004),
	(610005,61,610005),
	(630000,63,630000),
	(630001,63,630001),
	(630002,63,630002),
	(630003,63,630003),
	(630004,63,630004),
	(630005,63,630005),
	(640000,64,640000),
	(640001,64,640001),
	(640002,64,640002),
	(640003,64,640003),
	(640004,64,640004),
	(640005,64,640005),
	(650000,65,650000),
	(650001,65,650001),
	(650002,65,650002),
	(650003,65,650003),
	(650004,65,650004),
	(650005,65,650005),
	(650006,65,650006),
	(650007,65,650007),
	(650008,65,650008),
	(650009,65,650009),
	(660000,66,660000),
	(660001,66,660001),
	(660002,66,660002),
	(660003,66,660003),
	(660004,66,660004),
	(660005,66,660005),
	(660006,66,660006),
	(660007,66,660007),
	(670000,67,670000),
	(680000,68,680000),
	(690000,69,690000),
	(690001,69,690001),
	(710000,71,710000),
	(730000,73,730000),
	(730001,73,730001),
	(740000,74,740000),
	(740001,74,740001),
	(740002,74,740002),
	(740003,74,740003),
	(740004,74,740004),
	(740005,74,740005),
	(740006,74,740006),
	(740007,74,740007),
	(740008,74,740008),
	(740009,74,740009),
	(740010,74,740010),
	(740011,74,740011),
	(740012,74,740012),
	(740013,74,740013),
	(740014,74,740014),
	(740015,74,740015),
	(760000,76,760000),
	(760001,76,760001),
	(760002,76,760002),
	(760003,76,760003),
	(760004,76,760004),
	(760005,76,760005),
	(760006,76,760006),
	(760007,76,760007),
	(760008,76,760008),
	(760009,76,760009),
	(760010,76,760010),
	(760011,76,760011),
	(760012,76,760012),
	(760013,76,760013),
	(760014,76,760014),
	(760015,76,760015),
	(760016,76,760016),
	(760017,76,760017),
	(760018,76,760018),
	(760019,76,760019),
	(760020,76,760020),
	(760021,76,760021),
	(760022,76,760022),
	(760023,76,760023),
	(770000,77,770000),
	(770001,77,770001),
	(770002,77,770002),
	(770003,77,770003),
	(770004,77,770004),
	(770005,77,770005),
	(770006,77,770006),
	(770007,77,770007),
	(770008,77,770008),
	(770009,77,770009),
	(10195,1,10195),
	(10196,1,10196),
	(10197,1,10197),
	(10198,1,10198),
	(10199,1,10199),
	(10200,1,10200),
	(10201,1,10201),
	(10202,1,10202),
	(10203,1,10203),
	(10204,1,10204),
	(10205,1,10205),
	(10206,1,10206),
	(10207,1,10207),
	(10208,1,10208),
	(10209,1,10209),
	(10210,1,10210),
	(10211,1,10211),
	(10212,1,10212),
	(10213,1,10213),
	(10214,1,10214),
	(10215,1,10215),
	(10216,1,10216),
	(10217,1,10217),
	(10218,1,10218),
	(10219,1,10219),
	(10220,1,10220),
	(10221,1,10221),
	(10222,1,10222),
	(10223,1,10223),
	(10224,1,10224),
	(10225,1,10225),
	(10226,1,10226),
	(10227,1,10227),
	(10228,1,10228),
	(10229,1,10229),
	(10230,1,10230),
	(10231,1,10231),
	(10232,1,10232),
	(10233,1,10233),
	(10234,1,10234),
	(10235,1,10235),
	(10236,1,10236),
	(10237,1,10237),
	(10238,1,10238),
	(10239,1,10239),
	(10240,1,10240),
	(10241,1,10241),
	(10242,1,10242),
	(10243,1,10243),
	(10244,1,10244),
	(10245,1,10245),
	(10246,1,10246),
	(10247,1,10247),
	(10248,1,10248),
	(10249,1,10249),
	(10250,1,10250),
	(10251,1,10251),
	(10252,1,10252),
	(10253,1,10253),
	(10254,1,10254),
	(10255,1,10255),
	(10256,1,10256),
	(10257,1,10257),
	(10258,1,10258),
	(10259,1,10259),
	(10260,1,10260),
	(10261,1,10261),
	(10262,1,10262),
	(10263,1,10263);

/*!40000 ALTER TABLE `source_file_text_resource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table study_period
# ------------------------------------------------------------

LOCK TABLES `study_period` WRITE;
/*!40000 ALTER TABLE `study_period` DISABLE KEYS */;

INSERT INTO `study_period` (`id`, `name`, `description`)
VALUES
	(1,'2010','Study period of the year of 2010');

/*!40000 ALTER TABLE `study_period` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table survey_answer
# ------------------------------------------------------------



# Dump of table survey_answer_attachment
# ------------------------------------------------------------



# Dump of table survey_answer_attachment_version
# ------------------------------------------------------------



# Dump of table survey_answer_version
# ------------------------------------------------------------



# Dump of table survey_category
# ------------------------------------------------------------

LOCK TABLES `survey_category` WRITE;
/*!40000 ALTER TABLE `survey_category` DISABLE KEYS */;

INSERT INTO `survey_category` (`id`, `survey_config_id`, `parent_category_id`, `name`, `description`, `label`, `title`, `weight`)
VALUES
	(1,1,0,'Cat_I','1st top category','I','Civil Society, Public Information and Media',1),
	(2,1,1,'Cat_I-1','1st subcategory of Category I','I-1','Civil Society Organizations',1),
	(3,1,2,'Set_I-1-1','1st question set of I-1','1','Are anti-corruption/good governance CSOs legally protected?',1),
	(4,1,2,'Set_I-1-2','2nd question set of Category I-1','2','Are good governance/anti-corruption CSOs able to operate freely?',2),
	(5,1,1,'Cat_I-2','2nd subcategory of Category I','I-2','Media',2),
	(6,1,5,'Set_I-2-1','1st question set in subcat I-2','5','Are media and free speech protected?',1),
	(7,1,5,'Set_I-2-2','2nd question set of Cat I-2','6','Are citizens able to form print media entities?',2),
	(8,1,0,'Cat_II','2nd top category','II','Elections',2),
	(9,1,8,'Cat_II-1','1st subcat of cat II','II-1','Voting & Citizen Participation',1),
	(10,1,9,'set_II-1-1','1st question set of II-1','14','Is there a legal framework guaranteeing the right to vote?',1),
	(11,1,9,'set_II-1-2','2nd question set of II-1','15','Can all citizens exercise their right to vote?',2),
	(12,2,0,'Cat_I','1st top category','I','Background',1),
	(13,2,12,'Cat_I-1','1st subcat of category I','I-1','Basic Info',1),
	(14,2,13,'Set_I-1-1','1st question set of I-1','1','Basic Statistics',1),
	(15,2,0,'Cat_II','2nd top category','II','Energy Study',2),
	(16,2,15,'cat_II-1','1st subcat of II','II-1','Policies',1),
	(17,2,16,'Set_II-1-1','1st question set of II-1','2','Laws and Regulations',1),
	(18,2,15,'Cat_II-2','2nd subcat of II','II-2','Energy Sources',2),
	(19,2,18,'Set_II-2-1','1st question set of II-2','3','Energy Sources',1);

/*!40000 ALTER TABLE `survey_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table survey_config
# ------------------------------------------------------------

LOCK TABLES `survey_config` WRITE;
/*!40000 ALTER TABLE `survey_config` DISABLE KEYS */;

INSERT INTO `survey_config` (`id`, `name`, `description`, `instructions`, `moe_algorithm`)
VALUES
	(1,'Scorecard 2010','Scorecard config for Project 2010','Please complete the questions the best you can. When in doubt, feel free to contact the project management team.',1),
	(2,'Energy Policy Survey 2010','Definition of energy policy survey for the year 2010','Please provide answers to the questions in the survey. Make sure to provide source of data.',1);

/*!40000 ALTER TABLE `survey_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table survey_content_object
# ------------------------------------------------------------

LOCK TABLES `survey_content_object` WRITE;
/*!40000 ALTER TABLE `survey_content_object` DISABLE KEYS */;

INSERT INTO `survey_content_object` (`id`, `content_header_id`, `survey_config_id`)
VALUES
	(1,2,1),
	(2,6,1),
	(3,7,1),
	(4,8,1);

/*!40000 ALTER TABLE `survey_content_object` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table survey_indicator
# ------------------------------------------------------------

LOCK TABLES `survey_indicator` WRITE;
/*!40000 ALTER TABLE `survey_indicator` DISABLE KEYS */;

INSERT INTO `survey_indicator` (`id`, `name`, `question`, `answer_type`, `answer_type_id`, `reference_id`, `tip`, `create_user_id`, `create_time`, `reusable`, `original_indicator_id`)
VALUES
	(1,'Anti-Corruption-001','In law, citizens have a right to form civil society organizations (CSOs) focused on anti-corruption or good governance.',1,1,1,NULL,1,'2010-06-10 22:07:12',1,NULL),
	(2,'Anti-Corruption-002','In law, anti-corruption/good governance CSOs are free to accept funding from any foreign or domestic sources.',1,2,1,NULL,1,'2010-06-10 22:11:34',1,NULL),
	(3,'Anti-Corruption-003','In law, anti-corruption/good governance CSOs are required to disclose their sources of funding.',1,3,1,'Such funding disclosures should be publicly accessible in some way and not simply disclosed only to the government.',1,'2010-06-10 22:12:34',1,2),
	(4,'Anti-Corruption-004','In practice, the government does not create barriers to the organization of new anti-corruption/good governance CSOs.',1,4,3,'In your comments, please provide both general statements on the environment within which CSOs operate as well as references to challenges facing specific CSOs.',1,'2010-06-10 22:15:55',1,NULL),
	(5,'Anti-Corruption-005','In practice, anti-corruption/good governance CSOs actively engage in the political and policymaking process.',1,5,3,'Please provide comments on specific CSOs that have been successful or unsuccessful in their political outreach.',1,'2010-06-11 11:49:07',1,NULL),
	(8,'Anti-Corruption-006','In practice, no anti-corruption/good governance CSOs have been shut down by the government for their work on corruption-related issues during the study period.',1,8,3,'Beware of confusion with the double negative in this indicator. Please pay close attention to the scoring criteria. When scoring this indicator, be sure to take a broad interpretation of Ã¢â?¬Å?corruption.Ã¢â?¬Â This can mean issues related to governance practices a requirement.',1,'2010-06-11 13:27:22',1,NULL),
	(9,'Anti-Corruption-007','What anti-corruption methods are available to citizens?',2,9,1,NULL,1,'2010-06-11 14:34:16',1,NULL),
	(10,'Basic-001','How many states or provinces in the nation?',3,1,1,NULL,1,'2010-06-11 14:41:02',1,NULL),
	(11,'Basic-002','Please give the names of the 3 biggest cities in the nation.',5,1,2,NULL,1,'2010-06-11 14:42:10',1,NULL),
	(12,'Basic-003','What\'s the average hourly wage in the nation?',4,1,1,NULL,1,'2010-06-11 14:42:50',1,NULL),
	(13,'Basic-004','How many major cities in the nation?',3,2,1,NULL,1,'2010-06-11 14:43:35',1,NULL),
	(14,'Election-001','In law, there is a legal framework requiring that elections be held at regular intervals.',1,10,1,NULL,1,'2010-06-11 14:44:12',1,NULL),
	(15,'Election-002','In practice, all adult citizens can vote.',1,11,3,'A YES score can still be earned if a basic age requirement exists.',1,'2010-06-11 14:46:01',1,NULL),
	(16,'Election-003','In practice, ballots are secret or equivalently protected.',1,12,3,'When scoring this indicator, please consider: are citizens able to vote in secret or are there government, military or party agents overtly pressuring voters at the polls? Are the ballots protected when they are transported? Have there been examples of tampering of ballots before or after voting periods?  Please provide any useful examples in your Comments.',1,'2010-06-11 14:48:04',1,NULL),
	(17,'Election-004','In practice, elections are held according to a regular schedule.',1,13,3,NULL,1,'2010-06-11 14:50:25',1,NULL),
	(18,'Energy-Policy-001','Is there any legislation about energy conservation and alternative energy sources?',1,14,1,NULL,1,'2010-06-11 14:52:09',1,NULL),
	(19,'Energy-Policy-002','What are the main energy sources in the nation?',2,15,3,NULL,1,'2010-06-11 14:53:26',1,NULL),
	(20,'Energy-Policy-003','Is there any incentive programs for the use of green energy?',1,16,3,'The following energy sources are typically considered Green: solar, wind, water.',1,'2010-06-11 14:55:40',1,NULL),
	(21,'Energy-Policy-004','What\'s the average daily sunshine time (in hours) in summer?',4,2,3,NULL,1,'2010-06-11 14:58:39',1,NULL),
	(22,'Energy-Policy-005','What\'s the average daily sunshine time (in hours) in seasons other than summer?',4,3,3,NULL,1,'2010-06-11 14:59:38',1,NULL),
	(23,'Energy-Policy-006','Which energy source is the primary source?',1,17,3,NULL,1,'2010-06-11 15:00:14',1,NULL),
	(24,'Freedom-001','In law, freedom of the media is guaranteed.',1,18,1,NULL,1,'2010-06-11 15:03:30',1,NULL),
	(25,'Freedom-002','In law, freedom of speech is guaranteed.',1,19,1,'Please pay careful attention to the NO criteria; a NO score should be triggered if there are specific prohibitions against speaking out against particular public figures or political leaders.',1,'2010-06-11 15:04:55',1,NULL),
	(26,'Freedom-003','In practice, the government does not create barriers to form a print media entity.',1,20,3,NULL,1,'2010-06-11 15:05:56',1,NULL),
	(27,'Freedom-004','In law, where a print media license is necessary, there is an appeals mechanism if a license is denied or revoked.',1,21,2,'In addition to providing the name of the appeals mechanism, please also provide some details on how the appeals mechanism works.',1,'2010-06-11 15:23:52',1,NULL),
	(28,'Freedom-005','In practice, where necessary, citizens can obtain a print media license within a reasonable time period.',1,22,3,NULL,1,'2010-06-11 15:26:52',1,NULL),
	(29,'Freedom-006','In practice, where necessary, citizens can obtain a print media license at a reasonable cost.',1,23,3,NULL,1,'2010-06-11 15:28:28',1,NULL),
	(30,'Freedom-007','In law, universal and equal adult suffrage is guaranteed to all citizens.',1,24,1,NULL,1,'2010-06-11 15:30:02',1,NULL);

/*!40000 ALTER TABLE `survey_indicator` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table survey_peer_review
# ------------------------------------------------------------



# Dump of table survey_question
# ------------------------------------------------------------

LOCK TABLES `survey_question` WRITE;
/*!40000 ALTER TABLE `survey_question` DISABLE KEYS */;

INSERT INTO `survey_question` (`id`, `name`, `survey_config_id`, `survey_indicator_id`, `survey_category_id`, `public_name`, `default_answer_id`, `weight`)
VALUES
	(1,'Anti-Corruption-001: 1a',1,1,3,'1a',1,1),
	(2,'Anti-Corruption-002: 1b',1,2,3,'1b',2,2),
	(3,'Anti-Corruption-003: 1c',1,3,3,'1c',3,3),
	(4,'Anti-Corruption-004: 2a',1,4,4,'2a',4,1),
	(5,'Anti-Corruption-005: 2b',1,5,4,'2b',5,2),
	(6,'Anti-Corruption-006: 2c',1,8,4,'2c',6,3),
	(7,'Basic-001: 2x',1,10,4,'2x',1,4),
	(8,'Basic-002: 2y',1,11,4,'2y',0,5),
	(9,'Basic-003: 2z',1,12,4,'2z',1,6),
	(10,'Anti-Corruption-007: 2w',1,9,4,'2w',7,7),
	(11,'Freedom-001: 5a',1,24,6,'5a',8,1),
	(12,'Freedom-002: 5b',1,25,6,'5b',9,2),
	(13,'Freedom-003: 6a',1,26,7,'6a',10,1),
	(14,'Freedom-004: 6b',1,27,7,'6b',11,2),
	(15,'Freedom-005: 6c',1,28,7,'6c',12,3),
	(16,'Freedom-006: 6d',1,29,7,'6d',13,4),
	(17,'Freedom-007: 14a',1,30,10,'14a',14,1),
	(18,'Election-001: 14b',1,14,10,'14b',15,2),
	(19,'Election-002: 15a',1,15,11,'15a',16,1),
	(20,'Election-003: 15b',1,16,11,'15b',17,2),
	(21,'Election-004: 15c',1,17,11,'15c',18,3),
	(22,'Basic-001: 1a',2,10,14,'1a',2,1),
	(23,'Basic-002: 1b',2,11,14,'1b',0,2),
	(24,'Energy-Policy-001: 2a',2,18,17,'2a',19,1),
	(25,'Energy-Policy-003: 2b',2,20,17,'2b',20,2),
	(26,'Energy-Policy-002: 3a',2,19,19,'3a',21,2),
	(27,'Energy-Policy-004: 3b',2,21,19,'3b',2,3),
	(28,'Energy-Policy-005: 3c',2,22,19,'3c',3,5),
	(29,'Energy-Policy-006: 3d',2,23,19,'3d',22,6),
	(30,'Energy-Policy-006',1,23,3,'Energy-Policy-006',NULL,1);

/*!40000 ALTER TABLE `survey_question` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tag
# ------------------------------------------------------------



# Dump of table target
# ------------------------------------------------------------

LOCK TABLES `target` WRITE;
/*!40000 ALTER TABLE `target` DISABLE KEYS */;

INSERT INTO `target` (`id`, `name`, `description`, `target_type`, `short_name`, `gid`)
VALUES
	(1,'US','United States',0,'USA',NULL),
	(2,'Argentina','Argentina',0,'ARG',NULL),
	(3,'Brazil','Brazil',0,'BRZ',NULL),
	(4,'China','PR China',0,'CHN',NULL),
	(5,'Japan','Japan',0,'JAP',NULL),
	(6,'Taiwan','Taiwan',1,'TWN',NULL),
	(7,'UK','United Kingdom',0,'UK',NULL);

/*!40000 ALTER TABLE `target` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table target_tag
# ------------------------------------------------------------



# Dump of table task
# ------------------------------------------------------------

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;

INSERT INTO `task` (`id`, `task_name`, `description`, `goal_id`, `product_id`, `tool_id`, `assignment_method`, `instructions`, `type`)
VALUES
	(-1,'Journal Review Response','Author provides response to review feedback',0,0,16,3,'Use this tool to provide response to reviewer feedback or questions.',16),
	(-2,'Survey Review Response','Author provides response to review feedback',0,0,17,3,'Use this tool to provide response to reviewer feedback or questions.',17),
	(1,'nb_submit_1','First notebook submission',9,1,1,1,'Your content will be completed in 2 rounds of submission. This is the first round of notebook submission.',1),
	(2,'nb_submit_2','2nd submission',10,1,1,1,'This is the 2nd round of notebook submission.',1),
	(3,'nb_staff_review_1','1st staff review',7,1,3,2,'This is the 1st round of staff review.',3),
	(4,'nb_staff_review_2','2nd staff review',8,1,3,2,'This is the second round of staff review.',3),
	(41,'nb_review_resp','response to review feedback',8,1,16,3,'Please provide response to reviewer feedback or questions.',16),
	(5,'nb_edit_1','1st edit',1,1,2,2,'This is the first round of edit.',2),
	(6,'nb_edit_2','2nd edit',2,1,2,2,'This is the 2nd content edit.',2),
	(7,'nb_edit_review','Review results of edit',3,1,2,2,'You are required to review the results of content edits.',2),
	(8,'nb_peer_review','Peer review',5,1,4,1,'Please review the content and provide your feedback.',4),
	(9,'nb_pr_review','Review results of peer review',6,1,13,2,'You are required to review the results of peer reviews.',13),
	(10,'nb_overall_review','Overall review',4,1,18,1,'This is the final review of the content.',18),
	(11,'sc_submit_1','First submission',11,2,6,1,'You will complete the scorecard in 3 rounds. This is the 1st round of submission.',6),
	(12,'sc_submit_2','2nd submission',13,2,6,1,'This is the 2nd round of submission.',6),
	(13,'sc_review_1','1st staff review',12,2,8,2,'This is the 1st round of staff review',8),
	(14,'sc_review_2','2nd staff review',14,2,8,2,'This is the 2nd staff review',8),
	(15,'sc_submit_3','2rd submission',16,2,6,1,'This is the 3rd round of submission',6),
	(16,'sc_review_3','3rd staff review',17,2,8,2,'This is the 3rd staff review',8),
	(42,'sc_review_resp','response to review feedback',17,2,17,3,'Please provide response to reviewer feedback or questions.',17),
	(17,'sc_peer_review','Peer review',15,2,9,1,'Please provide your opinions about the scorecard.',9),
	(18,'sc_pr_review','Review of peer review',18,2,14,2,'Please review the results of peer reviews',14),
	(20,'sc_pay_1','1st payment',19,2,11,1,'Please make the 1st payment to the author',11),
	(21,'sc_pay_2','2nd payment',20,2,11,1,'Please make the 2nd payment to the author',11),
	(22,'sc_start_horse','Start horse',21,2,15,2,'Please review and edit peer reviews.',15),
	(23,'sc_edit','Edit the scorecard',22,2,7,2,'Please review and edit the scorecard',7),
	(24,'epa_submit','Submit content',25,3,1,1,'Please create and submit your analysis of your country\'s energy policy.',1),
	(25,'epa_review','Review the content',26,3,3,2,'Please review the content and provide your feedback. You can also discuss with the author by using the staff/author discussion box.',3),
	(26,'epa_edit','Edit content',27,3,2,2,'Please edit the essay: fix typos, correct grammar errors, or rewrite the sentences. But please make sure that author\'s opinions are not changed.',2),
	(27,'epa_peer_review','Peer review',28,3,4,1,'Please read the essay and provide your opinions.',4),
	(28,'epa_pr_review','PR review',29,3,13,2,'Please review the peer reviews, and provide your feedback in each peer-review\'s discussion box.',13),
	(29,'epa_start','Approve to start the workflow',32,3,15,1,'The workflow will not continue until you start it.',15),
	(30,'epa_approve','Approve and accept the content',30,3,12,1,'Approve and accept the content after review it.',12),
	(31,'epa_pay','Make payment to the author',31,3,11,1,'Please make payment to the author',11),
	(32,'eps_submit','Submit the survey',25,4,6,1,'Please complete and submit the survey.',6),
	(33,'eps_review','Review the survey',26,4,8,2,'Please review the survey and provide your feedback.',8),
	(34,'eps_approve','Approve and accept the content',30,4,12,1,'Content is not accepted until you approve it.',12),
	(35,'eps_edit','Edit content',27,4,7,2,'Please edit the survey: fix typos and grammar errors.',7),
	(36,'eps_pay','Pay the author',31,4,11,1,'Please make payment to the author.',11),
	(37,'eps_peer_review','Peer review',28,4,9,1,'Please review the survey and provide your opinions.',9),
	(38,'eps_pr_review','Review of peer reviews',29,4,14,2,'Please review all peer reviews and provide your feedback',14),
	(39,'eps_start','Start the workflow',32,4,15,1,'Please approve to start the workflow process.',15),
	(40,'sc_finish','Finish up',24,2,19,1,'This is the final touch-up of the content. All required tasks have been completed.',19);

/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table task_assignment
# ------------------------------------------------------------

LOCK TABLES `task_assignment` WRITE;
/*!40000 ALTER TABLE `task_assignment` DISABLE KEYS */;

INSERT INTO `task_assignment` (`id`, `task_id`, `target_id`, `assigned_user_id`, `due_time`, `status`, `start_time`, `completion_time`, `event_log_id`, `q_enter_time`, `q_last_assigned_time`, `q_last_assigned_uid`, `q_last_return_time`, `q_priority`, `data`, `goal_object_id`, `percent`, `horse_id`)
VALUES
	(1,1,1,16,NULL,3,NULL,'2010-07-29 01:42:31',NULL,NULL,'2010-07-20 06:36:21',NULL,NULL,NULL,NULL,9,-1,1),
	(2,1,2,13,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:29:37',NULL,NULL,NULL,NULL,32,-1,3),
	(3,1,3,14,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:29:55',NULL,NULL,NULL,NULL,42,-1,4),
	(4,1,4,15,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:30:07',NULL,NULL,NULL,NULL,52,-1,5),
	(5,2,1,16,NULL,2,NULL,'2010-08-08 10:07:23',NULL,NULL,'2010-07-20 08:36:16',NULL,NULL,NULL,NULL,10,-1,1),
	(6,2,2,13,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:36:27',NULL,NULL,NULL,NULL,33,-1,3),
	(7,2,3,14,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:36:38',NULL,NULL,NULL,NULL,43,-1,4),
	(8,2,4,15,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:36:50',NULL,NULL,NULL,NULL,53,-1,5),
	(9,8,1,21,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 16:48:39',NULL,NULL,NULL,NULL,5,-1,1),
	(10,8,1,17,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:50:17',NULL,NULL,NULL,NULL,5,-1,1),
	(11,8,2,19,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 16:48:58',NULL,NULL,NULL,NULL,28,-1,3),
	(12,8,2,18,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:50:30',NULL,NULL,NULL,NULL,28,-1,3),
	(13,8,3,21,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 16:49:11',NULL,NULL,NULL,NULL,38,-1,4),
	(14,8,3,18,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:50:41',NULL,NULL,NULL,NULL,38,-1,4),
	(15,8,4,17,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:50:51',NULL,NULL,NULL,NULL,48,-1,5),
	(16,8,4,19,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 16:49:28',NULL,NULL,NULL,NULL,48,-1,5),
	(17,10,2,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:49:30',NULL,NULL,NULL,NULL,27,-1,3),
	(18,10,1,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:49:21',NULL,NULL,NULL,NULL,4,-1,1),
	(19,10,3,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:49:40',NULL,NULL,NULL,NULL,37,-1,4),
	(20,10,4,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:49:50',NULL,NULL,NULL,NULL,47,-1,5),
	(21,11,1,9,NULL,4,NULL,NULL,NULL,NULL,'2010-07-20 09:04:17',NULL,NULL,NULL,NULL,16,-1,2),
	(22,11,2,10,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:04:26',NULL,NULL,NULL,NULL,72,-1,7),
	(23,11,3,11,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:04:39',NULL,NULL,NULL,NULL,59,-1,6),
	(24,11,4,12,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:04:48',NULL,NULL,NULL,NULL,85,-1,8),
	(25,12,1,9,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:05:05',NULL,NULL,NULL,NULL,17,-1,2),
	(26,12,2,10,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:05:17',NULL,NULL,NULL,NULL,73,-1,7),
	(27,12,3,11,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:05:30',NULL,NULL,NULL,NULL,60,-1,6),
	(28,12,4,12,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:05:41',NULL,NULL,NULL,NULL,86,-1,8),
	(29,15,2,10,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:06:05',NULL,NULL,NULL,NULL,74,-1,7),
	(30,15,1,9,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:05:55',NULL,NULL,NULL,NULL,18,-1,2),
	(31,15,3,11,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:06:13',NULL,NULL,NULL,NULL,61,-1,6),
	(32,15,4,12,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:06:21',NULL,NULL,NULL,NULL,87,-1,8),
	(33,17,1,21,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 18:01:32',NULL,NULL,NULL,NULL,11,-1,2),
	(34,17,1,17,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:59:42',NULL,NULL,NULL,NULL,11,-1,2),
	(35,17,2,19,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 18:01:40',NULL,NULL,NULL,NULL,67,-1,7),
	(36,17,2,18,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:59:53',NULL,NULL,NULL,NULL,67,-1,7),
	(37,17,3,21,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 18:01:51',NULL,NULL,NULL,NULL,54,-1,6),
	(38,17,3,18,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:00:02',NULL,NULL,NULL,NULL,54,-1,6),
	(39,17,4,17,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:00:13',NULL,NULL,NULL,NULL,80,-1,8),
	(40,17,4,19,NULL,1,NULL,NULL,NULL,NULL,'2010-06-14 18:01:59',NULL,NULL,NULL,NULL,80,-1,8),
	(41,20,1,8,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:14:43',NULL,NULL,NULL,NULL,19,-1,2),
	(42,20,2,8,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:58:07',NULL,NULL,NULL,NULL,75,-1,7),
	(43,20,3,8,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:58:16',NULL,NULL,NULL,NULL,62,-1,6),
	(44,20,4,8,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:58:26',NULL,NULL,NULL,NULL,88,-1,8),
	(45,40,1,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:57:14',NULL,NULL,NULL,NULL,23,-1,2),
	(46,40,2,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:57:23',NULL,NULL,NULL,NULL,79,-1,7),
	(47,40,3,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:57:32',NULL,NULL,NULL,NULL,66,-1,6),
	(48,40,4,5,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:57:44',NULL,NULL,NULL,NULL,92,-1,8),
	(49,5,1,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:45:41',NULL,NULL,NULL,NULL,1,-1,1),
	(50,5,2,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:46:06',NULL,NULL,NULL,NULL,24,-1,3),
	(51,5,3,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:46:15',NULL,NULL,NULL,NULL,34,-1,4),
	(52,5,4,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:46:26',NULL,NULL,NULL,NULL,44,-1,5),
	(53,6,1,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:47:15',NULL,NULL,NULL,NULL,2,-1,1),
	(54,6,2,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:47:29',NULL,NULL,NULL,NULL,25,-1,3),
	(55,6,3,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:47:39',NULL,NULL,NULL,NULL,35,-1,4),
	(56,6,4,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:47:48',NULL,NULL,NULL,NULL,45,-1,5),
	(57,7,1,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:48:35',NULL,NULL,NULL,NULL,3,-1,1),
	(58,7,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:48:44',NULL,NULL,NULL,NULL,26,-1,3),
	(59,7,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:48:53',NULL,NULL,NULL,NULL,36,-1,4),
	(60,7,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:49:03',NULL,NULL,NULL,NULL,46,-1,5),
	(61,9,1,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:52:03',NULL,NULL,NULL,NULL,6,-1,1),
	(62,9,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:52:20',NULL,NULL,NULL,NULL,29,-1,3),
	(63,9,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:52:33',NULL,NULL,NULL,NULL,39,-1,4),
	(64,9,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:52:43',NULL,NULL,NULL,NULL,49,-1,5),
	(65,3,1,3,NULL,4,NULL,NULL,0,NULL,'2010-07-20 08:53:02',0,NULL,0,NULL,7,0.5,1),
	(66,3,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:53:14',NULL,NULL,NULL,NULL,30,-1,3),
	(67,3,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:53:24',NULL,NULL,NULL,NULL,40,-1,4),
	(68,3,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:53:32',NULL,NULL,NULL,NULL,50,-1,5),
	(69,4,1,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:53:46',NULL,NULL,NULL,NULL,8,-1,1),
	(70,4,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 08:53:54',NULL,NULL,NULL,NULL,31,-1,3),
	(71,4,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:54:03',NULL,NULL,NULL,NULL,41,-1,4),
	(72,4,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:54:12',NULL,NULL,NULL,NULL,51,-1,5),
	(73,23,1,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:56:15',NULL,NULL,NULL,NULL,22,-1,2),
	(74,23,2,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:56:24',NULL,NULL,NULL,NULL,78,-1,7),
	(75,23,3,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:56:33',NULL,NULL,NULL,NULL,65,-1,6),
	(76,23,4,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:56:49',NULL,NULL,NULL,NULL,91,-1,8),
	(77,22,1,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:03:33',NULL,NULL,NULL,NULL,21,-1,2),
	(78,22,2,2,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:03:42',NULL,NULL,NULL,NULL,77,-1,7),
	(79,22,3,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:03:52',NULL,NULL,NULL,NULL,64,-1,6),
	(80,22,4,20,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:04:01',NULL,NULL,NULL,NULL,90,-1,8),
	(81,18,1,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:00:30',NULL,NULL,NULL,NULL,12,-1,2),
	(82,18,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:00:44',NULL,NULL,NULL,NULL,68,-1,7),
	(83,18,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:00:53',NULL,NULL,NULL,NULL,55,-1,6),
	(84,18,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:01:02',NULL,NULL,NULL,NULL,81,-1,8),
	(85,13,1,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:01:14',NULL,NULL,NULL,NULL,13,-1,2),
	(86,13,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:01:24',NULL,NULL,NULL,NULL,69,-1,7),
	(87,13,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:01:33',NULL,NULL,NULL,NULL,56,-1,6),
	(88,13,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:01:43',NULL,NULL,NULL,NULL,82,-1,8),
	(89,14,1,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:02:01',NULL,NULL,NULL,NULL,14,-1,2),
	(90,14,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:02:11',NULL,NULL,NULL,NULL,70,-1,7),
	(91,14,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:02:19',NULL,NULL,NULL,NULL,57,-1,6),
	(92,14,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:02:28',NULL,NULL,NULL,NULL,83,-1,8),
	(93,16,1,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:02:41',NULL,NULL,NULL,NULL,15,-1,2),
	(94,16,2,3,NULL,2,NULL,NULL,NULL,NULL,'2010-07-20 09:02:50',NULL,NULL,NULL,NULL,71,-1,7),
	(95,16,3,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:02:59',NULL,NULL,NULL,NULL,58,-1,6),
	(96,16,4,4,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 09:03:09',NULL,NULL,NULL,NULL,84,-1,8),
	(97,21,1,7,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:58:42',NULL,NULL,NULL,NULL,20,-1,2),
	(98,21,2,7,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:58:49',NULL,NULL,NULL,NULL,76,-1,7),
	(99,21,3,7,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:58:58',NULL,NULL,NULL,NULL,63,-1,6),
	(100,21,4,7,NULL,1,NULL,NULL,NULL,NULL,'2010-07-20 08:59:03',NULL,NULL,NULL,NULL,89,-1,8);

/*!40000 ALTER TABLE `task_assignment` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table task_role
# ------------------------------------------------------------

LOCK TABLES `task_role` WRITE;
/*!40000 ALTER TABLE `task_role` DISABLE KEYS */;

INSERT INTO `task_role` (`id`, `task_id`, `role_id`)
VALUES
	(1,1,6),
	(2,2,6),
	(3,3,4),
	(4,4,4),
	(5,5,3),
	(6,6,3),
	(7,7,4),
	(8,8,5),
	(9,9,4),
	(10,10,2),
	(11,11,7),
	(12,12,7),
	(13,13,4),
	(14,14,4),
	(15,15,7),
	(16,16,4),
	(17,17,5),
	(18,18,4),
	(19,20,8),
	(20,21,8),
	(21,22,3),
	(22,23,3),
	(23,24,6),
	(24,25,4),
	(25,26,3),
	(26,27,5),
	(27,28,4),
	(28,29,2),
	(29,30,2),
	(31,31,2),
	(32,32,7),
	(33,33,4),
	(34,34,2),
	(35,35,3),
	(36,36,2),
	(37,37,5),
	(38,38,4),
	(39,39,2),
	(40,40,2);

/*!40000 ALTER TABLE `task_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table team
# ------------------------------------------------------------

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;

INSERT INTO `team` (`id`, `project_id`, `team_name`, `description`)
VALUES
	(1,1,'Reporters','Reporter Team'),
	(2,1,'Researchers','Team of researchers'),
	(3,1,'Staff','Internal employees'),
	(4,1,'Peer Reviewers','Team of peer reviewers');

/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table team_user
# ------------------------------------------------------------

LOCK TABLES `team_user` WRITE;
/*!40000 ALTER TABLE `team_user` DISABLE KEYS */;

INSERT INTO `team_user` (`id`, `user_id`, `team_id`)
VALUES
	(1,13,1),
	(2,15,1),
	(3,14,1),
	(4,16,1),
	(5,9,2),
	(6,11,2),
	(7,12,2),
	(8,10,2),
	(9,2,3),
	(10,20,3),
	(11,3,3),
	(12,4,3),
	(13,5,3),
	(14,6,3),
	(15,7,3),
	(16,8,3),
	(17,21,4),
	(18,17,4),
	(20,19,4);

/*!40000 ALTER TABLE `team_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table text_item
# ------------------------------------------------------------

LOCK TABLES `text_item` WRITE;
/*!40000 ALTER TABLE `text_item` DISABLE KEYS */;

INSERT INTO `text_item` (`id`, `text_resource_id`, `language_id`, `text`)
VALUES
	(10000,10000,1,'Log in failed!'),
	(10002,10001,1,'The attached file is missing from the data store. Please contact system administrator!'),
	(10004,10002,1,'Internal server error/exception occurs. Please contact your administrator!'),
	(10006,10003,1,'Invalid parameter: \"{0}\" is unrecongnized!'),
	(10008,10004,1,'Invalid request: parameter \"{0}\" is not specified!'),
	(10010,10005,1,'{0} is not existed: {1}!'),
	(10012,10006,1,'Exceeds max file size. Note, the permitted max file size is 10M!'),
	(10024,10012,1,'You have completed this assignment. Thank you!'),
	(10026,10013,1,'Notebook Saved!'),
	(10028,10014,1,'suspended'),
	(10030,10015,1,'inactive'),
	(10032,10016,1,'active'),
	(10034,10017,1,'aware'),
	(10038,10019,1,'started'),
	(10040,10020,1,'done'),
	(10042,10021,1,'STATUS'),
	(10044,10022,1,'VIEW'),
	(10046,10023,1,'HISTORY'),
	(10048,10024,1,'GOAL'),
	(10050,10025,1,'ESTIMATE'),
	(10052,10026,1,'% DONE'),
	(10054,10027,1,'NEXT DUE'),
	(10056,10028,1,'OPEN CASES'),
	(10058,10029,1,'PEOPLE ASSIGNED'),
	(10060,10030,1,'This box is currently empty.'),
	(10062,10031,1,'not started'),
	(10064,10032,1,'completed'),
	(10066,10033,1,'in progress'),
	(10068,10034,1,'noticed'),
	(10070,10035,1,'signed in'),
	(10072,10036,1,'% complete'),
	(10074,10037,1,'Next Step'),
	(10076,10038,1,'No assginment for \'{0}\''),
	(10078,10039,1,'view content'),
	(10080,10040,1,'history chart'),
	(10082,10041,1,'Edit'),
	(10084,10042,1,'Product'),
	(10086,10043,1,'Exclude Selected'),
	(10088,10044,1,'Include Selected'),
	(10090,10045,1,'Overdue'),
	(10092,10046,1,'Not Overdue'),
	(10094,10047,1,'All'),
	(10096,10048,1,'Add'),
	(10098,10049,1,'Answer'),
	(10100,10050,1,'CASE'),
	(10102,10051,1,'TITLE'),
	(10104,10052,1,'PRIORITY'),
	(10106,10053,1,'OWNER'),
	(10108,10054,1,'ATTACHED CONTENT'),
	(10110,10055,1,'NAME'),
	(10112,10056,1,'ROLE'),
	(10114,10057,1,'COUNTRY'),
	(10116,10058,1,'TEAMS'),
	(10118,10059,1,'ASSIGNED CONTENT'),
	(10120,10060,1,'Add note successfully!'),
	(10122,10061,1,'Failed to add {0} note!'),
	(10124,10062,1,'Create new case successfully!'),
	(10126,10063,1,'Create new case failed!'),
	(10128,10064,1,'SUCCESS'),
	(10130,10065,1,'FAIL'),
	(10132,10066,1,'Total: {0, number, #}, New: {1, number, #}'),
	(10134,10067,1,'Change password successfully!'),
	(10136,10068,1,'Change password failed!'),
	(10138,10069,1,'Anonymous'),
	(10140,10070,1,'Contributor'),
	(10142,10071,1,'No Project'),
	(10144,10072,1,'Invalid username!'),
	(10146,10073,1,'Validity username!'),
	(10148,10074,1,'Invalid parameters!'),
	(10150,10075,1,'Thank you for your answer!'),
	(10152,10076,1,'Only English is supported in this version!'),
	(10154,10077,1,'Cannot find survey question!'),
	(10156,10078,1,'Cannot get country name!'),
	(10158,10079,1,'Update case successfully!'),
	(10160,10080,1,'Update case failed!'),
	(10162,10081,1,'DEADLINE'),
	(10164,10082,1,'Please provide your opinions!'),
	(10166,10083,1,'Info'),
	(10168,10084,1,'Error'),
	(10170,10085,1,'Confirmation'),
	(10172,10086,1,'Warn'),
	(10174,10087,1,'Failed to submit your review because of internal error!'),
	(10176,10088,1,'Saved'),
	(10178,10089,1,'Please input source information!'),
	(10180,10090,1,'Please choose a source!'),
	(10182,10091,1,'Please choose at lease one source!'),
	(10184,10092,1,'Please input source description!'),
	(10186,10093,1,'Please choose a score option!'),
	(10188,10094,1,'Please input your answer!'),
	(10190,10095,1,'Please provide your suggested scoring change as well as comments supporting your change!'),
	(10192,10096,1,'Please enter your comments!'),
	(10194,10097,1,'Server internal error, please concact to your administrator!'),
	(10196,10098,1,'You have {0} answers left which have not been reviewed!'),
	(10198,10099,1,'Are you sure you have completed the assignment and want to submit now?'),
	(10200,10100,1,'There are pending questions that author has not responded.\nAre you sure you have completed the assignment and want to submit now?'),
	(10202,10101,1,'This text is currently <span style=\'font-weight: bold; color: orange;\'>{0}</span> words long. This exceeds the maximum word count of <span style=\'font-weight: bold; color: orange;\'>{1}</span>. Your work has not been submitted. Please revise the text and resubmit your work.'),
	(10204,10102,1,'This text is currently <span style=\'font-weight: bold; color: orange;\'>{0}</span> words long.  This is below the minimum word count of <span style=\'font-weight: bold; color: orange;\'>{1}</span>.  Your work has not been submitted. Please revise the text and resubmit your work.'),
	(10208,10104,1,'Please input the topic title!'),
	(10210,10105,1,'User Notes'),
	(10212,10106,1,'Staff Notes'),
	(10214,10107,1,'View'),
	(10218,10109,1,'Click to unblock'),
	(10220,10110,1,'Source'),
	(10222,10111,1,'PasteText'),
	(10224,10112,1,'PasteFromWord'),
	(10226,10113,1,'Print'),
	(10228,10114,1,'SpellChecker'),
	(10230,10115,1,'Scayt'),
	(10232,10116,1,'Undo'),
	(10234,10117,1,'Redo'),
	(10236,10118,1,'Find'),
	(10238,10119,1,'Replace'),
	(10240,10120,1,'SelectAll'),
	(10242,10121,1,'RemoveFormat'),
	(10244,10122,1,'Bold'),
	(10246,10123,1,'Italic'),
	(10248,10124,1,'Underline'),
	(10250,10125,1,'Strike'),
	(10252,10126,1,'NumberedList'),
	(10254,10127,1,'BulletedList'),
	(10256,10128,1,'Link'),
	(10258,10129,1,'Unlink'),
	(10260,10130,1,'Anchor'),
	(10262,10131,1,'Format'),
	(10264,10132,1,'BGColor'),
	(10266,10133,1,'Maximize'),
	(10268,10134,1,'MyToolbar'),
	(10270,10135,1,'Please input your enhanced text!'),
	(10272,10136,1,'TIME'),
	(10274,10137,1,'TOPIC TITLE'),
	(10276,10138,1,'Your enhancement saved successfully!'),
	(10278,10139,1,'Success'),
	(10280,10140,1,'Staff Discussion'),
	(10282,10141,1,'Staff / Author Discussion'),
	(10284,10142,1,'Your review has been successfully saved!'),
	(10286,10143,1,'Discussion'),
	(10292,10146,1,'Are you sure to submit your questions now?'),
	(10294,10147,1,'Your questions have not been answered yet.\nAre you sure you have completed the assignment and want to submit now?'),
	(10296,10148,1,'Sorry'),
	(10298,10149,1,'Team Content'),
	(10300,10150,1,'{0} has already been uploaded'),
	(10302,10151,1,'Fail to add attachment file {0}.'),
	(10304,10152,1,'Are you sure you don\'t want to specify the description for the attached file {0}?'),
	(10306,10153,1,'You MUST specify an attached file!'),
	(10308,10154,1,'The attachment file {0} will be deleted. Are you sure?'),
	(10310,10155,1,'ERROR: fail to delete the attachment!'),
	(10312,10156,1,'File upload successfully!.'),
	(10314,10157,1,'This file already exists.'),
	(10316,10158,1,'Invalid image type! The supported image type are: {0}'),
	(10318,10159,1,'You haven\'t specify an image!'),
	(10320,10160,1,'Current password cannot be empty!'),
	(10322,10161,1,'New password cannot be empty!'),
	(10324,10162,1,'Confirm password cannot be empty!'),
	(10326,10163,1,'New password and confirm password are inconsistent!'),
	(10328,10164,1,'Invalid password!'),
	(10330,10165,1,'Password changed successfully!'),
	(10332,10166,1,'Edit Deadline'),
	(10334,10167,1,'New Deadline'),
	(10336,10168,1,'End {0}\'s assignment \"{1}\" on \"{2}\".'),
	(10340,10170,1,'ACCEPT: Complete assignment and accept partial submission'),
	(10342,10171,1,'FORCE COMPLETE: Reject changes but describe assignment as complete'),
	(10344,10172,1,'End Assignment'),
	(10346,10173,1,'Are you sure?  This cannot be undone.'),
	(10348,10174,1,'You have selected '),
	(10350,10175,1,'Done'),
	(10352,10176,1,'Project'),
	(10354,10177,1,'Role'),
	(10356,10178,1,'Assigned User'),
	(10358,10179,1,'Case Object'),
	(10360,10180,1,'Case Body'),
	(10362,10181,1,'Action'),
	(10364,10182,1,'Fire All'),
	(10366,10183,1,'User Trigger List'),
	(10368,10184,1,'Description is empty!'),
	(10370,10185,1,'Project is not selected!'),
	(10372,10186,1,'Role is not selected!'),
	(10374,10187,1,'Assigned user is not selected!'),
	(10376,10188,1,'Case subject is empty!'),
	(10378,10189,1,'Case body is empty!'),
	(10382,10191,1,'Total:'),
	(10384,10192,1,'new:'),
	(10386,10193,1,'Too many characters! You can enter max 250 characters.'),
	(10388,10194,1,'The selected file {0} hasn\'t been added into the attachment list. Do you want to add it now?'),
	(10390,10195,1,'Save'),
	(10392,10196,1,'Save This Indicator'),
	(10394,10197,1,'Comments'),
	(10396,10198,1,'Sources'),
	(10398,10199,1,'PUBLISH'),
	(10400,10200,1,'PUBLISH THIS'),
	(10402,10201,1,'Add Comment'),
	(10404,10202,1,'adding ...'),
	(10406,10203,1,'From'),
	(10408,10204,1,'I\'m Done - Submit'),
	(10410,10205,1,'Only Selected'),
	(10412,10206,1,'Submit Indicators'),
	(10414,10207,1,'Scorecard Navigation'),
	(10416,10208,1,'Submit'),
	(10418,10209,1,'Review'),
	(10420,10210,1,'Reviews'),
	(10422,10211,1,'Next Category'),
	(10424,10212,1,'Please Wait...'),
	(10426,10213,1,'You Content'),
	(10428,10214,1,'Editing Disabled'),
	(10430,10215,1,'Target'),
	(10432,10216,1,'Rule Manager'),
	(10434,10217,1,'No'),
	(10436,10218,1,'Yes'),
	(10438,10219,1,'Please enter the subject'),
	(10440,10220,1,'TAGS'),
	(10442,10221,1,'Add User Trigger'),
	(10444,10222,1,'Previous Category'),
	(10446,10223,1,'Please specify the receiver'),
	(10448,10224,1,'To'),
	(10450,10225,1,'Cancel'),
	(10452,10226,1,'This indicator has not been answered!'),
	(10454,10227,1,'Message'),
	(10456,10228,1,'Please enter the message'),
	(10458,10229,1,'Next Indicator'),
	(10460,10230,1,'Send Message'),
	(10462,10231,1,'Project Updates'),
	(10464,10232,1,'Apply'),
	(10466,10233,1,'Original Answer'),
	(10468,10234,1,'Create Time'),
	(10470,10235,1,'Block Workflow'),
	(10472,10236,1,'Block Publishing'),
	(10474,10237,1,'Suggested Answer: (required)'),
	(10476,10238,1,'Previous Indicator'),
	(10478,10239,1,'Your Inbox'),
	(10480,10240,1,'Home'),
	(10482,10241,1,'Cases'),
	(10484,10242,1,'Fire'),
	(10486,10243,1,'Description'),
	(10488,10244,1,'File'),
	(10490,10245,1,'Reset'),
	(10492,10246,1,'Yes, I agree with the score but wish to add a comment, clarification, or suggest another reference.'),
	(10494,10247,1,'No, I do not agree with the score.'),
	(10496,10248,1,'I am not qualified to judge this indicator.'),
	(10498,10249,1,'Subject'),
	(10500,10250,1,'The Chart should be loaded here... Errors orrur if you see this text.'),
	(10502,10251,1,'I Have Questions'),
	(10504,10252,1,'User Name'),
	(10506,10253,1,'Preview'),
	(10508,10254,1,'Location'),
	(10510,10255,1,'Email Message Detail Level'),
	(10512,10256,1,'Mailing Address'),
	(10514,10257,1,'Review, Intended for Publication'),
	(10516,10258,1,'Last Name'),
	(10518,10259,1,'NOTE'),
	(10520,10260,1,'Bio'),
	(10522,10261,1,'First Name'),
	(10524,10262,1,'Copy Deep Link'),
	(10526,10263,1,'Set {0}\'s deadline for {1}'),
	(30000,30000,1,'Indaba Builder | All Content'),
	(30002,30001,1,'All Contents'),
	(30004,30002,1,'The Chart should be loaded here... Errors orrur if you see this text.'),
	(40000,40000,1,'Indaba Builder | Indicator Answer Detail'),
	(40002,40001,1,'Sources'),
	(50000,50000,1,'Indaba Builder | Assignment Message'),
	(60000,60000,1,'Attachments'),
	(60002,60001,1,'FILE'),
	(60004,60002,1,'SIZE'),
	(60006,60003,1,'BY'),
	(60008,60004,1,'MB'),
	(60010,60005,1,'KB'),
	(60012,60006,1,'B'),
	(60014,60007,1,'ADD NEW ATTACHMENTS'),
	(60016,60008,1,'Max file size'),
	(60018,60009,1,'10M'),
	(60020,60010,1,'File'),
	(70000,70000,1,'Tag'),
	(70002,70001,1,'Status'),
	(80000,80000,1,'Indaba Builder | New Case'),
	(80002,80001,1,'Indaba Builder | Cases Detail'),
	(80004,80002,1,'New Case'),
	(80006,80003,1,'Case'),
	(80008,80004,1,'Created By'),
	(80010,80005,1,'Case Title'),
	(80012,80006,1,'Case Description'),
	(80014,80007,1,'Case Status'),
	(80016,80008,1,'Open - New'),
	(80018,80009,1,'Assign To'),
	(80020,80010,1,'Attach Users'),
	(80022,80011,1,'Attach Content'),
	(80024,80012,1,'Attach Tags'),
	(80026,80013,1,'Attach File(s)'),
	(80028,80014,1,'Submit'),
	(90000,90000,1,'Indaba Builder | Cases'),
	(90002,90001,1,'Open New Case'),
	(90004,90002,1,'All Cases'),
	(90006,90003,1,'ATTACHED CONTENT'),
	(100000,100000,1,'Indaba Builder | Content Approval'),
	(100002,100001,1,'PLEASE APPROVE THE CONTENT AND CLICK [ I\'m Done - Submit ] WHEN YOU ARE DONE'),
	(100004,100002,1,'APPROVE THIS CONTENT'),
	(100006,100003,1,'I\'m Done - Submit'),
	(110000,110000,1,'Instructions'),
	(110002,110001,1,'Current Tasks'),
	(110004,110002,1,'NO.'),
	(110006,110003,1,'OPENED'),
	(110008,110004,1,'LAST UPDATED'),
	(120000,120000,1,'Indaba Builder | Content Payment'),
	(120002,120001,1,'PLEASE RECORD PAYMENT INFORMATION HERE. WHEN YOU FINISH, PLEASE CLICK [ I\'m Done - Submit ] BUTTON'),
	(120004,120002,1,'PAYMENT AMOUNT'),
	(120006,120003,1,'Please enter correct amount number'),
	(120008,120004,1,'PAYEES'),
	(120010,120005,1,'PAYMENT DATE'),
	(120012,120006,1,'I\'m Done - Submit'),
	(140000,140000,1,'adding ...'),
	(150000,150000,1,'Indaba Build | Error'),
	(150002,150001,1,'ERRORS'),
	(150004,150002,1,'Internal server error/exception occurs. Please contact your administrator.'),
	(150006,150003,1,'Back'),
	(160000,160000,1,'Browse Files'),
	(160002,160001,1,'Clear List'),
	(160004,160002,1,'Start Upload'),
	(170000,170000,1,'Apply'),
	(180000,180000,1,'Indaba Home'),
	(180002,180001,1,'All Content'),
	(180004,180002,1,'Queue'),
	(180006,180003,1,'People'),
	(180008,180004,1,'Messaging'),
	(180010,180005,1,'Help'),
	(180012,180006,1,'Logout'),
	(180014,180007,1,'Indaba Admin'),
	(190000,190000,1,'Indaba Builder | Help'),
	(190002,190001,1,'Are you having problems? We\'re very sorry! Here are some ways to get support.'),
	(190004,190002,1,'Read the instructions for your project at the <a href=\"http://getindaba.org/help-desk/\">Indaba Help Desk</a>.'),
	(190006,190003,1,'Read the Frequently Asked Questions at the <a href=\"http://getindaba.org/help-desk/\">Indaba Help Desk</a>.'),
	(190008,190004,1,'<a href=\"newmsg.do\">Send a sitemail</a> to your manager. Your managers like hearing from you! You can find your manager at the <a href=\"people.do\">PEOPLE</a> tab, under \"People You Should Know.\" Your manager will address your question and may post the exchange to the FAQ thread.'),
	(190010,190005,1,'We use Indaba\'s case manager to track problems with fieldwork or using Indaba. If a case is open and related to you, you can see it at the <a href=\"cases.do\">CASES</a> tab. Some users can open a new case, however not all users are allowed to do this.'),
	(190012,190006,1,'If you have an Indaba-related emergency, you can call the Global Integrity office at +1 202.449.4100.'),
	(190014,190007,1,'Workflow Console'),
	(190016,190008,1,'Rule Manager'),
	(200000,200000,1,'Indaba Builder | Horse Approval'),
	(200002,200001,1,'PLEASE APPROVE THE HORSE'),
	(200004,200002,1,'Approve - I\'m Done'),
	(220000,220000,1,'Tag The Indicator'),
	(220002,220001,1,'Add New Tag'),
	(230000,230000,1,'Comments'),
	(240000,240000,1,'Indaba Builder | Indicator Peer Review'),
	(240002,240001,1,'Link To Original Answer'),
	(240004,240002,1,'Submit Indicators'),
	(250000,250000,1,'Indaba Builder | Indicator PR Review'),
	(250002,250001,1,'Submit Indicators'),
	(260000,260000,1,'Indaba Builder | Indicator Review'),
	(260002,260001,1,'Submit Indicators'),
	(270000,270000,1,'Indaba Builder | Journal Content Version {0} by'),
	(270002,270001,1,'Content'),
	(280000,280000,1,'Indaba Builder | Notebook Overall Review'),
	(280002,280001,1,'I\'m Done - Submit'),
	(290000,290000,1,'Indaba Builder | Notebook Peer Review'),
	(290002,290001,1,'I\'m Done - Submit'),
	(300000,300000,1,'Indaba Builder | Notebook PR Review'),
	(300002,300001,1,'I\'m Done - Submit'),
	(310000,310000,1,'Indaba Builder | Notebook Review'),
	(310002,310001,1,'I\'m Done - Submit'),
	(320000,320000,1,'Indaba Builder | Notebook Review Page'),
	(320002,320001,1,'I\'m Done - Submit'),
	(330000,330000,1,'User name can\'t be empty!'),
	(330002,330001,1,'Password can\'t be empty!'),
	(330004,330002,1,'Please input user name'),
	(330006,330003,1,'Checking the validity of username now.'),
	(330008,330004,1,'Sending password to your email...'),
	(330010,330005,1,'ERROR when check validity!'),
	(330012,330006,1,'ERROR when send email!'),
	(330014,330007,1,'Indaba Login'),
	(330016,330008,1,'Password'),
	(340000,340000,1,'Indaba Builder | Message Detail'),
	(340002,340001,1,'Reply'),
	(340004,340002,1,'Title'),
	(340006,340003,1,'Message'),
	(350000,350000,1,'Indaba Builder | Messages'),
	(350002,350001,1,'Create New Message'),
	(350004,350002,1,'New'),
	(350006,350003,1,'Total'),
	(350008,350004,1,'FROM'),
	(350010,350005,1,'DATE'),
	(350012,350006,1,'NEW'),
	(350014,350007,1,'Rows'),
	(350016,350008,1,'Rows'),
	(360000,360000,1,'new'),
	(360002,360001,1,'from'),
	(360004,360002,1,'more'),
	(360006,360003,1,'more'),
	(370000,370000,1,'Your Open Cases'),
	(370002,370001,1,'ATTACHED CONTENT'),
	(380000,380000,1,'Indaba Builder | New Message'),
	(380002,380001,1,'New Message'),
	(380004,380002,1,'Post to Project Updates'),
	(380006,380003,1,'Send Message'),
	(390000,390000,1,'Indaba Builder | Create NoteBook'),
	(390002,390001,1,'Notification'),
	(390004,390002,1,'You have'),
	(390006,390003,1,'task(s) assigned.'),
	(390008,390004,1,'enter'),
	(390010,390005,1,'Message'),
	(400000,400000,1,'Indaba Builder | Notebook'),
	(410000,410000,1,'Indaba Builder | Notebook Edit'),
	(420000,420000,1,'Previous Versions'),
	(420002,420001,1,'Notebook Editor'),
	(420004,420002,1,'Words'),
	(420006,420003,1,'Cancel'),
	(430000,430000,1,'Edit This'),
	(430002,430001,1,'Notebook Content'),
	(440000,440000,1,'Peer Review, Intended for Publication'),
	(440002,440001,1,'Save'),
	(450000,450000,1,'Indaba Builder | People'),
	(450002,450001,1,'People You Should Know'),
	(450004,450002,1,'Your Teams'),
	(450006,450003,1,'All Users'),
	(450008,450004,1,'TARGET'),
	(450010,450005,1,'ASSIGNED CONTENT'),
	(460000,460000,1,'Team'),
	(460002,460001,1,'Case Association'),
	(460004,460002,1,'Assignment Status'),
	(460006,460003,1,'Overdue'),
	(470000,470000,1,'Indaba Builder | People Profile'),
	(470002,470001,1,'User'),
	(470004,470002,1,'Limited Bio'),
	(470006,470003,1,'Full Bio'),
	(470008,470004,1,'Phone'),
	(470010,470005,1,'Mobile Phone'),
	(470012,470006,1,'System Information'),
	(470014,470007,1,'Email'),
	(470016,470008,1,'Forward Inbox Message'),
	(470018,470009,1,'Alert Only'),
	(470020,470010,1,'Full Message'),
	(470022,470011,1,'Language'),
	(470024,470012,1,'Project(s)'),
	(470026,470013,1,'Role(s)'),
	(470028,470014,1,'Last Login Time'),
	(470030,470015,1,'Last Logout Time'),
	(470032,470016,1,'Inactive'),
	(470034,470017,1,'Active'),
	(470036,470018,1,'Deleted'),
	(470038,470019,1,'Send A Sitemail'),
	(470040,470020,1,'<b>About Your User Information</b><br/> This bio is visible to other Indaba users. All fields are optional. You must decide how much information you want to share with your colleagues. If you are working anonymously, do not submit any real information here!<br/><br/> Note to authors: The name you enter here will your exact byline for any work published with Indaba. You must use your pen name here if you are using one for your byline.<br/><br/> Indaba displays this bio information in two formats, depending on your role in the project. There is a LIMITED bio, and a FULL bio. Each project\'s managers can control who can see which version, or can prevent users from seeing anything at all (a blind review, for example). Indaba administrators will also have access to additional SYSTEM INFORMATION about you, including your email address. <br/><br/> <b>Limited Bio (for the public):</b>'),
	(470042,470021,1,'Full Bio (for Indaba users)'),
	(470044,470022,1,'System Information (for Indaba administrators)'),
	(470046,470023,1,'System Email'),
	(470048,470024,1,'The system will also record statistics about how often you use Indaba and share this with system administrators.'),
	(470050,470025,1,'Change Password'),
	(470052,470026,1,'Current Password'),
	(470054,470027,1,'New Password'),
	(470056,470028,1,'Confirm New Password'),
	(470058,470029,1,'Click to change people icon'),
	(470060,470030,1,'people icon'),
	(470062,470031,1,'Upload'),
	(470064,470032,1,'Assignments'),
	(470066,470033,1,'Open Cases'),
	(470068,470034,1,'ATTACHED CONTENT'),
	(480000,480000,1,'Reviews'),
	(490000,490000,1,'Indaba Builder | Queues'),
	(490002,490001,1,'No queues available for you at this time'),
	(490004,490002,1,'Average time to assign'),
	(490006,490003,1,'days'),
	(490008,490004,1,'Average time to completion'),
	(490010,490005,1,'CONTENT'),
	(490012,490006,1,'IN QUEUE'),
	(490014,490007,1,'ASSIGNMENT'),
	(490016,490008,1,'SET ASSIGNMENT'),
	(490018,490009,1,'SET PRIORITY'),
	(490020,490010,1,'CLAIM THIS'),
	(490022,490011,1,'Assigned To'),
	(490024,490012,1,'Not Assigned'),
	(490026,490013,1,'Low'),
	(490028,490014,1,'Medium'),
	(490030,490015,1,'High'),
	(490032,490016,1,'Update Assignment'),
	(490034,490017,1,'Return To Queue'),
	(490036,490018,1,'I Want This'),
	(500000,500000,1,'Indaba Builder | Reply Message'),
	(500002,500001,1,'Reply Message'),
	(500004,500002,1,'Send to Project Updates'),
	(500006,500003,1,'Send Message'),
	(510000,510000,1,'Indaba Builder | Rule Manager'),
	(510002,510001,1,'RULE FILES'),
	(510004,510002,1,'RULE COMPONENTS'),
	(510006,510003,1,'Path'),
	(510008,510004,1,'Save'),
	(520000,520000,1,'Indaba Builder | Scorecard'),
	(530000,530000,1,'INDICATOR TAGS'),
	(540000,540000,1,'(Read Only View)'),
	(540002,540001,1,'Scorecard Indicator Navigator'),
	(540004,540002,1,'Survey Review Question List'),
	(540006,540003,1,'Submit Questions'),
	(540008,540004,1,'Indicator Tags'),
	(550000,550000,1,'Survey Review Disagreement List'),
	(560000,560000,1,'Identify two or more of the following sources to support your score.'),
	(560002,560001,1,'Media Reports (...)'),
	(560004,560002,1,'Acadamic, ...'),
	(560006,560003,1,'Government studies'),
	(560008,560004,1,'International organization studies'),
	(560010,560005,1,'Interviews with government officials'),
	(560012,560006,1,'Interviews with acadamics'),
	(560014,560007,1,'Interviews with civil society'),
	(560016,560008,1,'Interviews with journalists'),
	(560018,560009,1,'Comments'),
	(570000,570000,1,'Identify and describe the name(s) and section(s) of the applicable law(s), statute(s), regulation(s), constitutional provision(s). Provide a web link to the source(s) if available.'),
	(570002,570001,1,'Comments'),
	(590000,590000,1,'Indaba Builder | Survey Answer'),
	(590002,590001,1,'Submit Indicators'),
	(600000,600000,1,'Indaba Builder | Scorecard Answer View'),
	(600002,600001,1,'Answer from'),
	(600004,600002,1,'Original Answer'),
	(610000,610000,1,'Next Indicator'),
	(630000,630000,1,'Next Indicator'),
	(640000,640000,1,'Indaba Builder | Survey Answer Original'),
	(640002,640001,1,'Task Name'),
	(640004,640002,1,'Comments'),
	(650000,650000,1,'Your Opinions'),
	(650002,650001,1,'Do you agree?'),
	(650004,650002,1,'Yes, I agree with the score and have no comments to add.'),
	(650006,650003,1,'Please avoid the use of \"see previous comment\" or \"see comment to indicator X\". <br/> <b>Reviewer\'s Comments:(required)</b><br/> <font color=\"red\"><b>DO NOT</b></font> enter scoring change or private comments to project managers here, just rationale and background. These comments <font color=\"red\"><b>will be published</b></font> by default.'),
	(650008,650004,1,'Please provide your suggested scoring change as well as comments supporting your change (supporting comments are required). These comments will be published in our final report. Avoid the use of \"see previous comment\" or \"see comment to indicator X.\" If you wish to send staff other feedback about this indicator that you do NOT want to be published, please use the separate Comments box below to submit that feedback.'),
	(650010,650005,1,'Suggested Answer: (required)'),
	(660000,660000,1,'OPINION'),
	(660002,660001,1,'YES, I agree with the score and have no comments to add.'),
	(660004,660002,1,'<p>Please avoid the use of \"see previous comment\" or \"see comment to indicator X\".</p> <p> <b>Reviewer\'s Comments:(required)</b><br/> <font color=\"red\"><b>DO NOT</b></font> enter scoring change or private comments to project managers here, just rationale and background. These comments <font color=\"red\"><b>will be published</b></font> by default. </p>'),
	(660006,660003,1,'<p>Please provide your suggested scoring change as well as comments supporting your change. Only YES/NO scores (for \"in law\" indicators) and 0, 25, 50, 75, or 100 scores (for \"in practice\" indicators) are allowed You do not need to mention the score change itself in the Comments box (we will see your suggested score change in the Suggested Score Change box); please use the Comments box only to provide commentary and narrative that supports the suggested score change. Avoid the use of \"see previous comment\" or \"see comment to indicator X\".</p> <p> <b>Reviewer\'s Comments:(required)</b><br/> <font color=\"red\"><b>DO NOT</b></font> enter scoring change or private comments to project managers here, just rationale and background. These comments <font color=\"red\"><b>will be published</b></font> by default. </p>'),
	(660008,660004,1,'Suggested Answer: (required)'),
	(670000,670000,1,'Reviews'),
	(680000,680000,1,'Reviews'),
	(690000,690000,1,'Add to Question List'),
	(690002,690001,1,'Question List'),
	(710000,710000,1,'Indaba Builder | Survey Overall Review'),
	(730000,730000,1,'Targets'),
	(730002,730001,1,'loading answer...'),
	(740000,740000,1,'Indaba User Finder Trigger'),
	(740002,740001,1,'View User Tirgger List'),
	(740004,740002,1,'No User Trigger Defined.'),
	(740006,740003,1,'Choose Project ...'),
	(740008,740004,1,'Choose Role ...'),
	(740010,740005,1,'Choose Assigned User ...'),
	(740012,740006,1,'Case Subject'),
	(740014,740007,1,'ERROR'),
	(760000,760000,1,'Indaba Builder | Workflow Console'),
	(760002,760001,1,'Workflows'),
	(760004,760002,1,'Run All'),
	(760006,760003,1,'ID'),
	(760008,760004,1,'DESCRIPTION'),
	(760010,760005,1,'CREATED AT'),
	(760012,760006,1,'CREATED BY'),
	(760014,760007,1,'DURATION'),
	(760016,760008,1,'Workflow Objects'),
	(760018,760009,1,'WORKFLOW - TARGET'),
	(760020,760010,1,'WORKFLOW - OBJECT'),
	(760022,760011,1,'START TIME'),
	(760024,760012,1,'Workflows / Goals / Tasks'),
	(760026,760013,1,'WORKFLOW - TARGET / OBJECT'),
	(760028,760014,1,'SEQUENCE'),
	(760030,760015,1,'TASK'),
	(760032,760016,1,'USER'),
	(770000,770000,1,'Indaba Builder | Your Content'),
	(770002,770001,1,'Sponsors'),
	(770004,770002,1,'Administrator Announcement'),
	(770006,770003,1,'Your Assignments'),
	(770008,770004,1,'New Deadline (YYYY-MM-DD)'),
	(770010,770005,1,'The Chart should be loaded here... Errors orrur if you see this text.'),
	(2150000,2150000,1,'Indaba Builder | Widgets'),
	(2150002,2150001,1,'client site page with widgets'),
	(2160000,2160000,1,'Indaba Builder | Manage Widget'),
	(2160002,2160001,1,'Widget Configuration Tool'),
	(2160004,2160002,1,'Configuration'),
	(2160006,2160003,1,'width'),
	(2160008,2160004,1,'height'),
	(2160010,2160005,1,'target address'),
	(2160012,2160006,1,'parameter'),
	(2160014,2160007,1,'mapping to'),
	(2160016,2160008,1,'Remove Selected'),
	(2160018,2160009,1,'Generate Code'),
	(2160020,2160010,1,'Preview'),
	(2170000,2170000,1,'Indaba Builder | Widget #1'),
	(2170002,2170001,1,'Please choose your language'),
	(2170004,2170002,1,'Copy Deep Link'),
	(2180000,2180000,1,'Indaba Builder | Widget #2'),
	(2180002,2180001,1,'Enter your name'),
	(2180004,2180002,1,'Choose your favorite ice cream flavor'),
	(2180006,2180003,1,'Chocolate'),
	(2180008,2180004,1,'Strawberry'),
	(2180010,2180005,1,'Vanilla'),
	(2180012,2180006,1,'Copy Deep Link'),
	(2190000,2190000,1,'Indaba Builder | Widget #3'),
	(2190002,2190001,1,'click links below'),
	(2190004,2190002,1,'Go to journal 4'),
	(2190005,10000,2,'Ouverture de session avortée'),
	(2190006,10001,2,'Le fichier joint a disparu de la mémoire de données. Veuillez contacter l\'administrateur du système!'),
	(2190007,10002,2,'Erreur du serveur interne / Il se produit une exclusion. Veuillez contacter votre administrateur  '),
	(2190008,10003,2,'Paramètre invalide: \"{0}\" n\'est pas reconnu !'),
	(2190009,10004,2,'Requête non determinée: \"{0}\" n\'est pas spécifié !'),
	(2190010,10005,2,'\"{0}\" : n\'existe pas {1}'),
	(2190011,10006,2,'Fichier au-delà de la limite maximale. Remarque: le volume maximal permis d\'un fichier est de 10M'),
	(2190012,10012,2,'Vous avez terminé la tâche assignée. Merci.'),
	(2190013,10013,2,'Notebook sauvegardé '),
	(2190014,10014,2,'En suspens'),
	(2190015,10015,2,'Non activé'),
	(2190016,10016,2,'Activé'),
	(2190017,10017,2,'je suis au courant '),
	(2190018,10019,2,'Commencé'),
	(2190019,10020,2,'Fait'),
	(2190020,10021,2,'STATUT'),
	(2190021,10022,2,'VISIONNER'),
	(2190022,10023,2,'ANTÉCÉDENT'),
	(2190023,10024,2,'OBJET'),
	(2190024,10025,2,'ÉVALUATION'),
	(2190025,10026,2,'% RÉALISÉ'),
	(2190026,10027,2,'À REMETTRE SOUS PEU'),
	(2190027,10028,2,'AFFAIRES OUVERTES'),
	(2190028,10029,2,'PERSONNES ASSIGNÉES'),
	(2190029,10030,2,'Cette case est actuellement vide'),
	(2190030,10031,2,'non commencé'),
	(2190031,10032,2,'terminé'),
	(2190032,10033,2,'travail en cours'),
	(2190033,10034,2,'Pris en compte'),
	(2190034,10035,2,'enregistré'),
	(2190035,10036,2,'% terminé'),
	(2190036,10037,2,'Étape suivante'),
	(2190037,10038,2,'\"{0}\" n\'est pas assigné'),
	(2190038,10039,2,'vue du contenu'),
	(2190039,10040,2,'tableau historique'),
	(2190040,10041,2,'Edition'),
	(2190041,10042,2,'Produit'),
	(2190042,10043,2,'Exclusion selectionnée'),
	(2190043,10044,2,'Inclusion selectionnée'),
	(2190044,10045,2,'Exigible'),
	(2190045,10046,2,'Non exigible'),
	(2190046,10047,2,'Selectionner le tout'),
	(2190047,10048,2,'Ajout'),
	(2190048,10049,2,'Réponse'),
	(2190049,10050,2,'AFFAIRE'),
	(2190050,10051,2,'TITRE'),
	(2190051,10052,2,'PRIORITÉ'),
	(2190052,10053,2,'AUTEUR'),
	(2190053,10054,2,'TEXTE EN PIÈCE JOINTE'),
	(2190054,10055,2,'NOM'),
	(2190055,10057,2,'PAYS'),
	(2190056,10058,2,'ÉQUIPES'),
	(2190057,10059,2,'TEXTE ASSIGNÉ'),
	(2190058,10060,2,'Ajout de note faite avec succès'),
	(2190059,10061,2,'Ajout de note {0} non effectué'),
	(2190060,10062,2,'Création de cas effectué avec succès'),
	(2190061,10063,2,'Ouverture de cas non effectuée'),
	(2190062,10064,2,'SUCCÈS'),
	(2190063,10065,2,'ÉCHEC'),
	(2190064,10067,2,'Changement de code d\'accès exécuté avec succés'),
	(2190065,10068,2,'Changement du code d\'accès non effectué'),
	(2190066,10069,2,'Anonyme'),
	(2190067,10070,2,'Collaborateur'),
	(2190068,10071,2,'Pas de projet'),
	(2190069,10072,2,'Nom d\'utilisateur non valide'),
	(2190070,10073,2,'Validité du nom de l\'utilisateur '),
	(2190071,10074,2,'Paramètres non valides'),
	(2190072,10075,2,'Merci pour votre réponse !'),
	(2190073,10076,2,'Cette version n\'accepte que l\'anglais !'),
	(2190074,10077,2,'Impossible de trouver la question du sondage !'),
	(2190075,10078,2,'Impossible d\'obtenir le nom du pays'),
	(2190076,10079,2,'Affaire mise à jour avec succès'),
	(2190077,10080,2,'Mise à jour de l\'affaire non réalisée'),
	(2190078,10081,2,'DÉLAI DE REMISE'),
	(2190079,10082,2,'Veuillez donner votre opinion !'),
	(2190080,10083,2,'Information'),
	(2190081,10084,2,'Erreur  '),
	(2190082,10085,2,'Confirmation'),
	(2190083,10086,2,'Avertissement'),
	(2190084,10087,2,'Échec de remise de votre article due à une erreur interne !'),
	(2190085,10088,2,'sauvegardé'),
	(2190086,10089,2,'Veuillez citer la source de votre information ! '),
	(2190087,10090,2,'Veuillez selectionner une source d\'information !'),
	(2190088,10091,2,'Veuillez choisir au moins une source ! '),
	(2190089,10092,2,'Veuillez citer la description de votre source d\'information '),
	(2190090,10093,2,'Veuillez choisir une option de qualification ! '),
	(2190091,10094,2,'Veuillez introduire votre réponse'),
	(2190092,10095,2,'Veuillez apporter un changement dans la qualification suggérée et  apportez les raisons qui motivent votre changement ! '),
	(2190093,10096,2,'Veuillez introduire vos commentaires'),
	(2190094,10097,2,'Erreur du serveur interne. Veuillez contacter votre administrateur ! '),
	(2190095,10098,2,'Il vous reste {0} réponses qui n\'ont pas été révisées'),
	(2190096,10099,2,'Êtes-vous sûr d\'avoir terminé le travail assigné et voulez-vous le remettre maintenant ? '),
	(2190097,10100,2,'Il existe des questions restées sans réponse de la part de l\'auteur. Êtes-vous sûr d\'avoir terminé le travail assigné et voulez-vous le remettre maintenant ? '),
	(2190098,10101,2,'Le texte actuel est  <span style=\'font-weight: bold; color: orange;\'>{0}</span> words long. Ceci excède le comptage maximum de mots <span style=\'font-weight: bold; color: orange;\'>{1}</span>. Votre travail n\'a pas été reçu. Veuillex revoir le texte et remttre à nouveau votre travail. '),
	(2190099,10102,2,'Ce texte actuel est <span style=\'font-weight: bold; color: orange;\'>{0}</span> words long.  Ceci est inférieur au comptage minimum de  <span style=\'font-weight: bold; color: orange;\'>{0}</span> words long. Votre travail n\'a pas été reçu. Veuillez revoir le texte renvoyer votre travail.  '),
	(2190100,10104,2,'Veuillez introduire le titre du sujet traité !'),
	(2190101,10105,2,'Notes de l\'utilisateur'),
	(2190102,10106,2,'Notes du personnel'),
	(2190103,10107,2,'Visionner'),
	(2190104,10109,2,'Cliquez pour débloquer'),
	(2190105,10110,2,'Source'),
	(2190106,10111,2,'Coller le texte'),
	(2190107,10112,2,'Coller le texte à partir de Word'),
	(2190108,10113,2,'Imprimer'),
	(2190109,10114,2,'Vérifier l\'orthographe'),
	(2190110,10115,2,'Scayt'),
	(2190111,10116,2,'Annuler'),
	(2190112,10117,2,'Refaire'),
	(2190113,10118,2,'Chercher'),
	(2190114,10119,2,'Remplacer'),
	(2190115,10120,2,'Sélectionner tout le texte'),
	(2190116,10121,2,'Éliminer le format'),
	(2190117,10122,2,'en caractère gras'),
	(2190118,10123,2,'en italique'),
	(2190119,10124,2,'Souligner le mot'),
	(2190120,10125,2,'Effacer'),
	(2190121,10126,2,'Liste numérotée'),
	(2190122,10127,2,'Liste avec gros points de mise en évidence'),
	(2190123,10128,2,'Relier'),
	(2190124,10129,2,'Défaire le lien'),
	(2190125,10130,2,'Ancrage'),
	(2190126,10131,2,'Format'),
	(2190127,10132,2,'Couleur BG'),
	(2190128,10133,2,'Maximiser'),
	(2190129,10134,2,'Ma barre d\'outils'),
	(2190130,10135,2,'Veuillez introduire votre texte amélioré'),
	(2190131,10136,2,'DURÉE'),
	(2190132,10137,2,'TITRE DU SUJET TRAITÉ'),
	(2190133,10138,2,'Les améliorations apportées ont été sauvegardées'),
	(2190134,10139,2,'Avec succès'),
	(2190135,10140,2,'Discussion entamée par le personnel'),
	(2190136,10141,2,'Discussion entamée par le personnel / par l\'auteur '),
	(2190137,10142,2,'Votre révision a été sauvegardée'),
	(2190138,10143,2,'Discussion'),
	(2190139,10146,2,'Êtes-vous certain de vouloir envoyer vos questions maintenant ? '),
	(2190140,10147,2,'Vos questions n\'ont pas encore reçu de réponse. Êtes-vous  d\'avoir terminé le travail assigné et voulez-vous l\'envoyer maintenant ? '),
	(2190141,10148,2,'Désolé'),
	(2190142,10149,2,'Contenu du texte de l\'équipe'),
	(2190143,10150,2,'{0} a déjà été téléchargé en amont '),
	(2190144,10151,2,'Fichier en pièce jointe non envoyé {0}'),
	(2190145,10152,2,'Êtes-vous sûr de ne pas vouloir spécifier la description du ficher envoyé en pièce jointe ?'),
	(2190146,10153,2,'Il est obligatoire de spécifier un fichier envoyé en pièce jointe !'),
	(2190147,10154,2,'Le fichier en pièce jointe {0} sera supprimé. Voulez-vous continuer ?'),
	(2190148,10155,2,'Erreur: Fichier en pièce jointe non supprimé'),
	(2190149,10156,2,'Fichier téléchargé avec succès !.'),
	(2190150,10157,2,'Ce fichier existe déjà'),
	(2190151,10158,2,'Type d\'illustration non valide ! Le type d\'illustration complémentaire est: {0}'),
	(2190152,10159,2,'Vous avez omis de spécifier une illustration'),
	(2190153,10160,2,'La case du code d\'accès actuel doit être obligatoirement remplie'),
	(2190154,10161,2,'La case du nouveau code d\'accès doit être obligatoirement remplie'),
	(2190155,10162,2,'La case de confirmation du code d\'accès doit être obligatoirement remplie !'),
	(2190156,10163,2,'Le nouveau code d\'accès et sa confirmation ne coïncident pas'),
	(2190157,10164,2,'Code d\'accès non valide'),
	(2190158,10165,2,'Changement du code d\'accès réalisé avec succès'),
	(2190159,10166,2,'Délai de remise de l\'édition'),
	(2190160,10167,2,'Nouveau délai'),
	(2190161,10168,2,'Terminez l\'assignation de {0} \"{1}\" sur \"{2}\"'),
	(2190162,10170,2,'ACCEPTATION: Terminez l\'assignation du travail et acceptez des remises partielles du travail. '),
	(2190163,10171,2,'EXIGENCE DE TERMINAISON FORCÉE : Refuse les changements mais considère le travail assigné comme terminé'),
	(2190164,10172,2,'Fin du travail assigné'),
	(2190165,10173,2,'Êtes-vous sûr de vouloir exécuter cette action. Cette action est irréversible'),
	(2190166,10174,2,'Vous avez sélectionné'),
	(2190167,10175,2,'Réalisé'),
	(2190168,10176,2,'Projet'),
	(2190169,10177,2,'Rôle'),
	(2190170,10178,2,'Utilisateur assigné'),
	(2190171,10179,2,'Objet de l\'affaire'),
	(2190172,10180,2,'Corps de l\'affaire'),
	(2190173,10181,2,'Action'),
	(2190174,10182,2,'Commande donnant le feu vert à l\'assignation d\'un projet'),
	(2190175,10183,2,'Liste d\'activation de l\'utilisateur'),
	(2190176,10184,2,'La case de description n\'a pas été remplie !'),
	(2190177,10185,2,'Le projet n\'a pas été sélectionné !'),
	(2190178,10186,2,'Le rôle n\'a pas été selectionné !'),
	(2190179,10187,2,'l\'utilisateur assigné n\'a pas été sélectionné !'),
	(2190180,10188,2,'Le sujet de l\'affaire n\'est pas indiqué'),
	(2190181,10189,2,'Le corps de l\'affaire n\'est pas indiqué'),
	(2190182,10191,2,'Total:'),
	(2190183,10192,2,'nouveau:'),
	(2190184,10193,2,'Trop de caractères. Vous ne pouvez introduire qu\'un maximum de 250 caractères.'),
	(2190185,10194,2,'Le fichier sélectionné {0} n\'a pas été annexé à la liste de pièces jointes. Désirez-vous l\'ajouter maintenant'),
	(2190186,10195,2,'Sauvegarder'),
	(2190187,10196,2,'Sauvegarder cet indicateur '),
	(2190188,10197,2,'Commentaires'),
	(2190189,10198,2,'Sources'),
	(2190190,10199,2,'PUBLIER'),
	(2190191,10200,2,'PUBLIER CECI'),
	(2190192,10201,2,'Ajoutez un commenatire'),
	(2190193,10202,2,'en ajoutant '),
	(2190194,10203,2,'À partir de'),
	(2190195,10204,2,'J\'ai terminé - Je remets le travail'),
	(2190196,10205,2,'Uniquement sélectionné'),
	(2190197,10206,2,'Remettre des indicateurs'),
	(2190198,10207,2,'Outil de révision de sondages faisant partie du système'),
	(2190199,10208,2,'Remise de travail'),
	(2190200,10209,2,'Réviser'),
	(2190201,10210,2,'Révisions'),
	(2190202,10211,2,'Catégorie suivante'),
	(2190203,10212,2,'Veuillez attendre, spv.'),
	(2190204,10213,2,'Votre contenu'),
	(2190205,10214,2,'Édition non activée'),
	(2190206,10215,2,'Cible'),
	(2190207,10216,2,'Chef de règlement'),
	(2190208,10217,2,'Non'),
	(2190209,10218,2,'Oui'),
	(2190210,10219,2,'Veuillez introduire le thème du travail'),
	(2190211,10220,2,'ÉTIQUETTES'),
	(2190212,10221,2,'Ajouter la zone d\'activation de l\'utilisateur '),
	(2190213,10222,2,'Catégorie antérieure'),
	(2190214,10223,2,'Veuillez indiquer le nom du recepteur'),
	(2190215,10224,2,'à l\'attention de'),
	(2190216,10225,2,'Éliminer '),
	(2190217,10226,2,'L\'indicateur n\'a pas été indiqué !'),
	(2190218,10227,2,'Message'),
	(2190219,10228,2,'Veuillez introduire le message'),
	(2190220,10229,2,'Indicateur suivant'),
	(2190221,10230,2,'Envoyez le message'),
	(2190222,10231,2,'Mises à jour du projet'),
	(2190223,10232,2,'Appliquer'),
	(2190224,10233,2,'Réponse iniciale'),
	(2190225,10234,2,'Création d\'horaires'),
	(2190226,10235,2,'Arrêt du déroulement du travail '),
	(2190227,10236,2,'Arrêt de la publication'),
	(2190228,10237,2,'Réponse suggérée ( obligatoire)'),
	(2190229,10238,2,'Indicateur antérieur'),
	(2190230,10239,2,'Votre corbeille d\'arrivée'),
	(2190231,10240,2,'Page d\'accueil'),
	(2190232,10241,2,'Affaires '),
	(2190233,10242,2,'Cibler'),
	(2190234,10243,2,'Description'),
	(2190235,10244,2,'Fichier'),
	(2190236,10245,2,'Réinitialisation'),
	(2190237,10246,2,'Je suis d\'accord avec la qualification mais désire ajouter un commentaire, faire des éclaircissements ou suggérer une autre référence'),
	(2190238,10247,2,'Non, je ne suis pas d\'accord avec la qualification '),
	(2190239,10248,2,'Je ne suis pas qualifié pour émettre une opinion sur cet indicateur'),
	(2190240,10249,2,'Sujet'),
	(2190241,10250,2,'Le graphique devrait être téléchargé ici Signalement d\' erreur dès l\'apparition de ce texte.'),
	(2190242,10251,2,'J\'ai des questions à poser'),
	(2190243,10252,2,'Nom de l\'utilisateur'),
	(2190244,10253,2,'Prévisualisation '),
	(2190245,10254,2,'Positionnement'),
	(2190246,10255,2,'Niveau de détails du courriel'),
	(2190247,10256,2,'Adresse de la messagerie électronique'),
	(2190248,10257,2,'Révision, destinée à la publication'),
	(2190249,10258,2,'Nom de famille'),
	(2190250,10259,2,'REMARQUE'),
	(2190251,10260,2,'Bio'),
	(2190252,10261,2,'Prénom'),
	(2190253,10262,2,'Copier la liaison '),
	(2190254,10263,2,'Établir un délai pour {0} relatif à {1}'),
	(2190255,30000,2,'Système d\'Indaba  | Tout contenu '),
	(2190256,30001,2,'Tous les contenus'),
	(2190257,40000,2,'Système d\'Indaba  | Indicateur de détail de réponse'),
	(2190258,50000,2,'Système d\'Indaba  | Assignation de message'),
	(2190259,60000,2,'Adjonction de fichier '),
	(2190260,60001,2,'FICHIER'),
	(2190261,60002,2,'VOLUME'),
	(2190262,60003,2,'PAR'),
	(2190263,60004,2,'MB'),
	(2190264,60005,2,'KB'),
	(2190265,60006,2,'B'),
	(2190266,60007,2,'ADJONCTION D\' UN AUTRE FICHIER '),
	(2190267,60008,2,'Volume maximum du fichier'),
	(2190268,60009,2,'10M'),
	(2190269,70000,2,'Étiquette'),
	(2190270,80000,2,'Système d\'Indaba   | Nouvelle affaire'),
	(2190271,80001,2,'Systéme d\' Indaba  | Détail des affaires '),
	(2190272,80002,2,'Nouvelle affaire'),
	(2190273,80003,2,'Affaire'),
	(2190274,80004,2,'Créé par'),
	(2190275,80005,2,'Titre de l\'Affaire'),
	(2190276,80006,2,'Description de l\'Affaire'),
	(2190277,80007,2,'Statut de l\'Affaire'),
	(2190278,80008,2,'Ouverture d\'une nouvelle affaire'),
	(2190279,80009,2,'Assigné à'),
	(2190280,80010,2,'Adjonction des utilisateurs'),
	(2190281,80011,2,'Adjonction du contenu'),
	(2190282,80012,2,'Adjonction d\'étiquettes'),
	(2190283,80013,2,'Adjonction de fichier(s)'),
	(2190284,90000,2,'Système d\' Indaba  | Affaires'),
	(2190285,90001,2,'Ouverture d\'une nouvelle affaire'),
	(2190286,90002,2,'Toutes les affaires'),
	(2190287,100000,2,'Système d\' Indaba  | autorisation du contenu '),
	(2190288,100001,2,'VEUILLEZ AUTORISER LE CONTENU ET CLIQUEZ SUR [ J\'ai terminé - Envoyer] lorsque le travail est terminé'),
	(2190289,100002,2,'AUTORISATION DE CE CONTENU'),
	(2190290,110000,2,'Directives'),
	(2190291,110001,2,'Tâches actuelles'),
	(2190292,110002,2,'NO.'),
	(2190293,110003,2,'OUVERT'),
	(2190294,110004,2,'DERNIÈRE MISE À JOUR'),
	(2190295,120000,2,'Numération de Indaba  | Paiement du contenu'),
	(2190296,120001,2,'VEUILLEZ ENREGISTRER ICI L\'INFORMATION DU PAIEMENT. UNE FOIS EFFECTUÉ, CLIQUEZ ICI SUR LA TOUCHE  [ J\'ai terminé - Envoyer]'),
	(2190297,120002,2,'MONTANT DU PAIEMENT'),
	(2190298,120003,2,'Veuillez introduire le chiffre correct du montant '),
	(2190299,120004,2,'BÉNÉFICIAIRES'),
	(2190300,120005,2,'DATE DE PAIEMENT '),
	(2190301,150000,2,'Système d\' Indaba  | Erreur'),
	(2190302,150001,2,'ERREURS'),
	(2190303,150002,2,'Existence d\'une erreur du serveur interne. Veuillez contacter votre administrateur.'),
	(2190304,150003,2,'Retour'),
	(2190305,160000,2,'Explorer les fichiers'),
	(2190306,160001,2,'Liste d\'effacement'),
	(2190307,160002,2,'Commencer à télécharger'),
	(2190308,180000,2,'Page d\'accueil d\'Indaba'),
	(2190309,180001,2,'Tous les contenus'),
	(2190310,180002,2,'File d\'attente'),
	(2190311,180003,2,'Personnes '),
	(2190312,180004,2,'Messagerie'),
	(2190313,180005,2,'Assistance'),
	(2190314,180006,2,'Sortie'),
	(2190315,180007,2,'Administration d\'Indaba'),
	(2190316,190000,2,'Système d\'Indaba  | Assistance'),
	(2190317,190001,2,'Avez-vous des problèmes ? Nous sommes vraiment navrés ! Voici quelques directives pour obtenir de l\'aide.'),
	(2190318,190002,2,'Lisez les directives concernant votre projet sur <a href=\"http://getindaba.org/help-desk/\">Indaba Help Desk</a>.'),
	(2190319,190003,2,'Lisez les questions frequemment posées sur le site  <a href=\"http://getindaba.org/help-desk/\">Indaba Help Desk</a>.'),
	(2190320,190004,2,'<a href=\"newmsg.do\">Envoyer un message de site </a> à votre directeur. C\'est pour eux un plaisir de recevoir de vos nouvelles !. Contactez-les sur <a href=\"people.do\">PEOPLE</a> tab, sous la rubrique \"People You Should Know.\"  Votre directeur affichera l\'échange d\'idées sur le regroupement des messages d\'un groupe d\'intérêt sur le sujet concerné.'),
	(2190321,190005,2,'Nous utilisons le système de gestion d\'Indaba pour retrouver et suivre les problèmes rencontrés lors des travaux sur les lieux ou lors de l\'utilisation d\'Indaba. Si une affaire déjà traitée est liée à la votre, vous pouvez consulter le dossier sur <a href=\"cases.do\">CASES</a> tab. Certains utilisateurs peuvent ouvrir une nouvelle affaire. Toutefois l\'autorisation n\'est pas accordée à tous. '),
	(2190322,190006,2,'Dans le cas où vous rencontreriez un problème urgent lié à Indaba, vous pouvez appeler le bureau d\'Indaba au numéro  +1 202.449.4100.'),
	(2190323,190007,2,'Pupitre de flux de travail'),
	(2190324,200000,2,'Système d\'Indaba  | Approbation des négotiations '),
	(2190325,200001,2,'VEUILLEZ AUTORISER LES NÉGOTIATIONS'),
	(2190326,200002,2,'Approbation - j\'ai terminé'),
	(2190327,220000,2,'Étiqueter l\'indicateur'),
	(2190328,220001,2,'Ajouter une nouvelle étiquette'),
	(2190329,240000,2,'Numération d\'Indaba  | Révision des indicateurs semblables '),
	(2190330,240001,2,'Lien à la réponse initiale'),
	(2190331,250000,2,'Système d\'Indaba  | Révision de l\'indicateur PR'),
	(2190332,260000,2,'Système d\'Indaba  | Révision de l\'indicateur '),
	(2190333,270000,2,'Système d\'Indaba  | Version {0} du contenu du journal par'),
	(2190334,270001,2,'Contenu '),
	(2190335,280000,2,'Système d\'Indaba  | Révision générale du bloc-notes'),
	(2190336,290000,2,'Système d\'Indaba  | Révision du bloc-notes du collègue'),
	(2190337,300000,2,'Système d\'Indaba  | Révision du bloc-notes PR'),
	(2190338,310000,2,'Système d\'Indaba  | Révision du bloc-notes'),
	(2190339,320000,2,'Système d\'Indaba  | Page révisée du bloc-notes'),
	(2190340,330000,2,'La case destinée au nom de l\'utilisteur doit être obligatoirement remplie'),
	(2190341,330001,2,'La case du code d\'accès doit être obligatoirement remplie'),
	(2190342,330002,2,'Veuillez introduire le nom de l\'utilisateur'),
	(2190343,330003,2,'Vérification du nom de l\'utilisateur en cours'),
	(2190344,330004,2,'Envoi de votre code d\'accès à votre courrier électronique '),
	(2190345,330005,2,'Vérification de validité avortée'),
	(2190346,330006,2,'Envoi du courriel avorté'),
	(2190347,330007,2,'Connection à Indaba'),
	(2190348,330008,2,'Code d\'accès '),
	(2190349,340000,2,'Système d\'Indaba  | Détail du message'),
	(2190350,340001,2,'Réponse'),
	(2190351,340002,2,'Titre'),
	(2190352,350000,2,'Système d\'Indaba  | Message '),
	(2190353,350001,2,'Création d\'un nouveau message'),
	(2190354,350002,2,'Nouveau'),
	(2190355,350003,2,'Total:'),
	(2190356,350004,2,'DE LA PART DE'),
	(2190357,350005,2,'DATE  '),
	(2190358,350006,2,'NOUVEAU'),
	(2190359,350007,2,'RANGÉES'),
	(2190360,360000,2,'nouveau'),
	(2190361,360001,2,'de la part de'),
	(2190362,360002,2,'en plus'),
	(2190363,370000,2,'Vos affaires en cours'),
	(2190364,380000,2,'Système d\'Indaba  | Nouveau message '),
	(2190365,380001,2,'Nouveau message'),
	(2190366,380002,2,'Affichage de mise à jour de projet'),
	(2190367,390000,2,'Système d\'Indaba  | Création de bloc-notes '),
	(2190368,390001,2,'Notification'),
	(2190369,390002,2,'Vous avez'),
	(2190370,390003,2,'assignation de tâche(s)'),
	(2190371,390004,2,'Touche d\'entrée'),
	(2190372,400000,2,'Système d\'Indaba  | Bloc-notes'),
	(2190373,410000,2,'Systéme d\'Indaba  | Édition du bloc-notes'),
	(2190374,420000,2,'Versions antérieures'),
	(2190375,420001,2,'Mise en forme du bloc-notes'),
	(2190376,420002,2,'Mots'),
	(2190377,430000,2,'Mettre en forme ces données'),
	(2190378,430001,2,'Coontenu du bloc-notes'),
	(2190379,440000,2,'Révision du collégue, destinée à la publication'),
	(2190380,450000,2,'Système d\'Indaba  | Personnes'),
	(2190381,450001,2,'Personnes que vous devriez connaître'),
	(2190382,450002,2,'Vos Équipes'),
	(2190383,450003,2,'Tous les utilisateurs'),
	(2190384,450004,2,'CIBLE'),
	(2190385,460000,2,'Équipe'),
	(2190386,460001,2,'Association concernant l\'affaire'),
	(2190387,460002,2,'Statut de l\'assignation'),
	(2190388,470000,2,'Numération d\'Indaba  | Profiles des personnes'),
	(2190389,470001,2,'Utilisateur '),
	(2190390,470002,2,'Biographie resumée'),
	(2190391,470003,2,'Biographie complète'),
	(2190392,470004,2,'Téléphone'),
	(2190393,470005,2,'Téléphone portable'),
	(2190394,470006,2,'Information sur le système'),
	(2190395,470007,2,'Courrier électronique'),
	(2190396,470008,2,'Retransmettre message de la corbeille d\'arrivée'),
	(2190397,470009,2,'uniquement Alerte'),
	(2190398,470010,2,'Message complet'),
	(2190399,470011,2,'Langue'),
	(2190400,470012,2,'Projets'),
	(2190401,470013,2,'Rôle(s)'),
	(2190402,470014,2,'Heure de la dernière ouverture de la session'),
	(2190403,470015,2,'Heure de la dernière clôture de la session '),
	(2190404,470016,2,'Inactif'),
	(2190405,470017,2,'Actif'),
	(2190406,470018,2,'Éffacé'),
	(2190407,470019,2,'Envoyer au site de messagerie A'),
	(2190408,470020,2,'<b> Concernant vos informations d\'utilisateur, </b><br/> Cette biographie peut être consultée par d\'autres utilisateurs d\'Indaba. Toutes les zones ou champs sont optionnels. La décision de partager vos informations avec vos collègues n\'appartient qu\'à vous. Si vous travaillez dans l\'anonymat, ne transmttez pas d\'informations sur ce site <br/><br/> ! Conseil donné aux auteurs: Le nom indiqué par vous portera votre signature pour tous vos travaux publiés à travers Indaba. Faites usage de votre nom de plume our toutes vos publications <br/><br/> si vous en avez créé un. Indaba affiche cette biographie en deux formats selon le rôle que vous jouez au coeur du projet. '),
	(2190409,470021,2,'Biographie complète (pour utilisateurs d\'Indaba)'),
	(2190410,470022,2,'Informations obtenues du système pour l\'usage des administrateurs d\'Indaba.'),
	(2190411,470023,2,'Courrier électronique du système'),
	(2190412,470024,2,'Le système établira également des statistiques sur la fréquence d\'usage d\'Indaba et les administrateurs du système auront accès à cette information. '),
	(2190413,470025,2,'Changez votre code d\'accès'),
	(2190414,470026,2,'Code d\'accès actuel'),
	(2190415,470027,2,'Nouveau code d\'accès'),
	(2190416,470028,2,'Confirmation du nouveau code d\'accès'),
	(2190417,470029,2,'Cliquez pour changer l\'icône de la personne '),
	(2190418,470030,2,'Icône de la personne'),
	(2190419,470031,2,'Téléchargez'),
	(2190420,470032,2,'Assignation de tâches'),
	(2190421,470033,2,'Ouverture de recherches sur des affaires'),
	(2190422,490001,2,'Pour le moment, pas de disponibilité pour vous dans la file d\'attente'),
	(2190423,490002,2,'Durée moyenne pour obtenir une assignation '),
	(2190424,490003,2,'jours'),
	(2190425,490004,2,'Durée moyenne jusqu\'à la terminaison '),
	(2190426,490005,2,'CONTENU'),
	(2190427,490006,2,'En attente'),
	(2190428,490007,2,'ASSIGNATION'),
	(2190429,490008,2,'ÉTABLIR UNE ASSIGNATION'),
	(2190430,490009,2,'ÉTABLIR UNE PRIORITÉ'),
	(2190431,490010,2,'APPROPRIATION DE CE TRAVAIL'),
	(2190432,490011,2,'Assigné à'),
	(2190433,490012,2,'Non assigné'),
	(2190434,490013,2,'Attente de courte durée'),
	(2190435,490014,2,'Attente de durée moyenne'),
	(2190436,490015,2,'Attente de longue durée'),
	(2190437,490016,2,'Mise à jour de l\'assignation '),
	(2190438,490017,2,'Retour dans la file d\'attente'),
	(2190439,490018,2,'Je souhaiterais faire ce travail'),
	(2190440,500000,2,'Numération d\'Indaba  | Message de réponse'),
	(2190441,500001,2,'Message de réponse'),
	(2190442,500002,2,'Envoyer aux mises à jour du projet'),
	(2190443,510000,2,'Numération d\'Indaba  | Directeur de règlementation'),
	(2190444,510001,2,'FICHIER CONCERNANT LES RÈGLEMENTS'),
	(2190445,510002,2,'ÉLÉMENTS DU RÈGLEMENT'),
	(2190446,510003,2,'Voie d\'accès'),
	(2190447,520000,2,'Numération d\'Indaba  | Outil de révision d\'un sondage faisant partie du système'),
	(2190448,530000,2,'ÉTIQUETTES D\'INDICATEUR'),
	(2190449,540000,2,'Visualisation à lecture seulement '),
	(2190450,540001,2,'Navigateur pour indicateur d\'outil de révision de sondage'),
	(2190451,540002,2,'Liste de questions servant à la révision de sondage'),
	(2190452,540003,2,'Présentation de questions'),
	(2190453,540004,2,'Étiquettes d\'indicateur '),
	(2190454,550000,2,'Liste de conflits d\'opinion concernant la révision du sondage'),
	(2190455,560000,2,'Identifier deux ou plusieurs parmi les sources énoncées ci-dessous pour soutenir votre opinion sur le sondage'),
	(2190456,560001,2,'Rapports des media ()'),
	(2190457,560002,2,'Rapport académique'),
	(2190458,560003,2,'Études réalisées par le gouvernement'),
	(2190459,560004,2,'Études réalisées par des organisations internationales'),
	(2190460,560005,2,'Entretiens avec des fonctionnaires du gouvernement '),
	(2190461,560006,2,'Entretiens avec des universitaires'),
	(2190462,560007,2,'Entretiens auprés de la société civile'),
	(2190463,560008,2,'Entretiens avec des journalistes'),
	(2190464,570000,2,'Identifiez et décrivez le ou les noms et sections de la loi ( des lois) applicables, des statuts, des règlements et des dispositions inscrites dans la constitution. Fournir un lien de la Web pour avoir accès aux sources d\'informations, le cas échéant.'),
	(2190465,600000,2,'Numération d\'Indaba  | Visualisation des réponses au moyen de l\'outil de évision du sondage '),
	(2190466,600001,2,'Réponse de la part de'),
	(2190467,640000,2,'Réponse initiale du sondage'),
	(2190468,640001,2,'Nom de la tâche'),
	(2190469,650000,2,'Votre opinion'),
	(2190470,650001,2,'Êtes-vous d\'accord ?'),
	(2190471,650002,2,'Oui, je suis d\'accord avec l\'outil d\'évaluation et je n\'ai pas de commentaires à ajouter'),
	(2190472,650003,2,'Veuillez éviter l\'usage de l\'expression \"se reporter au commentaire antérieur\" ou \" voir commentaire concernant l\'indicateur X\". <br/> <b>Commentaires de celui qui révise: (obligatoires) </b><br/>'),
	(2190473,650004,2,'Veuillez indiquer les raisons de votre changement de qualification ainsi que les commentaires qui le justifient ( ces justifications sont obligatoires). Evitez l\'usage de l\'expression \"se reproter au commentaire antérieur\" ou \" voir commentaire su sujet de l\'indicateur X\". Si vous envoyez au personnel un autre retour d\'information concernant cet indicateur et ne souhaitez PAS qu\'il soit publié, utilisez une case de commentaires séparée en dessous prévue pour envoyer ce retour d\'information.'),
	(2190474,660000,2,'OPINION'),
	(2190475,660001,2,'Oui, je suis d\'accord avec l\'outil d\'évaluation et je n\'ai pas de commentaires à ajouter'),
	(2190476,660002,2,'Veuillez éviter l\'usage de l\'expression \"se reporter au commentaire antérieur\" ou \" voir commentaire concernant l\'indicateur X\". </p> <p> <b> Commentaires de la personne qui révise  le travail:(obligatoire)</b><br/> <couleur des caractères typographiques=\"rouge\"><b>NE PAS CHANGER </b></font> introduire le changement de qualification '),
	(2190477,660003,2,'<p>Veuillez introduire le changement de votre qualification ainsi que les commentaires qui justifient votre changement . Indiquez seulement qualifications OUI/NON (pour les indicateur du cadre juridique) et  les qualifications 0, 25, 50, 75, ou 100  pour les indicateurs de \"pratiques habituelles\". Ne mentionnez pas le changement de qualifications que vous avez suggéré dans la Case de Commentaires ( nous visualiserons votre changement de qualifications dans la case de Changement de Qualification). Évitez l\'usage de l\'expression \" se reporter au commentaire antérieur\" ou \"voir commentaire concernant l\'indicateur X\". </p> <p> <b>Commentaires de la personne qui révise:(obligatoire)</b><br/>  <couleur des caractères typographiques=\"rouge\">... Ces commentaires <couleur de caractères typographiques \"rouges\" ><b> seront publiés </b></font> par défaut. </p> À ce stade, n\'introduisez pas de commentaires personnels destinés au directeur de projets, présentez uniquement les raisons et les antécédents de votre exposé. </p>'),
	(2190478,690000,2,'Ajoutez une question à la liste'),
	(2190479,690001,2,'Liste de questions '),
	(2190480,710000,2,'Système Indaba  | Révision générale du sondage '),
	(2190481,730000,2,'Cibles'),
	(2190482,730001,2,'Téléchargement de la réponse'),
	(2190483,740000,2,'Déclencheur du système de recherche des utilisateurs d\'Indaba'),
	(2190484,740001,2,'Visualiser la liste des utilisateurs du déclencheur'),
	(2190485,740002,2,'Aucune indication d\' utilisateur du déclencheur '),
	(2190486,740003,2,'Sélectionner un projet'),
	(2190487,740004,2,'Sélectionner un rôle'),
	(2190488,740005,2,'Sélectionner un utilisateur assigné'),
	(2190489,740006,2,'Thème de l\'affaire'),
	(2190490,740007,2,'ÉRREUR'),
	(2190491,760001,2,'Déroulement du travail'),
	(2190492,760002,2,'Exécuter tout'),
	(2190493,760003,2,'IDENTITÉ'),
	(2190494,760004,2,'DESCRIPTION'),
	(2190495,760005,2,'CRÉÉ À'),
	(2190496,760006,2,'CRÉÉ PAR'),
	(2190497,760007,2,'DURÉE'),
	(2190498,760008,2,'Objets servant au déroulement du travail'),
	(2190499,760009,2,'Cible pour le déroulement du travail '),
	(2190500,760010,2,'DÉROULEMENT DU TRAVAIL - OBJET'),
	(2190501,760011,2,'HEURE D\'INITIALISATION DU TRAVAIL'),
	(2190502,760012,2,'Déoulement du travail / buts à atteindre / tâches'),
	(2190503,760013,2,'DÉROULEMENT DU TRAVAIL / CIBLE / OBJET'),
	(2190504,760014,2,'SÉQUENCE'),
	(2190505,760015,2,'TÂCHE'),
	(2190506,760016,2,'UTILISATEUR'),
	(2190507,770000,2,'Système Indaba  | Votre contenu'),
	(2190508,770001,2,'Sponsors'),
	(2190509,770002,2,'Déclaration de l\'administrateur'),
	(2190510,770003,2,'Vos assignations'),
	(2190511,770004,2,'Nouveau délai ( AAAA / MM / JJ )'),
	(2190512,2150000,2,'Système d\'Indaba  | Widgets'),
	(2190513,2150001,2,'Page du site du client avec widgets'),
	(2190514,2160000,2,'Système d\'Indaba | Gestion de widgets'),
	(2190515,2160001,2,'Outil de configuration de widget'),
	(2190516,2160002,2,'Configuration '),
	(2190517,2160003,2,'largeur'),
	(2190518,2160004,2,'hauteur'),
	(2190519,2160005,2,'adresse cible'),
	(2190520,2160006,2,'paramètre'),
	(2190521,2160007,2,'Unité logique vers'),
	(2190522,2160008,2,'Éliminer article sélectionné'),
	(2190523,2160009,2,'Établir un code'),
	(2190524,2170000,2,'Système d\'Indaba | widget #1 titre de la page pour # widget 1'),
	(2190525,2170001,2,'Veuillez choisir votre langue'),
	(2190526,2180000,2,'Système d\'Indaba | widget #2 titre de la page pour # widget 2'),
	(2190527,2180001,2,'Introduisez votre nom'),
	(2190528,2180002,2,'Sélectionnez votre parfum favori de glace'),
	(2190529,2180003,2,'Chocolat'),
	(2190530,2180004,2,'Fraise'),
	(2190531,2180005,2,'Vanille'),
	(2190532,2190000,2,'Système d\'Indaba | widget #3 titre de la page pour # widget 3'),
	(2190533,2190001,2,'Cliquez sur les liens ci-dessous');

/*!40000 ALTER TABLE `text_item` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table text_resource
# ------------------------------------------------------------

LOCK TABLES `text_resource` WRITE;
/*!40000 ALTER TABLE `text_resource` DISABLE KEYS */;

INSERT INTO `text_resource` (`id`, `resource_name`, `description`)
VALUES
	(10000,'common.err.invalid.user','last updated @ 2012-02-15 22:02:16'),
	(10001,'common.err.attachment.missed','last updated @ 2012-02-15 22:02:46'),
	(10002,'common.err.internal.server','last updated @ 2012-02-15 22:03:17'),
	(10003,'common.err.invalid.parameter.unrecongnized','last updated @ 2012-02-15 22:03:43'),
	(10004,'common.err.invalid.parameter','last updated @ 2012-02-15 22:05:57'),
	(10005,'common.err.data.notexisted','last updated @ 2012-02-15 22:06:20'),
	(10006,'common.err.exceed.max.filesize','last updated @ 2012-02-15 22:06:44'),
	(10012,'common.alert.task.done','last updated @ 2012-02-15 22:09:14'),
	(10013,'common.alert.task.save','last updated @ 2012-02-15 22:09:42'),
	(10014,'common.remark.suspended','last updated @ 2012-02-15 23:07:10'),
	(10015,'common.remark.inactive','last updated @ 2012-02-15 23:07:30'),
	(10016,'common.remark.active','last updated @ 2012-02-15 23:08:28'),
	(10017,'common.remark.aware','last updated @ 2012-02-15 23:08:39'),
	(10019,'common.remark.started','last updated @ 2012-02-15 23:09:00'),
	(10020,'common.remark.done','last updated @ 2012-02-15 23:09:07'),
	(10021,'common.label.status','last updated @ 2012-02-15 23:09:33'),
	(10022,'common.label.view','last updated @ 2012-02-15 23:09:49'),
	(10023,'common.label.history','last updated @ 2012-02-15 23:10:11'),
	(10024,'common.label.goal','last updated @ 2012-02-15 23:10:20'),
	(10025,'common.label.estimate','last updated @ 2012-02-15 23:10:26'),
	(10026,'common.label.donepercent','last updated @ 2012-02-15 23:14:48'),
	(10027,'common.label.nextdue','last updated @ 2012-02-15 23:15:10'),
	(10028,'common.label.opencases','last updated @ 2012-02-15 23:15:22'),
	(10029,'common.label.peopleassigned','last updated @ 2012-02-15 23:15:48'),
	(10030,'common.msg.nodata','last updated @ 2012-02-15 23:25:06'),
	(10031,'common.alt.notstarted','last updated @ 2012-02-15 23:37:59'),
	(10032,'common.alt.completed','last updated @ 2012-02-15 23:38:25'),
	(10033,'common.alt.inprogress','last updated @ 2012-02-15 23:38:40'),
	(10034,'common.alt.noticed','last updated @ 2012-02-15 23:38:56'),
	(10035,'common.alt.signedin','last updated @ 2012-02-15 23:39:25'),
	(10036,'common.label.completepercentage','last updated @ 2012-02-15 23:40:10'),
	(10037,'common.msg.nextstep','last updated @ 2012-02-15 23:49:45'),
	(10038,'common.msg.noassign','last updated @ 2012-02-15 23:50:12'),
	(10039,'common.alt.viewcontent','last updated @ 2012-02-15 23:50:46'),
	(10040,'common.alt.historychart','last updated @ 2012-02-15 23:51:41'),
	(10041,'common.btn.edit','last updated @ 2012-02-15 23:53:14'),
	(10042,'common.msg.product','last updated @ 2012-02-15 23:56:46'),
	(10043,'common.choice.exclude','last updated @ 2012-02-15 23:57:51'),
	(10044,'common.choice.include','last updated @ 2012-02-15 23:58:26'),
	(10045,'common.choice.overdue','last updated @ 2012-02-15 23:58:57'),
	(10046,'common.choice.notoverdue','last updated @ 2012-02-15 23:59:07'),
	(10047,'common.choice.all','last updated @ 2012-02-15 23:59:40'),
	(10048,'common.btn.add','last updated @ 2012-02-16 00:00:06'),
	(10049,'common.alt.answer','last updated @ 2012-02-16 00:08:53'),
	(10050,'common.label.case','last updated @ 2012-02-16 00:09:06'),
	(10051,'common.label.title','last updated @ 2012-02-16 00:09:17'),
	(10052,'common.label.priority','last updated @ 2012-02-16 00:09:29'),
	(10053,'common.label.owner','last updated @ 2012-02-16 00:09:51'),
	(10054,'common.label.attachedcontent','last updated @ 2012-02-16 00:10:43'),
	(10055,'common.label.name','last updated @ 2012-02-16 00:12:51'),
	(10177,'common.label.role','last updated @ 2012-02-20 15:41:18'),
	(10057,'common.label.country','last updated @ 2012-02-16 00:14:10'),
	(10058,'common.label.teams','last updated @ 2012-02-16 00:14:43'),
	(10059,'common.label.assignedcontent','last updated @ 2012-02-16 00:14:56'),
	(10060,'common.alert.addcasenotes.success','last updated @ 2012-02-16 09:44:03'),
	(10061,'common.alert.addcasenotes.fail','last updated @ 2012-02-16 09:44:23'),
	(10062,'common.alert.createcase.success','last updated @ 2012-02-16 09:47:26'),
	(10063,'common.alert.createcase.fail','last updated @ 2012-02-16 09:47:47'),
	(10064,'common.alert.fireuserfinder.success','last updated @ 2012-02-16 09:52:53'),
	(10065,'common.alert.fireuserfinder.fail','last updated @ 2012-02-16 09:52:57'),
	(10066,'common.msg.fireuserfinder.summary','last updated @ 2012-02-16 09:53:16'),
	(10067,'common.alert.chngpasswd.success','last updated @ 2012-02-16 09:55:29'),
	(10068,'common.alert.chngpasswd.fail','last updated @ 2012-02-16 09:55:54'),
	(10069,'common.msg.fname.anonymous','last updated @ 2012-02-16 09:59:11'),
	(10070,'common.msg.lname.contributor','last updated @ 2012-02-16 09:59:32'),
	(10071,'common.msg.noproject','last updated @ 2012-02-16 10:00:56'),
	(10072,'common.err.invaliduser','last updated @ 2012-02-16 10:06:34'),
	(10073,'common.label.validityuser','last updated @ 2012-02-16 10:07:08'),
	(10074,'common.err.badparam','last updated @ 2012-02-16 10:09:25'),
	(10075,'common.alert.surveyanswer.success','last updated @ 2012-02-16 10:12:48'),
	(10076,'common.alert.surveyanswer.fail','last updated @ 2012-02-16 10:13:15'),
	(10077,'common.err.notfindquestion','last updated @ 2012-02-16 10:19:17'),
	(10078,'common.err.notfindcountry','last updated @ 2012-02-16 10:19:37'),
	(10079,'common.alert.upcatecase.success','last updated @ 2012-02-16 10:21:29'),
	(10080,'common.alert.upcatecase.fail','last updated @ 2012-02-16 10:21:43'),
	(10081,'common.label.deadline','last updated @ 2012-02-16 11:16:10'),
	(10082,'common.js.alert.askopinions','last updated @ 2012-02-16 15:41:06'),
	(10083,'common.js.alert.title.info','last updated @ 2012-02-16 15:41:54'),
	(10084,'common.js.alert.title.error','last updated @ 2012-02-16 15:42:29'),
	(10085,'common.js.alert.title.confirm','last updated @ 2012-02-16 15:42:49'),
	(10086,'common.js.alert.title.warn','last updated @ 2012-02-16 15:42:59'),
	(10087,'common.js.err.submitreview','last updated @ 2012-02-16 15:43:42'),
	(10088,'common.btn.saved','last updated @ 2012-02-16 15:45:21'),
	(10089,'common.js.alert.askinputsource','last updated @ 2012-02-16 15:47:18'),
	(10090,'common.js.alert.choicesource','last updated @ 2012-02-16 15:49:42'),
	(10091,'common.js.alert.needonesource','last updated @ 2012-02-16 15:51:12'),
	(10092,'common.js.alert.asksoucedesc','last updated @ 2012-02-16 15:51:50'),
	(10093,'common.js.alert.askscoreoption','last updated @ 2012-02-16 15:52:36'),
	(10094,'common.js.alert.inputanswer','last updated @ 2012-02-16 15:53:26'),
	(10095,'common.js.alert.suggestedscore','last updated @ 2012-02-16 15:56:17'),
	(10096,'common.js.alert.comments','last updated @ 2012-02-16 15:58:47'),
	(10097,'common.js.err.interval','last updated @ 2012-02-16 16:00:42'),
	(10098,'common.js.alert.answerleft','last updated @ 2012-02-16 16:03:07'),
	(10099,'common.js.alert.sumbitassignment','last updated @ 2012-02-16 16:05:12'),
	(10100,'common.js.alert.pendingquestion','last updated @ 2012-02-16 16:07:41'),
	(10101,'common.js.alert.exceedwords','last updated @ 2012-02-16 16:10:28'),
	(10102,'common.js.alert.lesswords','last updated @ 2012-02-16 16:11:24'),
	(10104,'common.js.alert.topictitle','last updated @ 2012-02-16 16:15:15'),
	(10105,'common.js.msg.usernotes','last updated @ 2012-02-16 16:17:26'),
	(10106,'common.js.msg.staffnotes','last updated @ 2012-02-16 16:18:15'),
	(10107,'common.btn.view','last updated @ 2012-02-16 16:19:05'),
	(10109,'common.js.alt.unblock','last updated @ 2012-02-16 16:22:37'),
	(10110,'common.js.ckedit.toolbar.source','last updated @ 2012-02-16 16:24:57'),
	(10111,'common.js.ckedit.toolbar.pastetxt','last updated @ 2012-02-16 16:25:27'),
	(10112,'common.js.ckedit.toolbar.pastefromword','last updated @ 2012-02-16 16:25:54'),
	(10113,'common.js.ckedit.toolbar.print','last updated @ 2012-02-16 16:26:04'),
	(10114,'common.js.ckedit.toolbar.spellchecker','last updated @ 2012-02-16 16:26:22'),
	(10115,'common.js.ckedit.toolbar.scayt','last updated @ 2012-02-16 16:26:31'),
	(10116,'common.js.ckedit.toolbar.undo','last updated @ 2012-02-16 16:26:37'),
	(10117,'common.js.ckedit.toolbar.redo','last updated @ 2012-02-16 16:26:48'),
	(10118,'common.js.ckedit.toolbar.find','last updated @ 2012-02-16 16:26:54'),
	(10119,'common.js.ckedit.toolbar.replace','last updated @ 2012-02-16 16:27:02'),
	(10120,'common.js.ckedit.toolbar.selectall','last updated @ 2012-02-16 16:27:17'),
	(10121,'common.js.ckedit.toolbar.rmformat','last updated @ 2012-02-16 16:27:55'),
	(10122,'common.js.ckedit.toolbar.bold','last updated @ 2012-02-16 16:28:03'),
	(10123,'common.js.ckedit.toolbar.italic','last updated @ 2012-02-16 16:28:12'),
	(10124,'common.js.ckedit.toolbar.underline','last updated @ 2012-02-16 16:28:25'),
	(10125,'common.js.ckedit.toolbar.strike','last updated @ 2012-02-16 16:28:31'),
	(10126,'common.js.ckedit.toolbar.numlist','last updated @ 2012-02-16 16:28:43'),
	(10127,'common.js.ckedit.toolbar.bulletlist','last updated @ 2012-02-16 16:28:53'),
	(10128,'common.js.ckedit.toolbar.link','last updated @ 2012-02-16 16:28:58'),
	(10129,'common.js.ckedit.toolbar.unlink','last updated @ 2012-02-16 16:29:04'),
	(10130,'common.js.ckedit.toolbar.anchor','last updated @ 2012-02-16 16:29:11'),
	(10131,'common.js.ckedit.toolbar.format','last updated @ 2012-02-16 16:29:18'),
	(10132,'common.js.ckedit.toolbar.bgcolor','last updated @ 2012-02-16 16:29:25'),
	(10133,'common.js.ckedit.toolbar.maximize','last updated @ 2012-02-16 16:29:36'),
	(10134,'common.js.ckedit.toolbar','last updated @ 2012-02-16 16:53:13'),
	(10135,'common.js.alert.enhancedtext','last updated @ 2012-02-16 16:59:26'),
	(10136,'common.label.time','last updated @ 2012-02-16 17:01:03'),
	(10137,'common.label.topictitle','last updated @ 2012-02-16 17:01:29'),
	(10138,'common.js.alert.successenhance','last updated @ 2012-02-16 17:03:09'),
	(10139,'common.js.alert.title.success','last updated @ 2012-02-16 17:04:03'),
	(10140,'common.js.msg.staffdiscussion','last updated @ 2012-02-16 17:23:43'),
	(10141,'common.js.msg.staffauthordiscussion','last updated @ 2012-02-16 17:24:39'),
	(10142,'common.js.alert.succssreview','last updated @ 2012-02-16 17:26:25'),
	(10143,'common.js.msg.discussion','last updated @ 2012-02-16 17:30:00'),
	(10146,'common.js.alert.sumitquestion','last updated @ 2012-02-16 17:38:50'),
	(10147,'common.js.alert.incompletedquestion','last updated @ 2012-02-16 17:41:36'),
	(10148,'common.js.alert.title.sorry','last updated @ 2012-02-16 17:50:14'),
	(10149,'common.label.teamcontent','last updated @ 2012-02-16 18:22:56'),
	(10150,'common.js.alert.attachduplicated','last updated @ 2012-02-20 11:31:50'),
	(10151,'common.js.alert.attachfail','last updated @ 2012-02-20 11:34:12'),
	(10152,'common.js.alert.attachdesc','last updated @ 2012-02-20 11:36:50'),
	(10153,'common.js.alert.attachfile','last updated @ 2012-02-20 11:40:04'),
	(10154,'common.js.alert.deleteattach','last updated @ 2012-02-20 11:44:34'),
	(10155,'common.js.alert.deleteattachfail','last updated @ 2012-02-20 11:45:39'),
	(10156,'common.js.alert.uploadsuccess','last updated @ 2012-02-20 13:36:52'),
	(10157,'common.js.alert.fileexisted','last updated @ 2012-02-20 13:37:41'),
	(10158,'common.js.alert.invalidimage','last updated @ 2012-02-20 13:55:35'),
	(10159,'common.js.alert.noimagespecified','last updated @ 2012-02-20 13:57:53'),
	(10160,'common.js.alert.curpasswdempty','last updated @ 2012-02-20 13:59:04'),
	(10161,'common.js.alert.newpasswdempty','last updated @ 2012-02-20 13:59:46'),
	(10162,'common.js.alert.confirmpasswdempty','last updated @ 2012-02-20 14:01:14'),
	(10163,'common.js.alert.passwdinconsistent','last updated @ 2012-02-20 14:02:17'),
	(10164,'common.js.alert.passwdinvalid','last updated @ 2012-02-20 14:03:18'),
	(10165,'common.js.alert.passwdchngsuccess','last updated @ 2012-02-20 14:04:18'),
	(10166,'common.btn.editdeadline','last updated @ 2012-02-20 14:05:07'),
	(10167,'common.js.msg.newdeadline','last updated @ 2012-02-20 14:05:56'),
	(10168,'','last updated @ 2012-02-20 14:09:14'),
	(10170,'common.js.msg.partialsubmission','last updated @ 2012-02-20 14:48:15'),
	(10171,'common.js.msg.forcecomplete','last updated @ 2012-02-20 14:49:45'),
	(10172,'common.js.msg.exitassignment','last updated @ 2012-02-20 14:50:36'),
	(10173,'common.js.alert.undone','last updated @ 2012-02-20 14:52:56'),
	(10174,'common.js.msg.selected','last updated @ 2012-02-20 14:54:20'),
	(10175,'common.btn.done','last updated @ 2012-02-20 15:18:33'),
	(10176,'common.label.project','last updated @ 2012-02-20 15:40:21'),
	(10178,'common.label.assigneduser','last updated @ 2012-02-20 15:41:49'),
	(10179,'common.label.casesubject','last updated @ 2012-02-20 15:44:48'),
	(10180,'common.label.casebody','last updated @ 2012-02-20 15:45:14'),
	(10181,'common.label.action','last updated @ 2012-02-20 15:45:23'),
	(10182,'common.label.fireall','last updated @ 2012-02-20 15:45:31'),
	(10183,'common.js.msg.usertriggerlist','last updated @ 2012-02-20 15:47:05'),
	(10184,'common.js.alert.descempty','last updated @ 2012-02-20 15:48:11'),
	(10185,'common.js.alert.prjnoselected','last updated @ 2012-02-20 15:49:03'),
	(10186,'common.js.alert.rolenoselected','last updated @ 2012-02-20 15:50:04'),
	(10187,'common.js.alert.noassigneduser','last updated @ 2012-02-20 15:50:49'),
	(10188,'common.js.alert.nocasesubject','last updated @ 2012-02-20 15:51:56'),
	(10189,'common.js.alert.nocasebody','last updated @ 2012-02-20 15:52:16'),
	(10191,'common.js.msg.total','last updated @ 2012-02-20 15:54:08'),
	(10192,'common.js.msg.new','last updated @ 2012-02-20 15:54:13'),
	(10193,'common.js.alert.toolength','last updated @ 2012-02-20 17:44:12'),
	(10194,'common.js.alert.filenotadded','last updated @ 2012-02-20 17:47:26'),
	(10195,'common.btn.save','last updated @ 2012-02-23 12:02:15'),
	(10196,'common.btn.saveindicator','last updated @ 2012-02-23 12:13:07'),
	(10197,'common.title.comments','last updated @ 2012-02-23 12:17:17'),
	(10198,'common.title.sources','last updated @ 2012-02-23 12:23:55'),
	(10199,'common.btn.publish','last updated @ 2012-02-23 12:45:19'),
	(10200,'common.btn.publishthis','last updated @ 2012-02-23 12:45:26'),
	(10201,'common.btn.addcomment','last updated @ 2012-02-23 12:47:56'),
	(10202,'common.label.adding','last updated @ 2012-02-23 12:54:02'),
	(10203,'common.title.from','last updated @ 2012-02-23 12:58:30'),
	(10204,'common.btn.donesubmit','last updated @ 2012-02-23 13:01:11'),
	(10205,'common.choice.onlyselected','last updated @ 2012-02-23 13:46:53'),
	(10206,'common.btn.submitindicators','last updated @ 2012-02-23 14:56:46'),
	(10207,'common.msg.scorecardnav','last updated @ 2012-02-23 15:48:59'),
	(10208,'common.btn.submit','last updated @ 2012-02-23 15:50:33'),
	(10209,'common.label.review','last updated @ 2012-02-23 15:56:43'),
	(10210,'common.label.reviews','last updated @ 2012-02-23 15:58:02'),
	(10211,'common.msg.nextcategory','last updated @ 2012-02-23 16:09:54'),
	(10212,'common.msg.pleasewait','last updated @ 2012-02-23 16:11:10'),
	(10213,'common.msg.youcontent','last updated @ 2012-02-23 16:38:33'),
	(10214,'common.btn.editdisabled','last updated @ 2012-02-23 16:39:46'),
	(10215,'common.msg.target','last updated @ 2012-02-23 16:40:45'),
	(10216,'common.msg.rulemanager','last updated @ 2012-02-23 16:41:37'),
	(10217,'common.boolean.no','last updated @ 2012-02-23 16:51:29'),
	(10218,'common.boolean.yes','last updated @ 2012-02-23 16:54:15'),
	(10219,'common.alert.nosubject','last updated @ 2012-02-23 16:57:20'),
	(10220,'common.label.tags','last updated @ 2012-02-23 17:01:17'),
	(10221,'common.msg.addtrigger','last updated @ 2012-02-23 17:19:57'),
	(10222,'common.msg.prevcategory','last updated @ 2012-02-23 17:22:18'),
	(10223,'common.alert.noreceiver','last updated @ 2012-02-23 17:24:28'),
	(10224,'common.msg.to','last updated @ 2012-02-23 17:25:31'),
	(10225,'common.btn.cancel','last updated @ 2012-02-23 17:27:15'),
	(10226,'common.alert.noanswer','last updated @ 2012-02-23 17:30:35'),
	(10227,'common.label.message','last updated @ 2012-02-23 17:36:50'),
	(10228,'common.alert.nomessage','last updated @ 2012-02-23 17:41:34'),
	(10229,'common.msg.nextindicator','last updated @ 2012-02-23 17:42:32'),
	(10230,'common.btn.sendmsg','last updated @ 2012-02-23 18:01:43'),
	(10231,'common.msg.prjupdates','last updated @ 2012-02-23 18:04:43'),
	(10232,'common.btn.apply','last updated @ 2012-02-23 18:05:25'),
	(10233,'common.btn.origanswer','last updated @ 2012-02-23 18:07:28'),
	(10234,'common.label.createtime','last updated @ 2012-02-23 18:08:50'),
	(10235,'common.msg.blockworkflow','last updated @ 2012-02-23 18:11:04'),
	(10236,'common.msg.blockpublish','last updated @ 2012-02-23 18:12:00'),
	(10237,'common.msg.suggestedanswer','last updated @ 2012-02-23 18:12:58'),
	(10238,'common.msg.previndicator','last updated @ 2012-02-23 18:16:59'),
	(10239,'common.msg.yourinbox','last updated @ 2012-02-23 18:18:02'),
	(10240,'common.msg.home','last updated @ 2012-02-23 18:22:35'),
	(10241,'common.label.cases','last updated @ 2012-02-23 18:25:42'),
	(10242,'common.label.fire','last updated @ 2012-02-23 18:26:31'),
	(10243,'common.label.desc','last updated @ 2012-02-23 18:27:52'),
	(10244,'common.label.file','last updated @ 2012-02-23 18:30:26'),
	(10245,'common.btn.reset','last updated @ 2012-02-23 18:31:03'),
	(10246,'common.msg.opinionagree','last updated @ 2012-02-23 18:32:35'),
	(10247,'common.msg.opiniondisagree','last updated @ 2012-02-23 18:33:46'),
	(10248,'common.msg.opinionnojudge','last updated @ 2012-02-23 18:34:55'),
	(10249,'common.label.subject','last updated @ 2012-02-23 18:36:07'),
	(10250,'common.msg.charterror','last updated @ 2012-02-23 18:38:01'),
	(10251,'common.btn.havequestions','last updated @ 2012-02-23 18:41:57'),
	(10252,'common.label.username','last updated @ 2012-02-23 20:21:58'),
	(10253,'common.btn.preview','last updated @ 2012-02-23 20:25:40'),
	(10254,'common.label.location','last updated @ 2012-02-23 20:26:58'),
	(10255,'common.label.emailmsgdetaillevel','last updated @ 2012-02-23 20:31:14'),
	(10256,'common.label.mailaddr','last updated @ 2012-02-23 20:32:59'),
	(10257,'common.label.reviewTips','last updated @ 2012-02-23 20:33:47'),
	(10258,'common.label.lastname','last updated @ 2012-02-23 20:40:15'),
	(10259,'common.label.note','last updated @ 2012-02-23 20:41:33'),
	(10260,'common.label.bio','last updated @ 2012-02-23 20:44:29'),
	(10261,'common.label.firstname','last updated @ 2012-02-23 20:46:03'),
	(10262,'common.btn.copydeeplink','last updated @ 2012-02-23 20:49:37'),
	(10263,'common.msg.setdeadline','last updated @ 2012-02-24 19:49:32'),
	(30000,'jsp.allcontent.pagetitle','Last update @2012-02-27 12:35:35'),
	(30001,'jsp.allcontent.allcontents','Last update @2012-02-27 12:35:35'),
	(40000,'jsp.answerDetails.pagetitle','Last update @2012-02-27 12:35:36'),
	(50000,'jsp.assignmentMessage.pagetitle','Last update @2012-02-27 12:35:36'),
	(60000,'jsp.attachment.attachments','Last update @2012-02-27 12:35:36'),
	(60001,'jsp.attachment.file','Last update @2012-02-27 12:35:37'),
	(60002,'jsp.attachment.size','Last update @2012-02-27 12:35:37'),
	(60003,'jsp.attachment.by','Last update @2012-02-27 12:35:37'),
	(60004,'jsp.attachment.mb','Last update @2012-02-27 12:35:37'),
	(60005,'jsp.attachment.kb','Last update @2012-02-27 12:35:37'),
	(60006,'jsp.attachment.b','Last update @2012-02-27 12:35:38'),
	(60007,'jsp.attachment.addNewAttachments','Last update @2012-02-27 12:35:38'),
	(60008,'jsp.attachment.maxFileSize','Last update @2012-02-27 12:35:38'),
	(60009,'jsp.attachment.maxFileSizeNum','Last update @2012-02-27 12:35:38'),
	(70000,'jsp.caseFilter.tag','Last update @2012-02-27 12:35:39'),
	(80000,'jsp.casePage.title.newCase','Last update @2012-02-27 12:35:39'),
	(80001,'jsp.casePage.title.caseDetail','Last update @2012-02-27 12:35:40'),
	(80002,'jsp.casePage.newCase','Last update @2012-02-27 12:35:40'),
	(80003,'jsp.casePage.case','Last update @2012-02-27 12:35:40'),
	(80004,'jsp.casePage.createBy','Last update @2012-02-27 12:35:40'),
	(80005,'jsp.casePage.caseTitle','Last update @2012-02-27 12:35:40'),
	(80006,'jsp.casePage.caseDescription','Last update @2012-02-27 12:35:40'),
	(80007,'jsp.casePage.casestatus','Last update @2012-02-27 12:35:41'),
	(80008,'jsp.casePage.openNew','Last update @2012-02-27 12:35:41'),
	(80009,'jsp.casePage.assignTo','Last update @2012-02-27 12:35:41'),
	(80010,'jsp.casePage.attachUsers','Last update @2012-02-27 12:35:41'),
	(80011,'jsp.casePage.attachContent','Last update @2012-02-27 12:35:42'),
	(80012,'jsp.casePage.attachTags','Last update @2012-02-27 12:35:42'),
	(80013,'jsp.casePage.attachFiles','Last update @2012-02-27 12:35:42'),
	(90000,'jsp.cases.title','Last update @2012-02-27 12:35:42'),
	(90001,'jsp.cases.openNewCase','Last update @2012-02-27 12:35:43'),
	(90002,'jsp.cases.allCases','Last update @2012-02-27 12:35:43'),
	(100000,'jsp.contentApproval.pagetitle','Last update @2012-02-27 12:35:43'),
	(100001,'jsp.contentApproval.ApproveInstruction','Last update @2012-02-27 12:35:44'),
	(100002,'jsp.contentApproval.ApproveThisContent','Last update @2012-02-27 12:35:44'),
	(110000,'jsp.contentGeneral.instructions','Last update @2012-02-27 12:35:44'),
	(110001,'jsp.contentGeneral.currentTasks','Last update @2012-02-27 12:35:44'),
	(110002,'jsp.contentGeneral.caseNO','Last update @2012-02-27 12:35:45'),
	(110003,'jsp.contentGeneral.caseOpened','Last update @2012-02-27 12:35:45'),
	(110004,'jsp.contentGeneral.caseLastUpdated','Last update @2012-02-27 12:35:45'),
	(120000,'jsp.contentPayment.pagetitle','Last update @2012-02-27 12:35:45'),
	(120001,'jsp.contentPayment.instruction','Last update @2012-02-27 12:35:46'),
	(120002,'jsp.contentPayment.paymentMount','Last update @2012-02-27 12:35:46'),
	(120003,'jsp.contentPayment.prompt','Last update @2012-02-27 12:35:46'),
	(120004,'jsp.contentPayment.payees','Last update @2012-02-27 12:35:46'),
	(120005,'jsp.contentPayment.paymentDate','Last update @2012-02-27 12:35:46'),
	(150000,'jsp.error.pagetitle','Last update @2012-02-27 12:35:49'),
	(150001,'jsp.error.errors','Last update @2012-02-27 12:35:49'),
	(150002,'jsp.error.errMsg','Last update @2012-02-27 12:35:49'),
	(150003,'jsp.error.back','Last update @2012-02-27 12:35:49'),
	(160000,'jsp.fileupload.browseFiles','Last update @2012-02-27 12:35:49'),
	(160001,'jsp.fileupload.clearList','Last update @2012-02-27 12:35:49'),
	(160002,'jsp.fileupload.startUpload','Last update @2012-02-27 12:35:50'),
	(180000,'jsp.header.home','Last update @2012-02-27 12:35:50'),
	(180001,'jsp.header.menu.allcontent','Last update @2012-02-27 12:35:50'),
	(180002,'jsp.header.menu.queue','Last update @2012-02-27 12:35:51'),
	(180003,'jsp.header.menu.people','Last update @2012-02-27 12:35:51'),
	(180004,'jsp.header.menu.messaging','Last update @2012-02-27 12:35:51'),
	(180005,'jsp.header.menu.help','Last update @2012-02-27 12:35:51'),
	(180006,'jsp.header.logout','Last update @2012-02-27 12:35:51'),
	(180007,'jsp.header.admin','Last update @2012-02-27 12:35:52'),
	(190000,'jsp.help.title','Last update @2012-02-27 12:35:52'),
	(190001,'jsp.help.msg1','Last update @2012-02-27 12:35:52'),
	(190002,'jsp.help.msg2','Last update @2012-02-27 12:35:52'),
	(190003,'jsp.help.msg3','Last update @2012-02-27 12:35:52'),
	(190004,'jsp.help.msg4','Last update @2012-02-27 12:35:52'),
	(190005,'jsp.help.msg5','Last update @2012-02-27 12:35:53'),
	(190006,'jsp.help.msg6','Last update @2012-02-27 12:35:53'),
	(190007,'jsp.help.wokflowconsole','Last update @2012-02-27 12:35:53'),
	(200000,'jsp.horseApproval.pagetitle','Last update @2012-02-27 12:35:53'),
	(200001,'jsp.horseApproval.instruction','Last update @2012-02-27 12:35:53'),
	(200002,'jsp.horseApproval.approveDone','Last update @2012-02-27 12:35:54'),
	(220000,'jsp.indicatorTag.instruction','Last update @2012-02-27 12:35:54'),
	(220001,'jsp.indicatorTag.buttonAddTag','Last update @2012-02-27 12:35:54'),
	(240000,'jsp.indicatorPeerReviewPage.title','Last update @2012-02-27 12:35:55'),
	(240001,'jsp.indicatorPeerReviewPage.link2Answer','Last update @2012-02-27 12:35:55'),
	(250000,'jsp.indicatorPRReviewPage.title','Last update @2012-02-27 12:35:55'),
	(260000,'jsp.indicatorReviewPage.title','Last update @2012-02-27 12:35:56'),
	(270000,'jsp.journalContentVersion.pagetitle','Last update @2012-02-27 12:35:56'),
	(270001,'jsp.journalContentVersion.content','Last update @2012-02-27 12:35:56'),
	(280000,'jsp.journalOveralReview.title','Last update @2012-02-27 12:35:56'),
	(290000,'jsp.journalPeerReview.pagetitle','Last update @2012-02-27 12:35:57'),
	(300000,'jsp.journalPRReview.pagetitle','Last update @2012-02-27 12:35:57'),
	(310000,'jsp.journalReview.pagetitle','Last update @2012-02-27 12:35:58'),
	(320000,'jsp.journalReviewResponse.pagetitle','Last update @2012-02-27 12:35:58'),
	(330000,'common.label.username.null','Last update @2012-02-27 12:35:58'),
	(330001,'jsp.login.pwd.null','Last update @2012-02-27 12:35:59'),
	(330002,'common.label.username.input','Last update @2012-02-27 12:35:59'),
	(330003,'common.label.username.check','Last update @2012-02-27 12:35:59'),
	(330004,'jsp.login.pwd.toEmail','Last update @2012-02-27 12:35:59'),
	(330005,'jsp.login.check.error','Last update @2012-02-27 12:35:59'),
	(330006,'jsp.login.tomail.error','Last update @2012-02-27 12:35:59'),
	(330007,'jsp.login.pagetitle','Last update @2012-02-27 12:36:00'),
	(330008,'jsp.login.pwd','Last update @2012-02-27 12:36:00'),
	(340000,'jsp.messageDetail.pagetitle','Last update @2012-02-27 12:36:00'),
	(340001,'jsp.messageDetail.reply','Last update @2012-02-27 12:36:00'),
	(340002,'jsp.messageDetail.title','Last update @2012-02-27 12:36:00'),
	(350000,'jsp.messages.pagetitle','Last update @2012-02-27 12:36:01'),
	(350001,'jsp.messages.createNewMsg','Last update @2012-02-27 12:36:01'),
	(350002,'jsp.messages.newCount','Last update @2012-02-27 12:36:01'),
	(350003,'jsp.messages.total','Last update @2012-02-27 12:36:01'),
	(350004,'jsp.messages.from','Last update @2012-02-27 12:36:01'),
	(350005,'jsp.messages.date','Last update @2012-02-27 12:36:02'),
	(350006,'jsp.messages.new','Last update @2012-02-27 12:36:02'),
	(350007,'jsp.messages.rows','Last update @2012-02-27 12:36:02'),
	(360000,'jsp.messageSidebar.new','Last update @2012-02-27 12:36:03'),
	(360001,'jsp.messageSidebar.from','Last update @2012-02-27 12:36:03'),
	(360002,'jsp.messageSidebar.more','Last update @2012-02-27 12:36:03'),
	(370000,'jsp.myOpenCases.yourOpenCases','Last update @2012-02-27 12:36:03'),
	(380000,'jsp.newMessage.pagetitle','Last update @2012-02-27 12:36:04'),
	(380001,'jsp.newMessage.newMsg','Last update @2012-02-27 12:36:04'),
	(380002,'jsp.newMessage.projectUpdate','Last update @2012-02-27 12:36:04'),
	(390000,'jsp.notebook.pagetitle','Last update @2012-02-27 12:36:05'),
	(390001,'jsp.notebook.notification','Last update @2012-02-27 12:36:05'),
	(390002,'jsp.notebook.youHave','Last update @2012-02-27 12:36:05'),
	(390003,'jsp.notebook.assignedTasks','Last update @2012-02-27 12:36:05'),
	(390004,'jsp.notebook.enter','Last update @2012-02-27 12:36:06'),
	(400000,'jsp.notebookDisplay.pagetitle','Last update @2012-02-27 12:36:06'),
	(410000,'jsp.notebookEdit.pagetitle','Last update @2012-02-27 12:36:06'),
	(420000,'jsp.notebookEditor.previousVersions','Last update @2012-02-27 12:36:07'),
	(420001,'jsp.notebookEditor.notebookEditor','Last update @2012-02-27 12:36:07'),
	(420002,'jsp.notebookEditor.words','Last update @2012-02-27 12:36:07'),
	(430000,'jsp.notebookView.editThis','Last update @2012-02-27 12:36:07'),
	(430001,'jsp.notebookView.notebookContent','Last update @2012-02-27 12:36:08'),
	(440000,'jsp.peerReview.peerReviewTips','Last update @2012-02-27 12:36:08'),
	(450000,'jsp.people.pagetitle','Last update @2012-02-27 12:36:09'),
	(450001,'jsp.people.shouldKnow','Last update @2012-02-27 12:36:09'),
	(450002,'jsp.people.yourTeams','Last update @2012-02-27 12:36:09'),
	(450003,'jsp.people.allUsers','Last update @2012-02-27 12:36:09'),
	(450004,'jsp.people.target','Last update @2012-02-27 12:36:09'),
	(460000,'jsp.peopleFilter.team','Last update @2012-02-27 12:36:10'),
	(460001,'jsp.peopleFilter.caseAssociation','Last update @2012-02-27 12:36:10'),
	(460002,'jsp.peopleFilter.assignmentStatus','Last update @2012-02-27 12:36:11'),
	(470000,'jsp.peopleProfile.pagetitle','Last update @2012-02-27 12:36:11'),
	(470001,'jsp.peopleProfile.user','Last update @2012-02-27 12:36:11'),
	(470002,'jsp.peopleProfile.limitedBio','Last update @2012-02-27 12:36:11'),
	(470003,'jsp.peopleProfile.fullBio','Last update @2012-02-27 12:36:12'),
	(470004,'jsp.peopleProfile.phone','Last update @2012-02-27 12:36:12'),
	(470005,'jsp.peopleProfile.mobilePhone','Last update @2012-02-27 12:36:12'),
	(470006,'jsp.peopleProfile.sysInfo','Last update @2012-02-27 12:36:12'),
	(470007,'jsp.peopleProfile.email','Last update @2012-02-27 12:36:12'),
	(470008,'jsp.peopleProfile.forwardInboxMsg','Last update @2012-02-27 12:36:12'),
	(470009,'common.label.emailmsgdetaillevel.alert','Last update @2012-02-27 12:36:13'),
	(470010,'common.label.emailmsgdetaillevel.fullMsg','Last update @2012-02-27 12:36:13'),
	(470011,'jsp.peopleProfile.language','Last update @2012-02-27 12:36:13'),
	(470012,'jsp.peopleProfile.project','Last update @2012-02-27 12:36:13'),
	(470013,'jsp.peopleProfile.role','Last update @2012-02-27 12:36:13'),
	(470014,'jsp.peopleProfile.lastLoginTime','Last update @2012-02-27 12:36:13'),
	(470015,'jsp.peopleProfile.lastLogoutTime','Last update @2012-02-27 12:36:14'),
	(470016,'jsp.peopleProfile.inactive','Last update @2012-02-27 12:36:14'),
	(470017,'jsp.peopleProfile.active','Last update @2012-02-27 12:36:14'),
	(470018,'jsp.peopleProfile.deleted','Last update @2012-02-27 12:36:14'),
	(470019,'jsp.peopleProfile.sendSitemail','Last update @2012-02-27 12:36:14'),
	(470020,'jsp.peopleProfile.aboutYourUserInfo','Last update @2012-02-27 12:36:14'),
	(470021,'jsp.peopleProfile.fullBioE','Last update @2012-02-27 12:36:15'),
	(470022,'jsp.peopleProfile.sysInfoE','Last update @2012-02-27 12:36:15'),
	(470023,'jsp.peopleProfile.sysEmail','Last update @2012-02-27 12:36:15'),
	(470024,'jsp.peopleProfile.sysTips','Last update @2012-02-27 12:36:16'),
	(470025,'jsp.peopleProfile.pwd.change','Last update @2012-02-27 12:36:16'),
	(470026,'jsp.peopleProfile.pwd.cur','Last update @2012-02-27 12:36:16'),
	(470027,'jsp.peopleProfile.pwd.new','Last update @2012-02-27 12:36:16'),
	(470028,'jsp.peopleProfile.pwd.new.confirm','Last update @2012-02-27 12:36:16'),
	(470029,'jsp.peopleProfile.changIconTips','Last update @2012-02-27 12:36:16'),
	(470030,'jsp.peopleProfile.peopleIcon','Last update @2012-02-27 12:36:17'),
	(470031,'jsp.peopleProfile.upload','Last update @2012-02-27 12:36:17'),
	(470032,'jsp.peopleProfile.assignment','Last update @2012-02-27 12:36:17'),
	(470033,'jsp.peopleProfile.openCases','Last update @2012-02-27 12:36:17'),
	(490000,'jsp.queues.pagetitle','Last update @2012-02-27 12:36:18'),
	(490001,'jsp.queues.noQuenes','Last update @2012-02-27 12:36:18'),
	(490002,'jsp.queues.avgTime.assign','Last update @2012-02-27 12:36:18'),
	(490003,'jsp.queues.days','Last update @2012-02-27 12:36:18'),
	(490004,'jsp.queues.avgTimeComplete','Last update @2012-02-27 12:36:19'),
	(490005,'jsp.queues.content','Last update @2012-02-27 12:36:19'),
	(490006,'jsp.queues.inQueue','Last update @2012-02-27 12:36:19'),
	(490007,'jsp.queues.assignment','Last update @2012-02-27 12:36:19'),
	(490008,'jsp.queues.setAssignment','Last update @2012-02-27 12:36:19'),
	(490009,'jsp.queues.setPriority','Last update @2012-02-27 12:36:19'),
	(490010,'jsp.queues.claimThis','Last update @2012-02-27 12:36:20'),
	(490011,'jsp.queues.assignTo','Last update @2012-02-27 12:36:20'),
	(490012,'jsp.queues.noAssign','Last update @2012-02-27 12:36:20'),
	(490013,'jsp.queues.low','Last update @2012-02-27 12:36:20'),
	(490014,'jsp.queues.medium','Last update @2012-02-27 12:36:20'),
	(490015,'jsp.queues.high','Last update @2012-02-27 12:36:20'),
	(490016,'jsp.queues.update.assign','Last update @2012-02-27 12:36:21'),
	(490017,'jsp.queues.returnToQueue','Last update @2012-02-27 12:36:21'),
	(490018,'jsp.queues.iWantThis','Last update @2012-02-27 12:36:21'),
	(500000,'jsp.replyMessage.pagetitle','Last update @2012-02-27 12:36:21'),
	(500001,'jsp.replyMessage.replyMessage','Last update @2012-02-27 12:36:21'),
	(500002,'jsp.replyMessage.prjUpdate','Last update @2012-02-27 12:36:22'),
	(510000,'jsp.ruleManager.pagetitle','Last update @2012-02-27 12:36:22'),
	(510001,'jsp.ruleManager.ruleFiles','Last update @2012-02-27 12:36:22'),
	(510002,'jsp.ruleManager.ruleComponents','Last update @2012-02-27 12:36:23'),
	(510003,'jsp.ruleManager.path','Last update @2012-02-27 12:36:23'),
	(2150000,'jsp.clientpage.pagetitle','Last update @2012-02-27 12:36:23'),
	(2150001,'jsp.clientpage.h1','Last update @2012-02-27 12:36:24'),
	(2160000,'jsp.configtool.pagetitle','Last update @2012-02-27 12:36:24'),
	(2160001,'jsp.configtool.h1','Last update @2012-02-27 12:36:24'),
	(2160002,'jsp.configtool.configuration','Last update @2012-02-27 12:36:24'),
	(2160003,'jsp.configtool.width','Last update @2012-02-27 12:36:24'),
	(2160004,'jsp.configtool.height','Last update @2012-02-27 12:36:24'),
	(2160005,'jsp.configtool.targetaddr','Last update @2012-02-27 12:36:25'),
	(2160006,'jsp.configtool.parameter','Last update @2012-02-27 12:36:25'),
	(2160007,'jsp.configtool.mapptingto','Last update @2012-02-27 12:36:25'),
	(2160008,'jsp.configtool.btn.rmselected','Last update @2012-02-27 12:36:25'),
	(2160009,'jsp.configtool.btn.gencode','Last update @2012-02-27 12:36:25'),
	(2170000,'jsp.widget1.pagetitle','Last update @2012-02-27 12:36:27'),
	(2170001,'jsp.widget1.dt','Last update @2012-02-27 12:36:27'),
	(2180000,'jsp.widget2.title','Last update @2012-02-27 12:36:27'),
	(2180001,'jsp.widget2.entername','Last update @2012-02-27 12:36:27'),
	(2180002,'jsp.widget2.chooseflavor','Last update @2012-02-27 12:36:28'),
	(2180003,'jsp.widget2.chocolate','Last update @2012-02-27 12:36:28'),
	(2180004,'jsp.widget2.strawberry','Last update @2012-02-27 12:36:28'),
	(2180005,'jsp.widget2.vanilla','Last update @2012-02-27 12:36:28'),
	(2190000,'jsp.widget3.pagetitle','Last update @2012-02-27 12:36:28'),
	(2190001,'jsp.widget3.clicklinks','Last update @2012-02-27 12:36:28'),
	(2190002,'jsp.widget3.gotojournal','Last update @2012-02-27 12:36:29'),
	(520000,'jsp.scorecardDisplay.pagetitle','Last update @2012-02-27 12:36:29'),
	(530000,'jsp.scorecardIndicatorSearch.tags','Last update @2012-02-27 12:36:29'),
	(540000,'jsp.scorecardNav.readOnly','Last update @2012-02-27 12:36:30'),
	(540001,'jsp.scorecardNav.nav','Last update @2012-02-27 12:36:30'),
	(540002,'jsp.scorecardNav.questionList','Last update @2012-02-27 12:36:30'),
	(540003,'jsp.scorecardNav.submitQuestion','Last update @2012-02-27 12:36:30'),
	(540004,'jsp.scorecardNav.indicatorTags','Last update @2012-02-27 12:36:30'),
	(550000,'jsp.scorecardPRDisagree.list','Last update @2012-02-27 12:36:30'),
	(560000,'jsp.sourceWMChoice.sources.desc','Last update @2012-02-27 12:36:31'),
	(560001,'jsp.sourceWMChoice.sources.report','Last update @2012-02-27 12:36:31'),
	(560002,'jsp.sourceWMChoice.sources.acadamic','Last update @2012-02-27 12:36:31'),
	(560003,'jsp.sourceWMChoice.sources.govStudy','Last update @2012-02-27 12:36:31'),
	(560004,'jsp.sourceWMChoice.sources.organic','Last update @2012-02-27 12:36:31'),
	(560005,'jsp.sourceWMChoice.sources.govOfficial','Last update @2012-02-27 12:36:32'),
	(560006,'jsp.sourceWMChoice.sources.interAcadamic','Last update @2012-02-27 12:36:32'),
	(560007,'jsp.sourceWMChoice.sources.civilSociety','Last update @2012-02-27 12:36:32'),
	(560008,'jsp.sourceWMChoice.sources.journal','Last update @2012-02-27 12:36:32'),
	(570000,'jsp.sourceWidgetText.sourcedesc','Last update @2012-02-27 12:36:32'),
	(590000,'jsp.surveyAnswer.title','Last update @2012-02-27 12:36:33'),
	(600000,'jsp.surveryAnsDisp.pagetitle','Last update @2012-02-27 12:36:34'),
	(600001,'jsp.surveryAnsDisp.answerFrom','Last update @2012-02-27 12:36:34'),
	(640000,'jsp.surveryAnsOrig.pagetitle','Last update @2012-02-27 12:36:36'),
	(640001,'jsp.surveryAnsOrig.taskName','Last update @2012-02-27 12:36:36'),
	(650000,'jsp.surveryAnsPR.opinions','Last update @2012-02-27 12:36:36'),
	(650001,'jsp.surveryAnsPR.agree','Last update @2012-02-27 12:36:37'),
	(650002,'jsp.surveryAnsPR.opinion.1','Last update @2012-02-27 12:36:37'),
	(650003,'jsp.surveryAnsPR.opinion1.desc','Last update @2012-02-27 12:36:37'),
	(650004,'jsp.surveryAnsPR.opinion2.desc','Last update @2012-02-27 12:36:37'),
	(660000,'jsp.surveryAnsPRDisp.opinion','Last update @2012-02-27 12:36:37'),
	(660001,'jsp.surveryAnsPRDisp.opinion.1','Last update @2012-02-27 12:36:38'),
	(660002,'jsp.surveryAnsPRDisp.opinion1.desc','Last update @2012-02-27 12:36:38'),
	(660003,'jsp.surveryAnsPRDisp.opinion2.desc','Last update @2012-02-27 12:36:38'),
	(690000,'jsp.surveryAnsBar.add.question','Last update @2012-02-27 12:36:39'),
	(690001,'jsp.surveryAnsBar.question.list','Last update @2012-02-27 12:36:39'),
	(710000,'jsp.surveyOverallRev.pagetitle','Last update @2012-02-27 12:36:39'),
	(730000,'jsp.sidebar.h3','Last update @2012-02-27 12:36:40'),
	(730001,'jsp.sidebar.loadanswer','Last update @2012-02-27 12:36:40'),
	(740000,'jsp.userfinder.pagetitle','Last update @2012-02-27 12:36:40'),
	(740001,'jsp.userfinder.viewList','Last update @2012-02-27 12:36:40'),
	(740002,'jsp.userfinder.noData','Last update @2012-02-27 12:36:41'),
	(740003,'common.label.projectChoose','Last update @2012-02-27 12:36:41'),
	(740004,'jsp.userfinder.add.roleChoose','Last update @2012-02-27 12:36:41'),
	(740005,'jsp.userfinder.add.userChoose','Last update @2012-02-27 12:36:41'),
	(740006,'jsp.userfinder.add.caseSubject','Last update @2012-02-27 12:36:41'),
	(740007,'jsp.userfinder.add.error','Last update @2012-02-27 12:36:42'),
	(760000,'jsp.workflowConsole.pagetitle','Last update @2012-02-27 12:36:42'),
	(760001,'jsp.workflowConsole.workflows','Last update @2012-02-27 12:36:42'),
	(760002,'jsp.workflowConsole.workflows.runAll','Last update @2012-02-27 12:36:42'),
	(760003,'jsp.workflowConsole.workflows.id','Last update @2012-02-27 12:36:42'),
	(760004,'jsp.workflowConsole.workflows.description','Last update @2012-02-27 12:36:43'),
	(760005,'jsp.workflowConsole.workflows.createdAt','Last update @2012-02-27 12:36:43'),
	(760006,'jsp.workflowConsole.workflows.createdBy','Last update @2012-02-27 12:36:43'),
	(760007,'jsp.workflowConsole.workflows.duration','Last update @2012-02-27 12:36:43'),
	(760008,'jsp.workflowConsole.workflowObjects','Last update @2012-02-27 12:36:43'),
	(760009,'jsp.workflowConsole.workflowObjects.target','Last update @2012-02-27 12:36:43'),
	(760010,'jsp.workflowConsole.workflowObjects.object','Last update @2012-02-27 12:36:43'),
	(760011,'jsp.workflowConsole.workflowObjects.startTime','Last update @2012-02-27 12:36:44'),
	(760012,'jsp.workflowConsole.goals','Last update @2012-02-27 12:36:44'),
	(760013,'jsp.workflowConsole.goals.target','Last update @2012-02-27 12:36:44'),
	(760014,'jsp.workflowConsole.goals.sequence','Last update @2012-02-27 12:36:44'),
	(760015,'jsp.workflowConsole.goals.task','Last update @2012-02-27 12:36:44'),
	(760016,'jsp.workflowConsole.goals.user','Last update @2012-02-27 12:36:45'),
	(770000,'jsp.yourcontent.pagetitle','Last update @2012-02-27 12:36:45'),
	(770001,'jsp.yourcontent.sponsors','Last update @2012-02-27 12:36:45'),
	(770002,'jsp.yourcontent.adminAnnounce','Last update @2012-02-27 12:36:45'),
	(770003,'jsp.yourcontent.bar.assignments','Last update @2012-02-27 12:36:46'),
	(770004,'jsp.yourcontent.deadline','Last update @2012-02-27 12:36:46');

/*!40000 ALTER TABLE `text_resource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table token
# ------------------------------------------------------------

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;

INSERT INTO `token` (`id`, `name`, `description`)
VALUES
	(1,'<username>','Full name of the message recepient'),
	(2,'<projectname>','Name of the Project'),
	(3,'<productname>','Name of the Product'),
	(4,'<targetname>','Name of the Target'),
	(5,'<casename>','Name of the Case'),
	(6,'<casestatus>','Value of Case Status: open, closed, etc'),
	(7,'<caseid>','ID of the Case'),
	(8,'<taskduetime>','Due time of Assignment'),
	(9,'<daysbeforedue>','Number of days before assignment due time'),
	(10,'<daysafterdue>','Number of days after assignment due time'),
	(11,'<projectstarttime>','Start time of Project'),
	(12,'<goalname>','Name of Goal'),
	(13,'<taskname>','Name of Task'),
	(14,'<goalduetime>','Due time of the Goal'),
	(15,'<contenttitle>','Title of the content'),
	(16,'<projectadmin>','Full name of the project admin user');

/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tool
# ------------------------------------------------------------

LOCK TABLES `tool` WRITE;
/*!40000 ALTER TABLE `tool` DISABLE KEYS */;

INSERT INTO `tool` (`id`, `name`, `label`, `description`, `access_matrix_id`, `action`, `task_type`)
VALUES
	(3,'journal review','Journal Review','Used for staff review of journal content',NULL,'journalReview.do',3),
	(4,'journal peer review','Journal Peer Review','Used for peer review of journal content',NULL,'journalPeerReview.do',4),
	(6,'survey create','Survey Creation','Used for creating survey content',NULL,'surveyCreate.do',6),
	(7,'survey edit','Survey Edit','Used for editing survey content',NULL,'surveyEdit.do',7),
	(8,'survey review','Survey Review','Used for staff review of survey content',NULL,'surveyReview.do',8),
	(9,'survey peer review','Survey Peer Review','Used for peer review of survey content',NULL,'surveyPeerReview.do',9),
	(10,'survey view','Survey Display','Used for display survey content',NULL,'surveyView.do',10),
	(11,'payment','Payment','Used for recording payment info',NULL,'contentPayment.do',11),
	(12,'approve','Approve','Used for recording approval info',NULL,'contentApproval.do',12),
	(2,'journal edit','Journal Edit','UI tool for editing journal content',NULL,'journalEdit.do',2),
	(5,'journal view','Journal Display','Used for display of journal content',NULL,'journalView.do',5),
	(1,'journal create','Journal Creation','UI tool for creating journal content',NULL,'journalCreate.do',1),
	(13,'journal pr review','Journal Peer-Review Review','Review of journal peer review',NULL,'journalPRReviews.do',13),
	(14,'survey pr review','Survey Peer-Review Review','Review of survey peer review',NULL,'surveyPRReview.do',14),
	(15,'start horse','Start Horse','Approve to start the workflow of a horse',NULL,'horseApproval.do',15),
	(16,'journal review response','Journal Review Response','Author uses this tool to respond to reviewer\'s feedback',NULL,'journalReviewResponse.do',16),
	(17,'survey review response','Survey Review Response','Author uses this tool to respond to reviewer\'s feedback',NULL,'surveyReviewResponse.do',17),
	(18,'journal overall review','Journal Overall Review','Admin uses this tool to do overall review on journal content',NULL,'journalOverallReview.do',18),
	(19,'survey overall review','Survey Overall Review','Admin uses this tool to do overall review on survey content',NULL,'surveyOverallReview.do',19);

/*!40000 ALTER TABLE `tool` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ttags
# ------------------------------------------------------------

LOCK TABLES `ttags` WRITE;
/*!40000 ALTER TABLE `ttags` DISABLE KEYS */;

INSERT INTO `ttags` (`id`, `term`, `description`)
VALUES
	(1,'Asia',''),
	(2,'Europe',''),
	(3,'North America',''),
	(4,'South America',''),
	(5,'Africa','');

/*!40000 ALTER TABLE `ttags` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `username`, `email`, `password`, `last_password_change_time`, `create_time`, `last_logout_time`, `status`, `timezone`, `language_id`, `msgboard_id`, `forward_inbox_msg`, `number_msgs_per_screen`, `first_name`, `last_name`, `phone`, `cell_phone`, `address`, `bio`, `photo`, `location`, `email_detail_level`, `default_project_id`, `last_login_time`, `site_admin`, `organization_id`)
VALUES
	(1,'admin','admin@gi.org','test',NULL,'2010-06-10 14:40:18',NULL,1,0,1,4,1,10,'Adam','Boss','123-456-7890','123-456-7890',NULL,'I am the system admin.',NULL,'123 Kellie Jean Ct, Great Falls, VA 22066',1,-1,NULL,1,1),
	(2,'editor1','nsmith@gi.org','test',NULL,'2010-06-11 11:44:41',NULL,1,-5,1,NULL,1,10,'Nancy','Smith','703-743-3334','202-743-3334',NULL,'Nancy is an experienced editor.','upload_files/user_2.jpg','Washington',1,-1,NULL,0,1),
	(3,'reviewer1','reviewer1@gi.org','test',NULL,'2010-06-11 15:34:01','2010-07-29 01:42:59',1,0,1,6,0,10,'Lucy','Small','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,0,1,'2010-07-29 01:42:37',0,1),
	(4,'reviewer2','reviewer2@gi.org','test',NULL,'2010-06-11 15:35:23',NULL,1,0,1,NULL,0,10,'Jimmy','Regan','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(5,'manager1','manager1@gi.org','test',NULL,'2010-06-11 15:36:27','2012-03-05 11:01:15',1,0,1,0,0,10,'Chris','Willows','123-456-7890','123-456-7890','','',NULL,'',1,1,'2012-03-05 13:28:03',0,1),
	(6,'manager2','manager2@gi.org','test',NULL,'2010-06-11 15:37:13',NULL,1,0,2,0,0,10,'Sam','Lynch','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,1,2,'2012-03-05 11:58:32',0,1),
	(7,'support1','support1@gi.org','test',NULL,'2010-06-11 15:38:05',NULL,1,0,1,NULL,0,10,'Sean','Park','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(8,'support2','support2@gi.org','test',NULL,'2010-06-11 15:38:45',NULL,1,0,1,NULL,0,10,'Leo','Leonsis',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(9,'researcher1.us','researcher1.us@gi.org','test',NULL,'2010-06-11 15:39:34','2012-03-05 13:14:55',1,0,1,0,0,10,'John','Smith','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,1,1,'2012-03-05 13:32:27',0,1),
	(10,'researcher1.ar','researcher1.ar@gi.org','test',NULL,'2010-06-11 15:40:27','2012-03-05 13:15:03',1,0,2,0,0,10,'David','Delpotro','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,0,1,'2012-03-05 13:15:10',0,1),
	(11,'researcher1.bz','researcher1.bz@gi.org','test',NULL,'2010-06-11 15:41:53',NULL,1,NULL,1,NULL,0,10,'Fernando','Gangzalaz','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,0,-1,NULL,0,1),
	(12,'researcher1.cn','researcher1.cn@gi.org','test',NULL,'2010-06-11 15:43:07',NULL,1,NULL,1,NULL,0,10,'Hua','Wang','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,0,-1,NULL,0,1),
	(13,'reporter1.ar','reporter1.ar@gi.org','test',NULL,'2010-06-11 15:44:20',NULL,1,NULL,1,NULL,0,10,'Jeff','Julio','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,0,-1,NULL,0,1),
	(14,'reporter1.bz','reporter1.bz@gi.org','test',NULL,'2010-06-11 15:45:43',NULL,1,NULL,1,NULL,0,10,'Frank','Ferry','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,0,-1,NULL,0,1),
	(15,'reporter1.cn','reporter1.cn@gi.org','test',NULL,'2010-06-11 15:46:32',NULL,1,NULL,1,NULL,0,10,'Meng','Li','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,0,-1,NULL,0,1),
	(16,'reporter1.us','reporter1.us@gi.org','test',NULL,'2010-06-11 15:48:30','2010-08-08 10:59:53',1,0,1,3,0,10,'Betty','Young','123-456-7890','123-456-7890',NULL,NULL,NULL,NULL,1,1,'2012-02-22 00:10:11',0,1),
	(17,'pr02','pr2@xyz.com','test',NULL,'2010-06-11 15:49:29',NULL,1,NULL,1,NULL,0,10,'Victor','Bush',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(18,'pr03','pr3@xyz.com','test',NULL,'2010-06-11 15:50:42',NULL,1,NULL,1,NULL,0,10,'Kate','Ford','888-888-8888','888-888-8888',NULL,NULL,NULL,NULL,0,-1,NULL,0,1),
	(19,'pr04','pr4@xyz.com','test',NULL,'2010-06-11 15:52:52',NULL,1,NULL,1,NULL,0,10,'Kyle','Washington',NULL,NULL,NULL,NULL,NULL,NULL,0,-1,NULL,0,1),
	(20,'editor2','editor2@gi.org','test',NULL,'2010-06-11 15:53:28',NULL,1,NULL,1,NULL,1,10,'Mindy','Clark',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(21,'pr01','pr1@xyz.com','test',NULL,'2010-06-11 15:54:42',NULL,1,0,1,NULL,0,10,'Mike','Carter','888-888-8888','888-888-8888',NULL,'Outstanding performance',NULL,NULL,1,-1,NULL,0,1),
	(22,'reporter1.uk','jbrown@abc.com','test',NULL,'2010-06-13 18:32:00',NULL,1,-5,1,NULL,1,10,'Jeff','Brown',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(23,'researcher1.uk','jcharles@abc.com','test',NULL,'2010-06-13 18:32:56',NULL,1,-5,1,NULL,1,10,'Jay','Charles',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(24,'reporter1.tw','kliu@tu.org','test',NULL,'2010-06-13 18:34:10',NULL,1,-5,2,NULL,1,10,'Kevin','Liu',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(25,'researcher1.tw','awang@tu.edu','test',NULL,'2010-06-13 18:35:07',NULL,1,-5,2,NULL,1,10,'Ann','Wang',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1),
	(32,'DELETED-32','','',NULL,'2010-06-15 17:05:19',NULL,2,-5,1,NULL,0,10,'','','','','','','','',0,-1,NULL,0,1),
	(31,'DELETED-31','','',NULL,'2010-06-15 16:04:26',NULL,2,-5,1,NULL,0,10,'','','','','','','','',0,-1,NULL,0,1),
	(0,'system','','',NULL,'0000-00-00 00:00:00',NULL,1,NULL,1,NULL,0,10,'Indaba','System',NULL,NULL,NULL,NULL,NULL,NULL,1,-1,NULL,0,1);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table userfinder
# ------------------------------------------------------------



# Dump of table userfinder_event
# ------------------------------------------------------------



# Dump of table view_matrix
# ------------------------------------------------------------

LOCK TABLES `view_matrix` WRITE;
/*!40000 ALTER TABLE `view_matrix` DISABLE KEYS */;

INSERT INTO `view_matrix` (`id`, `name`, `description`, `default_value`)
VALUES
	(1,'Standard','The standard view permissions',2);

/*!40000 ALTER TABLE `view_matrix` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table view_permission
# ------------------------------------------------------------

LOCK TABLES `view_permission` WRITE;
/*!40000 ALTER TABLE `view_permission` DISABLE KEYS */;

INSERT INTO `view_permission` (`id`, `view_matrix_id`, `subject_role_id`, `target_role_id`, `permission`)
VALUES
	(1,1,2,2,3),
	(3,1,2,3,3),
	(4,1,2,4,3),
	(5,1,2,5,3),
	(6,1,2,6,3),
	(7,1,2,7,3),
	(8,1,2,8,3),
	(9,1,2,9,3),
	(14,1,5,7,0),
	(15,1,5,6,0),
	(16,1,5,5,0),
	(19,1,6,5,0),
	(20,1,6,6,0),
	(21,1,6,7,0),
	(22,1,7,7,0),
	(23,1,7,6,0),
	(24,1,7,5,0);

/*!40000 ALTER TABLE `view_permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflow
# ------------------------------------------------------------

LOCK TABLES `workflow` WRITE;
/*!40000 ALTER TABLE `workflow` DISABLE KEYS */;

INSERT INTO `workflow` (`id`, `name`, `description`, `created_time`, `created_by_user_id`, `total_duration`)
VALUES
	(1,'Notebook Workflow 2010','Workflow for notebooks','2010-06-11 13:54:49',1,63),
	(2,'Scorecard 2010 Workflow','Workflow for scorecard 2010','2010-06-14 16:58:45',1,60),
	(3,'Energy Policy 2010 Workflow','Workflow for Energy Policy Content - used by both EPA and EPS','2010-06-15 11:36:41',1,38);

/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflow_object
# ------------------------------------------------------------

LOCK TABLES `workflow_object` WRITE;
/*!40000 ALTER TABLE `workflow_object` DISABLE KEYS */;

INSERT INTO `workflow_object` (`id`, `workflow_id`, `start_time`, `status`)
VALUES
	(1,1,'2010-06-16 10:22:55',2),
	(2,2,'2010-06-16 10:33:42',2),
	(3,1,'2010-06-16 10:33:49',2),
	(4,1,'2010-06-16 10:33:49',2),
	(5,1,'2010-06-16 10:33:49',2),
	(6,2,'2010-06-16 10:33:55',2),
	(7,2,'2010-06-16 10:33:55',2),
	(8,2,'2010-06-16 10:33:55',2);

/*!40000 ALTER TABLE `workflow_object` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflow_sequence
# ------------------------------------------------------------

LOCK TABLES `workflow_sequence` WRITE;
/*!40000 ALTER TABLE `workflow_sequence` DISABLE KEYS */;

INSERT INTO `workflow_sequence` (`id`, `workflow_id`, `name`, `description`)
VALUES
	(1,1,'Main','The main sequence of the notebook workflow'),
	(2,2,'Main','The main sequence'),
	(4,2,'Pay1','1st payment'),
	(5,2,'Pay2','2nd payment'),
	(6,2,'PR Edit','Edit peer reviews'),
	(7,2,'Edit','Content edit'),
	(8,2,'Finish','Finishing up'),
	(11,3,'Main','The main sequence');

/*!40000 ALTER TABLE `workflow_sequence` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
