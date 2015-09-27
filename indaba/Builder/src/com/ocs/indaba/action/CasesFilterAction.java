/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Ctags;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.vo.CaseInfo;
import com.ocs.indaba.vo.LoginUser;
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
public class CasesFilterAction extends BaseAction {
    private CaseService caseService = null;
    

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        LoginUser loginUser = preprocess(mapping, request, response);

        List<Integer> targetFilter = new ArrayList<Integer>();
        List<Integer> productFilter = new ArrayList<Integer>();
        List<Integer> statusFilter = new ArrayList<Integer>();
        List<Integer> cTagFilter = new ArrayList<Integer>();
        fillFilters(request, targetFilter, productFilter, statusFilter, cTagFilter);
        List<CaseInfo> list = caseService.searchCasesByFilter(targetFilter, productFilter,
                statusFilter, cTagFilter, loginUser.getPrjid());
        if (list != null && list.size() > 0) {
            response.getWriter().write(outputList(request, list));
        }
        return null;
    }

    private void fillFilters(HttpServletRequest request, List<Integer> targetFilter,
            List<Integer> productFilter, List<Integer> statusFilter,
            List<Integer> cTagFilter) {
        fillFilter(targetFilter, request.getParameter("targetFilter").split(","));
        fillFilter(productFilter, request.getParameter("productFilter").split(","));
        fillFilter(statusFilter, request.getParameter("statusFilter").split(","));
        fillFilter(cTagFilter, request.getParameter("cTagFilter").split(","));
    }

    private void fillFilter(List<Integer> filter, String[] paramArray) {
        for (String param : paramArray) {
            try {
                filter.add(Integer.parseInt(param));
            } catch (Exception ex) {
            }
        }
    }

    private String outputList(HttpServletRequest request, List<CaseInfo> list) {
//        boolean isCasePermitted, isContentPermitted;
        StringBuilder sb = new StringBuilder();
        sb.append("<table id=\"casesList\" width=\"100%\" border=\"0\" ").
                append("cellspacing=\"0\" cellpadding=\"0\"><thead><tr class=\"thead\">").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_CASE)).append(" #</th>").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_TITLE)).append("</th>").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_STATUS)).append("</th>").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_PRIORITY)).append("</th>").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_TAGS)).append("</th>").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_OWNER)).append("</th>").
                append("<th>").append(getMessage(request, Messages.KEY_COMMON_TH_ATTACHEDCONTNET)).append("</th>").
                append("</tr></thead><tbody>");

//        isCasePermitted = accessPermissionService.isPermitted(prjid, uid, "see all cases");
//        isContentPermitted = accessPermissionService.isPermitted(prjid, uid, "see all content");

//        boolean canManageAllUsers = accessPermissionService.isPermitted(prjid, uid, "manage all users");
        for (CaseInfo caseInfo : list) {
            sb.append("<tr><td>");
//            if (isCasePermitted) {
            sb.append("<a href=\"casedetail.do?caseid=").
                    append(caseInfo.getCaseId()).
                    append("\">#").
                    append(caseInfo.getCaseId()).
                    append("</a>");
//            } else {
//                sb.append("#").append(caseInfo.getCaseId());
//            }
            sb.append("</td><td>").append("<a href=\"casedetail.do?caseid=").append(caseInfo.getCaseId()).append("\">").append(caseInfo.getTitle()).append("</a></td>");
            sb.append("<td>").append(caseInfo.getStatus()).append("</td>");
            sb.append("<td>").append(caseInfo.getPriority()).append("</td><td>");

            List<Ctags> tags = caseInfo.getTags();
            if (tags != null) {
                for (Ctags tag : tags) {
                    sb.append(tag.getTerm()).append(", ");
                }
            }
            if (!tags.isEmpty()) {
                sb.setLength(sb.length() - 2);
            }
//            if (canManageAllUsers) {
            sb.append("</td><td><a href=\"profile.do?targetUid=").
                    append(caseInfo.getAssignedUserId()).
                    append("\">").
                    append(caseInfo.getOwner()).
                    append("</a></td><td>");
//            } else {
//                sb.append("</td><td>")
//                  .append(caseInfo.getAssignedUserName())
//                  .append("</td><td>");
//            }

            List<ContentHeader> contentHeaderList = caseInfo.getAttachContents();
//            if (isContentPermitted) {
            if (contentHeaderList != null) {
                for (ContentHeader contentHeader : contentHeaderList) {
                    if (contentHeader.getContentType() == 1) {
                        sb.append("<a href=\"notebook.do?action=display&horseid=").
                                append(contentHeader.getHorseId()).
                                append("\">").
                                append(contentHeader.getTitle()).append("</a>, ");
                    } else {
                        sb.append("<a href=\"surveyDisplay.do?action=display&horseid=").
                                append(contentHeader.getHorseId()).
                                append("\">").
                                append(contentHeader.getTitle()).append("</a>, ");
                    }
                }
            }
//            } else {
//                for (ContentHeader contentHeader : contentHeaderList) {
//                    sb.append(contentHeader.getTitle()).append(",");
//                }
//            }
            if (!contentHeaderList.isEmpty()) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("</td></tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
