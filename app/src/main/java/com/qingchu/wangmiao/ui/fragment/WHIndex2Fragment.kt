package com.qingchu.wangmiao.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.RootFragment
import com.qingchu.wangmiao.databinding.FragmentIndex4Binding
import com.qingchu.wangmiao.databinding.Item4Binding
import com.qingchu.wangmiao.entity.Index4Entity
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ext.thrillClickListener
import com.qingchu.wangmiao.ui.activity.QCDetailActivity
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils
import com.tbuonomo.viewpagerdotsindicator.setBackgroundCompat
import com.qingchu.wangmiao.event.SimpleEvent
import com.qingchu.wangmiao.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WHIndex2Fragment : RootFragment(R.layout.fragment_index_4) {

    var _binding: FragmentIndex4Binding? = null

    val binding get() = _binding!!

    private lateinit var lzyadsUtils: LZYADSUtils

    var type = 1
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
//            lzyadsUtils.loadSimpleAd2(binding.feedContainerFragment4)
        }
    }
    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("Index4Fragment", requireActivity())
        binding.tvDog.thrillClickListener {
            lzyadsUtils.loadSimpleAd2(binding.feedContainerFragment4)
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
            lzyadsUtils.loadSimpleAd2(binding.feedContainerFragment4)
            addType<Index4Entity>(R.layout.item_4)
            onBind {
                getBinding<Item4Binding>().apply {
                    val item = getModel<Index4Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    tvDescription.text = item.content
                    if (modelPosition==1){
                        constraintAdv.visibility=View.VISIBLE
                        lzyadsUtils.loadSimpleAdTurn(feedContainerItemAdv,320)
                    }else{
                        constraintAdv.visibility=View.GONE
                    }
                    root.thrillClickListener {
                        var index = modelPosition + 1
                        if (type == 2) {
                            index += 5
                        }
                        QCDetailActivity.show(requireContext(), index)
                    }
                }
            }
        }
        dog()
    }

    private fun dog() {
        type = 1
        binding.rvList.bindingAdapter.models = listOf(
            Index4Entity(R.mipmap.icon_barking, getString(R.string.index_4_5), getString(R.string.content_barking)),
            Index4Entity(R.mipmap.icon_bitting, getString(R.string.index_4_3), getString(R.string.content_biting)),

            Index4Entity(R.mipmap.icon_praise, getString(R.string.index_4_2), getString(R.string.content_praise)),
            Index4Entity(R.mipmap.icon_food, getString(R.string.index_4_1), getString(R.string.content_food)),
            Index4Entity(R.mipmap.icon_obedience, getString(R.string.index_4_4), getString(R.string.content_obedience)),

        ).shuffled()
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
        ).shuffled()
    }
}