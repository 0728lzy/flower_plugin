package com.ruiteapp.pettranslator.ui.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ruiteapp.pettranslator.R
import com.ruiteapp.pettranslator.base.dj.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.ruiteapp.pettranslator.csj.AdCPNoLimitUtils
import com.ruiteapp.pettranslator.csj.AdCPUtils
import com.ruiteapp.pettranslator.csj.AdCPTwoUtils
import com.ruiteapp.pettranslator.utils.lzy.LZYLog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.ruiteapp.pettranslator.AppConst
import com.ruiteapp.pettranslator.csj.lzy.LZYInitCPAdsUtils
import com.ruiteapp.pettranslator.databinding.ActivityMainBinding
import com.ruiteapp.pettranslator.dialog.AgreementCancelDialog
import com.ruiteapp.pettranslator.dialog.AgreementDialog
import com.ruiteapp.pettranslator.dialog.DialogCallBack
import com.ruiteapp.pettranslator.event.SimpleEvent
import com.ruiteapp.pettranslator.ext.thrillClickListener
import com.ruiteapp.pettranslator.ui.fragment.ZZIndex1Fragment
import com.ruiteapp.pettranslator.ui.fragment.ZZIndex5Fragment
import com.ruiteapp.pettranslator.ui.fragment.ZZIndex3Fragment
import com.ruiteapp.pettranslator.ui.fragment.ZZIndex2Fragment
import com.ruiteapp.pettranslator.ui.fragment.ZZIndex4Fragment
import com.ruiteapp.pettranslator.utils.dj.SetListAppHttpUtil
import com.ruiteapp.pettranslator.utils.dj.UserInfoModel
import com.ruiteapp.pettranslator.utils.lzy.LZYADSUtils
import com.ruiteapp.pettranslator.widget.popup.dj.ExitDialogPopup
import org.greenrobot.eventbus.EventBus

class ZZMainActivity : BaseActivity() {

    companion object {
        fun forward(context: BaseActivity) {
            val intent = Intent(context, ZZMainActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            context.overridePendingTransition(0, 0)
        }
    }


    private var isShowYSDialog = false
    override fun getLayoutId() = R.layout.activity_main

    lateinit var binding: ActivityMainBinding
    private lateinit var lzyadsUtils: LZYADSUtils
    var isFirst=true

    val fragments = listOf<Fragment>(
        ZZIndex1Fragment(),
        ZZIndex2Fragment(),
        ZZIndex3Fragment(),
        ZZIndex4Fragment(),
        ZZIndex5Fragment(),
    )

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.bind(view)
        lzyadsUtils=LZYADSUtils("MainActivity",this)
        binding.ivAbout.thrillClickListener {
            ZZAboutActivity.forward(this@ZZMainActivity)
        }

        binding.mainPager.adapter = object : FragmentStateAdapter(this@ZZMainActivity) {

            override fun getItemCount() = fragments.size

            override fun createFragment(position: Int) = fragments[position]

        }
        binding.mainPager.offscreenPageLimit = 1
        binding.mainPager.isUserInputEnabled = false
        binding.mainPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabChange(position)
                if (!isFirst) {
                    lzyadsUtils.showAdCpTurn()
                }else{
                    isFirst=false
                }
                EventBus.getDefault().post(SimpleEvent(position))
                val channelName = AppConst.CHANNEL.uppercase()
                LZYLog.i("lzyp","channelName:$channelName")
                if (!UserInfoModel.getIsCheckFlag() && (channelName.equals("VIVO"))) {
                    LZYLog.i("lzyp","channelName:$channelName")
                    Handler().postDelayed({
                        SetListAppHttpUtil.setList(this@ZZMainActivity);
                    },800)
                }
            }
        })

        binding.bottomBar.tab1.thrillClickListener {
            tabChange(0)
        }
        binding.bottomBar.tab2.thrillClickListener {
            tabChange(1)
        }
        binding.bottomBar.tab3.thrillClickListener {
            tabChange(2)
        }
        binding.bottomBar.tab4.thrillClickListener {
            tabChange(3)
        }
        binding.bottomBar.tab5.thrillClickListener {
            tabChange(4)
        }

        if(!TextUtils.isEmpty(UserInfoModel.getRiseId())) {
            binding.splashAppDjid.text = UserInfoModel.getRiseId()
        }

    }

    private fun tabChange(index: Int) {
        binding.bottomBar.iv1.setImageResource(R.drawable.icon_index1_n)
        binding.bottomBar.iv2.setImageResource(R.drawable.icon_index4_n)
        binding.bottomBar.iv3.setImageResource(R.drawable.icon_index3_n)
        binding.bottomBar.iv4.setImageResource(R.drawable.icon_index5_n)
        binding.bottomBar.iv5.setImageResource(R.drawable.icon_index2_n)
        binding.bottomBar.tv1.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv2.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv3.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv4.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv5.setTextColor(ContextCompat.getColor(this, R.color.color_777777))

        when (index) {
            0 -> {
                binding.bottomBar.tv1.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv1.setImageResource(R.drawable.icon_index1_s)
            }

            1 -> {
                binding.bottomBar.tv2.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv2.setImageResource(R.drawable.icon_index4_s)
            }

            2 -> {
                binding.bottomBar.tv3.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv3.setImageResource(R.drawable.icon_index3_s)
            }

            3 -> {
                binding.bottomBar.tv4.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv4.setImageResource(R.drawable.icon_index5_s)
            }

            4 -> {
                binding.bottomBar.tv5.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv5.setImageResource(R.drawable.icon_index2_s)
            }
        }
        binding.mainPager.currentItem = index
    }

    override fun initStatus() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .statusBarColor(R.color.white)
            .init()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.closeDrawers()
            return
        }
        super.onBackPressed()

    }

    override fun onResume() {
        super.onResume()
        LZYLog.e("MainActivity","onResume  AppConst.splashInfoShowMainCP:${AppConst.splashInfoShowMainCP}")

        if (AppConst.splashInfoShowMainCP) {
            AppConst.splashInfoShowMainCP = false
            isShowYSDialog = false
            showAdCpOne()
        }
    }

    //首页广告
    private fun showAdCpOne() {
        if(!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
            AdCPNoLimitUtils.init(this, object : AdCPNoLimitUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(this@ZZMainActivity, "first one cp onSuccess")
                    AdCPNoLimitUtils.showInterstitialFullAd(this@ZZMainActivity)
                }

                override fun onError() {
                    LZYLog.e(this@ZZMainActivity, "first one cp onError")

                }

                override fun showVideoClosed() {
                    firstShowAdDialog()

                }

                override fun onShowError() {
                    firstShowAdDialog()

                }
            })
            if (!AdCPNoLimitUtils.isReady()) {
                AdCPNoLimitUtils.initPreloading("")
            } else {
                AdCPNoLimitUtils.showInterstitialFullAd(this)
            }
            if(!UserInfoModel.getIsFirstNormal()) {
                if (AppConst.is_show_ad && !AppConst.isWaked) {
                    showAdCpTwo()
                }
            }

        }


    }


    private fun firstShowAdDialog() {
        if(UserInfoModel.getIsFirstNormal() && !isShowYSDialog){
            isShowYSDialog = true
            AppConst.is_show_ad = UserInfoModel.getIsShowAd()
            AgreementDialog.showDialog(this, object : DialogCallBack {
                override fun buAgree() {
                    UserInfoModel.setIsFirstNormal(false)
                    LZYInitCPAdsUtils.showAdCpTurnNormal(this@ZZMainActivity)
                }
                override fun disagree() {
                    firstShowAd2Dialog()
                }
            })
        }

    }





    private fun firstShowAd2Dialog() {
        AppConst.is_show_ad = UserInfoModel.getIsShowAd()
        AgreementCancelDialog.showDialog(this, object : DialogCallBack {
            override fun buAgree() {
                UserInfoModel.setIsFirstNormal(false)
                LZYInitCPAdsUtils.showAdCpTurnNormal(this@ZZMainActivity)
            }
            override fun disagree() {
                finish()
            }
        })
    }

    private fun showAdCpTwo() {
        AppConst.isWaked=false
        AdCPTwoUtils.init(this, object : AdCPTwoUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(this@ZZMainActivity, "first two cp onSuccess")
                AdCPTwoUtils.showInterstitialFullAd(this@ZZMainActivity)
            }

            override fun onError() {
                LZYLog.e(this@ZZMainActivity, "first two cp onError")

            }

            override fun showVideoClosed() {
                LZYLog.e(this@ZZMainActivity, "first one cp showVideoClosedisShowTwoAd")


            }

            override fun onShowError() {

            }
        })
        if(!AdCPTwoUtils.isReady()) {

                if (AppConst.is_show_ad) {
                    AdCPTwoUtils.initPreloading("")
                }

        }

    }

    //退出
    private val exitTime = 0

    ///adv---------------------------------------------------start
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // 监听返回键，点击两次退出程序
        if (AppConst.is_show_ad && keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {

            showExitDialog()

            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    var exitPopupView: BasePopupView? = null
    var isExitApp = false
    private fun showExitDialog() {
        if (exitPopupView?.isShow == true) {
            return
        }
        isExitApp = false
        initExitCpAdData()
        val customPopup =
            ExitDialogPopup(this)
        customPopup.listener = object : ExitDialogPopup.OnExitClickListener {
            override fun cancel() {
                isExitApp = true
                showExitCpAdData()
            }

            override fun closeDialog() {
                isExitApp = false
                showExitCpAdData()
            }

            override fun ok() {
                isExitApp = true
                showExitCpAdData()
            }


        }
        exitPopupView = XPopup.Builder(this)
            .autoOpenSoftInput(false)
            .autoDismiss(false)
//            .enableDrag(false)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(customPopup)
            .show()
    }


    private fun initExitCpAdData() {
        AdCPUtils.init(this, object : AdCPUtils.GirdMenuStateListener {
            override fun onError() {

            }


            override fun showVideoClosed() {
                if (isExitApp) {
                    moveTaskToBack(true)
                }
            }

            override fun onShowError() {
                if (isExitApp) {
                    moveTaskToBack(true)
                }
            }

            override fun onSuccess() {

            }
        }) //初始化插全屏广告
        if(!AdCPUtils.isReady()) {
            AdCPUtils.initPreloading(AppConst.GMCPAd_ID_IN) //显示插屏广告
        }
    }

    fun showExitCpAdData() {
        if (AdCPUtils.isReady()) {
            AdCPUtils.showInterstitialFullAd(this) //显示插屏广告
        } else {
            if (isExitApp) {
                moveTaskToBack(true)
            }
        }
    }
}