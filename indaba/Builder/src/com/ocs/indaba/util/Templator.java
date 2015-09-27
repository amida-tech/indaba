/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.util;

import com.ocs.util.StringUtils;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;

/**
 *
 * @author yc06x
 */
public class Templator {

    private static final String DATE_TOOL_NAME = "DateTool";
    private static final Logger logger = Logger.getLogger(Templator.class);

    private static boolean velocityInitialized = false;

    public static String instantiateText(String text, Map<String,Object> tokenMap) {
        DateTool dateTool = (DateTool) tokenMap.get(DATE_TOOL_NAME);
        if (dateTool == null) {
            dateTool = new DateTool();
            tokenMap.put(DATE_TOOL_NAME, dateTool);
        }

        if (StringUtils.isEmpty(text)) return text;

        if (!velocityInitialized) {
            try {
                Velocity.init();
                velocityInitialized = true;
            } catch (Exception ex) {
                logger.error("Can't initialize velocity: " + ex);
                return text;
            }
        }

        VelocityContext context = new VelocityContext();
        Iterator it = tokenMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            context.put((String) pairs.getKey(), pairs.getValue());
        }

        StringWriter writer = new StringWriter();

        try {
            Velocity.evaluate(context, writer, "TemplateName", text);
        } catch (Exception ex) {
            logger.error("Can't evaluate velocity: " + ex);
            return text;
        }

        String result = writer.toString();

        return result;
    }

}
