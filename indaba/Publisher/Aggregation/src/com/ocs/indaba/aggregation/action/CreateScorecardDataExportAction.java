/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.common.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ocs.indaba.aggregation.service.ProductService;
import com.ocs.indaba.aggregation.vo.ProductForExportVO;
import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.LanguageService;
import com.ocs.indaba.service.UserService;
import com.ocs.util.StringUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class CreateScorecardDataExportAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CreateScorecardDataExportAction.class);
    //private static final String ATTR_ERRMSG = "errmsg";
    private static final String ATTR_PRODUCTS = "products";
    private static final String ATTR_LANGUAGES = "languages";
    private static final String FWD_VIEW = "view";
    private static final String ATTR_ORGS = "orgs";
    private static final String PARAM_FILTER_ORG_ID = "orgid";
    private static final String PARAM_SORT_NAME = "sortname";
    private static final String PARAM_SORT_TYPE = "sorttype";
    private static final String DEFAULT_SORT_NAME = "prod";
    private static final String DEFAULT_SORT_TYPE = "asc";
    //service
    private UserService userService = null;
    private ProductService productService = null;

    @Autowired
    private LanguageService langService = null;

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            logger.info("User session is invalid. Redirect to login page!");
            return actionFwd;
        }
        return handleRequest(mapping, request);
    }

    private ActionForward handleRequest(ActionMapping mapping, HttpServletRequest request) {
        if (!exportScorecardReportVisible) {
            return noAccess(mapping, request);
        }

        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortType = request.getParameter(PARAM_SORT_TYPE);
        int filterOrgId = StringUtils.str2int(request.getParameter(PARAM_FILTER_ORG_ID));
        if (StringUtils.isEmpty(sortName)) {
            sortName = DEFAULT_SORT_NAME;
        }
        if (StringUtils.isEmpty(sortType)) {
            sortType = DEFAULT_SORT_TYPE;
        }
        request.setAttribute(PARAM_FILTER_ORG_ID, filterOrgId);
        request.setAttribute(PARAM_SORT_NAME, sortName);
        request.setAttribute(PARAM_SORT_TYPE, sortType.toLowerCase());
        User user = userService.getUser(uid);

        logger.debug("Request Parameter: userId=" + uid + ", sortName=" + sortName + ", sortType=" + sortType);

        OrgAuthorizer orgAuth = new OrgAuthorizer(user);
        logger.debug("Request Parameter: userId=" + uid + ", sortName=" + sortName + ", sortType=" + sortType);

        List<ProductForExportVO> products = productService.getAccessibleSurveyProducts(orgAuth, filterOrgId, sortName, sortType);
       
        request.setAttribute(ATTR_PRODUCTS, products);

        request.setAttribute(ATTR_ORGS, orgAuth.getAccessibleOrgList(Constants.VISIBILITY_PRIVATE));

        request.setAttribute(ATTR_LANGUAGES, langService.getAllLanguages());

        return mapping.findForward(FWD_VIEW);
    }

    

    @Autowired
    public void setUserService(UserService userSrvc) {
        this.userService = userSrvc;
    }


    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
