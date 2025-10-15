package com.cslt.maogoufanyi.ui.fragment

import android.os.Bundle
import android.view.View
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.RootFragment
import com.cslt.maogoufanyi.databinding.FragmentHomeBinding
import com.cslt.maogoufanyi.event.SimpleEvent
import com.cslt.maogoufanyi.ext.getBinding
import com.cslt.maogoufanyi.ui.activity.MainActivity
import com.cslt.maogoufanyi.utils.dj.UserInfoModel
import com.cslt.maogoufanyi.utils.lzy.LZYADSUtils
import com.cslt.maogoufanyi.utils.lzy.LZYLog
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