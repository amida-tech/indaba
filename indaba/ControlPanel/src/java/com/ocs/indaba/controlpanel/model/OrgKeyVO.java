/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;
import com.ocs.util.ValueObject;
import org.apache.log4j.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 *
 * @author rick
 */
public class OrgKeyVO extends ValueObject {
    private static final Logger logger = Logger.getLogger(OrgKeyVO.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String[] statusMap = new String[]{"","Normal", "Revoked"};
    
    private int id;

    private int version;
    
    private int validDays;

    private String hashAlgorithm;

    private String issueTime;

    private String effectiveTime;
    
    private String expiryTime;

    private String status;

    private String data;

    private short statusId;
    
    public OrgKeyVO(Integer id, int version, String hashAlgorithm, Date issueTime, 
            Date effectiveTime, int validDays, short status, String data) {
        this.id = id;
        this.version = version;
        this.hashAlgorithm = hashAlgorithm;
        this.issueTime = dateFormat.format(issueTime);
        this.effectiveTime = dateFormat.format(effectiveTime);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(effectiveTime);
        calendar.add(Calendar.DAY_OF_MONTH, validDays);
        this.expiryTime = dateFormat.format(calendar.getTime());
        this.statusId = status;
        this.status = statusMap[status];
        this.data = data;
        this.validDays = validDays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getValidDays() {
        return this.validDays;
    }
    public void setValidDays(Integer days) {
        this.validDays = days;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime.toString();
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime.toString();
    }
    
    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime.toString();
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public short getStatusId() {
        return statusId;
    }

    public void setStatusId(short status) {
        this.statusId = status;
    }
}
