package com.hardrelice.playmylife.utils

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import com.hardrelice.playmylife.R
import kotlinx.android.synthetic.main.activity_main.*

object CurrencyManager {
    lateinit var currencyPreferences: SharedPreferences
    private const val settingName = "PMLConfig.object"

    val defaultCurrency = mapOf(
        "exist" to true,
        // 以下是货币
        "点数" to 0,
        "心愿" to 0,
        "火焰" to 0,
        "清泉" to 0,
        "青山" to 0,
        "层云" to 0,
        "明月" to 0,
        "帕里" to 0,
    )

    val stringMap = mapOf(
        "点数" to R.string.点数,
        "心愿" to R.string.心愿,
    )

    val currency = hashMapOf<String,Int>()

    fun init() {
        currencyPreferences = ApplicationContext.getSharedPreferences(settingName, Activity.MODE_PRIVATE)
        if (!currencyPreferences.getBoolean("exist", false)) {
            applyDefaultSettings()
        }
        for (key in defaultCurrency.keys){
            if (key!="exist"){
                currency[key] = currencyPreferences.getInt(key,0)
            }
        }
        save()
    }

    fun edit(key: String, value: Any) {
        try {
            val editor = currencyPreferences.edit()
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
            e.message?.let { Log.e("Currency Edit", it) }
        }
    }

    fun edit(map: Map<String, Any>) {
        for (key in map.keys) {
            map[key]?.let { edit(key, it) }
        }
    }

    fun addCurrency(currencyName:String,addCount:Int){
        if (currency[currencyName]==null) currency[currencyName] = 0
        currency[currencyName] = (currency[currencyName] as Int).plus(addCount)
        save()
    }

    fun save(){
        edit(currency)
        handler.post{
            GlobalActivity.bag_point_text.text = currency["点数"].toString()
            GlobalActivity.bag_wish_text.text = currency["心愿"].toString()
        }
    }

    fun getCurrency(currencyName: String):Int?{
        return currency[currencyName]
    }

    fun getRewardMax(currencyName: String):Int{
        return when(currencyName){
            "点数" -> 200
            "心愿"-> 50
            else -> 3
        }
    }

    fun getCurrencyNameLocal(currencyName: String):String?{
        return stringMap[currencyName]?.getString()
    }

    private fun applyDefaultSettings() {
        edit(defaultCurrency)
    }
}