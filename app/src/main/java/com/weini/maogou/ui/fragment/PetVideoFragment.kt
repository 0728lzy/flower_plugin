package com.weini.maogou.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.weini.maogou.R
import com.weini.maogou.base.dj.RootFragment
import com.weini.maogou.databinding.FragmentPetVideoBinding
import com.weini.maogou.databinding.ItemPetVideoBinding
import com.weini.maogou.event.SimpleEvent
import com.weini.maogou.ext.getBinding
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.ui.activity.WHVideoActivity
import com.weini.maogou.utils.lzy.LZYADSUtils
import com.weini.maogou.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class PetVideoFragment : RootFragment(R.layout.fragment_pet_video) {

    var _binding: FragmentPetVideoBinding? = null

    val binding get() = _binding!!

    private var selectedPosition = -1

    val list by lazy {  listOf(
        getString(R.string.pet_video_1) to R.drawable.ic_pets_purple,
        getString(R.string.pet_video_2) to R.drawable.ic_pets_purple,
        getString(R.string.pet_video_3) to R.drawable.ic_pets_purple,
        getString(R.string.pet_video_4) to R.drawable.ic_pets_purple,
        getString(R.string.pet_video_5) to R.drawable.ic_pets_purple,
    ).shuffled() }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()

        binding.rvList.grid(2).setup {
            addType<Pair<String, Int>>(R.layout.item_pet_video)
            onBind {
                getBinding<ItemPetVideoBinding>().apply {
                    val item = getModel<Pair<String, Int>>()
                    ivAvatar.setImageResource(item.second)
                    tvName.text = item.first
                    
                    // 设置选中状态
                    root.isSelected = (modelPosition == selectedPosition)
                    
                    root.thrillClickListener {
                        // 更新选中状态
                        val oldPosition = selectedPosition
                        selectedPosition = if (selectedPosition == modelPosition) -1 else modelPosition
                        
                        // 刷新旧的和新的选中项
                        if (oldPosition != -1) {
                            binding.rvList.bindingAdapter.notifyItemChanged(oldPosition)
                        }
                        if (selectedPosition != -1) {
                            binding.rvList.bindingAdapter.notifyItemChanged(selectedPosition)
                        }
                        
                        val index = list.indexOf(item) + 1
                        WHVideoActivity.show(requireContext(), index)
                    }
                }
            }
        }

        binding.rvList.bindingAdapter.models = list
    }

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
            LZYADSUtils("PetVideoFragment",requireActivity()).loadSimpleAd4(requireActivity(),binding.feedContainerPetVideo)
        }
    }
}