package com.hardrelice.playmylife.utils

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log

object PropManager {
    lateinit var sharedPreferences: SharedPreferences

    private const val settingName = "PMLConfig.prop"
    // 初始道具
    val defaultSettings = mapOf(
        "exist" to true,
        "奇怪的碎片" to "集齐它们！|0",
        "摸鱼卡" to "太累了，摸了|0",
        "时光机" to "可以修改历史，但历史不会改变|0",
        "点数卡" to "随机获得一些点数！|0",
        "心愿卡" to "随机获得一些心愿！|0",
        "商品卡" to "创建/改动一项商品|0",
    )

    var prop = hashMapOf<String, Any>()

    fun init() {
        sharedPreferences = ApplicationContext.getSharedPreferences(settingName, Activity.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("exist", false)) {
            applyDefaultSettings()
        }
        for(key in sharedPreferences.all.keys){
            if(sharedPreferences.all[key]!=null) prop[key] = sharedPreferences.all[key] as Any
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
            e.message?.let { Log.e("Prop Edit", it) }
        }
    }

    fun edit(map: Map<String, Any>) {
        for (key in map.keys) {
            map[key]?.let { edit(key, it) }
        }
    }

    fun save() {
        edit(prop)
    }

    private fun applyDefaultSettings() {
        edit(defaultSettings)
    }

    fun editPropCount(key: String, newCount: Int){
        prop[key] = (prop[key] as String).editPropCount(newCount)
        save()
    }

    fun addPropCount(key: String, addCount: Int){
        prop[key] = (prop[key] as String).addPropCount(addCount)
        save()
    }



    fun generateProp(key: String, description:String) {
        prop[key] = "$description|0"
        save()
    }

    fun String.editPropCount(newCount:Int): String = "${this.parseDescription()}|$newCount"
    fun String.addPropCount(addCount:Int): String = "${this.parseDescription()}|${this.parseCount()+addCount}"
    fun String.parseDescription(): String = this.slice(0..this.lastIndexOf('|'))
    fun String.parseCount(): Int = this.slice(this.lastIndexOf('|')+1..this.length).toInt()

}