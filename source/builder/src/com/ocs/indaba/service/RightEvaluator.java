/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Project;

/**
 *
 * @author yc06x
 */
public interface RightEvaluator {
    
    static public final int YES = Constants.ACCESS_PERMISSION_YES;
    static public final int NO = Constants.ACCESS_PERMISSION_NO;
    static public final int UNDEFINED = Constants.ACCESS_PERMISSION_UNDIFINED;

    public int evaluate(String rightName, Project project, int userId, int userRoleId);

}
