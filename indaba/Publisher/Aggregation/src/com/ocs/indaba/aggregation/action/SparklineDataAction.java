/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.json.SparklineDataJsonUtils;
import com.ocs.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
public class SparklineDataAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(SparklineDataAction.class);
    private static final String PARAM_HORSE_ID = "horseId";
    private static final String PARAM_PRODUCT_ID = "productId";

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
        int productId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID), -1);;
        String horseIdStr = request.getParameter(PARAM_HORSE_ID);
        List<Integer> ids = parseProductIds(horseIdStr);
        log.info("Get sparkline data. [productId=" + productId + ", productId=" + horseIdStr + ", version=" + version + "]: " + ids);
     
        JSONObject jsonObj = SparklineDataJsonUtils.toJson(productId, ids);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
           out.write(jsonObj.toJSONString());
        } finally {
            out.close();
        }
        return null;
    }
    
    private List<Integer> parseProductIds(String productIdStr) {
         if(productIdStr == null) {
            return null;
        }
        String[] idArr = productIdStr.split("\\+");
        if(idArr == null) {
            return null;
        }
        List<Integer> ids = new ArrayList<Integer>();
        for(String s: idArr) {
            try{
                ids.add(Integer.parseInt(s.trim()));
            } catch(Exception ex) {
                log.error("Invalid id: " + s);
            }
        }
        return ids;
     }
}
