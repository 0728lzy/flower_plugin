package com.qingchu.wangmiao.ui.fragment

import android.os.Bundle
import android.view.View
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.RootFragment
import com.qingchu.wangmiao.databinding.FragmentHomeBinding
import com.qingchu.wangmiao.event.SimpleEvent
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ui.activity.WHMainActivity
import com.qingchu.wangmiao.utils.dj.UserInfoModel
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils
import com.qingchu.wangmiao.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeFragment : RootFragment(R.layout.fragment_home) {

    var _binding: FragmentHomeBinding? = null
    private lateinit var lzyadsUtils: LZYADSUtils
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

            lzyadsUtils.loadSimpleAd1(binding.feedContainerHome)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("Index1Fragment", requireActivity())
        LZYLog.e("simple", "message simple:${111111}")
        val currentTimeMillis = System.currentTimeMillis()
        val showTime = UserInfoModel.getShowChapingYynTime()
        if (currentTimeMillis - showTime > 2 * 1000) {
            UserInfoModel.setShowChapingYynTime(currentTimeMillis)
            lzyadsUtils.loadSimpleAd1(binding.feedContainerHome)
        }
        // 叫声翻译卡片点击事件
        binding.llSoundTranslate.setOnClickListener {

            // 这里可以添加跳转逻辑
            (requireActivity() as WHMainActivity).tabChange(4)
        }

        // 对话翻译卡片点击事件
        binding.llDialogTranslate.setOnClickListener {
            (requireActivity() as WHMainActivity).tabChange(2)
            // 这里可以添加跳转逻辑
        }

        // 训练文章卡片点击事件
        binding.llTrainingArticles.setOnClickListener {
            (requireActivity() as WHMainActivity).tabChange(1)
            // 这里可以添加跳转逻辑
        }

        // 虚拟视频卡片点击事件
        binding.llVirtualVideo.setOnClickListener {
            // 这里可以添加跳转逻辑
            (requireActivity() as WHMainActivity).tabChange(3)
        }
    }

}