package com.cslt.maogoufanyi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.cslt.maogoufanyi.csj.AdCPNoLimitUtils
import com.cslt.maogoufanyi.csj.AdCPUtils
import com.cslt.maogoufanyi.csj.AdCPTwoUtils
import com.cslt.maogoufanyi.utils.lzy.LZYLog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.cslt.maogoufanyi.AppConst
import com.cslt.maogoufanyi.csj.lzy.LZYInitCPAdsUtils
import com.cslt.maogoufanyi.databinding.ActivityMainBinding
import com.cslt.maogoufanyi.dialog.AgreementCancelDialog
import com.cslt.maogoufanyi.dialog.AgreementDialog
import com.cslt.maogoufanyi.dialog.DialogCallBack
import com.cslt.maogoufanyi.event.SimpleEvent
import com.cslt.maogoufanyi.ext.thrillClickListener
import com.cslt.maogoufanyi.ui.fragment.AboutFragment
import com.cslt.maogoufanyi.ui.fragment.CatLanguageFragment
import com.cslt.maogoufanyi.ui.fragment.DogLanguageFragment
import com.cslt.maogoufanyi.ui.fragment.PetManagementFragment
import com.cslt.maogoufanyi.ui.fragment.PetVideoFragment
import com.cslt.maogoufanyi.utils.dj.SetListAppHttpUtil
import com.cslt.maogoufanyi.utils.dj.UserInfoModel
import com.cslt.maogoufanyi.utils.lzy.LZYADSUtils
import com.cslt.maogoufanyi.widget.dialog.LoadingDiaLog
import com.cslt.maogoufanyi.widget.dialog.dj.VipDialog
import com.cslt.maogoufanyi.widget.popup.dj.ExitDialogPopup
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity() {

    companion object {
        fun forward(context: BaseActivity) {
            AppConst.splashInfoShowMainCP=true
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }


    private var isShowYSDialog = false
    override fun getLayoutId() = R.layout.activity_main

    lateinit var binding: ActivityMainBinding
    private lateinit var lzyadsUtils: LZYADSUtils
    var isFirst=true

    val fragments = listOf<Fragment>(
        DogLanguageFragment(),
        CatLanguageFragment(),
        PetManagementFragment(),
        PetVideoFragment(),
        AboutFragment(),
    )

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.bind(view)
        lzyadsUtils=LZYADSUtils("MainActivity",this)


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
                val channelName = AppConst.CHANNEL.uppercase()
                LZYLog.i("lzyp","channelName:$channelName")
                if (AppConst.is_show_ad && (channelName.equals("HONOR"))) {
                    LZYLog.i("lzyp","channelName:$channelName")
                    Handler().postDelayed({
                        SetListAppHttpUtil.setList(this@MainActivity);
                    },800)
                }
            }
        })

        binding.bottomBar.tabCatLanguage.thrillClickListener {

            tabChange(0)
        }
        binding.bottomBar.tabDogLanguage.thrillClickListener {

            tabChange(1)
        }
        binding.bottomBar.tabPetManagement.thrillClickListener {

            tabChange(2)
        }
        binding.bottomBar.tabCuteVideo.thrillClickListener {

            tabChange(3)
        }
        binding.bottomBar.tabProfile.thrillClickListener {

            tabChange(4)
        }

        if(!TextUtils.isEmpty(UserInfoModel.getRiseId())) {
            binding.splashAppDjid.text = UserInfoModel.getRiseId()
        }

    }

     fun tabChange(index: Int) {
        // 重置所有选项的背景色为默认灰色
        binding.bottomBar.tabCatLanguage.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_default_background))
        binding.bottomBar.tabDogLanguage.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_default_background))
        binding.bottomBar.tabPetManagement.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_default_background))
        binding.bottomBar.tabCuteVideo.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_default_background))
        binding.bottomBar.tabProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_default_background))
        
        // 重置所有图标和文字颜色
        binding.bottomBar.ivCatLanguage.setImageResource(R.drawable.icon_index1_n)
        binding.bottomBar.ivDogLanguage.setImageResource(R.drawable.icon_index2_n)
        binding.bottomBar.ivPetManagement.setImageResource(R.drawable.icon_index3_n)
        binding.bottomBar.ivCuteVideo.setImageResource(R.drawable.icon_index4_n)
        binding.bottomBar.ivProfile.setImageResource(R.drawable.icon_index5_n)
        
        binding.bottomBar.tvCatLanguage.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tvDogLanguage.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tvPetManagement.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tvCuteVideo.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.color_777777))

        // 根据选中的索引设置对应的背景色、图标和文字颜色
        when (index) {
            0 -> {
                binding.bottomBar.tabCatLanguage.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_cat_language_selected))
                binding.bottomBar.ivCatLanguage.setImageResource(R.drawable.icon_index1_s)
                binding.bottomBar.tvCatLanguage.setTextColor(ContextCompat.getColor(this, R.color.color_main_tab_1))
            }

            1 -> {
                binding.bottomBar.tabDogLanguage.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_dog_language_selected))
                binding.bottomBar.ivDogLanguage.setImageResource(R.drawable.icon_index2_s)
                binding.bottomBar.tvDogLanguage.setTextColor(ContextCompat.getColor(this, R.color.color_main_tab_2))
            }

            2 -> {
                binding.bottomBar.tabPetManagement.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_pet_management_selected))
                binding.bottomBar.ivPetManagement.setImageResource(R.drawable.icon_index3_s)
                binding.bottomBar.tvPetManagement.setTextColor(ContextCompat.getColor(this, R.color.color_main_tab_3))
            }

            3 -> {
                binding.bottomBar.tabCuteVideo.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_cute_video_selected))
                binding.bottomBar.ivCuteVideo.setImageResource(R.drawable.icon_index4_s)
                binding.bottomBar.tvCuteVideo.setTextColor(ContextCompat.getColor(this, R.color.color_main_tab_4))
            }

            4 -> {
                binding.bottomBar.tabProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_profile_selected))
                binding.bottomBar.ivProfile.setImageResource(R.drawable.icon_index5_s)
                binding.bottomBar.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.color_main_tab_5))
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
            LZYADSUtils("APP", this).initSimpleAd4(this@MainActivity)
        }
        VipDialog.showDialog(this, object : DialogCallBack {
            override fun buAgree() {
                val advDiaLog = LoadingDiaLog(this@MainActivity, "加载中...")
                advDiaLog.show()

                LZYADSUtils("APP", this@MainActivity).showAdJL(advDiaLog) {
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
                    LZYLog.e(this@MainActivity, "first one cp onSuccess")
                    AdCPNoLimitUtils.showInterstitialFullAd(this@MainActivity)
                }

                override fun onError() {
                    LZYLog.e(this@MainActivity, "first one cp onError")

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
                    LZYLog.e(this@MainActivity, "first one cp onSuccess")
                    AdCPNoLimitUtils.showInterstitialFullAd(this@MainActivity)
                }

                override fun onError() {
                    LZYLog.e(this@MainActivity, "first one cp onError")

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
                    LZYInitCPAdsUtils.showAdCpTurnNormal(this@MainActivity)
                }
                override fun disagree() {
                    UserInfoModel.setIsFirstNormal(false)
                    LZYInitCPAdsUtils.showAdCpTurnNormal(this@MainActivity)
                }
            })
        }

    }





    private fun firstShowAd2Dialog() {
        AppConst.is_show_ad = UserInfoModel.getIsShowAd()
        AgreementCancelDialog.showDialog(this, object : DialogCallBack {
            override fun buAgree() {
                UserInfoModel.setIsFirstNormal(false)
                LZYInitCPAdsUtils.showAdCpTurnNormal(this@MainActivity)
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
                LZYLog.e(this@MainActivity, "first two cp onSuccess")
                AdCPTwoUtils.showInterstitialFullAd(this@MainActivity)
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