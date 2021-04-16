package com.hardrelice.playmylife.utils

import com.hardrelice.playmylife.datatype.GoodItem
import com.hardrelice.playmylife.utils.FileHandler.listDirs
import com.hardrelice.playmylife.utils.FileHandler.listDirsByDate
import com.hardrelice.playmylife.utils.FileHandler.listDirsById
import java.io.File
import kotlin.random.Random

object GoodManager {

    val goodsDir = FileHandler.goodsDir

    var goods = mutableListOf<GoodItem>()

    fun init() {
        loadGoods()
    }

    fun parseGoodContent(goodContent: String): GoodItem {
        val goodItem = GoodItem()
        for (line in goodContent.split('\n')) {
            if (line == "") break
            val splitPos = line.indexOf('|')
            val key = line.slice(0 until splitPos)
            val value = line.slice(splitPos + 1 until line.length)
            when (key) {
                "goodDuration", "goodCycle", "goodCycleLimit", "goodCycleCount", "goodPrice" -> {
                    goodItem[key] = value.toInt()
                }
                "goodId", "goodDeadlineTime", "goodNotificationTime", "goodCycleLastFinishDate" -> {
                    goodItem[key] = value.toLong()
                }
                "goodTitle", "goodDescription", "goodCurrency" -> {
                    goodItem[key] = value
                }
                "goodHasDeadLine", "goodHasDuration", "goodHasNotification", "goodExclusive" -> {
                    goodItem[key] = value.toBoolean()
                }
                "goodPriceList", "goodRewardList" -> {
                    val mutableList = mutableListOf<Pair<String, Int>>()
                    val prices = value.split(';')
                    for (price in prices) {
                        if (price == "") break
                        val pricePair = price.split(',')
                        mutableList.add(pricePair[0] to pricePair[1].toInt())
                    }
                    goodItem[key] = mutableList
                }
                "exclusiveWeekday", "exclusiveMonthDate", "exclusiveYearDate", "exclusiveSpecificDate" -> {
                    goodItem[key] = value.split(';')
                }

            }
        }
        return goodItem
    }

    fun loadGoods() {
        FileHandler.checkDir(goodsDir)
        goods.clear()
        for (fileId in goodsDir.listDirsById()) {
            val goodContent = getGoodFile(fileId).readText()
            goods.add(parseGoodContent(goodContent))
        }
        goods.add(GoodItem())
    }

    fun getGoodFile(goodId: Long): File {
        return File("$goodsDir/$goodId")
    }

    fun saveGood(goodId: Long, goodContent: String) {
        FileHandler.checkDir(goodsDir)
        getGoodFile(goodId).writeText(goodContent)
        loadGoods()
    }

    fun saveGood(goodItem: GoodItem) {
        FileHandler.checkDir(goodsDir)
        val goodId = goodItem.getGoodId()
        val goodContent = addGood(goodItem)
        getGoodFile(goodId).writeText(goodContent)
        loadGoods()
    }

    fun addGood(good: GoodItem): String {
        var string = ""
        for (key in good.keys) {
            if (key == "goodPriceList" || key == "goodRewardList") {
                string += "$key|"
                for (pair in (good[key] as MutableList<Pair<String, Int>>)) {
                    string += "${pair.first},${pair.second};"
                }
                string += "\n"
            } else {
                string = "$string$key|${good[key]}\n"
            }
        }
        return string
    }

    fun removeGood(goodId: Long) {
        getGoodFile(goodId).delete()
    }

    fun generateId(): Long {
        FileHandler.checkDir(goodsDir)
        return System.currentTimeMillis()
    }

}