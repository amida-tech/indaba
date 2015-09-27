/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.imp;

import com.ocs.indaba.idef.xo.Indicator;
import com.ocs.indaba.idef.xo.Product;
import com.ocs.indaba.idef.xo.Project;
import com.ocs.indaba.idef.xo.Ref;
import com.ocs.indaba.idef.xo.Scorecard;
import com.ocs.indaba.idef.xo.SurveyAnswer;
import com.ocs.indaba.idef.xo.SurveyCategory;
import com.ocs.indaba.idef.xo.SurveyConfig;
import com.ocs.indaba.idef.xo.SurveyQuestion;
import com.ocs.indaba.idef.xo.User;
import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.StudyPeriod;
import com.ocs.indaba.po.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yc06x
 */
public class ProcessContext {

    private Map<String, Object> map = null;

    private List<User> userList = new ArrayList<User>();
    private List<Project> projectList = new ArrayList<Project>();
    private List<Product> productList = new ArrayList<Product>();
    private List<Indicator> indicatorList = new ArrayList<Indicator>();
    private List<SurveyConfig> scList = new ArrayList<SurveyConfig>();
    private List<Scorecard> scorecardList = new ArrayList<Scorecard>();
    private List<Ref> refList = new ArrayList<Ref>();
    private List<SurveyCategory> categoryList = new ArrayList<SurveyCategory>();

    private List<String> errors = new ArrayList<String>();
    private int importId = 0;

    private boolean doMapping = false;
    private boolean addImpPrefix = true;

    public void setDoMapping(boolean flag) {
        this.doMapping = flag;
    }

    public void setAddImpPrefix(boolean flag) {
        this.addImpPrefix = flag;
    }

    public boolean doMapping() {
        return this.doMapping;
    }

    public boolean addImpPrefix() {
        return addImpPrefix;
    }

    public void setImportId(int id) {
        this.importId = id;
    }

    public int getImportId() {
        return this.importId;
    }

    public List<User> getUsers() {
        return userList;
    }

    public List<Project> getProjects() {
        return this.projectList;
    }

    public List<Product> getProducts() {
        return this.productList;
    }

    public List<Indicator> getIndicators() {
        return this.indicatorList;
    }

    public List<SurveyConfig> getSurveyConfigs() {
        return this.scList;
    }

    public List<Scorecard> getScorecards() {
        return this.scorecardList;
    }


    public List<Ref> getRefs() {
        return this.refList;
    }

    public List<String> getErrors() {
        return this.errors;
    }


    public int getErrorCount() {
        return errors.size();
    }


    public ProcessContext(List<Organization> orgs, List<Ref> refs, List<Language> langs, List<StudyPeriod> sps, List<Target> targets) {
        map = new HashMap<String, Object>();

        for (Organization org : orgs) {
            this.addOrg(org.getName(), org);
        }

        for (Language lang : langs) {
            this.addLanguage(lang.getLanguage(), lang);
            this.addLanguage(lang.getLanguageDesc(), lang);
        }

        for (Ref ref : refs) {
            this.addRefByName(ref.getName(), ref);
        }

        for (StudyPeriod sp : sps) {
            this.addStudyPeriod(sp.getName(), sp);
        }

        for (Target t : targets) {
            this.addTarget(t.getName(), t);
            this.addTarget(t.getShortName(), t);
        }
    }

    private static String normalize(String str) {
        String result = str.replaceAll("\\s+", "");
        return result.trim().toLowerCase();
    }

    public void addError(String error) {
        errors.add(error);
    }

    private String projectKey(String id) {
        return "proj:" + normalize(id);
    }

    private String productKey(String id) {
        return "prod:" + normalize(id);
    }

    private String indicatorKey(String id) {
        return "ind:" + normalize(id);
    }

    private String indicatorNameKey(String name) {
        return "indn:" + normalize(name);
    }

    private String scorecardKey(String id) {
        return "card:" + normalize(id);
    }

    private String categoryKey(String id) {
        return "cat:" + normalize(id);
    }

    private String questionKey(String id) {
        return "qst:" + normalize(id);
    }

    private String surveyConfigKey(String id) {
        return "sc:" + normalize(id);
    }

    public void addProject(String id, Project project) {
        map.put(projectKey(id), project);
        projectList.add(project);
    }

    public Project getProject(String id) {
        return (Project)map.get(projectKey(id));
    }

    public void addProduct(String id, Product product) {
        map.put(productKey(id), product);
        productList.add(product);
    }

    public Product getProduct(String id) {
        return (Product)map.get(productKey(id));
    }

    public void addIndicatorById(String id, Indicator obj) {
        map.put(indicatorKey(id), obj);
        indicatorList.add(obj);
    }

    public Indicator getIndicatorById(String id) {
        return (Indicator)map.get(indicatorKey(id));
    }

    public void addIndicatorByName(String name, Indicator obj) {
        map.put(indicatorNameKey(name), obj);
    }

    public Indicator getIndicatorByName(String name) {
        return (Indicator)map.get(indicatorNameKey(name));
    }

    public void addScorecard(String id, Scorecard obj) {
        map.put(scorecardKey(id), obj);
        scorecardList.add(obj);
    }

    public Scorecard getScorecard(String id) {
        return (Scorecard)map.get(scorecardKey(id));
    }

    public void addCategory(String id, SurveyCategory obj) {
        map.put(categoryKey(id), obj);
    }

    public SurveyCategory getCategory(String id) {
        return (SurveyCategory)map.get(categoryKey(id));
    }

    public void addQuestion(String id, SurveyQuestion obj) {
        map.put(questionKey(id), obj);
    }

    public SurveyQuestion getQuestion(String id) {
        return (SurveyQuestion)map.get(questionKey(id));
    }

    public void addSurveyConfig(String id, SurveyConfig obj) {
        map.put(surveyConfigKey(id), obj);
        scList.add(obj);
    }

    public SurveyConfig getSurveyConfig(String id) {
        return (SurveyConfig)map.get(surveyConfigKey(id));
    }


    private String orgKey(String name) {
        return "org:" + normalize(name);
    }

    private void addOrg(String name, Organization obj) {
        if (name == null || name.isEmpty()) return;
        map.put(orgKey(name), obj);
    }

    public Organization getOrg(String name) {
        return (Organization)map.get(orgKey(name));
    }

    private String userKey(String id) {
        return "usr:" + normalize(id);
    }

    public void addUser(String id, User obj) {
        map.put(userKey(id), obj);
        userList.add(obj);
    }

    public User getUser(String id) {
        return (User)map.get(userKey(id));
    }


    private String languageKey(String name) {
        return "lang:" + normalize(name);
    }

    private void addLanguage(String name, Language obj) {
        map.put(languageKey(name), obj);
    }

    public Language getLanguage(String name) {
        return (Language)map.get(languageKey(name));
    }


    private String studyPeriodKey(String name) {
        return "sp:" + normalize(name);
    }

    private void addStudyPeriod(String name, StudyPeriod obj) {
        map.put(studyPeriodKey(name), obj);
    }

    public StudyPeriod getStudyPeriod(String name) {
        return (StudyPeriod)map.get(studyPeriodKey(name));
    }

    private String refNameKey(String name) {
        return "ref:" + normalize(name);
    }

    public void addRefByName(String name, Ref obj) {
        map.put(refNameKey(name), obj);
    }

    public Ref getRefByName(String name) {
        String key = refNameKey(name);
        Ref ref = (Ref)map.get(key);
        return ref;
    }
   
    
    private String targetKey(String name) {
        return "t:" + normalize(name);
    }

    private void addTarget(String name, Target obj) {
        map.put(targetKey(name), obj);
    }

    public Target getTarget(String name) {
        return (Target)map.get(targetKey(name));
    }


    private String prodTargetScorecardKey(String prodId, int targetId) {
        return "pts:" + normalize(prodId) + ":" + targetId;
    }

    public void addProdTargetScorecard(String prodId, int targetId, Scorecard obj) {
        map.put(prodTargetScorecardKey(prodId, targetId), obj);
    }

    public Scorecard getProdTargetScorecard(String prodId, int targetId) {
        return (Scorecard)map.get(prodTargetScorecardKey(prodId, targetId));
    }

    private String cardQstAnswerKey(String cardId, String qstId) {
        return "cqa:" + normalize(cardId) + ":" + normalize(qstId);
    }

    public void addCardQstAnswer(String cardId, String qstId, SurveyAnswer obj) {
        map.put(cardQstAnswerKey(cardId, qstId), obj);
    }

    public SurveyAnswer getCardQstAnswer(String cardId, String qstId) {
        return (SurveyAnswer)map.get(cardQstAnswerKey(cardId, qstId));
    }


    public List<SurveyCategory> getCategories() {
        return this.categoryList;
    }

    public void setCategories(List<SurveyCategory> cats) {
        this.categoryList = cats;
    }


    private String projectNameKey(String name) {
        return "prjname:" + name;
    }

    public void setProjectNameMapping(String oldName, String newName) {
        map.put(projectNameKey(oldName), newName);
    }

    public String getProjectNameMapping(String oldName) {
        return (String)map.get(projectNameKey(oldName));
    }

    private String productNameKey(String name) {
        return "prdname:" + name;
    }

    public void setProductNameMapping(String oldName, String newName) {
        map.put(productNameKey(oldName), newName);
    }

    public String getProductNameMapping(String oldName) {
        return (String)map.get(productNameKey(oldName));
    }


    private String indicatorNameRuleKey(String indicatorName) {
        return "inr:" + indicatorName;
    }

    public void setIndicatorNameRule(String indicatorName, String ruleName) {
        map.put(indicatorNameRuleKey(indicatorName), ruleName);
    }

    public String getIndicatorNameRule(String indicatorName) {
        return (String)map.get(indicatorNameRuleKey(indicatorName));
    }


    private String questionNameRuleKey(String questionName) {
        return "qnr:" + questionName;
    }

    public void setQuestionNameRule(String questionName, String ruleName) {
        map.put(questionNameRuleKey(questionName), ruleName);
    }

    public String getQuestionNameRule(String questionName) {
        return (String)map.get(questionNameRuleKey(questionName));
    }

    private String indicatorRefMappingKey(String name) {
        return "indrefmap:" + name;
    }

    public void setIndicatorRefMapping(String indicatorName, String refName) {
        map.put(indicatorRefMappingKey(indicatorName), refName);
    }

    public String getIndicatorRefMapping(String indicatorName) {
        return (String)map.get(indicatorRefMappingKey(indicatorName));
    }


    private String metaPropKey(String name) {
        return "meta:" + name;
    }

    public void setMetaProperty(String name, String value) {
        map.put(metaPropKey(name), value);
    }


    public String getMetaProperty(String name) {
        return (String)map.get(metaPropKey(name));
    }

}
