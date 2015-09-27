/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.TargetService;
import java.lang.reflect.Method;
import java.text.MessageFormat;
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
public class AutoExportChartsAction extends BaseAction {

    private TargetService targetService;

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        StringBuffer sb = request.getRequestURL();
        int rootLength = sb.length() - request.getServletPath().length();
        sb.setLength(rootLength);

        String dataSummaryTemp = sb.append("/dataSummary.jsp?horseId={0}&includeLogo=0&version=1").toString();
        String indicatorSummaryTemp = sb.replace(rootLength, rootLength + 5, "/indicator").toString();
        String sparklineTemp = sb.replace(rootLength, sb.length(), "/sparkline.jsp?productId={0}&horseId={1}&includeLogo=0&version=1").toString();
        String dataSummaryUrl, indicatorSummaryUrl, sparklineUrl;
        int curProdId = -1;
        List<String> urlList = new ArrayList<String>();

        List<Integer[]> prdTgtIds = targetService.getAllProductAndTargetId();
        for (Integer[] horse : prdTgtIds) {
            dataSummaryUrl = MessageFormat.format(dataSummaryTemp, horse[0]);
            indicatorSummaryUrl = MessageFormat.format(indicatorSummaryTemp, horse[0]);
            sparklineUrl = MessageFormat.format(sparklineTemp, horse[1], horse[2]);
            urlList.add(dataSummaryUrl);
            urlList.add(dataSummaryUrl + "&size=large");
            urlList.add(indicatorSummaryUrl);
            urlList.add(indicatorSummaryUrl + "&size=large");
            urlList.add(sparklineUrl);
        }
        new Thread(new Browser(urlList)).start();
        return null;
    }

    class Browser implements Runnable {

        private List<String> urlList;

        public Browser(List<String> urlList) {
            this.urlList = urlList;
        }

        public void run() {
            callBrowser(urlList);
        }

        public void callBrowser(List<String> urlList) {
            for (String url : urlList) {
                System.out.println(url);
//                openURL(url);
            }
        }
    }

    private static void openURL(String url) {
        String osName = System.getProperty("os.name");
        Process p = null;
        int exitCode = 1;
        try {
            if (osName.startsWith("Mac")) {// Mac OS
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL",
                        new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else if (osName.startsWith("Windows")) {// Windows
                p = Runtime.getRuntime().exec(
                        "rundll32 url.dll,FileProtocolHandler " + url);
                exitCode = p.waitFor();
                System.out.println("###exitCode:" + exitCode);
            } else { // Unix or Linux
                String[] browsers = {"firefox", "opera", "konqueror",
                    "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(
                            new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
            Thread.sleep(10000);
            p.destroy();
        } catch (Exception ex) {
        }
    }

    @Autowired
    public void setTargetService(TargetService targetService) {
        this.targetService = targetService;
    }
}
