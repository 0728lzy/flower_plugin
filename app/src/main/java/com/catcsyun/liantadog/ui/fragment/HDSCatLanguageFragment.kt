package com.catcsyun.liantadog.ui.fragment

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
import com.catcsyun.liantadog.AppConst
import com.catcsyun.liantadog.R
import com.catcsyun.liantadog.base.dj.RootFragment
import com.catcsyun.liantadog.csj.ZYMAllAdsUtils
import com.catcsyun.liantadog.databinding.FragmentCatLanguageBinding
import com.catcsyun.liantadog.databinding.ItemDogBinding
import com.catcsyun.liantadog.entity.Index1Entity
import com.catcsyun.liantadog.entity.Index3Entity
import com.catcsyun.liantadog.event.SimpleEvent
import com.catcsyun.liantadog.ext.countDown
import com.catcsyun.liantadog.ext.getBinding
import com.catcsyun.liantadog.ext.thrillClickListener
import com.catcsyun.liantadog.ui.activity.KLTGSoundActivity
import com.catcsyun.liantadog.ui.dialog.ResultDialog

import com.catcsyun.liantadog.utils.lzy.LZYLog
import com.catcsyun.liantadog.utils.lzy.PermissionUtils
import com.drake.brv.utils.grid

import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Formatter

class HDSCatLanguageFragment : RootFragment(R.layout.fragment_cat_language) {

    var _binding: FragmentCatLanguageBinding? = null

    private var isRecording = false
    private var recordingType = 0 // 0: 未录音, 1: 人话录音, 2: 喵语录音
    private var record: com.catcsyun.liantadog.utils.AudioRecordUtil? = null

    private var job: Job? = null
    
    val binding get() = _binding!!

    private var PetType = 1;  //1 狗  2 猫
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
                var entity =  catList.random()


                if(PetType == 1){
                    // 停止录音并展示结果
                    entity = if (type==1) dogList.random() else person2Dog.random()
                }




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
//                                binding.desIv.visibility = View.VISIBLE
                            }else {
                                ResultDialog(entity).show(requireRootActivity())
//                                binding.desIv.visibility = View.VISIBLE
                            }
                            binding.tvRecordHint.text = "点击按钮开始录音"
                        }
                    }, 600)
                }

                // 恢复按钮样式
//                binding.ivCatMic.setBackgroundResource(R.drawable.bg_record_button_red)
                isRecording = false
                recordingType = 0
            } else {
                // 开始录音
                isRecording = true
                recordingType = type
                if (record == null) {
                    record = com.catcsyun.liantadog.utils.AudioRecordUtil()
                    record?.setOnCompleteListener {
                        record = null
                    }
                }
                record?.startRecord()

                // 按钮激活状态与提示文案
                when (type) {
                    1 -> {
                        binding.tvRecordHint.text = "正在录制人话..."
//                        binding.desIv.visibility = View.INVISIBLE
                    }
                    2 -> {
//                        binding.ivCatMic.setBackgroundResource(R.drawable.bg_record_button_active)
                        binding.tvRecordHint.text = "正在录制喵语..."
//                        binding.desIv.visibility = View.INVISIBLE
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
                        // 获取原始数据列表中的正确索引
                        val originalList = AppConst.catSoundList(requireContext())
                        val originalIndex = originalList.indexOfFirst { it.title == item.title && it.icon == item.icon }
                        // 播放猫咪声音，type=2表示猫，传递原始索引
                        KLTGSoundActivity.show(requireContext(), false, originalIndex)
                    }
                }
            }
        }
        
        // 加载猫咪声音数据
        loadCatSounds()

        binding.dogLl.setOnClickListener({
            initDog()
            ZYMAllAdsUtils.showAdCpTurnTab(requireActivity(),"CP")
        })

        binding.catLl.setOnClickListener({
            initCat()
            ZYMAllAdsUtils.showAdCpTurnTab(requireActivity(),"CP")
        })

        initDog()

    }


    private fun initDog(){
        binding.dogLl.setBackgroundResource(R.drawable.app_tab_selected_bg)
        binding.dogTv.setTextColor(Color.parseColor("#ffffff"))

        binding.catLl.setBackgroundResource(R.drawable.app_tab_unselected_bg)
        binding.catTv.setTextColor(Color.parseColor("#000000"))
        PetType = 1

    }

    private fun initCat(){
        binding.catLl.setBackgroundResource(R.drawable.app_tab_selected_bg)
        binding.catTv.setTextColor(Color.parseColor("#ffffff"))
        PetType = 2
        binding.dogLl.setBackgroundResource(R.drawable.app_tab_unselected_bg)
        binding.dogTv.setTextColor(Color.parseColor("#000000"))


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