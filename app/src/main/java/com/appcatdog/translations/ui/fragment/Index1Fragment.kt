package com.appcatdog.translations.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.appcatdog.translations.R
import com.appcatdog.translations.base.dj.RootFragment
import com.appcatdog.translations.databinding.FragmentIndex1Binding
import com.appcatdog.translations.databinding.ItemDogBinding
import com.appcatdog.translations.entity.Index1Entity
import com.appcatdog.translations.event.SimpleEvent
import com.appcatdog.translations.ext.getBinding
import com.appcatdog.translations.ext.thrillClickListener
import com.appcatdog.translations.ui.activity.WNCDSoundActivity
import com.appcatdog.translations.utils.lzy.LZYADSUtils
import com.appcatdog.translations.utils.lzy.LZYLog
import com.tbuonomo.viewpagerdotsindicator.setBackgroundCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class Index1Fragment : RootFragment(R.layout.fragment_index_1) {

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
            binding.tvCat.setBackgroundCompat(null)
            binding.tvCat.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_777777))
            dog()
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1,-1)
        }
        binding.tvCat.thrillClickListener {
            binding.tvCat.setBackgroundResource(R.drawable.border_txt_tab_training)
            binding.tvCat.setTextColor(Color.WHITE)
            binding.tvDog.setBackgroundCompat(null)
            binding.tvDog.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_777777))
            cat()
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1,-1)
        }
        binding.rvList.grid(3).setup {

            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {
                        WNCDSoundActivity.show(requireContext(), type==1, modelPosition)
                    }
                }
            }
        }
        dog()
    }

    private fun dog() {
        type = 1
        binding.rvList.bindingAdapter.models = com.appcatdog.translations.AppConst.dogSoundList(requireContext())
    }

    private fun cat() {
        type = 2
        binding.rvList.bindingAdapter.models = com.appcatdog.translations.AppConst.catSoundList(requireContext())
    }
}