-- phpMyAdmin SQL Dump
-- version 3.3.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 26, 2012 at 10:41 AM
-- Server version: 5.5.9
-- PHP Version: 5.3.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `indaba`
--

-- --------------------------------------------------------

--
-- Table structure for table `access_matrix`
--

CREATE TABLE `access_matrix` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT 'Each matrix has a unique name',
  `description` varchar(255) DEFAULT NULL,
  `default_value` tinyint(4) DEFAULT NULL COMMENT 'Default permission value if the value for a role/right combination is not explicitly defined.\n\n0 - No\n1 - Yes\n2 - Undefined',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='An access matrix defines permission values for role/right co' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `access_matrix`
--

INSERT INTO `access_matrix` VALUES(1, 'Project Default', 'The default access matrix for projects', 1);

-- --------------------------------------------------------

--
-- Table structure for table `access_permission`
--

CREATE TABLE `access_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_matrix_id` int(11) NOT NULL COMMENT 'ID of the access matrix that this permission belongs to',
  `role_id` int(11) NOT NULL COMMENT 'ID of the role in role/right combination',
  `rights_id` int(11) NOT NULL COMMENT 'ID of the right in role/right combination',
  `permission` tinyint(4) NOT NULL COMMENT 'Permission value:\n0 - No\n1 - Yes\n2 - Undefined',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_mrr` (`access_matrix_id`,`role_id`,`rights_id`),
  KEY `fk_permission_matrix1` (`access_matrix_id`),
  KEY `fk_permission_role1` (`role_id`),
  KEY `fk_permission_right` (`rights_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This defines the permission value for a role/right combinati' AUTO_INCREMENT=141 ;

--
-- Dumping data for table `access_permission`
--

INSERT INTO `access_permission` VALUES(1, 1, 2, 1, 1);
INSERT INTO `access_permission` VALUES(2, 1, 5, 3, 0);
INSERT INTO `access_permission` VALUES(3, 1, 6, 3, 0);
INSERT INTO `access_permission` VALUES(4, 1, 7, 3, 0);
INSERT INTO `access_permission` VALUES(5, 1, 7, 4, 0);
INSERT INTO `access_permission` VALUES(6, 1, 6, 4, 0);
INSERT INTO `access_permission` VALUES(7, 1, 5, 4, 0);
INSERT INTO `access_permission` VALUES(8, 1, 5, 6, 0);
INSERT INTO `access_permission` VALUES(9, 1, 6, 6, 0);
INSERT INTO `access_permission` VALUES(12, 1, 7, 6, 0);
INSERT INTO `access_permission` VALUES(13, 1, 7, 7, 0);
INSERT INTO `access_permission` VALUES(14, 1, 6, 7, 0);
INSERT INTO `access_permission` VALUES(15, 1, 5, 7, 0);
INSERT INTO `access_permission` VALUES(16, 1, 4, 7, 0);
INSERT INTO `access_permission` VALUES(17, 1, 3, 7, 0);
INSERT INTO `access_permission` VALUES(18, 1, 8, 7, 0);
INSERT INTO `access_permission` VALUES(19, 1, 9, 7, 0);
INSERT INTO `access_permission` VALUES(20, 1, 3, 9, 0);
INSERT INTO `access_permission` VALUES(21, 1, 4, 9, 0);
INSERT INTO `access_permission` VALUES(22, 1, 5, 9, 0);
INSERT INTO `access_permission` VALUES(24, 1, 6, 9, 0);
INSERT INTO `access_permission` VALUES(25, 1, 7, 9, 0);
INSERT INTO `access_permission` VALUES(26, 1, 8, 9, 0);
INSERT INTO `access_permission` VALUES(27, 1, 9, 9, 0);
INSERT INTO `access_permission` VALUES(29, 1, 9, 10, 1);
INSERT INTO `access_permission` VALUES(30, 1, 7, 10, 0);
INSERT INTO `access_permission` VALUES(31, 1, 6, 10, 0);
INSERT INTO `access_permission` VALUES(32, 1, 5, 10, 0);
INSERT INTO `access_permission` VALUES(33, 1, 5, 11, 0);
INSERT INTO `access_permission` VALUES(34, 1, 6, 11, 0);
INSERT INTO `access_permission` VALUES(35, 1, 7, 11, 0);
INSERT INTO `access_permission` VALUES(36, 1, 7, 12, 0);
INSERT INTO `access_permission` VALUES(37, 1, 6, 12, 0);
INSERT INTO `access_permission` VALUES(38, 1, 5, 12, 0);
INSERT INTO `access_permission` VALUES(39, 1, 3, 13, 0);
INSERT INTO `access_permission` VALUES(40, 1, 4, 13, 0);
INSERT INTO `access_permission` VALUES(41, 1, 5, 13, 0);
INSERT INTO `access_permission` VALUES(42, 1, 6, 13, 0);
INSERT INTO `access_permission` VALUES(43, 1, 7, 13, 0);
INSERT INTO `access_permission` VALUES(45, 1, 8, 13, 1);
INSERT INTO `access_permission` VALUES(46, 1, 9, 13, 1);
INSERT INTO `access_permission` VALUES(47, 1, 5, 14, 0);
INSERT INTO `access_permission` VALUES(48, 1, 6, 14, 0);
INSERT INTO `access_permission` VALUES(49, 1, 7, 14, 0);
INSERT INTO `access_permission` VALUES(50, 1, 7, 15, 0);
INSERT INTO `access_permission` VALUES(51, 1, 6, 15, 0);
INSERT INTO `access_permission` VALUES(53, 1, 5, 15, 0);
INSERT INTO `access_permission` VALUES(54, 1, 5, 16, 0);
INSERT INTO `access_permission` VALUES(55, 1, 6, 16, 0);
INSERT INTO `access_permission` VALUES(56, 1, 8, 16, 0);
INSERT INTO `access_permission` VALUES(57, 1, 7, 16, 0);
INSERT INTO `access_permission` VALUES(58, 1, 7, 19, 0);
INSERT INTO `access_permission` VALUES(59, 1, 6, 19, 0);
INSERT INTO `access_permission` VALUES(60, 1, 5, 19, 0);
INSERT INTO `access_permission` VALUES(63, 1, 5, 20, 0);
INSERT INTO `access_permission` VALUES(62, 1, 6, 20, 0);
INSERT INTO `access_permission` VALUES(64, 1, 7, 20, 0);
INSERT INTO `access_permission` VALUES(65, 1, 7, 21, 0);
INSERT INTO `access_permission` VALUES(66, 1, 6, 21, 0);
INSERT INTO `access_permission` VALUES(67, 1, 5, 21, 0);
INSERT INTO `access_permission` VALUES(68, 1, 8, 21, 0);
INSERT INTO `access_permission` VALUES(69, 1, 4, 21, 0);
INSERT INTO `access_permission` VALUES(70, 1, 3, 21, 0);
INSERT INTO `access_permission` VALUES(71, 1, 9, 21, 1);
INSERT INTO `access_permission` VALUES(72, 1, 3, 22, 0);
INSERT INTO `access_permission` VALUES(73, 1, 5, 22, 0);
INSERT INTO `access_permission` VALUES(74, 1, 6, 22, 0);
INSERT INTO `access_permission` VALUES(75, 1, 7, 22, 0);
INSERT INTO `access_permission` VALUES(76, 1, 8, 22, 0);
INSERT INTO `access_permission` VALUES(77, 1, 8, 23, 0);
INSERT INTO `access_permission` VALUES(78, 1, 7, 23, 0);
INSERT INTO `access_permission` VALUES(79, 1, 6, 23, 0);
INSERT INTO `access_permission` VALUES(80, 1, 5, 23, 0);
INSERT INTO `access_permission` VALUES(81, 1, 4, 23, 0);
INSERT INTO `access_permission` VALUES(82, 1, 3, 23, 0);
INSERT INTO `access_permission` VALUES(83, 1, 3, 24, 0);
INSERT INTO `access_permission` VALUES(84, 1, 4, 24, 0);
INSERT INTO `access_permission` VALUES(85, 1, 5, 24, 0);
INSERT INTO `access_permission` VALUES(86, 1, 6, 24, 0);
INSERT INTO `access_permission` VALUES(87, 1, 7, 24, 0);
INSERT INTO `access_permission` VALUES(88, 1, 8, 24, 0);
INSERT INTO `access_permission` VALUES(89, 1, 8, 25, 0);
INSERT INTO `access_permission` VALUES(90, 1, 7, 25, 0);
INSERT INTO `access_permission` VALUES(91, 1, 6, 25, 0);
INSERT INTO `access_permission` VALUES(92, 1, 5, 25, 0);
INSERT INTO `access_permission` VALUES(93, 1, 4, 25, 0);
INSERT INTO `access_permission` VALUES(94, 1, 3, 25, 0);
INSERT INTO `access_permission` VALUES(95, 1, 3, 26, 0);
INSERT INTO `access_permission` VALUES(96, 1, 4, 26, 0);
INSERT INTO `access_permission` VALUES(97, 1, 5, 26, 0);
INSERT INTO `access_permission` VALUES(98, 1, 6, 26, 0);
INSERT INTO `access_permission` VALUES(99, 1, 7, 26, 0);
INSERT INTO `access_permission` VALUES(100, 1, 8, 26, 0);
INSERT INTO `access_permission` VALUES(101, 1, 8, 27, 0);
INSERT INTO `access_permission` VALUES(102, 1, 7, 27, 0);
INSERT INTO `access_permission` VALUES(103, 1, 6, 27, 0);
INSERT INTO `access_permission` VALUES(104, 1, 5, 27, 0);
INSERT INTO `access_permission` VALUES(105, 1, 4, 27, 0);
INSERT INTO `access_permission` VALUES(106, 1, 3, 27, 0);
INSERT INTO `access_permission` VALUES(107, 1, 4, 28, 0);
INSERT INTO `access_permission` VALUES(108, 1, 5, 28, 0);
INSERT INTO `access_permission` VALUES(109, 1, 6, 28, 0);
INSERT INTO `access_permission` VALUES(110, 1, 8, 28, 0);
INSERT INTO `access_permission` VALUES(111, 1, 7, 28, 0);
INSERT INTO `access_permission` VALUES(112, 1, 7, 29, 0);
INSERT INTO `access_permission` VALUES(114, 1, 8, 29, 0);
INSERT INTO `access_permission` VALUES(115, 1, 6, 29, 0);
INSERT INTO `access_permission` VALUES(116, 1, 5, 29, 0);
INSERT INTO `access_permission` VALUES(117, 1, 4, 29, 0);
INSERT INTO `access_permission` VALUES(118, 1, 3, 29, 0);
INSERT INTO `access_permission` VALUES(119, 1, 3, 30, 0);
INSERT INTO `access_permission` VALUES(120, 1, 4, 30, 0);
INSERT INTO `access_permission` VALUES(121, 1, 5, 30, 0);
INSERT INTO `access_permission` VALUES(122, 1, 6, 30, 0);
INSERT INTO `access_permission` VALUES(123, 1, 7, 30, 0);
INSERT INTO `access_permission` VALUES(124, 1, 8, 30, 0);
INSERT INTO `access_permission` VALUES(125, 1, 8, 31, 0);
INSERT INTO `access_permission` VALUES(127, 1, 7, 31, 0);
INSERT INTO `access_permission` VALUES(128, 1, 6, 31, 0);
INSERT INTO `access_permission` VALUES(129, 1, 5, 31, 0);
INSERT INTO `access_permission` VALUES(130, 1, 4, 31, 0);
INSERT INTO `access_permission` VALUES(131, 1, 3, 31, 0);
INSERT INTO `access_permission` VALUES(132, 1, 8, 32, 0);
INSERT INTO `access_permission` VALUES(133, 1, 8, 33, 0);
INSERT INTO `access_permission` VALUES(134, 1, 7, 33, 0);
INSERT INTO `access_permission` VALUES(135, 1, 6, 33, 0);
INSERT INTO `access_permission` VALUES(136, 1, 5, 33, 0);
INSERT INTO `access_permission` VALUES(137, 1, 6, 36, 0);
INSERT INTO `access_permission` VALUES(138, 1, 7, 36, 0);
INSERT INTO `access_permission` VALUES(139, 1, 6, 37, 0);
INSERT INTO `access_permission` VALUES(140, 1, 7, 37, 0);

-- --------------------------------------------------------

--
-- Table structure for table `announcement`
--

CREATE TABLE `announcement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT 'Title of the announcement',
  `body` text COMMENT 'Announcement body',
  `created_by_user_id` int(11) NOT NULL COMMENT 'ID of user who created this announcement',
  `created_time` datetime NOT NULL COMMENT 'Time at which the announcement was created',
  `expiration` datetime NOT NULL COMMENT 'When this announcement will expire',
  `active` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Whether the announcement is active\n\n0 - inactive\n1 - active',
  PRIMARY KEY (`id`),
  KEY `fk_announcement_user1` (`created_by_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `announcement`
--


-- --------------------------------------------------------

--
-- Table structure for table `answer_object_choice`
--

CREATE TABLE `answer_object_choice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `choices` bigint(20) unsigned NOT NULL COMMENT 'Bit mask that contains all selected choices. ',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=61 ;

--
-- Dumping data for table `answer_object_choice`
--

INSERT INTO `answer_object_choice` VALUES(1, 0);
INSERT INTO `answer_object_choice` VALUES(2, 0);
INSERT INTO `answer_object_choice` VALUES(3, 0);
INSERT INTO `answer_object_choice` VALUES(4, 0);
INSERT INTO `answer_object_choice` VALUES(5, 0);
INSERT INTO `answer_object_choice` VALUES(6, 0);
INSERT INTO `answer_object_choice` VALUES(7, 0);
INSERT INTO `answer_object_choice` VALUES(8, 0);
INSERT INTO `answer_object_choice` VALUES(9, 0);
INSERT INTO `answer_object_choice` VALUES(10, 0);
INSERT INTO `answer_object_choice` VALUES(11, 0);
INSERT INTO `answer_object_choice` VALUES(12, 0);
INSERT INTO `answer_object_choice` VALUES(13, 0);
INSERT INTO `answer_object_choice` VALUES(14, 0);
INSERT INTO `answer_object_choice` VALUES(15, 0);
INSERT INTO `answer_object_choice` VALUES(16, 0);
INSERT INTO `answer_object_choice` VALUES(17, 0);
INSERT INTO `answer_object_choice` VALUES(18, 0);
INSERT INTO `answer_object_choice` VALUES(19, 0);
INSERT INTO `answer_object_choice` VALUES(20, 0);
INSERT INTO `answer_object_choice` VALUES(21, 0);
INSERT INTO `answer_object_choice` VALUES(22, 0);
INSERT INTO `answer_object_choice` VALUES(23, 2);
INSERT INTO `answer_object_choice` VALUES(24, 2);
INSERT INTO `answer_object_choice` VALUES(25, 2);
INSERT INTO `answer_object_choice` VALUES(26, 2);
INSERT INTO `answer_object_choice` VALUES(27, 2);
INSERT INTO `answer_object_choice` VALUES(28, 2);
INSERT INTO `answer_object_choice` VALUES(29, 1);
INSERT INTO `answer_object_choice` VALUES(30, 6);
INSERT INTO `answer_object_choice` VALUES(31, 1);
INSERT INTO `answer_object_choice` VALUES(32, 2);
INSERT INTO `answer_object_choice` VALUES(33, 1);
INSERT INTO `answer_object_choice` VALUES(34, 2);
INSERT INTO `answer_object_choice` VALUES(35, 1);
INSERT INTO `answer_object_choice` VALUES(36, 1);
INSERT INTO `answer_object_choice` VALUES(37, 2);
INSERT INTO `answer_object_choice` VALUES(38, 1);
INSERT INTO `answer_object_choice` VALUES(39, 2);
INSERT INTO `answer_object_choice` VALUES(40, 4);
INSERT INTO `answer_object_choice` VALUES(41, 1);
INSERT INTO `answer_object_choice` VALUES(42, 2);
INSERT INTO `answer_object_choice` VALUES(43, 2);
INSERT INTO `answer_object_choice` VALUES(44, 2);
INSERT INTO `answer_object_choice` VALUES(45, 2);
INSERT INTO `answer_object_choice` VALUES(46, 2);
INSERT INTO `answer_object_choice` VALUES(47, 1);
INSERT INTO `answer_object_choice` VALUES(48, 6);
INSERT INTO `answer_object_choice` VALUES(49, 1);
INSERT INTO `answer_object_choice` VALUES(50, 2);
INSERT INTO `answer_object_choice` VALUES(51, 1);
INSERT INTO `answer_object_choice` VALUES(52, 2);
INSERT INTO `answer_object_choice` VALUES(53, 1);
INSERT INTO `answer_object_choice` VALUES(54, 1);
INSERT INTO `answer_object_choice` VALUES(55, 2);
INSERT INTO `answer_object_choice` VALUES(56, 1);
INSERT INTO `answer_object_choice` VALUES(57, 2);
INSERT INTO `answer_object_choice` VALUES(58, 4);
INSERT INTO `answer_object_choice` VALUES(59, 1);
INSERT INTO `answer_object_choice` VALUES(60, 2);

-- --------------------------------------------------------

--
-- Table structure for table `answer_object_float`
--

CREATE TABLE `answer_object_float` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `answer_object_float`
--

INSERT INTO `answer_object_float` VALUES(1, NULL);
INSERT INTO `answer_object_float` VALUES(2, NULL);
INSERT INTO `answer_object_float` VALUES(3, NULL);
INSERT INTO `answer_object_float` VALUES(4, 45);
INSERT INTO `answer_object_float` VALUES(5, 45);

-- --------------------------------------------------------

--
-- Table structure for table `answer_object_integer`
--

CREATE TABLE `answer_object_integer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `answer_object_integer`
--

INSERT INTO `answer_object_integer` VALUES(1, NULL);
INSERT INTO `answer_object_integer` VALUES(2, NULL);
INSERT INTO `answer_object_integer` VALUES(3, 45);
INSERT INTO `answer_object_integer` VALUES(4, 45);

-- --------------------------------------------------------

--
-- Table structure for table `answer_object_text`
--

CREATE TABLE `answer_object_text` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `answer_object_text`
--

INSERT INTO `answer_object_text` VALUES(1, 'twer wert wert werterter');
INSERT INTO `answer_object_text` VALUES(2, 'twer wert wert werterter');

-- --------------------------------------------------------

--
-- Table structure for table `answer_type_choice`
--

CREATE TABLE `answer_type_choice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

--
-- Dumping data for table `answer_type_choice`
--

INSERT INTO `answer_type_choice` VALUES(1);
INSERT INTO `answer_type_choice` VALUES(2);
INSERT INTO `answer_type_choice` VALUES(3);
INSERT INTO `answer_type_choice` VALUES(4);
INSERT INTO `answer_type_choice` VALUES(5);
INSERT INTO `answer_type_choice` VALUES(6);
INSERT INTO `answer_type_choice` VALUES(7);
INSERT INTO `answer_type_choice` VALUES(8);
INSERT INTO `answer_type_choice` VALUES(9);
INSERT INTO `answer_type_choice` VALUES(10);
INSERT INTO `answer_type_choice` VALUES(11);
INSERT INTO `answer_type_choice` VALUES(12);
INSERT INTO `answer_type_choice` VALUES(13);
INSERT INTO `answer_type_choice` VALUES(14);
INSERT INTO `answer_type_choice` VALUES(15);
INSERT INTO `answer_type_choice` VALUES(16);
INSERT INTO `answer_type_choice` VALUES(17);
INSERT INTO `answer_type_choice` VALUES(18);
INSERT INTO `answer_type_choice` VALUES(19);
INSERT INTO `answer_type_choice` VALUES(20);
INSERT INTO `answer_type_choice` VALUES(21);
INSERT INTO `answer_type_choice` VALUES(22);
INSERT INTO `answer_type_choice` VALUES(23);
INSERT INTO `answer_type_choice` VALUES(24);

-- --------------------------------------------------------

--
-- Table structure for table `answer_type_float`
--

CREATE TABLE `answer_type_float` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `min_value` float NOT NULL,
  `max_value` float NOT NULL,
  `default_value` float DEFAULT NULL,
  `criteria` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `answer_type_float`
--

INSERT INTO `answer_type_float` VALUES(1, 0, 1000, NULL, 'Average hourly wage is across all states in the nation.');
INSERT INTO `answer_type_float` VALUES(2, 1, 100, NULL, 'Major cities are the executive regions directly under the central government');
INSERT INTO `answer_type_float` VALUES(3, 1, 100, NULL, 'Major cities are the executive regions directly under the central government');

-- --------------------------------------------------------

--
-- Table structure for table `answer_type_integer`
--

CREATE TABLE `answer_type_integer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `min_value` int(11) NOT NULL,
  `max_value` int(11) NOT NULL,
  `default_value` int(11) DEFAULT NULL,
  `criteria` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Structure of the Number answer type of indicator' AUTO_INCREMENT=3 ;

--
-- Dumping data for table `answer_type_integer`
--

INSERT INTO `answer_type_integer` VALUES(1, 1, 100, NULL, 'States and provinces are the executive regions directly under the central government');
INSERT INTO `answer_type_integer` VALUES(2, 1, 100, NULL, 'Major cities are the executive regions directly under the central government');

-- --------------------------------------------------------

--
-- Table structure for table `answer_type_text`
--

CREATE TABLE `answer_type_text` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `min_chars` int(11) NOT NULL,
  `max_chars` int(11) NOT NULL,
  `criteria` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Structure of answer type Text' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `answer_type_text`
--

INSERT INTO `answer_type_text` VALUES(1, 1, 1000, 'The biggest cities are based on the size of populations');

-- --------------------------------------------------------

--
-- Table structure for table `apicall`
--

CREATE TABLE `apicall` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `call_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the call was made',
  `func` varchar(105) NOT NULL COMMENT 'api function called',
  `url` varchar(255) NOT NULL COMMENT 'Original request url',
  `ip_addr` varchar(45) DEFAULT NULL COMMENT 'IP of the caller',
  `organization_id` int(11) DEFAULT NULL COMMENT 'org id on the call',
  `key_version` int(11) DEFAULT NULL COMMENT 'key version on the call',
  `product_id` int(11) DEFAULT NULL,
  `horse_id` int(11) DEFAULT NULL COMMENT 'horse ID of the requested content. ',
  `authn_code` tinyint(4) NOT NULL COMMENT '0 = ok;\n1 = bad function;\n2 = bad syntax;\n3 = bad org;\n4 = key not found;\n5 = key expired;\n6 = key revoked;\n7 = bad digest;\n8 = call too old;\n9 = replay;',
  `authz_code` tinyint(4) DEFAULT NULL COMMENT '0 = ok;\n1 = no access;',
  PRIMARY KEY (`id`),
  KEY `fk_apicall_org` (`organization_id`),
  KEY `fk_apicall_horse` (`horse_id`),
  KEY `fk_apicall_prod` (`product_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `apicall`
--


-- --------------------------------------------------------

--
-- Table structure for table `atc_choice`
--

CREATE TABLE `atc_choice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `answer_type_choice_id` int(11) NOT NULL,
  `label` varchar(45) NOT NULL COMMENT 'Display label of the choice',
  `score` int(11) DEFAULT NULL COMMENT 'Score associated with the choice',
  `criteria` text COMMENT 'Text resource ID of the choice''s criteria',
  `weight` int(11) NOT NULL COMMENT 'Weight that defines the display order of the choice ',
  `mask` bigint(20) unsigned NOT NULL COMMENT 'The bit mask representing this choice in the ATC',
  `default_selected` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Whether this choice is selected by default',
  `use_score` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Whether the score is applicable',
  PRIMARY KEY (`id`),
  KEY `fk_atcc_atc` (`answer_type_choice_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Choices for a ATC' AUTO_INCREMENT=72 ;

--
-- Dumping data for table `atc_choice`
--

INSERT INTO `atc_choice` VALUES(1, 1, 'YES', 1000000, 'A YES score is earned when freedom to assemble into groups promoting good governance or anti-corruption is protected by law, regardless of political ideology, religion or objectives. Groups with a history of violence or terrorism (within last ten years) may be banned. Groups sympathetic to or related to banned groups must be allowed if they have no history of violence.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(2, 1, 'NO', 0, 'A NO score is earned when any single non-violent group is legally prohibited from organizing to promote good governance or anti-corruption. These groups may include non-violent separatist groups, political parties or religious groups.', 2, 2, 1, 1);
INSERT INTO `atc_choice` VALUES(5, 2, 'YES', 1000000, 'A YES score is earned if anti-corruption/good governance CSOs face no legal or regulatory restrictions to raise or accept funds from any foreign or domestic sources.  A YES score may still be earned if funds from groups with a history of violence or terrorism (within last ten years) are banned.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(6, 2, 'NO', 0, 'A NO score is earned if there any formal legal or regulatory bans on foreign or domestic funding sources for CSOs focused on anti-corruption or good governance.', 2, 2, 1, 1);
INSERT INTO `atc_choice` VALUES(7, 3, 'YES', 1000000, 'A YES score is earned if anti-corruption/good governance CSOs are required to publicly disclose their sources of funding.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(10, 4, '50', 500000, 'CSOs focused on promoting good governance or anti-corruption must go through formal steps to form, requiring interaction with the state such as licenses or registration. Formation is possible, though there is some burden on the CSO. Some unofficial barriers, such as harassment of minority groups, may occur.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(8, 3, 'NO', 0, 'A NO score is earned if no such public disclosure requirement exists.', 2, 2, 1, 1);
INSERT INTO `atc_choice` VALUES(9, 4, '100', 1000000, 'CSOs focused on promoting good governance or anti-corruption can freely organize with little to no interaction with the government, other than voluntary registration.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(11, 4, '0', 0, 'Other than pro-government groups, CSOs focused on promoting good governance or anti-corruption are effectively prohibited, either by official requirements or by unofficial means, such as intimidation or fear.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(12, 5, '100', 1000000, 'Civil society organizations focused on anti-corruption or good governance are an essential component of the political process. CSOs provide widely valued insights and have political power. Those CSOs play a leading role in shaping public opinion on political matters.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(13, 5, '50', 500000, 'Anti-corruption/good governance CSOs are active, but may not be relevant to political decisions or the policymaking process. Those CSOs are willing to articulate opinions on political matters, but have little access to decision makers. They have some influence over public opinion, but considerably less than political figures.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(19, 5, '0', 0, 'Anti-corruption/good governance CSOs are effectively prohibited from engaging in the political process. Those CSOs are unwilling to take positions on political issues. They are not relevant to changes in public opinion.', 4, 8, 0, 1);
INSERT INTO `atc_choice` VALUES(24, 8, 'YES', 1000000, 'A YES score is earned if there were no CSOs shut down by the government or forced to cease operations because of their work on corruption-related issues during the study period.  YES is a positive score.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(25, 8, 'NO', 0, 'A NO score is earned if any CSO has been effectively shut down by the government or forced to cease operations because of its work on corruption-related issues during the study period.  The causal relationship between the cessation of operations and the CSO''s work may not be explicit, however the burden of proof here is low. If it seems likely that the CSO was forced to cease operations due to its work, then the indicator is scored as a NO. Corruption is defined broadly to include any abuses of power, not just the passing of bribes.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(26, 9, 'Protest', 500000, 'Citizens can organize protests against government corruptions legally.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(27, 9, 'Strike', 200000, 'Citizens can organize strikes against government corruptions legally.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(28, 9, 'Social Media', 200000, 'Citizens can criticize government corruptions using social media such as newspapers, websites, etc.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(29, 9, 'Law Suit', 100000, 'Citizens can file law suits against corrupted government officials.', 4, 8, 0, 1);
INSERT INTO `atc_choice` VALUES(30, 10, 'YES', 1000000, 'A YES score is earned if there is a statutory or other framework enshrined in law that mandates elections at reasonable intervals.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(31, 10, 'NO', 0, 'A NO score is earned if no such framework exists.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(32, 11, '100', 1000000, 'Voting is open to all citizens regardless of race, gender, prior political affiliations, physical disability, or other traditional barriers.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(33, 11, '50', 500000, 'Voting is often open to all citizens regardless of race, gender, prior political affiliations, physical disability, or other traditional barriers, with some exceptions.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(34, 11, '00', 0, 'Voting is not available to some demographics through some form of official or unofficial pressure. Voting may be too dangerous, expensive, or difficult for many people.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(35, 12, '100', 1000000, 'Ballots are secret, or there is a functional equivalent protection, in all cases.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(36, 12, '50', 500000, 'Ballots are secret, or there is a functional equivalent protection, in most cases. Some exceptions to this practice have occurred.  Ballots may be subject to tampering during transport or counting.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(37, 12, '0', 0, 'Ballot preferences are not secret.  Ballots are routinely tampered with during transport and counting.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(38, 13, '100', 1000000, 'Elections are always held according to a regular schedule, or there is a formal democratic process for calling a new election, with deadlines for mandatory elections.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(39, 13, '50', 500000, 'Elections are normally held according to a regular schedule, but there have been recent exceptions. The formal process for calling a new election may be flawed or abused.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(40, 13, '00', 0, 'Elections are called arbitrarily by the government. There is no functioning schedule or deadline for new elections.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(41, 14, 'YES', 1000000, 'Answer YES if the government has laws, regulations about energy conservation and/or alternative energy.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(42, 14, 'NO', 0, 'Answer NO if the government does not have such laws or regulations.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(43, 15, 'Coal', -500000, 'Coal is a major energy source.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(44, 15, 'Nuclear', -100000, 'Nuclear energy is a major energy source.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(45, 15, 'Solar', 0, 'Solar is a major energy source', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(46, 15, 'Wind', 0, 'Wind is a major energy source', 4, 8, 0, 1);
INSERT INTO `atc_choice` VALUES(47, 15, 'Water', -5, 'Water is a major energy source', 5, 16, 0, 1);
INSERT INTO `atc_choice` VALUES(48, 16, 'YES', 1000000, 'Answer YES if the government offers incentives for the use of green energies.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(49, 16, 'NO', 0, 'Answer NO if the government does not offer such programs.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(50, 17, 'Coal', 0, 'Check this if Coal is the primary energy source in the nation.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(51, 17, 'Nuclear', 500000, 'Check this if Nuclear is the primary energy source in the nation.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(52, 17, 'Solar', 1000000, 'Check this if solar energy is the primary energy source in the nation.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(53, 17, 'Wind', 1000000, 'Check this if Wind is the primary energy source in the nation.', 4, 8, 0, 1);
INSERT INTO `atc_choice` VALUES(54, 17, 'Water', 800000, 'Check this if Water is the primary energy source in the nation.', 5, 16, 0, 1);
INSERT INTO `atc_choice` VALUES(55, 18, 'YES', 1000000, 'A YES score is earned if freedom of the press is guaranteed in law, including to all political parties, religions, and ideologies.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(56, 18, 'NO', 0, 'A NO score is earned if any specific publication relating to government affairs is legally banned, or any general topic is prohibited from publication. Specific restrictions on media regarding privacy or slander are allowed, but not if these amount to legal censorship of a general topic, such as corruption or defense.  A NO score is earned if non-government media is prohibited or restricted.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(57, 19, 'YES', 1000000, 'A YES score is earned if freedom of individual speech is guaranteed in law, including to all political parties, religions, and ideologies.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(58, 19, 'NO', 0, 'A NO score is earned if any individual speech is legally prohibited, regardless of topic. Specific exceptions for speech linked with a criminal act, such as a prohibition on death threats, are allowed. However, any non-specific prohibition earns a NO score.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(59, 20, '100', 1000000, 'Print media entities can freely organize with little to no interaction with the government. This score may still be earned if groups or individuals with a history of political violence or terrorism (within last ten years) are banned from forming media entities.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(60, 20, '50', 500000, 'Formation of print media groups is possible, though there is some burden on the media group including overly complicated registration or licensing requirements. Some unofficial barriers, such as harassment of minority groups, may occur.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(61, 20, '000', 0, 'Print media groups are effectively prohibited, either by official requirements or by unofficial means, such as intimidation or fear.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(62, 21, 'YES', 1000000, 'A YES score is earned if there is, in law or in accompanying regulations, a formal process to appeal a denied print media license, including through the courts. A YES score is also earned if no print license is necessary.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(63, 21, 'NO', 0, 'A NO score is earned if there is no appeal process for print media licenses.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(64, 22, '100', 1000000, 'Licenses are not required or licenses can be obtained within two months.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(65, 22, '50', 500000, 'Licensing is required and takes more than two months.  Some groups may be delayed up to six months.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(66, 22, '00', 0, 'Licensing takes close to or more than one year for most groups.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(67, 23, '100', 1000000, 'Licenses are not required or can be obtained at minimal cost to the organization.  Licenses can be obtained on-line or through the mail.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(68, 23, '50', 500000, 'Licenses are required, and impose a financial burden on the organization. Licenses may require a visit to a specific office, such as a regional or national capital.', 2, 2, 0, 1);
INSERT INTO `atc_choice` VALUES(69, 23, '00', 0, 'Licenses are required, and impose a major financial burden on the organization. Licensing costs are prohibitive to the organization.', 3, 4, 0, 1);
INSERT INTO `atc_choice` VALUES(70, 24, 'YES', 1000000, 'A YES score is earned if the right to vote is guaranteed to all citizens of the country (basic age limitations are allowed). A YES score can still be earned if voting procedures are, in practice, inconvenient or unfair.', 1, 1, 0, 1);
INSERT INTO `atc_choice` VALUES(71, 24, 'NO', 0, 'A NO score is earned if suffrage is denied by law to any group of adult citizens for any reason. Citizen is defined broadly, to include all ethnicities, or anyone born in the country.  A NO score is earned if homeless or impoverished people are legally prohibited from voting.', 2, 2, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `atc_choice_intl`
--

CREATE TABLE `atc_choice_intl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atc_choice_id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `label` text NOT NULL,
  `criteria` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_acintl_atclang` (`atc_choice_id`,`language_id`),
  KEY `fk_acintl_atcc` (`atc_choice_id`),
  KEY `fk_acintl_lang` (`language_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `atc_choice_intl`
--


-- --------------------------------------------------------

--
-- Table structure for table `attachment`
--

CREATE TABLE `attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_header_id` int(11) NOT NULL COMMENT 'ID of the content that the file is attached to',
  `name` varchar(255) NOT NULL COMMENT 'Name of the attached file',
  `size` int(11) NOT NULL DEFAULT '0' COMMENT 'file size (bytes)',
  `type` varchar(8) NOT NULL COMMENT 'file type (suffix, i.e. txt, doc, pdf, etc.)',
  `note` varchar(255) DEFAULT NULL COMMENT 'Any notes to be attached',
  `file_path` varchar(255) NOT NULL COMMENT 'Path to the attached file in the file system after uploaded',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'the file upload time',
  `user_id` int(11) DEFAULT '0' COMMENT 'user who uploaded the file',
  PRIMARY KEY (`id`),
  KEY `fk_attachment_content_header1` (`content_header_id`),
  KEY `fk_attachment_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='defines file attachment.' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attachment`
--


-- --------------------------------------------------------

--
-- Table structure for table `cases`
--

CREATE TABLE `cases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `opened_by_user_id` int(11) NOT NULL COMMENT 'opened by this user',
  `assigned_user_id` int(11) DEFAULT NULL COMMENT 'User assigned to be responsible',
  `opened_time` datetime NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text,
  `priority` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 - low\n2 - medium\n3 - high',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Status code of the case.\n0 - open\n1 - closed',
  `substatus` tinyint(4) DEFAULT NULL COMMENT 'Substatus code. \nWhen status is open:\n1 - New\n2 - Assigned\n\nWhen status is closed\n101 - Resolved\n102 - Invalid\n103 - Withdrawn\n104 - Duplicate',
  `block_workflow` tinyint(1) DEFAULT '0' COMMENT 'whether this case blocks workflow from being executed\n0 - no\n1 - yes',
  `block_publishing` tinyint(1) DEFAULT '0' COMMENT 'whether this case blocks publishing\n0 - no\n1 - yes',
  `project_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `horse_id` int(11) DEFAULT NULL,
  `goal_id` int(11) DEFAULT NULL,
  `staff_msgboard_id` int(11) DEFAULT NULL,
  `user_msgboard_id` int(11) DEFAULT NULL,
  `last_updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_case_user1` (`opened_by_user_id`),
  KEY `fk_case_project1` (`project_id`),
  KEY `fk_case_staff_mb` (`staff_msgboard_id`),
  KEY `fk_case_user_mb` (`user_msgboard_id`),
  KEY `fk_case_product` (`product_id`),
  KEY `fk_case_horse` (`horse_id`),
  KEY `fk_case_goal` (`goal_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `cases`
--


-- --------------------------------------------------------

--
-- Table structure for table `case_attachment`
--

CREATE TABLE `case_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cases_id` int(11) NOT NULL COMMENT 'ID of the case the file is attached to',
  `file_name` varchar(45) NOT NULL COMMENT 'Name of the attached file',
  `file_path` varchar(255) NOT NULL COMMENT 'Path to file in the file system',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ca_case_file` (`cases_id`,`file_name`),
  KEY `fk_ca_case` (`cases_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Stores information about files attached to cases. A case cou' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `case_attachment`
--


-- --------------------------------------------------------

--
-- Table structure for table `case_object`
--

CREATE TABLE `case_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cases_id` int(11) NOT NULL COMMENT 'ID of the case that the object is attached to',
  `object_type` tinyint(4) NOT NULL COMMENT 'Only two kinds of objects for now:\n\n0 - user\n1 - content',
  `object_id` int(11) NOT NULL COMMENT 'The ID of the attached object, either content_header_id or uid, depending on the object_type.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cto` (`cases_id`,`object_type`,`object_id`),
  KEY `fk_co_case` (`cases_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Multiple objects could be associated with a case.' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `case_object`
--


-- --------------------------------------------------------

--
-- Table structure for table `case_tag`
--

CREATE TABLE `case_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cases_id` int(11) NOT NULL COMMENT 'ID of the case',
  `ctags_id` int(11) NOT NULL COMMENT 'ID of the ctag to be associated with the case',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ct_casetag` (`cases_id`,`ctags_id`),
  KEY `fk_ct_case` (`cases_id`),
  KEY `fk_ct_tag` (`ctags_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Define case/tag association' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `case_tag`
--


-- --------------------------------------------------------

--
-- Table structure for table `config`
--

CREATE TABLE `config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `default_language_id` int(11) NOT NULL,
  `platform_name` varchar(255) NOT NULL,
  `platform_admin_user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_config_admin_user` (`platform_admin_user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `config`
--

INSERT INTO `config` VALUES(1, 1, 'Indaba', 1);

-- --------------------------------------------------------

--
-- Table structure for table `content_approval`
--

CREATE TABLE `content_approval` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_header_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `time` datetime NOT NULL,
  `note` text,
  `task_assignment_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_chtask` (`content_header_id`,`task_assignment_id`),
  KEY `fk_ca_user` (`user_id`),
  KEY `fk_ca_content` (`content_header_id`),
  KEY `fk_ca_task` (`task_assignment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `content_approval`
--


-- --------------------------------------------------------

--
-- Table structure for table `content_header`
--

CREATE TABLE `content_header` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT 'ID of the project that the content belongs to',
  `content_type` int(11) NOT NULL COMMENT 'Content type:\n\n0 - survey\n1 - journal',
  `content_object_id` int(11) NOT NULL COMMENT 'ID of the content object in the appropriate content object table: survey_content_object or journal_content_object',
  `horse_id` int(11) NOT NULL COMMENT 'ID of the horse that the content belongs to',
  `title` varchar(255) NOT NULL COMMENT 'Title of the content',
  `author_user_id` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT 'Create time of the content',
  `last_update_time` datetime DEFAULT NULL COMMENT 'Last update time of the content',
  `last_update_user_id` int(11) DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `deleted_by_user_id` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL COMMENT 'Status of the content\n0 - in-flight\n1 - locked\n2 - deleted\n3 - completed\n4 - published',
  `internal_msgboard_id` int(11) DEFAULT NULL COMMENT 'ID of the message board used for internal staff discussions about this content',
  `staff_author_msgboard_id` int(11) DEFAULT NULL COMMENT 'ID of the message board used for staff/author discussions about this content',
  `editable` tinyint(1) DEFAULT NULL,
  `reviewable` tinyint(1) DEFAULT NULL,
  `peer_reviewable` tinyint(1) DEFAULT NULL,
  `approvable` tinyint(1) DEFAULT NULL,
  `submit_time` datetime DEFAULT NULL COMMENT 'when the content was submitted',
  PRIMARY KEY (`id`),
  KEY `fk_content_header_user1` (`author_user_id`),
  KEY `fk_content_header_messageboard1` (`internal_msgboard_id`),
  KEY `fk_ch_mb2` (`staff_author_msgboard_id`),
  KEY `fk_ch_horse` (`horse_id`),
  KEY `fk_ch_deletedby` (`deleted_by_user_id`),
  KEY `fk_ch_lastupdate_user` (`last_update_user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='defines the common attri of content instances of all types' AUTO_INCREMENT=9 ;

--
-- Dumping data for table `content_header`
--

INSERT INTO `content_header` VALUES(1, 1, 1, 1, 1, 'US - Notebook', 16, '2010-06-16 00:00:00', '2012-10-23 11:36:52', 16, NULL, 0, 0, 0, 10, 0, 0, 0, 0, '2012-10-23 11:36:52');
INSERT INTO `content_header` VALUES(2, 1, 0, 1, 2, 'US - Scorecard', 9, '2010-06-16 10:33:42', '2012-10-26 10:38:03', 9, NULL, 0, 0, 36, 13, 0, 0, 0, 0, '2012-10-26 10:38:03');
INSERT INTO `content_header` VALUES(3, 1, 1, 2, 3, 'Argentina - Notebook', 0, '2010-06-16 10:33:49', NULL, 0, NULL, 0, 0, 0, 0, 0, 0, 0, 0, NULL);
INSERT INTO `content_header` VALUES(4, 1, 1, 3, 4, 'Brazil - Notebook', 0, '2010-06-16 10:33:49', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `content_header` VALUES(5, 1, 1, 4, 5, 'China - Notebook', 0, '2010-06-16 10:33:49', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `content_header` VALUES(6, 1, 0, 2, 6, 'Brazil - Scorecard', 0, '2010-06-16 10:33:55', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `content_header` VALUES(7, 1, 0, 3, 7, 'Argentina - Scorecard', 0, '2010-06-16 10:33:55', NULL, 0, NULL, 0, 0, 0, 0, 0, 0, 0, 0, NULL);
INSERT INTO `content_header` VALUES(8, 1, 0, 4, 8, 'China - Scorecard', 0, '2010-06-16 10:33:55', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `content_payment`
--

CREATE TABLE `content_payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_header_id` int(11) NOT NULL,
  `paid_by_user_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `time` datetime NOT NULL,
  `payees` varchar(255) DEFAULT NULL,
  `note` text,
  `task_assignment_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cp_ct_task` (`content_header_id`,`task_assignment_id`),
  KEY `fk_cp_user` (`paid_by_user_id`),
  KEY `fk_cp_content` (`content_header_id`),
  KEY `fk_cp_task` (`task_assignment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `content_payment`
--


-- --------------------------------------------------------

--
-- Table structure for table `content_version`
--

CREATE TABLE `content_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_header_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `child_content_header_id` int(11) DEFAULT NULL COMMENT 'for scorecard version use',
  `complete_time` timestamp NULL DEFAULT NULL COMMENT 'when the version is completely built',
  PRIMARY KEY (`id`),
  KEY `fk_cv_header` (`content_header_id`),
  KEY `fk_cv_user` (`user_id`),
  KEY `fk_cv_child` (`child_content_header_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `content_version`
--

INSERT INTO `content_version` VALUES(1, 1, '2012-10-23 11:36:52', 16, 'nb_submit_1', NULL, NULL);
INSERT INTO `content_version` VALUES(2, 2, '2012-10-26 10:38:03', 9, 'sc_submit_1', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `ctags`
--

CREATE TABLE `ctags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term` varchar(45) NOT NULL COMMENT 'The term of the tag to be displayed',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This table keeps predefined tags that can be used to classif' AUTO_INCREMENT=5 ;

--
-- Dumping data for table `ctags`
--

INSERT INTO `ctags` VALUES(1, 'Administrative', '');
INSERT INTO `ctags` VALUES(2, 'Technical', '');
INSERT INTO `ctags` VALUES(3, 'Editorial', '');
INSERT INTO `ctags` VALUES(4, 'Contractual', '');

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

CREATE TABLE `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_log_id` int(11) NOT NULL COMMENT 'ID of the log book on that the event will be recorded',
  `event_type` smallint(6) NOT NULL COMMENT 'Type of the event',
  `event_data` text COMMENT 'Any event specific data',
  `user_id` int(11) DEFAULT NULL COMMENT 'ID of the user who caused the event to happen',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time at whioch the event ocurred',
  PRIMARY KEY (`id`),
  KEY `fk_event_log_id` (`event_log_id`),
  KEY `fk_event_user` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This table stores events that occured during workflow execut' AUTO_INCREMENT=31 ;

--
-- Dumping data for table `event`
--

INSERT INTO `event` VALUES(1, 1, 2, 'User Logout', 3, '2012-10-23 11:36:09');
INSERT INTO `event` VALUES(2, 1, 1, 'User Login', 16, '2012-10-23 11:36:14');
INSERT INTO `event` VALUES(3, 1, 1, 'Message Notification.[assignid=1][nt=Msg - Welcome]', 1, '2012-10-23 11:36:27');
INSERT INTO `event` VALUES(4, 2, 1, 'Message Notification.[assignid=2][nt=Msg - Welcome]', 1, '2012-10-23 11:36:27');
INSERT INTO `event` VALUES(5, 3, 1, 'Message Notification.[assignid=3][nt=Msg - Welcome]', 1, '2012-10-23 11:36:27');
INSERT INTO `event` VALUES(6, 4, 1, 'Message Notification.[assignid=4][nt=Msg - Welcome]', 1, '2012-10-23 11:36:28');
INSERT INTO `event` VALUES(7, 1, 3, 'User click the assigned task. [assignid=1][toolid=1][prdtype=1]', 16, '2012-10-23 11:36:33');
INSERT INTO `event` VALUES(8, 1, 5, 'User complete the content for the assigned task. [assignid=1][toolid=1][prdtype=1]', 16, '2012-10-23 11:36:52');
INSERT INTO `event` VALUES(9, 1, 1, 'Message Notification.[assignid=1][nt=Confirm - Task Completed]', 1, '2012-10-23 11:37:00');
INSERT INTO `event` VALUES(10, 1, 1, 'Message Notification.[assignid=1][nt=Post - Task Completed]', 1, '2012-10-23 11:37:00');
INSERT INTO `event` VALUES(11, 1, 1, 'Message Notification.[assignid=1][nt=Msg - Thank You]', 1, '2012-10-23 11:37:00');
INSERT INTO `event` VALUES(12, 5, 1, 'Message Notification.[assignid=101][nt=Notify - Please Claim]', 1, '2012-10-23 11:37:01');
INSERT INTO `event` VALUES(13, 1, 2, 'User Logout', 16, '2012-10-23 11:37:10');
INSERT INTO `event` VALUES(14, 1, 1, 'User Login', 3, '2012-10-23 11:37:14');
INSERT INTO `event` VALUES(15, 1, 2, 'User Logout', 3, '2012-10-23 11:38:01');
INSERT INTO `event` VALUES(16, 1, 1, 'User Login', 5, '2012-10-23 11:38:06');
INSERT INTO `event` VALUES(17, 1, 1, 'User Login', 5, '2012-10-25 12:04:30');
INSERT INTO `event` VALUES(18, 1, 1, 'User Login', 5, '2012-10-25 12:09:22');
INSERT INTO `event` VALUES(19, 1, 2, 'User Logout', 5, '2012-10-25 12:09:42');
INSERT INTO `event` VALUES(20, 1, 1, 'User Login', 3, '2012-10-25 12:09:48');
INSERT INTO `event` VALUES(21, 1, 2, 'User Logout', 3, '2012-10-25 12:11:05');
INSERT INTO `event` VALUES(22, 1, 1, 'User Login', 5, '2012-10-25 12:11:10');
INSERT INTO `event` VALUES(23, 1, 2, 'User Logout', 5, '2012-10-25 13:09:49');
INSERT INTO `event` VALUES(24, 1, 1, 'User Login', 3, '2012-10-25 13:09:56');
INSERT INTO `event` VALUES(25, 1, 2, 'User Logout', 3, '2012-10-25 13:10:55');
INSERT INTO `event` VALUES(26, 1, 1, 'User Login', 5, '2012-10-25 13:10:59');
INSERT INTO `event` VALUES(27, 1, 2, 'User Logout', 5, '2012-10-26 10:33:46');
INSERT INTO `event` VALUES(28, 1, 1, 'User Login', 9, '2012-10-26 10:33:57');
INSERT INTO `event` VALUES(29, 1, 2, 'User Logout', 9, '2012-10-26 10:39:16');
INSERT INTO `event` VALUES(30, 1, 1, 'User Login', 3, '2012-10-26 10:39:24');

-- --------------------------------------------------------

--
-- Table structure for table `event_log`
--

CREATE TABLE `event_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the log book',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Event log is used to record events that happened during work' AUTO_INCREMENT=6 ;

--
-- Dumping data for table `event_log`
--

INSERT INTO `event_log` VALUES(1);
INSERT INTO `event_log` VALUES(2);
INSERT INTO `event_log` VALUES(3);
INSERT INTO `event_log` VALUES(4);
INSERT INTO `event_log` VALUES(5);

-- --------------------------------------------------------

--
-- Table structure for table `goal`
--

CREATE TABLE `goal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workflow_sequence_id` int(11) NOT NULL COMMENT 'ID of the sequence that the goal belongs to',
  `weight` int(11) NOT NULL COMMENT 'Weight of the goal in the sequence. Lighter goals go first.',
  `name` varchar(45) NOT NULL COMMENT 'Unique name of the goal',
  `description` varchar(255) DEFAULT NULL,
  `access_matrix_id` int(11) NOT NULL COMMENT 'ID of the access matrix used within the sscope of the goal',
  `duration` int(11) NOT NULL COMMENT 'How many days allowed to complete this goal',
  `entrance_rule_file_name` varchar(255) DEFAULT NULL COMMENT 'ID of the entrance rule set',
  `inflight_rule_file_name` varchar(255) DEFAULT NULL COMMENT 'ID of the inflight rule ',
  `exit_rule_file_name` varchar(255) DEFAULT NULL COMMENT 'ID of the exit rule',
  `entrance_rule_desc` text NOT NULL,
  `inflight_rule_desc` text NOT NULL,
  `exit_rule_desc` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_goal_name_seq` (`workflow_sequence_id`,`name`),
  KEY `fk_goal_workflow_sequence1` (`workflow_sequence_id`),
  KEY `fk_goal_access_matrix1` (`access_matrix_id`),
  KEY `fk_goal_entrance_rule` (`entrance_rule_file_name`),
  KEY `fk_goal_inflight_rule` (`inflight_rule_file_name`),
  KEY `fk_goal_exit_rule` (`exit_rule_file_name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='A goal is a milestone in a project''s workflow. This table de' AUTO_INCREMENT=33 ;

--
-- Dumping data for table `goal`
--

INSERT INTO `goal` VALUES(1, 1, 2, 'nb_edit_1', 'First edit', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msgs to all editors to claim the task;Otherwise send notification to assigned user about the task;', 'If the task becomes unassigned for more than 1 day, send msg to all editors to claim the task', 'Exit if assignment is done.');
INSERT INTO `goal` VALUES(2, 1, 3, 'nb_edit_2', '2nd edit', 0, 5, NULL, NULL, NULL, 'If task not assigned, send messages to all editors to claim assignments from queue;Otherwise send notification to assigned user about the task;', 'If the task becomes unassigned for more than 1 day, send msg to editors to claim the task', 'Exit if all assignments are done');
INSERT INTO `goal` VALUES(3, 1, 4, 'nb_edit_review', 'Review editing results', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim the task;Otherwise send notification to assigned user about the task;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim the task', 'Exit if the assignment is done');
INSERT INTO `goal` VALUES(4, 1, 9, 'nb_manager_review', 'Final manager review', 0, 2, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about the task;', 'Nothing', 'Exit when the assignment is done;Set the content status to DONE;Send msg to project admin about the completion of the horse;Send congrats to all users on the horse;');
INSERT INTO `goal` VALUES(5, 1, 7, 'nb_peer_review', 'Peer review', 0, 10, 'nb_peer_review_entrance.drl', 'nb_peer_review_inflight.drl', 'nb_peer_review_exit.drl', 'IF task activated THEN send ''Msg - Welcome'' to the assigned user;', 'IF task is done THEN send ''Msg - Thank you'' to the assigned user;\nIF assignment is {5} days before due, THEN send ''Reminder 1 - Task about due'' to the assigned user;\nIF assignment is {2} days before due, THEN send ''Reminder 2 - Task about due'' to the assigned user;\nIF assignment is {2} days overdue, THEN send ''Notice 1 - Task overdue'' to the assigned user;\nIF assignment is {5} days overdue, THEN send ''Notice 2 - Task overdue'' to the assigned user;\nIF not all assignments are done AND it is {2} days after due THEN send ''Alert - Milestone Overdue'' to project admin;', 'IF all assignments are done THEN send ''Notify - Milestone Completed'' to project admin;');
INSERT INTO `goal` VALUES(6, 1, 8, 'nb_pr_review', 'Review results of peer review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim the task;Otherwise send notification to assigned user about the task;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim the task', 'Exit when assignment is done');
INSERT INTO `goal` VALUES(7, 1, 1, 'nb_staff_review_1', 'First staff review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim the task;Otherwise send notification to assigned user about the task;', 'If task becomes unassigned for more than one day, send msg to reviewers to claim the task', 'Exit when assignment is done');
INSERT INTO `goal` VALUES(8, 1, 6, 'nb_staff_review_2', 'Second staff review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim the task; Otherwise send notification to assigned user about the task;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim the task', 'Exit when the assignment is done');
INSERT INTO `goal` VALUES(9, 1, 0, 'nb_submit_1', '1st content submission', 0, 10, 'nb_submit_1_entrance.drl', 'nb_submit_1_inflight.drl', 'nb_submit_1_exit.drl', 'IF task activated THEN send ''Msg - Welcome'' to the assigned user;', 'IF assignment is {5} days before due, THEN send ''Reminder 1 - Task about due'' to the assigned user;\nIF assignment is {2} days before due, THEN send ''Reminder 2 - Task about due'' to the assigned user;\nIF assignment is {2} days overdue, THEN send ''Notice 1 - Task overdue'' to the assigned user;\nIF assignment is {5} days overdue, THEN send ''Notice 2 - Task overdue'' to the assigned user;\nIF assignment is {5} days overdue THEN send ''Alert 1 - Task Overdue'' to the project admin, and open a ''Task Overdue'' case for the Project Admin;\nIF assignment is {10} days overdue THEN send ''Alert 2 - Task Overdue'' to the project admin;', 'IF task is done THEN send ''Confirm - Task Completed'' to the assigned user;\nIF task is done THEN post ''Post - Task Completed'' to project wall;\nIF task is done THEN send ''Msg - Thank you'' to the assigned user;');
INSERT INTO `goal` VALUES(10, 1, 5, 'nb_submit_2', '2nd submission', 0, 10, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about the task;', 'Send 1st reminder 5 days before due; Send 2nd reminder 1 day before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due, and copy project admin;', 'Send ''Thank You'' msg to assigned user.');
INSERT INTO `goal` VALUES(11, 2, 0, 'sc_submit_1', '1st submission', 0, 10, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(12, 2, 1, 'sc_review_1', '1st staff review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(13, 2, 2, 'sc_submit_2', '2nd submission', 0, 10, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(14, 2, 3, 'sc_review_2', '2nd staff review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(15, 2, 4, 'sc_peer_review', 'Peer review', 0, 10, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;', 'When assignment is done, send thank-you msg to the assigned user;');
INSERT INTO `goal` VALUES(16, 2, 6, 'sc_submit_3', '3rd submission', 0, 10, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(17, 2, 7, 'sc_review_3', '3rd staff review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(18, 2, 5, 'sc_pr_review', 'Review of peer reviews', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(19, 4, 0, 'sc_pay1', '1st payment', 0, 2, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Nothing', 'After done, send notification to the content author that the 1st payment has been made;');
INSERT INTO `goal` VALUES(20, 5, 0, 'sc_pay2', '2nd payment', 0, 2, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'nothing', 'After done, send notification to the content author that the 2nd payment has been made;');
INSERT INTO `goal` VALUES(21, 6, 0, 'sc_start_horse', 'Start horse', 0, 5, NULL, NULL, NULL, 'none', 'none', 'none');
INSERT INTO `goal` VALUES(22, 7, 1, 'sc_edit', 'Edit scorecard content', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to editors to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to editors to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(25, 11, 1, 'ep_submit', 'Submit content', 0, 10, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(24, 8, 0, 'sc_finish', 'Finish up', 0, 1, NULL, NULL, NULL, 'If task not assigned, send alert to project admin.', 'If task becomes unassigned for more than 1 day, send alert to project admin to assign user;', 'Set content status to done; Send notification to project admin about the completion of the horse;');
INSERT INTO `goal` VALUES(26, 11, 2, 'ep_review', 'Staff review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(27, 11, 3, 'ep_edit', 'Edit content', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to editors to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to editors to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(28, 11, 4, 'ep_peer_review', 'Peer review', 0, 10, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Send 1st reminder 5 days before due; Send 2nd reminder 2 days before due; Send 1st notice 2 days after due; Send 2nd notice 5 days after due and copy project admin;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(29, 11, 5, 'ep_pr_review', 'Review of peer review', 0, 5, NULL, NULL, NULL, 'If task not assigned, send msg to reviewers to claim task;Otherwise send msg to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send msg to reviewers to claim task;', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(30, 11, 6, 'ep_approve', 'Approve the content', 0, 1, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Nothing', 'Exit when assignment is done;');
INSERT INTO `goal` VALUES(31, 11, 7, 'ep_pay', 'Pay the author', 0, 1, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'Nothing', 'Exit when the assignment is done;Send msg to author that the payment has been made;');
INSERT INTO `goal` VALUES(32, 11, 0, 'ep_start', 'Ask to approve start of the workflow', 0, 1, NULL, NULL, NULL, 'If task not assigned, send alert to project admin;Otherwise send notification to assigned user about start of the assignment;', 'If task becomes unassigned for more than 1 day, send alert to project admin;', 'Exit when assignment is done;');

-- --------------------------------------------------------

--
-- Table structure for table `goal_object`
--

CREATE TABLE `goal_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goal_id` int(11) NOT NULL COMMENT 'ID of the goal that this GO is instantiated from',
  `enter_time` datetime DEFAULT NULL COMMENT 'When this goal is started (entered)',
  `exit_time` datetime DEFAULT NULL COMMENT 'When the goal was ended (exited)',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Goal execution status:\n\n0 - waiting\n1 - starting\n2 - started\n3 - done\n',
  `sequence_object_id` int(11) NOT NULL COMMENT 'ID of the sequence object (SO) that the GO belongs to',
  `event_log_id` int(11) DEFAULT NULL COMMENT 'ID of the event log used to record events that occurred during this GO execution',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_seq_goal` (`goal_id`,`sequence_object_id`),
  KEY `fk_goal_object_goal1` (`goal_id`),
  KEY `fk_goal_object_seq` (`sequence_object_id`),
  KEY `fk_goal_object_log` (`event_log_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Goal Object is instantiated from a Goal Definition and is us' AUTO_INCREMENT=93 ;

--
-- Dumping data for table `goal_object`
--

INSERT INTO `goal_object` VALUES(1, 1, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(2, 2, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(3, 3, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(4, 4, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(5, 5, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(6, 6, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(7, 7, '2012-10-23 11:37:01', NULL, 1, 1, 0);
INSERT INTO `goal_object` VALUES(8, 8, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(9, 9, '2012-10-23 11:36:27', '2012-10-23 11:37:00', 3, 1, 0);
INSERT INTO `goal_object` VALUES(10, 10, NULL, NULL, 0, 1, NULL);
INSERT INTO `goal_object` VALUES(11, 15, NULL, NULL, 0, 2, NULL);
INSERT INTO `goal_object` VALUES(12, 18, NULL, NULL, 0, 2, NULL);
INSERT INTO `goal_object` VALUES(13, 12, '2012-10-26 10:38:23', NULL, 2, 2, 0);
INSERT INTO `goal_object` VALUES(14, 14, NULL, NULL, 0, 2, NULL);
INSERT INTO `goal_object` VALUES(15, 17, NULL, NULL, 0, 2, NULL);
INSERT INTO `goal_object` VALUES(16, 11, '2012-10-23 11:36:28', '2012-10-26 10:38:21', 3, 2, 0);
INSERT INTO `goal_object` VALUES(17, 13, NULL, NULL, 0, 2, NULL);
INSERT INTO `goal_object` VALUES(18, 16, NULL, NULL, 0, 2, NULL);
INSERT INTO `goal_object` VALUES(19, 19, NULL, NULL, 0, 3, NULL);
INSERT INTO `goal_object` VALUES(20, 20, NULL, NULL, 0, 4, NULL);
INSERT INTO `goal_object` VALUES(21, 21, NULL, NULL, 0, 5, NULL);
INSERT INTO `goal_object` VALUES(22, 22, NULL, NULL, 0, 6, NULL);
INSERT INTO `goal_object` VALUES(23, 24, NULL, NULL, 0, 7, NULL);
INSERT INTO `goal_object` VALUES(24, 1, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(25, 2, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(26, 3, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(27, 4, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(28, 5, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(29, 6, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(30, 7, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(31, 8, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(32, 9, '2012-10-23 11:36:27', NULL, 2, 8, 0);
INSERT INTO `goal_object` VALUES(33, 10, NULL, NULL, 0, 8, NULL);
INSERT INTO `goal_object` VALUES(34, 1, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(35, 2, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(36, 3, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(37, 4, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(38, 5, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(39, 6, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(40, 7, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(41, 8, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(42, 9, '2012-10-23 11:36:27', NULL, 2, 9, 0);
INSERT INTO `goal_object` VALUES(43, 10, NULL, NULL, 0, 9, NULL);
INSERT INTO `goal_object` VALUES(44, 1, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(45, 2, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(46, 3, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(47, 4, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(48, 5, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(49, 6, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(50, 7, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(51, 8, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(52, 9, '2012-10-23 11:36:28', NULL, 2, 10, 0);
INSERT INTO `goal_object` VALUES(53, 10, NULL, NULL, 0, 10, NULL);
INSERT INTO `goal_object` VALUES(54, 15, NULL, NULL, 0, 11, NULL);
INSERT INTO `goal_object` VALUES(55, 18, NULL, NULL, 0, 11, NULL);
INSERT INTO `goal_object` VALUES(56, 12, NULL, NULL, 0, 11, NULL);
INSERT INTO `goal_object` VALUES(57, 14, NULL, NULL, 0, 11, NULL);
INSERT INTO `goal_object` VALUES(58, 17, NULL, NULL, 0, 11, NULL);
INSERT INTO `goal_object` VALUES(59, 11, '2012-10-23 11:36:28', NULL, 2, 11, 0);
INSERT INTO `goal_object` VALUES(60, 13, NULL, NULL, 0, 11, NULL);
INSERT INTO `goal_object` VALUES(61, 16, NULL, NULL, 0, 11, NULL);
INSERT INTO `goal_object` VALUES(62, 19, NULL, NULL, 0, 12, NULL);
INSERT INTO `goal_object` VALUES(63, 20, NULL, NULL, 0, 13, NULL);
INSERT INTO `goal_object` VALUES(64, 21, NULL, NULL, 0, 14, NULL);
INSERT INTO `goal_object` VALUES(65, 22, NULL, NULL, 0, 15, NULL);
INSERT INTO `goal_object` VALUES(66, 24, NULL, NULL, 0, 16, NULL);
INSERT INTO `goal_object` VALUES(67, 15, NULL, NULL, 0, 17, NULL);
INSERT INTO `goal_object` VALUES(68, 18, NULL, NULL, 0, 17, NULL);
INSERT INTO `goal_object` VALUES(69, 12, NULL, NULL, 0, 17, NULL);
INSERT INTO `goal_object` VALUES(70, 14, NULL, NULL, 0, 17, NULL);
INSERT INTO `goal_object` VALUES(71, 17, NULL, NULL, 0, 17, NULL);
INSERT INTO `goal_object` VALUES(72, 11, '2012-10-23 11:36:28', NULL, 2, 17, 0);
INSERT INTO `goal_object` VALUES(73, 13, NULL, NULL, 0, 17, NULL);
INSERT INTO `goal_object` VALUES(74, 16, NULL, NULL, 0, 17, NULL);
INSERT INTO `goal_object` VALUES(75, 19, NULL, NULL, 0, 18, NULL);
INSERT INTO `goal_object` VALUES(76, 20, NULL, NULL, 0, 19, NULL);
INSERT INTO `goal_object` VALUES(77, 21, NULL, NULL, 0, 20, NULL);
INSERT INTO `goal_object` VALUES(78, 22, NULL, NULL, 0, 21, NULL);
INSERT INTO `goal_object` VALUES(79, 24, NULL, NULL, 0, 22, NULL);
INSERT INTO `goal_object` VALUES(80, 15, NULL, NULL, 0, 23, NULL);
INSERT INTO `goal_object` VALUES(81, 18, NULL, NULL, 0, 23, NULL);
INSERT INTO `goal_object` VALUES(82, 12, NULL, NULL, 0, 23, NULL);
INSERT INTO `goal_object` VALUES(83, 14, NULL, NULL, 0, 23, NULL);
INSERT INTO `goal_object` VALUES(84, 17, NULL, NULL, 0, 23, NULL);
INSERT INTO `goal_object` VALUES(85, 11, '2012-10-23 11:36:28', NULL, 2, 23, 0);
INSERT INTO `goal_object` VALUES(86, 13, NULL, NULL, 0, 23, NULL);
INSERT INTO `goal_object` VALUES(87, 16, NULL, NULL, 0, 23, NULL);
INSERT INTO `goal_object` VALUES(88, 19, NULL, NULL, 0, 24, NULL);
INSERT INTO `goal_object` VALUES(89, 20, NULL, NULL, 0, 25, NULL);
INSERT INTO `goal_object` VALUES(90, 21, NULL, NULL, 0, 26, NULL);
INSERT INTO `goal_object` VALUES(91, 22, NULL, NULL, 0, 27, NULL);
INSERT INTO `goal_object` VALUES(92, 24, NULL, NULL, 0, 28, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `horse`
--

CREATE TABLE `horse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL COMMENT 'ID of the product',
  `target_id` int(11) NOT NULL COMMENT 'ID of the target',
  `start_time` datetime DEFAULT NULL COMMENT 'When the process was started',
  `completion_time` datetime DEFAULT NULL COMMENT 'When the process was completed',
  `content_header_id` int(11) NOT NULL,
  `workflow_object_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_horse_target1` (`target_id`),
  KEY `fk_horse_prod` (`product_id`),
  KEY `fk_horse_workflow_object1` (`workflow_object_id`),
  KEY `fk_horse_content` (`content_header_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Each target/product produces a horse. The horse keeps state ' AUTO_INCREMENT=9 ;

--
-- Dumping data for table `horse`
--

INSERT INTO `horse` VALUES(1, 1, 1, '2010-06-16 10:22:55', NULL, 1, 1);
INSERT INTO `horse` VALUES(2, 2, 1, '2010-06-16 10:33:42', NULL, 2, 2);
INSERT INTO `horse` VALUES(3, 1, 2, '2010-06-16 10:33:49', NULL, 3, 3);
INSERT INTO `horse` VALUES(4, 1, 3, '2010-06-16 10:33:49', NULL, 4, 4);
INSERT INTO `horse` VALUES(5, 1, 4, '2010-06-16 10:33:49', NULL, 5, 5);
INSERT INTO `horse` VALUES(6, 2, 3, '2010-06-16 10:33:55', NULL, 6, 6);
INSERT INTO `horse` VALUES(7, 2, 2, '2010-06-16 10:33:55', NULL, 7, 7);
INSERT INTO `horse` VALUES(8, 2, 4, '2010-06-16 10:33:55', NULL, 8, 8);

-- --------------------------------------------------------

--
-- Table structure for table `indicator_tag`
--

CREATE TABLE `indicator_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_indicator_id` int(11) NOT NULL,
  `itags_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_it_td` (`survey_indicator_id`,`itags_id`),
  KEY `fk_it_ind` (`survey_indicator_id`),
  KEY `fk_it_tag` (`itags_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table contains indicator and itags association' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `indicator_tag`
--


-- --------------------------------------------------------

--
-- Table structure for table `itags`
--

CREATE TABLE `itags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `term_UNIQUE` (`term`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table contains indicator tags' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `itags`
--


-- --------------------------------------------------------

--
-- Table structure for table `journal_attachment_version`
--

CREATE TABLE `journal_attachment_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_version_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `type` varchar(8) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL COMMENT 'file upload time',
  `user_id` int(11) DEFAULT '0' COMMENT 'user who uploaded the file',
  PRIMARY KEY (`id`),
  KEY `jav_version` (`content_version_id`),
  KEY `fk_jav_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `journal_attachment_version`
--


-- --------------------------------------------------------

--
-- Table structure for table `journal_config`
--

CREATE TABLE `journal_config` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `instructions` text NOT NULL COMMENT 'ID of the instruction text for users',
  `min_words` int(11) NOT NULL,
  `max_words` int(11) NOT NULL,
  `exportable_items` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 - content and attachments\n1 - content only\n2 - attachments only',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Journal Config defines configuration infor for a journal pro';

--
-- Dumping data for table `journal_config`
--

INSERT INTO `journal_config` VALUES(1, 'Anti-Corruption Essay', 'Journal about anti-corruption status', 'You are required to write an article about the government''s anti-corruption effort in your country. Please provide sufficient objective evidence to support your points.\n\nPlease make sure that the article''s length is from 500 to 1000 words.', 500, 1000, 0);
INSERT INTO `journal_config` VALUES(2, 'Energy Policy Essay', 'Analysis of the nation''s energy policy', 'Please provide a detailed analysis on the nation''s energy policy. Use real examples, projects, data to support your view points. ', 500, 2000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `journal_content_object`
--

CREATE TABLE `journal_content_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the JCO',
  `content_header_id` int(11) NOT NULL COMMENT 'ID of the content header that contains common attributes for this JCO',
  `body` text COMMENT 'Content body of this JCO',
  `journal_config_id` int(11) NOT NULL COMMENT 'ID of the journal config',
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_header_id_UNIQUE` (`content_header_id`),
  KEY `fk_journal_content_object_content_header1` (`content_header_id`),
  KEY `fk_jco_config` (`journal_config_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Defines journal specific content object structure (JCO). Com' AUTO_INCREMENT=5 ;

--
-- Dumping data for table `journal_content_object`
--

INSERT INTO `journal_content_object` VALUES(1, 1, '<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>', 1);
INSERT INTO `journal_content_object` VALUES(2, 3, NULL, 1);
INSERT INTO `journal_content_object` VALUES(3, 4, NULL, 1);
INSERT INTO `journal_content_object` VALUES(4, 5, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `journal_content_version`
--

CREATE TABLE `journal_content_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_version_id` int(11) NOT NULL,
  `body` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `journal_content_object_id_UNIQUE` (`content_version_id`),
  UNIQUE KEY `jcv_header_task` (`content_version_id`),
  KEY `jcv_version` (`content_version_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `journal_content_version`
--

INSERT INTO `journal_content_version` VALUES(1, 1, '<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>\r\n<p class="desc">\r\n	<strong>Description: </strong>Merge the contents of two arrays together into the first array.</p>\r\n<ul class="signatures">\r\n	<li class="signature" id="jQuery-merge-first-second">\r\n		<h4 class="name">\r\n			<span class="versionAdded">version added: <a href="http://api.jquery.com/category/version/1.0/">1.0</a></span>jQuery.merge( first, second )</h4>\r\n		<p class="arguement">\r\n			<strong>first</strong>The first array to merge, the elements of second added.</p>\r\n		<p class="arguement">\r\n			<strong>second</strong>The second array to merge into the first, unaltered.</p>\r\n	</li>\r\n</ul>\r\n<div class="longdesc">\r\n	<p>\r\n		The <code>$.merge()</code> operation forms an array that contains all elements from the two arrays. The orders of items in the arrays are preserved, with items from the second array appended. The <code>$.merge()</code> function is destructive. It alters the first parameter to add the items from the second.</p>\r\n	<p>\r\n		If you need the original first array, make a copy of it before calling <code>$.merge()</code>. Fortunately, <code>$.merge()</code> itself can be used for this duplication:</p>\r\n	<pre>\r\n	var newArray = $.merge([], oldArray);</pre>\r\n	<p>\r\n		This shortcut creates a new, empty array and merges the contents of oldArray into it, effectively cloning the array.</p>\r\n	<p>\r\n		Prior to jQuery 1.4, the arguments should be true Javascript Array objects; use <code>$.makeArray</code> if they are not.</p>\r\n</div>');

-- --------------------------------------------------------

--
-- Table structure for table `journal_peer_review`
--

CREATE TABLE `journal_peer_review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `journal_content_object_id` int(11) NOT NULL COMMENT 'ID of the JCO that the peer review is for',
  `reviewer_user_id` int(11) NOT NULL COMMENT 'UID of the user who wrote the peer review',
  `opinions` text COMMENT 'Opinions of the peer reviewer',
  `last_change_time` datetime DEFAULT NULL COMMENT 'Last change time of the peer review',
  `msgboard_id` int(11) DEFAULT NULL COMMENT 'ID of the message board used for communication between the peer reviewer and staff members',
  `submit_time` datetime DEFAULT NULL COMMENT 'when the review was submitted',
  PRIMARY KEY (`id`),
  KEY `fk_jpr_jobj` (`journal_content_object_id`),
  KEY `fk_jpr_author` (`reviewer_user_id`),
  KEY `fk_jpr_msgboard` (`msgboard_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Keeps peer review data for a journal content object (JCO). A' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `journal_peer_review`
--


-- --------------------------------------------------------

--
-- Table structure for table `language`
--

CREATE TABLE `language` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `language` char(2) NOT NULL COMMENT 'Language abbreviation: EN, CN, JP, etc',
  `language_desc` varchar(45) NOT NULL,
  `status` tinyint(4) NOT NULL COMMENT 'Whether this lanaguge is enabled:\n\n0 - active\n1 - inactive',
  PRIMARY KEY (`id`),
  UNIQUE KEY `language_UNIQUE` (`language`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This table stores languages supported. All language using 2 ' AUTO_INCREMENT=6 ;

--
-- Dumping data for table `language`
--

INSERT INTO `language` VALUES(1, 'EN', 'English', 0);
INSERT INTO `language` VALUES(2, 'CN', 'Chinese', 0);
INSERT INTO `language` VALUES(3, 'SP', 'Spanish', 0);
INSERT INTO `language` VALUES(4, 'FR', 'French', 1);
INSERT INTO `language` VALUES(5, 'JP', 'Japanese', 1);

-- --------------------------------------------------------

--
-- Table structure for table `mailbatch`
--

CREATE TABLE `mailbatch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` text,
  `body` text,
  `from_email` varchar(255) NOT NULL,
  `to_email` varchar(255) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `send_count` int(11) DEFAULT NULL COMMENT 'how many times the msg was sent',
  `last_send_time` timestamp NULL DEFAULT NULL COMMENT 'last time tried',
  `is_sent` tinyint(1) DEFAULT NULL COMMENT 'whether the msg has been sent successfully',
  `message_id` int(11) DEFAULT NULL COMMENT 'the ID of the message. null if not in the message table.',
  PRIMARY KEY (`id`),
  KEY `fk_mailbatch_msg` (`message_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `mailbatch`
--

INSERT INTO `mailbatch` VALUES(1, 'Notify - Task Activated', 'Dear Betty Young:    <br><br>This is to inform you that your Task nb_submit_1 for Product Notebook of Target US in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.us@gi.org', '2012-10-23 11:36:25', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(2, 'Welcome', 'Dear Betty Young:                    <br><br>Welcome to the project team of Integrity 2010. We look forward to working with you.<br><br>Sincerely,        <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.us@gi.org', '2012-10-23 11:36:27', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(3, 'Notify - Task Activated', 'Dear Jeff Julio:    <br><br>This is to inform you that your Task nb_submit_1 for Product Notebook of Target Argentina in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.ar@gi.org', '2012-10-23 11:36:27', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(4, 'Welcome', 'Dear Jeff Julio:                    <br><br>Welcome to the project team of Integrity 2010. We look forward to working with you.<br><br>Sincerely,        <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.ar@gi.org', '2012-10-23 11:36:27', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(5, 'Notify - Task Activated', 'Dear Frank Ferry:    <br><br>This is to inform you that your Task nb_submit_1 for Product Notebook of Target Brazil in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.bz@gi.org', '2012-10-23 11:36:27', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(6, 'Welcome', 'Dear Frank Ferry:                    <br><br>Welcome to the project team of Integrity 2010. We look forward to working with you.<br><br>Sincerely,        <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.bz@gi.org', '2012-10-23 11:36:27', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(7, 'Notify - Task Activated', 'Dear Meng Li:    <br><br>This is to inform you that your Task nb_submit_1 for Product Notebook of Target China in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.cn@gi.org', '2012-10-23 11:36:28', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(8, 'Welcome', 'Dear Meng Li:                    <br><br>Welcome to the project team of Integrity 2010. We look forward to working with you.<br><br>Sincerely,        <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.cn@gi.org', '2012-10-23 11:36:28', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(9, 'Notify - Task Activated', 'Dear John Smith:    <br><br>This is to inform you that your Task sc_submit_1 for Product Scorecard of Target US in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'researcher1.us@gi.org', '2012-10-23 11:36:28', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(10, 'Notify - Task Activated', 'Dear Fernando Gangzalaz:    <br><br>This is to inform you that your Task sc_submit_1 for Product Scorecard of Target Brazil in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'researcher1.bz@gi.org', '2012-10-23 11:36:28', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(11, 'Notify - Task Activated', 'Dear David Delpotro:    <br><br>This is to inform you that your Task sc_submit_1 for Product Scorecard of Target Argentina in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'researcher1.ar@gi.org', '2012-10-23 11:36:28', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(12, 'Notify - Task Activated', 'Dear Hua Wang:    <br><br>This is to inform you that your Task sc_submit_1 for Product Scorecard of Target China in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'researcher1.cn@gi.org', '2012-10-23 11:36:28', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(13, 'Confirm - Task Completed', 'Dear Betty Young:          <br><br>This is to confirm that you have completed the Task nb_submit_1 for Product Notebook of Target US in the Project Integrity 2010.  Thank you for your hard work!<br><br>Sincerely,    <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.us@gi.org', '2012-10-23 11:37:00', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(14, 'Thank You', 'Dear Betty Young:                    <br><br>Thank you for working on Project Integrity 2010. We look forward to working with you again in the future.<br><br>Sincerely,        <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reporter1.us@gi.org', '2012-10-23 11:37:00', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(15, 'Notify - Please Claim Task', 'Dear Lucy Small:        <br><br>The task nb_staff_review_1 for Product Notebook of Target US in the Project Integrity 2010 is ready to be claimed, please login to Indaba to claim it.    <br><br>Sincerely,   <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reviewer1@gi.org', '2012-10-23 11:37:01', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(16, 'Notify - Please Claim Task', 'Dear Jimmy Regan:        <br><br>The task nb_staff_review_1 for Product Notebook of Target US in the Project Integrity 2010 is ready to be claimed, please login to Indaba to claim it.    <br><br>Sincerely,   <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reviewer2@gi.org', '2012-10-23 11:37:01', 0, NULL, 0, NULL);
INSERT INTO `mailbatch` VALUES(17, 'Notify - Task Activated', 'Dear Lucy Small:    <br><br>This is to inform you that your Task sc_review_1 for Product Scorecard of Target US in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.<br><br>Sincerely,  <br>Indaba System Admin', 'indaba@indabaplatform.com', 'reviewer1@gi.org', '2012-10-26 10:38:23', 0, NULL, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msgboard_id` int(11) NOT NULL COMMENT 'ID of the msgboard the msg is posted to',
  `publishable` tinyint(1) DEFAULT '0' COMMENT 'Whether this msg is publishable',
  `author_user_id` int(11) NOT NULL DEFAULT '0' COMMENT 'uid of the auther. 0 means system generated',
  `created_time` datetime NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `body` text,
  `enhancer_user_id` int(11) DEFAULT NULL COMMENT 'UID of the user who enhanced this msg',
  `enhance_time` datetime DEFAULT NULL COMMENT 'Time at which the user enhanced the msg',
  `enhance_body` text COMMENT 'Enhanced title by the enhancer',
  `enhance_title` varchar(255) DEFAULT NULL COMMENT 'Enhanced title by the enhancer',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT 'not null if the message is deleted.',
  `delete_user_id` int(11) DEFAULT NULL COMMENT 'user that deleted the message',
  PRIMARY KEY (`id`),
  KEY `fk_message_messageboard1` (`msgboard_id`),
  KEY `fk_message_author` (`author_user_id`),
  KEY `fk_message_enhancer` (`enhancer_user_id`),
  KEY `fk_msg_deleter` (`delete_user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='A message is posted to a message board.' AUTO_INCREMENT=19 ;

--
-- Dumping data for table `message`
--

INSERT INTO `message` VALUES(1, 2, NULL, 0, '2012-10-23 11:36:25', 'Notify - Task Activated', 'Dear Betty Young:    \n\nThis is to inform you that your Task nb_submit_1 for Product Notebook of Target US in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(2, 2, NULL, 0, '2012-10-23 11:36:27', 'Welcome', 'Dear Betty Young:                    \n\nWelcome to the project team of Integrity 2010. We look forward to working with you.\n\nSincerely,        \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(3, 3, NULL, 0, '2012-10-23 11:36:27', 'Notify - Task Activated', 'Dear Jeff Julio:    \n\nThis is to inform you that your Task nb_submit_1 for Product Notebook of Target Argentina in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(4, 3, NULL, 0, '2012-10-23 11:36:27', 'Welcome', 'Dear Jeff Julio:                    \n\nWelcome to the project team of Integrity 2010. We look forward to working with you.\n\nSincerely,        \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(5, 4, NULL, 0, '2012-10-23 11:36:27', 'Notify - Task Activated', 'Dear Frank Ferry:    \n\nThis is to inform you that your Task nb_submit_1 for Product Notebook of Target Brazil in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(6, 4, NULL, 0, '2012-10-23 11:36:27', 'Welcome', 'Dear Frank Ferry:                    \n\nWelcome to the project team of Integrity 2010. We look forward to working with you.\n\nSincerely,        \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(7, 5, NULL, 0, '2012-10-23 11:36:28', 'Notify - Task Activated', 'Dear Meng Li:    \n\nThis is to inform you that your Task nb_submit_1 for Product Notebook of Target China in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(8, 5, NULL, 0, '2012-10-23 11:36:28', 'Welcome', 'Dear Meng Li:                    \n\nWelcome to the project team of Integrity 2010. We look forward to working with you.\n\nSincerely,        \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(9, 6, NULL, 0, '2012-10-23 11:36:28', 'Notify - Task Activated', 'Dear John Smith:    \n\nThis is to inform you that your Task sc_submit_1 for Product Scorecard of Target US in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(10, 7, NULL, 0, '2012-10-23 11:36:28', 'Notify - Task Activated', 'Dear Fernando Gangzalaz:    \n\nThis is to inform you that your Task sc_submit_1 for Product Scorecard of Target Brazil in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(11, 8, NULL, 0, '2012-10-23 11:36:28', 'Notify - Task Activated', 'Dear David Delpotro:    \n\nThis is to inform you that your Task sc_submit_1 for Product Scorecard of Target Argentina in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(12, 9, NULL, 0, '2012-10-23 11:36:28', 'Notify - Task Activated', 'Dear Hua Wang:    \n\nThis is to inform you that your Task sc_submit_1 for Product Scorecard of Target China in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(13, 2, NULL, 0, '2012-10-23 11:37:00', 'Confirm - Task Completed', 'Dear Betty Young:          \n\nThis is to confirm that you have completed the Task nb_submit_1 for Product Notebook of Target US in the Project Integrity 2010.  Thank you for your hard work!\n\nSincerely,    \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(14, 1, NULL, 0, '2012-10-23 11:37:00', 'Task Completed', 'Betty Young has competed nb_submit_1 for US - Notebook. Hooray!', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(15, 2, NULL, 0, '2012-10-23 11:37:00', 'Thank You', 'Dear Betty Young:                    \n\nThank you for working on Project Integrity 2010. We look forward to working with you again in the future.\n\nSincerely,        \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(16, 11, NULL, 0, '2012-10-23 11:37:01', 'Notify - Please Claim Task', 'Dear Lucy Small:        \n\nThe task nb_staff_review_1 for Product Notebook of Target US in the Project Integrity 2010 is ready to be claimed, please login to Indaba to claim it.    \n\nSincerely,   \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(17, 12, NULL, 0, '2012-10-23 11:37:01', 'Notify - Please Claim Task', 'Dear Jimmy Regan:        \n\nThe task nb_staff_review_1 for Product Notebook of Target US in the Project Integrity 2010 is ready to be claimed, please login to Indaba to claim it.    \n\nSincerely,   \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `message` VALUES(18, 11, NULL, 0, '2012-10-26 10:38:23', 'Notify - Task Activated', 'Dear Lucy Small:    \n\nThis is to inform you that your Task sc_review_1 for Product Scorecard of Target US in the Project Integrity 2010 has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `msgboard`
--

CREATE TABLE `msgboard` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the msgboard',
  `create_time` datetime NOT NULL COMMENT 'When the msgboard was created',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='A msgboard contains multiple messages. It is used for discus' AUTO_INCREMENT=40 ;

--
-- Dumping data for table `msgboard`
--

INSERT INTO `msgboard` VALUES(1, '2012-10-23 11:36:14');
INSERT INTO `msgboard` VALUES(2, '2012-10-23 11:36:25');
INSERT INTO `msgboard` VALUES(3, '2012-10-23 11:36:27');
INSERT INTO `msgboard` VALUES(4, '2012-10-23 11:36:27');
INSERT INTO `msgboard` VALUES(5, '2012-10-23 11:36:28');
INSERT INTO `msgboard` VALUES(6, '2012-10-23 11:36:28');
INSERT INTO `msgboard` VALUES(7, '2012-10-23 11:36:28');
INSERT INTO `msgboard` VALUES(8, '2012-10-23 11:36:28');
INSERT INTO `msgboard` VALUES(9, '2012-10-23 11:36:28');
INSERT INTO `msgboard` VALUES(10, '2012-10-23 11:36:33');
INSERT INTO `msgboard` VALUES(11, '2012-10-23 11:37:01');
INSERT INTO `msgboard` VALUES(12, '2012-10-23 11:37:01');
INSERT INTO `msgboard` VALUES(13, '2012-10-26 10:34:08');
INSERT INTO `msgboard` VALUES(14, '2012-10-26 10:34:14');
INSERT INTO `msgboard` VALUES(15, '2012-10-26 10:34:28');
INSERT INTO `msgboard` VALUES(16, '2012-10-26 10:34:36');
INSERT INTO `msgboard` VALUES(17, '2012-10-26 10:34:42');
INSERT INTO `msgboard` VALUES(18, '2012-10-26 10:34:47');
INSERT INTO `msgboard` VALUES(19, '2012-10-26 10:34:57');
INSERT INTO `msgboard` VALUES(20, '2012-10-26 10:35:07');
INSERT INTO `msgboard` VALUES(21, '2012-10-26 10:35:16');
INSERT INTO `msgboard` VALUES(22, '2012-10-26 10:35:27');
INSERT INTO `msgboard` VALUES(23, '2012-10-26 10:35:45');
INSERT INTO `msgboard` VALUES(24, '2012-10-26 10:36:00');
INSERT INTO `msgboard` VALUES(25, '2012-10-26 10:36:13');
INSERT INTO `msgboard` VALUES(26, '2012-10-26 10:36:20');
INSERT INTO `msgboard` VALUES(27, '2012-10-26 10:36:26');
INSERT INTO `msgboard` VALUES(28, '2012-10-26 10:36:38');
INSERT INTO `msgboard` VALUES(29, '2012-10-26 10:36:47');
INSERT INTO `msgboard` VALUES(30, '2012-10-26 10:36:57');
INSERT INTO `msgboard` VALUES(31, '2012-10-26 10:37:14');
INSERT INTO `msgboard` VALUES(32, '2012-10-26 10:37:19');
INSERT INTO `msgboard` VALUES(33, '2012-10-26 10:37:25');
INSERT INTO `msgboard` VALUES(34, '2012-10-26 10:37:32');
INSERT INTO `msgboard` VALUES(35, '2012-10-26 10:37:43');
INSERT INTO `msgboard` VALUES(36, '2012-10-26 10:39:26');
INSERT INTO `msgboard` VALUES(37, '2012-10-26 10:39:30');
INSERT INTO `msgboard` VALUES(38, '2012-10-26 10:39:34');
INSERT INTO `msgboard` VALUES(39, '2012-10-26 10:39:38');

-- --------------------------------------------------------

--
-- Table structure for table `msg_reading_status`
--

CREATE TABLE `msg_reading_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` int(11) NOT NULL COMMENT 'ID of the msg read',
  `user_id` int(11) NOT NULL COMMENT 'UID of the user who read the msg',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time at which the msg was read',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_uid_msg` (`message_id`,`user_id`),
  KEY `fk_mrs_uid` (`user_id`),
  KEY `fk_mrs_msg` (`message_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table stores message reading status for a user ' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `msg_reading_status`
--


-- --------------------------------------------------------

--
-- Table structure for table `notification_item`
--

CREATE TABLE `notification_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_text` varchar(255) NOT NULL,
  `body_text` text,
  `language_id` int(11) DEFAULT NULL,
  `notification_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_ind` (`notification_type_id`,`language_id`),
  KEY `fk_language` (`language_id`),
  KEY `fk_notification_type` (`notification_type_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=29 ;

--
-- Dumping data for table `notification_item`
--

INSERT INTO `notification_item` VALUES(1, 'Alert - Milestone about due', 'Dear <username>:\n\nThis is to inform you that the milestone <goalname> of Product <productname> of Target <targetname> in the Project <projectname> is approaching its due time <goalduetime>.      \n\nSincerely,\nIndaba System Admin', 1, 1);
INSERT INTO `notification_item` VALUES(2, 'Alert - Milestone over due', 'Dear <username>:\n\nThis is to inform you that the milestone <goalname> of Product <productname> of Target <targetname> in the Project <projectname> has passed its due time <goalduetime>.    \n\nSincerely,          \nIndaba System Admin', 1, 2);
INSERT INTO `notification_item` VALUES(3, 'Alert - Task Not Assigned', 'Dear <username>:\n\nThis is to inform you that no user has been assigned to Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname>. Your immediate attention is required.\n\nSincerely,\nIndaba System Admin', 1, 3);
INSERT INTO `notification_item` VALUES(5, 'Confirm - Task Completed', 'Dear <username>:          \n\nThis is to confirm that you have completed the Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname>.  Thank you for your hard work!\n\nSincerely,    \nIndaba System Admin', 1, 5);
INSERT INTO `notification_item` VALUES(6, 'Thank You', 'Dear <username>:                    \n\nThank you for working on Project <projectname>. We look forward to working with you again in the future.\n\nSincerely,        \nIndaba System Admin', 1, 6);
INSERT INTO `notification_item` VALUES(7, 'Welcome', 'Dear <username>:                    \n\nWelcome to the project team of <projectname>. We look forward to working with you.\n\nSincerely,        \nIndaba System Admin', 1, 7);
INSERT INTO `notification_item` VALUES(8, 'Notice - Task Overdue', 'Dear <username>:              \n\nThis is a friendly reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> has reached its deadline <taskduetime>. Please try to complete the assignment ASAP.         \n\nSincerely,      \nIndaba System Admin', 1, 8);
INSERT INTO `notification_item` VALUES(9, 'Second Notice - Task Overdue', 'Dear <username>:                \n\nThis is the 2nd reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> has reached its deadline <taskduetime>. Please try to complete the assignment ASAP. If the task is completed in <numdays> days, we may have to suspend this task.\n\nSincerely,      \nIndaba System Admin', 1, 9);
INSERT INTO `notification_item` VALUES(10, 'Notify - Milestone Completed', 'Dear <username>:\n                   \nThis is to inform you that the milestone <goalname> of Product <productname> of Target <targetname> in the Project <projectname> has been completed.  \n\nSincerely,         \nIndaba System Admin', 1, 10);
INSERT INTO `notification_item` VALUES(11, 'Notify - Please Claim Task', 'Dear <username>:        \n\nThe task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> is ready to be claimed, please login to Indaba to claim it.    \n\nSincerely,   \nIndaba System Admin', 1, 11);
INSERT INTO `notification_item` VALUES(12, 'Notify - Project Done', 'Dear <username>: \n\nThis is to inform you that all tasks in the Project <projectname> have been completed. \n \nSincerely,         \nIndaba System Admin', 1, 12);
INSERT INTO `notification_item` VALUES(13, 'Notify - Task Activated', 'Dear <username>:    \n\nThis is to inform you that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> has now been activated.  Please start working on the assignment as soon as you can. The due time of the task is <duetime>.\n\nSincerely,  \nIndaba System Admin', 1, 13);
INSERT INTO `notification_item` VALUES(14, 'Reminder - Task About Due', 'Dear <username>:           \n\nThis is a friendly reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> is approaching its deadline <taskduetime>. Please try to complete the assignment ASAP.       \n\nSincerely,     \nIndaba System Admin', 1, 14);
INSERT INTO `notification_item` VALUES(15, 'Second Reminder - Task About Due', 'Dear <username>:           \n\nThis is the second reminder that your Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname> is approaching its deadline <taskduetime>. Please try to complete the assignment ASAP.       \n\nSincerely,     \nIndaba System Admin', 1, 15);
INSERT INTO `notification_item` VALUES(16, 'Alert - New Site Message', 'Dear <username>,\n\nThis is to notify you that there is a new message for you on Indaba website. Please login to the site to view the message.\n\nSincerely,\nIndaba System Administrator', 1, 16);
INSERT INTO `notification_item` VALUES(17, 'Notify - Case Assigned', 'Dear <username>:  \n \nThis is to inform you that the new case <casename> has been assigned to you. Please logon to Indaba system and resolve the issue as soon as possible.\n\nSincerely, \nIndaba System Admin', 1, 17);
INSERT INTO `notification_item` VALUES(18, 'Notify - Case Attached', 'Dear <username>:  \n \nThis is to inform you that you have been attached to the case <casename>. Please logon to Indaba system and review the issue as soon as possible.\n\nSincerely, \nIndaba System Admin', 1, 18);
INSERT INTO `notification_item` VALUES(19, 'Notify - Case Status Change', 'Dear <username>: \n \nThis is to inform you that the status of the case <casename> has been changed to <casestatus>.\n  \nSincerely, \nIndaba System Admin', 1, 19);
INSERT INTO `notification_item` VALUES(20, 'Notify - Review Feedback Posted', 'Dear <username>: \n \nThis is to inform you that review feedback has been posted for <contenttitle>. Please provide your responses as soon as possible.\n  \nSincerely, \nIndaba System Admin', 1, 20);
INSERT INTO `notification_item` VALUES(21, 'Notify - Review Feedback Response Posted', 'Dear <username>: \n \nThis is to inform you that responses to your review feedback have been posted for <contenttitle>. Please continue your review as soon as possible.\n  \nSincerely, \nIndaba System Admin', 1, 21);
INSERT INTO `notification_item` VALUES(22, 'Task Completed', '<username> has competed <taskname> for <contenttitle>. Hooray!', 1, 22);
INSERT INTO `notification_item` VALUES(23, 'Horse Completed', '<contenttitle> is done. Hip Hip Hooray!', 1, 23);
INSERT INTO `notification_item` VALUES(24, 'Your Indaba Password', 'Dear <username>: \n \nYour Indaba password is <password>. \n  \nSincerely, \nIndaba System Admin', 1, 24);
INSERT INTO `notification_item` VALUES(25, 'Notify - Review Feedback Canceled', 'Dear <username>: \n \nThis is to inform you that review feedback for <contenttitle> has been canceled. You no longer need to provide responses.\n  \nSincerely, \nIndaba System Admin', 1, 25);
INSERT INTO `notification_item` VALUES(26, 'Notify - Task Assigned', 'Dear <username>:    \n\nThis is to inform you that you have been assigned the Task <taskname> for Product <productname> of Target <targetname> in the Project <projectname>. \n\nSincerely,  \nIndaba System Admin', 1, 26);
INSERT INTO `notification_item` VALUES(27, 'Alert - Task Overdue', 'Dear <username>:\n\nThis is to inform you that the task <taskname> of <contenttitle> in the Project <projectname> has passed its deadline <taskduetime>.      \n\nSincerely,\nIndaba System Admin', 1, 27);
INSERT INTO `notification_item` VALUES(28, 'Alert - Task Overdue', 'Dear <username>:\n\nThis is the second alert that the task <taskname> of <contenttitle> in the Project <projectname> has passed its deadline <taskduetime>.      \n\nSincerely,\nIndaba System Admin', 1, 28);

-- --------------------------------------------------------

--
-- Table structure for table `notification_type`
--

CREATE TABLE `notification_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Stores predefined notification types' AUTO_INCREMENT=29 ;

--
-- Dumping data for table `notification_type`
--

INSERT INTO `notification_type` VALUES(1, 'Alert - Milestone About Due', 'Alert user that the milestone is reaching due time');
INSERT INTO `notification_type` VALUES(2, 'Alert - Milestone Overdue', 'Alert user that the milestone is overdue');
INSERT INTO `notification_type` VALUES(3, 'Alert - Task Not Assigned', 'Alert user that nobody is assigned to a task');
INSERT INTO `notification_type` VALUES(5, 'Confirm - Task Completed', 'Confirmation message that a task has been completed');
INSERT INTO `notification_type` VALUES(6, 'Msg - Thank You', 'Send this msg to thank a user after he''s done with the project');
INSERT INTO `notification_type` VALUES(7, 'Msg - Welcome', 'Welcome the user to a project');
INSERT INTO `notification_type` VALUES(8, 'Notice 1 - Task overdue', 'First notice to a user that assigned task is overdue');
INSERT INTO `notification_type` VALUES(9, 'Notice 2 - Task overdue', 'Second notice to a user that assigned task is overdue');
INSERT INTO `notification_type` VALUES(10, 'Notify - Milestone Completed', 'Notify a user that a milestone has been completed');
INSERT INTO `notification_type` VALUES(11, 'Notify - Please Claim', 'Ask a user to claim a task from queue');
INSERT INTO `notification_type` VALUES(12, 'Notify - Project Done', 'Notify user that a project is done');
INSERT INTO `notification_type` VALUES(13, 'Notify - Task Activated', 'Notify a user that her task assignment is now activated');
INSERT INTO `notification_type` VALUES(14, 'Reminder 1 - Task about due', 'First reminder to a user that her task assignment is about due');
INSERT INTO `notification_type` VALUES(15, 'Reminder 2 - Task about due', 'Second reminder to a user that her task assignment is about due');
INSERT INTO `notification_type` VALUES(16, 'Sys - Site Message Alert', 'Used to notify user of new site messages');
INSERT INTO `notification_type` VALUES(17, 'Sys - Case Assigned ', 'Message for case assignment notification');
INSERT INTO `notification_type` VALUES(18, 'Sys - Case Attached', 'Notify user that he is attached to a case');
INSERT INTO `notification_type` VALUES(19, 'Sys - Case Status Change', 'Notify user of case status change');
INSERT INTO `notification_type` VALUES(20, 'Sys - Review Feedback Posted', 'Notify user of review feedback');
INSERT INTO `notification_type` VALUES(21, 'Sys - Review Feedback Response Posted', 'Notify user of responses to review feedback');
INSERT INTO `notification_type` VALUES(22, 'Post - Task Completed', 'Post the task completion msg to project wall');
INSERT INTO `notification_type` VALUES(23, 'Post - Horse Completed', 'Post the horse completion msg to project wall');
INSERT INTO `notification_type` VALUES(24, 'Sys - User Password', 'Send password to user through email');
INSERT INTO `notification_type` VALUES(25, 'Sys - Review Feedback Canceled', 'Notify the author that review feedback has been canceled');
INSERT INTO `notification_type` VALUES(26, 'Sys - Task Assigned', 'Notify a user that she has been assigned to a task');
INSERT INTO `notification_type` VALUES(27, 'Alert 1 - Task Overdue', 'First alert that a task is overdue');
INSERT INTO `notification_type` VALUES(28, 'Alert 2 - Task Overdue', 'Second alert that a task is overdue');

-- --------------------------------------------------------

--
-- Table structure for table `orgadmin`
--

CREATE TABLE `orgadmin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_oaoa` (`organization_id`,`user_id`),
  KEY `fk_oa_org` (`organization_id`),
  KEY `fk_oa_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `orgadmin`
--


-- --------------------------------------------------------

--
-- Table structure for table `organization`
--

CREATE TABLE `organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `admin_user_id` int(11) NOT NULL COMMENT 'This is the super admin - first admin of the org',
  `url` varchar(255) DEFAULT NULL,
  `enforce_api_security` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'whether to enforce api key',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_org_admin_user` (`admin_user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `organization`
--

INSERT INTO `organization` VALUES(1, 'Global Integrity', NULL, 1, 'http://globalintegrity.org', 1);

-- --------------------------------------------------------

--
-- Table structure for table `orgkey`
--

CREATE TABLE `orgkey` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) NOT NULL COMMENT 'The org that the key belongs to. Org 0 means super key.',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT 'Version number of the key',
  `hash_algorithm` varchar(45) NOT NULL DEFAULT 'sha1',
  `issue_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the key is issued',
  `issue_user_id` int(11) NOT NULL COMMENT 'user that issued the key',
  `effective_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'when the key becomes effective',
  `valid_days` int(11) NOT NULL DEFAULT '0' COMMENT 'how many days the key is valid. 0 means no expiration.',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = normal;\n2 = revoked;',
  `revoke_time` timestamp NULL DEFAULT NULL COMMENT 'when the key was revoked',
  `revoke_user_id` int(11) DEFAULT NULL COMMENT 'user that revoked the key',
  `revoke_reason` varchar(255) DEFAULT NULL,
  `renew_time` timestamp NULL DEFAULT NULL COMMENT 'when the key was renewed',
  `renew_user_id` int(11) DEFAULT NULL COMMENT 'User that renewed the key. 0 means by system',
  `data` varchar(45) NOT NULL COMMENT 'Key data. Must be valid ASCII with no spaces. Not to exceed 40 chars.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ok_orgversion` (`organization_id`,`version`),
  KEY `fk_ok_org` (`organization_id`),
  KEY `fk_ok_issuer` (`issue_user_id`),
  KEY `fk_ok_revoker` (`revoke_user_id`),
  KEY `fk_ok_renewer` (`renew_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `orgkey`
--


-- --------------------------------------------------------

--
-- Table structure for table `pregoal`
--

CREATE TABLE `pregoal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workflow_sequence_id` int(11) NOT NULL COMMENT 'ID of the sequence ',
  `pre_goal_id` int(11) NOT NULL COMMENT 'ID of the goal that is a pre-requisite goal of the sequence',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_seq_goal` (`workflow_sequence_id`,`pre_goal_id`),
  KEY `fk_pg_seqid` (`workflow_sequence_id`),
  KEY `fk_pg_goal` (`pre_goal_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Defines pre-requisite goals that must be completed before th' AUTO_INCREMENT=16 ;

--
-- Dumping data for table `pregoal`
--

INSERT INTO `pregoal` VALUES(4, 0, 14);
INSERT INTO `pregoal` VALUES(7, 0, 17);
INSERT INTO `pregoal` VALUES(3, 0, 15);
INSERT INTO `pregoal` VALUES(6, 4, 14);
INSERT INTO `pregoal` VALUES(8, 9, 22);
INSERT INTO `pregoal` VALUES(9, 0, 21);
INSERT INTO `pregoal` VALUES(10, 5, 17);
INSERT INTO `pregoal` VALUES(11, 6, 15);
INSERT INTO `pregoal` VALUES(12, 7, 14);
INSERT INTO `pregoal` VALUES(13, 8, 21);
INSERT INTO `pregoal` VALUES(14, 8, 22);
INSERT INTO `pregoal` VALUES(15, 8, 17);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workflow_id` int(11) NOT NULL COMMENT 'ID of the workflow that drives the product creation',
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `project_id` int(11) NOT NULL COMMENT 'ID of the project the product belongs to',
  `access_matrix_id` int(11) DEFAULT NULL,
  `product_config_id` int(11) NOT NULL COMMENT 'ID of the product config, either journal_config or survey_config, depending on the content_type',
  `content_type` tinyint(4) NOT NULL COMMENT '0 - survey\n1 - journal',
  `mode` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = config;\n2 = test;\n3 = prod;',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_prod_proj` (`project_id`),
  KEY `fk_prod_matrix` (`access_matrix_id`),
  KEY `fk_prod_wfd` (`workflow_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `product`
--

INSERT INTO `product` VALUES(1, 1, 'Notebook', 'Notebook product of Indaba 2010 project', 1, NULL, 1, 1, 3);
INSERT INTO `product` VALUES(2, 2, 'Scorecard', 'Survey of anti-corruption status', 1, NULL, 1, 0, 3);
INSERT INTO `product` VALUES(3, 3, 'Energy Policy Analysis', 'An essay that analyzes the energy policy of the nation', 2, 0, 2, 1, 3);
INSERT INTO `product` VALUES(4, 3, 'Energy Policy Survey', 'Survey of national energy policy ', 2, NULL, 2, 0, 3);

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the project',
  `organization_id` int(11) NOT NULL DEFAULT '1' COMMENT 'ID of the organization the project belongs to',
  `code_name` varchar(45) NOT NULL COMMENT 'Code name of the project',
  `description` varchar(255) NOT NULL,
  `owner_user_id` int(11) NOT NULL COMMENT 'Project owner''s uid',
  `creation_time` datetime DEFAULT NULL COMMENT 'When the project was created',
  `access_matrix_id` int(11) NOT NULL COMMENT 'Project-level default access matrix',
  `view_matrix_id` int(11) NOT NULL COMMENT 'ID of the view matrix for this project',
  `start_time` date NOT NULL COMMENT 'When the project is to be opened',
  `study_period_id` int(11) NOT NULL COMMENT 'ID of the study period specification',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT 'Status of the project\n\n0 - waiting\n1 - in-flight\n2 - completed\n3 - suspended\n4 - abandoned\n',
  `logo_path` varchar(255) DEFAULT NULL COMMENT 'File path to the project''s logo',
  `msgboard_id` int(11) DEFAULT NULL COMMENT 'ID of the msgboard for project-level discussions: the project wall',
  `admin_user_id` int(11) NOT NULL COMMENT 'UID of the project admin',
  `sponsor_logos` varchar(256) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'whether the project is active',
  `close_time` date DEFAULT NULL COMMENT 'When the project is expected to be closed',
  `visibility` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'Visibility type of the project. \n\n1 = public; \n2 = private;',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_name_UNIQUE` (`code_name`),
  KEY `fk_project_user1` (`owner_user_id`),
  KEY `fk_project_table11` (`access_matrix_id`),
  KEY `fk_project_study_period1` (`study_period_id`),
  KEY `fk_project_view_matrix1` (`view_matrix_id`),
  KEY `fk_project_messageboard1` (`msgboard_id`),
  KEY `fk_project_admin` (`admin_user_id`),
  KEY `fk_project_org` (`organization_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Definition of project' AUTO_INCREMENT=3 ;

--
-- Dumping data for table `project`
--

INSERT INTO `project` VALUES(1, 1, 'Integrity 2010', 'World Anti-Corruption Study', 1, NULL, 1, 1, '2010-06-01', 1, 1, NULL, 1, 1, NULL, 1, '2010-12-31', 1);
INSERT INTO `project` VALUES(2, 1, 'Green Energy 2010', 'World Energy Policy Study', 1, NULL, 1, 1, '2010-10-01', 1, 1, NULL, NULL, 1, NULL, 1, '2010-12-31', 1);

-- --------------------------------------------------------

--
-- Table structure for table `project_admin`
--

CREATE TABLE `project_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_pa_projuser` (`project_id`),
  KEY `fk_pa_proj` (`project_id`),
  KEY `fk_pa_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `project_admin`
--


-- --------------------------------------------------------

--
-- Table structure for table `project_contact`
--

CREATE TABLE `project_contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT 'ID of the project',
  `user_id` int(11) NOT NULL COMMENT 'UID of the user who will be a contact person in the project',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_fc` (`project_id`,`user_id`),
  KEY `fk_project_contact_project1` (`project_id`),
  KEY `fk_project_contact_user1` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This table stores users who are to be the contact people of ' AUTO_INCREMENT=6 ;

--
-- Dumping data for table `project_contact`
--

INSERT INTO `project_contact` VALUES(1, 1, 5);
INSERT INTO `project_contact` VALUES(2, 1, 6);
INSERT INTO `project_contact` VALUES(3, 2, 5);
INSERT INTO `project_contact` VALUES(4, 2, 1);
INSERT INTO `project_contact` VALUES(5, 2, 6);

-- --------------------------------------------------------

--
-- Table structure for table `project_membership`
--

CREATE TABLE `project_membership` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'UID of the user who is included into a project',
  `role_id` int(11) NOT NULL COMMENT 'ID of the role the user takes in the project',
  `project_id` int(11) NOT NULL COMMENT 'ID of the project',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_pu` (`user_id`,`project_id`),
  KEY `fk_project_membership_user1` (`user_id`),
  KEY `fk_project_membership_project_roles1` (`role_id`),
  KEY `fk_pm_project` (`project_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Defines user membership in a project. User can be a member o' AUTO_INCREMENT=36 ;

--
-- Dumping data for table `project_membership`
--

INSERT INTO `project_membership` VALUES(1, 13, 6, 1);
INSERT INTO `project_membership` VALUES(2, 15, 6, 1);
INSERT INTO `project_membership` VALUES(3, 14, 6, 1);
INSERT INTO `project_membership` VALUES(4, 16, 6, 1);
INSERT INTO `project_membership` VALUES(5, 9, 7, 1);
INSERT INTO `project_membership` VALUES(6, 11, 7, 1);
INSERT INTO `project_membership` VALUES(7, 12, 7, 1);
INSERT INTO `project_membership` VALUES(8, 10, 7, 1);
INSERT INTO `project_membership` VALUES(9, 2, 3, 1);
INSERT INTO `project_membership` VALUES(10, 20, 3, 1);
INSERT INTO `project_membership` VALUES(11, 3, 4, 1);
INSERT INTO `project_membership` VALUES(12, 4, 4, 1);
INSERT INTO `project_membership` VALUES(13, 5, 2, 1);
INSERT INTO `project_membership` VALUES(14, 6, 2, 1);
INSERT INTO `project_membership` VALUES(15, 7, 8, 1);
INSERT INTO `project_membership` VALUES(16, 8, 8, 1);
INSERT INTO `project_membership` VALUES(22, 2, 3, 2);
INSERT INTO `project_membership` VALUES(18, 21, 5, 1);
INSERT INTO `project_membership` VALUES(19, 17, 5, 1);
INSERT INTO `project_membership` VALUES(35, 18, 5, 1);
INSERT INTO `project_membership` VALUES(21, 19, 5, 1);
INSERT INTO `project_membership` VALUES(23, 3, 4, 2);
INSERT INTO `project_membership` VALUES(24, 4, 4, 2);
INSERT INTO `project_membership` VALUES(25, 5, 2, 2);
INSERT INTO `project_membership` VALUES(26, 6, 2, 2);
INSERT INTO `project_membership` VALUES(27, 17, 5, 2);
INSERT INTO `project_membership` VALUES(28, 18, 5, 2);
INSERT INTO `project_membership` VALUES(29, 19, 5, 2);
INSERT INTO `project_membership` VALUES(30, 21, 5, 2);
INSERT INTO `project_membership` VALUES(31, 22, 6, 2);
INSERT INTO `project_membership` VALUES(32, 24, 6, 2);
INSERT INTO `project_membership` VALUES(33, 23, 7, 2);
INSERT INTO `project_membership` VALUES(34, 25, 7, 2);

-- --------------------------------------------------------

--
-- Table structure for table `project_owner`
--

CREATE TABLE `project_owner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_po_projorg` (`project_id`,`org_id`),
  KEY `fk_po_project` (`project_id`),
  KEY `fk_po_org` (`org_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `project_owner`
--


-- --------------------------------------------------------

--
-- Table structure for table `project_roles`
--

CREATE TABLE `project_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT 'ID of the project',
  `role_id` int(11) NOT NULL COMMENT 'ID of the role to be included into this project',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_proj_role` (`role_id`,`project_id`),
  KEY `fk_project_roles_role1` (`role_id`),
  KEY `fk_project_roles_project1` (`project_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Which roles apply to this project. The system could define m' AUTO_INCREMENT=17 ;

--
-- Dumping data for table `project_roles`
--

INSERT INTO `project_roles` VALUES(1, 1, 2);
INSERT INTO `project_roles` VALUES(2, 1, 4);
INSERT INTO `project_roles` VALUES(3, 1, 3);
INSERT INTO `project_roles` VALUES(4, 1, 7);
INSERT INTO `project_roles` VALUES(5, 1, 6);
INSERT INTO `project_roles` VALUES(6, 1, 8);
INSERT INTO `project_roles` VALUES(10, 2, 2);
INSERT INTO `project_roles` VALUES(8, 2, 6);
INSERT INTO `project_roles` VALUES(9, 1, 5);
INSERT INTO `project_roles` VALUES(11, 2, 3);
INSERT INTO `project_roles` VALUES(12, 2, 4);
INSERT INTO `project_roles` VALUES(13, 2, 5);
INSERT INTO `project_roles` VALUES(14, 2, 7);
INSERT INTO `project_roles` VALUES(16, 2, 9);

-- --------------------------------------------------------

--
-- Table structure for table `project_target`
--

CREATE TABLE `project_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT 'ID of the project',
  `target_id` int(11) NOT NULL COMMENT 'ID of the target to be included into the project',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_pt` (`project_id`,`target_id`),
  KEY `fk_project` (`project_id`),
  KEY `fk_target` (`target_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='The system could define many targets, but only a subset of t' AUTO_INCREMENT=10 ;

--
-- Dumping data for table `project_target`
--

INSERT INTO `project_target` VALUES(1, 1, 2);
INSERT INTO `project_target` VALUES(2, 1, 1);
INSERT INTO `project_target` VALUES(3, 1, 3);
INSERT INTO `project_target` VALUES(4, 1, 4);
INSERT INTO `project_target` VALUES(8, 2, 6);
INSERT INTO `project_target` VALUES(9, 2, 7);

-- --------------------------------------------------------

--
-- Table structure for table `reference`
--

CREATE TABLE `reference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `choice_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 - no choice\n1 - single choice\n2 - multi choice',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='predefined to be used to provide standardized Reference requ' AUTO_INCREMENT=4 ;

--
-- Dumping data for table `reference`
--

INSERT INTO `reference` VALUES(1, 'In Law 1', 'Identify and describe the name(s) and section(s) of the applicable law(s), statute(s), regulation(s), or constitutional provision(s).  Provide a web link to the source(s) if available. ', 0);
INSERT INTO `reference` VALUES(2, 'In Law 2 ', 'Identify and describe the name(s) of the institution(s), agency (or agencies), government entity (or entities), or mechanism(s) by name.  Also identify the law(s), statute(s), regulation(s), or constitutional provision(s) that created the institution(s), entity (or entities), or mechanism(s).  Provide a web link to the source(s) if available.', 0);
INSERT INTO `reference` VALUES(3, 'In Practice', 'Identify two or more of the following sources to support your score (two sources of the same type are sufficient, provided they are both identified separately in the same text box):', 2);

-- --------------------------------------------------------

--
-- Table structure for table `reference_choice`
--

CREATE TABLE `reference_choice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reference_id` int(11) NOT NULL,
  `label` varchar(255) NOT NULL COMMENT 'Text to be displayed to the user',
  `weight` int(11) DEFAULT NULL COMMENT 'Display order of the choice',
  `mask` bigint(20) unsigned NOT NULL COMMENT 'Bit mask that represents the choice',
  PRIMARY KEY (`id`),
  KEY `fk_rc_ref` (`reference_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Choices for reference' AUTO_INCREMENT=9 ;

--
-- Dumping data for table `reference_choice`
--

INSERT INTO `reference_choice` VALUES(1, 3, '1) media reports (identify the publication, author, date published, title, and website if available)', 0, 1);
INSERT INTO `reference_choice` VALUES(2, 3, '2) academic, policy or professional studies (identify the publication, author, date published, title, and website if available)', 1, 2);
INSERT INTO `reference_choice` VALUES(3, 3, '3) government studies (identify the publication, author, date published, title, and website if available)', 2, 4);
INSERT INTO `reference_choice` VALUES(4, 3, '4) international organization studies (identify the publication, author, date published, title, and website if available)', 3, 8);
INSERT INTO `reference_choice` VALUES(5, 3, '5) interviews with government officials (identify the person by name, title, organization, date of interview, and place of interview)', 4, 16);
INSERT INTO `reference_choice` VALUES(6, 3, '6) interviews with academics (identify the person by name, title, organization, date of interview, and place of interview)', 5, 32);
INSERT INTO `reference_choice` VALUES(7, 3, '7) interviews with civil society or NGO representatives (identify the person by name, title, organization, date of interview, and place of interview)', 6, 64);
INSERT INTO `reference_choice` VALUES(8, 3, '8) interviews with journalists or media representatives (identify the person by name, title, organization, date of interview, and place of interview)', 7, 128);

-- --------------------------------------------------------

--
-- Table structure for table `reference_object`
--

CREATE TABLE `reference_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reference_id` int(11) NOT NULL,
  `source_description` text NOT NULL,
  `comments` text,
  `choices` bigint(20) unsigned DEFAULT NULL COMMENT 'Bit mask that contains all selected choices',
  PRIMARY KEY (`id`),
  KEY `fk_ro_ref` (`reference_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=45 ;

--
-- Dumping data for table `reference_object`
--

INSERT INTO `reference_object` VALUES(1, 1, 'sdfsdfggsfd', '', 0);
INSERT INTO `reference_object` VALUES(2, 3, 'dsfg gsfdg', '', 6);
INSERT INTO `reference_object` VALUES(3, 1, 'sdfg dfgfdsg', '', 0);
INSERT INTO `reference_object` VALUES(4, 1, 'sdfg sdfgsdfg', '', 0);
INSERT INTO `reference_object` VALUES(5, 3, 'dfg dfsgdfgdfg', '', 6);
INSERT INTO `reference_object` VALUES(6, 3, 'dh drhrtyertrgr ', '', 10);
INSERT INTO `reference_object` VALUES(7, 3, 'dfh df hdfhd fghdfgh', '', 10);
INSERT INTO `reference_object` VALUES(8, 1, 'dfgh dfghdgfh df', '', 0);
INSERT INTO `reference_object` VALUES(9, 2, 'esr wertert etr ', '', 0);
INSERT INTO `reference_object` VALUES(10, 1, 'fdgh dfgh fgh', '', 0);
INSERT INTO `reference_object` VALUES(11, 1, 'df dsfgdgdfg', '', 0);
INSERT INTO `reference_object` VALUES(12, 1, '456gdfh erth', '', 0);
INSERT INTO `reference_object` VALUES(13, 1, 'fgh tyht hytrhg', '', 0);
INSERT INTO `reference_object` VALUES(14, 3, 'fgh fghfghfg', '', 12);
INSERT INTO `reference_object` VALUES(15, 2, 'fgh drthdrthtrhhg', '', 0);
INSERT INTO `reference_object` VALUES(16, 3, 'fj fghdf hfghgfhgfh', '', 6);
INSERT INTO `reference_object` VALUES(17, 3, 'xcvncnfg fghj ', '', 16);
INSERT INTO `reference_object` VALUES(18, 1, 'cxgh dfhd rtydtry drt', '', 0);
INSERT INTO `reference_object` VALUES(19, 1, 'fgh fh fgh gf', '', 0);
INSERT INTO `reference_object` VALUES(20, 3, 'fgh t6uhgfgdaf', '', 32);
INSERT INTO `reference_object` VALUES(21, 3, 'esrytdb h bf', '', 2);
INSERT INTO `reference_object` VALUES(22, 3, 'dfth hsrtg', '', 4);
INSERT INTO `reference_object` VALUES(23, 1, 'sdfsdfggsfd', '', 0);
INSERT INTO `reference_object` VALUES(24, 1, 'sdfg dfgfdsg', '', 0);
INSERT INTO `reference_object` VALUES(25, 1, 'sdfg sdfgsdfg', '', 0);
INSERT INTO `reference_object` VALUES(26, 3, 'dfg dfsgdfgdfg', '', 6);
INSERT INTO `reference_object` VALUES(27, 3, 'dh drhrtyertrgr ', '', 10);
INSERT INTO `reference_object` VALUES(28, 3, 'dfh df hdfhd fghdfgh', '', 10);
INSERT INTO `reference_object` VALUES(29, 1, 'dfgh dfghdgfh df', '', 0);
INSERT INTO `reference_object` VALUES(30, 2, 'esr wertert etr ', '', 0);
INSERT INTO `reference_object` VALUES(31, 1, 'fdgh dfgh fgh', '', 0);
INSERT INTO `reference_object` VALUES(32, 1, 'df dsfgdgdfg', '', 0);
INSERT INTO `reference_object` VALUES(33, 1, '456gdfh erth', '', 0);
INSERT INTO `reference_object` VALUES(34, 1, 'fgh tyht hytrhg', '', 0);
INSERT INTO `reference_object` VALUES(35, 3, 'fgh fghfghfg', '', 12);
INSERT INTO `reference_object` VALUES(36, 2, 'fgh drthdrthtrhhg', '', 0);
INSERT INTO `reference_object` VALUES(37, 3, 'fj fghdf hfghgfhgfh', '', 6);
INSERT INTO `reference_object` VALUES(38, 3, 'xcvncnfg fghj ', '', 16);
INSERT INTO `reference_object` VALUES(39, 1, 'cxgh dfhd rtydtry drt', '', 0);
INSERT INTO `reference_object` VALUES(40, 1, 'fgh fh fgh gf', '', 0);
INSERT INTO `reference_object` VALUES(41, 3, 'fgh t6uhgfgdaf', '', 32);
INSERT INTO `reference_object` VALUES(42, 3, 'esrytdb h bf', '', 2);
INSERT INTO `reference_object` VALUES(43, 3, 'dfth hsrtg', '', 4);
INSERT INTO `reference_object` VALUES(44, 3, 'dsfg gsfdg', '', 6);

-- --------------------------------------------------------

--
-- Table structure for table `rights`
--

CREATE TABLE `rights` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `right_category_id` int(11) NOT NULL COMMENT 'ID of the category the right belongs to',
  `name` varchar(45) NOT NULL COMMENT 'Unique name of the right',
  `label` varchar(45) NOT NULL COMMENT 'Label is displayed to end users',
  `description` varchar(255) DEFAULT NULL,
  `default_value` tinyint(4) NOT NULL DEFAULT '2',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_right_category` (`right_category_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='System function access is controlled by the role-based autho' AUTO_INCREMENT=51 ;

--
-- Dumping data for table `rights`
--

INSERT INTO `rights` VALUES(1, 1, 'read announcements', 'read announcements', 'Allows users to read admin announcements', 2);
INSERT INTO `rights` VALUES(2, 1, 'read project wall', 'read project wall', 'Allows to read messages on the project wall', 2);
INSERT INTO `rights` VALUES(3, 1, 'write project wall', 'write project wall', 'Allows users to post to the project wall', 2);
INSERT INTO `rights` VALUES(4, 1, 'read content details of others', 'read content details of others', 'Allows user to read details of the content that he is not part of', 2);
INSERT INTO `rights` VALUES(5, 1, 'read content status of others', 'read content status of others', 'Allows user to see status of content that he is not part of ', 2);
INSERT INTO `rights` VALUES(6, 1, 'see all content', 'see all content', 'Allows user to access all content and its status, associated cases and users', 2);
INSERT INTO `rights` VALUES(7, 1, 'manage queues', 'manage queues', 'Allows user to assign/remove users to tasks in the queue, change task priority', 2);
INSERT INTO `rights` VALUES(8, 1, 'open cases', 'open cases', 'Allows user to open new cases.', 2);
INSERT INTO `rights` VALUES(9, 1, 'close all cases', 'close all cases', 'Allows user to close all cases, including cases not opened by herself', 2);
INSERT INTO `rights` VALUES(10, 1, 'attach any content to cases', 'attach any content to cases', 'Allows user to attach to the case content that the user is not part of', 2);
INSERT INTO `rights` VALUES(11, 1, 'attach any user to cases', 'attach any user to cases', 'Allows user to attach to the case any user in the project', 2);
INSERT INTO `rights` VALUES(12, 1, 'assign any user to cases', 'assign any user to cases', 'Allows user to assign the case to any user in the project', 2);
INSERT INTO `rights` VALUES(13, 1, 'manage all cases', 'manage all cases', 'Allows the user to read and edit all cases, including those that are not hers', 2);
INSERT INTO `rights` VALUES(14, 1, 'block content progress', 'block content progress', 'Allows the user to block progress and publishing of the content attached to the case', 2);
INSERT INTO `rights` VALUES(15, 1, 'manage all users', 'manage all users', 'Allows to search and view details of users', 2);
INSERT INTO `rights` VALUES(16, 1, 'write to teams', 'write to teams', 'Allows the user to send messages to teams', 2);
INSERT INTO `rights` VALUES(17, 1, 'see content current tasks', 'see content current tasks', 'Allows the user to see currently active tasks of the content', 2);
INSERT INTO `rights` VALUES(18, 1, 'see content cases', 'see content cases', 'Allows the user to see the ''Cases'' section of the content page', 2);
INSERT INTO `rights` VALUES(19, 1, 'read content internal discussion', 'read content internal discussion', 'Allows user to access internal discussion message board', 2);
INSERT INTO `rights` VALUES(20, 1, 'write content internal discussion', 'write content internal discussion', 'Allows the user to add comments to the internal discussion message board', 2);
INSERT INTO `rights` VALUES(21, 1, 'manage content internal discussion', 'manage content internal discussion', 'Allows user to enhance/publish comments on the internal discussion MB', 2);
INSERT INTO `rights` VALUES(22, 1, 'read journal staff-author discussion', 'read journal staff-author discussion', 'Allows user to access staff-author discussion message board of journal content', 2);
INSERT INTO `rights` VALUES(23, 1, 'manage journal staff-author discussion', 'manage journal staff-author discussion', 'Allows user to enhance/publish comments on the staff-author discussion MB', 2);
INSERT INTO `rights` VALUES(24, 1, 'read journal peer reviews', 'read journal peer reviews', 'Allows the user to read peer reviewers'' opinions', 2);
INSERT INTO `rights` VALUES(25, 1, 'read journal peer review discussions', 'read journal peer review discussions', 'Allows user to access peer review discussion message boards of journal content', 2);
INSERT INTO `rights` VALUES(26, 1, 'manage journal peer review discussions', 'manage journal peer review discussions', 'Allows user to enhance and publish comments on peer review discussion', 2);
INSERT INTO `rights` VALUES(27, 1, 'read survey staff-author discussion', 'read survey staff-author discussion', 'Allows user to access staff-author discussion message board of survey content', 2);
INSERT INTO `rights` VALUES(28, 1, 'manage survey staff-author discussion', 'manage survey staff-author discussion', 'Allows user to enhance and publish comments on the staff-author discussion MB', 2);
INSERT INTO `rights` VALUES(29, 1, 'read survey peer reviews', 'read survey peer reviews', 'Allows the user to read peer reviewers'' opinions of survey content', 2);
INSERT INTO `rights` VALUES(30, 1, 'read survey peer review discussions', 'read survey peer review discussions', 'Allows user to access peer review discussion message boards of survey content', 2);
INSERT INTO `rights` VALUES(31, 1, 'manage survey peer review discussions', 'manage survey peer review discussions', 'Allows user to enhance and publish comments on peer review discussion MB''s', 2);
INSERT INTO `rights` VALUES(32, 1, 'tag indicators', 'tag indicators', 'Allows user to add tags to indicators and to search indicators with tags', 2);
INSERT INTO `rights` VALUES(33, 1, 'see target indicator values', 'see target indicator values', 'Allows user to see indicator values of other targets', 2);
INSERT INTO `rights` VALUES(34, 1, 'use indaba admin', 'use indaba admin', 'Allows user to use Indaba Admin tool (Loader)', 2);
INSERT INTO `rights` VALUES(35, 1, 'manage site', 'manage site', 'Allows user to manage the site(for admin or super user)', 2);
INSERT INTO `rights` VALUES(36, 1, 'access case staff notes', 'access case staff notes', 'Allows user to read/write staff notes to cases', 2);
INSERT INTO `rights` VALUES(37, 1, 'edit any cases', 'edit any cases', 'Allows user to edit any cases including those opened by others', 2);
INSERT INTO `rights` VALUES(38, 1, 'super edit content', 'super edit content', 'Allows user to edit content at any time', 2);
INSERT INTO `rights` VALUES(39, 1, 'write to roles', 'write to roles', 'Allow user to write message to selected roles', 2);
INSERT INTO `rights` VALUES(40, 1, 'edit assignment deadlines', 'edit assignment deadlines', 'Allow user to change task assignment deadlines', 2);
INSERT INTO `rights` VALUES(41, 1, 'see journal file attachments', 'see journal file attachments', 'Allows user to see files attached to journal content', 2);
INSERT INTO `rights` VALUES(42, 1, 'see indicator file attachments', 'see indicator file attachments', 'Allows user to see files attached to survey answers', 2);
INSERT INTO `rights` VALUES(43, 1, 'manage widgets', 'manage widgets', 'manage widgets', 2);
INSERT INTO `rights` VALUES(44, 1, 'manage working sets', 'manage working sets', 'manage working sets', 2);
INSERT INTO `rights` VALUES(45, 1, 'export scorecard reports', 'export scorecard reports', 'can the user export scorecard reports', 2);
INSERT INTO `rights` VALUES(46, 1, 'download and attach files to journal', 'download and attach files to journal', 'this right allows the user to download and upload files to journal content. The user will also be able to delete files attached by self.', 2);
INSERT INTO `rights` VALUES(47, 1, 'delete files from journal', 'delete files from journal', 'this right allows the user to delete all files attached to journal content, including files attached by other users', 2);
INSERT INTO `rights` VALUES(48, 1, 'download and attach files to survey', 'download and attach files to survey', 'this right allows the user to download and upload files to survey content. The user will also be able to delete files attached by self.', 2);
INSERT INTO `rights` VALUES(49, 1, 'delete files from survey', 'delete files from survey', 'this right allows the user to delete all files attached to survey content, including files attached by other users', 2);
INSERT INTO `rights` VALUES(50, 1, 'see task queues', 'see task queues', 'allows the user to see task queues in Field Manager', 2);

-- --------------------------------------------------------

--
-- Table structure for table `right_category`
--

CREATE TABLE `right_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the category',
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Define categories for user rights.' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `right_category`
--

INSERT INTO `right_category` VALUES(1, 'indaba', 'Indaba rules');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique id of the role',
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This table contains definitions of all roles in the system' AUTO_INCREMENT=10 ;

--
-- Dumping data for table `role`
--

INSERT INTO `role` VALUES(2, 'manager', 'Project manager');
INSERT INTO `role` VALUES(3, 'editor', 'Content editor');
INSERT INTO `role` VALUES(4, 'reviewer', 'Staff reviewer');
INSERT INTO `role` VALUES(5, 'peer reviewer', 'Content peer reviewer');
INSERT INTO `role` VALUES(6, 'reporter', 'Reporters write journal reports');
INSERT INTO `role` VALUES(7, 'researcher', 'Researchers answer scorecards');
INSERT INTO `role` VALUES(8, 'support', 'Project support personnel');
INSERT INTO `role` VALUES(9, 'publisher', 'Content publisher');

-- --------------------------------------------------------

--
-- Table structure for table `rule`
--

CREATE TABLE `rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT 'Unique name of the rule',
  `rule_type` tinyint(4) DEFAULT NULL,
  `file_name` varchar(216) DEFAULT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `file_name_UNIQUE` (`file_name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This table stores predefined biz rules for either validation' AUTO_INCREMENT=20 ;

--
-- Dumping data for table `rule`
--

INSERT INTO `rule` VALUES(1, 'Check task before due 1', NULL, NULL, 'IF assignment is {5} days before due, THEN send ''Reminder 1 - Task about due'' to the assigned user;');
INSERT INTO `rule` VALUES(2, 'Check task before due 2', NULL, NULL, 'IF assignment is {2} days before due, THEN send ''Reminder 2 - Task about due'' to the assigned user;');
INSERT INTO `rule` VALUES(3, 'Check task overdue 1', NULL, NULL, 'IF assignment is {2} days overdue, THEN send ''Notice 1 - Task overdue'' to the assigned user;');
INSERT INTO `rule` VALUES(4, 'Check task overdue 2', NULL, NULL, 'IF assignment is {5} days overdue, THEN send ''Notice 2 - Task overdue'' to the assigned user;');
INSERT INTO `rule` VALUES(5, 'Welcome user', NULL, NULL, 'IF task activated THEN send ''Msg - Welcome'' to the assigned user;');
INSERT INTO `rule` VALUES(6, 'Thank user', NULL, NULL, 'IF task is done THEN send ''Msg - Thank you'' to the assigned user;');
INSERT INTO `rule` VALUES(7, 'Confirm task completion', NULL, NULL, 'IF task is done THEN send ''Confirm - Task Completed'' to the assigned user;');
INSERT INTO `rule` VALUES(8, 'Post task completion', NULL, NULL, 'IF task is done THEN post ''Post - Task Completed'' to project wall;');
INSERT INTO `rule` VALUES(9, 'Notify payment', NULL, NULL, 'IF task is done THEN send ''Notify - Payment sent'' to the author;');
INSERT INTO `rule` VALUES(10, 'Report milestone done', NULL, NULL, 'IF all assignments are done THEN send ''Notify - Milestone Completed'' to project admin;');
INSERT INTO `rule` VALUES(11, 'Report milestone about due', NULL, NULL, 'IF not all assignments are done AND it is {5} days before due THEN send ''Alert - Milestone about due'' to project admin;');
INSERT INTO `rule` VALUES(12, 'Report milestone overdue', NULL, NULL, 'IF not all assignments are done AND it is {2} days after due THEN send ''Alert - Milestone Overdue'' to project admin;');
INSERT INTO `rule` VALUES(13, 'Report horse completion', NULL, NULL, 'IF all assignments are done THEN send ''Notify - Horse Completion'' to project admin, and post ''Post - Horse completed'' to the Project Wall;');
INSERT INTO `rule` VALUES(14, 'Post horse completion', NULL, NULL, 'IF all assignments are done THEN post ''Post - Horse Completion'' to project wall;');
INSERT INTO `rule` VALUES(15, 'Goal Exit - normal', NULL, NULL, 'IF all assignments are done THEN exit the goal;');
INSERT INTO `rule` VALUES(16, 'Goal Exit - overdue', NULL, NULL, 'IF the goal is {30} days overdue THEN exit the goal;');
INSERT INTO `rule` VALUES(17, 'Goal Exit - partial completion', NULL, NULL, 'IF {80%} of assignments are completed AND the goal is {10} days overdue THEN exit the goal;');
INSERT INTO `rule` VALUES(18, 'Report task overdue 1', NULL, NULL, 'IF assignment is {5} days overdue THEN send ''Alert 1 - Task Overdue'' to the project admin, and open a ''Task Overdue'' case for the Project Admin;');
INSERT INTO `rule` VALUES(19, 'Report task overdue 2', NULL, NULL, 'IF assignment is {10} days overdue THEN send ''Alert 2 - Task Overdue'' to the project admin;');

-- --------------------------------------------------------

--
-- Table structure for table `sc_contributor`
--

CREATE TABLE `sc_contributor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_config_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_scontrib` (`survey_config_id`,`org_id`),
  KEY `fk_scontrib_survey` (`survey_config_id`),
  KEY `fk_scontrib_org` (`org_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `sc_contributor`
--


-- --------------------------------------------------------

--
-- Table structure for table `sequence_object`
--

CREATE TABLE `sequence_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the SO',
  `workflow_object_id` int(11) NOT NULL COMMENT 'ID of the WFO that this SO belongs to',
  `workflow_sequence_id` int(11) NOT NULL COMMENT 'The ID of the workflow sequence that this SO is instantiated from',
  `status` tinyint(4) NOT NULL COMMENT '0 - waiting\n1 - started\n2 - done',
  PRIMARY KEY (`id`),
  KEY `fk_sequence_object_workflow_sequence1` (`workflow_sequence_id`),
  KEY `fk_sequence_object_wfi_id` (`workflow_object_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='A Sequence Object (SO) is instantiated from workflow sequenc' AUTO_INCREMENT=29 ;

--
-- Dumping data for table `sequence_object`
--

INSERT INTO `sequence_object` VALUES(1, 1, 1, 1);
INSERT INTO `sequence_object` VALUES(2, 2, 2, 1);
INSERT INTO `sequence_object` VALUES(3, 2, 4, 0);
INSERT INTO `sequence_object` VALUES(4, 2, 5, 0);
INSERT INTO `sequence_object` VALUES(5, 2, 6, 0);
INSERT INTO `sequence_object` VALUES(6, 2, 7, 0);
INSERT INTO `sequence_object` VALUES(7, 2, 8, 0);
INSERT INTO `sequence_object` VALUES(8, 3, 1, 1);
INSERT INTO `sequence_object` VALUES(9, 4, 1, 1);
INSERT INTO `sequence_object` VALUES(10, 5, 1, 1);
INSERT INTO `sequence_object` VALUES(11, 6, 2, 1);
INSERT INTO `sequence_object` VALUES(12, 6, 4, 0);
INSERT INTO `sequence_object` VALUES(13, 6, 5, 0);
INSERT INTO `sequence_object` VALUES(14, 6, 6, 0);
INSERT INTO `sequence_object` VALUES(15, 6, 7, 0);
INSERT INTO `sequence_object` VALUES(16, 6, 8, 0);
INSERT INTO `sequence_object` VALUES(17, 7, 2, 1);
INSERT INTO `sequence_object` VALUES(18, 7, 4, 0);
INSERT INTO `sequence_object` VALUES(19, 7, 5, 0);
INSERT INTO `sequence_object` VALUES(20, 7, 6, 0);
INSERT INTO `sequence_object` VALUES(21, 7, 7, 0);
INSERT INTO `sequence_object` VALUES(22, 7, 8, 0);
INSERT INTO `sequence_object` VALUES(23, 8, 2, 1);
INSERT INTO `sequence_object` VALUES(24, 8, 4, 0);
INSERT INTO `sequence_object` VALUES(25, 8, 5, 0);
INSERT INTO `sequence_object` VALUES(26, 8, 6, 0);
INSERT INTO `sequence_object` VALUES(27, 8, 7, 0);
INSERT INTO `sequence_object` VALUES(28, 8, 8, 0);

-- --------------------------------------------------------

--
-- Table structure for table `session`
--

CREATE TABLE `session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(32) NOT NULL COMMENT 'User sessions.',
  `last_updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `ticket_UNIQUE` (`ticket`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `session`
--


-- --------------------------------------------------------

--
-- Table structure for table `source_file`
--

CREATE TABLE `source_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(256) NOT NULL,
  `path` varchar(256) NOT NULL DEFAULT '',
  `extension` varchar(10) NOT NULL DEFAULT '',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `last_update_time` timestamp NULL DEFAULT NULL,
  `note` varchar(512) DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `idx_filename` (`filename`),
  KEY `idx_status` (`status`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `source_file`
--


-- --------------------------------------------------------

--
-- Table structure for table `source_file_text_resource`
--

CREATE TABLE `source_file_text_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_file_id` int(11) NOT NULL,
  `text_resource_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_SOURCE_FILE_ID` (`source_file_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `source_file_text_resource`
--


-- --------------------------------------------------------

--
-- Table structure for table `study_period`
--

CREATE TABLE `study_period` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Definition of study period' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `study_period`
--

INSERT INTO `study_period` VALUES(1, '2010', 'Study period of the year of 2010');

-- --------------------------------------------------------

--
-- Table structure for table `survey_answer`
--

CREATE TABLE `survey_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_content_object_id` int(11) NOT NULL,
  `survey_question_id` int(11) NOT NULL,
  `answer_object_id` int(11) DEFAULT NULL COMMENT 'ID of the answer object in answer_choice_object, answer_number_object or answer_text_object, depending on answer type',
  `reference_object_id` int(11) DEFAULT NULL,
  `comments` text,
  `answer_time` datetime DEFAULT NULL,
  `answer_user_id` int(11) DEFAULT NULL,
  `internal_msgboard_id` int(11) DEFAULT NULL,
  `staff_author_msgboard_id` int(11) DEFAULT NULL,
  `reviewer_has_problem` tinyint(1) NOT NULL DEFAULT '0',
  `problem_submit_time` datetime DEFAULT NULL COMMENT 'Time at which the problem is last submitted by reviewer',
  `problem_resp_time` datetime DEFAULT NULL COMMENT 'Time at which  the author provided last response',
  `staff_reviewed` tinyint(1) NOT NULL DEFAULT '0',
  `edited` tinyint(1) NOT NULL DEFAULT '0',
  `pr_reviewed` tinyint(1) NOT NULL DEFAULT '0',
  `overall_reviewed` tinyint(1) NOT NULL DEFAULT '0',
  `author_responded` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_sa_co_qst` (`survey_content_object_id`,`survey_question_id`),
  KEY `fk_sa_co` (`survey_content_object_id`),
  KEY `fk_sa_internal_msgboard` (`internal_msgboard_id`),
  KEY `fk_sa_uid` (`answer_user_id`),
  KEY `fk_sa_question` (`survey_question_id`),
  KEY `fk_sa_sad_msgboard` (`staff_author_msgboard_id`),
  KEY `fk_sa_ref` (`reference_object_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=23 ;

--
-- Dumping data for table `survey_answer`
--

INSERT INTO `survey_answer` VALUES(1, 1, 1, 23, 1, '', '2012-10-26 10:34:27', 0, 37, 14, 1, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(2, 1, 30, 24, 2, '', '2012-10-26 10:34:35', 0, 38, 15, 1, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(3, 1, 2, 25, 3, '', '2012-10-26 10:34:41', 0, 39, 16, 1, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(4, 1, 3, 26, 4, '', '2012-10-26 10:34:46', 0, 0, 17, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(5, 1, 4, 27, 5, '', '2012-10-26 10:34:56', 0, 0, 18, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(6, 1, 5, 28, 6, '', '2012-10-26 10:35:06', 0, 0, 19, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(7, 1, 6, 29, 7, '', '2012-10-26 10:35:15', 0, 0, 20, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(8, 1, 7, 3, 8, '', '2012-10-26 10:35:26', 0, 0, 21, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(9, 1, 8, 1, 9, '', '2012-10-26 10:35:36', 0, 0, 22, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(10, 1, 9, 4, 10, '', '2012-10-26 10:35:59', 0, 0, 23, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(11, 1, 10, 30, 11, '', '2012-10-26 10:36:12', 0, 0, 24, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(12, 1, 11, 31, 12, '', '2012-10-26 10:36:19', 0, 0, 25, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(13, 1, 12, 32, 13, '', '2012-10-26 10:36:25', 0, 0, 26, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(14, 1, 13, 33, 14, '', '2012-10-26 10:36:37', 0, 0, 27, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(15, 1, 14, 34, 15, '', '2012-10-26 10:36:46', 0, 0, 28, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(16, 1, 15, 35, 16, '', '2012-10-26 10:36:56', 0, 0, 29, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(17, 1, 16, 36, 17, '', '2012-10-26 10:37:04', 0, 0, 30, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(18, 1, 17, 37, 18, '', '2012-10-26 10:37:18', 0, 0, 31, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(19, 1, 18, 38, 19, '', '2012-10-26 10:37:24', 0, 0, 32, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(20, 1, 19, 39, 20, '', '2012-10-26 10:37:31', 0, 0, 33, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(21, 1, 20, 40, 21, '', '2012-10-26 10:37:42', 0, 0, 34, 0, NULL, NULL, 0, 0, 0, 0, 0);
INSERT INTO `survey_answer` VALUES(22, 1, 21, 41, 22, '', '2012-10-26 10:37:51', 0, 0, 35, 0, NULL, NULL, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `survey_answer_attachment`
--

CREATE TABLE `survey_answer_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_answer_id` int(11) NOT NULL,
  `attachment_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_saa_saa` (`survey_answer_id`,`attachment_id`),
  KEY `fk_saa_answer` (`survey_answer_id`),
  KEY `fk_saa_attach` (`attachment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `survey_answer_attachment`
--


-- --------------------------------------------------------

--
-- Table structure for table `survey_answer_attachment_version`
--

CREATE TABLE `survey_answer_attachment_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_answer_version_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `type` varchar(8) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL COMMENT 'file upload time',
  `user_id` int(11) DEFAULT '0' COMMENT 'user who uploaded the file',
  PRIMARY KEY (`id`),
  KEY `fk_saav_version` (`survey_answer_version_id`),
  KEY `fk_saav_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `survey_answer_attachment_version`
--


-- --------------------------------------------------------

--
-- Table structure for table `survey_answer_version`
--

CREATE TABLE `survey_answer_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_version_id` int(11) NOT NULL,
  `survey_question_id` int(11) NOT NULL,
  `answer_object_id` int(11) NOT NULL,
  `reference_object_id` int(11) NOT NULL,
  `comments` text,
  `answer_time` datetime NOT NULL,
  `answer_user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sav_content_qst` (`content_version_id`,`survey_question_id`),
  KEY `sav_question` (`survey_question_id`),
  KEY `sav_ref` (`reference_object_id`),
  KEY `sav_user` (`answer_user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=23 ;

--
-- Dumping data for table `survey_answer_version`
--

INSERT INTO `survey_answer_version` VALUES(1, 2, 1, 42, 23, '', '2012-10-26 10:34:27', 0);
INSERT INTO `survey_answer_version` VALUES(2, 2, 2, 43, 24, '', '2012-10-26 10:34:41', 0);
INSERT INTO `survey_answer_version` VALUES(3, 2, 3, 44, 25, '', '2012-10-26 10:34:46', 0);
INSERT INTO `survey_answer_version` VALUES(4, 2, 4, 45, 26, '', '2012-10-26 10:34:56', 0);
INSERT INTO `survey_answer_version` VALUES(5, 2, 5, 46, 27, '', '2012-10-26 10:35:06', 0);
INSERT INTO `survey_answer_version` VALUES(6, 2, 6, 47, 28, '', '2012-10-26 10:35:15', 0);
INSERT INTO `survey_answer_version` VALUES(7, 2, 7, 4, 29, '', '2012-10-26 10:35:26', 0);
INSERT INTO `survey_answer_version` VALUES(8, 2, 8, 2, 30, '', '2012-10-26 10:35:36', 0);
INSERT INTO `survey_answer_version` VALUES(9, 2, 9, 5, 31, '', '2012-10-26 10:35:59', 0);
INSERT INTO `survey_answer_version` VALUES(10, 2, 10, 48, 32, '', '2012-10-26 10:36:12', 0);
INSERT INTO `survey_answer_version` VALUES(11, 2, 11, 49, 33, '', '2012-10-26 10:36:19', 0);
INSERT INTO `survey_answer_version` VALUES(12, 2, 12, 50, 34, '', '2012-10-26 10:36:25', 0);
INSERT INTO `survey_answer_version` VALUES(13, 2, 13, 51, 35, '', '2012-10-26 10:36:37', 0);
INSERT INTO `survey_answer_version` VALUES(14, 2, 14, 52, 36, '', '2012-10-26 10:36:46', 0);
INSERT INTO `survey_answer_version` VALUES(15, 2, 15, 53, 37, '', '2012-10-26 10:36:56', 0);
INSERT INTO `survey_answer_version` VALUES(16, 2, 16, 54, 38, '', '2012-10-26 10:37:04', 0);
INSERT INTO `survey_answer_version` VALUES(17, 2, 17, 55, 39, '', '2012-10-26 10:37:18', 0);
INSERT INTO `survey_answer_version` VALUES(18, 2, 18, 56, 40, '', '2012-10-26 10:37:24', 0);
INSERT INTO `survey_answer_version` VALUES(19, 2, 19, 57, 41, '', '2012-10-26 10:37:31', 0);
INSERT INTO `survey_answer_version` VALUES(20, 2, 20, 58, 42, '', '2012-10-26 10:37:42', 0);
INSERT INTO `survey_answer_version` VALUES(21, 2, 21, 59, 43, '', '2012-10-26 10:37:51', 0);
INSERT INTO `survey_answer_version` VALUES(22, 2, 30, 60, 44, '', '2012-10-26 10:34:35', 0);

-- --------------------------------------------------------

--
-- Table structure for table `survey_category`
--

CREATE TABLE `survey_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_config_id` int(11) NOT NULL,
  `parent_category_id` int(11) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(225) NOT NULL,
  `label` varchar(45) NOT NULL COMMENT 'Displayed before the title text',
  `title` text,
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_sc_parent` (`parent_category_id`),
  KEY `fk_sc_config` (`survey_config_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `survey_category`
--

INSERT INTO `survey_category` VALUES(1, 1, 0, 'Cat_I', '1st top category', 'I', 'Civil Society, Public Information and Media', 1);
INSERT INTO `survey_category` VALUES(2, 1, 1, 'Cat_I-1', '1st subcategory of Category I', 'I-1', 'Civil Society Organizations', 1);
INSERT INTO `survey_category` VALUES(3, 1, 2, 'Set_I-1-1', '1st question set of I-1', '1', 'Are anti-corruption/good governance CSOs legally protected?', 1);
INSERT INTO `survey_category` VALUES(4, 1, 2, 'Set_I-1-2', '2nd question set of Category I-1', '2', 'Are good governance/anti-corruption CSOs able to operate freely?', 2);
INSERT INTO `survey_category` VALUES(5, 1, 1, 'Cat_I-2', '2nd subcategory of Category I', 'I-2', 'Media', 2);
INSERT INTO `survey_category` VALUES(6, 1, 5, 'Set_I-2-1', '1st question set in subcat I-2', '5', 'Are media and free speech protected?', 1);
INSERT INTO `survey_category` VALUES(7, 1, 5, 'Set_I-2-2', '2nd question set of Cat I-2', '6', 'Are citizens able to form print media entities?', 2);
INSERT INTO `survey_category` VALUES(8, 1, 0, 'Cat_II', '2nd top category', 'II', 'Elections', 2);
INSERT INTO `survey_category` VALUES(9, 1, 8, 'Cat_II-1', '1st subcat of cat II', 'II-1', 'Voting & Citizen Participation', 1);
INSERT INTO `survey_category` VALUES(10, 1, 9, 'set_II-1-1', '1st question set of II-1', '14', 'Is there a legal framework guaranteeing the right to vote?', 1);
INSERT INTO `survey_category` VALUES(11, 1, 9, 'set_II-1-2', '2nd question set of II-1', '15', 'Can all citizens exercise their right to vote?', 2);
INSERT INTO `survey_category` VALUES(12, 2, 0, 'Cat_I', '1st top category', 'I', 'Background', 1);
INSERT INTO `survey_category` VALUES(13, 2, 12, 'Cat_I-1', '1st subcat of category I', 'I-1', 'Basic Info', 1);
INSERT INTO `survey_category` VALUES(14, 2, 13, 'Set_I-1-1', '1st question set of I-1', '1', 'Basic Statistics', 1);
INSERT INTO `survey_category` VALUES(15, 2, 0, 'Cat_II', '2nd top category', 'II', 'Energy Study', 2);
INSERT INTO `survey_category` VALUES(16, 2, 15, 'cat_II-1', '1st subcat of II', 'II-1', 'Policies', 1);
INSERT INTO `survey_category` VALUES(17, 2, 16, 'Set_II-1-1', '1st question set of II-1', '2', 'Laws and Regulations', 1);
INSERT INTO `survey_category` VALUES(18, 2, 15, 'Cat_II-2', '2nd subcat of II', 'II-2', 'Energy Sources', 2);
INSERT INTO `survey_category` VALUES(19, 2, 18, 'Set_II-2-1', '1st question set of II-2', '3', 'Energy Sources', 1);

-- --------------------------------------------------------

--
-- Table structure for table `survey_config`
--

CREATE TABLE `survey_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT 'Unique name of the config',
  `description` varchar(255) DEFAULT NULL,
  `instructions` text NOT NULL COMMENT 'ID of the text resource used as user instructions',
  `moe_algorithm` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = GI specific',
  `is_tsc` tinyint(1) DEFAULT '1',
  `creator_org_id` int(11) NOT NULL DEFAULT '1',
  `owner_org_id` int(11) NOT NULL DEFAULT '1',
  `visibility` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = private;\n2 = public;',
  `language_id` int(11) NOT NULL DEFAULT '1',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = active;\n2 = inactive;\n3 = deleted;',
  `create_time` timestamp NULL DEFAULT NULL,
  `delete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Each product must have a config for its content type. This t' AUTO_INCREMENT=3 ;

--
-- Dumping data for table `survey_config`
--

INSERT INTO `survey_config` VALUES(1, 'Scorecard 2010', 'Scorecard config for Project 2010', 'Please complete the questions the best you can. When in doubt, feel free to contact the project management team.', 1, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_config` VALUES(2, 'Energy Policy Survey 2010', 'Definition of energy policy survey for the year 2010', 'Please provide answers to the questions in the survey. Make sure to provide source of data.', 1, 1, 1, 1, 1, 1, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `survey_content_object`
--

CREATE TABLE `survey_content_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID of the SCO',
  `content_header_id` int(11) NOT NULL COMMENT 'ID of the content header that keeps common attributes for this SCO.',
  `survey_config_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_header_id_UNIQUE` (`content_header_id`),
  KEY `fk_sco_ch` (`content_header_id`),
  KEY `fk_sco_config` (`survey_config_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='This table keeps all created suvey content objects (SCO). A ' AUTO_INCREMENT=5 ;

--
-- Dumping data for table `survey_content_object`
--

INSERT INTO `survey_content_object` VALUES(1, 2, 1);
INSERT INTO `survey_content_object` VALUES(2, 6, 1);
INSERT INTO `survey_content_object` VALUES(3, 7, 1);
INSERT INTO `survey_content_object` VALUES(4, 8, 1);

-- --------------------------------------------------------

--
-- Table structure for table `survey_indicator`
--

CREATE TABLE `survey_indicator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `question` text NOT NULL,
  `answer_type` tinyint(4) NOT NULL COMMENT '1 - single choice\n2 - multi choice\n3 - integer\n4 - float\n5 - text\n\n',
  `answer_type_id` int(11) NOT NULL COMMENT 'ID of the type definition in answer_type_choice, answer_type_number, or answer_type_text table',
  `reference_id` int(11) NOT NULL,
  `tip` text COMMENT 'Additional tip text',
  `create_user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `reusable` tinyint(1) DEFAULT NULL,
  `original_indicator_id` int(11) DEFAULT NULL,
  `language_id` int(11) NOT NULL DEFAULT '1',
  `creator_org_id` int(11) NOT NULL DEFAULT '1' COMMENT 'Org that created the indicator',
  `owner_org_id` int(11) NOT NULL DEFAULT '1' COMMENT 'Org that owns the indicator',
  `visibility` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = public;\n2 = private;',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'Only for public indicator:\n\n1 = endorsed;\n2 = extended;\n3 = test;',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = active;\n2 = inactive;\n3 = deleted;',
  `delete_time` timestamp NULL DEFAULT NULL,
  `delete_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_survey_indicator_document_type1` (`reference_id`),
  KEY `fk_survey_user` (`create_user_id`),
  KEY `fk_survey_original_ind` (`original_indicator_id`),
  KEY `fk_surveyind_delete_user` (`delete_user_id`),
  KEY `fk_surveyind_language` (`language_id`),
  KEY `fk_surveyind_ownerorg` (`owner_org_id`),
  KEY `fk_surveyind_creatororg` (`creator_org_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Survey Element structure defines the header info of the indi' AUTO_INCREMENT=31 ;

--
-- Dumping data for table `survey_indicator`
--

INSERT INTO `survey_indicator` VALUES(1, 'Anti-Corruption-001', 'In law, citizens have a right to form civil society organizations (CSOs) focused on anti-corruption or good governance.', 1, 1, 1, NULL, 1, '2010-06-10 22:07:12', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(2, 'Anti-Corruption-002', 'In law, anti-corruption/good governance CSOs are free to accept funding from any foreign or domestic sources.', 1, 2, 1, NULL, 1, '2010-06-10 22:11:34', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(3, 'Anti-Corruption-003', 'In law, anti-corruption/good governance CSOs are required to disclose their sources of funding.', 1, 3, 1, 'Such funding disclosures should be publicly accessible in some way and not simply disclosed only to the government.', 1, '2010-06-10 22:12:34', 1, 2, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(4, 'Anti-Corruption-004', 'In practice, the government does not create barriers to the organization of new anti-corruption/good governance CSOs.', 1, 4, 3, 'In your comments, please provide both general statements on the environment within which CSOs operate as well as references to challenges facing specific CSOs.', 1, '2010-06-10 22:15:55', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(5, 'Anti-Corruption-005', 'In practice, anti-corruption/good governance CSOs actively engage in the political and policymaking process.', 1, 5, 3, 'Please provide comments on specific CSOs that have been successful or unsuccessful in their political outreach.', 1, '2010-06-11 11:49:07', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(8, 'Anti-Corruption-006', 'In practice, no anti-corruption/good governance CSOs have been shut down by the government for their work on corruption-related issues during the study period.', 1, 8, 3, 'Beware of confusion with the double negative in this indicator. Please pay close attention to the scoring criteria. When scoring this indicator, be sure to take a broad interpretation of Ã¢â‚¬Å“corruption.Ã¢â‚¬Â This can mean issues related to governance practices a requirement.', 1, '2010-06-11 13:27:22', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(9, 'Anti-Corruption-007', 'What anti-corruption methods are available to citizens?', 2, 9, 1, NULL, 1, '2010-06-11 14:34:16', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(10, 'Basic-001', 'How many states or provinces in the nation?', 3, 1, 1, NULL, 1, '2010-06-11 14:41:02', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(11, 'Basic-002', 'Please give the names of the 3 biggest cities in the nation.', 5, 1, 2, NULL, 1, '2010-06-11 14:42:10', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(12, 'Basic-003', 'What''s the average hourly wage in the nation?', 4, 1, 1, NULL, 1, '2010-06-11 14:42:50', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(13, 'Basic-004', 'How many major cities in the nation?', 3, 2, 1, NULL, 1, '2010-06-11 14:43:35', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(14, 'Election-001', 'In law, there is a legal framework requiring that elections be held at regular intervals.', 1, 10, 1, NULL, 1, '2010-06-11 14:44:12', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(15, 'Election-002', 'In practice, all adult citizens can vote.', 1, 11, 3, 'A YES score can still be earned if a basic age requirement exists.', 1, '2010-06-11 14:46:01', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(16, 'Election-003', 'In practice, ballots are secret or equivalently protected.', 1, 12, 3, 'When scoring this indicator, please consider: are citizens able to vote in secret or are there government, military or party agents overtly pressuring voters at the polls? Are the ballots protected when they are transported? Have there been examples of tampering of ballots before or after voting periods?  Please provide any useful examples in your Comments.', 1, '2010-06-11 14:48:04', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(17, 'Election-004', 'In practice, elections are held according to a regular schedule.', 1, 13, 3, NULL, 1, '2010-06-11 14:50:25', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(18, 'Energy-Policy-001', 'Is there any legislation about energy conservation and alternative energy sources?', 1, 14, 1, NULL, 1, '2010-06-11 14:52:09', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(19, 'Energy-Policy-002', 'What are the main energy sources in the nation?', 2, 15, 3, NULL, 1, '2010-06-11 14:53:26', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(20, 'Energy-Policy-003', 'Is there any incentive programs for the use of green energy?', 1, 16, 3, 'The following energy sources are typically considered Green: solar, wind, water.', 1, '2010-06-11 14:55:40', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(21, 'Energy-Policy-004', 'What''s the average daily sunshine time (in hours) in summer?', 4, 2, 3, NULL, 1, '2010-06-11 14:58:39', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(22, 'Energy-Policy-005', 'What''s the average daily sunshine time (in hours) in seasons other than summer?', 4, 3, 3, NULL, 1, '2010-06-11 14:59:38', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(23, 'Energy-Policy-006', 'Which energy source is the primary source?', 1, 17, 3, NULL, 1, '2010-06-11 15:00:14', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(24, 'Freedom-001', 'In law, freedom of the media is guaranteed.', 1, 18, 1, NULL, 1, '2010-06-11 15:03:30', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(25, 'Freedom-002', 'In law, freedom of speech is guaranteed.', 1, 19, 1, 'Please pay careful attention to the NO criteria; a NO score should be triggered if there are specific prohibitions against speaking out against particular public figures or political leaders.', 1, '2010-06-11 15:04:55', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(26, 'Freedom-003', 'In practice, the government does not create barriers to form a print media entity.', 1, 20, 3, NULL, 1, '2010-06-11 15:05:56', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(27, 'Freedom-004', 'In law, where a print media license is necessary, there is an appeals mechanism if a license is denied or revoked.', 1, 21, 2, 'In addition to providing the name of the appeals mechanism, please also provide some details on how the appeals mechanism works.', 1, '2010-06-11 15:23:52', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(28, 'Freedom-005', 'In practice, where necessary, citizens can obtain a print media license within a reasonable time period.', 1, 22, 3, NULL, 1, '2010-06-11 15:26:52', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(29, 'Freedom-006', 'In practice, where necessary, citizens can obtain a print media license at a reasonable cost.', 1, 23, 3, NULL, 1, '2010-06-11 15:28:28', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `survey_indicator` VALUES(30, 'Freedom-007', 'In law, universal and equal adult suffrage is guaranteed to all citizens.', 1, 24, 1, NULL, 1, '2010-06-11 15:30:02', 1, NULL, 1, 1, 1, 1, 1, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `survey_indicator_intl`
--

CREATE TABLE `survey_indicator_intl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_indicator_id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `question` text NOT NULL,
  `tip` text,
  `criteria` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_siintl_indlang` (`survey_indicator_id`,`language_id`),
  KEY `fk_siintl_ind` (`survey_indicator_id`),
  KEY `fk_siintl_lang` (`language_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `survey_indicator_intl`
--


-- --------------------------------------------------------

--
-- Table structure for table `survey_peer_review`
--

CREATE TABLE `survey_peer_review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_answer_id` int(11) NOT NULL,
  `opinion` tinyint(4) NOT NULL COMMENT '0 - agree\n1 - agree with comments\n2 - disagree\n3 - not qualified',
  `suggested_answer_object_id` int(11) DEFAULT NULL COMMENT 'Referenced table depends answer type: answer_object_choice, number, or text',
  `comments` text,
  `last_change_time` datetime NOT NULL,
  `reviewer_user_id` int(11) NOT NULL COMMENT 'uid who created this review',
  `msgboard_id` int(11) NOT NULL,
  `submit_time` datetime DEFAULT NULL COMMENT 'When the peer review was submitted',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_spr_answer_user` (`survey_answer_id`,`reviewer_user_id`),
  KEY `fk_survey_peer_review_user1` (`reviewer_user_id`),
  KEY `fk_survey_peer_review_messageboard1` (`msgboard_id`),
  KEY `fk_spr_answer` (`survey_answer_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `survey_peer_review`
--


-- --------------------------------------------------------

--
-- Table structure for table `survey_question`
--

CREATE TABLE `survey_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `survey_config_id` int(11) NOT NULL,
  `survey_indicator_id` int(11) NOT NULL,
  `survey_category_id` int(11) NOT NULL DEFAULT '0' COMMENT '0 if this element is the root (no parent)',
  `public_name` varchar(45) NOT NULL,
  `default_answer_id` int(11) DEFAULT NULL COMMENT 'The ID of default answer object: answer_choice_object, answer_number_object, or answer_text_object',
  `weight` int(11) DEFAULT NULL COMMENT 'used to determine display order of the element in the survey',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_sce` (`survey_config_id`,`survey_indicator_id`),
  KEY `fk_survey_item_config` (`survey_config_id`),
  KEY `fk_survey_item_indicator` (`survey_indicator_id`),
  KEY `fk_survey_item_category` (`survey_category_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='associate indicator with survey category\n' AUTO_INCREMENT=31 ;

--
-- Dumping data for table `survey_question`
--

INSERT INTO `survey_question` VALUES(1, 'Anti-Corruption-001: 1a', 1, 1, 3, '1a', 1, 1);
INSERT INTO `survey_question` VALUES(2, 'Anti-Corruption-002: 1b', 1, 2, 3, '1b', 2, 2);
INSERT INTO `survey_question` VALUES(3, 'Anti-Corruption-003: 1c', 1, 3, 3, '1c', 3, 3);
INSERT INTO `survey_question` VALUES(4, 'Anti-Corruption-004: 2a', 1, 4, 4, '2a', 4, 1);
INSERT INTO `survey_question` VALUES(5, 'Anti-Corruption-005: 2b', 1, 5, 4, '2b', 5, 2);
INSERT INTO `survey_question` VALUES(6, 'Anti-Corruption-006: 2c', 1, 8, 4, '2c', 6, 3);
INSERT INTO `survey_question` VALUES(7, 'Basic-001: 2x', 1, 10, 4, '2x', 1, 4);
INSERT INTO `survey_question` VALUES(8, 'Basic-002: 2y', 1, 11, 4, '2y', 0, 5);
INSERT INTO `survey_question` VALUES(9, 'Basic-003: 2z', 1, 12, 4, '2z', 1, 6);
INSERT INTO `survey_question` VALUES(10, 'Anti-Corruption-007: 2w', 1, 9, 4, '2w', 7, 7);
INSERT INTO `survey_question` VALUES(11, 'Freedom-001: 5a', 1, 24, 6, '5a', 8, 1);
INSERT INTO `survey_question` VALUES(12, 'Freedom-002: 5b', 1, 25, 6, '5b', 9, 2);
INSERT INTO `survey_question` VALUES(13, 'Freedom-003: 6a', 1, 26, 7, '6a', 10, 1);
INSERT INTO `survey_question` VALUES(14, 'Freedom-004: 6b', 1, 27, 7, '6b', 11, 2);
INSERT INTO `survey_question` VALUES(15, 'Freedom-005: 6c', 1, 28, 7, '6c', 12, 3);
INSERT INTO `survey_question` VALUES(16, 'Freedom-006: 6d', 1, 29, 7, '6d', 13, 4);
INSERT INTO `survey_question` VALUES(17, 'Freedom-007: 14a', 1, 30, 10, '14a', 14, 1);
INSERT INTO `survey_question` VALUES(18, 'Election-001: 14b', 1, 14, 10, '14b', 15, 2);
INSERT INTO `survey_question` VALUES(19, 'Election-002: 15a', 1, 15, 11, '15a', 16, 1);
INSERT INTO `survey_question` VALUES(20, 'Election-003: 15b', 1, 16, 11, '15b', 17, 2);
INSERT INTO `survey_question` VALUES(21, 'Election-004: 15c', 1, 17, 11, '15c', 18, 3);
INSERT INTO `survey_question` VALUES(22, 'Basic-001: 1a', 2, 10, 14, '1a', 2, 1);
INSERT INTO `survey_question` VALUES(23, 'Basic-002: 1b', 2, 11, 14, '1b', 0, 2);
INSERT INTO `survey_question` VALUES(24, 'Energy-Policy-001: 2a', 2, 18, 17, '2a', 19, 1);
INSERT INTO `survey_question` VALUES(25, 'Energy-Policy-003: 2b', 2, 20, 17, '2b', 20, 2);
INSERT INTO `survey_question` VALUES(26, 'Energy-Policy-002: 3a', 2, 19, 19, '3a', 21, 2);
INSERT INTO `survey_question` VALUES(27, 'Energy-Policy-004: 3b', 2, 21, 19, '3b', 2, 3);
INSERT INTO `survey_question` VALUES(28, 'Energy-Policy-005: 3c', 2, 22, 19, '3c', 3, 5);
INSERT INTO `survey_question` VALUES(29, 'Energy-Policy-006: 3d', 2, 23, 19, '3d', 22, 6);
INSERT INTO `survey_question` VALUES(30, 'Energy-Policy-006', 1, 23, 3, 'Energy-Policy-006', NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_type` tinyint(4) NOT NULL COMMENT '0 - user tag\n1 - system tag',
  `tagged_object_type` smallint(6) NOT NULL COMMENT 'Object type',
  `tagged_object_id` int(11) NOT NULL,
  `tagged_object_scope_id` int(11) DEFAULT NULL COMMENT 'ID of the object that defines the scope of the tagged object',
  `tagging_time` datetime NOT NULL,
  `user_id` int(11) NOT NULL COMMENT 'user who applied the tag',
  `label` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tag` (`tagged_object_type`,`tagged_object_id`,`label`),
  KEY `fk_tag_user1` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `tag`
--


-- --------------------------------------------------------

--
-- Table structure for table `target`
--

CREATE TABLE `target` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `target_type` tinyint(4) NOT NULL COMMENT '0 - region\nmore to follow',
  `short_name` varchar(45) NOT NULL COMMENT 'Short name of the target. ',
  `guid` varchar(255) DEFAULT NULL COMMENT 'global id of the target. typically a uri.',
  `creator_org_id` int(11) NOT NULL DEFAULT '1',
  `owner_org_id` int(11) NOT NULL DEFAULT '1',
  `visibility` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = public;\n2 = private;',
  `language_id` int(11) NOT NULL DEFAULT '1',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 = active;\n2 = inactive;\n3 = deleted;',
  `create_time` timestamp NULL DEFAULT NULL,
  `delete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `short_name_UNIQUE` (`short_name`),
  KEY `fk_target_creator` (`creator_org_id`),
  KEY `fk_target_owner` (`owner_org_id`),
  KEY `fk_target_language` (`language_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Unique definition for each country/region' AUTO_INCREMENT=8 ;

--
-- Dumping data for table `target`
--

INSERT INTO `target` VALUES(1, 'US', 'United States', 0, 'USA', NULL, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `target` VALUES(2, 'Argentina', 'Argentina', 0, 'ARG', NULL, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `target` VALUES(3, 'Brazil', 'Brazil', 0, 'BRZ', NULL, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `target` VALUES(4, 'China', 'PR China', 0, 'CHN', NULL, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `target` VALUES(5, 'Japan', 'Japan', 0, 'JAP', NULL, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `target` VALUES(6, 'Taiwan', 'Taiwan', 1, 'TWN', NULL, 1, 1, 1, 1, 1, NULL, NULL);
INSERT INTO `target` VALUES(7, 'UK', 'United Kingdom', 0, 'UK', NULL, 1, 1, 1, 1, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `target_tag`
--

CREATE TABLE `target_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `target_id` int(11) NOT NULL,
  `ttags_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tt_tt` (`target_id`,`ttags_id`),
  KEY `fk_tt_target` (`target_id`),
  KEY `fk_tt_ttags` (`ttags_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `target_tag`
--


-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `goal_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `tool_id` int(11) NOT NULL,
  `assignment_method` tinyint(4) NOT NULL COMMENT '1 - manual\n2 - queue\n3 - dynamic',
  `instructions` text NOT NULL,
  `type` tinyint(4) NOT NULL DEFAULT '-1' COMMENT 'Task type',
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_name_UNIQUE` (`task_name`,`product_id`),
  KEY `fk_task_goal` (`goal_id`),
  KEY `fk_task_tool` (`tool_id`),
  KEY `fk_task_product` (`product_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=43 ;

--
-- Dumping data for table `task`
--

INSERT INTO `task` VALUES(-1, 'Journal Review Response', 'Author provides response to review feedback', 0, 0, 16, 3, 'Use this tool to provide response to reviewer feedback or questions.', 16);
INSERT INTO `task` VALUES(-2, 'Survey Review Response', 'Author provides response to review feedback', 0, 0, 17, 3, 'Use this tool to provide response to reviewer feedback or questions.', 17);
INSERT INTO `task` VALUES(1, 'nb_submit_1', 'First notebook submission', 9, 1, 1, 1, 'Your content will be completed in 2 rounds of submission. This is the first round of notebook submission.', 1);
INSERT INTO `task` VALUES(2, 'nb_submit_2', '2nd submission', 10, 1, 1, 1, 'This is the 2nd round of notebook submission.', 1);
INSERT INTO `task` VALUES(3, 'nb_staff_review_1', '1st staff review', 7, 1, 3, 2, 'This is the 1st round of staff review.', 3);
INSERT INTO `task` VALUES(4, 'nb_staff_review_2', '2nd staff review', 8, 1, 3, 2, 'This is the second round of staff review.', 3);
INSERT INTO `task` VALUES(41, 'nb_review_resp', 'response to review feedback', 8, 1, 16, 3, 'Please provide response to reviewer feedback or questions.', 16);
INSERT INTO `task` VALUES(5, 'nb_edit_1', '1st edit', 1, 1, 2, 2, 'This is the first round of edit.', 2);
INSERT INTO `task` VALUES(6, 'nb_edit_2', '2nd edit', 2, 1, 2, 2, 'This is the 2nd content edit.', 2);
INSERT INTO `task` VALUES(7, 'nb_edit_review', 'Review results of edit', 3, 1, 2, 2, 'You are required to review the results of content edits.', 2);
INSERT INTO `task` VALUES(8, 'nb_peer_review', 'Peer review', 5, 1, 4, 1, 'Please review the content and provide your feedback.', 4);
INSERT INTO `task` VALUES(9, 'nb_pr_review', 'Review results of peer review', 6, 1, 13, 2, 'You are required to review the results of peer reviews.', 13);
INSERT INTO `task` VALUES(10, 'nb_overall_review', 'Overall review', 4, 1, 18, 1, 'This is the final review of the content.', 18);
INSERT INTO `task` VALUES(11, 'sc_submit_1', 'First submission', 11, 2, 6, 1, 'You will complete the scorecard in 3 rounds. This is the 1st round of submission.', 6);
INSERT INTO `task` VALUES(12, 'sc_submit_2', '2nd submission', 13, 2, 6, 1, 'This is the 2nd round of submission.', 6);
INSERT INTO `task` VALUES(13, 'sc_review_1', '1st staff review', 12, 2, 8, 2, 'This is the 1st round of staff review', 8);
INSERT INTO `task` VALUES(14, 'sc_review_2', '2nd staff review', 14, 2, 8, 2, 'This is the 2nd staff review', 8);
INSERT INTO `task` VALUES(15, 'sc_submit_3', '2rd submission', 16, 2, 6, 1, 'This is the 3rd round of submission', 6);
INSERT INTO `task` VALUES(16, 'sc_review_3', '3rd staff review', 17, 2, 8, 2, 'This is the 3rd staff review', 8);
INSERT INTO `task` VALUES(42, 'sc_review_resp', 'response to review feedback', 17, 2, 17, 3, 'Please provide response to reviewer feedback or questions.', 17);
INSERT INTO `task` VALUES(17, 'sc_peer_review', 'Peer review', 15, 2, 9, 1, 'Please provide your opinions about the scorecard.', 9);
INSERT INTO `task` VALUES(18, 'sc_pr_review', 'Review of peer review', 18, 2, 14, 2, 'Please review the results of peer reviews', 14);
INSERT INTO `task` VALUES(20, 'sc_pay_1', '1st payment', 19, 2, 11, 1, 'Please make the 1st payment to the author', 11);
INSERT INTO `task` VALUES(21, 'sc_pay_2', '2nd payment', 20, 2, 11, 1, 'Please make the 2nd payment to the author', 11);
INSERT INTO `task` VALUES(22, 'sc_start_horse', 'Start horse', 21, 2, 15, 2, 'Please review and edit peer reviews.', 15);
INSERT INTO `task` VALUES(23, 'sc_edit', 'Edit the scorecard', 22, 2, 7, 2, 'Please review and edit the scorecard', 7);
INSERT INTO `task` VALUES(24, 'epa_submit', 'Submit content', 25, 3, 1, 1, 'Please create and submit your analysis of your country''s energy policy.', 1);
INSERT INTO `task` VALUES(25, 'epa_review', 'Review the content', 26, 3, 3, 2, 'Please review the content and provide your feedback. You can also discuss with the author by using the staff/author discussion box.', 3);
INSERT INTO `task` VALUES(26, 'epa_edit', 'Edit content', 27, 3, 2, 2, 'Please edit the essay: fix typos, correct grammar errors, or rewrite the sentences. But please make sure that author''s opinions are not changed.', 2);
INSERT INTO `task` VALUES(27, 'epa_peer_review', 'Peer review', 28, 3, 4, 1, 'Please read the essay and provide your opinions.', 4);
INSERT INTO `task` VALUES(28, 'epa_pr_review', 'PR review', 29, 3, 13, 2, 'Please review the peer reviews, and provide your feedback in each peer-review''s discussion box.', 13);
INSERT INTO `task` VALUES(29, 'epa_start', 'Approve to start the workflow', 32, 3, 15, 1, 'The workflow will not continue until you start it.', 15);
INSERT INTO `task` VALUES(30, 'epa_approve', 'Approve and accept the content', 30, 3, 12, 1, 'Approve and accept the content after review it.', 12);
INSERT INTO `task` VALUES(31, 'epa_pay', 'Make payment to the author', 31, 3, 11, 1, 'Please make payment to the author', 11);
INSERT INTO `task` VALUES(32, 'eps_submit', 'Submit the survey', 25, 4, 6, 1, 'Please complete and submit the survey.', 6);
INSERT INTO `task` VALUES(33, 'eps_review', 'Review the survey', 26, 4, 8, 2, 'Please review the survey and provide your feedback.', 8);
INSERT INTO `task` VALUES(34, 'eps_approve', 'Approve and accept the content', 30, 4, 12, 1, 'Content is not accepted until you approve it.', 12);
INSERT INTO `task` VALUES(35, 'eps_edit', 'Edit content', 27, 4, 7, 2, 'Please edit the survey: fix typos and grammar errors.', 7);
INSERT INTO `task` VALUES(36, 'eps_pay', 'Pay the author', 31, 4, 11, 1, 'Please make payment to the author.', 11);
INSERT INTO `task` VALUES(37, 'eps_peer_review', 'Peer review', 28, 4, 9, 1, 'Please review the survey and provide your opinions.', 9);
INSERT INTO `task` VALUES(38, 'eps_pr_review', 'Review of peer reviews', 29, 4, 14, 2, 'Please review all peer reviews and provide your feedback', 14);
INSERT INTO `task` VALUES(39, 'eps_start', 'Start the workflow', 32, 4, 15, 1, 'Please approve to start the workflow process.', 15);
INSERT INTO `task` VALUES(40, 'sc_finish', 'Finish up', 24, 2, 19, 1, 'This is the final touch-up of the content. All required tasks have been completed.', 19);

-- --------------------------------------------------------

--
-- Table structure for table `tasksub`
--

CREATE TABLE `tasksub` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `notify` tinyint(1) NOT NULL COMMENT 'whether to notify the user of new event of the task',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tasksub` (`user_id`,`task_id`),
  KEY `fk_tasksub_user` (`user_id`),
  KEY `fk_tasksub_task` (`task_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `tasksub`
--


-- --------------------------------------------------------

--
-- Table structure for table `task_assignment`
--

CREATE TABLE `task_assignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `target_id` int(11) NOT NULL,
  `assigned_user_id` int(11) NOT NULL,
  `due_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 - inactive\n1 - active\n2 - aware\n3 - noticed\n4 - started\n5 - done',
  `start_time` datetime DEFAULT NULL,
  `completion_time` datetime DEFAULT NULL,
  `event_log_id` int(11) DEFAULT NULL,
  `q_enter_time` datetime DEFAULT NULL,
  `q_last_assigned_time` datetime DEFAULT NULL,
  `q_last_assigned_uid` int(11) DEFAULT NULL,
  `q_last_return_time` datetime DEFAULT NULL,
  `q_priority` tinyint(4) DEFAULT NULL COMMENT '1 - low\n2 - medium\n3 - high',
  `data` text COMMENT 'Used by app to stored any app-processing data',
  `goal_object_id` int(11) DEFAULT NULL COMMENT 'Used by app to remember id of the goal object that the assignment belongs to',
  `percent` float DEFAULT '-1' COMMENT 'Percent of completion',
  `horse_id` int(11) NOT NULL,
  `exit_type` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ta_task_horse_uid` (`task_id`,`assigned_user_id`,`horse_id`),
  KEY `fk_ta_task` (`task_id`),
  KEY `fk_ta_target` (`target_id`),
  KEY `fk_ta_assigned` (`assigned_user_id`),
  KEY `fk_ta_event_log` (`event_log_id`),
  KEY `fk_ta_goal_object` (`goal_object_id`),
  KEY `fk_ta_horse` (`horse_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=102 ;

--
-- Dumping data for table `task_assignment`
--

INSERT INTO `task_assignment` VALUES(1, 1, 1, 16, '2012-11-02 11:36:25', 5, '2012-10-23 11:36:25', '2012-10-23 11:36:52', 1, NULL, '2010-07-20 06:36:21', 0, NULL, 0, NULL, 9, 1.0001, 1, 0);
INSERT INTO `task_assignment` VALUES(2, 1, 2, 13, '2012-11-02 11:36:27', 1, '2012-10-23 11:36:27', NULL, 2, NULL, '2010-07-20 08:29:37', 0, NULL, 0, NULL, 32, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(3, 1, 3, 14, '2012-11-02 11:36:27', 1, '2012-10-23 11:36:27', NULL, 3, NULL, '2010-07-20 08:29:55', 0, NULL, 0, NULL, 42, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(4, 1, 4, 15, '2012-11-02 11:36:28', 1, '2012-10-23 11:36:28', NULL, 4, NULL, '2010-07-20 08:30:07', 0, NULL, 0, NULL, 52, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(5, 2, 1, 16, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:36:16', NULL, NULL, NULL, NULL, 10, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(6, 2, 2, 13, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:36:27', NULL, NULL, NULL, NULL, 33, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(7, 2, 3, 14, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:36:38', NULL, NULL, NULL, NULL, 43, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(8, 2, 4, 15, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:36:50', NULL, NULL, NULL, NULL, 53, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(9, 8, 1, 21, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 16:48:39', NULL, NULL, NULL, NULL, 5, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(10, 8, 1, 17, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:50:17', NULL, NULL, NULL, NULL, 5, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(11, 8, 2, 19, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 16:48:58', NULL, NULL, NULL, NULL, 28, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(12, 8, 2, 18, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:50:30', NULL, NULL, NULL, NULL, 28, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(13, 8, 3, 21, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 16:49:11', NULL, NULL, NULL, NULL, 38, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(14, 8, 3, 18, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:50:41', NULL, NULL, NULL, NULL, 38, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(15, 8, 4, 17, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:50:51', NULL, NULL, NULL, NULL, 48, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(16, 8, 4, 19, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 16:49:28', NULL, NULL, NULL, NULL, 48, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(17, 10, 2, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:49:30', NULL, NULL, NULL, NULL, 27, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(18, 10, 1, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:49:21', NULL, NULL, NULL, NULL, 4, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(19, 10, 3, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:49:40', NULL, NULL, NULL, NULL, 37, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(20, 10, 4, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:49:50', NULL, NULL, NULL, NULL, 47, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(21, 11, 1, 9, '2012-11-02 11:36:28', 5, '2012-10-23 11:36:28', '2012-10-26 10:38:03', 0, NULL, '2010-07-20 09:04:17', 0, NULL, 0, NULL, 16, 1.0001, 2, 0);
INSERT INTO `task_assignment` VALUES(22, 11, 2, 10, '2012-11-02 11:36:28', 1, '2012-10-23 11:36:28', NULL, 0, NULL, '2010-07-20 09:04:26', 0, NULL, 0, NULL, 72, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(23, 11, 3, 11, '2012-11-02 11:36:28', 1, '2012-10-23 11:36:28', NULL, 0, NULL, '2010-07-20 09:04:39', 0, NULL, 0, NULL, 59, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(24, 11, 4, 12, '2012-11-02 11:36:28', 1, '2012-10-23 11:36:28', NULL, 0, NULL, '2010-07-20 09:04:48', 0, NULL, 0, NULL, 85, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(25, 12, 1, 9, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:05:05', NULL, NULL, NULL, NULL, 17, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(26, 12, 2, 10, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:05:17', NULL, NULL, NULL, NULL, 73, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(27, 12, 3, 11, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:05:30', NULL, NULL, NULL, NULL, 60, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(28, 12, 4, 12, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:05:41', NULL, NULL, NULL, NULL, 86, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(29, 15, 2, 10, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:06:05', NULL, NULL, NULL, NULL, 74, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(30, 15, 1, 9, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:05:55', NULL, NULL, NULL, NULL, 18, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(31, 15, 3, 11, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:06:13', NULL, NULL, NULL, NULL, 61, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(32, 15, 4, 12, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:06:21', NULL, NULL, NULL, NULL, 87, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(33, 17, 1, 21, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 18:01:32', NULL, NULL, NULL, NULL, 11, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(34, 17, 1, 17, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:59:42', NULL, NULL, NULL, NULL, 11, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(35, 17, 2, 19, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 18:01:40', NULL, NULL, NULL, NULL, 67, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(36, 17, 2, 18, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:59:53', NULL, NULL, NULL, NULL, 67, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(37, 17, 3, 21, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 18:01:51', NULL, NULL, NULL, NULL, 54, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(38, 17, 3, 18, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:00:02', NULL, NULL, NULL, NULL, 54, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(39, 17, 4, 17, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:00:13', NULL, NULL, NULL, NULL, 80, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(40, 17, 4, 19, NULL, 0, NULL, NULL, NULL, NULL, '2010-06-14 18:01:59', NULL, NULL, NULL, NULL, 80, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(41, 20, 1, 8, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:14:43', NULL, NULL, NULL, NULL, 19, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(42, 20, 2, 8, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:58:07', NULL, NULL, NULL, NULL, 75, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(43, 20, 3, 8, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:58:16', NULL, NULL, NULL, NULL, 62, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(44, 20, 4, 8, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:58:26', NULL, NULL, NULL, NULL, 88, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(45, 40, 1, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:57:14', NULL, NULL, NULL, NULL, 23, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(46, 40, 2, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:57:23', NULL, NULL, NULL, NULL, 79, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(47, 40, 3, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:57:32', NULL, NULL, NULL, NULL, 66, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(48, 40, 4, 5, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:57:44', NULL, NULL, NULL, NULL, 92, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(49, 5, 1, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:45:41', NULL, NULL, NULL, NULL, 1, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(50, 5, 2, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:46:06', NULL, NULL, NULL, NULL, 24, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(51, 5, 3, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:46:15', NULL, NULL, NULL, NULL, 34, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(52, 5, 4, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:46:26', NULL, NULL, NULL, NULL, 44, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(53, 6, 1, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:47:15', NULL, NULL, NULL, NULL, 2, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(54, 6, 2, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:47:29', NULL, NULL, NULL, NULL, 25, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(55, 6, 3, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:47:39', NULL, NULL, NULL, NULL, 35, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(56, 6, 4, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:47:48', NULL, NULL, NULL, NULL, 45, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(57, 7, 1, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:48:35', NULL, NULL, NULL, NULL, 3, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(58, 7, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:48:44', NULL, NULL, NULL, NULL, 26, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(59, 7, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:48:53', NULL, NULL, NULL, NULL, 36, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(60, 7, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:49:03', NULL, NULL, NULL, NULL, 46, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(61, 9, 1, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:52:03', NULL, NULL, NULL, NULL, 6, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(62, 9, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:52:20', NULL, NULL, NULL, NULL, 29, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(63, 9, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:52:33', NULL, NULL, NULL, NULL, 39, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(64, 9, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:52:43', NULL, NULL, NULL, NULL, 49, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(66, 3, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:53:14', NULL, NULL, NULL, NULL, 30, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(67, 3, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:53:24', NULL, NULL, NULL, NULL, 40, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(68, 3, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:53:32', NULL, NULL, NULL, NULL, 50, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(69, 4, 1, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:53:46', NULL, NULL, NULL, NULL, 8, -1, 1, 0);
INSERT INTO `task_assignment` VALUES(70, 4, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:53:54', NULL, NULL, NULL, NULL, 31, -1, 3, 0);
INSERT INTO `task_assignment` VALUES(71, 4, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:54:03', NULL, NULL, NULL, NULL, 41, -1, 4, 0);
INSERT INTO `task_assignment` VALUES(72, 4, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:54:12', NULL, NULL, NULL, NULL, 51, -1, 5, 0);
INSERT INTO `task_assignment` VALUES(73, 23, 1, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:56:15', NULL, NULL, NULL, NULL, 22, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(74, 23, 2, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:56:24', NULL, NULL, NULL, NULL, 78, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(75, 23, 3, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:56:33', NULL, NULL, NULL, NULL, 65, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(76, 23, 4, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:56:49', NULL, NULL, NULL, NULL, 91, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(77, 22, 1, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:03:33', NULL, NULL, NULL, NULL, 21, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(78, 22, 2, 2, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:03:42', NULL, NULL, NULL, NULL, 77, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(79, 22, 3, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:03:52', NULL, NULL, NULL, NULL, 64, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(80, 22, 4, 20, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:04:01', NULL, NULL, NULL, NULL, 90, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(81, 18, 1, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:00:30', NULL, NULL, NULL, NULL, 12, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(82, 18, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:00:44', NULL, NULL, NULL, NULL, 68, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(83, 18, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:00:53', NULL, NULL, NULL, NULL, 55, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(84, 18, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:01:02', NULL, NULL, NULL, NULL, 81, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(85, 13, 1, 3, '2012-10-31 10:38:23', 4, '2012-10-26 10:38:23', NULL, 0, '2012-10-26 10:38:23', '2010-07-20 09:01:14', 0, NULL, 0, NULL, 13, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(86, 13, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:01:24', NULL, NULL, NULL, NULL, 69, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(87, 13, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:01:33', NULL, NULL, NULL, NULL, 56, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(88, 13, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:01:43', NULL, NULL, NULL, NULL, 82, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(89, 14, 1, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:02:01', NULL, NULL, NULL, NULL, 14, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(90, 14, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:02:11', NULL, NULL, NULL, NULL, 70, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(91, 14, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:02:19', NULL, NULL, NULL, NULL, 57, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(92, 14, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:02:28', NULL, NULL, NULL, NULL, 83, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(93, 16, 1, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:02:41', NULL, NULL, NULL, NULL, 15, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(94, 16, 2, 3, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:02:50', NULL, NULL, NULL, NULL, 71, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(95, 16, 3, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:02:59', NULL, NULL, NULL, NULL, 58, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(96, 16, 4, 4, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 09:03:09', NULL, NULL, NULL, NULL, 84, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(97, 21, 1, 7, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:58:42', NULL, NULL, NULL, NULL, 20, -1, 2, 0);
INSERT INTO `task_assignment` VALUES(98, 21, 2, 7, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:58:49', NULL, NULL, NULL, NULL, 76, -1, 7, 0);
INSERT INTO `task_assignment` VALUES(99, 21, 3, 7, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:58:58', NULL, NULL, NULL, NULL, 63, -1, 6, 0);
INSERT INTO `task_assignment` VALUES(100, 21, 4, 7, NULL, 0, NULL, NULL, NULL, NULL, '2010-07-20 08:59:03', NULL, NULL, NULL, NULL, 89, -1, 8, 0);
INSERT INTO `task_assignment` VALUES(101, 3, 1, 0, NULL, 2, NULL, NULL, 5, '2012-10-23 11:37:01', '2012-10-25 12:10:55', 3, '2012-10-25 13:10:07', NULL, NULL, 7, -1, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `task_role`
--

CREATE TABLE `task_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `can_claim` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tr` (`task_id`,`role_id`),
  KEY `fk_task_role_task` (`task_id`),
  KEY `fk_task_role_role` (`role_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=41 ;

--
-- Dumping data for table `task_role`
--

INSERT INTO `task_role` VALUES(1, 1, 6, 1);
INSERT INTO `task_role` VALUES(2, 2, 6, 1);
INSERT INTO `task_role` VALUES(3, 3, 4, 1);
INSERT INTO `task_role` VALUES(4, 4, 4, 1);
INSERT INTO `task_role` VALUES(5, 5, 3, 1);
INSERT INTO `task_role` VALUES(6, 6, 3, 1);
INSERT INTO `task_role` VALUES(7, 7, 4, 1);
INSERT INTO `task_role` VALUES(8, 8, 5, 1);
INSERT INTO `task_role` VALUES(9, 9, 4, 1);
INSERT INTO `task_role` VALUES(10, 10, 2, 1);
INSERT INTO `task_role` VALUES(11, 11, 7, 1);
INSERT INTO `task_role` VALUES(12, 12, 7, 1);
INSERT INTO `task_role` VALUES(13, 13, 4, 1);
INSERT INTO `task_role` VALUES(14, 14, 4, 1);
INSERT INTO `task_role` VALUES(15, 15, 7, 1);
INSERT INTO `task_role` VALUES(16, 16, 4, 1);
INSERT INTO `task_role` VALUES(17, 17, 5, 1);
INSERT INTO `task_role` VALUES(18, 18, 4, 1);
INSERT INTO `task_role` VALUES(19, 20, 8, 1);
INSERT INTO `task_role` VALUES(20, 21, 8, 1);
INSERT INTO `task_role` VALUES(21, 22, 3, 1);
INSERT INTO `task_role` VALUES(22, 23, 3, 1);
INSERT INTO `task_role` VALUES(23, 24, 6, 1);
INSERT INTO `task_role` VALUES(24, 25, 4, 1);
INSERT INTO `task_role` VALUES(25, 26, 3, 1);
INSERT INTO `task_role` VALUES(26, 27, 5, 1);
INSERT INTO `task_role` VALUES(27, 28, 4, 1);
INSERT INTO `task_role` VALUES(28, 29, 2, 1);
INSERT INTO `task_role` VALUES(29, 30, 2, 1);
INSERT INTO `task_role` VALUES(31, 31, 2, 1);
INSERT INTO `task_role` VALUES(32, 32, 7, 1);
INSERT INTO `task_role` VALUES(33, 33, 4, 1);
INSERT INTO `task_role` VALUES(34, 34, 2, 1);
INSERT INTO `task_role` VALUES(35, 35, 3, 1);
INSERT INTO `task_role` VALUES(36, 36, 2, 1);
INSERT INTO `task_role` VALUES(37, 37, 5, 1);
INSERT INTO `task_role` VALUES(38, 38, 4, 1);
INSERT INTO `task_role` VALUES(39, 39, 2, 1);
INSERT INTO `task_role` VALUES(40, 40, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `team`
--

CREATE TABLE `team` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `team_name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_pid_name` (`project_id`,`team_name`),
  KEY `fk_team_project1` (`project_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `team`
--

INSERT INTO `team` VALUES(1, 1, 'Reporters', 'Reporter Team');
INSERT INTO `team` VALUES(2, 1, 'Researchers', 'Team of researchers');
INSERT INTO `team` VALUES(3, 1, 'Staff', 'Internal employees');
INSERT INTO `team` VALUES(4, 1, 'Peer Reviewers', 'Team of peer reviewers');

-- --------------------------------------------------------

--
-- Table structure for table `team_user`
--

CREATE TABLE `team_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `team_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tu` (`user_id`,`team_id`),
  KEY `fk_team_user_user1` (`user_id`),
  KEY `fk_team_user_team1` (`team_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `team_user`
--

INSERT INTO `team_user` VALUES(1, 13, 1);
INSERT INTO `team_user` VALUES(2, 15, 1);
INSERT INTO `team_user` VALUES(3, 14, 1);
INSERT INTO `team_user` VALUES(4, 16, 1);
INSERT INTO `team_user` VALUES(5, 9, 2);
INSERT INTO `team_user` VALUES(6, 11, 2);
INSERT INTO `team_user` VALUES(7, 12, 2);
INSERT INTO `team_user` VALUES(8, 10, 2);
INSERT INTO `team_user` VALUES(9, 2, 3);
INSERT INTO `team_user` VALUES(10, 20, 3);
INSERT INTO `team_user` VALUES(11, 3, 3);
INSERT INTO `team_user` VALUES(12, 4, 3);
INSERT INTO `team_user` VALUES(13, 5, 3);
INSERT INTO `team_user` VALUES(14, 6, 3);
INSERT INTO `team_user` VALUES(15, 7, 3);
INSERT INTO `team_user` VALUES(16, 8, 3);
INSERT INTO `team_user` VALUES(17, 21, 4);
INSERT INTO `team_user` VALUES(18, 17, 4);
INSERT INTO `team_user` VALUES(20, 19, 4);

-- --------------------------------------------------------

--
-- Table structure for table `text_item`
--

CREATE TABLE `text_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text_resource_id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `text` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_rlc` (`text_resource_id`,`language_id`),
  KEY `fk_ti_lang` (`language_id`),
  KEY `fk_ti_resource` (`text_resource_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2191113 ;

--
-- Dumping data for table `text_item`
--

INSERT INTO `text_item` VALUES(10000, 10000, 1, 'Log in failed!');
INSERT INTO `text_item` VALUES(10002, 10001, 1, 'The attached file is missing from the data store. Please contact system administrator!');
INSERT INTO `text_item` VALUES(10004, 10002, 1, 'Internal server error/exception occurs. Please contact your administrator!');
INSERT INTO `text_item` VALUES(10006, 10003, 1, 'Invalid parameter: "{0}" is unrecongnized!');
INSERT INTO `text_item` VALUES(10008, 10004, 1, 'Invalid request: parameter "{0}" is not specified!');
INSERT INTO `text_item` VALUES(10010, 10005, 1, '{0} is not existed: {1}!');
INSERT INTO `text_item` VALUES(10012, 10006, 1, 'Exceeds max file size. Note, the permitted max file size is 10M!');
INSERT INTO `text_item` VALUES(10024, 10012, 1, 'You have completed this assignment. Thank you!');
INSERT INTO `text_item` VALUES(10026, 10013, 1, 'Notebook Saved!');
INSERT INTO `text_item` VALUES(10028, 10014, 1, 'suspended');
INSERT INTO `text_item` VALUES(10030, 10015, 1, 'inactive');
INSERT INTO `text_item` VALUES(10032, 10016, 1, 'active');
INSERT INTO `text_item` VALUES(10034, 10017, 1, 'aware');
INSERT INTO `text_item` VALUES(10038, 10019, 1, 'started');
INSERT INTO `text_item` VALUES(10040, 10020, 1, 'done');
INSERT INTO `text_item` VALUES(10042, 10021, 1, 'STATUS');
INSERT INTO `text_item` VALUES(10044, 10022, 1, 'VIEW');
INSERT INTO `text_item` VALUES(10046, 10023, 1, 'HISTORY');
INSERT INTO `text_item` VALUES(10048, 10024, 1, 'GOAL');
INSERT INTO `text_item` VALUES(10050, 10025, 1, 'ESTIMATE');
INSERT INTO `text_item` VALUES(10052, 10026, 1, '% DONE');
INSERT INTO `text_item` VALUES(10054, 10027, 1, 'NEXT DUE');
INSERT INTO `text_item` VALUES(10056, 10028, 1, 'OPEN CASES');
INSERT INTO `text_item` VALUES(10058, 10029, 1, 'PEOPLE ASSIGNED');
INSERT INTO `text_item` VALUES(10060, 10030, 1, 'This box is currently empty.');
INSERT INTO `text_item` VALUES(10062, 10031, 1, 'not started');
INSERT INTO `text_item` VALUES(10064, 10032, 1, 'completed');
INSERT INTO `text_item` VALUES(10066, 10033, 1, 'in progress');
INSERT INTO `text_item` VALUES(10068, 10034, 1, 'noticed');
INSERT INTO `text_item` VALUES(10070, 10035, 1, 'signed in');
INSERT INTO `text_item` VALUES(10072, 10036, 1, '% complete');
INSERT INTO `text_item` VALUES(10074, 10037, 1, 'Next Step');
INSERT INTO `text_item` VALUES(10076, 10038, 1, 'No assginment for ''{0}''');
INSERT INTO `text_item` VALUES(10078, 10039, 1, 'view content');
INSERT INTO `text_item` VALUES(10080, 10040, 1, 'history chart');
INSERT INTO `text_item` VALUES(10082, 10041, 1, 'Edit');
INSERT INTO `text_item` VALUES(10084, 10042, 1, 'Product');
INSERT INTO `text_item` VALUES(10086, 10043, 1, 'Exclude Selected');
INSERT INTO `text_item` VALUES(10088, 10044, 1, 'Include Selected');
INSERT INTO `text_item` VALUES(10090, 10045, 1, 'Overdue');
INSERT INTO `text_item` VALUES(10092, 10046, 1, 'Not Overdue');
INSERT INTO `text_item` VALUES(10094, 10047, 1, 'All');
INSERT INTO `text_item` VALUES(10096, 10048, 1, 'Add');
INSERT INTO `text_item` VALUES(10098, 10049, 1, 'Answer');
INSERT INTO `text_item` VALUES(10100, 10050, 1, 'CASE');
INSERT INTO `text_item` VALUES(10102, 10051, 1, 'TITLE');
INSERT INTO `text_item` VALUES(10104, 10052, 1, 'PRIORITY');
INSERT INTO `text_item` VALUES(10106, 10053, 1, 'OWNER');
INSERT INTO `text_item` VALUES(10108, 10054, 1, 'ATTACHED CONTENT');
INSERT INTO `text_item` VALUES(10110, 10055, 1, 'NAME');
INSERT INTO `text_item` VALUES(10114, 10057, 1, 'COUNTRY');
INSERT INTO `text_item` VALUES(10116, 10058, 1, 'TEAMS');
INSERT INTO `text_item` VALUES(10118, 10059, 1, 'ASSIGNED CONTENT');
INSERT INTO `text_item` VALUES(10120, 10060, 1, 'Add note successfully!');
INSERT INTO `text_item` VALUES(10122, 10061, 1, 'Failed to add {0} note!');
INSERT INTO `text_item` VALUES(10124, 10062, 1, 'Create new case successfully!');
INSERT INTO `text_item` VALUES(10126, 10063, 1, 'Create new case failed!');
INSERT INTO `text_item` VALUES(10128, 10064, 1, 'SUCCESS');
INSERT INTO `text_item` VALUES(10130, 10065, 1, 'FAIL');
INSERT INTO `text_item` VALUES(10132, 10066, 1, 'Total: {0, number, #}, New: {1, number, #}');
INSERT INTO `text_item` VALUES(10134, 10067, 1, 'Change password successfully!');
INSERT INTO `text_item` VALUES(10136, 10068, 1, 'Change password failed!');
INSERT INTO `text_item` VALUES(10138, 10069, 1, 'Anonymous');
INSERT INTO `text_item` VALUES(10140, 10070, 1, 'Contributor');
INSERT INTO `text_item` VALUES(10142, 10071, 1, 'No Project');
INSERT INTO `text_item` VALUES(10144, 10072, 1, 'Invalid username!');
INSERT INTO `text_item` VALUES(10146, 10073, 1, 'Validity username!');
INSERT INTO `text_item` VALUES(10148, 10074, 1, 'Invalid parameters!');
INSERT INTO `text_item` VALUES(10150, 10075, 1, 'Thank you for your answer!');
INSERT INTO `text_item` VALUES(10152, 10076, 1, 'Only English is supported in this version!');
INSERT INTO `text_item` VALUES(10154, 10077, 1, 'Cannot find survey question!');
INSERT INTO `text_item` VALUES(10156, 10078, 1, 'Cannot get country name!');
INSERT INTO `text_item` VALUES(10158, 10079, 1, 'Update case successfully!');
INSERT INTO `text_item` VALUES(10160, 10080, 1, 'Update case failed!');
INSERT INTO `text_item` VALUES(10162, 10081, 1, 'DEADLINE');
INSERT INTO `text_item` VALUES(10164, 10082, 1, 'Please provide your opinions!');
INSERT INTO `text_item` VALUES(10166, 10083, 1, 'Info');
INSERT INTO `text_item` VALUES(10168, 10084, 1, 'Error');
INSERT INTO `text_item` VALUES(10170, 10085, 1, 'Confirmation');
INSERT INTO `text_item` VALUES(10172, 10086, 1, 'Warn');
INSERT INTO `text_item` VALUES(10174, 10087, 1, 'Failed to submit your review because of internal error!');
INSERT INTO `text_item` VALUES(10176, 10088, 1, 'Saved');
INSERT INTO `text_item` VALUES(10178, 10089, 1, 'Please input source information!');
INSERT INTO `text_item` VALUES(10180, 10090, 1, 'Please choose a source!');
INSERT INTO `text_item` VALUES(10182, 10091, 1, 'Please choose at lease one source!');
INSERT INTO `text_item` VALUES(10184, 10092, 1, 'Please input source description!');
INSERT INTO `text_item` VALUES(10186, 10093, 1, 'Please choose a score option!');
INSERT INTO `text_item` VALUES(10188, 10094, 1, 'Please input your answer!');
INSERT INTO `text_item` VALUES(10190, 10095, 1, 'Please provide your suggested scoring change as well as comments supporting your change!');
INSERT INTO `text_item` VALUES(10192, 10096, 1, 'Please enter your comments!');
INSERT INTO `text_item` VALUES(10194, 10097, 1, 'Server internal error, please concact to your administrator!');
INSERT INTO `text_item` VALUES(10196, 10098, 1, 'You have {0} answers left which have not been reviewed!');
INSERT INTO `text_item` VALUES(10198, 10099, 1, 'Are you sure you want to do this? This action cannot be undone.\r\n');
INSERT INTO `text_item` VALUES(10200, 10100, 1, 'There are pending questions that author has not responded.\nAre you sure you have completed the assignment and want to submit now?');
INSERT INTO `text_item` VALUES(10202, 10101, 1, 'This text is currently <span style=''font-weight: bold; color: orange;''>{0}</span> words long. This exceeds the maximum word count of <span style=''font-weight: bold; color: orange;''>{1}</span>. Your work has not been submitted. Please revise the text and resubmit your work.');
INSERT INTO `text_item` VALUES(10204, 10102, 1, 'This text is currently <span style=''font-weight: bold; color: orange;''>{0}</span> words long.  This is below the minimum word count of <span style=''font-weight: bold; color: orange;''>{1}</span>.  Your work has not been submitted. Please revise the text and resubmit your work.');
INSERT INTO `text_item` VALUES(10208, 10104, 1, 'Please input the topic title!');
INSERT INTO `text_item` VALUES(10210, 10105, 1, 'User Notes');
INSERT INTO `text_item` VALUES(10212, 10106, 1, 'Staff Notes');
INSERT INTO `text_item` VALUES(10214, 10107, 1, 'View');
INSERT INTO `text_item` VALUES(10218, 10109, 1, 'Click to unblock');
INSERT INTO `text_item` VALUES(10220, 10110, 1, 'Source');
INSERT INTO `text_item` VALUES(10222, 10111, 1, 'PasteText');
INSERT INTO `text_item` VALUES(10224, 10112, 1, 'PasteFromWord');
INSERT INTO `text_item` VALUES(10226, 10113, 1, 'Print');
INSERT INTO `text_item` VALUES(10228, 10114, 1, 'SpellChecker');
INSERT INTO `text_item` VALUES(10230, 10115, 1, 'Scayt');
INSERT INTO `text_item` VALUES(10232, 10116, 1, 'Undo');
INSERT INTO `text_item` VALUES(10234, 10117, 1, 'Redo');
INSERT INTO `text_item` VALUES(10236, 10118, 1, 'Find');
INSERT INTO `text_item` VALUES(10238, 10119, 1, 'Replace');
INSERT INTO `text_item` VALUES(10240, 10120, 1, 'SelectAll');
INSERT INTO `text_item` VALUES(10242, 10121, 1, 'RemoveFormat');
INSERT INTO `text_item` VALUES(10244, 10122, 1, 'Bold');
INSERT INTO `text_item` VALUES(10246, 10123, 1, 'Italic');
INSERT INTO `text_item` VALUES(10248, 10124, 1, 'Underline');
INSERT INTO `text_item` VALUES(10250, 10125, 1, 'Strike');
INSERT INTO `text_item` VALUES(10252, 10126, 1, 'NumberedList');
INSERT INTO `text_item` VALUES(10254, 10127, 1, 'BulletedList');
INSERT INTO `text_item` VALUES(10256, 10128, 1, 'Link');
INSERT INTO `text_item` VALUES(10258, 10129, 1, 'Unlink');
INSERT INTO `text_item` VALUES(10260, 10130, 1, 'Anchor');
INSERT INTO `text_item` VALUES(10262, 10131, 1, 'Format');
INSERT INTO `text_item` VALUES(10264, 10132, 1, 'BGColor');
INSERT INTO `text_item` VALUES(10266, 10133, 1, 'Maximize');
INSERT INTO `text_item` VALUES(10268, 10134, 1, 'MyToolbar');
INSERT INTO `text_item` VALUES(10270, 10135, 1, 'Please input your enhanced text!');
INSERT INTO `text_item` VALUES(10272, 10136, 1, 'TIME');
INSERT INTO `text_item` VALUES(10274, 10137, 1, 'TOPIC TITLE');
INSERT INTO `text_item` VALUES(10276, 10138, 1, 'Your enhancement saved successfully!');
INSERT INTO `text_item` VALUES(10278, 10139, 1, 'Success');
INSERT INTO `text_item` VALUES(10280, 10140, 1, 'Private Discussion');
INSERT INTO `text_item` VALUES(10282, 10141, 1, 'Staff / Author Discussion');
INSERT INTO `text_item` VALUES(10284, 10142, 1, 'Your review has been successfully saved!');
INSERT INTO `text_item` VALUES(10286, 10143, 1, 'Discussion');
INSERT INTO `text_item` VALUES(10292, 10146, 1, 'Are you sure to submit your questions now?');
INSERT INTO `text_item` VALUES(10294, 10147, 1, 'Your questions have not been answered yet.\nAre you sure you have completed the assignment and want to submit now?');
INSERT INTO `text_item` VALUES(10296, 10148, 1, 'Sorry');
INSERT INTO `text_item` VALUES(10298, 10149, 1, 'Team Content');
INSERT INTO `text_item` VALUES(10300, 10150, 1, '{0} has already been uploaded');
INSERT INTO `text_item` VALUES(10302, 10151, 1, 'Fail to add attachment file {0}.');
INSERT INTO `text_item` VALUES(10304, 10152, 1, 'Are you sure you don''t want to specify the description for the attached file {0}?');
INSERT INTO `text_item` VALUES(10306, 10153, 1, 'You MUST specify an attached file!');
INSERT INTO `text_item` VALUES(10308, 10154, 1, 'The attachment file {0} will be deleted. Are you sure?');
INSERT INTO `text_item` VALUES(10310, 10155, 1, 'ERROR: fail to delete the attachment!');
INSERT INTO `text_item` VALUES(10312, 10156, 1, 'File upload successfully!.');
INSERT INTO `text_item` VALUES(10314, 10157, 1, 'This file already exists.');
INSERT INTO `text_item` VALUES(10316, 10158, 1, 'Invalid image type! The supported image type are: {0}');
INSERT INTO `text_item` VALUES(10318, 10159, 1, 'You haven''t specify an image!');
INSERT INTO `text_item` VALUES(10320, 10160, 1, 'Current password cannot be empty!');
INSERT INTO `text_item` VALUES(10322, 10161, 1, 'New password cannot be empty!');
INSERT INTO `text_item` VALUES(10324, 10162, 1, 'Confirm password cannot be empty!');
INSERT INTO `text_item` VALUES(10326, 10163, 1, 'New password and confirm password are inconsistent!');
INSERT INTO `text_item` VALUES(10328, 10164, 1, 'Invalid password!');
INSERT INTO `text_item` VALUES(10330, 10165, 1, 'Password changed successfully!');
INSERT INTO `text_item` VALUES(10332, 10166, 1, 'Edit Deadline');
INSERT INTO `text_item` VALUES(10334, 10167, 1, 'New Deadline');
INSERT INTO `text_item` VALUES(10336, 10168, 1, 'End {0}''s assignment "{1}" on "{2}"');
INSERT INTO `text_item` VALUES(10340, 10170, 1, 'ACCEPT: Complete assignment and accept partial submission. ');
INSERT INTO `text_item` VALUES(10342, 10171, 1, 'FORCE COMPLETE: Reject changes but describe assignment as complete');
INSERT INTO `text_item` VALUES(10344, 10172, 1, 'End Assignment');
INSERT INTO `text_item` VALUES(10346, 10173, 1, 'Are you sure?  This cannot be undone.');
INSERT INTO `text_item` VALUES(10348, 10174, 1, 'You have selected:  ');
INSERT INTO `text_item` VALUES(10350, 10175, 1, 'Done');
INSERT INTO `text_item` VALUES(10352, 10176, 1, 'Project');
INSERT INTO `text_item` VALUES(10354, 10177, 1, 'Role');
INSERT INTO `text_item` VALUES(10356, 10178, 1, 'Assigned User');
INSERT INTO `text_item` VALUES(10358, 10179, 1, 'Case Object');
INSERT INTO `text_item` VALUES(10360, 10180, 1, 'Case Body');
INSERT INTO `text_item` VALUES(10362, 10181, 1, 'ACTION');
INSERT INTO `text_item` VALUES(10364, 10182, 1, 'Fire All');
INSERT INTO `text_item` VALUES(10366, 10183, 1, 'User Trigger List');
INSERT INTO `text_item` VALUES(10368, 10184, 1, 'Description is empty!');
INSERT INTO `text_item` VALUES(10370, 10185, 1, 'Project is not selected!');
INSERT INTO `text_item` VALUES(10372, 10186, 1, 'Role is not selected!');
INSERT INTO `text_item` VALUES(10374, 10187, 1, 'Assigned user is not selected!');
INSERT INTO `text_item` VALUES(10376, 10188, 1, 'Case subject is empty!');
INSERT INTO `text_item` VALUES(10378, 10189, 1, 'Case body is empty!');
INSERT INTO `text_item` VALUES(10382, 10191, 1, 'Total:');
INSERT INTO `text_item` VALUES(10384, 10192, 1, 'new:');
INSERT INTO `text_item` VALUES(10386, 10193, 1, 'Too many characters! You can enter max 250 characters.');
INSERT INTO `text_item` VALUES(10388, 10194, 1, 'The selected file {0} hasn''t been added into the attachment list. Do you want to add it now?');
INSERT INTO `text_item` VALUES(10390, 10195, 1, 'Save');
INSERT INTO `text_item` VALUES(10392, 10196, 1, 'Save This Indicator');
INSERT INTO `text_item` VALUES(10394, 10197, 1, 'Comments');
INSERT INTO `text_item` VALUES(10396, 10198, 1, 'Sources');
INSERT INTO `text_item` VALUES(10398, 10199, 1, 'PUBLISH');
INSERT INTO `text_item` VALUES(10400, 10200, 1, 'PUBLISH THIS');
INSERT INTO `text_item` VALUES(10402, 10201, 1, 'Add Comment');
INSERT INTO `text_item` VALUES(10404, 10202, 1, 'adding ...');
INSERT INTO `text_item` VALUES(10406, 10203, 1, 'From');
INSERT INTO `text_item` VALUES(10408, 10204, 1, 'I''m Done - Submit');
INSERT INTO `text_item` VALUES(10410, 10205, 1, 'Only Selected');
INSERT INTO `text_item` VALUES(10412, 10206, 1, 'Submit Indicators');
INSERT INTO `text_item` VALUES(10414, 10207, 1, 'Scorecard Navigation');
INSERT INTO `text_item` VALUES(10416, 10208, 1, 'Submit');
INSERT INTO `text_item` VALUES(10418, 10209, 1, 'Review');
INSERT INTO `text_item` VALUES(10420, 10210, 1, 'Reviews');
INSERT INTO `text_item` VALUES(10422, 10211, 1, 'Next Category');
INSERT INTO `text_item` VALUES(10424, 10212, 1, 'Please Wait...');
INSERT INTO `text_item` VALUES(10426, 10213, 1, 'Your Content');
INSERT INTO `text_item` VALUES(10428, 10214, 1, 'Editing Disabled');
INSERT INTO `text_item` VALUES(10430, 10215, 1, 'Target');
INSERT INTO `text_item` VALUES(10432, 10216, 1, 'Rule Manager');
INSERT INTO `text_item` VALUES(10434, 10217, 1, 'No');
INSERT INTO `text_item` VALUES(10436, 10218, 1, 'Yes');
INSERT INTO `text_item` VALUES(10438, 10219, 1, 'Please enter the subject');
INSERT INTO `text_item` VALUES(10440, 10220, 1, 'TAGS');
INSERT INTO `text_item` VALUES(10442, 10221, 1, 'Add User Trigger');
INSERT INTO `text_item` VALUES(10444, 10222, 1, 'Previous Category');
INSERT INTO `text_item` VALUES(10446, 10223, 1, 'Please specify the receiver');
INSERT INTO `text_item` VALUES(10448, 10224, 1, 'To');
INSERT INTO `text_item` VALUES(10450, 10225, 1, 'Cancel');
INSERT INTO `text_item` VALUES(10452, 10226, 1, 'This indicator has not been answered!');
INSERT INTO `text_item` VALUES(10454, 10227, 1, 'Message');
INSERT INTO `text_item` VALUES(10456, 10228, 1, 'Please enter the message');
INSERT INTO `text_item` VALUES(10458, 10229, 1, 'Next Indicator');
INSERT INTO `text_item` VALUES(10460, 10230, 1, 'Send Message');
INSERT INTO `text_item` VALUES(10462, 10231, 1, 'Project Updates');
INSERT INTO `text_item` VALUES(10464, 10232, 1, 'Apply');
INSERT INTO `text_item` VALUES(10466, 10233, 1, 'Original Answer');
INSERT INTO `text_item` VALUES(10468, 10234, 1, 'Create Time');
INSERT INTO `text_item` VALUES(10470, 10235, 1, 'Block Workflow');
INSERT INTO `text_item` VALUES(10472, 10236, 1, 'Block Publishing');
INSERT INTO `text_item` VALUES(10474, 10237, 1, 'Suggested Answer: (required)');
INSERT INTO `text_item` VALUES(10476, 10238, 1, 'Previous Indicator');
INSERT INTO `text_item` VALUES(10478, 10239, 1, 'Your Inbox');
INSERT INTO `text_item` VALUES(10480, 10240, 1, 'Home');
INSERT INTO `text_item` VALUES(10482, 10241, 1, 'Cases');
INSERT INTO `text_item` VALUES(10484, 10242, 1, 'Fire');
INSERT INTO `text_item` VALUES(10486, 10243, 1, 'Description');
INSERT INTO `text_item` VALUES(10488, 10244, 1, 'File');
INSERT INTO `text_item` VALUES(10490, 10245, 1, 'Reset');
INSERT INTO `text_item` VALUES(10492, 10246, 1, 'Yes, I agree with the score but wish to add a comment, clarification, or suggest another reference.');
INSERT INTO `text_item` VALUES(10494, 10247, 1, 'No, I do not agree with the score.');
INSERT INTO `text_item` VALUES(10496, 10248, 1, 'I am not qualified to judge this indicator.');
INSERT INTO `text_item` VALUES(10498, 10249, 1, 'Subject');
INSERT INTO `text_item` VALUES(10500, 10250, 1, 'The Chart should be loaded here... Errors orrur if you see this text.');
INSERT INTO `text_item` VALUES(10502, 10251, 1, 'I Have Questions');
INSERT INTO `text_item` VALUES(10504, 10252, 1, 'User Name');
INSERT INTO `text_item` VALUES(10506, 10253, 1, 'Preview');
INSERT INTO `text_item` VALUES(10508, 10254, 1, 'Location');
INSERT INTO `text_item` VALUES(10510, 10255, 1, 'Email Message Detail Level');
INSERT INTO `text_item` VALUES(10512, 10256, 1, 'Mailing Address');
INSERT INTO `text_item` VALUES(10514, 10257, 1, 'Review, Intended for Publication');
INSERT INTO `text_item` VALUES(10516, 10258, 1, 'Last Name');
INSERT INTO `text_item` VALUES(10518, 10259, 1, 'NOTE');
INSERT INTO `text_item` VALUES(10520, 10260, 1, 'Bio');
INSERT INTO `text_item` VALUES(10522, 10261, 1, 'First Name');
INSERT INTO `text_item` VALUES(10524, 10262, 1, 'Copy Deep Link');
INSERT INTO `text_item` VALUES(10526, 10263, 1, 'Set {0}''s deadline for {1}');
INSERT INTO `text_item` VALUES(30000, 30000, 1, 'Indaba Builder | All Content');
INSERT INTO `text_item` VALUES(30002, 30001, 1, 'All Content');
INSERT INTO `text_item` VALUES(40000, 40000, 1, 'Indaba Builder | Indicator Answer Detail');
INSERT INTO `text_item` VALUES(50000, 50000, 1, 'Indaba Builder | Assignment Message');
INSERT INTO `text_item` VALUES(60000, 60000, 1, 'Attachments');
INSERT INTO `text_item` VALUES(60002, 60001, 1, 'FILE');
INSERT INTO `text_item` VALUES(60004, 60002, 1, 'SIZE');
INSERT INTO `text_item` VALUES(60006, 60003, 1, 'BY');
INSERT INTO `text_item` VALUES(60008, 60004, 1, 'MB');
INSERT INTO `text_item` VALUES(60010, 60005, 1, 'KB');
INSERT INTO `text_item` VALUES(60012, 60006, 1, 'B');
INSERT INTO `text_item` VALUES(60014, 60007, 1, 'ADD NEW ATTACHMENTS');
INSERT INTO `text_item` VALUES(60016, 60008, 1, 'Max file size');
INSERT INTO `text_item` VALUES(60018, 60009, 1, '10M');
INSERT INTO `text_item` VALUES(70000, 70000, 1, 'Tag');
INSERT INTO `text_item` VALUES(80000, 80000, 1, 'Indaba Builder | New Case');
INSERT INTO `text_item` VALUES(80002, 80001, 1, 'Indaba Builder | Cases Detail');
INSERT INTO `text_item` VALUES(80004, 80002, 1, 'New Case');
INSERT INTO `text_item` VALUES(80006, 80003, 1, 'Case');
INSERT INTO `text_item` VALUES(80008, 80004, 1, 'Created By');
INSERT INTO `text_item` VALUES(80010, 80005, 1, 'Case Title');
INSERT INTO `text_item` VALUES(80012, 80006, 1, 'Case Description');
INSERT INTO `text_item` VALUES(80014, 80007, 1, 'Case Status');
INSERT INTO `text_item` VALUES(80016, 80008, 1, 'Open - New');
INSERT INTO `text_item` VALUES(80018, 80009, 1, 'Assign To');
INSERT INTO `text_item` VALUES(80020, 80010, 1, 'Attach Users');
INSERT INTO `text_item` VALUES(80022, 80011, 1, 'Attach Content');
INSERT INTO `text_item` VALUES(80024, 80012, 1, 'Attach Tags');
INSERT INTO `text_item` VALUES(80026, 80013, 1, 'Attach File(s)');
INSERT INTO `text_item` VALUES(90000, 90000, 1, 'Indaba Builder | Cases');
INSERT INTO `text_item` VALUES(90002, 90001, 1, 'Open New Case');
INSERT INTO `text_item` VALUES(90004, 90002, 1, 'All Cases');
INSERT INTO `text_item` VALUES(100000, 100000, 1, 'Indaba Builder | Content Approval');
INSERT INTO `text_item` VALUES(100002, 100001, 1, 'PLEASE APPROVE THE CONTENT AND CLICK [ I''m Done - Submit ] WHEN YOU ARE DONE');
INSERT INTO `text_item` VALUES(100004, 100002, 1, 'APPROVE THIS CONTENT');
INSERT INTO `text_item` VALUES(110000, 110000, 1, 'Instructions');
INSERT INTO `text_item` VALUES(110002, 110001, 1, 'Current Tasks');
INSERT INTO `text_item` VALUES(110004, 110002, 1, 'NO.');
INSERT INTO `text_item` VALUES(110006, 110003, 1, 'OPENED');
INSERT INTO `text_item` VALUES(110008, 110004, 1, 'LAST UPDATED');
INSERT INTO `text_item` VALUES(120000, 120000, 1, 'Indaba Builder | Content Payment');
INSERT INTO `text_item` VALUES(120002, 120001, 1, 'PLEASE RECORD PAYMENT INFORMATION HERE. WHEN YOU FINISH, PLEASE CLICK [ I''m Done - Submit ] BUTTON');
INSERT INTO `text_item` VALUES(120004, 120002, 1, 'PAYMENT AMOUNT');
INSERT INTO `text_item` VALUES(120006, 120003, 1, 'Please enter correct amount number');
INSERT INTO `text_item` VALUES(120008, 120004, 1, 'PAYEES');
INSERT INTO `text_item` VALUES(120010, 120005, 1, 'PAYMENT DATE');
INSERT INTO `text_item` VALUES(150000, 150000, 1, 'Indaba Build | Error');
INSERT INTO `text_item` VALUES(150002, 150001, 1, 'ERRORS');
INSERT INTO `text_item` VALUES(150004, 150002, 1, 'Internal server error/exception occurs. Please contact your administrator.');
INSERT INTO `text_item` VALUES(150006, 150003, 1, 'Back');
INSERT INTO `text_item` VALUES(160000, 160000, 1, 'Browse Files');
INSERT INTO `text_item` VALUES(160002, 160001, 1, 'Clear List');
INSERT INTO `text_item` VALUES(160004, 160002, 1, 'Start Upload');
INSERT INTO `text_item` VALUES(180000, 180000, 1, 'Indaba Home');
INSERT INTO `text_item` VALUES(180002, 180001, 1, 'All Content');
INSERT INTO `text_item` VALUES(180004, 180002, 1, 'Queue');
INSERT INTO `text_item` VALUES(180006, 180003, 1, 'People');
INSERT INTO `text_item` VALUES(180008, 180004, 1, 'Messaging');
INSERT INTO `text_item` VALUES(180010, 180005, 1, 'Help');
INSERT INTO `text_item` VALUES(180012, 180006, 1, 'Logout');
INSERT INTO `text_item` VALUES(180014, 180007, 1, 'Indaba Admin');
INSERT INTO `text_item` VALUES(190000, 190000, 1, 'Indaba Builder | Help');
INSERT INTO `text_item` VALUES(190002, 190001, 1, 'Are you having problems? We''re very sorry! Here are some ways to get support.');
INSERT INTO `text_item` VALUES(190004, 190002, 1, 'Read the instructions for your project at the <a href="http://getindaba.org/help-desk/">Indaba Help Desk</a>.');
INSERT INTO `text_item` VALUES(190006, 190003, 1, 'Read the Frequently Asked Questions at the <a href="http://getindaba.org/help-desk/">Indaba Help Desk</a>.');
INSERT INTO `text_item` VALUES(190008, 190004, 1, '<a href="newmsg.do">Send a sitemail</a> to your manager. Your managers like hearing from you! You can find your manager at the <a href="people.do">PEOPLE</a> tab, under "People You Should Know." Your manager will address your question and may post the exchange to the FAQ thread.');
INSERT INTO `text_item` VALUES(190010, 190005, 1, 'We use Indaba''s case manager to track problems with fieldwork or using Indaba. If a case is open and related to you, you can see it at the <a href="cases.do">CASES</a> tab. Some users can open a new case, however not all users are allowed to do this.');
INSERT INTO `text_item` VALUES(190012, 190006, 1, 'If you have an Indaba-related emergency, you can call the Global Integrity office at +1 202.449.4100.');
INSERT INTO `text_item` VALUES(190014, 190007, 1, 'Workflow Console');
INSERT INTO `text_item` VALUES(200000, 200000, 1, 'Indaba Builder | Horse Approval');
INSERT INTO `text_item` VALUES(200002, 200001, 1, 'PLEASE APPROVE THE HORSE');
INSERT INTO `text_item` VALUES(200004, 200002, 1, 'Approve - I''m Done');
INSERT INTO `text_item` VALUES(220000, 220000, 1, 'Tag The Indicator');
INSERT INTO `text_item` VALUES(220002, 220001, 1, 'Add New Tag');
INSERT INTO `text_item` VALUES(240000, 240000, 1, 'Indaba Builder | Indicator Peer Review');
INSERT INTO `text_item` VALUES(240002, 240001, 1, 'Link To Original Answer');
INSERT INTO `text_item` VALUES(250000, 250000, 1, 'Indaba Builder | Indicator PR Review');
INSERT INTO `text_item` VALUES(260000, 260000, 1, 'Indaba Builder | Indicator Review');
INSERT INTO `text_item` VALUES(270000, 270000, 1, 'Indaba Builder | Journal Content Version {0} by');
INSERT INTO `text_item` VALUES(270002, 270001, 1, 'Content');
INSERT INTO `text_item` VALUES(280000, 280000, 1, 'Indaba Builder | Notebook Overall Review');
INSERT INTO `text_item` VALUES(290000, 290000, 1, 'Indaba Builder | Notebook Peer Review');
INSERT INTO `text_item` VALUES(300000, 300000, 1, 'Indaba Builder | Notebook PR Review');
INSERT INTO `text_item` VALUES(310000, 310000, 1, 'Indaba Builder | Notebook Review');
INSERT INTO `text_item` VALUES(320000, 320000, 1, 'Indaba Builder | Notebook Review Page');
INSERT INTO `text_item` VALUES(330000, 330000, 1, 'User name can''t be empty!');
INSERT INTO `text_item` VALUES(330002, 330001, 1, 'Password can''t be empty!');
INSERT INTO `text_item` VALUES(330004, 330002, 1, 'Please input user name');
INSERT INTO `text_item` VALUES(330006, 330003, 1, 'Checking the validity of username now.');
INSERT INTO `text_item` VALUES(330008, 330004, 1, 'Sending password to your email...');
INSERT INTO `text_item` VALUES(330010, 330005, 1, 'ERROR when check validity!');
INSERT INTO `text_item` VALUES(330012, 330006, 1, 'ERROR when send email!');
INSERT INTO `text_item` VALUES(330014, 330007, 1, 'Indaba Login');
INSERT INTO `text_item` VALUES(330016, 330008, 1, 'Password');
INSERT INTO `text_item` VALUES(340000, 340000, 1, 'Indaba Builder | Message Detail');
INSERT INTO `text_item` VALUES(340002, 340001, 1, 'Reply');
INSERT INTO `text_item` VALUES(340004, 340002, 1, 'Title');
INSERT INTO `text_item` VALUES(350000, 350000, 1, 'Indaba Builder | Messages');
INSERT INTO `text_item` VALUES(350002, 350001, 1, 'Create New Message');
INSERT INTO `text_item` VALUES(350004, 350002, 1, 'New');
INSERT INTO `text_item` VALUES(350006, 350003, 1, 'Total');
INSERT INTO `text_item` VALUES(350008, 350004, 1, 'FROM');
INSERT INTO `text_item` VALUES(350010, 350005, 1, 'DATE');
INSERT INTO `text_item` VALUES(350012, 350006, 1, 'NEW');
INSERT INTO `text_item` VALUES(350014, 350007, 1, 'Rows');
INSERT INTO `text_item` VALUES(360000, 360000, 1, 'new');
INSERT INTO `text_item` VALUES(360002, 360001, 1, 'from');
INSERT INTO `text_item` VALUES(360004, 360002, 1, 'more');
INSERT INTO `text_item` VALUES(370000, 370000, 1, 'Your Open Cases');
INSERT INTO `text_item` VALUES(380000, 380000, 1, 'Indaba Builder | New Message');
INSERT INTO `text_item` VALUES(380002, 380001, 1, 'New Message');
INSERT INTO `text_item` VALUES(380004, 380002, 1, 'Post to Project Updates');
INSERT INTO `text_item` VALUES(390000, 390000, 1, 'Indaba Builder | Create NoteBook');
INSERT INTO `text_item` VALUES(390002, 390001, 1, 'Notification');
INSERT INTO `text_item` VALUES(390004, 390002, 1, 'You have');
INSERT INTO `text_item` VALUES(390006, 390003, 1, 'task(s) assigned.');
INSERT INTO `text_item` VALUES(390008, 390004, 1, 'enter');
INSERT INTO `text_item` VALUES(400000, 400000, 1, 'Indaba Builder | Notebook');
INSERT INTO `text_item` VALUES(410000, 410000, 1, 'Indaba Builder | Notebook Edit');
INSERT INTO `text_item` VALUES(420000, 420000, 1, 'Previous Versions');
INSERT INTO `text_item` VALUES(420002, 420001, 1, 'Notebook Editor');
INSERT INTO `text_item` VALUES(420004, 420002, 1, 'Words');
INSERT INTO `text_item` VALUES(430000, 430000, 1, 'Edit This');
INSERT INTO `text_item` VALUES(430002, 430001, 1, 'Notebook Content');
INSERT INTO `text_item` VALUES(440000, 440000, 1, 'Peer Review, Intended for Publication');
INSERT INTO `text_item` VALUES(450000, 450000, 1, 'Indaba Builder | People');
INSERT INTO `text_item` VALUES(450002, 450001, 1, 'People You Should Know');
INSERT INTO `text_item` VALUES(450004, 450002, 1, 'Your Teams');
INSERT INTO `text_item` VALUES(450006, 450003, 1, 'All Users');
INSERT INTO `text_item` VALUES(450008, 450004, 1, 'TARGET');
INSERT INTO `text_item` VALUES(460000, 460000, 1, 'Team');
INSERT INTO `text_item` VALUES(460002, 460001, 1, 'Case Association');
INSERT INTO `text_item` VALUES(460004, 460002, 1, 'Assignment Status');
INSERT INTO `text_item` VALUES(470000, 470000, 1, 'Indaba Builder | People Profile');
INSERT INTO `text_item` VALUES(470002, 470001, 1, 'User');
INSERT INTO `text_item` VALUES(470004, 470002, 1, 'Limited Bio');
INSERT INTO `text_item` VALUES(470006, 470003, 1, 'Full Bio');
INSERT INTO `text_item` VALUES(470008, 470004, 1, 'Phone');
INSERT INTO `text_item` VALUES(470010, 470005, 1, 'Mobile Phone');
INSERT INTO `text_item` VALUES(470012, 470006, 1, 'System Information');
INSERT INTO `text_item` VALUES(470014, 470007, 1, 'Email');
INSERT INTO `text_item` VALUES(470016, 470008, 1, 'Forward Inbox Message');
INSERT INTO `text_item` VALUES(470018, 470009, 1, 'Alert Only');
INSERT INTO `text_item` VALUES(470020, 470010, 1, 'Full Message');
INSERT INTO `text_item` VALUES(470022, 470011, 1, 'Language');
INSERT INTO `text_item` VALUES(470024, 470012, 1, 'Project(s)');
INSERT INTO `text_item` VALUES(470026, 470013, 1, 'Role(s)');
INSERT INTO `text_item` VALUES(470028, 470014, 1, 'Last Login Time');
INSERT INTO `text_item` VALUES(470030, 470015, 1, 'Last Logout Time');
INSERT INTO `text_item` VALUES(470032, 470016, 1, 'Inactive');
INSERT INTO `text_item` VALUES(470034, 470017, 1, 'Active');
INSERT INTO `text_item` VALUES(470036, 470018, 1, 'Deleted');
INSERT INTO `text_item` VALUES(470038, 470019, 1, 'Send A Sitemail');
INSERT INTO `text_item` VALUES(470040, 470020, 1, '<b>About Your User Information</b><br/> This bio is visible to other Indaba users. All fields are optional. You must decide how much information you want to share with your colleagues. If you are working anonymously, do not submit any real information here!<br/><br/> Note to authors: The name you enter here will your exact byline for any work published with Indaba. You must use your pen name here if you are using one for your byline.<br/><br/> Indaba displays this bio information in two formats, depending on your role in the project. There is a LIMITED bio, and a FULL bio. Each project''s managers can control who can see which version, or can prevent users from seeing anything at all (a blind review, for example). Indaba administrators will also have access to additional SYSTEM INFORMATION about you, including your email address. <br/><br/> <b>Limited Bio (for the public):</b>');
INSERT INTO `text_item` VALUES(470042, 470021, 1, 'Full Bio (for Indaba users)');
INSERT INTO `text_item` VALUES(470044, 470022, 1, 'System Information (for Indaba administrators)');
INSERT INTO `text_item` VALUES(470046, 470023, 1, 'System Email');
INSERT INTO `text_item` VALUES(470048, 470024, 1, 'The system will also record statistics about how often you use Indaba and share this with system administrators.');
INSERT INTO `text_item` VALUES(470050, 470025, 1, 'Change Password');
INSERT INTO `text_item` VALUES(470052, 470026, 1, 'Current Password');
INSERT INTO `text_item` VALUES(470054, 470027, 1, 'New Password');
INSERT INTO `text_item` VALUES(470056, 470028, 1, 'Confirm New Password');
INSERT INTO `text_item` VALUES(470058, 470029, 1, 'Click to change people icon');
INSERT INTO `text_item` VALUES(470060, 470030, 1, 'people icon');
INSERT INTO `text_item` VALUES(470062, 470031, 1, 'Upload');
INSERT INTO `text_item` VALUES(470064, 470032, 1, 'Assignments');
INSERT INTO `text_item` VALUES(470066, 470033, 1, 'Open Cases');
INSERT INTO `text_item` VALUES(490000, 490000, 1, 'Indaba Builder | Queues');
INSERT INTO `text_item` VALUES(490002, 490001, 1, 'No queues available for you at this time');
INSERT INTO `text_item` VALUES(490004, 490002, 1, 'Average time to assign');
INSERT INTO `text_item` VALUES(490006, 490003, 1, 'days');
INSERT INTO `text_item` VALUES(490008, 490004, 1, 'Average time to completion');
INSERT INTO `text_item` VALUES(490010, 490005, 1, 'CONTENT');
INSERT INTO `text_item` VALUES(490012, 490006, 1, 'IN QUEUE');
INSERT INTO `text_item` VALUES(490014, 490007, 1, 'ASSIGNMENT');
INSERT INTO `text_item` VALUES(490016, 490008, 1, 'SET ASSIGNMENT');
INSERT INTO `text_item` VALUES(490018, 490009, 1, 'SET PRIORITY');
INSERT INTO `text_item` VALUES(490020, 490010, 1, 'CLAIM THIS');
INSERT INTO `text_item` VALUES(490022, 490011, 1, 'Assigned To');
INSERT INTO `text_item` VALUES(490024, 490012, 1, 'Not Assigned');
INSERT INTO `text_item` VALUES(490026, 490013, 1, 'Low');
INSERT INTO `text_item` VALUES(490028, 490014, 1, 'Medium');
INSERT INTO `text_item` VALUES(490030, 490015, 1, 'High');
INSERT INTO `text_item` VALUES(490032, 490016, 1, 'Update Assignment');
INSERT INTO `text_item` VALUES(490034, 490017, 1, 'Return To Queue');
INSERT INTO `text_item` VALUES(490036, 490018, 1, 'I Want This');
INSERT INTO `text_item` VALUES(500000, 500000, 1, 'Indaba Builder | Reply Message');
INSERT INTO `text_item` VALUES(500002, 500001, 1, 'Reply Message');
INSERT INTO `text_item` VALUES(500004, 500002, 1, 'Send to Project Updates');
INSERT INTO `text_item` VALUES(510000, 510000, 1, 'Indaba Builder | Rule Manager');
INSERT INTO `text_item` VALUES(510002, 510001, 1, 'RULE FILES');
INSERT INTO `text_item` VALUES(510004, 510002, 1, 'RULE COMPONENTS');
INSERT INTO `text_item` VALUES(510006, 510003, 1, 'Path');
INSERT INTO `text_item` VALUES(520000, 520000, 1, 'Indaba Builder | Scorecard');
INSERT INTO `text_item` VALUES(530000, 530000, 1, 'INDICATOR TAGS');
INSERT INTO `text_item` VALUES(540000, 540000, 1, '(Read Only View)');
INSERT INTO `text_item` VALUES(540002, 540001, 1, 'Scorecard Indicator Navigator');
INSERT INTO `text_item` VALUES(540004, 540002, 1, 'Survey Review Question List');
INSERT INTO `text_item` VALUES(540006, 540003, 1, 'Submit Questions');
INSERT INTO `text_item` VALUES(540008, 540004, 1, 'Indicator Tags');
INSERT INTO `text_item` VALUES(550000, 550000, 1, 'Survey Review Disagreement List');
INSERT INTO `text_item` VALUES(560000, 560000, 1, 'Identify two or more of the following sources to support your score.');
INSERT INTO `text_item` VALUES(560002, 560001, 1, 'Media Reports (...)');
INSERT INTO `text_item` VALUES(560004, 560002, 1, 'Acadamic, ...');
INSERT INTO `text_item` VALUES(560006, 560003, 1, 'Government studies');
INSERT INTO `text_item` VALUES(560008, 560004, 1, 'International organization studies');
INSERT INTO `text_item` VALUES(560010, 560005, 1, 'Interviews with government officials');
INSERT INTO `text_item` VALUES(560012, 560006, 1, 'Interviews with acadamics');
INSERT INTO `text_item` VALUES(560014, 560007, 1, 'Interviews with civil society');
INSERT INTO `text_item` VALUES(560016, 560008, 1, 'Interviews with journalists');
INSERT INTO `text_item` VALUES(570000, 570000, 1, 'Identify and describe the name(s) and section(s) of the applicable law(s), statute(s), regulation(s), constitutional provision(s). Provide a web link to the source(s) if available.');
INSERT INTO `text_item` VALUES(590000, 590000, 1, 'Indaba Builder | Survey Answer');
INSERT INTO `text_item` VALUES(600000, 600000, 1, 'Indaba Builder | Scorecard Answer View');
INSERT INTO `text_item` VALUES(600002, 600001, 1, 'Answer from');
INSERT INTO `text_item` VALUES(640000, 640000, 1, 'Indaba Builder | Survey Answer Original');
INSERT INTO `text_item` VALUES(640002, 640001, 1, 'Task Name');
INSERT INTO `text_item` VALUES(650000, 650000, 1, 'Your Opinions');
INSERT INTO `text_item` VALUES(650002, 650001, 1, 'Do you agree?');
INSERT INTO `text_item` VALUES(650004, 650002, 1, 'Yes, I agree with the score and have no comments to add.');
INSERT INTO `text_item` VALUES(650006, 650003, 1, 'Please avoid the use of "see previous comment" or "see comment to indicator X". <br/> <b>Reviewer''s Comments:(required)</b><br/> <font color="red"><b>DO NOT</b></font> enter scoring change or private comments to project managers here, just rationale and background. These comments <font color="red"><b>will be published</b></font> by default.');
INSERT INTO `text_item` VALUES(650008, 650004, 1, 'Please provide your suggested scoring change as well as comments supporting your change (supporting comments are required). These comments will be published in our final report. Avoid the use of "see previous comment" or "see comment to indicator X." If you wish to send staff other feedback about this indicator that you do NOT want to be published, please use the separate Comments box below to submit that feedback.');
INSERT INTO `text_item` VALUES(660000, 660000, 1, 'OPINION');
INSERT INTO `text_item` VALUES(660002, 660001, 1, 'YES, I agree with the score and have no comments to add.');
INSERT INTO `text_item` VALUES(660004, 660002, 1, '<p>Please avoid the use of "see previous comment" or "see comment to indicator X".</p> <p> <b>Reviewer''s Comments:(required)</b><br/> <font color="red"><b>DO NOT</b></font> enter scoring change or private comments to project managers here, just rationale and background. These comments <font color="red"><b>will be published</b></font> by default. </p>');
INSERT INTO `text_item` VALUES(660006, 660003, 1, '<p>Please provide your suggested scoring change as well as comments supporting your change. Only YES/NO scores (for "in law" indicators) and 0, 25, 50, 75, or 100 scores (for "in practice" indicators) are allowed You do not need to mention the score change itself in the Comments box (we will see your suggested score change in the Suggested Score Change box); please use the Comments box only to provide commentary and narrative that supports the suggested score change. Avoid the use of "see previous comment" or "see comment to indicator X".</p> <p> <b>Reviewer''s Comments:(required)</b><br/> <font color="red"><b>DO NOT</b></font> enter scoring change or private comments to project managers here, just rationale and background. These comments <font color="red"><b>will be published</b></font> by default. </p>');
INSERT INTO `text_item` VALUES(690000, 690000, 1, 'Add to Question List');
INSERT INTO `text_item` VALUES(690002, 690001, 1, 'Question List');
INSERT INTO `text_item` VALUES(710000, 710000, 1, 'Indaba Builder | Survey Overall Review');
INSERT INTO `text_item` VALUES(730000, 730000, 1, 'Targets');
INSERT INTO `text_item` VALUES(730002, 730001, 1, 'loading answer...');
INSERT INTO `text_item` VALUES(740000, 740000, 1, 'Indaba User Finder Trigger');
INSERT INTO `text_item` VALUES(740002, 740001, 1, 'View User Tirgger List');
INSERT INTO `text_item` VALUES(740004, 740002, 1, 'No User Trigger Defined.');
INSERT INTO `text_item` VALUES(740006, 740003, 1, 'Choose Project ...');
INSERT INTO `text_item` VALUES(740008, 740004, 1, 'Choose Role ...');
INSERT INTO `text_item` VALUES(740010, 740005, 1, 'Choose Assigned User ...');
INSERT INTO `text_item` VALUES(740012, 740006, 1, 'Case Subject');
INSERT INTO `text_item` VALUES(740014, 740007, 1, 'ERROR');
INSERT INTO `text_item` VALUES(760000, 760000, 1, 'Indaba Builder | Workflow Console');
INSERT INTO `text_item` VALUES(760002, 760001, 1, 'Workflows');
INSERT INTO `text_item` VALUES(760004, 760002, 1, 'Run All');
INSERT INTO `text_item` VALUES(760006, 760003, 1, 'ID');
INSERT INTO `text_item` VALUES(760008, 760004, 1, 'DESCRIPTION');
INSERT INTO `text_item` VALUES(760010, 760005, 1, 'CREATED AT');
INSERT INTO `text_item` VALUES(760012, 760006, 1, 'CREATED BY');
INSERT INTO `text_item` VALUES(760014, 760007, 1, 'DURATION');
INSERT INTO `text_item` VALUES(760016, 760008, 1, 'Workflow Objects');
INSERT INTO `text_item` VALUES(760018, 760009, 1, 'WORKFLOW - TARGET');
INSERT INTO `text_item` VALUES(760020, 760010, 1, 'WORKFLOW - OBJECT');
INSERT INTO `text_item` VALUES(760022, 760011, 1, 'START TIME');
INSERT INTO `text_item` VALUES(760024, 760012, 1, 'Workflows / Goals / Tasks');
INSERT INTO `text_item` VALUES(760026, 760013, 1, 'WORKFLOW - TARGET / OBJECT');
INSERT INTO `text_item` VALUES(760028, 760014, 1, 'SEQUENCE');
INSERT INTO `text_item` VALUES(760030, 760015, 1, 'TASK');
INSERT INTO `text_item` VALUES(760032, 760016, 1, 'USER');
INSERT INTO `text_item` VALUES(770000, 770000, 1, 'Indaba Builder | Your Content');
INSERT INTO `text_item` VALUES(770002, 770001, 1, 'Sponsors');
INSERT INTO `text_item` VALUES(770004, 770002, 1, 'Administrator Announcement');
INSERT INTO `text_item` VALUES(770006, 770003, 1, 'Your Assignments');
INSERT INTO `text_item` VALUES(770008, 770004, 1, 'New Deadline (YYYY-MM-DD)');
INSERT INTO `text_item` VALUES(2150000, 2150000, 1, 'Indaba Builder | Widgets');
INSERT INTO `text_item` VALUES(2150002, 2150001, 1, 'client site page with widgets');
INSERT INTO `text_item` VALUES(2160000, 2160000, 1, 'Indaba Builder | Manage Widget');
INSERT INTO `text_item` VALUES(2160002, 2160001, 1, 'Widget Configuration Tool');
INSERT INTO `text_item` VALUES(2160004, 2160002, 1, 'Configuration');
INSERT INTO `text_item` VALUES(2160006, 2160003, 1, 'width');
INSERT INTO `text_item` VALUES(2160008, 2160004, 1, 'height');
INSERT INTO `text_item` VALUES(2160010, 2160005, 1, 'target address');
INSERT INTO `text_item` VALUES(2160012, 2160006, 1, 'parameter');
INSERT INTO `text_item` VALUES(2160014, 2160007, 1, 'mapping to');
INSERT INTO `text_item` VALUES(2160016, 2160008, 1, 'Remove Selected');
INSERT INTO `text_item` VALUES(2160018, 2160009, 1, 'Generate Code');
INSERT INTO `text_item` VALUES(2170000, 2170000, 1, 'Indaba Builder | Widget #1');
INSERT INTO `text_item` VALUES(2170002, 2170001, 1, 'Please choose your language');
INSERT INTO `text_item` VALUES(2180000, 2180000, 1, 'Indaba Builder | Widget #2');
INSERT INTO `text_item` VALUES(2180002, 2180001, 1, 'Enter your name');
INSERT INTO `text_item` VALUES(2180004, 2180002, 1, 'Choose your favorite ice cream flavor');
INSERT INTO `text_item` VALUES(2180006, 2180003, 1, 'Chocolate');
INSERT INTO `text_item` VALUES(2180008, 2180004, 1, 'Strawberry');
INSERT INTO `text_item` VALUES(2180010, 2180005, 1, 'Vanilla');
INSERT INTO `text_item` VALUES(2190000, 2190000, 1, 'Indaba Builder | Widget #3');
INSERT INTO `text_item` VALUES(2190002, 2190001, 1, 'click links below');
INSERT INTO `text_item` VALUES(2190004, 2190002, 1, 'Go to journal 4');
INSERT INTO `text_item` VALUES(10553, 10277, 1, 'Case Priority');
INSERT INTO `text_item` VALUES(10554, 10278, 1, 'Open');
INSERT INTO `text_item` VALUES(10555, 10279, 1, 'Closed');
INSERT INTO `text_item` VALUES(10534, 10267, 1, 'you');
INSERT INTO `text_item` VALUES(10536, 10268, 1, '{0} will {1}');
INSERT INTO `text_item` VALUES(10538, 10269, 1, 'OK');
INSERT INTO `text_item` VALUES(10544, 10272, 1, 'display user list');
INSERT INTO `text_item` VALUES(10546, 10273, 1, 'display role list');
INSERT INTO `text_item` VALUES(10548, 10274, 1, 'display team list');
INSERT INTO `text_item` VALUES(10528, 10264, 1, 'Type in a search term');
INSERT INTO `text_item` VALUES(10530, 10265, 1, 'No results');
INSERT INTO `text_item` VALUES(10532, 10266, 1, 'Searching...');
INSERT INTO `text_item` VALUES(10550, 10275, 1, 'Prompt');
INSERT INTO `text_item` VALUES(10552, 10276, 1, 'Alert');
INSERT INTO `text_item` VALUES(10560, 10280, 1, 'Are you sure you want to do this? This action cannot be undone.');
INSERT INTO `text_item` VALUES(10562, 10281, 1, 'Browse...');
INSERT INTO `text_item` VALUES(2190534, 10282, 1, 'Invalid answer value!');
INSERT INTO `text_item` VALUES(10566, 10283, 1, 'Bad parameter section!');
INSERT INTO `text_item` VALUES(10568, 10284, 1, 'Bad parameter source!');
INSERT INTO `text_item` VALUES(10570, 10285, 1, 'Can not find survey answer!');
INSERT INTO `text_item` VALUES(10572, 10286, 1, 'Can not find survey indicator!');
INSERT INTO `text_item` VALUES(10574, 10287, 1, 'Can not find reference!');
INSERT INTO `text_item` VALUES(2190535, 10000, 2, 'Ouverture de session avortée');
INSERT INTO `text_item` VALUES(2190536, 10001, 2, 'Le fichier joint a disparu de la mémoire de données. Veuillez contacter l''administrateur du système!');
INSERT INTO `text_item` VALUES(2190537, 10002, 2, 'Erreur du serveur interne / Il se produit une exclusion. Veuillez contacter votre administrateur  ');
INSERT INTO `text_item` VALUES(2190538, 10003, 2, 'Paramètre invalide: "{0}" n''est pas reconnu !');
INSERT INTO `text_item` VALUES(2190539, 10004, 2, 'Requête non determinée: "{0}" n''est pas spécifié !');
INSERT INTO `text_item` VALUES(2190540, 10005, 2, '{0}: n''existe pas {1} !');
INSERT INTO `text_item` VALUES(2190541, 10006, 2, 'Fichier au-delà de la limite maximale. Remarque: le volume maximal permis d''un fichier est de 10M');
INSERT INTO `text_item` VALUES(2190542, 10012, 2, 'Vous avez terminé la tâche assignée. Merci.');
INSERT INTO `text_item` VALUES(2190543, 10013, 2, 'Notebook sauvegardé!');
INSERT INTO `text_item` VALUES(2190544, 10014, 2, 'En suspens');
INSERT INTO `text_item` VALUES(2190545, 10015, 2, 'Non activé');
INSERT INTO `text_item` VALUES(2190546, 10016, 2, 'Activé');
INSERT INTO `text_item` VALUES(2190547, 10017, 2, 'je suis au courant ');
INSERT INTO `text_item` VALUES(2190548, 10019, 2, 'Commencé');
INSERT INTO `text_item` VALUES(2190549, 10020, 2, 'Fait');
INSERT INTO `text_item` VALUES(2190550, 10021, 2, 'STATUT');
INSERT INTO `text_item` VALUES(2190551, 10022, 2, 'VISIONNER');
INSERT INTO `text_item` VALUES(2190552, 10023, 2, 'ANTÉCÉDENT');
INSERT INTO `text_item` VALUES(2190553, 10024, 2, 'OBJET');
INSERT INTO `text_item` VALUES(2190554, 10025, 2, 'ÉVALUATION');
INSERT INTO `text_item` VALUES(2190555, 10026, 2, '% RÉALISÉ');
INSERT INTO `text_item` VALUES(2190556, 10027, 2, 'À REMETTRE SOUS PEU');
INSERT INTO `text_item` VALUES(2190557, 10028, 2, 'AFFAIRES OUVERTES');
INSERT INTO `text_item` VALUES(2190558, 10029, 2, 'PERSONNES ASSIGNÉES');
INSERT INTO `text_item` VALUES(2190559, 10030, 2, 'Cette case est actuellement vide');
INSERT INTO `text_item` VALUES(2190560, 10031, 2, 'non commencé');
INSERT INTO `text_item` VALUES(2190561, 10032, 2, 'terminé');
INSERT INTO `text_item` VALUES(2190562, 10033, 2, 'travail en cours');
INSERT INTO `text_item` VALUES(2190563, 10034, 2, 'Pris en compte');
INSERT INTO `text_item` VALUES(2190564, 10035, 2, 'enregistré');
INSERT INTO `text_item` VALUES(2190565, 10036, 2, '% terminé');
INSERT INTO `text_item` VALUES(2190566, 10037, 2, 'Étape suivante');
INSERT INTO `text_item` VALUES(2190567, 10038, 2, '"{0}" n''est pas assigné');
INSERT INTO `text_item` VALUES(2190568, 10039, 2, 'vue du contenu');
INSERT INTO `text_item` VALUES(2190569, 10040, 2, 'tableau historique');
INSERT INTO `text_item` VALUES(2190570, 10041, 2, 'Edition');
INSERT INTO `text_item` VALUES(2190571, 10042, 2, 'Produit');
INSERT INTO `text_item` VALUES(2190572, 10043, 2, 'Exclusion selectionnée');
INSERT INTO `text_item` VALUES(2190573, 10044, 2, 'Inclusion selectionnée');
INSERT INTO `text_item` VALUES(2190574, 10045, 2, 'Exigible');
INSERT INTO `text_item` VALUES(2190575, 10046, 2, 'Non exigible');
INSERT INTO `text_item` VALUES(2190576, 10047, 2, 'Selectionner le tout');
INSERT INTO `text_item` VALUES(2190577, 10048, 2, 'Ajout');
INSERT INTO `text_item` VALUES(2190578, 10049, 2, 'Réponse');
INSERT INTO `text_item` VALUES(2190579, 10050, 2, 'AFFAIRE');
INSERT INTO `text_item` VALUES(2190580, 10051, 2, 'TITRE');
INSERT INTO `text_item` VALUES(2190581, 10052, 2, 'PRIORITÉ');
INSERT INTO `text_item` VALUES(2190582, 10053, 2, 'AUTEUR');
INSERT INTO `text_item` VALUES(2190583, 10054, 2, 'TEXTE EN PIÈCE JOINTE');
INSERT INTO `text_item` VALUES(2190584, 10055, 2, 'NOM');
INSERT INTO `text_item` VALUES(2190585, 10057, 2, 'PAYS');
INSERT INTO `text_item` VALUES(2190586, 10058, 2, 'ÉQUIPES');
INSERT INTO `text_item` VALUES(2190587, 10059, 2, 'TEXTE ASSIGNÉ');
INSERT INTO `text_item` VALUES(2190588, 10060, 2, 'Ajout de note faite avec succès');
INSERT INTO `text_item` VALUES(2190589, 10061, 2, 'Ajout de note {0} non effectué');
INSERT INTO `text_item` VALUES(2190590, 10062, 2, 'Création de cas effectué avec succès');
INSERT INTO `text_item` VALUES(2190591, 10063, 2, 'Ouverture de cas non effectuée');
INSERT INTO `text_item` VALUES(2190592, 10064, 2, 'SUCCÈS');
INSERT INTO `text_item` VALUES(2190593, 10065, 2, 'ÉCHEC');
INSERT INTO `text_item` VALUES(2190594, 10067, 2, 'Changement de code d''accès exécuté avec succés!');
INSERT INTO `text_item` VALUES(2190595, 10068, 2, 'Changement du code d''accès non effectué!');
INSERT INTO `text_item` VALUES(2190596, 10069, 2, 'Anonyme');
INSERT INTO `text_item` VALUES(2190597, 10070, 2, 'Collaborateur');
INSERT INTO `text_item` VALUES(2190598, 10071, 2, 'Pas de projet');
INSERT INTO `text_item` VALUES(2190599, 10072, 2, 'Nom d''utilisateur non valide');
INSERT INTO `text_item` VALUES(2190600, 10073, 2, 'Validité du nom de l''utilisateur ');
INSERT INTO `text_item` VALUES(2190601, 10074, 2, 'Paramètres non valides');
INSERT INTO `text_item` VALUES(2190602, 10075, 2, 'Merci pour votre réponse !');
INSERT INTO `text_item` VALUES(2190603, 10076, 2, 'Cette version n''accepte que l''anglais !');
INSERT INTO `text_item` VALUES(2190604, 10077, 2, 'Impossible de trouver la question du sondage !');
INSERT INTO `text_item` VALUES(2190605, 10078, 2, 'Impossible d''obtenir le nom du pays');
INSERT INTO `text_item` VALUES(2190606, 10079, 2, 'Affaire mise à jour avec succès');
INSERT INTO `text_item` VALUES(2190607, 10080, 2, 'Mise à jour de l''affaire non réalisée');
INSERT INTO `text_item` VALUES(2190608, 10081, 2, 'DÉLAI DE REMISE');
INSERT INTO `text_item` VALUES(2190609, 10082, 2, 'Veuillez donner votre opinion !');
INSERT INTO `text_item` VALUES(2190610, 10083, 2, 'Information');
INSERT INTO `text_item` VALUES(2190611, 10084, 2, 'Erreur  ');
INSERT INTO `text_item` VALUES(2190612, 10085, 2, 'Confirmation');
INSERT INTO `text_item` VALUES(2190613, 10086, 2, 'Avertissement');
INSERT INTO `text_item` VALUES(2190614, 10087, 2, 'Échec de remise de votre article due à une erreur interne !');
INSERT INTO `text_item` VALUES(2190615, 10088, 2, 'sauvegardé');
INSERT INTO `text_item` VALUES(2190616, 10089, 2, 'Veuillez citer la source de votre information ! ');
INSERT INTO `text_item` VALUES(2190617, 10090, 2, 'Veuillez selectionner une source d''information !');
INSERT INTO `text_item` VALUES(2190618, 10091, 2, 'Veuillez choisir au moins une source ! ');
INSERT INTO `text_item` VALUES(2190619, 10092, 2, 'Veuillez citer la description de votre source d''information ');
INSERT INTO `text_item` VALUES(2190620, 10093, 2, 'Veuillez choisir une option de qualification ! ');
INSERT INTO `text_item` VALUES(2190621, 10094, 2, 'Veuillez introduire votre réponse');
INSERT INTO `text_item` VALUES(2190622, 10095, 2, 'Veuillez apporter un changement dans la qualification suggérée et  apportez les raisons qui motivent votre changement ! ');
INSERT INTO `text_item` VALUES(2190623, 10096, 2, 'Veuillez introduire vos commentaires');
INSERT INTO `text_item` VALUES(2190624, 10097, 2, 'Erreur du serveur interne. Veuillez contacter votre administrateur ! ');
INSERT INTO `text_item` VALUES(2190625, 10098, 2, 'Il vous reste {0} réponses qui n''ont pas été révisées');
INSERT INTO `text_item` VALUES(2190626, 10099, 2, 'Êtes-vous sûr d''avoir terminé le travail assigné et voulez-vous le remettre maintenant ? ');
INSERT INTO `text_item` VALUES(2190627, 10100, 2, 'Il existe des questions restées sans réponse de la part de l''auteur. Êtes-vous sûr d''avoir terminé le travail assigné et voulez-vous le remettre maintenant ? ');
INSERT INTO `text_item` VALUES(2190628, 10101, 2, 'Le texte actuel est  <span style=''font-weight: bold; color: orange;''>{0}</span> words long. Ceci excède le comptage maximum de mots <span style=''font-weight: bold; color: orange;''>{1}</span>. Votre travail n''a pas été reçu. Veuillex revoir le texte et remttre à nouveau votre travail. ');
INSERT INTO `text_item` VALUES(2190629, 10102, 2, 'Ce texte actuel est <span style=''font-weight: bold; color: orange;''>{0}</span> words long.  Ceci est inférieur au comptage minimum de  <span style=''font-weight: bold; color: orange;''>{1}</span> words long. Votre travail n''a pas été reçu. Veuillez revoir le texte renvoyer votre travail.  ');
INSERT INTO `text_item` VALUES(2190630, 10104, 2, 'Veuillez introduire le titre du sujet traité !');
INSERT INTO `text_item` VALUES(2190631, 10105, 2, 'Notes de l''utilisateur');
INSERT INTO `text_item` VALUES(2190632, 10106, 2, 'Notes du personnel');
INSERT INTO `text_item` VALUES(2190633, 10107, 2, 'Visionner');
INSERT INTO `text_item` VALUES(2190634, 10109, 2, 'Cliquez pour débloquer');
INSERT INTO `text_item` VALUES(2190635, 10110, 2, 'Source');
INSERT INTO `text_item` VALUES(2190636, 10111, 2, 'Coller le texte');
INSERT INTO `text_item` VALUES(2190637, 10112, 2, 'Coller le texte à partir de Word');
INSERT INTO `text_item` VALUES(2190638, 10113, 2, 'Imprimer');
INSERT INTO `text_item` VALUES(2190639, 10114, 2, 'Vérifier l''orthographe');
INSERT INTO `text_item` VALUES(2190640, 10115, 2, 'Scayt');
INSERT INTO `text_item` VALUES(2190641, 10116, 2, 'Annuler');
INSERT INTO `text_item` VALUES(2190642, 10117, 2, 'Refaire');
INSERT INTO `text_item` VALUES(2190643, 10118, 2, 'Chercher');
INSERT INTO `text_item` VALUES(2190644, 10119, 2, 'Remplacer');
INSERT INTO `text_item` VALUES(2190645, 10120, 2, 'Sélectionner tout le texte');
INSERT INTO `text_item` VALUES(2190646, 10121, 2, 'Éliminer le format');
INSERT INTO `text_item` VALUES(2190647, 10122, 2, 'en caractère gras');
INSERT INTO `text_item` VALUES(2190648, 10123, 2, 'en italique');
INSERT INTO `text_item` VALUES(2190649, 10124, 2, 'Souligner le mot');
INSERT INTO `text_item` VALUES(2190650, 10125, 2, 'Effacer');
INSERT INTO `text_item` VALUES(2190651, 10126, 2, 'Liste numérotée');
INSERT INTO `text_item` VALUES(2190652, 10127, 2, 'Liste avec gros points de mise en évidence');
INSERT INTO `text_item` VALUES(2190653, 10128, 2, 'Relier');
INSERT INTO `text_item` VALUES(2190654, 10129, 2, 'Défaire le lien');
INSERT INTO `text_item` VALUES(2190655, 10130, 2, 'Ancrage');
INSERT INTO `text_item` VALUES(2190656, 10131, 2, 'Format');
INSERT INTO `text_item` VALUES(2190657, 10132, 2, 'Couleur BG');
INSERT INTO `text_item` VALUES(2190658, 10133, 2, 'Maximiser');
INSERT INTO `text_item` VALUES(2190659, 10134, 2, 'Ma barre d''outils');
INSERT INTO `text_item` VALUES(2190660, 10135, 2, 'Veuillez introduire votre texte amélioré');
INSERT INTO `text_item` VALUES(2190661, 10136, 2, 'DURÉE');
INSERT INTO `text_item` VALUES(2190662, 10137, 2, 'TITRE DU SUJET TRAITÉ');
INSERT INTO `text_item` VALUES(2190663, 10138, 2, 'Les améliorations apportées ont été sauvegardées');
INSERT INTO `text_item` VALUES(2190664, 10139, 2, 'Avec succès');
INSERT INTO `text_item` VALUES(2190665, 10140, 2, 'Discussion entamée par le personnel');
INSERT INTO `text_item` VALUES(2190666, 10141, 2, 'Discussion entamée par le personnel / par l''auteur ');
INSERT INTO `text_item` VALUES(2190667, 10142, 2, 'Votre révision a été sauvegardée');
INSERT INTO `text_item` VALUES(2190668, 10143, 2, 'Discussion');
INSERT INTO `text_item` VALUES(2190669, 10146, 2, 'Êtes-vous certain de vouloir envoyer vos questions maintenant ? ');
INSERT INTO `text_item` VALUES(2190670, 10147, 2, 'Vos questions n''ont pas encore reçu de réponse. Êtes-vous  d''avoir terminé le travail assigné et voulez-vous l''envoyer maintenant ? ');
INSERT INTO `text_item` VALUES(2190671, 10148, 2, 'Désolé');
INSERT INTO `text_item` VALUES(2190672, 10149, 2, 'Contenu du texte de l''équipe');
INSERT INTO `text_item` VALUES(2190673, 10150, 2, '{0} a déjà été téléchargé en amont ');
INSERT INTO `text_item` VALUES(2190674, 10151, 2, 'Fichier en pièce jointe non envoyé {0}');
INSERT INTO `text_item` VALUES(2190675, 10152, 2, 'Êtes-vous sûr de ne pas vouloir spécifier la description du ficher envoyé en pièce jointe {0} ?');
INSERT INTO `text_item` VALUES(2190676, 10153, 2, 'Il est obligatoire de spécifier un fichier envoyé en pièce jointe !');
INSERT INTO `text_item` VALUES(2190677, 10154, 2, 'Le fichier en pièce jointe {0} sera supprimé. Voulez-vous continuer ?');
INSERT INTO `text_item` VALUES(2190678, 10155, 2, 'Erreur: Fichier en pièce jointe non supprimé');
INSERT INTO `text_item` VALUES(2190679, 10156, 2, 'Fichier téléchargé avec succès !.');
INSERT INTO `text_item` VALUES(2190680, 10157, 2, 'Ce fichier existe déjà');
INSERT INTO `text_item` VALUES(2190681, 10158, 2, 'Type d''illustration non valide ! Le type d''illustration complémentaire est: {0}');
INSERT INTO `text_item` VALUES(2190682, 10159, 2, 'Vous avez omis de spécifier une illustration');
INSERT INTO `text_item` VALUES(2190683, 10160, 2, 'La case du code d''accès actuel doit être obligatoirement remplie');
INSERT INTO `text_item` VALUES(2190684, 10161, 2, 'La case du nouveau code d''accès doit être obligatoirement remplie');
INSERT INTO `text_item` VALUES(2190685, 10162, 2, 'La case de confirmation du code d''accès doit être obligatoirement remplie !');
INSERT INTO `text_item` VALUES(2190686, 10163, 2, 'Le nouveau code d''accès et sa confirmation ne coïncident pas');
INSERT INTO `text_item` VALUES(2190687, 10164, 2, 'Code d''accès non valide');
INSERT INTO `text_item` VALUES(2190688, 10165, 2, 'Changement du code d''accès réalisé avec succès');
INSERT INTO `text_item` VALUES(2190689, 10166, 2, 'Délai de remise de l''édition');
INSERT INTO `text_item` VALUES(2190690, 10167, 2, 'Nouveau délai');
INSERT INTO `text_item` VALUES(2190691, 10168, 2, 'Terminez l''assignation de {0} "{1}" sur "{2}"');
INSERT INTO `text_item` VALUES(2190692, 10170, 2, 'ACCEPTATION: Terminez l''assignation du travail et acceptez des remises partielles du travail. ');
INSERT INTO `text_item` VALUES(2190693, 10171, 2, 'EXIGENCE DE TERMINAISON FORCÉE : Refuse les changements mais considère le travail assigné comme terminé');
INSERT INTO `text_item` VALUES(2190694, 10172, 2, 'Fin du travail assigné');
INSERT INTO `text_item` VALUES(2190695, 10173, 2, 'Êtes-vous sûr de vouloir exécuter cette action? Cette action est irréversible');
INSERT INTO `text_item` VALUES(2190696, 10174, 2, 'Vous avez sélectionné: ');
INSERT INTO `text_item` VALUES(2190697, 10175, 2, 'Réalisé');
INSERT INTO `text_item` VALUES(2190698, 10176, 2, 'Projet');
INSERT INTO `text_item` VALUES(2190699, 10177, 2, 'Rôle');
INSERT INTO `text_item` VALUES(2190700, 10178, 2, 'Utilisateur assigné');
INSERT INTO `text_item` VALUES(2190701, 10179, 2, 'Objet de l''affaire');
INSERT INTO `text_item` VALUES(2190702, 10180, 2, 'Corps de l''affaire');
INSERT INTO `text_item` VALUES(2190703, 10181, 2, 'Action');
INSERT INTO `text_item` VALUES(2190704, 10182, 2, 'Commande donnant le feu vert à l''assignation d''un projet');
INSERT INTO `text_item` VALUES(2190705, 10183, 2, 'Liste d''activation de l''utilisateur');
INSERT INTO `text_item` VALUES(2190706, 10184, 2, 'La case de description n''a pas été remplie !');
INSERT INTO `text_item` VALUES(2190707, 10185, 2, 'Le projet n''a pas été sélectionné !');
INSERT INTO `text_item` VALUES(2190708, 10186, 2, 'Le rôle n''a pas été selectionné !');
INSERT INTO `text_item` VALUES(2190709, 10187, 2, 'l''utilisateur assigné n''a pas été sélectionné !');
INSERT INTO `text_item` VALUES(2190710, 10188, 2, 'Le sujet de l''affaire n''est pas indiqué');
INSERT INTO `text_item` VALUES(2190711, 10189, 2, 'Le corps de l''affaire n''est pas indiqué');
INSERT INTO `text_item` VALUES(2190712, 10191, 2, 'Total:');
INSERT INTO `text_item` VALUES(2190713, 10192, 2, 'nouveau:');
INSERT INTO `text_item` VALUES(2190714, 10193, 2, 'Trop de caractères. Vous ne pouvez introduire qu''un maximum de 250 caractères.');
INSERT INTO `text_item` VALUES(2190715, 10194, 2, 'Le fichier sélectionné {0} n''a pas été annexé à la liste de pièces jointes. Désirez-vous l''ajouter maintenant?');
INSERT INTO `text_item` VALUES(2190716, 10195, 2, 'Sauvegarder');
INSERT INTO `text_item` VALUES(2190717, 10196, 2, 'Sauvegarder cet indicateur ');
INSERT INTO `text_item` VALUES(2190718, 10197, 2, 'Commentaires');
INSERT INTO `text_item` VALUES(2190719, 10198, 2, 'Sources');
INSERT INTO `text_item` VALUES(2190720, 10199, 2, 'PUBLIER');
INSERT INTO `text_item` VALUES(2190721, 10200, 2, 'PUBLIER CECI');
INSERT INTO `text_item` VALUES(2190722, 10201, 2, 'Ajoutez un commenatire');
INSERT INTO `text_item` VALUES(2190723, 10202, 2, 'en ajoutant …');
INSERT INTO `text_item` VALUES(2190724, 10203, 2, 'À partir de');
INSERT INTO `text_item` VALUES(2190725, 10204, 2, 'J''ai terminé - Je remets le travail');
INSERT INTO `text_item` VALUES(2190726, 10205, 2, 'Uniquement sélectionné');
INSERT INTO `text_item` VALUES(2190727, 10206, 2, 'Remettre des indicateurs');
INSERT INTO `text_item` VALUES(2190728, 10207, 2, 'Outil de révision de sondages faisant partie du système');
INSERT INTO `text_item` VALUES(2190729, 10208, 2, 'Remise de travail');
INSERT INTO `text_item` VALUES(2190730, 10209, 2, 'Réviser');
INSERT INTO `text_item` VALUES(2190731, 10210, 2, 'Révisions');
INSERT INTO `text_item` VALUES(2190732, 10211, 2, 'Catégorie suivante');
INSERT INTO `text_item` VALUES(2190733, 10212, 2, 'Veuillez attendre, spv.');
INSERT INTO `text_item` VALUES(2190734, 10213, 2, 'Votre contenu');
INSERT INTO `text_item` VALUES(2190735, 10214, 2, 'Édition non activée');
INSERT INTO `text_item` VALUES(2190736, 10215, 2, 'Cible');
INSERT INTO `text_item` VALUES(2190737, 10216, 2, 'Chef de règlement');
INSERT INTO `text_item` VALUES(2190738, 10217, 2, 'Non');
INSERT INTO `text_item` VALUES(2190739, 10218, 2, 'Oui');
INSERT INTO `text_item` VALUES(2190740, 10219, 2, 'Veuillez introduire le thème du travail');
INSERT INTO `text_item` VALUES(2190741, 10220, 2, 'ÉTIQUETTES');
INSERT INTO `text_item` VALUES(2190742, 10221, 2, 'Ajouter la zone d''activation de l''utilisateur ');
INSERT INTO `text_item` VALUES(2190743, 10222, 2, 'Catégorie antérieure');
INSERT INTO `text_item` VALUES(2190744, 10223, 2, 'Veuillez indiquer le nom du recepteur');
INSERT INTO `text_item` VALUES(2190745, 10224, 2, 'à l''attention de');
INSERT INTO `text_item` VALUES(2190746, 10225, 2, 'Éliminer ');
INSERT INTO `text_item` VALUES(2190747, 10226, 2, 'L''indicateur n''a pas été indiqué !');
INSERT INTO `text_item` VALUES(2190748, 10227, 2, 'Message');
INSERT INTO `text_item` VALUES(2190749, 10228, 2, 'Veuillez introduire le message');
INSERT INTO `text_item` VALUES(2190750, 10229, 2, 'Indicateur suivant');
INSERT INTO `text_item` VALUES(2190751, 10230, 2, 'Envoyez le message');
INSERT INTO `text_item` VALUES(2190752, 10231, 2, 'Mises à jour du projet');
INSERT INTO `text_item` VALUES(2190753, 10232, 2, 'Appliquer');
INSERT INTO `text_item` VALUES(2190754, 10233, 2, 'Réponse iniciale');
INSERT INTO `text_item` VALUES(2190755, 10234, 2, 'Création d''horaires');
INSERT INTO `text_item` VALUES(2190756, 10235, 2, 'Arrêt du déroulement du travail ');
INSERT INTO `text_item` VALUES(2190757, 10236, 2, 'Arrêt de la publication');
INSERT INTO `text_item` VALUES(2190758, 10237, 2, 'Réponse suggérée ( obligatoire)');
INSERT INTO `text_item` VALUES(2190759, 10238, 2, 'Indicateur antérieur');
INSERT INTO `text_item` VALUES(2190760, 10239, 2, 'Votre corbeille d''arrivée');
INSERT INTO `text_item` VALUES(2190761, 10240, 2, 'Page d''accueil');
INSERT INTO `text_item` VALUES(2190762, 10241, 2, 'Affaires ');
INSERT INTO `text_item` VALUES(2190763, 10242, 2, 'Cibler');
INSERT INTO `text_item` VALUES(2190764, 10243, 2, 'Description');
INSERT INTO `text_item` VALUES(2190765, 10244, 2, 'Fichier');
INSERT INTO `text_item` VALUES(2190766, 10245, 2, 'Réinitialisation');
INSERT INTO `text_item` VALUES(2190767, 10246, 2, 'Je suis d''accord avec la qualification mais désire ajouter un commentaire, faire des éclaircissements ou suggérer une autre référence');
INSERT INTO `text_item` VALUES(2190768, 10247, 2, 'Non, je ne suis pas d''accord avec la qualification ');
INSERT INTO `text_item` VALUES(2190769, 10248, 2, 'Je ne suis pas qualifié pour émettre une opinion sur cet indicateur');
INSERT INTO `text_item` VALUES(2190770, 10249, 2, 'Sujet');
INSERT INTO `text_item` VALUES(2190771, 10250, 2, 'Le graphique devrait être téléchargé ici… Signalement d'' erreur dès l''apparition de ce texte.');
INSERT INTO `text_item` VALUES(2190772, 10251, 2, 'J''ai des questions à poser');
INSERT INTO `text_item` VALUES(2190773, 10252, 2, 'Nom de l''utilisateur');
INSERT INTO `text_item` VALUES(2190774, 10253, 2, 'Prévisualisation ');
INSERT INTO `text_item` VALUES(2190775, 10254, 2, 'Positionnement');
INSERT INTO `text_item` VALUES(2190776, 10255, 2, 'Niveau de détails du courriel');
INSERT INTO `text_item` VALUES(2190777, 10256, 2, 'Adresse de la messagerie électronique');
INSERT INTO `text_item` VALUES(2190778, 10257, 2, 'Révision, destinée à la publication');
INSERT INTO `text_item` VALUES(2190779, 10258, 2, 'Nom de famille');
INSERT INTO `text_item` VALUES(2190780, 10259, 2, 'REMARQUE');
INSERT INTO `text_item` VALUES(2190781, 10260, 2, 'Bio');
INSERT INTO `text_item` VALUES(2190782, 10261, 2, 'Prénom');
INSERT INTO `text_item` VALUES(2190783, 10262, 2, 'Copier la liaison ');
INSERT INTO `text_item` VALUES(2190784, 10263, 2, 'Établir un délai pour {0} relatif à {1}');
INSERT INTO `text_item` VALUES(2190785, 30000, 2, 'Système d''Indaba  | Tout contenu ');
INSERT INTO `text_item` VALUES(2190786, 30001, 2, 'Tous les contenus');
INSERT INTO `text_item` VALUES(2190787, 40000, 2, 'Système d''Indaba  | Indicateur de détail de réponse');
INSERT INTO `text_item` VALUES(2190788, 50000, 2, 'Système d''Indaba  | Assignation de message');
INSERT INTO `text_item` VALUES(2190789, 60000, 2, 'Adjonction de fichier ');
INSERT INTO `text_item` VALUES(2190790, 60001, 2, 'FICHIER');
INSERT INTO `text_item` VALUES(2190791, 60002, 2, 'VOLUME');
INSERT INTO `text_item` VALUES(2190792, 60003, 2, 'PAR');
INSERT INTO `text_item` VALUES(2190793, 60004, 2, 'MB');
INSERT INTO `text_item` VALUES(2190794, 60005, 2, 'KB');
INSERT INTO `text_item` VALUES(2190795, 60006, 2, 'B');
INSERT INTO `text_item` VALUES(2190796, 60007, 2, 'ADJONCTION D'' UN AUTRE FICHIER ');
INSERT INTO `text_item` VALUES(2190797, 60008, 2, 'Volume maximum du fichier');
INSERT INTO `text_item` VALUES(2190798, 60009, 2, '10M');
INSERT INTO `text_item` VALUES(2190799, 70000, 2, 'Étiquette');
INSERT INTO `text_item` VALUES(2190800, 80000, 2, 'Système d''Indaba   | Nouvelle affaire');
INSERT INTO `text_item` VALUES(2190801, 80001, 2, 'Systéme d'' Indaba  | Détail des affaires ');
INSERT INTO `text_item` VALUES(2190802, 80002, 2, 'Nouvelle affaire');
INSERT INTO `text_item` VALUES(2190803, 80003, 2, 'Affaire');
INSERT INTO `text_item` VALUES(2190804, 80004, 2, 'Créé par');
INSERT INTO `text_item` VALUES(2190805, 80005, 2, 'Titre de l''Affaire');
INSERT INTO `text_item` VALUES(2190806, 80006, 2, 'Description de l''Affaire');
INSERT INTO `text_item` VALUES(2190807, 80007, 2, 'Statut de l''Affaire');
INSERT INTO `text_item` VALUES(2190808, 80008, 2, 'Ouverture d''une nouvelle affaire');
INSERT INTO `text_item` VALUES(2190809, 80009, 2, 'Assigné à');
INSERT INTO `text_item` VALUES(2190810, 80010, 2, 'Adjonction des utilisateurs');
INSERT INTO `text_item` VALUES(2190811, 80011, 2, 'Adjonction du contenu');
INSERT INTO `text_item` VALUES(2190812, 80012, 2, 'Adjonction d''étiquettes');
INSERT INTO `text_item` VALUES(2190813, 80013, 2, 'Adjonction de fichier(s)');
INSERT INTO `text_item` VALUES(2190814, 90000, 2, 'Système d'' Indaba  | Affaires');
INSERT INTO `text_item` VALUES(2190815, 90001, 2, 'Ouverture d''une nouvelle affaire');
INSERT INTO `text_item` VALUES(2190816, 90002, 2, 'Toutes les affaires');
INSERT INTO `text_item` VALUES(2190817, 100000, 2, 'Système d'' Indaba  | autorisation du contenu ');
INSERT INTO `text_item` VALUES(2190818, 100001, 2, 'VEUILLEZ AUTORISER LE CONTENU ET CLIQUEZ SUR [ J''ai terminé - Envoyer] lorsque le travail est terminé');
INSERT INTO `text_item` VALUES(2190819, 100002, 2, 'AUTORISATION DE CE CONTENU');
INSERT INTO `text_item` VALUES(2190820, 110000, 2, 'Directives');
INSERT INTO `text_item` VALUES(2190821, 110001, 2, 'Tâches actuelles');
INSERT INTO `text_item` VALUES(2190822, 110002, 2, 'NO.');
INSERT INTO `text_item` VALUES(2190823, 110003, 2, 'OUVERT');
INSERT INTO `text_item` VALUES(2190824, 110004, 2, 'DERNIÈRE MISE À JOUR');
INSERT INTO `text_item` VALUES(2190825, 120000, 2, 'Numération de Indaba  | Paiement du contenu');
INSERT INTO `text_item` VALUES(2190826, 120001, 2, 'VEUILLEZ ENREGISTRER ICI L''INFORMATION DU PAIEMENT. UNE FOIS EFFECTUÉ, CLIQUEZ ICI SUR LA TOUCHE  [ J''ai terminé - Envoyer]');
INSERT INTO `text_item` VALUES(2190827, 120002, 2, 'MONTANT DU PAIEMENT');
INSERT INTO `text_item` VALUES(2190828, 120003, 2, 'Veuillez introduire le chiffre correct du montant ');
INSERT INTO `text_item` VALUES(2190829, 120004, 2, 'BÉNÉFICIAIRES');
INSERT INTO `text_item` VALUES(2190830, 120005, 2, 'DATE DE PAIEMENT ');
INSERT INTO `text_item` VALUES(2190831, 150000, 2, 'Système d'' Indaba  | Erreur');
INSERT INTO `text_item` VALUES(2190832, 150001, 2, 'ERREURS');
INSERT INTO `text_item` VALUES(2190833, 150002, 2, 'Existence d''une erreur du serveur interne. Veuillez contacter votre administrateur.');
INSERT INTO `text_item` VALUES(2190834, 150003, 2, 'Retour');
INSERT INTO `text_item` VALUES(2190835, 160000, 2, 'Explorer les fichiers');
INSERT INTO `text_item` VALUES(2190836, 160001, 2, 'Liste d''effacement');
INSERT INTO `text_item` VALUES(2190837, 160002, 2, 'Commencer à télécharger');
INSERT INTO `text_item` VALUES(2190838, 180000, 2, 'Page d''accueil d''Indaba');
INSERT INTO `text_item` VALUES(2190839, 180001, 2, 'Tous les contenus');
INSERT INTO `text_item` VALUES(2190840, 180002, 2, 'File d''attente');
INSERT INTO `text_item` VALUES(2190841, 180003, 2, 'Personnes ');
INSERT INTO `text_item` VALUES(2190842, 180004, 2, 'Messagerie');
INSERT INTO `text_item` VALUES(2190843, 180005, 2, 'Assistance');
INSERT INTO `text_item` VALUES(2190844, 180006, 2, 'Sortie');
INSERT INTO `text_item` VALUES(2190845, 180007, 2, 'Administration d''Indaba');
INSERT INTO `text_item` VALUES(2190846, 190000, 2, 'Système d''Indaba  | Assistance');
INSERT INTO `text_item` VALUES(2190847, 190001, 2, 'Avez-vous des problèmes ? Nous sommes vraiment navrés ! Voici quelques directives pour obtenir de l''aide.');
INSERT INTO `text_item` VALUES(2190848, 190002, 2, 'Lisez les directives concernant votre projet sur <a href="http://getindaba.org/help-desk/">Indaba Help Desk</a>.');
INSERT INTO `text_item` VALUES(2190849, 190003, 2, 'Lisez les questions frequemment posées sur le site  <a href="http://getindaba.org/help-desk/">Indaba Help Desk</a>.');
INSERT INTO `text_item` VALUES(2190850, 190004, 2, '<a href="newmsg.do">Envoyer un message de site </a> à votre directeur. C''est pour eux un plaisir de recevoir de vos nouvelles !. Contactez-les sur <a href="people.do">PEOPLE</a> tab, sous la rubrique "People You Should Know."  Votre directeur affichera l''échange d''idées sur le regroupement des messages d''un groupe d''intérêt sur le sujet concerné.');
INSERT INTO `text_item` VALUES(2190851, 190005, 2, 'Nous utilisons le système de gestion d''Indaba pour retrouver et suivre les problèmes rencontrés lors des travaux sur les lieux ou lors de l''utilisation d''Indaba. Si une affaire déjà traitée est liée à la votre, vous pouvez consulter le dossier sur <a href="cases.do">AFFAIRES</a> tab. Certains utilisateurs peuvent ouvrir une nouvelle affaire. Toutefois l''autorisation n''est pas accordée à tous. ');
INSERT INTO `text_item` VALUES(2190852, 190006, 2, 'Dans le cas où vous rencontreriez un problème urgent lié à Indaba, vous pouvez appeler le bureau d''Indaba au numéro  +1 202.449.4100.');
INSERT INTO `text_item` VALUES(2190853, 190007, 2, 'Pupitre de flux de travail');
INSERT INTO `text_item` VALUES(2190854, 200000, 2, 'Système d''Indaba  | Approbation des négotiations ');
INSERT INTO `text_item` VALUES(2190855, 200001, 2, 'VEUILLEZ AUTORISER LES NÉGOTIATIONS');
INSERT INTO `text_item` VALUES(2190856, 200002, 2, 'Approbation - j''ai terminé');
INSERT INTO `text_item` VALUES(2190857, 220000, 2, 'Étiqueter l''indicateur');
INSERT INTO `text_item` VALUES(2190858, 220001, 2, 'Ajouter une nouvelle étiquette');
INSERT INTO `text_item` VALUES(2190859, 240000, 2, 'Numération d''Indaba  | Révision des indicateurs semblables ');
INSERT INTO `text_item` VALUES(2190860, 240001, 2, 'Lien à la réponse initiale');
INSERT INTO `text_item` VALUES(2190861, 250000, 2, 'Système d''Indaba  | Révision de l''indicateur PR');
INSERT INTO `text_item` VALUES(2190862, 260000, 2, 'Système d''Indaba  | Révision de l''indicateur ');
INSERT INTO `text_item` VALUES(2190863, 270000, 2, 'Système d''Indaba  | Version {0} du contenu du journal par');
INSERT INTO `text_item` VALUES(2190864, 270001, 2, 'Contenu ');
INSERT INTO `text_item` VALUES(2190865, 280000, 2, 'Système d''Indaba  | Révision générale du bloc-notes');
INSERT INTO `text_item` VALUES(2190866, 290000, 2, 'Système d''Indaba  | Révision du bloc-notes du collègue');
INSERT INTO `text_item` VALUES(2190867, 300000, 2, 'Système d''Indaba  | Révision du bloc-notes PR');
INSERT INTO `text_item` VALUES(2190868, 310000, 2, 'Système d''Indaba  | Révision du bloc-notes');
INSERT INTO `text_item` VALUES(2190869, 320000, 2, 'Système d''Indaba  | Page révisée du bloc-notes');
INSERT INTO `text_item` VALUES(2190870, 330000, 2, 'La case destinée au nom de l''utilisteur doit être obligatoirement remplie');
INSERT INTO `text_item` VALUES(2190871, 330001, 2, 'La case du code d''accès doit être obligatoirement remplie');
INSERT INTO `text_item` VALUES(2190872, 330002, 2, 'Veuillez introduire le nom de l''utilisateur');
INSERT INTO `text_item` VALUES(2190873, 330003, 2, 'Vérification du nom de l''utilisateur en cours');
INSERT INTO `text_item` VALUES(2190874, 330004, 2, 'Envoi de votre code d''accès à votre courrier électronique ');
INSERT INTO `text_item` VALUES(2190875, 330005, 2, 'Vérification de validité avortée');
INSERT INTO `text_item` VALUES(2190876, 330006, 2, 'Envoi du courriel avorté');
INSERT INTO `text_item` VALUES(2190877, 330007, 2, 'Connection à Indaba');
INSERT INTO `text_item` VALUES(2190878, 330008, 2, 'Code d''accès ');
INSERT INTO `text_item` VALUES(2190879, 340000, 2, 'Système d''Indaba  | Détail du message');
INSERT INTO `text_item` VALUES(2190880, 340001, 2, 'Réponse');
INSERT INTO `text_item` VALUES(2190881, 340002, 2, 'Titre');
INSERT INTO `text_item` VALUES(2190882, 350000, 2, 'Système d''Indaba  | Message ');
INSERT INTO `text_item` VALUES(2190883, 350001, 2, 'Création d''un nouveau message');
INSERT INTO `text_item` VALUES(2190884, 350002, 2, 'Nouveau');
INSERT INTO `text_item` VALUES(2190885, 350003, 2, 'Total');
INSERT INTO `text_item` VALUES(2190886, 350004, 2, 'DE LA PART DE');
INSERT INTO `text_item` VALUES(2190887, 350005, 2, 'DATE  ');
INSERT INTO `text_item` VALUES(2190888, 350006, 2, 'NOUVEAU');
INSERT INTO `text_item` VALUES(2190889, 350007, 2, 'RANGÉES');
INSERT INTO `text_item` VALUES(2190890, 360000, 2, 'nouveau');
INSERT INTO `text_item` VALUES(2190891, 360001, 2, 'de la part de');
INSERT INTO `text_item` VALUES(2190892, 360002, 2, 'en plus');
INSERT INTO `text_item` VALUES(2190893, 370000, 2, 'Vos affaires en cours');
INSERT INTO `text_item` VALUES(2190894, 380000, 2, 'Système d''Indaba  | Nouveau message ');
INSERT INTO `text_item` VALUES(2190895, 380001, 2, 'Nouveau message');
INSERT INTO `text_item` VALUES(2190896, 380002, 2, 'Affichage de mise à jour de projet');
INSERT INTO `text_item` VALUES(2190897, 390000, 2, 'Système d''Indaba  | Création de bloc-notes ');
INSERT INTO `text_item` VALUES(2190898, 390001, 2, 'Notification');
INSERT INTO `text_item` VALUES(2190899, 390002, 2, 'Vous avez');
INSERT INTO `text_item` VALUES(2190900, 390003, 2, 'assignation de tâche(s)');
INSERT INTO `text_item` VALUES(2190901, 390004, 2, 'Touche d''entrée');
INSERT INTO `text_item` VALUES(2190902, 400000, 2, 'Système d''Indaba  | Bloc-notes');
INSERT INTO `text_item` VALUES(2190903, 410000, 2, 'Systéme d''Indaba  | Édition du bloc-notes');
INSERT INTO `text_item` VALUES(2190904, 420000, 2, 'Versions antérieures');
INSERT INTO `text_item` VALUES(2190905, 420001, 2, 'Mise en forme du bloc-notes');
INSERT INTO `text_item` VALUES(2190906, 420002, 2, 'Mots');
INSERT INTO `text_item` VALUES(2190907, 430000, 2, 'Mettre en forme ces données');
INSERT INTO `text_item` VALUES(2190908, 430001, 2, 'Coontenu du bloc-notes');
INSERT INTO `text_item` VALUES(2190909, 440000, 2, 'Révision du collégue, destinée à la publication');
INSERT INTO `text_item` VALUES(2190910, 450000, 2, 'Système d''Indaba  | Personnes');
INSERT INTO `text_item` VALUES(2190911, 450001, 2, 'Personnes que vous devriez connaître');
INSERT INTO `text_item` VALUES(2190912, 450002, 2, 'Vos Équipes');
INSERT INTO `text_item` VALUES(2190913, 450003, 2, 'Tous les utilisateurs');
INSERT INTO `text_item` VALUES(2190914, 450004, 2, 'CIBLE');
INSERT INTO `text_item` VALUES(2190915, 460000, 2, 'Équipe');
INSERT INTO `text_item` VALUES(2190916, 460001, 2, 'Association concernant l''affaire');
INSERT INTO `text_item` VALUES(2190917, 460002, 2, 'Statut de l''assignation');
INSERT INTO `text_item` VALUES(2190918, 470000, 2, 'Numération d''Indaba  | Profiles des personnes');
INSERT INTO `text_item` VALUES(2190919, 470001, 2, 'Utilisateur ');
INSERT INTO `text_item` VALUES(2190920, 470002, 2, 'Biographie resumée');
INSERT INTO `text_item` VALUES(2190921, 470003, 2, 'Biographie complète');
INSERT INTO `text_item` VALUES(2190922, 470004, 2, 'Téléphone');
INSERT INTO `text_item` VALUES(2190923, 470005, 2, 'Téléphone portable');
INSERT INTO `text_item` VALUES(2190924, 470006, 2, 'Information sur le système');
INSERT INTO `text_item` VALUES(2190925, 470007, 2, 'Courrier électronique');
INSERT INTO `text_item` VALUES(2190926, 470008, 2, 'Retransmettre message de la corbeille d''arrivée');
INSERT INTO `text_item` VALUES(2190927, 470009, 2, 'uniquement Alerte');
INSERT INTO `text_item` VALUES(2190928, 470010, 2, 'Message complet');
INSERT INTO `text_item` VALUES(2190929, 470011, 2, 'Langue');
INSERT INTO `text_item` VALUES(2190930, 470012, 2, 'Projets');
INSERT INTO `text_item` VALUES(2190931, 470013, 2, 'Rôle(s)');
INSERT INTO `text_item` VALUES(2190932, 470014, 2, 'Heure de la dernière ouverture de la session');
INSERT INTO `text_item` VALUES(2190933, 470015, 2, 'Heure de la dernière clôture de la session ');
INSERT INTO `text_item` VALUES(2190934, 470016, 2, 'Inactif');
INSERT INTO `text_item` VALUES(2190935, 470017, 2, 'Actif');
INSERT INTO `text_item` VALUES(2190936, 470018, 2, 'Éffacé');
INSERT INTO `text_item` VALUES(2190937, 470019, 2, 'Envoyer au site de messagerie A');
INSERT INTO `text_item` VALUES(2190938, 470020, 2, '<b> Concernant vos informations d''utilisateur, </b><br/> Cette biographie peut être consultée par d''autres utilisateurs d''Indaba. Toutes les zones ou champs sont optionnels. La décision de partager vos informations avec vos collègues n''appartient qu''à vous. Si vous travaillez dans l''anonymat, ne transmttez pas d''informations sur ce site <br/><br/> ! Conseil donné aux auteurs: Le nom indiqué par vous portera votre signature pour tous vos travaux publiés à travers Indaba. Faites usage de votre nom de plume our toutes vos publications <br/><br/> si vous en avez créé un. Indaba affiche cette biographie en deux formats selon le rôle que vous jouez au coeur du projet. ');
INSERT INTO `text_item` VALUES(2190939, 470021, 2, 'Biographie complète (pour utilisateurs d''Indaba)');
INSERT INTO `text_item` VALUES(2190940, 470022, 2, 'Informations obtenues du système pour l''usage des administrateurs d''Indaba.');
INSERT INTO `text_item` VALUES(2190941, 470023, 2, 'Courrier électronique du système');
INSERT INTO `text_item` VALUES(2190942, 470024, 2, 'Le système établira également des statistiques sur la fréquence d''usage d''Indaba et les administrateurs du système auront accès à cette information. ');
INSERT INTO `text_item` VALUES(2190943, 470025, 2, 'Changez votre code d''accès');
INSERT INTO `text_item` VALUES(2190944, 470026, 2, 'Code d''accès actuel');
INSERT INTO `text_item` VALUES(2190945, 470027, 2, 'Nouveau code d''accès');
INSERT INTO `text_item` VALUES(2190946, 470028, 2, 'Confirmation du nouveau code d''accès');
INSERT INTO `text_item` VALUES(2190947, 470029, 2, 'Cliquez pour changer l''icône de la personne ');
INSERT INTO `text_item` VALUES(2190948, 470030, 2, 'Icône de la personne');
INSERT INTO `text_item` VALUES(2190949, 470031, 2, 'Téléchargez');
INSERT INTO `text_item` VALUES(2190950, 470032, 2, 'Assignation de tâches');
INSERT INTO `text_item` VALUES(2190951, 470033, 2, 'Ouverture de recherches sur des affaires');
INSERT INTO `text_item` VALUES(2190952, 490001, 2, 'Pour le moment, pas de disponibilité pour vous dans la file d''attente');
INSERT INTO `text_item` VALUES(2190953, 490002, 2, 'Durée moyenne pour obtenir une assignation ');
INSERT INTO `text_item` VALUES(2190954, 490003, 2, 'jours');
INSERT INTO `text_item` VALUES(2190955, 490004, 2, 'Durée moyenne jusqu''à la terminaison ');
INSERT INTO `text_item` VALUES(2190956, 490005, 2, 'CONTENU');
INSERT INTO `text_item` VALUES(2190957, 490006, 2, 'En attente');
INSERT INTO `text_item` VALUES(2190958, 490007, 2, 'ASSIGNATION');
INSERT INTO `text_item` VALUES(2190959, 490008, 2, 'ÉTABLIR UNE ASSIGNATION');
INSERT INTO `text_item` VALUES(2190960, 490009, 2, 'ÉTABLIR UNE PRIORITÉ');
INSERT INTO `text_item` VALUES(2190961, 490010, 2, 'APPROPRIATION DE CE TRAVAIL');
INSERT INTO `text_item` VALUES(2190962, 490011, 2, 'Assigné à');
INSERT INTO `text_item` VALUES(2190963, 490012, 2, 'Non assigné');
INSERT INTO `text_item` VALUES(2190964, 490013, 2, 'Attente de courte durée');
INSERT INTO `text_item` VALUES(2190965, 490014, 2, 'Attente de durée moyenne');
INSERT INTO `text_item` VALUES(2190966, 490015, 2, 'Attente de longue durée');
INSERT INTO `text_item` VALUES(2190967, 490016, 2, 'Mise à jour de l''assignation ');
INSERT INTO `text_item` VALUES(2190968, 490017, 2, 'Retour dans la file d''attente');
INSERT INTO `text_item` VALUES(2190969, 490018, 2, 'Je souhaiterais faire ce travail');
INSERT INTO `text_item` VALUES(2190970, 500000, 2, 'Numération d''Indaba  | Message de réponse');
INSERT INTO `text_item` VALUES(2190971, 500001, 2, 'Message de réponse');
INSERT INTO `text_item` VALUES(2190972, 500002, 2, 'Envoyer aux mises à jour du projet');
INSERT INTO `text_item` VALUES(2190973, 510000, 2, 'Numération d''Indaba  | Directeur de règlementation');
INSERT INTO `text_item` VALUES(2190974, 510001, 2, 'FICHIER CONCERNANT LES RÈGLEMENTS');
INSERT INTO `text_item` VALUES(2190975, 510002, 2, 'ÉLÉMENTS DU RÈGLEMENT');
INSERT INTO `text_item` VALUES(2190976, 510003, 2, 'Voie d''accès');
INSERT INTO `text_item` VALUES(2190977, 520000, 2, 'Numération d''Indaba  | Outil de révision d''un sondage faisant partie du système');
INSERT INTO `text_item` VALUES(2190978, 530000, 2, 'ÉTIQUETTES D''INDICATEUR');
INSERT INTO `text_item` VALUES(2190979, 540000, 2, '(Visualisation à lecture seulement)');
INSERT INTO `text_item` VALUES(2190980, 540001, 2, 'Navigateur pour indicateur d''outil de révision de sondage');
INSERT INTO `text_item` VALUES(2190981, 540002, 2, 'Liste de questions servant à la révision de sondage');
INSERT INTO `text_item` VALUES(2190982, 540003, 2, 'Présentation de questions');
INSERT INTO `text_item` VALUES(2190983, 540004, 2, 'Étiquettes d''indicateur ');
INSERT INTO `text_item` VALUES(2190984, 550000, 2, 'Liste de conflits d''opinion concernant la révision du sondage');
INSERT INTO `text_item` VALUES(2190985, 560000, 2, 'Identifier deux ou plusieurs parmi les sources énoncées ci-dessous pour soutenir votre opinion sur le sondage');
INSERT INTO `text_item` VALUES(2190986, 560001, 2, 'Rapports des media (…)');
INSERT INTO `text_item` VALUES(2190987, 560002, 2, 'Rapport académique, …');
INSERT INTO `text_item` VALUES(2190988, 560003, 2, 'Études réalisées par le gouvernement');
INSERT INTO `text_item` VALUES(2190989, 560004, 2, 'Études réalisées par des organisations internationales');
INSERT INTO `text_item` VALUES(2190990, 560005, 2, 'Entretiens avec des fonctionnaires du gouvernement ');
INSERT INTO `text_item` VALUES(2190991, 560006, 2, 'Entretiens avec des universitaires');
INSERT INTO `text_item` VALUES(2190992, 560007, 2, 'Entretiens auprés de la société civile');
INSERT INTO `text_item` VALUES(2190993, 560008, 2, 'Entretiens avec des journalistes');
INSERT INTO `text_item` VALUES(2190994, 570000, 2, 'Identifiez et décrivez le ou les noms et sections de la loi ( des lois) applicables, des statuts, des règlements et des dispositions inscrites dans la constitution. Fournir un lien de la Web pour avoir accès aux sources d''informations, le cas échéant.');
INSERT INTO `text_item` VALUES(2190995, 600000, 2, 'Numération d''Indaba  | Visualisation des réponses au moyen de l''outil de évision du sondage ');
INSERT INTO `text_item` VALUES(2190996, 600001, 2, 'Réponse de la part de');
INSERT INTO `text_item` VALUES(2190997, 640000, 2, 'Réponse initiale du sondage');
INSERT INTO `text_item` VALUES(2190998, 640001, 2, 'Nom de la tâche');
INSERT INTO `text_item` VALUES(2190999, 650000, 2, 'Votre opinion');
INSERT INTO `text_item` VALUES(2191000, 650001, 2, 'Êtes-vous d''accord ?');
INSERT INTO `text_item` VALUES(2191001, 650002, 2, 'Oui, je suis d''accord avec l''outil d''évaluation et je n''ai pas de commentaires à ajouter');
INSERT INTO `text_item` VALUES(2191002, 650003, 2, 'Veuillez éviter l''usage de l''expression "se reporter au commentaire antérieur" ou " voir commentaire concernant l''indicateur X". </p> <p> <b> Commentaires de la personne qui révise  le travail:(obligatoire)</b><br/><font color="red"><b>NE PAS CHANGER </b></font> introduire le changement de qualification ');
INSERT INTO `text_item` VALUES(2191003, 650004, 2, 'Veuillez indiquer les raisons de votre changement de qualification ainsi que les commentaires qui le justifient ( ces justifications sont obligatoires). Evitez l''usage de l''expression "se reproter au commentaire antérieur" ou " voir commentaire su sujet de l''indicateur X". Si vous envoyez au personnel un autre retour d''information concernant cet indicateur et ne souhaitez PAS qu''il soit publié, utilisez une case de commentaires séparée en dessous prévue pour envoyer ce retour d''information.');
INSERT INTO `text_item` VALUES(2191004, 660000, 2, 'OPINION');
INSERT INTO `text_item` VALUES(2191005, 660001, 2, 'Oui, je suis d''accord avec l''outil d''évaluation et je n''ai pas de commentaires à ajouter');
INSERT INTO `text_item` VALUES(2191006, 660002, 2, 'Veuillez éviter l''usage de l''expression "se reporter au commentaire antérieur" ou " voir commentaire concernant l''indicateur X". </p> <p> <b> Commentaires de la personne qui révise  le travail:(obligatoire)</b><br/><font color="red"><b>NE PAS CHANGER </b></font> introduire le changement de qualification ');
INSERT INTO `text_item` VALUES(2191007, 660003, 2, '<p>Veuillez introduire le changement de votre qualification ainsi que les commentaires qui justifient votre changement . Indiquez seulement qualifications OUI/NON (pour les indicateur du cadre juridique) et  les qualifications 0, 25, 50, 75, ou 100  pour les indicateurs de "pratiques habituelles". Ne mentionnez pas le changement de qualifications que vous avez suggéré dans la Case de Commentaires ( nous visualiserons votre changement de qualifications dans la case de Changement de Qualification). Évitez l''usage de l''expression " se reporter au commentaire antérieur" ou "voir commentaire concernant l''indicateur X". </p> <p> <b>Commentaires de la personne qui révise:(obligatoire)</b><br/> Ces commentaires <font color="red"><b> seront publiés </b></font> par défaut. </p> À ce stade, n''introduisez pas de commentaires personnels destinés au directeur de projets, présentez uniquement les raisons et les antécédents de votre exposé. </p>');
INSERT INTO `text_item` VALUES(2191008, 690000, 2, 'Ajoutez une question à la liste');
INSERT INTO `text_item` VALUES(2191009, 690001, 2, 'Liste de questions ');
INSERT INTO `text_item` VALUES(2191010, 710000, 2, 'Système Indaba  | Révision générale du sondage ');
INSERT INTO `text_item` VALUES(2191011, 730000, 2, 'Cibles');
INSERT INTO `text_item` VALUES(2191012, 730001, 2, 'Téléchargement de la réponse');
INSERT INTO `text_item` VALUES(2191013, 740000, 2, 'Déclencheur du système de recherche des utilisateurs d''Indaba');
INSERT INTO `text_item` VALUES(2191014, 740001, 2, 'Visualiser la liste des utilisateurs du déclencheur');
INSERT INTO `text_item` VALUES(2191015, 740002, 2, 'Aucune indication d'' utilisateur du déclencheur ');
INSERT INTO `text_item` VALUES(2191016, 740003, 2, 'Sélectionner un projet…');
INSERT INTO `text_item` VALUES(2191017, 740004, 2, 'Sélectionner un rôle…');
INSERT INTO `text_item` VALUES(2191018, 740005, 2, 'Sélectionner un utilisateur assigné');
INSERT INTO `text_item` VALUES(2191019, 740006, 2, 'Thème de l''affaire');
INSERT INTO `text_item` VALUES(2191020, 740007, 2, 'ÉRREUR');
INSERT INTO `text_item` VALUES(2191021, 760001, 2, 'Déroulement du travail');
INSERT INTO `text_item` VALUES(2191022, 760002, 2, 'Exécuter tout');
INSERT INTO `text_item` VALUES(2191023, 760003, 2, 'IDENTITÉ');
INSERT INTO `text_item` VALUES(2191024, 760004, 2, 'DESCRIPTION');
INSERT INTO `text_item` VALUES(2191025, 760005, 2, 'CRÉÉ À');
INSERT INTO `text_item` VALUES(2191026, 760006, 2, 'CRÉÉ PAR');
INSERT INTO `text_item` VALUES(2191027, 760007, 2, 'DURÉE');
INSERT INTO `text_item` VALUES(2191028, 760008, 2, 'Objets servant au déroulement du travail');
INSERT INTO `text_item` VALUES(2191029, 760009, 2, 'Cible pour le déroulement du travail ');
INSERT INTO `text_item` VALUES(2191030, 760010, 2, 'DÉROULEMENT DU TRAVAIL - OBJET');
INSERT INTO `text_item` VALUES(2191031, 760011, 2, 'HEURE D''INITIALISATION DU TRAVAIL');
INSERT INTO `text_item` VALUES(2191032, 760012, 2, 'Déoulement du travail / buts à atteindre / tâches');
INSERT INTO `text_item` VALUES(2191033, 760013, 2, 'DÉROULEMENT DU TRAVAIL / CIBLE / OBJET');
INSERT INTO `text_item` VALUES(2191034, 760014, 2, 'SÉQUENCE');
INSERT INTO `text_item` VALUES(2191035, 760015, 2, 'TÂCHE');
INSERT INTO `text_item` VALUES(2191036, 760016, 2, 'UTILISATEUR');
INSERT INTO `text_item` VALUES(2191037, 770000, 2, 'Système Indaba  | Votre contenu');
INSERT INTO `text_item` VALUES(2191038, 770001, 2, 'Sponsors');
INSERT INTO `text_item` VALUES(2191039, 770002, 2, 'Déclaration de l''administrateur');
INSERT INTO `text_item` VALUES(2191040, 770003, 2, 'Vos assignations');
INSERT INTO `text_item` VALUES(2191041, 770004, 2, 'Nouveau délai ( YYYY-MM-DD )');
INSERT INTO `text_item` VALUES(2191042, 2150000, 2, 'Système d''Indaba  | Widgets');
INSERT INTO `text_item` VALUES(2191043, 2150001, 2, 'Page du site du client avec widgets');
INSERT INTO `text_item` VALUES(2191044, 2160000, 2, 'Système d''Indaba | Gestion de widgets');
INSERT INTO `text_item` VALUES(2191045, 2160001, 2, 'Outil de configuration de widget');
INSERT INTO `text_item` VALUES(2191046, 2160002, 2, 'Configuration ');
INSERT INTO `text_item` VALUES(2191047, 2160003, 2, 'largeur');
INSERT INTO `text_item` VALUES(2191048, 2160004, 2, 'hauteur');
INSERT INTO `text_item` VALUES(2191049, 2160005, 2, 'adresse cible');
INSERT INTO `text_item` VALUES(2191050, 2160006, 2, 'paramètre');
INSERT INTO `text_item` VALUES(2191051, 2160007, 2, 'Unité logique vers');
INSERT INTO `text_item` VALUES(2191052, 2160008, 2, 'Éliminer article sélectionné');
INSERT INTO `text_item` VALUES(2191053, 2160009, 2, 'Établir un code');
INSERT INTO `text_item` VALUES(2191054, 2170000, 2, 'Système d''Indaba | widget #1');
INSERT INTO `text_item` VALUES(2191055, 2170001, 2, 'Veuillez choisir votre langue');
INSERT INTO `text_item` VALUES(2191056, 2180000, 2, 'Système d''Indaba | widget #2');
INSERT INTO `text_item` VALUES(2191057, 2180001, 2, 'Introduisez votre nom');
INSERT INTO `text_item` VALUES(2191058, 2180002, 2, 'Sélectionnez votre parfum favori de glace');
INSERT INTO `text_item` VALUES(2191059, 2180003, 2, 'Chocolat');
INSERT INTO `text_item` VALUES(2191060, 2180004, 2, 'Fraise');
INSERT INTO `text_item` VALUES(2191061, 2180005, 2, 'Vanille');
INSERT INTO `text_item` VALUES(2191062, 2190000, 2, 'Système d''Indaba | widget #3');
INSERT INTO `text_item` VALUES(2191063, 2190001, 2, 'Cliquez sur les liens ci-dessous');
INSERT INTO `text_item` VALUES(2191064, 490000, 2, 'Système de numération d''Indaba | File d''attente');
INSERT INTO `text_item` VALUES(2191065, 2190002, 2, 'Ouvrez le journal 4');
INSERT INTO `text_item` VALUES(2191066, 590000, 2, 'Système de numération d''Indaba | Réponse du sondage');
INSERT INTO `text_item` VALUES(2191067, 760000, 2, 'Système de numération d''Indaba | Console de rythme de travail ');
INSERT INTO `text_item` VALUES(2191068, 10277, 2, 'Affaire à traiter en priorité');
INSERT INTO `text_item` VALUES(2191069, 10278, 2, 'Ouvert');
INSERT INTO `text_item` VALUES(2191070, 10279, 2, 'Fermé');
INSERT INTO `text_item` VALUES(2191071, 10264, 2, 'Tapez un mot de recherche');
INSERT INTO `text_item` VALUES(2191072, 10265, 2, 'Recherche infructueuse');
INSERT INTO `text_item` VALUES(2191073, 10266, 2, 'Recherche en cours…');
INSERT INTO `text_item` VALUES(2191074, 10268, 2, '{0} sera {1}');
INSERT INTO `text_item` VALUES(2191075, 10269, 2, 'OK');
INSERT INTO `text_item` VALUES(2191076, 10273, 2, 'Affichage de liste de rôles à jouer');
INSERT INTO `text_item` VALUES(2191077, 10274, 2, 'Affichage de liste des équipes');
INSERT INTO `text_item` VALUES(2191078, 10272, 2, 'Affichage de liste d''utilisateurs');
INSERT INTO `text_item` VALUES(2191079, 10275, 2, 'Aide-mémoire');
INSERT INTO `text_item` VALUES(2191080, 10276, 2, 'Signal d''alarme');
INSERT INTO `text_item` VALUES(2191081, 10280, 2, 'Êtes-vous sûr de présenter les indicateurs?');
INSERT INTO `text_item` VALUES(2191082, 10281, 2, 'Naviguez sur le Web...');
INSERT INTO `text_item` VALUES(2191083, 10282, 2, 'Valeur de réponse indéterminée !');
INSERT INTO `text_item` VALUES(2191084, 10283, 2, 'Section erronée de paramètre !');
INSERT INTO `text_item` VALUES(2191085, 10284, 2, 'Source erronée de paramètre  !');
INSERT INTO `text_item` VALUES(2191086, 10285, 2, 'Impossible de trouver de réponses au sondage !');
INSERT INTO `text_item` VALUES(2191087, 10286, 2, 'Impossible de trouver l''indicateur du sondage !');
INSERT INTO `text_item` VALUES(2191088, 10287, 2, 'Impossible de trouver les références !');
INSERT INTO `text_item` VALUES(2191089, 10267, 2, 'vous');
INSERT INTO `text_item` VALUES(2191090, 2190003, 1, 'Privacy Policy');
INSERT INTO `text_item` VALUES(2191091, 2190003, 2, 'Politique de confidentialité');
INSERT INTO `text_item` VALUES(2191092, 2190004, 1, 'By clicking Save you consent to the collection and processing (including\nthe disclosure and international transfer) of your Personal Information as\ndescribed in the Indaba Privacy Policy (http://getindaba.org/indaba-\nprivacy-policy/).  In particular you consent to the transfer of your\nPersonal Information internationally as described in the Privacy Policy.\n');
INSERT INTO `text_item` VALUES(2191093, 2190004, 2, 'En cliquant sur "Sauvegarder", vous consentez à ce que soient captées et\narchivées vos informations personnelles ( y compris la divulgation de vos\ncoordonnées sur le réseau international), conformément aux conditions\nstipulées dans la politique de confidentialité d''Indaba  (http://getindaba.org/indaba-privacy-policy). \nEn particulier, vous donnez votre consentement à la transmission de vos informations personnelles sur\nle réseau international, tel que l''indique la politique de confidentialité d''Indaba.');
INSERT INTO `text_item` VALUES(2191094, 2190005, 1, 'click to open filter');
INSERT INTO `text_item` VALUES(2191095, 2190006, 1, 'click to close filter');
INSERT INTO `text_item` VALUES(2191096, 2190007, 1, 'Private Discussion');
INSERT INTO `text_item` VALUES(2191097, 2190008, 1, 'Send Questions');
INSERT INTO `text_item` VALUES(2191098, 2190009, 1, 'Complete Assignment');
INSERT INTO `text_item` VALUES(2191099, 2190010, 1, 'Send Feedback');
INSERT INTO `text_item` VALUES(2191100, 2190011, 1, 'ENGAGE STATUS');
INSERT INTO `text_item` VALUES(2191101, 2190012, 1, 'TASK STATUS');
INSERT INTO `text_item` VALUES(2191102, 2190013, 1, 'DONE');
INSERT INTO `text_item` VALUES(2191103, 2190014, 1, 'INFLIGHT');
INSERT INTO `text_item` VALUES(2191104, 2190015, 1, 'INACTIVE');
INSERT INTO `text_item` VALUES(2191105, 2190016, 1, 'OVERDUE');
INSERT INTO `text_item` VALUES(2191106, 2190017, 1, 'FORCED EXIT');
INSERT INTO `text_item` VALUES(2191107, 2190018, 1, 'UNASSIGNED');
INSERT INTO `text_item` VALUES(2191108, 2190019, 1, 'Your Outbox');
INSERT INTO `text_item` VALUES(2191109, 2190020, 1, 'Forward');
INSERT INTO `text_item` VALUES(2191110, 2190021, 1, 'delete');
INSERT INTO `text_item` VALUES(2191111, 2190022, 1, 'undelete');
INSERT INTO `text_item` VALUES(2191112, 2190023, 1, 'Forward Message');

-- --------------------------------------------------------

--
-- Table structure for table `text_resource`
--

CREATE TABLE `text_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_name_UNIQUE` (`resource_name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2190024 ;

--
-- Dumping data for table `text_resource`
--

INSERT INTO `text_resource` VALUES(10000, 'common.err.invalid.user', 'last updated @ 2012-02-15 22:02:16');
INSERT INTO `text_resource` VALUES(10001, 'common.err.attachment.missed', 'last updated @ 2012-02-15 22:02:46');
INSERT INTO `text_resource` VALUES(10002, 'common.err.internal.server', 'last updated @ 2012-02-15 22:03:17');
INSERT INTO `text_resource` VALUES(10003, 'common.err.invalid.parameter.unrecongnized', 'last updated @ 2012-02-15 22:03:43');
INSERT INTO `text_resource` VALUES(10004, 'common.err.invalid.parameter', 'last updated @ 2012-02-15 22:05:57');
INSERT INTO `text_resource` VALUES(10005, 'common.err.data.notexisted', 'last updated @ 2012-02-15 22:06:20');
INSERT INTO `text_resource` VALUES(10006, 'common.err.exceed.max.filesize', 'last updated @ 2012-02-15 22:06:44');
INSERT INTO `text_resource` VALUES(10012, 'common.alert.task.done', 'last updated @ 2012-02-15 22:09:14');
INSERT INTO `text_resource` VALUES(10013, 'common.alert.task.save', 'last updated @ 2012-02-15 22:09:42');
INSERT INTO `text_resource` VALUES(10014, 'common.remark.suspended', 'last updated @ 2012-02-15 23:07:10');
INSERT INTO `text_resource` VALUES(10015, 'common.remark.inactive', 'last updated @ 2012-02-15 23:07:30');
INSERT INTO `text_resource` VALUES(10016, 'common.remark.active', 'last updated @ 2012-02-15 23:08:28');
INSERT INTO `text_resource` VALUES(10017, 'common.remark.aware', 'last updated @ 2012-02-15 23:08:39');
INSERT INTO `text_resource` VALUES(10019, 'common.remark.started', 'last updated @ 2012-02-15 23:09:00');
INSERT INTO `text_resource` VALUES(10020, 'common.remark.done', 'last updated @ 2012-02-15 23:09:07');
INSERT INTO `text_resource` VALUES(10021, 'common.label.status', 'last updated @ 2012-02-15 23:09:33');
INSERT INTO `text_resource` VALUES(10022, 'common.label.view', 'last updated @ 2012-02-15 23:09:49');
INSERT INTO `text_resource` VALUES(10023, 'common.label.history', 'last updated @ 2012-02-15 23:10:11');
INSERT INTO `text_resource` VALUES(10024, 'common.label.goal', 'last updated @ 2012-02-15 23:10:20');
INSERT INTO `text_resource` VALUES(10025, 'common.label.estimate', 'last updated @ 2012-02-15 23:10:26');
INSERT INTO `text_resource` VALUES(10026, 'common.label.donepercent', 'last updated @ 2012-02-15 23:14:48');
INSERT INTO `text_resource` VALUES(10027, 'common.label.nextdue', 'last updated @ 2012-02-15 23:15:10');
INSERT INTO `text_resource` VALUES(10028, 'common.label.opencases', 'last updated @ 2012-02-15 23:15:22');
INSERT INTO `text_resource` VALUES(10029, 'common.label.peopleassigned', 'last updated @ 2012-02-15 23:15:48');
INSERT INTO `text_resource` VALUES(10030, 'common.msg.nodata', 'last updated @ 2012-02-15 23:25:06');
INSERT INTO `text_resource` VALUES(10031, 'common.alt.notstarted', 'last updated @ 2012-02-15 23:37:59');
INSERT INTO `text_resource` VALUES(10032, 'common.alt.completed', 'last updated @ 2012-02-15 23:38:25');
INSERT INTO `text_resource` VALUES(10033, 'common.alt.inprogress', 'last updated @ 2012-02-15 23:38:40');
INSERT INTO `text_resource` VALUES(10034, 'common.alt.noticed', 'last updated @ 2012-02-15 23:38:56');
INSERT INTO `text_resource` VALUES(10035, 'common.alt.signedin', 'last updated @ 2012-02-15 23:39:25');
INSERT INTO `text_resource` VALUES(10036, 'common.label.completepercentage', 'last updated @ 2012-02-15 23:40:10');
INSERT INTO `text_resource` VALUES(10037, 'common.msg.nextstep', 'last updated @ 2012-02-15 23:49:45');
INSERT INTO `text_resource` VALUES(10038, 'common.msg.noassign', 'last updated @ 2012-02-15 23:50:12');
INSERT INTO `text_resource` VALUES(10039, 'common.alt.viewcontent', 'last updated @ 2012-02-15 23:50:46');
INSERT INTO `text_resource` VALUES(10040, 'common.alt.historychart', 'last updated @ 2012-02-15 23:51:41');
INSERT INTO `text_resource` VALUES(10041, 'common.btn.edit', 'last updated @ 2012-02-15 23:53:14');
INSERT INTO `text_resource` VALUES(10042, 'common.msg.product', 'last updated @ 2012-02-15 23:56:46');
INSERT INTO `text_resource` VALUES(10043, 'common.choice.exclude', 'last updated @ 2012-02-15 23:57:51');
INSERT INTO `text_resource` VALUES(10044, 'common.choice.include', 'last updated @ 2012-02-15 23:58:26');
INSERT INTO `text_resource` VALUES(10045, 'common.choice.overdue', 'last updated @ 2012-02-15 23:58:57');
INSERT INTO `text_resource` VALUES(10046, 'common.choice.notoverdue', 'last updated @ 2012-02-15 23:59:07');
INSERT INTO `text_resource` VALUES(10047, 'common.choice.all', 'last updated @ 2012-02-15 23:59:40');
INSERT INTO `text_resource` VALUES(10048, 'common.btn.add', 'last updated @ 2012-02-16 00:00:06');
INSERT INTO `text_resource` VALUES(10049, 'common.alt.answer', 'last updated @ 2012-02-16 00:08:53');
INSERT INTO `text_resource` VALUES(10050, 'common.label.case', 'last updated @ 2012-02-16 00:09:06');
INSERT INTO `text_resource` VALUES(10051, 'common.label.title', 'last updated @ 2012-02-16 00:09:17');
INSERT INTO `text_resource` VALUES(10052, 'common.label.priority', 'last updated @ 2012-02-16 00:09:29');
INSERT INTO `text_resource` VALUES(10053, 'common.label.owner', 'last updated @ 2012-02-16 00:09:51');
INSERT INTO `text_resource` VALUES(10054, 'common.label.attachedcontent', 'last updated @ 2012-02-16 00:10:43');
INSERT INTO `text_resource` VALUES(10055, 'common.label.name', 'last updated @ 2012-02-16 00:12:51');
INSERT INTO `text_resource` VALUES(10177, 'common.label.role', 'last updated @ 2012-02-20 15:41:18');
INSERT INTO `text_resource` VALUES(10057, 'common.label.country', 'last updated @ 2012-02-16 00:14:10');
INSERT INTO `text_resource` VALUES(10058, 'common.label.teams', 'last updated @ 2012-02-16 00:14:43');
INSERT INTO `text_resource` VALUES(10059, 'common.label.assignedcontent', 'last updated @ 2012-02-16 00:14:56');
INSERT INTO `text_resource` VALUES(10060, 'common.alert.addcasenotes.success', 'last updated @ 2012-02-16 09:44:03');
INSERT INTO `text_resource` VALUES(10061, 'common.alert.addcasenotes.fail', 'last updated @ 2012-02-16 09:44:23');
INSERT INTO `text_resource` VALUES(10062, 'common.alert.createcase.success', 'last updated @ 2012-02-16 09:47:26');
INSERT INTO `text_resource` VALUES(10063, 'common.alert.createcase.fail', 'last updated @ 2012-02-16 09:47:47');
INSERT INTO `text_resource` VALUES(10064, 'common.alert.fireuserfinder.success', 'last updated @ 2012-02-16 09:52:53');
INSERT INTO `text_resource` VALUES(10065, 'common.alert.fireuserfinder.fail', 'last updated @ 2012-02-16 09:52:57');
INSERT INTO `text_resource` VALUES(10066, 'common.msg.fireuserfinder.summary', 'last updated @ 2012-02-16 09:53:16');
INSERT INTO `text_resource` VALUES(10067, 'common.alert.chngpasswd.success', 'last updated @ 2012-02-16 09:55:29');
INSERT INTO `text_resource` VALUES(10068, 'common.alert.chngpasswd.fail', 'last updated @ 2012-02-16 09:55:54');
INSERT INTO `text_resource` VALUES(10069, 'common.msg.fname.anonymous', 'last updated @ 2012-02-16 09:59:11');
INSERT INTO `text_resource` VALUES(10070, 'common.msg.lname.contributor', 'last updated @ 2012-02-16 09:59:32');
INSERT INTO `text_resource` VALUES(10071, 'common.msg.noproject', 'last updated @ 2012-02-16 10:00:56');
INSERT INTO `text_resource` VALUES(10072, 'common.err.invaliduser', 'last updated @ 2012-02-16 10:06:34');
INSERT INTO `text_resource` VALUES(10073, 'common.label.validityuser', 'last updated @ 2012-02-16 10:07:08');
INSERT INTO `text_resource` VALUES(10074, 'common.err.badparam', 'last updated @ 2012-02-16 10:09:25');
INSERT INTO `text_resource` VALUES(10075, 'common.alert.surveyanswer.success', 'last updated @ 2012-02-16 10:12:48');
INSERT INTO `text_resource` VALUES(10076, 'common.alert.surveyanswer.fail', 'last updated @ 2012-02-16 10:13:15');
INSERT INTO `text_resource` VALUES(10077, 'common.err.notfindquestion', 'last updated @ 2012-02-16 10:19:17');
INSERT INTO `text_resource` VALUES(10078, 'common.err.notfindcountry', 'last updated @ 2012-02-16 10:19:37');
INSERT INTO `text_resource` VALUES(10079, 'common.alert.upcatecase.success', 'last updated @ 2012-02-16 10:21:29');
INSERT INTO `text_resource` VALUES(10080, 'common.alert.upcatecase.fail', 'last updated @ 2012-02-16 10:21:43');
INSERT INTO `text_resource` VALUES(10081, 'common.label.deadline', 'last updated @ 2012-02-16 11:16:10');
INSERT INTO `text_resource` VALUES(10082, 'common.js.alert.askopinions', 'last updated @ 2012-02-16 15:41:06');
INSERT INTO `text_resource` VALUES(10083, 'common.js.alert.title.info', 'last updated @ 2012-02-16 15:41:54');
INSERT INTO `text_resource` VALUES(10084, 'common.js.alert.title.error', 'last updated @ 2012-02-16 15:42:29');
INSERT INTO `text_resource` VALUES(10085, 'common.js.alert.title.confirm', 'last updated @ 2012-02-16 15:42:49');
INSERT INTO `text_resource` VALUES(10086, 'common.js.alert.title.warn', 'last updated @ 2012-02-16 15:42:59');
INSERT INTO `text_resource` VALUES(10087, 'common.js.err.submitreview', 'last updated @ 2012-02-16 15:43:42');
INSERT INTO `text_resource` VALUES(10088, 'common.btn.saved', 'last updated @ 2012-02-16 15:45:21');
INSERT INTO `text_resource` VALUES(10089, 'common.js.alert.askinputsource', 'last updated @ 2012-02-16 15:47:18');
INSERT INTO `text_resource` VALUES(10090, 'common.js.alert.choicesource', 'last updated @ 2012-02-16 15:49:42');
INSERT INTO `text_resource` VALUES(10091, 'common.js.alert.needonesource', 'last updated @ 2012-02-16 15:51:12');
INSERT INTO `text_resource` VALUES(10092, 'common.js.alert.asksoucedesc', 'last updated @ 2012-02-16 15:51:50');
INSERT INTO `text_resource` VALUES(10093, 'common.js.alert.askscoreoption', 'last updated @ 2012-02-16 15:52:36');
INSERT INTO `text_resource` VALUES(10094, 'common.js.alert.inputanswer', 'last updated @ 2012-02-16 15:53:26');
INSERT INTO `text_resource` VALUES(10095, 'common.js.alert.suggestedscore', 'last updated @ 2012-02-16 15:56:17');
INSERT INTO `text_resource` VALUES(10096, 'common.js.alert.comments', 'last updated @ 2012-02-16 15:58:47');
INSERT INTO `text_resource` VALUES(10097, 'common.js.err.interval', 'last updated @ 2012-02-16 16:00:42');
INSERT INTO `text_resource` VALUES(10098, 'common.js.alert.answerleft', 'last updated @ 2012-02-16 16:03:07');
INSERT INTO `text_resource` VALUES(10099, 'common.js.alert.sumbitassignment', 'last updated @ 2012-02-16 16:05:12');
INSERT INTO `text_resource` VALUES(10100, 'common.js.alert.pendingquestion', 'last updated @ 2012-02-16 16:07:41');
INSERT INTO `text_resource` VALUES(10101, 'common.js.alert.exceedwords', 'last updated @ 2012-02-16 16:10:28');
INSERT INTO `text_resource` VALUES(10102, 'common.js.alert.lesswords', 'last updated @ 2012-02-16 16:11:24');
INSERT INTO `text_resource` VALUES(10104, 'common.js.alert.topictitle', 'last updated @ 2012-02-16 16:15:15');
INSERT INTO `text_resource` VALUES(10105, 'common.js.msg.usernotes', 'last updated @ 2012-02-16 16:17:26');
INSERT INTO `text_resource` VALUES(10106, 'common.js.msg.staffnotes', 'last updated @ 2012-02-16 16:18:15');
INSERT INTO `text_resource` VALUES(10107, 'common.btn.view', 'last updated @ 2012-02-16 16:19:05');
INSERT INTO `text_resource` VALUES(10109, 'common.js.alt.unblock', 'last updated @ 2012-02-16 16:22:37');
INSERT INTO `text_resource` VALUES(10110, 'common.js.ckedit.toolbar.source', 'last updated @ 2012-02-16 16:24:57');
INSERT INTO `text_resource` VALUES(10111, 'common.js.ckedit.toolbar.pastetxt', 'last updated @ 2012-02-16 16:25:27');
INSERT INTO `text_resource` VALUES(10112, 'common.js.ckedit.toolbar.pastefromword', 'last updated @ 2012-02-16 16:25:54');
INSERT INTO `text_resource` VALUES(10113, 'common.js.ckedit.toolbar.print', 'last updated @ 2012-02-16 16:26:04');
INSERT INTO `text_resource` VALUES(10114, 'common.js.ckedit.toolbar.spellchecker', 'last updated @ 2012-02-16 16:26:22');
INSERT INTO `text_resource` VALUES(10115, 'common.js.ckedit.toolbar.scayt', 'last updated @ 2012-02-16 16:26:31');
INSERT INTO `text_resource` VALUES(10116, 'common.js.ckedit.toolbar.undo', 'last updated @ 2012-02-16 16:26:37');
INSERT INTO `text_resource` VALUES(10117, 'common.js.ckedit.toolbar.redo', 'last updated @ 2012-02-16 16:26:48');
INSERT INTO `text_resource` VALUES(10118, 'common.js.ckedit.toolbar.find', 'last updated @ 2012-02-16 16:26:54');
INSERT INTO `text_resource` VALUES(10119, 'common.js.ckedit.toolbar.replace', 'last updated @ 2012-02-16 16:27:02');
INSERT INTO `text_resource` VALUES(10120, 'common.js.ckedit.toolbar.selectall', 'last updated @ 2012-02-16 16:27:17');
INSERT INTO `text_resource` VALUES(10121, 'common.js.ckedit.toolbar.rmformat', 'last updated @ 2012-02-16 16:27:55');
INSERT INTO `text_resource` VALUES(10122, 'common.js.ckedit.toolbar.bold', 'last updated @ 2012-02-16 16:28:03');
INSERT INTO `text_resource` VALUES(10123, 'common.js.ckedit.toolbar.italic', 'last updated @ 2012-02-16 16:28:12');
INSERT INTO `text_resource` VALUES(10124, 'common.js.ckedit.toolbar.underline', 'last updated @ 2012-02-16 16:28:25');
INSERT INTO `text_resource` VALUES(10125, 'common.js.ckedit.toolbar.strike', 'last updated @ 2012-02-16 16:28:31');
INSERT INTO `text_resource` VALUES(10126, 'common.js.ckedit.toolbar.numlist', 'last updated @ 2012-02-16 16:28:43');
INSERT INTO `text_resource` VALUES(10127, 'common.js.ckedit.toolbar.bulletlist', 'last updated @ 2012-02-16 16:28:53');
INSERT INTO `text_resource` VALUES(10128, 'common.js.ckedit.toolbar.link', 'last updated @ 2012-02-16 16:28:58');
INSERT INTO `text_resource` VALUES(10129, 'common.js.ckedit.toolbar.unlink', 'last updated @ 2012-02-16 16:29:04');
INSERT INTO `text_resource` VALUES(10130, 'common.js.ckedit.toolbar.anchor', 'last updated @ 2012-02-16 16:29:11');
INSERT INTO `text_resource` VALUES(10131, 'common.js.ckedit.toolbar.format', 'last updated @ 2012-02-16 16:29:18');
INSERT INTO `text_resource` VALUES(10132, 'common.js.ckedit.toolbar.bgcolor', 'last updated @ 2012-02-16 16:29:25');
INSERT INTO `text_resource` VALUES(10133, 'common.js.ckedit.toolbar.maximize', 'last updated @ 2012-02-16 16:29:36');
INSERT INTO `text_resource` VALUES(10134, 'common.js.ckedit.toolbar', 'last updated @ 2012-02-16 16:53:13');
INSERT INTO `text_resource` VALUES(10135, 'common.js.alert.enhancedtext', 'last updated @ 2012-02-16 16:59:26');
INSERT INTO `text_resource` VALUES(10136, 'common.label.time', 'last updated @ 2012-02-16 17:01:03');
INSERT INTO `text_resource` VALUES(10137, 'common.label.topictitle', 'last updated @ 2012-02-16 17:01:29');
INSERT INTO `text_resource` VALUES(10138, 'common.js.alert.successenhance', 'last updated @ 2012-02-16 17:03:09');
INSERT INTO `text_resource` VALUES(10139, 'common.js.alert.title.success', 'last updated @ 2012-02-16 17:04:03');
INSERT INTO `text_resource` VALUES(10140, 'common.js.msg.staffdiscussion', 'last updated @ 2012-02-16 17:23:43');
INSERT INTO `text_resource` VALUES(10141, 'common.js.msg.staffauthordiscussion', 'last updated @ 2012-02-16 17:24:39');
INSERT INTO `text_resource` VALUES(10142, 'common.js.alert.succssreview', 'last updated @ 2012-02-16 17:26:25');
INSERT INTO `text_resource` VALUES(10143, 'common.js.msg.discussion', 'last updated @ 2012-02-16 17:30:00');
INSERT INTO `text_resource` VALUES(10146, 'common.js.alert.sumitquestion', 'last updated @ 2012-02-16 17:38:50');
INSERT INTO `text_resource` VALUES(10147, 'common.js.alert.incompletedquestion', 'last updated @ 2012-02-16 17:41:36');
INSERT INTO `text_resource` VALUES(10148, 'common.js.alert.title.sorry', 'last updated @ 2012-02-16 17:50:14');
INSERT INTO `text_resource` VALUES(10149, 'common.label.teamcontent', 'last updated @ 2012-02-16 18:22:56');
INSERT INTO `text_resource` VALUES(10150, 'common.js.alert.attachduplicated', 'last updated @ 2012-02-20 11:31:50');
INSERT INTO `text_resource` VALUES(10151, 'common.js.alert.attachfail', 'last updated @ 2012-02-20 11:34:12');
INSERT INTO `text_resource` VALUES(10152, 'common.js.alert.attachdesc', 'last updated @ 2012-02-20 11:36:50');
INSERT INTO `text_resource` VALUES(10153, 'common.js.alert.attachfile', 'last updated @ 2012-02-20 11:40:04');
INSERT INTO `text_resource` VALUES(10154, 'common.js.alert.deleteattach', 'last updated @ 2012-02-20 11:44:34');
INSERT INTO `text_resource` VALUES(10155, 'common.js.alert.deleteattachfail', 'last updated @ 2012-02-20 11:45:39');
INSERT INTO `text_resource` VALUES(10156, 'common.js.alert.uploadsuccess', 'last updated @ 2012-02-20 13:36:52');
INSERT INTO `text_resource` VALUES(10157, 'common.js.alert.fileexisted', 'last updated @ 2012-02-20 13:37:41');
INSERT INTO `text_resource` VALUES(10158, 'common.js.alert.invalidimage', 'last updated @ 2012-02-20 13:55:35');
INSERT INTO `text_resource` VALUES(10159, 'common.js.alert.noimagespecified', 'last updated @ 2012-02-20 13:57:53');
INSERT INTO `text_resource` VALUES(10160, 'common.js.alert.curpasswdempty', 'last updated @ 2012-02-20 13:59:04');
INSERT INTO `text_resource` VALUES(10161, 'common.js.alert.newpasswdempty', 'last updated @ 2012-02-20 13:59:46');
INSERT INTO `text_resource` VALUES(10162, 'common.js.alert.confirmpasswdempty', 'last updated @ 2012-02-20 14:01:14');
INSERT INTO `text_resource` VALUES(10163, 'common.js.alert.passwdinconsistent', 'last updated @ 2012-02-20 14:02:17');
INSERT INTO `text_resource` VALUES(10164, 'common.js.alert.passwdinvalid', 'last updated @ 2012-02-20 14:03:18');
INSERT INTO `text_resource` VALUES(10165, 'common.js.alert.passwdchngsuccess', 'last updated @ 2012-02-20 14:04:18');
INSERT INTO `text_resource` VALUES(10166, 'common.btn.editdeadline', 'last updated @ 2012-02-20 14:05:07');
INSERT INTO `text_resource` VALUES(10167, 'common.js.msg.newdeadline', 'last updated @ 2012-02-20 14:05:56');
INSERT INTO `text_resource` VALUES(10168, 'common.js.msg.endassignment', 'last updated @ 2012-02-20 14:09:14');
INSERT INTO `text_resource` VALUES(10170, 'common.js.msg.partialsubmission', 'last updated @ 2012-02-20 14:48:15');
INSERT INTO `text_resource` VALUES(10171, 'common.js.msg.forcecomplete', 'last updated @ 2012-02-20 14:49:45');
INSERT INTO `text_resource` VALUES(10172, 'common.js.msg.exitassignment', 'last updated @ 2012-02-20 14:50:36');
INSERT INTO `text_resource` VALUES(10173, 'common.js.alert.undone', 'last updated @ 2012-02-20 14:52:56');
INSERT INTO `text_resource` VALUES(10174, 'common.js.msg.selected', 'last updated @ 2012-02-20 14:54:20');
INSERT INTO `text_resource` VALUES(10175, 'common.btn.done', 'last updated @ 2012-02-20 15:18:33');
INSERT INTO `text_resource` VALUES(10176, 'common.label.project', 'last updated @ 2012-02-20 15:40:21');
INSERT INTO `text_resource` VALUES(10178, 'common.label.assigneduser', 'last updated @ 2012-02-20 15:41:49');
INSERT INTO `text_resource` VALUES(10179, 'common.label.casesubject', 'last updated @ 2012-02-20 15:44:48');
INSERT INTO `text_resource` VALUES(10180, 'common.label.casebody', 'last updated @ 2012-02-20 15:45:14');
INSERT INTO `text_resource` VALUES(10181, 'common.label.action', 'last updated @ 2012-02-20 15:45:23');
INSERT INTO `text_resource` VALUES(10182, 'common.label.fireall', 'last updated @ 2012-02-20 15:45:31');
INSERT INTO `text_resource` VALUES(10183, 'common.js.msg.usertriggerlist', 'last updated @ 2012-02-20 15:47:05');
INSERT INTO `text_resource` VALUES(10184, 'common.js.alert.descempty', 'last updated @ 2012-02-20 15:48:11');
INSERT INTO `text_resource` VALUES(10185, 'common.js.alert.prjnoselected', 'last updated @ 2012-02-20 15:49:03');
INSERT INTO `text_resource` VALUES(10186, 'common.js.alert.rolenoselected', 'last updated @ 2012-02-20 15:50:04');
INSERT INTO `text_resource` VALUES(10187, 'common.js.alert.noassigneduser', 'last updated @ 2012-02-20 15:50:49');
INSERT INTO `text_resource` VALUES(10188, 'common.js.alert.nocasesubject', 'last updated @ 2012-02-20 15:51:56');
INSERT INTO `text_resource` VALUES(10189, 'common.js.alert.nocasebody', 'last updated @ 2012-02-20 15:52:16');
INSERT INTO `text_resource` VALUES(10191, 'common.js.msg.total', 'last updated @ 2012-02-20 15:54:08');
INSERT INTO `text_resource` VALUES(10192, 'common.js.msg.new', 'last updated @ 2012-02-20 15:54:13');
INSERT INTO `text_resource` VALUES(10193, 'common.js.alert.toolength', 'last updated @ 2012-02-20 17:44:12');
INSERT INTO `text_resource` VALUES(10194, 'common.js.alert.filenotadded', 'last updated @ 2012-02-20 17:47:26');
INSERT INTO `text_resource` VALUES(10195, 'common.btn.save', 'last updated @ 2012-02-23 12:02:15');
INSERT INTO `text_resource` VALUES(10196, 'common.btn.saveindicator', 'last updated @ 2012-02-23 12:13:07');
INSERT INTO `text_resource` VALUES(10197, 'common.title.comments', 'last updated @ 2012-02-23 12:17:17');
INSERT INTO `text_resource` VALUES(10198, 'common.title.sources', 'last updated @ 2012-02-23 12:23:55');
INSERT INTO `text_resource` VALUES(10199, 'common.btn.publish', 'last updated @ 2012-02-23 12:45:19');
INSERT INTO `text_resource` VALUES(10200, 'common.btn.publishthis', 'last updated @ 2012-02-23 12:45:26');
INSERT INTO `text_resource` VALUES(10201, 'common.btn.addcomment', 'last updated @ 2012-02-23 12:47:56');
INSERT INTO `text_resource` VALUES(10202, 'common.label.adding', 'last updated @ 2012-02-23 12:54:02');
INSERT INTO `text_resource` VALUES(10203, 'common.title.from', 'last updated @ 2012-02-23 12:58:30');
INSERT INTO `text_resource` VALUES(10204, 'common.btn.donesubmit', 'last updated @ 2012-02-23 13:01:11');
INSERT INTO `text_resource` VALUES(10205, 'common.choice.onlyselected', 'last updated @ 2012-02-23 13:46:53');
INSERT INTO `text_resource` VALUES(10206, 'common.btn.submitindicators', 'last updated @ 2012-02-23 14:56:46');
INSERT INTO `text_resource` VALUES(10207, 'common.msg.scorecardnav', 'last updated @ 2012-02-23 15:48:59');
INSERT INTO `text_resource` VALUES(10208, 'common.btn.submit', 'last updated @ 2012-02-23 15:50:33');
INSERT INTO `text_resource` VALUES(10209, 'common.label.review', 'last updated @ 2012-02-23 15:56:43');
INSERT INTO `text_resource` VALUES(10210, 'common.label.reviews', 'last updated @ 2012-02-23 15:58:02');
INSERT INTO `text_resource` VALUES(10211, 'common.msg.nextcategory', 'last updated @ 2012-02-23 16:09:54');
INSERT INTO `text_resource` VALUES(10212, 'common.msg.pleasewait', 'last updated @ 2012-02-23 16:11:10');
INSERT INTO `text_resource` VALUES(10213, 'common.msg.youcontent', 'last updated @ 2012-02-23 16:38:33');
INSERT INTO `text_resource` VALUES(10214, 'common.btn.editdisabled', 'last updated @ 2012-02-23 16:39:46');
INSERT INTO `text_resource` VALUES(10215, 'common.msg.target', 'last updated @ 2012-02-23 16:40:45');
INSERT INTO `text_resource` VALUES(10216, 'common.msg.rulemanager', 'last updated @ 2012-02-23 16:41:37');
INSERT INTO `text_resource` VALUES(10217, 'common.boolean.no', 'last updated @ 2012-02-23 16:51:29');
INSERT INTO `text_resource` VALUES(10218, 'common.boolean.yes', 'last updated @ 2012-02-23 16:54:15');
INSERT INTO `text_resource` VALUES(10219, 'common.alert.nosubject', 'last updated @ 2012-02-23 16:57:20');
INSERT INTO `text_resource` VALUES(10220, 'common.label.tags', 'last updated @ 2012-02-23 17:01:17');
INSERT INTO `text_resource` VALUES(10221, 'common.msg.addtrigger', 'last updated @ 2012-02-23 17:19:57');
INSERT INTO `text_resource` VALUES(10222, 'common.msg.prevcategory', 'last updated @ 2012-02-23 17:22:18');
INSERT INTO `text_resource` VALUES(10223, 'common.alert.noreceiver', 'last updated @ 2012-02-23 17:24:28');
INSERT INTO `text_resource` VALUES(10224, 'common.msg.to', 'last updated @ 2012-02-23 17:25:31');
INSERT INTO `text_resource` VALUES(10225, 'common.btn.cancel', 'last updated @ 2012-02-23 17:27:15');
INSERT INTO `text_resource` VALUES(10226, 'common.alert.noanswer', 'last updated @ 2012-02-23 17:30:35');
INSERT INTO `text_resource` VALUES(10227, 'common.label.message', 'last updated @ 2012-02-23 17:36:50');
INSERT INTO `text_resource` VALUES(10228, 'common.alert.nomessage', 'last updated @ 2012-02-23 17:41:34');
INSERT INTO `text_resource` VALUES(10229, 'common.msg.nextindicator', 'last updated @ 2012-02-23 17:42:32');
INSERT INTO `text_resource` VALUES(10230, 'common.btn.sendmsg', 'last updated @ 2012-02-23 18:01:43');
INSERT INTO `text_resource` VALUES(10231, 'common.msg.prjupdates', 'last updated @ 2012-02-23 18:04:43');
INSERT INTO `text_resource` VALUES(10232, 'common.btn.apply', 'last updated @ 2012-02-23 18:05:25');
INSERT INTO `text_resource` VALUES(10233, 'common.btn.origanswer', 'last updated @ 2012-02-23 18:07:28');
INSERT INTO `text_resource` VALUES(10234, 'common.label.createtime', 'last updated @ 2012-02-23 18:08:50');
INSERT INTO `text_resource` VALUES(10235, 'common.msg.blockworkflow', 'last updated @ 2012-02-23 18:11:04');
INSERT INTO `text_resource` VALUES(10236, 'common.msg.blockpublish', 'last updated @ 2012-02-23 18:12:00');
INSERT INTO `text_resource` VALUES(10237, 'common.msg.suggestedanswer', 'last updated @ 2012-02-23 18:12:58');
INSERT INTO `text_resource` VALUES(10238, 'common.msg.previndicator', 'last updated @ 2012-02-23 18:16:59');
INSERT INTO `text_resource` VALUES(10239, 'common.msg.yourinbox', 'last updated @ 2012-02-23 18:18:02');
INSERT INTO `text_resource` VALUES(10240, 'common.msg.home', 'last updated @ 2012-02-23 18:22:35');
INSERT INTO `text_resource` VALUES(10241, 'common.label.cases', 'last updated @ 2012-02-23 18:25:42');
INSERT INTO `text_resource` VALUES(10242, 'common.label.fire', 'last updated @ 2012-02-23 18:26:31');
INSERT INTO `text_resource` VALUES(10243, 'common.label.desc', 'last updated @ 2012-02-23 18:27:52');
INSERT INTO `text_resource` VALUES(10244, 'common.label.file', 'last updated @ 2012-02-23 18:30:26');
INSERT INTO `text_resource` VALUES(10245, 'common.btn.reset', 'last updated @ 2012-02-23 18:31:03');
INSERT INTO `text_resource` VALUES(10246, 'common.msg.opinionagree', 'last updated @ 2012-02-23 18:32:35');
INSERT INTO `text_resource` VALUES(10247, 'common.msg.opiniondisagree', 'last updated @ 2012-02-23 18:33:46');
INSERT INTO `text_resource` VALUES(10248, 'common.msg.opinionnojudge', 'last updated @ 2012-02-23 18:34:55');
INSERT INTO `text_resource` VALUES(10249, 'common.label.subject', 'last updated @ 2012-02-23 18:36:07');
INSERT INTO `text_resource` VALUES(10250, 'common.msg.charterror', 'last updated @ 2012-02-23 18:38:01');
INSERT INTO `text_resource` VALUES(10251, 'common.btn.havequestions', 'last updated @ 2012-02-23 18:41:57');
INSERT INTO `text_resource` VALUES(10252, 'common.label.username', 'last updated @ 2012-02-23 20:21:58');
INSERT INTO `text_resource` VALUES(10253, 'common.btn.preview', 'last updated @ 2012-02-23 20:25:40');
INSERT INTO `text_resource` VALUES(10254, 'common.label.location', 'last updated @ 2012-02-23 20:26:58');
INSERT INTO `text_resource` VALUES(10255, 'common.label.emailmsgdetaillevel', 'last updated @ 2012-02-23 20:31:14');
INSERT INTO `text_resource` VALUES(10256, 'common.label.mailaddr', 'last updated @ 2012-02-23 20:32:59');
INSERT INTO `text_resource` VALUES(10257, 'common.label.reviewTips', 'last updated @ 2012-02-23 20:33:47');
INSERT INTO `text_resource` VALUES(10258, 'common.label.lastname', 'last updated @ 2012-02-23 20:40:15');
INSERT INTO `text_resource` VALUES(10259, 'common.label.note', 'last updated @ 2012-02-23 20:41:33');
INSERT INTO `text_resource` VALUES(10260, 'common.label.bio', 'last updated @ 2012-02-23 20:44:29');
INSERT INTO `text_resource` VALUES(10261, 'common.label.firstname', 'last updated @ 2012-02-23 20:46:03');
INSERT INTO `text_resource` VALUES(10262, 'common.btn.copydeeplink', 'last updated @ 2012-02-23 20:49:37');
INSERT INTO `text_resource` VALUES(10263, 'common.msg.setdeadline', 'last updated @ 2012-02-24 19:49:32');
INSERT INTO `text_resource` VALUES(30000, 'jsp.allcontent.pagetitle', 'Last update @2012-02-27 12:35:35');
INSERT INTO `text_resource` VALUES(30001, 'jsp.allcontent.allcontents', 'Last update @2012-02-27 12:35:35');
INSERT INTO `text_resource` VALUES(40000, 'jsp.answerDetails.pagetitle', 'Last update @2012-02-27 12:35:36');
INSERT INTO `text_resource` VALUES(50000, 'jsp.assignmentMessage.pagetitle', 'Last update @2012-02-27 12:35:36');
INSERT INTO `text_resource` VALUES(60000, 'jsp.attachment.attachments', 'Last update @2012-02-27 12:35:36');
INSERT INTO `text_resource` VALUES(60001, 'jsp.attachment.file', 'Last update @2012-02-27 12:35:37');
INSERT INTO `text_resource` VALUES(60002, 'jsp.attachment.size', 'Last update @2012-02-27 12:35:37');
INSERT INTO `text_resource` VALUES(60003, 'jsp.attachment.by', 'Last update @2012-02-27 12:35:37');
INSERT INTO `text_resource` VALUES(60004, 'jsp.attachment.mb', 'Last update @2012-02-27 12:35:37');
INSERT INTO `text_resource` VALUES(60005, 'jsp.attachment.kb', 'Last update @2012-02-27 12:35:37');
INSERT INTO `text_resource` VALUES(60006, 'jsp.attachment.b', 'Last update @2012-02-27 12:35:38');
INSERT INTO `text_resource` VALUES(60007, 'jsp.attachment.addNewAttachments', 'Last update @2012-02-27 12:35:38');
INSERT INTO `text_resource` VALUES(60008, 'jsp.attachment.maxFileSize', 'Last update @2012-02-27 12:35:38');
INSERT INTO `text_resource` VALUES(60009, 'jsp.attachment.maxFileSizeNum', 'Last update @2012-02-27 12:35:38');
INSERT INTO `text_resource` VALUES(70000, 'jsp.caseFilter.tag', 'Last update @2012-02-27 12:35:39');
INSERT INTO `text_resource` VALUES(80000, 'jsp.casePage.title.newCase', 'Last update @2012-02-27 12:35:39');
INSERT INTO `text_resource` VALUES(80001, 'jsp.casePage.title.caseDetail', 'Last update @2012-02-27 12:35:40');
INSERT INTO `text_resource` VALUES(80002, 'jsp.casePage.newCase', 'Last update @2012-02-27 12:35:40');
INSERT INTO `text_resource` VALUES(80003, 'jsp.casePage.case', 'Last update @2012-02-27 12:35:40');
INSERT INTO `text_resource` VALUES(80004, 'jsp.casePage.createBy', 'Last update @2012-02-27 12:35:40');
INSERT INTO `text_resource` VALUES(80005, 'jsp.casePage.caseTitle', 'Last update @2012-02-27 12:35:40');
INSERT INTO `text_resource` VALUES(80006, 'jsp.casePage.caseDescription', 'Last update @2012-02-27 12:35:40');
INSERT INTO `text_resource` VALUES(80007, 'jsp.casePage.casestatus', 'Last update @2012-02-27 12:35:41');
INSERT INTO `text_resource` VALUES(80008, 'jsp.casePage.openNew', 'Last update @2012-02-27 12:35:41');
INSERT INTO `text_resource` VALUES(80009, 'jsp.casePage.assignTo', 'Last update @2012-02-27 12:35:41');
INSERT INTO `text_resource` VALUES(80010, 'jsp.casePage.attachUsers', 'Last update @2012-02-27 12:35:41');
INSERT INTO `text_resource` VALUES(80011, 'jsp.casePage.attachContent', 'Last update @2012-02-27 12:35:42');
INSERT INTO `text_resource` VALUES(80012, 'jsp.casePage.attachTags', 'Last update @2012-02-27 12:35:42');
INSERT INTO `text_resource` VALUES(80013, 'jsp.casePage.attachFiles', 'Last update @2012-02-27 12:35:42');
INSERT INTO `text_resource` VALUES(90000, 'jsp.cases.title', 'Last update @2012-02-27 12:35:42');
INSERT INTO `text_resource` VALUES(90001, 'jsp.cases.openNewCase', 'Last update @2012-02-27 12:35:43');
INSERT INTO `text_resource` VALUES(90002, 'jsp.cases.allCases', 'Last update @2012-02-27 12:35:43');
INSERT INTO `text_resource` VALUES(100000, 'jsp.contentApproval.pagetitle', 'Last update @2012-02-27 12:35:43');
INSERT INTO `text_resource` VALUES(100001, 'jsp.contentApproval.ApproveInstruction', 'Last update @2012-02-27 12:35:44');
INSERT INTO `text_resource` VALUES(100002, 'jsp.contentApproval.ApproveThisContent', 'Last update @2012-02-27 12:35:44');
INSERT INTO `text_resource` VALUES(110000, 'jsp.contentGeneral.instructions', 'Last update @2012-02-27 12:35:44');
INSERT INTO `text_resource` VALUES(110001, 'jsp.contentGeneral.currentTasks', 'Last update @2012-02-27 12:35:44');
INSERT INTO `text_resource` VALUES(110002, 'jsp.contentGeneral.caseNO', 'Last update @2012-02-27 12:35:45');
INSERT INTO `text_resource` VALUES(110003, 'jsp.contentGeneral.caseOpened', 'Last update @2012-02-27 12:35:45');
INSERT INTO `text_resource` VALUES(110004, 'jsp.contentGeneral.caseLastUpdated', 'Last update @2012-02-27 12:35:45');
INSERT INTO `text_resource` VALUES(120000, 'jsp.contentPayment.pagetitle', 'Last update @2012-02-27 12:35:45');
INSERT INTO `text_resource` VALUES(120001, 'jsp.contentPayment.instruction', 'Last update @2012-02-27 12:35:46');
INSERT INTO `text_resource` VALUES(120002, 'jsp.contentPayment.paymentMount', 'Last update @2012-02-27 12:35:46');
INSERT INTO `text_resource` VALUES(120003, 'jsp.contentPayment.prompt', 'Last update @2012-02-27 12:35:46');
INSERT INTO `text_resource` VALUES(120004, 'jsp.contentPayment.payees', 'Last update @2012-02-27 12:35:46');
INSERT INTO `text_resource` VALUES(120005, 'jsp.contentPayment.paymentDate', 'Last update @2012-02-27 12:35:46');
INSERT INTO `text_resource` VALUES(150000, 'jsp.error.pagetitle', 'Last update @2012-02-27 12:35:49');
INSERT INTO `text_resource` VALUES(150001, 'jsp.error.errors', 'Last update @2012-02-27 12:35:49');
INSERT INTO `text_resource` VALUES(150002, 'jsp.error.errMsg', 'Last update @2012-02-27 12:35:49');
INSERT INTO `text_resource` VALUES(150003, 'jsp.error.back', 'Last update @2012-02-27 12:35:49');
INSERT INTO `text_resource` VALUES(160000, 'jsp.fileupload.browseFiles', 'Last update @2012-02-27 12:35:49');
INSERT INTO `text_resource` VALUES(160001, 'jsp.fileupload.clearList', 'Last update @2012-02-27 12:35:49');
INSERT INTO `text_resource` VALUES(160002, 'jsp.fileupload.startUpload', 'Last update @2012-02-27 12:35:50');
INSERT INTO `text_resource` VALUES(180000, 'jsp.header.home', 'Last update @2012-02-27 12:35:50');
INSERT INTO `text_resource` VALUES(180001, 'jsp.header.menu.allcontent', 'Last update @2012-02-27 12:35:50');
INSERT INTO `text_resource` VALUES(180002, 'jsp.header.menu.queue', 'Last update @2012-02-27 12:35:51');
INSERT INTO `text_resource` VALUES(180003, 'jsp.header.menu.people', 'Last update @2012-02-27 12:35:51');
INSERT INTO `text_resource` VALUES(180004, 'jsp.header.menu.messaging', 'Last update @2012-02-27 12:35:51');
INSERT INTO `text_resource` VALUES(180005, 'jsp.header.menu.help', 'Last update @2012-02-27 12:35:51');
INSERT INTO `text_resource` VALUES(180006, 'jsp.header.logout', 'Last update @2012-02-27 12:35:51');
INSERT INTO `text_resource` VALUES(180007, 'jsp.header.admin', 'Last update @2012-02-27 12:35:52');
INSERT INTO `text_resource` VALUES(190000, 'jsp.help.title', 'Last update @2012-02-27 12:35:52');
INSERT INTO `text_resource` VALUES(190001, 'jsp.help.msg1', 'Last update @2012-02-27 12:35:52');
INSERT INTO `text_resource` VALUES(190002, 'jsp.help.msg2', 'Last update @2012-02-27 12:35:52');
INSERT INTO `text_resource` VALUES(190003, 'jsp.help.msg3', 'Last update @2012-02-27 12:35:52');
INSERT INTO `text_resource` VALUES(190004, 'jsp.help.msg4', 'Last update @2012-02-27 12:35:52');
INSERT INTO `text_resource` VALUES(190005, 'jsp.help.msg5', 'Last update @2012-02-27 12:35:53');
INSERT INTO `text_resource` VALUES(190006, 'jsp.help.msg6', 'Last update @2012-02-27 12:35:53');
INSERT INTO `text_resource` VALUES(190007, 'jsp.help.wokflowconsole', 'Last update @2012-02-27 12:35:53');
INSERT INTO `text_resource` VALUES(200000, 'jsp.horseApproval.pagetitle', 'Last update @2012-02-27 12:35:53');
INSERT INTO `text_resource` VALUES(200001, 'jsp.horseApproval.instruction', 'Last update @2012-02-27 12:35:53');
INSERT INTO `text_resource` VALUES(200002, 'jsp.horseApproval.approveDone', 'Last update @2012-02-27 12:35:54');
INSERT INTO `text_resource` VALUES(220000, 'jsp.indicatorTag.instruction', 'Last update @2012-02-27 12:35:54');
INSERT INTO `text_resource` VALUES(220001, 'jsp.indicatorTag.buttonAddTag', 'Last update @2012-02-27 12:35:54');
INSERT INTO `text_resource` VALUES(240000, 'jsp.indicatorPeerReviewPage.title', 'Last update @2012-02-27 12:35:55');
INSERT INTO `text_resource` VALUES(240001, 'jsp.indicatorPeerReviewPage.link2Answer', 'Last update @2012-02-27 12:35:55');
INSERT INTO `text_resource` VALUES(250000, 'jsp.indicatorPRReviewPage.title', 'Last update @2012-02-27 12:35:55');
INSERT INTO `text_resource` VALUES(260000, 'jsp.indicatorReviewPage.title', 'Last update @2012-02-27 12:35:56');
INSERT INTO `text_resource` VALUES(270000, 'jsp.journalContentVersion.pagetitle', 'Last update @2012-02-27 12:35:56');
INSERT INTO `text_resource` VALUES(270001, 'jsp.journalContentVersion.content', 'Last update @2012-02-27 12:35:56');
INSERT INTO `text_resource` VALUES(280000, 'jsp.journalOveralReview.title', 'Last update @2012-02-27 12:35:56');
INSERT INTO `text_resource` VALUES(290000, 'jsp.journalPeerReview.pagetitle', 'Last update @2012-02-27 12:35:57');
INSERT INTO `text_resource` VALUES(300000, 'jsp.journalPRReview.pagetitle', 'Last update @2012-02-27 12:35:57');
INSERT INTO `text_resource` VALUES(310000, 'jsp.journalReview.pagetitle', 'Last update @2012-02-27 12:35:58');
INSERT INTO `text_resource` VALUES(320000, 'jsp.journalReviewResponse.pagetitle', 'Last update @2012-02-27 12:35:58');
INSERT INTO `text_resource` VALUES(330000, 'common.label.username.null', 'Last update @2012-02-27 12:35:58');
INSERT INTO `text_resource` VALUES(330001, 'jsp.login.pwd.null', 'Last update @2012-02-27 12:35:59');
INSERT INTO `text_resource` VALUES(330002, 'common.label.username.input', 'Last update @2012-02-27 12:35:59');
INSERT INTO `text_resource` VALUES(330003, 'common.label.username.check', 'Last update @2012-02-27 12:35:59');
INSERT INTO `text_resource` VALUES(330004, 'jsp.login.pwd.toEmail', 'Last update @2012-02-27 12:35:59');
INSERT INTO `text_resource` VALUES(330005, 'jsp.login.check.error', 'Last update @2012-02-27 12:35:59');
INSERT INTO `text_resource` VALUES(330006, 'jsp.login.tomail.error', 'Last update @2012-02-27 12:35:59');
INSERT INTO `text_resource` VALUES(330007, 'jsp.login.pagetitle', 'Last update @2012-02-27 12:36:00');
INSERT INTO `text_resource` VALUES(330008, 'jsp.login.pwd', 'Last update @2012-02-27 12:36:00');
INSERT INTO `text_resource` VALUES(340000, 'jsp.messageDetail.pagetitle', 'Last update @2012-02-27 12:36:00');
INSERT INTO `text_resource` VALUES(340001, 'jsp.messageDetail.reply', 'Last update @2012-02-27 12:36:00');
INSERT INTO `text_resource` VALUES(340002, 'jsp.messageDetail.title', 'Last update @2012-02-27 12:36:00');
INSERT INTO `text_resource` VALUES(350000, 'jsp.messages.pagetitle', 'Last update @2012-02-27 12:36:01');
INSERT INTO `text_resource` VALUES(350001, 'jsp.messages.createNewMsg', 'Last update @2012-02-27 12:36:01');
INSERT INTO `text_resource` VALUES(350002, 'jsp.messages.newCount', 'Last update @2012-02-27 12:36:01');
INSERT INTO `text_resource` VALUES(350003, 'jsp.messages.total', 'Last update @2012-02-27 12:36:01');
INSERT INTO `text_resource` VALUES(350004, 'jsp.messages.from', 'Last update @2012-02-27 12:36:01');
INSERT INTO `text_resource` VALUES(350005, 'jsp.messages.date', 'Last update @2012-02-27 12:36:02');
INSERT INTO `text_resource` VALUES(350006, 'jsp.messages.new', 'Last update @2012-02-27 12:36:02');
INSERT INTO `text_resource` VALUES(350007, 'jsp.messages.rows', 'Last update @2012-02-27 12:36:02');
INSERT INTO `text_resource` VALUES(360000, 'jsp.messageSidebar.new', 'Last update @2012-02-27 12:36:03');
INSERT INTO `text_resource` VALUES(360001, 'jsp.messageSidebar.from', 'Last update @2012-02-27 12:36:03');
INSERT INTO `text_resource` VALUES(360002, 'jsp.messageSidebar.more', 'Last update @2012-02-27 12:36:03');
INSERT INTO `text_resource` VALUES(370000, 'jsp.myOpenCases.yourOpenCases', 'Last update @2012-02-27 12:36:03');
INSERT INTO `text_resource` VALUES(380000, 'jsp.newMessage.pagetitle', 'Last update @2012-02-27 12:36:04');
INSERT INTO `text_resource` VALUES(380001, 'jsp.newMessage.newMsg', 'Last update @2012-02-27 12:36:04');
INSERT INTO `text_resource` VALUES(380002, 'jsp.newMessage.projectUpdate', 'Last update @2012-02-27 12:36:04');
INSERT INTO `text_resource` VALUES(390000, 'jsp.notebook.pagetitle', 'Last update @2012-02-27 12:36:05');
INSERT INTO `text_resource` VALUES(390001, 'jsp.notebook.notification', 'Last update @2012-02-27 12:36:05');
INSERT INTO `text_resource` VALUES(390002, 'jsp.notebook.youHave', 'Last update @2012-02-27 12:36:05');
INSERT INTO `text_resource` VALUES(390003, 'jsp.notebook.assignedTasks', 'Last update @2012-02-27 12:36:05');
INSERT INTO `text_resource` VALUES(390004, 'jsp.notebook.enter', 'Last update @2012-02-27 12:36:06');
INSERT INTO `text_resource` VALUES(400000, 'jsp.notebookDisplay.pagetitle', 'Last update @2012-02-27 12:36:06');
INSERT INTO `text_resource` VALUES(410000, 'jsp.notebookEdit.pagetitle', 'Last update @2012-02-27 12:36:06');
INSERT INTO `text_resource` VALUES(420000, 'jsp.notebookEditor.previousVersions', 'Last update @2012-02-27 12:36:07');
INSERT INTO `text_resource` VALUES(420001, 'jsp.notebookEditor.notebookEditor', 'Last update @2012-02-27 12:36:07');
INSERT INTO `text_resource` VALUES(420002, 'jsp.notebookEditor.words', 'Last update @2012-02-27 12:36:07');
INSERT INTO `text_resource` VALUES(430000, 'jsp.notebookView.editThis', 'Last update @2012-02-27 12:36:07');
INSERT INTO `text_resource` VALUES(430001, 'jsp.notebookView.notebookContent', 'Last update @2012-02-27 12:36:08');
INSERT INTO `text_resource` VALUES(440000, 'jsp.peerReview.peerReviewTips', 'Last update @2012-02-27 12:36:08');
INSERT INTO `text_resource` VALUES(450000, 'jsp.people.pagetitle', 'Last update @2012-02-27 12:36:09');
INSERT INTO `text_resource` VALUES(450001, 'jsp.people.shouldKnow', 'Last update @2012-02-27 12:36:09');
INSERT INTO `text_resource` VALUES(450002, 'jsp.people.yourTeams', 'Last update @2012-02-27 12:36:09');
INSERT INTO `text_resource` VALUES(450003, 'jsp.people.allUsers', 'Last update @2012-02-27 12:36:09');
INSERT INTO `text_resource` VALUES(450004, 'jsp.people.target', 'Last update @2012-02-27 12:36:09');
INSERT INTO `text_resource` VALUES(460000, 'jsp.peopleFilter.team', 'Last update @2012-02-27 12:36:10');
INSERT INTO `text_resource` VALUES(460001, 'jsp.peopleFilter.caseAssociation', 'Last update @2012-02-27 12:36:10');
INSERT INTO `text_resource` VALUES(460002, 'jsp.peopleFilter.assignmentStatus', 'Last update @2012-02-27 12:36:11');
INSERT INTO `text_resource` VALUES(470000, 'jsp.peopleProfile.pagetitle', 'Last update @2012-02-27 12:36:11');
INSERT INTO `text_resource` VALUES(470001, 'jsp.peopleProfile.user', 'Last update @2012-02-27 12:36:11');
INSERT INTO `text_resource` VALUES(470002, 'jsp.peopleProfile.limitedBio', 'Last update @2012-02-27 12:36:11');
INSERT INTO `text_resource` VALUES(470003, 'jsp.peopleProfile.fullBio', 'Last update @2012-02-27 12:36:12');
INSERT INTO `text_resource` VALUES(470004, 'jsp.peopleProfile.phone', 'Last update @2012-02-27 12:36:12');
INSERT INTO `text_resource` VALUES(470005, 'jsp.peopleProfile.mobilePhone', 'Last update @2012-02-27 12:36:12');
INSERT INTO `text_resource` VALUES(470006, 'jsp.peopleProfile.sysInfo', 'Last update @2012-02-27 12:36:12');
INSERT INTO `text_resource` VALUES(470007, 'jsp.peopleProfile.email', 'Last update @2012-02-27 12:36:12');
INSERT INTO `text_resource` VALUES(470008, 'jsp.peopleProfile.forwardInboxMsg', 'Last update @2012-02-27 12:36:12');
INSERT INTO `text_resource` VALUES(470009, 'common.label.emailmsgdetaillevel.alert', 'Last update @2012-02-27 12:36:13');
INSERT INTO `text_resource` VALUES(470010, 'common.label.emailmsgdetaillevel.fullMsg', 'Last update @2012-02-27 12:36:13');
INSERT INTO `text_resource` VALUES(470011, 'jsp.peopleProfile.language', 'Last update @2012-02-27 12:36:13');
INSERT INTO `text_resource` VALUES(470012, 'jsp.peopleProfile.project', 'Last update @2012-02-27 12:36:13');
INSERT INTO `text_resource` VALUES(470013, 'jsp.peopleProfile.role', 'Last update @2012-02-27 12:36:13');
INSERT INTO `text_resource` VALUES(470014, 'jsp.peopleProfile.lastLoginTime', 'Last update @2012-02-27 12:36:13');
INSERT INTO `text_resource` VALUES(470015, 'jsp.peopleProfile.lastLogoutTime', 'Last update @2012-02-27 12:36:14');
INSERT INTO `text_resource` VALUES(470016, 'jsp.peopleProfile.inactive', 'Last update @2012-02-27 12:36:14');
INSERT INTO `text_resource` VALUES(470017, 'jsp.peopleProfile.active', 'Last update @2012-02-27 12:36:14');
INSERT INTO `text_resource` VALUES(470018, 'jsp.peopleProfile.deleted', 'Last update @2012-02-27 12:36:14');
INSERT INTO `text_resource` VALUES(470019, 'jsp.peopleProfile.sendSitemail', 'Last update @2012-02-27 12:36:14');
INSERT INTO `text_resource` VALUES(470020, 'jsp.peopleProfile.aboutYourUserInfo', 'Last update @2012-02-27 12:36:14');
INSERT INTO `text_resource` VALUES(470021, 'jsp.peopleProfile.fullBioE', 'Last update @2012-02-27 12:36:15');
INSERT INTO `text_resource` VALUES(470022, 'jsp.peopleProfile.sysInfoE', 'Last update @2012-02-27 12:36:15');
INSERT INTO `text_resource` VALUES(470023, 'jsp.peopleProfile.sysEmail', 'Last update @2012-02-27 12:36:15');
INSERT INTO `text_resource` VALUES(470024, 'jsp.peopleProfile.sysTips', 'Last update @2012-02-27 12:36:16');
INSERT INTO `text_resource` VALUES(470025, 'jsp.peopleProfile.pwd.change', 'Last update @2012-02-27 12:36:16');
INSERT INTO `text_resource` VALUES(470026, 'jsp.peopleProfile.pwd.cur', 'Last update @2012-02-27 12:36:16');
INSERT INTO `text_resource` VALUES(470027, 'jsp.peopleProfile.pwd.new', 'Last update @2012-02-27 12:36:16');
INSERT INTO `text_resource` VALUES(470028, 'jsp.peopleProfile.pwd.new.confirm', 'Last update @2012-02-27 12:36:16');
INSERT INTO `text_resource` VALUES(470029, 'jsp.peopleProfile.changIconTips', 'Last update @2012-02-27 12:36:16');
INSERT INTO `text_resource` VALUES(470030, 'jsp.peopleProfile.peopleIcon', 'Last update @2012-02-27 12:36:17');
INSERT INTO `text_resource` VALUES(470031, 'jsp.peopleProfile.upload', 'Last update @2012-02-27 12:36:17');
INSERT INTO `text_resource` VALUES(470032, 'jsp.peopleProfile.assignment', 'Last update @2012-02-27 12:36:17');
INSERT INTO `text_resource` VALUES(470033, 'jsp.peopleProfile.openCases', 'Last update @2012-02-27 12:36:17');
INSERT INTO `text_resource` VALUES(490000, 'jsp.queues.pagetitle', 'Last update @2012-02-27 12:36:18');
INSERT INTO `text_resource` VALUES(490001, 'jsp.queues.noQuenes', 'Last update @2012-02-27 12:36:18');
INSERT INTO `text_resource` VALUES(490002, 'jsp.queues.avgTime.assign', 'Last update @2012-02-27 12:36:18');
INSERT INTO `text_resource` VALUES(490003, 'jsp.queues.days', 'Last update @2012-02-27 12:36:18');
INSERT INTO `text_resource` VALUES(490004, 'jsp.queues.avgTimeComplete', 'Last update @2012-02-27 12:36:19');
INSERT INTO `text_resource` VALUES(490005, 'jsp.queues.content', 'Last update @2012-02-27 12:36:19');
INSERT INTO `text_resource` VALUES(490006, 'jsp.queues.inQueue', 'Last update @2012-02-27 12:36:19');
INSERT INTO `text_resource` VALUES(490007, 'jsp.queues.assignment', 'Last update @2012-02-27 12:36:19');
INSERT INTO `text_resource` VALUES(490008, 'jsp.queues.setAssignment', 'Last update @2012-02-27 12:36:19');
INSERT INTO `text_resource` VALUES(490009, 'jsp.queues.setPriority', 'Last update @2012-02-27 12:36:19');
INSERT INTO `text_resource` VALUES(490010, 'jsp.queues.claimThis', 'Last update @2012-02-27 12:36:20');
INSERT INTO `text_resource` VALUES(490011, 'jsp.queues.assignTo', 'Last update @2012-02-27 12:36:20');
INSERT INTO `text_resource` VALUES(490012, 'jsp.queues.noAssign', 'Last update @2012-02-27 12:36:20');
INSERT INTO `text_resource` VALUES(490013, 'jsp.queues.low', 'Last update @2012-02-27 12:36:20');
INSERT INTO `text_resource` VALUES(490014, 'jsp.queues.medium', 'Last update @2012-02-27 12:36:20');
INSERT INTO `text_resource` VALUES(490015, 'jsp.queues.high', 'Last update @2012-02-27 12:36:20');
INSERT INTO `text_resource` VALUES(490016, 'jsp.queues.update.assign', 'Last update @2012-02-27 12:36:21');
INSERT INTO `text_resource` VALUES(490017, 'jsp.queues.returnToQueue', 'Last update @2012-02-27 12:36:21');
INSERT INTO `text_resource` VALUES(490018, 'jsp.queues.iWantThis', 'Last update @2012-02-27 12:36:21');
INSERT INTO `text_resource` VALUES(500000, 'jsp.replyMessage.pagetitle', 'Last update @2012-02-27 12:36:21');
INSERT INTO `text_resource` VALUES(500001, 'jsp.replyMessage.replyMessage', 'Last update @2012-02-27 12:36:21');
INSERT INTO `text_resource` VALUES(500002, 'jsp.replyMessage.prjUpdate', 'Last update @2012-02-27 12:36:22');
INSERT INTO `text_resource` VALUES(510000, 'jsp.ruleManager.pagetitle', 'Last update @2012-02-27 12:36:22');
INSERT INTO `text_resource` VALUES(510001, 'jsp.ruleManager.ruleFiles', 'Last update @2012-02-27 12:36:22');
INSERT INTO `text_resource` VALUES(510002, 'jsp.ruleManager.ruleComponents', 'Last update @2012-02-27 12:36:23');
INSERT INTO `text_resource` VALUES(510003, 'jsp.ruleManager.path', 'Last update @2012-02-27 12:36:23');
INSERT INTO `text_resource` VALUES(2150000, 'jsp.clientpage.pagetitle', 'Last update @2012-02-27 12:36:23');
INSERT INTO `text_resource` VALUES(2150001, 'jsp.clientpage.h1', 'Last update @2012-02-27 12:36:24');
INSERT INTO `text_resource` VALUES(2160000, 'jsp.configtool.pagetitle', 'Last update @2012-02-27 12:36:24');
INSERT INTO `text_resource` VALUES(2160001, 'jsp.configtool.h1', 'Last update @2012-02-27 12:36:24');
INSERT INTO `text_resource` VALUES(2160002, 'jsp.configtool.configuration', 'Last update @2012-02-27 12:36:24');
INSERT INTO `text_resource` VALUES(2160003, 'jsp.configtool.width', 'Last update @2012-02-27 12:36:24');
INSERT INTO `text_resource` VALUES(2160004, 'jsp.configtool.height', 'Last update @2012-02-27 12:36:24');
INSERT INTO `text_resource` VALUES(2160005, 'jsp.configtool.targetaddr', 'Last update @2012-02-27 12:36:25');
INSERT INTO `text_resource` VALUES(2160006, 'jsp.configtool.parameter', 'Last update @2012-02-27 12:36:25');
INSERT INTO `text_resource` VALUES(2160007, 'jsp.configtool.mapptingto', 'Last update @2012-02-27 12:36:25');
INSERT INTO `text_resource` VALUES(2160008, 'jsp.configtool.btn.rmselected', 'Last update @2012-02-27 12:36:25');
INSERT INTO `text_resource` VALUES(2160009, 'jsp.configtool.btn.gencode', 'Last update @2012-02-27 12:36:25');
INSERT INTO `text_resource` VALUES(2170000, 'jsp.widget1.pagetitle', 'Last update @2012-02-27 12:36:27');
INSERT INTO `text_resource` VALUES(2170001, 'jsp.widget1.dt', 'Last update @2012-02-27 12:36:27');
INSERT INTO `text_resource` VALUES(2180000, 'jsp.widget2.title', 'Last update @2012-02-27 12:36:27');
INSERT INTO `text_resource` VALUES(2180001, 'jsp.widget2.entername', 'Last update @2012-02-27 12:36:27');
INSERT INTO `text_resource` VALUES(2180002, 'jsp.widget2.chooseflavor', 'Last update @2012-02-27 12:36:28');
INSERT INTO `text_resource` VALUES(2180003, 'jsp.widget2.chocolate', 'Last update @2012-02-27 12:36:28');
INSERT INTO `text_resource` VALUES(2180004, 'jsp.widget2.strawberry', 'Last update @2012-02-27 12:36:28');
INSERT INTO `text_resource` VALUES(2180005, 'jsp.widget2.vanilla', 'Last update @2012-02-27 12:36:28');
INSERT INTO `text_resource` VALUES(2190000, 'jsp.widget3.pagetitle', 'Last update @2012-02-27 12:36:28');
INSERT INTO `text_resource` VALUES(2190001, 'jsp.widget3.clicklinks', 'Last update @2012-02-27 12:36:28');
INSERT INTO `text_resource` VALUES(2190002, 'jsp.widget3.gotojournal', 'Last update @2012-02-27 12:36:29');
INSERT INTO `text_resource` VALUES(520000, 'jsp.scorecardDisplay.pagetitle', 'Last update @2012-02-27 12:36:29');
INSERT INTO `text_resource` VALUES(530000, 'jsp.scorecardIndicatorSearch.tags', 'Last update @2012-02-27 12:36:29');
INSERT INTO `text_resource` VALUES(540000, 'jsp.scorecardNav.readOnly', 'Last update @2012-02-27 12:36:30');
INSERT INTO `text_resource` VALUES(540001, 'jsp.scorecardNav.nav', 'Last update @2012-02-27 12:36:30');
INSERT INTO `text_resource` VALUES(540002, 'jsp.scorecardNav.questionList', 'Last update @2012-02-27 12:36:30');
INSERT INTO `text_resource` VALUES(540003, 'jsp.scorecardNav.submitQuestion', 'Last update @2012-02-27 12:36:30');
INSERT INTO `text_resource` VALUES(540004, 'jsp.scorecardNav.indicatorTags', 'Last update @2012-02-27 12:36:30');
INSERT INTO `text_resource` VALUES(550000, 'jsp.scorecardPRDisagree.list', 'Last update @2012-02-27 12:36:30');
INSERT INTO `text_resource` VALUES(560000, 'jsp.sourceWMChoice.sources.desc', 'Last update @2012-02-27 12:36:31');
INSERT INTO `text_resource` VALUES(560001, 'jsp.sourceWMChoice.sources.report', 'Last update @2012-02-27 12:36:31');
INSERT INTO `text_resource` VALUES(560002, 'jsp.sourceWMChoice.sources.acadamic', 'Last update @2012-02-27 12:36:31');
INSERT INTO `text_resource` VALUES(560003, 'jsp.sourceWMChoice.sources.govStudy', 'Last update @2012-02-27 12:36:31');
INSERT INTO `text_resource` VALUES(560004, 'jsp.sourceWMChoice.sources.organic', 'Last update @2012-02-27 12:36:31');
INSERT INTO `text_resource` VALUES(560005, 'jsp.sourceWMChoice.sources.govOfficial', 'Last update @2012-02-27 12:36:32');
INSERT INTO `text_resource` VALUES(560006, 'jsp.sourceWMChoice.sources.interAcadamic', 'Last update @2012-02-27 12:36:32');
INSERT INTO `text_resource` VALUES(560007, 'jsp.sourceWMChoice.sources.civilSociety', 'Last update @2012-02-27 12:36:32');
INSERT INTO `text_resource` VALUES(560008, 'jsp.sourceWMChoice.sources.journal', 'Last update @2012-02-27 12:36:32');
INSERT INTO `text_resource` VALUES(570000, 'jsp.sourceWidgetText.sourcedesc', 'Last update @2012-02-27 12:36:32');
INSERT INTO `text_resource` VALUES(590000, 'jsp.surveyAnswer.title', 'Last update @2012-02-27 12:36:33');
INSERT INTO `text_resource` VALUES(600000, 'jsp.surveryAnsDisp.pagetitle', 'Last update @2012-02-27 12:36:34');
INSERT INTO `text_resource` VALUES(600001, 'jsp.surveryAnsDisp.answerFrom', 'Last update @2012-02-27 12:36:34');
INSERT INTO `text_resource` VALUES(640000, 'jsp.surveryAnsOrig.pagetitle', 'Last update @2012-02-27 12:36:36');
INSERT INTO `text_resource` VALUES(640001, 'jsp.surveryAnsOrig.taskName', 'Last update @2012-02-27 12:36:36');
INSERT INTO `text_resource` VALUES(650000, 'jsp.surveryAnsPR.opinions', 'Last update @2012-02-27 12:36:36');
INSERT INTO `text_resource` VALUES(650001, 'jsp.surveryAnsPR.agree', 'Last update @2012-02-27 12:36:37');
INSERT INTO `text_resource` VALUES(650002, 'jsp.surveryAnsPR.opinion.1', 'Last update @2012-02-27 12:36:37');
INSERT INTO `text_resource` VALUES(650003, 'jsp.surveryAnsPR.opinion1.desc', 'Last update @2012-02-27 12:36:37');
INSERT INTO `text_resource` VALUES(650004, 'jsp.surveryAnsPR.opinion2.desc', 'Last update @2012-02-27 12:36:37');
INSERT INTO `text_resource` VALUES(660000, 'jsp.surveryAnsPRDisp.opinion', 'Last update @2012-02-27 12:36:37');
INSERT INTO `text_resource` VALUES(660001, 'jsp.surveryAnsPRDisp.opinion.1', 'Last update @2012-02-27 12:36:38');
INSERT INTO `text_resource` VALUES(660002, 'jsp.surveryAnsPRDisp.opinion1.desc', 'Last update @2012-02-27 12:36:38');
INSERT INTO `text_resource` VALUES(660003, 'jsp.surveryAnsPRDisp.opinion2.desc', 'Last update @2012-02-27 12:36:38');
INSERT INTO `text_resource` VALUES(690000, 'jsp.surveryAnsBar.add.question', 'Last update @2012-02-27 12:36:39');
INSERT INTO `text_resource` VALUES(690001, 'jsp.surveryAnsBar.question.list', 'Last update @2012-02-27 12:36:39');
INSERT INTO `text_resource` VALUES(710000, 'jsp.surveyOverallRev.pagetitle', 'Last update @2012-02-27 12:36:39');
INSERT INTO `text_resource` VALUES(730000, 'jsp.sidebar.h3', 'Last update @2012-02-27 12:36:40');
INSERT INTO `text_resource` VALUES(730001, 'jsp.sidebar.loadanswer', 'Last update @2012-02-27 12:36:40');
INSERT INTO `text_resource` VALUES(740000, 'jsp.userfinder.pagetitle', 'Last update @2012-02-27 12:36:40');
INSERT INTO `text_resource` VALUES(740001, 'jsp.userfinder.viewList', 'Last update @2012-02-27 12:36:40');
INSERT INTO `text_resource` VALUES(740002, 'jsp.userfinder.noData', 'Last update @2012-02-27 12:36:41');
INSERT INTO `text_resource` VALUES(740003, 'common.label.projectChoose', 'Last update @2012-02-27 12:36:41');
INSERT INTO `text_resource` VALUES(740004, 'jsp.userfinder.add.roleChoose', 'Last update @2012-02-27 12:36:41');
INSERT INTO `text_resource` VALUES(740005, 'jsp.userfinder.add.userChoose', 'Last update @2012-02-27 12:36:41');
INSERT INTO `text_resource` VALUES(740006, 'jsp.userfinder.add.caseSubject', 'Last update @2012-02-27 12:36:41');
INSERT INTO `text_resource` VALUES(740007, 'jsp.userfinder.add.error', 'Last update @2012-02-27 12:36:42');
INSERT INTO `text_resource` VALUES(760000, 'jsp.workflowConsole.pagetitle', 'Last update @2012-02-27 12:36:42');
INSERT INTO `text_resource` VALUES(760001, 'jsp.workflowConsole.workflows', 'Last update @2012-02-27 12:36:42');
INSERT INTO `text_resource` VALUES(760002, 'jsp.workflowConsole.workflows.runAll', 'Last update @2012-02-27 12:36:42');
INSERT INTO `text_resource` VALUES(760003, 'jsp.workflowConsole.workflows.id', 'Last update @2012-02-27 12:36:42');
INSERT INTO `text_resource` VALUES(760004, 'jsp.workflowConsole.workflows.description', 'Last update @2012-02-27 12:36:43');
INSERT INTO `text_resource` VALUES(760005, 'jsp.workflowConsole.workflows.createdAt', 'Last update @2012-02-27 12:36:43');
INSERT INTO `text_resource` VALUES(760006, 'jsp.workflowConsole.workflows.createdBy', 'Last update @2012-02-27 12:36:43');
INSERT INTO `text_resource` VALUES(760007, 'jsp.workflowConsole.workflows.duration', 'Last update @2012-02-27 12:36:43');
INSERT INTO `text_resource` VALUES(760008, 'jsp.workflowConsole.workflowObjects', 'Last update @2012-02-27 12:36:43');
INSERT INTO `text_resource` VALUES(760009, 'jsp.workflowConsole.workflowObjects.target', 'Last update @2012-02-27 12:36:43');
INSERT INTO `text_resource` VALUES(760010, 'jsp.workflowConsole.workflowObjects.object', 'Last update @2012-02-27 12:36:43');
INSERT INTO `text_resource` VALUES(760011, 'jsp.workflowConsole.workflowObjects.startTime', 'Last update @2012-02-27 12:36:44');
INSERT INTO `text_resource` VALUES(760012, 'jsp.workflowConsole.goals', 'Last update @2012-02-27 12:36:44');
INSERT INTO `text_resource` VALUES(760013, 'jsp.workflowConsole.goals.target', 'Last update @2012-02-27 12:36:44');
INSERT INTO `text_resource` VALUES(760014, 'jsp.workflowConsole.goals.sequence', 'Last update @2012-02-27 12:36:44');
INSERT INTO `text_resource` VALUES(760015, 'jsp.workflowConsole.goals.task', 'Last update @2012-02-27 12:36:44');
INSERT INTO `text_resource` VALUES(760016, 'jsp.workflowConsole.goals.user', 'Last update @2012-02-27 12:36:45');
INSERT INTO `text_resource` VALUES(770000, 'jsp.yourcontent.pagetitle', 'Last update @2012-02-27 12:36:45');
INSERT INTO `text_resource` VALUES(770001, 'jsp.yourcontent.sponsors', 'Last update @2012-02-27 12:36:45');
INSERT INTO `text_resource` VALUES(770002, 'jsp.yourcontent.adminAnnounce', 'Last update @2012-02-27 12:36:45');
INSERT INTO `text_resource` VALUES(770003, 'jsp.yourcontent.bar.assignments', 'Last update @2012-02-27 12:36:46');
INSERT INTO `text_resource` VALUES(770004, 'jsp.yourcontent.deadline', 'Last update @2012-02-27 12:36:46');
INSERT INTO `text_resource` VALUES(10277, 'jsp.casePage.casePriority', 'Last update @2012-03-11 09:07:42');
INSERT INTO `text_resource` VALUES(10278, 'common.label.open', 'Last update @2012-03-11 10:41:01');
INSERT INTO `text_resource` VALUES(10279, 'common.label.closed', 'Last update @2012-03-11 10:41:01');
INSERT INTO `text_resource` VALUES(10264, 'common.js.token.hint', 'last updated @ 2012-03-11 16:11:44');
INSERT INTO `text_resource` VALUES(10265, 'common.js.token.noResult', 'last updated @ 2012-03-11 16:11:59');
INSERT INTO `text_resource` VALUES(10266, 'common.js.token.search', 'last updated @ 2012-03-11 16:12:12');
INSERT INTO `text_resource` VALUES(10267, 'common.label.you', 'last updated @ 2012-03-11 16:50:13');
INSERT INTO `text_resource` VALUES(10268, 'common.label.willdo', 'last updated @ 2012-03-11 16:50:40');
INSERT INTO `text_resource` VALUES(10269, 'common.btn.ok', 'last updated @ 2012-03-11 16:51:38');
INSERT INTO `text_resource` VALUES(10273, 'common.js.hint.role.list', 'last updated @ 2012-03-11 19:31:31');
INSERT INTO `text_resource` VALUES(10274, 'common.js.hint.team.list', 'last updated @ 2012-03-11 19:33:29');
INSERT INTO `text_resource` VALUES(10272, 'common.js.hint.user.list', 'last updated @ 2012-03-11 19:27:21');
INSERT INTO `text_resource` VALUES(10275, 'common.js.alert.title.prompt', 'last updated @ 2012-03-11 19:39:54');
INSERT INTO `text_resource` VALUES(10276, 'common.js.alert.title.alert', 'last updated @ 2012-03-11 19:40:47');
INSERT INTO `text_resource` VALUES(10280, 'common.js.alert.submitindicators', 'last updated @ 2012-03-12 00:05:39');
INSERT INTO `text_resource` VALUES(10281, 'common.btn.browse', 'last updated @ 2012-03-13 23:48:50');
INSERT INTO `text_resource` VALUES(10282, 'common.err.invalid.answer.value', 'last updated @ 2012-03-17 13:05:39');
INSERT INTO `text_resource` VALUES(10283, 'common.err.badparam.section', 'last updated @ 2012-03-17 16:19:55');
INSERT INTO `text_resource` VALUES(10284, 'common.err.badparam.source', 'last updated @ 2012-03-17 13:05:39');
INSERT INTO `text_resource` VALUES(10285, 'common.err.notfind.answer', 'last updated @ 2012-03-17 13:05:39');
INSERT INTO `text_resource` VALUES(10286, 'common.err.notfind.surveyindicator', 'last updated @ 2012-03-17 13:05:39');
INSERT INTO `text_resource` VALUES(10287, 'common.err.notfind.ref', 'last updated @ 2012-03-17 13:05:39');
INSERT INTO `text_resource` VALUES(2190003, 'jsp.footer.privacy', NULL);
INSERT INTO `text_resource` VALUES(2190004, 'common.js.peopleProfile.savePopup', NULL);
INSERT INTO `text_resource` VALUES(2190005, 'common.filter.clickopen', NULL);
INSERT INTO `text_resource` VALUES(2190006, 'common.filter.clickclose', NULL);
INSERT INTO `text_resource` VALUES(2190007, 'common.js.msg.privatediscussion', NULL);
INSERT INTO `text_resource` VALUES(2190008, 'jsp.scorecardNav.sendQuestions', NULL);
INSERT INTO `text_resource` VALUES(2190009, 'common.btn.completeAssignment', NULL);
INSERT INTO `text_resource` VALUES(2190010, 'common.btn.sendFeedback', NULL);
INSERT INTO `text_resource` VALUES(2190011, 'common.label.engageStatus', NULL);
INSERT INTO `text_resource` VALUES(2190012, 'common.label.taskStatus', NULL);
INSERT INTO `text_resource` VALUES(2190013, 'common.msg.taskstatus.done', NULL);
INSERT INTO `text_resource` VALUES(2190014, 'common.msg.taskstatus.inflight', NULL);
INSERT INTO `text_resource` VALUES(2190015, 'common.msg.taskstatus.inactive', NULL);
INSERT INTO `text_resource` VALUES(2190016, 'common.msg.taskstatus.overdue', NULL);
INSERT INTO `text_resource` VALUES(2190017, 'common.msg.taskstatus.forcedexit', NULL);
INSERT INTO `text_resource` VALUES(2190018, 'common.msg.taskstatus.unassigned', NULL);
INSERT INTO `text_resource` VALUES(2190019, 'common.msg.youroutbox', NULL);
INSERT INTO `text_resource` VALUES(2190020, 'common.btn.forward', NULL);
INSERT INTO `text_resource` VALUES(2190021, 'common.btn.delete', NULL);
INSERT INTO `text_resource` VALUES(2190022, 'common.btn.undelete', NULL);
INSERT INTO `text_resource` VALUES(2190023, 'jsp.replyMessage.forwardMessage', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `token`
--

CREATE TABLE `token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Tokens defined in this table is only for display to end user' AUTO_INCREMENT=17 ;

--
-- Dumping data for table `token`
--

INSERT INTO `token` VALUES(1, '<username>', 'Full name of the message recepient');
INSERT INTO `token` VALUES(2, '<projectname>', 'Name of the Project');
INSERT INTO `token` VALUES(3, '<productname>', 'Name of the Product');
INSERT INTO `token` VALUES(4, '<targetname>', 'Name of the Target');
INSERT INTO `token` VALUES(5, '<casename>', 'Name of the Case');
INSERT INTO `token` VALUES(6, '<casestatus>', 'Value of Case Status: open, closed, etc');
INSERT INTO `token` VALUES(7, '<caseid>', 'ID of the Case');
INSERT INTO `token` VALUES(8, '<taskduetime>', 'Due time of Assignment');
INSERT INTO `token` VALUES(9, '<daysbeforedue>', 'Number of days before assignment due time');
INSERT INTO `token` VALUES(10, '<daysafterdue>', 'Number of days after assignment due time');
INSERT INTO `token` VALUES(11, '<projectstarttime>', 'Start time of Project');
INSERT INTO `token` VALUES(12, '<goalname>', 'Name of Goal');
INSERT INTO `token` VALUES(13, '<taskname>', 'Name of Task');
INSERT INTO `token` VALUES(14, '<goalduetime>', 'Due time of the Goal');
INSERT INTO `token` VALUES(15, '<contenttitle>', 'Title of the content');
INSERT INTO `token` VALUES(16, '<projectadmin>', 'Full name of the project admin user');

-- --------------------------------------------------------

--
-- Table structure for table `tool`
--

CREATE TABLE `tool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT 'Technical name used by system code',
  `label` varchar(45) NOT NULL COMMENT 'Label is displayed to end users',
  `description` varchar(255) DEFAULT NULL,
  `access_matrix_id` int(11) DEFAULT NULL COMMENT 'ID of the access matrix that configures the tool',
  `action` varchar(32) DEFAULT NULL,
  `task_type` tinyint(4) NOT NULL,
  `bsc_compatible` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'whether the tool is BSC compatible',
  `multi_user` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'whether the tool supports multiple users working on the task at the same time',
  `purpose` text COMMENT 'text explaining the purpose of the tool',
  `inactive_reason` text COMMENT 'text explaining why the task cannot be submitted',
  `content_type` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_tool_config_matrix` (`access_matrix_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `tool`
--

INSERT INTO `tool` VALUES(3, 'journal review', 'Journal Review', 'Used for staff review of journal content', NULL, 'journalReview.do', 3, 0, 0, NULL, NULL, 2);
INSERT INTO `tool` VALUES(4, 'journal peer review', 'Journal Peer Review', 'Used for peer review of journal content', NULL, 'journalPeerReview.do', 4, 0, 1, NULL, NULL, 2);
INSERT INTO `tool` VALUES(6, 'survey create', 'Survey Creation', 'Used for creating survey content', NULL, 'surveyCreate.do', 6, 0, 0, NULL, NULL, 1);
INSERT INTO `tool` VALUES(7, 'survey edit', 'Survey Edit', 'Used for editing survey content', NULL, 'surveyEdit.do', 7, 0, 0, NULL, NULL, 1);
INSERT INTO `tool` VALUES(8, 'survey review', 'Survey Review', 'Used for staff review of survey content', NULL, 'surveyReview.do', 8, 0, 0, NULL, NULL, 1);
INSERT INTO `tool` VALUES(9, 'survey peer review', 'Survey Peer Review', 'Used for peer review of survey content', NULL, 'surveyPeerReview.do', 9, 0, 1, NULL, NULL, 1);
INSERT INTO `tool` VALUES(10, 'survey view', 'Survey Display', 'Used for display survey content', NULL, 'surveyView.do', 10, 0, 0, NULL, NULL, 1);
INSERT INTO `tool` VALUES(11, 'payment', 'Payment', 'Used for recording payment info', NULL, 'contentPayment.do', 11, 1, 0, NULL, NULL, 3);
INSERT INTO `tool` VALUES(12, 'approve', 'Approve', 'Used for recording approval info', NULL, 'contentApproval.do', 12, 1, 0, NULL, NULL, 3);
INSERT INTO `tool` VALUES(2, 'journal edit', 'Journal Edit', 'UI tool for editing journal content', NULL, 'journalEdit.do', 2, 0, 0, NULL, NULL, 2);
INSERT INTO `tool` VALUES(5, 'journal view', 'Journal Display', 'Used for display of journal content', NULL, 'journalView.do', 5, 0, 0, NULL, NULL, 2);
INSERT INTO `tool` VALUES(1, 'journal create', 'Journal Creation', 'UI tool for creating journal content', NULL, 'journalCreate.do', 1, 0, 0, NULL, NULL, 2);
INSERT INTO `tool` VALUES(13, 'journal pr review', 'Journal Peer-Review Review', 'Review of journal peer review', NULL, 'journalPRReviews.do', 13, 0, 0, NULL, NULL, 2);
INSERT INTO `tool` VALUES(14, 'survey pr review', 'Survey Peer-Review Review', 'Review of survey peer review', NULL, 'surveyPRReview.do', 14, 0, 0, NULL, NULL, 1);
INSERT INTO `tool` VALUES(15, 'start horse', 'Start Horse', 'Approve to start the workflow of a horse', NULL, 'horseApproval.do', 15, 1, 0, NULL, NULL, 3);
INSERT INTO `tool` VALUES(16, 'journal review response', 'Journal Review Response', 'Author uses this tool to respond to reviewer''s feedback', NULL, 'journalReviewResponse.do', 16, 0, 0, NULL, NULL, 2);
INSERT INTO `tool` VALUES(17, 'survey review response', 'Survey Review Response', 'Author uses this tool to respond to reviewer''s feedback', NULL, 'surveyReviewResponse.do', 17, 0, 0, NULL, NULL, 1);
INSERT INTO `tool` VALUES(18, 'journal overall review', 'Journal Overall Edit', 'Use this tool to do overall editing on journal content', NULL, 'journalOverallReview.do', 18, 0, 0, NULL, NULL, 2);
INSERT INTO `tool` VALUES(19, 'survey overall review', 'Survey Overall Edit', 'Use this tool to do overall editing on survey content', NULL, 'surveyOverallReview.do', 19, 0, 0, NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tool_intl`
--

CREATE TABLE `tool_intl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tool_id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `purpose` text NOT NULL,
  `inactive_reason` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_toolintl` (`tool_id`,`language_id`),
  KEY `fk_toolintl_tool` (`tool_id`),
  KEY `fk_toolintl_lang` (`language_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `tool_intl`
--


-- --------------------------------------------------------

--
-- Table structure for table `ttags`
--

CREATE TABLE `ttags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `term_UNIQUE` (`term`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Predefined target tags' AUTO_INCREMENT=6 ;

--
-- Dumping data for table `ttags`
--

INSERT INTO `ttags` VALUES(1, 'Asia', '');
INSERT INTO `ttags` VALUES(2, 'Europe', '');
INSERT INTO `ttags` VALUES(3, 'North America', '');
INSERT INTO `ttags` VALUES(4, 'South America', '');
INSERT INTO `ttags` VALUES(5, 'Africa', '');

-- --------------------------------------------------------

--
-- Table structure for table `upload_file`
--

CREATE TABLE `upload_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL COMMENT 'Real filename stored',
  `file_path` varchar(255) NOT NULL COMMENT 'Path to the attached file in the file system after uploaded',
  `display_name` varchar(255) NOT NULL COMMENT 'Name of the upload file',
  `size` int(11) NOT NULL DEFAULT '0' COMMENT 'file size (bytes)',
  `type` varchar(8) NOT NULL COMMENT 'file type (suffix, i.e. txt, doc, pdf, etc.)',
  `note` varchar(255) DEFAULT NULL COMMENT 'Any notes to be attached',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'the file upload time',
  `user_id` int(11) DEFAULT '0' COMMENT 'user who uploaded the file',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_file_name` (`file_name`),
  KEY `fk_upload_file_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='defines upload files.' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `upload_file`
--


-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(20) NOT NULL COMMENT 'clear text password for easy management',
  `last_password_change_time` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `last_logout_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'account status.\n0 - inactive\n1 - active\n2 - deleted',
  `timezone` int(11) DEFAULT NULL,
  `language_id` int(11) DEFAULT NULL,
  `msgboard_id` int(11) DEFAULT NULL,
  `forward_inbox_msg` tinyint(1) DEFAULT '1',
  `number_msgs_per_screen` int(11) DEFAULT '10',
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `cell_phone` varchar(45) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `bio` text,
  `photo` varchar(255) DEFAULT NULL COMMENT 'file path of user''s photo',
  `location` varchar(255) DEFAULT NULL COMMENT 'List of role IDs to whom the profile is visible. ',
  `email_detail_level` tinyint(4) DEFAULT '1' COMMENT '0 - Alert only\n1 - Full message',
  `default_project_id` int(11) DEFAULT '-1',
  `last_login_time` datetime DEFAULT NULL,
  `site_admin` tinyint(4) DEFAULT '0',
  `organization_id` int(11) NOT NULL DEFAULT '1' COMMENT 'The organization the user belongs to',
  `privacy_policy_accept_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `fk_user_language` (`language_id`),
  KEY `fk_user_messageboard` (`msgboard_id`),
  KEY `fk_user_org` (`organization_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='The user account structure' AUTO_INCREMENT=33 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES(1, 'admin', 'admin@gi.org', 'test', NULL, '2010-06-10 14:40:18', NULL, 1, 0, 1, 0, 1, 10, 'Adam', 'Boss', '123-456-7890', '123-456-7890', NULL, 'I am the system admin.', NULL, '123 Kellie Jean Ct, Great Falls, VA 22066', 1, -1, NULL, 1, 1, NULL);
INSERT INTO `user` VALUES(2, 'editor1', 'nsmith@gi.org', 'test', NULL, '2010-06-11 11:44:41', NULL, 1, -5, 1, NULL, 1, 10, 'Nancy', 'Smith', '703-743-3334', '202-743-3334', NULL, 'Nancy is an experienced editor.', 'upload_files/user_2.jpg', 'Washington', 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(3, 'reviewer1', 'reviewer1@gi.org', 'test', NULL, '2010-06-11 15:34:01', '2012-10-25 13:10:55', 1, 0, 1, 11, 0, 10, 'Lucy', 'Small', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 0, 1, '2012-10-26 10:39:24', 0, 1, NULL);
INSERT INTO `user` VALUES(4, 'reviewer2', 'reviewer2@gi.org', 'test', NULL, '2010-06-11 15:35:23', NULL, 1, 0, 1, 12, 0, 10, 'Jimmy', 'Regan', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(5, 'manager1', 'manager1@gi.org', 'test', NULL, '2010-06-11 15:36:27', '2012-10-26 10:33:46', 1, 0, 1, 0, 0, 10, 'Chris', 'Willows', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 1, 1, '2012-10-25 13:10:59', 0, 1, NULL);
INSERT INTO `user` VALUES(6, 'manager2', 'manager2@gi.org', 'test', NULL, '2010-06-11 15:37:13', NULL, 1, 0, 1, NULL, 0, 10, 'Sam', 'Lynch', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(7, 'support1', 'support1@gi.org', 'test', NULL, '2010-06-11 15:38:05', NULL, 1, 0, 1, NULL, 0, 10, 'Sean', 'Park', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(8, 'support2', 'support2@gi.org', 'test', NULL, '2010-06-11 15:38:45', NULL, 1, 0, 1, NULL, 0, 10, 'Leo', 'Leonsis', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(9, 'researcher1.us', 'researcher1.us@gi.org', 'test', NULL, '2010-06-11 15:39:34', '2012-10-26 10:39:16', 1, 0, 1, 6, 0, 10, 'John', 'Smith', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 1, 1, '2012-10-26 10:33:57', 0, 1, NULL);
INSERT INTO `user` VALUES(10, 'researcher1.ar', 'researcher1.ar@gi.org', 'test', NULL, '2010-06-11 15:40:27', NULL, 1, NULL, 1, 8, 0, 10, 'David', 'Delpotro', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(11, 'researcher1.bz', 'researcher1.bz@gi.org', 'test', NULL, '2010-06-11 15:41:53', NULL, 1, NULL, 1, 7, 0, 10, 'Fernando', 'Gangzalaz', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(12, 'researcher1.cn', 'researcher1.cn@gi.org', 'test', NULL, '2010-06-11 15:43:07', NULL, 1, NULL, 1, 9, 0, 10, 'Hua', 'Wang', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(13, 'reporter1.ar', 'reporter1.ar@gi.org', 'test', NULL, '2010-06-11 15:44:20', NULL, 1, NULL, 1, 3, 0, 10, 'Jeff', 'Julio', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(14, 'reporter1.bz', 'reporter1.bz@gi.org', 'test', NULL, '2010-06-11 15:45:43', NULL, 1, NULL, 1, 4, 0, 10, 'Frank', 'Ferry', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(15, 'reporter1.cn', 'reporter1.cn@gi.org', 'test', NULL, '2010-06-11 15:46:32', NULL, 1, NULL, 1, 5, 0, 10, 'Meng', 'Li', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(16, 'reporter1.us', 'reporter1.us@gi.org', 'test', NULL, '2010-06-11 15:48:30', '2012-10-23 11:37:10', 1, 0, 1, 2, 0, 10, 'Betty', 'Young', '123-456-7890', '123-456-7890', NULL, NULL, NULL, NULL, 1, 1, '2012-10-23 11:36:14', 0, 1, NULL);
INSERT INTO `user` VALUES(17, 'pr02', 'pr2@xyz.com', 'test', NULL, '2010-06-11 15:49:29', NULL, 1, NULL, 1, NULL, 0, 10, 'Victor', 'Bush', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(18, 'pr03', 'pr3@xyz.com', 'test', NULL, '2010-06-11 15:50:42', NULL, 1, NULL, 1, NULL, 0, 10, 'Kate', 'Ford', '888-888-8888', '888-888-8888', NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(19, 'pr04', 'pr4@xyz.com', 'test', NULL, '2010-06-11 15:52:52', NULL, 1, NULL, 1, NULL, 0, 10, 'Kyle', 'Washington', NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(20, 'editor2', 'editor2@gi.org', 'test', NULL, '2010-06-11 15:53:28', NULL, 1, NULL, 1, NULL, 1, 10, 'Mindy', 'Clark', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(21, 'pr01', 'pr1@xyz.com', 'test', NULL, '2010-06-11 15:54:42', NULL, 1, 0, 1, NULL, 0, 10, 'Mike', 'Carter', '888-888-8888', '888-888-8888', NULL, 'Outstanding performance', NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(22, 'reporter1.uk', 'jbrown@abc.com', 'test', NULL, '2010-06-13 18:32:00', NULL, 1, -5, 1, NULL, 1, 10, 'Jeff', 'Brown', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(23, 'researcher1.uk', 'jcharles@abc.com', 'test', NULL, '2010-06-13 18:32:56', NULL, 1, -5, 1, NULL, 1, 10, 'Jay', 'Charles', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(24, 'reporter1.tw', 'kliu@tu.org', 'test', NULL, '2010-06-13 18:34:10', NULL, 1, -5, 2, NULL, 1, 10, 'Kevin', 'Liu', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(25, 'researcher1.tw', 'awang@tu.edu', 'test', NULL, '2010-06-13 18:35:07', NULL, 1, -5, 2, NULL, 1, 10, 'Ann', 'Wang', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(32, 'DELETED-32', '', '', NULL, '2010-06-15 17:05:19', NULL, 2, -5, 1, NULL, 0, 10, '', '', '', '', '', '', '', '', 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(31, 'DELETED-31', '', '', NULL, '2010-06-15 16:04:26', NULL, 2, -5, 1, NULL, 0, 10, '', '', '', '', '', '', '', '', 0, -1, NULL, 0, 1, NULL);
INSERT INTO `user` VALUES(0, 'system', '', '', NULL, '0000-00-00 00:00:00', NULL, 1, NULL, 1, NULL, 0, 10, 'Indaba', 'System', NULL, NULL, NULL, NULL, NULL, NULL, 1, -1, NULL, 0, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `userfinder`
--

CREATE TABLE `userfinder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `project_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `assigned_user_id` int(11) NOT NULL,
  `case_subject` text NOT NULL,
  `case_body` text NOT NULL,
  `case_priority` tinyint(4) NOT NULL,
  `attach_user` tinyint(1) NOT NULL,
  `attach_content` tinyint(1) NOT NULL,
  `status` tinyint(4) NOT NULL COMMENT '1 = active;\n2 = inactive;\n3 = deleted;',
  `create_time` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `delete_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_uf_projrole` (`project_id`,`role_id`),
  KEY `fk_uf_project` (`project_id`),
  KEY `fk_uf_role` (`role_id`),
  KEY `fk_uf_assigned` (`assigned_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table contains userfinder triggers' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `userfinder`
--


-- --------------------------------------------------------

--
-- Table structure for table `userfinder_event`
--

CREATE TABLE `userfinder_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userfinder_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT 'User who has completed all assignments',
  `cases_id` int(11) NOT NULL COMMENT 'Case that was opened',
  `exe_time` datetime NOT NULL COMMENT 'when the even was fired',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ufe_event` (`userfinder_id`,`user_id`),
  KEY `fk_ufe_uf` (`userfinder_id`),
  KEY `fk_ufe_user` (`user_id`),
  KEY `fk_ufe_case` (`cases_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table keeps fired userfinder events' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `userfinder_event`
--


-- --------------------------------------------------------

--
-- Table structure for table `view_matrix`
--

CREATE TABLE `view_matrix` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `default_value` tinyint(4) NOT NULL COMMENT '0 - none\n1 - limited\n2 - full',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Controls what roles can see what other roles'' profile' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `view_matrix`
--

INSERT INTO `view_matrix` VALUES(1, 'Standard', 'The standard view permissions', 2);

-- --------------------------------------------------------

--
-- Table structure for table `view_permission`
--

CREATE TABLE `view_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `view_matrix_id` int(11) NOT NULL,
  `subject_role_id` int(11) NOT NULL,
  `target_role_id` int(11) NOT NULL,
  `permission` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 - none\n1 - limited\n2 - full',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_vst` (`view_matrix_id`,`subject_role_id`,`target_role_id`),
  KEY `fk_view_permission_view_matrix1` (`view_matrix_id`),
  KEY `fk_view_permission_role1` (`subject_role_id`),
  KEY `fk_view_permission_role2` (`target_role_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

--
-- Dumping data for table `view_permission`
--

INSERT INTO `view_permission` VALUES(1, 1, 2, 2, 3);
INSERT INTO `view_permission` VALUES(3, 1, 2, 3, 3);
INSERT INTO `view_permission` VALUES(4, 1, 2, 4, 3);
INSERT INTO `view_permission` VALUES(5, 1, 2, 5, 3);
INSERT INTO `view_permission` VALUES(6, 1, 2, 6, 3);
INSERT INTO `view_permission` VALUES(7, 1, 2, 7, 3);
INSERT INTO `view_permission` VALUES(8, 1, 2, 8, 3);
INSERT INTO `view_permission` VALUES(9, 1, 2, 9, 3);
INSERT INTO `view_permission` VALUES(14, 1, 5, 7, 0);
INSERT INTO `view_permission` VALUES(15, 1, 5, 6, 0);
INSERT INTO `view_permission` VALUES(16, 1, 5, 5, 0);
INSERT INTO `view_permission` VALUES(19, 1, 6, 5, 0);
INSERT INTO `view_permission` VALUES(20, 1, 6, 6, 0);
INSERT INTO `view_permission` VALUES(21, 1, 6, 7, 0);
INSERT INTO `view_permission` VALUES(22, 1, 7, 7, 0);
INSERT INTO `view_permission` VALUES(23, 1, 7, 6, 0);
INSERT INTO `view_permission` VALUES(24, 1, 7, 5, 0);

-- --------------------------------------------------------

--
-- Table structure for table `workflow`
--

CREATE TABLE `workflow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_time` datetime NOT NULL,
  `created_by_user_id` int(11) NOT NULL,
  `total_duration` int(11) DEFAULT NULL COMMENT 'Total length (number of days) for the workflow to complete ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_workflow_user1` (`created_by_user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='Structure of workflow definition' AUTO_INCREMENT=4 ;

--
-- Dumping data for table `workflow`
--

INSERT INTO `workflow` VALUES(1, 'Notebook Workflow 2010', 'Workflow for notebooks', '2010-06-11 13:54:49', 1, 62);
INSERT INTO `workflow` VALUES(2, 'Scorecard 2010 Workflow', 'Workflow for scorecard 2010', '2010-06-14 16:58:45', 1, 60);
INSERT INTO `workflow` VALUES(3, 'Energy Policy 2010 Workflow', 'Workflow for Energy Policy Content - used by both EPA and EPS', '2010-06-15 11:36:41', 1, 38);

-- --------------------------------------------------------

--
-- Table structure for table `workflow_object`
--

CREATE TABLE `workflow_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workflow_id` int(11) NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `status` int(11) NOT NULL COMMENT '1 - initial\n2 - running\n3 - completed\n4 - stopped\n5 - cancelled',
  `orig_status` int(11) NOT NULL DEFAULT '0',
  `is_cancelled` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'whether the horse if cancelled. Cancelled horse is not published.',
  PRIMARY KEY (`id`),
  KEY `fk_wfi_workflow` (`workflow_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `workflow_object`
--

INSERT INTO `workflow_object` VALUES(1, 1, '2010-06-16 10:22:55', 2, 0, 0);
INSERT INTO `workflow_object` VALUES(2, 2, '2010-06-16 10:33:42', 2, 0, 0);
INSERT INTO `workflow_object` VALUES(3, 1, '2010-06-16 10:33:49', 2, 0, 0);
INSERT INTO `workflow_object` VALUES(4, 1, '2010-06-16 10:33:49', 2, 0, 0);
INSERT INTO `workflow_object` VALUES(5, 1, '2010-06-16 10:33:49', 2, 0, 0);
INSERT INTO `workflow_object` VALUES(6, 2, '2010-06-16 10:33:55', 2, 0, 0);
INSERT INTO `workflow_object` VALUES(7, 2, '2010-06-16 10:33:55', 2, 0, 0);
INSERT INTO `workflow_object` VALUES(8, 2, '2010-06-16 10:33:55', 2, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `workflow_sequence`
--

CREATE TABLE `workflow_sequence` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'sequence starts from 1 for each workflow',
  `workflow_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_workflow_sequence_workflow1` (`workflow_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='each workflow can have multiple sequence' AUTO_INCREMENT=12 ;

--
-- Dumping data for table `workflow_sequence`
--

INSERT INTO `workflow_sequence` VALUES(1, 1, 'Main', 'The main sequence of the notebook workflow');
INSERT INTO `workflow_sequence` VALUES(2, 2, 'Main', 'The main sequence');
INSERT INTO `workflow_sequence` VALUES(4, 2, 'Pay1', '1st payment');
INSERT INTO `workflow_sequence` VALUES(5, 2, 'Pay2', '2nd payment');
INSERT INTO `workflow_sequence` VALUES(6, 2, 'PR Edit', 'Edit peer reviews');
INSERT INTO `workflow_sequence` VALUES(7, 2, 'Edit', 'Content edit');
INSERT INTO `workflow_sequence` VALUES(8, 2, 'Finish', 'Finishing up');
INSERT INTO `workflow_sequence` VALUES(11, 3, 'Main', 'The main sequence');
