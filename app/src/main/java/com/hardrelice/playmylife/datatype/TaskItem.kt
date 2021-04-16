package com.hardrelice.playmylife.datatype

import com.hardrelice.playmylife.utils.*
import kotlin.collections.HashMap
import kotlin.random.Random

class TaskItem : HashMap<String, Any>() {

    companion object {
        const val SUCCESS = 0
        const val OVER_LIMIT = 1
    }

    init {
        val map = hashMapOf(
            "taskId" to 0L,
            "taskTitle" to "",
            "taskDescription" to "",
            "taskHasDeadLine" to false,
            "taskDeadlineTime" to 0.toLong(),
            "taskHasDuration" to false,
            "taskDuration" to 0,
            "taskHasNotification" to false,
            "taskNotificationTime" to 0.toLong(),
            "taskRewardList" to mutableListOf<Pair<String, Int>>(),
            "taskCycle" to 0,
            "taskCycleLimit" to 0,
            "taskCycleCount" to 0,
            "taskCycleStartDate" to "true",
            "taskExclusive" to false,
            "exclusiveWeekday" to mutableListOf<String>(),
            "exclusiveMonthDate" to mutableListOf<String>(),
            "exclusiveYearDate" to mutableListOf<String>(),
            "exclusiveSpecificDate" to mutableListOf<String>(),
            "lastFinishDate" to "",
            "tempFinishCount" to 0,
            "finishLog" to hashMapOf<String, MutableList<String>>(),
//            "" to "",
        )
        for (key in map.keys) {
            this[key] = map[key] as Any
        }
    }

    fun getTaskId() = this["taskId"] as Long
    fun getTaskTitle() = this["taskTitle"] as String
    fun getTaskDescription() = this["taskDescription"] as String
    fun getTaskHasDeadLine() = this["taskHasDeadLine"] as Boolean
    fun getTaskDeadlineTime() = this["taskDeadlineTime"] as Long
    fun getTaskHasDuration() = this["taskHasDuration"] as Boolean
    fun getTaskDuration() = this["taskDuration"] as Int
    fun getTaskHasNotification() = this["taskHasNotification"] as Boolean
    fun getTaskNotificationTime() = this["taskNotificationTime"] as Long
    fun getTaskRewardList() = this["taskRewardList"] as MutableList<Pair<String, Int>>
    fun getTaskCycle() = this["taskCycle"] as Int
    fun getTaskCycleLimit() = this["taskCycleLimit"] as Int
    fun getTaskCycleCount() = this["taskCycleCount"] as Int
    fun getTaskCycleStartDate() = this["taskCycleStartDate"] as String
    fun getTaskExclusive() = this["taskExclusive"] as Boolean
    fun getExclusiveWeekday() = this["exclusiveWeekday"] as MutableList<String>
    fun getExclusiveMonthDate() = this["exclusiveMonthDate"] as MutableList<String>
    fun getExclusiveYearDate() = this["exclusiveYearDate"] as MutableList<String>
    fun getExclusiveSpecificDate() = this["exclusiveSpecificDate"] as MutableList<String>
    fun getLastFinishDate() = this["lastFinishDate"] as String
    fun getTempFinishCount() = this["tempFinishCount"] as Int
    fun getFinishLog() = this["finishLog"] as HashMap<String, MutableList<String>>
    fun getFinishCount(): Int {
        var count = 0; for (date in getFinishLog()) {
            count += date.value.size
        }; return count
    }

    fun finishTask(): Int {
        // TO-DO
        if (getTaskCycle() != 0) {
            if (getTaskCycleCount() < getTaskCycleLimit() || getTaskCycleLimit() == 0) {
                this["taskCycleCount"] = getTaskCycleCount() + 1
            }
        }

        // RewardList
        for (pair in getTaskRewardList()) {
            if (pair.first == "") {
                CurrencyManager.addCurrency("点数", Random.nextInt(1, 201))
                CurrencyManager.addCurrency("心愿", Random.nextInt(1, 50))
                continue
            }
            if (pair.first in CurrencyManager.defaultCurrency.keys) {
                CurrencyManager.addCurrency(pair.first, pair.second)
            } else {
                PropManager.addPropCount(pair.first, pair.second)
            }
        }

        // Log
        val log = getFinishLog()
        val date = getFormattedDate().split(" ")
        if (log[date[0]] != null) {
            log[date[0]]!!.add(date[1])
        } else {
            log[date[0]] = mutableListOf(date[1])
        }

        this["finishLog"] = log
        println(date)
        TaskManager.saveTask(this)
        TaskManager.loadTasks()
        return SUCCESS
    }

    fun canFinish(): Boolean {
        if (this.getTaskCycle() != 0 && this.getTaskCycleLimit() != 0 && this.getTaskCycleLimit() <= this.getTaskCycleCount()) {
            return false
        }
        return true
    }

    fun refreshCycle() {
        val cycle = this.getTaskCycle()
        if (cycle == 0) return
        val startDate = this.getTaskCycleStartDate()
        val today = getToday()
        if (startDate.toBoolean()) {
            this["taskCycleStartDate"] = getToday()
            this["taskCycleCount"] = 0
        } else {
            val deltaDays = daysBetween(startDate, today)!!
            if (deltaDays >= this.getTaskCycle()) {
                this["taskCycleStartDate"] = addDaysToDate(today, -(deltaDays % cycle))!!
                this["taskCycleCount"] = 0
            }
        }
        TaskManager.saveTaskWithoutLoad(this)
    }

    fun getCycleLeftDays():Int{
        val cycle = this.getTaskCycle()
        if (cycle==0)return 0
        val startDate = this.getTaskCycleStartDate()
        val today = getToday()
        return if (startDate.toBoolean()) {
            this["taskCycleStartDate"] = getToday()
            this["taskCycleCount"] = 0
            0
        }else {
            val deltaDays = daysBetween(startDate, today)!!
            deltaDays % cycle
        }
    }
}