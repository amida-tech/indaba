/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

/**
 *
 * @author rick
 */
public class RecordConstants {
    public static final String[] TYPE = new String[] {"N/A", "Job Record", "User Record", "Vendor Record"};
    public static final String[] STATUS = new String[] {"N/A", "Active", "Draft", "Deleted"};
    public static final String[] VISIBILITY = new String[]{"N/A", "Creator Only", "Account Only", "Vendor and Customer"};
    public static final int TYPE_JOB_RECORD = 1;
    public static final int TYPE_USER_RECORD = 2;
    public static final int TYPE_VENDOR_RECORD = 3;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_DRAFT = 2;
    public static final int STATUS_DELETED = 3;
    public static final int VISIBILITY_CREATOR_ONLY = 1;
    public static final int VISIBILITY_ACCOUNT_ONLY = 2;
    public static final int VISIBILITY_VENDOR_AND_CUSTOMER = 3;
}
