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
import com.qingchu.wangmiao.csj.AdFeedSimpleOneNoLimitUtils
import com.qingchu.wangmiao.databinding.FragmentCatLanguageBinding
import com.qingchu.wangmiao.databinding.ItemDogBinding
import com.qingchu.wangmiao.entity.Index1Entity
import com.qingchu.wangmiao.entity.Index3Entity
import com.qingchu.wangmiao.event.SimpleEvent
import com.qingchu.wangmiao.ext.countDown
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ext.thrillClickListener
import com.qingchu.wangmiao.ui.activity.QCSoundActivity
import com.qingchu.wangmiao.ui.dialog.ResultDialog
import com.qingchu.wangmiao.utils.dj.UserInfoModel
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils
import com.qingchu.wangmiao.utils.lzy.LZYLog
import com.qingchu.wangmiao.utils.lzy.PermissionUtils
import com.qingchu.wangmiao.widget.dialog.LoadingDiaLog
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Formatter

class CatLanguageFragment : RootFragment(R.layout.fragment_cat_language) {

    var _binding: FragmentCatLanguageBinding? = null
    private lateinit var lzyadsUtils: LZYADSUtils
    private var isRecording = false
    private var recordingType = 0 // 0: 未录音, 1: 人话录音, 2: 喵语录音
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
        if (message.simple == 0) { // 使用新的事件ID避免冲突
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
        if (AppConst.is_show_ad){
            binding.tvTop.visibility=View.GONE
        }else{
            binding.tvTop.visibility=View.VISIBLE
        }
        Handler().postDelayed({
            loadSimpleAd(binding.feedContainerCatLanguage)
        },500)
    }

    private fun initRecordButtons() {
        // 人话录音按钮（人话 -> 喵语）
        binding.btnHumanRecord.thrillClickListener {
            handleRecordClick(1)
        }

        // 喵语录音按钮（喵语 -> 人话）
        binding.btnCatRecord.thrillClickListener {
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
                val entity = catList.random()
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
                binding.ivCatMic.setBackgroundResource(R.drawable.bg_record_button_red)
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
                        binding.ivCatMic.setBackgroundResource(R.drawable.bg_record_button_active)
                        binding.tvRecordHint.text = "正在录制喵语..."
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
                        // 播放猫咪声音，type=2表示猫
                        QCSoundActivity.show(requireContext(), false, modelPosition)
                    }
                }
            }
        }
        
        // 加载猫咪声音数据
        loadCatSounds()
    }

    private fun loadCatSounds() {
        // 获取猫咪声音列表并随机排序，只显示前6个
        val catSounds = AppConst.catSoundList(requireContext()).shuffled()
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

    // 与 WHIndex3Fragment 一致的猫咪资源列表，用于结果弹窗展示
    private val catList by lazy {
        listOf(
            Index3Entity("", "cat_images/cat_01.webp", "cat_sounds/cat_01.wav"),
            Index3Entity("", "cat_images/cat_02.webp", "cat_sounds/cat_02.wav"),
            Index3Entity("", "cat_images/cat_03.webp", "cat_sounds/cat_03.wav"),
            Index3Entity("", "cat_images/cat_04.webp", "cat_sounds/cat_04.wav"),
            Index3Entity("", "cat_images/cat_05.webp", "cat_sounds/cat_05.wav"),
            Index3Entity("", "cat_images/cat_06.webp", "cat_sounds/cat_06.wav"),
            Index3Entity("", "cat_images/cat_07.webp", "cat_sounds/cat_07.wav"),
            Index3Entity("", "cat_images/cat_08.webp", "cat_sounds/cat_08.wav"),
            Index3Entity("", "cat_images/cat_09.webp", "cat_sounds/cat_09.wav"),
            Index3Entity("", "cat_images/cat_10.webp", "cat_sounds/cat_10.wav"),
            Index3Entity("", "cat_images/cat_11.webp", "cat_sounds/cat_11.wav"),
            Index3Entity("", "cat_images/cat_12.webp", "cat_sounds/cat_12.wav"),
            Index3Entity("", "cat_images/cat_13.webp", "cat_sounds/cat_13.wav"),
            Index3Entity("", "cat_images/cat_14.webp", "cat_sounds/cat_14.wav"),
            Index3Entity("", "cat_images/cat_15.webp", "cat_sounds/cat_15.wav"),
            Index3Entity("", "cat_images/cat_16.webp", "cat_sounds/cat_16.wav"),
            Index3Entity("", "cat_images/cat_17.webp", "cat_sounds/cat_17.wav"),
            Index3Entity("", "cat_images/cat_18.webp", "cat_sounds/cat_18.wav"),
            Index3Entity("", "cat_images/cat_19.webp", "cat_sounds/cat_19.wav")
        )
    }
}