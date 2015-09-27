-- phpMyAdmin SQL Dump
-- version 2.10.2
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: May 15, 2011 at 11:40 AM
-- Server version: 5.0.41
-- PHP Version: 5.2.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `indaba_publisher`
-- 

-- 
-- Dumping data for table `aggr_method`
-- 

INSERT INTO `aggr_method` (`name`, `description`, `module_name`) VALUES ('average', 'average', 'com.ocs.indaba.aggregation.method.AverageMethod');
INSERT INTO `aggr_method` (`name`, `description`, `module_name`) VALUES ('max', 'max', 'com.ocs.indaba.aggregation.method.MaxMethod');
INSERT INTO `aggr_method` (`name`, `description`, `module_name`) VALUES ('min', 'min', 'com.ocs.indaba.aggregation.method.MinMethod');
INSERT INTO `aggr_method` (`name`, `description`, `module_name`) VALUES ('median', 'median', 'com.ocs.indaba.aggregation.method.MedianMethod');
INSERT INTO `aggr_method` (`name`, `description`, `module_name`) VALUES ('weightedAverage', 'weightedAverage', 'com.ocs.indaba.aggregation.method.WeightAverageMethod');

-- 
-- Dumping data for table `config`
-- 

INSERT INTO `config` VALUES (1, 'Indaba Publisher', 'Introduction to Indaba Publisher.\r\n', 'otis_value_b', 'scorecard_b', 'scorecard_answer_b', 'tds_value_b', 'otis_value_a', 'scorecard_a', 'scorecard_answer_a', 'tds_value_a', null);

-- 
-- Dumping data for table `datapoint`
-- 


-- 
-- Dumping data for table `dataset`
-- 


-- 
-- Dumping data for table `dp_member`
-- 


-- 
-- Dumping data for table `export_log`
-- 


-- 
-- Dumping data for table `exp_study_period`
-- 


-- 
-- Dumping data for table `exp_target`
-- 


-- 
-- Dumping data for table `otis_value`
-- 


-- 
-- Dumping data for table `scorecard`
-- 


-- 
-- Dumping data for table `scorecard_answer`
-- 


-- 
-- Dumping data for table `tds_value`
-- 


-- 
-- Dumping data for table `widget`
-- 

INSERT INTO `widget` VALUES (1, 'journal_display', 'Journal Display', 'This widget is used to display the content of configured journals.', 'OCS', 1, '1.0', 1, 'widgets/journalDisplay.html?includeReviews=1', 'widgets/image/journal_display_widget_icon.png', 'horseId', 1, 1);
INSERT INTO `widget` VALUES (2, 'journal_display', 'Journal Display without Review', 'This widget is used to display the content of configured journals without review.', 'OCS', 1, '1.0', 1, 'widgets/journalDisplay.html', 'widgets/image/journal_display_widget_no_review_icon.png', 'horseId', 1, 1);
INSERT INTO `widget` VALUES (3, 'vertical_scorecard', 'Vertical Scorecard', 'This widget is used to show content of configured scorecards.', 'OCS', 1, '1.0', 1, 'widgets/vcardDisplay.html', 'widgets/image/vertical_scorecard_widget_icon.png', 'horseId', 2, 1);
INSERT INTO `widget` VALUES (4, 'horizontal_scorecard', 'Horizontal Scorecard', 'This widget is used to show the target comparison data of a configured survey product.', 'OCS', 1, '1.0', 1, 'widgets/hcardDisplay.html', 'widgets/image/horizontal_scorecard_widget_icon.png', 'productId', 2, 2);
INSERT INTO `widget` VALUES (5, 'indicator_summary', 'Indicator Summary', 'This widget is used to show the comparison chart of a selected target against other targets in the same study on multiple aspects. ', 'OCS', 1, '1.0', 1, 'dataSummary.jsp', 'widgets/image/data_summary_widget_icon.png', 'horseId', 2, 1);
INSERT INTO `widget` VALUES (6, 'data_summary', 'Survey Data Summary', 'This widget is used to compare the major aspects of a selected target against the other targets in the same study. ', 'OCS', 1, '1.0', 1, 'indicatorSummary.jsp', 'widgets/image/indicator_summary_widget_icon.png', 'horseId', 2, 1);
INSERT INTO `widget` VALUES (7, 'sparkline', 'Indicator Sparkline', 'This widget is used to show the performance of a selected target against the median of other targets in the same study.', 'OCS', 1, '1.0', 1, 'sparkline.jsp', 'widgets/image/sparkline_widget_icon.png', 'productId,horseId', 2, 3);
INSERT INTO `widget` VALUES (8, 'vertical_scorecard(for RWI)', 'vertical_scorecard(for RWI)', 'This widget is used to show content of configured scorecards for RWI ONLY.', 'OCS', 1, '1.0', 1, 'widgets/vcardDisplay4RWI.html', 'widgets/image/vertical_scorecard_widget_for_RWI_icon.png', 'horseId', 2, 1);

-- 
-- Dumping data for table `workset`
-- 


-- 
-- Dumping data for table `ws_indicator_instance`
-- 


-- 
-- Dumping data for table `ws_project`
-- 


-- 
-- Dumping data for table `ws_puser`
-- 


-- 
-- Dumping data for table `ws_target`
-- 

