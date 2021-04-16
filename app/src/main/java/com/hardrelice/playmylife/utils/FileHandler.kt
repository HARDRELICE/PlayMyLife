package com.hardrelice.playmylife.utils

import java.io.File

object FileHandler {
    const val storageRoot = "/storage/emulated/0/"

    val externalCacheDir = ApplicationContext.externalCacheDir?.absolutePath
    val filesDir = ApplicationContext.filesDir.absolutePath
    val tasksDir = "$filesDir/tasks/"
    val iconsDir = "$filesDir/icons/"
    val goodsDir = "$filesDir/goods/"

    fun String.listDirs(): Array<String> {
        return File(this).list() ?: arrayOf()
    }

    fun join(vararg dirs: String): String {
        var retDir = ""
        for (dir in dirs) {
            retDir += "/$dir"
        }
        return retDir
    }

    fun checkDir(path: String): Boolean {
        var ret = true
        if (!File(path).exists()) {
            File(path).mkdir().also { ret = it }
        }
        return ret
    }

    fun getBaseName(path: String): String {
        var pathArr = path.split('/')
        var pt = pathArr.size - 1
        while (pathArr[pt] == "") {
            pt -= 1
        }
        return pathArr[pt]
    }

    fun String.listDirsByDate(latestPriority: Boolean = true): List<String> {
        if (File(this).exists()) {
//            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US)
            val map = hashMapOf<Long, String>()
            val list = mutableListOf<Long>()
            for (file in this.listDirs()) {
                val lastModified = File("$this/$file").lastModified()
                map[lastModified] = file
                list.add(lastModified)
            }
            quickSort(list, 0, list.size - 1)
            val retList = mutableListOf<String>()
            for (lastModified in list) {
                map[lastModified]?.let { retList.add(it) }
            }
            println(retList)
            return if (latestPriority) retList else retList.reversed()
        }
        return listOf()
    }

    fun String.listDirsById(latestPriority: Boolean = true): List<Long> {
        if (File(this).exists()) {
//            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US)
            val map = hashMapOf<Long, String>()
            val list = mutableListOf<Long>()
            for (file in this.listDirs()) {
                list.add(file.toLong())
            }
            quickSort(list, 0, list.size - 1)
            return if (latestPriority) list else list.reversed()
        }
        return listOf()
    }
}