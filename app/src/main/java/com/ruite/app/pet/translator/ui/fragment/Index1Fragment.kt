package com.ruite.app.pet.translator.ui.fragment

import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.ruite.app.pet.translator.Const
import com.ruite.app.pet.translator.R
import com.ruite.app.pet.translator.base.RootFragment
import com.ruite.app.pet.translator.databinding.FragmentIndex1Binding
import com.ruite.app.pet.translator.databinding.ItemDogBinding
import com.ruite.app.pet.translator.entity.Index1Entity
import com.ruite.app.pet.translator.ext.getBinding
import com.ruite.app.pet.translator.ext.thrillClickListener
import com.ruite.app.pet.translator.ui.activity.SoundActivity


class Index1Fragment : RootFragment(R.layout.fragment_index_1) {

    var _binding: FragmentIndex1Binding? = null

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
                        SoundActivity.show(requireContext(), true, modelPosition)
                    }
                }
            }
        }

        binding.rvList.bindingAdapter.models = Const.dogSoundList(requireContext())
    }
}