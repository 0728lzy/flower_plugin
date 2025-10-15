package com.cslt.maogoufanyi.model

import org.litepal.crud.LitePalSupport

/**
 * 宠物记事本数据模型
 * 使用LitePal进行数据持久化
 */
data class PetNote(
    var id: Long = 0,
    var petId: Long = 0, // 关联的宠物ID
    var petName: String = "", // 宠物名称
    var title: String = "", // 记事标题
    var content: String = "", // 记事内容
    var category: String = "", // 记事分类（如：疫苗接种、美容预约、训练计划等）
    var reminderTime: Long = 0, // 提醒时间（时间戳，0表示无提醒）
    var isCompleted: Boolean = false, // 是否已完成
    var priority: Int = 0, // 优先级（0-普通，1-重要，2-紧急）
    var createdAt: Long = System.currentTimeMillis(), // 创建时间
    var updatedAt: Long = System.currentTimeMillis() // 更新时间
) : LitePalSupport() {

    /**
     * 更新时间戳
     */
    fun updateTimestamp() {
        updatedAt = System.currentTimeMillis()
    }

    /**
     * 标记为已完成
     */
    fun markAsCompleted() {
        isCompleted = true
        updateTimestamp()
    }

    /**
     * 标记为未完成
     */
    fun markAsIncomplete() {
        isCompleted = false
        updateTimestamp()
    }

    companion object {
        /**
         * 根据宠物ID获取所有记事
         */
        fun getNotesByPetId(petId: Long): List<PetNote> {
            return org.litepal.LitePal.where("petId = ?", petId.toString())
                .order("createdAt desc")
                .find(PetNote::class.java)
        }

        /**
         * 获取所有记事（按创建时间倒序）
         */
        fun getAllNotes(): List<PetNote> {
            return org.litepal.LitePal.order("createdAt desc")
                .find(PetNote::class.java)
        }

        /**
         * 获取未完成的记事
         */
        fun getIncompleteNotes(): List<PetNote> {
            return org.litepal.LitePal.where("isCompleted = ?", "0")
                .order("createdAt desc")
                .find(PetNote::class.java)
        }

        /**
         * 获取有提醒的记事
         */
        fun getNotesWithReminder(): List<PetNote> {
            return org.litepal.LitePal.where("reminderTime > ?", "0")
                .order("reminderTime asc")
                .find(PetNote::class.java)
        }

        /**
         * 删除指定宠物的所有记事
         */
        fun deleteNotesByPetId(petId: Long): Int {
            return org.litepal.LitePal.deleteAll(PetNote::class.java, "petId = ?", petId.toString())
        }
    }
}