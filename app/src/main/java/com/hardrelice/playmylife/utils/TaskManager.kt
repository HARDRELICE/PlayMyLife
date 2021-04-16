package com.hardrelice.playmylife.utils

import com.hardrelice.playmylife.datatype.TaskItem
import com.hardrelice.playmylife.utils.FileHandler.listDirs
import com.hardrelice.playmylife.utils.FileHandler.listDirsById
import java.io.File
import kotlin.random.Random

object TaskManager {

    val tasksDir = FileHandler.tasksDir

    var tasks = mutableListOf<TaskItem>()

    fun init() {
        loadTasks()
    }

    fun parseTaskContent(taskContent: String): TaskItem {
        val taskItem = TaskItem()
        for (line in taskContent.split('\n')) {
            if (line == "") break
            val splitPos = line.indexOf('|')
            val key = line.slice(0 until splitPos)
            val value = line.slice(splitPos + 1 until line.length)
            when (key) {
                "taskDuration", "taskCycle", "taskCycleCount", "taskCycleLimit", "tempFinishCount" -> {
                    taskItem[key] = value.toInt()
                }
                "taskId", "taskDeadlineTime", "taskNotificationTime" -> {
                    taskItem[key] = value.toLong()
                }
                "taskTitle", "taskDescription", "lastFinishDate", "taskCycleStartDate" -> {
                    taskItem[key] = value.toString().replace("\t", "\n")
                }
                "taskHasDeadLine", "taskHasDuration", "taskHasNotification", "taskExclusive" -> {
                    taskItem[key] = value.toBoolean()
                }
                "exclusiveWeekday", "exclusiveMonthDate", "exclusiveYearDate", "exclusiveSpecificDate" -> {
                    taskItem[key] = value.split(';')
                }
                "taskRewardList" -> {
                    val mutableList = mutableListOf<Pair<String, Int>>()
                    val rewards = value.split(';')
                    for (reward in rewards) {
                        if (reward == "") continue
                        val rewardPair = reward.split(',')
                        mutableList.add(rewardPair[0] to rewardPair[1].toInt())
                    }
                    taskItem[key] = mutableList
                }
                "finishLog" -> {
                    val hashMap = hashMapOf<String, MutableList<String>>()
                    val list = value.split(";")
                    for (item in list) {
                        if (item == "") continue
                        val pair = item.split("=")
                        val date = pair[0]
                        val dateLogs = pair[1].split("/")
                        hashMap[date] = dateLogs.toMutableList()
                    }
                    taskItem[key] = hashMap
                }
            }
        }
        return taskItem
    }

    fun loadTasks() {
        FileHandler.checkDir(tasksDir)
        tasks.clear()
        for (fileId in tasksDir.listDirsById()) {
            val taskContent = getTaskFile(fileId).readText()
            val item = parseTaskContent(taskContent)
            item.refreshCycle()
            tasks.add(item)
        }
        tasks.add(TaskItem())
    }

    fun refreshTasks() {
        for (task in tasks) {
            if (task.getTaskTitle() != "") task.refreshCycle()
        }
    }

    fun getTaskFile(taskId: Long): File {
        return File("$tasksDir/$taskId")
    }

    fun saveTask(taskId: Long, taskContent: String) {
        FileHandler.checkDir(tasksDir)
        getTaskFile(taskId).writeText(taskContent)
        loadTasks()
    }

    fun saveTask(taskItem: TaskItem) {
        FileHandler.checkDir(tasksDir)
        val taskId = taskItem.getTaskId()
        val taskContent = addTask(taskItem)
        getTaskFile(taskId).writeText(taskContent)
        loadTasks()
    }

    fun saveTaskWithoutLoad(taskItem: TaskItem) {
        FileHandler.checkDir(tasksDir)
        val taskId = taskItem.getTaskId()
        val taskContent = addTask(taskItem)
        getTaskFile(taskId).writeText(taskContent)
    }

    fun addTask(task: TaskItem): String {
        var string = ""
        for (key in task.keys) {
            string += "$key|"
            when (key) {
                "exclusiveWeekday", "exclusiveMonthDate", "exclusiveYearDate", "exclusiveSpecificDate" -> {
                    string += (task[key] as MutableList<String>).joinToString(";") + "\n"
                }
                "taskRewardList" -> {
                    for (pair in (task[key] as MutableList<Pair<String, Int>>)) {
                        string += "${pair.first},${pair.second};"
                    }
                    string += "\n"
                }
                "finishLog" -> {
                    for (item in task[key] as HashMap<String, MutableList<String>>) {
                        string += "${item.key}=${item.value.joinToString("/")};"
                    }
                    string += "\n"
                }
                else -> {
                    string += "${task[key].toString().replace("\n", "\t")}\n"
                }
            }
        }
        return string
    }

    fun removeTask(taskId: Long) {
        getTaskFile(taskId).delete()
    }

    fun generateIdOld(): Int {
        FileHandler.checkDir(tasksDir)
        val rand = Random(System.currentTimeMillis())
        var id = rand.nextInt()
        while (id.toString() in tasksDir.listDirs()) {
            id = rand.nextInt()
        }
        return id
    }

    fun generateId(): Long {
        return System.currentTimeMillis()
    }

}