package com.qingchu.wangmiao.ui.fragment

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
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qingchu.wangmiao.APP
import com.qingchu.wangmiao.AppConst
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.RootFragment
import com.qingchu.wangmiao.databinding.FragmentAboutBinding
import com.qingchu.wangmiao.event.SimpleEvent
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ext.thrillClickListener
import com.qingchu.wangmiao.ui.activity.QCContactCustomerServiceActivity
import com.qingchu.wangmiao.ui.activity.QCWebViewActivity
import com.qingchu.wangmiao.utils.dj.DeviceUtils
import com.qingchu.wangmiao.utils.dj.UserInfoModel
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils
import com.qingchu.wangmiao.utils.lzy.LZYLog
import com.qingchu.wangmiao.utils.lzy.ScreenUtils
import com.qingchu.wangmiao.widget.popup.dj.QNInputPasswordDialogPopup
import com.qingchu.wangmiao.utils.dj.GetHttpDataUtil
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

    private lateinit var lzyAdsUtils: LZYADSUtils

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = view.getBinding()
        
        lzyAdsUtils = LZYADSUtils("AboutFragment", requireActivity())
        lzyAdsUtils.showAdCpTurn()
        lzyAdsUtils.loadSimpleAdTurn(binding.feedContainerFragmentAbout, -1)
        
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
            QCWebViewActivity.forward(
                requireActivity() as com.qingchu.wangmiao.base.dj.BaseActivity,
                getString(R.string.privacy_policy),
                AppConst.URL_PRIVACY_POLICY
            )
        }
        
        userProLinearLayout.thrillClickListener {
            QCWebViewActivity.forward(
                requireActivity() as com.qingchu.wangmiao.base.dj.BaseActivity,
                getString(R.string.user_agreement),
                AppConst.URL_USER_AGREEMENT
            )
        }
        
        feedbackLinearLayout.thrillClickListener {
            QCContactCustomerServiceActivity.show(requireActivity() as com.qingchu.wangmiao.base.dj.BaseActivity)
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
        
        binding.mineDjId.text = UserInfoModel.getDjid()
        binding.mineAppVersion.text = DeviceUtils.getVersionName(APP.instance)
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
            LZYADSUtils("PetVideoFragment",requireActivity()).loadSimpleAd4(requireActivity(),binding.feedContainerAbout)
        }
    }
}