package com.cslianta.catdog.ui.fragment

import android.graphics.Color
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
import com.cslianta.catdog.AppConst
import com.cslianta.catdog.R
import com.cslianta.catdog.base.dj.RootFragment
import com.cslianta.catdog.csj.ZYMAllAdsUtils
import com.cslianta.catdog.csj.lzy.LZYCPCounterHelper
import com.cslianta.catdog.csj.lzy.LzyUtils
import com.cslianta.catdog.databinding.FragmentDogLanguageBinding
import com.cslianta.catdog.databinding.ItemDogBinding
import com.cslianta.catdog.entity.Index1Entity
import com.cslianta.catdog.entity.Index3Entity
import com.cslianta.catdog.event.SimpleEvent
import com.cslianta.catdog.ext.countDown
import com.cslianta.catdog.ext.getBinding
import com.cslianta.catdog.ext.thrillClickListener
import com.cslianta.catdog.ui.activity.CASoundActivity
import com.cslianta.catdog.ui.dialog.ResultDialog

import com.cslianta.catdog.utils.lzy.LZYLog
import com.cslianta.catdog.utils.lzy.PermissionUtils
import com.drake.brv.utils.grid

import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Formatter

class HDSDogLanguageFragment : RootFragment(R.layout.fragment_dog_language) {

    var _binding: FragmentDogLanguageBinding? = null

    private var isRecording = false
    private var recordingType = 0 // 0: 未录音, 1: 人话录音, 2: 狗语录音
    private var record: com.cslianta.catdog.utils.AudioRecordUtil? = null

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
            LZYLog.e("simple", "DogLanguageFragment message simple:${message.simple}")


            ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerDogLanguage)
        }
    }

    private var type = 1;  //1 狗  2 猫


    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()


        // 初始化录音按钮点击事件
        initRecordButtons()
        

        if (AppConst.is_show_ad){
            binding.tvTop.visibility=View.GONE
        }else{
            binding.tvTop.visibility=View.VISIBLE
        }
        // 加载广告
        ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerDogLanguage)

        binding.dogLl.setOnClickListener({
            ZYMAllAdsUtils.showAdCpTurnTab(requireActivity(),"CP")

            initDog()
        })

        binding.catLl.setOnClickListener({
            ZYMAllAdsUtils.showAdCpTurnTab(requireActivity(),"CP")
            initCat()
        })

        initDog()
    }

    private fun initDog(){
        binding.dogLl.setBackgroundResource(R.drawable.app_tab_selected_bg)
        binding.dogTv.setTextColor(Color.parseColor("#ffffff"))

        binding.catLl.setBackgroundResource(R.drawable.app_tab_unselected_bg)
        binding.catTv.setTextColor(Color.parseColor("#000000"))
        type = 1
        // 初始化常见狗语列表
        initCommonSoundsList()

        // 加载狗狗声音数据
        loadDogSounds()
    }

    private fun initCat(){
        binding.catLl.setBackgroundResource(R.drawable.app_tab_selected_bg)
        binding.catTv.setTextColor(Color.parseColor("#ffffff"))
        type = 2
        binding.dogLl.setBackgroundResource(R.drawable.app_tab_unselected_bg)
        binding.dogTv.setTextColor(Color.parseColor("#000000"))
        // 初始化常见狗语列表
        initCommonSoundsList()

        // 加载猫咪声音数据
        loadCatSounds()

    }
    private fun loadCatSounds() {
        // 获取猫咪声音列表并随机排序，只显示前6个
        val catSounds = AppConst.catSoundList(requireContext()).shuffled()
        binding.rvCommonSounds.bindingAdapter.models = catSounds
    }

    private fun initRecordButtons() {
        // 人话录音按钮（人话 -> 狗语）
        binding.ivDogMic.thrillClickListener {
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
                val entity = if (type==1) dogList.random() else person2Dog.random()
                val result=record?.stopRecord()
                job?.cancel()

                ZYMAllAdsUtils.showAdJLTurn(requireActivity(),"JL"){

                        Handler().postDelayed({
                            runOnUiThread {
                                if (result==null||!result) {
                                    Toast.makeText(
                                        requireContext(),
                                        "请发出足够大的声音以保证能被识别翻译~",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.desIv.visibility = View.VISIBLE
                                }else {
                                    ResultDialog(entity).show(requireRootActivity())
                                    binding.desIv.visibility = View.VISIBLE
                                }
                                binding.tvRecordHint.text = "点击按钮开始录音"
                            }
                        }, 600)

                }

                // 恢复按钮样式
                binding.ivDogMic.setBackgroundResource(R.drawable.bg_record_button_red)
                isRecording = false
                recordingType = 0
            } else {
                // 开始录音
                isRecording = true
                recordingType = type

                if (record == null) {
                    record = com.cslianta.catdog.utils.AudioRecordUtil()
                    record?.setOnCompleteListener {
                        record = null
                    }
                }
                record?.startRecord()

                // 按钮激活状态与提示文案
                when (type) {
                    1 -> {
                        binding.tvRecordHint.text = "正在录制人话..."
                        binding.desIv.visibility = View.INVISIBLE
                    }
                    2 -> {
                        binding.ivDogMic.setBackgroundResource(R.drawable.bg_record_button_active)
                        binding.tvRecordHint.text = "正在录制狗语..."
                        binding.desIv.visibility = View.INVISIBLE
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
        binding.rvCommonSounds.grid(2,LinearLayout.VERTICAL).setup {
            addType<Index1Entity>(R.layout.item_dog)

            onBind {
                getBinding<ItemDogBinding>().apply {
                    val item = getModel<Index1Entity>()
                    ivThumb.setImageResource(item.icon)
                    tvName.text = item.title
                    root.thrillClickListener {


                        if(type == 1) {

                            // 获取原始数据列表中的正确索引
                            val originalList = AppConst.dogSoundList(requireContext())
                            val originalIndex =
                                originalList.indexOfFirst { it.title == item.title && it.icon == item.icon }
                            // 播放狗狗声音，type=1表示狗，传递原始索引
                            CASoundActivity.show(requireContext(), true, originalIndex)
                        }else{
                            // 获取原始数据列表中的正确索引
                            val originalList = AppConst.catSoundList(requireContext())
                            val originalIndex = originalList.indexOfFirst { it.title == item.title && it.icon == item.icon }
                            // 播放猫咪声音，type=2表示猫，传递原始索引
                            CASoundActivity.show(requireContext(), false, originalIndex)
                        }
                    }
                }
            }
        }

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
            Index3Entity(
                getString(R.string.result_dog_1),
                "dog_images/dog_angry.jpeg",
                "dog_sounds/dog_angry.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2),
                "dog_images/dog_yes.jpeg",
                "dog_sounds/dog_yes.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_3),
                "dog_images/dog_shy.jpeg",
                "dog_sounds/dog_shy.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_4),
                "dog_images/dog_dance.jpeg",
                "dog_sounds/dog_dance.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_5),
                "dog_images/dog_happy.jpeg",
                "dog_sounds/dog_happy.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_6),
                "dog_images/dog_soft_angry.jpeg",
                "dog_sounds/dog_soft_angry.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_7),
                "dog_images/dog_handclap.jpeg",
                "dog_sounds/dog_handclap.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_8),
                "dog_images/dog_lie.jpeg",
                "dog_sounds/dog_lie.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_9),
                "dog_images/dog_raise_hand.jpeg",
                "dog_sounds/dog_raise_hand.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_10),
                "dog_images/dog_pet.jpeg",
                "dog_sounds/dog_pet.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_11),
                "dog_images/dog_scared.jpeg",
                "dog_sounds/dog_scared.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_12),
                "dog_images/dog_hi.jpeg",
                "dog_sounds/dog_hi.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_13),
                "dog_images/dog_love.jpeg",
                "dog_sounds/dog_love.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_14),
                "dog_images/dog_happy_walk.jpeg",
                "dog_sounds/dog_happy_walk.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_15),
                "dog_images/dog_sad.jpeg",
                "dog_sounds/dog_sad.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_16),
                "dog_images/dog_exhausted.jpeg",
                "dog_sounds/dog_exhausted.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_17),
                "dog_images/dog_begging.jpeg",
                "dog_sounds/dog_begging.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_18),
                "dog_images/dog_cry_lying.jpeg",
                "dog_sounds/dog_cry_lying.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_19),
                "dog_images/dog_super_angry.jpeg",
                "dog_sounds/dog_super_angry.m4a"
            ),
        )
    }
    private val person2Dog by lazy {
        listOf(
            Index3Entity(
                getString(R.string.result_dog_2_1),
                "dog_images/dog_yes.jpeg",
                "dog_sounds/dog_yes.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_2),
                "dog_images/dog_wow.jpeg",
                "dog_sounds/dog_wow.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_3),
                "dog_images/dog_wonder.jpeg",
                "dog_sounds/dog_wonder.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_4),
                "dog_images/dog_hungry.jpeg",
                "dog_sounds/dog_hungry.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_5),
                "dog_images/dog_agree.jpeg",
                "dog_sounds/dog_agree.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_6),
                "dog_images/dog_happy.jpeg",
                "dog_sounds/dog_happy.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_7),
                "dog_images/dog_scratch.jpeg",
                "dog_sounds/dog_scratch.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_8),
                "dog_images/dog_raise_hand.jpeg",
                "dog_sounds/dog_raise_hand.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_9),
                "dog_images/dog_exhausted.jpeg",
                "dog_sounds/dog_exhausted.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_10),
                "dog_images/dog_cry.jpeg",
                "dog_sounds/dog_cry.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_11),
                "dog_images/dog_angry.jpeg",
                "dog_sounds/dog_angry.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_12),
                "dog_images/dog_cry_lying.jpeg",
                "dog_sounds/dog_cry_lying.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_13),
                "dog_images/dog_dance.jpeg",
                "dog_sounds/dog_dance.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_14),
                "dog_images/dog_handclap.jpeg",
                "dog_sounds/dog_handclap.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_15),
                "dog_images/dog_happy_walk.jpeg",
                "dog_sounds/dog_happy_walk.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_16),
                "dog_images/dog_hi.jpeg",
                "dog_sounds/dog_hi.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_17),
                "dog_images/dog_hi_fence.jpeg",
                "dog_sounds/dog_hi_fence.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_18),
                "dog_images/dog_lie.jpeg",
                "dog_sounds/dog_lie.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_19),
                "dog_images/dog_love.jpeg",
                "dog_sounds/dog_love.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_20),
                "dog_images/dog_no.jpeg",
                "dog_sounds/dog_no.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_21),
                "dog_images/dog_pet.jpeg",
                "dog_sounds/dog_pet.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_22),
                "dog_images/dog_sad.jpeg",
                "dog_sounds/dog_sad.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_23),
                "dog_images/dog_scared.jpeg",
                "dog_sounds/dog_scared.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_24),
                "dog_images/dog_shy.jpeg",
                "dog_sounds/dog_shy.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_25),
                "dog_images/dog_soft_angry.jpeg",
                "dog_sounds/dog_soft_angry.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_26),
                "dog_images/dog_soft_begging.jpeg",
                "dog_sounds/dog_soft_begging.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_27),
                "dog_images/dog_super_angry.jpeg",
                "dog_sounds/dog_super_angry.m4a"
            ),
            Index3Entity(
                getString(R.string.result_dog_2_28),
                "dog_images/dog_yeah.jpeg",
                "dog_sounds/dog_yeah.m4a"
            ),
        )
    }
}