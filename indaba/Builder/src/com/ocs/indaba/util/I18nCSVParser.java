/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import au.com.bytecode.opencsv.CSVReader;
import com.ocs.indaba.vo.NotificationVO;
import com.ocs.indaba.vo.TextItemVO;
import com.ocs.util.StringUtils;
import common.Logger;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jiangjeff
 */
public class I18nCSVParser {

    private static final Logger logger = Logger.getLogger(I18nCSVParser.class);

    public static List<TextItemVO> parseTextItem(Reader reader) throws IOException {
        List<TextItemVO> textItems = new ArrayList<TextItemVO>();
        CSVReader csvParser = new CSVReader(reader);
        String[] line = null;
        while ((line = csvParser.readNext()) != null) {
            if (line.length < 3) {
                continue;
            }

            TextItemVO txtItem = new TextItemVO();
            txtItem.setTextResourceId(StringUtils.str2int(line[0]));
            txtItem.setTextResourceName(line[1]);
            txtItem.setEnText(line[2]);
            if (line.length > 3) {
                txtItem.setFrText(line[3]);
            }
            textItems.add(txtItem);
        }
        return textItems;
    }

    public static List<NotificationVO> parseNotification(Reader reader) throws IOException {
        List<NotificationVO> notificationList = new ArrayList<NotificationVO>();
        CSVReader csvParser = new CSVReader(reader);
        String[] line = null;
        while ((line = csvParser.readNext()) != null) {
            if (line.length < 6) {
                continue;
            }

            NotificationVO notification = new NotificationVO();
            notification.setNotificationTypeId(StringUtils.str2int(line[0]));
            notification.setEnSubjectText(line[2]);
            notification.setFrSubjectText(line[3]);
            notification.setEnBodyText(line[4]);
            notification.setFrBodyText(line[5]);
            notificationList.add(notification);
        }
        return notificationList;
    }

    public static void main(String args[]) throws IOException {
        //
        byte[] bytes = {108, 111, 97, 100, 116, 120, 116, 114, 115, 114, 99};
        System.out.println(new String(bytes, "UTF-8"));

        //textResources test
        String textResourcesFile = "";
//    	textResourcesFile = "D:\\workspace\\indaba\\trunk\\Builder\\schema\\indaba_i18n_text_resources.csv";
        textResourcesFile = "D:\\workspace\\project_david\\indaba\\trunk\\Builder\\schema\\indaba_i18n_text_resources_cwb_UTF8.csv";
        FileReader fr = new FileReader(new File(textResourcesFile));
        List<TextItemVO> list = parseTextItem(fr);
        logger.debug(list);
        fr.close();

        //notification test
        String notificationFile = "";
//    	notificationFile = "D:\\workspace\\indaba\\trunk\\Builder\\schema\\indaba_i18n_notification_canned_text.csv";
        notificationFile = "D:\\workspace\\project_david\\indaba\\trunk\\Builder\\schema\\indaba_i18n_notification_canned_text_cwb.csv";
        FileReader fr2 = new FileReader(new File(notificationFile));
        List<NotificationVO> list2 = parseNotification(fr2);
        logger.debug(list2);
        fr2.close();
    }
}
