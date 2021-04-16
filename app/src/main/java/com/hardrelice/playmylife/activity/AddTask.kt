package com.hardrelice.playmylife.activity

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.adapter.AddRewardAdapter
import com.hardrelice.playmylife.datatype.TaskItem
import com.hardrelice.playmylife.ui.task.TaskFragment
import com.hardrelice.playmylife.utils.*
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.add_reward_dialog.*
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.modify_duration_dialog.*


class AddTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val mInputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            if (source == "\n") "" else null
        }
        new_task_name_input.filters = arrayOf(mInputFilter)

        val rewards = mutableListOf("点数" to 0, "心愿" to 0)

//        for (key in PropManager.prop.keys){
//            if(key!="exist") rewards.add(key to 0)
//        }

        val llm = LinearLayoutManager(this.applicationContext)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        new_task_reward_input.layoutManager = llm
        new_task_reward_input.adapter = AddRewardAdapter(rewards, this)

        add_task_tool_bar_cancel_button.onSingleClick({
            this.window.decorView.closeKeyboard()
            handler.postDelayed({
                this.finish()
            }, 200)
        })

        add_duration_button.onSingleClick({
            val builder = AlertDialog.Builder(this, R.style.RoundDialogTheme)
            builder.setView(R.layout.modify_duration_dialog)
            val dialog = builder.create()
            dialog.show()
            val seekBars = listOf(
                dialog.modify_hour_seek_bar,
                dialog.modify_minute_seek_bar,
                dialog.modify_second_seek_bar
            )
            val textViews = listOf(
                dialog.time_left_hour,
                dialog.time_left_minute,
                dialog.time_left_second
            )
            for (index in 0 until 3) {
                val list = new_task_duration_input.text.toString().split(':')
                seekBars[index].progress = list[index].toInt()
                textViews[index].text = list[index]
                seekBars[index].setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        textViews[index].text = seekBar.progress.formatDuo()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        textViews[index].text = seekBar.progress.formatDuo()

                    }

                    override fun onProgressChanged(
                        seekBar: SeekBar, progress: Int,
                        fromUser: Boolean
                    ) {
                        textViews[index].text = seekBar.progress.formatDuo()
                        new_task_duration_input.text =
                            "${textViews[0].text}:${textViews[1].text}:${textViews[2].text}"
                    }
                })
            }
        })

        add_cycle_button.onSingleClick({
            val builder = AlertDialog.Builder(this, R.style.RoundDialogTheme)
            builder.setView(R.layout.add_reward_dialog)
            val dialog = builder.create()
            dialog.show()
            dialog.add_reward_seek_bar.max = 30
            dialog.add_reward_seek_bar.progress = new_task_cycle_input.text.toString().toInt()
            dialog.add_reward_count_text.text = new_task_cycle_input.text.toString()
            dialog.add_reward_seek_bar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    dialog.add_reward_count_text.text = seekBar.progress.toString()
                    new_task_cycle_input.text = seekBar.progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    dialog.add_reward_count_text.text = seekBar.progress.toString()
                    new_task_cycle_input.text = seekBar.progress.toString()
                }

                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    dialog.add_reward_count_text.text = seekBar.progress.toString()
                    new_task_cycle_input.text = seekBar.progress.toString()
                }
            })
        })

        add_task_tool_bar_ok_button.onSingleClick({
            val item = TaskItem()
            item["taskId"] = TaskManager.generateId()
            item["taskTitle"] = new_task_name_input.text.toString()
            item["taskDescription"] = new_task_description_input.text.toString()
            item["taskRewardList"] = rewards.removeEmpty()
            if (new_task_duration_input.text.toString() != "00:00:00") {
                item["taskDuration"] = calculateSeconds(new_task_duration_input.text.toString())
                item["taskHasDuration"] = true
            }
            if (new_task_cycle_input.text.toString() != "0") {
                item["taskCycle"] = new_task_cycle_input.text.toString().toInt()
                item["taskCycleStartDate"] = getToday()
                val limitString = new_task_cycle_limit_input.text.toString()
                item["taskCycleLimit"] = if (limitString != "") limitString.toInt() else 0
            }
            if (item.getTaskTitle() != "") {
                this.window.decorView.closeKeyboard()
                this.window.decorView.isEnabled = false
                TaskManager.saveTask(item)
                (GlobalActivity.fragments[0] as TaskFragment).task_list.adapter!!.notifyItemInserted(
                    TaskManager.tasks.size - 2
                )
                handler.postDelayed({
                    this.finish()
                }, 200)
            }
        })
    }

    fun Int.formatDuo(): String {
        val str = this.toString()
        return if (str.length < 2) {
            ("0" times 2 - str.length) + str
        } else {
            str.slice(0 until 2)
        }
    }

    fun calculateSeconds(formattedString: String): Int {
        val list = formattedString.split(':')
        return list[0].toInt() * 3600 + list[1].toInt() * 60 + list[2].toInt()
    }
}