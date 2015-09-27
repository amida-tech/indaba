/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.service.ProductService;
import com.ocs.indaba.service.TargetService;
import com.ocs.indaba.util.SpringContextUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author Jeff Jiang
 */
public class FilterTagHandler extends BaseTagHandler {

    private static final Logger logger = Logger.getLogger(FilterTagHandler.class);
    private static final String FILTER_PRODUCT = "product";
    private static final String FILTER_TARGET = "target";
    private static final String FILTER_STATUS = "status";
    protected ProductService prdService = null;
    protected TargetService trgtService = null;
    /*
     * attribute for access
     */
    private String name = "";
    private int[] selectedIds = null;
    private boolean include = false;
    private int prjid = 0;
    private int status = 0;

    @Override
    public int doStartTag() {
        logger.debug("Filter Tag: name=" + name + ", include=" + include + ", selectedIds=" + selectedIds + ", status=" + status + ", prjid=" + prjid);
        if (FILTER_PRODUCT.equals(name)) {
            handleProduct();
        } else if (FILTER_TARGET.equals(name)) {
            handleTarget();
        } else if (FILTER_STATUS.equals(name)) {
            handleStatus();
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return Tag.SKIP_BODY;
    }

    private void handleProduct() {
        prdService = (ProductService) SpringContextUtil.getBean("productService");
        List<Product> products = null;
        if (prjid > 0) {
            products = prdService.getProductsByProjectId(prjid);
        } else {
            products = prdService.getAllProducts();
        }
        Map<Integer, String> prodMap = new HashMap<Integer, String>();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<div class='filter'>");
            out.println("<h4>" + getI18nMessage(Messages.KEY_COMMON_MSG_PRODUCT) + "</h4>");
            out.println("<select name='" + name + "' id='product' style='width: 150px; font-family: Arial, Helvetica;'>");
            if (products != null) {
                for (Product prod : products) {
                    boolean found = false;
                    prodMap.put(prod.getId(), prod.getName());
                    if (selectedIds != null) {
                        for (int i : selectedIds) {
                            if (i == prod.getId()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        out.println("<option value='" + prod.getId() + "'>" + prod.getName() + "</option>");
                    }
                }
            }
            out.println("</select>");
            out.println("<button onclick='return false;' class='small button blue icon-add'>" + getI18nMessage(Messages.KEY_COMMON_BUTTON_ADD) + "</button> =&gt;");
            out.println("<div id='product-selected-filters' class='selectedFilters'>");
            out.println("<div id='selectedList' style='display: none;'>");
            if (selectedIds != null) {
                for (int i : selectedIds) {
                    out.println("<input id='product_id_" + i + "' name='productId' value='" + i + "' checked='checked' type='checkbox'/>");
                }
            }
            out.println("</div>");
            if (selectedIds != null) {
                for (int i : selectedIds) {
                    out.println("<span class='removeSelection' value='" + i + "' key='" + prodMap.get(i) + "' field='product'><a title='remove'></a>");
                    out.println("<label>" + prodMap.get(i) + "</label></span>");
                }
            }
            out.println("</div>");
            out.println("<div class='clear'></div>");
            out.println("<div>");
            out.println("<label><input id='allExcpectProd' type='radio' name='prodSelectionType' value='0' " + ((include) ? "" : "checked='true'") + "/>" + getI18nMessage(Messages.KEY_COMMON_CHOICE_EXCLUDE) + "</label>");
            out.println("<label><input id='onlySelectedProd' type='radio' name='prodSelectionType' value='1' " + ((include) ? "checked='true'" : "") + "/>" + getI18nMessage(Messages.KEY_COMMON_CHOICE_INCLUDE) + "</label>");
            out.println("</div>");
            outputProductArrayInJS(out, products);
            out.println("</div>");
            out.flush();
        } catch (IOException ex) {
            logger.error("write to page error when doStartTag" + ex);
        }
    }

    private void handleTarget() {
        trgtService = (TargetService) SpringContextUtil.getBean("targetService");
        List<Target> targets = null;
        if (prjid > 0) {
            targets = trgtService.getTargetsByProjectId(prjid);
        } else {
            trgtService.getAllTargets();
        }
        Map<Integer, String> targetMap = new HashMap<Integer, String>();
        JspWriter out = pageContext.getOut();
        try {
            out.println("<div class='filter'>");
            out.println("<h4>" + getI18nMessage(Messages.KEY_COMMON_MSG_TARGET) + "</h4>");
            out.println("<select name='" + name + "' id='target' style='width: 150px; font-family: Arial, Helvetica; '>");
            if (targets != null) {
                for (Target target : targets) {
                    boolean found = false;
                    targetMap.put(target.getId(), target.getShortName());
                    if (selectedIds != null) {
                        for (int i : selectedIds) {
                            if (i == target.getId()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        out.println("<option value='" + target.getId() + "'>" + target.getShortName() + "</option>");
                    }
                }
            }
            out.println("</select>");
            out.println("<button onclick='return false;' class='small button blue icon-add'>" + getI18nMessage(Messages.KEY_COMMON_BUTTON_ADD) + "</button> =&gt;");
            out.println("<div id='content-selected-filters' class='selectedFilters'>");
            out.println("<div id='selectedList' style='display: none;'>");
            if (selectedIds != null) {
                for (int i : selectedIds) {
                    out.println("<input id='target_id_" + i + "' name='targetId' value='" + i + "' checked='checked' type='checkbox' />");
                }
            }
            out.println("</div>");
            if (selectedIds != null) {
                for (int i : selectedIds) {
                    out.println("<span class='removeSelection' value='" + i + "' key='" + targetMap.get(i) + "' field='target'><a title='remove'></a>");
                    out.println("<label>" + targetMap.get(i) + "</label></span>");
                }
            }
            out.println("</div>");
            out.println("<div class='clear'></div>");
            out.println("<div>");
            out.println("<label><input id='allExcpectTarget' type='radio' name='targetSelectionType' value='0' " + ((include) ? "" : "checked='true'") + "/>" + getI18nMessage(Messages.KEY_COMMON_CHOICE_EXCLUDE) + "</label>");
            out.println("<label><input id='onlySelectedTarget' type='radio' name='targetSelectionType' value='1' " + ((include) ? "checked='true'" : "") + "/>" + getI18nMessage(Messages.KEY_COMMON_CHOICE_INCLUDE) + "</label>");
            out.println("</div>");
            outputTargetArrayInJS(out, targets);
            out.println("</div>");
            out.flush();
        } catch (IOException ex) {
            logger.error("write to page error when doStartTag" + ex);

        }
    }

    private void handleStatus() {
        JspWriter out = pageContext.getOut();
        try {
            out.println("<div class='filter'>");
            out.println("<h4>" + getI18nMessage(Messages.KEY_COMMON_MSG_STATUS) + "</h4>");
            out.println("<div>");
            out.println("<label><input id='allStatus' type='radio' name='status' value='0' " + ((status == 0) ? "checked='true'" : "") + "/>" + getI18nMessage(Messages.KEY_COMMON_CHOICE_ALL) + "</label>");
            out.println("<label><input id='notOverdueStatus' type='radio' name='status' value='1' " + ((status == 1) ? "checked='true'" : "") + "/>" + getI18nMessage(Messages.KEY_COMMON_CHOICE_NOTOVERDUE) + "</label>");
            out.println("<label><input id='overdueStatus' type='radio' name='status' value='2' " + ((status == 2) ? "checked='true'" : "") + "/>" + getI18nMessage(Messages.KEY_COMMON_CHOICE_OVERDUE) + "</label>");
            out.println("</div>");
            out.flush();
        } catch (IOException ex) {
            logger.error("write to page error when doStartTag" + ex);

        }
    }

    private void outputTargetArrayInJS(JspWriter out, List<Target> targets) {
        try {
            out.println("<script type='text/javascript'>");
            /*
             * out.println("var targets = new Array();"); if (targets != null) {
             * for (int i = 0, len = targets.size(); i < len; ++i) { Target
             * target = targets.get(i); out.println("\ttargets[i] = new
             * Array();"); out.println("\ttargets[i][0] = " + target.getId() +
             * ";"); out.println("\ttargets[i][1] = '" + target.getName() +
             * "';"); } }
             */
            out.print("var obj_target = {");
            if (targets != null) {
                for (int i = 0, len = targets.size(); i < len; ++i) {
                    Target target = targets.get(i);
                    String targetName = StringEscapeUtils.escapeJavaScript(target.getName());
                    out.print("'" + targetName + "':" + target.getId());
                    if (i != len - 1) {
                        out.print(", ");
                    }
                }
            }
            out.println("};");
            out.println("</script>");
            out.flush();
        } catch (IOException ex) {
            logger.error("write to page error when doStartTag" + ex);

        }
    }

    private void outputProductArrayInJS(JspWriter out, List<Product> products) {
        try {
            out.println("<script type='text/javascript'>");
            /*
             * out.println("var products = new Array();"); if (products != null)
             * { for (int i = 0, len = products.size(); i < len; ++i) { Product
             * prod = products.get(i); out.print("\tproducts[i] = new
             * Array();"); out.print("\tproducts[i][0] = " + prod.getId() +
             * ";"); out.print("\tproducts[i][1] = " + prod.getName() + ";"); }
             * }
             */
            out.print("var obj_product = {");
            if (products != null) {
                for (int i = 0, len = products.size(); i < len; ++i) {
                    Product product = products.get(i);
                    String prodName = StringEscapeUtils.escapeJavaScript(product.getName());
                    out.print("'" + prodName + "':" + product.getId());
                    if (i != len - 1) {
                        out.print(", ");
                    }
                }
            }
            out.println("};");
            out.println("</script>");
            out.flush();
        } catch (IOException ex) {
            logger.error("write to page error when doStartTag" + ex);

        }
    }

    /**
     * @param tool the tool to set
     */
    public void setName(Object name) {
        try {
            this.name = (String) ExpressionEvaluatorManager.evaluate("name", name.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set tool value error" + ex);
        }
    }

    /**
     * @param link the link to set
     */
    public void setSelectedIds(Object selectedIds) {
        try {
            this.selectedIds = null;
            String selectedIdsStr = null;
            if (selectedIds != null) {
                selectedIdsStr = (String) ExpressionEvaluatorManager.evaluate("selectedIds", selectedIds.toString(), String.class, this, pageContext);
            }
            logger.debug("SELECTED STRING: " + selectedIds);
            if (selectedIdsStr != null && (selectedIdsStr = selectedIdsStr.trim()).length() > 0) {
                String[] ids = selectedIdsStr.split(",");
                if (ids != null) {
                    this.selectedIds = new int[ids.length];
                    for (int i = 0; i < ids.length; ++i) {
                        if ((ids[i] = ids[i].trim()).length() > 0) {
                            this.selectedIds[i] = Integer.parseInt(ids[i]);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("set selected ids value error" + ex);
        }
    }

    /**
     * @param text the text to set
     */
    public void setStatus(Object status) {
        try {
            if (status != null) {
                this.status = (Integer) ExpressionEvaluatorManager.evaluate("status", status.toString(), Integer.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set text value error" + ex);
        }
    }

    /**
     * @param prjid the project id
     */
    public void setPrjid(Object prjid) {
        try {
            if (prjid != null) {
                this.prjid = (Integer) ExpressionEvaluatorManager.evaluate("prjid", prjid.toString(), Integer.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set text value error" + ex);
        }
    }

    /**
     * @param prjid the prjid to set
     */
    public void setInclude(Object include) {
        try {
            if (include != null && "1".equals(include.toString())) {
                this.include = true;
            } else {
                this.include = false;
            }
        } catch (Exception ex) {
            logger.error("set prjid value error", ex);
        }
    }
}
