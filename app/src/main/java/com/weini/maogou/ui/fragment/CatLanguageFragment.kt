package com.weini.maogou.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.weini.maogou.AppConst
import com.weini.maogou.R
import com.weini.maogou.base.dj.RootFragment
import com.weini.maogou.csj.AdFeedSimpleOneNoLimitUtils
import com.weini.maogou.databinding.FragmentCatLanguageBinding
import com.weini.maogou.databinding.ItemDogBinding
import com.weini.maogou.entity.Index1Entity
import com.weini.maogou.event.SimpleEvent
import com.weini.maogou.ext.getBinding
import com.weini.maogou.ext.thrillClickListener
import com.weini.maogou.ui.activity.WHSoundActivity
import com.weini.maogou.utils.dj.UserInfoModel
import com.weini.maogou.utils.lzy.LZYADSUtils
import com.weini.maogou.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CatLanguageFragment : RootFragment(R.layout.fragment_cat_language) {

    var _binding: FragmentCatLanguageBinding? = null
    private lateinit var lzyadsUtils: LZYADSUtils
    private var isRecording = false
    private var recordingType = 0 // 0: 未录音, 1: 人话录音, 2: 喵语录音
    
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
        if (message.simple == 5) { // 使用新的事件ID避免冲突
            LZYLog.e("simple", "CatLanguageFragment message simple:${message.simple}")
            loadSimpleAd(binding.feedContainerCatLanguage)
        }
    }

    fun loadSimpleAd(fragment: FrameLayout?) {
        if (requireActivity() != null && (!UserInfoModel.getIsCheckFlag() || AppConst.is_show_ad)) {
            AdFeedSimpleOneNoLimitUtils.init(
                requireActivity(),
                object : AdFeedSimpleOneNoLimitUtils.GirdMenuStateListener {
                    override fun onSuccess() {
                        if (fragment != null && requireActivity() != null) {
                            Log.i("tttt", "准备刷新CatLanguageFragment的广告")
                            AdFeedSimpleOneNoLimitUtils.showAd(fragment, requireActivity())
                        }
                    }

                    override fun onError() {
                    }
                })
            AdFeedSimpleOneNoLimitUtils.initPreloading()
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("CatLanguageFragment", requireActivity())

        // 初始化录音按钮点击事件
        initRecordButtons()
        
        // 初始化常见喵语列表
        initCommonSoundsList()
        
        // 加载广告
        lzyadsUtils.loadSimpleAdTurn(binding.feedContainerCatLanguage, -1)
    }

    private fun initRecordButtons() {
        // 人话录音按钮
        binding.btnHumanRecord.thrillClickListener {
            if (!isRecording) {
                startRecording(1)
            } else {
                stopRecording()
            }
        }

        // 喵语录音按钮
        binding.btnCatRecord.thrillClickListener {
            if (!isRecording) {
                startRecording(2)
            } else {
                stopRecording()
            }
        }
    }

    private fun startRecording(type: Int) {
        isRecording = true
        recordingType = type
        
        // 启动Lottie动画
        binding.lottie.playAnimation()
        
        // 更新UI状态
        when (type) {
            1 -> {
                // 人话录音
                binding.ivHumanMic.setBackgroundResource(R.drawable.bg_record_button_active)
                binding.tvRecordHint.text = "正在录制人话..."
            }
            2 -> {
                // 喵语录音
                binding.ivCatMic.setBackgroundResource(R.drawable.bg_record_button_active)
                binding.tvRecordHint.text = "正在录制喵语..."
            }
        }
        
        // 这里应该添加实际的录音逻辑
        // 模拟录音过程，3秒后自动停止
        binding.root.postDelayed({
            if (isRecording) {
                stopRecording()
                showTranslationResult(type)
            }
        }, 3000)
    }

    private fun stopRecording() {
        isRecording = false
        recordingType = 0
        
        // 停止Lottie动画
        binding.lottie.cancelAnimation()
        
        // 恢复UI状态
        binding.ivHumanMic.setBackgroundResource(R.drawable.bg_record_button_red)
        binding.ivCatMic.setBackgroundResource(R.drawable.bg_record_button_red)
        binding.tvRecordHint.text = "点击麦克风开始录音翻译..."
    }

    private fun showTranslationResult(type: Int) {
        // 模拟翻译结果显示
        when (type) {
            1 -> {
                binding.tvRecordHint.text = "翻译完成：喵~ 喵喵~"
            }
            2 -> {
                binding.tvRecordHint.text = "翻译完成：我饿了，给我食物"
            }
        }
        
        // 3秒后恢复初始状态
        binding.root.postDelayed({
            binding.tvRecordHint.text = "点击麦克风开始录音翻译..."
        }, 3000)
    }

    private fun initCommonSoundsList() {
        binding.rvCommonSounds.grid(2).setup {
            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {
                        // 播放猫咪声音，type=2表示猫
                        WHSoundActivity.show(requireContext(), false, modelPosition)
                    }
                }
            }
        }
        
        // 加载猫咪声音数据
        loadCatSounds()
    }

    private fun loadCatSounds() {
        // 获取猫咪声音列表并随机排序，只显示前6个
        val catSounds = AppConst.catSoundList(requireContext()).shuffled().take(6)
        binding.rvCommonSounds.bindingAdapter.models = catSounds
    }

    // 创建录音按钮激活状态的drawable（如果不存在）
    private fun createActiveButtonDrawable() {
        // 这个方法可以用来动态创建激活状态的drawable
        // 或者可以在drawable文件夹中创建对应的资源文件
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}