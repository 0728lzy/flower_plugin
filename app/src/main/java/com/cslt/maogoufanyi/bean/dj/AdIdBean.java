package com.cslt.maogoufanyi.bean.dj;

import java.io.Serializable;

public class AdIdBean implements Serializable {

    public  Long  ad_unit_id;  //广告id
    public  String  app_name;  //应用名称

    public Long has_experiment;
    public String ad_unit_name; //广告id 描述
    public String ad_unit_type; //广告id 类型
    public String os_type; //系统类型
    public Long app_id;
    public Long status;
}
