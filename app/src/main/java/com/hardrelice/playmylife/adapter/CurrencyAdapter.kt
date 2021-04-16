package com.hardrelice.playmylife.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.activity.AddGood
import com.hardrelice.playmylife.utils.CurrencyManager
import com.hardrelice.playmylife.utils.IconManager
import com.hardrelice.playmylife.utils.onSingleClick
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrencyAdapter(var tempCurrency: AddGood.TempCurrency, val imageView: ImageView) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {


    val currency = CurrencyManager.stringMap.keys.toList()

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val allItemView = itemView
        val currencyIcon: ImageView = itemView.currency_icon
        val currencyText: TextView = itemView.currency_text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.currency_item,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyName = currency[position]
        holder.currencyIcon.setImageBitmap(IconManager.getIconBitmap(currencyName))
        holder.currencyText.text = CurrencyManager.getCurrencyNameLocal(currencyName)
        holder.allItemView.onSingleClick({
            tempCurrency.currency = currencyName
            imageView.setImageBitmap(IconManager.getIconBitmap(currencyName))
        })
    }

    override fun getItemCount(): Int {
        return currency.size
    }
}