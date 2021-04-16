package com.hardrelice.playmylife.utils

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log

object SettingManager {

    lateinit var sharedPreferences: SharedPreferences

    private const val settingName = "PMLConfig.top"

    val defaultSettings = mapOf(
        "exist" to true,
        // 基本信息
        "用户名" to "你的名字是！",
        "加入时间" to System.currentTimeMillis(),
        "打卡天数" to 0,
        "大目标" to "暂时没有～",
        "小目标" to mutableSetOf<String>(),
        // 自定义以put(键值,参数)的方式加入sharedPreferences
        // eg: sharedPreferences.putInt("魔法少女的低吟", 0)
    )

    fun init() {
        sharedPreferences = ApplicationContext.getSharedPreferences(settingName, Activity.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("exist", false)) {
            println("False")
            applyDefaultSettings()
            println(sharedPreferences.getString("用户名","失败了"))
            sharedPreferences.edit()
                .putString("用户名","hardrelice")
                .apply()
        }
    }

    fun edit(key: String, value: Any) {
        try {
            val editor = sharedPreferences.edit()
            when (value.javaClass.canonicalName) {
                java.lang.String::class.java.canonicalName -> {
                    editor.putString(key, value as String)
                }
                java.lang.Boolean::class.java.canonicalName -> {
                    editor.putBoolean(key, value as Boolean)
                }
                java.lang.Float::class.java.canonicalName -> {
                    editor.putFloat(key, value as Float)
                }
                java.lang.Integer::class.java.canonicalName -> {
                    editor.putInt(key, value as Int)
                }
                java.lang.Long::class.java.canonicalName -> {
                    editor.putLong(key, value as Long)
                }
                "java.util.Collections.SingletonSet" -> {
                    editor.putStringSet(key, value as Set<String>)
                }
            }
            editor.apply()
            editor.commit()
        } catch (e: Exception) {
            e.message?.let { Log.e("Setting Edit", it) }
        }
    }

    fun edit(map: Map<String, Any>) {
        for (key in map.keys) {
            map[key]?.let { edit(key, it) }
        }
    }

    private fun applyDefaultSettings() {
        edit(defaultSettings)
    }

}