package com.hardrelice.playmylife.ui.task

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.activity.AddTask
import com.hardrelice.playmylife.adapter.TaskAdapter
import com.hardrelice.playmylife.utils.ApplicationContext
import com.hardrelice.playmylife.utils.onSingleClick
import kotlinx.android.synthetic.main.fragment_task.view.*

class TaskFragment : Fragment() {

    private lateinit var homeViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task, container, false)

        // initial recycler view
        val adapter = TaskAdapter()
        root.task_list.layoutManager = LinearLayoutManager(this.activity)
        adapter.setFooterView(
            LayoutInflater.from(this.context).inflate(
                R.layout.item_footer,
                root.task_list,
                false
            )
        )
        root.task_list.adapter = adapter
        root.task_list.setHasFixedSize(false)
        // set listener on add task button
        root.add_task_button.onSingleClick({
            val intent = Intent(ApplicationContext, AddTask::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ApplicationContext.startActivity(intent)
        })
        return root
    }
}