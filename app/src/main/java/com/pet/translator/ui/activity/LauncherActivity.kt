package com.pet.translator.ui.activity

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.pet.translator.APP
import com.pet.translator.AppConst
import com.pet.translator.R
import com.pet.translator.base.dj.BaseActivity
import com.pet.translator.databinding.ActivityLauncherBinding
import com.pet.translator.event.dj.ActiveEvent
import com.pet.translator.helper.dj.PushHelper
import com.pet.translator.utils.dj.AntiRepeatClickUtils
import com.pet.translator.utils.dj.AppBlack
import com.pet.translator.utils.dj.CountDownTool
import com.pet.translator.utils.dj.DeviceInfoUtil
import com.pet.translator.utils.dj.GetHttpDataUtil
import com.pet.translator.utils.dj.ICountDown
import com.pet.translator.utils.dj.SharedPreferencesDelegate
import com.pet.translator.utils.dj.UserInfoModel
import com.pet.translator.widget.dialog.dj.NBAgreementDialog
import com.kwad.sdk.api.util.GMCPAdNoLimitUtils
import com.kwad.sdk.api.util.GMSPAdUtils
import com.kwad.sdk.api.util.GMSPTwoAdUtils
import com.pet.translator.utils.LanguageUtils
import com.umeng.commonsdk.utils.UMUtils
import io.reactivex.observers.DisposableObserver
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * date :   2023-12-24 024
 * author:  DengZhiYang
 * desc:    something
 */
class LauncherActivity : BaseActivity() {

    private lateinit var binding: ActivityLauncherBinding
    var TAG = "SplashActivity"
    var mHandler = Handler(Looper.getMainLooper())
    private var myProgress = 0 //当前进度
    var countDownTool: ICountDown? = null
    var isGread = false
    var disposable: DisposableObserver<Any>? = null
    var countDownTimerTwo: CountDownTimer? = null
    var isShowAD = false
    var isKPStart = 0
    var adHandler = Handler(Looper.getMainLooper())
    var status = 0

    var kpLoadIsSuccess = 0
    var kpLoadIsSuccess2 = 0
    var kpIsShow = false
    var kpStart = false

    var position = -1;
    var progressIndex = 76
    override fun getLayoutId() = R.layout.activity_launcher
    var isAgree by SharedPreferencesDelegate({ this }, false, "IS_AGREE")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityLauncherBinding.bind(view)


        progressIndex = 76
        if (null != intent) {
            position = intent.getIntExtra("position", -1)
            Log.e(TAG, "position=" + position)

        }
        if(position != 1){
            AppConst.isStopped = false
            AppConst.isWaked = false
        }else{
            AppConst.isWaked = true
        }

        isShowAD = false
        AppConst.splashIsJumpMain = false
        try {
            //部分手机第一次创建Web会崩溃, 让他自己先崩一次再创建正常的WebView
            //"AwContents must be created if we are not posting!"
            WebView(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            AppConst.GetWebViewUserAgent = DeviceInfoUtil.getWebViewUserAgent(this)
        } catch (e: Exception) {
            AppConst.GetWebViewUserAgent = ""
        }
        setProgressBar(5)
        if (!isAgree) {
            firstShowDialog()
        } else {
            if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
                callInstall()
            } else {
//                if(!TextUtils.isEmpty(UserInfoModel.getDjid())) {
//                    binding.myTvDjNumber1.setText(UserInfoModel.getDjid())
//                }
                if(!TextUtils.isEmpty(UserInfoModel.getRiseId())) {
                    binding.splashAppDjid.text = UserInfoModel.getRiseId()
                }
                AppConst.is_show_ad = UserInfoModel.getIsShowAd()
                if (UserInfoModel.getIsCheckFlag() && !AppConst.is_show_ad) {
                    val animator = ObjectAnimator.ofInt(binding.pbProgress, "progress", 5, 100)
                    animator.duration = 500 // 动画持续时间
                    animator.start() // 启动动画
                    goMainActivity()
                    return;
                } else {
                    val animator =
                        ObjectAnimator.ofInt(binding.pbProgress, "progress", 5, progressIndex)
                    animator.duration = 2000 // 动画持续时间
                    animator.start() // 启动动画
                    Handler().postDelayed({
                        startCountDownTool()
                    }, 2000)
                }

            }

        }


    }

    private fun firstShowDialog() {
        val dialog = Dialog(this, R.style.MyDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_first_install_permission_splash);
        updateTextColor(dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //底部弹出的Dialog
        dialog.window?.setGravity(Gravity.BOTTOM);
        val ref_title = dialog.findViewById<TextView>(R.id.ref_title)
        val spannableString = SpannableString("欢迎使用${getString(R.string.app_name)}")
        spannableString.setSpan(
            AgreementClickableSpan(
                this,
                AgreementClickableSpan.SPAN_TYPE_USER_SERVICE_AGREEMENT
            ), 4, spannableString.length, 33
        )
        ref_title.text = spannableString;
        dialog.findViewById<TextView>(R.id.htl).setOnClickListener {
            firstShowDialogTwo(dialog)
        }

        dialog.findViewById<View>(R.id.y1u).setOnClickListener {
            dialog.cancel()
            getHttpData()


        }
        if ((!isFinishing)) {
            dialog.show()
        }
    }

    private fun firstShowDialogTwo(dialogOne: Dialog) {
        dialogOne.dismiss()
        val dialog = Dialog(this, R.style.MyDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_first_install_permission_splash_two);
        updateTextColorTwo(dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //底部弹出的Dialog
        dialog.window?.setGravity(Gravity.BOTTOM);
        dialog.findViewById<TextView>(R.id.htl).setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        dialog.findViewById<View>(R.id.y1u).setOnClickListener {
            dialog.cancel()

            getHttpData()


        }
        if ((!isFinishing)) {
            dialog.show()
        }
    }

    private fun updateTextColorTwo(dialog: Dialog) {
        val cxv = dialog.findViewById<TextView>(R.id.cxv)
        val spannableStringOne = SpannableString(getString(R.string.ra12))
        spannableStringOne.setSpan(
            AgreementClickableSpan(
                this,
                AgreementClickableSpan.SPAN_TYPE_USER_SERVICE_AGREEMENT
            ), 4, 12, 33
        )
        spannableStringOne.setSpan(
            AgreementClickableSpan(
                this,
                AgreementClickableSpan.SPAN_TYPE_PRIVACY_POLICY_AGREEMENT
            ), 13, 19, 33
        )
        cxv.text = spannableStringOne;
        cxv.highlightColor = 0;
        cxv.movementMethod = LinkMovementMethod.getInstance();
        val tvPermissionTitle = dialog.findViewById<TextView>(R.id.nq7)
        val spannableString = SpannableString(getString(R.string.ra11))
        spannableString.setSpan(
            AgreementClickableSpan(
                this,
                AgreementClickableSpan.SPAN_TYPE_USER_SERVICE_AGREEMENT
            ), 6, 14, 33
        )
        spannableString.setSpan(
            AgreementClickableSpan(
                this,
                AgreementClickableSpan.SPAN_TYPE_PRIVACY_POLICY_AGREEMENT
            ), 15, 21, 33
        )
        tvPermissionTitle.text = spannableString;
        tvPermissionTitle.highlightColor = 0;
        tvPermissionTitle.movementMethod = LinkMovementMethod.getInstance();
    }

    private fun updateTextColor(dialog: Dialog) {
        val tvPermissionTitle = dialog.findViewById<TextView>(R.id.qbi)
        val spannableString = SpannableString(getString(R.string.d0))
        spannableString.setSpan(
            AgreementClickableSpan(
                this,
                AgreementClickableSpan.SPAN_TYPE_USER_SERVICE_AGREEMENT
            ), 10, 18, 33
        )
        spannableString.setSpan(
            AgreementClickableSpan(
                this,
                AgreementClickableSpan.SPAN_TYPE_PRIVACY_POLICY_AGREEMENT
            ), 19, 25, 33
        )
        tvPermissionTitle.text = spannableString;
        tvPermissionTitle.highlightColor = 0;
        tvPermissionTitle.movementMethod = LinkMovementMethod.getInstance();
    }

    /**
     * 协议点击。
     */
    class AgreementClickableSpan(private val context: Context, private val spanType: String) :
        ClickableSpan() {
        override fun onClick(view: View) {
            when (spanType) {
                NBAgreementDialog.AgreementClickableSpan.SPAN_TYPE_USER_SERVICE_AGREEMENT -> {
                    WebViewActivity.forward(
                        context,
                        context.getString(R.string.user_agreement),
                        AppConst.URL_USER_AGREEMENT
                    )
                }

                NBAgreementDialog.AgreementClickableSpan.SPAN_TYPE_PRIVACY_POLICY_AGREEMENT -> {
                    WebViewActivity.forward(
                        context,
                        context.getString(R.string.privacy_policy),
                        AppConst.URL_PRIVACY_POLICY
                    )
                }

                else -> {
                }
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = context.resources.getColor(R.color.ex);
        }

        companion object {
            const val SPAN_TYPE_USER_SERVICE_AGREEMENT = "UserServiceAgreement"
            const val SPAN_TYPE_PRIVACY_POLICY_AGREEMENT = "PrivacyPolicyAgreement"
        }

    }

    private fun callInstall() {
        Log.d(TAG, "splash setInstall")
        if (TextUtils.isEmpty(UserInfoModel.getDjid())) {
            if (!TextUtils.isEmpty(AppConst.oaid)) {
                GetHttpDataUtil.setInstall(this, AppConst.INSTALL_FROM_SPLASH)
            } else {
                DeviceInfoUtil.splashInit(this);
            }
        }
    }

    fun initKaiPing() {
        AppConst.is_show_ad = UserInfoModel.getIsShowAd()
        if (!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
            kpIsShow = false
            kpLoadIsSuccess = 0
            kpLoadIsSuccess2 = 0
            Log.d(TAG, "SplashOneActivity 加载开屏: ")
            GMSPAdUtils.init(object : GMSPAdUtils.GmSplashAdListener {
                override fun onLoadFinish() {
                    Log.d(
                        TAG,
                        "SplashOneActivity 展示开屏 kpLoadIsSuccess2:" + kpLoadIsSuccess2 + ",kpIsShow:" + kpIsShow
                    )
                    isShowAD = true
                    kpLoadIsSuccess = 1

                }

                override fun onLoadFail() {
                    Log.d(TAG, "SplashOneActivity load 报错了 ")
                    kpLoadIsSuccess = 2
                }

                override fun onClose() {
                    kpLoadIsSuccess = 2
                    isShowAD = true
                    Log.d(
                        TAG,
                        "SplashOneActivity load 开屏结束 kpLoadIsSuccess2： " + kpLoadIsSuccess2
                    )
                    if (adHandler != null) {
                        Log.d(TAG, "SplashOneActivity adHandler 销毁动作 ")
                        adHandler.removeCallbacksAndMessages(null)
                    }
                    if (AppConst.is_show_ad && position != 1) {
                        binding.splashAdContainer.visibility = View.VISIBLE
                        GMSPTwoAdUtils.showSplash(binding.splashAdContainer)
                        adHandler.postDelayed({
                            Log.e(TAG, "广告2计时销毁----------------")
                            goMainActivity()
                        }, 8000)
                    } else {
                        goMainActivity()
                    }


                }
            }, this, true) //兜底方案

            showTimeAdCp()

            if (AppConst.is_show_ad && position != 1) {

//                Handler().postDelayed({
                Log.d(TAG, "SplashTwoActivity 加载开屏: ")
                GMSPTwoAdUtils.init(object : GMSPTwoAdUtils.GmSplashAdListener {
                    override fun onLoadFinish() {
                        Log.d(
                            TAG,
                            "SplashTwoActivity 展示开屏:kpLoadIsSuccess:" + kpLoadIsSuccess + ",kpIsShow:" + kpIsShow
                        )
                        isShowAD = true
                        kpLoadIsSuccess2 = 1


                    }

                    override fun onLoadFail() {
                        Log.d(TAG, "SplashTwoActivity load 报错了 ")
                        kpLoadIsSuccess2 = 2
                    }

                    override fun onClose() {
                        isShowAD = true
                        kpLoadIsSuccess2 = 2
                        Log.d(TAG, "SplashTwoActivity load 开屏结束 ")
                        if (adHandler != null) {
                            Log.d(TAG, "SplashTwoActivity adHandler 销毁动作 ")
                            adHandler.removeCallbacksAndMessages(null)
                        }
                        goMainActivity()

                    }
                }, this, true) //兜底方案
            }else{
                kpLoadIsSuccess2 = 2

            }
        }
    }



    private fun showTimeAdCp() {
        Log.e(TAG, "showTimeAdCp:" + GMCPAdNoLimitUtils.isReady())
        if(!UserInfoModel.getIsCheckFlag() && !AppConst.is_show_ad) {
            GMCPAdNoLimitUtils.init(this, object : GMCPAdNoLimitUtils.GirdMenuStateListener {
                override fun onShowError() {
                    Log.e(TAG, "GMCPAdNoLimitUtils onShowError")
                }

                override fun showVideoClosed() {
                    Log.e(TAG, "GMCPAdNoLimitUtils showVideoClosed")
                }

                override fun onError() {
                    Log.e(TAG, "GMCPAdNoLimitUtils onError")
                }

                override fun onSuccess() {
                    Log.e(TAG, "GMCPAdNoLimitUtils onSuccess")

                }
            }) //初始化插全屏广告
            if (!GMCPAdNoLimitUtils.isReady()) {
                GMCPAdNoLimitUtils.initPreloading()
            }
        }else{
            if(AppConst.is_show_ad) {
                APP.initCp()
            }
        }
    }


    fun showKaiPing() {


        kpStart = true
        AppConst.is_show_ad = UserInfoModel.getIsShowAd()
        Log.e(TAG,"showKaiPingAppConst.is_show_ad :"+AppConst.is_show_ad+",UserInfoModel.getIsCheckFlag():"+UserInfoModel.getIsCheckFlag())
        if (!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {

            Log.e(
                TAG,
                "当前开屏kpLoadIsSuccess：" + kpLoadIsSuccess + ",kpLoadIsSuccess2:" + kpLoadIsSuccess2
            )
            if (kpLoadIsSuccess == 1) {
                binding.splashAdContainer.visibility = View.VISIBLE
                GMSPAdUtils.showSplash(binding.splashAdContainer)
                Log.e(TAG, "广告1计时开始----------------")
                adHandler.postDelayed({
                    Log.e(TAG, "广告1计时销毁----------------")
                    if (AppConst.is_show_ad) {
                        if (kpLoadIsSuccess2 == 1) {
                            GMSPTwoAdUtils.showSplash(binding.splashAdContainer)
                            adHandler.postDelayed({
                                Log.e(TAG, "广告2计时销毁----------------")
                                goMainActivity()
                            }, 8000)
                        } else {
                            goMainActivity()
                        }
                    } else {
                        goMainActivity()
                    }
                }, 8000)

            } else if (kpLoadIsSuccess2 == 1) {
                binding.splashAdContainer.visibility = View.VISIBLE
                GMSPTwoAdUtils.showSplash(binding.splashAdContainer)
                Log.e(TAG, "广告2计时开始----------------")
                adHandler.postDelayed({
                    Log.e(TAG, "广告2计时销毁----------------")
                    goMainActivity()
                }, 8000)
            }else{
                goMainActivity()
            }


        } else {
            goMainActivity()
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun goMainActivity() {
        countDownTool?.stop()
        isShowAD=true
        AppConst.splashInfoShowMainCP = true
        mHandler.postDelayed(Runnable {
            LanguageUtils.setIndex(1)
            MainActivity.forward(this)
            finish()
        }, 500)


    }

    fun startCountDownTool() {


        if (!AppConst.splashIsJumpMain) {
            AppConst.splashIsJumpMain = true
            AppConst.is_show_ad = UserInfoModel.getIsShowAd()
            Log.e(TAG, "startCountDownTool")

            if (!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad){

            }else{
                goMainActivity()
                return;

            }
            countDownTool = object : CountDownTool(8L) {
                override fun onTick(second: Long) {
                    progressIndex+= 3
                    Log.e(TAG, "当前倒计时：second：" + second)
                    if (second == 8L) {
                        if (!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
                            initKaiPing()
                        }

                    }


                    if (!kpStart && second <= 6) {
                        Log.e(TAG,"showKaiPingAppConst.is_show_ad :"+AppConst.is_show_ad+",UserInfoModel.getIsCheckFlag():"+UserInfoModel.getIsCheckFlag()+",isShowAD:"+isShowAD)
                        if (!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
                            if (kpLoadIsSuccess != 0 && kpLoadIsSuccess2 != 0) {
                                showKaiPing()
                            } else if (second == 1L) {
                                showKaiPing()
                            }
                        }


                    }


                    setProgressBar(progressIndex)
                }

                override fun finishTime() {


                    if (!isShowAD) {
                        goMainActivity()
                        setProgressBar(100)
                    }
                    countDownTool?.stop()
                }
            }
            countDownTool?.start()
        }

    }

    fun setProgressBar(progress: Int) {
        binding.pbProgress.progress = progress
    }

    private fun getHttpData() {
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        //MSDK的初始化需要放在Application中进行
//        if (!UserInfoModel.getIsFirstTime()) {
//            return
//        }


        isAgree = true
        UserInfoModel.setIsFirstTime(false)
        GetHttpDataUtil.getOutNetIP()

        AppConst.riskInfo = AppBlack.getRiskInfo(this)//设备异常标签，正常、代理、异常、模拟器、root、无SIM
        AppConst.AndroidId = DeviceInfoUtil.getAndroidId(this)



        Handler().postDelayed({
            DeviceInfoUtil.init(this)
        }, 1500)

//        setProgressBar(100)
//        goMainActivity()
    }

    //友盟初始化 已经同意
    private fun initUmeng() {
        //用户点击隐私协议同意按钮后，初始化PushSDK
        val isMainProcess = UMUtils.isMainProgress(this)
        if (isMainProcess) {
            Thread { PushHelper.init(applicationContext) }.start()
        } else {
            PushHelper.init(applicationContext)
        }
    }

    override fun initStatus() {
//        ImmersionBar.with(this)
//            .fullScreen(true)
//            .init()
        EventBus.getDefault().register(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageVideoEvent(t: ActiveEvent) {

        Log.e(TAG, "当前接口返回结果：" + Gson().toJson(t))
        if (t.isActive) {
            binding.clRefresh.visibility = View.GONE
            binding.llProgress.visibility = View.VISIBLE
            if (!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
                APP.initAdSdk()
                initUmeng()

                val animator = ObjectAnimator.ofInt( binding.pbProgress, "progress", 5, progressIndex)
                animator.duration = 2000 // 动画持续时间
                animator.start() // 启动动画


                Handler().postDelayed({
                    startCountDownTool()
                }, 2000)

            } else {

                if (!AppConst.splashIsJumpMain) {
                    AppConst.splashIsJumpMain = true;
                    val animator = ObjectAnimator.ofInt(binding.pbProgress, "progress", 5, 100)
                    animator.duration = 500 // 动画持续时间
                    animator.start() // 启动动画
                    goMainActivity()
                }

            }
//            if(!TextUtils.isEmpty(UserInfoModel.getDjid())) {
//                binding.myTvDjNumber1.setText(UserInfoModel.getDjid())
//            }
            if(!TextUtils.isEmpty(UserInfoModel.getRiseId())) {
                binding.splashAppDjid.text = UserInfoModel.getRiseId()
            }

        } else {
            if (t.source == AppConst.INSTALL_FROM_SPLASH) {
                binding.clRefresh.visibility = View.VISIBLE
                binding.llProgress.visibility = View.GONE

                binding.tvRefresh.setOnClickListener {
                    if (AntiRepeatClickUtils.isFastHttpClick()) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.llProgress.visibility = View.VISIBLE
                            binding.clRefresh.visibility = View.GONE
                            callInstall()
                        }, 300)
                    }
                }


            } else if (t.source == AppConst.INSTALL_FROM_APP) {
                callInstall()
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}