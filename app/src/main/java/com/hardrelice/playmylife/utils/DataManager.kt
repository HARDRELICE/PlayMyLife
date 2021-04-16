package com.hardrelice.playmylife.utils

import java.io.File

open class DataManager(
    private val hashMap: HashMap<String, HashMap<String, Any>>? = null,
    val basePath: String = "",
    var defaultMap: HashMap<String, Any> = hashMapOf(),
) : HashMap<String, HashMap<String, String>>() {

    private var separatorList = mutableListOf("\n", "|", ";", ",")

    init {
        if (hashMap != null) {
            for (dataKey in hashMap.keys) {
                val newMap = hashMapOf<String,String>()
                for (key in hashMap[dataKey]!!.keys) {
//                    println(hashMap[key]?.javaClass?.canonicalName)

                    newMap[key] = when (hashMap[key]?.javaClass?.canonicalName) {
                        arrayListOf(1).javaClass.canonicalName -> {
                            (hashMap[key] as List<*>).joinToString(separatorList[2])
                        }
                        "java.lang.Integer[]" -> {
                            (hashMap[key] as Array<*>).toList().joinToString(separatorList[2])
                        }
                        "java.lang.String[]" -> {
                            (hashMap[key] as Array<*>).toList().joinToString(separatorList[2])
                        }
                        else -> hashMap[key].toString()
                    }
                }
                this[dataKey] = newMap
            }
        }
        loadData()
    }

    fun getDefault(): HashMap<String, HashMap<String,Any>>? {
        return hashMap
    }

    private fun List<List<Any>>.joinListToString(first: String, second: String): String {
        var list = mutableListOf<String>()
        for (obj in this) {
            list.add(obj.joinToString(second))
        }
        return list.joinToString(first)
    }

//    private fun List<Pair<Any, Any>>.joinPairToString(first: String, second: String): String {
//        var list = mutableListOf<String>()
//        for (obj in this) {
//            list.add(obj.toList().joinToString(second))
//        }
//        return list.joinToString(first)
//    }

    fun saveData(dataKey: String, filePath: String) {
        var str = ""
        if (dataKey in this.keys) {
            for (key in this[dataKey]!!.keys) {
                if (key != "") str += key + separatorList[1] + this[key] + separatorList[0]
            }
        } else {
            for (key in defaultMap.keys) {
                if (key != "") str += key + separatorList[1] + this[key] + separatorList[0]
            }
        }
        File(filePath).writeText(str)
    }

    fun setDataSeparator(separatorList: MutableList<String>) {
        this.separatorList = separatorList
    }

    fun putData(dataKey:String, key: String, value: Any) {
        this[dataKey]?.set(key, value.toString())
    }

    fun putStringList(dataKey:String, key: String, value: List<String>) {
        this[dataKey]?.set(key, value.joinToString(separatorList[2]))
    }

    fun putListList(dataKey:String,key: String, value: List<List<Any>>) {
        this[dataKey]?.set(key, value.joinListToString(separatorList[2], separatorList[3]))
    }

//    fun putPairList(key: String, value: List<Pair<Any, Any>>) {
//        this[key] = value.joinPairToString(separatorList[2],separatorList[3])
//    }

    fun getBoolean(dataKey: String,key: String): Boolean {
        return this[dataKey]?.get(key).toBoolean()
    }

    fun getInt(dataKey: String, key: String): Int {
        return this[dataKey]?.get(key).toString().toInt()
    }

    fun getString(dataKey: String, key: String): String {
        return this[dataKey]?.get(key).toString()
    }

    fun getStringList(dataKey: String,key: String): List<String>? {
        return this[dataKey]?.get(key)?.split(separatorList[2])
    }

    fun getListList(dataKey: String,key: String): List<List<String>>? {
        var list = mutableListOf<List<String>>()
        return try {
            for (item in this[dataKey]?.get(key)!!.split(separatorList[2])) {
                list.add(item.split(separatorList[3]))
            }
            list
        } catch (e: Exception) {
            null
        }
    }

    private fun parseData(dataKey: String, filePath: String) {
        if (File(filePath).exists()) {
            val content = File(filePath).readText()
            val list = content.split(separatorList[0])
            for (line in list) {
                val splitPos = line.indexOf(separatorList[1])
                val key = line.slice(0 until splitPos)
                val value = line.slice(splitPos + 1 until line.length)
                if (key != "") {
                    this[dataKey]?.set(key, value)
                }
            }
        } else {
            saveData(dataKey, filePath)
        }
    }

    fun loadData(){
        for (filePath in File(basePath).list()?: arrayOf()){
            parseData(filePath,"$basePath/$filePath")
        }
    }


    private fun checkDir(path: String): Boolean {
        var ret = true
        if (!File(path).exists()) {
            File(path).mkdir().also { ret = it }
        }
        return ret
    }

}