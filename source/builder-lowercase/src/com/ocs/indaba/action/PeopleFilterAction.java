/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Cases;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.Team;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTaskDisplay;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.UserDisplay;
import com.ocs.indaba.vo.UserListInfo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class PeopleFilterAction extends BaseAction {

    private UserService userService;
    private ViewPermissionService viewPermisssionService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);
        ActionData actionData = new ActionData();
        fillFilters(request, actionData);
        List<UserListInfo> list = userService.getAllUserInfoListByFilter(loginUser.getPrjid(), actionData.getTargetFilter(), actionData.getProductFilter(),
                actionData.getRoleFilter(), actionData.getTeamFilter(), actionData.getHasCaseFilter(), actionData.getStatusFilter());
        if (list.size() > 0) {
            response.getWriter().write(outputList(request, loginUser, list));
        }
        return null;
    }

    public String outputList(HttpServletRequest request, LoginUser loginUser, List<UserListInfo> list) {
        UserDisplay userDisplay;
        StringBuilder sb = new StringBuilder();
        sb.append("<table id=\"userInfoList\" style=\"width:100%\"><thead><tr class=\"thead\">").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_NAME)).append("</th><th>").
                append(getMessage(request, Messages.KEY_COMMON_TH_ROLE)).append("</th><th>").
                append(getMessage(request, Messages.KEY_COMMON_TH_COUNTRY)).append("</th><th>").
                append(getMessage(request, Messages.KEY_COMMON_TH_TEAMS)).append("</th><th nowrap>").
                append(getMessage(request, Messages.KEY_COMMON_TH_OPENCASES)).append("</th><th>").
                append(getMessage(request, Messages.KEY_COMMON_TH_ASSIGNEDCONTENT)).append("</th></tr></thead><tbody>");
        if (list != null) {
            for (int i = 0, size = list.size(); i < size; ++i) {
                UserListInfo userListInfo = list.get(i);
                userDisplay = viewPermisssionService.getUserDisplayOfProject(loginUser.getPrjid(),
                        Constants.DEFAULT_VIEW_MATRIX_ID, loginUser.getUid(), userListInfo.getId());
                if (userDisplay.getPermission() == 0) {
                    sb.append("<tr><td>").append(userDisplay.getDisplayUsername()).append("</td>");
                } else {
                    sb.append("<tr><td><a href=\"profile.do?targetUid=").append(userDisplay.getUserId()).append("\">").append(userDisplay.getDisplayUsername()).append("</a></td>");
                }
                sb.append("<td>").append(userListInfo.getRole()).append("</td><td>");

                if (userListInfo.getTargets() != null) {
                    for (Target tar : userListInfo.getTargets()) {
                        sb.append(tar.getName()).append(", ");
                    }
                }
                if (sb.charAt(sb.length() - 2) == ',') {
                    sb.setLength(sb.length() - 2);
                }
                sb.append("</td><td>");

                if (userListInfo.getTeams() != null) {
                    for (Team team : userListInfo.getTeams()) {
                        sb.append(team.getTeamName()).append(", ");
                    }
                }
                if (sb.charAt(sb.length() - 2) == ',') {
                    sb.setLength(sb.length() - 2);
                }
                sb.append("</td><td>");

                if (userListInfo.getOpenCases() != null) {
                    for (Cases cases : userListInfo.getOpenCases()) {
                        sb.append("<a href=\"casedetail.do?caseid=").append(cases.getId()).append("\"># ").append(cases.getId()).append("</a>, ");
                    }
                }
                if (sb.charAt(sb.length() - 2) == ',') {
                    sb.setLength(sb.length() - 2);
                }
                sb.append("</td><td>");

                if (userListInfo.getAssignedTaskDisplay() != null) {
                    for (AssignedTaskDisplay atd : userListInfo.getAssignedTaskDisplay()) {
                        if (atd.getHorseId() == -1) {
                            sb.append(atd.getContent()).append(", ");
                        } else {
                            if (atd.getContentType() == Constants.CONTENT_TYPE_JOURNAL) {
                                sb.append("<a href=\"notebook.do?action=display&horseid=").append(atd.getHorseId()).append("\">").append(atd.getContent()).append("</a>, ");
                            } else if (atd.getContentType() == Constants.CONTENT_TYPE_SURVEY) {
                                sb.append("<a href=\"surveyDisplay.do?action=display&horseid=").append(atd.getHorseId()).append("\">").append(atd.getContent()).append("</a>, ");
                            }
                        }
                    }
                }
                if (sb.charAt(sb.length() - 2) == ',') {
                    sb.setLength(sb.length() - 2);
                }
                sb.append("</td></tr>");
            }
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    private void fillFilters(HttpServletRequest request, ActionData actionData) {
        fillFilter(actionData.getTargetFilter(), request.getParameter("targetFilter").split(","));
        fillFilter(actionData.getProductFilter(), request.getParameter("productFilter").split(","));
        fillFilter(actionData.getRoleFilter(), request.getParameter("roleFilter").split(","));
        fillFilter(actionData.getTeamFilter(), request.getParameter("teamFilter").split(","));
        actionData.setHasCaseFilter(StringUtils.str2int(request.getParameter("hasCaseFilter")));
        actionData.setStatusFilter(StringUtils.str2int(request.getParameter("statusFilter")));
    }

    private void fillFilter(List<Integer> filter, String[] paramArray) {
        for (String param : paramArray) {
            filter.add(Integer.parseInt(param));
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermisssionService) {
        this.viewPermisssionService = viewPermisssionService;
    }

    private class ActionData {

        private List<Integer> targetFilter = new ArrayList<Integer>();
        private List<Integer> productFilter = new ArrayList<Integer>();
        private List<Integer> roleFilter = new ArrayList<Integer>();
        private List<Integer> teamFilter = new ArrayList<Integer>();
        private Integer hasCaseFilter;
        private Integer statusFilter;

        public Integer getHasCaseFilter() {
            return hasCaseFilter;
        }

        public void setHasCaseFilter(Integer hasCaseFilter) {
            this.hasCaseFilter = hasCaseFilter;
        }

        public List<Integer> getProductFilter() {
            return productFilter;
        }

        public void setProductFilter(List<Integer> productFilter) {
            this.productFilter = productFilter;
        }

        public List<Integer> getRoleFilter() {
            return roleFilter;
        }

        public void setRoleFilter(List<Integer> roleFilter) {
            this.roleFilter = roleFilter;
        }

        public Integer getStatusFilter() {
            return statusFilter;
        }

        public void setStatusFilter(Integer statusFilter) {
            this.statusFilter = statusFilter;
        }

        public List<Integer> getTargetFilter() {
            return targetFilter;
        }

        public void setTargetFilter(List<Integer> targetFilter) {
            this.targetFilter = targetFilter;
        }

        public List<Integer> getTeamFilter() {
            return teamFilter;
        }

        public void setTeamFilter(List<Integer> teamFilter) {
            this.teamFilter = teamFilter;
        }
    }
}
