/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.json.HorizontalScorecardSubCategorySummaryJsonUtils;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class HorizontalScorecardSubCategorySummaryAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(HorizontalScorecardSubCategorySummaryAction.class);
    private static final String PARAM_PRODUCT_ID = "productId";
    private static final String PARAM_SUBCAT_ID = "subcatId";

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        prehandle(mapping, request);
        productId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID), -1);
        int subcatId = StringUtils.str2int(request.getParameter(PARAM_SUBCAT_ID), -1);
        log.info("Get horizontal scorcard sub-category summary. [productId=" + productId + ", subcatId=" + subcatId + ", version=" + version + "]");

        ProductInfo productInfo = getProductInfo(productId);

        if (productInfo != null) {
            JSONObject jsonObj = HorizontalScorecardSubCategorySummaryJsonUtils.toJson(productId, subcatId, productInfo);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                out.write(jsonObj.toJSONString());
            } finally {
                out.close();
            }
        }
        return null;
    }
}
