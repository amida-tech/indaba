/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;
import com.ocs.indaba.common.Messages;
import org.apache.commons.lang.StringUtils;
/**
 *
 * @author luwb
 */
public class SurveyAnswerSubmitView {
    private boolean succeed;
    private String errorMsg;
    private boolean done;
    private String errorMsgKey;
    private String errorField;

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the succeed
     */
    public boolean isSucceed() {
        return succeed;
    }

    /**
     * @param succeed the succeed to set
     */
    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    /**
     * @return the done
     */
    public boolean isDone() {
        return done;
    }

    /**
     * @param done the done to set
     */
    public void setDone(boolean done) {
        this.done = done;
    }
    /**
     * @parame error message key
     */
    public void setErrorMsgKey(String errMsgKey) {
        this.errorMsgKey = errMsgKey;
    }
    /**
     * @return error message key
     */
    public String getErrorMsgKey() {
        return this.errorMsgKey;
    }
    /**
     * I18N error message
     */
    public void I18NErrorMsg(int langId) {
        if(this.errorMsgKey != null &&
                StringUtils.isNotBlank(this.errorMsgKey)) {
            this.errorMsg = Messages.getInstance().getMessage(this.errorMsgKey, langId);
        }
    }


    public String getErrorField() {
        return errorField;
    }

    
    public void setErrorField(String fieldName) {
        this.errorField = fieldName;
    }
}
