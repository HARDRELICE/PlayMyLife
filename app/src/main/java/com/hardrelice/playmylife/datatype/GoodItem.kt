package com.hardrelice.playmylife.datatype

import com.hardrelice.playmylife.utils.CurrencyManager

class GoodItem:HashMap<String,Any>() {
    init {
        val map = hashMapOf(
            "goodId" to 0L,
            "goodTitle" to "",
            "goodDescription" to "",
            "goodHasDeadLine" to false,
            "goodDeadlineTime" to 0.toLong(),
            "goodHasDuration" to false,
            "goodDuration" to 0,
            "goodHasNotification" to false,
            "goodNotificationTime" to 0.toLong(),
            "goodRewardList" to mutableListOf<Pair<String, Int>>(),
            "goodCurrency" to "点数",
            "goodPrice" to 0,
            "goodCycle" to 0,
            "goodCycleLimit" to 0,
            "goodCycleCount" to 0,
            "goodCycleStartDate" to 0.toLong(),
            "goodExclusive" to false,
            "exclusiveWeekday" to "",
            "exclusiveMonthDate" to "",
            "exclusiveYearDate" to "",
            "exclusiveSpecificDate" to "",
        )
        for (key in map.keys) {
            this[key] = map[key] as Any
        }
    }

    fun getGoodId() = this["goodId"] as Long
    fun getGoodTitle() = this["goodTitle"] as String
    fun getGoodDescription() = this["goodDescription"] as String
    fun getGoodHasDeadLine() = this["goodHasDeadLine"] as Boolean
    fun getGoodDeadlineTime() = this["goodDeadlineTime"] as Long
    fun getGoodHasDuration() = this["goodHasDuration"] as Boolean
    fun getGoodDuration() = this["goodDuration"] as Int
    fun getGoodHasNotification() = this["goodHasNotification"] as Boolean
    fun getGoodNotificationTime() = this["goodNotificationTime"] as Long
    fun getGoodRewardList() = this["goodRewardList"] as MutableList<Pair<String, Int>>
    fun getGoodCurrency() = this["goodCurrency"] as String
    fun getGoodPrice() = this["goodPrice"] as Int
    fun getGoodCycle() = this["goodCycle"] as Int
    fun getGoodCycleLimit() = this["goodCycleLimit"] as Int
    fun getGoodCycleCount() = this["goodCycleCount"] as Int
    fun getGoodCycleStartDate() = this["goodCycleStartDate"] as Long
    fun getGoodExclusive() = this["goodExclusive"] as Boolean
    fun getExclusiveWeekday() = this["exclusiveWeekday"] as String
    fun getExclusiveMonthDate() = this["exclusiveMonthDate"] as String
    fun getExclusiveYearDate() = this["exclusiveYearDate"] as String
    fun getExclusiveSpecificDate() = this["exclusiveSpecificDate"] as String

    fun buyGood():Boolean{
        val goodCurrency = getGoodCurrency()
        val goodPrice = getGoodPrice()
        if (CurrencyManager.getCurrency(goodCurrency)==null){
            CurrencyManager.addCurrency(goodCurrency,0)
        }
        return if (CurrencyManager.getCurrency(goodCurrency)!!>=goodPrice){
            CurrencyManager.addCurrency(goodCurrency, -goodPrice)
            true
        } else{
            false
        }
    }

    fun canBuy():Boolean{
        return true
    }
}