package com.hardrelice.playmylife.activity

import android.content.res.ColorStateList
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.datatype.TaskItem
import com.hardrelice.playmylife.ui.task.TaskFragment
import com.hardrelice.playmylife.utils.*
import kotlinx.android.synthetic.main.activity_execute_task.*
import kotlinx.android.synthetic.main.activity_execute_task.execute_button
import kotlinx.android.synthetic.main.fragment_task.*
import java.text.FieldPosition
import kotlin.math.round

class ExecuteTask : AppCompatActivity() {
    companion object {
        const val ACTION_START = 0
        const val ACTION_PAUSE = 1
        const val ACTION_RESUME = 2
    }

    var action = ACTION_START
    var duration: Long = 10000
    var position:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_execute_task)
        position = intent.extras!!.getInt("position")
        val item = TaskManager.tasks[position]

        item.refreshCycle()

        execute_task_title.text = item.getTaskTitle()
        execute_task_description.text = item.getTaskDescription()
        cycle_finish_count.text = if(item.getTaskCycleLimit()!=0) "${item.getTaskCycleCount()}/${item.getTaskCycleLimit()}" else "${item.getTaskCycleCount()}"
        total_finish_count.text = "${item.getFinishCount()}"
        val hasDuration = item.getTaskHasDuration()
        if (hasDuration&&item.canFinish()) {
            val rawDuration = item.getTaskDuration().toLong()*1000
            duration = rawDuration
            var timeLeft = duration
            updateTime(timeLeft,true)
            var timer: CountDownTimer = object : CountDownTimer(duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft = millisUntilFinished
                    updateTime(timeLeft)
                }

                override fun onFinish() {
                    updateTime(0,true,100)
                    execute_button.setImageResource(R.drawable.ic_baseline_check_24)
                    execute_button.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.pixiv_blue))
                    execute_button.onSingleClick({
                        onFinishTask(item)
                    })
                    restart_button.isEnabled = false
                    restart_button.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.grey))
                }
            }
            execute_button.onSingleClick({
                when (action) {
                    ACTION_START -> {
                        execute_button.setImageResource(R.drawable.ic_baseline_pause_24)
                        timeLeft = duration
                        timer = object : CountDownTimer(duration, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                timeLeft = millisUntilFinished
                                updateTime(timeLeft)
                            }

                            override fun onFinish() {
                                updateTime(0,true,100)
                                execute_button.setImageResource(R.drawable.ic_baseline_check_24)
                                execute_button.backgroundTintList =
                                    ColorStateList.valueOf(resources.getColor(R.color.pixiv_blue))
                                execute_button.onSingleClick({
                                    onFinishTask(item)
                                })
                                restart_button.isEnabled = false
                                restart_button.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.grey))
                            }
                        }
                        timer.start()
                        action = ACTION_PAUSE
                    }
                    ACTION_PAUSE -> {
                        execute_button.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                        timer.cancel()
                        action = ACTION_RESUME
                    }
                    ACTION_RESUME -> {
                        execute_button.setImageResource(R.drawable.ic_baseline_pause_24)
                        timer = object : CountDownTimer(timeLeft, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                timeLeft = millisUntilFinished
                                updateTime(timeLeft)
                            }

                            override fun onFinish() {
                                updateTime(0,true,100)
                                execute_button.setImageResource(R.drawable.ic_baseline_check_24)
                                execute_button.backgroundTintList =
                                    ColorStateList.valueOf(resources.getColor(R.color.pixiv_blue))
                                execute_button.onSingleClick({
                                    onFinishTask(item)
                                })
                                restart_button.isEnabled = false
                                restart_button.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.grey))
                            }
                        }
                        timer.start()
                        action = ACTION_PAUSE
                    }
                }
            }, 100)

            restart_button.onSingleClick({
                execute_button.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                updateTime(duration)
                timer.cancel()
                action = ACTION_START
            }, 100)

            quit_button.onSingleClick({
                timer.cancel()
                this.finish()
            })

        } else {
            time_wrapper.removeAllViews()
            execute_button.setImageResource(R.drawable.ic_baseline_check_24)
            execute_button.backgroundTintList =
                ColorStateList.valueOf(R.color.pixiv_blue.getColor())
            if (item.canFinish()) {
                execute_button.onSingleClick({
                    onFinishTask(item)
                })
            }else{
                cycle_finish_count.text = "${item.getTaskCycleLimit()}/${item.getTaskCycleLimit()}"
                execute_button.isEnabled = false
                execute_button.backgroundTintList = ColorStateList.valueOf(R.color.white.getColor())
                execute_button.imageTintList = ColorStateList.valueOf(R.color.grey.getColor())
            }
            restart_button.isEnabled = false
            restart_button.imageTintList = ColorStateList.valueOf(R.color.grey.getColor())
            execute_task_progress_bar.progress = 100

            quit_button.onSingleClick({
                this.finish()
            })

        }
    }

    fun updateTime(time: Long, initial:Boolean=false, rawValue:Int = 0) {
        println(time)
        if (time<2000 && !initial){
            println("test")
            handler.postDelayed({
                println("post")
                updateTime(1000,true,((duration-1000)*100/duration).toInt())
            },time-1000)
        }
        val allSeconds = round(time.toFloat() / 1000).toInt()
        println(allSeconds)
        val seconds = (allSeconds % 60)
        val allMinutes = (allSeconds - seconds) / 60
        val minutes = (allMinutes % 60)
        val hours = ((allMinutes - minutes) / 60)
        SimpleTimeStructure(hours, minutes, seconds)
        if (time_left_hour.text != hours.formatDuo()) {
            time_left_hour.text = hours.formatDuo()
        }
        if (time_left_minute.text != minutes.formatDuo()) {
            time_left_minute.text = minutes.formatDuo()
        }
        if (time_left_second.text != seconds.formatDuo()) {
            time_left_second.text = seconds.formatDuo()
        }
        execute_task_progress_bar.progress = if (!initial) {
                (((duration - (allSeconds) * 1000).toFloat() / duration.toFloat()) * 100).toInt()
        } else {
            rawValue
        }
    }

    fun onFinishTask(item: TaskItem) {
        item.finishTask()
        if (!item.canFinish())(GlobalActivity.fragments[0] as TaskFragment).task_list.adapter!!.notifyItemChanged(position)
        MediaCenter.playAudio(R.raw.finish)
//
        handler.postDelayed({
            this.finish()
        },100)
    }

    fun Int.formatDuo(): String {
        val str = this.toString()
        return if (str.length < 2) {
            ("0" times 2 - str.length) + str
        } else {
            str.slice(0 until 2)
        }
    }

    data class SimpleTimeStructure(var hours: Int = 0, var minutes: Int = 0, var seconds: Int = 0)
}