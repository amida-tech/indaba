package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.JournalConfig;
import java.util.List;
import org.apache.log4j.Logger;

public class JournalConfigDAO extends SmartDaoMySqlImpl<JournalConfig, Integer> {

    private static final Logger log = Logger.getLogger(JournalConfigDAO.class);
    private static final String GET_JOURNAL_INSTRUCTION_BY_ID =
            "select * from journal_config where id ="
            + "(select journal_config_id from journal_content_object where content_header_id ="
            + "( select content_header_id from horse where id = ?))";
private static final String SELECT_ALL_JOURNAL_CONGIGS =
            "SELECT * FROM journal_config ORDER BY name";

    public List<JournalConfig> selectAllJournalConfigs() {
        return super.find(SELECT_ALL_JOURNAL_CONGIGS);
    }
    public String getInstructionbyID(int id) {
        log.debug(GET_JOURNAL_INSTRUCTION_BY_ID + id);
        JournalConfig temp = (JournalConfig) super.findSingle(GET_JOURNAL_INSTRUCTION_BY_ID, id);
        if (temp == null) {
            return "No Instructions";
        }
        return temp.getInstructions();
    }

    public JournalConfig getJournalConfigbyHorseid(int id) {
        log.debug(GET_JOURNAL_INSTRUCTION_BY_ID + id);
        return super.findSingle(GET_JOURNAL_INSTRUCTION_BY_ID, id);
    }
}
