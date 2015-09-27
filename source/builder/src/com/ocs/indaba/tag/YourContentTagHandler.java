/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.indaba.vo.ActiveHorseView;
import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff Jiang
 */
public class YourContentTagHandler extends BaseHorseContentTagHandler {

    private static final Logger logger = Logger.getLogger(YourContentTagHandler.class);
    private HorseService horseService = (HorseService) SpringContextUtil.getBean("horseService");

    @Override
    public int doStartTag() {
        logger.debug("########################### <indaba:yourcontent> Tag Start###########################");
        logger.debug("Your assignments: [projectId=" + prjid + ", userId=" + uid + "].");
        JspWriter out = pageContext.getOut();
        List<ActiveHorseView> activeHorses = horseService.getActiveHorsesOfUser(uid, prjid);
        if (activeHorses != null && activeHorses.size() > 0) {
            try {
                out.print(" <tr class='thead'>");
                out.print("<th width='250px'></th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_STATUS) + "</th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_VIEW) + "</th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_HISTORY) + "</th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_GOAL) + "</th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_ESTIMATE) + "</th>");
                out.print("</tr>");

                for (int i = 0, n = activeHorses.size(); i < n; ++i) {
                    outputHorse(out, activeHorses.get(i));
                    if (i < n - 1) { // output delimitor line
                        out.println("<tr><td colspan='5'><hr/></td></tr>");
                    }
                }
                //   outputTask(out, task);

            } catch (IOException ex) {
                logger.error("Error Occurs", ex);
            }
        } else {
            try {
                out.print("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getI18nMessage(Messages.KEY_COMMON_MSG_NODATA));
            } catch (IOException ex) {
                logger.error("Error Occurs", ex);
            }
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:yourcontent> Tag End ###########################");
        return Tag.EVAL_PAGE;
    }

    public void setBodyContent() {
    }
}

