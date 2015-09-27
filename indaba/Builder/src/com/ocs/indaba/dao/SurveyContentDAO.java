/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyContentObject;
import org.apache.log4j.Logger;

/**
 *
 * @author sjf
 */
public class SurveyContentDAO extends SmartDaoMySqlImpl<SurveyContentObject, Integer> {

    private static final Logger logger = Logger.getLogger(SurveyContentDAO.class);
    private static final String GET_CONTENT_OBJECT = "select * from survey_content_object where content_header_id = ?";

    public SurveyContentObject getSurveyContentObjectbyContentHeaderId(int contentHeaderIds) {
        return this.findSingle(GET_CONTENT_OBJECT,contentHeaderIds);
    }
}
