package com.ruite.app.pet.translator.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.ruite.app.pet.translator.R
import com.ruite.app.pet.translator.base.RootFragment
import com.ruite.app.pet.translator.databinding.FragmentIndex4Binding
import com.ruite.app.pet.translator.databinding.Item4Binding
import com.ruite.app.pet.translator.entity.Index4Entity
import com.ruite.app.pet.translator.ext.getBinding
import com.ruite.app.pet.translator.ext.thrillClickListener
import com.ruite.app.pet.translator.ui.activity.DetailActivity
import com.tbuonomo.viewpagerdotsindicator.setBackgroundCompat


class Index4Fragment : RootFragment(R.layout.fragment_index_4) {

    var _binding: FragmentIndex4Binding? = null

    val binding get() = _binding!!

    var type = 1

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()

        binding.tvDog.thrillClickListener {
            binding.tvDog.setBackgroundResource(R.drawable.border_txt_tab_training)
            binding.tvDog.setTextColor(Color.WHITE)
            binding.tvCat.setBackgroundCompat(null)
            binding.tvCat.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_777777))
            dog()
        }
        binding.tvCat.thrillClickListener {
            binding.tvCat.setBackgroundResource(R.drawable.border_txt_tab_training)
            binding.tvCat.setTextColor(Color.WHITE)
            binding.tvDog.setBackgroundCompat(null)
            binding.tvDog.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_777777))
            cat()
        }
        binding.rvList.linear().setup {
            addType<Index4Entity>(R.layout.item_4)
            onBind {
                getBinding<Item4Binding>().apply {
                    val item = getModel<Index4Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    tvDescription.text = item.content
                    root.thrillClickListener {
                        var index = modelPosition + 1
                        if (type == 2) {
                            index += 5
                        }
                        DetailActivity.show(requireContext(), index)
                    }
                }
            }
        }
        dog()
    }

    private fun dog() {
        type = 1
        binding.rvList.bindingAdapter.models = listOf(
            Index4Entity(R.mipmap.icon_food, getString(R.string.index_4_1), getString(R.string.content_food)),
            Index4Entity(R.mipmap.icon_praise, getString(R.string.index_4_2), getString(R.string.content_praise)),
            Index4Entity(R.mipmap.icon_bitting, getString(R.string.index_4_3), getString(R.string.content_biting)),
            Index4Entity(R.mipmap.icon_obedience, getString(R.string.index_4_4), getString(R.string.content_obedience)),
            Index4Entity(R.mipmap.icon_barking, getString(R.string.index_4_5), getString(R.string.content_barking)),
        )
    }

    private fun cat() {
        type = 2
        binding.rvList.bindingAdapter.models = listOf(
            Index4Entity(R.mipmap.icon_feeding_cats, getString(R.string.index_4_6), getString(R.string.content_feeding)),
            Index4Entity(
                R.mipmap.ic_playing_with_cats,
                getString(R.string.index_4_7),
                getString(R.string.content_playing_with_your_cat)
            ),
            Index4Entity(R.mipmap.icon_petting_cats, getString(R.string.index_4_8), getString(R.string.content_petting_your_cat)),
            Index4Entity(
                R.mipmap.ic_scratching_needs,
                getString(R.string.index_4_9),
                getString(R.string.content_scratching_needs)
            ),
        )
    }
}