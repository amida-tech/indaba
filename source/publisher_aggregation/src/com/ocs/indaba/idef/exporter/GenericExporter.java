/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.exporter;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.dao.IdefDAO;
import com.ocs.indaba.aggregation.service.ScorecardService;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.HorseVO;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.builder.dao.ProductDAO;
import com.ocs.indaba.builder.dao.ProjectDAO;
import com.ocs.indaba.dao.NotedefDAO;
import com.ocs.indaba.dao.NoteobjIntlDAO;
import com.ocs.indaba.idef.xo.DenormalizedSurveyAnswer;
import com.ocs.indaba.idef.xo.IndicatorChoices;
import com.ocs.indaba.idef.xo.QuestionAspect;
import com.ocs.indaba.po.Notedef;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.po.SurveyConfig;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.service.SurveyConfigService;
import com.ocs.indaba.survey.table.SurveyTableService;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.QuestionNode;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.util.Tree;
import com.ocs.indaba.vo.NoteobjIntlView;
import com.ocs.ssu.SpreadsheetWriter;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.ssu.WriterFactory;
import com.ocs.util.SimpleTree;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yc06x
 */
public class GenericExporter extends BaseExporter {

    private static final int DP_FIELD_POS_PROJ_ID = 0;
    private static final int DP_FIELD_POS_PROJ_NAME = 1;
    private static final int DP_FIELD_POS_PROD_ID = 2;
    private static final int DP_FIELD_POS_PROD_NAME = 3;
    private static final int DP_FIELD_POS_DP_ID = 4;
    private static final int DP_FIELD_POS_ASPECT_ID = 5;
    private static final int DP_FIELD_POS_ASPECT_TEXT = 6;
    private static final int DP_FIELD_POS_MEMBERS = 7;
    private static final int DP_FIELD_POS_TARGET_ID = 8;
    private static final int DP_FIELD_POS_TARGET_NAME = 9;
    private static final int DP_FIELD_POS_TARGET_DESC = 10;
    private static final int DP_FIELD_POS_ANSWER_USER_ID = 11;
    private static final int DP_FIELD_POS_ANSWER_USER_NAME = 12;
    private static final int DP_FIELD_POS_ANSWER_TIME = 13;
    private static final int DP_FIELD_POS_ANSWER_VALUE = 14;
    private static final int DP_FIELD_POS_ANSWER_SCORE = 15;
    private static final int DP_FIELD_POS_ANSWER_COMMENTS = 16;
    private static final int DP_FIELD_POS_ANSWER_SOURCES = 17;
    private static final int DP_FIELD_POS_ANSWER_SOURCE_DESC = 18;
    private static final int DP_FIELD_POS_REVIEW_USER_ID = 19;
    private static final int DP_FIELD_POS_REVIEW_USER_NAME = 20;
    private static final int DP_FIELD_POS_REVIEW_OPINION = 21;
    private static final int DP_FIELD_POS_REVIEW_ANSWER_VALUE = 22;
    private static final int DP_FIELD_POS_REVIEW_COMMENTS = 23;
    private static final int DP_FIELD_POS_DEPTH = 24;
    private static final int DP_FIELD_POS_LEVEL = 25;
    private static final int DP_FIELD_POS_PARENT = 26;
    private static final int DP_FIELD_POS_REVIEW_USER_ROLE = 27;
    private static final int DP_FIELD_COUNT = 28;

    private static final String DP_FIELD_NAMES[] = {
            "projectId",
            "projectName",
            "productId",
            "productName",
            "dataPointId",
            "aspectId",
            "aspectText",
            "memberDataPoints",
            "targetId",
            "targetName",
            "targetDescription",
            "answerUserId",
            "answerUserName",
            "answerTime",
            "answerValue",
            "answerScore",
            "answerComments",
            "answerSources",
            "answerSourceDescription",
            "reviewUserId",
            "reviewUserName",
            "reviewOpinion",
            "reviewAnswerValue",
            "reviewComments",
            "depth",
            "level",
            "parentDataPointId",
            "reviewUserRole"
    };


    private static final int META_FIELD_POS_ASPECT_ID = 0;
    private static final int META_FIELD_POS_ASPECT_NAME = 1;
    private static final int META_FIELD_POS_ASPECT_DESC = 2;
    private static final int META_FIELD_POS_RELATION_TYPE = 3;
    private static final int META_FIELD_POS_MEMBERS = 4;
    private static final int META_FIELD_POS_DATA_TYPE = 5;
    private static final int META_FIELD_POS_METHOD = 6;
    private static final int META_FIELD_POS_CHOICE = 7;
    private static final int META_FIELD_POS_SCORE = 8;
    private static final int META_FIELD_POS_CRITERIA = 9;
    private static final int META_FIELD_POS_DEPTH = 10;
    private static final int META_FIELD_POS_LEVEL = 11;
    private static final int META_FIELD_POS_PARENT = 12;
    private static final int META_FIELD_COUNT = 13;

    private static final String META_FIELD_NAMES[] = {
            "aspectId",
            "aspectName",
            "aspectDescription",
            "relationType",
            "memberAspects",
            "dataType",
            "scoreMethod",
            "choice",
            "score",
            "criteria",
            "depth",
            "level",
            "parentAspectId"
    };

    private static final String FIXED_ASPECT_ID_OVERALL = "OV";
    private static final String FIXED_ASPECT_NAME_OVERALL = "OVERALL";
    private static final String FIXED_ASPECT_DESC_OVERALL = "The overall performance of the target";

    private static final String FIXED_ASPECT_ID_LF = "LF";
    private static final String FIXED_ASPECT_NAME_LF = "Legal Framework";
    private static final String FIXED_ASPECT_DESC_LF = "The overall score of the target on questions that measure legal framework";

    private static final String FIXED_ASPECT_ID_IMPL = "AI";
    private static final String FIXED_ASPECT_NAME_IMPL = "Actual Implementation";
    private static final String FIXED_ASPECT_DESC_IMPL = "The overall score of the target on questions that measure actual implementation";

    private static final String FIXED_ASPECT_ID_GAP = "GAP";
    private static final String FIXED_ASPECT_NAME_GAP = "Implementation Gap";
    private static final String FIXED_ASPECT_DESC_GAP = "The gap between actual implementation and legal framework";


    private SurveyConfigService surveyConfigService;
    private SurveyTableService surveyTableService;

    private int productId;
    private Project project = null;
    private Product product = null;
    private String outputPath = null;

    private Map<Integer, List<IndicatorChoices>> indicatorChoiceMap = null;
    private Map<Integer, Target> targetMap = null;
    private List<HorseVO> horses = null;
    private SurveyConfig surveyConfig = null;
    private SurveyTree tree = null;
    private List<Node> nodeList = null;
    private Map<String, Node> nodeMap = null;

    private ProjectDAO projectDao = null;
    private NotedefDAO notedefDao = null;
    private NoteobjIntlDAO noteobjIntlDao = null;

    public GenericExporter(IdefDAO idefDao, ScorecardService srvc, Map<Integer, Target>targetMap,
            ProjectDAO projectDao, ProductDAO productDao, NotedefDAO notedefDao, NoteobjIntlDAO noteobjIntlDao,
            SurveyConfigService surveyConfigService, SurveyTableService surveyTableService,
            int productId, String outputPath) {
        this.idefDao = idefDao;
        this.scorecardService = srvc;
        this.productDao = productDao;
        this.productId = productId;
        this.outputPath = outputPath;
        this.surveyConfigService = surveyConfigService;
        this.surveyTableService = surveyTableService;
        this.projectDao = projectDao;
        this.notedefDao = notedefDao;
        this.noteobjIntlDao = noteobjIntlDao;
        this.targetMap = targetMap;
    }


    static private class ChoiceAnswer {
        Float score;
        String text;
    }


    private ChoiceAnswer getChoiceAnswer(int indicatorId, Integer choices, Map<Integer, List<IndicatorChoices>> indicatorChoiceMap) {
        ChoiceAnswer result = new ChoiceAnswer();

        result.score = null;
        result.text = "";

        if (choices == null) return result;

        List<IndicatorChoices> icList = indicatorChoiceMap.get(indicatorId);

        if (icList == null) return result;

        StringBuilder sb = new StringBuilder("");
        boolean isFirst = true;
        for (IndicatorChoices ic : icList) {
            if (ic.getMask() != null && ((ic.getMask() & choices) != 0)) {
                if (!isFirst) sb.append(" | ");
                else result.score = ic.getScore();

                sb.append(ic.getLabel());
                isFirst = false;
            }
        }
        result.text = sb.toString();
        return result;
    }


    private String getQuestionAspectId(int questionId) {
        return "Q"+questionId;
    }

    private String getCategoryAspectId(int catId) {
        return "C"+catId;
    }


    private String getDpID(int targetId, String aspectId) {
        return "" + targetId + "." + aspectId;
    }
    
    private String getNodeAspectId(Node node) {
        if (node.getType() == Node.NODE_TYPE_QUESTION) {
            return getQuestionAspectId(node.getId());
        } else {
            return getCategoryAspectId(node.getId());
        }
    }


    public void export() {
        try {
            doExport();
        } catch (Throwable ex) {
            setError("Exception: " + ex);
        }
    }


    private String getFullName(String firstName, String lastName) {
        boolean firstNameEmpty = StringUtils.isEmpty(firstName);
        boolean lastNameEmpty = StringUtils.isEmpty(lastName);

        return ((firstNameEmpty) ? "" : firstName + " ") +
               ((lastNameEmpty) ? "" : lastName);
    }

    
    private void exportHorse(HorseVO horse, SpreadsheetWriter writer)
            throws FileNotFoundException, UnsupportedFileTypeException, UnsupportedEncodingException, IOException {

        resetNodeAppData();

        Target target = targetMap.get(horse.getTargetId());
        List<DenormalizedSurveyAnswer> answers = idefDao.getAnswersByProduct(productId, target.getId());

        if (answers == null || answers.isEmpty()) {
            this.addMsg("Nothing to export for target " + target.getId() + " of product " + productId);
            return;
        }

        for (DenormalizedSurveyAnswer dsa : answers) {
            if (dsa.getIndicatorDataType() == 6) {
                // table type
                String tableText = surveyTableService.generateTableHtml(horse.getHorseId(), dsa.getQuestionId(), 1, 1, true);
                dsa.setAnswerValueText(tableText);
            }

            Node node = nodeMap.get(getQuestionAspectId(dsa.getQuestionId()));
            if (node == null) continue;

            // get the HTML text for table type DSA
            

            ArrayList<DenormalizedSurveyAnswer> dsaList = (ArrayList<DenormalizedSurveyAnswer>)node.getAppData();
            if (dsaList == null) {
                dsaList = new ArrayList<DenormalizedSurveyAnswer>();
                node.setAppData(dsaList);
            }
            dsaList.add(dsa);     
        }

        if (surveyConfig.getIsTsc()) {
            File srfFile = BasePersistence.getSRFFile(horse.getHorseId(), null);

            if (srfFile == null) {
                this.setError("Can't get SRF file for horse " + horse.getHorseId() + " of product " + productId);
                return;
            }

            SRFPersistence persistence = new SRFPersistence(null);

            ScorecardInfo scorecard = persistence.deserializeSRF(srfFile);

            List<Tree<ScorecardBaseNode>> rootCats = scorecard.getRootCategories();
            if (rootCats != null && !rootCats.isEmpty()) {
                for (Tree<ScorecardBaseNode> t : rootCats) {
                    registerCatNodes(t);
                }
            }

            String[] row = new String[DP_FIELD_COUNT];
            for (int i = 0; i < DP_FIELD_COUNT; i++) row[i] = "";

            row[DP_FIELD_POS_PROJ_ID] = "" + project.getId();
            row[DP_FIELD_POS_PROJ_NAME] = project.getCodeName();
            row[DP_FIELD_POS_PROD_ID] = "" + productId;
            row[DP_FIELD_POS_PROD_NAME] = product.getName();
            row[DP_FIELD_POS_TARGET_ID] = "" + target.getId();
            row[DP_FIELD_POS_TARGET_NAME] = target.getName();
            row[DP_FIELD_POS_TARGET_DESC] = target.getDescription();

            // overall
            row[DP_FIELD_POS_ASPECT_ID] = FIXED_ASPECT_ID_OVERALL;
            row[DP_FIELD_POS_ASPECT_TEXT] = FIXED_ASPECT_NAME_OVERALL;
            row[DP_FIELD_POS_DP_ID] = getDpID(target.getId(), row[DP_FIELD_POS_ASPECT_ID]);
            row[DP_FIELD_POS_ANSWER_SCORE] = "" + scorecard.getOverall();
            writer.writeNext(row);
            writer.flush();

            // legal framework
            row[DP_FIELD_POS_ASPECT_ID] = FIXED_ASPECT_ID_LF;
            row[DP_FIELD_POS_ASPECT_TEXT] = FIXED_ASPECT_NAME_LF;
            row[DP_FIELD_POS_DP_ID] = getDpID(target.getId(), row[DP_FIELD_POS_ASPECT_ID]);
            row[DP_FIELD_POS_ANSWER_SCORE] = "" + scorecard.getLegalFramework();
            writer.writeNext(row);
            writer.flush();

            // implementation
            row[DP_FIELD_POS_ASPECT_ID] = FIXED_ASPECT_ID_IMPL;
            row[DP_FIELD_POS_ASPECT_TEXT] = FIXED_ASPECT_NAME_IMPL;
            row[DP_FIELD_POS_DP_ID] = getDpID(target.getId(), row[DP_FIELD_POS_ASPECT_ID]);
            row[DP_FIELD_POS_ANSWER_SCORE] = "" + scorecard.getImplementation();
            writer.writeNext(row);
            writer.flush();

            // gap
            row[DP_FIELD_POS_ASPECT_ID] = FIXED_ASPECT_ID_GAP;
            row[DP_FIELD_POS_ASPECT_TEXT] = FIXED_ASPECT_NAME_GAP;
            row[DP_FIELD_POS_DP_ID] = getDpID(target.getId(), row[DP_FIELD_POS_ASPECT_ID]);
            row[DP_FIELD_POS_ANSWER_SCORE] = "" + scorecard.getImplementationGap();
            writer.writeNext(row);
            writer.flush();          
        }

        SimpleTree<Node> root = tree.getRoot();
        List<SimpleTree<Node>> children = root.getChildren();
        if (children != null) {
            for (SimpleTree<Node> child : children) {
                exportTree(writer, child, target);
            }
        }             
    }

    
    private void registerCatNodes(Tree<ScorecardBaseNode> tree) {
        ScorecardBaseNode node = tree.getNode();
        if (node.getNodeType() != Constants.NODE_TYPE_CATEGORY) return;
        CategoryNode catNode = (CategoryNode) node;
        Node surveyTreeNode = nodeMap.get(getCategoryAspectId(catNode.getId()));
        if (surveyTreeNode != null) surveyTreeNode.setAppData(catNode);

        List<Tree<ScorecardBaseNode>> children = tree.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Tree<ScorecardBaseNode> child : children) {
                registerCatNodes(child);
            }
        }
    }


    private void exportTree(SpreadsheetWriter writer, SimpleTree<Node> tree, Target target) throws IOException {

        String[] row = new String[DP_FIELD_COUNT];
        for (int i = 0; i < DP_FIELD_COUNT; i++) row[i] = "";

        Node node = tree.getValue();
        List<SimpleTree<Node>> children = tree.getChildren();

        row[DP_FIELD_POS_DEPTH] = "" + node.getDepth();
        row[DP_FIELD_POS_LEVEL] = "" + node.getLevel();

        Node parent = node.getParent();
        if (parent != null) {
            row[DP_FIELD_POS_PARENT] = getDpID(target.getId(), getNodeAspectId(parent));
        }

        row[DP_FIELD_POS_PROJ_ID] = "" + project.getId();
        row[DP_FIELD_POS_PROJ_NAME] = project.getCodeName();
        row[DP_FIELD_POS_PROD_ID] = "" + productId;
        row[DP_FIELD_POS_PROD_NAME] = product.getName();
        row[DP_FIELD_POS_TARGET_ID] = "" + target.getId();
        row[DP_FIELD_POS_TARGET_NAME] = target.getName();
        row[DP_FIELD_POS_TARGET_DESC] = target.getDescription();
        row[DP_FIELD_POS_ASPECT_ID] = getNodeAspectId(node);
        row[DP_FIELD_POS_ASPECT_TEXT] = node.getName();

        if (node.getType() == Node.NODE_TYPE_QUESTION) {            
            row[DP_FIELD_POS_DP_ID] = getDpID(target.getId(), row[DP_FIELD_POS_ASPECT_ID]);

            List<DenormalizedSurveyAnswer> dsaList = (List<DenormalizedSurveyAnswer>)node.getAppData();
            if (dsaList == null) {
                row[DP_FIELD_POS_ANSWER_SCORE] = NULL;
                writer.writeNext(row);
                writer.flush();
                return;
            }
            for (DenormalizedSurveyAnswer dsa : dsaList) {
                row[DP_FIELD_POS_ANSWER_USER_ID] = "" + dsa.getAnswerUserId();
                row[DP_FIELD_POS_ANSWER_USER_NAME] = getFullName(dsa.getAnswerUserFirstName(), dsa.getAnswerUserLastName());
                row[DP_FIELD_POS_ANSWER_TIME] = "" + dsa.getAnswerTime();
                row[DP_FIELD_POS_ANSWER_COMMENTS] = dsa.getAnswerComments();
                row[DP_FIELD_POS_ANSWER_SOURCE_DESC] = dsa.getRefSourceDescription();
                row[DP_FIELD_POS_REVIEW_USER_ID] = "" + dsa.getReviewUserId();
                row[DP_FIELD_POS_REVIEW_USER_NAME] = getFullName(dsa.getReviewUserFirstName(), dsa.getReviewUserLastName());
                row[DP_FIELD_POS_REVIEW_USER_ROLE] = dsa.getReviewUserRole();
                row[DP_FIELD_POS_REVIEW_OPINION] = "" + dsa.getReviewOpinion();
                row[DP_FIELD_POS_REVIEW_COMMENTS] = dsa.getReviewComments();
                row[DP_FIELD_POS_ANSWER_SOURCES] = "";

                switch (dsa.getIndicatorDataType()) {
                    case 1:
                        // single-choice type
                        ChoiceAnswer ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getAnswerValueChoices(), indicatorChoiceMap);
                        row[DP_FIELD_POS_ANSWER_VALUE] = ca.text;
                        row[DP_FIELD_POS_ANSWER_SCORE] = "" + ca.score;

                        ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getReviewAnswerValueChoices(), indicatorChoiceMap);
                        row[DP_FIELD_POS_REVIEW_ANSWER_VALUE] = ca.text;
                        break;

                    case 2:
                        ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getAnswerValueChoices(), indicatorChoiceMap);
                        row[DP_FIELD_POS_ANSWER_VALUE] = ca.text;
                        row[DP_FIELD_POS_ANSWER_SCORE] = NULL;

                        ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getReviewAnswerValueChoices(), indicatorChoiceMap);
                        row[DP_FIELD_POS_REVIEW_ANSWER_VALUE] = ca.text;
                        break;

                    case 3:
                        // int
                        row[DP_FIELD_POS_ANSWER_VALUE] = "" + dsa.getAnswerValueInt();
                        row[DP_FIELD_POS_ANSWER_SCORE] = NULL;
                        row[DP_FIELD_POS_REVIEW_ANSWER_VALUE] = "" + dsa.getReviewAnswerValueInt();
                        break;

                    case 4:
                        // float
                        row[DP_FIELD_POS_ANSWER_VALUE] = "" + dsa.getAnswerValueFloat();
                        row[DP_FIELD_POS_ANSWER_SCORE] = NULL;
                        row[DP_FIELD_POS_REVIEW_ANSWER_VALUE] = "" + dsa.getReviewAnswerValueFloat();
                        break;

                    case 5:
                    case 6:
                        // text or table
                        row[DP_FIELD_POS_ANSWER_VALUE] = dsa.getAnswerValueText();
                        row[DP_FIELD_POS_ANSWER_SCORE] = NULL;
                        row[DP_FIELD_POS_REVIEW_ANSWER_VALUE] = dsa.getReviewAnswerValueText();
                        break;

                    default:
                        row[DP_FIELD_POS_ANSWER_VALUE] = "";
                        row[DP_FIELD_POS_ANSWER_SCORE] = NULL;
                        row[DP_FIELD_POS_REVIEW_ANSWER_VALUE] = "";
                }

                writer.writeNext(row);
                writer.flush();
            }                
        } else {
            // category node
            CategoryNode catNode = (CategoryNode)node.getAppData();
            row[DP_FIELD_POS_DP_ID] = getDpID(target.getId(), row[DP_FIELD_POS_ASPECT_ID]);
            row[DP_FIELD_POS_ANSWER_SCORE] = "" + (catNode != null ? catNode.getMean() : NULL);
            row[DP_FIELD_POS_MEMBERS] = "";

            if (children != null) {
                StringBuffer sb = new StringBuffer();
                boolean isFirst = true;
                for (SimpleTree<Node> child : children) {
                    Node childNode = child.getValue();
                    if (!isFirst) {
                        sb.append("|");
                    }
                    String aspectId = getNodeAspectId(childNode);
                    sb.append(getDpID(target.getId(), aspectId));
                    isFirst = false;
                }
                row[DP_FIELD_POS_MEMBERS] = sb.toString();
            }

            writer.writeNext(row);
            writer.flush();
        }

        // export children
        if (children != null) {
            for (SimpleTree<Node> child : children) {
                exportTree(writer, child, target);
            }
        }
    }


    private void doExport() throws FileNotFoundException, UnsupportedFileTypeException, UnsupportedEncodingException, IOException {

        this.addMsg("======== STARTED PRODUCT " + productId);

        product = productDao.get(productId);
        if (product == null) {
            this.setError("Nonexistent product " + productId);
            return;
        } else if (product.getContentType() != 0) {
            this.setError("Product " + productId + " is not a survey product!");
            return;
        }

        project = projectDao.get(product.getProjectId());
        if (product == null) {
            this.setError("Nonexistent project for product " + productId);
            return;
        }

        surveyConfig = surveyConfigService.getSurveyConfig(product.getProductConfigId());
        if (surveyConfig == null) {
            this.setError("Product " + productId + " has no survey config!");
            return;
        }

        // get flattened tree
        tree = surveyConfigService.buildTree(product.getProductConfigId());
        if (tree == null) {
            this.setError("Can't build survey tree.");
            return;
        }
        if (tree.getRoot() == null) {
            this.setError("Bad survey tree - no root.");
            return;
        }

        nodeList = tree.listNodes();
        nodeMap = new HashMap<String, Node>();
        for (Node node : nodeList) {
            node.setAppData(null);
            nodeMap.put(getNodeAspectId(node), node);
        }

        // get all targets of the product
        horses = scorecardService.getHorsesByProductId(productId);
        if (horses == null || horses.isEmpty()) {
            this.addMsg("No exportable horses for product " + productId);
            return;
        }        

        // get all indicator choices
        List<IndicatorChoices> indicatorChoices = idefDao.getIndicatorChoicesByProduct(productId);
        indicatorChoiceMap = new HashMap<Integer, List<IndicatorChoices>>();

        if (indicatorChoices != null && !indicatorChoices.isEmpty()) {
            for (IndicatorChoices ic : indicatorChoices) {
                List<IndicatorChoices> choiceList = indicatorChoiceMap.get(ic.getIndicatorId());
                if (choiceList == null) {
                    choiceList = new ArrayList<IndicatorChoices>();
                    indicatorChoiceMap.put(ic.getIndicatorId(), choiceList);
                }
                choiceList.add(ic);
            }
        }

        // get all ref choices
        List<ReferenceChoice> refChoices = idefDao.getReferenceChoicesByProduct(productId);
        Map<Integer, List<ReferenceChoice>> refChoiceMap = new HashMap<Integer, List<ReferenceChoice>>();

        if (refChoices != null) {
            for (ReferenceChoice rc : refChoices) {
                List<ReferenceChoice> choiceList = refChoiceMap.get(rc.getReferenceId());
                if (choiceList == null) {
                    choiceList = new ArrayList<ReferenceChoice>();
                    refChoiceMap.put(rc.getReferenceId(), choiceList);
                }
                choiceList.add(rc);
            }
        }

        generateMetaFile();
        generateDpFile();
        generateNotesFile();

        this.addMsg("======== FINISHED PRODUCT " + productId);
    }



    private void resetNodeAppData() {
        for (Node node : nodeList) {
            node.setAppData(null);
        }
    }


    private void generateMetaFile() throws FileNotFoundException, UnsupportedFileTypeException, UnsupportedEncodingException, IOException {
        // output the meta file
        String metaFileName = "export.generic.prod." + productId + ".meta.csv";
        String outputFileName = outputPath + "/" + metaFileName;
        this.setMetaFileName(outputFileName);

        File metaFile = new File(outputPath, metaFileName);
	FileOutputStream metaOut = new FileOutputStream(metaFile);
        SpreadsheetWriter metaWriter = WriterFactory.createWriter(metaOut, WriterFactory.FILE_TYPE_CSV);

        // write the header line
        metaWriter.writeNext(META_FIELD_NAMES);
        metaWriter.flush();

        List<QuestionAspect> qstAspects = idefDao.getQuestionAspectsByProduct(productId);

        for (QuestionAspect aspect : qstAspects) {
            Node node = nodeMap.get(getQuestionAspectId(aspect.getQuestionId()));
            if (node == null) continue;

            ArrayList<QuestionAspect> aspectList = (ArrayList<QuestionAspect>)node.getAppData();
            if (aspectList == null) {
                aspectList = new ArrayList<QuestionAspect>();
                node.setAppData(aspectList);
            }
            aspectList.add(aspect);
        }

        SimpleTree<Node> root = tree.getRoot();

        List<SimpleTree<Node>> children = root.getChildren();
        if (children != null) {
            for (SimpleTree<Node> child : children) {
                exportTreeAspects(metaWriter, child);
            }
        }

        if (surveyConfig.getIsTsc()) {
            // finally fixed aspects
            exportFixedAspect(metaWriter, FIXED_ASPECT_ID_OVERALL, FIXED_ASPECT_NAME_OVERALL, FIXED_ASPECT_DESC_OVERALL);
            exportFixedAspect(metaWriter, FIXED_ASPECT_ID_LF, FIXED_ASPECT_NAME_LF, FIXED_ASPECT_DESC_LF);
            exportFixedAspect(metaWriter, FIXED_ASPECT_ID_IMPL, FIXED_ASPECT_NAME_IMPL, FIXED_ASPECT_DESC_IMPL);
            exportFixedAspect(metaWriter, FIXED_ASPECT_ID_GAP, FIXED_ASPECT_NAME_GAP, FIXED_ASPECT_DESC_GAP);
        }

        metaWriter.close();

        this.addMsg("Meta file generated: " + outputFileName);
    }


    private void generateDpFile() throws FileNotFoundException, UnsupportedFileTypeException, UnsupportedEncodingException, IOException {
        String dpFileName = "export.generic.prod." + productId + ".dp.csv";
        String outputFileName = outputPath + "/" + dpFileName;
        this.setExportFileName(outputFileName);
        File outputFile = new File(outputPath, dpFileName);
	FileOutputStream fout = new FileOutputStream(outputFile);
        SpreadsheetWriter writer = WriterFactory.createWriter(fout, WriterFactory.FILE_TYPE_CSV);

        // write the header line
        writer.writeNext(DP_FIELD_NAMES);
        writer.flush();

        for (HorseVO horse : horses) {
            exportHorse(horse, writer);
        }

        writer.close();
        this.addMsg("DP file generated: " + outputFileName);

    }


    private void exportFixedAspect(SpreadsheetWriter writer, String aspectId, String aspectName, String aspectDesc) throws IOException {
        String[] row = new String[META_FIELD_COUNT];
        for (int i = 0; i < META_FIELD_COUNT; i++) row[i] = "";

        row[META_FIELD_POS_ASPECT_ID] = aspectId;
        row[META_FIELD_POS_ASPECT_NAME] = aspectName;
        row[META_FIELD_POS_ASPECT_DESC] = aspectDesc;
        row[META_FIELD_POS_DATA_TYPE] = "Float";
        row[META_FIELD_POS_METHOD] = "aggr";
        row[META_FIELD_POS_RELATION_TYPE] = "self";

        writer.writeNext(row);
        writer.flush();
    }



    private void exportTreeAspects(SpreadsheetWriter writer, SimpleTree<Node> tree) throws IOException {
        String[] row = new String[META_FIELD_COUNT];
        for (int i = 0; i < META_FIELD_COUNT; i++) row[i] = "";

        Node node = tree.getValue();
        List<SimpleTree<Node>> children = tree.getChildren();

        row[META_FIELD_POS_DEPTH] = "" + node.getDepth();
        row[META_FIELD_POS_LEVEL] = "" + node.getLevel();

        Node parent = node.getParent();
        if (parent != null) {
            row[META_FIELD_POS_PARENT] = getNodeAspectId(parent);
        }

        row[META_FIELD_POS_ASPECT_ID] = getNodeAspectId(node);
        row[META_FIELD_POS_ASPECT_DESC] = node.getName();

        if (node.getType() == Node.NODE_TYPE_QUESTION) {
            List<QuestionAspect> aspectList = (List<QuestionAspect>)node.getAppData();

            if (aspectList == null) {
                this.setError("Cannot get aspects for question " + node.getId());
                return;
            }

            for (QuestionAspect aspect : aspectList) {                
                row[META_FIELD_POS_ASPECT_NAME] = aspect.getQuestionName();               
                row[META_FIELD_POS_RELATION_TYPE] = "self";
                row[META_FIELD_POS_DATA_TYPE] = getDataTypeName(aspect.getAnswerType());
                row[META_FIELD_POS_METHOD] = "raw";
                row[META_FIELD_POS_CHOICE] = aspect.getChoiceLabel();
                row[META_FIELD_POS_SCORE] = (aspect.getScore() == null) ? NULL : "" + aspect.getScore();
                row[META_FIELD_POS_CRITERIA] = aspect.getCriteria();

                writer.writeNext(row);
                writer.flush();
            }
        } else {
            // category node
            row[META_FIELD_POS_ASPECT_NAME] = "Category " + node.getId();
            row[META_FIELD_POS_RELATION_TYPE] = "contains";
            row[META_FIELD_POS_DATA_TYPE] = "Float";
            row[META_FIELD_POS_METHOD] = "mean";

            if (children != null) {
                StringBuffer sb = new StringBuffer();
                boolean isFirst = true;
                for (SimpleTree<Node> child : children) {
                    Node childNode = child.getValue();
                    if (!isFirst) {
                        sb.append("|");
                    }
                    String aspectId = getNodeAspectId(childNode);
                    sb.append(aspectId);
                    isFirst = false;
                }
                row[META_FIELD_POS_MEMBERS] = sb.toString();
            }

            writer.writeNext(row);
            writer.flush();
        }        

        // export children
        if (children != null) {
            for (SimpleTree<Node> child : children) {
                exportTreeAspects(writer, child);
            }
        }
    }



    static private final String DATA_TYPE_NAMES[] = {
        "UNDEFINED",
        "SingleChoice",
        "MultiChoice",
        "Integer",
        "Float",
        "Text",
        "Table"
    };

    private String getDataTypeName(int answerType) {
        return (answerType <= 0 || answerType > 6) ? "UNDEFINED" : DATA_TYPE_NAMES[answerType];
    }


    private static final String NOTES_FIXED_FIELD_NAMES[] = {
            "aspectId",
            "targetId",
            "targetName",
            "targetDescription"
    };

    private static final int NOTES_FIXED_FIELD_POS_ASPECT_ID = 0;
    private static final int NOTES_FIXED_FIELD_POS_TARGET_ID = 1;
    private static final int NOTES_FIXED_FIELD_POS_TARGET_NAME = 2;
    private static final int NOTES_FIXED_FIELD_POS_TARGET_DESC = 3;
    private static final int NUM_NOTES_FIXED_FIELDS = 4;


    private void generateNotesFile() throws FileNotFoundException, UnsupportedFileTypeException, UnsupportedEncodingException, IOException {
        List<Notedef> notedefs = notedefDao.selectEnabledNotedefsByProductId(productId);
        if (notedefs == null || notedefs.isEmpty()) return;

        String headers[] = new String[NUM_NOTES_FIXED_FIELDS + 2 * notedefs.size()];
        for (int i = 0; i < NUM_NOTES_FIXED_FIELDS; i++) {
            headers[i] = NOTES_FIXED_FIELD_NAMES[i];
        }

        Map<Integer, Integer> notedefSeqMap = new HashMap<Integer, Integer>();
        int seqNum = 0;
        for (Notedef def : notedefs) {
            headers[NUM_NOTES_FIXED_FIELDS + 2 * seqNum] = "noteTitle" + (seqNum+1);
            headers[NUM_NOTES_FIXED_FIELDS + 2 * seqNum + 1] = "noteData" + (seqNum+1);
            notedefSeqMap.put(def.getId(), seqNum);
            seqNum++;
        }

        String notesFileName = "export.generic.prod." + productId + ".notes.csv";
        String outputFileName = outputPath + "/" + notesFileName;
        this.setExportFileName(outputFileName);
        File outputFile = new File(outputPath, notesFileName);
	FileOutputStream fout = new FileOutputStream(outputFile);
        SpreadsheetWriter writer = WriterFactory.createWriter(fout, WriterFactory.FILE_TYPE_CSV);

        // write the header line
        writer.writeNext(headers);
        writer.flush();

        for (HorseVO horse : horses) {
            exportHorseNotes(horse, writer, notedefs, notedefSeqMap);
        }

        writer.close();
        this.addMsg("Notes file generated: " + outputFileName);
    }


    private String questionNoteKey(int qstId, int notedefId) {
        return "Q" + qstId + ".N" + notedefId;
    }

    private void exportHorseNotes(HorseVO horse, SpreadsheetWriter writer, List<Notedef> notedefs, Map<Integer, Integer> notedefSeqMap) throws IOException {
        List<NoteobjIntlView> intls = noteobjIntlDao.selectByHorseId(horse.getHorseId());

        if (intls == null || intls.isEmpty()) return;

        List<Integer> qstIds = new ArrayList<Integer>();
        Map<String, StringBuffer> noteMap = new HashMap<String, StringBuffer>();

        for (NoteobjIntlView intl : intls) {
            String key = questionNoteKey(intl.getSurveyQuestionId(), intl.getNotedefId());
            StringBuffer buffer = noteMap.get(key);
            boolean isFirst = false;
            if (buffer == null) {
                buffer = new StringBuffer();
                noteMap.put(key, buffer);
                isFirst = true;
                if (!qstIds.contains(intl.getSurveyQuestionId())) {
                    qstIds.add(intl.getSurveyQuestionId());
                }
            }

            if (!isFirst) {
                buffer.append(" | ");
            }

            buffer.append("<").append(intl.getLanguage()).append("> ").append(intl.getNote());
        }

        String[] row = new String[NUM_NOTES_FIXED_FIELDS + 2 * notedefs.size()];
        for (Integer qstId : qstIds) {

            // clear the row
            for (int i = 0; i < row.length; i++) {
                row[i] = "";
            }

            Target target = targetMap.get(horse.getTargetId());
            row[NOTES_FIXED_FIELD_POS_ASPECT_ID] = getQuestionAspectId(qstId);
            row[NOTES_FIXED_FIELD_POS_TARGET_ID] = ""+target.getId();
            row[NOTES_FIXED_FIELD_POS_TARGET_NAME] = target.getName();
            row[NOTES_FIXED_FIELD_POS_TARGET_DESC] = target.getDescription();

            for (Notedef def : notedefs) {
                StringBuffer noteBuffer = noteMap.get(questionNoteKey(qstId, def.getId()));

                Integer defSeq = notedefSeqMap.get(def.getId());
                if (defSeq == null) continue;

                row[NUM_NOTES_FIXED_FIELDS + 2 * defSeq] = def.getName();

                if (noteBuffer != null) {
                    row[NUM_NOTES_FIXED_FIELDS + 2 * defSeq + 1] = noteBuffer.toString();
                }
            }
            writer.writeNext(row);
            writer.flush();
        }
    }

}
