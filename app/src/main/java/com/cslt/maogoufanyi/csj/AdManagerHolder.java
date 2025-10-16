package com.cslt.maogoufanyi.csj;

import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTCustomController;
import com.bytedance.sdk.openadsdk.mediation.init.MediationConfig;
import com.bytedance.sdk.openadsdk.mediation.init.MediationConfigUserInfoForSegment;
import com.bytedance.sdk.openadsdk.mediation.init.MediationPrivacyConfig;
import com.cslt.maogoufanyi.AppConst;
import com.cslt.maogoufanyi.R;

import java.util.HashMap;
import java.util.Map;


/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class AdManagerHolder {

    private static final String TAG = "TTAdManagerHolder";

    private static boolean sInit;



    public static TTAdManager get() {

        return TTAdSdk.getAdManager();
    }

    public static void init(final Context context) {
        //初始化穿山甲SDK
        doInit(context);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            //TTAdSdk.init(context, buildConfig(context));
//
//            TTAdSdk.init(context, buildConfig(context), new TTAdSdk.InitCallback() {
//                @Override
//                public void success() {
//                    Log.i(TAG, "success: " + TTAdSdk.isInitSuccess());
//                }
//
//                @Override
//                public void fail(int code, String msg) {
//                    Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
//                }
//            });
            TTAdSdk.init(context, buildConfig(context));

            TTAdSdk.start(new TTAdSdk.Callback() {
                @Override
                public void success() {

                    Log.i(TAG, "success: " + TTAdSdk.isInitSuccess());
                }

                @Override
                public void fail(int code, String msg) {
                    Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
                }
            });
            sInit = true;
        }
    }



    private static TTAdConfig buildConfig(Context context) {
        MediationConfigUserInfoForSegment userInfo = new MediationConfigUserInfoForSegment();
        userInfo.setUserId("msdk-demo");
        userInfo.setGender(MediationConfigUserInfoForSegment.GENDER_MALE);
        userInfo.setChannel("msdk-channel");
        userInfo.setSubChannel("msdk-sub-channel");
        userInfo.setAge(999);
        userInfo.setUserValueGroup("msdk-demo-user-value-group");

        Map<String, String> customInfos = new HashMap<>();
        customInfos.put("aaaa", "test111");
        customInfos.put("bbbb", "test222");
        userInfo.setCustomInfos(customInfos);


        return new TTAdConfig.Builder()
                .appId(AppConst.Ad_ID)
                .appName(context.getString(R.string.app_name))
                .debug(false) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .useMediation(true)
                .setAgeGroup(TTAdConstant.ADULT)
                .customController(new TTCustomController() {

                    @Override
                    public boolean isCanUseWifiState() {
                        return super.isCanUseWifiState();
                    }

                    @Override
                    public String getMacAddress() {
                        return super.getMacAddress();
                    }

                    @Override
                    public boolean isCanUseWriteExternal() {
                        return super.isCanUseWriteExternal();
                    }

                    @Override
                    public String getDevOaid() {
                        return super.getDevOaid();
                    }

                    @Override
                    public boolean isCanUseAndroidId() {
                        return super.isCanUseAndroidId();
                    }

                    @Override
                    public String getAndroidId() {
                        return super.getAndroidId();
                    }

                    @Override
                    public MediationPrivacyConfig getMediationPrivacyConfig() {
                        return new MediationPrivacyConfig() {

                            @Override
                            public boolean isLimitPersonalAds() {
                                return super.isLimitPersonalAds();
                            }

                            @Override
                            public boolean isProgrammaticRecommend() {
                                return super.isProgrammaticRecommend();
                            }
                        };
                    }

                    @Override
                    public boolean isCanUsePermissionRecordAudio() {
                        return super.isCanUsePermissionRecordAudio();
                    }
                })
                .setMediationConfig(new MediationConfig.Builder()
                        .setMediationConfigUserInfoForSegment(userInfo)
                        .build())
                .build();
    }
}
