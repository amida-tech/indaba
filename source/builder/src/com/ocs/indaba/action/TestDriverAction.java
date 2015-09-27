/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.dao.AnswerTypeTableDAO;
import com.ocs.indaba.po.AnswerTypeTable;
import com.ocs.indaba.survey.table.Block;
import com.ocs.indaba.survey.table.SurveyTableService;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class TestDriverAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyEditAction.class);
    private static final String PARAM_TEST = "t";

    @Autowired
    private AnswerTypeTableDAO attDao = null;

    @Autowired
    private SurveyTableService stSrv = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String test = request.getParameter(PARAM_TEST);

        if (test == null) return null;
        else if (test.equalsIgnoreCase("tj")) {
            String data = createTableJson();
            super.writeMsgUTF8(response, data);
            return null;
        } else if (test.equalsIgnoreCase("ftj")) {
            int typeId = StringUtils.str2int(request.getParameter("att"));
            AnswerTypeTable att = attDao.get(typeId);
            String jsonStr = att.getData();
            List<List<Block>> blocks = Block.fromJson(jsonStr);
            String encoded = Block.toJson(blocks);
            super.writeMsgUTF8(response, encoded);
            return null;
        } else if (test.equalsIgnoreCase("html")) {
            int qstId = StringUtils.str2int(request.getParameter("qst"));
            int horseId = StringUtils.str2int(request.getParameter("horse"));
            int langId = StringUtils.str2int(request.getParameter("lang"), 1);
            String dis = request.getParameter("dis");
            boolean disabled = (dis != null);
            String html = stSrv.generateTableHtml(horseId, qstId, 1, langId, disabled);
            super.writeMsgUTF8(response, html);
            return null;
        }

        return null;
    }


    private String createTableJson() {
        List<List<Block>> grid = new ArrayList<List<Block>>();

        // row 0
        List<Block> row = new ArrayList<Block>();
        grid.add(row);

        Block block;
        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_LABEL, 0, 0);
        block.setData("");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_REGION, Block.BLOCK_DATA_TYPE_HEADER, 0, 1);
        block.setData("Numeric");
        block.setColSpan(2);
        block.setRowSpan(1);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_REGION, Block.BLOCK_DATA_TYPE_HEADER, 0, 3);
        block.setData("Choices");
        block.setColSpan(2);
        block.setRowSpan(1);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 0, 5);
        block.setData("");
        row.add(block);

        // Row 1
        row = new ArrayList<Block>();
        grid.add(row);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_LABEL, 1, 0);
        block.setData("");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 1, 1);
        block.setData("Int");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 1, 2);
        block.setData("Float");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 1, 3);
        block.setData("Single");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 1, 4);
        block.setData("Multiple");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 1, 5);
        block.setData("Text");
        row.add(block);

        // Row 2
        row = new ArrayList<Block>();
        grid.add(row);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 2, 0);
        block.setData("Year One");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 2, 1);
        block.setIndicatorId(32);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 2, 2);
        block.setIndicatorId(33);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 2, 3);
        block.setIndicatorId(38);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 2, 4);
        block.setIndicatorId(39);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 2, 5);
        block.setIndicatorId(44);
        row.add(block);

        // Row 3
        row = new ArrayList<Block>();
        grid.add(row);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 3, 0);
        block.setData("Year Two");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 3, 1);
        block.setIndicatorId(34);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 3, 2);
        block.setIndicatorId(35);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 3, 3);
        block.setIndicatorId(40);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 3, 4);
        block.setIndicatorId(41);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 3, 5);
        block.setIndicatorId(45);
        row.add(block);

        // Row 4
        row = new ArrayList<Block>();
        grid.add(row);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_HEADER, 4, 0);
        block.setData("Year Three");
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 4, 1);
        block.setIndicatorId(36);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 4, 2);
        block.setIndicatorId(37);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 4, 3);
        block.setIndicatorId(42);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 4, 4);
        block.setIndicatorId(43);
        row.add(block);

        block = new Block(Block.BLOCK_TYPE_CELL, Block.BLOCK_DATA_TYPE_QUESTION, 4, 5);
        block.setIndicatorId(46);
        row.add(block);

        return Block.toJson(grid);
    }

}
