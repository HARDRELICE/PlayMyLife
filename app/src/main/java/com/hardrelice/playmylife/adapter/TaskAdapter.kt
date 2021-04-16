package com.hardrelice.playmylife.adapter

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.activity.EditTask
import com.hardrelice.playmylife.activity.ExecuteTask
import com.hardrelice.playmylife.utils.*
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    companion object {
        const val NORMAL_VIEW = 0
        const val HEADER_VIEW = 1
        const val FOOTER_VIEW = 2
        var useHeaderView = false
        var useFooterView = true
        var headerView: View? = null
        var footerView: View? = null
    }

    fun setHeaderView(view: View) {
        headerView = view
    }

    fun setFooterView(view: View) {
        footerView = view
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskItemAll: ConstraintLayout? = null
        var taskTitle: TextView? = null
        var taskDescription: TextView? = null
        var bottomLine: ConstraintLayout? = null
        var taskRewardList: RecyclerView? = null
        var deadlineTimeLeft: TextView? = null
        var deadlineTimeRight: TextView? = null
        var deadlineText: TextView? = null
        var useRight = false
        var taskDuration: TextView? = null
        var deleteButton: ImageButton? = null
        var editButton: ImageButton? = null
        var executeButton: ImageButton? = null
        var viewType: Int = NORMAL_VIEW

        init {
            if (itemView == headerView) {
                viewType = HEADER_VIEW
            } else if (itemView == footerView) {
                viewType = FOOTER_VIEW
            } else {
                taskItemAll = itemView.task_item_all
                taskTitle = itemView.task_title
                taskDescription = itemView.task_description
                bottomLine = itemView.bottom_line
                taskRewardList = itemView.task_reward_list
                deadlineTimeLeft = itemView.deadline_time_left
                deadlineTimeRight = itemView.deadline_time_right
                deadlineText = itemView.deadline_text
                useRight = ((deadlineText)!!.text == "Deadline")
                taskDuration = itemView.duration
                deleteButton = itemView.delete_button
                editButton = itemView.edit_button
                executeButton = itemView.execute_button
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return if (viewType == HEADER_VIEW && headerView != null) {
            TaskViewHolder(headerView!!)
        } else if (viewType == FOOTER_VIEW && footerView != null) {
            TaskViewHolder(footerView!!)
        } else {
            TaskViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.task_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount-1 -> FOOTER_VIEW
            else -> NORMAL_VIEW
        }

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        when (holder.viewType) {
            NORMAL_VIEW -> {

                val item = TaskManager.tasks[position]
                item.refreshCycle()

                val llm = LinearLayoutManager(ApplicationContext)
                llm.orientation = LinearLayoutManager.HORIZONTAL
                holder.taskRewardList!!.layoutManager = llm
                holder.taskRewardList!!.adapter = RewardAdapter(item.getTaskRewardList())

                holder.taskTitle!!.text = item.getTaskTitle()

                if (!item.canFinish()) {
                    holder.taskTitle!!.setTextColor(ColorStateList.valueOf(R.color.grey.getColor()))
                    holder.deadlineText!!.text = (item.getCycleLeftDays()+1).toString()
                    holder.deadlineTimeRight!!.text = R.string.days_small_cap.getString()
                } else {
                    holder.taskTitle!!.setTextColor(ColorStateList.valueOf(R.color.white.getColor()))
                }

                holder.taskDuration!!.text = if (item.getTaskHasDuration()) TimeController.seconds2string(item.getTaskDuration()) else ApplicationContext.resources.getString(R.string.no_duration)
                if (item.getTaskHasDeadLine()&&item.canFinish()) {
                    if (holder.useRight) {
                        holder.deadlineTimeRight!!.text =
                            TimeController.formatRightTime(item.getTaskDeadlineTime())
                    } else {
                        holder.deadlineTimeLeft!!.text =
                            TimeController.formatLeftTime(item.getTaskDeadlineTime())
                    }
                }

                holder.deleteButton!!.onSingleClick ({
                    TaskManager.removeTask(item.getTaskId())
                    TaskManager.tasks.removeAt(position)
                    this.notifyItemRemoved(position)
                    if (position != itemCount-1) {
                        notifyItemRangeChanged(position, itemCount - position -1)
                    }
//            (GlobalActivity.fragments[0] as TaskFragment).refresh()
                })

                holder.editButton!!.onSingleClick ({
                    val intent = Intent(ApplicationContext, EditTask::class.java)
                    intent.putExtra("id", item.getTaskId())
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    ApplicationContext.startActivity(intent)
                })

                holder.executeButton!!.onSingleClick ({
//                    if (item.getTaskHasDuration()) {
                        val intent = Intent(ApplicationContext, ExecuteTask::class.java)
                        intent.putExtra("position", position)
                        intent.flags = FLAG_ACTIVITY_NEW_TASK
                        ApplicationContext.startActivity(intent)
//                    } else {
//                        item.finishTask()
//                    }
                })
            }
            FOOTER_VIEW -> {

            }
            HEADER_VIEW -> {

            }
        }

    }

    override fun getItemCount(): Int {
        return TaskManager.tasks.size
    }
}