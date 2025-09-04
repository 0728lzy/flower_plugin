package com.weini.maogou.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.weini.maogou.AppConst
import com.weini.maogou.R
import com.weini.maogou.base.dj.RootFragment
import com.weini.maogou.csj.AdFeedSimpleOneNoLimitUtils
import com.weini.maogou.csj.AdFeedSimpleOneUtils
import com.weini.maogou.databinding.FragmentIndex1Binding
import com.weini.maogou.databinding.ItemDogBinding
import com.weini.maogou.entity.Index1Entity
import com.weini.maogou.event.SimpleEvent
import com.weini.maogou.ext.getBinding
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.ui.activity.WHSoundActivity
import com.weini.maogou.utils.dj.UserInfoModel
import com.weini.maogou.utils.lzy.LZYADSUtils
import com.weini.maogou.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WHIndex1Fragment : RootFragment(R.layout.fragment_index_1) {

    var _binding: FragmentIndex1Binding? = null
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
        if (message.simple == 4) {
            LZYLog.e("simple", "message simple:${message.simple}")
            loadSimpleAd1(binding.feedContainerFragment1)
        }
    }

    fun loadSimpleAd1(fragment: FrameLayout?) {
        if (requireActivity() != null &&(!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad) ) {
            AdFeedSimpleOneNoLimitUtils.init(
                requireActivity(),
                object : AdFeedSimpleOneNoLimitUtils.GirdMenuStateListener {
                    override fun onSuccess() {

                        if (fragment != null && requireActivity() != null) {
                            Log.i("tttt", "准备刷新视频列表的小信息流")
                            AdFeedSimpleOneNoLimitUtils.showAd(fragment, requireActivity())
                        }
                    }

                    override fun onError() {
                    }
                })
            AdFeedSimpleOneNoLimitUtils.initPreloading()
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("Index1Fragment", requireActivity())

        binding.tvDog.thrillClickListener {
            binding.tvDog.setBackgroundResource(R.drawable.border_txt_tab_training)
            binding.tvDog.setTextColor(Color.WHITE)
            binding.tvCat.setBackgroundResource(R.drawable.border_txt_tab_two_training)
            binding.tvCat.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_777777
                )
            )
            dog()
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1, -1)
        }
        binding.tvCat.thrillClickListener {
            binding.tvCat.setBackgroundResource(R.drawable.border_txt_tab_training)
            binding.tvCat.setTextColor(Color.WHITE)
            binding.tvDog.setBackgroundResource(R.drawable.border_txt_tab_two_training)
            binding.tvDog.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_777777
                )
            )
            cat()
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1, -1)
        }

        binding.rvList.grid(3).setup {

            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {
                        WHSoundActivity.show(requireContext(), type == 1, modelPosition)
                    }
                }
            }
        }
        dog()
    }

    private fun dog() {
        type = 1
        binding.rvList.bindingAdapter.models =
            com.weini.maogou.AppConst.dogSoundList(requireContext()).shuffled()
    }

    private fun cat() {
        type = 2
        binding.rvList.bindingAdapter.models =
            com.weini.maogou.AppConst.catSoundList(requireContext()).shuffled()
    }
}