/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.i18n;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Jeff
 */
public class I18nSourceFiles {

    private static final String BASEPATH = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/";
    private static final String SQL_INSERT_I18N_PAGE = "INSERT INTO source_file(`filename`, `path`, `extension`, `status`, `note`) VALUES(''{0}'', ''{1}'', ''{2}'', {3}, ''{4}'');";

    public Collection listFiles(String dir) {
        return listFiles(dir, false);
    }
    public Collection listFiles(String dir, boolean recursive) {
        //System.out.println("List files under folder: " + dir);
        return FileUtils.listFiles(new File(dir), new String[]{"jsp", "html", "js", "java", "properties"}, recursive);
    }

    public String generateSQL(File file) {
        return MessageFormat.format(SQL_INSERT_I18N_PAGE, file.getName(), 
                file.getParent().replaceAll("\\\\", "/").replaceAll(BASEPATH, ""), 
                getExtension(file.getName()), 1, "");
    }
    
    private String getExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(index + 1);
    }
    public void output(Collection files) {
        Iterator it = files.iterator();
        int count = 0;
        while (it.hasNext()) {
            File f = (File) it.next();
            String filename = f.getName();
            if (filename.startsWith("jquery") || filename.startsWith("FusionCharts") || filename.startsWith("log4j")) {
                continue;
            }
            ++count;
            System.out.println(generateSQL(f));
        }
        //System.out.println("Total: " + count);
    }

    public static void main(String args[]) {
        final String[] paths = {"web", "web/js"};
        I18nSourceFiles instance = new I18nSourceFiles();
        //System.out.println(MessageFormat.format(SQL_INSERT_I18N_PAGE, "common", "", "common", "1", "Virtual file for common resources."));
        //instance.output(instance.listFiles(BASEPATH + "web"));
        instance.output(instance.listFiles(BASEPATH + "web/sample"));
        instance.output(instance.listFiles(BASEPATH + "web/popups"));
        //instance.output(instance.listFiles(BASEPATH + "web/js"));
        //instance.output(instance.listFiles(BASEPATH + "src"));
        //instance.output(instance.listFiles(BASEPATH + "src/com/ocs/indaba/action"));
        //instance.output(instance.listFiles(BASEPATH + "src/com/ocs/indaba/tag"));
    }
}
