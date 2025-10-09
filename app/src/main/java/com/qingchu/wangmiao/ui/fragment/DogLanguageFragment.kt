package com.qingchu.wangmiao.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.hjq.permissions.Permission
import com.qingchu.wangmiao.AppConst
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.RootFragment
import com.qingchu.wangmiao.csj.AdFeedSimpleTwoUtils
import com.qingchu.wangmiao.databinding.FragmentDogLanguageBinding
import com.qingchu.wangmiao.databinding.ItemDogBinding
import com.qingchu.wangmiao.entity.Index1Entity
import com.qingchu.wangmiao.entity.Index3Entity
import com.qingchu.wangmiao.event.SimpleEvent
import com.qingchu.wangmiao.ext.countDown
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ext.thrillClickListener
import com.qingchu.wangmiao.ui.activity.QCSoundActivity
import com.qingchu.wangmiao.ui.dialog.ResultDialog
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils
import com.qingchu.wangmiao.utils.lzy.LZYLog
import com.qingchu.wangmiao.utils.lzy.PermissionUtils
import com.qingchu.wangmiao.widget.dialog.LoadingDiaLog
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Formatter

class DogLanguageFragment : RootFragment(R.layout.fragment_dog_language) {

    var _binding: FragmentDogLanguageBinding? = null
    private lateinit var lzyadsUtils: LZYADSUtils
    private var isRecording = false
    private var recordingType = 0 // 0: 未录音, 1: 人话录音, 2: 狗语录音
    private var record: com.qingchu.wangmiao.utils.AudioRecordUtil? = null
    private lateinit var myDiaLog: LoadingDiaLog
    private var job: Job? = null
    
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
        if (message.simple == 1) { // 使用新的事件ID避免冲突
            LZYLog.e("simple", "DogLanguageFragment message simple:${message.simple}")
            loadSimpleAd(binding.feedContainerDogLanguage)
        }
    }

    fun loadSimpleAd(fragment: FrameLayout?) {
        if (requireActivity() != null && AppConst.is_show_ad) {
            AdFeedSimpleTwoUtils.init(
                requireActivity(),
                object : AdFeedSimpleTwoUtils.GirdMenuStateListener {
                    override fun onSuccess() {
                        if (fragment != null && requireActivity() != null) {
                            Log.i("tttt", "准备刷新DogLanguageFragment的广告")
                            AdFeedSimpleTwoUtils.showAd(fragment, requireActivity())
                        }
                    }

                    override fun onError() {
                    }
                })
            AdFeedSimpleTwoUtils.initPreloading("")
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils = LZYADSUtils("DogLanguageFragment", requireActivity())

        // 初始化录音按钮点击事件
        initRecordButtons()
        
        // 初始化常见狗语列表
        initCommonSoundsList()
        if (AppConst.is_show_ad){
            binding.tvTop.visibility=View.GONE
        }else{
            binding.tvTop.visibility=View.VISIBLE
        }
        // 加载广告
        lzyadsUtils.loadSimpleAdTurn(binding.feedContainerDogLanguage, -1)
    }

    private fun initRecordButtons() {
        // 人话录音按钮（人话 -> 狗语）
        binding.btnHumanRecord.thrillClickListener {
            handleRecordClick(1)
        }

        // 狗语录音按钮（狗语 -> 人话）
        binding.btnDogRecord.thrillClickListener {
            handleRecordClick(2)
        }
    }

    /**
     * 仿照 WHIndex3Fragment 的录音/翻译流程：
     * - 权限校验
     * - 开始录音：播放 Lottie，启动 AudioRecordUtil，倒计时显示
     * - 停止录音：停止 Lottie 和录音，展示 LoadingDiaLog 与广告，随后弹出结果对话框
     * 页面 UI 不变，仅替换逻辑
     */
    private fun handleRecordClick(type: Int) {
        PermissionUtils.tryToDoSomethingWithCheckPermissionAndCode(
            requireContext(),
            arrayOf(Permission.RECORD_AUDIO),
            1,
            "权限被拒绝，无法使用该功能"
        ) {
            if (binding.lottie.isAnimating) {
                // 停止录音并展示结果
                val entity = dogList.random()
                binding.lottie.cancelAnimation()
                record?.stopRecord()
                job?.cancel()

                myDiaLog = LoadingDiaLog(requireContext())
                myDiaLog.show()
                lzyadsUtils.showAdJL(myDiaLog) {
                    Handler().postDelayed({
                        runOnUiThread {
                            ResultDialog(entity).show(requireRootActivity())
                            binding.tvRecordHint.text = "点击麦克风开始录音翻译..."
                        }
                    }, 600)
                }

                // 恢复按钮样式
                binding.ivHumanMic.setBackgroundResource(R.drawable.bg_record_button_red)
                binding.ivDogMic.setBackgroundResource(R.drawable.bg_record_button_red)
                isRecording = false
                recordingType = 0
            } else {
                // 开始录音
                isRecording = true
                recordingType = type

                binding.lottie.playAnimation()
                if (record == null) {
                    record = com.qingchu.wangmiao.utils.AudioRecordUtil()
                    record?.setOnCompleteListener {
                        record = null
                    }
                }
                record?.startRecord()

                // 按钮激活状态与提示文案
                when (type) {
                    1 -> {
                        binding.ivHumanMic.setBackgroundResource(R.drawable.bg_record_button_active)
                        binding.tvRecordHint.text = "正在录制人话..."
                    }
                    2 -> {
                        binding.ivDogMic.setBackgroundResource(R.drawable.bg_record_button_active)
                        binding.tvRecordHint.text = "正在录制狗语..."
                    }
                }

                // 启动计时（显示到提示文案）
                job = requireRootActivity().countDown(
                    time = 100000,
                    start = {
                        // no-op
                    },
                    next = {
                        val time = 100000 - it.toInt()
                        val seconds = time % 60
                        val minutes = (time / 60) % 60
                        binding.tvRecordHint.text = Formatter().format("%02d:%02d", minutes, seconds).toString()
                    },
                    end = {
                        // no-op
                    }
                )
            }
        }
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
                        // 获取原始数据列表中的正确索引
                        val originalList = AppConst.dogSoundList(requireContext())
                        val originalIndex = originalList.indexOfFirst { it.title == item.title && it.icon == item.icon }
                        // 播放狗狗声音，type=1表示狗，传递原始索引
                        QCSoundActivity.show(requireContext(), true, originalIndex)
                    }
                }
            }
        }
        
        // 加载狗狗声音数据
        loadDogSounds()
    }

    private fun loadDogSounds() {
        // 获取狗狗声音列表并随机排序，只显示前6个
        val dogSounds = AppConst.dogSoundList(requireContext()).shuffled()
        binding.rvCommonSounds.bindingAdapter.models = dogSounds
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

    // 与 WHIndex3Fragment 一致的狗狗资源列表，用于结果弹窗展示
    private val dogList by lazy {
        listOf(
            Index3Entity("", "dog_images/dog_01.webp", "dog_sounds/dog_01.wav"),
            Index3Entity("", "dog_images/dog_02.webp", "dog_sounds/dog_02.wav"),
            Index3Entity("", "dog_images/dog_03.webp", "dog_sounds/dog_03.wav"),
            Index3Entity("", "dog_images/dog_04.webp", "dog_sounds/dog_04.wav"),
            Index3Entity("", "dog_images/dog_05.webp", "dog_sounds/dog_05.wav"),
            Index3Entity("", "dog_images/dog_06.webp", "dog_sounds/dog_06.wav"),
            Index3Entity("", "dog_images/dog_07.webp", "dog_sounds/dog_07.wav"),
            Index3Entity("", "dog_images/dog_08.webp", "dog_sounds/dog_08.wav"),
            Index3Entity("", "dog_images/dog_09.webp", "dog_sounds/dog_09.wav"),
            Index3Entity("", "dog_images/dog_10.webp", "dog_sounds/dog_10.wav"),
            Index3Entity("", "dog_images/dog_11.webp", "dog_sounds/dog_11.wav"),
            Index3Entity("", "dog_images/dog_12.webp", "dog_sounds/dog_12.wav"),
            Index3Entity("", "dog_images/dog_13.webp", "dog_sounds/dog_13.wav"),
            Index3Entity("", "dog_images/dog_14.webp", "dog_sounds/dog_14.wav"),
            Index3Entity("", "dog_images/dog_15.webp", "dog_sounds/dog_15.wav"),
            Index3Entity("", "dog_images/dog_16.webp", "dog_sounds/dog_16.wav"),
            Index3Entity("", "dog_images/dog_17.webp", "dog_sounds/dog_17.wav"),
            Index3Entity("", "dog_images/dog_18.webp", "dog_sounds/dog_18.wav"),
            Index3Entity("", "dog_images/dog_19.webp", "dog_sounds/dog_19.wav")
        )
    }
}