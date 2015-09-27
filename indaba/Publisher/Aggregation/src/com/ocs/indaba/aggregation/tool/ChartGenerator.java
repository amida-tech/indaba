/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.vo.ChartVO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeanbone
 */
public abstract class ChartGenerator {

    private XMLOutputter outputter;
    private Document document;
    protected String caption;
    protected JSONObject jsonObj;
    protected Double[] values;

    protected ChartGenerator(String caption) {
        init(caption);
    }

    private void init(String caption) {
        this.outputter = new XMLOutputter();
        this.caption = caption;
        this.document = new Document(new Element("chart"));
        Element root = document.getRootElement();
        root.setAttribute("showBorder", "0");
        root.setAttribute("bgColor", "FFFFFF");
        root.setAttribute("canvasBorderThickness", "0");
        root.setAttribute("canvasBorderColor", "FFFFFF");
        root.setAttribute("divLineAlpha", "0");
        root.setAttribute("showPlotBorder", "0");
        root.setAttribute("showAlternateHGridColor", "0");
        root.setAttribute("showValues", "0");
        root.setAttribute("plotGradientColor", "");
        root.setAttribute("chartLeftMargin", "5");
        root.setAttribute("chartRightMargin", "5");
        root.setAttribute("chartTopMargin", "5");
        root.setAttribute("chartBottomMargin", "5");
        root.setAttribute("caption", caption);
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Element getRoot() {
        return document.getRootElement();
    }

    public String getOutputString() {
        try {
            return URLEncoder.encode(outputter.outputString(document), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(ChartGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public abstract void fillChart(ChartVO chart);
}
