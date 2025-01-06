package com.ruiteapp.pettranslator.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.ruiteapp.pettranslator.R
import com.ruiteapp.pettranslator.base.dj.RootFragment
import com.ruiteapp.pettranslator.databinding.FragmentIndex1Binding
import com.ruiteapp.pettranslator.databinding.ItemDogBinding
import com.ruiteapp.pettranslator.entity.Index1Entity
import com.ruiteapp.pettranslator.event.SimpleEvent
import com.ruiteapp.pettranslator.ext.getBinding
import com.ruiteapp.pettranslator.ext.thrillClickListener
import com.ruiteapp.pettranslator.ui.activity.ZZSoundActivity
import com.ruiteapp.pettranslator.utils.lzy.LZYADSUtils
import com.ruiteapp.pettranslator.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ZZIndex1Fragment : RootFragment(R.layout.fragment_index_1) {

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
        if(message.simple == 0){
            LZYLog.e("simple","message simple:${message.simple}")
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1,-1)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("Index1Fragment", requireActivity())

        binding.tvDog.thrillClickListener {
            binding.tvDog.setBackgroundResource(R.drawable.border_txt_tab_training)
            binding.tvDog.setTextColor(Color.WHITE)
            binding.tvCat.setBackgroundResource(R.drawable.border_txt_tab_two_training)
            binding.tvCat.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_777777))
            dog()
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1,-1)
        }
        binding.tvCat.thrillClickListener {
            binding.tvCat.setBackgroundResource(R.drawable.border_txt_tab_training)
            binding.tvCat.setTextColor(Color.WHITE)
            binding.tvDog.setBackgroundResource(R.drawable.border_txt_tab_two_training)
            binding.tvDog.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_777777))
            cat()
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1,-1)
        }
        binding.rvList.linear().setup {

            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {
                        ZZSoundActivity.show(requireContext(), type==1, modelPosition)
                    }
                }
            }
        }
        dog()
    }

    private fun dog() {
        type = 1
        binding.rvList.bindingAdapter.models = com.ruiteapp.pettranslator.AppConst.dogSoundList(requireContext())
    }

    private fun cat() {
        type = 2
        binding.rvList.bindingAdapter.models = com.ruiteapp.pettranslator.AppConst.catSoundList(requireContext())
    }
}