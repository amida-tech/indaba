/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.SequenceObject;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class SequenceObjectView {

    private SequenceObject seqObj;
    private List<GoalObjectView> goalObjects;

    public SequenceObjectView() {
    }

    public SequenceObjectView(SequenceObject seqObj, List<GoalObjectView> goalObjects) {
        this.seqObj = seqObj;
        this.goalObjects = goalObjects;
    }

    public List<GoalObjectView> getGoalObjects() {
        return goalObjects;
    }

    public void setGoalObjects(List<GoalObjectView> goalObjects) {
        this.goalObjects = goalObjects;
    }

    public SequenceObject getSeqObj() {
        return seqObj;
    }

    public void setSeqObj(SequenceObject seqObj) {
        this.seqObj = seqObj;
    }
    
}
