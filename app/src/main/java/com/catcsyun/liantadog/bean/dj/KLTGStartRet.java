package com.catcsyun.liantadog.bean.dj;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xtc on 2018/5/15.
 */
public class KLTGStartRet implements Serializable {


    @SerializedName("ad_chain")
    private String adChain;
    @SerializedName("upgrade")
    private String upgrade;
    @SerializedName("ad_limit_hour")
    private String adLimitHour;
    @SerializedName("ad_location_limit")
    private String adLocationLimit;
    @SerializedName("pop_rate")
    private String popRate;
    @SerializedName("ad_delay")
    private String adDelay;
    @SerializedName("force_upgrade")
    private String forceUpgrade;
    @SerializedName("key_action")
    private String keyAction;
    @SerializedName("ad_show_interval")
    private String adShowInterval;

    @SerializedName("click_trigger")
    private String clickTrigger;
    @SerializedName("pkg_name")
    private String pkgName;
    @SerializedName("app_name")
    private String appName;
    @SerializedName("download_link")
    private String downloadLink;
    @SerializedName("check_flag")
    private String checkFlag;
    @SerializedName("ad_limit_day")
    private String adLimitDay;
    @SerializedName("close_rate")
    private String closeRate;
    @SerializedName("id")
    private String id;
    @SerializedName("app_id")
    private String appId;
    @SerializedName("ad_switch")
    private String adSwitch;
    @SerializedName("baidu_id")
    private String baiduId;
    @SerializedName("motivation_video")
    private String motivationVideo;
    @SerializedName("burying_enable")
    private String buryingEnable;


    @SerializedName("csj_check_flag")
    private String csjCheckFlag;


    @SerializedName("is_white_list")
    private String isWhiteList;

    @SerializedName("riseId")
    private Long riseId;
    @SerializedName("adUnitList")
    private String adUnitList;


    public String getAdUnitList() {
        return adUnitList;
    }

    public void setAdUnitList(String adUnitList) {
        this.adUnitList = adUnitList;
    }
    public Long getRiseId() {
        return riseId;
    }

    public void setRiseId(Long riseId) {
        this.riseId = riseId;
    }


    public String getIsWhiteList() {
        return isWhiteList;
    }

    public void setIsWhiteList(String isWhiteList) {
        this.isWhiteList = isWhiteList;
    }


    public String getCsjCheckFlag() {
        return csjCheckFlag;
    }

    public void setCsjCheckFlag(String csjCheckFlag) {
        this.csjCheckFlag = csjCheckFlag;
    }


    public String getBuryingEnable() {
        return buryingEnable;
    }

    public void setBuryingEnable(String buryingEnable) {
        this.buryingEnable = buryingEnable;
    }
    public String getMotivationVideo() {
        return motivationVideo;
    }

    public void setMotivationVideo(String motivationVideo) {
        this.motivationVideo = motivationVideo;
    }

    public String getDjId() {
        return djId;
    }

    public void setDjId(String djId) {
        this.djId = djId;
    }

    @SerializedName("djId")
    private String djId;
    @SerializedName("lock_switch")
    private String lockSwitch;
    @SerializedName("ban_status")
    private String banStatus;
    @SerializedName("curr_channel")
    private String currChannel;

    public String getCurrChannel() {
        return currChannel;
    }

    public void setCurrChannel(String currChannel) {
        this.currChannel = currChannel;
    }
    public String getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }
    public String getLockSwitch() {
        return lockSwitch;
    }

    public void setLockSwitch(String lockSwitch) {
        this.lockSwitch = lockSwitch;
    }
    public String getAdChain() {
        return adChain;
    }

    public void setAdChain(String adChain) {
        this.adChain = adChain;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    public String getAdLimitHour() {
        return adLimitHour;
    }

    public void setAdLimitHour(String adLimitHour) {
        this.adLimitHour = adLimitHour;
    }

    public String getAdLocationLimit() {
        return adLocationLimit;
    }

    public void setAdLocationLimit(String adLocationLimit) {
        this.adLocationLimit = adLocationLimit;
    }

    public Object getPopRate() {
        return popRate;
    }

    public void setPopRate(String popRate) {
        this.popRate = popRate;
    }

    public String getAdDelay() {
        return adDelay;
    }

    public void setAdDelay(String adDelay) {
        this.adDelay = adDelay;
    }

    public String getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(String forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getKeyAction() {
        return keyAction;
    }

    public void setKeyAction(String keyAction) {
        this.keyAction = keyAction;
    }

    public String getAdShowInterval() {
        return adShowInterval;
    }

    public void setAdShowInterval(String adShowInterval) {
        this.adShowInterval = adShowInterval;
    }


    public String getClickTrigger() {
        return clickTrigger;
    }

    public void setClickTrigger(String clickTrigger) {
        this.clickTrigger = clickTrigger;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    public String getAdLimitDay() {
        return adLimitDay;
    }

    public void setAdLimitDay(String adLimitDay) {
        this.adLimitDay = adLimitDay;
    }

    public String getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(String closeRate) {
        this.closeRate = closeRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAdSwitch() {
        return adSwitch;
    }

    public void setAdSwitch(String adSwitch) {
        this.adSwitch = adSwitch;
    }

    public String getBaiduId() {
        return baiduId;
    }

    public void setBaiduId(String baiduId) {
        this.baiduId = baiduId;
    }

    @SerializedName("showId")
    private String showId;

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }


    @Override
    public String toString() {
        return "StartRet{" +
                "adChain='" + adChain + '\'' +
                ", upgrade='" + upgrade + '\'' +
                ", adLimitHour=" + adLimitHour +
                ", adLocationLimit=" + adLocationLimit +
                ", popRate=" + popRate +
                ", adDelay=" + adDelay +
                ", forceUpgrade=" + forceUpgrade +
                ", keyAction='" + keyAction + '\'' +
                ", adShowInterval=" + adShowInterval +
                ", clickTrigger='" + clickTrigger + '\'' +
                ", pkgName='" + pkgName + '\'' +
                ", appName='" + appName + '\'' +
                ", downloadLink=" + downloadLink +
                ", checkFlag='" + checkFlag + '\'' +
                ", adLimitDay=" + adLimitDay +
                ", closeRate=" + closeRate +
                ", id=" + id +
                ", appId='" + appId + '\'' +
                ", adSwitch='" + adSwitch + '\'' +
                '}';
    }
}
