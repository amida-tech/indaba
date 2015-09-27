/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.google.gson.Gson;
import com.ocs.common.Config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ocs.util.StringUtils;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.po.Datapoint;
import com.ocs.indaba.aggregation.po.Workset;
import com.ocs.indaba.aggregation.service.DataExportService;
import com.ocs.indaba.aggregation.service.IndicatorService;
import com.ocs.indaba.aggregation.service.OrganizationService;
import com.ocs.indaba.aggregation.service.TargetService;
import com.ocs.indaba.aggregation.service.WorksetService;
import com.ocs.indaba.aggregation.vo.DataForm;
import com.ocs.indaba.aggregation.vo.PubIndicatorVO;
import com.ocs.indaba.aggregation.vo.TargetVO;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.StudyPeriod;
import com.ocs.indaba.service.UserService;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class CreateDataExportAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CreateDataExportAction.class);
    private static final String ATTR_ERRMSG = "errmsg";
    private static final String ATTR_STEP_PROCESSING = "step_process";
    private static final String EXPORT_ZIP_NAME = "/indaba_export.zip";
    private static String EXPORT_TEMP_ZIP_FOLDER = null;
    //service
    private UserService userService;
    private WorksetService pubWorksetService;
    private IndicatorService pubIndicatorService;
    private OrganizationService pubOrganizationService;
    private TargetService pubTargetService;
    private DataExportService dataExportService;

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
        logger.debug("Export data. [Step=" + step + "]");
        String fwd = null;

        switch (step) {
            case 1:
                fwd = handleStep1(request);             // select workset
                break;
            case 2:
                fwd = handleStep2(request);             // select indicators
                break;
            case 3:
                fwd = handleStep3(request);             // select targets
                break;
            case 4:
                fwd = handleStep4(request);             // select study periods
                break;
            case 5:
                fwd = handleStep5(request);             // export csv file
                break;
            case 6:
                fwd = handleStep5Waiting(request);      // waiting until the file is ready
                break;
            default:
                fwd = FWD_ERROR;
                logger.error("Invalid export data step: " + step);
                request.setAttribute(ATTR_ERRMSG, "Invalid parameter value 'step': " + step + "!");
                break;
        }
        request.setAttribute("dataForm", getDataForm(request));
        return mapping.findForward(fwd);
    }

    private String handleStep1(HttpServletRequest request) {
        //step 1 begins here
        DataForm dataForm = getNewDataForm(request);

        List<Workset> worksetList = pubWorksetService.getPublicWorksets();
        if (username != null) {
            worksetList.addAll(pubWorksetService.getWorksetByUsername(username));
            request.setAttribute("user", userService.getUser(username));
        }
        request.setAttribute("worksetList", worksetList);
        return FWD_STEP1;
    }

    private String handleStep2(HttpServletRequest request) {
        //last step
        DataForm dataForm = getDataForm(request);
        if (dataForm.isExportEntireSet()) {
            return handleStep1(request);
        }
        if (request.getParameter("workingset") == null && dataForm.getWorksetId() == Constants.INVALID_INT_ID) {
            return handleStep1(request);
        }
        Workset cWorkset;
        if (request.getParameter("workingset") != null) {
            dataForm.setWorksetName(request.getParameter("workingset"));
        }
        cWorkset = pubWorksetService.getWorksetByWorksetName(dataForm.getWorksetName());
        dataForm.setWorksetId(cWorkset.getId());
        if (request.getParameter("includeUnverifiedData") != null) {
            dataForm.setIncludeUnverifiedData(true);
        }
        //prepare for step 2
        Organization cOrganization = pubOrganizationService.getOrganizationByWorksetId(cWorkset.getId());//get organization
        request.setAttribute("organization", cOrganization);

        if (!dataForm.getDatapointIds().isEmpty()) {
            request.setAttribute("dataType", 2);
        } else {
            request.setAttribute("dataType", 1);
        }

        List<PubIndicatorVO> indicatorVOList = pubIndicatorService.getPubIndicatorVOsByWorksetId(cWorkset.getId());//get indicator list
        request.setAttribute("indicatorNames",
                new Gson().toJson(pubIndicatorService.getIndicatorNamesByVO(indicatorVOList)));
        request.setAttribute("indicatorQuestions",
                new Gson().toJson(pubIndicatorService.getIndicatorQuestionsByVO(indicatorVOList)));
        request.setAttribute("indicatorTags",
                new Gson().toJson(pubIndicatorService.getDistinctItagsByWorksetId(cWorkset.getId())));
        request.setAttribute("indicatorVOList", indicatorVOList);

        List<Datapoint> datapointList = pubWorksetService.getDatapointByWorksetId(cWorkset.getId());
        request.setAttribute("datapointNames",
                new Gson().toJson(pubWorksetService.getDatapointNames(datapointList)));
        request.setAttribute("datapointDescriptions",
                new Gson().toJson(pubWorksetService.getDatapointDes(datapointList)));
        request.setAttribute("datapointList", datapointList);

        return FWD_STEP2;
    }

    private String handleStep3(HttpServletRequest request) {

        DataForm dataForm = getDataForm(request);
        String idsStr = null;

        dataForm.getWsIndicatorIds().clear();
        dataForm.getDatapointIds().clear();
        if (request.getParameter("selectedIndicatorList") != null) {
            idsStr = request.getParameter("selectedIndicatorList");
            String[] idsArr = idsStr.split(",");
            List<Integer> ids = new ArrayList<Integer>();
            if (idsStr != null && !idsStr.isEmpty()) {
                for (String idStr : idsArr) {
                    ids.add(StringUtils.str2int(idStr, Constants.INVALID_INT_ID));
                }
                if (!ids.isEmpty()) {
                    dataForm.setWsIndicatorIds(ids);
                }
            }
        }
        if (request.getParameter("selectedDatapointList") != null) {
            idsStr = request.getParameter("selectedDatapointList");
            String[] idsArr = idsStr.split(",");
            List<Integer> ids = new ArrayList<Integer>();
            if (idsStr != null && !idsStr.isEmpty()) {
                for (String idStr : idsArr) {
                    ids.add(StringUtils.str2int(idStr, Constants.INVALID_INT_ID));
                }
                if (!ids.isEmpty()) {
                    dataForm.setDatapointIds(ids);
                }
            }
        }

        if (dataForm.isExportEntireSet()) {
            return handleStep1(request);
        }
        if (dataForm.getWsIndicatorIds().isEmpty()
                && dataForm.getDatapointIds().isEmpty() && idsStr == null) {     // invalid access
            return handleStep2(request);
        }

        // start step 3
        int worksetId = dataForm.getWorksetId();
        if (worksetId == Constants.INVALID_INT_ID) {
            return handleStep1(request);
        }
        List<TargetVO> targets = pubTargetService.getTargetVOsByWorksetId(worksetId);
        request.setAttribute("targets", targets);
        request.setAttribute("targetNames",
                new Gson().toJson(pubTargetService.getTargetNamesByTargets(targets)));
        request.setAttribute("targetTypes", Constants.TARGET_TYPES);
        request.setAttribute("targetTags", pubTargetService.getTargetTagsByTargets(targets));
        return FWD_STEP3;
    }

    private String handleStep4(HttpServletRequest request) {

        DataForm dataForm = getDataForm(request);
        String targedIdsStr = request.getParameter("targetIds");
        String[] targetIdsArr = targedIdsStr.split(",");

        if (dataForm.isExportEntireSet()) {
            return handleStep1(request);
        }
        if (dataForm.getTargetIds().isEmpty()
                && (targetIdsArr == null || targetIdsArr.length <= 0)) {     // invalid access
            return handleStep3(request);
        }
        List<Integer> targetIds = new ArrayList<Integer>();
        if (targetIdsArr != null) {            // next step
            for (String idStr : targetIdsArr) {
                targetIds.add(StringUtils.str2int(idStr, Constants.INVALID_INT_ID));
            }
            if (!targetIds.isEmpty()) {
                dataForm.setTargetIds(targetIds);
            }
        }

        // start step 4
        int worksetId = dataForm.getWorksetId();
        if (worksetId == Constants.INVALID_INT_ID) {
            return handleStep1(request);
        }
        List<StudyPeriod> studyPeriods = pubWorksetService.getStudyPeriodByWorksetId(worksetId);
        request.setAttribute("studyPeriods", studyPeriods);

        return FWD_STEP4;
    }

    private String handleStep5(HttpServletRequest request) {

        DataForm dataForm = getDataForm(request);
        String studyPeriodIdsStr = request.getParameter("studyPeriodIds");
        String[] studyPeriodIdsArr = null;

        String isExportEntireSet = request.getParameter("exportEntireSet");   // entire set
        if (isExportEntireSet != null) {    // from step 1
            dataForm.setExportEntireSet(true);
            if (request.getParameter("workingset") == null && dataForm.getWorksetId() == Constants.INVALID_INT_ID) {
                return handleStep1(request);
            }
            Workset cWorkset;
            if (request.getParameter("workingset") != null) {
                dataForm.setWorksetName(request.getParameter("workingset"));
            }
            cWorkset = pubWorksetService.getWorksetByWorksetName(dataForm.getWorksetName());
            dataForm.setWorksetId(cWorkset.getId());
            if (request.getParameter("includeUnverifiedData") != null) {
                dataForm.setIncludeUnverifiedData(true);
            }
        } else {    // from step 4
            studyPeriodIdsArr = studyPeriodIdsStr.split(",");
            dataForm.setExportEntireSet(false);
        }

        if (!dataForm.isExportEntireSet()) {
            if (dataForm.getStudyPeriodIds().isEmpty()
                    && (studyPeriodIdsArr == null || studyPeriodIdsArr.length <= 0)) {     // invalid access
                return handleStep4(request);
            }
            List<Integer> studyPeriodIds = new ArrayList<Integer>();
            if (studyPeriodIdsArr != null) {
                for (String idStr : studyPeriodIdsArr) {
                    studyPeriodIds.add(StringUtils.str2int(idStr, Constants.INVALID_INT_ID));
                }
                if (!studyPeriodIds.isEmpty()) {
                    dataForm.setStudyPeriodIds(studyPeriodIds);
                }
            }
        }

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
                DataExportProcessing processThread = new DataExportProcessing();
                processThread.setSession(session);
                processThread.setDataForm(dataForm);
                processThread.setDataExportService(dataExportService);
                processThread.start();
            }
            request.setAttribute("process_flag", 1);
        } else {
            session.removeAttribute("process_flag");
            logger.error("Invalid export data step: " + 5);
            request.setAttribute(ATTR_ERRMSG, "Invalid parameter value 'ep': null!");
            return FWD_ERROR;
        }

        return FWD_DONE;
    }

    private String handleStep5Waiting(HttpServletRequest request) {
        Object processFlag = session.getAttribute("process_flag");
        if (processFlag.toString().equals("1")) {
            session.setAttribute("process_flag", 2);
            request.setAttribute("process_flag", 2);
            EXPORT_TEMP_ZIP_FOLDER = "/data_export_zip";
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
        }
        return ATTR_STEP_PROCESSING;
    }

    private DataForm getNewDataForm(HttpServletRequest request) {
        DataForm dataForm = getDataForm(request);
        dataForm.init();
        return dataForm;
    }

    private DataForm getDataForm(HttpServletRequest request) {

        DataForm dataForm = (DataForm) request.getSession().getAttribute(ATTR_DATA);
        if (dataForm == null) {
            dataForm = new DataForm();
            request.getSession().setAttribute(ATTR_DATA, dataForm);
        }
        return dataForm;
    }

    @Autowired
    public void setUserService(UserService userSerivce) {
        this.userService = userSerivce;
    }

    @Autowired
    public void setWorksetService(WorksetService worksetSerivce) {
        this.pubWorksetService = worksetSerivce;
    }

    @Autowired
    public void setIndicatorService(IndicatorService indicatorSerivce) {
        this.pubIndicatorService = indicatorSerivce;
    }

    @Autowired
    public void setOrganizationService(OrganizationService organizationSerivce) {
        this.pubOrganizationService = organizationSerivce;
    }

    @Autowired
    public void setTargetService(TargetService pubTargetService) {
        this.pubTargetService = pubTargetService;
    }

    @Autowired
    public void setDataExportService(DataExportService dataExportService) {
        this.dataExportService = dataExportService;
    }
}

class DataExportProcessing extends Thread {

    private static final Logger logger = Logger.getLogger(DataExportProcessing.class);
    private HttpSession session = null;
    private DataForm dataForm = null;
    private DataExportService dataExportService = null;

    @Override
    public void run() {
        //start creating create zip
        try {
            System.out.println(">>start data export");
            dataExportService.createDataExport(dataForm, session);
        } catch (Exception e) {
            e.printStackTrace();
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
     * @return the dataForm
     */
    public DataForm getDataForm() {
        return dataForm;
    }

    /**
     * @param dataForm the dataForm to set
     */
    public void setDataForm(DataForm dataForm) {
        this.dataForm = dataForm;
    }

    @Autowired
    public void setDataExportService(DataExportService dataExportService) {
        this.dataExportService = dataExportService;
    }
}
