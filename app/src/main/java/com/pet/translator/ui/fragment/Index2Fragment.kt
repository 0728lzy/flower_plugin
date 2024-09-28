package com.pet.translator.ui.fragment

import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.pet.translator.R
import com.pet.translator.base.dj.RootFragment
import com.pet.translator.databinding.FragmentIndex2Binding
import com.pet.translator.databinding.ItemDogBinding
import com.pet.translator.entity.Index1Entity
import com.pet.translator.event.SimpleEvent
import com.pet.translator.ext.getBinding
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.ui.activity.SoundActivity
import com.pet.translator.utils.lzy.LZYADSUtils
import com.pet.translator.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class Index2Fragment : RootFragment(R.layout.fragment_index_2) {

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
        if(message.simple == 1){
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


        binding.rvList.bindingAdapter.models = com.pet.translator.AppConst.catSoundList(requireContext())
    }
}