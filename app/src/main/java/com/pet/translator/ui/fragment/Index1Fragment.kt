package com.pet.translator.ui.fragment

import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.pet.translator.R
import com.pet.translator.base.dj.RootFragment
import com.pet.translator.databinding.FragmentIndex1Binding
import com.pet.translator.databinding.ItemDogBinding
import com.pet.translator.entity.Index1Entity
import com.pet.translator.ext.getBinding
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.ui.activity.SoundActivity
import com.pet.translator.utils.lzy.LZYADSUtils


class Index1Fragment : RootFragment(R.layout.fragment_index_1) {

    var _binding: FragmentIndex1Binding? = null
    private lateinit var lzyadsUtils: LZYADSUtils

    val binding get() = _binding!!

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("Index1Fragment", requireActivity())
        lzyadsUtils.loadSimpleAdTurn(binding.feedContainerFragment1,-1)
        binding.rvList.grid(3).setup {

            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {
                        SoundActivity.show(requireContext(), true, modelPosition)
                    }
                }
            }
        }

        binding.rvList.bindingAdapter.models = com.pet.translator.AppConst.dogSoundList(requireContext())
    }
}