/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.intl;

import com.ocs.util.StringUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yc06x
 */
public class ResourceManager {

    private Map<Integer, Language> langIdMap = null;
    private Map<String, Language> langNameMap = null;
    private Language defaultLang = null;

    // this map contains all resources
    // Each language ID has a resource table
    // Map key is the language ID, map value is the resource table
    private Map<Integer, Map<String, String>> allResourceMap = null;

    // This map contains resources that match specified regular expression
    // Map key is the regular expression, map value is a resource map
    private Map<String, Map<Integer, Map<String, String>>> regxResourceMap = null;

    // The resources of default language
    private Map<String, String> defaultMap = null;

    ResourceManager(
            Map<Integer, Language> langIdMap,
            Map<String, Language> langNameMap,
            Language defLang,
            Map<Integer, Map<String, String>> resourceMap) {

        this.langIdMap = langIdMap;
        this.langNameMap = langNameMap;
        defaultLang = defLang;
        defaultMap = resourceMap.get(defLang.getId());
        allResourceMap = resourceMap;
        regxResourceMap = new HashMap<String, Map<Integer, Map<String, String>>>();        
    }

    public Language getLanguage(int id) {
        if (langIdMap == null) return null;

        return langIdMap.get(id);
    }

    public Language getLanguageByName(String name) {
        if (langNameMap == null) return null;

        return langNameMap.get(name);
    }

    public Language getDefaultLanguage() {
        return defaultLang;
    }

    public String getResource(String key, int langId) {
        if (allResourceMap == null) return null;

        String result = null;
        Map<String, String> langRsrcMap = allResourceMap.get(langId);

        result = (langRsrcMap == null) ? null : langRsrcMap.get(key);

        if (result == null && langRsrcMap != defaultMap) {
            // preferred language is not available. Fallback to default.
            result = defaultMap.get(key);
        }

        if (StringUtils.isEmpty(result)) {
            result = key;
        }

        return result;
    }


    public String formatMessage(String key, int langId, Object... args) {
        String resource = getResource(key, langId);
        return MessageFormat.format(resource, args);
    }


    private Map<String, String> createResourceMap(int langId, String regex) {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> langRsrcMap = allResourceMap.get(langId);
        Set<String> keys = defaultMap.keySet();
        String value;

        for (String k : keys) {
            if (!k.matches(regex)) {
                continue;
            }
            value = (langRsrcMap != null) ? langRsrcMap.get(k) : null;

            if (value == null && langRsrcMap != defaultMap) {
                // use default resource
                value = defaultMap.get(k);
            }

            result.put(k, value);
        }
        
        return result;
    }

    public Map<String, String> getResourcesRegex(int langId, String regex) {

        Map<Integer, Map<String, String>> langMap = regxResourceMap.get(regex);
        Map<String, String> resourceMap;

        if (langMap == null) {
            // create the map based on resource keys in the default map
            langMap = new HashMap<Integer, Map<String, String>>();

            resourceMap = createResourceMap(langId, regex);

            langMap.put(langId, resourceMap);

            regxResourceMap.put(regex, langMap);
        } else {
            resourceMap = langMap.get(langId);

            if (resourceMap == null) {
                resourceMap = createResourceMap(langId, regex);
                langMap.put(langId, resourceMap);
            }
        }

        return resourceMap;
    }
}
