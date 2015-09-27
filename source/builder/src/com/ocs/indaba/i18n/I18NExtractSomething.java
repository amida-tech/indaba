package com.ocs.indaba.i18n;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.ocs.indaba.util.RegexUtil;

public class I18NExtractSomething {
	@SuppressWarnings("unchecked")
	public static String proeccessAllFiles(String taggedSourceDir, String pattern, String[] fileType) throws IOException {
        Collection<File> collections = FileUtils.listFiles(new File(taggedSourceDir), fileType, true);
        Set<String> jsSet = new HashSet<String>();
        List<String> keyList = new ArrayList<String>();
        int num = 1;
        if (collections != null && !collections.isEmpty()) {
            for (File taggedSrcFile : collections) {
                String filename = taggedSrcFile.getAbsolutePath().replaceAll("\\\\", "/").replace(taggedSourceDir.replaceAll("\\\\", "/"), "");
                if (filename.startsWith("/")) {
                    filename = filename.substring(1);
                }
                String fileContent = FileUtils.readFileToString(taggedSrcFile, "utf-8");
                String[] keysList = RegexUtil.findAllArr(fileContent, pattern, 1);
                if(keysList != null && keysList.length > 0){
                	System.out.println(num+". Have js key, file: "+taggedSrcFile+", keys: "+ Arrays.toString(keysList));
                	num++;
                	for(String key : keysList){
                		if(!jsSet.contains(key)){
                			keyList.add(key);
                			jsSet.add(key);
                		}
                	}
                }
            }
        }
        System.out.println("All js keys, num:"+jsSet.size()+", keys:");
        Collections.sort(keyList);
        String noneCommon = "";
        String noneCommonJS = "";
        int numOfNoneCommonJS = 0;
        for(String key : jsSet){
        	System.out.println(key);
        	if(!key.startsWith("common.")){
        		noneCommon += key + ",";
        	}
        	if(!key.startsWith("common.js.")){
        		numOfNoneCommonJS ++;
        		noneCommonJS += key + ",";
        	}
        }
        System.err.println("noneCommon: "+noneCommon);
        System.err.println("noneCommonJS: "+numOfNoneCommonJS+", "+noneCommonJS);
        return null;
    }

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String pattern_tag = "\\$\\.i18n\\.message.*?\\(\'(.*?)\'";;
		String basepath = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/";
		String[] fileType = new String[]{"jsp", "js", "html"};
		proeccessAllFiles(basepath, pattern_tag, fileType);
	}

}
