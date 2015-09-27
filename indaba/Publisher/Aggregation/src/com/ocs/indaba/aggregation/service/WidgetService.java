/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.dao.WidgetDAO;
import com.ocs.indaba.aggregation.po.Widget;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class WidgetService {

    private WidgetDAO widgetDao = null;

    public List<Widget> getAllWidgets() {
        return widgetDao.findAll();
    }

    public Widget getWidget(int id) {
        return widgetDao.get(id);
    }

    public void updateWidget(Widget widget) {
       widgetDao.update(widget);
    }

    @Autowired
    public void setWidgetDao(WidgetDAO widgetDao) {
        this.widgetDao = widgetDao;
    }
}
