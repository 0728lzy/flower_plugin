package com.weini.maogou.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.weini.maogou.R
import com.weini.maogou.base.dj.RootFragment
import com.weini.maogou.databinding.FragmentHomeBinding
import com.weini.maogou.databinding.FragmentIndex1Binding
import com.weini.maogou.databinding.ItemDogBinding
import com.weini.maogou.entity.Index1Entity
import com.weini.maogou.event.SimpleEvent
import com.weini.maogou.ext.getBinding
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.ui.activity.WHMainActivity
import com.weini.maogou.ui.activity.WHSoundActivity
import com.weini.maogou.utils.dj.UserInfoModel
import com.weini.maogou.utils.lzy.LZYADSUtils
import com.weini.maogou.utils.lzy.LZYLog
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