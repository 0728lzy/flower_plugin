package com.catcsyun.liantadog.ui.fragment

import android.graphics.Outline
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.catcsyun.liantadog.APP
import com.catcsyun.liantadog.AppConst
import com.catcsyun.liantadog.R
import com.catcsyun.liantadog.base.dj.RootFragment
import com.catcsyun.liantadog.csj.ZYMAllAdsUtils
import com.catcsyun.liantadog.databinding.FragmentAboutBinding
import com.catcsyun.liantadog.event.SimpleEvent
import com.catcsyun.liantadog.ext.getBinding
import com.catcsyun.liantadog.ext.thrillClickListener
import com.catcsyun.liantadog.ui.activity.KLTGContactCustomerServiceActivity
import com.catcsyun.liantadog.ui.activity.KLTGWebViewActivity
import com.catcsyun.liantadog.utils.dj.DeviceUtils
import com.catcsyun.liantadog.utils.dj.UserInfoModel
import com.catcsyun.liantadog.utils.lzy.LZYLog
import com.catcsyun.liantadog.utils.lzy.ScreenUtils
import com.catcsyun.liantadog.widget.popup.dj.QNInputPasswordDialogPopup
import com.catcsyun.liantadog.utils.dj.GetHttpDataUtil
import com.zym.customer.CustomerConfig
import com.zym.customer.ui.activity.SpecialCustomerServiceActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AboutFragment : RootFragment(R.layout.fragment_about) {


    private lateinit var binding: FragmentAboutBinding

    private lateinit var mineLinearLayout: LinearLayout
    private lateinit var privacyLinearLayout: LinearLayout
    private lateinit var userProLinearLayout: LinearLayout
    private lateinit var feedbackLinearLayout: LinearLayout
    private lateinit var versionTextView: TextView
    private lateinit var djIdTextView: TextView
    private lateinit var appLogoImageView: ImageView
    private var stat = 0

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = view.getBinding()


        mineLinearLayout = binding.mineLin
        privacyLinearLayout = binding.mineLinPrivacy
        userProLinearLayout = binding.mineLinUserPro
        feedbackLinearLayout = binding.mineLinUserFb
        versionTextView = binding.mineAppVersion
        djIdTextView = binding.mineDjId
        appLogoImageView = binding.mineAppImg

        mineLinearLayout.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline?) {
                outline?.setRoundRect(
                    0,
                    0,
                    view.width,
                    view.height,
                    ScreenUtils.dip2px(15, requireContext()).toFloat()
                )
            }
        }
        mineLinearLayout.clipToOutline = true

        privacyLinearLayout.thrillClickListener {
            KLTGWebViewActivity.forward(
                requireActivity() as com.catcsyun.liantadog.base.dj.BaseActivity,
                getString(R.string.privacy_policy),
                AppConst.URL_PRIVACY_POLICY
            )
        }

        userProLinearLayout.thrillClickListener {
            KLTGWebViewActivity.forward(
                requireActivity() as com.catcsyun.liantadog.base.dj.BaseActivity,
                getString(R.string.user_agreement),
                AppConst.URL_USER_AGREEMENT
            )
        }

        feedbackLinearLayout.thrillClickListener {
            KLTGContactCustomerServiceActivity.show(requireActivity() as com.catcsyun.liantadog.base.dj.BaseActivity)
        }

        binding.mineAppImg.setOnClickListener {
            stat++
            LZYLog.i("countDownTimerstat", "$stat")
            if (stat == 1) {
                countDownTimer.start()
            }
            if (stat > 5) {
                stat = 0
                showInputPasswordDialog()
            }
        }

        binding.mineDjId.text = UserInfoModel.getRiseId()
        binding.mineAppVersion.text = DeviceUtils.getVersionName(APP.instance)
        Glide.with(this)
            .load(R.mipmap.ic_app_logo)
            .transform(CenterCrop(),RoundedCorners(ScreenUtils.dip2px(50,requireContext())))
            .into(binding.mineAppImg)

        if (AppConst.is_show_ad){
            binding.mineLinUserKf.visibility=View.VISIBLE
        }else{
            binding.mineLinUserKf.visibility=View.GONE
        }

        binding.mineLinUserKf.thrillClickListener {
            CustomerConfig.setDjValue(AppConst.DJ_APP_ID, UserInfoModel.getDjid())
            SpecialCustomerServiceActivity.show(requireActivity())
        }
    }

    var inputPopupView: BasePopupView? = null
    private fun showInputPasswordDialog() {
        if (inputPopupView?.isShow == true) {
            return
        }
        val customPopup =
            QNInputPasswordDialogPopup(requireActivity())
        customPopup.listener = object : QNInputPasswordDialogPopup.OnInputPasswordListener {
            override fun cancel() {
            }

            override fun ok(password: String) {
                if (null != password && !TextUtils.isEmpty(password.trim())) {
                    inputPopupView?.dismiss()
                    GetHttpDataUtil.setWhiteListHttp(password.trim())
                } else {
                    ToastUtils.showLong("密码为空")
                }
            }
        }
        inputPopupView = XPopup.Builder(requireActivity())
            .autoOpenSoftInput(false)
            .autoDismiss(false)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(customPopup)
            .show()
    }

    /**
     * CountDownTimer 实现倒计时
     */
    private val countDownTimer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            stat = 0
            LZYLog.i("countDownTimerstat", "$stat")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageSimpleEvent(message: SimpleEvent) {
        if(message.simple == 4){
            LZYLog.e("simple","message simple:${message.simple}")

            ZYMAllAdsUtils.loadSimpleNoLimitAd1(requireActivity(),"信息",binding.feedContainerAbout)
        }
    }


}