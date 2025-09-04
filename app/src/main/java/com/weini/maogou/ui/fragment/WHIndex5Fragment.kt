package com.weini.maogou.ui.fragment

import android.graphics.Outline
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.weini.maogou.APP
import com.weini.maogou.AppConst
import com.weini.maogou.R
import com.weini.maogou.base.dj.RootFragment
import com.weini.maogou.databinding.FragmentIndex2Binding
import com.weini.maogou.event.SimpleEvent
import com.weini.maogou.ext.getBinding
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.ui.activity.WHMainActivity
import com.weini.maogou.ui.activity.WHContactCustomerServiceActivity
import com.weini.maogou.ui.activity.WHWebViewActivity
import com.weini.maogou.utils.dj.DeviceUtils
import com.weini.maogou.utils.dj.GetHttpDataUtil
import com.weini.maogou.utils.dj.UserInfoModel
import com.weini.maogou.utils.lzy.LZYADSUtils
import com.weini.maogou.utils.lzy.LZYLog
import com.weini.maogou.utils.lzy.ScreenUtils
import com.weini.maogou.widget.popup.dj.QNInputPasswordDialogPopup
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WHIndex5Fragment : RootFragment(R.layout.fragment_index_2) {

    var _binding: FragmentIndex2Binding? = null
    private lateinit var lzyadsUtils: LZYADSUtils
    private lateinit var myActivity: WHMainActivity
    private lateinit var mineLinearLayout: LinearLayout
    private lateinit var privacyLinearLayout: LinearLayout
    private lateinit var userProLinearLayout: LinearLayout
    private lateinit var feedbackLinearLayout: LinearLayout
    private lateinit var versionTextView: TextView
    private lateinit var djIdTextView: TextView
    private lateinit var appLogoImageView: ImageView
    private var stat = 0


    val binding get() = _binding!!

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
            lzyadsUtils.loadSimpleAdTurn(binding.fragmentHomeAdv,-1)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("Index2Fragment", requireActivity())
        myActivity = requireActivity() as WHMainActivity
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
            WHWebViewActivity.forward(
                requireContext(),
                getString(R.string.privacy_policy),
                AppConst.URL_PRIVACY_POLICY
            )
        }
        userProLinearLayout.thrillClickListener {
            WHWebViewActivity.forward(
                requireContext(),
                getString(R.string.user_agreement),
                AppConst.URL_USER_AGREEMENT
            )
        }
        feedbackLinearLayout.thrillClickListener {
            WHContactCustomerServiceActivity.show(requireContext())
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
        Glide.with(this)
            .load(R.mipmap.ic_app_logo)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(ScreenUtils.dip2px(10,myActivity)))) // 同时应用centerCrop和圆角
            .into(binding.mineAppImg)
    }

    var inputPopupView: BasePopupView? = null
    private fun showInputPasswordDialog() {
        if (inputPopupView?.isShow == true) {
            return
        }
        val customPopup =
            QNInputPasswordDialogPopup(requireContext())
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
        inputPopupView = XPopup.Builder(requireContext())
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
}