/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.po.SourceFile;
import com.ocs.indaba.service.TextResourceService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.I18nJsonUtils;
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
 * @author Jeff
 */
public class SourceFileAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SourceFileAction.class);
    private static final String PARAM_OPER = "oper";
    //private static final String PARAM_OPER_LIST = "list";
    //private static final String PARAM_OPER_SEARCH = "search";
    private static final String PARAM_SEARCH_FIELD = "searchField";
    private static final String PARAM_SEARCH_STRING = "searchString";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_ROWS = "rows";
    private static final String PARAM_SORT_INDEX = "sidx";
    private static final String PARAM_SORT_ORDER = "sord";
    private static final int DEFAULT_ROWS = 20;
    private TextResourceService txtResourceSrvc;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String action = request.getParameter(PARAM_OPER);
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGE), 1);
        int rows = StringUtils.str2int(request.getParameter(PARAM_ROWS), DEFAULT_ROWS);
        String sortIndex = request.getParameter(PARAM_SORT_INDEX);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        String searchField = request.getParameter(PARAM_SEARCH_FIELD);
        String searchString = request.getParameter(PARAM_SEARCH_STRING);
        logger.debug("Get list of source files:"
                + "\n\toper=" + action
                + "\n\trows=" + rows
                + "\n\tpage=" + page
                + "\n\tsortIndex=" + sortIndex
                + "\n\tsortOrder=" + sortOrder
                + "\n\tsearchField=" + searchField
                + "\n\tsearchString=" + searchString);
        try {
            List<SourceFile> sourceFiles = (StringUtils.isEmpty(searchField) || StringUtils.isEmpty(searchString))
                    ? txtResourceSrvc.getActiveSourceFiles()
                    : txtResourceSrvc.searchSourceFilesByFilenameLike(searchString);

            super.writeMsgUTF8(response, I18nJsonUtils.srcFileToJQGridJson(sourceFiles,
                    txtResourceSrvc.getTextResourceCountsMap(), page, rows, sortIndex, sortOrder));
        } catch (Exception ex) {
            logger.error("error occurs!", ex);
        }
        return null;
    }

    @Autowired
    public void setTextResourceService(TextResourceService txtResourceSrvc) {
        this.txtResourceSrvc = txtResourceSrvc;
    }
}
