package com.qingchu.wangmiao.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qingchu.wangmiao.databinding.ActivityPetBathBinding
import com.qingchu.wangmiao.model.Pet
import com.qingchu.wangmiao.model.PetBathRecord
import com.qingchu.wangmiao.ui.adapter.PetBathRecordAdapter
import com.qingchu.wangmiao.ui.dialog.AddEditBathRecordDialog
import org.litepal.LitePal

/**
 * 宠物洗澡记录Activity
 * 显示和管理宠物的洗澡记录
 */
class PetBathActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetBathBinding
    private lateinit var adapter: PetBathRecordAdapter
    private val bathRecords = mutableListOf<PetBathRecord>()
    private var selectedPetId: Long = -1
    companion object {
        fun forward(context: Context) {
            val intent = Intent(context, PetBathActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetBathBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initViews()
        setupRecyclerView()
        loadAllBathRecords()
    }

    private fun initViews() {
        // 返回按钮
        binding.ivBack.setOnClickListener {
            finish()
        }

        // 添加记录按钮
        binding.btnAddBathRecord.setOnClickListener {
            showAddEditDialog(null)
        }
    }

    private fun setupRecyclerView() {
        adapter = PetBathRecordAdapter(bathRecords) { record, action ->
            when (action) {
                "edit" -> showAddEditDialog(record)
                "delete" -> deleteBathRecord(record)
            }
        }
        
        binding.rvBathRecords.layoutManager = LinearLayoutManager(this)
        binding.rvBathRecords.adapter = adapter
    }

    /**
     * 加载所有洗澡记录
     */
    private fun loadAllBathRecords() {
        try {
            // 确保数据库已初始化
            if (!LitePal.getDatabase().isOpen) {
                LitePal.initialize(this)
                LitePal.getDatabase()
            }

            val records = if (selectedPetId != -1L) {
                // 如果指定了宠物ID，只显示该宠物的记录
                PetBathRecord.getBathRecordsByPetId(selectedPetId)
            } else {
                // 否则显示所有记录
                PetBathRecord.getAllBathRecords()
            }

            bathRecords.clear()
            bathRecords.addAll(records)
            adapter.notifyDataSetChanged()

            // 更新空状态显示
            updateEmptyState()

        } catch (e: Exception) {
            e.printStackTrace()
            // 如果出现数据库相关异常，尝试重新初始化数据库
            try {
                LitePal.initialize(this)
                LitePal.getDatabase()
                loadAllBathRecords()
            } catch (ex: Exception) {
                ex.printStackTrace()
                // 显示错误信息或空状态
                updateEmptyState()
            }
        }
    }

    /**
     * 更新空状态显示
     */
    private fun updateEmptyState() {
        if (bathRecords.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvBathRecords.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvBathRecords.visibility = View.VISIBLE
        }
    }
    var inputPopupView: BasePopupView? = null
    /**
     * 显示添加/编辑对话框
     */
    private fun showAddEditDialog(record: PetBathRecord?) {
        if (inputPopupView?.isShow == true) {
            return
        }
        val dialog = AddEditBathRecordDialog(
            context = this,
            existingRecord = record,
            preSelectedPetId = selectedPetId,
            onBathRecordActionListener = object : AddEditBathRecordDialog.OnBathRecordActionListener {
                override fun onBathRecordSaved(updatedRecord: PetBathRecord) {
                    if (record == null) {
                        // 添加新记录
                        bathRecords.add(0, updatedRecord)
                        adapter.notifyItemInserted(0)
                        binding.rvBathRecords.scrollToPosition(0)
                    } else {
                        // 更新现有记录
                        val index = bathRecords.indexOf(record)
                        if (index != -1) {
                            bathRecords[index] = updatedRecord
                            adapter.notifyItemChanged(index)
                        }
                    }
                    updateEmptyState()
                }
            }
        )
        inputPopupView = XPopup.Builder(this@PetBathActivity)
            .autoOpenSoftInput(false)
            .autoDismiss(false)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(dialog)
            .show()
    }

    /**
     * 删除洗澡记录
     */
    private fun deleteBathRecord(record: PetBathRecord) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("删除记录")
            .setMessage("确定要删除这条洗澡记录吗？")
            .setPositiveButton("删除") { _, _ ->
                try {
                    // 从数据库删除
                    record.delete()
                    
                    // 从列表删除
                    val index = bathRecords.indexOf(record)
                    if (index != -1) {
                        bathRecords.removeAt(index)
                        adapter.notifyItemRemoved(index)
                    }
                    
                    updateEmptyState()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 可以显示错误提示
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /**
     * 获取宠物列表（用于对话框中的宠物选择）
     */
    fun getAllPets(): List<Pet> {
        return try {
            Pet.getAllPets()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}