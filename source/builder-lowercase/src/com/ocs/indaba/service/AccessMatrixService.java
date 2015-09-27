/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.AccessMatrixDAO;
import com.ocs.indaba.po.AccessMatrix;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class AccessMatrixService {
    
    private AccessMatrixDAO accessMatrixDao = null;
    
    @Autowired
    public void setAccessMatrixDao(AccessMatrixDAO accessMatrixDao) {
        this.accessMatrixDao = accessMatrixDao;
    }
    
    public List<AccessMatrix> getAllAccessMatrixes() {
        return accessMatrixDao.findAll();
    }
}
