/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.AnswerObjectChoiceDAO;
import com.ocs.indaba.dao.AnswerObjectFloatDAO;
import com.ocs.indaba.dao.AnswerObjectIntegerDAO;
import com.ocs.indaba.dao.AnswerObjectTextDAO;
import com.ocs.indaba.dao.AtcChoiceDAO;
import com.ocs.indaba.po.AnswerObjectChoice;
import com.ocs.indaba.po.AnswerObjectFloat;
import com.ocs.indaba.po.AnswerObjectInteger;
import com.ocs.indaba.po.AnswerObjectText;
import com.ocs.indaba.po.AtcChoice;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sjf
 */
public class AnswerObjectService {

    private static final Logger logger = Logger.getLogger(AnswerObjectService.class);
    private AnswerObjectChoiceDAO answerObjectChoiceDAO = null;
    private AnswerObjectFloatDAO answerObjectFloatDAO = null;
    private AnswerObjectIntegerDAO answerObjectIntegerDAO = null;
    private AnswerObjectTextDAO answerObjectTextDAO = null;
    private AtcChoiceDAO atcChoiceDAO = null;

    public String getAnswerObjectby(int answerObjectId, int answerType, int answerTypeId) {
        String result = "";
        switch (answerType) {
            case 1://single choice
            case 2://multi choice
                AnswerObjectChoice answerObjectChoice = answerObjectChoiceDAO.getAnswerObjectChoicebyId(answerObjectId);
                if(answerObjectChoice == null)
                    break;
                List<AtcChoice> atcChoices = atcChoiceDAO.getAtcChoicesbyId(answerTypeId);
                long value = answerObjectChoice.getChoices();
                long mask = -1;
                
                if (atcChoices != null) {
                    for (AtcChoice atcchoice : atcChoices) {
                        mask = atcchoice.getMask();
                        if ((value & mask) == mask) {
                            if (result.length() != 0) {
                                result += ", " + atcchoice.getLabel();
                            } else {
                                result += atcchoice.getLabel();
                            }
                        }
                    }
                }
                break;
            case 3://integer
                AnswerObjectInteger answerObjectInteger = answerObjectIntegerDAO.getAnswerObjectIntegerbyId(answerObjectId);
                if (answerObjectInteger != null) {
                    result = "" + answerObjectInteger.getValue();
                }
                break;
            case 4://float
                AnswerObjectFloat answerObjectFloat = answerObjectFloatDAO.getAnswerObjectFloatbyId(answerObjectId);
                if (answerObjectFloat != null) {
                    result = "" + answerObjectFloat.getValue();
                }
                break;
            case 5://text
                AnswerObjectText answerObjectText = answerObjectTextDAO.getAnswerObjectTextbyId(answerObjectId);
                if (answerObjectText != null) {
                    result = answerObjectText.getValue();
                }
                break;
        }
        if (result.length() == 0) {
            result = "No Answer";
        }
        return result;
    }

    @Autowired
    public void setAnswerObjectTextDAO(AnswerObjectTextDAO answerObjectTextDAO) {
        this.answerObjectTextDAO = answerObjectTextDAO;
    }

    @Autowired
    public void setAnswerObjectChoiceDAO(AnswerObjectChoiceDAO answerObjectChoiceDAO) {
        this.answerObjectChoiceDAO = answerObjectChoiceDAO;
    }

    @Autowired
    public void setAnswerObjectIntegerDAO(AnswerObjectIntegerDAO answerObjectIntegerDAO) {
        this.answerObjectIntegerDAO = answerObjectIntegerDAO;
    }

    @Autowired
    public void setAnswerObjectFloatDAO(AnswerObjectFloatDAO answerObjectFloatDAO) {
        this.answerObjectFloatDAO = answerObjectFloatDAO;
    }

    @Autowired
    public void setAtcChoiceDAO(AtcChoiceDAO atcChoiceDAO) {
        this.atcChoiceDAO = atcChoiceDAO;
    }
}
