package com.pet.translator.ui.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.pet.translator.R
import com.pet.translator.base.dj.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.kwad.sdk.api.util.GMCPAdNoLimitUtils
import com.kwad.sdk.api.util.GMCPAdUtils
import com.kwad.sdk.api.util.GMCPTwoAdUtils
import com.pet.translator.utils.lzy.LZYLog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.pet.translator.AppConst
import com.pet.translator.databinding.ActivityMainBinding
import com.pet.translator.event.SimpleEvent
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.ui.fragment.Index1Fragment
import com.pet.translator.ui.fragment.Index2Fragment
import com.pet.translator.ui.fragment.Index3Fragment
import com.pet.translator.ui.fragment.Index4Fragment
import com.pet.translator.ui.fragment.Index5Fragment
import com.pet.translator.utils.dj.UserInfoModel
import com.pet.translator.utils.lzy.LZYADSUtils
import com.pet.translator.widget.popup.dj.ExitDialogPopup
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity() {

    companion object {
        fun forward(context: BaseActivity) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            context.overridePendingTransition(0, 0)
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    lateinit var binding: ActivityMainBinding
    private lateinit var lzyadsUtils: LZYADSUtils
    var isFirst=true

    val fragments = listOf<Fragment>(
        Index1Fragment(),
        Index2Fragment(),
        Index3Fragment(),
        Index4Fragment(),
        Index5Fragment(),
    )

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.bind(view)
        lzyadsUtils=LZYADSUtils("MainActivity",this)
        binding.ivAbout.thrillClickListener {
            AboutActivity.forward(this@MainActivity)
        }

        binding.mainPager.adapter = object : FragmentStateAdapter(this@MainActivity) {

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
    }

    private fun tabChange(index: Int) {
        binding.bottomBar.iv1.setImageResource(R.drawable.ic_index1_n)
        binding.bottomBar.iv2.setImageResource(R.drawable.ic_index2_n)
        binding.bottomBar.iv3.setImageResource(R.drawable.ic_index3_n)
        binding.bottomBar.iv4.setImageResource(R.drawable.ic_index4_n)
        binding.bottomBar.iv5.setImageResource(R.drawable.ic_index5_n)
        binding.bottomBar.tv1.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv2.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv3.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv4.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv5.setTextColor(ContextCompat.getColor(this, R.color.color_777777))

        when (index) {
            0 -> {
                binding.bottomBar.tv1.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv1.setImageResource(R.drawable.ic_index1_s)
            }

            1 -> {
                binding.bottomBar.tv2.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv2.setImageResource(R.drawable.ic_index2_s)
            }

            2 -> {
                binding.bottomBar.tv3.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv3.setImageResource(R.drawable.ic_index3_s)
            }

            3 -> {
                binding.bottomBar.tv4.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv4.setImageResource(R.drawable.ic_index4_s)
            }

            4 -> {
                binding.bottomBar.tv5.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv5.setImageResource(R.drawable.ic_index5_s)
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
            showAdCpOne()
        }
    }

    //首页广告
    private fun showAdCpOne() {
        if(!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
            GMCPAdNoLimitUtils.init(this, object : GMCPAdNoLimitUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(this@MainActivity, "first one cp onSuccess")
                    GMCPAdNoLimitUtils.showInterstitialFullAd(this@MainActivity)
                }

                override fun onError() {
                    LZYLog.e(this@MainActivity, "first one cp onError")

                }

                override fun showVideoClosed() {

                    if(AppConst.is_show_ad  && !AppConst.isWaked){
                        if(GMCPTwoAdUtils.isReady()) {
                            GMCPTwoAdUtils.showInterstitialFullAd(this@MainActivity)
                        }
                    }
                }

                override fun onShowError() {

                }
            })
            if (!GMCPAdNoLimitUtils.isReady()) {
                GMCPAdNoLimitUtils.initPreloading("")
            } else {
                GMCPAdNoLimitUtils.showInterstitialFullAd(this)
            }
            if (AppConst.is_show_ad && !AppConst.isWaked){
                showAdCpTwo()
            }

        }


    }

    private fun showAdCpTwo() {
        AppConst.isWaked=false
        GMCPTwoAdUtils.init(this, object : GMCPTwoAdUtils.GirdMenuStateListener {
            override fun onSuccess() {
                LZYLog.e(this@MainActivity, "first two cp onSuccess")
            }

            override fun onError() {
                LZYLog.e(this@MainActivity, "first two cp onError")

            }

            override fun showVideoClosed() {
                LZYLog.e(this@MainActivity, "first one cp showVideoClosedisShowTwoAd")


            }

            override fun onShowError() {

            }
        })
        if(!GMCPTwoAdUtils.isReady()) {
            Handler().postDelayed({
                if (AppConst.is_show_ad) {
                    GMCPTwoAdUtils.initPreloading("")
                }
            }, 1000)
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
        GMCPAdUtils.init(this, object : GMCPAdUtils.GirdMenuStateListener {
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
        if(!GMCPAdUtils.isReady()) {
            GMCPAdUtils.initPreloading(AppConst.GMCPAd_ID_IN) //显示插屏广告
        }
    }

    fun showExitCpAdData() {
        if (GMCPAdUtils.isReady()) {
            GMCPAdUtils.showInterstitialFullAd(this) //显示插屏广告
        } else {
            if (isExitApp) {
                moveTaskToBack(true)
            }
        }
    }
}