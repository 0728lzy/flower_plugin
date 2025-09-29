package com.weini.maogou.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.weini.maogou.R
import com.weini.maogou.databinding.DialogAddPetBinding
import com.weini.maogou.model.Pet
import com.weini.maogou.utils.ToastUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * 添加/编辑宠物对话框
 */
class AddPetDialog(
    private val context: Context,
    private val existingPet: Pet? = null,
    private val onPetSaved: (Pet) -> Unit
) {

    private val binding = DialogAddPetBinding.inflate(LayoutInflater.from(context))
    private val dialog: AlertDialog
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        dialog = AlertDialog.Builder(context)
            .setTitle(if (existingPet == null) "添加宠物" else "编辑宠物")
            .setView(binding.root)
            .setPositiveButton("保存", null)
            .setNegativeButton("取消", null)
            .create()

        setupViews()
        existingPet?.let { fillExistingData(it) }
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

        // 设置性别下拉框
        val genderAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            arrayOf("公", "母")
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = genderAdapter

        // 设置品种下拉框
        updateBreedSpinner(0) // 默认显示猫咪品种

        // 宠物类型改变时更新品种列表
        binding.spinnerType.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateBreedSpinner(position)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })

        // 设置颜色下拉框
        val colorAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            Pet.COLORS
        )
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerColor.adapter = colorAdapter

        // 设置疫苗状态下拉框
        val vaccineAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            Pet.VACCINE_STATUS
        )
        vaccineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerVaccineStatus.adapter = vaccineAdapter

        // 设置健康状态下拉框
        val healthAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            Pet.HEALTH_STATUS
        )
        healthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHealthStatus.adapter = healthAdapter

        // 生日选择
        binding.etBirthday.setOnClickListener {
            showDatePicker { date ->
                binding.etBirthday.setText(dateFormat.format(date))
            }
        }

        // 领养日期选择
        binding.etAdoptionDate.setOnClickListener {
            showDatePicker { date ->
                binding.etAdoptionDate.setText(dateFormat.format(date))
            }
        }

        // 设置保存按钮点击事件
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (validateInput()) {
                    savePet()
                }
            }
        }
    }

    private fun updateBreedSpinner(typePosition: Int) {
        val breeds = if (typePosition == 0) Pet.CAT_BREEDS else Pet.DOG_BREEDS
        val breedAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            breeds
        )
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBreed.adapter = breedAdapter
    }

    private fun fillExistingData(pet: Pet) {
        with(binding) {
            etName.setText(pet.name)
            spinnerType.setSelection(if (pet.type == Pet.TYPE_CAT) 0 else 1)
            
            // 设置品种
            val breeds = if (pet.type == Pet.TYPE_CAT) Pet.CAT_BREEDS else Pet.DOG_BREEDS
            val breedIndex = breeds.indexOf(pet.breed)
            if (breedIndex >= 0) {
                spinnerBreed.setSelection(breedIndex)
            }
            
            etAge.setText(pet.age.toString())
            etWeight.setText(pet.weight.toString())
            spinnerGender.setSelection(if (pet.gender == Pet.GENDER_MALE) 0 else 1)
            
            val colorIndex = Pet.COLORS.indexOf(pet.color)
            if (colorIndex >= 0) {
                spinnerColor.setSelection(colorIndex)
            }
            
            etBirthday.setText(pet.birthday)
            etAdoptionDate.setText(pet.adoptionDate)
            etDescription.setText(pet.description)
            switchNeutered.isChecked = pet.isNeutered
            
            val vaccineIndex = Pet.VACCINE_STATUS.indexOf(pet.vaccineStatus)
            if (vaccineIndex >= 0) {
                spinnerVaccineStatus.setSelection(vaccineIndex)
            }
            
            val healthIndex = Pet.HEALTH_STATUS.indexOf(pet.healthStatus)
            if (healthIndex >= 0) {
                spinnerHealthStatus.setSelection(healthIndex)
            }
            
            etFavoriteFood.setText(pet.favoriteFood)
            etFavoriteActivity.setText(pet.favoriteActivity)
            etNotes.setText(pet.notes)
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
            if (etName.text.toString().trim().isEmpty()) {
                ToastUtils.show("请输入宠物名称")
                etName.requestFocus()
                return false
            }

            val ageText = etAge.text.toString().trim()
            if (ageText.isEmpty()) {
                ToastUtils.show("请输入宠物年龄")
                etAge.requestFocus()
                return false
            }

            try {
                val age = ageText.toInt()
                if (age < 0 || age > 30) {
                    ToastUtils.show("年龄应在0-30岁之间")
                    etAge.requestFocus()
                    return false
                }
            } catch (e: NumberFormatException) {
                ToastUtils.show("请输入有效的年龄")
                etAge.requestFocus()
                return false
            }

            val weightText = etWeight.text.toString().trim()
            if (weightText.isEmpty()) {
                ToastUtils.show("请输入宠物体重")
                etWeight.requestFocus()
                return false
            }

            try {
                val weight = weightText.toDouble()
                if (weight <= 0 || weight > 100) {
                    ToastUtils.show("体重应在0-100kg之间")
                    etWeight.requestFocus()
                    return false
                }
            } catch (e: NumberFormatException) {
                ToastUtils.show("请输入有效的体重")
                etWeight.requestFocus()
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
            
            val breeds = if (pet.type == Pet.TYPE_CAT) Pet.CAT_BREEDS else Pet.DOG_BREEDS
            pet.breed = breeds[spinnerBreed.selectedItemPosition]
            
            pet.age = (etAge.text.toString().trim().toIntOrNull() ?: 0).toDouble()
            pet.weight = etWeight.text.toString().trim().toDoubleOrNull() ?: 0.0
            pet.gender = if (spinnerGender.selectedItemPosition == 0) Pet.GENDER_MALE else Pet.GENDER_FEMALE
            pet.color = Pet.COLORS[spinnerColor.selectedItemPosition]
            pet.birthday = etBirthday.text.toString()
            pet.adoptionDate = etAdoptionDate.text.toString()
            pet.description = etDescription.text.toString().trim()
            pet.isNeutered = switchNeutered.isChecked
            pet.vaccineStatus = Pet.VACCINE_STATUS[spinnerVaccineStatus.selectedItemPosition]
            pet.healthStatus = Pet.HEALTH_STATUS[spinnerHealthStatus.selectedItemPosition]
            pet.favoriteFood = etFavoriteFood.text.toString().trim()
            pet.favoriteActivity = etFavoriteActivity.text.toString().trim()
            pet.notes = etNotes.text.toString().trim()
            
            if (existingPet == null) {
                pet.updateTimestamp()
            }
            
            onPetSaved(pet)
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}