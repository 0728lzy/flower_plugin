package com.cslt.maogoufanyi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.cslt.maogoufanyi.AppConst
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.RootFragment
import com.cslt.maogoufanyi.csj.AdFeedSimpleFourUtils
import com.cslt.maogoufanyi.databinding.FragmentPetVideoBinding
import com.cslt.maogoufanyi.databinding.ItemPetVideoBinding
import com.cslt.maogoufanyi.event.SimpleEvent
import com.cslt.maogoufanyi.ext.getBinding
import com.cslt.maogoufanyi.ext.thrillClickListener
import com.cslt.maogoufanyi.ui.activity.QCVideoActivity
import com.cslt.maogoufanyi.utils.lzy.LZYLog
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
            requireActivity().getString(R.string.pet_video_1) to R.drawable.maoamo2_tab4 to R.drawable.maomao_tab4, // 毛毛 -> index 5
            requireActivity().getString(R.string.pet_video_2) to R.drawable.fa_cai_tab4 to R.drawable.fa_cai2_tab4, // 发财 -> index 1
            requireActivity().getString(R.string.pet_video_3) to R.drawable.er_ha_tab4 to R.drawable.er_ha2_tab4, // 二哈 -> index 2
            requireActivity().getString(R.string.pet_video_4) to R.drawable.xiao_bai_tab4 to R.drawable.xiao_abi2_tab4, // 小白 -> index 4
            requireActivity().getString(R.string.pet_video_5) to R.drawable.bo_bo_tab4 to R.drawable.bo_bo2_tab4, // 波波 -> index 3 (映射到波波)
        ) }
        val nameToIndexMap = mapOf(
            requireActivity().getString(R.string.pet_video_1) to 5, // 毛毛 -> call_4
            requireActivity().getString(R.string.pet_video_2) to 1, // 发财 -> call_3
            requireActivity().getString(R.string.pet_video_3) to 2, // 二哈 -> call_1
            requireActivity().getString(R.string.pet_video_4) to 4, // 小白 -> call_2
            requireActivity().getString(R.string.pet_video_5) to 3  // 嘟嘟 -> call_5 (波波)
        )
        binding.rvList.grid(1).setup {
            addType< Pair<Pair<String,Int>,Int>>(R.layout.item_pet_video)
            onBind {
                getBinding<ItemPetVideoBinding>().apply {
                    val item = getModel< Pair<Pair<String,Int>,Int>>()
                    ivAvatar.setImageResource(item.first.second)
                    tvName.text = item.first.first
                    
                    // 设置选中状态
                    root.isSelected = (modelPosition == selectedPosition)
                    beis.setImageResource(item.second)
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
                        val index = nameToIndexMap[item.first.first] ?: 1
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