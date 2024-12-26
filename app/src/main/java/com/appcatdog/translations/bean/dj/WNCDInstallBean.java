package com.appcatdog.translations.bean.dj;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WNCDInstallBean implements Serializable {

    private String djId;

    public String getAdSwitch() {
        return adSwitch;
    }

    public void setAdSwitch(String ad_switch) {
        this.adSwitch = ad_switch;
    }

    @SerializedName("ad_switch")
    private String adSwitch;
    @SerializedName("initSdk")
    private String initSdk;

    public String getInitSdk() {
        return initSdk;
    }

    public void setInitSdk(String initSdk) {
        this.initSdk = initSdk;
    }

    public String getDjId() {
        return djId;
    }

    public void setDjId(String djId) {
        this.djId = djId;
    }
}
