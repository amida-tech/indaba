/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class FlagNavView {

    private int horseId;
    private int flagId;   // current flag id
    private FlagWorkView prevFlag;
    private FlagWorkView nextFlag;
    private String exitUrl;
    private String exitUrlDecoded;
    private String homeUrl;

    public void setHorseId(int value) {
        horseId = value;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setFlagId(int value) {
        flagId = value;
    }

    public int getFlagId() {
        return flagId;
    }

    public void setPrevFlag(FlagWorkView flag) {
        prevFlag = flag;
    }

    public FlagWorkView getPrevFlag() {
        return prevFlag;
    }

    public void setNextFlag(FlagWorkView flag) {
        nextFlag = flag;
    }

    public FlagWorkView getNextFlag() {
        return nextFlag;
    }

    public void setExitUrl(String url) {
        exitUrl = url;
    }

    public String getExitUrl() {
        return exitUrl;
    }

    public void setHomeUrl(String url) {
        homeUrl = url;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setExitUrlDecoded(String url) {
        exitUrlDecoded = url;
    }

    public String getExitUrlDecoded() {
        return exitUrlDecoded;
    }
    
}
