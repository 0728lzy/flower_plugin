package com.ruiteapp.pettranslator.utils.dj;


/**
 * 描 述:
 */
public class UserInfoModel {
    //token
    private final static String TOKEN = "access_token";
    //存储每一次接口获取时间
    private final static String STORAGE_TIME = "storage_time";

    //存储展示cp时间
    private final static String SHOW_CHAPING_YYN_TIME = "chaping_yyn_time";

    //首页插屏2间隔时间
    private final static String SHOW_CHAPING_HOME_YYN_TIME2 = "chaping_home_yyn_time_2";

    //首页激励间隔时间
    private final static String SHOW_JL_HOME_YYN_TIME= "jl_home_yyn_time";

    private final static String SHOW_CHAPING_ALL_TYPE_TIME = "chaping_all_type_time";

    //存储展示xxl时间
    private final static String SHOW_XINXILUI_YYN_TIME = "xinxilui_yyn_time";

    //存储展示xxl时间
    private final static String SHOW_XINXILUI_YYN_TIME2 = "xinxilui_yyn_time2";

    //设备号
    private final static String DEVICE_ID = "deviceId";
    private final static String START_HTTP_RESPON = "start_response_data";//responseData

    //纪录首次安装时间
    private final static String APK_INSTALL_TIME = "apk_installation_time";
    //纪录广告显示次数
    private final static String AD_SHOW_COUNT = "ad_show_count";
    //djId
    private final static String DJID = "djId";

    //放大的图片
    private final static String ENLARGE_PHOTO = "enlarge_photo";
    //结果页面存储数据
    private final static String RESULTP_ACTIVITY_DATA = "result_activity_data";
    //点击时间村存储
    private final static String RESULTP_ACTIVITY_TIME = "result_activity_time";

    //点击时间村存储
    private final static String IS_FIRST_TIME = "is_first_time";
    //白名单记录
    private final static String IS_WHITE_LIST = "is_white_list";
    //是否开启了动态壁纸
    private final static String IS_DYNAMIC_WALLPAPER = "dynamic_wallpaper";
    //是否开启了动态壁纸
    private final static String IS_DYNAMIC_WALLPAPER_TIME = "dynamic_wallpaper_time";
    //是否自动管理
    private final static String STARTUP_MANAGEMENT = "startup_management";
    //后台保护
    private final static String BACKGROUND_PROTECTION = "background_protection";
    //锁屏资讯
    private final static String LOCK_SCREEN_INFORMATION = "lock_screen_information";
    //个性化推送
    private final static String PERSONALIZED_PUSH = "personalized_push";
    //自动释放内存
    private final static String AUTOMATIC_POWER_SAVING = "automatic_power_saving";
    //自动降温
    private final static String AUTOMATIC_COOLING = "automatic_cooling";
    //通知栏功能
    private final static String NOTIFICATION_FUNCTION = "notification_function";
    //网络检测
    private final static String NETWORK_DETECTION = "network_detection";

    private final static String FIRST_SHOW_PERMISSIONS_DIALOG = "first_show_permissions_dialog";

    private final static String SHOW_APP_INSTALL_TIME = "show_app_install";
    private final static String CLICK_JL_TIME = "click_jl_time";
    //记录一天的插屏和全屏弹窗个数
    private final static String IS_TO_DAY_AD_SHOW_TOTAL = "is_to_day_ad_show_total";
    //设置当天的时间
    private final static String TO_DAT_TIME = "to_day_time";
    //记录第一次进来是不是开启广告
    private final static String IS_SHOW_AD_ONE = "is_show_ad_one";
    private final static String IS_SHOW_AD= "is_show_ad";

    //个人中心关闭以后的时间 超过15天就重新设置
    private final static String SET_15_TIME = "set_15_time";


    private final static String  MEMBER_WEEK_TIME = "member_week_time";


    //是否开启了小组件
    private final static String IS_DYNAMIC_WIDGETS_TIME = "dynamic_widgets_time";
    //是否开启了动态壁纸
    private final static String DESKTOP_WIDGETS_TIME = "desktop_widgets_time";

    //存储信息流外展示xxl时间
    private final static String FLOW_OUT_POP = "flow_out_pop";
    private final static String LOCK_SCREEN = "lock_screen";
    private final static String BAN_STATUS = "ban_status";
    //信息流弹窗时间记录
    private final static String INFORMATION_FLOW_TIME = "Information_flow_time";
    private final static String SET_UNUSUAL_ACTION_IP = "set_unusual_action_ip";
    private final static String DOWNLOAD_SPEED = "download_speed";
    private final static String PHONE_SPEED_TIME = "phone_speed_time";
    private final static String IS_FIRST_TIME_TWO = "is_first_time_TWO";
    private final static String SHOW_KAIPING_YYN_TIME = "chaping_kp_time";
    //存储展示kp预加载时间
    private final static String SHOW_KP_Yu_TIME = "kp_yyn_time";
    //记录首页功能每天
    private final static String HOME_FUNCTION_ONE = "home_function_one";
    private final static String HOME_FUNCTION_TWO = "home_function_two";
    private final static String HOME_FUNCTION_THREE = "home_function_three";
    private final static String HOME_FUNCTION_FOUR = "home_function_four";
    private final static String HOME_FUNCTION_FIVE= "home_function_five";
    private final static String HOME_FUNCTION_SIX= "home_function_six";
    private final static String HOME_FUNCTION_SEVEN= "home_function_seven";
    private final static String HOME_FUNCTION_EIGHT= "home_function_eight";
    //记录每天的时间戳，进行判断是否为次日
    private final static String CURRENT_DAY_TIME = "current_day_time";
    //记录手机使用的天数
    private final static String SAVE_USE_APP_DAY_COUNT = "save_use_app_day_count";
    //记录每天使用测速的次数
    private final static String CURRENT_DAY_USE_NETWORK_SPEED = "current_day_use_network_speed";

    //记录每天使用wifi加速的次数
    private final static String CURRENT_DAY_WIFI_SPEED = "current_day_wifi_speed";
    //记录每天使用手机流量数据
    private final static String SAVE_CURRENT_DAY_MOBILE_FLOW = "save_current_day_mobile_flow";


    //记录昨天使用WIFI流量数据
    private final static String SAVE_CURRENT_YESTERDAY_WIFI_FLOW = "save_current_yesterday_wifi_flow";
    //记录昨天使用手机流量数据
    private final static String SAVE_CURRENT_YESTERDAY_MOBILE_FLOW = "save_current_yesterday_mobile_flow";
    //wifi速度可提升数据
    private final static String WIFI_IMPROVE_DATA = "wifi_improve_data";
    //记录每天使用WIFI流量数据
    private final static String SAVE_CURRENT_DAY_WIFI_FLOW = "save_current_day_wifi_flow";
    //用于记录使用功能的次数，超过2次任意功能，判断未添加小组件的，弹出引导添加小组件
    private final static String CURRENT_DAY_RECORD_USE_FUNCTION = "current_day_record_use_function";
    private final static String RESULT_TIME= "result_time";
    private final static String IS_CURR_CHANNEL= "is_curr_channel";
    //杀掉进程的app
    private final static String TIMING_APP_LIST = "timing_app_list";
    private final static String IS_SETING_LOCATION = "is_seting_location";
    private final static String SHOW_KAIPING_YYN_TIME2 = "chaping_kp_time_two";

    private final static String SHOW_KP_Yu_TIME2 = "kp_yyn_time_two";

    private final static String SHOW_DIALOG_WAY_BURRY = "show_way_burry";

    private final static String LOAD_NATIVE_TIME = "native_time";

    //红包金额
    private final static String MONEY_UI_ACCOUNT = "money_ui_account";

    //记录第一次进入红包界面时间
    private final static String MONEY_UI_FIRST_TIME = "money_ui_first_time";

    //记录红包相关数据
    private final static String MONEY_UI_RECORD_DATA = "money_ui_record_data";

    //记录当天的领红包次数
    private final static String MONEY_UI_MONEY_COUNT = "money_ui_money_count";

    //记录第一次进入红包界面弹出抽奖弹窗
    private final static String MONEY_UI_FIRST_LOTTERY = "money_ui_first_lottery";

    //记录首页弹出一次红包弹窗
    private final static String IS_SHOW_HONGBAO_DIALOG = "is_show_hongbao_dialog";

    private final static String IS_SHOW_MEMBER_DIALOG = "is_show_member_dialog";

    //每日打卡记录第一次检测时间
    private final static String EVERY_DAY_TIME = "every_day_time";

    //记录每天进入红包界面时间
    private final static String EVERY_INTO_MONEY_DAY_TIME = "every_into_money_day_time";

    private final static String CHECK_FLAG = "is_check_flag";
    private final static String BURYING_ENABLE = "burying_enable";
    private final static String IS_LOCAL_SHOW_AD= "is_local_show_ad";
    private final static String IS_WHITE_LIST_STATE= "is_white_list_state";
    private final static String RISE_ID = "riseId";


    private final static String OAIDU = "oaid_u";
    private final static String OAIDH = "oaid_h";
    private final static String OAID = "oaid";

    public static void setOaid(String oaid) {
        SPUtils.getInstance().setString(OAID, oaid);
    }
    public static String getOaid() {
        return SPUtils.getInstance().getString(OAID,"");
    }


    public static void setOaidU(String oaid) {
        SPUtils.getInstance().setString(OAIDU, oaid);
    }
    public static String getOaidU() {
        return SPUtils.getInstance().getString(OAIDU,"");
    }
    public static void setOaidH(String oaid) {
        SPUtils.getInstance().setString(OAIDH, oaid);
    }
    public static String getOaidH() {
        return SPUtils.getInstance().getString(OAIDH,"");
    }

    public static void setIsWhiteListState(String state) {
        SPUtils.getInstance().setString(IS_WHITE_LIST_STATE, state);
    }
    public static String getIsWhiteListState() {
        return SPUtils.getInstance().getString(IS_WHITE_LIST_STATE,"");
    }
    public static void setRiseId(String state) {
        SPUtils.getInstance().setString(RISE_ID, state);
    }
    public static String getRiseId() {
        return SPUtils.getInstance().getString(RISE_ID,"");
    }


    public static void setIsLocalShowAd(boolean isShowAd) {
        SPUtils.getInstance().setBoolean(IS_LOCAL_SHOW_AD, isShowAd);
    }

    public static Boolean getIsLocalShowAd() {
        return SPUtils.getInstance().getBoolean(IS_LOCAL_SHOW_AD, false);
    }

    public static Boolean getBuryingEnable() {
        return SPUtils.getInstance().getBoolean(BURYING_ENABLE, true);
    }

    public static void setBuryingEnable(boolean token) {
        SPUtils.getInstance().setBoolean(BURYING_ENABLE, token);
    }
    public static Boolean getIsCheckFlag() {
        return SPUtils.getInstance().getBoolean(CHECK_FLAG, true);
    }

    public static void setIsCheckFlag(boolean token) {
        SPUtils.getInstance().setBoolean(CHECK_FLAG, token);
    }
    public static void setEveryDayTime(long ts) {
        SPUtils.getInstance().setLong(EVERY_DAY_TIME, ts);
    }

    public static long getEveryDayTime() {
        return SPUtils.getInstance().getLong(EVERY_DAY_TIME);
    }
    public static void setEveryIntoMoneyDayTime(long ts) {
        SPUtils.getInstance().setLong(EVERY_INTO_MONEY_DAY_TIME, ts);
    }

    public static long getEveryIntoMoneyDayTime() {
        return SPUtils.getInstance().getLong(EVERY_INTO_MONEY_DAY_TIME);
    }

    public static void setMoneyUiFirstLottery(long ts) {
        SPUtils.getInstance().setLong(MONEY_UI_FIRST_LOTTERY, ts);
    }

    public static long getMoneyUiFirstLottery() {
        return SPUtils.getInstance().getLong(MONEY_UI_FIRST_LOTTERY);
    }


    public static void setMoneyUiFirstTime(long ts) {
        SPUtils.getInstance().setLong(MONEY_UI_FIRST_TIME, ts);
    }

    public static long getMoneyUiFirstTime() {
        return SPUtils.getInstance().getLong(MONEY_UI_FIRST_TIME);
    }


    public static void setMoneyUiAccount(float ts) {
        SPUtils.getInstance().setFloat(MONEY_UI_ACCOUNT, ts);
    }

    public static float getMoneyUiAccount() {
        return SPUtils.getInstance().getFloat(MONEY_UI_ACCOUNT);
    }




    public static void setIsShowHongbaoDialog(Boolean ts) {
        SPUtils.getInstance().setBoolean(IS_SHOW_HONGBAO_DIALOG, ts);
    }

    public static Boolean getIsShowHongbaoDialog() {
        return SPUtils.getInstance().getBoolean(IS_SHOW_HONGBAO_DIALOG);
    }




    public static void setIsShowMemberDialog(Boolean ts) {
        SPUtils.getInstance().setBoolean(IS_SHOW_MEMBER_DIALOG, ts);
    }

    public static Boolean getIsShowMemberDialog() {
        return SPUtils.getInstance().getBoolean(IS_SHOW_MEMBER_DIALOG);
    }



    public static void setMoneyUiRecordData(String ts) {
        SPUtils.getInstance().setString(MONEY_UI_RECORD_DATA, ts);
    }

    public static String getMoneyUiRecordData() {
        return SPUtils.getInstance().getString(MONEY_UI_RECORD_DATA);
    }


    public static void setMoneyUiMoneyCount(long ts) {
        SPUtils.getInstance().setLong(MONEY_UI_MONEY_COUNT, ts);
    }

    public static Long getMoneyUiMoneyCount() {
        return SPUtils.getInstance().getLong(MONEY_UI_MONEY_COUNT);
    }


    public static void setLoadNativeTime(long ts) {
        SPUtils.getInstance().setLong(LOAD_NATIVE_TIME, ts);
    }

    public static long getLoadNativeTime() {
        return SPUtils.getInstance().getLong(LOAD_NATIVE_TIME);
    }


    public static Boolean getShowDialogWayBurry() {
        return  SPUtils.getInstance().getBoolean(SHOW_DIALOG_WAY_BURRY,true);
    }
    public static void setShowDialogWayBurry(boolean wayBurry) {
        SPUtils.getInstance().setBoolean(SHOW_DIALOG_WAY_BURRY,wayBurry);
    }

    public static long getShowKPYuTime2() {
        return SPUtils.getInstance().getLong(SHOW_KP_Yu_TIME2);
    }
    public static void setShowKPYuTimeTim2(long ts) {
        SPUtils.getInstance().setLong(SHOW_KP_Yu_TIME2, ts);
    }
    public static Long getShowKaipingYynTime2() {
        return SPUtils.getInstance().getLong(SHOW_KAIPING_YYN_TIME2);
    }

    public static void setShowKaipingYynTime2(Long showKaipingYynTime) {
        SPUtils.getInstance().setLong(SHOW_KAIPING_YYN_TIME2, showKaipingYynTime);
    }
    public static Boolean getIsSettingLocation() {
        return SPUtils.getInstance().getBoolean(IS_SETING_LOCATION, true);
    }

    public static void setIsSettingLocation(boolean token) {
        SPUtils.getInstance().setBoolean(IS_SETING_LOCATION, token);
    }
    public static String getTimingAppList() {
        return SPUtils.getInstance().getString(TIMING_APP_LIST);
    }
    public static void setTimingAppList(String timingAppList) {
        SPUtils.getInstance().setString(TIMING_APP_LIST, timingAppList);
    }
    public static String getIsCurrChannel() {
        return SPUtils.getInstance().getString(IS_CURR_CHANNEL);
    }
    public static void setIsCurrChannel(String isCurrChannel) {
        SPUtils.getInstance().setString(IS_CURR_CHANNEL, isCurrChannel);
    }
    public static String getResultTime() {
        return SPUtils.getInstance().getString(LOCK_SCREEN, "");
    }

    public static void setResultTime(String hotNews) {
        SPUtils.getInstance().setString(LOCK_SCREEN, hotNews);
    }
    public static void setCurrentDayRecordUseFunction(long ts) {
        SPUtils.getInstance().setLong(CURRENT_DAY_RECORD_USE_FUNCTION, ts);
    }
    public static long getCurrentDayRecordUseFunction() {
        return SPUtils.getInstance().getLong(CURRENT_DAY_RECORD_USE_FUNCTION);
    }
    public static void setSaveCurrentDayWifiFlow(long ts) {
        SPUtils.getInstance().setLong(SAVE_CURRENT_DAY_WIFI_FLOW, ts);
    }
    public static long getSaveCurrentDayWifiFlow() {
        return SPUtils.getInstance().getLong(SAVE_CURRENT_DAY_WIFI_FLOW);
    }
    public static void setWifiImproveData(int ts) {
        SPUtils.getInstance().setInt(WIFI_IMPROVE_DATA, ts);
    }
    public static int getWifiImproveData() {
        return SPUtils.getInstance().getInt(WIFI_IMPROVE_DATA);
    }
    public static void setSaveCurrentDayMobileFlow(long ts) {
        SPUtils.getInstance().setLong(SAVE_CURRENT_DAY_MOBILE_FLOW, ts);
    }
    public static long getSaveCurrentDayMobileFlow() {
        return SPUtils.getInstance().getLong(SAVE_CURRENT_DAY_MOBILE_FLOW);
    }


    public static void setSaveCurrentYesterdayWifiFlow(long ts) {
        SPUtils.getInstance().setLong(SAVE_CURRENT_YESTERDAY_WIFI_FLOW, ts);
    }
    public static long getSaveCurrentYesterdayWifiFlow() {
        return SPUtils.getInstance().getLong(SAVE_CURRENT_YESTERDAY_WIFI_FLOW);
    }


    public static void setSaveCurrentYesterdayMobileFlow(long ts) {
        SPUtils.getInstance().setLong(SAVE_CURRENT_YESTERDAY_MOBILE_FLOW, ts);
    }
    public static long getSaveCurrentYesterdayMobileFlow() {
        return SPUtils.getInstance().getLong(SAVE_CURRENT_YESTERDAY_MOBILE_FLOW);
    }

    public static void setCurrentDayUseNetworkSpeed(long ts) {
        SPUtils.getInstance().setLong(CURRENT_DAY_USE_NETWORK_SPEED, ts);
    }
    public static long getCurrentDayUseNetworkSpeed() {
        return SPUtils.getInstance().getLong(CURRENT_DAY_USE_NETWORK_SPEED);
    }

    public static void setCurrentDayTime(long ts) {
        SPUtils.getInstance().setLong(CURRENT_DAY_TIME, ts);
    }
    public static long getCurrentDayTime() {
        return SPUtils.getInstance().getLong(CURRENT_DAY_TIME);
    }
    //记录桌面小组件数据
    private final static String SAVE_MOBILE_DESK_DATA = "save_mobile_desk_data";
    public static void setSaveMobileDeskData(String ts) {
        SPUtils.getInstance().setString(SAVE_MOBILE_DESK_DATA, ts);
    }
    public static String getSaveMobileDeskData() {
        return SPUtils.getInstance().getString(SAVE_MOBILE_DESK_DATA,"");
    }
    public static String getHomeFunctionOne() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_ONE);
    }
    public static void setHomeFunctionOne(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_ONE, homeFunctionOne);
    }
    public static String getHomeFunctionTwo() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_TWO);
    }
    public static void setHomeFunctionTwo(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_TWO, homeFunctionOne);
    }
    public static String getHomeFunctionThree() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_THREE);
    }
    public static void setHomeFunctionThree(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_THREE, homeFunctionOne);
    }
    public static String getHomeFunctionFour() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_FOUR);
    }
    public static void setHomeFunctionFour(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_FOUR, homeFunctionOne);
    }
    public static String getHomeFunctionFive() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_FIVE);
    }
    public static void setHomeFunctionFive(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_FIVE, homeFunctionOne);
    }
    public static String getHomeFunctionSix() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_SIX);
    }
    public static void setHomeFunctionSix(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_SIX, homeFunctionOne);
    }
    public static String getHomeFunctionSeven() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_SEVEN);
    }
    public static void setHomeFunctionSeven(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_SEVEN, homeFunctionOne);
    }
    public static String getHomeFunctionEight() {
        return SPUtils.getInstance().getString(HOME_FUNCTION_EIGHT);
    }
    public static void setHomeFunctionEight(String homeFunctionOne) {
        SPUtils.getInstance().setString(HOME_FUNCTION_EIGHT, homeFunctionOne);
    }
    public static long getShowKPYuTime() {
        return SPUtils.getInstance().getLong(SHOW_KP_Yu_TIME);
    }
    public static void setShowKPYuTimeTime(long ts) {
        SPUtils.getInstance().setLong(SHOW_KP_Yu_TIME, ts);
    }
    public static Long getShowKaipingYynTime() {
        return SPUtils.getInstance().getLong(SHOW_KAIPING_YYN_TIME);
    }

    public static void setShowKaipingYynTime(Long showKaipingYynTime) {
        SPUtils.getInstance().setLong(SHOW_KAIPING_YYN_TIME, showKaipingYynTime);
    }
    public static Boolean getIsFirstTimeTwo() {
        return SPUtils.getInstance().getBoolean(IS_FIRST_TIME_TWO, true);
    }

    public static void setIsFirstTimeTwo(boolean token) {
        SPUtils.getInstance().setBoolean(IS_FIRST_TIME_TWO, token);
    }
    public static void setPhoneSpeedTime(long ts) {
        SPUtils.getInstance().setLong(PHONE_SPEED_TIME, ts);
    }

    public static long getPhoneSpeedTime() {
        return SPUtils.getInstance().getLong(PHONE_SPEED_TIME);
    }
    public static Float getDownloadSpeed() {
        return SPUtils.getInstance().getFloat(DOWNLOAD_SPEED);
    }

    public static void setDownloadSpeed(Float downloadSpeed) {
        SPUtils.getInstance().setFloat(DOWNLOAD_SPEED, downloadSpeed);
    }

    public static String getSetUnusualActionIp() {
        return SPUtils.getInstance().getString(SET_UNUSUAL_ACTION_IP);
    }
    public static void setSetUnusualActionIp(String setUnusualActionIp) {
        SPUtils.getInstance().setString(SET_UNUSUAL_ACTION_IP, setUnusualActionIp);
    }
    public static String getInformationFlowTime() {
        return SPUtils.getInstance().getString(INFORMATION_FLOW_TIME, "0");
    }

    public static void setInformationFlowTime(String informationFlowTime) {
        SPUtils.getInstance().setString(INFORMATION_FLOW_TIME, informationFlowTime);
    }
    public static String getBanStatus() {
        return SPUtils.getInstance().getString(BAN_STATUS, "0");
    }

    public static void setBanStatus(String banStatus) {
        SPUtils.getInstance().setString(BAN_STATUS, banStatus);
    }
    public static String getLockScreen() {
        return SPUtils.getInstance().getString(LOCK_SCREEN, "");
    }

    public static void setLockScreen(String hotNews) {
        SPUtils.getInstance().setString(LOCK_SCREEN, hotNews);
    }
    public static String getFlowOutPop() {
        return SPUtils.getInstance().getString(FLOW_OUT_POP, "");
    }

    public static void setFlowOutPop(String hotNews) {
        SPUtils.getInstance().setString(FLOW_OUT_POP, hotNews);
    }
    public static String getDesktopWidgetsTime() {
        return SPUtils.getInstance().getString(DESKTOP_WIDGETS_TIME);
    }
    public static void setDesktopWidgetsTime(String token) {
        SPUtils.getInstance().setString(DESKTOP_WIDGETS_TIME, token);
    }
    public static String getIsDynamicWidgetsTime() {
        return SPUtils.getInstance().getString(IS_DYNAMIC_WIDGETS_TIME);
    }
    public static void setIsDynamicWidgetsTime(String token) {
        SPUtils.getInstance().setString(IS_DYNAMIC_WIDGETS_TIME, token);
    }
    public static long getSet15Time() {
        return SPUtils.getInstance().getLong(SET_15_TIME);
    }

    public static void setSet15Time(Long set15Time) {
        SPUtils.getInstance().setLong(SET_15_TIME, set15Time);
    }


    public static long getMemberWeekTime() {
        return SPUtils.getInstance().getLong(MEMBER_WEEK_TIME);
    }

    public static void setMemberWeekTime(Long memberTime) {
        SPUtils.getInstance().setLong(MEMBER_WEEK_TIME, memberTime);
    }
    public static void setIsShowAd(boolean isShowAd) {
        SPUtils.getInstance().setBoolean(IS_SHOW_AD, isShowAd);
    }

    public static Boolean getIsShowAd() {
        return SPUtils.getInstance().getBoolean(IS_SHOW_AD, false);
    }
    public static void setIsShowAdOne(boolean isShowAd) {
        SPUtils.getInstance().setBoolean(IS_SHOW_AD_ONE, isShowAd);
    }

    public static Boolean getIsShowAdOne() {
        return SPUtils.getInstance().getBoolean(IS_SHOW_AD_ONE, false);
    }

    public static long getToDatTime() {
        return SPUtils.getInstance().getLong(TO_DAT_TIME);
    }

    public static void setToDatTime(Long toDatTime) {
        SPUtils.getInstance().setLong(TO_DAT_TIME, toDatTime);
    }

    public static long getIsToDayAdShowTotal() {
        return SPUtils.getInstance().getLong(IS_TO_DAY_AD_SHOW_TOTAL);
    }

    public static void setIsToDayAdShowTotal(Long isToDayLockSwitchTotal) {
        SPUtils.getInstance().setLong(IS_TO_DAY_AD_SHOW_TOTAL, isToDayLockSwitchTotal);
    }

    public static Boolean getFirstShowPermissionsDialog() {
        return SPUtils.getInstance().getBoolean(FIRST_SHOW_PERMISSIONS_DIALOG, false);
    }

    public static void setFirstShowPermissionsDialog(boolean firstShowPermissionsDialog) {
        SPUtils.getInstance().setBoolean(FIRST_SHOW_PERMISSIONS_DIALOG, firstShowPermissionsDialog);
    }

    public static void setShowChapingYynTime(long ts) {
        SPUtils.getInstance().setLong(SHOW_CHAPING_YYN_TIME, ts);
    }

    public static long getShowChapingYynTime() {
        return SPUtils.getInstance().getLong(SHOW_CHAPING_YYN_TIME);
    }

    public static void setShowChapingHomeYynTime2(long ts) {
        SPUtils.getInstance().setLong(SHOW_CHAPING_HOME_YYN_TIME2, ts);
    }

    public static long getShowChapingHomeYynTime2() {
        return SPUtils.getInstance().getLong(SHOW_CHAPING_HOME_YYN_TIME2);
    }
    public static void setShowJlHomeYynTime(long ts) {
        SPUtils.getInstance().setLong(SHOW_JL_HOME_YYN_TIME, ts);
    }

    public static long getShowJlHomeYynTime() {
        return SPUtils.getInstance().getLong(SHOW_JL_HOME_YYN_TIME);
    }

    public static void setShowChapingAllTypeTime(long ts) {
        SPUtils.getInstance().setLong(SHOW_CHAPING_ALL_TYPE_TIME, ts);
    }

    public static long getShowChapingAllTypeTime() {
        return SPUtils.getInstance().getLong(SHOW_CHAPING_ALL_TYPE_TIME);
    }

    public static void setShowXinxiluiYynTime(long ts) {
        SPUtils.getInstance().setLong(SHOW_XINXILUI_YYN_TIME, ts);
    }

    public static long getShowXinxiluiYynTime() {
        return SPUtils.getInstance().getLong(SHOW_XINXILUI_YYN_TIME);
    }

    public static void setShowXinxiluiYynTime2(long ts) {
        SPUtils.getInstance().setLong(SHOW_XINXILUI_YYN_TIME2, ts);
    }

    public static long getShowXinxiluiYynTime2() {
        return SPUtils.getInstance().getLong(SHOW_XINXILUI_YYN_TIME2);
    }

    public static String getNetworkDetection() {
        return SPUtils.getInstance().getString(NETWORK_DETECTION, "");
    }

    public static void setNetworkDetection(String networkDetection) {
        SPUtils.getInstance().setString(NETWORK_DETECTION, networkDetection);
    }

    public static String getNotificationFunction() {
        return SPUtils.getInstance().getString(NOTIFICATION_FUNCTION, "");
    }

    public static void setNotificationFunction(String notificationFunction) {
        SPUtils.getInstance().setString(NOTIFICATION_FUNCTION, notificationFunction);
    }

    public static Boolean getStartupManagement() {
        return SPUtils.getInstance().getBoolean(STARTUP_MANAGEMENT, true);
    }

    public static void setStartupManagement(boolean startupManagement) {
        SPUtils.getInstance().setBoolean(STARTUP_MANAGEMENT, startupManagement);

    }

    public static long getShowAppInstallTime() {
        return SPUtils.getInstance().getLong(SHOW_APP_INSTALL_TIME);
    }

    public static void setShowAppInstallTime(long ts) {
        SPUtils.getInstance().setLong(SHOW_APP_INSTALL_TIME, ts);
    }
    public static long getClickJlTime() {
        return SPUtils.getInstance().getLong(CLICK_JL_TIME);
    }

    public static void setClickJlTime(long ts) {
        SPUtils.getInstance().setLong(CLICK_JL_TIME, ts);
    }

    public static Boolean getBackgroundProtection() {
        return SPUtils.getInstance().getBoolean(BACKGROUND_PROTECTION, true);

    }

    public static void setBackgroundProtection(boolean backgroundProtection) {
        SPUtils.getInstance().setBoolean(BACKGROUND_PROTECTION, backgroundProtection);

    }

    public static String getLockScreenInformation() {
        return SPUtils.getInstance().getString(LOCK_SCREEN_INFORMATION, "");

    }

    public static void setLockScreenInformation(String lockScreenInformation) {
        SPUtils.getInstance().setString(LOCK_SCREEN_INFORMATION, lockScreenInformation);

    }

    public static void setPersonalizedPush(String personalizedPush) {
        SPUtils.getInstance().setString(PERSONALIZED_PUSH, personalizedPush);
    }

    public static String getPersonalizedPush() {
        return SPUtils.getInstance().getString(PERSONALIZED_PUSH, "");
    }

    public static String getAutomaticPowerSaving() {
        return SPUtils.getInstance().getString(AUTOMATIC_POWER_SAVING, "");
    }

    public static void setAutomaticPowerSaving(String automaticPowerSaving) {
        SPUtils.getInstance().setString(AUTOMATIC_POWER_SAVING, automaticPowerSaving);
    }

    public static String getAutomaticCooling() {
        return SPUtils.getInstance().getString(AUTOMATIC_COOLING, "");
    }

    public static void setAutomaticCooling(String automaticCooling) {
        SPUtils.getInstance().setString(AUTOMATIC_COOLING, automaticCooling);
    }


    public static Boolean getIsDynamicWallpaper() {
        return SPUtils.getInstance().getBoolean(IS_DYNAMIC_WALLPAPER, false);
    }

    public static void setIsDynamicWallpaper(boolean token) {
        SPUtils.getInstance().setBoolean(IS_DYNAMIC_WALLPAPER, token);
    }

    public static String getIsDynamicWallpaperTime() {
        return SPUtils.getInstance().getString(IS_DYNAMIC_WALLPAPER_TIME);
    }

    public static void setIsDynamicWallpaperTime(String token) {
        SPUtils.getInstance().setString(IS_DYNAMIC_WALLPAPER_TIME, token);
    }

    public static Boolean getIsWhiteList() {
        return SPUtils.getInstance().getBoolean(IS_WHITE_LIST, false);
    }

    public static void setIsWhiteList(boolean token) {
        SPUtils.getInstance().setBoolean(IS_WHITE_LIST, token);
    }

    public static Boolean getIsFirstTime() {
        return SPUtils.getInstance().getBoolean(IS_FIRST_TIME, true);
    }

    public static void setIsFirstTime(boolean token) {
        SPUtils.getInstance().setBoolean(IS_FIRST_TIME, token);
    }

    public static String getResultpActivityTime() {
        return SPUtils.getInstance().getString(RESULTP_ACTIVITY_TIME);
    }

    public static void setResultpActivityTime(String token) {
        SPUtils.getInstance().setString(RESULTP_ACTIVITY_TIME, token);
    }

    public static String getResultpActivityData() {
        return SPUtils.getInstance().getString(RESULTP_ACTIVITY_DATA);
    }

    public static void setResultpActivityData(String token) {
        SPUtils.getInstance().setString(RESULTP_ACTIVITY_DATA, token);
    }

    public static String getEnlargePhoto() {
        return SPUtils.getInstance().getString(ENLARGE_PHOTO);
    }

    public static void setEnlargePhoto(String token) {
        SPUtils.getInstance().setString(ENLARGE_PHOTO, token);
    }

    public static String getToken() {
        return SPUtils.getInstance().getString(TOKEN);
    }

    public static void setToken(String token) {
        SPUtils.getInstance().setString(TOKEN, token);
    }


    public static String getDeviceId() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(DEVICE_ID));
    }

    public static void setDeviceId(String deviceid) {
        SPUtils.getInstance().setString(DEVICE_ID, deviceid);
    }

    public static String getStartHttpRespon() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(START_HTTP_RESPON));
    }

    public static void setStartHttpRespon(String httpData) {
        SPUtils.getInstance().setString(START_HTTP_RESPON, httpData);
    }

    public static String getStorageTime() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(STORAGE_TIME));
    }

    public static void setStorageTime(String httpData) {
        SPUtils.getInstance().setString(STORAGE_TIME, httpData);
    }

    public static String getApkInstallationTime() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(APK_INSTALL_TIME));
    }

    public static void setApkInstallationTime(String httpData) {
        SPUtils.getInstance().setString(APK_INSTALL_TIME, httpData);
    }

    public static String getAdShowCount() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(AD_SHOW_COUNT));
    }

    public static void setAdShowCount(String count) {
        SPUtils.getInstance().setString(AD_SHOW_COUNT, count);
    }

    public static String getDjid() {
        return StrUtil.null2Str(SPUtils.getInstance().getString(DJID));
    }

    public static void setDjid(String count) {
        SPUtils.getInstance().setString(DJID, count);
    }
}