package com.pet.translator.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.hjq.permissions.Permission
import com.pet.translator.R
import com.pet.translator.base.dj.RootFragment
import com.pet.translator.databinding.FragmentIndex3Binding
import com.pet.translator.entity.Index3Entity
import com.pet.translator.event.SimpleEvent
import com.pet.translator.ext.countDown
import com.pet.translator.ext.getBinding
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.ui.dialog.ResultDialog
import com.pet.translator.utils.lzy.LZYADSUtils
import com.pet.translator.utils.lzy.LZYLog
import com.pet.translator.utils.lzy.PermissionUtils
import com.pet.translator.widget.dialog.LoadingDiaLog
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Formatter


class Index3Fragment : RootFragment(R.layout.fragment_index_3) {

    var _binding: FragmentIndex3Binding? = null

    val binding get() = _binding!!

    private var record: com.pet.translator.utils.AudioRecordUtil? = null

    private lateinit var lzyadsUtils: LZYADSUtils
    private lateinit var myDiaLog:LoadingDiaLog


    private var isDog1 = true
    private var isDog2 = true

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
        if(message.simple == 2){
            LZYLog.e("simple","message simple:${message.simple}")
            LZYADSUtils("Index5Fragment",requireActivity()).loadSimpleAdTurn(binding.feedContainerFragment3,-1)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()
        lzyadsUtils= LZYADSUtils("Index3Fragment",requireActivity())
        binding.viewAnimal1.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val scrollY = binding.viewAnimal1.scrollY
            val contentHeight = binding.viewAnimal1.getChildAt(0).height
            val scrollViewHeight = binding.viewAnimal1.height
            val calc = contentHeight - scrollViewHeight
            if (calc == 0) return@setOnScrollChangeListener
            val progress = scrollY.toFloat() / (calc)
            isDog1 = progress < 0.5f
        }
        binding.viewAnimal2.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val scrollY = binding.viewAnimal2.scrollY
            val contentHeight = binding.viewAnimal2.getChildAt(0).height
            val scrollViewHeight = binding.viewAnimal2.height
            val calc = contentHeight - scrollViewHeight
            if (calc == 0) return@setOnScrollChangeListener
            val progress = scrollY.toFloat() / calc
            isDog2 = progress < 0.5f
        }

        binding.btnSwitch.thrillClickListener {
            if (binding.viewAnimal1.isVisible) {
                // person to dog
                binding.viewAnimal1.isVisible = false
                binding.viewAnimal2.isVisible = true
                binding.ivTranslateFrom1.isVisible = true
                binding.ivTranslateFrom2.isVisible = false
            } else {
                // dog to person
                binding.viewAnimal1.isVisible = true
                binding.viewAnimal2.isVisible = false
                binding.ivTranslateFrom1.isVisible = false
                binding.ivTranslateFrom2.isVisible = true
            }
        }
        binding.btnRecord.thrillClickListener {
            PermissionUtils.tryToDoSomethingWithCheckPermissionAndCode(
                requireContext(),
                arrayOf(
                    Permission.RECORD_AUDIO,
                ),
                1,
                "权限被拒绝，无法使用该功能"
            ){
                // 录制
                if (binding.lottie.isAnimating) {
                    val entity = if (binding.viewAnimal1.isVisible) {
                        if (isDog1) {
                            person2Dog.random()
                        } else {
                            catList.random()
                        }
                    } else {
                        if (isDog2) {
                            dog2Person.random()
                        } else {
                            catList.random()
                        }
                    }
                    binding.lottie.cancelAnimation()
                    record?.stopRecord()
                    myDiaLog= LoadingDiaLog(requireContext())
                    myDiaLog.show()
                    lzyadsUtils.showAdJL(myDiaLog){
                        Handler().postDelayed({
                            // 这里是延时后执行的代码
                            runOnUiThread {
                                ResultDialog(entity).show(requireRootActivity())
                                job?.cancel()
                                binding.tvRecordingDuration.text = "00:00"
                            }
                        }, 600)
                    }
                } else {
                    binding.lottie.playAnimation()
                    if (record == null) {
                        record = com.pet.translator.utils.AudioRecordUtil()
                        record?.setOnCompleteListener {
                            record = null
                        }
                    }
                    record?.startRecord()

                    job = requireRootActivity().countDown(
                        time = 100000,
                        start = {

                        },
                        next = {
                            val time = 100000 - it.toInt()
                            val seconds = time % 60
                            val minutes = (time / 60) % 60
                            // 计时
                            binding.tvRecordingDuration.text = Formatter().format("%02d:%02d", minutes, seconds).toString()
                        },
                        end = {

                        }
                    )
                }
            }
        }
    }

    private var job: Job? = null

    private val dog2Person by lazy {
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

    private val catList by lazy {
        listOf(
            Index3Entity(
                "",
                "cat_images/cat_01.webp",
                "cat_sounds/cat_01.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_02.webp",
                "cat_sounds/cat_02.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_03.webp",
                "cat_sounds/cat_03.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_04.webp",
                "cat_sounds/cat_04.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_05.webp",
                "cat_sounds/cat_05.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_06.webp",
                "cat_sounds/cat_06.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_07.webp",
                "cat_sounds/cat_07.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_08.webp",
                "cat_sounds/cat_08.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_09.webp",
                "cat_sounds/cat_09.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_10.webp",
                "cat_sounds/cat_10.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_11.webp",
                "cat_sounds/cat_11.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_11.webp",
                "cat_sounds/cat_11.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_12.webp",
                "cat_sounds/cat_12.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_13.webp",
                "cat_sounds/cat_13.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_14.webp",
                "cat_sounds/cat_14.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_15.webp",
                "cat_sounds/cat_15.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_16.webp",
                "cat_sounds/cat_16.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_17.webp",
                "cat_sounds/cat_17.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_18.webp",
                "cat_sounds/cat_18.wav"
            ),
            Index3Entity(
                "",
                "cat_images/cat_19.webp",
                "cat_sounds/cat_19.wav"
            ),
        )
    }
}