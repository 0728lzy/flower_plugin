package com.weini.catdog.ui.dialog

import android.content.Context
import android.widget.ArrayAdapter
import com.weini.catdog.R
import com.weini.catdog.databinding.DialogAddEditNoteBinding
import com.weini.catdog.model.Pet
import com.weini.catdog.model.PetNote
import com.weini.catdog.utils.ToastUtils
import com.lxj.xpopup.core.CenterPopupView

/**
 * 添加/编辑记录对话框
 */
class AddEditNoteDialog(
    context: Context,
    private val pets: List<Pet>,
    private val editingNote: PetNote? = null,
    private val onNoteActionListener: OnNoteActionListener
) : CenterPopupView(context) {

    private lateinit var binding: DialogAddEditNoteBinding
    
    private var selectedPet: Pet? = null

    interface OnNoteActionListener {
        fun onNoteSaved(note: PetNote)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_add_edit_note
    }

    override fun onCreate() {
        super.onCreate()
        binding = DialogAddEditNoteBinding.bind(popupImplView)
        setupViews()
        
        // 防止软键盘自动弹出
        clearFocusFromEditTexts()
    }

    private fun setupViews() {
        // 设置对话框标题
        binding.tvDialogTitle.text = if (editingNote != null) "编辑记录" else "添加记录"
        
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
        binding.etTitle.clearFocus()
        binding.etContent.clearFocus()
        
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
    }

    private fun setupClickListeners() {
        // 按钮点击
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener { saveNote() }
    }

    private fun fillDataIfEditing() {
        editingNote?.let { note ->
            binding.etTitle.setText(note.title)
            binding.etContent.setText(note.content)
            
            // 设置宠物
            selectedPet = pets.find { it.id == note.petId }
            selectedPet?.let { pet ->
                val petIndex = pets.indexOf(pet) + 1 // +1 因为第一项是"请选择宠物"
                binding.spinnerPet.setSelection(petIndex)
            }
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isEmpty()) {
            ToastUtils.show("请输入标题")
            return
        }

        if (content.isEmpty()) {
            ToastUtils.show("请输入内容")
            return
        }

        // 从Spinner获取选中的宠物
        val petSelectedIndex = binding.spinnerPet.selectedItemPosition
        if (petSelectedIndex <= 0) {
            ToastUtils.show("请选择宠物")
            return
        }
        selectedPet = pets[petSelectedIndex - 1] // -1 因为第一项是"请选择宠物"

        val note = if (editingNote != null) {
            // 编辑现有记录
            editingNote!!.copy(
                title = title,
                content = content,
                petId = selectedPet!!.id,
                petName = selectedPet!!.name,
                updatedAt = System.currentTimeMillis()
            )
        } else {
            // 创建新记录
            PetNote(
                id = System.currentTimeMillis(),
                petId = selectedPet!!.id,
                petName = selectedPet!!.name,
                title = title,
                content = content,
                category = "其他", // 默认分类
                priority = 0, // 默认优先级（0-普通）
                reminderTime = 0, // 默认无提醒
                isCompleted = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        }

        onNoteActionListener.onNoteSaved(note)
        dismiss()
    }
}