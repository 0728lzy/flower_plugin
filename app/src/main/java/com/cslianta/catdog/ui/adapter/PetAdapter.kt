package com.cslianta.catdog.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cslianta.catdog.R
import com.cslianta.catdog.databinding.ItemPetCardBinding
import com.cslianta.catdog.model.Pet
import com.cslianta.catdog.utils.lzy.ScreenUtils

/**
 * 宠物列表适配器
 */
class PetAdapter(
    private val petList: List<Pet>,
    private val onItemAction: (Pet, String) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    companion object {
        const val ACTION_CLICK = "click"
        const val ACTION_EDIT = "edit"
        const val ACTION_DELETE = "delete"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(petList[position])
    }

    override fun getItemCount(): Int = petList.size

    inner class PetViewHolder(
        private val binding: ItemPetCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pet: Pet) {
            with(binding) {
                // 设置宠物头像
                if (pet.type=="dog"){
                    Glide.with(itemView.context)
                        .load(pet.avatar.ifEmpty { pet.getDefaultAvatar() })
                        .transform(CenterCrop(),RoundedCorners(ScreenUtils.dip2px(50,itemView.context)))
                        .placeholder(R.drawable.icon_index2_n)
                        .placeholder(R.drawable.icon_index2_n)
                        .into(ivPetAvatar)
                }else{
                    Glide.with(itemView.context)
                        .load(pet.avatar.ifEmpty { pet.getDefaultAvatar() })
                        .transform(CenterCrop(),RoundedCorners(ScreenUtils.dip2px(50,itemView.context)))
                        .placeholder(R.drawable.icon_index1_n)
                        .placeholder(R.drawable.icon_index1_n)
                        .into(ivPetAvatar)
                }


                // 设置宠物信息
                tvPetName.text = pet.name
                tvPetBreed.text = "品种：${pet.breed}"
                tvPetType.text = "类型：${pet.type}"
                // 设置年龄数字（去掉"岁"字）
                tvPetAgeNumber.text = "年龄：${pet.age.toInt()}岁"
                
                // 设置体重数字（去掉"kg"字）
                tvPetWeightNumber.text = "体重：${pet.weight}kg"

                // 卡片点击事件 - 点击跳转到宠物相册
                root.setOnClickListener {
                    onItemAction(pet, ACTION_CLICK)
                }
                
                // 长按显示菜单（编辑、删除）
                root.setOnLongClickListener {
                    showPopupMenu(it, pet)
                    true
                }
            }
        }

        /**
         * 显示弹出菜单
         */
        private fun showPopupMenu(view: View, pet: Pet) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.menu_pet_actions, popupMenu.menu)
            
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        onItemAction(pet, ACTION_EDIT)
                        true
                    }
                    R.id.action_delete -> {
                        onItemAction(pet, ACTION_DELETE)
                        true
                    }
                    else -> false
                }
            }
            
            popupMenu.show()
        }


    }
}