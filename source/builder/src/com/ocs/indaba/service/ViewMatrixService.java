/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.ViewMatrixDAO;
import com.ocs.indaba.po.ViewMatrix;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class ViewMatrixService {
    
    private ViewMatrixDAO viewMatrixDao = null;
    
    @Autowired
    public void setViewMatrixDao(ViewMatrixDAO viewMatrixDao) {
        this.viewMatrixDao = viewMatrixDao;
    }
    
    public List<ViewMatrix> getAllViewMatrixes() {
        return viewMatrixDao.findAll();
    }
}
