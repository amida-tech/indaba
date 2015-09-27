/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.builder.dao.ProjectDAO;
import com.ocs.indaba.aggregation.vo.ProductVO;
import com.ocs.indaba.po.Project;
import java.util.List;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class ProjectService {

    private ProjectDAO projectDao;

    public List<Integer> getProjectIdsByUsername(String username) {
        return projectDao.selectProjectIdsByUsername(username);
    }

    public List<Integer> getPublicProjectIds() {
        return projectDao.selectPublicProjectIds();
    }

    public String[] getProjectNamesByProductVOs(List<ProductVO> products) {
        TreeSet<String> ts = new TreeSet<String>();
        for (ProductVO productVO : products) {
            ts.add(productVO.getProjectName());
        }
        return ts.toArray(new String[]{});
    }

    public boolean checkUserInProject(int userId, String projectName) {
        return projectDao.checkUserInProject(userId, projectName);
    }

    public Project getProjectByName(String porjectName) {
        return projectDao.selectProjectByName(porjectName);
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }
}
