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
import com.pet.translator.ext.getBinding
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.ui.activity.SoundActivity


class Index2Fragment : RootFragment(R.layout.fragment_index_2) {

    var _binding: FragmentIndex2Binding? = null

    val binding get() = _binding!!

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
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