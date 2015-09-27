/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.dao.WorksetTargetDAO;
import com.ocs.indaba.aggregation.po.WsTarget;
import com.ocs.indaba.aggregation.vo.HorseBriefVO;
import com.ocs.indaba.aggregation.vo.TargetVO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.builder.dao.TargetDAO;
import com.ocs.indaba.po.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeanbone
 */
public class TargetService {
    private static final Logger log = Logger.getLogger(TargetService.class);
    //private HorseDAO horseDao;
    private TargetDAO targetDao;
    private WorksetTargetDAO wsTargetDao;

    public Map<String, String> getAllHorseTargetMap() {
        return targetDao.selectAllHorseTargetMap();
    }

    public String getTargetByHorseId(int horseId) {
        return targetDao.selectTargetByHorseId(horseId);
    }

    public List<Target> getTargetsByProductIds(List<Integer> productIds) {
        return targetDao.selectTargetsByProductIds(productIds);
    }

    public List<TargetVO> getTargetVOsByProductIdForExport(int productId) {
        List<TargetVO> targetVOs = new ArrayList<TargetVO>();
        List<Target> targets = targetDao.selectTargetsByProductIdForExport(productId);
        for (Target target : targets) {
            TargetVO targetVO = new TargetVO();
            targetVO.setTarget(target);
            targetVO.setTags(getTargetTagsByTargetId(target.getId()));
            targetVOs.add(targetVO);
        }
        return targetVOs;
    }

    public String getTargetTagsByTargetId(int targetId) {
        return targetDao.selectTargetTagTermsByTargetId(targetId).toString().replaceAll("\\[|\\]", "");
    }

    public List<TargetVO> getTargetVOsByProductIds(List<Integer> productIds) {
        List<TargetVO> targetVOs = new ArrayList<TargetVO>();
        List<Target> targets = getTargetsByProductIds(productIds);
        for (Target target : targets) {
            TargetVO targetVO = new TargetVO();
            targetVO.setTarget(target);
            targetVO.setTags(targetDao.selectTargetTagTermsByTargetId(target.getId()).toString().replaceAll("\\[|\\]", ""));
            targetVOs.add(targetVO);
        }
        return targetVOs;
    }

    public String[] getTargetNamesByTargets(List<TargetVO> targets) {
        TreeSet<String> ts = new TreeSet<String>();
        for (TargetVO target : targets) {
            ts.add(target.getTarget().getName());
        }
        return ts.toArray(new String[]{});
    }

    public String[] getTargetTagsByTargets(List<TargetVO> targets) {
        TreeSet<String> ts = new TreeSet<String>();
        for (TargetVO target : targets) {
            ts.addAll(Arrays.asList(target.getTags().split(",")));
        }
        ts.remove("");

        int i = 0;
        String[] tags = new String[ts.size() + 1];
        for (String tag : ts) {
            tags[i] = tag;
            i++;
        }
        tags[i] = "";
        return tags;
    }

    private List<WsTarget> getWsTargetsByWorksetId(int worksetId) {
        return wsTargetDao.selectWsTargetsByWorksetId(worksetId);
    }

    public List<TargetVO> getTargetVOsByWorksetId(int worksetId) {
        List<TargetVO> targetVOs = new ArrayList<TargetVO>();
        List<WsTarget> wsTargets = this.getWsTargetsByWorksetId(worksetId);
        for (WsTarget wt : wsTargets) {
            TargetVO targetVO = new TargetVO();
            Target target = targetDao.get(wt.getTargetId());
            targetVO.setTarget(target);
            targetVO.setTags(targetDao.selectTargetTagTermsByTargetId(target.getId()).toString().replaceAll("\\[|\\]", ""));
            targetVO.setWsTargetId(wt.getId());
            targetVOs.add(targetVO);
        }
        return targetVOs;
    }

    public List<HorseBriefVO> getNotCancelledTargetsForProduct(int projectId) {
        return targetDao.selectNotCancelledTargetsForProduct(projectId);
    }

    public List<HorseBriefVO> getCompletedTargetsForProduct(int projectId) {
        return targetDao.selectCompletedTargetsForProduct(projectId);
    }

    public List<Integer[]> getAllProductAndTargetId() {
        return targetDao.selectAllTargetId();
    }

    /*
    @Autowired
    public void setHorseDao(HorseDAO horseDao) {
        this.horseDao = horseDao;
    }
     *
     */

    @Autowired
    public void setTargetDao(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }

    @Autowired
    public void setWorksetTargetDao(WorksetTargetDAO wsTargetDao) {
        this.wsTargetDao = wsTargetDao;
    }
}
