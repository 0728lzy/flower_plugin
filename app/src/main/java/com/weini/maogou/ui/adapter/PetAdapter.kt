package com.weini.maogou.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.weini.maogou.R
import com.weini.maogou.databinding.ItemPetCardBinding
import com.weini.maogou.model.Pet

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
                        .placeholder(R.drawable.icon_index2_n)
                        .placeholder(R.drawable.icon_index2_n)
                        .into(ivPetAvatar)
                }else{
                    Glide.with(itemView.context)
                        .load(pet.avatar.ifEmpty { pet.getDefaultAvatar() })
                        .placeholder(R.drawable.icon_index1_n)
                        .placeholder(R.drawable.icon_index1_n)
                        .into(ivPetAvatar)
                }


                // 设置宠物信息
                tvPetName.text = pet.name
                tvPetType.text = pet.breed
                
                // 设置年龄数字（去掉"岁"字）
                tvPetAgeNumber.text = pet.age.toString()
                
                // 设置体重数字（去掉"kg"字）
                tvPetWeightNumber.text = pet.weight.toString()

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