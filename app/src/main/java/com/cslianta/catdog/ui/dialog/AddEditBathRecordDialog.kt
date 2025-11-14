package com.cslianta.catdog.ui.dialog

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.lxj.xpopup.core.CenterPopupView
import com.cslianta.catdog.R
import com.cslianta.catdog.databinding.DialogAddEditBathRecordBinding
import com.cslianta.catdog.model.Pet
import com.cslianta.catdog.model.PetBathRecord

/**
 * 添加/编辑洗澡记录对话框
 */
class AddEditBathRecordDialog(
    context: Context,
    private val existingRecord: PetBathRecord? = null,
    private val preSelectedPetId: Long = -1L,
    private val onBathRecordActionListener: OnBathRecordActionListener
) : CenterPopupView(context) {

    private lateinit var binding: DialogAddEditBathRecordBinding
    private val dryingMethods = listOf("低热风", "中热风", "高热风")
    private var selectedPet: Pet? = null
    private var pets: List<Pet> = emptyList()

    interface OnBathRecordActionListener {
        fun onBathRecordSaved(record: PetBathRecord)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_add_edit_bath_record
    }

    override fun onCreate() {
        super.onCreate()
        binding = DialogAddEditBathRecordBinding.bind(popupImplView)
        loadPets()
        setupViews()
        
        // 防止软键盘自动弹出
        clearFocusFromEditTexts()
    }

    private fun loadPets() {
        pets =
            try {
                Pet.getAllPets()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
    }

    private fun setupViews() {
        // 设置对话框标题
        // binding.tvDialogTitle.text = if (existingRecord != null) "编辑洗澡记录" else "添加洗澡记录"
        
        // 设置关闭按钮点击事件
        binding.ivClose?.setOnClickListener {
            dismiss()
        }
        
        setupSpinners()
        setupClickListeners()
        fillDataIfEditing()
    }

    /**
     * 清除所有 EditText 的焦点，防止软键盘自动弹出
     */
    private fun clearFocusFromEditTexts() {
        binding.etWaterTemperature.clearFocus()
        binding.etBathDuration.clearFocus()
        binding.etShampoo.clearFocus()
        binding.etNotes.clearFocus()
        
        // 将焦点设置到根布局，确保没有 EditText 获得焦点
        popupImplView.requestFocus()
    }

    private fun setupSpinners() {
        // 设置宠物选择Spinner
        val petNames = mutableListOf("请选择宠物")
        petNames.addAll(pets.map { it.name })
        val petAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, petNames)
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPet.adapter = petAdapter

        // 设置宠物选择监听器
        binding.spinnerPet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPet = if (position > 0) pets[position - 1] else null
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 设置默认选择
        if (preSelectedPetId != -1L) {
            val petIndex = pets.indexOfFirst { it.id == preSelectedPetId }
            if (petIndex != -1) {
                binding.spinnerPet.setSelection(petIndex + 1) // +1 因为第一项是"请选择宠物"
            }
        }

        // 设置吹风方式选择器
        val dryingAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, dryingMethods)
        dryingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDryingMethod.adapter = dryingAdapter

        // 设置默认吹风方式
        val defaultDryingIndex = dryingMethods.indexOf("低热风")
        if (defaultDryingIndex != -1) {
            binding.spinnerDryingMethod.setSelection(defaultDryingIndex)
        }
    }

    private fun setupClickListeners() {
        // 按钮点击
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener { saveBathRecord() }
    }

    private fun fillDataIfEditing() {
        existingRecord?.let { record ->
            // 设置宠物
            selectedPet = pets.find { it.id == record.petId }
            selectedPet?.let { pet ->
                val petIndex = pets.indexOf(pet) + 1 // +1 因为第一项是"请选择宠物"
                binding.spinnerPet.setSelection(petIndex)
            }

            // 填充其他数据
            binding.etWaterTemperature.setText(record.waterTemperature.toString())
            binding.etBathDuration.setText(record.bathDuration.toString())
            binding.etShampoo.setText(record.shampoo)
            binding.etNotes.setText(record.notes)

            // 选择吹风方式
            val dryingIndex = dryingMethods.indexOf(record.dryingMethod)
            if (dryingIndex != -1) {
                binding.spinnerDryingMethod.setSelection(dryingIndex)
            }
        }
    }

    private fun saveBathRecord() {
        try {
            // 验证宠物选择
            if (selectedPet == null) {
                Toast.makeText(context, "请选择宠物", Toast.LENGTH_SHORT).show()
                return
            }

            val waterTempStr = binding.etWaterTemperature.text.toString().trim()
            val durationStr = binding.etBathDuration.text.toString().trim()

            if (waterTempStr.isEmpty()) {
                Toast.makeText(context, "请输入水温", Toast.LENGTH_SHORT).show()
                return
            }

            if (durationStr.isEmpty()) {
                Toast.makeText(context, "请输入洗澡时长", Toast.LENGTH_SHORT).show()
                return
            }

            val waterTemp = waterTempStr.toIntOrNull()
            val duration = durationStr.toIntOrNull()

            if (waterTemp == null || waterTemp < 20 || waterTemp > 50) {
                Toast.makeText(context, "水温应在20-50°C之间", Toast.LENGTH_SHORT).show()
                return
            }

            if (duration == null || duration < 1 || duration > 120) {
                Toast.makeText(context, "洗澡时长应在1-120分钟之间", Toast.LENGTH_SHORT).show()
                return
            }

            val shampoo = binding.etShampoo.text.toString().trim()
            val notes = binding.etNotes.text.toString().trim()
            val dryingMethod = dryingMethods[binding.spinnerDryingMethod.selectedItemPosition]

            // 创建或更新记录
            val record = existingRecord?.copy(
                petId = selectedPet!!.id,
                petName = selectedPet!!.name,
                waterTemperature = waterTemp,
                bathDuration = duration,
                shampoo = shampoo,
                dryingMethod = dryingMethod,
                notes = notes
            ) ?: PetBathRecord(
                petId = selectedPet!!.id,
                petName = selectedPet!!.name,
                waterTemperature = waterTemp,
                bathDuration = duration,
                shampoo = shampoo,
                dryingMethod = dryingMethod,
                notes = notes
            )

            // 更新时间戳
            record.updateTimestamp()

            // 保存到数据库
            if (record.save()) {
                onBathRecordActionListener.onBathRecordSaved(record)
                dismiss()
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "保存失败：${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}