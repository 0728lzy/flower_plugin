package com.qingchu.wangmiao.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.qingchu.wangmiao.AppConst
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.RootFragment
import com.qingchu.wangmiao.csj.AdFeedSimpleFourUtils
import com.qingchu.wangmiao.databinding.FragmentPetVideoBinding
import com.qingchu.wangmiao.databinding.ItemPetVideoBinding
import com.qingchu.wangmiao.event.SimpleEvent
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ext.thrillClickListener
import com.qingchu.wangmiao.ui.activity.QCVideoActivity
import com.qingchu.wangmiao.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class PetVideoFragment : RootFragment(R.layout.fragment_pet_video) {

    var _binding: FragmentPetVideoBinding? = null

    val binding get() = _binding!!

    private var selectedPosition = -1


    // 创建名称到索引的映射关系


    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()

        val list by lazy {  listOf(
            requireActivity().getString(R.string.pet_video_1) to R.drawable.ic_pets_purple, // 毛毛 -> index 5
            requireActivity().getString(R.string.pet_video_2) to R.drawable.ic_pets_purple, // 发财 -> index 1
            requireActivity().getString(R.string.pet_video_3) to R.drawable.ic_pets_purple, // 二哈 -> index 2
            requireActivity().getString(R.string.pet_video_4) to R.drawable.ic_pets_purple, // 小白 -> index 4
            requireActivity().getString(R.string.pet_video_5) to R.drawable.ic_pets_purple, // 嘟嘟 -> index 3 (映射到波波)
        ) }
        val nameToIndexMap = mapOf(
            requireActivity().getString(R.string.pet_video_1) to 5, // 毛毛 -> call_4
            requireActivity().getString(R.string.pet_video_2) to 1, // 发财 -> call_3
            requireActivity().getString(R.string.pet_video_3) to 2, // 二哈 -> call_1
            requireActivity().getString(R.string.pet_video_4) to 4, // 小白 -> call_2
            requireActivity().getString(R.string.pet_video_5) to 3  // 嘟嘟 -> call_5 (波波)
        )
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
                        
                        // 使用映射关系获取正确的索引
                        val index = nameToIndexMap[item.first] ?: 1
                        QCVideoActivity.show(requireContext(), index)
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
        if(message.simple ==3){
            LZYLog.e("simple","message simple:${message.simple}")
            loadSimpleAd(binding.feedContainerPetVideo)
        }
    }
    fun loadSimpleAd(fragment: FrameLayout?) {
        if (requireActivity() != null && AppConst.is_show_ad) {
            AdFeedSimpleFourUtils.init(
                requireActivity(),
                object : AdFeedSimpleFourUtils.GirdMenuStateListener {
                    override fun onSuccess() {
                        if (fragment != null && requireActivity() != null) {
                            Log.i("tttt", "准备刷新DogLanguageFragment的广告")
                            AdFeedSimpleFourUtils.showAd(fragment, requireActivity())
                        }
                    }

                    override fun onError() {
                    }
                })
            AdFeedSimpleFourUtils.initPreloading("")
        }
    }
}