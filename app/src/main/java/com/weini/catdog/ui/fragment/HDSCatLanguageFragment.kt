package com.weini.catdog.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.hjq.permissions.Permission
import com.weini.catdog.AppConst
import com.weini.catdog.R
import com.weini.catdog.base.dj.RootFragment
import com.weini.catdog.csj.ZYMAllAdsUtils
import com.weini.catdog.databinding.FragmentCatLanguageBinding
import com.weini.catdog.databinding.ItemDogBinding
import com.weini.catdog.entity.Index1Entity
import com.weini.catdog.entity.Index3Entity
import com.weini.catdog.event.SimpleEvent
import com.weini.catdog.ext.countDown
import com.weini.catdog.ext.getBinding
import com.weini.catdog.ext.thrillClickListener
import com.weini.catdog.ui.activity.WCSoundActivity
import com.weini.catdog.ui.dialog.ResultDialog

import com.weini.catdog.utils.lzy.LZYLog
import com.weini.catdog.utils.lzy.PermissionUtils

import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Formatter

class HDSCatLanguageFragment : RootFragment(R.layout.fragment_cat_language) {

    var _binding: FragmentCatLanguageBinding? = null

    private var isRecording = false
    private var recordingType = 0 // 0: 未录音, 1: 人话录音, 2: 喵语录音
    private var record: com.weini.catdog.utils.AudioRecordUtil? = null

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
            LZYLog.e("simple", "CatLanguageFragment message simple:${message.simple}")

            ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerCatLanguage)
        }
    }



    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()


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
            ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerCatLanguage)
        },500)
    }

    private fun initRecordButtons() {
        // 喵语录音按钮（喵语 -> 人话）
        binding.ivCatMic.thrillClickListener {
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
            if (isRecording) {
                // 停止录音并展示结果
                val entity =  catList.random()
                val result=record?.stopRecord()
                job?.cancel()


                ZYMAllAdsUtils.showAdJLTurn(requireActivity(),"JL"){
                    Handler().postDelayed({
                        runOnUiThread {
                            if (result==null||!result)
                                Toast.makeText(requireContext(),"请发出足够大的声音以保证能被识别翻译~", Toast.LENGTH_SHORT).show()
                            else
                                ResultDialog(entity).show(requireRootActivity())
                            binding.tvRecordHint.text = "点击按钮开始录音"
                        }
                    }, 600)
                }

                // 恢复按钮样式
                binding.ivCatMic.setBackgroundResource(R.drawable.bg_record_button_red)
                isRecording = false
                recordingType = 0
            } else {
                // 开始录音
                isRecording = true
                recordingType = type
                if (record == null) {
                    record = com.weini.catdog.utils.AudioRecordUtil()
                    record?.setOnCompleteListener {
                        record = null
                    }
                }
                record?.startRecord()

                // 按钮激活状态与提示文案
                when (type) {
                    1 -> {
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
        binding.rvCommonSounds.linear(LinearLayout.VERTICAL).setup {
            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {
                        // 获取原始数据列表中的正确索引
                        val originalList = AppConst.catSoundList(requireContext())
                        val originalIndex = originalList.indexOfFirst { it.title == item.title && it.icon == item.icon }
                        // 播放猫咪声音，type=2表示猫，传递原始索引
                        WCSoundActivity.show(requireContext(), false, originalIndex)
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
            Index3Entity(
                "我好担心啊",
                "cat_images/cat_01.webp",
                "cat_sounds/cat_01.wav"
            ),
            Index3Entity(
                "你确定吗",
                "cat_images/cat_02.webp",
                "cat_sounds/cat_02.wav"
            ),
            Index3Entity(
                "这太让猫窒息了",
                "cat_images/cat_03.webp",
                "cat_sounds/cat_03.wav"
            ),
            Index3Entity(
                "好饿啊",
                "cat_images/cat_04.webp",
                "cat_sounds/cat_04.wav"
            ),
            Index3Entity(
                "饿了",
                "cat_images/cat_05.webp",
                "cat_sounds/cat_05.wav"
            ),
            Index3Entity(
                "有一点点恐怖",
                "cat_images/cat_06.webp",
                "cat_sounds/cat_06.wav"
            ),
            Index3Entity(
                "我想吃点东西了",
                "cat_images/cat_07.webp",
                "cat_sounds/cat_07.wav"
            ),
            Index3Entity(
                "这一天天的",
                "cat_images/cat_08.webp",
                "cat_sounds/cat_08.wav"
            ),
            Index3Entity(
                "可怜可怜我",
                "cat_images/cat_09.webp",
                "cat_sounds/cat_09.wav"
            ),
            Index3Entity(
                "我好伤心",
                "cat_images/cat_10.webp",
                "cat_sounds/cat_10.wav"
            ),
            Index3Entity(
                "什么",
                "cat_images/cat_11.webp",
                "cat_sounds/cat_11.wav"
            ),
            Index3Entity(
                "啊？",
                "cat_images/cat_11.webp",
                "cat_sounds/cat_11.wav"
            ),
            Index3Entity(
                "悄咪咪的",
                "cat_images/cat_12.webp",
                "cat_sounds/cat_12.wav"
            ),
            Index3Entity(
                "哈哈哈哈",
                "cat_images/cat_13.webp",
                "cat_sounds/cat_13.wav"
            ),
            Index3Entity(
                "等一下",
                "cat_images/cat_14.webp",
                "cat_sounds/cat_14.wav"
            ),
            Index3Entity(
                "生气了",
                "cat_images/cat_15.webp",
                "cat_sounds/cat_15.wav"
            ),
            Index3Entity(
                "喵~",
                "cat_images/cat_16.webp",
                "cat_sounds/cat_16.wav"
            ),
            Index3Entity(
                "真的吗",
                "cat_images/cat_17.webp",
                "cat_sounds/cat_17.wav"
            ),
            Index3Entity(
                "嗯？疑惑",
                "cat_images/cat_18.webp",
                "cat_sounds/cat_18.wav"
            ),
            Index3Entity(
                "好吧",
                "cat_images/cat_19.webp",
                "cat_sounds/cat_19.wav"
            ),
        )
    }
}