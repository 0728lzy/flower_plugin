package com.cslianta.catdog.ui.fragment

import android.os.Bundle
import android.view.View
import com.cslianta.catdog.R
import com.cslianta.catdog.base.dj.RootFragment
import com.cslianta.catdog.csj.ZYMAllAdsUtils
import com.cslianta.catdog.databinding.FragmentHomeBinding
import com.cslianta.catdog.event.SimpleEvent
import com.cslianta.catdog.ext.getBinding
import com.cslianta.catdog.ui.activity.MainActivity
import com.cslianta.catdog.utils.dj.UserInfoModel

import com.cslianta.catdog.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeFragment : RootFragment(R.layout.fragment_home) {

    var _binding: FragmentHomeBinding? = null

    var type = 1
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
        if (message.simple == 0) {
            LZYLog.e("simple", "message simple:${message.simple}")


            ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerHome)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()

        LZYLog.e("simple", "message simple:${111111}")
        val currentTimeMillis = System.currentTimeMillis()
        val showTime = UserInfoModel.getShowChapingYynTime()
        if (currentTimeMillis - showTime > 2 * 1000) {
            UserInfoModel.setShowChapingYynTime(currentTimeMillis)
            ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerHome)
        }
        // 叫声翻译卡片点击事件
        binding.llSoundTranslate.setOnClickListener {

            // 这里可以添加跳转逻辑
            (requireActivity() as MainActivity).tabChange(4)
        }

        // 对话翻译卡片点击事件
        binding.llDialogTranslate.setOnClickListener {
            (requireActivity() as MainActivity).tabChange(2)
            // 这里可以添加跳转逻辑
        }

        // 训练文章卡片点击事件
        binding.llTrainingArticles.setOnClickListener {
            (requireActivity() as MainActivity).tabChange(1)
            // 这里可以添加跳转逻辑
        }

        // 虚拟视频卡片点击事件
        binding.llVirtualVideo.setOnClickListener {
            // 这里可以添加跳转逻辑
            (requireActivity() as MainActivity).tabChange(3)
        }
    }

}