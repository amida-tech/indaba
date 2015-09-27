/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.intl;

import au.com.bytecode.opencsv.CSVReader;
import com.ocs.ssu.SmartCsvReader;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yc06x
 */
public class ResourceLoader {

    private static final String LANGUAGE_LIST_FILE = "languages.csv";

    private static final int LANGUAGE_LIST_COL_COUNT = 4;
    private static final int LANGUAGE_LIST_COL_POS_ID = 0;
    private static final int LANGUAGE_LIST_COL_POS_NAME = 1;
    private static final int LANGUAGE_LIST_COL_POS_SHORT_NAME = 2;
    private static final int LANGUAGE_LIST_COL_POS_DEFAULT = 3;

    private static final String LANGUAGE_LIST_COL_LABEL_ID = "ID";
    private static final String LANGUAGE_LIST_COL_LABEL_NAME = "NAME";
    private static final String LANGUAGE_LIST_COL_LABEL_SHORT_NAME = "SHORT NAME";
    private static final String LANGUAGE_LIST_COL_LABEL_DEFAULT = "DEFAULT";

    private static final int RESOURCE_COL_MIN_COUNT = 2;
    private static final int RESOURCE_COL_POS_KEY = 0;
    private static final String RESOURCE_COL_LABEL_KEY = "KEY";


    private class ResourceInfo {
        File file;
        int lineNum;

        ResourceInfo(File file, int lineNum) {
            this.file = file;
            this.lineNum = lineNum;
        }
    }

    private class FileInfo {
        File file;
        String langName;
        int langId = 0;

        FileInfo(File file, String langName) {
            this.file = file;
            this.langName = langName;
        }
    }

    private Map<Integer, Map<String, ResourceInfo>> infoMap;
    private Map<Integer, Language> langIdMap;
    private Map<String, Language> langNameMap;
    private Language defaultLang;
    private Map<Integer, Map<String, String>> resourceMap = null;
    private List<String> errors = null;
    private List<String> warnings = null;

    public ResourceLoader() {
        infoMap = new HashMap<Integer, Map<String, ResourceInfo>>();
        langIdMap = new HashMap<Integer, Language>();
        langNameMap = new HashMap<String, Language>();
        resourceMap = new HashMap<Integer, Map<String, String>>();
        errors = new ArrayList<String>();
        warnings = new ArrayList<String>();
        defaultLang = null;
    }

    public ResourceManager load(String resourceDir) {
        File dir = new File(resourceDir);

        if (dir == null) {
            errors.add("Bad resource directory: " + resourceDir);
            return null;
        }

        File dirList[] = dir.listFiles();

        if (dirList == null) {
            errors.add("Empty resource directory: " + resourceDir);
            return null;
        }

        File langFile = null;
        List<FileInfo> fileList = new ArrayList<FileInfo>();

        for (File file : dirList) {
            String fileName = file.getName();

            if (fileName.endsWith((".csv"))) {
                if (fileName.equalsIgnoreCase(LANGUAGE_LIST_FILE)) {
                    langFile = file;
                } else {
                    // check name: text.lang.csv
                    String parts[] = fileName.split("[.]");

                    if (parts.length < 3 || !"text".equalsIgnoreCase(parts[0])) {
                        errors.add("Bad resource file name: " + fileName);
                    } else {
                        fileList.add(new FileInfo(file, parts[1]));
                    }
                }
            }
        }

        if (langFile == null) {
            errors.add("Missing language file " + LANGUAGE_LIST_FILE);
        } else {
            loadLanguages(langFile);
        }

        if (langIdMap.isEmpty()) {
            errors.add("No languages specified in language file " + LANGUAGE_LIST_FILE);
            return null;
        }

        if (defaultLang == null) {
            errors.add("Default language is not specified in language file " + LANGUAGE_LIST_FILE);
            return null;
        }

        if (fileList.size() == 0) {
            errors.add("No resources found in " + resourceDir);
            return null;
        }

        // now process resource files
        for (FileInfo info : fileList) {
            // see if this is a valid resource file
            if (!info.langName.equalsIgnoreCase("all")) {
                Language lang = getLanguage(info.langName);
            
                if (lang == null) {
                    errors.add("Bad language name in file name: " + info.file.getName());
                    continue;
                }

                info.langId = lang.getId();
            } else {
                info.langId = 0;
            }
            
            loadResources(info);
        }

        // make sure at least default map is available
        Map<String, String> defaultResourceMap = resourceMap.get(defaultLang.getId());

        if (defaultResourceMap == null || defaultResourceMap.isEmpty()) {
            errors.add("Resources not defined for default language " + defaultLang.getName());
        } 

        if (errors.size() > 0) {
            // there are errors
            return null;
        }

        return new ResourceManager(langIdMap, langNameMap, defaultLang, resourceMap);
    }


    private Language getLanguage(String name) {
        if (name == null) return null;        
        return langNameMap.get(name.toLowerCase());
    }


    static private class LangCtx {
        Language lang;
        int colNum;
        Map<String, ResourceInfo> langInfoMap;
        Map<String, String> langResourceMap;
    }


    private LangCtx makeLangCtx(Language lang, int colNum) {
        LangCtx ctx = new LangCtx();
        ctx.lang = lang;
        ctx.colNum = colNum;

        ctx.langInfoMap = infoMap.get(lang.getId());
        if (ctx.langInfoMap == null) {
            ctx.langInfoMap = new HashMap<String, ResourceInfo>();
            infoMap.put(lang.getId(), ctx.langInfoMap);
        }

        ctx.langResourceMap = resourceMap.get(lang.getId());

        if (ctx.langResourceMap == null) {
            ctx.langResourceMap = new HashMap<String, String>();
            resourceMap.put(lang.getId(), ctx.langResourceMap);
        }

        return ctx;
    }

    private void loadResources(FileInfo info) {
        SmartCsvReader reader = null;
        List<LangCtx> langCtxList = new ArrayList<LangCtx>();

        try {
            reader = new SmartCsvReader(new CSVReader(new InputStreamReader(new FileInputStream(info.file), "UTF-8")));

            // The resource file has 2 or more columns: KEY, lang1, lang2. lang1 is usually the source of translation
            // One of the language columns must match the lang name in the file name
            String[] row;

            row = reader.readNext();

            // Look for header line
            if (row.length < RESOURCE_COL_MIN_COUNT) {
                addError("Bad header in resource file: columns != " + LANGUAGE_LIST_COL_COUNT,
                        info.file, reader.getLineNumber());
                return;
            }

            for (int i = 0; i < row.length; i++) {
                if (row[i].isEmpty()) {
                    addError("Bad header in resource file: missing columns.", info.file, reader.getLineNumber());
                    return;
                }

                if (i != RESOURCE_COL_POS_KEY) {
                    // must be a language name - try to find the corresponding id
                    Language lang = getLanguage(row[i]);

                    if (lang == null) continue;

                    if (info.langId > 0) {
                        // only load one language in this file
                        if (lang.getId() == info.langId) {
                            langCtxList.add(makeLangCtx(lang, i));
                            break;
                        }
                    } else {
                        // all languages
                        langCtxList.add(makeLangCtx(lang, i));
                    }
                }
            }

            if (!RESOURCE_COL_LABEL_KEY.equalsIgnoreCase(row[RESOURCE_COL_POS_KEY])) {
                addError("Bad header in languages file: wrong KEY column name.", info.file, reader.getLineNumber());
                return;
            }

            if (langCtxList.isEmpty()) {
                addError("Bad header in languages file: no column for target language " + info.langName,
                        info.file, reader.getLineNumber());
                return;
            }

            // read each resource
            while ((row = reader.readNext()) != null) {
                if (row.length < RESOURCE_COL_POS_KEY+1 || StringUtils.isEmpty(row[RESOURCE_COL_POS_KEY])) {
                    addWarning("Missing key", info.file, reader.getLineNumber());
                    continue;
                }

                String key = row[RESOURCE_COL_POS_KEY];

                // get each language
                for (LangCtx ctx : langCtxList) {
                    int targetLangCol = ctx.colNum;

                    if (row.length < targetLangCol+1 || StringUtils.isEmpty(row[targetLangCol])) {
                        addWarning("Missing text for language " + ctx.lang.getName(), info.file, reader.getLineNumber());
                        continue;
                    }

                    String text = row[targetLangCol];

                    // see whether this resource has been defined for this key
                    ResourceInfo ri = ctx.langInfoMap.get(key);
                    if (ri != null) {
                        // the same resource has been defined already
                        errors.add("Duplicate resource definition for key '" + key + "': " +
                            ri.file.getName() + ":" + ri.lineNum + " AND " +
                            info.file.getName() + ":" + reader.getLineNumber());
                    } else {
                        ri = new ResourceInfo(info.file, reader.getLineNumber());
                        ctx.langInfoMap.put(key, ri);
                        ctx.langResourceMap.put(key, text);
                    }
                }
            }
        } catch (Exception ex) {
            errors.add("Can't open CSV reader for file: " + info.file.getName());
        } finally {
            if (reader != null) reader.close();
        }
    }

    private boolean checkLanguageHeaderLine(String[] row) {
        if (row.length < LANGUAGE_LIST_COL_COUNT) {
            errors.add("Bad header in languages file: columns != " + LANGUAGE_LIST_COL_COUNT);
            return false;
        }

        for (int i = 0; i < row.length; i++) {
            if (row[i].isEmpty()) {
                errors.add("Bad header in languages file: missing columns.");
                return false;
            }
        }

        if (!LANGUAGE_LIST_COL_LABEL_ID.equalsIgnoreCase(row[LANGUAGE_LIST_COL_POS_ID])) {
            errors.add("Bad header in languages file: wrong ID column name.");
            return false;
        } else if (!LANGUAGE_LIST_COL_LABEL_NAME.equalsIgnoreCase(row[LANGUAGE_LIST_COL_POS_NAME])) {
            errors.add("Bad header in languages file: wrong NAME column name.");
            return false;
        } else if (!LANGUAGE_LIST_COL_LABEL_SHORT_NAME.equalsIgnoreCase(row[LANGUAGE_LIST_COL_POS_SHORT_NAME])) {
            errors.add("Bad header in languages file: wrong SHORT NAME column name.");
            return false;
        } else if (!LANGUAGE_LIST_COL_LABEL_DEFAULT.equalsIgnoreCase(row[LANGUAGE_LIST_COL_POS_DEFAULT])) {
            errors.add("Bad header in languages file: wrong DEFAULT column name.");
            return false;
        } else {
            return true;
        }
    }

    private void loadLanguages(File file) {
        SmartCsvReader reader = null;
        try {
            reader = new SmartCsvReader(new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8")));

            // The langauge file has 4 columns: ID, NAME, SHORT NAME, DEFAULT
            String[] row;

            row = reader.readNext();

            // Look for header line
            if (!checkLanguageHeaderLine(row)) {
                return;
            }

            // read each language
            while ((row = reader.readNext()) != null) {
                if (row.length < LANGUAGE_LIST_COL_POS_ID+1 || StringUtils.isEmpty(row[LANGUAGE_LIST_COL_POS_ID])) {
                    addError("Missing language ID", file, reader.getLineNumber());
                    continue;
                }

                if (row.length < LANGUAGE_LIST_COL_POS_NAME+1 || StringUtils.isEmpty(row[LANGUAGE_LIST_COL_POS_NAME])) {
                    addError("Missing language NAME", file, reader.getLineNumber());
                    continue;
                }

                if (row.length < LANGUAGE_LIST_COL_POS_SHORT_NAME+1 || StringUtils.isEmpty(row[LANGUAGE_LIST_COL_POS_SHORT_NAME])) {
                    addError("Missing language ID", file, reader.getLineNumber());
                    continue;
                }

                int langId = StringUtils.str2int(row[LANGUAGE_LIST_COL_POS_ID], -1);
                if (langId < 0) {
                    addError("Bad language ID " + row[LANGUAGE_LIST_COL_POS_ID], file, reader.getLineNumber());
                    continue;
                }

                boolean isDefault = false;
                if (row.length >= LANGUAGE_LIST_COL_POS_DEFAULT+1 && !StringUtils.isEmpty(row[LANGUAGE_LIST_COL_POS_DEFAULT])) {
                    isDefault = true;
                }

                String name = row[LANGUAGE_LIST_COL_POS_NAME];
                String shortName = row[LANGUAGE_LIST_COL_POS_SHORT_NAME];

                Language lang = new Language(langId, name, shortName, isDefault);

                langIdMap.put(langId, lang);
                langNameMap.put(name, lang);
                langNameMap.put(shortName, lang);

                if (isDefault) defaultLang = lang;
            }
            
        } catch (Exception ex) {
            errors.add("Can't open CSV reader for file: " + file.getName());
        } finally {
            if (reader != null) reader.close();
        }
    }


    private void addError(String msg, File file, int lineNum) {
        errors.add(msg + " (" + file.getName() + ": " + lineNum + ")");
    }

    private void addWarning(String msg, File file, int lineNum) {
        warnings.add(msg + " (" + file.getName() + ": " + lineNum + ")");
    }


    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }


    public static void main(String[] args){
        ResourceLoader loader = new ResourceLoader();

        ResourceManager mgr = loader.load("/Users/yc06x/intltext");

        List<String> errs = loader.getErrors();

        System.out.println("Number of Errors: " + errs.size());

        for (String err : errs) {
            System.out.println(err);
        }

        errs = loader.getWarnings();

        System.out.println("Number of Warnings: " + errs.size());

        for (String err : errs) {
            System.out.println(err);
        }
    }
}
