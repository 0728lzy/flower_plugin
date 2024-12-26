package com.appcatdog.translations.ui.fragment

import android.graphics.Outline
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.appcatdog.translations.APP
import com.appcatdog.translations.AppConst
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.appcatdog.translations.R
import com.appcatdog.translations.base.dj.RootFragment
import com.appcatdog.translations.databinding.FragmentIndex2Binding
import com.appcatdog.translations.databinding.ItemDogBinding
import com.appcatdog.translations.entity.Index1Entity
import com.appcatdog.translations.event.SimpleEvent
import com.appcatdog.translations.ext.getBinding
import com.appcatdog.translations.ext.thrillClickListener
import com.appcatdog.translations.ui.activity.MainActivity
import com.appcatdog.translations.ui.activity.RBContactCustomerServiceActivity
import com.appcatdog.translations.ui.activity.SoundActivity
import com.appcatdog.translations.ui.activity.WebViewActivity
import com.appcatdog.translations.utils.dj.DeviceUtils
import com.appcatdog.translations.utils.dj.GetHttpDataUtil
import com.appcatdog.translations.utils.dj.UserInfoModel
import com.appcatdog.translations.utils.lzy.LZYADSUtils
import com.appcatdog.translations.utils.lzy.LZYLog
import com.appcatdog.translations.utils.lzy.ScreenUtils
import com.appcatdog.translations.widget.popup.dj.QNInputPasswordDialogPopup
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


class Index5Fragment : RootFragment(R.layout.fragment_index_2) {

    var _binding: FragmentIndex2Binding? = null
    private lateinit var lzyadsUtils: LZYADSUtils
    private lateinit var myActivity: MainActivity
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
        myActivity = requireActivity() as MainActivity
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
            WebViewActivity.forward(
                requireContext(),
                getString(R.string.privacy_policy),
                AppConst.URL_PRIVACY_POLICY
            )
        }
        userProLinearLayout.thrillClickListener {
            WebViewActivity.forward(
                requireContext(),
                getString(R.string.user_agreement),
                AppConst.URL_USER_AGREEMENT
            )
        }
        feedbackLinearLayout.thrillClickListener {
            RBContactCustomerServiceActivity.show(requireContext())
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
            .load(R.drawable.ic_app_logo)
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