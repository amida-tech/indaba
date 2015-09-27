/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AnswerTypeChoice;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.vo.AnswerTypeChoiceDef;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author seanpcheng
 */
public class AnswerTypeChoiceDAO extends SmartDaoMySqlImpl<AnswerTypeChoice, Integer>{
    
    @Autowired
    private AtcChoiceDAO atcChoiceDao = null;

    public AnswerTypeChoiceDef getDefinition(int answerTypeChoiceId, int languageId) {
        List<AtcChoice> choices = atcChoiceDao.getChoices(answerTypeChoiceId, languageId);
        AnswerTypeChoiceDef def = new AnswerTypeChoiceDef();
        def.setAnswerTypeId(answerTypeChoiceId);
        def.setChoices(choices);
        return def;
    }
    
}
