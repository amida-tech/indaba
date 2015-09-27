/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.exporter;

import com.ocs.indaba.aggregation.dao.IdefDAO;
import com.ocs.indaba.aggregation.service.ScorecardService;
import com.ocs.indaba.builder.dao.ProductDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class BaseExporter {

    protected IdefDAO idefDao = null;
    protected ProductDAO productDao = null;
    protected ScorecardService scorecardService = null;

    protected static final String NULL = "null";

    private String error;
    private List<String> msgs = new ArrayList<String>();
    private String expFileName;
    private String metaFileName;

    protected void addMsg(String msg) {
        Date d = new Date();
        msgs.add(d.toString() + ": " + msg);
    }

    protected void setError(String msg) {
        this.error = msg;
    }

    public String getError() {
        return error;
    }

    public List<String> getMsgs() {
        return msgs;
    }

    protected void setExportFileName(String name) {
        this.expFileName = name;
    }

    public String getExportFileName() {
        return this.expFileName;
    }

    protected void setMetaFileName(String name) {
        this.metaFileName = name;
    }

    public String getMetaFileName() {
        return this.metaFileName;
    }


}
