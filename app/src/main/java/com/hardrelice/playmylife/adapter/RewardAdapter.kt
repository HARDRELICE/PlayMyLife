package com.hardrelice.playmylife.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.utils.ApplicationContext
import com.hardrelice.playmylife.utils.IconManager
import kotlinx.android.synthetic.main.coin_p.view.*

class RewardAdapter(val rewards: MutableList<Pair<String,Int>>):RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {
    class RewardViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val rewardIcon:ImageView = itemView.reward_icon
        val rewardCount:TextView = itemView.reward_count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        return RewardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.coin_p, parent, false))
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewards[position]
        holder.rewardIcon.setImageDrawable(IconManager.getIconBitmap(reward.first)?.toDrawable(
            ApplicationContext.resources))
        holder.rewardCount.text = if (reward.first=="") "" else reward.second.toString()
    }

    override fun getItemCount() = rewards.size
}