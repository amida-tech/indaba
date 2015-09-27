/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Language;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class NoteobjPage extends NoteActionResult {    

    // This list contains all noteobjs applicable to the user.
    // There should be one note widget for each noteobj.
    // If the list is empty, then don't show any note widgets.
    private List<NoteobjView> noteobjs;

    public void setNoteobjs(List<NoteobjView> objs) {
        this.noteobjs = objs;
    }

    public List<NoteobjView> getNoteobjs() {
        return this.noteobjs;
    }

}
