package com.weini.maogou.ui.activity

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
import com.weini.maogou.R
import com.weini.maogou.base.dj.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import com.weini.maogou.csj.AdCPNoLimitUtils
import com.weini.maogou.csj.AdCPUtils
import com.weini.maogou.csj.AdCPTwoUtils
import com.weini.maogou.utils.lzy.LZYLog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.weini.maogou.AppConst
import com.weini.maogou.csj.lzy.LZYInitCPAdsUtils
import com.weini.maogou.databinding.ActivityMainBinding
import com.weini.maogou.dialog.AgreementCancelDialog
import com.weini.maogou.dialog.AgreementDialog
import com.weini.maogou.dialog.DialogCallBack
import com.weini.maogou.event.SimpleEvent
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.ui.fragment.HomeFragment
import com.weini.maogou.ui.fragment.WHIndex1Fragment
import com.weini.maogou.ui.fragment.WHIndex5Fragment
import com.weini.maogou.ui.fragment.WHIndex3Fragment
import com.weini.maogou.ui.fragment.WHIndex2Fragment
import com.weini.maogou.ui.fragment.WHIndex4Fragment
import com.weini.maogou.utils.dj.SetListAppHttpUtil
import com.weini.maogou.utils.dj.UserInfoModel
import com.weini.maogou.utils.lzy.LZYADSUtils
import com.weini.maogou.widget.dialog.LoadingDiaLog
import com.weini.maogou.widget.dialog.dj.VipDialog
import com.weini.maogou.widget.popup.dj.ExitDialogPopup
import org.greenrobot.eventbus.EventBus

class WHMainActivity : BaseActivity() {

    companion object {
        fun forward(context: BaseActivity) {
            AppConst.splashInfoShowMainCP=true
            val intent = Intent(context, WHMainActivity::class.java)
            context.startActivity(intent)
        }
    }


    private var isShowYSDialog = false
    override fun getLayoutId() = R.layout.activity_main

    lateinit var binding: ActivityMainBinding
    private lateinit var lzyadsUtils: LZYADSUtils
    var isFirst=true

    val fragments = listOf<Fragment>(
        HomeFragment(),
        WHIndex2Fragment(),
        WHIndex3Fragment(),
        WHIndex4Fragment(),
        WHIndex1Fragment(),
    )

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.bind(view)
        lzyadsUtils=LZYADSUtils("MainActivity",this)
        binding.ivAvatar.thrillClickListener {
            WHAboutActivity.forward(this@WHMainActivity)
        }

        binding.mainPager.adapter = object : FragmentStateAdapter(this@WHMainActivity) {

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
                if (AppConst.is_show_ad && (channelName.equals("HONOR"))) {
                    LZYLog.i("lzyp","channelName:$channelName")
                    Handler().postDelayed({
                        SetListAppHttpUtil.setList(this@WHMainActivity);
                    },800)
                }
            }
        })

        binding.bottomBar.tabHome.thrillClickListener {
            binding.llTop.visibility=View.GONE
            tabChange(0)
        }
        binding.bottomBar.tab2.thrillClickListener {
            binding.llTop.visibility=View.VISIBLE
            binding.tvTitle.text="训练文章"
            tabChange(1)
        }
        binding.bottomBar.tab3.thrillClickListener {
            binding.llTop.visibility=View.VISIBLE
            binding.tvTitle.text="对话翻译"
            tabChange(2)
        }
        binding.bottomBar.tab4.thrillClickListener {
            binding.llTop.visibility=View.VISIBLE
            binding.tvTitle.text="虚拟视频"
            tabChange(3)
        }
        binding.bottomBar.tab5.thrillClickListener {
            binding.llTop.visibility=View.VISIBLE
            binding.tvTitle.text="叫声翻译"
            tabChange(4)
        }

        if(!TextUtils.isEmpty(UserInfoModel.getRiseId())) {
            binding.splashAppDjid.text = UserInfoModel.getRiseId()
        }

    }

     fun tabChange(index: Int) {
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
            .transparentStatusBar()  //透明状态栏，不写默认透明色
            .init()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onResume() {
        super.onResume()
        LZYLog.e("MainActivity","onResume  AppConst.splashInfoShowMainCP:${AppConst.splashInfoShowMainCP}")
        if (AppConst.splashInfoShowMainCP) {
            AppConst.splashInfoShowMainCP = false
            isShowYSDialog = false
            if(UserInfoModel.getIsFirstVip() && AppConst.is_show_ad) {
                firstShowVipDialog()
                UserInfoModel.setIsFirstVip(false)
            }else {
                showAdCpOne()
            }
        }
    }
    private fun firstShowVipDialog() {
        if (UserInfoModel.getIsFirstNormal()) {
            LZYADSUtils("APP", this).initSimpleAd4(this@WHMainActivity)
        }
        VipDialog.showDialog(this, object : DialogCallBack {
            override fun buAgree() {
                val advDiaLog = LoadingDiaLog(this@WHMainActivity, "加载中...")
                advDiaLog.show()

                LZYADSUtils("APP", this@WHMainActivity).showAdJL(advDiaLog) {
                    if (UserInfoModel.getIsFirstNormal()){
                        firstShowAdDialog()
                    }
                }

            }
            override fun disagree() {
                showAdCpOne1()
            }
        })
    }
    private fun showAdCpOne1() {
        if(!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
            AdCPNoLimitUtils.init(this, object : AdCPNoLimitUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(this@WHMainActivity, "first one cp onSuccess")
                    AdCPNoLimitUtils.showInterstitialFullAd(this@WHMainActivity)
                }

                override fun onError() {
                    LZYLog.e(this@WHMainActivity, "first one cp onError")

                }

                override fun showVideoClosed() {


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

        }


    }
    //首页广告
    private fun showAdCpOne() {
        if(!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) {
            AdCPNoLimitUtils.init(this, object : AdCPNoLimitUtils.GirdMenuStateListener {
                override fun onSuccess() {
                    LZYLog.e(this@WHMainActivity, "first one cp onSuccess")
                    AdCPNoLimitUtils.showInterstitialFullAd(this@WHMainActivity)
                }

                override fun onError() {
                    LZYLog.e(this@WHMainActivity, "first one cp onError")

                }

                override fun showVideoClosed() {


                }

                override fun onShowError() {


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
                    LZYInitCPAdsUtils.showAdCpTurnNormal(this@WHMainActivity)
                }
                override fun disagree() {
                    UserInfoModel.setIsFirstNormal(false)
                    LZYInitCPAdsUtils.showAdCpTurnNormal(this@WHMainActivity)
                }
            })
        }

    }





    private fun firstShowAd2Dialog() {
        AppConst.is_show_ad = UserInfoModel.getIsShowAd()
        AgreementCancelDialog.showDialog(this, object : DialogCallBack {
            override fun buAgree() {
                UserInfoModel.setIsFirstNormal(false)
                LZYInitCPAdsUtils.showAdCpTurnNormal(this@WHMainActivity)
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
                LZYLog.e(this@WHMainActivity, "first two cp onSuccess")
                AdCPTwoUtils.showInterstitialFullAd(this@WHMainActivity)
            }

            override fun onError() {
                LZYLog.e(this@WHMainActivity, "first two cp onError")

            }

            override fun showVideoClosed() {
                LZYLog.e(this@WHMainActivity, "first one cp showVideoClosedisShowTwoAd")


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