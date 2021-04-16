package com.hardrelice.playmylife.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.utils.*
import kotlinx.android.synthetic.main.add_reward_dialog.*
import kotlinx.android.synthetic.main.coin_p.view.*


class AddRewardAdapter(val rewards: MutableList<Pair<String, Int>>, val activity: Activity) :
    RecyclerView.Adapter<AddRewardAdapter.AddRewardViewHolder>() {
    class AddRewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rewardIcon: ImageView = itemView.reward_icon
        val rewardCount: TextView = itemView.reward_count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRewardViewHolder {
        return AddRewardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.coin_p, parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AddRewardViewHolder, position: Int) {
        holder.rewardIcon.setImageDrawable(
            IconManager.getIconBitmap(rewards[position].first)?.toDrawable(
                ApplicationContext.resources
            )
        )
        holder.rewardCount.text = rewards[position].second.toString()

        holder.rewardIcon.onSingleClick({
            val builder = AlertDialog.Builder(activity, R.style.RoundDialogTheme)
            builder.setView(R.layout.add_reward_dialog)
            val dialog = builder.create()
            dialog.show()
            dialog.add_reward_count_text.text = rewards[position].second.toString()
            dialog.add_reward_seek_bar.max = CurrencyManager.getRewardMax(rewards[position].first)
            dialog.add_reward_seek_bar.progress = rewards[position].second
            dialog.add_reward_seek_bar.setOnSeekBarChangeListener(object :
                OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    dialog.add_reward_count_text.text = seekBar.progress.toString()
                    rewards[position] = rewards[position].first to seekBar.progress
                    holder.rewardCount.text = rewards[position].second.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    dialog.add_reward_count_text.text = seekBar.progress.toString()
                    rewards[position] = rewards[position].first to seekBar.progress
                    holder.rewardCount.text = rewards[position].second.toString()
                }

                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    dialog.add_reward_count_text.text = seekBar.progress.toString()
                    rewards[position] = rewards[position].first to seekBar.progress
                    holder.rewardCount.text = rewards[position].second.toString()
                }
            })
        })

    }

    override fun getItemCount() = rewards.size
}
