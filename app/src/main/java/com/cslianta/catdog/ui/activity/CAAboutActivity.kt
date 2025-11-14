package com.cslianta.catdog.ui.activity

import android.content.Intent
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
import com.cslianta.catdog.APP
import com.cslianta.catdog.AppConst
import com.cslianta.catdog.R
import com.cslianta.catdog.base.dj.BaseActivity
import com.cslianta.catdog.csj.ZYMAllAdsUtils
import com.cslianta.catdog.databinding.ActivityAboutBinding
import com.cslianta.catdog.ext.getBinding
import com.cslianta.catdog.ext.thrillClickListener
import com.cslianta.catdog.utils.dj.DeviceUtils
import com.cslianta.catdog.utils.dj.GetHttpDataUtil
import com.cslianta.catdog.utils.dj.UserInfoModel

import com.cslianta.catdog.utils.lzy.LZYLog
import com.cslianta.catdog.utils.lzy.ScreenUtils
import com.cslianta.catdog.widget.popup.dj.QNInputPasswordDialogPopup

class CAAboutActivity : BaseActivity() {

    companion object {
        fun forward(context: BaseActivity) {
            val intent = Intent(context, CAAboutActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_about

    private lateinit var binding: ActivityAboutBinding

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
        binding.toolbar.ivMenu.setImageResource(R.drawable.ic_arrow_back_24)
        binding.toolbar.tvTitle.text="个人中心"
        binding.toolbar.ivMenu.thrillClickListener {
            finish()
        }
        ZYMAllAdsUtils.showAdCpTurnTab(this@CAAboutActivity,"CP")
        ZYMAllAdsUtils.loadSimpleAll(this@CAAboutActivity,"",binding.feedContainerActivityAbout)
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
                    ScreenUtils.dip2px(15, this@CAAboutActivity).toFloat()
                )
            }
        }
        mineLinearLayout.clipToOutline = true
        privacyLinearLayout.thrillClickListener {
            CAWebViewActivity.forward(
                this@CAAboutActivity,
                getString(R.string.privacy_policy),
                AppConst.URL_PRIVACY_POLICY
            )
        }
        userProLinearLayout.thrillClickListener {
            CAWebViewActivity.forward(
                this@CAAboutActivity,
                getString(R.string.user_agreement),
                AppConst.URL_USER_AGREEMENT
            )
        }
        feedbackLinearLayout.thrillClickListener {
            CAContactCustomerServiceActivity.show(this@CAAboutActivity)
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
            QNInputPasswordDialogPopup(this@CAAboutActivity)
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
        inputPopupView = XPopup.Builder(this@CAAboutActivity)
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