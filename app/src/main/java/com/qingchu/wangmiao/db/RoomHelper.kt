package com.qingchu.wangmiao.db

import android.app.Application

object RoomHelper {
    private lateinit var app: Application
    lateinit var impl: AppDatabase

    private const val startVersion = 1
    private var currVersion = 1

    /**
     * @param version 当前apk中的数据库版本号，从1开始
     */
    fun init(application: Application, dbName: String, version: Int = 1) {
        // app = application
        // currVersion = version
        //
        // val builder = Room.databaseBuilder(
        //     app, AppDatabase::class.java, dbName
        // )
        //     // .createFromAsset("game.db")
        //     .fallbackToDestructiveMigration()
        // // 判断本地数据库版本是否需要更新
        //
        // impl = builder.build()
    }


}