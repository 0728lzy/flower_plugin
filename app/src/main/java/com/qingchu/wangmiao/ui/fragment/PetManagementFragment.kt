package com.qingchu.wangmiao.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.lxj.xpopup.XPopup
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.RootFragment
import com.qingchu.wangmiao.databinding.FragmentPetManagementBinding
import com.qingchu.wangmiao.model.Pet
import com.qingchu.wangmiao.ui.activity.PetBathActivity
import com.qingchu.wangmiao.ui.activity.PetNotesActivity
import com.qingchu.wangmiao.ui.activity.PetPhotosActivity
import com.qingchu.wangmiao.ui.adapter.PetAdapter
import com.qingchu.wangmiao.ui.dialog.AddPetDialog
import com.qingchu.wangmiao.utils.ToastUtils
import org.litepal.LitePal
import kotlinx.coroutines.*

/**
 * 宠物管理Fragment
 * 实现宠物信息的增删改查功能
 */
class PetManagementFragment : RootFragment(R.layout.fragment_pet_management) {

    private var _binding: FragmentPetManagementBinding? = null
    private val binding get() = _binding!!

    private lateinit var petAdapter: PetAdapter
    private val petList = mutableListOf<Pet>()

    private val fragmentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPetManagementBinding.bind(view)
        initViews()
        loadPets()
    }

    private fun initViews() {
        // 初始化RecyclerView
        petAdapter = PetAdapter(petList) { pet, action ->
            when (action) {
                PetAdapter.ACTION_CLICK -> {
                    // 点击宠物卡片，显示详情
                    showPetDetail(pet)
                }

                PetAdapter.ACTION_EDIT -> {
                    // 编辑宠物
                    showEditPetDialog(pet)
                }

                PetAdapter.ACTION_DELETE -> {
                    // 删除宠物
                    showDeleteConfirmDialog(pet)
                }
            }
        }

        binding.rvPets.apply {
            layoutManager = GridLayoutManager(context, 2) // 2列网格布局
            adapter = petAdapter
        }

        // 添加宠物按钮点击事件
        binding.btnAddPet.setOnClickListener {
            showAddPetDialog()
        }

        // 管理功能点击事件
        binding.layoutPetAlbum.setOnClickListener {
            if (petList.isEmpty()) {
                ToastUtils.show("请先添加宠物")
                return@setOnClickListener
            }

            // 如果只有一只宠物，直接跳转到该宠物的相册
            PetPhotosActivity.forward(requireContext())

        }

        binding.layoutPetNotes.setOnClickListener {
            if (petList.isEmpty()) {
                ToastUtils.show("请先添加宠物")
                return@setOnClickListener
            }

            // 如果只有一只宠物，直接跳转到该宠物的相册
            PetNotesActivity.forward(requireContext())
        }

        binding.layoutBathRecord.setOnClickListener {
            ToastUtils.show("洗澡记录功能开发中...")
            if (petList.isEmpty()) {
                ToastUtils.show("请先添加宠物")
                return@setOnClickListener
            }

            // 如果只有一只宠物，直接跳转到该宠物的相册
            PetBathActivity.forward(requireContext())
        }
    }

    /**
     * 加载宠物列表
     */
    private fun loadPets() {
        showLoading(true)

        fragmentScope.launch {
            try {
                val pets = withContext(Dispatchers.IO) {
                    LitePal.findAll(Pet::class.java)
                }

                petList.clear()
                petList.addAll(pets)
                petAdapter.notifyDataSetChanged()

                updateEmptyState()

            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("加载宠物列表失败: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    /**
     * 显示添加宠物对话框
     */
    private fun showAddPetDialog() {
        val dialog = AddPetDialog(requireContext()) { pet ->
            addPet(pet)
        }
        XPopup.Builder(requireContext())
            .asCustom(dialog)
            .show()
    }

    /**
     * 显示编辑宠物对话框
     */
    private fun showEditPetDialog(pet: Pet) {
        val dialog = AddPetDialog(requireContext(), pet) { updatedPet ->
            updatePet(updatedPet)
        }
        XPopup.Builder(requireContext())
            .asCustom(dialog)
            .show()
    }

    /**
     * 显示删除确认对话框
     */
    private fun showDeleteConfirmDialog(pet: Pet) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("删除宠物")
            .setMessage("确定要删除 ${pet.name} 吗？此操作不可撤销。")
            .setPositiveButton("删除") { _, _ ->
                deletePet(pet)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /**
     * 显示宠物选择对话框
     */
    private fun showPetSelectionDialog() {
        val petNames = petList.map { it.name }.toTypedArray()

        XPopup.Builder(requireContext())
            .asBottomList("选择宠物", petNames) { position, text ->
                com.qingchu.wangmiao.ui.activity.PetPhotosActivity.forward(requireContext())
            }
            .show()
    }

    /**
     * 显示宠物详情
     */
    private fun showPetDetail(pet: Pet) {
        // 点击宠物卡片跳转到宠物相册
        com.qingchu.wangmiao.ui.activity.PetPhotosActivity.forward(requireContext())
    }

    /**
     * 添加宠物
     */
    private fun addPet(pet: Pet) {
        fragmentScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    pet.save()
                }

                if (success) {
                    petList.add(pet)
                    petAdapter.notifyItemInserted(petList.size - 1)
                    updateEmptyState()
                    ToastUtils.show("添加宠物成功")
                } else {
                    ToastUtils.show("添加宠物失败")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("添加宠物失败: ${e.message}")
            }
        }
    }

    /**
     * 更新宠物信息
     */
    private fun updatePet(pet: Pet) {
        fragmentScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    pet.updateTimestamp()
                    pet.save()
                }

                if (success) {
                    val index = petList.indexOfFirst { it.id == pet.id }
                    if (index != -1) {
                        petList[index] = pet
                        petAdapter.notifyItemChanged(index)
                        ToastUtils.show("更新宠物信息成功")
                    }
                } else {
                    ToastUtils.show("更新宠物信息失败")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("更新宠物信息失败: ${e.message}")
            }
        }
    }

    /**
     * 删除宠物
     */
    private fun deletePet(pet: Pet) {
        fragmentScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    LitePal.delete(Pet::class.java, pet.id)
                }

                if (success > 0) {
                    val index = petList.indexOfFirst { it.id == pet.id }
                    if (index != -1) {
                        petList.removeAt(index)
                        petAdapter.notifyItemRemoved(index)
                        updateEmptyState()
                        ToastUtils.show("删除宠物成功")
                    }
                } else {
                    ToastUtils.show("删除宠物失败")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("删除宠物失败: ${e.message}")
            }
        }
    }

    /**
     * 更新空状态显示
     */
    private fun updateEmptyState() {
        if (petList.isEmpty()) {
            binding.rvPets.visibility = View.GONE
            binding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvPets.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        }
    }

    /**
     * 显示/隐藏加载状态
     */
    private fun showLoading(show: Boolean) {
        binding.progressLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentScope.cancel()
        _binding = null
    }

    companion object {
        fun newInstance(): PetManagementFragment {
            return PetManagementFragment()
        }
    }
}