/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.google.gson.Gson;
import com.ocs.common.Config;
import com.ocs.indaba.aggregation.vo.JournalForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ocs.util.StringUtils;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.JournalExportService;
import com.ocs.indaba.aggregation.service.ProductService;
import com.ocs.indaba.aggregation.service.ProjectService;
import com.ocs.indaba.aggregation.service.TargetService;
import com.ocs.indaba.aggregation.vo.ProductBriefVO;
import com.ocs.indaba.aggregation.vo.ProductVO;
import com.ocs.indaba.aggregation.vo.TargetVO;
import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.UserService;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author rocky
 */
public class CreateJournalExportAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CreateJournalExportAction.class);
    private static final String ATTR_ERRMSG = "errmsg";
    private static final String ATTR_STEP_PROCESSING = "step_process";
    private static final String EXPORT_ZIP_NAME = "/indaba_export.zip";
    private static String EXPORT_TEMP_ZIP_FOLDER = null;
    private ProductService pubProductService;
    private ProjectService pubProjectService;
    private TargetService pubTargetService;
    private JournalExportService journalExportService = null;
    private UserService userService = null;


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
        /*
        if (actionFwd != null) {
            logger.info("User session is invalid. Redirect to login page!");
            return actionFwd;
        }
        */
        int step = StringUtils.str2int(request.getParameter(PARAM_STEP), Constants.INVALID_INT_ID);
        logger.debug("Export document. [Step=" + step + "]");
        String fwd = null;
        switch (step) {
            case 1:
                fwd = handleStep1(request);             // select product
                break;
            case 2:
                fwd = handleStep2(request);             // select targets
                break;
            case 3:
                fwd = handleStep3(request);             // create zip file
                break;
            case 4:
                fwd = handleStep3Waiting(request);      // waiting until the file is ready
                break;
            default:
                fwd = FWD_ERROR;
                logger.error("Invalid export document step: " + step);
                request.setAttribute(ATTR_ERRMSG, "Invalid parameter value 'step': " + step + "!");
                break;
        }
        request.setAttribute("journalForm", getJournalForm(request));
        return mapping.findForward(fwd);
    }

    // list products
    private String handleStep1(HttpServletRequest request) {
        logger.debug("User ID: " + uid);

        User user = (uid > 0) ? userService.getUser(uid) : null;
        OrgAuthorizer orgAuth = (user == null) ? null : new OrgAuthorizer(user);

        List<ProductBriefVO> bvos = pubProductService.getAccessibleJournalProducts(orgAuth, 0, null, null);
        ArrayList<ProductVO> products = new ArrayList<ProductVO>();

        if (bvos != null && !bvos.isEmpty()) {
            for (ProductBriefVO bvo : bvos) {
                ProductVO vo = new ProductVO();
                Product product = new Product();
                product.setId(bvo.getProductId());
                product.setName(bvo.getProductName());
                vo.setProduct(product);
                vo.setProjectName(bvo.getProjectName());
                products.add(vo);
            }
        }
        
        request.setAttribute("projectNames",
                new Gson().toJson(pubProjectService.getProjectNamesByProductVOs(products)));
        request.setAttribute("productNames",
                new Gson().toJson(pubProductService.getProductNamesByProductVOs(products)));
        request.setAttribute("products", products);
        return FWD_STEP1;
    }

    // list targets
    private String handleStep2(HttpServletRequest request) {

        JournalForm journalForm = getJournalForm(request);
        productId = StringUtils.str2int(request.getParameter("productId"), Constants.INVALID_INT_ID);

        if (journalForm.getProductId() == Constants.INVALID_INT_ID
                && productId == Constants.INVALID_INT_ID) {     // invalid access
            return handleStep1(request);
        }

        if (productId != Constants.INVALID_INT_ID) {            // next step
            ProductVO product = pubProductService.getProductVOById(productId);

            journalForm.setProductId(productId);
            journalForm.setProductName(product.getProduct().getName());
            journalForm.setProjectId(product.getProduct().getProjectId());
            journalForm.setProjectName(product.getProjectName());
            journalForm.setTargetIds(new ArrayList<Integer>());
        } else {                                                // the same step
            productId = journalForm.getProductId();
        }

        // start step 2
        List<TargetVO> targets = pubTargetService.getTargetVOsByProductIdForExport(productId);
        request.setAttribute("productId", productId);
        request.setAttribute("targets", targets);
        request.setAttribute("targetNames",
                new Gson().toJson(pubTargetService.getTargetNamesByTargets(targets)));
        request.setAttribute("targetTypes", Constants.TARGET_TYPES);
        request.setAttribute("targetTags", pubTargetService.getTargetTagsByTargets(targets));
        return FWD_STEP2;
    }

    private String handleStep3(HttpServletRequest request) {

        JournalForm journalForm = getJournalForm(request);

        journalForm.setProductId(StringUtils.str2int(request.getParameter("productId")));
        Product product =pubProductService.getProduct(journalForm.getProductId());
        journalForm.setProjectId(product.getProjectId());
        String targedIdsStr = request.getParameter("targetIds");
        String[] targetIdsArr = targedIdsStr.split(",");

        if (journalForm.getTargetIds().isEmpty()
                && (targetIdsArr == null || targetIdsArr.length <= 0)) {     // invalid access
            return handleStep2(request);
        }

        List<Integer> targetIds = new ArrayList<Integer>();
        if (targetIdsArr != null) {
            for (String idStr : targetIdsArr) {
                targetIds.add(StringUtils.str2int(idStr, Constants.INVALID_INT_ID));
            }
            if (!targetIds.isEmpty()) {
                journalForm.setTargetIds(targetIds);
            }
        }
        /*
         * in session
         * process_flag = 0 means processing
         * process_flag = 1 means finished
         * process_flag = null means not start or waiting for download
         */
        // start step 3

        String entryPoint = request.getParameter("ep");

        if (entryPoint != null) {
            Object processFlag = session.getAttribute("process_flag");
            if (processFlag == null || processFlag.toString().equals("2")) {
                int tempUid = -1;
                if (uid < 0) {
                    tempUid = (int) (new Date().getTime() % 1000000000);
                    session.setAttribute("temp_uid", tempUid);
                }
                session.setAttribute("process_flag", 0);
                MyThreadProcessing processThread = new MyThreadProcessing();
                processThread.setSession(session);
                processThread.setJournalForm(journalForm);
                processThread.setJournalExportService(journalExportService);
                processThread.start();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                logger.info("Time: " + df.format(new Date())
                        + "\tUser: " + (uid < 0 ? tempUid : uid) + "\tProject: " + journalForm.getProjectId()
                        + "\tProduct: " + journalForm.getProductId() + "\tTargets: " + journalForm.getTargetIds().toString());
            }
            request.setAttribute("process_flag", 1);
        } else {
            session.removeAttribute("process_flag");
            logger.error("Invalid export document step: " + 2);
            request.setAttribute(ATTR_ERRMSG, "Invalid parameter value 'ep': null!");
            return FWD_ERROR;
        }

        return FWD_DONE;
    }

    private String handleStep3Waiting(HttpServletRequest request) {

        Object processFlag = session.getAttribute("process_flag");
        if (processFlag.toString().equals("1")) {
            //TODO prevent from press back when processing
            session.setAttribute("process_flag", 2);
            request.setAttribute("process_flag", 2);
            EXPORT_TEMP_ZIP_FOLDER = "/journal_export_zip";
            EXPORT_TEMP_ZIP_FOLDER += "_" + (uid < 0 ? session.getAttribute("temp_uid") : uid);

            String exportBasePath = Config.getString(Constants.KEY_EXPORT_BASE_PATH);
            File zipFolder = new File(exportBasePath, EXPORT_TEMP_ZIP_FOLDER);
            if (zipFolder.exists()) {
                session.setAttribute("zipFileName", EXPORT_ZIP_NAME.substring(1));
                File zipFile = new File(exportBasePath + EXPORT_TEMP_ZIP_FOLDER + EXPORT_ZIP_NAME);
                long size = zipFile.length();
                DecimalFormat myFormatter = new DecimalFormat("#.#");
                if (size > 1024 * 1024) {
                    session.setAttribute("zipSize", myFormatter.format((double) size / (1024 * 1024.0)) + " MB");
                } else {
                    session.setAttribute("zipSize", myFormatter.format((double) size / 1024.0) + " KB");
                }
            }
        } else if (processFlag.toString().equals("0")) {
            request.setAttribute("process_flag", 1);
            //TODO We could show percentage here
        }
        return ATTR_STEP_PROCESSING;
    }

    private JournalForm getJournalForm(HttpServletRequest request) {

        JournalForm journalForm = (JournalForm) request.getSession().getAttribute(ATTR_JOURNAL);
        if (journalForm == null) {
            journalForm = new JournalForm();
            request.getSession().setAttribute(ATTR_JOURNAL, journalForm);
        }
        return journalForm;
    }

    @Autowired
    public void setProjectService(ProjectService pubProjectService) {
        this.pubProjectService = pubProjectService;
    }

    @Autowired
    public void setProductService(ProductService pubProductService) {
        this.pubProductService = pubProductService;
    }

    @Autowired
    public void setTargetService(TargetService pubTargetService) {
        this.pubTargetService = pubTargetService;
    }

    @Autowired
    public void setJournalExportService(JournalExportService journalExportService) {
        this.journalExportService = journalExportService;
    }

    @Autowired
    public void setUserService(UserService userSrvc) {
        this.userService = userSrvc;
    }
}

class MyThreadProcessing extends Thread {

    private static final Logger logger = Logger.getLogger(MyThreadProcessing.class);
    private HttpSession session = null;
    private JournalForm journalForm = null;
    private JournalExportService journalExportService = null;

    @Override
    public void run() {
        //start creating create zip
        try {
            journalExportService.createJournalExport(journalForm, session);
        } catch (Exception e) {
            logger.debug("Exception occurs", e);
        } finally {
            session.setAttribute("process_flag", 1);//Process finished
        }
    }

    /**
     * @return the session
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(HttpSession session) {
        this.session = session;
    }

    /**
     * @return the journalForm
     */
    public JournalForm getJournalForm() {
        return journalForm;
    }

    /**
     * @param journalForm the journalForm to set
     */
    public void setJournalForm(JournalForm journalForm) {
        this.journalForm = journalForm;
    }

    @Autowired
    public void setJournalExportService(JournalExportService journalExportService) {
        this.journalExportService = journalExportService;
    }
   
}
