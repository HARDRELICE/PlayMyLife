package com.hardrelice.playmylife.utils

import android.app.Activity
import android.content.SharedPreferences

object MigrateHelper {
    lateinit var sharedPreferences: SharedPreferences

    private const val settingName = "PMLConfig.migrateHelper"

    fun init() {
        sharedPreferences = ApplicationContext.getSharedPreferences(settingName, Activity.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("migrated", false)) {
            for (task in TaskManager.tasks) {
                if (task.getTaskTitle() != "") {
                    val id = task.getTaskId()
                    val newId = TaskManager.getTaskFile(id).lastModified()
                    task["taskId"] = newId
                    TaskManager.removeTask(id)
                    TaskManager.saveTaskWithoutLoad(task)
                }
            }
            val editor = sharedPreferences.edit()
            editor.putBoolean("migrated", true)
            editor.apply()
        }
    }
}