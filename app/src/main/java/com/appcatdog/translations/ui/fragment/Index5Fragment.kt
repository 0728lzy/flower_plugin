package com.appcatdog.translations.ui.fragment

import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.appcatdog.translations.R
import com.appcatdog.translations.base.dj.RootFragment
import com.appcatdog.translations.databinding.FragmentIndex2Binding
import com.appcatdog.translations.databinding.ItemDogBinding
import com.appcatdog.translations.entity.Index1Entity
import com.appcatdog.translations.event.SimpleEvent
import com.appcatdog.translations.ext.getBinding
import com.appcatdog.translations.ext.thrillClickListener
import com.appcatdog.translations.ui.activity.SoundActivity
import com.appcatdog.translations.utils.lzy.LZYADSUtils
import com.appcatdog.translations.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class Index5Fragment : RootFragment(R.layout.fragment_index_2) {

    var _binding: FragmentIndex2Binding? = null
    private lateinit var lzyadsUtils: LZYADSUtils


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
        if(message.simple == 4){
            LZYLog.e("simple","message simple:${message.simple}")
            lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment2,-1)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("Index2Fragment", requireActivity())
        binding.rvList.grid(3).setup {

            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {
                        SoundActivity.show(requireContext(), false, modelPosition)
                    }
                }
            }
        }


        binding.rvList.bindingAdapter.models = com.appcatdog.translations.AppConst.catSoundList(requireContext())
    }
}