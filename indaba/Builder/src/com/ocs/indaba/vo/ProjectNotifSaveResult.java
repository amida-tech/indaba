/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.ProjectNotif;

/**
 *
 * @author yc06x
 */
public class ProjectNotifSaveResult {

    static public final short RESULT_CODE_OK = 0;
    static public final short RESULT_CODE_CONFLICT = 1;
    static public final short RESULT_CODE_ERROR = 2;

    private short code;
    private ProjectNotif notif;

    public void setResultCode(short code) {
        this.code = code;
    }

    public short getResultCode() {
        return this.code;
    }

    public void setProjectNotif(ProjectNotif notif) {
        this.notif = notif;
    }

    public ProjectNotif getProjectNotif() {
        return this.notif;
    }

}
