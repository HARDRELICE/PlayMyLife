package com.hardrelice.playmylife.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardrelice.playmylife.R

class ShopAdapter() : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        return ShopViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.task_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}