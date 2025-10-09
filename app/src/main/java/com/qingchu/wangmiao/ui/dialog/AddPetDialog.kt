package com.qingchu.wangmiao.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.widget.ArrayAdapter
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.databinding.DialogAddPetBinding
import com.qingchu.wangmiao.model.Pet
import com.qingchu.wangmiao.utils.ToastUtils
import com.lxj.xpopup.core.CenterPopupView
import java.text.SimpleDateFormat
import java.util.*

/**
 * 添加/编辑宠物对话框
 */
class AddPetDialog(
    context: Context,
    private val existingPet: Pet? = null,
    private val onPetSaved: (Pet) -> Unit
) : CenterPopupView(context) {

    private lateinit var binding: DialogAddPetBinding
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_add_pet
    }

    override fun onCreate() {
        super.onCreate()
        binding = DialogAddPetBinding.bind(popupImplView)
        setupViews()
        existingPet?.let { fillExistingData(it) }
        
        // 防止软键盘自动弹出
        clearFocusFromEditTexts()
    }

    private fun setupViews() {
        // 设置宠物类型下拉框
        val typeAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            arrayOf("猫咪", "狗狗")
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = typeAdapter

        // 设置关闭按钮点击事件
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        // 设置取消按钮点击事件
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 设置保存按钮点击事件
        binding.btnSave.setOnClickListener {
            if (validateInput()) {
                savePet()
            }
        }
    }

    /**
     * 清除所有 EditText 的焦点，防止软键盘自动弹出
     */
    private fun clearFocusFromEditTexts() {
        binding.etName.clearFocus()
        binding.etAge.clearFocus()
        binding.etWeight.clearFocus()
        binding.etBreed.clearFocus()
        
        // 将焦点设置到根布局，确保没有 EditText 获得焦点
        popupImplView.requestFocus()
    }



    private fun fillExistingData(pet: Pet) {
        with(binding) {
            etName.setText(pet.name)
            // 正确映射宠物类型：cat=0(猫咪), dog=1(狗狗)
            spinnerType.setSelection(if (pet.type == Pet.TYPE_CAT) 0 else 1)
            etBreed.setText(pet.breed)
            etAge.setText(pet.age.toInt().toString())
            etWeight.setText(pet.weight.toString())
        }
    }

    private fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun validateInput(): Boolean {
        with(binding) {
            // 检查宠物名称
            if (etName.text.toString().trim().isEmpty()) {
                ToastUtils.show("请输入宠物名称")
                return false
            }

            // 检查年龄
            val ageText = etAge.text.toString().trim()
            if (ageText.isEmpty()) {
                ToastUtils.show( "请输入宠物年龄")
                return false
            }

            try {
                val age = ageText.toInt()
                if (age < 0 || age > 30) {
                    ToastUtils.show( "年龄必须在0-30岁之间")
                    return false
                }
            } catch (e: NumberFormatException) {
                ToastUtils.show( "请输入有效的年龄")
                return false
            }

            // 检查体重
            val weightText = etWeight.text.toString().trim()
            if (weightText.isEmpty()) {
                ToastUtils.show("请输入宠物体重")
                return false
            }

            try {
                val weight = weightText.toDouble()
                if (weight <= 0 || weight > 100) {
                    ToastUtils.show( "体重必须在0-100kg之间")
                    return false
                }
            } catch (e: NumberFormatException) {
                ToastUtils.show("请输入有效的体重")
                return false
            }

            return true
        }
    }

    private fun savePet() {
        with(binding) {
            val pet = existingPet ?: Pet()
            
            pet.name = etName.text.toString().trim()
            pet.type = if (spinnerType.selectedItemPosition == 0) Pet.TYPE_CAT else Pet.TYPE_DOG
            
            // 使用用户输入的品种信息
            pet.breed = etBreed.text.toString().trim().ifEmpty { 
                val breeds = if (pet.type == Pet.TYPE_CAT) Pet.CAT_BREEDS else Pet.DOG_BREEDS
                breeds[0] // 如果用户没有输入，使用默认品种
            }
            
            pet.age = (etAge.text.toString().trim().toIntOrNull() ?: 0).toDouble()
            pet.weight = etWeight.text.toString().trim().toDoubleOrNull() ?: 0.0
            
            // 设置默认值
            pet.gender = Pet.GENDER_MALE
            pet.color = Pet.COLORS[0]
            pet.birthday = ""
            pet.adoptionDate = ""
            pet.description = ""
            pet.isNeutered = false
            pet.vaccineStatus = Pet.VACCINE_STATUS[0]
            pet.healthStatus = Pet.HEALTH_STATUS[0]
            pet.favoriteFood = ""
            pet.favoriteActivity = ""
            pet.notes = ""
            
            if (existingPet == null) {
                pet.updateTimestamp()
            }
            
            onPetSaved(pet)
            dismiss()
        }
    }
}